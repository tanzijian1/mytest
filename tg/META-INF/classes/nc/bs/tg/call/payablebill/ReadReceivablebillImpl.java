package nc.bs.tg.call.payablebill;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.zhsc.utils.ZhscRecbillConvertUtils;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;
import nc.vo.tg.outside.ReceivableBodyVO;
import nc.vo.tg.outside.ReceivableHeadVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gdata.util.common.base.Joiner;



public class ReadReceivablebillImpl extends TGImplCallHttpClient{
	private IPFBusiAction pfBusiAction = null;

	@Override
	public String onCallMethod(CallImplInfoVO info) throws BusinessException {
		String result = null;
		//用于回调给综合商城的单据ID
		String retSrcid = "";
		Map<String, String> resultInfo = new HashMap<String, String>();
		String token = null;
		List<String> iDFailList = new ArrayList<String>();
		List<Object> aggbillvos = new ArrayList<Object>();
		
		List<String> billnoList = new ArrayList<String>();
		//获取token
		try{
			String tokenUrl = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCTOKEN");
			//获取TOKEN接口的参数
			String paramsString = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCTOKENPARAMS");
			token = getPostToken(tokenUrl,paramsString);
			if(token == null){
				throw new BusinessException("获取token失败");
			}
		}catch(Exception e){
			throw new BusinessException("获取综合商城应收单：",e);
		}
		
		//请求数据URL
		JSONArray tempArr = null;
		int billsize = 0;
		try{
			String requestUrl = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCGETBILL");
			String requsetParams = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCRECPARAMS");
			result = getPostRequest(requestUrl, token, requsetParams);
			tempArr = (JSONArray)JSON.parse(result);
			
			if(tempArr.isEmpty() || tempArr.size() <=0){
				return null;
				//throw new BusinessException("暂无单据");
			}
			billsize = tempArr.size();
		}catch(Exception e){
			throw new BusinessException("获取综合商城应收单：",e);
		}
		
		
		for (int i = 0; i < tempArr.size(); i++) {
			String tempResult = tempArr.getString(i);
			JSONObject jsonData = JSON.parseObject(tempResult);// 表单数据
			String jsonhead = jsonData.getString("headInfo");// 外系统来源表头数据
			String jsonbody = jsonData.getString("bodyList");// 外系统来源表体数据
			if (jsonData == null || jsonhead == null || jsonbody == null) {
				throw new BusinessException("表单数据为空，请检查！json:" + jsonData);
			}
			// 转换json
			ReceivableHeadVO headVO = JSONObject.parseObject(jsonhead,
					ReceivableHeadVO.class);
			List<ReceivableBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
					ReceivableBodyVO.class);
			if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
				throw new BusinessException("表单数据转换失败，请检查！json:" + jsonData);
			}
			String methodname = info.getClassName();
			String srcid = headVO.getSrcid();// 外系统业务单据ID
			String srcno = headVO.getSrcbillno();// 外系统业务单据单据号

			String billqueue = methodname + ":" + srcid;
			String billkey = methodname + ":" + srcno;
			
			retSrcid = retSrcid + srcid + ",";
			
			AggReceivableBillVO billVO = (AggReceivableBillVO)BillUtils.getUtils().getBillVO(AggReceivableBillVO.class, "isnull(dr,0)=0 and def1 = '"+ srcid + "'");
			if(billVO != null){
				throw new BusinessException("【"+ srcid + "】,NC已存在对应的业务单据【"+ billVO.getParentVO().getAttributeValue(ReceivableBillVO.BILLNO) + "】,请勿重复上传!");
			}
			
