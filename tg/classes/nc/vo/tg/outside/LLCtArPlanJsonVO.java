package nc.vo.tg.outside;

import nc.vo.tg.ctar.CtArPlanJsonVO;

public class LLCtArPlanJsonVO extends CtArPlanJsonVO {
	// ��׼�տ�����
	private String name;
	// 2020-09-28-tzj
	private String performancedate; // ��������
	private String money; // ��˰���
	private String notaxmny; // ����˰���
	private String approvetime; // Ӧ�յ�����ʱ��
	private String amountreceivable; // ��תӦ�ս��
	private String payer; // ������
	private String projectprogress; // ���̽���
	private String isdeposit; // �Ƿ��ʱ���
	private String depositterm; // �ʱ�������
	private java.lang.String chargingname;// �շѱ�׼����

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
