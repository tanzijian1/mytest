package nc.ui.tg.report.freereport.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.ActionButton;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.tg.report.freereport.util.ExcelImportUtil;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.mortgagelist.MortgageListDetailedVO;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import uap.iweb.log.Logger;

import com.ufida.report.anadesigner.AbsAnaReportDesigner;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.util.UIUtilities;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.excel.ExcelImpAction;
import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.format.TableConstant;

public class ExcelImportAction extends ExcelImpAction {
	private ActionButton button = null;

	protected void dealAfterImpExcel(CellsModel cellsModel) {
		AreaFormulaModel formulaModel = AreaFormulaModel
				.getInstance(cellsModel);
		formulaModel.setAreaFmlExecutor(new AreaFmlExecutor(cellsModel));
		try {
			onHandleData(cellsModel);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}

	}

	@Override
	public void execute(ActionEvent e) {
		if (checkIsInit()) {
			UIUtilities.sendMessage("抵押物清单已初始化,不能重复导入！", getCellsPane());
			return;
		}
		super.execute(e);
	}

	private boolean checkIsInit() {
		String sql = "select count(1) from tgrz_rep_mortgagelist where dr = 0";
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object value = null;
		try {
			value = bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			return false;
		}
		if ("0".equals(String.valueOf(value))) {
			return true;
		}

		return false;
	}

	/**
	 * 数据导入
	 * 
	 * @param cellsModel
	 * @throws BusinessException
	 */
	private void onHandleData(CellsModel cellsModel) throws BusinessException {
		if (cellsModel.getRowNum() < 8) {
			return;
		}
		Map<String, MortgageListDetailedVO> value = new HashMap<String, MortgageListDetailedVO>();
		List<List<com.ufsoft.table.Cell>> list = cellsModel.getCells();
		for (int i$ = 7; i$ < list.size(); i$++) {
			List<com.ufsoft.table.Cell> clist = list.get(i$);
			String projectcode = clist.get(0) == null ? null : (String) clist
					.get(0).getValue();// 项目编码
			String periodizationname = clist.get(2) == null ? null
					: (String) clist.get(2).getValue();// 项目分期
			MortgageListDetailedVO vo = new MortgageListDetailedVO();
			vo.setPk_group(AppContext.getInstance().getPkGroup());
			vo.setPk_org(AppContext.getInstance().getPkGroup());
			vo.setCreator(AppContext.getInstance().getPkUser());
			vo.setCreationtime(new UFDateTime(AppContext.getInstance()
					.getBusiDate().toString()));
			// 土地
			vo.setAttributeValue("land_state", clist.get(3) == null ? null
					: clist.get(3).getValue());//
			vo.setAttributeValue("land_date",
					convertDateCellValue(clist.get(4)));//
			vo.setAttributeValue("land_undate",
					convertDateCellValue(clist.get(5)));//
			vo.setAttributeValue("land_note", clist.get(6) == null ? null
					: clist.get(6).getValue());//

			// 在建工程
			vo.setAttributeValue("engineering_state",
					clist.get(7) == null ? null : clist.get(7).getValue());//
			vo.setAttributeValue("engineering_date",
					convertDateCellValue(clist.get(8)));//
			vo.setAttributeValue("engineering_undate",
					convertDateCellValue(clist.get(9)));//
			vo.setAttributeValue("engineering_note",
					clist.get(10) == null ? null : clist.get(10).getValue());//
			// 物业
			vo.setAttributeValue("property_detailed",
					clist.get(11) == null ? null : clist.get(11).getValue());//
			vo.setAttributeValue("property_date",
					convertDateCellValue(clist.get(12)));//
			vo.setAttributeValue("property_undate",
					convertDateCellValue(clist.get(13)));//
			vo.setAttributeValue("property_note", list.get(14) == null ? null
					: clist.get(14).getValue());//
			vo.setAttributeValue("dr", new Integer(0));
			vo.setAttributeValue("ts", new UFDate().toString());//

			value.put(projectcode + "-" + periodizationname, vo);

		}

		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);

		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append("select tgrz_projectdata.pk_projectdata")
				.append(",tgrz_projectdata.code||'-'||tgrz_projectdata_c.periodizationname groupid")
				.append(", tgrz_projectdata.name projectname")
				.append(",tgrz_projectdata_c.pk_projectdata_c")
				.append(",tgrz_rep_mortgagelist.pk_rep_mortgagelist")
				.append(" from tgrz_projectdata ")
				.append(" inner join tgrz_projectdata_c on tgrz_projectdata.pk_projectdata = tgrz_projectdata_c.pk_projectdata ")
				.append(" left join tgrz_rep_mortgagelist on tgrz_projectdata_c.pk_projectdata_c = tgrz_rep_mortgagelist.pk_periodization ")
				.append("  where tgrz_projectdata.dr = 0 and tgrz_projectdata_c.dr = 0 ")
				.append(" and "
						+ SQLUtil
								.buildSqlForIn(
										"(tgrz_projectdata.code||'-'||tgrz_projectdata_c.periodizationname)",
										value.keySet().toArray(new String[0])));

