package nc.impl.pub.ace;

import nc.bs.tg.distribution.ace.bp.AceDistributionInsertBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionUpdateBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionDeleteBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionSendApproveBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionUnSendApproveBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionApproveBP;
import nc.bs.tg.distribution.ace.bp.AceDistributionUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceDistributionPubServiceImpl {
	// 新增
	public AggDistribution[] pubinsertBills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggDistribution> transferTool = new BillTransferTool<AggDistribution>(
					clientFullVOs);
			// 调用BP
			AceDistributionInsertBP action = new AceDistributionInsertBP();
			AggDistribution[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceDistributionDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggDistribution[] pubupdateBills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggDistribution> transferTool = new BillTransferTool<AggDistribution>(
					clientFullVOs);
			AceDistributionUpdateBP bp = new AceDistributionUpdateBP();
			AggDistribution[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggDistribution[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggDistribution[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggDistribution> query = new BillLazyQuery<AggDistribution>(
					AggDistribution.class);
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
	public AggDistribution[] pubsendapprovebills(
			AggDistribution[] clientFullVOs, AggDistribution[] originBills)
			throws BusinessException {
		AceDistributionSendApproveBP bp = new AceDistributionSendApproveBP();
		AggDistribution[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggDistribution[] pubunsendapprovebills(
			AggDistribution[] clientFullVOs, AggDistribution[] originBills)
			throws BusinessException {
		AceDistributionUnSendApproveBP bp = new AceDistributionUnSendApproveBP();
		AggDistribution[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggDistribution[] pubapprovebills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDistributionApproveBP bp = new AceDistributionApproveBP();
		AggDistribution[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggDistribution[] pubunapprovebills(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDistributionUnApproveBP bp = new AceDistributionUnApproveBP();
		AggDistribution[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}