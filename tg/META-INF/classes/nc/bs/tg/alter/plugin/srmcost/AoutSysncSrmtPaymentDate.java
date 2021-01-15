package nc.bs.tg.alter.plugin.srmcost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.AoutSysncEbsData;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;

public class AoutSysncSrmtPaymentDate extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = getSettInfoCurrent(bgwc);
		String code = "pushSrmPaymentCheck";
		String syscode = "test";// PropertiesUtil.getInstance("ebs_url.properties")
		// .readValue("EBSSYSTEM");
		/*
		 * String key = PropertiesUtil.getInstance("ebs_url.properties")
		 * .readValue("KEY");
		 * http://times-dpctest.timesgroup.cn:6001/postMethod?
		 * code=pushSrmPaymentCheck&syscode=test&token=
		 */
		if (settMap != null && settMap.size() > 0) {

			for (Map<String, Object> info : settMap) {
				
				String[] msgObj = new String[2];
				msgObj[0] = (String) info.get("billcode");
				try {
					String settid = (String) info.get("pk_settlement");
					Map<String, Object> postdata = new HashMap<String, Object>();

					String paymentType = (String) info.get("paymenttype");
					String type = null;
					if ("预付款".equals(paymentType)) {
						type = "YPAY";
					} else if ("投标保证金".equals(paymentType)) {
						type = "Y_REFUND";
					} else {
						type = "BZPAY";
					} 
					postdata.put("paymentType", type);// 付款类型
					
					//postdata.put("paymentType", info.get("def52"));// 付款类型
					postdata.put("pkOrg", info.get("pkorg"));// 财务组织
					postdata.put("orgName", info.get("orgname"));// NC财务组织名称
					postdata.put("vendorName", info.get("vendorname"));// 供应商名称
					postdata.put("vendorNumber", info.get("vendornumber"));// 供应商编码
					postdata.put("agreementNumber", info.get("agreementnumber"));// 协议编号

					String paymentDate = info.get("paymentdate").toString()
							.substring(0, 10);
					postdata.put("paymentDate", paymentDate);// 付款日期
					postdata.put("glDate", "");// 入账日期
					postdata.put("payMethodCode", info.get("paymethodcode"));// 付款方法
					postdata.put("paymentAmount",
							info.get("paymentamount") == null ? null : info
									.get("paymentamount").toString());// 付款金额
					postdata.put("ncPaymentId", info.get("ncpaymentid"));// NC单据ID
					postdata.put("ncPaymentNumber", info.get("ncpaymentnumber"));// NC单据编号
					postdata.put("applyNumber", info.get("applynumber"));// 付款申请编号
					postdata.put("currency", "CNY");// 币种
					postdata.put("fqAmount", "0");// 付讫金额()
					postdata.put("fqFlag", "N");// 付讫标识
					postdata.put("fqDate", "");// 付讫日期
					postdata.put("fqOperator", "");// 付讫人编号

					postdata.put("processType", "");// 流程类型
					postdata.put("command", "付款完成");// 审批意见
					postdata.put("operationType", "none");// NC逆向流程操作类型
					postdata.put("status", "true");// 流程状态
					postdata.put("taskID", "");// BPM流程id
					postdata.put("pId", info.get("pid"));// 付款申请或预付款申请

					List<Map<String, String>> bodylist = new ArrayList<Map<String, String>>();
					HashMap<String, String> bodyMap = new HashMap<String, String>();
					bodyMap.put("invoiceNumber", "");// 发票编号
					bodyMap.put("fpayAmount", "");// 冲销金额
					bodylist.add(bodyMap);

					postdata.put("invoiceList", bodylist);

					String token = "";
					/*
					 * if ("nc".equals(syscode)) { Date date = new Date();
					 * SimpleDateFormat formater = new SimpleDateFormat(
					 * "yyyyMMddHHmm");// 年月日时分 String time =
					 * formater.format(date); String tokenkey = time + key;
					 * token = MD5Util.getMD5(tokenkey).toUpperCase();
					 * 
					 * }
					 */
					Map<String, String> refMap = getEBSServcie()
							.onPushEBSPayData_RequiresNew(settid, code,
									syscode, token, postdata, "SRM付款完成" ,(String)info.get("ncpaymentid"));
					if (refMap != null) {
						msgObj[1] = refMap.get("msg");
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				}
				msglist.add(msgObj);
			}
		}
		return msglist;
	}

	/**
	 * SRM付款回写sql
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getSettInfoCurrent(BgWorkingContext bgwc)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.pk_settlement")
				.append(",gitem.def2 paymentType")
				// 付款类型
				.append(",org.code pkOrg")
				// NC财务组织
				.append(",org.name orgName")
				// NC财务组织名称
				.append(",sup.code vendorNumber")
				// 供应商名称
				.append(",sup.name vendorName")
				// 供应商编码
				.append(",ghead.def5 agreementNumber")
				// 协议编号
				.append(",t.settledate paymentDate")
				// 付款日期
				.append(",bal.code payMethodCode")
				// 付款方法
				.append(",gitem.local_money_de paymentAmount")
				// 付款金额
				.append(",ghead.pk_paybill ncPaymentId")
				// NC单据ID
				.append(",ghead.billno ncPaymentNumber")
				// NC单据编号
				.append(",preq.def2 applyNumber")
				// 付款申请编号
				.append(",preq.def1 pid")
				// 付款申请编号
//				.append(",preq.def52 def52")
//				// 付款类型
//				.append(",preq.def53 def53")
//				// BPM流程ID
				.append(" from cmp_settlement t")
				.append(" left join cmp_detail d on d.pk_settlement = t.pk_settlement ")
				.append(" left join ap_paybill ghead on ghead.pk_paybill = t.pk_busibill ")
				.append(" left join ap_payitem gitem on gitem.pk_paybill = ghead.pk_paybill ")
				.append(" left join tgfn_payrequest preq on gitem.src_billid = preq.pk_payreq ")
				.append(" left join org_orgs org on ghead.pk_org = org.pk_org ")
				.append(" left join bd_balatype bal on bal.pk_balatype = d.pk_balatype ")
				.append(" left join bd_supplier sup on gitem.supplier = sup.pk_supplier ")
				.append(" where nvl(t.dr,0)= 0 and nvl(d.dr,0)=0 and nvl(org.dr, 0) = 0 and nvl(bal.dr, 0) = 0 ")
				.append(" and nvl(sup.dr, 0) = 0 and nvl(ghead.dr, 0) = 0 and nvl(gitem.dr, 0) = 0  and d.def1 <> 'Y'  and ghead.def61 <> 'Y'")
				.append(" and t.settlestatus =5 and t.pk_billtype = 'F3'  ")
				.append(" and ghead.pk_tradetype in('F3-Cxx-005') ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}
}
