package nc.vo.tg.rz.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;

/**
 * 月度融资计划表
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 上午11:24:08
 */
public class FinMonthlyPlanVO extends SuperVO {
	String pk_finmonthlyplan;
	String pk_project;// 项目ID
	String projectcode;// 项目编码
	String projectname;// 项目名称
	String pk_periodization;// 分期主键
	String periodizationname;// 分期名称
	String pk_fintype;// 融资类型
	String fintypecode;// 融资类型编码
	String fintypename;// 融资类型名称
	UFDouble fin_amount;// 融资金额
	UFDouble finyearplan_amount;// 年度计划融资额 
	UFDouble finyearplanadj_amount;///年度计划融资额（调整数）
	UFDouble loanyear_amount;// 年度放款金额
	UFDouble loanyeartotal_amount;// 累计放款金额
	String loandate;// 放款时间
	UFDouble notloan_amount;// 未放款金额
	String note; // 备注
	String fourcardsdate;// 四证齐全时间
	String landpaydate;// 地价完成支付时间
	String threecardsdate;// 三证齐全时间
	String paymentdate;// 对价40%支付时间
	String monthtype; // 月度类型
	UFDouble loanmonplan_amount; // 月度计划放款
	UFDouble loanmonactual_amount; // 月度实际放款
	UFDouble loanmonadj_amount;// 月度调整放款
	UFDouble initplan_amount; // 年度期初计划放款
	UFDouble initactual_amount; // 年度期初实际放款
	UFDouble initadj_amount;// 年度期初调整放款
	UFDouble loanmondiff_amount;// 月度累计差额
	String msg;// 说明

	
	
	public UFDouble getFinyearplanadj_amount() {
		return finyearplanadj_amount;
	}

	public void setFinyearplanadj_amount(UFDouble finyearplanadj_amount) {
		this.finyearplanadj_amount = finyearplanadj_amount;
	}

	public String getPk_project() {
		return pk_project;
	}

	public void setPk_project(String pk_project) {
		this.pk_project = pk_project;
	}

	public String getProjectcode() {
		return projectcode;
	}

	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getPk_periodization() {
		return pk_periodization;
	}

	public void setPk_periodization(String pk_periodization) {
		this.pk_periodization = pk_periodization;
	}

	public String getPeriodizationname() {
		return periodizationname;
	}

	public void setPeriodizationname(String periodizationname) {
		this.periodizationname = periodizationname;
	}

	public String getPk_fintype() {
		return pk_fintype;
	}

	public void setPk_fintype(String pk_fintype) {
		this.pk_fintype = pk_fintype;
	}

	public String getFintypecode() {
		return fintypecode;
	}

	public void setFintypecode(String fintypecode) {
		this.fintypecode = fintypecode;
	}

	public String getFintypename() {
		return fintypename;
	}

	public void setFintypename(String fintypename) {
		this.fintypename = fintypename;
	}

	public UFDouble getFin_amount() {
		return fin_amount;
	}

	public void setFin_amount(UFDouble fin_amount) {
		this.fin_amount = fin_amount;
	}

	public UFDouble getFinyearplan_amount() {
		return finyearplan_amount;
	}

	public void setFinyearplan_amount(UFDouble finyearplan_amount) {
		this.finyearplan_amount = finyearplan_amount;
	}

	public UFDouble getLoanyear_amount() {
		return loanyear_amount;
	}

	public void setLoanyear_amount(UFDouble loanyear_amount) {
		this.loanyear_amount = loanyear_amount;
	}

	public UFDouble getLoanyeartotal_amount() {
		return loanyeartotal_amount;
	}

	public void setLoanyeartotal_amount(UFDouble loanyeartotal_amount) {
		this.loanyeartotal_amount = loanyeartotal_amount;
	}

	public String getLoandate() {
		return loandate;
	}

	public void setLoandate(String loandate) {
		this.loandate = loandate;
	}

	public UFDouble getNotloan_amount() {
		return notloan_amount;
	}

	public void setNotloan_amount(UFDouble notloan_amount) {
		this.notloan_amount = notloan_amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFourcardsdate() {
		return fourcardsdate;
	}

	public void setFourcardsdate(String fourcardsdate) {
		this.fourcardsdate = fourcardsdate;
	}

	public UFDouble getLoanmonplan_amount() {
		return loanmonplan_amount;
	}

	public void setLoanmonplan_amount(UFDouble loanmonplan_amount) {
		this.loanmonplan_amount = loanmonplan_amount;
	}

	public UFDouble getLoanmonactual_amount() {
		return loanmonactual_amount;
	}

	public void setLoanmonactual_amount(UFDouble loanmonactual_amount) {
		this.loanmonactual_amount = loanmonactual_amount;
	}

	public String getMonthtype() {
		return monthtype;
	}

	public void setMonthtype(String monthtype) {
		this.monthtype = monthtype;
	}

	public String getLandpaydate() {
		return landpaydate;
	}

	public void setLandpaydate(String landpaydate) {
		this.landpaydate = landpaydate;
	}

	public String getThreecardsdate() {
		return threecardsdate;
	}

	public void setThreecardsdate(String threecardsdate) {
		this.threecardsdate = threecardsdate;
	}

	public String getPaymentdate() {
		return paymentdate;
	}

	public void setPaymentdate(String paymentdate) {
		this.paymentdate = paymentdate;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public UFDouble getLoanmonadj_amount() {
		return loanmonadj_amount;
	}

	public void setLoanmonadj_amount(UFDouble loanmonadj_amount) {
		this.loanmonadj_amount = loanmonadj_amount;
	}

	public UFDouble getLoanmondiff_amount() {
		return loanmondiff_amount;
	}

	public void setLoanmondiff_amount(UFDouble loanmondiff_amount) {
		this.loanmondiff_amount = loanmondiff_amount;
	}

	@Override
	public String getEntityName() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void validate() throws ValidationException {
		// TODO 自动生成的方法存根

	}

	public UFDouble getInitplan_amount() {
		return initplan_amount;
	}

	public void setInitplan_amount(UFDouble initplan_amount) {
		this.initplan_amount = initplan_amount;
	}

	public UFDouble getInitactual_amount() {
		return initactual_amount;
	}

	public void setInitactual_amount(UFDouble initactual_amount) {
		this.initactual_amount = initactual_amount;
	}

	public UFDouble getInitadj_amount() {
		return initadj_amount;
	}

	public void setInitadj_amount(UFDouble initadj_amount) {
		this.initadj_amount = initadj_amount;
	}

	public String getPk_finmonthlyplan() {
		return pk_finmonthlyplan;
	}

	public void setPk_finmonthlyplan(String pk_finmonthlyplan) {
		this.pk_finmonthlyplan = pk_finmonthlyplan;
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "tgrz_finmonthlyplan";
	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_finmonthlyplan";
	}

}
