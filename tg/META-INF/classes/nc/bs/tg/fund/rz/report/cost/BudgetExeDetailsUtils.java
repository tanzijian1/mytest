package nc.bs.tg.fund.rz.report.cost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import nc.vo.tgfn.report.cost.BudgetExeDetailsVO;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.tgfp.report.ReportOrgVO;

import com.ufida.dataset.IContext;

public class BudgetExeDetailsUtils extends ReportUtils {

	static BudgetExeDetailsUtils utils;

	public static BudgetExeDetailsUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new BudgetExeDetailsUtils();
		}
		return utils;
	}

	public BudgetExeDetailsUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new BudgetExeDetailsVO()));
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
			List<BudgetExeDetailsVO> list = getBudgetExeDetailsListVOs();

			if (list != null) {
				if (queryValueMap.get("dimension") != null) {
					// �����������ɸѡ
					List<BudgetExeDetailsVO> finalcoll = new ArrayList<BudgetExeDetailsVO>();
					// ��ʼ�����Ϊ0
					int sequeNumGlobal = 0;
					for (BudgetExeDetailsVO budgetExeDetailsVO : list) {
						// ���area��ֵ,���϶�Ϊ����ʾ��ѯ����
						// ÿ����һ��Ԫ�أ���ż�1
						if (budgetExeDetailsVO.getDimension() != null) {
							++sequeNumGlobal;
							budgetExeDetailsVO.setSortnumglobal(new UFDouble(
									sequeNumGlobal));
							finalcoll.add(budgetExeDetailsVO);
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

	@SuppressWarnings("unchecked")
	private List<BudgetExeDetailsVO> getBudgetExeDetailsListVOs()
			throws BusinessException {
		// ��ȡ���й�˾��μ���VO
		List<ReportOrgVO> orgVOs = ReportConts.getUtils().getOrgVOs();
		// ���pk_org��pk_region�Ķ�Ӧ��ϵ
		Map<String, String> regionMap = new HashMap<String, String>();
		Map<String, ArrayList<String>> orgsMap = new HashMap<String, ArrayList<String>>();
		for (ReportOrgVO reportOrgVO : orgVOs) {
			String pk_org = reportOrgVO.getPk_org();
			String pk_region = reportOrgVO.getPk_region();
			// ���pk_org����pk_region
			regionMap.put(pk_org, pk_region);
			/**
			 * ��orgsMap�л�ȡpk_region��Ӧ��list���ϣ��������Ϊnull��
			 * orgsMapҪ�洢�Ե�ǰpk_regionΪkey������˵�ǰpk_org��listΪvalue���¼�ֵ��
			 * �����Ϊnull��˵����ǰorgsMap���Ѵ���pk_regionΪkey�ļ�ֵ�ԣ�ֻ��Ҫ��list �д��뵱ǰpk_org����
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
		// �������pk_region������
		String[] pk_regionArr = null;
		// �������pk_region��Ӧ��name
		Map<String, String> nameMap = new HashMap<String, String>();

		ArrayList<String> arrpk_orgs = null;
		if (queryValueMap.get("dimension") != null) {
			pk_regionArr = queryValueMap.get("dimension").split(",");
			arrpk_orgs = new ArrayList<String>();
			for (String string : pk_regionArr) {
				arrpk_orgs.add(string);
				ArrayList<String> arrpk_org = orgsMap.get(string);
				if (arrpk_org != null) {
					arrpk_orgs.addAll(arrpk_org);
				}
			}
		}

		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append("   substr(er_bxzb.djrq,1,4) year, ");// ��
		sql.append("   substr(er_bxzb.djrq,6,2) month, ");// ��
		sql.append("   bd_accasoa.dispname expenseSub, ");// ���ÿ�Ŀ
		sql.append("   tb_budgetsub.objname as budgetSub, ");// Ԥ���Ŀ
		sql.append("   tb_budgetsub.objcode as budgetsubno, ");// Ԥ���Ŀ����
		sql.append("   o1.name as budgetSubCom, ");// Ԥ�������λ
		sql.append("   org_dept.name as budgetSubdep, ");// Ԥ���������
		sql.append("   o2.name as outAccCompa, ");// ���˹�˾
		sql.append("   er_bxzb.fydwbm pk_org, ");// ��֯pk
		sql.append("   er_bxzb.djbh as billno, ");// ���ݱ��
		sql.append("   bd_currtype.name as currency, ");// ����
		sql.append("   er_busitem.amount as actualamount, ");// ʵ�ʷ�����
		sql.append("   p1.name as payee, ");// �տ���
		sql.append("   p2.name as reimburser, ");// ������
		sql.append("   ( ");
		sql.append("     case ");
		sql.append("       when s1.name is not null then  s1.name ");
		sql.append("       when s2.name is not null then  s2.name ");
		sql.append("       else p1.name ");
		sql.append("     end ");
		sql.append("   ) as supplier, ");// ��Ӧ��
		sql.append("   bd_project.project_name as project, ");// ��Ŀ
		sql.append("   d1.name as format, ");// ҵ̬
		sql.append("   d2.name as departmentFloor, ");// ����¥��
		sql.append("   d3.name as regisDepartment, ");// ���Ʋ���
		sql.append("   bd_billtype.billtypename as billtype, ");// ��������
		sql.append("   (case when nvl(er_bxzb.djrq,'~')<>'~' then substr(er_bxzb.djrq,1,10) else '' end) as billdate, ");// ��������
		sql.append("   (case when nvl(er_bxzb.shrq,'~')<>'~' then substr(er_bxzb.shrq,1,10) else '' end) as approvedate, ");// ��������
		sql.append("   (case when nvl(er_bxzb.jsrq,'~')<>'~' then substr(er_bxzb.jsrq,1,10) else '' end) as settmentdate, ");// ��������
		sql.append("   u1.user_name as applicant, ");// ������
		sql.append("   d4.name as invoceType, ");// ��Ʊ����
		sql.append("   er_busitem.defitem18 as invoceno, ");// ��Ʊ��
		sql.append("   er_busitem.defitem23 as invocemnyamount, ");// ��Ʊ���
		sql.append("   er_busitem.defitem14 as taxamount, ");// ��Ʊ���
		sql.append("   ( ");
		sql.append("     case ");
		sql.append("       er_bxzb.spzt ");
		sql.append("       when 0 then '����δͨ��' ");
		sql.append("       when 1 then '����ͨ��' ");
		sql.append("       when 2 then '����������' ");
		sql.append("       when 3 then '�ύ' ");
		sql.append("       when -1 then '����' ");
		sql.append("     end ");
		sql.append("   ) as approvestatus, ");// ����״̬
		sql.append("   er_bxzb.zy as subjectMatter, ");// ����
		sql.append("   d5.code as contractno, ");// ��ͬ����
		sql.append("   d5.name as contractname, ");// ��ͬ����
		sql.append("   u2.user_name as glbillmake, ");// ƾ֤�Ƶ���
		sql.append("   er_busitem.pk_busitem as busi, ");
		sql.append("   (case when nvl(gl_voucher.prepareddate,'~')<>'~' then substr(gl_voucher.prepareddate,1,10) else '' end) as glbilldate, ");// ƾ֤�Ƶ�����
		sql.append("   (case when nvl(gl_voucher.num,0)<>0 then '��-'||lpad(gl_voucher.num,4,'0') else '' end) as glcode, ");// ƾ֤��
		sql.append("   listagg(to_char(case when er_accrued_verify.accrued_billno is null then '' else er_accrued_verify.accrued_billno  end), ',') "
				+ "within GROUP(order by er_accrued_verify.bxd_billno) accruedbillno");// Ԥ�ᵥ��
		sql.append(" from ");
		sql.append("   er_bxzb ");
		sql.append("   left join er_busitem on er_busitem.pk_jkbx = er_bxzb.pk_jkbx ");
		sql.append("   and er_busitem.dr = 0 ");
		sql.append("   left join er_accrued_verify on er_accrued_verify.bxd_billno = er_bxzb.djbh ");
		sql.append("   and er_accrued_verify.dr = 0 ");
		sql.append("   left join org_orgs o1 on o1.pk_org = er_bxzb.fydwbm ");
		sql.append("   and o1.dr = 0 ");
		sql.append("   left join org_orgs o2 on o2.pk_org = er_bxzb.pk_org ");
		sql.append("   and o2.dr = 0 ");
		sql.append("   left join org_dept on org_dept.pk_dept = er_bxzb.fydeptid ");
		sql.append("   and org_dept.dr = 0 ");
		sql.append("   left join tb_budgetsub on tb_budgetsub.pk_obj = er_busitem.defitem12 ");
		sql.append("   left join bd_currtype on bd_currtype.pk_currtype = er_bxzb.bzbm ");
		sql.append("   and bd_currtype.dr = 0 ");
		sql.append("   left join bd_psndoc p1 on p1.pk_psndoc = er_busitem.receiver ");
		sql.append("   and p1.dr = 0 ");
		sql.append("   left join bd_psndoc p2 on p2.pk_psndoc = er_busitem.jkbxr ");
		sql.append("   and p2.dr = 0 ");
		sql.append("   left join bd_supplier s1 on s1.pk_supplier = er_bxzb.hbbm ");
		sql.append("   and s1.dr = 0 ");
		sql.append("   left join bd_supplier s2 on s2.pk_supplier = er_busitem.defitem20 ");
		sql.append("   and s2.dr = 0 ");
		sql.append("   left join sm_user u1 on u1.cuserid = er_bxzb.operator ");
		sql.append("   and u1.dr = 0 ");
		sql.append("   left join bd_project on bd_project.pk_project = er_busitem.jobid ");
		sql.append("   and bd_project.dr = 0 ");
		sql.append("   left join bd_defdoc d1 on d1.pk_defdoc = er_busitem.defitem22 ");
		sql.append("   and d1.dr = 0 ");
		sql.append("   left join bd_defdoc d2 on d2.pk_defdoc = er_busitem.defitem24 ");
		sql.append("   and d2.dr = 0 ");
		sql.append("   left join bd_defdoc d3 on d3.pk_defdoc = er_busitem.defitem26 ");
		sql.append("   and d3.dr = 0 ");
		sql.append("   left join bd_defdoc d4 on d4.pk_defdoc = er_busitem.defitem15 ");
		sql.append("   and d4.dr = 0 ");
		sql.append("   left join yer_contractfile d5 on d5.pk_defdoc = er_bxzb.zyx9 ");
		sql.append("   and d5.dr = 0 ");
		sql.append("   left join fip_docview_b on fip_docview_b.factorvalue1 = tb_budgetsub.pk_obj ");
		sql.append("   and fip_docview_b.dr = 0 ");
		sql.append("   and fip_docview_b.pk_classview in ( ");
		sql.append("     select ");
		sql.append("       fip_docview.pk_classview ");
		sql.append("     from ");
		sql.append("       fip_docview ");
		sql.append("     where ");
		sql.append("       fip_docview.viewcode = 'FY01' ");
		sql.append("       and pk_org = '000112100000000005FD' ");
		sql.append("   ) ");
		sql.append("   left join bd_accasoa on bd_accasoa.pk_accasoa = fip_docview_b.desdocvalue ");
		sql.append("   and bd_accasoa.dr = 0 ");
		sql.append("   left join fip_relation f on f.src_relationid LIKE '%' || er_bxzb.pk_jkbx || '%' ");
		sql.append("   and f.dr = 0 ");
		sql.append("   left join gl_voucher on gl_voucher.pk_voucher = f.des_relationid ");
		sql.append("   and gl_voucher.dr = 0 ");
		sql.append("   left join sm_user u2 on u2.cuserid = gl_voucher.billmaker ");
		sql.append("   and u2.dr = 0 ");
		sql.append("   left join bd_billtype on bd_billtype.pk_billtypeid = er_bxzb.pk_tradetypeid and nvl(bd_billtype.dr,0) = 0");
		sql.append(" where ");
		sql.append("   er_bxzb.dr = 0  and er_bxzb.djzt in (1,2,3) ");

		// ���
		if (queryValueMap.get("yeare") != null) {
			sql.append(" and substr(er_bxzb.djrq,1,4) in ('"
					+ queryValueMap.get("yeare") + "')");
		}
		// �·�
		if (queryValueMap.get("month") != null) {
			String[] monstr = queryValueMap.get("month").split(",");
			sql.append("and "
					+ SQLUtil.buildSqlForIn("substr(er_bxzb.djrq,6,2)", monstr));
		}
		// Ԥ���Ŀ
		if (queryValueMap.get("objcode") != null) {

			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("tb_budgetsub.pk_obj",
							queryValueMap.get("objcode").split(",")));
		}
		// ���ÿ�Ŀ
		if (queryValueMap.get("expensesub") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("bd_accasoa.pk_accasoa",
							queryValueMap.get("expensesub").split(",")));
		}
		// ��������
		if (queryValueMap.get("billtype") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("er_bxzb.pk_tradetypeid",
							queryValueMap.get("billtype").split(",")));
		}
		// ���˹�˾
		if (queryValueMap.get("pk_org") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("o2.pk_org",
							queryValueMap.get("pk_org").split(",")));
		}
		// Ԥ�������λ
		if (queryValueMap.get("ysgsdw") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("er_bxzb.fydwbm", queryValueMap
							.get("ysgsdw").split(",")));
		}
		// �Ƶ��ˣ�ƾ֤���Ƶ��ˣ�
		if (queryValueMap.get("billmaker") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("gl_voucher.billmaker",
							queryValueMap.get("billmaker").split(",")));
		}
		// NC���ݱ��
		if (queryValueMap.get("djbh") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("er_bxzb.djbh",
							queryValueMap.get("djbh").split(",")));
		}
		// ƾ֤���ڡ����ڼ䡿2020-09�����ջ���ڼ�
		if (queryValueMap.get("pzrq") != null) {
			String[] kjperiod = queryValueMap.get("pzrq").split(",");
			String stratdate = null;
			String enddate = null;
			if ((kjperiod.length == 1)) {
				stratdate = kjperiod[0];
				sql.append(" and gl_voucher.prepareddate > '" + stratdate + "'");
			} else {
				stratdate = kjperiod[0];
				enddate = kjperiod[1];
				sql.append(" and gl_voucher.prepareddate between '" + stratdate
						+ "' and '" + enddate + "'");
			}

		}
		// ����˾,��ѯ��������˾�����ҵ���������˾����Ϊ��ѯ����
		if (arrpk_orgs != null && arrpk_orgs.size() > 0) {
			sql.append("and "
					+ SQLUtil.buildSqlForIn("er_bxzb.fydwbm",
							arrpk_orgs.toArray(new String[arrpk_orgs.size()])));
		}

		sql.append(" group by ");
		sql.append("   er_bxzb.djrq, ");
		sql.append("   er_bxzb.djrq, ");
		sql.append("   bd_accasoa.dispname, ");
		sql.append("   tb_budgetsub.objname, ");
		sql.append("   tb_budgetsub.objcode, ");
		sql.append("   o1.name, ");
		sql.append("   org_dept.name, ");
		sql.append("   o2.name, ");
		sql.append("   er_bxzb.djbh, ");
		sql.append("   bd_currtype.name, ");
		sql.append("   er_busitem.amount, ");
		sql.append("   p1.name, ");
		sql.append("   p2.name, ");
		sql.append("   s2.name, ");
		sql.append("   s1.name, ");
		sql.append("   bd_project.project_name, ");
		sql.append("   d1.name, ");
		sql.append("   d2.name, ");
		sql.append("   d3.name, ");
		sql.append("   bd_billtype.billtypename, ");
		sql.append("   er_bxzb.djrq, ");
		sql.append("   er_bxzb.shrq, ");
		sql.append("   er_bxzb.jsrq, ");
		sql.append("   u1.user_name, ");
		sql.append("   d4.name, ");
		sql.append("   er_busitem.defitem18, ");
		sql.append("   er_busitem.defitem23, ");
		sql.append("   er_busitem.defitem14, ");
		sql.append("   er_bxzb.spzt, ");
		sql.append("   er_bxzb.zy, ");
		sql.append("   d5.code, ");
		sql.append("   d5.name, ");
		sql.append("   er_bxzb.fydwbm, ");
		sql.append("   u2.user_name, ");
		sql.append("   gl_voucher.prepareddate, ");
		sql.append("   gl_voucher.num, ");
		sql.append("   er_busitem.pk_busitem ");

		sql.append(" union all");
		sql.append(" select substr(er_accrued.billdate, 1, 4) as year,");
		sql.append(" substr(er_accrued.billdate, 6, 2) as month,");
		sql.append(" bd_accasoa.dispname               as expenseSub,");
		sql.append(" tb_budgetsub.objname              as budgetSub,");
		sql.append(" tb_budgetsub.objcode              as budgetsubno,");
		sql.append(" o1.name                           as budgetSubCom,");
		sql.append(" org_dept.name                     as budgetSubdep,");
		sql.append(" o2.name                           as outAccCompa,");
		sql.append(" er_accrued_detail.assume_org      as pk_org,");
		sql.append(" er_accrued.billno                 as billno,");
		sql.append(" bd_currtype.name                  as currency,");
		sql.append(" er_accrued_detail.amount          as actualamount,");
		sql.append(" ''                                as payee,");
		sql.append(" ''                                as reimburser,");
		sql.append(" bd_supplier.name                  as supplier,");
		sql.append(" bd_project.project_name           as project,");
		sql.append(" d1.name                           as format,");
		sql.append(" d2.name                           as departmentFloor,");
		sql.append(" d3.name                           as regisDepartment,");
		sql.append(" bd_billtype.billtypename          as billtype,");
		sql.append(" (case when nvl(er_accrued.billdate, '~') <> '~' then substr(er_accrued.billdate, 1, 10) else '' end) as billdate,");
		sql.append(" (case when nvl(er_accrued.approvetime, '~') <> '~' then substr(er_accrued.approvetime, 1, 10) else '' end) as approvedate,");
		sql.append(" ''                                as settmentdate,");
		sql.append(" u1.user_name                      as applicant,");
		sql.append(" '' 							   as invoceType,");
		sql.append(" ''                                as invoceno,");
		sql.append(" ''                                as invocemnyamount,");
		sql.append(" ''                                as taxamount,");
		sql.append(" (case er_accrued.apprstatus " + " when 0 then '����δͨ��'"
				+ " when 1 then '����ͨ��'" + " when 2 then '����������'"
				+ " when 3 then '�ύ'" + " when -1 then '����'"
				+ " end)                       as approvestatus,");
		sql.append(" er_accrued.reason                 as subjectMatter,");
		sql.append(" er_accrued.defitem11                                as contractno,");
		sql.append(" er_accrued.defitem12                                as contractname,");
		sql.append(" ''                                as glbillmake,");
		sql.append(" ''                                as busi,");
		sql.append(" ''                                as glbilldate,");
		sql.append(" ''                                as glcode,");
		sql.append(" ''                                as accruedbillno");
		sql.append(" from er_accrued");
		sql.append(" left join er_accrued_detail on er_accrued.pk_accrued_bill = er_accrued_detail.pk_accrued_bill and er_accrued_detail.dr = 0");
		sql.append(" left join tb_budgetsub      on tb_budgetsub.pk_obj = er_accrued_detail.defitem12 ");
		sql.append(" left join fip_docview_b     on fip_docview_b.factorvalue1 = tb_budgetsub.pk_obj and fip_docview_b.dr = 0"
				+ "and fip_docview_b.pk_classview in (select fip_docview.pk_classview from fip_docview where fip_docview.viewcode = 'FY01' and pk_org = '000112100000000005FD')");
		sql.append(" left join bd_accasoa        on bd_accasoa.pk_accasoa = fip_docview_b.desdocvalue and bd_accasoa.dr = 0");
		sql.append(" left join org_orgs o1       on o1.pk_org = er_accrued_detail.assume_org and o1.dr = 0");
		sql.append(" left join org_dept          on org_dept.pk_dept = er_accrued_detail.assume_dept and org_dept.dr = 0");
		sql.append(" left join org_orgs o2       on o2.pk_org = er_accrued.pk_org and o2.dr = 0");
		sql.append(" left join bd_currtype       on bd_currtype.pk_currtype = er_accrued.pk_currtype and bd_currtype.dr = 0");
		sql.append(" left join bd_defdoc d1      on d1.pk_defdoc = er_accrued_detail.defitem22 and d1.dr = 0");
		sql.append(" left join bd_defdoc d2      on d2.pk_defdoc = er_accrued_detail.defitem2 and d2.dr = 0 ");
		sql.append(" left join bd_defdoc d3      on d3.pk_defdoc = er_accrued_detail.defitem26 and d3.dr = 0");
		sql.append(" left join bd_supplier       on bd_supplier.pk_supplier = er_accrued_detail.pk_supplier and bd_supplier.dr = 0");
		sql.append(" left join bd_project        on bd_project.pk_project = er_accrued_detail.pk_project and bd_project.dr = 0");
		sql.append(" left join sm_user u1        on u1.cuserid = er_accrued.operator and u1.dr = 0");
		sql.append(" left join bd_billtype		 on bd_billtype.pk_billtypeid = er_accrued.pk_tradetypeid and nvl(bd_billtype.dr,0) = 0");
		sql.append(" where er_accrued.dr = 0 and er_accrued.billstatus in (1,3)");
		// ���
		if (queryValueMap.get("yeare") != null) {
			sql.append(" and substr(er_accrued.billdate,1,4) in ('"
					+ queryValueMap.get("yeare") + "')");
		}
		// �·�
		if (queryValueMap.get("month") != null) {
			String[] monstr = queryValueMap.get("month").split(",");
			sql.append("and "
					+ SQLUtil.buildSqlForIn("substr(er_accrued.billdate,6,2)",
							monstr));
		}
		// Ԥ���Ŀ
		if (queryValueMap.get("objcode") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("tb_budgetsub.pk_obj",
							queryValueMap.get("objcode").split(",")));
		}
		// ���ÿ�Ŀ
		if (queryValueMap.get("expensesub") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("bd_accasoa.pk_accasoa",
							queryValueMap.get("expensesub").split(",")));
		}
		// ��������
		if (queryValueMap.get("billtype") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("er_accrued.pk_tradetypeid",
							queryValueMap.get("billtype").split(",")));
		}
		// ���˹�˾
		if (queryValueMap.get("pk_org") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("o2.pk_org",
							queryValueMap.get("pk_org").split(",")));
		}

		// Ԥ�������λ
		if (queryValueMap.get("ysgsdw") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("er_accrued_detail.assume_org",
							queryValueMap.get("ysgsdw").split(",")));
		}
		// �Ƶ��ˣ�ƾ֤���Ƶ��ˣ�
		if (queryValueMap.get("billmaker") != null) {
			sql.append(" and 1<>1 ");
		}
		// NC���ݱ��
		if (queryValueMap.get("djbh") != null) {
			sql.append(" and  "
					+ SQLUtil.buildSqlForIn("er_accrued.billno",
							queryValueMap.get("djbh").split(",")));
		}
		// ƾ֤���ڡ����ڼ䡿2020-09�����ջ���ڼ�
		if (queryValueMap.get("pzrq") != null) {
			sql.append(" and 1<>1");
		}

		// ����˾,��ѯ��������˾�����ҵ���������˾����Ϊ��ѯ����
		if (arrpk_orgs != null && arrpk_orgs.size() > 0) {
			sql.append("and "
					+ SQLUtil.buildSqlForIn("er_accrued_detail.assume_org",
							arrpk_orgs.toArray(new String[arrpk_orgs.size()])));
		}
		List<BudgetExeDetailsVO> coll = (List<BudgetExeDetailsVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(BudgetExeDetailsVO.class));

		// ��ѯ����������������PK����������name��Ӧ��ϵ
		if (queryValueMap.get("dimension") != null) {
			pk_regionArr = queryValueMap.get("dimension").split(",");
			StringBuilder sqlstr = new StringBuilder();
			sqlstr.append("select name,pk_org from org_orgs where org_orgs.dr = 0");
			sqlstr.append(" and "
					+ SQLUtil.buildSqlForIn(" org_orgs.pk_org ", pk_regionArr));
			List<Map<String, String>> value = (List<Map<String, String>>) getBaseDAO()
					.executeQuery(sqlstr.toString(), new MapListProcessor());
			for (Map<String, String> map : value) {
				nameMap.put(map.get("pk_org"), map.get("name"));
			}
		}
		Iterator<BudgetExeDetailsVO> iterator = coll.iterator();
		while (iterator.hasNext()) {
			BudgetExeDetailsVO budgetExeDetailsVO = iterator.next();
			// ��VO�л�ȡ��˾��pk_org
			String pk_org = budgetExeDetailsVO.getPk_org();
			// ���ݹ�˾��pk_org��regionMap�л�ȡ�����pk_region
			String pk_region = regionMap.get(pk_org);

			if ("~".equals(pk_region)) {
				iterator.remove();
			} else {

				if (pk_region == null || "".equals(pk_region)) {
					pk_region = pk_org;
				}
				// �����ѯ������area������Ϊnull�Ļ���nameMap��û��ֵ�ģ���������newһ��map�����pk_region�����򣩶�Ӧname�����ƣ�
				Map<String, String> region_nameMap = new HashMap<String, String>();
				if (!"~".equals(pk_region)) {
					// ��nameMap��ֵʱ���ѯ������ָ��������PK��ǰ���Ѿ���ѯ������pk��Ӧ��name����
					if (nameMap.size() > 0) {
						// ���������pk_region��nameMap�л�ȡ�����name
						String name = nameMap.get(pk_region);
						budgetExeDetailsVO.setDimension(name);
					} else {
						/**
						 * ��nameMapΪ��ʱ��˵����ѯ������û������pk��
						 * ��Ҫ�Ӳ�ѯ���costLedgerVOs�в�����Ӧ������pk
						 * ��������pk����ѯ��������name���������set��costLedgerVO
						 * �����Ҵ���region_nameMap�У�
						 * �����ظ���ѯ�������ƣ������˾��pk_org���ܶ�Ӧһ������pk��
						 */
						if (region_nameMap.get(pk_region) != null) {
							budgetExeDetailsVO.setDimension(region_nameMap
									.get(pk_region));
						} else {
							String sql_region = "select name,pk_org from org_orgs where org_orgs.dr = 0 and pk_org = '"
									+ pk_region + "'";
							Map<String, String> value = (Map<String, String>) getBaseDAO()
									.executeQuery(sql_region,
											new MapProcessor());
							budgetExeDetailsVO.setDimension(value.get("name"));
							region_nameMap.put(value.get("pk_org"),
									value.get("name"));
						}
					}
				} else {
					budgetExeDetailsVO.setDimension("");
				}
			}
		}

		return coll;
	}

}
