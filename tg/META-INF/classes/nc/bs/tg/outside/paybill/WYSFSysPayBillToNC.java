package nc.bs.tg.outside.paybill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 供应商付款单
 * 
 * @author kyy
 * 
 */
public class WYSFSysPayBillToNC extends PayBillBaseUtils {
	public static final String DefaultOperator = "LLWYSF";// 默认制单人

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(DefaultOperator));
		InvocationInfoProxy.getInstance().setUserCode(DefaultOperator);
		// 外系统信息
		JSONObject data = (JSONObject) info.get("data");// 外系统来源表头数据
		JSONObject jsonhead = (JSONObject) data.get("headInfo");// 外系统来源表头数据
		String srcid = jsonhead.getString("srcid");// 来源ID
		String srcno = jsonhead.getString("srcbillno");// 来源单据号
		Map<String, String> resultInfo = new HashMap<String, String>();
		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【"
						+ billkey
						+ "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue(
								PayBillVO.BILLNO) + "】,请勿重复上传!");
			}
			AggPayBillVO billvo = onTranBill(info);
			if(billvo.getChildrenVO().length==1)
				billvo.getParentVO().setAttributeValue("def74", billvo.getChildrenVO()[0].getAttributeValue("contractno"));
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			WorkflownoteVO worknoteVO = ((IWorkflowMachine) NCLocator.getInstance().lookup(
					IWorkflowMachine.class)).checkWorkFlow("SAVE",
							(String) billvo.getParentVO().getAttributeValue(PayBillVO.PK_BILLTYPE), billvo, eParam);
			AggPayBillVO[] obj = (AggPayBillVO[]) getPfBusiAction().processAction("SAVE", "F3", worknoteVO,
					billvo, null, null);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
			// WorkflownoteVO noteVO =
			// getWorkflowMachine().checkWorkflowActions(
			// "F1", billvos[0].getPrimaryKey());
			// getPfBusiAction().processAction("SAVE", "F1", noteVO, billvos[0],
			// null, null);
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	@Override
	protected AggPayBillVO onDefaultValue(JSONObject head, JSONArray bodylist)
			throws BusinessException {
		// TODO 自动生成的方法存根
		return super.onDefaultValue(head, bodylist);
	}

	/**
	 * 邻里-供应商合同\非合同请款
	 */
	@Override
	protected String getTradetype() {
		// TODO 自动生成的方法存根
		return "F3-Cxx-LL02";
	}

	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("pk_org"))) {
			throw new BusinessException("财务组织不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("WYSF主键不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("WYSF单据号不可为空");
		}
		// if (StringUtils.isBlank(head.getString("contcode"))) {
		// throw new BusinessException("合同编码不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("contname"))) {
		// throw new BusinessException("合同名称不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("conttype"))) {
		// throw new BusinessException("合同类型不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("plate"))) {
		// throw new BusinessException("板块不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("supplier"))) {
		// throw new BusinessException("供应商不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("proejctdata"))) {
		// throw new BusinessException("项目不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("proejctstages"))) {
		// throw new BusinessException("项目分期不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("totalmny_inv"))) {
		// throw new BusinessException("累计发票金额不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("auditstate"))) {
		// throw new BusinessException("地区财务审批状态不可为空");
		// }
		if (StringUtils.isBlank(head.getString("billdate"))) {
			throw new BusinessException("单据日期不可为空");
		}

	}

	@Override
	protected void setHeaderVO(PayBillVO hvo, JSONObject head)
			throws BusinessException {

		checkHeaderNotNull(head);
		// bpmid
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));
		hvo.setAttributeValue("def1", head.getString("srcid"));// 自定义项1 外系统主键
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// 自定义项2
																	// 外系统单据号->付款申请号
		hvo.setAttributeValue("def3", head.getString("imgcode"));// 自定义项3
																	// 影像编码->影像编码
		hvo.setAttributeValue("def4", head.getString("imgstate"));// 自定义项4
																	// 影像状态->影像状态
																	// hvo.setAttributeValue("def5",
																	// head.getString("contcode"));//
																	// 自定义项5
		// 合同编码->合同编码
		// hvo.setAttributeValue("def6", head.getString("contname"));//
		// 合同名称->合同名称
		// def6
		// hvo.setAttributeValue("def7", head.getString("conttype"));// 自定义项7
