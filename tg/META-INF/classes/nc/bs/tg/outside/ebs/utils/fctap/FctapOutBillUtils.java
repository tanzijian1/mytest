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
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApBVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.fct.ap.entity.FctMoretax;
import nc.vo.fct.ap.entity.LeaseconBVO;
import nc.vo.fct.ap.entity.PMPlanBVO;
import nc.vo.fct.ap.entity.SupplyAgreementBVO;
import nc.vo.gateway60.accountbook.GlOrgUtils;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FctapOutBillUtils extends EBSBillUtils {
	static FctapOutBillUtils utils;

	public static FctapOutBillUtils getUtils() {
		if (utils == null) {
			utils = new FctapOutBillUtils();
		}
		return utils;
	}

	/**
	 * �����ͬ����
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

		String vbillcode = headJSON.getString("vbillcode");// ���ݱ��(ebs��ͬ���)
		String pk = headJSON.getString("def49");// ebs��ͷ��ͬ����

		// Ŀ�굥����+���󵥾ݺ�������
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
					"isnull(dr,0)=0 and blatest ='Y'  and pk_fct_ap = '" + pk
							+ "'");
			String hpk = null;
			String def105=null;//�����ڳɱ�̨���С�����ͷdef105��
			String def106=null;//�����Ƿ��߲�������ť�޸����ݱ�־�����ΪY��def105ȡԭֵ��
			if (aggVO != null) {
				hpk = aggVO.getParentVO().getPrimaryKey();
			    def106=aggVO.getParentVO().getDef106();
			    def105=aggVO.getParentVO().getDef105();
			}
			// ����ת�����߶���
			FctapOutConvertor fctapConvertor = new FctapOutConvertor();
			// ���ñ�ͷkey������ӳ��
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("fct_ap", "�����ͬ");
			fctapConvertor.setHVOKeyName(hVOKeyName);

			// ���ñ���key������ӳ��
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("fct_ap_plan", "����ƻ�ҳǩ��ǩԼ��");
			bVOKeyName.put("supplyAgreementBVO", "����Э��");
			bVOKeyName.put("fct_ap_b", "�ɱ����ҳǩ����ͬ������");
			bVOKeyName.put("fct_pmplan", "����ƻ�_��֤��_Ѻ��_�����_�����ʽ�");
			bVOKeyName.put("leaseconBVO", "����Э��");
			bVOKeyName.put("fct_execution", "ִ�����");
			bVOKeyName.put("fct_moretax", "��˰��");
			fctapConvertor.setBVOKeyName(bVOKeyName);

			// ���ñ�ͷ�����ֶ�
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hKeyName = new HashMap<String, String>();
			hKeyName.put("pk_org", "������֯(ebs���˹�˾)");
			// hKeyName.put("subscribedate", "ǩ�ָ�������");
			// hKeyName.put("cvendorid", "��Ӧ��");
			// hKeyName.put("plate", "���");
			hKeyName.put("ebscontractstatus", "ebs��ͬ״̬");
			hKeyName.put("subbudget", "Ԥ������");
			// hKeyName.put("contype", "EBS��ͬ����");
			// hKeyName.put("condetails", "��ͬϸ��");
			hKeyName.put("vbillcode", "��ͬ����");
			hKeyName.put("ctname", "��ͬ����");
			// hKeyName.put("proname", "��Ŀ����");
			// hKeyName.put("pronode", "��Ŀ�ڵ�");
			// hKeyName.put("b_lease", "�Ƿ����޺�ͬ");
			// hKeyName.put("b_payed", "�Ƿ�����Ѹ�����");
			// hKeyName.put("b_stryear", "�Ƿ�Ԥ������ͬ");
			hKeyName.put("accountorg", "���˹�˾");
			// hKeyName.put("first", "�׷�");
			// hKeyName.put("second", "�ҷ�");
			// hKeyName.put("third", "����");
			// hKeyName.put("fourth", "����");
			// hKeyName.put("fifth", "�췽");
			// hKeyName.put("sixth", "����");
			hKeyName.put("vdef19", "��ͬ����(ebs��������)");
			// hKeyName.put("d_sign", "ǩԼ����");
			hKeyName.put("d_creator", "ebs��������");
			// hKeyName.put("personnelid", "���죨�а죩��Ա");
			hKeyName.put("conadmin", "��ͬ������");
			// hKeyName.put("grade", "���죨�а죩ְλ");
			// hKeyName.put("con_abstract", "��ͬժҪ");
			// hKeyName.put("vdef17", "Ѻ��/��֤��/�����ʽ���");
			// hKeyName.put("ntotalorigmny", "ǩԼ���");
			// hKeyName.put("rate", "˰��");
			// hKeyName.put("con_link", "��ͬ�ı�����");
			// hKeyName.put("depid", "���죨�а죩����");
			// hKeyName.put("vdef9", "����Э����");
			// hKeyName.put("vdef11", "��̬���");
			// hKeyName.put("fstatusflag", "��ͬ״̬");
			// hKeyName.put("vdef20", "�ۼ����շ�Ʊ/Ʊ��");
			hKeyName.put("corigcurrencyid", "����");
			hValidatedKeyName.put("fct_ap", hKeyName);
			fctapConvertor.sethValidatedKeyName(hValidatedKeyName);

			// ���ñ�������ֶ�
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			// **********ִ�����ҳǩ�ֶμ���
			/*
			 * Map<String, String> bExecutionBVOKeyName = new HashMap<String,
			 * String>(); bExecutionBVOKeyName.put("pk_ebs", "ebs����");
			 * bExecutionBVOKeyName.put("billno", "���������");
			 * bExecutionBVOKeyName.put("d_apply", "��������");
			 * bExecutionBVOKeyName.put("mapply", "������");
			 * bExecutionBVOKeyName.put("mpay", "�Ѹ����");
			 * bExecutionBVOKeyName.put("paydate", "��������");
			 * bExecutionBVOKeyName.put("minvalid", "���Ͻ��");
			 * bExecutionBVOKeyName.put("d_invalid", "��������");
			 * bExecutionBVOKeyName.put("mreturn", "�˻ؽ��");
			 * bExecutionBVOKeyName.put("d_return", "�˻�����");
			 * bExecutionBVOKeyName.put("munpaid", "��ͬδ�����");
			 * bExecutionBVOKeyName.put("mapplyunpaid", "�����δ��");
			 * bExecutionBVOKeyName.put("def1", "�ۼ������");
			 * bExecutionBVOKeyName.put("def2", "�ۼ��Ѹ���"); //
			 * bExecutionBVOKeyName.put("def3", "�ۼ����շ�Ʊ");
			 * bValidatedKeyName.put("fct_execution", bExecutionBVOKeyName);
			 */
			// **********ִ�����ҳǩ�ֶμ���

			// **********����ƻ�ҳǩ�ֶμ���
			/*
			 * Map<String, String> bCtApPlanVOKeyName = new HashMap<String,
			 * String>(); bCtApPlanVOKeyName.put("d_payplan", "�ƻ���������");
			 * bCtApPlanVOKeyName.put("planrate", "�ƻ��������");
			 * bCtApPlanVOKeyName.put("planmoney", "�ƻ�������");
			 * bCtApPlanVOKeyName.put("pay_condition", "��������");
			 * bCtApPlanVOKeyName.put("paymenttype", "��������");
			 * bCtApPlanVOKeyName.put("def8", "������ʽ");
			 * bCtApPlanVOKeyName.put("def9", "��������");
			 * bCtApPlanVOKeyName.put("def10", "�������");
			 * bCtApPlanVOKeyName.put("d_return", "Ԥ���˻أ��黹��ʱ��"); //
			 * bCtApPlanVOKeyName.put("def4", "ʵ��/�Գ�����"); //
			 * bCtApPlanVOKeyName.put("def5", "�Գ��������");
			 * bCtApPlanVOKeyName.put("msumapply", "�ۼ������");
			 * bCtApPlanVOKeyName.put("msumpayed", "�ۼ��Ѹ�����");
			 * bCtApPlanVOKeyName.put("msumallpayed", "ȫ������ƻ��Ľ��ϼƣ��ϼ��У�");
			 * bCtApPlanVOKeyName.put("compensateno", "�ֳ嵥��");
			 * bCtApPlanVOKeyName.put("mcompensate", "�ֳ���");
			 * bCtApPlanVOKeyName.put("compensatecor", "�ֳ���˹�˾");
			 * bCtApPlanVOKeyName.put("pk_ebs", "ebs����");
			 * bValidatedKeyName.put("fct_ap_plan", bCtApPlanVOKeyName);
			 */
			// **********����ƻ�ҳǩ�ֶμ���

			// **********����ƻ�_��֤��_Ѻ��_�����_�����ʽ�ҳǩ�ֶμ���
			/*
			 * Map<String, String> bPMPlanBVOKeyName = new HashMap<String,
			 * String>(); bPMPlanBVOKeyName.put("d_paymonery", "�ƻ���������");
			 * bPMPlanBVOKeyName.put("pay_proportion", "�������");
			 * bPMPlanBVOKeyName.put("mpay", "�ƻ�������");
			 * bPMPlanBVOKeyName.put("paycondition", "��������"); //
			 * bPMPlanBVOKeyName.put("m_payed", "�Ѹ����");
			 * bPMPlanBVOKeyName.put("paytype", "��������");
			 * bPMPlanBVOKeyName.put("deal", "��������");
			 * bPMPlanBVOKeyName.put("d_return", "Ԥ���˻�/�⸶����");
			 * bPMPlanBVOKeyName.put("conreturn", "�˻�/�⸶����"); //
			 * bPMPlanBVOKeyName.put("b_refund", "�Ƿ��˿�");
			 * bPMPlanBVOKeyName.put("d_refund", "�յ��˿�����");
			 * bPMPlanBVOKeyName.put("operation", "������ʽ");
			 * bPMPlanBVOKeyName.put("mback", "�˿���");
			 * bPMPlanBVOKeyName.put("offsetno", "�ֳ嵥��");
			 * bPMPlanBVOKeyName.put("moffset", "�ֳ���");
			 * bPMPlanBVOKeyName.put("offsetcompany", "�ֳ���˹�˾");
			 * bPMPlanBVOKeyName.put("msumapply", "�ۼ������");
			 * bPMPlanBVOKeyName.put("msumpay", "�ۼ��Ѹ�����");
			 * bPMPlanBVOKeyName.put("msumallpay", "ȫ������ƻ��Ľ��ϼ�(�ϼ���)");
			 * bPMPlanBVOKeyName.put("pk_ebs", "ebs����");
			 * bValidatedKeyName.put("fct_pmplan", bPMPlanBVOKeyName);
			 */
			// **********����ƻ�_��֤��_Ѻ��_�����_�����ʽ�ҳǩ�ֶμ���

			// **********����Э��ҳǩ�ֶμ���
			/*
			 * Map<String, String> bSupplyAgreementBVOKeyName = new
			 * HashMap<String, String>();
			 * bSupplyAgreementBVOKeyName.put("pk_ebs", "ebs����");
			 * bSupplyAgreementBVOKeyName.put("billno", "Э����");
			 * bSupplyAgreementBVOKeyName.put("name", "Э������");
			 * bSupplyAgreementBVOKeyName.put("mmonery", "Э����");
			 * bSupplyAgreementBVOKeyName.put("d_date", "ǩԼ����");
			 * bSupplyAgreementBVOKeyName.put("msupply", "Э�����ۼ�");
			 * bValidatedKeyName.put("supplyAgreementBVO",
			 * bSupplyAgreementBVOKeyName);
			 */
			// **********����Э��ҳǩ�ֶμ���

			// **********���޺�ͬҳǩ�ֶμ���
			/*
			 * Map<String, String> bLeaseconBVOKeyName = new HashMap<String,
			 * String>(); bLeaseconBVOKeyName.put("pk_ebs", "ebs����");
			 * bLeaseconBVOKeyName.put("lease", "������");
			 * bLeaseconBVOKeyName.put("area", "�������");
			 * bLeaseconBVOKeyName.put("d_start", "���޿�ʼ��");
			 * bLeaseconBVOKeyName.put("d_end", "���޽�����");
			 * bLeaseconBVOKeyName.put("b_fixedfunds", "�Ƿ�̶����");
			 * bLeaseconBVOKeyName.put("clauses", "��������(��������������(��%)or��������)");
			 * bValidatedKeyName.put("leaseconBVO", bLeaseconBVOKeyName);
			 */
			// **********���޺�ͬҳǩ�ֶμ���

			// **********��˰��ҳǩ�ֶμ���
			Map<String, String> bFctMoretaxKeyName = new HashMap<String, String>();
			bFctMoretaxKeyName.put("pk_ebs", "ebs����");
			bFctMoretaxKeyName.put("maininvoicetype", "��Ҫ��Ʊ����");
			// bFctMoretaxKeyName.put("taxrate", "˰��");
			bFctMoretaxKeyName.put("intaxmny", "��˰���");
			// bFctMoretaxKeyName.put("taxmny", "˰��");
			// bFctMoretaxKeyName.put("notaxmny", "����˰���");
			bValidatedKeyName.put("fct_moretax", bFctMoretaxKeyName);
			// **********��˰��ҳǩ�ֶμ���

			// **********��ͬ�������ɱ���֣�ҳǩ�ֶμ���
			/*
			 * Map<String, String> bCtApBVOKeyName = new HashMap<String,
			 * String>(); bCtApBVOKeyName.put("pk_ebs", "ebs����");
			 * bCtApBVOKeyName.put("vbdef11", "��Ŀ����");
			 * bCtApBVOKeyName.put("vbdef13", "��ֱ���");
			 * bCtApBVOKeyName.put("vbdef14", "���"); //
			 * bCtApBVOKeyName.put("norigtaxmny", "���");
			 * bCtApBVOKeyName.put("vbdef15", "ҵ̬");
			 * bCtApBVOKeyName.put("vbdef16", "��������");
			 * bCtApBVOKeyName.put("vbdef17", "����/¥��");
			 * bCtApBVOKeyName.put("vbdef18", "�ÿ���");
			 * bCtApBVOKeyName.put("vbdef19", "�Ƿ�Ԥ��");
			 * bCtApBVOKeyName.put("vbdef20", "˵��"); //
			 * bCtApBVOKeyName.put("vbdef12", "Ԥ����");
			 * bCtApBVOKeyName.put("vbdef30", "Ԥ�����");
			 * bValidatedKeyName.put("fct_ap_b", bCtApBVOKeyName);
			 */
			// **********��ͬ�������ɱ���֣�ҳǩ�ֶμ���
			fctapConvertor.setbValidatedKeyName(bValidatedKeyName);

			// ���ò����ֶ�
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("fct_ap-pk_org");
			// refKeys.add("fct_ap-grade"); // ���죨�а죩ְλ ���գ���λ��*
			refKeys.add("fct_ap-first"); // �׷���������֯��*
			refKeys.add("fct_ap-second");// �ҷ�����Ӧ�̣�*
			refKeys.add("fct_ap-third");// ��������Ӧ�̣�*
			refKeys.add("fct_ap-fourth");// ��������Ӧ�̣�*
			refKeys.add("fct_ap-fifth");// �췽����Ӧ�̣�*
			refKeys.add("fct_ap-sixth");// ��������Ӧ�̣�*
			refKeys.add("fct_ap-corigcurrencyid");// ����
			refKeys.add("fct_ap-accountorg");// ���˹�˾(������֯)*
			refKeys.add("fct_ap-subbudget");// Ԥ������(���ղ�����֯)*
			refKeys.add("fct_ap_plan-compensatecor");// �ֳ���˹�˾������ƻ�ǩԼ�� ���ղ�����֯*
			refKeys.add("fct_pmplan-offsetcompany");// �ֳ���˹�˾(����ƻ�Ѻ��) ���ղ�����֯*
			// refKeys.add("fct_ap-cvendorid"); // ��Ӧ��
			// refKeys.add("fct_ap-personnelid");// ���죨�а죩��*
			refKeys.add("fct_ap-conadmin");// ��ͬ������*
			// refKeys.add("fct_ap-depid");// ���죨�а죩����*
			refKeys.add("fct_ap-plate");// ���*
			// refKeys.add("fct_ap-proname");// ��Ŀ����*
			refKeys.add("fct_moretax-maininvoicetype");// ��Ҫ��Ʊ����*
			refKeys.add("fct_pmplan-paytype");// ��������-����ƻ�����֤��Ѻ�𡢳����͹����ʽ�*
			refKeys.add("fct_ap_plan-def2");// ��������-����ƻ���ǩԼ��*
			refKeys.add("fct_ap_b-vbdef11");// ��Ŀ���ƣ�Ԥ���Ŀ��-��ͬ�������ɱ���֣�*
			refKeys.add("fct_ap_b-vbdef15");// ҵ̬-��ͬ�������ɱ���֣�*
			refKeys.add("fct_ap_b-vbdef16");// ��������-��ͬ�������ɱ���֣�*
			refKeys.add("fct_ap_b-vbdef17");// ����/¥��-��ͬ�������ɱ���֣�*
			refKeys.add("fct_ap_b-vbdef18");// �ÿ���-��ͬ�������ɱ���֣�*
			fctapConvertor.setRefKeys(refKeys);

			// ������֯Ĭ����Ϣ
			fctapConvertor.setDefaultGroup("000112100000000005FD");

			AggCtApVO billvo = (AggCtApVO) fctapConvertor.castToBill(value,
					AggCtApVO.class, aggVO);

			// ********************��ͷ������Ϣ����***************
			billvo.getParentVO().setPk_org(
					fctapConvertor.getRefAttributePk("fct_ap-pk_org", billvo
							.getParentVO().getPk_org())); // ������֯
			billvo.getParentVO().setPk_org_v(
					GlOrgUtils.getPkorgVersionByOrgID(billvo.getParentVO()
							.getPk_org()));
			billvo.getParentVO().setAccountorg(
					fctapConvertor.getRefAttributePk("fct_ap-accountorg",
							billvo.getParentVO().getAccountorg())); // ���˹�˾(������֯)
			billvo.getParentVO().setSubbudget(
					fctapConvertor.getRefAttributePk("fct_ap-subbudget", billvo
							.getParentVO().getSubbudget())); // Ԥ������(���ղ�����֯)
			billvo.getParentVO().setPlate(
					fctapConvertor.getRefAttributePk("fct_ap-plate", billvo
							.getParentVO().getPlate())); // ���
			billvo.getParentVO().setProname(billvo.getParentVO().getProname());// ��Ŀ���ƣ���Ŀ��

			billvo.getParentVO().setFirst(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getFirst())); // �׷���������֯��

			billvo.getParentVO().setSecond(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getSecond()));// �ҷ�����Ӧ�̣�
			billvo.getParentVO().setThird(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getThird()));// ��������Ӧ�̣�
			billvo.getParentVO().setFourth(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getFourth()));// ��������Ӧ�̣�
			billvo.getParentVO().setFifth(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getFifth()));// �췽����Ӧ�̣�
			billvo.getParentVO().setSixth(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getSixth()));// ��������Ӧ�̣�
			// ����
			billvo.getParentVO().setCorigcurrencyid(
					fctapConvertor.getRefAttributePk("fct_ap-corigcurrencyid",
							billvo.getParentVO().getCorigcurrencyid()));// ����

			// Map<String, String> psnInfoMap = getPsnInfo(billvo.getParentVO()
			// .getPersonnelid(), billvo.getParentVO().getDepid(), billvo
			// .getParentVO().getGrade());
			// if (psnInfoMap == null) {
			// throw new BusinessException("������["
			// + billvo.getParentVO().getPersonnelid() + "],���첿��["
			// + billvo.getParentVO().getDepid() + "],����ְλ["
			// + billvo.getParentVO().getGrade()
			// + "]δ����NC��Ա���������ϲ�ѯ�������Ϣ");
			// }

			// billvo.getParentVO().setPersonnelid(psnInfoMap.get("pk_psndoc"));
			// // ���죨�а죩��
			// billvo.getParentVO().setDepid(psnInfoMap.get("pk_dept")); //
			// ���죨�а죩����
			// billvo.getParentVO().setGrade(psnInfoMap.get("pk_post")); // ����ְλ
			billvo.getParentVO().setDef58(billvo.getParentVO().getDef58()); // ���죨�а죩��
			billvo.getParentVO().setDef59(billvo.getParentVO().getDef59()); // ���죨�а죩����
			billvo.getParentVO().setDef60(billvo.getParentVO().getDef60()); // ����ְλ
			String def76 = billvo.getParentVO().getDef76();
			if (def76 != null && !"".equals(def76)) {
				billvo.getParentVO().setDef76(def76);// �Ƿ��Զ��ۿ���
				billvo.getParentVO().setDef77(billvo.getParentVO().getDef77());// ǩԼ�˻�
				billvo.getParentVO().setDef78(billvo.getParentVO().getDef78());// ��������
			} else {
				throw new BusinessException("�Ƿ��Զ��ۿ��Ϊ��!");
			}
			billvo.getParentVO().setDef79(billvo.getParentVO().getDef79());// ebs�汾����
			// billvo.getParentVO().setConadmin(
			// fctapConvertor.getRefAttributePk("fct_ap-conadmin", billvo
			// .getParentVO().getConadmin(), billvo.getParentVO()
			// .getDepid())); // ��ͬ������
			// ********************��ͷ������Ϣ����***************

			// ��ͷĬ����Ϣ����
			billvo.getParentVO().setCtrantypeid(
					fctapConvertor.getRefAttributePk("ctrantypeid",
							"FCT1-Cxx-003")); // ��������
			billvo.getParentVO().setCbilltypecode("FCT1"); // ��������
			billvo.getParentVO().setValdate(new UFDate()); // �ƻ���Ч����
			billvo.getParentVO().setInvallidate(new UFDate());// �ƻ���ֹ����
			billvo.getParentVO().setNexchangerate(new UFDouble(100));// �۱�����
			billvo.getParentVO().setSubscribedate(new UFDate());// ����ǩ������
			billvo.getParentVO().setCvendorid(billvo.getParentVO().getSecond());// ��Ӧ�̣�ȡ�ҷ���
			UFDouble ntotalorigmny = (UFDouble) billvo.getParent()
					.getAttributeValue("ntotalorigmny");
			billvo.getParentVO().setMsign(ntotalorigmny);// ǩԼ���
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
					ctApBVO.setAttributeValue("ntaxmny", new UFDouble(1)); // ���Ҽ�˰�ϼ�
					ctApBVORowNo += 10;
               
					// **********�ɱ���֣���ͬ������������ֵ
					ctApBVO.setVbdef11(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef11", ctApBVO.getVbdef11(),
							ctApBVO.getPk_org())); // ��Ŀ���ƣ�Ԥ���Ŀ��
					ctApBVO.setVbdef15(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef15", ctApBVO.getVbdef15())); // ҵ̬
					ctApBVO.setVbdef16(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef16", ctApBVO.getVbdef16())); // ��������
					ctApBVO.setVbdef17(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef17", ctApBVO.getVbdef17())); // ����/¥��
					ctApBVO.setVbdef18(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef18", ctApBVO.getVbdef18())); // �ÿ���
					// **********�ɱ���֣���ͬ������������ֵ
				}
			}
          
			ISuperVO[] leaseconBVOs = billvo.getChildren(LeaseconBVO.class);// ����Э��
			if (leaseconBVOs != null && leaseconBVOs.length > 0) {
				for (ISuperVO tmpLeaseconBVO : leaseconBVOs) {
					LeaseconBVO leaseconBVO = (LeaseconBVO) tmpLeaseconBVO;
					leaseconBVO.setPk_fct_ap(hpk);
				}
			}

			ISuperVO[] supplyAgreementBVOs = billvo
					.getChildren(SupplyAgreementBVO.class);// ����Э��
			if (supplyAgreementBVOs != null && supplyAgreementBVOs.length > 0) {
				for (ISuperVO tmpSupplyAgreementBVO : supplyAgreementBVOs) {
					SupplyAgreementBVO supplyAgreementBVO = (SupplyAgreementBVO) tmpSupplyAgreementBVO;
					supplyAgreementBVO.setPk_fct_ap(hpk);
				}
			}

			UFDouble sumMny = new UFDouble(0); // Ѻ����ϼ�
			ISuperVO[] pMPlanBVOs = billvo.getChildren(PMPlanBVO.class);// ����ƻ�_��֤��_Ѻ��_�����_�����ʽ�
			if (pMPlanBVOs != null && pMPlanBVOs.length > 0) {
				for (ISuperVO tmpPMPlanBVO : pMPlanBVOs) {
					PMPlanBVO pMPlanBVO = (PMPlanBVO) tmpPMPlanBVO;
					pMPlanBVO.setPk_fct_ap(hpk);
					// �ϼ�Ѻ����
					UFDouble mpay = pMPlanBVO.getMpay();
					if (mpay != null) {
						sumMny = sumMny.add(mpay);
					}

					// **********����ƻ�_��֤��_Ѻ��_�����_�����ʽ������ֵ
					pMPlanBVO.setOffsetcompany(fctapConvertor
							.getRefAttributePk("fct_pmplan-offsetcompany",
									pMPlanBVO.getOffsetcompany())); // �ֳ���˹�˾(����ƻ�Ѻ��)
																	// ���ղ�����֯*
					pMPlanBVO.setPaytype(fctapConvertor.getRefAttributePk(
							"fct_pmplan-paytype", pMPlanBVO.getPaytype())); // ��������-����ƻ�����֤��Ѻ�𡢳����͹����ʽ�
					// **********����ƻ�_��֤��_Ѻ��_�����_�����ʽ������ֵ
				}
			}

			// ����(����ƻ�_��֤��_Ѻ��_�����_�����ʽ�)������ϼƲ����ڱ�ͷѺ����ϼƣ�����
			if (sumMny.toDouble() != Double.parseDouble(billvo.getParentVO()
					.getVdef17())) {
				throw new BusinessException(
						"���塾����ƻ�_��֤��_Ѻ��_�����_�����ʽ𡿸�����ϼƲ����ڱ�ͷѺ����ϼ�");
			}

			ISuperVO[] ctApPlanVOs = billvo.getChildren(CtApPlanVO.class);// ����ƻ�ҳǩ��ǩԼ��
			if (ctApPlanVOs != null && ctApPlanVOs.length > 0) {
				for (ISuperVO tmpCtApPlanVO : ctApPlanVOs) {
					CtApPlanVO ctApPlanVO = (CtApPlanVO) tmpCtApPlanVO;
					ctApPlanVO.setPk_fct_ap(hpk);
					ctApPlanVO.setPk_org(billvo.getParentVO().getPk_org());
					ctApPlanVO.setPk_group(billvo.getParentVO().getPk_group());
					// **********����ƻ�ҳǩ��ǩԼ��������ֵ
					ctApPlanVO.setCompensatecor(fctapConvertor
							.getRefAttributePk("fct_ap_plan-compensatecor",
									ctApPlanVO.getCompensatecor())); // �ֳ���˹�˾������ƻ�ǩԼ��
																		// ���ղ�����֯
					ctApPlanVO.setPaymenttype(fctapConvertor.getRefAttributePk(
							"fct_ap_plan-paymenttype",
							ctApPlanVO.getPaymenttype())); // ��������-����ƻ���ǩԼ��*
					// **********����ƻ�ҳǩ��ǩԼ��������ֵ
					// ���ÿ�������def2
					ctApPlanVO.setDef2(fctapConvertor.getRefAttributePk(
							"fct_ap_plan-def2", ctApPlanVO.getDef2()));
				}
			}

			ISuperVO[] executionBVOs = billvo.getChildren(ExecutionBVO.class);// ִ�����
			if (executionBVOs != null && executionBVOs.length > 0) {
				for (ISuperVO tmpExecutionBVO : executionBVOs) {
					ExecutionBVO executionBVO = (ExecutionBVO) tmpExecutionBVO;
					executionBVO.setPk_fct_ap(hpk);
				}
			}

			ISuperVO[] fctMoretaxs = billvo.getChildren(FctMoretax.class);// ��˰��
			if (fctMoretaxs != null && fctMoretaxs.length > 0) {
				for (ISuperVO tmpFctMoretax : fctMoretaxs) {
					FctMoretax fctMoretax = (FctMoretax) tmpFctMoretax;
					fctMoretax.setPk_fct_ap(hpk);
					// **********����ƻ�ҳǩ��ǩԼ��������ֵ
					fctMoretax.setMaininvoicetype(fctapConvertor
							.getRefAttributePk("fct_moretax-maininvoicetype",
									fctMoretax.getMaininvoicetype())); // ��Ҫ��Ʊ����
					// **********����ƻ�ҳǩ��ǩԼ��������ֵ
				}
			}
			HashMap<String, Object> eParam = new HashMap<String, Object>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			AggCtApVO billVO = null;
			if (aggVO == null) {
				// �����ڣ���������

				billvo.getParentVO().setCreator(getSaleUserID());
				billvo.getParentVO().setBillmaker(getSaleUserID());
				billvo.getParentVO().setCreationtime(new UFDateTime());
				billvo.getParentVO().setDmakedate(new UFDate());
				billvo.getParentVO().setFstatusflag(0);
				billvo.getParentVO().setVersion(UFDouble.ONE_DBL);
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
				// ���ڸ��µ���
                 //TODO 20200814-C
				if("Y".equals(def106)){//���鿴����ע��
				 billvo.getParentVO().setDef105(def105);
				}
				//end
				syncBvoPkByEbsPk(CtApPlanVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_ap_plan",
						"fct_ap_plan"); // ͬ������ƻ���ǩԼ��PK
				syncBvoPkByEbsPk(PMPlanBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_pmplan_b",
						"fct_pmplan_b");// ͬ������ƻ�_��֤��_Ѻ��_�����_�����ʽ�PK
				syncBvoPkByEbsPk(CtApBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_ap_b", "fct_ap_b");// ͬ���ɱ����ҳǩ����ͬ������PK
				syncBvoPkByEbsPk(SupplyAgreementBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_supply_agreement_b",
						"fct_supply_agreement_b");// ͬ������Э��PK
//				syncBvoPkByEbsPk(ExecutionBVO.class, aggVO.getParentVO()
//						.getPrimaryKey(), billvo, "pk_execution_b",
//						"fct_execution_b");// ͬ��ִ�����PK
				syncBvoPkByEbsPk(FctMoretax.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_moretaxrate",
						"fct_moretax");// ͬ����˰��PK
				syncBvoPkByEbsPk(LeaseconBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_leasecon_b",
						"fct_leasecon_b");// ͬ������Э�飨��ͬ��PK

				billvo.getParentVO().setModifier(getSaleUserID());
				billvo.getParentVO().setModifiedtime(new UFDateTime());

				// syncBvoTs(CtApPlanVO.class, aggVO, billvo); //
				// ����ƻ�ҳǩ��ǩԼ��ҳǩts
				// syncBvoTs(PMPlanBVO.class, aggVO, billvo); //
				// ͬ������ƻ�_��֤��_Ѻ��_�����_�����ʽ�ҳǩts
				// syncBvoTs(CtApBVO.class, aggVO, billvo); //
				// ͬ���ɱ����ҳǩ����ͬ������ҳǩts
				// syncBvoTs(SupplyAgreementBVO.class, aggVO, billvo); //
				// ͬ������Э��ҳǩts
				// syncBvoTs(ExecutionBVO.class, aggVO, billvo); // ͬ��ִ�����ҳǩts
				// syncBvoTs(FctMoretax.class, aggVO, billvo); // ͬ����˰��ҳǩts
				// syncBvoTs(LeaseconBVO.class, aggVO, billvo); //
				// ͬ������Э�飨��ͬ��ҳǩts

				billvo.getParentVO().setTs(aggVO.getParentVO().getTs()); // ͬ����ͷts
				billvo.getParentVO().setStatus(VOStatus.UPDATED);
				billvo.getParentVO().setPrimaryKey(hpk);
				billvo.getParentVO().setModifier(getSaleUserID());
				billvo.getParentVO().setModifiedtime(new UFDateTime());

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

	/**
	 * <p>
	 * Title: syncBvoTs<��p>
	 * <p>
	 * Description: �޸ĺ�ͬʱͬ����ͷ����VO��ts<��p>
	 * 
	 * @param clazz
	 *            VOclass
	 * @param srcAggVO
	 *            ���ݿ��VO
	 * @param desAggVO
	 *            ��ֵ��VO
	 */
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
	 * // ***********������ƻ�_��֤��_Ѻ��_�����_�����ʽ��� // �������� billIdNoMap = new
	 * HashMap<String, Object>(); bidList = new ArrayList<String>(); ISuperVO[]
	 * pMPlanBVOs = aggVO.getChildren(PMPlanBVO.class);// ����ƻ�_��֤��_Ѻ��_�����_�����ʽ�
	 * for(ISuperVO tmpPMPlanBVO : pMPlanBVOs){ PMPlanBVO pMPlanBVO =
	 * (PMPlanBVO) tmpPMPlanBVO; bidList.add(pMPlanBVO.getPrimaryKey()); }
	 * billIdNoMap.put("billid", bidList); voIdNoMap.put("fct_pmplan",
	 * billIdNoMap); // ***********������ƻ�_��֤��_Ѻ��_�����_�����ʽ���
	 * 
	 * // ***********������Э�鱨�� // �������� billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] leaseconBVOs =
	 * aggVO.getChildren(LeaseconBVO.class);// ����Э�� for(ISuperVO tmpLeaseconBVO
	 * : leaseconBVOs){ LeaseconBVO leaseconBVO = (LeaseconBVO) tmpLeaseconBVO;
	 * bidList.add(leaseconBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("leaseconBVO", billIdNoMap); //
	 * ***********������Э�鱨��
	 * 
	 * // ***********��ִ��������� // �������� billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] executionBVOs =
	 * aggVO.getChildren(ExecutionBVO.class);// ִ����� for(ISuperVO
	 * tmpExecutionBVO : executionBVOs){ ExecutionBVO executionBVO =
	 * (ExecutionBVO) tmpExecutionBVO;
	 * bidList.add(executionBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_execution", billIdNoMap); //
	 * ***********��ִ���������
	 * 
	 * // ***********����˰�ʱ��� // �������� billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] fctMoretaxs =
	 * aggVO.getChildren(FctMoretax.class);// ��˰�� for(ISuperVO tmpFctMoretax :
	 * fctMoretaxs){ FctMoretax fctMoretax = (FctMoretax) tmpFctMoretax;
	 * bidList.add(fctMoretax.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_moretax", billIdNoMap); //
	 * ***********����˰�ʱ��� return voIdNoMap; }
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
	private Map<String, String> getBvoPkByEbsMap(String parentPKfield,
			String bvoPKfiled, String table, String parentPkValue) {
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = getBaseDAO();
		sql = "select pk_ebs," + bvoPKfiled + " pk from " + table + " where  "
				+ parentPKfield + " = ? and dr =0";
		Map<String, String> info = new HashMap<String, String>();
		try {
			parameter.addParam(parentPkValue);
			List<Map<String, String>> list = (List<Map<String, String>>) dao
					.executeQuery(sql, parameter, new MapListProcessor());
			if (list != null && list.size() > 0) {
				for (Map<String, String> map : list) {
					info.put(map.get("pk_ebs"), map.get("pk"));
				}
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return info;
	}

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
		Map<String, String> info = getBvoPkByEbsMap("pk_fct_ap", bvoPKfiled,
				table, parentPKValue);
		Map<String, UFDateTime> tsinfo = getBvoTsByEbsMap("pk_fct_ap",
				bvoPKfiled, table, parentPKValue);
		List<String> list = new ArrayList<String>();
		if (info.size() > 0) {
			list.addAll(info.values());
		}
		if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
			for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
				// ������������ebsPK
				String desEBSPk = (String) tmpDesBVO
						.getAttributeValue("pk_ebs");
				// ������������ebsPKΪ�����������ı������ݣ������Ǹ��µı�������
				if (desEBSPk != null && !"~".equals(desEBSPk)) {
					String pk = info.get(desEBSPk);
					if (pk != null && !"".equals(pk)) {
						tmpDesBVO.setAttributeValue(bvoPKfiled, pk);
						tmpDesBVO.setAttributeValue("ts", tsinfo.get(pk));
						tmpDesBVO.setStatus(VOStatus.UPDATED);
						list.remove(pk);
					} else {
						tmpDesBVO.setStatus(VOStatus.NEW);
					}

				}

			}
		}

		// ɾ��ԭ��������
		if (list != null && list.size() > 0) {
			String condition = " pk_fct_ap='" + parentPKValue
					+ "' and dr = 0   ";
			String sqlwhere = "";
			for (String value : list) {
				sqlwhere += "'" + value + "',";
			}
			sqlwhere = sqlwhere.substring(0, sqlwhere.length() - 1);
			condition += " and " + bvoPKfiled + "  in(" + sqlwhere + ")";
			Collection<ISuperVO> coll = getBaseDAO().retrieveByClause(clazz,
					condition);
			if (coll != null && coll.size() > 0) {
				List<ISuperVO> bodyList = new ArrayList<ISuperVO>();
				if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
					bodyList.addAll(Arrays.asList(syncDesPkBVOs));
				}
				for (ISuperVO obj : coll) {
					SuperVO vo = (SuperVO) obj;
					vo.setStatus(VOStatus.DELETED);
					vo.setAttributeValue("dr", 1);
					bodyList.add(vo);
				}
				desAggVO.setChildren(clazz, bodyList.toArray(new SuperVO[0]));
			}

		}
	}

	private Map<String, UFDateTime> getBvoTsByEbsMap(String parentPKfield,
			String bvoPKfiled, String table, String parentPkValue) {
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = getBaseDAO();
		sql = "select " + bvoPKfiled + " pk ,ts  from " + table + " where  "
				+ parentPKfield + " = ? and dr =0";
		Map<String, UFDateTime> info = new HashMap<String, UFDateTime>();
		try {
			parameter.addParam(parentPkValue);
			List<Map<String, Object>> list = (List<Map<String, Object>>) dao
					.executeQuery(sql, parameter, new MapListProcessor());
			if (list != null && list.size() > 0) {
				for (Map<String, Object> map : list) {
					info.put((String) map.get("pk"), new UFDateTime(
							(String) map.get("ts")));
				}
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return info;
	}
}
