package nc.impl.pub.ace;

import nc.bs.tg.marginworkorder.ace.bp.AceMarginWorkorderInsertBP;
import nc.bs.tg.marginworkorder.ace.bp.AceMarginWorkorderUpdateBP;
import nc.bs.tg.marginworkorder.ace.bp.AceMarginWorkorderDeleteBP;
import nc.bs.tg.marginworkorder.ace.bp.AceMarginWorkorderSendApproveBP;
import nc.bs.tg.marginworkorder.ace.bp.AceMarginWorkorderUnSendApproveBP;
import nc.bs.tg.marginworkorder.ace.bp.AceMarginWorkorderApproveBP;
import nc.bs.tg.marginworkorder.ace.bp.AceMarginWorkorderUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceMarginWorkorderPubServiceImpl {
	// ����
	public AggMarginHVO[] pubinsertBills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggMarginHVO> transferTool = new BillTransferTool<AggMarginHVO>(
					clientFullVOs);
			// ����BP
			AceMarginWorkorderInsertBP action = new AceMarginWorkorderInsertBP();
			AggMarginHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceMarginWorkorderDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggMarginHVO[] pubupdateBills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggMarginHVO> transferTool = new BillTransferTool<AggMarginHVO>(
					clientFullVOs);
			AceMarginWorkorderUpdateBP bp = new AceMarginWorkorderUpdateBP();
			AggMarginHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggMarginHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggMarginHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggMarginHVO> query = new BillLazyQuery<AggMarginHVO>(
					AggMarginHVO.class);
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
	public AggMarginHVO[] pubsendapprovebills(
			AggMarginHVO[] clientFullVOs, AggMarginHVO[] originBills)
			throws BusinessException {
		AceMarginWorkorderSendApproveBP bp = new AceMarginWorkorderSendApproveBP();
		AggMarginHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggMarginHVO[] pubunsendapprovebills(
			AggMarginHVO[] clientFullVOs, AggMarginHVO[] originBills)
			throws BusinessException {
		AceMarginWorkorderUnSendApproveBP bp = new AceMarginWorkorderUnSendApproveBP();
		AggMarginHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggMarginHVO[] pubapprovebills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceMarginWorkorderApproveBP bp = new AceMarginWorkorderApproveBP();
		AggMarginHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggMarginHVO[] pubunapprovebills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceMarginWorkorderUnApproveBP bp = new AceMarginWorkorderUnApproveBP();
		AggMarginHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}