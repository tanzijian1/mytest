package nc.impl.pub.ace;

import nc.bs.tg.temporaryestimate.ace.bp.AceTemporaryEstimateInsertBP;
import nc.bs.tg.temporaryestimate.ace.bp.AceTemporaryEstimateUpdateBP;
import nc.bs.tg.temporaryestimate.ace.bp.AceTemporaryEstimateDeleteBP;
import nc.bs.tg.temporaryestimate.ace.bp.AceTemporaryEstimateSendApproveBP;
import nc.bs.tg.temporaryestimate.ace.bp.AceTemporaryEstimateUnSendApproveBP;
import nc.bs.tg.temporaryestimate.ace.bp.AceTemporaryEstimateApproveBP;
import nc.bs.tg.temporaryestimate.ace.bp.AceTemporaryEstimateUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTemporaryEstimatePubServiceImpl {
	// ����
	public AggTemest[] pubinsertBills(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggTemest> transferTool = new BillTransferTool<AggTemest>(
					clientFullVOs);
			// ����BP
			AceTemporaryEstimateInsertBP action = new AceTemporaryEstimateInsertBP();
			AggTemest[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceTemporaryEstimateDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggTemest[] pubupdateBills(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggTemest> transferTool = new BillTransferTool<AggTemest>(
					clientFullVOs);
			AceTemporaryEstimateUpdateBP bp = new AceTemporaryEstimateUpdateBP();
			AggTemest[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggTemest[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggTemest[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggTemest> query = new BillLazyQuery<AggTemest>(
					AggTemest.class);
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
	public AggTemest[] pubsendapprovebills(
			AggTemest[] clientFullVOs, AggTemest[] originBills)
			throws BusinessException {
		AceTemporaryEstimateSendApproveBP bp = new AceTemporaryEstimateSendApproveBP();
		AggTemest[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggTemest[] pubunsendapprovebills(
			AggTemest[] clientFullVOs, AggTemest[] originBills)
			throws BusinessException {
		AceTemporaryEstimateUnSendApproveBP bp = new AceTemporaryEstimateUnSendApproveBP();
		AggTemest[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggTemest[] pubapprovebills(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTemporaryEstimateApproveBP bp = new AceTemporaryEstimateApproveBP();
		AggTemest[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggTemest[] pubunapprovebills(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTemporaryEstimateUnApproveBP bp = new AceTemporaryEstimateUnApproveBP();
		AggTemest[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}