		List<Map<String, String>> projectlist = (List<Map<String, String>>) bs
				.executeQuery(sqlBuff.toString(), new MapListProcessor());
		Map<String, Map<String, String>> projectMap = new HashMap<String, Map<String, String>>();
		if (projectlist != null && projectlist.size() > 0) {
			for (Map<String, String> map : projectlist) {
				projectMap.put(map.get("groupid"), map);
			}

		}

		List<MortgageListDetailedVO> insertVOs = new ArrayList<MortgageListDetailedVO>();
		List<MortgageListDetailedVO> updateVOs = new ArrayList<MortgageListDetailedVO>();
		for (String key : projectMap.keySet()) {
			MortgageListDetailedVO vo = value.get(key);
			if (vo == null) {
				continue;
			}

			Map<String, String> map = projectMap.get(key);
			vo.setPk_periodization(map.get("pk_projectdata_c"));
			vo.setPk_project(map.get("pk_projectdata"));
			if (map.get("pk_rep_mortgagelist") != null) {
				vo.setPk_rep_mortgagelist(map.get("pk_rep_mortgagelist"));
				updateVOs.add(vo);
			} else {
				insertVOs.add(vo);
			}
		}

		IVOPersistence per = NCLocator.getInstance().lookup(
				IVOPersistence.class);
		if (updateVOs.size() > 0) {
			per.updateVOList(updateVOs);
		}

		if (insertVOs.size() > 0) {
			per.insertVOList(insertVOs);
		}

	}

	private Object convertDateCellValue(Cell cell) {
		String value = null;
		if (cell != null) {
			if (cell.getValue() != null) {
				if (cell.getFormat().getCellType() == TableConstant.CELLTYPE_NUMBER) {
					Date date = HSSFDateUtil.getJavaDate((double) cell
							.getValue());
					value = new UFDate(date).toStdString();
				} else {
					value = (String) cell.getValue();
				}

			}
		}
		return value;
	}

	@Override
	public boolean isEnabled() {
		Boolean hasDoQuery = true;
		AbsAnaReportDesigner designer = getAnaDesigner();
		if (designer == null) {
			hasDoQuery = false;
		}
		if (button != null) {
			button.setEnabled(hasDoQuery);
		}

		return hasDoQuery;
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setName("Excel导入");
		descriptor.setExtensionPoints(XPOINT.MENU);
		descriptor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				KeyEvent.CTRL_MASK));
		descriptor.setCode("ExcelImport");
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

	/**
	 * liuchun 20110510 抽出此方法，企业报表中复写此方法将workBook中公式封装到报表模型中
	 * 
	 * @create by liuchuna at 2011-5-6,上午09:23:44
	 * 
	 * @param file
	 * @return
	 */
	protected CellsModel getCellsModel(File file) {
		CellsModel cellsModel = ExcelImportUtil.importCellsModel_TG(file);
		return cellsModel;
	}
}
