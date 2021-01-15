package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * ͨ��(EBS)�����ͬ����NCӦ����(���ڼ���)
 * 
 * @author ASUS
 * 
 */
public class PayablebillCreateForProvision extends PayableBillUtils {

	/**
	 * ���ڼ���Ӧ����
	 */
	@Override
	protected String getTradetype() {
		return "F1-Cxx-LL04";
	}

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(getDefaultoperator()));
		InvocationInfoProxy.getInstance().setUserCode(getDefaultoperator());
		// ��ϵͳ��Ϣ
		JSONObject data = (JSONObject) info.get("data");// ��ϵͳ��Դ��ͷ����
		JSONObject jsonhead = (JSONObject) data.get("headInfo");// ��ϵͳ��Դ��ͷ����
		String srcid = jsonhead.getString("srcid") + "&"
				+ jsonhead.getString("period");// ��ԴID
		String srcno = jsonhead.getString("srcbillno");// ��Դ���ݺ�

		Map<String, String> resultInfo = new HashMap<String, String>();
		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
					AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��"
						+ billkey
						+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "��,�����ظ��ϴ�!");
			}
			AggPayableBillVO billvo = onTranBill(info);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			WorkflownoteVO worknoteVO = getWorkflowMachine().checkWorkFlow(
					"SAVE", billvo.getHeadVO().getPk_tradetype(), billvo,
					new HashMap<String, Object>());
			Object obj = getPfBusiAction().processAction("SAVE",
					billvo.getHeadVO().getPk_tradetype(), worknoteVO, billvo,
					null, null);
			AggPayableBillVO[] billvos = (AggPayableBillVO[]) obj;
			if (isPushApprove()) {
				approveSilently(billvo.getHeadVO().getPk_tradetype(),
						billvos[0].getPrimaryKey(), "Y", "���ͨ��", billvos[0]
								.getHeadVO().getCreator(), false);
			}

			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayableBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	/**
	 * �����ͷ��¼��Ϣ
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("org"))) {
			throw new BusinessException("���˹�˾����Ϊ��");
		}
		// if (StringUtils.isBlank(head.getString("billdate"))) {
		// throw new BusinessException("�Ƶ����ڲ���Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("srcbillno"))) {
		// throw new BusinessException("���ݺŲ���Ϊ��");
		// }
		if (StringUtils.isBlank(head.getString("period"))) {
			throw new BusinessException("�·ݲ���Ϊ��");
		}

		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("EBS��������Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("contcode"))) {
			throw new BusinessException("��ͬ��Ų���Ϊ��");
		}
		// if (StringUtils.isBlank(head.getString("contname"))) {
		// throw new BusinessException("*��ͬ���Ʋ���Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("conttype"))) {
		// throw new BusinessException("��ͬ���Ͳ���Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("contcell"))) {
		// throw new BusinessException("��ͬϸ�಻��Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("supplier"))) {
		// throw new BusinessException("��Ӧ�̲���Ϊ��");
		// }

	}

	@Override
	protected void setHeaderVO(PayableBillVO hvo, JSONObject head)
			throws BusinessException {

		checkHeaderNotNull(head);

		Map<String, String> orgInfo = DocInfoQryUtils.getUtils().getOrgInfo(
				head.getString("org"));
		if (orgInfo == null) {
			throw new BusinessException("���˹�˾[" + head.getString("org")
					+ "]δ����NC�����������Ϣ");
		}
		hvo.setAttributeValue(PayableBillVO.PK_ORG, orgInfo.get("pk_org"));// Ӧ��������֯->NCҵ��Ԫ����

		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������

		// bpmid
		hvo.setAttributeValue("def1", head.getString("srcid"));// �Զ�����1 ��ϵͳ����
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// �Զ�����2
																	// ��ϵͳ���ݺ�->���������
		Map<String, String> contInfo = DocInfoQryUtils.getUtils()
				.getPayContInfo(head.getString("contcode"));
		if (contInfo == null) {
			throw new BusinessException("��ͬ[" + head.getString("contcode")
					+ "]δ����NC�����ͬ�����������Ϣ");
		}

		hvo.setAttributeValue("def5", contInfo.get("contcode"));// �Զ�����5
																// ��ͬ����->��ͬ����
		hvo.setAttributeValue("def6", contInfo.get("contname"));// ��ͬ����->��ͬ����
																// def6
		hvo.setAttributeValue("def7", contInfo.get("conttype"));// �Զ�����7/��ͬ����
		// ��ͬϸ��->��ͬϸ��
		hvo.setAttributeValue("def8", contInfo.get("contcell"));// �Զ�����8
																// ��ͬϸ��->��ͬϸ��

		String period = head.getString("period");
		String[] splitStr = period.split("-");

		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR, splitStr[0]);// ���ݻ�����
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD, splitStr[1]);// ���ݻ���ڼ�

		hvo.setAttributeValue("def10", "ͨ��-EBS");// �Զ�����10/��Դ�ⲿϵͳ
		// String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
		// head.getString("supplier"), hvo.getPk_org(), hvo.getPk_group());
		// if (pk_supplier == null) {
		// throw new BusinessException("��Ӧ�̡�" + head.getString("supplier")
		// + "��δ����NC������ѯ�������Ϣ");
		// }
		String pk_supplier = contInfo.get("supplier");
		if (StringUtils.isBlank(pk_supplier) || "~".equals(pk_supplier)) {
			throw new BusinessException("�����ͬ��" + head.getString("contcode")
					+ "�� ���տ��Ϣ!");
		}
		hvo.setAttributeValue("supplier", pk_supplier);// ��Ӧ��
		hvo.setAttributeValue("scomment", head.getString("scomment"));// *�������/����
		hvo.setAttributeValue("def20", head.getString("iscycle"));// �Ƿ������Լ���/�Զ�����20
		hvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.FALSE);// �Ƿ����/�Զ�����16
		
		hvo.setAttributeValue("def83", contInfo.get("conttypeid"));// �Զ�����7/��ͬ����ID
		// ��ͬϸ��->��ͬϸ��
		hvo.setAttributeValue("def84", contInfo.get("contcellid"));// �Զ�����8
																	// ��ͬϸ��->��ͬϸ��ID
		
	}

	@Override
	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			JSONObject body) throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(PayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// ��������ID
		itemVO.setAttributeValue(PayableBillItemVO.BILLDATE,
				headVO.getBilldate());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
				headVO.getPk_deptid());// ����
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// �� ��
		itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
				headVO.getPk_psndoc());// ҵ��Ա

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, headVO.getObjtype());// ��������

		itemVO.setAttributeValue(PayableBillItemVO.DIRECTION,
				BillEnumCollection.Direction.CREDIT.VALUE);// ����

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayableBillItemVO.PAUSETRANSACT,
				UFBoolean.FALSE);// �����־
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// ��������
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// ����ó��
		itemVO.setAttributeValue(PayableBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// ����
		itemVO.setAttributeValue(PayableBillItemVO.RATE, headVO.getRate());// ��֯���һ���
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// �ջ���
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// ��˰���
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER,
				headVO.getSupplier());// ��Ӧ��
		itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
				headVO.getScomment());// ժҪ

		String budgetsub = body.getString("budgetsub");// Ԥ���Ŀ
		HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
				.getBudgetsubInfo(budgetsub);
		if (budgetsubInfo == null) {
			throw new BusinessException("Ԥ���Ŀ[" + budgetsub
					+ "]δ����NC���������������Ϣ!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.DEF1,
				budgetsubInfo.get("pk_obj"));//

		String project = body.getString("budgetproject");// ��Ŀ����
		HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
				.getProjectInfo(project);
		if (projectInfo == null) {
			throw new BusinessException("��Ŀ����[" + project + "]δ����NC���������������Ϣ!");
		}

		itemVO.setAttributeValue(PayableBillItemVO.PROJECT,
				projectInfo.get("pk_project"));// ��Ŀ����

		itemVO.setAttributeValue(PayableBillItemVO.DEF4,
				body.getString("formatratio"));// ��ֱ���/�Զ���4

		itemVO.setAttributeValue(PayableBillItemVO.DEF6,
				body.getString("budgetyear"));// Ԥ�����/�Զ���6
		itemVO.setAttributeValue(PayableBillItemVO.DEF7,
				body.getString("budgetmoney"));// Ԥ���Ԫ��/�Զ���7

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, UFDouble.ZERO_DBL);// ˰��
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, body
				.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr")));// �跽ԭ�ҽ�� //
		// ��˰�ϼ�->����ƻ���ϸ���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// ���ҽ��
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR,
				itemVO.getMoney_cr());// ����˰���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR,
				UFDouble.ZERO_DBL);// ˰��

		itemVO.setAttributeValue(PayableBillItemVO.DEF31,
				projectInfo.get("note"));// ˵��/�Զ�����31

	}

	/**
	 * ��������Ϣ��¼
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		if (StringUtils.isBlank(body.getString("budgetsub"))) {
			throw new BusinessException("Ԥ���Ŀ����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("formatratio"))) {
			throw new BusinessException("��ֱ�������Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("budgetyear"))) {
			throw new BusinessException("Ԥ����Ȳ���Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("budgetmoney"))) {
			throw new BusinessException("Ԥ���Ԫ������Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("budgetproject"))) {
			throw new BusinessException("��Ŀ���Ʋ���Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("�������Ϊ��");
		}

	}

	/**
	 * �Ƿ������
	 * 
	 * @return
	 */
	public boolean isPushApprove() {
		return true;
	}
}
