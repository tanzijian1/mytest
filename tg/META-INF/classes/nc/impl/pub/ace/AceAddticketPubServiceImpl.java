package nc.impl.pub.ace;

import nc.bs.tg.addticket.ace.bp.AceAddticketInsertBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketUpdateBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketDeleteBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketSendApproveBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketUnSendApproveBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketApproveBP;
import nc.bs.tg.addticket.ace.bp.AceAddticketUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceAddticketPubServiceImpl {
	// 新增
	public AggAddTicket[] pubinsertBills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggAddTicket> transferTool = new BillTransferTool<AggAddTicket>(
					clientFullVOs);
			// 调用BP
			AceAddticketInsertBP action = new AceAddticketInsertBP();
			AggAddTicket[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceAddticketDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggAddTicket[] pubupdateBills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggAddTicket> transferTool = new BillTransferTool<AggAddTicket>(
					clientFullVOs);
			AceAddticketUpdateBP bp = new AceAddticketUpdateBP();
			AggAddTicket[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggAddTicket[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggAddTicket[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggAddTicket> query = new BillLazyQuery<AggAddTicket>(
					AggAddTicket.class);
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
	public AggAddTicket[] pubsendapprovebills(
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills)
			throws BusinessException {
		AceAddticketSendApproveBP bp = new AceAddticketSendApproveBP();
		AggAddTicket[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggAddTicket[] pubunsendapprovebills(
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills)
			throws BusinessException {
		AceAddticketUnSendApproveBP bp = new AceAddticketUnSendApproveBP();
		AggAddTicket[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggAddTicket[] pubapprovebills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceAddticketApproveBP bp = new AceAddticketApproveBP();
		AggAddTicket[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggAddTicket[] pubunapprovebills(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceAddticketUnApproveBP bp = new AceAddticketUnApproveBP();
		AggAddTicket[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}