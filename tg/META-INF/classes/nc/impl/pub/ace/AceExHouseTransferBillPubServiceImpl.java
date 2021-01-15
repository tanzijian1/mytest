package nc.impl.pub.ace;

import nc.bs.tg.exhousetransferbill.ace.bp.AceExHouseTransferBillInsertBP;
import nc.bs.tg.exhousetransferbill.ace.bp.AceExHouseTransferBillUpdateBP;
import nc.bs.tg.exhousetransferbill.ace.bp.AceExHouseTransferBillDeleteBP;
import nc.bs.tg.exhousetransferbill.ace.bp.AceExHouseTransferBillSendApproveBP;
import nc.bs.tg.exhousetransferbill.ace.bp.AceExHouseTransferBillUnSendApproveBP;
import nc.bs.tg.exhousetransferbill.ace.bp.AceExHouseTransferBillApproveBP;
import nc.bs.tg.exhousetransferbill.ace.bp.AceExHouseTransferBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceExHouseTransferBillPubServiceImpl {
	// 新增
	public AggExhousetransferbillHVO[] pubinsertBills(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggExhousetransferbillHVO> transferTool = new BillTransferTool<AggExhousetransferbillHVO>(
					clientFullVOs);
			// 调用BP
			AceExHouseTransferBillInsertBP action = new AceExHouseTransferBillInsertBP();
			AggExhousetransferbillHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceExHouseTransferBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggExhousetransferbillHVO[] pubupdateBills(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggExhousetransferbillHVO> transferTool = new BillTransferTool<AggExhousetransferbillHVO>(
					clientFullVOs);
			AceExHouseTransferBillUpdateBP bp = new AceExHouseTransferBillUpdateBP();
			AggExhousetransferbillHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggExhousetransferbillHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggExhousetransferbillHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggExhousetransferbillHVO> query = new BillLazyQuery<AggExhousetransferbillHVO>(
					AggExhousetransferbillHVO.class);
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
	public AggExhousetransferbillHVO[] pubsendapprovebills(
			AggExhousetransferbillHVO[] clientFullVOs, AggExhousetransferbillHVO[] originBills)
			throws BusinessException {
		AceExHouseTransferBillSendApproveBP bp = new AceExHouseTransferBillSendApproveBP();
		AggExhousetransferbillHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggExhousetransferbillHVO[] pubunsendapprovebills(
			AggExhousetransferbillHVO[] clientFullVOs, AggExhousetransferbillHVO[] originBills)
			throws BusinessException {
		AceExHouseTransferBillUnSendApproveBP bp = new AceExHouseTransferBillUnSendApproveBP();
		AggExhousetransferbillHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggExhousetransferbillHVO[] pubapprovebills(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceExHouseTransferBillApproveBP bp = new AceExHouseTransferBillApproveBP();
		AggExhousetransferbillHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggExhousetransferbillHVO[] pubunapprovebills(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceExHouseTransferBillUnApproveBP bp = new AceExHouseTransferBillUnApproveBP();
		AggExhousetransferbillHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}