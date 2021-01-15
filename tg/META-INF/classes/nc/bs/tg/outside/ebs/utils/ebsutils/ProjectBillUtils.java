package nc.bs.tg.outside.ebs.utils.ebsutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.pub.doc.DefdocUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.AppContext;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

import com.alibaba.fastjson.JSON;

/**
 * ��Ŀ����
 * @author acer
 *
 */
public class ProjectBillUtils extends EBSBillUtils{

	static ProjectBillUtils utils;

	public static ProjectBillUtils getUtils() {
		if (utils == null) {
			utils = new ProjectBillUtils();
		}
		return utils;
	}

	/**
	 * �Զ��嵵��������
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId(AppContext.getInstance()
				.getPkGroup());
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
//		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		String billqueue = null;
		String billkey = null;
		try {
		nc.itf.pmpub.prv.IProject defdocService = NCLocator.getInstance().lookup(nc.itf.pmpub.prv.IProject.class);
		
		if(value.get("code")==null&&"".equals(value.get("code"))){
			throw new BusinessException("����ʧ�ܣ���Ŀ�����Ǳ���������������");
		}
		
		//ȡ����������
		if(value.get("iscancel")!=null&&"Y".equals(value.get("iscancel"))){
			deleteDef1(value);
			Collection<nc.vo.pmpub.project.ProjectHeadVO> docVO = getBaseDAO()
					.retrieveByClause(nc.vo.pmpub.project.ProjectHeadVO.class,
							"(isnull(dr,0)=0 and project_code = '" + value.get("code")+ "' and def2 = '"+value.get("maindata_code")+"')");
			if (docVO != null&&docVO.size()>0) {
				nc.vo.pmpub.project.ProjectHeadVO[] defdocVO = docVO.toArray(new nc.vo.pmpub.project.ProjectHeadVO[0]);
				defdocVO[0].setStatus(VOStatus.UPDATED);
				defdocVO[0].setEnablestate(3);
				defdocService.updateProject(defdocVO[0]);
			}else{
				throw new BusinessException("����ʧ�ܣ�δ��NC��Ŀ�����ҵ�����Ŀ���룺"+value.get("code"));
			}
			Map<String,String> refMap = new HashMap<String,String>();
			refMap.put("msg", "��" + billkey + "��," + "ȡ�������������!");
			refMap.put("data", "");
			return JSON.toJSONString(refMap) ;
		}
		
		String pk_org = "000112100000000005FD";
		nc.vo.pmpub.project.ProjectHeadVO headVO=new nc.vo.pmpub.project.ProjectHeadVO();
		headVO.setPk_group(AppContext.getInstance()
				.getPkGroup());
		headVO.setPk_org(pk_org);
		headVO.setMemo((String) value.get("memo"));
		if(value.get("enablestate")!=null&&!"".equals(value.get("enablestate"))){
			headVO.setEnablestate("Y".equals(value.get("enablestate"))?2:3);
		}else{
			headVO.setEnablestate(2);
		}
		if(StringUtils.isNotBlank((String) value.get("name"))){
			headVO.setProject_name((String) value.get("name"));
		}else{
			throw new BusinessException("����ʧ�ܣ���Ŀ�����Ǳ���������������");
		}
		headVO.setProject_code((String) value.get("code"));
		if(value.get("pk_maindata")!=null&&!"null".equals(value.get("pk_maindata"))&&!"".equals(value.get("pk_maindata"))){
			headVO.setDef1(value.get("pk_maindata").toString());//������pk
		}else{
			throw new BusinessException("����ʧ�ܣ�������pk�Ǳ���������������");
		}
		if(StringUtils.isNotBlank((String) value.get("maindata_name"))){
			headVO.setDef3((String) value.get("maindata_name"));//����������
		}else{
			throw new BusinessException("����ʧ�ܣ������������Ǳ���������������");
		}
		if(value.get("maindata_code")!=null&&!"null".equals(value.get("maindata_code"))&&!"".equals(value.get("maindata_code"))){
			headVO.setDef2(value.get("maindata_code").toString());//�����ݱ���
		}else{
			throw new BusinessException("����ʧ�ܣ������ݱ����Ǳ���������������");
		}
		if(value.get("projectType")!=null&&!"".equals(value.get("projectType"))){
			headVO.setPk_projectclass(DefdocUtils.getUtils().getProjectclass("03"));
		}else{
			headVO.setPk_projectclass(null);
		}
		headVO.setPk_eps(getPkEPSByCode("03"));//pk_epsĬ�Ϸ��ز���Ŀ
		headVO.setPk_projectclass(getPkProjectClassByCode("03"));//��Ŀ����--Ĭ�Ϸ��ز���Ŀ����
		if(value.get("org_code")!=null&&!"".equals(value.get("org_code"))){
			headVO.setPk_duty_org(DefdocUtils.getUtils().getPk_org(value.get("org_code").toString()));
		}else{
			throw new BusinessException("����ʧ�ܣ���֯�����Ǳ���������������");
		}
		
		if(value.get("project_status")!=null&&!"".equals(value.get("project_status"))){//�ʱ���
			headVO.setDef8(value.get("project_status").equals("Y")?"Y":"N");
		}else{
			headVO.setDef8(null);
		}
		if(value.get("project_properties")!=null&&!"".equals(value.get("project_properties"))){//��Ŀ����
			headVO.setDef9(DefdocUtils.getUtils().getDefdocByCode("zdy041", value.get("project_properties").equals("Y")?"01":"02").get("pk_defdoc").toString());
		}else{
			headVO.setDef9(null);
		}
		
		headVO.setDr(0);
		headVO.setStatus(VOStatus.NEW);
		//���ebsӳ���ϵ����nc�Ѵ���,���������û����ر�����Ϣ
//		Integer check = checkChangeCode(value);
//		if(check==3){throw new BusinessException("����Ŀ���룺"+value.get("org_code")+"�Ѵ���NC�������޸ģ�����");}
		
		
//		String srcid = headVO.getEbsid();// ����ϵͳҵ�񵥾�ID
//		String srcno = headVO.getEbsbillcode();// ����ϵͳҵ�񵥾ݵ��ݺ�
		billqueue = EBSCont.getDocNameMap().get(dectype) + ":" + headVO.getProject_code();
		billkey = EBSCont.getDocNameMap().get(dectype) + ":" + headVO.getProject_name();
		// TODO Saleid ��ʵ�ʴ�����Ϣλ�ý��б��
		
			EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
//			NCObject[] docVO = (NCObject[]) getHeadVO(
//					DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
//					+ "'");
			Collection<nc.vo.pmpub.project.ProjectHeadVO> docVO = null;
			//nc�������
//			if(check==2){
//				docVO = getBaseDAO()
//						.retrieveByClause(nc.vo.pmpub.project.ProjectHeadVO.class,
//								"isnull(dr,0)=0  and def2 = '~'  and project_code = '"+value.get("code")+"' ");
//			}else{//����
//				docVO = getBaseDAO()
//						.retrieveByClause(nc.vo.pmpub.project.ProjectHeadVO.class,
//								"isnull(dr,0)=0  and def2 = '"+value.get("maindata_code")+"' and project_code = '"+value.get("code")+"'");
//				
//			}
			//��ѯnc����EBS����nc��Ŀ�ĵ���,�������,û��������
			docVO = getBaseDAO()
					.retrieveByClause(nc.vo.pmpub.project.ProjectHeadVO.class,
							"isnull(dr,0)=0  and project_code = '"+value.get("code")+"' ");
			
			if (docVO != null&&docVO.size()>0) {
				nc.vo.pmpub.project.ProjectHeadVO[] defdocVO = docVO.toArray(new nc.vo.pmpub.project.ProjectHeadVO[0]);
				defdocVO[0].setStatus(VOStatus.UPDATED);
				defdocVO[0].setMemo((String) value.get("memo"));
//				if(check==1){
//					if(value.get("code")!=null&&!"".equals(value.get("code"))){
//						defdocVO[0].setProject_code(value.get("code").toString());
//					}else{
//						throw new BusinessException("����ʧ�ܣ���Ŀ�����Ǳ���������������");
//					}
//				}
//				if(value.get("code")!=null&&!"".equals(value.get("code"))){
//					defdocVO[0].setProject_code(value.get("code").toString());
//				}else{
//					throw new BusinessException("����ʧ�ܣ���Ŀ�����Ǳ���������������");
//				}
//				if(StringUtils.isNotBlank((String) value.get("name"))){
//					defdocVO[0].setProject_name((String) value.get("name"));
//				}else{
//					throw new BusinessException("����ʧ�ܣ���Ŀ�����Ǳ���������������");
//				}
				if(value.get("enablestate")!=null&&!"".equals(value.get("enablestate"))){
					defdocVO[0].setEnablestate("Y".equals(value.get("enablestate"))?2:3);
				}else{
					defdocVO[0].setEnablestate(2);
				}
				
				if(value.get("pk_maindata")!=null&&!"null".equals(value.get("pk_maindata"))&&!"".equals(value.get("pk_maindata"))){
					defdocVO[0].setDef1(value.get("pk_maindata").toString());//������pk
				}else{
					throw new BusinessException("����ʧ�ܣ�������pk�Ǳ���������������");
				}
				if(StringUtils.isNotBlank((String) value.get("maindata_name"))){
					defdocVO[0].setDef3((String) value.get("maindata_name"));//����������
				}else{
					throw new BusinessException("����ʧ�ܣ������������Ǳ���������������");
				}
				if(value.get("maindata_code")!=null&&!"null".equals(value.get("maindata_code"))&&!"".equals(value.get("maindata_code"))){
					defdocVO[0].setDef2(value.get("maindata_code").toString());//�����ݱ���
				}else{
					throw new BusinessException("����ʧ�ܣ������ݱ����Ǳ���������������");
				}
				if(value.get("project_status")!=null&&!"".equals(value.get("project_status"))){//�ʱ���
					defdocVO[0].setDef8(value.get("project_status").equals("Y")?"Y":"N");
				}else{
					defdocVO[0].setDef8(null);
				}
				if(value.get("project_properties")!=null&&!"".equals(value.get("project_properties"))){//��Ŀ����
					defdocVO[0].setDef9(DefdocUtils.getUtils().getDefdocByCode("zdy041", value.get("project_properties").equals("Y")?"01":"02").get("pk_defdoc").toString());
				}else{
					defdocVO[0].setDef9(null);
				}
				defdocVO[0].setCreator("#UAP#");
				defdocVO[0].setModifier("#UAP#");
				defdocVO[0].setPk_projectclass(DefdocUtils.getUtils().getProjectclass("03"));
				defdocVO[0].setPk_eps(getPkEPSByCode("03"));//pk_epsĬ�Ϸ��ز���Ŀ
				defdocVO[0].setPk_projectclass(getPkProjectClassByCode("03"));//��Ŀ����--Ĭ�Ϸ��ز���Ŀ����
				if(value.get("org_code")!=null){
					defdocVO[0].setPk_duty_org(DefdocUtils.getUtils().getPk_org(value.get("org_code").toString()));
				}else{
					throw new BusinessException("����ʧ�ܣ���֯�����Ǳ���������������");
				}
				defdocService.updateProject(defdocVO[0]);
				
//				throw new BusinessException("��"
//						+ billkey
//						+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
//						+ docVO[0].getAttributeValue(
//								DefdocVO.CODE) + "��,�����ظ��ϴ�!");
			}else{
//				//����ǰУ��,��Ϊncֻ�����²���,�˴˴�ֱ�ӱ���
				throw new BusinessException("����ʧ�ܣ����룺"+value.get("code").toString()+"δ����NC��Ŀ��������������");
//				if(!value.get("code").equals(value.get("maindata_code").toString())){
//					throw new BusinessException("����ʧ�ܣ���NC���룺"+value.get("code")+"�������ݱ��룺"+value.get("maindata_code").toString()+"��һ�£�"+"��NC��Ŀ����û�и�NC����");
//				}
////				headVO.setPk_defdoclist(DefdocUtils.getUtils().getDefdoclist(EBSCont.getDocNameMap().get(dectype)));
//				nc.vo.pmpub.project.ProjectHeadVO newHeadvo = defdocService.insertProject(headVO);
//				
//				//��Ŀ���乫˾�ӿ�
//				List<String> pk_orgList = new ArrayList<String>();
//				List<String> pk_projectList = new ArrayList<String>();
//				pk_projectList.add(newHeadvo.getPk_project());
//				if(null!=value.get("org_code")&&!"".equals(value.get("org_code"))){
//					String org_code = value.get("org_code").toString();
//					pk_orgList.add(DefdocUtils.getUtils().getPk_org(org_code));
//					getProjectAssignService().assignProjectByPks(pk_projectList.toArray(new String[0]),pk_orgList.toArray(new String[0]),pk_orgList.toArray(new String[0]));
//				}
//			}
//			AggPayrequest billvo = onTranBill(value, dectype);
//			HashMap eParam = new HashMap();
//			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
//					PfUtilBaseTools.PARAM_NOTE_CHECKED);
//			getPfBusiAction().processAction("SAVEBASE", "FN13", null, billvo,
//					null, eParam);
			}
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		Map<String,String> refMap = new HashMap<String,String>();
		refMap.put("msg", "��" + billkey + "��," + "�������!");
		refMap.put("data", "");
		return JSON.toJSONString(refMap) ;

	}

	/**
	 * ������ݿ�������ݱ���ʹ������������ݱ����Ƿ�ƥ��
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	private Integer checkChangeCode(HashMap<String, Object> value) throws BusinessException {
		if(value.get("maindata_code")!=null&&value.get("org_code")!=null){
			if(StringUtils.isNotBlank(value.get("maindata_code").toString())&&StringUtils.isNotBlank(value.get("code").toString())){
				//�������ݿ�������ݱ���
				String oldMainCode = (String) getBaseDAO().executeQuery("select project_code from bd_project where  project_code = '"+value.get("code").toString()+"' and def2 = '"+value.get("maindata_code").toString()+"' and nvl(dr,0) = 0", new ColumnProcessor());
				String newNc = (String) getBaseDAO().executeQuery("select def2 from bd_project where def2 = '~'  and project_code = '"+value.get("code").toString()+"' and nvl(dr,0) = 0", new ColumnProcessor());
				String newNcThird = (String) getBaseDAO().executeQuery("select def2 from bd_project where project_code = '"+value.get("code").toString()+"' and nvl(dr,0) = 0", new ColumnProcessor());
				if(StringUtils.isNotBlank(oldMainCode)){
					return 1;//�����޸�
				}else if(StringUtils.isBlank(newNc)){
					return 2;//nc������,���ѽ�������޸�
				}else{
					return 3;//�����ݱ�����ڵ���ƥ��,��ζ����nc���б���,�����޸�
				}
			}
		}
		return 0;
		
	}
	
	

	/**
	 * ��EBS��ȡ����ʶʱ�����������ebs_id��NC���Զ�����1
	 * @param value
	 * @throws DAOException 
	 */
	private void deleteDef1(HashMap<String, Object> value) throws DAOException {
//		getBaseDAO().deleteByClause(nc.vo.pmpub.project.ProjectHeadVO.class, " isnull(dr,0) = 0 and def1 = '"+value.get("pk_maindata")+"' ");
		getBaseDAO().executeUpdate("update bd_project set def1 = '~',def2 = '~',def3 = '~' where isnull(dr,0) = 0 and def1 = '"+value.get("pk_maindata")+"'");
	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 */
	private AggPayrequest onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {
		AggPayrequest aggvo = new AggPayrequest();
		JSON headjson = (JSON) value.get("headInfo");
		JSON bodyjson = (JSON) value.get("itemInfo");

		return aggvo;
	}
}
