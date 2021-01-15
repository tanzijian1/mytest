package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.appaybill.PayBillHeaderVO;
import nc.vo.tg.outside.appaybill.PayBillItemVO;

import org.apache.commons.lang.StringUtils;

/*
 *  EBS-ͨ���ղ������->Ӧ����
 *  
 */
public class CollectContTranBillUtils extends BillTranUtils {

	static CollectContTranBillUtils utils;

	public static CollectContTranBillUtils getUtils() {
		if (utils == null) {
			utils = new CollectContTranBillUtils();
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

		// itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
		// headVO.getPk_deptid());// ����
		// itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
		// headVO.getPk_deptid_v());// �� ��
		// itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
		// headVO.getPk_psndoc());// ҵ��Ա

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
		if (StringUtils.isBlank(bodyvo.getScomment())) {
			itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
					bodyvo.getScomment());// ժҪ
		}

		/*
		 * itemVO.setAttributeValue(PayableBillItemVO.INVOICENO,
		 * bodyvo.getInvoiceno());// ��Ʊ��
		 * itemVO.setAttributeValue(PayableBillItemVO.DEF10,
		 * bodyvo.getOffsetcompany());// �ֳ���˹�˾
		 */

		if (headVO.getDef18() == null) {
			throw new BusinessException("ʵ���տ����Ϊ��");
		} else {
			itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER,
					headVO.getDef18());// ��Ӧ��
		}

