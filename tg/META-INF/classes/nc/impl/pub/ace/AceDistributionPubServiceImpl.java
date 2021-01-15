package nc.impl.pub.ace;

import nc.bs.tg.distribution.ace.bp.AceDistributionInsertBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionUpdateBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionDeleteBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionSendApproveBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionUnSendApproveBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionApproveBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceDistributionPubServiceImpl {
	// ����
	public AggDistribution[] pubinsertBills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggDistribution> transferTool = new BillTransferTool<AggDistribution>(
					clientFullVOs);
			// ����BP
			AceDistributionInsertBP action = new AceDistributionInsertBP();
			AggDistribution[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceDistributionDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggDistribution[] pubupdateBills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggDistribution> transferTool = new BillTransferTool<AggDistribution>(
					clientFullVOs);
			AceDistributionUpdateBP bp = new AceDistributionUpdateBP();
			AggDistribution[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggDistribution[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggDistribution[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggDistribution> query = new BillLazyQuery<AggDistribution>(
					AggDistribution.class);
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
	public AggDistribution[] pubsendapprovebills(
			AggDistribution[] clientFullVOs, AggDistribution[] originBills)
			throws BusinessException {
		AceDistributionSendApproveBP bp = new AceDistributionSendApproveBP();
		AggDistribution[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggDistribution[] pubunsendapprovebills(
			AggDistribution[] clientFullVOs, AggDistribution[] originBills)
			throws BusinessException {
		AceDistributionUnSendApproveBP bp = new AceDistributionUnSendApproveBP();
		AggDistribution[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggDistribution[] pubapprovebills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDistributionApproveBP bp = new AceDistributionApproveBP();
		AggDistribution[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggDistribution[] pubunapprovebills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDistributionUnApproveBP bp = new AceDistributionUnApproveBP();
		AggDistribution[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}