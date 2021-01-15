package nc.impl.pub.ace;

import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentInsertBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentUpdateBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentDeleteBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentSendApproveBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentUnSendApproveBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentApproveBP;
import nc.bs.tg.contractapportionment.ace.bp.AceContractApportionmentUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceContractApportionmentPubServiceImpl {
	// 新增
	public AggContractAptmentVO[] pubinsertBills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggContractAptmentVO> transferTool = new BillTransferTool<AggContractAptmentVO>(
					clientFullVOs);
			// 调用BP
			AceContractApportionmentInsertBP action = new AceContractApportionmentInsertBP();
			AggContractAptmentVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceContractApportionmentDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggContractAptmentVO[] pubupdateBills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggContractAptmentVO> transferTool = new BillTransferTool<AggContractAptmentVO>(
					clientFullVOs);
			AceContractApportionmentUpdateBP bp = new AceContractApportionmentUpdateBP();
			AggContractAptmentVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggContractAptmentVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggContractAptmentVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggContractAptmentVO> query = new BillLazyQuery<AggContractAptmentVO>(
					AggContractAptmentVO.class);
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
	public AggContractAptmentVO[] pubsendapprovebills(
			AggContractAptmentVO[] clientFullVOs, AggContractAptmentVO[] originBills)
			throws BusinessException {
		AceContractApportionmentSendApproveBP bp = new AceContractApportionmentSendApproveBP();
		AggContractAptmentVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggContractAptmentVO[] pubunsendapprovebills(
			AggContractAptmentVO[] clientFullVOs, AggContractAptmentVO[] originBills)
			throws BusinessException {
		AceContractApportionmentUnSendApproveBP bp = new AceContractApportionmentUnSendApproveBP();
		AggContractAptmentVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggContractAptmentVO[] pubapprovebills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceContractApportionmentApproveBP bp = new AceContractApportionmentApproveBP();
		AggContractAptmentVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggContractAptmentVO[] pubunapprovebills(AggContractAptmentVO[] clientFullVOs,
			AggContractAptmentVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceContractApportionmentUnApproveBP bp = new AceContractApportionmentUnApproveBP();
		AggContractAptmentVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}