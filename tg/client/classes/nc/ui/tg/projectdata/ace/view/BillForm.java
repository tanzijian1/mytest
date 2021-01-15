package nc.ui.tg.projectdata.ace.view;

import java.util.List;

import javax.swing.Action;
import javax.swing.SwingUtilities;

import nc.bs.logging.Logger;
import nc.ui.pub.bill.IBillItem;
import nc.ui.uif2.UIState;
import nc.vo.tg.projectdata.AggProjectDataVO;
import nc.vo.tg.projectdata.ProjectDataVO;

public class BillForm extends nc.ui.pubapp.uif2app.view.ShowUpableBillForm {
	
	protected void setBodyTabActive(String tabcode) {
		List<Action> actions = getBodyActionMap().get(tabcode);
		String srcSystem = (String) getBillCardPanel().getHeadItem(
				ProjectDataVO.SRCSYSTEM).getValueObject();
		billCardPanel.addParentTabAction(IBillItem.BODY, null);
		if ("DATADB".equals(srcSystem)) {
			if ("pk_projectdata_c".equals(tabcode)) {
				billCardPanel.addParentTabAction(IBillItem.BODY, actions);
			}

		} else {
			if ("pk_projectdata_c".equals(tabcode)
					|| "pk_projectdata_b".equals(tabcode) 
					|| "pk_residential_sales_b".equals(tabcode) || "pk_commercial_sales_b".equals(tabcode) 
					|| "pk_office_sales_b".equals(tabcode) || "pk_parking_sales_b".equals(tabcode) 
					|| "pk_construction_supporting_b".equals(tabcode)) {
				billCardPanel.addParentTabAction(IBillItem.BODY, actions);
			}

		}
		if (getBillCardPanel() != null) {
			String[] tabcodes = getBillCardPanel().getBillData()
					.getBodyTableCodes();
			for (String code : tabcodes) {
				getBillCardPanel().getBodyPanel(code).removeTableSortListener();
				//2020-08-26 LJF ����ҳǩ���޸�����
				if("yhrzfa".equals(code)||"pk_projectdata_n".equals(code)||"zbsc".equals(code) 
						|| "pk_residential_sales_b".equals(code) || "pk_commercial_sales_b".equals(code) 
						|| "pk_office_sales_b".equals(code) || "pk_parking_sales_b".equals(code) 
						|| "pk_construction_supporting_b".equals(code)){
					getBillCardPanel().getBodyPanel(code).setRowNOShow(false);
				}
			}
		}
	}

	/**
	 * �ڱ༭����������״̬�󱻵��á� �˷������ñ༭��Ĭ��ֵ.
	 */
	protected void setDefaultValue() {

	}

	protected void onAdd() {
		if (!"bt".equals(getNodekey())) {
			setNodekey("bt");
			initBillCardPanel();
		}

		super.onAdd();
	}

	protected void synchronizeDataFromModel() {
		Logger.debug("entering synchronizeDataFromModel");
		Object selectedData = getModel().getSelectedData();//��Ŀ���Ͽ�Ƭ���� self
		if (selectedData != null) {
			String srcSystem = ((AggProjectDataVO) selectedData).getParentVO()
					.getSrcsystem();
			if ("DATADB".equals(srcSystem)) {
				setNodekey("bt");//self�Ѿ�ʧЧ
			} else {
				setNodekey("bt");
			}
			if (getBillCardPanel() != null) {
				String[] tabcodes = getBillCardPanel().getBillData()
						.getBodyTableCodes();
				for (String code : tabcodes) {
					getBillCardPanel().getBodyPanel(code).removeTableSortListener();
					if("yhrzfa".equals(code)||"pk_projectdata_n".equals(code)||"zbsc".equals(code) 
							|| "pk_residential_sales_b".equals(code) || "pk_commercial_sales_b".equals(code) 
							|| "pk_office_sales_b".equals(code) || "pk_parking_sales_b".equals(code) 
							|| "pk_construction_supporting_b".equals(code)){
						getBillCardPanel().getBodyPanel(code).setRowNOShow(false);
					}
				}
			}
			initBillCardPanel();
		}
		setValue(selectedData);
		Logger.debug("leaving synchronizeDataFromModel");
	}

	public BillForm() {
		super();
		if (getBillCardPanel() != null) {
			String[] tabcodes = getBillCardPanel().getBillData()
					.getBodyTableCodes();
			for (String code : tabcodes) {
				getBillCardPanel().getBodyPanel(code).removeTableSortListener();
				if("yhrzfa".equals(code)||"pk_projectdata_n".equals(code)||"zbsc".equals(code) 
						|| "pk_residential_sales_b".equals(code) || "pk_commercial_sales_b".equals(code) 
						|| "pk_office_sales_b".equals(code) || "pk_parking_sales_b".equals(code) 
						|| "pk_construction_supporting_b".equals(code)){
					getBillCardPanel().getBodyPanel(code).setRowNOShow(false);
				}
			}
		}
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
				getBillCardPanel().getBodyPanel(code).removeTableSortListener();
				if("yhrzfa".equals(code)||"pk_projectdata_n".equals(code)||"zbsc".equals(code) 
						|| "pk_residential_sales_b".equals(code) || "pk_commercial_sales_b".equals(code) 
						|| "pk_office_sales_b".equals(code) || "pk_parking_sales_b".equals(code) 
						|| "pk_construction_supporting_b".equals(code)){
					getBillCardPanel().getBodyPanel(code).setRowNOShow(false);
				}
			}
		}
	}
}
