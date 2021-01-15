package nc.bs.baseapp.itfformulacfg.plugin.bpplugin;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;

/**
 * @Description:
 * @version with NC V6.5
 */
public enum ItfformulacfgPluginPoint implements IPluginPoint {
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
		return "itfformulacfg";
	}

	@Override
	public String getModule() {
		return "baseapp";
	}

	@Override
	public String getPoint() {
		return this.getClass().getName() + "." + this.name();
	}

}
