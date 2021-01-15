package nc.bs.tg.outside.ebs.utils.reachgoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.ComCopewithUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.WareToReceivableUtils;
import nc.bs.tg.outside.ebs.utils.recbill.RecbillUtils;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.vo.tgfn.temporaryestimate.Business;
import nc.vo.tgfn.temporaryestimate.Temest;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * SRM-到货单->到货明细表，暂估应付工单
 * 
 * @author huangxj
 * 
 */
public class ReachGoodsUtils extends EBSBillUtils {
	static ReachGoodsUtils utils;

	public static ReachGoodsUtils getUtils() {
		if (utils == null) {
			utils = new ReachGoodsUtils();
		}
		return utils;
	}

	/**
	 * SRM到货/出库 分存NC各个单据 huangxj 2019年11月15日14:19:15
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException7
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		// 处理表单信息
		JSONObject jsonData = (JSONObject) value.get("data");
		String jsonbody = jsonData.getString("BodyVO");

		JSONArray jsonArr = JSON.parseArray(jsonbody);
		if (jsonArr.size() < 1) {
			throw new BusinessException("到货信息为空");
		}
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject bJSONObject = jsonArr.getJSONObject(i);

			/*// 检验是否有对应的采购协议

			if (!"".equals(bJSONObject.get("def11"))
					|| bJSONObject.get("def11") != null) {

				AggCtApVO ApVO = (AggCtApVO) getBillVO(
						AggCtApVO.class,
						"isnull(dr,0)=0 and vbillcode = '"
								+ bJSONObject.get("def11") + "' and ctname = '"
								+ bJSONObject.get("def12") + "'");

				if (ApVO == null) {
					throw new BusinessException("请先同步协议到NC，协议编码："
							+ bJSONObject.get("def11") + "，协议名称："
							+ bJSONObject.get("def12"));
				}
			}*/

			// 接受附件信息
			JSONArray JSarr = bJSONObject.getJSONArray("attachment");
			if (JSarr != null) {

				for (int j = 0; j < JSarr.size(); j++) {
					JSONObject JSObj = (JSONObject) JSarr.get(j);

					String srmno = JSObj.getString("srmno");
					String srmid = JSObj.getString("srmid");
					String srmtype = JSObj.getString("srmtype");
					String att_name = JSObj.getString("att_name");
					String att_address = JSObj.getString("att_address");

					String select_sql = "select count(1) from attachment where nvl(dr,0) = 0 and srmno = '"
							+ srmno + "' and srmid = '" + srmid + "'";

					int num = (int) getBaseDAO().executeQuery(select_sql,
							new ColumnProcessor());

					if (num == 0) {

						String att_sql = "insert into attachment values('"
								+ srmno + "','" + srmid + "','" + srmtype
								+ "','" + att_name + "','" + att_address
								+ "',0)";

						getBaseDAO().executeUpdate(att_sql);
					}
				}
			}

