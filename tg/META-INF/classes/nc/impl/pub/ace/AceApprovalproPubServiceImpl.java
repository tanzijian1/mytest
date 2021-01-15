package nc.impl.pub.ace;

import nc.bs.tg.approvalpro.ace.bp.AceApprovalproApproveBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproDeleteBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproInsertBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproSendApproveBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproUnApproveBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproUnSendApproveBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.ProgressCtrVO;

public abstract class AceApprovalproPubServiceImpl {
	// ����
	public AggApprovalProVO[] pubinsertBills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggApprovalProVO> transferTool = new BillTransferTool<AggApprovalProVO>(
					clientFullVOs);
			// ����BP
			AceApprovalproInsertBP action = new AceApprovalproInsertBP();
			AggApprovalProVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceApprovalproDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggApprovalProVO[] pubupdateBills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggApprovalProVO> transferTool = new BillTransferTool<AggApprovalProVO>(
					clientFullVOs);
			AceApprovalproUpdateBP bp = new AceApprovalproUpdateBP();
			AggApprovalProVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggApprovalProVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggApprovalProVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggApprovalProVO> query = new BillLazyQuery<AggApprovalProVO>(
					AggApprovalProVO.class);
			query.setOrderAttribute(ProgressCtrVO.class, new String[]{"rowno"});
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
	public AggApprovalProVO[] pubsendapprovebills(
			AggApprovalProVO[] clientFullVOs, AggApprovalProVO[] originBills)
			throws BusinessException {
		AceApprovalproSendApproveBP bp = new AceApprovalproSendApproveBP();
		AggApprovalProVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggApprovalProVO[] pubunsendapprovebills(
			AggApprovalProVO[] clientFullVOs, AggApprovalProVO[] originBills)
			throws BusinessException {
		AceApprovalproUnSendApproveBP bp = new AceApprovalproUnSendApproveBP();
		AggApprovalProVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggApprovalProVO[] pubapprovebills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceApprovalproApproveBP bp = new AceApprovalproApproveBP();
		AggApprovalProVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggApprovalProVO[] pubunapprovebills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceApprovalproUnApproveBP bp = new AceApprovalproUnApproveBP();
		AggApprovalProVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}