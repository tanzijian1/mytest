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
	public static final String DataSourceName = "design";// 接口默认数据源
	public static final String SaleOperatorName = "IMG";// BPM默认操作员
	public static final String STATUS_SUCCESS = "1";// 成功
	public static final String STATUS_FAILED = "0";// 失败

	private ISecurityTokenCallback sc;

	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IPFBusiAction pfBusiAction = null;

	String bpmUserid = null;// BPM操作用户

	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//

	public static IMGBillUtils getUtils() {
		if (utils == null) {
			utils = new IMGBillUtils();
		}
		return utils;
	}

	/**
	 * 接口保存操作前增加 用于防止保存排队情况(耗时长)时，外系统重新送单据，NC出现重复操作
	 * 
	 * @param billcode
	 *            外系统单据唯一标识字符串
	 * @throws BusinessException
	 */
	public static String addBillQueue(String billqueue)
			throws BusinessException {
		if (null != billqueue && !"".equals(billqueue)
				&& !m_billqueue.add(billqueue)) {
			throw new BusinessException("业务单据【" + billqueue
					+ "】,已在执行队列,请勿重复执行!");
		}
		return billqueue;
	}

	/**
	 * 接口退出时移出
	 * 
	 * @param billcode
	 *            外系统单据唯一标识字符串,用于防止保存排除情况重复导入
	 * @return
	 */
	public static boolean removeBillQueue(String billqueue) {
		if (null != billqueue && !"".equals(billqueue))
			m_billqueue.remove(billqueue);
		return true;
	}

	/**
	 * 元数据持久化查询接口
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
	 * 数据库持久化
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
	 * 读取BPM操作员默认用户
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
	 * 读操作员信息
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
	 * 注入安全令牌
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
	 * 释放安全令牌
	 * 
	 * @param token
	 */
	public void restore(byte[] token) {
		getTokenCallBacck().restore(token);

	}

	/**
	 * 通过NC部门主键从中间表读取HCM部门主键
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
	 * 读取业务单据聚合VO
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
	 * 根据影像发票的返回数据插入进项发票的VO
	 * 
	 * @param isLLBill
	 *            是否邻里单据
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
			// TODO已保存不保存
			String billno = (String) getBaseDAO().executeQuery(
					"select vbillno from hzvat_invoice_h where nvl(dr,0)=0 and fpdm='"
							+ invoicecode + "' and fph = '" + invoicenum + "'",
					new ColumnProcessor());
			if (!"".equals(billno) && billno != null) {
				continue;
			}
			String inname = jsonObject.getString("inname");
			String invoicedate = jsonObject.getString("invoicedate");
			String indate = invoicedate.replace("年", "-").replace("月", "-")
					.replace("日", "");

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
			String pk_org = getPKbyName(jsonObject.getString("inname"));// "广州市傲璟投资有限公司");
			if ("".equals(pk_org) || pk_org == null) {
				throw new BusinessException("付款方名称【"
						+ jsonObject.getString("inname") + "】未与NC关联");
			}
			String pk_vid = getVIDbyPK(pk_org);
			String ischeckresult = "0".equals(jsonObject
					.getString("ischeckresult")) ? "Y" : "N";
			UFDouble jamount = new UFDouble(jsonObject.getString("jamount"));
			UFDouble totalamount = new UFDouble(
					jsonObject.getString("totalamount"));
			// 进项发票表头数据
			InvoiceVO invoicvo = new InvoiceVO();
			// 进项发票表体数据
			InvoiceBVO[] invoicebvos = new InvoiceBVO[1];
			InvoiceBVO invoicebvo = new InvoiceBVO();

			invoicvo.setPk_group("000112100000000005FD");
			// 财务组织
			invoicvo.setPk_org(pk_org);
			// 财务组织版本
			invoicvo.setPk_org_v(pk_vid);
			invoicvo.setVbillstatus(-1);

			// 付款函名称
			invoicvo.setDef9(fkhname);
			// 付款函账号
			invoicvo.setDef10(fkhaccount);
			// 付款函账号
			invoicvo.setDef11(fkhbankaccount);
			// 付款方纳税人识别号
			invoicvo.setSellnsrsbh(outaccount);
			// 付款方名称
			invoicvo.setSellname(outname);
			// 付款方地址电话
			invoicvo.setSelladrpho(outphone);
			// 付款方银行账户
			invoicvo.setSellbank(outbank);
			// 价款合计z
			invoicvo.setDef16(jsonObject.getString("jamount"));

			// 发票类型
			// invoicvo.setFplx(invoicetype);
			// 发票号
			invoicvo.setFph(invoicenum);
			// 发票代码
			invoicvo.setFpdm(invoicecode);
			// 无税金额
			invoicvo.setCostmoney(jamount);
			// 价税合计
			invoicvo.setCostmoneyin(totalamount);
			// 开票日期
			invoicvo.setKprq(new UFDate(indate));
			// 票到日期
			invoicvo.setPdrq(new UFDate());
			// 销售方名称
			invoicvo.setDef13(inname);
			// 销方纳税人识别号
			invoicvo.setDef12(inaccount);
			// 销方银行账户
			invoicvo.setDef15(inbank);
			// 销方地址电话
			invoicvo.setDef14(inphone);
			// 是否已验伪
			invoicvo.setDef1(ischeckresult);
			// 合同编码
			invoicvo.setDef3(contractnumber);
			// 外系统请款单号
			invoicvo.setDef4(billcode);
			// 税额
			invoicvo.setDef5(taxamount);
			// 校验码（普票必填）
			invoicvo.setDef6(fpjym);
			// 影像编码
			invoicvo.setDef8(barcode);

			// TODO addBy ln 2020年10月20日09:49:00 邻里新增标识区别===start{===
			if (isLLBill) {
				invoicvo.setDef17("物业板块");// 所属板块
			}
			// ===end}===

			/* 表体信息 */
			org.json.JSONArray arr = jsonObject.getJSONArray("hxitems");
			if (arr == null) {
				throw new BusinessException("发票明细行不能为空");
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

				// 组织
				invoicebvo.setAttributeValue("pk_org", pk_org);
				// 集团
				invoicebvo
						.setAttributeValue("pk_group", "000112100000000005FD");
				// 货物或劳务名称
				invoicebvo.setAttributeValue("pk_product", goodsname);
				// 规格型号
				invoicebvo.setAttributeValue("ggxh", goodscode);
				// 单位
				invoicebvo.setAttributeValue("pk_math", goodsp);
				// 数量
				invoicebvo.setAttributeValue("num", goodscount);

				UFDouble one = UFDouble.ONE_DBL;
				UFDouble hundred = new UFDouble(100);

				// 含税单价
				invoicebvo.setAttributeValue("priceintax", goodsamount);
				// 价税合计
				invoicebvo.setAttributeValue("moneyintax", goodscamount);
				// 税率
				invoicebvo.setAttributeValue("taxrate", goodstax);
				// 税额
				invoicebvo.setAttributeValue("moneytax", goodstaxamount);
				// 无税单价
				invoicebvo.setAttributeValue("priceouttax",
						goodsamount.div(one.add(goodstax.div(hundred))));
				// 无税金额
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
