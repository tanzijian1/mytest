package nc.bs.tg.outside.salebpm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import nc.bs.os.outside.TGCallUtils;
import nc.bs.tg.outside.utils.BPMBillUtil;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.pub.BusinessException;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class LLSaleNoteBPMUtils extends BPMBillUtil {
	static LLSaleNoteBPMUtils utils;
	IMDPersistenceQueryService mdQryService = null;

	public static LLSaleNoteBPMUtils getUtils() {
		if (utils == null) {
			utils = new LLSaleNoteBPMUtils();
		}
		return utils;
	}
	public void chargebackBillToImg(Map<String,String> map)
					throws BusinessException {
		String billid = map.get("billid");
		String barcode = map.get("barcode");
		String type = map.get("type");
		String useraccount = map.get("useraccount");
		String username = map.get("username");
		String phone = map.get("phone");
		String remark = map.get("remark");
		String billtype = map.get("billtype");
		String optype = map.get("optype");
		String xml = createRefund_xml(barcode, type, useraccount, username,
				phone, remark, billtype, optype);
		TGCallUtils.getUtils().onDesCallService(billid, "IMG", "onChargebackNoticeImg", xml);
	}

	public String createRefund_xml(String barcode, String type,
			String useraccount, String username, String phone, String remark,
			String billtype, String optype) {
		String servername = "rejectBill";
		Element services = initElement(servername);
		Element service = services.addElement("service");
		service.addElement("serviceid").addText("1");
		if (barcode != null)
			service.addElement("barcode").addText(barcode);
		if (type != null)
			service.addElement("type").addText(type);
		if (useraccount != null)
			service.addElement("useraccount").addText(useraccount);
		if (username != null)
			service.addElement("username").addText(username);
		if (phone != null)
			service.addElement("phone").addText(phone);
		if (remark != null)
			service.addElement("remark").addText(remark);
		if (billtype != null)
			service.addElement("billtype").addText(billtype);
		if (optype != null)
			service.addElement("optype").addText(optype);
		return service.getDocument().asXML();
	}
	/**
	 * ��ʼ�����õ�XML��Ϣ
	 * 
	 * @param servername
	 * @return
	 */
	private Element initElement(String servername) {
		Document doc = DocumentHelper.createDocument();
		String clientcode = "FK";
		String servicecode = "ImageCenter";
		Date date = new Date();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = formater.format(date);
		String key = "yonyou";
		String ticket = time + servicecode + clientcode + key;
		String ticketMD5 = DigestUtils.md5Hex(ticket);
		doc.setXMLEncoding("UTF-8");
		Element params = doc.addElement("params");
		Element safety = params.addElement("safety");
		safety.addElement("clientcode").addText(clientcode);
		safety.addElement("servicecode").addText(servicecode);
		safety.addElement("time").addText(time);
		safety.addElement("ticket").addText(ticketMD5);
		Element serverbody = params.addElement("serverbody");
		serverbody.addElement("servername").addText(servername);
		serverbody.addElement("servertype");
		Element services = serverbody.addElement("services");
		return services;
	}
}
