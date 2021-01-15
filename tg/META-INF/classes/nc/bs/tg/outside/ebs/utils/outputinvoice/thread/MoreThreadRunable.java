package nc.bs.tg.outside.ebs.utils.outputinvoice.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.OutsideUtils;
import nc.vo.gl.fireport.FiAnaReportVO;
import nc.vo.hzvat.outputtax.AggOutputTaxHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.tg.outputtax.NcOutputtaxJsonVO;
import nc.vo.tg.outside.OutsideLogVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MoreThreadRunable extends SaleBillUtils  implements Runnable {
	HashMap<String, Object> map;

   public MoreThreadRunable(HashMap<String, Object> map){
	   this.map=map;
   }
   Boolean extraBill = false;//�Ƿ�Ʊ
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		byte[] sc = null;
		sc = EBSBillUtils.getUtils().token("ISqlThread:" + JSONObject.toJSONString(map),
				"moreOutputinvoice_RequiresNew");
//		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);
		ISqlThread service=NCLocator.getInstance().lookup(ISqlThread.class);
		String hdurl=null;
		try {
			hdurl = OutsideUtils.getOutsideInfo("SALEHD");//����ϵͳ�ص��ӿ�
		} catch (BusinessException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		List<HashMap<String,String>> hdlistmap=new ArrayList<HashMap<String,String>>();
		JSONArray jarr=(JSONArray)map.get("list");
		List<HashMap<String,String>>  returnlist=new ArrayList<HashMap<String,String>>();
 		for(int i=0;i<jarr.size();i++){
 			HashMap<String, String> hdmap=new HashMap<String,String>();//�ص��ӿڲ���map
			JSONObject value=jarr.getJSONObject(i);
		extraBill = false;
		String jsonstr = JSONObject.toJSONString(value.get("data"));
		NcOutputtaxJsonVO aggoJsonvo = JSONObject.parseObject(jsonstr,
				NcOutputtaxJsonVO.class);
		hdmap.put("vouchid", aggoJsonvo.getSaveSysBillid());//����ID 
		hdlistmap.add(hdmap);
		// ��������
		int actiontype = Integer.valueOf((String) value.get("actiontype"));
		try {
			AggOutputTaxHVO[] aggvos=service.moreOutputinvoice_RequiresNew(jsonstr, actiontype);
			if(actiontype==1||actiontype==2){
				for(CircularlyAccessibleValueObject  bvo: aggvos[0].getAllChildrenVO()){
					hdmap.put("billGuid",aggvos[0].getParentVO().getPrimaryKey());//NC��������
					hdmap.put("ncInvoiceCode",aggvos[0].getParentVO().getFpdm());//NC��Ʊ����
					hdmap.put("invoiceIssueDate",aggvos[0].getParentVO().getKprq()==null?null:aggvos[0].getParentVO().getKprq().toString());//��Ʊ��������
					hdmap.put("invoNO",aggvos[0].getParentVO().getFph());//��Ʊ����
					hdmap.put("pdfUrl", aggvos[0].getParentVO().getDef21());//���ӷ�Ʊ���ص�ַ
					hdmap.put("jkRate", bvo.getAttributeValue("Taxrate")!=null?bvo.getAttributeValue("Taxrate").toString():null);//��Ʊʱ˰��
					hdmap.put("jkTax",bvo.getAttributeValue("moneyintax")!=null?bvo.getAttributeValue("moneyintax").toString():null);//��Ʊʱ˰��
					hdmap.put("jkNoAmount",bvo.getAttributeValue("moneyouttax")!=null?bvo.getAttributeValue("moneyouttax").toString():null);//��Ʊʱ����˰���
				}
			}else{
				for(CircularlyAccessibleValueObject  bvo: aggvos[0].getAllChildrenVO()){
					hdmap.put("billGuid",aggvos[0].getParentVO().getPrimaryKey());//NC��������
					hdmap.put("ncInvoiceCode",aggvos[0].getParentVO().getFpdm());//NC��Ʊ����
					hdmap.put("invoiceIssueDate",aggvos[0].getParentVO().getKprq()==null?null:aggvos[0].getParentVO().getKprq().toString());//��Ʊ��������
					hdmap.put("invoNO",aggvos[0].getParentVO().getFph());//��Ʊ����
					hdmap.put("pdfUrl", aggvos[0].getParentVO().getDef21());//���ӷ�Ʊ���ص�ַ
					hdmap.put("jkRate", bvo.getAttributeValue("Taxrate")!=null?bvo.getAttributeValue("Taxrate").toString():null);//��Ʊʱ˰��
					hdmap.put("jkTax",bvo.getAttributeValue("moneyintax")!=null?bvo.getAttributeValue("moneyintax").toString():null);//��Ʊʱ˰��
					hdmap.put("jkNoAmount",bvo.getAttributeValue("moneyouttax")!=null?bvo.getAttributeValue("moneyouttax").toString():null);//��Ʊʱ����˰���
				}
			}
           } catch (Exception e) {
			HashMap<String,String> errmap = new HashMap<String,String>();
			errmap.put("SysBillid", aggoJsonvo.getSaveSysBillid());
			errmap.put("flag", "FAILED");
			errmap.put("msg", e.getMessage());
//			returnlist.add(errmap);
//			hdlistmap.add(hdmap);
//			throw new BusinessException(e.getMessage());
			returnlist.add(errmap);
		} 
	  }
 		String kpreturn=JSONArray.toJSONString(returnlist);
 		//������Ʊ�ص��ӿڵ���
 		OutsideLogVO logVO = new OutsideLogVO();
 		String str="��Ʊ��Ϣ"+kpreturn;
 		logVO.setDesbill("��Ʊnc->����ϵͳ�ص�");
 		if(hdlistmap!=null&&hdlistmap.size()>0){
 	 try{
 		String parm=JSON.toJSONString(hdlistmap);
 		
 		logVO.setSrcparm(str+"�ص�����"+parm);
 		logVO.setResult("1");
 		String hdjson=connectionjson(hdurl, parm);
 		if(hdjson!=null&&hdjson.length()>0){
 			JSONObject jobj=JSON.parseObject(hdjson);
 			boolean falg=jobj.getBoolean("success");
 			if(!falg) throw new BusinessException(jobj.getString("message"));
 		}
 		
 		 }catch(Exception e){
 			logVO.setResult("0"); 
 			logVO.setErrmsg(e.getMessage());
 		 }finally{
 			try {
 				EBSBillUtils.getUtils().restore(sc);
				service.billInsert_RequiresNew(logVO);
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
 		 }
 		}
	}
AggOutputTaxHVO aggvo = new AggOutputTaxHVO();

	
	/**
	 * 16����md5
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public String getmd5_16(String key) throws NoSuchAlgorithmException{
		// 1 ����һ���ṩ��ϢժҪ�㷨�Ķ��󣬳�ʼ��Ϊmd5�㷨����  
	    MessageDigest md = MessageDigest.getInstance("MD5");  

	    // 2 ����Ϣ���byte����  
	    byte[] input = key.getBytes();  

	    // 3 ��������ֽ�����,�������128λ��  
	    byte[] buff = md.digest(input);  

	    // 4 ������ÿһ�ֽڣ�һ���ֽ�ռ��λ������16��������md5�ַ���  
	    StringBuffer sb = new StringBuffer(buff.length);
	    String sTmp;
        int digtal;
	    for (int i = 0; i < buff.length; i++) {
	    	digtal=buff[i];
	    	if(digtal<0){
	    		digtal+=256;
	    	}
	    	if(digtal<16){sb.append("0");}
	    	
	      sb.append(Integer.toHexString(digtal));
    }
		 return sb.toString().toUpperCase();
	}
   public String connectionjson(String address,String data) throws BusinessException{
		  StringBuffer sb=new StringBuffer();
		  String responsecode=null;
		 String system= OutsideUtils.getOutsideInfo("Salesystem");
		 String key=OutsideUtils.getOutsideInfo("Salekey");
		try {
			 SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
			 String str=key+format.format(new Date().getTime())+system;
//			 String secretkey=MD5Util.getMD5(str);
			 String secretkey=getmd5_16(str);
			 address=address+"?system="+system+"&secretkey="+secretkey;
			 URL url = new URL(address);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("Charset", "UTF-8");
			
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.connect();
			if(data!=null){
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8"));
			writer.write(data);
			writer.flush();
			}
			String temp="";
			responsecode=String.valueOf(connection.getResponseCode());
			if(200==connection.getResponseCode()){
				BufferedReader br=new BufferedReader(new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8")));
		       while((temp=br.readLine())!=null){
			      sb.append(temp);
		          }
			}else{
				throw new BusinessException(responsecode);
			}
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new BusinessException("�����쳣 "+responsecode+"    "+e.getMessage());
		}
		  return sb.toString();
	  }

}
