package nc.ui.tg.paymentrequest.action;

import java.awt.event.ActionEvent;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

public class PayRequestApproveAction extends nc.ui.pubapp.uif2app.actions.pflow.ApproveScriptAction{
  @Override
public void doAction(ActionEvent e) throws Exception {
	// TODO 自动生成的方法存根
    Object obj=getModel().getSelectedData();
       if(obj==null){
    	   throw new BusinessException("未选中数据");
       }
       AggPayrequest vo=(AggPayrequest)obj;
		     if("FN01-Cxx-002".equals(vo.getParentVO().getTranstype())||"FN01-Cxx-006".equals(vo.getParentVO().getTranstype()))
				throw new BusinessException("自动审批的单据，请到应付单管理节点审批");
	super.doAction(e);
}

}
