package nc.bs.tg.pf.messagepriority;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;

/**
 * RZ06 ���ʷ��� ����id��ȡҵ�񵥾����ȼ�
 * 
 * @author ̸�ӽ�
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
		// 10 �� 5 ��ͨ -1��
		return Integer.parseInt(getPriority("tgrz_financexpense", "def3",
				"PK_FINEXPENSE", billid));
	}
}
