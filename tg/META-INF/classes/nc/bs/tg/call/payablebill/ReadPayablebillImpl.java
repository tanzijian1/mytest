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
		
		//��ȡTOKEN
		try {
			String tokenUrl = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCTOKEN");
			//��ȡTOKEN�ӿڵĲ���
			String paramsString = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCTOKENPARAMS");
			token = getPostToken(tokenUrl,paramsString);
			if(token == null){
				throw new BusinessException("��ȡtokenʧ��");
			}
		} catch (Exception e) {
			throw new BusinessException("��ȡtokenʧ��",e);
		}
			
		//��������URL
		try {
			String requsetParams = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCPAYPARAMS");
			String requestUrl = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCGETBILL");
			result = getPostRequest(requestUrl, token, requsetParams);
            result = result.substring(1, result.length()-1);
            if(StringUtils.isBlank(result)){
            	return null;
            	//throw new BusinessException("��������");
            }
		} catch (Exception e) {
			throw new BusinessException("��ȡ�ۺ��̳�Ӧ������",e);
		}	
		//result = "{\"headInfo\":{\"pk_org\":\"CS1-0001\",\"billdate\":\"2020-08-04\",\"money\":10.00,\"srcid\":\"29589112\",\"srcbillno\":\"100103025.01-020112\",\"local_money\":10.00000000,\"rate\":1.0,\"Hyperlinks\":\"http://www.baidu.com\",\"pk_tradetype\":\"F1-Cxx-LL07\",\"pk_tradetypeid\":\"����-Ӧ����-�ۺ��̳�Ӧ���ɱ���\",\"businesstype\":\"Ӧ����Ӧ�̳ɱ���\",\"pk_currtype\":\"CNY\",\"billstatus\":2,\"pk_group\":\"000112100000000005FD\",\"pk_billtype\":\"F1\",\"billclass\":\"yf\",\"syscode\":\"1\",\"billmaker\":\"LLZHSC\",\"objtype\":1},\"bodyList\":[{\"scomment\":\"����-Ӧ����-�ۺ��̳�Ӧ���ɱ���\",\"customer\":\"100253\",\"quantity_cr\":1,\"taxrate\":0.1,\"taxprice\":5.1,\"local_tax_cr\":0.1,\"notax_cr\":0.0,\"money_cr\":5.0,\"money_bal\":5.00000000,\"rate\":1.0,\"local_money_bal\":5.0,\"local_money_cr\":5.00000000,\"local_notax_cr\":5.00000000,\"rowid\":50000000000001,\"ratio\":0.2,\"Amountdue\":1,\"memo\":\"���Ա����ۺ��̳�Ӧ����1\"},{\"scomment\":\"����-Ӧ����-�ۺ��̳�Ӧ�����˿�\",\"customer\":\"100253\",\"quantity_cr\":1,\"taxrate\":0.1,\"taxprice\":5.1,\"local_tax_cr\":0.1,\"notax_cr\":0.0,\"money_cr\":5.0,\"money_bal\":5.00000000,\"rate\":1.0,\"local_money_bal\":5.00000000,\"local_money_cr\":5.00000000,\"local_notax_cr\":5.0,\"rowid\":50000000000002,\"ratio\":0.2,\"Amountdue\":1,\"memo\":\"���Ա����ۺ��̳�Ӧ����2\"}]}";    
	            
		//������Ϣ����ȡ��ͷ������
		JSONObject jsonobj = JSONObject.parseObject(result);
		String jsonhead = jsonobj.getString("headInfo");// ��ϵͳ��Դ��ͷ����
		String jsonbody = jsonobj.getString("bodyList");// ��ϵͳ��Դ��������
				
		//ת����ͷ�������ֶ�
		PayableHeadVO headVO = JSONObject.parseObject(jsonhead, PayableHeadVO.class);
		List<PayableBodyVO> bodyVOs = JSONObject.parseArray(jsonbody, PayableBodyVO.class);
		
		//У���ֶ���Ϣ
		if(headVO != null){
			if (bodyVOs == null || bodyVOs.size() < 1) {
				throw new BusinessException("������ת��ʧ�ܣ����飡json:" + result);
			}
			//�жϵ����Ƿ��Ѵ���
			srcid = headVO.getSrcid();// ��ϵͳҵ�񵥾�ID
			retSrcid = retSrcid + srcid + ",";
			
			AggPayableBillVO billVO = (AggPayableBillVO)BillUtils.getUtils().getBillVO(AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '"+ srcid + "'");
			
			if(billVO != null){
				throw new BusinessException("��"+ srcid + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"+ billVO.getParentVO().getAttributeValue(ReceivableBillVO.BILLNO) + "��,�����ظ��ϴ�!");
			}
			
			//У�����Ӧ������
			if(StringUtils.isBlank(headVO.getPk_tradetype()) || !"F1-Cxx-LL07".equals(headVO.getPk_tradetype())){
				throw new BusinessException("����ID��"+srcid+"��Ӧ�����ͱ��룺"+ headVO.getPk_tradetype() +"���󣬲�������Ᵽ�������");
			}
		}else {
			throw new BusinessException("������ת��ʧ�ܣ����飡json:" + result);
		}
		
		String methodname = info.getClassName();
		billqueue = methodname + ":" + srcid;
		
		
		//ת����Ϣ
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
			//֪ͨ�ۺ��̳ǣ������Ƿ����ɹ�
			BillUtils.removeBillQueue(billqueue);
			//�����У�鲻ͨ������У�鲻ͨ���ĵ���ID����
			String flagstr = resultInfo.get("IDFails");
			
			//���������� ZHSCRETURN
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
					throw new BusinessException("��ȡ�ۺ��̳�Ӧ�����󷵻��ۺ��̳ǽ��ս����������IDΪ��"+flagstr);
				}
			} catch (Exception e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}		
		return JSON.toJSONString(resultInfo);
	}

	/**
	 * ƴ��������Ϣ
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
		// TODO �Զ����ɵķ������
		return result;
	}
	
	/**
	 * ��ȡ����
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostReturnRequest(String requestUrl, String token,String param) throws Exception{
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// ��ʼ��������Ϣ
		// �����������
		conn.setDoOutput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ(Ĭ��POST)
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// �����ļ�����:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		//��������ͷ
		conn.setRequestProperty("certificate", token);
		
		
		// ��ʼ��������
		//�����������
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(param);
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//��Ӧ�ɹ��������Ӧ������
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //����String�������ڴ��浥������  
            String line=null;  
            //����StringBuffer�������ڴ洢��������  
            StringBuffer sb=new StringBuffer();  
            while((line=br.readLine())!=null){  
                sb.append(line);  
            }   
            resultMsg=sb.toString();
            
        }
		
		return resultMsg;
		
	}
	
	/**
	 * POST�����ȡtoken
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostToken(String urlstr,String param) throws Exception{
		URL url = new URL(urlstr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// ��ʼ��������Ϣ
		// �����������
		conn.setDoOutput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ(Ĭ��POST)
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// �����ļ�����:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		// ת��Ϊ�ֽ�����
		
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(param);
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//��Ӧ�ɹ��������Ӧ������

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //����String�������ڴ��浥������  
            String line=null;  
            //����StringBuffer�������ڴ洢��������  
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
	 * ��ȡ����
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostRequest(String requestUrl, String token,String param) throws Exception{
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// ��ʼ��������Ϣ
		// �����������
		conn.setDoOutput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ(Ĭ��POST)
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// �����ļ�����:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		//��������ͷ
		conn.setRequestProperty("certificate", token);
		
		
		// ��ʼ��������
		//�����������
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(param);
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//��Ӧ�ɹ��������Ӧ������
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //����String�������ڴ��浥������  
            String line=null;  
            //����StringBuffer�������ڴ洢��������  
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
