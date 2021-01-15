package nc.ui.tg.paymentrequest.ace;

import java.awt.BorderLayout;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pubapp.bill.BillCardPanel;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

public class InvalidMoneyPanel extends UIDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4357065200850342122L;
	UITextField textfiled;
	private UIPanel centerpanel;
	public UITextField getTextfiled() {
		if(textfiled==null){
			textfiled=new UITextField();
			textfiled.setBounds(840, 430, 200, 50);
		}
		return textfiled;
	}
	public void setTextfiled(UITextField textfiled) {
		this.textfiled = textfiled;
	}
	public UIPanel getCenterpanel() {
		if(centerpanel==null){
			centerpanel=new UIPanel();
			centerpanel.setSize(400, 50);
			centerpanel.add(getTextfiled());
		}
		return centerpanel;
	}
	public void setCenterpanel(UIPanel centerpanel) {
		this.centerpanel = centerpanel;
	}
	private UIPanel buttonpanel=new UIPanel();//按钮界面
	private UIButton okbutton;//确定按钮
	private UIButton cancelbutton;//取消按钮
	public InvalidMoneyPanel(AggPayrequest aggvo){
		setTitle("作废金额");
		setBounds(800, 400, 400, 100);
	    add(getButtonpanel(),BorderLayout.SOUTH);
	    add(getCenterpanel(),BorderLayout.CENTER);
	}
	public UIPanel getButtonpanel() {
		buttonpanel.add(getOkbutton());
		buttonpanel.add(getCancelbutton());
		return buttonpanel;
	}
	public void setButtonpanel(UIPanel buttonpanel) {
		this.buttonpanel = buttonpanel;
	}
	public UIButton getOkbutton() {
		if(okbutton==null){
			okbutton=new UIButton();
			okbutton.setBounds(50, 510, 70, 30);
			okbutton.setText("确定");
			okbutton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onok();
				}
			});
		}
		return okbutton;
	}
	private void onok(){
		this.closeOK();
	}
	public void setOkbutton(UIButton okbutton) {
		this.okbutton = okbutton;
	}
	public UIButton getCancelbutton() {
		if(cancelbutton==null){
			cancelbutton=new UIButton();
			cancelbutton.setBounds(120, 510, 70, 30);
			cancelbutton.setText("取消");
			cancelbutton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					oncancel();
				}
			});
		}
		return cancelbutton;
	}
	private void oncancel(){
		this.textfiled.setValue(null);
		this.closeCancel();
	}
	public void setCancelbutton(UIButton cancelbutton) {
		this.cancelbutton = cancelbutton;
	}
	
}
