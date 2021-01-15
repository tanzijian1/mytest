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
	// ����
	public AggFinancexpenseVO[] pubinsertBills(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggFinancexpenseVO> transferTool = new BillTransferTool<AggFinancexpenseVO>(
					clientFullVOs);
			// ����BP
			AceFinancingExpenseInsertBP action = new AceFinancingExpenseInsertBP();
			AggFinancexpenseVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceFinancingExpenseDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggFinancexpenseVO[] pubupdateBills(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggFinancexpenseVO> transferTool = new BillTransferTool<AggFinancexpenseVO>(
					clientFullVOs);
			AceFinancingExpenseUpdateBP bp = new AceFinancingExpenseUpdateBP();
			AggFinancexpenseVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
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
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	}

	// �ύ
	public AggFinancexpenseVO[] pubsendapprovebills(
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills)
			throws BusinessException {
		AceFinancingExpenseSendApproveBP bp = new AceFinancingExpenseSendApproveBP();
		AggFinancexpenseVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggFinancexpenseVO[] pubunsendapprovebills(
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills)
			throws BusinessException {
		AceFinancingExpenseUnSendApproveBP bp = new AceFinancingExpenseUnSendApproveBP();
		AggFinancexpenseVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggFinancexpenseVO[] pubapprovebills(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFinancingExpenseApproveBP bp = new AceFinancingExpenseApproveBP();
		AggFinancexpenseVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

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