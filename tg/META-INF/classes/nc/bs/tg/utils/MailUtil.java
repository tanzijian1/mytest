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
			throw new BusinessException("未配置发送人邮箱信息");
		}
		Map<String, String> propMap = sendType.getPropMap();
		if (checkProp(propMap)) {
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("ncmessage", "EmailSending-0000")/* 电子邮件属性为空，请检查！ */);

		}

		String mailHost = propMap.get("mailHost");
		String port = propMap.get("mailPort");
		final String from = propMap.get("from");
		final String password = propMap.get("pwd");

		// 1.创建一个程序与邮件服务器会话对象 Session
		Properties props = new Properties();
		props.put("mail.transport.protocol ", "smtp");// 传输协议
		props.put("mail.smtp.host", mailHost);// 设置SMTP服务器
		props.put("mail.smtp.port", port);// 端口
		props.setProperty("mail.smtp.auth", "true");// 指定验证为true是否需要身份验证
		props.setProperty("mail.smtp.socketFactory.fallback", "true");
		props.put("mail.smtp.starttls.enable", "true");

		// 使用SSL，企业邮箱必需！
		// 创建验证器
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		// 2.创建一个Message，它相当于是邮件内容
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from)); // 设置发送者
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(
				toMail)); // 设置发送方式与接收者
		message.setSubject(subject);
		message.setContent(emailMsg, "text/html;charset=utf-8");

		// 3.创建 Transport用于将邮件发送

		Transport.send(message);
	}

	/**
	 * 检查配置信息
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
	 * <b>获得邮件发送类型对象</b>
	 */
	private NCMsgSendType getNCMsgSendTypeVO() {
		Logger.debug("开始获取邮件发送类型对象");/* -=notranslate=- */

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
