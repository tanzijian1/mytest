package nc.bs.tg.pf.messagepriority;

import nc.vo.pub.BusinessException;

/**
 * 36S4 ���˽���
 * ����id��ȡҵ�񵥾����ȼ�
 * 
 * @author ̸�ӽ�
 * @since 2020-05-04
 * @version NC6.5
 */
public class TransformBillimpl extends MessagepriorityBase {

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 �� 5 ��ͨ -1��
		return Integer.parseInt(getPriority("cmp_transformbill", "vdef2",
				"pk_transformbill", billid));
	}

	
}
