package nc.bs.tg.alter.plugin.word;

import nc.vo.pub.BusinessException;

public class SyncVoucherToWord extends AoutSysncWordData {
	@Override
	protected String getWorkResult(String orgid, String yearmth)
			throws BusinessException {
		String[] key = yearmth.split("-");
		getWordServcie().onSyncVoucherMinTab_RequiresNew(orgid, key[0], key[1]);
		return "同步完成!";
	}
}
