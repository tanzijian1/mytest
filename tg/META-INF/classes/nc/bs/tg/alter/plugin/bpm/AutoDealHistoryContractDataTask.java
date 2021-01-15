package nc.bs.tg.alter.plugin.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.alter.plugin.bpm.result.BPMAlterMessage;
import nc.bs.tg.alter.result.ConvertResultUtil;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 用于更新009报销单数据
 * 
 * @author ln
 * 
 */
public class AutoDealHistoryContractDataTask implements IBackgroundWorkPlugin {
	IPushBPMBillFileService fileService = null;
	IMDPersistenceQueryService queryServcie = null;

	@Override
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		List<Map<String, Object>> settMap = getSettInfoCurrent();
		List<Object[]> msglist = new ArrayList<Object[]>();
		String title = arg0.getAlertTypeName();
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new BPMAlterMessage());
		Object pk_jkbx = null;
		Object zyx2 = null;
		Object def3 = null;
		Object def4 = null;
		Object djrq = null;
		UFDouble zyx19 = UFDouble.ZERO_DBL;
		UFDouble def70 = UFDouble.ZERO_DBL;
		if (settMap != null && settMap.size() > 0) {
			for (Map<String, Object> map : settMap) {
				String[] msgObj = new String[util.getRemsg().getBodyFields().length];
				try {
					pk_jkbx = map.get("pk_jkbx");
					zyx2 = map.get("zyx2");
					def3 = map.get("def3");
					def4 = map.get("def4");
					djrq = map.get("djrq");
					StringBuffer sql = new StringBuffer();
					sql.append("select sum(total) from er_bxzb where zyx2='"+zyx2+"' and spzt =1 ");
					sql.append("and (djlxbm in('264X-Cxx-004','264X-Cxx-009') ");
					sql.append("and zyx31 = (select pk_defdoc from bd_defdoc where name = '付款计划（押金/保证金/共管金额）'and code = '002'))and djrq <'"
							+ djrq + "'");
					Object obj = EBSBillUtils
							.getUtils()
							.getBaseDAO()
							.executeQuery(sql.toString(), new ColumnProcessor());
					UFDouble historyMny = new UFDouble(
							String.valueOf(obj == null ? 0 : obj));// 历史已请款金额
					zyx19 = new UFDouble(def3 == null ? "" : def3.toString())
							.add(historyMny);
					def70 = new UFDouble(def4 == null ? "" : def4.toString())
							.add(historyMny);
					String bxsql = "update er_bxzb set zyx19 = '" + zyx19
							+ "',def70 = '" + def70 + "' where pk_jkbx = '"
							+ pk_jkbx + "'";
					EBSBillUtils.getUtils().getBaseDAO().executeUpdate(bxsql);
					msgObj[1] = "success";
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				}
				msglist.add(msgObj);
			}
		}
		return util.executeTask(title, msglist);
	}

	/**
	 * nc传bpm归档回写sql
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getSettInfoCurrent()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select v1.pk_jkbx,v1.djbh,v1.djrq,v1.zyx2,v1.zyx19, ");
		sql.append("case when v2.def3 = '~' then '0' when v2.def3 is null then '0' else v2.def3 end as def3,v1.def70, ");
		sql.append("case when v2.def4 = '~' then '0' when v2.def4 is null then '0' else v2.def4 end as def4 ");
		sql.append("from er_bxzb v1 ");
		sql.append("left join yer_contractfile v2 on v2.code = v1.zyx2 and v2.def15 != '0' and v2.enablestate = 2 and v2.dr = 0 ");
		sql.append("where v1.djlxbm = '264X-Cxx-009' and v1.dr = 0 ");
//		sql.append(" and v1.djbh = '264X20200615005860' ");
		sql.append(" and v1.djrq < '2020-07-02 23:59:59' ");
		sql.append("order by v1.djrq desc");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		return list;
	}

	/**
	 * 读取业务单据聚合VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getQueryServcie().queryBillOfVOByCond(c,
				whereCondStr, false, false);
		if (coll == null || coll.size() == 0) {
			throw new BusinessException("NC系统未能关联信息!");
		}
		return (AggregatedValueObject) coll.toArray()[0];
	}

	/**
	 * 后台推送bpm接口
	 * 
	 * @return
	 */
	public IPushBPMBillFileService getPushBPMBillFileService() {
		if (fileService == null) {
			fileService = NCLocator.getInstance().lookup(
					IPushBPMBillFileService.class);
		}
		return fileService;
	}

	public IMDPersistenceQueryService getQueryServcie() {
		if (queryServcie == null) {
			queryServcie = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return queryServcie;
	}

}
