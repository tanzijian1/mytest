package nc.bs.tg.capitalmarketrepay.plugin.bpplugin;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;

/**
 * ��׼���ݵ���չ�����
 * 
 */
public enum CapitalmarketrepayPluginPoint implements IPluginPoint {
	/**
	 * ����
	 */
	APPROVE,
	/**
	 * ����
	 */
	SEND_APPROVE,

	/**
	 * ȡ�����
	 */
	UNAPPROVE,

	/**
	 * �ջ�
	 */
	UNSEND_APPROVE,
	/**
	 * ɾ��
	 */
	DELETE,
	/**
	 * ����
	 */
	INSERT,
	/**
	 * ����
	 */
	UPDATE,
	/**
	 * �ű�ɾ��
	 */
	SCRIPT_DELETE,
	/**
	 * �ű�����
	 */
	SCRIPT_INSERT,
	/**
	 * �ű�����
	 */
	SCRIPT_UPDATE;

	@Override
	public String getComponent() {
		return "SDFN";
	}

	@Override
	public String getModule() {
		return "tg";
	}

	@Override
	public String getPoint() {
		return this.getClass().getName() + "." + this.name();
	}

}
