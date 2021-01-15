package nc.vo.tg.outside;

import java.io.Serializable;

public class LLArPerformanceJsonBVO implements Serializable {

	private java.lang.String performancedate;// 日期xx年xx月
	private java.lang.String money;// 含税金额
	private java.lang.String notaxmny;// 不含税金额
	private java.lang.String approvestatus;// 共享审核应收单状态

	public java.lang.String getPerformancedate() {
		return performancedate;
	}

	public void setPerformancedate(java.lang.String performancedate) {
		this.performancedate = performancedate;
	}

	public java.lang.String getMoney() {
		return money;
	}

	public void setMoney(java.lang.String money) {
		this.money = money;
	}

	public java.lang.String getNotaxmny() {
		return notaxmny;
	}

	public void setNotaxmny(java.lang.String notaxmny) {
		this.notaxmny = notaxmny;
	}

	public java.lang.String getApprovestatus() {
		return approvestatus;
	}

	public void setApprovestatus(java.lang.String approvestatus) {
		this.approvestatus = approvestatus;
	}

}
