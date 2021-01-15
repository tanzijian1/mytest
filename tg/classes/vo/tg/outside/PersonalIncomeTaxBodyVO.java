package vo.tg.outside;

import java.io.Serializable;

/**
 * 外系统付款单
 * 
 * @author 彭伟
 * 
 */
public class PersonalIncomeTaxBodyVO implements Serializable {
	private String recaccount; // 收款账户信息
	private String money_de; // 实扣个税
	private String ssname;// 公积金中心名称（供应商档案）

	public String getRecaccount() {
		return recaccount;
	}

	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}

	public String getMoney_de() {
		return money_de;
	}

	public void setMoney_de(String money_de) {
		this.money_de = money_de;
	}

	public String getSsname() {
		return ssname;
	}

	public void setSsname(String ssname) {
		this.ssname = ssname;
	}

}
