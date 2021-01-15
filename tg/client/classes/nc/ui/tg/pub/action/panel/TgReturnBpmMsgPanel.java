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

	private final UIPanel btnPanel = new UIPanel();// 按钮界面
	UIPanel centerPanel;
	private UIButton btnOK = null;
	private UIButton btnCancel = null;
	UILabel lable = new UILabel("退回原因:");
	UITextArea area = null;
	String msg = null;
    int flag;
	public TgReturnBpmMsgPanel() {
		initUI();
	}

	private void initUI() {
		setTitle("退回原因");
		setSize(600, 300);
		add(getCenterPanel(), BorderLayout.CENTER);// 单据卡片
		add(getBtnPanel(), BorderLayout.SOUTH);// 确定与取消按钮
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
	 * 确定按钮
	 * 
	 * @return
	 */
	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton("确定");
		}
		return btnOK;
	}

	/**
	 * 取消按钮
	 * 
	 * @return
	 */
	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton("取消");
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
