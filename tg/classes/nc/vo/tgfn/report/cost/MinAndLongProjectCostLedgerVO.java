package nc.vo.tgfn.report.cost;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * �г�����Ŀ�ɱ�̨�ˣ��ɱ�����
 * 
 * ljf 2020_05_21 �ɱ�̨�˱���
 * 
 * @author ASUS
 * 
 */
public class MinAndLongProjectCostLedgerVO extends SuperVO implements Cloneable {

	private String pk_costleder;
	private String pk_fct_ap;// ��ͬ����
	private String pk_project;// ��Ŀ����
	private String innercode;// �ڱ���
	private String projectname;
	private String vclass;// ����
	private String contcode;// ��ͬ����
	private String contname;// ��ͬ����
	private String tatalcontcode;// �ܰ���ͬ����
	private String tatalcontname;// �ܰ���ͬ����
	private String conttype;// ��ͬ���
	private String contattribute;// ��ͬ����
	private String pk_first;// �׷���λ
	private String firstname;
	private String pk_second;// �ҷ���λ
	private String secondname;
	private String signdate;// ǩԼ����
	private String settstate;// ����״̬
	private UFDouble amount1;// ǩԼ���
	private UFDouble amount2;// �ۼƱ�����
	private UFDouble amount3;// �ۼƲ���Э����
	private UFDouble amount4;// ����������
	private UFDouble amount5;// ��̬����˰��
	private UFDouble rate1;// ˰��
	private UFDouble amount6;// ��̬������˰��
	private UFDouble amount7;// ��̬��˰�
	private UFDouble amount8;// ������
	private UFDouble amount9;// �ۼ�Ӧ����
	private UFDouble amount10;// �ۼ�Ӧ��δ����
	private UFDouble amount11;// �ۼ�ʵ�����˰��
	private UFDouble amount12;// �ۼ�ʵ�������˰��
	private UFDouble amount13;//
	private UFDouble amount14;// �ۼ�ʵ���˰�
	private UFDouble amount15;// ?��ͬδ�����˰��
	private UFDouble amount16;// ?��ͬδ�������˰��
	private UFDouble amount17;// ��ͬδ���� ��˰�
	private UFDouble amount18;//
	private UFDouble amount19;// �ۼ�ʵ����-NC
	private UFDouble amount20;// �ۼ�ʵ����-NC������˰��
	private UFDouble amount21;// �ۼ�ʵ����-NC��˰�
	private UFDouble amount22;// ��������
	private UFDouble amount23;// nc���˷�Ʊ����˰��
	private UFDouble amount24;// ����ɱ�������˰��
	private UFDouble amount25;// �ɵֿ۽���˰��˰�
	private UFDouble amount26;// δ��ɱ�����˰��
	private UFDouble amount27;// δ��ɱ�������˰��
	private UFDouble amount28;// δ��ɱ���˰�
	private UFDouble amount29;// �ۼƲ�ֵ����˰��
	private UFDouble amount30;// �ۼƲ�ֵ������˰��
	private UFDouble amount31;// �ۼƲ�ֵ��˰�
	private UFDouble amount32;// ���ղ�ֵ
	private UFDouble amount33;// �ۼ� ��ֵδ���� ����˰��
	private UFDouble amount34;// �ۼƲ�ֵδ���� ������˰��
	private UFDouble amount35;// �ۼƲ�ֵδ���� ��˰�
	private String pk_dept;// ����
	private String pk_psndoc;// ������
	private String pk_mode;// �а���ʽ
	private String modename;// �а���ʽ
	private String pk_format;// ҵ̬
	private String formatname;// ҵ̬����
	private UFDouble rate2;// ҵ̬����
	
	//add by �ƹڻ�  ������ʷ��úͿ�����ӷѵ�������� 20200813 begin
	private UFDouble amt36;// ������ӷ�
	private UFDouble amt37;// ���ʷ���
	//add by �ƹڻ�  ������ʷ��úͿ�����ӷѵ�������� 20200813 end

	public UFDouble getAmt36() {
		return amt36;
	}

	public void setAmt36(UFDouble amt36) {
		this.amt36 = amt36;
	}

	public UFDouble getAmt37() {
		return amt37;
	}

	public void setAmt37(UFDouble amt37) {
		this.amt37 = amt37;
	}
	
	
	public String getInnercode() {
		return innercode;
	}

