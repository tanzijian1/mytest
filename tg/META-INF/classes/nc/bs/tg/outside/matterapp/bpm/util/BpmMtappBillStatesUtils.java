package nc.bs.tg.outside.matterapp.bpm.util;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BPMBillUtil;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.IPushBPMLLBillService;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IPfExchangeService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.erm.matterapp.MatterAppVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import uap.distribution.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BpmMtappBillStatesUtils extends BPMBillUtil implements
		ITGSyncService {

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		JSONObject jsonhead = (JSONObject) info.get("headinfo");// ��ϵͳ��Դ��ͷ����
		valid(jsonhead);
		Map<String, String> resultInfo = new HashMap<String, String>();// ������Ϣ
		String operator = jsonhead.getString("operator");
		if (operator == null || "".equals(operator)) {
			InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
			InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		} else {
			Map<String, String> userInfo = getUserInfo(operator);
			if (userInfo == null) {
				throw new BusinessException("����Ա��" + operator
						+ "��δ����NC�û�����������,����ϵϵͳ����Ա��");
			}
		}
		String bpmid = jsonhead.getString("bpmid");// BPMID
		String action = jsonhead.getString("billstate");/*
														 * UNSAVE ���Ų����˻أ�
														 * UNAPPROVE ���������˻أ�
														 * GROUPAPPROVE ���Ų���������
														 * REGAPPROVE ��������������
														 * REFUSE �ܾ���
														 */
		String returnMsg = jsonhead.getString("returnMsg");// �˻�ԭ��
		if (returnMsg.contains("�˻ط�����")) {/*
										 * ����BPM�������ƣ� ���繲���˻ط�����NC�����Ѿ��ص�����̬
										 * ����BPM����ص�NCȡ�������ӿ�
										 * ����ֱ�ӷ��سɹ�;���˻�ԭ������NC����BPM��BPMֱ�ӷ���
										 * �ģ��漰����hz
										 * .itf.fssc.impl.billhandler.around
										 * .after.MatterHandlerAfterHandler
										 * ��nc.ui.cmp.settlement.actions.
										 * ChargebackBillAction
										 */
			resultInfo.put("BPMID", bpmid);
			resultInfo.put("msg", "��" + action + "�����ݲ������");
			return JSON.toJSONString(resultInfo);
		}

		if ("UNSAVE".equals(action) || "UNAPPROVE".equals(action)
				|| "REFUSE".equals(action)) {
			if (StringUtil.isBlank(returnMsg)) {
				throw new BusinessException("�����쳣���˻ز���ʱ�˻���Ϣ����Ϊ��!");
			}
		}
		AggMatterAppVO aggVO = (AggMatterAppVO) getBillVO(AggMatterAppVO.class,
				"nvl(dr,0)=0 and defitem1 ='" + bpmid + "'");
		try {
			if (aggVO == null) {
				throw new BusinessException("NCϵͳbpm����δ�ж�Ӧ����");
			}
			MatterAppVO hvo = aggVO.getParentVO();
			String billtype = hvo.getPk_tradetype();
			String billid = hvo.getPrimaryKey();
			String defitem14 = hvo.getDefitem14();// �Ƿ�Ԥ����
			String concode = hvo.getDefitem7();// ��ͬ���

			if ("GROUPAPPROVE".equals(action) || "REGAPPROVE".equals(action)) {
				if (hvo.getApprstatus() != 1) {
					approveSilently("261X", billid, "Y", "��׼", "", false);
				}
				// BPM��һ�ε��������ӿ�ʱ����Ӧ�������ڶ��ε���ʱ��ͬʱ����Ӧ����
				AggPayableBillVO payaggvo = (AggPayableBillVO) getBillVO(
						AggPayableBillVO.class,
						"nvl(dr,0)=0 and pk_tradetype in('F1-Cxx-LL02','F1-Cxx-LL03') and bpmid ='"
								+ bpmid + "'");
				AggPayBillVO paybillaggvo = null;
				if (payaggvo == null) {// Ӧ����Ϊnullʱ����ȥ��ѯ���
					paybillaggvo = (AggPayBillVO) getBillVO(AggPayBillVO.class,
							"nvl(dr,0)=0 and bpmid ='" + bpmid + "'");
				}
				if (payaggvo != null || paybillaggvo != null) {
					if (payaggvo != null) {// ��Ӧ������Ϊ��ʱ������Ӧ��������������
						approveSilently("F1", payaggvo.getParentVO()
								.getPrimaryKey(), "Y", "��׼", "", false);
					}
					if (paybillaggvo != null) {// �������Ϊ��ʱ�����и������������
						approveSilently("F3", paybillaggvo.getParentVO()
								.getPrimaryKey(), "Y", "��׼", "", false);
					}
				} else {
					/*
					 * 1���������뵥�Ƿ�Ԥ����ΪY��NC�͹������ɸ���� 2���������뵥�Ƿ�Ԥ����ΪN��NC�͹�������Ӧ������
					 * 3���������뵥�Ƿ������Ժ�ͬΪY���Ƿ�Ԥ����ΪN�������NC����Ӧ������������
					 * ��������һ�ź���Ӧ�����������Ӧ��ͬ���������ۼƼ�����������<�ۼƼ�����,
					 * ����Ӧ�������Ϊ�������������>�ۼƼ��������Ӧ�������Ϊ�����ۼƼ������
					 */
					if ("Y".equals(defitem14)) {
						// ���ɸ��
						try {
							createPayBill(aggVO, billtype, bpmid);
						} catch (Exception e) {
							throw new BusinessException("���ɸ���쳣:"
									+ e.getMessage());
						}
					} else {
						Object obj;
						try {
							obj = createPayableBill(payaggvo, aggVO, billtype,
									bpmid, concode, false);
						} catch (Exception e) {
							throw new BusinessException("����Ӧ�����쳣:"
									+ e.getMessage());
						}
					}
				}

			} else if ("UNSAVE".equals(action) || "UNAPPROVE".equals(action)
					|| "REFUSE".equals(action)) {// BPM���Ų����˻ز����������taskid

				// ��ֹ�������뵥������
				try {
					IPushBPMLLBillService service = NCLocator.getInstance()
							.lookup(IPushBPMLLBillService.class);
					service.dealChargebackMattApp(bpmid, returnMsg);
				} catch (Exception e) {
					throw new BusinessException("��ֹ���̷����쳣:" + e.getMessage(), e);
				}

				aggVO = (AggMatterAppVO) getBillVO(AggMatterAppVO.class,
						"nvl(dr,0)=0 and defitem1 ='" + bpmid + "'");
				aggVO.getParentVO().setDefitem2("N");
				if ("REFUSE".equals(action)) {
					aggVO.getParentVO().setDefitem1("~");
				}
				// ����VO����(��BPM�����޸�)
				updateBillWithAttrs(aggVO, new String[] { MatterAppVO.DEFITEM1,
						MatterAppVO.DEFITEM2 });
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		resultInfo.put("BPMID", bpmid);
		resultInfo.put("msg", "��" + action + "�����ݲ������");
		return JSON.toJSONString(resultInfo);
	}

	private void valid(JSONObject jobj) throws BusinessException {
		if (jobj.getString("billstate") == null || jobj.getString("") == "")
			throw new BusinessException("����״̬����Ϊ��");
		if (jobj.getString("bpmid") == null || jobj.getString("") == "")
			throw new BusinessException("BPMҵ�񵥾���������Ϊ��");
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "")
			throw new BusinessException("Ŀ�굥�ݲ���Ϊ��");
		// if ("APPROVE".equals(jobj.getString("billstate"))) {
		// if (jobj.getString("data") == null || jobj.getString("") == "")
		// throw new BusinessException("������ϢDATA����Ϊ��");
		// }
	}

	/**
	 * ���ɶ�Ӧ��Ӧ������Ϣ
	 * 
	 * @param payaggvo
	 *            Ӧ�����ۺ�VO
	 * @param aggVO
	 *            �������뵥�ۺ�VO
	 * @param billtype
	 *            �������뵥��������
	 * @param bpmid
	 *            BPMID
	 * @param isCycleProvision
	 *            �Ƿ������Լ���
	 * @throws BusinessException
	 */
	private Object createPayableBill(AggPayableBillVO payaggvo,
			AggMatterAppVO aggVO, String billtype, String bpmid,
			String concode, Boolean isCycleProvision) throws Exception {
		String pk_tradetype = null;
		if ("261X-Cxx-LL01".equals(billtype)) {
			pk_tradetype = "F1-Cxx-LL02";
		} else if ("261X-Cxx-LL02".equals(billtype)) {
			pk_tradetype = "F1-Cxx-LL03";
		}
		IPfExchangeService service = NCLocator.getInstance().lookup(
				IPfExchangeService.class);
		payaggvo = (AggPayableBillVO) service.runChangeData(billtype, pk_tradetype,
				aggVO, null);
		payaggvo.getParent().setAttributeValue(PayableBillVO.PK_TRADETYPE,
				pk_tradetype);
		Map<String, String> map = DocInfoQryUtils.getUtils().getBillType(
				String.valueOf(payaggvo.getParent().getAttributeValue(
						PayableBillVO.PK_TRADETYPE)));
		String billtypid = map.get("pk_billtypeid");
		PayableBillVO hvo = (PayableBillVO) payaggvo.getParentVO();
		UFDate billdate = hvo.getBilldate();
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPEID, billtypid);
		hvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// �ڳ���־
		if (billdate != null) {
			hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
					String.valueOf(billdate.getYear()));// ���ݻ�����
			hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
					billdate.getStrMonth());// ���ݻ���ڼ�
		}
		hvo.setAttributeValue(PayableBillVO.BILLCLASS, IBillFieldGet.YF);// ���ݴ���
		hvo.setAttributeValue(PayableBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ��������ϵͳ
		hvo.setAttributeValue(PayableBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ������Դϵͳ
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);
		hvo.setAttributeValue(PayableBillVO.BPMID, bpmid);// BPMID
		hvo.setAttributeValue(PayableBillVO.BILLSTATUS,
				ARAPBillStatus.SAVE.VALUE);// ����״̬
		if (isCycleProvision) {// ����Ӧ����
			UFDouble orig_amount = aggVO.getParentVO().getOrig_amount();
			UFDouble def64 = UFDouble.ZERO_DBL;// �ۼ����ڼ�����
			UFDouble def50 = UFDouble.ZERO_DBL;// �ۼƼ�������
			String sql = "SELECT fct_ap.def64,fct_ap.def50 FROM fct_ap "
					+ "where fct_ap.vbillcode = '" + concode
					+ "' and fct_ap.blatest = 'Y' and fct_ap.dr = 0";
			@SuppressWarnings("unchecked")
			Map<String, String> conMap = (Map<String, String>) getQueryBS()
					.executeQuery(sql, new MapProcessor());
			if (conMap != null && conMap.size() > 0) {
				def64 = new UFDouble(conMap.get("def64") == null ? ""
						: conMap.get("def64"));
				def50 = new UFDouble(conMap.get("def50") == null ? ""
						: conMap.get("def50"));
			}
			UFDouble rest_money = def64.sub(def50);
			UFDouble red_money = UFDouble.ZERO_DBL;// �����
			if (orig_amount.compareTo(rest_money) > 0) {
				hvo.setAttributeValue(PayableBillVO.LOCAL_MONEY,
						rest_money.multiply(-1));
				red_money = rest_money;
			} else {
				hvo.setAttributeValue(PayableBillVO.LOCAL_MONEY,
						orig_amount.multiply(-1));
				rest_money = orig_amount;
			}
			// ���¸����ͬ���ۼƼ�������
			red_money = red_money.add(def50);
		}

		String recaccount = null;// �տ������˺�
		String def18 = aggVO.getParentVO().getDefitem18();
		if (StringUtil.isBlank(def18)) {
			recaccount = aggVO.getParentVO().getDefitem21();
		} else {
			recaccount = def18;
		}
		hvo.setAttributeValue(PayableBillVO.RECACCOUNT, recaccount);// �տ������˺�
		hvo.setAttributeValue(PayableBillVO.PAYACCOUNT, recaccount);// ���������˻�

		String def15 = aggVO.getParentVO().getDefitem15();// �������뵥-�տ����
		String objtypeName = DocInfoQryUtils.getUtils().getDefNameInfo(def15);// �տ��������
		Integer objtype = new Integer(3);
		if (!StringUtil.isBlank(objtypeName)) {
			switch (objtypeName) {
			case "ҵ��Ա":
				objtype = 3;
				break;
			case "����":
				objtype = 2;
				break;
			case "��Ӧ��":
				objtype = 1;
				break;
			default:
				break;
			}
		}
		hvo.setAttributeValue(PayableBillVO.OBJTYPE, objtype);// �տ����
		PayableBillItemVO[] itemVOs = (PayableBillItemVO[]) payaggvo
				.getChildrenVO();
		String def16 = aggVO.getParentVO().getDefitem16();// �������뵥-�ÿ���
		String supplier = aggVO.getParentVO().getPk_supplier();// �������뵥-��Ӧ��
		String pk_bankaccbas = null;
		if (objtype == 3) {// ҵ��Ա
			String sql = "SELECT pk_bankaccsub FROM bd_bankaccsub where pk_bankaccbas = (SELECT "
					+ "pk_bankaccbas FROM bd_bankaccbas bas where bas.accnum = '"
					+ def18
					+ "' and enablestate = 2 and nvl(dr,0)=0) and nvl(dr,0)=0";
			pk_bankaccbas = (String) getQueryBS().executeQuery(sql,
					new ColumnProcessor());
		}
		for (int i$ = 0, j = itemVOs.length; i$ < j; i$++) {
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// �к�
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
			itemVOs[i$].setAttributeValue(PayableBillItemVO.OBJTYPE, objtype);
			itemVOs[i$].setAttributeValue(PayableBillItemVO.PK_PSNDOC, def16);
			itemVOs[i$].setAttributeValue(PayableBillItemVO.SUPPLIER, supplier);
			if (objtype == 3) {
				itemVOs[i$].setAttributeValue(IBillFieldGet.RECACCOUNT,
						pk_bankaccbas);
			}
		}
		IArapBillPubQueryService servie = NCLocator.getInstance().lookup(
				IArapBillPubQueryService.class);
		servie.getDefaultVO(payaggvo, true);
		HashMap hmPfExParams = new HashMap();
		hmPfExParams.put("nolockandconsist", true);
		Object obj = getPfBusiAction().processAction("SAVE", "F1", null,
				payaggvo, null, hmPfExParams);
		return obj;
	}

	/**
	 * ���ɶ�Ӧ�ĸ����Ϣ
	 * 
	 * @param aggVO
	 *            �������뵥�ۺ�VO
	 * @param billtype
	 *            �������뵥��������
	 * @param bpmid
	 *            BPMID
	 * @throws BusinessException
	 */
	private void createPayBill(AggMatterAppVO aggVO, String billtype,
			String bpmid) throws Exception {
		AggPayBillVO aggvo = new AggPayBillVO();
		String busicode = null;
		String pk_tradetype = null;
		String mbilltype = aggVO.getParentVO().getPk_tradetype();// �������뵥��������
		if ("261X-Cxx-LL01".equals(mbilltype)) {// ����-��ʷ��ͬ��Ǻ�ͬ���
			pk_tradetype = "F3-Cxx-LL07";
			busicode = "Cxx-SDLL05";
		} else if ("261X-Cxx-LL02".equals(mbilltype)) {// ����-��ͬ��Ǻ�ͬ���
			pk_tradetype = "F3-Cxx-LL06";
			busicode = "Cxx-SDLL04";
		}
		IPfExchangeService service = NCLocator.getInstance().lookup(
				IPfExchangeService.class);
		aggvo = (AggPayBillVO) service.runChangeData(billtype, pk_tradetype, aggVO,
				null);
		PayBillVO hvo = (PayBillVO) aggvo.getParentVO();

		String def15 = aggVO.getParentVO().getDefitem15();// �������뵥-�տ����
		String objtypeName = DocInfoQryUtils.getUtils().getDefNameInfo(def15);// �տ��������
		Integer objtype = new Integer(3);
		if (!StringUtil.isBlank(objtypeName)) {
			switch (objtypeName) {
			case "ҵ��Ա":
				objtype = 3;
				break;
			case "����":
				objtype = 2;
				break;
			case "��Ӧ��":
				objtype = 1;
				break;
			default:
				break;
			}
		}
		hvo.setObjtype(objtype);
		hvo.setPk_billtype("F3");
		hvo.setPk_tradetype(pk_tradetype);
		// ����Ĭ����Ϣ
		hvo.setAttributeValue("syscode", "1");
		hvo.setAttributeValue("src_syscode", "1");
		String pk_busitype = DocInfoQryUtils.getUtils().getBusitype(busicode);
		hvo.setAttributeValue("pk_busitype", pk_busitype);
		hvo.setAttributeValue("billstatus", -1);
		hvo.setAttributeValue("approvestatus", -1);
		hvo.setBpmid(bpmid);
		Map<String, String> map = DocInfoQryUtils.getUtils().getBillType(
				hvo.getPk_tradetype());
		String pk_tradetypeid = map.get("pk_billtypeid");
		hvo.setPk_tradetypeid(pk_tradetypeid);

		String pk_bankaccbas = null;
		String def18 = aggVO.getParentVO().getDefitem18();
		if (objtype == 3) {// ҵ��Ա
			String sql = "SELECT pk_bankaccbas FROM bd_bankaccbas bas where bas.accnum = '"
					+ def18 + "' and enablestate = 2 and nvl(dr,0)=0";
			pk_bankaccbas = (String) getQueryBS().executeQuery(sql,
					new ColumnProcessor());
		}

		// ��������Ϣ����Ĭ��ֵ
		PayBillItemVO[] bvos = (PayBillItemVO[]) aggvo.getChildrenVO();
		for (PayBillItemVO bvo : bvos) {
			bvo.setObjtype(objtype);
			bvo.setSupplier(hvo.getSupplier());
			bvo.setMoney_de(bvo.getLocal_money_de());
			if (objtype == 3) {
				bvo.setRecaccount(pk_bankaccbas);
			}
		}

		IArapBillPubQueryService servie = NCLocator.getInstance().lookup(
				IArapBillPubQueryService.class);
		servie.getDefaultVO(aggvo, true);
		getPfBusiAction().processAction("SAVE", "F3", null, aggvo, null, null);
		// approveSilently("F3", aggvo.getPrimaryKey(), "Y", "��׼", "", false);
	}

}
