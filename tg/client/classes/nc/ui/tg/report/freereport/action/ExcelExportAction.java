package nc.ui.tg.report.freereport.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.apache.poi.ss.usermodel.Workbook;

import nc.funcnode.ui.ActionButton;
import nc.ui.pub.beans.MessageDialog;

import com.ufida.report.anadesigner.AbsAnaReportDesigner;
import com.ufida.report.anareport.FreePrivateContextKey;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.sysplugin.print.FreeReportPrintStatusMng;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.util.UIUtilities;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.excel.ExcelExpAction;
import com.ufsoft.table.CellsModel;

public class ExcelExportAction extends ExcelExpAction {
	private ActionButton button = null;

	@Override
	public void execute(ActionEvent e) {
		if (!isEnabled()) {
			return;
		}
		super.execute(e);
	}

	public CellsModel getCellsModel() {
		AbsAnaReportDesigner designer = getAnaDesigner();
		return designer == null ? null : ((AnaReportModel) designer
				.getAnaReportModel()).getCellsModel();
	}

	@Override
	public boolean isEnabled() {
		// 只有查询过数据后才可用
		Boolean hasDoQuery = false;
		if (getAnaDesigner() != null && getAnaDesigner().getContext() != null)
			hasDoQuery = (Boolean) this.getAnaDesigner().getContext()
					.getAttribute(FreePrivateContextKey.REPORT_DOQUERY);
		hasDoQuery = hasDoQuery == null ? false : hasDoQuery;
		if (button != null) {
			button.setEnabled(hasDoQuery);
		}
		AbsAnaReportDesigner designer = getAnaDesigner();
		if (designer == null) {
			hasDoQuery = false;
		}

		return hasDoQuery;
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setName("Excel导出");
		descriptor.setExtensionPoints(XPOINT.MENU);
		descriptor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				KeyEvent.CTRL_MASK));
		descriptor.setCode("ExcelExport");
		descriptor.setCompentFactory(new DefaultCompentFactory() {
			@Override
			protected JComponent createMenuBarItem(AbstractAction action) {
				button = new ActionButton(action);
				return button;
			}
		});
		return descriptor;
	}

	public AbsAnaReportDesigner getAnaDesigner() {
		ReportDesigner designer = getEditor();
		if (designer instanceof AbsAnaReportDesigner) {
			return (AbsAnaReportDesigner) designer;
		}
		return null;
	}

}
