package nc.bs.tg.outside.ctar;

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
import nc.itf.tg.outside.ITGSyncService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.fct.ar.entity.AggCtArVO;
import nc.vo.fct.ar.entity.CtArVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.LLCtArJsonBVO;
import nc.vo.tg.outside.LLCtArJsonVO;
import nc.vo.tg.outside.LLCtArPlanJsonVO;

import com.alibaba.fastjson.JSONObject;

/**
 * 收款合同工具类入口
 * 
 * @since 2020-07-24
 * @author 谈子健
 * 
 */
public class CtArBillUtil extends BillUtils implements ITGSyncService {
	static CtArBillUtil utils;
	public static final String DefaultOperator = "LLWYSF";// 默认制单人
	String userid = null;// 操作用户

	public static CtArBillUtil getUtils() {
		if (utils == null) {
			utils = new CtArBillUtil();
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
		AggCtArVO billvos[] = null;
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
		LLCtArJsonVO headVO = JSONObject.parseObject(jsonhead,
				LLCtArJsonVO.class);

		// 合同基本
		List<LLCtArJsonBVO> ctArBvos = JSONObject.parseArray(
				jsonData.getString("ctarbvos"), LLCtArJsonBVO.class);

		// 收款计划
		List<LLCtArPlanJsonVO> ctArPlanBvos = JSONObject.parseArray(
				jsonData.getString("ctarplans"), LLCtArPlanJsonVO.class);

		// 校验表头数据
		vaildHeadData(headVO);
		// 校验收款计划数据
		vaildCtArPlanBodyData(ctArPlanBvos);
		// 校验合同基本数据
		vaildCtArBvosData(ctArBvos);

		String srcid = headVO.getSrcid();// 外系统业务单据ID

		String billqueue = methodname + ":" + srcid;
		BillUtils.addBillQueue(billqueue);
		try {
			AggCtArVO aggVO = (AggCtArVO) getBillVO(AggCtArVO.class,
					"isnull(dr,0)=0 and blatest ='Y' and vdef18 = '" + srcid
							+ "'");

			String hpk = null;
			if (aggVO != null) {
				if (aggVO != null) {
					hpk = aggVO.getParentVO().getPrimaryKey();
				}
			}

			AggCtArVO billvo = onTranBill(info, hpk);

			Map valueMap = new HashMap<>();
			HashMap<String, Object> eParam = new HashMap<String, Object>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			if (billvo != null) {
				if (hpk != null && !"".equals(hpk)) {
					Object obj = getPfBusiAction().processAction("MODIFY",
							"FCT2", null, billvo, null, eParam);
					billvos = (AggCtArVO[]) obj;

				} else {
					billvos = ((AggCtArVO[]) getPfBusiAction().processAction(
							"SAVEBASE", "FCT2", null, billvo, null, eParam));

					eParam.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER",
							new AggCtArVO[] { billvos[0] });

					AggCtArVO[] aggvo = (AggCtArVO[]) getPfBusiAction()
							.processAction("APPROVE" + getSaleUserID(), "FCT2",
									null, billvos[0], null, eParam);
					Object processAction = getPfBusiAction().processAction(
							"VALIDATE" + getSaleUserID(), "FCT2", null,
							aggvo[0], null, eParam);

				}

			}

			valueMap.put("billno", billvos[0].getParentVO().getVbillcode());
			valueMap.put("billid", billvos[0].getParentVO().getPrimaryKey());
			return JSONObject.toJSON(valueMap).toString();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}

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
	protected void vaildHeadData(LLCtArJsonVO headvo) throws BusinessException {
		StringBuffer str = new StringBuffer();
		// if (StringUtil.isEmpty(headvo.getCbilltypecode())) {
		// str.append("数据异常，表头单据类型不能为空！");
		// }
		if (StringUtil.isEmpty(headvo.getVtrantypecode())) {
			str.append("数据异常，表头合同类型不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getDbilldate())) {
			str.append("数据异常，表头创建日期不能为空！");
		}
		// if (StringUtil.isEmpty(headvo.getCorigcurrencyid())) {
		// str.append("数据异常，表头币种不能为空！");
		// }
		if (StringUtil.isEmpty(headvo.getCtname())) {
			str.append("数据异常，表头合同名称不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getVbillcode())) {
			str.append("数据异常，表头合同编码不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getSrcid())) {
			str.append("数据异常，表头外系统主键不能为空！");
		}
		if (StringUtil.isEmpty(headvo.getSrcbillno())) {
			str.append("数据异常，表头外系统编号不能为空！");
		}
		// if (StringUtil.isEmpty(headvo.getVdef3())) {
		// str.append("数据异常，表头甲方不能为空！");
		// }
		// if (StringUtil.isEmpty(headvo.getVdef4())) {
		// str.append("数据异常，表头乙方不能为空！");
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
	protected CtArVO buildHeadVo(OrgVO orgvo, Object[] defpks,
			LLCtArJsonVO headvo) throws BusinessException {
		CtArVO ctarvo = new CtArVO();
		ctarvo.setBankaccount(headvo.getBankaccount());
		ctarvo.setPk_org(orgvo.getPk_org());// 财务组织
		ctarvo.setPk_org_v(orgvo.getPk_vid());// 组织版本
		ctarvo.setCbilltypecode("FCT2"); // 业务类型
		ctarvo.setVtrantypecode(headvo.getVtrantypecode());
		ctarvo.setDbilldate(new UFDate(headvo.getDbilldate()));// 创建日期
		ctarvo.setCcurrencyid("1002Z0100000000001K1");
		ctarvo.setCorigcurrencyid("1002Z0100000000001K1");// 币种
		ctarvo.setCrececountryid("0001Z010000000079UJJ");// 收货国家地区
		ctarvo.setCsendcountryid("0001Z010000000079UJJ");// 发货国家地区
		ctarvo.setCtaxcountryid("0001Z010000000079UJJ");// 报税国家地区
		ctarvo.setVbillcode(headvo.getVbillcode());// 合同编码
		ctarvo.setCtrantypeid(getBillTypePkByCode(headvo.getVtrantypecode(),
				orgvo.getPk_group()));// 交易类型
		ctarvo.setPk_customer(getcustomer(headvo.getPk_customer()));// 客户
		ctarvo.setCtname(headvo.getCtname());// 合同名称
		ctarvo.setFbuysellflag(1);// 购销类型
		ctarvo.setPersonnelid(getPsndocPkByCode(headvo.getPersonnelid()));// 经办人
		if (defpks != null) {
			ctarvo.setDepid(defpks[0].toString());// 部门
			ctarvo.setDepid_v(defpks[1].toString());// 部门版本
		}
		ctarvo.setValdate(headvo.getValdate() == null ? new UFDate()
				: new UFDate(headvo.getValdate()));// 计划生效时间
		ctarvo.setInvallidate(headvo.getInvallidate() == null ? new UFDate()
				: new UFDate(headvo.getInvallidate()));// 计划终止时间
		ctarvo.setSubscribedate(headvo.getSubscribedate() == null ? new UFDate()
				: new UFDate(headvo.getSubscribedate()));// 合同签订日志
		ctarvo.setNexchangerate(new UFDouble(1.00));
		// 组织部部门，人员，信息
		ctarvo.setOrganizer(orgvo.getPk_org());// 承办组织
		ctarvo.setOrganizer_v(orgvo.getPk_vid());// 承办组织版本
		ctarvo.setPk_group(orgvo.getPk_group());
		ctarvo.setPk_org(orgvo.getPk_org());
		ctarvo.setPk_org_v(orgvo.getPk_vid());
		ctarvo.setSignorg(orgvo.getPk_org());
		ctarvo.setSignorg_v(orgvo.getPk_vid());
		ctarvo.setOverrate(UFDouble.ZERO_DBL);// 超合同收款比例
		ctarvo.setIprintcount(0);
		ctarvo.setFstatusflag(0);// 合同状态
		ctarvo.setVersion(new UFDouble(1.0));
		ctarvo.setVdef18(headvo.getSrcid());// 物业收费系统单据ID
		ctarvo.setVdef19(headvo.getSrcbillno());// 物业收费系统单据号
		ctarvo.setActualvalidate(new UFDate(headvo.getActualvalidate()));// 合同开始日期
		ctarvo.setActualinvalidate(new UFDate(headvo.getActualinvalidate()));// 合同结束日期
		ctarvo.setCitycompany(headvo.getCitycompany()); // 城市公司
		ctarvo.setEntryname(headvo.getEntryname()); // 邻里项目名称
		ctarvo.setNeighborhood(headvo.getNeighborhood()); // 邻里项目简称
		ctarvo.setRelatedthird(headvo.getRelatedthird()); // 关联方/第三方
		ctarvo.setContractdate(headvo.getContractdate()); // 合同签订日期
		ctarvo.setStartexecution(headvo.getStartexecution()); // 合同约定的开始执行情况
		ctarvo.setEndexecution(headvo.getEndexecution()); // 合同约定的结束执行情况
		ctarvo.setTermcontract(headvo.getTermcontract()); // 合同期限（月+日）
		ctarvo.setSettlementcycle(headvo.getSettlementcycle()); // 结算周期
		ctarvo.setTaxrate(headvo.getTaxrate()); // 税率
		ctarvo.setNtotalorigmny(new UFDouble(headvo.getContractamount())); // 签约含税金额
		ctarvo.setDynamicamount(headvo.getDynamicamount()); // 动态金额
		ctarvo.setContractualstatus(headvo.getContractualstatus()); // 合同状态
		ctarvo.setVdef42(getdefdocBycode(headvo.getBusinesstype(), "SDLL003")); // 业务类型
		ctarvo.setVdef43(getdefdocBycode(headvo.getBusinessbreakdown(),
				"SDLL004")); // 业务细类

		// 2020-09-28-谈子健--新加字段-start

		ctarvo.setCorporatename(headvo.getCorporatename());// 我方签订合同公司名称
		ctarvo.setFirst(headvo.getFirst());// 甲方
		ctarvo.setSecond(headvo.getSecond());// 乙方
		ctarvo.setThird(headvo.getThird());// 丙方
		ctarvo.setFourth(headvo.getFourth());// 丁方
		ctarvo.setFifth(headvo.getFifth());// 戊方
		ctarvo.setSixth(headvo.getSixth());// 己方
		ctarvo.setOperator(headvo.getOperator());// 经办人
		ctarvo.setDepartment(headvo.getDepartment());// 经办部门
		ctarvo.setProject(headvo.getProject());// 自营项目/分包项目
		ctarvo.setPeriod(headvo.getPeriod());// 周期/非周期
		ctarvo.setTripartiteagreement(headvo.getTripartiteagreement());// 买断/三方协议合同
		ctarvo.setQuantity(headvo.getQuantity());// 台数
		ctarvo.setClassification(headvo.getClassification());// 时代邻里/时代地产/第三方
		ctarvo.setPropertyname(headvo.getPropertyname());// 邻里在管楼盘名称
		ctarvo.setOtherentryname(headvo.getOtherentryname());// 其他项目名称

		// 2020-09-28-谈子健--新加字段-end
		return ctarvo;

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
			String parentPKValue, AggCtArVO desAggVO, String bvoPKfiled,
			String table) throws BusinessException {
		// 传参数的BVOs
		ISuperVO[] syncDesPkBVOs = desAggVO.getChildren(clazz);
		Map<String, String> info = getBvoPkByEbsMap("pk_fct_ar", bvoPKfiled,
				table, parentPKValue);
		List<String> list = new ArrayList<String>();
		if (info.size() > 0) {
			list.addAll(info.values());
		}
		if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
			for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
				tmpDesBVO.setStatus(VOStatus.NEW);
				tmpDesBVO.setAttributeValue("pk_fct_ar", parentPKValue);
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
			String parentPKValue, AggCtArVO desAggVO, String bvoPKfiled,
			String table, ISuperVO[] syncDesPkBVOs, List<String> list)
			throws BusinessException {
		String condition = " pk_fct_ar='" + parentPKValue + "' and dr = 0   ";
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

	protected AggCtArVO onTranBill(HashMap<String, Object> info, String hpk)
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
	protected Object[] getDeptpksByCode(String code, String pk_org)
			throws BusinessException {
		String sql = "select pk_dept,pk_vid  from org_dept "
				+ "  where nvl(org_dept.dr,0)=0  and nvl(org_dept.dr,0)=0 and enablestate = '2' and org_dept.code='"
				+ code + "'";
		Object[] pk_depts = null;
		try {
			pk_depts = (Object[]) getBaseDAO().executeQuery(sql,
					new ArrayProcessor());
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
		String sql = "select bd_psndoc.pk_psndoc from bd_psndoc "
				+ "  where nvl(bd_psndoc.dr,0)=0 and bd_psndoc.code ='" + code
				+ "'";
		String pk = null;
		try {
			pk = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
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
	 * @param ctArBvos
	 *            收款计划
	 * @throws BusinessException
	 */
	private void vaildCtArPlanBodyData(List<LLCtArPlanJsonVO> ctArPlanBvos)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (ctArPlanBvos == null || ctArPlanBvos.size() <= 0) {
			str.append("数据异常，收款计划页签不能为空！");
		} else {
			for (int i = 0; i < ctArPlanBvos.size(); i++) {
				if (StringUtil.isEmpty(String.valueOf(ctArPlanBvos.get(i)
						.getPlanmoney()))) {
					str.append("数据异常，收款计划页签，第" + i + 1 + "行，收款金额不能为空！");
				}
				if (StringUtil.isEmpty(String.valueOf(ctArPlanBvos.get(i)
						.getName()))) {
					str.append("数据异常，收款计划页签，第" + i + 1 + "行，收款标准名称不能为空！");
				}
				if (StringUtil.isEmpty(String.valueOf(ctArPlanBvos.get(i)
						.getEnddate()))) {
					str.append("数据异常，收款计划页签，第" + i + 1 + "行，收款日期不能为空！");
				}

			}
		}

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

	/**
	 * 
	 * @param ctArBvos
	 *            合同基本信息页签
	 * @throws BusinessException
	 */
	private void vaildCtArBvosData(List<LLCtArJsonBVO> ctArBvos)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		if (ctArBvos == null || ctArBvos.size() <= 0) {
			str.append("数据异常，收款合同表体基本页签不能为空！");
		} else {
			for (int i = 0; i < ctArBvos.size(); i++) {
				if (StringUtil.isEmpty(String.valueOf(ctArBvos.get(i)
						.getNorigtaxmny()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1 + "行，签约含税金额不能为空！");
				}
				if (StringUtil.isEmpty(String.valueOf(ctArBvos.get(i)
						.getNorigmny()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1 + "行，税额不能为空！");
				}
				if (StringUtil.isEmpty(String.valueOf(ctArBvos.get(i)
						.getNtaxrate()))) {
					str.append("数据异常，收款合同表体基本页签，第" + i + 1 + "行，税率不能为空！");
				}

			}
		}

		if (str.length() > 0) {
			throw new BusinessException(str.toString());
		}
	}

}
