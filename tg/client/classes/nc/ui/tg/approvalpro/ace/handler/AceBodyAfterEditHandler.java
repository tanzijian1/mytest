package nc.ui.tg.approvalpro.ace.handler;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.uif2.ShowStatusBarMsgUtil;
/**
 * 表体编辑后事件，校验输入的字符串是否是时间格式
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
		// TODO 自动生成的方法存根
		if(list.contains(e.getKey()) && e.getRow()==1){
			String rex = "[0-9][0-9][0-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])";
			if(e.getValue()!=null){
				//校验输入的日期格式是否为YYYY-MM-DD
				boolean matches = ((String) e.getValue()).matches(rex);
				if(!matches){
					e.getBillCardPanel().getBillModel(e.getTableCode()).setValueAt(null, e.getRow(), e.getKey());
					ShowStatusBarMsgUtil.showErrorMsg("提示", "请输入格式为【YYYY-MM-DD】的日期", e.getContext());
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
