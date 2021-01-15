package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * SRMϵͳ����Ӧ�������ݵ�NC����ϵͳ�ӿ� �ⲿ�ӿ��޶���ʷ��Ϣ�Խ�
 * 
 * @author ASUS
 * 
 */
@Deprecated
public class PayableBillCreateForHistory extends PayableBillUtils {

	/**
	 * ����-Ա������ʷ��Ӧ�̺�ͬ\�Ǻ�ͬ���
	 */
	@Override
	protected String getTradetype() {
		return "F1-Cxx-LL01";
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
		if (StringUtils.isBlank(head.getString("billamount"))) {
			throw new BusinessException("����Ʊ�ݽ���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("EBS��������Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("EBS���ݺŲ���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("contcode"))) {
			throw new BusinessException("��ͬ���벻��Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("contname"))) {
			throw new BusinessException("��ͬ���Ʋ���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("conttype"))) {
			throw new BusinessException("��ͬ���Ͳ���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("plate"))) {
			throw new BusinessException("��鲻��Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("supplier"))) {
			throw new BusinessException("��Ӧ�̲���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("proejctdata"))) {
			throw new BusinessException("��Ŀ����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("proejctstages"))) {
			throw new BusinessException("��Ŀ���ڲ���Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("totalmny_inv"))) {
			throw new BusinessException("�ۼƷ�Ʊ����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("auditstate"))) {
			throw new BusinessException("������������״̬����Ϊ��");
		}
		// if (StringUtils.isBlank(head.getString("billdate"))) {
		// throw new BusinessException("�������ڲ���Ϊ��");
		// }
		if (StringUtils.isBlank(head.getString("def55"))) {
			throw new BusinessException("EBS��ʽ����Ϊ��!");
		}

	}

	/**
	 * �ɱ�Ӧ��������def15�ɵֿ�˰����ݷ�Ʊ���������¼���-2020-04-24-̸�ӽ�
	 * 
	 * @param bodyvo
	 * 
	 * @param ocal_tax_cr
	 *            ()
	 * @throws BusinessException
	 */
	private String getDeductibleTax(JSONObject body) throws BusinessException {
		String deductibleTax = "0";
		String invtype = body.getString("invtype");
		String local_tax_cr = body.getString("local_tax_cr");
		if ("01".equals(invtype) || "08".equals(invtype)
				|| "10".equals(invtype) || "11".equals(invtype)
				|| "15".equals(invtype) || "18".equals(invtype)
				|| "19".equals(invtype)) {
			deductibleTax = local_tax_cr;
		}

		if ("04".equals(invtype) || "05".equals(invtype)
				|| "07".equals(invtype) || "16".equals(invtype)
				|| "17".equals(invtype)) {
			Map<String, String> info = DocInfoQryUtils.getUtils()
					.getDefdocInfo(invtype, "pjlx");
			String calculation = info.get("memo");
			String amount = body.getString("local_money_cr");
			String sqlstr = calculation.toString().replace("amount", amount);
			String sql = "select " + sqlstr + " from dual ";
			Object Tax = getBaseDAO().executeQuery(sql, new ColumnProcessor());
			deductibleTax = Tax.toString();
		}
		return deductibleTax;
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
		hvo.setAttributeValue("def5", head.getString("contcode"));// �Զ�����5
																	// ��ͬ����->��ͬ����
		hvo.setAttributeValue("def6", head.getString("contname"));// ��ͬ����->��ͬ����
																	// def6
		hvo.setAttributeValue("def7", head.getString("conttype"));// �Զ�����7
		/**
		 * �ɱ�Ӧ�����ӿڣ��Զ����ݺ�ͬ���ʹ���Ӧ������ͷ������Ʊ�����͡���NC�ֶ�-def56��,���յ���zdy008-2020-05-08-̸�ӽ�
		 * -start
		 */
		String conttype = head.getString("conttype");
		Map<String, String> conttypeInfo = DocInfoQryUtils.getUtils()
				.getDefdocInfo(conttype, "zdy045");
		if (conttypeInfo == null) {
			conttypeInfo = DocInfoQryUtils.getUtils().getDefdocInfo(conttype,
					"zdy008");
		}
		if (conttypeInfo != null) {
			hvo.setAttributeValue("def56", conttypeInfo.get("pk_defdoc"));// �Զ�����8
		}
		/**
		 * �ɱ�Ӧ�����ӿڣ��Զ����ݺ�ͬ���ʹ���Ӧ������ͷ������Ʊ�����͡���NC�ֶ�-def56��,���յ���zdy008-2020-05-08-̸�ӽ�
		 * -end
		 */
		// ��ͬϸ��->��ͬϸ��
		// ��ͬ����->��ͬ����
		hvo.setAttributeValue("def8", head.getString("contcell"));// �Զ�����8
																	// ��ͬϸ��->��ͬϸ��
		// �����̶�->�����̶�2020-02-18-tzj
		String emergency = head.getString("mergency");
		Map<String, String> emergencyInfo = DocInfoQryUtils.getUtils()
				.getDefdocInfo(emergency, "zdy031");
		if (emergencyInfo != null) {
			hvo.setAttributeValue("def9", emergencyInfo.get("pk_defdoc"));// �Զ�����9
		}
		hvo.setAttributeValue("def10", "SRM");// �Զ�����10 ��Դ�ⲿϵͳ

		if (StringUtils.isNotBlank(head.getString("plate"))) {
			Map<String, String> plateInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(head.getString("plate"), "bkxx");
			if (plateInfo == null) {
				throw new BusinessException("��顾" + head.getString("plate")
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def15", plateInfo.get("pk_defdoc"));// �Զ�����15
																		// ���
		}
		if (StringUtils.isNotBlank(head.getString("accorg"))) {
			Map<String, String> accorgInfo = DocInfoQryUtils.getUtils()
					.getOrgInfo(head.getString("accorg"));
			if (accorgInfo == null) {
				throw new BusinessException("���˹�˾��" + head.getString("accorg")
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def17", accorgInfo.get("pk_org"));// �Զ�����17
			// ���˹�˾->NCҵ��Ԫ����
		}
		// ������Ӧ��2019-11-01-tzj
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				head.getString("supplier"), hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("��Ӧ�̡�" + head.getString("supplier")
					+ "��δ����NC������ѯ�������Ϣ");
		}
		hvo.setAttributeValue("supplier", pk_supplier);// ��Ӧ��

		// hvo.setPk_busitype(null);//��������
		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������

		if (StringUtils.isNotBlank(head.getString("proejctdata"))) {
			HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
					.getSpecialProjectInfo(head.getString("proejctdata"));
			if (projectInfo == null) {
				throw new BusinessException("��Ŀ��"
						+ head.getString("proejctdata") + "��δ����NC���������������Ϣ!");

			}
			hvo.setAttributeValue("def19", projectInfo.get("pk_project"));// ��Ŀ
			// Ӧ�����ɴ�����Ŀ��Ϣ�����Ƿ��ʱ�(def8),��Ŀ����(def9)
			hvo.setAttributeValue("def61", projectInfo.get("def8"));// �Ƿ��ʱ�
			hvo.setAttributeValue("def62", projectInfo.get("def9"));// ��Ŀ����

		}

		if (StringUtils.isNotBlank(head.getString("proejctstages"))) {
			HashMap<String, String> projectstagesInfo = DocInfoQryUtils
					.getUtils().getSpecialProjectInfo(
							head.getString("proejctstages"));
			if (projectstagesInfo == null) {
				throw new BusinessException("��Ŀ���� ��"
						+ head.getString("proejctstages") + "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def32", projectstagesInfo.get("pk_project"));// �Զ�����32
																				// ��Ŀ����
		}
		hvo.setAttributeValue("money",
				head.getString("billamount") == null ? UFDouble.ZERO_DBL
						: new UFDouble(head.getString("billamount")));// ����Ʊ�ݽ��
		// hvo.setAttributeValue("def21", headvo.getTotalmny_request());//
		// �ۼ������
		// hvo.setAttributeValue("def22", headvo.getTotalmny_pay());// �ۼƸ�����
		// hvo.setAttributeValue("def23", headvo.getIsshotgun());// �Ƿ��ȸ����Ʊ
		// hvo.setAttributeValue("def24", headvo.getTotalmny_paybythisreq());//
		// ��������ۼƸ�����
		hvo.setAttributeValue("def26",
				head.getString("totalmny_inv") == null ? UFDouble.ZERO_DBL
						: new UFDouble(head.getString("totalmny_inv")));// �ۼƷ�Ʊ���
		// hvo.setAttributeValue("def28", headvo.getIsdeduction());// �Ƿ����ʱ���۳�
		hvo.setAttributeValue("def31", head.getString("note"));// ˵��
		// hvo.setAttributeValue("def33", headvo.getAuditstate());
		// ������������״̬
		if (StringUtils.isNotBlank(head.getString("auditstate"))) {
			Map<String, String> auditstateInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(head.getString("auditstate"), "zdy032");
			if (auditstateInfo == null) {
				throw new BusinessException("������������״̬ ��"
						+ head.getString("auditstate") + "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def33", auditstateInfo.get("pk_defdoc"));// �Զ�����32
																			// ��Ŀ����
		}
		hvo.setAttributeValue("def43", head.getString("def43"));// �Ǳ�׼ָ�����def43
		hvo.setAttributeValue("def44", head.getString("def44"));// ������def44
		hvo.setAttributeValue("def45", head.getString("def45"));// �Ѳ�ȫdef45
		hvo.setAttributeValue("def46", head.getString("dept"));// ���첿��
		hvo.setAttributeValue("def47", head.getString("psndoc"));// ������
		hvo.setAttributeValue("def50", "N");// Ʊ��Ȩ������
		hvo.setAttributeValue("def55", head.getString("def55"));// EBS��ʽ-def55

		if (StringUtils.isNotBlank(head.getString("signorg"))) {
			Map<String, String> signOrgInfo = DocInfoQryUtils.getUtils()
					.getOrgInfo(head.getString("signorg"));
			if (signOrgInfo == null) {
				throw new BusinessException("ǩԼ��˾��" + head.getString("signorg")
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def34", signOrgInfo.get("pk_org"));// ǩԼ��˾
		}
		/**
		 * �ɱ�Ӧ������ӽ��㷽ʽ-2020-05-26-̸�ӽ�
		 */
		String pk_balatype = DocInfoQryUtils.getUtils().getBalatypeKey(
				head.getString("balatype"));
		hvo.setAttributeValue("pk_balatype", pk_balatype);// ���㷽ʽ

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
		itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
				body.getString("scomment"));// ժҪ

		if (StringUtils.isNotBlank(body.getString("subjcode"))) {
			String pk_subjcode = DocInfoQryUtils.getUtils().getAccSubInfo(
					body.getString("subjcode"), headVO.getPk_org());
			if (pk_subjcode == null) {
				throw new BusinessException("��ƿ�Ŀ["
						+ body.getString("subjcode") + "]δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.SUBJCODE, pk_subjcode);// ��Ŀ����
																				// ��ƿ�Ŀ
		}
		if (StringUtils.isNotBlank(body.getString("costsubject"))) {
			Map<String, String> costsubjectInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(body.getString("costsubject"), "zdy024");
			if (costsubjectInfo == null) {
				throw new BusinessException("�ɱ���Ŀ["
						+ body.getString("costsubject") + "]δ����NC����������!");
			}
			itemVO.setAttributeValue("def7", costsubjectInfo.get("pk_defdoc"));// �ɱ���Ŀ

		}

		if (StringUtils.isNotBlank(body.getString("invtype"))) {
			Map<String, String> invtypetInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(body.getString("invtype"), "pjlx");
			if (invtypetInfo == null) {
				throw new BusinessException("Ʊ��[" + body.getString("invtype")
						+ "]δ����NC����������!");
			}
			itemVO.setAttributeValue("def8", invtypetInfo.get("pk_defdoc"));
		}
		// ����ҵ̬ def3
		itemVO.setAttributeValue("def3", body.getString("format"));
		// ���ñ��� def4
		UFDouble formatratio = body.getString("formatratio") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("formatratio"));
		itemVO.setAttributeValue("def4", formatratio.multiply(100).toString());
		// ����Ʊ������
		itemVO.setAttributeValue("def9", "100112100000000069WH");
		// �ɱ�Ӧ��������def15�ɵֿ�˰����ݷ�Ʊ���������¼���-2020-04-24-̸�ӽ�
		String deductibleTax = getDeductibleTax(body);
		itemVO.setAttributeValue("def15", deductibleTax);
		/**
		 * �ɱ�Ӧ������ӽ��㷽ʽ-2020-06-04-̸�ӽ�
		 */
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// ���㷽ʽ

	}

	/**
	 * ��������Ϣ��¼
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		// if (StringUtils.isBlank(body.getString("subjcode"))) {
		// throw new BusinessException("��ƿ�Ŀ����Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("inoutbusiclass"))) {
		// throw new BusinessException("��֧��Ŀ����Ϊ��");
		// }
		if (StringUtils.isBlank(body.getString("taxrate"))) {
			throw new BusinessException("˰�ʲ���Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("local_notax_cr"))) {
			throw new BusinessException("��˰����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("local_tax_cr"))) {
			throw new BusinessException("˰���Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("��˰�ϼƲ���Ϊ��");
		}
		// if (StringUtils.isBlank(body.getString("paymenttype"))) {
		// throw new BusinessException("�������Ͳ���Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("format"))) {
		// throw new BusinessException("ҵ̬����Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("formatratio"))) {
		// throw new BusinessException("��������Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("recaccount"))) {
		// throw new BusinessException("�տ�������кŲ���Ϊ��");
		// }
		if (StringUtils.isBlank(body.getString("costsubject"))) {
			throw new BusinessException("�ɱ���Ŀ����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("invtype"))) {
			throw new BusinessException("Ʊ������(Ʊ��)����Ϊ��");
		}
		// if (StringUtils.isBlank(body.getString("scomment"))) {
		// throw new BusinessException("ժҪ����Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("supplier"))) {
		// throw new BusinessException("��Ӧ�̲���Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("budgetsub"))) {
		// throw new BusinessException("Ԥ���Ŀ����Ϊ��");
		// }
	}

}
