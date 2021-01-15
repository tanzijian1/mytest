package nc.impl.pub.ace;

import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentInsertBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentUpdateBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentDeleteBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentSendApproveBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentUnSendApproveBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentApproveBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceContractApportionmentPubServiceImpl {
	// ����
	public AggContractAptmentVO[] pubinsertBills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggContractAptmentVO> transferTool = new BillTransferTool<AggContractAptmentVO>(
					clientFullVOs);
			// ����BP
			AceContractApportionmentInsertBP action = new AceContractApportionmentInsertBP();
			AggContractAptmentVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceContractApportionmentDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggContractAptmentVO[] pubupdateBills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggContractAptmentVO> transferTool = new BillTransferTool<AggContractAptmentVO>(
					clientFullVOs);
			AceContractApportionmentUpdateBP bp = new AceContractApportionmentUpdateBP();
			AggContractAptmentVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggContractAptmentVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggContractAptmentVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggContractAptmentVO> query = new BillLazyQuery<AggContractAptmentVO>(
					AggContractAptmentVO.class);
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
	public AggContractAptmentVO[] pubsendapprovebills(
			AggContractAptmentVO[] clientFullVOs, AggContractAptmentVO[] originBills)
			throws BusinessException {
		AceContractApportionmentSendApproveBP bp = new AceContractApportionmentSendApproveBP();
		AggContractAptmentVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggContractAptmentVO[] pubunsendapprovebills(
			AggContractAptmentVO[] clientFullVOs, AggContractAptmentVO[] originBills)
			throws BusinessException {
		AceContractApportionmentUnSendApproveBP bp = new AceContractApportionmentUnSendApproveBP();
		AggContractAptmentVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggContractAptmentVO[] pubapprovebills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceContractApportionmentApproveBP bp = new AceContractApportionmentApproveBP();
		AggContractAptmentVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggContractAptmentVO[] pubunapprovebills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceContractApportionmentUnApproveBP bp = new AceContractApportionmentUnApproveBP();
		AggContractAptmentVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}