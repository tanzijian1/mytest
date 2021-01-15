package nc.ui.tg.pub.excel;

import java.util.Vector;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class ExcelUtils {
	static ExcelUtils utils = null;

	public static ExcelUtils getUtils() {
		if (utils == null) {
			utils = new ExcelUtils();
		}
		return utils;

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
