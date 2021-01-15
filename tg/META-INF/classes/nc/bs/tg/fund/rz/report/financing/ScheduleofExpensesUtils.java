package nc.bs.tg.fund.rz.report.financing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.DAOException;
import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.financing.ScheduleofExpensesTempVO;
import nc.vo.tgfn.report.financing.ScheduleofExpensesVO;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.uap.pf.PFBusinessException;
import uap.distribution.util.StringUtil;

import com.ufida.dataset.IContext;

/**
 * 费用科目明细表
 * 
 * @param ljf
 * @return
 * @throws BusinessException
 */
public class ScheduleofExpensesUtils extends ReportUtils {

	static ScheduleofExpensesUtils utils;

	public static ScheduleofExpensesUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new ScheduleofExpensesUtils();
		}
		return utils;
	}

	public ScheduleofExpensesUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new ScheduleofExpensesVO()));
		initTempTable();
	}
	
	private void initTempTable() throws PFBusinessException {
		StringBuffer columnSQL = new StringBuffer();
		columnSQL.append("pk_schedule char(20),")
				.append("billskey char(20),")// 单据PK
				.append("billtype varchar(50), ")// 单据类型
				.append("dr number(10), ")//
				.append("ts char(19) ");

		SQLUtil.createTempTable("temp_tg_schedule", columnSQL.toString(), null);
	}

	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);
			List<ScheduleofExpensesVO> list = new ArrayList<ScheduleofExpensesVO>();
			List<ScheduleofExpensesTempVO> li = new ArrayList<ScheduleofExpensesTempVO>();
			// 获取凭证信息
			List<ScheduleofExpensesVO> glList = getScheduleofGL();
			Set<String> glSet = new HashSet<String>();// 凭证主键信息
			if (glList != null && glList.size() > 0) {
				for (ScheduleofExpensesVO vo : glList) {
					glSet.add(vo.getBillskey());
				}
			}
			if(glSet != null && glSet.size() > 0){
				List<Map<String, String>> glrelationidList = getRelationid(glSet);// 凭证主键、关联单据PK和交易类型
				Map<String,String> pkmap = new HashMap<String,String>(glrelationidList.size());
				Map<String,String> billtypemap = new HashMap<String,String>(glrelationidList.size());
				Map<String,String> systemmap = new HashMap<String,String>(glrelationidList.size());//用于存在凭证PK和来源系统名称
				for (Map<String, String> map : glrelationidList) {
					ScheduleofExpensesTempVO expensesvo = new ScheduleofExpensesTempVO();
					String src_billtype = map.get("src_billtype");//来源单据类型
					String billskey = map.get("src_relationid")== null ? "" : map.get("src_relationid").split("_")[0];//业务单据PK
					String des_relationid = map.get("des_relationid");//凭证PK
					String systypename = map.get("systypename");//来源系统名称
					if (!StringUtil.isBlank(src_billtype)) {
						if (src_billtype.contains("264")) {
							String pk = billskey.split("_")[0];
							expensesvo.setBillskey(pk);
							expensesvo.setBilltype("264X");
							li.add(expensesvo);
						} else if (src_billtype.contains("F1")) {
							expensesvo.setBillskey(billskey);
							expensesvo.setBilltype("F1");
							li.add(expensesvo);
						}
					}
					pkmap.put(billskey, des_relationid);
					billtypemap.put(des_relationid, src_billtype);
					systemmap.put(des_relationid, systypename);
				}
				getBaseDAO().insertVOList(li);
				// 报销单信息map
				Map<String, ScheduleofExpensesVO> bxzbMap = null;
				// 应付单信息map
				Map<String, ScheduleofExpensesVO> payableMap = null;
				
				bxzbMap = getScheduleofBx(pkmap);
				payableMap = getScheduleofPayreq(pkmap);
				
				if (glList != null && glList.size() > 0) {
					for (ScheduleofExpensesVO vo : glList) {
						String pk = vo.getBillskey();
						vo.setSrcsystem(systemmap.get(pk));
						if (pk != null && billtypemap.get(pk) != null
								&& billtypemap.get(pk).contains("264")) {
							for (String key : bxzbMap.keySet()) {
								if (vo.getBillskey().contains(key)) {
									ScheduleofExpensesVO newbxvo = setBillList(vo,
											bxzbMap.get(key));
									list.add(newbxvo);
									break;
								}
							}
						} else if (pk != null && billtypemap.get(pk) != null
								&& billtypemap.get(pk).contains("F1")) {
							for (String key : payableMap.keySet()) {
								if (vo.getBillskey().contains(key)) {
									ScheduleofExpensesVO newpayvo = setBillList(vo,
											payableMap.get(key));
									list.add(newpayvo);
									break;
								} else {
									vo.setBilltype("");
									list.add(vo);
									break;
								}
							}
						} else if (vo.getBilltype() == null) {
							vo.setSrcsystem("总账");
							list.add(vo);
						}
					}
				}
			}

			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

	/**
	 * 查询费用科目明细报销单数据方法
	 * 
	 * @return
	 * @throws DAOException
	 */
	private List<ScheduleofExpensesVO> getScheduleofGL() throws DAOException {

		StringBuffer sql = getSQl();
		@SuppressWarnings("unchecked")
		List<ScheduleofExpensesVO> list = (List<ScheduleofExpensesVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(ScheduleofExpensesVO.class));

		return list;
	}

	/**
	 * 查询费用科目明细报销单数据方法
	 * @param pkmap 
	 * 
	 * @param keySet
	 * 
	 * @return
	 * @throws DAOException
	 */
	private Map<String, ScheduleofExpensesVO> getScheduleofBx(Map<String, String> pkmap) throws DAOException {
		StringBuffer sql = getBxSQl();
		@SuppressWarnings("unchecked")
		List<ScheduleofExpensesVO> list = (List<ScheduleofExpensesVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(ScheduleofExpensesVO.class));
		Map<String, ScheduleofExpensesVO> tempMap = dealDuplicateData(list, pkmap);
		return tempMap;

	}

	/**
	 * 查询费用科目明细付款申请单数据方法
	 * 
	 * @param keySet
	 * @param pkmap 
	 * 
	 * @return
	 * @throws DAOException
	 */
	private Map<String, ScheduleofExpensesVO> getScheduleofPayreq(Map<String, String> pkmap) throws DAOException {
		StringBuffer sql = getPayreqSQl();
		@SuppressWarnings("unchecked")
		List<ScheduleofExpensesVO> list = (List<ScheduleofExpensesVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(ScheduleofExpensesVO.class));
		Map<String, ScheduleofExpensesVO> tempMap = dealDuplicateData(list, pkmap);
		return tempMap;
	}

	/**
	 * 获取到凭证的数据
	 * 
	 * @return
	 */
	public StringBuffer getSQl() {
		StringBuffer sql = new StringBuffer();
		String cdate = dealDate(queryWhereMap.get("cdate"),
				"to_char(to_date(v4.prepareddatev,'yyyy-MM-dd hh24:mi:ss'),'yyyy-MM')");
		// sql.append("select  v2.src_billtype as billtype")
		// .append(",v2.src_relationid as billskey")
		sql.append("select  v3.pk_voucher as billskey")
				.append(",v7.name as account")// --出账公司
				.append(",v5.user_name as makingpeople")
				.append(",v6.dispname as expenseaccount")
				.append(",v4.explanation as abstraction")
				.append(",v4.localdebitamount as incurredamount")
				.append(",case when v4.prepareddatev is null then null else substr(v4.prepareddatev,0,10) end as voucherdate")
				.append(",'记-' || lpad(v3.num,4,0) as vertificatenumber")
				.append(" from gl_detail v4")
				// .append(" left join fip_relation v2 on v2.des_relationid = v4.pk_voucher and v2.dr = 0")
				.append(" inner join gl_voucher v3 on v3.pk_voucher = v4.pk_voucher and v3.dr =0 ")
				.append(" inner join sm_user v5 on v5.cuserid = v3.creator  and v5.dr =0 ")
				.append(" inner join bd_accasoa v6 on v6.pk_accasoa = v4.pk_accasoa  and v6.dr =0 ")
				.append(" inner join org_orgs v7 on v7.pk_org = v3.pk_org  and v7.dr =0 ");
		sql.append(" where  1=1 and substr(v4.accountcode, 0, 4) between '6401' and '6801' and v4.localdebitamount <> 0");
		sql.append(" and v4.discardflagv <> 'Y' and v4.voucherkindv <> 255  and nvl(v4.errmessage2, '~') = '~' and v4.tempsaveflag <> 'Y' and v4.voucherkindv <> 5 ");
		// .append("and (substr(v2.src_billtype, 0, 4) = '264X' OR v2.src_billtype in('F1-Cxx-004','F1-Cxx-007'))");
		if (!StringUtil.isBlank(cdate)) {
			sql.append(cdate);
		}
		if (queryValueMap.get("expenseaccount") != null) {// 费用会计科目
			String[] expenseaccounts = queryValueMap.get("expenseaccount")
					.split(",");
			sql.append("and (");
			for (int i$ = 0; i$ < expenseaccounts.length; i$++) {
				if (i$ == 0) {
					sql.append(" v4.accountcode like '" + expenseaccounts[i$]
							+ "%' ");
				} else {
					sql.append(" or v4.accountcode like '"
							+ expenseaccounts[i$] + "%' ");

				}
			}

			sql.append(")");
		}
		if (queryValueMap.get("pk_org") != null) {
			String[] orgs = queryValueMap.get("pk_org").split(",");
			sql.append(" and " + SQLUtil.buildSqlForIn("v3.pk_org", orgs));
		}

		return sql;
	}

	/**
	 * 获取报销单主线sql
	 * 
	 * @param keySet
	 * 
	 * @return
	 */
	private StringBuffer getBxSQl() {
		StringBuffer sql = new StringBuffer();
		sql.append("select e.pk_jkbx as billskey,").append("\r\n");
		sql.append("   e.djbh as billno,").append("\r\n"); // --单据编号
		sql.append(
				"   regexp_replace(listagg(d12.objcode ,',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3') as code,")
				.append("\r\n"); // --预算科目编码
		sql.append(
				"   regexp_replace(listagg(d12.objname ,',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3') as subject,")
				.append("\r\n"); // --预算科目
		sql.append("   o.name as units,").append("\r\n");// --预算归属单位
		sql.append("   dept.name as department,").append("\r\n");// --预算归属部门
		sql.append("   o1.name as account,").append("\r\n");// --出账公司
		sql.append("   currtype.name as currency,").append("\r\n");// --币种
		sql.append("   e.total as appliedamount,").append("\r\n");// --申请金额
		sql.append(
				"   regexp_replace(listagg(psn.name,',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3')as receiver,")
				.append("\r\n");// --收款人
		sql.append(
				"   regexp_replace(listagg(psn1.name,',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3') as reimbursement,")
				.append("\r\n");// --报销人
		sql.append(
				"   regexp_replace(listagg(case e.hbbm when '~' then su1.name else su.name end ,',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3') as supplier,")
				.append("\r\n");// --供应商
		sql.append("   pr.project_name as project,").append("\r\n");// --项目
		sql.append(
				"   regexp_replace(listagg(def1.name ,',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3') as formats,")
				.append("\r\n");// --业态
		sql.append(
				"   regexp_replace(listagg(de.name,',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3') as floor,")
				.append("\r\n");// --部门楼层
		sql.append(
				"   regexp_replace(listagg(de1.name,',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3') as platenumber,")
				.append("\r\n");// --车牌部门
		sql.append("   billtype.billtypename as billtype,").append("\r\n");// --单据类型
		sql.append(
				"   case when e.djzt=-1 then '作废' when e.djzt=0 then '暂存' when e.djzt=1 then '保存' when e.djzt=2 then '审核过' else '签字' end as billsstate,")
				.append("\r\n");// --单据状态
		sql.append(
				"   case when e.djrq is null then null else substr(e.djrq,0,10) end as billsdate,")
				.append("\r\n");// --单据日期
		sql.append(
				"   case when e.shrq is null then null else substr(e.shrq,0,10) end  as approvaldate,")
				.append("\r\n");// --审批日期
		sql.append(
				"   case when sett.settledate is null then null else substr(sett.settledate,0,10) end as settlementdate,")
				.append("\r\n");// --结算日期
		sql.append("   sm.user_name as applicant,").append("\r\n");// --申请人
		sql.append(
				"   regexp_replace(listagg(def.name,',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3') as invoicetype,")
				.append("\r\n");// --发票类型
		sql.append(
				"   regexp_replace(listagg(b.defitem18, ',') within GROUP(order by e.djbh),'([^,]+)(,\\1)*(,|$)','\\1\\3') as invoiceon,")
				.append("\r\n");// --发票号
		sql.append(
				"   sum(to_number(case when b.defitem23 <>'~' then b.defitem23 else '0' end )) as invoiceamount,")
				.append("\r\n");// --发票金额
		sql.append(
				"   sum(to_number(case when b.defitem14 <>'~' then b.defitem14 else '0' end )) as taxamount,")
				.append("\r\n");// --税额
		sql.append(
				"   to_char(case when e.spzt=-1 then '自由' when e.spzt=3 then '提交' when e.spzt=2 then '审批进行中' when e.spzt=1 then '审批通过' else '审批未通过' end) as approvalstatus,")
				.append("\r\n");// --审批状态
		sql.append("   e.zy as reason,").append("\r\n");// --事由
		sql.append("   e.zyx2 as contractcode,").append("\r\n");// --合同编码
		sql.append("   con.name as contractname,").append("\r\n");// --合同名称
		sql.append("   '' as documentone,").append("\r\n");// --预提单据号 报销单
															// 有冲预提则显示
		sql.append("   '' as ebsappnum").append("\r\n");// --EBS申请号 付款申请单
		sql.append("from er_bxzb e").append("\r\n");
		sql.append(
				"left join er_busitem b on b.pk_jkbx = e.pk_jkbx and nvl(b.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join org_orgs o on o.pk_org = e.fydwbm and nvl(o.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join org_orgs o1 on o1.pk_org = e.pk_org and nvl(o1.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join bd_currtype currtype on currtype.pk_currtype = e.bzbm and nvl(currtype.dr,0)=0")
				.append("\r\n");// --币种
		sql.append(
				"left join bd_psndoc psn  on psn.pk_psndoc = b.receiver and nvl(psn.dr,0)=0 ")
				.append("\r\n");// --收款人
		sql.append(
				"left join bd_psndoc psn1 on psn1.pk_psndoc = b.jkbxr and nvl(psn1.dr,0)=0")
				.append("\r\n");// --报销人
		sql.append(
				"left join bd_supplier su on su.pk_supplier = e.hbbm and nvl(su.dr,0)=0")
				.append("\r\n");// --供应商
		sql.append(
				"left join bd_supplier su1 on su1.pk_supplier = b.defitem20  and nvl(su1.dr,0)=0   ")
				.append("\r\n");// --供应商
		sql.append(
				"left join bd_project pr on pr.pk_project = e.jobid and nvl(pr.dr,0)=0 ")
				.append("\r\n");// --项目
		sql.append(
				"left join bd_defdoc de on de.pk_defdoc = b.defitem24 and nvl(pr.dr,0)=0 ")
				.append("\r\n");// --部门楼层
		sql.append(
				"left join bd_defdoc de1 on de1.pk_defdoc = b.defitem26 and nvl(de1.dr,0)=0")
				.append("\r\n");// --车辆部门
		sql.append(
				"left join org_dept dept on dept.pk_dept = e.fydeptid and nvl(dept.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join tb_budgetsub d12 on d12.pk_obj= b.defitem12 and nvl(d12.dr,0)=0")
				.append("\r\n");// --预算科目
		sql.append(
				"left join bd_defdoc def on def.pk_defdoc = b.defitem15 and nvl(def.dr,0)=0")
				.append("\r\n");// --发票类型
		sql.append(
				"left join bd_defdoc def1 on def1.pk_defdoc = b.defitem22 and nvl(def1.dr,0)=0")
				.append("\r\n");// --业态
		sql.append(
				"left join sm_user sm on sm.cuserid = e.operator  and nvl(sm.dr,0)=0 ")
				.append("\r\n");
		sql.append(
				"left join yer_contractfile con on con.pk_defdoc = e.zyx9 and nvl(con.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join bd_billtype billtype on billtype.pk_billtypeid = e.pk_tradetypeid and nvl(billtype.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join cmp_settlement sett on sett.pk_busibill = e.pk_jkbx and nvl(sett.dr,0)=0")
				.append("\r\n");
		sql.append("where nvl(e.dr,0)=0   ").append("\r\n");
		// sql.append("and e.djbh = '264X20200601000331'").append("\r\n");
		if (queryValueMap.get("pk_org") != null) {
			String[] orgs = queryValueMap.get("pk_org").split(",");
			sql.append(" and " + SQLUtil.buildSqlForIn("e.pk_org", orgs));
		}
		if (queryValueMap.get("fydwbm") != null) {
			String[] depts = queryValueMap.get("fydwbm").split(",");
			sql.append(" and " + SQLUtil.buildSqlForIn("e.fydwbm", depts));
		}
		sql.append("and exists (select 1 from temp_tg_schedule n where n.billskey = e.pk_jkbx and n.billtype = '264X')");
//		if (bxkeySet != null && bxkeySet.size() > 0) {
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn("e.pk_jkbx",
//							bxkeySet.toArray(new String[0])));
//		}
		// if (!StringUtil.isBlank(cdate)) {
		// sql.append(cdate);
		// }
		sql.append("group by e.pk_jkbx,e.djbh,o.name,dept.name,")
				.append("\r\n");
		sql.append("o1.name,currtype.name,e.total,").append("\r\n");
		sql.append("e.pk_billtype,e.djzt,e.djrq,e.shrq,e.jsrq,").append("\r\n");
		sql.append("sm.user_name,e.spzt,e.zy,e.zyx2,").append("\r\n");
		sql.append(
				"con.name,su1.name,su.name,pr.project_name,billtype.billtypename,sett.settledate")
				.append("\r\n");

		return sql;
	}

	/**
	 * 获取付款申请单主线sql
	 * 
	 * @param keySet
	 * 
	 * @return
	 */
	private StringBuffer getPayreqSQl() {
		StringBuffer sql = new StringBuffer();
		sql.append("select v1.pk_payablebill as billskey,").append("\r\n");
		sql.append("       v1.billno as billno,").append("\r\n");// --单据编号
		sql.append(
				"       regexp_replace(listagg(v3.objcode,',') within GROUP(order by v1.billno),'([^,]+)(,\\1)*(,|$)','\\1\\3') as code,")
				.append("\r\n");// --预算科目编码
		sql.append(
				"       regexp_replace(listagg(v3.objname,',') within GROUP(order by v1.billno),'([^,]+)(,\\1)*(,|$)','\\1\\3')subject,")
				.append("\r\n");// --预算科目
		sql.append("       v4.name units,").append("\r\n");// --预算归属单位
		sql.append("       v1.def60 department,").append("\r\n");// --预算归属部门
		sql.append("       v4.name account,").append("\r\n");// --出账公司
		sql.append("       v5.name currency,").append("\r\n");// --币种
		sql.append("       v1.money appliedamount,").append("\r\n");// --申请金额
		sql.append("       v6.name receiver,").append("\r\n");// --收款人
		sql.append("       v1.def59 reimbursement,").append("\r\n");// --报销人
		sql.append(
				"       regexp_replace(listagg(v7.name, ',') within GROUP(order by v1.billno),'([^,]+)(,\\1)*(,|$)','\\1\\3') supplier,")
				.append("\r\n");// --供应商
		sql.append("       v1.def12 project,").append("\r\n");// --项目
		sql.append("   	   '' as formats,").append("\r\n");// --业态
		sql.append("   	   '' as floor,").append("\r\n");// --部门楼层
		sql.append("   	   '' as platenumber,").append("\r\n");// --车牌部门
		sql.append("       v8.billtypename billtype,").append("\r\n");// --单据类型
		sql.append(
				"       case when v1.billstatus = -1 then '保存' when v1.billstatus =-99 then '暂存' ")
				.append("\r\n");
		sql.append(
				"         when v1.billstatus = 9 then '未确认' when v1.billstatus = 1 then '审批通过'")
				.append("\r\n");
		sql.append(
				"           when v1.billstatus = 2 then '审批中' when v1.billstatus = 8 then '签字' else '' end as billsstate,")
				.append("\r\n");// --单据状态
		sql.append(
				"       case when v1.billdate is null then null else substr(v1.billdate, 0, 10) end billsdate,")
				.append("\r\n");// --单据日期
		sql.append(
				"       case when v1.approvedate is null then null else substr(v1.approvedate, 0, 10) end approvaldate,")
				.append("\r\n");// --审批日期
		sql.append("   '' as settlementdate,").append("\r\n");// --结算日期
		sql.append("       v1.def59 applicant,").append("\r\n");// --申请人
		sql.append(
				"       regexp_replace(listagg(v9.name, ',') within GROUP(order by v1.billno),'([^,]+)(,\\1)*(,|$)','\\1\\3') invoicetype,")
				.append("\r\n");// --发票类型
		sql.append(
				"       regexp_replace(listagg(v10.fph, ',') within GROUP(order by v1.billno),'([^,]+)(,\\1)*(,|$)','\\1\\3') invoiceon,")
				.append("\r\n");// --发票号
		sql.append("       sum(v2.local_money_cr) invoiceamount,").append(
				"\r\n");// --发票金额
		sql.append("       sum(v2.local_tax_cr) taxamount,").append("\r\n");// --税额
		sql.append(
				"       case when v1.approvestatus = -1 then '自由态' when v1.approvestatus =-0 then '未通过'")
				.append("\r\n");//
		sql.append(
				"         when v1.approvestatus = 1 then '通过态' when v1.approvestatus = 2 then '进行中态' ")
				.append("\r\n");//
		sql.append(
				"           when v1.approvestatus = 3 then '提交态' else '~' end approvalstatus,")
				.append("\r\n");// --审批状态
		sql.append("       v1.def31 reason,").append("\r\n");// --事由
		sql.append("       v1.def5 contractcode,").append("\r\n");// --合同编码
		sql.append("       v1.def6 contractname,").append("\r\n");// --合同名称
		sql.append("       '' documentone,").append("\r\n");// --预提单据号
		sql.append("       v1.def2 ebsappnum").append("\r\n");// --EBS申请号 付款申请单
		// sql.append("       v1.def2 def1").append("\r\n");// --外系统主键
		sql.append("from ap_payablebill v1 ").append("\r\n");
		sql.append(
				"left join ap_payableitem v2 on v1.pk_payablebill = v2.pk_payablebill and nvl(v2.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join tb_budgetsub v3 on v3.pk_obj = v2.def1 and nvl(v3.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join org_orgs v4 on v4.pk_org = v1.pk_org and nvl(v4.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join bd_currtype v5 on v5.pk_currtype = v1.pk_currtype and nvl(v5.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join bd_supplier v6 on v6.pk_supplier = v1.def18 and nvl(v6.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join bd_supplier v7 on v7.pk_supplier = v2.supplier and nvl(v7.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join bd_billtype v8 on v8.pk_billtypeid = v1.pk_tradetypeid and nvl(v8.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join bd_defdoc v9 on v9.pk_defdoc = v2.def8 and nvl(v9.dr,0)=0")
				.append("\r\n");
		sql.append(
				"left join hzvat_invoice_h v10 on v10.def4 = v1.def2 and nvl(v10.dr,0)=0")
				.append("\r\n");
		sql.append(
				"where nvl(v1.dr,0)=0 and v1.pk_tradetype in('F1-Cxx-004','F1-Cxx-007')")
				.append("\r\n");
		if (queryValueMap.get("pk_org") != null) {
			String[] orgs = queryValueMap.get("pk_org").split(",");
			sql.append(" and " + SQLUtil.buildSqlForIn("v1.pk_org", orgs));
		}
//		if (keySet != null && keySet.size() > 0) {
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn("v1.pk_payablebill",
//							keySet.toArray(new String[0])));
//		}
		sql.append("and exists (select 1 from temp_tg_schedule n where n.billskey = v1.pk_payablebill and n.billtype = 'F1')");
		// if (!StringUtil.isBlank(cdate)) {
		// sql.append(cdate);
		// }
		sql.append(
				"group by v1.pk_payablebill,v1.billno,v4.name,v1.def60,v5.name,v1.money,v6.name,v1.def59,")
				.append("\r\n");
		sql.append(
				"v1.def12,v8.billtypename,v1.billstatus,v1.billdate,v1.approvedate,")
				.append("\r\n");
		sql.append("v1.approvestatus,v1.def31,v1.def5,v1.def6,v1.def2").append(
				"\r\n");
		return sql;
	}

	private List<Map<String, String>> getRelationid(Set<String> set)
			throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT v1.des_relationid,v1.src_relationid,v1.src_billtype,v2.systypename FROM fip_relation v1")
				.append("\r\n");
		sql.append(
				"inner join dap_dapsystem v2 on v2.systypecode = v1.src_system")
				.append("\r\n");
		if (set != null && set.size() > 0) {
			sql.append(" where"
					+ SQLUtil.buildSqlForIn("des_relationid",
							set.toArray(new String[0])));
		}
		@SuppressWarnings("unchecked")
		List<Map<String, String>> li = (List<Map<String, String>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		return li;
	}

	/**
	 * 设置业务单据值
	 * 
	 * @param glvo
	 *            凭证信息
	 * @param vo
	 *            单据信息
	 * @return
	 */
	private ScheduleofExpensesVO setBillList(ScheduleofExpensesVO glvo,
			ScheduleofExpensesVO vo) {
		ScheduleofExpensesVO coloneVO = (ScheduleofExpensesVO) vo.clone();
		coloneVO.setMakingpeople(glvo.getMakingpeople());
		coloneVO.setExpenseaccount(glvo.getExpenseaccount());
		coloneVO.setAbstraction(glvo.getAbstraction());
		coloneVO.setIncurredamount(glvo.getIncurredamount());
		coloneVO.setVoucherdate(glvo.getVoucherdate());
		coloneVO.setVertificatenumber(glvo.getVertificatenumber());
		coloneVO.setSrcsystem(glvo.getSrcsystem());
		return coloneVO;
	}

	public ScheduleofExpensesVO getMinVO(String startDate, String endDate,
			Map<String, Object> map) throws DAOException {
		ScheduleofExpensesVO vo = new ScheduleofExpensesVO();
		return vo;
	}

	private String dealDate(String date, String replaceValue) {
		String str = null;
		if (!StringUtil.isBlank(date)) {
			str = date.replaceAll("cdate", replaceValue);
		}
		return str;
	}
	/**
	 * 处理多个单据对应一个凭证的情况
	 * @param list
	 * @param pkmap
	 * @return
	 */
	private Map<String, ScheduleofExpensesVO> dealDuplicateData(List<ScheduleofExpensesVO> list,Map<String, String> pkmap){
		List<String> li = new ArrayList<String>(list.size());
		Map<String, ScheduleofExpensesVO> tempMap = new HashMap<String, ScheduleofExpensesVO>();
		if (list != null && list.size() > 0) {
			for (ScheduleofExpensesVO vo : list) {
				String pk_voucher = pkmap.get(vo.getBillskey());
				if(li != null && li.size()>0){
					if(li.contains(pk_voucher)){
						ScheduleofExpensesVO vos= tempMap.get(pk_voucher);
						vos = dealVouExistBills(vos, vo);
					}else{
						li.add(pk_voucher);
						tempMap.put(pk_voucher, vo);
					}
				}else{
					li.add(pk_voucher);
					tempMap.put(pk_voucher, vo);//用于存贮多单据一个凭证的数据
				}
			}
		}
		return tempMap;
	}
	/**
	 * 处理多张单据对应一个凭证的情况
	 * @param vos
	 * @param vo
	 */
	private ScheduleofExpensesVO dealVouExistBills(ScheduleofExpensesVO vos,ScheduleofExpensesVO vo){
		if(vos.getAccount() != null && vo.getAccount() != null && !vos.getAccount().contains(vo.getAccount())){
			String str = null;
			String string = vos.getAccount()+","+vo.getAccount();
			str = dealDuplicateString(string);
			vos.setAccount(str);
		}else if(vos.getAccount() == null && vo.getAccount() != null){
			vos.setAccount(vo.getAccount());
		}
		if(vos.getApplicant() != null && vo.getApplicant() != null && !vos.getApplicant().contains(vo.getApplicant())){
			String str = null;
			String string = vos.getApplicant()+","+vo.getApplicant();
			str = dealDuplicateString(string);
			vos.setApplicant(str);
		}else if(vos.getApplicant() == null && vo.getApplicant() != null){
			vos.setApplicant(vo.getApplicant());
		}
		if(vos.getApprovaldate() != null && vo.getApprovaldate() != null && !vos.getApprovaldate().contains(vo.getApprovaldate())){
			String str = null;
			String string = vos.getApprovaldate()+","+vo.getApprovaldate();
			str = dealDuplicateString(string);
			vos.setApprovaldate(str);
		}else if(vos.getApprovaldate() == null && vo.getApprovaldate() != null){
			vos.setApprovaldate(vo.getApprovaldate());
		}
		if(vos.getApprovalstatus() != null && vo.getApprovalstatus() != null && !vos.getApprovalstatus().contains(vo.getApprovalstatus())){
			String str = null;
			String string = vos.getApprovalstatus()+","+vo.getApprovalstatus();
			str = dealDuplicateString(string);
			vos.setApprovalstatus(str);
		}else if(vos.getApprovalstatus() == null && vo.getApprovalstatus() != null){
			vos.setApprovalstatus(vo.getApprovalstatus());
		}
		if(vos.getBillno() != null && vo.getBillno() != null && !vos.getBillno().contains(vo.getBillno())){
			String str = null;
			String string = vos.getBillno()+","+vo.getBillno();
			str = dealDuplicateString(string);
			vos.setBillno(str);
		}else if(vos.getBillno() == null && vo.getBillno() != null){
			vos.setBillno(vo.getBillno());
		}
		if(vos.getBillsdate() != null && vo.getBillsdate() != null && !vos.getBillsdate().contains(vo.getBillsdate())){
			String str = null;
			String string = vos.getBillsdate()+","+vo.getBillsdate();
			str = dealDuplicateString(string);
			vos.setBillsdate(str);
		}else if(vos.getBillsdate() == null && vo.getBillsdate() != null){
			vos.setBillsdate(vo.getBillsdate());
		}
		if(vos.getBillsstate() != null && vo.getBillsstate() != null && !vos.getBillsstate().contains(vo.getBillsstate())){
			String str = null;
			String string = vos.getBillsstate()+","+vo.getBillsstate();
			str = dealDuplicateString(string);
			vos.setBillsstate(str);
		}else if(vos.getBillsstate() == null && vo.getBillsstate() != null){
			vos.setBillsstate(vo.getBillsstate());
		}
		if(vos.getBilltype() != null && vo.getBilltype() != null && !vos.getBilltype().contains(vo.getBilltype())){
			String str = null;
			String string = vos.getBilltype()+","+vo.getBilltype();
			str = dealDuplicateString(string);
			vos.setBilltype(str);
		}else if(vos.getBilltype() == null && vo.getBilltype() != null){
			vos.setBilltype(vo.getBilltype());
		}
		if(vos.getCode() != null && vo.getCode() != null && !vos.getCode().contains(vo.getCode())){
			String str = null;
			String string = vos.getCode()+","+vo.getCode();
			str = dealDuplicateString(string);
			vos.setCode(str);
		}else if(vos.getCode() == null && vo.getCode() != null){
			vos.setCode(vo.getCode());
		}
		if(vos.getContractcode() != null && vo.getContractcode() != null && !vos.getContractcode().contains(vo.getContractcode())){
			String str = null;
			String string = vos.getContractcode()+","+vo.getContractcode();
			str = dealDuplicateString(string);
			vos.setContractcode(str);
		}else if(vos.getContractcode() == null && vo.getContractcode() != null){
			vos.setContractcode(vo.getContractcode());
		}
		if(vos.getContractname() != null && vo.getContractname() != null && !vos.getContractname().contains(vo.getContractname())){
			String str = null;
			String string = vos.getContractname()+","+vo.getContractname();
			str = dealDuplicateString(string);
			vos.setContractname(str);
		}else if(vos.getContractname() == null && vo.getContractname() != null){
			vos.setContractname(vo.getContractname());
		}
		if(vos.getCurrency() != null && vo.getCurrency()!= null && !vos.getCurrency().contains(vo.getCurrency())){
			String str = null;
			String string = vos.getCurrency()+","+vo.getCurrency();
			str = dealDuplicateString(string);
			vos.setCurrency(str);
		}else if(vos.getCurrency() == null && vo.getCurrency()!= null){
			vos.setCurrency(vo.getCurrency());
		}
		if(vos.getDepartment() != null && vo.getDepartment() != null && !vos.getDepartment().contains(vo.getDepartment())){
			String department = null;
			String string = vos.getDepartment()+","+vo.getDepartment();
			department = dealDuplicateString(string);
			vos.setDepartment(department);
		}else if(vos.getDepartment() == null && vo.getDepartment() != null){
			vos.setDepartment(vo.getDepartment());
		}
		if(vos.getDocumentone() != null && vo.getDocumentone() != null && !vos.getDocumentone().contains(vos.getDocumentone())){
			String str = null;
			String string = vos.getDocumentone()+","+vo.getDocumentone();
			str = dealDuplicateString(string);
			vos.setDocumentone(str);
		}else if(vos.getDocumentone() != null && vo.getDocumentone() != null){
			vos.setDocumentone(vo.getDocumentone());
		}
		if(vos.getEbsappnum() != null && vo.getEbsappnum() != null && !vos.getEbsappnum().contains(vo.getEbsappnum())){
			String str = null;
			String string = vos.getEbsappnum()+","+vo.getEbsappnum();
			str = dealDuplicateString(string);
			vos.setEbsappnum(str);
		}else if(vos.getEbsappnum() == null && vo.getEbsappnum() != null){
			vos.setEbsappnum(vo.getEbsappnum());
		}
		if(vos.getFloor() != null && vo.getFloor() != null && !vos.getFloor().contains(vo.getFloor())){
			String str = null;
			String string = vos.getFloor()+","+vo.getFloor();
			str = dealDuplicateString(string);
			vos.setFloor(str);
		}else if(vos.getFloor() == null && vo.getFloor() != null){
			vos.setFloor(vo.getFloor());
		}
		if(vos.getFormats() != null && vo.getFormats() != null && !vos.getFormats().contains(vo.getFormats())){
			String str = null;
			String string = vos.getFormats()+","+vo.getFormats();
			str = dealDuplicateString(string);
			vos.setFormats(str);
		}else if(vos.getFormats() == null && vo.getFormats() != null){
			vos.setFormats(vo.getFormats());
		}
		vos.setAppliedamount(vos.getAppliedamount().add(vo.getAppliedamount()));
		vos.setInvoiceamount(vos.getInvoiceamount().add(vo.getInvoiceamount()));
		if(vos.getInvoiceon() != null && vo.getInvoiceon() != null && !vos.getInvoiceon().contains(vo.getInvoiceon())){
			String str = null;
			String string = vos.getInvoiceon()+","+vo.getInvoiceon();
			str = dealDuplicateString(string);
			vos.setInvoiceon(str);
		}else if(vos.getInvoiceon() == null && vo.getInvoiceon() != null){
			vos.setInvoiceon(vo.getInvoiceon());
		}
		if(vos.getInvoicetype() != null && vo.getInvoicetype() != null && !vos.getInvoicetype().contains(vo.getInvoicetype())){
			String str = null;
			String string = vos.getInvoicetype()+","+vo.getInvoicetype();
			str = dealDuplicateString(string);
			vos.setInvoicetype(str);
		}else if(vos.getInvoicetype() == null && vo.getInvoicetype() != null){
			vos.setInvoicetype(vo.getInvoicetype());
		}
		if(vos.getPlatenumber() != null && vo.getPlatenumber() != null && !vos.getPlatenumber().contains(vo.getPlatenumber())){
			String str = null;
			String string = vos.getPlatenumber()+","+vo.getPlatenumber();
			str = dealDuplicateString(string);
			vos.setPlatenumber(str);
		}else if(vos.getPlatenumber() == null && vo.getPlatenumber() != null){
			vos.setPlatenumber(vo.getPlatenumber());
		}
		if(vos.getProject() != null && vo.getProject() != null && !vos.getProject().contains(vo.getProject())){
			String str = null;
			String string = vos.getProject()+","+vo.getProject();
			str = dealDuplicateString(string);
			vos.setProject(str);
		}else if(vos.getProject() == null && vo.getProject() != null){
			vos.setProject(vo.getProject());
		}
		if(vos.getReason() != null && vo.getReason() != null && !vos.getReason().contains(vo.getReason())){
			String str = null;
			String string = vos.getReason()+","+vo.getReason();
			str = dealDuplicateString(string);
			vos.setReason(str);
		}else if(vos.getReason() == null && vo.getReason() != null){
			vos.setReason(vo.getReason());
		}
		if(vos.getReceiver() != null && vo.getReceiver() != null && !vos.getReceiver().contains(vos.getReceiver())){
			String str = null;
			String string = vos.getReceiver()+","+vo.getReceiver();
			str = dealDuplicateString(string);
			vos.setReceiver(str);
		}else if(vos.getReceiver() == null && vo.getReceiver() != null){
			vos.setReceiver(vo.getReceiver());
		}
		if(vos.getReimbursement() != null && vo.getReimbursement() != null && !vos.getReimbursement().contains(vo.getReimbursement())){
			String str = null;
			String string = vos.getReimbursement()+","+vo.getReimbursement();
			str = dealDuplicateString(string);
			vos.setReimbursement(str);
		}else if(vo.getReimbursement() == null && vo.getReimbursement() != null){
			vos.setReimbursement(vo.getReimbursement());
		}
		if(vos.getSettlementdate() != null && vo.getSettlementdate() != null&& !vos.getSettlementdate().contains(vo.getSettlementdate())){
			String str = null;
			String string = vos.getSettlementdate()+","+vo.getSettlementdate();
			str = dealDuplicateString(string);
			vos.setSettlementdate(str);
		}else if(vo.getSettlementdate() == null && vo.getSettlementdate() !=null){
			vos.setSettlementdate(vo.getSettlementdate());
		}
		if(vos.getSubject() != null && vo.getSubject() != null && !vos.getSubject().contains(vo.getSubject())){
			String str = null;
			String string = vos.getSubject()+","+vo.getSubject();
			str = dealDuplicateString(string);
			vos.setSubject(str);
		}else if(vos.getSubject() == null && vo.getSubject() != null){
			vos.setSubject(vo.getSubject());
		}
		if(vos.getSupplier() != null && vo.getSupplier() != null && !vos.getSupplier().contains(vo.getSupplier())){
			String str = null;
			String string = vos.getSupplier()+","+vo.getSupplier();
			str = dealDuplicateString(string);
			vos.setSupplier(str);
		}else if(vos.getSupplier() == null && vo.getSupplier() != null){
			vos.setSupplier(vo.getSupplier());
		}
		if(vos.getUnits() != null && vo.getUnits()!= null && !vos.getUnits().contains(vo.getUnits())){
			String str = null;
			String string = vos.getUnits()+","+vo.getUnits();
			str = dealDuplicateString(string);
			vos.setUnits(str);
		}else if(vos.getUnits() == null && vo.getUnits()!= null){
			vos.setUnits(vo.getUnits());
		}
		return vos;
	}
	private String dealDuplicateString(String string){
		String department = null;
		String[] str = string.split(",");
		List<String> list = new ArrayList<String>(str.length);
		for(String s : str){
			if(!list.contains(s)){
				list.add(s);
			}
		}
		for(String s : list){
			if(department != null){
				department = department+","+s;
			}else{
				department = s;
			}
		}
		return department;
	}
}
