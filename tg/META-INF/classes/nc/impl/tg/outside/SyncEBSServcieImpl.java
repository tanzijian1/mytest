package nc.impl.tg.outside;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.ebs.pub.PushEbsDataUtils;
import nc.bs.tg.outside.ebs.pub.PushSrmDataUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.IncomeBillWorkorder.DataChangeUtils;
import nc.bs.tg.outside.ebs.utils.Invoicing.InvoicingUtils;
import nc.bs.tg.outside.ebs.utils.appayPayrequest.ApayRequestutil;
import nc.bs.tg.outside.ebs.utils.appaybill.ApPayBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.WareToReceivableUtils;
import nc.bs.tg.outside.ebs.utils.costadvance.CostAdvanceBillUtil;
import nc.bs.tg.outside.ebs.utils.ctar.CtArBillUtil;
import nc.bs.tg.outside.ebs.utils.ebsutils.BudgetSubjectUtils;
import nc.bs.tg.outside.ebs.utils.ebsutils.CarDeptUtils;
import nc.bs.tg.outside.ebs.utils.ebsutils.CostSubjectUtils;
import nc.bs.tg.outside.ebs.utils.ebsutils.FloorDeptUtils;
import nc.bs.tg.outside.ebs.utils.ebsutils.FormatUtils;
import nc.bs.tg.outside.ebs.utils.ebsutils.InoutbusiclassUtils;
import nc.bs.tg.outside.ebs.utils.ebsutils.PlateBillUtils;
import nc.bs.tg.outside.ebs.utils.ebsutils.ProjectBillUtils;
import nc.bs.tg.outside.ebs.utils.ebsutils.PsnBankAccountUtils;
import nc.bs.tg.outside.ebs.utils.fctap.FctapCostBillUtils;
import nc.bs.tg.outside.ebs.utils.fctap.FctapMaterialBillUtils;
import nc.bs.tg.outside.ebs.utils.fctap.FctapOutBillUtils;
import nc.bs.tg.outside.ebs.utils.jkbxbill.BXBillUtils;
import nc.bs.tg.outside.ebs.utils.landfundshare.LandFundsShareUtils;
import nc.bs.tg.outside.ebs.utils.margin.MarginPayment;
import nc.bs.tg.outside.ebs.utils.margin.MarginReceivableUtils;
import nc.bs.tg.outside.ebs.utils.marginworkorder.MarginwWorkorderUtils;
import nc.bs.tg.outside.ebs.utils.outputinvoice.MoreOutputinvoiceUtil;
import nc.bs.tg.outside.ebs.utils.outputinvoice.OutputinvoiceUtil;
import nc.bs.tg.outside.ebs.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.ebs.utils.reachgoods.ReachGoodsUtils;
import nc.bs.tg.outside.ebs.utils.recbill.RecbillUtils;
import nc.bs.tg.outside.ebs.utils.recebill.ReceBillUtils;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.outside.EBSCont;
import nc.itf.tg.outside.ISaveLogService;
import nc.itf.tg.outside.ISyncEBSServcie;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.NcToEbsLogVO;
import nc.vo.tgfn.invoicing.AggInvoicingHead;

import com.alibaba.fastjson.JSONObject;

public class SyncEBSServcieImpl implements ISyncEBSServcie {
	BaseDAO baseDAO = null;

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	IPFBusiAction pfBusiAction = null;

