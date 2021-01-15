package nc.bs.tg.alter.plugin.ebs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.itf.tg.outside.ISyncEBSServcie;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * ebs系统定时同步执行情况信息至NC系统
 * 
 * @author ASUS
 * 
 */
public class AoutUpdateSituation implements IBackgroundWorkPlugin {
	private static final String String = null;
	BaseDAO baseDAO = null;
	IMDPersistenceQueryService queryServcie = null;

	ISyncEBSServcie ebsService = null;

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		String title = bgwc.getAlertTypeName();
		UpdateSituation(title);
		return null;
	}

	/**
	 * 读取报销单
	 * 
	 * @param title
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused" })
	private Map<String, List<Object>> UpdateSituation(String title) {
		try {
			String msg = "";
			// 查询合同
			ArrayList<String> contractNum = getContractData();
			if (contractNum != null && contractNum.size() > 0) {
				for (String ctapbillno : contractNum) {
					AggCtApVO aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
							"isnull(dr,0)=0 and vbillcode = '" + ctapbillno
									+ "'");

					List<Map<String, Object>> data = getDate(ctapbillno);
					ExecutionBVO[] childrens = (ExecutionBVO[]) aggVO
							.getChildren(ExecutionBVO.class);
					// 查询出原来有的数据
					Map<String, String> bvopkinfo = new HashMap<String, String>();
					Map<String, String> info = new HashMap<String, String>();
					Map<String, UFDateTime> tsinfo = new HashMap<String, UFDateTime>();
					List<String> billnolist = new ArrayList<String>();

					if (childrens != null && childrens.length > 0) {
						for (ExecutionBVO executionBVO : childrens) {
							info.put(executionBVO.getDef15(),
									executionBVO.getDef15());
							tsinfo.put(executionBVO.getDef15(),
									executionBVO.getTs());
							bvopkinfo.put(executionBVO.getDef15(),
									executionBVO.getPk_execution_b());
						}
						if (info.size() > 0) {
							billnolist.addAll(info.values());
						}
					}
					// 数据库查出来的更新数据
					if (data != null && data.size() > 0) {
						List<ExecutionBVO> list = new ArrayList<ExecutionBVO>();
						for (Map<String, Object> map : data) {
							// 更新数据的单据号
							String syscode = (String) map.get("syscode");
							String nc_number = (String) map.get("nc_number");
							if (nc_number != null && !"".equals(nc_number)) {
								String billno = info.get(nc_number);
								if (billno != null && !"".equals(billno)&& "NC".equals(syscode)) {
									ExecutionBVO bvo = setExecutionBVO(map);
									bvo.setPk_execution_b(bvopkinfo.get(billno));
									bvo.setPk_fct_ap(aggVO.getPrimaryKey());
									bvo.setTs(tsinfo.get(billno));
									bvo.setStatus(VOStatus.UPDATED);
									list.add(bvo);
									billnolist.remove(billno);
								} else {
									ExecutionBVO bvo = setExecutionBVO(map);
									bvo.setPk_fct_ap(aggVO.getPrimaryKey());
									bvo.setStatus(VOStatus.NEW);
									list.add(bvo);
								}

							}
							aggVO.setChildren(ExecutionBVO.class,
									list.toArray(new ExecutionBVO[0]));
						}

						// 删除原来的数据
						if (billnolist != null && billnolist.size() > 0) {
							String condition = " pk_fct_ap='"
									+ aggVO.getPrimaryKey() + "' and dr = 0   ";
							String sqlwhere = "";
							for (String value : billnolist) {
								sqlwhere += "'" + value + "',";
							}
							sqlwhere = sqlwhere.substring(0,
									sqlwhere.length() - 1);
							condition += " and def15  in(" + sqlwhere + ")";
							Collection<ISuperVO> coll = getBaseDAO()
									.retrieveByClause(ExecutionBVO.class,
											condition);
							if (coll != null && coll.size() > 0) {
								for (ISuperVO obj : coll) {
									ExecutionBVO vo = (ExecutionBVO) obj;
									vo.setStatus(VOStatus.DELETED);
									vo.setAttributeValue("dr", 1);
									list.add(vo);
								}
								aggVO.setChildren(ExecutionBVO.class,
										list.toArray(new ExecutionBVO[0]));
							}

						}
						/**
						 * 设置合同一些新加字段-2020-06-19-谈子健
						 * 
						 */
						aggVO = setFctApBillDate(aggVO);
						getEBSServcie()
								.syncExecutionDataUpdateExecution_RequiresNew(
										aggVO, title, msg);

					}

				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return null;

	}

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	public IMDPersistenceQueryService getQueryServcie() {
		if (queryServcie == null) {
			queryServcie = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return queryServcie;
	}

	public ISyncEBSServcie getEBSServcie() {
		if (ebsService == null) {
			ebsService = NCLocator.getInstance().lookup(ISyncEBSServcie.class);
		}
		return ebsService;
	}

	/**
	 * 读取业务单据聚合VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getQueryServcie().queryBillOfVOByCond(c,
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}

	private List<Map<String, Object>> getDate(String billno)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select e.zyx2 as fnumber,  ");
		query.append("       '' as fapplynumber,  ");
		query.append("       to_date(e.djrq, 'YYYY-MM-DD hh24:mi:ss') as nc_pat_date,  ");
		query.append("       e.total as fapplyamount,  ");
		query.append("       to_char(e.total) as nc_pay_amount,  ");
		query.append("       '付款' as type_code,  ");
		query.append("       nvl(c.name, '签约金额') as amount_type,  ");
		query.append("       e.djbh as nc_number,  ");
		query.append("       to_date(e.jsrq, 'YYYY-MM-DD hh24:mi:ss') as fapplydate,  ");
		query.append("       'NC' as syscode,  ");
		query.append("       decode(e.djzt, 3, '签字', '未付款') as djzt  ");
		query.append("  from er_bxzb e  ");
		query.append("  left join bd_defdoc c  ");
		query.append("    on c.pk_defdoc = e.zyx31  ");
		query.append(" where e.zyx2 = '" + billno + "'  ");
		query.append("   and e.djzt in (1, 2, 3)  ");
		query.append("   and e.dr = 0  ");
		query.append("union all  ");
		query.append("select fnumber,  ");
		query.append("       fapplynumber,  ");
		query.append("       to_date(nc_pat_date, 'YYYY-MM-DD hh24:mi:ss') nc_pat_date,  ");
		query.append("       fapplyamount,  ");
		query.append("       nc_pay_amount,  ");
		query.append("       type_code,  ");
		query.append("       amount_type,  ");
		query.append("       nc_number,  ");
		query.append("       to_date(fapplydate) fapplydate,  ");
		query.append("       'EBS' as syscode,  ");
		query.append("       decode(fapplyamount, 0, '未付款', '签字') djzt  ");
		query.append("  from CUX_OKC_EXPLAIN_PLAN_V@APPSTEST  ");
		query.append(" where fnumber = '" + billno + "'  ");
		query.append("   and last_update_date is not null  ");
		query.append("   and nc_number is not null  ");
		query.append("   and fapplynumber is not null  ");
//		query.append("   and not exists  ");
//		query.append(" (select 1  ");
//		query.append("          from fct_execution_b b  ");
//		query.append("         where dr = 0  ");
//		query.append("           and b.def16 = (fnumber || fapplynumber || nc_number))  ");

		List<Map<String, Object>> data = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(query.toString(), new MapListProcessor());
		return data;

	}

	private ExecutionBVO setExecutionBVO(Map<String, Object> map) {
		// 执行情况vo
		ExecutionBVO newExecutionBVO = new ExecutionBVO();
		// 合同编号
		String fnumber = (String) map.get("fnumber");

		// 来源系统
		String syscode = (String) map.get("syscode");
		newExecutionBVO.setDef6(syscode);

		// 最后修改时间
		UFDateTime dateTime = new UFDateTime();
		newExecutionBVO.setDef7(dateTime.toString());
		// 请款类型
		String amount_type = (String) map.get("amount_type");
		newExecutionBVO.setDef8(amount_type);
		// 请款单号
		Object fapplynumber = (String) map.get("fapplynumber");
		if (fapplynumber != null) {
			newExecutionBVO.setDef9(fapplynumber.toString());
		}
		// 请款日期
		Object fapplydate = map.get("fapplydate");
		if (fapplydate != null) {
			newExecutionBVO.setDef10(fapplydate.toString());
		}
		// 请款金额
		Object fapplyamount = map.get("fapplyamount");
		if (fapplyamount != null) {
			newExecutionBVO.setDef11(fapplyamount.toString());
		}
		// 单据金额
		Object nc_pay_amount = map.get("nc_pay_amount");
		if (nc_pay_amount != null) {
			newExecutionBVO.setDef12(nc_pay_amount.toString());
		}
		// 单据状态
		Object djzt = map.get("djzt");
		if (djzt != null) {
			newExecutionBVO.setDef4(djzt.toString());
		}

		// 操作方式
		newExecutionBVO.setDef13((String) map.get("type_code"));
		// 单据日期
		Object nc_pat_date = map.get("nc_pat_date");
		if (nc_pat_date != null) {
			newExecutionBVO.setDef14(nc_pat_date.toString());
		}
		// 单据编号
		String nc_number = (String) map.get("nc_number");
		if (nc_number != null) {
			newExecutionBVO.setDef15(nc_number);
		}
		// 拼接唯一标识
		String def16 = fnumber + fapplynumber + nc_number;
		newExecutionBVO.setDef16(def16);

		newExecutionBVO.setDr(0);

		return newExecutionBVO;
	}

	private ArrayList<String> getContractData() throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select a.vbillcode from fct_ap a  where a.blatest = 'Y'  and nvl(a.dr,0) = 0  ");
		ArrayList<String> data = (ArrayList<String>) getBaseDAO().executeQuery(
				query.toString(), new ColumnListProcessor());
		return data;
	}

	private AggCtApVO setFctApBillDate(AggCtApVO aggVO)
			throws BusinessException {
		// 执行情况vo
		ExecutionBVO[] executionBVOs = aggVO.getExecutionBVOs();
		if (executionBVOs != null) {
			// 累计保证金金额
			UFDouble totalBondAmount = new UFDouble("0");
			// 累计请款金额
			UFDouble totalAmount = new UFDouble("0");
			// 累计保证金已付金额
			UFDouble cumulativeTotalBondAmount = new UFDouble("0");
			// 累计请款已付金额
			UFDouble cumulativeTotalAmount = new UFDouble("0");
			// EBS已收发票金额
			UFDouble EbsInvoiceAmount = new UFDouble("0");

			for (ExecutionBVO executionBVO : executionBVOs) {
				Integer dr = executionBVO.getDr();
				if (dr == 0) {
					// 请款类型
					String amount_type = executionBVO.getDef8();
					// 请款金额
					String def11 = executionBVO.getDef11();
					// 单据状态
					String def4 = executionBVO.getDef4();
					// 来源系统
					String syscode = executionBVO.getDef6();

					UFDouble amount = new UFDouble(def11);
					String type = "保证金";
					if (amount_type.contains(type)) {
						totalBondAmount = totalBondAmount.add(amount);
						if ("签字".equals(def4)) {
							// 累计保证金已付金额
							cumulativeTotalBondAmount = cumulativeTotalBondAmount
									.add(amount);
						}
					} else {
						totalAmount = totalAmount.add(amount);
						if ("签字".equals(def4)) {
							// 累计请款已付金额
							cumulativeTotalAmount = cumulativeTotalAmount
									.add(amount);
						}
					}

				}
			}
			// EBS视图的（commoncontract_to_nc_v@APPSTEST）金额字段（sum_invoice）
			String sumInvoice = getSumInvoice(aggVO.getParentVO()
					.getVbillcode());
			EbsInvoiceAmount = new UFDouble(sumInvoice);
			aggVO.getParentVO().setAttributeValue("vdef25",
					totalAmount.toString());
			aggVO.getParentVO().setAttributeValue("vdef26",
					totalBondAmount.toString());
			aggVO.getParentVO().setAttributeValue("vdef27",
					cumulativeTotalAmount.toString());
			aggVO.getParentVO().setAttributeValue("vdef28",
					cumulativeTotalBondAmount.toString());
			aggVO.getParentVO().setAttributeValue("vdef29",
					EbsInvoiceAmount.toString());

			// 获取合同对应的报销单的发票累计金额
			String invoiceCumulativeAmount = getInvoiceCumulativeAmount(aggVO
					.getPrimaryKey());
			aggVO.getParentVO().setAttributeValue("vdef30",
					invoiceCumulativeAmount);
			UFDouble invoiceCumulativeAmountDouble = new UFDouble(
					invoiceCumulativeAmount);
			// 发票合计金额
			UFDouble def80 = EbsInvoiceAmount
					.add(invoiceCumulativeAmountDouble);
			aggVO.getParentVO().setAttributeValue("def80", def80.toString());
		}
		return aggVO;
	}

	private String getInvoiceCumulativeAmount(String pk)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select sum(amount) as totalamount  ");
		query.append("  from (select nvl(sum(to_number(z.zyx25)), 0) amount  ");
		query.append("          from fct_ap a  ");
		query.append("          left join er_bxzb z  ");
		query.append("            on a.vbillcode = z.zyx2  ");
		query.append("         where a.pk_fct_ap = '" + pk + "'  ");
		query.append("           and nvl(a.dr, 0) = 0  ");
		query.append("           and z.djlxbm in ('264X-Cxx-009')  ");
		query.append("           and z.spzt = 1  ");
		query.append("           and nvl(z.dr, 0) = 0  ");
		query.append("           and z.zyx25 <> '~'  ");
		query.append("           and nvl(a.dr, 0) = 0  ");
		query.append("           and a.blatest = 'Y'  ");
		query.append("        union all  ");
		query.append("        select nvl(sum(to_number(f.amount)), 0) amount  ");
		query.append("          from fct_ap a  ");
		query.append("          left join yer_fillbill f  ");
		query.append("            on a.vbillcode = f.concode  ");
		query.append("         where a.pk_fct_ap = '" + pk + "'  ");
		query.append("           and f.approvestatus = 1  ");
		query.append("           and nvl(a.dr, 0) = 0  ");
		query.append("           and nvl(f.dr, 0) = 0)  ");

		Object totalamount = (Object) getBaseDAO().executeQuery(
				query.toString(), new ColumnProcessor());

		return totalamount.toString();

	}

	// 获取EBS已收发票金额
	private String getSumInvoice(String billno) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select nvl(sum(to_number(v.sum_invoice)), 0)sum_invoice from commoncontract_to_nc_v@APPSTEST v where v.fnumber = '"
				+ billno + "'  ");
		Object sumInvoice = (Object) getBaseDAO().executeQuery(
				query.toString(), new ColumnProcessor());
		return sumInvoice.toString();
	}
}
