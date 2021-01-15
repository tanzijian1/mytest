package nc.bs.tg.outside.sale.utils.salessystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.SaleBillCont;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.cdm.innerpay.action.returnBackAction;
import nc.vo.pmpub.project.ProjectHeadVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.SaleProjectVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ProjectGroupUtils {
	static ProjectGroupUtils utils;
     private BaseDAO dao=new BaseDAO();
	public BaseDAO getDao() {
		return dao;
	}
	public void setDao(BaseDAO dao) {
		this.dao = dao;
	}
	public static ProjectGroupUtils getUtils() {
		if (utils == null) {
			utils = new ProjectGroupUtils();
		}
		return utils;
	}
	/**
	 * 生成项目-集团档案表头
	 * @param value
	 * @param billtype
	 * @throws BusinessException 
	 */
	public String onSyncBill(HashMap<String, Object> value,String billtype) throws BusinessException{
//		Map<String,String> map=null;
//		 JSONArray jarry= null;
//             map=(Map<String,String>)value.get("data");
		ISqlThread service=NCLocator.getInstance().lookup(ISqlThread.class);
		String str=JSONArray.toJSONString(value.get("data"));
              List<nc.vo.tg.outside.SaleProjectVO> vos= JSONObject.parseArray(str,SaleProjectVO.class);
             if(vos==null||vos.size()<1) throw new BusinessException("转换json[data]结果为空");
             String billqueue="";
             List<String> errlist=new ArrayList<String>();
      for(SaleProjectVO vo:vos){
        	   try{
//			jarry.get(i).
	    String def2=vo.getDef2();//对应销售系统项目分期内码
		if(isexist(def2)){
			throw new BusinessException("NC系统已存在对应销售系统项目分期内码"+def2);
		}
		 billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ def2;
//		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理
		ProjectHeadVO phvo=null;
			ProjectHeadVO orgin_hvo=(ProjectHeadVO)getDao().executeQuery("select * from bd_project where nvl(dr,0)=0 and  project_code='"+vo.getProject_code()+"'", new BeanProcessor(ProjectHeadVO.class));
			if(orgin_hvo!=null){
				orgin_hvo.setAttributeValue("def2", def2);
				phvo=orgin_hvo;
				service.billupdate_RequiresNew(phvo);
			}else{
				phvo=onTranBill(vo);
			valid(phvo);
			service.billInsert_RequiresNew(phvo);
			}
        	   }catch(Exception e){
        		   errlist.add("["+billqueue+"]"+e.getMessage()); 
     	       }
		    }
           String strerr="";
           if(errlist!=null&& errlist.size()>0){
        	   strerr= JSON.toJSONString(errlist);
           }
             HashMap<String, String> resultmap=new HashMap<String,String>();
 			resultmap.put("msg",strerr);
 			return JSON.toJSONString(resultmap);
	}
	
	/**
	 * 生成表头vo
	 * @param map
	 * @return
	 * @throws DAOException
	 */
	public ProjectHeadVO onTranBill(SaleProjectVO vo) throws Exception{
		ProjectHeadVO hvo=new ProjectHeadVO();
		hvo.setAttributeValue("project_name", vo.getProject_name());//项目名称（对应销售系统房间核算名称）
		hvo.setAttributeValue("project_code", vo.getProject_code());//项目编码（对应销售系统房间核算代码）
		hvo.setAttributeValue("pk_org", "000112100000000005FD");
		hvo.setAttributeValue("pk_group", "000112100000000005FD");
		hvo.setAttributeValue("creationtime", new UFDateTime());
		hvo.setAttributeValue("modifiedtime", new UFDateTime());
		String userid=(String)getDao().executeQuery("select s.cuserid from sm_user s where  s.user_code='SALE' ", new ColumnProcessor());
		hvo.setAttributeValue("creator", userid);
		hvo.setAttributeValue("enablestate", vo.getEnablestate());//启用状态(对应销售系统房间状态)
		hvo.setAttributeValue("def4", null);//是否已签约
		hvo.setAttributeValue("dr", 0);
		hvo.setAttributeValue("memo", vo.getMemo());//备注
		hvo.setDef2(vo.getDef2());//对应销售系统项目分期内码
		String[] strs=vo.getEpscode().split("-");
		StringBuffer epscode=new StringBuffer();
		epscode.append("04");
		for(String str:strs){
			epscode.append(str);
		}
		String eps=(String)getDao().executeQuery("select pk_eps from pm_eps  where eps_code='"+epscode+"'", new ColumnProcessor());//EPS
		if(eps==null||eps=="") throw new BusinessException("无法在nc找到对应的EPS，请检查！");
		hvo.setAttributeValue("pk_eps", eps);//EPS(对应销售系统项目期数)
		String pkprojectclass=(String)dao.executeQuery("select   pk_projectclass  from bd_projectclass where  type_code='04'", new ColumnProcessor());//项目类型
		hvo.setAttributeValue("pk_projectclass", pkprojectclass);//项目类型(默认"房地产项目明细“)
		hvo.setAttributeValue("bill_type", "4D10");
		return hvo;
	}
	 /**
	  * 校验非空
	  * @param hvo
	 * @throws BusinessException 
	  */
	public void valid(ProjectHeadVO hvo) throws BusinessException{
		if(hvo.getProject_name()==null) throw new BusinessException("项目名称不能为空");
		if(hvo.getProject_code()==null) throw new BusinessException("项目编码不能为空");
		if(hvo.getEnablestate()==null) throw new BusinessException("房间状态不能为空");
		if(hvo.getDef2()==null) throw new BusinessException("项目分期内码不能为空");
		if(hvo.getPk_eps()==null) throw new BusinessException("项目期数不能为空");
	}
	
	public boolean isexist(String def2) throws DAOException{
		boolean flag=false;
		String pk=(String)getDao().executeQuery("select pk_project from bd_project where nvl(dr,0)=0 and def2='"+def2+"'", new ColumnProcessor());
		if(pk!=null){
			flag=true;
		}
		return flag;
	}
}
