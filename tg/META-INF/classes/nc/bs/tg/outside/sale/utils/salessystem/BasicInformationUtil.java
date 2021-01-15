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
	 * �ͻ�������Ϣ������
	 * 
	 * @param value
	 * @param dectype
	 * @param srcsystem
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String[] onBasicBill(JSONObject headJson) throws BusinessException {
		// ���������˻��ֶ�У��
		if (!"F3-Cxx-024".equals(headJson.getString("pk_tradetypeid"))) {
			if (headJson.getString("def43") == null) {
				throw new BusinessException("�����˺Ų���Ϊ�գ������������");
			}
		}
		if (headJson.getString("supplier") == null) {
			throw new BusinessException("��Ӧ�����Ʋ���Ϊ�գ������������");
		}
		String pk_org = BusinessQuerydata.getInstance().getPk_org(
				headJson.getString("pk_org"));

		PayBillConvertor convertor = new PayBillConvertor();
		// ��Ӧ�̣��ͻ������̣�
		String supplier = convertor.getRefAttributePk("paybill-supplier",
				headJson.getString("supplier"), pk_org, pk_org);

		if (supplier == null || "".equals(supplier)) {

			CustomerVO custvo2;

			String pk_timezone = BusinessQuerydata.getInstance().getStringvalue(
					"bd_timezone", "pk_timezone", "name", "����ʱ��(UTC+08:00)");
			String pk_custclass = BusinessQuerydata.getInstance().getcustomerClass();
			String pk_currtype = BusinessQuerydata.getInstance().getpk_currtype();
			String pk_format = BusinessQuerydata.getInstance().getStringvalue(
					"bd_formatdoc", "pk_formatdoc", "name", "���ļ���");
			String pk_country = BusinessQuerydata.getInstance().getStringvalue(
					"bd_countryzone", "pk_country ", "name", "�й�");

			CustomerVO custvo = new CustomerVO();

			custvo.setAttributeValue(CustomerVO.NAME,
					headJson.getString("supplier"));
			custvo.setAttributeValue(CustomerVO.PK_TIMEZONE, pk_timezone);// ����ʱ��(UTC+08:00)"0001Z010000000079U2P"
			custvo.setAttributeValue(CustomerVO.PK_CUSTCLASS, pk_custclass);// �ͻ���������
			custvo.setAttributeValue(CustomerVO.ENABLESTATE, Integer.valueOf(2));
			custvo.setAttributeValue(CustomerVO.PK_FORMAT, pk_format);// ���ݸ�ʽ--���ļ���
			custvo.setAttributeValue(CustomerVO.PK_CURRTYPE, pk_currtype);// ����
			custvo.setAttributeValue(CustomerVO.PK_COUNTRY, pk_country);// ����--�й�
			custvo.setAttributeValue(CustomerVO.PK_GROUP,
					"000112100000000005FD");// ����
			custvo.setAttributeValue(CustomerVO.PK_ORG, "000112100000000005FD");// ��֯
			custvo.setAttributeValue(CustomerVO.CUSTPROP, 0);// �ͻ�����
			custvo.setAttributeValue(CustomerVO.DEF1, "SALE");// ��Դϵͳ����
			custvo.setAttributeValue(CustomerVO.DEF2,
					headJson.getString("def43"));
			IBillcodeManage codeManage = NCLocator.getInstance().lookup(
					IBillcodeManage.class);
			// ���ݱ�����򴴽����̱���
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
				supvo.setAttributeValue(SupplierVO.PK_ORG, custvo2.getPk_org());// ��֯
				supvo.setAttributeValue(SupplierVO.PK_GROUP,
						custvo2.getPk_group());// ����
				supvo.setAttributeValue(SupplierVO.CODE, code);// ����
				supvo.setAttributeValue(SupplierVO.PK_CURRTYPE, pk_currtype);
				supvo.setAttributeValue(SupplierVO.NAME,
						custvo.getAttributeValue(CustomerVO.NAME));// ����
				supvo.setAttributeValue(SupplierVO.ISCUSTOMER, UFBoolean.FALSE);// �Ƿ�ͻ�
				supvo.setAttributeValue(SupplierVO.PK_COUNTRY, pk_country);// ���ҵ���
				supvo.setAttributeValue(SupplierVO.PK_TIMEZONE, pk_timezone);// ʱ��
				supvo.setAttributeValue(SupplierVO.PK_SUPPLIERCLASS, BusinessQuerydata
						.getInstance().getpk_supplierclass());// BusinessQuerydata.getInstance().getStringvalue("bd_supplierclass",
																// "pk_supplierclass",
																// "name",
																// "�ڲ���Ӧ��")
				supvo.setAttributeValue(SupplierVO.PK_FORMAT, pk_format);// ���ݸ�ʽ
				supvo.setAttributeValue(SupplierVO.SUPPROP,
						custvo.getCustprop());// �ͻ�����
				supvo.setStatus(VOStatus.NEW);
				supvo.setDef1("SALE");// ��Դϵͳ����
				supvo.setDef2(headJson.getString("def43"));// ��Դϵͳid
				// ISupplierBaseInfoService supservice = NCLocator.getInstance()
				// .lookup(ISupplierBaseInfoService.class);
				NCLocator.getInstance().lookup(ICustSupplierService.class)
						.insertSupAndRelaToCust(supvo, custvo2);

				supplier = custvo2.getPk_supplier();

			}
		}
		// �ͻ����˺ţ��������������У�ncͬ����ebs����
		// �������ncͬ����ebs�����˻�����(��˾/����)��
		// ����״̬��Ĭ�������ã��������ˣ�������ͬ�����û�ȥ���ã���
		// ����ʱ�䣨ͬ��ʱ�䣩�����кţ�
		// ����������ʡ�ݣ����У����֣�Ĭ������ң�
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
					bankaccbasVO.setAccattribute(1);// �˻�����
					bankaccbasVO
							.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);// ����״̬
					bankaccbasVO.setStatus(VOStatus.NEW);
					bankaccbasVO.setAccclass(1);// �˻�����
					bankaccbasVO.setAccname(headJson.getString("supplier"));// ����
					bankaccbasVO.setAccnum(headJson.getString("def43"));// �˺�
					bankaccbasVO.setAccopendate(new UFDate());
					bankaccbasVO.setAccountproperty(Integer.valueOf(1));// �˻�����
					bankaccbasVO.setAccstate(Integer.valueOf(0));// �˻�״̬
					bankaccbasVO.setBankarea(bankdoc.getBankarea());// ��������
					bankaccbasVO.setCity(bankdoc.getCity());// ����
					bankaccbasVO.setCombinenum(bankdoc.getCombinenum());// ���к�
					bankaccbasVO.setDataoriginflag(Integer.valueOf(0));// �ֲ�ʽ
					bankaccbasVO
							.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);
					bankaccbasVO.setEnabletime(new UFDateTime());
					bankaccbasVO.setEnableuser("#UAP#");
					// bankaccbasVO.setName(newName);//�˻���
					bankaccbasVO.setPk_bankdoc(bankdoc.getPk_bankdoc());// ��������
					bankaccbasVO.setPk_banktype(bankdoc.getPk_banktype());// �������
					bankaccbasVO.setPk_group(BusinessQuerydata.getInstance()
							.getpk_org());// ��������
					// bankaccbasVO.setPk_org("00016A10000000000380");//������֯
					bankaccbasVO.setProvince(bankdoc.getProvince());
					bankaccbasVO.setQrybalanceitf(Integer.valueOf(0));
					bankaccbasVO.setSharetag(UFBoolean.FALSE);
					bankaccbasVO.setAreacode(bankdoc.getAreacode());// ��������
					bankaccbasVO.setCombineaccnum(bankdoc.getPcombinenum());// �������к�
					bankaccbasVO.setCombineaccname(bankdoc.getPcombinename());// ������������
					bankaccbasVO.setOrgnumber(bankdoc.getOrgnumber());// ������/���к�
					bankaccbasVO.setCustomernumber(bankdoc.getCustomernumber());// �ͻ����
					bankaccbasVO.setIssigned(bankdoc.getIssigned());// ǩԼ
					// �����������˻�
					BankAccSubVO subvo = new BankAccSubVO();
					subvo.setAccname(headJson.getString("supplier"));
					subvo.setAccnum(headJson.getString("def43"));
					subvo.setPk_currtype(nc.vo.tg.pub.BusinessQuerydata.getInstance()
							.getpk_currtype());
					subvo.setStatus(VOStatus.NEW);
					bankaccbasVO.setBankaccsub(new BankAccSubVO[] { subvo });
					// bankaccbasVO.setban
					// �ͻ������˻�
					CustbankVO custbankVO = new CustbankVO();// 201712180001
																// 1002Z0100000000001K1
					custbankVO.setAccclass(Integer.valueOf(3));// �˻�����
					custbankVO.setPk_cust(supplier);
					custbankVO.setDataoriginflag(Integer.valueOf(0));// �ֲ�ʽ
					// custbankVO.setIsdefault();//Ĭ��
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
					throw new BusinessException("�ÿ����в�δ��NC�й���");
				}
			}
		}

		String[] result = new String[2];
		result[0] = supplier;
		result[1] = pk_bankaccbas;
		return result;
	}
}
