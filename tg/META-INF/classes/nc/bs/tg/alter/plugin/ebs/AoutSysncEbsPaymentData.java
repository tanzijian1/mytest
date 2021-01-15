package nc.bs.tg.alter.plugin.ebs;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.NcToEbsLogVO;

import org.apache.tools.ant.BuildException;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ����������ʱ��д����
 * 
 * @author ASUS
 * 
 */
public class AoutSysncEbsPaymentData extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		StringBuffer errorsb = new StringBuffer();
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = getSettInfoCurrent(bgwc);
		ISqlThread inset = NCLocator.getInstance().lookup(ISqlThread.class);
		String syscode = OutsideUtils.getOutsideInfo("EBS-SYSTEM");
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");

		String token = "";
		String tokenkey = "";
		// String syscode = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("EBSSYSTEM");
		// String key = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("KEY");

		if (settMap == null || settMap.size() == 0) {
			throw new BusinessException("û�ҵ���д����Ϣ!");
		}
		// String url = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("PAYURL");
		String url = OutsideUtils.getOutsideInfo("EBS0206");
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();// ��д����
		HashMap<String, Object> bodymap = new HashMap<String, Object>();// ͬ�ŵ��ݱ�������
		HashMap<String, Object> contract_temp = new HashMap<String, Object>();// �����ͬ��ID
		HashMap<String, Object> payid_temp = new HashMap<String, Object>();// ����EBSid
		HashMap<String, Object> receivables_temp = new HashMap<String, Object>();// �����տ���Ϣ
		HashMap<String, Object> pk_settlements = new HashMap<String, Object>();// ��������
		HashMap<String, Object> receivablesMap = new HashMap<String, Object>();
		for (Map<String, Object> info : settMap) {
			if (info.get("p_ncrecpnumber") != null) {
				contract_temp.put(info.get("p_ncrecpnumber").toString(),
						info.get("p_contractid"));
				payid_temp.put(info.get("p_ncrecpnumber").toString(),
						info.get("p_paybillid"));
				pk_settlements.put(info.get("p_ncrecpnumber").toString(),
						info.get("pk_settlement"));// ���㵥����
				receivablesMap.put("p_Bond_Scale", info.get("p_bond_scale"));// ��֤�����
				receivablesMap.put("p_Abs_Scale", info.get("p_abs_scale"));// ABSʵ������
				receivablesMap.put("p_Abs_Amount", info.get("p_abs_amount"));// ABSʵ�����
				receivables_temp.put(info.get("p_ncrecpnumber").toString(),
						receivablesMap);
				if (bodymap.get(info.get("p_ncrecpnumber")) == null) {
					ArrayList<HashMap<String, Object>> list_temp = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> map_temp = new HashMap<String, Object>();
					map_temp.put("p_Actreceiver", info.get("p_actreceiver"));// ��Ӧ��
					map_temp.put("p_Actrevaccount", info.get("p_actrevaccount"));// �տ������˻�
					map_temp.put("p_Actpaymoney", info.get("p_actpaymoney"));// ������
					if (info.get("p_actpaydate") != null) {// ��������
						UFDate uf = new UFDate(info.get("p_actpaydate")
								.toString());
						String s = uf.getYear() + "-" + uf.getMonth() + "-"
								+ uf.getDay();
						map_temp.put("p_Actpaydate", s);
					}
					// map_temp.put("p_Nopaidamount",
					// info.get("p_nopaidamount"));
					map_temp.put("p_Ispaid", "N");
					map_temp.put("p_Nopaidamount", 0);

					list_temp.add(map_temp);
					bodymap.put(info.get("p_ncrecpnumber").toString(),
							list_temp);

				} else {
					ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) bodymap
							.get(info.get("p_ncrecpnumber"));

					HashMap<String, Object> map_temp = new HashMap<String, Object>();
					map_temp.put("p_Actreceiver", info.get("p_actreceiver"));
					map_temp.put("p_Actrevaccount", info.get("p_actrevaccount"));
					map_temp.put("p_Actpaymoney", info.get("p_actpaymoney"));
					if (info.get("p_actpaydate") != null) {//
						UFDate uf = new UFDate(info.get("p_actpaydate")
								.toString());
						String s = uf.getYear() + "-" + uf.getMonth() + "-"
								+ uf.getDay();
						map_temp.put("p_Actpaydate", s);
					}
					// map_temp.put("p_Nopaidamount",
					// info.get("p_nopaidamount"));
					map_temp.put("p_Ispaid", "N");
					map_temp.put("p_Nopaidamount", 0);
					list.add(map_temp);
				}
			}
		}
		for (String billno : bodymap.keySet()) {
			HashMap<String, Object> data_temp = new HashMap<String, Object>();
			if (contract_temp.get(billno) != null)
				data_temp.put("p_Contractid",
						Integer.valueOf(contract_temp.get(billno).toString()));
			if (payid_temp.get(billno) != null)
				data_temp.put("p_Paybillid",
						Integer.valueOf(payid_temp.get(billno).toString()));
			if (receivables_temp.get(billno) != null) {
				HashMap<String, Object> map = (HashMap<String, Object>) receivables_temp
						.get(billno);

				if (pk_settlements.get(billno) != null)
					data_temp.put("p_Pay_Key", pk_settlements.get(billno)
							.toString());// ���㵥����

				data_temp.put("p_Bond_Scale", map.get("p_bond_scale"));// ��֤�����
				data_temp.put("p_Abs_Scale", map.get("p_abs_scale"));// ABSʵ������
				data_temp.put("p_Abs_Amount", map.get("p_abs_amount"));// ABSʵ�����
			}

			data_temp.put("p_Ncrecpnumber", billno);
			data_temp.put("Actualreceiver", bodymap.get(billno));

			data.add(data_temp);
		}
		// �ֵ���������
		for (HashMap<String, Object> map : data) {
			String[] msgObj = new String[2];
			try {
				String param = getjson(map);
				logVO.setTaskname("EBS_���ҵ���Ӧ�ĸ��������и���Ǽ�");
				logVO.setSrcsystem("EBS");
				logVO.setOperator("NC");
				logVO.setExedate(new UFDateTime().toString());

				// if ("nc".equals(syscode)) {
				// Date date = new Date();
				// SimpleDateFormat formater = new
				// SimpleDateFormat("yyyyMMddHHmm");// ������ʱ��
				// String time = formater.format(date);
				// tokenkey = time + key;
				// token = MD5Util.getMD5(tokenkey).toUpperCase();
				//
				// }
				tokenkey = param + key;
				token = EncoderByMd5(tokenkey);

				String response = Httpconnectionutil.newinstance().connection(
						url + token, "&req=" + param);
				logVO.setNcparm("δ����token:" + tokenkey + "��ϵͳEBS��ַ:" + url
						+ "���ܺ��token:" + token + "NC����:" + param);
				if (response != null) {
					JSONObject jsonobj = JSONObject.parseObject(response);
					String msg = "EBS��д��Ϣ: " + jsonobj.getString("msg");
					String code = (String) jsonobj.get("code");
					if (!("S".equalsIgnoreCase(code))) {
						msgObj[0] = msg;
						errorsb.append(msg);
						logVO.setMsg(msg);
						logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
					} else {
						if (map.get("p_Ncrecpnumber") != null) {
							String billno = (String) map.get("p_Ncrecpnumber");
							StringBuffer query = new StringBuffer();
							query.append("update cmp_detail  ");
							query.append("   set def1 = 'Y'  ");
							query.append(" where exists  ");
							query.append("       (select 1  ");
							query.append("          from cmp_settlement t  ");
							query.append("         inner join ap_paybill a  ");
							query.append("            on a.pk_paybill = t.pk_busibill  ");
							query.append("         where a.billno = '"
									+ billno
									+ "' and cmp_detail.pk_settlement  = t.pk_settlement);  ");
							String sql = query.toString();
							inset.insertsql_RequiresNew(sql);
							String sql1 = "update ap_paybill set def61='Y' where ap_paybill.billno='"
									+ billno + "'";
							inset.insertsql_RequiresNew(sql1);

						}
						logVO.setResult(code);
						logVO.setMsg(msg);
						logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
					}
				}

			} catch (Exception e) {
				logVO.setResult("E");
				logVO.setMsg(e.getMessage());
				logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
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
		if (errorsb.length() > 0) {
			throw new BuildException(errorsb.toString());
		}
		return msglist;
	}

	/**
	 * ͨ�ø����дsql
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getSettInfoCurrent(BgWorkingContext bgwc)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct ")
				.append("  t.pk_settlement, ")
				// ���㵥����Ϊ�˱�֤Ψһ�Եı�ʶ-2020-05-12-̸�ӽ�
				.append(" g.def11 p_Contractid, ")
				// ��ͬ��id
				.append(" g.def1 p_Paybillid, ")
				// ��id
				.append(" g.billno p_Ncrecpnumber, ")
				// ��֤�����
				.append("g.def37 p_Bond_Scale,  ")
				// ABSʵ������
				.append("g.def54 p_Abs_Scale,  ")
				// �Ƿ񹲹��˻�
				.append("g.def55 p_Abs_Amount,  ")
				// nc�տ��
				.append(" gitem.local_money_de p_Actpaymoney, ")
				// ������
				.append(" gitem.billdate p_Actpaydate, ")
				// ��������
				.append(" (select bd_supplier.code from bd_supplier where pk_supplier = gitem.supplier) as p_Actreceiver,")
				// ʵ���տӦ��
				.append("  (select bd_bankaccsub.accnum from bd_bankaccsub where pk_bankaccsub = gitem.recaccount) as p_Actrevaccount")
				// �տ������˻�
				.append(" from cmp_settlement t")
				.append(" left join cmp_detail d on d.pk_settlement = t.pk_settlement ")
				.append(" left join ap_paybill g on g.pk_paybill = t.pk_busibill ")
				.append(" left join ap_payitem gitem on gitem.pk_paybill = g.pk_paybill ")
				.append(" where nvl(t.dr,0)= 0 and nvl(d.dr,0)=0 and nvl(g.dr,0) = 0 and nvl(gitem.dr,0) = 0 and d.def1 <> 'Y' and g.def61 <> 'Y' ")
				.append(" and t.settlestatus =5 and t.pk_billtype = 'F3'  ")
				// .append(" and g.billno='D32020033000001426' ")
				// ͨ�õĽ�������
				.append(" and g.pk_tradetype in('F3-Cxx-007') ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
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
