package nc.bs.tg.alter.plugin.bpm;

import java.util.ArrayList;
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
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.pub.BusinessException;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class AoutNcFinToBpmNoneData extends AoutSysncEbsData{

	IPushBPMBillFileService fileService = null;
	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> repaylist = getBillVO(AggRePayReceiptBankCreditVO.class);//还款单
		List<Map<String, Object>> finpaylist = getBillVO(AggFinancexpenseVO.class);//融资费用/财顾费
		List<Map<String, Object>> addticketlist = getBillVO();//补票单
		List<Map<String, Object>> marketlist = getAggMarketRepalayVO();
		String[] msgObj = new String[2];
		
		if(repaylist.size()>0){
			for (Map<String, Object> map : repaylist) {
				try {
					msgObj[0] = map.get("pk_repayrcpt").toString();
					Map<String,Object> resultMap = getPushBPMBillFileService().pushBPMBillFileNone(map,"0");
					msgObj[1] = (String) resultMap.get("flag");
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				}finally {
					msglist.add(msgObj);
					PKLock.getInstance().releaseDynamicLocks();
				}
			}
		}
		if(finpaylist.size()>0){
			for (Map<String, Object> map : finpaylist) {
				try {
					msgObj[0] = map.get("pk_finexpense").toString();
					Map<String,Object> resultMap = getPushBPMBillFileService().pushBPMBillFileNone(map,"1");
					msgObj[1] = (String) resultMap.get("flag");
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				}finally {
					msglist.add(msgObj);
					PKLock.getInstance().releaseDynamicLocks();
				}
			}
		}
		if(addticketlist.size()>0){
			for (Map<String, Object> map : addticketlist) {
				try {
					msgObj[0] = map.get("pk_ticket").toString();
					Map<String,Object> resultMap = getPushBPMBillFileService().pushBPMBillFileNone(map,"2");
					msgObj[1] = (String) resultMap.get("flag");
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				}finally {
					msglist.add(msgObj);
					PKLock.getInstance().releaseDynamicLocks();
				}
			}
		}
		if(marketlist.size()>0){
			for (Map<String, Object> map : marketlist) {
				try {
					msgObj[0] = (String) map.get("pk_marketreplay");
					Map<String, Object> resultMap = getPushBPMBillFileService().pushBPMBillFileNone(map,"3");
					msgObj[1] = (String) resultMap.get("flag");
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();
				}finally {
					msglist.add(msgObj);
					PKLock.getInstance().releaseDynamicLocks();
				}
			}
		}
		return msglist;
	}
	
	/**
	 * nc传bpm归档回写sql(融资/财顾费/还款单)
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getBillVO(Class<?> class1)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		if(AggFinancexpenseVO.class.isAssignableFrom(class1)){
			sql.append("select distinct r.pk_finexpense, ");
			sql.append(" r.def19 bpmid, ");
		}else if(AggRePayReceiptBankCreditVO.class.isAssignableFrom(class1)){
			sql.append("select distinct r.pk_repayrcpt, ");
			sql.append(" r.vdef19 bpmid, ");
		}
		sql.append(" t.pk_settlement pk_settlement ");
		sql.append(" from cmp_detail d ");
		sql.append(" left join cmp_settlement t on t.pk_settlement = d.pk_settlement ");
		sql.append(" left join cmp_paybill p on t.pk_busibill = p.pk_paybill ");
		if(AggFinancexpenseVO.class.isAssignableFrom(class1)){
			sql.append(" left join tgrz_financexpense r on r.pk_finexpense = p.pk_upbill ");
		}else if(AggRePayReceiptBankCreditVO.class.isAssignableFrom(class1)){
			sql.append(" left join cdm_repayrcpt r on r.pk_repayrcpt = p.pk_upbill ");
		}
		sql.append(" where d.def1 <> 'Y'  ");
		sql.append(" and r.def39 <> 'Y' ");
		sql.append(" and t.settlestatus = 5 ");
		sql.append(" and nvl(t.dr,0) = 0 ");
		sql.append(" and nvl(p.dr,0) = 0  ");
		sql.append(" and nvl(r.dr,0) = 0  ");
		if(AggFinancexpenseVO.class.isAssignableFrom(class1)){
			sql.append(" and r.def19 <> '~' ");
			sql.append(" and r.def19 is not null ");
		}else if(AggRePayReceiptBankCreditVO.class.isAssignableFrom(class1)){
			sql.append(" and r.vdef19 <> '~' ");
			sql.append(" and r.vdef19 is not null ");
			//sql.append(" and r.vbillno = 'HK20060005' ");
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}
	
	/**
	 * 补票单归档回写sql
	 * 
	 * @param bgwc
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getBillVO()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select b.def19 bpmid, ");
		sql.append(" b.pk_ticket pk_ticket ");
		sql.append(" from  ");
		sql.append(" tg_addticket b ");
		sql.append(" where  ");
		sql.append(" nvl(b.dr,0) = 0 and b.def39 <> 'Y' and b.def19 <> '~' and b.def19 is not null and b.approvestatus =1 ");
		//sql.append(" and  b.billno = 'RZ302020041500000004' ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}
	
	/**
	 * 资本市场还款归档
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String,Object>> getAggMarketRepalayVO() throws BusinessException{
		String sql = "select def20 bpmid, pk_marketreplay from sdfn_marketrepalay where approvestatus=1"
					+ " and nvl(dr,0) = 0 and def39 <> 'Y' and def20 <> '~' and def20 is not null"
					+ " and ((def42='Y' and def43='Y') or (def42<>'Y' and def43<>'Y'))";
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
