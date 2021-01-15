package nc.bs.tg.mortgagelistdetailed.plugin.bpplugin;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;

/**
 * 标准单据的扩展插入点
 * 
 */
public enum MortgageListDetailedPluginPoint implements IPluginPoint {
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
	UPDATE;

	@Override
	public String getComponent() {
		return "Financing";
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
