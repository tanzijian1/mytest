package nc.bs.tg.outside.business.utils.incomebusi;

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
import nc.vo.itf.result.ResultVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.BusinessBillLogVO;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.tgfn.changebill.ChangeBillBVO;
import nc.vo.tgfn.changebill.ChangeBillHVO;
import uap.json.JSONObject;

import com.alibaba.fastjson.JSON;

public class IncomeBusinessUtils extends BusinessBillUtils{
	static IncomeBusinessUtils utils;

	public static IncomeBusinessUtils getUtils() {
		if (utils == null) {
			utils = new IncomeBusinessUtils();
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
		JSONObject jsonObject = Object.getJSONObject("headInfo");//��ͷ��Ϣ
		String srcid = jsonObject.getString("def1");// ����ϵͳҵ�񵥾�ID
		String srcno = jsonObject.getString("def2");// ����ϵͳҵ�񵥾ݵ��ݺ�

		try {

			logVO.setSrcsystem(BusinessBillCont.SRCSYS);
			logVO.setSrcparm(value.toString());
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult(STATUS_SUCCESS);
			logVO.setOperator(OperatorName);
			logVO.setDesbill(BusinessBillCont.getBillNameMap().get(srctype));
			logVO.setTrantype("FN11-Cxx-SY001");
			logVO.setBusinessno(jsonObject.getString("def2"));
			String billqueue = BusinessBillCont.getBillNameMap().get(srctype)
					+ ":" + srcid;
			String billkey = BusinessBillCont.getBillNameMap().get(srctype)
					+ ":" + srcno;

			
			// srcid ��ʵ�ʴ�����Ϣλ�ý��б��
			AggChangeBillHVO aggVO = (AggChangeBillHVO) getBillVO(
					AggChangeBillHVO.class, "nvl(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ srcid + "��,�����ظ��ϴ�!");
			}
			addBillQueue(billqueue);// ���Ӷ��д���

			AggregatedValueObject[] aggvo = null;

			try {
				AggChangeBillHVO billvo = onTranBill(value, srctype);
				// HashMap eParam = new HashMap();
				// eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				// PfUtilBaseTools.PARAM_NOTE_CHECKED);
				AggChangeBillHVO[] aggvos = (AggChangeBillHVO[]) getPfBusiAction().processAction(
						"SAVEBASE", "FN11", null, billvo, null, null);
				aggvo = (AggregatedValueObject[]) getPfBusiAction().processAction(
						"SAVE", "FN11", null, aggvos[0], null, null);
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
			logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			Logger.error(e.getMessage(), e);
			logVO.setResult(STATUS_FAILED);
			resultVO.setBillid(srcid);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
			resultVO.setMsg("�����쳣��" + e.getMessage());
		} finally {
			rsInfo = net.sf.json.JSONObject.fromObject(resultVO).toString();
			try {
				sql.billInsert_RequiresNew(logVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return rsInfo;
	}

	private AggChangeBillHVO onTranBill(JSONObject value, String srctype)
			throws Exception {
		JSONObject Object = (JSONObject) value.get("data");// ��������

		AggChangeBillHVO billVO = ItfJsonTools.jsonToAggVO(Object,
				AggChangeBillHVO.class, ChangeBillHVO.class,
				ChangeBillBVO.class);

		// // ��̨ת��
		// GatheringBillItemVO[] gitemvos = (GatheringBillItemVO[]) billVO
		// .getChildrenVO();
		// for (GatheringBillItemVO gatheringBillItemVO : gitemvos) {
		// // �տ������˻�
		// gatheringBillItemVO.setRecaccount(getPersonalAccountIDByCode(
		// (String) billVO.getParentVO().getAttributeValue("pk_org"),
		// gatheringBillItemVO.getSupplier()));
		// }

		// ת��ǰ�ǿ�У�顢ִ�й�ʽ����������ǿ�У��
		ItfUtils.notNullCheckAndExFormula(ItfConstants.INCOME_HEAD,
				ItfConstants.INCOME_BODY, billVO);

		return billVO;
	}
}
