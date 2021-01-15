package nc.bs.tg.alter.plugin.ebs;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;

import uap.serverdes.appesc.MD5Util;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pmpub.uap.util.ExceptionUtils;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISqlThread;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.uap.distribution.components.IBackgroundWork;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.outside.OutsideLogVO;
/*
 * 收款完成定时
 * */
public class GatheringFinishTask implements IBackgroundWorkPlugin{

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		// TODO 自动生成的方法存根
		PreAlertObject pobj=new PreAlertObject();
		pobj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
		StringBuffer errorsb=new StringBuffer();
		List<OutsideLogVO> listlog=new ArrayList<OutsideLogVO>();
		ISqlThread insert=NCLocator.getInstance().lookup(ISqlThread.class);
		String token = "";
		String url =PropertiesUtil.getInstance("ebs_url.properties").readValue("GATHERFINISHURL");//收款完成url
//			UFDateTime time = AppContext.getInstance().getServerTime();
//			String date = time.getYear() + time.getStrMonth()
//					+ time.getStrDay() + time.getLocalHour()
//					+ time.getLocalMinute();
//			token = date + MD5Util.getMD5(key);
		String syscode = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("EBSSYSTEM");
		String keya = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("KEY");
		if ("nc".equals(syscode)) {
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat(
					"yyyyMMddHHmm");// 年月日时分
			String time = formater.format(date);
			String tokenkey = time + keya;
			token = MD5Util.getMD5(tokenkey).toUpperCase();

		}
             try {
            	BaseDAO dao=new BaseDAO();
            	String sql="select a.billno as p_Collnumber , a.billdate as p_Colldate,a.local_money as p_Thisamount,f.vdef18  as p_Contractid,'100' as p_Sumamount,'50' as p_Unamount from ar_gatherbill a inner  join cmp_settlement t on  t.pk_busibill=a.pk_gatherbill inner join fct_ar f on f.pk_fct_ar=a.def21 where t.settlestatus='5' and nvl(a.dr,0)=0 and nvl(f.dr,0)=0 and t.def1 <> 'Y'";
            	List<HashMap<String,Object>> maplist=	(List<HashMap<String,Object>>)dao.executeQuery(sql, new MapListProcessor());
            	for(HashMap<String,Object> map:maplist){
            		 OutsideLogVO log= new OutsideLogVO();
       			  log.setDesbill("EBS收款完成回写");
       			  log.setResult("1");
       			 listlog.add(log);
            	HashMap<String,Object> datamap=new HashMap<String,Object>();
            	if(map!=null){
            		for(String key:map.keySet()){
            			if(map.get(key)!=null){
            			String[] strs=key.split("_");
            			datamap.put(strs[0]+"_"+Upper(strs[1]),map.get(key));
            			}
            		}//p_Unamountp_Collnumber
            	 String data=(String)getdata(datamap);
            	 log.setSrcparm(data);
            	  if(data!=null){
            	     String returndata=Httpconnectionutil.newinstance().connection(url+token,"&req="+data);
            	      if(returndata!=null){
            		  JSONObject jsonobj=JSONObject.parseObject(returndata);
            		  String code=(String)jsonobj.get("code");
            		  if(!("S".equalsIgnoreCase(code))){
            			  log.setResult("0");
            			  log.setErrmsg(jsonobj.getString("msg"));
            			  errorsb.append(jsonobj.get("msg"));
            			  pobj.setReturnObj(jsonobj.get("msg"));
            		  }else{
            			  BaseDAO updao=new BaseDAO();
            			 JSONObject jobj=  JSON.parseObject(data);
            			String billno= jobj.getString("p_Collnumber");
            			 String updatesql="update cmp_settlement set def1='Y' where pk_settlement in(select pk_settlement from cmp_settlement t inner join  ar_gatherbill a on a.pk_gatherbill = t.pk_busibill where a.billno='"+billno+"')";
            		  insert.insertsql_RequiresNew(updatesql);
            		  }
            	   }
            	 }
             }
            	}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				bgwc.setLogStr(e.getMessage());
				errorsb.append(e.getMessage());
				pobj.setReturnObj(e.getMessage());
				throw new BusinessException(e.getMessage());
			}finally{
				for(OutsideLogVO lvo:listlog){//处理报错日志
	            	insert.billInsert_RequiresNew(lvo);
	            	}
			}
		
		return pobj;
	}
  
	
public String getdata(HashMap<String,Object> map) throws Exception{
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
GsonBuilder gsonBuilder = new GsonBuilder();
for(String str:map.keySet()){
	if(map.get(str)!=null){
	if("p_Colldate".equals(str)){//收款日期截取日期
			UFDate uf=new UFDate(map.get("p_Colldate")+"");
			String s=uf.getYear()+"-"+uf.getMonth()+"-"+uf.getDay();
			map.put(str, s);
		}else if("p_Unamount".equals(str)||"p_Sumamount".equals(str)||"p_Thisamount".equals(str)){//金额转类型
			if(map.get(str) instanceof BigDecimal){
				map.put(str, ((BigDecimal)map.get(str)).doubleValue());
			}else{
		       map.put(str, new Double((String)map.get(str)));
			}
	  }
	}
  }
//     String jsonString = JSON.toJSONString(map);
	 String strh=gsonBuilder.serializeNulls().create().toJson(map);
	 
	return strh;
}
/*
 * 改变p_后面首字母为大写
 */
public String Upper(String str){
	char[] cs=str.toCharArray();
	cs[0]=(char)(cs[0]-32);
	return new String(cs);
}
}
