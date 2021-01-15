package nc.vo.tg.fischeme;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 * �˴�����۵�������Ϣ
 * </p>
 * ��������:2019-6-23
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class FISchemeBVO extends SuperVO {
	/**
	 * �������ȣ����У�
	 */
	private static final long serialVersionUID = -6907939727706828050L;
	public static final String PK_SCHEME_B = "pk_scheme_b";// ���ʷ�����������
	public static final String NOTE = "note";// ���ȱ�ע
	public static final String BRACNCHMEET = "bracnchmeet";// ���зֲ��ϻ�
	public static final String PROVINCEMEET = "provincemeet";// ʡ���ϻ�
	public static final String TOTALBANKMEET = "totalbankmeet";// �����ϻ�
	public static final String REPLY = "reply";// ����
	public static final String CONTRSINGNTIME = "contrsingntime";// ��ͬǩ��ʱ��
	public static final String VBDEF1 = "vbdef1";// �Զ�����1
	public static final String VBDEF2 = "vbdef2";// �Զ�����2
	public static final String VBDEF3 = "vbdef3";// �Զ�����3
	public static final String VBDEF4 = "vbdef4";// �Զ�����4
	public static final String VBDEF5 = "vbdef5";// �Զ�����5
	public static final String VBDEF6 = "vbdef6";// �Զ�����6
	public static final String VBDEF7 = "vbdef7";// �Զ�����7
	public static final String VBDEF8 = "vbdef8";// �Զ�����8
	public static final String VBDEF9 = "vbdef9";// �Զ�����9
	public static final String VBDEF10 = "vbdef10";// �Զ�����10
	public static final String PK_SCHEME = "pk_scheme";// ���ʷ���_����
	public static final String ISDRAWESCHEME = "isdrawescheme";// �Ƿ������
//add by tjl 2019-11-08
	public static final String DEF11 = "def11";
	public static final String DEF12 = "def12";
	public static final String DEF13 = "def13";
	public static final String DEF14 = "def14";
	public static final String DEF15 = "def15";
	public static final String DEF16 = "def16";
	public static final String DEF17 = "def17";
	public static final String DEF18 = "def18";
	public static final String DEF19 = "def19";
	public static final String DEF20 = "def20";
    public static final String DEF21 = "def21";
	public static final String DEF22 = "def22";
	public static final String DEF23 = "def23";
	public static final String DEF24 = "def24";
	public static final String DEF25 = "def25";
	public static final String DEF26 = "def26";
	public static final String DEF27 = "def27";
	public static final String DEF28 = "def28";
	public static final String DEF29 = "def29";
	public static final String DEF30 = "def30";
	public static final String DEF31 = "def31";
	public static final String DEF32 = "def32";
	public static final String DEF33 = "def33";
	public static final String DEF34 = "def34";
	public static final String DEF35 = "def35";
	public static final String DEF36 = "def36";
	public static final String DEF37 = "def37";
	public static final String DEF38 = "def38";
	public static final String DEF39 = "def39";
	public static final String DEF40 = "def40";
	public static final String DEF41 = "def41";
	public static final String DEF42 = "def42";
	public static final String DEF43 = "def43";
	public static final String DEF44 = "def44";
	public static final String DEF45 = "def45";
	public static final String DEF46 = "def46";
	public static final String DEF47 = "def47";
	public static final String DEF48 = "def48";
	public static final String DEF49 = "def49";
	public static final String DEF50 = "def50";
	public static final String DEF51 = "def51";
	public static final String DEF52 = "def52";
	public static final String DEF53 = "def53";
	public static final String DEF54 = "def54";
	public static final String DEF55 = "def55";
	public static final String DEF56 = "def56";
	public static final String DEF57 = "def57";
	public static final String DEF58 = "def58";
	public static final String DEF59 = "def59";
	public static final String DEF60 = "def60";

	/**
	 * ���ʷ�����������
	 */
	public String pk_scheme_b;
	/**
	 * ���ȱ�ע
	 */
	public String note;
	/**
	 * ���зֲ��ϻ�
	 */
	public String bracnchmeet;
	/**
	 * ʡ���ϻ�
	 */
	public String provincemeet;
	/**
	 * �����ϻ�
	 */
	public String totalbankmeet;
	/**
	 * ����
	 */
	public String reply;
	/**
	 * ��ͬǩ��ʱ��
	 */
	public String contrsingntime;
	/**
	 * �Զ�����1
	 */
	public String vbdef1;
	/**
	 * �Զ�����2
	 */
	public String vbdef2;
	/**
	 * �Զ�����3
	 */
	public String vbdef3;
	/**
	 * �Զ�����4
	 */
	public String vbdef4;
	/**
	 * �Զ�����5
	 */
	public String vbdef5;
	/**
	 * �Զ�����6
	 */
	public String vbdef6;
	/**
	 * �Զ�����7
	 */
	public String vbdef7;
	/**
	 * �Զ�����8
	 */
	public String vbdef8;
	/**
	 * �Զ�����9
	 */
	public String vbdef9;
	/**
	 * �Զ�����10
	 */
	public String vbdef10;
	/**
	 * �ϲ㵥������
	 */
	public String pk_scheme;
	/*
	 * �Ƿ������
	 */
	public UFBoolean isdrawescheme;
	/**
	 * ʱ���
	 */
	public UFDateTime ts;
	
	/**
	 * dr
	 */
	public  Integer dr;    

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
	 * �Զ�����21
	 */
	public String def21;
	
	/**
	 * �Զ�����22
	 */
	public String def22;
	/**
	 * �Զ�����23
	 */
	public String def23;
	
	/**
	 * �Զ�����24
	 */
	public String def24;
	/**
	 * �Զ�����25
	 */
	public String def25;
	
	/**
	 * �Զ�����26
	 */
	public String def26;
	/**
	 * �Զ�����27
	 */
	public String def27;
	
	/**
	 * �Զ�����28
	 */
	public String def28;
	/**
	 * �Զ�����29
	 */
	public String def29;
	/**
	 * �Զ�����30
	 */
	public String def30;
	/**
	 * �Զ�����31
	 */
	public String def31;
	/**
	 * �Զ�����32
	 */
	public String def32;
	/**
	 * �Զ�����33
	 */
	public String def33;
	/**
	 * �Զ�����34
	 */
	public String def34;
	/**
	 * �Զ�����35
	 */
	public String def35;
	/**
	 * �Զ�����36
	 */
	public String def36;
	/**
	 * �Զ�����37
	 */
	public String def37;
	/**
	 * �Զ�����38
	 */
	public String def38;
	/**
	 * �Զ�����39
	 */
	public String def39;
	/**
	 * �Զ�����40
	 */
	public String def40;
	/**
	 * �Զ�����41
	 */
	public String def41;
	/**
	 * �Զ�����42
	 */
	public String def42;
	/**
	 * �Զ�����43
	 */
	public String def43;
	/**
	 * �Զ�����44
	 */
	public String def44;
	/**
	 * �Զ�����45
	 */
	public String def45;
	/**
	 * �Զ�����46
	 */
	public String def46;
	/**
	 * �Զ�����47
	 */
	public String def47;
	/**
	 * �Զ�����48
	 */
	public String def48;
	/**
	 * �Զ�����49
	 */
	public String def49;
	/**
	 * �Զ�����50
	 */
	public String def50;
	/**
	 * �Զ�����51
	 */
	public String def51;
	/**
	 * �Զ�����52
	 */
	public String def52;
	/**
	 * �Զ�����53
	 */
	public String def53;
	/**
	 * �Զ�����54
	 */
	public String def54;
	/**
	 * �Զ�����55
	 */
	public String def55;
	/**
	 * �Զ�����56
	 */
	public String def56;
	/**
	 * �Զ�����57
	 */
	public String def57;
	/**
	 * �Զ�����58
	 */
	public String def58;
	/**
	 * �Զ�����59
	 */
	public String def59;
	/**
	 * �Զ�����60
	 */
	public String def60;    
	
	
	
	
	public String getDef11() {
		return def11;
	}

	public void setDef11(String def11) {
		this.def11 = def11;
	}

	public String getDef12() {
		return def12;
	}

	public void setDef12(String def12) {
		this.def12 = def12;
	}

	public String getDef13() {
		return def13;
	}

	public void setDef13(String def13) {
		this.def13 = def13;
	}

	public String getDef14() {
		return def14;
	}

	public void setDef14(String def14) {
		this.def14 = def14;
	}

	public String getDef15() {
		return def15;
	}

	public void setDef15(String def15) {
		this.def15 = def15;
	}

	public String getDef16() {
		return def16;
	}

	public void setDef16(String def16) {
		this.def16 = def16;
	}

	public String getDef17() {
		return def17;
	}

	public void setDef17(String def17) {
		this.def17 = def17;
	}

	public String getDef18() {
		return def18;
	}

	public void setDef18(String def18) {
		this.def18 = def18;
	}

	public String getDef19() {
		return def19;
	}

	public void setDef19(String def19) {
		this.def19 = def19;
	}

	public String getDef20() {
		return def20;
	}

	public void setDef20(String def20) {
		this.def20 = def20;
	}

	public String getDef21() {
		return def21;
	}

	public void setDef21(String def21) {
		this.def21 = def21;
	}

	public String getDef22() {
		return def22;
	}

	public void setDef22(String def22) {
		this.def22 = def22;
	}

	public String getDef23() {
		return def23;
	}

	public void setDef23(String def23) {
		this.def23 = def23;
	}

	public String getDef24() {
		return def24;
	}

	public void setDef24(String def24) {
		this.def24 = def24;
	}

	public String getDef25() {
		return def25;
	}

	public void setDef25(String def25) {
		this.def25 = def25;
	}

	public String getDef26() {
		return def26;
	}

	public void setDef26(String def26) {
		this.def26 = def26;
	}

	public String getDef27() {
		return def27;
	}

	public void setDef27(String def27) {
		this.def27 = def27;
	}

	public String getDef28() {
		return def28;
	}

	public void setDef28(String def28) {
		this.def28 = def28;
	}

	public String getDef29() {
		return def29;
	}

	public void setDef29(String def29) {
		this.def29 = def29;
	}

	public String getDef30() {
		return def30;
	}

	public void setDef30(String def30) {
		this.def30 = def30;
	}

	public String getDef31() {
		return def31;
	}

	public void setDef31(String def31) {
		this.def31 = def31;
	}

	public String getDef32() {
		return def32;
	}

	public void setDef32(String def32) {
		this.def32 = def32;
	}

	public String getDef33() {
		return def33;
	}

	public void setDef33(String def33) {
		this.def33 = def33;
	}

	public String getDef34() {
		return def34;
	}

	public void setDef34(String def34) {
		this.def34 = def34;
	}

	public String getDef35() {
		return def35;
	}

	public void setDef35(String def35) {
		this.def35 = def35;
	}

	public String getDef36() {
		return def36;
	}

	public void setDef36(String def36) {
		this.def36 = def36;
	}

	public String getDef37() {
		return def37;
	}

	public void setDef37(String def37) {
		this.def37 = def37;
	}

	public String getDef38() {
		return def38;
	}

	public void setDef38(String def38) {
		this.def38 = def38;
	}

	public String getDef39() {
		return def39;
	}

	public void setDef39(String def39) {
		this.def39 = def39;
	}

	public String getDef40() {
		return def40;
	}

	public void setDef40(String def40) {
		this.def40 = def40;
	}

	public String getDef41() {
		return def41;
	}

	public void setDef41(String def41) {
		this.def41 = def41;
	}

	public String getDef42() {
		return def42;
	}

	public void setDef42(String def42) {
		this.def42 = def42;
	}

	public String getDef43() {
		return def43;
	}

	public void setDef43(String def43) {
		this.def43 = def43;
	}

	public String getDef44() {
		return def44;
	}

	public void setDef44(String def44) {
		this.def44 = def44;
	}

	public String getDef45() {
		return def45;
	}

	public void setDef45(String def45) {
		this.def45 = def45;
	}

	public String getDef46() {
		return def46;
	}

	public void setDef46(String def46) {
		this.def46 = def46;
	}

	public String getDef47() {
		return def47;
	}

	public void setDef47(String def47) {
		this.def47 = def47;
	}

	public String getDef48() {
		return def48;
	}

	public void setDef48(String def48) {
		this.def48 = def48;
	}

	public String getDef49() {
		return def49;
	}

	public void setDef49(String def49) {
		this.def49 = def49;
	}

	public String getDef50() {
		return def50;
	}

	public void setDef50(String def50) {
		this.def50 = def50;
	}

	public String getDef51() {
		return def51;
	}

	public void setDef51(String def51) {
		this.def51 = def51;
	}

	public String getDef52() {
		return def52;
	}

	public void setDef52(String def52) {
		this.def52 = def52;
	}

	public String getDef53() {
		return def53;
	}

	public void setDef53(String def53) {
		this.def53 = def53;
	}

	public String getDef54() {
		return def54;
	}

	public void setDef54(String def54) {
		this.def54 = def54;
	}

	public String getDef55() {
		return def55;
	}

	public void setDef55(String def55) {
		this.def55 = def55;
	}

	public String getDef56() {
		return def56;
	}

	public void setDef56(String def56) {
		this.def56 = def56;
	}

	public String getDef57() {
		return def57;
	}

	public void setDef57(String def57) {
		this.def57 = def57;
	}

	public String getDef58() {
		return def58;
	}

	public void setDef58(String def58) {
		this.def58 = def58;
	}

	public String getDef59() {
		return def59;
	}

	public void setDef59(String def59) {
		this.def59 = def59;
	}

	public String getDef60() {
		return def60;
	}

	public void setDef60(String def60) {
		this.def60 = def60;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getBracnchmeet() {
		return bracnchmeet;
	}

	public void setBracnchmeet(String bracnchmeet) {
		this.bracnchmeet = bracnchmeet;
	}

	public String getProvincemeet() {
		return provincemeet;
	}

	public void setProvincemeet(String provincemeet) {
		this.provincemeet = provincemeet;
	}

	public String getTotalbankmeet() {
		return totalbankmeet;
	}

	public void setTotalbankmeet(String totalbankmeet) {
		this.totalbankmeet = totalbankmeet;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getContrsingntime() {
		return contrsingntime;
	}

	public void setContrsingntime(String contrsingntime) {
		this.contrsingntime = contrsingntime;
	}

	/**
	 * ���� isdrawescheme��Getter����.�����������ʷ��������Ƿ������ ��������:2019-08-29
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getIsdrawescheme() {
		return isdrawescheme;
	}

	public void setIsdrawescheme(UFBoolean isdrawescheme) {
		this.isdrawescheme = isdrawescheme;
	}

	/**
	 * ���� pk_scheme_b��Getter����.�����������ʷ����������� ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getPk_scheme_b() {
		return this.pk_scheme_b;
	}

	/**
	 * ����pk_scheme_b��Setter����.�����������ʷ����������� ��������:2019-6-23
	 * 
	 * @param newPk_scheme_b
	 *            java.lang.String
	 */
	public void setPk_scheme_b(String pk_scheme_b) {
		this.pk_scheme_b = pk_scheme_b;
	}

	/**
	 * ���� note��Getter����.�����������ȱ�ע ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getNote() {
		return this.note;
	}

	/**
	 * ����note��Setter����.�����������ȱ�ע ��������:2019-6-23
	 * 
	 * @param newNote
	 *            java.lang.String
	 */
	public void setNote(String note) {
		this.note = note;
	}


	/**
	 * ���� vbdef1��Getter����.���������Զ�����1 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef1() {
		return this.vbdef1;
	}

	/**
	 * ����vbdef1��Setter����.���������Զ�����1 ��������:2019-6-23
	 * 
	 * @param newVbdef1
	 *            java.lang.String
	 */
	public void setVbdef1(String vbdef1) {
		this.vbdef1 = vbdef1;
	}

	/**
	 * ���� vbdef2��Getter����.���������Զ�����2 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef2() {
		return this.vbdef2;
	}

	/**
	 * ����vbdef2��Setter����.���������Զ�����2 ��������:2019-6-23
	 * 
	 * @param newVbdef2
	 *            java.lang.String
	 */
	public void setVbdef2(String vbdef2) {
		this.vbdef2 = vbdef2;
	}

	/**
	 * ���� vbdef3��Getter����.���������Զ�����3 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef3() {
		return this.vbdef3;
	}

	/**
	 * ����vbdef3��Setter����.���������Զ�����3 ��������:2019-6-23
	 * 
	 * @param newVbdef3
	 *            java.lang.String
	 */
	public void setVbdef3(String vbdef3) {
		this.vbdef3 = vbdef3;
	}

	/**
	 * ���� vbdef4��Getter����.���������Զ�����4 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef4() {
		return this.vbdef4;
	}

	/**
	 * ����vbdef4��Setter����.���������Զ�����4 ��������:2019-6-23
	 * 
	 * @param newVbdef4
	 *            java.lang.String
	 */
	public void setVbdef4(String vbdef4) {
		this.vbdef4 = vbdef4;
	}

	/**
	 * ���� vbdef5��Getter����.���������Զ�����5 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef5() {
		return this.vbdef5;
	}

	/**
	 * ����vbdef5��Setter����.���������Զ�����5 ��������:2019-6-23
	 * 
	 * @param newVbdef5
	 *            java.lang.String
	 */
	public void setVbdef5(String vbdef5) {
		this.vbdef5 = vbdef5;
	}

	/**
	 * ���� vbdef6��Getter����.���������Զ�����6 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef6() {
		return this.vbdef6;
	}

	/**
	 * ����vbdef6��Setter����.���������Զ�����6 ��������:2019-6-23
	 * 
	 * @param newVbdef6
	 *            java.lang.String
	 */
	public void setVbdef6(String vbdef6) {
		this.vbdef6 = vbdef6;
	}

	/**
	 * ���� vbdef7��Getter����.���������Զ�����7 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef7() {
		return this.vbdef7;
	}

	/**
	 * ����vbdef7��Setter����.���������Զ�����7 ��������:2019-6-23
	 * 
	 * @param newVbdef7
	 *            java.lang.String
	 */
	public void setVbdef7(String vbdef7) {
		this.vbdef7 = vbdef7;
	}

	/**
	 * ���� vbdef8��Getter����.���������Զ�����8 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef8() {
		return this.vbdef8;
	}

	/**
	 * ����vbdef8��Setter����.���������Զ�����8 ��������:2019-6-23
	 * 
	 * @param newVbdef8
	 *            java.lang.String
	 */
	public void setVbdef8(String vbdef8) {
		this.vbdef8 = vbdef8;
	}

	/**
	 * ���� vbdef9��Getter����.���������Զ�����9 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef9() {
		return this.vbdef9;
	}

	/**
	 * ����vbdef9��Setter����.���������Զ�����9 ��������:2019-6-23
	 * 
	 * @param newVbdef9
	 *            java.lang.String
	 */
	public void setVbdef9(String vbdef9) {
		this.vbdef9 = vbdef9;
	}

	/**
	 * ���� vbdef10��Getter����.���������Զ�����10 ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public String getVbdef10() {
		return this.vbdef10;
	}

	/**
	 * ����vbdef10��Setter����.���������Զ�����10 ��������:2019-6-23
	 * 
	 * @param newVbdef10
	 *            java.lang.String
	 */
	public void setVbdef10(String vbdef10) {
		this.vbdef10 = vbdef10;
	}

	/**
	 * ���� �����ϲ�������Getter����.���������ϲ����� ��������:2019-6-23
	 * 
	 * @return String
	 */
	public String getPk_scheme() {
		return this.pk_scheme;
	}

	/**
	 * ���������ϲ�������Setter����.���������ϲ����� ��������:2019-6-23
	 * 
	 * @param newPk_scheme
	 *            String
	 */
	public void setPk_scheme(String pk_scheme) {
		this.pk_scheme = pk_scheme;
	}

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2019-6-23
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2019-6-23
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.tgrz_fischeme_b");
	}
}
