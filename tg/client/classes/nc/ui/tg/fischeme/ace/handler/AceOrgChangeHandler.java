package nc.ui.tg.fischeme.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.pubapp.uif2app.view.util.BillPanelUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
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
		String userpk=AppContext.getInstance().getPkUser();
		IUAPQueryBS query=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		if(userpk!=null){
		String sql="select   pk_psndoc  from sm_user where cuserid ='"+userpk+"'";
		String pk_psndoc=null;
		String   up_psnpk =null;
		try {
			pk_psndoc = (String)query.executeQuery(sql, new ColumnProcessor());
			if(pk_psndoc!=null){
				String up_psnpk_sql="select def1 from bd_psndoc where pk_psndoc ='"+pk_psndoc+"'";
				up_psnpk=(String)query.executeQuery(up_psnpk_sql, new ColumnProcessor());
				}
		} catch (BusinessException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
			
		}
		
		billForm.getBillCardPanel().getHeadItem("followper").setValue(pk_psndoc);
		billForm.getBillCardPanel().getHeadItem("percharge").setValue(up_psnpk);
		}
		BillPanelUtils.setOrgForAllRef(this.billForm.getBillCardPanel(),
				context);
	}

	public BillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(BillForm billForm) {
		this.billForm = billForm;
	}

}
