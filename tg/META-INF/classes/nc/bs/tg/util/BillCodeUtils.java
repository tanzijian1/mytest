package nc.bs.tg.util;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.itf.org.IOrgConst;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.billcode.vo.BillCodeContext;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public class BillCodeUtils {
	static BillCodeUtils utils = null;
	private IBillcodeManage billcodeManage;

	public static BillCodeUtils getUtils() {
		if (utils == null) {
			utils = new BillCodeUtils();
		}
		return utils;
	}

	/**
	 * ÍË»Ø±àÂë
	 * 
	 * @param delVO
	 * @param nbcr_code
	 * @throws BusinessException
	 */
	public void returnBillCodeForDel(SuperVO delVO, String nbcr_code)
			throws BusinessException {
		String pk_group = (String) delVO.getAttributeValue("pk_group");
		// String pk_org = (String) delVO.getAttributeValue("pk_org");
		String pk_org = IOrgConst.GLOBEORG;
		BillCodeContext billCodeContext = getBillcodeServicer()
				.getBillCodeContext(nbcr_code, pk_group, pk_org);
		if (billCodeContext != null) {
			getBillcodeServicer().returnBillCodeOnDelete(nbcr_code, pk_group,
					pk_org, (String) delVO.getAttributeValue("code"), delVO);
		}

	}

	/**
	 * Ôö¼Ó±àÂë
	 * 
	 * @param insertedVO
	 * @param billCodeContext
	 * @param e
	 * @throws BusinessException
	 */
	public void abandonBillCode(SuperVO insertedVO, String nbcr_code,
			ValidationException e) throws BusinessException {
		String pk_group = (String) insertedVO.getAttributeValue("pk_group");
		// String pk_org = (String) insertedVO.getAttributeValue("pk_org");
		String pk_org = IOrgConst.GLOBEORG;
		BillCodeContext billCodeContext = getBillcodeServicer()
				.getBillCodeContext(nbcr_code, pk_group, pk_org);
		if (billCodeContext != null) {
			List<ValidationFailure> failures = e.getFailures();
			if (failures != null && failures.size() > 0) {
				for (ValidationFailure f : failures) {
					if (f.getErrorcode().equals("32001")) {
						getBillcodeServicer()
								.AbandonBillCode_RequiresNew(
										nbcr_code,
										(String) insertedVO
												.getAttributeValue("pk_group"),
										(String) insertedVO
												.getAttributeValue("pk_org"),
										(String) insertedVO
												.getAttributeValue("code"));
						break;
					}
				}
			}
		}
		throw e;
	}

	public void rollbackBillCode(SuperVO insertedVO, String nbcr_code,
			BusinessException e) throws BusinessException {
		String pk_group = (String) insertedVO.getAttributeValue("pk_group");
		// String pk_org = (String) insertedVO.getAttributeValue("pk_org");
		String pk_org = IOrgConst.GLOBEORG;
		BillCodeContext billCodeContext = getBillcodeServicer()
				.getBillCodeContext(nbcr_code, pk_group, pk_org);
		if (billCodeContext != null) {
			getBillcodeServicer().rollbackPreBillCode(nbcr_code,
					(String) insertedVO.getAttributeValue("pk_group"),
					(String) insertedVO.getAttributeValue("pk_org"),
					(String) insertedVO.getAttributeValue("code"));
		}
		throw e;
	}

	private IBillcodeManage getBillcodeServicer() {
		if (billcodeManage == null) {
			billcodeManage = NCLocator.getInstance().lookup(
					IBillcodeManage.class);
		}
		return billcodeManage;
	}

}
