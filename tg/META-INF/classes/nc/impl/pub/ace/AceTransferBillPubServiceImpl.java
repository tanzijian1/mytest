package nc.impl.pub.ace;

import nc.bs.tg.transferbill.ace.bp.AceTransferBillInsertBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillUpdateBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillDeleteBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillSendApproveBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillUnSendApproveBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillApproveBP;
import nc.bs.tg.transferbill.ace.bp.AceTransferBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTransferBillPubServiceImpl {
	// 新增
	public AggTransferBillHVO[] pubinsertBills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggTransferBillHVO> transferTool = new BillTransferTool<AggTransferBillHVO>(
					clientFullVOs);
			// 调用BP
			AceTransferBillInsertBP action = new AceTransferBillInsertBP();
			AggTransferBillHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceTransferBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggTransferBillHVO[] pubupdateBills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggTransferBillHVO> transferTool = new BillTransferTool<AggTransferBillHVO>(
					clientFullVOs);
			AceTransferBillUpdateBP bp = new AceTransferBillUpdateBP();
			AggTransferBillHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggTransferBillHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggTransferBillHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggTransferBillHVO> query = new BillLazyQuery<AggTransferBillHVO>(
					AggTransferBillHVO.class);
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
	public AggTransferBillHVO[] pubsendapprovebills(
			AggTransferBillHVO[] clientFullVOs, AggTransferBillHVO[] originBills)
			throws BusinessException {
		AceTransferBillSendApproveBP bp = new AceTransferBillSendApproveBP();
		AggTransferBillHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggTransferBillHVO[] pubunsendapprovebills(
			AggTransferBillHVO[] clientFullVOs, AggTransferBillHVO[] originBills)
			throws BusinessException {
		AceTransferBillUnSendApproveBP bp = new AceTransferBillUnSendApproveBP();
		AggTransferBillHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggTransferBillHVO[] pubapprovebills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTransferBillApproveBP bp = new AceTransferBillApproveBP();
		AggTransferBillHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggTransferBillHVO[] pubunapprovebills(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTransferBillUnApproveBP bp = new AceTransferBillUnApproveBP();
		AggTransferBillHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}