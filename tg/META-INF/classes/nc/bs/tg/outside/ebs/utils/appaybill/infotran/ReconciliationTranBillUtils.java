package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.appaybill.PayBillHeaderVO;
import nc.vo.tg.outside.appaybill.PayBillItemVO;

import org.apache.commons.lang.StringUtils;

/**
 * SRM���˵�
 * 
 * @author ASUS
 * 
 */
public class ReconciliationTranBillUtils extends BillTranUtils {
	static ReconciliationTranBillUtils utils;

	public static ReconciliationTranBillUtils getUtils() {
		if (utils == null) {
			utils = new ReconciliationTranBillUtils();
		}
		return utils;
	}

	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			PayBillItemVO bodyvo) throws BusinessException {
		checkItemNotNull(bodyvo);
		itemVO.setAttributeValue(PayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// ��������ID
		// itemVO.setAttributeValue(PayableBillItemVO.BILLDATE,
		// headVO.getBilldate());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// ��������

		// itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
		// headVO.getPk_deptid());// ����
		// itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
		// headVO.getPk_deptid_v());// �� ��
		// itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
		// headVO.getPk_psndoc());// ҵ��Ա

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������

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

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, bodyvo.getTaxrate());// ˰��
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// ��˰���
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, bodyvo
				.getLocal_money_cr() == null ? UFDouble.ZERO_DBL
				: new UFDouble(bodyvo.getLocal_money_cr()));// �跽ԭ�ҽ�� //
															// ��˰�ϼ�->����ƻ���ϸ���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// ��Ӧ�̿�Ʊ����˰��
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR, bodyvo
				.getLocal_notax_cr() == null ? UFDouble.ZERO_DBL
				: new UFDouble(bodyvo.getLocal_notax_cr()));// // ��Ӧ�̿�Ʊ������˰��
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR, bodyvo
				.getLocal_tax_cr() == null ? UFDouble.ZERO_DBL : new UFDouble(
				bodyvo.getLocal_tax_cr()));// ��Ӧ�̿�Ʊ��˰�

		String pk_supplier = headVO.getSupplier();
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// ��Ӧ��

		itemVO.setAttributeValue("def19", "100112100000000071JH");// ��Ŀ(Ĭ��0409)
		itemVO.setAttributeValue("def22", bodyvo.getInvposted());// ��Ʊ�����˱��
		itemVO.setAttributeValue("def23", bodyvo.getArrimoney());// �������ս�����˰��
		/*
		 * // Ʊ�� if (bodyvo.getChecktype() != null) { String pk_checktype =
		 * getNotetypeByCode(bodyvo.getChecktype()); if (pk_checktype == null) {
		 * throw new BusinessException("Ʊ������[" + bodyvo.getChecktype() +
		 * "]δ����NC���������������Ϣ!"); }
		 * itemVO.setAttributeValue(PayableBillItemVO.CHECKTYPE,
		 * pk_checktype);// Ʊ������ }
		 * 
		 * // Ʊ�� if (bodyvo.getChecktype() != null) { String pk_checktype =
		 * getdefdocBycode(bodyvo.getChecktype(),"pjlx" ); if (pk_checktype ==
		 * null) { throw new BusinessException("Ʊ��[" + bodyvo.getInvtype() +
		 * "]δ����NC����������!"); } itemVO.setAttributeValue("def8", pk_checktype);//
		 * } if (bodyvo.getMny_accadj() != null) {
		 * itemVO.setAttributeValue("def10", bodyvo.getMny_accadj());// ���˵�������˰��
		 * } if (bodyvo.getMny_notax_accadj() != null) {
		 * itemVO.setAttributeValue("def11", bodyvo.getMny_notax_accadj());//
		 * ���˵���������˰�� } if (bodyvo.getMny_notax_accadj() != null) {
		 * itemVO.setAttributeValue("def12", bodyvo.getTax_accadj());// ���˵�����˰�
		 * }
		 */
		// itemVO.setAttributeValue("def27", bodyvo.getPurordercode());// �ɹ���������
		// def26
		// itemVO.setAttributeValue("def13", bodyvo.getMny_scm());// ��Ӧ������˰��
		// itemVO.setAttributeValue("def14", bodyvo.getMny_notax_scm());//
		// ��Ӧ��������˰��
		// itemVO.setAttributeValue("def15", bodyvo.getTax_scm());// ��Ӧ����˰�
		// itemVO.setAttributeValue("def21", bodyvo.getAccbodyid());// ��̨����id
		// itemVO.setAttributeValue("def23", bodyvo.getAccbillno());// ���˵���
		// itemVO.setAttributeValue("def24", bodyvo.getDeduction());// �ۿ���
		// itemVO.setAttributeValue("def25", bodyvo.getAppliedamount());// ������
		/*
		 * itemVO.setAttributeValue("def22", bodyvo.getEvaluatecostbillno());//
		 * nc�ݹ�������� itemVO.setAttributeValue("def20", bodyvo.getMny_cost());//
		 * �ɱ� itemVO.setAttributeValue("def16", bodyvo.getMny_evaluatecost());//
		 * �ݹ���ɱ�
		 */}

	/**
	 * ����������Ϣ
	 * 
	 * @param parentVO
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void setHeaderVO(PayableBillVO hvo, PayBillHeaderVO headvo)
			throws BusinessException {
		checkHeaderNotNull(headvo);

		/*
		 * String pk_deptid = getPk_DeptByCode(headvo.getDept(),
		 * hvo.getPk_org());// ���� if (pk_deptid == null) { throw new
		 * BusinessException("���š�" + headvo.getDept() + "��δ����NC������ѯ�������Ϣ"); }
		 * hvo.setAttributeValue("def46", pk_deptid);// ����
		 * 
		 * String pk_psnodc = getPsndocPkByCode(headvo.getPsndoc()); if
		 * (pk_psnodc == null) { throw new BusinessException("�����ˡ�" +
		 * headvo.getPsndoc() + "��δ����NC������ѯ�������Ϣ"); }
		 * hvo.setAttributeValue("def47", pk_psnodc);// ҵ��Ա
		 */
		hvo.setAttributeValue("def1", headvo.getEbsid());// �Զ�����1 ��ϵͳ����
		hvo.setAttributeValue("def2", headvo.getEbsbillno());// �Զ�����2
																// ��ϵͳ���ݺ�->���������
		hvo.setAttributeValue("def3", headvo.getImgno());// �Զ�����3 Ӱ�����->Ӱ�����
		hvo.setAttributeValue("def4", headvo.getImgstate());// �Զ�����4 Ӱ��״̬->Ӱ��״̬
		hvo.setAttributeValue("def5", headvo.getPurchaseno());// �ɹ�Э�����
		hvo.setAttributeValue("def6", headvo.getPurchasename());// �ɹ�Э������
		hvo.setAttributeValue("def10", "SRM");// �Զ�����10 ��Դ�ⲿϵͳ
		hvo.setAttributeValue("def59", headvo.getAdvanceinv());// Ԥ���Ʊ

		//hvo.setAttributeValue("def51", headvo.getReceivedamount());// �Զ�����10
																	// ��Դ�ⲿϵͳ
		// hvo.setAttributeValue("def7", headvo.getConttype());// �Զ�����7
		// ��ͬ����->��ͬ����
		// hvo.setAttributeValue("def8", headvo.getContcell());// �Զ�����8
		// ��ͬϸ��->��ͬϸ��
		// hvo.setAttributeValue("def9", headvo.getEmergency());// �Զ�����9
		// �����̶�->�����̶�
		// hvo.setAttributeValue("def31", headvo.getNote());// ˵��
		/*
		 * hvo.setAttributeValue("def35", headvo.getPurchaseno());// �ɹ�Э�����
		 * hvo.setAttributeValue("def37", headvo.getPurchasename());// �ɹ�Э������
		 * hvo.setAttributeValue("def38", headvo.getAccbillno());// ���˵���
		 */
		String pk_supplier = getSupplierIDByCode(headvo.getSupplier(),
				hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("��Ӧ�̡�" + headvo.getSupplier()
					+ "��δ����NC������ѯ�������Ϣ");
		}
		hvo.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// ��Ӧ��

		/*
		 * if (headvo.getProejctstages() != null) { String pk_proejctstages =
		 * getpk_projectByCode(headvo .getProejctstages()); if (pk_proejctstages
		 * == null) { throw new BusinessException("��Ŀ��" +
		 * headvo.getProejctstages() + "��δ����NC���������������Ϣ!"); }
		 * hvo.setAttributeValue("def32", pk_proejctstages);// �Զ�����32 ��Ŀ���� }
		 */
		hvo.setAttributeValue("def9", getdefdocBycode("zdy008", "02"));// ����Ʊ������

	}

	/**
	 * ��������¼��Ϣ
	 * 
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void checkItemNotNull(PayBillItemVO bodyvo)
			throws BusinessException {

		/*
		 * if (StringUtils.isBlank(bodyvo.getChecktype())) { throw new
		 * BusinessException("Ʊ�����ͣ�Ʊ�֣�����Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_accadj())) { throw new
		 * BusinessException("���˵�������˰������Ϊ��"); } if
		 * (StringUtils.isBlank(bodyvo.getMny_notax_accadj())) { throw new
		 * BusinessException("���˵���������˰������Ϊ��"); } if
		 * (StringUtils.isBlank(bodyvo.getTax_accadj())) { throw new
		 * BusinessException("���˵�����˰�����Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_scm())) { throw new
		 * BusinessException("��Ӧ������˰������Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_notax_scm())) { throw new
		 * BusinessException("��Ӧ��������˰������Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getTax_scm())) { throw new
		 * BusinessException("��Ӧ����˰�����Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_evaluatecost())) { throw new
		 * BusinessException("�ݹ���ɱ�����Ϊ��"); }
		 */
		if (StringUtils.isBlank(bodyvo.getLocal_money_cr())) {
			throw new BusinessException("��Ӧ�̿�Ʊ����˰������Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_notax_cr())) {
			throw new BusinessException("��Ӧ�̿�Ʊ������˰������Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_tax_cr())) {
			throw new BusinessException("��Ӧ�̿�Ʊ��˰�����Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getInvposted())) {
			throw new BusinessException("��Ʊ�����˱�ǲ���Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_cost())) { throw new
		 * BusinessException("�ɱ�����Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getAccbodyid())) { throw new
		 * BusinessException("��̨����id����Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getEvaluatecostbillno())) { throw new
		 * BusinessException("nc�ݹ�������Ų���Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getAccbillno())) { throw new
		 * BusinessException("���˵��Ų���Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getDeduction())) { throw new
		 * BusinessException("�ۿ����Ϊ��"); } if
		 * (StringUtils.isBlank(bodyvo.getAppliedamount())) { throw new
		 * BusinessException("�������Ϊ��"); }
		 */
		// if (StringUtils.isBlank(bodyvo.getProejctdata())) {
		// throw new BusinessException("��Ŀ����Ϊ��");
		// }
	}

	/**
	 * �����ͷ��¼��Ϣ
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(PayBillHeaderVO headvo)
			throws BusinessException {
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("������֯����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getSupplier())) {
			throw new BusinessException("��Ӧ�̲���Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getDept())) { throw new
		 * BusinessException("���첿�Ų���Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(headvo.getPsndoc())) { throw new
		 * BusinessException("�����˲���Ϊ��"); }
		 */

		if (!"Y".equals(headvo.getAdvanceinv())) {
			if (StringUtils.isBlank(headvo.getEbsid())) {
				throw new BusinessException("��ϵͳ��������Ϊ��");
			}
			if (StringUtils.isBlank(headvo.getEbsbillno())) {
				throw new BusinessException("��ϵͳ���ݺŲ���Ϊ��");
			}
		}
//		if (StringUtils.isBlank(headvo.getImgno())) {
//			throw new BusinessException("Ӱ����벻��Ϊ��");
//		}
//		if (StringUtils.isBlank(headvo.getImgstate())) {
//			throw new BusinessException("Ӱ��״̬����Ϊ��");
//		}
		if (StringUtils.isBlank(headvo.getAdvanceinv())) {
			throw new BusinessException("Ԥ���Ʊ����Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getContcode())) { throw new
		 * BusinessException("��ͬ���벻��Ϊ��"); } if
		 * (StringUtils.isBlank(headvo.getContname())) { throw new
		 * BusinessException("��ͬ���Ʋ���Ϊ��"); } if
		 * (StringUtils.isBlank(headvo.getConttype())) { throw new
		 * BusinessException("��ͬ���Ͳ���Ϊ��"); } if
		 * (StringUtils.isBlank(headvo.getContcell())) { throw new
		 * BusinessException("��ͬϸ�಻��Ϊ��"); }
		 * 
		 * if (StringUtils.isBlank(headvo.getNote())) { throw new
		 * BusinessException("˵������Ϊ��"); }
		 */

		if (StringUtils.isBlank(headvo.getPurchaseno())) {
			throw new BusinessException("�ɹ�Э����벻��Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getPurchasename())) {
			throw new BusinessException("�ɹ�Э�����Ʋ���Ϊ��");
		}

	}

}
