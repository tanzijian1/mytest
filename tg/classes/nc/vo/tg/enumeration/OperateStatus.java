package nc.vo.tg.enumeration;

import nc.vo.ml.NCLangRes4VoTransl;

public enum OperateStatus {
	/**
	 * �޶�
	 */
	Emend(0),
	/**
	 * Ĭ��
	 */
	Init(-1);
	// ö�ٵ�����ֵ
	private int intValue;

	/**
	 * ö�ٵĹ��췽��
	 * 
	 * @param intValue
	 */
	private OperateStatus(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	@Override
	public String toString() {
		switch (this.getIntValue()) {
		case -1:
			return "��ͨ";
		case 0:
			return "�޶�";
		default:
			break;
		}
		return "UnknownStatus";
	}
}
