package nc.ui.tg.rzreportbi.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;

import nc.itf.tg.outside.OutsideUtils;
import nc.ui.uif2.NCAction;

public class LinkQueryReport extends NCAction{
	
	public LinkQueryReport() {
		super();
		setCode("linkQueryReport");
		setBtnName("查看报表");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
//		String openUrl = "http://times-nbi.timesgroup.cn/Reports/powerbi/%E4%B8%93%E9%A2%98%E6%8A%A5%E8%A1%A8/%E8%B5%84%E9%87%91%E4%B8%93%E9%A2%98/%E8%B5%84%E9%87%91%E4%B8%AD%E5%BF%83%E6%8A%A5%E8%A1%A8";
		String openUrl = OutsideUtils.getOutsideInfo("BI01");
		Desktop.getDesktop().browse(new URI(openUrl));
	}

}
