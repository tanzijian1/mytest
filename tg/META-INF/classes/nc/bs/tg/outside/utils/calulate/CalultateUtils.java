package nc.bs.tg.outside.utils.calulate;

import nc.bs.arap.bill.ArapBillCalUtil;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.fi.pub.SysInit;
import nc.pubitf.bbd.CurrtypeQuery;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.arap.basebill.BaseAggVO;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.sysinit.SysinitConst;
import nc.vo.arappub.calculator.data.RelationItemForCal_Credit;
import nc.vo.arappub.calculator.data.RelationItemForCal_Debit;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.Calculator;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.calculator.data.IRelationForItems;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.scale.ScaleUtils;

/**
 * 借方方向业务单金额计算金额[应收单、款单]
 * 
 * @author ASUS
 * 
 */
public class CalultateUtils {

	static CalultateUtils utils = null;

	public static CalultateUtils getUtils() {
		if (utils == null) {
			utils = new CalultateUtils();
		}
		return utils;
	}

	public void onCalculate(BaseAggVO billvo, String key, int direction,
			int rowcount) throws BusinessException {
		for (int row = 0; row < rowcount; row++) {
			calculate(billvo, key, row, direction);
		}

	}

	/**
	 * 金额信息计算
	 * 
	 * @param direction
	 * 
	 * @throws BusinessException
	 */
	public void calculate(BaseAggVO billvo, String key, int row, int direction)
			throws BusinessException {
		if (billvo.getChildrenVO() != null && billvo.getChildrenVO().length > 0) {
			BaseItemVO itemVO = (BaseItemVO) billvo.getChildrenVO()[row];
			String currType = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_CURRTYPE);
			String pk_org = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_ORG);
			String pk_group = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_GROUP);
			String pk_currtype = CurrencyRateUtilHelper.getInstance()
					.getLocalCurrtypeByOrgID(pk_org);

			ScaleUtils scale = new ScaleUtils(pk_group);
			IRelationForItems relationForItems = direction == Direction.CREDIT.VALUE
					.intValue() ? new RelationItemForCal_Credit()
					: new RelationItemForCal_Debit();
			IDataSetForCal data = new ArapDataDataSet(billvo, row,
					relationForItems);
			Calculator tool = new Calculator(data, scale);
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
			tool.calculate(cond, key);
		}

	}

	/**
	 * 处理余额信息
	 * 
	 * @param itemVO
	 */
	private void calulateBalance(BaseItemVO itemVO) {
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
	private void calulateTax(BaseItemVO itemVO) {
		Object buysellflag = itemVO.getBuysellflag();
		boolean isInternational = BillEnumCollection.BuySellType.OUT_BUY.VALUE
				.equals(buysellflag)
				|| BillEnumCollection.BuySellType.OUT_SELL.VALUE
						.equals((buysellflag));

		boolean direction = (Integer) itemVO
				.getAttributeValue(IBillFieldGet.DIRECTION) == Direction.CREDIT.VALUE
				.intValue();
		String grouptaxmny_col = direction ? IBillFieldGet.GROUPCREBIT
				: IBillFieldGet.GROUPDEBIT;
		String groupnotaxmny_col = direction ? IBillFieldGet.GROUPNOTAX_CRE
				: IBillFieldGet.GROUPNOTAX_DE;
		String grouptax_col = direction ? IBillFieldGet.GROUPTAX_CRE
				: IBillFieldGet.GROUPTAX_DE;
		String globaltaxmny_col = direction ? IBillFieldGet.GLOBALCREBIT
				: IBillFieldGet.GLOBALDEBIT;
		String globalnotaxmny_col = direction ? IBillFieldGet.GLOBALNOTAX_CRE
				: IBillFieldGet.GLOBALNOTAX_DE;
		String globaltax = direction ? IBillFieldGet.GLOBALTAX_CRE
				: IBillFieldGet.GLOBALTAX_DE;

		UFDouble grouptaxmny = (UFDouble) itemVO
				.getAttributeValue(grouptaxmny_col);

		UFDouble groupnotaxmny = (UFDouble) itemVO
				.getAttributeValue(groupnotaxmny_col);
		itemVO.setAttributeValue(grouptax_col,
				MathTool.sub(grouptaxmny, groupnotaxmny));

		UFDouble globaltaxmny = (UFDouble) itemVO
				.getAttributeValue(globaltaxmny_col);
		UFDouble globalnotaxmny = (UFDouble) itemVO
				.getAttributeValue(globalnotaxmny_col);
		itemVO.setAttributeValue(globaltax,
				MathTool.sub(globaltaxmny, globalnotaxmny));
		if (isInternational) {
			if (direction) {
				itemVO.setGlobalnotax_cre(itemVO.getGlobalcrebit());
				itemVO.setGroupnotax_cre(itemVO.getGroupcrebit());
				itemVO.setLocal_notax_cr(itemVO.getLocal_money_cr());
				itemVO.setNotax_cr(itemVO.getMoney_cr());
				itemVO.setCaltaxmny(itemVO.getLocal_notax_cr());
			} else {
				itemVO.setGlobalnotax_de(itemVO.getGlobaldebit());
				itemVO.setGroupnotax_de(itemVO.getGroupdebit());
				itemVO.setLocal_notax_de(itemVO.getLocal_money_de());
				itemVO.setNotax_de(itemVO.getMoney_de());
				itemVO.setCaltaxmny(itemVO.getLocal_notax_de());
			}
		}
	}
}
