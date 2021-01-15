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
					postdata.put("pay_method_code", info.get("pay_method_code"));// 付款方式
					postdata.put("currency", info.get("currency"));// 币种
					postdata.put(
							"pay_amount",
							info.get("pay_amount") == null ? null : info.get(
									"pay_amount").toString());// 付款金额
					postdata.put("apply_number", info.get("apply_number"));// ebs付款申请单号
					postdata.put("nc_payment_id", info.get("nc_payment_id"));// ebs付款申请id
					List<UFDouble> mnylist = (List<UFDouble>) info
							.get("line_listData");
					List<Map<String, String>> bodylist = new ArrayList<Map<String, String>>();
					HashMap<String, String> bodyMap = new HashMap<String, String>();
					bodyMap.put(
							"pay_amount",
							info.get("pay_amount") == null ? null : info.get(
									"pay_amount").toString());// 付款金额
					bodyMap.put("fq_amoun", "0");// 付讫金额()
					bodyMap.put("fq_flag", "N");// 付讫标识
					bodyMap.put("fq_date", "");// 付讫日期
					bodylist.add(bodyMap);

					postdata.put("line_listData", bodylist);

					String token = "";
					if ("nc".equals(syscode)) {
						Date date = new Date();
						SimpleDateFormat formater = new SimpleDateFormat(
								"yyyyMMddHHmm");// 年月日时分
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
	 * 付款单付讫查询
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
		// 付款申请单(成本)作废
		BaseDAO dao = new BaseDAO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fq_date = sdf.format(new Date());
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();// 付款申请单数据
		// 结算方式
		String pk_balatype = aggvo.getParentVO().getPk_balatype();
		String pay_method_code = (String) dao.executeQuery(
				"select code from bd_balatype where nvl(dr,0)=0 and pk_balatype ='"
						+ pk_balatype + "'", new ColumnProcessor());
		payapplymap_temp.put("pay_method_code", pay_method_code);// 付款方式
		// payapplymap_temp.put("p_Ncrecpnumber", aggvo.getParentVO()
		// .getAttributeValue("billno"));// 单据号
		// payapplymap_temp.put("p_Paybillid", aggvo.getParentVO()
		// .getAttributeValue("def1"));// ebs主键
		// 币种
		String pk_currtype = aggvo.getParentVO().getPk_currtype();
		String currency = (String) dao.executeQuery(
				"select code from bd_currtype where nvl(dr,0)=0 and pk_currtype ='"
						+ pk_currtype + "'", new ColumnProcessor());
		payapplymap_temp.put("currency", currency);// 币种
		payapplymap_temp.put("pay_amount", "");// 付款金额
		payapplymap_temp.put("apply_number", aggvo.getParentVO().getDef2());// ebs付款申请单号
		payapplymap_temp.put("nc_payment_id", aggvo.getPrimaryKey());// ebs付款申请id
		payapplymap_temp.put("pay_date", fq_date);
		List<Map<String, Object>> bodylist = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> bodyMap = new HashMap<String, Object>();
		bodyMap.put("pay_amount", "");// 付款金额
		bodyMap.put("fq_amount", aggvo.getParentVO().getDef50());// 付讫金额()
		bodyMap.put("fq_flag", "Y");// 付讫标识
		bodyMap.put("fq_date", fq_date);// 付讫日期
		/**
		 * 添加多表体信息返回2020-04-01-谈子健-start
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
		 * 添加多表体信息返回2020-04-01-谈子健-end
		 */
		bodylist.add(bodyMap);

		payapplymap_temp.put("line_listData", bodylist);

		// 数据推送

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
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
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
		// 记录日志nctoebslog
		if (returnjson != null) {
			JSONObject jobj = JSON.parseObject(returnjson);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("付款申请单(成本)作废");
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
	 * SRM付款申请单作废反写Ebs
	 * 
	 * @author Huangxj
	 */
	@Override
	public String pushSrmPayRequestPaid(AggPayrequest aggvo) throws Exception {
		// 付款申请单
		BaseDAO dao = new BaseDAO();
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();

		// 款项类型
		String paymentType = (String) aggvo.getChildrenVO()[0]
				.getAttributeValue("def2");
		String type = null;
		if ("预付款".equals(paymentType)) {
			type = "YPAY";
		} else if ("投标保证金".equals(paymentType)) {
			type = "Y_REFUND";
		} else {
			type = "BZPAY";
		} /*
		 * else if ("预付款退款".equals(paymentType)) { type = "Y_REFUND"; }
		 */

		payapplymap_temp.put("pay_method_code", type);// 付款类型

		payapplymap_temp.put("command", "作废");// 审批意见
		payapplymap_temp.put("operationType", "Cancel");// NC逆向流程操作类型
		payapplymap_temp.put("status", "true");// 流程状态
		payapplymap_temp.put("taskID", "");// BPM流程id
		payapplymap_temp.put("pId", aggvo.getParentVO().getDef1());// 付款申请或预付款申请id

		//
		String address = OutsideUtils.getOutsideInfo("EBS0301");
		// String urls = address + "/postMethod?" + params;
		String data = getjson(payapplymap_temp);

		String receive = null;
		// 创建post方式请求对象
		PostMethod method = new PostMethod(address);
		try {

			// 创建httpclient对象
			HttpClient client = new HttpClient();

			// 设置参数到请求对象中
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
				logVO.setTaskname("付款申请单(SRM)作废");
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
					throw new BusinessException("EBS反馈异常:" + msg);
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
	 * 付款申请单付讫查询
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
		// 付款申请单(通用)作废
		BaseDAO dao = new BaseDAO();
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String token = "";
		String tokenkey = "";
		String url = OutsideUtils.getOutsideInfo("EBS0206");
		HashMap<String, Object> payapplymap = new HashMap<String, Object>();// 付款申请单数据
		List<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
		for (nc.vo.pub.CircularlyAccessibleValueObject vo : aggvo
				.getChildrenVO()) {
			Business_b bvo = (Business_b) vo;
			String def46 = bvo.getDef46();
			UFDouble def46ufDouble = new UFDouble(def46);
			if (def46 != null && !(def46ufDouble.isTrimZero())) {
				HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();
				payapplymap_temp.put("p_Ispaid", "Y");// 是否付讫
				/**
				 * 实际收款供应商和实际收款银行账户改为code传给ebs-谈子健-2020-03-31-start
				 */
				Object supplier = dao.executeQuery(
						"select code from bd_supplier where pk_supplier= '"
								+ bvo.getSupplier() + "'",
						new ColumnProcessor());
				// add by huangxj 2020年8月10日
				payapplymap_temp.put("p_Payeetype", bvo.getObjtype());// 往来对象
				// end
				if (bvo.getObjtype() == 3) {
					Object psn = dao.executeQuery(
							"select code from bd_psndoc where pk_psndoc=  '"
									+ bvo.getDef58() + "'",
							new ColumnProcessor());
					payapplymap_temp.put("p_Actreceiver", psn);// 人员
				} else {
					payapplymap_temp.put("p_Actreceiver", supplier);// 实际收款供应商
				}
				Object accnum = dao.executeQuery(
						"select bd_bankaccsub.accnum from bd_bankaccsub where pk_bankaccsub =  '"
								+ bvo.getDef15() + "'", new ColumnProcessor());
				payapplymap_temp.put("p_Actrevaccount", accnum);// 实际收款银行账户
				/**
				 * 实际收款方和实际收款银行账户改为code传给ebs-谈子健-2020-03-31-end
				 */
				/**
				 * 添加付讫金额2020-04-01-谈子健-start
				 */
				payapplymap_temp.put("p_Nopaidamount", def46ufDouble);// 付讫金额
				/**
				 * 添加付讫金额2020-04-01-谈子健-end
				 */
				payapplymap_temp.put("p_Actpaymoney", 0);// 实际付款金额
				/**
				 * 添加付讫金额2020-04-01-谈子健-end
				 */

				if (aggvo.getParentVO().getAttributeValue("billdate") != null) {
					UFDate uf = (UFDate) aggvo.getParentVO().getAttributeValue(
							"billdate");// 单据日期
					String s = uf.getYear() + "-" + uf.getMonth() + "-"
							+ uf.getDay();
					payapplymap_temp.put("p_Actpaydate", s);// 单据日期
				}
				lists.add(payapplymap_temp);
			}
		}
		payapplymap.put("p_Contractid",
				Integer.valueOf(aggvo.getParentVO().getDef11()));// 合同id
		payapplymap.put(
				"p_Paybillid",
				Integer.valueOf(aggvo.getParentVO().getAttributeValue("def1")
						+ ""));// ebs主键
		payapplymap.put("p_Ncrecpnumber", aggvo.getParentVO()
				.getAttributeValue("billno"));// 单据号
		payapplymap.put("Actualreceiver", lists);
		// 添加唯一标识付讫时因为没有结算单我们就使用付款申请单的pk做唯一标识
		payapplymap.put("p_Pay_Key", aggvo.getPrimaryKey().toString());// 结算单主键
		// 数据推送
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		String param = getjson(payapplymap);
		tokenkey = param + key;
		token = EncoderByMd5(tokenkey);
		String returnjson = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// 记录日志nctoebslog
		if (returnjson != null) {
			JSONObject jobj = JSON.parseObject(returnjson);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("付款申请单(通用)作废");
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
	 * map转json
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
		// TODO 自动生成的方法存根
		// String url = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("PAYURL");// ebs付款完成接口地址
		String url = OutsideUtils.getOutsideInfo("EBS0206");
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		List<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> paymap = new HashMap<String, Object>();// 付款单map
		for (nc.vo.pub.CircularlyAccessibleValueObject cvo : aggvo_pay
				.getChildrenVO()) {
			paymap = paydata(aggvo_pay, cvo);
			if (paymap != null)
				lists.add(paymap);
		}
		datamap.put("p_Contractid",
				aggvo_pay.getParentVO().getAttributeValue("def5"));// 合同编码
		datamap.put("p_Paybillid",
				aggvo_pay.getParentVO().getAttributeValue("def1"));// ebs主键
		datamap.put("p_Ncrecpnumber", aggvo_pay.getParentVO()
				.getAttributeValue("billno"));// 单据号
		String param = JSON.toJSONString(datamap);
		String jsondata = Httpconnectionutil.newinstance().connection(url,
				param);
		return jsondata;
	}

	/*
	 * 付款单数据
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
				.getAttributeValue("def29"));// 是否作废
		mappay.put("p_Actreceiver", query.executeQuery(
				"select name from bd_supplier where pk_supplier= gitem.supplier"
						+ itemvo.getSupplier(), new ColumnProcessor()));// 供应商
		mappay.put("p_Actrevaccount", itemvo.getDef5());// 实际收款方银行账户
		mappay.put("p_Actpaymoney", itemvo.getLocal_money_de());
		mappay.put("p_Actpaydate",
				aggvopay.getParentVO().getAttributeValue("billdate"));
		return mappay;
	}

	@Override
	public String pushEBSthPayrequest(AggPayrequest aggvo)
			throws BusinessException {
		// TODO 自动生成的方法存根
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		// String url = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("YF_BACK_URL");// ebs退回接口地址
		String url = OutsideUtils.getOutsideInfo("EBS0202");
		Object ebsID = aggvo.getParentVO().getAttributeValue("def1");
		if (ebsID == null) {
			throw new BusinessException("EBS id为空");
		}
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS 地区财务审核状态为空");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def48");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("EBS 退回原因为空");
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
		// 记录日志nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("付款申请单(通用)退回");
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
		// .readValue("COSTRETURNURL");// ebs退回接口地址
		String url = OutsideUtils.getOutsideInfo("EBS0103");// ebs退回接口地址
		Object ebsID = aggvo.getParentVO().getAttributeValue("def1");
		if (ebsID == null) {
			throw new BusinessException("EBS id为空");
		}
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS 地区财务审核状态为空");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def48");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("EBS 退回原因为空");
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
		// 记录日志nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("付款申请单(成本)退回");
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
	 * 应付单单退回(成本) 2019-12-25-tzj
	 */
	@Override
	public String costPushEBSPayablerequest(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException {

		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String url = "";
		Object ebsID = aggvo.getParentVO().getAttributeValue("def1");
		if (ebsID == null) {
			throw new BusinessException("EBS id为空");
		}
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS 地区财务审核状态为空");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def53");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("EBS 退回原因为空");
		}
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		String fpaid = "";
		if (ebsID != null) {
			fpaid = ebsID.toString();
		}
		/**
		 * 退回时添加如果是仅补票的调用仅补票接口2020-04-03-谈子健-start
		 */
		String def55 = (String) aggvo.getParent().getAttributeValue("def55");
		if (def55 != null && "仅补票".equals(def55)) {
			// 外系统单据号
			datamap.put("apply_number",
					aggvo.getParent().getAttributeValue("def2"));
			// 应付单主键
			datamap.put("nc_payment_id", aggvo.getPrimaryKey());
			// 退回或审批通过标识 通过为Y 退回为N
			datamap.put("flag", "N");
			// 回退原因
			datamap.put("reason", reasons);
			// url = PropertiesUtil.getInstance("ebs_url.properties").readValue(
			// "COSTINVOICETOEBSURL");
			url = OutsideUtils.getOutsideInfo("EBS0105");
		} else {
			datamap.put("FPAID", fpaid);
			datamap.put("RETURN_FROM_STATUS", finstatusCode);
			datamap.put("RETURN_REASON", reasons);
			// url = PropertiesUtil.getInstance("ebs_url.properties").readValue(
			// "COSTRETURNURL");// ebs退回接口地址
			url = OutsideUtils.getOutsideInfo("EBS0103");
		}
		/**
		 * 退回时添加如果是仅补票的调用仅补票接口2020-04-03-谈子健-end
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
		// 回收并删除2020-03-25-谈子健
		UnSaveBillAndDelete(aggvo, aggVO);

		String jsondata = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// 记录日志nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("应付单(成本)退回");
			logVO.setNcparm("加密前token:" + tokenkey + "，加密后token：" + token
					+ "；报文：" + param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg("EBS回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
			} else {
				logVO.setMsg("EBS回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				throw new BusinessException("EBS回写信息:" + msg);
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
			throw new BusinessException("EBS id为空");
		}
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS 地区财务审核状态为空");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def53");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("EBS 退回原因为空");
		}
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		/**
		 * 退回时添加如果是仅补票的调用仅补票接口2020-04-03-谈子健-start
		 */
		String def55 = (String) aggvo.getParent().getAttributeValue("def55");
		if (def55 != null && "仅补票".equals(def55)) {
			// 外系统单据号
			datamap.put("apply_number",
					aggvo.getParent().getAttributeValue("def2"));
			// 应付单主键
			datamap.put("nc_payment_id", aggvo.getPrimaryKey());
			// 退回或审批通过标识 通过为Y 退回为N
			datamap.put("flag", "N");
			// 回退原因
			datamap.put("reason", reasons);
			// url = PropertiesUtil.getInstance("ebs_url.properties").readValue(
			// "TRANINVOICETOEBSURL");
			url = OutsideUtils.getOutsideInfo("EBS0203");
		} else {
			datamap.put("FPAID", Integer.valueOf(ebsID.toString()));
			datamap.put("RETURN_FROM_STATUS", finstatusCode);
			datamap.put("RETURN_REASON", reasons);
			// url = PropertiesUtil.getInstance("ebs_url.properties").readValue(
			// "YF_BACK_URL");// ebs退回接口地址
			url = OutsideUtils.getOutsideInfo("EBS0202");

		}
		/**
		 * 退回时添加如果是仅补票的调用仅补票接口2020-04-03-谈子健-end
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
		// 回收并删除2020-03-25-谈子健
		UnSaveBillAndDelete(aggvo, aggVO);

		String jsondata = Httpconnectionutil.newinstance().connection(
				url + token, "&req=" + param);
		// String jsondata = Httpconnectionutil.newinstance().connection(
		// url, "&req=" + param);
		// 记录日志nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("应付单(通用)退回");
			logVO.setNcparm(param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg("EBS回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
			} else {
				logVO.setMsg("EBS回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				throw new BusinessException("EBS回写信息:" + msg);
			}

		}

		return jsondata;
	}

	/**
	 * 退保证金回写
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
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();// 进项发票数据
		payapplymap_temp.put("refundStatus", aggvo.getParentVO()
				.getAttributeValue("def23"));// 退款状态
		payapplymap_temp.put("pkOrg", map_org.get("code"));// NC财务组织
		payapplymap_temp.put("orgName", map_org.get("name"));// NC财务组织
		payapplymap_temp.put("vendorName", map_customer.get("name"));// 供应商名称
		payapplymap_temp.put("vendorNumber", map_customer.get("code"));// 供应商编码
		payapplymap_temp.put("agreementNumber", aggvo.getParentVO()
				.getAttributeValue("def5"));// 协议编号
		payapplymap_temp.put("paymentDate", aggvo.getParentVO()
				.getAttributeValue("billdate").toString().substring(0, 10));// 退款日期
		payapplymap_temp.put("payMethodCode", balatypecode);// 付款方法
		payapplymap_temp.put("currency", currtypecode);// 币种

		UFDouble mny = (UFDouble) aggvo.getParentVO()
				.getAttributeValue("money");
		payapplymap_temp.put("paymentAmount", mny != null ? mny.multiply(-1)
				.toString() : "0");// 付款金额
		payapplymap_temp
				.put("ncPaymentId", aggvo.getParentVO().getPrimaryKey());// NC单据ID
		payapplymap_temp.put("ncPaymentNumber", aggvo.getParentVO()
				.getAttributeValue("billno"));// NC单据编号
		payapplymap_temp.put("applyNumber", aggvo.getParentVO()
				.getAttributeValue("def2"));// 预付款申请单号

		// 数据推送

		String syscode = PropertiesUtil.getInstance("srm_url.properties")
				.readValue("SRMSYSTEM");
		String key = PropertiesUtil.getInstance("srm_url.properties")
				.readValue("KEY");
		String token = "";
		if ("nc".equals(syscode)) {
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
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
		// 记录日志nctoebslog
		if (returnjson != null) {
			JSONObject jobj = JSON.parseObject(returnjson);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("退保证金回写");
			logVO.setNcparm(getjson(payapplymap_temp));
			logVO.setSrcsystem("SRM");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(flag)) {
				logVO.setResult(flag);
				logVO.setMsg("SRM回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
			} else {
				logVO.setMsg("SRM回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			}
			logVO.setDr(0);
			saveLog.SaveLog_RequiresNew(logVO);
		}
		return returnjson;
	}

	/**
	 * 读取业务单据聚合VO
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
	 * 元数据持久化查询接口
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
		// 进行删除付款申请单和应付单的操作2020-03-25-谈子健 -start
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
								eParam);// 收回付款申请单
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
	 * 付款申请单通过表头主键查询多表体2020-03-30
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
	 * 应付单审批通过仅补票推ebs(成本) 2020-04-03-谈子健
	 */
	@Override
	public String CostInvoiceSupplement(AggPayableBillVO aggvo)
			throws BusinessException {
		// 是否已回写标识
		String def63 = (String) aggvo.getParentVO().getAttributeValue("def63");
		String jsondata = "";
		if (!"Y".equals(def63)) {
			// throw new BusinessException("该单据已回写成功ebs不能重复回写!");
			NcToEbsLogVO logVO = new NcToEbsLogVO();
			ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
			// String url = PropertiesUtil.getInstance("ebs_url.properties")
			// .readValue("COSTINVOICETOEBSURL");// ebs仅补票接口地址
			String url = OutsideUtils.getOutsideInfo("EBS0105");

			HashMap<String, Object> datamap = new HashMap<String, Object>();
			// 外系统单据号
			datamap.put("apply_number",
					aggvo.getParent().getAttributeValue("def2"));
			// 应付单主键
			datamap.put("nc_payment_id", aggvo.getPrimaryKey());
			// 退回或审批通过标识 通过为Y 退回为N
			datamap.put("flag", "Y");
			// 回退原因
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
			// 记录日志nctoebslog
			if (jsondata != null) {
				JSONObject jobj = JSON.parseObject(jsondata);
				String flag = jobj.getString("code");
				String msg = jobj.getString("msg");
				logVO.setTaskname("应付单仅补票推EBS(成本)");
				logVO.setNcparm(param);
				logVO.setSrcsystem("EBS");
				logVO.setOperator("NC");
				logVO.setExedate(new UFDateTime().toString());
				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg("EBS回写信息:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
					logVO.setDr(0);
					saveLog.SaveLog_RequiresNew(logVO);
					// 如果回写成功标记上标识-2020-06-09-谈子健
					insertFlag(aggvo.getPrimaryKey());
				} else {
					logVO.setMsg("EBS回写信息:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
					logVO.setDr(0);
					saveLog.SaveLog_RequiresNew(logVO);
					throw new BusinessException("EBS回写信息:" + msg);
				}

			}
			return jsondata;
		}
		return jsondata;
	}

	/**
	 * 应付单审批通过仅补票推ebs(通用) 2020-04-03-谈子健
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
			// .readValue("TRANINVOICETOEBSURL");// ebs仅补票接口地址
			String url = OutsideUtils.getOutsideInfo("EBS0203");
			HashMap<String, Object> datamap = new HashMap<String, Object>();
			// 外系统单据号
			datamap.put("apply_number",
					aggvo.getParent().getAttributeValue("def2"));
			// 应付单主键
			datamap.put("nc_payment_id", aggvo.getPrimaryKey());
			// 退回或审批通过标识 通过为Y 退回为N
			datamap.put("flag", "Y");
			// 回退原因
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
			// 记录日志nctoebslog
			if (jsondata != null) {
				JSONObject jobj = JSON.parseObject(jsondata);
				String flag = jobj.getString("code");
				String msg = jobj.getString("msg");
				logVO.setTaskname("应付单仅补票推EBS(通用)");
				logVO.setNcparm(param);
				logVO.setSrcsystem("EBS");
				logVO.setOperator("NC");
				logVO.setExedate(new UFDateTime().toString());
				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg("EBS回写信息:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
					logVO.setDr(0);
					saveLog.SaveLog_RequiresNew(logVO);
					// 如果回写成功标记上标识-2020-06-09-谈子健
					insertFlag(aggvo.getPrimaryKey());
				} else {
					logVO.setMsg("EBS回写信息:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
					logVO.setDr(0);
					saveLog.SaveLog_RequiresNew(logVO);
					throw new BusinessException("EBS回写信息:" + msg);
				}

			}

			return jsondata;
		}
		return jsondata;
	}

	/**
	 * SRM退回回写
	 */
	@Override
	public String pushSRMthPayrequest(AggPayrequest aggvo) throws Exception {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		Object ebsID = aggvo.getParentVO().getAttributeValue("def1");
		if (ebsID == null) {
			throw new BusinessException("SRM id为空");
		}
		String reasons = (String) aggvo.getParentVO()
				.getAttributeValue("def48");
		if (reasons == null || "".equals(reasons)) {
			throw new BusinessException("SRM 退回原因为空");
		}
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();

		// 款项类型
		String paymentType = (String) aggvo.getChildrenVO()[0]
				.getAttributeValue("def2");

		String type = null;
		if ("预付款".equals(paymentType)) {
			type = "YPAY";
		} else if ("投标保证金".equals(paymentType)) {
			type = "Y_REFUND";
		} else {
			type = "BZPAY";
		} /*
		 * else if ("预付款退款".equals(paymentType)) { type = "Y_REFUND"; }
		 */
		payapplymap_temp.put("paymentType", type);// 付款类型

		payapplymap_temp.put("command", aggvo.getParentVO().getDef48());// 审批意见
		payapplymap_temp.put("operationType", "SrmBack");// NC逆向流程操作类型
		payapplymap_temp.put("status", "false");// 流程状态
		payapplymap_temp.put("taskID", "");// BPM流程id
		payapplymap_temp.put("pId", aggvo.getParentVO().getDef1());// 付款申请或预付款申请id

		String address = OutsideUtils.getOutsideInfo("EBS0301");
		String data = getjson(payapplymap_temp);

		String receive = null;
		// 创建post方式请求对象
		PostMethod method = new PostMethod(address);
		try {

			// 创建httpclient对象
			HttpClient client = new HttpClient();

			// 设置参数到请求对象中
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
				logVO.setTaskname("付款申请单(SRM)退回");
				logVO.setNcparm(data);
				logVO.setSrcsystem("SRM");
				logVO.setOperator("NC");
				logVO.setExedate(new UFDateTime().toString());
				logVO.setDr(0);

				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg("SRM回写信息:" + msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				} else {
					throw new BusinessException("EBS反馈异常:" + msg);
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
	 * 开票工单开票 huangxj
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
		// 日志记录url信息
		logvo.setDesbill("供应链数据传入乐税");
		JSONObject jsonobject = new JSONObject();

		StringBuilder sb = new StringBuilder();
		try {
			// 组装开票数据
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
			// 设置不用缓存
			connection.setUseCaches(false);
			// 设置传递方式
			connection.setRequestMethod("POST");
			// 设置维持长连接
			connection.setRequestProperty("Connection", "Keep-Alive");
			// 设置文件字符集:
			connection.setRequestProperty("Charset", "UTF-8");
			// 设置文件类型:
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
			// 访问状态
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
					throw new BusinessException("乐税返回信息：" + sb);
				} else {
					logvo.setErrmsg("乐税回传信息:" + sb.toString());
					logvo.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				}

				// 开票信息回写
				// aggvo =writeBackInvoiseMsg(sb.toString(),aggvo,negativeFlag);
				// returnMap.put("aggvo", aggvo);
				returnMap.put("msg", sb.toString());
				return sb.toString();
			}
		} catch (Exception e) {
			returnMap.put("msg", e);
			rewriteLog.setDef2(e.getMessage());
			e.printStackTrace();
			logvo.setErrmsg("调用乐税接口异常:" + e.getMessage());
			logvo.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			throw new Exception("调用乐税接口异常:" + e.getMessage());
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
			// 记录开票日志
		}
		return sb.toString();
	}

	/**
	 * 占预算+扣税差应付单审批通过推ebs(成本) 2020-06-05-谈子健
	 */
	@Override
	public String CostBudgetTaxdifferencePushEbs(AggPayableBillVO aggvo,
			String flag) throws BusinessException {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String url = OutsideUtils.getOutsideInfo("EBS0106");
		// 是否已回写标识
		String def63 = (String) aggvo.getParentVO().getAttributeValue("def63");
		if ("Y".equals(def63)) {
			throw new BusinessException("该单据已回写成功ebs不能重复回写!");
		}
		// 如果标识为N就是退回动作
		String reasons = "";
		String apply_number = (String) aggvo.getParentVO().getAttributeValue(
				"def2");
		String billid = aggvo.getPrimaryKey();
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue(
				"pk_tradetype");
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS 地区财务审核状态为空");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		if ("N".equals(flag)) {
			reasons = (String) aggvo.getParentVO().getAttributeValue("def53");
			if (reasons == null || "".equals(reasons)) {
				throw new BusinessException("EBS 退回原因为空");
			}
			UnSaveBillAndDelete(aggvo, null);
		}

		// {"APPLY_NUMBER": "100101258.123-00119", "NC_PAYMENT_ID":
		// "123421133","FLAG":"Y","REASON":"因为发票不合格" }

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		// 外系统单据号
		datamap.put("APPLY_NUMBER", apply_number);
		// 应付单主键
		datamap.put("NC_PAYMENT_ID", billid);
		// 退回或审批通过标识 通过为Y 退回为N
		datamap.put("FLAG", flag);
		String def55 = (String) aggvo.getParent().getAttributeValue("def55");

		// 地区财务审批状态
		if ("仅补票".equals(def55)) {
			datamap.put("RETURN_FROM_STATUS", "ticketOnly");
		} else {
			datamap.put("RETURN_FROM_STATUS", finstatusCode);
		}
		// 回退原因
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
		// 记录日志nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String code = jobj.getString("code");
			String msg = jobj.getString("msg");
			/**
			 * F1-Cxx-010,成本税差应付单 F1-Cxx-011,成本占预算应付单
			 */
			if ("F1-Cxx-010".equals(pk_tradetype)) {
				if ("Y".equals(flag)) {
					logVO.setTaskname("成本税差应付单审批通过推ebs(成本)");
				} else {
					logVO.setTaskname("成本税差应付单退回推ebs(成本)");
				}

			}
			if ("F1-Cxx-011".equals(pk_tradetype)) {
				if ("Y".equals(flag)) {
					logVO.setTaskname("成本占预算应付单审批通过推ebs(成本)");
				} else {
					logVO.setTaskname("成本占预算应付单退回推ebs(成本)");
				}

			}

			logVO.setNcparm(param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(code)) {
				logVO.setResult(code);
				logVO.setMsg("EBS回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				if ("Y".equals(flag)) {
					// 如果回写成功标记上标识-2020-06-09-谈子健
					insertFlag(billid);
				}
			} else {
				logVO.setMsg("EBS回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				throw new BusinessException("EBS回写信息:" + msg);
			}

		}

		return jsondata;

	}

	/**
	 * 占预算+扣税差应付单审批通过推ebs(通用)
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
		// 是否已回写标识
		String def63 = (String) aggvo.getParentVO().getAttributeValue("def63");
		if ("Y".equals(def63)) {
			throw new BusinessException("该单据已回写成功ebs不能重复回写!");
		}
		// 如果标识为N就是退回动作
		String reasons = "";
		String ebsid = (String) aggvo.getParentVO().getAttributeValue("def1");
		String billid = aggvo.getPrimaryKey();
		String pk_tradetype = (String) aggvo.getParentVO().getAttributeValue(
				"pk_tradetype");
		String finstatus = (String) aggvo.getParentVO().getAttributeValue(
				"def33");
		if (finstatus == null) {
			throw new BusinessException("EBS 地区财务审核状态为空");
		}
		String sql = "select d.code from bd_defdoc d where d.pk_defdoc = '"
				+ finstatus + "' and dr = 0 and enablestate = 2";
		String finstatusCode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new ColumnProcessor());
		if ("N".equals(flag)) {
			reasons = (String) aggvo.getParentVO().getAttributeValue("def53");
			if (reasons == null || "".equals(reasons)) {
				throw new BusinessException("EBS 退回原因为空");
			}
			UnSaveBillAndDelete(aggvo, null);
		}

		HashMap<String, Object> datamap = new HashMap<String, Object>();
		if ("N".equals(flag)) {
			// 外系统主键
			datamap.put("FPAID", Integer.valueOf(ebsid));
			// 地区财务审批状态
			datamap.put("RETURN_FROM_STATUS", "ticketOnly");
			// 回退原因
			datamap.put("RETURN_REASON", reasons);
		} else {
			// 外系统主键
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
		// 记录日志nctoebslog
		if (jsondata != null) {
			JSONObject jobj = JSON.parseObject(jsondata);
			String code = jobj.getString("code");
			String msg = jobj.getString("msg");
			/**
			 * F1-Cxx-012,通用税差应付单 F1-Cxx-013,通用占预算应付单
			 */
			if ("F1-Cxx-012".equals(pk_tradetype)) {
				if ("Y".equals(flag)) {
					logVO.setTaskname("通用税差应付单审批通过推ebs(通用)");
				} else {
					logVO.setTaskname("通用税差应付单退回推ebs(通用)");
				}

			}
			if ("F1-Cxx-013".equals(pk_tradetype)) {
				if ("Y".equals(flag)) {
					logVO.setTaskname("通用占预算应付单审批通过推ebs(通用)");
				} else {
					logVO.setTaskname("通用占预算应付单退回推ebs(通用)");
				}

			}

			logVO.setNcparm(param);
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			if ("S".equals(code)) {
				logVO.setResult(code);
				logVO.setMsg("EBS回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				if ("Y".equals(flag)) {
					// 如果回写成功标记上标识-2020-06-09-谈子健
					insertFlag(billid);
				}
			} else {
				logVO.setMsg("EBS回写信息:" + msg);
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
				throw new BusinessException("EBS回写信息:" + msg);
			}

		}

		return jsondata;

	}

	/**
	 * 已回写需标上标识
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
