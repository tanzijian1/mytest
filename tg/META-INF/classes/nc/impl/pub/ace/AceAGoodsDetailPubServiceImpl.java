package nc.impl.pub.ace;

import nc.bs.tg.agoodsdetail.ace.bp.AceAGoodsDetailInsertBP;
import nc.bs.tg.agoodsdetail.ace.bp.AceAGoodsDetailUpdateBP;
import nc.bs.tg.agoodsdetail.ace.bp.AceAGoodsDetailDeleteBP;
import nc.bs.tg.agoodsdetail.ace.bp.AceAGoodsDetailSendApproveBP;
import nc.bs.tg.agoodsdetail.ace.bp.AceAGoodsDetailUnSendApproveBP;
import nc.bs.tg.agoodsdetail.ace.bp.AceAGoodsDetailApproveBP;
import nc.bs.tg.agoodsdetail.ace.bp.AceAGoodsDetailUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceAGoodsDetailPubServiceImpl {
	// 新增
	public AggAGoodsDetail[] pubinsertBills(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggAGoodsDetail> transferTool = new BillTransferTool<AggAGoodsDetail>(
					clientFullVOs);
			// 调用BP
			AceAGoodsDetailInsertBP action = new AceAGoodsDetailInsertBP();
			AggAGoodsDetail[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceAGoodsDetailDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggAGoodsDetail[] pubupdateBills(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggAGoodsDetail> transferTool = new BillTransferTool<AggAGoodsDetail>(
					clientFullVOs);
			AceAGoodsDetailUpdateBP bp = new AceAGoodsDetailUpdateBP();
			AggAGoodsDetail[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggAGoodsDetail[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggAGoodsDetail[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggAGoodsDetail> query = new BillLazyQuery<AggAGoodsDetail>(
					AggAGoodsDetail.class);
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
	public AggAGoodsDetail[] pubsendapprovebills(
			AggAGoodsDetail[] clientFullVOs, AggAGoodsDetail[] originBills)
			throws BusinessException {
		AceAGoodsDetailSendApproveBP bp = new AceAGoodsDetailSendApproveBP();
		AggAGoodsDetail[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggAGoodsDetail[] pubunsendapprovebills(
			AggAGoodsDetail[] clientFullVOs, AggAGoodsDetail[] originBills)
			throws BusinessException {
		AceAGoodsDetailUnSendApproveBP bp = new AceAGoodsDetailUnSendApproveBP();
		AggAGoodsDetail[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggAGoodsDetail[] pubapprovebills(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceAGoodsDetailApproveBP bp = new AceAGoodsDetailApproveBP();
		AggAGoodsDetail[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggAGoodsDetail[] pubunapprovebills(AggAGoodsDetail[] clientFullVOs,
			AggAGoodsDetail[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceAGoodsDetailUnApproveBP bp = new AceAGoodsDetailUnApproveBP();
		AggAGoodsDetail[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}