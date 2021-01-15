package nc.bs.hcm.event.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.arap.bill.ArapBillCalUtil;
import nc.bs.businessevent.BusinessEvent;
import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.IEventType;
import nc.bs.businessevent.BusinessEvent.BusinessUserObj;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.cmp.bill.util.SysInit;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPfExchangeService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.message.util.IDefaultMsgConst;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.pubitf.bbd.CurrtypeQuery;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.ui.fa.newasset.view.newasset_config;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.sysinit.SysinitConst;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;

/**
 * 调用社保付款单生成社保往来应付单 单据转换规则
 * @author zhaozhiying
 *
 */
public class ApproveF3PayBillAfterDataListener extends EBSBillUtils implements IBusinessListener {

	private IPfExchangeService ips = null;
	/**
	 * VO转换服务类
	 * 
	 * @return
	 */
	public IPfExchangeService exchangeservice() {
		if (ips == null) {
			ips = NCLocator.getInstance().lookup(IPfExchangeService.class);
		}
		return ips;
	}
	
	@Override
	public void doAction(IBusinessEvent event) throws BusinessException {
		
	    try {
	        //付款单
	        Object obj = ((BusinessUserObj) event.getUserObject()).getUserObj();
			if (obj == null) {
				return;
			}
			AggregatedValueObject[] aggPayBillVOs = (AggregatedValueObject[]) obj;
	        
	        for (AggregatedValueObject vo : aggPayBillVOs) {
	        	AggPayBillVO billVO = (AggPayBillVO)vo;
	        	
	        	PayBillVO headVo = (PayBillVO)billVO.getParentVO();
	        	PayBillItemVO[] itemVos = (PayBillItemVO[])billVO.getAllChildrenVO();
	        	
	        	//单据类型
	        	String pkTradeType = headVo.getPk_tradetype();
	        	//社保购买公司pk
	        	String sbpk = headVo.getPk_org();
	        	//判断是否为社保应付单
	        	if("F3-Cxx-030".equals(pkTradeType)){
	        		for (PayBillItemVO bodyvo : itemVos) {
						String htqdpk = bodyvo.getDef18();
						
						//如果社保购买公司!=合同签订公司时，付款单审批通过触发生成合同签订公司的社保往来应付单
						if(StringUtils.isNotBlank(sbpk) && StringUtils.isNotBlank(htqdpk) && !sbpk.equals(htqdpk)){
							
							//AggPayableBillVO billvo = (AggPayableBillVO)
							
							convertBill(headVo,bodyvo);
						}
					}
	        	}
			}
	      //}
	    }
	    catch (Exception e) {
	      ExceptionUtils.marsh(e);
	    }
	}
	
