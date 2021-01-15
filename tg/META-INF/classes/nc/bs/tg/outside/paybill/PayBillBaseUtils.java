package nc.bs.tg.outside.paybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.arap.bill.ArapBillCalUtil;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.core.service.TimeService;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.bs.tg.outside.utils.calulate.CalultateUtils;
import nc.cmp.bill.util.SysInit;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.pubitf.bbd.CurrtypeQuery;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.arap.sysinit.SysinitConst;
import nc.vo.arappub.calculator.data.RelationItemForCal_Debit;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.calculator.Calculator;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.scale.ScaleUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class PayBillBaseUtils extends BillUtils implements
		ITGSyncService {
	public static final String DefaultOperator = "LLWYSF";// Ĭ���Ƶ���
	String pk_tradetype = null;

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(DefaultOperator));
		InvocationInfoProxy.getInstance().setUserCode(DefaultOperator);
		// ��ϵͳ��Ϣ
		JSONObject data = (JSONObject) info.get("data");// ��ϵͳ��Դ��ͷ����
		JSONObject jsonhead = (JSONObject) data.get("headInfo");// ��ϵͳ��Դ��ͷ����
		String srcid = jsonhead.getString("srcid");// ��ԴID
		String srcno = jsonhead.getString("srcbillno");// ��Դ���ݺ�
		Map<String, String> resultInfo = new HashMap<String, String>();
		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��"
						+ billkey
						+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue(
								PayBillVO.BILLNO) + "��,�����ظ��ϴ�!");
			}
			AggPayBillVO billvo = onTranBill(info);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVE", "F3", null,
					billvo, null, null);

			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
			// WorkflownoteVO noteVO =
			// getWorkflowMachine().checkWorkflowActions(
			// "F1", billvos[0].getPrimaryKey());
			// getPfBusiAction().processAction("SAVE", "F1", noteVO, billvos[0],
			// null, null);
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected AggPayBillVO onTranBill(HashMap<String, Object> info)
			throws BusinessException {
		JSONObject data = (JSONObject) info.get("data");// ��ϵͳ��Դ��ͷ����
		JSONObject head = (JSONObject) data.get("headInfo");// ��ϵͳ��Դ��ͷ����
		JSONArray bodys = (JSONArray) data.get("bodyInfos");// ��ϵͳ��Դ��������
		AggPayBillVO billVO = onDefaultValue(head, bodys);
		// ������Ϣת��
		UFDouble money = UFDouble.ZERO_DBL;// ���
		UFDouble local_money = UFDouble.ZERO_DBL;// ��֯���
		UFDouble group_money = UFDouble.ZERO_DBL;// ���Ž��
		UFDouble global_money = UFDouble.ZERO_DBL;// ȫ�ֽ��
		// ��ϸ��Ϣת��
		List<PayBillItemVO> blists = new ArrayList<PayBillItemVO>();
		if (bodys != null && bodys.size() > 0) {
			for (int row = 0; row < bodys.size(); row++) {
				try {
					JSONObject body = bodys.getJSONObject(row);
					PayBillItemVO itmevo = (PayBillItemVO) billVO
							.getChildrenVO()[row];
					setItemVO((PayBillVO) billVO.getParentVO(), itmevo, body);
					CalultateUtils.getUtils().calculate(billVO,
							IBillFieldGet.MONEY_DE, row,
							Direction.DEBIT.VALUE.intValue());
					CalultateUtils.getUtils().calculate(billVO,
							IBillFieldGet.LOCAL_TAX_DE, row,
							Direction.DEBIT.VALUE.intValue());
					// calculate(billVO, IBillFieldGet.LOCAL_TAX_DE, row);
					itmevo.setOccupationmny(itmevo.getMoney_de());// Ԥռ��ԭ�����

					money = money.add(itmevo.getMoney_de());// ���
					local_money = local_money.add(itmevo.getLocal_money_de());// ��֯���
					group_money = group_money.add(itmevo.getGroupdebit());// ���Ž��
					global_money = global_money.add(itmevo.getGlobaldebit());// ȫ�ֽ��
					blists.add(itmevo);
				} catch (Exception e) {
					throw new BusinessException("��[" + (row + 1) + "],"
							+ e.getMessage(), e);
				}
			}
		}

		billVO.getParentVO().setAttributeValue(PayBillVO.MONEY, money);
		billVO.getParentVO().setAttributeValue(PayBillVO.LOCAL_MONEY,
				local_money);
		billVO.getParentVO().setAttributeValue(PayBillVO.GROUPLOCAL,
				group_money);
		billVO.getParentVO().setAttributeValue(PayBillVO.GLOBALLOCAL,
				global_money);

		return billVO;
	}

	protected AggPayBillVO onDefaultValue(JSONObject head, JSONArray bodylist)
			throws BusinessException {
		AggPayBillVO aggvo = new AggPayBillVO();
		PayBillVO hvo = new PayBillVO();
		String billdate = head.getString("billdate") == null ? new UFDate()
				.toString() : head.getString("billdate");
		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// ��ǰʱ��
		hvo.setAttributeValue(PayBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// ����
		hvo.setAttributeValue(PayBillVO.PK_ORG, DocInfoQryUtils.getUtils()
				.getOrgInfo(head.getString("pk_org")).get("pk_org"));// Ӧ��������֯->NCҵ��Ԫ����
		hvo.setAttributeValue(PayBillVO.BILLMAKER,
				getUserIDByCode(DefaultOperator));// �Ƶ���
		hvo.setAttributeValue(PayBillVO.CREATOR, hvo.getBillmaker());// ������
		hvo.setAttributeValue(PayBillVO.CREATIONTIME, currTime);// ����ʱ��
		hvo.setAttributeValue(PayBillVO.PK_BILLTYPE, IBillFieldGet.F3);// �������ͱ���
		hvo.setAttributeValue(PayBillVO.BILLCLASS, IBillFieldGet.FK);// ���ݴ���
		hvo.setAttributeValue(PayBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ��������ϵͳ
		hvo.setAttributeValue(PayBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ������Դϵͳ
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE, IBillFieldGet.D3);// Ӧ������code
		hvo.setAttributeValue(PayBillVO.BILLSTATUS, ARAPBillStatus.SAVE.VALUE);// ����״̬
		// ��������
		BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetype());

		hvo.setAttributeValue(PayBillVO.PK_TRADETYPEID,
				billTypeVo.getPk_billtypeid());// Ӧ������
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE,
				billTypeVo.getPk_billtypecode());// Ӧ������
		hvo.setAttributeValue(PayBillVO.ISINIT, UFBoolean.FALSE);// �ڳ���־
		hvo.setAttributeValue(PayBillVO.ISREDED, UFBoolean.FALSE);// �Ƿ����
		hvo.setAttributeValue(PayBillVO.BILLDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(PayBillVO.BUSIDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// ���ݻ�����
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// ���ݻ���ڼ�
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);
		setHeaderVO(hvo, head);

		aggvo.setParentVO(hvo);
		PayBillItemVO[] itemVOs = new PayBillItemVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new PayBillItemVO();
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
	protected abstract void setHeaderVO(PayBillVO hvo, JSONObject head)
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
	protected abstract void setItemVO(PayBillVO parentVO, PayBillItemVO itmevo,
			JSONObject body) throws BusinessException;

	/**
	 * ��Ӧ�ӿ���ָ��Ľ�������
	 */
	protected abstract String getTradetype();
}