	public void setInnercode(String innercode) {
		this.innercode = innercode;
	}

	public String getTatalcontcode() {
		return tatalcontcode;
	}

	public void setTatalcontcode(String tatalcontcode) {
		this.tatalcontcode = tatalcontcode;
	}

	public String getTatalcontname() {
		return tatalcontname;
	}

	public void setTatalcontname(String tatalcontname) {
		this.tatalcontname = tatalcontname;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSecondname() {
		return secondname;
	}

	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}

	public String getModename() {
		return modename;
	}

	public void setModename(String modename) {
		this.modename = modename;
	}

	public String getFormatname() {
		return formatname;
	}

	public void setFormatname(String formatname) {
		this.formatname = formatname;
	}

	public String getPk_fct_ap() {
		return pk_fct_ap;
	}

	public void setPk_fct_ap(String pk_fct_ap) {
		this.pk_fct_ap = pk_fct_ap;
	}

	public String getPk_project() {
		return pk_project;
	}

	public void setPk_project(String pk_project) {
		this.pk_project = pk_project;
	}

	public String getVclass() {
		return vclass;
	}

	public void setVclass(String vclass) {
		this.vclass = vclass;
	}

	public String getContcode() {
		return contcode;
	}

	public void setContcode(String contcode) {
		this.contcode = contcode;
	}

	public String getContname() {
		return contname;
	}

	public void setContname(String contname) {
		this.contname = contname;
	}

	public String getConttype() {
		return conttype;
	}

	public void setConttype(String conttype) {
		this.conttype = conttype;
	}

	public String getContattribute() {
		return contattribute;
	}

	public void setContattribute(String contattribute) {
		this.contattribute = contattribute;
	}

	public String getPk_first() {
		return pk_first;
	}

	public void setPk_first(String pk_first) {
		this.pk_first = pk_first;
	}

	public String getPk_second() {
		return pk_second;
	}

	public void setPk_second(String pk_second) {
		this.pk_second = pk_second;
	}

