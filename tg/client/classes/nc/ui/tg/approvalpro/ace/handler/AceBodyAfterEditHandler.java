package nc.ui.tg.approvalpro.ace.handler;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.uif2.ShowStatusBarMsgUtil;
/**
 * ����༭���¼���У��������ַ����Ƿ���ʱ���ʽ
 * @author wenjie
 *
 */
public class AceBodyAfterEditHandler implements IAppEventHandler<CardBodyAfterEditEvent>{
	private static List<String> list = new ArrayList<String>();
	static{
		if(list == null){
			list = new ArrayList<String>();
		}
		list.add("def2");
		list.add("def3");
		list.add("def4");
		list.add("def5");
		list.add("def6");
		list.add("def7");
		list.add("def8");
	}
	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		// TODO �Զ����ɵķ������
		if(list.contains(e.getKey()) && e.getRow()==1){
			String rex = "[0-9][0-9][0-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])";
			if(e.getValue()!=null){
				//У����������ڸ�ʽ�Ƿ�ΪYYYY-MM-DD
				boolean matches = ((String) e.getValue()).matches(rex);
				if(!matches){
					e.getBillCardPanel().getBillModel(e.getTableCode()).setValueAt(null, e.getRow(), e.getKey());
					ShowStatusBarMsgUtil.showErrorMsg("��ʾ", "�������ʽΪ��YYYY-MM-DD��������", e.getContext());
				}else{
					ShowStatusBarMsgUtil.showStatusBarMsg(null, e.getContext());
				}
			}
			if("def8".equals(e.getKey())){
				if(e.getValue()!=null){
					e.getBillCardPanel().getHeadItem("def19").setNull(true);
				}else{
					e.getBillCardPanel().getHeadItem("def19").setNull(false);
				}
			}
		}
	}
}
