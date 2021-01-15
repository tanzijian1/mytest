package nc.ui.tg.capitalmarketrepay.ace.view;

public class CapitalBillForm extends nc.ui.pubapp.uif2app.view.ShowUpableBillForm{

	@Override
	public void initUI() {
		// TODO 自动生成的方法存根
		super.initUI();
		
		//禁止排序
        this.billCardPanel.getBillTable("pk_marketrepaley_b").setSortEnabled(false);
	}
		
}