	IMDPersistenceQueryService mdQryService = null;

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	@Override
	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String dectype, String srctype) throws BusinessException {
		String result = null;
		if (EBSCont.BILL_01.equals(dectype)) {// 费用预提单
			result = CostAdvanceBillUtil.getUtils().onSyncBill(value, dectype,
					srctype);
		} else if (EBSCont.BILL_02.equals(dectype)) {// 进项发票
			result = DataChangeUtils.getUtils().onSyncBill(value, srctype);

		} else if (EBSCont.BILL_03.equals(dectype)
				|| EBSCont.BILL_31.equals(dectype)
				|| EBSCont.BILL_32.equals(dectype)) {// 应付单
			if (EBSCont.SRCBILL_43.equals(srctype)) {// 成本应付单挂起后动作
				ApPayBillUtils.getUtils().onSyncBillUpAndDown(value, srctype);
			} else {
				result = ApPayBillUtils.getUtils().onSyncBill(value, srctype);
			}
		} else if (EBSCont.BILL_21.equals(dectype)) {// 报销单 add by tjl
														// 2020-01-13
			result = BXBillUtils.getUtils().onSyncBill(value, dectype, srctype);
		} else if (EBSCont.BILL_04.equals(dectype)) {// 付款申请单
			result = PayBillUtils.getUtils()
					.onSyncBill(value, dectype, srctype);
		} else if (EBSCont.BILL_12.equals(dectype)) {// 应收单
			result = ReceBillUtils.getUtils().onSyncBill(value, dectype,
					srctype);
		} else if (EBSCont.BILL_06.equals(dectype)) {// 收款合同
			result = CtArBillUtil.getUtils()
					.onSyncBill(value, dectype, srctype);
		} else if (EBSCont.BILL_05.equals(dectype)) {
			if (EBSCont.SRCBILL_08.equals(srctype)) {// 通用合同
				result = FctapOutBillUtils.getUtils().onSyncBill(value,
						dectype, srctype);
			} else if (EBSCont.SRCBILL_38.equals(srctype)) {// 成本合同
				result = FctapCostBillUtils.getUtils().onSyncBill(value,
						dectype, srctype);
			} else if (EBSCont.SRCBILL_39.equals(srctype)) {// EBS-采购协议单
				result = FctapMaterialBillUtils.getUtils().onSyncBill(value,
						dectype, srctype);
			}
		} else if (EBSCont.BILL_13.equals(dectype)) {
			// 领用出库单
			result = RecbillUtils.getUtils()
					.onSyncBill(value, dectype, srctype);
		} else if (EBSCont.BILL_10.equals(dectype)) {
			// 到货单
			result = ReachGoodsUtils.getUtils().onSyncBill(value, dectype,
					srctype);
		} else if (EBSCont.BILL_08.equals(dectype)) {
			// 土地分摊工单
			result = LandFundsShareUtils.getUtils().onSyncBill(value, dectype,
					srctype);
		} else if (EBSCont.BILL_15.equals(dectype)) {
			// 销项发票开票
			result = OutputinvoiceUtil.getUtils().onSyncBill(value, dectype,
					srctype);
		} else if (EBSCont.BILL_16.equals(dectype)) {
			// 保证金工单
			result = MarginwWorkorderUtils.getUtils().onSyncBill(value,
					dectype, srctype);
		} else if (EBSCont.BILL_19.equals(dectype)) { // SRM-出库->应收单
			result = WareToReceivableUtils.getUtils().onSyncBill(value,
					dectype, srctype);
		} else if (EBSCont.BILL_20.equals(dectype)) { // SRM-保证金->收款单
			result = MarginPayment.getUtils().onSyncBill(value, dectype,
					srctype);
		} else if (EBSCont.BILL_22.equals(dectype)) { // SRM-保证金->应收单
			result = MarginReceivableUtils.getUtils().onSyncBill(value,
					dectype, srctype);
		} else if (EBSCont.BILL_25.equals(dectype)) { // EBS通用->应付与付款申请单
			result = ApayRequestutil.getUtils().onSyncBill(value, dectype,
					srctype);
		} else if (EBSCont.BILL_23.equals(dectype)) { // EBS成本->应付单与付款申请单
			result = ApayRequestutil.getUtils().onSyncBill(value, dectype,
					srctype);
		} else if (EBSCont.BILL_26.equals(dectype)) {// 销售系统->批量销项发票
			result = MoreOutputinvoiceUtil.getUtils().onSyncBill(value,
					dectype, srctype);
		} else if (EBSCont.BILL_27.equals(dectype)) {// SRM供应链对供应商对账信息【开票工单】
			result = InvoicingUtils.getUtils().onSyncBill(value, dectype,
					srctype);
		} else if (EBSCont.BILL_28.equals(dectype)) {// 成本税差应付单
			result = ApPayBillUtils.getUtils().onSyncBill(value, srctype);
		} else if (EBSCont.BILL_29.equals(dectype)) {// 成本占预算应付单
			result = ApPayBillUtils.getUtils().onSyncBill(value, srctype);
		} else if (EBSCont.BILL_30.equals(dectype)) { // EBS成本->应付单与付款申请单
			result = ApayRequestutil.getUtils().onSyncBill(value, dectype,
					srctype);
		}
		return result;
	}

	@Override
	public String onSyncDoc_RequiresNew(HashMap<String, Object> value,
			String decdoc) throws BusinessException {
		String result = null;
		if (EBSCont.DOC_01.equals(decdoc)) {// 项目档案
			result = ProjectBillUtils.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_02.equals(decdoc)) {// 项目公司

		} else if (EBSCont.DOC_10.equals(decdoc)) {// 业态
			result = FormatUtils.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_12.equals(decdoc)) {// 预算科目
			result = BudgetSubjectUtils.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_13.equals(decdoc)) {// 成本科目
			result = CostSubjectUtils.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_14.equals(decdoc)) {// 收支项目
			result = InoutbusiclassUtils.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_15.equals(decdoc)) {// 板块
			result = PlateBillUtils.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_16.equals(decdoc)) {// EBS银行档案
			result = nc.bs.tg.outside.ebs.utils.ebsutils.BankArchivesUtils
					.getUtils().onSyncBill(value, decdoc, "EBS");
		} else if (EBSCont.DOC_17.equals(decdoc)) {// EBS银行账户
			result = nc.bs.tg.outside.ebs.utils.ebsutils.BankAccountUtils
					.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_18.equals(decdoc)) {// EBS结算方式
			result = nc.bs.tg.outside.ebs.utils.ebsutils.SettlementMethodUtils
					.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_19.equals(decdoc)) {// 车辆部门
			result = CarDeptUtils.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_20.equals(decdoc)) {// 部门楼层
			result = FloorDeptUtils.getUtils().onSyncBill(value, decdoc);
		} else if (EBSCont.DOC_22.equals(decdoc)) {// 个人银行账户
			result = PsnBankAccountUtils.getUtils().onSyncBill(value, decdoc);
		}

		return result;
	}

	@Override
	public Map<String, String> onPushEBSPayData_RequiresNew(String settid,
			String code, String syscode, String token, Object postdata,
			String registryName, String pk_paybill) throws BusinessException {
		Map<String, String> infoMap;
		try {

			if ("SRM付款完成".equals(registryName)) {
				infoMap = PushSrmDataUtils.getUtils().pushBillToSRM(code,
						syscode, token, postdata, registryName);
			} else {
				infoMap = PushEbsDataUtils.getUtils().pushBillToEBS(code,
						syscode, token, postdata, registryName);
			}

			if ("S".equals(infoMap.get("code"))) {

				insertFlag(pk_paybill);// 传ebs成功后更新付款结算单状态

				EBSBillUtils
						.getUtils()
						.getBaseDAO()
						.executeUpdate(
								"update cmp_detail set def1 = 'Y' where pk_settlement = '"
										+ settid + "'");
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return infoMap;
	}

	@Override
	public Map<String, String> onPushEBSInvData_RequiresNew(String settid,
			String code, String syscode, String token, Object postdata)
			throws BusinessException {
		Map<String, String> infoMap;
		try {
			infoMap = PushEbsDataUtils.getUtils().pushBillToEBS(code, syscode,
					token, postdata, null);
			if ("S".equals(infoMap.get("code"))) {

				EBSBillUtils
						.getUtils()
						.getBaseDAO()
						.executeUpdate(
								"update cmp_settlement set def2 = 'Y' where pk_settlement = '"
										+ settid + "'");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return infoMap;
	}

	/**
	 * 已回写需标上标识
	 * 
	 * @param pk
	 * @throws BusinessException
	 */
	private void insertFlag(String pk) throws BusinessException {
		PayBillVO headvo = (PayBillVO) EBSBillUtils.getUtils().getBaseDAO()
				.retrieveByPK(PayBillVO.class, pk);
		headvo.setStatus(VOStatus.UPDATED);
		headvo.setDef61("Y");
		headvo.setDr(0);
		EBSBillUtils.getUtils().getBaseDAO().updateVO(headvo);
	}

	@Override
	public Map<String, String> onPushEBSReceivablesData_RequiresNew(
			String settid, String code, String syscode, String token,
			Object postdata, String registryName) throws BusinessException {
		Map<String, String> infoMap;
		try {
			infoMap = PushEbsDataUtils.getUtils().pushBillToEBS(code, syscode,
					token, postdata, registryName);
			if ("S".equals(infoMap.get("code"))) {
				EBSBillUtils
						.getUtils()
						.getBaseDAO()
						.executeUpdate(
								"update cmp_settlement set def3 = 'Y' where pk_settlement = '"
										+ settid + "'");
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return infoMap;
	}

	@Override
	public void syncExecutionDataUpdateExecution_RequiresNew(AggCtApVO aggVO,
			String title, String msg) throws BusinessException {
		NcToEbsLogVO logvo = new NcToEbsLogVO();
		String vbillcode = "";
		if (aggVO != null) {
			vbillcode = aggVO.getParentVO().getVbillcode();
		}
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		logvo.setTaskname(title);
		logvo.setSrcsystem("BXD/EBS");
		logvo.setOperator("NC");
		logvo.setDr(0);
		logvo.setExedate(new UFDateTime().toString());

		try {
			/**
			 * 设置合同一些新加字段-2020-06-19-谈子健
			 * 
			 */
			// aggVO = setFctApBillDate(aggVO);
			AggCtApVO billVO = ((AggCtApVO[]) getPfBusiAction().processAction(
					"MODIFY", "FCT1", null, aggVO, null, null))[0];

			logvo.setNcparm("单据号:" + vbillcode + "表体执行情况更新成功");
			logvo.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
		} catch (Exception e) {
			logvo.setMsg("表体执行情况更新失败:" + e.getMessage());
			logvo.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
		} finally {
			saveLog.SaveLog_RequiresNew(logvo);
		}
	}

	@Override
	public String MakeInvoiceApproveUpdate(AggInvoicingHead aggvo,
			JSONObject json) throws BusinessException {

		StringBuffer sb = new StringBuffer();

		HashMap eParam = new HashMap();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);

		// try {

		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		try {
			AggInvoicingHead aggvo1 = (AggInvoicingHead) getBillVO(
					AggInvoicingHead.class, "isnull(dr,0)=0 and billno = '"
							+ aggvo.getParentVO().getBillno() + "'");

			AggInvoicingHead[] billvo = (AggInvoicingHead[]) getPfBusiAction()
					.processAction("SAVE", "FN23", null, aggvo1, null, eParam);

			getPfBusiAction().processAction("APPROVE", "FN23", null, billvo[0],
					null, eParam);

			String reconciliationid = aggvo.getParentVO().getDef1();
			String reconciliationno = aggvo.getParentVO().getDef2();
			Redrush(reconciliationid, reconciliationno);
			sb.append("成功");
		} catch (Exception e) {
			sb.append("红冲失败");
		}

		return sb.toString();
	}

	private String Redrush(String reconciliationid, String reconciliationno)
			throws Exception {

		StringBuffer sb = new StringBuffer();

		// 应收单聚合vo
		AggReceivableBillVO reaggVO = (AggReceivableBillVO) getBillVO(
				AggReceivableBillVO.class, "isnull(dr,0)=0 and def29 = '"
						+ reconciliationid + "' and def30 = '"
						+ reconciliationno + "' and def28 <> 'Y'");
		// 应付单聚合vo
		AggPayableBillVO payaggVO = (AggPayableBillVO) getBillVO(
				AggPayableBillVO.class, "isnull(dr,0)=0 and def35 = '"
						+ reconciliationid + "' and def36 = '"
						+ reconciliationno + "' and def37 <> 'Y'");

		if (reaggVO != null) {

			ReceivableBillVO recvo = (ReceivableBillVO) reaggVO.getParentVO()
					.clone();

			AggReceivableBillVO aggReceivableBillVO = reaggVO;

			ReceivableBillVO parent = (ReceivableBillVO) aggReceivableBillVO
					.getParentVO();

			parent.setPrimaryKey(null);
			parent.setBillno(null);

			ReceivableBillItemVO[] childs = (ReceivableBillItemVO[]) aggReceivableBillVO
					.getChildrenVO();
			for (ReceivableBillItemVO child : childs) {
				child.setPrimaryKey(null);
				child.setDef6(new UFDouble((String) child.getDef6()).multiply(
						-1).toString());
				child.setDef7(new UFDouble((String) child.getDef7()).multiply(
						-1).toString());
				child.setDef9(new UFDouble((String) child.getDef9()).multiply(
						-1).toString());
				child.setDef10(new UFDouble((String) child.getDef10())
						.multiply(-1).toString());
				child.setDef11(new UFDouble((String) child.getDef11())
						.multiply(-1).toString());
				child.setDef12(new UFDouble((String) child.getDef12())
						.multiply(-1).toString());
				child.setLocal_money_de(child.getLocal_money_de().multiply(-1));
				child.setNotax_de(child.getNotax_de().multiply(-1));
				child.setLocal_tax_de(child.getLocal_tax_de().multiply(-1));
				child.setMoney_de(child.getMoney_de().multiply(-1));
				child.setMoney_cr(child.getMoney_cr().multiply(-1));
				child.setMoney_bal(child.getMoney_bal().multiply(-1));
			}
			// parent.setMoney(parent.getMoney().multiply(-1).sub(5));
			// parent.setLocal_money(parent.getLocal_money().multiply(-1));
			parent.setDef28("Y");
			parent.setEffectstatus(0);

			getPfBusiAction().processAction("SAVE", "F0-Cxx-001", null,
					aggReceivableBillVO, null, null);

			recvo.setStatus(VOStatus.UPDATED);
			recvo.setDef28("Y");
			recvo.setDr(0);

			NCLocator.getInstance().lookup(ISaveLogService.class)
					.updateVO(recvo);
		} else {
			sb.append("对应应收单已红冲或不存在");
		}

		if (payaggVO != null) {

			PayableBillVO payvo = (PayableBillVO) payaggVO.getParentVO()
					.clone();

			AggPayableBillVO aggPayableBillVO = payaggVO;

			PayableBillVO parent = (PayableBillVO) aggPayableBillVO
					.getParentVO();

			parent.setPrimaryKey(null);
			parent.setBillno(null);

			PayableBillItemVO[] childs = (PayableBillItemVO[]) aggPayableBillVO
					.getChildrenVO();

			for (PayableBillItemVO child : childs) {
				child.setPrimaryKey(null);
				child.setDef20(new UFDouble((String) child.getDef20())
						.multiply(-1).toString());
				child.setDef60(new UFDouble((String) child.getDef60())
						.multiply(-1).toString());
				child.setDef22(new UFDouble((String) child.getDef22())
						.multiply(-1).toString());
				child.setDef24(new UFDouble((String) child.getDef24())
						.multiply(-1).toString());
				child.setDef25(new UFDouble((String) child.getDef25())
						.multiply(-1).toString());
				child.setDef12(new UFDouble((String) child.getDef12())
						.multiply(-1).toString());
				child.setLocal_money_cr(child.getLocal_money_cr().multiply(-1));
				child.setLocal_tax_cr(child.getLocal_tax_cr().multiply(-1));
				child.setLocal_notax_cr(child.getLocal_notax_cr().multiply(-1));
				child.setMoney_de(child.getMoney_de().multiply(-1));
				child.setMoney_cr(child.getMoney_cr().multiply(-1));
				child.setMoney_bal(child.getMoney_bal().multiply(-1));
			}
			// parent.setLocal_money(parent.getLocal_money().multiply(-1));
			// parent.setMoney(parent.getMoney().multiply(-1));
			parent.setDef37("Y");
			parent.setEffectstatus(0);

			getPfBusiAction().processAction("SAVE", "F1-Cxx-006", null,
					aggPayableBillVO, null, null);

			payvo.setStatus(VOStatus.UPDATED);
			payvo.setDef37("Y");
			payvo.setDr(0);

			NCLocator.getInstance().lookup(ISaveLogService.class)
					.updateVO(payvo);
		} else {
			sb.append("对应的应付单已红冲或不存在");
		}
		if (sb != null) {
			if ("".equals(sb.toString())) {
				sb.append("红冲成功");
			}
		}

		return sb == null ? "红冲成功" : sb.toString();
	}

	/**
	 * 读取业务单据聚合VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}

	/**
	 * 元数据持久化查询接口
	 * 
	 * @return
	 */
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}

}
