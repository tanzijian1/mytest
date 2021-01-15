package nc.bs.tg.zhsc.alter;

import nc.bs.os.outside.TGCallUtils;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.vo.pub.BusinessException;

/**
 * 将综合商城应付单同步到NC财务系统定时任务
 * @author zhaozhiying
 *
 */
public class AoutSysncZHSCPayableBillToNCTask implements IBackgroundWorkPlugin {

	@Override
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		//模拟手动调用获取应付单的接口
		TGCallUtils.getUtils().onDesCallService("", "zhsc", "getPayableBill", "");
		
		PreAlertObject retObj = new PreAlertObject();
		retObj.setReturnObj("业务插件成功执行完毕.");
		retObj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
		return retObj;
	}

}
