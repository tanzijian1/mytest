package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 通用(EBS)付款合同生成NC应付单(周期计提)
 * 
 * @author ASUS
 * 
 */
public class PayablebillCreateForProvision extends PayableBillUtils {

	/**
	 * 周期计提应付单
	 */
	@Override
	protected String getTradetype() {
		return "F1-Cxx-LL04";
	}

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(getDefaultoperator()));
		InvocationInfoProxy.getInstance().setUserCode(getDefaultoperator());
		// 外系统信息
		JSONObject data = (JSONObject) info.get("data");// 外系统来源表头数据
		JSONObject jsonhead = (JSONObject) data.get("headInfo");// 外系统来源表头数据
		String srcid = jsonhead.getString("srcid") + "&"
				+ jsonhead.getString("period");// 来源ID
		String srcno = jsonhead.getString("srcbillno");// 来源单据号

		Map<String, String> resultInfo = new HashMap<String, String>();
		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
					AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【"
						+ billkey
						+ "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "】,请勿重复上传!");
			}
			AggPayableBillVO billvo = onTranBill(info);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			WorkflownoteVO worknoteVO = getWorkflowMachine().checkWorkFlow(
					"SAVE", billvo.getHeadVO().getPk_tradetype(), billvo,
					new HashMap<String, Object>());
			Object obj = getPfBusiAction().processAction("SAVE",
					billvo.getHeadVO().getPk_tradetype(), worknoteVO, billvo,
					null, null);
			AggPayableBillVO[] billvos = (AggPayableBillVO[]) obj;
			if (isPushApprove()) {
				approveSilently(billvo.getHeadVO().getPk_tradetype(),
						billvos[0].getPrimaryKey(), "Y", "审核通过", billvos[0]
								.getHeadVO().getCreator(), false);
			}

			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayableBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("org"))) {
			throw new BusinessException("出账公司不可为空");
		}
		// if (StringUtils.isBlank(head.getString("billdate"))) {
		// throw new BusinessException("制单日期不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("srcbillno"))) {
		// throw new BusinessException("请款单据号不可为空");
		// }
		if (StringUtils.isBlank(head.getString("period"))) {
			throw new BusinessException("月份不可为空");
		}

		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("EBS主键不可为空");
		}
		if (StringUtils.isBlank(head.getString("contcode"))) {
			throw new BusinessException("合同编号不可为空");
		}
		// if (StringUtils.isBlank(head.getString("contname"))) {
		// throw new BusinessException("*合同名称不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("conttype"))) {
		// throw new BusinessException("合同类型不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("contcell"))) {
		// throw new BusinessException("合同细类不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("supplier"))) {
		// throw new BusinessException("供应商不可为空");
		// }

	}

	@Override
	protected void setHeaderVO(PayableBillVO hvo, JSONObject head)
			throws BusinessException {

		checkHeaderNotNull(head);

		Map<String, String> orgInfo = DocInfoQryUtils.getUtils().getOrgInfo(
				head.getString("org"));
		if (orgInfo == null) {
			throw new BusinessException("出账公司[" + head.getString("org")
					+ "]未能在NC关联到相关信息");
		}
		hvo.setAttributeValue(PayableBillVO.PK_ORG, orgInfo.get("pk_org"));// 应付财务组织->NC业务单元编码

		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// 往来对象

		// bpmid
		hvo.setAttributeValue("def1", head.getString("srcid"));// 自定义项1 外系统主键
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// 自定义项2
																	// 外系统单据号->付款申请号
		Map<String, String> contInfo = DocInfoQryUtils.getUtils()
				.getPayContInfo(head.getString("contcode"));
		if (contInfo == null) {
			throw new BusinessException("合同[" + head.getString("contcode")
					+ "]未能在NC付款合同关联到相关信息");
		}

		hvo.setAttributeValue("def5", contInfo.get("contcode"));// 自定义项5
																// 合同编码->合同编码
		hvo.setAttributeValue("def6", contInfo.get("contname"));// 合同名称->合同名称
																// def6
		hvo.setAttributeValue("def7", contInfo.get("conttype"));// 自定义项7/合同类型
		// 合同细类->合同细类
		hvo.setAttributeValue("def8", contInfo.get("contcell"));// 自定义项8
																// 合同细类->合同细类

		String period = head.getString("period");
		String[] splitStr = period.split("-");

		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR, splitStr[0]);// 单据会计年度
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD, splitStr[1]);// 单据会计期间

		hvo.setAttributeValue("def10", "通用-EBS");// 自定义项10/来源外部系统
		// String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
		// head.getString("supplier"), hvo.getPk_org(), hvo.getPk_group());
		// if (pk_supplier == null) {
		// throw new BusinessException("供应商【" + head.getString("supplier")
		// + "】未能在NC档案查询到相关信息");
		// }
		String pk_supplier = contInfo.get("supplier");
		if (StringUtils.isBlank(pk_supplier) || "~".equals(pk_supplier)) {
			throw new BusinessException("付款合同【" + head.getString("contcode")
					+ "】 无收款方信息!");
		}
		hvo.setAttributeValue("supplier", pk_supplier);// 供应商
		hvo.setAttributeValue("scomment", head.getString("scomment"));// *请款事由/事由
		hvo.setAttributeValue("def20", head.getString("iscycle"));// 是否周期性计提/自定义项20
		hvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.FALSE);// 是否红冲过/自定义项16
		
		hvo.setAttributeValue("def83", contInfo.get("conttypeid"));// 自定义项7/合同类型ID
		// 合同细类->合同细类
		hvo.setAttributeValue("def84", contInfo.get("contcellid"));// 自定义项8
																	// 合同细类->合同细类ID
		
	}

	@Override
	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			JSONObject body) throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(PayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// 所属集团
		itemVO.setAttributeValue(PayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// 单据类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// 交易类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// 交易类型ID
		itemVO.setAttributeValue(PayableBillItemVO.BILLDATE,
				headVO.getBilldate());// 单据日期
		itemVO.setAttributeValue(PayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// 起算日期
		itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
				headVO.getPk_deptid());// 部门
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// 部 门
		itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
				headVO.getPk_psndoc());// 业务员

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, headVO.getObjtype());// 往来对象

		itemVO.setAttributeValue(PayableBillItemVO.DIRECTION,
				BillEnumCollection.Direction.CREDIT.VALUE);// 方向

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayableBillItemVO.PAUSETRANSACT,
				UFBoolean.FALSE);// 挂起标志
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// 购销类型
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// 三角贸易
		itemVO.setAttributeValue(PayableBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// 币种
		itemVO.setAttributeValue(PayableBillItemVO.RATE, headVO.getRate());// 组织本币汇率
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// 收货国
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// 扣税类别
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER,
				headVO.getSupplier());// 供应商
		itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
				headVO.getScomment());// 摘要

		String budgetsub = body.getString("budgetsub");// 预算科目
		HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
				.getBudgetsubInfo(budgetsub);
		if (budgetsubInfo == null) {
			throw new BusinessException("预算科目[" + budgetsub
					+ "]未能在NC档案关联到相关信息!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.DEF1,
				budgetsubInfo.get("pk_obj"));//

		String project = body.getString("budgetproject");// 项目名称
		HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
				.getProjectInfo(project);
		if (projectInfo == null) {
			throw new BusinessException("项目名称[" + project + "]未能在NC档案关联到相关信息!");
		}

		itemVO.setAttributeValue(PayableBillItemVO.PROJECT,
				projectInfo.get("pk_project"));// 项目名称

		itemVO.setAttributeValue(PayableBillItemVO.DEF4,
				body.getString("formatratio"));// 拆分比例/自定项4

		itemVO.setAttributeValue(PayableBillItemVO.DEF6,
				body.getString("budgetyear"));// 预算年度/自定项6
		itemVO.setAttributeValue(PayableBillItemVO.DEF7,
				body.getString("budgetmoney"));// 预算金额（元）/自定项7

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, UFDouble.ZERO_DBL);// 税率
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, body
				.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr")));// 借方原币金额 //
		// 价税合计->付款计划明细金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// 本币金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR,
				itemVO.getMoney_cr());// 不含税金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR,
				UFDouble.ZERO_DBL);// 税额

		itemVO.setAttributeValue(PayableBillItemVO.DEF31,
				projectInfo.get("note"));// 说明/自定义项31

	}

	/**
	 * 检验行信息必录
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		if (StringUtils.isBlank(body.getString("budgetsub"))) {
			throw new BusinessException("预算科目不可为空");
		}
		if (StringUtils.isBlank(body.getString("formatratio"))) {
			throw new BusinessException("拆分比例不可为空");
		}
		if (StringUtils.isBlank(body.getString("budgetyear"))) {
			throw new BusinessException("预算年度不可为空");
		}
		if (StringUtils.isBlank(body.getString("budgetmoney"))) {
			throw new BusinessException("预算金额（元）不可为空");
		}
		if (StringUtils.isBlank(body.getString("budgetproject"))) {
			throw new BusinessException("项目名称不可为空");
		}
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("计提金额不可为空");
		}

	}

	/**
	 * 是否推审核
	 * 
	 * @return
	 */
	public boolean isPushApprove() {
		return true;
	}
}
