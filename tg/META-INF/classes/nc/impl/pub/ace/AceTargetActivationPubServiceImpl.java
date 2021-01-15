package nc.impl.pub.ace;

import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationInsertBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationUpdateBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationDeleteBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationSendApproveBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationUnSendApproveBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationApproveBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTargetActivationPubServiceImpl {
	// ����
	public AggTargetactivation[] pubinsertBills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggTargetactivation> transferTool = new BillTransferTool<AggTargetactivation>(
					clientFullVOs);
			// ����BP
			AceTargetActivationInsertBP action = new AceTargetActivationInsertBP();
			AggTargetactivation[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceTargetActivationDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggTargetactivation[] pubupdateBills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggTargetactivation> transferTool = new BillTransferTool<AggTargetactivation>(
					clientFullVOs);
			AceTargetActivationUpdateBP bp = new AceTargetActivationUpdateBP();
			AggTargetactivation[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggTargetactivation[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggTargetactivation[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggTargetactivation> query = new BillLazyQuery<AggTargetactivation>(
					AggTargetactivation.class);
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
	public AggTargetactivation[] pubsendapprovebills(
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills)
			throws BusinessException {
		AceTargetActivationSendApproveBP bp = new AceTargetActivationSendApproveBP();
		AggTargetactivation[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggTargetactivation[] pubunsendapprovebills(
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills)
			throws BusinessException {
		AceTargetActivationUnSendApproveBP bp = new AceTargetActivationUnSendApproveBP();
		AggTargetactivation[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggTargetactivation[] pubapprovebills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTargetActivationApproveBP bp = new AceTargetActivationApproveBP();
		AggTargetactivation[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggTargetactivation[] pubunapprovebills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTargetActivationUnApproveBP bp = new AceTargetActivationUnApproveBP();
		AggTargetactivation[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}