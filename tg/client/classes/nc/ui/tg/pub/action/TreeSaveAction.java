package nc.ui.tg.pub.action;

import nc.bs.uif2.BusinessExceptionAdapter;
import nc.ui.uif2.actions.SaveAction;
import nc.ui.uif2.editor.BillForm;
import nc.vo.pub.ValidationException;

public class TreeSaveAction extends SaveAction {
	@Override
	protected void validate(Object value) {
		super.validate(value);
		try {
			((BillForm) getEditor()).getBillCardPanel().dataNotNullValidate();
		} catch (ValidationException e) {
			throw new BusinessExceptionAdapter(e);
		}
	}
}
