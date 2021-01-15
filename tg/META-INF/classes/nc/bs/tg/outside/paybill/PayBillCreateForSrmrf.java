package nc.bs.tg.outside.paybill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * SRM������NC���
 * 
 * @author ASUS
 * 
 */
public class PayBillCreateForSrmrf extends PayBillUtils {
	/**
	 * ���︶�(SRM���)
	 */
	@Override
	protected String getTradetype() {
		return "F3-Cxx-LL01";
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
			throw new BusinessException("���˹�˾����Ϊ��");
		}
		// if (StringUtils.isBlank(head.getString("billdate"))) {
		// throw new BusinessException("������ڲ���Ϊ��");
		// }
		if (StringUtils.isBlank(head.getString("bpmid"))) {
			throw new BusinessException("BPMID����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("���ݺŲ���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("EBS��������Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("contcode"))) {
			throw new BusinessException("��ͬ��Ų���Ϊ��");
		}
		// if (StringUtils.isBlank(head.getString("orderbillno"))) {
		// throw new BusinessException("�ɹ������Ų���Ϊ��");
		// }
		if (StringUtils.isBlank(head.getString("supplier"))) {
			throw new BusinessException("��Ӧ�����Ʋ���Ϊ��");
		}
		// if (StringUtils.isBlank(head.getString("bankname"))) {
		// throw new BusinessException("���������Ʋ���Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("bankcode"))) {
		// throw new BusinessException("�����б��벻��Ϊ��");
		// }
		if (StringUtils.isBlank(head.getString("recaccount"))) {
			throw new BusinessException("�տ�˺Ų���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("scomment"))) {
			throw new BusinessException("*������ɲ���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("fdtype"))) {
			throw new BusinessException("������Ͳ���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("psndoc"))) {
			throw new BusinessException("�����˲���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("psnmail"))) {
			throw new BusinessException("���������䲻��Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("imgno"))) {
			throw new BusinessException("Ӱ����벻��Ϊ��");
		}
		// if (StringUtils.isBlank(head.getString("imgstate"))) {
		// throw new BusinessException("Ӱ��״̬����Ϊ��");
		// }
		if (StringUtils.isBlank(head.getString("iscycle"))) {
			throw new BusinessException("�Ƿ������Լ��᲻��Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("isshare"))) {
			throw new BusinessException("�Ƿ��̯��ͬ����Ϊ��");
		}

	}

	@Override
	protected void setHeaderVO(PayBillVO hvo, JSONObject head)
			throws BusinessException {

		checkHeaderNotNull(head);
		Map<String, String> orgInfo = DocInfoQryUtils.getUtils().getOrgInfo(
				head.getString("org"));
		if (orgInfo == null) {
			throw new BusinessException("���˹�˾[" + head.getString("org")
					+ "]δ����NC�����������Ϣ");
		}
		hvo.setAttributeValue(PayBillVO.PK_ORG, orgInfo.get("pk_org"));// Ӧ��������֯->NCҵ��Ԫ����

		hvo.setAttributeValue(PayBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������
		// bpmid
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));
		hvo.setAttributeValue("def1", head.getString("srcid"));// �Զ�����1 ��ϵͳ����
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// �Զ�����2
																	// ��ϵͳ���ݺ�->���������
		hvo.setAttributeValue("def3", head.getString("imgno"));// �Զ�����3
																// Ӱ�����->Ӱ�����
		hvo.setAttributeValue("def4", head.getString("imgstate"));// �Զ�����4
																	// Ӱ��״̬->Ӱ��״̬

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

		hvo.setAttributeValue("def83", contInfo.get("conttypeid"));// �Զ�����7/��ͬ����ID
		// ��ͬϸ��->��ͬϸ��
		hvo.setAttributeValue("def84", contInfo.get("contcellid"));// �Զ�����8
																	// ��ͬϸ��->��ͬϸ��ID

		// String pk_supplier = contInfo.get("supplier");
		// if (StringUtils.isBlank(pk_supplier) || "~".equals(pk_supplier)) {
		// throw new BusinessException("�����ͬ��" + head.getString("contcode")
		// + "�� ���տ��Ϣ!");
		// }
		// hvo.setAttributeValue("supplier", pk_supplier);// ��Ӧ��

		hvo.setAttributeValue("def9", head.getString("orderbillno"));// �ɹ�������/�Զ�����9
		hvo.setAttributeValue("def10", "SRM");// �Զ�����10/��Դ�ⲿϵͳ
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				head.getString("supplier"), hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("��Ӧ�̡�" + head.getString("supplier")
					+ "��δ����NC������ѯ�������Ϣ");
		}
		hvo.setAttributeValue("supplier", pk_supplier);// ��Ӧ��

		// �տ������˻�
		Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
				.getCustAccnumInfo(pk_supplier, head.getString("recaccount"));
		if (recaccountInfo == null) {
			throw new BusinessException("�տ������˻�["
					+ head.getString("recaccount") + "]δ����NC���������������Ϣ!");
		}
		hvo.setAttributeValue(PayBillVO.RECACCOUNT,
				recaccountInfo.get("pk_bankaccsub"));// �տ������˻�

		hvo.setAttributeValue("def13", recaccountInfo.get("bankname"));// ����������/�Զ�����28
		hvo.setAttributeValue("def14", recaccountInfo.get("bankcode"));// �����б���/�Զ�����25

		hvo.setAttributeValue("scomment", head.getString("scomment"));// *�������/����

		// �������/�Զ�����19
		Map<String, String> fdTypeInfo = DocInfoQryUtils.getUtils()
				.getDefdocInfo(head.getString("fdtype"), "SDLL012");
		if (fdTypeInfo == null) {
			throw new BusinessException("�������[" + head.getString("fdtype")
					+ "]δ����NC���������������Ϣ!");
		}
		hvo.setAttributeValue("def19", fdTypeInfo.get("pk_defdoc"));// *�������/����

		// ������/ҵ��Ա
		Map<String, String> psnInfo = DocInfoQryUtils.getUtils().getPsnInfo(
				head.getString("psndoc"));
		if (psnInfo == null) {
			throw new BusinessException(" ������[" + head.getString("psndoc")
					+ "]δ����NC���������������Ϣ!");
		}
		hvo.setAttributeValue("def22", psnInfo.get("pk_psndoc"));// ������
		// hvo.setAttributeValue("def40", head.getString("psnmail"));//
		// ҵ��Ա����/�Զ�����24
		// hvo.setAttributeValue("def38", head.getString("iscycle"));//
		// �Ƿ������Լ���/�Զ�����20
		// hvo.setAttributeValue("def39", head.getString("isshare"));//
		// �Ƿ��̯��ͬ/�Զ�����16

		hvo.setAttributeValue(PayableBillVO.DEF11, psnInfo.get("pk_psndoc"));
		hvo.setAttributeValue("def24", head.getString("psnmail"));// ҵ��Ա����/�Զ�����24
		hvo.setAttributeValue("def20", head.getString("iscycle"));// �Ƿ������Լ���/�Զ�����20
		hvo.setAttributeValue("def16", head.getString("isshare"));// �Ƿ��̯��ͬ/�Զ�����16

		hvo.setAttributeValue(
				"def44",
				head.getString("issmalltsaxpayer") == null ? "N" : head
						.getString("issmalltsaxpayer"));// �Ƿ�С��ģ��˰��

	}

	@Override
	protected void setItemVO(PayBillVO headVO, PayBillItemVO itemVO,
			JSONObject body) throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(PayBillItemVO.PK_GROUP, headVO.getPk_group());// ��������
		itemVO.setAttributeValue(PayBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// ��������
		itemVO.setAttributeValue(PayBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// ��������
		itemVO.setAttributeValue(PayBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// ��������ID
		itemVO.setAttributeValue(PayBillItemVO.BILLDATE, headVO.getBilldate());// ��������
		itemVO.setAttributeValue(PayBillItemVO.BUSIDATE, headVO.getBusidate());// ��������
		itemVO.setAttributeValue(PayBillItemVO.PK_DEPTID, headVO.getPk_deptid());// ����
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// �� ��
		itemVO.setAttributeValue(PayBillItemVO.PK_PSNDOC, headVO.getPk_psndoc());// ҵ��Ա

		itemVO.setAttributeValue(PayBillItemVO.OBJTYPE, headVO.getObjtype());// ��������

		itemVO.setAttributeValue(PayBillItemVO.DIRECTION,
				BillEnumCollection.Direction.DEBIT.VALUE);// ����

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayBillItemVO.PAUSETRANSACT, UFBoolean.FALSE);// �����־
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// ��������
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// ����ó��
		itemVO.setAttributeValue(PayBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// ����
		itemVO.setAttributeValue(PayBillItemVO.RATE, headVO.getRate());// ��֯���һ���
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// �ջ���
		itemVO.setAttributeValue(PayBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// ��˰���
		itemVO.setAttributeValue(PayBillItemVO.SUPPLIER, headVO.getSupplier());// ��Ӧ��
		itemVO.setAttributeValue(PayBillItemVO.SCOMMENT, headVO.getScomment());// ժҪ
		
		itemVO.setAttributeValue(PayBillItemVO.RECACCOUNT, headVO.getSupplier());//�տ��˻�


		String budgetsub = body.getString("budgetsub");// Ԥ���Ŀ
		HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
				.getBudgetsubInfo(budgetsub);
		if (budgetsubInfo == null) {
			throw new BusinessException("Ԥ���Ŀ[" + budgetsub
					+ "]δ����NC���������������Ϣ!");
		}
		itemVO.setAttributeValue(PayBillItemVO.DEF1,
				budgetsubInfo.get("pk_obj"));//

		String project = body.getString("budgetproject");// ��Ŀ����
		HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
				.getProjectInfo(project);
		if (projectInfo == null) {
			throw new BusinessException("��Ŀ����[" + project + "]δ����NC���������������Ϣ!");
		}

		itemVO.setAttributeValue(PayBillItemVO.PROJECT,
				projectInfo.get("pk_project"));// ��Ŀ����

		itemVO.setAttributeValue(PayBillItemVO.DEF4,
				body.getString("formatratio"));// ��ֱ���/�Զ���4

		itemVO.setAttributeValue(PayBillItemVO.DEF6,
				body.getString("budgetyear"));// Ԥ�����/�Զ���6
		itemVO.setAttributeValue(PayBillItemVO.DEF7,
				body.getString("budgetmoney"));// Ԥ���Ԫ��/�Զ���7

		itemVO.setAttributeValue(PayBillItemVO.INVOICENO,
				body.getString("invoicecode"));// ��Ʊ���

		String invoicetype = body.getString("invoicetype");
		if (StringUtils.isNotBlank(invoicetype)) {

			Map<String, String> invTypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(invoicetype, "pjlx");
			if (invTypeInfo == null) {
				throw new BusinessException("��Ʊ����[" + budgetsub
						+ "]δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayBillItemVO.DEF2,
					invTypeInfo.get("pk_defdoc"));// ��Ʊ����/�Զ���2
		}

		itemVO.setAttributeValue(PayBillItemVO.DEF20,
				body.getString("invoicenumber"));// ��Ʊ����

		itemVO.setAttributeValue(PayBillItemVO.DEF11,
				body.getString("invoicedate"));// ��Ʊ����

		UFDouble local_money_cr = body.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr"));// ��˰���˴�����

		itemVO.setAttributeValue(PayBillItemVO.TAXRATE, UFDouble.ZERO_DBL);// ˰��
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_NOTAX_DE, local_money_cr);// ����˰���
		itemVO.setAttributeValue(PayBillItemVO.MONEY_DE, local_money_cr);// �跽ԭ�ҽ��
																			// //
		// ��˰�ϼ�->����ƻ���ϸ���
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_MONEY_DE,
				itemVO.getMoney_de());// ���ҽ��
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_TAX_DE, UFDouble.ZERO_DBL);// ˰��->����ƻ���ϸ����˰���*˰��

		String local_tax_cr = body.getString("local_tax_cr");// ˰��
		String taxrate = body.getString("taxrate");// ˰��
		String local_notax_cr = body.getString("local_notax_cr");// ����˰���
		String local_moeny_inv = body.getString("local_moeny_inv");// ��Ʊ���
		itemVO.setAttributeValue("def81", local_tax_cr);// ˰��
		itemVO.setAttributeValue("def82", local_notax_cr);// ����˰���
		itemVO.setAttributeValue("def83", taxrate);// ˰��
		itemVO.setAttributeValue("def60", local_moeny_inv);// ��Ʊ���

		String fundtype = body.getString("fundtype");
		if (StringUtils.isNotBlank(fundtype)) {
			Map<String, String> fundTypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(fundtype, "zdy020");
			if (fundtype == null) {
				throw new BusinessException("��������[" + fundtype
						+ "]δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.DEF14,
					fundTypeInfo.get("pk_defdoc"));// ��������/�Զ���2
		}

		itemVO.setAttributeValue(PayBillItemVO.DEF8, headVO.getDef19());// *�������/����

		itemVO.setAttributeValue(PayBillItemVO.DEF3,
				body.getString("moeny_deduction"));// �ɵֿ�˰��

		itemVO.setAttributeValue(PayBillItemVO.DEF26,
				body.getString("srcbodyid"));// ������ID
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
		// if (StringUtils.isBlank(body.getString("formatratio"))) {
		// throw new BusinessException("��ֱ�������Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("budgetyear"))) {
		// throw new BusinessException("Ԥ����Ȳ���Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("budgetmoney"))) {
		// throw new BusinessException("Ԥ���Ԫ������Ϊ��");
		// }
		if (StringUtils.isBlank(body.getString("budgetproject"))) {
			throw new BusinessException("��Ŀ���Ʋ���Ϊ��");
		}
		// if (StringUtils.isBlank(body.getString("invoicecode"))) {
		// throw new BusinessException("��Ʊ��Ų���Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("invoicetype"))) {
		// throw new BusinessException("��Ʊ���Ͳ���Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("fundtype"))) {
		// throw new BusinessException("�������ʲ���Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("invoicenumber"))) {
		// throw new BusinessException("��Ʊ��������Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("invoicedate"))) {
		// throw new BusinessException("��Ʊ���ڲ���Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("local_notax_cr"))) {
		// throw new BusinessException("����˰����Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("taxrate"))) {
		// throw new BusinessException("˰��%����Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("local_tax_cr"))) {
		// throw new BusinessException("˰���Ϊ��");
		// }
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("��˰����Ϊ��");
		}
		// if (StringUtils.isBlank(body.getString("moeny_deduction"))) {
		// throw new BusinessException("�ɵֿ�˰���Ϊ��");
		// }
	}

}