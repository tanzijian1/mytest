package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCtApJsonBVO implements Serializable {
	private String fbudgetsubjectname;// 科目名称
	private String pk_ebs;// EBS主表ID
	private String splitratio;// 拆分比例
	private String amount;// 金额（元）
	private String ispresupposed;// 预提
	private String remark;// 说明
	private String budgetyears;// 预算年度
	private String ncprojectname;// 项目
	private String materialcategory;// 物料类别
	private String materialcode;// ebs物料编码
	private String remarks;// 物料说明
	private String nastnum;// 数量
	private String ntaxrate;// 税率（%）
	private String norigtaxprice;// 单价（含税）
	private String norigprice;// 单价（不含税）
	private String notaxmny;// 不含税总价
	 

	public String getFbudgetsubjectname() {
		return fbudgetsubjectname;
	}

	public void setFbudgetsubjectname(String fbudgetsubjectname) {
		this.fbudgetsubjectname = fbudgetsubjectname;
	}

	public String getPk_ebs() {
		return pk_ebs;
	}

	public void setPk_ebs(String pk_ebs) {
		this.pk_ebs = pk_ebs;
	}

	public String getSplitratio() {
		return splitratio;
	}

	public void setSplitratio(String splitratio) {
		this.splitratio = splitratio;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getIspresupposed() {
		return ispresupposed;
	}

	public void setIspresupposed(String ispresupposed) {
		this.ispresupposed = ispresupposed;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBudgetyears() {
		return budgetyears;
	}

	public void setBudgetyears(String budgetyears) {
		this.budgetyears = budgetyears;
	}

	public String getNcprojectname() {
		return ncprojectname;
	}

	public void setNcprojectname(String ncprojectname) {
		this.ncprojectname = ncprojectname;
	}

	public String getMaterialcategory() {
		return materialcategory;
	}

	public void setMaterialcategory(String materialcategory) {
		this.materialcategory = materialcategory;
	}

	public String getMaterialcode() {
		return materialcode;
	}

	public void setMaterialcode(String materialcode) {
		this.materialcode = materialcode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNastnum() {
		return nastnum;
	}

	public void setNastnum(String nastnum) {
		this.nastnum = nastnum;
	}

	public String getNtaxrate() {
		return ntaxrate;
	}

	public void setNtaxrate(String ntaxrate) {
		this.ntaxrate = ntaxrate;
	}

	public String getNorigtaxprice() {
		return norigtaxprice;
	}

	public void setNorigtaxprice(String norigtaxprice) {
		this.norigtaxprice = norigtaxprice;
	}

	public String getNorigprice() {
		return norigprice;
	}

	public void setNorigprice(String norigprice) {
		this.norigprice = norigprice;
	}

	public String getNotaxmny() {
		return notaxmny;
	}

	public void setNotaxmny(String notaxmny) {
		this.notaxmny = notaxmny;
	}

}
