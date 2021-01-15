package nc.bs.tg.outside.ebs.pub;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.alter.plugin.ebs.Httpconnectionutil;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.NcToEbsLogVO;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PushEbsDataUtils extends EBSBillUtils {
	static PushEbsDataUtils utils;

	public static PushEbsDataUtils getUtils() {
		if (utils == null) {
			utils = new PushEbsDataUtils();
		}
		return utils;
	}

	public Map<String, String> pushBillToEBS(String code, String syscode,
			String token, Object postdata, String registryName)
			throws DAOException {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String flag = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(postdata);
			logVO.setTaskname(registryName);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			String address = "";
			//成本收款完成后回写信息给EBS接口判断-2020-06-30-谈子健	
			if ("response_nc_receipt_data".equals(code)) {
				 address = OutsideUtils.getOutsideInfo("EBS0107");
			} else {
				 address = OutsideUtils.getOutsideInfo("EBS0101");
			}
			String param = json;
			String key = OutsideUtils.getOutsideInfo("EBS-KEY");
			String tokenkey = json + key;
			token = EncoderByMd5(tokenkey);
			logVO.setNcparm("未加密token:" + tokenkey + "外系统EBS地址:" + address
					+ "加密后的token:" + token + "NC报文:" + param);
			String response = Httpconnectionutil.newinstance().connection(
					address + token, "&req=" + param);
			if (response != null) {
				JSONObject jsonobj = JSONObject.parseObject(response);
				String msg = "EBS回写信息: " + jsonobj.getString("msg");
				flag = (String) jsonobj.get("code");
				if ("S".equals(flag)) {
					Map<String, String> resultInfo = new HashMap<String, String>();
					resultInfo.put("msg", msg);
					resultInfo.put("code", flag);
					logVO.setResult(flag);
					logVO.setMsg(msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
					return resultInfo;
				} else {
					Map<String, String> resultInfo = new HashMap<String, String>();
					resultInfo.put("msg", msg);
					resultInfo.put("code", flag);
					logVO.setMsg(msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
					return resultInfo;
				}
			}
		} catch (Exception e) {
			logVO.setResult(flag);
			logVO.setMsg(e.getMessage());
			logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
		} finally {
			logVO.setDr(0);
			saveLog.SaveLog_RequiresNew(logVO);
		}
		return null;
	}

	public String EncoderByMd5(String str) {
		String result = "";
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");

			md5.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte b[] = md5.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		result = buf.toString();

		return result.toUpperCase();
	}
}
