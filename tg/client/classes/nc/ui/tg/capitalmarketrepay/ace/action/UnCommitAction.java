package nc.ui.tg.capitalmarketrepay.ace.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.outside.IBPMBillCont;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;

public class UnCommitAction extends nc.ui.pubapp.uif2app.actions.pflow.UnCommitScriptAction{

	@Override
	public void doAction(ActionEvent e) throws Exception {
		//由于是收回前动作,所以这里的bill必定有值
		IBill bill = (IBill) getModel().getSelectedData();
		AggMarketRepalayVO aggvo = (AggMarketRepalayVO)bill;
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(IPushBPMBillFileService.class);
		service.pushRetrieveBpmFile(IBPMBillCont.PROCESSNAME_SD08, aggvo.getParentVO().getDef20());
		// TODO 自动生成的方法存根
		super.doAction(e);
	}
	
}
