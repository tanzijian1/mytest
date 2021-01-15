package nc.vo.tgfn.report.tax;

import java.io.Serializable;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ˰������֧��-�������ݱ�˰�񱨱�
 * 
 * @author hzp
 * 
 */
public class TaxLiquidationProvisionVO implements Serializable, Cloneable {
	
	private String projectname; //��Ŀ����
	private Integer num; //ƾ֤��
	private String explanation; //����ժҪ
	private String fptype; //��Ʊ����
	private String fph;//��Ʊ����
	private UFDouble costmoneyin;//��Ʊ�ϼƽ��
	private UFDouble dkmoney;//�ɵֿ۵���ֵ˰����˰��
	private String applynum;//ebs����
	private String nbillno;//nc���ݺ�
	private String costsubject;//�ɱ���Ŀ
	private String formats;//ҵ̬
	private String contract;//��ͬ���ƣ���ţ�
	private String orgname;//��˰������
	private String taxid;//��˰��ʶ���
	private UFDate formdata;//������ڳ�
	
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getFptype() {
		return fptype;
	}
	public void setFptype(String fptype) {
		this.fptype = fptype;
	}
	public String getFph() {
		return fph;
	}
	public void setFph(String fph) {
		this.fph = fph;
	}
	public String getApplynum() {
		return applynum;
	}
	public void setApplynum(String applynum) {
		this.applynum = applynum;
	}
	public String getNbillno() {
		return nbillno;
	}
	public void setNbillno(String nbillno) {
		this.nbillno = nbillno;
	}
	public String getCostsubject() {
		return costsubject;
	}
	public void setCostsubject(String costsubject) {
		this.costsubject = costsubject;
	}
	public String getFormats() {
		return formats;
	}
	public void setFormats(String formats) {
		this.formats = formats;
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public UFDouble getCostmoneyin() {
		return costmoneyin;
	}
	public void setCostmoneyin(UFDouble costmoneyin) {
		this.costmoneyin = costmoneyin;
	}
	public UFDouble getDkmoney() {
		return dkmoney;
	}
	public void setDkmoney(UFDouble dkmoney) {
		this.dkmoney = dkmoney;
	}
	public UFDate getFormdata() {
		return formdata;
	}
	public void setFormdata(UFDate formdata) {
		this.formdata = formdata;
	}
	
	
}
