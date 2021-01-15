package nc.bs.tg.alter.plugin.ll;

import java.util.ArrayList;
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
 * 邻里-收费系统退押金付款结算单结算后回写信息给物业收费系统后台任务
 * 
 * @author 2020-09-25-谈子健
 * 
 */
public class AoutSyncLLWYSFPaySettInfoTask extends AoutLLAlterTask {

	@Override
	protected AlertMessage getAlterMessage() {
		return new EBSAlterMessage();
	}

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		AlertMessage message = getAlterMessage();
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settlist = getWYSFPaySettInfo();
		if (settlist != null && settlist.size() > 0) {
			for (Map<String, Object> info : settlist) {
				Object[] obj = new Object[message.getNames().length];
				obj[0] = info.get("billcode");
				try {
					ResultVO resultVO = TGCallUtils.getUtils()
							.onDesCallService(null, "wysf",
									"PushWYSFReturnDepositInfoImpl", info);
					obj[1] = "同步成功,反馈信息:" + resultVO.getData();
				} catch (Exception e) {
					obj[1] = "同步失败,反馈信息:" + e.getMessage();
				}
				msglist.add(obj);
			}
		}

		return msglist;
	}

	private List<Map<String, Object>> getWYSFPaySettInfo()
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select l.pk_settlement,  ");
		query.append("       paybill.def2    srcbillno,  ");
		query.append("       l.orglocal      amount,  ");
		query.append("       l.billcode      billcode,  ");
		query.append("       l.settledate    settledate  ");
		query.append("  from cmp_settlement l  ");
		query.append(" inner join cmp_paybill paybill  ");
		query.append("    on paybill.pk_paybill = l.pk_busibill  ");
		query.append(" where L.PK_TRADETYPE IN ('F5-Cxx-LL05')  ");
		query.append("   and l.dr = 0  ");
		query.append("   and paybill.dr = 0  ");
		query.append("   and paybill.def2 <> '~'  ");
		query.append("   and l.def1 <> 'Y'  ");
		query.append("   and l.settlestatus = '5'  ");

		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(query.toString(), new MapListProcessor());

		return list;
	}

}
