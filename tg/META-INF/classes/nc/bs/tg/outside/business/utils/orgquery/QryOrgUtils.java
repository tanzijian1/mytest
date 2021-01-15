package nc.bs.tg.outside.business.utils.orgquery;

import java.util.HashMap;

import uap.json.JSONObject;
import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.vo.pub.BusinessException;

public class QryOrgUtils extends BusinessBillUtils{

	static QryOrgUtils utils;

	public static QryOrgUtils getUtils() {
		if (utils == null) {
			utils = new QryOrgUtils();
		}
		return utils;
	}

	public String getValue(JSONObject value, String dectype)
			throws BusinessException {
		return null;
	}

}
