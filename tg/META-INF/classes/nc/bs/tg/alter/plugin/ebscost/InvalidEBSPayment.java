package nc.bs.tg.alter.plugin.ebscost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.AoutSysncEbsData;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.infotransform.IInfoTransformService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

public class InvalidEBSPayment extends AoutSysncEbsData {
	IMDPersistenceQueryService mdQryService = null;

	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		try {
			ArrayList<String> pklist = null;
			String sql = "select q.pk_payreq from tgfn_payrequest q where q.def27 = 'Y' and q.dr = 0 and q.def29 <> 'Y'  ";
			IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			pklist = (ArrayList<String>) bs.executeQuery(sql,
					new ColumnListProcessor());
			if (pklist != null && pklist.size() > 0) {
				for (String pk : pklist) {
					AggPayrequest aggvo = (AggPayrequest) getBillVO(
							AggPayrequest.class,
							"isnull(dr,0)=0 and pk_payreq = '" + pk + "'");
					IInfoTransformService iInfoTransformService = NCLocator
							.getInstance().lookup(IInfoTransformService.class);
					String returnjson = null;
					Boolean state = CheckDef61State(pk);
					if (!state) {
						continue;
					}
					// �ɱ��������뵥����
					if ("FN01-Cxx-002".equals(aggvo.getParentVO()
							.getTranstype())
							|| "FN01-Cxx-005".equals(aggvo.getParentVO()
									.getTranstype())) {
						returnjson = iInfoTransformService
								.pushEbsCostPayRequestPaid(aggvo);
					} else if ("FN01-Cxx-001".equals(aggvo.getParentVO()
							.getTranstype())
							|| "FN01-Cxx-006".equals(aggvo.getParentVO()
									.getTranstype())) {// ͨ�ø������뵥����
						returnjson = iInfoTransformService
								.pushEbsgeneralPayrquest(aggvo);// ����ͨ�û�дebs
					} else if ("FN01-Cxx-003".equals(aggvo.getParentVO()
							.getTranstype())) {// SRM�������뵥����
						returnjson = iInfoTransformService
								.pushSrmPayRequestPaid(aggvo);// ����SRM��дebs
					}

					if (returnjson == null) {
						Logger.error("ͨ�ø������뵥�����쳣 ����null");
					}

				}
			}

		} catch (Exception e2) {
			Logger.error(e2.getMessage());
		}

		return null;
	}

	/**
	 * ��ȡҵ�񵥾ݾۺ�VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}

	/**
	 * ͨ���������뵥�����ҵ���Ӧ���еĸ�����Զ�����61�Ƿ񸶿�ɹ���ʶ �ж�����Ϊ�ɹ��������ϻ�д2020-03-20-̸�ӽ�
	 * 
	 * @throws BusinessException
	 */
	private Boolean CheckDef61State(String pk) throws BusinessException {
		Boolean f = true;
		StringBuffer query = new StringBuffer();
		query.append("select b.def61  ");
		query.append("  from ap_paybill b  ");
		query.append("  left join ap_payitem i  ");
		query.append("    on b.pk_paybill = i.pk_paybill  ");
		query.append(" where i.top_billid = '" + pk + "'  ");
		query.append(" and b.dr = 0  ");
		query.append(" and i.dr = 0  ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(query.toString(), new MapListProcessor());
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				String def61 = (String) map.get("def61");
				if (!"Y".equals(def61)) {
					return false;
				}
			}
		}
		return true;

	}
}
