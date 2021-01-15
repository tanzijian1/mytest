package nc.ui.tg.report.freereport.query;

import java.util.ArrayList;
import java.util.Arrays;

import nc.ui.tg.report.freereport.plugin.ExcelPlugin;

import com.ufida.report.free.publish.query.FreeReportQueryViewer;

public class MortgageListRepQryViewer extends FreeReportQueryViewer {
	public String[] createPluginList() {
		ArrayList<String> pluginList = new ArrayList<String>();
		String[] plugins = super.createPluginList();
		pluginList.addAll(Arrays.asList(plugins));
		pluginList.add(ExcelPlugin.class.getName());
		return pluginList.toArray(new String[0]);
	}
}
