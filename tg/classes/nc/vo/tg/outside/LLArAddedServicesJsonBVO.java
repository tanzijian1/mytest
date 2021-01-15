package nc.vo.tg.outside;

import java.io.Serializable;

public class LLArAddedServicesJsonBVO implements Serializable {

	private java.lang.String servicecontent;// 开展增值服务内容
	private java.lang.String chargingname; // 收费标准名称
	private java.lang.String serviceperiod; // 服务期限
	private java.lang.String money; // 含税金额
	private java.lang.String paytype; // 付款方式

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
