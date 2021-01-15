package nc.bs.tg.capitalmarketrepay.ace.bp.rule;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.bpm.IPushBillToBpm;
import nc.itf.tg.outside.IBPMBillCont;
import nc.vo.ecpubapp.pattern.exception.ExceptionUtils;
import nc.vo.pub.BusinessException;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;

public class SendBPMBillRule implements IRule<AggMarketRepalayVO>{

	@Override
	public void process(AggMarketRepalayVO[] vos) {
		// TODO 自动生成的方法存根
		if(vos != null && vos.length>0){
			IPushBillToBpm servcie = NCLocator.getInstance().lookup(IPushBillToBpm.class);
			try {
				for (AggMarketRepalayVO aggvo : vos) {
					aggvo = (AggMarketRepalayVO)servcie.pushBillToBPM(IBPMBillCont.BILL_SD08, aggvo);
				}
			} catch (BusinessException e) {
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}
		}
	}

}
