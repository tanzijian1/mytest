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

					postdata.put("projectOrgId", info.get("projectorgid"));// ��Ŀ��˾ID
					//postdata.put("projectPkOrg", "");// ��Ŀ��˾����
					postdata.put("projectOrgName",
							queryOrgName((String) info.get("projectorgname")));// ��Ŀ��˾��
					postdata.put("materialOrgId", info.get("materialorgid"));// ���Ϲ�˾ID
					//postdata.put("materialPkOrg", "");// ���Ϲ�˾����
					postdata.put("materialOrgName",
							queryOrgName((String) info.get("materialorgname")));// ���Ϲ�˾��
					postdata.put("checkAccountNum", info.get("checkaccountnum"));// ���˵����
					postdata.put("checkTotalAmount",
							info.get("checktotalamount"));// �����ܶ�(��˰)
					postdata.put("checkNoTaxAmount",
							info.get("checknotaxamount"));// �����ܶ�(����˰)
					postdata.put("checkTaxAmount", info.get("checktaxamount"));// ����˰��
					postdata.put("invoiceTotalAmount",
							info.get("invoicetotalamount"));// ��Ʊ�ܶ�(��˰)
					postdata.put("invoiceNoTaxAmount",
							info.get("invoicenotaxamount"));// ��Ʊ�ܶ�(����˰)
					postdata.put("invoiceTaxAmount",
							info.get("invoicetaxamount"));// ��Ʊ˰��
					postdata.put("validateFlag", "��");// �Ƿ���֤��α
					//postdata.put("fileId", "");// ���Ϲ�˾����
					//postdata.put("fileName", "");// ���Ϲ�˾����
					//postdata.put("fileUrl", "");// ���Ϲ�˾����
					postdata.put("description", info.get("description"));// ��ע
					postdata.put("ncDocumentId", info.get("ncdocumentid"));// NC����ID
					postdata.put("ncDocumentNumber",
							info.get("ncdocumentnumber"));// NC���ݺ�

					List<Map<String, Object>> invbodys = getInvbodyInfoCurrent((String) info
							.get("checkaccountnum"));

					List<Map<String, Object>> bodylist = new ArrayList<Map<String, Object>>();

					if (invbodys.size() <= 0) {
						throw new BusinessException("�õ��޶�Ӧ���Ʊ");
					}

					for (Map<String, Object> inv : invbodys) {
						HashMap<String, Object> bodyMap = new HashMap<String, Object>();
						bodyMap.put("ncLineId", inv.get("nclineid"));// ������ID
						bodyMap.put("invoiceNumber", inv.get("invoicenumber"));// ��Ʊ��
						bodyMap.put("invoiceCode", inv.get("invoicecode"));// ��Ʊ����
						bodyMap.put("invoiceType",
								queryInvoiceTypeName((String) inv
										.get("invoicetype")));// ��Ʊ����
						bodyMap.put("invoiceDate",
								inv.get("invoicedate") == null ? null : inv
										.get("invoicedate").toString()
										.substring(0, 10));// ��Ʊ����
						bodyMap.put("invoiceTotalAmount",
								inv.get("invoicetotalamount"));// ��Ʊ���(��˰)
						bodyMap.put("invoiceNoTaxAmount",
								inv.get("invoicenotaxamount"));// ��Ʊ���(����˰)
						bodyMap.put("invoiceTaxAmount",
								inv.get("invoicetaxamount"));// ��Ʊ˰��
						bodyMap.put("invoiceRate", inv.get("invoicerate"));// ��Ʊ˰��
						bodyMap.put("creditFlag", "��");//�Ƿ����
						bodyMap.put("reverseInvoiceNumber",
								inv.get("reverseinvoicenumber"));// ���ַ�Ʊ��
						bodyMap.put("reverseInvoiceCode",
								inv.get("reverseinvoicecode"));// ���ַ�Ʊ����
						bodylist.add(bodyMap);
					}

					postdata.put("invoiceList", bodylist);

					PropertiesUtil propUtil = PropertiesUtil
							.getInstance("srm_url.properties");
					String key = propUtil.readValue("KEY");
					Date date = new Date();
					SimpleDateFormat formater = new SimpleDateFormat(
							"yyyyMMddHHmm");// ������ʱ��
					String time = formater.format(date);
					String tokenkey = time + key;
					String token = MD5Util.getMD5(tokenkey).toUpperCase();

					Map<String, String> refMap = PushSrmDataUtils.getUtils()
							.pushInvoicelToSRM(code, syscode, token, postdata,
									"SRM��Ʊ��д");
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
				// ������ID
				.append("o.fph invoiceNumber,")
				// ��Ʊ��
				.append("o.fpdm invoiceCode,")
				// ��Ʊ����
				.append("o.fplx invoiceType,")
				// ��Ʊ����
				.append("o.dbilldate invoiceDate,")
				// ��Ʊ����
				.append("bo.moneyintax invoiceTotalAmount,")
				// ��Ʊ���(��˰)
				.append("bo.moneyouttax invoiceNoTaxAmount,")
				// ��Ʊ���(����˰)
				.append("bo.MONEYTAX invoiceTaxAmount,")
				// ��Ʊ˰��
				.append("bo.taxrate invoiceRate,")
				// ��Ʊ˰��
				.append("o.src_taxbillno reverseInvoiceNumber,")
				// ���ַ�Ʊ��
				.append("o.src_taxbillcode reverseInvoiceCode")
				// ���ַ�Ʊ����
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
				// ��Ŀ��˾ID
				.append("i.def21 projectOrgName,")
				// ��Ŀ��˾��
				.append("i.def9 materialOrgId,")
				// ���Ϲ�˾ID
				.append("i.def10 materialOrgName,")
				// ���Ϲ�˾��
				.append("i.def2 checkAccountNum,")
				// ���˵����
				.append("i.def34 checkTotalAmount,")
				// �����ܶ�(��˰)
				.append("i.def32 checkNoTaxAmount,")
				// �����ܶ�(����˰)
				.append("i.def33 checkTaxAmount,")
				// ����˰��
				.append("i.def38 invoiceTotalAmount,")
				// ��Ʊ�ܶ�(��˰)
				.append("i.def36 invoiceNoTaxAmount,")
				// ��Ʊ�ܶ�(����˰)
				.append("i.def37 invoiceTaxAmount,")
				// ��Ʊ˰��
				.append("i.def42 description,")
				// ��ע
				.append("i.pk_invoicing ncDocumentId,")
				// NC����ID
				.append("i.billno ncDocumentNumber")
				// NC���ݺ�
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
