package nc.impl.pub.ace;

import nc.bs.tg.singleissue.ace.bp.AceSingleissueApproveBP;
import nc.bs.tg.singleissue.ace.bp.AceSingleissueDeleteBP;
import nc.bs.tg.singleissue.ace.bp.AceSingleissueInsertBP;
import nc.bs.tg.singleissue.ace.bp.AceSingleissueSendApproveBP;
import nc.bs.tg.singleissue.ace.bp.AceSingleissueUnApproveBP;
import nc.bs.tg.singleissue.ace.bp.AceSingleissueUnSendApproveBP;
import nc.bs.tg.singleissue.ace.bp.AceSingleissueUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.RepaymentPlanVO;

public abstract class AceSingleissuePubServiceImpl {
	// ����
	public AggSingleIssueVO[] pubinsertBills(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggSingleIssueVO> transferTool = new BillTransferTool<AggSingleIssueVO>(
					clientFullVOs);
			// ����BP
			AceSingleissueInsertBP action = new AceSingleissueInsertBP();
			AggSingleIssueVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceSingleissueDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggSingleIssueVO[] pubupdateBills(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggSingleIssueVO> transferTool = new BillTransferTool<AggSingleIssueVO>(
					clientFullVOs);
			AceSingleissueUpdateBP bp = new AceSingleissueUpdateBP();
			AggSingleIssueVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggSingleIssueVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggSingleIssueVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggSingleIssueVO> query = new BillLazyQuery<AggSingleIssueVO>(
					AggSingleIssueVO.class);
			query.setOrderAttribute(RepaymentPlanVO.class, new String[]{"def2"});
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
	public AggSingleIssueVO[] pubsendapprovebills(
			AggSingleIssueVO[] clientFullVOs, AggSingleIssueVO[] originBills)
			throws BusinessException {
		AceSingleissueSendApproveBP bp = new AceSingleissueSendApproveBP();
		AggSingleIssueVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggSingleIssueVO[] pubunsendapprovebills(
			AggSingleIssueVO[] clientFullVOs, AggSingleIssueVO[] originBills)
			throws BusinessException {
		AceSingleissueUnSendApproveBP bp = new AceSingleissueUnSendApproveBP();
		AggSingleIssueVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggSingleIssueVO[] pubapprovebills(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSingleissueApproveBP bp = new AceSingleissueApproveBP();
		AggSingleIssueVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggSingleIssueVO[] pubunapprovebills(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSingleissueUnApproveBP bp = new AceSingleissueUnApproveBP();
		AggSingleIssueVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}