package nc.ui.tg.addticket.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.billform.AddEvent;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;

public class AceAddHandler implements IAppEventHandler<AddEvent> {

	@Override
	public void handleAppEvent(AddEvent e) {
		IUAPQueryBS bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String pk_group = e.getContext().getPk_group();
		String pk_org = e.getContext().getPk_org();
		BillCardPanel panel = e.getBillForm().getBillCardPanel();
		// ��������֯Ĭ��ֵ
		panel.setHeadItem("pk_group", pk_group);
		panel.setHeadItem("pk_org", pk_org);
		panel.setHeadItem("pkorg", pk_org);
		// ���õ���״̬������ҵ������Ĭ��ֵ
		panel.setHeadItem("approvestatus", BillStatusEnum.FREE.value());
		panel.setHeadItem("billdate", AppContext.getInstance().getBusiDate());
		panel.setHeadItem("maketime", AppContext.getInstance().getServerTime());
		String sql="select d.pk_psndoc ,d.pk_dept,d.pk_org from sm_user c,bd_psnjob d where c.pk_psndoc = d.pk_psndoc and c.cuserid='"+ AppContext.getInstance().getPkUser()+"' and  d.ismainjob='Y' and d.dr=0";
		try {
			Object[] as=(Object[]) bs.executeQuery(sql, new ArrayProcessor());
			if(as!=null&&as.length>0){
				panel.setHeadItem("def24",as[0]);//������
				panel.setHeadItem("def25",as[1]);//���벿��
				panel.setHeadItem("def26",as[2]);//���빫˾
			}

		} catch (BusinessException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
			MessageDialog.showErrorDlg(null, "����", e1.getMessage());
		}
	}
}
