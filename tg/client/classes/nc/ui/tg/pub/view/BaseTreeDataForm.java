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
		// 设置所属组织以及所属集团默认值
		getBillCardPanel().getHeadItem("pk_group").setValue(
				context.getPk_group());
		getBillCardPanel().getHeadItem("pk_org").setValue(context.getPk_org());
		// 设置上级地区主键默认值
		setFatherRefDefaultValue();
		// 自动产生单据编码
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
