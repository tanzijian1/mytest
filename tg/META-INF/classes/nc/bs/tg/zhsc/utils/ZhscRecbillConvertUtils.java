package nc.bs.tg.zhsc.utils;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.ReceivableBodyVO;
import nc.vo.tg.outside.ReceivableHeadVO;


public class ZhscRecbillConvertUtils{
public static final String DefaultOperator = "LLZHSC";// 默认制单人
	
	private static ZhscRecbillConvertUtils convertUtils;
	
	public static ZhscRecbillConvertUtils getInstance() {
		if (convertUtils == null) {
			convertUtils = new ZhscRecbillConvertUtils();
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
	
	public AggReceivableBillVO onTranBill(ReceivableHeadVO headVO,
			List<ReceivableBodyVO> bodyVOs) throws BusinessException{
		
		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO save_headVO = new ReceivableBillVO();
		
		
		/**
		 * 综合商城传过来的表头字段
		 */
		String pk_org = getPk_orgByCode(headVO.getPk_org());
		if(pk_org != null){
			save_headVO.setPk_org(pk_org);// 财务组织
		}else {
			throw new BusinessException("综合商城同步NC财务系统应收单："+ headVO.getSrcid() +" 的财务组织在NC财务系统中不存在！财务组织："+headVO.getPk_org());
		}
		
		save_headVO.setAttributeValue("billdate", new UFDate(headVO.getBilldate()));// 单据日期
		save_headVO.setAttributeValue("money", headVO.getMoney());// 原币金额
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// 外系统主键
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// 外系统单号
		save_headVO.setAttributeValue("local_money", headVO.getMoney());// 组织本币金额
		save_headVO.setAttributeValue("rate", 1);// 组织本币汇率
		save_headVO.setAttributeValue("def11", headVO.getHyperlinks());//综合商城附件超链接
		save_headVO.setAttributeValue("pk_tradetype", headVO.getPk_tradetype());// 应收类型code
		save_headVO.setAttributeValue("pk_tradetypeid",headVO.getPk_tradetypeid());// 应收类型
		save_headVO.setAttributeValue("def7", headVO.getBusinesstype());// 业务类型
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
		save_headVO.setAttributeValue("billstatus", 2);// 单据状态,默认为2审批中
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团，默认为时代集团
		save_headVO.setAttributeValue("pk_billtype", "F0");// 单据类型编码
		save_headVO.setAttributeValue("billclass", "ys");// 单据大类,默认为应收单
		save_headVO.setAttributeValue("syscode", 0);// 单据所属系统，默认为0，0=应收系统
		save_headVO.setAttributeValue("src_syscode", 0);// 单据来源系统
		save_headVO.setAttributeValue("billmaker", getUserPkByCode(DefaultOperator));// 制单人，默认为邻里综合商城系统
		save_headVO.setAttributeValue("objtype", 0); //往来对象，默认为0=客户
		save_headVO.setAttributeValue("creator",getUserPkByCode(DefaultOperator));// 创建人
		
		save_headVO.setStatus(VOStatus.NEW);
		
		UFDouble headMoney = new UFDouble(headVO.getMoney());
		UFDouble bodyTotalMoney = new UFDouble();
		
		/**
		 * 综合商城报文传来的字段
		 */
		List<ReceivableBillItemVO> bodylist = new ArrayList<>();
		for (ReceivableBodyVO receivableBodyVO : bodyVOs) {
			ReceivableBillItemVO save_bodyVO = new ReceivableBillItemVO();
			
			save_bodyVO.setAttributeValue("scomment", save_bodyVO.getScomment());// 摘要
			save_bodyVO.setAttributeValue("customer", getcustomer(receivableBodyVO.getCustomer()));// 客户
			save_bodyVO.setAttributeValue("quantity_de", new UFDouble(receivableBodyVO.getQuantity_de()));// 借方数量
			save_bodyVO.setAttributeValue("taxrate", new UFDouble(receivableBodyVO.getTaxrate()));// 税率
			save_bodyVO.setAttributeValue("taxprice", receivableBodyVO.getTaxprice());// 含税单价
			save_bodyVO.setAttributeValue("local_tax_de", receivableBodyVO.getLocal_tax_de());// 税额
			save_bodyVO.setAttributeValue("notax_de", receivableBodyVO.getNotax_de());// 借方原币无税金额
			save_bodyVO.setAttributeValue("money_de", new UFDouble(receivableBodyVO.getMoney_de()));// 借方原币金额
			save_bodyVO.setAttributeValue("money_bal", new UFDouble(receivableBodyVO.getMoney_bal()));// 原币余额
			save_bodyVO.setAttributeValue("rate", 1);// 组织本币汇率
			save_bodyVO.setAttributeValue("local_money_bal", new UFDouble(receivableBodyVO.getMoney_bal()));// 组织本币余额
			save_bodyVO.setAttributeValue("local_money_de", new UFDouble(receivableBodyVO.getMoney_bal()));// 组织本币余额
			save_bodyVO.setAttributeValue("local_notax_de", new UFDouble(receivableBodyVO.getMoney_bal()));// 组织本币无税金额
			save_bodyVO.setAttributeValue("def12", receivableBodyVO.getRowid()); // 综合商城系统单据行ID
			save_bodyVO.setAttributeValue("def25", receivableBodyVO.getRatio()); // 分账比例
			save_bodyVO.setAttributeValue("def26", new UFDouble(receivableBodyVO.getMoney_de())); //应分账金额
			save_bodyVO.setAttributeValue("memo", receivableBodyVO.getMemo());//备注
			save_bodyVO.setAttributeValue("billdate", save_headVO.getBilldate());// 单据日期
			save_bodyVO.setAttributeValue("pk_group", save_headVO.getPk_group());// 所属集团
			save_bodyVO.setAttributeValue("pk_billtype",save_headVO.getPk_billtype());// 单据类型编码
			save_bodyVO.setAttributeValue("billclass","ys");// 单据大类
			save_bodyVO.setAttributeValue("pk_tradetype",save_headVO.getPk_tradetype());// 应收类型code
			save_bodyVO.setAttributeValue("pk_tradetypeid",save_headVO.getPk_tradetypeid());// 应收类型
			save_bodyVO.setAttributeValue("busidate", save_headVO.getBilldate());// 起算日期
			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// 应收财务组织
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// 挂起标志
			save_bodyVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
			save_bodyVO.setAttributeValue("objtype", "0");// 往来对象，默认为0=客户
			
			String project = getpk_projectByCode(receivableBodyVO.getProject());
			if(project == null){
				throw new BusinessException("综合商城同步NC财务系统应收单："+ headVO.getSrcid() +" 的项目在NC财务系统中不存在！项目编码:"+project);
			}
			save_bodyVO.setAttributeValue("project", project); //项目

			bodyTotalMoney = bodyTotalMoney.add(new UFDouble(receivableBodyVO.getMoney_bal()));


			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new ReceivableBillItemVO[0]));

		if(!(headMoney.compareTo(bodyTotalMoney) == 0)){
			throw new BusinessException("综合商城应收单："+ headVO.getSrcid() +" 的表头金额与表体总金额不相等！");
		}
		
		return aggvo;
		
	}
	
	/**
	 * 根据【项目编码】获取主键
	 * 
	 * @param code
	 * @return
	 */

	public String getpk_projectByCode(String code) {
		String sql = "select pk_project from bd_project where nvl(dr,0)=0 and enablestate='2' and def1='"
				+ code + "'";
		// 成本改为传项目编码
		String costsql = "select pk_project from bd_project where nvl(dr,0)=0 and enablestate='2' and  project_code  = '"
				+ code + "'";
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (result == null) {
				result = (String) getBaseDAO().executeQuery(costsql,
						new ColumnProcessor());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
	 * 根据编码或名称找客商
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getcustomer(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select pk_customer from bd_customer where (code='" + code
						+ "' or name='" + code + "') and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
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
