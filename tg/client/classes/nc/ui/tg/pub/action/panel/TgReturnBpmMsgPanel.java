package nc.ui.tg.pub.action.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.bill.table.BillTableTextAreaRenderer;

public class TgReturnBpmMsgPanel extends UIDialog implements ActionListener {

	private final UIPanel btnPanel = new UIPanel();// ��ť����
	UIPanel centerPanel;
	private UIButton btnOK = null;
	private UIButton btnCancel = null;
	UILabel lable = new UILabel("�˻�ԭ��:");
	UITextArea area = null;
	String msg = null;
    int flag;
	public TgReturnBpmMsgPanel() {
		initUI();
	}

	private void initUI() {
		setTitle("�˻�ԭ��");
		setSize(600, 300);
		add(getCenterPanel(), BorderLayout.CENTER);// ���ݿ�Ƭ
		add(getBtnPanel(), BorderLayout.SOUTH);// ȷ����ȡ����ť
		initListener();
	}

	public UIPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new UIPanel();
//			centerPanel.add(lable);
			JScrollPane scroll = new JScrollPane(getArea());
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			centerPanel.add(scroll);

		}

		return centerPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			setMsg(getArea().getText());
			setFlag(1);
		} else {
			setMsg(null);
		}
		dispose();
	}

	private void initListener() {
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);
	}

	private UIPanel getBtnPanel() {
		btnPanel.add(getBtnOK());
		btnPanel.add(getBtnCancel());
		return btnPanel;
	}

	/**
	 * ȷ����ť
	 * 
	 * @return
	 */
	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton("ȷ��");
		}
		return btnOK;
	}

	/**
	 * ȡ����ť
	 * 
	 * @return
	 */
	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton("ȡ��");
		}
		return btnCancel;
	}

	public UITextArea getArea() {
		if (area == null) {
			area = new BillTableTextAreaRenderer();
			area.setMaxLength(2000);
			area.setRows(14);
			area.setColumns(53);
		}
		return area;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
