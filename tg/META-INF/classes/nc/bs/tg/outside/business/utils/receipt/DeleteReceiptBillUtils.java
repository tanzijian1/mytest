package nc.bs.tg.outside.business.utils.receipt;

/**
 * 收款核销单删除
 * @author huangxj
 * 
 */

import java.util.HashMap;

import com.alibaba.fastjson.JSON;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.itf.tg.outside.BusinessBillCont;
import nc.itf.tg.outside.EBSCont;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.itf.result.ResultVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.BusinessBillLogVO;
import uap.json.JSONObject;

public class DeleteReceiptBillUtils extends BusinessBillUtils {

	static DeleteReceiptBillUtils utils;

	public static DeleteReceiptBillUtils getUtils() {
		if (utils == null) {
			utils = new DeleteReceiptBillUtils();
		}
		return utils;
	}

	/**
	 * 收款核销单
	 * 
	 * @param value
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
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
		
		ResultVO resultVO = new ResultVO();
		String rsInfo = "";

		// 日志记录VO
		BusinessBillLogVO logVO = new BusinessBillLogVO();
		logVO.setSrcsystem(BusinessBillCont.SRCSYS);
		logVO.setSrcparm(value.toString());
		logVO.setExedate(new UFDateTime().toString());
		logVO.setResult(STATUS_SUCCESS);
		logVO.setOperator(OperatorName);
		logVO.setDesbill(BusinessBillCont.getBillNameMap().get(srctype));
		logVO.setTrantype(srctype);
		logVO.setBusinessno(value.getString("srcno"));
		resultVO.setBillid(value.getString("srcid"));
		// 返回参
		String result = null;
		try {
			// 执行删除
			result = execute(value, srctype);
			logVO.setErrmsg("操作成功");
			logVO.setBillno(result);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
			resultVO.setMsg("操作成功");
		} catch (Exception e) {
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("srcbillno", value.getString("srcno"));
			dataMap.put(
					"msg",
					"删除失败"
							+ org.apache.commons.lang.exception.ExceptionUtils
									.getFullStackTrace(e));
			logVO.setErrmsg(JSON.toJSONString(dataMap));
			Logger.error(e.getMessage(), e);
			logVO.setResult(STATUS_FAILED);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
			resultVO.setMsg("操作异常：" + e.getMessage());
		} finally {
			rsInfo = net.sf.json.JSONObject.fromObject(resultVO).toString();
			try {
				getBaseDAO().insertVO(logVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return rsInfo;
	}

	/**
	 * 执行删除方法
	 * 
	 * @param value
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	private String execute(JSONObject value, String srctype)
			throws BusinessException {
		String srcno = value.getString("srcno");
		String billqueue = EBSCont.getBillNameMap().get(srctype) + ":" + srcno;
		addBillQueue(billqueue);// 增加队列处理

		// 查询对应的收款单
		AggGatheringBillVO aggVO = (AggGatheringBillVO) getBillVO(
				AggGatheringBillVO.class, "isnull(dr,0)=0  and def2 = '" + srcno
						+ "'");
		if (aggVO == null) {
			throw new BusinessException(srcno + "此单据已被删除或者没在NC创建");
		}
		GatheringBillVO headvo = (GatheringBillVO) aggVO.getParent();
		// 如果不是自由态便不允许删除单据
		if (headvo.getApprovestatus() != -1 && headvo.getApprovestatus() != 3) {
			throw new BusinessException(srcno + "此单已在流程中不可删除，如需删除请上NC系统取消流程");
		}

		try {
			getPfBusiAction().processAction(
					"DELETE", "F2", null, aggVO, null, null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			removeBillQueue(billqueue);// 移除队列
		}
		return headvo.getBillno();
	}
}
