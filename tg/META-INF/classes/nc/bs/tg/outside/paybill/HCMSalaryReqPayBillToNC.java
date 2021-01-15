package nc.bs.tg.outside.paybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.service.TimeService;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.ebs.utils.recbill.RecbillUtils;
import nc.bs.tg.outside.sale.utils.paybill.PayBillConvertor;
import nc.bs.tg.outside.sale.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.ui.cdm.innerpay.action.returnBackAction;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import vo.tg.outside.PayBillBodyVO;
import vo.tg.outside.PayBillHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class HCMSalaryReqPayBillToNC extends PayBillUtils implements
		ITGSyncService {
	public static final String HCMSalaryOperatorName = "RLZY";// HCM默认操作员
	private IArapBillPubQueryService arapBillPubQueryService = null;
	String rlzyUserid = null;// HCM系统操作用户
	static HCMSalaryReqPayBillToNC utils;

	public static HCMSalaryReqPayBillToNC getUtils() {
		if (utils == null) {
			utils = new HCMSalaryReqPayBillToNC();
		}
		return utils;
	}

	/**
	 * 付款单
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
		Logger.init("nc");
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
		Logger.debug("HCMSalaryReqPayBillToNC:开始转换");
		PayBillHeadVO headVO = JSONObject.parseObject(jsonhead,
				PayBillHeadVO.class);
		List<PayBillBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				PayBillBodyVO.class);
		Logger.debug("HCMSalaryReqPayBillToNC:结束转换");
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
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【" + billkey + "】,NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue("billno")
						+ "】,请勿重复上传!");
			}
			// AggPayBillVO billvo = (AggPayBillVO) convertor.castToBill(value,
			// AggPayBillVO.class, aggVO);
			Logger.debug("paybill开始转换");
			AggPayBillVO billvo = onTranBill(headVO, bodyVOs);
			Logger.debug("paybill结束转换");
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = (AggPayBillVO[]) getPfBusiAction().processAction(
					"SAVE", "F3", null, billvo, null, eParam);
			// obj = ipfBusiAction.processAction("APPROVE", billtype,
			// finalWorkflowVO == null ? null : finalWorkflowVO, objs[0],
			// userObj == null ? null : userObj, map != null ? map
			// : new HashMap());
			// IPFBusiAction ipfBusiAction = NCLocator.getInstance().lookup(
			// IPFBusiAction.class);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	protected AggPayBillVO onTranBill(PayBillHeadVO headVO,
			List<PayBillBodyVO> bodyVOs) throws BusinessException {
		
		AggPayBillVO aggvo = new AggPayBillVO();
		PayBillVO save_headVO = new PayBillVO();
		String pk_org = getPk_orgByCode(headVO.getPk_org());
		if (pk_org != null) {
			save_headVO.setPk_org(pk_org);// 财务组织
		} else {
			throw new BusinessException("HCM同步NC财务系统工资付款单：" + headVO.getSrcid()
					+ " 的财务组织在NC财务系统中不存在！财务组织：" + headVO.getPk_org());
		}
		save_headVO.setDef1(headVO.getSrcid());// 外系统主键
		save_headVO.setDef2(headVO.getSrcbillno());// 外系统单号
		save_headVO.setPk_balatype(getBalatypePkByCode(headVO.getPk_balatype()));// 结算方式
		save_headVO.setBilldate(new UFDate(headVO.getBilldate()));// 扣款日期
		save_headVO.setDef67(headVO.getDef67().substring(0, 7));// 工资所属月
		save_headVO.setDef15(headVO.getDef30());// HCM审批情况 NC存def15，json传def30

		List<Map<String, String>> budgetsubNames = getLinkCompany(pk_org);
		for (Map<String, String> names : budgetsubNames) {
			save_headVO.setDef56(names.get("factorvalue4"));// 是否资本化
			save_headVO.setDef57(names.get("factorvalue5"));// 公司性质
		}
		
		String pk_vid = getvidByorg(save_headVO
				.getPk_org());
		save_headVO.setPk_fiorg(save_headVO
				.getPk_org());
		save_headVO.setPk_fiorg_v(pk_vid);
		save_headVO.setSett_org((String) save_headVO
				.getPk_org());
		save_headVO.setSett_org_v(pk_vid);
		save_headVO.setCreationtime(new UFDateTime());
		save_headVO.setObjtype(1);
		save_headVO.setBillclass("fk");
		save_headVO.setApprovestatus(-1);
		save_headVO.setPk_tradetype("F3-Cxx-029");
		save_headVO.setPk_billtype("F3");// 单据类型编码
		save_headVO.setBilldate(new UFDate());// 单据日期
		save_headVO.setBusidate(new UFDate());//
		save_headVO.setSyscode(1);// 单据所属系统，默认为1，1=应付系统
		save_headVO.setSrc_syscode(1);// 单据来源系统
		save_headVO.setPk_currtype("1002Z0100000000001K1");// 币种
		save_headVO.setBillstatus(2);// 单据状态,默认为2审批中
		save_headVO.setPk_group("000112100000000005FD");// 所属集团，默认为时代集团
		save_headVO.setBillmaker(getUserIDByCode(HCMSalaryOperatorName));// 制单人
		save_headVO.setObjtype(3); // 往来对象
														// 2=部门，3=业务员，1=供应商，0=客户
		save_headVO.setCreator(getUserIDByCode(HCMSalaryOperatorName));// 创建人
		save_headVO.setIsinit(UFBoolean.FALSE);// 期初标志
		save_headVO.setIsreded(UFBoolean.FALSE);// 是否红冲过
		save_headVO.setStatus(VOStatus.NEW);

		List<PayBillItemVO> bodylist = new ArrayList<>();
		for (PayBillBodyVO payBillBodyVO : bodyVOs) {
			PayBillItemVO save_bodyVO = new PayBillItemVO();
			save_bodyVO.setPk_org(save_headVO.getPk_org());// 应收财务组织

			save_bodyVO.setDef18(getPk_orgByCode(payBillBodyVO.getDef18()));// 合同签订公司
			save_bodyVO.setDef30(getDefdocInfo(payBillBodyVO.getDef30(), "zdy046").get(
							"pk_defdoc"));// 部门属性
			if (getPsnInfo(payBillBodyVO.getPk_psndoc()) != null) {
				save_bodyVO.setPk_psndoc(getPsnInfo(payBillBodyVO.getPk_psndoc()).get(
								"pk_psndoc"));// 业务员
			} else {
				throw new BusinessException("业务员"
						+ payBillBodyVO.getPk_psndoc() + "在NC系统中不存在！");
			}
			if (getPersonalAccountIDByCode(payBillBodyVO.getRecaccount(),
					save_bodyVO.getPk_psndoc()) != null) {
				save_bodyVO.setRecaccount(getPersonalAccountIDByCode(payBillBodyVO
								.getRecaccount(), save_bodyVO
								.getPk_psndoc()));// 收款单位银行账户
			} else {
				throw new BusinessException("业务员"
						+ payBillBodyVO.getPk_psndoc() + "下没有"
						+ payBillBodyVO.getRecaccount() + "账号！");
			}
//			save_bodyVO.setDef22(payBillBodyVO.getDef22());// 人数22
//			save_bodyVO.setDef29(payBillBodyVO.getDef29());// 代发奖金-营销29
//			save_bodyVO.setDef27(payBillBodyVO.getDef27());// 代发奖金-拓展27
//			save_bodyVO.setDef25(payBillBodyVO.getDef25());// 代发奖金-其他25
//			save_bodyVO.setDef24(payBillBodyVO.getDef24());// 佣金24
//			save_bodyVO.setDef23(payBillBodyVO.getDef23());// 其他税前扣款-社保公司部分23
//			save_bodyVO.setDef20(payBillBodyVO.getDef20());// 其他税前扣款-社保个人部分20
//			save_bodyVO.setDef19(payBillBodyVO.getDef19());// 其他税前扣款-公积金公司部分19
//			save_bodyVO.setDef17(payBillBodyVO.getDef17());// 其他税前扣款-公积金个人部分17
//			save_bodyVO.setDef6(payBillBodyVO.getDef6());// 应发合计6
//			save_bodyVO.setDef31(payBillBodyVO.getDef31());// 应扣基金31
//			save_bodyVO.setDef32(payBillBodyVO.getDef32());// 慈善捐款32
//			
//			save_bodyVO.setDef33(payBillBodyVO.getDef33());// 其他税后扣款-其他33
//			save_bodyVO.setDef45(payBillBodyVO.getDef45());// 其他税后扣款-营销45
//			save_bodyVO.setDef46(payBillBodyVO.getDef46());// 其他税后扣款-社保公司部分46
//			save_bodyVO.setDef47(payBillBodyVO.getDef47());// 其他税后扣款-社保个人部分47
//			save_bodyVO.setDef21(payBillBodyVO.getDef21());// 其他税后扣款-公积金公司部分21
//			save_bodyVO.setDef15(payBillBodyVO.getDef15());// 其他税后扣款-公积金个人部分15
//			save_bodyVO.setDef14(payBillBodyVO.getDef14());// 其他税后扣款-个税14
//			save_bodyVO.setDef13(payBillBodyVO.getDef13());// 应扣个税13
//			save_bodyVO.setDef12(payBillBodyVO.getDef12());// 养老公司部分12
//			save_bodyVO.setDef11(payBillBodyVO.getDef11());// 养老个人部分11
//			
//			save_bodyVO.setDef59(payBillBodyVO.getDef59());// 基础医疗公司部分59
//			save_bodyVO.setDef60(payBillBodyVO.getDef60());// 基础医疗个人部分60
			save_bodyVO.setDef90(payBillBodyVO.getDef10());// 补充医疗公司部分10
			save_bodyVO.setDef89(payBillBodyVO.getDef9());// 补充医疗个人部分9
			save_bodyVO.setDef88(payBillBodyVO.getDef8());// 失业公司部分8
			save_bodyVO.setDef87(payBillBodyVO.getDef7());// 失业个人部分7
			save_bodyVO.setDef85(payBillBodyVO.getDef5());// 工伤公司部分5
			save_bodyVO.setDef84(payBillBodyVO.getDef4());// 工伤个人部分4
			save_bodyVO.setDef83(payBillBodyVO.getDef3());// 生育公司部分3
			save_bodyVO.setDef82(payBillBodyVO.getDef2());// 生育个人部分2
			save_bodyVO.setDef81(payBillBodyVO.getDef1());// 公积金公司部分1
			
//			save_bodyVO.setDef36(payBillBodyVO.getDef36());// 公积金个人部分36
//			save_bodyVO.setDef48(payBillBodyVO.getDef48());// 代发奖金扣款-营销48
//			save_bodyVO.setDef49(payBillBodyVO.getDef49());// 代发奖金扣款-拓展49
//			save_bodyVO.setDef50(payBillBodyVO.getDef50());// 代发奖金扣款-其他50
//			save_bodyVO.setDef53(payBillBodyVO.getDef53());// 重大疾病医疗补助-公司部分53
//			save_bodyVO.setDef54(payBillBodyVO.getDef54());// 重大疾病医疗补助-个人部分54
//			save_bodyVO.setDef55(payBillBodyVO.getDef55());// 医保个人账户-公司部分55
//			save_bodyVO.setDef56(payBillBodyVO.getDef56());// 医保个人账户-个人部分56
//			save_bodyVO.setDef57(payBillBodyVO.getDef57());// 其他-公司部分57
//			save_bodyVO.setDef58(payBillBodyVO.getDef58());// 其他-个人部分58

			
			save_bodyVO.setMoney_de(new UFDouble(payBillBodyVO.getMoney_de()));// 实发合计 原币
			save_bodyVO.setLocal_money_bal(new UFDouble(payBillBodyVO.getMoney_de()));// 本币=原币（币种一样）
			save_bodyVO.setLocal_money_de(new UFDouble(payBillBodyVO.getMoney_de()));// 余额
			save_bodyVO.setPausetransact(UFBoolean.FALSE);// 挂起标志
			save_bodyVO.setPk_balatype(save_headVO.getPk_balatype());// 结算方式
			save_bodyVO
					.setBilldate(save_headVO.getBilldate());// 单据日期
			save_bodyVO
					.setPk_group(save_headVO.getPk_group());// 所属集团
			save_bodyVO.setPk_billtype(
					save_headVO.getPk_billtype());// 单据类型编码
			save_bodyVO.setBillclass(save_headVO.getBillclass());// 单据大类
			save_bodyVO.setPk_tradetype(
					save_headVO.getPk_tradetype());// 应收类型code
			save_bodyVO.setPk_tradetypeid(save_headVO.getPk_tradetypeid());// 应收类型
			save_bodyVO.setBusidate(save_headVO.getBilldate());// 起算日期
			save_bodyVO.setObjtype(save_headVO.getObjtype());// 往来对象
																				// 2=部门，3=业务员，1=供应商，0=客户
			save_bodyVO.setDirection(1);// 方向
			save_bodyVO.setPk_currtype(
					save_headVO.getPk_currtype());// 币种
			save_bodyVO.setRate(new UFDouble(1));// 组织本币汇率
			save_bodyVO.setPk_deptid(save_headVO.getPk_deptid());// 部门
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new PayBillItemVO[0]));
//		getArapBillPubQueryService().getDefaultVO(aggvo, true);
		
		return aggvo;
	}

	protected AggPayBillVO onDefaultValue(JSONObject head, JSONArray bodylist)
			throws BusinessException {
		AggPayBillVO aggvo = new AggPayBillVO();
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
//		getArapBillPubQueryService().getDefaultVO(aggvo, true);

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
	 * 通过业务员编码带出业务员主键
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
		query.append("select factorvalue4,factorvalue5 from fip_docview_b where pk_classview=(select pk_classview from fip_docview ta where ta.viewcode ='XM02') and factorvalue2 = '"
				+ pk_org + "'");
		try {
			result = (List<Map<String, String>>) getBaseDAO().executeQuery(
					query.toString(), new MapListProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}
}
