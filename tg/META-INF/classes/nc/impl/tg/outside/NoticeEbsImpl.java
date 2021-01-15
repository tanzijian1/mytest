package nc.impl.tg.outside;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.os.outside.TGCallUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.INoticeEbs;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.erm.matterapp.MatterAppVO;
import nc.vo.erm.matterapp.MtAppPayplanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import uap.distribution.util.StringUtil;

import com.alibaba.fastjson.JSONObject;

public class NoticeEbsImpl extends BillUtils implements INoticeEbs {

	@Override
	public void updateFlag_RequiresNew(String updateSql)
			throws BusinessException {
		if (!StringUtil.isBlank(updateSql)) {
			EBSBillUtils.getUtils().getBaseDAO().executeUpdate(updateSql);
		}
	}

	@Override
	public void pushMattContractDataToEbs(AggMatterAppVO aggvo, String apflag,
			Boolean isSetted) throws BusinessException {
		MatterAppVO hvo = aggvo.getParentVO();
		String fnumber = hvo.getDefitem7();
		if (!StringUtil.isBlank(fnumber)) {
			JSONObject content = new JSONObject();
			JSONObject req = IncontentByReq(content);
			content.put("apflag", apflag);// 类型标识 (该字段值可为10(新增或更新)或20(删除))
			content.put("fnumber", fnumber);// 合同编码
			content.put("askpaymentnumber", hvo.getBillno());// 请款单号

			String sql = "select code from bd_defdoc where pk_defdoc = '"
					+ hvo.getDefitem36() + "'";
			String result = getIUAPQueryBS(sql);// MARGIN--保证金/押金；CONTRACT--动态金额
			Integer approvestatus = hvo.getApprstatus();
			String ncstatuscode = "";
			// 0=审批未通过，1=审批通过，2=审批进行中，3=提交，-1=自由，
			switch (approvestatus) {
			case 0:
				ncstatuscode = "approving";
				break;
			case 1:
				ncstatuscode = "approved";
				break;
			case 2:
				ncstatuscode = "approving";
				break;
			case 3:
				ncstatuscode = "approving";
				break;
			case -1:
				ncstatuscode = "approving";
				break;
			default:
				break;
			}
			content.put("ncaskpaymentid", hvo.getPrimaryKey());//
			content.put("askpaymenttype", result);// 请款类型
			content.put("invoiceamount", hvo.getDefitem25());// 发票金额
			content.put("ncstatuscode", ncstatuscode);// 请款状态
			String date = hvo.getCreationtime().toString();
			if (!StringUtil.isBlank(date)) {
				content.put("askpaymentdate", date.subSequence(0, 10));// 请款日期
			}
			content.put("source", "NC");// 请款来源
			content.put("fconid", "");// 合同ID
			List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
			purchaseDetaillist = getLLPurchaseDetaillist(aggvo, isSetted);
			content.put("apline", purchaseDetaillist);
			TGCallUtils.getUtils().onDesCallService(hvo.getPrimaryKey(), "EBS",
					"onPushConDataToEbs", req);
		}
	}

