package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * �������ͷſ�ͳ��VO,����������ʷſ��ת����Ϣ
 * 
 * @author HUANGDQ
 * @date 2019��7��2�� ����11:06:14
 */
public class FinTypeLoanTotalVO implements Serializable, Cloneable {
	String cyear;//���
	String pk_fintype;// ��������
	String fintypecode;// �������ͱ���
	String fintypename;// ������������
	UFDouble amount;// ��ȷſ���

	
	public String getCyear() {
		return cyear;
	}

	public void setCyear(String cyear) {
		this.cyear = cyear;
	}

	public String getPk_fintype() {
		return pk_fintype;
	}

	public void setPk_fintype(String pk_fintype) {
		this.pk_fintype = pk_fintype;
	}

	public String getFintypecode() {
		return fintypecode;
	}

	public void setFintypecode(String fintypecode) {
		this.fintypecode = fintypecode;
	}

	public String getFintypename() {
		return fintypename;
	}

	public void setFintypename(String fintypename) {
		this.fintypename = fintypename;
	}

	public UFDouble getAmount() {
		return amount;
	}

	public void setAmount(UFDouble amount) {
		this.amount = amount;
	}

}
