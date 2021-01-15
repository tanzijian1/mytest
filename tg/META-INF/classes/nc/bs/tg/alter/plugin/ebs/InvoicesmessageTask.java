package nc.bs.tg.alter.plugin.ebs;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISqlThread;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.tg.outside.OutsideLogVO;
/*
 *��Ʊ��Ϣ��ʱ����
 * */
public class InvoicesmessageTask implements IBackgroundWorkPlugin{
private StringBuffer sb=new StringBuffer();
	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		StringBuffer errorsb=new StringBuffer();
		PreAlertObject pobj=new PreAlertObject();
		pobj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
		ISqlThread insert=NCLocator.getInstance().lookup(ISqlThread.class);
		OutsideLogVO logvo=new OutsideLogVO();
		logvo.setDesbill("EBS��Ʊ��Ϣ��ʱ����");
		logvo.setResult("1");
		String token = "";
		String syscode = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("EBSSYSTEM");
		String key = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("KEY");
		if ("nc".equals(syscode)) {
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat(
					"yyyyMMddHHmm");// ������ʱ��
			String time = formater.format(date);
			String tokenkey = time + key;
			token = MD5Util.getMD5(tokenkey).toUpperCase();

		}
//			UFDateTime time = AppContext.getInstance().getServerTime();
//			String date = time.getYear() + time.getStrMonth()
//					+ time.getStrDay() + time.getLocalHour()
//					+ time.getLocalMinute();
//			token = date + MD5Util.getMD5(key);
		try {
		String data=	getData();
		logvo.setSrcparm(data);
			String testurl=PropertiesUtil.getInstance("ebs_url.properties").readValue("FPURL");
        	String returndata=Httpconnectionutil.newinstance().connection(testurl+token, "&req="+data);
        	  if(returndata!=null){
        		  JSONObject jsonobj=JSONObject.parseObject(returndata);
        		  String code=(String)jsonobj.get("code");
        		  if(!("S".equalsIgnoreCase(code))){
        			 errorsb.append(jsonobj.get("msg"));
        			 logvo.setResult("0");
        			 logvo.setErrmsg(jsonobj.getString("msg"));
        		  }else{
        			  BaseDAO dao=new BaseDAO();
        			  String insql=sb.toString().substring(0,sb.length()-1)+")";
        			  String filtersql="update hzvat_invoice_b b set b.def10='Y' where b.pk_invoice_b "+insql+"";
        			  insert.insertsql_RequiresNew(filtersql);
        		  }
        	}
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			errorsb.append(e.getMessage());
			logvo.setResult("0");
			 logvo.setErrmsg(e.getMessage());
			pobj.setReturnObj(errorsb);
			bgwc.setLogStr(errorsb.toString());
			e.printStackTrace();
		}finally{
			insert.billInsert_RequiresNew(logvo);
		}
	if(errorsb.length()>0){
		bgwc.setLogStr(errorsb.toString());
	}
	return null;
	}
public String getData() throws Exception{
	List<HashMap< String, Object>> list=new ArrayList<HashMap< String, Object>>();
	Map<String,List<HashMap< String, Object>>> returnmap=new HashMap<String,List<HashMap< String, Object>>>();//h.costmoney as p_Excludetaxamount//htbh
	String sql="select h.def3,b.pk_invoice_b,h.Fph,h.Fpdm,h.Kprq,h.sellname,h.pk_org,h.fplx,b.Taxrate,b.Moneyintax,b.Moneytax,b.moneyouttax  from hzvat_invoice_h h inner join hzvat_invoice_b b on b.pk_invoice_h=h.pk_invoice_h where  h.def3 in (select  a.def11  from ap_paybill  a    inner join cmp_settlement t  on  t.pk_busibill=a.pk_paybill  where a.pk_tradetype	='F3-Cxx-007' and t.settlestatus='5') and h.def3  is not null and h.def3 !='~' and nvl(h.dr,0)=0 and  nvl(b.def10,'N') <> 'Y'";
	BaseDAO dao=new BaseDAO();
	sb.append("in (");
	List<HashMap< String, Object>> maplist=(List<HashMap< String, Object>>)dao.executeQuery(sql, new MapListProcessor());
	for(HashMap< String, Object> map:maplist){
		HashMap< String, Object> datamap=new HashMap<String,Object>();
		if(map.get("def3")!=null)
		datamap.put("p_Conid",Long.valueOf((String)map.get("def3")));// ��ͬ��ID
		sb.append("'"+map.get("pk_invoice_b")+"',");
		datamap.put("p_Invoicelineid", map.get("pk_invoice_b"));//��Ʊ��ID
		datamap.put("p_Invoicenumber", map.get("fph"));//��Ʊ���
		datamap.put("p_Invoicecode", map.get("fpdm"));//��Ʊ����
		if(map.get("kprq")!=null){
		UFDate uf=new UFDate(map.get("kprq")+"");
		String s=uf.getYear()+"-"+uf.getMonth()+"-"+uf.getDay();
		datamap.put("p_Invoicedate", s);//��Ʊ����
		}
		datamap.put("p_Drawerparty", map.get("sellname"));//��Ʊ��
		datamap.put("p_Receivingparty", map.get("pk_org"));//��Ʊ��
		datamap.put("p_Billtype", map.get("fplx"));//��Ʊ����
		datamap.put("p_Taxrate", String.valueOf(map.get("taxrate")));//˰��
		datamap.put("p_Rateamount", map.get("moneyintax"));//��˰���
		datamap.put("p_Taxamount", map.get("moneytax"));//˰��
		datamap.put("p_Excludetaxamount", map.get("moneyouttax"));//����˰���
		list.add(datamap);
	}
//	List<HashMap< String, Object>> lsitreturn=changetype(list);
	returnmap.put("INVOICE_LIST",list);
	
	return JSON.toJSONString(returnmap);
}

/*
 * �ı�p_��������ĸΪ��д
 */
public String Upper(String str){
	char[] cs=str.toCharArray();
	cs[0]=(char)(cs[0]-32);
	return new String(cs);
}

/*
 * ת����
 * */
public List<HashMap< String, Object>> changetype(List<HashMap< String, Object>> maplist){
//	List<HashMap< String, Object>> list=new ArrayList<HashMap< String, Object>>();
	for(HashMap< String, Object> map:maplist){
		for(String str:map.keySet()){
			 if("p_Taxamount".equals(str)||"p_Rateamount".equals(str) || "p_Conid".equals(str)){//���ת����
					if(map.get(str) instanceof BigDecimal){
						map.put(str, ((BigDecimal)map.get(str)).doubleValue());
					}else if(map.get(str) instanceof String){
						map.put(str, new Double((String)map.get(str)));
					}
		}
      }	
   }
	return maplist;
}
}