//		String itemtype = head.getString("itemtype");
//		if (StringUtils.isNotBlank(itemtype)) {
//			Map<String, String> itemtypeInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo(itemtype);
//			if (itemtypeInfo == null) {
//				throw new BusinessException("业务类型【" + itemtype
//						+ "】未能在NC档案关联到相关信息!");
//			}
//			hvo.setAttributeValue("def82", itemtypeInfo.get("pk_obj"));// 收费项目类型
//		}
//		String itemname = head.getString("itemname");
//		if (StringUtils.isNotBlank(itemname)) {
//			Map<String, String> itemnameInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo(itemname);
//			if (itemnameInfo == null) {
//				throw new BusinessException("业务类型【" + itemname
//						+ "】未能在NC档案关联到相关信息!");
//			}
//			hvo.setAttributeValue("def83", itemnameInfo.get("pk_obj"));// 收费项目名称
//		}

		String businesstype = head.getString("businesstype");
		if (StringUtils.isNotBlank(businesstype)) {
			Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(businesstype, "SDLL003");
			if (businesstypeInfo == null
					|| businesstypeInfo.get("pk_defdoc") == null) {
				throw new BusinessException("业务类型【" + businesstype
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def84", head.getString("businesstype"));// 业务类型
		}
	

		hvo.setAttributeValue("def86", head.getString("mailbox"));// 邮箱
		/**
		 * 成本应付单接口，自动根据合同类型带出应付单表头“财务票据类型”（NC字段-def56）,参照档案zdy008-2020-05-08-谈子健
		 * -start
		 */
		//
		/**
		 * 成本应付单接口，自动根据合同类型带出应付单表头“财务票据类型”（NC字段-def56）,参照档案zdy008-2020-05-08-谈子健
		 * -end
		 */
		// 合同细类->合同细类
		// 合同类型->合同类型
		// hvo.setAttributeValue("def8", head.getString("contcell"));// 自定义项8
		// 合同细类->合同细类
		// 紧急程度->紧急程度2020-02-18-tzj
		// String emergency = head.getString("mergency");
		// Map<String, String> emergencyInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(emergency, "zdy031");
		// if (emergencyInfo != null) {
		// hvo.setAttributeValue("def9", emergencyInfo.get("pk_defdoc"));//
		// 自定义项9
		// }
		// hvo.setAttributeValue("def10", "");// 自定义项10 来源外部系统

		// if (StringUtils.isNotBlank(head.getString("plate"))) {
		// Map<String, String> plateInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(head.getString("plate"), "bkxx");
		// if (plateInfo == null) {
		// throw new BusinessException("板块【" + head.getString("plate")
		// + "】未能在NC档案关联到相关信息!");
		// }
		// hvo.setAttributeValue("def15", plateInfo.get("pk_defdoc"));// 自定义项15
		// // 板块
		// }
		// if (StringUtils.isNotBlank(head.getString("accorg"))) {
		// Map<String, String> accorgInfo = DocInfoQryUtils.getUtils()
		// .getOrgInfo(head.getString("accorg"));
		// if (accorgInfo == null) {
		// throw new BusinessException("出账公司【" + head.getString("accorg")
		// + "】未能在NC档案关联到相关信息!");
		// }
		// hvo.setAttributeValue("def17", accorgInfo.get("pk_org"));// 自定义项17
		// // 出账公司->NC业务单元编码
		// }

		// hvo.setPk_busitype(null);//交易类型
		hvo.setAttributeValue(PayBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// 往来对象

//		 if (StringUtils.isNotBlank(head.getString("project"))) {
//		 HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
//		 .getSpecialProjectInfo(head.getString("proejctdata"));
//		 if (projectInfo == null) {
//		 throw new BusinessException("项目【"
//		 + head.getString("proejctdata") + "】未能在NC档案关联到相关信息!");
//		
//		 }
		 // hvo.setAttributeValue("def19", projectInfo.get("pk_project"));//
		// 项目
		// 应付单由传入项目信息带出是否资本(def8),项目资质(def9)
		// hvo.setAttributeValue("def61", projectInfo.get("def8"));// 是否资本
		// hvo.setAttributeValue("def62", projectInfo.get("def9"));// 项目性质

		// }

		// if (StringUtils.isNotBlank(head.getString("proejctstages"))) {
		// HashMap<String, String> projectstagesInfo = DocInfoQryUtils
		// .getUtils().getSpecialProjectInfo(
		// head.getString("proejctstages"));
		// if (projectstagesInfo == null) {
		// throw new BusinessException("项目分期 【"
		// + head.getString("proejctstages") + "】未能在NC档案关联到相关信息!");
		// }
		// hvo.setAttributeValue("def32",
		// projectstagesInfo.get("pk_project"));// 自定义项32
		// // 项目分期
		// }
		hvo.setAttributeValue("money",
				head.getString("money") == null ? UFDouble.ZERO_DBL
						: new UFDouble(head.getString("money")));// 本次票据金额
		// hvo.setAttributeValue("def21", headvo.getTotalmny_request());//
		// 累计请款金额
		// hvo.setAttributeValue("def22", headvo.getTotalmny_pay());// 累计付款金额
		// hvo.setAttributeValue("def23", headvo.getIsshotgun());// 是否先付款后补票
		// hvo.setAttributeValue("def24", headvo.getTotalmny_paybythisreq());//
		// 本次请款累计付款金额
		// hvo.setAttributeValue("def26",
		// head.getString("totalmny_inv") == null ? UFDouble.ZERO_DBL
		// : new UFDouble(head.getString("totalmny_inv")));// 累计发票金额
		// hvo.setAttributeValue("def28", headvo.getIsdeduction());// 是否有质保金扣除
		// hvo.setAttributeValue("def31", head.getString("note"));// 说明
		// hvo.setAttributeValue("def33", headvo.getAuditstate());
		// 地区财务审批状态
		// if (StringUtils.isNotBlank(head.getString("auditstate"))) {
		// Map<String, String> auditstateInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(head.getString("auditstate"), "zdy032");
		// if (auditstateInfo == null) {
		// throw new BusinessException("地区财务审批状态 【"
		// + head.getString("auditstate") + "】未能在NC档案关联到相关信息!");
		// }
		// hvo.setAttributeValue("def33", auditstateInfo.get("pk_defdoc"));//
		// 自定义项32
		// // 项目分期
		// }
		// hvo.setAttributeValue("def43", head.getString("def43"));//
		// 非标准指定付款涵def43
		// hvo.setAttributeValue("def44", head.getString("def44"));// 补附件def44
		// hvo.setAttributeValue("def45", head.getString("def45"));// 已补全def45
		// hvo.setAttributeValue("def46", head.getString("dept"));// 经办部门
		// hvo.setAttributeValue("def47", head.getString("psndoc"));// 经办人
		// hvo.setAttributeValue("def50", "N");// 票据权责发生制
		// hvo.setAttributeValue("def55", head.getString("def55"));//
		// EBS请款方式-def55

		// if (StringUtils.isNotBlank(head.getString("signorg"))) {
		// Map<String, String> signOrgInfo = DocInfoQryUtils.getUtils()
		// .getOrgInfo(head.getString("signorg"));
		// if (signOrgInfo == null) {
		// throw new BusinessException("签约公司【" + head.getString("signorg")
		// + "】未能在NC档案关联到相关信息!");
		// }
		// hvo.setAttributeValue("def34", signOrgInfo.get("pk_org"));// 签约公司
		// }
		String pk_balatype = DocInfoQryUtils.getUtils().getBalatypeKey(
				head.getString("pk_balatype"));
		hvo.setAttributeValue("pk_balatype", pk_balatype);// 结算方式
		hvo.setAttributeValue(PayBillVO.RATE, new UFDouble(1));

	}

	@Override
	protected void setItemVO(PayBillVO headVO, PayBillItemVO itemVO,
			JSONObject body) throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(PayBillItemVO.PK_GROUP, headVO.getPk_group());// 所属集团
		itemVO.setAttributeValue(PayBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// 单据类型
		itemVO.setAttributeValue(PayBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// 交易类型
		itemVO.setAttributeValue(PayBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// 交易类型ID
		itemVO.setAttributeValue(PayBillItemVO.BILLDATE, headVO.getBilldate());// 单据日期
		itemVO.setAttributeValue(PayBillItemVO.BUSIDATE, headVO.getBusidate());// 起算日期
		itemVO.setAttributeValue(PayBillItemVO.PK_DEPTID, headVO.getPk_deptid());// 部门
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// 部 门
		itemVO.setAttributeValue(PayBillItemVO.PK_PSNDOC, headVO.getPk_psndoc());// 业务员

		itemVO.setAttributeValue(PayBillItemVO.OBJTYPE, headVO.getObjtype());// 往来对象

		itemVO.setAttributeValue(PayBillItemVO.DIRECTION,
				BillEnumCollection.Direction.DEBIT.VALUE);// 方向

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayBillItemVO.PAUSETRANSACT, UFBoolean.FALSE);// 挂起标志
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// 购销类型
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// 三角贸易
		itemVO.setAttributeValue(PayBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// 币种
		itemVO.setAttributeValue(PayBillItemVO.RATE, headVO.getRate());// 组织本币汇率
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// 收货国

		itemVO.setAttributeValue(PayBillItemVO.TAXRATE, body
				.getString("taxrate") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("taxrate")));// 税率
		itemVO.setAttributeValue(PayBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// 扣税类别
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_MONEY_DE, body
				.getString("money_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_de")));// 借方原币金额 //
		// 价税合计->付款计划明细金额
		itemVO.setAttributeValue(PayBillItemVO.MONEY_DE, body
				.getString("money_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_de")));// 本币金额
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_NOTAX_DE, body
				.getString("local_notax_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_notax_de")));// // 组织本币无税金额
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_TAX_DE, body
				.getString("local_tax_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_tax_de")));// 税额->付款计划明细不含税金额*税率
		itemVO.setAttributeValue(PayBillItemVO.NOTAX_DE, body
				.getString("notax_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("notax_de")));// 借方原币无税金额
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_NOTAX_DE, body
				.getString("notax_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("notax_de")));// 本币无税金额
		itemVO.setAttributeValue(PayBillItemVO.MONEY_DE, body
				.getString("money_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_de")));// 借方原币金额
		itemVO.setAttributeValue(PayBillItemVO.MONEY_BAL, body
				.getString("money_bal") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_bal")));// 借方原币余额
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_MONEY_BAL, body
				.getString("money_bal") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_bal")));// 本币余额余额

		itemVO.setAttributeValue(PayBillItemVO.CONTRACTNO,
				body.getString("contractno"));// 合同编码
		itemVO.setAttributeValue(PayBillItemVO.INVOICENO,
				body.getString("invoiceno"));// 发票编码
		itemVO.setAttributeValue(PayBillItemVO.DEF28,
				body.getString("contractname"));// 合同名称
		itemVO.setAttributeValue("memo",
				body.getString("memo"));// 备注
		if (StringUtils.isNotBlank(body.getString("productline"))) {
			itemVO.setAttributeValue(
					PayBillItemVO.PRODUCTLINE,
					DocInfoQryUtils.getUtils().saveProdlineByname(
							body.getString("productline")));// 产品线
		}
		// TODO 表体备注
//		String itemtype = body.getString("itemtype");
//		if (StringUtils.isNotBlank(itemtype)) {
//			Map<String, String> itemtypeInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo(itemtype);
//			if (itemtypeInfo == null) {
//				throw new BusinessException("业务类型【" + itemtype
//						+ "】未能在NC档案关联到相关信息!");
//			}
//			itemVO.setAttributeValue(PayBillItemVO.DEF57,
//					itemtypeInfo.get("pk_obj"));// 收费项目类型
//		}
		String itemname = body.getString("itemname");
		if (StringUtils.isNotBlank(itemname)) {
			Map<String, String> itemnameInfo = DocInfoQryUtils.getUtils()
					.getBudgetsubInfo(itemname);
			if (itemnameInfo == null) {
				throw new BusinessException("业务类型【" + itemname
						+ "】未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayBillItemVO.DEF58,
					itemnameInfo.get("pk_obj"));// 收费项目名称
			itemVO.setAttributeValue(PayBillItemVO.DEF57,itemnameInfo.get("pk_parent"));// 收费项目类型
		}

		String projectphase = body.getString("projectphase");
		if (StringUtils.isNotBlank(projectphase)) {
			Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(projectphase, "SDLL006");
			if (businesstypeInfo == null
					|| businesstypeInfo.get("pk_defdoc") == null) {
				throw new BusinessException("业务类型【" + projectphase
						+ "】未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayBillItemVO.DEF59,
					businesstypeInfo.get("pk_defdoc"));// 项目阶段
		}
		// 新增供应商2019-11-01-tzj
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				body.getString("supplier"), headVO.getPk_org(),
				headVO.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("供应商【" + body.getString("supplier")
					+ "】未能在NC档案查询到相关信息");
		}
		itemVO.setAttributeValue("supplier", pk_supplier);// 供应商
		itemVO.setAttributeValue(PayBillItemVO.SCOMMENT,
				body.getString("scomment"));// 摘要

		itemVO.setAttributeValue(PayBillItemVO.DEF55, body.getString("rowid"));// 行id
		itemVO.setAttributeValue(PayBillItemVO.DEF56,
				body.getString("contractname"));// 合同名称
		if(StringUtils.isNotBlank(body.getString("arrears"))){
			Map<String, String> detdocMap = DocInfoQryUtils.getUtils().getDefdocInfo(body.getString("arrears"), "SDLL002");
			if(detdocMap==null){
				throw new BusinessException("是否往年欠费【" + body.getString("arrears")
						+ "】未能在NC档案查询到相关信息");
			}
			itemVO.setAttributeValue(PayBillItemVO.DEF25,
					detdocMap.get("pk_defdoc"));// 是否往年欠费
		}
	
		 if (StringUtils.isNotBlank(body.getString("project"))) {
			 HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
			 .getSpecialProjectInfo(body.getString("project"));
			 if (projectInfo == null) {
			 throw new BusinessException("项目【"
			 + body.getString("project") + "】未能在NC档案关联到相关信息!");
			
			 }
			 itemVO.setProject(projectInfo.get("pk_project"));
		 }
			String businessbreakdown = body.getString("businessbreakdown");
			if (StringUtils.isNotBlank(businessbreakdown)) {
				Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
						.getDefdocInfo(businessbreakdown, "SDLL004");
				if (businesstypeInfo == null
						|| businesstypeInfo.get("pk_defdoc") == null) {
					throw new BusinessException("业务细类【" + businessbreakdown
							+ "】未能在NC档案关联到相关信息!");
				}
				itemVO.setAttributeValue("def27", businessbreakdown);// 业务细类
			}
		// if (StringUtils.isNotBlank(body.getString("subjcode"))) {
		// String pk_subjcode = DocInfoQryUtils.getUtils().getAccSubInfo(
		// body.getString("subjcode"), headVO.getPk_org());
		// if (pk_subjcode == null) {
		// throw new BusinessException("会计科目["
		// + body.getString("subjcode") + "]未能在NC档案关联到相关信息!");
		// }
		// itemVO.setAttributeValue(PayBillItemVO.SUBJCODE, pk_subjcode);// 科目编码
		// // 会计科目
		// }
		// if (StringUtils.isNotBlank(body.getString("costsubject"))) {
		// Map<String, String> costsubjectInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(body.getString("costsubject"), "zdy024");
		// if (costsubjectInfo == null) {
		// throw new BusinessException("成本科目["
		// + body.getString("costsubject") + "]未能在NC档案关联到!");
		// }
		// itemVO.setAttributeValue("def7", costsubjectInfo.get("pk_defdoc"));//
		// 成本科目
		//
		// }

		// if (StringUtils.isNotBlank(body.getString("invtype"))) {
		// Map<String, String> invtypetInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(body.getString("invtype"), "pjlx");
		// if (invtypetInfo == null) {
		// throw new BusinessException("票种[" + body.getString("invtype")
		// + "]未能在NC档案关联到!");
		// }
		// itemVO.setAttributeValue("def8", invtypetInfo.get("pk_defdoc"));
		// }
		// // 设置业态 def3
		// itemVO.setAttributeValue("def3", body.getString("format"));
		// // 设置比例 def4
		// UFDouble formatratio = body.getString("formatratio") == null ?
		// UFDouble.ZERO_DBL
		// : new UFDouble(body.getString("formatratio"));
		// itemVO.setAttributeValue("def4",
		// formatratio.multiply(100).toString());
		// // 财务票据类型
		// itemVO.setAttributeValue("def9", "100112100000000069WH");
		// // 成本应付单表体def15可抵扣税额，根据发票类型来重新计算-2020-04-24-谈子健
		// String deductibleTax = getDeductibleTax(body);
		// itemVO.setAttributeValue("def15", deductibleTax);
		/**
		 * 成本应付单添加结算方式-2020-06-04-谈子健
		 */
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// 结算方式
		if (StringUtils.isNotBlank(body.getString("recaccount"))) {
			Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
					.getCustAccnumInfo(itemVO.getSupplier(),
							body.getString("recaccount"));
			if (recaccountInfo == null) {
				throw new BusinessException("收款银行账户["
						+ body.getString("recaccount") + "]未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.RECACCOUNT,
					recaccountInfo.get("pk_bankaccsub"));// 付款银行账户
		}

		// 付银行账户
		if (StringUtils.isNotBlank(body.getString("payaccount"))) {
			Map<String, String> payaccountInfo = DocInfoQryUtils.getUtils()
					.getBankaccnumInfo(itemVO.getPk_org(),
							body.getString("payaccount"));
			if (payaccountInfo == null) {
				throw new BusinessException("付款银行账户["
						+ body.getString("payaccount") + "]未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.PAYACCOUNT,
					payaccountInfo.get("pk_bankaccsub"));
		}

	}

	/**
	 * 检验行信息必录
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		if (StringUtils.isBlank(body.getString("supplier"))) {
			throw new BusinessException("供应商不可为空");
		}
		if (StringUtils.isBlank(body.getString("notax_de"))) {
			throw new BusinessException("借方原币无税金额不可为空");
		}
		if (new UFDouble(body.getString("notax_de")).doubleValue() < 0) {
			throw new BusinessException("借方原币无税金额不可小于0");
		}
		if (StringUtils.isBlank(body.getString("local_tax_de"))) {
			throw new BusinessException("税额不能为空");
		}
		if (new UFDouble(body.getString("local_tax_de")).doubleValue() < 0) {
			throw new BusinessException("税额不可小于0");
		}
		if (StringUtils.isBlank(body.getString("notax_de"))) {
			throw new BusinessException("借方原币无税金额不可为空");
		}
		if (StringUtils.isBlank(body.getString("money_de"))) {
			throw new BusinessException("借方原币金额不可为空");
		}
		if (new UFDouble(body.getString("money_de")).doubleValue() < 0) {
			throw new BusinessException("借方原币金额不可小于0");
		}
		if (StringUtils.isBlank(body.getString("money_bal"))) {
			throw new BusinessException("原币余额不可为空");
		}
		if (new UFDouble(body.getString("money_bal")).doubleValue() < 0) {
			throw new BusinessException("原币余额不可小于0");
		}
		if (StringUtils.isBlank(body.getString("taxrate"))) {
			throw new BusinessException("税率不可为空");
		}
		if (StringUtils.isBlank(body.getString("local_tax_de"))) {
			throw new BusinessException("无税金额不可为空");
		}
		if (new UFDouble(body.getString("local_tax_de")).doubleValue() < 0) {
			throw new BusinessException("无税金额不可小于0");
		}
		 if (StringUtils.isBlank(body.getString("project"))) {
		 throw new BusinessException("不可为空");
		 }
		// if (StringUtils.isBlank(body.getString("local_money_de"))) {
		// throw new BusinessException("价税合计不可为空");
		// }
		/*
		 * if (StringUtils.isBlank(body.getString("money_cr"))) { throw new
		 * BusinessException("借方原币金额不可为空"); } if
		 * (StringUtils.isBlank(body.getString("money_bal"))) { throw new
		 * BusinessException("原币余额不可为空"); }
		 */
		if (StringUtils.isBlank(body.getString("rowid"))) {
			throw new BusinessException("物业收费系统单据行ID不可为空");
		}
		if (StringUtils.isBlank(body.getString("productline"))) {
			throw new BusinessException("核销码不可为空");
		}
		 if (StringUtils.isBlank(body.getString("project"))) {
		 throw new BusinessException("项目不可为空");
		 }
		// if (StringUtils.isBlank(body.getString("rate"))) {
		// throw new BusinessException("票据类型(票种)不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("scomment"))) {
		// throw new BusinessException("摘要不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("supplier"))) {
		// throw new BusinessException("供应商不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("budgetsub"))) {
		// throw new BusinessException("预算科目不可为空");
		// }
	}

}
