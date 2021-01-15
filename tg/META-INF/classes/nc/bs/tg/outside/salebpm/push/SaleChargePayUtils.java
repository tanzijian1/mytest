package nc.bs.tg.outside.salebpm.push;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.salebpm.utils.SaleBPMBillUtils;
import nc.bs.tg.outside.salebpm.utils.SalePushBPMBillUtils;
import nc.itf.arap.pay.IArapPayBillService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.pnt.vo.FileManageVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * 税费缴纳申请单
 * 
 * @author nctanjingliang
 * 
 */
public class SaleChargePayUtils extends SaleBPMBillUtils {

	static SaleChargePayUtils utils;

	public static SaleChargePayUtils getUtils() {
		if (utils == null) {
			utils = new SaleChargePayUtils();
		}
		return utils;
	}

	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggPayBillVO aggVO = (AggPayBillVO) bill;
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		String deptid = getHCMDeptID((String) aggVO.getParentVO()
				.getAttributeValue("pu_deptid"));
		Map<String, Object> formData = getFormData(billCode, aggVO);
		Map<String, String> infoMap = SalePushBPMBillUtils.getUtils()
				.pushBillToBpm(userid, formData,
						ISaleBPMBillCont.getBillNameMap().get(billCode),
						deptid, bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("def55"));
		aggVO.getParentVO().setAttributeValue("def55", infoMap.get("taskID"));
		aggVO.getParentVO().setAttributeValue("def56", infoMap.get("OpenUrl"));
		aggVO.getParentVO().setAttributeValue("def60", "N");// nc收回bpm标识
		aggVO.getParentVO().setAttributeValue("def71", "N");// nc通知bpm驳回标识
		aggVO.getParentVO().setAttributeValue("def33", null);
		return aggVO;
	}

	/**
	 * 信息转换
	 * 
	 * @param billCode
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFormData(String billCode, AggPayBillVO aggVO)
			throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
		formData = getPayBillsInfo(aggVO);
		return formData;
	}

	/**
	 * 获得传参信息
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getPayBillsInfo(AggPayBillVO aggVO)
			throws BusinessException {
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
		// 单据主键
		purchase.put("BussinessId",
				aggVO.getParentVO().getAttributeValue(PayBillVO.PK_PAYBILL));

		// 标题
		purchase.put("Title",
				aggVO.getParentVO().getAttributeValue(PayBillVO.DEF54));

		// 申请人和申请部门
		if (aggVO.getParentVO().getAttributeValue(PayBillVO.BILLMAKER) != null) {
			if (getUserInfoByID((String) aggVO.getParentVO().getAttributeValue(
					PayBillVO.BILLMAKER)) != null) {
				purchase.put(
						"Applicant",
						getUserInfoByID(
								(String) aggVO.getParentVO().getAttributeValue(
										PayBillVO.BILLMAKER)).get("psnname"));
				purchase.put(
						"ApplicationDepartment",
						getUserInfoByID(
								(String) aggVO.getParentVO().getAttributeValue(
										PayBillVO.BILLMAKER)).get("deptname"));
			} else {
				purchase.put("Applicant", null);
				purchase.put("ApplicationDepartment", null);
			}
		} else {
			purchase.put("Applicant", null);
			purchase.put("ApplicationDepartment", null);
		}

		// 申请日期
		purchase.put(
				"ApplicationDate",
				aggVO.getParentVO().getAttributeValue(PayBillVO.BILLDATE) == null ? null
						: aggVO.getParentVO()
								.getAttributeValue(PayBillVO.BILLDATE)
								.toString());

		// 申请公司
		if (aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG) != null) {
			if (QueryDocInfoUtils.getUtils().getOrgInfo(
					(String) aggVO.getParentVO().getAttributeValue(
							PayBillVO.PK_ORG)) != null) {
				purchase.put(
						"ApplicationCompany",
						QueryDocInfoUtils
								.getUtils()
								.getOrgInfo(
										(String) aggVO.getParentVO()
												.getAttributeValue(
														PayBillVO.PK_ORG))
								.get("name"));
			} else {
				purchase.put("ApplicationCompany", null);
			}
		} else {
			purchase.put("ApplicationCompany", null);
		}

		// 付款单位
		if (aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG) != null) {
			if (QueryDocInfoUtils.getUtils().getOrgInfo(
					(String) aggVO.getParentVO().getAttributeValue(
							PayBillVO.PK_ORG)) != null) {
				purchase.put(
						"PaymentUnit",
						QueryDocInfoUtils
								.getUtils()
								.getOrgInfo(
										(String) aggVO.getParentVO()
												.getAttributeValue(
														PayBillVO.PK_ORG))
								.get("name"));
			} else {
				purchase.put("PaymentUnit", null);
			}
		} else {
			purchase.put("PaymentUnit", null);
		}

		// 收款单位
		if (aggVO.getParentVO().getAttributeValue(PayBillVO.SUPPLIER) != null) {
			if (QueryDocInfoUtils.getUtils().getSupplierInfo(
					(String) aggVO.getParentVO().getAttributeValue(
							PayBillVO.SUPPLIER)) != null) {
				purchase.put(
						"CollectionUnit",
						QueryDocInfoUtils
								.getUtils()
								.getSupplierInfo(
										(String) aggVO.getParentVO()
												.getAttributeValue(
														PayBillVO.SUPPLIER))
								.get("name"));
			} else {
				purchase.put("CollectionUnit", null);
			}
		} else {
			purchase.put("CollectionUnit", null);
		}

		// 请款金额
		purchase.put("AmountOfPayment",
				aggVO.getParentVO().getAttributeValue(PayBillVO.MONEY));

		// 累计已付款
		purchase.put("CumulativePaid",
				aggVO.getParentVO().getAttributeValue(PayBillVO.DEF57));

		// 项目名称
		if (aggVO.getParentVO().getAttributeValue(PayBillVO.DEF32) != null) {
			if (QueryDocInfoUtils.getUtils().getProjectByPK(
					(String) aggVO.getParentVO().getAttributeValue(
							PayBillVO.DEF32)) != null) {
				purchase.put(
						"ProjectName",
						QueryDocInfoUtils
								.getUtils()
								.getProjectByPK(
										(String) aggVO.getParentVO()
												.getAttributeValue(
														PayBillVO.DEF32))
								.get("project_name"));
			} else {
				purchase.put("ProjectName", null);
			}
		} else {
			purchase.put("ProjectName", null);
		}

		// 结算方式
		if (aggVO.getParentVO().getAttributeValue(PayBillVO.PK_BALATYPE) != null) {
			if (QueryDocInfoUtils.getUtils().getBalatypeByPK(
					(String) aggVO.getParentVO().getAttributeValue(
							PayBillVO.PK_BALATYPE)) != null) {
				purchase.put(
						"SettlementMethod",
						QueryDocInfoUtils
								.getUtils()
								.getBalatypeByPK(
										(String) aggVO.getParentVO()
												.getAttributeValue(
														PayBillVO.PK_BALATYPE))
								.get("name"));
			} else {
				purchase.put("SettlementMethod", null);
			}
		} else {
			purchase.put("SettlementMethod", null);
		}

		// 用款内容
		purchase.put("UsageContent",
				aggVO.getParentVO().getAttributeValue(PayBillVO.BIG_TEXT_A));

		// 附件
		String Attachments = "";
		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
				aggVO.getPrimaryKey());
		if (fileVOs != null && fileVOs.size() > 0) {
			for (int i = 0; i < fileVOs.size(); i++) {
				Attachments += fileVOs.get(i).getDocument_name() + "&"
						+ fileVOs.get(i).getFile_id() + ";";
			}
			purchase.put("File",
					Attachments.substring(0, Attachments.lastIndexOf(";")));
		} else {
			purchase.put("File", null);
		}
		Map<String, Object> formData = new HashMap<String, Object>();// 表头数据
		formData.put("I_TaxPaymentProcess", purchase);

		List<Map<String, Object>> listPurchase = new ArrayList<Map<String, Object>>();

		PayBillItemVO[] bodyvos = aggVO.getBodyVOs();
		for (PayBillItemVO payBillItemVO : bodyvos) {
			Map<String, Object> bodyPurchase = new HashMap<String, Object>();// 表体数据

			// 项目名称
			if (payBillItemVO.getProject() != null) {
				if (QueryDocInfoUtils.getUtils().getProjectByPK(
						(String) payBillItemVO.getProject()) != null) {
					bodyPurchase
							.put("ProjectName",
									QueryDocInfoUtils
											.getUtils()
											.getProjectByPK(
													(String) payBillItemVO
															.getProject())
											.get("project_name"));
				} else {
					bodyPurchase.put("ProjectName", null);
				}
			} else {
				bodyPurchase.put("ProjectName", null);
			}

			// 项目分期
			if (payBillItemVO.getDef37() != null) {
				if (QueryDocInfoUtils.getUtils().getProjectByPK(
						(String) payBillItemVO.getDef37()) != null) {
					bodyPurchase.put("PeriodName", QueryDocInfoUtils.getUtils()
							.getProjectByPK((String) payBillItemVO.getDef37())
							.get("project_name"));
				} else {
					bodyPurchase.put("PeriodName", null);
				}
			} else {
				bodyPurchase.put("PeriodName", null);
			}

			// 税种
			if (payBillItemVO.getDef41() != null) {
				if (QueryDocInfoUtils.getUtils().getDefdocInfo(
						payBillItemVO.getDef41()) != null) {
					bodyPurchase.put("TaxCategory", QueryDocInfoUtils
							.getUtils().getDefdocInfo(payBillItemVO.getDef41())
							.get("name"));
				} else {
					bodyPurchase.put("TaxCategory", null);
				}
			} else {
				bodyPurchase.put("TaxCategory", null);
			}

			// 金额
			bodyPurchase.put("Amount", payBillItemVO.getMoney_de());

			listPurchase.add(bodyPurchase);
		}

		formData.put("C_TaxPayment", listPurchase);

		return formData;
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
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
}
