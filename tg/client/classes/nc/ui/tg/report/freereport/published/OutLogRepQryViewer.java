package nc.ui.tg.report.freereport.published;

import java.util.ArrayList;
import java.util.Arrays;

import com.ufida.report.free.publish.query.FreeReportQueryViewer;

public class OutLogRepQryViewer extends FreeReportQueryViewer {
	public String[] createPluginList() {
		ArrayList<String> pluginList = new ArrayList<String>();
		String[] plugins = super.createPluginList();
		pluginList.addAll(Arrays.asList(plugins));
		pluginList.add(LogTextPlugin.class.getName());
		return pluginList.toArray(new String[0]);
	}
}
