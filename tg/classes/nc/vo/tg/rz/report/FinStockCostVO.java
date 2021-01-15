package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 年度融资成本
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 上午11:24:08
 */
public class FinStockCostVO implements Serializable, Cloneable {
	String pk_fintype;// 融资类型
	String fintypecode;// 融资类型编码
	String fintypename;// 融资类型名称
	UFDouble loanadd_amount;//总年度新增放款
	UFDouble fincost_amount;// 融资成本（合同利率）
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
