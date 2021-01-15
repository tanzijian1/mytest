package nc.bs.tg.fund.rz.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.itf.cmp.report.iufo.SmartProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.tax.TaxLiquidationCashFlowVO;
import nc.vo.tgfn.report.tax.TaxLiquidationProvisionVO;

import com.ufida.dataset.IContext;

public abstract class ReportUtils {
	String[] keys = null;
	BaseDAO baseDAO = null;
	protected Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	protected Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql

	protected String[] getKeys() {
		return keys;
	}

	protected void setKeys(String[] keys) {
		this.keys = keys;
	}

	/**
	 * 报表加工处理类
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public abstract DataSet getProcessData(IContext context)
			throws BusinessException;

	/**
	 * 将转换信息生成为DataSet
	 * 
	 * @param cmpreportresults
	 * @param keys
	 * @return
	 * @throws BusinessException
	 */
	protected DataSet generateDateset(CmpReportResultVO[] cmpreportresults,
			String[] keys) throws BusinessException {
		if (cmpreportresults == null) {
			if (cmpreportresults == null || cmpreportresults.length == 0) {
				if (cmpreportresults == null) {
					cmpreportresults = new CmpReportResultVO[] { new CmpReportResultVO(
							getKeys()) };
				}
			}
		}

		DataSet resultDataSet = new DataSet();
		resultDataSet.setMetaData(SmartProcessor.getMetaData(cmpreportresults));
		resultDataSet.setDatas(SmartProcessor.getDatas(cmpreportresults));
		return resultDataSet;
	}

	/**
	 * 转换报表返回参数类型
	 * 
	 * @param vos
	 * @return
	 * @throws Exception
	 */
	protected CmpReportResultVO[] transReportResult(List volist)
			throws Exception {
		if (volist == null || volist.size() == 0) {
			return null;
		}
		List<CmpReportResultVO> resultVOs = new ArrayList<CmpReportResultVO>();
		for (Object vo : volist) {
			CmpReportResultVO resultVO = new CmpReportResultVO(getKeys());
			for (int i$ = 0; i$ < keys.length; i$++) {
				if (!"primarykey".equals(keys[i$])) {
					resultVO.setValue(i$, BeanHelper.getProperty(vo, keys[i$]));
				}
			}
			resultVOs.add(resultVO);
		}
		return resultVOs.toArray(new CmpReportResultVO[0]);
	}

	/**
	 * 
	 * @param startDay
	 *            开始日期
	 * @param endDay
	 *            截止日期
	 * @return
	 * @throws Exception
	 */
	public static int getDaysBetween(UFDate startDate, UFDate endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate.asBegin().toDate());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long time1 = cal.getTimeInMillis();
		cal.setTime(endDate.asEnd().toDate());
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	/**
	 * 初始化查询条件信息
	 * 
	 * @param condVOs
	 */

	protected void initQuery(ConditionVO[] condVOs) {
		queryValueMap.clear();
		queryWhereMap.clear();
		for (ConditionVO condVO : condVOs) {
			if (condVO.getValue() != null && !"".equals(condVO.getValue())) {
				if (condVO.getDataType() == ConditionVO.DATE) {
					String[] dates = condVO.getValue().split("@@");
					queryValueMap.put(condVO.getFieldCode() + "_begin",
							new UFDate(dates[0]).asBegin().toString());
					queryValueMap.put(condVO.getFieldCode() + "_end",
							new UFDate(dates[dates.length - 1]).asEnd()
									.toString());

				} else {
					queryValueMap
							.put(condVO.getFieldCode(),
									condVO.getValue().replace("(", "")
											.replace(")", "").replace("'", ""));
				}
			}
			queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
		}
	}

