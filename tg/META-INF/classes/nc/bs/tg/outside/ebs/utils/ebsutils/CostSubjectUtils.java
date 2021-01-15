package nc.bs.tg.outside.ebs.utils.ebsutils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.pub.doc.DefdocUtils;
import nc.bs.uap.lock.PKLock;
import nc.bs.uif2.LockFailedException;
import nc.itf.bd.defdoc.IDefdocService;
import nc.itf.tg.outside.EBSCont;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.AppContext;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * 成本科目
 * 
 * @author acer
 * 
 */
public class CostSubjectUtils extends EBSBillUtils {

	static CostSubjectUtils utils;

	public static CostSubjectUtils getUtils() {
		if (utils == null) {
			utils = new CostSubjectUtils();
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
	public String onSyncBill(HashMap<String, Object> value, String dectype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId(
				AppContext.getInstance().getPkGroup());
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		IDefdocService defdocService = NCLocator.getInstance().lookup(
				IDefdocService.class);
		// JSONObject jsonData= (JSONObject) value.get("json");
		// JSON jsonhead= (JSON) jsonData.get("DefdocVO");
		// String jsonbody= jsonData.getString("applyBodyVO");
		// List<ApplyBillBodyVO> bodyVOs=JSONObject.parseArray(jsonbody,
		// ApplyBillBodyVO.class);
		// AggPayrequest aggvo=new AggPayrequest();
		// Payrequest save_headVO=new Payrequest();
		// Business_b save_bodyVO=new Business_b();
		// String pk_org =
		// DefdocUtils.getUtils().getPk_org(jsonData.getString("name"));
		String pk_org = "000112100000000005FD";
		// if(StringUtils.isBlank(pk_org)){
		// throw new BusinessException("nc组织档案没有维护此组织，请联系管理员");
		// }
		// JSONArray jsonarr = JSONArray.fromObject(value.get("data"));
		Map<String, String> refMap = new HashMap<String, String>();
		// List<DefdocVO> list = JSONObject.parseArray(jsonarr.to,
		// DefdocVO.class);
		String pk_defdoclist = DefdocUtils.getUtils().getDefdoclist(
				EBSCont.getDocNameMap().get(dectype));
		// Collection<EBSDefdocVO> defdocvos=
		// JSONArray.toCollection(jsonarr,EBSDefdocVO.class);
		DefdocVO headVO = new DefdocVO();
		// for (EBSDefdocVO defdocheadVO : defdocvos) {
		headVO.setPk_group("000112100000000005FD");
		headVO.setPk_org(pk_org);
		headVO.setMemo((String) value.get("memo"));
		if (value.get("enablestate") != null
				&& !"".equals(value.get("enablestate"))) {
			headVO.setEnablestate("Y".equals(value.get("enablestate")) ? 2 : 3);
		} else {
			headVO.setEnablestate(2);
		}
		headVO.setDr(0);
		headVO.setStatus(VOStatus.NEW);// #UAP#
		headVO.setCreator("#UAP#");
		headVO.setModifier("#UAP#");
		if (StringUtils.isNotBlank((String) value.get("code"))) {
			headVO.setCode((String) value.get("code"));
		} else {
			throw new BusinessException("操作失败，成本科目编码是必填项，请检查参数设置");
		}
		if (StringUtils.isNotBlank((String) value.get("name"))
				&& StringUtils.isNotBlank((String) value.get("code"))) {
			headVO.setName((String) value.get("name"));
		} else {
			throw new BusinessException("操作失败，成本科目名称是必填项，请检查参数设置");
		}

		// 上级档案
		Map<String, String> map = DefdocUtils.getUtils().getDefdocPranetPk(
				value.get("ebs_parent_id").toString(), EBSBillUtils.getUtils()
				.getPkDefdocListByCode("zdy024"));
		if (null == map) {
			headVO.setPid(null);
		} else {
			headVO.setPid(map.get("pk_defdoc"));
		}
		// EBS传子项目档案id
		if (null != value.get("ebs_id") && !"".equals(value.get("ebs_id"))) {
			headVO.setDef1(value.get("ebs_id").toString());
		} else {
			throw new BusinessException("操作失败，成本科目id是必填项，请检查参数设置");
		}
		// EBS传母项目档案id
		if (null != value.get("ebs_parent_id")
				&& !"".equals(value.get("ebs_parent_id"))) {
			headVO.setDef2(value.get("ebs_parent_id").toString());
		} else {
			headVO.setDef2(null);
		}
		String billqueue = EBSCont.getDocNameMap().get(dectype) + ":"
				+ headVO.getCode();
		String billkey = EBSCont.getDocNameMap().get(dectype) + ":"
				+ headVO.getName();
		// TODO Saleid 按实际存入信息位置进行变更
		String pk_defdoc = null;
		try {
			EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
			// NCObject[] docVO = (NCObject[]) getHeadVO(
			// DefdocVO.class, "isnull(dr,0)=0 and code = '" + headVO.getCode()
			// + "'");

			Collection<DefdocVO> docVO = getBaseDAO().retrieveByClause(
					DefdocVO.class,
					"(pk_defdoclist = '"
							+ EBSBillUtils.getUtils().getPkDefdocListByCode(
									"zdy024")
							+ "' and isnull(dr,0)=0 and def1 = '"
							+ value.get("ebs_id") + "') ");
			if (docVO != null && docVO.size() > 0) {
				DefdocVO[] defdocVO = docVO.toArray(new DefdocVO[0]);
				defdocVO[0].setStatus(VOStatus.UPDATED);
				if (StringUtils.isNotBlank((String) value.get("name"))) {
					defdocVO[0].setName((String) value.get("name"));
				} else {
					throw new BusinessException("操作失败，成本科目名称是必填项，请检查参数设置");
				}
				if (StringUtils.isNotBlank((String) value.get("code"))) {
					defdocVO[0].setCode((String) value.get("code"));
				} else {
					throw new BusinessException("操作失败，成本科目编码是必填项，请检查参数设置");
				}
				if (value.get("enablestate") != null
						&& !"".equals(value.get("enablestate"))) {
					defdocVO[0].setEnablestate("Y".equals(value
							.get("enablestate")) ? 2 : 3);
				} else {
					defdocVO[0].setEnablestate(2);
				}
				defdocVO[0].setMemo((String) value.get("memo"));
				defdocVO[0].setCreator("#UAP#");
				defdocVO[0].setModifier("#UAP#");
				// 上级档案
				if (null == map) {
					defdocVO[0].setPid(null);
				} else {
					defdocVO[0].setPid(map.get("pk_defdoc"));
				}
				// EBS传子项目档案id
				if (null != value.get("ebs_id")
						&& !"".equals(value.get("ebs_id"))) {
					defdocVO[0].setDef1(value.get("ebs_id").toString());
				} else {
					throw new BusinessException("操作失败，成本科目id是必填项，请检查参数设置");
				}
				// EBS传母项目档案id
				if (null != value.get("ebs_parent_id")
						&& !"".equals(value.get("ebs_parent_id"))) {
					defdocVO[0].setDef2(value.get("ebs_parent_id").toString());
				} else {
					defdocVO[0].setDef2(null);
				}
				DefdocVO[] docVOs = defdocService.updateDefdocs(pk_org,
						defdocVO);
				pk_defdoc = docVOs[0].getPrimaryKey();
			} else {
				headVO.setPk_defdoclist(EBSBillUtils.getUtils()
						.getPkDefdocListByCode("zdy024"));
				DefdocVO[] docVOs = defdocService.insertDefdocs(pk_org,
						new DefdocVO[] { headVO });
				pk_defdoc = docVOs[0].getPrimaryKey();

			}
		} catch (LockFailedException e1) {
			throw new BusinessException("【" + billkey + "】已被上锁请稍后再次推送," + e1.getMessage(),
					e1);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			// 解锁2019-12-19-谈子健
			EBSBillUtils.removeBillQueue(billqueue);
			PKLock.getInstance().releaseLock(
					EBSBillUtils.getUtils().getPkDefdocListByCode("zdy024"),
					null, null);

			PKLock.getInstance().releaseLock(pk_defdoc, null, null);

		}
		refMap = new HashMap<String, String>();
		refMap.put("msg", "【" + billkey + "】," + "操作完成!");
		refMap.put("data", "");
		return JSON.toJSONString(refMap);
		// }
		// refMap.put("msg", "参数为空,请检查参数设置");
		// return JSON.toJSONString(refMap) ;
	}
}

// String srcid = headVO.getEbsid();// 销售系统业务单据ID
// String srcno = headVO.getEbsbillcode();// 销售系统业务单据单据号

// AggPayrequest billvo = onTranBill(value, dectype);
// HashMap eParam = new HashMap();
// eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
// PfUtilBaseTools.PARAM_NOTE_CHECKED);
// getPfBusiAction().processAction("SAVEBASE", "FN13", null, billvo,
// null, eParam);

