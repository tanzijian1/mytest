package nc.impl.pub.ace;

import nc.bs.tg.internalinterest.ace.bp.AceInternalInterestInsertBP;
import nc.bs.tg.internalinterest.ace.bp.AceInternalInterestUpdateBP;
import nc.bs.tg.internalinterest.ace.bp.AceInternalInterestDeleteBP;
import nc.bs.tg.internalinterest.ace.bp.AceInternalInterestSendApproveBP;
import nc.bs.tg.internalinterest.ace.bp.AceInternalInterestUnSendApproveBP;
import nc.bs.tg.internalinterest.ace.bp.AceInternalInterestApproveBP;
import nc.bs.tg.internalinterest.ace.bp.AceInternalInterestUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceInternalInterestPubServiceImpl {
	// ����
	public AggInternalinterest[] pubinsertBills(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggInternalinterest> transferTool = new BillTransferTool<AggInternalinterest>(
					clientFullVOs);
			// ����BP
			AceInternalInterestInsertBP action = new AceInternalInterestInsertBP();
			AggInternalinterest[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceInternalInterestDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggInternalinterest[] pubupdateBills(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggInternalinterest> transferTool = new BillTransferTool<AggInternalinterest>(
					clientFullVOs);
			AceInternalInterestUpdateBP bp = new AceInternalInterestUpdateBP();
			AggInternalinterest[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggInternalinterest[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggInternalinterest[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggInternalinterest> query = new BillLazyQuery<AggInternalinterest>(
					AggInternalinterest.class);
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
	public AggInternalinterest[] pubsendapprovebills(
			AggInternalinterest[] clientFullVOs, AggInternalinterest[] originBills)
			throws BusinessException {
		AceInternalInterestSendApproveBP bp = new AceInternalInterestSendApproveBP();
		AggInternalinterest[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggInternalinterest[] pubunsendapprovebills(
			AggInternalinterest[] clientFullVOs, AggInternalinterest[] originBills)
			throws BusinessException {
		AceInternalInterestUnSendApproveBP bp = new AceInternalInterestUnSendApproveBP();
		AggInternalinterest[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggInternalinterest[] pubapprovebills(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInternalInterestApproveBP bp = new AceInternalInterestApproveBP();
		AggInternalinterest[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggInternalinterest[] pubunapprovebills(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInternalInterestUnApproveBP bp = new AceInternalInterestUnApproveBP();
		AggInternalinterest[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}