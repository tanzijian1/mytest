package nc.impl.pub.ace;

import nc.bs.tg.fischeme.ace.bp.AceFischemeInsertBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeUpdateBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeDeleteBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeSendApproveBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeUnSendApproveBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeApproveBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceFischemePubServiceImpl {
	// 新增
	public AggFIScemeHVO[] pubinsertBills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggFIScemeHVO> transferTool = new BillTransferTool<AggFIScemeHVO>(
					clientFullVOs);
			// 调用BP
			AceFischemeInsertBP action = new AceFischemeInsertBP();
			AggFIScemeHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceFischemeDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggFIScemeHVO[] pubupdateBills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggFIScemeHVO> transferTool = new BillTransferTool<AggFIScemeHVO>(
					clientFullVOs);
			AceFischemeUpdateBP bp = new AceFischemeUpdateBP();
			AggFIScemeHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggFIScemeHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggFIScemeHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggFIScemeHVO> query = new BillLazyQuery<AggFIScemeHVO>(
					AggFIScemeHVO.class);
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
	public AggFIScemeHVO[] pubsendapprovebills(
			AggFIScemeHVO[] clientFullVOs, AggFIScemeHVO[] originBills)
			throws BusinessException {
		AceFischemeSendApproveBP bp = new AceFischemeSendApproveBP();
		AggFIScemeHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggFIScemeHVO[] pubunsendapprovebills(
			AggFIScemeHVO[] clientFullVOs, AggFIScemeHVO[] originBills)
			throws BusinessException {
		AceFischemeUnSendApproveBP bp = new AceFischemeUnSendApproveBP();
		AggFIScemeHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggFIScemeHVO[] pubapprovebills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFischemeApproveBP bp = new AceFischemeApproveBP();
		AggFIScemeHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggFIScemeHVO[] pubunapprovebills(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFischemeUnApproveBP bp = new AceFischemeUnApproveBP();
		AggFIScemeHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}