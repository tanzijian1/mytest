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
			// TODO 自动生成的 catch 块
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
