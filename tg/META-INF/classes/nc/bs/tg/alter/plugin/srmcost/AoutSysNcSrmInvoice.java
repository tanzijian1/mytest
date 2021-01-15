package nc.bs.tg.alter.plugin.srmcost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.AoutSysncEbsData;
import nc.bs.tg.outside.ebs.pub.PushSrmDataUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import uap.serverdes.appesc.MD5Util;

public class AoutSysNcSrmInvoice extends AoutSysncEbsData {

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		List<Object[]> msglist = new ArrayList<Object[]>();
		List<Map<String, Object>> settMap = getInvheadInfoCurrent(bgwc);
		if (settMap != null && settMap.size() > 0) {
			for (Map<String, Object> info : settMap) {

				String code = "pushSrmInternalInvoice";

				String[] msgObj = new String[2];

				try {

					String syscode = "test";

					Map<String, Object> postdata = new HashMap<String, Object>();

					postdata.put("projectOrgId", info.get("projectorgid"));// 项目公司ID
					//postdata.put("projectPkOrg", "");// 项目公司编码
					postdata.put("projectOrgName",
							queryOrgName((String) info.get("projectorgname")));// 项目公司名
					postdata.put("materialOrgId", info.get("materialorgid"));// 材料公司ID
					//postdata.put("materialPkOrg", "");// 材料公司编码
					postdata.put("materialOrgName",
							queryOrgName((String) info.get("materialorgname")));// 材料公司名
					postdata.put("checkAccountNum", info.get("checkaccountnum"));// 对账单编号
					postdata.put("checkTotalAmount",
							info.get("checktotalamount"));// 对账总额(含税)
					postdata.put("checkNoTaxAmount",
							info.get("checknotaxamount"));// 对账总额(不含税)
					postdata.put("checkTaxAmount", info.get("checktaxamount"));// 对账税额
					postdata.put("invoiceTotalAmount",
							info.get("invoicetotalamount"));// 发票总额(含税)
					postdata.put("invoiceNoTaxAmount",
							info.get("invoicenotaxamount"));// 发票总额(不含税)
					postdata.put("invoiceTaxAmount",
							info.get("invoicetaxamount"));// 发票税额
					postdata.put("validateFlag", "是");// 是否验证真伪
					//postdata.put("fileId", "");// 材料公司编码
					//postdata.put("fileName", "");// 材料公司编码
					//postdata.put("fileUrl", "");// 材料公司编码
					postdata.put("description", info.get("description"));// 备注
					postdata.put("ncDocumentId", info.get("ncdocumentid"));// NC单据ID
					postdata.put("ncDocumentNumber",
							info.get("ncdocumentnumber"));// NC单据号

					List<Map<String, Object>> invbodys = getInvbodyInfoCurrent((String) info
							.get("checkaccountnum"));

					List<Map<String, Object>> bodylist = new ArrayList<Map<String, Object>>();

					if (invbodys.size() <= 0) {
						throw new BusinessException("该单无对应销项发票");
					}

					for (Map<String, Object> inv : invbodys) {
						HashMap<String, Object> bodyMap = new HashMap<String, Object>();
						bodyMap.put("ncLineId", inv.get("nclineid"));// 单据行ID
						bodyMap.put("invoiceNumber", inv.get("invoicenumber"));// 发票号
						bodyMap.put("invoiceCode", inv.get("invoicecode"));// 发票代码
						bodyMap.put("invoiceType",
								queryInvoiceTypeName((String) inv
										.get("invoicetype")));// 发票类型
						bodyMap.put("invoiceDate",
								inv.get("invoicedate") == null ? null : inv
										.get("invoicedate").toString()
										.substring(0, 10));// 开票日期
						bodyMap.put("invoiceTotalAmount",
								inv.get("invoicetotalamount"));// 发票金额(含税)
						bodyMap.put("invoiceNoTaxAmount",
								inv.get("invoicenotaxamount"));// 发票金额(不含税)
						bodyMap.put("invoiceTaxAmount",
								inv.get("invoicetaxamount"));// 发票税额
						bodyMap.put("invoiceRate", inv.get("invoicerate"));// 发票税率
						bodyMap.put("creditFlag", "否");//是否红字
						bodyMap.put("reverseInvoiceNumber",
								inv.get("reverseinvoicenumber"));// 蓝字发票号
						bodyMap.put("reverseInvoiceCode",
								inv.get("reverseinvoicecode"));// 蓝字发票代码
						bodylist.add(bodyMap);
					}

					postdata.put("invoiceList", bodylist);

					PropertiesUtil propUtil = PropertiesUtil
							.getInstance("srm_url.properties");
					String key = propUtil.readValue("KEY");
					Date date = new Date();
					SimpleDateFormat formater = new SimpleDateFormat(
							"yyyyMMddHHmm");// 年月日时分
					String time = formater.format(date);
					String tokenkey = time + key;
					String token = MD5Util.getMD5(tokenkey).toUpperCase();

					Map<String, String> refMap = PushSrmDataUtils.getUtils()
							.pushInvoicelToSRM(code, syscode, token, postdata,
									"SRM开票回写");
					if (refMap != null) {
						msgObj[1] = refMap.get("msg");
						if ("S".equals(refMap.get("code"))) {

							String sql = "update tgfn_invhead set def48 = 'Y' where PK_INVOICING = '"
									+ (String) info.get("ncdocumentid")
									+ "' and nvl(dr,0) = 0";

							BaseDAO dao = new BaseDAO();

							dao.executeUpdate(sql);
						}
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					msgObj[1] = e.getMessage();

				}
				msglist.add(msgObj);
			}
		}

		return msglist;

	}

