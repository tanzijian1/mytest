package nc.impl.pub.ace;

import nc.bs.tg.abnormaltaxtransfer.ace.bp.AceAbnormalTaxTransferInsertBP;
import nc.bs.tg.abnormaltaxtransfer.ace.bp.AceAbnormalTaxTransferUpdateBP;
import nc.bs.tg.abnormaltaxtransfer.ace.bp.AceAbnormalTaxTransferDeleteBP;
import nc.bs.tg.abnormaltaxtransfer.ace.bp.AceAbnormalTaxTransferSendApproveBP;
import nc.bs.tg.abnormaltaxtransfer.ace.bp.AceAbnormalTaxTransferUnSendApproveBP;
import nc.bs.tg.abnormaltaxtransfer.ace.bp.AceAbnormalTaxTransferApproveBP;
import nc.bs.tg.abnormaltaxtransfer.ace.bp.AceAbnormalTaxTransferUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceAbnormalTaxTransferPubServiceImpl {
	// ����
	public AggAbTaxTransferHVO[] pubinsertBills(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggAbTaxTransferHVO> transferTool = new BillTransferTool<AggAbTaxTransferHVO>(
					clientFullVOs);
			// ����BP
			AceAbnormalTaxTransferInsertBP action = new AceAbnormalTaxTransferInsertBP();
			AggAbTaxTransferHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceAbnormalTaxTransferDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggAbTaxTransferHVO[] pubupdateBills(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggAbTaxTransferHVO> transferTool = new BillTransferTool<AggAbTaxTransferHVO>(
					clientFullVOs);
			AceAbnormalTaxTransferUpdateBP bp = new AceAbnormalTaxTransferUpdateBP();
			AggAbTaxTransferHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggAbTaxTransferHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggAbTaxTransferHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggAbTaxTransferHVO> query = new BillLazyQuery<AggAbTaxTransferHVO>(
					AggAbTaxTransferHVO.class);
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
	public AggAbTaxTransferHVO[] pubsendapprovebills(
			AggAbTaxTransferHVO[] clientFullVOs, AggAbTaxTransferHVO[] originBills)
			throws BusinessException {
		AceAbnormalTaxTransferSendApproveBP bp = new AceAbnormalTaxTransferSendApproveBP();
		AggAbTaxTransferHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggAbTaxTransferHVO[] pubunsendapprovebills(
			AggAbTaxTransferHVO[] clientFullVOs, AggAbTaxTransferHVO[] originBills)
			throws BusinessException {
		AceAbnormalTaxTransferUnSendApproveBP bp = new AceAbnormalTaxTransferUnSendApproveBP();
		AggAbTaxTransferHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggAbTaxTransferHVO[] pubapprovebills(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceAbnormalTaxTransferApproveBP bp = new AceAbnormalTaxTransferApproveBP();
		AggAbTaxTransferHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggAbTaxTransferHVO[] pubunapprovebills(AggAbTaxTransferHVO[] clientFullVOs,
			AggAbTaxTransferHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceAbnormalTaxTransferUnApproveBP bp = new AceAbnormalTaxTransferUnApproveBP();
		AggAbTaxTransferHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}