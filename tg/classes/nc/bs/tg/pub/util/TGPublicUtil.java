package nc.bs.tg.pub.util;

import nc.bs.framework.common.NCLocator;
import nc.bs.uap.lock.PKLock;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class TGPublicUtil {
	/**
	 * �Ե��ݱ�ż���
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
																			 * "�޷�������ǰ���ݣ�ȡ�õĵ��ݱ��Ϊ�ա�"
																			 */);
		}
		// ��������µĴ�����ʱ���ѡ������£���������Σ��ͻ��������⣩����һ��Ʊ�ݺ�Ϊ��
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
																			 * "����ʧ�ܣ��������ڱ�ʹ�ã����Ժ����ԡ�"
																			 */);
		}
	}

	/**
	 * �Ե��ݺŽ���
	 * 
	 * @param billnos
	 * @throws BusinessException
	 */
	public static void releaseBillNO(String[] billnos) throws BusinessException {
		if (null == billnos || billnos.length == 0) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("3607cash_0", "03607cash-0097")/*
																			 * @res
																			 * "�޷�������ǰ���ݣ�ȡ�õĵ��ݱ��Ϊ�ա�"
																			 */);
		}
		// ��������µĴ�����ʱ���ѡ������£���������Σ��ͻ��������⣩����һ��Ʊ�ݺ�Ϊ��
		for (String billno : billnos) {
			if (nc.vo.jcom.lang.StringUtil.isEmptyWithTrim(billno)) {
				return;
			}
		}
		PKLock.getInstance().releaseBatchLock(billnos, null, null);

	}

	/**
	 * ͨ���ۺ�VO������������ѯ��Ӧ��VO��¼
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
