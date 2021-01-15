package nc.bs.tg.alter.plugin.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.AoutSysncEbsData;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.uap.lock.PKLock;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.ep.bx.BXVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.bpm.NcToBpmVO;
/**
 * 网报请款单结算完成之后调用BPM归档
 * @author ln
 *
 */
public class AutoPushSettledBXtoBpmTask extends AoutSysncEbsData{
	IPushBPMBillFileService fileService = null;
	IMDPersistenceQueryService mdQryService = null;
	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = getSettInfoCurrent();
		String[] msgObj = new String[2];
		NcToBpmVO vo = null;
		if (settMap != null && settMap.size() > 0) {
			for (Map<String, Object> map : settMap) {
				vo = new NcToBpmVO();
				BXVO aggVO = (BXVO) getBillVO(
						BXVO.class,
						"isnull(dr,0)=0 and pk_jkbx = '" + map.get("pk_jkbx") + "'");
				try {
					msgObj[0] = (String) map.get("pk_jkbx");
					//推送bpm
					vo.setApprovaltype("ProcessApproved");
					vo.setOperationtype("none");
					vo.setTaskid(aggVO.getParentVO().getZyx30());
					vo.setDesbill(aggVO.getParentVO().getZyx10());//流程类别
					Map<String,Object> resultMap = getPushBPMBillFileService().pushBillToBpm(vo,(String)map.get("pk_settlement"));
					msgObj[1] = (String) resultMap.get("flag");
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				}finally {
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
	private List<Map<String, Object>> getSettInfoCurrent()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct p.pk_jkbx pk_jkbx, ");
		sql.append("	t.pk_settlement pk_settlement FROM cmp_settlement t ");
		sql.append("left join cmp_detail d on d.pk_settlement = t.pk_settlement ");
		sql.append("left join er_bxzb p on p.pk_jkbx = t.pk_busibill ");
		sql.append("where t.dr= 0 and d.dr=0  and d.def1 <> 'Y' ");
		sql.append("      and t.settlestatus =5 and t.pk_billtype = '264X' and t.pk_tradetype in('264X-Cxx-007','264X-Cxx-008','264X-Cxx-009') ");
		sql.append(" and p.ts > '2020-03-26 00:00:00'");
//		sql.append(" and p.djbh = '264X2020032500000026'")
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
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, false,false);
		if (coll == null || coll.size() == 0) {
			throw new BusinessException("NC系统未能关联信息!");
		}
		return (AggregatedValueObject) coll.toArray()[0];
	}
	
	/**
	 * 元数据持久化查询接口
	 * 
	 * @return
	 */
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
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
