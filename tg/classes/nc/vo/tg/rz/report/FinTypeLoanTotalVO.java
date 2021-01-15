package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 融资类型放款统计VO,用于年度融资放款报表转换信息
 * 
 * @author HUANGDQ
 * @date 2019年7月2日 上午11:06:14
 */
public class FinTypeLoanTotalVO implements Serializable, Cloneable {
	String cyear;//年度
	String pk_fintype;// 融资类型
	String fintypecode;// 融资类型编码
	String fintypename;// 融资类型名称
	UFDouble amount;// 年度放款金额

	
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