	private List<Map<String, Object>> getInvbodyInfoCurrent(String object)
			throws BusinessException {

		StringBuffer sql = new StringBuffer();

		sql.append("select o.pk_outputtax ncLineId,")
				// 单据行ID
				.append("o.fph invoiceNumber,")
				// 发票号
				.append("o.fpdm invoiceCode,")
				// 发票代码
				.append("o.fplx invoiceType,")
				// 发票类型
				.append("o.dbilldate invoiceDate,")
				// 开票日期
				.append("bo.moneyintax invoiceTotalAmount,")
				// 发票金额(含税)
				.append("bo.moneyouttax invoiceNoTaxAmount,")
				// 发票金额(不含税)
				.append("bo.MONEYTAX invoiceTaxAmount,")
				// 发票税额
				.append("bo.taxrate invoiceRate,")
				// 发票税率
				.append("o.src_taxbillno reverseInvoiceNumber,")
				// 蓝字发票号
				.append("o.src_taxbillcode reverseInvoiceCode")
				// 蓝字发票代码
				.append(" from hzvat_outputtax_h o ")
				.append(" left join hzvat_outputtax_b bo on o.pk_outputtax = bo.pk_outputtax ")
				.append(" where o.def15 = '" + object
						+ "' and nvl(o.dr,0) = 0 ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}

	private List<Map<String, Object>> getInvheadInfoCurrent(
			BgWorkingContext bgwc) throws BusinessException {
		StringBuffer sql = new StringBuffer();

		sql.append("select i.def20 projectOrgId,")
				// 项目公司ID
				.append("i.def21 projectOrgName,")
				// 项目公司名
				.append("i.def9 materialOrgId,")
				// 材料公司ID
				.append("i.def10 materialOrgName,")
				// 材料公司名
				.append("i.def2 checkAccountNum,")
				// 对账单编号
				.append("i.def34 checkTotalAmount,")
				// 对账总额(含税)
				.append("i.def32 checkNoTaxAmount,")
				// 对账总额(不含税)
				.append("i.def33 checkTaxAmount,")
				// 对账税额
				.append("i.def38 invoiceTotalAmount,")
				// 发票总额(含税)
				.append("i.def36 invoiceNoTaxAmount,")
				// 发票总额(不含税)
				.append("i.def37 invoiceTaxAmount,")
				// 发票税额
				.append("i.def42 description,")
				// 备注
				.append("i.pk_invoicing ncDocumentId,")
				// NC单据ID
				.append("i.billno ncDocumentNumber")
				// NC单据号
				.append(" from tgfn_invhead i ")
				.append(" where i.def47 = 'Y' and i.def48 <> 'Y' ")
				.append(" and nvl(i.dr,0) = 0 ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) EBSBillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}

	public String queryOrgName(String pk) throws BusinessException {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select name from org_orgs where pk_org = '" + pk
				+ "' and nvl(dr,0) = 0";

		String name = (String) bs.executeQuery(sql, new ColumnProcessor());

		return name;
	}

	public String queryInvoiceTypeName(String pk) throws BusinessException {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select name from hzvat_invoicetype where code = '"
				+ pk + "' and nvl(dr,0) = 0";

		String name = (String) bs.executeQuery(sql, new ColumnProcessor());

		return name;
	}
}
