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
 * ������
 * 
 * @author ASUS
 * 
 */
public class MaterialTranBillUtils extends BillTranUtils {
	static MaterialTranBillUtils utils;

	public static MaterialTranBillUtils getUtils() {
		if (utils == null) {
			utils = new MaterialTranBillUtils();
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

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������

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

/*		itemVO.setAttributeValue(PayableBillItemVO.SUBJCODE, bodyvo.getSubjcode());// ��ƿ�Ŀ
		itemVO.setAttributeValue(PayableBillItemVO.PK_SUBJCODE, bodyvo.getInoutbusiclass());// ��֧��Ŀ
		itemVO.setAttributeValue(PayableBillItemVO.DEF2, bodyvo.getPaymenttype());// ��������
*/		String pk_supplier = getSupplierIDByCode(bodyvo.getSupplier(),
				headVO.getPk_org(), headVO.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("ʵ���տ��" + bodyvo.getSupplier()
					+ "��δ����NC������ѯ�������Ϣ");
		}
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// ��Ӧ��

		// Ʊ��
		if (bodyvo.getInvtype() != null) {
			String invtype = getdefdocBycode(
					bodyvo.getInvtype(),"pjlx");
			if (invtype == null) {
				throw new BusinessException("Ʊ��[" + bodyvo.getInvtype()
						+ "]δ����NC����������!");
			}
			itemVO.setAttributeValue("def8", invtype);//
		}

	}
	

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

		String pk_deptid = getPk_DeptByCode(headvo.getDept(), hvo.getPk_org());// ����
		if (pk_deptid == null) {
			throw new BusinessException("���š�" + headvo.getDept()
					+ "��δ����NC������ѯ�������Ϣ");
		}
		hvo.setAttributeValue("def46", pk_deptid);// ����

		String pk_psnodc = getPsndocPkByCode(headvo.getPsndoc());
		if (pk_psnodc == null) {
			throw new BusinessException("�����ˡ�" + headvo.getPsndoc()
					+ "��δ����NC������ѯ�������Ϣ");
		}
		hvo.setAttributeValue("def47", pk_psnodc);// ҵ��Ա
/*
		String pk_balatype = getBalatypePkByCode(headvo.getBalatype());
		if (pk_balatype == null) {
			throw new BusinessException("���㷽ʽ��" + headvo.getBalatype()
					+ "��δ����NC������ѯ�������Ϣ!");
		}
		hvo.setAttributeValue(PayableBillVO.PK_BALATYPE, pk_balatype);// ���㷽ʽ
*/
		hvo.setAttributeValue("def1", headvo.getEbsid());// �Զ�����1 ��ϵͳ����
		hvo.setAttributeValue("def2", headvo.getEbsbillno());// �Զ�����2
																// ��ϵͳ���ݺ�->���������
		/*hvo.setAttributeValue("def3", headvo.getImgno());// �Զ�����3 Ӱ�����->Ӱ�����
		hvo.setAttributeValue("def4", headvo.getImgstate());// �Զ�����4 Ӱ��״̬->Ӱ��״̬
*/		hvo.setAttributeValue("def5", headvo.getContcode());// �Զ�����5 ��ͬ����->��ͬ����
		//hvo.setAttributeValue("def6", headvo.getContname());// ��ͬ����->��ͬ���� def6
		/*hvo.setAttributeValue("def7", headvo.getConttype());// �Զ�����7
		// ��ͬ����->��ͬ����
		hvo.setAttributeValue("def8", headvo.getContcell());// �Զ�����8 ��ͬϸ��->��ͬϸ�� 
*/		// hvo.setAttributeValue("def9", headvo.getEmergency());// �Զ�����9
		// �����̶�->�����̶�
		if (StringUtils.isNotBlank(headvo.getProejctdata())) {
			String pk_projectdata = getpk_projectByCode(headvo.getProejctdata());
			if (pk_projectdata == null) {
				throw new BusinessException("��Ŀ��" + headvo.getProejctdata()
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def19", pk_projectdata);// ��Ŀ
		}
		
		hvo.setAttributeValue("def31", headvo.getNote());//˵��
		hvo.setAttributeValue("def10", "SRM");// �Զ�����10 ��Դ�ⲿϵͳ
		hvo.setAttributeValue("def9", getdefdocBycode( "02","zdy008"));// ����Ʊ������

	}

	/**
	 * ��������¼��Ϣ
	 *  
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void checkItemNotNull(PayBillItemVO bodyvo)
			throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("��Ӧ�̲���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getTaxrate())) {
			throw new BusinessException("˰�ʲ���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_money_cr())) {
			throw new BusinessException("��˰�ϼƲ���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getInvtype())) {
			throw new BusinessException("Ʊ�����ͣ�Ʊ�֣�����Ϊ��");
		}
		
	}

	/**
	 * �����ͷ��¼��Ϣ
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(PayBillHeaderVO headvo)
			throws BusinessException {
		if (headvo.getDept() == null) {
			throw new BusinessException("���첿����Ϣ����Ϊ��!");
		}
		if (headvo.getPsndoc() == null) {
			throw new BusinessException("��������Ϣ����Ϊ��!");
		}
/*
		if (headvo.getBalatype() == null) {
			throw new BusinessException("���㷽ʽ����Ϊ��!");
		}*/

		if (headvo.getEbsid() == null) {
			throw new BusinessException("EBS��������Ϊ��");
		}

		if (headvo.getEbsbillno() == null) {
			throw new BusinessException("EBS���벻��Ϊ��");
		}
/*		if (headvo.getImgno() == null) {
			throw new BusinessException("Ӱ����������Ϊ��");
		}
		if (headvo.getImgstate() == null) {
			throw new BusinessException("Ӱ��״̬����Ϊ��");
		}*/
		if (headvo.getContcode() == null) {
			throw new BusinessException("��ͬ���벻��Ϊ��");
		}
		/*if (headvo.getContname() == null) {
			throw new BusinessException("��ͬ���Ʋ���Ϊ��");
		}*/
/*		if (headvo.getConttype() == null) {
			throw new BusinessException("��ͬ���Ͳ���Ϊ��");
		}
		if (headvo.getContcell() == null) {
			throw new BusinessException("��ͬϸ�಻��Ϊ��");
		}*/
		if (headvo.getProejctdata() == null) {
			throw new BusinessException("��Ŀ����Ϊ��");
		}
	/*	if (StringUtils.isBlank(headvo.getSupplier())) {
			throw new BusinessException("�տ����Ϊ��");
		}*/
/*		if (headvo.getNote() == null) {
			throw new BusinessException("˵������Ϊ��");
		}*/
	}
}