	/**
	 * 查询现金流数据
	 * 
	 * @param 查询条件
	 * @return 现金流VO
	 * @throws BusinessException
	 */
	public List<TaxLiquidationCashFlowVO> getCash(ConditionVO[] conditionVOs) throws BusinessException {

		StringBuffer sql = new StringBuffer();
		sql.append("select a.pk_paybill,").append("\r\n");
		sql.append("o.pk_org,").append("\r\n");
		sql.append("p.pk_project,").append("\r\n");
		sql.append("o.name orgname,").append("\r\n");// --付款组织
		sql.append("p.project_name projectname,").append("\r\n");// --项目名称
		sql.append("v.num,").append("\r\n");// --凭证号
		sql.append("a.billno,").append("\r\n");// --nc支付单号
		sql.append("a.money,").append("\r\n");// --付款金额
		sql.append("a.def6 contract,").append("\r\n");// --合同
		sql.append("a.def2 applynum,").append("\r\n");// --ebs请款单号
		// sql.append("a.def18,").append("\r\n");//--收款方
		sql.append("su.name payee,").append("\r\n");// --收款方
		sql.append("a.def32,").append("\r\n");// --项目Pk
		sql.append("hz.taxid,").append("\r\n");// --纳税人识别号
		sql.append("cla.claname costsubject").append("\r\n");// --成本科目Pk
		sql.append("from ap_paybill a").append("\r\n");
		sql.append("left join ap_payitem b").append("\r\n");
		sql.append("on a.pk_paybill = b.pk_paybill").append("\r\n");
		sql.append("left join bd_project p").append("\r\n");
		sql.append("on p.pk_project = a.def32").append("\r\n");
		sql.append("left join org_orgs o").append("\r\n");
		sql.append("on o.pk_org = a.pk_org").append("\r\n");

		sql.append("left join hzvat_billmaintenance hz").append("\r\n");
		sql.append("on a.pk_org = hz.pk_org").append("\r\n");

		sql.append("left join bd_supplier su").append("\r\n");
		sql.append("on su.pk_supplier = a.def18").append("\r\n");

		sql.append("inner join fip_relation fip").append("\r\n");
		sql.append("on a.pk_tradetype = fip.src_billtype").append("\r\n");
		sql.append("and a.pk_paybill = fip.src_relationid").append("\r\n");
		sql.append("inner join gl_voucher v").append("\r\n");
		sql.append("on v.pk_voucher = fip.des_relationid").append("\r\n");

		sql.append("left join (  select  a1.pk_defdoc,").append("\r\n");
		sql.append("			   a1.code,").append("\r\n");
		sql.append(
				"              (a1.code || '\\' || a1.name || '\\' || a2.name || '\\' ")
				.append("\r\n");
		sql.append("              || a3.name || '\\' || a4.name ) as claname")
				.append("\r\n");
		sql.append("            from bd_defdoc a1").append("\r\n");
		sql.append(
				"            left join bd_defdoc a2 on a1.pid = a2.pk_defdoc")
				.append("\r\n");
		sql.append(
				"            left join bd_defdoc a3 on a2.pid = a3.pk_defdoc")
				.append("\r\n");
		sql.append(
				"            left join bd_defdoc a4 on a3.pid = a4.pk_defdoc")
				.append("\r\n");
		// sql.append("            left join bd_defdoc a5 on a4.pid = a5.pk_defdoc"
		// ).append("\r\n");
		sql.append(
				"            where  nvl(a1.dr,0)=0 and nvl(a2.dr,0)=0 and nvl(a3.dr,0)=0")
				.append("\r\n");
		sql.append("             and nvl(a4.dr,0)=0  )cla").append("\r\n");
		sql.append("            on b.def9 = cla.pk_defdoc").append("\r\n");

		sql.append("where nvl(a.dr, 0) = 0").append("\r\n");
		sql.append("and nvl(b.dr, 0) = 0").append("\r\n");
		sql.append("and nvl(p.dr, 0) = 0").append("\r\n");
		sql.append("and nvl(o.dr, 0) = 0").append("\r\n");
		sql.append(" and nvl(v.dr, 0) = 0").append("\r\n");
		sql.append("and nvl(fip.dr, 0) = 0").append("\r\n");
		sql.append("and nvl(hz.dr, 0) = 0").append("\r\n");
		sql.append("and nvl(su.dr, 0) = 0").append("\r\n");
		sql.append("and o.enablestate = 2").append("\r\n");
		if (conditionVOs != null && conditionVOs.length > 0) {
			String wheresql = new ConditionVO().getWhereSQL(conditionVOs);
			if (wheresql != null && !"".equals(wheresql)) {
				sql.append(" and "
						+ new ConditionVO().getWhereSQL(conditionVOs));
			}
		}
//		String pk_org = queryValueMap.get("pk_org");
//		String pk_project = queryValueMap.get("pk_project");
//		String billno = queryValueMap.get("billno");
//		String num = queryValueMap.get("num");
//		String applynum = queryValueMap.get("applynum");
//		String taxid = queryValueMap.get("taxid");
//		String costsubject = queryValueMap.get("costsubject");
//		String payee = queryValueMap.get("payee");
//
//		if (pk_org != null && !"".equals(pk_org)) {
//			sql.append("and o.pk_org = '" + pk_org + "'").append("\r\n");
//		}
//
//		if (pk_project != null && !"".equals(pk_project)) {
//			sql.append("and p.pk_project = '" + pk_project + "'")
//					.append("\r\n");
//		}
//
//		if (billno != null && !"".equals(billno)) {
//			sql.append("and a.billno = '" + billno + "'").append("\r\n");
//		}
//
//		if (applynum != null && !"".equals(applynum)) {
//			sql.append("and a.def2 = '" + applynum + "'").append("\r\n");
//		}
//
//		if (num != null && !"".equals(num)) {
//			sql.append("and v.num = '" + num + "'").append("\r\n");
//		}
//
//		if (taxid != null && !"".equals(taxid)) {
//			sql.append("and hz.taxid = '" + taxid + "'").append("\r\n");
//		}
//
//		if (costsubject != null && !"".equals(costsubject)) {
//			sql.append("and cla.code = '" + costsubject + "'").append("\r\n");
//		}
//
//		if (payee != null && !"".equals(payee)) {
//			sql.append("and a.def18 = '" + payee + "'").append("\r\n");
//		}

		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		List<TaxLiquidationCashFlowVO> listvos = new ArrayList<TaxLiquidationCashFlowVO>();

		if (list.size() > 0) {
			for (Map<String, Object> map : list) {
				TaxLiquidationCashFlowVO taxvo = new TaxLiquidationCashFlowVO();
				taxvo.setBillno((String) map.get("billno"));
				taxvo.setNum((Integer) map.get("num"));
				taxvo.setOrgname((String) map.get("orgname"));
				taxvo.setMoney((new UFDouble(
						String.valueOf(map.get("money") == null ? "" : map
								.get("money")))));
				taxvo.setContract((String) map.get("contract"));
				taxvo.setCostsubject((String) map.get("costsubject"));
				taxvo.setApplynum((String) map.get("applynum"));
				taxvo.setProjectname((String) map.get("projectname"));
				taxvo.setTaxid((String) map.get("taxid"));
				taxvo.setPayee((String) map.get("payee"));

				listvos.add(taxvo);
			}
		}

		return listvos == null || listvos.size() == 0 ? new ArrayList<TaxLiquidationCashFlowVO>()
				: listvos;
	}

