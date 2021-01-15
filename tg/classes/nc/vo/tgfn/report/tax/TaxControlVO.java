package nc.vo.tgfn.report.tax;

import java.io.Serializable;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ˰�ر�˰�񱨱�
 * 
 * @author ASUS
 * 
 */
public class TaxControlVO implements Serializable, Cloneable {
	public String getDef24() {
		return def24;
	}
	public void setDef24(String def24) {
		this.def24 = def24;
	}
	public String getProject_code() {
		return project_code;
	}
	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}
	public String getAreanum() {
		return areanum;
	}
	public void setAreanum(String areanum) {
		this.areanum = areanum;
	}
	public String getDef58() {
		return def58;
	}
	public void setDef58(String def58) {
		this.def58 = def58;
	}
	public String getSuppliername() {
		return suppliername;
	}
	public void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getProjecttype() {
		return projecttype;
	}
	public void setProjecttype(String projecttype) {
		this.projecttype = projecttype;
	}
	public String getTaxclass() {
		return taxclass;
	}
	public void setTaxclass(String taxclass) {
		this.taxclass = taxclass;
	}
	public String getDef52() {
		return def52;
	}
	public void setDef52(String def52) {
		this.def52 = def52;
	}
	public String getBilldate() {
		return billdate;
	}
	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}
	public UFDouble getDef43amount() {
		return def43amount;
	}
	public void setDef43amount(UFDouble def43amount) {
		this.def43amount = def43amount;
	}
	
	public String getProjectproperty() {
		return projectproperty;
	}
	public void setProjectproperty(String projectproperty) {
		this.projectproperty = projectproperty;
	}
	public String getIsmerge() {
		return ismerge;
	}
	public void setIsmerge(String ismerge) {
		this.ismerge = ismerge;
	}
	public UFDouble getMoneyamount() {
		return moneyamount;
	}
	public void setMoneyamount(UFDouble moneyamount) {
		this.moneyamount = moneyamount;
	}
	private String def24;//,--���й�˾
	private String project_code;// ,--���ڱ��
	private String areanum;//--������
	private String  def58;//,--˰���������������/��
	private String suppliername ;//,--����˰�����
	private String orgname;//,--��ҵ����(��˰����)
	private String project_name;//,--��������
	private String projecttype;//,--��Ŀ����
	private String taxclass;//,--˰��-˰Ŀ
	private String def52;//,--������
	private String billdate;//,--������
	private UFDouble def43amount;//--�ѽ�˰��
	
	private UFDouble moneyamount;//--���˰��
	private String projectproperty;//--��Ŀ����
	private String ismerge;// --�Ƿ񲢱�
}
