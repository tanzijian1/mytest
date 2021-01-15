package nc.bs.tg.call.apply;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

public class Read36ZJBillImpl extends TGImplCallHttpClient {

	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		String ip = TGOutsideUtils.getUtils().getOutsidInfo("BPM");
		Map<String, Object> map = (Map<String, Object>) value;
		CallImplInfoVO info = new CallImplInfoVO();
		info.setClassName("nc.bs.tg.call.apply.Read36ZJBillImpl");
		info.setDessystem(dessystem);
		String urls = ip
				+ "/YZSoft/WebService/YZService.ashx?Method=PostTask&UserAccount="
				+ map.get("account");
		info.setUrls(urls);
		String key = TGOutsideUtils.getUtils().getOutsidInfo("ZHSC02");// 密钥
		String token = DigestUtils.md5Hex(dessystem + key);
		info.setToken(token);
		String postdata = null;
		Map<String, Object> other = new HashMap<String, Object>();
		other.put("table", map.get("table"));
		other.put("pk_field", map.get("pk_field"));
		other.put("pk", map.get("pk"));
		map.remove("account");
		map.remove("table");
		map.remove("pk_field");
		map.remove("pk");
		info.setPostdata(JSONObject.toJSONString(map));

		info.setOther(other);

		return info;
	}

	@Override
	protected String getResultInfo(HttpURLConnection conn, CallImplInfoVO info)
			throws BusinessException {
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
				String flag = (String) result.getString("success");
				if (!flag.equals("true")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("errorMessage") + "】");
				} else {
					resultMsg = result.getString("TaskID");
					if (info != null) {
						String sql = "update " + info.getOther().get("table")
								+ " set vuserdef2 ='" + resultMsg + "' where "
								+ info.getOther().get("pk_field") + " = '"
								+ info.getOther().get("pk") + "'";
						new BaseDAO().executeUpdate(sql);
					}
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
		return null;
	}

}
