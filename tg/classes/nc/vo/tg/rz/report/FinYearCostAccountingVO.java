package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 存量融资成本
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 上午11:24:08
 */
public class FinYearCostAccountingVO implements Serializable, Cloneable {
	String pk_fintype;// 融资类型
	String fintypecode;// 融资类型编码
	String fintypename;// 融资类型名称
	UFDouble fin_balance;// 融资余额
	UFDouble avecost_amount;// 加权平均成本（合同利率）
	UFDouble finadviser_amount;//财顾费请款

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

	public UFDouble getFin_balance() {
		return fin_balance;
	}

	public void setFin_balance(UFDouble fin_balance) {
		this.fin_balance = fin_balance;
	}

	public UFDouble getAvecost_amount() {
		return avecost_amount;
	}

	public void setAvecost_amount(UFDouble avecost_amount) {
		this.avecost_amount = avecost_amount;
	}

	public UFDouble getFinadviser_amount() {
		return finadviser_amount;
	}

	public void setFinadviser_amount(UFDouble finadviser_amount) {
		this.finadviser_amount = finadviser_amount;
	}
	 
	
}
