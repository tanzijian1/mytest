package nc.ui.tg.rzreportbi.ace.view;

import java.awt.Desktop;
import java.net.URI;

import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class ShowBillListView extends nc.ui.pubapp.uif2app.view.ShowUpableBillListView{

	@Override
	public void initUI() {
		// TODO 自动生成的方法存根
		super.initUI();
//		String openUrl = "http://10.1.9.159/Reports/powerbi/%E6%98%8E%E7%BB%86%E6%8A%A5%E8%A1%A8/%E5%BE%85%E9%AA%8C%E8%AF%81%E6%8A%A5%E8%A1%A8%EF%BC%88%E6%9C%AA%E7%BB%8F%E7%A1%AE%E8%AE%A4%EF%BC%89/%E8%B5%84%E9%87%91%E4%B8%AD%E5%BF%83/%E8%B5%84%E9%87%91%E4%B8%AD%E5%BF%83%E6%8A%A5%E8%A1%A8";
		try {
			String openUrl = OutsideUtils.getOutsideInfo("BI01");
			Desktop.getDesktop().browse(new URI(openUrl));
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}
	
}
