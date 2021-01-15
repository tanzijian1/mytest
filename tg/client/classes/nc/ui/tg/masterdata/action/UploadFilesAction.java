package nc.ui.tg.masterdata.action;

import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.model.AbstractUIAppModel;

public class UploadFilesAction  extends nc.ui.tm.base.actions.TMBaseFileDocManageAction{

	/**
	 * �ϴ�������ť
	 */
	private static final long serialVersionUID = 8445470754778399257L;
	
	public UploadFilesAction(){
		setBtnName("�ϴ�����");
		setCode("uploadFilesAction");
	}
	
	private AbstractUIAppModel model;
	private ShowUpableBillForm editor;
	public AbstractUIAppModel getModel() {
		return model;
	}
	public void setModel(AbstractUIAppModel model) {
		this.model = model;
		this.model.addAppEventListener(this);
	}
	public ShowUpableBillForm getEditor() {
		return editor;
	}
	public void setEditor(ShowUpableBillForm editor) {
		this.editor = editor;
	}
	
}
