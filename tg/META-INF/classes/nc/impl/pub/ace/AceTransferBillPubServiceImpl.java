package nc.impl.pub.ace;

import nc.bs.tg.transferbill.ace.bp.AceTransferBillInsertBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillUpdateBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillDeleteBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillSendApproveBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillUnSendApproveBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillApproveBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTransferBillPubServiceImpl {
	// ����
	public AggTransferBillHVO[] pubinsertBills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggTransferBillHVO> transferTool = new BillTransferTool<AggTransferBillHVO>(
					clientFullVOs);
			// ����BP
			AceTransferBillInsertBP action = new AceTransferBillInsertBP();
			AggTransferBillHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceTransferBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggTransferBillHVO[] pubupdateBills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggTransferBillHVO> transferTool = new BillTransferTool<AggTransferBillHVO>(
					clientFullVOs);
			AceTransferBillUpdateBP bp = new AceTransferBillUpdateBP();
			AggTransferBillHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggTransferBillHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggTransferBillHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggTransferBillHVO> query = new BillLazyQuery<AggTransferBillHVO>(
					AggTransferBillHVO.class);
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
	public AggTransferBillHVO[] pubsendapprovebills(
			AggTransferBillHVO[] clientFullVOs, AggTransferBillHVO[] originBills)
			throws BusinessException {
		AceTransferBillSendApproveBP bp = new AceTransferBillSendApproveBP();
		AggTransferBillHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggTransferBillHVO[] pubunsendapprovebills(
			AggTransferBillHVO[] clientFullVOs, AggTransferBillHVO[] originBills)
			throws BusinessException {
		AceTransferBillUnSendApproveBP bp = new AceTransferBillUnSendApproveBP();
		AggTransferBillHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggTransferBillHVO[] pubapprovebills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTransferBillApproveBP bp = new AceTransferBillApproveBP();
		AggTransferBillHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggTransferBillHVO[] pubunapprovebills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTransferBillUnApproveBP bp = new AceTransferBillUnApproveBP();
		AggTransferBillHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}