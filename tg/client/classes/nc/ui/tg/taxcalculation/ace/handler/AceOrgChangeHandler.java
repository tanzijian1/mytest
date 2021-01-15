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
 * <b> ��֯�л��¼� </b>
 *
 * @author author
 * @version tempProject version
 */
public class AceOrgChangeHandler implements IAppEventHandler<OrgChangedEvent> {

	private BillForm billForm;

	@Override
	public void handleAppEvent(OrgChangedEvent e) {
		if (this.billForm.isEditable()) {
			// �ڱ༭״̬�£�����֯�л�ʱ����ս������ݣ��Զ��������У��������к�
			this.billForm.addNew();
		}
		LoginContext context = this.billForm.getModel().getContext();
		// ���в��չ���
		BillPanelUtils.setOrgForAllRef(this.billForm.getBillCardPanel(),
				context);
		String pk_org=e.getNewPkOrg();
		 IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			Map<String, String> map=null;
			try {
				map = (Map<String,String>)bs.executeQuery("select def8,def9 from org_orgs where pk_org='"+pk_org+"'", new MapProcessor());
			} catch (BusinessException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
            if(map!=null){ 
			getBillForm().getBillCardPanel().setHeadItem("def21", map.get("def8"));//����˰�����
			getBillForm().getBillCardPanel().setHeadItem("def22",map.get("def9"));//˰���������������/��
            }
	}

	public BillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(BillForm billForm) {
		this.billForm = billForm;
	}

}
