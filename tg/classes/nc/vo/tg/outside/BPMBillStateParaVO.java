package nc.vo.tg.outside;

import java.io.Serializable;

public class BPMBillStateParaVO implements Serializable {

	String billtypeName;// ִ�е���
	String billstate;// ִ��״̬
	String bpmid;// BPM��Ӧ��������
	String operator;// ����Ա
	String data;//��д����
	
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getBilltypeName() {
		return billtypeName;
	}
	public void setBilltypeName(String billtypeName) {
		this.billtypeName = billtypeName;
	}
	public String getBillstate() {
		return billstate;
	}
	public void setBillstate(String billstate) {
		this.billstate = billstate;
	}
	public String getBpmid() {
		return bpmid;
	}
	public void setBpmid(String bpmid) {
		this.bpmid = bpmid;
	}
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
	
	
}
