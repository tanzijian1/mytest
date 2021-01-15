package nc.bs.tg.outside.ebs.utils.paybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.apply.ApplyBillBodyVO;
import nc.vo.tg.apply.ApplyBillHeadVO;
import nc.vo.tg.apply.CostApplyBillBodyVO;
import nc.vo.tg.apply.CostApplyBillHeadVO;
import nc.vo.tg.apply.InsideApplyBillBodyVO;
import nc.vo.tg.apply.InsideApplyBillHeadVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.paymentrequest.Business_b;
import nc.vo.tgfn.paymentrequest.Payrequest;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 付单申请->付款单保存类
 * 
 * @author ASUS
 * 
 */
public class PayBillUtils extends EBSBillUtils {
	static PayBillUtils utils;

	public static PayBillUtils getUtils() {
		if (utils == null) {
			utils = new PayBillUtils();
		}
		return utils;
	}

	/**
	 * 付单申请->付款单保存类
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		// 处理表单信息
		JSONObject jsonData = (JSONObject) value.get("data");
		String jsonhead = jsonData.getString("applyHeadVO");
		String jsonbody = jsonData.getString("applyBodyVOs");
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("表单数据为空，请检查！json:" + jsonData);
		}
		AggPayrequest[] aggvo = null;
		String srcid = JSONObject.parseObject(jsonhead).getString("ebsid");// EBS系统业务单据ID
		String srcno = JSONObject.parseObject(jsonhead)
				.getString("ebsbillcode");// EBS系统业务单据单据号

		/**
		 * 成本nc接收ebs信息时，包括生成应付单与付款申请单，需先校验应先有合同 2020-03-10-谈子健 -start
		 * 通用的付款申请单也要加判断
		 */
		// if (/*EBSCont.SRCBILL_15.equals(srctype)||*/
		// EBSCont.SRCBILL_04.equals(srctype)) {
		// String contractcode = JSONObject.parseObject(jsonhead).getString(
		// "contractcode");// 合同编码
		// if (contractcode == null || "".equals(contractcode)) {
		// throw new BusinessException("合同编码不能为空!");
		// }
		// String contractname = JSONObject.parseObject(jsonhead).getString(
		// "contractname");// 合同名称
		// if (contractname == null || "".equals(contractname)) {
		// throw new BusinessException("合同名称不能为空!");
		// }
		// InspectionContract(contractcode, contractname);
		// }

