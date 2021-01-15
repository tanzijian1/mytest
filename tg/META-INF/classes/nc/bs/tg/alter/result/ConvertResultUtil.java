package nc.bs.tg.alter.result;

import java.util.List;

import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.vo.pub.BusinessException;

public class ConvertResultUtil {

	TGAlertMessage remsg = null;
	static ConvertResultUtil util;

	public static ConvertResultUtil getUtil() {
		if (util == null) {
			util = new ConvertResultUtil();
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
			if (getRemsg() != null && getRemsg().getBodyValue() !=null) {
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

	public TGAlertMessage getRemsg() {
		return remsg;
	}

	public void setRemsg(TGAlertMessage remsg) {
		this.remsg = remsg;
	}

}
