package nc.ui.tg.taxcalculation.ace.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.tg.pub.excel.ExcelUtils;
import nc.ui.trade.excelimport.InputItem;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.AppContext;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.vo.tgfn.taxcalculation.TaxCalculationBody;
import nc.vo.tgfn.taxcalculation.TaxCalculationHead;

public class ImportablePanel extends BillImportablePanel{
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
			filter.add("billno");
			filter.add("pk_org_v");
			filter.add("pkorg");
			filter.add("emendenum");
			filter.add("transtypepk");
			filter.add("billversionpk");
			filter.add("approvestatus");
		}
		return filter;
	}

	@Override
	protected AggregatedValueObject getTransData(
			CircularlyAccessibleValueObject headVO,
			Map<String, CircularlyAccessibleValueObject[]> bodyVOMap)
			throws BusinessException {
		AggTaxCalculationHead aggVO = new AggTaxCalculationHead();
		TaxCalculationHead hvo = new TaxCalculationHead();
		hvo.setAttributeValue("pk_group", AppContext.getInstance()
				.getPkGroup());
		hvo.setAttributeValue("pk_org", AppContext.getInstance()
				.getPkGroup());
		hvo.setAttributeValue("billdate", AppContext.getInstance()
				.getBusiDate());
		hvo.setAttributeValue("creationtime", AppContext
				.getInstance().getBusiDate());
		hvo.setAttributeValue("creator", AppContext.getInstance()
				.getPkUser());
		hvo.setAttributeValue("dr", new Integer(0));
		hvo.setApprovestatus(-1);

		hvo.setStatus(VOStatus.NEW);

		BillCardPanel cardPanel = getUiEditor().getBillCardPanel();

		BillItem[] headItems = cardPanel.getHeadItems();
		for (BillItem item : headItems) {
			if (getFilterFiled().contains(item.getKey())) {
				continue;
			}
			String str = item.getKey();
			if(item.getKey().equals("pk_deptid")){
				//经办部门
				String pk_deptid = (String) item.getValueObject();
				hvo.setPk_deptid(pk_deptid);
			}else if(item.getKey().equals("pk_psndoc")){
				//经办人
				String pk_psndoc = (String) item.getValueObject();
				hvo.setPk_psndoc(pk_psndoc);
			}else{
				ExcelUtils.getUtils().setValue(
						headVO.getAttributeValue(item.getKey()), hvo,
						hvo.getPk_org(), item);
				
			}
			
		}
		
		//表体
		CircularlyAccessibleValueObject[] tableVos = bodyVOMap
				.get("id_taxcalculationbody");
		if (tableVos != null && tableVos.length > 0) {
			TaxCalculationBody[] bodyVOs = new TaxCalculationBody[tableVos.length];
			BillItem[] bodyItems = cardPanel.getBillData()
					.getBodyItemsForTable("id_taxcalculationbody");
			int row = 0;
			for (CircularlyAccessibleValueObject obj : tableVos) {
				bodyVOs[row] = new TaxCalculationBody();
				bodyVOs[row].setStatus(VOStatus.NEW);
				bodyVOs[row].setAttributeValue("dr", new Integer(0));
				for (BillItem item : bodyItems) {
					setValue(obj.getAttributeValue(item.getKey()),bodyVOs[row],
							(String) hvo.getAttributeValue("pk_org"),item);
				}
				row++;
			}
			aggVO.setChildren(TaxCalculationBody.class, bodyVOs);
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
			} else if (item.getKey().contains("pk_group")
					|| item.getKey().contains("pk_org")
					|| item.getKey().contains("billdate")
					|| item.getKey().contains("pk_deptid")
					|| item.getKey().contains("pk_psndoc")
					|| item.getKey().contains("busitype")
					|| item.getKey().contains("approvenote")
					|| item.getKey().contains("transtype")
					|| item.getKey().contains("billtype")
					|| item.getKey().contains("billstatus")
					|| item.getKey().contains("effectstatus")
					|| item.getKey().contains("def1")
					|| item.getKey().contains("def2")
					|| item.getKey().contains("def3")
					|| item.getKey().contains("def4")
					|| item.getKey().contains("def5")
					|| item.getKey().contains("def6")
					|| item.getKey().contains("def7")
					|| item.getKey().contains("def8")
					) {
				resultList.add(getInputItemByBillItem(item, item.isNull(),
						true, item.isShow()));
			} else {
				resultList.add(getInputItemByBillItem(item, true, true,
						item.isShow()));
			}
		}
		
		//表体
		String[] tableCodes = getBillcardPanelEditor().getBillCardPanel()
				.getBillData().getBodyTableCodes();
		for (String tableCode : tableCodes) {
			items = getBillcardPanelEditor().getBillCardPanel().getBillData()
					.getBodyItemsForTable(tableCode);
			if ("id_taxcalculationbody".equals(tableCode)) {
				for (BillItem item : items) {
					resultList.add(getInputItemByBillItem(item, item.isNull(),
							true, item.isShow()));
				}
			}

		}

	}
}
