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
 * 项目档案
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
	 * 自定义档案保存类
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
			throw new BusinessException("操作失败，项目编码是必填项，请检查参数设置");
		}
		
		//取消关联操作
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
				throw new BusinessException("操作失败，未在NC项目档案找到该项目编码："+value.get("code"));
			}
			Map<String,String> refMap = new HashMap<String,String>();
			refMap.put("msg", "【" + billkey + "】," + "取消关联操作完成!");
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
			throw new BusinessException("操作失败，项目名称是必填项，请检查参数设置");
		}
		headVO.setProject_code((String) value.get("code"));
		if(value.get("pk_maindata")!=null&&!"null".equals(value.get("pk_maindata"))&&!"".equals(value.get("pk_maindata"))){
			headVO.setDef1(value.get("pk_maindata").toString());//主数据pk
		}else{
			throw new BusinessException("操作失败，主数据pk是必填项，请检查参数设置");
		}
		if(StringUtils.isNotBlank((String) value.get("maindata_name"))){
			headVO.setDef3((String) value.get("maindata_name"));//主数据名称
		}else{
			throw new BusinessException("操作失败，主数据名称是必填项，请检查参数设置");
		}
		if(value.get("maindata_code")!=null&&!"null".equals(value.get("maindata_code"))&&!"".equals(value.get("maindata_code"))){
			headVO.setDef2(value.get("maindata_code").toString());//主数据编码
		}else{
			throw new BusinessException("操作失败，主数据编码是必填项，请检查参数设置");
		}
		if(value.get("projectType")!=null&&!"".equals(value.get("projectType"))){
			headVO.setPk_projectclass(DefdocUtils.getUtils().getProjectclass("03"));
		}else{
			headVO.setPk_projectclass(null);
		}
		headVO.setPk_eps(getPkEPSByCode("03"));//pk_eps默认房地产项目
		headVO.setPk_projectclass(getPkProjectClassByCode("03"));//项目类型--默认房地产项目类型
		if(value.get("org_code")!=null&&!"".equals(value.get("org_code"))){
			headVO.setPk_duty_org(DefdocUtils.getUtils().getPk_org(value.get("org_code").toString()));
		}else{
			throw new BusinessException("操作失败，组织编码是必填项，请检查参数设置");
		}
		
		if(value.get("project_status")!=null&&!"".equals(value.get("project_status"))){//资本化
			headVO.setDef8(value.get("project_status").equals("Y")?"Y":"N");
		}else{
			headVO.setDef8(null);
		}
		if(value.get("project_properties")!=null&&!"".equals(value.get("project_properties"))){//项目性质
			headVO.setDef9(DefdocUtils.getUtils().getDefdocByCode("zdy041", value.get("project_properties").equals("Y")?"01":"02").get("pk_defdoc").toString());
		}else{
			headVO.setDef9(null);
		}
		
		headVO.setDr(0);
		headVO.setStatus(VOStatus.NEW);
		//检查ebs映射关系是在nc已存在,如存在则给用户返回报错信息
//		Integer check = checkChangeCode(value);
//		if(check==3){throw new BusinessException("该项目编码："+value.get("org_code")+"已存在NC，不能修改，请检查");}
		
		
//		String srcid = headVO.getEbsid();// 销售系统业务单据ID
//		String srcno = headVO.getEbsbillcode();// 销售系统业务单据单据号
		billqueue = EBSCont.getDocNameMap().get(dectype) + ":" + headVO.getProject_code();
		billkey = EBSCont.getDocNameMap().get(dectype) + ":" + headVO.getProject_name();
		// TODO Saleid 按实际存入信息位置进行变更
		
			EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
//			NCObject[] docVO = (NCObject[]) getHeadVO(
//					DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
//					+ "'");
			Collection<nc.vo.pmpub.project.ProjectHeadVO> docVO = null;
			//nc新增情况
