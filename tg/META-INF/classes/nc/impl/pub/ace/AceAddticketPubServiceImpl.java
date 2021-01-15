package nc.impl.pub.ace;

import nc.bs.tg.addticket.ace.bp.AceAddticketInsertBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketUpdateBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketDeleteBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketSendApproveBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketUnSendApproveBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketApproveBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceAddticketPubServiceImpl {
	// ����
	public AggAddTicket[] pubinsertBills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggAddTicket> transferTool = new BillTransferTool<AggAddTicket>(
					clientFullVOs);
			// ����BP
			AceAddticketInsertBP action = new AceAddticketInsertBP();
			AggAddTicket[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceAddticketDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggAddTicket[] pubupdateBills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggAddTicket> transferTool = new BillTransferTool<AggAddTicket>(
					clientFullVOs);
			AceAddticketUpdateBP bp = new AceAddticketUpdateBP();
			AggAddTicket[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggAddTicket[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggAddTicket[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggAddTicket> query = new BillLazyQuery<AggAddTicket>(
					AggAddTicket.class);
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
	public AggAddTicket[] pubsendapprovebills(
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills)
			throws BusinessException {
		AceAddticketSendApproveBP bp = new AceAddticketSendApproveBP();
		AggAddTicket[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggAddTicket[] pubunsendapprovebills(
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills)
			throws BusinessException {
		AceAddticketUnSendApproveBP bp = new AceAddticketUnSendApproveBP();
		AggAddTicket[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggAddTicket[] pubapprovebills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceAddticketApproveBP bp = new AceAddticketApproveBP();
		AggAddTicket[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggAddTicket[] pubunapprovebills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceAddticketUnApproveBP bp = new AceAddticketUnApproveBP();
		AggAddTicket[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}