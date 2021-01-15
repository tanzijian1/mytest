package nc.impl.pub.ace;

import nc.bs.tg.outbill.ace.bp.AceOutBillInsertBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillUpdateBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillDeleteBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillSendApproveBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillUnSendApproveBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillApproveBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceOutBillPubServiceImpl {
	// ����
	public AggOutbillHVO[] pubinsertBills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggOutbillHVO> transferTool = new BillTransferTool<AggOutbillHVO>(
					clientFullVOs);
			// ����BP
			AceOutBillInsertBP action = new AceOutBillInsertBP();
			AggOutbillHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceOutBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggOutbillHVO[] pubupdateBills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggOutbillHVO> transferTool = new BillTransferTool<AggOutbillHVO>(
					clientFullVOs);
			AceOutBillUpdateBP bp = new AceOutBillUpdateBP();
			AggOutbillHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggOutbillHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggOutbillHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggOutbillHVO> query = new BillLazyQuery<AggOutbillHVO>(
					AggOutbillHVO.class);
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
	public AggOutbillHVO[] pubsendapprovebills(
			AggOutbillHVO[] clientFullVOs, AggOutbillHVO[] originBills)
			throws BusinessException {
		AceOutBillSendApproveBP bp = new AceOutBillSendApproveBP();
		AggOutbillHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggOutbillHVO[] pubunsendapprovebills(
			AggOutbillHVO[] clientFullVOs, AggOutbillHVO[] originBills)
			throws BusinessException {
		AceOutBillUnSendApproveBP bp = new AceOutBillUnSendApproveBP();
		AggOutbillHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggOutbillHVO[] pubapprovebills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOutBillApproveBP bp = new AceOutBillApproveBP();
		AggOutbillHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggOutbillHVO[] pubunapprovebills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOutBillUnApproveBP bp = new AceOutBillUnApproveBP();
		AggOutbillHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}