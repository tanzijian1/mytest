package nc.bs.tg.outside.archives;

public class WYSFAlterMessage extends TGAlertMessage {

	public final static String CODE = "结算编码";// 编码
	public final static String MSG = "处理结果";// 处理结果

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
