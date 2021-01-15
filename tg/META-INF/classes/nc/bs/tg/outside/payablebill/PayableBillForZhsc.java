package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * SDLL-467-NCӦ�����Խ��ۺ��̳ǳɱ��ӿ�
 * 
 * 1��F1-Cxx-LL07����-�ۺ��̳ǳɱ���Ӧ����F3-Cxx-LL04����-�ۺ��̳����۳ɱ���
 * 
 * 2��F1-Cxx-LL11����-������ѡ��Ӫ���۳ɱ�Ӧ����F3-Cxx-LL05 ����-������ѡ���۳ɱ���
 * 
 * @author ̸�ӽ� 2020-12-28
 * 
 */

public class PayableBillForZhsc extends PayableBillUtils {

	@Override
	protected String getTradetype() {
		return null;
	}

	/**
	 * �����ͷ��¼��Ϣ
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("pk_org"))) {
			throw new BusinessException("������֯pk_org����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("��ϵͳ��������srcid����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("��ϵͳ���ݺ�srcbillno����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("bpmid"))) {
			throw new BusinessException("bpmid����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("billdate"))) {
			throw new BusinessException("��������billdate����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("fdtype"))) {
			throw new BusinessException("�����fdtype����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("scomment"))) {
			throw new BusinessException("�������scomment����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("pk_psndoc"))) {
			throw new BusinessException("ҵ��Աpk_psndoc����Ϊ��!");
		}
		if (StringUtils.isBlank(head.getString("mail"))) {
			throw new BusinessException("ҵ��Ա����mail����Ϊ��!");
		}
		if (StringUtils.isBlank(head.getString("isapproved"))) {
			throw new BusinessException("EBSԤ��ռ���Ƿ����ͨ��isapproved����Ϊ��!");
		}
		if (StringUtils.isBlank(head.getString("issmalltsaxpayer"))) {
			throw new BusinessException("�Ƿ�С��ģ��˰��issmalltsaxpayer����Ϊ��!");
		}
		if (StringUtils.isBlank(head.getString("hyperlinks"))) {
			throw new BusinessException("�ۺ��̳Ǹ���������hyperlinks����Ϊ��!");
		}

	}

	// /**
	// * �ɱ�Ӧ��������def15�ɵֿ�˰����ݷ�Ʊ���������¼���-2020-04-24-̸�ӽ�
	// *
	// * @param bodyvo
	// *
	// * @param ocal_tax_cr
	// * ()
	// * @throws BusinessException
	// */
	// private String getDeductibleTax(JSONObject body) throws BusinessException
	// {
	// String deductibleTax = "0";
	// String invtype = body.getString("invtype");
	// String local_tax_cr = body.getString("local_tax_cr");
	// if ("01".equals(invtype) || "08".equals(invtype)
	// || "10".equals(invtype) || "11".equals(invtype)
	// || "15".equals(invtype) || "18".equals(invtype)
	// || "19".equals(invtype)) {
	// deductibleTax = local_tax_cr;
	// }
	//
	// if ("04".equals(invtype) || "05".equals(invtype)
	// || "07".equals(invtype) || "16".equals(invtype)
	// || "17".equals(invtype)) {
	// Map<String, String> info = DocInfoQryUtils.getUtils()
	// .getDefdocInfo(invtype, "pjlx");
	// String calculation = info.get("memo");
	// String amount = body.getString("local_money_cr");
	// String sqlstr = calculation.toString().replace("amount", amount);
	// String sql = "select " + sqlstr + " from dual ";
	// Object Tax = getBaseDAO().executeQuery(sql, new ColumnProcessor());
	// deductibleTax = Tax.toString();
	// }
	// return deductibleTax;
	// }

