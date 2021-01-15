package nc.impl.pub.ace;

import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationInsertBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationUpdateBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationDeleteBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationSendApproveBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationUnSendApproveBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationApproveBP;
import nc.bs.tg.targetactivation.ace.bp.AceTargetActivationUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTargetActivationPubServiceImpl {
	// 新增
	public AggTargetactivation[] pubinsertBills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggTargetactivation> transferTool = new BillTransferTool<AggTargetactivation>(
					clientFullVOs);
			// 调用BP
			AceTargetActivationInsertBP action = new AceTargetActivationInsertBP();
			AggTargetactivation[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceTargetActivationDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggTargetactivation[] pubupdateBills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggTargetactivation> transferTool = new BillTransferTool<AggTargetactivation>(
					clientFullVOs);
			AceTargetActivationUpdateBP bp = new AceTargetActivationUpdateBP();
			AggTargetactivation[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggTargetactivation[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggTargetactivation[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggTargetactivation> query = new BillLazyQuery<AggTargetactivation>(
					AggTargetactivation.class);
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
	public AggTargetactivation[] pubsendapprovebills(
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills)
			throws BusinessException {
		AceTargetActivationSendApproveBP bp = new AceTargetActivationSendApproveBP();
		AggTargetactivation[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggTargetactivation[] pubunsendapprovebills(
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills)
			throws BusinessException {
		AceTargetActivationUnSendApproveBP bp = new AceTargetActivationUnSendApproveBP();
		AggTargetactivation[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggTargetactivation[] pubapprovebills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTargetActivationApproveBP bp = new AceTargetActivationApproveBP();
		AggTargetactivation[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggTargetactivation[] pubunapprovebills(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTargetActivationUnApproveBP bp = new AceTargetActivationUnApproveBP();
		AggTargetactivation[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}