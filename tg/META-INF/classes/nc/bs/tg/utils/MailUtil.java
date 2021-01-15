package nc.bs.tg.utils;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.message.sendtype.vo.MsgSendTypeCache;
import nc.message.sendtype.vo.NCMsgSendType;
import nc.vo.pub.BusinessException;

import org.springframework.util.CollectionUtils;

/**
 * @ClassName MailUtil
 * @Description TODO
 * @Author zxw
 * @Date 2020/8/25 16:31
 * @Version 1.0
 **/
public class MailUtil {
	static MailUtil mailUtil = null;

	public static MailUtil getMailUtil() {
		if (mailUtil == null) {
			mailUtil = new MailUtil();
		}
		return mailUtil;
	}

	public void sendMail(String toMail, String subject, String emailMsg)
			throws AddressException, MessagingException, BusinessException {

		NCMsgSendType sendType = getNCMsgSendTypeVO();
		if (sendType == null) {
			throw new BusinessException("δ���÷�����������Ϣ");
		}
		Map<String, String> propMap = sendType.getPropMap();
		if (checkProp(propMap)) {
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("ncmessage", "EmailSending-0000")/* �����ʼ�����Ϊ�գ����飡 */);

		}

		String mailHost = propMap.get("mailHost");
		String port = propMap.get("mailPort");
		final String from = propMap.get("from");
		final String password = propMap.get("pwd");

		// 1.����һ���������ʼ��������Ự���� Session
		Properties props = new Properties();
		props.put("mail.transport.protocol ", "smtp");// ����Э��
		props.put("mail.smtp.host", mailHost);// ����SMTP������
		props.put("mail.smtp.port", port);// �˿�
		props.setProperty("mail.smtp.auth", "true");// ָ����֤Ϊtrue�Ƿ���Ҫ�����֤
		props.setProperty("mail.smtp.socketFactory.fallback", "true");
		props.put("mail.smtp.starttls.enable", "true");

		// ʹ��SSL����ҵ������裡
		// ������֤��
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		// ����Session��debugģʽ�������Ϳ��Բ鿴��������Email������״̬
		// 2.����һ��Message�����൱�����ʼ�����
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from)); // ���÷�����
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(
				toMail)); // ���÷��ͷ�ʽ�������
		message.setSubject(subject);
		message.setContent(emailMsg, "text/html;charset=utf-8");

		// 3.���� Transport���ڽ��ʼ�����

		Transport.send(message);
	}

	/**
	 * ���������Ϣ
	 * 
	 * @param propMap
	 * @return
	 */
	private boolean checkProp(Map<String, String> propMap) {
		boolean isNull = true;
		if (propMap != null && propMap.size() != 0) {
			if ((propMap.get("from") != null && !propMap.get("from").equals(""))
					&& (propMap.get("rcvhost") != null && !propMap.get(
							"rcvhost").equals(""))
					&& (propMap.get("mailHost") != null && !propMap.get(
							"mailHost").equals(""))
					&& (propMap.get("pwd") != null && !propMap.get("pwd")
							.equals(""))
					&& (propMap.get("user") != null && !propMap.get("user")
							.equals(""))) {
				isNull = false;
			}
		}
		return isNull;
	}

	/**
	 * <b>����ʼ��������Ͷ���</b>
	 */
	private NCMsgSendType getNCMsgSendTypeVO() {
		Logger.debug("��ʼ��ȡ�ʼ��������Ͷ���");/* -=notranslate=- */

		List<NCMsgSendType> sendTypeList = MsgSendTypeCache.getInstance()
				.getSendTypeByCode("email");

		if (!CollectionUtils.isEmpty(sendTypeList)) {
			for (NCMsgSendType sendType : sendTypeList) {
				if ("email".equals(sendType.getTypeid()))
					return sendType;
			}
		}

		return null;
	}
}
