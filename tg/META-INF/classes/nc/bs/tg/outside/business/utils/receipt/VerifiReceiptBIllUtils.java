package nc.bs.tg.outside.business.utils.receipt;

import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.itf.tools.ItfJsonTools;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.bs.tg.outside.itf.utils.ItfUtils;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.BusinessBillCont;
import nc.itf.tg.outside.ItfConstants;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.itf.result.ResultVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.outside.BusinessBillLogVO;
import uap.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class VerifiReceiptBIllUtils extends BusinessBillUtils {

	static VerifiReceiptBIllUtils utils;

	public static VerifiReceiptBIllUtils getUtils() {
		if (utils == null) {
			utils = new VerifiReceiptBIllUtils();
		}
		return utils;
	}

	public String getValue(JSONObject value, String srctype)
			throws BusinessException {
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// �����û���
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		HashMap<String, String> dataMap = new HashMap<String, String>();
		
		ISqlThread sql=NCLocator.getInstance().lookup(ISqlThread.class);

		// ��־��Ϣ
		BusinessBillLogVO logVO = new BusinessBillLogVO();
		ResultVO resultVO = new ResultVO();
		String rsInfo = "";

		JSONObject Object = (JSONObject) value.get("data");// ��������
		JSONObject jsonObject = Object.getJSONObject("headInfo");// ��ͷ��Ϣ
		String srcid = jsonObject.getString("def1");// ����ϵͳҵ�񵥾�ID
		String srcno = jsonObject.getString("def2");// ����ϵͳҵ�񵥾ݵ��ݺ�

		try {

			logVO.setSrcsystem(BusinessBillCont.SRCSYS);
			logVO.setSrcparm(value.toString());
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult(STATUS_SUCCESS);
			logVO.setOperator(OperatorName);
			logVO.setDesbill(BusinessBillCont.getBillNameMap().get(srctype));
			logVO.setTrantype("F2-Cxx-SY001");

			logVO.setBusinessno(jsonObject.getString("def2"));

			String billqueue = BusinessBillCont.getBillNameMap().get(srctype)
					+ ":" + srcid;
			String billkey = BusinessBillCont.getBillNameMap().get(srctype)
					+ ":" + srcno;

			// srcid ��ʵ�ʴ�����Ϣλ�ý��б��
			AggGatheringBillVO aggVO = (AggGatheringBillVO) getBillVO(
					AggGatheringBillVO.class, "nvl(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ srcid + "��,�����ظ��ϴ�!");
			}
			addBillQueue(billqueue);// ���Ӷ��д���

			AggregatedValueObject[] aggvo = null;

			try {
				AggGatheringBillVO billvo = onTranBill(value, srctype);
				// HashMap eParam = new HashMap();
				// eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				// PfUtilBaseTools.PARAM_NOTE_CHECKED);
				IWorkflowMachine iWorkflowMachine = NCLocator.getInstance()
						.lookup(IWorkflowMachine.class);
				WorkflownoteVO worknote = null;
				worknote = iWorkflowMachine.checkWorkFlow(
						IPFActionName.SAVE,
						(String) billvo.getParentVO().getAttributeValue(
								"pk_tradetype"), billvo, null);
				aggvo = (AggregatedValueObject[]) getPfBusiAction().processAction(
						"SAVE", "F2", worknote, billvo, null, null);
			} catch (Exception e) {
				throw new BusinessException("��" + billkey + "��,"
						+ e.getMessage(), e);
			} finally {
				removeBillQueue(billqueue);
			}
			dataMap.put("billid", aggvo[0].getParentVO().getPrimaryKey());
			dataMap.put("billno", (String) aggvo[0].getParentVO()
					.getAttributeValue("billno"));

			logVO.setBillno((String) aggvo[0].getParentVO().getAttributeValue(
					"billno"));
			logVO.setErrmsg(JSON.toJSONString(dataMap));
			resultVO.setBillid((String) aggvo[0].getParentVO()
					.getAttributeValue("billno"));
			resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
			resultVO.setMsg("�����ɹ�");
		} catch (Exception e) {
			logVO.setErrmsg(e.getMessage());
			Logger.error(e.getMessage(), e);
			logVO.setResult(STATUS_FAILED);
			resultVO.setBillid(srcid);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
			resultVO.setMsg("�����쳣��" + e.getMessage());
		} finally {
			rsInfo = net.sf.json.JSONObject.fromObject(resultVO).toString();
			try {
				sql.billInsert_RequiresNew(logVO);
				//getBaseDAO().executeUpdate("commit");
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return rsInfo;
	}

	private AggGatheringBillVO onTranBill(JSONObject value, String srctype)
			throws Exception {
		JSONObject Object = (JSONObject) value.get("data");// ��������
		String jsonbody = Object.getString("bodyInfos");

		JSONArray jsonArr = JSON.parseArray(jsonbody);

		AggGatheringBillVO billVO = ItfJsonTools.jsonToAggVO(Object,
				AggGatheringBillVO.class, GatheringBillVO.class,
				GatheringBillItemVO.class);

		// ת��ǰ�ǿ�У�顢ִ�й�ʽ����������ǿ�У��
		ItfUtils.notNullCheckAndExFormula(ItfConstants.RECEIPT_HEAD,
				ItfConstants.RECEIPT_BODY, billVO);
		GatheringBillItemVO[] itemvos = (GatheringBillItemVO[]) billVO
				.getChildrenVO();
		for (int i = 0; i < jsonArr.size(); i++) {
			com.alibaba.fastjson.JSONObject bJSONObject = jsonArr
					.getJSONObject(i);
			if (!"".equals(bJSONObject.getString("vdef1"))
					&& bJSONObject.getString("vdef1") != null) {
				if ("".equals(itemvos[i].getVdef1())
						|| itemvos[i].getVdef1() == null) {
					throw new BusinessException("�������ƣ�ԭ�ֿ۵���"
							+ bJSONObject.getString("vdef1") + "δ��NC����");
				}
			}
			if (!"".equals(bJSONObject.getString("vdef2"))
					&& bJSONObject.getString("vdef2") != null) {
				if ("".equals(itemvos[i].getVdef2())
						|| itemvos[i].getVdef2() == null) {
					throw new BusinessException("���ز���Ŀ��ԭ�ֿ۵���"
							+ bJSONObject.getString("vdef2") + "δ��NC����");
				}
			}
			if (!"".equals(bJSONObject.getString("vdef3"))
					&& bJSONObject.getString("vdef3") != null) {
				if ("".equals(itemvos[i].getVdef3())
						|| itemvos[i].getVdef3() == null) {
					throw new BusinessException("�������ϣ�ԭ�ֿ۵���"
							+ bJSONObject.getString("vdef3") + "δ��NC����");
				}
			}
			if (!"".equals(bJSONObject.getString("recaccount"))
					&& bJSONObject.getString("recaccount") != null) {
				if ("".equals(itemvos[i].getRecaccount())
						|| itemvos[i].getRecaccount() == null) {
					throw new BusinessException("�տ��˻�"
							+ bJSONObject.getString("recaccount") + "δ��NC����");
				}
			}
		}
		return billVO;
	}
}
