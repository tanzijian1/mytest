package nc.bs.tg.zhsc.alter;

import nc.bs.os.outside.TGCallUtils;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.vo.pub.BusinessException;

/**
 * ���ۺ��̳�Ӧ����ͬ����NC����ϵͳ��ʱ����
 * @author zhaozhiying
 *
 */
public class AoutSysncZHSCPayableBillToNCTask implements IBackgroundWorkPlugin {

	@Override
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		//ģ���ֶ����û�ȡӦ�����Ľӿ�
		TGCallUtils.getUtils().onDesCallService("", "zhsc", "getPayableBill", "");
		
		PreAlertObject retObj = new PreAlertObject();
		retObj.setReturnObj("ҵ�����ɹ�ִ�����.");
		retObj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
		return retObj;
	}

}
