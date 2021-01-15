package nc.bs.tg.call.receivable;

import hz.vo.fssc.ActionTypeEnum;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.itf.tg.outside.OutsideUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IplatFormEntry;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.CallImplInfoVO;

import com.alibaba.fastjson.JSONObject;

public class PushReceivableStateToWYSFImpl extends TGImplCallHttpClient {
	private IUAPQueryBS bs = null;

	protected IUAPQueryBS getIUAPQueryBS() {
		if (bs == null) {
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
	}

	/**
	 * ���̽�����
	 * 
	 * @return
	 */
	private IplatFormEntry pfBusiAction = null;

	public IplatFormEntry getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IplatFormEntry.class);
		}
		return pfBusiAction;
	}

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
				AggregatedValueObject obj = (AggregatedValueObject) map
						.get("aggVo");
				String pk_tradetype = (String) map.get("pk_tradetype");
				String approveAction = (String) map.get("approveAction");

				Map<String, Object> returnMap = setDate(obj, pk_tradetype,
						approveAction);

				String urls = OutsideUtils.getOutsideInfo("WYSF002");
				info.setClassName("nc.bs.tg.call.receivable.PushReceivableStateToWYSFImpl");
				info.setDessystem(dessystem);
				info.setUrls(urls);
				info.setIsrequiresnew("Y");
				info.setPostdata(JSONObject.toJSONString(returnMap));
				Map<String, Object> other = new HashMap<String, Object>();
				info.setOther(other);
				// �������״̬������̬�ľ��ǲ��ظ��Ƶ�����Ҫ����ɾ��
				Integer approvestatus = (Integer) obj.getParentVO()
						.getAttributeValue("approvestatus");
				if (null != approvestatus && -1 == approvestatus) {
					getPfBusiAction().processAction("DELETE", pk_tradetype,
							null, obj, null, null);
				}

			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return info;
	}

	@Override
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
				}
			} else {
				throw new BusinessException("����ʧ��");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}

	@Override
	protected String onBusinessProcessing(CallImplInfoVO info, String result) {
		return result;
	}

	private Map<String, Object> setDate(AggregatedValueObject obj,
			String pk_tradetype, String approveAction) throws BusinessException {
		/**
		 * NCϵͳ�еĹ����û����� fssc_cs NCϵͳ�еĹ����û����� fssc_es NCϵͳ�еĹ����û�Ӱ��ר�� fssc_yx
		 */
		String billCode = "";// ���ݺ�
		String sign = "";// ǩ��
		String time = new UFDateTime().toString();
		Map<String, Object> map = new HashMap<String, Object>();
		String fsscPost = getFsscPost(InvocationInfoProxy.getInstance()
				.getUserId());
		if ("fssc_yx".equals(fsscPost) || "fssc_cs".equals(fsscPost)
				|| "fssc_es".equals(fsscPost)) {
			String wysfkey = OutsideUtils.getOutsideInfo("WYSFKEY");
			billCode = (String) obj.getParentVO().getAttributeValue("def2");
			sign = EncoderByMd5(billCode + wysfkey);
			map.put("billCode", billCode);
			if (pk_tradetype.startsWith("F0")) {
				map.put("billType", "1");// �������ͣ�����Ϊ�գ�1Ӧ�յ�
			}
			if (pk_tradetype.startsWith("F1")) {
				map.put("billType", "2");// �������ͣ�����Ϊ�գ�2Ӧ����
			}
			if (pk_tradetype.startsWith("F2")) {
				map.put("billType", "3");// �������ͣ�����Ϊ�գ�3�տ
			}
			if (pk_tradetype.startsWith("F3")) {
				map.put("billType", "4");// �������ͣ�����Ϊ�գ�4���
			}
			if (pk_tradetype.startsWith("F5")) {
				map.put("billType", "4");// �������ͣ�����Ϊ�գ�4������㵥
			}

			map.put("sign", sign);
			// Ӱ������ͨ��
			if (approveAction.equals(ActionTypeEnum.APPROVE)) {
				if ("fssc_yx".equals(fsscPost)) {
					map.put("approvalStatus", "Ӱ������ͨ��");
				}

				if ("fssc_cs".equals(fsscPost)) {
					map.put("approvalStatus", "��������ͨ��");
				}

				if ("fssc_es".equals(fsscPost)) {
					map.put("approvalStatus", "��������ͨ��");
				}
				map.put("approvalDate", time);// ����ʱ��
			}
			// Ӱ�񲵻�
			if (approveAction.equals(ActionTypeEnum.APPROVEBACK)) {
				if ("fssc_yx".equals(fsscPost)) {
					map.put("approvalStatus", "Ӱ�񲵻�");
				}

				if ("fssc_cs".equals(fsscPost)) {
					map.put("approvalStatus", "���󲵻�");
				}

				if ("fssc_es".equals(fsscPost)) {
					map.put("approvalStatus", "���󲵻�");
				}
				map.put("approvalDate", time);// ����ʱ��
			}

		}
		return map;

	}

	/**
	 * ��ȡ�����λ
	 * 
	 * @throws BusinessException
	 */
	private String getFsscPost(String userid) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select u.user_code  ");
		query.append("  from sm_user u  ");
		query.append(" where u.cuserid = '" + userid + "'  ");
		query.append("   and u.enablestate = 2  ");
		query.append("   and u.dr = 0  ");

		String user_code = (String) getIUAPQueryBS().executeQuery(
				query.toString(), new ColumnProcessor());
		return user_code;

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

		return result;
	}
}
