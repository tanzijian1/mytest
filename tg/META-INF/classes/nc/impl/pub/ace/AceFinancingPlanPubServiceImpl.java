package nc.impl.pub.ace;

import nc.bs.tg.financingplan.ace.bp.AceFinancingPlanInsertBP;
import nc.bs.tg.financingplan.ace.bp.AceFinancingPlanUpdateBP;
import nc.bs.tg.financingplan.ace.bp.AceFinancingPlanDeleteBP;
import nc.bs.tg.financingplan.ace.bp.AceFinancingPlanSendApproveBP;
import nc.bs.tg.financingplan.ace.bp.AceFinancingPlanUnSendApproveBP;
import nc.bs.tg.financingplan.ace.bp.AceFinancingPlanApproveBP;
import nc.bs.tg.financingplan.ace.bp.AceFinancingPlanUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceFinancingPlanPubServiceImpl {
	// 新增
	public AggFinancingPlan[] pubinsertBills(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggFinancingPlan> transferTool = new BillTransferTool<AggFinancingPlan>(
					clientFullVOs);
			// 调用BP
			AceFinancingPlanInsertBP action = new AceFinancingPlanInsertBP();
			AggFinancingPlan[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceFinancingPlanDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggFinancingPlan[] pubupdateBills(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggFinancingPlan> transferTool = new BillTransferTool<AggFinancingPlan>(
					clientFullVOs);
			AceFinancingPlanUpdateBP bp = new AceFinancingPlanUpdateBP();
			AggFinancingPlan[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggFinancingPlan[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggFinancingPlan[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggFinancingPlan> query = new BillLazyQuery<AggFinancingPlan>(
					AggFinancingPlan.class);
			bills = query.query(queryScheme, null);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return bills;
	}

	/**
	 * 由子类实现，查询之前对queryScheme进行加工，加入自己的逻辑
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// 查询之前对queryScheme进行加工，加入自己的逻辑
	}

	// 提交
	public AggFinancingPlan[] pubsendapprovebills(
			AggFinancingPlan[] clientFullVOs, AggFinancingPlan[] originBills)
			throws BusinessException {
		AceFinancingPlanSendApproveBP bp = new AceFinancingPlanSendApproveBP();
		AggFinancingPlan[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggFinancingPlan[] pubunsendapprovebills(
			AggFinancingPlan[] clientFullVOs, AggFinancingPlan[] originBills)
			throws BusinessException {
		AceFinancingPlanUnSendApproveBP bp = new AceFinancingPlanUnSendApproveBP();
		AggFinancingPlan[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggFinancingPlan[] pubapprovebills(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFinancingPlanApproveBP bp = new AceFinancingPlanApproveBP();
		AggFinancingPlan[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggFinancingPlan[] pubunapprovebills(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFinancingPlanUnApproveBP bp = new AceFinancingPlanUnApproveBP();
		AggFinancingPlan[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}