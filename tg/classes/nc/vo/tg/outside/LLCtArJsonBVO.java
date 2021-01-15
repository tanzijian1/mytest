package nc.vo.tg.outside;

import nc.vo.tg.ctar.CtArJsonBVO;

/**
 * 邻里合同基本抽象VO
 * 
 * @author 谈子健-2020-07-24
 * 
 */
public class LLCtArJsonBVO extends CtArJsonBVO {
	// 本币价税合计
	private String ntaxmny;
	// 原币无税金额
	public String norigmny;
	public String collectionitemstype; // 收费项目类型
	public String collectionitemsname; // 收费项目名称
	public String norigtax;// 原币税额

	public String getNorigtax() {
		return norigtax;
	}

	public void setNorigtax(String norigtax) {
		this.norigtax = norigtax;
	}

	public String getCollectionitemstype() {
		return collectionitemstype;
	}

	public void setCollectionitemstype(String collectionitemstype) {
		this.collectionitemstype = collectionitemstype;
	}

	public String getCollectionitemsname() {
		return collectionitemsname;
	}

	public void setCollectionitemsname(String collectionitemsname) {
		this.collectionitemsname = collectionitemsname;
	}

	public String getNorigmny() {
		return norigmny;
	}

	public void setNorigmny(String norigmny) {
		this.norigmny = norigmny;
	}

	public String getNtaxmny() {
		return ntaxmny;
	}

	public void setNtaxmny(String ntaxmny) {
		this.ntaxmny = ntaxmny;
	}

}
