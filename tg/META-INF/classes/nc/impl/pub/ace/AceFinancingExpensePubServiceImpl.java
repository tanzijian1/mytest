package nc.impl.pub.ace;

import nc.bs.tg.financingexpense.ace.bp.AceFinancingExpenseApproveBP;
import nc.bs.tg.financingexpense.ace.bp.AceFinancingExpenseDeleteBP;
import nc.bs.tg.financingexpense.ace.bp.AceFinancingExpenseInsertBP;
import nc.bs.tg.financingexpense.ace.bp.AceFinancingExpenseSendApproveBP;
import nc.bs.tg.financingexpense.ace.bp.AceFinancingExpenseUnApproveBP;
import nc.bs.tg.financingexpense.ace.bp.AceFinancingExpenseUnSendApproveBP;
import nc.bs.tg.financingexpense.ace.bp.AceFinancingExpenseUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public abstract class AceFinancingExpensePubServiceImpl {
	// 新增
	public AggFinancexpenseVO[] pubinsertBills(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggFinancexpenseVO> transferTool = new BillTransferTool<AggFinancexpenseVO>(
					clientFullVOs);
			// 调用BP
			AceFinancingExpenseInsertBP action = new AceFinancingExpenseInsertBP();
			AggFinancexpenseVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceFinancingExpenseDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggFinancexpenseVO[] pubupdateBills(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggFinancexpenseVO> transferTool = new BillTransferTool<AggFinancexpenseVO>(
					clientFullVOs);
			AceFinancingExpenseUpdateBP bp = new AceFinancingExpenseUpdateBP();
			AggFinancexpenseVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggFinancexpenseVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggFinancexpenseVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggFinancexpenseVO> query = new BillLazyQuery<AggFinancexpenseVO>(
					AggFinancexpenseVO.class);
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
	public AggFinancexpenseVO[] pubsendapprovebills(
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills)
			throws BusinessException {
		AceFinancingExpenseSendApproveBP bp = new AceFinancingExpenseSendApproveBP();
		AggFinancexpenseVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggFinancexpenseVO[] pubunsendapprovebills(
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills)
			throws BusinessException {
		AceFinancingExpenseUnSendApproveBP bp = new AceFinancingExpenseUnSendApproveBP();
		AggFinancexpenseVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggFinancexpenseVO[] pubapprovebills(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFinancingExpenseApproveBP bp = new AceFinancingExpenseApproveBP();
		AggFinancexpenseVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggFinancexpenseVO[] pubunapprovebills(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFinancingExpenseUnApproveBP bp = new AceFinancingExpenseUnApproveBP();
		AggFinancexpenseVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}