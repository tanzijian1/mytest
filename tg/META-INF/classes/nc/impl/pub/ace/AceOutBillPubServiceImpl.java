package nc.impl.pub.ace;

import nc.bs.tg.outbill.ace.bp.AceOutBillInsertBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillUpdateBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillDeleteBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillSendApproveBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillUnSendApproveBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillApproveBP;
import nc.bs.tg.outbill.ace.bp.AceOutBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceOutBillPubServiceImpl {
	// 新增
	public AggOutbillHVO[] pubinsertBills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggOutbillHVO> transferTool = new BillTransferTool<AggOutbillHVO>(
					clientFullVOs);
			// 调用BP
			AceOutBillInsertBP action = new AceOutBillInsertBP();
			AggOutbillHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceOutBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggOutbillHVO[] pubupdateBills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggOutbillHVO> transferTool = new BillTransferTool<AggOutbillHVO>(
					clientFullVOs);
			AceOutBillUpdateBP bp = new AceOutBillUpdateBP();
			AggOutbillHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggOutbillHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggOutbillHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggOutbillHVO> query = new BillLazyQuery<AggOutbillHVO>(
					AggOutbillHVO.class);
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
	public AggOutbillHVO[] pubsendapprovebills(
			AggOutbillHVO[] clientFullVOs, AggOutbillHVO[] originBills)
			throws BusinessException {
		AceOutBillSendApproveBP bp = new AceOutBillSendApproveBP();
		AggOutbillHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggOutbillHVO[] pubunsendapprovebills(
			AggOutbillHVO[] clientFullVOs, AggOutbillHVO[] originBills)
			throws BusinessException {
		AceOutBillUnSendApproveBP bp = new AceOutBillUnSendApproveBP();
		AggOutbillHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggOutbillHVO[] pubapprovebills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOutBillApproveBP bp = new AceOutBillApproveBP();
		AggOutbillHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggOutbillHVO[] pubunapprovebills(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOutBillUnApproveBP bp = new AceOutBillUnApproveBP();
		AggOutbillHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}