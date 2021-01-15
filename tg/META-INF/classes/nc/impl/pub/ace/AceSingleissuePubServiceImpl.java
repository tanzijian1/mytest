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
	// 新增
	public AggSingleIssueVO[] pubinsertBills(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggSingleIssueVO> transferTool = new BillTransferTool<AggSingleIssueVO>(
					clientFullVOs);
			// 调用BP
			AceSingleissueInsertBP action = new AceSingleissueInsertBP();
			AggSingleIssueVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceSingleissueDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggSingleIssueVO[] pubupdateBills(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggSingleIssueVO> transferTool = new BillTransferTool<AggSingleIssueVO>(
					clientFullVOs);
			AceSingleissueUpdateBP bp = new AceSingleissueUpdateBP();
			AggSingleIssueVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
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
	 * 由子类实现，查询之前对queryScheme进行加工，加入自己的逻辑
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// 查询之前对queryScheme进行加工，加入自己的逻辑
	}

	// 提交
	public AggSingleIssueVO[] pubsendapprovebills(
			AggSingleIssueVO[] clientFullVOs, AggSingleIssueVO[] originBills)
			throws BusinessException {
		AceSingleissueSendApproveBP bp = new AceSingleissueSendApproveBP();
		AggSingleIssueVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggSingleIssueVO[] pubunsendapprovebills(
			AggSingleIssueVO[] clientFullVOs, AggSingleIssueVO[] originBills)
			throws BusinessException {
		AceSingleissueUnSendApproveBP bp = new AceSingleissueUnSendApproveBP();
		AggSingleIssueVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggSingleIssueVO[] pubapprovebills(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSingleissueApproveBP bp = new AceSingleissueApproveBP();
		AggSingleIssueVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

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