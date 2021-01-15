package nc.bs.tg.pf.messagepriority;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;

/**
 * RZ06 融资费用 根据id获取业务单据优先级
 * 
 * @author 谈子健
 * @since 2020-03-02
 * @version NC6.5
 */
public class FinancingCostimpl extends MessagepriorityBase {
	BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			BaseDAO dao = new BaseDAO();
		}
		return dao;
	}

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 高 5 普通 -1低
		return Integer.parseInt(getPriority("tgrz_financexpense", "def3",
				"PK_FINEXPENSE", billid));
	}
}
