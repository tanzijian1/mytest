package nc.bs.tg.invoicing.ace.rule;

import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.vo.trade.pub.IBillStatus;

public class InvoicingSendAddDAPRule implements IRule<AggInvoicingHead> {

	@Override
	public void process(AggInvoicingHead[] vos) {
		TGSendVoucherUtil util = new TGSendVoucherUtil();
		for (AggInvoicingHead vo : vos) {
			try {
				if (IBillStatus.CHECKPASS == ((Integer) vo.getParentVO()
						.getAttributeValue("approvestatus")).intValue())
					util.addVoucher(vo);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}

		}
	}

}
