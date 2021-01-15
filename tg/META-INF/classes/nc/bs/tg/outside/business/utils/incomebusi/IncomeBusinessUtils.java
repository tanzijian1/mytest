package nc.bs.tg.outside.business.utils.incomebusi;

import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.itf.tools.ItfJsonTools;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.bs.tg.outside.itf.utils.ItfUtils;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.BusinessBillCont;
import nc.itf.tg.outside.ItfConstants;
import nc.vo.itf.result.ResultVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.BusinessBillLogVO;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.tgfn.changebill.ChangeBillBVO;
import nc.vo.tgfn.changebill.ChangeBillHVO;
import uap.json.JSONObject;

import com.alibaba.fastjson.JSON;

public class IncomeBusinessUtils extends BusinessBillUtils{
	static IncomeBusinessUtils utils;

	public static IncomeBusinessUtils getUtils() {
		if (utils == null) {
			utils = new IncomeBusinessUtils();
		}
		return utils;
	}

	public String getValue(JSONObject value, String srctype)
			throws BusinessException {
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// 设置用户码
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		HashMap<String, String> dataMap = new HashMap<String, String>();
		
		ISqlThread sql=NCLocator.getInstance().lookup(ISqlThread.class);

		// 日志信息
		BusinessBillLogVO logVO = new BusinessBillLogVO();
		ResultVO resultVO = new ResultVO();
		String rsInfo = "";
		JSONObject Object = (JSONObject) value.get("data");// 报文内容
		JSONObject jsonObject = Object.getJSONObject("headInfo");//表头信息
		String srcid = jsonObject.getString("def1");// 销售系统业务单据ID
		String srcno = jsonObject.getString("def2");// 销售系统业务单据单据号

		try {

			logVO.setSrcsystem(BusinessBillCont.SRCSYS);
			logVO.setSrcparm(value.toString());
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult(STATUS_SUCCESS);
			logVO.setOperator(OperatorName);
			logVO.setDesbill(BusinessBillCont.getBillNameMap().get(srctype));
			logVO.setTrantype("FN11-Cxx-SY001");
			logVO.setBusinessno(jsonObject.getString("def2"));
			String billqueue = BusinessBillCont.getBillNameMap().get(srctype)
					+ ":" + srcid;
			String billkey = BusinessBillCont.getBillNameMap().get(srctype)
					+ ":" + srcno;

			
			// srcid 按实际存入信息位置进行变更
			AggChangeBillHVO aggVO = (AggChangeBillHVO) getBillVO(
					AggChangeBillHVO.class, "nvl(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【" + billkey + "】,NC已存在对应的业务单据【"
						+ srcid + "】,请勿重复上传!");
			}
			addBillQueue(billqueue);// 增加队列处理

			AggregatedValueObject[] aggvo = null;

			try {
				AggChangeBillHVO billvo = onTranBill(value, srctype);
				// HashMap eParam = new HashMap();
				// eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				// PfUtilBaseTools.PARAM_NOTE_CHECKED);
				AggChangeBillHVO[] aggvos = (AggChangeBillHVO[]) getPfBusiAction().processAction(
						"SAVEBASE", "FN11", null, billvo, null, null);
				aggvo = (AggregatedValueObject[]) getPfBusiAction().processAction(
						"SAVE", "FN11", null, aggvos[0], null, null);
			} catch (Exception e) {
				throw new BusinessException("【" + billkey + "】,"
						+ e.getMessage(), e);
			} finally {
				removeBillQueue(billqueue);
			}
			dataMap.put("billid", aggvo[0].getParentVO().getPrimaryKey());
			dataMap.put("billno", (String) aggvo[0].getParentVO()
					.getAttributeValue("billno"));

			logVO.setBillno((String) aggvo[0].getParentVO().getAttributeValue(
					"billno"));
			logVO.setErrmsg(JSON.toJSONString(dataMap));
			resultVO.setBillid((String) aggvo[0].getParentVO()
					.getAttributeValue("billno"));
			resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
			resultVO.setMsg("操作成功");
		} catch (Exception e) {
			logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			Logger.error(e.getMessage(), e);
			logVO.setResult(STATUS_FAILED);
			resultVO.setBillid(srcid);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
			resultVO.setMsg("操作异常：" + e.getMessage());
		} finally {
			rsInfo = net.sf.json.JSONObject.fromObject(resultVO).toString();
			try {
				sql.billInsert_RequiresNew(logVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return rsInfo;
	}

	private AggChangeBillHVO onTranBill(JSONObject value, String srctype)
			throws Exception {
		JSONObject Object = (JSONObject) value.get("data");// 报文内容

		AggChangeBillHVO billVO = ItfJsonTools.jsonToAggVO(Object,
				AggChangeBillHVO.class, ChangeBillHVO.class,
				ChangeBillBVO.class);

		// // 后台转换
		// GatheringBillItemVO[] gitemvos = (GatheringBillItemVO[]) billVO
		// .getChildrenVO();
		// for (GatheringBillItemVO gatheringBillItemVO : gitemvos) {
		// // 收款银行账户
		// gatheringBillItemVO.setRecaccount(getPersonalAccountIDByCode(
		// (String) billVO.getParentVO().getAttributeValue("pk_org"),
		// gatheringBillItemVO.getSupplier()));
		// }

		// 转换前非空校验、执行公式、翻译后各项非空校验
		ItfUtils.notNullCheckAndExFormula(ItfConstants.INCOME_HEAD,
				ItfConstants.INCOME_BODY, billVO);

		return billVO;
	}
}
