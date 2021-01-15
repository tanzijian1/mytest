package nc.impl.pub.ace;

import nc.bs.tg.carryovercost.ace.bp.AceCarryoverCostInsertBP;
import nc.bs.tg.carryovercost.ace.bp.AceCarryoverCostUpdateBP;
import nc.bs.tg.carryovercost.ace.bp.AceCarryoverCostDeleteBP;
import nc.bs.tg.carryovercost.ace.bp.AceCarryoverCostSendApproveBP;
import nc.bs.tg.carryovercost.ace.bp.AceCarryoverCostUnSendApproveBP;
import nc.bs.tg.carryovercost.ace.bp.AceCarryoverCostApproveBP;
import nc.bs.tg.carryovercost.ace.bp.AceCarryoverCostUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceCarryoverCostPubServiceImpl {
	// 新增
	public AggCarrycost[] pubinsertBills(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggCarrycost> transferTool = new BillTransferTool<AggCarrycost>(
					clientFullVOs);
			// 调用BP
			AceCarryoverCostInsertBP action = new AceCarryoverCostInsertBP();
			AggCarrycost[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceCarryoverCostDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggCarrycost[] pubupdateBills(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggCarrycost> transferTool = new BillTransferTool<AggCarrycost>(
					clientFullVOs);
			AceCarryoverCostUpdateBP bp = new AceCarryoverCostUpdateBP();
			AggCarrycost[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggCarrycost[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggCarrycost[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggCarrycost> query = new BillLazyQuery<AggCarrycost>(
					AggCarrycost.class);
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
	public AggCarrycost[] pubsendapprovebills(
			AggCarrycost[] clientFullVOs, AggCarrycost[] originBills)
			throws BusinessException {
		AceCarryoverCostSendApproveBP bp = new AceCarryoverCostSendApproveBP();
		AggCarrycost[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggCarrycost[] pubunsendapprovebills(
			AggCarrycost[] clientFullVOs, AggCarrycost[] originBills)
			throws BusinessException {
		AceCarryoverCostUnSendApproveBP bp = new AceCarryoverCostUnSendApproveBP();
		AggCarrycost[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggCarrycost[] pubapprovebills(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCarryoverCostApproveBP bp = new AceCarryoverCostApproveBP();
		AggCarrycost[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggCarrycost[] pubunapprovebills(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCarryoverCostUnApproveBP bp = new AceCarryoverCostUnApproveBP();
		AggCarrycost[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}