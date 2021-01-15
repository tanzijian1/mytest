package nc.bs.tg.fund.rz.report.cost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.tg.fund.rz.report.ReportConts;
import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.AppContext;
import nc.vo.tgfn.report.cost.CostLedgerVO;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.tgfp.report.ReportOrgVO;

import org.apache.commons.lang.StringUtils;

import com.ufida.dataset.IContext;

/**
 * 押金保证金台账（成本报表）
 * 
 * @author ASUS
 * 
 */
public class CostLedgerUtils extends ReportUtils {
	static CostLedgerUtils utils;

	public static CostLedgerUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new CostLedgerUtils();
		}
		return utils;
	}

	public CostLedgerUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance()
				.getPropertiesAry(new CostLedgerVO()));
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
			List<CostLedgerVO> list = getCostLedgerVOs();
			if(list != null && list.size() > 0){
				cmpreportresults = transReportResult(list);
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

	/**
	 * 查询押金保证金台账报表数据方法
	 * @param travellingTrader 
	 * @param area2 
	 * @param documentationDate 
	 * @param travellingTrader2 
	 * @throws BusinessException 
	 * */
	@SuppressWarnings("unchecked")
	private List<CostLedgerVO> getCostLedgerVOs() throws BusinessException{
		String documentationDate_begin = queryValueMap.get("documentationDate_begin");
		String documentationDate_end = queryValueMap.get("documentationDate_end");
		String area = queryValueMap.get("area");
		String travellingTrader = queryValueMap.get("travellingTrader");
		String companyName = queryValueMap.get("companyName");
		String transactionPerson = queryValueMap.get("transactionPerson");
		String receiptNum = queryValueMap.get("receiptNum");
		String contractCode = queryValueMap.get("contractCode");
		String outBillNo = queryValueMap.get("outBillNo");
		String tradeType = queryValueMap.get("tradeType");
		//add by 黄冠华 SDYC-56 押金保证金台账（成本报表）20200818 begin
		List<Map<String, Object>> lists=getAviBalOrg(companyName);
		if(lists==null||lists.isEmpty()){//没有余额直接返回空
			return null;
		}
		//add by 黄冠华 SDYC-56 押金保证金台账（成本报表）20200818 end
		//获取所有公司层次集合VO
		List<ReportOrgVO> orgVOs = getOrgVOs();
		//存放pk_org和pk_region的对应关系
		Map<String, String> regionMap = new HashMap<String,String>();
		Map<String, ArrayList<String>> orgsMap = new HashMap<String,ArrayList<String>>();
		for (ReportOrgVO reportOrgVO : orgVOs) {
			String pk_org = reportOrgVO.getPk_org();
			String pk_region = reportOrgVO.getPk_region();
			//存放pk_org——pk_region
			regionMap.put(pk_org, pk_region);
			/**
			 * 从orgsMap中获取pk_region对应的list集合，如果集合为null则
			 * orgsMap要存储以当前pk_region为key，存放了当前pk_org的list为value的新键值对
			 * 如果不为null则说明当前orgsMap中已存在pk_region为key的键值对，只需要往list
			 * 中存入当前pk_org即可
			 */
			ArrayList<String> arrayList = orgsMap.get(pk_region);
			if(arrayList != null){
				arrayList.add(pk_org);
			}else{
				ArrayList<String> arrayList2 = new ArrayList<String>();
				arrayList2.add(pk_org);
				orgsMap.put(pk_region, arrayList2);
			}
		}
		StringBuilder sql = new StringBuilder();
		//付款单
		sql.append("select distinct * from ( ");
		sql.append("select  * from ( ");
		sql.append("select ");
		sql.append("gl_voucher.pk_voucher as pkVoucher, ");//凭证主键
		sql.append("org_orgs.pk_org as area, ");//地区
		sql.append("org_orgs.name as companyName, ");//公司名称
		sql.append("bd_accasoa.name as accountingSubject, ");//会计科目
		sql.append("bd_cust_supplier.name as travellingTrader, ");//客商
		sql.append("gl_voucher.explanation as voucherAbstract , ");//保证金或押金内容（凭证摘要）
		sql.append("gl_detail.localdebitamount as paymentAmount, ");//支付金额
		sql.append("SUBSTR(ap_paybill.paydate,1,10) as paymentDate, ");//支付日期
		sql.append("gl.user_name as proofMaker, ");//凭证制单人
		sql.append("SUBSTR(gl_voucher.prepareddate,1,10) as documentationDate, ");//制单日期
		sql.append("to_char(gl_voucher.num) as proofNum, ");//凭证号
		sql.append("org_dept.name as transactionDepartment, ");//经办部门
		sql.append("ap.user_name as transactionPerson, ");//经办人（退款责任人）
		sql.append("fct_ap.actualinvalidate as refundDate, ");//退款到期日
//		sql.append("dap_dapsystem.systypename as sourceSystem, ");//来源系统
		sql.append("case when nvl(dap_dapsystem.systypename,'~')='~' then '总账' else dap_dapsystem.systypename end  as sourceSystem, ");//来源系统
		sql.append("bd_billtype.billtypename as tradeType, ");//交易类型
		sql.append("ap_paybill.billno as receiptNum, ");//单据编号
		sql.append("ap_paybill.def3 as imageCode, ");//影像编码
		sql.append("fct_ap.vbillcode as contractCode, ");//合同编码
		sql.append("fct_ap.ctname as contractName, ");//合同名称
		sql.append("ap_paybill.def2 as outBillNo, ");//外系统单据号
		sql.append("'' as sourceRemark ");//来源备注
		sql.append(" ,gl_detail.pk_org ");//组织pk
		sql.append(" ,bd_accasoa.pk_accasoa ");//科目
		sql.append(" ,gl_detail.assid ");//来源备注
		sql.append(" ,case when  length(nvl(fct_ap.vbillcode,'0'))>1 and fct_ap.blatest = 'Y' then 'Y' else (case when length(nvl(fct_ap.vbillcode,'0'))<=1 then 'Y' else 'N' end)end isneed ");
		sql.append("from gl_voucher ");//凭证
		//凭证关联分录
		sql.append("left join gl_detail on gl_detail.pk_voucher = gl_voucher.pk_voucher and nvl(gl_detail.dr,0) = 0 ");
		//分录关联模块信息
//		sql.append("left join dap_dapsystem on RTRIM(gl_detail.pk_systemv) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//分录关联会计科目
		sql.append("left join bd_accasoa on bd_accasoa.pk_accasoa = gl_detail.pk_accasoa and nvl(bd_accasoa.dr,0) = 0  ");
		//会计科目关联会计科目基本信息表
		sql.append("left join bd_account on bd_accasoa.pk_account = bd_account.pk_account and nvl(bd_account.dr,0) = 0 ");
		//凭证关联中间表
		sql.append("left join fip_relation on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(fip_relation.dr,0) = 0 ");
		//分录关联模块信息
		sql.append("left join dap_dapsystem on RTRIM(fip_relation.src_system) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//中间表关联供应商付款单
		sql.append("left join ap_paybill on ap_paybill.pk_paybill = fip_relation.src_relationid and nvl(ap_paybill.dr,0) = 0 ");
		//关联交易类型
		sql.append("left join bd_billtype on bd_billtype.pk_billtypeid = ap_paybill.pk_tradetypeid and (nvl(bd_billtype.dr,0) = 0 "
				+ "or bd_billtype.dr is null)  ");
		//供应商付款单关联付款合同
		sql.append("left join fct_ap on fct_ap.vbillcode = ap_paybill.def5 and nvl(fct_ap.dr,0) = 0 ");
		//凭证关联用户
		sql.append("left join sm_user gl on gl_voucher.pk_prepared = gl.cuserid and nvl(gl.dr,0) = 0 ");
		//凭证关联组织
		sql.append("left join org_orgs on org_orgs.pk_org = gl_voucher.pk_org and nvl(org_orgs.dr,0) = 0 ");
		//分录关联辅助项目
		sql.append("left join gl_docfree1 on gl_detail.assid = gl_docfree1.assid and nvl(gl_docfree1.dr,0) = 0 ");
		//辅助项目关联客商
		sql.append("left join bd_cust_supplier on bd_cust_supplier.pk_cust_sup = gl_docfree1.f4 and nvl(bd_cust_supplier.dr,0) = 0 ");
		//付款单关联用户
		sql.append("left join sm_user ap on ap_paybill.creator = ap.cuserid and nvl(ap.dr,0) = 0 ");
		//用户关联人员基本信息
		sql.append("left join bd_psndoc on bd_psndoc.pk_psndoc = ap.pk_psndoc and nvl(bd_psndoc.dr,0) = 0 ");
		//人员基本信息关联人员工作信息
		sql.append("left join bd_psnjob on bd_psnjob.pk_psndoc = bd_psndoc.pk_psndoc and bd_psnjob.ismainjob = 'Y' and nvl(bd_psnjob.dr,0) = 0 ");
		//人员工作信息 关联 HR部门 
		sql.append("left join org_dept on org_dept.pk_dept = bd_psnjob.pk_dept and nvl(org_dept.dr,0) = 0 ");
		sql.append("where nvl(gl_voucher.dr,0) = 0 ");
		//sql.append("and fct_ap.blatest = 'Y' ");  
		sql.append("and gl_voucher.period <> '00' ");
		sql.append("and bd_account.code in ('12210201','12210202','22410401','22410402') ");
		//sql.append("and gl_detail.localdebitamount <> 0 ");
		
		//添加查询条件，区域转换成相应的公司pk
		if(area != null){
			String[] pk_regionArr = area.split(",");
			//new 一个list用于存放所有的pk_region(区域)对应的pk_org(公司)
			List<String> pk_org = new ArrayList<String>();
			//循环遍历多个pk_region(区域)
			for (String pk_region : pk_regionArr) {
				//如果get pk_region对应的org list不为null，则存放在pk_org这个list中
				if(orgsMap.get(pk_region) != null){
					pk_org.addAll(orgsMap.get(pk_region));
				}
			}
			if(pk_org.size()>0){
				sql.append(" and "
						+ SQLUtil.buildSqlForIn(
								" org_orgs.pk_org ",
								pk_org.toArray(new String[pk_org.size()])));
			}
		}
		//添加查询条件，客商pk
		if(travellingTrader != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_cust_supplier.pk_cust_sup ",
							travellingTrader.split(",")));
		}
		//添加查询条件，制单日期介于date与date之间
		if(documentationDate_begin != null && documentationDate_end != null){
			sql.append("and gl_voucher.prepareddate between '"+documentationDate_begin+"' and '"+documentationDate_end+"' ");
		}
		//添加查询条件，公司PK
		if(companyName != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" org_orgs.pk_org ",
							companyName.split(",")));
		}
		//添加查询条件，经办人PK
		if(transactionPerson != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ap.cuserid ",
							transactionPerson.split(",")));
		}
		//添加查询条件，单据编号
		if(receiptNum != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ap_paybill.billno ",
							receiptNum.split(",")));
		}
		//添加查询条件，合同编码
		if(contractCode != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" fct_ap.vbillcode ",
							contractCode.split(",")));
		}
		//添加查询条件，外系统单据编号
		if(outBillNo != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ap_paybill.def2 ",
							outBillNo.split(",")));
		}
		//添加查询条件，单据类型
		if(tradeType != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_billtype.pk_billtypeid ",
							tradeType.split(",")));
		}
		sql.append(" ) ");
		sql.append("where isneed= 'Y' ");
		
		sql.append("union all ");
		//报销单
		sql.append("select * from ( ");
		sql.append("select ");
		sql.append("gl_voucher.pk_voucher as pkVoucher, ");//凭证主键
		sql.append("org_orgs.pk_org as area, ");//地区
		sql.append("org_orgs.name as companyName, ");//公司名称
		sql.append("bd_accasoa.name as accountingSubject, ");
		sql.append("bd_cust_supplier.name as travellingTrader, ");
		sql.append("gl_voucher.explanation as voucherAbstract, ");
		sql.append("gl_detail.localdebitamount as paymentAmount, ");
		sql.append("SUBSTR(er_bxzb.paydate,1,10) as paymentDate, ");
		sql.append("gl.user_name as proofMaker, ");
		sql.append("SUBSTR(gl_voucher.prepareddate,1,10) as documentationDate, ");
		sql.append("to_char(gl_voucher.num) as proofNum, ");
		sql.append("org_dept.name as transactionDepartment, ");
		sql.append("er.user_name as transactionPerson, ");
		sql.append("fct_ap.actualinvalidate as refundDate, ");
