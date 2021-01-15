package nc.bs.tg.outside.recbill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.ReceivableBodyVO;
import nc.vo.tg.outside.ReceivableHeadVO;

/**
 * Ӧ�յ���ؽӿ�
 * 
 * @author ̸�ӽ�-2020-07-22
 */
public class WYSFRecbillToNC extends RecbillUtils {
	protected AggReceivableBillVO onTranBill(ReceivableHeadVO headVO,
			List<ReceivableBodyVO> bodyVOs) throws BusinessException {
		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO save_headVO = new ReceivableBillVO();
		OrgVO orgvo = getOrgVO(headVO.getPk_org());
		/**
		 * ��ҵ�շ�ϵͳ���Ĵ������ֶ�-2020-07-31-̸�ӽ�-start
		 */
		save_headVO.setPk_org(orgvo.getPk_org());// ������֯
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// ��ϵͳ����
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setAttributeValue("billdate",
				new UFDate(headVO.getBilldate()));// ��������
		save_headVO.setAttributeValue("pk_deptid", headVO.getPk_deptid());// ����
		save_headVO.setAttributeValue("pk_psndoc", headVO.getPk_psndoc());// ҵ��Ա
		String pk_tradetype = getPk_tradetype(headVO.getPk_tradetype());
		if (pk_tradetype == null || pk_tradetype.equals("")) {
			throw new BusinessException("�������ͱ�����nc��������ϵ�շ�ϵͳ����Ա����");
		}
		save_headVO.setAttributeValue("pk_tradetype", headVO.getPk_tradetype());// Ӧ������code
		save_headVO.setAttributeValue("money", headVO.getMoney());// ԭ�ҽ��
		save_headVO.setAttributeValue("isreded", headVO.getIsreded());// �Ƿ���
		save_headVO.setAttributeValue("pk_balatype",
				getBalatypePkByCode(headVO.getPk_balatype()));// ���㷽ʽ
		save_headVO.setAttributeValue("bpmid", headVO.getBpmid());// bpmid
		save_headVO.setAttributeValue("def3", headVO.getImgcode());// Ӱ�����
		save_headVO.setAttributeValue("def4", headVO.getImgstate());// Ӱ��״̬
		// save_headVO.setAttributeValue("def5", headVO.getItemtype());// �շ���Ŀ����
		// save_headVO.setAttributeValue("def6", headVO.getItemname());// �շ���Ŀ����
		save_headVO.setAttributeValue("def7",
				getdefdocBycode(headVO.getBusinesstype(), "SDLL003"));// ҵ������
		// save_headVO.setAttributeValue("def8",
		// headVO.getBusinessbreakdownpublic());// ҵ��ϸ��
		save_headVO.setAttributeValue("mail", headVO.getMailbox());// �����������
		// save_headVO.setAttributeValue("def22", headVO.getProjectphase()); //
		// ��Ŀ�׶�
		/**
		 * ��ҵ�շ�ϵͳ���Ĵ������ֶ�-2020-07-31-̸�ӽ�-end
		 */

		save_headVO.setAttributeValue("pk_fiorg", null);// ����������֯
		save_headVO.setAttributeValue("pk_pcorg", null);// ��������
		save_headVO.setAttributeValue("sett_org", null);// ���������֯
		save_headVO.setAttributeValue("pk_org_v", orgvo.getPk_vid());// Ӧ�ղ�����֯�汾
		save_headVO.setAttributeValue("pk_fiorg_v", null);// ����������֯�汾
		save_headVO.setAttributeValue("pk_pcorg_v", null);// �������İ汾
		save_headVO.setAttributeValue("sett_org_v", null);// ���������֯�汾
		save_headVO.setAttributeValue("outbusitype", null);// ��ϵͳҵ������
		save_headVO.setAttributeValue("officialprintuser", null);// ��ʽ��ӡ��
		save_headVO.setAttributeValue("officialprintdate", null);// ��ʽ��ӡ����
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ��������
		save_headVO.setAttributeValue("modifiedtime", null);// ����޸�ʱ��
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
		save_headVO.setAttributeValue("creator",
				getUserPkByCode(WYSFDefaultOperator));// ������
		save_headVO.setAttributeValue("pk_billtype", "F0");// �������ͱ���
		save_headVO.setAttributeValue("custdelegate", null);// ���浥λ
		save_headVO.setAttributeValue("pk_corp", null);// ��λ����
		save_headVO.setAttributeValue("modifier", null);// ����޸���
		save_headVO.setAttributeValue("billclass", "ys");// ���ݴ���
		save_headVO.setAttributeValue("pk_recbill", null);// Ӧ�յ���ʶ
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
				getUserPkByCode(WYSFDefaultOperator));// �Ƶ���
		save_headVO.setAttributeValue("approver", null);// �����
		save_headVO.setAttributeValue("approvedate", null);// ���ʱ��
		save_headVO.setAttributeValue("lastadjustuser", null);// ���յ�����
		save_headVO.setAttributeValue("signuser", null);// ǩ����
		save_headVO.setAttributeValue("signyear", null);// ǩ�����
		save_headVO.setAttributeValue("signperiod", null);// ǩ���ڼ�
		save_headVO.setAttributeValue("signdate", null);// ǩ������
		save_headVO.setAttributeValue("pk_busitype", "0001ZZ10000000258BF2");// ҵ������
		save_headVO.setAttributeValue("local_money", headVO.getMoney());// ��֯���ҽ��
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

