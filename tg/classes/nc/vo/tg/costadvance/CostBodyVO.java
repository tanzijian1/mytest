package nc.vo.tg.costadvance;

import java.io.Serializable;
/**
 * ��ϵͳ����Ԥ�ᵥ����
 * @author lyq
 *
 */
public class CostBodyVO implements Serializable {
	private String assumeorg_name;//���óе���λ����
	private String assumeorg;//���óе���λ����
	private String assumedept_name;//���óе���������
	private String assumedept;//���óе����ű���
	private String budgetsubject;//Ԥ���Ŀ
	private String budgetsubjectname;//Ԥ���Ŀȫ��
	private String businessformat;//ҵ̬
	private String lender;//�ÿ���
	private String project;//��Ŀ
	private String cardeptdoc;//���Ʋ��ŵ���
	private String deptfloor;//����/¥��
	private float amount;//���
	private String scale;//��ֱ���
	private String explain;//˵��
	private float advanceamount;//��Ԥ����
	private String  budgetyear;//Ԥ�����
	
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
