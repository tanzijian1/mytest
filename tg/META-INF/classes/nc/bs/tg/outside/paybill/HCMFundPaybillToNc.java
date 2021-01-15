package nc.bs.tg.outside.paybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.recbill.RecbillUtils;
import nc.bs.tg.outside.sale.utils.paybill.PayBillConvertor;
import nc.bs.tg.outside.sale.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.cdm.innerpay.action.returnBackAction;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import vo.tg.outside.HCMFundBillBodyVo;
import vo.tg.outside.HCMFundBillHeadVo;
import vo.tg.outside.HCMSBBillBodyVo;
import vo.tg.outside.HCMSBBillHeadVo;
import vo.tg.outside.PayBillBodyVO;
import vo.tg.outside.PayBillHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * HCM 各公司公积金付款单接收结果
 * @author zhaozhiying
 *
 */
public class HCMFundPaybillToNc extends PayBillUtils implements ITGSyncService{

	public static final String HCMSalaryOperatorName = "RLZY";// HCM默认操作员
	 
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname) throws BusinessException {

		Map<String, String> resultInfo = new HashMap<String, String>();
			
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		InvocationInfoProxy.getInstance().setUserId(getUserByCode(HCMSalaryOperatorName));
		InvocationInfoProxy.getInstance().setUserCode(HCMSalaryOperatorName);
		
		PayBillConvertor convertor = new PayBillConvertor();
		// 处理表单信息
		JSONObject jsonData = (JSONObject) info.get("data");// 表单数据
		String jsonhead = jsonData.getString("headInfo");// 外系统来源表头数据
		String jsonbody = jsonData.getString("bodyInfo");// 外系统来源表体数据
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("表单数据为空，请检查！json:" + jsonData);
		}
		HashMap<String, Object> value = JSONObject.parseObject(jsonData.toJSONString(),new TypeReference<HashMap<String, Object>>() {});

		// 转换json
		HCMFundBillHeadVo headVO = JSONObject.parseObject(jsonhead,HCMFundBillHeadVo.class);
		List<HCMFundBillBodyVo> bodyVOs = JSONObject.parseArray(jsonbody,HCMFundBillBodyVo.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("表单数据转换失败，请检查！json:" + jsonData);
		}
		
		String srcid = headVO.getSrcid();// 外系统业务单据ID
		String srcno = headVO.getSrcbillno();// 外系统业务单据单据号

		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		
		
		try {
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【" + billkey + "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue("billno")
						+ "】,请勿重复上传!");
			}
			// AggPayBillVO billvo = (AggPayBillVO) convertor.castToBill(value,
			// AggPayBillVO.class, aggVO);
			AggPayBillVO billvo = onTranBill(headVO, bodyVOs);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = (AggPayBillVO[]) getPfBusiAction().processAction(
					"SAVE", "F3", null, billvo, null, eParam);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
			
			WorkflownoteVO worknoteVO = NCLocator.getInstance().lookup(IWorkflowMachine.class)
					   .checkWorkFlow("SAVE", "F3", billvos[0], eParam);
			obj = (AggPayBillVO[]) getPfBusiAction().processAction("SAVE", "F3", worknoteVO, billvos[0], null, eParam);
			
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO().getAttributeValue("billno"));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	
	}
	
	public AggPayBillVO onTranBill(HCMFundBillHeadVo headVo,List<HCMFundBillBodyVo> bodyVos) throws BusinessException{
		AggPayBillVO aggvo = new AggPayBillVO();
		
		PayBillVO save_headVO = new PayBillVO();
		
		//校验收款账户信息
		//设置表头信息
		
		String pk_org = getPk_orgByCode(headVo.getPk_org());
		headVo.setPk_org(pk_org);
		/*save_headVO.setAttributeValue("pk_org", headVo.getPk_org());
		save_headVO.setAttributeValue("pk_balatype", getBalatypePkByCode(headVo.getPk_balatype()));

		save_headVO.setAttributeValue("billdate", new UFDate(headVo.getBilldate()));
		save_headVO.setAttributeValue("def67", headVo.getDef67().substring(0,7));
		
		save_headVO.setAttributeValue("money", headVo.getTotal());
		save_headVO.setAttributeValue("def15", headVo.getDef30());
		
		save_headVO.setAttributeValue("def1", headVo.getSrcid());
		save_headVO.setAttributeValue("def2", headVo.getSrcbillno());*/
		
		
		save_headVO.setPk_org(headVo.getPk_org());
		save_headVO.setPk_balatype(getBalatypePkByCode(headVo.getPk_balatype()));
		save_headVO.setBilldate(new UFDate(headVo.getBilldate()));
		if(StringUtils.isNotBlank(headVo.getDef67())){
			if(headVo.getDef67().length() >=7){
				save_headVO.setDef67(headVo.getDef67().substring(0,7));
			}else {
				save_headVO.setDef67(headVo.getDef67());
			}
		}
		if(StringUtils.isNotBlank(headVo.getTotal())){
			save_headVO.setMoney(new UFDouble(headVo.getTotal()));
		}
		save_headVO.setDef15(headVo.getDef30());
		save_headVO.setDef1(headVo.getSrcid());
		save_headVO.setDef2(headVo.getSrcbillno());
		
		
		//设置表头默认字段
		//财务组织
		String pk_vid = getvidByorg((String) save_headVO.getAttributeValue("pk_org"));
		/*save_headVO.setAttributeValue("pk_fiorg",(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("pk_fiorg_v", pk_vid);
		save_headVO.setAttributeValue("sett_org",(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("sett_org_v", pk_vid);
		save_headVO.setAttributeValue("creationtime",save_headVO.getAttributeValue("billdate"));
		save_headVO.setAttributeValue("objtype", 1);
		save_headVO.setAttributeValue("billclass", "fk");
		save_headVO.setAttributeValue("approvestatus", -1);
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");
		save_headVO.setAttributeValue("pk_tradetype", "F3-Cxx-031");
		save_headVO.setAttributeValue("pk_tradetypeid", "10013410000000568SA8");
		save_headVO.setAttributeValue("pk_billtype", "F3");// 单据类型编码
		save_headVO.setAttributeValue("billdate",new UFDate(headVo.getBilldate()));// 单据日期
		save_headVO.setAttributeValue("syscode", 1);// 单据所属系统，默认为1，1=应付系统
		save_headVO.setAttributeValue("src_syscode", 1);// 单据来源系统
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
		save_headVO.setAttributeValue("billstatus", 2);// 单据状态,默认为2审批中
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团，默认为时代集团
		save_headVO.setAttributeValue("billmaker", getUserByCode(HCMSalaryOperatorName));// 制单人
		save_headVO.setAttributeValue("objtype", 1); // 往来对象 2=部门，3=业务员，1=供应商，0=客户  
		save_headVO.setAttributeValue("creator", RecbillUtils.getUtils().getUserPkByCode(HCMSalaryOperatorName));// 创建人
		save_headVO.setAttributeValue("pk_busitype", "1001ZZ1000000057GMMB"); //业务流程
*/		
		
		save_headVO.setPk_fiorg(save_headVO.getPk_org());
		save_headVO.setPk_fiorg_v(pk_vid);
		save_headVO.setSett_org(save_headVO.getPk_org());
		save_headVO.setSett_org_v(pk_vid);
		save_headVO.setCreationtime(new UFDateTime());
		save_headVO.setObjtype(1);
		save_headVO.setBillclass("fk");
		save_headVO.setApprovestatus(-1);
		save_headVO.setPk_group("000112100000000005FD");
		save_headVO.setPk_tradetype("F3-Cxx-031");
		save_headVO.setPk_tradetypeid("10013410000000568SA8");
		save_headVO.setPk_billtype("F3");
		save_headVO.setBilldate(new UFDate(headVo.getBilldate()));
		save_headVO.setSyscode(1);
		save_headVO.setSrc_syscode(1);
		save_headVO.setPk_currtype("1002Z0100000000001K1");
		save_headVO.setBillstatus(2);
		save_headVO.setBillmaker(getUserByCode(HCMSalaryOperatorName));
		save_headVO.setObjtype(1);
		save_headVO.setCreator(RecbillUtils.getUtils().getUserPkByCode(HCMSalaryOperatorName));
		save_headVO.setStatus(VOStatus.NEW);
		
		// 新增 公司性质、资本化字段 2020-11-23 zzy start
		List<Map<String, String>> budgetsubNames = getLinkCompany(pk_org);
		if(budgetsubNames != null && budgetsubNames.size() >0){
			for (Map<String, String> names : budgetsubNames) {
				save_headVO.setAttributeValue("def91", names.get("factorvalue5"));// 公司性质
				save_headVO.setAttributeValue("def92", names.get("factorvalue4"));// 是否资本化
			}
		}
		// 新增 公司性质、资本化字段 2020-11-23 zzy end
		
		List<PayBillItemVO> bodylist = new ArrayList<>();
		for (HCMFundBillBodyVo payBillBodyVO : bodyVos) {
			PayBillItemVO save_bodyVO = new PayBillItemVO();
			/*save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// 应收财务组织
			save_bodyVO.setAttributeValue("def18", getPk_orgByCode(payBillBodyVO.getDef18()));// 合同签订公司
			save_bodyVO.setAttributeValue("def30",getDefdocInfo(payBillBodyVO.getDef30(), "zdy046").get("pk_defdoc"));// 部门属性
			save_bodyVO.setAttributeValue("recaccount", payBillBodyVO.getRecaccount());// 收款单位银行账户
			save_bodyVO.setAttributeValue("def22", payBillBodyVO.getDef22());// 人数
*/		
			//如果结算方式为自动划扣时，社保方传：A11031
			//save_bodyVO.setAttributeValue("supplier", getCustomerPK(payBillBodyVO.getSsname()));
			String num = getBankNUm(getCustomerPK(payBillBodyVO.getSsname()),payBillBodyVO.getRecaccount());
			/*if(num != null){
				save_bodyVO.setAttributeValue("recaccount", num);
			}else {
				throw new BusinessException("社保方的客商银行账户与社保方不相符，请检查！单据号srcid:" + headVo.getSrcid());
			}*/
			
			
			save_bodyVO.setPk_org(save_headVO.getPk_org());
			
			if(StringUtils.isNotBlank(payBillBodyVO.getDef18())){
				String pk_supplier = getPk_orgByCode(payBillBodyVO.getDef18());
				if(pk_supplier != null){
					save_bodyVO.setDef18(pk_supplier);
				}/*else{
					throw new BusinessException("合同签订公司不能为空，请检查！单据号srcid:" + headVo.getSrcid());
				}*/
			}/*else{
				throw new BusinessException("合同签订公司不能为空，请检查！单据号srcid:" + headVo.getSrcid());
			}*/
			
			if(payBillBodyVO.getDef30() != null){
				Map<String, String> map_dept = getDefdocInfo(payBillBodyVO.getDef30(), "zdy046");
				if(map_dept.size() >0 && StringUtils.isNotBlank(map_dept.get("pk_defdoc"))){
					save_bodyVO.setDef30(map_dept.get("pk_defdoc"));
				}else{
					throw new BusinessException("部门属性不能为空，请检查！单据号srcid:" + headVo.getSrcid());
				}
			
			}else {
				throw new BusinessException("部门属性不能为空，请检查！单据号srcid:" + headVo.getSrcid());
			}
			save_bodyVO.setDef22(payBillBodyVO.getDef22());
			save_bodyVO.setSupplier(getCustomerPK(payBillBodyVO.getSsname()));
			if(num != null){
				save_bodyVO.setRecaccount(num);
			}else {
				throw new BusinessException("公积金中心的客商银行账户与公积金中心不相符，请检查！单据号srcid:" + headVo.getSrcid());
			}
			
			
			
			/*save_bodyVO.setAttributeValue("def1", payBillBodyVO.getDef1());// 公积金公司部分
			save_bodyVO.setAttributeValue("def36", payBillBodyVO.getDef36());// 公积金个人部分
			save_bodyVO.setAttributeValue("money_de", new UFDouble(payBillBodyVO.getMoney_de()));// 公司+个人合计

			save_bodyVO.setAttributeValue("local_money_bal", new UFDouble(payBillBodyVO.getMoney_de()));// 本币=原币（币种一样） 
			save_bodyVO.setAttributeValue("local_money_de", new UFDouble(payBillBodyVO.getMoney_de()));// 余额
			save_bodyVO.setAttributeValue("local_notax_de", new UFDouble(payBillBodyVO.getMoney_de()));// 余额
			save_bodyVO.setAttributeValue("money_bal", new UFDouble(payBillBodyVO.getMoney_de()));// 余额
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// 挂起标志
			save_bodyVO.setAttributeValue("billdate", save_headVO.getBilldate());// 单据日期
			save_bodyVO.setAttributeValue("pk_balatype", save_headVO.getPk_balatype());// 结算方式
			save_bodyVO.setAttributeValue("pk_group", save_headVO.getPk_group());// 所属集团
			save_bodyVO.setAttributeValue("pk_billtype",save_headVO.getPk_billtype());// 单据类型编码
			save_bodyVO.setAttributeValue("billclass",save_headVO.getBillclass());// 单据大类
			save_bodyVO.setAttributeValue("pk_tradetype","F3-Cxx-031");// 应收类型code
			save_bodyVO.setAttributeValue("pk_tradetypeid","10013410000000568SA8");// 应收类型
			save_bodyVO.setAttributeValue("busidate", save_headVO.getBilldate());// 起算日期
			save_bodyVO.setAttributeValue("objtype", 1);// 往来对象 2=部门，3=业务员，1=供应商，0=客户  
			save_bodyVO.setAttributeValue("direction", 1);// 方向
			save_bodyVO.setAttributeValue("pk_currtype",save_headVO.getPk_currtype());// 币种
			save_bodyVO.setAttributeValue("rate", 1);// 组织本币汇率
*/			
			save_bodyVO.setDef1(payBillBodyVO.getDef1());
			save_bodyVO.setDef36(payBillBodyVO.getDef36());
			save_bodyVO.setMoney_de(new UFDouble(payBillBodyVO.getMoney_de()));
			
			save_bodyVO.setLocal_money_bal(new UFDouble(payBillBodyVO.getMoney_de()));
			save_bodyVO.setLocal_money_de(new UFDouble(payBillBodyVO.getMoney_de()));
			save_bodyVO.setLocal_notax_de(new UFDouble(payBillBodyVO.getMoney_de()));
			save_bodyVO.setMoney_bal(new UFDouble(payBillBodyVO.getMoney_de()));
			save_bodyVO.setPausetransact(UFBoolean.FALSE);
			save_bodyVO.setBilldate(save_headVO.getBilldate());
			save_bodyVO.setPk_balatype(save_headVO.getPk_balatype());
			save_bodyVO.setPk_group(save_headVO.getPk_group());
			save_bodyVO.setPk_billtype(save_headVO.getPk_billtype());
			save_bodyVO.setBillclass(save_headVO.getBillclass());

			save_bodyVO.setPk_tradetype("F3-Cxx-031");
			save_bodyVO.setPk_tradetypeid("10013410000000568SA8");
			save_bodyVO.setBusidate(save_headVO.getBilldate());
			save_bodyVO.setObjtype(1);
			save_bodyVO.setDirection(1);
			save_bodyVO.setPk_currtype(save_headVO.getPk_currtype());
			save_bodyVO.setStatus(VOStatus.NEW);
			save_bodyVO.setRate(new UFDouble("1"));
			
			
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new PayBillItemVO[0]));
		
		return aggvo;
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

	/**
	 * 结算方式查询
	 * 
	 * @param balatype
	 * @return
	 */
	protected String getBalatypePkByCode(String balatype) {
		String result = null;
		String sql = "select  pk_balatype from bd_balatype where dr = 0 and code ='"
				+ balatype + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取org_orgs的pk_org值
	 * 
	 * @param pk_org主键
	 * 
	 * @return 返回转换后的pk_vid值
	 */
	public String getvidByorg(String pk_org) throws DAOException {
		String sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + pk_org
				+ "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_vid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_vid;
	}
	
	/**
	 * 获取客商pk
	 * @param code
	 * @return
	 */
	public String getCustomerPK(String code){
		String result = null;
		//String sql = "SELECT pk_cust_sup FROM bd_cust_supplier WHERE CODE = '"+code+"' AND custenablestate = '2' AND NVL(dr,0)=0";
		String sql = "SELECT pk_customer FROM bd_customer WHERE (CODE = '"+code+"' or name = '"+ code + "') AND NVL(dr,0)=0";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取客商银行账户
	 * @param pk_org
	 * @return
	 */
	public String getCustomerBankNum(String pk_org,String accnum){
		String result = null;
		String sql = "SELECT a.accnum FROM bd_bankaccsub a inner join bd_bankaccuse b on a.pk_bankaccbas = b.pk_bankaccbas and b.pk_org = '"+pk_org+"' where a.accnum = '"+ accnum +"' AND NVL(a.dr,0)=0";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (result != null) {
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	    * 通过用户编码得到用户pk
	 * @throws DAOException 
	    */
	public String getUserByCode(String psndoccode) throws DAOException{
			String pk=(String)getBaseDAO().executeQuery("select  cuserid  from sm_user where user_code='"+psndoccode+"'", new ColumnProcessor());
		    return pk;
	}
	
	/**
	 * 根据【部门编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_deptByCode(String code) {
		
		String sql = "select pk_dept from org_dept  where code = '"
				+ code + "' and dr=0 ";
		
		String pk_dept = null;
		try {
			pk_dept = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_dept != null) {
				return pk_dept;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询业务流程
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getBusitypeID(String code, String pk_group)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_busitype   ");
		query.append("  from bd_busitype ");
		query.append(" where validity = 1  ");
		query.append("   and busicode = '" + code + "' ");
		query.append(" and pk_group = '" + pk_group + "'");
		query.append("   and isnull(dr,0) = 0  ");
		String value = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());

		return value;
	}
	
	/**
	 * 自定义档案
	 * 
	 * @param key
	 * @param pk_project
	 * @param
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDefdocInfo(String key, String listcode)
			throws BusinessException {
		// listcode = "zdy046";
		return DocInfoQryUtils.getUtils().getDefdocInfo(key, listcode);
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
	 * 获取是否资本化和公司性质 测试环境的档案编码是XM02、正式的是XM01
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	 protected List<Map<String, String>> getLinkCompany(String pk_org) throws BusinessException {
		 List<Map<String, String>> result = null;
		 StringBuffer query = new StringBuffer();
		 query.append("select factorvalue4,factorvalue5 from fip_docview_b where pk_classview=(select pk_classview from fip_docview ta where ta.viewcode ='XM01') and factorvalue2 = '"
				 + pk_org + "'");
		 try {
			 result = (List<Map<String, String>>) getBaseDAO().executeQuery(
					 query.toString(), new MapListProcessor());
		 } catch (Exception e) {
			 throw new BusinessException(e.getMessage(), e);
		 }
		 return result;
	 } 
	

}
