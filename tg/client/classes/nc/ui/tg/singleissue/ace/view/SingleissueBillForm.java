package nc.ui.tg.singleissue.ace.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import nc.ui.pub.bill.IBillItem;
/**
 * ����ƻ���ծȯ����ҳǩ��Ӱ�ť
 * @author wenjie
 *
 */
public class SingleissueBillForm extends nc.ui.pubapp.uif2app.view.ShowUpableBillForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3054551105040648686L;

	@Override
	protected void setBodyTabActive(String tabcode) {
		List<Action> list = getBodyActionMap().get(tabcode);
		List<Action> actionList = new ArrayList<Action>();
		if(null!=list && list.size()>0 && !"pk_constateexe".equals(tabcode)){
			for (Action action : list) {
				if(action instanceof nc.ui.tg.singleissue.action.CreateConstractPayBillAction){
					if("pk_repayplan".equals(tabcode) || "pk_bondresale".equals(tabcode)){
						actionList.add(action);
					}
				}else{
					actionList.add(action);
				}
			}
		}
		this.billCardPanel.addTabAction(IBillItem.BODY, actionList);
	}

	@Override
	public void initUI() {
		// TODO �Զ����ɵķ������
		super.initUI();
		//��ֹ����
        this.billCardPanel.getBillTable("pk_repayplan").setSortEnabled(false);
        this.billCardPanel.getBillTable("pk_bondtranssale").setSortEnabled(false);
	}
	
	
}
