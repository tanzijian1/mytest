package nc.ui.tg.financingexpense.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.tg.pub.action.pub.model.GdModelDataManager;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class FinancingExpenseSaveAction extends nc.ui.pubapp.uif2app.actions.pflow.SaveScriptAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3880081851590052316L;

	@Override
public void doAction(ActionEvent e) throws Exception {
	// TODO 自动生成的方法存根
		IMDPersistenceQueryService service=NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
	super.doAction(e);
	Object obj= getModel().getSelectedData();
	if(obj!=null){
		AggFinancexpenseVO billvo=	(AggFinancexpenseVO)obj;
		String pk=billvo.getParentVO().getPk_finexpense();
		NCObject nobj=service.queryBillOfNCObjectByPK(AggFinancexpenseVO.class, pk);
		if(nobj!=null){
			getModel().update(nobj.getContainmentObject());
		}
	}
	
}
}