	public String getSigndate() {
		return signdate;
	}

	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}

	public String getSettstate() {
		return settstate;
	}

	public void setSettstate(String settstate) {
		this.settstate = settstate;
	}

	public UFDouble getAmount1() {
		return amount1;
	}

	public void setAmount1(UFDouble amount1) {
		this.amount1 = amount1;
	}

	public UFDouble getAmount2() {
		return amount2;
	}

	public void setAmount2(UFDouble amount2) {
		this.amount2 = amount2;
	}

	public UFDouble getAmount3() {
		return amount3;
	}

	public void setAmount3(UFDouble amount3) {
		this.amount3 = amount3;
	}

	public UFDouble getAmount4() {
		return amount4;
	}

	public void setAmount4(UFDouble amount4) {
		this.amount4 = amount4;
	}

	public UFDouble getAmount5() {
		return amount5;
	}

	public void setAmount5(UFDouble amount5) {
		this.amount5 = amount5;
	}

	public UFDouble getRate1() {
		return rate1;
	}

	public void setRate1(UFDouble rate1) {
		this.rate1 = rate1;
	}

	public UFDouble getAmount6() {
		return amount6;
	}

	public void setAmount6(UFDouble amount6) {
		this.amount6 = amount6;
	}

	public UFDouble getAmount7() {
		return amount7;
	}

	public void setAmount7(UFDouble amount7) {
		this.amount7 = amount7;
	}

	public UFDouble getAmount8() {
		return amount8;
	}

	public void setAmount8(UFDouble amount8) {
		this.amount8 = amount8;
	}

	public UFDouble getAmount9() {
		return amount9;
	}

	public void setAmount9(UFDouble amount9) {
		this.amount9 = amount9;
	}

	public UFDouble getAmount10() {
		return amount10;
	}

	public void setAmount10(UFDouble amount10) {
		this.amount10 = amount10;
	}

	public UFDouble getAmount11() {
		return amount11;
	}

	public void setAmount11(UFDouble amount11) {
		this.amount11 = amount11;
	}

	public UFDouble getAmount12() {
		return amount12;
	}

	public void setAmount12(UFDouble amount12) {
		this.amount12 = amount12;
	}

	public UFDouble getAmount13() {
		return amount13;
	}

	public void setAmount13(UFDouble amount13) {
		this.amount13 = amount13;
	}

	public UFDouble getAmount14() {
		return amount14;
	}

	public void setAmount14(UFDouble amount14) {
		this.amount14 = amount14;
	}

	public UFDouble getAmount15() {
		return amount15;
	}

	public void setAmount15(UFDouble amount15) {
		this.amount15 = amount15;
	}

	public UFDouble getAmount16() {
		return amount16;
	}

	public void setAmount16(UFDouble amount16) {
		this.amount16 = amount16;
	}

	public UFDouble getAmount17() {
		return amount17;
	}

	public void setAmount17(UFDouble amount17) {
		this.amount17 = amount17;
	}

	public UFDouble getAmount18() {
		return amount18;
	}

	public void setAmount18(UFDouble amount18) {
		this.amount18 = amount18;
	}

	public UFDouble getAmount19() {
		return amount19;
	}

	public void setAmount19(UFDouble amount19) {
		this.amount19 = amount19;
	}

	public UFDouble getAmount20() {
		return amount20;
	}

	public void setAmount20(UFDouble amount20) {
		this.amount20 = amount20;
	}

	public UFDouble getAmount21() {
		return amount21;
	}

	public void setAmount21(UFDouble amount21) {
		this.amount21 = amount21;
	}

	public UFDouble getAmount22() {
		return amount22;
	}

	public void setAmount22(UFDouble amount22) {
		this.amount22 = amount22;
	}

	public UFDouble getAmount23() {
		return amount23;
	}

	public void setAmount23(UFDouble amount23) {
		this.amount23 = amount23;
	}

	public UFDouble getAmount24() {
		return amount24;
	}

	public void setAmount24(UFDouble amount24) {
		this.amount24 = amount24;
	}

	public UFDouble getAmount35() {
		return amount35;
	}

	public void setAmount35(UFDouble amount35) {
		this.amount35 = amount35;
	}

	public UFDouble getAmount25() {
		return amount25;
	}

	public void setAmount25(UFDouble amount25) {
		this.amount25 = amount25;
	}

	public UFDouble getAmount26() {
		return amount26;
	}

	public void setAmount26(UFDouble amount26) {
		this.amount26 = amount26;
	}

	public UFDouble getAmount27() {
		return amount27;
	}

	public void setAmount27(UFDouble amount27) {
		this.amount27 = amount27;
	}

	public UFDouble getAmount28() {
		return amount28;
	}

	public void setAmount28(UFDouble amount28) {
		this.amount28 = amount28;
	}

	public UFDouble getAmount29() {
		return amount29;
	}

	public void setAmount29(UFDouble amount29) {
		this.amount29 = amount29;
	}

	public UFDouble getAmount30() {
		return amount30;
	}

	public void setAmount30(UFDouble amount30) {
		this.amount30 = amount30;
	}

	public UFDouble getAmount31() {
		return amount31;
	}

	public void setAmount31(UFDouble amount31) {
		this.amount31 = amount31;
	}

	public UFDouble getAmount32() {
		return amount32;
	}

	public void setAmount32(UFDouble amount32) {
		this.amount32 = amount32;
	}

	public UFDouble getAmount33() {
		return amount33;
	}

	public void setAmount33(UFDouble amount33) {
		this.amount33 = amount33;
	}

	public UFDouble getAmount34() {
		return amount34;
	}

	public void setAmount34(UFDouble amount34) {
		this.amount34 = amount34;
	}

	public String getPk_dept() {
		return pk_dept;
	}

	public void setPk_dept(String pk_dept) {
		this.pk_dept = pk_dept;
	}

	public String getPk_psndoc() {
		return pk_psndoc;
	}

	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	public String getPk_mode() {
		return pk_mode;
	}

	public void setPk_mode(String pk_mode) {
		this.pk_mode = pk_mode;
	}

	public String getPk_format() {
		return pk_format;
	}

	public void setPk_format(String pk_format) {
		this.pk_format = pk_format;
	}

	public UFDouble getRate2() {
		return rate2;
	}

	public void setRate2(UFDouble rate2) {
		this.rate2 = rate2;
	}

	public String getPk_costleder() {
		return pk_costleder;
	}

	public void setPk_costleder(String pk_costleder) {
		this.pk_costleder = pk_costleder;
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "temp_tg_costleder";
	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_costleder";
	}
}
