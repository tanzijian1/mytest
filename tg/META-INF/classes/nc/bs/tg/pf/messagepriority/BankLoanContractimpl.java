package nc.bs.tg.pf.messagepriority;

import nc.vo.pub.BusinessException;

/**
 * 36FF ���д����ͬ���� ����id��ȡҵ�񵥾����ȼ�
 * 
 * @author ̸�ӽ�
 * @since 2020-03-02
 * @version NC6.5
 */
public class BankLoanContractimpl extends MessagepriorityBase {

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		// 10 �� 5 ��ͨ -1��
		// return Integer.parseInt(getPriority("cdm_repayrcpt", "def32",
		// "pk_repayrcpt", billid));
		return 10;
	}

}
