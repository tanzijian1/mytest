package nc.itf.tg.outside;

import nc.vo.pub.SuperVO;

public interface ISendEmailService {
	/**
	 * <b>发送普通文本邮件</b>
	 * 
	 * @param mailAddress
	 *            邮件接收人地址数组
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws Exception
	 */
	public void sendPlainTextEmail(String mailAddress, String subject,
			String content) throws Exception;

	public void upSettoppInfo(SuperVO[] vos, String[] filtAttrNames)
			throws nc.vo.pub.BusinessException;
}
