package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import java.util.HashMap;
import java.util.Map;

import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
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
 * ��Ŀ���ܽ�/�ɱ����
 * 
 * @author ASUS
 * 
 */
public class CostTranBillUtils extends BillTranUtils {
	static CostTranBillUtils utils;

	public static CostTranBillUtils getUtils() {
		if (utils == null) {
			utils = new CostTranBillUtils();
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

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, bodyvo.getTaxrate());// ˰��
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// ��˰���
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, bodyvo
				.getLocal_money_cr() == null ? UFDouble.ZERO_DBL
				: new UFDouble(bodyvo.getLocal_money_cr()));// �跽ԭ�ҽ�� //
															// ��˰�ϼ�->����ƻ���ϸ���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// ���ҽ��
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR, bodyvo
				.getLocal_notax_cr() == null ? UFDouble.ZERO_DBL
				: new UFDouble(bodyvo.getLocal_notax_cr()));// // ��֯������˰���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR, bodyvo
				.getLocal_tax_cr() == null ? UFDouble.ZERO_DBL : new UFDouble(
				bodyvo.getLocal_tax_cr()));// ˰��->����ƻ���ϸ����˰���*˰��

		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER,
				headVO.getSupplier());// ��Ӧ��
		if (StringUtils.isNotBlank(bodyvo.getScomment())) {
			itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
					bodyvo.getScomment());// ժҪ
		}
		if (StringUtils.isNotBlank(bodyvo.getSubjcode())) {
			String pk_subjcode = getAccsubjKeyByCode(bodyvo.getSubjcode(),
					headVO.getPk_org());
			if (pk_subjcode == null) {
				throw new BusinessException("��ƿ�Ŀ[" + bodyvo.getSubjcode()
						+ "]δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.SUBJCODE, pk_subjcode);// ��Ŀ����
																				// ��ƿ�Ŀ
		}
		if (StringUtils.isNotBlank(bodyvo.getCostsubject())) {
			String pk_costsubject = getdefdocBycode(bodyvo.getCostsubject(),
					"zdy024");
			if (pk_costsubject == null) {
				throw new BusinessException("�ɱ���Ŀ[" + bodyvo.getCostsubject()
						+ "]δ����NC����������!");
			}
			itemVO.setAttributeValue("def7", pk_costsubject);// �ɱ���Ŀ

		}

		if (StringUtils.isNotBlank(bodyvo.getInvtype())) {
			String pk_costsubject = getdefdocBycode(bodyvo.getInvtype(), "pjlx");
			if (pk_costsubject == null) {
				throw new BusinessException("Ʊ��[" + bodyvo.getInvtype()
						+ "]δ����NC����������!");
			}
			itemVO.setAttributeValue("def8", pk_costsubject);
		}
		// ����ҵ̬ def3
		itemVO.setAttributeValue("def3", bodyvo.getFormat());
		// ���ñ��� def4
		String formatratio = bodyvo.getFormatratio();
		UFDouble formatratioDouble = new UFDouble(formatratio);
		formatratio = formatratioDouble.multiply(100).toString();
		itemVO.setAttributeValue("def4", formatratio);
		// ����Ʊ������
		itemVO.setAttributeValue("def9", "100112100000000069WH");
		// �ɱ�Ӧ��������def15�ɵֿ�˰����ݷ�Ʊ���������¼���-2020-04-24-̸�ӽ�
		String deductibleTax = getDeductibleTax(bodyvo);
		itemVO.setAttributeValue("def15", deductibleTax);
		/**
		 * �ɱ�Ӧ������ӽ��㷽ʽ-2020-06-04-̸�ӽ�
		 */
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// ���㷽ʽ

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
		// bpmid
		hvo.setAttributeValue("bpmid", headvo.getBpmid());
		/*
		 * String pk_balatype = getBalatypePkByCode(headvo.getBalatype()); if
		 * (pk_balatype == null) { throw new BusinessException("���㷽ʽ��" +
		 * headvo.getBalatype() + "��δ����NC������ѯ�������Ϣ!"); }
		 * hvo.setAttributeValue(PayableBillVO.PK_BALATYPE, pk_balatype);// ���㷽ʽ
		 */
		hvo.setAttributeValue("def1", headvo.getEbsid());// �Զ�����1 ��ϵͳ����
		hvo.setAttributeValue("def2", headvo.getEbsbillno());// �Զ�����2
																// ��ϵͳ���ݺ�->���������
		hvo.setAttributeValue("def3", headvo.getImgno());// �Զ�����3 Ӱ�����->Ӱ�����
		hvo.setAttributeValue("def4", headvo.getImgstate());// �Զ�����4 Ӱ��״̬->Ӱ��״̬
		hvo.setAttributeValue("def5", headvo.getContcode());// �Զ�����5 ��ͬ����->��ͬ����
		hvo.setAttributeValue("def6", headvo.getContname());// ��ͬ����->��ͬ���� def6
		hvo.setAttributeValue("def7", headvo.getConttype());// �Զ�����7
		/**
		 * �ɱ�Ӧ�����ӿڣ��Զ����ݺ�ͬ���ʹ���Ӧ������ͷ������Ʊ�����͡���NC�ֶ�-def56��,���յ���zdy008-2020-05-08-̸�ӽ�
		 * -start
		 */
		String conttype = headvo.getConttype();
		hvo.setAttributeValue("def56", getNoteType(conttype));// �Զ�����8
		/**
		 * �ɱ�Ӧ�����ӿڣ��Զ����ݺ�ͬ���ʹ���Ӧ������ͷ������Ʊ�����͡���NC�ֶ�-def56��,���յ���zdy008-2020-05-08-̸�ӽ�
		 * -end
		 */
		// ��ͬϸ��->��ͬϸ��
		// ��ͬ����->��ͬ����
		hvo.setAttributeValue("def8", headvo.getContcell());// �Զ�����8 ��ͬϸ��->��ͬϸ��
		// �����̶�->�����̶�2020-02-18-tzj
		String emergency = headvo.getEmergency();
		String def9 = getEmergency(emergency);
		hvo.setAttributeValue("def9", def9);// �Զ�����9
		hvo.setAttributeValue("def10", "EBS");// �Զ�����10 ��Դ�ⲿϵͳ
		// hvo.setAttributeValue("def11", headvo.getMny_actual());// �Զ�����11 �����
		// hvo.setAttributeValue("def14", headvo.getMny_abs());// �Զ�����14 abs֧�����

		if (StringUtils.isNotBlank(headvo.getPlate())) {
			String pk_plate = getdefdocBycode(headvo.getPlate(), "bkxx");
			if (pk_plate == null) {
				throw new BusinessException("��顾" + headvo.getPlate()
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def15", pk_plate);// �Զ�����15 ���
		}

		if (StringUtils.isNotBlank(headvo.getAccorg())) {
			String pk_accorg = getPk_orgByCode(headvo.getAccorg());
			if (pk_accorg == null) {
				throw new BusinessException("���˹�˾��" + headvo.getAccorg()
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def17", pk_accorg);// �Զ�����17
			// ���˹�˾->NCҵ��Ԫ����
		}
		// ������Ӧ��2019-11-01-tzj
		String pk_supplier = getSupplierIDByCode(headvo.getSupplier(),
				hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("��Ӧ�̡�" + headvo.getSupplier()
					+ "��δ����NC������ѯ�������Ϣ");
		}
		hvo.setAttributeValue("supplier", pk_supplier);// ��Ӧ��

		// hvo.setPk_busitype(null);//��������
		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������

		if (StringUtils.isNotBlank(headvo.getProejctdata())) {
			String pk_projectdata = getpk_projectByCode(headvo.getProejctdata());
			if (pk_projectdata == null) {
				throw new BusinessException("��Ŀ��" + headvo.getProejctdata()
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def19", pk_projectdata);// ��Ŀ
			// Ӧ�����ɴ�����Ŀ��Ϣ�����Ƿ��ʱ�(def8),��Ŀ����(def9)
			HashMap<String, Object> projectData = getProjectDataByCode(headvo
					.getProejctdata());
			if (projectData != null) {
				hvo.setAttributeValue("def61", projectData.get("def8"));// �Ƿ��ʱ�
				hvo.setAttributeValue("def62", projectData.get("def9"));// ��Ŀ����
			}
		}

		if (StringUtils.isNotBlank(headvo.getProejctstages())) {
			String pk_proejctstages = getpk_projectByCode(headvo
					.getProejctstages());
			if (pk_proejctstages == null) {
				throw new BusinessException("��Ŀ���� ��"
						+ headvo.getProejctstages() + "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def32", pk_proejctstages);// �Զ�����32 ��Ŀ����
		}
		hvo.setAttributeValue("money", headvo.getBillamount());// ����Ʊ�ݽ��
		// hvo.setAttributeValue("def21", headvo.getTotalmny_request());//
		// �ۼ������
		// hvo.setAttributeValue("def22", headvo.getTotalmny_pay());// �ۼƸ�����
		// hvo.setAttributeValue("def23", headvo.getIsshotgun());// �Ƿ��ȸ����Ʊ
		// hvo.setAttributeValue("def24", headvo.getTotalmny_paybythisreq());//
		// ��������ۼƸ�����
		hvo.setAttributeValue("def26", headvo.getTotalmny_inv());// �ۼƷ�Ʊ���
		// hvo.setAttributeValue("def28", headvo.getIsdeduction());// �Ƿ����ʱ���۳�
		hvo.setAttributeValue("def31", headvo.getNote());// ˵��
		// hvo.setAttributeValue("def33", headvo.getAuditstate());
		// ������������״̬
		if (StringUtils.isNotBlank(headvo.getAuditstate())) {
			String pk_auditstate = getAuditstateByCode(headvo.getAuditstate());
			if (pk_auditstate == null) {
				throw new BusinessException("������������״̬ ��"
						+ headvo.getAuditstate() + "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def33", pk_auditstate);// �Զ�����32 ��Ŀ����
		}
		hvo.setAttributeValue("def43", headvo.getDef43());// �Ǳ�׼ָ�����def43
		hvo.setAttributeValue("def44", headvo.getDef44());// ������def44
		hvo.setAttributeValue("def45", headvo.getDef45());// �Ѳ�ȫdef45
		hvo.setAttributeValue("def46", headvo.getDept());// ���첿��
		hvo.setAttributeValue("def47", headvo.getPsndoc());// ������
		hvo.setAttributeValue("def50", "N");// Ʊ��Ȩ������
		String def55 = headvo.getDef55();
		if (def55 != null && !"".equals(def55)) {
			hvo.setAttributeValue("def55", def55);// EBS��ʽ-def55
		} else {
			throw new BusinessException("EBS��ʽ����Ϊ��!");
		}

		if (StringUtils.isNotBlank(headvo.getSignorg())) {
			String pk_signorg = getPk_orgByCode(headvo.getSignorg());
			if (pk_signorg == null) {
				throw new BusinessException("ǩԼ��˾��" + headvo.getSignorg()
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def34", pk_signorg);// ǩԼ��˾
		}
		/**
		 * �ɱ�Ӧ������ӽ��㷽ʽ-2020-05-26-̸�ӽ�
		 */
		String pk_balatype = null;
		pk_balatype = getBalatypePkByCode(headvo.getBalatype());
//		if (pk_balatype == null) {
//			throw new BusinessException("���㷽ʽ��" + headvo.getBalatype()
//					+ "��δ����NC������ѯ�������Ϣ!");
//		}
		hvo.setAttributeValue("pk_balatype", pk_balatype);// ���㷽ʽ

	}

	/**
	 * ��������¼��Ϣ
	 * 
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void checkItemNotNull(PayBillItemVO bodyvo)
			throws BusinessException {

		// if (StringUtils.isBlank(bodyvo.getSubjcode())) {
		// throw new BusinessException("��ƿ�Ŀ����Ϊ��");
		// }
		// if (bodyvo.getInoutbusiclass() == null) {
		// throw new BusinessException("��֧��Ŀ����Ϊ��");
		// }
		if (StringUtils.isBlank(bodyvo.getTaxrate())) {
			throw new BusinessException("˰�ʲ���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_notax_cr())) {
			throw new BusinessException("��˰����Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_tax_cr())) {
			throw new BusinessException("˰���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_money_cr())) {
			throw new BusinessException("��˰�ϼƲ���Ϊ��");
		}
		// if (bodyvo.getPaymenttype() == null) {
		// throw new BusinessException("�������Ͳ���Ϊ��");
		// }
		// if (StringUtils.isBlank(bodyvo.getFormat())) {
		// throw new BusinessException("ҵ̬����Ϊ��");
		// }
		// if (StringUtils.isBlank(bodyvo.getFormatratio())) {
		// throw new BusinessException("��������Ϊ��");
		// }
		// if (StringUtils.isBlank(bodyvo.getRecaccount())) {
		// throw new BusinessException("�տ�������кŲ���Ϊ��");
		// }
		if (StringUtils.isBlank(bodyvo.getCostsubject())) {
			throw new BusinessException("�ɱ���Ŀ����Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getInvtype())) {
			throw new BusinessException("Ʊ������(Ʊ��)����Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(bodyvo.getScomment())) { throw new
		 * BusinessException("ժҪ����Ϊ��"); }
		 */
		// if (StringUtils.isBlank(bodyvo.getSupplier())) {
		// throw new BusinessException("��Ӧ�̲���Ϊ��");
		// }
		// if (StringUtils.isBlank(bodyvo.getBudgetsub())) {
		// throw new BusinessException("Ԥ���Ŀ����Ϊ��");
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
		// if (StringUtils.isBlank(headvo.getDept())) {
		// throw new BusinessException("���첿�Ų���Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getPsndoc())) {
		// throw new BusinessException("�����˲���Ϊ��");
		// }
		if (StringUtils.isBlank(headvo.getBillamount())) {
			throw new BusinessException("����Ʊ�ݽ���Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getBalatype())) { throw new
		 * BusinessException("���㷽ʽ����Ϊ��"); }
		 */
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("EBS��������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsbillno())) {
			throw new BusinessException("EBS���ݺŲ���Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getImgno())) {
		// throw new BusinessException("Ӱ����벻��Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getImgstate())) {
		// throw new BusinessException("Ӱ��״̬����Ϊ��");
		// }
		if (StringUtils.isBlank(headvo.getContcode())) {
			throw new BusinessException("��ͬ���벻��Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getContname())) {
			throw new BusinessException("��ͬ���Ʋ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getConttype())) {
			throw new BusinessException("��ͬ���Ͳ���Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getContcell())) {
		// throw new BusinessException("��ͬϸ�಻��Ϊ��");
		// }
		// // if (StringUtils.isBlank(headvo.getEmergency())) {
		// throw new BusinessException("�����̶Ȳ���Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getMny_actual())) {
		// throw new BusinessException("�����/�������Ϊ��");
		// }
		/*
		 * if (StringUtils.isBlank(headvo.getMny_abs())) { throw new
		 * BusinessException("abs֧������Ϊ��"); }
		 */
		if (StringUtils.isBlank(headvo.getPlate())) {
			throw new BusinessException("��鲻��Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getAccorg())) {
		// throw new BusinessException("���˹�˾����Ϊ��");
		// }
		if (StringUtils.isBlank(headvo.getSupplier())) {
			throw new BusinessException("��Ӧ�̲���Ϊ��");
		}
		if (headvo.getProejctdata() == null) {
			throw new BusinessException("��Ŀ����Ϊ��");
		}
		if (headvo.getProejctstages() == null) {
			throw new BusinessException("��Ŀ���ڲ���Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getTotalmny_request())) {
		// throw new BusinessException("�ۼ�������Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getTotalmny_pay())) {
		// throw new BusinessException("�ۼƸ������Ϊ��");
		// }
		/*
		 * if (StringUtils.isBlank(headvo.getIsshotgun())) { throw new
		 * BusinessException("�Ƿ��ȸ����Ʊ����Ϊ��"); }
		 */
		// if (StringUtils.isBlank(headvo.getTotalmny_paybythisreq())) {
		// throw new BusinessException("��������ۼƸ������Ϊ��");
		// }
		if (StringUtils.isBlank(headvo.getTotalmny_inv())) {
			throw new BusinessException("�ۼƷ�Ʊ����Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getIsdeduction())) {
		// throw new BusinessException("�Ƿ����ʱ���۳�����Ϊ��");
		// }
		if (StringUtils.isBlank(headvo.getAuditstate())) {
			throw new BusinessException("������������״̬����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getBilldate())) {
			throw new BusinessException("�������ڲ���Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getSignorg())) {
		// throw new BusinessException("ǩԼ��˾����Ϊ��");
		// }

	}

	/**
	 * ���ݳɱ���ĿPK��ȡ��֧��Ŀ�ͻ�ƿ�ĿPK
	 * 
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getPk_subcode(String pk_costsubject)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT distinct f.factorvalue1 as pk_subjcode,f.factorvalue2 as subjcode  ");
		query.append("  FROM fip_docview_b f  ");
		query.append(" WHERE NVL(dr, 0) = 0  ");
		query.append("   AND factorvalue1 = '" + pk_costsubject + "'  ");
		query.append("   AND pk_classview = (SELECT pk_classview  ");
		query.append("                         FROM fip_docview  ");
		query.append("                        WHERE viewcode = 'CB03'  ");
		query.append("                          AND NVL(dr, 0) = 0)  ");

		Map<String, Object> pks = (Map<String, Object>) getBaseDAO()
				.executeQuery(query.toString(), new MapProcessor());
		return pks;
	}

	/**
	 * ����emergency�����ȡ�����̶�����2020-02-18-̸�ӽ�
	 * 
	 * @throws BusinessException
	 */
	private String getEmergency(String emergency) throws BusinessException {
		String pk_defdoc = "";
		StringBuffer query = new StringBuffer();
		query.append("select d.pk_defdoc  ");
		query.append("  from bd_defdoclist l  ");
		query.append("  left join bd_defdoc d  ");
		query.append("    on l.pk_defdoclist = d.pk_defdoclist  ");
		query.append(" where d.code = '" + emergency + "'  ");
		query.append("   and l.code = 'zdy031'  ");
		query.append("   and d.enablestate = 2  ");
		query.append("   and l.dr = 0  ");
		query.append("   and d.dr = 0  ");

		pk_defdoc = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_defdoc;

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
	private String getDeductibleTax(PayBillItemVO bodyvo)
			throws BusinessException {
		String deductibleTax = "0";
		String invtype = bodyvo.getInvtype();
		String local_tax_cr = bodyvo.getLocal_tax_cr();
		if ("01".equals(invtype) || "08".equals(invtype)
				|| "10".equals(invtype) || "11".equals(invtype)
				|| "15".equals(invtype) || "18".equals(invtype)
				|| "19".equals(invtype)) {
			deductibleTax = local_tax_cr;
		}

		if ("04".equals(invtype) || "05".equals(invtype)
				|| "07".equals(invtype) || "16".equals(invtype)
				|| "17".equals(invtype)) {
			StringBuffer query = new StringBuffer();
			query.append("select d.memo  ");
			query.append("  from bd_defdoclist c, bd_defdoc d  ");
			query.append(" where c.pk_defdoclist = d.pk_defdoclist  ");
			query.append("   and c.code = 'pjlx'  ");
			query.append("   and d.code = '" + invtype + "'  ");
			query.append("   and nvl(c.dr, 0) = 0  ");
			query.append("   and nvl(d.dr, 0) = 0  ");
			query.append("   and d.enablestate = '2'  ");
			String calculation = (String) getBaseDAO().executeQuery(
					query.toString(), new ColumnProcessor());
			String amount = bodyvo.getLocal_money_cr();
			String sqlstr = calculation.toString().replace("amount", amount);
			String sql = "select " + sqlstr + " from dual ";
			Object Tax = getBaseDAO().executeQuery(sql, new ColumnProcessor());
			deductibleTax = Tax.toString();
		}

		return deductibleTax;
	}
}
