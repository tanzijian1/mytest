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
			content.put("apflag", apflag);// ���ͱ�ʶ (���ֶ�ֵ��Ϊ10(���������)��20(ɾ��))
			content.put("fnumber", fnumber);// ��ͬ����
			content.put("askpaymentnumber", hvo.getBillno());// ����

			String sql = "select code from bd_defdoc where pk_defdoc = '"
					+ hvo.getDefitem36() + "'";
			String result = getIUAPQueryBS(sql);// MARGIN--��֤��/Ѻ��CONTRACT--��̬���
			Integer approvestatus = hvo.getApprstatus();
			String ncstatuscode = "";
			// 0=����δͨ����1=����ͨ����2=���������У�3=�ύ��-1=���ɣ�
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
			content.put("askpaymenttype", result);// �������
			content.put("invoiceamount", hvo.getDefitem25());// ��Ʊ���
			content.put("ncstatuscode", ncstatuscode);// ���״̬
			String date = hvo.getCreationtime().toString();
			if (!StringUtil.isBlank(date)) {
				content.put("askpaymentdate", date.subSequence(0, 10));// �������
			}
			content.put("source", "NC");// �����Դ
			content.put("fconid", "");// ��ͬID
			List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
			purchaseDetaillist = getLLPurchaseDetaillist(aggvo, isSetted);
			content.put("apline", purchaseDetaillist);
			TGCallUtils.getUtils().onDesCallService(hvo.getPrimaryKey(), "EBS",
					"onPushConDataToEbs", req);
		}
	}

	/**
	 * �����ϸ
	 * 
	 * @param aggvo
	 * @return
	 */
	private List<Map<String, Object>> getLLPurchaseDetaillist(
			AggMatterAppVO aggvo, Boolean isSetted) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
		String sql = null;
		CircularlyAccessibleValueObject[] vos = aggvo
				.getTableVO("mtapp_payplan");
		if (vos != null && vos.length > 0) {
			for (CircularlyAccessibleValueObject vo : vos) {
				Map<String, Object> purchaseDetail = new HashMap<String, Object>();// ��������
				Object applymny = vo.getAttributeValue(MtAppPayplanVO.APPLYMNY);
				if (applymny != null) {
					sql = "select code from bd_defdoc where pk_defdoc = '"
							+ vo.getAttributeValue(MtAppPayplanVO.DEFITEM1)
							+ "' and nvl(dr,0) = 0";
					purchaseDetail.put("charactertype", getIUAPQueryBS(sql));// ��������
					purchaseDetail.put("askamount", applymny == null ? ""
							: applymny.toString());// ���������
					if (isSetted) {
						purchaseDetail.put("paymentamount",
								applymny == null ? "" : applymny.toString());// ������
					}
					purchaseDetail.put("attribute15",
							vo.getAttributeValue(MtAppPayplanVO.PK_EBS));// EBS����
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
			// 0=����δͨ����1=����ͨ����2=���������У�3=�ύ��-1=���ɣ�
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
			content.put("apflag", apflag);// ���ͱ�ʶ (���ֶ�ֵ��Ϊ10(���������)��20(ɾ��))
			content.put("fnumber", fnumber);// ��ͬ����
			content.put("askpaymentnumber", hvo.getDjbh());// ����
			content.put("ncaskpaymentid", hvo.getPrimaryKey());//
			content.put("askpaymenttype", "CONTRACT");// �������
			content.put("ncstatuscode", ncstatuscode);// ���״̬
			String date = hvo.getDjrq().toString();
			if (!StringUtil.isBlank(date)) {
				content.put("askpaymentdate", date.subSequence(0, 10));// �������
			}
			content.put("source", "NC");// �����Դ
			content.put("fconid", "");// ��ͬID
			List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
			purchaseDetaillist = getBXPurchaseDetaillist(aggvo, isSetted);
			content.put("apline", purchaseDetaillist);
			TGCallUtils.getUtils().onDesCallService(hvo.getPrimaryKey(), "EBS",
					"onPushConDataToEbs", req);
		}
	}

	/**
	 * �����ϸ
	 * 
	 * @param aggvo
	 * @return
	 */
	private List<Map<String, Object>> getBXPurchaseDetaillist(BXVO aggvo,
			Boolean isSetted) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
		BXBusItemVO[] detailvos = aggvo.getChildrenVO();
		for (BXBusItemVO vo : detailvos) {
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// ��������
			purchaseDetail.put("askamount", vo.getAmount() == null ? 0 : vo
					.getAmount().toString());// �����
			if (isSetted) {
				purchaseDetail.put("paymentamount", vo.getAmount() == null ? 0
						: vo.getAmount().toString());// ������
			}
			purchaseDetail.put("charactertype", "A3");// ��������
			purchaseDetail.put("ncaskpaymentlineid", vo.getPrimaryKey());
			purchaseDetail.put("attribute15","0");// EBS����
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
