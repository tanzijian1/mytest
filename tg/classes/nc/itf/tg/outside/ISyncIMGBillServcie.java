package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

/**
 * ����ϵͳ�Խ��ڲ�����ӿ�
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncIMGBillServcie {

	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException;

	public void onSyncInv_RequiresNew(String imgid) throws Exception;

	/**
	 * ��Ӱ���ȡ��Ʊ����
	 * @param imgid
	 *            Ӱ�����
	 * @param isLLBill
	 *            �Ƿ����ﵥ��
	 * @throws Exception
	 */
	public void onSyncInv_RequiresNew(String imgid, Boolean isLLBill)
			throws Exception;

}
