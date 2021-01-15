package nc.bs.tg.alter.plugin.word.result;

import nc.bs.tg.alter.result.TGAlertMessage;

public class WordAlterMessage extends TGAlertMessage {
	public final static String ORG = "组织";// 组织
	public final static String PERIOD = "期间";// 期间
	public final static String MSG = "处理结果";//

	@Override
	public String[] getNames() {
		return new String[] { ORG, PERIOD, MSG };
	}

	@Override
	public int[] getColumnType() {
		return new int[] { TYPE_STRING, TYPE_STRING, TYPE_STRING };
	}

	@Override
	public float[] getWidths() {
		return new float[] { 0.2F, 0.1F, 0.7F };

	}
}
