package nc.impl.pub.ace;

import nc.bs.tg.financingtotal.ace.bp.AceFinancingTotalInsertBP;
import nc.bs.tg.financingtotal.ace.bp.AceFinancingTotalUpdateBP;
import nc.bs.tg.financingtotal.ace.bp.AceFinancingTotalDeleteBP;
import nc.bs.tg.financingtotal.ace.bp.AceFinancingTotalSendApproveBP;
import nc.bs.tg.financingtotal.ace.bp.AceFinancingTotalUnSendApproveBP;
import nc.bs.tg.financingtotal.ace.bp.AceFinancingTotalApproveBP;
import nc.bs.tg.financingtotal.ace.bp.AceFinancingTotalUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceFinancingTotalPubServiceImpl {
	// ����
	public AggFinancingTotal[] pubinsertBills(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggFinancingTotal> transferTool = new BillTransferTool<AggFinancingTotal>(
					clientFullVOs);
			// ����BP
			AceFinancingTotalInsertBP action = new AceFinancingTotalInsertBP();
			AggFinancingTotal[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceFinancingTotalDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggFinancingTotal[] pubupdateBills(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggFinancingTotal> transferTool = new BillTransferTool<AggFinancingTotal>(
					clientFullVOs);
			AceFinancingTotalUpdateBP bp = new AceFinancingTotalUpdateBP();
			AggFinancingTotal[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggFinancingTotal[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggFinancingTotal[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggFinancingTotal> query = new BillLazyQuery<AggFinancingTotal>(
					AggFinancingTotal.class);
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
	public AggFinancingTotal[] pubsendapprovebills(
			AggFinancingTotal[] clientFullVOs, AggFinancingTotal[] originBills)
			throws BusinessException {
		AceFinancingTotalSendApproveBP bp = new AceFinancingTotalSendApproveBP();
		AggFinancingTotal[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggFinancingTotal[] pubunsendapprovebills(
			AggFinancingTotal[] clientFullVOs, AggFinancingTotal[] originBills)
			throws BusinessException {
		AceFinancingTotalUnSendApproveBP bp = new AceFinancingTotalUnSendApproveBP();
		AggFinancingTotal[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggFinancingTotal[] pubapprovebills(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFinancingTotalApproveBP bp = new AceFinancingTotalApproveBP();
		AggFinancingTotal[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggFinancingTotal[] pubunapprovebills(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFinancingTotalUnApproveBP bp = new AceFinancingTotalUnApproveBP();
		AggFinancingTotal[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}