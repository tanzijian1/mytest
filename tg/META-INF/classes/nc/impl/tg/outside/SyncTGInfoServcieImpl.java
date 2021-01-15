package nc.impl.tg.outside;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.core.util.ObjectCreator;
import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.ISyncTGInfoServcie;
import nc.itf.tg.outside.ITGCallService;
import nc.itf.tg.outside.ITGSyncService;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.tg.outside.CallImplInfoVO;

public class SyncTGInfoServcieImpl implements ISyncTGInfoServcie {

	@Override
	public String onSyncInfo_RequiresNew(String srcsystem, String method,
			HashMap<String, Object> info) throws BusinessException {
		if (StringUtil.isEmptyWithTrim(srcsystem)) {
			throw new BusinessException("来源系统不可为空!");
		}
		if (StringUtil.isEmptyWithTrim(method)) {
			throw new BusinessException("调用接口方法不可为空!");
		}
		String className = null;
		Map<String, String> implInfo = TGOutsideUtils.getUtils()
				.getReleaseImplInfo(srcsystem, method);
		if (implInfo != null) {
			className = implInfo.get("classname");
		}
		if (StringUtil.isEmptyWithTrim(className)) {
			throw new BusinessException("来源系统【" + srcsystem + "】调用接口方法【"
					+ method + "】未关联到对应实现类,请联系系统管理员!");
		}
		Object obj = ObjectCreator.newInstance(className);
		if (obj == null) {
			throw new BusinessException("未能找到对应接口类,请联系系统管理员");
		}
		if (!(obj instanceof ITGSyncService)) {
			throw new BusinessException("接口实现类出错,请检查接口信息!");
		}
		ITGSyncService servcie = (ITGSyncService) obj;
		return servcie.onSyncInfo(info, implInfo.get("name"));
	}

	@Override
	public CallImplInfoVO getCallImplInfoVO(String dessys, String method,
			Object value) throws BusinessException {
		if (StringUtil.isEmptyWithTrim(dessys)) {
			throw new BusinessException("来源系统不可为空!");
		}
		if (StringUtil.isEmptyWithTrim(method)) {
			throw new BusinessException("调用接口方法不可为空!");
		}
		String className = null;
		Map<String, String> implInfo = TGOutsideUtils.getUtils()
				.getCallImplInfo(dessys, method);
		if (implInfo != null) {
			className = implInfo.get("classname");
		}
		if (StringUtil.isEmptyWithTrim(className)) {
			throw new BusinessException("目标系统【" + dessys + "】调用接口方法【" + method
					+ "】未关联到对应实现类,请联系系统管理员!");
		}
		Object obj = ObjectCreator.newInstance(className);
		if (obj == null) {
			throw new BusinessException("未能找到对应接口类,请联系系统管理员");
		}
		if (!(obj instanceof ITGCallService)) {
			throw new BusinessException("接口实现类出错,请检查接口信息!");
		}
		ITGCallService servcie = (ITGCallService) obj;
		return servcie.getImplInfo(dessys, className, value);
	}

	@Override
	public String onCallInfo(CallImplInfoVO info) throws BusinessException {
		Object obj = ObjectCreator.newInstance(info.getClassName());
		if (obj == null) {
			throw new BusinessException("未能找到对应接口类,请联系系统管理员");
		}
		if (!(obj instanceof ITGCallService)) {
			throw new BusinessException("接口实现类出错,请检查接口信息!");
		}
		ITGCallService servcie = (ITGCallService) obj;
		return servcie.onCallMethod(info);
	}

	@Override
	public void insertVO_RequiresNew(SuperVO vo) throws BusinessException {
		BillUtils.getUtils().getBaseDAO().insertVO(vo);
	}

	@Override
	public String onCallInfo_RequiresNew(CallImplInfoVO info)
			throws BusinessException {
		// TODO 自动生成的方法存根
		return onCallInfo(info);
	}

}
