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

public class ProjectDataCVO extends SuperVO {
	public static final String PK_PROJECTDATA_C = "pk_projectdata_c";// ����
	public static final String PERIODIZATIONNAME = "periodizationname";// ��������
	public static final String P6_DATADATE1 = "p6_datadate1";// ��Ŀ��ȡʱ��_p6
	public static final String NC_DATADATE1 = "nc_datadate1";// ��Ŀ��ȡʱ��_nc
	public static final String P6_DATADATE2 = "p6_datadate2";// ��Ӫ������ﵽ_p6
	public static final String NC_DATADATE2 = "nc_datadate2";// ��Ӫ������ﵽ_nc
	public static final String P6_DATADATE3 = "p6_datadate3";// ���������_p6
	public static final String NC_DATADATE3 = "nc_datadate3";// ���������_nc
	public static final String P6_DATADATE4 = "p6_datadate4";// ��������ʹ��֤_p6
	public static final String NC_DATADATE4 = "nc_datadate4";// ��������ʹ��֤_nc
	public static final String P6_DATADATE5 = "p6_datadate5";// �õع滮���֤_p6
	public static final String NC_DATADATE5 = "nc_datadate5";// �õع滮���֤_nc
	public static final String P6_DATADATE6 = "p6_datadate6";// ���蹤�̹滮���֤_p6
	public static final String NC_DATADATE6 = "nc_datadate6";// ���蹤�̹滮���֤_nc
	public static final String P6_DATADATE7 = "p6_datadate7";// ʩ�����֤_p6
	public static final String NC_DATADATE7 = "nc_datadate7";// ʩ�����֤_nc
	public static final String P6_DATADATE8 = "p6_datadate8";// ����ʱ��_p6
	public static final String NC_DATADATE8 = "nc_datadate8";// ����ʱ��_nc
	public static final String P6_DATADATE9 = "p6_datadate9";// ������_p6
	public static final String NC_DATADATE9 = "nc_datadate9";// ������_nc
	public static final String P6_DATADATE10 = "p6_datadate10";// Ԥ��֤_p6
	public static final String NC_DATADATE10 = "nc_datadate10";// Ԥ��֤_nc
	public static final String P6_DATADATE11 = "p6_datadate11";// �ṹ�ⶥ_p6
	public static final String NC_DATADATE11 = "nc_datadate11";// �ṹ�ⶥ_nc
	public static final String P6_DATADATE12 = "p6_datadate12";// ��������_p6
	public static final String NC_DATADATE12 = "nc_datadate12";// ��������_nc
	public static final String P6_DATADATE13 = "p6_datadate13";// ����_p6
	public static final String NC_DATADATE13 = "nc_datadate13";// ����_nc
	public static final String P6_DATADATE14 = "p6_datadate14";// ȷȨ_p6
	public static final String NC_DATADATE14 = "nc_datadate14";// ȷȨ_nc
	public static final String DEF1 = "def1";// �Զ�����1
	public static final String DEF2 = "def2";// �Զ�����2
	public static final String DEF3 = "def3";// �Զ�����3
	public static final String DEF4 = "def4";// �Զ�����4
	public static final String DEF5 = "def5";// �Զ�����5
	public static final String DEF6 = "def6";// �Զ�����6
	public static final String DEF7 = "def7";// �Զ�����7
	public static final String DEF8 = "def8";// �Զ�����8
	public static final String DEF9 = "def9";// �Զ�����9
	public static final String DEF10 = "def10";// �Զ�����10
	public static final String DEF11 = "def11";// �Զ�����11
	public static final String DEF12 = "def12";// �Զ�����12
	public static final String DEF13 = "def13";// �Զ�����13
	public static final String DEF14 = "def14";// �Զ�����14
	public static final String DEF15 = "def15";// �Զ�����15
	public static final String DEF16 = "def16";// �Զ�����16
	public static final String DEF17 = "def17";// �Զ�����17
	public static final String DEF18 = "def18";// �Զ�����18
	public static final String DEF19 = "def19";// �Զ�����19
	public static final String DEF20 = "def20";// �Զ�����20
	public static final String PK_PROJECTDATA = "pk_projectdata";// ��Ŀ����_����

