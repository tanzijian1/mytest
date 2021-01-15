package nc.vo.tg.costadvance;

import java.io.Serializable;
/**
 * 外系统费用预提单表体
 * @author lyq
 *
 */
public class CostBodyVO implements Serializable {
	private String assumeorg_name;//费用承担单位名称
	private String assumeorg;//费用承担单位编码
	private String assumedept_name;//费用承担部门名称
	private String assumedept;//费用承担部门编码
	private String budgetsubject;//预算科目
	private String budgetsubjectname;//预算科目全称
	private String businessformat;//业态
	private String lender;//用款人
	private String project;//项目
	private String cardeptdoc;//车牌部门档案
	private String deptfloor;//部门/楼层
	private float amount;//金额
	private String scale;//拆分比例
	private String explain;//说明
	private float advanceamount;//冲预提金额
	private String  budgetyear;//预算年度
	
	public String getBudgetyear() {
		return budgetyear;
	}
	public void setBudgetyear(String budgetyear) {
		this.budgetyear = budgetyear;
	}
	public String getAssumeorg_name() {
		return assumeorg_name;
	}
	public void setAssumeorg_name(String assumeorg_name) {
		this.assumeorg_name = assumeorg_name;
	}
	public String getAssumedept_name() {
		return assumedept_name;
	}
	public void setAssumedept_name(String assumedept_name) {
		this.assumedept_name = assumedept_name;
	}
	public float getAdvanceamount() {
		return advanceamount;
	}
	public void setAdvanceamount(float advanceamount) {
		this.advanceamount = advanceamount;
	}
	public String getAssumeorg() {
		return assumeorg;
	}
	public void setAssumeorg(String assumeorg) {
		this.assumeorg = assumeorg;
	}
	public String getAssumedept() {
		return assumedept;
	}
	public void setAssumedept(String assumedept) {
		this.assumedept = assumedept;
	}
	public String getBudgetsubject() {
		return budgetsubject;
	}
	public void setBudgetsubject(String budgetsubject) {
		this.budgetsubject = budgetsubject;
	}
	public String getBudgetsubjectname() {
		return budgetsubjectname;
	}
	public void setBudgetsubjectname(String budgetsubjectname) {
		this.budgetsubjectname = budgetsubjectname;
	}
	public String getBusinessformat() {
		return businessformat;
	}
	public void setBusinessformat(String businessformat) {
		this.businessformat = businessformat;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getLender() {
		return lender;
	}
	public void setLender(String lender) {
		this.lender = lender;
	}
	public String getCardeptdoc() {
		return cardeptdoc;
	}
	public void setCardeptdoc(String cardeptdoc) {
		this.cardeptdoc = cardeptdoc;
	}
	public String getDeptfloor() {
		return deptfloor;
	}
	public void setDeptfloor(String deptfloor) {
		this.deptfloor = deptfloor;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}

	
	
}
