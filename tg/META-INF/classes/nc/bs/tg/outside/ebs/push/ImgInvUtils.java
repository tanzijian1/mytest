package nc.bs.tg.outside.ebs.push;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import nc.bs.logging.Logger;
import nc.bs.tg.outside.img.utils.PushIMGBillUtils;

public class ImgInvUtils {

	static ImgInvUtils utils;

	public static ImgInvUtils getUtils() {
		if (utils == null) {
			utils = new ImgInvUtils();
		}
		return utils;
	}

	/**
	 * ����Ӱ�񷽷�
	 * 
	 * @param billCode
	 * @throws Exception
	 */
	public void onPushBillToImgInv(String billCode, Boolean isLLBill)
			throws Exception {
		// AggInvoiceVO aggVO = (AggInvoiceVO) bill;
		Map<String, Object> formData = getFormData();
		Map<String, Object> formBody = getFormBody(billCode);
		Logger.error("huangxj���룺pushBillToImg");
		PushIMGBillUtils.getUtils().pushBillToImg(formData, formBody, isLLBill);
	}

	/**
	 * ������Ϣת��
	 * 
	 * @param billCode
	 * @return
	 */
	private Map<String, Object> getFormBody(String billCode) {
		Map<String, Object> formData = new HashMap<String, Object>();// ��������
		formData = pushBillsToInvBody(billCode);
		return formData;
	}

	/**
	 * ��ͷ��Ϣת��
	 * 
	 * @return
	 */
	private Map<String, Object> getFormData() {
		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData = pushBillsToInvHead();
		return formData;
	}

	private Map<String, Object> pushBillsToInvHead() {
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
		String clientcode = "FK";
		String servicecode = "ImageCenter";
		Date date = new Date();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = formater.format(date);
		String key = "yonyou";
		String ticket = time + servicecode + clientcode + key;
		String ticketMD5 = DigestUtils.md5Hex(ticket);
		purchase.put("time", time);
		purchase.put("ticket", ticketMD5);
		purchase.put("clientcode", clientcode);
		purchase.put("servicecode", servicecode);
		return purchase;
	}

	private Map<String, Object> pushBillsToInvBody(String billCode) {
		Map<String, Object> purchase = new HashMap<String, Object>();// ��������
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapBody = new HashMap<String, Object>();
		mapBody.put("barcode", billCode);
		mapBody.put("serviceid", "");
		list.add(mapBody);
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
				.fromObject(list);
		purchase.put("services", jsonArray);
		purchase.put("servername", "findBillcodeInvoice");
		return purchase;
	}
}
