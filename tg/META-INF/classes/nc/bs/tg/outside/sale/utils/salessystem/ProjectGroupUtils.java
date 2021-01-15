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
	 * ������Ŀ-���ŵ�����ͷ
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
             if(vos==null||vos.size()<1) throw new BusinessException("ת��json[data]���Ϊ��");
             String billqueue="";
             List<String> errlist=new ArrayList<String>();
      for(SaleProjectVO vo:vos){
        	   try{
//			jarry.get(i).
	    String def2=vo.getDef2();//��Ӧ����ϵͳ��Ŀ��������
		if(isexist(def2)){
			throw new BusinessException("NCϵͳ�Ѵ��ڶ�Ӧ����ϵͳ��Ŀ��������"+def2);
		}
		 billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ def2;
//		BPMBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
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
	 * ���ɱ�ͷvo
	 * @param map
	 * @return
	 * @throws DAOException
	 */
	public ProjectHeadVO onTranBill(SaleProjectVO vo) throws Exception{
		ProjectHeadVO hvo=new ProjectHeadVO();
		hvo.setAttributeValue("project_name", vo.getProject_name());//��Ŀ���ƣ���Ӧ����ϵͳ����������ƣ�
		hvo.setAttributeValue("project_code", vo.getProject_code());//��Ŀ���루��Ӧ����ϵͳ���������룩
		hvo.setAttributeValue("pk_org", "000112100000000005FD");
		hvo.setAttributeValue("pk_group", "000112100000000005FD");
		hvo.setAttributeValue("creationtime", new UFDateTime());
		hvo.setAttributeValue("modifiedtime", new UFDateTime());
		String userid=(String)getDao().executeQuery("select s.cuserid from sm_user s where  s.user_code='SALE' ", new ColumnProcessor());
		hvo.setAttributeValue("creator", userid);
		hvo.setAttributeValue("enablestate", vo.getEnablestate());//����״̬(��Ӧ����ϵͳ����״̬)
		hvo.setAttributeValue("def4", null);//�Ƿ���ǩԼ
		hvo.setAttributeValue("dr", 0);
		hvo.setAttributeValue("memo", vo.getMemo());//��ע
		hvo.setDef2(vo.getDef2());//��Ӧ����ϵͳ��Ŀ��������
		String[] strs=vo.getEpscode().split("-");
		StringBuffer epscode=new StringBuffer();
		epscode.append("04");
		for(String str:strs){
			epscode.append(str);
		}
		String eps=(String)getDao().executeQuery("select pk_eps from pm_eps  where eps_code='"+epscode+"'", new ColumnProcessor());//EPS
		if(eps==null||eps=="") throw new BusinessException("�޷���nc�ҵ���Ӧ��EPS�����飡");
		hvo.setAttributeValue("pk_eps", eps);//EPS(��Ӧ����ϵͳ��Ŀ����)
		String pkprojectclass=(String)dao.executeQuery("select   pk_projectclass  from bd_projectclass where  type_code='04'", new ColumnProcessor());//��Ŀ����
		hvo.setAttributeValue("pk_projectclass", pkprojectclass);//��Ŀ����(Ĭ��"���ز���Ŀ��ϸ��)
		hvo.setAttributeValue("bill_type", "4D10");
		return hvo;
	}
	 /**
	  * У��ǿ�
	  * @param hvo
	 * @throws BusinessException 
	  */
	public void valid(ProjectHeadVO hvo) throws BusinessException{
		if(hvo.getProject_name()==null) throw new BusinessException("��Ŀ���Ʋ���Ϊ��");
		if(hvo.getProject_code()==null) throw new BusinessException("��Ŀ���벻��Ϊ��");
		if(hvo.getEnablestate()==null) throw new BusinessException("����״̬����Ϊ��");
		if(hvo.getDef2()==null) throw new BusinessException("��Ŀ�������벻��Ϊ��");
		if(hvo.getPk_eps()==null) throw new BusinessException("��Ŀ��������Ϊ��");
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
