package nc.bs.tg.pub.paymentrequest.rule;
/**
 * 付款申请单取消审批校验
 * 2019年12月26日
 */
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

public class UnapprovePayBillCheckRule implements IRule {
	public void process(Object[] bills) {
		if (bills == null) {
			return;
		}
		for (Object bill : bills) {

			AggPayrequest aggvo = (AggPayrequest) bill;
			
			String pk_payreq = aggvo.getParentVO().pk_payreq;
			
			boolean check = querySrcPayBill(pk_payreq);

			if (!check) {
				ExceptionUtils.wrappBusinessException("该付款申请单已有下游付款单，不能取消审批");
			}
		}
	}
	
	public boolean querySrcPayBill(String src_billid) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);

		String sql = "select count(1) from ap_payitem where nvl(dr,0)=0 and src_billid = '"
				+ src_billid + "'";

		int number = 0;

		try {
			number = (int) bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (number > 0) {
			return false;
		}

		return true;
	}
}
