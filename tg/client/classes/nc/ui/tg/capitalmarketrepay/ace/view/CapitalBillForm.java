package nc.ui.tg.capitalmarketrepay.ace.view;

public class CapitalBillForm extends nc.ui.pubapp.uif2app.view.ShowUpableBillForm{

	@Override
	public void initUI() {
		// TODO �Զ����ɵķ������
		super.initUI();
		
		//��ֹ����
        this.billCardPanel.getBillTable("pk_marketrepaley_b").setSortEnabled(false);
	}
		
}
