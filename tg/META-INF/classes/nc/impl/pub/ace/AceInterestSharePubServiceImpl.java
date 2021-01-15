package nc.impl.pub.ace;

import nc.bs.tg.interestshare.ace.bp.AceInterestShareInsertBP;
import nc.bs.tg.interestshare.ace.bp.AceInterestShareUpdateBP;
import nc.bs.tg.interestshare.ace.bp.AceInterestShareDeleteBP;
import nc.bs.tg.interestshare.ace.bp.AceInterestShareSendApproveBP;
import nc.bs.tg.interestshare.ace.bp.AceInterestShareUnSendApproveBP;
import nc.bs.tg.interestshare.ace.bp.AceInterestShareApproveBP;
import nc.bs.tg.interestshare.ace.bp.AceInterestShareUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceInterestSharePubServiceImpl {
	// 新增
	public AggIntshareHead[] pubinsertBills(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggIntshareHead> transferTool = new BillTransferTool<AggIntshareHead>(
					clientFullVOs);
			// 调用BP
			AceInterestShareInsertBP action = new AceInterestShareInsertBP();
			AggIntshareHead[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceInterestShareDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggIntshareHead[] pubupdateBills(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggIntshareHead> transferTool = new BillTransferTool<AggIntshareHead>(
					clientFullVOs);
			AceInterestShareUpdateBP bp = new AceInterestShareUpdateBP();
			AggIntshareHead[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggIntshareHead[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggIntshareHead[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggIntshareHead> query = new BillLazyQuery<AggIntshareHead>(
					AggIntshareHead.class);
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
	public AggIntshareHead[] pubsendapprovebills(
			AggIntshareHead[] clientFullVOs, AggIntshareHead[] originBills)
			throws BusinessException {
		AceInterestShareSendApproveBP bp = new AceInterestShareSendApproveBP();
		AggIntshareHead[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggIntshareHead[] pubunsendapprovebills(
			AggIntshareHead[] clientFullVOs, AggIntshareHead[] originBills)
			throws BusinessException {
		AceInterestShareUnSendApproveBP bp = new AceInterestShareUnSendApproveBP();
		AggIntshareHead[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggIntshareHead[] pubapprovebills(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInterestShareApproveBP bp = new AceInterestShareApproveBP();
		AggIntshareHead[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggIntshareHead[] pubunapprovebills(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInterestShareUnApproveBP bp = new AceInterestShareUnApproveBP();
		AggIntshareHead[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}