package nc.impl.pub.ace;

import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceInsertBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceUpdateBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceDeleteBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceSendApproveBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceUnSendApproveBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceApproveBP;
import nc.bs.tg.checkfinance.ace.bp.AceCheckfinanceUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceCheckfinancePubServiceImpl {
	// 新增
	public AggCheckFinanceHVO[] pubinsertBills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggCheckFinanceHVO> transferTool = new BillTransferTool<AggCheckFinanceHVO>(
					clientFullVOs);
			// 调用BP
			AceCheckfinanceInsertBP action = new AceCheckfinanceInsertBP();
			AggCheckFinanceHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceCheckfinanceDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggCheckFinanceHVO[] pubupdateBills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggCheckFinanceHVO> transferTool = new BillTransferTool<AggCheckFinanceHVO>(
					clientFullVOs);
			AceCheckfinanceUpdateBP bp = new AceCheckfinanceUpdateBP();
			AggCheckFinanceHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggCheckFinanceHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggCheckFinanceHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggCheckFinanceHVO> query = new BillLazyQuery<AggCheckFinanceHVO>(
					AggCheckFinanceHVO.class);
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
	public AggCheckFinanceHVO[] pubsendapprovebills(
			AggCheckFinanceHVO[] clientFullVOs, AggCheckFinanceHVO[] originBills)
			throws BusinessException {
		AceCheckfinanceSendApproveBP bp = new AceCheckfinanceSendApproveBP();
		AggCheckFinanceHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggCheckFinanceHVO[] pubunsendapprovebills(
			AggCheckFinanceHVO[] clientFullVOs, AggCheckFinanceHVO[] originBills)
			throws BusinessException {
		AceCheckfinanceUnSendApproveBP bp = new AceCheckfinanceUnSendApproveBP();
		AggCheckFinanceHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggCheckFinanceHVO[] pubapprovebills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCheckfinanceApproveBP bp = new AceCheckfinanceApproveBP();
		AggCheckFinanceHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggCheckFinanceHVO[] pubunapprovebills(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCheckfinanceUnApproveBP bp = new AceCheckfinanceUnApproveBP();
		AggCheckFinanceHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}