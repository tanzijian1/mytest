package nc.ui.tg.report.freereport.published.action;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFileChooser;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.itf.uap.IUAPQueryBS;
import nc.pub.smart.tracedata.TraceDataParam;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.OutsideLogVO;

import com.ufida.iufo.table.exarea.ExtendAreaConstants;
import com.ufida.report.anadesigner.AbsAnaReportDesigner;
import com.ufida.report.anareport.FreeReportContextKey;
import com.ufida.report.free.action.AbstractFreeRepPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys;
import com.ufida.zior.util.UIUtilities;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.ExtDataModel;
import com.ufsoft.table.IExtModel;

public class LogTextExportAction extends AbstractFreeRepPluginAction {

	@Override
	public void execute(ActionEvent event) {
		BufferedWriter out = null;
		try {
			TraceDataParam param = getTraceDataParam();
			if (param == null || param.getRowData() == null) {
				throw new BusinessException("��ѡ��һ����Ϣ֮��,�ٲ�����������");
			}
			String filePath = obtainExcelFilePath();

			Object value = param.getRowData().getData("pk_log");

			IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			OutsideLogVO logVOs = (OutsideLogVO) bs.retrieveByPK(
					OutsideLogVO.class, (String) value);
			File file = new File(filePath);
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "GBK"));
			out.write("������Ϣ:");
			out.write(logVOs.getSrcparm());
			out.newLine();
			out.newLine();
			out.write("������Ϣ:");
			out.newLine();
			out.write(logVOs.getErrmsg());

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			UIUtilities.sendMessage(e.getMessage(), getCellsPane());
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * �����ѡEXCEL�ļ�·��
	 * 
	 * @param ui
	 * @return
	 * @throws Exception
	 */
	private String obtainExcelFilePath() throws Exception {
		JFileChooser chooser = new JFileChooser();
		TXTFileFilter filter = new TXTFileFilter();
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("��־����");

		String fileName = null;
		int returnVal = chooser.showSaveDialog(getEditor());
		// �û�ȡ���������
		if (returnVal == JFileChooser.CANCEL_OPTION)
			return null;

		// ȷ��
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filepath = chooser.getSelectedFile().getPath();
			String filename = chooser.getSelectedFile().getName();
			int nindex = filename.indexOf(".txt");
			if (nindex < 0) {
				filename = filename + ".txt";
				int nindex1 = filepath.lastIndexOf("\\");
				String filepath1 = filepath.substring(0, nindex1 + 1);
				fileName = filepath1 + filename;
			} else {
				fileName = chooser.getSelectedFile().getPath();
			}

		}
		return fileName;
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = new PluginActionDescriptor("��־����");
		desc.setGroupPaths(new String[] { "��־����", "textExp" });
		desc.setExtensionPoints(new PluginKeys.XPOINT[] { PluginKeys.XPOINT.MENU });
		return desc;
	}

	@SuppressWarnings("unchecked")
	private TraceDataParam[] getSelectedTraceDataParams() {
		AbsAnaReportDesigner designer = (AbsAnaReportDesigner) getCurrentView();
		CellsModel dataModel = designer.getAnaReportModel().getDataModel();
		IExtModel extModel = dataModel
				.getExtProp(ExtendAreaConstants.TRACEDATA_REFER_MAP);
		if (extModel == null)
			return null;
		Hashtable<CellPosition, TraceDataParam> map = (Hashtable<CellPosition, TraceDataParam>) ((ExtDataModel) extModel)
				.getValue();
		if (map == null)
			return null;

		ArrayList<TraceDataParam> traceList = new ArrayList<TraceDataParam>();
		CellPosition[] selCells = dataModel.getSelectModel().getSelectedCells();

		for (CellPosition pos : selCells) {
			TraceDataParam param = map.get(pos);
			if (param != null)
				traceList.add(param);
		}
		// }

		if (traceList.size() > 0) {
			IQueryCondition queryCondition = (IQueryCondition) designer
					.getContext().getAttribute(
							FreeReportContextKey.KEY_IQUERYCONDITION);
			if (queryCondition != null)
				traceList.get(0).addParam(
						FreeReportContextKey.KEY_IQUERYCONDITION,
						queryCondition);
			return traceList.toArray(new TraceDataParam[0]);
		}
		return null;
	}

	private TraceDataParam getTraceDataParam() {
		TraceDataParam[] trace = getSelectedTraceDataParams();
		if (trace != null && trace.length > 0) {
			// @edit by ll at 2012-5-25,����10:12:21
			// �����������map�����Ӷ�ѡ���������飬�ṩ��ȫ�����Ϣ������ҵ��������Լ����ж�
			TraceDataParam result = trace[0];
			result.addParam(TraceDataParam.KEY_ALL_TRACEPARAMS, trace);
			return result;
		}
		return null;
	}
}
