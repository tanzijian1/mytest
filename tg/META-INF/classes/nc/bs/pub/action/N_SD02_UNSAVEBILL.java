package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.masterdata.plugin.bpplugin.MasterdataPluginPoint;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.itf.tg.IMasterdataMaintain;

public class N_SD02_UNSAVEBILL extends AbstractPfAction<AggMasterDataVO> {

	@Override
	protected CompareAroundProcesser<AggMasterDataVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMasterDataVO> processor = new CompareAroundProcesser<AggMasterDataVO>(
				MasterdataPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggMasterDataVO[] processBP(Object userObj,
			AggMasterDataVO[] clientFullVOs, AggMasterDataVO[] originBills) {
		IMasterdataMaintain operator = NCLocator.getInstance().lookup(
				IMasterdataMaintain.class);
		AggMasterDataVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
