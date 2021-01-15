package nc.itf.tg.outside;

import nc.vo.pub.SuperVO;

public interface ISendEmailService {
	/**
	 * <b>������ͨ�ı��ʼ�</b>
	 * 
	 * @param mailAddress
	 *            �ʼ������˵�ַ����
	 * @param subject
	 *            �ʼ�����
	 * @param content
	 *            �ʼ�����
	 * @throws Exception
	 */
	public void sendPlainTextEmail(String mailAddress, String subject,
			String content) throws Exception;

	public void upSettoppInfo(SuperVO[] vos, String[] filtAttrNames)
			throws nc.vo.pub.BusinessException;
}
