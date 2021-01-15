package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.core.service.TimeService;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.appaybill.PayBillItemVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * wysf系统推送应付单数据到NC财务系统接口
 * 
 * @author lhh
 * 
 */
public class PayableBillForWysf extends PayableBillUtilsForWy {
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
			AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
					AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【"
						+ billkey
						+ "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "】,请勿重复上传!");
			}
			AggPayableBillVO billvo = onTranBill(info);
			if(billvo.getChildrenVO().length==1)
				billvo.getParentVO().setAttributeValue("def17", billvo.getChildrenVO()[0].getAttributeValue("contractno"));
			
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVE", "F1", null,
					billvo, null, null);
			AggPayableBillVO[] billvos = (AggPayableBillVO[]) obj;
			// WorkflownoteVO noteVO =
			// getWorkflowMachine().checkWorkflowActions(
			// "F1", billvos[0].getPrimaryKey());
			// getPfBusiAction().processAction("SAVE", "F1", noteVO, billvos[0],
			// null, null);
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayableBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}
	@Override
	protected AggPayableBillVO onDefaultValue(JSONObject head,
			JSONArray bodylist) throws BusinessException {

		AggPayableBillVO aggvo = new AggPayableBillVO();
		PayableBillVO hvo = new PayableBillVO();
		String billdate = head.getString("billdate") == null ? new UFDate()
				.toString() : head.getString("billdate");
		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// 当前时间
		hvo.setAttributeValue(PayableBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// 集团
		hvo.setAttributeValue(PayableBillVO.PK_ORG, DocInfoQryUtils.getUtils()
				.getOrgInfo(head.getString("pk_org")).get("pk_org"));// 应付财务组织->NC业务单元编码
		hvo.setAttributeValue(PayableBillVO.BILLMAKER,
				getUserIDByCode(DefaultOperator));// 制单人
		hvo.setAttributeValue(PayableBillVO.CREATOR, hvo.getBillmaker());// 创建人
		hvo.setAttributeValue(PayableBillVO.CREATIONTIME, currTime);// 创建时间
		hvo.setAttributeValue(PayableBillVO.PK_BILLTYPE, IBillFieldGet.F1);// 单据类型编码
		hvo.setAttributeValue(PayableBillVO.BILLCLASS, IBillFieldGet.YF);// 单据大类
		hvo.setAttributeValue(PayableBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据所属系统
		hvo.setAttributeValue(PayableBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据来源系统
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, IBillFieldGet.D1);// 应收类型code
		hvo.setAttributeValue(PayableBillVO.BILLSTATUS,
				ARAPBillStatus.SAVE.VALUE);// 单据状态
		// 交易类型
		BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetype());

		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPEID,
				billTypeVo.getPk_billtypeid());// 应收类型
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE,
				billTypeVo.getPk_billtypecode());// 应收类型
		hvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// 期初标志
		hvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.FALSE);// 是否红冲过
		hvo.setAttributeValue(PayableBillVO.BILLDATE, new UFDate(billdate));// 单据日期
		hvo.setAttributeValue(PayableBillVO.BUSIDATE, new UFDate(billdate));// 起算日期
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// 单据会计年度
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// 单据会计期间
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);
		setHeaderVO(hvo, head);

		aggvo.setParentVO(hvo);
		PayableBillItemVO[] itemVOs = new PayableBillItemVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new PayableBillItemVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// 行号
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
		getArapBillPubQueryService().getDefaultVO(aggvo, true);

		return aggvo;
	
	}
	/**
	 * 邻里-供应商合同\非合同请款
	 */
	@Override
	protected String getTradetype() {
		// TODO 自动生成的方法存根
		return "F1-Cxx-LL05";
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
//		if (StringUtils.isBlank(head.getString("contcode"))) {
//			throw new BusinessException("合同编码不可为空");
//		}
//		if (StringUtils.isBlank(head.getString("contname"))) {
//			throw new BusinessException("合同名称不可为空");
//		}
//		if (StringUtils.isBlank(head.getString("conttype"))) {
//			throw new BusinessException("合同类型不可为空");
//		}
//		if (StringUtils.isBlank(head.getString("plate"))) {
//			throw new BusinessException("板块不可为空");
//		}
//		if (StringUtils.isBlank(head.getString("supplier"))) {
//			throw new BusinessException("供应商不可为空");
//		}
//		if (StringUtils.isBlank(head.getString("proejctdata"))) {
//			throw new BusinessException("项目不可为空");
//		}
//		if (StringUtils.isBlank(head.getString("proejctstages"))) {
//			throw new BusinessException("项目分期不可为空");
//		}
//		if (StringUtils.isBlank(head.getString("totalmny_inv"))) {
//			throw new BusinessException("累计发票金额不可为空");
//		}
//		if (StringUtils.isBlank(head.getString("auditstate"))) {
//			throw new BusinessException("地区财务审批状态不可为空");
//		}
		 if (StringUtils.isBlank(head.getString("billdate"))) {
		 throw new BusinessException("单据日期不可为空");
		 }

	}


	@Override
	protected void setHeaderVO(PayableBillVO hvo, JSONObject head)
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
//		hvo.setAttributeValue("def5", head.getString("contcode"));// 自定义项5
																	// 合同编码->合同编码
