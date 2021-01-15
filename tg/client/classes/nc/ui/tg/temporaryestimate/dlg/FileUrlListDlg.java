package nc.ui.tg.temporaryestimate.dlg;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItemHyperlinkEvent;
import nc.ui.pub.bill.BillItemHyperlinkListener;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.linksrmenclosure.LinkSRMEnclosureVO;

public class FileUrlListDlg extends UIDialog implements
		BillItemHyperlinkListener {
	private BillCardPanel billCardPanel = null;// �������
	private final UIPanel btnPanel = new UIPanel();// ��ť����

	public FileUrlListDlg(String pk_key) {
		super();
		init();
		initData(pk_key);
	}

	private void initData(String pk_key) {
		// SuperVO[] vos = new SuperVO[0];
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Collection vos = null;
		try {
			vos = bs.retrieveByClause(LinkSRMEnclosureVO.class,
					"isnull(dr,0)=0 and srmno = '" + pk_key + "' and srmtype = '����'");
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		LinkSRMEnclosureVO[] vo = (LinkSRMEnclosureVO[]) vos
				.toArray(new LinkSRMEnclosureVO[0]);

		getBillCardPanel().getBillModel().setBodyDataVO(vo);

	}

	private void init() {
		setTitle("����SRM����");
		setSize(800, 600);
		add(getBillCardPanel(), "Center");
		initListener();
	}

	private void initListener() {
		getBillCardPanel().getBodyItem("att_name")
				.addBillItemHyperlinkListener(this);
	}

	/**
	 * ��ÿ�Ƭ��Ϣ
	 * 
	 * @return
	 */
	private BillCardPanel getBillCardPanel() {
		if (billCardPanel == null) {
			try {
				billCardPanel = new BillCardPanel();
				billCardPanel.loadTemplet("1001ZZ100000001RBRVB");// �ϲ��ͻ�����ģ��
			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return billCardPanel;
	}

	public int showDlg() {
		// �¾��ʹ��Ļ��˸
		// if (getTopFrame() == null && getTopParent() != null)
		// getTopParent().setEnabled(false);
		//
		setModal(true);

		if (!isShowing()) {
			setLocationRelativeTo(null);
			show();
		}
		return getResult();
	}

	public void hyperlink(BillItemHyperlinkEvent event) {
		

		Desktop desktop = Desktop.getDesktop(); 
		
		int row = event.getRow();
		String url = (String) getBillCardPanel().getBodyValueAt(row,
				"att_address");
		URI u;
		try {
			u = new URI(url);
			desktop.browse(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
