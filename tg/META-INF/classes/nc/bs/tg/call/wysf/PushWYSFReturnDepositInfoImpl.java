package nc.bs.tg.call.wysf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

import com.alibaba.fastjson.JSONObject;

/**
 * 邻里-收费系统退押金付款结算单结算后回写信息给物业收费系统
 * 
 * 2020-09-25-谈子健
 * 
 */
public class PushWYSFReturnDepositInfoImpl extends TGImplCallHttpClient {
	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		/**
		 * 接口地址 测试：http://pms.linli580.com.cn/timesexpense/a/nc/approval
		 * 正式：http://times-pms.timesgroup.cn:8080/timesexpense/a/nc/approval
		 */
		CallImplInfoVO info = new CallImplInfoVO();
		try {
			Map<String, Object> map = (Map<String, Object>) value;
			if (map != null && map.size() > 0) {
				Map<String, Object> returnMap = setDate(map);

				String urls = OutsideUtils.getOutsideInfo("WYSF002");
				info.setClassName("nc.bs.tg.call.wysf.PushWYSFReturnDepositInfoImpl");
				info.setDessystem(dessystem);
				info.setUrls(urls);
				info.setIsrequiresnew("Y");
				info.setPostdata(JSONObject.toJSONString(returnMap));
				Map<String, Object> other = new HashMap<String, Object>();
				other.put("settid", map.get("pk_settlement"));
				info.setOther(other);

			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return info;
	}

	@Override
	protected String onBusinessProcessing(CallImplInfoVO info, String result) {
		return result;
	}

	@Override
	public String onCallMethod(CallImplInfoVO info) throws BusinessException {
		String result = null;
		try {
			// 创建url资源
			URL url = new URL(info.getUrls());
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			// 初始化配置信息
			initConnParameter(conn, info);
			// 开始连接请求
			conn.connect();
			onWriteInfo(conn, info.getPostdata());
			result = getResultInfo(conn, info);

			result = onBusinessProcessing(info, result);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	private Map<String, Object> setDate(Map<String, Object> data)
			throws BusinessException {
		/**
		 * NC系统中的共享用户初审 fssc_cs NC系统中的共享用户复审 fssc_es NC系统中的共享用户影像专岗 fssc_yx
		 */
		String billCode = "";// 单据号
		String sign = "";// 签名
		String time = data.get("settledate") == null ? "" : data.get(
				"settledate").toString();
		Map<String, Object> map = new HashMap<String, Object>();
		String wysfkey = OutsideUtils.getOutsideInfo("WYSFKEY");
		billCode = (String) data.get("srcbillno");
		sign = EncoderByMd5(billCode + wysfkey);
		map.put("billCode", billCode);
		map.put("billType", "4");// 单据类型，不能为空，4付款结算单
		map.put("sign", sign);
		map.put("approvalStatus", "NC单据结算成功");
		map.put("approvalDate", time);// 结算时间

		return map;

	}

	protected String getResultInfo(HttpURLConnection conn, CallImplInfoVO info)
			throws BusinessException {
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
				String flag = (String) result.getString("code");
				if (!flag.equals("0000")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("msg") + "】");
				} else {
					resultMsg = "【来自" + info.getDessystem() + "的信息：" + result
							+ "】";
					String sql = "update cmp_settlement set def1 = 'Y' where pk_settlement = '"
							+ info.getOther().get("settid") + "'";
					new BaseDAO().executeUpdate(sql);
				}
			} else {
				throw new BusinessException("连接失败");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}

	/**
	 * Md5加密
	 * 
	 * @param str
	 * @return
	 */
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

		return result;
	}
}
