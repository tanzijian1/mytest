package nc.ui.tg.internalinterest.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.bs.pmpub.rule.SetBodyOrgRule;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.internalinterest.Internalinterest;

public class AceBodyAfterEditerEvent implements IAppEventHandler<CardBodyAfterEditEvent>{

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		// TODO 自动生成的方法存根
		BillCardPanel panel = e.getBillCardPanel();
		headDef11_sum(panel);//表头 表体金额合计(def11)
		bodyDef5_sum(panel);//表体 金额合计(def5)=利息金额(def2)+待摊分闲置资金利息金额(def3)+待摊分承销费发行费金额(def4)
		bodyDef8_sum(panel);//表体 差额(def8)=应付利息(def7)-金额合计(def5)
		int row = e.getRow();
	}
	
	public void headDef11_sum(BillCardPanel panel){
		int rowCount = panel.getRowCount();
		Double sum = new Double(0);
		for (int row = 0; row < rowCount; row++) {
			Object tax = panel.getBodyValueAt(row, "def7");
			if (tax != null) {

				if (!"".equals(tax.toString())) {
					Double tax_d = new Double(tax.toString());
					sum = sum + tax_d;
				}
			}
		}
		panel.setHeadItem("def11", sum.toString());
	}
	
	public void bodyDef5_sum(BillCardPanel panel){
		int rowCount = panel.getRowCount();
		for (int row = 0; row < rowCount; row++) {
			Double sum = new Double(0);
			Object def2 = panel.getBodyValueAt(row, "def2");
			Object def3 = panel.getBodyValueAt(row, "def3");
			Object def4 = panel.getBodyValueAt(row, "def4");
			if(def2==null||"".equals(def2)){
				def2 = new Double(0);
			}else{
				def2 = new Double(def2.toString());
			}
			
			if(def3==null||"".equals(def3)){
				def3 = new Double(0);
			}else{
				def3 = new Double(def3.toString());
			}
			
			if(def4==null||"".equals(def4)){
				def4 = new Double(0);
			}else{
				def4 = new Double(def4.toString());
			}
			sum = sum + (double) def2 + (double) def3 + (double) def4;
			panel.setBodyValueAt(sum, row, "def5");
		}
	}
	
	public void bodyDef8_sum(BillCardPanel panel){
		int rowCount = panel.getRowCount();
		for (int row = 0; row < rowCount; row++) {
			Double sum = new Double(0);
			Object def5 = panel.getBodyValueAt(row, "def5");
			Object def7 = panel.getBodyValueAt(row, "def7");
			if(def5==null||"".equals(def5)){
				def5 = new Double(0);
			}else{
				def5 = new Double(def5.toString());
			}
			
			if(def7==null||"".equals(def7)){
				def7 = new Double(0);
			}else{
				def7 = new Double(def7.toString());
			}
			sum = sum + (double) def7 - (double) def5;
			panel.setBodyValueAt(sum, row, "def8");
		}
	}
	
}
