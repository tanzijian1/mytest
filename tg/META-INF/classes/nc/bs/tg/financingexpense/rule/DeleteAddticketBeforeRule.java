package nc.bs.tg.financingexpense.rule;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.vo.tg.addticket.AggAddTicket;

public class DeleteAddticketBeforeRule extends BPMBillUtils implements IRule<AggAddTicket>{

	IPushBPMBillFileService service = NCLocator.getInstance().lookup(
			IPushBPMBillFileService.class);
	@Override
	public void process(AggAddTicket[] vos) {
		if(vos != null && vos.length > 0){
			for (AggAddTicket aggvo : vos) {
				if(aggvo.getParentVO().getAttributeValue("def19")!=null){
					try {
						aggvo = (AggAddTicket) service.pushToFinBpmDeleteFile(aggvo,ISaleBPMBillCont.BILL_19);
					} catch (Exception e) {
						Logger.error(e.getMessage(), e);
						nc.vo.pubapp.pattern.exception.ExceptionUtils
						.wrappBusinessException(e.getMessage());
					}
				}
			}
		}
		
	}

}
