package nc.bs.tg.alter.plugin.ebs;

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
import nc.impl.pub.ace.IsqlThreadimpl;
import nc.itf.tg.outside.INoticeEbs;
import nc.itf.tg.outside.IPushBPMLLBillService;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 将结算完成参照合同请款的费用申请单推送给EBS
 * 
 * @author ln
 * 
 */
public class AutoPushLLSettledSQ2EbsTask implements IBackgroundWorkPlugin {
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
					AggMatterAppVO aggVO = (AggMatterAppVO) getBillVO(
							AggMatterAppVO.class,
							"isnull(dr,0)=0 and pk_mtapp_bill = '"
									+ map.get("pk_mtapp_bill") + "'");
					String billno = (String) aggVO.getParentVO()
							.getAttributeValue(PayBillVO.BILLNO);
					msgObj[0] = billno;
					// 推送EBS
					String pk = (String) map.get("pk_settlement");
					INoticeEbs service = NCLocator.getInstance().lookup(
							INoticeEbs.class);
					service.pushMattContractDataToEbs(aggVO, "10", true);
					msgObj[1] = "true";

					String sql = "update cmp_detail set def3 = 'Y' where pk_settlement = '"
							+ pk + "'";
					service.updateFlag_RequiresNew(sql);
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
		sql.append("SELECT distinct mt.pk_mtapp_bill,");
		sql.append("  t.pk_settlement pk_settlement FROM er_mtapp_bill mt ");
		sql.append("left join ap_paybill pay on mt.defitem1 = pay.bpmid ");
		sql.append("left join cmp_settlement t on t.pk_busibill = pay.pk_paybill ");
		sql.append("left join cmp_detail d on d.pk_settlement = t.pk_settlement  ");
		sql.append("where t.dr= 0 and d.dr=0  and d.def3 <> 'Y' and mt.pk_tradetype ");
		sql.append("in('261X-Cxx-LL02') and t.settlestatus = 5");
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