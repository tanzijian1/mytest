package nc.ui.tg.taxcalculation.ace.handler;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.pubapp.uif2app.view.util.BillPanelUtils;
import nc.vo.pub.BusinessException;
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
		String pk_org=e.getNewPkOrg();
		 IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			Map<String, String> map=null;
			try {
				map = (Map<String,String>)bs.executeQuery("select def8,def9 from org_orgs where pk_org='"+pk_org+"'", new MapProcessor());
			} catch (BusinessException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
            if(map!=null){ 
			getBillForm().getBillCardPanel().setHeadItem("def21", map.get("def8"));//主管税务机关
			getBillForm().getBillCardPanel().setHeadItem("def22",map.get("def9"));//税务机关所在行政区/镇
            }
	}

	public BillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(BillForm billForm) {
		this.billForm = billForm;
	}

}
