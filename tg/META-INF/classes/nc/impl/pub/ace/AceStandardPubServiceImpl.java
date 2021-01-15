package nc.impl.pub.ace;

import nc.bs.tg.standard.ace.bp.AceStandardDeleteBP;
import nc.bs.tg.standard.ace.bp.AceStandardInsertBP;
import nc.bs.tg.standard.ace.bp.AceStandardUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.standard.AggStandardVO;

public abstract class AceStandardPubServiceImpl {
	// 新增
	public AggStandardVO[] pubinsertBills(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggStandardVO> transferTool = new BillTransferTool<AggStandardVO>(
					clientFullVOs);
			// 调用BP
			AceStandardInsertBP action = new AceStandardInsertBP();
			AggStandardVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceStandardDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggStandardVO[] pubupdateBills(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggStandardVO> transferTool = new BillTransferTool<AggStandardVO>(
					clientFullVOs);
			AceStandardUpdateBP bp = new AceStandardUpdateBP();
			AggStandardVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggStandardVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggStandardVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggStandardVO> query = new BillLazyQuery<AggStandardVO>(
					AggStandardVO.class);
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

}