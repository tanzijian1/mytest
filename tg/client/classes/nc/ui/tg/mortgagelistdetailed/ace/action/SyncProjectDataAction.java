package nc.ui.tg.mortgagelistdetailed.ace.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.im.exception.BusinessException;
import nc.itf.tg.IMortgagelistdetailed;
import nc.ui.uif2.NCAction;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

public class SyncProjectDataAction extends NCAction {
	private nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm;
	private nc.ui.pubapp.uif2app.model.BillManageModel model;

	public SyncProjectDataAction() {
		super();
		setCode("SyncProjectData");
		setBtnName("项目资料同步");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		IMortgagelistdetailed service = NCLocator.getInstance().lookup(
				IMortgagelistdetailed.class);
		AggMortgageListDetailedVO[] vos = service.syncProjectData();
		if (vos == null) {
			ExceptionUtils.wrappBusinessException("抵押物清单已同步最新项目资料信息!");
		}
		getModel().initModel(vos);

	}

	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(
			nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm) {
		this.billForm = billForm;
	}

	public nc.ui.pubapp.uif2app.model.BillManageModel getModel() {
		return model;
	}

	public void setModel(nc.ui.pubapp.uif2app.model.BillManageModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}
}