	/**
	 * ����
	 */
	public String pk_projectdata_c;
	/**
	 * ��������
	 */
	public String periodizationname;
	/**
	 * ��Ŀ��ȡʱ��_p6
	 */
	public UFDate p6_datadate1;
	/**
	 * ��Ŀ��ȡʱ��_nc
	 */
	public UFDate nc_datadate1;
	/**
	 * ��Ӫ������ﵽ_p6
	 */
	public UFDate p6_datadate2;
	/**
	 * ��Ӫ������ﵽ_nc
	 */
	public UFDate nc_datadate2;
	/**
	 * ���������_p6
	 */
	public UFDate p6_datadate3;
	/**
	 * ���������_nc
	 */
	public UFDate nc_datadate3;
	/**
	 * ��������ʹ��֤_p6
	 */
	public UFDate p6_datadate4;
	/**
	 * ��������ʹ��֤_nc
	 */
	public UFDate nc_datadate4;
	/**
	 * �õع滮���֤_p6
	 */
	public UFDate p6_datadate5;
	/**
	 * �õع滮���֤_nc
	 */
	public UFDate nc_datadate5;
	/**
	 * ���蹤�̹滮���֤_p6
	 */
	public UFDate p6_datadate6;
	/**
	 * ���蹤�̹滮���֤_nc
	 */
	public UFDate nc_datadate6;
	/**
	 * ʩ�����֤_p6
	 */
	public UFDate p6_datadate7;
	/**
	 * ʩ�����֤_nc
	 */
	public UFDate nc_datadate7;
	/**
	 * ����ʱ��_p6
	 */
	public UFDate p6_datadate8;
	/**
	 * ����ʱ��_nc
	 */
	public UFDate nc_datadate8;
	/**
	 * ������_p6
	 */
	public UFDate p6_datadate9;
	/**
	 * ������_nc
	 */
	public UFDate nc_datadate9;
	/**
	 * Ԥ��֤_p6
	 */
	public UFDate p6_datadate10;
	/**
	 * Ԥ��֤_nc
	 */
	public UFDate nc_datadate10;
	/**
	 * �ṹ�ⶥ_p6
	 */
	public UFDate p6_datadate11;
	/**
	 * �ṹ�ⶥ_nc
	 */
	public UFDate nc_datadate11;
	/**
	 * ��������_p6
	 */
	public UFDate p6_datadate12;
	/**
	 * ��������_nc
	 */
	public UFDate nc_datadate12;
	/**
	 * ����_p6
	 */
	public UFDate p6_datadate13;
	/**
	 * ����_nc
	 */
	public UFDate nc_datadate13;
	/**
	 * ȷȨ_p6
	 */
	public UFDate p6_datadate14;
	/**
	 * ȷȨ_nc
	 */
	public UFDate nc_datadate14;
	/**
	 * �Զ�����1
	 */
	public String def1;
	/**
	 * �Զ�����2
	 */
	public String def2;
	/**
	 * �Զ�����3
	 */
	public String def3;
	/**
	 * �Զ�����4
	 */
	public String def4;
	/**
	 * �Զ�����5
	 */
	public String def5;
	/**
	 * �Զ�����6
	 */
	public String def6;
	/**
	 * �Զ�����7
	 */
	public String def7;
	/**
	 * �Զ�����8
	 */
	public String def8;
	/**
	 * �Զ�����9
	 */
	public String def9;
	/**
	 * �Զ�����10
	 */
	public String def10;
	/**
	 * �Զ�����11
	 */
	public String def11;
	/**
	 * �Զ�����12
	 */
	public String def12;
	/**
	 * �Զ�����13
	 */
	public String def13;
	/**
	 * �Զ�����14
	 */
	public String def14;
	/**
	 * �Զ�����15
	 */
	public String def15;
	/**
	 * �Զ�����16
	 */
	public String def16;
	/**
	 * �Զ�����17
	 */
	public String def17;
	/**
	 * �Զ�����18
	 */
	public String def18;
	/**
	 * �Զ�����19
	 */
	public String def19;
	/**
	 * �Զ�����20
	 */
	public String def20;

	/**
	 * �ϲ㵥������
	 */
	public String pk_projectdata;
	/**
	 * ʱ���
	 */
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
	 * ���� pk_projectdata_c��Getter����.������������ ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPk_projectdata_c() {
		return this.pk_projectdata_c;
	}

