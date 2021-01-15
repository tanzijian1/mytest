package nc.bs.tg.alter.plugin.ll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.os.outside.TGCallUtils;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ll.message.AlertMessage;
import nc.bs.tg.alter.plugin.ll.message.EBSAlterMessage;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.ResultVO;

/**
 * 供应商请款结算之后信息回写
 * 
 * @author ASUS
 * 
 */
public class AoutSyncLLSRMPaySettInfoTask extends AoutLLAlterTask {

	@Override
	protected AlertMessage getAlterMessage() {
		return new EBSAlterMessage();
	}

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		AlertMessage message = getAlterMessage();
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settlist = getSrmPaySettInfo();
		if (settlist != null && settlist.size() > 0) {
			for (Map<String, Object> info : settlist) {
				Object[] obj = new Object[message.getNames().length];
				obj[0] = info.get("settbillno");
				try {
					ResultVO resultVO = null;
					if ("F3-Cxx-LL03".equals(info.get("pk_tradetype"))) {
						Map<String, Object> settInfo = new HashMap<String, Object>();
						settInfo.put("TaskID", info.get("bpmid"));
						settInfo.put("Comments", "结算完成");
						settInfo.put("SystemIdentification", "Capital");
						settInfo.put("pk_settlement", info.get("pk_settlement"));
						settInfo.put("tradetype", info.get("pk_tradetype"));
						settInfo.put("usercode", "OtherSysOpt");
						resultVO = TGCallUtils.getUtils().onDesCallService(
								null, "BPM", "ProcessApprove", settInfo);
					} else {
						resultVO = TGCallUtils.getUtils().onDesCallService(
								null, "SRM", "pushSRMFundsReceiptInfo", info);
					}
					obj[1] = "同步成功,反馈信息:" + resultVO.getData();
				} catch (Exception e) {
					obj[1] = "同步失败,反馈信息:" + e.getMessage();
				}
				msglist.add(obj);
			}
		}

		return msglist;
	}

	private List<Map<String, Object>> getSrmPaySettInfo()
			throws BusinessException {
		String sql = "select L.pk_tradetype, l.pk_settlement,paybill.def2 srcbillno,paybill.def5 contcode,bd_defdoc.name requesttype,l.orglocal amount,paybill.bpmid  from cmp_settlement l  "
				+ " inner join ap_paybill paybill on paybill.pk_paybill = l.pk_busibill "
				+ " left  join bd_defdoc on bd_defdoc.pk_defdoc = paybill.def19 "
				+ " where L.PK_TRADETYPE IN('F3-Cxx-LL01','F3-Cxx-LL03','F3-Cxx-LL09') "
				+ " and l.dr = 0 and paybill.dr = 0 and paybill.def2<>'~'  "
				+ " and l.def1<>'Y' "
				+ " and l.settlestatus = '5' ";
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql, new MapListProcessor());

		return list;
	}

}