//		sql.append("dap_dapsystem.systypename as sourceSystem, ");//来源系统
		sql.append("case when nvl(dap_dapsystem.systypename,'~')='~' then '总账' else dap_dapsystem.systypename end  as sourceSystem, ");//来源系统
		sql.append("bd_billtype.billtypename as tradeType, ");
		sql.append("er_bxzb.djbh as receiptNum, ");
		sql.append("er_bxzb.zyx16 as imageCode, ");
		sql.append("fct_ap.vbillcode as contractCode, ");
		sql.append("fct_ap.ctname as contractName, ");
		sql.append("'' as outBillNo, ");//无外系统单据号
		sql.append("'' as sourceRemark ");
		sql.append(" ,gl_detail.pk_org ");//组织pk
		sql.append(" ,bd_accasoa.pk_accasoa ");//科目
		sql.append(" ,gl_detail.assid ");//来源备注
	    sql.append(" ,case when  length(nvl(fct_ap.vbillcode,'0'))>1 and fct_ap.blatest = 'Y' then 'Y' else (case when length(nvl(fct_ap.vbillcode,'0'))<=1 then 'Y' else 'N' end)end isneed ");
		sql.append("from gl_voucher ");
		//凭证关联分录
		sql.append("left join gl_detail on gl_detail.pk_voucher = gl_voucher.pk_voucher and nvl(gl_detail.dr,0) = 0 ");
		//分录关联模块信息
