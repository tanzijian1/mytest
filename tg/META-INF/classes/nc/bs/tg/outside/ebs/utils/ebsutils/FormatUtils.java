package nc.bs.tg.outside.ebs.utils.ebsutils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.pub.doc.DefdocUtils;
import nc.bs.uap.lock.PKLock;
import nc.itf.bd.defdoc.IDefdocService;
import nc.itf.tg.outside.EBSCont;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.outside.EBSDefdocVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import net.sf.json.JSONArray;

/**
 * 业态
 * @author acer
 *
 */
public class FormatUtils extends EBSBillUtils{

	static FormatUtils utils;

	public static FormatUtils getUtils() {
		if (utils == null) {
			utils = new FormatUtils();
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

		IDefdocService defdocService = NCLocator.getInstance().lookup(IDefdocService.class);
//		JSONObject jsonData=  (JSONObject) value.get("json");
//		JSON jsonhead=  (JSON) jsonData.get("DefdocVO");
//		String jsonbody=  jsonData.getString("applyBodyVO");
//		List<ApplyBillBodyVO> bodyVOs=JSONObject.parseArray(jsonbody, ApplyBillBodyVO.class);
//		AggPayrequest aggvo=new AggPayrequest();
//		Payrequest save_headVO=new Payrequest();
//		Business_b save_bodyVO=new Business_b();
//		String pk_org = DefdocUtils.getUtils().getPk_org(jsonData.getString("name"));
		String pk_org = "000112100000000005FD";
//		if(StringUtils.isBlank(pk_org)){
//			throw new BusinessException("nc组织档案没有维护此组织，请联系管理员");
//		}
		JSONArray jsonarr = JSONArray.fromObject(value.get("data")); 
		Map<String,String> refMap = new HashMap<String,String>();
//		List<DefdocVO> list = JSONObject.parseArray(jsonarr.to, DefdocVO.class);
		String pk_defdoclist = DefdocUtils.getUtils().getDefdoclist("业态".equals(EBSCont.getDocNameMap().get(dectype))?"业态维":EBSCont.getDocNameMap().get(dectype));
		Collection<EBSDefdocVO> defdocvos= JSONArray.toCollection(jsonarr,EBSDefdocVO.class);
		for (EBSDefdocVO defdocheadVO : defdocvos) {
			DefdocVO headVO=new DefdocVO();
			headVO.setPk_group(AppContext.getInstance()
					.getPkGroup());
			headVO.setPk_org(pk_org);
			headVO.setMemo(defdocheadVO.getMemo());
			headVO.setCreator("#UAP#");
			headVO.setModifier("#UAP#");
			if(StringUtils.isNotBlank(defdocheadVO.getEnablestate())){
				headVO.setEnablestate("Y".equals(defdocheadVO.getEnablestate())?2:3);
			}else{
				headVO.setEnablestate(2);
			}
			headVO.setDr(0);
			headVO.setStatus(VOStatus.NEW);
			if(StringUtils.isNotBlank(defdocheadVO.getName())){
				headVO.setName(defdocheadVO.getName());
			}else{
				throw new BusinessException("操作失败，业态名称是必填项，请检查参数设置");
			}
			if(StringUtils.isNotBlank(defdocheadVO.getCode())){
				headVO.setCode(defdocheadVO.getCode());
			}else{
				throw new BusinessException("操作失败，业态编码是必填项，请检查参数设置");
			}
			
			//上级档案
			Map<String,String> map = DefdocUtils.getUtils().getDefdocPranetPkByVO(defdocheadVO,"10011210000000000569");
			if(null!=map&&map.size()>0){
				headVO.setPid(map.get("pk_defdoc"));
			}else{
				headVO.setPid(null);
			}
			//EBS传子项目档案id
			if(StringUtils.isNotBlank(defdocheadVO.getEbs_id())){
				headVO.setDef1(defdocheadVO.getEbs_id());
			}else{
				throw new BusinessException("操作失败，业态id是必填项，请检查参数设置");
			}
			//EBS传母项目档案id
			headVO.setDef2(defdocheadVO.getEbs_parent_id());
			String billqueue = EBSCont.getDocNameMap().get(dectype) + ":" + headVO.getCode();
			String billkey = EBSCont.getDocNameMap().get(dectype) + ":" + headVO.getName();
			// TODO Saleid 按实际存入信息位置进行变更
			try {
				EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
//				NCObject[] docVO = (NCObject[]) getHeadVO(
//						DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
//						+ "'");
				//按自定义档案业态列表pk,编码,名称查自定义档案
				Collection<DefdocVO> docVO = getBaseDAO()
						.retrieveByClause(DefdocVO.class,
								"(pk_defdoclist = '"+EBSBillUtils.getUtils().getPkDefdocListByCode("ys004")+"' and isnull(dr,0)=0 and def1 = '" + defdocheadVO.getEbs_id()
								+ "')  ");
				if (docVO != null&&docVO.size()>0) {
					DefdocVO[] defdocVO = docVO.toArray(new DefdocVO[0]);
					defdocVO[0].setStatus(VOStatus.UPDATED);
					if(StringUtils.isNotBlank(defdocheadVO.getName())){
						defdocVO[0].setName(defdocheadVO.getName());
					}else{
						throw new BusinessException("操作失败，业态名称是必填项，请检查参数设置");
					}
					if(StringUtils.isNotBlank(defdocheadVO.getCode())){
						defdocVO[0].setCode(defdocheadVO.getCode());
					}else{
						throw new BusinessException("操作失败，业态编码是必填项，请检查参数设置");
					}
					if(StringUtils.isNotBlank(defdocheadVO.getEnablestate())){
						defdocVO[0].setEnablestate("Y".equals(defdocheadVO.getEnablestate())?2:3);
					}else{
						defdocVO[0].setEnablestate(2);
					}
					defdocVO[0].setMemo(defdocheadVO.getMemo());
					defdocVO[0].setCreator("#UAP#");
					defdocVO[0].setModifier("#UAP#");
					//上级档案
					if(null!=map&&map.size()>0){
						defdocVO[0].setPid(map.get("pk_defdoc"));
					}else{
						defdocVO[0].setPid(null);
					}
					//EBS传子项目档案id
					if(StringUtils.isNotBlank(defdocheadVO.getEbs_id())){
						defdocVO[0].setDef1(defdocheadVO.getEbs_id());
					}else{
						throw new BusinessException("操作失败，业态id是必填项，请检查参数设置");
					}
					//EBS传母项目档案id
					defdocVO[0].setDef2(defdocheadVO.getEbs_parent_id());
					defdocService.updateDefdocs(pk_org, defdocVO);
//					throw new BusinessException("【"
//							+ billkey
//							+ "】,NC已存在对应的业务单据【"
//							+ docVO[0].getAttributeValue(
//									DefdocVO.CODE) + "】,请勿重复上传!");
				}else{
//					headVO.setPk_defdoclist(pk_defdoclist);
					headVO.setPk_defdoclist(EBSBillUtils.getUtils().getPkDefdocListByCode("ys004"));
					PKLock.getInstance().releaseLock(EBSBillUtils.getUtils().getPkDefdocListByCode("ys004"), null, null);
					defdocService.insertDefdocs(pk_org, new DefdocVO[]{headVO});
				}
			}catch (Exception e) {
				throw new BusinessException("【" + billkey + "】," + e.getMessage(),
						e);
			} finally {
				EBSBillUtils.removeBillQueue(billqueue);
			}
			refMap.put("msg", "【业态信息】," + "操作完成!");
			refMap.put("data", "");
		}
		return JSON.toJSONString(refMap) ;
	}
	
//	public String onSyncBill(HashMap<String, Object> value, String dectype) throws BusinessException {
//		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
//		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
////		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
//		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
//
//		IDefdocService defdocService = NCLocator.getInstance().lookup(IDefdocService.class);
////		JSONObject jsonData=  (JSONObject) value.get("json");
////		JSON jsonhead=  (JSON) jsonData.get("DefdocVO");
////		String jsonbody=  jsonData.getString("applyBodyVO");
////		List<ApplyBillBodyVO> bodyVOs=JSONObject.parseArray(jsonbody, ApplyBillBodyVO.class);
////		AggPayrequest aggvo=new AggPayrequest();
////		Payrequest save_headVO=new Payrequest();
////		Business_b save_bodyVO=new Business_b();
////		String pk_org = DefdocUtils.getUtils().getPk_org(jsonData.getString("name"));
//		String pk_org = "000112100000000005FD";
////		if(StringUtils.isBlank(pk_org)){
////			throw new BusinessException("nc组织档案没有维护此组织，请联系管理员");
////		}
//		DefdocVO headVO=new DefdocVO();
//		headVO.setPk_group("000112100000000005FD");
//		headVO.setPk_org(pk_org);
//		if(StringUtils.isNotBlank((String) value.get("name"))){
//			headVO.setName((String) value.get("name"));
//		}else{
//			throw new BusinessException("操作失败，业态名称是必填项，请检查参数设置");
//		}
//		if(StringUtils.isNotBlank((String) value.get("code"))){
//			headVO.setCode((String) value.get("code"));
//		}else{
//			throw new BusinessException("操作失败，业态编码是必填项，请检查参数设置");
//		}
//		headVO.setEnablestate(StringUtils.isBlank((String) value.get("enablestate"))?2:Integer.valueOf((String) value.get("enablestate")));
//		headVO.setDr(0);
//		headVO.setStatus(VOStatus.NEW);
////		String srcid = headVO.getEbsid();// 销售系统业务单据ID
////		String srcno = headVO.getEbsbillcode();// 销售系统业务单据单据号
//		String billqueue = EBSCont.getDocNameMap().get(dectype) + ":" + headVO.getCode();
//		String billkey = EBSCont.getDocNameMap().get(dectype) + ":" + headVO.getName();
//		// TODO Saleid 按实际存入信息位置进行变更
//		try {
//			EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
////			NCObject[] docVO = (NCObject[]) getHeadVO(
////					DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
////					+ "'");
//			
//			Collection<DefdocVO> docVO = getBaseDAO()
//					.retrieveByClause(DefdocVO.class,
//							"(isnull(dr,0)=0 and code = '" + headVO.getCode()
//							+ "') or (isnull(dr,0)=0 and name = '" + headVO.getName()
//							+ "')");
//			if (docVO != null&&docVO.size()>0) {
//				DefdocVO[] defdocVO = docVO.toArray(new DefdocVO[0]);
//				defdocVO[0].setStatus(VOStatus.UPDATED);
//				if(StringUtils.isNotBlank((String) value.get("name"))){
//					defdocVO[0].setName((String) value.get("name"));
//				}else{
//					throw new BusinessException("操作失败，业态名称是必填项，请检查参数设置");
//				}
//				if(StringUtils.isNotBlank((String) value.get("code"))){
//					defdocVO[0].setCode((String) value.get("code"));
//				}else{
//					throw new BusinessException("操作失败，业态编码是必填项，请检查参数设置");
//				}
//				defdocVO[0].setEnablestate(value.get("enablestate")==null?2:Integer.valueOf((String) value.get("enablestate")));
//				defdocService.updateDefdocs(pk_org, defdocVO);
////				throw new BusinessException("【"
////						+ billkey
////						+ "】,NC已存在对应的业务单据【"
////						+ docVO[0].getAttributeValue(
////								DefdocVO.CODE) + "】,请勿重复上传!");
//			}else{
//				headVO.setPk_defdoclist(DefdocUtils.getUtils().getDefdoclist(EBSCont.getDocNameMap().get(dectype.equals("业态")?"业态维":dectype)));
//				defdocService.insertDefdocs(pk_org, new DefdocVO[]{headVO});
//			}
////			AggPayrequest billvo = onTranBill(value, dectype);
////			HashMap eParam = new HashMap();
////			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
////					PfUtilBaseTools.PARAM_NOTE_CHECKED);
////			getPfBusiAction().processAction("SAVEBASE", "FN13", null, billvo,
////					null, eParam);
//		} catch (Exception e) {
//			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
//					e);
//		} finally {
//			EBSBillUtils.removeBillQueue(billqueue);
//		}
//		Map<String,String> refMap = new HashMap<String,String>();
//		refMap.put("msg", "【" + billkey + "】," + "操作完成!");
//		refMap.put("data", "");
//		return JSON.toJSONString(refMap) ;
//
//	}

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