	/**
	 * 请款明细
	 * 
	 * @param aggvo
	 * @return
	 */
	private List<Map<String, Object>> getLLPurchaseDetaillist(
			AggMatterAppVO aggvo, Boolean isSetted) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
		String sql = null;
		CircularlyAccessibleValueObject[] vos = aggvo
				.getTableVO("mtapp_payplan");
		if (vos != null && vos.length > 0) {
			for (CircularlyAccessibleValueObject vo : vos) {
				Map<String, Object> purchaseDetail = new HashMap<String, Object>();// 表体数据
				Object applymny = vo.getAttributeValue(MtAppPayplanVO.APPLYMNY);
				if (applymny != null) {
					sql = "select code from bd_defdoc where pk_defdoc = '"
							+ vo.getAttributeValue(MtAppPayplanVO.DEFITEM1)
							+ "' and nvl(dr,0) = 0";
					purchaseDetail.put("charactertype", getIUAPQueryBS(sql));// 款项性质
					purchaseDetail.put("askamount", applymny == null ? ""
							: applymny.toString());// 本次请款金额
					if (isSetted) {
						purchaseDetail.put("paymentamount",
								applymny == null ? "" : applymny.toString());// 付款金额
					}
					purchaseDetail.put("attribute15",
							vo.getAttributeValue(MtAppPayplanVO.PK_EBS));// EBS主键
					purchaseDetaillist.add(purchaseDetail);
				}
			}
		}
		return purchaseDetaillist;
	}

	@Override
	public void pushBXContractDataToEbs(BXVO aggvo, String apflag,
			Boolean isSetted) throws BusinessException {
		JKBXHeaderVO hvo = aggvo.getParentVO();
		String fnumber = hvo.getZyx2();
		if (!StringUtil.isBlank(fnumber)) {
			JSONObject content = new JSONObject();
			JSONObject req = IncontentByReq(content);
			Integer approvestatus = hvo.getSpzt();
			String ncstatuscode = "";
			// 0=审批未通过，1=审批通过，2=审批进行中，3=提交，-1=自由，
			switch (approvestatus) {
			case 0:
				ncstatuscode = "approving";
				break;
			case 1:
				ncstatuscode = "approved";
				break;
			case 2:
				ncstatuscode = "approving";
				break;
			case 3:
				ncstatuscode = "approving";
				break;
			case -1:
				ncstatuscode = "approving";
				break;
			default:
				break;
			}
			content.put("apflag", apflag);// 类型标识 (该字段值可为10(新增或更新)或20(删除))
			content.put("fnumber", fnumber);// 合同编码
			content.put("askpaymentnumber", hvo.getDjbh());// 请款单号
			content.put("ncaskpaymentid", hvo.getPrimaryKey());//
			content.put("askpaymenttype", "CONTRACT");// 请款类型
			content.put("ncstatuscode", ncstatuscode);// 请款状态
			String date = hvo.getDjrq().toString();
			if (!StringUtil.isBlank(date)) {
				content.put("askpaymentdate", date.subSequence(0, 10));// 请款日期
			}
			content.put("source", "NC");// 请款来源
			content.put("fconid", "");// 合同ID
			List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
			purchaseDetaillist = getBXPurchaseDetaillist(aggvo, isSetted);
			content.put("apline", purchaseDetaillist);
			TGCallUtils.getUtils().onDesCallService(hvo.getPrimaryKey(), "EBS",
					"onPushConDataToEbs", req);
		}
	}

	/**
	 * 请款明细
	 * 
	 * @param aggvo
	 * @return
	 */
	private List<Map<String, Object>> getBXPurchaseDetaillist(BXVO aggvo,
			Boolean isSetted) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
		BXBusItemVO[] detailvos = aggvo.getChildrenVO();
		for (BXBusItemVO vo : detailvos) {
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// 表体数据
			purchaseDetail.put("askamount", vo.getAmount() == null ? 0 : vo
					.getAmount().toString());// 请款金额
			if (isSetted) {
				purchaseDetail.put("paymentamount", vo.getAmount() == null ? 0
						: vo.getAmount().toString());// 付款金额
			}
			purchaseDetail.put("charactertype", "A3");// 款项性质
			purchaseDetail.put("ncaskpaymentlineid", vo.getPrimaryKey());
			purchaseDetail.put("attribute15","0");// EBS主键
			purchaseDetaillist.add(purchaseDetail);
		}
		return purchaseDetaillist;
	}

	private JSONObject IncontentByReq(JSONObject content) {
		JSONObject req = new JSONObject();
		req.put("req", content);
		return req;
	}

	protected String getIUAPQueryBS(String sql) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String result = "";
		try {
			result = (String) bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}

}
