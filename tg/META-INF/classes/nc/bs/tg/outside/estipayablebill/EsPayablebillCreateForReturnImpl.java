package nc.bs.tg.outside.estipayablebill;

import java.util.Map;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.estipayable.EstiPayableBillItemVO;
import nc.vo.arap.estipayable.EstiPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * �ݹ�Ӧ����
 * 
 * @author ASUS
 * 
 */
public class EsPayablebillCreateForReturnImpl extends EstiPayableBillUtils {
	/**
	 * ����-SRM�˻���
	 */
	@Override
	protected String getTradetype() {
		return "23E1-Cxx-LL02";
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
			throw new BusinessException("��������������Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("���������벻��Ϊ��");
		}

		if (StringUtils.isBlank(head.getString("supplier"))) {
			throw new BusinessException("��Ӧ�̲���Ϊ��");
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
	}

	/**
	 * ��������Ϣ��¼
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		// if (StringUtils.isBlank(body.getString("budgetsub"))) {
		// throw new BusinessException("Ԥ���Ŀ����Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("formatratio"))) {
		// throw new BusinessException("��ֱ�������Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("budgetyear"))) {
		// throw new BusinessException("Ԥ����Ȳ���Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("budgetmoney"))) {
		// throw new BusinessException("Ԥ���Ԫ������Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("budgetproject"))) {
		// throw new BusinessException("��Ŀ���Ʋ���Ϊ��");
		// }
		if (StringUtils.isBlank(body.getString("money_cr"))) {
			throw new BusinessException("���ս���Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("quantity_cr"))) {
			throw new BusinessException("��������Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("srmid"))) {
			throw new BusinessException("��ID����Ϊ��");
		}
	}

	@Override
	protected void setHeaderVO(EstiPayableBillVO hvo, JSONObject head)
			throws BusinessException {
		checkHeaderNotNull(head);

		Map<String, String> orgInfo = DocInfoQryUtils.getUtils().getOrgInfo(
				head.getString("org"));
		if (orgInfo == null) {
			throw new BusinessException("���˹�˾[" + head.getString("org")
					+ "]δ����NC�����������Ϣ");
		}
		hvo.setAttributeValue(EstiPayableBillVO.PK_ORG, orgInfo.get("pk_org"));// Ӧ��������֯->NCҵ��Ԫ����

		hvo.setAttributeValue(IBillFieldGet.ESTFLAG,
				BillEnumCollection.EstiType.EST.VALUE);// ����״̬
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));
		hvo.setAttributeValue("def1", head.getString("srcid"));// �Զ�����1 ��ϵͳ����
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// �Զ�����2
																	// ��ϵͳ���ݺ�->���������
		hvo.setAttributeValue("def3", head.getString("imgno"));// �Զ�����3
																// Ӱ�����->Ӱ�����
		hvo.setAttributeValue("def4", head.getString("imgstate"));// �Զ�����4
																	// Ӱ��״̬->Ӱ��״̬

		// hvo.setAttributeValue("def5", head.getString("contcode"));// �Զ�����5
		// // ��ͬ����->��ͬ����
		// hvo.setAttributeValue("def6", head.getString("contname"));//
		// ��ͬ����->��ͬ����
		// // def6
		// hvo.setAttributeValue("def7", head.getString("conttype"));// �Զ�����7

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
		hvo.setAttributeValue("def7", contInfo.get("conttypeid"));// �Զ�����7/��ͬ����ID
		// ��ͬϸ��->��ͬϸ��
		hvo.setAttributeValue("def8", contInfo.get("contcellid"));// �Զ�����8

		// String pk_supplier = contInfo.get("supplier");
		// if (StringUtils.isBlank(pk_supplier) || "~".equals(pk_supplier)) {
		// throw new BusinessException("�����ͬ��" + head.getString("contcode")
		// + "�� ���տ��Ϣ!");
		// }
		// hvo.setAttributeValue("supplier", pk_supplier);// ��Ӧ��

		hvo.setAttributeValue("def10", "SRM");// �Զ�����10 ��Դ�ⲿϵͳ

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

		/**
		 * �ɱ�Ӧ������ӽ��㷽ʽ-2020-05-26-̸�ӽ�
		 */
		String pk_balatype = DocInfoQryUtils.getUtils().getBalatypeKey(
				head.getString("balatype"));
		hvo.setAttributeValue("pk_balatype", pk_balatype);// ���㷽ʽ

	}

	@Override
	protected void setItemVO(EstiPayableBillVO headVO,
			EstiPayableBillItemVO itemVO, JSONObject body)
			throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// ��������
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// ��������
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// ��������
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// ��������ID
		itemVO.setAttributeValue(EstiPayableBillItemVO.BILLDATE,
				headVO.getBilldate());// ��������
		itemVO.setAttributeValue(EstiPayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// ��������
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_DEPTID,
				headVO.getPk_deptid());// ����
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// �� ��
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_PSNDOC,
				headVO.getPk_psndoc());// ҵ��Ա

		itemVO.setAttributeValue(EstiPayableBillItemVO.OBJTYPE,
				headVO.getObjtype());// ��������

		itemVO.setAttributeValue(EstiPayableBillItemVO.DIRECTION,
				BillEnumCollection.Direction.CREDIT.VALUE);// ����

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(EstiPayableBillItemVO.PAUSETRANSACT,
				UFBoolean.FALSE);// �����־
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// ��������
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// ����ó��
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// ����
		itemVO.setAttributeValue(EstiPayableBillItemVO.RATE, headVO.getRate());// ��֯���һ���
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// �ջ���

		itemVO.setAttributeValue(EstiPayableBillItemVO.TAXRATE, body
				.getString("taxrate") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("taxrate")));// ˰��
		itemVO.setAttributeValue(EstiPayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// ��˰���
		itemVO.setAttributeValue(EstiPayableBillItemVO.MONEY_CR, body
				.getString("money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_cr")));// �跽ԭ�ҽ�� //
		// ��˰�ϼ�->����ƻ���ϸ���
		itemVO.setAttributeValue(EstiPayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// ���ҽ��
		itemVO.setAttributeValue(EstiPayableBillItemVO.LOCAL_NOTAX_CR,
				itemVO.getMoney_cr());// // ��֯������˰���
		itemVO.setAttributeValue(EstiPayableBillItemVO.LOCAL_TAX_CR, body
				.getString("local_tax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_tax_cr")));// ˰��->����ƻ���ϸ����˰���*˰��

		itemVO.setAttributeValue(EstiPayableBillItemVO.SUPPLIER,
				headVO.getSupplier());// ��Ӧ��
		itemVO.setAttributeValue(EstiPayableBillItemVO.SCOMMENT,
				body.getString("scomment"));// ժҪ
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// ���㷽ʽ

		itemVO.setAttributeValue(EstiPayableBillItemVO.QUANTITY_CR, body
				.getString("quantity_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("quantity_cr")));// ˰��->����ƻ���ϸ����˰���*˰��

		itemVO.setAttributeValue("def21", body.getString("srmid"));// SRM������

		if (!StringUtils.isBlank(body.getString("materieltype"))) {
			String materieltype = DocInfoQryUtils.getUtils()
					.getMaterielTypeInfo(body.getString("materieltype"));
			if (materieltype == null) {
				throw new BusinessException("���Ϸ���[" + materieltype
						+ "]δ��NC��������Ϣ");
			}
			itemVO.setAttributeValue("def1", materieltype);// SRM���Ϸ���

		}

	}
}
