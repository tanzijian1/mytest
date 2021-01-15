package nc.impl.pub.ace;

import nc.bs.tg.changebill.ace.bp.AceChangeBillInsertBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillUpdateBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillDeleteBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillSendApproveBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillUnSendApproveBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillApproveBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceChangeBillPubServiceImpl {
	// ����
	public AggChangeBillHVO[] pubinsertBills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggChangeBillHVO> transferTool = new BillTransferTool<AggChangeBillHVO>(
					clientFullVOs);
			// ����BP
			AceChangeBillInsertBP action = new AceChangeBillInsertBP();
			AggChangeBillHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceChangeBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggChangeBillHVO[] pubupdateBills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggChangeBillHVO> transferTool = new BillTransferTool<AggChangeBillHVO>(
					clientFullVOs);
			AceChangeBillUpdateBP bp = new AceChangeBillUpdateBP();
			AggChangeBillHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggChangeBillHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggChangeBillHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggChangeBillHVO> query = new BillLazyQuery<AggChangeBillHVO>(
					AggChangeBillHVO.class);
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
	public AggChangeBillHVO[] pubsendapprovebills(
			AggChangeBillHVO[] clientFullVOs, AggChangeBillHVO[] originBills)
			throws BusinessException {
		AceChangeBillSendApproveBP bp = new AceChangeBillSendApproveBP();
		AggChangeBillHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggChangeBillHVO[] pubunsendapprovebills(
			AggChangeBillHVO[] clientFullVOs, AggChangeBillHVO[] originBills)
			throws BusinessException {
		AceChangeBillUnSendApproveBP bp = new AceChangeBillUnSendApproveBP();
		AggChangeBillHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggChangeBillHVO[] pubapprovebills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceChangeBillApproveBP bp = new AceChangeBillApproveBP();
		AggChangeBillHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggChangeBillHVO[] pubunapprovebills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceChangeBillUnApproveBP bp = new AceChangeBillUnApproveBP();
		AggChangeBillHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}