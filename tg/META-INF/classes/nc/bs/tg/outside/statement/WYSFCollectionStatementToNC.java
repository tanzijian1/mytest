package nc.bs.tg.outside.statement;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.cmp.bill.RecBillAggVO;
import nc.vo.cmp.bill.RecBillDetailVO;
import nc.vo.cmp.bill.RecBillVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.LLCollectionStatementJsonBVO;
import nc.vo.tg.outside.LLCollectionStatementJsonVO;

public class WYSFCollectionStatementToNC extends CollectionStatementUtils {

	protected RecBillAggVO onTranBill(LLCollectionStatementJsonVO headVO,
			List<LLCollectionStatementJsonBVO> bodyVOs)
			throws BusinessException {
		nc.vo.cmp.bill.RecBillAggVO aggVO = new RecBillAggVO();
		RecBillVO save_headVO = new RecBillVO();
		/**
		 * ��ҵ�շ�ϵͳ�������ֶ�-2020-11-23-̸�ӽ�-start
		 */
		OrgVO orgvo = getOrgVO(headVO.getPk_org());
		if (orgvo == null || "".equals(orgvo)) {
			throw new BusinessException("������֯pk_org:" + headVO.getPk_org()
					+ "��NC�Ҳ�������������!");
		}
		save_headVO.setPk_org(orgvo.getPk_org());// ������֯
		save_headVO.setPk_org_v(orgvo.getPk_vid());// ������֯�汾
		save_headVO.setBill_date(new UFDate(headVO.getBilldate()));// ��������
		save_headVO.setPk_tradetypeid(getPk_tradetype(headVO
				.getPk_tradetypeid()));// ��������id
		save_headVO.setTrade_type(headVO.getPk_tradetypeid());// ��������
		save_headVO.setObjecttype(0); // ���׶�������
		// save_headVO.setLocal_money(new UFDouble(headVO.getLocal_money()));//
		// �ϼƽ��
		// save_headVO.setPrimal_money(new UFDouble(headVO.getLocal_money()));//
		// �ϼƽ��
		save_headVO.setContractno(headVO.getContractno());// ��ͬ��
		save_headVO.setMail(headVO.getMailbox());// �����������
		save_headVO.setDef4(headVO.getImgcode());// Ӱ�����
		save_headVO.setDef1(headVO.getSrcid());// ��ϵͳ����
		save_headVO.setDef2(headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setPk_busiflow(getBusitypeByCode("cmp401",
				"000112100000000005FD"));// ҵ������

		/**
		 * ��ҵ�շ�ϵͳ�������ֶ�-2020-11-23-̸�ӽ�-end
		 */
		save_headVO.setSource_flag("2");
		save_headVO.setBillmaker_date(new UFDate());
		save_headVO.setCreationtime(new UFDateTime());
		save_headVO.setBill_status(-1);// ����״̬
		save_headVO.setEffect_flag(0);// ��Ч״̬
		save_headVO.setPk_group("000112100000000005FD");// ��������
		save_headVO.setBillclass("sj");
		save_headVO.setBillmaker(getUserIDByCode(WYSFDefaultOperator));
		save_headVO.setCreator(getUserIDByCode(WYSFDefaultOperator));
		save_headVO.setBill_type("F4");
		save_headVO.setPk_billtypeid(getPk_billtypeid("F4"));
		save_headVO.setStatus(VOStatus.NEW);

		List<RecBillDetailVO> bodylist = new ArrayList<>();
		for (LLCollectionStatementJsonBVO bodyVO : bodyVOs) {
			RecBillDetailVO save_bodyVO = new RecBillDetailVO();
			/**
			 * ��ҵ�շ�ϵͳ�������ֶα�����Ϣ-2020-11-23-̸�ӽ�-start
			 * nc.vo.cmp.bill.RecBillDetailVO
			 */
			save_bodyVO.setMemo(bodyVO.getMemo());
			String customer = getCustomerByCode(bodyVO.getPk_customer());
			save_bodyVO.setPk_customer(customer);
			save_bodyVO.setPk_balatype(getBalatypePkByCode(bodyVO
					.getPk_balatype()));// ���㷽ʽ
			String accountId = getAccountIDByCode(orgvo.getPk_org(),
					bodyVO.getAccnum());
			if (accountId == null || "".equals(accountId)) {
				throw new BusinessException("�ڹ�Ӧ��pk_org��" + headVO.getPk_org()
						+ "���²����������˺�:" + bodyVO.getAccnum() + "��������!");
			}
			save_bodyVO.setPk_account(accountId);

			save_bodyVO.setDef16(bodyVO.getTaxrate()); // ˰��
			save_bodyVO.setDef17(bodyVO.getLocal_tax_cr()); // ˰��
			save_bodyVO.setDef18(bodyVO.getNotax_cr()); // ��˰���
			save_bodyVO.setRec_local(new UFDouble(bodyVO.getRec_local())); // �տ���֯���ҽ��
			save_bodyVO.setRec_primal(new UFDouble(bodyVO.getRec_local())); // �տ�ԭ�ҽ��
			save_bodyVO.setPk_jobid(getProject(bodyVO.getProject()));// ��Ŀ
			save_bodyVO
					.setDef3(getdefdocBycode(bodyVO.getItemtype(), "SDLL008"));// �շ���Ŀ����
			save_bodyVO
					.setDef4(getItemnameByPk(bodyVO.getItemname(), "SDLL008"));// �շ���Ŀ����
			save_bodyVO.setDef5(getdefdocBycode(bodyVO.getProjectphase(),
					"SDLL006"));// ��Ŀ�׶�
			save_bodyVO.setDef6(getdefdocBycode(bodyVO.getProperties(),
					"SDLL007"));// ��Ŀ����
			save_bodyVO.setDef7(getdefdocBycode(bodyVO.getPaymenttype(),
					"SDLL010"));// ��������

			String propertycode = bodyVO.getPropertycode(); // ��������
			String propertyname = bodyVO.getPropertyname();// ��������

			if (propertycode != null && !"".equals(propertycode)
					&& propertyname != null && !"".equals(propertyname)) {
				String pk_defdoc = saveHousePropertyArchives(propertycode,
						propertyname, orgvo.getPk_org());
				if (pk_defdoc != null && !"".equals(pk_defdoc)) {
					save_bodyVO.setDef8(propertycode); // ��������
					save_bodyVO.setDef9(pk_defdoc);// ��������
				}

			}
			save_bodyVO.setDef10(bodyVO.getSubordinateyear());// Ӧ�յ���������
			save_bodyVO
					.setDef11(getdefdocBycode(bodyVO.getArrears(), "SDLL002"));// ���Ƿ�����Ƿ�ѡ�
			save_bodyVO.setDef12(bodyVO.getRowid());// ��ҵ�շ�ϵͳ������ID

			String subordiperiod = bodyVO.getSubordiperiod();// Ӧ�չ�����
			if (null != subordiperiod && !"".equals(subordiperiod)
					&& subordiperiod.contains("-")) {
				// ����ͱ��嵥�����ڲ�һ����ȡ�·ݵĵ�һ��
				int year = Integer.parseInt(subordiperiod.substring(0,
						subordiperiod.indexOf("-")));// ��ȡ���
				int month = Integer
						.parseInt(subordiperiod.substring(
								subordiperiod.indexOf("-") + 1,
								subordiperiod.length()));// ��ȡ�·�
				subordiperiod = getFirstDayOfMonth1(year, month);
				save_bodyVO.setDef13(subordiperiod);// Ӧ�չ�����
			} else {
				throw new BusinessException(
						"���鵥��Ӧ�չ�����subordiperiod�ֶ��Ƿ�Ϊ�ջ��ʽ�Ƿ���ȷ!");
			}

			String actualnateyear = bodyVO.getActualnateyear();// ʵ����������-����
			// ����ͱ��嵥������һ����ȡ��������
			if (actualnateyear != null && !"".equals(actualnateyear)
					&& actualnateyear.contains("-")) {
				if (headVO.getBilldate() != null
						&& headVO.getBilldate().startsWith(actualnateyear)) {
					save_bodyVO.setDef14(headVO.getBilldate());// ʵ����������
				} else {
					// ����ͱ��嵥�����ڲ�һ����ȡ�·ݵĵ�һ��
					int year = Integer.parseInt(actualnateyear.substring(0,
							actualnateyear.indexOf("-")));// ��ȡ���
					int month = Integer.parseInt(actualnateyear.substring(
							actualnateyear.indexOf("-") + 1,
							actualnateyear.length()));// ��ȡ�·�
					actualnateyear = getFirstDayOfMonth1(year, month);
					save_bodyVO.setDef14(actualnateyear);// ʵ����������
				}
			} else {
				throw new BusinessException(
						"���鵥��ʵ����������actualnateyear�ֶ��Ƿ�Ϊ�ջ��ʽ�Ƿ���ȷ!");
			}
			// ʵ�չ�����
			String actualperiod = bodyVO.getActualperiod();// Ӧ�չ�����
			if (null != actualperiod && !"".equals(actualperiod)
					&& actualperiod.contains("-")) {
				// ����ͱ��嵥�����ڲ�һ����ȡ�·ݵĵ�һ��
				int year = Integer.parseInt(actualperiod.substring(0,
						actualperiod.indexOf("-")));// ��ȡ���
				int month = Integer.parseInt(actualperiod.substring(
						actualperiod.indexOf("-") + 1, actualperiod.length()));// ��ȡ�·�
				actualperiod = getFirstDayOfMonth1(year, month);
				save_bodyVO.setDef15(actualperiod);// ʵ�չ�����

			} else {
				throw new BusinessException(
						"���鵥��Ӧ�չ�����actualperiod�ֶ��Ƿ�Ϊ�ջ��ʽ�Ƿ���ȷ!");
			}

			/**
			 * ��ҵ�շ�ϵͳ�������ֶα�����Ϣ-2020-11-23-̸�ӽ�-end nc.vo.cmp.bill.RecBillDetailVO
			 */
			save_bodyVO.setCreationtime(new UFDateTime());
			save_bodyVO.setBill_date(new UFDate(headVO.getBilldate()));
			save_bodyVO.setObjecttype(0);
			save_bodyVO.setPk_group("000112100000000005FD");// ��������
			save_bodyVO.setDirection(1);
			save_bodyVO.setPk_currtype("1002Z0100000000001K1");
			save_bodyVO.setPk_org(orgvo.getPk_org());
			save_bodyVO.setPk_org_v(orgvo.getPk_vid());
			save_bodyVO.setBill_type("F4");
			save_bodyVO.setStatus(VOStatus.NEW);
			save_bodyVO.setBillclass("sj");
			bodylist.add(save_bodyVO);
		}
		if (bodylist.size() > 0) {
			save_headVO.setDef3(bodylist.get(0).getDef14());
		}
		aggVO.setParentVO(save_headVO);
		aggVO.setChildrenVO(bodylist.toArray(new RecBillDetailVO[0]));

		return aggVO;
	}

	/**
	 * ��ȡ������������
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getPk_tradetype(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		String pk_tradetypeid = null;
		pk_tradetypeid = (String) dao.executeQuery(
				"select pk_billtypeid from bd_billtype where pk_billtypecode='"
						+ code + "' and nvl(dr,0)=0", new ColumnProcessor());
		if (pk_tradetypeid == null || "".equals(pk_tradetypeid)) {
			throw new BusinessException("���ݽ�������pk_tradetypeid��" + code
					+ "����NC����������!");
		}

		return pk_tradetypeid;
	}
}
