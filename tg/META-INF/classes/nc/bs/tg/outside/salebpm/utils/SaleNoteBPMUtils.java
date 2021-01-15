package nc.bs.tg.outside.salebpm.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.outside.IBPMBillCont;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.itf.tg.outside.OutsideUtils;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.cdm.repayreceiptbankcredit.RePayReceiptBankCreditVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.addticket.AddTicket;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tg.outside.bpm.NcToBpmVO;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import uap.serverdes.appesc.MD5Util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ֪ͨbpm�ӿ�
 * 
 * @author nctanjingliang
 * 
 */
public class SaleNoteBPMUtils extends SaleBPMBillUtils {

	static SaleNoteBPMUtils utils;
	IMDPersistenceQueryService mdQryService = null;

	public static SaleNoteBPMUtils getUtils() {
		if (utils == null) {
			utils = new SaleNoteBPMUtils();
		}
		return utils;
	}

	/**
	 * ֧������bpm�鵵�ӿ�
	 * 
	 * @param aggvo
	 * @param pk_settlement
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> pushBillToBpm(AggPayBillVO aggvo,
			String pk_settlement) throws Exception {
		// TODO �Զ����ɵķ������

		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(
				IPushBPMBillFileService.class);
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////		String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=RestoreExt&UserAccount="
				+ usercode;

		String token = "";
		Date date = new Date();
		String app_key = PropertiesUtil.getInstance("salebpm_url.properties")
				.readValue("SALEBPMKEY");
		String app_key_ticket = PropertiesUtil.getInstance(
				"salebpm_url.properties").readValue("SALEBPMTICKET");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");// ������ʱ��
		String time = formater.format(date);
		String tokenkey = app_key + app_key_ticket + time;
		token = MD5Util.getMD5(tokenkey).toUpperCase();

		String proname = null;
		if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-011")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_011);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-012")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_012);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-016")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_016);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-017")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_017);
		}else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-027")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_027);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		postdata.put("ApprovalType",
				aggvo.getParentVO().getAttributeValue("approvestatus")
						.equals(1) ? "ProcessApproved" : "ProcessBack");
		postdata.put("OperationType",
				aggvo.getParentVO().getAttributeValue("approvestatus")
						.equals(1) ? "none" : "back");
		postdata.put("TaskID", aggvo.getParentVO().getAttributeValue("def55"));
		postdata.put("Command", "������");
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(proname);
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(urls+json);
		getBaseDAO().insertVO(logVO);

		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");

		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// �����������
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");

		// ������Ϣͷϵͳ����
		conn.setRequestProperty("System", "NC");

		// ������Ϣͷtoken
		conn.setRequestProperty("Authorization", token);

		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			String errmsg = null;
			map.put("flag", flag);
			map.put("errorMessage", result.get("errorMessage"));
			// String openUrl = null;
			if (flag.equals("false")) {
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				if (result.get("errorMessage") != null
						&& !"null".equals(result.get("errorMessage"))) {
					logVO.setErrmsg((String) result.get("errorMessage"));
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��"
							+ result.get("errorMessage") + "��");
				} else {
					logVO.setErrmsg(errmsg);
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
				}
			} else if (flag.equals("true")
					&& aggvo.getParentVO().getAttributeValue("approvestatus")
							.equals(1) && StringUtils.isNotBlank(pk_settlement)) {// ������سɹ�����bpm��Ϣ����nc������״̬��������,����´�bpm״̬
				PayBillVO headvo = (PayBillVO) aggvo.getParentVO();
				headvo.setStatus(VOStatus.UPDATED);
				headvo.setDef61("Y");
				getBaseDAO().updateVO(headvo);
				EBSBillUtils
						.getUtils()
						.getBaseDAO()
						.executeUpdate(
								"update cmp_detail set def1 = 'Y' where pk_settlement = '"
										+ pk_settlement + "'");
			}
			return map;
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
		}
	}

	/**
	 * ֧�����Ĳ���֪ͨbpm�ӿ�
	 * 
	 * @param aggvo
	 * @param pk_settlement
	 * @return
	 * @throws Exception
	 */
	public AggPayBillVO pushReturnBillToBpmInitiator(AggPayBillVO aggvo)
			throws Exception {
		// TODO �Զ����ɵķ������

		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(
				IPushBPMBillFileService.class);
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////		String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=ReturnToInitiator&UserAccount="
				+ usercode;

		String token = "";
		Date date = new Date();
		String app_key = PropertiesUtil.getInstance("salebpm_url.properties")
				.readValue("SALEBPMKEY");
		String app_key_ticket = PropertiesUtil.getInstance(
				"salebpm_url.properties").readValue("SALEBPMTICKET");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");// ������ʱ��
		String time = formater.format(date);
		String tokenkey = app_key + app_key_ticket + time;
		token = MD5Util.getMD5(tokenkey).toUpperCase();

		String proname = null;
		if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-011")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_011);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-012")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_012);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-016")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_016);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-017")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_017);
		}else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-027")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_027);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		postdata.put("TaskID", aggvo.getParentVO().getAttributeValue("def55"));
		postdata.put("Comments", "�˻�BPM");
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(proname+"����֪ͨbpm");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(json);
		getBaseDAO().insertVO(logVO);

		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");

		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// �����������
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");

		// ������Ϣͷϵͳ����
		conn.setRequestProperty("System", "NC");

		// ������Ϣͷtoken
		conn.setRequestProperty("Authorization", token);

		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			String errmsg = null;
			map.put("flag", flag);
			// String openUrl = null;
			if (flag.equals("false")) {
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				if (result.get("errorMessage") != null
						&& !"null".equals(result.get("errorMessage"))) {
					logVO.setErrmsg((String) result.get("errorMessage"));
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��"
							+ result.get("errorMessage") + "��");
				} else {
					logVO.setErrmsg(errmsg);
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
				}
			} else {
				EBSBillUtils
				.getUtils()
				.getBaseDAO()
				.executeUpdate(
						"update ap_paybill set def71 = 'Y' where pk_paybill = '"
								+ aggvo.getPrimaryKey() + "'");
			}
			return aggvo;
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
		}
	}

	/**
	 * ����BPM�����鵵�ӿ�
	 * 
	 * @param aggvo
	 * @param pk_settlement
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> pushToBpm(NcToBpmVO aggvo, String pk_settlement)
			throws Exception {
		// TODO �Զ����ɵķ������

		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		OutsideLogVO logVO = new OutsideLogVO();
		String billqueue = aggvo.getPk_busibill();
		EBSBillUtils.addBillQueue(billqueue);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
//			String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
//			String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//			String address = QueryDocInfoUtils.getUtils().getColumnValue(
//					getUrlSql);
			String address = OutsideUtils.getOutsideInfo("BPM");
			String urls = address
					+ "/YZSoft/WebService/YZService.ashx?Method=RestoreExt&UserAccount="
					+ usercode;

			String token = "";
			Date date = new Date();
			String app_key = PropertiesUtil.getInstance(
					"salebpm_url.properties").readValue("SALEBPMKEY");
			String app_key_ticket = PropertiesUtil.getInstance(
					"salebpm_url.properties").readValue("SALEBPMTICKET");
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");// ������ʱ��
			String time = formater.format(date);
			String tokenkey = app_key + app_key_ticket + time;
			token = MD5Util.getMD5(tokenkey).toUpperCase();

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
			postdata.put("ApprovalType", aggvo.getApprovaltype());
			postdata.put("OperationType", aggvo.getOperationtype());
			postdata.put("TaskID", aggvo.getTaskid());
			if(pk_settlement==null){
				postdata.put("Command", aggvo.getCommand());
			}else{
				postdata.put("Command", "������");
			}
			logVO.setSrcsystem("BPM");
			logVO.setDesbill(aggvo.getDesbill());
			logVO.setPrimaryKey(aggvo.getPk_busibill());
			logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
			logVO.setExedate(new UFDateTime().toString());
			String json = objectMapper.writeValueAsString(postdata);
			logVO.setSrcparm(json);
			Logger.error("----------����ʼ---------------");
			Logger.error("----------�����������" + json + "��---------------");

			// ����url��Դ
			URL url = new URL(urls);
			// ����http����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// �����������
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// ���ò��û���
			conn.setUseCaches(false);
			// ���ô��ݷ�ʽ
			conn.setRequestMethod("POST");
			// ����ά�ֳ�����
			conn.setRequestProperty("Connection", "Keep-Alive");
			// �����ļ��ַ���:
			conn.setRequestProperty("Charset", "utf-8");
			// ת��Ϊ�ֽ�����
			byte[] data = json.getBytes("utf-8");
			// �����ļ�����
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));

			// �����ļ�����:
			conn.setRequestProperty("Content-Type", "application/json");

			// ������Ϣͷϵͳ����
			conn.setRequestProperty("System", "NC");

			// ������Ϣͷtoken
			conn.setRequestProperty("Authorization", token);

			// ��ʼ��������
			conn.connect();
			OutputStream out = conn.getOutputStream();
			// д��������ַ���
			out.write(data);
			out.flush();
			out.close();
			String a = "";
			// ���󷵻ص�״̬
			if (conn.getResponseCode() == 200) {
				Logger.error("���ӳɹ�");
				// ���󷵻ص�����
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				String line = null;
				while ((line = in.readLine()) != null) {
					a += line;
				}
				JSONObject result = new JSONObject(a);
				String flag = (String) result.getString("success");
				logVO.setErrmsg(String.valueOf(result));
				String errmsg = null;
				map.put("flag", flag);
				// String openUrl = null;
				logVO.setErrmsg(result.toString());
				if (flag.equals("false")) {
					if (result.get("errorMessage") != null
							&& !"null".equals(result.get("errorMessage"))) {
						logVO.setErrmsg(result.get("errorMessage").toString());
						throw new BusinessException("������BPM�Ĵ�����Ϣ��"
								+ result.get("errorMessage") + "��");
					} else {
						throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg
								+ "��");
					}
				} else if (flag.equals("true")
						&& StringUtils.isNotBlank(pk_settlement)) {// ������سɹ�����bpm��Ϣ����nc������״̬��������,����´�bpm״̬
					EBSBillUtils
							.getUtils()
							.getBaseDAO()
							.executeUpdate(
									"update cmp_detail set def1 = 'Y' where pk_settlement = '"
											+ pk_settlement + "'");
				}
				return map;
			} else {
				Logger.error("����ʧ��");
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
			}
		} catch (Exception e) {
			logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
			IPushBPMBillFileService service = NCLocator.getInstance().lookup(
					IPushBPMBillFileService.class);
			service.saveLog_RequiresNew(logVO);
		}
	}

	/**
	 * ֧������֪ͨbpm�鵵�ӿ�
	 * 
	 * @param pk_paybill
	 * @param pk_settlement
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> pushToBpmFileNone(String pk_paybill,
			String pk_settlement) throws Exception {
		// TODO �Զ����ɵķ������

//		BillQuery<AggPayBillVO> billquery = new BillQuery<AggPayBillVO>(
//				AggPayBillVO.class);
//		AggPayBillVO[] aggvos = billquery.query(new String[] { pk_paybill });
//		if (aggvos != null) {
//			for (AggPayBillVO aggvo : aggvos) {
		AggPayBillVO aggvo  = (AggPayBillVO) getBillVO(AggPayBillVO.class," isnull(dr,0) = 0 and pk_paybill = '"+pk_paybill+"' ");
		String usercode = InvocationInfoProxy.getInstance()
				.getUserCode();
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////				String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(
//				getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=RestoreExt&UserAccount="
				+ usercode;
		
		String token = "";
		Date date = new Date();
		String app_key = PropertiesUtil.getInstance(
				"salebpm_url.properties").readValue("SALEBPMKEY");
		String app_key_ticket = PropertiesUtil.getInstance(
				"salebpm_url.properties").readValue("SALEBPMTICKET");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");// ������ʱ��
		String time = formater.format(date);
		String tokenkey = app_key + app_key_ticket + time;
		token = MD5Util.getMD5(tokenkey).toUpperCase();
		
		String proname = null;
		if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-011")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_011);
		} else if (aggvo.getParentVO()
				.getAttributeValue("pk_tradetype").equals("F3-Cxx-012")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_012);
		} else if (aggvo.getParentVO()
				.getAttributeValue("pk_tradetype").equals("F3-Cxx-016")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_016);
		} else if (aggvo.getParentVO()
				.getAttributeValue("pk_tradetype").equals("F3-Cxx-017")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_017);
		}else if (aggvo.getParentVO()
				.getAttributeValue("pk_tradetype").equals("F3-Cxx-027")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_027);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		postdata.put("ApprovalType",
				aggvo.getParentVO().getAttributeValue("approvestatus")
				.equals(1) ? "ProcessApproved" : "ProcessBack");
		postdata.put("OperationType", aggvo.getParentVO()
				.getAttributeValue("approvestatus").equals(1) ? "none"
						: "back");
		postdata.put("TaskID",
				aggvo.getParentVO().getAttributeValue("def55"));
		postdata.put("Command", "������");
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(proname);
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(json);
		logVO.setOperator((String) aggvo.getParentVO().getAttributeValue("billno"));
		getBaseDAO().insertVO(logVO);
		
		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");
		
		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url
				.openConnection();
		// �����������
		conn.setDoOutput(true);
		
		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length",
				String.valueOf(data.length));
		
		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");
		
		// ������Ϣͷϵͳ����
		conn.setRequestProperty("System", "NC");
		
		// ������Ϣͷtoken
		conn.setRequestProperty("Authorization", token);
		
		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(),
							"UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			map.put("flag", flag);
			// String openUrl = null;
			if (flag.equals("false")) {
				return map;
			} else {// ������سɹ�����bpm��Ϣ����nc������״̬��������,����´�bpm״̬
				PayBillVO headvo = (PayBillVO) aggvo.getParentVO();
				headvo.setStatus(VOStatus.UPDATED);
				headvo.setDef61("Y");
				getBaseDAO().updateVO(headvo);
				EBSBillUtils
				.getUtils()
				.getBaseDAO()
				.executeUpdate(
						"update cmp_detail set def1 = 'Y' where pk_settlement = '"
								+ pk_settlement + "'");
				EBSBillUtils
				.getUtils()
				.getBaseDAO()
				.executeUpdate(
						"update cmp_settlement set bpmid = '"+headvo.getDef55()+"' where pk_settlement = '"
								+ pk_settlement + "'");
			}
			return map;
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
		}

	}

	/**
	 * bpmɾ������
	 * 
	 * @param aggvo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> pushToBPMBillDelete(AggPayBillVO aggvo)
			throws Exception {

		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(
				IPushBPMBillFileService.class);
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////		String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=DeleteExt&UserAccount="
				+ usercode;
		String proname = null;
		if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-011")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_011);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-012")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_012);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-016")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_016);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-017")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_017);
		}else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-027")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_027);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		postdata.put("TaskID", aggvo.getParentVO().getAttributeValue("def55"));
		postdata.put("Comments", "ɾ��ԭ��");
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(proname + "ɾ������");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(json);
		getBaseDAO().insertVO(logVO);

		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");

		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// �����������
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");

		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			String errmsg = null;
			map.put("flag", flag);
			// String openUrl = null;
			if (flag.equals("false")) {
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				if (result.get("errorMessage") != null
						&& !"null".equals(result.get("errorMessage"))) {
					logVO.setErrmsg((String) result.get("errorMessage"));
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��"
							+ result.get("errorMessage") + "��");
				} else {
					logVO.setErrmsg(errmsg);
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
				}
			}
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
		}
		return map;
	}

	/**
	 * ���ʵ��ݲ���֪ͨbpm
	 * 
	 * @param pk_paybill
	 * @param pk_settlement
	 * @return
	 * @throws Exception
	 */
	public AggregatedValueObject pushToFinBpmFile(AbstractBill bill,
			String billcode, String pk_settlement) throws Exception {
		// TODO �Զ����ɵķ������

		AggFinancexpenseVO finAggvo = null;
		AggRePayReceiptBankCreditVO repayAggvo = null;
		AggAddTicket addTicketAggvo = null;
		AggMarketRepalayVO marketvo = null;
		HashMap eParam = new HashMap();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		String billtype = null;
//		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		if (ISaleBPMBillCont.BILL_17.equals(billcode)
				|| ISaleBPMBillCont.BILL_18.equals(billcode)) {// �ƹ˷�&���ʷ�
			billtype = "RZ06";
			finAggvo = (AggFinancexpenseVO) bill;
			String approve=(String) finAggvo.getParentVO().getAttributeValue("approver");
			if(approve!=null){
				finAggvo = ((AggFinancexpenseVO[]) getPfBusiAction().processAction(IPFActionName.UNAPPROVE+getBPMUserID(), billtype, null, finAggvo, null, eParam))[0];
			}else{
				finAggvo = ((AggFinancexpenseVO[]) getPfBusiAction().processAction(IPFActionName.UNAPPROVE, billtype, null, finAggvo, null, eParam))[0];
			}
			
		} else if (ISaleBPMBillCont.BILL_15.equals(billcode)
				|| ISaleBPMBillCont.BILL_16.equals(billcode)) {// ������Ϣ
			billtype = "36FF";
			repayAggvo = (AggRePayReceiptBankCreditVO) bill;
			String approve=(String) repayAggvo.getParentVO().getAttributeValue("approver");
			if(approve!=null){
				repayAggvo = ((AggRePayReceiptBankCreditVO[]) getPfBusiAction().processAction(IPFActionName.UNAPPROVE+getBPMUserID(), billtype, null, repayAggvo, null, eParam))[0];
			}else{
				repayAggvo = ((AggRePayReceiptBankCreditVO[]) getPfBusiAction().processAction(IPFActionName.UNAPPROVE, billtype, null, repayAggvo, null, eParam))[0];
			}
		} else if (ISaleBPMBillCont.BILL_19.equals(billcode)) {// ��Ʊ��
			billtype = "RZ30";
			addTicketAggvo = (AggAddTicket) bill;
			String approve= addTicketAggvo.getParentVO().getApprover();
			if(approve!=null){
				addTicketAggvo = ((AggAddTicket[]) getPfBusiAction().processAction(IPFActionName.UNAPPROVE+getBPMUserID(), billtype, null, addTicketAggvo, null, eParam))[0];
			}else{
				addTicketAggvo = ((AggAddTicket[]) getPfBusiAction().processAction(IPFActionName.UNAPPROVE, billtype, null, addTicketAggvo, null, eParam))[0];
			}
		} else if(ISaleBPMBillCont.BILL_24.equals(billcode)){
			billtype = "SD08";
			marketvo = (AggMarketRepalayVO)bill;
			String approve= marketvo.getParentVO().getApprover();
			if(approve!=null){
				marketvo = ((AggMarketRepalayVO[])getPfBusiAction().processAction(IPFActionName.UNAPPROVE+getBPMUserID(), billtype, null, marketvo, null, eParam))[0];
			}else{
				marketvo = ((AggMarketRepalayVO[])getPfBusiAction().processAction(IPFActionName.UNAPPROVE, billtype, null, marketvo, null, eParam))[0];
			}
		}

		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////		String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=RestoreExt&UserAccount="
				+ usercode;

		String token = "";
		Date date = new Date();
		String app_key = PropertiesUtil.getInstance("salebpm_url.properties")
				.readValue("SALEBPMKEY");
		String app_key_ticket = PropertiesUtil.getInstance(
				"salebpm_url.properties").readValue("SALEBPMTICKET");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");// ������ʱ��
		String time = formater.format(date);
		String tokenkey = app_key + app_key_ticket + time;
		token = MD5Util.getMD5(tokenkey).toUpperCase();

		IPushBPMBillFileService service = NCLocator.getInstance().lookup(
				IPushBPMBillFileService.class);

		String proname = null;
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		postdata.put("ApprovalType", pk_settlement != null ? "ProcessApproved"
				: "ProcessBack");
		postdata.put("OperationType", pk_settlement != null ? "none" : "back");
		if (finAggvo != null) {
			postdata.put("TaskID",
					finAggvo.getParentVO().getAttributeValue("def19"));
		} else if (repayAggvo != null) {
			postdata.put("TaskID",
					repayAggvo.getParentVO().getAttributeValue("vdef19"));
		} else if (addTicketAggvo != null) {
			postdata.put("TaskID", addTicketAggvo.getParentVO()
					.getAttributeValue("def19"));
		} else if(marketvo != null){
			postdata.put("TaskID", marketvo.getParentVO().getDef20());
		}
		if(pk_settlement==null){
		postdata.put("Command", bill.getParentVO().getAttributeValue("def47"));
		bill.getParentVO().setAttributeValue("def47", null);
		}else{
			postdata.put("Command","�������");	
		}
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(proname);
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(json);
		getBaseDAO().insertVO(logVO);

		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");

		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// �����������
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");

		// ������Ϣͷϵͳ����
		conn.setRequestProperty("System", "NC");

		// ������Ϣͷtoken
		conn.setRequestProperty("Authorization", token);

		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			map.put("flag", flag);
			logVO.setErrmsg(String.valueOf(result));
			// String openUrl = null;
			String errmsg = null;
			if (flag.equals("false")) {
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				if (result.get("errorMessage") != null
						&& !"null".equals(result.get("errorMessage"))) {
					logVO.setErrmsg((String) result.get("errorMessage"));
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��"
							+ result.get("errorMessage") + "��");
				} else {
					logVO.setErrmsg(errmsg);
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
				}
			} else {
				if (StringUtils.isNotBlank(pk_settlement)) {
					EBSBillUtils
							.getUtils()
							.getBaseDAO()
							.executeUpdate(
									"update cmp_detail set def1 = 'Y' where pk_settlement = '"
											+ pk_settlement + "'");
				} else if (StringUtils.isBlank(pk_settlement)) {
					if (finAggvo != null) {// �ƹ˷Ѻ����ʷ�
						finAggvo.getParentVO().setAttributeValue("def34", "Y");
						
						FinancexpenseVO headvo = finAggvo.getParentVO();
						headvo.setDr(0);
						headvo.setStatus(VOStatus.UPDATED);
						getBaseDAO().updateVO(headvo);
						finAggvo = (AggFinancexpenseVO) getBillVO(AggFinancexpenseVO.class,
								"isnull(dr,0)=0 and def19 = '" + finAggvo.getParentVO().getAttributeValue("def19") + "'");
						return finAggvo;
					} else if (repayAggvo != null) {// ������Ϣ
						repayAggvo.getParentVO().setAttributeValue("def34", "Y");
						RePayReceiptBankCreditVO headvo = repayAggvo.getParentVO();
						headvo.setDr(0);
						headvo.setStatus(VOStatus.UPDATED);
						getBaseDAO().updateVO(headvo);
						repayAggvo = (AggRePayReceiptBankCreditVO) getBillVO(AggRePayReceiptBankCreditVO.class,
								"isnull(dr,0)=0 and vdef19 = '" + repayAggvo.getParentVO().getAttributeValue("vdef19") + "'");
						return repayAggvo;
					} else if (addTicketAggvo != null) {// ��Ʊ��
						addTicketAggvo.getParentVO().setAttributeValue("def34", "Y");
						AddTicket headvo = addTicketAggvo.getParentVO();
						headvo.setDr(0);
						headvo.setStatus(VOStatus.UPDATED);
						getBaseDAO().updateVO(headvo);
						addTicketAggvo = (AggAddTicket) getBillVO(AggAddTicket.class,
								"isnull(dr,0)=0 and def19 = '" + addTicketAggvo.getParentVO().getDef19() + "'");
						return addTicketAggvo;
					} else if(marketvo != null){//�ʱ��г�������Ϣ
						MarketRepalayVO parentVO = marketvo.getParentVO();
						parentVO.setDef34("Y");
						parentVO.setDr(0);
						parentVO.setStatus(VOStatus.UPDATED);
						this.getBaseDAO().updateVO(parentVO);
						marketvo = (AggMarketRepalayVO)this.getBillVO(AggMarketRepalayVO.class,
								"isnull(dr,0)=0 and pk_marketreplay = '"+parentVO.getPk_marketreplay()+"'");
						return marketvo;
					}
				}
			}

		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
		}
		return null;

		// AggPayBillVO aggvo = (AggPayBillVO) getBillVO(
		// AggPayBillVO.class,
		// "nvl(dr,0)=0 and pk_paybill = '" + pk_paybill + "'");
	}

	public Object pushRetrieveBpmFile(AggPayBillVO aggvo) throws Exception {
		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(
				IPushBPMBillFileService.class);
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////		String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=PickBackRestart&UserAccount="
				+ usercode;
		String proname = null;
		if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-011")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_011);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-012")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_012);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-016")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_016);
		} else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-017")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_017);
		}else if (aggvo.getParentVO().getAttributeValue("pk_tradetype")
				.equals("F3-Cxx-027")) {
			proname = ISaleBPMBillCont.getBillNameMap().get(
					ISaleBPMBillCont.F3_Cxx_027);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		postdata.put("TaskID", aggvo.getParentVO().getAttributeValue("def55"));
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(proname + "�ջز���");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(json);
		getBaseDAO().insertVO(logVO);

		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");

		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// �����������
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");

		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			String errmsg = null;
			map.put("flag", flag);
			// String openUrl = null;
			if (flag.equals("false")) {
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				if (result.get("errorMessage") != null
						&& !"null".equals(result.get("errorMessage"))) {
					logVO.setErrmsg((String) result.get("errorMessage"));
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��"
							+ result.get("errorMessage") + "��");
				} else {
					logVO.setErrmsg(errmsg);
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
				}
			} else {
				// �ջغ����bpm������Ϣ
				// PayBillVO headvo = (PayBillVO) aggvo.getParentVO();
				aggvo.getParentVO().setStatus(VOStatus.UPDATED);
				// aggvo.getParentVO().setAttributeValue("def55", null);
				aggvo.getParentVO().setAttributeValue("def33", null);// bpm״̬
				aggvo.getParentVO().setAttributeValue("def56", null);//bpm��ַ
				aggvo.getParentVO().setAttributeValue("def60", "Y");// �Ƿ���֪ͨbpm�ջ�
				Object object = getPfBusiAction().processAction(
						IPFActionName.SAVE, "F3", null, aggvo, null, null);
				return object;
			}
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
		}
	}

	/**
	 * nc����ɾ��֪ͨbpm
	 * 
	 * @param bill
	 * @param billcode
	 * @return
	 * @throws Exception
	 */
	public AbstractBill pushToFinBpmDeleteFile(AbstractBill bill,
			String billcode) throws Exception {
		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////		String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=DeleteExt&UserAccount="
				+ usercode;

		String token = "";
		Date date = new Date();
		String app_key = PropertiesUtil.getInstance("salebpm_url.properties")
				.readValue("SALEBPMKEY");
		String app_key_ticket = PropertiesUtil.getInstance(
				"salebpm_url.properties").readValue("SALEBPMTICKET");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");// ������ʱ��
		String time = formater.format(date);
		String tokenkey = app_key + app_key_ticket + time;
		token = MD5Util.getMD5(tokenkey).toUpperCase();

		String proname = null;
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		if (ISaleBPMBillCont.BILL_17.equals(billcode)) {// �ƹ˷�
			proname = ISaleBPMBillCont.getBillNameMap().get(billcode);
			postdata.put("TaskID", bill.getParentVO()
					.getAttributeValue("def19"));
		} else if (ISaleBPMBillCont.BILL_18.equals(billcode)) {// ���ʷ����
			proname = ISaleBPMBillCont.getBillNameMap().get(billcode);
			postdata.put("TaskID", bill.getParentVO()
					.getAttributeValue("def19"));
		} else if (ISaleBPMBillCont.BILL_15.equals(billcode)) {// ����
			proname = ISaleBPMBillCont.getBillNameMap().get(billcode);
			postdata.put("TaskID",
					bill.getParentVO().getAttributeValue("vdef19"));
		} else if (ISaleBPMBillCont.BILL_16.equals(billcode)) {// ��Ϣ
			proname = ISaleBPMBillCont.getBillNameMap().get(billcode);
			postdata.put("TaskID",
					bill.getParentVO().getAttributeValue("vdef19"));
		} else if (ISaleBPMBillCont.BILL_19.equals(billcode)) {// ��Ʊ��
			proname = ISaleBPMBillCont.getBillNameMap().get(billcode);
			postdata.put("TaskID", bill.getParentVO()
					.getAttributeValue("def19"));
		}else if (IBPMBillCont.BILL_36FA.equals(billcode)) {// ���д�������
			proname = IBPMBillCont.getBillNameMap().get(billcode);
			postdata.put("TaskID", bill.getParentVO()
					.getAttributeValue("vdef19"));
		} else if (IBPMBillCont.BILL_RZ04.equals(billcode)) {// ���д�������
			proname = IBPMBillCont.getBillNameMap().get(billcode);
			postdata.put("TaskID", bill.getParentVO()
					.getAttributeValue("def19"));
		} 
		postdata.put("Comments", "ɾ��ԭ��");
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(proname + "ɾ������");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(json);
		getBaseDAO().insertVO(logVO);

		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");

		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// �����������
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");

		// ������Ϣͷϵͳ����
		conn.setRequestProperty("System", "NC");

		// ������Ϣͷtoken
		conn.setRequestProperty("Authorization", token);

		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			String errmsg = null;
			map.put("flag", flag);
			// String openUrl = null;
			if (flag.equals("false")) {
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				if (result.get("errorMessage") != null
						&& !"null".equals(result.get("errorMessage"))) {
					logVO.setErrmsg((String) result.get("errorMessage"));
					getPushBPMBillFile().saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��"
							+ result.get("errorMessage") + "��");
				} else {
					logVO.setErrmsg(errmsg);
					getPushBPMBillFile().saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
				}
			}
			return bill;
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ��");
		}
	}

	IPushBPMBillFileService service = null;

	public IPushBPMBillFileService getPushBPMBillFile() {
		if (service == null) {
			service = NCLocator.getInstance().lookup(
					IPushBPMBillFileService.class);
		}
		return service;
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
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}

	/**
	 * ���ʵ���֪ͨbpm�鵵
	 * @param map
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> pushBPMBillFileNone(Map<String, Object> map,
			String number) throws Exception {

		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map1 = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////		String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=RestoreExt&UserAccount="
				+ usercode;

		String token = "";
		Date date = new Date();
		String app_key = PropertiesUtil.getInstance("salebpm_url.properties")
				.readValue("SALEBPMKEY");
		String app_key_ticket = PropertiesUtil.getInstance(
				"salebpm_url.properties").readValue("SALEBPMTICKET");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");// ������ʱ��
		String time = formater.format(date);
		String tokenkey = app_key + app_key_ticket + time;
		token = MD5Util.getMD5(tokenkey).toUpperCase();

		String proname = null;
		if("0".equals(number)){
			proname = "���������дBPM";
		}else if("1".equals(number)){
			proname = "�ƹ˷ѽ�����дBPM";
		}else if("2".equals(number)){
			proname = "��Ʊ���������дBPM";
		}else if("3".equals(number)){
			proname = "�ʱ��г����������дBPM";
		}
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		postdata.put("ApprovalType", "ProcessApproved");
		postdata.put("OperationType", "none");
		postdata.put("TaskID", map.get("bpmid"));
		postdata.put("Command", "������");
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(proname);
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(json);
		getBaseDAO().insertVO(logVO);

		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");

		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// �����������
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");

		// ������Ϣͷϵͳ����
		conn.setRequestProperty("System", "NC");

		// ������Ϣͷtoken
		conn.setRequestProperty("Authorization", token);

		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			map1.put("flag", flag);
			// String openUrl = null;
			if (flag.equals("false")) {
				return map1;
			} else {// ������سɹ�����bpm��Ϣ����nc������״̬��������,����´�bpm״̬
				// PayBillVO headvo = (PayBillVO) aggvo.getParentVO();
				// headvo.setStatus(VOStatus.UPDATED);
				// headvo.setDef61("Y");
				// getBaseDAO().updateVO(headvo);
				if ("0".equals(number)) {
					AggRePayReceiptBankCreditVO aggvo = (AggRePayReceiptBankCreditVO) getBillVO(
							AggRePayReceiptBankCreditVO.class,
							"pk_repayrcpt = '" + map.get("pk_repayrcpt") + "' ");
					RePayReceiptBankCreditVO headvo = aggvo.getParentVO();
					headvo.setAttributeValue("def39", "Y");
					headvo.setStatus(VOStatus.UPDATED);
					getBaseDAO().updateVO(headvo);
					EBSBillUtils
							.getUtils()
							.getBaseDAO()
							.executeUpdate(
									"update cmp_detail set def1 = 'Y' where pk_settlement = '"
											+ map.get("pk_settlement") + "'");
				}
				if ("1".equals(number)) {
					AggFinancexpenseVO aggvo = (AggFinancexpenseVO) getBillVO(
							AggFinancexpenseVO.class,
							"pk_finexpense = '" + map.get("pk_finexpense") + "' ");
					FinancexpenseVO headvo = aggvo.getParentVO();
					headvo.setAttributeValue("def39", "Y");
					headvo.setStatus(VOStatus.UPDATED);
					getBaseDAO().updateVO(headvo);
					EBSBillUtils
							.getUtils()
							.getBaseDAO()
							.executeUpdate(
									"update cmp_detail set def1 = 'Y' where pk_settlement = '"
											+ map.get("pk_settlement") + "'");
				}
				if ("2".equals(number)) {
					AggAddTicket aggvo = (AggAddTicket) getBillVO(
							AggAddTicket.class, "pk_ticket = '"+map.get("pk_ticket")+"'");
					AddTicket headvo = aggvo.getParentVO();
					headvo.setAttributeValue("def39", "Y");
					headvo.setStatus(VOStatus.UPDATED);
					getBaseDAO().updateVO(headvo);
				}
				if("3".equals(number)){
					AggMarketRepalayVO billVO = (AggMarketRepalayVO) getBillVO(AggMarketRepalayVO.class, "pk_marketreplay = '"+map.get("pk_marketreplay")+"'");
					MarketRepalayVO parentVO = billVO.getParentVO();
					parentVO.setDef39("Y");
					parentVO.setStatus(VOStatus.UPDATED);
					getBaseDAO().updateVO(parentVO);
				}
			}
			return map1;
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
		}
	}

	/**
	 * ɾ�����˻�ʱ֪ͨBPM
	 * @param aggvo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> informBpmBackOrDelete(NcToBpmVO vo)
			throws Exception {
		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(
				IPushBPMBillFileService.class);
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////		String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String action = vo.getApprovaltype();//"back":����bpm��bpm����ǰ�ջ�֪ͨbpm;"delete":NCɾ����֪ͨbpm
		OutsideLogVO logVO = new OutsideLogVO();
		String urls = "";
		try {
			Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
			postdata.put("TaskID", vo.getTaskid());
			String method = "";
			if("delete".equals(action)){
				method = "DeleteExt";
				postdata.put("Comments", "ɾ��ԭ��");
				logVO.setDesbill(vo.getDesbill() + "ɾ������");
			}else if("back".equals(action)){
				method = "PickBackRestart";
				logVO.setDesbill(vo.getDesbill() + "�ջز���");
			}else if("reject".equals(action)){//�˻�BPM��ť
				method = "ReturnToInitiator";
				logVO.setDesbill(vo.getDesbill() + "�˻ز���");
				postdata.put("Comments", "�˻�BPM");
			}else if("query".equals(action)){//����Ȩ�޲�ѯ
				method = "PublicExt";
				logVO.setDesbill("����Ȩ�޲�ѯ");
			}
			logVO.setIsOperInNC(vo.getIsOperInNC());
			logVO.setOperator(usercode);
			urls = address
					+ "/YZSoft/WebService/YZService.ashx?Method="+method+"&UserAccount="
					+ usercode;
			ObjectMapper objectMapper = new ObjectMapper();
			logVO.setPrimaryKey(vo.getPk_busibill());
			logVO.setSrcsystem("BPM");
			logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
			logVO.setExedate(new UFDateTime().toString());
			String json = objectMapper.writeValueAsString(postdata);
			logVO.setSrcparm(json);
			Logger.error("----------����ʼ---------------");
			Logger.error("----------�����������" + json + "��---------------");

			// ����url��Դ
			URL url = new URL(urls);
			// ����http����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// �����������
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// ���ò��û���
			conn.setUseCaches(false);
			// ���ô��ݷ�ʽ
			conn.setRequestMethod("POST");
			// ����ά�ֳ�����
			conn.setRequestProperty("Connection", "Keep-Alive");
			// �����ļ��ַ���:
			conn.setRequestProperty("Charset", "utf-8");
			// ת��Ϊ�ֽ�����
			byte[] data = json.getBytes("utf-8");
			// �����ļ�����
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));

			// �����ļ�����:
			conn.setRequestProperty("Content-Type", "application/json");

			// ��ʼ��������
			conn.connect();
			OutputStream out = conn.getOutputStream();
			// д��������ַ���
			out.write(data);
			out.flush();
			out.close();
			String a = "";
			// ���󷵻ص�״̬
			if (conn.getResponseCode() == 200) {
				Logger.error("���ӳɹ�");
				// ���󷵻ص�����
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				String line = null;
				while ((line = in.readLine()) != null) {
					a += line;
				}
				JSONObject result = new JSONObject(a);
				String flag = (String) result.getString("success");
				logVO.setErrmsg(String.valueOf(result));
				String errmsg = null;
				map.put("flag", flag);
				if (flag.equals("false")) {
					logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
					if (result.get("errorMessage") != null
							&& !"null".equals(result.get("errorMessage"))) {
						logVO.setErrmsg((String) result.get("errorMessage"));
						map.put("errorMessage", result.get("errorMessage"));
						throw new BusinessException("������BPM�Ĵ�����Ϣ��"
								+ result.get("errorMessage") + "��");
					} else {
						logVO.setErrmsg(errmsg);
						throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
					}
				}
			} else {
				Logger.error("����ʧ��");
				throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
			}
		} catch (Exception e) {
			logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
			logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			throw new BusinessException(e.getMessage());
		} finally {
			service.saveLog_RequiresNew(logVO);
		}
		return map;
	}

	/**
	 * ���������ջ�(��bpm�����ӿ�)֪ͨbpm
	 * @param proname
	 * @param bpmid
	 * @throws Exception
	 */
	public void pushRetrieveBpmFile(String proname,String bpmid) throws Exception {
		String usercode = InvocationInfoProxy.getInstance().getUserCode();
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(
				IPushBPMBillFileService.class);
		if (StringUtils.isBlank(usercode)) {
			usercode = "fanyanting";
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";//��ʽ
////		String getUrlSql = "select url from NC_SALEURLPORT where sysid = 'salebpm'";
//		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=AbortExt&UserAccount="
				+ usercode;
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		postdata.put("TaskID", bpmid);
		postdata.put("Comments", "���̳���");
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(proname + "���̳�������");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(json);
		getBaseDAO().insertVO(logVO);

		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");

		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// �����������
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");

		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			String errmsg = null;
			map.put("flag", flag);
			// String openUrl = null;
			if (flag.equals("false")) {
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				if (result.get("errorMessage") != null
						&& !"null".equals(result.get("errorMessage"))) {
					logVO.setErrmsg((String) result.get("errorMessage"));
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��"
							+ result.get("errorMessage") + "��");
				} else {
					logVO.setErrmsg(errmsg);
					service.saveLog_RequiresNew(logVO);
					throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
				}
			} 
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
		}
	}
}
