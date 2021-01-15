/**
 * <p>Title: IncomeInvoiceUtils.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月18日 上午10:55:43

 * @version 1.0
 */

package nc.bs.tg.outside.img.utils.incomeinvoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.img.utils.IMGBillUtils;
import nc.itf.tg.outside.IMGBillCont;
import nc.vo.hzvat.invoice.AggInvoiceVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 创建时间：2019年9月18日 上午10:55:43  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：IncomeInvoiceUtils.java  
 * 类说明：  
 */

/**
 * <p>
 * Title: IncomeInvoiceUtils<／p>
 * 
 * <p>
 * Description: <／p>
 * 
 * <p>
 * Company: hanzhi<／p>
 * 
 * @author bobo
 * 
 * @date 2019年9月18日 上午10:55:43
 */

public class IncomeInvoiceUtils extends IMGBillUtils {
	static IncomeInvoiceUtils utils;

	public static IncomeInvoiceUtils getUtils() {
		if (utils == null) {
			utils = new IncomeInvoiceUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getIMGUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		// 解析json参数,获取表头数据
		JSONObject headInfo = (JSONObject) value.get("headInfo");
		// 单据发票号，获取唯一字段值
		String fph = headInfo.getString("fph");// 业务单据发票号

		// 业务单据发票号做队列控制和日志输出
		String billNo = IMGBillCont.getBillNameMap().get(billtype) + ":" + fph;
		/*
		 * String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":" +
		 * saleno;
		 */

		// 检查影像系统业务单据是否存在**************
		AggInvoiceVO aggVO = (AggInvoiceVO) getBillVO(AggInvoiceVO.class,
				"isnull(dr,0)=0 and fph = '" + fph + "'");

		if (aggVO != null) {
			throw new BusinessException("【" + billNo + "】,NC已存在对应的业务单据，发票号：【"
					+ fph + "】,请勿重复存单");
		}

		BPMBillUtils.addBillQueue(billNo);// 增加队列处理***************
		try {
			// **********************
			// 转换数据为VO
			// *********************************
			IncomeInvoiceConvertor convertor = new IncomeInvoiceConvertor();
			// 默认集团PK
			convertor.setDefaultGroup("000112100000000005FD");
			// 配置表体key与名称映射
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("InvoiceBVO", "进项发票表体");
			convertor.setBVOKeyName(bVOKeyName);
			// 配置表头key与名称映射
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("InvoiceVO", "进项发票表头");
			convertor.setHVOKeyName(hVOKeyName);
			// 表头空值校验字段
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> bInvoiceVOKeyName = new HashMap<String, String>();
			bInvoiceVOKeyName.put("pk_org", "财务组织");
			bInvoiceVOKeyName.put("fplx", "发票类型");
			bInvoiceVOKeyName.put("fph", "发票号");
			bInvoiceVOKeyName.put("fpdm", "发票代码");
			bInvoiceVOKeyName.put("def5", "税额");
			bInvoiceVOKeyName.put("def1", "是否已验伪");
			bInvoiceVOKeyName.put("costmoney", "无税金额总额");
			bInvoiceVOKeyName.put("costmoneyin", "价税合计总额");
			bInvoiceVOKeyName.put("kprq", "开票日期");
			bInvoiceVOKeyName.put("sellname", "销售方名称");
			bInvoiceVOKeyName.put("sellnsrsbh", "销售方纳税人识别号");
			bInvoiceVOKeyName.put("selladrpho", "销售方地址和电话");
			bInvoiceVOKeyName.put("sellbank", "销售方开户行及账号");
			bInvoiceVOKeyName.put("kpr", "开票人");
			bInvoiceVOKeyName.put("htbh", "合同编号");
			hValidatedKeyName.put("InvoiceVO", bInvoiceVOKeyName);

			// 表体空值校验字段
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> bInvoiceBVOKeyName = new HashMap<String, String>();
			bInvoiceBVOKeyName.put("pk_product", "货物或应税劳务");
			bInvoiceBVOKeyName.put("def1", "税收分类");
			bInvoiceBVOKeyName.put("pk_math", "单位");
			bInvoiceBVOKeyName.put("num", "数量");
			bInvoiceBVOKeyName.put("priceouttax", "无税单价");
			bInvoiceBVOKeyName.put("moneyouttax", "无税金额");
			bInvoiceBVOKeyName.put("taxrate", "税率");
			bInvoiceBVOKeyName.put("moneytax", "税额");
			bValidatedKeyName.put("InvoiceBVO", bInvoiceBVOKeyName);

			// 参照类型字段
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("InvoiceVO-pk_org");// 财务组织
			refKeys.add("InvoiceVO-fplx");// 发票类型
			refKeys.add("InvoiceVO-thyy");// 退回原因

			convertor.sethValidatedKeyName(hValidatedKeyName);
			convertor.setbValidatedKeyName(bValidatedKeyName);
			convertor.setRefKeys(refKeys);

			// *********************************
			AggInvoiceVO billvo = (AggInvoiceVO) convertor.castToBill(value,
					AggInvoiceVO.class, aggVO);

			// ********************表头参照信息设置***************
			billvo.getParentVO().setPk_org(
					convertor.getRefAttributePk("InvoiceVO-pk_org", billvo
							.getParentVO().getPk_org()));
			billvo.getParentVO().setFplx(
					convertor.getRefAttributePk("InvoiceVO-fplx", billvo
							.getParentVO().getFplx()));
			billvo.getParentVO().setThyy(
					convertor.getRefAttributePk("InvoiceVO-thyy", billvo
							.getParentVO().getThyy()));
			// ********************表头参照信息设置***************

			// 设置NC默认信息
			billvo.getParentVO().setPk_billtype("HZ01");
			billvo.getParentVO().setDbilldate(new UFDate());
			billvo.getParentVO().setVbillstatus(-1);
			// 默认创建人IMG
			billvo.getParentVO().setCreator("1001ZZ100000001MYVM7");
			billvo.getParentVO().setCreationtime(new UFDateTime());
			// 默认制单人IMG
			billvo.getParentVO().setBillmaker("1001ZZ100000001MYVM7");
			billvo.getParentVO().setDmakedate(new UFDateTime());

			HashMap<String, String> eParam = new HashMap<String, String>();
			// 持久化工单VO
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "HZ01",
					null, billvo, null, eParam);
			// **************************
			AggInvoiceVO[] billVOs = (AggInvoiceVO[]) obj;
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("billid", billVOs[0].getPrimaryKey());
			dataMap.put("billno", (String) billVOs[0].getParentVO()
					.getAttributeValue("vbillno"));
			// **************************
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("【" + billNo + "】," + e.getMessage(), e);
		} finally {
			BPMBillUtils.removeBillQueue(billNo);
		}
	}
}