	/**
	 * 社保付款单生成社保往来应付单
	 * @param headVo
	 * @param bodyvo
	 * @throws BusinessException
	 */
	public void convertBill(PayBillVO headVo,PayBillItemVO bodyvo) throws BusinessException{

		try {
			
			BaseDAO baseDAO = new BaseDAO();
			
			AggPayableBillVO aggVo = new AggPayableBillVO();
			List<PayableBillItemVO> bodylist = new ArrayList<>();
			
			//将表头转换成表体
			PayableBillVO payableBillVO = new PayableBillVO();
			
			payableBillVO.setPk_org(bodyvo.getDef18());
			payableBillVO.setPk_balatype(headVo.getPk_balatype());
			payableBillVO.setBilldate(headVo.getBilldate());
			payableBillVO.setDef17(headVo.getDef67());
			
			payableBillVO.setDef16(bodyvo.getDef55());
			payableBillVO.setDef24(bodyvo.getDef56());
			payableBillVO.setMoney(bodyvo.getMoney_de());
			
			payableBillVO.setDef15(headVo.getDef15());
			payableBillVO.setDef1(headVo.getDef1());
			payableBillVO.setDef2(headVo.getDef2());
			
			payableBillVO.setPk_group(bodyvo.getPk_group());
			payableBillVO.setPk_billtype("F1");
			payableBillVO.setBillclass("yf");
			payableBillVO.setLocal_money(bodyvo.getMoney_cr());
			payableBillVO.setSyscode(1);
			payableBillVO.setBillmaker(headVo.getCreator());
			payableBillVO.setObjtype(1);
			payableBillVO.setCreator(headVo.getCreator());
			payableBillVO.setPk_currtype("1002Z0100000000001K1");
			payableBillVO.setPk_tradetype("F1-Cxx-014");
			payableBillVO.setPk_tradetypeid("1001341000000057AILN");
			payableBillVO.setApprovestatus(-1);
			payableBillVO.setSrc_syscode(1);
			payableBillVO.setPk_busitype("1001341000000057FW29");
			payableBillVO.setStatus(VOStatus.NEW);
			payableBillVO.setBillstatus(2);
			
			// 新增 公司性质、资本化字段 2020-11-23 zzy start
			List<Map<String, String>> budgetsubNames = getLinkCompany(payableBillVO.getPk_org());
			if(budgetsubNames != null && budgetsubNames.size() >0){
				for (Map<String, String> names : budgetsubNames) {
					payableBillVO.setAttributeValue("def64", names.get("factorvalue5"));// 公司性质
					payableBillVO.setAttributeValue("def65", names.get("factorvalue4"));// 是否资本化
				}
			}
			// 新增 公司性质、资本化字段 2020-11-23 zzy end
			
			
			//表体转换成表头
			PayableBillItemVO itemVO = new PayableBillItemVO();
			itemVO.setPk_org(headVo.getPk_org());
			itemVO.setDef18(headVo.getPk_org());
			itemVO.setDef30(bodyvo.getDef30());

			itemVO.setDef22(bodyvo.getDef22());
			String supplier_pk = getSupplierIDByPkOrg(headVo.getPk_org());
			/*itemVO.setSupplier(bodyvo.getSupplier());
			itemVO.setRecaccount(bodyvo.getRecaccount());*/
			if(StringUtils.isNotBlank(supplier_pk)){
				itemVO.setSupplier(supplier_pk);
			}
			//itemVO.setSupplier(bodyvo.getSupplier());
			//itemVO.setRecaccount(bodyvo.getRecaccount());
			itemVO.setDef12(bodyvo.getDef12());
			itemVO.setDef11(bodyvo.getDef11());
			itemVO.setDef51(bodyvo.getDef51());
			itemVO.setDef52(bodyvo.getDef52());
			itemVO.setDef10(bodyvo.getDef10());
			itemVO.setDef9(bodyvo.getDef9());
			itemVO.setDef8(bodyvo.getDef8());
			itemVO.setDef7(bodyvo.getDef7());
			itemVO.setDef5(bodyvo.getDef5());
			itemVO.setDef4(bodyvo.getDef4());
			itemVO.setDef3(bodyvo.getDef3());
			itemVO.setDef2(bodyvo.getDef2());
			
			itemVO.setDef55(bodyvo.getDef55());
			itemVO.setDef56(bodyvo.getDef56());
			
			//新增字段 2020-10-26 zhaozhiying start
			/*itemVO.setDef57(bodyvo.getDef57());
			itemVO.setDef58(bodyvo.getDef58());
			itemVO.setDef59(bodyvo.getDef59());
			itemVO.setDef60(bodyvo.getDef60());
			itemVO.setDef53(bodyvo.getDef61());
			itemVO.setDef54(bodyvo.getDef62());*/
			
			itemVO.setDef57((String)bodyvo.getAttributeValue("def57"));
			itemVO.setDef58((String)bodyvo.getAttributeValue("def58"));
			itemVO.setDef59((String)bodyvo.getAttributeValue("def59"));
			itemVO.setDef60((String)bodyvo.getAttributeValue("def60"));
			itemVO.setDef53((String)bodyvo.getAttributeValue("def61"));
			itemVO.setDef54((String)bodyvo.getAttributeValue("def62"));
			
			itemVO.setMoney_bal(bodyvo.getMoney_de());
			itemVO.setLocal_money_bal(bodyvo.getMoney_de());
			itemVO.setLocal_notax_cr(bodyvo.getMoney_de());
			
			//新增字段 end
			
			//itemVO.setMoney_de(bodyvo.getMoney_cr());
			
			itemVO.setLocal_tax_cr(bodyvo.getMoney_de());
			itemVO.setNotax_cr(bodyvo.getMoney_de());
			itemVO.setLocal_money_cr(bodyvo.getMoney_de());
			
			itemVO.setAttributeValue(PayableBillItemVO.DIRECTION,
				    BillEnumCollection.Direction.CREDIT.VALUE);// 方向
			
			itemVO.setMoney_cr(bodyvo.getMoney_de());
			
			itemVO.setObjtype(1);
			itemVO.setPk_group(headVo.getPk_group());
			itemVO.setBusidate(bodyvo.getBilldate());
			itemVO.setBillclass(payableBillVO.getBillclass());
			itemVO.setPk_currtype(payableBillVO.getPk_currtype());
			itemVO.setPk_tradetype(payableBillVO.getPk_tradetype());
			itemVO.setPk_tradetypeid(payableBillVO.getPk_tradetypeid());
			itemVO.setPk_billtype(payableBillVO.getPk_billtype());
			itemVO.setBilldate(bodyvo.getBilldate());
			itemVO.setPausetransact(UFBoolean.FALSE);
			itemVO.setStatus(VOStatus.NEW);
			itemVO.setRate(bodyvo.getRate());
			
			/*itemVO.setTop_billid(headVo.getPk_paybill()); // 上层单据主键
			itemVO.setTop_tradetype(headVo.getPk_tradetype()); // 上层交易类型
			itemVO.setTop_billtype("F3"); // 上层单据类型
			itemVO.setAttributeValue("top_itemid",bodyvo.getPk_payitem());// 上层单据行主键
*/			
			aggVo.setParentVO(payableBillVO);
			
			bodylist.add(itemVO);
			aggVo.setChildrenVO(bodylist.toArray(new PayableBillItemVO[0]));
			
			calculate(aggVo, IBillFieldGet.MONEY_BAL, 0);
			calculate(aggVo, IBillFieldGet.MONEY_CR, 0);
			calculate(aggVo, IBillFieldGet.LOCAL_TAX_CR, 0);
			
			//保存
			HashMap eParam = new HashMap<>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED, PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = (AggPayableBillVO[])getPfBusiAction().processAction("SAVE", "F1", null, aggVo, null, eParam);
			
			AggPayableBillVO[] billvos = (AggPayableBillVO[]) obj;
			WorkflownoteVO worknoteVO = NCLocator.getInstance().lookup(IWorkflowMachine.class)
					   .checkWorkFlow("SAVE", "F1", billvos[0], eParam);
			obj = (AggPayableBillVO[]) getPfBusiAction().processAction(
					"SAVE", "F1", worknoteVO, billvos[0], null, eParam);
			
		} catch (BusinessException e) {
			//ExceptionUtils.marsh(e);
			String string = e.getMessage();
			throw new BusinessException(e.getMessage(), e);
		}
	}

