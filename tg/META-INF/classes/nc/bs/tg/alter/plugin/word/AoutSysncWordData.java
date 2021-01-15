package nc.bs.tg.alter.plugin.word;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.alter.plugin.word.result.WordAlterMessage;
import nc.bs.tg.alter.result.ConvertResultUtil;
import nc.bs.tg.outside.word.utils.WordBillUtils;
import nc.itf.tg.outside.ISyncWordBillServcie;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.lang.StringUtils;

/**
 * NC调用Word接口交互处理任务
 * 
 * @author ASUS
 * 
 */
public abstract class AoutSysncWordData implements IBackgroundWorkPlugin {
	BaseDAO baseDAO = null;
	ISyncWordBillServcie WordService = null;

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		String title = bgwc.getAlertTypeName();
		String[] orgids = StringUtils.isEmpty((String) bgwc.getKeyMap().get(
				"orgids")) ? null : ((String) bgwc.getKeyMap().get("orgids"))
				.split(",");
		UFDate begin = new UFDate((String) bgwc.getKeyMap().get("begin"));
		UFDate end = new UFDate((String) bgwc.getKeyMap().get("end"));
		if (begin == null) {
			begin = new UFDate();
		}
		if (end == null) {
			end = new UFDate();
		}

		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new WordAlterMessage());
		List<String> yearmthlist = WordBillUtils.getUtils().getYearMthList(
				begin, end);
		List<Object[]> msglist = new ArrayList<Object[]>();
		if (orgids == null) {
			String sql = "select org_financeorg.pk_financeorg from org_financeorg where org_financeorg.enablestate = 2 and org_financeorg.dr = 0";
			List<String> orglist = (List<String>) getBaseDAO().executeQuery(
					sql, new ColumnListProcessor());
			orgids = orglist.toArray(new String[0]);
		}

		for (String orgid : orgids) {
			for (String yearmth : yearmthlist) {
				String[] msg = new String[util.getRemsg().getNames().length];
				msg[0] = WordBillUtils.getUtils().getOrgName(orgid);
				msg[1] = yearmth;
				String result = null;
				try {
					result = getWorkResult(orgid, yearmth);
					msg[2] = result;
				} catch (Exception e) {
					msg[2] = e.getMessage();
					Logger.error(e.getMessage(), e);
				}
				msglist.add(msg);
			}

		}

		return util.executeTask(title, msglist);
	}

	protected abstract String getWorkResult(String orgid, String yearmth)
			throws BusinessException;

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	public ISyncWordBillServcie getWordServcie() {
		if (WordService == null) {
			WordService = NCLocator.getInstance().lookup(
					ISyncWordBillServcie.class);
		}
		return WordService;
	}
}
