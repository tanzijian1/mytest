package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * ���Ϣ��
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����10:54:34
 */
public class RepAndPayVO  implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1420441408876658290L;
	String code;//���д����ͬ���
	String pk_project;// ��ĿID
	String projectcode;// ��Ŀ����
	String projectname;// ��Ŀ����
	String pk_periodization;// ��������
	String periodizationname;// ��������
	String projectcorp;// ��Ŀ��˾
	String borrower;// �����
	String pk_fintype;// ��������
	String fintypecode;// �������ͱ���
	String fintypename;// ������������
	String pk_organization;// ���ʻ���
	String organizationcode;// ��������
	String organizationname;// ��������
	UFDouble fin_amount;// ���ʽ��
	UFDouble loantatol_amount;// �ۼƷſ���
	UFDouble yeartatol_amount;//����ۼƻ�����
	String contnote; // ���ʺ�ͬ(��ִͬ����ϸ)
	String begindate;// ������ʼ��
	String enddate; // ���ʵ�����
	String pk_repaymenttype; // ���ʽ
	String repaymenttypename;// ���ʽ
	String  rePaycondition;//��������
	UFDouble contratio;// ��ͬ����
	String pk_paytype1;// ��Ϣ֧����ʽ
	String paytype1name;// ��Ϣ֧����ʽ
	UFDouble adviserratio; // ������ʷ���
	String pk_paytype2;// ������ʷ���֧����ʽ
	String paytype2name;// ������ʷ���֧����ʽ
	String datatype;
	UFDouble repyearplantotal_amount;// N��ƻ�������ϼ�
	UFDouble repyearactualtotal_amount; // N��ʵ�ʻ�����ϼ�
	UFDouble ipyearactualtotal_amount;// N�긶Ϣ���ϼ�
	UFDouble advyearplantotal_amount;// N��ƻ�������ʷѽ��ϼ�
	UFDouble advyearactualtotal_amount;// N��ʵ�ʲ�����ʷѽ��ϼ�

	UFDouble repmonplantotal_amount;// N��/n+x��ƻ�������
	UFDouble repmonactualtotal_amount;// N��/n+xʵ�ʻ�����
	UFDouble ipmonplantotal_amount;// N��/n+x�ƻ���Ϣ���
	UFDouble ipmonactualtotal_amount;// N��/n+xʵ�ʸ�Ϣ���ϼ�
	UFDouble advmonplantotal_amount;// N��/n+x�ƻ�������ʷ��ý��
	UFDouble advmonactualtotal_amount;// N��/n+xʵ�ʲ�����ʷ��ý��

	
	
	public UFDouble getYeartatol_amount() {
		return yeartatol_amount;
	}

	public void setYeartatol_amount(UFDouble yeartatol_amount) {
		this.yeartatol_amount = yeartatol_amount;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getContnote() {
		return contnote;
	}

	public void setContnote(String contnote) {
		this.contnote = contnote;
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

	public String getProjectcorp() {
		return projectcorp;
	}

	public void setProjectcorp(String projectcorp) {
		this.projectcorp = projectcorp;
	}

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
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

	public String getPk_organization() {
		return pk_organization;
	}

	public void setPk_organization(String pk_organization) {
		this.pk_organization = pk_organization;
	}

	public String getOrganizationcode() {
		return organizationcode;
	}

	public void setOrganizationcode(String organizationcode) {
		this.organizationcode = organizationcode;
	}

	public String getOrganizationname() {
		return organizationname;
	}

	public void setOrganizationname(String organizationname) {
		this.organizationname = organizationname;
	}

	public UFDouble getFin_amount() {
		return fin_amount;
	}

	public void setFin_amount(UFDouble fin_amount) {
		this.fin_amount = fin_amount;
	}

	public UFDouble getLoantatol_amount() {
		return loantatol_amount;
	}

	public void setLoantatol_amount(UFDouble loantatol_amount) {
		this.loantatol_amount = loantatol_amount;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getPk_repaymenttype() {
		return pk_repaymenttype;
	}

	public void setPk_repaymenttype(String pk_repaymenttype) {
		this.pk_repaymenttype = pk_repaymenttype;
	}

	public String getRepaymenttypename() {
		return repaymenttypename;
	}

	public void setRepaymenttypename(String repaymenttypename) {
		this.repaymenttypename = repaymenttypename;
	}

	public UFDouble getContratio() {
		return contratio;
	}

	public void setContratio(UFDouble contratio) {
		this.contratio = contratio;
	}

	public String getPk_paytype1() {
		return pk_paytype1;
	}

	public void setPk_paytype1(String pk_paytype1) {
		this.pk_paytype1 = pk_paytype1;
	}

	public String getPaytype1name() {
		return paytype1name;
	}

	public void setPaytype1name(String paytype1name) {
		this.paytype1name = paytype1name;
	}

	public UFDouble getAdviserratio() {
		return adviserratio;
	}

	public void setAdviserratio(UFDouble adviserratio) {
		this.adviserratio = adviserratio;
	}

	public String getPk_paytype2() {
		return pk_paytype2;
	}

	public void setPk_paytype2(String pk_paytype2) {
		this.pk_paytype2 = pk_paytype2;
	}

	public String getPaytype2name() {
		return paytype2name;
	}

	public void setPaytype2name(String paytype2name) {
		this.paytype2name = paytype2name;
	}

	public UFDouble getRepyearplantotal_amount() {
		return repyearplantotal_amount;
	}

	public void setRepyearplantotal_amount(UFDouble repyearplantotal_amount) {
		this.repyearplantotal_amount = repyearplantotal_amount;
	}

	public UFDouble getRepyearactualtotal_amount() {
		return repyearactualtotal_amount;
	}

	public void setRepyearactualtotal_amount(UFDouble repyearactualtotal_amount) {
		this.repyearactualtotal_amount = repyearactualtotal_amount;
	}

	public UFDouble getIpyearactualtotal_amount() {
		return ipyearactualtotal_amount;
	}

	public void setIpyearactualtotal_amount(UFDouble ipyearactualtotal_amount) {
		this.ipyearactualtotal_amount = ipyearactualtotal_amount;
	}

	public UFDouble getAdvyearplantotal_amount() {
		return advyearplantotal_amount;
	}

	public void setAdvyearplantotal_amount(UFDouble advyearplantotal_amount) {
		this.advyearplantotal_amount = advyearplantotal_amount;
	}

	public UFDouble getAdvyearactualtotal_amount() {
		return advyearactualtotal_amount;
	}

	public void setAdvyearactualtotal_amount(UFDouble advyearactualtotal_amount) {
		this.advyearactualtotal_amount = advyearactualtotal_amount;
	}

	public UFDouble getRepmonplantotal_amount() {
		return repmonplantotal_amount;
	}

	public void setRepmonplantotal_amount(UFDouble repmonplantotal_amount) {
		this.repmonplantotal_amount = repmonplantotal_amount;
	}

	public UFDouble getRepmonactualtotal_amount() {
		return repmonactualtotal_amount;
	}

	public void setRepmonactualtotal_amount(UFDouble repmonactualtotal_amount) {
		this.repmonactualtotal_amount = repmonactualtotal_amount;
	}

	public UFDouble getIpmonplantotal_amount() {
		return ipmonplantotal_amount;
	}

	public void setIpmonplantotal_amount(UFDouble ipmonplantotal_amount) {
		this.ipmonplantotal_amount = ipmonplantotal_amount;
	}

	public UFDouble getAdvmonplantotal_amount() {
		return advmonplantotal_amount;
	}

	public void setAdvmonplantotal_amount(UFDouble advmonplantotal_amount) {
		this.advmonplantotal_amount = advmonplantotal_amount;
	}

	public UFDouble getAdvmonactualtotal_amount() {
		return advmonactualtotal_amount;
	}

	public void setAdvmonactualtotal_amount(UFDouble advmonactualtotal_amount) {
		this.advmonactualtotal_amount = advmonactualtotal_amount;
	}

	public UFDouble getIpmonactualtotal_amount() {
		return ipmonactualtotal_amount;
	}

	public void setIpmonactualtotal_amount(UFDouble ipmonactualtotal_amount) {
		this.ipmonactualtotal_amount = ipmonactualtotal_amount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRePaycondition() {
		return rePaycondition;
	}

	public void setRePaycondition(String rePaycondition) {
		this.rePaycondition = rePaycondition;
	}
	
	
}
