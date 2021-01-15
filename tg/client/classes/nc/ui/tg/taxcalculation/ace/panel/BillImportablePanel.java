package nc.ui.tg.taxcalculation.ace.panel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.itf.trade.excelimport.ExportDataInfo;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.trade.excelimport.InputItem;
import nc.ui.trade.excelimport.InputItemCreator;
import nc.ui.trade.excelimport.Uif2ImportablePanel;
import nc.ui.trade.excelimport.inputitem.InputItemImpl;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.excelimport.ImExPortHelper;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ExtendedAggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public abstract class BillImportablePanel extends Uif2ImportablePanel {
	protected final String AddActionName = "addAction";
	protected final String SaveActionName = "saveScriptAction";
	protected final String CancelActionName = "cancelAction";
	protected final String AppModelName = "bmModel";
	protected final String BillFormName = "billForm";
	/**
	 * UI����billForm
	 */
	ShowUpableBillForm uiEditor = null;

	/**
	 * ͨ�����췽��������ע��
	 * 
	 * @param title
	 * @param appModel
	 * @param configPath
	 */
	public BillImportablePanel(String title, AbstractUIAppModel appModel,
			String configPath) {
		super(title, appModel.getContext().getFuncInfo().getFuncode(),
				configPath);
	}

	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getUiEditor() {
		return uiEditor;
	}

	public void setUiEditor(
			nc.ui.pubapp.uif2app.view.ShowUpableBillForm uiEditor) {
		this.uiEditor = uiEditor;
	}

	@Override
	protected String getAddActionBeanName() {

		return AddActionName;
	}

	@Override
	protected String getSaveActionBeanName() {

		return SaveActionName;
	}

	@Override
	protected String getCancelActionBeanName() {

		return CancelActionName;
	}

	@Override
	protected String getAppModelBeanName() {

		return AppModelName;
	}

	@Override
	protected String getBillCardEditorBeanName() {

		return BillFormName;
	}

	public void setValue(Object obj) {
		ExtendedAggregatedValueObject importdata = (ExtendedAggregatedValueObject) obj;
		CircularlyAccessibleValueObject headVO = importdata.getParentVO();
		Map<String, CircularlyAccessibleValueObject[]> bodyVOMap = null;
		String[] bodyTableCodes = getUiEditor().getBillCardPanel()
				.getBillData().getBodyTableCodes();
		if (bodyTableCodes != null && bodyTableCodes.length > 0) {
			bodyVOMap = new HashMap<String, CircularlyAccessibleValueObject[]>();
			for (String tablecode : bodyTableCodes) {
				CircularlyAccessibleValueObject[] tableVos = importdata
						.getTableVO(tablecode);
				bodyVOMap.put(tablecode, tableVos);
			}
		}
		try {
			AggregatedValueObject bill = getTransData(headVO, bodyVOMap);
			createBillCardPanelEditor().setValue(bill);
			((BillForm) (createBillCardPanelEditor())).getBillCardPanel()
					.setBillValueVO(bill);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}

	}

	/**
	 * ��ȡExcel������Ϣ��ת����NC��ӦVO
	 * 
	 * @param headVO
	 * @param tableVos
	 * @return
	 * @throws BusinessException
	 */
	protected abstract AggregatedValueObject getTransData(
			CircularlyAccessibleValueObject headVO,
			Map<String, CircularlyAccessibleValueObject[]> bodyVOMap)
			throws BusinessException;

	/**
	 * Ĭ�ϵ�������ʵ��
	 * 
	 */
	@Override
	public ExportDataInfo getValue(List<InputItem> exportItems) {
		Object[] vos = getSelectedObject();
		BillData billData = getBillcardPanelEditor().getBillCardPanel()
				.getBillData();
		ExtendedAggregatedValueObject[] aggvos = getDataConvertor()
				.convertDataFromEditorData(billData, vos, exportItems);
		return new ExportDataInfo(beforeExport(aggvos));
	}

	protected Object[] getSelectedObject() {
		if (getAppModel() instanceof BillManageModel) {
			BillManageModel mm = (BillManageModel) getAppModel();
			if (mm.getSelectedOperaDatas() != null)
				// return mm.getSelectedOperaDatas();
				return ImExPortHelper.getNoneLazyLoadingData(mm,
						getBillcardPanelEditor());
			else
				return new Object[] { mm.getSelectedData() };
		} else if (getAppModel() instanceof BatchBillTableModel) {
			BatchBillTableModel bm = (BatchBillTableModel) getAppModel();
			if (bm.getSelectedOperaDatas() != null)
				return bm.getSelectedOperaDatas();
			else
				return new Object[] { bm.getSelectedData() };
		} else
			return new Object[] { getAppModel().getSelectedData() };
	}

	/**
	 * Ĭ��ʵ�ֵ������б�ͷ/����/��β�����е��������������󣬸��ǡ�
	 * 
	 */
	@Override
	public List<InputItem> getInputItems() {
		List<InputItem> resultList = null;

		if (getBillcardPanelEditor() != null) {
			resultList = InputItemCreator.getInputItems(
					getBillcardPanelEditor().getBillCardPanel().getBillData(),
					false);
			onAdjBillItem(resultList);

			return resultList;
		}
		return null;
	}

	/**
	 * Excel������ʾ��Ϣ�Ƿ���ʾ/�޸�/��¼��������
	 * 
	 * @param resultList
	 */
	protected abstract void onAdjBillItem(List<InputItem> resultList);

	protected InputItemImpl getInputItemByBillItem(BillItem item,
			boolean notNull, boolean edit, boolean show) {
		InputItemImpl input = new InputItemImpl();
		input.setItemKey(item.getKey());
		input.setShowName(item.getName());
		input.setTabCode(item.getTableCode());
		input.setTabName(item.getTableName());
		input.setPos(item.getPos());
		input.setShow(show);
		input.setEdit(edit);
		input.setOrder(item.getReadOrder());
		input.setNotNull(notNull);

		return input;
	}

	/**
	 * ���ݸ�ֵ
	 * 
	 * @param value
	 * @param vo
	 * @param message
	 * @param deployvo
	 * @param key
	 * @param pk_org
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public void setValue(Object value, CircularlyAccessibleValueObject vo,
			String pk_org, BillItem item) throws BusinessException {
		try {
			Object result = null;
			if (!item.isNull() && (value == null || "".equals(value))) {
				return;
			}
			if (item.isNull() && (value == null || "".equals(value))) {
				throw new BusinessException("[" + item.getName()
						+ ":��¼�ֶβ���Ϊ�� !");
			}
			value = value.toString().trim();
			if (IBillItem.STRING == item.getDataType()) {
				result = value == null || "".equals(value) ? null : String
						.valueOf(value);
			} else if (IBillItem.DECIMAL == item.getDataType()) {
				try {
					value = String.valueOf(value).trim();
					result = value == null ? null : new UFDouble(
							value.toString());
				} catch (Exception e) {
					throw new BusinessException("��" + item.getKey() + ":"
							+ String.valueOf(value) + "������ֵ��ʽ;");
				}
			} else if (IBillItem.UFREF == item.getDataType()
					|| IBillItem.USERDEF == item.getDataType()) {
				UIRefPane pane = (UIRefPane) item.getComponent();
				UIRefPane refpane = new UIRefPane(pane.getRefNodeName());
				onHandleSpecialRefModel(vo, pane);
				String whererpart = getWherePart(refpane, value);
				if (pk_org != null) {
					refpane.setPk_org(pk_org);
				}
				refpane.getRefModel().addWherePart(whererpart);
				Vector vector = refpane.getRefModel().getQueryResultVO();
				if (vector != null && vector.size() > 0) {
					refpane.getRefModel().setSelectedDataAndConvertData(vector);
					result = refpane.getRefModel().getPkValue();
				} else {
					throw new BusinessException("��" + item.getName() + ":"
							+ String.valueOf(value) + "������"
							+ pane.getRefNodeName() + "δ�ܲ�ѯ����Ϣ;");

				}

			} else if (IBillItem.USERDEFITEM == item.getDataType()) {
				result = value == null || "".equals(value) ? null : String
						.valueOf(value);
			} else if (IBillItem.DATE == item.getDataType()
					|| IBillItem.LITERALDATE == item.getDataType()) {
				try {
					UFDate date = value == null || "".equals(value) ? null
							: new UFDate(String.valueOf(value));
					result = date;
				} catch (Exception e) {
					throw new BusinessException("��" + item.getName() + ":"
							+ String.valueOf(value) + "��������NC���ڸ�ʽ[YYYY-MM-DD];");
				}

			} else if (IBillItem.BOOLEAN == item.getDataType()) {
				result = "��".equals(value) || "Y".equals(value) ? UFBoolean.TRUE
						: UFBoolean.FALSE;

			} else if (IBillItem.COMBO == item.getDataType()) {
				UIComboBox box = (UIComboBox) item.getComponent();
				String msg = "";
				boolean isaccord = false;
				for (int i = 0; i < box.getModel().getSize(); i++) {
					if ("".equals(box.getModel().getElementAt(i))) {
						continue;
					}
					DefaultConstEnum constEnum = (DefaultConstEnum) box
							.getModel().getElementAt(i);
					msg += constEnum.getName() + ",";
					if (!isaccord) {
						if (value.equals(constEnum.getValue())
								|| value.equals(constEnum.getName())) {
							isaccord = true;
						}

					}

				}
				if (isaccord) {
					box.setSelectedItem(value);
					result = box.getSelectdItemValue();
				}
				if (!isaccord) {
					throw new BusinessException("��" + item.getName() + ":"
							+ String.valueOf(value) + "��ö�ٸ�ʽ["
							+ msg.substring(0, msg.length()) + "];");
				}

			} else {
				result = value == null || "".equals(value) ? null : String
						.valueOf(value);
			}
			if (equals(item.getKey().startsWith("def"))
					|| equals(item.getKey().startsWith("vdef"))) {
				result = result.toString();
			}
			vo.setAttributeValue(item.getKey(), result);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());

		}

	}

	/**
	 * ������մ���[��������/���������]
	 * 
	 * @param vo
	 * @param pane
	 */
	protected void onHandleSpecialRefModel(CircularlyAccessibleValueObject vo,
			UIRefPane pane) {

	}

	/**
	 * ���������ֶ���������
	 * 
	 * @param refpane
	 * @param deployvo
	 * @param whererpart
	 * @param value
	 */
	public String getWherePart(UIRefPane refpane, Object value) {
		String whererpart = null;
		if (refpane.getRefNodeName().contains("��������˲�")) {
			whererpart = " and (" + refpane.getRefModel().getRefCodeField()
					+ " like '" + String.valueOf(value) + "%' or "
					+ refpane.getRefModel().getRefNameField() + " like '"
					+ String.valueOf(value) + "%')";
		} else {
			whererpart = " and (" + refpane.getRefModel().getRefCodeField()
					+ "='" + String.valueOf(value) + "' or "
					+ refpane.getRefModel().getRefNameField() + "='"
					+ String.valueOf(value) + "')";
		}

		return whererpart;

	}

}
