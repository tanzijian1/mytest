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
	 * 推送影像方法
	 * 
	 * @param billCode
	 * @throws Exception
	 */
	public void onPushBillToImgInv(String billCode, Boolean isLLBill)
			throws Exception {
		// AggInvoiceVO aggVO = (AggInvoiceVO) bill;
		Map<String, Object> formData = getFormData();
		Map<String, Object> formBody = getFormBody(billCode);
		Logger.error("huangxj进入：pushBillToImg");
		PushIMGBillUtils.getUtils().pushBillToImg(formData, formBody, isLLBill);
	}

	/**
	 * 表体信息转换
	 * 
	 * @param billCode
	 * @return
	 */
	private Map<String, Object> getFormBody(String billCode) {
		Map<String, Object> formData = new HashMap<String, Object>();// 表体数据
		formData = pushBillsToInvBody(billCode);
		return formData;
	}

	/**
	 * 表头信息转换
	 * 
	 * @return
	 */
	private Map<String, Object> getFormData() {
		Map<String, Object> formData = new HashMap<String, Object>();// 表头数据
		formData = pushBillsToInvHead();
		return formData;
	}

	private Map<String, Object> pushBillsToInvHead() {
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
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
		Map<String, Object> purchase = new HashMap<String, Object>();// 表体数据
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
