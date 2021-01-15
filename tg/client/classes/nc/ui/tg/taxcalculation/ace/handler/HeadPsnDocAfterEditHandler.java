package nc.ui.tg.taxcalculation.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.pub.BusinessException;

public class HeadPsnDocAfterEditHandler implements IAppEventHandler<CardHeadTailAfterEditEvent>{
	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		// TODO 自动生成的方法存根
		if(e.getKey().equals("pk_psndoc")){
			BillCardPanel panel = e.getBillCardPanel();
			String pk_org = (String) panel.getHeadItem("pk_org").getValueObject();
			String pk_psndoc = (String) panel.getHeadItem("pk_psndoc").getValueObject();
			IUAPQueryBS bs= NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String code = null;
			String pk_dept = null;
			String pk_deptid_v = null;
			String deptName = null;
			try {
				//查询人员编码
				code = (String)bs.executeQuery("select code from bd_psndoc where pk_psndoc = '"+pk_psndoc+"' and nvl(dr,0)=0", new ColumnProcessor());
				//查询部门信息
				String sql = "select distinct bd_psnjob.pk_dept,bd_psndoc.code,bd_psndoc.name,bd_psndoc.pk_psndoc,bd_psnjob.pk_psnjob,bd_psndoc.idtype,bd_psndoc.id from bd_psndoc left outer join bd_psnjob on bd_psndoc.pk_psndoc = bd_psnjob.pk_psndoc where 11 = 11 and (enablestate = 2) and ((bd_psnjob.pk_org in (select pk_adminorg from org_admin_dept where pk_busirole in (select pk_busichild from org_busi_func where pk_org = '"+ pk_org +"') and nvl(pk_dept, '~') = '~') or bd_psnjob.pk_dept in (select pk_dept from org_admin_dept where pk_busirole in (select pk_busichild from org_busi_func where pk_org = '"+pk_org+"')) or bd_psnjob.pk_org = '"+pk_org+"') and (bd_psnjob.enddutydate = '~' or bd_psnjob.enddutydate is null)) and (lower(bd_psndoc.code) = '"+code+"' or lower(bd_psndoc.name) = '"+code+"') and bd_psnjob.pk_dept in (select pk_org from (select code,name,name2,name3,name4,name5,name6,pk_org, case when orgtype3 = 'Y' and nvl(pk_fatherorg, '~') = '~' then pk_ownorg else pk_fatherorg end pk_fatherorg,isbusinessunit,enablestate,pk_ownorg,orgtype3 from org_orgs where (orgtype3 = 'Y' or isbusinessunit = 'Y')) temp_dept where (enablestate = 2 and (pk_org in (select pk_adminorg from org_admin_dept where (enablestate = 2) and pk_busirole in (select pk_busichild from org_busi_func where pk_org = '"+pk_org+"')) or (pk_ownorg in (select pk_adminorg from org_admin_dept where (enablestate = 2) and pk_busirole in (select pk_busichild from org_busi_func where pk_org = '"+pk_org+"') and nvl(pk_dept, '~') = '~') and orgtype3 = 'Y') or pk_org in (select pk_dept from org_admin_dept where (enablestate = 2) and pk_busirole in (select pk_busichild from org_busi_func where pk_org = '"+pk_org+"')) or pk_org = '"+pk_org+"' or pk_ownorg = '"+pk_org+"'))) and rownum = 1 order by bd_psndoc.code";
				pk_dept = (String)bs.executeQuery(sql,new ColumnProcessor());
				pk_deptid_v = (String)bs.executeQuery("select pk_vid from org_dept where pk_dept = '"+pk_dept+"'", new ColumnProcessor());
				panel.setHeadItem("pk_deptid", pk_deptid_v);
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			
		}
				
	}
}
