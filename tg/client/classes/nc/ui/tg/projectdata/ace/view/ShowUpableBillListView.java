package nc.ui.tg.projectdata.ace.view;

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
				if("yhrzfa".equals(code)||"pk_projectdata_n".equals(code)||"zbsc".equals(code) 
						|| "pk_residential_sales_b".equals(code) || "pk_commercial_sales_b".equals(code) 
						|| "pk_office_sales_b".equals(code) || "pk_parking_sales_b".equals(code) 
						|| "pk_construction_supporting_b".equals(code)){
					getBillListPanel().getBodyScrollPane(code).setRowNOShow(false);
				}
			}
		}
	}
}
