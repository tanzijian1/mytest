package nc.bs.tg.outside.ebs.utils.landfundshare;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.tg.contractapportionment.ContractAptmentVO;

import com.alibaba.fastjson.JSONObject;

/**
 * 土地款分摊工单
 * 
 * @author king
 * 
 */
public class LandFundsShareUtils extends EBSBillUtils {

	static LandFundsShareUtils utils;

	/**
	 * 获取土地分摊工单实例
	 * 
	 * @return
	 */
	public static LandFundsShareUtils getUtils() {
		if (utils == null) {
			utils = new LandFundsShareUtils();
		}
		return utils;
	}

	/**
	 * 同步单据
	 * 
	 * @param value
	 *            传入的接送体
	 * @param dectype
	 *            目标单据
	 * @param srctype
	 *            业务单据
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// 设置用户码
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		// json数据
		JSONObject date = (JSONObject) value.get("data");

		JSONObject accrualHeadVO = (JSONObject) date.get("headInfo");// 获取表头信息

		String srcid = accrualHeadVO.getString("def1");// 外系统主键
		String srcno = accrualHeadVO.getString("def3");// 外系统单据号
		// 目标业务单据中文名队列
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcid;
		// 目标业务单据中文名键
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		// 根据销售系统业务单据ID查询的应付单聚合VO
		AggContractAptmentVO aggVO = (AggContractAptmentVO) getBillVO(
				AggContractAptmentVO.class, "isnull(dr,0)=0 and def1 = '"
						+ srcid + "'");

		// 不为空抛出错误
		if (aggVO != null) {
			throw new BusinessException("【"
					+ billkey
					+ "】,NC已存在对应的业务单据【"
					+ aggVO.getParentVO().getAttributeValue(
							PayableBillVO.BILLNO) + "】,请勿重复上传!");
		}
		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {
			// 将来源信息转换成NC信息
			AggContractAptmentVO billvo = onTranBill(value, dectype);
			HashMap eParam = new HashMap();
			// 工作项是否已检查
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			// 传入操作和编码
			getPfBusiAction().processAction("SAVEBASE", "FN02", null, billvo,
					null, eParam);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

		return "【" + billkey + "】," + "操作完成!";

	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 */
	private AggContractAptmentVO onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {
		// 表头VO
		AggContractAptmentVO aggvo = new AggContractAptmentVO();
		aggvo.getChildrenVO();
		ContractAptmentVO hvo = new ContractAptmentVO();
		// json数据
		JSONObject date = (JSONObject) value.get("data");
		// json数据头
		JSONObject headjson = (JSONObject) date.get("headInfo");

		String pk_org = headjson.getString("pk_org");
		if (!isNull(pk_org)) {
			throw new BusinessException("财务组织不能为空，请检查输入数据");
		}
		String pk_org_id = getRefAttributePk("pk_org", pk_org);
		hvo.setPk_org(pk_org_id);
		// 外系统主键
		String def1 = headjson.getString("def1");
		// 外系统单据号
		String def3 = headjson.getString("def3");
		// 土地通用合同编码
		String def4 = headjson.getString("def4");
		// 成本合同编码
		String def5 = headjson.getString("def5");
		// 分摊前项目分期编码
		String def6 = getRefAttributePk("bd_project",
				headjson.getString("def6"), pk_org_id, pk_org_id);
		// 分摊后项目分期编码
		String def7 = getRefAttributePk("bd_project",
				headjson.getString("def7"), pk_org_id, pk_org_id);
		// 分摊金额
		Double def9 = headjson.getDouble("def9");
		// 单据日期
		hvo.setDbilldate(new UFDate());

		hvo.setPk_group("000112100000000005FD");

		if (isNull(def1)) {
			hvo.setDef1(def1);
		} else {
			throw new BusinessException("外系统主键不能为空，请检查输入数据");
		}
		// 默认
		hvo.setDef2("EBS系统成本");
		hvo.setStatus(VOStatus.NEW);
		hvo.setApprovestatus(-1);// 审批状态
		hvo.setBilltype("FN02");//单据类型
		if (isNull(def3)) {
			hvo.setDef3(def3);
		} else {
			throw new BusinessException("外系统单据号不能为空，请检查输入数据");
		}

		if (isNull(def4)) {
			hvo.setDef4(def4);
		} else {
			throw new BusinessException("土地通用合同编码不能为空，请检查输入数据");
		}

		if (isNull(def5)) {
			hvo.setDef5(def5);
		} else {
			throw new BusinessException("成本合同编码不能为空，请检查输入数据");
		}
		if (isNull(def6)) {
			hvo.setDef6(def6);
		} else {
			throw new BusinessException("分摊前项目分期编码不能为空，请检查输入数据");
		}
		if (isNull(def7)) {
			hvo.setDef7(def7);
		} else {
			throw new BusinessException("分摊后项目分期编码不能为空，请检查输入数据");
		}
		if (isNull(def9)) {
			hvo.setDef9(def9.toString());
		} else {
			throw new BusinessException("分摊金额不能为空，请检查输入数据");
		}
		aggvo.setParentVO(hvo);
		return aggvo;
	}

	/**
	 * 根据传入的键名
	 * 
	 * @param key
	 *            编码
	 * @param conditions
	 *            数据库数据
	 * @return
	 * @throws DAOException
	 */
	private String getRefAttributePk(String key, String... conditions)
			throws BusinessException {
		String code = conditions[0];
		String pkValue = null;
		String sql = null;
		BaseDAO dao = getBaseDAO();
		SQLParameter parameter = new SQLParameter();
		if ("bd_project".equals(key)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			sql = "select  bd_project.pk_project from bd_project bd_project  left   join  bd_project_b b on bd_project.PK_PROJECT=b.PK_PROJECT   where "
					+ "project_code = ? and ( bd_project.dr = 0  ) and (enablestate = 2)  and ( deletestate is null or deletestate<>1) and (   b.dr = 0)  and ("
					+ "b.PK_PARTI_ORG=?  or " + "b.PK_ORG =? )";

			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表头项目分期编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (Exception e) {
				throw new BusinessException("表头项目分期编码" + code + "未能在NC档案中关联");
			}
		} else if ("pk_org".equals(key)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			sql = "SELECT pk_org from org_orgs where code = ? and enablestate = 2 and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表头财务组织" + code + "未能在NC档案中关联");
				}
			} catch (Exception e) {
				throw new BusinessException("表头财务组织" + code + "未能在NC档案中关联");
			}
		}
		return pkValue;
	}

	/**
	 * 判空方法
	 * 
	 * @param obj
	 * @return
	 */
	public Boolean isNull(Object obj) {
		if (obj != null) {
			return true;
		}
		return false;

	}

}
