package nc.ui.tg.paymentrequest.action;

import java.awt.event.ActionEvent;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

public class PayRequestApproveAction extends nc.ui.pubapp.uif2app.actions.pflow.ApproveScriptAction{
  @Override
public void doAction(ActionEvent e) throws Exception {
	// TODO �Զ����ɵķ������
    Object obj=getModel().getSelectedData();
       if(obj==null){
    	   throw new BusinessException("δѡ������");
       }
       AggPayrequest vo=(AggPayrequest)obj;
		     if("FN01-Cxx-002".equals(vo.getParentVO().getTranstype())||"FN01-Cxx-006".equals(vo.getParentVO().getTranstype()))
				throw new BusinessException("�Զ������ĵ��ݣ��뵽Ӧ��������ڵ�����");
	super.doAction(e);
}

}
