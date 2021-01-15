package nc.bs.os.outside;

import java.util.Date;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.ISyncTGInfoServcie;
import nc.itf.uap.IVOPersistence;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.outside.CallImplInfoVO;
import nc.vo.tg.outside.OSImplLogVO;
import nc.vo.tg.outside.ResultVO;

import com.alibaba.fastjson.JSON;

/**
 * 调用外部系统公共类
 * 
 * @author HDQ
 * 
 */
public class TGCallUtils {
	static TGCallUtils utils = null;
	ISyncTGInfoServcie service = null;
	IVOPersistence persistence = null;

	public static TGCallUtils getUtils() {
		if (utils == null) {
			utils = new TGCallUtils();
		}
		return utils;
	}

	public ISyncTGInfoServcie getService() {
		if (service == null) {
			service = NCLocator.getInstance().lookup(ISyncTGInfoServcie.class);
		}
		return service;
	}

	/**
	 * 调用目标系统服务接口
	 * 
	 * @param pk_relation
	 * @param dessystem
	 * @param method
	 * @param info
	 * @return
	 * @throws BusinessException
	 */
	public ResultVO onDesCallService(String pk_relation, String dessystem,
			String method, Object value) throws BusinessException {
		OSImplLogVO logVO = new OSImplLogVO();
		ResultVO resultVO = new ResultVO();
		logVO.setPk_relation(pk_relation);
		logVO.setExedate(new UFDateTime(new Date()).toString());
		logVO.setResult(BillUtils.STATUS_SUCCESS);
		logVO.setSrcsystem("NC");
		logVO.setDessystem(dessystem);
		logVO.setMethod(method);
		logVO.setOperator(AppContext.getInstance().getPkUser());
		logVO.setDr(new Integer(0));
		StringBuffer srcparm = new StringBuffer();
		CallImplInfoVO info = null;
		try {
			info = getService().getCallImplInfoVO(dessystem, method, value);
			srcparm.append("调用地址:" + info.getUrls() + "\n\n").append(
					"密钥:" + info.getToken() + "\n\n");
			srcparm.append("报文:" + info.getPostdata() + "\n\n");
			logVO.setSrcparm(srcparm.toString());
			String result = null;
			ISyncTGInfoServcie servcie = NCLocator.getInstance().lookup(
					ISyncTGInfoServcie.class);
			if ("Y".equals(info.getIsrequiresnew())) {
				result = servcie.onCallInfo_RequiresNew(info);
			} else {
				result = servcie.onCallInfo(info);
			}
			if (info.getOther() != null && info.getOther().size() > 0) {
				srcparm.append("其他:" + JSON.toJSONString(info.getOther())
						+ "\n\n");
			}
			resultVO.setRsmstate(BillUtils.STATUS_SUCCESS);
			resultVO.setData(result);
			logVO.setMsg(result);
		} catch (Exception e) {
			logVO.setMsg(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			if (info != null) {
				if (info.getOther() != null && info.getOther().size() > 0) {
					srcparm.append("其他:" + JSON.toJSONString(info.getOther())
							+ "\n\n");
				}
			}
			Logger.error(e.getMessage(), e);
			resultVO.setMsg(e.getMessage());
			resultVO.setRsmstate(BillUtils.STATUS_FAILED);
			logVO.setResult(BillUtils.STATUS_FAILED);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			try {
				getService().insertVO_RequiresNew(logVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return resultVO;
	}
}
