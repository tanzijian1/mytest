package nc.impl.pub.ace;

import nc.bs.tg.invoicing.ace.bp.AceInvoicingInsertBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingUpdateBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingDeleteBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingSendApproveBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingUnSendApproveBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingApproveBP;
import nc.bs.tg.invoicing.ace.bp.AceInvoicingUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceInvoicingPubServiceImpl {
	// 新增
	public AggInvoicingHead[] pubinsertBills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggInvoicingHead> transferTool = new BillTransferTool<AggInvoicingHead>(
					clientFullVOs);
			// 调用BP
			AceInvoicingInsertBP action = new AceInvoicingInsertBP();
			AggInvoicingHead[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceInvoicingDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggInvoicingHead[] pubupdateBills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggInvoicingHead> transferTool = new BillTransferTool<AggInvoicingHead>(
					clientFullVOs);
			AceInvoicingUpdateBP bp = new AceInvoicingUpdateBP();
			AggInvoicingHead[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggInvoicingHead[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggInvoicingHead[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggInvoicingHead> query = new BillLazyQuery<AggInvoicingHead>(
					AggInvoicingHead.class);
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
	public AggInvoicingHead[] pubsendapprovebills(
			AggInvoicingHead[] clientFullVOs, AggInvoicingHead[] originBills)
			throws BusinessException {
		AceInvoicingSendApproveBP bp = new AceInvoicingSendApproveBP();
		AggInvoicingHead[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggInvoicingHead[] pubunsendapprovebills(
			AggInvoicingHead[] clientFullVOs, AggInvoicingHead[] originBills)
			throws BusinessException {
		AceInvoicingUnSendApproveBP bp = new AceInvoicingUnSendApproveBP();
		AggInvoicingHead[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggInvoicingHead[] pubapprovebills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInvoicingApproveBP bp = new AceInvoicingApproveBP();
		AggInvoicingHead[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggInvoicingHead[] pubunapprovebills(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInvoicingUnApproveBP bp = new AceInvoicingUnApproveBP();
		AggInvoicingHead[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}