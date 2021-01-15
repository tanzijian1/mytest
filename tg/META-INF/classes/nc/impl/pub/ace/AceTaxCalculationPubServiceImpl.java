package nc.impl.pub.ace;

import nc.bs.tg.taxcalculation.ace.bp.AceTaxCalculationInsertBP;
import nc.bs.tg.taxcalculation.ace.bp.AceTaxCalculationUpdateBP;
import nc.bs.tg.taxcalculation.ace.bp.AceTaxCalculationDeleteBP;
import nc.bs.tg.taxcalculation.ace.bp.AceTaxCalculationSendApproveBP;
import nc.bs.tg.taxcalculation.ace.bp.AceTaxCalculationUnSendApproveBP;
import nc.bs.tg.taxcalculation.ace.bp.AceTaxCalculationApproveBP;
import nc.bs.tg.taxcalculation.ace.bp.AceTaxCalculationUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceTaxCalculationPubServiceImpl {
	// 新增
	public AggTaxCalculationHead[] pubinsertBills(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggTaxCalculationHead> transferTool = new BillTransferTool<AggTaxCalculationHead>(
					clientFullVOs);
			// 调用BP
			AceTaxCalculationInsertBP action = new AceTaxCalculationInsertBP();
			AggTaxCalculationHead[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceTaxCalculationDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggTaxCalculationHead[] pubupdateBills(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggTaxCalculationHead> transferTool = new BillTransferTool<AggTaxCalculationHead>(
					clientFullVOs);
			AceTaxCalculationUpdateBP bp = new AceTaxCalculationUpdateBP();
			AggTaxCalculationHead[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggTaxCalculationHead[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggTaxCalculationHead[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggTaxCalculationHead> query = new BillLazyQuery<AggTaxCalculationHead>(
					AggTaxCalculationHead.class);
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
	public AggTaxCalculationHead[] pubsendapprovebills(
			AggTaxCalculationHead[] clientFullVOs, AggTaxCalculationHead[] originBills)
			throws BusinessException {
		AceTaxCalculationSendApproveBP bp = new AceTaxCalculationSendApproveBP();
		AggTaxCalculationHead[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggTaxCalculationHead[] pubunsendapprovebills(
			AggTaxCalculationHead[] clientFullVOs, AggTaxCalculationHead[] originBills)
			throws BusinessException {
		AceTaxCalculationUnSendApproveBP bp = new AceTaxCalculationUnSendApproveBP();
		AggTaxCalculationHead[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggTaxCalculationHead[] pubapprovebills(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTaxCalculationApproveBP bp = new AceTaxCalculationApproveBP();
		AggTaxCalculationHead[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggTaxCalculationHead[] pubunapprovebills(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceTaxCalculationUnApproveBP bp = new AceTaxCalculationUnApproveBP();
		AggTaxCalculationHead[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}