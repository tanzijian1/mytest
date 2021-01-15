package nc.vo.tg.outside;

import java.io.Serializable;

import nc.vo.pub.SuperVO;

public class EBSBankdocVO extends SuperVO implements Serializable{
	/**编码*/
	public String code;
	/**名称*/
	public String name;
	/**所属组织编码或名称*/
	public String org_code;
	/**银行类别编码或名称*/
	public String banktype_code;
	/**简称*/
	public String shortname;
	/**启用状态*/	
	public String enablestate;
	/**地区代码*/
	public String areacode;	
	/**开户地区*/
	public String bankarea;
	/**机构号/分行号*/
	public String orgnumber;
	/**省份*/
	public String province;
	/**城市*/
	public String city;
	/**联行号*/
	public String combinenum;
	/**自定义项2*/	
	public String def2;
	/**自定义项3*/	
	public String def3;
	/**自定义项4*/	
	public String def4;
	/**自定义项5*/	
	public String def5;

	
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getBankarea() {
		return bankarea;
	}
	public void setBankarea(String bankarea) {
		this.bankarea = bankarea;
	}
	public String getOrgnumber() {
		return orgnumber;
	}
	public void setOrgnumber(String orgnumber) {
		this.orgnumber = orgnumber;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCombinenum() {
		return combinenum;
	}
	public void setCombinenum(String combinenum) {
		this.combinenum = combinenum;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	public String getBanktype_code() {
		return banktype_code;
	}
	public void setBanktype_code(String banktype_code) {
		this.banktype_code = banktype_code;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getEnablestate() {
		return enablestate;
	}
	public void setEnablestate(String enablestate) {
		this.enablestate = enablestate;
	}
	public String getDef2() {
		return def2;
	}
	public void setDef2(String def2) {
		this.def2 = def2;
	}
	public String getDef3() {
		return def3;
	}
	public void setDef3(String def3) {
		this.def3 = def3;
	}
	public String getDef4() {
		return def4;
	}
	public void setDef4(String def4) {
		this.def4 = def4;
	}
	public String getDef5() {
		return def5;
	}
	public void setDef5(String def5) {
		this.def5 = def5;
	}

	
	

}
