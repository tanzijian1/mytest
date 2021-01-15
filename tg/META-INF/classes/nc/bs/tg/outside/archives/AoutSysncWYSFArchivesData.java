package nc.bs.tg.outside.archives;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.Httpconnectionutil;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.IInsertArchives;
import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OSImplLogVO;
import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 时代邻里获取物业收费系统业务类型和业务细类档案-2020-08-18
 * 
 * @author 谈子健
 * 
 */
public class AoutSysncWYSFArchivesData extends AoutSysncWYSFData {
	public static final String WYSFDefaultOperator = "LLWYSF";// 物业收费系统制单人
	private IInsertArchives insertArchives = null;

	public IInsertArchives getInsertArchives() {
		if (insertArchives == null) {
			insertArchives = NCLocator.getInstance().lookup(
					IInsertArchives.class);
		}
		return insertArchives;
	}

	/**
	 * 合同对应的code SDLL003 业务类型 SDLL004 业务细类 SDLL005 城市公司
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		OSImplLogVO logVO = new OSImplLogVO();

		try {
			String syscode = OutsideUtils.getOutsideInfo("WYSF-SYSTEM").trim();
			String key = OutsideUtils.getOutsideInfo("WYSF-KEY").trim();
			String token = "";
			String tokenkey = "";
			if ("nc".equals(syscode)) {
				Date date = new Date();
				SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
				String time = formater.format(date);
				tokenkey = time + key;
				token = MD5Util.getMD5(tokenkey).toUpperCase();
			}

			String code = OutsideUtils.getOutsideInfo("WYSF001").trim();
			String params = "code=" + code + "&syscode=" + syscode + "&token="
					+ token;
			String address = OutsideUtils.getOutsideInfo("WYSF-URL").trim();
			String urls = address + "/method?" + params;
			String returnjson = Httpconnectionutil.newinstance().connection(
					urls, null);
			HashMap<String, Object> info = JSON.parseObject(returnjson,
					HashMap.class);

			JSONArray jsonData = (JSONArray) info.get("data");// 表单数据
			JSONObject data = null;
			if ("S".equals(info.get("code"))) {
				if (jsonData != null && jsonData.size() > 0) {
					try {
						for (int row = 0; row < jsonData.size(); row++) {
							data = jsonData.getJSONObject(row);
							getInsertArchives()
									.insertArchives_RequiresNew(data);
						}
					} catch (Exception e) {
						logVO.setSrcparm(data.toJSONString());
						logVO.setExedate(new UFDateTime().toString());
						logVO.setSrcsystem(WYSFDefaultOperator);
						logVO.setDessystem("NC");
						logVO.setMethod(code);
						logVO.setDr(new Integer(0));
						logVO.setMsg("同步档案失败:" + "NC档案类型编码"
								+ data.getString("nccode") + "物业收费系统大类编码:"
								+ data.getString("type") + "物业收费系统小类编码: "
								+ data.getString("value") + "  name :"
								+ data.getString("label") + "报错信息:"
								+ e.getMessage() + "错误信息堆栈信息:" + e);
						logVO.setResult(BillUtils.STATUS_FAILED);
						BillUtils.getUtils().getBaseDAO().insertVO(logVO);
					}
				}

			} else {
				logVO.setSrcparm(returnjson);
				logVO.setExedate(new UFDateTime().toString());
				logVO.setSrcsystem(WYSFDefaultOperator);
				logVO.setDessystem("NC");
				logVO.setMethod(code);
				logVO.setDr(new Integer(0));
				logVO.setResult(BillUtils.STATUS_FAILED);
				logVO.setMsg(info.get("msg").toString() + ",加密前token:"
						+ tokenkey + ",加密后token:" + token + ",URl地址:" + urls);
				BillUtils.getUtils().getBaseDAO().insertVO(logVO);
			}
		} catch (Exception e) {
			logVO.setResult(BillUtils.STATUS_FAILED);
			logVO.setMsg(e.getMessage());
			BillUtils.getUtils().getBaseDAO().insertVO(logVO);
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
}
