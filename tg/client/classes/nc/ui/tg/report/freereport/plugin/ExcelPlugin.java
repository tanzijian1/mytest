package nc.ui.tg.report.freereport.plugin;

import nc.ui.tg.report.freereport.action.ExcelExportAction;
import nc.ui.tg.report.freereport.action.ExcelImportAction;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class ExcelPlugin extends AbstractPlugin {

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { new ExcelImportAction(),
				new ExcelExportAction() };
	}

	@Override
	public void startup() {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void shutdown() {
		// TODO �Զ����ɵķ������

	}

}
