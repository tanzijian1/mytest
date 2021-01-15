package nc.bs.tg.outside.ctap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.calulate.CalultateUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.ITGSyncService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.outside.LLCtApJsonBVO;
import nc.vo.tg.outside.LLCtApJsonVO;
import nc.vo.tg.outside.LLCtApPlanJsonVO;

import com.alibaba.fastjson.JSONObject;

/**
 * 收款合同工具类入口
 * 
 * @since 2020-07-24
 * @author 谈子健
 * 
 */
public class CtApBillUtil extends BillUtils implements ITGSyncService {
	static CtApBillUtil utils;
	String userid = null;// 操作用户

	public static CtApBillUtil getUtils() {
		if (utils == null) {
			utils = new CtApBillUtil();
		}
		return utils;
	}

	/**
	 * 收款合同
	 * 
	 * @param value
	 * @param dectype
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		AggCtApVO billvos[] = null;
		Map valueMap = new HashMap<>();
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(DefaultOperator));
		// 设置用户码
		InvocationInfoProxy.getInstance().setUserCode(DefaultOperator);

		// 处理表单信息
		JSONObject jsonData = (JSONObject) info.get("data");// 表单数据
		String jsonhead = jsonData.getString("headInfo");// 外系统来源表头数据
		if (jsonData == null || jsonhead == null) {
			throw new BusinessException("表单数据或表头信息为空，请检查！json:" + jsonData);
		}
		// 表头信息
		LLCtApJsonVO headVO = JSONObject.parseObject(jsonhead,
				LLCtApJsonVO.class);
		// 枚举，00 草稿、10 审批中、20 履行中、30 已作废、40 变更中、50 已完结
		String wflowstate = headVO.getWflowstate();
		String srcid = headVO.getSrcid();// 外系统业务单据ID
		String srcbillno = headVO.getSrcbillno();
		// 检查NC是否有相应的单据存在
		AggCtApVO aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
				"isnull(dr,0)=0 and blatest ='Y'  and vbillcode = '"
						+ srcbillno + "'");

		String hpk = null;
		if (aggVO != null) {
			hpk = aggVO.getParentVO().getPrimaryKey();
		}
		// 判断是否是作废的合同-2020-09-04-谈子健
		if ("30".equals(wflowstate)) {
			if (hpk != null && !"".equals(hpk)) {
				cancellationEbsContract(aggVO);
				valueMap.put("billno", aggVO.getParentVO().getVbillcode());
				valueMap.put("billid", aggVO.getParentVO().getPrimaryKey());
			} else {
				throw new BusinessException("单据号:" + srcid
						+ "相关的合同在NC不存在,不能进行作废合同的操作!");
			}

		} else {
			// 合同基本(科目拆分)
			List<LLCtApJsonBVO> ctApBvos = JSONObject.parseArray(
					jsonData.getString("ctapbvos"), LLCtApJsonBVO.class);

			if (jsonData == null || jsonhead == null) {
				throw new BusinessException("表单数据或表头信息为空，请检查！json:" + jsonData);
			}
			// 付款计划
			List<LLCtApPlanJsonVO> ctApPlanBvos = JSONObject.parseArray(
					jsonData.getString("ctapplans"), LLCtApPlanJsonVO.class);
			// 校验表头数据
			vaildHeadData(headVO);
			// 校验合同基本数据
			vaildctApBvosData(ctApBvos);
			// 校验付款计划数据
			vaildCtApPlanBodyData(ctApPlanBvos);

			String billqueue = methodname + ":" + srcid;
			BillUtils.addBillQueue(billqueue);
			try {

				AggCtApVO billvo = onTranBill(info, hpk);

				HashMap<String, Object> eParam = new HashMap<String, Object>();
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
						PfUtilBaseTools.PARAM_NOTE_CHECKED);

				if (billvo != null) {
					if (hpk != null && !"".equals(hpk)) {
						Object obj = getPfBusiAction().processAction("MODIFY",
								"FCT1", null, billvo, null, eParam);
						billvos = (AggCtApVO[]) obj;
					} else {
						billvos = ((AggCtApVO[]) getPfBusiAction()
								.processAction("SAVEBASE", "FCT1", null,
										billvo, null, eParam));

						billvo = (AggCtApVO) getMDQryService()
								.queryBillOfVOByPK(AggCtApVO.class,
										billvos[0].getPrimaryKey(), false);

						eParam.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER",
								new AggCtApVO[] { billvo });

						Object obj = getPfBusiAction().processAction(
								"APPROVE" + getSaleUserID(), "FCT1", null,
								billvo, null, eParam);

						getPfBusiAction().processAction(
								"VALIDATE" + getSaleUserID(), "FCT1", null,
								((AggCtApVO[]) obj)[0], null, eParam);
					}

				}

			} catch (Exception e) {
				throw new BusinessException(e.getMessage(), e);
			} finally {
				BillUtils.removeBillQueue(billqueue);
			}
			valueMap.put("billno", billvos[0].getParentVO().getVbillcode());
			valueMap.put("billid", billvos[0].getParentVO().getPrimaryKey());
		}
		return JSONObject.toJSON(valueMap).toString();

	}

	/**
	 * 查询
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getCustVO(String code) throws BusinessException {
		String s = (String) super.getBaseDAO().executeQuery(
				"select pk_cust_sup from bd_cust_supplier where code ='" + code
						+ "'", new ColumnProcessor());
		return s;
	}

	/**
	 * 非空数据校验
	 * 
	 * @param headvo
	 *            表头VO
	 */
	protected void vaildHeadData(LLCtApJsonVO headvo) throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (StringUtil.isEmpty(headvo.getSrcid())) {
			str.append("数据异常，表头Srcid外系统主键不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getSrcbillno())) {
			str.append("数据异常，表头Srcbillno外系统编号不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getOuflag())) {
			str.append("数据异常，表头Ouflag财务组织或出账公司不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getPlanbudgetname())) {
			str.append("数据异常，表头Planbudgetname预算主体不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getFcreatemode())) {
			str.append("数据异常，表头Fcreatemode合同属性不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getFcontracttypenew())) {
			str.append("数据异常，表头Fcontracttypenew合同类型不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getFcontractdetail())) {
			str.append("数据异常，表头Fcontractdetail合同细类不能为空！");
		}
		// if (StringUtil.isEmpty(headvo.getFnumber())) {
		// str.append("数据异常，表头Fnumber合同编码不能为空！");
		// }
		if (StringUtil.isEmpty(headvo.getFname())) {
			str.append("数据异常，表头Fname合同名称不能为空！");
		}

		if (StringUtil.isEmpty(headvo.getOrgvendorname())) {
			str.append("数据异常，表头Orgvendorname甲方不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVendorname())) {
			str.append("数据异常，表头Vendorname乙方不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getCreateddate())) {
			str.append("数据异常，表头Createddate创建日期不能为空！");
		}
		// if (StringUtil.isEmpty(headvo.getContractoperatorname())) {
		// str.append("数据异常，表头Contractoperatorname合同管理人不能为空！");
		// }
		// if (StringUtil.isEmpty(headvo.getContractmanagerdepartment())) {
		// str.append("数据异常，表头contractmanagerdepartment合同管理人部门不能为空！");
		// }
		if (StringUtil.isEmpty(headvo.getAgent())) {
			str.append("数据异常，表头agent经办人不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getOperatordepartment())) {
			str.append("数据异常，表头operatordepartment经办人部门不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getWflowstate())) {
			str.append("数据异常，表头WflowstateEBS合同状态不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getLldatasources())) {
			str.append("数据异常，表头Lldatasources数据来源不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getLlpayperiod())) {
			str.append("数据异常，表头Llpayperiod付款周期不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getLlsharecontractflag())) {
			str.append("数据异常，表头llsharecontractflag是否分摊合同不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getLlperiodaccrualflag())) {
			str.append("数据异常，表头llperiodaccrualflag是否周期计提类合同不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getLlsubcontractflag())) {
			str.append("数据异常，表头llsubcontractflag是否为分包合同不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getCvendorid())) {
			str.append("数据异常，表头cvendorid收款方不能为空！");
		}
		// if (StringUtil.isEmpty(headvo.getIsreimbursement())) {
		// str.append("数据异常，表头isreimbursement是否已报销不能为空！");
		// }

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * 创建表头vo
	 * 
	 * @param srctype
	 *            收入合同类型
	 * @return
	 * @throws BusinessException
	 */
	protected CtApVO buildHeadVo(OrgVO orgvo, Object[] defpks,
			LLCtApJsonVO headvo) throws BusinessException {
		CtApVO ctApVO = new CtApVO();
		if (defpks != null) {
			ctApVO.setDepid(defpks[0].toString());// 部门
			ctApVO.setDepid_v(defpks[1].toString());// 部门版本
		}// 经办（承办）部门
		/**
		 * EBS传过来的字段-2020-08-06-谈子健-Start
		 */
		ctApVO.setDef49(headvo.getSrcid());// EBS合同主键
		ctApVO.setVbillcode(headvo.getSrcbillno());// 合同编码

		if (orgvo != null) {
			String pk_org = orgvo.getPk_org();
			String pk_vid = orgvo.getPk_vid();
			ctApVO.setPk_org(pk_org);// 财务组织(ebs出账公司)
			ctApVO.setPk_org_v(pk_vid);
			// 合同管理人
			Object[] psndoc = getPsndocByCode(headvo.getContractoperatorname());
			if (psndoc == null || psndoc.length < 0) {
				// throw new BusinessException("合同管理人Contractoperatorname编码:"
				// + headvo.getContractoperatorname() + "在NC找不到关联的人员信息!");
			} else {
				ctApVO.setConadmin((String) psndoc[0]);// 合同管理人名称
				ctApVO.setDef110((String) psndoc[1]);// 合同管理人主键
			}

		}
		// ctApVO.setPlate(getPlateByCode(headvo.getPlate()));// 板块
		ctApVO.setSubbudget(getSubbudgetByCode(headvo.getPlanbudgetname()));// 预算主体
		ctApVO.setBilltype(headvo.getFcreatemode());// 合同属性(ebs单据类型)
		ctApVO.setContype(headvo.getFcontracttypenew());// 合同类型（EBS合同类型）
		ctApVO.setCondetails(headvo.getFcontractdetail());// 合同细类
		ctApVO.setCtname(headvo.getFname());// 合同名称
		ctApVO.setB_lease("Y".equals(headvo.getIsleasecontract()) ? UFBoolean.TRUE
				: UFBoolean.FALSE);// 是否租赁合同
		ctApVO.setB_payed("Y".equals(headvo.getIsbond()) ? UFBoolean.TRUE
				: UFBoolean.FALSE);// 是否存在已付款项
		ctApVO.setB_stryear("Y".equals(headvo.getIscrossyear()) ? UFBoolean.TRUE
				: UFBoolean.FALSE);// 是否预算跨年合同
		ctApVO.setAccountorg(getAccountorgByCode(headvo.getOuflag()));// 出账公司
		ctApVO.setFirst(getSupplierByCode(headvo.getOrgvendorname()));// 甲方
		ctApVO.setSecond(getSupplierByCode(headvo.getVendorname()));// 乙方
		ctApVO.setThird(getSupplierByCode(headvo.getSecondparty1()));// 丙方
		ctApVO.setFourth(getSupplierByCode(headvo.getSecondparty2()));// 丁方
		ctApVO.setFifth(getSupplierByCode(headvo.getSecondparty3()));// 戊方
		ctApVO.setSixth(getSupplierByCode(headvo.getSecondparty4()));// 己方
		ctApVO.setVdef1(headvo.getLlfsecondparty5name());// 庚方
		ctApVO.setVdef2(headvo.getLlfsecondparty6name());// 辛方
		ctApVO.setD_creator(headvo.getCreateddate());// 创建日期
		// ctApVO.setPersonnelid(getPsndocByCode(headvo.getPersonnelid(),
		// orgvo.getPk_org(), defpks == null ? "" : defpks[0].toString()));//

		// ctApVO.setGrade(getGradeByCode(headvo.getGrade(),
		// orgvo.getPk_org()));// 经办（承办）职位
		ctApVO.setCon_abstract(headvo.getFdescription());// 合同摘要
		ctApVO.setVdef17(headvo.getSumboudamount());// 押金/保证金金额
		// ctApVO.setNtotalorigmny(new
		// UFDouble(headvo.getSumdynamicamount()));// 动态含税金额
		ctApVO.setMsign(new UFDouble(headvo.getFcontractmoney()));// 签约金额
		ctApVO.setVdef9(headvo.getSumsupamount());// 补充协议金额
		ctApVO.setVdef10(headvo.getSumdynamicamount());// 动态含税金额
		ctApVO.setEbscontractstatus(headvo.getWflowstate());// ebs合同状态
		ctApVO.setDef61(headvo.getIspresupposition());// 是否冲预提
		ctApVO.setDef38(headvo.getLldatasources());// 数据来源
		String llstartdate = headvo.getLlstartdate();
		if (llstartdate != null && !"".equals(llstartdate)) {
			ctApVO.setDef108(llstartdate);// 邻里合同开始日期
		}
		ctApVO.setValdate(new UFDate());// 合同开始日期

		String llenddate = headvo.getLlenddate();
		if (llenddate != null && !"".equals(llenddate)) {
			ctApVO.setDef109(llenddate);// 邻里合同结束日期
		}

		ctApVO.setInvallidate(new UFDate());// 合同结束日期

		ctApVO.setVdef13(headvo.getLlpayperiod());// 付款周期
		ctApVO.setVdef3(headvo.getLlbusinessperiod());// 业务所属期
		ctApVO.setDef32(headvo.getLlsharecontractflag());// 是否分摊合同
		ctApVO.setDef33(headvo.getLlperiodaccrualflag());// 是否周期计提类合同
		ctApVO.setDef34(headvo.getLlsubcontractflag());// 是否为分包合同
		ctApVO.setDef35(headvo.getLlmaincontractno());// 总包合同编码
		ctApVO.setDef36(headvo.getLlmaincontractname());// 总包合同名称
		ctApVO.setDef37(headvo.getLlMaincontractamt());// 总包合同金额
		String cvendorCode = headvo.getCvendorid();// 收款方
		String cvendorid = getSupplierByCode(cvendorCode);
		if (cvendorid == null || "".equals(cvendorid)) {
			throw new BusinessException("收款方" + cvendorCode + "在NC供应商没找到关联的信息!");
		}
		ctApVO.setCvendorid(cvendorid);
		ctApVO.setDef47(headvo.getAdvancebillno());// 预提单号
		ctApVO.setDef48(headvo.getAmountwithdrawn());// 本次冲预提金额
		ctApVO.setDef60(headvo.getIsdirectdebit()); // 是否自动扣款
		ctApVO.setDef61(headvo.getDeductiontype()); // 划扣类型
		ctApVO.setDef62(headvo.getSigningaccount()); // 签约账户
		ctApVO.setDef44(headvo.getIsreimbursement()); // 是否已报销
		ctApVO.setDef45(getPsndocPkByCode(headvo.getExpense_employee_number())); // 报销人编号
		// ctApVO.setFstatusflag(1);// 合同状态默认为生效

		ctApVO.setDef85(getDeptpksByCode(headvo.getContractmanagerdepartment()));// 合同管理人部门
		ctApVO.setDef86(getPsndocPkByCode(headvo.getAgent()));// 经办人
		ctApVO.setDef87(getDeptpksByCode(headvo.getOperatordepartment()));// 经办人部门

		ctApVO.setDef81(getPkContractByName(headvo.getFcontracttypenew(),
				"SDLL015"));// 合同类型
		ctApVO.setDef82(getPkContractByName(headvo.getFcontractdetail(),
				"SDLL016"));// 合同细类

		/**
		 * EBS传过来的字段-2020-08-06-谈子健-end
		 */
		// ********************表头参照信息设置***************
		// 表头默认信息配置
		ctApVO.setPk_group("000112100000000005FD");
		ctApVO.setRate(UFDouble.ONE_DBL);// 税率
		ctApVO.setCtrantypeid(getCtrantypeidByCode("FCT1-Cxx-LL01")); // 交易类型
		ctApVO.setCbilltypecode("FCT1"); // 单据类型
		ctApVO.setNexchangerate(new UFDouble(100));// 折本汇率
		ctApVO.setSubscribedate(new UFDate());// 盖章签字日期
		return ctApVO;

	}

	/**
	 * 处理新表体数据信息
	 * 
	 * @param clazz
	 *            voClass
	 * @param parentPKValue
	 *            NC付款合同表头主键值
	 * @param desAggVO
	 *            传参过来的aggVO(目标)
	 * @param bvoPKfiled
	 *            vo的主键字段名
	 * @param table
	 *            vo数据表名
	 * @throws BusinessException
	 */
	protected void syncBvoPkByEbsPk(Class<? extends ISuperVO> clazz,
			String parentPKValue, AggCtApVO desAggVO, String bvoPKfiled,
			String table) throws BusinessException {
		// 传参数的BVOs
		ISuperVO[] syncDesPkBVOs = desAggVO.getChildren(clazz);
		Map<String, String> info = getBvoPkByEbsMap("pk_fct_ap", bvoPKfiled,
				table, parentPKValue);
		List<String> list = new ArrayList<String>();
		if (info.size() > 0) {
			list.addAll(info.values());
		}
		if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
			for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
				tmpDesBVO.setStatus(VOStatus.NEW);
				tmpDesBVO.setAttributeValue("pk_fct_ap", parentPKValue);
				tmpDesBVO.setAttributeValue("pk_group", InvocationInfoProxy
						.getInstance().getGroupId());

			}
			if (list != null && list.size() > 0) {
				setVODelStatus(clazz, parentPKValue, desAggVO, bvoPKfiled,
						table, syncDesPkBVOs, list);
			}

		}
		if (syncDesPkBVOs == null && !info.isEmpty()) {
			setVODelStatus(clazz, parentPKValue, desAggVO, bvoPKfiled, table,
					syncDesPkBVOs, list);
		}
	}

	/**
	 * 处理表体删除标志
	 * 
	 * @param clazz
	 * @param parentPKValue
	 * @param desAggVO
	 * @param bvoPKfiled
	 * @param table
	 * @param syncDesPkBVOs
	 * @param list
	 * @throws BusinessException
	 */
	private void setVODelStatus(Class<? extends ISuperVO> clazz,
			String parentPKValue, AggCtApVO desAggVO, String bvoPKfiled,
			String table, ISuperVO[] syncDesPkBVOs, List<String> list)
			throws BusinessException {
		String condition = " pk_fct_ap='" + parentPKValue + "' and dr = 0   ";
		// 删除原来的数据
		String sqlwhere = "";
		for (String value : list) {
			sqlwhere += "'" + value + "',";
		}
		sqlwhere = sqlwhere.substring(0, sqlwhere.length() - 1);
		Collection<ISuperVO> coll = getBaseDAO().retrieveByClause(clazz,
				condition);
		if (coll != null && coll.size() > 0) {
			List<ISuperVO> bodyList = new ArrayList<ISuperVO>();
			if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
				bodyList.addAll(Arrays.asList(syncDesPkBVOs));
			}
			for (ISuperVO obj : coll) {
				SuperVO vo = (SuperVO) obj;
				vo.setStatus(VOStatus.DELETED);
				bodyList.add(vo);
			}
			desAggVO.setChildren(clazz, bodyList.toArray(new SuperVO[0]));
		}
	}

	/**
	 * <p>
	 * Title: getBvoPkByEbsPK<／p>
	 * <p>
	 * Description: <／p>
	 * 
	 * @param ebsPk
	 *            ebs对应页签数据行主键
	 * @param parentPKfield
	 *            NC付款合同表头主键字段
	 * @param bvoPKfiled
	 *            NC付款合同对应页签主键字段
	 * @param table
	 *            NC付款合同对应页签表名
	 * @param parentPkValue
	 *            NC付款合同表头主键值
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, String> getBvoPkByEbsMap(String parentPKfield,
			String bvoPKfiled, String table, String parentPkValue)
			throws BusinessException {
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = getBaseDAO();
		sql = "select ts," + bvoPKfiled + " pk from " + table + " where  "
				+ parentPKfield + " = ? and dr =0";
		Map<String, String> info = new HashMap<String, String>();
		parameter.addParam(parentPkValue);
		List<Map<String, String>> list = (List<Map<String, String>>) dao
				.executeQuery(sql, parameter, new MapListProcessor());
		if (list != null && list.size() > 0) {
			for (Map<String, String> map : list) {
				info.put(map.get("vbdef2"), map.get("pk"));
			}
		}
		return info;
	}

	protected AggCtApVO onTranBill(HashMap<String, Object> info, String hpk)
			throws BusinessException {
		return null;
	}

	/**
	 * 根据组织编码查询组织VO
	 * 
	 * @param orgCode
	 * @return
	 * @throws BusinessException
	 */
	protected OrgVO getOrgVO(String orgCode) throws BusinessException {
		OrgVO orgvo = (OrgVO) super.getBaseDAO().executeQuery(
				"select * from org_orgs where code ='" + orgCode + "'",
				new BeanProcessor(OrgVO.class));
		return orgvo;
	}

	/**
	 * 根据【部门编码】获取主键,版本信息
	 * 
	 * @param code
	 * @return
	 */
	protected String getDeptpksByCode(String code) throws BusinessException {
		String sql = "select pk_dept  from org_dept "
				+ "  where nvl(org_dept.dr,0)=0  and nvl(org_dept.dr,0)=0 and enablestate = '2' and org_dept.code='"
				+ code + "'";
		String pk_depts = null;
		try {
			pk_depts = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_depts != null) {
				return pk_depts;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 根据【交易类型编码】获取主键
	 * 
	 * @param code
	 * @return
	 */

	public String getBillTypePkByCode(String code, String pk_group) {
		String pk_billtypeid = null;
		String sql = "select pk_billtypeid from bd_billtype where nvl(dr,0)=0 and PK_BILLTYPECODE='"
				+ code + "' and pk_group='" + pk_group + "'";
		try {
			pk_billtypeid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (pk_billtypeid != null) {
			return pk_billtypeid;
		}
		return pk_billtypeid;

	}

	/**
	 * 根据编码或名称找客商
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	protected String getcustomer(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = null;
		try {
			obj = dao.executeQuery(
					"select pk_customer from bd_customer where (code='" + code
							+ "' or name='" + code + "') and nvl(dr,0)=0",
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return (String) obj;
	}

	/**
	 * 根据【人员编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPsndocPkByCode(String code) {
		StringBuffer query = new StringBuffer();
		query.append("select bd_psndoc.pk_psndoc  ");
		query.append("     from bd_psndoc  ");
		query.append("    where nvl(bd_psndoc.dr, 0) = 0  ");
		query.append("      and bd_psndoc.code = '" + code + "'  ");
		query.append("      and bd_psndoc.enablestate = 2  ");

		String pk = null;
		try {
			pk = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (pk != null && !pk.equals("")) {
			return pk;
		}
		return null;
	}

	/**
	 * 读取物业收费系统操作员默认用户
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getSaleUserID() throws BusinessException {
		if (userid == null) {
			String sql = "select cuserid from sm_user  where user_code = '"
					+ DefaultOperator + "'";
			userid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}
		return userid;
	}

	/**
	 * 
	 * @param ctApBvos
	 *            付款计划
	 * @throws BusinessException
	 */
	private void vaildCtApPlanBodyData(List<LLCtApPlanJsonVO> ctApPlanBvos)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (ctApPlanBvos == null || ctApPlanBvos.size() <= 0) {
			str.append("数据异常，付款计划页签不能为空！");
		} else {
			for (int i = 0; i < ctApPlanBvos.size(); i++) {
				if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
						.getPk_ebs()))) {
					str.append("数据异常，付款计划页签，第" + i + 1 + "行，pk_ebs行主键不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
						.getProceedtype()))) {
					str.append("数据异常，付款计划页签，第" + i + 1
							+ "行，proceedtype请款类型不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
						.getPaymentamount()))) {
					str.append("数据异常，付款计划页签，第" + i + 1
							+ "行，paymentamount计划付款金额不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
						.getCharactertype()))) {
					str.append("数据异常，付款计划页签，第" + i + 1
							+ "行，charactertype款项性质不能为空！");
				}

				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getReturnconditions()))) {
				// str.append("数据异常，付款计划页签，第" + i + 1
				// + "行，returnconditions退回/解付条件不能为空！");
				// }

				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getLineamountam()))) {
				// str.append("数据异常，付款计划页签，第" + i + 1
				// + "行，lineamountam累计请款金额不能为空！");
				// }
				//
				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getPaidamount()))) {
				// str.append("数据异常，付款计划页签，第" + i + 1
				// + "行，paidamount累计已付款金额不能为空！");
				// }

				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getFollowup()))) {
				// str.append("数据异常，付款计划页签，第" + i + 1 + "行，followup后续处理不能为空！");
				// }
				//
				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getExpecteddate()))) {
				// str.append("数据异常，付款计划页签，第" + i + 1
				// + "行，expecteddate预计退回/解付日期不能为空！");
				// }
				//
				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getReturnconditions()))) {
				// str.append("数据异常，付款计划页签，第" + i + 1
				// + "行，returnconditions退回/解付条件不能为空！");
				// }
				//
				// if (StringUtil.isEmpty(String.valueOf(ctApPlanBvos.get(i)
				// .getRefunddate()))) {
				// str.append("数据异常，付款计划页签，第" + i + 1
				// + "行，refunddate收到退款日期不能为空！");
				// }

			}
		}

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * 
	 * @param ctApBvos
	 *            合同基本信息页签
	 * @throws BusinessException
	 */
	private void vaildctApBvosData(List<LLCtApJsonBVO> ctApBvos)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (ctApBvos == null || ctApBvos.size() <= 0) {
			str.append("数据异常，收款合同表体基本页签不能为空！");
		} else {
			for (int i = 0; i < ctApBvos.size(); i++) {
				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getFbudgetsubjectname()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1
							+ "行，Fbudgetsubjectname行科目名称不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getPk_ebs()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1 + "行，pk_ebs行主键不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getSplitratio()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1
							+ "行，splitratio拆分比例不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getNotaxmny()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1 + "行，amount金额不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getIspresupposed()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1
							+ "行，ispresupposed是否预提不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getIspresupposed()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1
							+ "行，ispresupposed是否预提不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getBudgetyears()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1
							+ "行，budgetyears预算年度不能为空！");
				}

				if (StringUtil.isEmpty(String.valueOf(ctApBvos.get(i)
						.getNcprojectname()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1
							+ "行，ncprojectname项目不能为空！");
				}
			}
		}

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * 查询板块
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getPlateByCode(String code) throws BusinessException {
		// 板块
		StringBuffer query = new StringBuffer();
		query.append("select pk_defdoc  ");
		query.append("  from bd_defdoc  ");
		query.append(" where code = '" + code + "'  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and (pk_defdoclist =  ");
		query.append("       (select l.pk_defdoclist from bd_defdoclist l where l.code = 'bkxx'))  ");

		String plate = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return plate;

	}

	/**
	 * 查询预算主体
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getSubbudgetByCode(String code) throws BusinessException {

		StringBuffer query = new StringBuffer();
		query.append("select PK_PLANBUDGET from org_planbudget where code = '"
				+ code + "' and nvl(dr,0) = 0 and enablestate = 2;  ");

		String subbudget = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return subbudget;

	}

	/**
	 * 查询出账公司
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getAccountorgByCode(String code) throws BusinessException {

		StringBuffer query = new StringBuffer();
		query.append("select pk_financeorg  ");
		query.append("  from org_financeorg  ");
		query.append(" where code = '" + code + "'  ");
		query.append("   and nvl(dr, 0) = 0  ");
		query.append("   and enablestate = 2;  ");

		String accountorg = (String) getBaseDAO().executeQuery(
				query.toString(), new ColumnProcessor());
		return accountorg;

	}

	/**
	 * 根据code查询供应商基本信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getSupplierByCode(String code) throws BusinessException {
		// 根据code查询供应商基本信息
		StringBuffer query = new StringBuffer();
		query.append("SELECT pk_supplier  ");
		query.append("  FROM bd_supplier  ");
		query.append(" WHERE CODE = '" + code + "'  ");
		query.append("   and enablestate = '2'  ");
		query.append("   and dr = 0  ");
		String supplier = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return supplier;
	}

	/**
	 * 根据code查询经办（承办）职位
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getGradeByCode(String code, String pk_org)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_post  ");
		query.append("  from om_post  ");
		query.append(" where postcode = '" + code + "'  ");
		query.append("   and enablestate = 2  ");
		query.append("   and pk_org = '" + pk_org + "'  ");

		String grade = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return grade;
	}

	/**
	 * 根据code查询经办（承办）人员
	 * 
	 * @param code
	 * @param pk_org
	 * @param defpk
	 * @return
	 * @throws BusinessException
	 */
	private Object[] getPsndocByCode(String code) throws BusinessException {
		Object[] psndoc = null;

		StringBuffer query = new StringBuffer();
		query.append("select bd_psndoc.name,bd_psndoc.pk_psndoc ");
		query.append("  from bd_psndoc  ");
		query.append(" where bd_psndoc.code = '" + code + "'  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and dr = 0  ");

		psndoc = (Object[]) getBaseDAO().executeQuery(query.toString(),
				new ArrayProcessor());

		return psndoc;
	}

	/**
	 * 根据code查询交易类型主键
	 * 
	 * @param code
	 * @param pk_org
	 * @param defpk
	 * @return
	 * @throws BusinessException
	 */
	private String getCtrantypeidByCode(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_billtypeid  ");
		query.append("  from bd_billtype  ");
		query.append(" where nvl(dr, 0) = 0  ");
		query.append("   and PK_BILLTYPECODE = '" + code + "';  ");

		String ctrantypeid = (String) getBaseDAO().executeQuery(
				query.toString(), new ColumnProcessor());
		return ctrantypeid;
	}

	/**
	 * 根据编码获取科目名称主键 2020-08-10-谈子健
	 * 
	 * @throws BusinessException
	 */
	public String getPk_objByCode(String objcode, String pk_org)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_obj  ");
		query.append("  from tb_budgetsub  ");
		query.append(" where objcode = '" + objcode + "'  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and ((pk_org = '" + pk_org
				+ "' or pk_group = '000112100000000005FD'))  ");
		String pk_obj = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_obj;
	}

	/**
	 * EBS合同作废-2020-09-04
	 * 
	 * @author 谈子健
	 * 
	 */
	public void cancellationEbsContract(AggCtApVO aggVO)
			throws BusinessException {
		// 获取合同编号
		String billno = aggVO.getParentVO().getVbillcode();
		if (billno != null && !"".equals(billno)) {
			// 获取llperiodaccrualflag 是否周期计提类合同
			String llperiodaccrualflag = aggVO.getParentVO().getDef33();

			if (llperiodaccrualflag != null && !"".equals(llperiodaccrualflag)) {
				// 如果是周期性合同作废，NC生成红字应付单冲该合同之前周期性计提的累计金额
				if ("Y".equals(llperiodaccrualflag)) {
					// 查询合同关联的周期计提应付单
					Collection<AggregatedValueObject> billVOs = getpayableBillVO(
							AggPayableBillVO.class,
							"isnull(dr,0)=0 and def5 = '" + billno
									+ "' and pk_tradetype ='F1-Cxx-LL04'");
					for (AggregatedValueObject vo : billVOs) {
						AggPayableBillVO payableBillvo = (AggPayableBillVO) vo;
						AggPayableBillVO targetBill = changeBill(payableBillvo);
						HashMap eParam = new HashMap();
						eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
								PfUtilBaseTools.PARAM_NOTE_CHECKED);
						WorkflownoteVO worknoteVO = getWorkflowMachine()
								.checkWorkFlow(
										"SAVE",
										targetBill.getHeadVO()
												.getPk_tradetype(), targetBill,
										new HashMap<String, Object>());
						Object obj = getPfBusiAction().processAction("SAVE",
								targetBill.getHeadVO().getPk_tradetype(),
								worknoteVO, targetBill, null, null);
					}
				}
			}
			// 终止合同操作
			// TERMINATE
			getPfBusiAction().processAction("TERMINATE", "FCT1", null, aggVO,
					null, null);
		}

	}

	public AggPayableBillVO changeBill(AggPayableBillVO payableBillvo)
			throws BusinessException {
		UFDouble money = UFDouble.ZERO_DBL;// 金额
		UFDouble local_money = UFDouble.ZERO_DBL;// 组织金额
		UFDouble group_money = UFDouble.ZERO_DBL;// 集团金额
		UFDouble global_money = UFDouble.ZERO_DBL;// 全局金额
		AggPayableBillVO targetAggVo = payableBillvo.clone();
		PayableBillVO targetHvo = (PayableBillVO) targetAggVo.getParentVO();
		targetHvo.setPk_payablebill(null);
		targetHvo.setBillno(null);
		targetHvo.setAttributeValue(PayableBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// 集团
		targetHvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// 期初标志
		targetHvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.TRUE);// 是否红冲过
		targetHvo.setAttributeValue(PayableBillVO.BILLDATE, new UFDate());// 单据日期
		targetHvo.setAttributeValue(PayableBillVO.BUSIDATE, new UFDate());// 起算日期
		targetHvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate().getYear()));// 单据会计年度
		targetHvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate().getStrMonth());// 单据会计期间
		targetHvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);

		targetHvo.setAttributeValue(PayableBillVO.CREATIONTIME, new UFDate());// 创建时间

		targetHvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, "F1-Cxx-LL09");// 应收类型code

		// 明细信息转换
		PayableBillItemVO[] childrenVOs = (PayableBillItemVO[]) targetAggVo
				.getChildrenVO();
		if (childrenVOs != null && childrenVOs.length > 0) {
			for (int i = 0; i < childrenVOs.length; i++) {
				try {
					PayableBillItemVO payableBillItemVO = childrenVOs[i];
					payableBillItemVO.setPk_payablebill(null);
					payableBillItemVO.setPk_payableitem(null);
					UFDouble money_cr = payableBillItemVO.getMoney_cr();
					if (money_cr != null) {
						money_cr = money_cr.multiply(-1);
						payableBillItemVO.setMoney_cr(money_cr);
					}
					CalultateUtils.getUtils().calculate(targetAggVo,
							IBillFieldGet.MONEY_CR, i, Direction.CREDIT.VALUE);

					UFDouble local_tax_cr = payableBillItemVO.getLocal_tax_cr();
					if (local_tax_cr != null) {
						local_tax_cr = local_tax_cr.multiply(-1);
						payableBillItemVO.setLocal_tax_cr(local_tax_cr);
					}
					CalultateUtils.getUtils().calculate(targetAggVo,
							IBillFieldGet.LOCAL_TAX_CR, i,
							Direction.CREDIT.VALUE);
					money = money.add(payableBillItemVO.getMoney_cr());// 金额
					local_money = local_money.add(payableBillItemVO
							.getLocal_money_cr());// 组织金额
					group_money = group_money.add(payableBillItemVO
							.getGroupcrebit());// 集团金额
					global_money = global_money.add(payableBillItemVO
							.getGlobalcrebit());// 全局金额

				} catch (BusinessException e) {
					throw new BusinessException("行[" + (i + 1) + "],"
							+ e.getMessage(), e);
				}

			}

			targetHvo.setAttributeValue(PayableBillVO.MONEY, money);
			targetHvo.setAttributeValue(PayableBillVO.LOCAL_MONEY, local_money);
			targetHvo.setAttributeValue(PayableBillVO.GROUPLOCAL, group_money);
			targetHvo
					.setAttributeValue(PayableBillVO.GLOBALLOCAL, global_money);

		}
		return targetAggVo;

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
	public Collection<AggregatedValueObject> getpayableBillVO(Class c,
			String whereCondStr) throws BusinessException {
		Collection<AggregatedValueObject> aggVos = getMDQryService()
				.queryBillOfVOByCond(c, whereCondStr, false);
		if (aggVos.size() > 0) {
			return aggVos;
		} else {
			return null;
		}
	}

	/**
	 * 查询合同类型和细类
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private String getPkContractByName(String name, String listcode)
			throws BusinessException {

		StringBuffer query = new StringBuffer();
		query.append("select pk_defdoc  ");
		query.append(" from bd_defdoc  ");
		query.append(" where bd_defdoc.name = '" + name + "'");
		query.append(" and bd_defdoc.enablestate = 2  ");
		query.append(" and bd_defdoc.dr  =0  ");
		query.append(" and pk_defdoclist in  ");
		query.append(" (select pk_defdoclist from bd_defdoclist where code = '"
				+ listcode + "');");
		String pk_defdoc = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_defdoc;

	}

}
