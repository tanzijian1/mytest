package nc.impl.pub.ace;

import nc.bs.baseapp.itfformulacfg.ace.bp.AceItfformulacfgInsertBP;
import nc.bs.baseapp.itfformulacfg.ace.bp.AceItfformulacfgUpdateBP;
import nc.bs.baseapp.itfformulacfg.ace.bp.AceItfformulacfgDeleteBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.baseapp.itfformulacfg.AggFormulaCfgVO;

/**
 * @Description:
 * @version with NC V6.5
 */
public abstract class AceItfformulacfgPubServiceImpl {
	// 新增
	public AggFormulaCfgVO[] pubinsertBills(AggFormulaCfgVO[] vos) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggFormulaCfgVO> transferTool = new BillTransferTool<AggFormulaCfgVO>(vos);
			AggFormulaCfgVO[] mergedVO = transferTool.getClientFullInfoBill();

			// 调用BP
			AceItfformulacfgInsertBP action = new AceItfformulacfgInsertBP();
			AggFormulaCfgVO[] retvos = action.insert(mergedVO);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggFormulaCfgVO[] vos) throws BusinessException {
		try {
			// 加锁 比较ts
			BillTransferTool<AggFormulaCfgVO> transferTool = new BillTransferTool<AggFormulaCfgVO>(vos);
			AggFormulaCfgVO[] fullBills = transferTool.getClientFullInfoBill();
			AceItfformulacfgDeleteBP deleteBP = new AceItfformulacfgDeleteBP();
			deleteBP.delete(fullBills);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggFormulaCfgVO[] pubupdateBills(AggFormulaCfgVO[] vos) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggFormulaCfgVO> transTool = new BillTransferTool<AggFormulaCfgVO>(vos);
			// 补全前台VO
			AggFormulaCfgVO[] fullBills = transTool.getClientFullInfoBill();
			// 获得修改前vo
			AggFormulaCfgVO[] originBills = transTool.getOriginBills();
			// 调用BP
			AceItfformulacfgUpdateBP bp = new AceItfformulacfgUpdateBP();
			AggFormulaCfgVO[] retBills = bp.update(fullBills, originBills);
			// 构造返回数据
			retBills = transTool.getBillForToClient(retBills);
			return retBills;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggFormulaCfgVO[] pubquerybills(IQueryScheme queryScheme) throws BusinessException {
		AggFormulaCfgVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggFormulaCfgVO> query = new BillLazyQuery<AggFormulaCfgVO>(AggFormulaCfgVO.class);
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