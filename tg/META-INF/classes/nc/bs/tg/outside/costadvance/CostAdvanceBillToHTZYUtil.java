package nc.bs.tg.outside.costadvance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.erm.accruedexpense.common.ErmAccruedBillUtils;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.fi.pub.Currency;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.erm.accruedexpense.IErmAccruedBillManage;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.er.exception.BugetAlarmBusinessException;
import nc.vo.erm.accruedexpense.AccruedDetailVO;
import nc.vo.erm.accruedexpense.AccruedVO;
import nc.vo.erm.accruedexpense.AggAccruedBillVO;
import nc.vo.fct.ap.entity.CtApVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.outside.CostBodyVO;
import nc.vo.tg.outside.CostHeadVO;

public class CostAdvanceBillToHTZYUtil extends BillUtils implements
		ITGSyncService {
	public static final String WYSFDefaultOperator = "LLWYSF";// 物业收费系统制单人
	public static final String SRMDefaultOperator = "LLSRM";// 物业收费系统制单人
	private IErmAccruedBillManage billManagerService;

	/**
	 * EBS合同调用合同预提单审核通过后，同时生成一张红字预提单，一张篮字合同预占单（合同金额）。
	 * 红字预提单和调用的预提单信息一致，只有金额是原金额负数，红字预提单单号、金额、科目等信息回写调用的预提单核销明细子表中。
	 * 蓝字合同预占单金额就是合同金额。
	 */
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		// TODO 自动生成的方法存根

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

		// InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		ISqlThread iser = NCLocator.getInstance().lookup(ISqlThread.class);// 预提服务
		AggAccruedBillVO aggvo = null;
		// 处理表单信息
		String jsonhead = jsonData.getString("accrualHeadVO");// 外系统来源表头数据
		String jsonbody = jsonData.getString("accrualBodyVOs");// 外系统来源表体数据
		StringBuffer operation = new StringBuffer();// nc进行操作详情
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("表单数据为空，请检查！json:" + jsonData);
		}
		// 转换json
		CostHeadVO headVO = JSONObject.parseObject(jsonhead, CostHeadVO.class);
		List<CostBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				CostBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("表单数据转换失败，请检查！json:" + jsonData);
		}

		String srcid = headVO.getSrcid();// 来源业务单据ID
		String srcno = headVO.getSrcbillcode();// 来源业务单据单据号
		// String ncid = headVO.getNcid();
		String isadvance = headVO.getIsadvance();
		String billqueue = "合同预占接口" + ":" + srcid;
		String billkey = "合同预占接口" + ":" + srcid;
		String destype = "01";
		// // TODO ebsid 按实际存入信息位置进行变更

		String contractcode = headVO.getContractcode();
		try {
			// 删除单据
			if ("1".equals(headVO.getType())) {
				AggAccruedBillVO prebillvo = new AggAccruedBillVO();
				IPFBusiAction pfaction = NCLocator.getInstance().lookup(
						IPFBusiAction.class);
				operation.append("删除操作");
				AggAccruedBillVO deleaggvo = (AggAccruedBillVO) getBillVO(
						AggAccruedBillVO.class,
						"isnull(dr,0)=0 and   defitem11  = '"
								+ headVO.getContractcode()
								+ "' and defitem25='" + headVO.getYear() + "'");
				if (deleaggvo == null) {
					throw new BusinessException("找不到对应单据" + headVO.getNcid());
				}
				if (deleaggvo.getParentVO().getBillstatus() == 3) {
					Object returnobj_uapp = pfaction.processAction(
							IPFActionName.UNAPPROVE, "262X", null, deleaggvo,
							null, null);
					if (((Object[]) returnobj_uapp)[0] instanceof nc.vo.erm.common.MessageVO) {
						prebillvo = (AggAccruedBillVO) ((nc.vo.erm.common.MessageVO) ((Object[]) returnobj_uapp)[0])
								.getSuccessVO();
						if (prebillvo != null) {
							deleaggvo = prebillvo;
						}
					}
				}
				Object returnobj = null;
				if (deleaggvo.getParentVO().getApprstatus() != -1) {
					returnobj = pfaction.processAction(IPFActionName.UNSAVE,
							"262X", null, deleaggvo, null, null);
				}
				if (returnobj != null) {
					if (((Object[]) returnobj)[0] instanceof nc.vo.erm.accruedexpense.AggAccruedBillVO) {
						deleaggvo = (AggAccruedBillVO) ((Object[]) returnobj)[0];
					}
				}
				getAppService().deleteVOs(new AggAccruedBillVO[] { deleaggvo });
				aggvo = deleaggvo;
			} else {
				if ("2".equals(headVO.getType())) {// 单据审批
					aggvo = doApprove(headVO, bodyVOs);
					operation.append("审批操作");
					if ("Y".equals(isadvance)) {
						String billno =aggvo.getParentVO().getBillno();
						AggAccruedBillVO preupdateAggvo = (AggAccruedBillVO) getBillVO(
								AggAccruedBillVO.class,
								"isnull(dr,0)=0 and   billno='"
										+ headVO.getNcid() + "'");
						if (preupdateAggvo == null) {
							throw new BusinessException("冲预提操作传入预提单号无效，找不到对应单据"
									+ headVO.getNcid());
						}
						if(preupdateAggvo.getParentVO().getRedflag()==null||preupdateAggvo.getParentVO().getRedflag()!=1||preupdateAggvo.getParentVO().getRedflag()!=2){
							AggAccruedBillVO redbill = onTranRedBill(preupdateAggvo);// 生成红字预提单
							redbill.getParentVO().setDefitem30(billno);
							redbill.getParentVO().setPk_tradetype("262X-Cxx-LL04");
							redbill.getParentVO().setPk_tradetypeid(getBillTypePkByCode("262X-Cxx-LL04",
									redbill.getParentVO().getPk_group()));
							getAppService().insertVO(redbill);
						}else {
							String sql="select d.defitem30 from er_accrued_detail c, er_accrued d where c.pk_accrued_bill = d.pk_accrued_bill "
									+ " and c.src_accruedpk = '"+preupdateAggvo.getParentVO().getPk_accrued_bill()+"'";
							
							String redbillno=(String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
							if(redbillno!=null&&!redbillno.equals(billno)){
								throw new BusinessException("冲预提操作,预提单已被合同占用单"+billno+"红冲，请勿重复操作！");
							}
						}
					}
				} else if ("3".equals(headVO.getType())) {// 新增单据
						operation.append("新增/更新操作");
						aggvo=doInsertOrUpdate(headVO, bodyVOs);
						
				} else if ("4".equals(headVO.getType())) { // 新增单据+审批
					operation.append("新增+审批操作");
					aggvo=doInsertOrUpdate(headVO, bodyVOs);
					doApprove(headVO, bodyVOs);
				}
			}
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
			// }
		}
		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("billid", aggvo.getPrimaryKey());
		dataMap.put("operation", operation.toString());
		dataMap.put("billno",
				(String) aggvo.getParentVO()
						.getAttributeValue(AccruedVO.BILLNO));
		if (aggvo == null) {
			throw new BusinessException("【" + billkey + "】," + "错误**无返回单据"
					+ "进行操作**" + operation.toString());
		}
		return JSON.toJSONString(dataMap);
	}

	private AggAccruedBillVO onTranRedBill(AggAccruedBillVO preupdateAggvo)
			throws Exception {
		// TODO 自动生成的方法存根
		AggAccruedBillVO redbill = (AggAccruedBillVO) preupdateAggvo.clone();
		AggAccruedBillVO newredbill = ErmAccruedBillUtils.getRedbackVO(redbill);
		return newredbill;
	}

	/*
	 * 更新预提单据vo
	 */
	public AggAccruedBillVO updateVO(CostHeadVO headvo, AggAccruedBillVO aggvo,
			List<CostBodyVO> bodyVOs, CtApVO contracVO) throws Exception {
		AccruedVO update_hvo = aggvo.getParentVO();
		AccruedDetailVO[] bodyvos = aggvo.getChildrenVO();
		List<AccruedDetailVO> list = Arrays.asList(new AccruedDetailVO[0]);
		update_hvo.setAttributeValue("org_currinfo", null);// 组织本币汇率
		update_hvo.setAttributeValue("group_currinfo", null);// 集团本币汇率
		update_hvo.setAttributeValue("global_currinfo", null);// 全局本币汇率
		update_hvo.setAttributeValue("amount", null);// 金额
		update_hvo.setAttributeValue("org_amount", null);// 组织本币金额
		update_hvo.setAttributeValue("group_amount", null);// 集团本币金额
		update_hvo.setAttributeValue("global_amount", null);// 全局本币金额
		update_hvo.setAttributeValue("verify_amount", null);// 核销金额
		update_hvo.setAttributeValue("org_verify_amount", null);// 组织本币核销金额
		update_hvo.setAttributeValue("group_verify_amount", null);// 集团本币核销金额
		update_hvo.setAttributeValue("global_verify_amount", null);// 全局本币核销金额
		update_hvo.setAttributeValue("predict_rest_amount", null);// 预计余额
		update_hvo.setAttributeValue("rest_amount", null);// 余额
		update_hvo.setAttributeValue("org_rest_amount", null);// 组织本币余额
		update_hvo.setAttributeValue("group_rest_amount", null);// 集团本币余额
		update_hvo.setAttributeValue("global_rest_amount", null);// 全局本币余额
		update_hvo.setAttributeValue("reason", headvo.getReason());// 事由
		update_hvo.setAttributeValue("attach_amount", null);// 附件张数
		update_hvo.setAttributeValue("defitem1", headvo.getSrcid());// EBSID
		update_hvo.setAttributeValue("defitem6", headvo.getPlate());// 板块
		update_hvo.setAttributeValue("defitem7", headvo.getIspostcontract());// 是否跨年合同
		update_hvo.setAttributeValue("defitem8", headvo.getContractmoney());// 合同动态金额
		update_hvo.setAttributeValue("defitem9", null);// 自定义项9
		update_hvo.setAttributeValue("defitem10", null);// 自定义项10
		update_hvo.setAttributeValue("defitem11", headvo.getContractcode());// 合同编码
		update_hvo.setAttributeValue("defitem12", headvo.getContractname());// 合同名称
		update_hvo.setAttributeValue("defitem13", headvo.getContractversion());// 合同版本
		update_hvo.setAttributeValue("defitem14", headvo.getBilltype());// 单据类型
		update_hvo.setAttributeValue("defitem3",
				getPk_orgByCode(headvo.getBudget()));// 预算主体
		update_hvo.setAttributeValue("defitem29", headvo.getOperatordept());// 经办部门
		update_hvo.setAttributeValue("defitem30", headvo.getOperator());// 经办人
		List<AccruedDetailVO> save_bodyVOs = new ArrayList<>();

		String assume_org = update_hvo.getDefitem3();
		String assume_dept = getOperator_dept("12", assume_org);
		int row = 1;
		for (CostBodyVO costBodyVO : bodyVOs) {
			AccruedDetailVO save_bodyVO = new AccruedDetailVO();
			save_bodyVO.setAttributeValue("rowno", row);// 行号
			row++;
			// save_bodyVO.setAttributeValue("assume_org_name",costBodyVO.getAssumeorg_name());//
			// 费用承担单位名称
			save_bodyVO.setAttributeValue("assume_org", assume_org);// 费用承担单位编码
			// save_bodyVO.setAttributeValue("assume_dept_name",costBodyVO.getAssumedept_name());//
			// 费用承担部门名称
			save_bodyVO.setAttributeValue("assume_dept", assume_dept);// 费用承担部门编码
			save_bodyVO.setAttributeValue("pk_iobsclass", null);// 收支项目
			save_bodyVO.setAttributeValue("pk_pcorg", null);// 利润中心
			save_bodyVO.setAttributeValue("pk_resacostcenter", null);// 成本中心
			save_bodyVO.setAttributeValue("pk_checkele", null);// 核算要素
			save_bodyVO.setAttributeValue("pk_project",
					getProject(costBodyVO.getProject()));// 项目getpk_projectByCode()
			save_bodyVO.setAttributeValue("pk_wbs", null);// 项目任务
			save_bodyVO.setAttributeValue("pk_supplier", null);// 供应商
			save_bodyVO.setAttributeValue("pk_customer", null);// 客户
			save_bodyVO.setAttributeValue("pk_proline", null);// 产品线
			save_bodyVO.setAttributeValue("pk_brand", null);// 品牌
			save_bodyVO.setAttributeValue("org_currinfo", null);// 组织本币汇率
			save_bodyVO.setAttributeValue("group_currinfo", null);// 集团本币汇率
			save_bodyVO.setAttributeValue("global_currinfo", null);// 全局本币汇率
			save_bodyVO.setAttributeValue("amount",
					new UFDouble(costBodyVO.getAmount()));// 金额
			save_bodyVO.setAttributeValue("org_amount", null);// 组织本币金额
			save_bodyVO.setAttributeValue("group_amount", null);// 集团本币金额
			save_bodyVO.setAttributeValue("global_amount", null);// 全局本币金额
			save_bodyVO.setAttributeValue("verify_amount", null);// 核销金额
			save_bodyVO.setAttributeValue("org_verify_amount", null);// 组织本币核销金额
			save_bodyVO.setAttributeValue("group_verify_amount", null);// 集团本币核销金额
			save_bodyVO.setAttributeValue("global_verify_amount", null);// 全局本币核销金额
			save_bodyVO.setAttributeValue("predict_rest_amount", new UFDouble(
					costBodyVO.getAmount()));// 预计余额
			save_bodyVO.setAttributeValue("rest_amount", new UFDouble(
					costBodyVO.getAmount()));// 余额
			save_bodyVO.setAttributeValue("org_rest_amount", null);// 组织本币余额
			save_bodyVO.setAttributeValue("group_rest_amount", null);// 集团本币余额
			save_bodyVO.setAttributeValue("global_rest_amount", null);// 全局本币余额
			save_bodyVO
					.setAttributeValue("defitem2", costBodyVO.getDeptfloor());// 部门楼层
			// save_bodyVO.setAttributeValue("defitem3", null);// 自定义项3
			if ("262X-Cxx-FY01".equals(update_hvo.getPk_tradetype())) {
				save_bodyVO.setAttributeValue("defitem7",
						costBodyVO.getAdvanceamount());// 冲预提金额
			}
			save_bodyVO.setAttributeValue("defitem4", costBodyVO.getScale());// 拆分比例
			save_bodyVO.setAttributeValue("defitem5", costBodyVO.getExplain());// 说明
			save_bodyVO.setAttributeValue("defitem8",
					costBodyVO.getBudgetyear());// 预算年度
			save_bodyVO.setAttributeValue("defitem9", null);// 自定义项9
			save_bodyVO.setAttributeValue("defitem10", null);// 自定义项10
			save_bodyVO.setAttributeValue("defitem11", null);// 自定义项11
			save_bodyVO.setAttributeValue("defitem12",
					costBodyVO.getBudgetsubject());// 预算科目
			// save_bodyVO.setAttributeValue("defitem48",costBodyVO.getBudgetsubjectname());//
			// 预算科目全称
			// save_bodyVO.setAttributeValue("defitem12","1011001");// 预算科目
			save_bodyVO.setAttributeValue("defitem13", null);// 自定义项13
			save_bodyVO.setAttributeValue("defitem14", null);// 自定义项14
			save_bodyVO.setAttributeValue("defitem15", null);// 自定义项15
			save_bodyVO.setAttributeValue("defitem16", null);// 自定义项16
			save_bodyVO.setAttributeValue("defitem17", null);// 自定义项17
			save_bodyVO.setAttributeValue("defitem18", null);// 自定义项18
			save_bodyVO.setAttributeValue("defitem19", null);// 自定义项19
			save_bodyVO.setAttributeValue("defitem20", null);// 自定义项20
			save_bodyVO.setAttributeValue("defitem21", null);// 自定义项21
			// save_bodyVO.setAttributeValue("defitem22",getdefdocBycode(costBodyVO.getBusinessformat(),
			// "ys004"));// 业态
			save_bodyVO.setAttributeValue("defitem22",
					costBodyVO.getBusinessformat());
			save_bodyVO.setAttributeValue("defitem23", null);// 自定义项23
			save_bodyVO.setAttributeValue("defitem24", null);// 自定义项24
			save_bodyVO.setAttributeValue("defitem25",
					getPsndocPkByCode(costBodyVO.getLender()));// 用款人
			save_bodyVO.setAttributeValue("defitem26",
					getcarnum(costBodyVO.getCardeptdoc()));// 车牌部门档案
			save_bodyVO.setAttributeValue("defitem27", null);// 自定义项27
			save_bodyVO.setAttributeValue("defitem28", null);// 自定义项28
			save_bodyVO.setAttributeValue("defitem29",
					costBodyVO.getBudgetsubjectname());// 自定义项29
			save_bodyVO.setAttributeValue("defitem30", null);// 自定义项30
			save_bodyVO.setAttributeValue("src_accruedpk", null);// 来源预提单pk
			save_bodyVO.setAttributeValue("srctype", null);// 来源单据类型
			save_bodyVO.setAttributeValue("src_detailpk", null);// 来源预提detailpk
			save_bodyVO.setAttributeValue("red_amount", null);// 红冲金额
			save_bodyVO.setAttributeValue("pk_accrued_bill", null);// 费用预提单_主键
			save_bodyVO.setStatus(VOStatus.NEW);
			save_bodyVOs.add(save_bodyVO);
		}
		update_hvo.setStatus(VOStatus.UPDATED);
		aggvo.setParentVO(update_hvo);
		aggvo.setChildrenVO(save_bodyVOs.toArray(new AccruedDetailVO[0]));
		resetCurrencyRate(aggvo.getParentVO());
		setHeadAmountByBodyAmounts(aggvo);
		List<AccruedDetailVO> update_bodyVOs = new ArrayList<>();
		update_bodyVOs.addAll(Arrays.asList(aggvo.getChildrenVO()));
		for (AccruedDetailVO bvo : bodyvos) {
			bvo.setStatus(VOStatus.DELETED);
			bvo.setDr(1);
			update_bodyVOs.add(bvo);
		}
		aggvo.setChildrenVO(update_bodyVOs.toArray(new AccruedDetailVO[0]));
		resetBody(aggvo);
		return aggvo;
	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public AggAccruedBillVO onTranBill(CostHeadVO headvo,
			List<CostBodyVO> bodyVOs, CtApVO contracVO) throws Exception {
		AggAccruedBillVO aggvo = new AggAccruedBillVO();
		AccruedVO save_headVO = new AccruedVO();
		save_headVO.setAttributeValue("pk_accrued_bill", null);// 主键
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团
		save_headVO.setAttributeValue("pk_org",
				getPk_orgByCode(headvo.getOrg()));// 财务组织
		save_headVO.setAttributeValue("pk_billtype", "262X");// 单据类型
		save_headVO.setAttributeValue("pk_tradetype", "262X-Cxx-LL03");// 交易类型编号
		if (StringUtils.isNotBlank(getBillTypePkByCode(
				save_headVO.getPk_tradetype(), save_headVO.getPk_group()))) {
			save_headVO.setAttributeValue(
					"pk_tradetypeid",
					getBillTypePkByCode(save_headVO.getPk_tradetype(),
							save_headVO.getPk_group()));// 交易类型
		} else {
			throw new BusinessException("该交易类型编码："
					+ save_headVO.getPk_tradetype() + "未能在NC关联，请检查");
		}

		save_headVO.setAttributeValue("billno", null);// 单据编号
		save_headVO.setBilldate(new UFDate());// 单据日期
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
		if (headvo.getCurrency() != null && headvo.getCurrency() != "") {
			save_headVO.setAttributeValue("pk_currtype", headvo.getCurrency());
		}
		save_headVO.setAttributeValue("billstatus", 1);// 单据状态
		save_headVO.setAttributeValue("apprstatus", -1);// 审批状态
		save_headVO.setAttributeValue("effectstatus", 0);// 生效状态
		save_headVO.setAttributeValue("org_currinfo", null);// 组织本币汇率
		save_headVO.setAttributeValue("group_currinfo", null);// 集团本币汇率
		save_headVO.setAttributeValue("global_currinfo", null);// 全局本币汇率
		save_headVO.setAttributeValue("amount", null);// 金额
		save_headVO.setAttributeValue("org_amount", null);// 组织本币金额
		save_headVO.setAttributeValue("group_amount", null);// 集团本币金额
		save_headVO.setAttributeValue("global_amount", null);// 全局本币金额
		save_headVO.setAttributeValue("verify_amount", null);// 核销金额
		save_headVO.setAttributeValue("org_verify_amount", null);// 组织本币核销金额
		save_headVO.setAttributeValue("group_verify_amount", null);// 集团本币核销金额
		save_headVO.setAttributeValue("global_verify_amount", null);// 全局本币核销金额
		save_headVO.setAttributeValue("predict_rest_amount", null);// 预计余额
		save_headVO.setAttributeValue("rest_amount", null);// 余额
		save_headVO.setAttributeValue("org_rest_amount", null);// 组织本币余额
		save_headVO.setAttributeValue("group_rest_amount", null);// 集团本币余额
		save_headVO.setAttributeValue("global_rest_amount", null);// 全局本币余额
		save_headVO.setAttributeValue("reason", headvo.getReason());// 事由
		save_headVO.setAttributeValue("attach_amount", null);// 附件张数
		save_headVO.setAttributeValue("operator_org", getopertor_org());// 经办人单位
		save_headVO.setAttributeValue("operator_dept", getOperator_dept());// 经办人部门
		save_headVO.setAttributeValue("operator", getopertor());// 经办人
		save_headVO.setAttributeValue("defitem1", headvo.getSrcid());// EBSID
		save_headVO.setAttributeValue("defitem2", headvo.getSrcbillcode());// EBS单号
		save_headVO.setAttributeValue("defitem3",
				getPk_orgByCode(headvo.getBudget()));// 预算主体
		save_headVO.setAttributeValue("defitem4", null);// 自定义项4
		save_headVO.setAttributeValue("defitem5", null);// 自定义项5
		save_headVO.setAttributeValue("defitem6", headvo.getPlate());// 板块
		save_headVO.setAttributeValue("defitem7", headvo.getIspostcontract());// 是否是跨年合同
		save_headVO.setAttributeValue("defitem8", headvo.getContractmoney());// 合同动态金额
		save_headVO.setAttributeValue("defitem9", null);// 自定义项9
		save_headVO.setAttributeValue("defitem10", null);// 自定义项10
		save_headVO.setAttributeValue("defitem11", headvo.getContractcode());// 合同编码
		save_headVO.setAttributeValue("defitem12", headvo.getContractname());// 合同名称
		save_headVO.setAttributeValue("defitem13", headvo.getContractversion());// 合同版本
		save_headVO.setAttributeValue("defitem14", headvo.getBilltype());// 单据类型
		save_headVO.setAttributeValue("defitem15", null);// 预算主体
		save_headVO.setAttributeValue("defitem16", null);// 自定义项16
		save_headVO.setAttributeValue("defitem17", null);// 自定义项17
		save_headVO.setAttributeValue("defitem18", null);// 自定义项18
		save_headVO.setAttributeValue("defitem19", null);// 自定义项19
		save_headVO.setAttributeValue("defitem20", null);// 自定义项20
		save_headVO.setAttributeValue("defitem21", null);// 自定义项21
		save_headVO.setAttributeValue("defitem22", null);// 自定义项22
		save_headVO.setAttributeValue("defitem23", null);// 自定义项23
		save_headVO.setAttributeValue("defitem24", null);// 自定义项24
		save_headVO.setAttributeValue("defitem25", headvo.getYear());// 自定义项25
		save_headVO.setAttributeValue("defitem26", null);// 自定义项26
		save_headVO.setAttributeValue("defitem28", null);// 自定义项28
		save_headVO.setAttributeValue("defitem29", headvo.getOperatordept());// 经办部门
		save_headVO.setAttributeValue("defitem30", headvo.getOperator());// 经办人
		save_headVO.setAttributeValue("approver", null);// 审批人
		save_headVO.setAttributeValue("approvetime", null);// 审批时间
		save_headVO.setAttributeValue("printer", null);// 正式打印人
		save_headVO.setAttributeValue("printdate", null);// 正式打印日期
		save_headVO.setAttributeValue("creator",
				getUserIDByCode(headvo.getCreator()));
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// 创建时间
		save_headVO.setAttributeValue("modifier", null);// 最后修改人
		save_headVO.setAttributeValue("modifiedtime", null);// 最后修改时间
		save_headVO.setAttributeValue("auditman",
				getUserIDByCode(headvo.getCreator()));
		save_headVO.setAttributeValue("redflag", null);// 红冲标志
		save_headVO.setAttributeValue("imag_status", null);// 影像状态
		save_headVO.setAttributeValue("isneedimag", UFBoolean.FALSE);// 需要影像扫描
		save_headVO.setAttributeValue("isexpedited", UFBoolean.FALSE);// 紧急
		save_headVO.setAttributeValue("red_amount", null);// 红冲金额
		save_headVO.setAttributeValue("org_amount", null);// 组织金额
		List<AccruedDetailVO> save_bodyVOs = new ArrayList<>();
		int row = 1;
		String assume_org = save_headVO.getDefitem3();
		String assume_dept = getOperator_dept("12", assume_org);
		for (CostBodyVO costBodyVO : bodyVOs) {
			AccruedDetailVO save_bodyVO = new AccruedDetailVO();
			checkBodyTransforExpense(costBodyVO);// 空值检查
			save_bodyVO.setAttributeValue("pk_accrued_detail", null);// 主键
			save_bodyVO.setAttributeValue("rowno", row);// 行号
			row++;
			// 费用承担单位名称
			save_bodyVO.setAttributeValue("assume_org", assume_org);// 费用承担单位编码
			// 费用承担部门名称
			save_bodyVO.setAttributeValue("assume_dept", assume_dept);// 费用承担部门编码
			save_bodyVO.setAttributeValue("pk_iobsclass", null);// 收支项目
			save_bodyVO.setAttributeValue("pk_pcorg", null);// 利润中心
			save_bodyVO.setAttributeValue("pk_resacostcenter", null);// 成本中心
			save_bodyVO.setAttributeValue("pk_checkele", null);// 核算要素
			save_bodyVO.setAttributeValue("pk_wbs", null);// 项目任务
			save_bodyVO.setAttributeValue("pk_supplier", null);// 供应商
			save_bodyVO.setAttributeValue("pk_customer", null);// 客户
			save_bodyVO.setAttributeValue("pk_proline", null);// 产品线
			save_bodyVO.setAttributeValue("pk_brand", null);// 品牌
			save_bodyVO.setAttributeValue("org_currinfo", null);// 组织本币汇率
			save_bodyVO.setAttributeValue("group_currinfo", null);// 集团本币汇率
			save_bodyVO.setAttributeValue("global_currinfo", null);// 全局本币汇率
			save_bodyVO.setAttributeValue("amount",
					new UFDouble(costBodyVO.getAmount()));// 金额
			save_bodyVO.setAttributeValue("org_amount", null);// 组织本币金额
			save_bodyVO.setAttributeValue("group_amount", null);// 集团本币金额
			save_bodyVO.setAttributeValue("global_amount", null);// 全局本币金额
			save_bodyVO.setAttributeValue("verify_amount", null);// 核销金额
			save_bodyVO.setAttributeValue("org_verify_amount", null);// 组织本币核销金额
			save_bodyVO.setAttributeValue("group_verify_amount", null);// 集团本币核销金额
			save_bodyVO.setAttributeValue("global_verify_amount", null);// 全局本币核销金额
			save_bodyVO.setAttributeValue("predict_rest_amount", new UFDouble(
					costBodyVO.getAmount()));// 预计余额
			save_bodyVO.setAttributeValue("rest_amount", new UFDouble(
					costBodyVO.getAmount()));// 余额
			save_bodyVO.setAttributeValue("org_rest_amount", null);// 组织本币余额
			save_bodyVO.setAttributeValue("group_rest_amount", null);// 集团本币余额
			save_bodyVO.setAttributeValue("global_rest_amount", null);// 全局本币余额
			save_bodyVO
					.setAttributeValue("defitem2", costBodyVO.getDeptfloor());// 部门楼层
			if ("262X-Cxx-FY01".equals(save_headVO.getPk_tradetype())
					&& Float.valueOf(costBodyVO.getAdvanceamount()) != null) {
				save_bodyVO.setAttributeValue("defitem7",
						costBodyVO.getAdvanceamount());// 是否冲预提
			}
			save_bodyVO.setAttributeValue("defitem3", null);// 自定义项3
			if ("262X-Cxx-FY01".equals(save_headVO.getPk_tradetype())) {
				save_bodyVO.setAttributeValue("defitem3",
						costBodyVO.getAdvanceamount());// 冲预提金额
			}
			save_bodyVO.setAttributeValue("defitem4", costBodyVO.getScale());// 拆分比例
			save_bodyVO.setAttributeValue("defitem5", costBodyVO.getExplain());// 说明
			save_bodyVO.setAttributeValue("defitem6", null);// 自定义项6
			save_bodyVO.setAttributeValue("defitem8",
					costBodyVO.getBudgetyear());// 预算年度
			save_bodyVO.setAttributeValue("defitem9", null);// 自定义项9
			save_bodyVO.setAttributeValue("defitem10", null);// 自定义项10
			save_bodyVO.setAttributeValue("defitem11", null);// 自定义项11
			save_bodyVO.setAttributeValue("defitem12",
					costBodyVO.getBudgetsubject());// 预算科目
			save_bodyVO.setAttributeValue("defitem48",
					costBodyVO.getBudgetsubjectname());// 预算科目全称
			// save_bodyVO.setAttributeValue("defitem12","10011210000000002ALE");//
			// 预算科目
			save_bodyVO.setAttributeValue("defitem13", null);// 自定义项13
			save_bodyVO.setAttributeValue("defitem14", null);// 自定义项14
			save_bodyVO.setAttributeValue("defitem15", null);// 自定义项15
			save_bodyVO.setAttributeValue("defitem16", null);// 自定义项16
			save_bodyVO.setAttributeValue("defitem17", null);// 自定义项17
			save_bodyVO.setAttributeValue("defitem18", null);// 自定义项18
			save_bodyVO.setAttributeValue("defitem19", null);// 自定义项19
			save_bodyVO.setAttributeValue("defitem20", null);// 自定义项20
			save_bodyVO.setAttributeValue("defitem21", null);// 自定义项21
			save_bodyVO.setAttributeValue("defitem22",
					costBodyVO.getBusinessformat());// 业态
			// save_bodyVO.setAttributeValue("defitem22","10011210000000002BD7");
			save_bodyVO.setAttributeValue("defitem23", null);// 自定义项23
			save_bodyVO.setAttributeValue("defitem24", null);// 自定义项24
			save_bodyVO.setAttributeValue("defitem25",
					getPsndocPkByCode(costBodyVO.getLender()));// 用款人
			save_bodyVO.setAttributeValue("defitem26",
					getcarnum(costBodyVO.getCardeptdoc()));// 车牌部门档案
			save_bodyVO.setAttributeValue("defitem27", null);// 自定义项27
			save_bodyVO.setAttributeValue("defitem28", null);// 自定义项28
			save_bodyVO.setAttributeValue("defitem29",
					costBodyVO.getBudgetsubjectname());// 自定义项29
			save_bodyVO.setAttributeValue("defitem30", null);// 自定义项30
			save_bodyVO.setAttributeValue("src_accruedpk", null);// 来源预提单pk
			save_bodyVO.setAttributeValue("srctype", null);// 来源单据类型
			save_bodyVO.setAttributeValue("src_detailpk", null);// 来源预提detailpk
			save_bodyVO.setAttributeValue("red_amount", null);// 红冲金额
			save_bodyVO.setAttributeValue("pk_accrued_bill", null);// 费用预提单_主键
			save_bodyVO.setAttributeValue("pk_project",
					getProject(costBodyVO.getProject()));
			save_bodyVO.setStatus(VOStatus.NEW);
			save_bodyVOs.add(save_bodyVO);
		}

		save_headVO.setStatus(VOStatus.NEW);
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(save_bodyVOs.toArray(new AccruedDetailVO[0]));
		resetCurrencyRate(aggvo.getParentVO());
		setHeadAmountByBodyAmounts(aggvo);
		resetBody(aggvo);
		return aggvo;
	}

	private IErmAccruedBillManage getAppService() {
		if (billManagerService == null) {
			billManagerService = NCLocator.getInstance().lookup(
					IErmAccruedBillManage.class);
		}
		return billManagerService;
	}

	/**
	 * 根据表体金额计算表头总金额，并设置总金额
	 * 
	 * @param cardPanel
	 */
	public static UFDouble setHeadAmountByBodyAmounts(AggAccruedBillVO aggvo) {
		UFDouble newYbje = null;
		AccruedDetailVO[] items = (AccruedDetailVO[]) aggvo.getChildrenVO();

		int length = items.length;

		for (int i = 0; i < length; i++) {
			if (items[i].getAmount() != null) {// 当表体中存在空行时，原币金额为空，所以在这里判空
				if (newYbje == null) {
					newYbje = items[i].getAmount();
				} else {
					newYbje = newYbje.add(items[i].getAmount());
				}
			}
		}

		aggvo.getParentVO().setAmount(newYbje);
		aggvo.getParentVO().setPredict_rest_amount(newYbje);
		aggvo.getParentVO().setOrg_rest_amount(newYbje);
		aggvo.getParentVO().setOrg_verify_amount(newYbje);
		aggvo.getParentVO().setRest_amount(newYbje);
		aggvo.getParentVO().setOrg_amount(newYbje);
		return newYbje;
	}

	public String getPk_group() {
		return AppContext.getInstance().getPkGroup();
	}

	/**
	 * 根据表体数据进行计算
	 * 
	 * @param aggvo
	 * @throws Exception
	 */
	public void resetBody(AggAccruedBillVO aggvo) throws Exception {
		AccruedVO headvo = aggvo.getParentVO();
		String headPk_org = headvo.getPk_org();
		AccruedDetailVO[] bodyVOs = aggvo.getChildrenVO();

		for (int i = 0; i < bodyVOs.length; i++) {
			AccruedDetailVO accruedDetailVO = bodyVOs[i];
			String assume_org = accruedDetailVO.getAssume_org();
			String pk_currtype = headvo.getPk_currtype();// 币种
			UFDate date = headvo.getBilldate();// 单据日期
			if (headPk_org == null || assume_org == null || pk_currtype == null
					|| date == null) {
				return;
			}

			try {
				// 重算汇率
				String headOrgCurrPk = Currency.getOrgLocalCurrPK(headPk_org);
				String assume_orgCurrPk = Currency
						.getOrgLocalCurrPK(assume_org);// 本币相同时，取表头汇率
				if (headPk_org.equals(assume_org)
						|| (headOrgCurrPk != null && assume_orgCurrPk != null && assume_orgCurrPk
								.equals(headOrgCurrPk))) {

					accruedDetailVO.setOrg_currinfo(getHeadUFDoubleValue(
							headvo, AccruedVO.ORG_CURRINFO));
					accruedDetailVO.setGroup_currinfo(getHeadUFDoubleValue(
							headvo, AccruedVO.GROUP_CURRINFO));
					accruedDetailVO.setGlobal_currinfo(getHeadUFDoubleValue(
							headvo, AccruedVO.GLOBAL_CURRINFO));
				} else {
					// 汇率(本币，集团本币，全局本币汇率)
					UFDouble orgRate = Currency.getRate(assume_org,
							pk_currtype, date);
					UFDouble groupRate = Currency.getGroupRate(assume_org,
							getPk_group(), pk_currtype, date);
					UFDouble globalRate = Currency.getGlobalRate(assume_org,
							pk_currtype, date);

					accruedDetailVO.setOrg_currinfo(orgRate);
					accruedDetailVO.setGroup_currinfo(groupRate);
					accruedDetailVO.setGlobal_currinfo(globalRate);
				}

				Object amount = accruedDetailVO.getAmount();
				accruedDetailVO.setRest_amount(accruedDetailVO.getAmount());
				accruedDetailVO.setPredict_rest_amount(accruedDetailVO
						.getAmount());

				// 重算金额
				UFDouble ori_amount = accruedDetailVO.getAmount();// 重置精度

				accruedDetailVO.setVerify_amount(UFDouble.ZERO_DBL);
				accruedDetailVO.setOrg_verify_amount(UFDouble.ZERO_DBL);
				accruedDetailVO.setGroup_verify_amount(UFDouble.ZERO_DBL);
				accruedDetailVO.setGlobal_verify_amount(UFDouble.ZERO_DBL);

				String pk_org = accruedDetailVO.getAssume_org();

				// 集团
				String pk_group = (String) headvo
						.getAttributeValue(AccruedVO.PK_GROUP);

				// 获取到汇率(能根据表体费用单位)计算表体本币金额
				UFDouble hl = (UFDouble) accruedDetailVO
						.getAttributeValue(AccruedDetailVO.ORG_CURRINFO);
				UFDouble grouphl = (UFDouble) accruedDetailVO
						.getAttributeValue(AccruedDetailVO.GROUP_CURRINFO);
				UFDouble globalhl = (UFDouble) accruedDetailVO
						.getAttributeValue(AccruedDetailVO.GLOBAL_CURRINFO);

				UFDouble[] bbje = null;
				// 组织本币金额
				bbje = Currency.computeYFB(pk_org, Currency.Change_YBCurr,
						pk_currtype, ori_amount, null, null, null, hl,
						AppContext.getInstance().getServerTime().getDate());

				accruedDetailVO.setAttributeValue(AccruedDetailVO.ORG_AMOUNT,
						bbje[2]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.ORG_REST_AMOUNT, bbje[2]);
				// 集团、全局金额
				UFDouble[] money = null;
				if (bbje == null || bbje[2] == null) {
					money = Currency.computeGroupGlobalAmount(ori_amount,
							UFDouble.ZERO_DBL, pk_currtype, AppContext
									.getInstance().getServerTime().getDate(),
							pk_org, pk_group, globalhl, grouphl);

				} else {
					money = Currency.computeGroupGlobalAmount(ori_amount,
							bbje[2], pk_currtype, AppContext.getInstance()
									.getServerTime().getDate(), pk_org,
							pk_group, globalhl, grouphl);
				}

				accruedDetailVO.setAttributeValue(AccruedDetailVO.GROUP_AMOUNT,
						money[0]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.GROUP_REST_AMOUNT, money[0]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.GLOBAL_AMOUNT, money[1]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.GLOBAL_REST_AMOUNT, money[1]);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
	}

	public UFDouble getHeadUFDoubleValue(AccruedVO headvo, String key) {
		if (headvo.getAttributeValue(key) == null) {
			return UFDouble.ZERO_DBL;
		}
		return (UFDouble) headvo.getAttributeValue(key);
	}

	/**
	 * 设置汇率
	 * 
	 * @param pk_org
	 *            组织pk
	 * @param pk_currtype
	 *            原币币种
	 * @param date
	 *            制单时间
	 * @throws Exception
	 */
	public void resetCurrencyRate(AccruedVO headvo) throws Exception {
		String pk_org = (String) headvo.getAttributeValue(AccruedVO.PK_ORG);// 组织
		String pk_currtype = (String) headvo
				.getAttributeValue(AccruedVO.PK_CURRTYPE);// 币种
		UFDate date = (UFDate) headvo.getAttributeValue(AccruedVO.BILLDATE);// 单据日期

		try {
			// 汇率(本币，集团本币，全局本币汇率)
			UFDouble orgRate = Currency.getRate(pk_org, pk_currtype, date);
			UFDouble groupRate = Currency.getGroupRate(pk_org,
					headvo.getPk_group(), pk_currtype, date);
			UFDouble globalRate = Currency.getGlobalRate(pk_org, pk_currtype,
					date);

			headvo.setAttributeValue(AccruedVO.ORG_CURRINFO, orgRate);
			headvo.setAttributeValue(AccruedVO.GROUP_CURRINFO, orgRate);
			headvo.setAttributeValue(AccruedVO.GLOBAL_CURRINFO, orgRate);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}


	private void checkBodyTransforExpense(CostBodyVO bodyvo)
			throws BusinessException {
		if (StringUtils.isBlank(Float.valueOf(bodyvo.getAmount()).toString())) {
			throw new BusinessException("金额不可为空");
		}
	}

	public AggAccruedBillVO  doInsertOrUpdate(CostHeadVO headVO, List<CostBodyVO> bodyVOs) throws Exception {
		ISqlThread iser = NCLocator.getInstance().lookup(ISqlThread.class);// 预提服务
		AggAccruedBillVO aggvo = null;
		AggAccruedBillVO exitvo = (AggAccruedBillVO) getBillVO(
				AggAccruedBillVO.class, "isnull(dr,0)=0 and   defitem11  = '"
						+ headVO.getContractcode() + "' and defitem25='"
						+ headVO.getYear() + "'");
		if (exitvo != null) {
			AggAccruedBillVO prebillvo = new AggAccruedBillVO();
			AggAccruedBillVO updedbillvo = new AggAccruedBillVO();
			AggAccruedBillVO preupdateAggvo = exitvo;
			if (preupdateAggvo.getParentVO().getApprstatus() == 1) {//
				// 更新前如果已审批先弃审
				// flag=true;
				prebillvo = iser.ytunapprove_RequiresNew(preupdateAggvo);
			} else {
				prebillvo = preupdateAggvo;
			}
			updedbillvo = updateVO(headVO, prebillvo, bodyVOs, null);
			aggvo=iser.ytupdate_RequiresNew(updedbillvo);
		} else {
			AggAccruedBillVO billvo = onTranBill(headVO, bodyVOs, null);
			AccruedDetailVO[] bvoss = billvo.getChildrenVO();
			AggAccruedBillVO nbillvo = (AggAccruedBillVO) billvo.clone();
			aggvo=iser.ytinsert_RequiresNew(billvo);
		}
		return aggvo;
	}
	public AggAccruedBillVO  doApprove(CostHeadVO headVO, List<CostBodyVO> bodyVOs) throws Exception {
		AggAccruedBillVO aggvo = null;

		AggAccruedBillVO billvo = (AggAccruedBillVO) getBillVO(
				AggAccruedBillVO.class,
				"isnull(dr,0)=0 and   defitem11  = '"
						+ headVO.getContractcode()
						+ "' and defitem25='" + headVO.getYear()
						+ "'");
		if (billvo == null) {
			throw new BusinessException("找不到对应单据"
					+ headVO.getNcid());
		}
		billvo.getParentVO().setApprover(
				getUserIDByCode(DefaultOperator));
		billvo.getParentVO().setApprovetime(new UFDateTime());
		IPFBusiAction pfaction = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		Object returnobj = pfaction.processAction(
				IPFActionName.APPROVE, "262X", null, billvo, null,
				null);
		if (((Object[]) returnobj)[0] instanceof nc.vo.erm.common.MessageVO) {
			aggvo = (AggAccruedBillVO) ((nc.vo.erm.common.MessageVO) ((Object[]) returnobj)[0])
					.getSuccessVO();
		}
		
		return aggvo;
	}
	
	
}
