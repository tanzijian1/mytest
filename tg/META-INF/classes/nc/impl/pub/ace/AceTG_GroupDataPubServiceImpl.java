package nc.impl.pub.ace;

import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataInsertBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataUpdateBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataDeleteBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataSendApproveBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataUnSendApproveBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataApproveBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTG_GroupDataPubServiceImpl {
	// ����
	public AggGroupDataVO[] pubinsertBills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggGroupDataVO> transferTool = new BillTransferTool<AggGroupDataVO>(
					clientFullVOs);
			// ����BP
			AceTG_GroupDataInsertBP action = new AceTG_GroupDataInsertBP();
			AggGroupDataVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceTG_GroupDataDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggGroupDataVO[] pubupdateBills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggGroupDataVO> transferTool = new BillTransferTool<AggGroupDataVO>(
					clientFullVOs);
			AceTG_GroupDataUpdateBP bp = new AceTG_GroupDataUpdateBP();
			AggGroupDataVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggGroupDataVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggGroupDataVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggGroupDataVO> query = new BillLazyQuery<AggGroupDataVO>(
					AggGroupDataVO.class);
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
	public AggGroupDataVO[] pubsendapprovebills(
			AggGroupDataVO[] clientFullVOs, AggGroupDataVO[] originBills)
			throws BusinessException {
		AceTG_GroupDataSendApproveBP bp = new AceTG_GroupDataSendApproveBP();
		AggGroupDataVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggGroupDataVO[] pubunsendapprovebills(
			AggGroupDataVO[] clientFullVOs, AggGroupDataVO[] originBills)
			throws BusinessException {
		AceTG_GroupDataUnSendApproveBP bp = new AceTG_GroupDataUnSendApproveBP();
		AggGroupDataVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggGroupDataVO[] pubapprovebills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTG_GroupDataApproveBP bp = new AceTG_GroupDataApproveBP();
		AggGroupDataVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggGroupDataVO[] pubunapprovebills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTG_GroupDataUnApproveBP bp = new AceTG_GroupDataUnApproveBP();
		AggGroupDataVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}