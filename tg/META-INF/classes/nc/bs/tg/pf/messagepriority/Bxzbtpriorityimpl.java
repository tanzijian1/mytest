package nc.bs.tg.pf.messagepriority;

import nc.vo.pub.BusinessException;

/**
 * 264X ������
 * ����id��ȡҵ�񵥾����ȼ�
 * 
 * @author ̸�ӽ�
 * @since 2020-02-19
 * @version NC6.5
 */
public class Bxzbtpriorityimpl extends MessagepriorityBase {

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 �� 5 ��ͨ -1��
		return Integer.parseInt(getPriority("er_bxzb", "zyx46",
				"pk_jkbx", billid));
	}

	
}
