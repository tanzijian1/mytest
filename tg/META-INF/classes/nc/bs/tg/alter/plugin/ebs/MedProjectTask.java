package nc.bs.tg.alter.plugin.ebs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.ufida.web.html.Map;

import uap.serverdes.appesc.MD5Util;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.generator.IdGenerator;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pmpub.project.ProjectBillVO;
import nc.vo.pmpub.project.ProjectHeadVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.tg.pub.MedProjectVO;

/**
 * 中长期项目定时任务
 * @author yy
 *
 */
public class MedProjectTask implements IBackgroundWorkPlugin{
	private BaseDAO dao=new BaseDAO("nc65");
	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		// TODO 自动生成的方法存根
//		StringBuff
		String sql="select * from med_project";
	List<MedProjectVO> list=(List<MedProjectVO>)dao.executeQuery(sql, new BeanListProcessor(MedProjectVO.class));
		for(MedProjectVO vo:list){
 
	        			 String ifsign=vo.getResult_IsDelistingSign();//是否签约
	        			String projectname= vo.getLandName();
	        			String projectcode= vo.getLandId();//项目编码
	        			String pk_project=isexistcode(projectcode);
	        			if(pk_project!=null&&pk_project!=""){
	        				ProjectHeadVO hvo=new ProjectHeadVO();
	        				hvo.setAttributeValue("project_name", projectname);
	        				hvo.setAttributeValue("project_code", projectcode);
	        				hvo.setAttributeValue("pk_org", "000112100000000005FD");
	        				hvo.setAttributeValue("pk_group", "000112100000000005FD");
	        				hvo.setAttributeValue("modifier", "1001ZZ100000001MR736");
	        				hvo.setAttributeValue("creationtime", new UFDateTime());
	        				hvo.setAttributeValue("modifiedtime", new UFDateTime());
	        				hvo.setAttributeValue("creator", "00011210000000000WJ0");
	        				hvo.setAttributeValue("enablestate", 2);
	        				hvo.setAttributeValue("def4", ifsign);
	        				hvo.setAttributeValue("dr", 0);
	        				String eps=(String)dao.executeQuery("select pk_eps from pm_eps  where eps_code='05'", new ColumnProcessor());//EPS
	        				hvo.setAttributeValue("pk_eps", eps);
	        				String pkprojectclass=(String)dao.executeQuery("select   pk_projectclass  from bd_projectclass where  type_code='05'", new ColumnProcessor());//项目类型
	        				hvo.setAttributeValue("pk_projectclass", pkprojectclass);
	        				hvo.setAttributeValue("bill_type", "4D10");
	        				hvo.setDef5(vo.getIsHistory());//是否历史数据
	        				hvo.setDef6(vo.getBaseLandId());//历史数据宗地ID
	        				hvo.setDef7(vo.getGrantTypeName());
	        				dao.insertVO(hvo);
		}else{
			ProjectHeadVO hvo=(ProjectHeadVO)dao.retrieveByPK(ProjectHeadVO.class, pk_project);
			hvo.setAttributeValue("def4", ifsign);
			hvo.setDef5(vo.getIsHistory());//是否历史数据
			hvo.setDef6(vo.getBaseLandId());//历史数据宗地ID
			hvo.setDef7(vo.getGrantTypeName());
			hvo.setAttributeValue("project_name", projectname);
			dao.updateVO(hvo);
		}
	}
		return null;
	}
	/**
	 * 该项目是否已存在（true为不存在）
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public String  isexistcode(String code) throws DAOException{
		String pk_defdoc=(String)dao.executeQuery("select     pk_project   from bd_project b where b.project_code ='"+code+"'",new ColumnProcessor());
		return pk_defdoc;
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
