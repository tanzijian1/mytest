package nc.impl.pub.ace;

import nc.bs.tg.fischeme.ace.bp.AceFischemeInsertBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeUpdateBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeDeleteBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeSendApproveBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeUnSendApproveBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeApproveBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceFischemePubServiceImpl {
	// ����
	public AggFIScemeHVO[] pubinsertBills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggFIScemeHVO> transferTool = new BillTransferTool<AggFIScemeHVO>(
					clientFullVOs);
			// ����BP
			AceFischemeInsertBP action = new AceFischemeInsertBP();
			AggFIScemeHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceFischemeDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggFIScemeHVO[] pubupdateBills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggFIScemeHVO> transferTool = new BillTransferTool<AggFIScemeHVO>(
					clientFullVOs);
			AceFischemeUpdateBP bp = new AceFischemeUpdateBP();
			AggFIScemeHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggFIScemeHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggFIScemeHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggFIScemeHVO> query = new BillLazyQuery<AggFIScemeHVO>(
					AggFIScemeHVO.class);
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
	public AggFIScemeHVO[] pubsendapprovebills(
			AggFIScemeHVO[] clientFullVOs, AggFIScemeHVO[] originBills)
			throws BusinessException {
		AceFischemeSendApproveBP bp = new AceFischemeSendApproveBP();
		AggFIScemeHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggFIScemeHVO[] pubunsendapprovebills(
			AggFIScemeHVO[] clientFullVOs, AggFIScemeHVO[] originBills)
			throws BusinessException {
		AceFischemeUnSendApproveBP bp = new AceFischemeUnSendApproveBP();
		AggFIScemeHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggFIScemeHVO[] pubapprovebills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFischemeApproveBP bp = new AceFischemeApproveBP();
		AggFIScemeHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggFIScemeHVO[] pubunapprovebills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFischemeUnApproveBP bp = new AceFischemeUnApproveBP();
		AggFIScemeHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}