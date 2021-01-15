package nc.ui.tg.singleissue.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.billform.AddEvent;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.ui.pub.bill.BillCardPanel;

public class AceAddHandler implements IAppEventHandler<AddEvent> {

	@Override
	public void handleAppEvent(AddEvent e) {
		String pk_group = e.getContext().getPk_group();
		String pk_org = e.getContext().getPk_org();
		BillCardPanel panel = e.getBillForm().getBillCardPanel();
		// ��������֯Ĭ��ֵ
		panel.setHeadItem("pk_group", pk_group);
		panel.setHeadItem("pk_org", pk_org);
		// ���õ���״̬������ҵ������Ĭ��ֵ
		panel.setHeadItem("approvestatus", BillStatusEnum.FREE.value());
		panel.setHeadItem("billdate", AppContext.getInstance().getBusiDate());
		panel.setHeadItem("billtype", "SD06");
		//������Ĭ������Ϊ�Ƶ�
		String userpk=AppContext.getInstance().getPkUser();
		IUAPQueryBS query=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		if(userpk!=null){
			try {
				String sql="select   pk_psndoc  from sm_user where cuserid ='"+userpk+"'";
				String pk_psndoc = (String)query.executeQuery(sql, new ColumnProcessor());
				String up_psnpk_sql="select def1 from sm_user where cuserid ='"+userpk+"'";
				String   up_psnpk =(String)query.executeQuery(up_psnpk_sql, new ColumnProcessor());
				panel.getHeadItem("def24").setValue(up_psnpk);//�������ϼ�
				panel.getHeadItem("def23").setValue(pk_psndoc);//������
			} catch (BusinessException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		}
		
		panel.getHeadItem("def13").setEnabled(false);//Ӧ���˵�����
		panel.getHeadItem("def14").setEnabled(false);//ר��ƻ�������
		panel.getHeadItem("def20").setEnabled(false);//ѭ��������
	}
}
