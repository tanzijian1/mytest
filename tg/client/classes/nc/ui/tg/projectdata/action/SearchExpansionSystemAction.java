package nc.ui.tg.projectdata.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.outside.OutsideUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class SearchExpansionSystemAction extends NCAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3154698064746285136L;

	/**
	 * 查看投拓资料
	 */
	public SearchExpansionSystemAction() {
		super();
		setCode("searchExpansionSystem");
		setBtnName("查看投拓资料");
	}

	private BillForm editor;

	private BillManageModel model;

	private BillListView listView;

	private IUAPQueryBS queryBS;

	@Override
	public boolean isEnabled() {
		AggregatedValueObject aggvo = (AggregatedValueObject) getModel()
				.getSelectedData();
		if (aggvo == null || aggvo.getParentVO() == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected boolean isActionEnable() {
		AggregatedValueObject aggvo = (AggregatedValueObject) getModel()
				.getSelectedData();
		if (aggvo == null || aggvo.getParentVO() == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		hyperlink();
	}

	public void hyperlink() throws Exception {

		Desktop desktop = Desktop.getDesktop();
		// 正式地址：http://tz.timesgroup.cn
		// 测试地址：http://tz-uat.timesgroup.cn
		String url = OutsideUtils.getOutsideInfo("DATA05");
		URI u;
		try {
			u = new URI(url);
			desktop.browse(u);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public BillManageModel getModel() {
		model.addAppEventListener(this);
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
	}

	public BillListView getListView() {
		return listView;
	}

	public void setListView(BillListView listView) {
		this.listView = listView;
	}

	public IUAPQueryBS getQueryBS() {
		if (queryBS == null) {
			queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return queryBS;
	}

}
