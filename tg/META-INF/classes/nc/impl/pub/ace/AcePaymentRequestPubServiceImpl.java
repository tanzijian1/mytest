package nc.impl.pub.ace;

import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestInsertBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestUpdateBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestDeleteBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestSendApproveBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestUnSendApproveBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestApproveBP;
import nc.bs.tg.paymentrequest.ace.bp.AcePaymentRequestUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AcePaymentRequestPubServiceImpl {
	// 新增
	public AggPayrequest[] pubinsertBills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggPayrequest> transferTool = new BillTransferTool<AggPayrequest>(
					clientFullVOs);
			// 调用BP
			AcePaymentRequestInsertBP action = new AcePaymentRequestInsertBP();
			AggPayrequest[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AcePaymentRequestDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggPayrequest[] pubupdateBills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggPayrequest> transferTool = new BillTransferTool<AggPayrequest>(
					clientFullVOs);
			AcePaymentRequestUpdateBP bp = new AcePaymentRequestUpdateBP();
			AggPayrequest[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggPayrequest[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggPayrequest[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggPayrequest> query = new BillLazyQuery<AggPayrequest>(
					AggPayrequest.class);
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
	public AggPayrequest[] pubsendapprovebills(
			AggPayrequest[] clientFullVOs, AggPayrequest[] originBills)
			throws BusinessException {
		AcePaymentRequestSendApproveBP bp = new AcePaymentRequestSendApproveBP();
		AggPayrequest[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggPayrequest[] pubunsendapprovebills(
			AggPayrequest[] clientFullVOs, AggPayrequest[] originBills)
			throws BusinessException {
		AcePaymentRequestUnSendApproveBP bp = new AcePaymentRequestUnSendApproveBP();
		AggPayrequest[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggPayrequest[] pubapprovebills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AcePaymentRequestApproveBP bp = new AcePaymentRequestApproveBP();
		AggPayrequest[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggPayrequest[] pubunapprovebills(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AcePaymentRequestUnApproveBP bp = new AcePaymentRequestUnApproveBP();
		AggPayrequest[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}