package nc.bs.tg.outside.statement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.cmp.bill.RecBillAggVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.outside.LLCollectionStatementJsonBVO;
import nc.vo.tg.outside.LLCollectionStatementJsonVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CollectionStatementUtils extends BillUtils implements
		ITGSyncService {
	public static final String WYSFDefaultOperator = "LLWYSF";// ��ҵ�շ�ϵͳ�Ƶ���

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(WYSFDefaultOperator));
		// �����û���
		InvocationInfoProxy.getInstance().setUserCode(WYSFDefaultOperator);

		// �������Ϣ
		JSONObject jsonData = (JSONObject) info.get("data");// ������
		String jsonhead = jsonData.getString("headInfo");// ��ϵͳ��Դ��ͷ����
		String jsonbody = jsonData.getString("bodyInfo");// ��ϵͳ��Դ��������
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + jsonData);
		}
		// ת��json
		LLCollectionStatementJsonVO headVO = JSONObject.parseObject(jsonhead,
				LLCollectionStatementJsonVO.class);
		List<LLCollectionStatementJsonBVO> bodyVOs = JSONObject.parseArray(
				jsonbody, LLCollectionStatementJsonBVO.class);
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
			RecBillAggVO aggVO = (RecBillAggVO) getBillVO(RecBillAggVO.class,
					"isnull(dr,0)=0 and def1 = '" + srcid + "'");

			if (aggVO != null) {
				throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue("bill_no")
						+ "��,�����ظ��ϴ�!");
			}

			RecBillAggVO billvo = onTranBill(headVO, bodyVOs);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			String trade_type = (String) billvo.getParentVO()
					.getAttributeValue("trade_type");

			WorkflownoteVO worknoteVO = NCLocator.getInstance()
					.lookup(IWorkflowMachine.class)
					.checkWorkFlow("SAVE", trade_type, billvo, eParam);

			Object obj = (RecBillAggVO[]) getPfBusiAction().processAction(
					"SAVE", trade_type, worknoteVO, billvo, null, null);
			RecBillAggVO[] billvos = (RecBillAggVO[]) obj;
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("bill_no"));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected RecBillAggVO onTranBill(LLCollectionStatementJsonVO headVO,
			List<LLCollectionStatementJsonBVO> bodyVOs)
			throws BusinessException {
		return null;
	}

	/**
	 * 
	 * 2020-09-23-̸�ӽ�
	 * 
	 * ��ȡ�շ���Ŀ����
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getItemnameByPk(String pk, String docCode)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select d.pk_defdoc  ");
		query.append("  from bd_defdoclist c, bd_defdoc d  ");
		query.append(" where c.pk_defdoclist = d.pk_defdoclist  ");
		query.append("   and c.code = '" + docCode + "'  ");
		query.append("   and d.def1 = '" + pk + "'  ");
		query.append("   and nvl(c.dr, 0) = 0  ");
		query.append("   and nvl(d.dr, 0) = 0  ");
		query.append("   and d.enablestate = '2'  ");
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	public String getPk_billtypeid(String pk_billtype) throws BusinessException {
		String pk_billtypeid = null;
		StringBuffer query = new StringBuffer();
		query.append("select y.pk_billtypeid from bd_billtype y where y.pk_billtypecode = '"
				+ pk_billtype + "'  ");
		pk_billtypeid = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_billtypeid;

	}

	public String getAccountIDByCode(String pk_org, String accnum)
			throws BusinessException {
		String pk_bankaccsub = null;
		StringBuffer query = new StringBuffer();
		query.append("select distinct bd_bankaccsub.pk_bankaccsub  ");
		query.append("  from bd_bankaccbas  ");
		query.append(" INNER JOIN bd_bankaccsub  ");
		query.append("    ON bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas  ");
		query.append(" where (accclass = 2 and accstate <> 3 and acctype in (0) and  ");
		query.append("       pk_currtype = '1002Z0100000000001K1')  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and (bd_bankaccsub.pk_bankaccsub in  ");
		query.append("       (select pk_bankaccsub  ");
		query.append("           from bd_bankaccuse  ");
		query.append("          where pk_org = '" + pk_org + "'  ");
		query.append("            and enablestate = 2))  ");
		query.append("   and (bd_bankaccbas.pk_banktype = '0001Z010000000000366')  ");
		query.append("   and bd_bankaccsub.accnum = '" + accnum + "'  ");
		pk_bankaccsub = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_bankaccsub;

	}
}