	/**
	 * 计提数据查询
	 * 
	 * @return 计提vo
	 * @throws BusinessException
	 */
	public List<TaxLiquidationProvisionVO> getProvisions(ConditionVO[] conditionVOs)
			throws BusinessException {

		StringBuffer sqlstr = new StringBuffer("");
		sqlstr.append("select able.def2 applynum,").append("\r\n");// --ebs请款单号
		sqlstr.append("       able.billno nbillno,").append("\r\n");// --nc单据号
		sqlstr.append("       able.def5 contract, ").append("\r\n");// --合同编号
		// sqlstr.append("       --able.def6, ").append("\r\n");//--合同名称
		sqlstr.append("       cla.claname costsubject, ").append("\r\n");// --成本科目
		sqlstr.append("       item.def3 formats, ").append("\r\n");// --业态
		sqlstr.append("       numb.num, ").append("\r\n");// --凭证号
		sqlstr.append("       numb.explanation, ").append("\r\n");// --内容摘要
		sqlstr.append("       hz.taxid,").append("\r\n");// --纳税人识别号
		sqlstr.append("       p.project_name projectname, ").append("\r\n");// --项目名称
		sqlstr.append("       o.name orgname, ").append("\r\n");// --纳税人名称
		sqlstr.append("       fp.fph, ").append("\r\n");// --发票号
		sqlstr.append("       fptype.name fptype,").append("\r\n");// --发票类型
		sqlstr.append("       fp.costmoneyin, ").append("\r\n");// --发票合计总额
		sqlstr.append("       fp.dkmoney ").append("\r\n");// --可抵扣金额
		sqlstr.append("").append("\r\n");
		sqlstr.append("  from ap_payablebill able").append("\r\n");
		sqlstr.append("  left join ap_payableitem item").append("\r\n");
		sqlstr.append("     on able.pk_payablebill = item.pk_payablebill")
				.append("\r\n");
		sqlstr.append("   left join bd_project p").append("\r\n");
		sqlstr.append("     on p.pk_project = able.def32").append("\r\n");
		sqlstr.append("   left join org_orgs o").append("\r\n");
		sqlstr.append("     on o.pk_org = able.pk_org").append("\r\n");
		sqlstr.append("   left join hzvat_billmaintenance hz").append("\r\n");
		sqlstr.append("     on able.pk_org = hz.pk_org").append("\r\n");
		sqlstr.append("   left join hzvat_invoice_h fp").append("\r\n");
		sqlstr.append("     on able.def3 = fp.def8").append("\r\n");
		sqlstr.append("   left join hzvat_invoicetype fptype").append("\r\n");
		sqlstr.append("     on fptype.pk_invoicetype = fp.fplx").append("\r\n");
		sqlstr.append("").append("\r\n");
		sqlstr.append("").append("\r\n");
		sqlstr.append("   left join (  select  a1.pk_defdoc,").append("\r\n");
		sqlstr.append("   				a1.code,").append("\r\n");
		sqlstr.append(
				"                (a1.code || '\\' || a1.name || '\\' || a2.name || '\\'")
				.append("\r\n");
		sqlstr.append(
				"                || a3.name || '\\' || a4.name ) as claname")
				.append("\r\n");
		sqlstr.append("              from bd_defdoc a1").append("\r\n");
		sqlstr.append(
				"              left join bd_defdoc a2 on a1.pid = a2.pk_defdoc")
				.append("\r\n");
		sqlstr.append(
				"              left join bd_defdoc a3 on a2.pid = a3.pk_defdoc")
				.append("\r\n");
		sqlstr.append(
				"              left join bd_defdoc a4 on a3.pid = a4.pk_defdoc")
				.append("\r\n");
		sqlstr.append(
				"              where  nvl(a1.dr,0)=0 and nvl(a2.dr,0)=0 and nvl(a3.dr,0)=0")
				.append("\r\n");
		sqlstr.append(
				"               and nvl(a4.dr,0)=0 and a1.enablestate = 2 )cla")
				.append("\r\n");
		sqlstr.append("           on item.def7 = cla.pk_defdoc").append("\r\n");
		sqlstr.append("").append("\r\n");
		sqlstr.append("    left join (select ap.pk_payablebill,")
				.append("\r\n");
		sqlstr.append("                      v.explanation,").append("\r\n");
		sqlstr.append("                      v.num").append("\r\n");
		sqlstr.append("                from fip_relation fip").append("\r\n");
		sqlstr.append("               inner join ap_payablebill ap").append(
				"\r\n");
		sqlstr.append("                  on ap.pk_tradetype = fip.src_billtype")
				.append("\r\n");
		sqlstr.append(
				"                 and ap.pk_payablebill  = fip.src_relationid")
				.append("\r\n");
		sqlstr.append("               inner join gl_voucher v").append("\r\n");
		sqlstr.append(
				"                  on v.pk_voucher = fip.des_relationid )numb")
				.append("\r\n");
		sqlstr.append("           on able.pk_payablebill = numb.pk_payablebill")
				.append("\r\n");
		sqlstr.append("").append("\r\n");
		sqlstr.append("  where nvl(able.dr,0)=0").append("\r\n");
		sqlstr.append("        and nvl(item.dr,0)=0").append("\r\n");
		sqlstr.append("        and nvl(hz.dr,0)=0").append("\r\n");
		sqlstr.append("        and nvl(fp.dr,0)=0").append("\r\n");
		sqlstr.append("        and nvl(o.dr,0)=0").append("\r\n");
		sqlstr.append("        and nvl(p.dr,0)=0").append("\r\n");
		sqlstr.append("		   and o.enablestate = 2").append("\r\n");

		String pk_org = queryValueMap.get("pk_org");
		String pk_project = queryValueMap.get("pk_project");
		String taxid = queryValueMap.get("taxid");
		String fph = queryValueMap.get("fph");
		String applynum = queryValueMap.get("applynum");// able.def2
		String nbillno = queryValueMap.get("nbillno");
		String formats = queryValueMap.get("formats");// item.def3
		String costsubject = queryValueMap.get("costsubject");// cla.claname
		String contract = queryValueMap.get("contract");// able.def5
		if (conditionVOs != null && conditionVOs.length > 0) {
			String wheresql = new ConditionVO().getWhereSQL(conditionVOs);
			if (wheresql != null && !"".equals(wheresql)) {
				sqlstr.append(" and "
						+ new ConditionVO().getWhereSQL(conditionVOs));
			}
		}
//		if (pk_org != null && !"".equals(pk_org)) {
//			sqlstr.append("        and o.pk_org = '" + pk_org + "'").append(
//					"\r\n");
//		}
//		if (pk_project != null && !"".equals(pk_project)) {
//			sqlstr.append("        and p.pk_project = '" + pk_project + "'")
//					.append("\r\n");
//		}
//		if (taxid != null && !"".equals(taxid)) {
//			sqlstr.append("        and  hz.taxid= '" + taxid + "'").append(
//					"\r\n");
//		}
//		if (fph != null && !"".equals(fph)) {
//			sqlstr.append("        and  fp.fph= '" + fph + "'").append("\r\n");
//		}
//		if (applynum != null && !"".equals(applynum)) {
//			sqlstr.append("        and able.def2 = '" + applynum + "'").append(
//					"\r\n");
//		}
//		if (nbillno != null && !"".equals(nbillno)) {
//			sqlstr.append("        and able.billno = '" + nbillno + "'")
//					.append("\r\n");
//		}
//		if (formats != null && !"".equals(formats)) {
//			sqlstr.append("        and item.def3 = '" + formats + "'").append(
//					"\r\n");
//		}
//		if (costsubject != null && !"".equals(costsubject)) {
//			sqlstr.append("        and cla.code = '" + costsubject + "'")
//					.append("\r\n");
//		}
//		if (contract != null && !"".equals(contract)) {
//			sqlstr.append("        and able.def5 = '" + contract + "'");
//		}

		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sqlstr.toString(), new MapListProcessor());

