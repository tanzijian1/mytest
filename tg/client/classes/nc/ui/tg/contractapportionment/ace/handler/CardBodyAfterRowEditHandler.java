/**
 * <p>Title: CardBodyAfterRowEditEvent.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月19日 下午11:29:45

 * @version 1.0
 */

package nc.ui.tg.contractapportionment.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent;
import nc.vo.pub.lang.UFDouble;

/**
 * 创建时间：2019年9月19日 下午11:29:45  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：CardBodyAfterRowEditEvent.java  
 * 类说明：  
 */

/**
 * <p>
 * Title: CardBodyAfterRowEditEvent<／p>
 * 
 * <p>
 * Description: <／p>
 * 
 * <p>
 * Company: hanzhi<／p>
 * 
 * @author bobo
 * 
 * @date 2019年9月19日 下午11:29:45
 */

public class CardBodyAfterRowEditHandler implements
		IAppEventHandler<CardBodyAfterRowEditEvent> {
	//表体行改变后，金额合计重新计算
	@Override
	public void handleAppEvent(CardBodyAfterRowEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		int row = panel.getRowCount();//获取表体行数
		UFDouble total = new UFDouble(0);
		UFDouble def3 = new UFDouble(0);
		UFDouble def4 = new UFDouble(0);
		UFDouble def5 = new UFDouble(0);
		for(int i=0 ; i<row ; i++){
			UFDouble def1 = new UFDouble(0);//含税金额
			def3 = new UFDouble((String)panel.getBodyValueAt(i, "def3"));//一期分摊金额
			def4 = new UFDouble((String)panel.getBodyValueAt(i, "def4"));//二期分摊金额
			def5 = new UFDouble((String)panel.getBodyValueAt(i, "def5"));//三期分摊金额
			if("".equals(def1)||def1==null){
				def1 = new UFDouble(0);
				total = total.add(def1);
			}else{
				def1 = def1.add(def3).add(def4).add(def5);
				total = total.add(def1);
				panel.setBodyValueAt(def1, i, "def1");
			}
		}
	
		panel.setHeadItem("def11", new UFDouble(total));
	}

}
