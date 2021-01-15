package nc.ui.tg.approvalpro.ace.view;

public class ShowUpableBillListView extends nc.ui.pubapp.uif2app.view.ShowUpableBillListView{

	@Override
	public void initUI() {
		// TODO 自动生成的方法存根
		super.initUI();
		if (getBillListPanel() != null) {
			String[] tabcodes = getBillListPanel().getBillListData()
					.getBodyTableCodes();
			for (String code : tabcodes) {
				getBillListPanel().getBodyScrollPane(code).removeTableSortListener();
				getBillListPanel().getBodyScrollPane(code).setRowNOShow(false);
			}
		}
	}

	
}
