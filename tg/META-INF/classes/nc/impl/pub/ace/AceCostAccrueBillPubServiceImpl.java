package nc.impl.pub.ace;

import nc.bs.tg.costaccruebill.ace.bp.AceCostAccrueBillInsertBP;
import nc.bs.tg.costaccruebill.ace.bp.AceCostAccrueBillUpdateBP;
import nc.bs.tg.costaccruebill.ace.bp.AceCostAccrueBillDeleteBP;
import nc.bs.tg.costaccruebill.ace.bp.AceCostAccrueBillSendApproveBP;
import nc.bs.tg.costaccruebill.ace.bp.AceCostAccrueBillUnSendApproveBP;
import nc.bs.tg.costaccruebill.ace.bp.AceCostAccrueBillApproveBP;
import nc.bs.tg.costaccruebill.ace.bp.AceCostAccrueBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceCostAccrueBillPubServiceImpl {
	// 新增
	public AggCostAccrueBill[] pubinsertBills(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggCostAccrueBill> transferTool = new BillTransferTool<AggCostAccrueBill>(
					clientFullVOs);
			// 调用BP
			AceCostAccrueBillInsertBP action = new AceCostAccrueBillInsertBP();
			AggCostAccrueBill[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceCostAccrueBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggCostAccrueBill[] pubupdateBills(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggCostAccrueBill> transferTool = new BillTransferTool<AggCostAccrueBill>(
					clientFullVOs);
			AceCostAccrueBillUpdateBP bp = new AceCostAccrueBillUpdateBP();
			AggCostAccrueBill[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggCostAccrueBill[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggCostAccrueBill[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggCostAccrueBill> query = new BillLazyQuery<AggCostAccrueBill>(
					AggCostAccrueBill.class);
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
	public AggCostAccrueBill[] pubsendapprovebills(
			AggCostAccrueBill[] clientFullVOs, AggCostAccrueBill[] originBills)
			throws BusinessException {
		AceCostAccrueBillSendApproveBP bp = new AceCostAccrueBillSendApproveBP();
		AggCostAccrueBill[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggCostAccrueBill[] pubunsendapprovebills(
			AggCostAccrueBill[] clientFullVOs, AggCostAccrueBill[] originBills)
			throws BusinessException {
		AceCostAccrueBillUnSendApproveBP bp = new AceCostAccrueBillUnSendApproveBP();
		AggCostAccrueBill[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggCostAccrueBill[] pubapprovebills(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCostAccrueBillApproveBP bp = new AceCostAccrueBillApproveBP();
		AggCostAccrueBill[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggCostAccrueBill[] pubunapprovebills(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCostAccrueBillUnApproveBP bp = new AceCostAccrueBillUnApproveBP();
		AggCostAccrueBill[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}