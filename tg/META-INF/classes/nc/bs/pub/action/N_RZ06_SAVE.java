package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.bs.tg.financingexpense.rule.PushBPMBillRule;
import nc.bs.tg.financingexpense.rule.PushImageRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IFinancingExpenseMaintain;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class N_RZ06_SAVE extends AbstractPfAction<AggFinancexpenseVO> {

	protected CompareAroundProcesser<AggFinancexpenseVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancexpenseVO> processor = new CompareAroundProcesser<AggFinancexpenseVO>(
				FinancingExpensePluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggFinancexpenseVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		 //获取全局参数
	    ISysInitQry SysInitQry = NCLocator.getInstance().lookup(ISysInitQry.class);
	    String para = null;
	    String para_ima=null;
	    try {
			para = SysInitQry.getParaString(nc.itf.org.IOrgConst.GLOBEORG, "TGRZ02");
			para_ima = SysInitQry.getParaString(nc.itf.org.IOrgConst.GLOBEORG, "TGRZ04");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			nc.vo.pubapp.pattern.exception.ExceptionUtils
					.wrappBusinessException(e.getMessage());
		}
	  //影像代办
	    IRule<AggFinancexpenseVO> pushrule= new PushImageRule(para_ima);
	    processor.addBeforeRule(pushrule);
	    if ("Y".equals(para)) {
		rule = new PushBPMBillRule();
		processor.addBeforeRule(rule);
		}
	  
		return processor;
	}

	@Override
	protected AggFinancexpenseVO[] processBP(Object userObj,
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills) {
		IFinancingExpenseMaintain operator = NCLocator.getInstance().lookup(
				IFinancingExpenseMaintain.class);
		AggFinancexpenseVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
