package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

public interface ISyncWordBillServcie {

	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException;

	/**
	 * ͬ��ƾ֤�м��
	 * 
	 * @param org
	 *            ��֯
	 * @param year
	 *            ���
	 * @param proid
	 *            �ڼ�
	 * @throws BusinessException
	 */
	public void onSyncVoucherMinTab_RequiresNew(String org, String year,
			String proid) throws BusinessException;
}
