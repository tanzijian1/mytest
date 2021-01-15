package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IFinancingExpenseMaintain;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;

public class N_RZ06_SAVEBASE extends AbstractPfAction<AggFinancexpenseVO> {

	@Override
	protected CompareAroundProcesser<AggFinancexpenseVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancexpenseVO> processor = null;
		//获取全局参数
	    ISysInitQry SysInitQry = NCLocator.getInstance().lookup(ISysInitQry.class);
//	    String para_ima=null;
//	    try {
//			para_ima = SysInitQry.getParaString(nc.itf.org.IOrgConst.GLOBEORG, "TGRZ04");
//		} catch (BusinessException e) {
//			Logger.error(e.getMessage(), e);
//			nc.vo.pubapp.pattern.exception.ExceptionUtils
//					.wrappBusinessException(e.getMessage());
//		}
	    
		AggFinancexpenseVO[] clientFullVOs = (AggFinancexpenseVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggFinancexpenseVO>(
					FinancingExpensePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggFinancexpenseVO>(
					FinancingExpensePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggFinancexpenseVO> rule = null;
//		//影像代办
//		IRule<AggFinancexpenseVO> pushrule= new PushImageRule(para_ima);
//	    processor.addAfterRule(pushrule);
		return processor;
	}

	@Override
	protected AggFinancexpenseVO[] processBP(Object userObj,
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills) {

		AggFinancexpenseVO[] bills = null;
		try {
			IFinancingExpenseMaintain operator = NCLocator.getInstance()
					.lookup(IFinancingExpenseMaintain.class);
			FinancexpenseVO parentVO=clientFullVOs[0].getParentVO();
			
			if(parentVO!=null&&"RZ06-Cxx-001".equals(parentVO.getAttributeValue("transtype"))){
				if(parentVO.getDef12()==null){
					if(parentVO.getAttributeValue("def61")==null){//需要先判断新字段付款单位是否为空,为空则走旧的判断 add by tjl 2020-05-13
						//本次请款金额系统要校验【累计已付款+本次请款金额】小于等于【贷款合同明细】表体分摊给该公司的财顾费金额
						UFDouble applyamount= (UFDouble) parentVO.getAttributeValue("applyamount")==null?UFDouble.ZERO_DBL:(UFDouble) parentVO.getAttributeValue("applyamount");
						//本单位累计付款金额
						UFDouble paymentamount= parentVO.getAttributeValue("def8")==null?UFDouble.ZERO_DBL:new UFDouble((String)parentVO.getAttributeValue("def8"));
						UFDouble total=paymentamount.add(applyamount);
						UFDouble shareamount=(String)parentVO.getAttributeValue("def6")==null?new UFDouble(0):new UFDouble((String)parentVO.getAttributeValue("def6"));
						if(total.compareTo(shareamount)>0){
							throw new BusinessException("【累计已付款+本次请款金额】大于【贷款合同明细】分摊给该公司的财顾费金额！");
						}
					}
				}else{
					//单期发行情况不为空，在此校验
					//本次请款金额
					UFDouble applyamount= parentVO.getApplyamount()==null?UFDouble.ZERO_DBL:parentVO.getApplyamount();
					//累计已付款金额
					UFDouble paymentamount= parentVO.getPaymentamount()==null?UFDouble.ZERO_DBL:parentVO.getPaymentamount();
					//财务分摊金额
					UFDouble shareamount=parentVO.getDef6()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef6());
					if((applyamount.add(paymentamount)).compareTo(shareamount)>0){
						throw new BusinessException("【累计已付款+本次请款金额】大于【单期发行管理】分摊给该公司的财顾费金额！");
					}
				}
			}
			
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
