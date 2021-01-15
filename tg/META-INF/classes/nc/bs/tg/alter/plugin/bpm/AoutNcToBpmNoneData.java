package nc.bs.tg.alter.plugin.bpm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.AoutSysncEbsData;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.salebpm.utils.SyncSaleBPMBillStatesUtils;
import nc.bs.uap.lock.PKLock;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OutsideLogVO;

public class AoutNcToBpmNoneData extends AoutSysncEbsData{

	IPushBPMBillFileService fileService = null;
	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = getSettInfoCurrent(bgwc);
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(
				IPushBPMBillFileService.class);
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill("NC调用BPM归档接口后台任务");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String[] msgObj = new String[2];
		if (settMap != null && settMap.size() > 0) {
			for (Map<String, Object> map : settMap) {
//				AggPayBillVO aggVO = (AggPayBillVO) getBillVO(
//						AggPayBillVO.class,
//						"isnull(dr,0)=0 and pk_paybill = '" + map.get("pk_paybill") + "'");
				try {
					msgObj[0] = (String) map.get("pk_paybill");
					//推送bpm
					Map<String,Object> resultMap = getPushBPMBillFileService().pushBPMBillFileNone((String) map.get("pk_paybill"),(String)map.get("pk_settlement"));
					msgObj[1] = (String) resultMap.get("flag");
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
					logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
							.getFullStackTrace(e));
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				}finally {
					logVO.setSrcparm((String) map.get("pk_paybill"));
					try {
						service.saveLog_RequiresNew(logVO);
					} catch (Exception e1) {
						throw new BusinessException("日志录入数据库错误:"+e1.getMessage());
					}
					PKLock.getInstance().releaseDynamicLocks();
				}
				msglist.add(msgObj);
			}
		}
		return msglist;
	}
	
	/**
	 * nc传bpm归档回写sql
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getSettInfoCurrent(BgWorkingContext bgwc)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct p.pk_paybill pk_paybill, ")
				.append(" t.pk_settlement pk_settlement ")
				.append(" from cmp_settlement t")
				.append(" left join cmp_detail d on d.pk_settlement = t.pk_settlement ")
				.append(" left join ap_paybill p on p.pk_paybill = t.pk_busibill")
				.append(" left join bd_balatype on bd_balatype.pk_balatype = d.pk_balatype ")
				.append(" left join bd_currtype on bd_currtype.pk_currtype = d.pk_currtype ")
				.append(" where t.dr= 0 and d.dr=0 and bd_balatype.dr =0 and bd_currtype.dr = 0 and d.def1 <> 'Y' and  p.def61 <> 'Y' ")
				.append(" and t.settlestatus =5 and t.pk_billtype = 'F3' and t.pk_tradetype in('F3-Cxx-011','F3-Cxx-012','F3-Cxx-016','F3-Cxx-017','F3-Cxx-027') ");
//				.append(" and p.billno = 'D32020032500001300' ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}

	
	/**
	 * 后台推送bpm接口
	 * @return
	 */
	public IPushBPMBillFileService getPushBPMBillFileService() {
		if (fileService == null) {
			fileService = NCLocator.getInstance().lookup(
					IPushBPMBillFileService.class);
		}
		return fileService;
	}
	
}
