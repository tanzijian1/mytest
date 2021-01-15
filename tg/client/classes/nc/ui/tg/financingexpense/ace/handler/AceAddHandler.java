package nc.ui.tg.financingexpense.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.billform.AddEvent;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;

public class AceAddHandler implements IAppEventHandler<AddEvent> {

	@Override
	public void handleAppEvent(AddEvent e) {
		String pk_group = e.getContext().getPk_group();
		String pk_org = e.getContext().getPk_org();
		BillCardPanel panel = e.getBillForm().getBillCardPanel();
		// 设置主组织默认值
		panel.setHeadItem("pk_group", pk_group);
		panel.setHeadItem("pk_org", pk_org);
		// 设置单据状态、单据业务日期默认值
		panel.setHeadItem("approvestatus", BillStatusEnum.FREE.value());
		panel.setHeadItem("dbilldate", AppContext.getInstance().getBusiDate());
		panel.setHeadItem("billtype","RZ06");
		panel.setHeadItem("applicationdate", AppContext.getInstance().getBusiDate());
		
		String sql="select d.pk_psndoc ,d.pk_dept,d.pk_org from sm_user c,bd_psnjob d where c.pk_psndoc = d.pk_psndoc and c.cuserid='"+ AppContext.getInstance().getPkUser()+"' and  d.ismainjob='Y' and d.dr=0";
		IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		try {
			Object[] as=(Object[]) bs.executeQuery(sql, new ArrayProcessor());
			if(as!=null&&as.length>0){
				panel.setHeadItem("pk_applicant",as[0]);
				panel.setHeadItem("pk_applicationdept",as[1]);
				panel.setHeadItem("pk_applicationorg",as[2]);
			}

		} catch (BusinessException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
			MessageDialog.showErrorDlg(null, "错误", e1.getMessage());
		}
	}
}
