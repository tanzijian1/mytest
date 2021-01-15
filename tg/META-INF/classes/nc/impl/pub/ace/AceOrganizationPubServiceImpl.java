package nc.impl.pub.ace;

import nc.bs.tg.organization.ace.bp.AceOrganizationApproveBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationDeleteBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationInsertBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationSendApproveBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationUnApproveBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationUnSendApproveBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.organization.AggOrganizationVO;

public abstract class AceOrganizationPubServiceImpl {
	// ����
	public AggOrganizationVO[] pubinsertBills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggOrganizationVO> transferTool = new BillTransferTool<AggOrganizationVO>(
					clientFullVOs);
			// ����BP
			AceOrganizationInsertBP action = new AceOrganizationInsertBP();
			AggOrganizationVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceOrganizationDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggOrganizationVO[] pubupdateBills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggOrganizationVO> transferTool = new BillTransferTool<AggOrganizationVO>(
					clientFullVOs);
			AceOrganizationUpdateBP bp = new AceOrganizationUpdateBP();
			AggOrganizationVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggOrganizationVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggOrganizationVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggOrganizationVO> query = new BillLazyQuery<AggOrganizationVO>(
					AggOrganizationVO.class);
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
	public AggOrganizationVO[] pubsendapprovebills(
			AggOrganizationVO[] clientFullVOs, AggOrganizationVO[] originBills)
			throws BusinessException {
		AceOrganizationSendApproveBP bp = new AceOrganizationSendApproveBP();
		AggOrganizationVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggOrganizationVO[] pubunsendapprovebills(
			AggOrganizationVO[] clientFullVOs, AggOrganizationVO[] originBills)
			throws BusinessException {
		AceOrganizationUnSendApproveBP bp = new AceOrganizationUnSendApproveBP();
		AggOrganizationVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggOrganizationVO[] pubapprovebills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOrganizationApproveBP bp = new AceOrganizationApproveBP();
		AggOrganizationVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggOrganizationVO[] pubunapprovebills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOrganizationUnApproveBP bp = new AceOrganizationUnApproveBP();
		AggOrganizationVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}