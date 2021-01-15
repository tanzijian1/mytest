package nc.bs.tg.outside.ebs.pub;

import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.push.ImgInvUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;

import org.dom4j.DocumentException;
import org.dom4j.tree.DefaultDocument;
import org.jdom.Document;

/**
 * 
 * 
 * @author ASUS
 * 
 */
public class ImgQryUtils extends EBSBillUtils {
	static ImgQryUtils utils;

	public static ImgQryUtils getUtils() {
		if (utils == null) {
			utils = new ImgQryUtils();
		}
		return utils;
	}

	public String onQryImgData(String barcode) throws BusinessException {
		// String url = iGuoXinImage.createImagePath(barcode);
		// try {
		// Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+url);
		// } catch (IOException e) {
		// // TODO �Զ����ɵ� catch ��
		// e.printStackTrace();
		// }
		return null;
	}

//	public void onInsertInv(String imgid) throws Exception {
//		IGuoXinImage iGuoXinImage = NCLocator.getInstance().lookup(
//				IGuoXinImage.class);
//		String invXml = iGuoXinImage.createImageInv(imgid);
//		IInvoiceMaintain iInvoiceMaintain = NCLocator.getInstance().lookup(
//				IInvoiceMaintain.class);
//		getDocByString(invXml);
//		StringReader read = new StringReader(invXml);
//		// iGuoXinImage.startWorkFlow(vbilltype, billid, pk_org, scanUser,
//		// billMny, scantype, billno);
//		// subString("<errormsg>","</errormsg>");
//		// �����µ�����ԴSAX ��������ʹ�� InputSource ������ȷ����ζ�ȡ XML ����
//		InputSource source = new InputSource(read);
//		// ����һ���µ�SAXBuilder
//		SAXBuilder sb = new SAXBuilder();
//		// ͨ������Դ����һ��Document
//		Document doc = sb.build(source);
//		Element root = doc.getRootElement();
//		List<Element> children = root.getChildren();
//		AggInvoiceVO aggvo = new AggInvoiceVO();
//		InvoiceVO headvo = new InvoiceVO();
//		String pk_org = "000112100000000005FD";
//		headvo.setStatus(VOStatus.NEW);
//		headvo.setAttributeValue("dr", 0);
//		headvo.setPk_billtype("HZ01");
//		headvo.setVbillstatus(-1);
//		headvo.setCreator("#UAP#");
//		headvo.setModifier("#UAP#");
//		headvo.setBillmaker("#UAP#");
//		headvo.setPk_org(pk_org);
//		headvo.setPk_group(pk_org);
//		headvo.setPk_org_v(getPk_org_vByPK(pk_org));
//		List<InvoiceBVO> listBvo = null;
//		InvoiceBVO bvo = new InvoiceBVO();
//		bvo.setStatus(VOStatus.NEW);
//		bvo.setAttributeValue("dr", 0);
//		bvo.setPk_org(pk_org);
//		bvo.setPk_group(pk_org);
//		bvo.setPk_org_v(getPk_org_vByPK(pk_org));
//		for (Element child : children) {
//			listBvo = new ArrayList<InvoiceBVO>();
//			// List<Attribute> attributes = child.getAttributes();
//			List<Element> childrenList = child.getChildren();
//			if (childrenList.size() > 0 && childrenList != null) {
//				for (Element element : childrenList) {
//					if (element.getName().equals("invoiceqty")) {
//						aggvo.getParent().setAttributeValue("",
//								element.getValue());
//					}
//				}
//				listBvo.add(bvo);
//			}
//		}
//		aggvo.setParentVO(headvo);
//		aggvo.setChildrenVO(listBvo.toArray(new InvoiceBVO[0]));
//		// AggInvoiceVO[] saveAggvo = iInvoiceMaintain.insert(new
//		// AggInvoiceVO[]{aggvo}, null);
//		HashMap eParam = new HashMap();
//		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
//				PfUtilBaseTools.PARAM_NOTE_CHECKED);
//		AggInvoiceVO[] appAggvo = (AggInvoiceVO[]) getPfBusiAction()
//				.processAction("SAVEBASE", "HZ01", null, aggvo, null, eParam);
//		getPfBusiAction().processAction("APPROVE", "HZ01", null, appAggvo[0],
//				null, eParam);
//		// Document doc = DocumentHelper.parseText(invXml);
//		// Element root = doc.getRootElement();
//		// String
//		// invoiceqty=root.element("items").element("item").element("invoiceqty").getTextTrim();//��Ʊ����
//		// String
//		// invoicecode=root.element("items").element("item").element("invoicecode").getTextTrim();//��Ʊ����
//		// String
//		// inname=root.element("items").element("item").element("inname").getTextTrim();//��������
//		// String
//		// invoicedate=root.element("items").element("item").element("invoicedate").getTextTrim();//��Ʊ����
//		// String
//		// outname=root.element("items").element("item").element("outname").getTextTrim();//��������
//		// String
//		// ratetaxlower=root.element("items").element("item").element("ratetaxlower").getTextTrim();//��˰�ϼƽ��
//		// String
//		// taxpayernosales=root.element("items").element("item").element("taxpayernosales").getTextTrim();//��˰��ʶ���(����)
//		// String
//		// invoicetype=root.element("items").element("item").element("invoicetype").getTextTrim();//��˰��ʶ���(����)
//	}
	
	public void onInsertInv(String imgid,Boolean isLLBill) throws Exception{
		Logger.error("huangxj���룺onPushBillToImgInv");
		ImgInvUtils.getUtils().onPushBillToImgInv(imgid,isLLBill);
	}

	public static Document getDocByString(String xml) throws BusinessException {
		org.jdom.Document doc = null;
		try {
			org.dom4j.tree.DefaultDocument document = (DefaultDocument) org.dom4j.DocumentHelper
					.parseText(xml);
			document.getRootElement();
			doc =null;
		} catch (DocumentException e) {
			// Logger.error(e.getMessage(), e);
			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("1054003_0", "01054003-0039"));
		}
		return doc;
	}

}
