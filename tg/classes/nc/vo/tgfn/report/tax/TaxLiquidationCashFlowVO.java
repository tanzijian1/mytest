package nc.vo.tgfn.report.tax;

import java.io.Serializable;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ˰������֧��-�ֽ������ݱ�˰�񱨱�
 * 
 * @author hzp
 * 
 */
public class TaxLiquidationCashFlowVO implements Serializable, Cloneable {
	
	private String taxid;// ��˰��ʶ���
	private UFDate formdate;// �������
	
	private String orgname;// ��˰������ (������֯)
	private String pk_org;// ��֯pk
	private String projectname;// ��Ŀ 
	private String pk_project;// ��Ŀpk
	private String contract;// ��ͬ
	private String applynum;// ebs���ţ���ϵͳ���ݺţ�
	private String billno;// nc֧�����ţ����ݺţ�
	private UFDouble money;// ֧�����
	private Integer num;// ƾ֤��
	private String payee;// �տ
	private String costsubject;// �ɱ���Ŀ
	
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	public String getApplynum() {
		return applynum;
	}
	public void setApplynum(String applynum) {
		this.applynum = applynum;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public UFDouble getMoney() {
		return money;
	}
	public void setMoney(UFDouble money) {
		this.money = money;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getCostsubject() {
		return costsubject;
	}
	public void setCostsubject(String costsubject) {
		this.costsubject = costsubject;
	}
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	public UFDate getFormdate() {
		return formdate;
	}
	public void setFormdate(UFDate formdate) {
		this.formdate = formdate;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getPk_project() {
		return pk_project;
	}
	public void setPk_project(String pk_project) {
		this.pk_project = pk_project;
	}
	
	
	
}
