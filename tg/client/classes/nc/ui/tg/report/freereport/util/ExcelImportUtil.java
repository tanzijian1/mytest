package nc.ui.tg.report.freereport.util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import nc.vo.pub.lang.UFDate;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.sysplugin.excel.ExcelExpUtil;
import com.ufsoft.report.sysplugin.excel.ExcelImpUtil;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.PrintSetModel;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.format.CellAlign;
import com.ufsoft.table.format.CellFont;
import com.ufsoft.table.format.CellLines;
import com.ufsoft.table.format.DefaultDataFormat;
import com.ufsoft.table.format.ICellAlign;
import com.ufsoft.table.format.ICellFont;
import com.ufsoft.table.format.IDataFormat;
import com.ufsoft.table.format.IFormat;
import com.ufsoft.table.format.NumberFormat;
import com.ufsoft.table.format.StringFormat;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.print.PrintSet;

public class ExcelImportUtil extends ExcelImpUtil {

	/**
	 * 从Excel文件对象生成CellsModel
	 * 
	 * @param excelFile
	 *            如果文件不存在，则返回null
	 * @return
	 */
	public static CellsModel importCellsModel_TG(File file) {
		if (file == null) {
			throw new RuntimeException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("8001003_0", "08001003-0186")/*
																			 * @res
																			 * "文件名称不能为空！"
																			 */);
		}
		CellsModel cellsModel = null;
		Workbook workBook = null;
		try {
			workBook = getWorkbook(file);
			Sheet sheet = workBook.getSheetAt(0);
			cellsModel = ExcelImportUtil.getCellsModelByExcel_TG(sheet,
					workBook);
		} catch (FileNotFoundException e) {
			AppDebug.debug(e);
		} catch (IOException e) {
			AppDebug.debug(e);
		}

