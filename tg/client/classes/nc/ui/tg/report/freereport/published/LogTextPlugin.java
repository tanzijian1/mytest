package nc.ui.tg.report.freereport.published;

import nc.ui.tg.report.freereport.published.action.LogTextExportAction;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class LogTextPlugin extends AbstractPlugin {

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { new LogTextExportAction() };
	}

	@Override
	public void startup() {

	}

	@Override
	public void shutdown() {

	}

}
