package nc.bs.tg.financingexpense.rule;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.vo.pub.BusinessException;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class DeleteFinToBPMBeforeRule extends BPMBillUtils implements IRule<AggFinancexpenseVO>{

	IPushBPMBillFileService service = NCLocator.getInstance().lookup(
			IPushBPMBillFileService.class);
	@Override
	public void process(AggFinancexpenseVO[] vos) {
		// TODO 自动生成的方法存根
		if(vos != null && vos.length > 0){
			for (AggFinancexpenseVO aggvo : vos) {
				if(aggvo.getParentVO().getAttributeValue("def19")!=null){
					try {
						if ("RZ06-Cxx-001".equals(getTranstypeCode((String) aggvo.getParentVO()
								.getAttributeValue("transtypepk")))) {//财顾费
							aggvo = (AggFinancexpenseVO) service.pushToFinBpmDeleteFile(aggvo,ISaleBPMBillCont.BILL_17);
						}else{
							aggvo = (AggFinancexpenseVO) service.pushToFinBpmDeleteFile(aggvo,ISaleBPMBillCont.BILL_18);
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

}
