package nc.impl.pub.ace;

import nc.bs.tg.salaryfundaccure.ace.bp.AceSalaryfundaccureInsertBP;
import nc.bs.tg.salaryfundaccure.ace.bp.AceSalaryfundaccureUpdateBP;
import nc.bs.tg.salaryfundaccure.ace.bp.AceSalaryfundaccureDeleteBP;
import nc.bs.tg.salaryfundaccure.ace.bp.AceSalaryfundaccureSendApproveBP;
import nc.bs.tg.salaryfundaccure.ace.bp.AceSalaryfundaccureUnSendApproveBP;
import nc.bs.tg.salaryfundaccure.ace.bp.AceSalaryfundaccureApproveBP;
import nc.bs.tg.salaryfundaccure.ace.bp.AceSalaryfundaccureUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceSalaryfundaccurePubServiceImpl {
	// ����
	public AggSalaryfundaccure[] pubinsertBills(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggSalaryfundaccure> transferTool = new BillTransferTool<AggSalaryfundaccure>(
					clientFullVOs);
			// ����BP
			AceSalaryfundaccureInsertBP action = new AceSalaryfundaccureInsertBP();
			AggSalaryfundaccure[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceSalaryfundaccureDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggSalaryfundaccure[] pubupdateBills(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggSalaryfundaccure> transferTool = new BillTransferTool<AggSalaryfundaccure>(
					clientFullVOs);
			AceSalaryfundaccureUpdateBP bp = new AceSalaryfundaccureUpdateBP();
			AggSalaryfundaccure[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggSalaryfundaccure[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggSalaryfundaccure[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggSalaryfundaccure> query = new BillLazyQuery<AggSalaryfundaccure>(
					AggSalaryfundaccure.class);
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
	public AggSalaryfundaccure[] pubsendapprovebills(
			AggSalaryfundaccure[] clientFullVOs, AggSalaryfundaccure[] originBills)
			throws BusinessException {
		AceSalaryfundaccureSendApproveBP bp = new AceSalaryfundaccureSendApproveBP();
		AggSalaryfundaccure[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggSalaryfundaccure[] pubunsendapprovebills(
			AggSalaryfundaccure[] clientFullVOs, AggSalaryfundaccure[] originBills)
			throws BusinessException {
		AceSalaryfundaccureUnSendApproveBP bp = new AceSalaryfundaccureUnSendApproveBP();
		AggSalaryfundaccure[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggSalaryfundaccure[] pubapprovebills(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSalaryfundaccureApproveBP bp = new AceSalaryfundaccureApproveBP();
		AggSalaryfundaccure[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggSalaryfundaccure[] pubunapprovebills(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSalaryfundaccureUnApproveBP bp = new AceSalaryfundaccureUnApproveBP();
		AggSalaryfundaccure[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}