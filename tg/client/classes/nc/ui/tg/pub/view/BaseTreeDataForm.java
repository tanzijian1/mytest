package nc.ui.tg.pub.view;

import nc.pub.billcode.vo.BillCodeContext;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.tg.pub.model.BaseTreeDataAppModel;
import nc.ui.uif2.AppEvent;
import nc.vo.pub.SuperVO;
import nc.vo.uif2.LoginContext;

public class BaseTreeDataForm extends nc.ui.uif2.editor.BillForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2220632620703958100L;

	@Override
	public void initUI() {
		super.initUI();
	}

	@Override
	protected void setDefaultValue() {
		LoginContext context = getModel().getContext();
		// ����������֯�Լ���������Ĭ��ֵ
		getBillCardPanel().getHeadItem("pk_group").setValue(
				context.getPk_group());
		getBillCardPanel().getHeadItem("pk_org").setValue(context.getPk_org());
		// �����ϼ���������Ĭ��ֵ
		setFatherRefDefaultValue();
		// �Զ��������ݱ���
		setPreCode();
	}

	protected void setPreCode() {
		BaseTreeDataAppModel appModel = ((BaseTreeDataAppModel) getModel());
		BillCodeContext billCodeContext = appModel.getBillCodeContext();
		if (billCodeContext != null) {
			appModel.generateNewBillCode();
			BillItem codeItem = getBillCardPanel().getHeadItem("code");
			codeItem.setValue(appModel.getNewDateBillCode());
			codeItem.setEdit(billCodeContext.isEditable());
		}
	}

	private void setFatherRefDefaultValue() {
		Object selObj = getModel().getSelectedData();
		SuperVO selVO = (SuperVO) selObj;
		UIRefPane fatherRef = (UIRefPane) (getBillCardPanel().getHeadItem(
				"pk_father").getComponent());
		fatherRef.setPK(selVO == null ? null : selVO.getPrimaryKey());
	}

	@Override
	public void handleEvent(AppEvent event) {
		super.handleEvent(event);

	}

	@Override
	protected void onEdit() {
		super.onEdit();
		BillCodeContext billCodeContext = ((BaseTreeDataAppModel) getModel())
				.getBillCodeContext();
		if (billCodeContext != null)
			getBillCardPanel().getHeadItem("code").setEnabled(
					billCodeContext.isEditable());
	}

	@Override
	protected void onAdd() {
		super.onAdd();
	}

	public void setValue(Object object) {
		super.setValue(object);
	}

	@Override
	public Object getValue() {
		return super.getValue();
	}
}
