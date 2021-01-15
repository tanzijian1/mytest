package nc.bs.tg.outside.ebs.utils.fctap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApBVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.fct.ap.entity.LeaseconBVO;
import nc.vo.fct.ap.entity.PMPlanBVO;
import nc.vo.fct.ap.entity.SupplyAgreementBVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FctapBillUtils extends EBSBillUtils {
	static FctapBillUtils utils;

	public static FctapBillUtils getUtils() {
		if (utils == null) {
			utils = new FctapBillUtils();
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
		//��ͷ����
		JSONObject headJSON = (JSONObject)value.get("headInfo");
		
		String vbillcode = headJSON.getString("vbillcode");// ���ݱ��(ebs��ͬ���)
		String pk = headJSON.getString("pk_fct_ap");// ��������
		
		//Ŀ�굥����+���󵥾ݺ�������
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":"
				+ vbillcode;
		//Ŀ�굥����+���󵥾�pk����־���
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + pk;
		
		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		AggCtApVO aggVO = null;
		//���ر���
		HashMap<String, String> dataMap = null;
		try {
			//���NC�Ƿ�����Ӧ�ĵ��ݴ���
			aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
					"isnull(dr,0)=0 and pk_fct_ap = '" + pk + "'");
			//����ת�����߶���
			FctapConvertor fctapConvertor = new FctapConvertor();
			//���ñ�ͷkey������ӳ��
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("fct_ap", "�����ͬ");
			fctapConvertor.setHVOKeyName(hVOKeyName);
			
			//���ñ���key������ӳ��
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("fct_ap_plan", "����ƻ�ҳǩ��ǩԼ��");
			bVOKeyName.put("supplyAgreementBVO", "����Э��");
			bVOKeyName.put("fct_ap_b", "�ɱ����ҳǩ����ͬ������");
			bVOKeyName.put("fct_pmplan", "����ƻ�_��֤��_Ѻ��_�����_�����ʽ�");
			bVOKeyName.put("leaseconBVO", "����Э��");
			bVOKeyName.put("fct_execution", "ִ�����");
			fctapConvertor.setBVOKeyName(bVOKeyName);
			
			// ���ñ�ͷ�����ֶ�
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hKeyName = new HashMap<String, String>();
			hKeyName.put("pk_org", "������֯(ebs���˹�˾)");
			hKeyName.put("subscribedate", "ǩ�ָ�������");
			hKeyName.put("cvendorid", "��Ӧ��");
			hKeyName.put("plate", "���");
			hKeyName.put("subbudget", "Ԥ������");
			hKeyName.put("contype", "EBS��ͬ����");
			hKeyName.put("condetails", "��ͬϸ��");
			hKeyName.put("vbillcode", "��ͬ����");
			hKeyName.put("ctname", "��ͬ����");
			hKeyName.put("proname", "��Ŀ����");
			hKeyName.put("pronode", "��Ŀ�ڵ�");
			hKeyName.put("b_lease", "�Ƿ����޺�ͬ");
			hKeyName.put("b_payed", "�Ƿ�����Ѹ�����");
			hKeyName.put("b_stryear", "�Ƿ�Ԥ������ͬ");
			hKeyName.put("accountorg", "���˹�˾");
			hKeyName.put("first", "�׷�");
			hKeyName.put("second", "�ҷ�");
			hKeyName.put("third", "����");
			hKeyName.put("fourth", "����");
			hKeyName.put("fifth", "�췽");
			hKeyName.put("sixth", "����");
			hKeyName.put("vdef19", "��ͬ����(ebs��������)");
			hKeyName.put("d_sign", "ǩԼ����");
			hKeyName.put("d_creator", "ebs��������");
			hKeyName.put("personnelid", "�а���Ա");
			hKeyName.put("conadmin", "��ͬ������");
			hKeyName.put("grade", "����ְλ");
			hKeyName.put("con_abstract", "��ͬժҪ");
			hKeyName.put("vdef17", "Ѻ����");
			hKeyName.put("m_sign", "ǩԼ���");
			hKeyName.put("rate", "˰��");
			hKeyName.put("con_link", "��ͬ�ı�����");
			hKeyName.put("depid", "�а첿��");
			hKeyName.put("vdef9", "����Э����");
			hKeyName.put("vdef11", "��̬���");
			hKeyName.put("fstatusflag", "��ͬ״̬");
			
			hValidatedKeyName.put("fct_ap", hKeyName);
			fctapConvertor.sethValidatedKeyName(hValidatedKeyName);
			
			// ���ñ�������ֶ�
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			//**********ִ�����ҳǩ�ֶμ���
			Map<String, String> bExecutionBVOKeyName = new HashMap<String, String>();
			bExecutionBVOKeyName.put("billno", "���������");
			bExecutionBVOKeyName.put("d_apply", "��������");
			bExecutionBVOKeyName.put("m_apply", "������");
			bExecutionBVOKeyName.put("m_pay", "����֧�����");
			bExecutionBVOKeyName.put("m_return", "�˻ؽ��");
			bExecutionBVOKeyName.put("d_return", "�˻�����");
			bExecutionBVOKeyName.put("m_unpaid", "��ͬδ�����");
			bExecutionBVOKeyName.put("m_apply_unpaid", "�����δ��");
			bExecutionBVOKeyName.put("def1", "�ۼ������");
			bExecutionBVOKeyName.put("def2", "�ۼ��Ѹ���");
			bExecutionBVOKeyName.put("def3", "�ۼ����շ�Ʊ");
			bValidatedKeyName.put("fct_execution", bExecutionBVOKeyName);
			//**********ִ�����ҳǩ�ֶμ���
			
			//**********����ƻ�ҳǩ�ֶμ���
			Map<String, String> bCtApPlanVOKeyName = new HashMap<String, String>();
			bCtApPlanVOKeyName.put("d_payplan", "�ƻ���������");
			bCtApPlanVOKeyName.put("planrate", "�������");
			bCtApPlanVOKeyName.put("planmoney", "������");
			bCtApPlanVOKeyName.put("pay_condition", "��������");
			bCtApPlanVOKeyName.put("paymenttype", "��������");
			bCtApPlanVOKeyName.put("def8", "������ʽ");
			bCtApPlanVOKeyName.put("def9", "��������");
			bCtApPlanVOKeyName.put("def10", "�������");
			bCtApPlanVOKeyName.put("d_return", "Ԥ���˻�ʱ��");
			bCtApPlanVOKeyName.put("def4", "ʵ��/�Գ�����");
			bCtApPlanVOKeyName.put("def5", "�Գ��������");
			bCtApPlanVOKeyName.put("def6", "�ۼ������");
			bCtApPlanVOKeyName.put("def7", "�ۼƸ�����");
			bValidatedKeyName.put("ctApPlanVO", bCtApPlanVOKeyName);
			//**********����ƻ�ҳǩ�ֶμ���
			
			//**********����ƻ�_��֤��_Ѻ��_�����_�����ʽ�ҳǩ�ֶμ���
			Map<String, String> bPMPlanBVOKeyName = new HashMap<String, String>();
			bPMPlanBVOKeyName.put("d_paymonery", "�ƻ���������");
			bPMPlanBVOKeyName.put("pay_proportion", "�������");
			bPMPlanBVOKeyName.put("m_offset", "�ֳ���");
			bPMPlanBVOKeyName.put("m_pay", "������");
			bPMPlanBVOKeyName.put("paycondition", "��������");
			bPMPlanBVOKeyName.put("m_payed", "�Ѹ����");
			bPMPlanBVOKeyName.put("paytype", "��������");
			bPMPlanBVOKeyName.put("deal", "��������");
			bPMPlanBVOKeyName.put("d_return", "Ԥ���˻�����");
			bPMPlanBVOKeyName.put("conreturn", "�˻�����");
			bPMPlanBVOKeyName.put("b_refund", "�Ƿ��˿�");
			bPMPlanBVOKeyName.put("d_refund", "�յ��˿�����");
			bPMPlanBVOKeyName.put("operation", "������ʽ");
			bValidatedKeyName.put("fct_pmplan", bPMPlanBVOKeyName);
			//**********����ƻ�_��֤��_Ѻ��_�����_�����ʽ�ҳǩ�ֶμ���
			
			//**********����Э��ҳǩ�ֶμ���
			Map<String, String> bSupplyAgreementBVOKeyName = new HashMap<String, String>();
			bSupplyAgreementBVOKeyName.put("billno", "Э����");
			bSupplyAgreementBVOKeyName.put("name", "Э������");
			bSupplyAgreementBVOKeyName.put("m_monery", "Э����");
			bSupplyAgreementBVOKeyName.put("d_date", "ǩԼ����");
			bSupplyAgreementBVOKeyName.put("m_supply", "Э�����ۼ�");
			bValidatedKeyName.put("supplyAgreementBVO", bSupplyAgreementBVOKeyName);
			//**********����Э��ҳǩ�ֶμ���
			
			//**********���޺�ͬҳǩ�ֶμ���
			Map<String, String> bLeaseconBVOKeyName = new HashMap<String, String>();
			bLeaseconBVOKeyName.put("lease", "������");
			bLeaseconBVOKeyName.put("area", "�������");
			bLeaseconBVOKeyName.put("d_start", "���޿�ʼ��");
			bLeaseconBVOKeyName.put("d_end", "���޽�����");
			bLeaseconBVOKeyName.put("b_fixedfunds", "�Ƿ�̶����");
			bLeaseconBVOKeyName.put("clauses", "��������(��������������(��%)or��������)");
			bValidatedKeyName.put("leaseconBVO", bLeaseconBVOKeyName);
			//**********���޺�ͬҳǩ�ֶμ���
			
			//**********��ͬ�������ɱ���֣�ҳǩ�ֶμ���
			Map<String, String> bCtApBVOKeyName = new HashMap<String, String>();
			bCtApBVOKeyName.put("vbdef11", "��Ŀ����");
			bCtApBVOKeyName.put("vbdef13", "��ֱ���");
			bCtApBVOKeyName.put("vbdef14", "���");
			bCtApBVOKeyName.put("vbdef15", "ҵ̬");
			bCtApBVOKeyName.put("vbdef16", "��������");
			bCtApBVOKeyName.put("vbdef17", "����/¥��");
			bCtApBVOKeyName.put("vbdef18", "�ÿ���");
			bCtApBVOKeyName.put("vbdef19", "�Ƿ�Ԥ��");
			bCtApBVOKeyName.put("vbdef20", "˵��");
			bCtApBVOKeyName.put("vbdef12", "Ԥ����");
			bCtApBVOKeyName.put("vbdef30", "Ԥ�����");
			bValidatedKeyName.put("fct_ap_b", bCtApBVOKeyName);
			//**********��ͬ�������ɱ���֣�ҳǩ�ֶμ���
			fctapConvertor.setbValidatedKeyName(bValidatedKeyName);
			
			// ���ò����ֶ�
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("pk_org");
			refKeys.add("cvendorid");
			refKeys.add("personnelid");
			refKeys.add("depid");
			fctapConvertor.setRefKeys(refKeys);
			
			// ������֯Ĭ����Ϣ
			fctapConvertor.setDefaultGroup("000112100000000005FD");
			
			AggCtApVO billvo = (AggCtApVO) fctapConvertor.castToBill(value, AggCtApVO.class);
			
			// ��ͷĬ����Ϣ����
			billvo.getParentVO().setAttributeValue("ctrantypeid", "FCT1-01"); // ��������
			billvo.getParentVO().setAttributeValue("cbilltypecode", "FCT1"); // ��������
			billvo.getParentVO().setAttributeValue("valdate", new UFDate()); // �ƻ���Ч����
			billvo.getParentVO().setAttributeValue("invallidate", new UFDate());// �ƻ���ֹ����
			billvo.getParentVO().setAttributeValue("nexchangerate", new UFDouble());// �۱�����
			
			// ����Ĭ����Ϣ����
			
			ISuperVO[] ctApBVOs =  billvo.getChildren(CtApBVO.class);// �ɱ����ҳǩ����ͬ������
			for(ISuperVO ctApBVO : ctApBVOs){
				ctApBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
				ctApBVO.setAttributeValue("pk_org", billvo.getParentVO().getPk_org());
				ctApBVO.setAttributeValue("pk_org_v", billvo.getParentVO().getPk_org());
				ctApBVO.setAttributeValue("pk_group", billvo.getParentVO().getPk_group());
				ctApBVO.setAttributeValue("ftaxtypeflag", 1);
			}
			
			ISuperVO[] leaseconBVOs =  billvo.getChildren(LeaseconBVO.class);// ����Э��
			for(ISuperVO leaseconBVO : leaseconBVOs){
				leaseconBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
			}
			
			ISuperVO[] supplyAgreementBVOs =  billvo.getChildren(SupplyAgreementBVO.class);// ����Э��
			for(ISuperVO supplyAgreementBVO : supplyAgreementBVOs){
				supplyAgreementBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
			}
			
			ISuperVO[] pMPlanBVOs =  billvo.getChildren(PMPlanBVO.class);// ����ƻ�_��֤��_Ѻ��_�����_�����ʽ�
			for(ISuperVO pMPlanBVO : pMPlanBVOs){
				pMPlanBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
			}
			
			ISuperVO[] ctApPlanVOs =  billvo.getChildren(CtApPlanVO.class);// ����ƻ�ҳǩ��ǩԼ��
			for(ISuperVO ctApPlanVO : ctApPlanVOs){
				ctApPlanVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
				ctApPlanVO.setAttributeValue("pk_org", billvo.getParentVO().getPk_org());
				ctApPlanVO.setAttributeValue("pk_group", billvo.getParentVO().getPk_group());
			}
			
			ISuperVO[] executionBVOs =  billvo.getChildren(ExecutionBVO.class);// ִ�����
			for(ISuperVO executionBVO : executionBVOs){
				executionBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
			}
			
			HashMap<String, String> eParam = new HashMap<String, String>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			
			AggCtApVO billVO = null;
			if(aggVO == null){
				//�����ڣ���������
				billVO = ((AggCtApVO[]) getPfBusiAction().processAction("SAVEBASE",
						"FCT1", null, billvo, null, eParam))[0];
			}else{
				//���ڸ��µ���
				billVO = ((AggCtApVO[]) getPfBusiAction().processAction("SAVEBASE",
						"FCT1", null, billvo, null, eParam))[0];
			}
			
			dataMap = new HashMap<String, String>();
			dataMap.put("billid", billVO.getPrimaryKey());
			dataMap.put("billno", (String) billVO.getParentVO()
					.getAttributeValue("vbillcode"));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(dataMap);

	}
}
