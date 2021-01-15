package nc.bs.tg.fund.rz.report.cost;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.cost.InvLedgerVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * Nc发票台账（成本报表）
 * 
 * @author ASUS
 * 
 */
public class InvLedgerUtils extends ReportUtils {
	static InvLedgerUtils utils;

	public static InvLedgerUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new InvLedgerUtils();
		}
		return utils;
	}

	public InvLedgerUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new InvLedgerVO()));
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
//			List<InvLedgerVO> list = getInvLedgerVOs();
			List<InvLedgerVO> list = getTestMainInvLedgerVOs();
			if(list != null && list.size() > 0){
				cmpreportresults = transReportResult(list);
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

//	@SuppressWarnings("unchecked")
//	private List<InvLedgerVO> getInvLedgerVOs()  throws BusinessException{
//		String companyname = queryValueMap.get("companyname");
//		String gcode = queryValueMap.get("gcode");
//		String code = queryWhereMap.get("code");
//		String cname = queryWhereMap.get("cname");
//		
//		StringBuilder sql = new StringBuilder();
//		
//		//应付单
//		sql.append("select ");
//		sql.append("org_orgs.name 		         as companyname,");//公司名称
//		sql.append("hzvat_invoice_h.sellname     as gbill,");//开票供应商
//		sql.append("hzvat_invoice_h.fph          as fnumber,");//发票号码
//		sql.append("hzvat_invoice_h.costmoneyin  as taxisamount,");//含税金额
//		sql.append("hzvat_invoice_h.costmoney    as taxnotincludedamount,");//不含税金额
//		sql.append("(hzvat_invoice_b.taxrate*0.01)      as rateamount,");//税率
//		sql.append("hzvat_invoice_h.def5      	 as taxamount,");//税额
//		sql.append("ap_payablebill.def5          as code,");//合同编号
//		sql.append("ap_payablebill.def6          as cname,");//合同名称
//		sql.append("bd_supplier.code             as gcode,");//供应商编码
//		sql.append("bd_supplier.name             as gname,");//供应商名称
//		sql.append("gl_voucher.period            as accounmonth,");//入账月份
//		sql.append("gl_voucher.num               as certificatenum,");//凭证号
//		sql.append("(case when ap_payablebill.pk_tradetype <> 'F1-Cxx-003' then ap_payablebill.def2 end) as billon,");//请款单号
//		sql.append("(case when ap_payablebill.pk_tradetype = 'F1-Cxx-003' then ap_payablebill.def2 end) as accountone ");//对账单号
//		sql.append(" from hzvat_invoice_h");
//		sql.append(" left join org_orgs on hzvat_invoice_h.pk_org = org_orgs.pk_org and org_orgs.dr = 0");
//		sql.append(" left join hzvat_invoice_b on hzvat_invoice_h.pk_invoice_h = hzvat_invoice_b.pk_invoice_h and hzvat_invoice_b.dr = 0");
//		sql.append(" left join ap_payablebill on hzvat_invoice_h.def8 = ap_payablebill.def3 and ap_payablebill.dr = 0");
//		sql.append(" left join ap_payableitem on ap_payablebill.pk_payablebill = ap_payableitem.pk_payablebill and ap_payableitem.dr = 0");
//		sql.append(" left join bd_supplier on ap_payableitem.supplier = bd_supplier.pk_supplier and bd_supplier.dr = 0");
//		sql.append(" left join fip_relation on fip_relation.src_relationid = ap_payablebill.pk_payablebill and fip_relation.dr = 0");
//		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and gl_voucher.dr = 0");
//		sql.append(" where hzvat_invoice_h.dr = 0");
//		if(null != companyname){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" org_orgs.pk_org ",companyname.split(",")));
//		}
//		if(null != gcode){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" bd_supplier.pk_supplier ",gcode.split(",")));
//		}
//		if(null != code){
//			sql.append(code.replace("code", "ap_payablebill.def5"));
//		}
//		if(null != cname){
//			sql.append(cname.replace("cname", "ap_payablebill.def6"));
//		}
//		
//		sql.append(" union ");
//		
//		//网报补票工单
//		sql.append("select ");
//		sql.append("org_orgs.name		         as companyname,");//公司名称
//		sql.append("hzvat_invoice_h.sellname     as gbill,");//开票供应商
//		sql.append("hzvat_invoice_h.fph          as fnumber,");//发票号码
//		sql.append("hzvat_invoice_h.costmoneyin  as taxisamount,");//含税金额
//		sql.append("hzvat_invoice_h.costmoney    as taxnotincludedamount,");//不含税金额
//		sql.append("(hzvat_invoice_b.taxrate*0.01)       as rateamount,");//税率
//		sql.append("hzvat_invoice_h.def5      	 as taxamount,");//税额
//		sql.append("yer_fillbill.concode         as code,");//合同编号
//		sql.append("yer_fillbill.conname         as cname,");//合同名称
//		sql.append("bd_supplier.code             as gcode,");//供应商编码
//		sql.append("bd_supplier.name             as gname,");//供应商名称
//		sql.append("gl_voucher.period            as accounmonth,");//入账月份
//		sql.append("gl_voucher.num               as certificatenum,");//凭证号
//		sql.append("yer_fillbill.billno		 	 as billon,");//请款单号
//		sql.append("''							 as accountone ");//对账单号
//		sql.append(" from hzvat_invoice_h");
//		sql.append(" left join org_orgs on hzvat_invoice_h.pk_org = org_orgs.pk_org and org_orgs.dr = 0");
//		sql.append(" left join hzvat_invoice_b on hzvat_invoice_h.pk_invoice_h = hzvat_invoice_b.pk_invoice_h and hzvat_invoice_b.dr = 0");
//		sql.append(" left join yer_fillbill on hzvat_invoice_h.def8 = yer_fillbill.imagcode and yer_fillbill.dr = 0");
//		sql.append(" left join bd_supplier on yer_fillbill.supplier = bd_supplier.pk_supplier and bd_supplier.dr = 0");
//		sql.append(" left join fip_relation on fip_relation.src_relationid = yer_fillbill.pk_addbill and fip_relation.dr = 0");
//		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and gl_voucher.dr = 0");
//		sql.append(" where hzvat_invoice_h.dr = 0");
//		if(null != companyname){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" org_orgs.pk_org ",companyname.split(",")));
//		}
//		if(null != gcode){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" bd_supplier.pk_supplier ",gcode.split(",")));
//		}
//		if(null != code){
//			sql.append(code.replace("code", "yer_fillbill.concode"));
//		}
//		if(null != cname){
//			sql.append(cname.replace("cname", "yer_fillbill.conname"));
//		}
//		
//		sql.append(" union ");
//		//报销单
//		sql.append("select ");
//		sql.append("org_orgs.name		         as companyname,");//公司名称
//		sql.append("hzvat_invoice_h.sellname     as gbill,");//开票供应商
//		sql.append("hzvat_invoice_h.fph          as fnumber,");//发票号码
//		sql.append("hzvat_invoice_h.costmoneyin  as taxisamount,");//含税金额
//		sql.append("hzvat_invoice_h.costmoney    as taxnotincludedamount,");//不含税金额
//		sql.append("(hzvat_invoice_b.taxrate*0.01)       as rateamount,");//税率
//		sql.append("hzvat_invoice_h.def5      	 as taxamount,");//税额
//		sql.append("er_bxzb.zyx2         		 as code,");//合同编号
//		sql.append("yer_contractfile.name        as cname,");//合同名称
//		sql.append("bd_supplier.code             as gcode,");//供应商编码
//		sql.append("bd_supplier.name             as gname,");//供应商名称
//		sql.append("gl_voucher.period            as accounmonth,");//入账月份
//		sql.append("gl_voucher.num               as certificatenum,");//凭证号
//		sql.append("er_bxzb.djbh		 		 as billon,");//请款单号
//		sql.append("''							 as accountone ");//对账单号
//		sql.append(" from hzvat_invoice_h");
//		sql.append(" left join org_orgs on hzvat_invoice_h.pk_org = org_orgs.pk_org and org_orgs.dr = 0");
//		sql.append(" left join hzvat_invoice_b on hzvat_invoice_h.pk_invoice_h = hzvat_invoice_b.pk_invoice_h and hzvat_invoice_b.dr = 0");
//		sql.append(" left join er_bxzb on hzvat_invoice_h.def8 = er_bxzb.zyx16 and er_bxzb.dr = 0");
//		sql.append(" left join yer_contractfile on yer_contractfile.pk_defdoc = er_bxzb.zyx9 and yer_contractfile.dr = 0 and yer_contractfile.def15 != '0'");
//		sql.append(" left join bd_supplier on er_bxzb.hbbm = bd_supplier.pk_supplier and bd_supplier.dr = 0");
//		sql.append(" left join fip_relation on fip_relation.src_relationid = er_bxzb.pk_jkbx and fip_relation.dr = 0");
//		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and gl_voucher.dr = 0");
//		sql.append(" where hzvat_invoice_h.dr = 0");
//		if(null != companyname){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" org_orgs.pk_org ",companyname.split(",")));
//		}
//		if(null != gcode){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" bd_supplier.pk_supplier ",gcode.split(",")));
//		}
//		if(null != code){
//			sql.append(code.replace("code", "er_bxzb.zyx2"));
//		}
//		if(null != cname){
//			sql.append(cname.replace("cname", "yer_contractfile.name"));
//		}
//		
//		sql.append(" union ");
//		
//		//请款单
//		sql.append("select ");
//		sql.append("org_orgs.name		         as companyname,");//公司名称
//		sql.append("hzvat_invoice_h.sellname     as gbill,");//开票供应商
//		sql.append("hzvat_invoice_h.fph          as fnumber,");//发票号码
//		sql.append("hzvat_invoice_h.costmoneyin  as taxisamount,");//含税金额
//		sql.append("hzvat_invoice_h.costmoney    as taxnotincludedamount,");//不含税金额
//		sql.append("(hzvat_invoice_b.taxrate*0.01)       as rateamount,");//税率
//		sql.append("hzvat_invoice_h.def5      	 as taxamount,");//税额
//		sql.append("cdm_contract.contractcode    as code,");//合同编号
//		sql.append("cdm_contract.htmc	       	 as cname,");//合同名称
//		sql.append("bd_supplier.code             as gcode,");//供应商编码
//		sql.append("bd_supplier.name             as gname,");//供应商名称
//		sql.append("gl_voucher.period            as accounmonth,");//入账月份
//		sql.append("gl_voucher.num               as certificatenum,");//凭证号
//		sql.append("tgrz_financexpense.billno	 as billon,");//请款单号
//		sql.append("''							 as accountone ");//对账单号
//		sql.append(" from hzvat_invoice_h");
//		sql.append(" left join org_orgs on hzvat_invoice_h.pk_org = org_orgs.pk_org and org_orgs.dr = 0");
//		sql.append(" left join hzvat_invoice_b on hzvat_invoice_h.pk_invoice_h = hzvat_invoice_b.pk_invoice_h and hzvat_invoice_b.dr = 0");
//		sql.append(" left join tgrz_financexpense on hzvat_invoice_h.def8 = tgrz_financexpense.def21 and tgrz_financexpense.dr = 0");
//		sql.append(" left join cdm_contract on cdm_contract.pk_contract = tgrz_financexpense.def4 and cdm_contract.dr = 0 ");
//		sql.append(" left join bd_supplier on tgrz_financexpense.pk_payee = bd_supplier.pk_supplier and bd_supplier.dr = 0");
//		sql.append(" left join fip_relation on fip_relation.src_relationid = tgrz_financexpense.pk_finexpense and fip_relation.dr = 0");
//		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and gl_voucher.dr = 0");
//		sql.append(" where hzvat_invoice_h.dr = 0");
//		if(null != companyname){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" org_orgs.pk_org ",companyname.split(",")));
//		}
//		if(null != gcode){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" bd_supplier.pk_supplier ",gcode.split(",")));
//		}
//		if(null != code){
//			sql.append(code.replace("code", "cdm_contract.contractcode"));
//		}
//		if(null != cname){
//			sql.append(cname.replace("cname", "cdm_contract.htmc"));
//		}
//		
//		sql.append(" union ");
//
//		//还款单
//		sql.append("select ");
//		sql.append("org_orgs.name		         as companyname,");//公司名称
//		sql.append("hzvat_invoice_h.sellname     as gbill,");//开票供应商
//		sql.append("hzvat_invoice_h.fph          as fnumber,");//发票号码
//		sql.append("hzvat_invoice_h.costmoneyin  as taxisamount,");//含税金额
//		sql.append("hzvat_invoice_h.costmoney    as taxnotincludedamount,");//不含税金额
//		sql.append("(hzvat_invoice_b.taxrate*0.01)       as rateamount,");//税率
//		sql.append("hzvat_invoice_h.def5      	 as taxamount,");//税额
//		sql.append("cdm_contract.contractcode    as code,");//合同编号
//		sql.append("cdm_contract.htmc	       	 as cname,");//合同名称
//		sql.append("bd_supplier.code             as gcode,");//供应商编码
//		sql.append("bd_supplier.name             as gname,");//供应商名称
//		sql.append("gl_voucher.period            as accounmonth,");//入账月份
//		sql.append("gl_voucher.num               as certificatenum,");//凭证号
//		sql.append("cdm_repayrcpt.vbillno	 	 as billon,");//请款单号
//		sql.append("''							 as accountone ");//对账单号
//		sql.append(" from hzvat_invoice_h");
//		sql.append(" left join org_orgs on hzvat_invoice_h.pk_org = org_orgs.pk_org and org_orgs.dr = 0");
//		sql.append(" left join hzvat_invoice_b on hzvat_invoice_h.pk_invoice_h = hzvat_invoice_b.pk_invoice_h and hzvat_invoice_b.dr = 0");
//		sql.append(" left join cdm_repayrcpt on hzvat_invoice_h.def8 = cdm_repayrcpt.def21 and cdm_repayrcpt.dr = 0");
//		sql.append(" left join cdm_contract on cdm_contract.pk_contract = cdm_repayrcpt.pk_contract and cdm_contract.dr = 0 ");
//		sql.append(" left join bd_supplier on cdm_repayrcpt.reunit = bd_supplier.pk_supplier and bd_supplier.dr = 0");
//		sql.append(" left join fip_relation on fip_relation.src_relationid = cdm_repayrcpt.pk_repayrcpt and fip_relation.dr = 0");
//		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and gl_voucher.dr = 0");
//		sql.append(" where hzvat_invoice_h.dr = 0");
//		if(null != companyname){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" org_orgs.pk_org ",companyname.split(",")));
//		}
//		if(null != gcode){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" bd_supplier.pk_supplier ",gcode.split(",")));
//		}
//		if(null != code){
//			sql.append(code.replace("code", "cdm_contract.contractcode"));
//		}
//		if(null != cname){
//			sql.append(cname.replace("cname", "cdm_contract.htmc"));
//		}
//		
//		sql.append(" union ");
//		
//		//融资补票工单
//		sql.append("select ");
//		sql.append("org_orgs.name       	 	 as companyname,");//公司名称
//		sql.append("hzvat_invoice_h.sellname     as gbill,");//开票供应商
//		sql.append("hzvat_invoice_h.fph          as fnumber,");//发票号码
//		sql.append("hzvat_invoice_h.costmoneyin  as taxisamount,");//含税金额
//		sql.append("hzvat_invoice_h.costmoney    as taxnotincludedamount,");//不含税金额
//		sql.append("(hzvat_invoice_b.taxrate*0.01)      as rateamount,");//税率
//		sql.append("hzvat_invoice_h.def5      	 as taxamount,");//税额
//		sql.append("cdm_contract.contractcode	 as code,");//合同编号
//		sql.append("tg_addTicket.def18	       	 as cname,");//合同名称
//		sql.append("''             				 as gcode,");//供应商编码
//		sql.append("''              			 as gname,");//供应商名称
//		sql.append("gl_voucher.period            as accounmonth,");//入账月份
//		sql.append("gl_voucher.num               as certificatenum,");//凭证号
//		sql.append("(case when tg_ticketbody.def1 is not null then tg_ticketbody.def1 end) as billon,");//请款单号
//		sql.append("(case when tg_ticketbody.def1 is null then tg_ticketbody.def9 end) as accountone ");//对账单号
//		sql.append(" from hzvat_invoice_h");
//		sql.append(" left join org_orgs on hzvat_invoice_h.pk_org = org_orgs.pk_org and org_orgs.dr = 0");
//		sql.append(" left join hzvat_invoice_b on hzvat_invoice_h.pk_invoice_h = hzvat_invoice_b.pk_invoice_h and hzvat_invoice_b.dr = 0");
//		sql.append(" left join tg_addTicket on hzvat_invoice_h.def8 = tg_addTicket.def21 and tg_addTicket.dr = 0");
//		sql.append(" left join tg_ticketbody on tg_ticketbody.pk_ticket = tg_addTicket.pk_ticket and tg_ticketbody.dr = 0");
//		sql.append(" left join cdm_contract on tg_addTicket.def2 = cdm_contract.pk_contract and cdm_contract.dr = 0");
//		sql.append(" left join fip_relation on fip_relation.src_relationid = tg_addTicket.pk_ticket and fip_relation.dr = 0");
//		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and gl_voucher.dr = 0");
//		sql.append(" where hzvat_invoice_h.dr = 0");
//		if(null != companyname){
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							" org_orgs.pk_org ",companyname.split(",")));
//		}
////		if(null != gcode){
////			sql.append(" and "
////					+ SQLUtil.buildSqlForIn(
////							" bd_supplier.pk_supplier ",gcode.split(",")));
////		}融资补票工单没有供应商
//		if(null != code){
//			sql.append(code.replace("code", "cdm_contract.contractcode"));
//		}
//		if(null != cname){
//			sql.append(cname.replace("cname", "cdm_contract.htmc"));
//		}
//		
//		List<InvLedgerVO> invLedgerVOs = (List<InvLedgerVO>)getBaseDAO().executeQuery(sql.toString(), new BeanListProcessor(InvLedgerVO.class));
//		return invLedgerVOs;
//	}
	
	private List<InvLedgerVO> getTestMainInvLedgerVOs()  throws BusinessException{
		List<InvLedgerVO> invMainList = getInvMainList();
		Set<String> keySet = new HashSet<String>();
		List<InvLedgerVO> integrationList = new ArrayList<InvLedgerVO>();
		//应付单信息map
		Map<String, List<Map<String, Object>>> payableMap = null;
		//网报工单信息map
		Map<String, List<Map<String, Object>>> actualMap = null;
		//报销单信息map
		Map<String, List<Map<String, Object>>> bxzbMap = null;
		//请款单信息map
		Map<String, List<Map<String, Object>>> finMap = null;
		//还款单信息map
		Map<String, List<Map<String, Object>>> repayMap = null;
		//融资补票单信息map
		Map<String, List<Map<String, Object>>> addticketMap = null;
		if(invMainList.size()>0){
			for (InvLedgerVO invLedgerVO : invMainList) {
				keySet.add(invLedgerVO.getImagecode());
			}
		}
		if (keySet != null && keySet.size() > 0) {
			//读取应付单信息
			payableMap = getPayableBillMap(keySet);
			//读取网报工单信息
			actualMap = getWebBillMap(keySet);
			//读取报销单信息
			bxzbMap = getBXBillMap(keySet);
			//读取请款单信息
			finMap =  getFinBillMap(keySet);
			//读取还款单信息
			repayMap = getRepayBillMap(keySet);
			//读取融资补票单信息
			addticketMap = getAddticketBillMap(keySet);
		}
		if(invMainList!=null&&invMainList.size()>0){
			for (InvLedgerVO invLedgerVO : invMainList) {
				if(payableMap.containsKey(invLedgerVO.getImagecode())){
					List<InvLedgerVO> payableBillList = setBillList(invLedgerVO,payableMap.get(invLedgerVO.getImagecode()));
					integrationList.addAll(payableBillList);
				}
				if (actualMap.containsKey(invLedgerVO.getImagecode())) {
					List<InvLedgerVO> webBillList = setBillList(invLedgerVO,actualMap.get(invLedgerVO.getImagecode()));
					integrationList.addAll(webBillList);
				}
				if (bxzbMap.containsKey(invLedgerVO.getImagecode())) {
					List<InvLedgerVO> bxBillList = setBillList(invLedgerVO,bxzbMap.get(invLedgerVO.getImagecode()));
					integrationList.addAll(bxBillList);
				}
				if (finMap.containsKey(invLedgerVO.getImagecode())) {
					List<InvLedgerVO> finBillList = setBillList(invLedgerVO,finMap.get(invLedgerVO.getImagecode()));
					integrationList.addAll(finBillList);
				}
				if(repayMap.containsKey(invLedgerVO.getImagecode())){
					List<InvLedgerVO> repayBillList = setBillList(invLedgerVO,repayMap.get(invLedgerVO.getImagecode()));
					integrationList.addAll(repayBillList);
				}
				if(addticketMap.containsKey(invLedgerVO.getImagecode())){
					List<InvLedgerVO> addticketBillList = setBillList(invLedgerVO,addticketMap.get(invLedgerVO.getImagecode()));
					integrationList.addAll(addticketBillList);
				}
			}
		}
		return integrationList;
	}

	/**
	 * 获取融资补票单信息
	 * @param keySet
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, List<Map<String, Object>>> getAddticketBillMap(
			Set<String> keySet) throws BusinessException {
		
		String code = queryWhereMap.get("code");
		String cname = queryWhereMap.get("cname");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append("tg_addTicket.def21 as imagcode,");//影像编码
		sql.append("cdm_contract.contractcode	 as code,");//合同编号
		sql.append("tg_addTicket.def18	       	 as cname,");//合同名称
		sql.append("''             				 as gcode,");//供应商编码
		sql.append("''              			 as gname,");//供应商名称
		sql.append("gl_voucher.period            as accounmonth,");//入账月份
		sql.append("gl_voucher.num               as certificatenum,");//凭证号
		sql.append("(case when tg_ticketbody.def1 is not null then tg_ticketbody.def1 end) as billon,");//请款单号
		sql.append("(case when tg_ticketbody.def1 is null then tg_ticketbody.def9 end) as accountone ");//对账单号
		sql.append(" from tg_addTicket ");
		sql.append(" left join tg_ticketbody on tg_ticketbody.pk_ticket = tg_addTicket.pk_ticket and nvl(tg_ticketbody.dr,0) = 0");
		sql.append(" left join cdm_contract on tg_addTicket.def2 = cdm_contract.pk_contract and nvl(cdm_contract.dr,0) = 0");
		sql.append(" left join fip_relation on fip_relation.src_relationid = tg_addTicket.pk_ticket and nvl(fip_relation.dr,0) = 0");
		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(gl_voucher.dr,0) = 0");
		sql.append(" where nvl(tg_addTicket.dr,0) = 0 ");
		
		if(null != code){
			sql.append(code.replace("code", "cdm_contract.contractcode"));
		}
		if(null != cname){
			sql.append(cname.replace("cname", "cdm_contract.htmc"));
		}
		
		sql.append(" and " +
				SQLUtil.buildSqlForIn("tg_addTicket.def21",
						keySet.toArray(new String[0])));
		
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, List<java.util.Map<String, Object>>> infoMap = new HashMap<String, List<java.util.Map<String, Object>>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> info : list) {
				String imagcode = (String) info.get("imagcode");
				if (!infoMap.containsKey(imagcode)) {
					infoMap.put(imagcode, new ArrayList<Map<String, Object>>());
				}
				infoMap.get(imagcode).add(info);
			}

		}
		return infoMap;
	}

	/**
	 * 获取应付单信息
	 * @param keySet
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, List<Map<String, Object>>> getPayableBillMap(
			Set<String> keySet) throws BusinessException {
		
		String gcode = queryValueMap.get("gcode");
		String code = queryWhereMap.get("code");
		String cname = queryWhereMap.get("cname");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct ");
		sql.append("ap_payablebill.def3 as imagcode,");//影像编码
		sql.append("ap_payablebill.def5          as code,");//合同编号
		sql.append("ap_payablebill.def6          as cname,");//合同名称
		sql.append("bd_supplier.code             as gcode,");//供应商编码
		sql.append("bd_supplier.name             as gname,");//供应商名称
		sql.append("gl_voucher.period            as accounmonth,");//入账月份
		sql.append("gl_voucher.num               as certificatenum,");//凭证号
		sql.append("(case when ap_payablebill.pk_tradetype <> 'F1-Cxx-003' then ap_payablebill.def2 end) as billon,");//请款单号
		sql.append("(case when ap_payablebill.pk_tradetype = 'F1-Cxx-003' then ap_payablebill.def2 end) as accountone ");//对账单号
		sql.append(" from ap_payablebill ");
		sql.append(" left join ap_payableitem on ap_payablebill.pk_payablebill = ap_payableitem.pk_payablebill and nvl(ap_payableitem.dr,0) = 0");
		sql.append(" left join bd_supplier on ap_payableitem.supplier = bd_supplier.pk_supplier and nvl(bd_supplier.dr,0) = 0");
		sql.append(" left join fip_relation on fip_relation.src_relationid = ap_payablebill.pk_payablebill and nvl(fip_relation.dr,0) = 0");
		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(gl_voucher.dr,0) = 0");
		sql.append(" where nvl(ap_payablebill.dr,0) = 0 ");
		
		if(null != gcode){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_supplier.pk_supplier ",gcode.split(",")));
		}
		if(null != code){
			sql.append(code.replace("code", "ap_payablebill.def5"));
		}
		if(null != cname){
			sql.append(cname.replace("cname", "ap_payablebill.def6"));
		}
		
		sql.append(" and " +
				SQLUtil.buildSqlForIn("ap_payablebill.def3",
						keySet.toArray(new String[0])));
		
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, List<java.util.Map<String, Object>>> infoMap = new HashMap<String, List<java.util.Map<String, Object>>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> info : list) {
				String imagcode = (String) info.get("imagcode");
				if (!infoMap.containsKey(imagcode)) {
					infoMap.put(imagcode, new ArrayList<Map<String, Object>>());
				}
				infoMap.get(imagcode).add(info);
			}

		}
		return infoMap;
	}

	/**
	 * 获取还款单信息
	 * @param keySet
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, List<Map<String, Object>>> getRepayBillMap(
			Set<String> keySet) throws BusinessException {
		
		String gcode = queryValueMap.get("gcode");
		String code = queryWhereMap.get("code");
		String cname = queryWhereMap.get("cname");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append("cdm_repayrcpt.def21    as imagcode,");//影像编码
		sql.append("cdm_contract.contractcode    as code,");//合同编号
		sql.append("cdm_contract.htmc	       	 as cname,");//合同名称
		sql.append("bd_supplier.code             as gcode,");//供应商编码
		sql.append("bd_supplier.name             as gname,");//供应商名称
		sql.append("gl_voucher.period            as accounmonth,");//入账月份
		sql.append("gl_voucher.num               as certificatenum,");//凭证号
		sql.append("cdm_repayrcpt.vbillno	 	 as billon,");//请款单号
		sql.append("''							 as accountone ");//对账单号
		sql.append(" from cdm_repayrcpt ");
		sql.append(" left join cdm_contract on cdm_contract.pk_contract = cdm_repayrcpt.pk_contract and nvl(cdm_contract.dr,0) = 0 ");
		sql.append(" left join bd_supplier on cdm_repayrcpt.reunit = bd_supplier.pk_supplier and nvl(bd_supplier.dr,0) = 0");
		sql.append(" left join fip_relation on fip_relation.src_relationid = cdm_repayrcpt.pk_repayrcpt and nvl(fip_relation.dr,0) = 0");
		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(gl_voucher.dr,0) = 0");
		sql.append(" where nvl(cdm_repayrcpt.dr,0) = 0 ");
		if(null != gcode){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_supplier.pk_supplier ",gcode.split(",")));
		}
		if(null != code){
			sql.append(code.replace("code", "cdm_contract.contractcode"));
		}
		if(null != cname){
			sql.append(cname.replace("cname", "cdm_contract.htmc"));
		}
		
		sql.append(" and " +
				SQLUtil.buildSqlForIn("cdm_repayrcpt.def21",
						keySet.toArray(new String[0])));
		
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, List<java.util.Map<String, Object>>> infoMap = new HashMap<String, List<java.util.Map<String, Object>>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> info : list) {
				String imagcode = (String) info.get("imagcode");
				if (!infoMap.containsKey(imagcode)) {
					infoMap.put(imagcode, new ArrayList<Map<String, Object>>());
				}
				infoMap.get(imagcode).add(info);
			}

		}
		return infoMap;
	}

	/**
	 * 获取请款单信息
	 * @param keySet
	 * @return
	 * @throws BusinessException 
	 */
	private Map<String, List<Map<String, Object>>> getFinBillMap(
			Set<String> keySet) throws BusinessException {
		
		String gcode = queryValueMap.get("gcode");
		String code = queryWhereMap.get("code");
		String cname = queryWhereMap.get("cname");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append("tgrz_financexpense.def21    as imagcode,");//影像编码
		sql.append("cdm_contract.contractcode    as code,");//合同编号
		sql.append("cdm_contract.htmc	       	 as cname,");//合同名称
		sql.append("bd_supplier.code             as gcode,");//供应商编码
		sql.append("bd_supplier.name             as gname,");//供应商名称
		sql.append("gl_voucher.period            as accounmonth,");//入账月份
		sql.append("gl_voucher.num               as certificatenum,");//凭证号
		sql.append("tgrz_financexpense.billno	 as billon,");//请款单号
		sql.append("''							 as accountone ");//对账单号
		sql.append(" from tgrz_financexpense ");
		sql.append(" left join cdm_contract on cdm_contract.pk_contract = tgrz_financexpense.def4 and nvl(cdm_contract.dr,0) = 0 ");
		sql.append(" left join bd_supplier on tgrz_financexpense.pk_payee = bd_supplier.pk_supplier and nvl(bd_supplier.dr,0) = 0");
		sql.append(" left join fip_relation on fip_relation.src_relationid = tgrz_financexpense.pk_finexpense and nvl(fip_relation.dr,0) = 0");
		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(gl_voucher.dr,0) = 0");
		sql.append(" where nvl(tgrz_financexpense.dr,0) = 0 ");
		
		if(null != gcode){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_supplier.pk_supplier ",gcode.split(",")));
		}
		if(null != code){
			sql.append(code.replace("code", "cdm_contract.contractcode"));
		}
		if(null != cname){
			sql.append(cname.replace("cname", "cdm_contract.htmc"));
		}
		
		sql.append(" and " +
				SQLUtil.buildSqlForIn("tgrz_financexpense.def21",
						keySet.toArray(new String[0])));
		
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, List<java.util.Map<String, Object>>> infoMap = new HashMap<String, List<java.util.Map<String, Object>>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> info : list) {
				String imagcode = (String) info.get("imagcode");
				if (!infoMap.containsKey(imagcode)) {
					infoMap.put(imagcode, new ArrayList<Map<String, Object>>());
				}
				infoMap.get(imagcode).add(info);
			}

		}
		return infoMap;
	}

	/**
	 * 读取报销单信息
	 * @param keySet
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, List<Map<String, Object>>> getBXBillMap(
			Set<String> keySet) throws BusinessException {
		
		String gcode = queryValueMap.get("gcode");
		String code = queryWhereMap.get("code");
		String cname = queryWhereMap.get("cname");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append("er_bxzb.zyx16         		 as imagcode,");//影像编码
		sql.append("er_bxzb.zyx2         		 as code,");//合同编号
		sql.append("yer_contractfile.name        as cname,");//合同名称
		sql.append("bd_supplier.code             as gcode,");//供应商编码
		sql.append("bd_supplier.name             as gname,");//供应商名称
		sql.append("gl_voucher.period            as accounmonth,");//入账月份
		sql.append("gl_voucher.num               as certificatenum,");//凭证号
		sql.append("er_bxzb.djbh		 		 as billon,");//请款单号
		sql.append("''							 as accountone ");//对账单号
		sql.append(" from er_bxzb ");
		sql.append(" left join yer_contractfile on yer_contractfile.pk_defdoc = er_bxzb.zyx9 and nvl(yer_contractfile.dr,0) = 0 and yer_contractfile.def15 != '0'");
		sql.append(" left join bd_supplier on er_bxzb.hbbm = bd_supplier.pk_supplier and nvl(bd_supplier.dr,0) = 0");
		sql.append(" left join fip_relation on fip_relation.src_relationid = er_bxzb.pk_jkbx and nvl(fip_relation.dr,0) = 0");
		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(gl_voucher.dr,0) = 0");
		sql.append(" where nvl(er_bxzb.dr,0) = 0 ");
		
		if(null != gcode){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_supplier.pk_supplier ",gcode.split(",")));
		}
		if(null != code){
			sql.append(code.replace("code", "er_bxzb.zyx2"));
		}
		if(null != cname){
			sql.append(cname.replace("cname", "yer_contractfile.name"));
		}
		
		sql.append(" and " +
		SQLUtil.buildSqlForIn("er_bxzb.zyx16",
				keySet.toArray(new String[0])));
		
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, List<java.util.Map<String, Object>>> infoMap = new HashMap<String, List<java.util.Map<String, Object>>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> info : list) {
				String imagcode = (String) info.get("imagcode");
				if (!infoMap.containsKey(imagcode)) {
					infoMap.put(imagcode, new ArrayList<Map<String, Object>>());
				}
				infoMap.get(imagcode).add(info);
			}

		}
		return infoMap;
	}

	/**
	 * 设置业务单据值
	 * @param invLedgerVO
	 * @param list
	 * @return
	 */
	private List<InvLedgerVO> setBillList(InvLedgerVO invLedgerVO,
			List<Map<String, Object>> list) {
		InvLedgerVO coloneVO = (InvLedgerVO) invLedgerVO.clone();
		List<InvLedgerVO> listvo = new ArrayList<InvLedgerVO>();
		for (Map<String, Object> map : list) {
			coloneVO.setCode(map.get("code")==null?null:map.get("code").toString());//合同编码
			coloneVO.setCname(map.get("cname")==null?null:map.get("cname").toString());//合同名称
			coloneVO.setGcode(map.get("gcode")==null?null:map.get("gcode").toString());//供应商编码
			coloneVO.setGname(map.get("gname")==null?null:map.get("gname").toString());//供应商名称
			coloneVO.setAccounmonth(map.get("accounmonth")==null?null:map.get("accounmonth").toString());//入账月份
			coloneVO.setCertificatenum(map.get("certificatenum")==null?null:map.get("certificatenum").toString());//凭证号
			coloneVO.setBillon(map.get("billon")==null?null:map.get("billon").toString());//请款单号
			coloneVO.setAccountone(map.get("accountone")==null?null:map.get("accountone").toString());//对账单号
			listvo.add(coloneVO);
		}
		return listvo;
	}

	/**
	 * 获取网报工单发票信息
	 * @param keySet
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, List<Map<String, Object>>> getWebBillMap(Set<String> keySet) throws BusinessException {
		
		String gcode = queryValueMap.get("gcode");
		String code = queryWhereMap.get("code");
		String cname = queryWhereMap.get("cname");
		
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("yer_fillbill.imagcode imagcode, ");//影像编码
		sql.append("yer_fillbill.concode         as code,");//合同编号
		sql.append("yer_fillbill.conname         as cname,");//合同名称
		sql.append("bd_supplier.code             as gcode,");//供应商编码
		sql.append("bd_supplier.name             as gname,");//供应商名称
		sql.append("gl_voucher.period            as accounmonth,");//入账月份
		sql.append("gl_voucher.num               as certificatenum,");//凭证号
		sql.append("yer_fillbill.billno		 	 as billon,");//请款单号
		sql.append("''							 as accountone ");//对账单号
		sql.append(" from  yer_fillbill ");
		sql.append(" left join bd_supplier on yer_fillbill.supplier = bd_supplier.pk_supplier and nvl(bd_supplier.dr,0) = 0");
		sql.append(" left join fip_relation on fip_relation.src_relationid = yer_fillbill.pk_addbill and nvl(fip_relation.dr,0) = 0");
		sql.append(" left join gl_voucher on fip_relation.des_relationid = gl_voucher.pk_voucher and nvl(gl_voucher.dr,0) = 0");
		sql.append(" where nvl(yer_fillbill.dr,0) = 0 ");
		
		//供应商
		if(null != gcode){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" bd_supplier.pk_supplier ",gcode.split(",")));
		}
		//合同编号
		if(null != code){
			sql.append(code.replace("code", "yer_fillbill.concode"));
		}
		//合同名称
		if(null != cname){
			sql.append(cname.replace("cname", "yer_fillbill.conname"));
		}
		
		sql.append(" and " +
		SQLUtil.buildSqlForIn("yer_fillbill.imagcode",
				keySet.toArray(new String[0])));
		
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, List<java.util.Map<String, Object>>> infoMap = new HashMap<String, List<java.util.Map<String, Object>>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> info : list) {
				String imagcode = (String) info.get("imagcode");
				if (!infoMap.containsKey(imagcode)) {
					infoMap.put(imagcode, new ArrayList<Map<String, Object>>());
				}
				infoMap.get(imagcode).add(info);
			}

		}
		return infoMap;
	}

	/**
	 * 获取发票主体信息
	 * @return
	 * @throws BusinessException
	 */
	private List<InvLedgerVO> getInvMainList() throws BusinessException {
		
		String companyname = queryValueMap.get("companyname");//公司
		
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("hzvat_invoice_h.def8 as imagecode,");//发票影像编码
		sql.append("org_orgs.name 		         as companyname,");//公司名称
		sql.append("hzvat_invoice_h.sellname     as gbill,");//开票供应商
		sql.append("hzvat_invoice_h.fph          as fnumber,");//发票号码
		sql.append("hzvat_invoice_h.costmoneyin  as taxisamount,");//含税金额
		sql.append("hzvat_invoice_h.costmoney    as taxnotincludedamount,");//不含税金额
		sql.append("(hzvat_invoice_b.taxrate*0.01)      as rateamount,");//税率
		sql.append("hzvat_invoice_h.def5      	 as taxamount ");//税额
		sql.append(" from hzvat_invoice_h");
		sql.append(" left join org_orgs on hzvat_invoice_h.pk_org = org_orgs.pk_org and org_orgs.dr = 0");
		sql.append(" left join hzvat_invoice_b on hzvat_invoice_h.pk_invoice_h = hzvat_invoice_b.pk_invoice_h and hzvat_invoice_b.dr = 0");
		sql.append(" where nvl(hzvat_invoice_h.dr,0) = 0 ");
		//公司
		if(null != companyname){
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							" org_orgs.pk_org ",companyname.split(",")));
		}
		List<InvLedgerVO> invLedgerVOs = (List<InvLedgerVO>)getBaseDAO().executeQuery(sql.toString(), new BeanListProcessor(InvLedgerVO.class));
		return invLedgerVOs;
	}

}