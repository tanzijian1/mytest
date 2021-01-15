package vo.tg.outside;

import java.io.Serializable;

/**
 * HCM 各公司公积金应付单表体
 * @author zhaozhiying
 *
 */
public class HCMFundBillBodyVo implements Serializable {
	
	private String def18; // 合同签订公司
	private String def30; // 部门属性
	private String def22; // 人数
	private String ssname; // 社保方名称（供应商档案）
	private String recaccount; // 收款账户信息
	private String def1; // 公积金公司部分
	private String def36; // 公积金个人部分
	private String money_de;	//公司+个人合计
	public String getDef18() {
		return def18;
	}
	public void setDef18(String def18) {
		this.def18 = def18;
	}
	public String getDef30() {
		return def30;
	}
	public void setDef30(String def30) {
		this.def30 = def30;
	}
	public String getDef22() {
		return def22;
	}
	public void setDef22(String def22) {
		this.def22 = def22;
	}
	public String getSsname() {
		return ssname;
	}
	public void setSsname(String ssname) {
		this.ssname = ssname;
	}
	public String getRecaccount() {
		return recaccount;
	}
	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}
	public String getDef1() {
		return def1;
	}
	public void setDef1(String def1) {
		this.def1 = def1;
	}
	public String getDef36() {
		return def36;
	}
	public void setDef36(String def36) {
		this.def36 = def36;
	}
	public String getMoney_de() {
		return money_de;
	}
	public void setMoney_de(String money_de) {
		this.money_de = money_de;
	}
	
	
}
