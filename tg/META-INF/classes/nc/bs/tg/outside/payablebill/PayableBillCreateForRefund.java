package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ObjType;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * SRMϵͳ����Ӧ�������ݵ�NC����ϵͳ�ӿ�
 * 
 * @author ASUS
 * 
 */
public class PayableBillCreateForRefund extends PayableBillUtils {
	/**
	 * ����-SRM��֤���˿�
	 */
	@Override
	protected String getTradetype() {
		return "F1-Cxx-LL06";
	}

	public String getDefaultoperator() {
		return "LLBPM";
	}

	/**
	 * �����ͷ��¼��Ϣ
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("org"))) {
			throw new BusinessException("������֯����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("��֤����������Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("��֤�𵥵��ݺŲ���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("bpmid"))) {
			throw new BusinessException("BPM��������Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("imgno"))) {
			throw new BusinessException("Ӱ����벻��Ϊ��");
		}
		// if (StringUtils.isBlank(head.getString("imgstate"))) {
		// throw new BusinessException("Ӱ��״̬����Ϊ��");
		// }
		if (StringUtils.isBlank(head.getString("psndoc"))) {
			throw new BusinessException("ҵ��Ա����Ϊ��");
		}

		if (StringUtils.isBlank(head.getString("psnmail"))) {
			throw new BusinessException("ҵ��Ա���䲻��Ϊ��");
		}

		if (StringUtils.isBlank(head.getString("isforfeiture"))) {
			throw new BusinessException("�Ƿ�û����Ϊ��");
		}

		if (StringUtils.isBlank(head.getString("currtype"))) {
			throw new BusinessException("���ֲ���Ϊ��");
		}

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

		// bpmid
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));
		hvo.setAttributeValue("def1", head.getString("srcid"));// �Զ�����1 ��ϵͳ����
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// �Զ�����2
																	// ��ϵͳ���ݺ�->���������
		hvo.setAttributeValue("def3", head.getString("imgno"));// �Զ�����3
																// Ӱ�����->Ӱ�����
		hvo.setAttributeValue("def4", head.getString("imgstate"));// �Զ�����4
																	// Ӱ��״̬->Ӱ��״̬

		hvo.setAttributeValue("def13", head.getString("def13"));// �Զ�����13 ��ע

		String currtype = head.getString("currtype");// ����
		HashMap<String, String> currtypeInfo = DocInfoQryUtils.getUtils()
				.getCurrtypeInfo(currtype);
		if (currtypeInfo == null) {
			throw new BusinessException("����[" + currtype + "]δ����NC���������������Ϣ!");
		}
		hvo.setPk_currtype(currtypeInfo.get("pk_currtype"));

		String psndoc = head.getString("psndoc");// ��Ա
		hvo.setAttributeValue(PayableBillVO.DEF11, psndoc);
		hvo.setAttributeValue(PayableBillVO.DEF24, head.getString("psnmail"));// ҵ��Ա����/�Զ�����24

		hvo.setAttributeValue(PayableBillVO.DEF13,
				"Y".equals(head.getString("isforfeiture")) ? "Y" : "N");// �Ƿ�û/def13s

		String supplier = head.getString("supplier");
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				supplier, hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("��Ӧ��[" + supplier + "]δ����NC���������������Ϣ!");
		}

		hvo.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// ��Ӧ��

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

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE,
				ObjType.SUPPLIER.VALUE);// ��������

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

		itemVO.setAttributeValue(IBillFieldGet.SUPPLIER, headVO.getSupplier());// ��Ӧ��

		String balatype = body.getString("balatype");// ���㷽ʽ
		String pk_balatype = DocInfoQryUtils.getUtils()
				.getBalatypeKey(balatype);//
		if (pk_balatype == null) {
			throw new BusinessException("���㷽ʽ[" + balatype + "]δ����NC���������������Ϣ!");
		}

		itemVO.setAttributeValue("pk_balatype", pk_balatype);// ���㷽ʽ

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, UFDouble.ZERO_DBL);// ˰��
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// ��˰���
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, body
				.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr")));// �跽ԭ�ҽ�� //
		// ��˰�ϼ�
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// ���ҽ��
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR,
				itemVO.getMoney_cr());// // ��֯������˰���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR,
				UFDouble.ZERO_DBL);// ˰��

		String moneytype = body.getString("fundtype");// ��������
		itemVO.setDef10(moneytype);

		// �տ������˻�
		Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
				.getSupplierAccnumInfo(itemVO.getSupplier(),
						body.getString("recaccount"));
		if (recaccountInfo == null) {
			throw new BusinessException("�տ������˻�["
					+ body.getString("recaccount") + "]δ����NC���������������Ϣ!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.RECACCOUNT,
				recaccountInfo.get("pk_bankaccsub"));// �տ������˻�

		itemVO.setAttributeValue("def30", recaccountInfo.get("bankname"));// ����������/�Զ�����28
		itemVO.setAttributeValue("def29", recaccountInfo.get("bankcode"));// �����б���/�Զ�����25

		// �������˻�
		Map<String, String> payaccountInfo = DocInfoQryUtils.getUtils()
				.getBankaccnumInfo(itemVO.getPk_org(),
						body.getString("payaccount"));
		if (payaccountInfo == null) {
			throw new BusinessException("���������˻�["
					+ body.getString("payaccount") + "]δ����NC���������������Ϣ!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.PAYACCOUNT,
				recaccountInfo.get("pk_bankaccsub"));// ���������˻�

		String bondtype = body.getString("bondtype");// �б걣֤������
		itemVO.setAttributeValue(PayableBillItemVO.DEF11, bondtype);// �б걣֤������
		itemVO.setAttributeValue(PayableBillItemVO.DEF43,
				body.getString("bondmoney"));// Ͷ�걣֤����

		itemVO.setAttributeValue(PayableBillItemVO.DEF44,
				body.getString("forfeituremoney"));// ��û���
		itemVO.setAttributeValue(PayableBillItemVO.DEF42,
				body.getString("forfeiturenote"));// ��ûԭ��

		String budgetsub = body.getString("budgetsub");// Ԥ���Ŀ
		HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
				.getBudgetsubInfo(budgetsub);
		if (budgetsubInfo == null) {
			throw new BusinessException("Ԥ���Ŀ[" + budgetsub
					+ "]δ����NC���������������Ϣ!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.DEF1,
				budgetsubInfo.get("pk_obj"));//
	}

	/**
	 * ��������Ϣ��¼
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		if (StringUtils.isBlank(body.getString("balatype"))) {
			throw new BusinessException("���㷽ʽ����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("budgetsub"))) {
			throw new BusinessException("Ԥ���Ŀ����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("recaccount"))) {
			throw new BusinessException("�տ������˻�����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("payaccount"))) {
			throw new BusinessException("���������˻�����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("�˱�֤�������Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("fundtype"))) {
			throw new BusinessException("�������Ͳ���Ϊ��");
		}

		if (StringUtils.isBlank(body.getString("bondtype"))) {
			throw new BusinessException("�б걣֤�����Ͳ���Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("bondmoney"))) {
			throw new BusinessException("Ͷ�걣֤�����Ϊ��");
		}

	}

}
