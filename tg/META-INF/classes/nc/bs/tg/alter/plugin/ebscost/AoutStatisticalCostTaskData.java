package nc.bs.tg.alter.plugin.ebscost;

import java.util.HashMap;
import java.util.List;

import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.AoutStatisticalCostTask;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;

public class AoutStatisticalCostTaskData extends AoutStatisticalCostTask {

	protected void getStatisticalResult(BgWorkingContext bgwc)
			throws BusinessException {
		// 查询已入成本方法
		getIncomingCost();
		// 查询可抵扣进项税
		getInputTax();

	}

	private void getInputTax() throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select f.src_billtype, ");
		query.append("       p.pk_payablebill, ");
		query.append("       p.def5, ");
		query.append("       sum(gl_detail.localdebitamount) - sum(gl_detail.localcreditamount) tax ");
		query.append("  from ap_payablebill p ");
		query.append("  left join fip_relation f ");
		query.append("    on p.pk_payablebill = f.src_relationid ");
		query.append(" inner join gl_detail ");
		query.append("    on gl_detail.pk_voucher = des_relationid ");
		query.append(" inner join bd_account n ");
		query.append("    on n.pk_account = gl_detail.pk_account ");
		query.append("    and (n.code like '22211101%' or n.code like '2221110604%') ");
		query.append(" where f.src_billtype = 'F1-Cxx-001' ");
		query.append("   and gl_detail.dr = 0 ");
		query.append("   and gl_detail.discardflagv <> 'Y' ");
		query.append("   and gl_detail.voucherkindv <> 255 ");
		query.append("   and nvl(gl_detail.errmessage2, '~') = '~' ");
		query.append("   and gl_detail.tempsaveflag <> 'Y' ");
		query.append("   and gl_detail.voucherkindv <> 5 ");
		query.append(" group by src_billtype, p.pk_payablebill, p.def5; ");

		List<HashMap<String, Object>> listmap = (List<HashMap<String, Object>>) getBaseDAO()
				.executeQuery(query.toString(), new MapListProcessor());
		if (listmap != null) {
			for (HashMap<String, Object> map : listmap) {
				String vbillcode = (String) map.get("def5");
				Object tax = map.get("tax");
				if (vbillcode != null && tax != null) {
					String contractsql = "select a.pk_fct_ap from fct_ap a where a.vbillcode = '"
							+ vbillcode + "' and a.dr = 0 and a.blatest = 'Y' ";
					String pk_fct_ap = (String) getBaseDAO().executeQuery(
							contractsql, new ColumnProcessor());// 付款单对应付款申请单申请金额
					if (pk_fct_ap != null) {
						try {
							getBaseDAO()
									.executeUpdate(
											"update fct_ap a set def35 = '"
													+ tax.toString()
													+ "' where a.pk_fct_ap = '"
													+ pk_fct_ap
													+ "' and a.dr = 0 and a.blatest = 'Y' ");
						} catch (Exception e) {
							throw new BusinessException(e.getMessage());
						}
					}
				}
			}
		}

	}

	// 查询已入成本方法
	private void getIncomingCost() throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select f.src_billtype,  ");
		query.append("       p.pk_payablebill,  ");
		query.append("       p.def5,  ");
		query.append("       sum(gl_detail.localdebitamount) debitamount  ");
		query.append("  from ap_payablebill p  ");
		query.append("  left join fip_relation f  ");
		query.append("    on p.pk_payablebill = f.src_relationid  ");
		query.append(" inner join gl_detail  ");
		query.append("    on gl_detail.pk_voucher = des_relationid  ");
		query.append(" inner join bd_account n  ");
		query.append("    on n.pk_account = gl_detail.pk_account  ");
		query.append("   and (n.code like '5102%' or n.code like '5001%')  ");
		query.append(" where f.src_billtype = 'F1-Cxx-001'  ");
		query.append("   and gl_detail.dr = 0  ");
		query.append("   and gl_detail.discardflagv <> 'Y'  ");
		query.append("   and gl_detail.voucherkindv <> 255  ");
		query.append("   and nvl(gl_detail.errmessage2, '~') = '~'  ");
		query.append("   and gl_detail.tempsaveflag <> 'Y'  ");
		query.append("   and gl_detail.voucherkindv <> 5  ");
		query.append(" group by src_billtype,p.pk_payablebill, p.def5;  ");

		List<HashMap<String, Object>> listmap = (List<HashMap<String, Object>>) getBaseDAO()
				.executeQuery(query.toString(), new MapListProcessor());
		if (listmap != null) {
			for (HashMap<String, Object> map : listmap) {
				String vbillcode = (String) map.get("def5");
				Object debitamount = map.get("debitamount");
				if (vbillcode != null && debitamount != null) {
					String contractsql = "select a.pk_fct_ap from fct_ap a where a.vbillcode = '"
							+ vbillcode + "' and a.dr = 0 and a.blatest = 'Y' ";
					String pk_fct_ap = (String) getBaseDAO().executeQuery(
							contractsql, new ColumnProcessor());// 付款单对应付款申请单申请金额
					if (pk_fct_ap != null) {
						try {
							getBaseDAO()
									.executeUpdate(
											"update fct_ap a set def34 = '"
													+ debitamount.toString()
													+ "' where a.pk_fct_ap = '"
													+ pk_fct_ap
													+ "' and a.dr = 0 and a.blatest = 'Y' ");
						} catch (Exception e) {
							throw new BusinessException(e.getMessage());
						}
					}
				}
			}
		}

	}
}
