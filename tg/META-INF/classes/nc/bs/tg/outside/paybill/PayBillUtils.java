package nc.bs.tg.outside.paybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.arap.bill.ArapBillCalUtil;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.core.service.TimeService;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.cmp.bill.util.SysInit;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.pubitf.bbd.CurrtypeQuery;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.arap.sysinit.SysinitConst;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.pattern.pub.MathTool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 付款单
 * 
 * @author ASUS
 * 
 */
public abstract class PayBillUtils extends BillUtils implements ITGSyncService {
	String pk_tradetype = null;
	boolean isPushApprove = false;
	String defaultoperator = null;

	public String getDefaultoperator() {
		return DefaultOperator;
	}

	/**
	 * 是否推审核
	 * 
	 * @return
	 */
	public boolean isPushApprove() {
		return isPushApprove;
	}

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(getDefaultoperator()));
		InvocationInfoProxy.getInstance().setUserCode(getDefaultoperator());
		// 外系统信息
		JSONObject data = (JSONObject) info.get("data");// 外系统来源表头数据
		JSONObject jsonhead = (JSONObject) data.get("headInfo");// 外系统来源表头数据
		String srcid = jsonhead.getString("srcid");// 来源ID
		String srcno = jsonhead.getString("srcbillno");// 来源单据号
		Map<String, String> resultInfo = new HashMap<String, String>();
		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【"
						+ billkey
						+ "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue(
								PayBillVO.BILLNO) + "】,请勿重复上传!");
			}
			AggPayBillVO billvo = onTranBill(info);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVE",
					IBillFieldGet.F3, null, billvo, null, null);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;

			if (isPushApprove()) {
				approveSilently(billvo.getHeadVO().getPk_tradetype(),
						billvos[0].getPrimaryKey(), "Y", "审核通过", billvos[0]
								.getHeadVO().getCreator(), false);
			}
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected AggPayBillVO onTranBill(HashMap<String, Object> info)
			throws BusinessException {
		JSONObject data = (JSONObject) info.get("data");// 外系统来源表头数据
		JSONObject head = (JSONObject) data.get("headInfo");// 外系统来源表头数据
		JSONArray bodys = (JSONArray) data.get("bodyInfos");// 外系统来源表体数据
		AggPayBillVO billVO = onDefaultValue(head, bodys);
		// 主表信息转换
		UFDouble money = UFDouble.ZERO_DBL;// 金额
		UFDouble local_money = UFDouble.ZERO_DBL;// 组织金额
		UFDouble group_money = UFDouble.ZERO_DBL;// 集团金额
		UFDouble global_money = UFDouble.ZERO_DBL;// 全局金额
		// 明细信息转换
		List<PayBillItemVO> blists = new ArrayList<PayBillItemVO>();
		if (bodys != null && bodys.size() > 0) {
			for (int row = 0; row < bodys.size(); row++) {
				try {
					JSONObject body = bodys.getJSONObject(row);
					PayBillItemVO itmevo = (PayBillItemVO) billVO
							.getChildrenVO()[row];
					setItemVO((PayBillVO) billVO.getParentVO(), itmevo, body);
					calculate(billVO, IBillFieldGet.MONEY_DE, row);
					calculate(billVO, IBillFieldGet.MONEY_DE, row);
					itmevo.setOccupationmny(itmevo.getMoney_de());// 预占用原币余额
					money = money.add(itmevo.getMoney_cr());// 金额
					local_money = local_money.add(itmevo.getLocal_money_cr());// 组织金额
					group_money = group_money.add(itmevo.getGroupcrebit());// 集团金额
					global_money = global_money.add(itmevo.getGlobalcrebit());// 全局金额
					blists.add(itmevo);
				} catch (Exception e) {
					throw new BusinessException("行[" + (row + 1) + "],"
							+ e.getMessage(), e);
				}
			}
		}

		billVO.getParentVO().setAttributeValue(PayBillVO.MONEY, money);
		billVO.getParentVO().setAttributeValue(PayBillVO.LOCAL_MONEY,
				local_money);
		billVO.getParentVO().setAttributeValue(PayBillVO.GROUPLOCAL,
				group_money);
		billVO.getParentVO().setAttributeValue(PayBillVO.GLOBALLOCAL,
				global_money);

		return billVO;
	}

	/**
	 * 金额信息计算
	 * 
	 * @throws BusinessException
	 */
	protected void calculate(AggPayBillVO billvo, String key, int row)
			throws BusinessException {

		if (billvo.getChildrenVO() != null && billvo.getChildrenVO().length > 0) {
			PayBillItemVO itemVO = (PayBillItemVO) billvo.getChildrenVO()[row];
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

	protected AggPayBillVO onDefaultValue(JSONObject head, JSONArray bodylist)
			throws BusinessException {
		AggPayBillVO aggvo = new AggPayBillVO();
		PayBillVO hvo = new PayBillVO();
		String billdate = head.getString("billdate") == null ? new UFDate()
				.toString() : head.getString("billdate");
		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// 当前时间
		hvo.setAttributeValue(PayBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// 集团
		hvo.setAttributeValue(PayBillVO.ISINIT, UFBoolean.FALSE);// 期初标志
		hvo.setAttributeValue(PayBillVO.ISREDED, UFBoolean.FALSE);// 是否红冲过
		hvo.setAttributeValue(PayBillVO.BILLDATE, new UFDate(billdate));// 单据日期
		hvo.setAttributeValue(PayBillVO.BUSIDATE, new UFDate(billdate));// 起算日期
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// 单据会计年度
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// 单据会计期间
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);
		setHeaderVO(hvo, head);

		hvo.setAttributeValue(PayBillVO.BILLMAKER,
				getUserIDByCode(getDefaultoperator()));// 制单人
		hvo.setAttributeValue(PayBillVO.CREATOR, hvo.getBillmaker());// 创建人
		hvo.setAttributeValue(PayBillVO.CREATIONTIME, currTime);// 创建时间
		hvo.setAttributeValue(PayBillVO.PK_BILLTYPE, IBillFieldGet.F3);// 单据类型编码
		hvo.setAttributeValue(PayBillVO.BILLCLASS, IBillFieldGet.YF);// 单据大类
		hvo.setAttributeValue(PayBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据所属系统
		hvo.setAttributeValue(PayBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据来源系统
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE, IBillFieldGet.D1);// 应收类型code
		hvo.setAttributeValue(PayBillVO.BILLSTATUS, ARAPBillStatus.SAVE.VALUE);// 单据状态
		// 交易类型
		BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetype());

		hvo.setAttributeValue(PayBillVO.PK_TRADETYPEID,
				billTypeVo.getPk_billtypeid());// 应收类型
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE,
				billTypeVo.getPk_billtypecode());// 应收类型

		aggvo.setParentVO(hvo);
		PayBillItemVO[] itemVOs = new PayBillItemVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new PayBillItemVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// 行号
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
		getArapBillPubQueryService().getDefaultVO(aggvo, true);

		return aggvo;
	}

	/**
	 * 主体信息
	 * 
	 * @param hvo
	 * @param headvo
	 */
	protected abstract void setHeaderVO(PayBillVO hvo, JSONObject head)
			throws BusinessException;

	/**
	 * 明细信息
	 * 
	 * @param parentVO
	 * @param itmevo
	 * @param row
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected abstract void setItemVO(PayBillVO parentVO, PayBillItemVO itmevo,
			JSONObject body) throws BusinessException;

	/**
	 * 处理余额信息
	 * 
	 * @param itemVO
	 */
	protected void calulateBalance(PayBillItemVO itemVO) {
		boolean direction = (Integer) itemVO
				.getAttributeValue(IBillFieldGet.DIRECTION) == Direction.CREDIT.VALUE
				.intValue();
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
	protected void calulateTax(PayBillItemVO itemVO) {
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
	 * 相应接口下指向的交易类型
	 */
	protected abstract String getTradetype();
}
