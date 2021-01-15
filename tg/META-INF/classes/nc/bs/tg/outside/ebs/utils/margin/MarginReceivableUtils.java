package nc.bs.tg.outside.ebs.utils.margin;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 保证金->收款单
 * 
 * @author huangxj
 * 
 */
public class MarginReceivableUtils extends EBSBillUtils {

	static MarginReceivableUtils utils;

	public static MarginReceivableUtils getUtils() {
		if (utils == null) {
			utils = new MarginReceivableUtils();
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
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// 设置用户码
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		JSONObject date = (JSONObject) value.get("data");

		JSONObject jsonObject = (JSONObject) date.get("headInfo");// 获取表头信息
		String srcid = jsonObject.getString("def2");// 保证金主键
		String srcno = jsonObject.getString("def1");// 保证金编号
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		// srcid 按实际存入信息位置进行变更
		AggReceivableBillVO aggVO = (AggReceivableBillVO) getBillVO(
				AggReceivableBillVO.class, "nvl(dr,0)=0 and def1 = '" + srcno
						+ "'");
		if (aggVO != null) {
			throw new BusinessException("【" + billkey + "】,NC已存在对应的业务单据【"
					+ srcno + "】,请勿重复上传!");
		}
		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理

		AggReceivableBillVO[] aggvo = null;

		try {
			AggReceivableBillVO billvo = onTranBill(value, dectype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			aggvo = (AggReceivableBillVO[]) getPfBusiAction().processAction(
					"SAVE", "F0-Cxx-002", null, billvo, null, eParam);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
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
	private AggReceivableBillVO onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {

		// json数据
		JSONObject date = (JSONObject) value.get("data");
		// json数据头
		JSONObject headjson = (JSONObject) date.get("headInfo");
		// 数据体数组
		JSONArray bodyArray = (JSONArray) date.get("itemInfo");

		// 表头空值校验
		CheckHeadNull(headjson);

		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO hvo = new ReceivableBillVO();
		// 组织
		String pk_org = getPkByCode(headjson.getString("pk_org"), "pk_org");
		if (!isNull(pk_org)) {
			throw new BusinessException("财务组织编码未与NC关联，请检查输入数据");
		}
		// 币种
		String pk_currtype = getPkByCode(headjson.getString("pk_currtype"),
				"pk_currtype");
		if (!isNull(pk_currtype)) {
			throw new BusinessException("币种编码未与NC关联，请检查输入数据");
		}
		// 组织版本
		String pk_vid = getPkByCode(pk_org, "pk_vid");

		// 表头默认值设置
		hvo.setPk_org(pk_org);// 组织
		hvo.setPk_group("000112100000000005FD");// 集团
		hvo.setPk_billtype("F0");// 单据类型
		hvo.setPk_busitype(getBusitypeID("AR01", hvo.getPk_group()));// 业务流程
		hvo.setPk_fiorg(pk_org);
		hvo.setBillclass("ys");// 单据大类
		hvo.setPk_tradetype("F0-Cxx-002");// 应收类型编码
		hvo.setPk_tradetypeid(getBillTypePkByCode("F0-Cxx-002",
				hvo.getPk_group()));// 应收类型
		hvo.setBillstatus(-1);// 单据状态
		hvo.setApprovestatus(-1);// 审批状态
		hvo.setEffectstatus(2);// 生效状态
		hvo.setCreationtime(new UFDateTime());// 制单时间
		hvo.setBilldate(new UFDate());// 制单时间
		hvo.setCreator(getSaleUserID());
		hvo.setBillmaker(getSaleUserID());// 制单人
		hvo.setIsflowbill(UFBoolean.FALSE);
		hvo.setIsreded(UFBoolean.FALSE);
		hvo.setObjtype(0);
		hvo.setPk_fiorg_v(pk_vid);
		hvo.setSrc_syscode(0);
		hvo.setSyscode(0);
		hvo.setDr(0);
		hvo.setPk_org_v(pk_vid);// 组织版本

		// 表头接受值
		hvo.setPk_currtype(pk_currtype);// 币种
		hvo.setDef10("EBS系统SRM");// 外系统名称
		hvo.setDef1(headjson.getString("def1"));// 外系统来源单据id
		hvo.setDef2(headjson.getString("def2"));// 外系统来源单据编号
		hvo.setDef3(headjson.getString("def3"));// 影像编码
		hvo.setDef4(headjson.getString("def4"));// 影像状态
		hvo.setDef6(headjson.getString("def6"));// 备注
		hvo.setDef11(headjson.getString("def11"));// 经办人
		hvo.setDef12(headjson.getString("def12"));// 备注
		hvo.setAttributeValue("bpmid", headjson.getString("bpmid"));
		
		if (headjson.getString("def13") != null) {
			hvo.setDef13(headjson.getString("def13"));// 是否罚没
		} else {
			throw new BusinessException("是否罚没不能为空");
		}

		aggvo.setParentVO(hvo);

		ReceivableBillItemVO[] bvos = new ReceivableBillItemVO[bodyArray.size()];
		for (int i = 0; i < bvos.length; i++) {
			ReceivableBillItemVO bvo = new ReceivableBillItemVO();
			JSONObject bJSONObject = bodyArray.getJSONObject(i);
			CheckBodyNull(bJSONObject);
			// 表体默认值设置

			String pk_supplier = getPkByCode(bJSONObject.getString("supplier"),
					"pk_customer");
			if (!isNull(pk_supplier)) {
				throw new BusinessException("投标供应商编码未与NC关联，请检查输入数据");
			}
			bvo.setCustomer(pk_supplier);
			// 收款银行账户
			String recaccount = getrecaccount(
					bJSONObject.getString("recaccount"), pk_org);
			if (!isNull(recaccount)) {
				throw new BusinessException("该供应商下无该收款银行账户，请检查输入数据");
			}

			bvo.setRecaccount(recaccount);
			if (isNull(bJSONObject.getString("payaccount"))) {
				// 付款银行账户
				bvo.setRecaccount(recaccount);
				String payaccount = getAccountIDByCode(
						bJSONObject.getString("payaccount"), pk_supplier);
				if (!isNull(payaccount)) {
					throw new BusinessException("付款银行账户编码未与NC关联，请检查输入数据");
				}
				bvo.setPayaccount(payaccount);
			}
			// 默认数据
			bvo.setObjtype(0);
			bvo.setPk_currtype(pk_currtype);
			bvo.setSupplier(pk_supplier);// 供应商
			bvo.setPk_billtype("F0");// 单据类型
			bvo.setPk_tradetype("F0-Cxx-002");// 应收类型编码
			bvo.setPk_tradetypeid(getBillTypePkByCode("F0-Cxx-002",
					hvo.getPk_group()));// 应收类型
			bvo.setPk_fiorg(pk_org);
			bvo.setPk_fiorg_v(pk_vid);
			bvo.setSett_org(pk_org);
			bvo.setSett_org_v(pk_vid);
			bvo.setTriatradeflag(UFBoolean.FALSE);
			bvo.setBillclass("ys");// 单据大类
			bvo.setGrouprate(UFDouble.ONE_DBL);
			bvo.setGlobalrate(UFDouble.ONE_DBL);
			bvo.setRate(UFDouble.ONE_DBL);
			bvo.setRowno(0);
			bvo.setDirection(1);
			bvo.setTaxtype(1);

			String pk_balatype = null;
			if ("网银".equals(bJSONObject.getString("pk_balatype"))) {
				pk_balatype = getBalatypePkByCode("1");
			} else {
				pk_balatype = getBalatypePkByCode(bJSONObject
						.getString("pk_balatype"));
			}

			if (pk_balatype == null || "".equals(pk_balatype)) {
				throw new BusinessException("结算方式编码未与NC关联，请检查输入数据");
			}

			bvo.setAttributeValue("pk_balatype", pk_balatype);// 结算方式

			/*
			 * String pk_balatype =
			 * getPkByCode(bJSONObject.getString("pk_balatype"), "pk_balatype");
			 * if (!isNull(pk_balatype)) { throw new
			 * BusinessException("结算方式编码未与NC关联，请检查输入数据");
			 */
			// 暂时默认结算方式
			bvo.setDef10(bJSONObject.getString("def10"));// 款项类型
			bvo.setDef11(bJSONObject.getString("def11"));// 招标保证金类型
			bvo.setDef9(bJSONObject.getString("def9"));// 投标供应商id
			bvo.setLocal_money_de(new UFDouble(bJSONObject
					.getString("local_money_de")));// 投标保证金金额
			bvo.setMoney_de(new UFDouble(bJSONObject
					.getString("local_money_de")));// 贷方原币金额
			bvos[i] = bvo;
		}
		aggvo.setChildrenVO(bvos);

		return aggvo;
	}

	/**
	 * 
	 * @param code传入的数据PK值
	 *            ，转换为数据库中的PK值
	 * 
	 * @return 返回转换后的PK值
	 */
	public String getPkByCode(String code, String key) throws DAOException {

		String sql = null;
		if ("pk_org".equals(key)) {
			sql = "SELECT pk_org from org_orgs where (code = '" + code
					+ "' or name = '" + code
					+ "' ) and enablestate = 2 and nvl(dr,0)=0";
		}
		if ("pk_currtype".equals(key)) {
			sql = "select pk_currtype from bd_currtype where code = '" + code
					+ "' and nvl(dr,0)=0";
		}
		if ("pk_balatype".equals(key)) {
			sql = "select pk_balatype from bd_balatype where code = '" + code
					+ "' and nvl(dr,0)=0";
		}
		if ("pk_supplier".equals(key)) {
			sql = "select pk_supplier from bd_supplier where ( code = '" + code
					+ "' or name = '" + code + "' ) and nvl(dr,0)=0";
		}
		if ("pk_customer".equals(key)) {
			sql = "select pk_customer from bd_customer where ( code = '" + code
					+ "' or name = '" + code + "' ) and nvl(dr,0)=0";
		}
		if ("pk_vid".equals(key)) {
			sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + code
					+ "' and enablestate = 2 and nvl(dr,0)=0";
		}
		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}

	public String getrecaccount(String code, String pk_org) {
		String result = null;
		String sql = "select pk_bankaccsub from bd_bankaccsub where pk_bankaccbas = "
				+ "(select pk_bankaccbas from bd_bankaccbas where ( accclass = 2 ) "
				+ "and ( enablestate in ( 2, 1 ) and ( pk_group = '000112100000000005FD' AND "
				+ "accnum = '" + code + "' and pk_org = '" + pk_org + "')))  ";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据银行账户编码读取对应主键
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 */
	public String getAccountIDByCode(String recaccount, String pk_receiver) {
		String result = null;
		String sql = "SELECT bd_custbank.pk_bankaccsub AS pk_bankaccsub "
				+ "   FROM bd_bankaccbas, bd_bankaccsub, bd_custbank "
				+ " WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
				+ " AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub "
				+ " AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ " AND bd_custbank.pk_bankaccsub != '~' "
				+ " AND bd_bankaccsub.Accnum = '"
				+ recaccount
				+ "' "
				+ " AND exists "
				+ " (select 1 from bd_bankaccbas bas  where bas.pk_bankaccbas = bd_custbank.pk_bankaccbas  and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y')) "
				+ " and (enablestate = 2) "
				+ " and (pk_cust = '"
				+ pk_receiver
				+ "' and pk_custbank IN (SELECT min(pk_custbank) FROM bd_custbank GROUP BY pk_bankaccsub, pk_cust)) ";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判空方法
	 * 
	 * @param obj
	 * @return
	 */
	public Boolean isNull(Object obj) {
		if (obj != null && !"".equals((String) obj)) {
			return true;
		}
		return false;

	}

	/**
	 * 表头空值校验
	 * 
	 * @param headjson
	 */
	public void CheckHeadNull(JSONObject headjson) throws BusinessException {
		if (!isNull(headjson.getString("pk_org"))) {
			throw new BusinessException("财务组织不能为空，请检查参数设置");
		}
		if (!isNull(headjson.getString("pk_currtype"))) {
			throw new BusinessException("币种不能为空，请检查参数设置");
		}
		if (!isNull(headjson.getString("def1"))) {
			throw new BusinessException("外系统主键不能为空，请检查参数设置");
		}
		if (!isNull(headjson.getString("def2"))) {
			throw new BusinessException("外系统单据号不能为空，请检查参数设置");
		}
		// if (!isNull(headjson.get("def3"))) {
		// throw new BusinessException("影像编码不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.getString("def4"))) {
		// throw new BusinessException("def4不能为空，请检查参数设置");
		// }

	}

	/**
	 * 表体空值校验
	 * 
	 * @param bodyjson
	 */
	public void CheckBodyNull(JSONObject bodyjson) throws BusinessException {
		if (!isNull(bodyjson.getString("pk_balatype"))) {
			throw new BusinessException("结算方式不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.getString("def9"))) {
			throw new BusinessException("供应商ID不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.getString("def10"))) {
			throw new BusinessException("款项类型不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.getString("def11"))) {
			throw new BusinessException("招标保证金类型不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.getString("recaccount"))) {
			throw new BusinessException("收款银行账户不能为空，请检查参数设置");
		}
		/*
		 * if (!isNull(bodyjson.get("payaccount"))) { throw new
		 * BusinessException("付款银行账户不能为空，请检查参数设置"); }
		 */
		if (!isNull(bodyjson.getString("local_money_de"))) {
			throw new BusinessException("投标保证金金额不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.getString("supplier"))) {
			throw new BusinessException("投标供应商名称不能为空，请检查参数设置");
		}

	}
}
