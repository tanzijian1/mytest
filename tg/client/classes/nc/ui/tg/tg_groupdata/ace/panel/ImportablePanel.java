package nc.ui.tg.tg_groupdata.ace.panel;

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
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.vo.tg.tg_groupdata.GroupDataVO;

public class ImportablePanel extends BillImportablePanel {
	List<String> filter = new ArrayList<String>();

	public ImportablePanel(String title, AbstractUIAppModel appModel,
			String configPath) {
		super(title, appModel, configPath);
	}

	@Override
	protected String getSaveActionBeanName() {

		return "saveScriptAction";
	}

	public List<String> getFilterFiled() {
		if (filter == null || filter.size() == 0) {
			filter = new ArrayList<String>();
			filter.add(GroupDataVO.PK_GROUP);
			filter.add(GroupDataVO.PK_ORG);
			filter.add(GroupDataVO.BILLDATE);
			filter.add(GroupDataVO.CREATIONTIME);
			filter.add(GroupDataVO.CREATOR);
			filter.add(GroupDataVO.MODIFIER);
			filter.add(GroupDataVO.MODIFIEDTIME);
			filter.add(GroupDataVO.PK_GROUPDATA);
			filter.add(GroupDataVO.BILLNO);
			filter.add("ts");
			//filter.add(GroupDataVO.SRCID);
		}
		return filter;
	}

	@Override
	protected AggregatedValueObject getTransData(
			CircularlyAccessibleValueObject headVO,
			Map<String, CircularlyAccessibleValueObject[]> bodyVOMap)
			throws BusinessException {
		AggGroupDataVO aggVO = new AggGroupDataVO();
		GroupDataVO hvo = new GroupDataVO();
		hvo.setAttributeValue(GroupDataVO.PK_GROUP, AppContext.getInstance()
				.getPkGroup());
		hvo.setAttributeValue(GroupDataVO.PK_ORG, AppContext.getInstance()
				.getPkGroup());
		hvo.setAttributeValue(GroupDataVO.BILLDATE, AppContext.getInstance()
				.getBusiDate());
		hvo.setAttributeValue(GroupDataVO.CREATIONTIME, AppContext
				.getInstance().getBusiDate());
		hvo.setAttributeValue(GroupDataVO.CREATOR, AppContext.getInstance()
				.getPkUser());
	//	hvo.setAttributeValue(GroupDataVO.SRCSYSTEM, "Excel");
		hvo.setAttributeValue("dr", new Integer(0));
		hvo.setAttributeValue("approvestatus", new Integer(-1));
		hvo.setStatus(VOStatus.NEW);

		BillCardPanel cardPanel = getUiEditor().getBillCardPanel();

		BillItem[] headItems = cardPanel.getHeadItems();
		for (BillItem item : headItems) {
			if (getFilterFiled().contains(item.getKey())) {
				continue;
			}
			if("projectstaging".equals(item.getKey())){
				System.out.println();
			}
			ExcelUtils.getUtils().setValue(
					headVO.getAttributeValue(item.getKey()), hvo,
					hvo.getPk_org(), item);

		}
/*
		// 地价信息
		CircularlyAccessibleValueObject[] tableVos = bodyVOMap
				.get("pk_projectdata_b");
		if (tableVos != null && tableVos.length > 0) {
			ProjectDataBVO[] bodyVOs = new ProjectDataBVO[tableVos.length];
			BillItem[] bodyItems = cardPanel.getBillData()
					.getBodyItemsForTable("pk_projectdata_b");
			int row = 0;
			for (CircularlyAccessibleValueObject obj : tableVos) {
				bodyVOs[row] = new ProjectDataBVO();
				bodyVOs[row].setStatus(VOStatus.NEW);
				bodyVOs[row].setAttributeValue("dr", new Integer(0));
				for (BillItem item : bodyItems) {
					ExcelUtils.getUtils().setValue(
							obj.getAttributeValue(item.getKey()), bodyVOs[row],
							hvo.getPk_org(), item);
				}
				row++;
			}
			aggVO.setChildren(ProjectDataBVO.class, bodyVOs);
		}
		// 分期信息
		tableVos = bodyVOMap.get("pk_projectdata_c");
		if (tableVos != null && tableVos.length > 0) {
			ProjectDataCVO[] bodyVOs = new ProjectDataCVO[tableVos.length];
			BillItem[] bodyItems = cardPanel.getBillData()
					.getBodyItemsForTable("pk_projectdata_c");
			int row = 0;
			for (CircularlyAccessibleValueObject obj : tableVos) {
				bodyVOs[row] = new ProjectDataCVO();
				bodyVOs[row].setStatus(VOStatus.NEW);
				bodyVOs[row].setAttributeValue("dr", new Integer(0));
				for (BillItem item : bodyItems) {
					ExcelUtils.getUtils().setValue(
							obj.getAttributeValue(item.getKey()), bodyVOs[row],
							hvo.getPk_org(), item);
				}
				row++;
			}
			aggVO.setChildren(ProjectDataCVO.class, bodyVOs);
		}
*/
		aggVO.setParentVO(hvo);
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
			} else if (item.getKey().contains("def")
					|| item.getKey().contains("code")) {
				resultList.add(getInputItemByBillItem(item, item.isNull(),
						true, item.isShow()));
			} else {
				resultList.add(getInputItemByBillItem(item, true, true,
						item.isShow()));
			}
		}
		String[] tableCodes = getBillcardPanelEditor().getBillCardPanel()
				.getBillData().getBodyTableCodes();
		if(tableCodes!=null){
		for (String tableCode : tableCodes) {
			items = getBillcardPanelEditor().getBillCardPanel().getBillData()
					.getBodyItemsForTable(tableCode);
			if ("pk_projectdata_c".equals(tableCode)
					|| "pk_projectdata_b".equals(tableCode)) {
				for (BillItem item : items) {
					resultList.add(getInputItemByBillItem(item, item.isNull(),
							true, item.isShow()));
				}
			}

		}
		}

	}
}
