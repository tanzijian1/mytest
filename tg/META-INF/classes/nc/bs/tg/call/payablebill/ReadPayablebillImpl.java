package nc.bs.tg.call.payablebill;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.call.vo.PayableBodyVO;
import nc.bs.tg.call.vo.PayableHeadVO;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.zhsc.utils.ZhscPaybillConvertUtils;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ReadPayablebillImpl extends TGImplCallHttpClient {
	private IPFBusiAction pfBusiAction = null;
	
	@Override
	public String onCallMethod(CallImplInfoVO info) throws BusinessException {
		String result = null;
		Map<String, String> resultInfo = new HashMap<String, String>();
		
		String retSrcid = "";
		String token = null;
		String srcid =  null;
		String billqueue = null;
		
		//获取TOKEN
		try {
			String tokenUrl = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCTOKEN");
			//获取TOKEN接口的参数
			String paramsString = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCTOKENPARAMS");
			token = getPostToken(tokenUrl,paramsString);
			if(token == null){
				throw new BusinessException("获取token失败");
			}
		} catch (Exception e) {
			throw new BusinessException("获取token失败",e);
		}
			
		//请求数据URL
		try {
			String requsetParams = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCPAYPARAMS");
			String requestUrl = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCGETBILL");
			result = getPostRequest(requestUrl, token, requsetParams);
            result = result.substring(1, result.length()-1);
            if(StringUtils.isBlank(result)){
            	return null;
            	//throw new BusinessException("暂无数据");
            }
		} catch (Exception e) {
			throw new BusinessException("获取综合商城应付单：",e);
		}	
		//result = "{\"headInfo\":{\"pk_org\":\"CS1-0001\",\"billdate\":\"2020-08-04\",\"money\":10.00,\"srcid\":\"29589112\",\"srcbillno\":\"100103025.01-020112\",\"local_money\":10.00000000,\"rate\":1.0,\"Hyperlinks\":\"http://www.baidu.com\",\"pk_tradetype\":\"F1-Cxx-LL07\",\"pk_tradetypeid\":\"邻里-应付单-综合商城应付成本款\",\"businesstype\":\"应付供应商成本款\",\"pk_currtype\":\"CNY\",\"billstatus\":2,\"pk_group\":\"000112100000000005FD\",\"pk_billtype\":\"F1\",\"billclass\":\"yf\",\"syscode\":\"1\",\"billmaker\":\"LLZHSC\",\"objtype\":1},\"bodyList\":[{\"scomment\":\"邻里-应付单-综合商城应付成本款\",\"customer\":\"100253\",\"quantity_cr\":1,\"taxrate\":0.1,\"taxprice\":5.1,\"local_tax_cr\":0.1,\"notax_cr\":0.0,\"money_cr\":5.0,\"money_bal\":5.00000000,\"rate\":1.0,\"local_money_bal\":5.0,\"local_money_cr\":5.00000000,\"local_notax_cr\":5.00000000,\"rowid\":50000000000001,\"ratio\":0.2,\"Amountdue\":1,\"memo\":\"测试保存综合商城应付单1\"},{\"scomment\":\"邻里-应付单-综合商城应付分账款\",\"customer\":\"100253\",\"quantity_cr\":1,\"taxrate\":0.1,\"taxprice\":5.1,\"local_tax_cr\":0.1,\"notax_cr\":0.0,\"money_cr\":5.0,\"money_bal\":5.00000000,\"rate\":1.0,\"local_money_bal\":5.00000000,\"local_money_cr\":5.00000000,\"local_notax_cr\":5.0,\"rowid\":50000000000002,\"ratio\":0.2,\"Amountdue\":1,\"memo\":\"测试保存综合商城应付单2\"}]}";    
	            
		//处理信息，获取表头、表体
		JSONObject jsonobj = JSONObject.parseObject(result);
		String jsonhead = jsonobj.getString("headInfo");// 外系统来源表头数据
		String jsonbody = jsonobj.getString("bodyList");// 外系统来源表体数据
				
		//转换表头、表体字段
		PayableHeadVO headVO = JSONObject.parseObject(jsonhead, PayableHeadVO.class);
		List<PayableBodyVO> bodyVOs = JSONObject.parseArray(jsonbody, PayableBodyVO.class);
		
		//校验字段信息
		if(headVO != null){
			if (bodyVOs == null || bodyVOs.size() < 1) {
				throw new BusinessException("表单数据转换失败，请检查！json:" + result);
			}
			//判断单据是否已存在
			srcid = headVO.getSrcid();// 外系统业务单据ID
			retSrcid = retSrcid + srcid + ",";
			
			AggPayableBillVO billVO = (AggPayableBillVO)BillUtils.getUtils().getBillVO(AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '"+ srcid + "'");
			
			if(billVO != null){
				throw new BusinessException("【"+ srcid + "】,NC已存在对应的业务单据【"+ billVO.getParentVO().getAttributeValue(ReceivableBillVO.BILLNO) + "】,请勿重复上传!");
			}
			
			//校验表体应付类型
			if(StringUtils.isBlank(headVO.getPk_tradetype()) || !"F1-Cxx-LL07".equals(headVO.getPk_tradetype())){
				throw new BusinessException("单据ID："+srcid+"的应付类型编码："+ headVO.getPk_tradetype() +"错误，不进行入库保存操作！");
			}
		}else {
			throw new BusinessException("表单数据转换失败，请检查！json:" + result);
		}
		
		String methodname = info.getClassName();
		billqueue = methodname + ":" + srcid;
		
		
		//转存信息
		try{
			BillUtils.addBillQueue(billqueue);
			AggPayableBillVO billvo = ZhscPaybillConvertUtils.getInstance().onTranBill(headVO, bodyVOs);
			/*HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED, PfUtilBaseTools.PARAM_NOTE_CHECKED);*/
			Object obj =  (AggPayableBillVO[]) getPfBusiAction().processAction("SAVE", "F1-Cxx-LL07", null, billvo, null, null);
			
			AggPayableBillVO[] billvos = (AggPayableBillVO[])obj;
			
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO().getAttributeValue(ReceivableBillVO.BILLNO));
		}catch(Exception e2){
			resultInfo.put("IDFails", retSrcid.substring(0, retSrcid.length()-1));
			throw new BusinessException(e2.getMessage());
		}finally{
			//通知综合商城，单据是否入库成功
			BillUtils.removeBillQueue(billqueue);
			//如果是校验不通过，则将校验不通过的单据ID返回
			String flagstr = resultInfo.get("IDFails");
			
			//返回请求结果 ZHSCRETURN
			String returnUrl = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCRETURN");
			String resultstrString= "";
			if(StringUtils.isNotBlank(flagstr)){
				resultstrString= "params={\"checkStatus\":\"1\",\"type\":\"2\",\"IDFails\":\""+flagstr+"\"}";
			}else {
				resultstrString= "params={\"checkStatus\":\"0\",\"type\":\"2\",\"IDFails\":\""+flagstr+"\"}";
			}
			try {
				String resultStr = getPostReturnRequest(returnUrl, token, resultstrString);
				JSONObject dataStr = JSONObject.parseObject(resultStr);
	            
	            String resStatus = dataStr.getString("status");
				if(!"S".equals(resStatus)){
					throw new BusinessException("获取综合商城应付单后返回综合商城接收结果出错！单据ID为："+flagstr);
				}
			} catch (Exception e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}		
		return JSON.toJSONString(resultInfo);
	}

	/**
	 * 拼接请求信息
	 */
	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		CallImplInfoVO info = new CallImplInfoVO();
		info.setClassName(methodname);
		info.setDessystem(dessystem);

		return info;
	}
	


	@Override
	protected String onBusinessProcessing(CallImplInfoVO info, String result) {
		// TODO 自动生成的方法存根
		return result;
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
	
	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

}
