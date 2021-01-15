package nc.bs.tg.call.reimbursement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSONObject;

import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.bs.tg.outside.utils.BillUtils;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

/**
 * BPM流程审批(结算完，共享审批完调用)接口 2020年9月21日09:44:18
 * 
 * @author ln
 * 
 */
public class PushSettledBillToBpmImpl extends TGImplCallHttpClient {
	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		String ip = TGOutsideUtils.getUtils().getOutsidInfo("BPM");
		Map<String, Object> map = (Map<String, Object>) value;
		CallImplInfoVO info = new CallImplInfoVO();
		info.setClassName("nc.bs.tg.call.reimbursement.PushSettledBillToBpmImpl");
		info.setDessystem(dessystem);
		String urls = ip
				+ "/YZSoft/WebService/TimesNeighborhood/TimesNeighborhood.ashx?Method=ProcessApprove&UserAccount="
				+ map.get("usercode") + "&IsPrivilege=true";
		info.setUrls(urls);
		Date date = new Date();
		String app_key = TGOutsideUtils.getUtils().getOutsidInfo("BPM_KEY");
		String app_key_ticket = TGOutsideUtils.getUtils().getOutsidInfo(
				"BPM_KEY_TICKET");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");// 年月日时分
		String time = formater.format(date);
		String tokenkey = app_key + app_key_ticket + time;
		String token = MD5Util.getMD5(tokenkey).toUpperCase();
		info.setToken(token);
		Map<String, Object> other = new HashMap<String, Object>();
		other.put("pk_settlement", (String) map.get("pk_settlement"));
		other.put("tradetype", (String) map.get("tradetype"));
		map.remove("usercode");
		map.remove("pk_settlement");
		map.remove("tradetype");
		info.setPostdata(JSONObject.toJSONString(map));
		info.setOther(other);
		if (map.get("pk_settlement") != null
				&& !"".equals(map.get("pk_settlement"))) {
			info.setIsrequiresnew("Y");
		}
		return info;
	}

	/**
	 * 设置参数配置
	 * 
	 * @param conn
	 * @param info
	 * @throws ProtocolException
	 * @throws IOException
	 */
	protected void initConnParameter(HttpURLConnection conn, CallImplInfoVO info)
			throws ProtocolException, IOException {
		super.initConnParameter(conn, info);
		conn.setRequestProperty("Authorization", info.getToken());
		conn.setRequestProperty("System", "NC");
	}

	@Override
	protected String getResultInfo(HttpURLConnection conn, CallImplInfoVO info)
			throws BusinessException {
		Map<String, Object> map = info.getOther();
		String pk_settlement = null;
		String tradetype = null;
		if (map != null && map.size() > 0) {
			pk_settlement = (String) map.get("pk_settlement");
			tradetype = (String) map.get("tradetype");
		}
		// TODO 自动生成的方法存根
		String resultMsg = null;
		try {
			// 请求返回的状态
			String msg = "";
			if (conn.getResponseCode() == 200) {
				// 请求返回的数据
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), getEncoding()));
				String line = null;
				while ((line = in.readLine()) != null) {
					msg += line;
				}
				org.json.JSONObject result = new org.json.JSONObject(msg);
				resultMsg = msg;
				String flag = (String) result.getString("Success");
				if (!flag.equals("true")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("Content") + "】");
				} else if (flag.equals("true")
						&& StringUtils.isNotBlank(pk_settlement)) {// 如果返回成功推送bpm信息并且nc的审批状态是已审批,则跟新传bpm状态
					String sql = null;
					if ("F3-Cxx-LL01".equals(tradetype)
							|| "F3-Cxx-LL03".equals(tradetype)
							|| "F3-Cxx-LL09".equals(tradetype)) {
						sql = "update cmp_settlement set def1 = 'Y' where pk_settlement = '"
								+ pk_settlement + "'";
					} else if (tradetype.startsWith("264X")
							|| tradetype.startsWith("261X")
							|| tradetype.startsWith("F3")) {
						sql = "update cmp_detail set def1 = 'Y' where pk_settlement = '"
								+ pk_settlement + "'";
					}
					BillUtils.getUtils().getBaseDAO().executeUpdate(sql);
				}
			} else {
				throw new BusinessException("连接失败");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}

	@Override
	protected String onBusinessProcessing(CallImplInfoVO info, String result) {
		// TODO 自动生成的方法存根
		return result;
	}

}
