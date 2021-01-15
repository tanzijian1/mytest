package nc.bs.pub.action;

import java.util.Collection;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.fischeme.ace.bp.AceFischemeChangeBP;
import nc.bs.tg.fischeme.ace.bp.AceFischemeUnApproveBP;
import nc.bs.tg.fischeme.plugin.bpplugin.FischemePluginPoint;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.fischeme.FISchemeBVO;
import nc.vo.tg.projectdata.ProjectDataVVO;
import nc.itf.tg.IFischemeMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;

public class N_RZ05_SAVEBASE extends AbstractPfAction<AggFIScemeHVO> {

	@Override
	protected CompareAroundProcesser<AggFIScemeHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFIScemeHVO> processor = null;
		AggFIScemeHVO[] clientFullVOs = (AggFIScemeHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggFIScemeHVO>(
					FischemePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggFIScemeHVO>(
					FischemePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggFIScemeHVO> rule = null;

		return processor;
	}

	@Override
	protected AggFIScemeHVO[] processBP(Object userObj,
			AggFIScemeHVO[] clientFullVOs, AggFIScemeHVO[] originBills) {
		Integer auditstate = (Integer) clientFullVOs[0].getParentVO().getAttributeValue(FIScemeHVO.APPROVESTATUS);
		AggFIScemeHVO[] bills = null;
		try {
			IFischemeMaintain operator = NCLocator.getInstance()
					.lookup(IFischemeMaintain.class);
			if (StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO().getPrimaryKey())) {
				bills = operator.insert(clientFullVOs, originBills);
			} else if(auditstate == null || auditstate.equals(BillStatusEnum.FREE.value()))  {
				bills = operator.update(clientFullVOs, originBills);
			}else{
				for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
					clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
				}
				IFischemeMaintain operator1 = NCLocator.getInstance()
						.lookup(IFischemeMaintain.class);
//				try {
					for(AggFIScemeHVO vo:clientFullVOs){
						((FIScemeHVO)vo.getParent()).setAttributeValue("approvestatus","-1");
						((FIScemeHVO)vo.getParent()).setAttributeValue("emendenum","0");
						 ((nc.vo.tg.fischeme.AggFIScemeHVO)vo).getParentVO().setApprover(null);
					      ((nc.vo.tg.fischeme.AggFIScemeHVO)vo).getParentVO().setApprovenote(null);
					      ((nc.vo.tg.fischeme.AggFIScemeHVO)vo).getParentVO().setApprovestatus(Integer.valueOf(BillStatusEnum.FREE.toIntValue()));
					      ((nc.vo.tg.fischeme.AggFIScemeHVO)vo).getParentVO().setCreator(AppContext.getInstance().getPkUser());
					      ((nc.vo.tg.fischeme.AggFIScemeHVO)vo).getParentVO().setCreationtime(AppContext.getInstance().getServerTime());
					}
//					bills = operator1.unapprove(clientFullVOs, originBills);
//					AceFischemeUnApproveBP bp=new AceFischemeUnApproveBP();
//					bp.unApprove(bills, originBills);
//				} catch (BusinessException e) {
//					// TODO 自动生成的 catch 块
//					e.printStackTrace();
//				}
				bills=new AceFischemeChangeBP().change(clientFullVOs, originBills);
			}
			
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
