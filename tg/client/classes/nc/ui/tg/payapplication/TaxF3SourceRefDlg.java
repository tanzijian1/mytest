package nc.ui.tg.payapplication;

import java.awt.Container;

import nc.ui.pub.pf.BillSourceVar;
import nc.ui.pubapp.billref.src.view.SourceRefDlg;

public class TaxF3SourceRefDlg extends SourceRefDlg{

	public TaxF3SourceRefDlg(Container parent, BillSourceVar bsVar) {
		super(parent, bsVar);
	}
	
	@Override
	  public String getRefBillInfoBeanPath() {
	    return "nc/ui/tg/payapplication/taxf3refinfo.xml";
	  }
}
