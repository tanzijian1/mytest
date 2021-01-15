package nc.bs.tg.outside.sale.utils.salessystem;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.bs.tg.outside.sale.utils.paybill.PayBillConvertor;
import nc.itf.bd.bankacc.cust.ICustBankaccService;
import nc.itf.bd.cust.baseinfo.ICustBaseInfoService;
import nc.itf.bd.cust.baseinfo.ICustSupplierService;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.bankaccount.cust.CustBankaccUnionVO;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.bd.cust.CustbankVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.pub.BusinessQuerydata;

import com.alibaba.fastjson.JSONObject;

public class BasicInformationUtil extends SaleBillUtils {

	static BasicInformationUtil utils;

	public static BasicInformationUtil getUtils() {
		if (utils == null) {
			utils = new BasicInformationUtil();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String dectype)
			throws BusinessException {
		return null;
	}

	/**
	 * 客户基本信息保存类
	 * 
	 * @param value
	 * @param dectype
	 * @param srcsystem
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String[] onBasicBill(JSONObject headJson) throws BusinessException {
		// 客商银行账户字段校验
		if (!"F3-Cxx-024".equals(headJson.getString("pk_tradetypeid"))) {
			if (headJson.getString("def43") == null) {
				throw new BusinessException("银行账号不能为空，请检查参数设置");
			}
		}
		if (headJson.getString("supplier") == null) {
			throw new BusinessException("供应商名称不能为空，请检查参数设置");
		}
		String pk_org = BusinessQuerydata.getInstance().getPk_org(
				headJson.getString("pk_org"));

		PayBillConvertor convertor = new PayBillConvertor();
		// 供应商（客户，客商）
		String supplier = convertor.getRefAttributePk("paybill-supplier",
				headJson.getString("supplier"), pk_org, pk_org);

		if (supplier == null || "".equals(supplier)) {

			CustomerVO custvo2;

			String pk_timezone = BusinessQuerydata.getInstance().getStringvalue(
					"bd_timezone", "pk_timezone", "name", "北京时间(UTC+08:00)");
			String pk_custclass = BusinessQuerydata.getInstance().getcustomerClass();
			String pk_currtype = BusinessQuerydata.getInstance().getpk_currtype();
			String pk_format = BusinessQuerydata.getInstance().getStringvalue(
					"bd_formatdoc", "pk_formatdoc", "name", "中文简体");
			String pk_country = BusinessQuerydata.getInstance().getStringvalue(
					"bd_countryzone", "pk_country ", "name", "中国");

			CustomerVO custvo = new CustomerVO();

			custvo.setAttributeValue(CustomerVO.NAME,
					headJson.getString("supplier"));
			custvo.setAttributeValue(CustomerVO.PK_TIMEZONE, pk_timezone);// 北京时间(UTC+08:00)"0001Z010000000079U2P"
			custvo.setAttributeValue(CustomerVO.PK_CUSTCLASS, pk_custclass);// 客户基本分类
			custvo.setAttributeValue(CustomerVO.ENABLESTATE, Integer.valueOf(2));
			custvo.setAttributeValue(CustomerVO.PK_FORMAT, pk_format);// 数据格式--中文简体
			custvo.setAttributeValue(CustomerVO.PK_CURRTYPE, pk_currtype);// 币种
			custvo.setAttributeValue(CustomerVO.PK_COUNTRY, pk_country);// 国家--中国
			custvo.setAttributeValue(CustomerVO.PK_GROUP,
					"000112100000000005FD");// 集团
			custvo.setAttributeValue(CustomerVO.PK_ORG, "000112100000000005FD");// 组织
			custvo.setAttributeValue(CustomerVO.CUSTPROP, 0);// 客户类型
			custvo.setAttributeValue(CustomerVO.DEF1, "SALE");// 来源系统类型
			custvo.setAttributeValue(CustomerVO.DEF2,
					headJson.getString("def43"));
			IBillcodeManage codeManage = NCLocator.getInstance().lookup(
					IBillcodeManage.class);
			// 根据编码规则创建客商编码
			String code = codeManage.getBillCode_RequiresNew("customer",
					pk_org, pk_org, custvo);
			custvo.setAttributeValue(CustomerVO.CODE, code);
			ICustBaseInfoService service = NCLocator.getInstance().lookup(
					ICustBaseInfoService.class);
			custvo2 = service.insertCustomerVO(custvo, false);
			if (custvo2 != null) {
				SupplierVO supvo = new SupplierVO();
				// supvo.setAttributeValue(SupplierVO.PK_SUPPLIER,
				// custvo.getPk_customer());
				supvo.setAttributeValue(SupplierVO.PK_SUPPLIER,
						custvo.getPk_customer());
				supvo.setAttributeValue(SupplierVO.PK_ORG, custvo2.getPk_org());// 组织
				supvo.setAttributeValue(SupplierVO.PK_GROUP,
						custvo2.getPk_group());// 集团
				supvo.setAttributeValue(SupplierVO.CODE, code);// 编码
				supvo.setAttributeValue(SupplierVO.PK_CURRTYPE, pk_currtype);
				supvo.setAttributeValue(SupplierVO.NAME,
						custvo.getAttributeValue(CustomerVO.NAME));// 名称
				supvo.setAttributeValue(SupplierVO.ISCUSTOMER, UFBoolean.FALSE);// 是否客户
				supvo.setAttributeValue(SupplierVO.PK_COUNTRY, pk_country);// 国家地区
				supvo.setAttributeValue(SupplierVO.PK_TIMEZONE, pk_timezone);// 时区
				supvo.setAttributeValue(SupplierVO.PK_SUPPLIERCLASS, BusinessQuerydata
						.getInstance().getpk_supplierclass());// BusinessQuerydata.getInstance().getStringvalue("bd_supplierclass",
																// "pk_supplierclass",
																// "name",
																// "内部供应商")
				supvo.setAttributeValue(SupplierVO.PK_FORMAT, pk_format);// 数据格式
				supvo.setAttributeValue(SupplierVO.SUPPROP,
						custvo.getCustprop());// 客户类型
				supvo.setStatus(VOStatus.NEW);
				supvo.setDef1("SALE");// 来源系统类型
				supvo.setDef2(headJson.getString("def43"));// 来源系统id
				// ISupplierBaseInfoService supservice = NCLocator.getInstance()
				// .lookup(ISupplierBaseInfoService.class);
				NCLocator.getInstance().lookup(ICustSupplierService.class)
						.insertSupAndRelaToCust(supvo, custvo2);

				supplier = custvo2.getPk_supplier();

			}
		}
		// 客户，账号，户名，开户银行（nc同步给ebs），
		// 银行类别（nc同步给ebs），账户性质(公司/个人)，
		// 启用状态（默认已启用），启用人（建数据同步的用户去启用），
		// 启用时间（同步时间），联行号，
		// 开户地区，省份，城市，币种（默认人民币）
		String pk_bankaccbas = null;
		if (!"".equals(headJson.getString("def43"))
				&& headJson.getString("def43") != null) {
			if (BusinessQuerydata.getInstance().getaccinfo(supplier,
					headJson.getString("def43")) != null) {
				pk_bankaccbas = BusinessQuerydata.getInstance().getaccinfo(supplier,
						headJson.getString("def43"));
			} else {
				BankAccbasVO bankaccbasVO = new BankAccbasVO();
				// IBankdocQueryService query = NCLocator.getInstance().lookup(
				// IBankdocQueryService.class);
				BankdocVO bankdoc = BusinessQuerydata.getInstance().getIBankdoc(
						headJson.getString("def44"));
				if (bankdoc != null) {
					bankaccbasVO.setAccattribute(1);// 账户属性
					bankaccbasVO
							.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);// 启用状态
					bankaccbasVO.setStatus(VOStatus.NEW);
					bankaccbasVO.setAccclass(1);// 账户分类
					bankaccbasVO.setAccname(headJson.getString("supplier"));// 户名
					bankaccbasVO.setAccnum(headJson.getString("def43"));// 账号
					bankaccbasVO.setAccopendate(new UFDate());
					bankaccbasVO.setAccountproperty(Integer.valueOf(1));// 账户性质
					bankaccbasVO.setAccstate(Integer.valueOf(0));// 账户状态
					bankaccbasVO.setBankarea(bankdoc.getBankarea());// 开户地区
					bankaccbasVO.setCity(bankdoc.getCity());// 城市
					bankaccbasVO.setCombinenum(bankdoc.getCombinenum());// 联行号
					bankaccbasVO.setDataoriginflag(Integer.valueOf(0));// 分布式
					bankaccbasVO
							.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);
					bankaccbasVO.setEnabletime(new UFDateTime());
					bankaccbasVO.setEnableuser("#UAP#");
					// bankaccbasVO.setName(newName);//账户名
					bankaccbasVO.setPk_bankdoc(bankdoc.getPk_bankdoc());// 开户银行
					bankaccbasVO.setPk_banktype(bankdoc.getPk_banktype());// 银行类别
					bankaccbasVO.setPk_group(BusinessQuerydata.getInstance()
							.getpk_org());// 所属集团
					// bankaccbasVO.setPk_org("00016A10000000000380");//所属组织
					bankaccbasVO.setProvince(bankdoc.getProvince());
					bankaccbasVO.setQrybalanceitf(Integer.valueOf(0));
					bankaccbasVO.setSharetag(UFBoolean.FALSE);
					bankaccbasVO.setAreacode(bankdoc.getAreacode());// 地区代码
					bankaccbasVO.setCombineaccnum(bankdoc.getPcombinenum());// 人行联行号
					bankaccbasVO.setCombineaccname(bankdoc.getPcombinename());// 人行联行名称
					bankaccbasVO.setOrgnumber(bankdoc.getOrgnumber());// 机构号/分行号
					bankaccbasVO.setCustomernumber(bankdoc.getCustomernumber());// 客户编号
					bankaccbasVO.setIssigned(bankdoc.getIssigned());// 签约
					// 客商银行子账户
					BankAccSubVO subvo = new BankAccSubVO();
					subvo.setAccname(headJson.getString("supplier"));
					subvo.setAccnum(headJson.getString("def43"));
					subvo.setPk_currtype(nc.vo.tg.pub.BusinessQuerydata.getInstance()
							.getpk_currtype());
					subvo.setStatus(VOStatus.NEW);
					bankaccbasVO.setBankaccsub(new BankAccSubVO[] { subvo });
					// bankaccbasVO.setban
					// 客户银行账户
					CustbankVO custbankVO = new CustbankVO();// 201712180001
																// 1002Z0100000000001K1
					custbankVO.setAccclass(Integer.valueOf(3));// 账户分类
					custbankVO.setPk_cust(supplier);
					custbankVO.setDataoriginflag(Integer.valueOf(0));// 分布式
					// custbankVO.setIsdefault();//默认
					CustBankaccUnionVO banaccUnionvo = new CustBankaccUnionVO();
					bankaccbasVO.setStatus(VOStatus.NEW);
					// BankAccbasVO accvo
					// =NCLocator.getInstance().lookup(IBankAccBaseInfoService.class).insertBankAccBaseInfo(bankaccbasVO);
					banaccUnionvo.setBankaccbasVO(bankaccbasVO);
					banaccUnionvo.setCustbankVO(custbankVO);
					ICustBankaccService custservice = NCLocator.getInstance()
							.lookup(ICustBankaccService.class);
					CustBankaccUnionVO newUnionvo = custservice
							.insertCustBankacc(banaccUnionvo);
					newUnionvo.getBankaccbasVO().setEnablestate(
							IPubEnumConst.ENABLESTATE_ENABLE);
					newUnionvo.getBankaccbasVO()
							.setEnabletime(new UFDateTime());
					newUnionvo.getBankaccbasVO().setEnableuser(
							"NC_USER0000000000000");
					custservice.enableCustBankacc(newUnionvo);
					pk_bankaccbas = subvo.getPk_bankaccsub();
				} else {
					throw new BusinessException("该开户行并未在NC中关联");
				}
			}
		}

		String[] result = new String[2];
		result[0] = supplier;
		result[1] = pk_bankaccbas;
		return result;
	}
}
