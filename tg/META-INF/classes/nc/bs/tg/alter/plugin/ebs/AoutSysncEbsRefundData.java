package nc.bs.tg.alter.plugin.ebs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.security.provider.MD5;
import uap.serverdes.appesc.MD5Util;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;

/**
 * 付款回写_EBS退保证金付款登记回写
 * 
 * @author ASUS
 * 
 */
public class AoutSysncEbsRefundData extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settlist = getSettInfo(bgwc);
		String registryName = bgwc.getRegistryName();
		String code = "Insert_ContractRefund";
		String syscode = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("EBSSYSTEM");
		String key = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("KEY");
		if (settlist.size() > 0 && settlist != null) {
			for (Map<String, Object> info : settlist) {
				String[] msgObj = new String[3];
				msgObj[0] = (String) info.get("p_Ncrecpnumber");
				try {
					String settid = (String) info.get("pk_settlement");
					Map<String, Object> postdata = new HashMap<String, Object>();
					postdata.put("p_Contractid", info.get("p_Contractid"));// 合同主ID
					postdata.put("p_Paybillid", info.get("p_Paybillid"));// 请款单ID
					postdata.put("p_Payplanamid", "");// 付款计划ID（签约金额）
					postdata.put("p_Payplanbondid", "");// 付款计划ID（保证金、押金、诚意金和共管资金）
					postdata.put("p_Ncrecpnumber", info.get("p_Ncrecpnumber"));// NC收款单号
					postdata.put("p_refundmoney", info.get("p_refundmoney"));// 退款金额
					postdata.put("p_Refunddate", info.get("p_Refunddate"));// 退款日期
					// List<UFDouble> mnylist = (List<UFDouble>) info
					// .get("line_listData");
					// List<Map<String, String>> bodylist = new
					// ArrayList<Map<String, String>>();
					// HashMap<String, String> bodyMap = new HashMap<String,
					// String>();
					// bodyMap.put("ebs_cbcon_lines_id", (String)
					// info.get("ebs_cbcon_lines_id"));//发票系统头(对应nc的发票头id)
					// bodyMap.put("untaxed_amount", (String)
					// info.get("untaxed_amount"));// 未含税金额
					// bodyMap.put("tax_rate", (String) info.get("tax_rate"));//
					// 税率
					// bodyMap.put("tax_amount", (String)
					// info.get("tax_amount"));// 税额
					// bodylist.add(bodyMap);
					//
					// postdata.put("line_listData", bodylist);

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
									syscode, token, postdata, registryName,null);
					if (refMap != null) {
						msgObj[1] = refMap.get("msg");
					}

				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[2] = e.getMessage();
				}
				msglist.add(msgObj);
			}
		}
		return msglist;
	}

	private List<Map<String, Object>> getSettInfo(BgWorkingContext bgwc)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select ")
				.append(" g.def30 p_Contractid, ")
				// 合同主id
				.append(" gitem.def30 p_Paybillid, ")
				// 请款单id
				.append(" g.billno p_Ncrecpnumber,")
				// nc收款单号
				.append(" gitem.local_money_cr p_refundmoney, ")
				// 退款金额
				.append(" gitem.billdate p_Refunddate ")
				// 单据日期
				.append(" from cmp_settlement t")
				.append(" left join cmp_detail d on d.pk_settlement = t.pk_settlement ")
				.append(" left join ar_gatherbill g on g.pk_gatherbill = t.pk_busibill ")
				.append(" left join ar_gatheritem gitem on gitem.pk_gatherbill = g.pk_gatherbill ")
				.append(" where nvl(t.dr,0)= 0 and nvl(d.dr,0)=0 and nvl(g.dr,0) = 0 and nvl(gitem.dr,0) = 0 and t.def1 <> 'Y' ")
				.append(" and t.settlestatus =5 and t.pk_billtype = 'F2'  ")
				// 通用的交易类型
				.append(" and t.pk_tradetype in('F2-Cxx-007') ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}
}