	/**
	 * ����pk_projectdata_c��Setter����.������������ ��������:2019-6-29
	 * 
	 * @param newPk_projectdata_c
	 *            java.lang.String
	 */
	public void setPk_projectdata_c(String pk_projectdata_c) {
		this.pk_projectdata_c = pk_projectdata_c;
	}

	/**
	 * ���� periodizationname��Getter����.���������������� ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPeriodizationname() {
		return this.periodizationname;
	}

	/**
	 * ����periodizationname��Setter����.���������������� ��������:2019-6-29
	 * 
	 * @param newPeriodizationname
	 *            java.lang.String
	 */
	public void setPeriodizationname(String periodizationname) {
		this.periodizationname = periodizationname;
	}

	/**
	 * ���� p6_datadate1��Getter����.����������Ŀ��ȡʱ��_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate1() {
		return this.p6_datadate1;
	}

	/**
	 * ����p6_datadate1��Setter����.����������Ŀ��ȡʱ��_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate1
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate1(UFDate p6_datadate1) {
		this.p6_datadate1 = p6_datadate1;
	}

	/**
	 * ���� nc_datadate1��Getter����.����������Ŀ��ȡʱ��_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate1() {
		return this.nc_datadate1;
	}

	/**
	 * ����nc_datadate1��Setter����.����������Ŀ��ȡʱ��_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate1
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate1(UFDate nc_datadate1) {
		this.nc_datadate1 = nc_datadate1;
	}

	/**
	 * ���� p6_datadate2��Getter����.����������Ӫ������ﵽ_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate2() {
		return this.p6_datadate2;
	}

	/**
	 * ����p6_datadate2��Setter����.����������Ӫ������ﵽ_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate2
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate2(UFDate p6_datadate2) {
		this.p6_datadate2 = p6_datadate2;
	}

	/**
	 * ���� nc_datadate2��Getter����.����������Ӫ������ﵽ_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate2() {
		return this.nc_datadate2;
	}

	/**
	 * ����nc_datadate2��Setter����.����������Ӫ������ﵽ_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate2
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate2(UFDate nc_datadate2) {
		this.nc_datadate2 = nc_datadate2;
	}

	/**
	 * ���� p6_datadate3��Getter����.�����������������_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate3() {
		return this.p6_datadate3;
	}

	/**
	 * ����p6_datadate3��Setter����.�����������������_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate3
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate3(UFDate p6_datadate3) {
		this.p6_datadate3 = p6_datadate3;
	}

	/**
	 * ���� nc_datadate3��Getter����.�����������������_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate3() {
		return this.nc_datadate3;
	}

	/**
	 * ����nc_datadate3��Setter����.�����������������_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate3
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate3(UFDate nc_datadate3) {
		this.nc_datadate3 = nc_datadate3;
	}

	/**
	 * ���� p6_datadate4��Getter����.����������������ʹ��֤_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate4() {
		return this.p6_datadate4;
	}

	/**
	 * ����p6_datadate4��Setter����.����������������ʹ��֤_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate4
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate4(UFDate p6_datadate4) {
		this.p6_datadate4 = p6_datadate4;
	}

	/**
	 * ���� nc_datadate4��Getter����.����������������ʹ��֤_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate4() {
		return this.nc_datadate4;
	}

	/**
	 * ����nc_datadate4��Setter����.����������������ʹ��֤_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate4
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate4(UFDate nc_datadate4) {
		this.nc_datadate4 = nc_datadate4;
	}

	/**
	 * ���� p6_datadate5��Getter����.���������õع滮���֤_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate5() {
		return this.p6_datadate5;
	}

	/**
	 * ����p6_datadate5��Setter����.���������õع滮���֤_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate5
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate5(UFDate p6_datadate5) {
		this.p6_datadate5 = p6_datadate5;
	}

	/**
	 * ���� nc_datadate5��Getter����.���������õع滮���֤_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate5() {
		return this.nc_datadate5;
	}

	/**
	 * ����nc_datadate5��Setter����.���������õع滮���֤_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate5
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate5(UFDate nc_datadate5) {
		this.nc_datadate5 = nc_datadate5;
	}

	/**
	 * ���� p6_datadate6��Getter����.�����������蹤�̹滮���֤_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate6() {
		return this.p6_datadate6;
	}

	/**
	 * ����p6_datadate6��Setter����.�����������蹤�̹滮���֤_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate6
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate6(UFDate p6_datadate6) {
		this.p6_datadate6 = p6_datadate6;
	}

	/**
	 * ���� nc_datadate6��Getter����.�����������蹤�̹滮���֤_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate6() {
		return this.nc_datadate6;
	}

	/**
	 * ����nc_datadate6��Setter����.�����������蹤�̹滮���֤_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate6
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate6(UFDate nc_datadate6) {
		this.nc_datadate6 = nc_datadate6;
	}

	/**
	 * ���� p6_datadate7��Getter����.��������ʩ�����֤_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate7() {
		return this.p6_datadate7;
	}

	/**
	 * ����p6_datadate7��Setter����.��������ʩ�����֤_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate7
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate7(UFDate p6_datadate7) {
		this.p6_datadate7 = p6_datadate7;
	}

	/**
	 * ���� nc_datadate7��Getter����.��������ʩ�����֤_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate7() {
		return this.nc_datadate7;
	}

	/**
	 * ����nc_datadate7��Setter����.��������ʩ�����֤_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate7
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate7(UFDate nc_datadate7) {
		this.nc_datadate7 = nc_datadate7;
	}

	/**
	 * ���� p6_datadate8��Getter����.������������ʱ��_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate8() {
		return this.p6_datadate8;
	}

	/**
	 * ����p6_datadate8��Setter����.������������ʱ��_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate8
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate8(UFDate p6_datadate8) {
		this.p6_datadate8 = p6_datadate8;
	}

	/**
	 * ���� nc_datadate8��Getter����.������������ʱ��_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate8() {
		return this.nc_datadate8;
	}

	/**
	 * ����nc_datadate8��Setter����.������������ʱ��_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate8
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate8(UFDate nc_datadate8) {
		this.nc_datadate8 = nc_datadate8;
	}

	/**
	 * ���� p6_datadate9��Getter����.��������������_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate9() {
		return this.p6_datadate9;
	}

	/**
	 * ����p6_datadate9��Setter����.��������������_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate9
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate9(UFDate p6_datadate9) {
		this.p6_datadate9 = p6_datadate9;
	}

	/**
	 * ���� nc_datadate9��Getter����.��������������_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate9() {
		return this.nc_datadate9;
	}

	/**
	 * ����nc_datadate9��Setter����.��������������_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate9
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate9(UFDate nc_datadate9) {
		this.nc_datadate9 = nc_datadate9;
	}

	/**
	 * ���� p6_datadate10��Getter����.��������Ԥ��֤_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate10() {
		return this.p6_datadate10;
	}

	/**
	 * ����p6_datadate10��Setter����.��������Ԥ��֤_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate10
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate10(UFDate p6_datadate10) {
		this.p6_datadate10 = p6_datadate10;
	}

	/**
	 * ���� nc_datadate10��Getter����.��������Ԥ��֤_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate10() {
		return this.nc_datadate10;
	}

	/**
	 * ����nc_datadate10��Setter����.��������Ԥ��֤_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate10
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate10(UFDate nc_datadate10) {
		this.nc_datadate10 = nc_datadate10;
	}

	/**
	 * ���� p6_datadate11��Getter����.���������ṹ�ⶥ_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate11() {
		return this.p6_datadate11;
	}

	/**
	 * ����p6_datadate11��Setter����.���������ṹ�ⶥ_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate11
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate11(UFDate p6_datadate11) {
		this.p6_datadate11 = p6_datadate11;
	}

	/**
	 * ���� nc_datadate11��Getter����.���������ṹ�ⶥ_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate11() {
		return this.nc_datadate11;
	}

	/**
	 * ����nc_datadate11��Setter����.���������ṹ�ⶥ_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate11
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate11(UFDate nc_datadate11) {
		this.nc_datadate11 = nc_datadate11;
	}

	/**
	 * ���� p6_datadate12��Getter����.����������������_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate12() {
		return this.p6_datadate12;
	}

	/**
	 * ����p6_datadate12��Setter����.����������������_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate12
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate12(UFDate p6_datadate12) {
		this.p6_datadate12 = p6_datadate12;
	}

	/**
	 * ���� nc_datadate12��Getter����.����������������_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate12() {
		return this.nc_datadate12;
	}

	/**
	 * ����nc_datadate12��Setter����.����������������_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate12
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate12(UFDate nc_datadate12) {
		this.nc_datadate12 = nc_datadate12;
	}

	/**
	 * ���� p6_datadate13��Getter����.������������_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate13() {
		return this.p6_datadate13;
	}

	/**
	 * ����p6_datadate13��Setter����.������������_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate13
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate13(UFDate p6_datadate13) {
		this.p6_datadate13 = p6_datadate13;
	}

	/**
	 * ���� nc_datadate13��Getter����.������������_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate13() {
		return this.nc_datadate13;
	}

	/**
	 * ����nc_datadate13��Setter����.������������_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate13
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate13(UFDate nc_datadate13) {
		this.nc_datadate13 = nc_datadate13;
	}

	/**
	 * ���� p6_datadate14��Getter����.��������ȷȨ_p6 ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate14() {
		return this.p6_datadate14;
	}

	/**
	 * ����p6_datadate14��Setter����.��������ȷȨ_p6 ��������:2019-6-29
	 * 
	 * @param newP6_datadate14
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate14(UFDate p6_datadate14) {
		this.p6_datadate14 = p6_datadate14;
	}

	/**
	 * ���� nc_datadate14��Getter����.��������ȷȨ_nc ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate14() {
		return this.nc_datadate14;
	}

	/**
	 * ����nc_datadate14��Setter����.��������ȷȨ_nc ��������:2019-6-29
	 * 
	 * @param newNc_datadate14
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate14(UFDate nc_datadate14) {
		this.nc_datadate14 = nc_datadate14;
	}

	/**
	 * ���� def1��Getter����.���������Զ�����1 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef1() {
		return this.def1;
	}

	/**
	 * ����def1��Setter����.���������Զ�����1 ��������:2019-6-29
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(String def1) {
		this.def1 = def1;
	}

	/**
	 * ���� def2��Getter����.���������Զ�����2 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef2() {
		return this.def2;
	}

	/**
	 * ����def2��Setter����.���������Զ�����2 ��������:2019-6-29
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(String def2) {
		this.def2 = def2;
	}

	/**
	 * ���� def3��Getter����.���������Զ�����3 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef3() {
		return this.def3;
	}

	/**
	 * ����def3��Setter����.���������Զ�����3 ��������:2019-6-29
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(String def3) {
		this.def3 = def3;
	}

	/**
	 * ���� def4��Getter����.���������Զ�����4 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef4() {
		return this.def4;
	}

	/**
	 * ����def4��Setter����.���������Զ�����4 ��������:2019-6-29
	 * 
	 * @param newDef4
	 *            java.lang.String
	 */
	public void setDef4(String def4) {
		this.def4 = def4;
	}

