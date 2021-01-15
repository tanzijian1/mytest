package nc.ui.tg.financingplan.ace.interceptor;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.ui.uif2.actions.ActionInterceptor;

public class AddOrgPanelInterceptor implements ActionInterceptor {
	nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm;
	/**
	 * @param:����1����
	 * @return�����ؽ������
	 * @throws���쳣����
	 */
	@Override
	public boolean beforeDoAction(Action action, ActionEvent e) {
		// TODO �Զ����ɵķ������
		return true;
	}

	/**
	 * @param:����1����
	 * @return�����ؽ������
	 * @throws���쳣����
	 */
	@Override
	public void afterDoActionSuccessed(Action action, ActionEvent e) {
		
		getBillForm().getBillOrgPanel().setPkOrg("00011210000000000ZHZ");
		
	}

	/**
	 * @param:����1����
	 * @return�����ؽ������
	 * @throws���쳣����
	 */
	@Override
	public boolean afterDoActionFailed(Action action, ActionEvent e,
			Throwable ex) {
		// TODO �Զ����ɵķ������
		return true;
	}

	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(
			nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm) {
		this.billForm = billForm;
	}

	
	
}