		List<TaxLiquidationProvisionVO> listvos = new ArrayList<TaxLiquidationProvisionVO>();

		if (list.size() > 0) {
			for (Map<String, Object> map : list) {
				TaxLiquidationProvisionVO provo = new TaxLiquidationProvisionVO();
				provo.setApplynum((String) map.get("applynum"));
				provo.setFph((String) map.get("fph"));
				provo.setCostmoneyin(new UFDouble(String.valueOf(map
						.get("costmoneyin") == null ? "" : map
						.get("costmoneyin"))));
				provo.setFptype((String) map.get("fptype"));
				provo.setContract((String) map.get("contract"));
				provo.setCostsubject((String) map.get("costsubject"));
				provo.setFormats((String) map.get("formats"));
				// provo.setFormdata(new UFDate((String)map.get("formdata")));
				provo.setExplanation((String) map.get("explanation"));
				provo.setNbillno((String) map.get("nbillno"));
				provo.setNum((Integer) map.get("num"));
				provo.setOrgname((String) map.get("orgname"));
				provo.setProjectname((String) map.get("projectname"));
				provo.setDkmoney(new UFDouble(
						String.valueOf(map.get("dkmoney") == null ? "" : map
								.get("dkmoney"))));
				provo.setTaxid((String) map.get("taxid"));

				listvos.add(provo);
			}
		}

