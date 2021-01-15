package nc.ui.tg.tg_groupdata.ace.handler;
import java.util.Vector;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
/**
 * 单据表头表尾字段编辑后事件处理类
 * 
 * @since 6.0
 * @version 2019-7-1 下午02:52:22
 * @author Hugo
 */
@SuppressWarnings("restriction")
public class AceHeadTailAfterEditHandler implements IAppEventHandler<CardHeadTailAfterEditEvent> {
	
	private BillForm billForm;
	@Override
    public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		String key = e.getKey();
		if ("projectname".equals(key)) {
			String pk_projectdata = e.getValue().toString();
			BillCardPanel panel = e.getBillCardPanel();
			UIRefPane refPane = (UIRefPane) panel.getHeadItem("projectname")
					.getComponent();
			String refSql = "select  code,name,periodizationname,p6_datadate1,projectcorp,projectarea,pk_projectdata_c,pk_projectdata "
					+ "from v_tgrz_ref_project  where 11=11   and v_tgrz_ref_project.pk_projectdata= '"
					+ pk_projectdata + "' order by code";
			refPane.getRefModel().getSelectedData();
			Vector result = refPane.getRefModel().queryMain(null, refSql);
			if(result.size()>0){
				String project = result.get(0).toString().split(",")[6].trim();
				panel.getHeadItem("projectstaging").setValue(project);
			}else{
				panel.getHeadItem("projectstaging").setValue("");
			}
			
		}
	}
	public BillForm getBillForm() {
		return billForm;
	}
	public void setBillForm(BillForm billForm) {
		this.billForm = billForm;
	}
	
}
