package nc.bs.baseapp.itfformulacfg.plugin.bpplugin;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;

/**
 * @Description:
 * @version with NC V6.5
 */
public enum ItfformulacfgPluginPoint implements IPluginPoint {
	/**
	 * 审批
	 */
	APPROVE,
	/**
	 * 送审
	 */
	SEND_APPROVE,

	/**
	 * 取消审核
	 */
	UNAPPROVE,

	/**
	 * 收回
	 */
	UNSEND_APPROVE,
	/**
	 * 删除
	 */
	DELETE,
	/**
	 * 新增
	 */
	INSERT,
	/**
	 * 更新
	 */
	UPDATE,
	/**
	 * 脚本删除
	 */
	SCRIPT_DELETE,
	/**
	 * 脚本新增
	 */
	SCRIPT_INSERT,
	/**
	 * 脚本更新
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