		if (StringUtils.isNotBlank(bodyvo.getBudgetsub())) {
			String pk_budgetsub = getBudgetsubByCode(bodyvo.getBudgetsub());
			if (pk_budgetsub == null) {
				throw new BusinessException("��Ԥ���Ŀ" + bodyvo.getBudgetsub()
						+ "��δ����NC������ѯ�������Ϣ");
			}
			itemVO.setAttributeValue(PayableBillItemVO.DEF1, pk_budgetsub);
			// EBS��ͨ��Ӧ����ͨ��Ԥ���Ŀ�����ѯ��֧��Ŀ����2020-04-16-̸�ӽ�
			String pk_subjcode = getPksubjcodeByCode(bodyvo.getBudgetsub());
			itemVO.setAttributeValue(PayableBillItemVO.PK_SUBJCODE, pk_subjcode);
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
		/*
		 * if (bodyvo.getInoutbusiclass() == null) { throw new
		 * BusinessException("��֧��Ŀ����Ϊ��!"); } String pk_inoutbusicalss =
		 * getInoutPkByCode(bodyvo.getInoutbusiclass()); if (pk_inoutbusicalss
		 * == null) { throw new BusinessException("��֧��Ŀ[" +
		 * bodyvo.getInoutbusiclass() + "]δ����NC���������������Ϣ!"); }
		 * itemVO.setAttributeValue(PayableBillItemVO.PK_SUBJCODE,
		 * pk_inoutbusicalss);// ��֧��Ŀ
		 */

		if (StringUtils.isNotBlank(bodyvo.getPaymenttype())) {
			String pk_paymenttype = getdefdocBycode(bodyvo.getPaymenttype(),
					"zdy020");
			if (pk_paymenttype == null) {
				throw new BusinessException("��������[" + bodyvo.getPaymenttype()
						+ "]δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.DEF2, pk_paymenttype);// ��������
		}

		if (StringUtils.isNotBlank(bodyvo.getFormat())) {
			String pk_format = getdefdocBycode(bodyvo.getFormat(), "ys004");
			if (pk_format == null) {
				throw new BusinessException("ҵ̬[" + bodyvo.getFormat()
						+ "]δ����NC����������!");
			}
			itemVO.setAttributeValue("def3", pk_format);// �Զ�����3 ҵ̬
		}
		itemVO.setAttributeValue("def4", bodyvo.getFormatratio());// �Զ�����4 ҵ̬����

		if (StringUtils.isNotBlank(bodyvo.getRecaccount())) {
			String pk_recaccount = getAccountIDByCode(bodyvo.getRecaccount(),
					itemVO.getSupplier());
			if (pk_recaccount == null) {
				throw new BusinessException("�տ��˻�[" + bodyvo.getRecaccount()
						+ "]δ����NC����������!");
			}
			itemVO.setAttributeValue("def5", pk_recaccount);//
		}
		if (StringUtils.isNotBlank(bodyvo.getCostsubject())) {
			String pk_costsubject = getdefdocBycode(bodyvo.getCostsubject(),
					"zdy024");
			if (pk_costsubject == null) {
				throw new BusinessException("�ɱ���Ŀ[" + bodyvo.getCostsubject()
						+ "]δ����NC����������!");
			}
			itemVO.setAttributeValue("def7", pk_costsubject);//
		}

		if (StringUtils.isNotBlank(bodyvo.getInvtype())) {
			String pk_costsubject = getdefdocBycode(bodyvo.getInvtype(), "pjlx");
			if (pk_costsubject == null) {
				throw new BusinessException("Ʊ������[" + bodyvo.getInvtype()
						+ "]δ����NC����������!");
			}
			itemVO.setAttributeValue("def8", pk_costsubject);
		}
		/**
		 * ͨ��Ӧ������ӽ��㷽ʽ-2020-06-04-̸�ӽ�
		 */
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// ���㷽ʽ

		// ͨ��Ӧ��������def15�ɵֿ�˰����ݷ�Ʊ���������¼���-2020��7��22��-huangxj
		String deductibleTax = getDeductibleTax(bodyvo);
		itemVO.setAttributeValue("def15", deductibleTax);

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
		// ������
		hvo.setAttributeValue("def59", headvo.getDef59());
		// ���첿��
		hvo.setAttributeValue("def60", headvo.getDef60());
		// ��ӱ���2019-11-04-tzj
		String pk_currtype = headvo.getPk_currtype();
		pk_currtype = checkPk_currtype(pk_currtype);
		hvo.setPk_currtype(pk_currtype);

		// String pk_deptid = getPk_DeptByCode(headvo.getDept(),
		// hvo.getPk_org());// ����
		// if (pk_deptid == null) {
		// throw new BusinessException("���š�" + headvo.getDept()
		// + "��δ����NC������ѯ�������Ϣ");
		// }
		// hvo.setAttributeValue(PayableBillVO.PK_DEPTID, pk_deptid);// ����
		//
		// String pk_psnodc = getPsndocPkByCode(headvo.getPsndoc());
		// if (pk_psnodc == null) {
		// throw new BusinessException("�����ˡ�" + headvo.getPsndoc()
		// + "��δ����NC������ѯ�������Ϣ");
		// }
		// hvo.setAttributeValue(PayableBillVO.PK_PSNDOC, pk_psnodc);// ҵ��Ա

		/*
		 * String pk_balatype = getBalatypePkByCode(headvo.getBalatype()); if
		 * (pk_balatype == null) { throw new BusinessException("���㷽ʽ��" +
		 * headvo.getBalatype() + "��δ����NC������ѯ�������Ϣ!"); }
		 * hvo.setAttributeValue(PayableBillVO.PK_BALATYPE, pk_balatype);// ���㷽ʽ
		 */
		hvo.setAttributeValue("money", headvo.getBillamount());// ����Ʊ�ݽ��
		hvo.setAttributeValue("def1", headvo.getEbsid());// �Զ�����1 ��ϵͳ����
		hvo.setAttributeValue("def2", headvo.getEbsbillno());// �Զ�����2
		hvo.setAttributeValue("def3", headvo.getImgno());// �Զ�����3 Ӱ�����->Ӱ�����
		hvo.setAttributeValue("def4", headvo.getImgstate() == null ? "" : "��ɨ��");// �Զ�����4
																					// Ӱ��״̬->Ӱ��״̬
		hvo.setAttributeValue("def5", headvo.getContcode());// �Զ�����5 ��ͬ����->��ͬ����
		hvo.setAttributeValue("def6", headvo.getContname());// ��ͬ����->��ͬ���� def6
		hvo.setAttributeValue("def7", headvo.getConttype());// �Զ�����7
		// ��ͬ����->��ͬ����
		hvo.setAttributeValue("def8", headvo.getContcell());// �Զ�����8 ��ͬϸ��->��ͬϸ��
		// hvo.setAttributeValue("def9", headvo.getEmergency());// �Զ�����9
		// �����̶�->�����̶�
		hvo.setAttributeValue("def10", "EBS");// �Զ�����10 ��Դ�ⲿϵͳ

		hvo.setAttributeValue("def11", headvo.getMny_actual());// �Զ�����11 �����

		if (StringUtils.isNotBlank(headvo.getMedandlongpro())) {
			// String pk_medandlongpro = getpk_projectByCode(headvo
			// .getMedandlongpro());
			// if (pk_medandlongpro == null) {
			// throw new BusinessException("�г�����Ŀ��"
			// + headvo.getMedandlongpro() + "��δ����NC���������������Ϣ!");
			// }
			hvo.setAttributeValue("def12", headvo.getMedandlongpro());// �г�����Ŀ
			hvo.setAttributeValue("def62",
					getProjectNatureByName(headvo.getMedandlongpro()));// ��Ŀ����
		}
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
		// hvo.setPk_busitype(null);//��������
		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������

		String pk_supplier = getSupplierIDByCode(headvo.getSupplier(),
				hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("�տ��" + headvo.getSupplier()
					+ "��δ����NC������ѯ�������Ϣ");
		}
		hvo.setAttributeValue("def18", pk_supplier);// ��Ӧ��

		/*
		 * if (StringUtils.isNotBlank(headvo.getProejctdata())) { String
		 * pk_projectdata = getpk_projectByCode(headvo.getProejctdata()); if
		 * (pk_projectdata == null) { throw new BusinessException("��Ŀ��" +
		 * headvo.getProejctdata() + "��δ����NC���������������Ϣ!"); }
		 * hvo.setAttributeValue("def19", pk_projectdata);// ��Ŀ }
		 * hvo.setAttributeValue("def19", headvo.getProejctdata());// �Զ�����18 ��Ŀ
		 */
		if (StringUtils.isNotBlank(headvo.getProejctstages())) {
			String pk_proejctstages = getpk_projectByCode(headvo
					.getProejctstages());
			if (pk_proejctstages == null) {
				throw new BusinessException("��Ŀ��" + headvo.getProejctstages()
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def32", pk_proejctstages);// �Զ�����32 ��Ŀ����
		}

		if (StringUtils.isNotBlank(headvo.getTotalmny_request())) {

			hvo.setAttributeValue("def21", headvo.getTotalmny_request());// �ۼ������
		}
		if (StringUtils.isNotBlank(headvo.getTotalmny_pay())) {
			hvo.setAttributeValue("def22", headvo.getTotalmny_pay());// �ۼƸ�����
		}
		if (StringUtils.isNotBlank(headvo.getIsshotgun())) {

			hvo.setAttributeValue("def23", headvo.getIsshotgun());// �Ƿ��ȸ����Ʊ
		}
		if (StringUtils.isNotBlank(headvo.getTotalmny_inv())) {

			hvo.setAttributeValue("def26", headvo.getTotalmny_inv());// �ۼƷ�Ʊ���
		}
		if (StringUtils.isNotBlank(headvo.getNote())) {

			hvo.setAttributeValue("def31", headvo.getNote());// ˵��
		}
		// hvo.setAttributeValue("def24", headvo.getTotalmny_paybythisreq());//
		// ��������ۼƸ�����

		// hvo.setAttributeValue("def28", headvo.getIsdeduction());// �Ƿ����ʱ���۳�

		String pleasetype = headvo.getPleasetype();
		if (StringUtils.isNotBlank(pleasetype)) {
			String pk_defdoc = getAuditstateByCode(pleasetype);
			if (pk_defdoc == null) {
				throw new BusinessException("������͡�" + pleasetype
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def48", pk_defdoc);
		}
		// hvo.setAttributeValue("def33", headvo.getAuditstate());// ������������״̬
		// ����code��ȡ������������״̬2020-03-27-̸�ӽ�
		String auditstate = headvo.getAuditstate();
		if (StringUtils.isNotBlank(auditstate)) {
			String pk_defdoc = getAuditstateByCode(auditstate);
			if (pk_defdoc == null) {
				throw new BusinessException("������������״̬��" + auditstate
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def33", pk_defdoc);// ������������״̬
		}
		hvo.setAttributeValue("def55", headvo.getDef55()); // EBS��ʽ-def55

		/**
		 * ͨ��Ӧ������ӽ��㷽ʽ-2020-06-04-̸�ӽ�
		 */
		String pk_balatype = null;
		pk_balatype = getBalatypePkByCode(headvo.getBalatype());
		// if (pk_balatype == null) {
		// throw new BusinessException("���㷽ʽ��" + headvo.getBalatype()
		// + "��δ����NC������ѯ�������Ϣ!");
		// }
		hvo.setAttributeValue("pk_balatype", pk_balatype);// ���㷽ʽ

		/*
		 * if (StringUtils.isNotBlank(headvo.getSignorg())) { String pk_signorg
		 * = getPk_orgByCode(headvo.getSignorg()); if (pk_signorg == null) {
		 * throw new BusinessException("ǩԼ��˾��" + headvo.getSignorg() +
		 * "��δ����NC���������������Ϣ!"); } hvo.setAttributeValue("def34", pk_signorg);//
		 * ǩԼ��˾ }
		 */

		// hvo.setAttributeValue("def9", getdefdocBycode("02", "zdy008"));//
		// ����Ʊ������

	}

	/**
	 * ��������¼��Ϣ
	 * 
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void checkItemNotNull(PayBillItemVO bodyvo)
			throws BusinessException {
		// if (StringUtils.isBlank(bodyvo.getBudgetsub())) {
		// throw new BusinessException("Ԥ���Ŀ����Ϊ��");
		// }

		if (StringUtils.isBlank(bodyvo.getTaxrate())) {
			throw new BusinessException("˰�ʲ���Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(bodyvo.getLocal_notax_cr())) { throw new
		 * BusinessException("��˰����Ϊ��"); }
		 */
		if (StringUtils.isBlank(bodyvo.getLocal_tax_cr())) {
			throw new BusinessException("˰���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_money_cr())) {
			throw new BusinessException("Ʊ�ݽ���Ϊ��");
		}

		if (StringUtils.isBlank(bodyvo.getPaymenttype())) {
			throw new BusinessException("�������Ͳ���Ϊ��");
		}

		// if (StringUtils.isBlank(bodyvo.getFormatratio())) {
		// throw new BusinessException("��������Ϊ��");
		// }

		if (StringUtils.isBlank(bodyvo.getInvtype())) {
			throw new BusinessException("Ʊ�����ͣ�Ʊ�֣�����Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(bodyvo.getScomment())) { throw new
		 * BusinessException("ժҪ����Ϊ��"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getInvoiceno())) { throw new
		 * BusinessException("��Ʊ�Ų���Ϊ��"); } if
		 * (StringUtils.isBlank(bodyvo.getOffsetcompany())) { throw new
		 * BusinessException("�ֳ���˹�˾����Ϊ��"); } if
		 * (StringUtils.isBlank(bodyvo.getSupplier())) { throw new
		 * BusinessException("��Ӧ�̲���Ϊ��"); }
		 */
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
			throw new BusinessException("���˹�˾����Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getDept())) {
		// throw new BusinessException("���첿�Ų���Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getPsndoc())) {
		// throw new BusinessException("�����˲���Ϊ��");
		// }
		/*
		 * if (StringUtils.isBlank(headvo.getBillamount())) { throw new
		 * BusinessException("����Ʊ�ݽ���Ϊ��"); }
		 */if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("��ϵͳ��������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsbillno())) {
			throw new BusinessException("��ϵͳ���ݺŲ���Ϊ��");
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
		if (StringUtils.isBlank(headvo.getContcell())) {
			throw new BusinessException("��ͬϸ�಻��Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getMny_actual())) {
			throw new BusinessException("������Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getMedandlongpro())) { throw new
		 * BusinessException("�г�����Ŀ����Ϊ��"); }
		 */
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
			throw new BusinessException("�տ����Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getTotalmny_request())) {
		// throw new BusinessException("�ۼ�������Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getTotalmny_pay())) {
		// throw new BusinessException("�ۼƸ������Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getIsshotgun())) {
		// throw new BusinessException("�Ƿ��ȸ����Ʊ����Ϊ��");
		// }
		/*
		 * if (StringUtils.isBlank(headvo.getTotalmny_paybythisreq())) { throw
		 * new BusinessException("��������ۼƸ������Ϊ��"); }
		 */
		// if (StringUtils.isBlank(headvo.getTotalmny_inv())) {
		// throw new BusinessException("�ۼƷ�Ʊ����Ϊ��");
		// }

		// if (StringUtils.isBlank(headvo.getNote())) {
		// throw new BusinessException("˵������Ϊ��");
		// }

		if (StringUtils.isBlank(headvo.getBilldate())) {
			throw new BusinessException("�������ڲ���Ϊ��");
		}

		if (StringUtils.isBlank(headvo.getAuditstate())) {
			throw new BusinessException("������������״̬����Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getSignorg())) { throw new
		 * BusinessException("ǩԼ��˾����Ϊ��"); } if
		 * (StringUtils.isBlank(headvo.getIsimgdefect())) { throw new
		 * BusinessException("�Ƿ�ȱʧӰ�񲻿�Ϊ��"); } if
		 * (StringUtils.isBlank(headvo.getIsdeduction())) { throw new
		 * BusinessException("�Ƿ����ʱ���۳�����Ϊ��"); }
		 */
	}

	/**
	 * ͨ��Ӧ��������def15�ɵֿ�˰����ݷ�Ʊ���������¼���-2020��7��22��-huangxj
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
