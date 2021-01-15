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
					if ("Ԥ����".equals(paymentType)) {
						type = "YPAY";
					} else if ("Ͷ�걣֤��".equals(paymentType)) {
						type = "Y_REFUND";
					} else {
						type = "BZPAY";
					} 
					postdata.put("paymentType", type);// ��������
					
					//postdata.put("paymentType", info.get("def52"));// ��������
					postdata.put("pkOrg", info.get("pkorg"));// ������֯
					postdata.put("orgName", info.get("orgname"));// NC������֯����
					postdata.put("vendorName", info.get("vendorname"));// ��Ӧ������
					postdata.put("vendorNumber", info.get("vendornumber"));// ��Ӧ�̱���
					postdata.put("agreementNumber", info.get("agreementnumber"));// Э����

					String paymentDate = info.get("paymentdate").toString()
							.substring(0, 10);
					postdata.put("paymentDate", paymentDate);// ��������
					postdata.put("glDate", "");// ��������
					postdata.put("payMethodCode", info.get("paymethodcode"));// �����
					postdata.put("paymentAmount",
							info.get("paymentamount") == null ? null : info
									.get("paymentamount").toString());// ������
					postdata.put("ncPaymentId", info.get("ncpaymentid"));// NC����ID
					postdata.put("ncPaymentNumber", info.get("ncpaymentnumber"));// NC���ݱ��
					postdata.put("applyNumber", info.get("applynumber"));// ����������
					postdata.put("currency", "CNY");// ����
					postdata.put("fqAmount", "0");// �������()
					postdata.put("fqFlag", "N");// ������ʶ
					postdata.put("fqDate", "");// ��������
					postdata.put("fqOperator", "");// �����˱��

					postdata.put("processType", "");// ��������
					postdata.put("command", "�������");// �������
					postdata.put("operationType", "none");// NC�������̲�������
					postdata.put("status", "true");// ����״̬
					postdata.put("taskID", "");// BPM����id
					postdata.put("pId", info.get("pid"));// ���������Ԥ��������

					List<Map<String, String>> bodylist = new ArrayList<Map<String, String>>();
					HashMap<String, String> bodyMap = new HashMap<String, String>();
					bodyMap.put("invoiceNumber", "");// ��Ʊ���
					bodyMap.put("fpayAmount", "");// �������
					bodylist.add(bodyMap);

					postdata.put("invoiceList", bodylist);

					String token = "";
					/*
					 * if ("nc".equals(syscode)) { Date date = new Date();
					 * SimpleDateFormat formater = new SimpleDateFormat(
					 * "yyyyMMddHHmm");// ������ʱ�� String time =
					 * formater.format(date); String tokenkey = time + key;
					 * token = MD5Util.getMD5(tokenkey).toUpperCase();
					 * 
					 * }
					 */
					Map<String, String> refMap = getEBSServcie()
							.onPushEBSPayData_RequiresNew(settid, code,
									syscode, token, postdata, "SRM�������" ,(String)info.get("ncpaymentid"));
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
	 * SRM�����дsql
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
				// ��������
				.append(",org.code pkOrg")
				// NC������֯
				.append(",org.name orgName")
				// NC������֯����
				.append(",sup.code vendorNumber")
				// ��Ӧ������
				.append(",sup.name vendorName")
				// ��Ӧ�̱���
				.append(",ghead.def5 agreementNumber")
				// Э����
				.append(",t.settledate paymentDate")
				// ��������
				.append(",bal.code payMethodCode")
				// �����
				.append(",gitem.local_money_de paymentAmount")
				// ������
				.append(",ghead.pk_paybill ncPaymentId")
				// NC����ID
				.append(",ghead.billno ncPaymentNumber")
				// NC���ݱ��
				.append(",preq.def2 applyNumber")
				// ����������
				.append(",preq.def1 pid")
				// ����������
//				.append(",preq.def52 def52")
//				// ��������
//				.append(",preq.def53 def53")
//				// BPM����ID
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
