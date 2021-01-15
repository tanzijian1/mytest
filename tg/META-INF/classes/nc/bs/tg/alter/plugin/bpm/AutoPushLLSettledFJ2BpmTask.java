package nc.bs.tg.alter.plugin.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.distribution.util.StringUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.os.outside.TGCallUtils;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.alter.plugin.bpm.result.BPMAlterMessage;
import nc.bs.tg.alter.result.ConvertResultUtil;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.IPushBPMLLBillService;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.arap.baddebts.BillVO;
import nc.vo.cmp.bill.BillAggVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 邻里报销单单据结算完成之后调用BPM归档接口
 * 
 * @author ln
 * 
 */
public class AutoPushLLSettledFJ2BpmTask implements IBackgroundWorkPlugin {
	IPushBPMLLBillService fileService = null;
	IMDPersistenceQueryService queryServcie = null;

	@Override
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		List<Map<String, Object>> settMap = getSettInfoCurrent();
		List<Object[]> msglist = new ArrayList<Object[]>();
		String title = arg0.getAlertTypeName();
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new BPMAlterMessage());
		if (settMap != null && settMap.size() > 0) {
			for (Map<String, Object> map : settMap) {
				String[] msgObj = new String[util.getRemsg().getBodyFields().length];
				try {
					BillAggVO aggVO = (BillAggVO) getBillVO(
							BillAggVO.class,
							"isnull(dr,0)=0 and pk_paybill = '"
									+ map.get("pk_jkbx") + "'");
					String billno = (String) aggVO.getParentVO()
							.getAttributeValue("bill_no");
					msgObj[0] = billno;
					// 推送bpm
					String tradetype = (String) aggVO.getParentVO()
							.getAttributeValue("trade_type");
					String pk = (String) map.get("pk_settlement");
					Map<String, Object> map1 = new HashMap<String, Object>();
					String taskid = (String) aggVO.getParentVO()
							.getAttributeValue(BillVO.DEF30);
					if (!StringUtil.isBlank(taskid)) {
						map1.put("TaskID", taskid);
						map1.put("Comments", "结算完成");
						map1.put("SystemIdentification", "Capital");
						map1.put("pk_settlement", pk);
						map1.put("tradetype", tradetype);
						map1.put("usercode", "OtherSysOpt");
						TGCallUtils.getUtils().onDesCallService(
								aggVO.getParentVO().getPrimaryKey(), "BPM",
								"ProcessApprove", map1);
						msgObj[1] = "true";
					} else {
						throw new BusinessException("当前单据[" + billno
								+ "]BPM主键为空，无法进行归档!");
					}
				} catch (Exception e) {
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
		sql.append("SELECT distinct t.pk_busibill pk_jkbx,");
		sql.append("  t.pk_settlement pk_settlement FROM cmp_settlement t ");
		sql.append("left join cmp_detail d on d.pk_settlement = t.pk_settlement ");
		sql.append("where t.dr= 0 and d.dr=0  and d.def1 <> 'Y' and t.pk_tradetype = 'F5-Cxx-LL02' and t.settlestatus = 5;");
		@SuppressWarnings("unchecked")
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
	public IPushBPMLLBillService getPushBPMBillFileService() {
		if (fileService == null) {
			fileService = NCLocator.getInstance().lookup(
					IPushBPMLLBillService.class);
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
