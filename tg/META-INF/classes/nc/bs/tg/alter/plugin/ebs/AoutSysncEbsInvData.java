package nc.bs.tg.alter.plugin.ebs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.serverdes.appesc.MD5Util;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;

/**
 * ��Ʊϵͳ
 * 
 * @author acer
 * 
 */
public class AoutSysncEbsInvData extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = getSettInfo(bgwc);
		String registryName = bgwc.getRegistryName();
		if (settMap == null || settMap.size() == 0) {
			throw new BusinessException("");
		}

		String code = "response_ap_invoince_date";
		String syscode = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("EBSSYSTEM");
		String key = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("KEY");
		for (Map<String, Object> info : settMap) {
			String[] msgObj = new String[3];
			msgObj[0] = (String) info.get("billcode");
			try {
				String settid = (String) info.get("pk_settlement");
				Map<String, Object> postdata = new HashMap<String, Object>();
				postdata.put("ebs_cbcon_fconid", info.get("ebs_cbcon_fconid"));// ��Ʊϵͳid
				postdata.put("invoinces_type", info.get("invoinces_type"));// ��Ʊ����
				postdata.put("invoinces_code", info.get("invoinces_code"));// ��Ʊ����
				postdata.put("invoinces_no", info.get("invoinces_no"));// ��Ʊ���
				postdata.put("invoinces_date", info.get("invoinces_date"));// ��Ʊ����
				postdata.put("vendor_id", info.get("vendor_id"));// ��Ӧ��id
				postdata.put("invoinces_amount", info.get("invoinces_amount"));// ��Ʊ�ܽ��
				// List<UFDouble> mnylist = (List<UFDouble>) info
				// .get("line_listData");
				List<Map<String, String>> bodylist = new ArrayList<Map<String, String>>();
				HashMap<String, String> bodyMap = new HashMap<String, String>();
				bodyMap.put("ebs_cbcon_lines_id",
						(String) info.get("ebs_cbcon_lines_id"));// ��Ʊϵͳͷ(��Ӧnc�ķ�Ʊͷid)
				bodyMap.put("untaxed_amount",
						(String) info.get("untaxed_amount"));// δ��˰���
				bodyMap.put("tax_rate", (String) info.get("tax_rate"));// ˰��
				bodyMap.put("tax_amount", (String) info.get("tax_amount"));// ˰��
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
						.onPushEBSPayData_RequiresNew(settid, code, syscode,
								token, postdata, registryName,null);
				if (refMap != null) {
					msgObj[1] = refMap.get("msg");
				}

			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				msgObj[2] = e.getMessage();
			}
			msglist.add(msgObj);
		}
		return msglist;
	}

	/**
	 * ͨ��Ӱ��id�鷢Ʊ��Ϣ
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getSettInfo(BgWorkingContext bgwc)
			throws BusinessException {
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
				.append(" where t.dr= 0 and d.dr=0 and bd_balatype.dr =0 and bd_currtype.dr = 0 and t.def2 <> 'Y' ")
				.append(" and t.settlestatus =5 and t.pk_billtype = 'F3' and t.pk_tradetype in( ")
				// ͨ�õĽ�������
				.append(" 'F3-Cxx-007') ")
				.append(" group by t.pk_settlement,t.billcode ")
				.append(" ,t.billcode ").append(" ,bd_balatype.name ")
				.append(" ,bd_currtype.code ").append(" ,p.def1 ")
				.append(" ,p.def2 ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}

}
