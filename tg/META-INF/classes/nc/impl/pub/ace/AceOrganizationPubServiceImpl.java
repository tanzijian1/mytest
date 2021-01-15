package nc.impl.pub.ace;

import nc.bs.tg.organization.ace.bp.AceOrganizationApproveBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationDeleteBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationInsertBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationSendApproveBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationUnApproveBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationUnSendApproveBP;
import nc.bs.tg.organization.ace.bp.AceOrganizationUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.organization.AggOrganizationVO;

public abstract class AceOrganizationPubServiceImpl {
	// 新增
	public AggOrganizationVO[] pubinsertBills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggOrganizationVO> transferTool = new BillTransferTool<AggOrganizationVO>(
					clientFullVOs);
			// 调用BP
			AceOrganizationInsertBP action = new AceOrganizationInsertBP();
			AggOrganizationVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceOrganizationDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggOrganizationVO[] pubupdateBills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggOrganizationVO> transferTool = new BillTransferTool<AggOrganizationVO>(
					clientFullVOs);
			AceOrganizationUpdateBP bp = new AceOrganizationUpdateBP();
			AggOrganizationVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggOrganizationVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggOrganizationVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggOrganizationVO> query = new BillLazyQuery<AggOrganizationVO>(
					AggOrganizationVO.class);
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
	public AggOrganizationVO[] pubsendapprovebills(
			AggOrganizationVO[] clientFullVOs, AggOrganizationVO[] originBills)
			throws BusinessException {
		AceOrganizationSendApproveBP bp = new AceOrganizationSendApproveBP();
		AggOrganizationVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggOrganizationVO[] pubunsendapprovebills(
			AggOrganizationVO[] clientFullVOs, AggOrganizationVO[] originBills)
			throws BusinessException {
		AceOrganizationUnSendApproveBP bp = new AceOrganizationUnSendApproveBP();
		AggOrganizationVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggOrganizationVO[] pubapprovebills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOrganizationApproveBP bp = new AceOrganizationApproveBP();
		AggOrganizationVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggOrganizationVO[] pubunapprovebills(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOrganizationUnApproveBP bp = new AceOrganizationUnApproveBP();
		AggOrganizationVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}