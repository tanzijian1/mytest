package nc.impl.pub.ace;

import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestInsertBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestUpdateBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestDeleteBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestSendApproveBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestUnSendApproveBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestApproveBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AcePaymentRequestPubServiceImpl {
	// ����
	public AggPayrequest[] pubinsertBills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggPayrequest> transferTool = new BillTransferTool<AggPayrequest>(
					clientFullVOs);
			// ����BP
			AcePaymentRequestInsertBP action = new AcePaymentRequestInsertBP();
			AggPayrequest[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		try {
			// ����BP
			new AcePaymentRequestDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggPayrequest[] pubupdateBills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggPayrequest> transferTool = new BillTransferTool<AggPayrequest>(
					clientFullVOs);
			AcePaymentRequestUpdateBP bp = new AcePaymentRequestUpdateBP();
			AggPayrequest[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggPayrequest[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggPayrequest[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggPayrequest> query = new BillLazyQuery<AggPayrequest>(
					AggPayrequest.class);
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
	public AggPayrequest[] pubsendapprovebills(
			AggPayrequest[] clientFullVOs, AggPayrequest[] originBills)
			throws BusinessException {
		AcePaymentRequestSendApproveBP bp = new AcePaymentRequestSendApproveBP();
		AggPayrequest[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggPayrequest[] pubunsendapprovebills(
			AggPayrequest[] clientFullVOs, AggPayrequest[] originBills)
			throws BusinessException {
		AcePaymentRequestUnSendApproveBP bp = new AcePaymentRequestUnSendApproveBP();
		AggPayrequest[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggPayrequest[] pubapprovebills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AcePaymentRequestApproveBP bp = new AcePaymentRequestApproveBP();
		AggPayrequest[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggPayrequest[] pubunapprovebills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AcePaymentRequestUnApproveBP bp = new AcePaymentRequestUnApproveBP();
		AggPayrequest[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}