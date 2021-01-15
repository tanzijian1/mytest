package nc.bs.tg.alter.result;

public class TGInterestMessage extends TGAlertMessage {
	public final static String CODE = "code";// ����
	public final static String NAME = "name";// ����
	public final static String MSG = "msg";// ������

	@Override
	public String[] getNames() {
		return new String[] { CODE, NAME, MSG };
	}

	@Override
	public int[] getColumnType() {
		return new int[] { TYPE_STRING, TYPE_STRING, TYPE_STRING };
	}

	@Override
	public float[] getWidths() {
		return new float[] { 0.1F, 0.1F, 0.8F };

	}
}
