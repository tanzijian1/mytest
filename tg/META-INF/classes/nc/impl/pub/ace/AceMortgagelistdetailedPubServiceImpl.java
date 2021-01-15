package nc.impl.pub.ace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.tg.mortgagelistdetailed.ace.bp.AceMortgageListDetailedDeleteBP;
import nc.bs.tg.mortgagelistdetailed.ace.bp.AceMortgageListDetailedInsertBP;
import nc.bs.tg.mortgagelistdetailed.ace.bp.AceMortgageListDetailedUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;
import nc.vo.tg.mortgagelist.MortgageListDetailedVO;

public abstract class AceMortgagelistdetailedPubServiceImpl {
	// 新增
	public AggMortgageListDetailedVO[] pubinsertBills(
			AggMortgageListDetailedVO[] vos) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggMortgageListDetailedVO> transferTool = new BillTransferTool<AggMortgageListDetailedVO>(
					vos);
			AggMortgageListDetailedVO[] mergedVO = transferTool
					.getClientFullInfoBill();

			// 调用BP
			AceMortgageListDetailedInsertBP action = new AceMortgageListDetailedInsertBP();
			AggMortgageListDetailedVO[] retvos = action.insert(mergedVO);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggMortgageListDetailedVO[] vos)
			throws BusinessException {
		try {
			// 加锁 比较ts
			BillTransferTool<AggMortgageListDetailedVO> transferTool = new BillTransferTool<AggMortgageListDetailedVO>(
					vos);
			AggMortgageListDetailedVO[] fullBills = transferTool
					.getClientFullInfoBill();
			AceMortgageListDetailedDeleteBP deleteBP = new AceMortgageListDetailedDeleteBP();
			deleteBP.delete(fullBills);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggMortgageListDetailedVO[] pubupdateBills(
			AggMortgageListDetailedVO[] vos) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggMortgageListDetailedVO> transTool = new BillTransferTool<AggMortgageListDetailedVO>(
					vos);
			// 补全前台VO
			AggMortgageListDetailedVO[] fullBills = transTool
					.getClientFullInfoBill();
			// 获得修改前vo
			AggMortgageListDetailedVO[] originBills = transTool
					.getOriginBills();
			// 调用BP
			AceMortgageListDetailedUpdateBP bp = new AceMortgageListDetailedUpdateBP();
			AggMortgageListDetailedVO[] retBills = bp.update(fullBills,
					originBills);
			// 构造返回数据
			retBills = transTool.getBillForToClient(retBills);
			return retBills;
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggMortgageListDetailedVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggMortgageListDetailedVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggMortgageListDetailedVO> query = new BillLazyQuery<AggMortgageListDetailedVO>(
					AggMortgageListDetailedVO.class);
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

	public AggMortgageListDetailedVO[] syncProjectData()
			throws BusinessException {
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append("select tgrz_projectdata.pk_projectdata pk_project")
				.append(",tgrz_projectdata_c.pk_projectdata_c pk_periodization")
				.append(" from tgrz_projectdata ")
				.append(" inner join tgrz_projectdata_c on tgrz_projectdata.pk_projectdata = tgrz_projectdata_c.pk_projectdata ")
				.append("  where tgrz_projectdata.dr = 0 and tgrz_projectdata_c.dr = 0 and tgrz_projectdata_c.pk_projectdata_c not in(select pk_projectdata_c from tgrz_rep_mortgagelist where dr =0)  ");
		sqlBuff.append(" order by tgrz_projectdata.code,nlssort(tgrz_projectdata_c.pk_projectdata_c) ");
		Collection<MortgageListDetailedVO> coll = (Collection<MortgageListDetailedVO>) new BaseDAO()
				.executeQuery(sqlBuff.toString(), new BeanListProcessor(
						MortgageListDetailedVO.class));

		if (coll != null && coll.size() > 0) {
			List<AggMortgageListDetailedVO> list = new ArrayList<AggMortgageListDetailedVO>();
			for (MortgageListDetailedVO vo : coll) {
				vo.setPk_group(AppContext.getInstance().getPkGroup());
				vo.setPk_org(AppContext.getInstance().getPkGroup());
				vo.setLand_state("未抵押");
				vo.setEngineering_state("未抵押");
				vo.setDr(new Integer(0));
				vo.setStatus(VOStatus.NEW);
				AggMortgageListDetailedVO aggVO = new AggMortgageListDetailedVO();
				aggVO.setParentVO(vo);
				list.add(aggVO);
			}
			return pubinsertBills(list
					.toArray(new AggMortgageListDetailedVO[0]));

		}

		return null;
	}
}