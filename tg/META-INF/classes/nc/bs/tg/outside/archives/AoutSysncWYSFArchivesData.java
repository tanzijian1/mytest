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
 * ʱ�������ȡ��ҵ�շ�ϵͳҵ�����ͺ�ҵ��ϸ�൵��-2020-08-18
 * 
 * @author ̸�ӽ�
 * 
 */
public class AoutSysncWYSFArchivesData extends AoutSysncWYSFData {
	public static final String WYSFDefaultOperator = "LLWYSF";// ��ҵ�շ�ϵͳ�Ƶ���
	private IInsertArchives insertArchives = null;

	public IInsertArchives getInsertArchives() {
		if (insertArchives == null) {
			insertArchives = NCLocator.getInstance().lookup(
					IInsertArchives.class);
		}
		return insertArchives;
	}

	/**
	 * ��ͬ��Ӧ��code SDLL003 ҵ������ SDLL004 ҵ��ϸ�� SDLL005 ���й�˾
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
				SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// ������ʱ��
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

			JSONArray jsonData = (JSONArray) info.get("data");// ������
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
						logVO.setMsg("ͬ������ʧ��:" + "NC�������ͱ���"
								+ data.getString("nccode") + "��ҵ�շ�ϵͳ�������:"
								+ data.getString("type") + "��ҵ�շ�ϵͳС�����: "
								+ data.getString("value") + "  name :"
								+ data.getString("label") + "������Ϣ:"
								+ e.getMessage() + "������Ϣ��ջ��Ϣ:" + e);
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
				logVO.setMsg(info.get("msg").toString() + ",����ǰtoken:"
						+ tokenkey + ",���ܺ�token:" + token + ",URl��ַ:" + urls);
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
