package nc.ui.tg.approvalpro.ace.view;


public class BillForm extends nc.ui.pubapp.uif2app.view.ShowUpableBillForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1606497519981487746L;

	public BillForm() {
		super();
	}

	/**
	 * ��д�˷�����Ϊ�˲���Ĭ�ϵ���ֶκ�����
	 */
	@Override
	public void initUI() {
		super.initUI();
		if (getBillCardPanel() != null) {
			String[] tabcodes = getBillCardPanel().getBillData()
					.getBodyTableCodes();
			for (String code : tabcodes) {
				getBillCardPanel().getBodyPanel(code).removeTableSortListener();//setRowNOShow
				getBillCardPanel().setRowNOShow(code, false);
			}
		}
	}

}
