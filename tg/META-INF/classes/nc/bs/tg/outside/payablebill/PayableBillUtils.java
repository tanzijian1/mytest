package nc.bs.tg.outside.payablebill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.core.service.TimeService;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.paybill.PayBillCreateForSrmrf;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.calulate.CalultateUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class PayableBillUtils extends BillUtils implements
		ITGSyncService {
	String pk_tradetype = null;
	boolean isPushApprove = false;
	String defaultoperator = null;

	public String getDefaultoperator() {
		return DefaultOperator;
	}

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(getDefaultoperator()));
		InvocationInfoProxy.getInstance().setUserCode(getDefaultoperator());
		// ��ϵͳ��Ϣ
		JSONObject data = (JSONObject) info.get("data");// ��ϵͳ��Դ��ͷ����
		JSONObject jsonhead = (JSONObject) data.get("headInfo");// ��ϵͳ��Դ��ͷ����
		// SRM�������ҵ��,��Ԥ������ֱ���߸�� -start-
		String isadvancepay = jsonhead.getString("isadvancepay");// �Ƿ�Ԥ����
		if ("Y".equals(isadvancepay)) {
			return new PayBillCreateForSrmrf().onSyncInfo(info, methodname);
		}
		// SRM�������ҵ��,��Ԥ������ֱ���߸�� -end-

		String srcid = jsonhead.getString("srcid");// ��ԴID
		String srcno = jsonhead.getString("srcbillno");// ��Դ���ݺ�

		Map<String, String> resultInfo = new HashMap<String, String>();
		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
					AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��"
						+ billkey
						+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "��,�����ظ��ϴ�!");
			}
			AggPayableBillVO billvo = onTranBill(info);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			WorkflownoteVO worknoteVO = getWorkflowMachine().checkWorkFlow(
					"SAVE", billvo.getHeadVO().getPk_tradetype(), billvo,
					new HashMap<String, Object>());
			Object obj = getPfBusiAction().processAction("SAVE",
					billvo.getHeadVO().getPk_tradetype(), worknoteVO, billvo,
					null, null);
			AggPayableBillVO[] billvos = (AggPayableBillVO[]) obj;
			if (isPushApprove()) {
				approveSilently(billvo.getHeadVO().getPk_tradetype(),
						billvos[0].getPrimaryKey(), "Y", "���ͨ��", billvos[0]
								.getHeadVO().getCreator(), false);
			}

			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayableBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected AggPayableBillVO onTranBill(HashMap<String, Object> info)
			throws BusinessException {
		JSONObject data = (JSONObject) info.get("data");// ��ϵͳ��Դ��ͷ����
		JSONObject head = (JSONObject) data.get("headInfo");// ��ϵͳ��Դ��ͷ����
		JSONArray bodys = (JSONArray) data.get("bodyInfos");// ��ϵͳ��Դ��������
		AggPayableBillVO billVO = onDefaultValue(head, bodys);
		// ������Ϣת��
		UFDouble money = UFDouble.ZERO_DBL;// ���
		UFDouble local_money = UFDouble.ZERO_DBL;// ��֯���
		UFDouble group_money = UFDouble.ZERO_DBL;// ���Ž��
		UFDouble global_money = UFDouble.ZERO_DBL;// ȫ�ֽ��
		// ��ϸ��Ϣת��
		List<PayableBillItemVO> blists = new ArrayList<PayableBillItemVO>();
		if (bodys != null && bodys.size() > 0) {
			for (int row = 0; row < bodys.size(); row++) {
				try {
					JSONObject body = bodys.getJSONObject(row);
					PayableBillItemVO itmevo = (PayableBillItemVO) billVO
							.getChildrenVO()[row];
					setItemVO((PayableBillVO) billVO.getParentVO(), itmevo,
							body);
					CalultateUtils.getUtils()
							.calculate(billVO, IBillFieldGet.MONEY_CR, row,
									Direction.CREDIT.VALUE);
					CalultateUtils.getUtils().calculate(billVO,
							IBillFieldGet.LOCAL_TAX_CR, row,
							Direction.CREDIT.VALUE);
					itmevo.setOccupationmny(itmevo.getMoney_cr());// Ԥռ��ԭ�����

					money = money.add(itmevo.getMoney_cr());// ���
					local_money = local_money.add(itmevo.getLocal_money_cr());// ��֯���
					group_money = group_money.add(itmevo.getGroupcrebit());// ���Ž��
					global_money = global_money.add(itmevo.getGlobalcrebit());// ȫ�ֽ��
					blists.add(itmevo);
				} catch (Exception e) {
					throw new BusinessException("��[" + (row + 1) + "],"
							+ e.getMessage(), e);
				}
			}
		}

		billVO.getParentVO().setAttributeValue(PayableBillVO.MONEY, money);
		billVO.getParentVO().setAttributeValue(PayableBillVO.LOCAL_MONEY,
				local_money);
		billVO.getParentVO().setAttributeValue(PayableBillVO.GROUPLOCAL,
				group_money);
		billVO.getParentVO().setAttributeValue(PayableBillVO.GLOBALLOCAL,
				global_money);

		return billVO;
	}

	protected AggPayableBillVO onDefaultValue(JSONObject head,
			JSONArray bodylist) throws BusinessException {
		AggPayableBillVO aggvo = new AggPayableBillVO();
		PayableBillVO hvo = new PayableBillVO();
		String billdate = head.getString("billdate") == null ? new UFDate()
				.toString() : head.getString("billdate");
		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// ��ǰʱ��
		hvo.setAttributeValue(PayableBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// ����
		hvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// �ڳ���־
		hvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.FALSE);// �Ƿ����
		hvo.setAttributeValue(PayableBillVO.BILLDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(PayableBillVO.BUSIDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// ���ݻ�����
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// ���ݻ���ڼ�
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);

		setHeaderVO(hvo, head);

		hvo.setAttributeValue(PayableBillVO.BILLMAKER,
				getUserIDByCode(getDefaultoperator()));// �Ƶ���
		hvo.setAttributeValue(PayableBillVO.CREATOR, hvo.getBillmaker());// ������
		hvo.setAttributeValue(PayableBillVO.CREATIONTIME, currTime);// ����ʱ��
		hvo.setAttributeValue(PayableBillVO.PK_BILLTYPE, IBillFieldGet.F1);// �������ͱ���
		hvo.setAttributeValue(PayableBillVO.BILLCLASS, IBillFieldGet.YF);// ���ݴ���
		hvo.setAttributeValue(PayableBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ��������ϵͳ
		hvo.setAttributeValue(PayableBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ������Դϵͳ
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, IBillFieldGet.D1);// Ӧ������code
		hvo.setAttributeValue(PayableBillVO.BILLSTATUS,
				ARAPBillStatus.SAVE.VALUE);// ����״̬
		// ��������
		BilltypeVO billTypeVo = PfDataCache.getBillType(StringUtils
				.isBlank(head.getString("tradetype")) ? getTradetype() : head
				.getString("tradetype"));

		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPEID,
				billTypeVo.getPk_billtypeid());// Ӧ������
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE,
				billTypeVo.getPk_billtypecode());// Ӧ������

		aggvo.setParentVO(hvo);
		PayableBillItemVO[] itemVOs = new PayableBillItemVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new PayableBillItemVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// �к�
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
		getArapBillPubQueryService().getDefaultVO(aggvo, true);

		return aggvo;
	}

	/**
	 * ������Ϣ
	 * 
	 * @param hvo
	 * @param headvo
	 */
	protected abstract void setHeaderVO(PayableBillVO hvo, JSONObject head)
			throws BusinessException;

	/**
	 * ��ϸ��Ϣ
	 * 
	 * @param parentVO
	 * @param itmevo
	 * @param row
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected abstract void setItemVO(PayableBillVO parentVO,
			PayableBillItemVO itmevo, JSONObject body) throws BusinessException;

	/**
	 * ��Ӧ�ӿ���ָ��Ľ�������
	 */
	protected abstract String getTradetype();

	/**
	 * �Ƿ������
	 * 
	 * @return
	 */
	public boolean isPushApprove() {
		return isPushApprove;
	}

}