//		hvo.setAttributeValue("def6", head.getString("contname"));// 合同名称->合同名称
																	// def6
//		hvo.setAttributeValue("def7", head.getString("conttype"));// 自定义项7
//		String itemtype = head.getString("itemtype");
//		if(StringUtils.isNotBlank(itemtype)){
//			Map<String,String> itemtypeInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo(itemtype);
//			if(itemtypeInfo==null){
//				throw new BusinessException("业务类型【" + itemtype
//						+ "】未能在NC档案关联到相关信息!");
//			}
//			hvo.setAttributeValue("def37",itemtypeInfo.get("pk_obj"));// 收费项目类型
//		}
//		String itemname = head.getString("itemname");
//		if(StringUtils.isNotBlank(itemname)){
//			Map<String,String> itemnameInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo( itemname);
//			if(itemnameInfo==null){
//				throw new BusinessException("业务类型【" + itemname
//						+ "】未能在NC档案关联到相关信息!");
//			}
//			hvo.setAttributeValue("def38",itemnameInfo.get("pk_obj"));// 收费项目名称
//		}
//		
		
		String businesstype = head.getString("businesstype");
		if(StringUtils.isNotBlank(businesstype)){
			Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(businesstype, "SDLL003");
			if(businesstypeInfo==null||businesstypeInfo.get("pk_defdoc")==null){
				throw new BusinessException("业务类型【" + businesstype
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def39", head.getString("businesstype"));// 业务类型
		}
		
		
		hvo.setAttributeValue("def41", head.getString("mailbox"));// 邮箱
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
//		hvo.setAttributeValue("def8", head.getString("contcell"));// 自定义项8
																	// 合同细类->合同细类
		// 紧急程度->紧急程度2020-02-18-tzj
//		String emergency = head.getString("mergency");
//		Map<String, String> emergencyInfo = DocInfoQryUtils.getUtils()
//				.getDefdocInfo(emergency, "zdy031");
//		if (emergencyInfo != null) {
//			hvo.setAttributeValue("def9", emergencyInfo.get("pk_defdoc"));// 自定义项9
//		}
//		hvo.setAttributeValue("def10", "");// 自定义项10 来源外部系统

//		if (StringUtils.isNotBlank(head.getString("plate"))) {
//			Map<String, String> plateInfo = DocInfoQryUtils.getUtils()
//					.getDefdocInfo(head.getString("plate"), "bkxx");
//			if (plateInfo == null) {
//				throw new BusinessException("板块【" + head.getString("plate")
//						+ "】未能在NC档案关联到相关信息!");
//			}
//			hvo.setAttributeValue("def15", plateInfo.get("pk_defdoc"));// 自定义项15
//																		// 板块
//		}
//		if (StringUtils.isNotBlank(head.getString("accorg"))) {
//			Map<String, String> accorgInfo = DocInfoQryUtils.getUtils()
//					.getOrgInfo(head.getString("accorg"));
//			if (accorgInfo == null) {
//				throw new BusinessException("出账公司【" + head.getString("accorg")
//						+ "】未能在NC档案关联到相关信息!");
//			}
//			hvo.setAttributeValue("def17", accorgInfo.get("pk_org"));// 自定义项17
//			// 出账公司->NC业务单元编码
//		}
	

		// hvo.setPk_busitype(null);//交易类型
		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// 往来对象

//		if (StringUtils.isNotBlank(head.getString("proejctdata"))) {
//			HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
//					.getSpecialProjectInfo(head.getString("proejctdata"));
//			if (projectInfo == null) {
//				throw new BusinessException("项目【"
//						+ head.getString("proejctdata") + "】未能在NC档案关联到相关信息!");
//
//			}
////			hvo.setAttributeValue("def19", projectInfo.get("pk_project"));// 项目
			// 应付单由传入项目信息带出是否资本(def8),项目资质(def9)
//			hvo.setAttributeValue("def61", projectInfo.get("def8"));// 是否资本
//			hvo.setAttributeValue("def62", projectInfo.get("def9"));// 项目性质

//		}

//		if (StringUtils.isNotBlank(head.getString("proejctstages"))) {
//			HashMap<String, String> projectstagesInfo = DocInfoQryUtils
//					.getUtils().getSpecialProjectInfo(
//							head.getString("proejctstages"));
//			if (projectstagesInfo == null) {
//				throw new BusinessException("项目分期 【"
//						+ head.getString("proejctstages") + "】未能在NC档案关联到相关信息!");
//			}
//			hvo.setAttributeValue("def32", projectstagesInfo.get("pk_project"));// 自定义项32
//																				// 项目分期
//		}
		hvo.setAttributeValue("money",
				head.getString("money") == null ? UFDouble.ZERO_DBL
						: new UFDouble(head.getString("money")));// 本次票据金额
		// hvo.setAttributeValue("def21", headvo.getTotalmny_request());//
		// 累计请款金额
		// hvo.setAttributeValue("def22", headvo.getTotalmny_pay());// 累计付款金额
		// hvo.setAttributeValue("def23", headvo.getIsshotgun());// 是否先付款后补票
		// hvo.setAttributeValue("def24", headvo.getTotalmny_paybythisreq());//
		// 本次请款累计付款金额
//		hvo.setAttributeValue("def26",
//				head.getString("totalmny_inv") == null ? UFDouble.ZERO_DBL
//						: new UFDouble(head.getString("totalmny_inv")));// 累计发票金额
		// hvo.setAttributeValue("def28", headvo.getIsdeduction());// 是否有质保金扣除
//		hvo.setAttributeValue("def31", head.getString("note"));// 说明
		// hvo.setAttributeValue("def33", headvo.getAuditstate());
		// 地区财务审批状态
//		if (StringUtils.isNotBlank(head.getString("auditstate"))) {
//			Map<String, String> auditstateInfo = DocInfoQryUtils.getUtils()
//					.getDefdocInfo(head.getString("auditstate"), "zdy032");
//			if (auditstateInfo == null) {
//				throw new BusinessException("地区财务审批状态 【"
//						+ head.getString("auditstate") + "】未能在NC档案关联到相关信息!");
//			}
//			hvo.setAttributeValue("def33", auditstateInfo.get("pk_defdoc"));// 自定义项32
//																			// 项目分期
//		}
//		hvo.setAttributeValue("def43", head.getString("def43"));// 非标准指定付款涵def43
//		hvo.setAttributeValue("def44", head.getString("def44"));// 补附件def44
//		hvo.setAttributeValue("def45", head.getString("def45"));// 已补全def45
//		hvo.setAttributeValue("def46", head.getString("dept"));// 经办部门
//		hvo.setAttributeValue("def47", head.getString("psndoc"));// 经办人
//		hvo.setAttributeValue("def50", "N");// 票据权责发生制
//		hvo.setAttributeValue("def55", head.getString("def55"));// EBS请款方式-def55

//		if (StringUtils.isNotBlank(head.getString("signorg"))) {
//			Map<String, String> signOrgInfo = DocInfoQryUtils.getUtils()
//					.getOrgInfo(head.getString("signorg"));
//			if (signOrgInfo == null) {
//				throw new BusinessException("签约公司【" + head.getString("signorg")
//						+ "】未能在NC档案关联到相关信息!");
//			}
//			hvo.setAttributeValue("def34", signOrgInfo.get("pk_org"));// 签约公司
//		}
		/**
		 * 成本应付单添加结算方式-2020-05-26-谈子健
		 */
		String pk_balatype = DocInfoQryUtils.getUtils().getBalatypeKey(
				head.getString("pk_balatype"));
		hvo.setAttributeValue("pk_balatype", pk_balatype);// 结算方式
		hvo.setAttributeValue(PayableBillVO.RATE, new UFDouble(1));

	}

	@Override
	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			JSONObject body) throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(PayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// 所属集团
		itemVO.setAttributeValue(PayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// 单据类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// 交易类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// 交易类型ID
		itemVO.setAttributeValue(PayableBillItemVO.BILLDATE,
				headVO.getBilldate());// 单据日期
		itemVO.setAttributeValue(PayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// 起算日期
		itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
				headVO.getPk_deptid());// 部门
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// 部 门
		itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
				headVO.getPk_psndoc());// 业务员

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, headVO.getObjtype());// 往来对象

		itemVO.setAttributeValue(PayableBillItemVO.DIRECTION,
				BillEnumCollection.Direction.CREDIT.VALUE);// 方向

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayableBillItemVO.PAUSETRANSACT,
				UFBoolean.FALSE);// 挂起标志
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// 购销类型
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// 三角贸易
		itemVO.setAttributeValue(PayableBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// 币种
		itemVO.setAttributeValue(PayableBillItemVO.RATE, headVO.getRate());// 组织本币汇率
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// 收货国

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, body
				.getString("taxrate") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("taxrate")));// 税率
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// 扣税类别
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR, body
				.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr")));// 借方原币金额 //
		// 价税合计->付款计划明细金额
