package nc.bs.tg.alter.plugin.ll.message;

import java.util.List;

import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.vo.pub.BusinessException;

public class AlterResultUtil {

	AlertMessage remsg = null;
	static AlterResultUtil util;

	public static AlterResultUtil getUtil() {
		if (util == null) {
			util = new AlterResultUtil();
		}
		return util;
	}

	public PreAlertObject executeTask(String title, List<Object[]> list)
			throws BusinessException {
		try {
			getRemsg().setMessageTitle(title);
			getRemsg().setData(list);
			PreAlertObject pao = new PreAlertObject();
			pao.setMsgTitle(title);
			if (getRemsg() != null && getRemsg().getBodyValue() != null) {
				pao.setReturnType(PreAlertReturnType.RETURNFORMATMSG);
			} else {
				pao.setReturnType(PreAlertReturnType.RETURNNOTHING);
			}
			pao.setReturnObj(remsg);
			return pao;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	public PreAlertObject executeTask(String title, String msg)
			throws BusinessException {
		try {
			PreAlertObject pao = new PreAlertObject();
			pao.setMsgTitle(title);
			pao.setReturnType(PreAlertReturnType.RETURNMESSAGE);
			pao.setReturnObj(msg);
			return pao;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	public AlertMessage getRemsg() {
		return remsg;
	}

	public void setRemsg(AlertMessage remsg) {
		this.remsg = remsg;
	}

}
