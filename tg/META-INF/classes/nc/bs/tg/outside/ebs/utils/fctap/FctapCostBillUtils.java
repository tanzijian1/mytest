package nc.bs.tg.outside.ebs.utils.fctap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IPfExchangeService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApBVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.CtApVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.fct.ap.entity.OutPutBVO;
import nc.vo.fct.ap.entity.SupplyAgreementBVO;
import nc.vo.fct.ar.entity.AggCtArVO;
import nc.vo.fct.ar.entity.ArOutPutBVO;
import nc.vo.fct.ar.entity.CtArBVO;
import nc.vo.fct.ar.entity.CtArVO;
import nc.vo.gateway60.accountbook.GlOrgUtils;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FctapCostBillUtils extends EBSBillUtils {
	static FctapCostBillUtils utils;

	public static FctapCostBillUtils getUtils() {
		if (utils == null) {
			utils = new FctapCostBillUtils();
		}
		return utils;
	}

	/**
	 * VOת��������
	 * 
	 * @return
	 */
	private IPfExchangeService ips = null;

	public IPfExchangeService getPfExchangeService() {
		if (ips == null) {
			ips = NCLocator.getInstance().lookup(IPfExchangeService.class);
		}
		return ips;
	}

	/**
	 * �����ͬ-�ɱ���ͬ����
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
		// ��ͷ����
		JSONObject headJSON = (JSONObject) value.get("headInfo");

		String vbillcode = headJSON.getString("vbillcode");// ��ͬ���(ebs��ͬ����)
		String pk = headJSON.getString("def49");// ebs��ͷ��ͬ����

		// Ŀ�굥����+�����ͬ���������
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":"
				+ vbillcode;
		// Ŀ�굥����+���󵥾�pk����־���
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + pk;

		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		AggCtApVO aggVO = null;
		// ���ر���
		Map<String, Object> dataMap = null;
		try {

			// ���NC�Ƿ�����Ӧ�ĵ��ݴ���
			aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
					"isnull(dr,0)=0 and blatest ='Y' and def49 = '" + pk + "'");
			String hpk = null;
			String def103=null;//�����ӳɱ�̨�����޳�������ͷdef103�����������ڳɱ�̨���С�(��ͷdef104)
			String def104=null;
			String def106=null;//�����Ƿ��߲�������ť�޸����ݱ�־�����ΪY��def103,def104ȡԭֵ��
			if (aggVO != null) {
				hpk = aggVO.getParentVO().getPrimaryKey();
				def103=aggVO.getParentVO().getDef103();
				def104=aggVO.getParentVO().getDef104();
				def106=aggVO.getParentVO().getDef106();
			}
			// ����ת�����߶���
			FctapCostConvertor fctapConvertor = new FctapCostConvertor();
			// ���ñ�ͷkey������ӳ��
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("fct_ap", "�����ͬ");
			fctapConvertor.setHVOKeyName(hVOKeyName);

			// ���ñ���key������ӳ��
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("fct_ap_plan", "����ƻ�ҳǩ��ǩԼ��");
			bVOKeyName.put("supplyAgreementBVO", "����Э��");
			bVOKeyName.put("fct_ap_b", "�ɱ����ҳǩ����ͬ������");
			bVOKeyName.put("fct_execution", "ִ�����");
			bVOKeyName.put("OutPutBVO", "��ֵ");
			fctapConvertor.setBVOKeyName(bVOKeyName);

			// ���ñ�ͷ�����ֶ�
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hKeyName = new HashMap<String, String>();
			// hKeyName.put("pk_org", "������֯(���й�˾)");
			// hKeyName.put("subscribedate", "ǩ�ָ�������");
			// hKeyName.put("cvendorid", "��Ӧ��");
			// hKeyName.put("plate", "���");
			hKeyName.put("contype", "��ͬ����");
			hKeyName.put("vbillcode", "��ͬ����");
			// hKeyName.put("def51", "NC���˷�Ʊ���");
			hKeyName.put("def52", "��ͬ״̬");
			hKeyName.put("def56", "�Ƿ�ͨ�ú�ͬ����");
			hKeyName.put("ctname", "��ͬ����");
			hKeyName.put("proname", "��Ŀ����");
			hKeyName.put("first", "�׷�");
			hKeyName.put("second", "�ҷ�");
			// hKeyName.put("vdef19", "��ͬ����");
			// hKeyName.put("d_sign", "ǩԼ����");
			// hKeyName.put("vdef4", "�а���ʽ");
			hKeyName.put("ntotalorigmny", "ǩԼ���");
			hKeyName.put("rate", "˰��");
			// hKeyName.put("depid", "���첿��");
			// hKeyName.put("vdef1", "ҵ̬");
			hKeyName.put("vdef15", "����״̬");
			// hKeyName.put("vdef2", "ҵ̬����");
			hKeyName.put("vdef5", "����ɱ����");
			// hKeyName.put("vdef6", "�ۼƱ�����");
			// hKeyName.put("vdef7", "�ۼƲ���Э����");
			// hKeyName.put("vdef8", "����������");
			// hKeyName.put("vdef16", "������");
			// hKeyName.put("vdef9", "��̬����˰��");
			// hKeyName.put("vdef10", "��̬������˰��");
			// hKeyName.put("vdef11", "��̬��˰�");
			// hKeyName.put("vdef18", "��Ʊ����˰��");
			// hKeyName.put("vdef21", "EBSʵ����(��˰)");
			// hKeyName.put("def31", "�ۼ�Ӧ����");
			// hKeyName.put("def40", "�ۼ������");
			hKeyName.put("def41", "EBS�ۼ�ʵ����(��˰)");
			// hKeyName.put("def42", "�ۼ�ʵ�������˰��");
			// hKeyName.put("def43", "�ۼ�ʵ���˰�");
			// hKeyName.put("def44", "δ���� ����˰��");
			// hKeyName.put("def45", "δ���� ������˰��");
			// hKeyName.put("def46", "δ���� ��˰�");

			hValidatedKeyName.put("fct_ap", hKeyName);
			fctapConvertor.sethValidatedKeyName(hValidatedKeyName);

			// ���ñ�������ֶ�
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			// **********ִ�����ҳǩ�ֶμ���
			Map<String, String> bExecutionBVOKeyName = new HashMap<String, String>();
			// bExecutionBVOKeyName.put("pk_ebs", "ebs����");
			// bExecutionBVOKeyName.put("billno", "���������");
			// bExecutionBVOKeyName.put("d_apply", "��������");
			// bExecutionBVOKeyName.put("mapply", "������");
			// bExecutionBVOKeyName.put("mpay", "�Ѹ����");
			// bExecutionBVOKeyName.put("paydate", "���֧��������");
			// bExecutionBVOKeyName.put("payway", "֧����ʽ");

			bValidatedKeyName.put("fct_execution", bExecutionBVOKeyName);
			// **********ִ�����ҳǩ�ֶμ���

			// **********����ƻ�ҳǩ�ֶμ���
			Map<String, String> bCtApPlanVOKeyName = new HashMap<String, String>();
			// bCtApPlanVOKeyName.put("pk_ebs", "ebs����");
			// bCtApPlanVOKeyName.put("d_payplan", "�ƻ���������");
			// bCtApPlanVOKeyName.put("planrate", "�ƻ��������");
			// bCtApPlanVOKeyName.put("planmoney", "�ƻ�������");
			// bCtApPlanVOKeyName.put("def2", "��������");
			// bCtApPlanVOKeyName.put("def1", "�Ƿ�֧�������ܻ�");
			bValidatedKeyName.put("fct_ap_plan", bCtApPlanVOKeyName);
			// **********����ƻ�ҳǩ�ֶμ���

			// **********����Э��ҳǩ�ֶμ���
			Map<String, String> bSupplyAgreementBVOKeyName = new HashMap<String, String>();
			// bSupplyAgreementBVOKeyName.put("pk_ebs", "ebs����");
			// bSupplyAgreementBVOKeyName.put("billno", "Э����");
			// bSupplyAgreementBVOKeyName.put("name", "Э������");
			// bSupplyAgreementBVOKeyName.put("mmonery", "Э����");
			// bSupplyAgreementBVOKeyName.put("d_date", "ǩԼ����");
			// bSupplyAgreementBVOKeyName.put("msupply", "Э�����ۼ�");
			bValidatedKeyName.put("supplyAgreementBVO",
					bSupplyAgreementBVOKeyName);
			// **********����Э��ҳǩ�ֶμ���

			// **********��ֵҳǩ�ֶμ���
			Map<String, String> bOutPutBVOKeyName = new HashMap<String, String>();
			// bOutPutBVOKeyName.put("pk_ebs", "ebs����");
			// bOutPutBVOKeyName.put("mcz", "�ۼƲ�ֵ_��˰");
			// bOutPutBVOKeyName.put("mczbhs", "�ۼƲ�ֵ_����˰");
			// bOutPutBVOKeyName.put("mczse", "�ۼƲ�ֵ_˰��");
			// bOutPutBVOKeyName.put("mwfcz", "�ۼ�δ����ֵ_��˰");
			// bOutPutBVOKeyName.put("mwfczbhs", "�ۼ�δ����ֵ_����˰");
			// bOutPutBVOKeyName.put("mwfczse", "�ۼ�δ����ֵ_˰��");
			// bOutPutBVOKeyName.put("outputtime", "��ֵʱ��");
			// bOutPutBVOKeyName.put("m_supply", "Э�����ۼ�");
			bValidatedKeyName.put("OutPutBVO", bOutPutBVOKeyName);
			// **********��ֵҳǩ�ֶμ���

			// **********��ͬ�������ɱ���֣�ҳǩ�ֶμ���
			Map<String, String> bCtApBVOKeyName = new HashMap<String, String>();
			// bCtApBVOKeyName.put("pk_ebs", "ebs����");
			// bCtApBVOKeyName.put("vbdef11", "��Ŀ����");
			// bCtApBVOKeyName.put("vbdef23", "��Ŀ����");
			// bCtApBVOKeyName.put("vbdef22", "��Ʒ���͡�ҵ̬");
			// bCtApBVOKeyName.put("vbdef13", "��ֱ���");
			// bCtApBVOKeyName.put("vbdef21", "��ֽ��");
			bValidatedKeyName.put("fct_ap_b", bCtApBVOKeyName);
			// **********��ͬ�������ɱ���֣�ҳǩ�ֶμ���
			fctapConvertor.setbValidatedKeyName(bValidatedKeyName);

			// ���ò����ֶ�
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("fct_ap-plate"); // ��� -- ���*
			refKeys.add("fct_ap-proname"); // ��Ŀ���� -- ��Ŀ*
			refKeys.add("fct_ap-pk_org"); // ������֯�����й�˾��*
			refKeys.add("fct_ap-depid");// ���첿�� -- ����*
			refKeys.add("fct_ap-first");// �׷���λ -- ������֯*
			refKeys.add("fct_ap-second");// �ҷ���λ -- ��Ӧ��*
			refKeys.add("fct_ap-vdef1");// ҵ̬ -- ҵ̬
			refKeys.add("fct_ap_plan-def2");// ��������-����ƻ���ǩԼ��
			refKeys.add("fct_ap_b-vbdef11");// ��Ŀ���ƣ�Ԥ���Ŀ��-��ͬ�������ɱ���֣�
			// refKeys.add("fct_ap-vdef4");// �а���ʽ -- �а���ʽ
			fctapConvertor.setRefKeys(refKeys);

			// Ĭ�ϼ���-ʱ������
			fctapConvertor.setDefaultGroup("000112100000000005FD");
			AggCtApVO billvo = (AggCtApVO) fctapConvertor.castToBill(value,
					AggCtApVO.class, aggVO);

			// ���ú�ͬ����Ĭ����Ϣ

			// ********************��ͷ������Ϣ����***************

			String first = fctapConvertor.getRefAttributePk("fct_ap-first",
					billvo.getParentVO().getFirst());
			billvo.getParentVO().setFirst(first); // �׷���������֯��

			// billvo.getParentVO().setPk_org(
			// fctapConvertor.getRefAttributePk("fct_ap-pk_org", billvo
			// .getParentVO().getFirst())); // ������֯
			billvo.getParentVO().setPk_org(first);

			billvo.getParentVO().setPk_org_v(
					GlOrgUtils.getPkorgVersionByOrgID(billvo.getParentVO()
							.getFirst()));

			billvo.getParentVO().setPlate(
					fctapConvertor.getRefAttributePk("fct_ap-plate", billvo
							.getParentVO().getPlate())); // ���
			billvo.getParentVO().setProname(
					fctapConvertor.getRefAttributePk("fct_ap-proname", billvo
							.getParentVO().getProname(), billvo.getParentVO()
							.getFirst(), billvo.getParentVO().getFirst())); // ��Ŀ���ƣ���Ŀ��
			billvo.getParentVO().setSecond(
					fctapConvertor.getRefAttributePk("fct_ap-second", billvo
							.getParentVO().getSecond(), billvo.getParentVO()
							.getPk_org(), billvo.getParentVO().getPk_org()));// �ҷ�����Ӧ�̣�
			// billvo.getParentVO().setDepid(fctapConvertor.getRefAttributePk("fct_ap-depid",
			// billvo.getParentVO().getDepid(),
			// billvo.getParentVO().getPk_org())); // ���죨�а죩����
			billvo.getParentVO().setVdef1(
					fctapConvertor.getRefAttributePk("fct_ap-vdef1", billvo
							.getParentVO().getVdef1())); // ҵ̬
			// ********************��ͷ������Ϣ����***************

			// ��ͷĬ����Ϣ����
			billvo.getParentVO().setCtrantypeid(
					fctapConvertor.getRefAttributePk("ctrantypeid",
							"FCT1-Cxx-001")); // ��������
			billvo.getParentVO().setCbilltypecode("FCT1"); // ��������
			billvo.getParentVO().setValdate(new UFDate()); // �ƻ���Ч����
			billvo.getParentVO().setInvallidate(new UFDate());// �ƻ���ֹ����
			billvo.getParentVO().setNexchangerate(new UFDouble(100));// �۱�����
			billvo.getParentVO().setSubscribedate(new UFDate());// ����ǩ������
			UFDouble ntotalorigmny = (UFDouble) billvo.getParent()
					.getAttributeValue("ntotalorigmny");
			billvo.getParentVO().setMsign(ntotalorigmny);// ǩԼ���

			/**
			 * ���й�˾ͨ���׷�������2019-10-24-tzj
			 */
			String vdef14 = fctapConvertor.getRefAttributePk("fct_ap-pk_org",
					billvo.getParentVO().getFirst());
			billvo.getParentVO().setVdef14(vdef14);
			// ͨ��vdef12���������Ա2020-02-17-̸�ӽ�
			String vdef12 = fctapConvertor.getRefAttributePk("fct_ap-vdef12",
					billvo.getParentVO().getVdef12());
			billvo.getParentVO().setVdef12(vdef12);
			// // ���й�˾��������֯��

			billvo.getParentVO().setCvendorid(billvo.getParentVO().getSecond());// �ҷ�����Ӧ�̣�

			// �����ֶ�2019-10-30-tzj
			// billvo.getParentVO().setDef51(billvo.getParentVO().getDef51());//
			// NC���˷�Ʊ���
			billvo.getParentVO().setDef41(billvo.getParentVO().getDef41());// EBS�ۼ�ʵ����(��˰)
			billvo.getParentVO().setDef52(billvo.getParentVO().getDef52());// ��ͬ״̬
			billvo.getParentVO().setDef53(billvo.getParentVO().getDef53());// �ڲ�������
			billvo.getParentVO().setDef54(billvo.getParentVO().getDef54());// �ܰ���ͬ����
			billvo.getParentVO().setDef55(billvo.getParentVO().getDef55());// �ܰ���ͬ����
			billvo.getParentVO().setDef56(billvo.getParentVO().getDef56());// �Ƿ�ͨ�ú�ͬ����
			billvo.getParentVO().setDef58(billvo.getParentVO().getDef58());// �����˱���
			billvo.getParentVO().setDef61(billvo.getParentVO().getDef61());// def61
																			// NC�ڳ���Ʊ����˰��
			billvo.getParentVO().setDef62(billvo.getParentVO().getDef62());// def62
																			// NC�ڳ���Ʊ������˰��
			billvo.getParentVO().setDef63(billvo.getParentVO().getDef63());// def63
																			// NC�ڳ���Ʊ��˰�
			billvo.getParentVO().setDef96(billvo.getParentVO().getDef96());// def96
																			// NC�ڳ�ʵ�������˰��
			billvo.getParentVO().setDef97(billvo.getParentVO().getDef97());// def97
																			// NC�ڳ�ʵ���˰�
			billvo.getParentVO().setDef98(billvo.getParentVO().getDef98());// def98
																			// NC�ڳ�ʵ�����˰��
			billvo.getParentVO().setDef99(billvo.getParentVO().getDef99());// def99
																			// �Ƿ��Զ��ۿ�
			String def100 = fctapConvertor.getRefAttributePk("fct_ap-def100",
					billvo.getParentVO().getDef100());
			billvo.getParentVO().setDef100(def100);// def100 ��������
			billvo.getParentVO().setDef79(billvo.getParentVO().getDef79());// ebs�汾����
			if (billvo.getParentVO().getDef101() != null
					&& !"".equals(billvo.getParentVO().getDef101())) {
				String def101 = fctapConvertor.getRefAttributePk(
						"fct_ap-def101", billvo.getParentVO().getDef101());
				billvo.getParentVO().setDef101(def101);// def101 ҵ����
			}
			billvo.getParentVO().setDef102(billvo.getParentVO().getDef102());// def102
																				// ���������˻�
			// ����Ĭ����Ϣ����

			ISuperVO[] ctApBVOs = billvo.getChildren(CtApBVO.class);// �ɱ����ҳǩ����ͬ������

			int ctApBVORowNo = 10;
			if (ctApBVOs != null && ctApBVOs.length > 0) {
				for (ISuperVO tmpCtApBVO : ctApBVOs) {
					CtApBVO ctApBVO = (CtApBVO) tmpCtApBVO;
					ctApBVO.setCrowno(String.valueOf(ctApBVORowNo));
					ctApBVO.setPk_fct_ap(hpk);
					ctApBVO.setPk_org(billvo.getParentVO().getPk_org());
					ctApBVO.setPk_org_v(billvo.getParentVO().getPk_org());
					ctApBVO.setPk_group(billvo.getParentVO().getPk_group());
					ctApBVO.setFtaxtypeflag(1); // ��˰���
					ctApBVO.setNorigtaxmny(billvo.getParentVO()
							.getNtotalorigmny()); // ԭ�Ҽ�˰�ϼ�
					ctApBVO.setAttributeValue("ntaxmny", 1); // ���Ҽ�˰�ϼ�
					ctApBVORowNo += 10;

					// **********�ɱ���֣���ͬ������������ֵ
					ctApBVO.setVbdef11(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef11", ctApBVO.getVbdef11())); // ��Ŀ���ƣ�Ԥ���Ŀ��
					// **********�ɱ���֣���ͬ������������ֵ
				}
			}
			// else {
			// throw new BusinessException("�ɱ����ҳǩ(��ͬ����)ҳǩ����Ϊ��!");
			// }

			ISuperVO[] supplyAgreementBVOs = billvo
					.getChildren(SupplyAgreementBVO.class);// ����Э��
			if (supplyAgreementBVOs != null && supplyAgreementBVOs.length > 0) {
				for (ISuperVO tmpSupplyAgreementBVO : supplyAgreementBVOs) {
					SupplyAgreementBVO supplyAgreementBVO = (SupplyAgreementBVO) tmpSupplyAgreementBVO;
					supplyAgreementBVO.setPk_fct_ap(hpk);
					supplyAgreementBVO.setStatus(VOStatus.NEW);
				}
			}

			ISuperVO[] ctApPlanVOs = billvo.getChildren(CtApPlanVO.class);// ����ƻ�ҳǩ��ǩԼ��
			if (ctApPlanVOs != null && ctApPlanVOs.length > 0) {
				for (ISuperVO tmpCtApPlanVO : ctApPlanVOs) {
					CtApPlanVO ctApPlanVO = (CtApPlanVO) tmpCtApPlanVO;
					ctApPlanVO.setPk_fct_ap(hpk);
					ctApPlanVO.setPk_org(billvo.getParentVO().getPk_org());
					ctApPlanVO.setPk_group(billvo.getParentVO().getPk_group());
					// **********����ƻ�ҳǩ��ǩԼ��������ֵ
					ctApPlanVO.setPaymenttype(fctapConvertor.getRefAttributePk(
							"fct_ap_plan-paymenttype",
							ctApPlanVO.getPaymenttype())); // ��������-����ƻ���ǩԼ��*
					// **********����ƻ�ҳǩ��ǩԼ��������ֵ
					ctApPlanVO.setStatus(VOStatus.NEW);
				}
			}

			ISuperVO[] executionBVOs = billvo.getChildren(ExecutionBVO.class);// ִ�����
			if (executionBVOs != null && executionBVOs.length > 0) {
				for (ISuperVO tmpExecutionBVO : executionBVOs) {

					ExecutionBVO executionBVO = (ExecutionBVO) tmpExecutionBVO;
					// ��ִ�������fct_executionҳǩ��֧����ʽ��payway��nc����ӳ���ϵ-2020-02-17-̸�ӽ�
					String payway = executionBVO.getPayway();
					String pk_balatype = getPayWay(payway);
					executionBVO.setPayway(pk_balatype);
					executionBVO.setPk_fct_ap(hpk);
					executionBVO.setStatus(VOStatus.NEW);

				}
			}

			ISuperVO[] outPutBVOs = billvo.getChildren(OutPutBVO.class);// ��ֵ
			if (outPutBVOs != null && outPutBVOs.length > 0) {
				for (ISuperVO tmpOutPutBVO : outPutBVOs) {
					OutPutBVO outPutBVO = (OutPutBVO) tmpOutPutBVO;
					outPutBVO.setPk_fct_ap(hpk);
					outPutBVO.setStatus(VOStatus.NEW);
				}
			}

			HashMap<String, Object> eParam = new HashMap<String, Object>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			AggCtApVO billVO = null;
			if (aggVO == null) {
				// �����ڣ���������blatest
				billvo.getParentVO().setCreator(getSaleUserID());
				billvo.getParentVO().setBillmaker(getSaleUserID());
				billvo.getParentVO().setCreationtime(new UFDateTime());
				billvo.getParentVO().setDmakedate(new UFDate());
				billvo.getParentVO().setFstatusflag(0);
				billvo.getParentVO().setVersion(UFDouble.ONE_DBL);
				// billvo.getParentVO().setBlatest(UFBoolean.TRUE);
				// NC�ڳ�ʵ���� ��һ��Ĭ�ϵ���EBS�ۼ�ʵ����
				billvo.getParentVO().setVdef22(billvo.getParentVO().getDef41());
				String def98 = billvo.getParentVO().getDef98();

				billvo = getCount(def98, executionBVOs, billvo);

				// �������
				String vdef23 = billvo.getParentVO().getVdef23();
				billvo.getParentVO().setVdef23(vdef23);
				// �ۿ���
				String vdef24 = billvo.getParentVO().getVdef24();
				billvo.getParentVO().setVdef24(vdef24);

				billVO = ((AggCtApVO[]) getPfBusiAction().processAction(
						"SAVEBASE", "FCT1", null, billvo, null, eParam))[0];

				billVO = (AggCtApVO) getMDQryService().queryBillOfVOByPK(
						AggCtApVO.class, billVO.getPrimaryKey(), false);
				eParam.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER",
						new AggCtApVO[] { billVO });

				Object obj = getPfBusiAction().processAction(
						"APPROVE" + getSaleUserID(), "FCT1", null, billVO,
						null, eParam);

				getPfBusiAction().processAction("VALIDATE" + getSaleUserID(),
						"FCT1", null, ((AggCtApVO[]) obj)[0], null, eParam);

			} else {
				
				billvo = getCount(aggVO.getParentVO().getDef98(),
						executionBVOs, billvo);
			//TODO 20200814-C
				if("Y".equals(def106)){
					billvo.getParentVO().setDef103(def103);
					billvo.getParentVO().setDef104(def104);
				}
				// ���ڸ��µ�
				syncBvoPkByEbsPk(CtApPlanVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_ap_plan",
						"fct_ap_plan"); // ͬ������ƻ���ǩԼ��PK
				syncBvoPkByEbsPk(CtApBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_ap_b", "fct_ap_b");// ͬ���ɱ����ҳǩ����ͬ������PK
				syncBvoPkByEbsPk(SupplyAgreementBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_supply_agreement_b",
						"fct_supply_agreement_b");// ͬ������Э��PK
				// syncBvoPkByEbsPk(ExecutionBVO.class, aggVO.getParentVO()
				// .getPrimaryKey(), billvo, "pk_execution_b",
				// "fct_execution_b");// ͬ��ִ�����PK
				syncBvoPkByEbsPk(OutPutBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_output", "fct_output_b");// ͬ����ֵPK

				billvo.getParentVO().setTs(aggVO.getParentVO().getTs()); // ͬ����ͷts
				billvo.getParentVO().setStatus(VOStatus.UPDATED);
				billvo.getParentVO().setPrimaryKey(hpk);

				billvo.getParentVO().setModifier(getSaleUserID());
				billvo.getParentVO().setModifiedtime(new UFDateTime());

				syncBvoTs(CtApPlanVO.class, aggVO, billvo); // ����ƻ�ҳǩ��ǩԼ��ҳǩts
				syncBvoTs(CtApBVO.class, aggVO, billvo); // ͬ���ɱ����ҳǩ����ͬ������ҳǩts
				syncBvoTs(SupplyAgreementBVO.class, aggVO, billvo); // ͬ������Э��ҳǩts
				// syncBvoTs(ExecutionBVO.class, aggVO, billvo); // ͬ��ִ�����ҳǩts
				syncBvoTs(OutPutBVO.class, aggVO, billvo); // ͬ����ֵҳǩts
				// �Ѿɵ�ִ�����ɾ��2019-11-27-̸�ӽ�
				deleteExecution(billvo);
				billVO = ((AggCtApVO[]) getPfBusiAction().processAction(
						"MODIFY", "FCT1", null, billvo, null, eParam))[0];
			}

			// ���ر�ͷ���ݺź͵���PK����
			dataMap = new HashMap<String, Object>();
			dataMap.put("billid", billVO.getParentVO().getPrimaryKey());
			dataMap.put("billno", billVO.getParentVO().getVbillcode());
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(dataMap);

	}

	private void syncBvoTs(Class<? extends ISuperVO> clazz, AggCtApVO srcAggVO,
			AggCtApVO desAggVO) {
		ISuperVO[] sbVOTss = srcAggVO.getChildren(clazz);// ��Դ���ݱ���
		// ������Դ���ݱ���
		if (sbVOTss != null && sbVOTss.length > 0) {
			for (ISuperVO stmpBVOts : sbVOTss) {
				// ��Դ����pk
				String sbvoPk = stmpBVOts.getPrimaryKey();
				// ��Դ����ts
				UFDateTime sts = (UFDateTime) stmpBVOts.getAttributeValue("ts");

				ISuperVO[] dbVOTss = desAggVO.getChildren(clazz);// Ŀ�굥�ݱ���
				// ����Ŀ�굥�ݱ���
				if (dbVOTss != null && dbVOTss.length > 0) {
					for (int i = 0; i < dbVOTss.length; i++) {
						// Ŀ�����pk
						String dbvoPk = dbVOTss[i].getPrimaryKey();
						// ��Դ������Ŀ�����pk��ͬ������ͬһ�������ͬ��ts
						if (sbvoPk.equals(dbvoPk)) {
							dbVOTss[i].setAttributeValue("ts", sts);
							dbVOTss[i].setStatus(VOStatus.UPDATED);
						}
					}
				}
			}
		}
	}

	/*
	 * private Map<String, Object> fillDataMap(AggCtApVO aggVO){ //
	 * vo��billid��billno��ӳ�� Map<String, Object> voIdNoMap = new HashMap<String,
	 * Object>(); // billid��ֵ��billno��ֵ��ӳ�� Map<String, Object> billIdNoMap =
	 * null;
	 * 
	 * // ***********��ͷ���� billIdNoMap = new HashMap<String, Object>();
	 * billIdNoMap.put("billid", aggVO.getParentVO().getPrimaryKey());
	 * billIdNoMap.put("billno", aggVO.getParentVO().getVbillcode());
	 * voIdNoMap.put("fct_ap", billIdNoMap); // ***********��ͷ����
	 * 
	 * // ����id�б� List<String> bidList = null; // ***********���帶��ƻ�ҳǩ��ǩԼ������ //
	 * �������� billIdNoMap = new HashMap<String, Object>(); bidList = new
	 * ArrayList<String>(); ISuperVO[] ctApPlanVOs =
	 * aggVO.getChildren(CtApPlanVO.class); for(ISuperVO tmpCtApPlanVO :
	 * ctApPlanVOs){ CtApPlanVO ctApPlanVO = (CtApPlanVO) tmpCtApPlanVO;
	 * bidList.add(ctApPlanVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_ap_plan", billIdNoMap); //
	 * ***********����ƻ�ҳǩ��ǩԼ������
	 * 
	 * // ***********������Э�鱨�� // �������� billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[]
	 * supplyAgreementBVOs = aggVO.getChildren(SupplyAgreementBVO.class);// ����Э��
	 * for(ISuperVO tmpSupplyAgreementBVO : supplyAgreementBVOs){
	 * SupplyAgreementBVO supplyAgreementBVO = (SupplyAgreementBVO)
	 * tmpSupplyAgreementBVO; bidList.add(supplyAgreementBVO.getPrimaryKey()); }
	 * billIdNoMap.put("billid", bidList); voIdNoMap.put("supplyAgreementBVO",
	 * billIdNoMap); // ***********������Э�鱨��
	 * 
	 * // ***********���ɱ����ҳǩ����ͬ���������� // �������� billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] ctApBVOs =
	 * aggVO.getChildren(CtApBVO.class);// �ɱ����ҳǩ����ͬ������ for(ISuperVO tmpCtApBVO
	 * : ctApBVOs){ CtApBVO ctApBVO = (CtApBVO) tmpCtApBVO;
	 * bidList.add(ctApBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_ap_b", billIdNoMap); //
	 * ***********���ɱ����ҳǩ����ͬ����������
	 * 
	 * // ***********��ִ��������� // �������� billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] executionBVOs =
	 * aggVO.getChildren(ExecutionBVO.class);// ִ����� for(ISuperVO
	 * tmpExecutionBVO : executionBVOs){ ExecutionBVO executionBVO =
	 * (ExecutionBVO) tmpExecutionBVO;
	 * bidList.add(executionBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_execution", billIdNoMap); //
	 * ***********����ִ���������
	 * 
	 * // ***********�����ֵ���� // �������� billIdNoMap = new HashMap<String, Object>();
	 * bidList = new ArrayList<String>(); ISuperVO[] outPutBVOs =
	 * aggVO.getChildren(OutPutBVO.class);// ��ֵ for(ISuperVO tmpOutPutBVO :
	 * outPutBVOs){ OutPutBVO outPutBVO = (OutPutBVO) tmpOutPutBVO;
	 * bidList.add(outPutBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("OutPutBVO", billIdNoMap); // ***********�����ֵ����
	 * return voIdNoMap; }
	 */

	/**
	 * <p>
	 * Title: getBvoPkByEbsPK<��p>
	 * <p>
	 * Description: <��p>
	 * 
	 * @param ebsPk
	 *            ebs��Ӧҳǩ����������
	 * @param parentPKfield
	 *            NC�����ͬ��ͷ�����ֶ�
	 * @param bvoPKfiled
	 *            NC�����ͬ��Ӧҳǩ�����ֶ�
	 * @param table
	 *            NC�����ͬ��Ӧҳǩ����
	 * @param parentPkValue
	 *            NC�����ͬ��ͷ����ֵ
	 * @return
	 */
	private String getBvoPkByEbsPK(String ebsPk, String parentPKfield,
			String bvoPKfiled, String table, String parentPkValue) {
		String bvoPk = null;
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = getBaseDAO();
		sql = "select " + bvoPKfiled + " from " + table
				+ " where pk_ebs = ? and " + parentPKfield + " = ?";
		try {
			parameter.addParam(ebsPk);
			parameter.addParam(parentPkValue);
			bvoPk = (String) dao.executeQuery(sql, parameter,
					new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return bvoPk;
	}

	/**
	 * <p>
	 * Title: syncBvoPkByEbsPk<��p>
	 * <p>
	 * Description: <��p>
	 * 
	 * @param clazz
	 *            voClass
	 * @param parentPKValue
	 *            NC�����ͬ��ͷ����ֵ
	 * @param desAggVO
	 *            ���ι�����aggVO(Ŀ��)
	 * @param bvoPKfiled
	 *            vo�������ֶ���
	 * @param table
	 *            vo���ݱ���
	 * @throws DAOException
	 */
	private void syncBvoPkByEbsPk(Class<? extends ISuperVO> clazz,
			String parentPKValue, AggCtApVO desAggVO, String bvoPKfiled,
			String table) throws DAOException {
		// ��������BVOs
		ISuperVO[] syncDesPkBVOs = desAggVO.getChildren(clazz);
		if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
			for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
				// ������������ebsPK
				String desEBSPk = (String) tmpDesBVO
						.getAttributeValue("pk_ebs");
				// �ж��Ƿ��ǲ�ֵҳǩ
				if (desEBSPk == null && "fct_output_b".equals(table)) {
					String sql = "select b.outputtime,b.pk_output from fct_output_b b where b.pk_fct_ap = '"
							+ parentPKValue + "' and b.dr = 0";

					List<Map<String, String>> outputtimeMap = (List<Map<String, String>>) getBaseDAO()
							.executeQuery(sql, new MapListProcessor());
					String time = (String) tmpDesBVO.getAttributeValue(
							"outputtime").toString();
					for (Map<String, String> map : outputtimeMap) {
						String outputtime = map.get("outputtime");
						String pk_output = map.get("pk_output");
						if (time != null && !"".equals(time)) {
							if (time.equals(outputtime)) {
								tmpDesBVO.setAttributeValue(bvoPKfiled,
										pk_output);
								tmpDesBVO.setStatus(VOStatus.UPDATED);
							} else {
								tmpDesBVO.setStatus(VOStatus.NEW);
							}
						}
					}
				} else {
					// ������������ebsPKΪ�����������ı������ݣ������Ǹ��µı�������
					tmpDesBVO.setAttributeValue(
							bvoPKfiled,
							getBvoPkByEbsPK(desEBSPk, "pk_fct_ap", bvoPKfiled,
									table, parentPKValue));
					if (tmpDesBVO.getPrimaryKey() == null) {
						tmpDesBVO.setStatus(VOStatus.NEW);
					} else {
						tmpDesBVO.setStatus(VOStatus.UPDATED);

					}
				}

			}
		}
	}

	// ��ͷ������
	private AggCtApVO getCount(String def98, ISuperVO[] executionBVOs,
			AggCtApVO billvo) {
		UFDouble def98Double = new UFDouble(def98);
		UFDouble mpay = new UFDouble();
		if (executionBVOs != null) {
			for (ISuperVO tmpExecutionBVO : executionBVOs) {
				ExecutionBVO executionBVO = (ExecutionBVO) tmpExecutionBVO;
				UFDouble m = executionBVO.getMpay();
				if (m != null) {
					mpay = mpay.add(m);
				}

			}
		}
		if (mpay != null) {
			UFDouble sun = mpay.add(def98Double);
			//UFDouble sun = mpay;
			if (sun != null) {
				String vdef21 = sun.toString();
				// NC�ۼ�ʵ�����˰��= NC�ڳ�ʵ����+NC�ۼƷ���
				billvo.getParentVO().setVdef21(vdef21);

				// NCδ���� ����˰��def44=def40-vdef21
				UFDouble def44 = new UFDouble(billvo.getParentVO().getDef40())
						.sub(sun);
				billvo.getParentVO().setDef44(
						def44 == null ? "0" : def44.toString());

				// �ۼ�ǷƱ=NC�ۼ�ʵ����˰-BES�ۼƷ�Ʊ��� def42=vdef21-vdef1
				UFDouble def42 = sun.sub(new UFDouble(billvo.getParentVO()
						.getVdef18()));
				billvo.getParentVO().setDef42(
						def42 == null ? "0" : def42.toString());

				// UFDouble rate = billvo.getParentVO().getRate();
				// UFDouble rateUFDouble = new UFDouble();
				// if (rate != null) {
				// rateUFDouble = new UFDouble(rate).div(100).add(1);
				// // NC�ۼ�ʵ�������˰��= NC�ۼ�ʵ�����˰��/(1+˰��/100)
				// String def42 = sun.div(rateUFDouble).toString();
				// billvo.getParentVO().setDef42(def42);
				// // NC�ۼ�ʵ���˰�= (�ۼ�ʵ�����˰��/(1+˰��/100))*˰��/100
				// String def43 = sun.div(rateUFDouble)
				// .multiply(rate.div(100)).toString();
				// billvo.getParentVO().setDef43(def43);
				// }
				// String vdef9 = billvo.getParentVO().getVdef9();
				// if (vdef9 != null && !"".equals(vdef9)) {
				// UFDouble vdef9UFDouble = new UFDouble(vdef9);
				// // NCδ���� ����˰�� = ��̬����˰��-NC�ۼ�ʵ�����˰��
				// String def44 = vdef9UFDouble.sub(sun).toString();
				// billvo.getParentVO().setDef44(def44);
				// // NCδ���� ������˰�� = δ�����˰��/(1+˰��/100)
				// if (rateUFDouble != null) {
				// UFDouble def45 = vdef9UFDouble.sub(sun).div(
				// rateUFDouble);
				// billvo.getParentVO().setDef45(def45.toString());
				// // NCδ���� ��˰� = δ�����˰��*(rate/100)
				// UFDouble def46 = def45.multiply(new UFDouble(rate)
				// .div(100));
				// billvo.getParentVO().setDef46(def46.toString());
				// }
				// }
			}

		}

		return billvo;

	}

	/**
	 * �Ѿɵ�ִ�����ɾ��2019-11-27-̸�ӽ�
	 * 
	 * @param billvo
	 * @throws DAOException
	 */
	private void deleteExecution(AggCtApVO billvo) throws DAOException {
		String primaryKey = billvo.getParent().getPrimaryKey();
		ISuperVO[] syncDesPkBVOs = billvo.getChildren(ExecutionBVO.class);
		BaseDAO dao = getBaseDAO();
		List<String> pkList = (List<String>) dao.executeQuery(
				"select b.pk_execution_b from fct_execution_b b where b.pk_fct_ap = '"
						+ primaryKey + "' and b.dr = 0",
				new ColumnListProcessor());
		StringBuffer sb = new StringBuffer();
		if (pkList != null && pkList.size() > 0) {
			for (String pk_execution_b : pkList) {
				sb.append("'");
				sb.append(pk_execution_b);
				sb.append("'");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			String condition = " pk_fct_ap='" + primaryKey
					+ "' and dr = 0  and pk_execution_b in (" + sb.toString()
					+ ")";

			Collection<ISuperVO> coll = getBaseDAO().retrieveByClause(
					ExecutionBVO.class, condition);
			if (coll != null && coll.size() > 0) {
				List<ISuperVO> bodyList = new ArrayList<ISuperVO>();
				if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
					for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
						tmpDesBVO.setStatus(VOStatus.NEW);
					}
					bodyList.addAll(Arrays.asList(syncDesPkBVOs));
					for (ISuperVO obj : coll) {
						SuperVO vo = (SuperVO) obj;
						vo.setStatus(VOStatus.DELETED);
						vo.setAttributeValue("dr", 1);
						bodyList.add(vo);
					}
					billvo.setChildren(ExecutionBVO.class,
							bodyList.toArray(new SuperVO[0]));
				}
			}

		}
	}

	public String getPayWay(String payway) throws BusinessException {
		BaseDAO dao = getBaseDAO();
		String pk_balatype = "";
		if (payway != null && !"".equals(payway)) {
			switch (payway) {
			case "10":
				payway = "9";// ABS/ABN
				break;
			case "20":
				payway = "10";// �ֽ�
				break;
			case "30":
				payway = "4";// ��Ʊ
				break;
			case "40":
				payway = "8";// ��¥(���ֽ�)
				break;
			case "50":
				payway = "1";// ����
				break;
			case "60":
				payway = "2";// ����ֱ��
				break;
			case "70":
				payway = "3";// ֧Ʊ
				break;
			case "80":
				payway = "6";// ���л��
				break;
			case "90":
				payway = "7";// ��㵥
				break;
			case "100":
				payway = "5";// ��Ʊ(ֽ��)
				break;
			}
			pk_balatype = (String) dao.executeQuery(
					"select t.pk_balatype from bd_balatype t where t.code = '"
							+ payway + "' and t.dr = 0 and t.enablestate = 2 ",
					new ColumnProcessor());
		}
		return pk_balatype;
	}
}
