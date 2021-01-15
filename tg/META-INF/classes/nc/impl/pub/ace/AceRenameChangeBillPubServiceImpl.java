package nc.impl.pub.ace;

import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillInsertBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillUpdateBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillDeleteBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillSendApproveBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillUnSendApproveBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillApproveBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceRenameChangeBillPubServiceImpl {
	// ����
	public AggRenameChangeBillHVO[] pubinsertBills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggRenameChangeBillHVO> transferTool = new BillTransferTool<AggRenameChangeBillHVO>(
					clientFullVOs);
			// ����BP
			AceRenameChangeBillInsertBP action = new AceRenameChangeBillInsertBP();
			AggRenameChangeBillHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceRenameChangeBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggRenameChangeBillHVO[] pubupdateBills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggRenameChangeBillHVO> transferTool = new BillTransferTool<AggRenameChangeBillHVO>(
					clientFullVOs);
			AceRenameChangeBillUpdateBP bp = new AceRenameChangeBillUpdateBP();
			AggRenameChangeBillHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggRenameChangeBillHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggRenameChangeBillHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggRenameChangeBillHVO> query = new BillLazyQuery<AggRenameChangeBillHVO>(
					AggRenameChangeBillHVO.class);
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
	public AggRenameChangeBillHVO[] pubsendapprovebills(
			AggRenameChangeBillHVO[] clientFullVOs, AggRenameChangeBillHVO[] originBills)
			throws BusinessException {
		AceRenameChangeBillSendApproveBP bp = new AceRenameChangeBillSendApproveBP();
		AggRenameChangeBillHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggRenameChangeBillHVO[] pubunsendapprovebills(
			AggRenameChangeBillHVO[] clientFullVOs, AggRenameChangeBillHVO[] originBills)
			throws BusinessException {
		AceRenameChangeBillUnSendApproveBP bp = new AceRenameChangeBillUnSendApproveBP();
		AggRenameChangeBillHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggRenameChangeBillHVO[] pubapprovebills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceRenameChangeBillApproveBP bp = new AceRenameChangeBillApproveBP();
		AggRenameChangeBillHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggRenameChangeBillHVO[] pubunapprovebills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceRenameChangeBillUnApproveBP bp = new AceRenameChangeBillUnApproveBP();
		AggRenameChangeBillHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}