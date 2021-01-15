package nc.impl.pub.ace;

import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillInsertBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillUpdateBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillDeleteBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillSendApproveBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillUnSendApproveBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillApproveBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceInvoicebillPubServiceImpl {
	// ����
	public AggInvoiceBillVO[] pubinsertBills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggInvoiceBillVO> transferTool = new BillTransferTool<AggInvoiceBillVO>(
					clientFullVOs);
			// ����BP
			AceInvoicebillInsertBP action = new AceInvoicebillInsertBP();
			AggInvoiceBillVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceInvoicebillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggInvoiceBillVO[] pubupdateBills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggInvoiceBillVO> transferTool = new BillTransferTool<AggInvoiceBillVO>(
					clientFullVOs);
			AceInvoicebillUpdateBP bp = new AceInvoicebillUpdateBP();
			AggInvoiceBillVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggInvoiceBillVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggInvoiceBillVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggInvoiceBillVO> query = new BillLazyQuery<AggInvoiceBillVO>(
					AggInvoiceBillVO.class);
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
	public AggInvoiceBillVO[] pubsendapprovebills(
			AggInvoiceBillVO[] clientFullVOs, AggInvoiceBillVO[] originBills)
			throws BusinessException {
		AceInvoicebillSendApproveBP bp = new AceInvoicebillSendApproveBP();
		AggInvoiceBillVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggInvoiceBillVO[] pubunsendapprovebills(
			AggInvoiceBillVO[] clientFullVOs, AggInvoiceBillVO[] originBills)
			throws BusinessException {
		AceInvoicebillUnSendApproveBP bp = new AceInvoicebillUnSendApproveBP();
		AggInvoiceBillVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggInvoiceBillVO[] pubapprovebills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInvoicebillApproveBP bp = new AceInvoicebillApproveBP();
		AggInvoiceBillVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggInvoiceBillVO[] pubunapprovebills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInvoicebillUnApproveBP bp = new AceInvoicebillUnApproveBP();
		AggInvoiceBillVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}