	/**
	 * 使用转换规则
	 * @param headVo
	 * @param bodyvo
	 * @throws BusinessException
	 */
	public void changeBillByRule(PayBillVO headVo,PayBillItemVO bodyvo) throws BusinessException{
		AggPayBillVO billVO = new AggPayBillVO();
		billVO.setParentVO(headVo);
		List<PayBillItemVO> bodylist = new ArrayList<>();
		bodylist.add(bodyvo);
		billVO.setChildrenVO(bodylist.toArray(new PayBillItemVO[0]));
		
		AggPayableBillVO aggVo = null;
		try {
			AggregatedValueObject newbusivo = exchangeservice().runChangeData("F3-Cxx-031", "F1-Cxx-015", billVO, null);
			Boolean istrue = newbusivo == null ;
			if(istrue){
				throw new BusinessException("HCM公积金付款申请单["
						+ headVo.getAttributeValue("pk_billtype")
						+ "]转换公积金往来应付单异常 [未转换成功]!");
			}else {
				aggVo = (AggPayableBillVO)newbusivo;
				//保存
				HashMap eParam = new HashMap<>();
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED, PfUtilBaseTools.PARAM_NOTE_CHECKED);
				getPfBusiAction().processAction("SAVE", "F1", null, aggVo, null, eParam);
			}
		} catch (BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
	/**
	 * 根据【pk_org】获取主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getSupplierIDByPkOrg(String pk_org) throws BusinessException {
		IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select c.pk_supplier from bd_supplier c where c.pk_financeorg = (select org.pk_financeorg from org_financeorg org where org.pk_financeorg ='"
				+ pk_org + "')";
		//String result = (String) getBaseDAO().executeQuery(sql,new ColumnProcessor());
		String result = (String) service.executeQuery(sql, new ColumnProcessor());
		return result;
	}
	
	
	/**
	 * 金额信息计算
	 * 
	 * @throws BusinessException
	 */
	protected void calculate(AggPayableBillVO billvo, String key, int row)
			throws BusinessException {

		if (billvo.getChildrenVO() != null && billvo.getChildrenVO().length > 0) {
			PayableBillItemVO itemVO = (PayableBillItemVO) billvo
					.getChildrenVO()[row];
			String currType = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_CURRTYPE);
			String pk_org = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_ORG);
			String pk_group = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_GROUP);
			String pk_currtype = CurrencyRateUtilHelper.getInstance()
					.getLocalCurrtypeByOrgID(pk_org);
			Condition cond = new Condition();
			if (pk_currtype == null) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("2006pub_0", "02006pub-0552"));
			}
			CurrtypeVO currTypeVo = CurrtypeQuery.getInstance().getCurrtypeVO(
					pk_currtype);
			String destCurrType = currTypeVo.getPk_currtype();
			cond.setCalOrigCurr(true);
			if ((IBillFieldGet.LOCAL_MONEY_CR.equals(key) || IBillFieldGet.LOCAL_MONEY_DE
					.equals(key)) && !destCurrType.equals(currType)) {// 组织当前币种为组织本币
				cond.setCalOrigCurr(false);
			}
			cond.setIsCalLocalCurr(true);
			cond.setIsChgPriceOrDiscount(false);
			cond.setIsFixNchangerate(false);
			cond.setIsFixNqtunitrate(false);
			Object buysellflag = itemVO
					.getAttributeValue(IBillFieldGet.BUYSELLFLAG);
			boolean isInternational = BillEnumCollection.BuySellType.OUT_BUY.VALUE
					.equals(buysellflag)
					|| BillEnumCollection.BuySellType.OUT_SELL.VALUE
							.equals(buysellflag);
			cond.setInternational(isInternational);

			String AP21 = SysInit.getParaString(pk_org, SysinitConst.AP21);
			cond.setIsTaxOrNet(SysinitConst.ARAP21_TAX.equals(AP21));

			cond.setGroupLocalCurrencyEnable(ArapBillCalUtil
					.isUseGroupMoney(pk_group));
			cond.setGlobalLocalCurrencyEnable(ArapBillCalUtil
					.isUseGlobalMoney());
			cond.setOrigCurToGlobalMoney(ArapBillCalUtil
					.isOrigCurToGlobalMoney());
			cond.setOrigCurToGroupMoney(ArapBillCalUtil
					.isOrigCurToGroupMoney(pk_group));
			calulateTax(itemVO);
			calulateBalance(itemVO);
		}

	}
	
	/**
	 * 处理余额信息
	 * 
	 * @param itemVO
	 */
	protected void calulateBalance(PayableBillItemVO itemVO) {
		/*boolean direction = (Integer) itemVO
				.getAttributeValue(IBillFieldGet.DIRECTION) == Direction.CREDIT.VALUE
				.intValue();*/
		boolean direction = false;
		String local_money = direction ? "local_money_cr" : "local_money_de";
		String money = direction ? "money_cr" : "money_de";
		String quantity = direction ? "quantity_cr" : "quantity_de";
		String group_money = direction ? "groupcrebit" : "groupdebit";
		String global_money = direction ? "globalcrebit" : "globaldebit";
		String money_bal = "money_bal";
		String local_notax = direction ? BaseItemVO.LOCAL_NOTAX_CR
				: BaseItemVO.LOCAL_NOTAX_DE;
		String group_notax = direction ? IBillFieldGet.GROUPNOTAX_CRE
				: IBillFieldGet.GROUPNOTAX_DE;
		String global_notax = direction ? IBillFieldGet.GLOBALNOTAX_CRE
				: IBillFieldGet.GLOBALNOTAX_DE;
		String group_tax = direction ? IBillFieldGet.GROUPTAX_CRE
				: IBillFieldGet.GROUPTAX_DE;
		String global_tax = direction ? IBillFieldGet.GLOBALTAX_CRE
				: IBillFieldGet.GLOBALTAX_DE;

		itemVO.setAttributeValue(money_bal,
				itemVO.getAttributeValue(money) == null ? UFDouble.ZERO_DBL
						: itemVO.getAttributeValue(money));
		itemVO.setAttributeValue(IBillFieldGet.LOCAL_MONEY_BAL, itemVO
				.getAttributeValue(local_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(local_money));

		itemVO.setAttributeValue(IBillFieldGet.GROUPBALANCE, itemVO
				.getAttributeValue(group_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(group_money));

		itemVO.setAttributeValue(IBillFieldGet.GLOBALBALANCE, itemVO
				.getAttributeValue(global_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(global_money));
		itemVO.setAttributeValue(IBillFieldGet.QUANTITY_BAL, itemVO
				.getAttributeValue(quantity) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(quantity));
		itemVO.setAttributeValue(IBillFieldGet.OCCUPATIONMNY, itemVO
				.getAttributeValue(money_bal) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(money_bal));

		String caltaxmny_key = BillEnumCollection.TaxType.TAXOUT.VALUE
				.equals(itemVO.getAttributeValue(BaseItemVO.TAXTYPE)) ? local_notax
				: local_money;
		itemVO.setAttributeValue(IBillFieldGet.CALTAXMNY, itemVO
				.getAttributeValue(caltaxmny_key) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(caltaxmny_key));

		itemVO.setAttributeValue(
				group_tax,
				MathTool.sub(
						itemVO.getAttributeValue(group_money) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(group_money),
						(UFDouble) itemVO.getAttributeValue(group_notax) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(group_notax)));

		itemVO.setAttributeValue(
				global_tax,
				MathTool.sub(
						itemVO.getAttributeValue(global_money) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(global_money),
						(UFDouble) itemVO.getAttributeValue(global_notax) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(global_notax)));
	}
	
	/***
	 * 计算无税金额信息
	 * 
	 * @param itemVO
	 */
	protected void calulateTax(PayableBillItemVO itemVO) {
		Object buysellflag = itemVO.getBuysellflag();
		boolean isInternational = BillEnumCollection.BuySellType.OUT_BUY.VALUE
				.equals(buysellflag)
				|| BillEnumCollection.BuySellType.OUT_SELL.VALUE
						.equals((buysellflag));
		UFDouble grouptaxmny = itemVO.getGroupcrebit();
		UFDouble groupnotaxmny = itemVO.getGroupnotax_cre();
		itemVO.setGrouptax_cre(MathTool.sub(grouptaxmny, groupnotaxmny));

		UFDouble globaltaxmny = itemVO.getGlobalcrebit();
		UFDouble globalnotaxmny = itemVO.getGlobalnotax_cre();
		itemVO.setGlobaltax_cre(MathTool.sub(globaltaxmny, globalnotaxmny));
		if (isInternational) {
			itemVO.setGlobalnotax_cre(itemVO.getGlobalcrebit());
			itemVO.setGroupnotax_cre(itemVO.getGroupcrebit());
			itemVO.setLocal_notax_cr(itemVO.getLocal_money_cr());
			itemVO.setNotax_cr(itemVO.getMoney_cr());
			itemVO.setCaltaxmny(itemVO.getLocal_notax_cr());
		}
	}
	
	/**
	 * 获取是否资本化和公司性质 测试环境的档案编码是XM02、正式的是XM01
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	 protected List<Map<String, String>> getLinkCompany(String pk_org) throws BusinessException {
		 IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		 List<Map<String, String>> result = null;
		 StringBuffer query = new StringBuffer();
		 query.append("select factorvalue4,factorvalue5 from fip_docview_b where pk_classview=(select pk_classview from fip_docview ta where ta.viewcode ='XM01') and factorvalue2 = '"
				 + pk_org + "'");
		 try {
			 result = (List<Map<String, String>>) service.executeQuery(query.toString(), new MapListProcessor());
		 } catch (Exception e) {
			 throw new BusinessException(e.getMessage(), e);
		 }
		 return result;
	 } 
	
}
