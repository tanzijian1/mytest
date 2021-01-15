package nc.vo.tg.enumeration;

import nc.vo.ml.NCLangRes4VoTransl;

public enum OperateStatus {
	/**
	 * 修订
	 */
	Emend(0),
	/**
	 * 默认
	 */
	Init(-1);
	// 枚举的整型值
	private int intValue;

	/**
	 * 枚举的构造方法
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
			return "普通";
		case 0:
			return "修订";
		default:
			break;
		}
		return "UnknownStatus";
	}
}
