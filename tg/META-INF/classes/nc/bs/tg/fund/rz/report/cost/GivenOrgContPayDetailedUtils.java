package nc.bs.tg.fund.rz.report.cost;

import java.util.List;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.cost.GivenOrgContPayDetailedVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 东莞裕景合同付款明细（成本报表）
 * 
 * @author ASUS
 * 
 */
public class GivenOrgContPayDetailedUtils extends ReportUtils {
	static GivenOrgContPayDetailedUtils utils;

	public static GivenOrgContPayDetailedUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new GivenOrgContPayDetailedUtils();
		}
		return utils;
	}

	public GivenOrgContPayDetailedUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new GivenOrgContPayDetailedVO()));
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
			List<GivenOrgContPayDetailedVO> list = getGivenOrgContPayDetailedListVOs();

			if (list.size() > 0 && list != null) {
				cmpreportresults = transReportResult(list);
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

	private List<GivenOrgContPayDetailedVO> getGivenOrgContPayDetailedListVOs()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  o1.name                       pk_org,");
		sql.append(" fct_ap.ctrantypeid            trantype,");
		sql.append(" fct_ap.contype                contracttype,");
		sql.append(" fct_ap.vbillcode              contractcode,");
		sql.append(" fct_ap.ctname                 as contractname,");
		sql.append(" bd_cust_supplier.name         supplier,");
		sql.append(" ap_paybill.money payamount,");
		sql.append(" ap_payablebill.def2 paynum,");
		sql.append(" bala.name paytype,");
		sql.append("        (case when ap_paybill.billstatus = 9 then '未确认' ");
		sql.append("       when ap_paybill.billstatus = -1 then '保存' ");
		sql.append("      when ap_paybill.billstatus = 1 then '审批通过' ");
		sql.append("      when ap_paybill.billstatus = 2 then '审批中'  ");
		sql.append("       when ap_paybill.billstatus = -99 then '暂存' ");
		sql.append("       when ap_paybill.billstatus = 8 then '签字' ");
		sql.append("        else '' end) stauts, ");
		
		sql.append(" (case");
		sql.append(" when gl_voucher.year is not null and");
		sql.append(" gl_voucher.period is not null then");
		sql.append(" gl_voucher.year || '-' || gl_voucher.period");
		sql.append(" else");
		sql.append("   ''");
		sql.append(" end) infodate,");//入账月份
		
		sql.append(" taxamount.pricetaxtotalamount pricetaxtotalamount,");//发票金额成本+税额（价税合计）
		sql.append(" taxamount.pricenotaxtotalamount pricenotaxtotalamount,");//成本金额（不含税）
		sql.append(" taxamount.settlementratio settlementrat,");//税率
		sql.append(" taxamount.settlementamount settlementamount,");//税额
		sql.append(" gl_voucher.num vouchernum,");//凭证号
		
		sql.append(" case");
		sql.append("  when tgfn_payrequest.def31 = '~' then");
		sql.append("  ''");
		sql.append("  else");
		sql.append(" tgfn_payrequest.def31");
		sql.append(" end memo");//备注
		sql.append(" ,b.project_name projectname ");//项目
		
		sql.append(" from fct_ap");

		sql.append(" left join ap_payablebill");
		sql.append(" on ap_payablebill.def5 = fct_ap.vbillcode");
		sql.append(" and ap_payablebill.dr = 0");
		sql.append(" left join (select ap_payablebill.pk_payablebill,");
		sql.append(" sum(ap_payableitem.local_notax_cr) pricenotaxtotalamount,");
		sql.append(" sum(ap_payableitem.local_tax_cr) settlementamount,");
		sql.append(" sum(ap_payableitem.local_money_cr) pricetaxtotalamount,");
		sql.append(" to_char(listagg(to_char(case");
		sql.append("  when ap_payableitem.taxrate is null then");
		sql.append("   0");
		sql.append(" else");
		sql.append(" ap_payableitem.taxrate");
		sql.append("  end,");
		sql.append(" 'fm99999990.009999') || '%',");
		sql.append(" ',') within");
		sql.append(" GROUP(order by ap_payableitem.taxrate)) as settlementratio");
		sql.append(" from ap_payablebill");
		sql.append(" left join ap_payableitem");
		sql.append(" on ap_payablebill.pk_payablebill =");
		sql.append(" ap_payableitem.pk_payablebill ");
		sql.append(" and ap_payableitem.dr = 0 ");
		sql.append("  where ap_payablebill.dr = 0 ");
		sql.append(" group by ap_payablebill.pk_payablebill) taxamount ");
		sql.append("  on taxamount.pk_payablebill = ap_payablebill.pk_payablebill ");
		
		sql.append("  left join fip_relation ");
		sql.append(" on fip_relation.src_relationid = ap_payablebill.pk_payablebill ");
		sql.append("  and fip_relation.dr = 0 ");
		sql.append("  left join gl_voucher ");
		sql.append(" on gl_voucher.pk_voucher = fip_relation.des_relationid ");
		sql.append(" and gl_voucher.dr = 0 ");
		sql.append("  left join bd_cust_supplier");
		sql.append(" on bd_cust_supplier.pk_cust_sup = fct_ap.second ");
		sql.append("  and bd_cust_supplier.dr = 0 ");
		sql.append("  left join org_orgs o1");
		sql.append("  on o1.pk_org = fct_ap.pk_org ");
		sql.append("  and o1.dr = 0 ");
		sql.append("  left join tgfn_payrequest ");
		sql.append(" on tgfn_payrequest.pk_payreq = ap_payablebill.def2 ");
		sql.append(" left join ap_paybill ");
		sql.append("  on ap_paybill.def2 = ap_payablebill.def2 ");
		sql.append(" left join ( ");
		sql.append(" 		   select distinct ap_payitem.pk_paybill, ");
		sql.append(" 		   bala1.name ");
		sql.append(" 		   from ap_payitem ");
		sql.append(" 		   left join bd_balatype bala1 ");
		sql.append(" 	     on bala1.pk_balatype = ap_payitem.pk_balatype ");
		sql.append(" 		    and bala1.dr = 0 ");
		sql.append(" 		   where ap_payitem.dr = 0 ");
		sql.append(" 		   ) bala ");
		sql.append(" 		   on bala.pk_paybill = ap_paybill.pk_paybill ");
		sql.append(" left join bd_project b on b.pk_project = fct_ap.proname and b.dr = 0 ");//add by 黄冠华 SDYC-391 需求调整：合同付款明细（成本报表）添加项目字段 20200819
		
		sql.append(" where ap_payablebill.effectstatus = 10 ");// 生效状态
		sql.append("and ap_payablebill.approvestatus = 1 ");// 结算状态
		sql.append("and ap_paybill.effectstatus = 10 ");// 结算状态
		sql.append("and ap_paybill.settleflag = 1 ");// 结算状态
		sql.append(" and fct_ap.dr = 0 ");
		sql.append(" and o1.enablestate = 2 ");
		sql.append(" and fct_ap.blatest = 'Y' ");
		sql.append(" and gl_voucher.period <> '00' ");
		// 核算公司
		if (queryValueMap.get("pk_org") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("fct_ap.pk_org",
							queryValueMap.get("pk_org").split(",")));
		}
		// 合同编码
		if (queryValueMap.get("contractcode") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("fct_ap.vbillcode", queryValueMap
							.get("contractcode").split(",")));
		}
		// 合同名称
		if (queryValueMap.get("contractname") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("fct_ap.ctname",
							queryValueMap.get("contractname").split(",")));
		}
		// 交易类型
		if (queryValueMap.get("trantype") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("fct_ap.ctrantypeid", queryValueMap
							.get("trantype").split(",")));
		}
		// 入帐时间(入帐时间只有年月)
		if (queryValueMap.get("infodate_begin") != null
				&& queryValueMap.get("infodate_end") == null) {
			sql.append(" and (case when gl_voucher.year is not null and gl_voucher.period is not null then gl_voucher.year||'-'||gl_voucher.period else '' end) >= substr('"
					+ queryValueMap.get("infodate_begin") + "',1,7) ");
		} else if (queryValueMap.get("infodate_begin") == null
				&& queryValueMap.get("infodate_end") != null) {
			sql.append(" and (case when gl_voucher.year is not null and gl_voucher.period is not null then gl_voucher.year||'-'||gl_voucher.period else '' end) <= substr('"
					+ queryValueMap.get("infodate_end") + "',1,7) ");
		} else if (queryValueMap.get("infodate_begin") != null
				&& queryValueMap.get("infodate_end") != null) {
			sql.append(" and (case when gl_voucher.year is not null and gl_voucher.period is not null then gl_voucher.year||'-'||gl_voucher.period else '' end) >= substr('"
					+ queryValueMap.get("infodate_begin") + "',1,7) ");
			sql.append(" and (case when gl_voucher.year is not null and gl_voucher.period is not null then gl_voucher.year||'-'||gl_voucher.period else '' end) <= substr('"
					+ queryValueMap.get("infodate_end") + "',1,7) ");
		}
		//add by 黄冠华 SDYC-391 需求调整：合同付款明细（成本报表）添加项目查询条件 begin
		// 项目 
		String pk_project=queryValueMap.get("pk_project");
		if (pk_project != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("fct_ap.proname",pk_project.split(",")));
		}
		//add by 黄冠华 SDYC-391 需求调整：合同付款明细（成本报表）添加项目查询条件 end
//		sql.append(" order by fct_ap.vbillcode ");
		List<GivenOrgContPayDetailedVO> coll = (List<GivenOrgContPayDetailedVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(GivenOrgContPayDetailedVO.class));
		return coll;
	}
}