		save_headVO.setStatus(VOStatus.NEW);

		List<ReceivableBillItemVO> bodylist = new ArrayList<>();
		for (ReceivableBodyVO receivableBodyVO : bodyVOs) {
			ReceivableBillItemVO save_bodyVO = new ReceivableBillItemVO();
			/**
			 * ��ҵ�շ�ϵͳ���Ĵ������ֶ�-2020-07-31-̸�ӽ�-start
			 */
			save_bodyVO.setAttributeValue("scomment",
					receivableBodyVO.getScomment());// ժҪ
			save_bodyVO.setAttributeValue("customer",
					getcustomer(receivableBodyVO.getCustomer()));// �ͻ�
			save_bodyVO.setAttributeValue("quantity_de", new UFDouble(
					receivableBodyVO.getQuantity_de()));// �跽����
			save_bodyVO.setAttributeValue("taxrate", new UFDouble(
					receivableBodyVO.getTaxrate()));// ˰��
			save_bodyVO.setAttributeValue("taxprice",
					receivableBodyVO.getTaxprice());// ��˰����
			save_bodyVO.setAttributeValue("price", receivableBodyVO.getPrice());// ����
			save_bodyVO.setAttributeValue("local_tax_de",
					receivableBodyVO.getLocal_tax_de());// ˰��
			save_bodyVO.setAttributeValue("local_money_de",
					receivableBodyVO.getMoney_de());// �跽���ҽ��
			save_bodyVO.setAttributeValue("notax_de",
					receivableBodyVO.getNotax_de());// �跽ԭ����˰���
			save_bodyVO.setAttributeValue("money_de", new UFDouble(
					receivableBodyVO.getMoney_de()));// �跽ԭ�ҽ��
			save_bodyVO.setAttributeValue("money_bal", new UFDouble(
					receivableBodyVO.getMoney_bal()));// ԭ�����
			save_bodyVO.setAttributeValue("pk_balatype",
					getBalatypePkByCode(receivableBodyVO.getPk_balatype()));// ���㷽ʽ
			save_bodyVO.setAttributeValue("contractno",
					receivableBodyVO.getContractno());// ��ͬ��
			// ��ͷ��ͬ��
			if (bodyVOs.size() == 1) {
				save_headVO.setAttributeValue("contractno",
						receivableBodyVO.getContractno());
			}

			save_bodyVO.setAttributeValue("invoiceno",
					receivableBodyVO.getInvoiceno());// ��Ʊ��
			save_bodyVO.setAttributeValue("def12", receivableBodyVO.getRowid());// ��ҵ�շ�ϵͳ������ID
			save_bodyVO.setAttributeValue("def13",
					receivableBodyVO.getContractname());// ��ͬ����
			String productline = receivableBodyVO.getProductline();
			if (productline == null || "".equals(productline)) {
				throw new BusinessException("��Ʒ��/������:productline����Ϊ��!");
			}
			// �����Ʒ�ߵ���
			String productlineId = saveProductlineArchives(productline);
			save_bodyVO.setAttributeValue("productline", productlineId);// ��Ʒ��

			save_bodyVO.setAttributeValue("memo", receivableBodyVO.getMemo());// ��ע
			// itemcode �����շ���Ŀ�����ѯ�շ���Ŀ���ƺ��շ���Ŀ����
			String itemcode = receivableBodyVO.getItemcode();
			if (itemcode != null && !"".equals(itemcode != null)) {
				List<Map<String, String>> budgetsubNames = getBudgetsubNameByCode(itemcode);
				for (Map<String, String> names : budgetsubNames) {
					save_bodyVO.setAttributeValue("def22",
							names.get("pk_defdoc"));// itemname �շ���Ŀ����
					save_bodyVO.setAttributeValue("def21", names.get("pid"));// itemtype
																				// �շ���Ŀ����
				}

			}
			// projectphase ��Ŀ�׶� �Զ��嵵��SDLL006
			save_bodyVO.setAttributeValue(
					"def16",
					getdefdocBycode(receivableBodyVO.getProjectphase(),
							"SDLL006"));// ��Ŀ�׶�

			save_bodyVO.setAttributeValue("project",
					getProject(receivableBodyVO.getProject()));// ��Ŀ

			save_bodyVO.setAttributeValue("def27",
					getdefdocBycode(receivableBodyVO.getArrears(), "SDLL002"));// �Ƿ�����Ƿ��

			save_bodyVO.setAttributeValue(
					"def28",
					getdefdocBycode(receivableBodyVO.getBusinessbreakdown(),
							"SDLL004"));// ҵ��ϸ��
			save_bodyVO.setAttributeValue(
					"def36",
					getdefdocBycode(receivableBodyVO.getProjectproperties(),
							"SDLL007"));// ��Ŀ����

			save_bodyVO.setAttributeValue("def31",
					getCustclassByCode(receivableBodyVO.getPk_custclass()));// �ͻ�����

			String propertycode = receivableBodyVO.getPropertycode(); // ��������
			String propertyname = receivableBodyVO.getPropertyname();// ��������

			if (propertycode != null && !"".equals(propertycode)
					&& propertyname != null && !"".equals(propertyname)) {
				String pk_defdoc = saveHousePropertyArchives(propertycode,
						propertyname, orgvo.getPk_org());
				if (pk_defdoc != null && !"".equals(pk_defdoc)) {
					save_bodyVO.setAttributeValue("def32", propertycode); // ��������
					save_bodyVO.setAttributeValue("def33", pk_defdoc);// ��������
				}

			}

			save_bodyVO.setAttributeValue(
					"def34",
					getdefdocBycode(receivableBodyVO.getPaymenttype(),
							"SDLL010"));// ��������

			String subordinateyear = receivableBodyVO.getSubordinateyear();// Ӧ�յ���������
			// ����ͱ��嵥������һ����ȡ��������
			if (subordinateyear != null && !"".equals(subordinateyear)
					&& subordinateyear.contains("-")) {
				if (headVO.getBilldate() != null
						&& headVO.getBilldate().startsWith(subordinateyear)) {
					save_bodyVO.setAttributeValue("def35",
							new UFDate(headVO.getBilldate()));// Ӧ�յ���������
				} else {
					// ����ͱ��嵥�����ڲ�һ����ȡ�·ݵĵ�һ��
					int year = Integer.parseInt(subordinateyear.substring(0,
							subordinateyear.indexOf("-")));// ��ȡ���
					int month = Integer.parseInt(subordinateyear.substring(
							subordinateyear.indexOf("-") + 1,
							subordinateyear.length()));// ��ȡ�·�
					subordinateyear = getFirstDayOfMonth1(year, month);
					save_bodyVO.setAttributeValue("def35", new UFDate(
							subordinateyear));// Ӧ�յ���������
				}
			} else {
				throw new BusinessException(
						"���鵥����������subordinateyear�ֶ��Ƿ�Ϊ�ջ��ʽ�Ƿ���ȷ!");
			}
			String def35 = (String) save_bodyVO.getAttributeValue("def35");
			save_bodyVO.setAttributeValue("busidate", def35);// ��������
			if (def35 != null) {
				save_headVO.setBusidate(new UFDate(def35));
			}

			String subordiperiod = receivableBodyVO.getSubordiperiod();// Ӧ�չ�����
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
				save_bodyVO.setAttributeValue("def37",
						new UFDate(subordiperiod));// Ӧ�չ�����
			} else {
				throw new BusinessException(
						"���鵥��Ӧ�չ�����subordiperiod�ֶ��Ƿ�Ϊ�ջ��ʽ�Ƿ���ȷ!");
			}
			/**
			 * ��ҵ�շ�ϵͳ���Ĵ������ֶ�-2020-07-31-̸�ӽ�-end
			 */

			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// Ӧ�ղ�����֯
			save_bodyVO.setAttributeValue("pk_pcorg", null);// ��������
			save_bodyVO.setAttributeValue("pk_fiorg", null);// ����������֯
			save_bodyVO.setAttributeValue("sett_org", null);// ���������֯
			save_bodyVO.setAttributeValue("pk_pcorg_v", null);// �������İ汾
			save_bodyVO.setAttributeValue("pk_org_v", null);// Ӧ�ղ�����֯�汾
			save_bodyVO.setAttributeValue("pk_fiorg_v", null);// ����������֯�汾
			save_bodyVO.setAttributeValue("sett_org_v", null);// ���������֯�汾
			save_bodyVO.setAttributeValue("so_org_v", null);// ҵ����֯�汾
			save_bodyVO.setAttributeValue("so_deptid", null);// ҵ����
			save_bodyVO.setAttributeValue("so_deptid_v", null);// ҵ���Ű汾
			save_bodyVO.setAttributeValue("so_psndoc", null);// ҵ����Ա
			save_bodyVO.setAttributeValue("so_ordertype", null);// ���۶�������
			save_bodyVO.setAttributeValue("so_transtype", null);// ������������
			save_bodyVO.setAttributeValue("so_org", null);// ҵ����֯
			save_bodyVO.setAttributeValue("material", null);// ����
			save_bodyVO.setAttributeValue("postunit", null);// ���ۼ�����λ
			save_bodyVO.setAttributeValue("postpricenotax", null);// ���۵�λ��˰����
			save_bodyVO.setAttributeValue("postquantity", null);// ���۵�λ����
			save_bodyVO.setAttributeValue("postprice", null);// ���۵�λ��˰����
			save_bodyVO.setAttributeValue("coordflag", null);// ����Эͬ״̬
			save_bodyVO.setAttributeValue("equipmentcode", null);// �豸����
			save_bodyVO.setAttributeValue("cashitem", null);// �ֽ�������Ŀ
			save_bodyVO.setAttributeValue("bankrollprojet", null);// �ʽ�ƻ���Ŀ
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// �����־
			save_bodyVO
					.setAttributeValue("billdate", save_headVO.getBilldate());// ��������
			save_bodyVO
					.setAttributeValue("pk_group", save_headVO.getPk_group());// ��������
			save_bodyVO.setAttributeValue("pk_billtype",
					save_headVO.getPk_billtype());// �������ͱ���
			save_bodyVO.setAttributeValue("billclass",
					save_headVO.getBillclass());// ���ݴ���
			save_bodyVO.setAttributeValue("pk_tradetype",
					save_headVO.getPk_tradetype());// Ӧ������code
			save_bodyVO.setAttributeValue("pk_tradetypeid",
					save_headVO.getPk_tradetypeid());// Ӧ������
			save_bodyVO.setAttributeValue("pk_recitem", null);// Ӧ�յ��б�ʶ

