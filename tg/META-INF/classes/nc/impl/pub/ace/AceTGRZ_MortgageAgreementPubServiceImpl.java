package nc.impl.pub.ace;

import nc.bs.tg.tgrz_mortgageagreement.ace.bp.AceTGRZ_MortgageAgreementInsertBP;
import nc.bs.tg.tgrz_mortgageagreement.ace.bp.AceTGRZ_MortgageAgreementUpdateBP;
import nc.bs.tg.tgrz_mortgageagreement.ace.bp.AceTGRZ_MortgageAgreementDeleteBP;
import nc.bs.tg.tgrz_mortgageagreement.ace.bp.AceTGRZ_MortgageAgreementSendApproveBP;
import nc.bs.tg.tgrz_mortgageagreement.ace.bp.AceTGRZ_MortgageAgreementUnSendApproveBP;
import nc.bs.tg.tgrz_mortgageagreement.ace.bp.AceTGRZ_MortgageAgreementApproveBP;
import nc.bs.tg.tgrz_mortgageagreement.ace.bp.AceTGRZ_MortgageAgreementUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTGRZ_MortgageAgreementPubServiceImpl {
	// ����
	public AggMortgageAgreementVO[] pubinsertBills(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggMortgageAgreementVO> transferTool = new BillTransferTool<AggMortgageAgreementVO>(
					clientFullVOs);
			// ����BP
			AceTGRZ_MortgageAgreementInsertBP action = new AceTGRZ_MortgageAgreementInsertBP();
			AggMortgageAgreementVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceTGRZ_MortgageAgreementDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggMortgageAgreementVO[] pubupdateBills(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggMortgageAgreementVO> transferTool = new BillTransferTool<AggMortgageAgreementVO>(
					clientFullVOs);
			AceTGRZ_MortgageAgreementUpdateBP bp = new AceTGRZ_MortgageAgreementUpdateBP();
			AggMortgageAgreementVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggMortgageAgreementVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggMortgageAgreementVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggMortgageAgreementVO> query = new BillLazyQuery<AggMortgageAgreementVO>(
					AggMortgageAgreementVO.class);
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
	public AggMortgageAgreementVO[] pubsendapprovebills(
			AggMortgageAgreementVO[] clientFullVOs, AggMortgageAgreementVO[] originBills)
			throws BusinessException {
		AceTGRZ_MortgageAgreementSendApproveBP bp = new AceTGRZ_MortgageAgreementSendApproveBP();
		AggMortgageAgreementVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggMortgageAgreementVO[] pubunsendapprovebills(
			AggMortgageAgreementVO[] clientFullVOs, AggMortgageAgreementVO[] originBills)
			throws BusinessException {
		AceTGRZ_MortgageAgreementUnSendApproveBP bp = new AceTGRZ_MortgageAgreementUnSendApproveBP();
		AggMortgageAgreementVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggMortgageAgreementVO[] pubapprovebills(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTGRZ_MortgageAgreementApproveBP bp = new AceTGRZ_MortgageAgreementApproveBP();
		AggMortgageAgreementVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggMortgageAgreementVO[] pubunapprovebills(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTGRZ_MortgageAgreementUnApproveBP bp = new AceTGRZ_MortgageAgreementUnApproveBP();
		AggMortgageAgreementVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}