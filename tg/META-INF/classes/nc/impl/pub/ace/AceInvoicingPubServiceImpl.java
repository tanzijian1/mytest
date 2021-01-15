package nc.impl.pub.ace;

import nc.bs.tg.invoicing.ace.bp.AceInvoicingInsertBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingUpdateBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingDeleteBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingSendApproveBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingUnSendApproveBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingApproveBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceInvoicingPubServiceImpl {
	// ����
	public AggInvoicingHead[] pubinsertBills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggInvoicingHead> transferTool = new BillTransferTool<AggInvoicingHead>(
					clientFullVOs);
			// ����BP
			AceInvoicingInsertBP action = new AceInvoicingInsertBP();
			AggInvoicingHead[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceInvoicingDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggInvoicingHead[] pubupdateBills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggInvoicingHead> transferTool = new BillTransferTool<AggInvoicingHead>(
					clientFullVOs);
			AceInvoicingUpdateBP bp = new AceInvoicingUpdateBP();
			AggInvoicingHead[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggInvoicingHead[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggInvoicingHead[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggInvoicingHead> query = new BillLazyQuery<AggInvoicingHead>(
					AggInvoicingHead.class);
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
	public AggInvoicingHead[] pubsendapprovebills(
			AggInvoicingHead[] clientFullVOs, AggInvoicingHead[] originBills)
			throws BusinessException {
		AceInvoicingSendApproveBP bp = new AceInvoicingSendApproveBP();
		AggInvoicingHead[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggInvoicingHead[] pubunsendapprovebills(
			AggInvoicingHead[] clientFullVOs, AggInvoicingHead[] originBills)
			throws BusinessException {
		AceInvoicingUnSendApproveBP bp = new AceInvoicingUnSendApproveBP();
		AggInvoicingHead[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggInvoicingHead[] pubapprovebills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInvoicingApproveBP bp = new AceInvoicingApproveBP();
		AggInvoicingHead[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggInvoicingHead[] pubunapprovebills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInvoicingUnApproveBP bp = new AceInvoicingUnApproveBP();
		AggInvoicingHead[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}