			save_bodyVO.setAttributeValue("pk_subjcode", null);// ��֧��Ŀ
			save_bodyVO.setAttributeValue("billno", null);// ���ݱ��
			save_bodyVO.setAttributeValue("objtype", 0);// ��������
			save_bodyVO.setAttributeValue("rowno", null);// ���ݷ�¼��
			save_bodyVO.setAttributeValue("rowtype", null);// ������
			save_bodyVO.setAttributeValue("direction", 1);// ����
			save_bodyVO.setAttributeValue("pk_ssitem", null);// ����������
			save_bodyVO.setAttributeValue("subjcode", null);// ��Ŀ����
			save_bodyVO
					.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
			save_bodyVO.setAttributeValue("rate", 1);// ��֯���һ���
			save_bodyVO.setAttributeValue("pk_deptid",
					save_headVO.getPk_deptid());// ����
			save_bodyVO.setAttributeValue("pk_deptid_v", null);// �� ��
			save_bodyVO.setAttributeValue("pk_psndoc",
					save_headVO.getPk_psndoc());// ҵ��Ա
			save_bodyVO.setAttributeValue("local_money_bal", new UFDouble(
					receivableBodyVO.getMoney_de()));// ��֯�������
			save_bodyVO.setAttributeValue("quantity_bal", null);// �������
			save_bodyVO.setAttributeValue("local_notax_de", new UFDouble(
					receivableBodyVO.getMoney_de()));// ��֯������˰���
			save_bodyVO.setAttributeValue("taxnum", null);// ˰��
			save_bodyVO.setAttributeValue("top_billid", null);// �ϲ㵥������
			save_bodyVO.setAttributeValue("top_itemid", null);// �ϲ㵥��������
			save_bodyVO.setAttributeValue("top_billtype", null);// �ϲ㵥������
			save_bodyVO.setAttributeValue("top_tradetype", null);// �ϲ㽻������
			save_bodyVO.setAttributeValue("src_tradetype", null);// Դͷ��������
			save_bodyVO.setAttributeValue("src_billtype", null);// Դͷ��������
			save_bodyVO.setAttributeValue("src_billid", null);// Դͷ��������
			save_bodyVO.setAttributeValue("src_itemid", null);// Դͷ����������
			save_bodyVO.setAttributeValue("taxtype", 1);// ��˰���
			save_bodyVO.setAttributeValue("pk_payterm", null);// �տ�Э��
			save_bodyVO.setAttributeValue("payaccount", null);// ���������˻�
			save_bodyVO.setAttributeValue("recaccount", null);// �տ������˻�
			save_bodyVO.setAttributeValue("ordercubasdoc", null);// �����ͻ�
			save_bodyVO.setAttributeValue("innerorderno", null);// ����������
			save_bodyVO.setAttributeValue("assetpactno", null);// �ʲ���ͬ��
			save_bodyVO.setAttributeValue("freecust", null);// ɢ��
			save_bodyVO.setAttributeValue("purchaseorder", null);// ������
			save_bodyVO.setAttributeValue("outstoreno", null);// ���ⵥ��
			save_bodyVO.setAttributeValue("checkelement", null);// ���κ���Ҫ��
			save_bodyVO.setAttributeValue("def30", null);// �Զ�����30
			save_bodyVO.setAttributeValue("def28", null);// �Զ�����28
			save_bodyVO.setAttributeValue("def27", null);// �Զ�����27
			save_bodyVO.setAttributeValue("def26", null);// �Զ�����26
			save_bodyVO.setAttributeValue("def25", null);// �Զ�����25
			save_bodyVO.setAttributeValue("def24", null);// �Զ�����24
			save_bodyVO.setAttributeValue("def23", null);// �Զ�����23
			save_bodyVO.setAttributeValue("def20", null);// �Զ�����20
			save_bodyVO.setAttributeValue("def19", null);// �Զ�����19
			save_bodyVO.setAttributeValue("def18", null);// �Զ�����18
			save_bodyVO.setAttributeValue("def17", null);// �Զ�����17
			save_bodyVO.setAttributeValue("def15", null);// �Զ�����15
			save_bodyVO.setAttributeValue("def14", null);// �Զ�����14
			save_bodyVO.setAttributeValue("def11", null);// �Զ�����11
			save_bodyVO.setAttributeValue("def10", null);// �Զ�����10
			save_bodyVO.setAttributeValue("def9", null);// �Զ�����9
			save_bodyVO.setAttributeValue("def8", null);// �Զ�����8
			save_bodyVO.setAttributeValue("pk_recbill", null);// �ͻ�Ӧ�յ���ʶ
			save_bodyVO.setAttributeValue("def7", null);// �Զ�����7
			save_bodyVO.setAttributeValue("def6", null);// �Զ�����6
			save_bodyVO.setAttributeValue("def5", null);// �Զ�����5
			save_bodyVO.setAttributeValue("def4", null);// �Զ�����4
			save_bodyVO.setAttributeValue("def3", null);// �Զ�����3
			save_bodyVO.setAttributeValue("def2", null);// �Զ�����2
			save_bodyVO.setAttributeValue("def1", null);// ��Ӧ��ͬ���
			save_bodyVO.setAttributeValue("grouprate", null);// ���ű��һ���
			save_bodyVO.setAttributeValue("globalrate", null);// ȫ�ֱ��һ���
			save_bodyVO.setAttributeValue("groupdebit", null);// ���ű��ҽ��(�跽)
			save_bodyVO.setAttributeValue("globaldebit", null);// ȫ�ֱ��ҽ��(�跽)
			save_bodyVO.setAttributeValue("groupbalance", null);// ���ű������
			save_bodyVO.setAttributeValue("globalbalance", null);// ȫ�ֱ������
			save_bodyVO.setAttributeValue("groupnotax_de", null);// ���ű�����˰���(�跽)
			save_bodyVO.setAttributeValue("globalnotax_de", null);// ȫ�ֱ�����˰���(�跽)
			// save_bodyVO.setAttributeValue("project", null);// ��Ŀ
			save_bodyVO.setAttributeValue("project_task", null);// ��Ŀ����
			save_bodyVO.setAttributeValue("settleno", null);// �����嵥��
			save_bodyVO.setAttributeValue("local_price", null);// ���ҵ���
			save_bodyVO.setAttributeValue("local_taxprice", null);// ���Һ�˰����
			save_bodyVO.setAttributeValue("confernum", null);// �ڲ����׽����
			save_bodyVO.setAttributeValue("costcenter", null);// �ɱ�����
			save_bodyVO.setAttributeValue("rececountryid", null);// �ջ���
			save_bodyVO.setAttributeValue("buysellflag", null);// ��������
			save_bodyVO.setAttributeValue("triatradeflag", null);// ����ó��
			save_bodyVO.setAttributeValue("vatcode", null);// VATע����
			save_bodyVO.setAttributeValue("custvatcode", null);// �ͻ�VATע����
			save_bodyVO.setAttributeValue("taxcodeid", null);// ˰��
			save_bodyVO.setAttributeValue("caltaxmny", null);// ��˰���
			save_bodyVO.setAttributeValue("pk_recbill", null);// �ͻ�Ӧ�յ���ʶ
			save_bodyVO.setAttributeValue("material_src", null);// ԭʼ����
			save_bodyVO.setAttributeValue("matcustcode", null);// �ͻ�������
			save_bodyVO.setAttributeValue("settlemoney", null);// �տ���
			save_bodyVO.setAttributeValue("settlecurr", null);// �տ����
			save_bodyVO.setAttributeValue("batchcode", null);// ���κ�
			save_bodyVO.setAttributeValue("pk_batchcode", null);// ���κű���
			save_bodyVO.setOccupationmny(save_bodyVO.getMoney_de());// Ԥռ��ԭ�����

			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}

		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new ReceivableBillItemVO[0]));

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