			if ("供应链到货接收".equals(bJSONObject.get("def49"))) {
				// 校验暂估应付工单
				validateBodyData(bJSONObject);
				// 生成暂估应付工单
				onTranTemestBill(bJSONObject);
			} else if ("供应链销售出库".equals(bJSONObject.get("def49"))) {
				// 生成出库工单
				RecbillUtils.getUtils().onRecBill(bJSONObject);
				// 材料应收单
				WareToReceivableUtils.getUtils().onpushBill(bJSONObject);
			} else if ("供应链退货".equals(bJSONObject.get("def49"))) {
				validateBodyData(bJSONObject);
				// 暂估应付工单（负）
				onTranTemestBill(bJSONObject);
			} else if ("项目公司到货接收".equals(bJSONObject.get("def49"))) {
				// 校验暂估应付工单
				validateBodyData(bJSONObject);
				// 生成暂估应付工单
				onTranTemestBill(bJSONObject);
				// 生成材料应付单
				ComCopewithUtils.getUtils().onpushBill(bJSONObject);
			} else if ("项目公司领用出库".equals(bJSONObject.get("def49"))) {
				// 生成出库工单
				RecbillUtils.getUtils().onRecBill(bJSONObject);
			} else if ("项目公司退货".equals(bJSONObject.get("def49"))) {
				validateBodyData(bJSONObject);
				// 生成暂估应付工单（负）
				onTranTemestBill(bJSONObject);
			} else {
				throw new BusinessException("单据类型为空或输入有误，请检查参数设置");
			}
		}

		return JSON.toJSONString("SRM-到货 接受完成");
	}

	/**
	 * SRM-到货单->到货明细表，暂估应付工单
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onReachBill(JSONObject bJSONObject) throws BusinessException {
		return null;
	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 * @return
	 */
	private void onTranTemestBill(JSONObject bJSONObject)
			throws BusinessException {
		// / 队列集合
		List<String> queues = new ArrayList<String>();

		// 检验每一行表体，条件：接收行ID不能重复
		String srcid = bJSONObject.getString("def3"); // 接收行ID

		// 接收行ID唯一作队列与日志
		String billqueue;
		billqueue = "SRM-到货单->暂估应付工单:" + srcid;
		queues.add(billqueue);

		AggTemest aggTemestVO = (AggTemest) getBillVO(AggTemest.class,
				"isnull(dr,0)=0 and def7 = '" + bJSONObject.getString("def2")
						+ "'");
		String pk = "";
		if (aggTemestVO != null) {
			pk = aggTemestVO.getParentVO().getPk_temest();
		}

		// 检查暂估工单是否有重复上传的到货单信息
		boolean check = selectTemp(bJSONObject.getString("def3"), pk);
		if (check) {
			// if (("项目公司到货接收".equals(bJSONObject.getString("def49")) ||
			// "供应链到货接收"
			// .equals(bJSONObject.get("def49")))) {
			// //onTranAGoodsDetail(bJSONObject);
			// } else {
			String def43 = bJSONObject.getString("def43");// 对账单号
			String def44 = bJSONObject.getString("def44");// 对账单总金额

			String sql = "update tgfn_temestbus set def43 = '" + def43
					+ "',def44 = '" + def44
					+ "'  where nvl(dr,0)=0 and def3 = '"
					+ bJSONObject.getString("def3") + "' and pk_temest = '"
					+ pk + "'";
			getBaseDAO().executeUpdate(sql);
			// }
			return;
		}

		AggTemest aggvo = new AggTemest();

		Temest hvo = new Temest();
		// 默认集团-时代集团-code:0001
		hvo.setPk_group("000112100000000005FD");
		if (getRefAttributePk("pk_org", bJSONObject.getString("pk_org")) != null) {
			hvo.setPk_org(getRefAttributePk("pk_org",
					bJSONObject.getString("pk_org")));
		} else {
			throw new BusinessException("该财务组织编码未与NC关联");
		}

		// 单据日期
		hvo.setBilldate(new UFDate());
		// 默认单据类型-code:FN03
		hvo.setBilltype("FN03");

		// 默认审批状态-1：自由
		hvo.setApprovestatus(-1);
		// 默认生效状态0：未生效
		hvo.setEffectstatus(0);
		// 往来对象-默认供应商
		hvo.setObjtype(1);
		// 是否退货
		hvo.setDef6("Y".equals(bJSONObject.getString("def50")) ? UFBoolean.TRUE
				: UFBoolean.FALSE);

		UFDouble dhjebhs = UFDouble.ZERO_DBL;
		UFDouble dhjese = UFDouble.ZERO_DBL;
		UFDouble dhjehs = UFDouble.ZERO_DBL;

		if ("供应链到货接收".equals(bJSONObject.getString("def49"))
				|| "项目公司到货接收".equals(bJSONObject.getString("def49"))) {

			if ("供应链到货接收".equals(bJSONObject.getString("def49"))) {
				// 到货金额（不含税）
				dhjebhs = new UFDouble(bJSONObject.getString("def21"));
				// 到货金额（税额）
				dhjese = new UFDouble(bJSONObject.getString("def22"));
				// 到货金额（含税）
				dhjehs = new UFDouble(bJSONObject.getString("def19"));

			} else {
				dhjebhs = new UFDouble(bJSONObject.getString("def26"));
				dhjese = new UFDouble(bJSONObject.getString("def27"));
				dhjehs = new UFDouble(bJSONObject.getString("def24"));
			}

		} else {

			if ("供应链退货".equals(bJSONObject.get("def49"))) {
				// 到货金额（不含税）
				dhjebhs = new UFDouble(bJSONObject.getString("def21"))
						.multiply(-1);
				// 到货金额（税额）
				dhjese = new UFDouble(bJSONObject.getString("def22"))
						.multiply(-1);
				// 到货金额（含税）
				dhjehs = new UFDouble(bJSONObject.getString("def19"))
						.multiply(-1);
			} else {
				dhjebhs = new UFDouble(bJSONObject.getString("def26"))
						.multiply(-1);
				dhjese = new UFDouble(bJSONObject.getString("def27"))
						.multiply(-1);
				dhjehs = new UFDouble(bJSONObject.getString("def24"))
						.multiply(-1);
			}

		}
		hvo.setDef3(dhjebhs);
		hvo.setDef4(dhjese);
		hvo.setDef5(dhjehs);

		hvo.setSupplier(getRefAttributePk("supplier",
				bJSONObject.getString("supplier")));// 客商
		hvo.setDef7(bJSONObject.getString("def2"));// 接收id
		hvo.setDef8(bJSONObject.getString("def1"));// 接收编号

		if (!"".equals(bJSONObject.getString("def51"))
				&& bJSONObject.getString("def51") != null) {
			hvo.setDef9(getRefAttributePk("pk_org",
					bJSONObject.getString("def51")));// 项目公司
			if ("".equals(hvo.getDef9()) || hvo.getDef9() == null) {
				throw new BusinessException("项目公司未与NC关联");
			}
		}
		hvo.setDef10(bJSONObject.getString("def52"));// 内部开票发票抬头公司
		hvo.setDef11(bJSONObject.getString("def53"));// 内部交易收款合同编码
		hvo.setDef12(bJSONObject.getString("def54"));// 内部交易收款合同名称

		aggvo.setParentVO(hvo);

		Business[] bvos = new Business[1];
		for (int i = 0; i < 1; i++) {
			Business bvo = new Business();
			// JSONObject bJSONObject = bodyArray.getJSONObject(i);
			bvo.setDr(0);
			// bvo.setSupplier(getRefAttributePk("supplier",
			// bJSONObject.getString("supplier")));// 采购供应商
			bvo.setDef3(bJSONObject.getString("def3"));// 接收行id
			bvo.setDef4(bJSONObject.getString("def4"));// 订单编码
			bvo.setDef5(bJSONObject.getString("def5"));// 订单id
			bvo.setDef6(bJSONObject.getString("def6"));// 订单行id
			bvo.setDef7(bJSONObject.getString("def7"));// 原接收编号
			// bvo.setDef8(bJSONObject.getString("def8"));// 会计科目编码（待定）
			// bvo.setDef9(bJSONObject.getString("def9"));// 会计科目名称（待定）
			// bvo.setDef10(bJSONObject.getString("def10"));// 供应商编码
			bvo.setDef11(bJSONObject.getString("def11"));// 采购协议编号
			bvo.setDef12(bJSONObject.getString("def12"));// 采购协议名称
			bvo.setDef13(bJSONObject.getString("def13"));// 物料编码
			bvo.setDef14(bJSONObject.getString("def14"));// 物料说明
			bvo.setDef15(bJSONObject.getString("def15"));// 单位
			if ("供应链到货接收".equals(bJSONObject.getString("def49"))
					|| "项目公司到货接收".equals(bJSONObject.getString("def49"))) {
				bvo.setDef20(new UFDouble(bJSONObject.getString("def20")));// 供应链税率
				bvo.setDef16(new UFDouble(bJSONObject.getString("def16")));// 本次到货数量
				bvo.setDef23(new UFDouble(bJSONObject.getString("def23")));// 加成率
				bvo.setDef25(new UFDouble(bJSONObject.getString("def25")));// 项目公司税率
				bvo.setDef17(new UFDouble(bJSONObject.getString("def17")));// 含税单价
				bvo.setDef18(new UFDouble(bJSONObject.getString("def18")));// 单价不含税
				bvo.setDef19(new UFDouble(bJSONObject.getString("def19")));// 供应链金额（含税）
				bvo.setDef21(new UFDouble(bJSONObject.getString("def21")));// 供应链金额（不含税）
				bvo.setDef22(new UFDouble(bJSONObject.getString("def22")));// 供应链金额（税额）
				bvo.setDef24(new UFDouble(bJSONObject.getString("def24")));// 项目公司金额（含税）
				bvo.setDef26(new UFDouble(bJSONObject.getString("def26")));// 项目公司金额（不含税）
				bvo.setDef27(new UFDouble(bJSONObject.getString("def27")));// 项目公司金额（税额）
			} else {
				bvo.setDef20((new UFDouble(bJSONObject.getString("def20")))
						.multiply(-1));// 供应链税率
				bvo.setDef16((new UFDouble(bJSONObject.getString("def16")))
						.multiply(-1));// 本次到货数量
				bvo.setDef23((new UFDouble(bJSONObject.getString("def23")))
						.multiply(-1));// 加成率
				bvo.setDef25((new UFDouble(bJSONObject.getString("def25")))
						.multiply(-1));// 项目公司税率
				bvo.setDef17((new UFDouble(bJSONObject.getString("def17")))
						.multiply(-1));// 含税单价
				bvo.setDef18((new UFDouble(bJSONObject.getString("def18")))
						.multiply(-1));// 单价不含税
				bvo.setDef19((new UFDouble(bJSONObject.getString("def19")))
						.multiply(-1));// 供应链金额（含税）
				bvo.setDef21((new UFDouble(bJSONObject.getString("def21")))
						.multiply(-1));// 供应链金额（不含税）
				bvo.setDef22((new UFDouble(bJSONObject.getString("def22")))
						.multiply(-1));// 供应链金额（税额）
				bvo.setDef24((new UFDouble(bJSONObject.getString("def24")))
						.multiply(-1));// 项目公司金额（含税）
				bvo.setDef26((new UFDouble(bJSONObject.getString("def26")))
						.multiply(-1));// 项目公司金额（不含税）
				bvo.setDef27((new UFDouble(bJSONObject.getString("def27")))
						.multiply(-1));// 项目公司金额（税额）
			}

			bvo.setDef31(bJSONObject.getString("def43"));// 对账单号
			bvo.setDef32(bJSONObject.getString("def44"));// 对账单总金额
			bvo.setDef33(StringUtils.isBlank(bJSONObject.getString("def45")) ? "N"
					: bJSONObject.getString("def45"));// 供应商是否开票(Y/N)

			bvo.setDef36(bJSONObject.getString("def36"));// 施工合同编号
			bvo.setDef37(bJSONObject.getString("def37"));// 施工合同名称
			bvo.setDef41(bJSONObject.getString("def38"));// 到货/退货确认日期

			String def28 = getRefAttributePk("pk_project",
					bJSONObject.getString("def39"));
			/*if (def28 == null || "".equals(def28)) {
				throw new BusinessException("【"
						+ bJSONObject.getString("def49") + "】收货项目未与NC关联");
			}*/
			bvo.setDef28(def28);// 收货项目
			
			String def30 = getRefAttributePk("pk_project",
					bJSONObject.getString("def41"));
			
			if (def30 == null || "".equals(def30)) {
				throw new BusinessException("【"
						+ bJSONObject.getString("def49") + "】收货项目分期未与NC关联");
			}
			bvo.setDef30(def30);// 收货项目分期
			
			bvo.setDef38(bJSONObject.getString("def46"));// 项目公司采购成本合同编号
			bvo.setDef39(bJSONObject.getString("def47"));// 项目公司采购成本合同名称
			bvo.setDef40(bJSONObject.getString("def48"));// 退货日期

			bvos[i] = bvo;
		}

		// 增加队列处理
		EBSBillUtils.addBillQueue(queues.toString());
		try {
			HashMap<String, String> eParam = new HashMap<String, String>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			if (aggTemestVO != null) {
				bvos[0].setPk_temest(pk);
				getBaseDAO().insertVO(bvos[0]);// dhjehs dhjese dhjebhs
				String sql = "update tgfn_temest set def3 = def3+"
						+ dhjebhs.doubleValue() + ",def4=def4+"
						+ dhjese.doubleValue() + ",def5=def5+"
						+ dhjehs.doubleValue()
						+ " where nvl(dr,0)=0 and pk_temest = '" + pk + "'";
				getBaseDAO().executeUpdate(sql);
			} else {
				aggvo.setChildrenVO(bvos);
				getPfBusiAction().processAction("SAVEBASE", "FN03", null,
						aggvo, null, null);
			}
		} catch (Exception e) {
			throw new BusinessException("【" + queues.toString() + "】,"
					+ e.getMessage(), e);
		} finally {
			EBSBillUtils.removeBillQueue(queues.toString());
		}
		/*
		 * if (aggvo != null &&
		 * ("项目公司到货接收".equals(bJSONObject.getString("def49")) || "供应链到货接收"
		 * .equals(bJSONObject.get("def49")))) {
		 * onTranAGoodsDetail(bJSONObject); }
		 */
		return;

	}

	/*
	 * private void onTranAGoodsDetail(JSONObject bJSONObject) throws
	 * BusinessException { // / 队列集合 List<String> queues = new
	 * ArrayList<String>();
	 * 
	 * // 检验每一行表体，条件：接收行ID不能重复 String srcid = bJSONObject.getString("def3"); //
	 * 接收ID // 接收行ID唯一作队列与日志 String billqueue; billqueue =
	 * bJSONObject.getString("def49") + srcid; queues.add(billqueue);
	 * 
	 * // 检查到货明细表是否有重复上传的到货单信息 boolean check =
	 * selectGoods(bJSONObject.getString("def3"));
	 * 
	 * if (check) { // 第二次则改变对账单号和对账金额 upadteGoodsDetail(bJSONObject); return; }
	 * AggAGoodsDetail aggvo = new AggAGoodsDetail();
	 * 
	 * AGoodsDetail hvo = new AGoodsDetail(); // 默认集团-时代集团-code:0001
	 * hvo.setPk_group("000112100000000005FD"); // 组织-时代集团-code:def32
	 * hvo.setPk_org(getRefAttributePk("pk_org",
	 * bJSONObject.getString("pk_org"))); // 单据日期 hvo.setBilldate(new UFDate());
	 * // 默认单据类型-code:FN20 hvo.setBilltype("FN20"); // 默认审批状态-1：自由
	 * hvo.setApprovestatus(-1);
	 * 
	 * hvo.setDef1(bJSONObject.getString("def1"));// 接收编号
	 * hvo.setDef2(bJSONObject.getString("def2"));// 接收id
	 * hvo.setDef3(bJSONObject.getString("def3"));// 接收行id
	 * hvo.setDef4(bJSONObject.getString("def4"));// 订单编码
	 * hvo.setDef5(bJSONObject.getString("def5"));// 订单id
	 * hvo.setDef6(bJSONObject.getString("def6"));// 订单行id //
	 * hvo.setDef7(bodyObj.getString("def7"));业务类型（退货/入库/领料/出库/杂收/杂发） //
	 * hvo.setDef8(bodyObj.getString("def7"));// 原接收编号 //
	 * hvo.setDef9(bodyObj.getString("def8"));// 会计科目编码（待定） //
	 * hvo.setDef10(bodyObj.getString("def9"));// 会计科目名称（待定） //
	 * hvo.setDef11(bodyObj.getString("def10"));// 供应商编码
	 * hvo.setDef12(getRefAttributePk("supplier",
	 * bJSONObject.getString("supplier")));// 采购供应商-参照
	 * hvo.setDef13(bJSONObject.getString("def11"));// 协议编号
	 * hvo.setDef14(bJSONObject.getString("def12"));// 合同名称(协议名称)
	 * hvo.setDef15(bJSONObject.getString("def13"));// 物料编码
	 * hvo.setDef16(bJSONObject.getString("def14"));// 物料说明
	 * hvo.setDef17(bJSONObject.getString("def15"));// 单位 hvo.setDef18(new
	 * UFDouble(bJSONObject.getString("def16")));// 本次到货数量 hvo.setDef19(new
	 * UFDouble(bJSONObject.getString("def17")));// 单价含税 hvo.setDef20(new
	 * UFDouble(bJSONObject.getString("def18")));// 单价不含税 hvo.setDef21(new
	 * UFDouble(bJSONObject.getString("def19")));// 供应链金额（含税） hvo.setDef22(new
	 * UFDouble(bJSONObject.getString("def20")));// 供应链税率 hvo.setDef23(new
	 * UFDouble(bJSONObject.getString("def21")));// 供应链金额（不含税） hvo.setDef24(new
	 * UFDouble(bJSONObject.getString("def22")));// 供应链金额（税额） hvo.setDef25(new
	 * UFDouble(bJSONObject.getString("def23")));// 加成率 hvo.setDef26(new
	 * UFDouble(bJSONObject.getString("def24")));// 项目公司金额（含税） hvo.setDef27(new
	 * UFDouble(bJSONObject.getString("def25")));// 项目公司税率 hvo.setDef28(new
	 * UFDouble(bJSONObject.getString("def26")));// 项目公司金额（不含税） hvo.setDef29(new
	 * UFDouble(bJSONObject.getString("def27")));// 项目公司金额（税额）
	 * 
	 * hvo.setDef46(bJSONObject.getString("def36"));// 施工合同编号
	 * hvo.setDef47(bJSONObject.getString("def37"));// 施工合同名称 hvo.setDef30(new
	 * UFDateTime(bJSONObject.getString("def38")));// 到货确认日期
	 * 
	 * String def33 = getRefAttributePk("def28",
	 * bJSONObject.getString("def41")); if (def33 == null || "".equals(def33)) {
	 * throw new BusinessException("【" + bJSONObject.getString("def49") +
	 * "】收货项目分期未与NC关联"); }
	 * 
	 * hvo.setDef33(def33);// 收货项目分期-参照项目
	 * hvo.setDef37(bJSONObject.getString("def43"));// 对账单号 hvo.setDef38(new
	 * UFDouble(bJSONObject.getString("def44")));// 对账单总金额
	 * hvo.setDef39(StringUtils.isBlank(bJSONObject.getString("def45")) ?
	 * UFBoolean.FALSE : new UFBoolean(bJSONObject.getString("def45")));//
	 * 供应商是否开票(Y/N) hvo.setDef49(bJSONObject.getString("def46"));// 项目公司采购成本合同编号
	 * hvo.setDef50(bJSONObject.getString("def47"));// 项目公司采购成本合同名称
	 * aggvo.setParentVO(hvo); try { HashMap<String, String> eParam = new
	 * HashMap<String, String>(); eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
	 * PfUtilBaseTools.PARAM_NOTE_CHECKED);
	 * 
	 * getPfBusiAction().processAction("SAVEBASE", "FN20", null, aggvo, null,
	 * eParam); } catch (Exception e) { throw new BusinessException("【" +
	 * queues.toString() + "】," + e.getMessage(), e); } finally {
	 * EBSBillUtils.removeBillQueue(queues.toString()); } return; }
	 */

	private void validateBodyData(JSONObject data) throws BusinessException {

		// 财务组织
		String pkOrg = data.getString("pk_org");
		// 客商
		String supplier = data.getString("supplier");

		if (pkOrg == null || "".equals(pkOrg)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】财务组织不能为空");
		}
		if (supplier == null || "".equals(supplier)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】客商不能为空");
		}

		// 接收编号
		String def1 = data.getString("def1");
		// 接收id
		String def2 = data.getString("def2");
		// 接收行id
		String def3 = data.getString("def3");
		// 订单编码
		String def4 = data.getString("def4");
		// 订单id
		String def5 = data.getString("def5");
		// 订单行id
		String def6 = data.getString("def6");
		// 原接收编号
		String def7 = data.getString("def6");
		// 采购协议编码
		String def11 = data.getString("def11");
		// 采购协议名称
		String def12 = data.getString("def12");
		// 物料编码
		String def13 = data.getString("def13");
		// 物料说明
		String def14 = data.getString("def14");
		// 单位
		String def15 = data.getString("def15");
		// 本次到货数量
		String def16 = data.getString("def16");
		// 单价含税
		String def17 = data.getString("def17");
		// 单价不含税
		String def18 = data.getString("def18");
		// 供应链金额（含税）
		String def19 = data.getString("def19");
		// 供应链税率
		String def20 = data.getString("def20");
		// 供应链金额（不含税）
		String def21 = data.getString("def21");
		// 供应链金额（税额）
		String def22 = data.getString("def22");
		// 加成率
		String def23 = data.getString("def23");
		// 项目公司金额（含税）
		String def24 = data.getString("def24");
		// 项目公司税率
		String def25 = data.getString("def25");
		// 项目公司金额（不含税）
		String def26 = data.getString("def26");
		// 项目公司金额（税额）
		String def27 = data.getString("def27");

		// 施工合同编号
		//String def36 = data.getString("def36");

		// 施工合同名称
		//String def37 = data.getString("def37");

		String def38 = data.getString("def38");// 到货确认日期
		String def41 = data.getString("def41");// 收货项目分期
		String def48 = data.getString("def48");// 单据完成日期

		/*
		 * if (supplier == null || "".equals(supplier)) { throw new
		 * BusinessException("【" + (i + 1) + "】采购供应商不能为空"); }
		 */

		if ("供应链到货接收".equals(data.get("def49"))) {
			if (def11 == null || "".equals(def11)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】采购协议编码不能为空");
			}
			if (def12 == null || "".equals(def12)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】采购协议名称不能为空");
			}

			/*
			 * // 检验是否有对应的采购协议 AggCtApVO ApVO = (AggCtApVO)
			 * getBillVO(AggCtApVO.class, "isnull(dr,0)=0 and vbillcode = '" +
			 * def11 + "' and ctname = '" + def12 + "'");
			 * 
			 * if (ApVO == null) { throw new BusinessException("请先同步协议到NC，协议编码："
			 * + def11 + "，协议名称：" + def12); }
			 */

			if (def19 == null || "".equals(def19)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】供应链金额（含税）不能为空");
			}
			if (def20 == null || "".equals(def20)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】供应链税率不能为空");
			}
			if (def21 == null || "".equals(def21)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】供应链金额（不含税）不能为空");
			}
			if (def22 == null || "".equals(def22)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】供应链金额（税额）不能为空");
			}
			if (def38 == null || "".equals(def38)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】到货确认日期不能为空");
			}
		}
		if ("供应链退货".equals(data.get("def49"))) {
			if (def11 == null || "".equals(def11)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】采购协议编码不能为空");
			}
			if (def12 == null || "".equals(def12)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】采购协议名称不能为空");
			}

			// 检验是否有对应的采购协议
			AggCtApVO ApVO = (AggCtApVO) getBillVO(AggCtApVO.class,
					"isnull(dr,0)=0 and vbillcode = '" + def11
							+ "' and ctname = '" + def12 + "'");

			if (ApVO == null) {
				throw new BusinessException("请先同步协议到NC，协议编码：" + def11
						+ "，协议名称：" + def12);
			}

			if (def7 == null || "".equals(def7)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】原接收编号不能为空");
			}
			if (def19 == null || "".equals(def19)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】供应链金额（含税）不能为空");
			}
			if (def20 == null || "".equals(def20)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】供应链税率不能为空");
			}
			if (def21 == null || "".equals(def21)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】供应链金额（不含税）不能为空");
			}
			if (def22 == null || "".equals(def22)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】供应链金额（税额）不能为空");
			}
			if (def48 == null || "".equals(def48)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】单据完成日期不能为空");
			}
		}

		if ("项目公司到货接收".equals(data.get("def49"))) {
			if (def23 == null || "".equals(def23)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】加成率不能为空");
			}
			if (def24 == null || "".equals(def24)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】项目公司金额（含税）不能为空");
			}
			if (def25 == null || "".equals(def25)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】项目公司税率不能为空");
			}
			if (def26 == null || "".equals(def26)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】项目公司金额不能为空");
			}
			if (def27 == null || "".equals(def27)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】项目公司金额（税额）不能为空");
			}
			// if (def36 == null || "".equals(def36)) {
			// throw new BusinessException("【" + data.getString("def49")
			// + "】施工合同编号不能为空");
			// }
			// if (def37 == null || "".equals(def37)) {
			// throw new BusinessException("【" + data.getString("def49")
			// + "】施工合同名称不能为空");
			// }
			if (def38 == null || "".equals(def38)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】到货确认日期不能为空");
			}
		}
		if ("项目公司退货".equals(data.get("def49"))) {
			if (def7 == null || "".equals(def7)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】原接收编号不能为空");
			}
			if (def23 == null || "".equals(def23)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】加成率不能为空");
			}
			if (def24 == null || "".equals(def24)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】项目公司金额（含税）不能为空");
			}
			if (def25 == null || "".equals(def25)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】项目公司税率不能为空");
			}
			if (def26 == null || "".equals(def26)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】项目公司金额不能为空");
			}
			if (def27 == null || "".equals(def27)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】项目公司金额（税额）不能为空");
			}
			// if (def36 == null || "".equals(def36)) {
			// throw new BusinessException("【" + data.getString("def49")
			// + "】施工合同编号不能为空");
			// }
			// if (def37 == null || "".equals(def37)) {
			// throw new BusinessException("【" + data.getString("def49")
			// + "】施工合同名称不能为空");
			// }
			if (def48 == null || "".equals(def48)) {
				throw new BusinessException("【" + data.getString("def49")
						+ "】单据完成日期不能为空");
			}
		}

		if (def1 == null || "".equals(def1)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】接收编号不能为空");
		}
		if (def2 == null || "".equals(def2)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】接收id不能为空");
		}
		if (def3 == null || "".equals(def3)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】接收行id不能为空");
		}
		if (def4 == null || "".equals(def4)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】订单编码不能为空");
		}
		if (def5 == null || "".equals(def5)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】订单id不能为空");
		}
		if (def6 == null || "".equals(def6)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】订单行id不能为空");
		}
		if (def13 == null || "".equals(def13)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】物料编码不能为空");
		}
		if (def14 == null || "".equals(def14)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】物料说明不能为空");
		}
		if (def15 == null || "".equals(def15)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】单位不能为空");
		}
		if (def16 == null || "".equals(def16)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】本次交易数量不能为空");
		}
		if (def17 == null || "".equals(def17)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】单价含税不能为空");
		}
		if (def18 == null || "".equals(def18)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】单价不含税不能为空");
		}
		if (def41 == null || "".equals(def41)) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】收货项目分期不能为空");
		}
		if (data.getString("def45") == null
				|| "".equals(data.getString("def45"))) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】供应商是否开票(Y/N)不能为空");
		}
		if (data.getString("def50") == null
				|| "".equals(data.getString("def50"))) {
			throw new BusinessException("【" + data.getString("def49")
					+ "】是否退货不能为空");
		}

	}

	private String getRefAttributePk(String key, String value)
			throws BusinessException {
		if (value == null || "".equals(value))
			return null;
		String pkValue = null;
		String sql = null;
		if ("pk_org".equals(key) || "def32".equals(key)) {
			sql = "select pk_financeorg from org_financeorg where code = ? and nvl(dr, 0) =0";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(value);

			pkValue = (String) getBaseDAO().executeQuery(sql, parameter,
					new ColumnProcessor());
		} else if ("supplier".equals(key)) {
			sql = "select  pk_supplier  from bd_supplier where code = ? and nvl(dr, 0) =0";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(value);

			pkValue = (String) getBaseDAO().executeQuery(sql, parameter,
					new ColumnProcessor());
		} else if ("pk_project".equals(key)) {// 项目参照
			sql = "select pk_project from bd_project where project_code = ? and nvl(dr, 0) =0";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(value);

			pkValue = (String) getBaseDAO().executeQuery(sql, parameter,
					new ColumnProcessor());
		}
		return pkValue;
	}

	/**
	 * 对暂估应付单表体的唯一性进行校验
	 * 
	 * @param def1
	 * @return
	 */
	private Boolean selectTemp(String def3, String pk) throws BusinessException {
		boolean check = false;

		int result = 0;

		String sql = "select count(1) from tgfn_temestbus where nvl(dr,0)=0 and def3 = '"
				+ def3 + "' and pk_temest = '" + pk + "'";

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
}
