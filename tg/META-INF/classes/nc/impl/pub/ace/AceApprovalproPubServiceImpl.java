package nc.impl.pub.ace;

import nc.bs.tg.approvalpro.ace.bp.AceApprovalproApproveBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproDeleteBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproInsertBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproSendApproveBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproUnApproveBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproUnSendApproveBP;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.ProgressCtrVO;

public abstract class AceApprovalproPubServiceImpl {
	// 新增
	public AggApprovalProVO[] pubinsertBills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggApprovalProVO> transferTool = new BillTransferTool<AggApprovalProVO>(
					clientFullVOs);
			// 调用BP
			AceApprovalproInsertBP action = new AceApprovalproInsertBP();
			AggApprovalProVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceApprovalproDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggApprovalProVO[] pubupdateBills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggApprovalProVO> transferTool = new BillTransferTool<AggApprovalProVO>(
					clientFullVOs);
			AceApprovalproUpdateBP bp = new AceApprovalproUpdateBP();
			AggApprovalProVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggApprovalProVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggApprovalProVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggApprovalProVO> query = new BillLazyQuery<AggApprovalProVO>(
					AggApprovalProVO.class);
			query.setOrderAttribute(ProgressCtrVO.class, new String[]{"rowno"});
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
	public AggApprovalProVO[] pubsendapprovebills(
			AggApprovalProVO[] clientFullVOs, AggApprovalProVO[] originBills)
			throws BusinessException {
		AceApprovalproSendApproveBP bp = new AceApprovalproSendApproveBP();
		AggApprovalProVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggApprovalProVO[] pubunsendapprovebills(
			AggApprovalProVO[] clientFullVOs, AggApprovalProVO[] originBills)
			throws BusinessException {
		AceApprovalproUnSendApproveBP bp = new AceApprovalproUnSendApproveBP();
		AggApprovalProVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggApprovalProVO[] pubapprovebills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceApprovalproApproveBP bp = new AceApprovalproApproveBP();
		AggApprovalProVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggApprovalProVO[] pubunapprovebills(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceApprovalproUnApproveBP bp = new AceApprovalproUnApproveBP();
		AggApprovalProVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}