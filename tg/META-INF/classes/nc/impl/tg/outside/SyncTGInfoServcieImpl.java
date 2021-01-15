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
			throw new BusinessException("��Դϵͳ����Ϊ��!");
		}
		if (StringUtil.isEmptyWithTrim(method)) {
			throw new BusinessException("���ýӿڷ�������Ϊ��!");
		}
		String className = null;
		Map<String, String> implInfo = TGOutsideUtils.getUtils()
				.getReleaseImplInfo(srcsystem, method);
		if (implInfo != null) {
			className = implInfo.get("classname");
		}
		if (StringUtil.isEmptyWithTrim(className)) {
			throw new BusinessException("��Դϵͳ��" + srcsystem + "�����ýӿڷ�����"
					+ method + "��δ��������Ӧʵ����,����ϵϵͳ����Ա!");
		}
		Object obj = ObjectCreator.newInstance(className);
		if (obj == null) {
			throw new BusinessException("δ���ҵ���Ӧ�ӿ���,����ϵϵͳ����Ա");
		}
		if (!(obj instanceof ITGSyncService)) {
			throw new BusinessException("�ӿ�ʵ�������,����ӿ���Ϣ!");
		}
		ITGSyncService servcie = (ITGSyncService) obj;
		return servcie.onSyncInfo(info, implInfo.get("name"));
	}

	@Override
	public CallImplInfoVO getCallImplInfoVO(String dessys, String method,
			Object value) throws BusinessException {
		if (StringUtil.isEmptyWithTrim(dessys)) {
			throw new BusinessException("��Դϵͳ����Ϊ��!");
		}
		if (StringUtil.isEmptyWithTrim(method)) {
			throw new BusinessException("���ýӿڷ�������Ϊ��!");
		}
		String className = null;
		Map<String, String> implInfo = TGOutsideUtils.getUtils()
				.getCallImplInfo(dessys, method);
		if (implInfo != null) {
			className = implInfo.get("classname");
		}
		if (StringUtil.isEmptyWithTrim(className)) {
			throw new BusinessException("Ŀ��ϵͳ��" + dessys + "�����ýӿڷ�����" + method
					+ "��δ��������Ӧʵ����,����ϵϵͳ����Ա!");
		}
		Object obj = ObjectCreator.newInstance(className);
		if (obj == null) {
			throw new BusinessException("δ���ҵ���Ӧ�ӿ���,����ϵϵͳ����Ա");
		}
		if (!(obj instanceof ITGCallService)) {
			throw new BusinessException("�ӿ�ʵ�������,����ӿ���Ϣ!");
		}
		ITGCallService servcie = (ITGCallService) obj;
		return servcie.getImplInfo(dessys, className, value);
	}

	@Override
	public String onCallInfo(CallImplInfoVO info) throws BusinessException {
		Object obj = ObjectCreator.newInstance(info.getClassName());
		if (obj == null) {
			throw new BusinessException("δ���ҵ���Ӧ�ӿ���,����ϵϵͳ����Ա");
		}
		if (!(obj instanceof ITGCallService)) {
			throw new BusinessException("�ӿ�ʵ�������,����ӿ���Ϣ!");
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
		// TODO �Զ����ɵķ������
		return onCallInfo(info);
	}

}
