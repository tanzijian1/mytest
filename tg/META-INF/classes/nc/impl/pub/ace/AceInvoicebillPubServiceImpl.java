package nc.impl.pub.ace;

import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillInsertBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillUpdateBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillDeleteBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillSendApproveBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillUnSendApproveBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillApproveBP;
import nc.bs.tg.invoicebill.ace.bp.AceInvoicebillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceInvoicebillPubServiceImpl {
	// 新增
	public AggInvoiceBillVO[] pubinsertBills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggInvoiceBillVO> transferTool = new BillTransferTool<AggInvoiceBillVO>(
					clientFullVOs);
			// 调用BP
			AceInvoicebillInsertBP action = new AceInvoicebillInsertBP();
			AggInvoiceBillVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceInvoicebillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggInvoiceBillVO[] pubupdateBills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggInvoiceBillVO> transferTool = new BillTransferTool<AggInvoiceBillVO>(
					clientFullVOs);
			AceInvoicebillUpdateBP bp = new AceInvoicebillUpdateBP();
			AggInvoiceBillVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggInvoiceBillVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggInvoiceBillVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggInvoiceBillVO> query = new BillLazyQuery<AggInvoiceBillVO>(
					AggInvoiceBillVO.class);
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
	public AggInvoiceBillVO[] pubsendapprovebills(
			AggInvoiceBillVO[] clientFullVOs, AggInvoiceBillVO[] originBills)
			throws BusinessException {
		AceInvoicebillSendApproveBP bp = new AceInvoicebillSendApproveBP();
		AggInvoiceBillVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggInvoiceBillVO[] pubunsendapprovebills(
			AggInvoiceBillVO[] clientFullVOs, AggInvoiceBillVO[] originBills)
			throws BusinessException {
		AceInvoicebillUnSendApproveBP bp = new AceInvoicebillUnSendApproveBP();
		AggInvoiceBillVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggInvoiceBillVO[] pubapprovebills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInvoicebillApproveBP bp = new AceInvoicebillApproveBP();
		AggInvoiceBillVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggInvoiceBillVO[] pubunapprovebills(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInvoicebillUnApproveBP bp = new AceInvoicebillUnApproveBP();
		AggInvoiceBillVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}