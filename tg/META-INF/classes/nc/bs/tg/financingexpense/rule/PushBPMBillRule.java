package nc.bs.tg.financingexpense.rule;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.bpm.IPushBillToBpm;
import nc.itf.tg.outside.IBPMBillCont;
import nc.vo.pub.BusinessException;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class PushBPMBillRule extends BPMBillUtils implements IRule<AggFinancexpenseVO> {
	IPushBillToBpm servcie = NCLocator.getInstance().lookup(
			IPushBillToBpm.class);
	

	@Override
	public void process(AggFinancexpenseVO[] vos) {
		if (vos != null && vos.length > 0) {
			try {

				for (AggFinancexpenseVO vo : vos) {
					String processname = null;
					if ("RZ06-Cxx-001".equals(getTranstypeCode((String) vo.getParentVO()
							.getAttributeValue("transtypepk")))) {
						processname = IBPMBillCont.BILL_RZ06_01;
					} else {
						processname = IBPMBillCont.BILL_RZ06_02;
					}
					vo = (AggFinancexpenseVO) servcie.pushBillToBPM(
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
