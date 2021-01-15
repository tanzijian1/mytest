package nc.vo.tg.outside;

import nc.vo.tg.ctar.CtArPlanJsonVO;

public class LLCtArPlanJsonVO extends CtArPlanJsonVO {
	// 标准收款名称
	private String name;
	// 2020-09-28-tzj
	private String performancedate; // 所属年月
	private String money; // 含税金额
	private String notaxmny; // 不含税金额
	private String approvetime; // 应收单审批时间
	private String amountreceivable; // 已转应收金额
	private String payer; // 付款人
	private String projectprogress; // 工程进度
	private String isdeposit; // 是否质保金
	private String depositterm; // 质保金期限
	private java.lang.String chargingname;// 收费标准名称

	public java.lang.String getChargingname() {
		return chargingname;
	}

	public void setChargingname(java.lang.String chargingname) {
		this.chargingname = chargingname;
	}

	public String getPerformancedate() {
		return performancedate;
	}

	public void setPerformancedate(String performancedate) {
		this.performancedate = performancedate;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getNotaxmny() {
		return notaxmny;
	}

	public void setNotaxmny(String notaxmny) {
		this.notaxmny = notaxmny;
	}

	public String getApprovetime() {
		return approvetime;
	}

	public void setApprovetime(String approvetime) {
		this.approvetime = approvetime;
	}

	public String getAmountreceivable() {
		return amountreceivable;
	}

	public void setAmountreceivable(String amountreceivable) {
		this.amountreceivable = amountreceivable;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getProjectprogress() {
		return projectprogress;
	}

	public void setProjectprogress(String projectprogress) {
		this.projectprogress = projectprogress;
	}

	public String getIsdeposit() {
		return isdeposit;
	}

	public void setIsdeposit(String isdeposit) {
		this.isdeposit = isdeposit;
	}

	public String getDepositterm() {
		return depositterm;
	}

	public void setDepositterm(String depositterm) {
		this.depositterm = depositterm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
