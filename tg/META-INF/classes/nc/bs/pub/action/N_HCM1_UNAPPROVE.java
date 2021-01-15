package nc.bs.pub.action;

import java.util.Collection;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.bs.tg.salaryfundaccure.plugin.bpplugin.SalaryfundaccurePluginPoint;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.itf.tg.ISalaryfundaccureMaintain;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uif.pub.IUifService;
import nc.md.persist.framework.IMDPersistenceQueryService;

public class N_HCM1_UNAPPROVE extends AbstractPfAction<AggSalaryfundaccure> {

	private IPFBusiAction pfBusiAction;
	private IMDPersistenceQueryService mdQryService;

	@Override
	protected CompareAroundProcesser<AggSalaryfundaccure> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSalaryfundaccure> processor = new CompareAroundProcesser<AggSalaryfundaccure>(
				SalaryfundaccurePluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggSalaryfundaccure[] processBP(Object userObj,
			AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) {
		AggSalaryfundaccure[] bills = null;
		try {
			SendVoucherUtil util = new SendVoucherUtil();
			for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
				clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
				util.delVoucher(clientFullVOs[i]);
			ISalaryfundaccureMaintain operator = NCLocator.getInstance()
					.lookup(ISalaryfundaccureMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);}
			Integer beforeApprovestatus = (Integer) originBills[0]
					.getParentVO().getAttributeValue("approvestatus");
			Integer afterApprovestatus = (Integer) clientFullVOs[0]
					.getParentVO().getAttributeValue("approvestatus");
			if (beforeApprovestatus == 1 && afterApprovestatus == 3) {
				String payBillID = originBills[0].getParentVO().getDef3();// 获取付款单主键
				IUifService iUifService = NCLocator.getInstance().lookup(
						IUifService.class);
				String[] payBillUserObj = { AggPayBillVO.class.getName(),
						PayBillVO.class.getName(),
						PayBillItemVO.class.getName() };
				AggregatedValueObject aggvo = null;
				if(payBillID!=null&&!"".equals(payBillID)){
					aggvo = iUifService
							.queryBillVOByPrimaryKey(payBillUserObj, payBillID);
				}
				if (aggvo!=null&&0!=aggvo.getChildrenVO().length) {
					throw new BusinessException("存在下游单据,无法取消审批");
//					// 删除单据
//					HashMap eParam = new HashMap();
//					eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
//							PfUtilBaseTools.PARAM_NOTE_CHECKED);
//					// 删除付款申请单
//					AggPayBillVO[] billvos = (AggPayBillVO[]) getPfBusiAction()
//							.processAction(IPFActionName.DEL_DELETE, "F3", null,
//									aggvo, null, eParam);
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

}