	/**
	 * ���� def5��Getter����.���������Զ�����5 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef5() {
		return this.def5;
	}

	/**
	 * ����def5��Setter����.���������Զ�����5 ��������:2019-6-29
	 * 
	 * @param newDef5
	 *            java.lang.String
	 */
	public void setDef5(String def5) {
		this.def5 = def5;
	}

	/**
	 * ���� def6��Getter����.���������Զ�����6 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef6() {
		return this.def6;
	}

	/**
	 * ����def6��Setter����.���������Զ�����6 ��������:2019-6-29
	 * 
	 * @param newDef6
	 *            java.lang.String
	 */
	public void setDef6(String def6) {
		this.def6 = def6;
	}

	/**
	 * ���� def7��Getter����.���������Զ�����7 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef7() {
		return this.def7;
	}

	/**
	 * ����def7��Setter����.���������Զ�����7 ��������:2019-6-29
	 * 
	 * @param newDef7
	 *            java.lang.String
	 */
	public void setDef7(String def7) {
		this.def7 = def7;
	}

	/**
	 * ���� def8��Getter����.���������Զ�����8 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef8() {
		return this.def8;
	}

	/**
	 * ����def8��Setter����.���������Զ�����8 ��������:2019-6-29
	 * 
	 * @param newDef8
	 *            java.lang.String
	 */
	public void setDef8(String def8) {
		this.def8 = def8;
	}

