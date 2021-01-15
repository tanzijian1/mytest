package nc.bs.tg.zhsc.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.call.vo.PayableBodyVO;
import nc.bs.tg.call.vo.PayableHeadVO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;


public class ZhscPaybillConvertUtils{
	

public static final String DefaultOperator = "LLZHSC";// 默认制单人
	
	private static ZhscPaybillConvertUtils convertUtils;
	
	public static ZhscPaybillConvertUtils getInstance() {
		if (convertUtils == null) {
			convertUtils = new ZhscPaybillConvertUtils();
		}
		return convertUtils;
	}
	
	BaseDAO baseDAO = null;
	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	
	public AggPayableBillVO onTranBill(PayableHeadVO headVO,
			List<PayableBodyVO> bodyVOs) throws BusinessException{
		
		AggPayableBillVO aggvo  = new AggPayableBillVO();
		PayableBillVO save_headVO = new PayableBillVO();
		
		
		/**
		 * 综合商城传过来的表头字段
		 */
		String pk_org = getPk_orgByCode(headVO.getPk_org());
		if(pk_org != null){
			save_headVO.setPk_org(pk_org);// 财务组织
		}else {
			throw new BusinessException("综合商城同步NC财务系统应付单："+ headVO.getSrcid() +" 的财务组织在NC财务系统中不存在！财务组织："+headVO.getPk_org());
		}
		
		save_headVO.setAttributeValue("billdate", new UFDate(headVO.getBilldate()));// 单据日期
		save_headVO.setAttributeValue("money", headVO.getMoney());// 原币金额
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// 外系统主键
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// 外系统单号
		save_headVO.setAttributeValue("local_money", headVO.getMoney());// 组织本币金额
		save_headVO.setAttributeValue("rate", 1);// 组织本币汇率
		save_headVO.setAttributeValue("def35", headVO.getHyperlinks());//综合商城附件超链接
		save_headVO.setAttributeValue("pk_tradetype", headVO.getPk_tradetype());// 应收类型code
		save_headVO.setAttributeValue("pk_tradetypeid",getPkTradeTypeid(save_headVO.getPk_tradetype()));// 应收类型
		save_headVO.setAttributeValue("def36", headVO.getBusinesstype());// 业务类型
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
		save_headVO.setAttributeValue("billstatus", 2);// 单据状态,默认为审批中
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团，默认为时代集团
		save_headVO.setAttributeValue("pk_billtype", "F1");// 单据类型编码
		save_headVO.setAttributeValue("billclass", "yf");// 单据大类,默认为应收单
		save_headVO.setAttributeValue("syscode", 1);// 单据所属系统，默认为1，1=应付系统
		save_headVO.setAttributeValue("billmaker", getUserPkByCode(DefaultOperator));// 制单人，默认为邻里综合商城系统
		save_headVO.setAttributeValue("objtype", 1); //往来对象，默认为1=供应商
		save_headVO.setAttributeValue("src_syscode", 1);// 单据来源系统
		save_headVO.setAttributeValue("creationtime", new UFDateTime());
		

		/*save_headVO.setAttributeValue("billyear", null);// 单据会计年度
		save_headVO.setAttributeValue("billperiod", null);// 单据会计期间
		save_headVO.setAttributeValue("scomment", null);// 摘要
		save_headVO.setAttributeValue("effectstatus", 0);// 生效状态
		save_headVO.setAttributeValue("effectuser", null);// 生效人
		save_headVO.setAttributeValue("effectdate", null);// 生效日期
		save_headVO.setAttributeValue("lastapproveid", null);// 最终审批人
		save_headVO.setAttributeValue("grouplocal", null);// 集团本币金额
		save_headVO.setAttributeValue("globallocal", null);// 全局本币金额

		save_headVO.setAttributeValue("approver", null);// 审核人
		save_headVO.setAttributeValue("approvedate", null);// 审核时间
		save_headVO.setAttributeValue("lastadjustuser", null);*/	// 最终调整人
		//save_headVO.setAttributeValue("pk_busitype", "1001341000000057FW29");// 业务流程
		//save_headVO.setAttributeValue("coordflag", null);// 单据协同标志
		//save_headVO.setAttributeValue("taxcountryid", null);// 报税国
		save_headVO.setAttributeValue("creator",getUserPkByCode(DefaultOperator));// 创建人
		
		save_headVO.setAttributeValue("approvestatus", 3);// 审批状态
		
		save_headVO.setStatus(VOStatus.NEW);
		
		UFDouble headMoney = new UFDouble(headVO.getMoney());
		UFDouble bodyTotalMoney = new UFDouble(0);
		
		String payaccount = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCYFFKZH");
		String payaccountpk = null;
		if(StringUtils.isNotBlank(payaccount)){
			payaccountpk = getPayBankAccountPK(payaccount);
			if(payaccountpk == null){
				throw new BusinessException("综合商城同步NC财务系统应付单："+ headVO.getSrcid() +" 的付款银行账户在NC财务系统中不存在！付款银行账户:"+payaccount);
			}
		}else {
			throw new BusinessException("综合商城同步NC财务系统应付单："+ headVO.getSrcid() +" 的付款银行账户在NC财务系统中不存在！");
		}
		
		/**
		 * 综合商城报文传来的字段
		 */
		List<PayableBillItemVO> bodylist = new ArrayList<>();
		for (PayableBodyVO payableBodyVO : bodyVOs) {
			
			//ReceivableBillItemVO save_bodyVO = new ReceivableBillItemVO();
			PayableBillItemVO save_bodyVO = new PayableBillItemVO();
			
			String pk_supplier = getSupplier(payableBodyVO.getCustomer());
			
			save_bodyVO.setAttributeValue("scomment", payableBodyVO.getScomment());// 摘要
			save_bodyVO.setAttributeValue("supplier", pk_supplier);// 客户
			save_bodyVO.setAttributeValue("quantity_de", new UFDouble(payableBodyVO.getQuantity_cr()));// 借方数量
			save_bodyVO.setAttributeValue("taxrate", new UFDouble(payableBodyVO.getTaxrate()));// 税率
			save_bodyVO.setAttributeValue("taxprice", payableBodyVO.getTaxprice());// 含税单价
			save_bodyVO.setAttributeValue("local_tax_cr", payableBodyVO.getLocal_tax_cr());// 税额
			save_bodyVO.setAttributeValue("notax_cr", payableBodyVO.getNotax_cr());// 借方原币无税金额
			save_bodyVO.setAttributeValue("money_cr", new UFDouble(payableBodyVO.getMoney_cr()));// 借方原币金额
			save_bodyVO.setAttributeValue("money_bal", new UFDouble(payableBodyVO.getMoney_bal()));// 原币余额
			save_bodyVO.setAttributeValue("rate", 1);// 组织本币汇率
			save_bodyVO.setAttributeValue("local_money_bal", new UFDouble(payableBodyVO.getMoney_bal()));// 组织本币余额
			save_bodyVO.setAttributeValue("local_money_cr", new UFDouble(payableBodyVO.getMoney_bal()));// 组织本币余额
			save_bodyVO.setAttributeValue("local_notax_cr", new UFDouble(payableBodyVO.getMoney_bal()));// 组织本币无税金额
			save_bodyVO.setAttributeValue("def24", payableBodyVO.getRowid()); // 综合商城系统单据行ID
			save_bodyVO.setAttributeValue("def25", payableBodyVO.getRatio()); // 分账比例
			save_bodyVO.setAttributeValue("def26", new UFDouble(payableBodyVO.getMoney_cr())); //应分账金额
			save_bodyVO.setAttributeValue("memo", payableBodyVO.getMemo());//备注
			save_bodyVO.setAttributeValue("objtype", 1); //往来对象，默认为1=供应商
			
			save_bodyVO.setAttributeValue("payaccount", payaccountpk); //付款银行账户
			
			String recaccount = getBankNUm(pk_supplier,payableBodyVO.getRecaccount());
			if(recaccount == null){
				throw new BusinessException("综合商城同步NC财务系统应付单："+ headVO.getSrcid() +" 的付款银行账户在NC财务系统中不存在！收款银行账户:"+recaccount);
			}
			save_bodyVO.setAttributeValue("recaccount", recaccount); //收款银行账户

			save_bodyVO.setAttributeValue("billdate", save_headVO.getBilldate());// 单据日期
			save_bodyVO.setAttributeValue("pk_group", save_headVO.getPk_group());// 所属集团
			save_bodyVO.setAttributeValue("pk_billtype",save_headVO.getPk_billtype());// 单据类型编码
			save_bodyVO.setAttributeValue("billclass","yf");// 单据大类
			save_bodyVO.setAttributeValue("pk_tradetype",save_headVO.getPk_tradetype());// 应收类型code
			save_bodyVO.setAttributeValue("pk_tradetypeid",getPkTradeTypeid(save_headVO.getPk_tradetype()));// 应收类型 save_headVO.getPk_tradetypeid()
			save_bodyVO.setAttributeValue("busidate", save_headVO.getBilldate());// 起算日期
			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// 应收财务组织
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// 挂起标志
			save_bodyVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
			save_bodyVO.setAttributeValue("direction", -1);
			
			save_bodyVO.setStatus(VOStatus.NEW);
			bodyTotalMoney = bodyTotalMoney.add(new UFDouble(payableBodyVO.getMoney_cr()));
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new PayableBillItemVO[0]));

