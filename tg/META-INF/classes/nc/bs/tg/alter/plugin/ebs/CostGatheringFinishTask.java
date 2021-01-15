package nc.bs.tg.alter.plugin.ebs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import uap.serverdes.appesc.MD5Util;

/**
 * 成本收款完成后回写信息给EBS
 * 
 * @author tzj
 * 
 */
public class CostGatheringFinishTask extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = getSettInfoCurrent(bgwc);
		String registryName = bgwc.getRegistryName();
		String code = "response_nc_receipt_data";
		String syscode = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("EBSSYSTEM");
		String key = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("KEY");
		if (settMap != null && settMap.size() > 0) {

			for (Map<String, Object> info : settMap) {
				String[] msgObj = new String[2];
				msgObj[0] = (String) info.get("receipt_code");
				try {
					String settid = (String) info.get("pk_settlement");
					Map<String, Object> postdata = new HashMap<String, Object>();
					postdata.put("receipt_code", info.get("receipt_code"));// nc退款单号
					postdata.put("receipt_id", info.get("receipt_id"));// nc收款单id
					postdata.put(
							"make_amt",
							info.get("make_amt") == null ? null : info.get(
									"make_amt").toString());// 收款金额
					postdata.put("connumber", info.get("connumber"));// 合同编码
					String make_date = (String) info.get("make_date");
					SimpleDateFormat mater = new SimpleDateFormat("yyyy-MM-dd");// 年月日时
					make_date = mater.format(mater.parse(make_date));
					postdata.put("make_date", make_date);// 收款日期
					postdata.put("deposit_flag", "N");// 是否保证金(暂时默认为否)

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
							.onPushEBSReceivablesData_RequiresNew(settid, code,
									syscode, token, postdata, registryName);
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
	 * 成本收款回写sql
	 * 
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getSettInfoCurrent(BgWorkingContext bgwc)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select t.pk_settlement , p.billno as receipt_code,");// nc退款单号
		query.append("       p.pk_gatherbill as receipt_id, "); // nc收款单id
		query.append("       a.vbillcode as connumber, "); // 合同编码
		query.append("       p.billdate as make_date, ");// 收款日期
		query.append("       p.local_money as make_amt ");// 收款金额
		query.append("  from cmp_settlement t  ");
		query.append(" inner join ar_gatherbill p  ");
		query.append("    on t.pk_busibill = p.pk_gatherbill  ");
		query.append(" inner join fct_ap a  ");
		query.append("    on a.pk_fct_ap = p.def30  ");
		query.append(" where p.dr = 0  ");
		query.append("   and a.dr = 0  ");
		query.append("   and t.dr = 0  ");
		query.append("   and t.settlestatus = 5  ");
		query.append("   and t.pk_billtype = 'F2'  ");
		query.append("   and t.pk_tradetype in ('F2-Cxx-013')  ");
		query.append("	 and t.def3 <> 'Y' ");
		query.append("	 and p.approvestatus  = 1 ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(query.toString(), new MapListProcessor());

		return list;
	}
}
