package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.masterdata.plugin.bpplugin.MasterdataPluginPoint;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.itf.tg.IMasterdataMaintain;

public class N_SD02_DELETE extends AbstractPfAction<AggMasterDataVO> {

	@Override
	protected CompareAroundProcesser<AggMasterDataVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMasterDataVO> processor = new CompareAroundProcesser<AggMasterDataVO>(
				MasterdataPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggMasterDataVO[] processBP(Object userObj,
			AggMasterDataVO[] clientFullVOs, AggMasterDataVO[] originBills) {
		IMasterdataMaintain operator = NCLocator.getInstance().lookup(
				IMasterdataMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
