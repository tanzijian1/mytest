package nc.bs.tg.outside.ebs.utils.margin;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
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
public class MarginPayment extends EBSBillUtils {

	static MarginPayment utils;

	public static MarginPayment getUtils() {
		if (utils == null) {
			utils = new MarginPayment();
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
		String srcid = jsonObject.getString("def1");// 销售系统业务单据ID
		String srcno = jsonObject.getString("def2");// 销售系统业务单据单据号
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcid;
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		// srcid 按实际存入信息位置进行变更
		AggGatheringBillVO aggVO = (AggGatheringBillVO) getBillVO(
				AggGatheringBillVO.class, "nvl(dr,0)=0 and def1 = '" + srcid
						+ "'");
		if (aggVO != null) {
			throw new BusinessException("【" + billkey + "】,NC已存在对应的业务单据【"
					+ srcid + "】,请勿重复上传!");
		}
		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理

		AggGatheringBillVO[] aggvo = null;

		try {
			AggGatheringBillVO billvo = onTranBill(value, dectype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			aggvo = (AggGatheringBillVO[]) getPfBusiAction().processAction(
					"SAVE", "F2-Cxx-006", null, billvo, null, eParam);
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
	private AggGatheringBillVO onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {

		// json数据
		JSONObject date = (JSONObject) value.get("data");
		// json数据头
		JSONObject headjson = (JSONObject) date.get("headInfo");
		// 数据体数组
		JSONArray bodyArray = (JSONArray) date.get("itemInfo");

		// 表头空值校验
		CheckHeadNull(headjson);

		AggGatheringBillVO aggvo = new AggGatheringBillVO();
		GatheringBillVO hvo = new GatheringBillVO();
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
		hvo.setPk_billtype("F2");// 单据类型
		hvo.setPk_busitype(getBusitypeID("AR02", hvo.getPk_group()));// 业务流程
		hvo.setPk_fiorg(pk_org);
		hvo.setBillclass("sk");// 单据大类
		hvo.setPk_tradetype("F2-Cxx-006");// 应收类型编码
		hvo.setPk_tradetypeid(getBillTypePkByCode("F2-Cxx-006",
				hvo.getPk_group()));// 应收类型
		hvo.setBillstatus(-1);// 单据状态
		hvo.setApprovestatus(-1);// 审批状态
		hvo.setEffectstatus(2);// 生效状态
		hvo.setCreationtime(new UFDateTime());// 制单时间
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
		hvo.setDef10(headjson.getString("def10"));// 外系统名称
		hvo.setDef1(headjson.getString("def1"));// 外系统来源单据id
		hvo.setDef2(headjson.getString("def2"));// 外系统来源单据编号
		hvo.setDef3(headjson.getString("def3"));// 影像编码
		hvo.setDef4(headjson.getString("def4"));// 影像状态
		hvo.setDef6(headjson.getString("def6"));// 备注
		hvo.setDef20(headjson.getString("pk_psndoc"));// 经办人

		aggvo.setParentVO(hvo);

		GatheringBillItemVO[] bvos = new GatheringBillItemVO[bodyArray.size()];
		for (int i = 0; i < bvos.length; i++) {
			GatheringBillItemVO bvo = new GatheringBillItemVO();
			JSONObject bJSONObject = bodyArray.getJSONObject(i);
			CheckBodyNull(bJSONObject);
			// 表体默认值设置

			String pk_supplier = getPkByCode(bJSONObject.getString("def25"),
					"pk_supplier");
			if (!isNull(pk_supplier)) {
				throw new BusinessException("供应商编码未与NC关联，请检查输入数据");
			}
			// 收款银行账户
			bvo.setSupplier(pk_supplier);
			String recaccount = getrecaccount(
					bJSONObject.getString("recaccount"), pk_org);
			if (!isNull(recaccount)) {
				throw new BusinessException("收款银行账户未与NC关联，请检查输入数据");
			}
			// 付款银行账户
			bvo.setRecaccount(recaccount);
			String payaccount = getAccountIDByCode(
					bJSONObject.getString("payaccount"), pk_supplier);
			if (!isNull(payaccount)) {
				throw new BusinessException("付款银行账户编码未与NC关联，请检查输入数据");
			}
			bvo.setPayaccount(payaccount);
			// 默认数据
			bvo.setObjtype(1);
			bvo.setPk_currtype(pk_currtype);
			bvo.setSupplier(pk_supplier);// 供应商
			bvo.setPk_billtype("F1");// 单据类型
			bvo.setPk_tradetype("F2-Cxx-006");// 应收类型编码
			bvo.setPk_tradetypeid(getBillTypePkByCode("F2-Cxx-006",
					hvo.getPk_group()));// 应收类型
			bvo.setPk_fiorg(pk_org);
			bvo.setPk_fiorg_v(pk_vid);
			bvo.setRececountryid("0001Z010000000079UJJ");
			bvo.setSett_org(pk_org);
			bvo.setSett_org_v(pk_vid);
			bvo.setTriatradeflag(UFBoolean.FALSE);
			bvo.setBillclass("sk");// 单据大类
			bvo.setGrouprate(UFDouble.ONE_DBL);
			bvo.setGlobalrate(UFDouble.ONE_DBL);
			bvo.setRate(UFDouble.ONE_DBL);
			bvo.setRowno(0);
			bvo.setDirection(1);
			bvo.setTaxtype(1);

			String pk_balatype = null;
			if ("现金".equals(bJSONObject.getString("pk_balatype"))) {
				pk_balatype = getBalatypePkByCode("10");
			}
			if ("保理".equals(bJSONObject.getString("pk_balatype"))) {
				pk_balatype = getBalatypePkByCode("13");
			}
			if ("期票".equals(bJSONObject.getString("pk_balatype"))) {
				pk_balatype = getBalatypePkByCode("7");
			}
			if (pk_balatype == null) {
				throw new BusinessException("结算方式【"
						+ bJSONObject.getString("pk_balatype")
						+ "】未能在NC档案查询到相关信息!");
			}
			bvo.setAttributeValue(PayableBillVO.PK_BALATYPE, pk_balatype);// 结算方式

			/*
			 * String pk_balatype =
			 * getPkByCode(bJSONObject.getString("pk_balatype"), "pk_balatype");
			 * if (!isNull(pk_balatype)) { throw new
			 * BusinessException("结算方式编码未与NC关联，请检查输入数据");
			 */
			// 暂时默认结算方式
			bvo.setDef21(bJSONObject.getString("def21"));// 款项类型
			bvo.setDef22(bJSONObject.getString("def22"));// 标书编号
			bvo.setDef23(bJSONObject.getString("def23"));// 招标名称
			bvo.setDef24(bJSONObject.getString("def24"));// 投标供应商id
			bvo.setLocal_money_cr(new UFDouble(bJSONObject
					.getString("local_money_cr")));// 投标保证金金额
			bvo.setMoney_cr(new UFDouble(bJSONObject
					.getString("local_money_cr")));// 贷方原币金额
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
			sql = "SELECT pk_org from org_orgs where code = '" + code
					+ "' and enablestate = 2 and nvl(dr,0)=0";
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
			sql = "select pk_supplier from bd_supplier where code = '" + code
					+ "' and nvl(dr,0)=0";
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
		String sql = "SELECT distinct bd_custbank.pk_bankaccsub AS pk_bankaccsub "
				+ "FROM bd_bankaccbas, bd_bankaccsub, bd_custbank "
				+ "WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
				+ " AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub "
				+ "AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ "AND bd_custbank.pk_bankaccsub != '~' "
				+ "AND bd_bankaccsub.Accnum = '"
				+ code
				+ "' "
				+ "AND exists (select 1 from bd_bankaccbas bas "
				+ "where bas.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ "and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y')) "
				+ "and (enablestate = 2) ";
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
		if (obj != null) {
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

		if (!isNull(headjson.get("billdate"))) {
			throw new BusinessException("单据日期不能为空，请检查参数设置");
		}
		if (!isNull(headjson.get("pk_currtype"))) {
			throw new BusinessException("币种不能为空，请检查参数设置");
		}
		if (!isNull(headjson.get("pk_psndoc"))) {
			throw new BusinessException("经办人不能为空，请检查参数设置");
		}
		if (!isNull(headjson.get("def1"))) {
			throw new BusinessException("外系统主键不能为空，请检查参数设置");
		}
		if (!isNull(headjson.get("def2"))) {
			throw new BusinessException("外系统单据号不能为空，请检查参数设置");
		}
		if (!isNull(headjson.get("def10"))) {
			throw new BusinessException("外系统名称不能为空，请检查参数设置");
		}
		// if (!isNull(headjson.get("def3"))) {
		// throw new BusinessException("影像编码不能为空，请检查参数设置");
		// }
		if (!isNull(headjson.get("def4"))) {
			throw new BusinessException("def4不能为空，请检查参数设置");
		}

	}

	/**
	 * 表体空值校验
	 * 
	 * @param bodyjson
	 */
	public void CheckBodyNull(JSONObject bodyjson) throws BusinessException {
		if (!isNull(bodyjson.get("pk_balatype"))) {
			throw new BusinessException("结算方式不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.get("def21"))) {
			throw new BusinessException("款项类型不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.get("def22"))) {
			throw new BusinessException("标书编号不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.get("def23"))) {
			throw new BusinessException("招标名称不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.get("recaccount"))) {
			throw new BusinessException("收款银行账户不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.get("payaccount"))) {
			throw new BusinessException("付款银行账户不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.get("local_money_cr"))) {
			throw new BusinessException("投标保证金金额不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.get("def24"))) {
			throw new BusinessException("供应商ID不能为空，请检查参数设置");
		}
		if (!isNull(bodyjson.get("def25"))) {
			throw new BusinessException("投标供应商名称不能为空，请检查参数设置");
		}

	}
}
