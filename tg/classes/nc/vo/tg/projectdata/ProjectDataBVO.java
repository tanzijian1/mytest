package nc.vo.tg.projectdata;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 * �˴�����۵�������Ϣ
 * </p>
 * ��������:2019-6-29
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class ProjectDataBVO extends SuperVO {
	public static final String PK_PROJECTDATA_B = "pk_projectdata_b";// ����
	public static final String PAYDATE = "paydate";// �ؼ�/��Ȩ֧��ʱ��
	public static final String PAYMNY = "paymny";// ���
	public static final String PK_PROJECTDATA = "pk_projectdata";// ��Ŀ����_����
	public static final String DEF1 = "DEF1";//
	public static final String DEF2 = "DEF2";
	public static final String DEF3 = "DEF3";
	public static final String DEF4 = "DEF4";
	public static final String DEF5 = "DEF5";
	/**
	 * ����
	 */
	public String pk_projectdata_b;
	/**
	 * �ؼ�/��Ȩ֧��ʱ��
	 */
	public UFDate paydate;
	/**
	 * ���
	 */
	public UFDouble paymny;
	/**
	 * �ϲ㵥������
	 */
	public String pk_projectdata;
	/**
	 * ʱ���
	 */
	/**
	 * �Զ�����1
	 */
	public String def1;
	/**
	 *�Զ�����2
	 */
	public String def2;
	/**
	 * �Զ�����3
	 */
	public String def3;
	/**
	 *�Զ�����4
	 */
	public String def4;
	/**
	 * �Զ�����5
	 */
	public String def5;
	public UFDateTime ts;
	
	/**
	 * dr
	 */
	public  Integer dr;    


	    
	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	/**
	 * ���� pk_projectdata_b��Getter����.������������ ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPk_projectdata_b() {
		return this.pk_projectdata_b;
	}

	/**
	 * ����pk_projectdata_b��Setter����.������������ ��������:2019-6-29
	 * 
	 * @param newPk_projectdata_b
	 *            java.lang.String
	 */
	public void setPk_projectdata_b(String pk_projectdata_b) {
		this.pk_projectdata_b = pk_projectdata_b;
	}

	/**
	 * ���� paydate��Getter����.���������ؼ�/��Ȩ֧��ʱ�� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getPaydate() {
		return this.paydate;
	}

	/**
	 * ����paydate��Setter����.���������ؼ�/��Ȩ֧��ʱ�� ��������:2019-6-29
	 * 
	 * @param newPaydate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setPaydate(UFDate paydate) {
		this.paydate = paydate;
	}

	/**
	 * ���� paymny��Getter����.����������� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public UFDouble getPaymny() {
		return this.paymny;
	}

	/**
	 * ����paymny��Setter����.����������� ��������:2019-6-29
	 * 
	 * @param newPaymny
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPaymny(UFDouble paymny) {
		this.paymny = paymny;
	}

	/**
	 * ���� �����ϲ�������Getter����.���������ϲ����� ��������:2019-6-29
	 * 
	 * @return String
	 */
	public String getPk_projectdata() {
		return this.pk_projectdata;
	}

	/**
	 * ���������ϲ�������Setter����.���������ϲ����� ��������:2019-6-29
	 * 
	 * @param newPk_projectdata
	 *            String
	 */
	public void setPk_projectdata(String pk_projectdata) {
		this.pk_projectdata = pk_projectdata;
	}
	


	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2019-6-29
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.ProjectDataBVO");
	}
}
