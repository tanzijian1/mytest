package nc.impl.pub.ace;

import nc.bs.tg.moonfinancingplan.ace.bp.AceMoonFinancingPlanInsertBP;
import nc.bs.tg.moonfinancingplan.ace.bp.AceMoonFinancingPlanUpdateBP;
import nc.bs.tg.moonfinancingplan.ace.bp.AceMoonFinancingPlanDeleteBP;
import nc.bs.tg.moonfinancingplan.ace.bp.AceMoonFinancingPlanSendApproveBP;
import nc.bs.tg.moonfinancingplan.ace.bp.AceMoonFinancingPlanUnSendApproveBP;
import nc.bs.tg.moonfinancingplan.ace.bp.AceMoonFinancingPlanApproveBP;
import nc.bs.tg.moonfinancingplan.ace.bp.AceMoonFinancingPlanUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceMoonFinancingPlanPubServiceImpl {
	// 新增
	public AggMoonFinancingPlan[] pubinsertBills(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggMoonFinancingPlan> transferTool = new BillTransferTool<AggMoonFinancingPlan>(
					clientFullVOs);
			// 调用BP
			AceMoonFinancingPlanInsertBP action = new AceMoonFinancingPlanInsertBP();
			AggMoonFinancingPlan[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceMoonFinancingPlanDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggMoonFinancingPlan[] pubupdateBills(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggMoonFinancingPlan> transferTool = new BillTransferTool<AggMoonFinancingPlan>(
					clientFullVOs);
			AceMoonFinancingPlanUpdateBP bp = new AceMoonFinancingPlanUpdateBP();
			AggMoonFinancingPlan[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggMoonFinancingPlan[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggMoonFinancingPlan[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggMoonFinancingPlan> query = new BillLazyQuery<AggMoonFinancingPlan>(
					AggMoonFinancingPlan.class);
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
	public AggMoonFinancingPlan[] pubsendapprovebills(
			AggMoonFinancingPlan[] clientFullVOs, AggMoonFinancingPlan[] originBills)
			throws BusinessException {
		AceMoonFinancingPlanSendApproveBP bp = new AceMoonFinancingPlanSendApproveBP();
		AggMoonFinancingPlan[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggMoonFinancingPlan[] pubunsendapprovebills(
			AggMoonFinancingPlan[] clientFullVOs, AggMoonFinancingPlan[] originBills)
			throws BusinessException {
		AceMoonFinancingPlanUnSendApproveBP bp = new AceMoonFinancingPlanUnSendApproveBP();
		AggMoonFinancingPlan[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggMoonFinancingPlan[] pubapprovebills(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceMoonFinancingPlanApproveBP bp = new AceMoonFinancingPlanApproveBP();
		AggMoonFinancingPlan[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggMoonFinancingPlan[] pubunapprovebills(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceMoonFinancingPlanUnApproveBP bp = new AceMoonFinancingPlanUnApproveBP();
		AggMoonFinancingPlan[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}