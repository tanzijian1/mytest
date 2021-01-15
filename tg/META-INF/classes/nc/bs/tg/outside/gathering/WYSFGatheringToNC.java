package nc.bs.tg.outside.gathering;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.GatheringBodyVO;
import nc.vo.tg.outside.GatheringHeadVO;

public class WYSFGatheringToNC extends GatheringUtils {
	protected AggGatheringBillVO onTranBill(GatheringHeadVO headVO,
			List<GatheringBodyVO> bodyVOs) throws BusinessException {
		AggGatheringBillVO aggvo = new AggGatheringBillVO();
		GatheringBillVO save_headVO = new GatheringBillVO();
		OrgVO orgvo = getOrgVO(headVO.getPk_org());
		/**
		 * ��ҵ�շ�ϵͳ���Ĵ������ֶ�-2020-08-01-̸�ӽ�-start
		 */
		save_headVO.setDef1(headVO.getSrcid());// ��ϵͳ����
		save_headVO.setDef2(headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setPk_org(orgvo.getPk_org());// ������֯
		save_headVO.setAttributeValue("billdate",
				new UFDate(headVO.getBilldate()));// ��������
		save_headVO.setAttributeValue("money", headVO.getMoney());// ԭ�ҽ��
		save_headVO.setAttributeValue("local_money_bal", headVO.getMoney());// ��֯�������
		save_headVO.setAttributeValue("def3", headVO.getImgcode());// Ӱ�����
		save_headVO.setAttributeValue("def4", headVO.getImgstate());// Ӱ��״̬
		save_headVO.setAttributeValue("def37",
				getdefdocBycode(headVO.getBusinesstype(), "SDLL003"));// ҵ������
		save_headVO.setAttributeValue("mail", headVO.getMailbox());// �����������
		String pk_tradetype = getPk_tradetype(headVO.getPk_tradetype());
		if (pk_tradetype == null || pk_tradetype.equals("")) {
			throw new BusinessException("�������ͱ�����nc��������ϵ�շ�ϵͳ����Ա����");
		}
		save_headVO.setAttributeValue("pk_tradetype", headVO.getPk_tradetype());// �տ�����code
		/**
		 * ��ҵ�շ�ϵͳ���Ĵ������ֶ�-2020-08-01-̸�ӽ�-end
		 */

		save_headVO.setAttributeValue("pk_fiorg", null);// ����������֯
		save_headVO.setAttributeValue("pk_pcorg", null);// ��������
		save_headVO.setAttributeValue("sett_org", null);// ���������֯
		save_headVO.setAttributeValue("pk_org_v", orgvo.getPk_vid());// Ӧ�ղ�����֯�汾
		save_headVO.setAttributeValue("pk_fiorg_v", null);// ����������֯�汾
		save_headVO.setAttributeValue("pk_pcorg_v", null);// �������İ汾
		save_headVO.setAttributeValue("sett_org_v", null);// ���������֯�汾
		save_headVO.setAttributeValue("isreded", headVO.getIsreded());// �Ƿ����
		save_headVO.setAttributeValue("outbusitype", null);// ��ϵͳҵ������
		save_headVO.setAttributeValue("officialprintuser", null);// ��ʽ��ӡ��
		save_headVO.setAttributeValue("officialprintdate", null);// ��ʽ��ӡ����
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ��������
		save_headVO.setAttributeValue("modifiedtime", null);// ����޸�ʱ��
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
		save_headVO.setAttributeValue("creator",
				getUserPkByCode(DefaultOperator));// ������
		save_headVO.setAttributeValue("pk_billtype", "F2");// �������ͱ���
		save_headVO.setAttributeValue("local_money", headVO.getMoney());// ��֯���ҽ��
		save_headVO.setAttributeValue("custdelegate", null);// ���浥λ
		save_headVO.setAttributeValue("pk_corp", null);// ��λ����
		save_headVO.setAttributeValue("modifier", null);// ����޸���
		save_headVO.setAttributeValue("billclass", "sk");// ���ݴ���
		save_headVO.setAttributeValue("accessorynum", null);// ��������
		save_headVO.setAttributeValue("subjcode", null);// ��Ŀ����
		save_headVO.setAttributeValue("isflowbill", UFBoolean.FALSE);// �Ƿ����̵���
		save_headVO.setAttributeValue("confirmuser", null);// ����ȷ����
		save_headVO.setAttributeValue("isinit", UFBoolean.FALSE);// �ڳ���־
		save_headVO.setAttributeValue("billno", null);// ���ݺ�

		save_headVO.setAttributeValue("syscode", 0);// ��������ϵͳ
		save_headVO.setAttributeValue("src_syscode", 0);// ������Դϵͳ
		save_headVO.setAttributeValue("billstatus", -1);// ����״̬
		save_headVO.setAttributeValue("billmaker",
				getUserPkByCode(DefaultOperator));// �Ƶ���
		save_headVO.setAttributeValue("approver", null);// �����
		save_headVO.setAttributeValue("approvedate", null);// ���ʱ��
		save_headVO.setAttributeValue("lastadjustuser", null);// ���յ�����
		save_headVO.setAttributeValue("signuser", null);// ǩ����
		save_headVO.setAttributeValue("signyear", null);// ǩ�����
		save_headVO.setAttributeValue("signperiod", null);// ǩ���ڼ�
		save_headVO.setAttributeValue("signdate", null);// ǩ������
		save_headVO.setAttributeValue("pk_busitype", "0001ZZ10000000258BF2");// ҵ������
		save_headVO.setAttributeValue("local_money", null);// ��֯���ҽ��
		save_headVO.setAttributeValue("billyear", null);// ���ݻ�����
		save_headVO.setAttributeValue("billperiod", null);// ���ݻ���ڼ�
		save_headVO.setAttributeValue("billyear", null);// ���ݻ�����
		save_headVO.setAttributeValue("scomment", null);// ժҪ
		save_headVO.setAttributeValue("effectstatus", 0);// ��Ч״̬
		save_headVO.setAttributeValue("effectuser", null);// ��Ч��
		save_headVO.setAttributeValue("effectdate", null);// ��Ч����
		save_headVO.setAttributeValue("lastapproveid", null);// ����������
		save_headVO.setAttributeValue("grouplocal", null);// ���ű��ҽ��
		save_headVO.setAttributeValue("globallocal", null);// ȫ�ֱ��ҽ��
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		save_headVO.setAttributeValue("coordflag", null);// ����Эͬ��־
		save_headVO.setAttributeValue("inner_effect_date", null);// �ڿ���������
		save_headVO.setAttributeValue("approvestatus", 3);// ����״̬
		save_headVO.setAttributeValue("sendcountryid", null);// ������
		save_headVO.setAttributeValue("taxcountryid", null);// ��˰��
		save_headVO.setAttributeValue("pk_balatype",
				getBalatypePkByCode(headVO.getPk_balatype()));// ���㷽ʽ
		// �����

		save_headVO.setStatus(VOStatus.NEW);
		List<GatheringBillItemVO> bodylist = new ArrayList<>();
		for (GatheringBodyVO gatheringBodyVO : bodyVOs) {
			GatheringBillItemVO save_bodyVO = new GatheringBillItemVO();
			/**
			 * ��ҵ�շ�ϵͳ���Ĵ������ֶ�-2020-08-01-̸�ӽ�-start
			 */
			save_bodyVO.setAttributeValue("scomment",
					gatheringBodyVO.getScomment());// ժҪ
			/**
			 * �ͻ�������Ϣ����-2020-11-03-̸�ӽ�-start
			 */
			String[] onBasicBill = onBasicBill(orgvo, gatheringBodyVO);
			save_bodyVO.setAttributeValue("customer", onBasicBill[0]);// �ͻ�
			save_bodyVO.setAttributeValue("recaccount", onBasicBill[1]);// �տ������˺�
			save_bodyVO.setAttributeValue("payaccount", onBasicBill[2]);// ���������˺�
			// ���������˻�

			/**
			 * �ͻ�������Ϣ����-2020-11-03-̸�ӽ�-end
			 */

			// save_bodyVO.setAttributeValue("taxrate",
			// gatheringBodyVO.getTaxrate());// ˰��
			// save_bodyVO.setAttributeValue("local_tax_cr",gatheringBodyVO.getLocal_tax_cr());//
			// ˰��

			save_bodyVO.setAttributeValue("def7",
					new UFDouble(gatheringBodyVO.getTaxrate()));// ˰��
			save_bodyVO.setAttributeValue("def8",
					gatheringBodyVO.getLocal_tax_cr());// ˰��

			save_bodyVO.setAttributeValue("taxprice",
					gatheringBodyVO.getTaxprice());// ��˰����
			save_bodyVO
					.setAttributeValue("def9", gatheringBodyVO.getNotax_cr());// Ӧ�ռۿ�
			save_bodyVO.setAttributeValue("local_money_cr",
					gatheringBodyVO.getMoney_cr());// ����Ӧ�ս�Ԫ��
			save_bodyVO.setAttributeValue("money_cr",
					gatheringBodyVO.getMoney_cr());// ԭ��Ӧ�ս�Ԫ��
			save_bodyVO.setAttributeValue("money_bal",
					gatheringBodyVO.getMoney_bal());// Ӧ�����
			String pk_balatype = gatheringBodyVO.getPk_balatype();
			if (pk_balatype != null && !"".equals(pk_balatype)) {
				save_bodyVO.setAttributeValue("pk_balatype",
						getBalatypePkByCode(pk_balatype));// ���㷽ʽ
			} else {
				throw new BusinessException("����pk_balatype���㷽ʽ����Ϊ��!");
			}

			save_bodyVO.setAttributeValue("contractno",
					gatheringBodyVO.getContractno());// ��ͬ���
			if (bodyVOs.size() == 1) {
				// ��ͷ��ͬ����
				save_headVO.setAttributeValue("contractno",
						gatheringBodyVO.getContractno());// ��ͬ���
			}

			save_bodyVO.setAttributeValue("invoiceno",
					gatheringBodyVO.getInvoiceno());// ��Ʊ���
			save_bodyVO.setAttributeValue("def18", gatheringBodyVO.getRowid());// ��ҵ�շ�ϵͳ������ID
			save_bodyVO.setAttributeValue("def19",
					gatheringBodyVO.getContractname());// ��ͬ����

			String productline = gatheringBodyVO.getProductline();
			if (productline != null && !"".equals(productline)) {
				String productlineId = saveProductlineArchives(productline);// �����Ʒ�ߵ���
				save_bodyVO.setAttributeValue("productline", productlineId);// ��Ʒ��
			}

			save_bodyVO.setAttributeValue("memo", gatheringBodyVO.getMemo());// ��ע

			// itemcode �����շ���Ŀ�����ѯ�շ���Ŀ���ƺ��շ���Ŀ����
			String itemcode = gatheringBodyVO.getItemcode();
			if (itemcode != null && !"".equals(itemcode != null)) {
				List<Map<String, String>> budgetsubNames = getBudgetsubNameByCode(itemcode);
				for (Map<String, String> names : budgetsubNames) {
					save_bodyVO.setAttributeValue("def50",
							names.get("pk_defdoc"));// itemname �շ���Ŀ����
					save_bodyVO.setAttributeValue("def49", names.get("pid"));// itemtype
																				// �շ���Ŀ����
				}
			}
			// projectphase ��Ŀ�׶� �Զ��嵵��SDLL006
			save_bodyVO.setAttributeValue(
					"def16",
					getdefdocBycode(gatheringBodyVO.getProjectphase(),
							"SDLL006"));// ��Ŀ�׶�

			save_bodyVO.setAttributeValue("project",
					getProject(gatheringBodyVO.getProject()));// ��Ŀ

			save_bodyVO.setAttributeValue("def43",
					getdefdocBycode(gatheringBodyVO.getArrears(), "SDLL002"));// �Ƿ�����Ƿ��

			save_bodyVO.setAttributeValue(
					"def44",
					getdefdocBycode(gatheringBodyVO.getBusinessbreakdown(),
							"SDLL004"));// ҵ��ϸ��

			save_bodyVO.setAttributeValue(
					"def56",
					getdefdocBycode(gatheringBodyVO.getProjectproperties(),
							"SDLL007"));// ��Ŀ����

			save_bodyVO.setAttributeValue("def51",
					getCustclassByCode(gatheringBodyVO.getPk_custclass()));// �ͻ�����

			String propertycode = gatheringBodyVO.getPropertycode(); // ��������
			String propertyname = gatheringBodyVO.getPropertyname();// ��������

			if (propertycode != null && !"".equals(propertycode)
					&& propertyname != null && !"".equals(propertyname)) {
				String pk_defdoc = saveHousePropertyArchives(propertycode,
						propertyname, orgvo.getPk_org());
				if (pk_defdoc != null && !"".equals(pk_defdoc)) {
					save_bodyVO.setAttributeValue("def52", propertycode); // ��������
					save_bodyVO.setAttributeValue("def53", pk_defdoc);// ��������
				}

			}
			String paymenttype = getdefdocBycode(
					gatheringBodyVO.getPaymenttype(), "SDLL010");
			save_bodyVO.setAttributeValue("def54", paymenttype);// ��������
			String subordinateyear = gatheringBodyVO.getSubordinateyear();
			// ����ͱ��嵥������һ����ȡ��������
			if (subordinateyear != null && !"".equals(subordinateyear)
					&& subordinateyear.contains("-")) {
				// ȡ�·ݵĵ�һ��
				int year = Integer.parseInt(subordinateyear.substring(0,
						subordinateyear.indexOf("-")));// ��ȡ���
				int month = Integer.parseInt(subordinateyear.substring(
						subordinateyear.indexOf("-") + 1,
						subordinateyear.length()));// ��ȡ�·�
				subordinateyear = getFirstDayOfMonth1(year, month);
				save_bodyVO.setAttributeValue("def55", new UFDate(
						subordinateyear));// Ӧ�յ���������
			} else {
				throw new BusinessException(
						"���鵥��Ӧ�յ���������subordinateyear�ֶ��Ƿ�Ϊ�ջ��ʽ�Ƿ���ȷ!");
			}

			String pk_recpaytype = "";
			pk_recpaytype = getRecpaytype(getBalatypePkByCode(pk_balatype));
			save_bodyVO.setAttributeValue("pk_recpaytype", pk_recpaytype);// �տ�ҵ������

			if ("10".equals(pk_balatype)) {
				save_bodyVO.setAttributeValue("cashaccount",
						getCashaccountByCode(orgvo.getCode()));// �ֽ��˻�
			}
			String actualnateyear = gatheringBodyVO.getActualnateyear();// ʵ����������-����
			// ����ͱ��嵥������һ����ȡ��������
			if (actualnateyear != null && !"".equals(actualnateyear)
					&& actualnateyear.contains("-")) {
				if (headVO.getBilldate() != null
						&& headVO.getBilldate().startsWith(actualnateyear)) {
					save_bodyVO.setAttributeValue("def58",
							new UFDate(headVO.getBilldate()));// ʵ����������
					save_bodyVO.setAttributeValue("busidate",
							new UFDate(headVO.getBilldate()));// ��������
				} else {
					// ����ͱ��嵥�����ڲ�һ����ȡ�·ݵĵ�һ��
					int year = Integer.parseInt(actualnateyear.substring(0,
							actualnateyear.indexOf("-")));// ��ȡ���
					int month = Integer.parseInt(actualnateyear.substring(
							actualnateyear.indexOf("-") + 1,
							actualnateyear.length()));// ��ȡ�·�
					actualnateyear = getFirstDayOfMonth1(year, month);
					save_bodyVO.setAttributeValue("def58", new UFDate(
							actualnateyear));// ʵ����������
					save_bodyVO.setAttributeValue("busidate", new UFDate(
							actualnateyear));// ��������
				}
			} else {
				throw new BusinessException(
						"���鵥��ʵ����������actualnateyear�ֶ��Ƿ�Ϊ�ջ��ʽ�Ƿ���ȷ!");
			}
			String def58 = (String) save_bodyVO.getAttributeValue("def58");
			if (def58 != null) {
				save_headVO.setBusidate(new UFDate(def58));
			}
			String subordiperiod = gatheringBodyVO.getSubordiperiod();// Ӧ�չ�����
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
				save_bodyVO.setAttributeValue("def57",
						new UFDate(subordiperiod));// Ӧ�չ�����
			} else {
				throw new BusinessException(
						"���鵥��Ӧ�չ�����subordiperiod�ֶ��Ƿ�Ϊ�ջ��ʽ�Ƿ���ȷ!");
			}
			// ʵ�չ�����
			String actualperiod = gatheringBodyVO.getActualperiod();// Ӧ�չ�����
			if (null != actualperiod && !"".equals(actualperiod)
					&& actualperiod.contains("-")) {
				// ����ͱ��嵥�����ڲ�һ����ȡ�·ݵĵ�һ��
				int year = Integer.parseInt(actualperiod.substring(0,
						actualperiod.indexOf("-")));// ��ȡ���
				int month = Integer.parseInt(actualperiod.substring(
						actualperiod.indexOf("-") + 1, actualperiod.length()));// ��ȡ�·�
				actualperiod = getFirstDayOfMonth1(year, month);
				save_bodyVO
						.setAttributeValue("def59", new UFDate(actualperiod));// ʵ�չ�����

			} else {
				throw new BusinessException(
						"���鵥��Ӧ�չ�����actualperiod�ֶ��Ƿ�Ϊ�ջ��ʽ�Ƿ���ȷ!");
			}
			/**
			 * ��ҵ�շ�ϵͳ���Ĵ������ֶ�-2020-08-01-̸�ӽ�-end
			 */
			save_bodyVO.setAttributeValue("isdiscount", UFBoolean.FALSE);
			save_bodyVO.setAttributeValue("rate", UFDouble.ONE_DBL);
			save_bodyVO.setAttributeValue("direction",
					BillEnumCollection.Direction.CREDIT.VALUE);
			save_bodyVO.setAttributeValue("local_price", 0);
			save_bodyVO.setAttributeValue("local_taxprice", 0);
			save_bodyVO.setAttributeValue("occupationmny", 1);
			save_bodyVO.setAttributeValue("pk_billtype",
					save_bodyVO.getAttributeValue("pk_billtype"));
			save_bodyVO.setAttributeValue("pk_group", "000112100000000005FD");
			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// Ӧ�ղ�����֯

			save_bodyVO.setAttributeValue("objtype", 0);// ��������
			save_bodyVO
					.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����

			save_bodyVO.setOccupationmny(save_bodyVO.getMoney_cr());// Ԥռ��ԭ�����

			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new GatheringBillItemVO[0]));
		return aggvo;
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
				"select pk_billtypecode from bd_billtype where pk_billtypecode='"
						+ code + "' and nvl(dr,0)=0", new ColumnProcessor());
		return (String) obj;
	}
}
