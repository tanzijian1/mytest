package nc.bs.tg.outside.word.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.rbac.IFunctionPermissionPubService;
import nc.vo.org.OrgVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 
 * @author ASUS
 * 
 */
public class QueryUserPowerUtils extends WordBillUtils {
	static QueryUserPowerUtils utils;

	public static QueryUserPowerUtils getUtils() {
		if (utils == null) {
			utils = new QueryUserPowerUtils();
		}
		return utils;
	}

	@SuppressWarnings("unchecked")
	public String getValue(HashMap<String, Object> srcvalue)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		IFunctionPermissionPubService powerService = NCLocator.getInstance()
				.lookup(IFunctionPermissionPubService.class);
		if (srcvalue.get("data") == null) {
			throw new BusinessException("查询条件信息不可为空！");
		}

		JSONObject info = (JSONObject) srcvalue.get("data");
		String usercode = (String) info.get("psncode");
		List<String> orgids = (List<String>) info.get("orgids");
		String sql = "select sm_user.cuserid from sm_user where sm_user.user_code  = '"
				+ usercode + "' and sm_user.enablestate =2 ";
		Object value = getBaseDAO()
				.executeQuery(sql, new ColumnListProcessor());
		if (value == null) {
			throw new BusinessException("人员编码【" + usercode
					+ "】未关联用户档案或已被停用，请联系NC管理员！");
		}
		List<String> list = (List<String>) value;
		Set<String> set = new HashSet<String>();
		if (orgids != null && orgids.size() > 0) {
			sql = " SELECT PK_ACCOUNTINGBOOK FROM ORG_ACCOUNTINGBOOK WHERE DR = 0 AND  "
					+ SQLUtil.buildSqlForIn(" ORG_ACCOUNTINGBOOK.PK_RELORG",
							orgids.toArray(new String[0]));
			orgids = (List<String>) getBaseDAO().executeQuery(sql,
					new ColumnListProcessor());
		}
		for (String userid : list) {
			OrgVO[] orgVOs = powerService.getUserPermissionOrg(userid,
					"20020PREPA", "000112100000000005FD");
			if (orgVOs != null && orgVOs.length > 0) {
				for (OrgVO orgVO : orgVOs) {
					if (orgVO != null) {
						if (orgids != null && orgids.size() > 0) {
							if (orgids.contains(orgVO.getPrimaryKey())) {
								set.add(orgVO.getPrimaryKey());
							}
						} else {
							set.add(orgVO.getPrimaryKey());
						}
					}
				}
				sql = "select pk_financeorg from org_financeorg where dr = 0 "
						+ " and pk_financeorg IN(SELECT ORG_ACCOUNTINGBOOK.PK_RELORG FROM ORG_ACCOUNTINGBOOK WHERE DR = 0 AND  "
						+ SQLUtil.buildSqlForIn("PK_ACCOUNTINGBOOK",
								set.toArray(new String[0])) + ")";
				list = (List<String>) getBaseDAO().executeQuery(sql,
						new ColumnListProcessor());
				if (list != null && list.size() > 0) {
					HashMap<String, Object> infoMap = new HashMap<String, Object>();
					infoMap.put("orgids", list);
					return JSON.toJSONString(infoMap);
				}
			}
		}
		return null;
	}

}
