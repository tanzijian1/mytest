package nc.bs.tg.alter.plugin.bpm.result;

import nc.bs.tg.alter.result.TGAlertMessage;

public class BPMAlterMessage extends TGAlertMessage {
	private static final long serialVersionUID = 1L;
	public final static String CODE = "���ݱ��";// ����
	public final static String MSG = "������";// ������

	@Override
	public String[] getNames() {
		return new String[] { CODE, MSG };
	}

	@Override
	public int[] getColumnType() {
		return new int[] { TYPE_STRING, TYPE_STRING };
	}

	@Override
	public float[] getWidths() {
		return new float[] { 0.2F, 0.8F };

	}

}
