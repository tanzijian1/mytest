package nc.bs.tg.financingexpense.rule;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.bpm.IPushBillToBpm;
import nc.itf.tg.outside.IBPMBillCont;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.vo.pub.BusinessException;
import nc.vo.tg.addticket.AggAddTicket;

public class PushAddTicketToBPMBillRule extends BPMBillUtils implements IRule<AggAddTicket>{

	IPushBillToBpm servcie = NCLocator.getInstance().lookup(
			IPushBillToBpm.class);
	@Override
	public void process(AggAddTicket[] vos) {
		if (vos != null && vos.length > 0) {
			try {

				for (AggAddTicket vo : vos) {
					String processname = null;
					processname = ISaleBPMBillCont.BILL_19;
					vo = (AggAddTicket) servcie.pushBillToBPM(
							processname, vo);
				}

			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				nc.vo.pubapp.pattern.exception.ExceptionUtils
						.wrappBusinessException(e.getMessage());
			}
		}
	}

}
