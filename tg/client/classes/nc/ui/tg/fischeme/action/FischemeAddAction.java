package nc.ui.tg.fischeme.action;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.xml.registry.infomodel.User;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.sm.UserVO;

public class FischemeAddAction extends nc.ui.pubapp.uif2app.actions.AddAction {
	private nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm;
  public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getBillForm() {
		return billForm;
	}
	public void setBillForm(nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm) {
		this.billForm = billForm;
	}
@Override
public void doAction(ActionEvent e) throws Exception {
	// TODO 自动生成的方法存根
	super.doAction(e);
	String userpk=model.getContext().getPk_loginUser();
	IUAPQueryBS query=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	if(userpk!=null){
	String sql="select   pk_psndoc  from sm_user where cuserid ='"+userpk+"'";
	String   pk_psndoc =(String)query.executeQuery(sql, new ColumnProcessor());
	if(pk_psndoc!=null){
		String up_psnpk_sql="select def1 from bd_psndoc where pk_psndoc ='"+pk_psndoc+"'";
	String   up_psnpk =(String)query.executeQuery(up_psnpk_sql, new ColumnProcessor());
	billForm.getBillCardPanel().getHeadItem("percharge").setValue(up_psnpk);
	}
	billForm.getBillCardPanel().getHeadItem("followper").setValue(pk_psndoc);
	
	}
}
}
