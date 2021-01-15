package nc.impl.tg.outside;

import nc.bs.tg.outside.business.utils.changeBill.BusinessChangeBill;
import nc.bs.tg.outside.business.utils.incomebusi.IncomeBusinessUtils;
import nc.bs.tg.outside.business.utils.billutils.BusiReceBillUtils;
import nc.bs.tg.outside.business.utils.bxBill.BusinessContractBxBillUtils;
import nc.bs.tg.outside.business.utils.bxBill.BusinessHistoryBxBillUtils;
import nc.bs.tg.outside.business.utils.orgquery.QryOrgUtils;
import nc.bs.tg.outside.business.utils.paybill.BusinessPayBillUtills;
import nc.bs.tg.outside.business.utils.receipt.DeleteReceiptBillUtils;
import nc.bs.tg.outside.business.utils.receipt.VerifiReceiptBIllUtils;
import nc.itf.tg.outside.BusinessBillCont;
import nc.itf.tg.outside.ISyncBusinessBillService;
import uap.json.JSONObject;

public class SyncBusinessBillServicel implements ISyncBusinessBillService {

	public String onSyncBill_RequiresNew(JSONObject value, String billtype)
			throws Exception {
		String result = null;
		if (BusinessBillCont.DOC_01.equals(billtype)) {
			// result = QryOrgUtils.getUtils().getValue(value, billtype);
		} else if (BusinessBillCont.DOC_02.equals(billtype)) {// ��ҵ�տɾ��
			result = DeleteReceiptBillUtils.getUtils()
					.getValue(value, billtype);
		} else if (BusinessBillCont.DOC_04.equals(billtype)) {// ��ҵ�տ�ӿ�
			result = VerifiReceiptBIllUtils.getUtils()
					.getValue(value, billtype);
		} else if (BusinessBillCont.DOC_05.equals(billtype)) {// ��ҵ���빤�����ӿ�
			result = IncomeBusinessUtils.getUtils().getValue(value, billtype);
		} else if (BusinessBillCont.DOC_10.equals(billtype)) {// ��ҵ�˿�ӿ�
			result = BusinessPayBillUtills.newInstance().onSyncBill(value,
					billtype, billtype);
		} else if (BusinessBillCont.DOC_11.equals(billtype)) {// �·Ǻ�ͬ�������д��ӿ�
			result = BusinessContractBxBillUtils.newInstance().onSyncBill(value,
					billtype, billtype);
		} else if (BusinessBillCont.DOC_12.equals(billtype)) {// ��ʷ���ݲ�¼��д��ӿ�
			result = BusinessHistoryBxBillUtils.newInstance().onSyncBill(value,
					billtype, billtype);
		} else if (BusinessBillCont.DOC_13.equals(billtype)) {// ��Ʊ��������Ʊ����
			result = BusinessChangeBill.newInstance().onSyncBill(value,
					billtype, billtype);
		} else if (BusinessBillCont.BUSIREC.equals(billtype)) {
			result = BusiReceBillUtils.getUtils().onSyncBill(value, billtype,
					billtype);
		}
		return result;
	}
}