//			if(check==2){
//				docVO = getBaseDAO()
//						.retrieveByClause(nc.vo.pmpub.project.ProjectHeadVO.class,
//								"isnull(dr,0)=0  and def2 = '~'  and project_code = '"+value.get("code")+"' ");
//			}else{//更新
//				docVO = getBaseDAO()
//						.retrieveByClause(nc.vo.pmpub.project.ProjectHeadVO.class,
//								"isnull(dr,0)=0  and def2 = '"+value.get("maindata_code")+"' and project_code = '"+value.get("code")+"'");
//				
//			}
			//查询nc是有EBS传的nc项目的单据,有则更新,没有则新增
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
//						throw new BusinessException("操作失败，项目编码是必填项，请检查参数设置");
//					}
//				}
//				if(value.get("code")!=null&&!"".equals(value.get("code"))){
//					defdocVO[0].setProject_code(value.get("code").toString());
//				}else{
//					throw new BusinessException("操作失败，项目编码是必填项，请检查参数设置");
//				}
//				if(StringUtils.isNotBlank((String) value.get("name"))){
//					defdocVO[0].setProject_name((String) value.get("name"));
//				}else{
//					throw new BusinessException("操作失败，项目名称是必填项，请检查参数设置");
//				}
				if(value.get("enablestate")!=null&&!"".equals(value.get("enablestate"))){
					defdocVO[0].setEnablestate("Y".equals(value.get("enablestate"))?2:3);
				}else{
					defdocVO[0].setEnablestate(2);
				}
				
				if(value.get("pk_maindata")!=null&&!"null".equals(value.get("pk_maindata"))&&!"".equals(value.get("pk_maindata"))){
					defdocVO[0].setDef1(value.get("pk_maindata").toString());//主数据pk
				}else{
					throw new BusinessException("操作失败，主数据pk是必填项，请检查参数设置");
				}
				if(StringUtils.isNotBlank((String) value.get("maindata_name"))){
					defdocVO[0].setDef3((String) value.get("maindata_name"));//主数据名称
				}else{
					throw new BusinessException("操作失败，主数据名称是必填项，请检查参数设置");
				}
				if(value.get("maindata_code")!=null&&!"null".equals(value.get("maindata_code"))&&!"".equals(value.get("maindata_code"))){
					defdocVO[0].setDef2(value.get("maindata_code").toString());//主数据编码
				}else{
					throw new BusinessException("操作失败，主数据编码是必填项，请检查参数设置");
				}
				if(value.get("project_status")!=null&&!"".equals(value.get("project_status"))){//资本化
					defdocVO[0].setDef8(value.get("project_status").equals("Y")?"Y":"N");
				}else{
					defdocVO[0].setDef8(null);
				}
				if(value.get("project_properties")!=null&&!"".equals(value.get("project_properties"))){//项目性质
					defdocVO[0].setDef9(DefdocUtils.getUtils().getDefdocByCode("zdy041", value.get("project_properties").equals("Y")?"01":"02").get("pk_defdoc").toString());
				}else{
					defdocVO[0].setDef9(null);
				}
				defdocVO[0].setCreator("#UAP#");
				defdocVO[0].setModifier("#UAP#");
				defdocVO[0].setPk_projectclass(DefdocUtils.getUtils().getProjectclass("03"));
				defdocVO[0].setPk_eps(getPkEPSByCode("03"));//pk_eps默认房地产项目
				defdocVO[0].setPk_projectclass(getPkProjectClassByCode("03"));//项目类型--默认房地产项目类型
				if(value.get("org_code")!=null){
					defdocVO[0].setPk_duty_org(DefdocUtils.getUtils().getPk_org(value.get("org_code").toString()));
				}else{
					throw new BusinessException("操作失败，组织编码是必填项，请检查参数设置");
				}
				defdocService.updateProject(defdocVO[0]);
				
//				throw new BusinessException("【"
//						+ billkey
//						+ "】,NC已存在对应的业务单据【"
//						+ docVO[0].getAttributeValue(
//								DefdocVO.CODE) + "】,请勿重复上传!");
			}else{
//				//新增前校验,因为nc只做更新操作,顾此处直接报错
				throw new BusinessException("操作失败，编码："+value.get("code").toString()+"未能在NC项目档案关联，请检查");
//				if(!value.get("code").equals(value.get("maindata_code").toString())){
//					throw new BusinessException("操作失败，该NC编码："+value.get("code")+"和主数据编码："+value.get("maindata_code").toString()+"不一致，"+"该NC项目档案没有该NC编码");
//				}
////				headVO.setPk_defdoclist(DefdocUtils.getUtils().getDefdoclist(EBSCont.getDocNameMap().get(dectype)));
//				nc.vo.pmpub.project.ProjectHeadVO newHeadvo = defdocService.insertProject(headVO);
//				
//				//项目分配公司接口
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
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		Map<String,String> refMap = new HashMap<String,String>();
		refMap.put("msg", "【" + billkey + "】," + "操作完成!");
		refMap.put("data", "");
		return JSON.toJSONString(refMap) ;

	}

	/**
	 * 检查数据库的主数据编码和传过来的主数据编码是否匹配
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	private Integer checkChangeCode(HashMap<String, Object> value) throws BusinessException {
		if(value.get("maindata_code")!=null&&value.get("org_code")!=null){
			if(StringUtils.isNotBlank(value.get("maindata_code").toString())&&StringUtils.isNotBlank(value.get("code").toString())){
				//存在数据库的主数据编码
				String oldMainCode = (String) getBaseDAO().executeQuery("select project_code from bd_project where  project_code = '"+value.get("code").toString()+"' and def2 = '"+value.get("maindata_code").toString()+"' and nvl(dr,0) = 0", new ColumnProcessor());
				String newNc = (String) getBaseDAO().executeQuery("select def2 from bd_project where def2 = '~'  and project_code = '"+value.get("code").toString()+"' and nvl(dr,0) = 0", new ColumnProcessor());
				String newNcThird = (String) getBaseDAO().executeQuery("select def2 from bd_project where project_code = '"+value.get("code").toString()+"' and nvl(dr,0) = 0", new ColumnProcessor());
				if(StringUtils.isNotBlank(oldMainCode)){
					return 1;//可以修改
				}else if(StringUtils.isBlank(newNc)){
					return 2;//nc先新增,或已解除不能修改
				}else{
					return 3;//主数据编码存在但不匹配,意味着在nc已有编码,不能修改
				}
			}
		}
		return 0;
		
	}
	
	

	/**
	 * 当EBS传取消标识时需清空所传的ebs_id即NC的自定义项1
	 * @param value
	 * @throws DAOException 
	 */
	private void deleteDef1(HashMap<String, Object> value) throws DAOException {
//		getBaseDAO().deleteByClause(nc.vo.pmpub.project.ProjectHeadVO.class, " isnull(dr,0) = 0 and def1 = '"+value.get("pk_maindata")+"' ");
		getBaseDAO().executeUpdate("update bd_project set def1 = '~',def2 = '~',def3 = '~' where isnull(dr,0) = 0 and def1 = '"+value.get("pk_maindata")+"'");
	}

	/**
	 * 将来源信息转换成NC信息
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
