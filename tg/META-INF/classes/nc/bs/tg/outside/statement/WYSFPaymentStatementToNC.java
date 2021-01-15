package nc.bs.tg.outside.statement;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.cmp.bill.BillAggVO;
import nc.vo.cmp.bill.BillDetailVO;
import nc.vo.cmp.bill.BillVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.LLPaymentStatementJsonBVO;
import nc.vo.tg.outside.LLPaymentStatementJsonVO;

public class WYSFPaymentStatementToNC extends PaymentStatementUtils {

	protected BillAggVO onTranBill(LLPaymentStatementJsonVO headVO,
			List<LLPaymentStatementJsonBVO> bodyVOs) throws BusinessException {
		BillAggVO aggVO = new BillAggVO();
		BillVO save_headVO = new BillVO();
		/**
		 * ��ҵ�շ�ϵͳ�������ֶ�-2020-09-22-̸�ӽ�-start
		 */
		OrgVO orgvo = getOrgVO(headVO.getPk_org());
		if (orgvo == null || "".equals(orgvo)) {
			throw new BusinessException("������֯Ouflag:" + headVO.getPk_org()
					+ "��NC�Ҳ�������������!");
		}
		save_headVO.setPk_org(orgvo.getPk_org());// ������֯
		save_headVO.setAttributeValue("pk_org_v", orgvo.getPk_vid());// ������֯�汾
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// ��ϵͳ����
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setAttributeValue("billdate",
				new UFDate(headVO.getBilldate()));// ��������

		save_headVO.setAttributeValue("local_money", headVO.getLocal_money());// ������֯���ҽ��
		save_headVO.setAttributeValue("def21", headVO.getImgcode());// Ӱ�����
		save_headVO.setAttributeValue("mail", headVO.getMailbox());// �����������
		String pk_tradetype = getPk_tradetype(headVO.getPk_tradetypeid());
		if (pk_tradetype==null||pk_tradetype.equals("")) {
			throw new BusinessException("�������ͱ�����nc��������ϵ�շ�ϵͳ����Ա����");
		}
		save_headVO.setAttributeValue("pk_tradetypeid",
				getPk_billtypeid(headVO.getPk_tradetypeid()));// ��������id
		save_headVO.setAttributeValue("trade_type", headVO.getPk_tradetypeid());// ��������
		save_headVO.setAttributeValue("def32",
				getdefdocBycode(headVO.getBusinesstype(), "SDLL003"));// ҵ������
		save_headVO.setAttributeValue("contractno", headVO.getContractno());// ��ͬ��
		save_headVO.setAttributeValue("memo", headVO.getMemo());// ��ע
		save_headVO.setAttributeValue("pk_busiflow",
				getBusitypeByCode("cmp501", "000112100000000005FD"));

		/**
		 * ��ҵ�շ�ϵͳ�������ֶ�-2020-09-22-̸�ӽ�-end
		 */
		save_headVO.setAttributeValue("source_flag", 2);
		save_headVO.setAttributeValue("bill_date", new UFDateTime());
		save_headVO.setAttributeValue("billmaker_date", new UFDateTime());
		save_headVO.setAttributeValue("creationtime", new UFDateTime());
		save_headVO.setAttributeValue("bill_status", -1);// ����״̬
		save_headVO.setAttributeValue("effect_flag", 0);// ��Ч״̬
		// save_headVO.setAttributeValue("paystatus", 1);// ֧��״̬
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ��������
		save_headVO.setAttributeValue("billclass", "fj");
		save_headVO.setAttributeValue("billmaker",
				getUserIDByCode(WYSFDefaultOperator));
		save_headVO.setAttributeValue("creator",
				getUserIDByCode(WYSFDefaultOperator));
		save_headVO.setBill_type("F5");
		save_headVO.setPk_billtypeid(getPk_billtypeid("F5"));
		save_headVO.setStatus(VOStatus.NEW);

		List<BillDetailVO> bodylist = new ArrayList<>();
		for (LLPaymentStatementJsonBVO bodyVO : bodyVOs) {
			BillDetailVO save_bodyVO = new BillDetailVO();
			/**
			 * ��ҵ�շ�ϵͳ�������ֶα�����Ϣ-2020-09-22-̸�ӽ�-start nc.vo.cmp.bill.BillDetailVO
			 */
			String isfreecust = supplierIsfreecust(bodyVO.getPk_supplier());
			if (null != isfreecust && !"".equals(isfreecust)) {
				// 0=�ͻ���1=��Ӧ�̣�2=���ţ�3=��Ա��4=ɢ��
				if ("Y".equals(isfreecust)) {
					save_bodyVO.setAttributeValue("objecttype", 4);// ���׶�������
					save_headVO.setAttributeValue("objecttype", 4);// ���׶�������
				} else {
					save_bodyVO.setAttributeValue("objecttype", 1);// ���׶�������
					save_headVO.setAttributeValue("objecttype", 1);// ���׶�������
				}

			}
			save_bodyVO.setAttributeValue("memo", bodyVO.getMemo());// ��ע
			save_bodyVO
					.setAttributeValue(
							"pk_supplier",
							getSupplierIDByCode(bodyVO.getPk_supplier(),
									save_headVO.getPk_org(),
									save_headVO.getPk_group()));// ��Ӧ��
			save_bodyVO.setAttributeValue("pk_balatype",
					getBalatypePkByCode(bodyVO.getPk_balatype()));// ���㷽ʽ

			save_bodyVO.setAttributeValue(
					"pk_oppaccount",
					getAccountIDByCode(bodyVO.getPk_oppaccount(),
							save_headVO.getPk_org(), "pk_org"));// ���������˻�
			save_bodyVO.setAttributeValue("def10",
					getBankdocIDByCode(bodyVO.getBd_bankdoc()));// ���λ������
			save_bodyVO.setAttributeValue("pay_local", bodyVO.getPay_local());// ������֯���ҽ��
			save_bodyVO.setAttributeValue("def31", bodyVO.getDeposit());// ��Ѻ����
			save_bodyVO.setAttributeValue("accounttype",
					bodyVO.getAccounttype());// �տ��˻���

			int objecttype = (int) save_bodyVO.getAttributeValue("objecttype");
			// ����ǹ�Ӧ��
			if (objecttype == 1) {
				String account = getAccountIDByCode(bodyVO.getPk_account(),
						save_headVO.getPk_org(), "pk_org");
				if (null != account && !"".equals(account)) {
					save_bodyVO.setAttributeValue("pk_account", account);// �տ������˺�
				} else {
					throw new BusinessException("�տ������˺���NC���ܹ���:"
							+ bodyVO.getPk_account() + "�뵽SRMϵͳ����ά��!");
				}
			} else {
				// �����ɢ��ֱ�ӱ���
				save_bodyVO.setAttributeValue("pk_account",
						bodyVO.getPk_account());// �տ������˺�
			}
			save_bodyVO
					.setAttributeValue("accountcode", bodyVO.getPk_account());// �տ��˻�����
			save_bodyVO.setAttributeValue("accountname",
					bodyVO.getAccountname());// �տ��˻�����
			save_bodyVO.setAttributeValue("accountopenbank",
					bodyVO.getAccountopenbank());// �տ���������
			save_bodyVO.setAttributeValue("def32", bodyVO.getRowid());// ��ҵ�շ�ϵͳ������ID
			save_bodyVO.setAttributeValue("def33",
					getProject(bodyVO.getProject()));// ��Ŀ
			save_bodyVO.setAttributeValue("def34",
					getdefdocBycode(bodyVO.getArrears(), "SDLL002"));// ���Ƿ�����Ƿ�ѡ�
			save_bodyVO.setAttributeValue("def35",
					getdefdocBycode(bodyVO.getBusinessbreakdown(), "SDLL004"));// ҵ��ϸ��
			save_bodyVO.setAttributeValue("def36",
					getdefdocBycode(bodyVO.getItemtype(), "SDLL008"));// �շ���Ŀ����
			save_bodyVO.setAttributeValue("def37",
					getItemnameByPk(bodyVO.getItemname(), "SDLL008"));// �շ���Ŀ����
			save_bodyVO.setAttributeValue("def38",
					getdefdocBycode(bodyVO.getProjectphase(), "SDLL006"));// ��Ŀ�׶�
			save_bodyVO.setAttributeValue("def39",
					getdefdocBycode(bodyVO.getProperties(), "SDLL007"));// ��Ŀ����
			save_bodyVO.setAttributeValue("def40",
					getdefdocBycode(bodyVO.getPaymenttype(), "SDLL010"));// ��������
			save_bodyVO.setAttributeValue("def41", bodyVO.getSubordinateyear());// Ӧ�յ���������
			save_bodyVO.setAttributeValue("def42",
					getSupplierclassByCode(bodyVO.getSupplierclassification()));// ��Ӧ�̷���
			String propertycode = bodyVO.getPropertycode(); // ��������
			String propertyname = bodyVO.getPropertyname();// ��������

			if (propertycode != null && !"".equals(propertycode)
					&& propertyname != null && !"".equals(propertyname)) {
				String pk_defdoc = saveHousePropertyArchives(propertycode,
						propertyname, orgvo.getPk_org());
				if (pk_defdoc != null && !"".equals(pk_defdoc)) {
					save_bodyVO.setAttributeValue("def43", propertycode); // ��������
					save_bodyVO.setAttributeValue("def44", pk_defdoc);// ��������
				}

			}
			/**
			 * ��ҵ�շ�ϵͳ�������ֶα�����Ϣ-2020-09-22-̸�ӽ�-end nc.vo.cmp.bill.BillDetailVO
			 */
			save_bodyVO.setAttributeValue("pay_primal", bodyVO.getPay_local());// ����ԭ�ҽ��
			save_bodyVO.setAttributeValue("pk_group", "000112100000000005FD");// ��������
			save_bodyVO.setAttributeValue("bill_date", new UFDateTime());
			save_bodyVO.setAttributeValue("creationtime", new UFDateTime());
			save_bodyVO.setAttributeValue("billclass", "fj");
			save_bodyVO.setAttributeValue("direction", -1);

			save_bodyVO
					.setAttributeValue("pk_currtype", "1002Z0100000000001K1");
			save_bodyVO.setAttributeValue("pk_org", orgvo.getPk_org());
			save_bodyVO.setAttributeValue("source_flag", 2);
			save_bodyVO.setBill_type("F5");
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
			aggVO.setParentVO(save_headVO);
			aggVO.setChildrenVO(bodylist.toArray(new BillDetailVO[0]));
		}

		return aggVO;
	}
	
	
	/**
	 * ��֤���������Ƿ����
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getPk_tradetype(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select pk_billtypecode from bd_billtype where pk_billtypecode='" + code
						+ "' and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}
}
