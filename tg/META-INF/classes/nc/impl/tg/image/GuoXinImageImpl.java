package nc.impl.tg.image;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.image.IGuoXinImage;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;
import nc.vo.tg.image.CallimgLogVO;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xerces.dom.DocumentImpl;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class GuoXinImageImpl implements IGuoXinImage {
	/**
	 * ��ȡӰ���ַ
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getImgUrl() throws BusinessException {
		return OutsideUtils.getOutsideInfo("IMG01");
	}

	@Override
	public String createImagePath(String barcode) throws BusinessException {

		// TODO �Զ����ɵķ������
		String userid = InvocationInfoProxy.getInstance().getUserId();
		UserVO userVO = getUser(userid);

		String xml = createImagePath_XML(barcode, userVO);
		String retXml = callImageService(xml);
		Element root = xmlConvertElement(retXml);
		String url = root.element("items").element("item")
				.elementText("success");

		CallimgLogVO callimgLogVO = new CallimgLogVO();
		callimgLogVO.setServicename("createImagePath");
		callimgLogVO.setBarcode(barcode);// Ӱ�������
		callimgLogVO.setUseraccount(userVO.getUser_code());
		callimgLogVO.setUsername(userVO.getUser_name());
		callimgLogVO.setSendmess(xml);// ���͵�XML
		callimgLogVO.setSendtime(new UFDateTime());// ����ʱ��
		callimgLogVO.setRemess(retXml);// Ӱ��ϵͳ���ص�XML
		callimgLogVO.setReaddress(url);// Ӱ��ϵͳ���ص�url��ַ

		// ��־��¼
		if ("0".equals(root.elementText("result"))) {
			callimgLogVO.setStatus(VOStatus.NEW);
			new BaseDAO().insertVO(callimgLogVO);
		} else {
			callimgLogVO.setStatus(VOStatus.NEW);
			new BaseDAO().insertVO(callimgLogVO);
			throw new BusinessException(root.elementText("errormsg"));
		}
		return url;
	}

	/**
	 * �����û�ID��ȡ�û�����UserVO
	 * 
	 * @param userid
	 * @return UserVO
	 */
	private UserVO getUser(String userid) {
		UserVO userVO = new UserVO();
		BaseDAO dao = new BaseDAO();
		try {
			userVO = (UserVO) dao.retrieveByPK(UserVO.class, userid);
			// userVO = (UserVO) bs.retrieveByPK(UserVO.class, userid);
		} catch (BusinessException e) {
			Logger.error("��ѯ�û�VO�쳣���û�id��" + userid, e);
		}
		return userVO;
	}

	private String createImagePath_XML(String barcode, UserVO userVO) {
		String servername = "createImagePath";
		Element services = initElement(servername);
		Element service = services.addElement("service");
		service.addElement("barcode").addText(barcode);
		service.addElement("type").addText("1");// �������ͣ�1/���˵� 2/�ʽ𵥾�
		service.addElement("useraccount").addText(userVO.getUser_code());
		service.addElement("showother").addText("1");// ��Ŀ��¼
		// �Ƿ��������Ӱ��1���������ۣ����������ԡ�
		// service.addElement("evaluation")
		// .addText(isEvaluate == true ? "1" : "0");
		String xml = services.getDocument().asXML();
		return xml;
	}

	/**
	 * ����Ӱ��ӿ�
	 * 
	 * @param xml
	 * @return
	 * @throws BusinessException
	 */
	private String callImageService(String xml) throws BusinessException {
		// PropertiesUtil util = PropertiesUtil
		// .getInstance("image_url.properties");
		String url = getImgUrl();
		String result = "";
		try {
			Object[] results = null;
			results = getClient(url).invoke("ImageInterfaceService",
					new Object[] { xml.toString() });
			Logger.error("���صĲ�����" + result);
			result = (String) results[0];
		} catch (Exception e) {
			throw new BusinessException("Ӱ����ýӿ������쳣:" + e.getMessage());
		}
		return result;
	}

	/**
	 * ���÷�Ʊ�ӿ�
	 * 
	 * @param xml
	 * @return
	 * @throws BusinessException
	 */
	private String callImageInvService(String xml) throws BusinessException {
		// PropertiesUtil util = PropertiesUtil
		// .getInstance("image_url.properties");
		// String url = util.readValue("imgInvUrl");
		String url = getImgUrl();
		String result = "";
		try {
			Object[] results = null;
			results = getClient(url).invoke("ImageInterfaceService",
					new Object[] { xml.toString() });
			DocumentImpl dp = (DocumentImpl) results[0];
			result = dp.getFirstChild().getTextContent();
			Logger.error("���صĲ�����" + result);
			// result = (String) results[0];
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException("�ӿ������쳣��" + e.getMessage());
		}
		return result;
	}

	private Element xmlConvertElement(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		Element root = doc.getRootElement();
		return root;
	}

	private Client getClient(String url) throws BusinessException {
		String endpoint = url;
		Client client = null;
		try {
			client = new Client(new URL(endpoint));
			client.setProperty(CommonsHttpMessageSender.HTTP_TIMEOUT,
					String.valueOf(30000));// ���÷��͵ĳ�ʱ����,��λ�Ǻ���;
		} catch (MalformedURLException e) {
			throw new BusinessException(e);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
		return client;
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

	@Override
	public void startWorkFlow(String vbilltype, String billid, String pk_org,
			String scanUser, UFDouble billMny, String scantype, String billno,
			HashMap<String, String> map) throws BusinessException {
		Logger.init("hzssc");

		BaseDAO dao = new BaseDAO();

		String barcode = map.get("barcode");
		Map<String, String> resultmap = (Map<String, String>) dao.executeQuery(
				"select def11,code  from org_orgs where pk_org='" + pk_org
						+ "'", new MapProcessor());
		if (resultmap == null) {
			throw new BusinessException("����Ӱ����֯����Ϊ��");
		}
		if ("��ҵ��ģʽ".equals(resultmap.get("def11"))) {
			return;
		}
		if (resultmap.get("code") == null) {
			throw new BusinessException("��֯����Ϊ��");
		}

		// pk_org=(String)resultmap.get("code");//ҵ�������֯�Ĵ�����

		UserVO userVO = getUser(scanUser);
		CallimgLogVO callimgLogVO = getCallimgLogVO(barcode,
				userVO.getUser_code());
		// if (callimgLogVO != null) {
		// Logger.error("�Ѿ�����Ӱ�񴥷����죬�������ٴη��ͣ����ݺţ�" + billno + "����id��" + billid);
		// throw new BusinessException("�Ѿ�����Ӱ�񴥷����죬�������ٴη��ͣ����ݺţ�" + billno
		// + "����id��" + billid);
		// }
		String xml = startWorkFlow_XML(vbilltype, billid, pk_org, scanUser,
				billMny, scantype, billno, barcode, map);
		try {
			String retXml = callImageService(xml);
			Element root = xmlConvertElement(retXml);
			// ���سɹ�����¼��־
			callimgLogVO = new CallimgLogVO();
			callimgLogVO.setServicename("startWorkFlow");
			callimgLogVO.setBarcode(barcode);// Ӱ�������
			callimgLogVO.setBilltypecode(vbilltype);// ��������
			callimgLogVO.setTypecode("1");// 1�����˵� �� 2���ʽ� �� 3����ͬ
			callimgLogVO.setUseraccount(userVO.getUser_code());
			callimgLogVO.setUsername(userVO.getUser_name());
			callimgLogVO.setBillcode(billno);// ���ݺ�
			callimgLogVO.setPk_org(pk_org);// ��˾id
			callimgLogVO.setSendmess(xml);// ���͵�XML
			callimgLogVO.setSendtime(new UFDateTime());// ����ʱ��
			callimgLogVO.setPk_billid(billid);// ����id
			callimgLogVO.setOptype(scantype);// ɨ������
			callimgLogVO.setRemess(retXml);// Ӱ��ϵͳ���ص�XML
			callimgLogVO.setStatus(VOStatus.NEW);
			// dao.insertVO(callimgLogVO);
			// ��־��¼
			if ("1".equals(root.elementText("result"))) {
				// callimgLogVO.setStatus(VOStatus.NEW);
				// new BaseDAO().insertVO(callimgLogVO);
				throw new BusinessException("Ӱ����췴������:"
						+ root.elementText("errormsg"));
			} else {
				// throw new BusinessException(root.elementText("errormsg"));
			}
		} catch (Exception e) {
			throw new BusinessException("����Ӱ�����ʧ�ܣ���" + e.getMessage() + "��");
		} finally {
			try {
				callimgLogVO.setStatus(VOStatus.NEW);
				// new BaseDAO().insertVO(callimgLogVO);
				IPushBPMBillFileService service = NCLocator.getInstance()
						.lookup(IPushBPMBillFileService.class);
				service.saveLog_RequiresNew(callimgLogVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private CallimgLogVO getCallimgLogVO(String barcode, String usercode)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from image_callimg_log ");
		sql.append(" where servicename='startWorkFlow' ");
		sql.append(" and barcode ='").append(barcode).append("' ");
		sql.append(" and useraccount ='").append(usercode).append("' ");
		CallimgLogVO callimgLogVO = new CallimgLogVO();
		BaseDAO dao = new BaseDAO();
		try {
			callimgLogVO = (CallimgLogVO) dao.executeQuery(sql.toString(),
					new BeanProcessor(CallimgLogVO.class));
		} catch (BusinessException e) {
			Logger.error("��ѯ���ʹ������־��¼�쳣������ţ�" + barcode + "���û��˺ţ�" + usercode,
					e);
			throw new BusinessException("��ѯ���ʹ������־��¼�쳣������ţ�" + barcode
					+ "���û��˺ţ�" + usercode, e);
		}
		return callimgLogVO;
	}

	/**
	 * Ӱ��ϵͳ����ӿ�XML����
	 * 
	 * @param vbilltype
	 * @param billid
	 * @param pk_org
	 * @param scanUser
	 * @param billMny
	 * @param scantype
	 * @param pk_group
	 * @param billno
	 * @param warehousename
	 * @param warehousecode
	 * @return XML
	 * @throws BusinessException
	 */
	private String startWorkFlow_XML(String vbilltype, String billid,
			String pk_org, String scanUser, UFDouble billMny, String scantype,
			String billno, String barcode, HashMap<String, String> map)
			throws BusinessException {
		String dataSource = InvocationInfoProxy.getInstance()
				.getUserDataSource();
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		UserVO userVO = getUser(scanUser);
		String servername = "startWorkFlow";
		Element services = initElement(servername);
		Element service = services.addElement("service");
		// Ӱ�������
		// String barcode = vbilltype + billid;
		// service.addElement("barcode").addText(barcode);
		service.addElement("type").addText("1");// 1/���˵�
		String sql = "SELECT code FROM org_orgs WHERE pk_org = '" + pk_org
				+ "'";
		String branchcode = (String) EBSBillUtils.getUtils().getBaseDAO()
				.executeQuery(sql, new ColumnProcessor());
		service.addElement("branchcode").addText(branchcode);
		service.addElement("username").addText(userVO.getUser_name());
		service.addElement("useraccount").addText(userVO.getUser_code());
		service.addElement("billdate").addText(new UFDateTime().toString());
		service.addElement("billcode").addText(billid);
		service.addElement("billno").addText(billno);
		service.addElement("billtype").addText(vbilltype);
		
		if(StringUtils.isNotBlank(map.get("systype"))){
			service.addElement("systype").addText(map.get("systype")); //NC����ϵͳ��ϵͳ��ǣ�NCCW����KEYֵ��NCCWImage��NC�ѿ�ϵͳ��ϵͳ��ǣ�NCFK����KEYֵ��NCKFImage��
		}

		if (map.get("anattr2") == null
				|| "null".equals(map.get("anattr2"))
				|| !(new UFDouble(map.get("anattr2"))
						.compareTo(UFDouble.ZERO_DBL) > 0)) {
			// ��ֵ˰��Ʊ���Ϊ0�Ͳ�������ֵ˰��Ʊ������ֵ˰��Ʊ˰�
			map.remove("anattr2");
			map.remove("anattr3");
		}
		if (map != null) {
			for (String key : map.keySet()) {
				if (map.get(key) != null && !"".equals(map.get(key))
						&& !"null".equals(map.get(key))) {
					service.addElement(key).addText(map.get(key));
				}
			}
		}
		if (billMny != null && billMny.compareTo(UFDouble.ZERO_DBL) > 0) {
			service.addElement("amount").addText(billMny.toString());
		}

		service.addElement("groupid").addText(pk_group);
		service.addElement("scantype")
				.addText("2".equals(scantype) ? "2" : "1");
		if (!StringUtil.isEmpty(map.get("datasource")))// �ĳɴ��������
			service.addElement("datasource").addText(map.get("datasource"));
		String xml = services.getDocument().asXML();
		return xml;
	}

	@Override
	public String createImageInv(String barcode) throws BusinessException {
		String userid = InvocationInfoProxy.getInstance().getUserId();
		String retXml = null;
		CallimgLogVO callimgLogVO = new CallimgLogVO();
		try {
			String xml = createImagePath_XML(barcode);
			retXml = callImageInvService(xml);
			if (StringUtils.isBlank(retXml)) {
				throw new BusinessException("Ӱ��ϵͳid��" + barcode
						+ "��,δ����Ӱ��ϵͳ���� ,����!");
			}
			// ���سɹ�����¼��־
			callimgLogVO = new CallimgLogVO();
			callimgLogVO.setServicename("findBillcodeInvoice");
			callimgLogVO.setBarcode(barcode);// Ӱ�������
			callimgLogVO.setRemess(retXml);// Ӱ��ϵͳ���ص�XML
			callimgLogVO.setStatus(VOStatus.NEW);
		} catch (Exception e) {
			callimgLogVO = new CallimgLogVO();
			callimgLogVO.setServicename("findBillcodeInvoice");
			callimgLogVO.setBarcode(barcode);// Ӱ�������
			callimgLogVO.setRemess(e.getMessage());// Ӱ��ϵͳ���ص�XML�����򷵻�0
			callimgLogVO.setStatus(VOStatus.NEW);
		} finally {
			try {
				EBSBillUtils.getUtils().getBaseDAO().insertVO(callimgLogVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return retXml;
	}

	private String createImagePath_XML(String barcode) {
		// String dataSource = InvocationInfoProxy.getInstance()
		// .getUserDataSource();
		// String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String servername = "findBillcodeInvoice";
		Element services = initElementInv(servername);
		Element service = services.addElement("service");
		// Ӱ�������
		// String barcode = vbilltype + billid;
		service.addElement("serviceid");
		service.addElement("barcode").addText(barcode);
		// service.addElement("branchcode").addText(pk_org);
		// service.addElement("username").addText(userVO.getUser_name());
		// service.addElement("useraccount").addText(userVO.getUser_code());
		// service.addElement("billdate").addText(new UFDateTime().toString());
		// service.addElement("billcode").addText(billid);
		// service.addElement("billno").addText(billno);
		// service.addElement("billtype").addText(vbilltype);
		// if (billMny == null) {
		// billMny = UFDouble.ZERO_DBL;
		// }
		// service.addElement("amount").addText(billMny.toString());
		// service.addElement("groupid").addText(pk_group);
		// service.addElement("scantype")
		// .addText("2".equals(scantype) ? "2" : "1");
		// service.addElement("datasource").addText(dataSource);
		String xml = services.getDocument().asXML();
		return xml;
	}

	/**
	 * ��ʼ����Ʊ��XML��Ϣ
	 * 
	 * @param servername
	 * @return
	 */
	private Element initElementInv(String servername) {
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
		// serverbody.addElement("servertype");
		Element services = serverbody.addElement("services");
		return services;
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

	@Override
	public String delrefund(String barcode, String type, String useraccount,
			String username, String phone, String remark, String billtype,
			String optype) throws BusinessException {
		// TODO �Զ����ɵķ������
		String retXml = null;
		CallimgLogVO callimgLogVO = new CallimgLogVO();
		try {
			String xml = createRefund_xml(barcode, type, useraccount, username,
					phone, remark, billtype, optype);
			retXml = callImageService(xml);
			if (StringUtils.isBlank(retXml)) {
				throw new BusinessException("Ӱ��ϵͳ���ýӿڷ�����ϢΪ��");
			}
			if (retXml != null) {
				Element root = xmlConvertElement(retXml);
				String errmsg = root.element("items").element("item")
						.elementText("errormsg");
				if (errmsg != null && errmsg != "") {
					throw new BusinessException("image exception "
							+ root.elementText("errormsg"));
				}
			}
			// ���سɹ�����¼��־
			callimgLogVO.setServicename("rejectBill");
			callimgLogVO.setBarcode(barcode);// Ӱ�������
			callimgLogVO.setRemess(retXml);// Ӱ��ϵͳ���ص�XML
			callimgLogVO.setStatus(VOStatus.NEW);
		} catch (Exception e) {
			callimgLogVO = new CallimgLogVO();
			callimgLogVO.setServicename("rejectBill");
			callimgLogVO.setBarcode(barcode);// Ӱ�������
			callimgLogVO.setRemess(e.getMessage());
			callimgLogVO.setStatus(VOStatus.NEW);
			throw new BusinessException(e.getMessage());
		} finally {
			try {
				IPushBPMBillFileService service = NCLocator.getInstance()
						.lookup(IPushBPMBillFileService.class);
				service.saveLog_RequiresNew(callimgLogVO);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return null;
	}

	// @Override
	// public String pushImageAttachment(IBill bill) throws Exception {
	// String retXml = null;
	// CallimgLogVO callimgLogVO = new CallimgLogVO();
	// String xml = "";
	// try {
	// xml = createPushAttachment_xml(bill);
	// retXml = callImageService(xml);
	// if (StringUtils.isBlank(retXml)) {
	// throw new BusinessException("Ӱ��ϵͳ���ýӿڷ�����ϢΪ��");
	// }
	// if (retXml != null) {
	// Element root = xmlConvertElement(retXml);
	// String errmsg = root.elementText("errormsg");
	// if (errmsg != null && errmsg != "") {
	// throw new BusinessException("������Ӱ��ϵͳ�Ĵ�����Ϣ��"
	// + root.elementText("errormsg") + "��");
	// }
	// }
	// // ���سɹ�����¼��־
	// callimgLogVO.setServicename("synAduitFlow");
	// callimgLogVO.setBarcode((String) bill.getParent()
	// .getAttributeValue("def21"));// Ӱ�������
	// callimgLogVO.setSendmess(xml);
	// callimgLogVO.setRemess(retXml);// Ӱ��ϵͳ���ص�XML
	// callimgLogVO.setStatus(VOStatus.NEW);
	// } catch (Exception e) {
	// callimgLogVO = new CallimgLogVO();
	// callimgLogVO.setServicename("synAduitFlow");
	// callimgLogVO.setBarcode((String) bill.getParent()
	// .getAttributeValue("def21"));// Ӱ�������
	// callimgLogVO.setSendmess(xml);
	// callimgLogVO.setRemess(e.getMessage());
	// callimgLogVO.setStatus(VOStatus.NEW);
	// throw new BusinessException(e.getMessage());
	// } finally {
	// try {
	// IPushBPMBillFileService service = NCLocator.getInstance()
	// .lookup(IPushBPMBillFileService.class);
	// service.saveLog_RequiresNew(callimgLogVO);
	// } catch (Exception e) {
	// Logger.error(e.getMessage(), e);
	// }
	// }
	//
	// return null;
	// }
	//
	// private String createPushAttachment_xml(IBill bill)
	// throws BusinessException {
	// String servername = "synAduitFlow";
	// Element services = initElement(servername);
	// Element service = services.addElement("service");
	// service.addElement("serviceid").addText("1");
	// if (bill.getParent().getAttributeValue("def21") != null)// Ӱ�����
	// service.addElement("barcode").addText(
	// (String) bill.getParent().getAttributeValue("def21"));
	// service.addElement("type").addText("1");
	// service.addElement("anattr1").addText("1");
	// Element bodyService = service.addElement("items");
	// // ��ȡ������ַ
	// List<Map<String, String>> fileInfoList = getFileVO(bill.getPrimaryKey());
	// if (fileInfoList != null && fileInfoList.size() > 0) {
	// List<String> pathList = new ArrayList<String>();
	// for (Map<String, String> fileInfo : fileInfoList) {
	// pathList.add(fileInfo.get("filepath"));
	// }
	// Map<String, String> urlMap = getFileUrl(pathList);
	// Element item = null;
	// for (Map<String, String> fileInfo : fileInfoList) {
	// String[] names = fileInfo.get("filepath").split("/");
	// item = bodyService.addElement("item");
	// item.addElement("name").addText(names[names.length - 1]);// ���ƴ���׺
	// item.addElement("url").addText(
	// urlMap.get(fileInfo.get("filepath")));// ��ַ
	// item.addElement("fileuuid").addText(fileInfo.get("pk"));// �ļ�pk
	// }
	// }
	// return service.getDocument().asXML();
	// }
	//
	// /**
	// * ���ݵ���pk��ȡϵͳϵͳ����filepath����webҳ������url
	// *
	// * @since 2019-11-28
	// * @param pk_key
	// * ��������
	// * @return
	// * @throws BusinessException
	// */
	// private Map<String, String> getFileUrl(List<String> pathlist)
	// throws BusinessException {
	// Map<String, String> urlMap = new HashMap<>();
	// for (int i = 0; i < pathlist.size(); i++) {
	// String path = pathlist.get(i);
	// Logger.debug("------------------------sungbztest showFileInWeb--------------------");
	//
	// IFileSystemService fser = NCLocator.getInstance().lookup(
	// IFileSystemService.class);
	//
	// ArrayList<String> fullpathlist = new ArrayList<String>();
	// path = FileSystemUtil.validatePathString(path);
	//
	// fullpathlist.add(path);
	// NCFileVO[] arrfilevo = null;
	// arrfilevo = fser.queryFileVOsByPathList(fullpathlist);
	// if (arrfilevo.length == 0) {
	// Logger.error("δ��ȡ����Ӧ·���ĸ���NCFileVO����");
	// // return;
	// }
	// if (FileTypeConst.getAttachMentStoreType(arrfilevo[0].getIsdoc())
	// .equals(FileTypeConst.STORE_TYPE_2)) {
	// String pk_doc = arrfilevo[0].getPk_doc();
	// String urlstring = FileStorageClient.getInstance()
	// .getDownloadURL(null, pk_doc);
	// urlMap.put(path, urlstring);
	//
	// } else if (FileTypeConst.getAttachMentStoreType(
	// arrfilevo[0].getIsdoc()).equals(FileTypeConst.STORE_TYPE_1)) {
	// // 63�洢��ʽ�ݲ�����
	// // showFileInWebStorebyV63(dsName,path,lastmodtime);
	// } else if (FileTypeConst.getAttachMentStoreType(
	// arrfilevo[0].getIsdoc()).equals(FileTypeConst.STORE_TYPE_3)) {
	// String uniqcode = arrfilevo[0].getIsdoc();
	// IAttachmentOperation downloadclass = AttachCloudStorageFactory
	// .getInstance().getDownloadImp(uniqcode);
	// String urlstring = downloadclass.getDownloadURL(arrfilevo[0]
	// .getPk_doc());
	// urlMap.put(path, urlstring);
	// }
	//
	// }
	// return urlMap;
	//
	// }
	//
	// /**
	// * ����pk��ȡ
	// *
	// * @throws BusinessException
	// */
	// private List<Map<String, String>> getFileVO(String pk_key)
	// throws BusinessException {
	// String sql = "select * from sm_pub_filesystem where ( filepath like '"
	// + pk_key + "%' ) and  isfolder='n' and nvl(dr,0)=0";
	// List<Map<String, String>> list = (List<Map<String, String>>) new
	// BaseDAO()
	// .executeQuery(sql, new MapListProcessor());
	// return list;
	//
	// }

	@Override
	public String sysncBranchInfo_RequiresNew(OrgVO vo)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		String retXml = null;
		CallimgLogVO callimgLogVO = new CallimgLogVO();
		try {
			String servername = "WBSynBranchInfo";
			callimgLogVO.setServicename(servername);
			Element services = initElement(servername);
			Element service = services.addElement("service");
			service.addElement("serviceid").addText("1");
			service.addElement("optype").addText("1");
			if (vo.getPk_org() != null)
				service.addElement("companyflag").addText(vo.getPk_org());
			if (vo.getCode() != null)
				service.addElement("companycode").addText(vo.getCode());
			if (vo.getName() != null)
				service.addElement("companyname").addText(vo.getName());
			if (vo.getPk_fatherorg() != null)
				service.addElement("superflag").addText(vo.getPk_fatherorg());
			String xml = service.getDocument().asXML();
			callimgLogVO.setSendmess(xml);
			retXml = callImageService(xml);
			if (StringUtils.isBlank(retXml)) {
				throw new BusinessException("Ӱ��ϵͳ���ýӿڷ�����ϢΪ��");
			}
			if (retXml != null) {
				Element root = xmlConvertElement(retXml);
				String errmsg = root.element("items").element("item")
						.elementText("errormsg");
				if (errmsg != null && errmsg != "") {
					throw new BusinessException("image exception "
							+ root.elementText("errormsg"));
				}
			}
			// ���سɹ�����¼��־
			callimgLogVO.setRemess(retXml);// Ӱ��ϵͳ���ص�XML
			callimgLogVO.setStatus(VOStatus.NEW);
		} catch (Exception e) {
			callimgLogVO.setRemess(e.getMessage());
			callimgLogVO.setStatus(VOStatus.NEW);
			throw new BusinessException(e.getMessage());
		} finally {
			try {
				IPushBPMBillFileService service = NCLocator.getInstance()
						.lookup(IPushBPMBillFileService.class);
				service.saveLog_RequiresNew(callimgLogVO);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return null;
	}

	@Override
	public void synXsbarcode(HashMap<String, String> maptwo)
			throws BusinessException {
		String xml = synXsbarcode_XML(maptwo);
		CallimgLogVO callimgLogVO = new CallimgLogVO();
		try {
			String retXml = callImageService(xml);
			Element root = xmlConvertElement(retXml);
			// ���سɹ�����¼��־
			callimgLogVO.setServicename("SynXsbarcode");
			callimgLogVO.setBarcode(maptwo.get("barcode"));// Ӱ�������
			callimgLogVO.setSendmess(xml);// ���͵�XML
			callimgLogVO.setSendtime(new UFDateTime());// ����ʱ��
			callimgLogVO.setRemess(retXml);// Ӱ��ϵͳ���ص�XML
			callimgLogVO.setStatus(VOStatus.NEW);
			// dao.insertVO(callimgLogVO);
			// ��־��¼
			if ("1".equals(root.elementText("result"))) {
				// callimgLogVO.setStatus(VOStatus.NEW);
				// new BaseDAO().insertVO(callimgLogVO);
				throw new BusinessException("Ӱ��������:"
						+ root.elementText("errormsg"));
			} else {
				// throw new BusinessException(root.elementText("errormsg"));
			}
		} catch (Exception e) {
			throw new BusinessException("����Ӱ�����ʧ�ܣ���" + e.getMessage() + "��");
		} finally {
			try {
				// new BaseDAO().insertVO(callimgLogVO);
				IPushBPMBillFileService service = NCLocator.getInstance()
						.lookup(IPushBPMBillFileService.class);
				service.saveLog_RequiresNew(callimgLogVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Ӱ��ϵͳ�ӿ�synXsbarcode XML����
	 * 
	 * @return XML
	 * @throws BusinessException
	 */
	private String synXsbarcode_XML(HashMap<String, String> map)
			throws BusinessException {
		String servername = "SynXsbarcode";
		Element services = initEle(servername);
		Element service = services.addElement("service");

		service.addElement("type").addText(map.get("type"));// 1/����
		// Ӱ�������
		String barcode = map.get("barcode");
		if (map.get("barcode") != null) {
			service.addElement("barcode").addText(barcode);
		}
		if (map.get("invoicecode") != null) {
			service.addElement("invoicecode").addText(map.get("invoicecode"));
		}
		if (map.get("invoicenum") != null) {
			service.addElement("invoicenum").addText(map.get("invoicenum"));
		}
		if (map.get("downloadurl") != null) {
			service.addElement("downloadurl").addText(map.get("downloadurl"));
		}
		if (map.get("filename") != null) {
			service.addElement("filename").addText(map.get("filename"));
		}
		String xml = services.getDocument().asXML();
		return xml;
	}

	/**
	 * ��ʼ�����õ�XML��Ϣ
	 * 
	 * @param servername
	 * @return
	 */
	private Element initEle(String servername) {
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