	/**
	 * ���� def9��Getter����.���������Զ�����9 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef9() {
		return this.def9;
	}

	/**
	 * ����def9��Setter����.���������Զ�����9 ��������:2019-6-29
	 * 
	 * @param newDef9
	 *            java.lang.String
	 */
	public void setDef9(String def9) {
		this.def9 = def9;
	}

	/**
	 * ���� def10��Getter����.���������Զ�����10 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef10() {
		return this.def10;
	}

	/**
	 * ����def10��Setter����.���������Զ�����10 ��������:2019-6-29
	 * 
	 * @param newDef10
	 *            java.lang.String
	 */
	public void setDef10(String def10) {
		this.def10 = def10;
	}

	/**
	 * ���� def11��Getter����.���������Զ�����11 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef11() {
		return this.def11;
	}

	/**
	 * ����def11��Setter����.���������Զ�����11 ��������:2019-6-29
	 * 
	 * @param newDef11
	 *            java.lang.String
	 */
	public void setDef11(String def11) {
		this.def11 = def11;
	}

	/**
	 * ���� def12��Getter����.���������Զ�����12 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef12() {
		return this.def12;
	}

	/**
	 * ����def12��Setter����.���������Զ�����12 ��������:2019-6-29
	 * 
	 * @param newDef12
	 *            java.lang.String
	 */
	public void setDef12(String def12) {
		this.def12 = def12;
	}

