package nc.ui.tg.singleissue.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.tg.singleissue.util.SingleIssueUtil;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.util.SdfnUtil;
/**
 * 表体编辑前，设置还款计划可编辑状态
 * @author wenjie
 *
 */
public class AceBodyBeforeEditHandler implements
		IAppEventHandler<CardBodyBeforeEditEvent> {
	@Override
	public void handleAppEvent(CardBodyBeforeEditEvent e) {
		String code = e.getBillCardPanel().getBodyPanel().getTableCode();
		try {
			String pk_appro = (String)e.getBillCardPanel().getHeadItem("def1").getValueObject();
			String busiType = SingleIssueUtil.getBusiType(pk_appro);
			if(busiType == null){
				if("pk_bondresale".equals(code) || "pk_repayplan".equals(code)
					|| "pk_cyclebuying".equals(code) || "pk_absrepay".equals(code)){
					e.setReturnValue(false);
				}else{
					e.setReturnValue(true);
				}
			}else{
				if("pk_bondresale".equals(code) && SdfnUtil.getABSList().contains(busiType)){
					e.setReturnValue(false);
				}else if("pk_repayplan".equals(code) && !SdfnUtil.getABSList().contains(busiType)){
					e.setReturnValue(false);
				}else if("pk_cyclebuying".equals(code) && "供应链ABS".equals(busiType)){
					e.setReturnValue(false);
				}else if("pk_absrepay".equals(code) && "购房尾款ABS".equals(busiType)){
					e.setReturnValue(false);
				}else{
					e.setReturnValue(true);
				}
			}
		} catch (Exception e2) {
			ShowStatusBarMsgUtil.showErrorMsg("编辑失败", "发生异常，不可编辑表体", e.getContext());
		}
		//财顾费执行情况和合同执行情况页签不允许编辑
		if("pk_bondtranssale".equals(code) || "pk_constateexe".equals(code)){
			e.setReturnValue(false);
		}
		if("pk_issuedetail".equals(code)){
			if("def3".equals(e.getKey())){
				DefaultConstEnum value = (DefaultConstEnum)e.getBillCardPanel().getBillModel(code).getValueObjectAt(e.getRow(), "def2");
				String pk_org = value==null?null:(String)value.getValue();
				
				UIRefPane refPane = (UIRefPane)e.getBillCardPanel().getBodyItem(code, "def3").getComponent();
				refPane.getRefModel().setPk_org(pk_org);
			}
		}
	}
}