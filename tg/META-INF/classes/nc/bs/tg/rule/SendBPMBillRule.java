package nc.bs.tg.rule;



import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.ITGRZ_MortgageAgreementMaintain;
import nc.itf.tg.bpm.IPushBillToBpm;
import nc.itf.tg.outside.IBPMBillCont;
import nc.vo.pub.BusinessException;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;

public class SendBPMBillRule implements IRule<AggMortgageAgreementVO> {
	
	IPushBillToBpm servcie = NCLocator.getInstance().lookup(
			IPushBillToBpm.class);
	

	@Override
	public void process(AggMortgageAgreementVO[] vos) {
		// TODO 自动生成的方法存根
		if (vos != null && vos.length > 0) {
			try {
				for (AggMortgageAgreementVO aggvo : vos) {
					aggvo = (AggMortgageAgreementVO) servcie.pushBillToBPM(
							 IBPMBillCont.BILL_RZ04, aggvo);
				}
			} catch (BusinessException e) {
				// TODO: handle exception
				Logger.error(e.getMessage(), e);
				nc.vo.pubapp.pattern.exception.ExceptionUtils
						.wrappBusinessException(e.getMessage());
			}
		}
	}
	
	
}
