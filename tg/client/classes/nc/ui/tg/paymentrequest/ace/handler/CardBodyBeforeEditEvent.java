package nc.ui.tg.paymentrequest.ace.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.ui.arap.viewhandler.AbstractBillHandler;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.model.CustBankaccDefaultRefModel;
import nc.ui.bd.ref.model.PsnbankaccDefaultRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.vo.arap.pub.BillEnumCollection.ObjType;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.cashaccount.CashAccountVO;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.fipub.utils.RefConstant;

public class CardBodyBeforeEditEvent extends AbstractBillHandler<nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent>{

	/*
	 * 表体根据表头组织带出银行信息
	 */
	public static final List<String> escapes = Arrays.asList(new String[] {"def5"});

	boolean isInit = true;
	
	@Override
	protected void handle() {
		BillCardPanel panel = getBillCardPanel();

		Integer objtype = 1;

		String pk_billtype = F3;
		BillItem def5 = panel.getBodyItem("def5");
		setRefFilter(def5, panel, pk_billtype);
		if (IBillFieldGet.F2.equals(pk_billtype) || IBillFieldGet.F3.equals(pk_billtype)) {
			// 收款、付款：设置现金账户过滤条件
			setRefFilter(panel.getHeadItem(IBillFieldGet.CASHACCOUNT), panel, pk_billtype);
		}
		
	}
	
	
	static boolean needResetRefModel(BillItem billItem, int objValue) {
		String refname = null;
		if (objValue == ObjType.CUSTOMER.VALUE.intValue()) {
			refname = RefConstant.REF_NODENAME_ACC_CUSTOMER;
		} else if (objValue == ObjType.PERSON.VALUE.intValue()) {
			refname = RefConstant.REF_NODENAME_ACC_PSN;
		} else if (objValue == ObjType.DEP.VALUE.intValue()) {
			refname = "";
		} else if (objValue == ObjType.SUPPLIER.VALUE.intValue()) {
			refname = RefConstant.REF_NODENAME_ACC_SUPPILER;
		}
		return needResetRefModel(billItem, refname);
	}

	static boolean needResetRefModel(BillItem billitem, String refname) {
		if (billitem == null || billitem.getComponent() == null) {
			return false;
		}

		if (StringUtils.isEmpty(refname)) {
			return true;
		}

		UIRefPane uiRefPane = (UIRefPane) billitem.getComponent();
		AbstractRefModel refModel = uiRefPane.getRefModel();
		if (refModel == null) {
			uiRefPane.setRefNodeName(RefConstant.REF_NODENAME_ACC_CUSUP);
			refModel = uiRefPane.getRefModel();
		}

		return !refModel.getRefNodeName().equals(refname);
	}

	private void setRefFilter(BillItem item, BillCardPanel panel, String pk_billtype) {
		if (item == null || item.getComponent() == null) {
			return;
		}

		// 表头财务组织
		String pk_org = (getHeadValue(IBillFieldGet.PK_ORG) == null ? ""
				: (String) getHeadValue(IBillFieldGet.PK_ORG));

		UIRefPane ref = (UIRefPane) item.getComponent();

		if (ref.getRefModel() != null) {
			if (!pk_org.equals(ref.getRefModel().getPk_org())) {
				ref.setPk_org(pk_org);
			}
		}

		String value = null;
		if (RefConstant.REF_NODENAME_CASHACCOUNT.equals(ref.getRefNodeName())) {
			// 现金账户过滤设置：根据主组织+币种过滤
			value = (String) getHeadValue(IBillFieldGet.PK_CURRTYPE);
			String wherePart = " and " + CashAccountVO.PK_ORG + " = '" + pk_org + "' and "
					+ CashAccountVO.PK_MONEYTYPE + " = '" + value + "' ";
			ref.getRefModel().addWherePart(wherePart);
		}
	}
	
	private String getCurrencyWherePart() {
		StringBuffer appending = new StringBuffer();
		appending.append(" and ").append(BankAccSubVO.PK_CURRTYPE);
		appending.append(" = '").append((String) getHeadValue(IBillFieldGet.PK_CURRTYPE)).append("'");
		return appending.toString();
	}
	private String getBankWherePart() {
		return getCurrencyWherePart()+getEnablestate();
	}
	private String getEnablestate() {
		StringBuffer appending = new StringBuffer();
		appending.append(" and ").append(BankAccbasVO.ENABLESTATE);
		appending.append(" =  ").append(IPubEnumConst.ENABLESTATE_ENABLE );
		return appending.toString();
	}
	public static void setRefModel(BillItem item, String refname) {
		if ((item == null || item.getComponent() == null)) {
			return;
		}
		UIRefPane ref = (UIRefPane) item.getComponent();
		ref.setRefNodeName(refname);
	}

	@Override
	protected Collection<String> getFilterKey() {
		return escapes;
	}

}