//		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR,
//				itemVO.getMoney_cr());// 本币金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR, body
				.getString("local_notax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_notax_cr")));// // 组织本币无税金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR, body
				.getString("local_tax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_tax_cr")));// 税额->付款计划明细不含税金额*税率
		itemVO.setAttributeValue(PayableBillItemVO.NOTAX_CR,  body
				.getString("notax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("notax_cr")));//借方原币无税金额
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR,  body
				.getString("money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_cr")));//借方原币金额
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_BAL,  body
				.getString("money_bal") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_bal")));//借方原币余额
		itemVO.setAttributeValue("memo",
				body.getString("memo"));// 备注
//		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_BAL,  itemVO.getMoney_bal());
//		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_DE,  itemVO.getLocal_money_cr());
//		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_DE,  itemVO.getLocal_notax_cr());
		itemVO.setAttributeValue(PayableBillItemVO.CONTRACTNO,  body.getString("contractno"));//合同编码
		itemVO.setAttributeValue(PayableBillItemVO.INVOICENO,  body.getString("invoiceno"));//发票编码
		itemVO.setAttributeValue(PayableBillItemVO.DEF28,  body.getString("contractname"));//合同名称
		itemVO.setRate(body
				.getString("rate") == null ? UFDouble.ONE_DBL
				: new UFDouble(body.getString("rate")));//汇率
		if(StringUtils.isNotBlank(body.getString("productline"))){
			itemVO.setAttributeValue(PayableBillItemVO.PRODUCTLINE,  DocInfoQryUtils.getUtils().saveProdlineByname(body.getString("productline")));//产品线
			}
		String itemname = body.getString("itemname");
		if(StringUtils.isNotBlank(itemname)){
			Map<String,String> itemnameInfo = DocInfoQryUtils.getUtils()
					.getBudgetsubInfo( itemname);
			if(itemnameInfo==null){
				throw new BusinessException("业务类型【" + itemname
						+ "】未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.DEF31,itemnameInfo.get("pk_obj"));// 收费项目名称
			itemVO.setAttributeValue(PayableBillItemVO.DEF29,itemnameInfo.get("pk_parent"));// 收费项目类型
		}
		
		
		String projectphase = body.getString("projectphase");
		if(StringUtils.isNotBlank(projectphase)){
			Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(projectphase, "SDLL006");
			if(businesstypeInfo==null||businesstypeInfo.get("pk_defdoc")==null){
				throw new BusinessException("业务类型【" + projectphase
						+ "】未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.DEF32,  businesstypeInfo.get("pk_defdoc"));//项目阶段
		}
		// 新增供应商2019-11-01-tzj
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				body.getString("supplier"), headVO.getPk_org(), headVO.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("供应商【" + body.getString("supplier")
					+ "】未能在NC档案查询到相关信息");
		}
		itemVO.setAttributeValue("supplier", pk_supplier);// 供应商
		itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
				body.getString("scomment"));// 摘要
		
		itemVO.setAttributeValue(PayableBillItemVO.DEF24,
				body.getString("rowid"));// 行id
		itemVO.setAttributeValue(PayableBillItemVO.DEF28,
				body.getString("contractname"));// 合同名称
		itemVO.setAttributeValue(PayableBillItemVO.QUANTITY_CR,
				body.getString("quantity_cr")==null ? UFDouble.ZERO_DBL:new UFDouble(body.getString("quantity_cr")));//数量
		// 收款银行账户
		if(StringUtils.isNotBlank(body.getString("recaccount"))){
			Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
					.getBankaccnumInfo(itemVO.getPk_org(),
							body.getString("recaccount"));
			if (recaccountInfo == null) {
				throw new BusinessException("收款银行账户["
						+ body.getString("recaccount") + "]未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.RECACCOUNT,
					recaccountInfo.get("pk_bankaccsub"));// 付款银行账户
		}
		

		// 付银行账户
		if(StringUtils.isNotBlank(body.getString("payaccount"))){
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
		if(StringUtils.isNotBlank(body.getString("arrears"))){
			Map<String, String> detdocMap = DocInfoQryUtils.getUtils().getDefdocInfo(body.getString("arrears"), "SDLL002");
			if(detdocMap==null){
				throw new BusinessException("是否往年欠费【" + body.getString("arrears")
						+ "】未能在NC档案查询到相关信息");
			}
			itemVO.setAttributeValue("def37",
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
			if(StringUtils.isNotBlank(businessbreakdown)){
				Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
						.getDefdocInfo(businessbreakdown, "SDLL004");
				if(businesstypeInfo==null||businesstypeInfo.get("pk_defdoc")==null){
					throw new BusinessException("业务细类【" + businessbreakdown
							+ "】未能在NC档案关联到相关信息!");
				}
				itemVO.setAttributeValue("def38", businessbreakdown);//业务细类
			}
			
//		if (StringUtils.isNotBlank(body.getString("subjcode"))) {
//			String pk_subjcode = DocInfoQryUtils.getUtils().getAccSubInfo(
//					body.getString("subjcode"), headVO.getPk_org());
//			if (pk_subjcode == null) {
//				throw new BusinessException("会计科目["
//						+ body.getString("subjcode") + "]未能在NC档案关联到相关信息!");
//			}
//			itemVO.setAttributeValue(PayableBillItemVO.SUBJCODE, pk_subjcode);// 科目编码
//																				// 会计科目
//		}
//		if (StringUtils.isNotBlank(body.getString("costsubject"))) {
//			Map<String, String> costsubjectInfo = DocInfoQryUtils.getUtils()
//					.getDefdocInfo(body.getString("costsubject"), "zdy024");
//			if (costsubjectInfo == null) {
//				throw new BusinessException("成本科目["
//						+ body.getString("costsubject") + "]未能在NC档案关联到!");
//			}
//			itemVO.setAttributeValue("def7", costsubjectInfo.get("pk_defdoc"));// 成本科目
//
//		}

//		if (StringUtils.isNotBlank(body.getString("invtype"))) {
//			Map<String, String> invtypetInfo = DocInfoQryUtils.getUtils()
//					.getDefdocInfo(body.getString("invtype"), "pjlx");
//			if (invtypetInfo == null) {
//				throw new BusinessException("票种[" + body.getString("invtype")
//						+ "]未能在NC档案关联到!");
//			}
//			itemVO.setAttributeValue("def8", invtypetInfo.get("pk_defdoc"));
//		}
//		// 设置业态 def3
//		itemVO.setAttributeValue("def3", body.getString("format"));
//		// 设置比例 def4
//		UFDouble formatratio = body.getString("formatratio") == null ? UFDouble.ZERO_DBL
//				: new UFDouble(body.getString("formatratio"));
//		itemVO.setAttributeValue("def4", formatratio.multiply(100).toString());
//		// 财务票据类型
//		itemVO.setAttributeValue("def9", "100112100000000069WH");
//		// 成本应付单表体def15可抵扣税额，根据发票类型来重新计算-2020-04-24-谈子健
//		String deductibleTax = getDeductibleTax(body);
//		itemVO.setAttributeValue("def15", deductibleTax);
		/**
		 * 成本应付单添加结算方式-2020-06-04-谈子健
		 */
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// 结算方式
		if(StringUtils.isNotBlank(body.getString("arrears"))){
			Map<String, String> detdocMap = DocInfoQryUtils.getUtils().getDefdocInfo(body.getString("arrears"), "SDLL002");
			if(detdocMap==null){
				throw new BusinessException("是否往年欠费【" + body.getString("arrears")
						+ "】未能在NC档案查询到相关信息");
			}
			itemVO.setAttributeValue("",
					detdocMap.get("pk_defdoc"));// 是否往年欠费
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
		 
		 if (StringUtils.isBlank(body.getString("notax_cr"))) {
		 throw new BusinessException("借方原币无税金额不可为空");
		 }
		 if(new UFDouble(body.getString("notax_cr")).doubleValue()<0){
			 throw new BusinessException("借方原币无税金额不可小于0");
		 }
		if (StringUtils.isBlank(body.getString("taxrate"))) {
			throw new BusinessException("税率不可为空");
		}
//		if (StringUtils.isBlank(body.getString("local_notax_cr"))) {
//			throw new BusinessException("无税金额不可为空");
//		}
//		if(new UFDouble(body.getString("local_notax_cr")).doubleValue()<0){
//			 throw new BusinessException("无税金额不可小于0");
//		 }
		if (StringUtils.isBlank(body.getString("local_tax_cr"))) {
			throw new BusinessException("税额不可为空");
		}
		if(new UFDouble(body.getString("local_tax_cr")).doubleValue()<0){
			 throw new BusinessException("税额不可小于0");
		 }
//		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
//			throw new BusinessException("价税合计不可为空");
//		}
//		if(new UFDouble(body.getString("local_money_cr")).doubleValue()<0){
//			 throw new BusinessException("价税合计不可小于0");
//		 }
		 if (StringUtils.isBlank(body.getString("money_cr"))) {
		 throw new BusinessException("借方原币金额不可为空");
		 }
		 if(new UFDouble(body.getString("money_cr")).doubleValue()<0){
			 throw new BusinessException("借方原币金额不可小于0");
		 }
		 if (StringUtils.isBlank(body.getString("money_bal"))) {
		 throw new BusinessException("原币余额不可为空");
		 }
		 if(new UFDouble(body.getString("money_bal")).doubleValue()<0){
			 throw new BusinessException("原币余额不可小于0");
		 }
		 if (StringUtils.isBlank(body.getString("rowid"))) {
		 throw new BusinessException("物业收费系统单据行ID不可为空");
		 }
		 if (StringUtils.isBlank(body.getString("productline"))) {
		 throw new BusinessException("核销码不可为空");
		 }
//		if (StringUtils.isBlank(body.getString("costsubject"))) {
//			throw new BusinessException("成本科目不可为空");
//		}
//		if (StringUtils.isBlank(body.getString("invtype"))) {
//			throw new BusinessException("票据类型(票种)不可为空");
//		}
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
