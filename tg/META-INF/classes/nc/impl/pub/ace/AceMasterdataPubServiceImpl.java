package nc.impl.pub.ace;

import nc.bs.tg.masterdata.ace.bp.AceMasterdataInsertBP;
import nc.bs.tg.masterdata.ace.bp.AceMasterdataUpdateBP;
import nc.bs.tg.masterdata.ace.bp.AceMasterdataDeleteBP;
import nc.bs.tg.masterdata.ace.bp.AceMasterdataSendApproveBP;
import nc.bs.tg.masterdata.ace.bp.AceMasterdataUnSendApproveBP;
import nc.bs.tg.masterdata.ace.bp.AceMasterdataApproveBP;
import nc.bs.tg.masterdata.ace.bp.AceMasterdataUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceMasterdataPubServiceImpl {
	// ����
	public AggMasterDataVO[] pubinsertBills(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggMasterDataVO> transferTool = new BillTransferTool<AggMasterDataVO>(
					clientFullVOs);
			// ����BP
			AceMasterdataInsertBP action = new AceMasterdataInsertBP();
			AggMasterDataVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceMasterdataDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggMasterDataVO[] pubupdateBills(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggMasterDataVO> transferTool = new BillTransferTool<AggMasterDataVO>(
					clientFullVOs);
			AceMasterdataUpdateBP bp = new AceMasterdataUpdateBP();
			AggMasterDataVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggMasterDataVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggMasterDataVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggMasterDataVO> query = new BillLazyQuery<AggMasterDataVO>(
					AggMasterDataVO.class);
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
	public AggMasterDataVO[] pubsendapprovebills(
			AggMasterDataVO[] clientFullVOs, AggMasterDataVO[] originBills)
			throws BusinessException {
		AceMasterdataSendApproveBP bp = new AceMasterdataSendApproveBP();
		AggMasterDataVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggMasterDataVO[] pubunsendapprovebills(
			AggMasterDataVO[] clientFullVOs, AggMasterDataVO[] originBills)
			throws BusinessException {
		AceMasterdataUnSendApproveBP bp = new AceMasterdataUnSendApproveBP();
		AggMasterDataVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggMasterDataVO[] pubapprovebills(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceMasterdataApproveBP bp = new AceMasterdataApproveBP();
		AggMasterDataVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggMasterDataVO[] pubunapprovebills(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceMasterdataUnApproveBP bp = new AceMasterdataUnApproveBP();
		AggMasterDataVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}