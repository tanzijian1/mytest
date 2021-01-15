package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * ������ʳɱ�
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����11:24:08
 */
public class FinStockCostVO implements Serializable, Cloneable {
	String pk_fintype;// ��������
	String fintypecode;// �������ͱ���
	String fintypename;// ������������
	UFDouble loanadd_amount;//����������ſ�
	UFDouble fincost_amount;// ���ʳɱ�����ͬ���ʣ�
	UFDouble finadviser_amount;//�ƹ˷����

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

	public UFDouble getLoanadd_amount() {
		return loanadd_amount;
	}

	public void setLoanadd_amount(UFDouble loanadd_amount) {
		this.loanadd_amount = loanadd_amount;
	}

	public UFDouble getFincost_amount() {
		return fincost_amount;
	}

	public void setFincost_amount(UFDouble fincost_amount) {
		this.fincost_amount = fincost_amount;
	}

	public UFDouble getFinadviser_amount() {
		return finadviser_amount;
	}

	public void setFinadviser_amount(UFDouble finadviser_amount) {
		this.finadviser_amount = finadviser_amount;
	}
 
	
}
