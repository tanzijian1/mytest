package nc.bs.tg.pf.messagepriority;

import nc.vo.pub.BusinessException;

/**
 * F3 ���
 * ����id��ȡҵ�񵥾����ȼ�
 * 
 * @author ̸�ӽ�
 * @since 2020-02-19
 * @version NC6.5
 */
public class ApPayBillpriorityimpl extends MessagepriorityBase {

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 �� 5 ��ͨ -1��
		return Integer.parseInt(getPriority("ap_paybill", "def9",
				"pk_paybill", billid));
	}

	
}
