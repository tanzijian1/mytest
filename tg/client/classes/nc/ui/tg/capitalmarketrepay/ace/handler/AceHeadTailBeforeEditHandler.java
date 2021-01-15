package nc.ui.tg.capitalmarketrepay.ace.handler;

import nc.ui.bd.ref.model.CustBankaccDefaultRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;

public class AceHeadTailBeforeEditHandler implements IAppEventHandler<CardHeadTailBeforeEditEvent>{

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		String pk_org = (String)e.getBillCardPanel().getHeadItem("pk_org").getValueObject();
		if("def5".equals(e.getKey())){//单期发行编号
			UIRefPane refpane = (UIRefPane)e.getBillCardPanel().getHeadItem(e.getKey()).getComponent();
			String wheresql = "and pk_org ='"+pk_org+"'";
			refpane.getRefModel().addWherePart(wheresql);
		}
		if("def8".equals(e.getKey())){//收款单位账户
			String pk_cust= (String) e.getBillCardPanel().getHeadItem("def7").getValueObject();
			UIRefPane refpane = (UIRefPane)e.getBillCardPanel().getHeadItem(e.getKey()).getComponent();
			nc.ui.bd.ref.model.CustBankaccDefaultRefModel ref=(CustBankaccDefaultRefModel) refpane.getRefModel();
			ref.setPk_cust(pk_cust);
		}
		/*if("def9".equals(e.getKey())){//付款单位
			String pk_singleissue = (String)e.getBillCardPanel().getHeadItem("def5").getValueObject();
			UIRefPane refpane = (UIRefPane)e.getBillCardPanel().getHeadItem(e.getKey()).getComponent();
			String wheresql = "and pk_financeorg in(select pk_org from sdfn_singleissue where pk_singleissue = '"+pk_singleissue+"' and dr=0)";
			refpane.getRefModel().addWherePart(wheresql);
		}*/
		if("def10".equals(e.getKey())){//付款单位账户
			String org = (String)e.getBillCardPanel().getHeadItem("def9").getValueObject();
			UIRefPane refpane = (UIRefPane)e.getBillCardPanel().getHeadItem(e.getKey()).getComponent();
			refpane.getRefModel().setPk_org(org);
		}
		e.setReturnValue(true);
	}

}
