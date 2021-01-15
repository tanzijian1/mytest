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
 * ����-�շ�ϵͳ��Ѻ�𸶿���㵥������д��Ϣ����ҵ�շ�ϵͳ
 * 
 * 2020-09-25-̸�ӽ�
 * 
 */
public class PushWYSFReturnDepositInfoImpl extends TGImplCallHttpClient {
	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		/**
		 * �ӿڵ�ַ ���ԣ�http://pms.linli580.com.cn/timesexpense/a/nc/approval
		 * ��ʽ��http://times-pms.timesgroup.cn:8080/timesexpense/a/nc/approval
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
			// ����url��Դ
			URL url = new URL(info.getUrls());
			// ����http����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			// ��ʼ��������Ϣ
			initConnParameter(conn, info);
			// ��ʼ��������
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
		 * NCϵͳ�еĹ����û����� fssc_cs NCϵͳ�еĹ����û����� fssc_es NCϵͳ�еĹ����û�Ӱ��ר�� fssc_yx
		 */
		String billCode = "";// ���ݺ�
		String sign = "";// ǩ��
		String time = data.get("settledate") == null ? "" : data.get(
				"settledate").toString();
		Map<String, Object> map = new HashMap<String, Object>();
		String wysfkey = OutsideUtils.getOutsideInfo("WYSFKEY");
		billCode = (String) data.get("srcbillno");
		sign = EncoderByMd5(billCode + wysfkey);
		map.put("billCode", billCode);
		map.put("billType", "4");// �������ͣ�����Ϊ�գ�4������㵥
		map.put("sign", sign);
		map.put("approvalStatus", "NC���ݽ���ɹ�");
		map.put("approvalDate", time);// ����ʱ��

		return map;

	}

	protected String getResultInfo(HttpURLConnection conn, CallImplInfoVO info)
			throws BusinessException {
		String resultMsg = null;
		try {
			// ���󷵻ص�״̬
			String msg = "";
			if (conn.getResponseCode() == 200) {
				// ���󷵻ص�����
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), getEncoding()));
				String line = null;
				while ((line = in.readLine()) != null) {
					msg += line;
				}
				org.json.JSONObject result = new org.json.JSONObject(msg);
				String flag = (String) result.getString("code");
				if (!flag.equals("0000")) {
					throw new BusinessException("������" + info.getDessystem()
							+ "�Ĵ�����Ϣ��" + result.getString("msg") + "��");
				} else {
					resultMsg = "������" + info.getDessystem() + "����Ϣ��" + result
							+ "��";
					String sql = "update cmp_settlement set def1 = 'Y' where pk_settlement = '"
							+ info.getOther().get("settid") + "'";
					new BaseDAO().executeUpdate(sql);
				}
			} else {
				throw new BusinessException("����ʧ��");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}

	/**
	 * Md5����
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
