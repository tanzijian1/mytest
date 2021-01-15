package nc.bs.pub.action;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.internalinterest.ace.rule.FN06_SaveAfterRule;
import nc.bs.tg.internalinterest.ace.rule.FN06_SaveBeforeRule;
import nc.bs.tg.internalinterest.ace.rule.FN06_UpdateAfterRule;
import nc.bs.tg.internalinterest.plugin.bpplugin.InternalInterestPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IInternalInterestMaintain;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.internalinterest.AggInternalinterest;

public class N_FN06_SAVEBASE extends AbstractPfAction<AggInternalinterest> {

	@Override
	protected CompareAroundProcesser<AggInternalinterest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInternalinterest> processor = null;
		AggInternalinterest[] clientFullVOs = (AggInternalinterest[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggInternalinterest>(
					InternalInterestPluginPoint.SCRIPT_UPDATE);
			IRule<AggInternalinterest> FN06_UpdateAfterRule = new FN06_UpdateAfterRule();
			FN06_SaveBeforeRule afterrule=new FN06_SaveBeforeRule();//为了修改，现在为先删除协同单据，再后面生成协同单据
			processor.addBeforeRule(afterrule);
			processor.addAfterRule(FN06_UpdateAfterRule);
		} else {
			processor = new CompareAroundProcesser<AggInternalinterest>(
					InternalInterestPluginPoint.SCRIPT_INSERT);
//			IRule<AggInternalinterest> FN06_SaveBeforeRule = new FN06_SaveBeforeRule();
			IRule<AggInternalinterest> FN06_SaveAfterRule = new FN06_SaveAfterRule();
//			processor.addBeforeRule(FN06_SaveBeforeRule);
			processor.addAfterRule(FN06_SaveAfterRule);//为了修改，现在为先删除协同单据，再后面生成协同单据
		}
		// TODO 在此处添加前后规则
		return processor;
	}

	/* （非 Javadoc）
	 * @see nc.bs.pubapp.pf.action.AbstractPfAction#processBP(java.lang.Object, nc.vo.pubapp.pattern.model.entity.bill.IBill[], nc.vo.pubapp.pattern.model.entity.bill.IBill[])
	 */
	@Override
	protected AggInternalinterest[] processBP(Object userObj,
			AggInternalinterest[] clientFullVOs, AggInternalinterest[] originBills) {
        BaseDAO dao=new BaseDAO();
		AggInternalinterest[] bills = null;
		try {
			IInternalInterestMaintain operator = NCLocator.getInstance()
					.lookup(IInternalInterestMaintain.class);
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
