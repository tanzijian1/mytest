package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * Nc��Ʊ̨�ˣ��ɱ�����
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
	private String imagecode;//Ӱ�����
	//end
	/**��˾����*/
	private String companyname;
	/** ��ͬ���*/
	private String code;
	/** ��ͬ����*/
	private String cname;
	/** ��Ӧ�̱���*/
	private String gcode;
	/** ��Ӧ������*/
	private String gname;
	/** ����*/
	private String billon;
	/** ���˵���*/
	private String accountone;
	/** ��Ʊ��Ӧ��*/
	private String gbill;
	/** ��Ʊ����*/
	private String fnumber;
	/**��˰���*/
	private UFDouble taxisamount;
	/**����˰���*/
	private UFDouble taxnotincludedamount;
	/**˰��*/
	private UFDouble rateamount;
	/**˰��*/
	private UFDouble taxamount;
	/**�����·�*/
	private String accounmonth;
	/**ƾ֤��*/
	private String certificatenum;
	/**Ԥ���ֶ�1*/
	private String ylzd1;
	/**Ԥ���ֶ�2*/
	private String ylzd2;
	/**Ԥ���ֶ�3*/
	private String ylzd3;
	/**Ԥ���ֶ�4*/
	private String ylzd4;
	/**Ԥ���ֶ�5*/
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
