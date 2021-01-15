package nc.bs.tg.alter.plugin.ebscost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.AoutSysncEbsData;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.uap.lock.PKLock;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public class AoutSysncEbsCostPaymentData extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = getSettInfoCurrent(bgwc);
		String registryName = bgwc.getRegistryName();
		String code = "";
		String syscode = OutsideUtils.getOutsideInfo("EBS-SYSTEM");
		String key = OutsideUtils.getOutsideInfo("EBS-KEY");
		// String syscode = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("EBSSYSTEM");
		// String key = PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("KEY");
		Map<String, String> refMap = null;
		if (settMap != null && settMap.size() > 0) {
			for (Map<String, Object> info : settMap) {
				String[] msgObj = new String[2];
				msgObj[0] = (String) info.get("billcode");
				try {
					String settid = (String) info.get("pk_settlement");
					String pk_paybill = (String) info.get("nc_payment_id");
					Map<String, Object> postdata = new HashMap<String, Object>();
					// 添加判断只是abs才填写登记日期-2020-02-24-tzj
					String pay_method_code = (String) info
							.get("pay_method_code");
					if (pay_method_code != null && "9".equals(pay_method_code)) {
						Date date2 = new Date();
						Calendar cal = Calendar.getInstance();
						cal.setTime(date2);
						cal.add(Calendar.YEAR, 1);
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd");
						cal.add(cal.DATE, -1);
						String abs_audit_date = format.format(cal.getTime());
						postdata.put("abs_audit_date", abs_audit_date);// abs登记日期
					} else {
						postdata.put("abs_audit_date", null);// abs登记日期
					}

					postdata.put("pay_method_code", pay_method_code);// 付款方式
					postdata.put("currency", info.get("currency"));// 币种
					// 添加判断只是抵扣款无支付回写金额为0-2020-02-24-huangxj
					if (pay_method_code != null && "62".equals(pay_method_code)) {
						postdata.put("pay_amount", "0");
					} else {
						postdata.put("pay_amount",
								info.get("pay_amount") == null ? null : info
										.get("pay_amount").toString());// 付款金额
					}
					postdata.put("apply_number", info.get("apply_number"));// ebs付款申请单号
					postdata.put("nc_payment_id", info.get("nc_payment_id"));// ebs付款申请id
					List<UFDouble> mnylist = (List<UFDouble>) info
							.get("line_listData");
					postdata.put("pay_date", info.get("settledate").toString());
					List<Map<String, Object>> bodylist = new ArrayList<Map<String, Object>>();
					HashMap<String, Object> bodyMap = new HashMap<String, Object>();
					// 添加判断只是抵扣款无支付回写金额为0-2020-02-24-huangxj
					if (pay_method_code != null && "62".equals(pay_method_code)) {
						bodyMap.put("pay_amount", "0");
					} else {
						bodyMap.put("pay_amount",
								info.get("pay_amount") == null ? null : info
										.get("pay_amount").toString());// 付款金额
					}
					bodyMap.put("fq_amoun", "0");// 付讫金额()
					bodyMap.put("fq_flag", "N");// 付讫标识
					bodyMap.put("fq_date", "");// 付讫日期
					/**
					 * 添加多表体信息返回2020-03-30-谈子健-start
					 */
					String nc_payment_id = (String) info.get("nc_payment_id");
					List<Map<String, String>> details_listData = new ArrayList<Map<String, String>>();
					if (nc_payment_id != null && !"".equals(nc_payment_id)) {
						List<Map<String, String>> billItem = getBillItem(nc_payment_id);
						if (billItem != null && billItem.size() > 0) {
							for (Map<String, String> map : billItem) {
								details_listData.add(map);
							}
							// String data = details_listData.toString();
							bodyMap.put("details_listData", details_listData);
						}
					}
					/**
					 * 添加多表体信息返回2020-03-30-谈子健-end
					 */
					bodylist.add(bodyMap);

					postdata.put("line_listData", bodylist);

					String token = "";
					// if ("nc".equals(syscode)) {
					// Date date = new Date();
					// SimpleDateFormat formater = new SimpleDateFormat(
					// "yyyyMMddHHmm");// 年月日时分
					// String time = formater.format(date);
					// String tokenkey = time + key;
					// token = MD5Util.getMD5(tokenkey).toUpperCase();
					//
					// }

					refMap = getEBSServcie().onPushEBSPayData_RequiresNew(
							settid, code, syscode, token, postdata,
							registryName, pk_paybill);

					if (refMap != null) {
						msgObj[1] = refMap.get("msg");
					}

				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				} finally {
					PKLock.getInstance().releaseDynamicLocks();
				}
				msglist.add(msgObj);
			}
		}
		return msglist;
	}

	/**
	 * 成本付款回写sql
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getSettInfoCurrent(BgWorkingContext bgwc)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.pk_settlement ")
				.append(",t.settledate")
				.append(",t.billcode")
				.append(",bd_balatype.code pay_method_code")
				.append(",bd_currtype.code currency")
				.append(",p.pk_paybill nc_payment_id")
				.append(",p.def2 apply_number")
				.append(",sum(case when p.def38 = '~' or p.def38 is null then 0 else to_number(p.def38) end) apply_waterandelectricity")
				// 水电费
				.append(",sum(case when p.def39 = '~' or p.def39 is null then 0 else to_number(p.def39) end) apply_penalty")
				// 罚款
				.append(",sum(case when p.def46 = '~' or p.def46 is null then 0 else to_number(p.def46) end) apply_other")
				// 其他扣款
				// .append(",p.def14 abs_audit_date")
				.append(",sum(d.pay) pay_amount")
				.append(" from cmp_settlement t")
				.append(" inner join cmp_detail d on d.pk_settlement = t.pk_settlement ")
				.append(" inner join ap_paybill p on p.pk_paybill = t.pk_busibill")
				.append(" inner join bd_balatype on bd_balatype.pk_balatype = d.pk_balatype ")
				.append(" inner join bd_currtype on bd_currtype.pk_currtype = d.pk_currtype ")
				.append(" where t.dr= 0 and d.dr=0 and bd_balatype.dr =0 and bd_currtype.dr = 0 and d.def1 <> 'Y'  and p.def61 <> 'Y' ")
				.append(" and t.settlestatus =5 and t.pk_billtype = 'F3' and t.pk_tradetype in('F3-Cxx-004') ")
				// .append(" and p.billno = 'D32020033100001450' ")
				.append(" group by t.pk_settlement,t.billcode ")
				.append(" ,t.billcode ").append(" ,bd_balatype.code ")
				.append(" ,bd_currtype.code ").append(" ,p.pk_paybill ")
				.append(" ,p.def2 ,t.settledate,p.def14");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}

	/**
	 * 付款申请单通过表头主键查询多表体2020-03-30
	 * 
	 * @throws BusinessException
	 */
	private List<Map<String, String>> getBillItem(String nc_payment_id)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select distinct to_char(b.def38)          as table_id,  ");
		query.append("                to_char(b.def40)          as table_type,  ");
		query.append("                to_char(i.local_money_de) as fsplit_amt  ");
		query.append("  from ap_payitem i  ");
		query.append("  left join tgfn_payreqbus b  ");
		query.append("    on b.pk_business_b = i.src_itemid  ");
		query.append(" where i.pk_paybill = '" + nc_payment_id + "'  ");
		query.append("   and i.dr = 0  ");
		query.append("   and b.dr = 0;  ");
		List<Map<String, String>> list = (List<Map<String, String>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(query.toString(), new MapListProcessor());
		return list;
	}
}
