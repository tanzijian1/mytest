package nc.bs.tg.pf.messagepriority;

import nc.vo.pub.BusinessException;

/**
 * FN01 付款申请单 根据id获取业务单据优先级
 * 
 * @author 谈子健
 * @since 2020-02-19
 * @version NC6.5
 */
public class PayreQuestpriorityimpl extends MessagepriorityBase {

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 高 5 普通 -1低
		return Integer.parseInt(getPriority("tgfn_payrequest", "def9",
				"pk_payreq", billid));
	}

}
