package nc.vo.tg.outside;

import java.io.Serializable;

public class LLArAddedServicesJsonBVO implements Serializable {

	private java.lang.String servicecontent;// ��չ��ֵ��������
	private java.lang.String chargingname; // �շѱ�׼����
	private java.lang.String serviceperiod; // ��������
	private java.lang.String money; // ��˰���
	private java.lang.String paytype; // ���ʽ

	public java.lang.String getServicecontent() {
		return servicecontent;
	}

	public void setServicecontent(java.lang.String servicecontent) {
		this.servicecontent = servicecontent;
	}

	public java.lang.String getChargingname() {
		return chargingname;
	}

	public void setChargingname(java.lang.String chargingname) {
		this.chargingname = chargingname;
	}

	public java.lang.String getServiceperiod() {
		return serviceperiod;
	}

	public void setServiceperiod(java.lang.String serviceperiod) {
		this.serviceperiod = serviceperiod;
	}

	public java.lang.String getMoney() {
		return money;
	}

	public void setMoney(java.lang.String money) {
		this.money = money;
	}

	public java.lang.String getPaytype() {
		return paytype;
	}

	public void setPaytype(java.lang.String paytype) {
		this.paytype = paytype;
	}

}
