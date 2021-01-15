package nc.bs.tg.alter.plugin.ll.message;

public class EBSAlterMessage extends AlertMessage {
	public final static String CODE = "�������";// ����
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
