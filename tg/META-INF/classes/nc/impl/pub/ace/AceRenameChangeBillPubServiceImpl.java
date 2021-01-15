package nc.impl.pub.ace;

import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillInsertBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillUpdateBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillDeleteBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillSendApproveBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillUnSendApproveBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillApproveBP;
import nc.bs.tg.renamechangebill.ace.bp.AceRenameChangeBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceRenameChangeBillPubServiceImpl {
	// 新增
	public AggRenameChangeBillHVO[] pubinsertBills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggRenameChangeBillHVO> transferTool = new BillTransferTool<AggRenameChangeBillHVO>(
					clientFullVOs);
			// 调用BP
			AceRenameChangeBillInsertBP action = new AceRenameChangeBillInsertBP();
			AggRenameChangeBillHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceRenameChangeBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggRenameChangeBillHVO[] pubupdateBills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggRenameChangeBillHVO> transferTool = new BillTransferTool<AggRenameChangeBillHVO>(
					clientFullVOs);
			AceRenameChangeBillUpdateBP bp = new AceRenameChangeBillUpdateBP();
			AggRenameChangeBillHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggRenameChangeBillHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggRenameChangeBillHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggRenameChangeBillHVO> query = new BillLazyQuery<AggRenameChangeBillHVO>(
					AggRenameChangeBillHVO.class);
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
	public AggRenameChangeBillHVO[] pubsendapprovebills(
			AggRenameChangeBillHVO[] clientFullVOs, AggRenameChangeBillHVO[] originBills)
			throws BusinessException {
		AceRenameChangeBillSendApproveBP bp = new AceRenameChangeBillSendApproveBP();
		AggRenameChangeBillHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggRenameChangeBillHVO[] pubunsendapprovebills(
			AggRenameChangeBillHVO[] clientFullVOs, AggRenameChangeBillHVO[] originBills)
			throws BusinessException {
		AceRenameChangeBillUnSendApproveBP bp = new AceRenameChangeBillUnSendApproveBP();
		AggRenameChangeBillHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggRenameChangeBillHVO[] pubapprovebills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceRenameChangeBillApproveBP bp = new AceRenameChangeBillApproveBP();
		AggRenameChangeBillHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggRenameChangeBillHVO[] pubunapprovebills(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceRenameChangeBillUnApproveBP bp = new AceRenameChangeBillUnApproveBP();
		AggRenameChangeBillHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}