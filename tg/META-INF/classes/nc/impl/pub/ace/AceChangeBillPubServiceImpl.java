package nc.impl.pub.ace;

import nc.bs.tg.changebill.ace.bp.AceChangeBillInsertBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillUpdateBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillDeleteBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillSendApproveBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillUnSendApproveBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillApproveBP;
import nc.bs.tg.changebill.ace.bp.AceChangeBillUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceChangeBillPubServiceImpl {
	// 新增
	public AggChangeBillHVO[] pubinsertBills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggChangeBillHVO> transferTool = new BillTransferTool<AggChangeBillHVO>(
					clientFullVOs);
			// 调用BP
			AceChangeBillInsertBP action = new AceChangeBillInsertBP();
			AggChangeBillHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceChangeBillDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggChangeBillHVO[] pubupdateBills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggChangeBillHVO> transferTool = new BillTransferTool<AggChangeBillHVO>(
					clientFullVOs);
			AceChangeBillUpdateBP bp = new AceChangeBillUpdateBP();
			AggChangeBillHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggChangeBillHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggChangeBillHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggChangeBillHVO> query = new BillLazyQuery<AggChangeBillHVO>(
					AggChangeBillHVO.class);
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
	public AggChangeBillHVO[] pubsendapprovebills(
			AggChangeBillHVO[] clientFullVOs, AggChangeBillHVO[] originBills)
			throws BusinessException {
		AceChangeBillSendApproveBP bp = new AceChangeBillSendApproveBP();
		AggChangeBillHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggChangeBillHVO[] pubunsendapprovebills(
			AggChangeBillHVO[] clientFullVOs, AggChangeBillHVO[] originBills)
			throws BusinessException {
		AceChangeBillUnSendApproveBP bp = new AceChangeBillUnSendApproveBP();
		AggChangeBillHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggChangeBillHVO[] pubapprovebills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceChangeBillApproveBP bp = new AceChangeBillApproveBP();
		AggChangeBillHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggChangeBillHVO[] pubunapprovebills(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceChangeBillUnApproveBP bp = new AceChangeBillUnApproveBP();
		AggChangeBillHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}