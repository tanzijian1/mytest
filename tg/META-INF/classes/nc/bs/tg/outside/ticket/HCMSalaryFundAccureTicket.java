package nc.bs.tg.outside.ticket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.service.TimeService;
import nc.bs.tg.outside.sale.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.tg.salaryfundaccure.SalaryFundAccureItem;
import nc.vo.tg.salaryfundaccure.SalaryFundAccureVO;
import vo.tg.outside.SalaryFundAccureTicketBodyVO;
import vo.tg.outside.SalaryFundAccureTicketHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HCMSalaryFundAccureTicket extends PayBillUtils implements
		ITGSyncService {
	public static final String HCMSalaryOperatorName = "RLZY";// HCMĬ�ϲ���Ա
	private IArapBillPubQueryService arapBillPubQueryService = null;
	static HCMSalaryFundAccureTicket utils;

	public static HCMSalaryFundAccureTicket getUtils() {
		if (utils == null) {
			utils = new HCMSalaryFundAccureTicket();
		}
		return utils;
	}

	/**
	 * ���ʹ���
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(HCMSalaryOperatorName));
		InvocationInfoProxy.getInstance().setUserCode(HCMSalaryOperatorName);

		// �������Ϣ
		JSONObject jsonData = (JSONObject) info.get("data");// ������
		String jsonhead = jsonData.getString("headInfo");// ��ϵͳ��Դ��ͷ����
		String jsonbody = jsonData.getString("bodyInfo");// ��ϵͳ��Դ��������
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + jsonData);
		}

		// ת��json
		SalaryFundAccureTicketHeadVO headVO = JSONObject.parseObject(jsonhead,
				SalaryFundAccureTicketHeadVO.class);
		List<SalaryFundAccureTicketBodyVO> bodyVOs = JSONObject.parseArray(
				jsonbody, SalaryFundAccureTicketBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("������ת��ʧ�ܣ����飡json:" + jsonData);
		}

		String srcid = headVO.getSrcid();// ��ϵͳҵ�񵥾�ID
		String srcno = headVO.getSrcbillno();// ��ϵͳҵ�񵥾ݵ��ݺ�
		Map<String, String> resultInfo = new HashMap<String, String>();

		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggSalaryfundaccure aggVO = (AggSalaryfundaccure) getBillVO(
					AggSalaryfundaccure.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue("billno")
						+ "��,�����ظ��ϴ�!");
			}
			// AggPayBillVO billvo = (AggPayBillVO) convertor.castToBill(value,
			// AggPayBillVO.class, aggVO);

			AggSalaryfundaccure billvo = onTranBill(headVO, bodyVOs);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = (AggSalaryfundaccure[]) getPfBusiAction()
					.processAction("SAVEBASE", "HCM1", null, billvo, null,
							eParam);
			
			AggSalaryfundaccure agg = (AggSalaryfundaccure) getBillVO(AggSalaryfundaccure.class,"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			
//			AggSalaryfundaccure[] billvos = (AggSalaryfundaccure[]) obj;
			
			resultInfo.put("billid", agg.getPrimaryKey());
			resultInfo.put("billno", (String) agg.getParentVO()
					.getAttributeValue("billno"));
			
			WorkflownoteVO worknoteVO = NCLocator.getInstance()
					.lookup(IWorkflowMachine.class)
					.checkWorkFlow("SAVE", "HCM1", agg, eParam);
			obj = (AggSalaryfundaccure[]) getPfBusiAction().processAction(
					"SAVE", "HCM1", worknoteVO,  agg, null, eParam);


		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected AggSalaryfundaccure onTranBill(
			SalaryFundAccureTicketHeadVO headVO,
			List<SalaryFundAccureTicketBodyVO> bodyVOs)
			throws BusinessException {
		AggSalaryfundaccure aggvo = new AggSalaryfundaccure();
		SalaryFundAccureVO save_headVO = new SalaryFundAccureVO();
		String pk_org = getPk_orgByCode(headVO.getPk_org());
		if (pk_org != null) {
			save_headVO.setAttributeValue("pk_org", pk_org);// ������֯
		} else {
			throw new BusinessException("HCMͬ��NC����ϵͳ���ʸ����" + headVO.getSrcid()
					+ " �Ĳ�����֯��NC����ϵͳ�в����ڣ�������֯��" + headVO.getPk_org());
		}
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// ��ϵͳ����
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setAttributeValue("billdate", headVO.getBilldate());// �ۿ�����
		save_headVO.setAttributeValue("def67", headVO.getDef67()
				.substring(0, 7));// ����������
		save_headVO.setAttributeValue("def18", pk_org);// ��ͬǩ����˾

		List<Map<String, String>> budgetsubNames = getLinkCompany(pk_org);
		for (Map<String, String> names : budgetsubNames) {
			save_headVO.setAttributeValue("def5", names.get("factorvalue4"));// �Ƿ��ʱ���
			save_headVO.setAttributeValue("def4", names.get("factorvalue5"));// ��˾����
		}

		String pk_vid = getvidByorg((String) save_headVO
				.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("pk_fiorg",
				(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("pk_fiorg_v", pk_vid);
		save_headVO.setAttributeValue("sett_org",
				(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("sett_org_v", pk_vid);
		save_headVO.setAttributeValue("creationtime",
				save_headVO.getAttributeValue("billdate"));
		save_headVO.setAttributeValue("objtype", 1);
		save_headVO.setAttributeValue("approvestatus", -1);
		save_headVO.setAttributeValue("transtype", "HCM1-Cxx-001");
		save_headVO.setAttributeValue("pk_transtype", "1001ZZ1000000057SWRA");
		 save_headVO.setAttributeValue("billtype", "HCM1");// �������ͱ���
//		save_headVO.setAttributeValue("billdate", new UFDate().toString());// ��������
		save_headVO.setAttributeValue("busidate", new UFDate().toString());//
		// save_headVO.setAttributeValue("syscode", 1);// ��������ϵͳ��Ĭ��Ϊ1��1=Ӧ��ϵͳ
		// save_headVO.setAttributeValue("src_syscode", 1);// ������Դϵͳ
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		save_headVO.setAttributeValue("billstatus", 2);// ����״̬,Ĭ��Ϊ2������
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// �������ţ�Ĭ��Ϊʱ������
		save_headVO.setAttributeValue("billmaker",
				getUserIDByCode(HCMSalaryOperatorName));// �Ƶ���
		// save_headVO.setAttributeValue("objtype", 3); // ��������
		// // 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�
		save_headVO.setAttributeValue("creator",
				getUserIDByCode(HCMSalaryOperatorName));// ������
		save_headVO.setAttributeValue("isinit", UFBoolean.FALSE);// �ڳ���־
		save_headVO.setAttributeValue("isreded", UFBoolean.FALSE);// �Ƿ����
		save_headVO.setStatus(VOStatus.NEW);

		List<SalaryFundAccureItem> bodylist = new ArrayList<>();
		for (SalaryFundAccureTicketBodyVO salaryFundAccureTicketBodyVO : bodyVOs) {
			SalaryFundAccureItem save_bodyVO = new SalaryFundAccureItem();
			save_bodyVO.setAttributeValue("pk_org",
					save_headVO.getAttributeValue("pk_org"));// Ӧ�ղ�����֯
			if (salaryFundAccureTicketBodyVO.getDef18() != null
					&& !("").equals(salaryFundAccureTicketBodyVO.getDef18())) {
				save_bodyVO
						.setAttributeValue("def18",
								getPk_orgByCode(salaryFundAccureTicketBodyVO
										.getDef18()));// ���ʷ��Ź�˾
			} else {
				throw new BusinessException("��ͬǩ����˾������Ϊ�գ�");
			}
			save_bodyVO.setAttributeValue(
					"def30",
					getDefdocInfo(salaryFundAccureTicketBodyVO.getDef30(),
							"zdy046").get("pk_defdoc"));// ��������
			save_bodyVO.setAttributeValue("def22",
					salaryFundAccureTicketBodyVO.getDef22());// ����
			save_bodyVO.setAttributeValue("def29",
					salaryFundAccureTicketBodyVO.getDef29());// ��������-Ӫ��
			save_bodyVO.setAttributeValue("def27",
					salaryFundAccureTicketBodyVO.getDef27());// ��������-��չ
			save_bodyVO.setAttributeValue("def25",
					salaryFundAccureTicketBodyVO.getDef25());// ��������-����
			save_bodyVO.setAttributeValue("def24",
					salaryFundAccureTicketBodyVO.getDef24());// Ӷ��
			save_bodyVO.setAttributeValue("def23",
					salaryFundAccureTicketBodyVO.getDef23());// ����˰ǰ�ۿ�-�籣��˾����
			save_bodyVO.setAttributeValue("def20",
					salaryFundAccureTicketBodyVO.getDef20());// ����˰ǰ�ۿ�-�籣���˲���
			save_bodyVO.setAttributeValue("def19",
					salaryFundAccureTicketBodyVO.getDef19());// ����˰ǰ�ۿ�-������˾����
			save_bodyVO.setAttributeValue("def17",
					salaryFundAccureTicketBodyVO.getDef17());// ����˰ǰ�ۿ�-��������˲���
			save_bodyVO.setAttributeValue("def6",
					salaryFundAccureTicketBodyVO.getDef6());// Ӧ���ϼ�
			save_bodyVO.setAttributeValue("def31",
					salaryFundAccureTicketBodyVO.getDef31());// Ӧ�ۻ���
			save_bodyVO.setAttributeValue("def32",
					salaryFundAccureTicketBodyVO.getDef32());// ���ƾ��
			save_bodyVO.setAttributeValue("def33",
					salaryFundAccureTicketBodyVO.getDef33());// ����˰��ۿ�-����
			save_bodyVO.setAttributeValue("def45",
					salaryFundAccureTicketBodyVO.getDef45());// ����˰��ۿ�-Ӫ��
			save_bodyVO.setAttributeValue("def46",
					salaryFundAccureTicketBodyVO.getDef46());// ����˰��ۿ�-�籣��˾����
			save_bodyVO.setAttributeValue("def47",
					salaryFundAccureTicketBodyVO.getDef47());// ����˰��ۿ�-�籣���˲���
			save_bodyVO.setAttributeValue("def21",
					salaryFundAccureTicketBodyVO.getDef21());// ����˰��ۿ�-������˾����
			save_bodyVO.setAttributeValue("def15",
					salaryFundAccureTicketBodyVO.getDef15());// ����˰��ۿ�-��������˲���
			save_bodyVO.setAttributeValue("def14",
					salaryFundAccureTicketBodyVO.getDef14());// ����˰��ۿ�-��˰
			save_bodyVO.setAttributeValue("def13",
					salaryFundAccureTicketBodyVO.getDef13());// Ӧ�۸�˰
			save_bodyVO.setAttributeValue("def12",
					salaryFundAccureTicketBodyVO.getDef12());// ���Ϲ�˾����
			save_bodyVO.setAttributeValue("def11",
					salaryFundAccureTicketBodyVO.getDef11());// ���ϸ��˲���
			save_bodyVO.setAttributeValue("def10",
					salaryFundAccureTicketBodyVO.getDef10());// ����ҽ�ƹ�˾����
			save_bodyVO.setAttributeValue("def9",
					salaryFundAccureTicketBodyVO.getDef9());// ����ҽ�Ƹ��˲���
			save_bodyVO.setAttributeValue("def8",
					salaryFundAccureTicketBodyVO.getDef8());// ʧҵ��˾����
			save_bodyVO.setAttributeValue("def7",
					salaryFundAccureTicketBodyVO.getDef7());// ʧҵ���˲���
			save_bodyVO.setAttributeValue("def5",
					salaryFundAccureTicketBodyVO.getDef5());// ���˹�˾����
			save_bodyVO.setAttributeValue("def4",
					salaryFundAccureTicketBodyVO.getDef4());// ���˸��˲���
			save_bodyVO.setAttributeValue("def3",
					salaryFundAccureTicketBodyVO.getDef3());// ������˾����
			save_bodyVO.setAttributeValue("def2",
					salaryFundAccureTicketBodyVO.getDef2());// �������˲���
			save_bodyVO.setAttributeValue("def1",
					salaryFundAccureTicketBodyVO.getDef1());// ������˾����
			save_bodyVO.setAttributeValue("def36",
					salaryFundAccureTicketBodyVO.getDef36());// ��������˲���
			save_bodyVO.setAttributeValue("def48",
					salaryFundAccureTicketBodyVO.getDef48());// ��������ۿ�-Ӫ��
			save_bodyVO.setAttributeValue("def49",
					salaryFundAccureTicketBodyVO.getDef49());// ��������ۿ�-��չ
			save_bodyVO.setAttributeValue("def50",
					salaryFundAccureTicketBodyVO.getDef50());// ��������ۿ�-����
			save_bodyVO.setAttributeValue("def53",
					salaryFundAccureTicketBodyVO.getDef53());// �ش󼲲�ҽ�Ʋ���-��˾����
			save_bodyVO.setAttributeValue("def54",
					salaryFundAccureTicketBodyVO.getDef54());// �ش󼲲�ҽ�Ʋ���-���˲���
			save_bodyVO.setAttributeValue("def55",
					salaryFundAccureTicketBodyVO.getDef55());// ҽ�������˻�-��˾����
			save_bodyVO.setAttributeValue("def56",
					salaryFundAccureTicketBodyVO.getDef56());// ҽ�������˻�-���˲���
			save_bodyVO.setAttributeValue("def57",
					salaryFundAccureTicketBodyVO.getDef57());// ����-��˾����
			save_bodyVO.setAttributeValue("def58",
					salaryFundAccureTicketBodyVO.getDef58());// ����-���˲���
			save_bodyVO.setStatus(VOStatus.NEW);
			save_bodyVO.setAttributeValue("money_de",
					salaryFundAccureTicketBodyVO.getMoney_de());// ʵ���ϼ� ԭ��
			save_bodyVO.setAttributeValue("local_money_bal",
					salaryFundAccureTicketBodyVO.getMoney_de());// ����=ԭ�ң�����һ����
			save_bodyVO.setAttributeValue("local_money_de",
					salaryFundAccureTicketBodyVO.getMoney_de());// ���
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// �����־
			save_bodyVO.setAttributeValue("billdate",
					save_headVO.getAttributeValue("billdate"));// ��������
			save_bodyVO.setAttributeValue("pk_group",
					save_headVO.getAttributeValue("pk_group"));// ��������
			save_bodyVO.setAttributeValue("pk_billtype",
					save_headVO.getAttributeValue("pk_billtype"));// �������ͱ���
			save_bodyVO.setAttributeValue("billclass",
					save_headVO.getAttributeValue("billclass"));// ���ݴ���
			save_bodyVO.setAttributeValue("pk_tradetype",
					save_headVO.getAttributeValue("pk_tradetype"));// Ӧ������code
			save_bodyVO.setAttributeValue("pk_tradetypeid",
					save_headVO.getAttributeValue("pk_tradetypeid"));// Ӧ������
			save_bodyVO.setAttributeValue("busidate",
					save_headVO.getAttributeValue("busidate"));// ��������
			save_bodyVO.setAttributeValue("objtype",
					save_headVO.getAttributeValue("objtype"));// ��������
			// 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�
			save_bodyVO.setAttributeValue("direction", 1);// ����
			save_bodyVO.setAttributeValue("pk_currtype",
					save_headVO.getAttributeValue("pk_currtype"));// ����
			save_bodyVO.setAttributeValue("rate", 1);// ��֯���һ���
			save_bodyVO.setAttributeValue("pk_deptid",
					save_headVO.getAttributeValue("pk_deptid"));// ����
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new SalaryFundAccureItem[0]));
		// getArapBillPubQueryService().getDefaultVO(aggvo, true);
		return aggvo;
	}

	protected AggSalaryfundaccure onDefaultValue(JSONObject head,
			JSONArray bodylist) throws BusinessException {
		AggSalaryfundaccure aggvo = new AggSalaryfundaccure();
		PayBillVO hvo = new PayBillVO();
		String billdate = head.getString("billdate") == null ? new UFDate()
				.toString() : head.getString("billdate");
		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// ��ǰʱ��
		hvo.setAttributeValue(PayBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// ����
		hvo.setAttributeValue(PayBillVO.PK_ORG, DocInfoQryUtils.getUtils()
				.getOrgInfo(head.getString("org")).get("pk_org"));// Ӧ��������֯->NCҵ��Ԫ����
		hvo.setAttributeValue(PayBillVO.BILLMAKER,
				getUserIDByCode(HCMSalaryOperatorName));// �Ƶ���
		hvo.setAttributeValue(PayBillVO.CREATOR, hvo.getBillmaker());// ������
		hvo.setAttributeValue(PayBillVO.CREATIONTIME, currTime);// ����ʱ��
		hvo.setAttributeValue(PayBillVO.PK_BILLTYPE, IBillFieldGet.F3);// �������ͱ���
		hvo.setAttributeValue(PayBillVO.BILLCLASS, IBillFieldGet.FK);// ���ݴ���
		hvo.setAttributeValue(PayBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ��������ϵͳ
		hvo.setAttributeValue(PayBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ������Դϵͳ
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE, IBillFieldGet.D1);// Ӧ������code
		hvo.setAttributeValue(PayBillVO.BILLSTATUS, ARAPBillStatus.SAVE.VALUE);// ����״̬
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE, "");// ��������

		// BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetype());
		// hvo.setAttributeValue(PayBillVO.PK_TRADETYPEID,
		// billTypeVo.getPk_billtypeid());// Ӧ������
		// hvo.setAttributeValue(PayBillVO.PK_TRADETYPE,
		// billTypeVo.getPk_billtypecode());// Ӧ������
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
		PayableBillItemVO[] itemVOs = new PayableBillItemVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new PayableBillItemVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// �к�
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
		// getArapBillPubQueryService().getDefaultVO(aggvo, true);

		return aggvo;
	}

	public IArapBillPubQueryService getArapBillPubQueryService() {
		if (arapBillPubQueryService == null) {
			arapBillPubQueryService = NCLocator.getInstance().lookup(
					IArapBillPubQueryService.class);
		}
		return arapBillPubQueryService;
	}

	/**
	 * ������Ϣ
	 * 
	 * @param hvo
	 * @param headvo
	 */
	protected void setHeaderVO(PayBillVO hvo, JSONObject head)
			throws BusinessException {
	};

	/**
	 * �Զ��嵵��
	 * 
	 * @param key
	 * @param pk_project
	 * @param
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDefdocInfo(String key, String listcode)
			throws BusinessException {
		// listcode = "zdy046";
		return DocInfoQryUtils.getUtils().getDefdocInfo(key, listcode);
	}

	/**
	 * ҵ��Ա��Ϣ
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getPsnInfo(String code) throws BusinessException {
		return DocInfoQryUtils.getUtils().getPsnInfo(code);
	}

	/**
	 * ���ݡ��û����롿��ȡ����
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getUserIDByCode(String code) throws BusinessException {
		String sql = "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
				+ code + "'";
		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}

	// /**
	// * ���ݡ��û����롿��ȡ����
	// *
	// * @param code
	// * @return
	// */
	// public String getUserPkByCode(String code) {
	// String sql =
	// "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
	// + code + "'";
	// String result = null;
	// try {
	// result = (String) getBaseDAO().executeQuery(sql,
	// new ColumnProcessor());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	/**
	 * ���ݱ���������ҿ���
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getcustomer(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select pk_customer from bd_customer where (code='" + code
						+ "' or name='" + code + "') and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}

	/**
	 * ���ݡ���˾���롿��ȡ������֯
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	/**
	 * ���ݡ���˾���롿��ȡ����
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_orgByCode(String code) {
		String sql = "select pk_org from org_orgs where (code='" + code
				+ "' or name = '" + code + "') and dr=0 and enablestate=2 ";
		String pk_org = null;
		try {
			pk_org = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���㷽ʽ��ѯ
	 * 
	 * @param balatype
	 * @return
	 */
	protected String getBalatypePkByCode(String balatype) {
		String result = null;
		String sql = "select  pk_balatype from bd_balatype where dr = 0 and code ='"
				+ balatype + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ͨ��ҵ��Ա�������ҵ��Ա���� 2020-07-02 ̸�ӽ�
	 * 
	 * @throws BusinessException
	 */
	public String getPk_psndocByCode(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select c.pk_psndoc from bd_psndoc c where c.code = '"
				+ code + "' and nvl(c.dr,0) = 0 and c.enablestate = 2  ");
		String value = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return value;
	}

	/**
	 * ���ݸ��������˻������ȡ��Ӧ���� 2020-07-03-̸�ӽ�
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getPersonalAccountIDByCode(String recaccount,
			String pk_receiver) throws BusinessException {
		String result = null;
		StringBuffer query = new StringBuffer();
		query.append("SELECT bd_psnbankacc.pk_bankaccsub AS pk_bankaccsub  ");
		query.append("  FROM bd_bankaccbas, bd_bankaccsub, bd_psnbankacc  ");
		query.append(" WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas  ");
		query.append("   AND bd_bankaccsub.pk_bankaccsub = bd_psnbankacc.pk_bankaccsub  ");
		query.append("   AND bd_bankaccsub.pk_bankaccbas = bd_psnbankacc.pk_bankaccbas  ");
		query.append("   and bd_psnbankacc.pk_bankaccsub != '~'  ");
		query.append("   AND bd_bankaccsub.Accnum = '" + recaccount + "'  ");
		query.append("   AND exists (select 1  ");
		query.append("          from bd_bankaccbas bas  ");
		query.append("         where bas.pk_bankaccbas = bd_psnbankacc.pk_bankaccbas  ");
		query.append("           and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y'))  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and (pk_psndoc = '" + pk_receiver + "' and  ");
		query.append("       pk_psnbankacc IN  ");
		query.append("       (SELECT min(pk_psnbankacc)  ");
		query.append("           FROM bd_psnbankacc  ");
		query.append("          GROUP BY pk_bankaccsub, pk_psndoc))  ");
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	// ��ȡ�Ƿ��ʱ����͹�˾����
	protected List<Map<String, String>> getLinkCompany(String pk_org)
			throws BusinessException {
		List<Map<String, String>> result = null;
		StringBuffer query = new StringBuffer();
		query.append("select factorvalue4,factorvalue5 from fip_docview_b where pk_classview=(select pk_classview from fip_docview ta where ta.viewcode ='XM01') and factorvalue2 = '"
				+ pk_org + "'");
		try {
			result = (List<Map<String, String>>) getBaseDAO().executeQuery(
					query.toString(), new MapListProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}
	
	 public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			   throws BusinessException {
			  Collection coll = getMDQryService().queryBillOfVOByCond(c,
			    whereCondStr, false);
			  if (coll.size() > 0) {
			   return (AggregatedValueObject) coll.toArray()[0];
			  } else {
			   return null;
			  }
		 }
}