//		sql.append("left join dap_dapsystem on RTRIM(gl_detail.pk_systemv) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//分录关联会计科目
		sql.append("left join bd_accasoa on bd_accasoa.pk_accasoa = gl_detail.pk_accasoa and nvl(bd_accasoa.dr,0) = 0 ");
		//会计科目关联会计科目基本信息表
		sql.append("left join bd_account on bd_accasoa.pk_account = bd_account.pk_account and nvl(bd_account.dr,0) = 0 ");
		//凭证关联中间表
		sql.append("left join fip_relation on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(fip_relation.dr,0) = 0 ");
		//分录关联模块信息
		sql.append("left join dap_dapsystem on RTRIM(fip_relation.src_system) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//中间表关联报销单
		sql.append("left join er_bxzb on er_bxzb.pk_jkbx = fip_relation.src_relationid and nvl(er_bxzb.dr,0) = 0 ");
		//关联交易类型
		sql.append("left join bd_billtype on bd_billtype.pk_billtypeid = er_bxzb.pk_tradetypeid and (nvl(bd_billtype.dr,0) = 0 "
				+ "or bd_billtype.dr is null) ");
		//报销单关联合同
		sql.append("left join fct_ap on fct_ap.vbillcode = er_bxzb.zyx2 and nvl(fct_ap.dr,0) = 0 ");
		//报销单关联用户
		sql.append("left join sm_user er on er_bxzb.creator = er.cuserid and nvl(er.dr,0) = 0 ");
		//用户关联人员基本信息
		sql.append("left join bd_psndoc on bd_psndoc.pk_psndoc = er.pk_psndoc and nvl(bd_psndoc.dr,0) = 0 ");
		//人员基本信息关联人员工作信息
		sql.append("left join bd_psnjob on bd_psnjob.pk_psndoc = bd_psndoc.pk_psndoc and bd_psnjob.ismainjob = 'Y' and nvl(bd_psnjob.dr,0) = 0 ");
		//人员工作信息 关联 HR部门 
		sql.append("left join org_dept on org_dept.pk_dept = bd_psnjob.pk_dept and nvl(org_dept.dr,0) = 0 ");
		//凭证关联用户
		sql.append("left join sm_user gl on gl_voucher.pk_prepared = gl.cuserid and nvl(gl.dr,0) = 0 ");
		//凭证关联组织
		sql.append("left join org_orgs on org_orgs.pk_org = gl_voucher.pk_org and nvl(org_orgs.dr,0) = 0 ");
		//分录关联辅助项目
		sql.append("left join gl_docfree1 on gl_detail.assid = gl_docfree1.assid  and nvl(gl_docfree1.dr,0) = 0 ");
		//辅助项目关联客商
		sql.append("left join bd_cust_supplier on bd_cust_supplier.pk_cust_sup = gl_docfree1.f4 and nvl(bd_cust_supplier.dr,0) = 0 ");
		sql.append("where nvl(gl_voucher.dr,0) = 0 ");
		//sql.append("and fct_ap.blatest = 'Y' ");
		sql.append("and gl_voucher.period <> '00' ");
		sql.append("and bd_account.code in ('12210201','12210202','22410401','22410402') ");
		//sql.append("and gl_detail.localdebitamount <> 0 ");

		if(area != null){
			String[] pk_regionArr = area.split(",");
			List<String> pk_org = new ArrayList<String>();
			for (String pk_region : pk_regionArr) {
				if(orgsMap.get(pk_region) != null){
					pk_org.addAll(orgsMap.get(pk_region));
				}
			}
			if(pk_org.size()>0){
				sql.append(" and "
						+ SQLUtil.buildSqlForIn(
								" org_orgs.pk_org ",
								pk_org.toArray(new String[pk_org.size()])));
			}
		}
		if(travellingTrader != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_cust_supplier.pk_cust_sup ",
							travellingTrader.split(",")));
		}
		if(documentationDate_begin != null && documentationDate_end != null){
			sql.append("and gl_voucher.prepareddate between '"+documentationDate_begin+"' and '"+documentationDate_end+"'");
		}
		//添加查询条件，公司PK
		if(companyName != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" org_orgs.pk_org ",
							companyName.split(",")));
		}
		//添加查询条件，经办人PK
		if(transactionPerson != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" er.cuserid ",
							transactionPerson.split(",")));
		}
		//添加查询条件，单据编号
		if(receiptNum != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" er_bxzb.djbh ",
							receiptNum.split(",")));
		}
		//添加查询条件，合同编码
		if(contractCode != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" fct_ap.vbillcode ",
							contractCode.split(",")));
		}
		//添加查询条件，外系统单据编号 
		if(outBillNo != null){
			sql.append(" and 1=2");//add by 黄冠华 报销单没有外系统单据号，用外系统单据编号过滤数据时报销单不显示 20200901
		}
		//添加查询条件，单据类型
		if(tradeType != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_billtype.pk_billtypeid ",
							tradeType.split(",")));
		}
		sql.append(" ) ");
		sql.append("where isneed= 'Y' ");
		
		sql.append(" union all ");
		//收款单
		sql.append("select * from (");
		sql.append("select ");
		sql.append("gl_voucher.pk_voucher as pkVoucher, ");//凭证主键
		sql.append("org_orgs.pk_org as area, ");//地区
		sql.append("org_orgs.name as companyName, ");//公司名称
		sql.append("bd_accasoa.name as accountingSubject, ");//会计科目
		sql.append("bd_cust_supplier.name as travellingTrader, ");//客商
		sql.append("gl_voucher.explanation as voucherAbstract , ");//保证金或押金内容（凭证摘要）
		sql.append("gl_detail.localdebitamount as paymentAmount, ");//支付金额
		sql.append("SUBSTR(ar_gatherbill.paydate,1,10) as paymentDate, ");//支付日期
		sql.append("gl.user_name as proofMaker, ");//凭证制单人
		sql.append("SUBSTR(gl_voucher.prepareddate,1,10) as documentationDate, ");//制单日期
		sql.append("to_char(gl_voucher.num) as proofNum, ");//凭证号
		sql.append("org_dept.name as transactionDepartment, ");//经办部门
		sql.append("ap.user_name as transactionPerson, ");//经办人（退款责任人）
		sql.append("fct_ap.actualinvalidate as refundDate, ");//退款到期日
