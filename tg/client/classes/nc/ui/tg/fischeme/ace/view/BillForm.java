package nc.ui.tg.fischeme.ace.view;

import java.awt.BorderLayout;

public class BillForm extends nc.ui.pubapp.uif2app.view.ShowUpableBillForm {
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
