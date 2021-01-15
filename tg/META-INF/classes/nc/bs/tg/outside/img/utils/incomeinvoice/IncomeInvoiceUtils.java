/**
 * <p>Title: IncomeInvoiceUtils.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��18�� ����10:55:43

 * @version 1.0
 */

package nc.bs.tg.outside.img.utils.incomeinvoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.img.utils.IMGBillUtils;
import nc.itf.tg.outside.IMGBillCont;
import nc.vo.hzvat.invoice.AggInvoiceVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * ����ʱ�䣺2019��9��18�� ����10:55:43  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * �ļ����ƣ�IncomeInvoiceUtils.java  
 * ��˵����  
 */

/**
 * <p>
 * Title: IncomeInvoiceUtils<��p>
 * 
 * <p>
 * Description: <��p>
 * 
 * <p>
 * Company: hanzhi<��p>
 * 
 * @author bobo
 * 
 * @date 2019��9��18�� ����10:55:43
 */

public class IncomeInvoiceUtils extends IMGBillUtils {
	static IncomeInvoiceUtils utils;

	public static IncomeInvoiceUtils getUtils() {
		if (utils == null) {
			utils = new IncomeInvoiceUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getIMGUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		// ����json����,��ȡ��ͷ����
		JSONObject headInfo = (JSONObject) value.get("headInfo");
		// ���ݷ�Ʊ�ţ���ȡΨһ�ֶ�ֵ
		String fph = headInfo.getString("fph");// ҵ�񵥾ݷ�Ʊ��

		// ҵ�񵥾ݷ�Ʊ�������п��ƺ���־���
		String billNo = IMGBillCont.getBillNameMap().get(billtype) + ":" + fph;
		/*
		 * String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":" +
		 * saleno;
		 */

		// ���Ӱ��ϵͳҵ�񵥾��Ƿ����**************
		AggInvoiceVO aggVO = (AggInvoiceVO) getBillVO(AggInvoiceVO.class,
				"isnull(dr,0)=0 and fph = '" + fph + "'");

		if (aggVO != null) {
			throw new BusinessException("��" + billNo + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݣ���Ʊ�ţ���"
					+ fph + "��,�����ظ��浥");
		}

		BPMBillUtils.addBillQueue(billNo);// ���Ӷ��д���***************
		try {
			// **********************
			// ת������ΪVO
			// *********************************
			IncomeInvoiceConvertor convertor = new IncomeInvoiceConvertor();
			// Ĭ�ϼ���PK
			convertor.setDefaultGroup("000112100000000005FD");
			// ���ñ���key������ӳ��
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("InvoiceBVO", "���Ʊ����");
			convertor.setBVOKeyName(bVOKeyName);
			// ���ñ�ͷkey������ӳ��
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("InvoiceVO", "���Ʊ��ͷ");
			convertor.setHVOKeyName(hVOKeyName);
			// ��ͷ��ֵУ���ֶ�
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> bInvoiceVOKeyName = new HashMap<String, String>();
			bInvoiceVOKeyName.put("pk_org", "������֯");
			bInvoiceVOKeyName.put("fplx", "��Ʊ����");
			bInvoiceVOKeyName.put("fph", "��Ʊ��");
			bInvoiceVOKeyName.put("fpdm", "��Ʊ����");
			bInvoiceVOKeyName.put("def5", "˰��");
			bInvoiceVOKeyName.put("def1", "�Ƿ�����α");
			bInvoiceVOKeyName.put("costmoney", "��˰����ܶ�");
			bInvoiceVOKeyName.put("costmoneyin", "��˰�ϼ��ܶ�");
			bInvoiceVOKeyName.put("kprq", "��Ʊ����");
			bInvoiceVOKeyName.put("sellname", "���۷�����");
			bInvoiceVOKeyName.put("sellnsrsbh", "���۷���˰��ʶ���");
			bInvoiceVOKeyName.put("selladrpho", "���۷���ַ�͵绰");
			bInvoiceVOKeyName.put("sellbank", "���۷������м��˺�");
			bInvoiceVOKeyName.put("kpr", "��Ʊ��");
			bInvoiceVOKeyName.put("htbh", "��ͬ���");
			hValidatedKeyName.put("InvoiceVO", bInvoiceVOKeyName);

			// �����ֵУ���ֶ�
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> bInvoiceBVOKeyName = new HashMap<String, String>();
			bInvoiceBVOKeyName.put("pk_product", "�����Ӧ˰����");
			bInvoiceBVOKeyName.put("def1", "˰�շ���");
			bInvoiceBVOKeyName.put("pk_math", "��λ");
			bInvoiceBVOKeyName.put("num", "����");
			bInvoiceBVOKeyName.put("priceouttax", "��˰����");
			bInvoiceBVOKeyName.put("moneyouttax", "��˰���");
			bInvoiceBVOKeyName.put("taxrate", "˰��");
			bInvoiceBVOKeyName.put("moneytax", "˰��");
			bValidatedKeyName.put("InvoiceBVO", bInvoiceBVOKeyName);

			// ���������ֶ�
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("InvoiceVO-pk_org");// ������֯
			refKeys.add("InvoiceVO-fplx");// ��Ʊ����
			refKeys.add("InvoiceVO-thyy");// �˻�ԭ��

			convertor.sethValidatedKeyName(hValidatedKeyName);
			convertor.setbValidatedKeyName(bValidatedKeyName);
			convertor.setRefKeys(refKeys);

			// *********************************
			AggInvoiceVO billvo = (AggInvoiceVO) convertor.castToBill(value,
					AggInvoiceVO.class, aggVO);

			// ********************��ͷ������Ϣ����***************
			billvo.getParentVO().setPk_org(
					convertor.getRefAttributePk("InvoiceVO-pk_org", billvo
							.getParentVO().getPk_org()));
			billvo.getParentVO().setFplx(
					convertor.getRefAttributePk("InvoiceVO-fplx", billvo
							.getParentVO().getFplx()));
			billvo.getParentVO().setThyy(
					convertor.getRefAttributePk("InvoiceVO-thyy", billvo
							.getParentVO().getThyy()));
			// ********************��ͷ������Ϣ����***************

			// ����NCĬ����Ϣ
			billvo.getParentVO().setPk_billtype("HZ01");
			billvo.getParentVO().setDbilldate(new UFDate());
			billvo.getParentVO().setVbillstatus(-1);
			// Ĭ�ϴ�����IMG
			billvo.getParentVO().setCreator("1001ZZ100000001MYVM7");
			billvo.getParentVO().setCreationtime(new UFDateTime());
			// Ĭ���Ƶ���IMG
			billvo.getParentVO().setBillmaker("1001ZZ100000001MYVM7");
			billvo.getParentVO().setDmakedate(new UFDateTime());

			HashMap<String, String> eParam = new HashMap<String, String>();
			// �־û�����VO
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "HZ01",
					null, billvo, null, eParam);
			// **************************
			AggInvoiceVO[] billVOs = (AggInvoiceVO[]) obj;
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("billid", billVOs[0].getPrimaryKey());
			dataMap.put("billno", (String) billVOs[0].getParentVO()
					.getAttributeValue("vbillno"));
			// **************************
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("��" + billNo + "��," + e.getMessage(), e);
		} finally {
			BPMBillUtils.removeBillQueue(billNo);
		}
	}
}
