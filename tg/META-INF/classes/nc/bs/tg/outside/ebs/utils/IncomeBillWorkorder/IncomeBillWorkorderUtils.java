package nc.bs.tg.outside.ebs.utils.IncomeBillWorkorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.core.service.TimeService;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.EBSCont;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.tg.invoicebill.InvoiceBillBVO;
import nc.vo.tg.invoicebill.InvoiceBillVO;

import nc.vo.tg.outside.incomebill.IncomeBillBodyVO;
import nc.vo.tg.outside.incomebill.IncomeBillHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class IncomeBillWorkorderUtils extends EBSBillUtils{
	Map<String, String> tradetypeMap = null;
	static IncomeBillWorkorderUtils utils;
	
	
	public static IncomeBillWorkorderUtils getUtils() {
		if (utils == null) {
			utils = new IncomeBillWorkorderUtils();
		}
		return utils;
	}
	

	/**
	 * ���Ʊ��
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */

	public String onSyncBill(HashMap<String, Object> value, String srctype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		// ��ϵͳ��Ϣ
		JSONObject jsonData = (JSONObject) value.get("data");// ������
		JSONObject jsonhead = (JSONObject) jsonData.get("headInfo");// ��ϵͳ��Դ��ͷ����
		String srcid = jsonhead.getString("ebsid");// EBSҵ�񵥾�ID
		String srcno = jsonhead.getString("ebsbillno");// EBSҵ�񵥾ݵ��ݺ�
		String billqueue = EBSCont.getSrcBillNameMap().get(srctype) + ":"
				+ srcid;
		String billkey = EBSCont.getSrcBillNameMap().get(srctype) + ":" + srcno;
		AggInvoiceBillVO aggVO = (AggInvoiceBillVO) DataChangeUtils.getUtils().getInvoiceBillVO(
				AggInvoiceBillVO.class, "isnull(dr,0)=0 and def1 = '" + srcid
						+ "'");
		if (aggVO != null) {
			throw new BusinessException("��"
					+ billkey
					+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
					+ aggVO.getParentVO().getAttributeValue(
							InvoiceBillVO.BILLNO) + "��,�����ظ��ϴ�!");
		}
		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			AggInvoiceBillVO billvo = onTranBill(jsonData, srctype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "FN05", null,
					billvo, null, eParam);
			AggInvoiceBillVO[] billvos = (AggInvoiceBillVO[]) obj;
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(InvoiceBillVO.BILLNO));
			return JSON.toJSONString(dataMap);

		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param hMap
	 * @return
	 */

	/**
	 * ��Ϣת��
	 * 
	 * @param srcdata
	 * @param srctype
	 * @return
	 */
	public AggInvoiceBillVO onTranBill(JSONObject srcdata, String srctype)
			throws BusinessException {
		JSON jsonhead = (JSON) srcdata.get("headInfo");// ��ϵͳ��Դ��ͷ����
		String jsonbody = srcdata.getString("bodyInfos");// ��ϵͳ��Դ��������
		if (srcdata == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + srcdata);
		}
		IncomeBillHeadVO headvo = JSONObject.toJavaObject(jsonhead,
				IncomeBillHeadVO.class);// �ⲿϵͳ��Ϣ
		List<IncomeBillBodyVO> bodylist = JSON.parseArray(jsonbody,
				IncomeBillBodyVO.class);// �ⲿϵͳ��Ϣ

		AggInvoiceBillVO billVO = onDefaultValue(headvo, bodylist, srctype);
		// ������Ϣת��

		UFDouble money = UFDouble.ZERO_DBL;// ���
		UFDouble local_money = UFDouble.ZERO_DBL;// ��֯���
		UFDouble group_money = UFDouble.ZERO_DBL;// ���Ž��
		UFDouble global_money = UFDouble.ZERO_DBL;// ȫ�ֽ��
		// ��ϸ��Ϣת��
		List<InvoiceBillBVO> blists = new ArrayList<InvoiceBillBVO>();
		if (bodylist != null && bodylist.size() > 0) {
			for (int row = 0; row < bodylist.size(); row++) {

				try {
					IncomeBillBodyVO bodyvo = bodylist.get(row);
					InvoiceBillBVO itmevo = (InvoiceBillBVO) billVO
							.getChildrenVO()[row];

					DataChangeUtils.getUtils().setItemVO((InvoiceBillVO) billVO.getParentVO(), itmevo,
							bodyvo);

//					calculate(billVO, IBillFieldGet.MONEY_CR, row);
					// itmevo.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR,
					// bodyvo.getLocal_tax_cr());// ˰��
//					calculate(billVO, IBillFieldGet.LOCAL_TAX_CR, row);
	/*				money = money.add(itmevo.getMoney_cr());// ���
					local_money = local_money.add(itmevo.getLocal_money_cr());// ��֯���
					group_money = group_money.add(itmevo.getGroupcrebit());// ���Ž��
					global_money = global_money.add(itmevo.getGlobalcrebit());// ȫ�ֽ��
					blists.add(itmevo);*/
				} catch (Exception e) {
					throw new BusinessException("��[" + (row + 1) + "],"
							+ e.getMessage(), e);
				}
			}
		}
//
//		billVO.getParentVO().setAttributeValue(PayableBillVO.MONEY, money);
//		billVO.getParentVO().setAttributeValue(PayableBillVO.LOCAL_MONEY,
//				local_money);
//		billVO.getParentVO().setAttributeValue(PayableBillVO.GROUPLOCAL,
//				group_money);
//		billVO.getParentVO().setAttributeValue(PayableBillVO.GLOBALLOCAL,
//				global_money);

		return billVO;
	}
	
	protected AggInvoiceBillVO onDefaultValue(IncomeBillHeadVO headvo,
			List<IncomeBillBodyVO> bodylist, String srctype)
			throws BusinessException {
		AggInvoiceBillVO aggvo = new AggInvoiceBillVO();
		InvoiceBillVO hvo = new InvoiceBillVO();
		String billdate = headvo.getBilldate();
		if (billdate == null || "".equals(billdate)) {
			throw new BusinessException("�������ڲ���Ϊ��!");
		}

		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// ��ǰʱ��

		hvo.setAttributeValue(InvoiceBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// ����
		hvo.setAttributeValue(InvoiceBillVO.PK_ORG,
				getPk_orgByCode(headvo.getOrg()));// Ӧ��������֯->NCҵ��Ԫ����
		hvo.setAttributeValue(InvoiceBillVO.CREATOR, getSaleUserID());// �Ƶ���
		//hvo.setAttributeValue(PayableBillVO.CREATOR, hvo.getBillmaker());// ������
		hvo.setAttributeValue(InvoiceBillVO.CREATIONTIME, currTime);// ����ʱ��
		/*			hvo.setAttributeValue(InvoiceBillVO.TRANSTYPE, IBillFieldGet.F1);// �������ͱ���
		//hvo.setAttributeValue(InvoiceBillVO.BILLCLASS, IBillFieldGet.YF);// ���ݴ���
		hvo.setAttributeValue(InvoiceBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ��������ϵͳ
		hvo.setAttributeValue(InvoiceBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ������Դϵͳ
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, IBillFieldGet.D1);// Ӧ������code
		hvo.setAttributeValue(PayableBillVO.BILLSTATUS,
				ARAPBillStatus.SAVE.VALUE);// ����״̬
		// ��������
	BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetypeMap().get(
				srctype));
		if (billTypeVo == null) {
			throw new BusinessException("��"
					+ EBSCont.getSrcBillNameMap().get(srctype)
					+ "����صĽ�������δ����,����ϵϵͳ����Ա!");
		}

		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPEID,
				billTypeVo.getPk_billtypeid());// Ӧ������
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, getTradetypeMap()
				.get(srctype));// Ӧ������
		hvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// �ڳ���־
		hvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.FALSE);// �Ƿ����
		hvo.setAttributeValue(PayableBillVO.BILLDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(PayableBillVO.BUSIDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// ���ݻ�����
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// ���ݻ���ڼ�
*/
		//DataChangeUtils.utils.setHeaderVO(hvo, headvo);
		DataChangeUtils.utils.setHeaderVO(hvo, headvo);

		aggvo.setParentVO(hvo);
		InvoiceBillBVO[] itemVOs = new InvoiceBillBVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new InvoiceBillBVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// �к�
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
	/*	IArapBillPubQueryService servie = NCLocator.getInstance().lookup(
				.class);
		servie.getDefaultVO(aggvo, true);*/

		return aggvo;
	}
	
	



	

}
