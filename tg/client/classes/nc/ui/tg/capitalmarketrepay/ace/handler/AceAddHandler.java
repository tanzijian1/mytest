package nc.ui.tg.capitalmarketrepay.ace.handler;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
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
		// ��������֯Ĭ��ֵ
		panel.setHeadItem("pk_group", pk_group);
		panel.setHeadItem("pk_org", pk_org);
		// ���õ���״̬������ҵ������Ĭ��ֵ
		panel.setHeadItem("approvestatus", BillStatusEnum.FREE.value());
		panel.setHeadItem("billdate", AppContext.getInstance().getBusiDate());
		panel.setHeadItem("billtype", "SD08");
		//������Ĭ������Ϊ�Ƶ�
		String userpk=AppContext.getInstance().getPkUser();
		IUAPQueryBS query=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		if(userpk!=null){
			String sql="select   pk_psndoc  from sm_user where cuserid ='"+userpk+"'";
			String pk_psndoc;
			try {
				pk_psndoc = (String)query.executeQuery(sql, new ColumnProcessor());
				if(pk_psndoc!=null){
					String up_psnpk_sql="select pk_dept,pk_org from bd_psnjob where bd_psnjob.pk_psndoc ='"+pk_psndoc+"'"
							+ " and ismainjob='Y'";
					Map<String,String>  map =(Map<String,String>)query.executeQuery(up_psnpk_sql, new MapProcessor());
					if(map != null){
						panel.getHeadItem("def3").setValue(map.get("pk_org"));//���빫˾
						panel.getHeadItem("def4").setValue(map.get("pk_dept"));//���벿��
					}
				}
				panel.getHeadItem("def1").setValue(pk_psndoc);//������
			} catch (BusinessException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		}
	}
}
