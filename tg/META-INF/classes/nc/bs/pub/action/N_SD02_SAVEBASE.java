package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.masterdata.plugin.bpplugin.MasterdataPluginPoint;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.itf.tg.IMasterdataMaintain;

public class N_SD02_SAVEBASE extends AbstractPfAction<AggMasterDataVO> {

	@Override
	protected CompareAroundProcesser<AggMasterDataVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMasterDataVO> processor = null;
		AggMasterDataVO[] clientFullVOs = (AggMasterDataVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggMasterDataVO>(
					MasterdataPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggMasterDataVO>(
					MasterdataPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggMasterDataVO> rule = null;

		return processor;
	}

	@Override
	protected AggMasterDataVO[] processBP(Object userObj,
			AggMasterDataVO[] clientFullVOs, AggMasterDataVO[] originBills) {

		AggMasterDataVO[] bills = null;
		try {
			IMasterdataMaintain operator = NCLocator.getInstance()
					.lookup(IMasterdataMaintain.class);
			if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.update(clientFullVOs, originBills);
			} else {
				bills = operator.insert(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
