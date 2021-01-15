package nc.bs.tg.outside.paybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.service.TimeService;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.ebs.utils.recbill.RecbillUtils;
import nc.bs.tg.outside.sale.utils.paybill.PayBillConvertor;
import nc.bs.tg.outside.sale.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.ui.cdm.innerpay.action.returnBackAction;
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
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import vo.tg.outside.PayBillBodyVO;
import vo.tg.outside.PayBillHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class HCMSalaryReqPayBillToNC extends PayBillUtils implements
		ITGSyncService {
	public static final String HCMSalaryOperatorName = "RLZY";// HCMĬ�ϲ���Ա
	private IArapBillPubQueryService arapBillPubQueryService = null;
	String rlzyUserid = null;// HCMϵͳ�����û�
	static HCMSalaryReqPayBillToNC utils;

	public static HCMSalaryReqPayBillToNC getUtils() {
		if (utils == null) {
			utils = new HCMSalaryReqPayBillToNC();
		}
		return utils;
	}

	/**
	 * ���
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
		Logger.init("nc");
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
		Logger.debug("HCMSalaryReqPayBillToNC:��ʼת��");
		PayBillHeadVO headVO = JSONObject.parseObject(jsonhead,
				PayBillHeadVO.class);
		List<PayBillBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				PayBillBodyVO.class);
		Logger.debug("HCMSalaryReqPayBillToNC:����ת��");
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
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue("billno")
						+ "��,�����ظ��ϴ�!");
			}
			// AggPayBillVO billvo = (AggPayBillVO) convertor.castToBill(value,
			// AggPayBillVO.class, aggVO);
			Logger.debug("paybill��ʼת��");
			AggPayBillVO billvo = onTranBill(headVO, bodyVOs);
			Logger.debug("paybill����ת��");
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = (AggPayBillVO[]) getPfBusiAction().processAction(
					"SAVE", "F3", null, billvo, null, eParam);
			// obj = ipfBusiAction.processAction("APPROVE", billtype,
			// finalWorkflowVO == null ? null : finalWorkflowVO, objs[0],
			// userObj == null ? null : userObj, map != null ? map
			// : new HashMap());
			// IPFBusiAction ipfBusiAction = NCLocator.getInstance().lookup(
			// IPFBusiAction.class);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected AggPayBillVO onTranBill(PayBillHeadVO headVO,
			List<PayBillBodyVO> bodyVOs) throws BusinessException {
		
		AggPayBillVO aggvo = new AggPayBillVO();
		PayBillVO save_headVO = new PayBillVO();
		String pk_org = getPk_orgByCode(headVO.getPk_org());
		if (pk_org != null) {
			save_headVO.setPk_org(pk_org);// ������֯
		} else {
			throw new BusinessException("HCMͬ��NC����ϵͳ���ʸ����" + headVO.getSrcid()
					+ " �Ĳ�����֯��NC����ϵͳ�в����ڣ�������֯��" + headVO.getPk_org());
		}
		save_headVO.setDef1(headVO.getSrcid());// ��ϵͳ����
		save_headVO.setDef2(headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setPk_balatype(getBalatypePkByCode(headVO.getPk_balatype()));// ���㷽ʽ
		save_headVO.setBilldate(new UFDate(headVO.getBilldate()));// �ۿ�����
		save_headVO.setDef67(headVO.getDef67().substring(0, 7));// ����������
		save_headVO.setDef15(headVO.getDef30());// HCM������� NC��def15��json��def30

		List<Map<String, String>> budgetsubNames = getLinkCompany(pk_org);
		for (Map<String, String> names : budgetsubNames) {
			save_headVO.setDef56(names.get("factorvalue4"));// �Ƿ��ʱ���
			save_headVO.setDef57(names.get("factorvalue5"));// ��˾����
		}
		
		String pk_vid = getvidByorg(save_headVO
				.getPk_org());
		save_headVO.setPk_fiorg(save_headVO
				.getPk_org());
		save_headVO.setPk_fiorg_v(pk_vid);
		save_headVO.setSett_org((String) save_headVO
				.getPk_org());
		save_headVO.setSett_org_v(pk_vid);
		save_headVO.setCreationtime(new UFDateTime());
		save_headVO.setObjtype(1);
		save_headVO.setBillclass("fk");
		save_headVO.setApprovestatus(-1);
		save_headVO.setPk_tradetype("F3-Cxx-029");
		save_headVO.setPk_billtype("F3");// �������ͱ���
		save_headVO.setBilldate(new UFDate());// ��������
		save_headVO.setBusidate(new UFDate());//
		save_headVO.setSyscode(1);// ��������ϵͳ��Ĭ��Ϊ1��1=Ӧ��ϵͳ
		save_headVO.setSrc_syscode(1);// ������Դϵͳ
		save_headVO.setPk_currtype("1002Z0100000000001K1");// ����
		save_headVO.setBillstatus(2);// ����״̬,Ĭ��Ϊ2������
		save_headVO.setPk_group("000112100000000005FD");// �������ţ�Ĭ��Ϊʱ������
		save_headVO.setBillmaker(getUserIDByCode(HCMSalaryOperatorName));// �Ƶ���
		save_headVO.setObjtype(3); // ��������
														// 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�
		save_headVO.setCreator(getUserIDByCode(HCMSalaryOperatorName));// ������
		save_headVO.setIsinit(UFBoolean.FALSE);// �ڳ���־
		save_headVO.setIsreded(UFBoolean.FALSE);// �Ƿ����
		save_headVO.setStatus(VOStatus.NEW);

		List<PayBillItemVO> bodylist = new ArrayList<>();
		for (PayBillBodyVO payBillBodyVO : bodyVOs) {
			PayBillItemVO save_bodyVO = new PayBillItemVO();
			save_bodyVO.setPk_org(save_headVO.getPk_org());// Ӧ�ղ�����֯

			save_bodyVO.setDef18(getPk_orgByCode(payBillBodyVO.getDef18()));// ��ͬǩ����˾
			save_bodyVO.setDef30(getDefdocInfo(payBillBodyVO.getDef30(), "zdy046").get(
							"pk_defdoc"));// ��������
			if (getPsnInfo(payBillBodyVO.getPk_psndoc()) != null) {
				save_bodyVO.setPk_psndoc(getPsnInfo(payBillBodyVO.getPk_psndoc()).get(
								"pk_psndoc"));// ҵ��Ա
			} else {
				throw new BusinessException("ҵ��Ա"
						+ payBillBodyVO.getPk_psndoc() + "��NCϵͳ�в����ڣ�");
			}
			if (getPersonalAccountIDByCode(payBillBodyVO.getRecaccount(),
					save_bodyVO.getPk_psndoc()) != null) {
				save_bodyVO.setRecaccount(getPersonalAccountIDByCode(payBillBodyVO
								.getRecaccount(), save_bodyVO
								.getPk_psndoc()));// �տλ�����˻�
			} else {
				throw new BusinessException("ҵ��Ա"
						+ payBillBodyVO.getPk_psndoc() + "��û��"
						+ payBillBodyVO.getRecaccount() + "�˺ţ�");
			}
//			save_bodyVO.setDef22(payBillBodyVO.getDef22());// ����22
//			save_bodyVO.setDef29(payBillBodyVO.getDef29());// ��������-Ӫ��29
//			save_bodyVO.setDef27(payBillBodyVO.getDef27());// ��������-��չ27
//			save_bodyVO.setDef25(payBillBodyVO.getDef25());// ��������-����25
//			save_bodyVO.setDef24(payBillBodyVO.getDef24());// Ӷ��24
//			save_bodyVO.setDef23(payBillBodyVO.getDef23());// ����˰ǰ�ۿ�-�籣��˾����23
//			save_bodyVO.setDef20(payBillBodyVO.getDef20());// ����˰ǰ�ۿ�-�籣���˲���20
//			save_bodyVO.setDef19(payBillBodyVO.getDef19());// ����˰ǰ�ۿ�-������˾����19
//			save_bodyVO.setDef17(payBillBodyVO.getDef17());// ����˰ǰ�ۿ�-��������˲���17
//			save_bodyVO.setDef6(payBillBodyVO.getDef6());// Ӧ���ϼ�6
//			save_bodyVO.setDef31(payBillBodyVO.getDef31());// Ӧ�ۻ���31
//			save_bodyVO.setDef32(payBillBodyVO.getDef32());// ���ƾ��32
//			
//			save_bodyVO.setDef33(payBillBodyVO.getDef33());// ����˰��ۿ�-����33
//			save_bodyVO.setDef45(payBillBodyVO.getDef45());// ����˰��ۿ�-Ӫ��45
//			save_bodyVO.setDef46(payBillBodyVO.getDef46());// ����˰��ۿ�-�籣��˾����46
//			save_bodyVO.setDef47(payBillBodyVO.getDef47());// ����˰��ۿ�-�籣���˲���47
//			save_bodyVO.setDef21(payBillBodyVO.getDef21());// ����˰��ۿ�-������˾����21
//			save_bodyVO.setDef15(payBillBodyVO.getDef15());// ����˰��ۿ�-��������˲���15
//			save_bodyVO.setDef14(payBillBodyVO.getDef14());// ����˰��ۿ�-��˰14
//			save_bodyVO.setDef13(payBillBodyVO.getDef13());// Ӧ�۸�˰13
//			save_bodyVO.setDef12(payBillBodyVO.getDef12());// ���Ϲ�˾����12
//			save_bodyVO.setDef11(payBillBodyVO.getDef11());// ���ϸ��˲���11
//			
//			save_bodyVO.setDef59(payBillBodyVO.getDef59());// ����ҽ�ƹ�˾����59
//			save_bodyVO.setDef60(payBillBodyVO.getDef60());// ����ҽ�Ƹ��˲���60
			save_bodyVO.setDef90(payBillBodyVO.getDef10());// ����ҽ�ƹ�˾����10
			save_bodyVO.setDef89(payBillBodyVO.getDef9());// ����ҽ�Ƹ��˲���9
			save_bodyVO.setDef88(payBillBodyVO.getDef8());// ʧҵ��˾����8
			save_bodyVO.setDef87(payBillBodyVO.getDef7());// ʧҵ���˲���7
			save_bodyVO.setDef85(payBillBodyVO.getDef5());// ���˹�˾����5
			save_bodyVO.setDef84(payBillBodyVO.getDef4());// ���˸��˲���4
			save_bodyVO.setDef83(payBillBodyVO.getDef3());// ������˾����3
			save_bodyVO.setDef82(payBillBodyVO.getDef2());// �������˲���2
			save_bodyVO.setDef81(payBillBodyVO.getDef1());// ������˾����1
			
//			save_bodyVO.setDef36(payBillBodyVO.getDef36());// ��������˲���36
//			save_bodyVO.setDef48(payBillBodyVO.getDef48());// ��������ۿ�-Ӫ��48
//			save_bodyVO.setDef49(payBillBodyVO.getDef49());// ��������ۿ�-��չ49
//			save_bodyVO.setDef50(payBillBodyVO.getDef50());// ��������ۿ�-����50
//			save_bodyVO.setDef53(payBillBodyVO.getDef53());// �ش󼲲�ҽ�Ʋ���-��˾����53
//			save_bodyVO.setDef54(payBillBodyVO.getDef54());// �ش󼲲�ҽ�Ʋ���-���˲���54
//			save_bodyVO.setDef55(payBillBodyVO.getDef55());// ҽ�������˻�-��˾����55
//			save_bodyVO.setDef56(payBillBodyVO.getDef56());// ҽ�������˻�-���˲���56
//			save_bodyVO.setDef57(payBillBodyVO.getDef57());// ����-��˾����57
//			save_bodyVO.setDef58(payBillBodyVO.getDef58());// ����-���˲���58

			
			save_bodyVO.setMoney_de(new UFDouble(payBillBodyVO.getMoney_de()));// ʵ���ϼ� ԭ��
			save_bodyVO.setLocal_money_bal(new UFDouble(payBillBodyVO.getMoney_de()));// ����=ԭ�ң�����һ����
			save_bodyVO.setLocal_money_de(new UFDouble(payBillBodyVO.getMoney_de()));// ���
			save_bodyVO.setPausetransact(UFBoolean.FALSE);// �����־
			save_bodyVO.setPk_balatype(save_headVO.getPk_balatype());// ���㷽ʽ
			save_bodyVO
					.setBilldate(save_headVO.getBilldate());// ��������
			save_bodyVO
					.setPk_group(save_headVO.getPk_group());// ��������
			save_bodyVO.setPk_billtype(
					save_headVO.getPk_billtype());// �������ͱ���
			save_bodyVO.setBillclass(save_headVO.getBillclass());// ���ݴ���
			save_bodyVO.setPk_tradetype(
					save_headVO.getPk_tradetype());// Ӧ������code
			save_bodyVO.setPk_tradetypeid(save_headVO.getPk_tradetypeid());// Ӧ������
			save_bodyVO.setBusidate(save_headVO.getBilldate());// ��������
			save_bodyVO.setObjtype(save_headVO.getObjtype());// ��������
																				// 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�
			save_bodyVO.setDirection(1);// ����
			save_bodyVO.setPk_currtype(
					save_headVO.getPk_currtype());// ����
			save_bodyVO.setRate(new UFDouble(1));// ��֯���һ���
			save_bodyVO.setPk_deptid(save_headVO.getPk_deptid());// ����
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new PayBillItemVO[0]));
//		getArapBillPubQueryService().getDefaultVO(aggvo, true);
		
		return aggvo;
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
//		getArapBillPubQueryService().getDefaultVO(aggvo, true);

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
	 * ͨ��ҵ��Ա�������ҵ��Ա����
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
		query.append("select factorvalue4,factorvalue5 from fip_docview_b where pk_classview=(select pk_classview from fip_docview ta where ta.viewcode ='XM02') and factorvalue2 = '"
				+ pk_org + "'");
		try {
			result = (List<Map<String, String>>) getBaseDAO().executeQuery(
					query.toString(), new MapListProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}
}
