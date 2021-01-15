package nc.bs.tg.outside.ebs.utils.recebill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.arap.bill.ArapBillCalUtil;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.ArapDataDataSet;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.fi.pub.SysInit;
import nc.itf.tg.outside.EBSCont;
import nc.pubitf.bbd.CurrtypeQuery;
import nc.pubitf.erm.accruedexpense.IErmAccruedBillManage;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.arap.sysinit.SysinitConst;
import nc.vo.arappub.calculator.data.RelationItemForCal_Debit;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.Calculator;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.tg.recebill.ReceivableBodyVO;
import nc.vo.tg.recebill.ReceivableHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ReceBillUtils extends EBSBillUtils {
	static ReceBillUtils utils;
	private IErmAccruedBillManage billManagerService;

	public static ReceBillUtils getUtils() {
		if (utils == null) {
			utils = new ReceBillUtils();
		}
		return utils;
	}

	/**
	 * Ӧ�յ�
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		// �������Ϣ
		JSONObject jsonData = (JSONObject) value.get("data");// ������
		String jsonhead = jsonData.getString("receivableHeadVO");// ��ϵͳ��Դ��ͷ����
		String jsonbody = jsonData.getString("receivableBodyVOs");// ��ϵͳ��Դ��������
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + jsonData);
		}
		// ת��json
		ReceivableHeadVO headVO = JSONObject.parseObject(jsonhead,
				ReceivableHeadVO.class);
		List<ReceivableBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				ReceivableBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("������ת��ʧ�ܣ����飡json:" + jsonData);
		}

		String srcid = headVO.getSrcsid();// ��ϵͳҵ�񵥾�ID
		String srcno = headVO.getSrcbillcode();// ��ϵͳҵ�񵥾ݵ��ݺ�
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcid;
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		AggReceivableBillVO[] aggvo=null;
		// // TODO ebsid ��ʵ�ʴ�����Ϣλ�ý��б��
		AggReceivableBillVO aggVO = (AggReceivableBillVO) getBillVO(
				AggReceivableBillVO.class, "isnull(dr,0)=0 and def1 = '"
						+ srcid + "'");
		if (aggVO != null) {
			throw new BusinessException("��"
					+ billkey
					+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
					+ aggVO.getParentVO().getAttributeValue(
							ReceivableBillVO.BILLNO) + "��,�����ظ��ϴ�!");
		}
		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			AggReceivableBillVO billvo = onTranBill(headVO, bodyVOs, srctype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			aggvo=(AggReceivableBillVO[]) getPfBusiAction().processAction("SAVE", "F0", null, billvo, null,
					eParam);
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("billid", aggvo[0].getPrimaryKey());
		dataMap.put("billno", (String) aggvo[0].getParentVO()
				.getAttributeValue(ReceivableBillVO.BILLNO));
		return JSON.toJSONString(dataMap);

	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	private AggReceivableBillVO onTranBill(ReceivableHeadVO headvo,
			List<ReceivableBodyVO> bodyVOs, String srctype) throws Exception {
		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO save_headVO = new ReceivableBillVO();
		save_headVO.setAttributeValue("pk_org",
				getPk_orgByCode(headvo.getOrg()));// Ӧ�ղ�����֯
		save_headVO.setAttributeValue("pk_fiorg", null);// ����������֯
		save_headVO.setAttributeValue("pk_pcorg", null);// ��������
		save_headVO.setAttributeValue("sett_org", null);// ���������֯
		save_headVO.setAttributeValue("pk_org_v", null);// Ӧ�ղ�����֯�汾
		save_headVO.setAttributeValue("pk_fiorg_v", null);// ����������֯�汾
		save_headVO.setAttributeValue("pk_pcorg_v", null);// �������İ汾
		save_headVO.setAttributeValue("sett_org_v", null);// ���������֯�汾
		save_headVO.setAttributeValue("isreded", UFBoolean.FALSE);// �Ƿ����
		save_headVO.setAttributeValue("outbusitype", null);// ��ϵͳҵ������
		save_headVO.setAttributeValue("officialprintuser", null);// ��ʽ��ӡ��
		save_headVO.setAttributeValue("officialprintdate", null);// ��ʽ��ӡ����
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ��������
		save_headVO.setAttributeValue("modifiedtime", null);// ����޸�ʱ��
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
		save_headVO.setAttributeValue("creator",
				getUserPkByCode(headvo.getCreator()));// ������
		save_headVO.setAttributeValue("pk_billtype", "F0");// �������ͱ���
		save_headVO.setAttributeValue("custdelegate", null);// ���浥λ
		save_headVO.setAttributeValue("pk_corp", null);// ��λ����
		save_headVO.setAttributeValue("modifier", null);// ����޸���
		save_headVO.setAttributeValue("pk_tradetype", "D0");// Ӧ������code
		save_headVO.setAttributeValue("pk_tradetypeid", "0001ZZ10000000258KGQ");// Ӧ������
		save_headVO.setAttributeValue("billclass", "ys");// ���ݴ���
		save_headVO.setAttributeValue("pk_recbill", null);// Ӧ�յ���ʶ
		save_headVO.setAttributeValue("accessorynum", null);// ��������
		save_headVO.setAttributeValue("subjcode", null);// ��Ŀ����
		save_headVO.setAttributeValue("isflowbill", UFBoolean.FALSE);// �Ƿ����̵���
		save_headVO.setAttributeValue("confirmuser", null);// ����ȷ����
		save_headVO.setAttributeValue("isinit", UFBoolean.FALSE);// �ڳ���־
		save_headVO.setAttributeValue("billno", null);// ���ݺ�
		save_headVO.setAttributeValue("billdate", new UFDate());// ��������
		save_headVO.setAttributeValue("syscode", 0);// ��������ϵͳ
		save_headVO.setAttributeValue("src_syscode", 0);// ������Դϵͳ
		save_headVO.setAttributeValue("billstatus", -1);// ����״̬
		save_headVO.setAttributeValue("billmaker",
				getUserPkByCode(headvo.getCreator()));// �Ƶ���
		save_headVO.setAttributeValue("approver", null);// �����
		save_headVO.setAttributeValue("approvedate", null);// ���ʱ��
		save_headVO.setAttributeValue("lastadjustuser", null);// ���յ�����
		save_headVO.setAttributeValue("signuser", null);// ǩ����
		save_headVO.setAttributeValue("signyear", null);// ǩ�����
		save_headVO.setAttributeValue("signperiod", null);// ǩ���ڼ�
		save_headVO.setAttributeValue("signdate", null);// ǩ������
		save_headVO.setAttributeValue("pk_busitype", "0001ZZ10000000258BF2");// ҵ������
		save_headVO.setAttributeValue("money", null);// ԭ�ҽ��
		save_headVO.setAttributeValue("local_money", null);// ��֯���ҽ��
		save_headVO.setAttributeValue("billyear", null);// ���ݻ�����
		save_headVO.setAttributeValue("billperiod", null);// ���ݻ���ڼ�
		save_headVO.setAttributeValue("scomment", null);// ժҪ
		save_headVO.setAttributeValue("effectstatus", 0);// ��Ч״̬
		save_headVO.setAttributeValue("effectuser", null);// ��Ч��
		save_headVO.setAttributeValue("effectdate", null);// ��Ч����
		save_headVO.setAttributeValue("lastapproveid", null);// ����������
		save_headVO.setAttributeValue("def1", headvo.getContractcode());// �Զ�����1
		save_headVO.setAttributeValue("def30", null);// �Զ�����30
		save_headVO.setAttributeValue("def29", null);// �Զ�����29
		save_headVO.setAttributeValue("def28", null);// �Զ�����28
		save_headVO.setAttributeValue("def27", null);// �Զ�����27
		save_headVO.setAttributeValue("def26", null);// �Զ�����26
		save_headVO.setAttributeValue("def25", null);// �Զ�����25
		save_headVO.setAttributeValue("def24", null);// �Զ�����24
		save_headVO.setAttributeValue("def23", null);// �Զ�����23
		save_headVO.setAttributeValue("def22", null);// �Զ�����22
		save_headVO.setAttributeValue("def21", null);// �Զ�����21
		save_headVO.setAttributeValue("def20", null);// �Զ�����20
		save_headVO.setAttributeValue("def19", null);// �Զ�����19
		save_headVO.setAttributeValue("def18", null);// �Զ�����18
		save_headVO.setAttributeValue("def17", null);// �Զ�����17
		save_headVO.setAttributeValue("def16", null);// �Զ�����16
		save_headVO.setAttributeValue("def15", null);// �Զ�����15
		save_headVO.setAttributeValue("def14", null);// �Զ�����14
		save_headVO.setAttributeValue("def13", null);// �Զ�����13
		save_headVO.setAttributeValue("def12", null);// �Զ�����12
		save_headVO.setAttributeValue("def11", null);// �Զ�����11
		save_headVO.setAttributeValue("def10", null);// �Զ�����10
		save_headVO.setAttributeValue("def9", null);// �Զ�����9
		save_headVO.setAttributeValue("def8", null);// �Զ�����8
		save_headVO.setAttributeValue("def7", null);// �Զ�����7
		save_headVO.setAttributeValue("def6", null);// �Զ�����6
		save_headVO.setAttributeValue("def5", null);// �Զ�����5
		save_headVO.setAttributeValue("def4", null);// �Զ�����4
		save_headVO.setAttributeValue("def3", null);// �Զ�����3
		save_headVO.setAttributeValue("def2", headvo.getTotalamount());// Ӧ�տ���
		save_headVO.setAttributeValue("grouplocal", null);// ���ű��ҽ��
		save_headVO.setAttributeValue("globallocal", null);// ȫ�ֱ��ҽ��
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		save_headVO.setAttributeValue("coordflag", null);// ����Эͬ��־
		save_headVO.setAttributeValue("inner_effect_date", null);// �ڿ���������
		save_headVO.setAttributeValue("approvestatus", 3);// ����״̬
		save_headVO.setAttributeValue("sendcountryid", null);// ������
		save_headVO.setAttributeValue("taxcountryid", null);// ��˰��
		save_headVO.setStatus(VOStatus.NEW);

		List<ReceivableBillItemVO> bodylist = new ArrayList<>();
		for (ReceivableBodyVO receivableBodyVO : bodyVOs) {
			ReceivableBillItemVO save_bodyVO = new ReceivableBillItemVO();
			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// Ӧ�ղ�����֯
			save_bodyVO.setAttributeValue("pk_pcorg", null);// ��������
			save_bodyVO.setAttributeValue("pk_fiorg", null);// ����������֯
			save_bodyVO.setAttributeValue("sett_org", null);// ���������֯
			save_bodyVO.setAttributeValue("pk_pcorg_v", null);// �������İ汾
			save_bodyVO.setAttributeValue("pk_org_v", null);// Ӧ�ղ�����֯�汾
			save_bodyVO.setAttributeValue("pk_fiorg_v", null);// ����������֯�汾
			save_bodyVO.setAttributeValue("sett_org_v", null);// ���������֯�汾
			save_bodyVO.setAttributeValue("so_org_v", null);// ҵ����֯�汾
			save_bodyVO.setAttributeValue("so_deptid", null);// ҵ����
			save_bodyVO.setAttributeValue("so_deptid_v", null);// ҵ���Ű汾
			save_bodyVO.setAttributeValue("so_psndoc", null);// ҵ����Ա
			save_bodyVO.setAttributeValue("so_ordertype", null);// ���۶�������
			save_bodyVO.setAttributeValue("so_transtype", null);// ������������
			save_bodyVO.setAttributeValue("so_org", null);// ҵ����֯
			save_bodyVO.setAttributeValue("material", null);// ����
			save_bodyVO.setAttributeValue("customer",
					getcustomer(receivableBodyVO.getCustomer()));// �ͻ�
			save_bodyVO.setAttributeValue("postunit", null);// ���ۼ�����λ
			save_bodyVO.setAttributeValue("postpricenotax", null);// ���۵�λ��˰����
			save_bodyVO.setAttributeValue("postquantity", null);// ���۵�λ����
			save_bodyVO.setAttributeValue("postprice", null);// ���۵�λ��˰����
			save_bodyVO.setAttributeValue("coordflag", null);// ����Эͬ״̬
			save_bodyVO.setAttributeValue("equipmentcode", null);// �豸����
			save_bodyVO.setAttributeValue("productline", null);// ��Ʒ��
			save_bodyVO.setAttributeValue("cashitem", null);// �ֽ�������Ŀ
			save_bodyVO.setAttributeValue("bankrollprojet", null);// �ʽ�ƻ���Ŀ
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// �����־
			save_bodyVO
					.setAttributeValue("billdate", save_headVO.getBilldate());// ��������
			save_bodyVO
					.setAttributeValue("pk_group", save_headVO.getPk_group());// ��������
			save_bodyVO.setAttributeValue("pk_billtype",
					save_headVO.getPk_billtype());// �������ͱ���
			save_bodyVO.setAttributeValue("billclass",
					save_headVO.getBillclass());// ���ݴ���
			save_bodyVO.setAttributeValue("pk_tradetype",
					save_headVO.getPk_tradetype());// Ӧ������code
			save_bodyVO.setAttributeValue("pk_tradetypeid",
					save_headVO.getPk_tradetypeid());// Ӧ������
			save_bodyVO.setAttributeValue("pk_recitem", null);// Ӧ�յ��б�ʶ
			save_bodyVO
					.setAttributeValue("busidate", save_headVO.getBilldate());// ��������
			save_bodyVO.setAttributeValue("pk_subjcode", null);// ��֧��Ŀ
			save_bodyVO.setAttributeValue("billno", null);// ���ݱ��
			save_bodyVO.setAttributeValue("objtype", 0);// ��������
			save_bodyVO.setAttributeValue("rowno", null);// ���ݷ�¼��
			save_bodyVO.setAttributeValue("rowtype", null);// ������
			save_bodyVO.setAttributeValue("direction", 1);// ����
			save_bodyVO.setAttributeValue("pk_ssitem", null);// ����������
			save_bodyVO.setAttributeValue("scomment", null);// ժҪ
			save_bodyVO.setAttributeValue("subjcode", null);// ��Ŀ����
			save_bodyVO.setAttributeValue("pk_currtype",
					save_headVO.getPk_currtype());// ����
			save_bodyVO.setAttributeValue("rate", 1);// ��֯���һ���
			save_bodyVO.setAttributeValue("pk_deptid", null);// ����
			save_bodyVO.setAttributeValue("pk_deptid_v", null);// �� ��
			save_bodyVO.setAttributeValue("pk_psndoc", null);// ҵ��Ա
			save_bodyVO.setAttributeValue("money_de", new UFDouble(
					receivableBodyVO.getMoney_de()));// �跽ԭ�ҽ��
			save_bodyVO.setAttributeValue("local_money_de", null);// ��֯���ҽ��
			save_bodyVO.setAttributeValue("quantity_de", new UFDouble(
					receivableBodyVO.getQuantity_de()));// �跽����
			save_bodyVO.setAttributeValue("money_bal", null);// ԭ�����
			save_bodyVO.setAttributeValue("local_money_bal", null);// ��֯�������
			save_bodyVO.setAttributeValue("quantity_bal", null);// �������
			save_bodyVO.setAttributeValue("local_tax_de",
					receivableBodyVO.getLocal_tax_de());// ˰��
			save_bodyVO.setAttributeValue("notax_de",
					receivableBodyVO.getNotax_de());// �跽ԭ����˰���
			save_bodyVO.setAttributeValue("local_notax_de", null);// ��֯������˰���
			save_bodyVO.setAttributeValue("price", null);// ����
			save_bodyVO.setAttributeValue("taxprice",
					receivableBodyVO.getTaxprice());// ��˰����
			save_bodyVO.setAttributeValue("taxrate", new UFDouble(
					receivableBodyVO.getTaxrate()));// ˰��
			save_bodyVO.setAttributeValue("taxnum", null);// ˰��
			save_bodyVO.setAttributeValue("top_billid", null);// �ϲ㵥������
			save_bodyVO.setAttributeValue("top_itemid", null);// �ϲ㵥��������
			save_bodyVO.setAttributeValue("top_billtype", null);// �ϲ㵥������
			save_bodyVO.setAttributeValue("top_tradetype", null);// �ϲ㽻������
			save_bodyVO.setAttributeValue("src_tradetype", null);// Դͷ��������
			save_bodyVO.setAttributeValue("src_billtype", null);// Դͷ��������
			save_bodyVO.setAttributeValue("src_billid", null);// Դͷ��������
			save_bodyVO.setAttributeValue("src_itemid", null);// Դͷ����������
			save_bodyVO.setAttributeValue("taxtype", 1);// ��˰���
			save_bodyVO.setAttributeValue("pk_payterm", null);// �տ�Э��
			save_bodyVO.setAttributeValue("payaccount", null);// ���������˻�
			save_bodyVO.setAttributeValue("recaccount", null);// �տ������˻�
			save_bodyVO.setAttributeValue("ordercubasdoc", null);// �����ͻ�
			save_bodyVO.setAttributeValue("innerorderno", null);// ����������
			save_bodyVO.setAttributeValue("assetpactno", null);// �ʲ���ͬ��
			save_bodyVO.setAttributeValue("contractno", null);// ��ͬ��
			save_bodyVO.setAttributeValue("freecust", null);// ɢ��
			save_bodyVO.setAttributeValue("purchaseorder", null);// ������
			save_bodyVO.setAttributeValue("invoiceno", null);// ��Ʊ��
			save_bodyVO.setAttributeValue("outstoreno", null);// ���ⵥ��
			save_bodyVO.setAttributeValue("checkelement", null);// ���κ���Ҫ��
			save_bodyVO.setAttributeValue("def30", null);// �Զ�����30
			save_bodyVO.setAttributeValue("def29", null);// �Զ�����29
			save_bodyVO.setAttributeValue("def28", null);// �Զ�����28
			save_bodyVO.setAttributeValue("def27", null);// �Զ�����27
			save_bodyVO.setAttributeValue("def26", null);// �Զ�����26
			save_bodyVO.setAttributeValue("def25", null);// �Զ�����25
			save_bodyVO.setAttributeValue("def24", null);// �Զ�����24
			save_bodyVO.setAttributeValue("def23", null);// �Զ�����23
			save_bodyVO.setAttributeValue("def22", null);// �Զ�����22
			save_bodyVO.setAttributeValue("def21", null);// �Զ�����21
			save_bodyVO.setAttributeValue("def20", null);// �Զ�����20
			save_bodyVO.setAttributeValue("def19", null);// �Զ�����19
			save_bodyVO.setAttributeValue("def18", null);// �Զ�����18
			save_bodyVO.setAttributeValue("def17", null);// �Զ�����17
			save_bodyVO.setAttributeValue("def16", null);// �Զ�����16
			save_bodyVO.setAttributeValue("def15", null);// �Զ�����15
			save_bodyVO.setAttributeValue("def14", null);// �Զ�����14
			save_bodyVO.setAttributeValue("def13", null);// �Զ�����13
			save_bodyVO.setAttributeValue("def12", null);// �Զ�����12
			save_bodyVO.setAttributeValue("def11", null);// �Զ�����11
			save_bodyVO.setAttributeValue("def10", null);// �Զ�����10
			save_bodyVO.setAttributeValue("def9", null);// �Զ�����9
			save_bodyVO.setAttributeValue("def8", null);// �Զ�����8
			save_bodyVO.setAttributeValue("pk_recbill", null);// �ͻ�Ӧ�յ���ʶ
			save_bodyVO.setAttributeValue("def7", null);// �Զ�����7
			save_bodyVO.setAttributeValue("def6", null);// �Զ�����6
			save_bodyVO.setAttributeValue("def5", null);// �Զ�����5
			save_bodyVO.setAttributeValue("def4", null);// �Զ�����4
			save_bodyVO.setAttributeValue("def3", null);// �Զ�����3
			save_bodyVO.setAttributeValue("def2", null);// �Զ�����2
			save_bodyVO.setAttributeValue("def1",
					receivableBodyVO.getContractcode());// ��Ӧ��ͬ���
			save_bodyVO.setAttributeValue("grouprate", null);// ���ű��һ���
			save_bodyVO.setAttributeValue("globalrate", null);// ȫ�ֱ��һ���
			save_bodyVO.setAttributeValue("groupdebit", null);// ���ű��ҽ��(�跽)
			save_bodyVO.setAttributeValue("globaldebit", null);// ȫ�ֱ��ҽ��(�跽)
			save_bodyVO.setAttributeValue("groupbalance", null);// ���ű������
			save_bodyVO.setAttributeValue("globalbalance", null);// ȫ�ֱ������
			save_bodyVO.setAttributeValue("groupnotax_de", null);// ���ű�����˰���(�跽)
			save_bodyVO.setAttributeValue("globalnotax_de", null);// ȫ�ֱ�����˰���(�跽)
			save_bodyVO.setAttributeValue("occupationmny", null);// Ԥռ��ԭ�����
			save_bodyVO.setAttributeValue("project", null);// ��Ŀ
			save_bodyVO.setAttributeValue("project_task", null);// ��Ŀ����
			save_bodyVO.setAttributeValue("settleno", null);// �����嵥��
			save_bodyVO.setAttributeValue("local_price", null);// ���ҵ���
			save_bodyVO.setAttributeValue("local_taxprice", null);// ���Һ�˰����
			save_bodyVO.setAttributeValue("confernum", null);// �ڲ����׽����
			save_bodyVO.setAttributeValue("costcenter", null);// �ɱ�����
			save_bodyVO.setAttributeValue("rececountryid", null);// �ջ���
			save_bodyVO.setAttributeValue("buysellflag", null);// ��������
			save_bodyVO.setAttributeValue("triatradeflag", null);// ����ó��
			save_bodyVO.setAttributeValue("vatcode", null);// VATע����
			save_bodyVO.setAttributeValue("custvatcode", null);// �ͻ�VATע����
			save_bodyVO.setAttributeValue("taxcodeid", null);// ˰��
			save_bodyVO.setAttributeValue("caltaxmny", null);// ��˰���
			save_bodyVO.setAttributeValue("pk_recbill", null);// �ͻ�Ӧ�յ���ʶ
			save_bodyVO.setAttributeValue("material_src", null);// ԭʼ����
			save_bodyVO.setAttributeValue("matcustcode", null);// �ͻ�������
			save_bodyVO.setAttributeValue("settlemoney", null);// �տ���
			save_bodyVO.setAttributeValue("settlecurr", null);// �տ����
			save_bodyVO.setAttributeValue("batchcode", null);// ���κ�
			save_bodyVO.setAttributeValue("pk_batchcode", null);// ���κű���
			save_bodyVO.setAttributeValue("pk_balatype", null);// ���㷽ʽ
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new ReceivableBillItemVO[0]));
		for (int i = 0; i < aggvo.getChildrenVO().length; i++) {
			calculate(aggvo, IBillFieldGet.MONEY_DE, i);
		}
		return aggvo;
	}

	/**
	 * �����Ϣ����
	 * 
	 * @throws BusinessException
	 */
	private void calculate(AggReceivableBillVO billvo, String key, int row)
			throws BusinessException {

		if (billvo.getChildrenVO() != null && billvo.getChildrenVO().length > 0) {
			ReceivableBillItemVO itemVO = (ReceivableBillItemVO) billvo
					.getChildrenVO()[row];
			String currType = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_CURRTYPE);
			String pk_org = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_ORG);
			String pk_group = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_GROUP);
			String pk_currtype = CurrencyRateUtilHelper.getInstance()
					.getLocalCurrtypeByOrgID(pk_org);

			ScaleUtils scale = new ScaleUtils(pk_group);
			IDataSetForCal data = new ArapDataDataSet(billvo, row,
					new RelationItemForCal_Debit());
			Calculator tool = new Calculator(data, scale);
			Condition cond = new Condition();

			if (pk_currtype == null) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("2006pub_0", "02006pub-0552"));
			}
			CurrtypeVO currTypeVo = CurrtypeQuery.getInstance().getCurrtypeVO(
					pk_currtype);
			String destCurrType = currTypeVo.getPk_currtype();
			cond.setCalOrigCurr(true);
			if ((IBillFieldGet.LOCAL_MONEY_CR.equals(key) || IBillFieldGet.LOCAL_MONEY_DE
					.equals(key)) && !destCurrType.equals(currType)) {// ��֯��ǰ����Ϊ��֯����
				cond.setCalOrigCurr(false);
			}
			cond.setIsCalLocalCurr(true);
			cond.setIsChgPriceOrDiscount(false);
			cond.setIsFixNchangerate(false);
			cond.setIsFixNqtunitrate(false);
			Object buysellflag = itemVO
					.getAttributeValue(IBillFieldGet.BUYSELLFLAG);
			boolean isInternational = BillEnumCollection.BuySellType.OUT_BUY.VALUE
					.equals(buysellflag)
					|| BillEnumCollection.BuySellType.OUT_SELL.VALUE
							.equals(buysellflag);
			cond.setInternational(isInternational);

			String AP21 = SysInit.getParaString(pk_org, SysinitConst.AP21);
			cond.setIsTaxOrNet(SysinitConst.ARAP21_TAX.equals(AP21));

			cond.setGroupLocalCurrencyEnable(ArapBillCalUtil
					.isUseGroupMoney(pk_group));
			cond.setGlobalLocalCurrencyEnable(ArapBillCalUtil
					.isUseGlobalMoney());
			cond.setOrigCurToGlobalMoney(ArapBillCalUtil
					.isOrigCurToGlobalMoney());
			cond.setOrigCurToGroupMoney(ArapBillCalUtil
					.isOrigCurToGroupMoney(pk_group));
			calulateTax(itemVO);
			calulateBalance(itemVO);
			tool.calculate(cond, key);
		}

	}

	/**
	 * ���������Ϣ
	 * 
	 * @param itemVO
	 */
	private void calulateBalance(ReceivableBillItemVO itemVO) {
		boolean direction = (Integer) itemVO
				.getAttributeValue(IBillFieldGet.DIRECTION) == Direction.CREDIT.VALUE
				.intValue();
		String local_money = direction ? "local_money_cr" : "local_money_de";
		String money = direction ? "money_cr" : "money_de";
		String quantity = direction ? "quantity_cr" : "quantity_de";
		String group_money = direction ? "groupcrebit" : "groupdebit";
		String global_money = direction ? "globalcrebit" : "globaldebit";
		String money_bal = "money_bal";
		String local_notax = direction ? BaseItemVO.LOCAL_NOTAX_CR
				: BaseItemVO.LOCAL_NOTAX_DE;
		String group_notax = direction ? IBillFieldGet.GROUPNOTAX_CRE
				: IBillFieldGet.GROUPNOTAX_DE;
		String global_notax = direction ? IBillFieldGet.GLOBALNOTAX_CRE
				: IBillFieldGet.GLOBALNOTAX_DE;
		String group_tax = direction ? IBillFieldGet.GROUPTAX_CRE
				: IBillFieldGet.GROUPTAX_DE;
		String global_tax = direction ? IBillFieldGet.GLOBALTAX_CRE
				: IBillFieldGet.GLOBALTAX_DE;

		itemVO.setAttributeValue(money_bal,
				itemVO.getAttributeValue(money) == null ? UFDouble.ZERO_DBL
						: itemVO.getAttributeValue(money));
		itemVO.setAttributeValue(IBillFieldGet.LOCAL_MONEY_BAL, itemVO
				.getAttributeValue(local_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(local_money));

		itemVO.setAttributeValue(IBillFieldGet.GROUPBALANCE, itemVO
				.getAttributeValue(group_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(group_money));

		itemVO.setAttributeValue(IBillFieldGet.GLOBALBALANCE, itemVO
				.getAttributeValue(global_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(global_money));
		itemVO.setAttributeValue(IBillFieldGet.QUANTITY_BAL, itemVO
				.getAttributeValue(quantity) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(quantity));
		itemVO.setAttributeValue(IBillFieldGet.OCCUPATIONMNY, itemVO
				.getAttributeValue(money_bal) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(money_bal));

		String caltaxmny_key = BillEnumCollection.TaxType.TAXOUT.VALUE
				.equals(itemVO.getAttributeValue(BaseItemVO.TAXTYPE)) ? local_notax
				: local_money;
		itemVO.setAttributeValue(IBillFieldGet.CALTAXMNY, itemVO
				.getAttributeValue(caltaxmny_key) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(caltaxmny_key));

		itemVO.setAttributeValue(
				group_tax,
				MathTool.sub(
						itemVO.getAttributeValue(group_money) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(group_money),
						(UFDouble) itemVO.getAttributeValue(group_notax) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(group_notax)));

		itemVO.setAttributeValue(
				global_tax,
				MathTool.sub(
						itemVO.getAttributeValue(global_money) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(global_money),
						(UFDouble) itemVO.getAttributeValue(global_notax) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(global_notax)));
	}

	/***
	 * ������˰�����Ϣ
	 * 
	 * @param itemVO
	 */
	private void calulateTax(ReceivableBillItemVO itemVO) {
		Object buysellflag = itemVO.getBuysellflag();
		boolean isInternational = BillEnumCollection.BuySellType.OUT_BUY.VALUE
				.equals(buysellflag)
				|| BillEnumCollection.BuySellType.OUT_SELL.VALUE
						.equals((buysellflag));
		UFDouble grouptaxmny = itemVO.getGroupcrebit();
		UFDouble groupnotaxmny = itemVO.getGroupnotax_cre();
		itemVO.setGrouptax_cre(MathTool.sub(grouptaxmny, groupnotaxmny));

		UFDouble globaltaxmny = itemVO.getGlobalcrebit();
		UFDouble globalnotaxmny = itemVO.getGlobalnotax_cre();
		itemVO.setGlobaltax_cre(MathTool.sub(globaltaxmny, globalnotaxmny));
		if (isInternational) {
			itemVO.setGlobalnotax_cre(itemVO.getGlobalcrebit());
			itemVO.setGroupnotax_cre(itemVO.getGroupcrebit());
			itemVO.setLocal_notax_cr(itemVO.getLocal_money_cr());
			itemVO.setNotax_cr(itemVO.getMoney_cr());
			itemVO.setCaltaxmny(itemVO.getLocal_notax_cr());
		}
	}

}
