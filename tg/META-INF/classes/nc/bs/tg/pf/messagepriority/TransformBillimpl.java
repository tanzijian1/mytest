package nc.bs.tg.pf.messagepriority;

import nc.vo.pub.BusinessException;

/**
 * 36S4 划账结算
 * 根据id获取业务单据优先级
 * 
 * @author 谈子健
 * @since 2020-05-04
 * @version NC6.5
 */
public class TransformBillimpl extends MessagepriorityBase {

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 高 5 普通 -1低
		return Integer.parseInt(getPriority("cmp_transformbill", "vdef2",
				"pk_transformbill", billid));
	}

	
}
