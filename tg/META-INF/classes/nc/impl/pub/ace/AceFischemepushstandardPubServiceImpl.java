package nc.impl.pub.ace;

import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardInsertBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardUpdateBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardDeleteBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardSendApproveBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardUnSendApproveBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardApproveBP;
import nc.bs.tg.fischemepushstandard.ace.bp.AceFischemepushstandardUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceFischemepushstandardPubServiceImpl {
	// 新增
	public AggFischemePushStandardHVO[] pubinsertBills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggFischemePushStandardHVO> transferTool = new BillTransferTool<AggFischemePushStandardHVO>(
					clientFullVOs);
			// 调用BP
			AceFischemepushstandardInsertBP action = new AceFischemepushstandardInsertBP();
			AggFischemePushStandardHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceFischemepushstandardDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggFischemePushStandardHVO[] pubupdateBills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggFischemePushStandardHVO> transferTool = new BillTransferTool<AggFischemePushStandardHVO>(
					clientFullVOs);
			AceFischemepushstandardUpdateBP bp = new AceFischemepushstandardUpdateBP();
			AggFischemePushStandardHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggFischemePushStandardHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggFischemePushStandardHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggFischemePushStandardHVO> query = new BillLazyQuery<AggFischemePushStandardHVO>(
					AggFischemePushStandardHVO.class);
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
	public AggFischemePushStandardHVO[] pubsendapprovebills(
			AggFischemePushStandardHVO[] clientFullVOs, AggFischemePushStandardHVO[] originBills)
			throws BusinessException {
		AceFischemepushstandardSendApproveBP bp = new AceFischemepushstandardSendApproveBP();
		AggFischemePushStandardHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggFischemePushStandardHVO[] pubunsendapprovebills(
			AggFischemePushStandardHVO[] clientFullVOs, AggFischemePushStandardHVO[] originBills)
			throws BusinessException {
		AceFischemepushstandardUnSendApproveBP bp = new AceFischemepushstandardUnSendApproveBP();
		AggFischemePushStandardHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggFischemePushStandardHVO[] pubapprovebills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFischemepushstandardApproveBP bp = new AceFischemepushstandardApproveBP();
		AggFischemePushStandardHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggFischemePushStandardHVO[] pubunapprovebills(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceFischemepushstandardUnApproveBP bp = new AceFischemepushstandardUnApproveBP();
		AggFischemePushStandardHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}