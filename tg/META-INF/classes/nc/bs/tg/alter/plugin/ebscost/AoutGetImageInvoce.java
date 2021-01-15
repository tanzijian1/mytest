package nc.bs.tg.alter.plugin.ebscost;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.AoutSysncEbsData;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.ISyncIMGBillServcie;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.pub.BusinessException;

public class AoutGetImageInvoce extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<String> settMap =  getSettInfoCurrent(bgwc);
		if (settMap != null && settMap.size() > 0) {
			for (String def3 : settMap) {
				String[] msgObj = new String[2];
				msgObj[0] = def3;
				ISyncIMGBillServcie syncIMGBillServcie = NCLocator
						.getInstance().lookup(ISyncIMGBillServcie.class);
				try {
					Logger.error("huangxj进入方法onSyncInv_RequiresNew");
					syncIMGBillServcie.onSyncInv_RequiresNew(def3);
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				}
			}
		}
		bgwc.setLogStr(msglist.toString());
		return msglist;

	}

	/**
	 * sql
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<String> getSettInfoCurrent(BgWorkingContext bgwc)
			throws BusinessException {

		// 应付单
		StringBuffer payablesql = new StringBuffer();
		payablesql.append(" select def3 from ap_payablebill ");
		payablesql
				.append(" where def3 <> '~' and nvl(dr, 0) = 0 and not exists ( ");
		payablesql.append("     select  pk_invoice_h from  hzvat_invoice_h ");
		payablesql
				.append("     where nvl(dr, 0) = 0 and def8 = ap_payablebill.def3 ) ");

		// 营销补票
		StringBuffer fillsql = new StringBuffer();
		fillsql.append(" select ");
		fillsql.append("   imagcode ");
		fillsql.append(" from ");
		fillsql.append("   yer_fillbill ");
		fillsql.append(" where ");
		fillsql.append("   nvl(imagcode, '~') <> '~' ");
		fillsql.append("   and nvl(dr, 0) = 0 ");
		fillsql.append("   and not exists ( ");
		fillsql.append("     select ");
		fillsql.append("       pk_invoice_h ");
		fillsql.append("     from ");
		fillsql.append("       hzvat_invoice_h ");
		fillsql.append("     where ");
		fillsql.append("       nvl(dr, 0) = 0 ");
		fillsql.append("       and def8 = yer_fillbill.imagcode ");
		fillsql.append("   ) ");

		// 报销单
		StringBuffer bxzbsql = new StringBuffer();
		bxzbsql.append(" select ");
		bxzbsql.append("   zyx16 ");
		bxzbsql.append(" from ");
		bxzbsql.append("   er_bxzb ");
		bxzbsql.append(" where ");
		bxzbsql.append("   nvl(zyx16, '~') <> '~' ");
		bxzbsql.append("   and er_bxzb.zyx17 = '已扫描' ");
		bxzbsql.append("   and nvl(dr, 0) = 0 ");
		bxzbsql.append("   and not exists ( ");
		bxzbsql.append("     select ");
		bxzbsql.append("       pk_invoice_h ");
		bxzbsql.append("     from ");
		bxzbsql.append("       hzvat_invoice_h ");
		bxzbsql.append("     where ");
		bxzbsql.append("       nvl(dr, 0) = 0 ");
		bxzbsql.append("       and def8 = er_bxzb.zyx16 ");
		bxzbsql.append("   ) ");

		// 融资请款
		StringBuffer finsql = new StringBuffer();
		finsql.append(" select ");
		finsql.append("   def21 ");
		finsql.append(" from ");
		finsql.append("   tgrz_financexpense ");
		finsql.append(" where ");
		finsql.append("   nvl(def21, '~') <> '~' ");
		finsql.append("   and nvl(dr, 0) = 0 ");
		finsql.append("   and not exists ( ");
		finsql.append("     select ");
		finsql.append("       pk_invoice_h ");
		finsql.append("     from ");
		finsql.append("       hzvat_invoice_h ");
		finsql.append("     where ");
		finsql.append("       nvl(dr, 0) = 0 ");
		finsql.append("       and def8 = tgrz_financexpense.def21 ");
		finsql.append("   ) ");

		// 补票单
		StringBuffer ticketsql = new StringBuffer();
		ticketsql.append(" select ");
		ticketsql.append("   def21 ");
		ticketsql.append(" from ");
		ticketsql.append("   tg_addTicket ");
		ticketsql.append(" where ");
		ticketsql.append("   nvl(def21, '~') <> '~' ");
		ticketsql.append("   and nvl(dr, 0) = 0 ");
		ticketsql.append("   and not exists ( ");
		ticketsql.append("     select ");
		ticketsql.append("       pk_invoice_h ");
		ticketsql.append("     from ");
		ticketsql.append("       hzvat_invoice_h ");
		ticketsql.append("     where ");
		ticketsql.append("       nvl(dr, 0) = 0 ");
		ticketsql.append("       and def8 = tg_addTicket.def21 ");
		ticketsql.append("   ) ");

		// 还款单
		StringBuffer repaysql = new StringBuffer();
		repaysql.append(" select ");
		repaysql.append("   def21 ");
		repaysql.append(" from ");
		repaysql.append("   cdm_repayrcpt ");
		repaysql.append(" where ");
		repaysql.append("   nvl(def21, '~') <> '~' ");
		repaysql.append("   and nvl(dr, 0) = 0 ");
		repaysql.append("   and not exists ( ");
		repaysql.append("     select ");
		repaysql.append("       pk_invoice_h ");
		repaysql.append("     from ");
		repaysql.append("       hzvat_invoice_h ");
		repaysql.append("     where ");
		repaysql.append("       nvl(dr, 0) = 0 ");
		repaysql.append("       and def8 = cdm_repayrcpt.def21 ");
		repaysql.append("   ) ");

		List<String> payablelist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(payablesql.toString(), new ColumnListProcessor());

		List<String> filllist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(fillsql.toString(), new ColumnListProcessor());

		List<String> bxzblist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(bxzbsql.toString(), new ColumnListProcessor());

		List<String> finblelist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(finsql.toString(), new ColumnListProcessor());

		List<String> ticketlist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(ticketsql.toString(), new ColumnListProcessor());

		List<String> repaylist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(repaysql.toString(), new ColumnListProcessor());

		payablelist.addAll(filllist);
		payablelist.addAll(bxzblist);
		payablelist.addAll(finblelist);
		payablelist.addAll(ticketlist);
		payablelist.addAll(repaylist);

		return payablelist;
	}
}
