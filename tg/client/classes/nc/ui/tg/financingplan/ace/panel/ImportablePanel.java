package nc.ui.tg.financingplan.ace.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.tg.pub.excel.BillImportablePanel;
import nc.ui.tg.pub.excel.ExcelUtils;
import nc.ui.trade.excelimport.InputItem;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.vo.tg.financingplan.FinancingPlan;

public class ImportablePanel extends BillImportablePanel {
	List<String> filter = new ArrayList<String>();

	public ImportablePanel(String title, AbstractUIAppModel appModel,
			String configPath) {
		super(title, appModel, configPath);
	}
	
	@Override
	protected String getSaveActionBeanName() {
		return "saveAction";
	}

	public List<String> getFilterFiled() {
		if (filter == null || filter.size() == 0) {
			filter = new ArrayList<String>();
			filter.add("billno");
			filter.add("approvestatus");
		}
		return filter;
	}

	@Override
	protected AggregatedValueObject getTransData(
			CircularlyAccessibleValueObject headVO,
			Map<String, CircularlyAccessibleValueObject[]> bodyVOMap)
			throws BusinessException {
		AggFinancingPlan aggVO = new AggFinancingPlan();
		FinancingPlan fcp = new FinancingPlan();
		fcp.setAttributeValue("pk_group", AppContext.getInstance()
				.getPkGroup());
		fcp.setAttributeValue("pk_org", AppContext.getInstance()
				.getPkGroup());
		fcp.setAttributeValue("dbilldate", AppContext.getInstance()
				.getBusiDate());
		fcp.setAttributeValue("creationtime", AppContext
				.getInstance().getBusiDate());
		fcp.setAttributeValue("creator", AppContext.getInstance()
				.getPkUser());
		fcp.setAttributeValue("dr", new Integer(0));
		fcp.setStatus(VOStatus.NEW);

		BillCardPanel cardPanel = getUiEditor().getBillCardPanel();

		BillItem[] headItems = cardPanel.getHeadItems();
		for (BillItem item : headItems) {
			if (getFilterFiled().contains(item.getKey())) {
				continue;
			}
			ExcelUtils.getUtils().setValue(
					headVO.getAttributeValue(item.getKey()), fcp,
					fcp.getPk_org(), item);

		}
		
		aggVO.setParentVO(fcp);
		return aggVO;
	}

	@Override
	protected void onAdjBillItem(List<InputItem> resultList) {
		resultList.clear();
		BillItem[] items = getBillcardPanelEditor().getBillCardPanel()
				.getHeadItems();
		for (BillItem item : items) {
			if (getFilterFiled().contains(item.getKey())) {
				resultList
						.add(getInputItemByBillItem(item, false, false, false));
			} else if (item.getKey().contains("pk_group")
					|| item.getKey().contains("pk_project")
					|| item.getKey().contains("project_phases")
					|| item.getKey().contains("financing_type")
					|| item.getKey().contains("nfinancing_money")
					|| item.getKey().contains("nfinancing_plan")
					|| item.getKey().contains("dfinancing_year")
					|| item.getKey().contains("dbilldate")
					|| item.getKey().contains("remark")
					) {
				resultList.add(getInputItemByBillItem(item, item.isNull(),
						true, item.isShow()));
			} else {
				resultList.add(getInputItemByBillItem(item, true, true,
						item.isShow()));
			}
		}

	}
	
}
