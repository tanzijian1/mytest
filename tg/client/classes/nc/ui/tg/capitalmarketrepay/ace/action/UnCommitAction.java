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
		//�������ջ�ǰ����,���������bill�ض���ֵ
		IBill bill = (IBill) getModel().getSelectedData();
		AggMarketRepalayVO aggvo = (AggMarketRepalayVO)bill;
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(IPushBPMBillFileService.class);
		service.pushRetrieveBpmFile(IBPMBillCont.PROCESSNAME_SD08, aggvo.getParentVO().getDef20());
		// TODO �Զ����ɵķ������
		super.doAction(e);
	}
	
}
