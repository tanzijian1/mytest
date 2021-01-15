package nc.vo.tgfn.report.tax;

import java.io.Serializable;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 税收清算支撑-计提数据表（税务报表）
 * 
 * @author hzp
 * 
 */
public class TaxLiquidationProvisionVO implements Serializable, Cloneable {
	
	private String projectname; //项目名称
	private Integer num; //凭证号
	private String explanation; //内容摘要
	private String fptype; //发票类型
	private String fph;//发票号码
	private UFDouble costmoneyin;//发票合计金额
	private UFDouble dkmoney;//可抵扣的增值税进项税额
	private String applynum;//ebs请款单号
	private String nbillno;//nc单据号
	private String costsubject;//成本科目
	private String formats;//业态
	private String contract;//合同名称（编号）
	private String orgname;//纳税人名称
	private String taxid;//纳税人识别号
	private UFDate formdata;//填表日期称
	
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
