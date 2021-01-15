package nc.bs.tg.financingexpense.rule;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;

public class DeleteRePayToBPMBeforeRule extends BPMBillUtils implements IRule<AggRePayReceiptBankCreditVO>{

	IPushBPMBillFileService service = NCLocator.getInstance().lookup(
			IPushBPMBillFileService.class);
	
	@Override
	public void process(AggRePayReceiptBankCreditVO[] vos) {
		// TODO 自动生成的方法存根
		if(vos != null && vos.length > 0){
			for (AggRePayReceiptBankCreditVO aggvo : vos) {
				try {
					if (aggvo.getParentVO().getRepayamount() == null
							&& (aggvo.getParentVO().getInterest() != null||aggvo.getParentVO().getAttributeValue("preinterestmoney")!=null)) {//还息
						aggvo = (AggRePayReceiptBankCreditVO) service.pushToFinBpmDeleteFile(aggvo,ISaleBPMBillCont.BILL_16);
					}else{//还本
						aggvo = (AggRePayReceiptBankCreditVO) service.pushToFinBpmDeleteFile(aggvo,ISaleBPMBillCont.BILL_15);
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					nc.vo.pubapp.pattern.exception.ExceptionUtils
							.wrappBusinessException(e.getMessage());
				}
			}
		}
	}

}
