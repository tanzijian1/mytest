package nc.bs.tg.outside.sale.utils.ticketsexchange;

import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.SaleBillCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.tgfn.changebill.ChangeBillBVO;
import nc.vo.tgfn.changebill.ChangeBillHVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 换票单(FN11)
 * 
 * @author ln
 * 
 */
public class TicketsexChangeUtils extends SaleBillUtils {
	static TicketsexChangeUtils utils;

	public static TicketsexChangeUtils getUtils() {
		if (utils == null) {
			utils = new TicketsexChangeUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		// 解析json参数,获取表头数据
		JSONObject headJson = (JSONObject) value.get("headInfo");
		// Saleid 按实际存入信息位置进行变更
		String saleid = headJson.getString("def1");// 销售系统业务单据ID
		String saleno = headJson.getString("def2");// 销售系统业务单据单据号

		// 销售系统业务单据ID和销售系统业务单据单据号做队列控制和日志输出
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		HashMap<String, String> dataMap = new HashMap<String, String>();
		// 检查销售系统业务单据ID唯一性
		AggChangeBillHVO aggVO = (AggChangeBillHVO) getBillVO(
				AggChangeBillHVO.class, "isnull(dr,0)=0 and def1 = '" + saleid
						+ "'");
		if (aggVO != null) {	
		dataMap.put("billid", aggVO.getPrimaryKey());
		dataMap.put("billno", (String) aggVO.getParentVO()
				.getAttributeValue("billno"));
		return JSON.toJSONString(dataMap);
		}

		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {
			// 转换数据为VO

			AggregatedValueObject billvo = onTranBill(value);

			HashMap<String, String> eParam = new HashMap<String, String>();
			// 持久化工单VO
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "FN11",
					null, billvo, null, eParam);
			AggChangeBillHVO[] billvos = (AggChangeBillHVO[]) obj;
			obj = getPfBusiAction().processAction("SAVE", "FN11", null, billvos[0],
					null, eParam);
			billvos = (AggChangeBillHVO[]) obj;
			getPfBusiAction().processAction("APPROVE", "FN11", null, billvos[0],
					null, eParam);
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}
	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param hMap
	 * @return
	 */
	private AggChangeBillHVO onTranBill(HashMap<String, Object> value)
			throws BusinessException {

		String errorMsg = null;
		if (value == null || value.size() <= 0) {
			errorMsg = "数据为空，请检查请求参数";
			throw new BusinessException(errorMsg);
		}
		// 获取表头和表体的数据
		JSONObject headObj = (JSONObject) value.get("headInfo");
		JSONArray bodyArray = (JSONArray) value.get("itemInfo");

		if (headObj == null || headObj.size() <= 0) {
			errorMsg = "数据异常，必须有表头数据";
			throw new BusinessException(errorMsg);
		}

		// 检验表头表体数据
		validateHeadData(headObj);
		validateBodyData(bodyArray);

		AggChangeBillHVO aggvo = new AggChangeBillHVO();

		ChangeBillHVO hvo = new ChangeBillHVO();
		// 默认集团-时代集团-code:0001
		hvo.setPk_group("000112100000000005FD");
		hvo.setPk_org(getRefAttributePk("pk_org", headObj.getString("pk_org")));
		hvo.setBilldate(new UFDate(headObj.getString("billdate")));
		// 默认交易类型-code:FN11
		hvo.setTranstype("FN11");
		// 默认单据类型-code:FN11
		hvo.setBilltype("FN11");

		hvo.setTranstype(headObj.getString("transtype"));
		hvo.setTranstypepk(getRefAttributePk("billtype",
				headObj.getString("transtype")));
		hvo.setBillmaker(getSaleUserID());
		// 默认单据状态-1：自由
		hvo.setBillstatus(-1);
		// 默认审批状态-1：自由
		hvo.setApprovestatus(-1);
		// 默认生效状态0：未生效
		hvo.setEffectstatus(0);
		hvo.setDef1(headObj.getString("def1"));
		hvo.setDef2(headObj.getString("def2"));
		hvo.setDef3(headObj.getString("def3"));
		hvo.setDef4(headObj.getString("def4"));
		//TODO
		hvo.setDef5(headObj.getString("isreded"));// 红冲标志
		hvo.setDef6(headObj.getString("def5"));
		hvo.setDef7(headObj.getString("def6"));
		hvo.setDef8(headObj.getString("def7"));
		hvo.setDef9(headObj.getString("def8"));
		hvo.setDef10(getUserByPsondoc(headObj.getString("def10")));//销售系统审批人
		hvo.setDef11(getCustpk(headObj.getString("def11")));//客户名称
		hvo.setDef15(headObj.getString("def15"));//附件张数
		aggvo.setParentVO(hvo);
		ChangeBillBVO[] bvos = new ChangeBillBVO[bodyArray.size()];
		for (int i = 0; i < bvos.length; i++) {
			ChangeBillBVO bvo = new ChangeBillBVO();
			JSONObject bJSONObject = bodyArray.getJSONObject(i);
			bvo.setScomment(bJSONObject.getString("scomment"));
			bvo.setDef1(getPk_projectByCode(bJSONObject.getString("def1")));
			bvo.setDef2(getPk_projectByCode(bJSONObject.getString("def2")));
			bvo.setDef3(getPk_defdocByNameORCode(bJSONObject.getString("def3"),"zdy020"));
			bvo.setDef4(getPk_defdocByNameORCode(bJSONObject.getString("def4"),"zdy021"));
			bvo.setDef5(getPk_defdocByNameORCode(bJSONObject.getString("def5"),"zdy009"));
			bvo.setDef6(getPk_defdocByNameORCode(bJSONObject.getString("def6"),"zdy041"));
			int rate = new UFDouble(String.valueOf((bJSONObject
					.getString("def7") == null ? 0 : bJSONObject
					.getString("def7")))).intValue();
			bvo.setDef7(String.valueOf(rate));
			bvo.setDef8(bJSONObject.getString("def8"));
			bvo.setDef9(bJSONObject.getString("def9"));
			bvo.setDef10(bJSONObject.getString("def10"));
			bvo.setDef11(bJSONObject.getString("def11"));
			bvo.setDef12(bJSONObject.getString("def12"));// 土地款抵扣税额
			bvo.setDef13(bJSONObject.getString("def13"));
			bvo.setDef14(bJSONObject.getString("def14"));
			bvo.setDef15(bJSONObject.getString("def15"));
			bvo.setDef16(bJSONObject.getString("def16"));
			bvo.setDef17(bJSONObject.getString("def17"));
			bvo.setDef18(bJSONObject.getString("def18"));
			bvo.setDef19(bJSONObject.getString("def19"));
			bvos[i] = bvo;
		}
		
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}


	private void validateHeadData(JSONObject data) throws BusinessException {

		String pkOrg = data.getString("pk_org");
		String billDate = data.getString("billdate");
		// 销售系统单据ID
		String def1 = data.getString("def1");
		// 销售系统单号
		// String def2 = data.getString("def2");
		// 影像编码
		String def3 = data.getString("def3");
		// 影像状态
		String def4 = data.getString("def4");

		if (pkOrg == null || "".equals(pkOrg)) {
			throw new BusinessException("财务组织不能为空");
		}

		if (billDate == null || "".equals(billDate)) {
			throw new BusinessException("单据日期不能为空");
		}
		if (def1 == null || "".equals(def1)) {
			throw new BusinessException("销售系统单据ID不能为空");
		}
		// if (def2 == null || "".equals(def2)) {
		// throw new BusinessException("销售系统单号不能为空");
		// }
//		if (def3 == null || "".equals(def3)) {
//			throw new BusinessException("影像编码不能为空");
//		}
//		if (def4 == null || "".equals(def4)) {
//			throw new BusinessException("影像状态不能为空");
//		}

	}

	private void validateBodyData(JSONArray jsonArray) throws BusinessException {

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject data = jsonArray.getJSONObject(i);
			// 摘要
//			String scomment = data.getString("scomment");
			// NC:房地产项目-SALE:所属项目
			String def1 = data.getString("def1");
			// NC:房地产项目明细-SALE:购买房间
			String def2 = data.getString("def2");
			// NC,SALE:款项类型
			String def3 = data.getString("def3");
			// NC,SALE:款项名称
			String def4 = data.getString("def4");
			// NC,SALE:业态
			String def5 = data.getString("def5");
			// NC,SALE:计税方式
			String def6 = data.getString("def6");
			// NC,SALE:税率
			String def7 = data.getString("def7");
			// NC,SALE:税额
			String def8 = data.getString("def8");
			// NC:不含税金额-SALE:折人民币金额（不含税）
			String def9 = data.getString("def9");
			// 含税金额
			String def10 = data.getString("def10");
			// 土地款抵扣税额
			String def12 = data.getString("def12");

			if (def1 == null || "".equals(def1)) {
				throw new BusinessException("【" + (i + 1) + "】所属项目不能为空");
			}
			if (def2 == null || "".equals(def2)) {
				throw new BusinessException("【" + (i + 1) + "】购买房间不能为空");
			}
			if (def3 == null || "".equals(def3)) {
				throw new BusinessException("【" + (i + 1) + "】款项类型不能为空");
			}
			if (def4 == null || "".equals(def4)) {
				throw new BusinessException("【" + (i + 1) + "】款项名称不能为空");
			}
			if (def5 == null || "".equals(def5)) {
				throw new BusinessException("【" + (i + 1) + "】业态不能为空");
			}
			if (def6 == null || "".equals(def6)) {
				throw new BusinessException("【" + (i + 1) + "】计税方式不能为空");
			}
			if (def7 == null || "".equals(def7)) {
				throw new BusinessException("【" + (i + 1) + "】税率不能为空");
			}
			if (def8 == null || "".equals(def8)) {
				throw new BusinessException("【" + (i + 1) + "】税额不能为空");
			}
			if (def9 == null || "".equals(def9)) {
				throw new BusinessException("【" + (i + 1) + "】折人民币金额（不含税）不能为空");
			}
//			if (scomment == null || "".equals(scomment)) {
//				throw new BusinessException("【" + (i + 1) + "】摘要不能为空");
//			}
			if (def10 == null || "".equals(def10)) {
				throw new BusinessException("【" + (i + 1) + "】含税金额不能为空");
			}
			if (def12 == null || "".equals(def12)) {
				throw new BusinessException("【" + (i + 1) + "】土地款抵扣税额不能为空");
			}
		}
	}
	/**
	 * 获取客商pk
	 * @param code
	 * @return
	 */
	public String getCustpk(String name){
		String result = null;
		String sql = "SELECT pk_customer FROM bd_customer WHERE name = '"+name+"' AND enablestate  = '2' AND NVL(dr,0)=0";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
