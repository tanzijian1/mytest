package nc.bs.tg.organization.plugin.bpplugin;

import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;

/**
 * 
 * 
 * @author
 * 
 */
public enum OrganizationtypePluginPoint implements IPluginPoint {
	/**
	 * ÐÂÔö
	 */
	INSERT,
	/**
	 * ÐÞ¸Ä
	 */
	UPDATE,
	/**
	 * É¾³ý
	 */
	DELETE;

	@Override
	public String getComponent() {
		return "OrganizationType";
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
