package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * �¶����ʼƻ����ܱ�
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����11:24:08
 */
public class FinMonthlyPlanTotalVO implements Serializable, Cloneable {
	String month;// �·�
	UFDouble loanplantotal_amount;// �ƻ��ſ�ϼ�
	UFDouble loanplantotaladj_amount;// �ƻ��ſ�ϼ�(������)
	UFDouble loanactualtotal_amount;// ʵ�ʷſ�ϼ�
	UFDouble loanplan_ratio;// �¶����ʼƻ������
	UFDouble loanplanadj_ratio;// �¶����ʼƻ�����ʣ���������
	UFDouble loanmonactualtotal_amount;// �¶��ۼ�ʵ�ʷſ�
	UFDouble loanmonplantotal_amount;// �¶��ۼƼƻ��ſ�
	UFDouble loanmonplantotaladj_amount;// �¶��ۼƼƻ��ſ��������
	UFDouble loanmonplan_ratio;// �¶����ʼƻ��ۼ������
	UFDouble loanmonplanadj_ratio;// �¶����ʼƻ��ۼ������(������)

	
	
	public UFDouble getLoanplantotaladj_amount() {
		return loanplantotaladj_amount;
	}

	public void setLoanplantotaladj_amount(UFDouble loanplantotaladj_amount) {
		this.loanplantotaladj_amount = loanplantotaladj_amount;
	}

	public UFDouble getLoanplanadj_ratio() {
		return loanplanadj_ratio;
	}

	public void setLoanplanadj_ratio(UFDouble loanplanadj_ratio) {
		this.loanplanadj_ratio = loanplanadj_ratio;
	}

	public UFDouble getLoanmonplantotaladj_amount() {
		return loanmonplantotaladj_amount;
	}

	public void setLoanmonplantotaladj_amount(UFDouble loanmonplantotaladj_amount) {
		this.loanmonplantotaladj_amount = loanmonplantotaladj_amount;
	}

	public UFDouble getLoanmonplanadj_ratio() {
		return loanmonplanadj_ratio;
	}

	public void setLoanmonplanadj_ratio(UFDouble loanmonplanadj_ratio) {
		this.loanmonplanadj_ratio = loanmonplanadj_ratio;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public UFDouble getLoanplantotal_amount() {
		return loanplantotal_amount;
	}

	public void setLoanplantotal_amount(UFDouble loanplantotal_amount) {
		this.loanplantotal_amount = loanplantotal_amount;
	}

	public UFDouble getLoanactualtotal_amount() {
		return loanactualtotal_amount;
	}

	public void setLoanactualtotal_amount(UFDouble loanactualtotal_amount) {
		this.loanactualtotal_amount = loanactualtotal_amount;
	}

	public UFDouble getLoanplan_ratio() {
		return loanplan_ratio;
	}

	public void setLoanplan_ratio(UFDouble loanplan_ratio) {
		this.loanplan_ratio = loanplan_ratio;
	}

	public UFDouble getLoanmonactualtotal_amount() {
		return loanmonactualtotal_amount;
	}

	public void setLoanmonactualtotal_amount(UFDouble loanmonactualtotal_amount) {
		this.loanmonactualtotal_amount = loanmonactualtotal_amount;
	}

	public UFDouble getLoanmonplantotal_amount() {
		return loanmonplantotal_amount;
	}

	public void setLoanmonplantotal_amount(UFDouble loanmonplantotal_amount) {
		this.loanmonplantotal_amount = loanmonplantotal_amount;
	}

	public UFDouble getLoanmonplan_ratio() {
		return loanmonplan_ratio;
	}

	public void setLoanmonplan_ratio(UFDouble loanmonplan_ratio) {
		this.loanmonplan_ratio = loanmonplan_ratio;
	}

}
