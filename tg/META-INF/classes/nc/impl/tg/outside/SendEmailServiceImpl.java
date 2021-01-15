package nc.impl.tg.outside;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.im.exception.BusinessException;
import nc.bs.tg.utils.MailUtil;
import nc.itf.tg.outside.ISendEmailService;
import nc.vo.pub.SuperVO;

public class SendEmailServiceImpl implements ISendEmailService {

	@Override
	public void sendPlainTextEmail(String mailAddress, String subject,
			String content) throws Exception {
		MailUtil.getMailUtil().sendMail(mailAddress, subject, content);
	}

	@Override
	public void upSettoppInfo(SuperVO[] vos, String[] filtAttrNames)
			throws nc.vo.pub.BusinessException {
		new BaseDAO().updateVOArray(vos, filtAttrNames);
	}
}