			try {
				BillUtils.addBillQueue(billqueue);
				AggReceivableBillVO billvo = ZhscRecbillConvertUtils.getInstance().onTranBill(headVO, bodyVOs);
				/*HashMap eParam = new HashMap();
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,PfUtilBaseTools.PARAM_NOTE_CHECKED);*/
				Object obj = (AggReceivableBillVO[]) getPfBusiAction().processAction("SAVE", (String)billvo.getParentVO().getAttributeValue("pk_tradetype"), null, billvo, null, null);
				AggReceivableBillVO[] billvos = (AggReceivableBillVO[]) obj;
				billnoList.add((String) billvos[0].getParentVO().getAttributeValue(ReceivableBillVO.BILLNO));
				aggbillvos.add(obj);
				BillUtils.removeBillQueue(billqueue);
			} catch (Exception e) {
				iDFailList.add(srcid);
				BillUtils.removeBillQueue(billqueue);
				//throw new BusinessException("【" + billkey + "】," + e.getMessage(),e);
				e.printStackTrace();
			} 
		}
		
		//resultInfo.put("billvos", aggbillvos.toString());
		resultInfo.put("billsize",Integer.toString(billsize));
		resultInfo.put("billnos", Joiner.on(",").join(billnoList));
		
		try {
			String returnUrl = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCRETURN");
			String resultstrString ="";
			if(iDFailList.size()>0){
				resultstrString= "params={\"checkStatus\":\"1\",\"type\":\"1\",\"IDFails\":\""+StringUtils.join(iDFailList.toArray(), ",")+"\"}";
			}else {
				resultstrString= "params={\"checkStatus\":\"0\",\"type\":\"1\",\"IDFails\":\"\"}";
			}
			/*String resultStr = getPostReturnRequest(returnUrl, token, resultstrString);
			JSONObject dataStr = JSONObject.parseObject(resultStr);
            
            String resStatus = dataStr.getString("status");
			if(!"S".equals(resStatus)){
				throw new BusinessException("获取综合商城应收单后返回综合商城接收结果出错！单据ID为："+StringUtils.join(iDFailList.toArray(), ","));
			}*/
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return JSON.toJSONString(resultInfo);
	}
	
	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		CallImplInfoVO info = new CallImplInfoVO();
		info.setClassName(methodname);
		info.setDessystem(dessystem);

		return info;
	}
	


	/**
	 * 设置参数配置
	 * 
	 * @param conn
	 * @param info
	 * @throws ProtocolException
	 * @throws IOException
	 */
	protected void initConnParameter(HttpURLConnection conn, CallImplInfoVO info)
			throws ProtocolException, IOException {
		super.initConnParameter(conn, info);

	}
	
	protected String getResultInfo(HttpURLConnection conn, CallImplInfoVO info)
			throws BusinessException {
		String resultMsg = null;
		try {
			// 请求返回的状态
			String msg = "";
			if (conn.getResponseCode() == 200) {
				// 请求返回的数据
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), getEncoding()));
				String line = null;
				while ((line = in.readLine()) != null) {
					msg += line;
				}
				JSONObject result = JSON.parseObject(msg);
				String flag = (String) result.getString("success");
				if (!flag.equals("true")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("errorMessage") + "】");
				} else {
					resultMsg = result.getString("data");
				}
			} else {
				throw new BusinessException("连接失败");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}
	
	
	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	@Override
	protected String onBusinessProcessing(CallImplInfoVO info, String result) {
		return null;
	}
	
	/**
	 * 获取数据
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostReturnRequest(String requestUrl, String token,String param) throws Exception{
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// 初始化配置信息
		// 设置允许输出
		conn.setDoOutput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式(默认POST)
		conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", "utf-8");
		// 设置文件类型:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		//设置请求头
		conn.setRequestProperty("certificate", token);
		
		
		// 开始连接请求
		//输入请求参数
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(param);
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//相应成功，获得相应的数据
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //定义String类型用于储存单行数据  
            String line=null;  
            //创建StringBuffer对象用于存储所有数据  
            StringBuffer sb=new StringBuffer();  
            while((line=br.readLine())!=null){  
                sb.append(line);  
            }   
            resultMsg=sb.toString();
            
        }
		
		return resultMsg;
		
	}
	
	/**
	 * POST请求获取token
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostToken(String urlstr,String param) throws Exception{
		URL url = new URL(urlstr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// 初始化配置信息
		// 设置允许输出
		conn.setDoOutput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式(默认POST)
		conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", "utf-8");
		// 设置文件类型:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		// 转换为字节数组
		
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(param);
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//相应成功，获得相应的数据

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //定义String类型用于储存单行数据  
            String line=null;  
            //创建StringBuffer对象用于存储所有数据  
            StringBuffer sb=new StringBuffer();  
            while((line=br.readLine())!=null){  
                sb.append(line);  
            }   
            String str=sb.toString();
            
            JSONObject result = JSONObject.parseObject(str);
            resultMsg = result.getString("data");
            JSONObject datajson = JSONObject.parseObject(resultMsg);
            resultMsg = datajson.getString("certificate");
        }
		
		return resultMsg;
		
	}
	
	/**
	 * 获取数据
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostRequest(String requestUrl, String token,String param) throws Exception{
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// 初始化配置信息
		// 设置允许输出
		conn.setDoOutput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式(默认POST)
		conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", "utf-8");
		// 设置文件类型:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		//设置请求头
		conn.setRequestProperty("certificate", token);
		
		
		// 开始连接请求
		//输入请求参数
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(param);
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//相应成功，获得相应的数据
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //定义String类型用于储存单行数据  
            String line=null;  
            //创建StringBuffer对象用于存储所有数据  
            StringBuffer sb=new StringBuffer();  
            while((line=br.readLine())!=null){  
                sb.append(line);  
            }   
            String str=sb.toString();
            JSONObject result = JSONObject.parseObject(str);
            
            resultMsg = result.getString("data");
        }
		
		return resultMsg;
		
	}

}
