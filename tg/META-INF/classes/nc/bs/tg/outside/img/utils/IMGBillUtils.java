package nc.bs.tg.outside.img.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.hzvat.invoice.AggInvoiceVO;
import nc.vo.hzvat.invoice.InvoiceBVO;
import nc.vo.hzvat.invoice.InvoiceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class IMGBillUtils {
	static IMGBillUtils utils;
	public static final String DataSourceName = "design";// �ӿ�Ĭ������Դ
	public static final String SaleOperatorName = "IMG";// BPMĬ�ϲ���Ա
	public static final String STATUS_SUCCESS = "1";// �ɹ�
	public static final String STATUS_FAILED = "0";// ʧ��

	private ISecurityTokenCallback sc;

	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IPFBusiAction pfBusiAction = null;

	String bpmUserid = null;// BPM�����û�

	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//

	public static IMGBillUtils getUtils() {
		if (utils == null) {
			utils = new IMGBillUtils();
		}
		return utils;
	}

	/**
	 * �ӿڱ������ǰ���� ���ڷ�ֹ�����Ŷ����(��ʱ��)ʱ����ϵͳ�����͵��ݣ�NC�����ظ�����
	 * 
	 * @param billcode
	 *            ��ϵͳ����Ψһ��ʶ�ַ���
	 * @throws BusinessException
	 */
	public static String addBillQueue(String billqueue)
			throws BusinessException {
		if (null != billqueue && !"".equals(billqueue)
				&& !m_billqueue.add(billqueue)) {
			throw new BusinessException("ҵ�񵥾ݡ�" + billqueue
					+ "��,����ִ�ж���,�����ظ�ִ��!");
		}
		return billqueue;
	}

	/**
	 * �ӿ��˳�ʱ�Ƴ�
	 * 
	 * @param billcode
	 *            ��ϵͳ����Ψһ��ʶ�ַ���,���ڷ�ֹ�����ų�����ظ�����
	 * @return
	 */
	public static boolean removeBillQueue(String billqueue) {
		if (null != billqueue && !"".equals(billqueue))
			m_billqueue.remove(billqueue);
		return true;
	}

	/**
	 * Ԫ���ݳ־û���ѯ�ӿ�
	 * 
	 * @return
	 */
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}

	/**
	 * ���ݿ�־û�
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	/**
	 * ��ȡBPM����ԱĬ���û�
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getIMGUserID() throws BusinessException {
		if (bpmUserid == null) {
			String sql = "select cuserid from sm_user  where user_code = '"
					+ SaleOperatorName + "'";
			bpmUserid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}
		return bpmUserid;
	}

	/**
	 * ������Ա��Ϣ
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getUserInfo(String username)
			throws BusinessException {
		String sql = "select cuserid,user_code from sm_user  where user_name = '"
				+ username + "'";
		Map<String, String> userInfo = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());

		return userInfo;
	}

	private ISecurityTokenCallback getTokenCallBacck() {
		if (sc == null) {
			sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		}
		return sc;
	}

	/**
	 * ע�밲ȫ����
	 * 
	 * @param key
	 * @param methodName
	 * @return
	 */
	public byte[] token(String key, String methodName) {
		return getTokenCallBacck().token((key + methodName).getBytes(),
				methodName.getBytes());

	}

	/**
	 * �ͷŰ�ȫ����
	 * 
	 * @param token
	 */
	public void restore(byte[] token) {
		getTokenCallBacck().restore(token);

	}

	/**
	 * ͨ��NC�����������м���ȡHCM��������
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getHCMDeptID(String id) throws BusinessException {
		String sql = "select t.id from organizationitem t  inner join org_dept  on org_dept.code = t.seqnum where org_dept.pk_dept ='"
				+ id + "'";
		String hcmid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return hcmid;
	}

	/**
	 * ��ȡҵ�񵥾ݾۺ�VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}

	/**
	 * ����Ӱ��Ʊ�ķ������ݲ�����Ʊ��VO
	 * 
	 * @param isLLBill
	 *            �Ƿ����ﵥ��
	 * @param jarr
	 */
	public void insertImgInvToInvoiceVO(org.json.JSONArray jarr,
			Boolean isLLBill) throws Exception {
		org.json.JSONObject jsonhead = jarr.getJSONObject(0);
		org.json.JSONArray jsonarr = jarr.getJSONObject(0).getJSONArray(
				"invoiceitems");

		String contractnumber = jsonhead.getString("contractnumber");
		String billcode = jsonhead.getString("billcode");
		String barcode = jsonhead.getString("barcode");
		String fkhname = jsonhead.getString("fkhname");
		String fkhaccount = jsonhead.getString("fkhaccount");
		String fkhbankaccount = jsonhead.getString("fkhbankaccount");
		for (int i = 0; i < jsonarr.length(); i++) {
			org.json.JSONObject jsonObject = jsonarr.getJSONObject(i);

			String invoicecode = jsonObject.getString("invoicecode");
			String invoicenum = jsonObject.getString("invoicenum");
			// TODO�ѱ��治����
			String billno = (String) getBaseDAO().executeQuery(
					"select vbillno from hzvat_invoice_h where nvl(dr,0)=0 and fpdm='"
							+ invoicecode + "' and fph = '" + invoicenum + "'",
					new ColumnProcessor());
			if (!"".equals(billno) && billno != null) {
				continue;
			}
			String inname = jsonObject.getString("inname");
			String invoicedate = jsonObject.getString("invoicedate");
			String indate = invoicedate.replace("��", "-").replace("��", "-")
					.replace("��", "");

			String taxamount = jsonObject.getString("taxamount");

			String outaccount = jsonObject.getString("outaccount");
			String outname = jsonObject.getString("outname");
			String outphone = jsonObject.getString("outphone");
			String outbank = jsonObject.getString("outbank");

			// String invoicetype = jsonObject.getString("invoicetype");
			String inaccount = jsonObject.getString("inaccount");
			String inbank = jsonObject.getString("inbank");
			String inphone = jsonObject.getString("inphone");
			String fpjym = jsonObject.getString("fpjym");
			String pk_org = getPKbyName(jsonObject.getString("inname"));// "�����а��ZͶ�����޹�˾");
			if ("".equals(pk_org) || pk_org == null) {
				throw new BusinessException("������ơ�"
						+ jsonObject.getString("inname") + "��δ��NC����");
			}
			String pk_vid = getVIDbyPK(pk_org);
			String ischeckresult = "0".equals(jsonObject
					.getString("ischeckresult")) ? "Y" : "N";
			UFDouble jamount = new UFDouble(jsonObject.getString("jamount"));
			UFDouble totalamount = new UFDouble(
					jsonObject.getString("totalamount"));
			// ���Ʊ��ͷ����
			InvoiceVO invoicvo = new InvoiceVO();
			// ���Ʊ��������
			InvoiceBVO[] invoicebvos = new InvoiceBVO[1];
			InvoiceBVO invoicebvo = new InvoiceBVO();

			invoicvo.setPk_group("000112100000000005FD");
			// ������֯
			invoicvo.setPk_org(pk_org);
			// ������֯�汾
			invoicvo.setPk_org_v(pk_vid);
			invoicvo.setVbillstatus(-1);

			// �������
			invoicvo.setDef9(fkhname);
			// ����˺�
			invoicvo.setDef10(fkhaccount);
			// ����˺�
			invoicvo.setDef11(fkhbankaccount);
			// �����˰��ʶ���
			invoicvo.setSellnsrsbh(outaccount);
			// �������
			invoicvo.setSellname(outname);
			// �����ַ�绰
			invoicvo.setSelladrpho(outphone);
			// ��������˻�
			invoicvo.setSellbank(outbank);
			// �ۿ�ϼ�z
			invoicvo.setDef16(jsonObject.getString("jamount"));

			// ��Ʊ����
			// invoicvo.setFplx(invoicetype);
			// ��Ʊ��
			invoicvo.setFph(invoicenum);
			// ��Ʊ����
			invoicvo.setFpdm(invoicecode);
			// ��˰���
			invoicvo.setCostmoney(jamount);
			// ��˰�ϼ�
			invoicvo.setCostmoneyin(totalamount);
			// ��Ʊ����
			invoicvo.setKprq(new UFDate(indate));
			// Ʊ������
			invoicvo.setPdrq(new UFDate());
			// ���۷�����
			invoicvo.setDef13(inname);
			// ������˰��ʶ���
			invoicvo.setDef12(inaccount);
			// ���������˻�
			invoicvo.setDef15(inbank);
			// ������ַ�绰
			invoicvo.setDef14(inphone);
			// �Ƿ�����α
			invoicvo.setDef1(ischeckresult);
			// ��ͬ����
			invoicvo.setDef3(contractnumber);
			// ��ϵͳ����
			invoicvo.setDef4(billcode);
			// ˰��
			invoicvo.setDef5(taxamount);
			// У���루��Ʊ���
			invoicvo.setDef6(fpjym);
			// Ӱ�����
			invoicvo.setDef8(barcode);

			// TODO addBy ln 2020��10��20��09:49:00 ����������ʶ����===start{===
			if (isLLBill) {
				invoicvo.setDef17("��ҵ���");// �������
			}
			// ===end}===

			/* ������Ϣ */
			org.json.JSONArray arr = jsonObject.getJSONArray("hxitems");
			if (arr == null) {
				throw new BusinessException("��Ʊ��ϸ�в���Ϊ��");
			}
			for (int j = 0; j < arr.length(); j++) {

				org.json.JSONObject jsonObj = arr.getJSONObject(j);

				String goodsname = jsonObj.getString("goodsname");
				String goodscode = jsonObj.getString("goodscode");
				String goodsp = jsonObj.getString("goodsp");
				String goodscount = jsonObj.getString("goodscount");
				UFDouble goodsamount = new UFDouble(
						jsonObj.getString("goodsamount"));
				UFDouble goodscamount = new UFDouble(
						jsonObj.getString("goodscamount"));
				String s = jsonObj.getString("goodstax");
				UFDouble goodstax = new UFDouble(s.substring(0, s.length() - 1));
				UFDouble goodstaxamount = new UFDouble(
						jsonObj.getString("goodstaxamount"));

				// ��֯
				invoicebvo.setAttributeValue("pk_org", pk_org);
				// ����
				invoicebvo
						.setAttributeValue("pk_group", "000112100000000005FD");
				// �������������
				invoicebvo.setAttributeValue("pk_product", goodsname);
				// ����ͺ�
				invoicebvo.setAttributeValue("ggxh", goodscode);
				// ��λ
				invoicebvo.setAttributeValue("pk_math", goodsp);
				// ����
				invoicebvo.setAttributeValue("num", goodscount);

				UFDouble one = UFDouble.ONE_DBL;
				UFDouble hundred = new UFDouble(100);

				// ��˰����
				invoicebvo.setAttributeValue("priceintax", goodsamount);
				// ��˰�ϼ�
				invoicebvo.setAttributeValue("moneyintax", goodscamount);
				// ˰��
				invoicebvo.setAttributeValue("taxrate", goodstax);
				// ˰��
				invoicebvo.setAttributeValue("moneytax", goodstaxamount);
				// ��˰����
				invoicebvo.setAttributeValue("priceouttax",
						goodsamount.div(one.add(goodstax.div(hundred))));
				// ��˰���
				invoicebvo.setAttributeValue("moneyouttax",
						goodscamount.div(one.add(goodstax.div(hundred))));

				invoicebvos[0] = invoicebvo;
			}
			AggInvoiceVO aggvo = new AggInvoiceVO();
			aggvo.setParentVO(invoicvo);
			aggvo.setChildrenVO(invoicebvos);

			// AggInvoiceVO[] aggvos = new AggInvoiceVO[1];
			//
			// aggvos[0].setParentVO(invoicvo);
			// aggvos[0].setChildrenVO(invoicebvos);
			//
			// nc.itf.hzvat.hz01.inputtax.IInvoiceMaintain operator =
			// NCLocator.getInstance().lookup(nc.itf.hzvat.hz01.inputtax.IInvoiceMaintain.class);
			//
			// operator.update(aggvos, aggvos);

			getPfBusiAction().processAction("SAVEBASE", "HZ01", null, aggvo,
					null, null);
		}
	}

	public String getPKbyName(String name) {

		String sql = "select pk_org from org_orgs where name = '" + name + "'";

		String pk_org = null;

		try {
			pk_org = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return pk_org;
	}

	public String getVIDbyPK(String pk_org) {

		String sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + pk_org
				+ "' and enablestate = 2 and nvl(dr,0)=0";

		String pk_vid = null;

		try {
			pk_vid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return pk_vid;
	}
}
