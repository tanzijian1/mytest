package nc.bs.tg.fund.rz.report.cost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.tg.fund.rz.report.ReportConts;
import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.cost.HydropowerDeductionInvVO;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.tgfp.report.ReportOrgVO;

import com.ufida.dataset.IContext;

/**
 * 水电自动划扣发票明细（成本报表）
 * 
 * @author ASUS
 * 
 */
public class HydropowerDeductionInvUtils extends ReportUtils {
	static HydropowerDeductionInvUtils utils;

	public static HydropowerDeductionInvUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new HydropowerDeductionInvUtils();
		}
		return utils;
	}

	public HydropowerDeductionInvUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new HydropowerDeductionInvVO()));
	}

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);
			List<HydropowerDeductionInvVO> list = null;
			list = getHydropowerDeductionInvListVOs();
			if (list != null) {
				if (queryValueMap.get("area") != null) {
					// 增加最后区域筛选
					List<HydropowerDeductionInvVO> finalcoll = new ArrayList<HydropowerDeductionInvVO>();
					// 初始化序号为0
					int sequeNumGlobal = 0;
					for (HydropowerDeductionInvVO hydropowerDeductionInvVO : list) {
						// 如果area有值,则认定为需显示查询数据
						// 每遍历一个元素，序号加1
						if (hydropowerDeductionInvVO.getArea() != null
								&& !"".equals(hydropowerDeductionInvVO
										.getArea())) {
							++sequeNumGlobal;
							hydropowerDeductionInvVO
									.setSortnumglobal(new UFDouble(
											sequeNumGlobal));
							finalcoll.add(hydropowerDeductionInvVO);
						}
					}
					cmpreportresults = transReportResult(finalcoll);
				} else {
					cmpreportresults = transReportResult(list);
				}
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

	private List<HydropowerDeductionInvVO> getHydropowerDeductionInvListVOs()
			throws BusinessException {
		// 获取所有公司层次集合VO
		List<ReportOrgVO> orgVOs = ReportConts.getUtils().getOrgVOs();
		// 存放pk_org和pk_region的对应关系
		Map<String, String> regionMap = new HashMap<String, String>();
		Map<String, ArrayList<String>> orgsMap = new HashMap<String, ArrayList<String>>();
		for (ReportOrgVO reportOrgVO : orgVOs) {
			String pk_org = reportOrgVO.getPk_org();
			String pk_region = reportOrgVO.getPk_region();
			// 存放pk_org——pk_region
			regionMap.put(pk_org, pk_region);
			/**
			 * 从orgsMap中获取pk_region对应的list集合，如果集合为null则
			 * orgsMap要存储以当前pk_region为key，存放了当前pk_org的list为value的新键值对
			 * 如果不为null则说明当前orgsMap中已存在pk_region为key的键值对，只需要往list 中存入当前pk_org即可
			 */
			ArrayList<String> arrayList = orgsMap.get(pk_region);
			if (arrayList != null) {
				arrayList.add(pk_org);
			} else {
				ArrayList<String> arrayList2 = new ArrayList<String>();
				arrayList2.add(pk_org);
				orgsMap.put(pk_region, arrayList2);
			}
		}
		// 存放区域pk_region的数组
		String[] pk_regionArr = null;
		// 存放区域pk_region对应的name
		Map<String, String> nameMap = new HashMap<String, String>();
		// 查询出输入条件的区域PK与区域名称name对应关系
		StringBuilder sqlarea = new StringBuilder();
		if (queryValueMap.get("area") != null) {
			pk_regionArr = queryValueMap.get("area").split(",");
			StringBuilder sqlstr = new StringBuilder();
			sqlstr.append("select name,pk_org from org_orgs where org_orgs.dr = 0");
			sqlstr.append(" and "
					+ SQLUtil.buildSqlForIn(" org_orgs.pk_org ", pk_regionArr));
			List<Map<String, String>> value = (List<Map<String, String>>) getBaseDAO()
					.executeQuery(sqlstr.toString(), new MapListProcessor());
			for (Map<String, String> map : value) {
				nameMap.put(map.get("pk_org"), map.get("name"));
			}
			ArrayList<String> arrpk_orgs = new ArrayList<String>();
			for (String string : pk_regionArr) {
				arrpk_orgs.add(string);
				ArrayList<String> arrpk_org = orgsMap.get(string);
				if (arrpk_org != null) {
					arrpk_orgs.addAll(arrpk_org);
				}
			}
			if (arrpk_orgs != null&&arrpk_orgs.size()>0) {
				sqlarea.append("and "
						+ SQLUtil.buildSqlForIn("org_orgs.pk_org",
								arrpk_orgs.toArray(new String[arrpk_orgs.size()])));
			}
		}

		StringBuffer sql = new StringBuffer();
		sql.append("select * ");
		sql.append(" from ( ");
		sql.append(" " + getPaybillData(queryValueMap, queryWhereMap, sqlarea)
				+ " ");// 付款单
		sql.append(" union all ");
		sql.append(" "
				+ getPayableBillData(queryValueMap, queryWhereMap, sqlarea)
				+ " ");// 应付单
		sql.append("  ) payandable ");

		sql.append(" order by payandable.contractcode ");
		List<HydropowerDeductionInvVO> coll = (List<HydropowerDeductionInvVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(HydropowerDeductionInvVO.class));
		// for (HydropowerDeductionInvVO hydropowerDeductionInvVO : coll) {

		Iterator<HydropowerDeductionInvVO> iterator = coll.iterator();
		// 初始化序号为0
		int sequeNumGlobal = 0;
		while (iterator.hasNext()) {
			HydropowerDeductionInvVO hydropowerDeductionInvVO = iterator.next();
			// 从VO中获取公司的pk_org
			String pk_org = hydropowerDeductionInvVO.getPk_org();
			// 根据公司的pk_org从regionMap中获取区域的pk_region
			String pk_region = regionMap.get(pk_org);
			// if("~".equals(pk_region)){
			// iterator.remove();
			// }else{
			++sequeNumGlobal;
			hydropowerDeductionInvVO.setSortnumglobal(new UFDouble(
					sequeNumGlobal));
			// 如果查询条件中area（区域）为null的话，nameMap是没有值的，所以这里new一个map来存放pk_region（区域）对应name（名称）
			Map<String, String> region_nameMap = new HashMap<String, String>();
			if (!"~".equals(pk_region)) {
				// 当nameMap有值时则查询条件中指定了区域PK，前面已经查询出区域pk对应的name名称
				if (nameMap.size() > 0) {
					// 根据区域的pk_region从nameMap中获取区域的name
					String name = nameMap.get(pk_region);
					hydropowerDeductionInvVO.setArea(name);
				} else {
					/**
					 * 当nameMap为空时则说明查询条件中没有区域pk，需要从查询结果costLedgerVOs中查找相应的区域pk
					 * 根据区域pk来查询区域名称name
					 * ，查出来后set进costLedgerVO，并且存入region_nameMap中，
					 * 避免重复查询区域名称（多个公司的pk_org可能对应一个区域pk）
					 */
					if (region_nameMap.get(pk_region) != null) {
						hydropowerDeductionInvVO.setArea(region_nameMap
								.get(pk_region));
					} else {
						String sql_region = "select name,pk_org from org_orgs where org_orgs.dr = 0 and pk_org = '"
								+ pk_region + "'";
						Map<String, String> value = (Map<String, String>) getBaseDAO()
								.executeQuery(sql_region, new MapProcessor());
						hydropowerDeductionInvVO.setArea(value == null ? null
								: value.get("name"));
						region_nameMap.put(
								value == null ? null : value.get("pk_org"),
								value == null ? null : value.get("name"));
					}
				}
			} else {
				hydropowerDeductionInvVO.setArea("");
			}
			// }
		}

		// }

		return coll.size() > 0 ? coll : null;
	}

	/**
	 * 获取付款单数据
	 * 
	 * @param queryValueMap
	 * @param queryWhereMap
	 * @param sqlstr
	 * @return
	 * @throws BusinessException
	 */
	private String getPaybillData(Map<String, String> queryValueMap,
			Map<String, String> queryWhereMap, StringBuilder sqlstr)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select '' sortnumglobal, ");//序号
		sql.append("   '' area, ");//区域公司
		sql.append("   fct_ap.vbillcode contractcode, ");//合同编号
		sql.append("    fct_ap.ctname contractname, ");//合同名称
		sql.append("     supplier1.name supplier, ");//供应商
		sql.append("    '' payreqnum, ");//付款申请单号
		sql.append("     doc1.name taxproperties, ");//计税性质
		sql.append("     voucherbill.cyear cyear, ");//凭证年
		sql.append("   voucherbill.cmonth cmonth, ");//凭证月
		sql.append("    voucherbill.cday cday, ");//凭证日
		sql.append("    org_orgs.pk_org pk_org, ");//公司pk
		sql.append("    org_orgs.name orgname, ");//公司名称
		sql.append("   doc2.name cost_type, ");//费用类型
		sql.append("    voucherbill.auxiliary_accounting_name auxiliary_accounting_name, ");//辅助核算名称
		sql.append("   voucherbill.vouchernum vouchernum, ");//凭证号
		sql.append("   voucherbill.memo memo, ");//摘要
		sql.append("    voucherbill.other_accchart other_accchart, ");//对方科目
		sql.append("   taxamount.pay_amount pay_amount, ");//付款金额
		sql.append("    0 reinv_amount, ");//收回发票金额
		sql.append("    taxamount.pay_amount lostinv_amount, ");//欠票金额
		sql.append("   voucherbill.accchart_name accchart_name, ");//科目名称
		sql.append("   '' invtype, ");//专票/普票
		sql.append("   '' special_ticket_ria, ");//专票税率
		sql.append("    0 settlementamount, ");//税额
		sql.append("   0 pricenotaxtotalamount ");//不含税金额
		sql.append(" from ap_paybill ");
		sql.append("  left join fct_ap ");
		sql.append("  on ap_paybill.def5 = fct_ap.pk_fct_ap ");
		sql.append("  and nvl(ap_paybill.dr, 0) = 0 ");
		sql.append("  left join bd_cust_supplier supplier1 ");
		sql.append(" on fct_ap.second = supplier1.pk_cust_sup ");
		sql.append("  and supplier1.dr = 0 ");
		sql.append(" left join bd_project ");
		sql.append("   on fct_ap.proname = bd_project.pk_project ");
		sql.append(" and nvl(bd_project.dr, 0) = 0 ");
		sql.append(" left join bd_defdoc doc1 ");
		sql.append("   on doc1.pk_defdoc = bd_project.def9 ");
		sql.append(" and nvl(doc1.dr, 0) = 0 ");
		sql.append("  and doc1.enablestate = 2 ");
		sql.append(" left join bd_defdoc doc2 ");
		sql.append("  on doc2.pk_defdoc = fct_ap.def100 ");
		sql.append("  and nvl(doc2.dr, 0) = 0 ");
		sql.append("  and doc2.enablestate = 2 ");
		sql.append("  left join org_orgs ");
		sql.append("  on fct_ap.pk_org = org_orgs.pk_org ");
		sql.append("  and org_orgs.dr = 0 ");
		sql.append("    and org_orgs.enablestate = 2 ");
		sql.append(" left join (select ap_paybill.pk_paybill, ");
		sql.append(" listagg(to_char(case ");
		sql.append(" when doc3.name is null then ");
		sql.append(" '' ");
		sql.append(" else ");
		sql.append(" doc3.name ");
		sql.append(" end), ");
		sql.append(" ',') within GROUP(order by doc3.name) invtype, ");
		sql.append(" sum(nvl(ap_payitem.local_money_de, 0)) pay_amount ");
		sql.append(" from fct_ap ");
		sql.append(" left join ap_paybill ");
		sql.append(" on ap_paybill.def5 = fct_ap.pk_fct_ap ");
		sql.append(" and nvl(ap_paybill.dr, 0) = 0 ");
		sql.append(" left join ap_payitem ");
		sql.append(" on ap_payitem.pk_paybill = ap_paybill.pk_paybill ");
		sql.append(" and nvl(ap_payitem.dr, 0) = 0 ");
		sql.append(" left join bd_defdoc doc3 ");
		sql.append(" on ap_payitem.def8 = doc3.pk_defdoc ");
		sql.append(" and nvl(doc3.dr, 0) = 0 ");
		sql.append(" and nvl(doc3.enablestate, 2) = 2 ");
		sql.append(" where ap_paybill.pk_tradetype = 'F3-Cxx-021' ");
		sql.append(" and ap_paybill.effectstatus = 10 ");
		sql.append(" and ap_paybill.settleflag = 1 ");
		sql.append(" and fct_ap.blatest = 'Y' ");
		sql.append(" group by ap_paybill.pk_paybill) taxamount ");
		sql.append("  on taxamount.pk_paybill = ap_paybill.pk_paybill ");
		sql.append("  left join (select ap_paybill.pk_paybill, ");
		sql.append(" gl_voucher.pk_voucher, ");
		sql.append(" gl_voucher.num vouchernum, ");
		sql.append(" gl_voucher.explanation memo, ");
		sql.append(" listagg(to_char(case ");
		sql.append(" when gl_detail.creditamount = 0 then ");
		sql.append(" null ");
		sql.append(" else ");
		sql.append(" acc1.name ");
		sql.append(" end), ");
		sql.append(" ',') within GROUP(order by acc1.name) other_accchart, ");
		sql.append(" listagg(to_char(case ");
		sql.append(" when gl_detail.debitamount = 0 then ");
		sql.append(" null ");
		sql.append(" else ");
		sql.append(" acc1.name ");
		sql.append(" end), ");
		sql.append(" ',') within GROUP(order by acc1.name) accchart_name, ");
		sql.append(" listagg(to_char(case ");
		sql.append(" when supplier2.name = '' then ");
		sql.append(" null ");
		sql.append(" else ");
		sql.append(" supplier2.name ");
		sql.append(" end), ");
		sql.append(" ',') within GROUP(order by supplier2.name) auxiliary_accounting_name, ");
		sql.append(" substr(gl_voucher.creationtime,1,4) cyear, ");
		sql.append(" substr(gl_voucher.creationtime,6,2) cmonth, ");
		sql.append(" substr(gl_voucher.creationtime,9,2) cday ");
		sql.append(" from ap_paybill ");
		sql.append(" left join fip_relation ");
		sql.append(" on fip_relation.src_relationid = ap_paybill.pk_paybill ");
		sql.append(" and nvl(fip_relation.dr, 0) = 0 ");
		sql.append(" left join gl_voucher ");
		sql.append(" on gl_voucher.pk_voucher = fip_relation.des_relationid ");
		sql.append(" and nvl(gl_voucher.dr, 0) = 0 ");
		sql.append(" left join gl_detail ");
		sql.append(" on gl_detail.pk_voucher = gl_voucher.pk_voucher ");
		sql.append(" and nvl(gl_detail.dr, 0) = 0 ");
		sql.append(" left join bd_accasoa acc1 ");
		sql.append(" on acc1.pk_accasoa = gl_detail.pk_accasoa ");
		sql.append(" and nvl(acc1.dr, 0) = 0 ");
		sql.append(" left join gl_docfree1 ");
		sql.append(" on gl_detail.assid = gl_docfree1.assid ");
		sql.append(" and nvl(gl_docfree1.dr, 0) = 0 ");
		sql.append(" left join bd_cust_supplier supplier2 ");
		sql.append(" on supplier2.pk_cust_sup = gl_docfree1.f4 ");
		sql.append(" and nvl(supplier2.dr, 0) = 0 ");
		sql.append(" where ap_paybill.effectstatus = 10 ");
		sql.append(" and ap_paybill.settleflag = 1 ");
		sql.append(" and gl_voucher.period <> '00' ");
		sql.append(" and ap_paybill.pk_tradetype = 'F3-Cxx-021' ");
		sql.append(" group by ap_paybill.pk_paybill, ");
		sql.append(" substr(gl_voucher.creationtime, 1, 4), ");
		sql.append(" substr(gl_voucher.creationtime, 6, 2), ");
		sql.append(" substr(gl_voucher.creationtime, 9, 2), ");
		sql.append(" gl_voucher.pk_voucher, ");
		sql.append(" gl_voucher.explanation, ");
		sql.append(" gl_voucher.num) voucherbill ");
		sql.append("  on voucherbill.pk_paybill = ap_paybill.pk_paybill ");
		sql.append("  where ap_paybill.effectstatus = 10 ");
		sql.append("   and ap_paybill.settleflag = 1 ");
		sql.append("  and fct_ap.blatest = 'Y' ");
		sql.append("  and ap_paybill.pk_tradetype = 'F3-Cxx-021' ");

		// 公司
		if (queryValueMap.get("pk_org") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("org_orgs.pk_org", queryValueMap
							.get("pk_org").split(",")));
		}
		// 供应商
		if (queryValueMap.get("supplier") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("supplier1.pk_cust_sup",
							queryValueMap.get("supplier").split(",")));
		}
		// 费用类型
		if (queryValueMap.get("cost_type") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("doc2.pk_defdoc", queryValueMap
							.get("cost_type").split(",")));
		}
		// 凭证年
		if (queryValueMap.get("cyear") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("voucherbill.cyear", queryValueMap
							.get("cyear").split(",")));
			// sql.append(" and voucherbill.cyear = '"+queryValueMap.get("cyear")+"' ");
		}
		// 凭证月
		if (queryValueMap.get("cmonth") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("voucherbill.cmonth", queryValueMap
							.get("cmonth").split(",")));
			// sql.append(" and voucherbill.cmonth = '"+queryValueMap.get("cmonth")+"' ");
		}

		// 先筛选区域公司下属所有公司以增加效率
		if (sqlstr != null) {
			sql.append(sqlstr);
		}
		return sql != null ? sql.toString() : "";
	}

	/**
	 * 获取应付单数据
	 * 
	 * @param queryValueMap
	 * @param queryWhereMap
	 * @param sqlstr
	 * @return
	 * @throws BusinessException
	 */
	private String getPayableBillData(Map<String, String> queryValueMap,
			Map<String, String> queryWhereMap, StringBuilder sqlstr)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select '' sortnumglobal, ");//序号
		sql.append(" '' area, ");//区域
		sql.append(" fct_ap.vbillcode contractcode, ");//合同编码
		sql.append(" fct_ap.ctname contractname, ");//合同名称
		sql.append(" supplier1.name supplier, ");//供应商
		sql.append(" ap_payablebill.def2 payreqnum, ");//付款申请单号
		sql.append(" doc1.name taxproperties, ");//计税性质
		sql.append(" voucherbill.cyear cyear, ");//凭证年
		sql.append(" voucherbill.cmonth cmonth, ");//凭证月
		sql.append(" voucherbill.cday cday, ");//凭证日
		sql.append(" org_orgs.pk_org pk_org, ");//公司pk
		sql.append(" org_orgs.name orgname, ");//公司名称
		sql.append(" doc2.name cost_type, ");//费用类型
		sql.append(" voucherbill.auxiliary_accounting_name auxiliary_accounting_name, ");//辅助核算名称
		sql.append(" voucherbill.vouchernum vouchernum, ");//凭证号
		sql.append(" voucherbill.memo memo, ");//摘要
		sql.append(" voucherbill.other_accchart other_accchart, ");//对方科目
		sql.append(" 0 pay_amount, ");//付款金额
		sql.append(" ap_payablebill.money reinv_amount, ");//收回发票金额
		sql.append(" 0 - ap_payablebill.money lostinv_amount, ");//欠票金额
		sql.append("  voucherbill.accchart_name accchart_name, ");//科目名称
		sql.append(" taxamount.invtype invtype, ");//专票/普票
		sql.append(" taxamount.settlementratio special_ticket_ria, ");//专票税率
		sql.append(" taxamount.settlementamount settlementamount, ");//税额
		sql.append(" nvl(ap_payablebill.money, 0) - nvl(taxamount.settlementamount, 0) pricenotaxtotalamount ");//不含税金额
		sql.append(" from fct_ap ");
		sql.append(" left join bd_cust_supplier supplier1 ");
		sql.append("  on fct_ap.second = supplier1.pk_cust_sup ");
		sql.append(" and supplier1.dr = 0 ");
		sql.append(" left join bd_project ");
		sql.append("  on fct_ap.proname = bd_project.pk_project ");
		sql.append(" and bd_project.dr = 0 ");
		sql.append(" left join bd_defdoc doc1 ");
		sql.append("   on doc1.pk_defdoc = bd_project.def9 ");
		sql.append(" and doc1.dr = 0 ");
		sql.append("  and doc1.enablestate = 2 ");
		sql.append("  left join bd_defdoc doc2 ");
		sql.append("   on doc2.pk_defdoc = fct_ap.def100 ");
		sql.append("  and doc2.dr = 0 ");
		sql.append("   and doc2.enablestate = 2 ");
		sql.append("  left join org_orgs ");
		sql.append("    on fct_ap.pk_org = org_orgs.pk_org ");
		sql.append("  and org_orgs.dr = 0 ");
		sql.append("  and org_orgs.enablestate = 2 ");
		sql.append(" left join ap_payablebill ");
		sql.append("  on ap_payablebill.def5 = fct_ap.vbillcode ");
		sql.append("  and ap_payablebill.dr = 0 ");
		sql.append(" left join (select ap_payablebill.pk_payablebill, ");
		sql.append(" listagg(to_char(case ");
		sql.append(" when doc3.name is null then ");
		sql.append(" '' ");
		sql.append(" else ");
		sql.append(" doc3.name ");
		sql.append(" end), ");
		sql.append(" ',') within GROUP(order by doc3.name) invtype, ");
		sql.append(" to_char(listagg(to_char(case ");
		sql.append(" when ap_payableitem.taxrate is null then ");
		sql.append(" 0 ");
		sql.append(" else ");
		sql.append(" ap_payableitem.taxrate ");
		sql.append(" end, ");
		sql.append(" 'fm99999990.009999') || '%', ");
		sql.append("  ',') within ");
		sql.append(" GROUP(order by ap_payableitem.taxrate)) as settlementratio, ");
		sql.append(" sum(nvl(case ");
		sql.append(" when ap_payableitem.def15 is null or ");
		sql.append(" ap_payableitem.def15 = '' or ");
		sql.append(" ap_payableitem.def15 = '~' then ");
		sql.append(" 0 ");
		sql.append(" else ");
		sql.append(" to_number(ap_payableitem.def15) ");
		sql.append(" end, ");
		sql.append(" 0)) settlementamount ");

		sql.append(" from ap_payablebill ");
		sql.append(" left join ap_payableitem ");
		sql.append(" on ap_payableitem.pk_payablebill = ");
		sql.append(" ap_payablebill.pk_payablebill ");
		sql.append(" and ap_payableitem.dr = 0 ");
		sql.append(" left join fct_ap ");
		sql.append(" on ap_payablebill.def5 = fct_ap.vbillcode ");
		sql.append(" and ap_payablebill.dr = 0 ");
		sql.append(" left join bd_defdoc doc3 ");
		sql.append(" on ap_payableitem.def8 = doc3.pk_defdoc ");
		sql.append(" and doc3.dr = 0 ");
		sql.append(" and doc3.enablestate = 2 ");
		sql.append(" where ap_payablebill.pk_tradetype = 'F1-Cxx-001' ");
		sql.append(" and ap_payablebill.effectstatus = 10 ");
		sql.append(" and ap_payablebill.approvestatus = 1 ");
		sql.append(" and fct_ap.blatest = 'Y' ");
		sql.append(" group by ap_payablebill.pk_payablebill) taxamount ");
		sql.append(" on taxamount.pk_payablebill = ap_payablebill.pk_payablebill ");
		sql.append("  left join (select ap_payablebill.pk_payablebill, ");
		sql.append(" gl_voucher.pk_voucher, ");
		sql.append(" gl_voucher.num vouchernum, ");
		sql.append(" gl_voucher.explanation memo, ");
		sql.append(" listagg(to_char(case ");
		sql.append(" when gl_detail.creditamount = 0 then ");
		sql.append(" null ");
		sql.append(" else ");
		sql.append(" acc1.name ");
		sql.append(" end), ");
		sql.append(" ',') within GROUP(order by acc1.name) other_accchart, ");
		sql.append(" listagg(to_char(case ");
		sql.append(" when gl_detail.debitamount = 0 then ");
		sql.append(" null ");
		sql.append(" else ");
		sql.append(" acc1.name ");
		sql.append(" end), ");
		sql.append(" ',') within GROUP(order by acc1.name) accchart_name, ");
		sql.append(" listagg(to_char(case ");
		sql.append("  when supplier2.name = '' then ");
		sql.append(" null ");
		sql.append(" else ");
		sql.append(" supplier2.name ");
		sql.append(" end), ");
		sql.append(" ',') within GROUP(order by supplier2.name) auxiliary_accounting_name, ");
		sql.append(" substr(gl_voucher.creationtime, 1, 4) cyear, ");
		sql.append(" substr(gl_voucher.creationtime, 6, 2) cmonth, ");
		sql.append(" substr(gl_voucher.creationtime, 9, 2) cday ");

		sql.append(" from ap_payablebill ");

		sql.append(" left join fct_ap ");
		sql.append(" on ap_payablebill.def5 = fct_ap.vbillcode ");
		sql.append(" and nvl(fct_ap.dr, 0) = 0 ");
		sql.append(" left join fip_relation ");
		sql.append(" on fip_relation.src_relationid = ");
		sql.append(" ap_payablebill.pk_payablebill ");
		sql.append(" and fip_relation.dr = 0 ");
		sql.append(" left join gl_voucher ");
		sql.append(" on gl_voucher.pk_voucher = fip_relation.des_relationid ");
		sql.append(" and gl_voucher.dr = 0 ");
		sql.append(" left join gl_detail ");
		sql.append("              on gl_detail.pk_voucher = gl_voucher.pk_voucher ");
		sql.append("            and gl_detail.dr = 0 ");
		sql.append("           left join bd_accasoa acc1 ");
		sql.append("              on acc1.pk_accasoa = gl_detail.pk_accasoa ");
		sql.append("           and acc1.dr = 0 ");
		sql.append("          left join gl_docfree1 ");
		sql.append("              on gl_detail.assid = gl_docfree1.assid ");
		sql.append("              and gl_docfree1.dr = 0 ");
		sql.append("             left join bd_cust_supplier supplier2 ");
		sql.append("             on supplier2.pk_cust_sup = gl_docfree1.f4 ");
		sql.append("             and supplier2.dr = 0 ");
		sql.append("           where ap_payablebill.pk_tradetype = 'F1-Cxx-001' ");
		sql.append("           and ap_payablebill.effectstatus = 10 ");
		sql.append("               and ap_payablebill.approvestatus = 1 ");
		sql.append("             and fct_ap.blatest = 'Y' ");
		sql.append("              and nvl(ap_payablebill.dr, 0) = 0 ");
		sql.append("             group by ap_payablebill.pk_payablebill, ");
		sql.append("                     substr(gl_voucher.creationtime, 1, 4), ");
		sql.append("                      substr(gl_voucher.creationtime, 6, 2), ");
		sql.append("                     substr(gl_voucher.creationtime, 9, 2), ");
		sql.append("                    gl_voucher.explanation, ");
		sql.append("                    gl_voucher.pk_voucher, ");
		sql.append("                      gl_voucher.num) voucherbill ");
		sql.append("  on voucherbill.pk_payablebill = ap_payablebill.pk_payablebill ");
		sql.append(" where ap_payablebill.pk_tradetype = 'F1-Cxx-001' ");
		sql.append(" and ap_payablebill.effectstatus = 10 ");
		sql.append(" and ap_payablebill.approvestatus = 1 ");
		sql.append(" and fct_ap.blatest = 'Y' ");
		//这里要加个筛选,在付款单有这个合同的情况下才能获取数据
		sql.append(" and ap_payablebill.def5 in ");
		sql.append(" (select fct_ap.vbillcode ");
		sql.append(" from ap_paybill ");
		sql.append(" left join fct_ap on ap_paybill.def5 = fct_ap.pk_fct_ap ");
		sql.append(" where ap_paybill.pk_tradetype = 'F3-Cxx-021' ");
		sql.append(" and fct_ap.blatest = 'Y' ");
		sql.append(" and nvl(fct_ap.dr,0) = 0 ");
		sql.append(" and ap_paybill.effectstatus = 10 ");
		sql.append(" and ap_paybill.settleflag = 1 ");
		sql.append(" and nvl(ap_paybill.dr, 0) = 0) ");
		// 公司
		if (queryValueMap.get("pk_org") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("org_orgs.pk_org", queryValueMap
							.get("pk_org").split(",")));
		}
		// 供应商
		if (queryValueMap.get("supplier") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("supplier1.pk_cust_sup",
							queryValueMap.get("supplier").split(",")));
		}
		// 费用类型
		if (queryValueMap.get("cost_type") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("doc2.pk_defdoc", queryValueMap
							.get("cost_type").split(",")));
		}
		// 凭证年
		if (queryValueMap.get("cyear") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("voucherbill.cyear", queryValueMap
							.get("cyear").split(",")));
			// sql.append(" and voucherbill.cyear = '"+queryValueMap.get("cyear")+"' ");
		}
		// 凭证月
		if (queryValueMap.get("cmonth") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("voucherbill.cmonth", queryValueMap
							.get("cmonth").split(",")));
			// sql.append(" and voucherbill.cmonth = '"+queryValueMap.get("cmonth")+"' ");
		}
		// 先筛选区域公司下属所有公司以增加效率
		if (sqlstr != null) {
			sql.append(sqlstr);
		}

		return sql != null ? sql.toString() : "";
	}

}