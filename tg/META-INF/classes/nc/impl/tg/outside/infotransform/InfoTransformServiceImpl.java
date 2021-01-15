package nc.impl.tg.outside.infotransform;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.alter.plugin.ebs.Httpconnectionutil;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.infotransform.IInfoTransformService;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.ISyncEBSServcie;
import nc.itf.tg.outside.OutsideUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.hzvat.taxout.log.NcRewriteOutSysInterfaceLog;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.NcToEbsLogVO;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.paymentrequest.Business_b;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InfoTransformServiceImpl implements IInfoTransformService {

	IPFBusiAction pfBusiAction = null;

	private BaseDAO dao = null;

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	ISyncEBSServcie ebsService = null;

	@Override
	public List<Object[]> pushEbsCostPayBillPaid(PayBillItemVO[] payBillItemVOs)
			throws BusinessException {

		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = getEbsCostPayBillPaidByPayBill(payBillItemVOs);
		String code = "response_nc_payment_data";
		// String syscode = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("EBSSYSTEM");
		String syscode = OutsideUtils.getOutsideInfo("EBS-SYSTEM");
		// String key = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("KEY");
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		if (settMap == null || settMap.size() == 0) {

			for (Map<String, Object> info : settMap) {
				String[] msgObj = new String[2];
				msgObj[0] = (String) info.get("billcode");
				try {
					String settid = (String) info.get("pk_settlement");
					Map<String, Object> postdata = new HashMap<String, Object>();
					postdata.put("pay_method_code", info.get("pay_method_code"));// ���ʽ
					postdata.put("currency", info.get("currency"));// ����
					postdata.put(
							"pay_amount",
							info.get("pay_amount") == null ? null : info.get(
									"pay_amount").toString());// ������
					postdata.put("apply_number", info.get("apply_number"));// ebs�������뵥��
					postdata.put("nc_payment_id", info.get("nc_payment_id"));// ebs��������id
					List<UFDouble> mnylist = (List<UFDouble>) info
							.get("line_listData");
					List<Map<String, String>> bodylist = new ArrayList<Map<String, String>>();
					HashMap<String, String> bodyMap = new HashMap<String, String>();
					bodyMap.put(
							"pay_amount",
							info.get("pay_amount") == null ? null : info.get(
									"pay_amount").toString());// ������
					bodyMap.put("fq_amoun", "0");// �������()
					bodyMap.put("fq_flag", "N");// ������ʶ
					bodyMap.put("fq_date", "");// ��������
					bodylist.add(bodyMap);

					postdata.put("line_listData", bodylist);

					String token = "";
					if ("nc".equals(syscode)) {
						Date date = new Date();
						SimpleDateFormat formater = new SimpleDateFormat(
								"yyyyMMddHHmm");// ������ʱ��
						String time = formater.format(date);
						String tokenkey = time + key;
						token = MD5Util.getMD5(tokenkey).toUpperCase();

					}
					Map<String, String> refMap = getEBSServcie()
							.onPushEBSPayData_RequiresNew(settid, code,
									syscode, token, postdata, null, null);
					msgObj[1] = refMap.get("msg");
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[2] = e.getMessage();
				}
				msglist.add(msgObj);
			}
		}

		return msglist;
	}

	/**
	 * ���������ѯ
	 * 
	 * @param payBillItemVOs
	 * @return
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> getEbsCostPayBillPaidByPayBill(
			PayBillItemVO[] payBillItemVOs) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.pk_settlement")
				.append(",t.billcode")
				.append(",bd_balatype.name pay_method_code")
				.append(",bd_currtype.code currency")
				.append(",p.def1 nc_payment_id")
				.append(",p.def2 apply_number")
				.append(",sum(d.pay) pay_amount")
				.append(" from cmp_settlement t")
				.append(" inner join cmp_detail d on d.pk_settlement = t.pk_settlement ")
				.append(" inner join ap_paybill p on p.pk_paybill = t.pk_busibill")
				.append(" inner join bd_balatype on bd_balatype.pk_balatype = d.pk_balatype ")
				.append(" inner join bd_currtype on bd_currtype.pk_currtype = d.pk_currtype ")
				.append(" where t.dr= 0 and d.dr=0 and bd_balatype.dr =0 and bd_currtype.dr = 0 and t.def1 <> 'Y' ")
				.append(" and t.settlestatus =5 and t.pk_billtype = 'F3' and t.pk_tradetype in('F3-Cxx-004') ")
				.append(" group by t.pk_settlement,t.billcode ")
				.append(" ,t.billcode ").append(" ,bd_balatype.name ")
				.append(" ,bd_currtype.code ").append(" ,p.def1 ")
				.append(" ,p.def2 ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;

	}

	public ISyncEBSServcie getEBSServcie() {
		if (ebsService == null) {
			ebsService = NCLocator.getInstance().lookup(ISyncEBSServcie.class);
		}
		return ebsService;
	}

	@Override
	public String pushEbsCostPayRequestPaid(AggPayrequest aggvo)
			throws BusinessException {
		// �������뵥(�ɱ�)����
		BaseDAO dao = new BaseDAO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fq_date = sdf.format(new Date());
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();// �������뵥����
		// ���㷽ʽ
		String pk_balatype = aggvo.getParentVO().getPk_balatype();
		String pay_method_code = (String) dao.executeQuery(
				"select code from bd_balatype where nvl(dr,0)=0 and pk_balatype ='"
						+ pk_balatype + "'", new ColumnProcessor());
		payapplymap_temp.put("pay_method_code", pay_method_code);// ���ʽ
		// payapplymap_temp.put("p_Ncrecpnumber", aggvo.getParentVO()
		// .getAttributeValue("billno"));// ���ݺ�
		// payapplymap_temp.put("p_Paybillid", aggvo.getParentVO()
		// .getAttributeValue("def1"));// ebs����
		// ����
		String pk_currtype = aggvo.getParentVO().getPk_currtype();
		String currency = (String) dao.executeQuery(
				"select code from bd_currtype where nvl(dr,0)=0 and pk_currtype ='"
						+ pk_currtype + "'", new ColumnProcessor());
		payapplymap_temp.put("currency", currency);// ����
		payapplymap_temp.put("pay_amount", "");// ������
		payapplymap_temp.put("apply_number", aggvo.getParentVO().getDef2());// ebs�������뵥��
		payapplymap_temp.put("nc_payment_id", aggvo.getPrimaryKey());// ebs��������id
		payapplymap_temp.put("pay_date", fq_date);
		List<Map<String, Object>> bodylist = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> bodyMap = new HashMap<String, Object>();
		bodyMap.put("pay_amount", "");// ������
		bodyMap.put("fq_amount", aggvo.getParentVO().getDef50());// �������()
		bodyMap.put("fq_flag", "Y");// ������ʶ
		bodyMap.put("fq_date", fq_date);// ��������
		/**
		 * ��Ӷ������Ϣ����2020-04-01-̸�ӽ�-start
		 */

		List<Map<String, String>> details_listData = new ArrayList<Map<String, String>>();
		if (aggvo.getPrimaryKey() != null && !"".equals(aggvo.getPrimaryKey())) {
			List<Map<String, String>> billItem = getBillItem(aggvo
					.getPrimaryKey());

			if (billItem != null && billItem.size() > 0) {
				for (Map<String, String> map : billItem) {
					details_listData.add(map);
				}
				// String data = details_listData.toString();
				bodyMap.put("details_listData", details_listData);
			}
		}
		/**
		 * ��Ӷ������Ϣ����2020-04-01-̸�ӽ�-end
		 */
		bodylist.add(bodyMap);

		payapplymap_temp.put("line_listData", bodylist);

		// ��������

		// String returnjson = Httpconnectionutil.newinstance().connection(url,
		// "&req=" + getjson(payapplymap_temp));
		// String syscode = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("EBSSYSTEM");
		String syscode = OutsideUtils.getOutsideInfo("EBS-SYSTEM");
		// String key = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("KEY");
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		String token = "";
		if ("nc".equals(syscode)) {
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// ������ʱ��
			String time = formater.format(date);
			String tokenkey = time + key;
			token = MD5Util.getMD5(tokenkey).toUpperCase();
		}
		String code = "response_nc_payment_data";
		String params = "code=" + code + "&syscode=" + syscode + "&token="
				+ token;
		// String address = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("EBSURL");
		String address = OutsideUtils.getOutsideInfo("EBS-URL");
		String urls = address + "/postMethod?" + params;
		String data = getjson(payapplymap_temp);
		String returnjson = Httpconnectionutil.newinstance().connection(urls,
				"&req=" + data);
		// ��¼��־nctoebslog
		if (returnjson != null) {
			JSONObject jobj = JSON.parseObject(returnjson);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("�������뵥(�ɱ�)����");
			logVO.setNcparm(getjson(payapplymap_temp));
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg(msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				IVOPersistence ip = NCLocator.getInstance().lookup(
						IVOPersistence.class);
				aggvo.getParentVO().setDef29("Y");
				ip.updateVO(aggvo.getParentVO());
			} else {
				logVO.setMsg(msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			}
			logVO.setDr(0);
			saveLog.SaveLog_RequiresNew(logVO);
		}

		return returnjson;
	}

	/**
	 * SRM�������뵥���Ϸ�дEbs
	 * 
	 * @author Huangxj
	 */
	@Override
	public String pushSrmPayRequestPaid(AggPayrequest aggvo) throws Exception {
		// �������뵥
		BaseDAO dao = new BaseDAO();
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();

		// ��������
		String paymentType = (String) aggvo.getChildrenVO()[0]
				.getAttributeValue("def2");
		String type = null;
		if ("Ԥ����".equals(paymentType)) {
			type = "YPAY";
		} else if ("Ͷ�걣֤��".equals(paymentType)) {
			type = "Y_REFUND";
		} else {
			type = "BZPAY";
		} /*
		 * else if ("Ԥ�����˿�".equals(paymentType)) { type = "Y_REFUND"; }
		 */

		payapplymap_temp.put("pay_method_code", type);// ��������

		payapplymap_temp.put("command", "����");// �������
		payapplymap_temp.put("operationType", "Cancel");// NC�������̲�������
		payapplymap_temp.put("status", "true");// ����״̬
		payapplymap_temp.put("taskID", "");// BPM����id
		payapplymap_temp.put("pId", aggvo.getParentVO().getDef1());// ���������Ԥ��������id

		//
		String address = OutsideUtils.getOutsideInfo("EBS0301");
		// String urls = address + "/postMethod?" + params;
		String data = getjson(payapplymap_temp);

		String receive = null;
		// ����post��ʽ�������
		PostMethod method = new PostMethod(address);
		try {

			// ����httpclient����
			HttpClient client = new HttpClient();

			// ���ò��������������
			method.setRequestHeader("Content-type",
					"application/json; charset=UTF-8");
			method.setRequestHeader("Accept", "application/json; charset=UTF-8");
			method.setRequestBody("&params=" + data);
			int rspCode = client.executeMethod(method);
			receive = method.getResponseBodyAsString();

			if (200 == rspCode) {
				JSONObject jobj = JSON.parseObject(receive);
				String flag = jobj.getString("code");
				String msg = jobj.getString("msg");
				logVO.setTaskname("�������뵥(SRM)����");
				logVO.setNcparm(data);
				logVO.setSrcsystem("SRM");
				logVO.setOperator("NC");
				logVO.setExedate(new UFDateTime().toString());
				logVO.setDr(0);

				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg(msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				} else {
					throw new BusinessException("EBS�����쳣:" + msg);
				}
			}
		} catch (Exception e) {
			logVO.setMsg(e.getMessage());
			logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			method.releaseConnection();

			saveLog.SaveLog_RequiresNew(logVO);

		}

		return receive;
	}

	/**
	 * �������뵥������ѯ
	 * 
	 * @param payBillItemVOs
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getEbsCostPayBillPaidByPayRequest(
			PayBillItemVO[] payBillItemVOs) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.pk_settlement")
				.append(",t.billcode")
				.append(",bd_balatype.name pay_method_code")
				.append(",bd_currtype.code currency")
				.append(",p.def1 nc_payment_id")
				.append(",p.def2 apply_number")
				.append(",sum(d.pay) pay_amount")
				.append(" from cmp_settlement t")
				.append(" inner join cmp_detail d on d.pk_settlement = t.pk_settlement ")
				.append(" inner join ap_paybill p on p.pk_paybill = t.pk_busibill")
				.append(" inner join bd_balatype on bd_balatype.pk_balatype = d.pk_balatype ")
				.append(" inner join bd_currtype on bd_currtype.pk_currtype = d.pk_currtype ")
				.append(" where t.dr= 0 and d.dr=0 and bd_balatype.dr =0 and bd_currtype.dr = 0 and t.def1 <> 'Y' ")
				.append(" and t.settlestatus =5 and t.pk_billtype = 'F3' and t.pk_tradetype in('F3-Cxx-004') ")
				.append(" group by t.pk_settlement,t.billcode ")
				.append(" ,t.billcode ").append(" ,bd_balatype.name ")
				.append(" ,bd_currtype.code ").append(" ,p.def1 ")
				.append(" ,p.def2 ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}

	@Override
	public String pushEbsgeneralPayrquest(AggPayrequest aggvo) throws Exception {
		// �������뵥(ͨ��)����
		BaseDAO dao = new BaseDAO();
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String token = "";
		String tokenkey = "";
		String url = OutsideUtils.getOutsideInfo("EBS0206");
		HashMap<String, Object> payapplymap = new HashMap<String, Object>();// �������뵥����
		List<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
		for (nc.vo.pub.CircularlyAccessibleValueObject vo : aggvo
				.getChildrenVO()) {
			Business_b bvo = (Business_b) vo;
			String def46 = bvo.getDef46();
			UFDouble def46ufDouble = new UFDouble(def46);
			if (def46 != null && !(def46ufDouble.isTrimZero())) {
				HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();
				payapplymap_temp.put("p_Ispaid", "Y");// �Ƿ���
				/**
				 * ʵ���տӦ�̺�ʵ���տ������˻���Ϊcode����ebs-̸�ӽ�-2020-03-31-start
				 */
				Object supplier = dao.executeQuery(
						"select code from bd_supplier where pk_supplier= '"
								+ bvo.getSupplier() + "'",
						new ColumnProcessor());
				// add by huangxj 2020��8��10��
				payapplymap_temp.put("p_Payeetype", bvo.getObjtype());// ��������
				// end
				if (bvo.getObjtype() == 3) {
					Object psn = dao.executeQuery(
							"select code from bd_psndoc where pk_psndoc=  '"
									+ bvo.getDef58() + "'",
							new ColumnProcessor());
					payapplymap_temp.put("p_Actreceiver", psn);// ��Ա
				} else {
					payapplymap_temp.put("p_Actreceiver", supplier);// ʵ���տӦ��
				}
				Object accnum = dao.executeQuery(
						"select bd_bankaccsub.accnum from bd_bankaccsub where pk_bankaccsub =  '"
								+ bvo.getDef15() + "'", new ColumnProcessor());
				payapplymap_temp.put("p_Actrevaccount", accnum);// ʵ���տ������˻�
				/**
				 * ʵ���տ��ʵ���տ������˻���Ϊcode����ebs-̸�ӽ�-2020-03-31-end
				 */
				/**
				 * ��Ӹ������2020-04-01-̸�ӽ�-start
				 */
				payapplymap_temp.put("p_Nopaidamount", def46ufDouble);// �������
				/**
				 * ��Ӹ������2020-04-01-̸�ӽ�-end
				 */
				payapplymap_temp.put("p_Actpaymoney", 0);// ʵ�ʸ�����
				/**
				 * ��Ӹ������2020-04-01-̸�ӽ�-end
				 */

				if (aggvo.getParentVO().getAttributeValue("billdate") != null) {
					UFDate uf = (UFDate) aggvo.getParentVO().getAttributeValue(
							"billdate");// ��������
					String s = uf.getYear() + "-" + uf.getMonth() + "-"
							+ uf.getDay();
					payapplymap_temp.put("p_Actpaydate", s);// ��������
				}
				lists.add(payapplymap_temp);
			}
		}
		payapplymap.put("p_Contractid",
				Integer.valueOf(aggvo.getParentVO().getDef11()));// ��ͬid
		payapplymap.put(
				"p_Paybillid",
				Integer.valueOf(aggvo.getParentVO().getAttributeValue("def1")
						+ ""));// ebs����
		payapplymap.put("p_Ncrecpnumber", aggvo.getParentVO()
				.getAttributeValue("billno"));// ���ݺ�
		payapplymap.put("Actualreceiver", lists);
		// ���Ψһ��ʶ����ʱ��Ϊû�н��㵥���Ǿ�ʹ�ø������뵥��pk��Ψһ��ʶ
		payapplymap.put("p_Pay_Key", aggvo.getPrimaryKey().toString());// ���㵥����
		// ��������
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		String param = getjson(payapplymap);
		tokenkey = param + key;
		token = EncoderByMd5(tokenkey);
		String returnjson = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// ��¼��־nctoebslog
		if (returnjson != null) {
			JSONObject jobj = JSON.parseObject(returnjson);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("�������뵥(ͨ��)����");
			logVO.setNcparm(getjson(payapplymap));
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg(msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				IVOPersistence ip = NCLocator.getInstance().lookup(
						IVOPersistence.class);
				aggvo.getParentVO().setDef29("Y");
				ip.updateVO(aggvo.getParentVO());
			} else {
				logVO.setMsg(msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			}
			logVO.setDr(0);
			saveLog.SaveLog_RequiresNew(logVO);
		}

		return returnjson;
	}

	/*
	 * mapתjson
	 */
	private String getjson(HashMap<String, Object> map) {
		// String json = JSON.toJSONString(map);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try {
			json = objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			Logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public String pushEbsgeneralPay(nc.vo.arap.pay.AggPayBillVO aggvo_pay)
			throws Exception {
		// TODO �Զ����ɵķ������
		// String url = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("PAYURL");// ebs������ɽӿڵ�ַ
		String url = OutsideUtils.getOutsideInfo("EBS0206");
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		List<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> paymap = new HashMap<String, Object>();// ���map
		for (nc.vo.pub.CircularlyAccessibleValueObject cvo : aggvo_pay
				.getChildrenVO()) {
			paymap = paydata(aggvo_pay, cvo);
			if (paymap != null)
				lists.add(paymap);
		}
		datamap.put("p_Contractid",
				aggvo_pay.getParentVO().getAttributeValue("def5"));// ��ͬ����
		datamap.put("p_Paybillid",
				aggvo_pay.getParentVO().getAttributeValue("def1"));// ebs����
		datamap.put("p_Ncrecpnumber", aggvo_pay.getParentVO()
				.getAttributeValue("billno"));// ���ݺ�
		String param = JSON.toJSONString(datamap);
		String jsondata = Httpconnectionutil.newinstance().connection(url,
				param);
		return jsondata;
	}

	/*
	 * �������
	 */
	private HashMap<String, Object> paydata(AggPayBillVO aggvopay,
			nc.vo.pub.CircularlyAccessibleValueObject vo)
			throws BusinessException {
		if (aggvopay == null)
			return null;
		BaseDAO query = new BaseDAO();
		PayBillItemVO itemvo = (PayBillItemVO) vo;
		HashMap<String, Object> mappay = new HashMap<String, Object>();
		mappay.put("p_Ispaid", aggvopay.getParentVO()
				.getAttributeValue("def29"));// �Ƿ�����
		mappay.put("p_Actreceiver", query.executeQuery(
				"select name from bd_supplier where pk_supplier= gitem.supplier"
						+ itemvo.getSupplier(), new ColumnProcessor()));// ��Ӧ��
		mappay.put("p_Actrevaccount", itemvo.getDef5());// ʵ���տ�����˻�
		mappay.put("p_Actpaymoney", itemvo.getLocal_money_de());
		mappay.put("p_Actpaydate",
				aggvopay.getParentVO().getAttributeValue("billdate"));
		return mappay;
	}

	@Override
	public String pushEBSthPayrequest(AggPayrequest aggvo)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		// String url = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("YF_BACK_URL");// ebs�˻ؽӿڵ�ַ
		String url = OutsideUtils.getOutsideInfo("EBS0202");
		Object ebsID = aggvo.getParentVO().getAttributeValue("def1");
		if (ebsID == null) {
			throw new BusinessException("EBS idΪ��");
		}
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS �����������״̬Ϊ��");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def48");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("EBS �˻�ԭ��Ϊ��");
		}
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("FPAID", Integer.valueOf(ebsID.toString()));
		datamap.put("RETURN_FROM_STATUS", finstatusCode);
		datamap.put("RETURN_REASON", reasons);
		String param = JSON.toJSONString(datamap);
		String token = "";
		// String key = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("KEY");
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		String tokenkey = param + key;
		token = EncoderByMd5(tokenkey);
		String jsondata = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// ��¼��־nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("�������뵥(ͨ��)�˻�");
			logVO.setNcparm(param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg(msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
			} else {
				logVO.setMsg(msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			}
			logVO.setDr(0);
			saveLog.SaveLog_RequiresNew(logVO);
		}

		return jsondata;
	}

	@Override
	public String costPushEBSthPayrequest(AggPayrequest aggvo)
			throws BusinessException {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		// String url = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("COSTRETURNURL");// ebs�˻ؽӿڵ�ַ
		String url = OutsideUtils.getOutsideInfo("EBS0103");// ebs�˻ؽӿڵ�ַ
		Object ebsID = aggvo.getParentVO().getAttributeValue("def1");
		if (ebsID == null) {
			throw new BusinessException("EBS idΪ��");
		}
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS �����������״̬Ϊ��");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def48");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("EBS �˻�ԭ��Ϊ��");
		}
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		String fpaid = "";
		if (ebsID != null) {
			fpaid = ebsID.toString();
		}
		datamap.put("FPAID", fpaid);
		datamap.put("RETURN_FROM_STATUS", finstatusCode);
		datamap.put("RETURN_REASON", reasons);
		String param = getjson(datamap);
		String token = "";
		// String key = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("KEY");
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		String tokenkey = param + key;
		token = EncoderByMd5(tokenkey);
		String jsondata = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// ��¼��־nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("�������뵥(�ɱ�)�˻�");
			logVO.setNcparm(param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg(msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
			} else {
				logVO.setMsg(msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			}
			logVO.setDr(0);
			saveLog.SaveLog_RequiresNew(logVO);
		}

		return jsondata;
	}

	/**
	 * Ӧ�������˻�(�ɱ�) 2019-12-25-tzj
	 */
	@Override
	public String costPushEBSPayablerequest(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException {

		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String url = "";
		Object ebsID = aggvo.getParentVO().getAttributeValue("def1");
		if (ebsID == null) {
			throw new BusinessException("EBS idΪ��");
		}
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS �����������״̬Ϊ��");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def53");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("EBS �˻�ԭ��Ϊ��");
		}
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		String fpaid = "";
		if (ebsID != null) {
			fpaid = ebsID.toString();
		}
		/**
		 * �˻�ʱ�������ǽ���Ʊ�ĵ��ý���Ʊ�ӿ�2020-04-03-̸�ӽ�-start
		 */
		String def55 = (String) aggvo.getParent().getAttributeValue("def55");
		if (def55 != null && "����Ʊ".equals(def55)) {
			// ��ϵͳ���ݺ�
			datamap.put("apply_number",
					aggvo.getParent().getAttributeValue("def2"));
			// Ӧ��������
			datamap.put("nc_payment_id", aggvo.getPrimaryKey());
			// �˻ػ�����ͨ����ʶ ͨ��ΪY �˻�ΪN
			datamap.put("flag", "N");
			// ����ԭ��
			datamap.put("reason", reasons);
			// url = PropertiesUtil.getInstance("ebs_url.properties").readValue(
			// "COSTINVOICETOEBSURL");
			url = OutsideUtils.getOutsideInfo("EBS0105");
		} else {
			datamap.put("FPAID", fpaid);
			datamap.put("RETURN_FROM_STATUS", finstatusCode);
			datamap.put("RETURN_REASON", reasons);
			// url = PropertiesUtil.getInstance("ebs_url.properties").readValue(
			// "COSTRETURNURL");// ebs�˻ؽӿڵ�ַ
			url = OutsideUtils.getOutsideInfo("EBS0103");
		}
		/**
		 * �˻�ʱ�������ǽ���Ʊ�ĵ��ý���Ʊ�ӿ�2020-04-03-̸�ӽ�-end
		 */
		// datamap.put("FPAID", fpaid);
		// datamap.put("RETURN_FROM_STATUS", finstatusCode);
		// datamap.put("RETURN_REASON", reasons);
		String param = getjson(datamap);
		String token = "";
		// String key = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("KEY");
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		String tokenkey = param + key;
		token = EncoderByMd5(tokenkey);
		// ���ղ�ɾ��2020-03-25-̸�ӽ�
		UnSaveBillAndDelete(aggvo, aggVO);

		String jsondata = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// ��¼��־nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("Ӧ����(�ɱ�)�˻�");
			logVO.setNcparm("����ǰtoken:" + tokenkey + "�����ܺ�token��" + token
					+ "�����ģ�" + param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg("EBS��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
			} else {
				logVO.setMsg("EBS��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				throw new BusinessException("EBS��д��Ϣ:" + msg);
			}

		}

		return jsondata;

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

	@Override
	public String pushEBSthPayablerequest(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String url = "";
		Object ebsID = aggvo.getParentVO().getAttributeValue("def1");
		if (ebsID == null) {
			throw new BusinessException("EBS idΪ��");
		}
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS �����������״̬Ϊ��");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def53");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("EBS �˻�ԭ��Ϊ��");
		}
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		/**
		 * �˻�ʱ�������ǽ���Ʊ�ĵ��ý���Ʊ�ӿ�2020-04-03-̸�ӽ�-start
		 */
		String def55 = (String) aggvo.getParent().getAttributeValue("def55");
		if (def55 != null && "����Ʊ".equals(def55)) {
			// ��ϵͳ���ݺ�
			datamap.put("apply_number",
					aggvo.getParent().getAttributeValue("def2"));
			// Ӧ��������
			datamap.put("nc_payment_id", aggvo.getPrimaryKey());
			// �˻ػ�����ͨ����ʶ ͨ��ΪY �˻�ΪN
			datamap.put("flag", "N");
			// ����ԭ��
			datamap.put("reason", reasons);
			// url = PropertiesUtil.getInstance("ebs_url.properties").readValue(
			// "TRANINVOICETOEBSURL");
			url = OutsideUtils.getOutsideInfo("EBS0203");
		} else {
			datamap.put("FPAID", Integer.valueOf(ebsID.toString()));
			datamap.put("RETURN_FROM_STATUS", finstatusCode);
			datamap.put("RETURN_REASON", reasons);
			// url = PropertiesUtil.getInstance("ebs_url.properties").readValue(
			// "YF_BACK_URL");// ebs�˻ؽӿڵ�ַ
			url = OutsideUtils.getOutsideInfo("EBS0202");

		}
		/**
		 * �˻�ʱ�������ǽ���Ʊ�ĵ��ý���Ʊ�ӿ�2020-04-03-̸�ӽ�-end
		 */
		// datamap.put("FPAID", Integer.valueOf(ebsID.toString()));
		// datamap.put("RETURN_FROM_STATUS", finstatusCode);
		// datamap.put("RETURN_REASON", reasons);
		String param = getjson(datamap);
		String token = "";
		// String key = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("KEY");
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		String tokenkey = param + key;
		token = EncoderByMd5(tokenkey);
		// ���ղ�ɾ��2020-03-25-̸�ӽ�
		UnSaveBillAndDelete(aggvo, aggVO);

		String jsondata = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// String jsondata = Httpconnectionutil.newinstance().connection(
		// url, "&req=" + param);
		// ��¼��־nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("Ӧ����(ͨ��)�˻�");
			logVO.setNcparm(param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg("EBS��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
			} else {
				logVO.setMsg("EBS��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				throw new BusinessException("EBS��д��Ϣ:" + msg);
			}

		}

		return jsondata;
	}

	/**
	 * �˱�֤���д
	 * 
	 */
	@Override
	public String bondtoSRM(AggGatheringBillVO aggvo) throws BusinessException {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);

		String pk_org = (String) aggvo.getParentVO()
				.getAttributeValue("pk_org");
		String customer = (String) aggvo.getParentVO().getAttributeValue(
				"customer");
		String pk_balatype = (String) aggvo.getParentVO().getAttributeValue(
				"pk_balatype");
		String pk_currtype = (String) aggvo.getParentVO().getAttributeValue(
				"pk_currtype");

		String sql_org = "select code,name from org_orgs where nvl(dr,0)=0 and pk_org='"
				+ pk_org + "'";
		String sql_customer = "select code,name from bd_customer where nvl(dr,0)=0 and pk_customer='"
				+ customer + "'";
		String sql_balatype = "select code from bd_balatype where nvl(dr,0)=0 and pk_balatype='"
				+ pk_balatype + "'";
		String sql_currtype = "select code from bd_currtype where nvl(dr,0)=0 and pk_currtype='"
				+ pk_currtype + "'";
		Map<String, Object> map_org = (Map<String, Object>) bs.executeQuery(
				sql_org, new MapProcessor());
		Map<String, Object> map_customer = (Map<String, Object>) bs
				.executeQuery(sql_customer, new MapProcessor());
		String balatypecode = (String) bs.executeQuery(sql_balatype,
				new ColumnProcessor());
		String currtypecode = (String) bs.executeQuery(sql_currtype,
				new ColumnProcessor());

		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();// ���Ʊ����
		payapplymap_temp.put("refundStatus", aggvo.getParentVO()
				.getAttributeValue("def23"));// �˿�״̬
		payapplymap_temp.put("pkOrg", map_org.get("code"));// NC������֯
		payapplymap_temp.put("orgName", map_org.get("name"));// NC������֯
		payapplymap_temp.put("vendorName", map_customer.get("name"));// ��Ӧ������
		payapplymap_temp.put("vendorNumber", map_customer.get("code"));// ��Ӧ�̱���
		payapplymap_temp.put("agreementNumber", aggvo.getParentVO()
				.getAttributeValue("def5"));// Э����
		payapplymap_temp.put("paymentDate", aggvo.getParentVO()
				.getAttributeValue("billdate").toString().substring(0, 10));// �˿�����
		payapplymap_temp.put("payMethodCode", balatypecode);// �����
		payapplymap_temp.put("currency", currtypecode);// ����

		UFDouble mny = (UFDouble) aggvo.getParentVO()
				.getAttributeValue("money");
		payapplymap_temp.put("paymentAmount", mny != null ? mny.multiply(-1)
				.toString() : "0");// ������
		payapplymap_temp
				.put("ncPaymentId", aggvo.getParentVO().getPrimaryKey());// NC����ID
		payapplymap_temp.put("ncPaymentNumber", aggvo.getParentVO()
				.getAttributeValue("billno"));// NC���ݱ��
		payapplymap_temp.put("applyNumber", aggvo.getParentVO()
				.getAttributeValue("def2"));// Ԥ�������뵥��

		// ��������

		String syscode = PropertiesUtil.getInstance("srm_url.properties")
				.readValue("SRMSYSTEM");
		String key = PropertiesUtil.getInstance("srm_url.properties")
				.readValue("KEY");
		String token = "";
		if ("nc".equals(syscode)) {
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// ������ʱ��
			String time = formater.format(date);
			String tokenkey = time + key;
			token = MD5Util.getMD5(tokenkey).toUpperCase();
		}
		String code = "pushSrmPaymentReturn";
		String params = "code=" + code + "&syscode=" + syscode + "&token="
				+ token;
		String address = PropertiesUtil.getInstance("srm_url.properties")
				.readValue("SRMPAYURL");
		String urls = address + "/postMethod?" + params;
		String data = getjson(payapplymap_temp);
		String returnjson = Httpconnectionutil.newinstance().connection(urls,
				"&req=" + data);
		// ��¼��־nctoebslog
		if (returnjson != null) {
			JSONObject jobj = JSON.parseObject(returnjson);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("�˱�֤���д");
			logVO.setNcparm(getjson(payapplymap_temp));
			logVO.setSrcsystem("SRM");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg("SRM��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
			} else {
				logVO.setMsg("SRM��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			}
			logVO.setDr(0);
			saveLog.SaveLog_RequiresNew(logVO);
		}
		return returnjson;
	}

	/**
	 * ��ȡҵ�񵥾ݾۺ�VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}

	/**
	 * Ԫ���ݳ־û���ѯ�ӿ�
	 * 
	 * @return
	 */
	IMDPersistenceQueryService mdQryService = null;

	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}

	private void UnSaveBillAndDelete(AggPayableBillVO aggvo, AggPayrequest aggVO)
			throws BusinessException {
		// ����ɾ���������뵥��Ӧ�����Ĳ���2020-03-25-̸�ӽ� -start
		HashMap eParam = new HashMap();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		AggPayableBillVO[] vos = (AggPayableBillVO[]) getPfBusiAction()
				.processAction("RECALL", "F1", null, aggvo, null, eParam);
		AggPayableBillVO newVo = null;
		if (vos != null && vos.length > 0) {
			String primaryKey = vos[0].getPrimaryKey();
			newVo = (AggPayableBillVO) getBillVO(AggPayableBillVO.class,
					"isnull(dr,0)=0 and pk_payablebill = '" + primaryKey + "'");
		}
		if (newVo != null) {
			getPfBusiAction().processAction("DELETE", "F1", null, newVo, null,
					eParam);
		}
		String oldUserId = "";
		if (aggVO != null) {
			oldUserId = InvocationInfoProxy.getInstance().getUserId();
			String billmaker = (String) aggvo.getParent().getAttributeValue(
					"billmaker");
			InvocationInfoProxy.getInstance().setUserId(billmaker);
			String approvestatus = (String) aggVO.getParentVO()
					.getAttributeValue("approvestatus").toString();
			if ("3".equals(approvestatus)) {
				AggPayrequest[] billvos = (AggPayrequest[]) getPfBusiAction()
						.processAction("UNSAVEBILL", "FN01", null, aggVO, null,
								eParam);// �ջظ������뵥
				getPfBusiAction().processAction("DELETE", "FN01", null,
						billvos[0], null, eParam);
			} else {
				getPfBusiAction().processAction("DELETE", "FN01", null, aggVO,
						null, eParam);
			}

			InvocationInfoProxy.getInstance().setUserId(oldUserId);
		}

	}

	/**
	 * �������뵥ͨ����ͷ������ѯ�����2020-03-30
	 * 
	 * @throws BusinessException
	 */
	private List<Map<String, String>> getBillItem(String nc_payment_id)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select distinct to_char(b.def38) as table_id,  ");
		query.append("                to_char(b.def40) as table_type,  ");
		query.append("                to_char(b.def46) as fsplit_amt  ");
		query.append("  from tgfn_payreqbus b  ");
		query.append(" where b.pk_payreq = '" + nc_payment_id + "'  ");
		query.append("   and b.dr = 0;  ");
		List<Map<String, String>> list = (List<Map<String, String>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(query.toString(), new MapListProcessor());
		return list;
	}

	/**
	 * Ӧ��������ͨ������Ʊ��ebs(�ɱ�) 2020-04-03-̸�ӽ�
	 */
	@Override
	public String CostInvoiceSupplement(AggPayableBillVO aggvo)
			throws BusinessException {
		// �Ƿ��ѻ�д��ʶ
		String def63 = (String) aggvo.getParentVO().getAttributeValue("def63");
		String jsondata = "";
		if (!"Y".equals(def63)) {
			// throw new BusinessException("�õ����ѻ�д�ɹ�ebs�����ظ���д!");
			NcToEbsLogVO logVO = new NcToEbsLogVO();
			ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
			// String url = PropertiesUtil.getInstance("ebs_url.properties")
			// .readValue("COSTINVOICETOEBSURL");// ebs����Ʊ�ӿڵ�ַ
			String url = OutsideUtils.getOutsideInfo("EBS0105");

			HashMap<String, Object> datamap = new HashMap<String, Object>();
			// ��ϵͳ���ݺ�
			datamap.put("apply_number",
					aggvo.getParent().getAttributeValue("def2"));
			// Ӧ��������
			datamap.put("nc_payment_id", aggvo.getPrimaryKey());
			// �˻ػ�����ͨ����ʶ ͨ��ΪY �˻�ΪN
			datamap.put("flag", "Y");
			// ����ԭ��
			datamap.put("reason", "");

			String param = getjson(datamap);
			String token = "";
			// String key = PropertiesUtil.getInstance("ebs_url.properties")
			// .readValue("KEY");
			String key = OutsideUtils.getOutsideInfo("EBS-KEY");
			String tokenkey = param + key;
			token = EncoderByMd5(tokenkey);

			jsondata = Httpconnectionutil.newinstance().connection(url + token,
					"&req=" + param);
			// ��¼��־nctoebslog
			if (jsondata != null) {
				JSONObject jobj = JSON.parseObject(jsondata);
				String flag = jobj.getString("code");
				String msg = jobj.getString("msg");
				logVO.setTaskname("Ӧ��������Ʊ��EBS(�ɱ�)");
				logVO.setNcparm(param);
				logVO.setSrcsystem("EBS");
				logVO.setOperator("NC");
				logVO.setExedate(new UFDateTime().toString());
				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg("EBS��д��Ϣ:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
					logVO.setDr(0);
					saveLog.SaveLog_RequiresNew(logVO);
					// �����д�ɹ�����ϱ�ʶ-2020-06-09-̸�ӽ�
					insertFlag(aggvo.getPrimaryKey());
				} else {
					logVO.setMsg("EBS��д��Ϣ:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
					logVO.setDr(0);
					saveLog.SaveLog_RequiresNew(logVO);
					throw new BusinessException("EBS��д��Ϣ:" + msg);
				}

			}
			return jsondata;
		}
		return jsondata;
	}

	/**
	 * Ӧ��������ͨ������Ʊ��ebs(ͨ��) 2020-04-03-̸�ӽ�
	 */
	@Override
	public String TranInvoiceSupplement(AggPayableBillVO aggvo)
			throws BusinessException {
		String def63 = (String) aggvo.getParentVO().getAttributeValue("def63");
		String jsondata = "";
		if (!"Y".equals(def63)) {
			NcToEbsLogVO logVO = new NcToEbsLogVO();
			ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
			// String url = PropertiesUtil.getInstance("ebs_url.properties")
			// .readValue("TRANINVOICETOEBSURL");// ebs����Ʊ�ӿڵ�ַ
			String url = OutsideUtils.getOutsideInfo("EBS0203");
			HashMap<String, Object> datamap = new HashMap<String, Object>();
			// ��ϵͳ���ݺ�
			datamap.put("apply_number",
					aggvo.getParent().getAttributeValue("def2"));
			// Ӧ��������
			datamap.put("nc_payment_id", aggvo.getPrimaryKey());
			// �˻ػ�����ͨ����ʶ ͨ��ΪY �˻�ΪN
			datamap.put("flag", "Y");
			// ����ԭ��
			datamap.put("reason", "");

			String param = getjson(datamap);
			String token = "";
			// String key = PropertiesUtil.getInstance("ebs_url.properties")
			// .readValue("KEY");
			String key = OutsideUtils.getOutsideInfo("EBS-KEY");
			String tokenkey = param + key;
			token = EncoderByMd5(tokenkey);

			jsondata = Httpconnectionutil.newinstance().connection(url + token,
					"&req=" + param);
			// ��¼��־nctoebslog
			if (jsondata != null) {
				JSONObject jobj = JSON.parseObject(jsondata);
				String flag = jobj.getString("code");
				String msg = jobj.getString("msg");
				logVO.setTaskname("Ӧ��������Ʊ��EBS(ͨ��)");
				logVO.setNcparm(param);
				logVO.setSrcsystem("EBS");
				logVO.setOperator("NC");
				logVO.setExedate(new UFDateTime().toString());
				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg("EBS��д��Ϣ:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
					logVO.setDr(0);
					saveLog.SaveLog_RequiresNew(logVO);
					// �����д�ɹ�����ϱ�ʶ-2020-06-09-̸�ӽ�
					insertFlag(aggvo.getPrimaryKey());
				} else {
					logVO.setMsg("EBS��д��Ϣ:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
					logVO.setDr(0);
					saveLog.SaveLog_RequiresNew(logVO);
					throw new BusinessException("EBS��д��Ϣ:" + msg);
				}

			}

			return jsondata;
		}
		return jsondata;
	}

	/**
	 * SRM�˻ػ�д
	 */
	@Override
	public String pushSRMthPayrequest(AggPayrequest aggvo) throws Exception {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		Object ebsID = aggvo.getParentVO().getAttributeValue("def1");
		if (ebsID == null) {
			throw new BusinessException("SRM idΪ��");
		}
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def48");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("SRM �˻�ԭ��Ϊ��");
		}
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();

		// ��������
		String paymentType = (String) aggvo.getChildrenVO()[0]
				.getAttributeValue("def2");

		String type = null;
		if ("Ԥ����".equals(paymentType)) {
			type = "YPAY";
		} else if ("Ͷ�걣֤��".equals(paymentType)) {
			type = "Y_REFUND";
		} else {
			type = "BZPAY";
		} /*
		 * else if ("Ԥ�����˿�".equals(paymentType)) { type = "Y_REFUND"; }
		 */
		payapplymap_temp.put("paymentType", type);// ��������

		payapplymap_temp.put("command", aggvo.getParentVO().getDef48());// �������
		payapplymap_temp.put("operationType", "SrmBack");// NC�������̲�������
		payapplymap_temp.put("status", "false");// ����״̬
		payapplymap_temp.put("taskID", "");// BPM����id
		payapplymap_temp.put("pId", aggvo.getParentVO().getDef1());// ���������Ԥ��������id

		String address = OutsideUtils.getOutsideInfo("EBS0301");
		String data = getjson(payapplymap_temp);

		String receive = null;
		// ����post��ʽ�������
		PostMethod method = new PostMethod(address);
		try {

			// ����httpclient����
			HttpClient client = new HttpClient();

			// ���ò��������������
			method.setRequestHeader("Content-type",
					"application/json; charset=UTF-8");
			method.setRequestHeader("Accept", "application/json; charset=UTF-8");
			method.setRequestBody("&params=" + data);
			int rspCode = client.executeMethod(method);
			receive = method.getResponseBodyAsString();

			if (200 == rspCode) {
				JSONObject jobj = JSON.parseObject(receive);
				String flag = jobj.getString("code");
				String msg = jobj.getString("msg");
				logVO.setTaskname("�������뵥(SRM)�˻�");
				logVO.setNcparm(data);
				logVO.setSrcsystem("SRM");
				logVO.setOperator("NC");
				logVO.setExedate(new UFDateTime().toString());
				logVO.setDr(0);

				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg("SRM��д��Ϣ:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				} else {
					throw new BusinessException("EBS�����쳣:" + msg);
				}
			}
		} catch (Exception e) {
			logVO.setMsg(e.getMessage());
			logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			method.releaseConnection();

			saveLog.SaveLog_RequiresNew(logVO);

		}

		return receive;
	}

	/***
	 * ��Ʊ������Ʊ huangxj
	 */
	@Override
	public String OpenInvoice(HashMap<String, Object> value) throws Exception {
		OutsideLogVO logvo = new OutsideLogVO();
		HashMap<String, Object> returnMap = new HashMap<>();
		ISqlThread saveLog = NCLocator.getInstance().lookup(ISqlThread.class);
		String urlstr = OutsideUtils.getOutsideInfo("LS008");
		;/*
		 * (String) getBaseDAO().executeQuery(
		 * "select url from nc_urlport where sysid='" + 2 + "'", new
		 * ColumnProcessor());
		 */
		NcRewriteOutSysInterfaceLog rewriteLog = new NcRewriteOutSysInterfaceLog();
		// ��־��¼url��Ϣ
		logvo.setDesbill("��Ӧ�����ݴ�����˰");
		JSONObject jsonobject = new JSONObject();

		StringBuilder sb = new StringBuilder();
		try {
			// ��װ��Ʊ����
			String data = getjson(value);
			logvo.setSrcparm(data);
			logvo.setSrcsystem("SRM");
			logvo.setOperator("NC");
			logvo.setExedate(new UFDateTime().toString());
			logvo.setDr(0);
			rewriteLog.setDef2((String) value.get("billGuid"));
			rewriteLog.setSendparam(data);
			rewriteLog.setUrlinfo(urlstr);
			// SSLContext sslcontext = SSLContext.getInstance("SSL","SunJSSE");

			// sslcontext.init(null, new TrustManager[] { new X509TrustUtiil()
			// }, new java.security.SecureRandom());
			URL url = new URL(urlstr);
			// URL url= new URL(null, urlstr, new
			// sun.net.www.protocol.https.Handler());

			// HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier()
			// {
			// public boolean verify(String s, SSLSession sslsession) {
			// System.out.println("WARNING: Hostname is not matched for cert.");
			// return true;
			// }
			// };
			// HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
			// HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(30000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// ���ò��û���
			connection.setUseCaches(false);
			// ���ô��ݷ�ʽ
			connection.setRequestMethod("POST");
			// ����ά�ֳ�����
			connection.setRequestProperty("Connection", "Keep-Alive");
			// �����ļ��ַ���:
			connection.setRequestProperty("Charset", "UTF-8");
			// �����ļ�����:
			connection.setRequestProperty("content-type", "application/json");

			connection.setRequestProperty("accept", "*/*");
			connection
					.setRequestProperty(
							"token",
							"839mkHas0we093s262Wfmsa+fds23ia13dfkRidMNo92Ul01266qasfDn636OoZ99xzZ5550ToMyHoo396332LxvIEbs520CxZ0109wLf");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			OutputStream os = connection.getOutputStream();
			os.write(data.getBytes("UTF-8"));
			int responseCode = connection.getResponseCode();
			// ����״̬
			rewriteLog.setDef1(responseCode + "");

			if (200 == responseCode) {
				InputStream is = connection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader br = new BufferedReader(isr);

				String temp = null;
				while (null != (temp = br.readLine())) {
					sb.append(temp);
				}
				is.close();
				isr.close();
				br.close();
				os.close();

				jsonobject = com.alibaba.fastjson.JSONObject.parseObject(sb
						.toString());

				JSONObject js = jsonobject.getJSONArray("objects")
						.getJSONObject(0);
				if (!"OK".equals(js.getString("msg"))) {
					throw new BusinessException("��˰������Ϣ��" + sb);
				} else {
					logvo.setErrmsg("��˰�ش���Ϣ:" + sb.toString());
					logvo.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				}

				// ��Ʊ��Ϣ��д
				// aggvo =writeBackInvoiseMsg(sb.toString(),aggvo,negativeFlag);
				// returnMap.put("aggvo", aggvo);
				returnMap.put("msg", sb.toString());
				return sb.toString();
			}
		} catch (Exception e) {
			returnMap.put("msg", e);
			rewriteLog.setDef2(e.getMessage());
			e.printStackTrace();
			logvo.setErrmsg("������˰�ӿ��쳣:" + e.getMessage());
			logvo.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			throw new Exception("������˰�ӿ��쳣:" + e.getMessage());
		} finally {
			rewriteLog.setUrlinfo(urlstr);

			try {
				saveLog.billInsert_RequiresNew(logvo);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
			/*
			 * IwriteInvoiceLog writeInvoiceLog =
			 * NCLocator.getInstance().lookup( IwriteInvoiceLog.class);
			 */
			// writeInvoiceLog(aggvo,sb.toString(),negativeFlag,rewriteLog);
			/*
			 * writeInvoiceLog.writeInvoiceLog_RequiresNew( aggvo null,
			 * sb.toString(), s 0, rewriteLog);
			 */
			// ��¼��Ʊ��־
		}
		return sb.toString();
	}

	/**
	 * ռԤ��+��˰��Ӧ��������ͨ����ebs(�ɱ�) 2020-06-05-̸�ӽ�
	 */
	@Override
	public String CostBudgetTaxdifferencePushEbs(AggPayableBillVO aggvo,
			String flag) throws BusinessException {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String url = OutsideUtils.getOutsideInfo("EBS0106");
		// �Ƿ��ѻ�д��ʶ
		String def63 = (String) aggvo.getParentVO().getAttributeValue("def63");
		if ("Y".equals(def63)) {
			throw new BusinessException("�õ����ѻ�д�ɹ�ebs�����ظ���д!");
		}
		// �����ʶΪN�����˻ض���
		String reasons = "";
		String apply_number = (String) aggvo.getParentVO().getAttributeValue(
				"def2");
		String billid = aggvo.getPrimaryKey();
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue(
				"pk_tradetype");
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS �����������״̬Ϊ��");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		if ("N".equals(flag)) {
			reasons = (String) aggvo.getParentVO().getAttributeValue("def53");
			if (reasons == null || "".equals(reasons)) {
				throw new BusinessException("EBS �˻�ԭ��Ϊ��");
			}
			UnSaveBillAndDelete(aggvo, null);
		}

		// {"APPLY_NUMBER": "100101258.123-00119", "NC_PAYMENT_ID":
		// "123421133","FLAG":"Y","REASON":"��Ϊ��Ʊ���ϸ�" }

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		// ��ϵͳ���ݺ�
		datamap.put("APPLY_NUMBER", apply_number);
		// Ӧ��������
		datamap.put("NC_PAYMENT_ID", billid);
		// �˻ػ�����ͨ����ʶ ͨ��ΪY �˻�ΪN
		datamap.put("FLAG", flag);
		String def55 = (String) aggvo.getParent().getAttributeValue("def55");

		// ������������״̬
		if ("����Ʊ".equals(def55)) {
			datamap.put("RETURN_FROM_STATUS", "ticketOnly");
		} else {
			datamap.put("RETURN_FROM_STATUS", finstatusCode);
		}
		// ����ԭ��
		if ("Y".equals(flag)) {
			datamap.put("REASON", "");
		} else {
			datamap.put("REASON", reasons);
		}

		String param = getjson(datamap);
		String token = "";
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		String tokenkey = param + key;
		token = EncoderByMd5(tokenkey);

		String jsondata = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// ��¼��־nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String code = jobj.getString("code");
			String msg = jobj.getString("msg");
			/**
			 * F1-Cxx-010,�ɱ�˰��Ӧ���� F1-Cxx-011,�ɱ�ռԤ��Ӧ����
			 */
			if ("F1-Cxx-010".equals(pk_tradetype)) {
				if ("Y".equals(flag)) {
					logVO.setTaskname("�ɱ�˰��Ӧ��������ͨ����ebs(�ɱ�)");
				} else {
					logVO.setTaskname("�ɱ�˰��Ӧ�����˻���ebs(�ɱ�)");
				}

			}
			if ("F1-Cxx-011".equals(pk_tradetype)) {
				if ("Y".equals(flag)) {
					logVO.setTaskname("�ɱ�ռԤ��Ӧ��������ͨ����ebs(�ɱ�)");
				} else {
					logVO.setTaskname("�ɱ�ռԤ��Ӧ�����˻���ebs(�ɱ�)");
				}

			}

			logVO.setNcparm(param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(code)) {
				logVO.setResult(code);
				logVO.setMsg("EBS��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				if ("Y".equals(flag)) {
					// �����д�ɹ�����ϱ�ʶ-2020-06-09-̸�ӽ�
					insertFlag(billid);
				}
			} else {
				logVO.setMsg("EBS��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				throw new BusinessException("EBS��д��Ϣ:" + msg);
			}

		}

		return jsondata;

	}

	/**
	 * ռԤ��+��˰��Ӧ��������ͨ����ebs(ͨ��)
	 */
	@Override
	public String TranBudgetTaxdifferencePushEbs(AggPayableBillVO aggvo,
			String flag) throws BusinessException {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String url = "";
		if ("N".equals(flag)) {
			url = OutsideUtils.getOutsideInfo("EBS0202");
		} else {
			url = OutsideUtils.getOutsideInfo("EBS0206");
		}
		// �Ƿ��ѻ�д��ʶ
		String def63 = (String) aggvo.getParentVO().getAttributeValue("def63");
		if ("Y".equals(def63)) {
			throw new BusinessException("�õ����ѻ�д�ɹ�ebs�����ظ���д!");
		}
		// �����ʶΪN�����˻ض���
		String reasons = "";
		String ebsid = (String) aggvo.getParentVO().getAttributeValue("def1");
		String billid = aggvo.getPrimaryKey();
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue(
				"pk_tradetype");
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS �����������״̬Ϊ��");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		if ("N".equals(flag)) {
			reasons = (String) aggvo.getParentVO().getAttributeValue("def53");
			if (reasons == null || "".equals(reasons)) {
				throw new BusinessException("EBS �˻�ԭ��Ϊ��");
			}
			UnSaveBillAndDelete(aggvo, null);
		}

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		if ("N".equals(flag)) {
			// ��ϵͳ����
			datamap.put("FPAID", Integer.valueOf(ebsid));
			// ������������״̬
			datamap.put("RETURN_FROM_STATUS", "ticketOnly");
			// ����ԭ��
			datamap.put("RETURN_REASON", reasons);
		} else {
			// ��ϵͳ����
			datamap.put("p_Paybillid", Integer.valueOf(ebsid));
			datamap.put("p_Is_Cashles", "Y");
		}

		String param = getjson(datamap);
		String token = "";
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		String tokenkey = param + key;
		token = EncoderByMd5(tokenkey);

		String jsondata = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// ��¼��־nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String code = jobj.getString("code");
			String msg = jobj.getString("msg");
			/**
			 * F1-Cxx-012,ͨ��˰��Ӧ���� F1-Cxx-013,ͨ��ռԤ��Ӧ����
			 */
			if ("F1-Cxx-012".equals(pk_tradetype)) {
				if ("Y".equals(flag)) {
					logVO.setTaskname("ͨ��˰��Ӧ��������ͨ����ebs(ͨ��)");
				} else {
					logVO.setTaskname("ͨ��˰��Ӧ�����˻���ebs(ͨ��)");
				}

			}
			if ("F1-Cxx-013".equals(pk_tradetype)) {
				if ("Y".equals(flag)) {
					logVO.setTaskname("ͨ��ռԤ��Ӧ��������ͨ����ebs(ͨ��)");
				} else {
					logVO.setTaskname("ͨ��ռԤ��Ӧ�����˻���ebs(ͨ��)");
				}

			}

			logVO.setNcparm(param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(code)) {
				logVO.setResult(code);
				logVO.setMsg("EBS��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				if ("Y".equals(flag)) {
					// �����д�ɹ�����ϱ�ʶ-2020-06-09-̸�ӽ�
					insertFlag(billid);
				}
			} else {
				logVO.setMsg("EBS��д��Ϣ:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				throw new BusinessException("EBS��д��Ϣ:" + msg);
			}

		}

		return jsondata;

	}

	/**
	 * �ѻ�д����ϱ�ʶ
	 * 
	 * @param pk
	 * @throws BusinessException
	 */
	private void insertFlag(String pk) throws BusinessException {
		PayableBillVO headvo = (PayableBillVO) EBSBillUtils.getUtils()
				.getBaseDAO().retrieveByPK(PayableBillVO.class, pk);
		headvo.setStatus(VOStatus.UPDATED);
		headvo.setDef63("Y");
		headvo.setDr(0);
		EBSBillUtils.getUtils().getBaseDAO().updateVO(headvo);
	}
}