//		sql.append("dap_dapsystem.systypename as sourceSystem, ");//来源系统
		sql.append("case when nvl(dap_dapsystem.systypename,'~')='~' then '总账' else dap_dapsystem.systypename end  as sourceSystem, ");//来源系统
		sql.append("bd_billtype.billtypename as tradeType, ");//交易类型
		sql.append("ar_gatherbill.billno as receiptNum, ");//单据编号
		sql.append("ar_gatherbill.def3 as imageCode, ");//影像编码
		sql.append("fct_ap.vbillcode as contractCode, ");//合同编码
		sql.append("fct_ap.ctname as contractName, ");//合同名称
		sql.append("ar_gatherbill.def2 as outBillNo, ");//外系统单据号
		sql.append("'' as sourceRemark ");//来源备注
		sql.append(" ,gl_detail.pk_org ");//组织pk
		sql.append(" ,bd_accasoa.pk_accasoa ");//科目
		sql.append(" ,gl_detail.assid ");//来源备注
	    sql.append(" ,case when length(nvl(fct_ap.vbillcode,'0'))>1 and fct_ap.blatest = 'Y' then 'Y' else (case when length(nvl(fct_ap.vbillcode,'0'))<=1 then 'Y' else 'N' end)end isneed ");
		sql.append("from gl_voucher ");//凭证
		//凭证关联分录
		sql.append("left join gl_detail on gl_detail.pk_voucher = gl_voucher.pk_voucher and nvl(gl_detail.dr,0) = 0 ");
		//分录关联模块信息
