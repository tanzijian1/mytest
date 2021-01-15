package nc.impl.pub.ace;

import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataInsertBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataUpdateBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataDeleteBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataSendApproveBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataUnSendApproveBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataApproveBP;
import nc.bs.tg.tg_groupdata.ace.bp.AceTG_GroupDataUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTG_GroupDataPubServiceImpl {
	// 新增
	public AggGroupDataVO[] pubinsertBills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggGroupDataVO> transferTool = new BillTransferTool<AggGroupDataVO>(
					clientFullVOs);
			// 调用BP
			AceTG_GroupDataInsertBP action = new AceTG_GroupDataInsertBP();
			AggGroupDataVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceTG_GroupDataDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggGroupDataVO[] pubupdateBills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggGroupDataVO> transferTool = new BillTransferTool<AggGroupDataVO>(
					clientFullVOs);
			AceTG_GroupDataUpdateBP bp = new AceTG_GroupDataUpdateBP();
			AggGroupDataVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggGroupDataVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggGroupDataVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggGroupDataVO> query = new BillLazyQuery<AggGroupDataVO>(
					AggGroupDataVO.class);
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
	public AggGroupDataVO[] pubsendapprovebills(
			AggGroupDataVO[] clientFullVOs, AggGroupDataVO[] originBills)
			throws BusinessException {
		AceTG_GroupDataSendApproveBP bp = new AceTG_GroupDataSendApproveBP();
		AggGroupDataVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggGroupDataVO[] pubunsendapprovebills(
			AggGroupDataVO[] clientFullVOs, AggGroupDataVO[] originBills)
			throws BusinessException {
		AceTG_GroupDataUnSendApproveBP bp = new AceTG_GroupDataUnSendApproveBP();
		AggGroupDataVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggGroupDataVO[] pubapprovebills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTG_GroupDataApproveBP bp = new AceTG_GroupDataApproveBP();
		AggGroupDataVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggGroupDataVO[] pubunapprovebills(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTG_GroupDataUnApproveBP bp = new AceTG_GroupDataUnApproveBP();
		AggGroupDataVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}