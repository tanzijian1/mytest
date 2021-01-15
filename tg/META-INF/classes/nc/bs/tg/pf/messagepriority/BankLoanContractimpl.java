package nc.bs.tg.pf.messagepriority;

import nc.vo.pub.BusinessException;

/**
 * 36FF 银行贷款合同还款 根据id获取业务单据优先级
 * 
 * @author 谈子健
 * @since 2020-03-02
 * @version NC6.5
 */
public class BankLoanContractimpl extends MessagepriorityBase {

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 高 5 普通 -1低
		// return Integer.parseInt(getPriority("cdm_repayrcpt", "def32",
		// "pk_repayrcpt", billid));
		return 10;
	}

}
