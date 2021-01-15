package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 月度融资计划汇总表
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 上午11:24:08
 */
public class FinMonthlyPlanTotalVO implements Serializable, Cloneable {
	String month;// 月份
	UFDouble loanplantotal_amount;// 计划放款合计
	UFDouble loanplantotaladj_amount;// 计划放款合计(调整数)
	UFDouble loanactualtotal_amount;// 实际放款合计
	UFDouble loanplan_ratio;// 月度融资计划完成率
	UFDouble loanplanadj_ratio;// 月度融资计划完成率（调整数）
	UFDouble loanmonactualtotal_amount;// 月度累计实际放款
	UFDouble loanmonplantotal_amount;// 月度累计计划放款
	UFDouble loanmonplantotaladj_amount;// 月度累计计划放款（调整数）
	UFDouble loanmonplan_ratio;// 月度融资计划累计完成率
	UFDouble loanmonplanadj_ratio;// 月度融资计划累计完成率(调整数)

	
	
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
