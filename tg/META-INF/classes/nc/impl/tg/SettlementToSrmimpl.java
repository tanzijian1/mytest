package nc.impl.tg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.alter.plugin.ebs.Httpconnectionutil;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISaveSrmLog;
import nc.itf.tg.ISettlementToSRM;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.NcToSrmLogVO;
import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SettlementToSrmimpl implements ISettlementToSRM {
	/**
	 * 核销回写srm 2020-03-21-谈子健
	 */
	@Override
	public String SendSettlementToSRM(CircularlyAccessibleValueObject bvo)
			throws BusinessException {
		ISaveSrmLog saveLog = NCLocator.getInstance().lookup(ISaveSrmLog.class);
		NcToSrmLogVO logvo = new NcToSrmLogVO();
		String json = null;
		String returnjson = null;
		String billid = (String) bvo.getAttributeValue("pk_bill");
		List<Map<String, String>> list = getDate(billid);
		String userId = InvocationInfoProxy.getInstance().getUserId();
		String operator = getwriteOffOperator(userId);
		HashMap<String, Object> returnDate = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			for (Map<String, String> map : list) {
				returnDate.put("depositNumber", map.get("depositnumber"));// 保证金编号
				returnDate.put("vendorNumber", map.get("vendornumber"));// 供应商编码
				returnDate.put("writeOffStatus", "已核销");// 核销状态
				returnDate.put("ncDocumentId", map.get("ncdocumentid"));// NC单据ID
				returnDate.put("ncDocumentNumber", map.get("ncdocumentnumber"));// NC单据编号

				returnDate.put("writeOffDate", new SimpleDateFormat(
						"YYYY-MM-dd").format(new Date()).toString());// 核销日期
				returnDate.put("writeOffAmount", map.get("writeoffamount"));// 核销金额
																			// 当点击“核销”按钮，传正数金额，当点击“取消核销”按钮，传负数金额。金额取应收单对应的表头金额money
				returnDate.put("writeOffOperator", operator);// 核销人编号 当前用户的编号

			}
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				json = objectMapper.writeValueAsString(returnDate);
			} catch (JsonProcessingException e) {
				logvo.setMsg(e.getMessage());
				logvo.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				e.printStackTrace();
			}

			logvo.setTaskname("NC回传的投标保证金的核销");
			logvo.setSrcsystem("SRM");
			logvo.setOperator("NC");
			String url = OutsideUtils.getOutsideInfo("EBS0302");
			String key = OutsideUtils.getOutsideInfo("EBS-SRM-KEY");
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
			String time = formater.format(date);
			String tokenkey = time + key;
			String token = MD5Util.getMD5(tokenkey).toUpperCase();
			logvo.setNcparm("未加密token:" + tokenkey + "外系统EBS地址:" + url
					+ "加密后的token:" + token + "NC报文:" + json);
			returnjson = Httpconnectionutil.newinstance().connection(
					url + token, "&req=" + json);
			if (returnjson != null) {
				JSONObject jobj = JSON.parseObject(returnjson);
				String flag = jobj.getString("code");
				String msg = jobj.getString("msg");

				logvo.setExedate(new UFDateTime().toString());
				if ("S".equals(flag)) {
					logvo.setResult(flag);
					logvo.setMsg("srm系统信息:" + msg);
					logvo.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				} else {
					logvo.setMsg("srm系统信息:" + msg);
					logvo.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
					saveLog.SaveSrmLog_RequiresNew(logvo);
					throw new BusinessException("srm系统信息:" + msg);
				}
			}
		}
		logvo.setDr(0);
		saveLog.SaveSrmLog_RequiresNew(logvo);
		return returnjson;
	}

	/**
	 * 反核销回写srm 2020-03-21-谈子健
	 */
	@Override
	public String SendCancelSettlementToSRM(CircularlyAccessibleValueObject bvo)
			throws BusinessException {
		ISaveSrmLog saveLog = NCLocator.getInstance().lookup(ISaveSrmLog.class);
		NcToSrmLogVO logvo = new NcToSrmLogVO();
		String json = null;
		String returnjson = null;
		String billid = (String) bvo.getAttributeValue("pk_bill");
		List<Map<String, String>> list = getDate(billid);
		String userId = InvocationInfoProxy.getInstance().getUserId();
		String operator = getwriteOffOperator(userId);
		HashMap<String, Object> returnDate = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			for (Map<String, String> map : list) {
				returnDate.put("depositNumber", map.get("depositnumber"));// 保证金编号
				returnDate.put("vendorNumber", map.get("vendornumber"));// 供应商编码
				returnDate.put("writeOffStatus", "未核销");// 核销状态
				returnDate.put("ncDocumentId", map.get("ncdocumentid"));// NC单据ID
				returnDate.put("ncDocumentNumber", map.get("ncdocumentnumber"));// NC单据编号

				returnDate.put("writeOffDate", new SimpleDateFormat(
						"YYYY-MM-dd").format(new Date()).toString());// 核销日期
				// 反核销金额设置为负数
				Integer writeoffamount = Integer.parseInt(String.valueOf(map
						.get("writeoffamount")));
				returnDate.put("writeOffAmount",
						"-" + writeoffamount.toString());// 核销金额
				returnDate.put("writeOffOperator", operator);// 核销人编号 当前用户的编号

			}
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				json = objectMapper.writeValueAsString(returnDate);
			} catch (JsonProcessingException e) {
				logvo.setMsg(e.getMessage());
				logvo.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				e.printStackTrace();
			}

			logvo.setTaskname("NC反核销回传的投标保证金的核销");
			logvo.setSrcsystem("SRM");
			logvo.setOperator("NC");
			String url = OutsideUtils.getOutsideInfo("EBS0302");
			String key = OutsideUtils.getOutsideInfo("EBS-SRM-KEY");
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
			String time = formater.format(date);
			String tokenkey = time + key;
			String token = MD5Util.getMD5(tokenkey).toUpperCase();
			logvo.setNcparm("未加密token:" + tokenkey + "外系统EBS地址:" + url
					+ "加密后的token:" + token + "NC报文:" + json);
			returnjson = Httpconnectionutil.newinstance().connection(
					url + token, "&req=" + json);
			if (returnjson != null) {
				JSONObject jobj = JSON.parseObject(returnjson);
				String flag = jobj.getString("code");
				String msg = jobj.getString("msg");

				logvo.setExedate(new UFDateTime().toString());
				if ("S".equals(flag)) {
					logvo.setResult(flag);
					logvo.setMsg("srm系统信息:" + msg);
					logvo.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				} else {
					logvo.setMsg("srm系统信息:" + msg);
					logvo.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
					saveLog.SaveSrmLog_RequiresNew(logvo);
					throw new BusinessException("srm系统信息:" + msg);
				}
			}
		}
		logvo.setDr(0);
		saveLog.SaveSrmLog_RequiresNew(logvo);
		return returnjson;
	}

	private List<Map<String, String>> getDate(String billid)
			throws BusinessException {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		StringBuffer query = new StringBuffer();
		query.append("select r.def2       as depositNumber,  ");
		query.append("       c.code   as vendorNumber,  ");
		query.append("       r.pk_recbill as ncDocumentId,  ");
		query.append("       r.billno     as ncDocumentNumber,  ");
		query.append("       r.money      as writeOffAmount  ");
		query.append("  from ar_recbill r  ");
		query.append("  left join ar_recitem a  ");
		query.append("    on r.pk_recbill = a.pk_recbill  ");
		query.append("    left join bd_customer c  ");
		query.append("    on a.customer = c.pk_customer  ");
		query.append(" where r.pk_recbill = '" + billid + "'  ");
		query.append("   and r.dr = 0  ");
		query.append("   and a.dr = 0  ");
		BaseDAO dao = new BaseDAO();
		list = (ArrayList<Map<String, String>>) dao.executeQuery(
				query.toString(), new MapListProcessor());
		return list;
	}

	/**
	 * 通过用户id找员人编码 2020-04-21-谈子健
	 * 
	 * @throws BusinessException
	 */
	private String getwriteOffOperator(String userid) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		StringBuffer query = new StringBuffer();
		query.append("select b.code  ");
		query.append("  from bd_psndoc b  ");
		query.append("  left join sm_user s  ");
		query.append("    on b.pk_psndoc = s.pk_psndoc  ");
		query.append(" where s.cuserid = '" + userid + "'  ");
		query.append("   and b.dr = 0  ");
		query.append("   and s.dr = 0  ");
		query.append("   and b.enablestate = 2  ");
		String code = (String) dao.executeQuery(query.toString(),
				new ColumnProcessor());
		return code;
	}
}
