package nc.bs.pub.action;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.sealflow.plugin.bpplugin.SealflowPluginPoint;
import nc.vo.tg.sealflow.AggSealFlowVO;
import nc.itf.tg.ISealflowMaintain;
import nc.itf.uif.pub.IUifService;

public class N_SD01_APPROVE extends AbstractPfAction<AggSealFlowVO> {
	BaseDAO baseDAO = null;
	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	public N_SD01_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggSealFlowVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSealFlowVO> processor = new CompareAroundProcesser<AggSealFlowVO>(
				SealflowPluginPoint.APPROVE);
		
		processor.addBeforeRule(new ApproveStatusCheckRule());
		
		return processor;
	}

	@Override
	protected AggSealFlowVO[] processBP(Object userObj,
			AggSealFlowVO[] clientFullVOs, AggSealFlowVO[] originBills) {
		AggSealFlowVO[] bills = null;
		ISealflowMaintain operator = NCLocator.getInstance().lookup(
				ISealflowMaintain.class);
		try {
			AggSealFlowVO[] clientFullVO = new AggSealFlowVO[clientFullVOs.length];
			AggSealFlowVO[] originBill = new AggSealFlowVO[originBills.length];
			int i = 0;
			int j = 0;
			if(clientFullVOs != null){
				for(AggSealFlowVO vo : clientFullVOs){
					vo.getParent().setAttributeValue("def57", "Y");
					clientFullVO[i] = vo;
					getBaseDAO().updateVO(vo.getParentVO());
//					String sqli = "update sdfn_sealflow set def57 = 'Y' where sdfn_sealflow.pk_stamp = '"+vo.getParentVO().getPk_stamp()+"'";
//					baseDAO.executeUpdate(sqli);
					i = i+1;
				}
			}
			if(originBills != null){
				for(AggSealFlowVO vo : clientFullVOs){
					vo.getParent().setAttributeValue("def57", "Y");
					originBill[j] = vo;
//					String sqlj = "update sdfn_sealflow set def57 = 'Y' where sdfn_sealflow.pk_stamp = '"+vo.getParentVO().getPk_stamp()+"'";
//					baseDAO.executeUpdate(sqlj);
					j = j+1;
				}
			}
			bills = operator.approve(clientFullVO, originBill);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
