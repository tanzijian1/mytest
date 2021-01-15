package nc.bs.tg.outside.sale.utils.paybill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.bs.tg.outside.sale.utils.salessystem.BasicInformationUtil;
import nc.itf.tg.outside.SaleBillCont;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 供应商付款单
 * 
 * @author kyy
 * 
 */
public class PayBillUtils extends SaleBillUtils {
	static PayBillUtils utils;

	public static PayBillUtils getUtils() {
		if (utils == null) {
			utils = new PayBillUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);
		
		// 解析json参数,获取表头数据
		JSONObject headJson = (JSONObject) value.get("headInfo");
		HashMap<String, String> dataMap = new HashMap<String, String>();
		
		// Saleid 按实际存入信息位置进行变更
		String saleid = headJson.getString("def1");// 销售系统业务单据ID
		String saleno = headJson.getString("def2");// 销售系统业务单据单据号

		// 销售系统业务单据ID和销售系统业务单据单据号做队列控制和日志输出
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		// 检验客商银行账户唯一性 add by huangxj
		Collection<SupplierVO> docVO = SaleBillUtils
				.getUtils()
				.getBaseDAO()
				.retrieveByClause(
						SupplierVO.class,
						"isnull(dr,0)=0 and def2 = '"
								+ headJson.getString("def43")
								+ "' and name = '"
								+ headJson.getString("supplier") + "'");

		// 检查销售系统业务单据ID唯一性
		AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
				"isnull(dr,0)=0 and def1 = '" + saleid + "'");
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String)aggVO.getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		}

		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {

			// 转换数据为VO
			// *********************************
			PayBillConvertor convertor = new PayBillConvertor();
			// 默认集团PK
			convertor.setDefaultGroup("000112100000000005FD");
			// 配置表体key与名称映射
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("payitem", "供应商付款单行");
			convertor.setBVOKeyName(bVOKeyName);

			// 配置表头key与名称映射
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("paybill", "供应商付款单");
			convertor.setHVOKeyName(hVOKeyName);

			// 表头空值校验字段
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hPayBillHVOKeyName = new HashMap<String, String>();

			hPayBillHVOKeyName.put("def1", "销售系统单据ID");
			// hPayBillHVOKeyName.put("def2", "销售系统单号");
			// hPayBillHVOKeyName.put("def3", "影像编码");
			// hPayBillHVOKeyName.put("def4", "影像状态");
			hPayBillHVOKeyName.put("def41", "开票员");
			// hPayBillHVOKeyName.put("def6", "备注");
			if (!"F3-Cxx-024".equals(headJson.getString("pk_tradetypeid"))) {
				hPayBillHVOKeyName.put("def43", "客商名称（供应商，客户）");
				hPayBillHVOKeyName.put("def44", "收款银行账户开户行");
			}
			hPayBillHVOKeyName.put("billdate", "单据日期");
			hPayBillHVOKeyName.put("pk_org", "财务组织");
			// hPayBillHVOKeyName.put("objtype", "往来对象");
			hPayBillHVOKeyName.put("supplier", "客商id（供应商，客户）");
			hPayBillHVOKeyName.put("pk_currtype", "币种");
			hValidatedKeyName.put("paybill", hPayBillHVOKeyName);
			// 银行账户
			// String pk_account = null;
			// if ("F3-Cxx-001".equals(headJson.getString("pk_tradetypeid"))) {
			String[] result = new String[2];

			if (docVO.size() <= 0) {
				// result = service.saveSupplierAccount_RequiresNew(headJson);
				result = BasicInformationUtil.getUtils().onBasicBill(headJson);
			}/*
			 * else { pk_account = convertor.getRefAttributePk(
			 * "payitem-recaccount", headJson.getString("def43")); }
			 */
			// }
			// 表体空值校验字段
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> bPayBillBVOKeyName = new HashMap<String, String>();
			
			//bPayBillBVOKeyName.put("scomment", "摘要");
			bPayBillBVOKeyName.put("def19", "所属项目");
			// bPayBillBVOKeyName.put("def20", "购买房间");
			// bPayBillBVOKeyName.put("def25", "业态");
			// bPayBillBVOKeyName.put("def26", "计税方式");
			// bPayBillBVOKeyName.put("recaccount", "客户银行账户");
			// 当交易类型为转备款款不需要传，其他的需要传
			// if (!"F3-Cxx-001".equals(headJson.getString("pk_tradetypeid")) )
			// {
			// bPayBillBVOKeyName.put("def23", "款项类型");
			// bPayBillBVOKeyName.put("def24", "款项名称");
			// bPayBillBVOKeyName.put("def27", "税率");
			// bPayBillBVOKeyName.put("def28", "税额");
			// bPayBillBVOKeyName.put("def29", "不含税金额");
			// bPayBillBVOKeyName.put("def30", "客户银行账号");
			// bPayBillBVOKeyName.put("def31", "供应商名称");
			// }
			bPayBillBVOKeyName.put("rate", "汇率");
			// bPayBillBVOKeyName.put("local_money_de",
			// "申请退款金额(NC-组织本币金额(借方))");
			bPayBillBVOKeyName.put("money_de", "申请退款金额(NC-退款原币金额)");
			bPayBillBVOKeyName.put("pk_balatype", "结算方式");
			// bPayBillBVOKeyName.put("payaccount", "付款银行账户");
			// bPayBillBVOKeyName.put("supplier", "供应商名称");
			bValidatedKeyName.put("payitem", bPayBillBVOKeyName);

			// 参照类型字段
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("paybill-pk_org");
			refKeys.add("paybill-supplier");
			refKeys.add("paybill-pk_currtype");
			refKeys.add("paybill-pk_tradetypeid");
			refKeys.add("paybill-def59");
			refKeys.add("payitem-scomment");
			refKeys.add("payitem-pk_balatype");
			refKeys.add("payitem-payaccount");
			refKeys.add("payitem-def19");
			refKeys.add("payitem-def20");
			refKeys.add("payitem-def23");
			refKeys.add("payitem-def24");
			refKeys.add("payitem-def25");
			refKeys.add("payitem-def26");
			refKeys.add("payitem-def27");

			convertor.sethValidatedKeyName(hValidatedKeyName);
			convertor.setbValidatedKeyName(bValidatedKeyName);
			convertor.setRefKeys(refKeys);
           
			// *********************************
			AggPayBillVO billvo = (AggPayBillVO) convertor.castToBill(value,
					AggPayBillVO.class, aggVO);
			//校验单据为转备案付款是否有唯一标识
			
			if("F3-Cxx-001".equals(billvo.getParentVO().getAttributeValue("F3-Cxx-001"))){
				String def80=(String)billvo.getParentVO().getAttributeValue("def80");
				for(PayBillItemVO bvo:billvo.getBodyVOs()){
					if(bvo.getScomment()==null||bvo.getScomment().length()<1){
						throw new BusinessException("转备案付款单网银备注必须不为空");
					}
				}
//				if(def80==null||def80.length()<1)throw new BusinessException("转备案收付款唯一标识不能为空");
			}
			// 销售审批人
			if (!"".equals(billvo.getParentVO().getAttributeValue("def59"))
					&& billvo.getParentVO().getAttributeValue("def59") != null) {
				billvo.getParentVO().setAttributeValue(
						"def59",
						getUserByPsondoc((String) billvo.getParentVO()
								.getAttributeValue("def59")));
			}
			// 财务组织
			billvo.getParentVO().setAttributeValue(
					"pk_org",
					convertor.getRefAttributePk(
							"paybill-pk_org",
							(String) billvo.getParentVO().getAttributeValue(
									"pk_org")));

			String pk_vid = getvidByorg((String) billvo.getParentVO()
					.getAttributeValue("pk_org"));
			String pk_tradetype = (String) billvo.getParentVO()
					.getAttributeValue("pk_tradetypeid");

			// 设置NC默认信息
			billvo.getParentVO().setAttributeValue("billmaker",
					getUserByCode("SALE"));
			billvo.getParentVO().setAttributeValue("objtype", 1);
			// billvo.getParentVO().setAttributeValue("pk_tradetypeid",
			// getRefAttributePk("transtype", "F3"));
			// billvo.getParentVO().setAttributeValue("pk_tradetype", "F3");
			billvo.getParentVO().setAttributeValue("syscode", "1");
			billvo.getParentVO().setAttributeValue("src_syscode", "1");
			billvo.getParentVO().setAttributeValue("pk_billtype", "F3");
			billvo.getParentVO().setAttributeValue("pk_busitype",
					getRefAttributePk("pk_busitype", "AP02"));
			billvo.getParentVO().setAttributeValue("billstatus", -1);
			billvo.getParentVO().setAttributeValue("approvestatus", -1);
			billvo.getParentVO().setAttributeValue("creationtime",
					billvo.getParentVO().getAttributeValue("billdate"));
			billvo.getParentVO().setAttributeValue("creator",
					getUserByCode("SALE"));
			billvo.getParentVO().setAttributeValue("pk_org_v", pk_vid);
            billvo.getParentVO().setAttributeValue("def83", headJson.getString("def83"));
            billvo.getHeadVO().setDef83(headJson.getString("def83"));
			billvo.getParentVO().setAttributeValue("pk_fiorg",
					(String) billvo.getParentVO().getAttributeValue("pk_org"));
			billvo.getParentVO().setAttributeValue("pk_fiorg_v", pk_vid);
			billvo.getParentVO().setAttributeValue("sett_org",
					(String) billvo.getParentVO().getAttributeValue("pk_org"));
			billvo.getParentVO().setAttributeValue("sett_org_v", pk_vid);

			billvo.getParentVO().setAttributeValue("pk_group",
					"000112100000000005FD");
			billvo.getParentVO()
					.setAttributeValue("pk_tradetype", pk_tradetype);
			// ***********表头参照设值

			// 供应商（客户，客商）
			if (docVO.size() <= 0) {
				billvo.getParentVO().setAttributeValue("supplier", result[0]);
			} else {
				//TODO--20201116-C转备案优先取内部客商
				if("F3-Cxx-001".equals(billvo.getParentVO().getAttributeValue("F3-Cxx-001"))){
					billvo.getParentVO().setAttributeValue(
							"supplier",
							getinner_supplier(
									(String) billvo.getParentVO()
											.getAttributeValue("supplier"),
									(String) billvo.getParentVO()
											.getAttributeValue("pk_org"),
									(String) billvo.getParentVO()
											.getAttributeValue("pk_org")));
				}else{
					billvo.getParentVO().setAttributeValue(
							"supplier",
							convertor.getRefAttributePk("paybill-supplier",
									(String) billvo.getParentVO()
											.getAttributeValue("supplier"),
									(String) billvo.getParentVO()
											.getAttributeValue("pk_org"),
									(String) billvo.getParentVO()
											.getAttributeValue("pk_org")));
				}
			}
			// 付款类型
			billvo.getParentVO().setAttributeValue(
					"pk_tradetypeid",
					convertor.getRefAttributePk(
							"paybill-pk_tradetypeid",
							(String) billvo.getParentVO().getAttributeValue(
									"pk_tradetypeid")));
			// 币种
			billvo.getParentVO().setAttributeValue(
					"pk_currtype",
					convertor.getRefAttributePk(
							"paybill-pk_currtype",
							(String) billvo.getParentVO().getAttributeValue(
									"pk_currtype")));

			// ***********表头参照设值
            
			//TODO 是否简易业财模式
			String def11 = (String) getBaseDAO().executeQuery(
					"select def11  from org_orgs where pk_org='" + 
							billvo.getParentVO()
							.getAttributeValue("pk_org")+ "'", new ColumnProcessor());

			
				
			// ***********表体参照设值
			ISuperVO[] payBillItemVOs = billvo.getChildren(PayBillItemVO.class);
			for (ISuperVO tmppayBillItemVO : payBillItemVOs) {
				PayBillItemVO payBillItemVO = (PayBillItemVO) tmppayBillItemVO;
				payBillItemVO.setPk_currtype((String) billvo.getParentVO()
						.getAttributeValue("pk_currtype"));

				payBillItemVO.setPk_billtype("F3");
				payBillItemVO.setPk_org((String) billvo.getParentVO()
						.getAttributeValue("pk_org"));
				payBillItemVO.setPk_org_v(pk_vid);
				payBillItemVO.setPk_fiorg((String) billvo.getParentVO()
						.getAttributeValue("pk_org"));
				payBillItemVO.setPk_fiorg_v(pk_vid);
				payBillItemVO.setSett_org((String) billvo.getParentVO()
						.getAttributeValue("pk_org"));
				payBillItemVO.setSett_org_v(pk_vid);
				payBillItemVO.setMoney_bal((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setNotax_de((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setLocal_money_de((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setLocal_money_bal((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setLocal_notax_de((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setOccupationmny((UFDouble) payBillItemVO
						.getMoney_de());
				payBillItemVO.setObjtype(1);
				payBillItemVO.setRowno(0);
				payBillItemVO.setDirection(1);
				payBillItemVO.setPk_group("000112100000000005FD");
				payBillItemVO.setPk_payitem("ID_INDEX0");
				payBillItemVO.setPk_tradetype(pk_tradetype);
				payBillItemVO.setPk_tradetypeid((String) billvo.getParentVO()
						.getAttributeValue("pk_tradetypeid"));
				// 所属项目(NC房地产项目)
				if (!"".equals(payBillItemVO.getDef19())
						&& payBillItemVO.getDef19() != null) {
					if (!("简化业财模式".equals(def11))) {
						String def19=convertor.getRefAttributePk(
								"payitem-def21", payBillItemVO.getDef19());
						if(!("F3-Cxx-001".equals(billvo.getHeadVO().getPk_tradetype()))){
							if(def19==null||def19.length()<1){
								throw new BusinessException("所属项目(NC房地产项目) 编码" +payBillItemVO.getDef19() 
										+ "未能在NC档案中关联");
							}
							payBillItemVO.setDef19(def19);
						}else{
							payBillItemVO.setDef19(def19);
						}
					}else{
						payBillItemVO.setDef19(null);
					}
				}
				// 购买房间（NC房地产项目明细）
				if (!"".equals(payBillItemVO.getDef20())
						&& payBillItemVO.getDef20() != null) {
					if (!("简化业财模式".equals(def11))) {
						String def20=convertor.getRefAttributePk(
								"payitem-def22", payBillItemVO.getDef20());
						if(!("F3-Cxx-001".equals(billvo.getHeadVO().getPk_tradetype()))){
							if(def20==null||def20.length()<1){
								throw new BusinessException("购买房间（NC房地产项目明细）编码" +payBillItemVO.getDef20() 
										+ "未能在NC档案中关联");
							}
							payBillItemVO.setDef20(def20);
						}else{
							payBillItemVO.setDef20(def20);
						}
					}else{
						payBillItemVO.setDef20(null);
					}
					
				}
				if (!"".equals(payBillItemVO.getDef23())
						&& payBillItemVO.getDef23() != null) {
					// 款项类型
					payBillItemVO.setDef23(convertor.getRefAttributePk(
							"payitem-def23", payBillItemVO.getDef23()));
				}
				
				if (!"".equals(payBillItemVO.getDef24())
						&& payBillItemVO.getDef24() != null) {
					
					//TODO20200817
					if(!("简化业财模式".equals(def11))){
					if("F3-Cxx-019".equals(billvo.getParentVO().getAttributeValue("pk_tradetype"))){
						String ischeck=getTaxsepara(payBillItemVO.getDef24(),"def7");//是否拆税校验
						if(!("否".equals(ischeck))){
						String iftax=getTaxsepara(payBillItemVO.getDef24(),"def6");//是否拆税
						   if("Y".equals(iftax)){
							   if("0".equals(payBillItemVO.getDef27())||payBillItemVO.getDef27()==null){//税率
								   throw new BusinessException("款项名称："+getdefdocname(payBillItemVO.getDef24())+",为拆税，税率不能为零或空 ");
							   }
						   }else{
							   if(!("0".equals(payBillItemVO.getDef27()))){
								   throw new BusinessException("款项名称："+getdefdocname(payBillItemVO.getDef24())+",为不拆税，税率不能大于零 ");
							   }
						   }
						}
					  }
					}
					// 款项名称
					payBillItemVO.setDef24(convertor.getRefAttributePk(
							"payitem-def24", payBillItemVO.getDef24()));
					
				}
                 
				// 业态
				if (payBillItemVO.getDef25() != null
						&& !"".equals(payBillItemVO.getDef25())) {

					payBillItemVO.setDef25(convertor.getRefAttributePk(
							"payitem-def25", payBillItemVO.getDef25()));
				}
				// 计税方式
				if (payBillItemVO.getDef26() != null
						&& !"".equals(payBillItemVO.getDef26())) {
					payBillItemVO.setDef26(convertor.getRefAttributePk(
							"payitem-def26", payBillItemVO.getDef26()));
				}
				// 收款银行账户开户行
				// payBillItemVO.setDef31(convertor.getRefAttributePk(
				// "payitem-def31", payBillItemVO.getDef31()));
				// 客户银行账号
				if (docVO.size() <= 0) {
					payBillItemVO.setRecaccount(result[1]);
				} else {
					
					payBillItemVO.setRecaccount(convertor.getRefAttributePk(
							"payitem-recaccount",
							(String) billvo.getParentVO().getAttributeValue(
									"def43"),
							(String) billvo.getParentVO().getAttributeValue(
									"supplier")));
				}
				//借款人名称
				payBillItemVO.setDef51(getCustomerPK(payBillItemVO.getDef51()));
				//def50合同编码
				payBillItemVO.setDef50(getPk_defdocByNameORCode(payBillItemVO.getDef50(), "zdy012"));
				// 表体供应商（客户，客商）
				payBillItemVO.setSupplier((String) billvo.getParentVO()
						.getAttributeValue("supplier"));
				// 表体供应商（客户，客商）
				payBillItemVO.setCustomer((String) billvo.getParentVO()
						.getAttributeValue("supplier"));
				// 付款银行账户
				payBillItemVO.setPayaccount(convertor.getRefAttributePk(
						"payitem-payaccount",
						payBillItemVO.getPayaccount(),
						(String) billvo.getParentVO().getAttributeValue(
								"pk_org")));
				// 结算方式
				payBillItemVO.setPk_balatype(convertor.getRefAttributePk(
						"payitem-pk_balatype", payBillItemVO.getPk_balatype(),
						payBillItemVO.getPk_balatype()));
			}
			// ***********表体参照设值

			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);

			WorkflownoteVO worknoteVO = null;
			HashMap<String, String> eParam = new HashMap<String, String>();
			// 持久化工单VO
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.SAVE,
					"F3", billvo, null);
			Object obj = getPfBusiAction().processAction("SAVE", "F3",
					worknoteVO, billvo, null, eParam);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}
	}
	/**
	 * 获取客商pk
	 * @param code
	 * @return
	 */
	public String getCustomerPK(String code){
		String result = null;
		String sql = "SELECT pk_cust_sup FROM bd_cust_supplier WHERE CODE = '"+code+"' AND custenablestate = '2' AND NVL(dr,0)=0";
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
	//F3-Cxx-019	退款单	false	true	
	/**
	 * 是否拆税
	 * @param code
	 * @throws DAOException 
	 */
	public String getTaxsepara(String code,String select) throws DAOException{
		return (String)getBaseDAO().executeQuery("select "+select+" from bd_defdoc where bd_defdoc.pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code='zdy021') and code='"+code+"'", new ColumnProcessor());
	}
	
	/**
	 * 得到自定义项名称
	 * @param code
	 * @return
	 * @throws DAOException 
	 */
	public String getdefdocname(String code) throws DAOException{
		return (String)getBaseDAO().executeQuery("select name from bd_defdoc where bd_defdoc.pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code='zdy021') and code='"+code+"'", new ColumnProcessor());
	}
	/**
	 * 优先获取内部供应商
	 */
	public String getinner_supplier(String ...condons) throws DAOException{
		String sql = "SELECT pk_supplier,to_char(supprop) supprop FROM bd_supplier WHERE "
				+ " name = ? AND ( enablestate = 2 ) AND ((pk_supplier IN (SELECT pk_supplier FROM bd_suporg  WHERE "
				+ "pk_org IN ( ? ) AND enablestate = 2 UNION SELECT pk_supplier FROM bd_supplier WHERE ( pk_org = '000112100000000005FD' OR "
				+ "pk_org = ? ))))";
		List<Map<String, String>> listmap=(List<Map<String, String>>)getBaseDAO().executeQuery(sql, new MapListProcessor());
		if(listmap!=null){
			if(listmap.size()==1){
				return listmap.get(0).get("pk_supplier");
			}else if(listmap.size()>1){
				for(Map<String, String> map:listmap){
					if("1".equals(map.get("supprop"))){
						return map.get("pk_supplier");
					}
				}
				return listmap.get(0).get("pk_supplier");
			}
		}
		return null;
	}
}
