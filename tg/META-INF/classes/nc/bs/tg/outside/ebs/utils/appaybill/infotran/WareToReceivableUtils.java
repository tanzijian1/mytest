package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSONObject;

/**
 * 出库单推应收单
 * 
 * @author king
 * 
 */
public class WareToReceivableUtils extends EBSBillUtils {

	static WareToReceivableUtils utils;

	public static WareToReceivableUtils getUtils() {
		if (utils == null) {
			utils = new WareToReceivableUtils();
		}
		return utils;
	}

	/**
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		return null;
	}

	/**
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onpushBill(JSONObject bJSONObject) throws BusinessException {
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// 设置用户码
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		String srcid = bJSONObject.getString("def2");// 外系统主键
		String srcbillcode = bJSONObject.getString("def1");// 外系统单据号
		// 目标业务单据中文名队列
		String billqueue = bJSONObject.getString("def49") + srcid;
		// 目标业务单据中文名键
		String billkey = bJSONObject.getString("def49") + srcbillcode;
		// 根据销售系统业务单据ID查询的应收单聚合VO
		AggReceivableBillVO aggVO = (AggReceivableBillVO) getBillVO(
				AggReceivableBillVO.class, "isnull(dr,0)=0 and def2 = '"
						+ srcid + "'");

		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {
			// 参数传过来的VO对象
			AggReceivableBillVO billvo = onTranBill(bJSONObject, billkey);
			HashMap eParam = new HashMap();
			// 工作项是否已检查
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			if (aggVO != null) {
				String pk = aggVO.getParentVO().getPrimaryKey();
				boolean check = selectRecbill(bJSONObject.getString("def3"), pk);
				if (check) {
					String def43 = bJSONObject.getString("def43");// 对账单号
					String def44 = bJSONObject.getString("def44");// 对账单总金额

					String sql = "update ar_recbill set def18 = '" + def43
							+ "',def19 = '" + def44
							+ "'  where nvl(dr,0)=0 and def3 = '"
							+ bJSONObject.getString("def3")
							+ "' and pk_recbill = '" + pk + "'";
					getBaseDAO().executeUpdate(sql);
					return null;
				}
				ReceivableBillItemVO bvo = new ReceivableBillItemVO();
				bvo = (ReceivableBillItemVO) billvo.getChildrenVO()[0];
				bvo.setPk_recbill(pk);
				bvo.setDr(0);
				getBaseDAO().insertVO(bvo);
				UFDouble money = bvo.getLocal_money_de();
				String sql = "update ar_recbill set money = money+"
						+ money.doubleValue()
						+ " where nvl(dr,0)=0 and pk_recbill = '" + pk + "'";
				getBaseDAO().executeUpdate(sql);
			} else {
				getPfBusiAction().processAction("SAVE", "F0-Cxx-001", null,
						billvo, null, eParam);
			}
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

		return "【" + billkey + "】," + "操作完成!";

	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 */
	private AggReceivableBillVO onTranBill(JSONObject bJSONObject,
			String billkey) throws BusinessException {
		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO hvo = new ReceivableBillVO();

		String pk_org = getPkByCode(bJSONObject.getString("pk_org"));
		if (!isNull(pk_org)) {
			throw new BusinessException("财务组织不能为空，请检查输入数据");
		}
		String pk_vid = getvidByorg(pk_org);

		hvo.setPk_org(pk_org);// 组织
		hvo.setPk_group("000112100000000005FD");// 集团
		hvo.setPk_billtype("F0");// 单据类型
		hvo.setPk_busitype(getBusitypeID("AR01", hvo.getPk_group())/*"0001ZZ10000000258BF2"*/);// 业务流程
		hvo.setPk_fiorg(pk_org);
		hvo.setPk_currtype("1002Z0100000000001K1");// 币种
		hvo.setBillclass("ys");// 单据大类
		hvo.setPk_tradetype("F0-Cxx-001");// 应收类型编码
		hvo.setPk_tradetypeid(gettradeTypeByCode("F0-Cxx-001"));// 应收类型
		hvo.setBillstatus(-1);// 单据状态
		hvo.setApprovestatus(-1);// 审批状态
		hvo.setEffectstatus(2);// 生效状态
		hvo.setCreationtime(new UFDateTime());// 制单时间
		hvo.setCreator(getUserPkByCode("Gadmin1"));
		hvo.setBillmaker(getUserPkByCode("Gadmin1"));// 制单人
		hvo.setIsflowbill(UFBoolean.FALSE);
		hvo.setIsreded(UFBoolean.FALSE);
		hvo.setObjtype(0);
		hvo.setPk_fiorg_v(pk_vid);
		hvo.setSrc_syscode(0);
		hvo.setSyscode(0);
		hvo.setDr(0);
		hvo.setPk_org_v(pk_vid);// 组织版本
		// 外系统来源单据id
		if (isNull(bJSONObject.getString("def2"))) {
			hvo.setDef2(bJSONObject.getString("def2"));
		} else {
			throw new BusinessException("外系统来源单据id 不能为空，请检查输入数据");
		}
		// 外系统来源单据编号
		if (isNull(bJSONObject.getString("def1"))) {
			hvo.setDef1(bJSONObject.getString("def1"));
		} else {
			throw new BusinessException("外系统来源单据编号 不能为空，请检查输入数据");
		}
		// 客户
		if (isNull(getcusPkByCode(bJSONObject.getString("customer")))) {
			hvo.setCustomer(getcusPkByCode(bJSONObject.getString("customer")));
		} else {
			throw new BusinessException("客户编号未与NC关联，请检查输入数据");
		}

		if (isNull(bJSONObject.getString("def4"))) {
			hvo.setDef14(bJSONObject.getString("def4"));
		} else {
			throw new BusinessException("采购订单编码 不能为空，请检查输入数据");
		}
		// 外系统来源单据编号
		if (isNull(bJSONObject.getString("def5"))) {
			hvo.setDef15(bJSONObject.getString("def5"));
		} else {
			throw new BusinessException("采购订单id 不能为空，请检查输入数据");
		}

		if (isNull(bJSONObject.getString("def11"))) {
			hvo.setDef16(bJSONObject.getString("def11"));
		} else {
			throw new BusinessException("采购协议编码 不能为空，请检查输入数据");
		}
		// 外系统来源单据编号
		if (isNull(bJSONObject.getString("def12"))) {
			hvo.setDef17(bJSONObject.getString("def12"));
		} else {
			throw new BusinessException("采购协议名称 不能为空，请检查输入数据");
		}

		if (!"".equals(bJSONObject.getString("def51"))
				&& bJSONObject.getString("def51") != null) {
			hvo.setDef18(getPkByCode(bJSONObject.getString("def51")));// 项目公司
			if ("".equals(hvo.getDef18()) || hvo.getDef18() == null) {
				throw new BusinessException("项目公司未与NC关联");
			}
		}

		hvo.setDef19(bJSONObject.getString("def52"));// 内部开票发票抬头公司

		// 内部交易收款合同编码
		if (isNull(bJSONObject.getString("def53"))) {
			hvo.setDef20(bJSONObject.getString("def53"));
		} else {
			throw new BusinessException("内部交易收款合同编码不能为空，请检查输入数据");
		}
		// 内部交易收款合同名称
		if (isNull(bJSONObject.getString("def54"))) {
			hvo.setDef21(bJSONObject.getString("def54"));
		} else {
			throw new BusinessException("内部交易收款合同名称不能为空，请检查输入数据");
		}
		// 内部对账ID
		if (isNull(bJSONObject.getString("def55"))) {
			hvo.setDef29(bJSONObject.getString("def55"));
		} else {
			throw new BusinessException("内部对账ID不能为空，请检查输入数据");
		}
		// 内部对账编码
		if (isNull(bJSONObject.getString("def56"))) {
			hvo.setDef30(bJSONObject.getString("def56"));
		} else {
			throw new BusinessException("内部对账编码不能为空，请检查输入数据");
		}

		// 合同编号
		/*
		 * if(isNull(bJSONObject.getString("contractcode"))){
		 * hvo.setDef5(bJSONObject.getString("contractcode")); }else { throw new
		 * BusinessException("合同编号 不能为空，请检查输入数据"); }
		 */

		// 合同名称
		/*
		 * if(isNull(data.get("contractname"))){ hvo.setDef6((String)
		 * data.get("contractname")); }else { throw new
		 * BusinessException("合同名称 不能为空，请检查输入数据"); }
		 */

		aggvo.setParentVO(hvo);

		ReceivableBillItemVO[] bvos = new ReceivableBillItemVO[1];
		ReceivableBillItemVO bvo = new ReceivableBillItemVO();
		// 默认数据
		bvo.setCustomer(getcusPkByCode(bJSONObject.getString("customer")));
		bvo.setObjtype(0);
		bvo.setOrdercubasdoc(getcusPkByCode(bJSONObject.getString("customer")));
		bvo.setPk_currtype("1002Z0100000000001K1");
		bvo.setPk_fiorg(pk_org);
		bvo.setPk_org(pk_org);
		bvo.setPk_group("000112100000000005FD");// 集团
		bvo.setPk_fiorg_v(pk_vid);
		bvo.setPk_org_v(pk_vid);
		bvo.setPk_recitem("ID_INDEX0");
		bvo.setRececountryid("0001Z010000000079UJJ");
		bvo.setSett_org(pk_org);
		bvo.setSett_org_v(pk_vid);
		bvo.setTriatradeflag(UFBoolean.FALSE);
		bvo.setBilldate(new UFDate());
		bvo.setBillclass("ys");// 单据大类
		bvo.setGrouprate(UFDouble.ONE_DBL);
		bvo.setGlobalrate(UFDouble.ONE_DBL);
		bvo.setQuantity_bal(UFDouble.ZERO_DBL);
		bvo.setMoney_bal(UFDouble.ZERO_DBL);
		bvo.setLocal_money_bal(UFDouble.ZERO_DBL);
		bvo.setGroupdebit(UFDouble.ZERO_DBL);
		bvo.setGlobaldebit(UFDouble.ZERO_DBL);
		bvo.setGroupbalance(UFDouble.ZERO_DBL);
		bvo.setGlobalbalance(UFDouble.ZERO_DBL);
		bvo.setGroupnotax_de(UFDouble.ZERO_DBL);
		bvo.setGlobalnotax_de(UFDouble.ZERO_DBL);
		bvo.setOccupationmny(UFDouble.ZERO_DBL);
		bvo.setCaltaxmny(UFDouble.ZERO_DBL);
		bvo.setLocal_notax_de(UFDouble.ZERO_DBL);
		bvo.setRate(UFDouble.ONE_DBL);
		bvo.setRowno(0);
		bvo.setDirection(1);
		bvo.setTaxtype(1);

		// 单据行id
		if (isNull(bJSONObject.getString("def3"))) {
			bvo.setDef8(bJSONObject.getString("def3"));
		} else {
			throw new BusinessException("单据行id不能为空，请检查输入数据");
		}

		// 采购订单行id
		if (isNull(bJSONObject.getString("def6"))) {
			bvo.setDef24(bJSONObject.getString("def6"));
		} else {
			throw new BusinessException("采购订单行id不能为空，请检查输入数据");
		}
		// 供应链金额（含税）
		if (isNull(bJSONObject.getString("def19"))) {
			bvo.setDef9(bJSONObject.getString("def19"));
		} else {
			throw new BusinessException("供应链金额（含税）不能为空，请检查输入数据");
		}
		// 供应链税率
		if (isNull(bJSONObject.getString("def20"))) {
			bvo.setDef10(bJSONObject.getString("def20"));
		} else {
			throw new BusinessException("供应链税率不能为空，请检查输入数据");
		}
		// 供应链金额（不含税）
		if (isNull(bJSONObject.getString("def21"))) {
			bvo.setDef11(bJSONObject.getString("def21"));
		} else {
			throw new BusinessException("供应链金额（不含税）不能为空，请检查输入数据");
		}
		// 供应链金额（税额）
		if (isNull(bJSONObject.getString("def22"))) {
			bvo.setDef12(bJSONObject.getString("def22"));
		} else {
			throw new BusinessException("供应链金额（税额）不能为空，请检查输入数据");
		}
		// 加成率
		if (isNull(bJSONObject.getString("def23"))) {
			bvo.setDef13(bJSONObject.getString("def23"));
		} else {
			throw new BusinessException("加成率不能为空，请检查输入数据");
		}

		// 施工合同编号
		if (isNull(bJSONObject.getString("def36"))) {
			bvo.setDef14(bJSONObject.getString("def36"));
		} /*else {
			throw new BusinessException("施工合同编号不能为空，请检查输入数据");
		}*/
		// 施工合同名称
		if (isNull(bJSONObject.getString("def37"))) {
			bvo.setDef15(bJSONObject.getString("def37"));
		} /*else {
			throw new BusinessException("施工合同名称不能为空，请检查输入数据");
		}*/
		// 收货项目
		if (isNull(getpk_projectByCode(bJSONObject.getString("def39")))) {
			bvo.setDef16(getpk_projectByCode(bJSONObject.getString("def39")));
		} /*
		 * else { throw new BusinessException("收货项目位于NC关联为空，请检查输入数据"); }
		 */
		// 收货项目分期
		if (isNull(getpk_projectByCode(bJSONObject.getString("def41")))) {
			bvo.setDef17(getpk_projectByCode(bJSONObject.getString("def41")));
		} else {
			throw new BusinessException("收货项目分期不能为空，请检查输入数据");
		}
		// 对账单号
		if (isNull(bJSONObject.getString("def43"))) {
			bvo.setDef18(bJSONObject.getString("def43"));
		}

		// 对账单总金额
		if (isNull(bJSONObject.getString("def44"))) {
			bvo.setDef19(bJSONObject.getString("def44"));
		}
		// 供应商是否开票
		if (isNull(bJSONObject.getString("def45"))) {
			bvo.setDef20(bJSONObject.getString("def45"));
		} else {
			throw new BusinessException("供应商是否开票不能为空，请检查输入数据");
		}
		// 出库日期
		if (isNull(bJSONObject.getString("def48"))) {
			bvo.setDef23(bJSONObject.getString("def48"));
		} else {
			throw new BusinessException("出库日期不能为空，请检查输入数据");
		}

		// 物料编码
		if (isNull(bJSONObject.getString("def13"))) {
			bvo.setDef2(bJSONObject.getString("def13"));
		} else {
			throw new BusinessException("物料编码 不能为空，请检查输入数据");
		}
		// 物料说明
		if (isNull(bJSONObject.get("def14"))) {
			bvo.setDef3(bJSONObject.getString("def14"));
		} else {
			throw new BusinessException("物料说明 不能为空，请检查输入数据");
		}
		// 单位
		if (isNull(bJSONObject.getString("def15"))) {
			bvo.setDef4(bJSONObject.getString("def15"));
		} else {
			throw new BusinessException("单位 不能为空，请检查输入数据");
		}
		// 本次到货数量
		if (isNull(bJSONObject.getString("def16"))) {
			bvo.setDef5(bJSONObject.getString("def16"));
		} else {
			throw new BusinessException("本次到货数量 不能为空，请检查输入数据");
		}
		// 单价含税
		if (isNull(bJSONObject.getString("def17"))) {
			bvo.setDef6(bJSONObject.getString("def17"));
		} else {
			throw new BusinessException("单价含税 不能为空，请检查输入数据");
		}
		// 单价不含税
		if (isNull(bJSONObject.getString("def18"))) {
			bvo.setDef7(bJSONObject.getString("def18"));
		} else {
			throw new BusinessException("单价不含税 不能为空，请检查输入数据");
		}
		// 税率
		if (isNull(bJSONObject.getString("def25"))) {
			bvo.setTaxrate(new UFDouble(bJSONObject.getString("def25")));
		} else {
			throw new BusinessException("项目公司税率 不能为空，请检查输入数据");
		}
		// 税额
		if (isNull(bJSONObject.getString("def27"))) {
			bvo.setLocal_tax_de(new UFDouble(bJSONObject.getString("def27")));
		} else {
			throw new BusinessException("项目公司税额 不能为空，请检查输入数据");
		}
		// 不含税金额
		if (isNull(bJSONObject.getString("def26"))) {
			bvo.setNotax_de(new UFDouble(bJSONObject.getString("def26")));
		} else {
			throw new BusinessException("项目公司不含税金额 不能为空，请检查输入数据");
		}
		// 项目公司金额（含税）
		if (isNull(bJSONObject.getString("def24"))) {
			bvo.setLocal_money_de(new UFDouble(bJSONObject.getString("def24")));
			bvo.setMoney_de(new UFDouble(bJSONObject.getString("def24")));
		} else {
			throw new BusinessException("项目公司金额（含税） 不能为空，请检查输入数据");
		}
		bvos[0] = bvo;
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}

