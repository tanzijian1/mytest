package vo.tg.outside;

import java.io.Serializable;

/**
 * ��ϵͳ���
 * 
 * @author ��ΰ
 * 
 */
public class PersonalIncomeTaxBodyVO implements Serializable {
	private String recaccount; // �տ��˻���Ϣ
	private String money_de; // ʵ�۸�˰
	private String ssname;// �������������ƣ���Ӧ�̵�����

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