		return cellsModel;
	}

	@SuppressWarnings("deprecation")
	public static CellsModel getCellsModelByExcel_TG(Sheet sheet,
			Workbook workBook) {
		CellsModel cellsModel = CellsModel.getInstance(null, true);
		cellsModel.setEnableEvent(true);
		// 转换单元格
		for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet
				.getLastRowNum(); rowIndex++) {
			Row row = (Row) sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			for (short colIndex = row.getFirstCellNum(); colIndex <= row
					.getLastCellNum(); colIndex++) {
				Cell cell = row.getCell(colIndex);
				if (cell == null)
					continue;
				ExcelImportUtil.convertCell_TG(cell, rowIndex, colIndex,
						cellsModel, sheet, workBook);
			}
		}

		ExcelImportUtil.treateHeightWidth(cellsModel, sheet);

		// 转换PrintSet
		ExcelImportUtil.convertPrintSet(sheet.getPrintSetup(), sheet, workBook,
				cellsModel);
		// 转换组合单元。
		ExcelImportUtil.convertCombinedCell(sheet, cellsModel);
		// 转换其他设置

		return cellsModel;
	}

	static void convertCell_TG(Cell cell, int rowIndex, short colIndex,
			CellsModel cellsModel, Sheet sheet, Workbook workBook) {
		// 转换单元值
		Object value = null;
		IDataFormat dataFormat = DefaultDataFormat.getInstance();
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			value = new Double(cell.getNumericCellValue());
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = HSSFDateUtil.getJavaDate((double) value);
				value = new UFDate(date).toStdString();
				dataFormat = StringFormat.getInstance();
			} else {
				dataFormat = NumberFormat.getInstance();
			}
			// cellsModel.getFormatIfNullNew(CellPosition.getInstance(rowIndex,colIndex)).setCellType(TableConstant.CELLTYPE_NUMBER);
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = new Boolean(cell.getBooleanCellValue());
			dataFormat = StringFormat.getInstance();
			// cellsModel.getFormatIfNullNew(CellPosition.getInstance(rowIndex,colIndex)).setCellType(TableConstant.CELLTYPE_STRING);
			break;

		case HSSFCell.CELL_TYPE_ERROR:
			value = cell.getErrorCellValue() + "";
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			value = null;
			break;

		case HSSFCell.CELL_TYPE_FORMULA:
			if (cell.getRichStringCellValue() == null
					|| ("" + cell.getNumericCellValue()).equals("NaN") == false) {
				value = new Double(cell.getNumericCellValue());
				dataFormat = NumberFormat.getInstance();
				// cellsModel.getFormatIfNullNew(CellPosition.getInstance(rowIndex,colIndex)).setCellType(TableConstant.CELLTYPE_NUMBER);
			} else {
				value = cell.getRichStringCellValue() + "";
			}
			break;
		default:
			value = cell.getRichStringCellValue() + "";
			break;
		}

		if (value instanceof String && value != null) {
			String strValue = "*" + value;
			strValue = strValue.trim();
			strValue = strValue.substring(1);
			value = strValue;
		}

		cellsModel.setCellValue(rowIndex, colIndex, value);
		// 转换格式
		CellStyle cellStyle = cell.getCellStyle();
		if (cellStyle != null) {
			IufoFormat format = (IufoFormat) IufoFormat.getInstance();
			cellsModel.setCellFormat(rowIndex, colIndex, format);
			// 字体
			short backColorIndex = cellStyle.getFillForegroundColor();
			Color backColor = ExcelImportUtil.getColor(backColorIndex);
			Font cellFont = workBook.getFontAt(cellStyle.getFontIndex());
			Color fontColor = ExcelImportUtil.getColor(cellFont.getColor());
			String strFontName = null;
			if (!"Arial".equals(cellFont.getFontName())) {
				strFontName = cellFont.getFontName();
			}
			// int fontSize = (int) (cellFont.getFontHeight() /
			// ExcelExpUtil.FONT_SISE_SCALE_TOEXCEL);
			int fontSize = (int) (cellFont.getFontHeightInPoints() / 72.0 * 25.4 * 4);
			int fontstyle = format.getFont().getFontstyle();
			// 未定义格式时，赋默认值 ，解决没有导入字体粗体显示的问题
			if (fontstyle == TableConstant.UNDEFINED) {
				fontstyle = TableConstant.FS_NORMAL;
			}
			if (cellFont.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD) {
				// 按位操作符
				fontstyle = fontstyle | TableConstant.FS_BOLD;
			}
			if (cellFont.getItalic()) {
				fontstyle = fontstyle | TableConstant.FS_SLOPE;
			}
			if (cellFont.getUnderline() == HSSFFont.U_SINGLE) {
				fontstyle = fontstyle | TableConstant.FS_UNDERLINE;
			}

			ICellFont font = CellFont.getInstance(strFontName, fontstyle,
					fontSize, backColor, fontColor);

			// 对齐方式。
			int halign = ExcelImportUtil.getAlignment(cellStyle.getAlignment(),
					true);
			int valign = ExcelImportUtil.getAlignment(
					cellStyle.getVerticalAlignment(), false);
			int fold = cellStyle.getWrapText() ? 1 : 0;

			ICellAlign align = CellAlign.getInstance(halign, valign, fold,
					TableConstant.UNDEFINED, TableConstant.UNDEFINED);

			// 边框线性和边框颜色
			int[] lineTypes = DefaultSetting.NO_LINES_TYPE;
			Color[] lineColors = DefaultSetting.NO_LINES_COLOR;
			lineTypes[IFormat.TOPLINE] = ExcelImportUtil
					.getBorderType(cellStyle.getBorderTop());
			lineTypes[IFormat.BOTTOMLINE] = ExcelImportUtil
					.getBorderType(cellStyle.getBorderBottom());
			lineTypes[IFormat.LEFTLINE] = ExcelImportUtil
					.getBorderType(cellStyle.getBorderLeft());
			lineTypes[IFormat.RIGHTLINE] = ExcelImportUtil
					.getBorderType(cellStyle.getBorderRight());

			Color color = ExcelImportUtil.getColor(cellStyle
					.getTopBorderColor());
			if (color != null) {
				lineColors[IFormat.TOPLINE] = color;
			}
			color = ExcelImportUtil.getColor(cellStyle.getBottomBorderColor());
			if (color != null) {
				lineColors[IFormat.BOTTOMLINE] = color;
			}
			color = ExcelImportUtil.getColor(cellStyle.getLeftBorderColor());
			if (color != null) {
				lineColors[IFormat.LEFTLINE] = color;
			}
			color = ExcelImportUtil.getColor(cellStyle.getRightBorderColor());
			if (color != null) {
				lineColors[IFormat.RIGHTLINE] = color;
			}
			// 其他格式
			IFormat newFormat = IufoFormat.getInstance(dataFormat, font, align,
					CellLines.getInstance(lineTypes, lineColors));
			cellsModel.setCellFormat(rowIndex, colIndex, newFormat);
		}
	}

	static int getAlignment(short alignment, boolean bHor) {
		if (bHor) {
			switch (alignment) {
			case HSSFCellStyle.ALIGN_CENTER:
				return TableConstant.HOR_CENTER;
			case HSSFCellStyle.ALIGN_LEFT:
				return TableConstant.HOR_LEFT;
			case HSSFCellStyle.ALIGN_RIGHT:
				return TableConstant.HOR_RIGHT;
			default:
				return TableConstant.UNDEFINED;
			}
		} else {
			switch (alignment) {
			case HSSFCellStyle.VERTICAL_CENTER:
				return TableConstant.VER_CENTER;
			case HSSFCellStyle.VERTICAL_TOP:
				return TableConstant.VER_UP;
			case HSSFCellStyle.VERTICAL_BOTTOM:
				return TableConstant.VER_DOWN;
			default:
				return TableConstant.UNDEFINED;
			}
		}
	}

	static short getBorderType(int lineType) {
		switch (lineType) {
		case HSSFCellStyle.BORDER_DASHED:
			return TableConstant.L_DASH;
		case HSSFCellStyle.BORDER_DASH_DOT:
			return TableConstant.L_DASHDOT;
		case HSSFCellStyle.BORDER_DOTTED:
			return TableConstant.L_DOT;
		case HSSFCellStyle.BORDER_NONE:
			return TableConstant.L_NULL;
		case HSSFCellStyle.BORDER_THIN:
			return TableConstant.L_SOLID1;
		case HSSFCellStyle.BORDER_MEDIUM:
			return TableConstant.L_SOLID2;
		case HSSFCellStyle.BORDER_THICK:
			return TableConstant.L_SOLID3;

		default:
			return TableConstant.UNDEFINED;
		}
	}

	static Color getColor(short colorIndex) {
		if (colorIndex >= 0
				&& colorIndex < ExcelExpUtil.s_colorIndexHash.size()) {
			HSSFColor excelColor = (HSSFColor) ExcelExpUtil.s_colorIndexHash
					.get(Integer.valueOf(colorIndex));
			if (excelColor == null) {
				return null;
			}
			short[] rgb = excelColor.getTriplet();
			return new Color(rgb[0], rgb[1], rgb[2]);
		} else {
			return null;
		}

	}

	@SuppressWarnings("deprecation")
	static void convertCombinedCell(Sheet sheet, CellsModel cellsModel) {
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress region = sheet.getMergedRegion(i);
			AreaPosition areaPos = AreaPosition.getInstance(
					region.getFirstRow(), region.getFirstColumn(),
					region.getLastColumn() - region.getFirstColumn() + 1,
					region.getLastRow() - region.getFirstRow() + 1);
			cellsModel.combineCell(areaPos);
		}
	}

	static void convertPrintSet(PrintSetup excelPs, Sheet sheet,
			Workbook workBook, CellsModel cellsModel) {
		PrintSet ps = PrintSetModel.getInstance(cellsModel).getPrintSet();
		ps.setViewScale(excelPs.getScale());

	}

	/**
	 * add by wangyga 2008-10-16 处理行高列宽
	 * 
	 * @param cellsModel
	 * @param sheet
	 */
	@SuppressWarnings("deprecation")
	static void treateHeightWidth(CellsModel cellsModel, Sheet sheet) {
		if (cellsModel == null || sheet == null) {
			return;
		}
		HeaderModel rowHerderModel = cellsModel.getRowHeaderModel();
		HeaderModel columnHerderModel = cellsModel.getColumnHeaderModel();
		int iColNum = 0;
		for (int rowIndex = sheet.getFirstRowNum(); rowIndex < sheet
				.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row == null)
				continue;
			// short height = row.getHeight();
			float height = row.getHeightInPoints();
			// 隐藏行处理
			if (row.getZeroHeight()) {
				height = TableStyle.MINHEADER;
			}
			/*
			 * if (height < 4) height = 0;
			 */
			Header header = rowHerderModel.getHeader(rowIndex);
			// header = Header.getInstance(header.getValue(), (int) ((((double)
			// height) * 4) / (20 * 3 * 0.9346)), header.getFormat(), true);
			header = Header.getInstance(header.getValue(),
					(int) (height / 72.0 * 25.4 * 4), header.getFormat(), true);
			rowHerderModel.setHeader(rowIndex, header);
			int singleColNum = row.getLastCellNum() + 1;
			if (singleColNum > iColNum)
				iColNum = singleColNum;
		}

		for (short colIndex = 0; colIndex < iColNum; colIndex++) {
			int width = sheet.getColumnWidth(colIndex);
			if (width < 4)
				width = 0;
			Header header = columnHerderModel.getHeader(colIndex);
			if (header == null)
				continue;
			int iNewWidth = (int) (((double) width * 7) / (256 * 0.892));
			if (iNewWidth < TableStyle.MINHEADER)
				iNewWidth = TableStyle.MINHEADER;
			else if (iNewWidth > TableStyle.MAXHEADER)
				iNewWidth = TableStyle.MAXHEADER;
			header = Header.getInstance(header.getValue(), iNewWidth,
					header.getFormat(), true);
			columnHerderModel.setHeader(colIndex, header);
		}
	}
}
