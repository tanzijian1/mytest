package nc.ui.tg.financingplan.ace.interceptor;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.ui.uif2.actions.ActionInterceptor;

public class AddOrgPanelInterceptor implements ActionInterceptor {
	nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm;
	/**
	 * @param:描述1描述
	 * @return：返回结果描述
	 * @throws：异常描述
	 */
	@Override
	public boolean beforeDoAction(Action action, ActionEvent e) {
		// TODO 自动生成的方法存根
		return true;
	}

	/**
	 * @param:描述1描述
	 * @return：返回结果描述
	 * @throws：异常描述
	 */
	@Override
	public void afterDoActionSuccessed(Action action, ActionEvent e) {
		
		getBillForm().getBillOrgPanel().setPkOrg("00011210000000000ZHZ");
		
	}

	/**
	 * @param:描述1描述
	 * @return：返回结果描述
	 * @throws：异常描述
	 */
	@Override
	public boolean afterDoActionFailed(Action action, ActionEvent e,
			Throwable ex) {
		// TODO 自动生成的方法存根
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
