package nc.ui.tg.projectdata.ace.panel;

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
import nc.vo.tg.projectdata.AggProjectDataVO;
import nc.vo.tg.projectdata.ProjectDataBVO;
import nc.vo.tg.projectdata.ProjectDataCVO;
import nc.vo.tg.projectdata.ProjectDataVO;

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
			filter.add(ProjectDataVO.PK_GROUP);
			filter.add(ProjectDataVO.PK_ORG);
			filter.add(ProjectDataVO.PK_ORG_V);
			filter.add(ProjectDataVO.BILLDATE);
			filter.add(ProjectDataVO.CREATIONTIME);
			filter.add(ProjectDataVO.CREATOR);
			filter.add(ProjectDataVO.MODIFIER);
			filter.add(ProjectDataVO.MODIFIEDTIME);
			filter.add(ProjectDataVO.BILLDATE);
			filter.add(ProjectDataVO.PK_PROJECTDATA);
			filter.add("ts");
			filter.add(ProjectDataVO.SRCID);
		}
		return filter;
	}

	@Override
	protected AggregatedValueObject getTransData(
			CircularlyAccessibleValueObject headVO,
			Map<String, CircularlyAccessibleValueObject[]> bodyVOMap)
			throws BusinessException {
		AggProjectDataVO aggVO = new AggProjectDataVO();
		ProjectDataVO hvo = new ProjectDataVO();
		hvo.setAttributeValue(ProjectDataVO.PK_GROUP, AppContext.getInstance()
				.getPkGroup());
		hvo.setAttributeValue(ProjectDataVO.PK_ORG, AppContext.getInstance()
				.getPkGroup());
		hvo.setAttributeValue(ProjectDataVO.BILLDATE, AppContext.getInstance()
				.getBusiDate());
		hvo.setAttributeValue(ProjectDataVO.CREATIONTIME, AppContext
				.getInstance().getBusiDate());
		hvo.setAttributeValue(ProjectDataVO.CREATOR, AppContext.getInstance()
				.getPkUser());
		hvo.setAttributeValue(ProjectDataVO.SRCSYSTEM, "Excel");
		hvo.setAttributeValue("dr", new Integer(0));
		hvo.setStatus(VOStatus.NEW);

		BillCardPanel cardPanel = getUiEditor().getBillCardPanel();

		BillItem[] headItems = cardPanel.getHeadItems();
		for (BillItem item : headItems) {
			if (getFilterFiled().contains(item.getKey())) {
				continue;
			}
			ExcelUtils.getUtils().setValue(
					headVO.getAttributeValue(item.getKey()), hvo,
					hvo.getPk_org(), item);

		}

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
