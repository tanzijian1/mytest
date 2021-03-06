package nc.impl.pub.ace;

import nc.bs.tg.marginsheet.ace.bp.AceMarginSheetInsertBP;
import nc.bs.tg.marginsheet.ace.bp.AceMarginSheetUpdateBP;
import nc.bs.tg.marginsheet.ace.bp.AceMarginSheetDeleteBP;
import nc.bs.tg.marginsheet.ace.bp.AceMarginSheetSendApproveBP;
import nc.bs.tg.marginsheet.ace.bp.AceMarginSheetUnSendApproveBP;
import nc.bs.tg.marginsheet.ace.bp.AceMarginSheetApproveBP;
import nc.bs.tg.marginsheet.ace.bp.AceMarginSheetUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.marginsheet.AggMarginHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceMarginSheetPubServiceImpl {
	// 新增
	public AggMarginHVO[] pubinsertBills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggMarginHVO> transferTool = new BillTransferTool<AggMarginHVO>(
					clientFullVOs);
			// 调用BP
			AceMarginSheetInsertBP action = new AceMarginSheetInsertBP();
			AggMarginHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceMarginSheetDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggMarginHVO[] pubupdateBills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggMarginHVO> transferTool = new BillTransferTool<AggMarginHVO>(
					clientFullVOs);
			AceMarginSheetUpdateBP bp = new AceMarginSheetUpdateBP();
			AggMarginHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggMarginHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggMarginHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggMarginHVO> query = new BillLazyQuery<AggMarginHVO>(
					AggMarginHVO.class);
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
	public AggMarginHVO[] pubsendapprovebills(
			AggMarginHVO[] clientFullVOs, AggMarginHVO[] originBills)
			throws BusinessException {
		AceMarginSheetSendApproveBP bp = new AceMarginSheetSendApproveBP();
		AggMarginHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggMarginHVO[] pubunsendapprovebills(
			AggMarginHVO[] clientFullVOs, AggMarginHVO[] originBills)
			throws BusinessException {
		AceMarginSheetUnSendApproveBP bp = new AceMarginSheetUnSendApproveBP();
		AggMarginHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggMarginHVO[] pubapprovebills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceMarginSheetApproveBP bp = new AceMarginSheetApproveBP();
		AggMarginHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggMarginHVO[] pubunapprovebills(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceMarginSheetUnApproveBP bp = new AceMarginSheetUnApproveBP();
		AggMarginHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}