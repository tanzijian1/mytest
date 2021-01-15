package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * Nc发票台账（成本报表）
 * LJF
 * @author ASUS
 * 
 */
public class InvLedgerVO extends SuperVO implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2230021315414345426L;
	//add by tjl
	private String imagecode;//影像编码
	//end
	/**公司名称*/
	private String companyname;
	/** 合同编号*/
	private String code;
	/** 合同名称*/
	private String cname;
	/** 供应商编码*/
	private String gcode;
	/** 供应商名称*/
	private String gname;
	/** 请款单号*/
	private String billon;
	/** 对账单号*/
	private String accountone;
	/** 开票供应商*/
	private String gbill;
	/** 发票号码*/
	private String fnumber;
	/**含税金额*/
	private UFDouble taxisamount;
	/**不含税金额*/
	private UFDouble taxnotincludedamount;
	/**税率*/
	private UFDouble rateamount;
	/**税额*/
	private UFDouble taxamount;
	/**入账月份*/
	private String accounmonth;
	/**凭证号*/
	private String certificatenum;
	/**预留字段1*/
	private String ylzd1;
	/**预留字段2*/
	private String ylzd2;
	/**预留字段3*/
	private String ylzd3;
	/**预留字段4*/
	private String ylzd4;
	/**预留字段5*/
	private String ylzd5;
	
	public String getImagecode() {
		return imagecode;
	}
	public void setImagecode(String imagecode) {
		this.imagecode = imagecode;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getGcode() {
		return gcode;
	}
	public void setGcode(String gcode) {
		this.gcode = gcode;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getBillon() {
		return billon;
	}
	public void setBillon(String billon) {
		this.billon = billon;
	}
	public String getAccountone() {
		return accountone;
	}
	public void setAccountone(String accountone) {
		this.accountone = accountone;
	}
	public String getGbill() {
		return gbill;
	}
	public void setGbill(String gbill) {
		this.gbill = gbill;
	}
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public UFDouble getTaxisamount() {
		return taxisamount;
	}
	public void setTaxisamount(UFDouble taxisamount) {
		this.taxisamount = taxisamount;
	}
	public UFDouble getTaxnotincludedamount() {
		return taxnotincludedamount;
	}
	public void setTaxnotincludedamount(UFDouble taxnotincludedamount) {
		this.taxnotincludedamount = taxnotincludedamount;
	}
	public UFDouble getRateamount() {
		return rateamount;
	}
	public void setRateamount(UFDouble rateamount) {
		this.rateamount = rateamount;
	}
	public UFDouble getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(UFDouble taxamount) {
		this.taxamount = taxamount;
	}
	public String getAccounmonth() {
		return accounmonth;
	}
	public void setAccounmonth(String accounmonth) {
		this.accounmonth = accounmonth;
	}
	public String getCertificatenum() {
		return certificatenum;
	}
	public void setCertificatenum(String certificatenum) {
		this.certificatenum = certificatenum;
	}
	public String getYlzd1() {
		return ylzd1;
	}
	public void setYlzd1(String ylzd1) {
		this.ylzd1 = ylzd1;
	}
	public String getYlzd2() {
		return ylzd2;
	}
	public void setYlzd2(String ylzd2) {
		this.ylzd2 = ylzd2;
	}
	public String getYlzd3() {
		return ylzd3;
	}
	public void setYlzd3(String ylzd3) {
		this.ylzd3 = ylzd3;
	}
	public String getYlzd4() {
		return ylzd4;
	}
	public void setYlzd4(String ylzd4) {
		this.ylzd4 = ylzd4;
	}
	public String getYlzd5() {
		return ylzd5;
	}
	public void setYlzd5(String ylzd5) {
		this.ylzd5 = ylzd5;
	}

}
