package nc.bs.tg.alter.plugin.ebs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.impl.tg.SaveLogImpl;
import nc.itf.tg.ISaveLog;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.generator.IdGenerator;
import nc.vo.fa.log.LogVO;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.NcToEbsLogVO;
import nc.vo.tg.pub.MedProjectVO;

public class MedProjectVOTask implements IBackgroundWorkPlugin{

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		// TODO 自动生成的方法存根
		NcToEbsLogVO logvo=new NcToEbsLogVO();
		SaveLogImpl save=NCLocator.getInstance().lookup(SaveLogImpl.class);
		logvo.setSrcsystem("中长期项目档案");
		try{
		BaseDAO dao=new BaseDAO();
		String url=PropertiesUtil.getInstance("ebs_url.properties").readValue("ZCURL");
		String appid="NC";//调用端编号
		IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class); 
		String appkey="10A12AE6-933A-4B87-876C-0539C3F0C098";// //密钥
		StringBuffer errorsb=new StringBuffer();
		long timeStampSec = System.currentTimeMillis()/1000;
	       String timestamp = String.format("%010d", timeStampSec);//时间戳
		String signStr="appId="+appid+"&timestamp="+timestamp+"&appkey="+appkey;
		String sign=MD5Util.getMD5(signStr).toUpperCase();
		String realurl=url+"?appId="+appid+"&timestamp="+timestamp+"&sign="+sign+"&pageIndex=0";
		String returnjson=connection(realurl);
		IdGenerator id=NCLocator.getInstance().lookup(IdGenerator.class);
		dao.executeUpdate("delete from med_project ");
		if(returnjson!=null){
			logvo.setNcparm(returnjson);
			JSONObject jobj=JSON.parseObject(returnjson);
	           if(jobj.getBoolean("IsSuccess")){
	        	  JSONArray jarr= jobj.getJSONArray("Data");
	        	  if(jarr!=null){
	        		  Iterator<Object> iter= jarr.iterator();
	        		 while(iter.hasNext()){
	        			 JSONObject dataobj=(JSONObject)iter.next(); 
	        			 MedProjectVO vo=JSON.parseObject(dataobj.toJSONString(), MedProjectVO.class);
	        			 dao.insertVO(vo);
	        		 }
	        	  }
	           }
		}
		}catch(Exception e){
		  logvo.setMsg(e.getMessage());
		}finally{
			save.SaveLog_RequiresNew(logvo);
		}
		return null;
	}
	/**
	 * GET方法连接
	 * @param address
	 * @return
	 * @throws BusinessException
	 */
	 public String connection(String address) throws BusinessException{
		 
		  StringBuffer sb=new StringBuffer();
		  String responsecode=null;
		try {
			 URL url = new URL(address);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(4000);
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.connect();
			String temp="";
			responsecode=String.valueOf(connection.getResponseCode());
			if(200==connection.getResponseCode()){
				BufferedReader br=new BufferedReader(new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8")));
		       while((temp=br.readLine())!=null){
			      sb.append(temp);
		          }
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			throw new BusinessException("连接异常 "+responsecode+"    "+e.getMessage());
		}
		  return sb.toString();
	  }
}
