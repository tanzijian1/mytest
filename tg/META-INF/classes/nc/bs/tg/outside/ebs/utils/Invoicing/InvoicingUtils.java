package nc.bs.tg.outside.ebs.utils.Invoicing;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.vo.tgfn.invoicing.InvoicingBody;
import nc.vo.tgfn.invoicing.InvoicingHead;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class InvoicingUtils extends EBSBillUtils {
	static InvoicingUtils utils;

	public static InvoicingUtils getUtils() {
		if (utils == null) {
			utils = new InvoicingUtils();
		}
		return utils;
	}

	/**
	 * srm供应链对供应商对账信息传nc【开票工单】 2020-04-02-谈子健
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
		InvocationInfoProxy.getInstance().setUserCode("SRM");

		JSONObject date = (JSONObject) value.get("data");

		JSONObject jsonObject = (JSONObject) date.get("headInfo");// 获取表头信息
		String srcid = jsonObject.getString("def1");// 传内部对账单ID
		String srcno = jsonObject.getString("def2");// 传内部对账单编码
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcid;
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;

		AggInvoicingHead aggVO = (AggInvoicingHead) getBillVO(
				AggInvoicingHead.class, "nvl(dr,0)=0 and def1 = '" + srcid
						+ "'");
		if (aggVO != null) {
			throw new BusinessException("【" + billkey + "】,NC已存在对应的业务单据【"
					+ aggVO.getParentVO().getBillno() + "】,请勿重复上传!");
		}
		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理

		AggInvoicingHead[] aggvo = null;

		try {
			AggInvoicingHead billvo = onTranBill(value, dectype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			aggvo = (AggInvoicingHead[]) getPfBusiAction().processAction(
					"SAVEBASE", "FN23", null, billvo, null, eParam);
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
	private AggInvoicingHead onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {

		// json数据
		JSONObject date = (JSONObject) value.get("data");
		// json数据头
		JSONObject headjson = (JSONObject) date.get("headInfo");
		// 数据体数组
		JSONArray bodyArray = (JSONArray) date.get("itemInfo");

		// 表头空值校验
		CheckHeadNull(headjson);

		AggInvoicingHead aggvo = new AggInvoicingHead();
		InvoicingHead hvo = new InvoicingHead();
		// 组织
		String pk_org = getPkByCode(headjson.getString("pk_org"), "pk_org");
		if (isNull(pk_org)) {
			throw new BusinessException("财务组织编码未与NC关联，请检查输入数据");
		}

		// 组织版本
		String pk_vid = getPkByCode(pk_org, "pk_vid");

		// // 表头默认值设置
		hvo.setPk_org(pk_org);// 组织
		hvo.setPk_group("000112100000000005FD");// 集团
		// hvo.setBilltype("FN23");// 单据类型
		hvo.setPk_busitype("0001ZZ10000000258BF2");// 业务流程
		hvo.setBillstatus("-1");// 单据状态
		hvo.setApprovestatus(-1);// 审批状态
		hvo.setCreationtime(new UFDateTime());// 制单时间
		hvo.setBilldate(new UFDate());// 制单时间
		hvo.setPk_org_v(pk_vid);// 组织版本
		hvo.setEffectstatus(10);// 生效状态
		hvo.setCreator("00011210000000000WJ0");
		hvo.setBillmaker("00011210000000000WJ0");// 制单人
		// 表头接受值
		hvo.setDef1(headjson.getString("def1"));
		hvo.setDef2(headjson.getString("def2"));
		String def3Code = headjson.getString("def3");
		hvo.setDef3(getPkByCode(def3Code, "def3"));
		if (isNull(hvo.getDef3())) {
			throw new BusinessException("发票抬头公司未与NC关联，请检查输入数据");
		}
		HashMap InvoiceInformation = getCustomerMaintenance(def3Code);
		if (InvoiceInformation != null) {
			hvo.setDef4((String) InvoiceInformation.get("taxpayerid"));// 发票抬头公司纳税人识别号
			hvo.setDef5((String) InvoiceInformation.get("def1"));// 发票抬头公司地址
			hvo.setDef6((String) InvoiceInformation.get("tel1"));// 发票抬头公司电话
			hvo.setDef7((String) InvoiceInformation.get("def2"));// 发票抬头公司开户行
			hvo.setDef8((String) InvoiceInformation.get("def3"));// 发票抬头公司银行账号
		}

		hvo.setDef9(headjson.getString("def9"));
		String def10Code = headjson.getString("def10");
		hvo.setDef10(getPkByCode(def10Code, "def10"));
		if (isNull(hvo.getDef10())) {
			throw new BusinessException("销售方名称未与NC关联，请检查输入数据");
		}
		HashMap Operator = getBillMaintenance(def10Code);
		if (Operator != null) {
			hvo.setDef11((String) Operator.get("taxid"));// 销售方纳税人识别号
			hvo.setDef12((String) Operator.get("address"));// 销售方地址
			hvo.setDef13((String) Operator.get("telephone"));// 销售方电话
			hvo.setDef14((String) Operator.get("khh"));// 销售方开户行
			hvo.setDef15((String) Operator.get("yhzh"));// 销售方银行账号
			hvo.setDef44((String) Operator.get("gathername"));// 票面默认收款人
			hvo.setDef45((String) Operator.get("openname"));// 票面默认开票人
			hvo.setDef46((String) Operator.get("checkname"));// 票面默认复核人
		} else {
			throw new BusinessException("该销方组织对应开票组织维护无数据，请检查输入数据");
		}
		hvo.setDef16(headjson.getString("def16"));
		hvo.setDef17(headjson.getString("def17"));
		hvo.setDef18(headjson.getString("def18"));
		hvo.setDef19(headjson.getString("def19"));
		hvo.setDef20(headjson.getString("def20"));
		String def21 = getPkByCode(headjson.getString("def21"), "def21");
		if (isNull(def21)) {
			throw new BusinessException("项目公司未与NC关联，请检查输入数据");
		}
		hvo.setDef21(def21);
		String def22 = getPkByCode(headjson.getString("def22"), "def22");
		if (isNull(def22)) {
			throw new BusinessException("项目编码未与NC关联，请检查输入数据");
		}
		hvo.setDef22(def22);
		String def23 = getPkByCode(headjson.getString("def23"), "def23");
		if (isNull(def23)) {
			throw new BusinessException("项目分期编码未与NC关联，请检查输入数据");
		}
		hvo.setDef23(def23);
		hvo.setDef24(headjson.getString("def24"));
		hvo.setDef25(headjson.getString("def25"));
		hvo.setDef26(headjson.getString("def26"));
		hvo.setDef27(headjson.getString("def27"));
		hvo.setDef28(headjson.getString("def28"));
		hvo.setDef29(headjson.getString("def29"));
		hvo.setDef30(headjson.getString("def30"));
		hvo.setDef31(headjson.getString("def31"));
		hvo.setDef32(headjson.getString("def32"));
		hvo.setDef33(headjson.getString("def33"));
		hvo.setDef34(headjson.getString("def34"));
		hvo.setDef35(headjson.getString("def35"));
		// hvo.setDef36(headjson.getString("def36"));
		// hvo.setDef37(headjson.getString("def37"));
		// hvo.setDef38(headjson.getString("def38"));
		hvo.setDef39(headjson.getString("def39"));
		hvo.setDef40(headjson.getString("def40"));
		hvo.setDef41(headjson.getString("def41"));
		hvo.setDef42(headjson.getString("def42"));

		aggvo.setParentVO(hvo);
		InvoicingBody[] bvos = new InvoicingBody[bodyArray.size()];
		for (int i = 0; i < bvos.length; i++) {
			JSONObject bJSONObject = bodyArray.getJSONObject(i);
			CheckBodyNull(bJSONObject);
			InvoicingBody bvo = new InvoicingBody();
			// 表体默认值设置
			bvo.setDef1(bJSONObject.getString("def1"));
			bvo.setDef2(bJSONObject.getString("def2"));
			bvo.setDef3(bJSONObject.getString("def3"));
			bvo.setDef4(bJSONObject.getString("def4"));
			bvo.setDef5(bJSONObject.getString("def5"));
			bvo.setDef6(bJSONObject.getString("def6"));
			bvo.setDef7(bJSONObject.getString("def7"));
			bvo.setDef8(bJSONObject.getString("def8"));
			bvo.setDef9(bJSONObject.getString("def9"));
			bvo.setDef10(bJSONObject.getString("def10"));
			bvo.setDef11(bJSONObject.getString("def11"));
			bvo.setDef12(bJSONObject.getString("def12"));
			bvo.setDef13(bJSONObject.getString("def13"));
			bvo.setDef14(bJSONObject.getString("def14"));
			bvo.setDef15(bJSONObject.getString("def15"));
			bvo.setDef16(bJSONObject.getString("def16"));
			bvo.setDef17(bJSONObject.getString("def17"));
			bvo.setDef18(bJSONObject.getString("def18"));
			bvo.setDef19(bJSONObject.getString("def19"));
			bvo.setDef20(bJSONObject.getString("def20"));
			bvo.setDef21(bJSONObject.getString("def21"));
			bvo.setDef22(bJSONObject.getString("def22"));
			bvo.setDef23(bJSONObject.getString("def23"));
			bvo.setDef24(bJSONObject.getString("def24"));
			bvo.setDef25(bJSONObject.getString("def25"));
			bvo.setDef26(bJSONObject.getString("def26"));
			bvo.setDef27(bJSONObject.getString("def27"));
			bvo.setDef28(bJSONObject.getString("def28"));
			bvo.setDef29(bJSONObject.getString("def29"));
			bvo.setDef30(bJSONObject.getString("def30"));
			bvos[i] = bvo;
		}
		aggvo.setChildrenVO(bvos);

		return aggvo;
	}

	/**
	 * 表体空值校验
	 * 
	 * @param 谈子健
	 * @throws BusinessException
	 */
	private void CheckBodyNull(JSONObject bodyjson) throws BusinessException {
		if (isNull(bodyjson.getString("def1"))) {
			throw new BusinessException("采购订单编号-项目公司不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def2"))) {
			throw new BusinessException("接收单号-供应链不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def3"))) {
			throw new BusinessException("物料分类编码不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def4"))) {
			throw new BusinessException("物料分类名称不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def5"))) {
			throw new BusinessException("物料编码不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def6"))) {
			throw new BusinessException("物料名称不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def7"))) {
			throw new BusinessException("规格型号不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def8"))) {
			throw new BusinessException("税收分类编码不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def9"))) {
			throw new BusinessException("单位不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def10"))) {
			throw new BusinessException("接收数量不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def11"))) {
			throw new BusinessException("采购单价（不含税）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def12"))) {
			throw new BusinessException("采购税率不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def13"))) {
			throw new BusinessException("采购单价（税额）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def14"))) {
			throw new BusinessException("采购单价（含税）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def15"))) {
			throw new BusinessException("加成率不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def16"))) {
			throw new BusinessException("销售单价（不含税）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def17"))) {
			throw new BusinessException("销售税率不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def18"))) {
			throw new BusinessException("销售单价（税额）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def19"))) {
			throw new BusinessException("销售单价（含税）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def20"))) {
			throw new BusinessException("采购金额（不含税）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def21"))) {
			throw new BusinessException("采购金额（税额）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def22"))) {
			throw new BusinessException("采购金额（含税）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def23"))) {
			throw new BusinessException("销售金额（不含税）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def24"))) {
			throw new BusinessException("销售金额（税额）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def25"))) {
			throw new BusinessException("销售金额（含税）不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def26"))) {
			throw new BusinessException("外部对账单编号---供应链和供应商不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def27"))) {
			throw new BusinessException("采购供应商---外部供应商不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def28"))) {
			throw new BusinessException("施工合同编码不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def29"))) {
			throw new BusinessException("施工合同名称不能为空，请检查参数设置");
		}
		if (isNull(bodyjson.getString("def30"))) {
			throw new BusinessException("后台接收ID-供应链不能为空，请检查参数设置");
		}

	}

	/**
	 * 表头空值校验
	 * 
	 * @param 谈子健
	 */
	public void CheckHeadNull(JSONObject headjson) throws BusinessException {
		if (isNull(headjson.getString("pk_org"))) {
			throw new BusinessException("财务组织不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def1"))) {
			throw new BusinessException("内部对账单ID---供应链和项目公司不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def2"))) {
			throw new BusinessException("内部对账单编号---供应链和项目公司不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def3"))) {
			throw new BusinessException("发票抬头公司名称不能为空，请检查参数设置");
		}
		// if (!isNull(headjson.get("def4"))) {
		// throw new BusinessException("发票抬头公司纳税人识别号不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def5"))) {
		// throw new BusinessException("发票抬头公司地址不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def6"))) {
		// throw new BusinessException("发票抬头公司电话不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def7"))) {
		// throw new BusinessException("发票抬头公司开户行不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def8"))) {
		// throw new BusinessException("发票抬头公司银行账号不能为空，请检查参数设置");
		// }
		if (isNull(headjson.getString("def9"))) {
			throw new BusinessException("销售方ID不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def10"))) {
			throw new BusinessException("销售方名称不能为空，请检查参数设置");
		}
		// if (!isNull(headjson.get("def11"))) {
		// throw new BusinessException("销售方纳税人识别号不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def12"))) {
		// throw new BusinessException("销售方地址不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def13"))) {
		// throw new BusinessException("销售方电话不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def14"))) {
		// throw new BusinessException("销售方开户行不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def15"))) {
		// throw new BusinessException("销售方银行账号不能为空，请检查参数设置");
		// }
		if (isNull(headjson.getString("def16"))) {
			throw new BusinessException("协议编号不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def17"))) {
			throw new BusinessException("协议名称不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def18"))) {
			throw new BusinessException("销售合同编码不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def19"))) {
			throw new BusinessException("销售合同名称不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def20"))) {
			throw new BusinessException("项目公司ID不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def21"))) {
			throw new BusinessException("项目公司不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def22"))) {
			throw new BusinessException("项目编码不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def23"))) {
			throw new BusinessException("项目分期编码不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def24"))) {
			throw new BusinessException("采购总额（不含税）不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def25"))) {
			throw new BusinessException("采购总额（税额）不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def26"))) {
			throw new BusinessException("采购总额（含税）不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def27"))) {
			throw new BusinessException("销售总额（不含税）不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def28"))) {
			throw new BusinessException("销售总额（税额）不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def29"))) {
			throw new BusinessException("销售总额（含税）不能为空，请检查参数设置");
		}
		// if (!isNull(headjson.get("def30"))) {
		// throw new BusinessException("内部扣款总金额（含税），请检查参数设置");
		// }
		// if (!isNull(headjson.get("def31"))) {
		// throw new BusinessException("内部调整总金额（含税），请检查参数设置");
		// }
		if (isNull(headjson.getString("def32"))) {
			throw new BusinessException("销售已对账总金额（不含税）不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def33"))) {
			throw new BusinessException("销售已对账总金额（税额）不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def34"))) {
			throw new BusinessException("销售已对账总金额（含税）不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def35"))) {
			throw new BusinessException("销售对账日期不能为空，请检查参数设置");
		}
		// if (!isNull(headjson.get("def36"))) {
		// throw new BusinessException("开票总金额（不含税）不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def37"))) {
		// throw new BusinessException("开票总金额（税额）不能为空，请检查参数设置");
		// }
		// if (!isNull(headjson.get("def38"))) {
		// throw new BusinessException("开票总金额（含税）不能为空，请检查参数设置");
		// }
		if (isNull(headjson.getString("def39"))) {
			throw new BusinessException("经办人不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def40"))) {
			throw new BusinessException("经办部门不能为空，请检查参数设置");
		}
		if (isNull(headjson.getString("def41"))) {
			throw new BusinessException("经办职位不能为空，请检查参数设置");
		}
		// if (!isNull(headjson.get("def42"))) {
		// throw new BusinessException("备注不能为空，请检查参数设置");
		// }

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
		if ("pk_org".equals(key) || "def10".equals(key) || "def21".equals(key)) {
			sql = "SELECT pk_org from org_orgs where (code = '" + code
					+ "' or name = '" + code
					+ "' ) and enablestate = 2 and nvl(dr,0)=0";
		}
		if ("def3".equals(key)) {
			sql = "select pk_customer from bd_customer where dr = 0  and (enablestate = 2)and (code = '"
					+ code + "'  or name = '" + code + "') ";
		}
		if ("pk_vid".equals(key)) {
			sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + code
					+ "' and enablestate = 2 and nvl(dr,0)=0";
		}
		if ("def22".equals(key) || "def23".equals(key)) {
			sql = "select c.pk_projectclass from bd_projectclass c where  c.type_code='"
					+ code + "' and nvl(dr,0) = 0";
		}

		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}

	/**
	 * 判空方法
	 * 
	 * @param obj
	 * @return
	 */
	public Boolean isNull(String obj) {
		if (obj != null && "".equals(obj)) {
			return true;
		}
		return false;

	}

	private HashMap getBillMaintenance(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select distinct h.taxid,  ");
		query.append("                h.address,  ");
		query.append("                h.telephone,  ");
		query.append("                h.khh,  ");
		query.append("                h.yhzh,  ");
		query.append("                h.gathername,  ");
		query.append("                h.openname,  ");
		query.append("                h.checkname  ");
		query.append("  from hzvat_billmaintenance h  ");
		query.append("  left join org_orgs o  ");
		query.append("    on h.pk_org = o.pk_org  ");
		query.append(" where o.code = '" + code + "'  ");
		query.append("   and h.dr = 0  ");
		query.append("   and o.dr = 0  ");
		query.append("   and o.enablestate = 2  ");

		HashMap<String, String> map = (HashMap<String, String>) getBaseDAO()
				.executeQuery(query.toString(), new MapProcessor());
		return map;
	}

	/**
	 * 客户税务信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	private HashMap getCustomerMaintenance(String code)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select name,taxpayerid,def1,tel1,def2,def3 from bd_customer where nvl(dr,0) = 0 and code = '"
				+ code + "' ");

		HashMap<String, String> map = (HashMap<String, String>) getBaseDAO()
				.executeQuery(query.toString(), new MapProcessor());
		return map;
	}
}
