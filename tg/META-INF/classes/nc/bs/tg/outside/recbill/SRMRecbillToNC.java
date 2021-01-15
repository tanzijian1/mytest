package nc.bs.tg.outside.recbill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.ReceivableBodyVO;
import nc.vo.tg.outside.ReceivableHeadVO;

/**
 * SRM ��֤��Ӧ�յ���ؽӿ�
 * 
 * @author ̸�ӽ�-2020-07-22
 */
public class SRMRecbillToNC extends RecbillUtils {
	protected AggReceivableBillVO onTranBill(ReceivableHeadVO headVO,
			List<ReceivableBodyVO> bodyVOs) throws BusinessException {
		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO save_headVO = new ReceivableBillVO();
		/**
		 * SRM�������Ĳ���-2020-08-24-̸�ӽ�-start
		 */
		save_headVO.setPk_org(getPk_orgByCode(headVO.getPk_org()));// ������֯
		save_headVO.setDef11(headVO.getOperator());// ������
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// ��ϵͳ����
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setAttributeValue("def3", headVO.getImgcode());// Ӱ�����
		save_headVO.setAttributeValue("def4", headVO.getImgstate());// Ӱ��״̬
		save_headVO.setAttributeValue("memo", headVO.getMemo());// ��ע
		save_headVO.setAttributeValue("def13", headVO.getConfiscatedmoney());// �Ƿ�û
		save_headVO.setAttributeValue("bpmid", headVO.getBpmid());// bpmid
		save_headVO.setAttributeValue("def24", headVO.getMailbox());// ����������

		/**
		 * SRM�������Ĳ���-2020-08-24-̸�ӽ�-end
		 */

		save_headVO.setAttributeValue("pk_fiorg", null);// ����������֯
		save_headVO.setAttributeValue("pk_pcorg", null);// ��������
		save_headVO.setAttributeValue("sett_org", null);// ���������֯
		save_headVO.setAttributeValue("pk_org_v", null);// Ӧ�ղ�����֯�汾
		save_headVO.setAttributeValue("pk_fiorg_v", null);// ����������֯�汾
		save_headVO.setAttributeValue("pk_pcorg_v", null);// �������İ汾
		save_headVO.setAttributeValue("sett_org_v", null);// ���������֯�汾
		save_headVO.setAttributeValue("isreded", null);// �Ƿ����
		save_headVO.setAttributeValue("outbusitype", null);// ��ϵͳҵ������
		save_headVO.setAttributeValue("officialprintuser", null);// ��ʽ��ӡ��
		save_headVO.setAttributeValue("officialprintdate", null);// ��ʽ��ӡ����
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ��������
		save_headVO.setAttributeValue("modifiedtime", null);// ����޸�ʱ��
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
		save_headVO.setAttributeValue("creator",
				getUserPkByCode(DefaultOperator));// ������
		save_headVO.setAttributeValue("pk_billtype", "F0");// �������ͱ���
		save_headVO.setAttributeValue("custdelegate", null);// ���浥λ
		save_headVO.setAttributeValue("pk_corp", null);// ��λ����
		save_headVO.setAttributeValue("modifier", null);// ����޸���
		save_headVO.setAttributeValue("pk_tradetype", "F0-Cxx-LL01");// Ӧ������code
		save_headVO.setAttributeValue("billclass", "ys");// ���ݴ���
		save_headVO.setAttributeValue("pk_recbill", null);// Ӧ�յ���ʶ
		save_headVO.setAttributeValue("accessorynum", null);// ��������
		save_headVO.setAttributeValue("subjcode", null);// ��Ŀ����
		save_headVO.setAttributeValue("isflowbill", UFBoolean.FALSE);// �Ƿ����̵���
		save_headVO.setAttributeValue("confirmuser", null);// ����ȷ����
		save_headVO.setAttributeValue("isinit", UFBoolean.FALSE);// �ڳ���־
		save_headVO.setAttributeValue("billno", null);// ���ݺ�
		save_headVO.setAttributeValue("billdate", new UFDate());// ��������
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
		save_headVO.setAttributeValue("money", headVO.getMoney());// ԭ�ҽ��
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

		save_headVO.setStatus(VOStatus.NEW);

		List<ReceivableBillItemVO> bodylist = new ArrayList<>();
		for (ReceivableBodyVO receivableBodyVO : bodyVOs) {
			ReceivableBillItemVO save_bodyVO = new ReceivableBillItemVO();
			/**
			 * SRM�������Ĳ���-2020-08-24-̸�ӽ�-start
			 */
			save_bodyVO.setAttributeValue("pk_balatype",
					getBalatypePkByCode(save_headVO.getPk_balatype()));// ���㷽ʽ
			String supplier = getCustomerBySupplierCode(receivableBodyVO
					.getSupplier());
			save_bodyVO.setAttributeValue("customer", supplier);// ��Ӧ��
			String recaccount = receivableBodyVO.getRecaccount();
			save_bodyVO.setAttributeValue("recaccount",
					getBankaccnumInfo(save_headVO.getPk_org(), recaccount).get("pk_bankaccsub"));// �տ������˻�
			String payaccount = receivableBodyVO.getPayaccount();
			save_bodyVO.setAttributeValue("payaccount",
					getAccountIDByCode(payaccount, supplier, "pk_cust"));// ���������˻�

			save_bodyVO.setAttributeValue("local_money_de",
					receivableBodyVO.getLocal_money_de());// ��֯���ҽ��
			save_bodyVO.setAttributeValue("def10",
					receivableBodyVO.getPaymenttype());// ��������
			save_bodyVO.setAttributeValue("def11",
					receivableBodyVO.getDeposittype());// �б걣֤������

			/**
			 * SRM�������Ĳ���-2020-08-24-̸�ӽ�-end
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
			save_bodyVO.setAttributeValue("productline", null);// ��Ʒ��
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
			save_bodyVO
					.setAttributeValue("busidate", save_bodyVO.getBilldate());// ��������
			save_bodyVO.setAttributeValue("pk_subjcode", null);// ��֧��Ŀ
			save_bodyVO.setAttributeValue("billno", null);// ���ݱ��
			save_bodyVO.setAttributeValue("objtype", 0);// ��������
			save_bodyVO.setAttributeValue("rowno", null);// ���ݷ�¼��
			save_bodyVO.setAttributeValue("rowtype", null);// ������
			save_bodyVO.setAttributeValue("direction", 1);// ����
			save_bodyVO.setAttributeValue("pk_ssitem", null);// ����������
			save_bodyVO.setAttributeValue("scomment", null);// ժҪ
			save_bodyVO.setAttributeValue("subjcode", null);// ��Ŀ����
			save_bodyVO.setAttributeValue("pk_currtype",
					save_headVO.getPk_currtype());// ����
			save_bodyVO.setAttributeValue("rate", 1);// ��֯���һ���
			save_bodyVO.setAttributeValue("pk_deptid",
					save_headVO.getPk_deptid());// ����
			save_bodyVO.setAttributeValue("pk_deptid_v", null);// �� ��
			save_bodyVO.setAttributeValue("pk_psndoc",
					save_headVO.getPk_psndoc());// ҵ��Ա
			save_bodyVO.setAttributeValue("money_de", new UFDouble(
					receivableBodyVO.getLocal_money_de()));// �跽ԭ�ҽ��
			save_bodyVO.setAttributeValue("quantity_de", new UFDouble(
					receivableBodyVO.getQuantity_de()));// �跽����
			save_bodyVO.setAttributeValue("money_bal", null);// ԭ�����
			save_bodyVO.setAttributeValue("local_money_bal", null);// ��֯�������
			save_bodyVO.setAttributeValue("quantity_bal", null);// �������
			save_bodyVO.setAttributeValue("local_tax_de",
					receivableBodyVO.getLocal_tax_de());// ˰��
			save_bodyVO.setAttributeValue("notax_de",
					receivableBodyVO.getNotax_de());// �跽ԭ����˰���
			save_bodyVO.setAttributeValue("local_notax_de", null);// ��֯������˰���
			save_bodyVO.setAttributeValue("price", save_bodyVO.getPrice());// ����
			save_bodyVO.setAttributeValue("taxprice",
					receivableBodyVO.getTaxprice());// ��˰����
			save_bodyVO.setAttributeValue("taxrate", new UFDouble(
					receivableBodyVO.getTaxrate()));// ˰��
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
			save_bodyVO.setAttributeValue("ordercubasdoc", null);// �����ͻ�
			save_bodyVO.setAttributeValue("innerorderno", null);// ����������
			save_bodyVO.setAttributeValue("assetpactno", null);// �ʲ���ͬ��
			save_bodyVO.setAttributeValue("contractno", null);// ��ͬ��
			save_bodyVO.setAttributeValue("freecust", null);// ɢ��
			save_bodyVO.setAttributeValue("purchaseorder", null);// ������
			save_bodyVO.setAttributeValue("invoiceno", null);// ��Ʊ��
			save_bodyVO.setAttributeValue("outstoreno", null);// ���ⵥ��
			save_bodyVO.setAttributeValue("checkelement", null);// ���κ���Ҫ��
			save_bodyVO.setAttributeValue("def30", null);// �Զ�����30
			save_bodyVO.setAttributeValue("def29", null);// �Զ�����29
			save_bodyVO.setAttributeValue("def28", null);// �Զ�����28
			save_bodyVO.setAttributeValue("def27", null);// �Զ�����27
			save_bodyVO.setAttributeValue("def26", null);// �Զ�����26
			save_bodyVO.setAttributeValue("def25", null);// �Զ�����25
			save_bodyVO.setAttributeValue("def24", null);// �Զ�����24
			save_bodyVO.setAttributeValue("def23", null);// �Զ�����23
			save_bodyVO.setAttributeValue("def22", null);// �Զ�����22
			save_bodyVO.setAttributeValue("def21", null);// �Զ�����21
			save_bodyVO.setAttributeValue("def20", null);// �Զ�����20
			save_bodyVO.setAttributeValue("def19", null);// �Զ�����19
			save_bodyVO.setAttributeValue("def18", null);// �Զ�����18
			save_bodyVO.setAttributeValue("def17", null);// �Զ�����17
			save_bodyVO.setAttributeValue("def16", null);// �Զ�����16
			save_bodyVO.setAttributeValue("def15", null);// �Զ�����15
			save_bodyVO.setAttributeValue("def14", null);// �Զ�����14
			save_bodyVO.setAttributeValue("def13",
					receivableBodyVO.getContractname());// ��ͬ����
			save_bodyVO.setAttributeValue("def12", receivableBodyVO.getRowid());// ��ҵ�շ�ϵͳ������ID
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
			save_bodyVO.setAttributeValue("project", null);// ��Ŀ
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

			String budgetsub = receivableBodyVO.getBudgetsub();// Ԥ���Ŀ
			HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
					.getBudgetsubInfo(budgetsub);
			if (budgetsubInfo == null) {
				throw new BusinessException("Ԥ���Ŀ[" + budgetsub
						+ "]δ����NC���������������Ϣ!");
			}
			save_bodyVO.setAttributeValue(ReceivableBillItemVO.DEF1,
					budgetsubInfo.get("pk_obj"));//

			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new ReceivableBillItemVO[0]));

		return aggvo;
	}
}
