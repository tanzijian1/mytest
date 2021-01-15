package nc.ui.tg.report.freereport.published;

import nc.itf.iufo.constants.ModuleConstants;
import nc.ui.iufo.freereport.FreeRepPublishUtil;
import nc.ui.iufo.freereport.FreeReportFunclet;

public class OutLogRepFunclet extends FreeReportFunclet {
	public static final String DEFAULT_PUBLISHED_ZIOR_FILE = ModuleConstants.MODULE_PATH
			+ "bi/zior/zior-outlog-published.xml";

	/**
	 * 加载发布后节点的zior文件
	 * 
	 * @see nc.ui.iufo.freereport.FreeReportFunclet#getConfigFile()
	 */
	public String getConfigFile() {
		String confFile = getParameter(FreeRepPublishUtil.ZIOR_CONF_FILE);
		if (confFile != null && confFile.length() > 0) {
			return confFile;
		}
		return ModuleConstants.MODULE_PATH
				+ "bi/zior/zior-outlog-published.xml";
	}
}
