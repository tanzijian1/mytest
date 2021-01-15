package nc.bs.pub.action;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.outbill.plugin.bpplugin.OutBillPluginPoint;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.itf.tg.IOutBillMaintain;

public class N_FN04_SAVEBASE extends AbstractPfAction<AggOutbillHVO> {
	
	private BaseDAO dao = null;

	@Override
	protected CompareAroundProcesser<AggOutbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOutbillHVO> processor = null;
		AggOutbillHVO[] clientFullVOs = (AggOutbillHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggOutbillHVO>(
					OutBillPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggOutbillHVO>(
					OutBillPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggOutbillHVO> rule = null;

		return processor;
	}

	@Override
	protected AggOutbillHVO[] processBP(Object userObj,
			AggOutbillHVO[] clientFullVOs, AggOutbillHVO[] originBills) {
		//add by tjl 2020-01-19 解决修改保存后财务组织为空的问题
		for (AggOutbillHVO clientFullVO : clientFullVOs) {
			OrgVO orgvo = null;
			try {
				orgvo = (OrgVO) getBaseDAO().retrieveByPK(OrgVO.class, clientFullVO.getParentVO().getPk_org());
			} catch (DAOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			clientFullVO.getParentVO().setPk_org_v(orgvo.getPk_vid());
		}
		//end
		AggOutbillHVO[] bills = null;
		try {
			IOutBillMaintain operator = NCLocator.getInstance()
					.lookup(IOutBillMaintain.class);
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
	
	public BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
}
