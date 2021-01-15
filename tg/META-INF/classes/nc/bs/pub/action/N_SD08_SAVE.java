package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.bs.tg.capitalmarketrepay.ace.bp.rule.PushImageRule;
import nc.bs.tg.capitalmarketrepay.ace.bp.rule.SendBPMBillRule;
import nc.bs.tg.capitalmarketrepay.plugin.bpplugin.CapitalmarketrepayPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ICapitalmarketrepayMaintain;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;

public class N_SD08_SAVE extends AbstractPfAction<AggMarketRepalayVO> {

	protected CompareAroundProcesser<AggMarketRepalayVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMarketRepalayVO> processor = new CompareAroundProcesser<AggMarketRepalayVO>(
				CapitalmarketrepayPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggMarketRepalayVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
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
	    if("Y".equals(para_ima)){
	    	processor.addBeforeRule(new PushImageRule());
	    }
	    if("Y".equals(para)){
	    	processor.addBeforeRule(new SendBPMBillRule());
	    }
		return processor;
	}

	@Override
	protected AggMarketRepalayVO[] processBP(Object userObj,
			AggMarketRepalayVO[] clientFullVOs, AggMarketRepalayVO[] originBills) {
		ICapitalmarketrepayMaintain operator = NCLocator.getInstance().lookup(
				ICapitalmarketrepayMaintain.class);
		AggMarketRepalayVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