	/**
	 * ���� def13��Getter����.���������Զ�����13 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef13() {
		return this.def13;
	}

	/**
	 * ����def13��Setter����.���������Զ�����13 ��������:2019-6-29
	 * 
	 * @param newDef13
	 *            java.lang.String
	 */
	public void setDef13(String def13) {
		this.def13 = def13;
	}

	/**
	 * ���� def14��Getter����.���������Զ�����14 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef14() {
		return this.def14;
	}

	/**
	 * ����def14��Setter����.���������Զ�����14 ��������:2019-6-29
	 * 
	 * @param newDef14
	 *            java.lang.String
	 */
	public void setDef14(String def14) {
		this.def14 = def14;
	}

	/**
	 * ���� def15��Getter����.���������Զ�����15 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef15() {
		return this.def15;
	}

	/**
	 * ����def15��Setter����.���������Զ�����15 ��������:2019-6-29
	 * 
	 * @param newDef15
	 *            java.lang.String
	 */
	public void setDef15(String def15) {
		this.def15 = def15;
	}

	/**
	 * ���� def16��Getter����.���������Զ�����16 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef16() {
		return this.def16;
	}

	/**
	 * ����def16��Setter����.���������Զ�����16 ��������:2019-6-29
	 * 
	 * @param newDef16
	 *            java.lang.String
	 */
	public void setDef16(String def16) {
		this.def16 = def16;
	}

	/**
	 * ���� def17��Getter����.���������Զ�����17 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef17() {
		return this.def17;
	}

	/**
	 * ����def17��Setter����.���������Զ�����17 ��������:2019-6-29
	 * 
	 * @param newDef17
	 *            java.lang.String
	 */
	public void setDef17(String def17) {
		this.def17 = def17;
	}

	/**
	 * ���� def18��Getter����.���������Զ�����18 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef18() {
		return this.def18;
	}

	/**
	 * ����def18��Setter����.���������Զ�����18 ��������:2019-6-29
	 * 
	 * @param newDef18
	 *            java.lang.String
	 */
	public void setDef18(String def18) {
		this.def18 = def18;
	}

	/**
	 * ���� def19��Getter����.���������Զ�����19 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef19() {
		return this.def19;
	}

	/**
	 * ����def19��Setter����.���������Զ�����19 ��������:2019-6-29
	 * 
	 * @param newDef19
	 *            java.lang.String
	 */
	public void setDef19(String def19) {
		this.def19 = def19;
	}

	/**
	 * ���� def20��Getter����.���������Զ�����20 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef20() {
		return this.def20;
	}

	/**
	 * ����def20��Setter����.���������Զ�����20 ��������:2019-6-29
	 * 
	 * @param newDef20
	 *            java.lang.String
	 */
	public void setDef20(String def20) {
		this.def20 = def20;
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
		return VOMetaFactory.getInstance().getVOMeta("tg.ProjectDataCVO");
	}
}
