package nc.impl.pub.ace;

import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIInsertBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIUpdateBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIDeleteBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBISendApproveBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIUnSendApproveBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIApproveBP;
import nc.bs.tg.rzreportbi.ace.bp.AceRZreportBIUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceRZreportBIPubServiceImpl {
	// 新增
	public AggRZreportBIVO[] pubinsertBills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggRZreportBIVO> transferTool = new BillTransferTool<AggRZreportBIVO>(
					clientFullVOs);
			// 调用BP
			AceRZreportBIInsertBP action = new AceRZreportBIInsertBP();
			AggRZreportBIVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceRZreportBIDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggRZreportBIVO[] pubupdateBills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggRZreportBIVO> transferTool = new BillTransferTool<AggRZreportBIVO>(
					clientFullVOs);
			AceRZreportBIUpdateBP bp = new AceRZreportBIUpdateBP();
			AggRZreportBIVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggRZreportBIVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggRZreportBIVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggRZreportBIVO> query = new BillLazyQuery<AggRZreportBIVO>(
					AggRZreportBIVO.class);
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
	public AggRZreportBIVO[] pubsendapprovebills(
			AggRZreportBIVO[] clientFullVOs, AggRZreportBIVO[] originBills)
			throws BusinessException {
		AceRZreportBISendApproveBP bp = new AceRZreportBISendApproveBP();
		AggRZreportBIVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggRZreportBIVO[] pubunsendapprovebills(
			AggRZreportBIVO[] clientFullVOs, AggRZreportBIVO[] originBills)
			throws BusinessException {
		AceRZreportBIUnSendApproveBP bp = new AceRZreportBIUnSendApproveBP();
		AggRZreportBIVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggRZreportBIVO[] pubapprovebills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceRZreportBIApproveBP bp = new AceRZreportBIApproveBP();
		AggRZreportBIVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggRZreportBIVO[] pubunapprovebills(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceRZreportBIUnApproveBP bp = new AceRZreportBIUnApproveBP();
		AggRZreportBIVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}