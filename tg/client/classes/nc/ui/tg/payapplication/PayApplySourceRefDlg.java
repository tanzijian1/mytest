package nc.ui.tg.payapplication;

import java.awt.Container;

import nc.ui.pub.pf.BillSourceVar;
import nc.ui.pubapp.billref.src.view.SourceRefDlg;

public class PayApplySourceRefDlg extends SourceRefDlg{

	public PayApplySourceRefDlg(Container parent, BillSourceVar bsVar) {
		super(parent, bsVar);
	}
	
	@Override
	  public String getRefBillInfoBeanPath() {
	    return "nc/ui/tg/payapplication/PayApplyrefinfo.xml";
	  }

}
