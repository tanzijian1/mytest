package nc.ui.tg.addticket.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.pub.contract.IContractQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.pubapp.uif2app.view.util.BillPanelUtils;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.view.tb.adjbill.dialog.MessageShowDlg;
import nc.vo.pub.BusinessException;
import nc.vo.pub.contract.ContractVO;
import nc.vo.pubapp.AppContext;
import nc.vo.uif2.LoginContext;
/**
 * <b> 组织切换事件 </b>
 *
 * @author author
 * @version tempProject version
 */
public class AceOrgChangeHandler implements IAppEventHandler<OrgChangedEvent> {

	private BillForm billForm;

	@Override
	public void handleAppEvent(OrgChangedEvent e) {
		if (this.billForm.isEditable()) {
			// 在编辑状态下，主组织切换时，清空界面数据，自动表体增行，并设置行号
			this.billForm.addNew();
		}
		LoginContext context = this.billForm.getModel().getContext();
		
		// 进行参照过滤
		BillPanelUtils.setOrgForAllRef(this.billForm.getBillCardPanel(),
				context);
		this.billForm.getBillCardPanel().getHeadItem("pkorg").setValue(this.billForm.getBillCardPanel().getHeadItem("pk_org").getValueObject());
		// nc.ui.bd.ref.AbstractRefModel
		// model=((UIRefPane)getBillForm().getBillCardPanel().getHeadItem("def2").getComponent()).getRefModel();
		// if(model instanceof nc.ui.cdm.contract.ref.ContractRefModel){
		// ((nc.ui.cdm.contract.ref.ContractRefModel)model).setAddTicketPk_org(e.getNewPkOrg());
		// }

	}

	public BillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(BillForm billForm) {
		this.billForm = billForm;
	}

}
