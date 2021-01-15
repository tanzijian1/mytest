package nc.impl.pub.ace;

import nc.bs.tg.projectdata.ace.bp.AceProjectdataInsertBP;
import nc.bs.tg.projectdata.ace.bp.AceProjectdataUpdateBP;
import nc.bs.tg.projectdata.ace.bp.AceProjectdataDeleteBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.projectdata.AggProjectDataVO;

public abstract class AceProjectdataPubServiceImpl {
	// 新增
	public AggProjectDataVO[] pubinsertBills(AggProjectDataVO[] vos)
			throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggProjectDataVO> transferTool = new BillTransferTool<AggProjectDataVO>(
					vos);
			AggProjectDataVO[] mergedVO = transferTool.getClientFullInfoBill();

			// 调用BP
			AceProjectdataInsertBP action = new AceProjectdataInsertBP();
			AggProjectDataVO[] retvos = action.insert(mergedVO);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggProjectDataVO[] vos) throws BusinessException {
		try {
			// 加锁 比较ts
			BillTransferTool<AggProjectDataVO> transferTool = new BillTransferTool<AggProjectDataVO>(
					vos);
			AggProjectDataVO[] fullBills = transferTool.getClientFullInfoBill();
			AceProjectdataDeleteBP deleteBP = new AceProjectdataDeleteBP();
			deleteBP.delete(fullBills);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggProjectDataVO[] pubupdateBills(AggProjectDataVO[] vos)
			throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggProjectDataVO> transTool = new BillTransferTool<AggProjectDataVO>(
					vos);
			// 补全前台VO
			AggProjectDataVO[] fullBills = transTool.getClientFullInfoBill();
			// 获得修改前vo
			AggProjectDataVO[] originBills = transTool.getOriginBills();
			// 调用BP
			AceProjectdataUpdateBP bp = new AceProjectdataUpdateBP();
			AggProjectDataVO[] retBills = bp.update(fullBills, originBills);
			// 构造返回数据
			retBills = transTool.getBillForToClient(retBills);
			return retBills;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggProjectDataVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggProjectDataVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggProjectDataVO> query = new BillLazyQuery<AggProjectDataVO>(
					AggProjectDataVO.class);
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