//		sql.append("left join dap_dapsystem on RTRIM(gl_detail.pk_systemv) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//分录关联会计科目
		sql.append("left join bd_accasoa on bd_accasoa.pk_accasoa = gl_detail.pk_accasoa and nvl(bd_accasoa.dr,0) = 0  ");
		//会计科目关联会计科目基本信息表
		sql.append("left join bd_account on bd_accasoa.pk_account = bd_account.pk_account and nvl(bd_account.dr,0) = 0 ");
		//凭证关联中间表
		sql.append("left join fip_relation on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(fip_relation.dr,0) = 0 ");
		//分录关联模块信息
		sql.append("left join dap_dapsystem on RTRIM(fip_relation.src_system) = dap_dapsystem.systypecode and nvl(dap_dapsystem.dr,0) = 0 ");
		//中间表关联供应商收款单
		sql.append("left join ar_gatherbill on ar_gatherbill.pk_gatherbill = fip_relation.src_relationid and nvl(ar_gatherbill.dr,0) = 0 ");
		//关联交易类型
		sql.append("left join bd_billtype on bd_billtype.pk_billtypeid = ar_gatherbill.pk_tradetypeid and (nvl(bd_billtype.dr,0) = 0 "
				+ "or bd_billtype.dr is null)  ");
		//供应商收款单关联收款合同
        sql.append("left join fct_ap on fct_ap.vbillcode = ar_gatherbill.def5 and nvl(fct_ap.dr,0) = 0 ");
		//凭证关联用户
		sql.append("left join sm_user gl on gl_voucher.pk_prepared = gl.cuserid and nvl(gl.dr,0) = 0 ");
		//凭证关联组织
		sql.append("left join org_orgs on org_orgs.pk_org = gl_voucher.pk_org and nvl(org_orgs.dr,0) = 0 ");
		//分录关联辅助项目
		sql.append("left join gl_docfree1 on gl_detail.assid = gl_docfree1.assid and nvl(gl_docfree1.dr,0) = 0 ");
		//辅助项目关联客商
		sql.append("left join bd_cust_supplier on bd_cust_supplier.pk_cust_sup = gl_docfree1.f4 and nvl(bd_cust_supplier.dr,0) = 0 ");
		//付款单关联用户
		sql.append("left join sm_user ap on ar_gatherbill.creator = ap.cuserid and nvl(ap.dr,0) = 0 ");
		//用户关联人员基本信息
		sql.append("left join bd_psndoc on bd_psndoc.pk_psndoc = ap.pk_psndoc and nvl(bd_psndoc.dr,0) = 0 ");
		//人员基本信息关联人员工作信息
		sql.append("left join bd_psnjob on bd_psnjob.pk_psndoc = bd_psndoc.pk_psndoc and bd_psnjob.ismainjob = 'Y' and nvl(bd_psnjob.dr,0) = 0 ");
		//人员工作信息 关联 HR部门 
		sql.append("left join org_dept on org_dept.pk_dept = bd_psnjob.pk_dept and nvl(org_dept.dr,0) = 0 ");
		sql.append("where nvl(gl_voucher.dr,0) = 0 ");
		//sql.append("and fct_ap.blatest = 'Y' "); 
		sql.append("and gl_voucher.period <> '00' ");
		sql.append("and bd_account.code in ('12210201','12210202','22410401','22410402') ");
		//sql.append("and gl_detail.localdebitamount <> 0 ");

		//添加查询条件，区域转换成相应的公司pk
		if(area != null){
			String[] pk_regionArr = area.split(",");
			//new 一个list用于存放所有的pk_region(区域)对应的pk_org(公司)
			List<String> pk_org = new ArrayList<String>();
			//循环遍历多个pk_region(区域)
			for (String pk_region : pk_regionArr) {
				//如果get pk_region对应的org list不为null，则存放在pk_org这个list中
				if(orgsMap.get(pk_region) != null){
					pk_org.addAll(orgsMap.get(pk_region));
				}
			}
			if(pk_org.size()>0){
				sql.append(" and "
						+ SQLUtil.buildSqlForIn(
								" org_orgs.pk_org ",
								pk_org.toArray(new String[pk_org.size()])));
			}
		}
		//添加查询条件，客商pk
		if(travellingTrader != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_cust_supplier.pk_cust_sup ",
							travellingTrader.split(",")));
		}
		//添加查询条件，制单日期介于date与date之间
		if(documentationDate_begin != null && documentationDate_end != null){
			sql.append("and gl_voucher.prepareddate between '"+documentationDate_begin+"' and '"+documentationDate_end+"' ");
		}
		//添加查询条件，公司PK
		if(companyName != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" org_orgs.pk_org ",
							companyName.split(",")));
		}
		//添加查询条件，经办人PK
		if(transactionPerson != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ap.cuserid ",
							transactionPerson.split(",")));
		}
		//添加查询条件，单据编号
		if(receiptNum != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ar_gatherbill.billno ",
							receiptNum.split(",")));
		}
		//添加查询条件，合同编码
		if(contractCode != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" fct_ap.vbillcode ",
							contractCode.split(",")));
		}
		//添加查询条件，外系统单据编号
		if(outBillNo != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" ar_gatherbill.def2 ",
							outBillNo.split(",")));
		}
		//添加查询条件，单据类型
		if(tradeType != null){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_billtype.pk_billtypeid ",
							tradeType.split(",")));
		}
		
		
		
		sql.append(") ");
	
		sql.append(") ");

		
		
		
		List<CostLedgerVO> costLedgerVOs = (List<CostLedgerVO>)getBaseDAO().executeQuery(sql.toString(), new BeanListProcessor(CostLedgerVO.class));
		// 如果查询条件中area（区域）为null的话，nameMap是没有值的，所以这里new一个map来存放pk_region（区域）对应name（名称）
		Map<String,String> region_nameMap = new HashMap<String,String>();
		List<CostLedgerVO> useLessVO= new ArrayList<CostLedgerVO>();
		//设置区域area名称和序号
		if(null != costLedgerVOs && costLedgerVOs.size()>0){
			//初始化序号为0
			int sequeNumGlobal = 0;
			Iterator<CostLedgerVO> iterator = costLedgerVOs.iterator();
			String pk_org2="";
			String pk_accasoa="";
			String assid="";
			String uniStr="";
			while(iterator.hasNext()){
				CostLedgerVO costLedgerVO = iterator.next();
				//从VO中获取公司的pk_org
				String pk_org = costLedgerVO.getArea();
				//根据公司的pk_org从regionMap中获取区域的pk_region
				String pk_region = regionMap.get(pk_org);
				
				//add by黄冠华 SDYC-56 押金保证金台账（成本报表）20200818 begin
				pk_org2=costLedgerVO.getPk_org();
				pk_accasoa=costLedgerVO.getPk_accasoa();
				assid=costLedgerVO.getAssid();
//				uniStr=pk_org2+pk_accasoa+assid;
//				if(!lists.contains(uniStr)){
//					useLessVO.add(costLedgerVO);
//				}
				for(Map map:lists){
					if(pk_org2.equals(map.get("pk_org"))&&pk_accasoa.equals(map.get("pk_accasoa"))&&assid.equals(map.get("assid"))){
						costLedgerVO.setBalMny(map.get("balmny")+"");
					}
				}
				//add by黄冠华 SDYC-56 押金保证金台账（成本报表）20200818 end
				//判断区域PK是否存在，不存在则移除
				if(null ==pk_region || pk_region.equals("~")){
					iterator.remove();
				}else{
					//每遍历一个元素，序号加1
					++sequeNumGlobal;
					//往VO类写入当前序号
					costLedgerVO.setSequeNumGlobal(new UFDouble(sequeNumGlobal));
					/**从查询结果costLedgerVOs中查找相应的区域pk根据区域pk来查询区域名称name，
					 * 查出来后set进costLedgerVO，并且存入region_nameMap中，
					避免重复查询区域名称（多个公司的pk_org可能对应一个区域pk）*/
						if(region_nameMap.get(pk_region) != null){
							costLedgerVO.setArea(region_nameMap.get(pk_region));
						}else{
							String sql_region = "select name,pk_org from org_orgs where org_orgs.dr = 0 and pk_org = '"+pk_region+"'";
							Map<String, String> value = (Map<String, String>) getBaseDAO()
									.executeQuery(sql_region, new MapProcessor());
							costLedgerVO.setArea(value.get("name"));
							region_nameMap.put(value.get("pk_org"), value.get("name"));
						}
				}
			}
			for(CostLedgerVO vo:costLedgerVOs){
				if(null==vo.getBalMny()){
					useLessVO.add(vo);
				}
			}
			if(useLessVO!=null&&useLessVO.size()>0){
				costLedgerVOs.removeAll(useLessVO);
			}
			//add by  黄冠华 SDYC-56 押金保证金台账（成本报表） 添加期初数据 20200824 begin
			if(costLedgerVOs!=null&&costLedgerVOs.size()>0){
				List<CostLedgerVO> initVOs=getInitDetails();
				if(initVOs!=null&&initVOs.size()>0){
					initVOs.addAll(costLedgerVOs);
					costLedgerVOs=initVOs;		
				}
				sequeNumGlobal=0;
				for(CostLedgerVO costLedgerVO:costLedgerVOs){
					++sequeNumGlobal;
					costLedgerVO.setSequeNumGlobal(new UFDouble(sequeNumGlobal));
				}
			}
			//add by  黄冠华 SDYC-56 押金保证金台账（成本报表） 添加期初数据 20200824 end
		}
		return costLedgerVOs;
	}
	
	/**
	 * @author 黄冠华
	 * 查询上线前数据
	 * 20200824
	 * @throws DAOException 
	 */
	private List<CostLedgerVO> getInitDetails() throws DAOException{
		List<CostLedgerVO> initVOs =null;
		String documentationDate_begin = queryValueMap.get("documentationDate_begin");
		String documentationDate_end = queryValueMap.get("documentationDate_end");
		String area = queryValueMap.get("area");
		String travellingTrader = queryValueMap.get("travellingTrader");
		String companyName = queryValueMap.get("companyName");
		String transactionPerson = queryValueMap.get("transactionPerson");
		String receiptNum = queryValueMap.get("receiptNum");
		String contractCode = queryValueMap.get("contractCode");
		String outBillNo = queryValueMap.get("outBillNo");
		String tradeType = queryValueMap.get("tradeType");
		StringBuffer sb=new StringBuffer();
		//取旧系统期初
		sb.append(" SELECT '' pkVoucher, ");                                                                        //凭证主键                   
		sb.append(" zb_3 as area,                                                                                ");//地区                     
		sb.append("    zb_4 as companyName,                                                                      ");//公司名称                   
		sb.append(" zb_7 as accountingSubject,                                                                   ");//会计科目                   
		sb.append(" zb_11 as travellingTrader,                                                                   ");//客商                     
		sb.append(" zb_1 as voucherAbstract,                                                                     ");//保证金或押金内容（凭证摘要）         
		sb.append(" zb_18 as paymentAmount,                                                                      ");//支付金额                   
		sb.append(" zb_19 as paymentDate,                                                                        ");//支付日期                   
		sb.append(" '' as proofMaker,                                                                            ");//凭证制单人                  
		sb.append(" zb_20 as documentationDate,                                                                  ");//制单日期                   
		sb.append(" zb_14 as proofNum,                                                                           ");//凭证号                    
	    sb.append("    zb_9 as transactionDepartment,                                                            ");//经办部门                   
	    sb.append("    zb_10 as transactionPerson,                                                               ");//经办人（退款责任人）             
	    sb.append("    zb_16 as refundDate,                                                                      ");//退款到期日                  
	    sb.append("    zb_13 as sourceSystem,                                                                    ");//来源系统                   
	    sb.append("    '' as tradeType,                                                                          ");//交易类型                   
	    sb.append("    '' as receiptNum,                                                                         ");//单据编号                   
	    sb.append("    '' as imageCode,                                                                          ");//影像编码                   
	    sb.append("    '' as contractCode,                                                                       ");//合同编码                   
	    sb.append("    '' as contractName,                                                                       ");//合同名称                   
	    sb.append("    '' as outBillNo,                                                                          ");//外系统单据号                 
	    sb.append("    '' as sourceRemark,                                                                       ");//来源备注                   
	    sb.append("    '' pk_org,                                                                                ");//组织pk                   
	    sb.append("    '' pk_accasoa,                                                                            ");//科目                     
	    sb.append("    '' assid,                                                                                 ");//来源备注                   
	    sb.append("    'Y' isneed                                                                                ");
		sb.append(" FROM (SELECT pubdata_table.alone_id              alone_id,                                 ");
		sb.append("              org_table.code                      org_code,                                 ");
		sb.append("              org_table.name                      org_name,                                 ");
		sb.append("              org_table.pk_reportorg              pk_org,                                   ");
		sb.append("              org_table.pk_group                  pk_group,                                 ");
		sb.append("              pubdata_table.keyword1              key1,                                     ");
		sb.append("              pubdata_table.keyword2              key2,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10008   zb_1,                                     ");
		sb.append("              iufo_measure_data_jgldhtdh.m10001   zb_2,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10012   zb_3,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10011   zb_4,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10015   zb_5,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10014   zb_6,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10010   zb_7,                                     ");
		sb.append("              iufo_measure_data_jgldhtdh.m10002   zb_8,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10002   zb_9,                                     ");
		sb.append("              iufo_measure_data_80kfvpy2.m10001   zb_10,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10009   zb_11,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10013   zb_12,                                    ");
		sb.append("              iufo_measure_data_jgldhtdh.m10003   zb_13,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10003   zb_14,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10005   zb_15,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10000   zb_16,                                    ");
		sb.append("              iufo_measure_data_jgldhtdh.m10000   zb_17,                                    ");
		sb.append("              iufo_measure_data_jgldhtdh.mn6ik8a  zb_18,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10006   zb_19,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.m10004   zb_20,                                    ");
		sb.append("              iufo_measure_data_80kfvpy2.alone_id alone_id1,                                ");
		sb.append("              iufo_measure_data_jgldhtdh.alone_id alone_id2,                                ");
		sb.append("              pubdata_table.ver                   ver                                       ");
		sb.append("         FROM iufo_measpub_GS7T pubdata_table                                               ");
		sb.append("         LEFT OUTER JOIN iufo_measure_data_80kfvpy2 ON pubdata_table.alone_id =             ");
		sb.append("                                                       iufo_measure_data_80kfvpy2.alone_id  ");
		sb.append("         LEFT OUTER JOIN iufo_measure_data_jgldhtdh ON pubdata_table.alone_id =             ");
		sb.append("                                                       iufo_measure_data_jgldhtdh.alone_id  ");
		sb.append("        INNER JOIN org_reportorg org_table ON pubdata_table.keyword1 =                      ");
		sb.append("                                              org_table.pk_reportorg                        ");
		sb.append("        WHERE (iufo_measure_data_80kfvpy2.alone_id IS NOT NULL OR                           ");
		sb.append("              iufo_measure_data_jgldhtdh.alone_id IS NOT NULL)             ");
		//添加查询条件，组织pk
		if(companyName != null){
			sb.append(" and "
					+ SQLUtil.buildSqlForIn(
							" pubdata_table.keyword2 ",
							companyName.split(",")));
		}
		//添加查询条件，制单开始日期
		if(documentationDate_begin != null){
			sb.append("and iufo_measure_data_80kfvpy2.m10006 >= '"+documentationDate_begin+"'  ");
		}
		//添加查询条件，制单结束日期
		if(documentationDate_end != null){
			sb.append("and iufo_measure_data_80kfvpy2.m10006 <= '"+documentationDate_end+"'  ");
		}		
		//期初数据很多数据为空还有一些数据不规范，除了组织条件做查询条件外其它查询条件不为空的情况不显示期初数据
		if(travellingTrader != null||transactionPerson!=null||receiptNum != null
				||contractCode != null||outBillNo != null||tradeType != null){
			sb.append(" and 1=2 ");
		}
		sb.append(" ) repdataprovider ");
		initVOs = (List<CostLedgerVO>)getBaseDAO().executeQuery(sb.toString(), new BeanListProcessor(CostLedgerVO.class));
		return initVOs;
	}
	
	
	/**@author 黄冠华
	 * 查询有余额的组织+科目
	 * 20200810
	 */
	private  List<Map<String,Object>> getAviBalOrg(String companyName) throws BusinessException {
		String[] accArr={"12210201","12210202","22410401","22410402"};
		StringBuffer sb=new StringBuffer();
		String endDate = queryValueMap.get("endDate_end");
		if(StringUtils.isBlank(endDate)){
			endDate=AppContext.getInstance().getServerTime().toLocalString();
		}
		String where_org="";
		if (StringUtils.isNotBlank(companyName)) {// 财务组织
			String[] orgstr = companyName.split(",");
			where_org=("and " + SQLUtil.buildSqlForIn("gl_balan.pk_org", orgstr));
		}
		String where_acc=("and " + SQLUtil.buildSqlForIn("account.code", accArr));
		String year=endDate.substring(0,4);
		String begMth="00";
		String endMth=endDate.substring(5, 7);
		sb.append("select pk_org,pk_accasoa,assid,balmny from ( ");
		sb.append("select sum(debitinitsum)-sum(creditinitsum)+sum(debitamountsum)-sum(creditamountsum) balmny,pk_org,pk_accasoa,assid from ( ");
	    sb.append("select                                                                                                  ");
	    sb.append("distinct                                                                                               ");
	    sb.append("         sum(gl_balan.debitamount) debitinitsum,                                                        ");
	    sb.append("         sum(gl_balan.creditamount ) creditinitsum,                                                   ");
	    sb.append("         0 debitamountsum,                                                                               ");
	    sb.append("         0 creditamountsum,                                                                              ");
	    sb.append("         gl_balan.pk_org , gl_balan.pk_accasoa,gl_balan.assid                                                                                                ");
	    sb.append("  from gl_balance gl_balan                                                                              ");
	    sb.append("  left join bd_accasoa accoa on accoa.pk_accasoa=gl_balan.pk_accasoa ");
	    sb.append("  left join bd_account account on accoa.pk_account=account.pk_account ");
	    sb.append(" where  gl_balan.year = '"+year+"' " )  ;  
	    if (StringUtils.isNotBlank(where_org)) {// 财务组织
	    	sb.append(where_org);
		}                                                                       
	    sb.append("   and gl_balan.adjustperiod >= '00'                                                                     ");
	    sb.append("   and gl_balan.adjustperiod < '01'                                                                      ");
	    sb.append("   and gl_balan.voucherkind <> 5                                                                         ");
	    sb.append(where_acc);
	    sb.append(" group by gl_balan.pk_org,gl_balan.pk_accasoa,gl_balan.assid                                                                                          ");
	    sb.append(" union                                                                                                   ");
	    sb.append("select                                                                                                  ");
	    sb.append("distinct                                                                                                 ");
	    sb.append("         0 debitinitsum,                                                                                 ");
	    sb.append("         0 creditinitsum,                                                                                ");
	    sb.append("         sum(gl_balan.debitamount) debitamountsum,                                                       ");
	    sb.append("         sum(gl_balan.creditamount ) creditamountsum,                                                    ");
	    sb.append("         gl_balan.pk_org, gl_balan.pk_accasoa,gl_balan.assid                                                                                 ");
	    sb.append("  from gl_balance gl_balan                                                                              ");
	    sb.append("  left join bd_accasoa accoa on accoa.pk_accasoa=gl_balan.pk_accasoa                                                                              ");
	    sb.append("  left join bd_account account on accoa.pk_account=account.pk_account ");
	    sb.append(" where  gl_balan.year = '"+year+"' " )  ;  
	    if (StringUtils.isNotBlank(where_org)) {// 财务组织
	    	sb.append(where_org);
		}  
	    sb.append("   and gl_balan.adjustperiod >= '01'                                                                     ");
	    sb.append("   and gl_balan.adjustperiod <= '"+endMth+"'                                                                    ");
	    sb.append("   and gl_balan.voucherkind <> 5                                                                         ");
	    sb.append(where_acc);
	    sb.append(" group by gl_balan.pk_org, gl_balan.pk_accasoa,gl_balan.assid )                                                                               ");
	    sb.append("group by pk_org,pk_accasoa,assid                                       ");
	    sb.append(") where balmny<>0 ");
	    List<Map<String,Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sb.toString(), new MapListProcessor());
		return list;
		
	}

	/**
	 * 获得时代公司层次集合VO,物业公司存在第五层次
	 * @return
	 * @throws BusinessException
	 */
	private List<ReportOrgVO> getOrgVOs() throws BusinessException{
		ReportConts reportUtils = ReportConts.getUtils();
		String pk_group = reportUtils.getorg("JT0001");// 集团主键
		Map<String, String> fatherMap = reportUtils.getFinanecMap();//获取业务单元及对应的父级单元
		List<String> regionList = reportUtils.getRegionOrgList();// 获得区域公司
		Map<String, List<String>> orgMap = reportUtils.getSecondaryOrgList(regionList);// 项目公司信息
		ReportOrgVO orgVO = new ReportOrgVO();
		orgVO.setPk_clique(pk_group);
		List<ReportOrgVO> volist = new ArrayList<ReportOrgVO>();
		for (String regionOrg : regionList) {
			String pk_plate = fatherMap.get(regionOrg);
			if(pk_plate != null){
				orgVO = (ReportOrgVO) orgVO.clone();
				orgVO.setPk_plate(pk_plate.equals(pk_group) ? regionOrg
						: pk_plate);// 板块公司
				orgVO.setPk_region(regionOrg.equals(orgVO.getPk_plate()) ? "~"
						: regionOrg);// 区域公司
				List<String> orgList = orgMap.get(regionOrg);
				if (orgList != null && orgList.size() > 0) {
					for (String org : orgList) {
						ReportOrgVO repOrgVO = (ReportOrgVO) orgVO.clone();
						repOrgVO.setPk_org(org);// 项目公司
						volist.add(repOrgVO);
					}
				} else {
					ReportOrgVO repOrgVO = (ReportOrgVO) orgVO.clone();
					repOrgVO.setPk_org(regionOrg);// 项目公司
					volist.add(repOrgVO);
				}
			}
		}
		return volist;
	}

}
