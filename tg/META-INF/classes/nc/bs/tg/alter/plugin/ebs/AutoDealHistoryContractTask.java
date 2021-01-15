package nc.bs.tg.alter.plugin.ebs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.erm.expenseaccount.PayPlanDetailVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
/**
 * 处理历史合同数据
 * @author ln
 *
 */
public class AutoDealHistoryContractTask implements IBackgroundWorkPlugin{
	
	BaseDAO baseDAO = null;
	private BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	@Override
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		// TODO 自动生成的方法存根
		String pk_jkbx = null;// 报销单PK
		String primaryKey = null;// 合同页签PK
		String vbillcode = null;// 合同编号
		UFDouble total = UFDouble.ZERO_DBL;// 报销总金额
		String payStyle = null;// 款项类型
		UFDate d_payplan = null;// 计划付款日期
		UFDouble planrate = UFDouble.ZERO_DBL;// 付款比率
		UFDouble planmoney = UFDouble.ZERO_DBL;// 应付款金额
		String pay_condition = null;// 付款条件
		List<PayPlanDetailVO> volist = new ArrayList<PayPlanDetailVO>();
		String bxsql = "select distinct def.code from bd_defdoc def " +
				"left join bd_defdoclist li on li.pk_defdoclist = def.pk_defdoclist " + 
				"where li.code = 'zdy013' and li.name = '合同' and def.def15!='0' and def.def22 ! = 'Y';";

		List<String> li = (List<String>) getBaseDAO().executeQuery(bxsql, new ColumnListProcessor());
		if (li.size() > 0) {
			int i = 0;
			List<Map<String, String>> list = null;
			for (String str : li) {
				Boolean flag = false;//判断实际请款金额是否已插入付款计划页签
				vbillcode = str;
				bxsql = "SELECT sett.primal FROM cmp_settlement sett " +
						"LEFT JOIN er_bxzb de ON de.pk_jkbx = sett.pk_busibill " + 
						"WHERE  de.zyx2 = '"+vbillcode+"' AND NVL(sett.dr,0)=0  AND NVL(de.dr,0)=0 AND sett.settlestatus in(5,6,9) and nvl(sett.dr,0)=0;";
				List<String> applyedlist = (List<String>) getBaseDAO().executeQuery(
						bxsql, new ColumnListProcessor());
				bxsql = "select pk_jkbx,total from er_bxzb " +  
						"where nvl(zyx2,'~')<>'~' and nvl(dr,0)=0 and djlxbm = '264X-Cxx-001' and spzt in(1,2,3) and zyx2 = '"+vbillcode+"'";
				Map<String,String> bxmap = (Map<String, String>) getBaseDAO().executeQuery(bxsql, new MapProcessor());
				if(bxmap.size() >0){
					for(int j=0 , n = 1; j<n; j++){
						pk_jkbx = bxmap.get("pk_jkbx");
						total = new UFDouble(bxmap.get("total") == null ? "":String.valueOf(bxmap.get("total")));
						
						String payplanSql = null;
						StringBuffer sql = new StringBuffer();
						sql.append("SELECT p.pk_fct_ap_plan as pk, p.def2, p.d_payplan, p.planrate, p.planmoney, p.pay_condition ");
						sql.append("	, p.msumapply, p.msumpayed ");
						sql.append("FROM fct_ap_plan p ");
						sql.append("WHERE nvl(p.dr,0) = 0 AND p.pk_fct_ap = ( ");
						sql.append("	SELECT pk_fct_ap ");
						sql.append("	FROM fct_ap ");
						sql.append("	WHERE (nvl(fct_ap.dr, 0) = 0 ");
						sql.append("		AND fct_ap.blatest = 'Y' ");
						sql.append("		AND fct_ap.vbillcode = '"+vbillcode+"' )");
						sql.append(");");
						payplanSql = sql.toString();
						if (payplanSql != null) {
							list = (List<Map<String, String>>) getBaseDAO().executeQuery(payplanSql,
									new MapListProcessor());
							String plSql = "select sum(total) from er_bxzb bx where spzt = 1 and bx.djlxbm = '264X-Cxx-001' and zyx2 = '"+vbillcode+"';";
							Object obj = (Object) getBaseDAO().executeQuery(plSql,
									new ColumnProcessor());
							UFDouble historyMny = new UFDouble(String.valueOf(obj == null ? 0 : obj));//历史已请款金额
							if (list.size() > 0) {
								for (Map<String, String> map : list) {
									PayPlanDetailVO vo = new PayPlanDetailVO();
									UFDouble msumapply = UFDouble.ZERO_DBL;// 累计已请款金额
									UFDouble msumpayed = UFDouble.ZERO_DBL;// 累计已付款金额
									primaryKey = map.get("pk");
									d_payplan = map.get("d_payplan") == null ? null
											: new UFDate(map.get("d_payplan"));
									pay_condition = map.get("pay_condition");
									planrate = new UFDouble(map.get("planrate") == null ? ""
											: String.valueOf(map.get("planrate")));
									planmoney = new UFDouble(map.get("planmoney") == null ? ""
											: String.valueOf(map.get("planmoney")));//计划付款金额
									msumapply = new UFDouble(map.get("msumapply") == null ? ""
											: String.valueOf(map.get("msumapply")));
									UFDouble leftplanmoney = UFDouble.ZERO_DBL;//剩余计划付款金额
									if(planmoney.compareTo(msumapply) >0 ){//计划付款金额与累计付款金额对比
										leftplanmoney = planmoney.sub(msumapply);
										if(leftplanmoney.compareTo(historyMny) >= 0){
											msumapply = msumapply.add(historyMny);
											leftplanmoney = leftplanmoney.sub(historyMny);
											historyMny = UFDouble.ZERO_DBL;
										}else{
											historyMny = historyMny.sub(leftplanmoney);
											msumapply = msumapply.add(leftplanmoney);
											leftplanmoney = UFDouble.ZERO_DBL;
										}
									}else{
										throw new BusinessException("付款合同台账中计划金额与累计请款金额数据异常，请检查！");
									}
									for(Object amount : applyedlist){
										msumpayed = msumpayed.add(new UFDouble(amount == null ? "" : String.valueOf(amount)));
									}
									if(leftplanmoney.compareTo(total) >0 && !flag){
										vo.setZyx1(total.toString());
										flag = true;
									}
									vo.setPk_jkbx(pk_jkbx);
									vo.setZyx2(primaryKey);
									vo.setFundtype(payStyle);
									vo.setZyx3(payStyle);
									vo.setPayingdate(d_payplan);
									vo.setPayrate(planrate);
									vo.setPaymoney(planmoney);
									vo.setPaycondit(pay_condition);
									vo.setTotalapplymoney(msumapply);
									vo.setTotalpaymoney(msumpayed);
									
									volist.add(vo);
								}
								bxsql = "update bd_defdoc set def22 = 'Y' where code = '"+vbillcode+"' "
										+ "and pk_defdoclist = (select pk_defdoclist from bd_defdoclist where code = 'zdy013' and name = '合同')";
								getBaseDAO().executeUpdate(bxsql);
								getBaseDAO().insertVOList(volist);
							}
						}
					}
				}
				i++;
				if(i==10)
					break;
			}
		
		}
		return null;
	}
}
