package nc.bs.tg.pf.messagepriority;

import nc.vo.pub.BusinessException;

/**
 * F1 应付单 根据id获取业务单据优先级
 * 
 * @author 谈子健
 * @since 2020-02-19
 * @version NC6.5
 */
public class Payablebillpriorityimpl extends MessagepriorityBase {

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 高 5 普通 -1低
		 return Integer.parseInt(getPriority("ap_payablebill", "def9",
		 "pk_payablebill", billid));
	}

}
