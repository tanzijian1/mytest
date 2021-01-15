package nc.impl.pub.ace;

import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceInsertBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceUpdateBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceDeleteBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceSendApproveBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceUnSendApproveBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceApproveBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceCheckfinancePubServiceImpl {
	// ����
	public AggCheckFinanceHVO[] pubinsertBills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggCheckFinanceHVO> transferTool = new BillTransferTool<AggCheckFinanceHVO>(
					clientFullVOs);
			// ����BP
			AceCheckfinanceInsertBP action = new AceCheckfinanceInsertBP();
			AggCheckFinanceHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceCheckfinanceDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggCheckFinanceHVO[] pubupdateBills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggCheckFinanceHVO> transferTool = new BillTransferTool<AggCheckFinanceHVO>(
					clientFullVOs);
			AceCheckfinanceUpdateBP bp = new AceCheckfinanceUpdateBP();
			AggCheckFinanceHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggCheckFinanceHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggCheckFinanceHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggCheckFinanceHVO> query = new BillLazyQuery<AggCheckFinanceHVO>(
					AggCheckFinanceHVO.class);
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
	public AggCheckFinanceHVO[] pubsendapprovebills(
			AggCheckFinanceHVO[] clientFullVOs, AggCheckFinanceHVO[] originBills)
			throws BusinessException {
		AceCheckfinanceSendApproveBP bp = new AceCheckfinanceSendApproveBP();
		AggCheckFinanceHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggCheckFinanceHVO[] pubunsendapprovebills(
			AggCheckFinanceHVO[] clientFullVOs, AggCheckFinanceHVO[] originBills)
			throws BusinessException {
		AceCheckfinanceUnSendApproveBP bp = new AceCheckfinanceUnSendApproveBP();
		AggCheckFinanceHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggCheckFinanceHVO[] pubapprovebills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCheckfinanceApproveBP bp = new AceCheckfinanceApproveBP();
		AggCheckFinanceHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggCheckFinanceHVO[] pubunapprovebills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCheckfinanceUnApproveBP bp = new AceCheckfinanceUnApproveBP();
		AggCheckFinanceHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}