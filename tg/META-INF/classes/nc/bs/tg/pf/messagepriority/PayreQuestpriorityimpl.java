package nc.bs.tg.pf.messagepriority;

import nc.vo.pub.BusinessException;

/**
 * FN01 �������뵥 ����id��ȡҵ�񵥾����ȼ�
 * 
 * @author ̸�ӽ�
 * @since 2020-02-19
 * @version NC6.5
 */
public class PayreQuestpriorityimpl extends MessagepriorityBase {

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 �� 5 ��ͨ -1��
		return Integer.parseInt(getPriority("tgfn_payrequest", "def9",
				"pk_payreq", billid));
	}

}
