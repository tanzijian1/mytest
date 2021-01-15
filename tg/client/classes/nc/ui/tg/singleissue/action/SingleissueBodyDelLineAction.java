package nc.ui.tg.singleissue.action;

import java.awt.event.ActionEvent;

import nc.ui.tg.singleissue.util.SingleIssueUtil;
import nc.util.SdfnUtil;
import nc.vo.pub.BusinessException;

public class SingleissueBodyDelLineAction extends nc.ui.pubapp.uif2app.actions.BodyDelLineAction{

	@Override
	public void doAction(ActionEvent e) throws Exception {
		String tableCode = getCardPanel().getBodyPanel().getTableCode();
		String value = (String)getCardPanel().getHeadItem("def1").getValueObject();
		String busiType = SingleIssueUtil.getBusiType(value);
		if(busiType==null &&("pk_bondresale".equals(tableCode) || "pk_repayplan".equals(tableCode)
				|| "pk_cyclebuying".equals(tableCode) || "pk_absrepay".equals(tableCode))) 
			throw new BusinessException("关联批文字段为空，当前表体不可编辑");
		
		if(SdfnUtil.getABSList().contains(busiType) && "pk_bondresale".equals(tableCode)){
			throw new BusinessException("关联批文的业务类型为ABS，【债券回售】不可编辑");
		}
		
		if(!SdfnUtil.getABSList().contains(busiType) && "pk_repayplan".equals(tableCode)){
			throw new BusinessException("关联批文的业务类型为债券，【还款计划】不可编辑");
		}
		if("pk_cyclebuying".equals(tableCode) && "供应链ABS".equals(busiType)){
			throw new BusinessException("关联批文的业务类型为供应链ABS，【循环购买计划】不可编辑");
		}
		if("pk_absrepay".equals(tableCode) && "购房尾款ABS".equals(busiType)){
			throw new BusinessException("关联批文的业务类型为购房尾款ABS，【供应链ABS还款计划】不可编辑");
		}
		super.doAction(e);
	}
	
}
