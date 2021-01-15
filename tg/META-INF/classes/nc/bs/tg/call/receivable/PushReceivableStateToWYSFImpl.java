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
	 * 流程交互类
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
		 * 接口地址 测试：http://pms.linli580.com.cn/timesexpense/a/nc/approval
		 * 正式：http://times-pms.timesgroup.cn:8080/timesexpense/a/nc/approval
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
				// 如果单据状态是自由态的就是驳回给制单人需要单据删除
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
		return result;
	}

	private Map<String, Object> setDate(AggregatedValueObject obj,
			String pk_tradetype, String approveAction) throws BusinessException {
		/**
		 * NC系统中的共享用户初审 fssc_cs NC系统中的共享用户复审 fssc_es NC系统中的共享用户影像专岗 fssc_yx
		 */
		String billCode = "";// 单据号
		String sign = "";// 签名
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
				map.put("billType", "1");// 单据类型，不能为空，1应收单
			}
			if (pk_tradetype.startsWith("F1")) {
				map.put("billType", "2");// 单据类型，不能为空，2应付单
			}
			if (pk_tradetype.startsWith("F2")) {
				map.put("billType", "3");// 单据类型，不能为空，3收款单
			}
			if (pk_tradetype.startsWith("F3")) {
				map.put("billType", "4");// 单据类型，不能为空，4付款单
			}
			if (pk_tradetype.startsWith("F5")) {
				map.put("billType", "4");// 单据类型，不能为空，4付款结算单
			}

			map.put("sign", sign);
			// 影像审批通过
			if (approveAction.equals(ActionTypeEnum.APPROVE)) {
				if ("fssc_yx".equals(fsscPost)) {
					map.put("approvalStatus", "影像审批通过");
				}

				if ("fssc_cs".equals(fsscPost)) {
					map.put("approvalStatus", "初审审批通过");
				}

				if ("fssc_es".equals(fsscPost)) {
					map.put("approvalStatus", "复审审批通过");
				}
				map.put("approvalDate", time);// 审批时间
			}
			// 影像驳回
			if (approveAction.equals(ActionTypeEnum.APPROVEBACK)) {
				if ("fssc_yx".equals(fsscPost)) {
					map.put("approvalStatus", "影像驳回");
				}

				if ("fssc_cs".equals(fsscPost)) {
					map.put("approvalStatus", "初审驳回");
				}

				if ("fssc_es".equals(fsscPost)) {
					map.put("approvalStatus", "复审驳回");
				}
				map.put("approvalDate", time);// 驳回时间
			}

		}
		return map;

	}

	/**
	 * 获取共享岗位
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
