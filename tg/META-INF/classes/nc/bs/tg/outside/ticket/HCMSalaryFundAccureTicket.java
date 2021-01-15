package nc.bs.tg.outside.ticket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.service.TimeService;
import nc.bs.tg.outside.sale.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.tg.salaryfundaccure.SalaryFundAccureItem;
import nc.vo.tg.salaryfundaccure.SalaryFundAccureVO;
import vo.tg.outside.SalaryFundAccureTicketBodyVO;
import vo.tg.outside.SalaryFundAccureTicketHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HCMSalaryFundAccureTicket extends PayBillUtils implements
		ITGSyncService {
	public static final String HCMSalaryOperatorName = "RLZY";// HCM默认操作员
	private IArapBillPubQueryService arapBillPubQueryService = null;
	static HCMSalaryFundAccureTicket utils;

	public static HCMSalaryFundAccureTicket getUtils() {
		if (utils == null) {
			utils = new HCMSalaryFundAccureTicket();
		}
		return utils;
	}

	/**
	 * 工资工单
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(HCMSalaryOperatorName));
		InvocationInfoProxy.getInstance().setUserCode(HCMSalaryOperatorName);

		// 处理表单信息
		JSONObject jsonData = (JSONObject) info.get("data");// 表单数据
		String jsonhead = jsonData.getString("headInfo");// 外系统来源表头数据
		String jsonbody = jsonData.getString("bodyInfo");// 外系统来源表体数据
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("表单数据为空，请检查！json:" + jsonData);
		}

		// 转换json
		SalaryFundAccureTicketHeadVO headVO = JSONObject.parseObject(jsonhead,
				SalaryFundAccureTicketHeadVO.class);
		List<SalaryFundAccureTicketBodyVO> bodyVOs = JSONObject.parseArray(
				jsonbody, SalaryFundAccureTicketBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("表单数据转换失败，请检查！json:" + jsonData);
		}

		String srcid = headVO.getSrcid();// 外系统业务单据ID
		String srcno = headVO.getSrcbillno();// 外系统业务单据单据号
		Map<String, String> resultInfo = new HashMap<String, String>();

		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggSalaryfundaccure aggVO = (AggSalaryfundaccure) getBillVO(
					AggSalaryfundaccure.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【" + billkey + "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue("billno")
						+ "】,请勿重复上传!");
			}
			// AggPayBillVO billvo = (AggPayBillVO) convertor.castToBill(value,
			// AggPayBillVO.class, aggVO);

			AggSalaryfundaccure billvo = onTranBill(headVO, bodyVOs);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = (AggSalaryfundaccure[]) getPfBusiAction()
					.processAction("SAVEBASE", "HCM1", null, billvo, null,
							eParam);
			
			AggSalaryfundaccure agg = (AggSalaryfundaccure) getBillVO(AggSalaryfundaccure.class,"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			
//			AggSalaryfundaccure[] billvos = (AggSalaryfundaccure[]) obj;
			
			resultInfo.put("billid", agg.getPrimaryKey());
			resultInfo.put("billno", (String) agg.getParentVO()
					.getAttributeValue("billno"));
			
			WorkflownoteVO worknoteVO = NCLocator.getInstance()
					.lookup(IWorkflowMachine.class)
					.checkWorkFlow("SAVE", "HCM1", agg, eParam);
			obj = (AggSalaryfundaccure[]) getPfBusiAction().processAction(
					"SAVE", "HCM1", worknoteVO,  agg, null, eParam);


		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected AggSalaryfundaccure onTranBill(
			SalaryFundAccureTicketHeadVO headVO,
			List<SalaryFundAccureTicketBodyVO> bodyVOs)
			throws BusinessException {
		AggSalaryfundaccure aggvo = new AggSalaryfundaccure();
		SalaryFundAccureVO save_headVO = new SalaryFundAccureVO();
		String pk_org = getPk_orgByCode(headVO.getPk_org());
		if (pk_org != null) {
			save_headVO.setAttributeValue("pk_org", pk_org);// 财务组织
		} else {
			throw new BusinessException("HCM同步NC财务系统工资付款单：" + headVO.getSrcid()
					+ " 的财务组织在NC财务系统中不存在！财务组织：" + headVO.getPk_org());
		}
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// 外系统主键
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// 外系统单号
		save_headVO.setAttributeValue("billdate", headVO.getBilldate());// 扣款日期
		save_headVO.setAttributeValue("def67", headVO.getDef67()
				.substring(0, 7));// 工资所属月
		save_headVO.setAttributeValue("def18", pk_org);// 合同签订公司

		List<Map<String, String>> budgetsubNames = getLinkCompany(pk_org);
		for (Map<String, String> names : budgetsubNames) {
			save_headVO.setAttributeValue("def5", names.get("factorvalue4"));// 是否资本化
			save_headVO.setAttributeValue("def4", names.get("factorvalue5"));// 公司性质
		}

		String pk_vid = getvidByorg((String) save_headVO
				.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("pk_fiorg",
				(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("pk_fiorg_v", pk_vid);
		save_headVO.setAttributeValue("sett_org",
				(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("sett_org_v", pk_vid);
		save_headVO.setAttributeValue("creationtime",
				save_headVO.getAttributeValue("billdate"));
		save_headVO.setAttributeValue("objtype", 1);
		save_headVO.setAttributeValue("approvestatus", -1);
		save_headVO.setAttributeValue("transtype", "HCM1-Cxx-001");
		save_headVO.setAttributeValue("pk_transtype", "1001ZZ1000000057SWRA");
		 save_headVO.setAttributeValue("billtype", "HCM1");// 单据类型编码
//		save_headVO.setAttributeValue("billdate", new UFDate().toString());// 单据日期
		save_headVO.setAttributeValue("busidate", new UFDate().toString());//
		// save_headVO.setAttributeValue("syscode", 1);// 单据所属系统，默认为1，1=应付系统
		// save_headVO.setAttributeValue("src_syscode", 1);// 单据来源系统
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
		save_headVO.setAttributeValue("billstatus", 2);// 单据状态,默认为2审批中
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团，默认为时代集团
		save_headVO.setAttributeValue("billmaker",
				getUserIDByCode(HCMSalaryOperatorName));// 制单人
		// save_headVO.setAttributeValue("objtype", 3); // 往来对象
		// // 2=部门，3=业务员，1=供应商，0=客户
		save_headVO.setAttributeValue("creator",
				getUserIDByCode(HCMSalaryOperatorName));// 创建人
		save_headVO.setAttributeValue("isinit", UFBoolean.FALSE);// 期初标志
		save_headVO.setAttributeValue("isreded", UFBoolean.FALSE);// 是否红冲过
		save_headVO.setStatus(VOStatus.NEW);

		List<SalaryFundAccureItem> bodylist = new ArrayList<>();
		for (SalaryFundAccureTicketBodyVO salaryFundAccureTicketBodyVO : bodyVOs) {
			SalaryFundAccureItem save_bodyVO = new SalaryFundAccureItem();
			save_bodyVO.setAttributeValue("pk_org",
					save_headVO.getAttributeValue("pk_org"));// 应收财务组织
			if (salaryFundAccureTicketBodyVO.getDef18() != null
					&& !("").equals(salaryFundAccureTicketBodyVO.getDef18())) {
				save_bodyVO
						.setAttributeValue("def18",
								getPk_orgByCode(salaryFundAccureTicketBodyVO
										.getDef18()));// 工资发放公司
			} else {
				throw new BusinessException("合同签订公司不允许为空！");
			}
			save_bodyVO.setAttributeValue(
					"def30",
					getDefdocInfo(salaryFundAccureTicketBodyVO.getDef30(),
							"zdy046").get("pk_defdoc"));// 部门属性
			save_bodyVO.setAttributeValue("def22",
					salaryFundAccureTicketBodyVO.getDef22());// 人数
			save_bodyVO.setAttributeValue("def29",
					salaryFundAccureTicketBodyVO.getDef29());// 代发奖金-营销
			save_bodyVO.setAttributeValue("def27",
					salaryFundAccureTicketBodyVO.getDef27());// 代发奖金-拓展
			save_bodyVO.setAttributeValue("def25",
					salaryFundAccureTicketBodyVO.getDef25());// 代发奖金-其他
			save_bodyVO.setAttributeValue("def24",
					salaryFundAccureTicketBodyVO.getDef24());// 佣金
			save_bodyVO.setAttributeValue("def23",
					salaryFundAccureTicketBodyVO.getDef23());// 其他税前扣款-社保公司部分
			save_bodyVO.setAttributeValue("def20",
					salaryFundAccureTicketBodyVO.getDef20());// 其他税前扣款-社保个人部分
			save_bodyVO.setAttributeValue("def19",
					salaryFundAccureTicketBodyVO.getDef19());// 其他税前扣款-公积金公司部分
			save_bodyVO.setAttributeValue("def17",
					salaryFundAccureTicketBodyVO.getDef17());// 其他税前扣款-公积金个人部分
			save_bodyVO.setAttributeValue("def6",
					salaryFundAccureTicketBodyVO.getDef6());// 应发合计
			save_bodyVO.setAttributeValue("def31",
					salaryFundAccureTicketBodyVO.getDef31());// 应扣基金
			save_bodyVO.setAttributeValue("def32",
					salaryFundAccureTicketBodyVO.getDef32());// 慈善捐款
			save_bodyVO.setAttributeValue("def33",
					salaryFundAccureTicketBodyVO.getDef33());// 其他税后扣款-其他
			save_bodyVO.setAttributeValue("def45",
					salaryFundAccureTicketBodyVO.getDef45());// 其他税后扣款-营销
			save_bodyVO.setAttributeValue("def46",
					salaryFundAccureTicketBodyVO.getDef46());// 其他税后扣款-社保公司部分
			save_bodyVO.setAttributeValue("def47",
					salaryFundAccureTicketBodyVO.getDef47());// 其他税后扣款-社保个人部分
			save_bodyVO.setAttributeValue("def21",
					salaryFundAccureTicketBodyVO.getDef21());// 其他税后扣款-公积金公司部分
			save_bodyVO.setAttributeValue("def15",
					salaryFundAccureTicketBodyVO.getDef15());// 其他税后扣款-公积金个人部分
			save_bodyVO.setAttributeValue("def14",
					salaryFundAccureTicketBodyVO.getDef14());// 其他税后扣款-个税
			save_bodyVO.setAttributeValue("def13",
					salaryFundAccureTicketBodyVO.getDef13());// 应扣个税
			save_bodyVO.setAttributeValue("def12",
					salaryFundAccureTicketBodyVO.getDef12());// 养老公司部分
			save_bodyVO.setAttributeValue("def11",
					salaryFundAccureTicketBodyVO.getDef11());// 养老个人部分
			save_bodyVO.setAttributeValue("def10",
					salaryFundAccureTicketBodyVO.getDef10());// 补充医疗公司部分
			save_bodyVO.setAttributeValue("def9",
					salaryFundAccureTicketBodyVO.getDef9());// 补充医疗个人部分
			save_bodyVO.setAttributeValue("def8",
					salaryFundAccureTicketBodyVO.getDef8());// 失业公司部分
			save_bodyVO.setAttributeValue("def7",
					salaryFundAccureTicketBodyVO.getDef7());// 失业个人部分
			save_bodyVO.setAttributeValue("def5",
					salaryFundAccureTicketBodyVO.getDef5());// 工伤公司部分
			save_bodyVO.setAttributeValue("def4",
					salaryFundAccureTicketBodyVO.getDef4());// 工伤个人部分
			save_bodyVO.setAttributeValue("def3",
					salaryFundAccureTicketBodyVO.getDef3());// 生育公司部分
			save_bodyVO.setAttributeValue("def2",
					salaryFundAccureTicketBodyVO.getDef2());// 生育个人部分
			save_bodyVO.setAttributeValue("def1",
					salaryFundAccureTicketBodyVO.getDef1());// 公积金公司部分
			save_bodyVO.setAttributeValue("def36",
					salaryFundAccureTicketBodyVO.getDef36());// 公积金个人部分
			save_bodyVO.setAttributeValue("def48",
					salaryFundAccureTicketBodyVO.getDef48());// 代发奖金扣款-营销
			save_bodyVO.setAttributeValue("def49",
					salaryFundAccureTicketBodyVO.getDef49());// 代发奖金扣款-拓展
			save_bodyVO.setAttributeValue("def50",
					salaryFundAccureTicketBodyVO.getDef50());// 代发奖金扣款-其他
			save_bodyVO.setAttributeValue("def53",
					salaryFundAccureTicketBodyVO.getDef53());// 重大疾病医疗补助-公司部分
			save_bodyVO.setAttributeValue("def54",
					salaryFundAccureTicketBodyVO.getDef54());// 重大疾病医疗补助-个人部分
			save_bodyVO.setAttributeValue("def55",
					salaryFundAccureTicketBodyVO.getDef55());// 医保个人账户-公司部分
			save_bodyVO.setAttributeValue("def56",
					salaryFundAccureTicketBodyVO.getDef56());// 医保个人账户-个人部分
			save_bodyVO.setAttributeValue("def57",
					salaryFundAccureTicketBodyVO.getDef57());// 其他-公司部分
			save_bodyVO.setAttributeValue("def58",
					salaryFundAccureTicketBodyVO.getDef58());// 其他-个人部分
			save_bodyVO.setStatus(VOStatus.NEW);
			save_bodyVO.setAttributeValue("money_de",
					salaryFundAccureTicketBodyVO.getMoney_de());// 实发合计 原币
			save_bodyVO.setAttributeValue("local_money_bal",
					salaryFundAccureTicketBodyVO.getMoney_de());// 本币=原币（币种一样）
			save_bodyVO.setAttributeValue("local_money_de",
					salaryFundAccureTicketBodyVO.getMoney_de());// 余额
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// 挂起标志
			save_bodyVO.setAttributeValue("billdate",
					save_headVO.getAttributeValue("billdate"));// 单据日期
			save_bodyVO.setAttributeValue("pk_group",
					save_headVO.getAttributeValue("pk_group"));// 所属集团
			save_bodyVO.setAttributeValue("pk_billtype",
					save_headVO.getAttributeValue("pk_billtype"));// 单据类型编码
			save_bodyVO.setAttributeValue("billclass",
					save_headVO.getAttributeValue("billclass"));// 单据大类
			save_bodyVO.setAttributeValue("pk_tradetype",
					save_headVO.getAttributeValue("pk_tradetype"));// 应收类型code
			save_bodyVO.setAttributeValue("pk_tradetypeid",
					save_headVO.getAttributeValue("pk_tradetypeid"));// 应收类型
			save_bodyVO.setAttributeValue("busidate",
					save_headVO.getAttributeValue("busidate"));// 起算日期
			save_bodyVO.setAttributeValue("objtype",
					save_headVO.getAttributeValue("objtype"));// 往来对象
			// 2=部门，3=业务员，1=供应商，0=客户
			save_bodyVO.setAttributeValue("direction", 1);// 方向
			save_bodyVO.setAttributeValue("pk_currtype",
					save_headVO.getAttributeValue("pk_currtype"));// 币种
			save_bodyVO.setAttributeValue("rate", 1);// 组织本币汇率
			save_bodyVO.setAttributeValue("pk_deptid",
					save_headVO.getAttributeValue("pk_deptid"));// 部门
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new SalaryFundAccureItem[0]));
		// getArapBillPubQueryService().getDefaultVO(aggvo, true);
		return aggvo;
	}

	protected AggSalaryfundaccure onDefaultValue(JSONObject head,
			JSONArray bodylist) throws BusinessException {
		AggSalaryfundaccure aggvo = new AggSalaryfundaccure();
		PayBillVO hvo = new PayBillVO();
		String billdate = head.getString("billdate") == null ? new UFDate()
				.toString() : head.getString("billdate");
		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// 当前时间
		hvo.setAttributeValue(PayBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// 集团
		hvo.setAttributeValue(PayBillVO.PK_ORG, DocInfoQryUtils.getUtils()
				.getOrgInfo(head.getString("org")).get("pk_org"));// 应付财务组织->NC业务单元编码
		hvo.setAttributeValue(PayBillVO.BILLMAKER,
				getUserIDByCode(HCMSalaryOperatorName));// 制单人
		hvo.setAttributeValue(PayBillVO.CREATOR, hvo.getBillmaker());// 创建人
		hvo.setAttributeValue(PayBillVO.CREATIONTIME, currTime);// 创建时间
		hvo.setAttributeValue(PayBillVO.PK_BILLTYPE, IBillFieldGet.F3);// 单据类型编码
		hvo.setAttributeValue(PayBillVO.BILLCLASS, IBillFieldGet.FK);// 单据大类
		hvo.setAttributeValue(PayBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据所属系统
		hvo.setAttributeValue(PayBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// 单据来源系统
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE, IBillFieldGet.D1);// 应收类型code
		hvo.setAttributeValue(PayBillVO.BILLSTATUS, ARAPBillStatus.SAVE.VALUE);// 单据状态
		hvo.setAttributeValue(PayBillVO.PK_TRADETYPE, "");// 交易类型

		// BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetype());
		// hvo.setAttributeValue(PayBillVO.PK_TRADETYPEID,
		// billTypeVo.getPk_billtypeid());// 应收类型
		// hvo.setAttributeValue(PayBillVO.PK_TRADETYPE,
		// billTypeVo.getPk_billtypecode());// 应收类型
		hvo.setAttributeValue(PayBillVO.ISINIT, UFBoolean.FALSE);// 期初标志
		hvo.setAttributeValue(PayBillVO.ISREDED, UFBoolean.FALSE);// 是否红冲过

		hvo.setAttributeValue(PayBillVO.BILLDATE, new UFDate(billdate));// 单据日期
		hvo.setAttributeValue(PayBillVO.BUSIDATE, new UFDate(billdate));// 起算日期
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
		// getArapBillPubQueryService().getDefaultVO(aggvo, true);

		return aggvo;
	}

	public IArapBillPubQueryService getArapBillPubQueryService() {
		if (arapBillPubQueryService == null) {
			arapBillPubQueryService = NCLocator.getInstance().lookup(
					IArapBillPubQueryService.class);
		}
		return arapBillPubQueryService;
	}

	/**
	 * 主体信息
	 * 
	 * @param hvo
	 * @param headvo
	 */
	protected void setHeaderVO(PayBillVO hvo, JSONObject head)
			throws BusinessException {
	};

	/**
	 * 自定义档案
	 * 
	 * @param key
	 * @param pk_project
	 * @param
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDefdocInfo(String key, String listcode)
			throws BusinessException {
		// listcode = "zdy046";
		return DocInfoQryUtils.getUtils().getDefdocInfo(key, listcode);
	}

	/**
	 * 业务员信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getPsnInfo(String code) throws BusinessException {
		return DocInfoQryUtils.getUtils().getPsnInfo(code);
	}

	/**
	 * 根据【用户编码】获取主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getUserIDByCode(String code) throws BusinessException {
		String sql = "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
				+ code + "'";
		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}

	// /**
	// * 根据【用户编码】获取主键
	// *
	// * @param code
	// * @return
	// */
	// public String getUserPkByCode(String code) {
	// String sql =
	// "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
	// + code + "'";
	// String result = null;
	// try {
	// result = (String) getBaseDAO().executeQuery(sql,
	// new ColumnProcessor());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	/**
	 * 根据编码或名称找客商
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getcustomer(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select pk_customer from bd_customer where (code='" + code
						+ "' or name='" + code + "') and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}

	/**
	 * 根据【公司编码】获取财务组织
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	/**
	 * 根据【公司编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_orgByCode(String code) {
		String sql = "select pk_org from org_orgs where (code='" + code
				+ "' or name = '" + code + "') and dr=0 and enablestate=2 ";
		String pk_org = null;
		try {
			pk_org = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 结算方式查询
	 * 
	 * @param balatype
	 * @return
	 */
	protected String getBalatypePkByCode(String balatype) {
		String result = null;
		String sql = "select  pk_balatype from bd_balatype where dr = 0 and code ='"
				+ balatype + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 通过业务员编码带出业务员主键 2020-07-02 谈子健
	 * 
	 * @throws BusinessException
	 */
	public String getPk_psndocByCode(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select c.pk_psndoc from bd_psndoc c where c.code = '"
				+ code + "' and nvl(c.dr,0) = 0 and c.enablestate = 2  ");
		String value = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return value;
	}

	/**
	 * 根据个人银行账户编码读取对应主键 2020-07-03-谈子健
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getPersonalAccountIDByCode(String recaccount,
			String pk_receiver) throws BusinessException {
		String result = null;
		StringBuffer query = new StringBuffer();
		query.append("SELECT bd_psnbankacc.pk_bankaccsub AS pk_bankaccsub  ");
		query.append("  FROM bd_bankaccbas, bd_bankaccsub, bd_psnbankacc  ");
		query.append(" WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas  ");
		query.append("   AND bd_bankaccsub.pk_bankaccsub = bd_psnbankacc.pk_bankaccsub  ");
		query.append("   AND bd_bankaccsub.pk_bankaccbas = bd_psnbankacc.pk_bankaccbas  ");
		query.append("   and bd_psnbankacc.pk_bankaccsub != '~'  ");
		query.append("   AND bd_bankaccsub.Accnum = '" + recaccount + "'  ");
		query.append("   AND exists (select 1  ");
		query.append("          from bd_bankaccbas bas  ");
		query.append("         where bas.pk_bankaccbas = bd_psnbankacc.pk_bankaccbas  ");
		query.append("           and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y'))  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and (pk_psndoc = '" + pk_receiver + "' and  ");
		query.append("       pk_psnbankacc IN  ");
		query.append("       (SELECT min(pk_psnbankacc)  ");
		query.append("           FROM bd_psnbankacc  ");
		query.append("          GROUP BY pk_bankaccsub, pk_psndoc))  ");
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	// 获取是否资本化和公司性质
	protected List<Map<String, String>> getLinkCompany(String pk_org)
			throws BusinessException {
		List<Map<String, String>> result = null;
		StringBuffer query = new StringBuffer();
		query.append("select factorvalue4,factorvalue5 from fip_docview_b where pk_classview=(select pk_classview from fip_docview ta where ta.viewcode ='XM01') and factorvalue2 = '"
				+ pk_org + "'");
		try {
			result = (List<Map<String, String>>) getBaseDAO().executeQuery(
					query.toString(), new MapListProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}
	
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
}
