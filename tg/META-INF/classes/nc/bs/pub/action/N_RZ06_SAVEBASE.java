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
		//��ȡȫ�ֲ���
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
		// TODO �ڴ˴����ǰ�����
		IRule<AggFinancexpenseVO> rule = null;
//		//Ӱ�����
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
					if(parentVO.getAttributeValue("def61")==null){//��Ҫ���ж����ֶθ��λ�Ƿ�Ϊ��,Ϊ�����߾ɵ��ж� add by tjl 2020-05-13
						//���������ϵͳҪУ�顾�ۼ��Ѹ���+��������С�ڵ��ڡ������ͬ��ϸ�������̯���ù�˾�Ĳƹ˷ѽ��
						UFDouble applyamount= (UFDouble) parentVO.getAttributeValue("applyamount")==null?UFDouble.ZERO_DBL:(UFDouble) parentVO.getAttributeValue("applyamount");
						//����λ�ۼƸ�����
						UFDouble paymentamount= parentVO.getAttributeValue("def8")==null?UFDouble.ZERO_DBL:new UFDouble((String)parentVO.getAttributeValue("def8"));
						UFDouble total=paymentamount.add(applyamount);
						UFDouble shareamount=(String)parentVO.getAttributeValue("def6")==null?new UFDouble(0):new UFDouble((String)parentVO.getAttributeValue("def6"));
						if(total.compareTo(shareamount)>0){
							throw new BusinessException("���ۼ��Ѹ���+�����������ڡ������ͬ��ϸ����̯���ù�˾�Ĳƹ˷ѽ�");
						}
					}
				}else{
					//���ڷ��������Ϊ�գ��ڴ�У��
					//���������
					UFDouble applyamount= parentVO.getApplyamount()==null?UFDouble.ZERO_DBL:parentVO.getApplyamount();
					//�ۼ��Ѹ�����
					UFDouble paymentamount= parentVO.getPaymentamount()==null?UFDouble.ZERO_DBL:parentVO.getPaymentamount();
					//�����̯���
					UFDouble shareamount=parentVO.getDef6()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef6());
					if((applyamount.add(paymentamount)).compareTo(shareamount)>0){
						throw new BusinessException("���ۼ��Ѹ���+�����������ڡ����ڷ��й�����̯���ù�˾�Ĳƹ˷ѽ�");
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
