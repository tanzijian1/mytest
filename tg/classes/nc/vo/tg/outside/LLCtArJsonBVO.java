package nc.vo.tg.outside;

import nc.vo.tg.ctar.CtArJsonBVO;

/**
 * �����ͬ��������VO
 * 
 * @author ̸�ӽ�-2020-07-24
 * 
 */
public class LLCtArJsonBVO extends CtArJsonBVO {
	// ���Ҽ�˰�ϼ�
	private String ntaxmny;
	// ԭ����˰���
	public String norigmny;
	public String collectionitemstype; // �շ���Ŀ����
	public String collectionitemsname; // �շ���Ŀ����
	public String norigtax;// ԭ��˰��

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
