package nc.bs.tg.alter.plugin.img;

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

/**
 * 时代邻里从影像获取发票数据后台任务
 * 
 * @author ln
 * 
 */
public class AutoGetInvoceInfoFrImg extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<String> settMap = getSettInfoCurrent(bgwc);
		if (settMap != null && settMap.size() > 0) {
			for (String def3 : settMap) {
				String[] msgObj = new String[2];
				msgObj[0] = def3;
				ISyncIMGBillServcie syncIMGBillServcie = NCLocator
						.getInstance().lookup(ISyncIMGBillServcie.class);
				try {
					syncIMGBillServcie.onSyncInv_RequiresNew(def3,true);
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

		// 付款结算单
		StringBuffer paySql = new StringBuffer();
		paySql.append("SELECT pay.def21");
		paySql.append("  FROM cmp_paybill pay");
		paySql.append(" where pay.trade_type in ('F5-Cxx-LL01', 'F5-Cxx-LL02')");
		paySql.append("   and nvl(pay.def21, '~') <> '~'");
		paySql.append("   and nvl(pay.dr, 0) = 0");
		paySql.append("   and not exists (select pk_invoice_h");
		paySql.append("          from hzvat_invoice_h");
		paySql.append("         where nvl(dr, 0) = 0");
		paySql.append("           and def8 = pay.def21)");

		// 补票工单
		StringBuffer fillsql = new StringBuffer();
		fillsql.append(" select ");
		fillsql.append("   imagcode ");
		fillsql.append(" from ");
		fillsql.append("   yer_fillbill ");
		fillsql.append(" where ");
		fillsql.append("   nvl(imagcode, '~') <> '~' ");
		fillsql.append("   and nvl(dr, 0) = 0 ");
		fillsql.append("   pk_tradetype in ('267X-Cxx-LL01') ");
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
		bxzbsql.append("   and er_bxzb.djlxbm in('264X-Cxx-LL01','264X-Cxx-LL02','264X-Cxx-LL03') ");
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

		// 费用申请单
		StringBuffer finsql = new StringBuffer();
		finsql.append("SELECT mt.defitem3");
		finsql.append("  FROM er_mtapp_bill mt");
		finsql.append(" where nvl(mt.defitem3, '~') <> '~'");
		finsql.append("   and mt.pk_tradetype in ('261X-Cxx-LL01', '261X-Cxx-LL02')");
		finsql.append("   and nvl(dr, 0) = 0");
		finsql.append("   and not exists (select pk_invoice_h");
		finsql.append("          from hzvat_invoice_h");
		finsql.append("         where nvl(dr, 0) = 0");
		finsql.append("           and def8 = mt.defitem3)");

		List<String> payablelist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(paySql.toString(), new ColumnListProcessor());

		List<String> filllist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(fillsql.toString(), new ColumnListProcessor());

		List<String> bxzblist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(bxzbsql.toString(), new ColumnListProcessor());

		List<String> finblelist = (List<String>) EBSBillUtils.getUtils()
				.getBaseDAO()
				.executeQuery(finsql.toString(), new ColumnListProcessor());

		payablelist.addAll(filllist);
		payablelist.addAll(bxzblist);
		payablelist.addAll(finblelist);

		return payablelist;
	}
}