	@Override
	protected void setHeaderVO(PayableBillVO hvo, JSONObject head)
			throws BusinessException {

		checkHeaderNotNull(head);
		Map<String, String> orgInfo = DocInfoQryUtils.getUtils().getOrgInfo(
				head.getString("pk_org"));
		if (orgInfo == null) {
			throw new BusinessException("������֯[" + head.getString("pk_org")
					+ "]δ����NC�����������Ϣ");
		}
		hvo.setAttributeValue(PayableBillVO.PK_ORG, orgInfo.get("pk_org"));// Ӧ��������֯->NCҵ��Ԫ����
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));// bpmid
		hvo.setAttributeValue("def1", head.getString("srcid"));// ��ϵͳ����
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// ��ϵͳ���ݺ�
		// �������/�Զ�����19
		Map<String, String> fdTypeInfo = DocInfoQryUtils.getUtils()
				.getDefdocInfo(head.getString("fdtype"), "SDLL012");
		if (fdTypeInfo == null) {
			throw new BusinessException("�������[" + head.getString("fdtype")
					+ "]δ����NC���������������Ϣ!");
		}
		hvo.setAttributeValue("def19", fdTypeInfo.get("pk_defdoc"));// �������
		hvo.setAttributeValue("scomment", head.getString("scomment"));// *�������/����
		hvo.setAttributeValue("def22", head.getString("pk_psndoc"));// ��Ա(��������)
		hvo.setAttributeValue("def24", head.getString("mail"));// ҵ��Ա����/�Զ�����24
		hvo.setAttributeValue("def87", head.getString("isapproved"));// EBSԤ��ռ���Ƿ����ͨ��
		hvo.setAttributeValue("def44", head.getString("issmalltsaxpayer"));// �Ƿ�С��ģ��˰��
		hvo.setAttributeValue("hyperlinks", head.getString("hyperlinks"));// �ۺ��̳Ǹ���������
		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������
		hvo.setAttributeValue(PayableBillVO.EFFECTSTATUS, 0);// ��Ч״̬
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

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, body
				.getString("taxrate") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("taxrate")));// ˰��
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// ��˰���
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, body
				.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr")));// �跽ԭ�ҽ�� //
		// ��˰�ϼ�->����ƻ���ϸ���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// ���ҽ��
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR, body
				.getString("local_notax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_notax_cr")));// // ��֯������˰���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR, body
				.getString("local_tax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_tax_cr")));// ˰��->����ƻ���ϸ����˰���*˰��
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER,
				headVO.getSupplier());// ��Ӧ��
		itemVO.setAttributeValue(PayableBillItemVO.DEF26,
				body.getString("rowid"));
		itemVO.setAttributeValue(PayableBillItemVO.DEF14, DocInfoQryUtils
				.getUtils().getDefdocInfo("A3", "zdy020"));// Ĭ�Ͻ�������Ϊ:A3

		String budgetsub = body.getString("budgetsub");// Ԥ���Ŀ
		HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
				.getBudgetsubInfo(budgetsub);
		if (budgetsubInfo == null) {
			throw new BusinessException("Ԥ���Ŀ[" + budgetsub
					+ "]δ����NC���������������Ϣ!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.DEF1,
				budgetsubInfo.get("pk_obj"));// Ԥ���Ŀ����

		String project = body.getString("project");// ��Ŀ����
		HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
				.getProjectInfo(project);
		if (projectInfo == null) {
			throw new BusinessException("��Ŀ����[" + project + "]δ����NC���������������Ϣ!");
		}

		itemVO.setAttributeValue(PayableBillItemVO.PROJECT,
				projectInfo.get("pk_project"));// ��Ŀ����

		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				body.getString("supplier"), headVO.getPk_org(),
				headVO.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("��Ӧ�̡�" + body.getString("supplier")
					+ "��δ����NC������ѯ�������Ϣ");
		}
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// ��Ӧ��

		Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
				.getCustAccnumInfo(pk_supplier, body.getString("recaccount"));// �տ������˻�

		if (recaccountInfo == null) {
			throw new BusinessException("�տ������˻�["
					+ body.getString("recaccount") + "]δ����NC���������������Ϣ!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.RECACCOUNT,
				recaccountInfo.get("pk_bankaccsub"));// �տ������˻�
		itemVO.setAttributeValue(PayableBillItemVO.DEF28,
				recaccountInfo.get("bankname"));// ����������/�Զ�����28

		itemVO.setAttributeValue(PayableBillItemVO.DEF3,
				body.getString("deductibletax"));// �ɵֿ�˰��

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, headVO.getObjtype());// ��������
	}

	/**
	 * ��������Ϣ��¼
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		if (StringUtils.isBlank(body.getString("rowid"))) {
			throw new BusinessException("������rowid����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("budgetsub"))) {
			throw new BusinessException("Ԥ���Ŀbudgetsub����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("project"))) {
			throw new BusinessException("��Ŀproject����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("supplier"))) {
			throw new BusinessException("��Ӧ��supplier����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("recaccount"))) {
			throw new BusinessException("�տ������˺�recaccount����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("local_notax_cr"))) {
			throw new BusinessException("��˰���local_notax_cr����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("taxrate"))) {
			throw new BusinessException("˰��taxrate����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("local_tax_cr"))) {
			throw new BusinessException("˰��local_tax_cr����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("invoice_money"))) {
			throw new BusinessException("��Ʊ���invoice_money����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("��˰���local_money_cr����Ϊ��");
		}

	}

}