		/**
		 * nc接收ebs信息时，包括生成应付单与付款申请单，需先校验应先有合同 2020-03-10-谈子健 -end
		 */
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcid;
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		// TODO ebsid 按实际存入信息位置进行变更
		AggPayrequest aggVO = (AggPayrequest) getBillVO(AggPayrequest.class,
				"isnull(dr,0)=0 and def1 = '" + srcid + "'");
		if (aggVO != null) {
			throw new BusinessException("【"
					+ billkey
					+ "】,NC已存在对应的业务单据【"
					+ aggVO.getParentVO().getAttributeValue(
							PayableBillVO.BILLNO) + "】,请勿重复上传!");
		}
		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {
			AggPayrequest billvo = onTranBill(jsonhead, jsonbody, srctype);
			// TODO20200825-添加合同不能为空校验
			// 2020年9月1日 -添加款项类型为投标保证金时去掉合同不能为空校验
			if (!"投标保证金".equals(billvo.getChildrenVO()[0]
					.getAttributeValue("def2"))) {
				BaseDAO dao = new BaseDAO();
				String ifCONTRACT = OutsideUtils.getOutsideInfo("ifCONTRACT");
				if (!("Y".equals(ifCONTRACT))) {
					String def5 = billvo.getParentVO().getDef5();// 合同编码，表头def5合同名称，表头def6
					String def6 = billvo.getParentVO().getDef6();
					if (def5 == null || def5.length() < 1 || def6 == null
							|| def6.length() < 1)
						throw new BusinessException("合同编码或合同名称不能为空");
					if (def5 != null && def6 != null) {
						String pk = (String) dao.executeQuery(
								"select pk_fct_ap from fct_ap f where (f.ctname='"
										+ def6 + "' or f.vbillcode='" + def5
										+ "')", new ColumnProcessor());
						if (pk == null || pk.length() < 1) {
							throw new BusinessException("合同编码或合同名称在NC系统不存在");
						}
					}
				}
			}
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			aggvo = (AggPayrequest[]) getPfBusiAction().processAction(
					"SAVEBASE", "FN01", null, billvo, null, eParam);

			getPfBusiAction().processAction("SAVE", "FN01", null, aggvo[0],
					null, null);
			/*
			 * AggPayrequest[] avo = (AggPayrequest[]) getPfBusiAction()
			 * .processAction("APPROVE", "FN01", null, svo[0], null, eParam);
			 * PushPayApplyUtils.getUtils().pushPayApplyBill(avo[0]);
			 */

		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("billid", aggvo[0].getPrimaryKey());
		dataMap.put("billno", (String) aggvo[0].getParentVO()
				.getAttributeValue("billno"));
		return JSON.toJSONString(dataMap);

	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 */
	private AggPayrequest onTranBill(String jsonhead, String jsonbody,
			String srctype) throws BusinessException {

		AggPayrequest aggvo = new AggPayrequest();
		if ("10".equals(srctype) || "15".equals(srctype)) {
			transforCost(jsonhead, jsonbody, aggvo, srctype);
		} else if ("27".equals(srctype)) {
			transforInside(jsonhead, jsonbody, aggvo, srctype);
		} else {
			transforexpense(jsonhead, jsonbody, aggvo, srctype);

		}

		return aggvo;
	}

	/**
	 * 内部交易
	 * 
	 * @param jsonhead
	 * @param jsonbody
	 * @param aggvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void transforInside(String jsonhead, String jsonbody,
			AggPayrequest aggvo, String srctype) throws BusinessException {
		// 转换json
		InsideApplyBillHeadVO headvo = JSONObject.parseObject(jsonhead,
				InsideApplyBillHeadVO.class);
		List<InsideApplyBillBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				InsideApplyBillBodyVO.class);
		checkHeadTransforInside(headvo, srctype);// 空值检查
		if (headvo == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("表单数据转换失败，请检查");
		}
		Payrequest save_headVO = new Payrequest();
		save_headVO.setAttributeValue("pk_payreq", null);// 单据主键
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 集团
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			save_headVO.setAttributeValue("pk_org",
					getPk_orgByCode(headvo.getOrg()));// 财务组织
		} else {
			throw new BusinessException("该财务组织编码：未能在NC关联，请检查");
		}// 财务组织
		save_headVO.setAttributeValue("pk_org_v", null);// 组织版本
		save_headVO.setAttributeValue("creator", null);// 创建人
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// 创建时间
		save_headVO.setAttributeValue("modifier", null);// 修改人
		save_headVO.setAttributeValue("modifiedtime", null);// 修改时间
		save_headVO.setAttributeValue("billno", null);// 单据号
		save_headVO.setAttributeValue("pkorg", save_headVO.getPk_org());// 所属组织
		save_headVO.setAttributeValue("busitype", null);// 业务类型
		save_headVO.setAttributeValue("billmaker", null);// 制单人
		save_headVO.setAttributeValue("approver", null);// 审批人
		save_headVO.setAttributeValue("approvenote", null);// 审批批语
		save_headVO.setAttributeValue("approvedate", null);// 审批时间
		save_headVO.setAttributeValue("recaccount", null);

		String pk_transtype = gettradeTypeByCode("FN01-Cxx-003");
		save_headVO.setAttributeValue("transtype", "FN01-Cxx-003");// 交易类型
		save_headVO.setAttributeValue("billtype", "FN01");// 单据类型
		save_headVO.setAttributeValue("transtypepk", pk_transtype);// 交易类型pk
		save_headVO.setAttributeValue("emendenum", null);// 修订枚举
		save_headVO.setAttributeValue("billversionpk", null);// 单据版本pk
		save_headVO.setAttributeValue("billdate", new UFDate());// 单据日期
		save_headVO.setAttributeValue("pk_tradetypeid", null);// 付款类型
		save_headVO.setAttributeValue("def1", headvo.getEbsid());// EBS主键
		save_headVO.setAttributeValue("def2", headvo.getEbsbillcode());// EBS申请编号
		save_headVO.setAttributeValue("def3", headvo.getImagecode());// 影像编码
		save_headVO.setAttributeValue("def4", headvo.getImagestatus());// 影像状态
		save_headVO.setAttributeValue("def5", headvo.getPurchasecode());// 采购协议编号
		save_headVO.setAttributeValue("def6", headvo.getPurchasename());// 采购协议名称
		save_headVO.setAttributeValue("def7", null);// 自定义项7
		save_headVO.setAttributeValue("def8", null);// 自定义项8
		if (StringUtils.isNotBlank(getdefdocBycode(headvo.getEmergency(),
				"zdy031"))) {
			save_headVO.setAttributeValue("def9",
					getdefdocBycode(headvo.getEmergency(), "zdy031"));// 紧急状态
		} else {
			throw new BusinessException("该紧急状态编码：" + headvo.getEmergency()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("def10", headvo.getSrcname());// 外系统名称
		save_headVO.setAttributeValue("def11", null);
		save_headVO.setAttributeValue("def12", null);
		save_headVO.setAttributeValue("objtype", 1);// 往来对象
		save_headVO.setAttributeValue("pk_deptid_v", null);// 经办部门
		save_headVO.setAttributeValue("pk_psndoc", null);// 经办人
		String pk_balatype = null;
		// if ("现金".equals(headvo.getBalatype())) {
		// pk_balatype = getBalatypePkByCode("10");
		// }
		// if ("保理".equals(headvo.getBalatype())) {
		// pk_balatype = getBalatypePkByCode("13");
		// }
		// if ("期票".equals(headvo.getBalatype())) {
		// pk_balatype = getBalatypePkByCode("7");
		// }
		pk_balatype = getBalatypePkByCode(headvo.getBalatype());
		if (pk_balatype == null) {
			throw new BusinessException("结算方式【" + headvo.getBalatype()
					+ "】未能在NC档案查询到相关信息!");
		}
		save_headVO.setAttributeValue(PayableBillVO.PK_BALATYPE, pk_balatype);// 结算方式

		// 当结算方式为保理时必传保理类型和资产编码-2020-06-09-谈子健-start
		if ("9".equals(headvo.getBalatype())) {
			save_headVO.setAttributeValue("def61", headvo.getDef61());//
			save_headVO.setAttributeValue("def62", headvo.getDef62());// 资产编码
		}
		// 当结算方式为保理时必传保理类型和资产编码-2020-06-09-谈子健-end

		save_headVO.setAttributeValue("payaccount", null);// 付款银行账户
		save_headVO.setAttributeValue("def13", null);
		save_headVO.setAttributeValue("cashaccount", null);// 现金账户
		save_headVO.setAttributeValue("money", new UFDouble(headvo.getMoney()));// 申请总金额
		save_headVO.setAttributeValue("def14", null);
		save_headVO.setAttributeValue("def15", null);
		save_headVO.setAttributeValue("def16", null);
		save_headVO.setAttributeValue("def17", null);
		save_headVO.setAttributeValue("def18", headvo.getContractsign());// 合同签约方
		if (StringUtils.isNotBlank(getSupplierIDByCodeOrName(headvo
				.getContractsign()))) {
			save_headVO.setAttributeValue("def18",
					getSupplierIDByCodeOrName(headvo.getContractsign()));// 合同签约方(供应商)
		} else {
			throw new BusinessException("该合同签约方：" + headvo.getContractsign()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
		save_headVO.setAttributeValue("def19", null);
		save_headVO.setAttributeValue("def20", null);
		save_headVO.setAttributeValue("def21", null);
		save_headVO.setAttributeValue("def22", null);
		save_headVO.setAttributeValue("def23", null);
		save_headVO.setAttributeValue("def24", null);
		save_headVO.setAttributeValue("def25", null);
		save_headVO.setAttributeValue("def26", null);
		save_headVO.setAttributeValue("def27", null);// 是否作废
		save_headVO.setAttributeValue("def28", null);
		save_headVO.setAttributeValue("billstatus", null);// 单据状态
		save_headVO.setAttributeValue("approvestatus", -1);// 审批状态
		save_headVO.setAttributeValue("effectstatus", 0);// 生效状态
		save_headVO.setAttributeValue("def29", null);//
		save_headVO.setAttributeValue("def30", headvo.getBondratio());//
		save_headVO.setAttributeValue("def31", headvo.getExplain());
		save_headVO.setAttributeValue("def32", "100112100000000071JH");// 默认项目为全项目
		// TODO ADD BY 20200227 HDQ 根据code 获取地区财务审批状态 -START-
		if (StringUtils.isNotBlank(headvo.getFinstatus())) {
			String def33 = getAuditstateByCode(headvo.getFinstatus());
			if (def33 == null) {
				throw new BusinessException("地区财务审批状态【" + headvo.getFinstatus()
						+ "】未能在NC档案关联到相关信息!");
			}
			save_headVO.setAttributeValue("def33", def33);// 地区财务审批状态
		}

		// TODO ADD BY 20200227 HDQ 根据code 获取地区财务审批状态 -END-

		save_headVO.setAttributeValue("def34", null);
		save_headVO.setAttributeValue("def35", headvo.getDeptname());// 经办部门
		save_headVO.setAttributeValue("def36", headvo.getOperatorname());// 经办人
		save_headVO.setAttributeValue("def37", headvo.getAbsmny());// ABS登记金额
		save_headVO.setAttributeValue("def38", null);// 自定义项38
		save_headVO.setAttributeValue("def39", null);
		save_headVO.setAttributeValue("def40", null);// 自定义项40
		save_headVO.setAttributeValue("def41", headvo.getBondcode());// 收保证金编号
		save_headVO.setAttributeValue("def42", headvo.getBondtype());// 保证金类型
		save_headVO.setAttributeValue("def43", headvo.getCompleted());// 自定义项43
		save_headVO.setAttributeValue("def44", headvo.getRepairannex());// 自定义项44
		save_headVO.setAttributeValue("def45", headvo.getPaymentletter());// 自定义项45
		save_headVO.setAttributeValue("def46", null);// 自定义项46
		save_headVO.setAttributeValue("def47", headvo.getArrivalbuild());// 自定义项47
		save_headVO.setAttributeValue("def48", null);// 自定义项48
		save_headVO.setAttributeValue("def49", headvo.getMoney());// 本次请款未付金额
		save_headVO.setAttributeValue("def50", null);// 自定义项50
		// 请款类型，def52，paymentType
		// save_headVO.setAttributeValue("def52", headvo.getPaymentType());//
		// 请款类型
		// save_headVO.setAttributeValue("def53", headvo.getTaskID());// BPM流程ID
		save_headVO.setAttributeValue("def55", headvo.getAbsratio());// ABS实付比例
		// bpmid
		save_headVO.setAttributeValue("bpmid", headvo.getBpmid());

		save_headVO.setStatus(VOStatus.NEW);

		List<Business_b> bodylist = new ArrayList<>();
		for (InsideApplyBillBodyVO applyBillBodyVO : bodyVOs) {
			Business_b save_bodyVO = new Business_b();
			checkBodyTransforInside(applyBillBodyVO, srctype);// 空值检查
			save_bodyVO.setAttributeValue("pk_business_b", null);// 业务页签主键
			save_bodyVO
					.setAttributeValue("scomment", applyBillBodyVO.getMemo());// 摘要
			save_bodyVO
					.setAttributeValue("def1", applyBillBodyVO.getNotetype());// 票据类型
			save_bodyVO.setAttributeValue("pk_subjcode", null);// 收支项目
			save_bodyVO.setAttributeValue("def2",
					applyBillBodyVO.getProceedtype());// 款项类型
			save_bodyVO.setAttributeValue("def3", applyBillBodyVO.getTaxrate());// 税率
			save_bodyVO.setAttributeValue("def4",
					applyBillBodyVO.getTaxamount());// 税额
			save_bodyVO.setAttributeValue("objtype", 1);// 往来对象
			if (StringUtils
					.isNotBlank(getSupplierIDByCodeOrName(applyBillBodyVO
							.getSupplier()))) {
				save_bodyVO
						.setAttributeValue("supplier",
								getSupplierIDByCodeOrName(applyBillBodyVO
										.getSupplier()));// 实际收款方(供应商)
			} else {
				throw new BusinessException("该供应商："
						+ applyBillBodyVO.getSupplier() + "未能在NC关联，请检查");
			}
			save_bodyVO.setAttributeValue("def5", applyBillBodyVO.getTotal());// 价税合计
			save_bodyVO.setAttributeValue("def6", null);// 自定义项6
			save_bodyVO.setAttributeValue("def7", null);// 自定义项7
			save_bodyVO.setAttributeValue("local_money_de",
					applyBillBodyVO.getPayamount());// 付款金额

			if (!"".equals(applyBillBodyVO.getRecaccount())
					&& applyBillBodyVO.getRecaccount() != null) {
				String recaccount = getAccountIDByCode(
						applyBillBodyVO.getRecaccount(),
						save_bodyVO.getSupplier());

				if ("".equals(recaccount) || recaccount == null) {
					throw new BusinessException("该供应商下没有该银行账号子户");
				}

				save_bodyVO.setAttributeValue("def15", recaccount);// 收款银行账户

				// 省份城市
				save_bodyVO.setAttributeValue("def43",
						getCityByrecaccount(recaccount).get("province"));
				save_bodyVO.setAttributeValue("def44",
						getCityByrecaccount(recaccount).get("city"));
			}

			save_bodyVO.setAttributeValue("def8", null);// 自定义项8
			save_bodyVO.setAttributeValue("def9", null);// 自定义项9
			save_bodyVO.setAttributeValue("def10", null);// 自定义项10
			save_bodyVO.setAttributeValue("def11", null);// 自定义项11
			save_bodyVO.setAttributeValue("def12", null);// 自定义项12
			save_bodyVO.setAttributeValue("def13", null);// 自定义项13
			save_bodyVO.setAttributeValue("def14", null);// 自定义项14
			save_bodyVO.setAttributeValue("def16", null);// 自定义项16
			if (!"".equals(applyBillBodyVO.getCompany())
					&& applyBillBodyVO.getCompany() != null) {
				if (getPk_orgByCode(applyBillBodyVO.getCompany()) == null) {
					throw new BusinessException("该抵楼所属公司："
							+ applyBillBodyVO.getCompany() + "未能在NC关联，请检查");
				}
			}
			save_bodyVO.setAttributeValue("def17",
					getPk_orgByCode(applyBillBodyVO.getCompany()));// 抵楼所属公司
			save_bodyVO.setAttributeValue("def18",
					applyBillBodyVO.getBidnumber());// 标书编号
			save_bodyVO.setAttributeValue("def19",
					applyBillBodyVO.getBidtitle());// 招标名称
			save_bodyVO.setAttributeValue("def20", null);// 自定义项20
			save_bodyVO.setAttributeValue("def21", null);// 自定义项21
			save_bodyVO.setAttributeValue("def22", null);// 自定义项22
			save_bodyVO.setAttributeValue("def23", null);// 自定义项23
			save_bodyVO.setAttributeValue("def24", null);// 自定义项24
			save_bodyVO.setAttributeValue("def25", null);// 自定义项25
			save_bodyVO.setAttributeValue("def26", null);// 自定义项26
			save_bodyVO.setAttributeValue("def27", null);// 自定义项27
			save_bodyVO.setAttributeValue("def28", null);// 自定义项28
			save_bodyVO.setAttributeValue("def29", null);// 自定义项29
			save_bodyVO.setAttributeValue("def30", null);// 自定义项30
			save_bodyVO.setAttributeValue("def31", null);// 自定义项31
			save_bodyVO.setAttributeValue("def32", null);// 自定义项32
			save_bodyVO.setAttributeValue("def33", null);// 自定义项33
			save_bodyVO.setAttributeValue("def34", null);// 自定义项34
			save_bodyVO.setAttributeValue("def35", null);// 自定义项35
			save_bodyVO.setAttributeValue("def36", null);// 自定义项36
			save_bodyVO.setAttributeValue("def37", null);// 自定义项37
			save_bodyVO.setAttributeValue("def38", null);// 自定义项38
			save_bodyVO.setAttributeValue("def39",
					applyBillBodyVO.getReaccountno());// 收款方开户行行号
			save_bodyVO.setAttributeValue("def40", null);// 自定义项40
			save_bodyVO.setAttributeValue("def41", null);// 自定义项41
			save_bodyVO.setAttributeValue("def42", null);// 自定义项42
			save_bodyVO.setAttributeValue("def45", null);// 自定义项45
			save_bodyVO.setAttributeValue("def46", null);// 自定义项46
			save_bodyVO.setAttributeValue("def47",
					applyBillBodyVO.getPayamount());// 未付金额
			save_bodyVO.setAttributeValue("def48", null);// 自定义项48
			save_bodyVO.setAttributeValue("def49", null);// 自定义项49
			save_bodyVO.setAttributeValue("def50", null);// 自定义项50
			save_bodyVO.setAttributeValue("pk_payreq", null);// 付款申请单_主键
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setChildrenVO(bodylist.toArray(new Business_b[0]));
		aggvo.setParentVO(save_headVO);

	}

	/**
	 * 成本付款申请单
	 * 
	 * @param jsonhead
	 * @param jsonbody
	 * @param aggvo
	 * @throws BusinessException
	 */
	private void transforCost(String jsonhead, String jsonbody,
			AggPayrequest aggvo, String srctype) throws BusinessException {
		// TODO 自动生成的方法存根
		// 转换json
		CostApplyBillHeadVO headvo = JSONObject.parseObject(jsonhead,
				CostApplyBillHeadVO.class);
		List<CostApplyBillBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				CostApplyBillBodyVO.class);
		checkHeadTransforCost(headvo, srctype);// 空值检查
		if (headvo == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("表单数据转换失败，请检查");
		}
		Payrequest save_headVO = new Payrequest();
		save_headVO.setAttributeValue("pk_payreq", null);// 单据主键
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 集团
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			save_headVO.setAttributeValue("pk_org",
					getPk_orgByCode(headvo.getOrg()));// 财务组织
		} else {
			throw new BusinessException("该财务组织编码：" + headvo.getOrg()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("pk_org_v", null);// 组织版本
		// if(StringUtils.isNotBlank(headvo.getCreator())){// 创建人
		// if(StringUtils.isNotBlank(getUserPkByCode(headvo.getCreator()))){
		// save_headVO.setAttributeValue("creator",
		// getUserPkByCode(headvo.getCreator()));
		// }else{
		// throw new
		// BusinessException("该创建人编码："+headvo.getCreator()+"未能在NC关联，请检查");
		// }
		// }
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// 创建时间
		save_headVO.setAttributeValue("modifier", "1001ZZ100000001MRF59");// 修改人
		save_headVO.setAttributeValue("modifiedtime", new UFDateTime());// 修改时间
		save_headVO.setAttributeValue("billno", null);// 单据号
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			String pk_orgCode = getPk_orgByCode(headvo.getOrg());
			save_headVO.setAttributeValue("pkorg", pk_orgCode);// 所属组织
			// 成本付款申请单接口，根据EBS传过来的财务组织+合同编码，带出该合同对应的“业务部门”，存到付款申请单“业务部门”表头def58-2020-04-20-谈子健
			String department = getDepartment(headvo.getContractcode(),
					pk_orgCode);
			save_headVO.setAttributeValue("def58", department);// 业务部门
		} else {
			throw new BusinessException("该所属组织编码：" + headvo.getOrg()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("busitype", headvo.getBusitype());// 业务类型
		save_headVO.setAttributeValue("billmaker",
				getUserInfo(headvo.getCreator()));// 制单人
		save_headVO.setAttributeValue("approver", null);// 审批人
		save_headVO.setAttributeValue("approvenote", null);// 审批批语
		save_headVO.setAttributeValue("approvedate", null);// 审批时间
		// 根据发票金额是否为0判断交易类型
		String transtype = null;
		String finstatus = headvo.getFinstatus();
		if ("finapprove".equals(finstatus)) {
			if ("Y".equals(headvo.getDef40())) {
				transtype = "FN01-Cxx-005";
			} else {
				transtype = "FN01-Cxx-002";
			}
		} else {
			transtype = "FN01-Cxx-005";
		}
		String pk_transtype = gettradeTypeByCode(transtype);
		save_headVO.setAttributeValue("transtype", transtype);// 交易类型
		save_headVO.setAttributeValue("billtype", "FN01");// 单据类型
		save_headVO.setAttributeValue("transtypepk", pk_transtype);// 交易类型pk
		save_headVO.setAttributeValue("emendenum", null);// 修订枚举
		save_headVO.setAttributeValue("billversionpk", null);// 单据版本pk
		save_headVO.setAttributeValue("billdate", new UFDate());// 单据日期
		save_headVO.setAttributeValue("pk_tradetypeid", null);// 付款类型
		save_headVO.setAttributeValue("def1", headvo.getEbsid());// EBS主键
		save_headVO.setAttributeValue("def2", headvo.getEbsbillcode());// EBS申请编号
		save_headVO.setAttributeValue("def3", headvo.getImagecode());// 影像编码
		save_headVO.setAttributeValue("def4", headvo.getImagestatus());// 影像状态
		save_headVO.setAttributeValue("def5", headvo.getContractcode());// 合同编码
		save_headVO.setAttributeValue("def6", headvo.getContractname());// 合同名称
		save_headVO.setAttributeValue("def7", headvo.getContracttype());// 合同类型
		// 成本付款申请单接口也需要根据合同类型带出对应的财务票据类型（成本类、费用类）,对应关系见自定义档案zdy045-2020-05-19-谈子健
		save_headVO.setAttributeValue("def59",
				getNoteType(headvo.getContracttype()));

		save_headVO.setAttributeValue("def8", headvo.getContractsubclass());// 合同细类
		String def9 = getdefdocBycode(headvo.getEmergency(), "zdy031");
		if (StringUtils.isNotBlank(def9)) {
			save_headVO.setAttributeValue("def9", def9);// 紧急程度
		} else {
			throw new BusinessException("紧急程度：" + headvo.getEmergency()
					+ "未能在NC关联，请检查");
		}

		save_headVO.setAttributeValue("def10", "EBS成本系统");// 外系统名称
		save_headVO.setAttributeValue("def11", headvo.getActuallypaidmoney());// 实付金额
		save_headVO.setAttributeValue("def12", null);// 自定义项12

		if (StringUtils.isNotBlank(getBalatypePkByCode(headvo.getBalatype()))) {
			getBalatypePkByCode(headvo.getBalatype());
			save_headVO.setAttributeValue("pk_balatype",
					getBalatypePkByCode(headvo.getBalatype()));// 结算方式
			// 当结算方式为保理时保理类型和资产编码-2020-06-09-谈子健-start
			if ("9".equals(headvo.getBalatype())) {
				save_headVO.setAttributeValue("def61", headvo.getDef61());// 保理类型（三种可选：平裕，声赫，联易融）
				save_headVO.setAttributeValue("def62", headvo.getDef62());// 资产编码
			}
			// 当结算方式为保理时必传保理类型和资产编码-2020-06-09-谈子健-end
		} else {
			throw new BusinessException("该结算方式编码：" + headvo.getBalatype()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("payaccount", headvo.getPayaccount());// 付款银行账户
		save_headVO.setAttributeValue("def13", null);// 自定义项13
		save_headVO.setAttributeValue("cashaccount", null);// 现金账户
		save_headVO.setAttributeValue("money", headvo.getMoney());// 金额
		// save_headVO.setAttributeValue("def14",
		// headvo.getAbsregisterdate());// abs登记日期
		if (StringUtils.isNotBlank(getdefdocBycode(headvo.getPlate(), "bkxx"))) {
			save_headVO.setAttributeValue("def15",
					getdefdocBycode(headvo.getPlate(), "bkxx"));// 板块
		} else {
			throw new BusinessException("该板块编码：" + headvo.getPlate()
					+ "未能在NC关联，请检查");
		}
		// 本次申请金额
		save_headVO.setAttributeValue("def16", headvo.getDef16());// 本次申请金额
		if (StringUtils.isNotBlank(headvo.getAccountingorg())) {
			if (StringUtils.isNotBlank(getPk_orgByCode(headvo
					.getAccountingorg()))) {
				save_headVO.setAttributeValue("def17",
						getPk_orgByCode(headvo.getAccountingorg()));// 出账公司
			} else {
				throw new BusinessException("该出账公司："
						+ headvo.getAccountingorg() + "未能在NC关联，请检查");
			}
		}
		if (StringUtils.isNotBlank(getSupplierIDByCodeOrName(headvo
				.getSupplier()))) {
			save_headVO.setAttributeValue("def18",
					getSupplierIDByCodeOrName(headvo.getSupplier()));// 收款方
		} else {
			throw new BusinessException("该收款方编码：" + headvo.getSupplier()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
		if (StringUtils.isNotBlank(headvo.getProject())) {
			if (StringUtils
					.isNotBlank(getpk_projectByCode(headvo.getProject()))) {
				save_headVO.setAttributeValue("def19",
						getpk_projectByCode(headvo.getProject()));// 项目
				// 付款申请单由传入项目信息带出是否资本(def8),项目资质(def9)
				HashMap<String, Object> projectData = getProjectDataByCode(headvo
						.getProject());
				if (projectData != null) {
					save_headVO.setAttributeValue("def56",
							projectData.get("def8"));// 是否资本
					save_headVO.setAttributeValue("def57",
							projectData.get("def9"));// 项目资质
				}

			} else {
				throw new BusinessException("该项目编码：" + headvo.getProject()
						+ "未能在NC关联，请检查");
			}
		} else {
			save_headVO.setAttributeValue("def19", null);// 项目
		}
		save_headVO.setAttributeValue("def20", null);// 自定义项20
		save_headVO.setAttributeValue("def21", headvo.getTotalapplymoney());// 累计请款金额
		save_headVO.setAttributeValue("def22", headvo.getTotalamount());// 累计付款金额
		save_headVO.setAttributeValue("def23", headvo.getIspaynote());// 是否先付款后补票
		save_headVO.setAttributeValue("def24", null);// 自定义项24
		save_headVO.setAttributeValue("def25", null);// 本次请款累计付款金额
		save_headVO.setAttributeValue("def26", headvo.getTotalnotemoney());// 累计发票金额
		save_headVO.setAttributeValue("def27", null);//
		save_headVO.setAttributeValue("def28", headvo.getIsmargin());// 是否质保金扣除
		save_headVO.setAttributeValue("billstatus", null);// 单据状态
		save_headVO.setAttributeValue("approvestatus", -1);// 审批状态
		save_headVO.setAttributeValue("effectstatus", 0);// 生效状态
		save_headVO.setAttributeValue("def29", headvo.getIspaid());// 是否付讫
		save_headVO.setAttributeValue("def30", headvo.getDef30());//
		save_headVO.setAttributeValue("def31", headvo.getExplain());// 说明
		save_headVO.setAttributeValue("def32", headvo.getProjectstages());// 项目分期

		// save_headVO.setAttributeValue("def33", headvo.getFinstatus());//
		// 地区财务审批状态
		if (StringUtils.isNotBlank(headvo.getFinstatus())) {
			String pk_auditstate = getAuditstateByCode(headvo.getFinstatus());
			if (pk_auditstate == null) {
				throw new BusinessException("地区财务审批状态 【"
						+ headvo.getFinstatus() + "】未能在NC档案关联到相关信息!");
			}
			save_headVO.setAttributeValue("def33", pk_auditstate);// 自定义项33 项目分期
		}

		save_headVO.setAttributeValue("def34", headvo.getSignorg());// 签约公司
		save_headVO.setAttributeValue("def35", headvo.getDeptname());// 经办部门
		save_headVO.setAttributeValue("def36", headvo.getOperatorname());// 经办人
		save_headVO.setAttributeValue("def37", headvo.getDef37());// 登记金额
		save_headVO.setAttributeValue("def38", headvo.getDef38());// 水电费
		save_headVO.setAttributeValue("def39", headvo.getDef39());// 罚款
		save_headVO.setAttributeValue("def40", headvo.getDef40());// 发票金额是否为0
		save_headVO.setAttributeValue("def41", null);// 自定义项41
		save_headVO.setAttributeValue("def42", null);// 自定义项42
		save_headVO.setAttributeValue("def43", headvo.getPaymentletter());// 非标准指定付款涵def43
		save_headVO.setAttributeValue("def44", headvo.getRepairannex());// 补附件def44
		save_headVO.setAttributeValue("def45", headvo.getCompleted());// 已补全def45

		// save_headVO.setAttributeValue("def43", null);// 自定义项43
		// save_headVO.setAttributeValue("def44", null);// 自定义项44
		// save_headVO.setAttributeValue("def45", null);// 自定义项45
		save_headVO.setAttributeValue("def46", headvo.getDef46());// 其他扣款
		save_headVO.setAttributeValue("def47", headvo.getDef47());// 抵楼标识
		save_headVO.setAttributeValue("def48", null);// 自定义项48
		save_headVO.setAttributeValue("def49", headvo.getMoney());// 本次请款未付金额
		save_headVO.setAttributeValue("def50", null);// 自定义项50
		String def51 = headvo.getDef51();
		if (def51 != null && !"".equals(def51)) {
			save_headVO.setAttributeValue("def51", def51);// EBS请款方式
		} else {
			throw new BusinessException("EBS请款方式不能为空");
		}
		String def52 = headvo.getDef52();
		if (def52 != null && !"".equals(def52)) {
			save_headVO.setAttributeValue("def52", def52);// 款项类型
		} else {
			throw new BusinessException("款项类型不能为空");
		}
		// 保证金比例
		save_headVO.setAttributeValue("def54", headvo.getDef54());
		// ABS实付比例
		save_headVO.setAttributeValue("def55", headvo.getDef55());
		// bpmid
		save_headVO.setBpmid(headvo.getBpmid());

		save_headVO.setStatus(VOStatus.NEW);

		List<Business_b> bodylist = new ArrayList<>();
		for (CostApplyBillBodyVO applyBillBodyVO : bodyVOs) {
			Business_b save_bodyVO = new Business_b();
			checkBodyTransforCost(applyBillBodyVO, srctype);// 空值检查
			save_bodyVO.setAttributeValue("pk_business_b", null);// 业务页签主键
			save_bodyVO
					.setAttributeValue("scomment", applyBillBodyVO.getMemo());// 摘要
			save_bodyVO.setAttributeValue("def1", null);// 自定义项1
			// save_bodyVO.setAttributeValue("pk_subjcode",
			// applyBillBodyVO.getSubjcode());// 收支项目
			// if (StringUtils.isNotBlank(getdefdocBycode(
			// applyBillBodyVO.getProceedtype(), "zdy020"))) {
			// save_bodyVO.setAttributeValue(
			// "def2",
			// getdefdocBycode(applyBillBodyVO.getProceedtype(),
			// "zdy020"));// 款项类型
			// } else {
			// throw new BusinessException("该款项类型编码："
			// + applyBillBodyVO.getProceedtype() + "未能在NC关联，请检查");
			// }
			if (StringUtils.isNotBlank(applyBillBodyVO.getBusinessformat())) {
				if (StringUtils.isNotBlank(getdefdocBycode(
						applyBillBodyVO.getBusinessformat(), "ys004"))) {
					save_bodyVO.setAttributeValue(
							"def3",
							getdefdocBycode(
									applyBillBodyVO.getBusinessformat(),
									"ys004"));// 业态
				} else {
					throw new BusinessException("该业态编码："
							+ applyBillBodyVO.getBusinessformat()
							+ "未能在NC关联，请检查");
				}
			}
			save_bodyVO.setAttributeValue("def4", applyBillBodyVO.getScale());// 比例
			/**
			 * ebs报文传表头往来对象:objtype ebs报文传表头业务员:psndoc NC保存在付款申请单的
			 * 表体往来对象:objtype 表体业务员:def58
			 */

			String objtype = applyBillBodyVO.getObjtype();
			if (objtype == null || "".equals(objtype)) {
				throw new BusinessException("往来对象objtype不能为空!");
			}
			String supplier = applyBillBodyVO.getSupplier();
			if (null != objtype && "3".equals(objtype)) {
				save_bodyVO.setAttributeValue("objtype", objtype);// 往来对象
				String pk_psndoc = getPk_psndocByCode(supplier);
				if (pk_psndoc != null) {
					save_bodyVO.setAttributeValue("def58", pk_psndoc);// 业务员
				} else {
					throw new BusinessException("业务员编码：" + supplier
							+ "未能在NC关联，请检查");
				}
				if (!"".equals(applyBillBodyVO.getRecaccount())
						&& applyBillBodyVO.getRecaccount() != null) {
					String recaccount = getPersonalAccountIDByCode(
							applyBillBodyVO.getRecaccount(), pk_psndoc);

					if ("".equals(recaccount) || recaccount == null) {
						throw new BusinessException("该供应商下没有该银行账号子户");
					}

					save_bodyVO.setAttributeValue("def15", recaccount);// 收款银行账户

					// 省份城市
					save_bodyVO.setAttributeValue("def43",
							getCityByrecaccount(recaccount).get("province"));
					save_bodyVO.setAttributeValue("def44",
							getCityByrecaccount(recaccount).get("city"));
				}
			} else {
				String pk_supplier = getSupplierIDByCodeOrName(supplier);
				if (StringUtils.isNotBlank(pk_supplier)) {
					save_bodyVO.setAttributeValue("supplier", pk_supplier);// 实际收款方(供应商)
					// 供应商基本分类
					String pk_supplierclass = getSupplierClassByCode(supplier);
					save_bodyVO.setDef45(pk_supplierclass);
					if (null != objtype && "1".equals(objtype)) {
						save_bodyVO.setAttributeValue("objtype", objtype);// 往来对象
					}
				} else {
					throw new BusinessException("该供应商编码：" + supplier
							+ "未能在NC关联，请检查");
				}

				if (!"".equals(applyBillBodyVO.getRecaccount())
						&& applyBillBodyVO.getRecaccount() != null) {
					String recaccount = getAccountIDByCode(
							applyBillBodyVO.getRecaccount(), pk_supplier);

					if ("".equals(recaccount) || recaccount == null) {
						throw new BusinessException("该供应商下没有该银行账号子户");
					}

					save_bodyVO.setAttributeValue("def15", recaccount);// 收款银行账户
					// 省份城市
					save_bodyVO.setAttributeValue("def43",
							getCityByrecaccount(recaccount).get("province"));
					save_bodyVO.setAttributeValue("def44",
							getCityByrecaccount(recaccount).get("city"));
				}
			}

			save_bodyVO.setAttributeValue("def5", null);// 自定义项5
			save_bodyVO.setAttributeValue("def6",
					applyBillBodyVO.getBankraccountcode());// 收款方开户行行号
			save_bodyVO.setAttributeValue("def7", null);// 自定义项7
			save_bodyVO.setAttributeValue("local_money_de",
					applyBillBodyVO.getPayamount());// 付款金额
			save_bodyVO.setAttributeValue("def8", null);// 自定义项8
			String pk_defdoc = getdefdocBycode(
					applyBillBodyVO.getCostsubjects(), "zdy024");
			if (StringUtils.isNotBlank(pk_defdoc)) {
				save_bodyVO.setAttributeValue("def9", pk_defdoc);// 成本科目
				if (save_headVO.getDef7() != null
						&& save_headVO.getDef47() != null) {
					// 收支项目
					String pk_subjcode = getSubjcode(save_headVO.getDef7(),
							save_headVO.getDef47());
					save_bodyVO.setAttributeValue("pk_subjcode", pk_subjcode);
				}

			} else {
				throw new BusinessException("该成本科目的EBSID："
						+ applyBillBodyVO.getCostsubjects() + "未能在NC关联，请检查");
			}
			save_bodyVO.setAttributeValue("def10", null);// 自定义项10
			save_bodyVO.setAttributeValue("def11",
					applyBillBodyVO.getNotetype());// 票据类型
			save_bodyVO.setAttributeValue("def12", applyBillBodyVO.getNoteno());// 票据号
			save_bodyVO.setAttributeValue("def13",
					applyBillBodyVO.getAccountsubjcode());// 会计科目
			save_bodyVO.setAttributeValue("def14",
					applyBillBodyVO.getPayaccount());// 付款银行账户
			// save_bodyVO.setAttributeValue(
			// "def15",
			// getAccountIDByCode(applyBillBodyVO.getRecaccount(),
			// pk_supplier));// 收款银行账户

			save_bodyVO.setAttributeValue("def16",
					applyBillBodyVO.getCastaccount());// 现金账户
			save_bodyVO.setAttributeValue("def17", null);// 自定义项17
			save_bodyVO.setAttributeValue("def18", null);// 自定义项18
			save_bodyVO.setAttributeValue("def19", null);// 自定义项19
			save_bodyVO.setAttributeValue("def20", null);// 自定义项20
			save_bodyVO.setAttributeValue("def21", null);// 自定义项21
			save_bodyVO.setAttributeValue("def22", null);// 自定义项22
			save_bodyVO.setAttributeValue("def23", null);// 自定义项23
			save_bodyVO.setAttributeValue("def24", null);// 自定义项24
			save_bodyVO.setAttributeValue("def25", null);// 自定义项25
			save_bodyVO.setAttributeValue("def26", null);// 自定义项26
			save_bodyVO.setAttributeValue("def27", null);// 自定义项27
			save_bodyVO.setAttributeValue("def28", null);// 自定义项28
			save_bodyVO.setAttributeValue("def29", null);// 自定义项29
			save_bodyVO.setAttributeValue("def30", null);// 自定义项30
			save_bodyVO.setAttributeValue("def31", null);// 自定义项31
			save_bodyVO.setAttributeValue("def32", null);// 自定义项32
			save_bodyVO.setAttributeValue("def33", null);// 自定义项33
			save_bodyVO.setAttributeValue("def34", null);// 自定义项34
			save_bodyVO.setAttributeValue("def35", null);// 自定义项35
			save_bodyVO.setAttributeValue("def36", null);// 自定义项36
			save_bodyVO.setAttributeValue("def37", null);// 自定义项37
			String def38 = applyBillBodyVO.getDef38();
			if (def38 != null && !"".equals(def38)) {
				save_bodyVO.setAttributeValue("def38", def38);// EBS行ID
			} else {
				throw new BusinessException("EBS行ID不能为空!");
			}
			save_bodyVO.setAttributeValue("def39", null);// 自定义项39
			String def40 = applyBillBodyVO.getDef40();
			if (def40 != null && !"".equals(def40)) {
				save_bodyVO.setAttributeValue("def40", def40);// EBS表名
			} else {
				throw new BusinessException("EBS表名不能为空!");
			}
			save_bodyVO.setAttributeValue("def41", null);// 自定义项41
			save_bodyVO.setAttributeValue("def42", null);// 自定义项42
			// save_bodyVO.setAttributeValue("def45", null);// 自定义项45
			save_bodyVO.setAttributeValue("def46", null);// 自定义项46
			save_bodyVO.setAttributeValue("def47",
					applyBillBodyVO.getPayamount());// 未付金额
			// applyBillBodyVO.getDef48());// 水电费金额
			// save_bodyVO.setAttributeValue("def48",
			// save_bodyVO.setAttributeValue("def49",
			// applyBillBodyVO.getDef49());// 罚款
			save_bodyVO.setAttributeValue("def50", null);// 自定义项50
			save_bodyVO.setAttributeValue("pk_payreq", null);// 付款申请单_主键
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setChildrenVO(bodylist.toArray(new Business_b[0]));
		aggvo.setParentVO(save_headVO);
	}

	/**
	 * 通用付款申请单
	 * 
	 * @param jsonhead
	 * @param jsonbody
	 * @param aggvo
	 * @throws BusinessException
	 */
	private void transforexpense(String jsonhead, String jsonbody,
			AggPayrequest aggvo, String srctype) throws BusinessException {
		// TODO 自动生成的方法存根
		// 转换json
		ApplyBillHeadVO headvo = JSONObject.parseObject(jsonhead,
				ApplyBillHeadVO.class);
		List<ApplyBillBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				ApplyBillBodyVO.class);
		if (headvo == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("表单数据转换失败，请检查");
		}
		checkHeadTransforExpense(headvo, srctype);// 空值检查
		Payrequest save_headVO = new Payrequest();
		save_headVO.setAttributeValue("pk_payreq", null);// 单据主键
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 集团
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			save_headVO.setAttributeValue("pk_org",
					getPk_orgByCode(headvo.getOrg()));// 财务组织
		} else {
			throw new BusinessException("该财务组织编码：" + headvo.getOrg()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("pk_org_v", null);// 组织版本
		if (StringUtils.isNotBlank(headvo.getCreator())) {// 创建人
			if (StringUtils.isNotBlank(getUserPkByCode(headvo.getCreator()))) {
				save_headVO.setAttributeValue("creator",
						getUserPkByCode(headvo.getCreator()));
			} else {
				throw new BusinessException("该创建人编码：" + headvo.getCreator()
						+ "未能在NC关联，请检查");
			}
		}
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// 创建时间
		save_headVO.setAttributeValue("modifier", null);// 修改人
		save_headVO.setAttributeValue("modifiedtime", null);// 修改时间
		save_headVO.setAttributeValue("billno", null);// 单据号
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			save_headVO.setAttributeValue("pkorg",
					getPk_orgByCode(headvo.getOrg()));// 所属组织
		} else {
			throw new BusinessException("该所属组织编码：" + headvo.getOrg()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("busitype", null);// 业务类型
		if (StringUtils.isNotBlank(headvo.getCreator())) {
			if (StringUtils.isNotBlank(getUserInfo(headvo.getCreator()).get(
					"cuserid"))) {
				save_headVO.setAttributeValue("billmaker",
						getUserInfo(headvo.getCreator()).get("cuserid"));// 制单人
			} else {
				throw new BusinessException("该制单人名称：" + headvo.getCreator()
						+ "未能在NC关联，请检查");
			}
		}
		save_headVO.setAttributeValue("approver", null);// 审批人
		save_headVO.setAttributeValue("approvenote", null);// 审批批语
		save_headVO.setAttributeValue("approvedate", null);// 审批时间
		// if("06".equals(srctype)){//EBS-通用请款为04 FN01-Cxx-001 EBS营销费用为06
		// 成本交易类型为FN01-Cxx-002
		// pk_transtype=gettradeTypeByCode("FN01-Cxx-002");
		// }else{
		// pk_transtype=gettradeTypeByCode("FN01-Cxx-001");
		// }

		// 根据发票金额是否为0判断交易类型
		String transtype = null;
		String finstatus = headvo.getFinstatus();
		if ("finapprove".equals(finstatus)) {
			if ("Y".equals(headvo.getDef40())) {
				transtype = "FN01-Cxx-001";
			} else {
				transtype = "FN01-Cxx-006";
			}
		} else {
			transtype = "FN01-Cxx-001";
		}
		String pk_transtype = gettradeTypeByCode(transtype);
		save_headVO.setAttributeValue("transtype", transtype);// 交易类型
		save_headVO.setAttributeValue("billtype", "FN01");// 单据类型
		save_headVO.setAttributeValue("transtypepk", pk_transtype);// 交易类型pk
		save_headVO.setAttributeValue("emendenum", null);// 修订枚举
		save_headVO.setAttributeValue("billversionpk", null);// 单据版本pk
		save_headVO.setAttributeValue("billdate", new UFDate());// 单据日期
		save_headVO.setAttributeValue("pk_tradetypeid", null);// 付款类型
		save_headVO.setAttributeValue("def1", headvo.getEbsid());// EBS主键
		save_headVO.setAttributeValue("def2", headvo.getEbsbillcode());// EBS申请编号
		save_headVO.setAttributeValue("def3", headvo.getImagecode());// 影像编码
		save_headVO.setAttributeValue("def4",
				headvo.getImagestatus() == null ? "" : "已扫描");// 影像状态
		save_headVO.setAttributeValue("def5", headvo.getContractcode());// 合同编码
		save_headVO.setAttributeValue("def6", headvo.getContractname());// 合同名称
		save_headVO.setAttributeValue("def7", headvo.getContracttype());// 合同类型
		save_headVO.setAttributeValue("def8", headvo.getContractsubclass());// 合同细类
		save_headVO.setAttributeValue("def9",
				getdefdocBycode(headvo.getEmergency(), "zdy031"));// 紧急程度
		save_headVO.setAttributeValue("def10", "EBS费用系统");// 外系统名称默认传EBS
		save_headVO.setAttributeValue("def11", headvo.getDef11());// 合同ID

		save_headVO.setAttributeValue("def12", headvo.getLongproject());// 中长期项目
		// if(StringUtils.isNotBlank(getPk_DeptByCode(headvo.getDept(),
		// save_headVO.getPk_org()))){
		// save_headVO.setAttributeValue("pk_deptid_v",
		// getPk_DeptByCode(headvo.getDept(), save_headVO.getPk_org()));// 经办部门
		// }else{
		// throw new
		// BusinessException("该经办部门编码："+headvo.getDept()+"未能在NC关联，请检查");
		// }
		// if(StringUtils.isNotBlank(getPsndocPkByCode(headvo.getOperator()))){
		// save_headVO.setAttributeValue("pk_psndoc",
		// getPsndocPkByCode(headvo.getOperator()));// 经办人
		// }else{
		// throw new
		// BusinessException("该经办人编码："+headvo.getOperator()+"未能在NC关联，请检查");
		// }
		if (StringUtils.isNotBlank(getBalatypePkByCode(headvo.getBalatype()))) {
			save_headVO.setAttributeValue("pk_balatype",
					getBalatypePkByCode(headvo.getBalatype()));// 结算方式
		} else {
			throw new BusinessException("该结算方式编码：" + headvo.getBalatype()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("payaccount", headvo.getPayaccount());// 付款银行账户
		save_headVO.setAttributeValue("recaccount", headvo.getBankaccount());// 收款银行账户
		save_headVO.setAttributeValue("def13", null);//
		save_headVO.setAttributeValue("cashaccount", null);// 现金账户

		// TODO add by huangdq 20200227 金额取接口传入的申请金额 -start-
		// save_headVO.setAttributeValue("money", headvo.getMoney());// 金额
		save_headVO.setAttributeValue("money", headvo.getApplymoney());// 金额
		// TODO add by huangdq 20200227 金额取接口传入的申请金额 -start-

		save_headVO.setAttributeValue("def14", null);//
		if (StringUtils.isNotBlank(headvo.getPlate())) {
			if (StringUtils.isNotBlank(getdefdocBycode(headvo.getPlate(),
					"bkxx"))) {
				save_headVO.setAttributeValue("def15",
						getdefdocBycode(headvo.getPlate(), "bkxx"));// 板块
			} else {
				throw new BusinessException("该板块编码：" + headvo.getPlate()
						+ "未能在NC关联，请检查");
			}
		}
		save_headVO.setAttributeValue("def16", null);//
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getAccountingorg()))) {
			save_headVO.setAttributeValue("def17",
					getPk_orgByCode(headvo.getAccountingorg()));// 出账公司
		} else {
			throw new BusinessException("该出账公司：" + headvo.getAccountingorg()
					+ "未能在NC关联，请检查");
		}
		save_headVO.setAttributeValue("def18",
				getCustPkByCode(headvo.getReceiver()));// 收款方
		save_headVO.setAttributeValue("pk_currtype", headvo.getPk_currtype());// 币种
		if (StringUtils.isNotBlank(headvo.getProject())) {
			if (StringUtils
					.isNotBlank(getpk_projectByCode(headvo.getProject()))) {
				save_headVO.setAttributeValue("def19",
						getpk_projectByCode(headvo.getProject()));// 项目
			} else {
				throw new BusinessException("该项目编码：" + headvo.getProject()
						+ "未能在NC关联，请检查");
			}
		}
		save_headVO.setAttributeValue("def20", headvo.getIsbuyticket());// 是否先付款后补票
		save_headVO.setAttributeValue("def21", headvo.getTotalapplyamount());// 累计请款金额
		save_headVO.setAttributeValue("def22", headvo.getTotalamount());// 累计付款金额
		save_headVO.setAttributeValue("def23", null);//
		save_headVO.setAttributeValue("def24", headvo.getApplytotalamount());// 本次请款累计付款金额
		save_headVO.setAttributeValue("def25", null);//
		save_headVO.setAttributeValue("def26", null);//
		save_headVO.setAttributeValue("def27", headvo.isIslostimg());// 是否缺失影像
		save_headVO.setAttributeValue("def28", null);//
		save_headVO.setAttributeValue("billstatus", null);// 单据状态
		save_headVO.setAttributeValue("approvestatus", -1);// 审批状态
		save_headVO.setAttributeValue("effectstatus", 0);// 生效状态
		save_headVO.setAttributeValue(
				"def29",
				Boolean.valueOf(headvo.isIsdelete()) == null ? false : Boolean
						.valueOf(headvo.isIsdelete()));// 是否作废
		save_headVO.setAttributeValue("def30", null);// 自定义项30
		save_headVO.setAttributeValue("def23", headvo.getIspaynote());// 是否先付款后补票
		save_headVO.setAttributeValue("def31", null);// 自定义项31
		save_headVO.setAttributeValue("def32", null);// 自定义项32
		// save_headVO.setAttributeValue("def33", null);// 自定义项33
		// 根据code获取地区财务审批状态2020-02-28-谈子健
		if (StringUtils.isNotBlank(headvo.getFinstatus())) {
			String pk_defdoc = getAuditstateByCode(headvo.getFinstatus());
			if (pk_defdoc == null) {
				throw new BusinessException("地区财务审批状态【" + headvo.getFinstatus()
						+ "】未能在NC档案关联到相关信息!");
			}
			save_headVO.setAttributeValue("def33", pk_defdoc);// 地区财务审批状态
		}
		save_headVO.setAttributeValue("def34", null);// 自定义项34
		save_headVO.setAttributeValue("def35", headvo.getDept());// 经办部门
		save_headVO.setAttributeValue("def36", headvo.getOperator());// 经办人
		// save_headVO.setAttributeValue("def37", null);// 自定义项37
		save_headVO.setAttributeValue("def38", null);// 自定义项38
		save_headVO.setAttributeValue("def39", null);// 自定义项39
		save_headVO.setAttributeValue("def40", headvo.getDef40());// 自定义项40
		save_headVO.setAttributeValue("def41", null);// 自定义项41
		save_headVO.setAttributeValue("def42", null);// 自定义项42
		save_headVO.setAttributeValue("def43", headvo.getDef43());// 非标准指定付款函
		save_headVO.setAttributeValue("def44", headvo.getDef44());// 补附件
		save_headVO.setAttributeValue("def45", headvo.getDef45());// 已补全
		save_headVO.setAttributeValue("def46", null);// 自定义项46
		save_headVO.setAttributeValue("def47", headvo.getDef47());// 是否抵楼标识
		save_headVO.setAttributeValue("def48", null);// 自定义项48
		// TODO add by huangdq 20200227 未付金额由定时任务回写 -start-
		// save_headVO.setAttributeValue("def49", headvo.getApplymoney());//
		// 本次请款未付金额
		save_headVO.setAttributeValue("def49", UFDouble.ZERO_DBL.toString());// 本次请款未付金额
		// TODO add by huangdq 20200227 未付金额由定时任务回写 -end-

		save_headVO.setAttributeValue("def50", null);// 自定义项50
		save_headVO.setAttributeValue("def61", headvo.getDef61());// 请款类型
		if (StringUtils.isNotBlank(getSupplierIDByCodeOrName(headvo
				.getReceiver()))) {
			save_headVO.setAttributeValue("supplier",
					getSupplierIDByCodeOrName(headvo.getReceiver()));// 供应商(收款方)
		} else {
			throw new BusinessException("该收款方编码：" + headvo.getReceiver()
					+ "未能在NC关联，请检查");
		}
		// 表头：保证金比例、ABS实付比例、ABS实付金额-2020-04-27-谈子健-start
		// 保证金比例
		save_headVO.setAttributeValue("def54", headvo.getDef54());
		// ABS实付比例
		save_headVO.setAttributeValue("def55", headvo.getDef55());
		// 登记金额
		String def37 = "";
		if (null != headvo.getDef37()) {
			UFDouble ufDouble = new UFDouble(headvo.getDef37());
			def37 = ufDouble.div(100).toString();
		}
		save_headVO.setAttributeValue("def37", def37);
		// 表头：保证金比例、ABS实付比例、ABS实付金额-2020-04-27-谈子健-end
		// bpmid
		save_headVO.setAttributeValue("bpmid", headvo.getBpmid());

		// 通用付款申请单添加EBS请款方式-2020-07-10-谈子健
		String def51 = headvo.getDef51();
		if (def51 != null && !"".equals(def51)) {
			save_headVO.setAttributeValue("def51", def51);// EBS请款方式
		} else {
			throw new BusinessException("EBS请款方式不能为空");
		}

		save_headVO.setStatus(VOStatus.NEW);

		List<Business_b> bodylist = new ArrayList<>();
		for (ApplyBillBodyVO applyBillBodyVO : bodyVOs) {
			Business_b save_bodyVO = new Business_b();
			checkBodyTransforExpense(applyBillBodyVO, srctype);// 空值检查
			save_bodyVO.setAttributeValue("pk_business_b", null);// 业务页签主键
			save_bodyVO
					.setAttributeValue("scomment", applyBillBodyVO.getMemo());// 摘要
			if (StringUtils.isNotBlank(applyBillBodyVO.getBudgetsubjects())) {
				if (StringUtils.isNotBlank(getBudgetsubByCode(applyBillBodyVO
						.getBudgetsubjects()))) {
					save_bodyVO.setAttributeValue("def1",
							getBudgetsubByCode(applyBillBodyVO
									.getBudgetsubjects()));// 预算科目
				} else {
					throw new BusinessException("该预算科目编码："
							+ applyBillBodyVO.getBudgetsubjects()
							+ "未能在NC关联，请检查");
				}
			}
			if (StringUtils.isNotBlank(applyBillBodyVO.getSubjcode())) {
				if (StringUtils.isNotBlank(getInoutPkByCode(applyBillBodyVO
						.getSubjcode()))) {
					save_bodyVO.setAttributeValue("pk_subjcode",
							getInoutPkByCode(applyBillBodyVO.getSubjcode()));// 收支项目
				} else {
					throw new BusinessException("该收支项目编码："
							+ applyBillBodyVO.getSubjcode() + "未能在NC关联，请检查");
				}
			}
			if (StringUtils.isNotBlank(applyBillBodyVO.getProceedtype())) {
				if (StringUtils.isNotBlank(getdefdocBycode(
						applyBillBodyVO.getProceedtype(), "zdy020"))) {
					save_bodyVO.setAttributeValue(
							"def2",
							getdefdocBycode(applyBillBodyVO.getProceedtype(),
									"zdy020"));// 款项类型
				} else {
					throw new BusinessException("该款项类型编码："
							+ applyBillBodyVO.getProceedtype() + "未能在NC关联，请检查");
				}
			}
			if (StringUtils.isNotBlank(applyBillBodyVO.getBusinessformat())) {
				if (StringUtils.isNotBlank(getdefdocBycode(
						applyBillBodyVO.getBusinessformat(), "ys004"))) {
					save_bodyVO.setAttributeValue(
							"def3",
							getdefdocBycode(
									applyBillBodyVO.getBusinessformat(),
									"ys004"));// 业态
				} else {
					throw new BusinessException("该业态编码："
							+ applyBillBodyVO.getBusinessformat()
							+ "未能在NC关联，请检查");
				}
			}
			save_bodyVO.setAttributeValue("def4", applyBillBodyVO.getScale());// 比例
			save_bodyVO.setAttributeValue("objtype", 1);// 往来对象

			save_bodyVO.setAttributeValue("def6",
					applyBillBodyVO.getBankraccountcode());// 收款方开户行行号
			save_bodyVO.setAttributeValue("def7",
					applyBillBodyVO.getBankaccount());// 收款方账户
			save_bodyVO.setAttributeValue("local_money_de",
					applyBillBodyVO.getPayamount());// 付款金额

			save_bodyVO.setAttributeValue("def8",
					applyBillBodyVO.getOffsetorg());// 抵冲出账公司
			save_bodyVO.setAttributeValue("def9", null);// 自定义项9
			save_bodyVO.setAttributeValue("def10", null);// 自定义项10
			save_bodyVO.setAttributeValue("def11",
					applyBillBodyVO.getChecktype());// 票据类型
			save_bodyVO
					.setAttributeValue("def12", applyBillBodyVO.getCheckno());// 票据号
			save_bodyVO.setAttributeValue("def13", null);// 自定义项13
			save_bodyVO.setAttributeValue("def14", null);// 自定义项14
			// save_bodyVO.setAttributeValue("def15", null);// 自定义项15
			save_bodyVO.setAttributeValue("def16", null);// 自定义项16
			save_bodyVO.setAttributeValue("def17", null);// 自定义项17
			save_bodyVO.setAttributeValue("def18", null);// 自定义项18
			save_bodyVO.setAttributeValue("def19", null);// 自定义项19
			save_bodyVO.setAttributeValue("def20", null);// 自定义项20
			save_bodyVO.setAttributeValue("def21",
					applyBillBodyVO.getPayoffaccomunt());// 解付账户
			save_bodyVO.setAttributeValue("def22",
					applyBillBodyVO.getPayoffamount());// 抵冲/解付金额
			save_bodyVO.setAttributeValue("def23", null);// 自定义项23
			save_bodyVO.setAttributeValue("def24", null);// 自定义项24
			save_bodyVO.setAttributeValue("def25", null);// 自定义项25
			save_bodyVO.setAttributeValue("def26", null);// 自定义项26
			save_bodyVO.setAttributeValue("def27", null);// 自定义项27
			save_bodyVO.setAttributeValue("def28", null);// 自定义项28
			save_bodyVO.setAttributeValue("def29", null);// 自定义项29
			save_bodyVO.setAttributeValue("def30", null);// 自定义项30
			save_bodyVO.setAttributeValue("def31", null);// 自定义项31
			save_bodyVO.setAttributeValue("def32", null);// 自定义项32
			save_bodyVO.setAttributeValue("def33", null);// 自定义项33
			save_bodyVO.setAttributeValue("def34", null);// 自定义项34
			save_bodyVO.setAttributeValue("def35", null);// 自定义项35
			save_bodyVO.setAttributeValue("def36", null);// 自定义项36
			save_bodyVO.setAttributeValue("def37", null);// 自定义项37
			save_bodyVO.setAttributeValue("def38", null);// 自定义项38
			save_bodyVO.setAttributeValue("def39", null);// 自定义项39
			save_bodyVO.setAttributeValue("def40", null);// 自定义项40
			save_bodyVO.setAttributeValue("def44", null);// 自定义项41
			save_bodyVO.setAttributeValue("def42", null);// 自定义项42
			save_bodyVO.setAttributeValue("def43", null);// 自定义项43
			save_bodyVO.setAttributeValue("def41",
					applyBillBodyVO.getOperationmode());// 操作方式
			save_bodyVO.setAttributeValue("def45", null);// 自定义项45
			save_bodyVO.setAttributeValue("def46", null);// 自定义项46
			save_bodyVO.setAttributeValue("def47",
					applyBillBodyVO.getPayamount());// 未付金额
			save_bodyVO.setAttributeValue("def48", null);// 自定义项48
			save_bodyVO.setAttributeValue("def49", null);// 自定义项49
			save_bodyVO.setAttributeValue("def50", null);// 自定义项50
			save_bodyVO.setAttributeValue("def51", applyBillBodyVO.getDef51());// 商票开户行号
			save_bodyVO.setAttributeValue("def52", applyBillBodyVO.getDef52());// 是否共管账户
			save_bodyVO.setAttributeValue("pk_payreq", null);// 付款申请单_主键
			/**
			 * ebs报文传表头往来对象:objtype ebs报文传表头业务员:psndoc NC保存在付款申请单的
			 * 表体往来对象:objtype 表体业务员:def58 2020-07-01-谈子健
			 */

			String objtype = applyBillBodyVO.getObjtype();
			if (objtype == null || "".equals(objtype)) {
				throw new BusinessException("往来对象objtype不能为空!");
			}
			if (null != objtype && "3".equals(objtype)) {
				save_bodyVO.setAttributeValue("objtype", objtype);// 往来对象
				String pk_psndoc = getPk_psndocByCode(applyBillBodyVO
						.getSupplier());
				if (pk_psndoc != null) {
					save_bodyVO.setAttributeValue("def58", pk_psndoc);// 业务员
				} else {
					throw new BusinessException("业务员编码："
							+ applyBillBodyVO.getSupplier() + "未能在NC关联，请检查");
				}

				if (!"".equals(applyBillBodyVO.getRealitybankraccount())
						&& applyBillBodyVO.getRealitybankraccount() != null) {
					String recaccount = getPersonalAccountIDByCode(
							applyBillBodyVO.getRealitybankraccount(), pk_psndoc);

					if ("".equals(recaccount) || recaccount == null) {
						throw new BusinessException("该供应商下没有该银行账号子户");
					}

					save_bodyVO.setAttributeValue("def15", recaccount);// 收款银行账户

					// 省份城市
					save_bodyVO.setAttributeValue("def43",
							getCityByrecaccount(recaccount).get("province"));
					save_bodyVO.setAttributeValue("def44",
							getCityByrecaccount(recaccount).get("city"));
				}

			} else {
				String supplier = getSupplierIDByCodeOrName(applyBillBodyVO
						.getSupplier());
				if (StringUtils.isNotBlank(supplier)) {
					String pk_supplier = getSupplierIDByCodeOrName(applyBillBodyVO
							.getSupplier());
					save_bodyVO.setAttributeValue("supplier", pk_supplier);// 供应商
					if (null != objtype && "1".equals(objtype)) {
						save_bodyVO.setAttributeValue("objtype", objtype);// 往来对象
					}
					if (!"".equals(applyBillBodyVO.getRealitybankraccount())
							&& applyBillBodyVO.getRealitybankraccount() != null) {
						String recaccount = getAccountIDByCode(
								applyBillBodyVO.getRealitybankraccount(),
								pk_supplier);

						if ("".equals(recaccount) || recaccount == null) {
							throw new BusinessException("该供应商下没有该银行账号子户");
						}

						save_bodyVO.setAttributeValue("def15", recaccount);// 收款银行账户

						// 省份城市
						save_bodyVO
								.setAttributeValue(
										"def43",
										getCityByrecaccount(recaccount).get(
												"province"));
						save_bodyVO.setAttributeValue("def44",
								getCityByrecaccount(recaccount).get("city"));
					}

				} else {
					throw new BusinessException("该供应商编码："
							+ applyBillBodyVO.getSupplier() + "未能在NC关联，请检查");
				}

			}

			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setChildrenVO(bodylist.toArray(new Business_b[0]));
		aggvo.setParentVO(save_headVO);

	}

	private String getBalatypeCode(String ebsCode) {
		if ("1".equals(ebsCode)) {
			return "10";// 银企直联
		} else if ("2".equals(ebsCode)) {
			return "15";// 支票
		} else if ("3".equals(ebsCode)) {
			return "12";// 抵楼
		} else if ("4".equals(ebsCode)) {
			return "6";// 银行承兑汇票
		} else if ("5".equals(ebsCode)) {
			return "16";// 电汇单
		} else if ("6".equals(ebsCode)) {
			return "17";// 商票（纸质）
		} else if ("7".equals(ebsCode)) {
			return "14";// 商票（网银）
		}
		return null;
	}

	/**
	 * 费用付款单表头空值检查
	 * 
	 * @param headvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkHeadTransforExpense(ApplyBillHeadVO headvo, String srctype)
			throws BusinessException {
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("所属组织不可为空");
		}
		if (StringUtils.isBlank(headvo.getFinstatus())) {
			throw new BusinessException("地区财务审核状态不可为空");
		}
		if (StringUtils.isBlank(headvo.getDef40())) {
			throw new BusinessException("发票是否为0字段不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("EBS主键不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsbillcode())) {
			throw new BusinessException("EBS申请编号不可为空");
		}
		// if (StringUtils.isBlank(headvo.getImagecode())) {
		// throw new BusinessException("影像编码不可为空");
		// }
		// if (StringUtils.isBlank(headvo.getImagestatus())) {
		// throw new BusinessException("影像状态不可为空");
		// }
		if (StringUtils.isBlank(headvo.getContractcode())) {
			throw new BusinessException("合同编号不可为空");
		}
		if (StringUtils.isBlank(headvo.getContractname())) {
			throw new BusinessException("合同名称不可为空");
		}
		if (StringUtils.isBlank(headvo.getContracttype())) {
			throw new BusinessException("合同类型不可为空");
		}
		if (StringUtils.isBlank(headvo.getEmergency())) {
			throw new BusinessException("紧急程度不可为空");
		}
		if (StringUtils.isBlank(headvo.getContractsubclass())) {
			throw new BusinessException("合同细类不可为空");
		}
		if (StringUtils.isBlank(headvo.getDept())) {
			throw new BusinessException("经办部门不可为空");
		}
		if (StringUtils.isBlank(headvo.getReceiver())) {
			throw new BusinessException("收款方不可为空");
		}
		if (StringUtils.isBlank(headvo.getOperator())) {
			throw new BusinessException("经办人不可为空");
		}
		if (StringUtils.isBlank(headvo.getBalatype())) {
			throw new BusinessException("结算方式不可为空");
		}
		if (StringUtils.isBlank(headvo.getApplymoney())) {
			throw new BusinessException("本次请款金额不可为空");
		}
		if (StringUtils.isBlank(headvo.getTotalamount())) {
			throw new BusinessException("累计付款金额不可为空");
		}
		if (StringUtils.isBlank(headvo.getTotalapplyamount())) {
			throw new BusinessException("累计请款金额不可为空");
		}
		if (StringUtils.isBlank(headvo.getPlate())) {
			throw new BusinessException("板块不可为空");
		}
		if (StringUtils.isBlank(headvo.getAccountingorg())) {
			throw new BusinessException("出账公司不可为空");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getBankaccount())) { throw new
		 * BusinessException("收款方账户不可为空"); }
		 */
		if (StringUtils.isBlank(getPk_orgByCode(headvo.getOrg()))) {
			throw new BusinessException("收款方账户不可为空");
		}
		if (StringUtils.isBlank(headvo.getPk_currtype())) {
			throw new BusinessException("币种不可为空");
		}
		if (StringUtils.isBlank(headvo.getIsbuyticket())) {
			throw new BusinessException("是否先付款后补票不可为空");
		}
		if (StringUtils.isBlank(headvo.getDef11())) {
			throw new BusinessException("合同ID不可为空");
		}
	}

	/**
	 * 费用付款单表体空值检查
	 * 
	 * @param bodyvo
	 * @throws BusinessException
	 */
	private void checkBodyTransforExpense(ApplyBillBodyVO bodyvo, String srctype)
			throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getPayamount())) {
			throw new BusinessException("申请金额不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("供应商不可为空");
		}
		// if (StringUtils.isBlank(bodyvo.getRealitybankraccount())) {throw new
		// BusinessException("实际收款方银行账户不可为空");}
	}

	/**
	 * 成本付款单表头空值检查
	 * 
	 * @param headvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkHeadTransforCost(CostApplyBillHeadVO headvo,
			String srctype) throws BusinessException {
		if (StringUtils.isBlank(headvo.getDef47())) {
			throw new BusinessException("抵楼标识不可为空");
		}
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("财务组织不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("EBS主键不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsbillcode())) {
			throw new BusinessException("EBS申请编号不可为空");
		}
		// if (StringUtils.isBlank(headvo.getImagecode())) {
		// throw new BusinessException("影像编码不可为空");
		// }
		// if (StringUtils.isBlank(headvo.getImagestatus())) {
		// throw new BusinessException("影像状态不可为空");
		// }
		if (StringUtils.isBlank(headvo.getContractcode())) {
			throw new BusinessException("合同编号不可为空");
		}
		if (StringUtils.isBlank(headvo.getContractname())) {
			throw new BusinessException("合同名称不可为空");
		}
		// if (StringUtils.isBlank(headvo.getContractsubclass())) {throw new
		// BusinessException("合同细类不可为空");}
		if (StringUtils.isBlank(headvo.getEmergency())) {
			throw new BusinessException("紧急程度不可为空");
		}
		// if (StringUtils.isBlank(headvo.getDept())) {throw new
		// BusinessException("经办部门编码不可为空");}
		// if (StringUtils.isBlank(headvo.getOperator())) {throw new
		// BusinessException("经办人编码不可为空");}
		if (StringUtils.isBlank(headvo.getBalatype())) {
			throw new BusinessException("结算方式不可为空");
		}
		if (StringUtils.isBlank(headvo.getTotalapplymoney())) {
			throw new BusinessException("累计请款金额不可为空");
		}
		if (StringUtils.isBlank(headvo.getPlate())) {
			throw new BusinessException("板块不可为空");
		}
		// if (StringUtils.isBlank(headvo.getAccountingorg())) {throw new
		// BusinessException("出账公司不可为空");}
		if (StringUtils.isBlank(headvo.getProject())) {
			throw new BusinessException("项目不可为空");
		}
		if (StringUtils.isBlank(headvo.getTotalamount())) {
			throw new BusinessException("累计付款金额不可为空");
		}
		if (StringUtils.isBlank(headvo.getApplytotalamount())) {
			throw new BusinessException("本次请款累计付款金额不可为空");
		}
		if (StringUtils.isBlank(headvo.getIsmargin())) {
			throw new BusinessException("是否质保金扣除不可为空");
		}
		if (StringUtils.isBlank(headvo.getTotalnotemoney())) {
			throw new BusinessException("累计发票金额不可为空");
		}
		// if (StringUtils.isBlank(headvo.getExplain())) {throw new
		// BusinessException("说明不可为空");}
		// if (StringUtils.isBlank(headvo.getProjectstages())) {throw new
		// BusinessException("项目分期不可为空");}
		if (StringUtils.isBlank(headvo.getFinstatus())) {
			throw new BusinessException("地区财务审核状态不可为空");
		}
		// if (StringUtils.isBlank(headvo.getSignorg())) {throw new
		// BusinessException("签约公司不可为空");}
	}

	/**
	 * 成本付款单表体空值检查
	 * 
	 * @param bodyvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkBodyTransforCost(CostApplyBillBodyVO bodyvo,
			String srctype) throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getCostsubjects())) {
			throw new BusinessException("成本科目不可为空");
		}
		// if (StringUtils.isBlank(bodyvo.getSubjcode())) {throw new
		// BusinessException("收支项目不可为空");}
		if (StringUtils.isBlank(bodyvo.getRecaccount())) {
			throw new BusinessException("收款银行账户不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getPayamount())
				|| "0".equals(bodyvo.getPayamount())) {
			throw new BusinessException("付款金额不可为空且不能为0");
		}
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("实际收款方不可为空");
		}
		// if (StringUtils.isBlank(bodyvo.getProceedtype())) {
		// throw new BusinessException("款项类型不可为空");
		// }
	}

	/**
	 * SRM付款单表头空值检查
	 * 
	 * @param headvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkHeadTransforInside(InsideApplyBillHeadVO headvo,
			String srctype) throws BusinessException {
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("财务组织不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("付款申请单主键不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsbillcode())) {
			throw new BusinessException("付款申请单编号不可为空");
		}

		// if (StringUtils.isBlank(headvo.getImagecode())) {
		// throw new BusinessException("影像编码不可为空");
		// }
		// if (StringUtils.isBlank(headvo.getImagestatus())) {
		// throw new BusinessException("影像状态不可为空");
		// }
		/*
		 * if (StringUtils.isBlank(headvo.getPurchasecode())) { throw new
		 * BusinessException("采购协议编码不可为空"); } if
		 * (StringUtils.isBlank(headvo.getPurchasename())) { throw new
		 * BusinessException("采购协议名称不可为空"); }
		 */
		if (StringUtils.isBlank(headvo.getEmergency())) {
			throw new BusinessException("紧急程度不可为空");
		}
		if (StringUtils.isBlank(headvo.getBalatype())) {
			throw new BusinessException("结算方式不可为空");
		}
		if (StringUtils.isBlank(headvo.getContractsign())) {
			throw new BusinessException("合同签约方不可为空");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getDeptname())) { throw new
		 * BusinessException("经办部门编码不可为空"); } if
		 * (StringUtils.isBlank(headvo.getOperatorname())) { throw new
		 * BusinessException("经办人编码不可为空"); }
		 */
		if (StringUtils.isBlank(headvo.getFinstatus())) {
			throw new BusinessException("地区财务审核状态不可为空");
		}
		if (StringUtils.isBlank(headvo.getMoney())) {
			throw new BusinessException("申请总金额不可为空");
		}
		if (StringUtils.isBlank(headvo.getPaymentletter())) {
			throw new BusinessException("非标准指定付款涵不可为空");
		}
		if (StringUtils.isBlank(headvo.getRepairannex())) {
			throw new BusinessException("补附件不可为空");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getCompleted())) { throw new
		 * BusinessException("已补全不可为空"); }
		 */
	}

	/**
	 * SRM付款单表体空值检查
	 * 
	 * @param bodyvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkBodyTransforInside(InsideApplyBillBodyVO bodyvo,
			String srctype) throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getPayamount())) {
			throw new BusinessException("申请金额不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getProceedtype())) {
			throw new BusinessException("款项类型不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("供应商不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getRecaccount())) {
			throw new BusinessException("收款银行账户不可为空");
		}
	}

	private void InspectionContract(String contractcode, String contractname)
			throws BusinessException {

		BaseDAO dao = new BaseDAO();
		StringBuffer query = new StringBuffer();
		query.append("select a.pk_fct_ap from fct_ap a  where a.vbillcode = '"
				+ contractcode + "' and a.ctname = '" + contractname
				+ "'  and a.blatest = 'Y' and dr = 0  ");

		String pk_fct_ap = (String) dao.executeQuery(query.toString(),
				new ColumnProcessor());

		if ("".equals(pk_fct_ap) || pk_fct_ap == null) {
			throw new BusinessException("请先同步合同到nc，合同编码：" + contractcode
					+ "，合同名称：" + contractname + "");
		}
	}

}