		return listvos == null || listvos.size() == 0 ? new ArrayList<TaxLiquidationProvisionVO>()
				: listvos;
	}

	/**
	 * 查出所有区域公司
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<String> getRegionalCompany() throws BusinessException {
		String orgnamesql = "select pk_org from org_orgs where dr = 0 and enablestate ='2' and def3 in (select pk_defdoc from bd_defdoc where name = '是')";

		List<String> list = (List<String>) getBaseDAO().executeQuery(
				orgnamesql, new ColumnListProcessor());
		return list == null || list.size() == 0 ? new ArrayList<String>()
				: list;
	}

	/**
	 * 查出板块公司
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBanCompany() throws BusinessException {
		String orgnamesql = "select pk_org from org_orgs where dr = 0 and enablestate ='2' and code = 'DC0001'";

		List<String> list = (List<String>) getBaseDAO().executeQuery(
				orgnamesql, new ColumnListProcessor());
		return list == null || list.size() == 0 ? new ArrayList<String>()
				: list;
	}

	/**
	 * 递归求区域公司
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String configRegionOrg(String pk_org) throws BusinessException {
		List<String> list = getRegionalCompany();
		if ("000112100000000005FD".equals(pk_org)) {
			return getorgname(pk_org);
		}
		if (getBanCompany().contains(pk_org)) {
			return getorgname(pk_org);
		}
		if (list.contains(pk_org)) {
			return getorgname(pk_org);
		} else {
			return configRegionOrg(getSuppk_org(pk_org));
		}
	}

	/**
	 * 转换为组织名称
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getorgname(String pk_org) throws BusinessException {
		String orgnamesql = "select name from org_orgs where pk_org = '"
				+ pk_org + "' and nvl(dr,0) = 0";
		String orgnam = (String) getBaseDAO().executeQuery(orgnamesql,
				new ColumnProcessor());
		return orgnam;
	};

	/**
	 * 查询上级业务单元
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getSuppk_org(String pk_org) throws BusinessException {
		String sql = "select pk_fatherorg from org_orgs where nvl(dr,0) = 0 and enablestate ='2' and pk_org = '"
				+ pk_org + "'";
		String pk_fatherorg = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_fatherorg;
	};

	/**
	 * 查询对应预提单编号
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public String getAccrued_billno(String bxd_billno) throws BusinessException {
		String sql = "select accrued_billno from er_accrued_verify where nvl(dr,0) = 0 and bxd_billno = '"
				+ bxd_billno + "'";
		List<String> accrued_billnos = (List<String>) getBaseDAO()
				.executeQuery(sql, new ColumnListProcessor());
		return accrued_billnos == null ? null : accrued_billnos.toString();
	};
}
