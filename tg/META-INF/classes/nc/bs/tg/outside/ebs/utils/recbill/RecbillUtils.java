package nc.bs.tg.outside.ebs.utils.recbill;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.tgfn.outbill.OutbillBVO;
import nc.vo.tgfn.outbill.OutbillHVO;

import com.alibaba.fastjson.JSONObject;

/**
 * 领用出库单
 * 
 * @author king
 * 
 */
public class RecbillUtils extends EBSBillUtils {

	static RecbillUtils utils;

	public static RecbillUtils getUtils() {
		if (utils == null) {
			utils = new RecbillUtils();
		}
		return utils;
	}

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
	public String onRecBill(JSONObject bJSONObject) throws BusinessException {
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// 设置用户码
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		// JSONObject jsonObject = (JSONObject) value.get("data");// 获取表头信息
		// JSONObject accrualHeadVO = (JSONObject)
		// jsonObject.get("OutbillHVO");// 获取表头信息

		String srcid = bJSONObject.getString("def2");// 外系统主键
		String srcno = bJSONObject.getString("def1");// 外系统单据号
		// 目标业务单据中文名队列
		String billqueue = bJSONObject.getString("def49")
				+ bJSONObject.getString("def3");
		// 目标业务单据中文名键
		String billkey = bJSONObject.getString("def49") + srcno;
		// 根据销售系统业务单据ID查询的应付单聚合VO
		AggOutbillHVO aggVO = (AggOutbillHVO) getBillVO(AggOutbillHVO.class,
				"isnull(dr,0)=0 and def1 = '" + srcid + "'");

		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {
			// 参数传过来的VO对象
			AggOutbillHVO billvo = onTranBill(bJSONObject);
			HashMap eParam = new HashMap();
			// 工作项是否已检查
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			if (aggVO != null) {
				String pk = aggVO.getParentVO().getPk_outbill_h();
				boolean check = selectOutbill(bJSONObject.getString("def3"), pk);
				if (check) {
					/*
					 * throw new BusinessException("该" +
					 * bJSONObject.getString("def3") + "出库表体明细已存在");
					 */
					String def43 = bJSONObject.getString("def43");// 对账单号
					String def44 = bJSONObject.getString("def44");// 对账单总金额

					String sql = "update tgfn_outbill_o set def24 = '" + def43
							+ "',def25 = '" + def44
							+ "'  where nvl(dr,0)=0 and def14 = '"
							+ bJSONObject.getString("def3")
							+ "' and pk_outbill_h = '" + pk + "'";
					getBaseDAO().executeUpdate(sql);
					return null;
				}
				OutbillBVO bvo = new OutbillBVO();
				bvo = (OutbillBVO) billvo.getChildrenVO()[0];
				bvo.setPk_outbill_h(pk);
				bvo.setDr(0);
				getBaseDAO().insertVO(bvo);
				Double def8 = new Double(aggVO.getParentVO().getDef8())
						+ new Double(billvo.getParentVO().getDef8());
				Double def9 = new Double(aggVO.getParentVO().getDef9())
						+ new Double(billvo.getParentVO().getDef9());
				Double def10 = new Double(aggVO.getParentVO().getDef10())
						+ new Double(billvo.getParentVO().getDef10());
				String sql = "update tgfn_outbill_h set def8 = "
						+ def8.toString() + ",def9 = " + def9.toString()
						+ ",def10 = " + def10.toString()
						+ " where nvl(dr,0)=0 and pk_outbill_h = '" + pk + "'";
				getBaseDAO().executeUpdate(sql);
			} else {
				getPfBusiAction().processAction("SAVEBASE", "FN04", null,
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
	private AggOutbillHVO onTranBill(JSONObject bJSONObject)
			throws BusinessException {
		AggOutbillHVO aggvo = new AggOutbillHVO();
		aggvo.getChildrenVO();
		OutbillHVO hvo = new OutbillHVO();
		// json数据头

		// JSONObject date = (JSONObject) value.get("data");
		// 单号
		// JSONObject headjson = (JSONObject) date.get("OutbillHVO");
		String pk_org = getPkByCode(bJSONObject.getString("pk_org"), "pk_org");
		String supplier = getPkByCode(bJSONObject.getString("supplier"),
				"supplier");
		String customer = getPkByCode(bJSONObject.getString("customer"),
				"customer");
		if (!isNull(pk_org)) {
			throw new BusinessException("财务组织未与NC关联，请检查输入数据");
		}
		if (!isNull(supplier)) {
			throw new BusinessException("供应商未与NC关联，请检查输入数据");
		}
		// json数据体
		// JSONArray bodyjson = (JSONArray) date.get("OutbillBVO");
		String srcid = bJSONObject.getString("def2");// 外系统id
		String srcno = bJSONObject.getString("def1");// 外系统单据号
		String purchasecode = bJSONObject.getString("def11");// 采购协议编码
		String purchasename = bJSONObject.getString("def12");// 采购协议名称
		String purordercode = bJSONObject.getString("def4");// 采购订单编码
		if ("供应链销售出库".equals(bJSONObject.get("def49"))) {
			if (!isNull(customer)) {
				throw new BusinessException("客户未与NC关联，请检查输入数据");
			}
			if (!isNull(purchasecode)) {
				throw new BusinessException("采购协议编码不能为空，请检查输入数据");
			}
			if (!isNull(purchasename)) {
				throw new BusinessException("采购协议名称不能为空，请检查输入数据");
			}
		}

		hvo.setPk_group("000112100000000005FD");// 集团
		hvo.setPk_org(pk_org);// 组织
		hvo.setBilltype("FN04");// 单据状态
		hvo.setBillstatus(null);// 单据状态
		hvo.setApprovestatus(-1);// 审批状态
		hvo.setEffectstatus(0);// 生效状态
		hvo.setDef11(supplier);// 供应商
		hvo.setDef12(customer);// 客户
		hvo.setBilldate(new UFDate());// 单据日期

		if (!"".equals(bJSONObject.getString("def51"))
				&& bJSONObject.getString("def51") != null) {
			String def18 = getPkByCode(bJSONObject.getString("def51"), "pk_org");
			if ("".equals(def18) || def18 == null) {
				throw new BusinessException("项目公司有误,NC无对应公司");
			}
			hvo.setDef18(def18);// 项目公司

		}
		hvo.setDef15(bJSONObject.getString("def52"));// 内部开票发票抬头公司
		hvo.setDef16(bJSONObject.getString("def53"));// 内部交易收款合同编码
		hvo.setDef17(bJSONObject.getString("def54"));// 内部交易收款合同名称

		UFDouble dhjebhs = UFDouble.ZERO_DBL;
		UFDouble dhjese = UFDouble.ZERO_DBL;
		UFDouble dhjehs = UFDouble.ZERO_DBL;

		if ("供应链销售出库".equals(bJSONObject.getString("def49"))) {
			// 出库金额（不含税）
			dhjebhs = new UFDouble(bJSONObject.getString("def21"));
			// 出库金额（税额）
			dhjese = new UFDouble(bJSONObject.getString("def22"));
			// 出库金额（含税）
			dhjehs = new UFDouble(bJSONObject.getString("def19"));
		} else {
			// 出库金额（不含税）
			dhjebhs = (new UFDouble(bJSONObject.getString("def26")));
			// 出库金额（税额）
			dhjese = new UFDouble(bJSONObject.getString("def27"));
			// 出库金额（含税）
			dhjehs = new UFDouble(bJSONObject.getString("def24"));
		}
		hvo.setDef10(dhjese.toString());
		hvo.setDef9(dhjebhs.toString());
		hvo.setDef8(dhjehs.toString());

		if (isNull(srcid)) {
			hvo.setDef1(srcid);
		} else {
			throw new BusinessException(" 外系统id不能为空，请检查输入数据");
		}
		if (isNull(srcno)) {
			hvo.setDef2(srcno);
		} else {
			throw new BusinessException(" 外系统单据号不能为空，请检查输入数据");
		}
		if (isNull(purchasecode)) {
			hvo.setDef5(purchasecode);
		}
		if (isNull(purchasename)) {
			hvo.setDef6(purchasename);
		}
		if (isNull(purordercode)) {
			hvo.setDef7(purordercode);
		} else {
			throw new BusinessException(" 采购订单编码不能为空，请检查输入数据");
		}
		if (isNull(bJSONObject.getString("def5"))) {
			hvo.setDef13(bJSONObject.getString("def5"));
		} else {
			throw new BusinessException(" 采购订单id不能为空，请检查输入数据");
		}

		aggvo.setParentVO(hvo);

		OutbillBVO[] bvos = new OutbillBVO[1];
		for (int i = 0; i < 1; i++) {
			OutbillBVO bvo = new OutbillBVO();
			String def14 = bJSONObject.getString("def3");// 行id
			String def13 = bJSONObject.getString("def48");// 出库日期
			String project_c = getPkByCode(bJSONObject.getString("def41"),
					"project");// 项目分期
			String supplymny_in = bJSONObject.getString("def19");// 供应链金额（含税）
			String supply_rate = bJSONObject.getString("def20");// 供应链税率
			String supplymny_out = bJSONObject.getString("def21");// 供应链金额（不含税）
			String supplymny_tax = bJSONObject.getString("def22");// 供应链金额（税额）
			String additionrate = bJSONObject.getString("def23");// 加成率
			String compnymny_in = bJSONObject.getString("def24");// 项目公司金额（含税）
			String compny_rate = bJSONObject.getString("def25");// 项目公司税率
			String compnymny_out = bJSONObject.getString("def26");// 项目公司金额（不含税）
			String compnymny_tax = bJSONObject.getString("def27");// 项目公司金额（税额）

			if ("供应链销售出库".equals(bJSONObject.get("def49"))) {
				if (!isNull(supplymny_in)) {
					throw new BusinessException("供应链金额（含税）不能为空，请检查输入数据");
				}
				if (!isNull(supply_rate)) {
					throw new BusinessException("供应链税率不能为空，请检查输入数据");
				}
				if (!isNull(supplymny_out)) {
					throw new BusinessException("供应链金额（不含税）不能为空，请检查输入数据");
				}
				if (!isNull(supplymny_tax)) {
					throw new BusinessException("供应链金额（税额）不能为空，请检查输入数据");
				}
			}
			if ("项目公司领用出库".equals(bJSONObject.get("def49"))) {
				if (!isNull(additionrate)) {
					throw new BusinessException("加成率不能为空，请检查输入数据");
				}
				if (!isNull(compnymny_in)) {
					throw new BusinessException("项目公司金额（含税）不能为空，请检查输入数据");
				}
				if (!isNull(compny_rate)) {
					throw new BusinessException("项目公司税率不能为空，请检查输入数据");
				}
				if (!isNull(compnymny_out)) {
					throw new BusinessException("项目公司金额（不含税）不能为空，请检查输入数据");
				}
				if (!isNull(compnymny_tax)) {
					throw new BusinessException("项目公司金额（税额）不能为空，请检查输入数据");
				}
			}

			if (isNull(def13)) {
				bvo.setDef15(def13);
			}
			if (isNull(def14)) {
				bvo.setDef14(def14);
			}
			if (isNull(project_c)) {
				bvo.setDef3(project_c);
			} else {
				throw new BusinessException("【"
						+ bJSONObject.getString("def49") + "】收货项目分期未与NC关联");
			}
			if (isNull(supplymny_in)) {
				bvo.setDef5(supplymny_in);
			}

			if (isNull(supply_rate)) {
				bvo.setDef6(supply_rate);
			}

			if (isNull(supplymny_out)) {
				bvo.setDef7(supplymny_out);
			}

			if (isNull(supplymny_tax)) {
				bvo.setDef8(supplymny_tax);
			}

			if (isNull(additionrate)) {
				bvo.setDef9(additionrate);
			}

			if (isNull(compnymny_in)) {
				bvo.setDef10(compnymny_in);
			}

			if (isNull(compny_rate)) {
				bvo.setDef11(compny_rate);
			}

			if (isNull(compnymny_out)) {
				bvo.setDef12(compnymny_out);
			}

			if (isNull(compnymny_tax)) {
				bvo.setDef13(compnymny_tax);
			}

			if (isNull(bJSONObject.getString("def13"))) {
				bvo.setDef16(bJSONObject.getString("def13"));
			} else {
				throw new BusinessException(" 物料编码不能为空，请检查输入数据");
			}
			if (isNull(bJSONObject.getString("def14"))) {
				bvo.setDef17(bJSONObject.getString("def14"));
			} else {
				throw new BusinessException(" 物料说明不能为空，请检查输入数据");
			}
			if (isNull(bJSONObject.getString("def15"))) {
				bvo.setDef18(bJSONObject.getString("def15"));
			} else {
				throw new BusinessException(" 单位不能为空，请检查输入数据");
			}
			if (isNull(bJSONObject.getString("def16"))) {
				bvo.setDef19(bJSONObject.getString("def16"));
			} else {
				throw new BusinessException(" 本次到货数量不能为空，请检查输入数据");
			}
			if (isNull(bJSONObject.getString("def17"))) {
				bvo.setDef20(bJSONObject.getString("def17"));
			} else {
				throw new BusinessException(" 单价含税不能为空，请检查输入数据");
			}
			if (isNull(bJSONObject.getString("def18"))) {
				bvo.setDef21(bJSONObject.getString("def18"));
			} else {
				throw new BusinessException(" 单价不含税不能为空，请检查输入数据");
			}

			if (isNull(bJSONObject.getString("def36"))) {
				bvo.setDef22(bJSONObject.getString("def36"));
			} /*else {
				throw new BusinessException(" 施工合同编号不能为空，请检查输入数据");
			}*/
			if (isNull(bJSONObject.getString("def37"))) {
				bvo.setDef23(bJSONObject.getString("def37"));
			} /*
			 * else { throw new BusinessException(" 施工合同名称不能为空，请检查输入数据"); }
			 */
			// 对账单号
			if (isNull(bJSONObject.getString("def43"))) {
				bvo.setDef24(bJSONObject.getString("def43"));
			}
			// 对账单总金额
			if (isNull(bJSONObject.getString("def44"))) {
				bvo.setDef25(bJSONObject.getString("def44"));
			}
			// 供应商是否开票(Y/N)
			if (isNull(bJSONObject.getString("def45"))) {
				bvo.setDef26("Y".equals(bJSONObject.getString("def45")) ? "Y"
						: "N");
			}
			// 采购订单行id
			if (isNull(bJSONObject.getString("def6"))) {
				bvo.setDef27(bJSONObject.getString("def6"));
			} else {
				throw new BusinessException(" 采购订单行id能为空，请检查输入数据");
			}

			bvos[i] = bvo;
		}
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}

	/**
	 * 
	 * 
	 * @param code
	 *            传入的数据PK值，转换为数据库中的PK值
	 * @return 返回转换后的PK值
	 */
	public String getPkByCode(String code, String key) throws DAOException {

		String result = null;
		String sql = null;
		if ("pk_org".equals(key)) {
			sql = "SELECT pk_org from org_orgs where code = '" + code
					+ "' and enablestate = 2 and nvl(dr,0)=0";
		}
		if ("project".equals(key)) {
			sql = "SELECT pk_project from bd_project where project_code = '"
					+ code + "' and enablestate = 2 and nvl(dr,0)=0";
		}
		if ("supplier".equals(key)) {
			sql = "SELECT pk_supplier from bd_supplier where code = '" + code
					+ "' and nvl(dr,0)=0";
		}
		if ("customer".equals(key)) {
			sql = "SELECT pk_customer from bd_customer where code = '" + code
					+ "' and nvl(dr,0)=0";
		}
		if (sql != null) {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}

		return result;
	}

	private Boolean selectOutbill(String def3, String pk)
			throws BusinessException {
		boolean check = false;

		int result = 0;

		String sql = "select count(1) from tgfn_outbill_o where nvl(dr,0)=0 and def14 = '"
				+ def3 + "' and pk_outbill_h = '" + pk + "'";

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
