package nc.bs.tg.alter.plugin.ebs;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.NcToEbsLogVO;

import org.apache.tools.ant.BuildException;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 回写水电费信息给ebs 2020-04-20
 * 
 * @author 谈子健
 * 
 */
public class AoutSysncEbsWaterAndElectricityData extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		StringBuffer errorsb = new StringBuffer();
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = null;

		String url = "";
		LinkedHashMap<String, Object> keyMap = bgwc.getKeyMap();
		if (keyMap != null) {
			String iscost = (String) keyMap.get("iscost");
			if ("N".equals(iscost)) {
				settMap = getSettInfoCurrent("FCT1-Cxx-003");
				if (settMap == null || settMap.size() == 0) {
					throw new BusinessException("没找到回写的信息!");
				}
				url = OutsideUtils.getOutsideInfo("EBS0207");
				// url = PropertiesUtil.getInstance("ebs_url.properties")
				// .readValue("CONTWATERANDELECTRICITYURL");
			}

			if ("Y".equals(iscost)) {
				settMap = getSettInfoCurrent("FCT1-Cxx-001");
				if (settMap == null || settMap.size() == 0) {
					throw new BusinessException("没找到回写的信息!");
				}
				url = OutsideUtils.getOutsideInfo("EBS0104");
				// url = PropertiesUtil.getInstance("ebs_url.properties")
				// .readValue("COSTWATERANDELECTRICITYURL");
			}
		}

		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();// 回写数据

		for (Map<String, Object> info : settMap) {
			HashMap<String, Object> dataMap = new HashMap<String, Object>();// 单独单据数据
			// 合同主ID
			if (info.get("main_contract_id") != null) {
				dataMap.put("MAIN_CONTRACT_ID", info.get("main_contract_id"));
			}
			// 结算金额
			if (info.get("settlement_amount") != null) {
				dataMap.put("SETTLEMENT_AMOUNT", info.get("settlement_amount"));
			}
			// 来源单据ID
			if (info.get("source_id") != null) {
				dataMap.put("SOURCE_ID", info.get("source_id"));
			}

			if ((String) keyMap.get("iscost") != null
					&& "N".equals((String) keyMap.get("iscost"))) {
				// 业务日期
				if (info.get("business_date") != null) {
					String business_date = (String) info.get("business_date")
							.toString().subSequence(0, 10);
					dataMap.put("BUSINESS_DATE", business_date);
				}
			}
			data.add(dataMap);
		}

		// 分单推送数据
		String token = "";
		String tokenkey = "";
		if (data != null && data.size() > 0) {
			for (HashMap<String, Object> map : data) {
				String[] msgObj = new String[2];
				try {
					String param = getjson(map);
					if ((String) keyMap.get("iscost") != null
							&& "N".equals((String) keyMap.get("iscost"))) {
						logVO.setTaskname("通用自动划扣水电费信息回写ebs");
					} else if ((String) keyMap.get("iscost") != null
							&& "Y".equals((String) keyMap.get("iscost"))) {
						logVO.setTaskname("成本自动划扣水电费信息回写ebs");
					}
					logVO.setNcparm("NC调用EBS地址:" + url + "NC报文:" + param);
					logVO.setSrcsystem("EBS");
					logVO.setOperator("NC");
					logVO.setExedate(new UFDateTime().toString());
					String key = OutsideUtils.getOutsideInfo("EBS-KEY");
					tokenkey = param + key;
					token = EncoderByMd5(tokenkey);
					String response = Httpconnectionutil.newinstance()
							.connection(url + token, "&req=" + param);
					// String response = Httpconnectionutil.newinstance()
					// .connection(url, "&req=" + param);
					if (response != null) {
						JSONObject jsonobj = JSONObject.parseObject(response);
						String code = (String) jsonobj.get("code");
						String msg = (String) jsonobj.get("msg");
						if (!("S".equalsIgnoreCase(code))) {
							msgObj[0] = msg;
							errorsb.append("EBS回写信息:" + msg);
							logVO.setMsg("EBS回写信息:" + msg);
							logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
						} else {
							if (map.get("source_id") != null) {
								String pk_paybill = (String) map
										.get("source_id");
								String sql = "update cmp_detail set def1='Y' where pk_settlement in(select pk_settlement from cmp_settlement t inner join  ap_paybill a on a.pk_paybill = t.pk_busibill where a.pk_paybill='"
										+ pk_paybill + "')";
								getBaseDAO().executeUpdate(sql);
								String sql1 = "update ap_paybill set def61='Y' where ap_paybill.pk_paybill='"
										+ pk_paybill + "'";
								getBaseDAO().executeUpdate(sql1);
							}
							logVO.setResult(code);
							logVO.setMsg("EBS回写信息:" + msg);
							logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
						}
					}

				} catch (Exception e) {
					logVO.setResult("E");
					logVO.setMsg(e.getMessage());
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
					errorsb.append(e.getMessage());
					bgwc.setLogStr(errorsb.toString());
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				} finally {
					logVO.setDr(0);
					saveLog.SaveLog_RequiresNew(logVO);
				}
				msglist.add(msgObj);
			}
		}

		if (errorsb.length() > 0) {
			throw new BuildException(errorsb.toString());
		}
		return msglist;
	}

	/**
	 * 回写信息sql
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getSettInfoCurrent(String billtype)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select distinct a.def79 MAIN_CONTRACT_ID,  ");
		query.append("                t.orglocal   SETTLEMENT_AMOUNT,  ");
		query.append("                g.pk_paybill SOURCE_ID,  ");
		query.append("                t.settledate BUSINESS_DATE,  ");
		query.append("                y.pk_billtypecode  ");
		query.append("  from cmp_settlement t  ");
		query.append("  left join cmp_detail d  ");
		query.append("    on d.pk_settlement = t.pk_settlement  ");
		query.append("  left join ap_paybill g  ");
		query.append("    on g.pk_paybill = t.pk_busibill  ");
		query.append("  left join ap_payitem gitem  ");
		query.append("    on gitem.pk_paybill = g.pk_paybill  ");
		query.append("  left join fct_ap a  ");
		query.append("    on a.pk_fct_ap = g.def5  ");
		query.append("    left join bd_billtype y  ");
		query.append("    on a.ctrantypeid  = y.pk_billtypeid  ");
		query.append(" where nvl(t.dr, 0) = 0  ");
		query.append("   and nvl(d.dr, 0) = 0  ");
		query.append("   and nvl(g.dr, 0) = 0  ");
		query.append("   and nvl(gitem.dr, 0) = 0  ");
		query.append("   and d.def1 <> 'Y'  ");
		query.append("   and g.def61 <> 'Y'  ");
		query.append("   and t.settlestatus = 5  ");
		query.append("   and t.pk_billtype = 'F3'  ");
		query.append("   and g.pk_tradetype = 'F3-Cxx-021'  ");
		query.append("   and g.def5 <> '~'  ");
		query.append("   and g.def5 is not null  ");
		query.append("	 and g.approvestatus = 1 ");
		query.append("   and y.pk_billtypecode = '" + billtype + "'  ");

		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(query.toString(), new MapListProcessor());

		return list;
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
}
