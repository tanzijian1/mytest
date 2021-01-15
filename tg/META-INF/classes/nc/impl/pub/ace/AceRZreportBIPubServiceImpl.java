package nc.impl.pub.ace;

import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIInsertBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIUpdateBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIDeleteBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBISendApproveBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIUnSendApproveBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIApproveBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceRZreportBIPubServiceImpl {
	// ����
	public AggRZreportBIVO[] pubinsertBills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggRZreportBIVO> transferTool = new BillTransferTool<AggRZreportBIVO>(
					clientFullVOs);
			// ����BP
			AceRZreportBIInsertBP action = new AceRZreportBIInsertBP();
			AggRZreportBIVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceRZreportBIDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggRZreportBIVO[] pubupdateBills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggRZreportBIVO> transferTool = new BillTransferTool<AggRZreportBIVO>(
					clientFullVOs);
			AceRZreportBIUpdateBP bp = new AceRZreportBIUpdateBP();
			AggRZreportBIVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggRZreportBIVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggRZreportBIVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggRZreportBIVO> query = new BillLazyQuery<AggRZreportBIVO>(
					AggRZreportBIVO.class);
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
	public AggRZreportBIVO[] pubsendapprovebills(
			AggRZreportBIVO[] clientFullVOs, AggRZreportBIVO[] originBills)
			throws BusinessException {
		AceRZreportBISendApproveBP bp = new AceRZreportBISendApproveBP();
		AggRZreportBIVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggRZreportBIVO[] pubunsendapprovebills(
			AggRZreportBIVO[] clientFullVOs, AggRZreportBIVO[] originBills)
			throws BusinessException {
		AceRZreportBIUnSendApproveBP bp = new AceRZreportBIUnSendApproveBP();
		AggRZreportBIVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggRZreportBIVO[] pubapprovebills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceRZreportBIApproveBP bp = new AceRZreportBIApproveBP();
		AggRZreportBIVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggRZreportBIVO[] pubunapprovebills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceRZreportBIUnApproveBP bp = new AceRZreportBIUnApproveBP();
		AggRZreportBIVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}