package nc.bs.tg.outside.salebpm.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.bpm.IRecieveBpmMsg;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.itf.tg.outside.ISyncIMGBillServcie;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceService;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKHeaderVO;
import nc.vo.erm.common.MessageVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BpmBXBillStatesUtils extends SaleBPMBillUtils {
	static BpmBXBillStatesUtils utils;

	public static BpmBXBillStatesUtils getUtils() {
		if (utils == null) {
			utils = new BpmBXBillStatesUtils();
		}
		return utils;
	}

	Map<String, String> acttionMap = null;

	public String onSyncBillState(String billtypename, String json)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		JSONObject jobj = JSON.parseObject(json);
		valid(jobj);
		String operator = jobj.getString("operator");
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
		String bpmid = jobj.getString("bpmid");
		String billqueue = ISaleBPMBillCont.getBillNameMap().get(billtypename)
				+ ":" + bpmid;
		String action = jobj.getString("billstate");// ��¼;?
													// ö�٣�APPROVE:���;UNAPPROVE:����;UNSAVE:����
		BXVO aggVO = getBillVO(BXVO.class,
				"nvl(dr,0)=0 and zyx30='" + jobj.getString("bpmid") + "'");
		// zyx30 bpmid
		try {
			if (aggVO == null)
				throw new BusinessException("NCϵͳbpm����δ�ж�Ӧ����");
			//TODO20201027(����״̬Ϊ1��2ֱ�ӳɹ�����)
			if(aggVO.getParentVO().getSpzt()==1||aggVO.getParentVO().getSpzt()==2){
				return "��" + billqueue + "��,"+"�������";
			}
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			HashMap paramMap = new HashMap();
			paramMap.put("flowdefpk", aggVO.getParentVO().getPrimaryKey());
			WorkflownoteVO worknoteVO = null;
			nc.vo.pubapp.pflow.PfUserObject userobjec = new nc.vo.pubapp.pflow.PfUserObject();
			userobjec.setBusinessCheckMap(new HashMap());
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			String maker = (String) aggVO.getParentVO().getAttributeValue(
					"creator");
			if ("APPROVE".equals(action)) {
				Map<String, String> map = (Map<String, String>) jobj
						.get("data");
				worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.SAVE,
						"264X", aggVO, null);
				BXVO newvo = aggVO;
				if (aggVO.getParentVO().getSpzt() == -1)
					throw new BusinessException("��ǰNC��������״̬�쳣�����飡");
				/**
				 * �Ȱ���Ϣ���ȼ������ݸ��µ����ݿ��ٲ��³���2020-04-14-̸�ӽ�-start
				 */
				IMDPersistenceService md = NCLocator.getInstance().lookup(
						IMDPersistenceService.class);
				newvo.getParentVO().setZyx46(map.get("billpriority"));// �����̶�
				md.updateBillWithAttrs(new Object[] { newvo },
						new String[] { "zyx46" });
				newvo = getBillVO(BXVO.class,
						"nvl(dr,0)=0 and zyx30='" + jobj.getString("bpmid")
								+ "'");
				worknoteVO = iWorkflowMachine.checkWorkFlow(
						IPFActionName.APPROVE, "264X", newvo, null);
				worknoteVO.setChecknote("��׼");
				worknoteVO.setApproveresult("Y");
				MessageVO[] messagevos1 = (MessageVO[]) getPfBusiAction()
						.processAction(IPFActionName.APPROVE, "264X",
								worknoteVO, newvo, null, null);

				if (messagevos1[0].getSuccessVO() != null) {
					BXVO newbxvo = approveUpdateVO(
							(BXVO) messagevos1[0].getSuccessVO(), map,
							billtypename);
					md.updateBillWithAttrs(
							new Object[] { newbxvo.getParentVO() },
							new String[] { "zyx12", "jsfs", "zyx15", "zyx18",
									"zyx17", "zyx14", JKHeaderVO.ZYX52,
									JKHeaderVO.ZYX53, JKHeaderVO.ZYX54,
									JKHeaderVO.CUSTACCOUNT });
					IRecieveBpmMsg msg = NCLocator.getInstance().lookup(
							IRecieveBpmMsg.class);
					msg.dealBalatypeMsg(newbxvo);
					getinvoiceByImg(newbxvo.getParentVO().getZyx16());
				}
			} else if ("UNAPPROVE".equals(action)) {// BPM�˻ز����������taskid��Ӱ�����
				BXVO newvo = aggVO;
				if (aggVO.getParentVO().getSpzt() != 3)
					throw new BusinessException("��ǰNC��������״̬�쳣�����飡");
				aggVO.getParentVO().setIsInterface("UNAPPROVE");// �Ƿ�ӿ�
				aggVO.getParentVO().setZyx29("N");// �Ƿ���BPM����
				getPfBusiAction().processAction(IPFActionName.UNSAVE + maker,
						"264X", worknoteVO, newvo, null, null);
			} else if ("UNSAVE".equals(action)) {// BPM�ܾ����������taskid��Ӱ�����
				String approve = aggVO.getParentVO().getApprover();
				MessageVO[] messagevos = null;
				BXVO newvo = aggVO;
				if (aggVO.getParentVO().getSpzt() != 3)
					throw new BusinessException("��ǰNC��������״̬�쳣�����飡");
				if (messagevos != null)
					newvo = (BXVO) messagevos[0].getSuccessVO();
				if (newvo != null) {
					// newvo.getParentVO().setZyx30(null);
					// newvo.getParentVO().setZyx29("N");
					newvo.getParentVO().setIsInterface("UNSAVE");// �Ƿ�ӿ�
				}
				getPfBusiAction().processAction(IPFActionName.UNSAVE + maker,
						"264X", worknoteVO, newvo, null, eParam);

			}
		} catch (Exception e) {
			throw new BusinessException(
					"��" + billqueue + "��," + e.getMessage(), e);
		} finally {
			SaleBPMBillUtils.removeBillQueue(billqueue);
		}
		return "��" + billqueue + "��," + getActtionMap().get(action) + "�������!";
	}

	/**
	 * ���̲���MAP
	 * 
	 * @return
	 */
	public Map<String, String> getActtionMap() {
		if (acttionMap == null) {
			acttionMap = new HashMap<String, String>();
			acttionMap.put(IPFActionName.SAVE, "�ύ");
			acttionMap.put(IPFActionName.APPROVE, "����");
			acttionMap.put(IPFActionName.UNAPPROVE, "����");
			acttionMap.put(IPFActionName.UNSAVE, "����");

		}
		return acttionMap;
	}

	public BXVO approveUpdateVO(BXVO aggvo, Map<String, String> map,
			String billtypename) throws DAOException {
		// �ѿط������-11:��ͬ�������-�ز�;12:��ͬ�������-��ҵ;13:��ͬ�������-��ҵ;
		String[] billtypenames = new String[] { "11", "12", "13" };
		List<String> list = Arrays.asList(billtypenames);
		JKBXHeaderVO hvo = aggvo.getParentVO();
		hvo.setZyx12(map.get("ispayalterinv"));// �Ƿ��ȸ����Ʊ
		hvo.setJsfs(getbd_balatypeBycode(map.get("balatype")));// ���㷽ʽ
		hvo.setAttributeValue("zyx46", map.get("billpriority"));// �������ȼ�
		hvo.setZyx14(map.get("isaddannex"));// �Ƿ񲹸���
		hvo.setZyx15(map.get("iscompletion"));// �Ѳ�ȫ
		if (list.contains(billtypename)) {
			hvo.setZyx18(getregionstate("finapprove"));// ������������״̬
			if (map.get("imgstate") != null)
				hvo.setZyx17(map.get("imgstate"));// Ӱ��״̬
		}
		hvo.setZyx53(map.get("MarginLevel"));// ��֤�����
		hvo.setDef65(map.get("ABSPaidInProportion"));// ABSʵ������
		hvo.setZyx54(map.get("ABSPaidInAmount"));// ABSʵ�����
		Object obj = map.get("body");
		if (obj != null) {
			JSONArray bodyjson = (JSONArray) obj;
			for (int i = 0, j = bodyjson.size(); i < j; i++) {
				JSONObject json = bodyjson.getJSONObject(i);
				hvo.setZyx52(String.valueOf(json
						.get("JointBankNoOfBillReceiver")));// ��Ʊ�����к�
				hvo.setCustaccount(getCustaccount_accnum(String.valueOf(json
						.get("PayeeCompanyCode"))));// ʵ���տ��˾���
			}
		}
		return aggvo;
	}

	public void valid(JSONObject jobj) throws BusinessException {
		if (jobj.getString("billstate") == null || jobj.getString("") == "")
			throw new BusinessException("����״̬����Ϊ��");
		if (jobj.getString("bpmid") == null || jobj.getString("") == "")
			throw new BusinessException("BPMҵ�񵥾���������Ϊ��");
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "")
			throw new BusinessException("Ŀ�굥�ݲ���Ϊ��");
		if ("APPROVE".equals(jobj.getString("billstate"))) {
			if (jobj.getString("data") == null || jobj.getString("") == "")
				throw new BusinessException("������ϢDATA����Ϊ��");
		}
	}

	public void getinvoiceByImg(String str) throws BusinessException {
		ISyncIMGBillServcie servcie = NCLocator.getInstance().lookup(
				ISyncIMGBillServcie.class);
		try {
			servcie.onSyncInv_RequiresNew(str);
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}

	public BXVO getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		NCObject[] nobjs = getMDQryService().queryBillOfNCObjectByCond(c,
				whereCondStr, false);

		if (nobjs == null || nobjs.length == 0) {
			throw new BusinessException("NCϵͳδ�ܹ�����Ϣ!");
		}
		return (BXVO) nobjs[0].getContainmentObject();// (BXVO) nobjs[0];
	}

	/**
	 * �����˻������˻�
	 * 
	 * @param custaccount
	 * @return
	 */
	private String getCustaccount_accnum(String custaccount) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select b.pk_bankaccsub from bd_bankaccsub b where b.accnum = '"
				+ custaccount + "' AND NVL(dr,0)=0";
		String result = "";
		try {
			result = (String) bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}

}
