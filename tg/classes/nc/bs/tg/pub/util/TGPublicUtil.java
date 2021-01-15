package nc.bs.tg.pub.util;

import nc.bs.framework.common.NCLocator;
import nc.bs.uap.lock.PKLock;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class TGPublicUtil {
	/**
	 * 对单据编号加锁
	 * 
	 * @param cmpbillno
	 * @param userid
	 * @throws BusinessException
	 */
	public static void lockBillNO(String[] billnos) throws BusinessException {
		if (null == billnos || billnos.length == 0) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("3607cash_0", "03607cash-0097")/*
																			 * @res
																			 * "无法锁定当前单据，取得的单据编号为空。"
																			 */);
		}
		// 特殊情况下的处理，有时候多选的情况下，会调用两次（客户化的问题），第一次票据号为空
		for (String billno : billnos) {
			if (nc.vo.jcom.lang.StringUtil.isEmptyWithTrim(billno)) {
				return;
			}
		}
		boolean isLock = PKLock.getInstance().addBatchDynamicLock(billnos);
		if (!isLock) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("3607cash_0", "03607cash-0098")/*
																			 * @res
																			 * "加锁失败，单据正在被使用，请稍候再试。"
																			 */);
		}
	}

	/**
	 * 对单据号解锁
	 * 
	 * @param billnos
	 * @throws BusinessException
	 */
	public static void releaseBillNO(String[] billnos) throws BusinessException {
		if (null == billnos || billnos.length == 0) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("3607cash_0", "03607cash-0097")/*
																			 * @res
																			 * "无法锁定当前单据，取得的单据编号为空。"
																			 */);
		}
		// 特殊情况下的处理，有时候多选的情况下，会调用两次（客户化的问题），第一次票据号为空
		for (String billno : billnos) {
			if (nc.vo.jcom.lang.StringUtil.isEmptyWithTrim(billno)) {
				return;
			}
		}
		PKLock.getInstance().releaseBatchLock(billnos, null, null);

	}

	/**
	 * 通过聚合VO类名及主键查询相应的VO记录
	 * 
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	public static AggregatedValueObject queryAggvoByPK(
			AggregatedValueObject aggvo) throws BusinessException {
		nc.itf.pubapp.pub.smart.IBillQueryService qry = NCLocator.getInstance()
				.lookup(nc.itf.pubapp.pub.smart.IBillQueryService.class);
		AbstractBill[] bills = qry.queryAbstractBillsByPks(aggvo.getClass(),
				new String[] { aggvo.getParentVO().getPrimaryKey() });
		return bills != null && bills.length > 0 ? bills[0] : null;
	}
}
