package nc.impl.pub.ace;

import nc.bs.tg.tartingbill.ace.bp.AceTartingBillInsertBP;
import nc.bs.tg.tartingbill.ace.bp.AceTartingBillUpdateBP;
import nc.bs.tg.tartingbill.ace.bp.AceTartingBillDeleteBP;
import nc.bs.tg.tartingbill.ace.bp.AceTartingBillSendApproveBP;
import nc.bs.tg.tartingbill.ace.bp.AceTartingBillUnSendApproveBP;
import nc.bs.tg.tartingbill.ace.bp.AceTartingBillApproveBP;
import nc.bs.tg.tartingbill.ace.bp.AceTartingBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTartingBillPubServiceImpl {
	// 新增
	public AggTartingBillVO[] pubinsertBills(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggTartingBillVO> transferTool = new BillTransferTool<AggTartingBillVO>(
					clientFullVOs);
			// 调用BP
			AceTartingBillInsertBP action = new AceTartingBillInsertBP();
			AggTartingBillVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceTartingBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggTartingBillVO[] pubupdateBills(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggTartingBillVO> transferTool = new BillTransferTool<AggTartingBillVO>(
					clientFullVOs);
			AceTartingBillUpdateBP bp = new AceTartingBillUpdateBP();
			AggTartingBillVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggTartingBillVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggTartingBillVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggTartingBillVO> query = new BillLazyQuery<AggTartingBillVO>(
					AggTartingBillVO.class);
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
	public AggTartingBillVO[] pubsendapprovebills(
			AggTartingBillVO[] clientFullVOs, AggTartingBillVO[] originBills)
			throws BusinessException {
		AceTartingBillSendApproveBP bp = new AceTartingBillSendApproveBP();
		AggTartingBillVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggTartingBillVO[] pubunsendapprovebills(
			AggTartingBillVO[] clientFullVOs, AggTartingBillVO[] originBills)
			throws BusinessException {
		AceTartingBillUnSendApproveBP bp = new AceTartingBillUnSendApproveBP();
		AggTartingBillVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggTartingBillVO[] pubapprovebills(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTartingBillApproveBP bp = new AceTartingBillApproveBP();
		AggTartingBillVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggTartingBillVO[] pubunapprovebills(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTartingBillUnApproveBP bp = new AceTartingBillUnApproveBP();
		AggTartingBillVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}