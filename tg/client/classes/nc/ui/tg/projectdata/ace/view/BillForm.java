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
				//2020-08-26 LJF 新增页签可修改新增
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
	 * 在编辑器进入新增状态后被调用。 此方法设置编辑器默认值.
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
		Object selectedData = getModel().getSelectedData();//项目资料卡片界面 self
		if (selectedData != null) {
			String srcSystem = ((AggProjectDataVO) selectedData).getParentVO()
					.getSrcsystem();
			if ("DATADB".equals(srcSystem)) {
				setNodekey("bt");//self已经失效
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
	 * 重写此方法是为了不让默认点击字段后排序
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