	/**
	 * 获取org_orgs的pk_org值
	 * 
	 * @param code传入的数据PK值
	 *            ，转换为数据库中的PK值
	 * 
	 * @return 返回转换后的PK值
	 */
	public String getPkByCode(String code) throws DAOException {
		String sql = "SELECT pk_org from org_orgs where code = '" + code
				+ "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_org = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	/**
	 * 获取org_orgs的pk_org值
	 * 
	 * @param code传入的数据PK值
	 *            ，转换为数据库中的PK值
	 * 
	 * @return 返回转换后的PK值
	 */
	public String getvidByorg(String pk_org) throws DAOException {
		String sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + pk_org
				+ "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_vid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_vid;
	}

	/**
	 * 获取项目的pk值
	 * 
	 * @param code传入的数据PK值
	 *            ，转换为数据库中的PK值
	 * 
	 * @return 返回转换后的PK值
	 */
	public String getprojectBycode(String code) throws DAOException {
		String sql = "select pk_project from bd_project where project_code = '"
				+ code + "' and nvl(dr, 0) =0";
		String pk_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}

	/**
	 * 获取bd_customer的pk_customer值
	 * 
	 * @param code传入的数据PK值
	 *            ，转换为数据库中的PK值
	 * 
	 * @return 返回转换后的PK值
	 */
	public String getcusPkByCode(String code) throws DAOException {
		String sql = "SELECT pk_customer from bd_customer where code = '"
				+ code + "' and nvl(dr,0)=0";
		String pk_customer = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_customer;
	}

	private Boolean selectRecbill(String def3, String pk)
			throws BusinessException {
		boolean check = false;

		int result = 0;

		String sql = "select count(1) from ar_recitem where nvl(dr,0)=0 and def8 = '"
				+ def3 + "' and pk_recbill = '" + pk + "' ";

		try {
			result = (int) getBaseDAO()
					.executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (result > 0) {
			check = true;
		}

		return check;
	}

	/**
	 * 判空方法
	 * 
	 * @param obj
	 * @return
	 */
	public Boolean isNull(Object obj) {
		if (!"".equals(obj) && obj != null) {
			return true;
		}
		return false;

	}
}
