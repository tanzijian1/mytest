package nc.ui.tg.fischeme.ace.handler;

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
		// 设置主组织默认值
		panel.setHeadItem("pk_group", pk_group);
		panel.setHeadItem("pk_org", pk_org);
		// 设置单据状态、单据业务日期默认值
		panel.setHeadItem("approvestatus", BillStatusEnum.FREE.value());
		panel.setHeadItem("dbilldate", AppContext.getInstance().getBusiDate());
		String userpk=AppContext.getInstance().getPkUser();
		IUAPQueryBS query=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		if(userpk!=null){
		String sql="select   pk_psndoc  from sm_user where cuserid ='"+userpk+"'";
		String pk_psndoc;
		try {
			pk_psndoc = (String)query.executeQuery(sql, new ColumnProcessor());
		
		if(pk_psndoc!=null){
			String up_psnpk_sql="select def1 from bd_psndoc where pk_psndoc ='"+pk_psndoc+"'";
		String   up_psnpk =(String)query.executeQuery(up_psnpk_sql, new ColumnProcessor());
		panel.getHeadItem("percharge").setValue(up_psnpk);
		}
		panel.getHeadItem("followper").setValue(pk_psndoc);
//		panel.getHeadItem("").setEnabled(false);
		} catch (BusinessException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		}

	}
}