		if(!(bodyTotalMoney.compareTo(headMoney) == 0)){
			throw new BusinessException("综合商城应付单："+ headVO.getSrcid() +" 的表头金额与表体总金额不相等！");
		}
		
		return aggvo;
	}
	
	public String getPkTradeTypeid(String pkTradeType){
		String sql = "select pk_billtypeid from bd_billtype where pk_billtypecode = '" + pkTradeType+ "'";
		String pk_org = null;
		try {
			pk_org = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据编码或名称找供应商
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getSupplier(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select corcustomer from bd_supplier where (code='" + code
						+ "' or name='" + code + "') and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}


	/**
	 * 获取客商收款、付款银行账户的pk
	 * @param bankNum 付款银行账户
	 * @return
	 */
	public String getPayBankAccountPK(String bankAccount){
		String sql = "select pk_bankaccsub from bd_bankaccsub where accnum = '"
				+ bankAccount + "' and nvl(dr,0)=0 ";
		
		String pk_account = null;
		try {
			pk_account = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_account != null) {
				return pk_account;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 根据付款公司pk与银行账号获取账号
	 */
	public String getBankNUm(String pkcust, String accnum){
		String result = null;
		
		String sql = "  select b.pk_bankaccsub "+
					" from bd_bankaccbas a , bd_bankaccsub b , bd_custbank c "+
						" where a.pk_bankaccbas = c.pk_bankaccbas and c.pk_bankaccsub != '~' and a.pk_bankaccbas = b.pk_bankaccbas "+
						" AND b.pk_bankaccbas = c.pk_bankaccbas and a.accnum = '"+accnum+"' and c.pk_cust = '"+pkcust+"' and c.accclass = '3'";
		
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取客商收款银行账户的pk
	 * @param bankNum 付款银行账户
	 * @return
	 */
	public String getRecBankAccountPK(String bankAccount){
		String sql = "select pk_bankaccsub from bd_bankaccsub where accnum = '"
				+ bankAccount + "' and dr=0 ";
		
		String pk_account = null;
		try {
			pk_account = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_account != null) {
				return pk_account;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据【用户编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getUserPkByCode(String code) {
		String sql = "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
				+ code + "'";
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据【公司编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_orgByCode(String code) {
		String sql = "select pk_org from org_orgs where (code='" + code
				+ "' or name = '" + code + "') and dr=0 and enablestate=2 ";
		String pk_org = null;
		try {
			pk_org = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
