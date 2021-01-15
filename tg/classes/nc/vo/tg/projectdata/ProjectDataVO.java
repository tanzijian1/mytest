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

public class ProjectDataVO extends SuperVO {
	public static final String PK_PROJECTDATA = "pk_projectdata";// ����
	public static final String PK_GROUP = "pk_group";// ����
	public static final String PK_ORG = "pk_org";// ��֯
	public static final String PK_ORG_V = "pk_org_v";// ��֯�汾
	public static final String CODE = "code";// ����
	public static final String NAME = "name";// ����
	public static final String PROJECTCORP = "projectcorp";// ��Ŀ���ڹ�˾
	public static final String SRCID = "srcid";// ��Դ��Ŀ����
	public static final String SRCSYSTEM = "srcsystem";// ��Դϵͳ
	public static final String PROJECTAREA = "projectarea";// ��Ŀ��������
	public static final String PERIODIZATIONNAME = "periodizationname";// ��������
	public static final String PROJECTTYPE = "projecttype";// ��Ŀ����
	public static final String P6_DATADATE1 = "p6_datadate1";// ��Ŀ��ȡʱ��_p6
	public static final String P6_DATADATE2 = "p6_datadate2";// ��Ӫ������ﵽ_p6
	public static final String P6_DATADATE3 = "p6_datadate3";// ���������_p6
	public static final String P6_DATADATE4 = "p6_datadate4";// ��������ʹ��֤_p6
	public static final String P6_DATADATE5 = "p6_datadate5";// �õع滮���֤_p6
	public static final String P6_DATADATE6 = "p6_datadate6";// ���蹤�̹滮���֤_p6
	public static final String P6_DATADATE7 = "p6_datadate7";// ʩ�����֤_p6
	public static final String P6_DATADATE8 = "p6_datadate8";// ����ʱ��_p6
	public static final String P6_DATADATE9 = "p6_datadate9";// ������_p6
	public static final String P6_DATADATE10 = "p6_datadate10";// Ԥ��֤_p6
	public static final String P6_DATADATE11 = "p6_datadate11";// �ṹ�ⶥ_p6
	public static final String P6_DATADATE12 = "p6_datadate12";// ��������_p6
	public static final String P6_DATADATE13 = "p6_datadate13";// ����_p6
	public static final String P6_DATADATE14 = "p6_datadate14";// ȷȨ_p6
	public static final String NC_DATADATE1 = "nc_datadate1";// ��Ŀ��ȡʱ��_nc
	public static final String NC_DATADATE2 = "nc_datadate2";// ��Ӫ������ﵽ_nc
	public static final String NC_DATADATE3 = "nc_datadate3";// ���������_nc
	public static final String NC_DATADATE4 = "nc_datadate4";// ��������ʹ��֤_nc
	public static final String NC_DATADATE5 = "nc_datadate5";// �õع滮���֤_nc
	public static final String NC_DATADATE6 = "nc_datadate6";// ���蹤�̹滮���֤_nc
	public static final String NC_DATADATE7 = "nc_datadate7";// ʩ�����֤_nc
	public static final String NC_DATADATE8 = "nc_datadate8";// ����ʱ��_nc
	public static final String NC_DATADATE9 = "nc_datadate9";// ������_nc
	public static final String NC_DATADATE10 = "nc_datadate10";// Ԥ��֤_nc
	public static final String NC_DATADATE11 = "nc_datadate11";// �ṹ�ⶥ_nc
	public static final String NC_DATADATE12 = "nc_datadate12";// ��������_nc
	public static final String NC_DATADATE13 = "nc_datadate13";// ����_nc
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
	public static final String DEF21 = "def21";// �Զ�����21
	public static final String DEF22 = "def22";// �Զ�����22
	public static final String DEF23 = "def23";// �Զ�����23
	public static final String DEF24 = "def24";// �Զ�����24
	public static final String DEF25 = "def25";// �Զ�����25
	public static final String DEF26 = "def26";// �Զ�����26
	public static final String DEF27 = "def27";// �Զ�����27
	public static final String DEF28 = "def28";// �Զ�����28
	public static final String DEF29 = "def29";// �Զ�����29
	public static final String DEF30 = "def30";// �Զ�����30
	
	public static final String DEF31 = "def31";// �Զ�����31
	public static final String DEF32 = "def32";// �Զ�����32
	public static final String DEF33 = "def33";// �Զ�����33
	public static final String DEF34 = "def34";// �Զ�����34
	public static final String DEF35 = "def35";// �Զ�����35
	public static final String DEF36 = "def36";// �Զ�����36
	public static final String DEF37 = "def37";// �Զ�����37
	public static final String DEF38 = "def38";// �Զ�����38
	public static final String DEF39 = "def39";// �Զ�����39
	public static final String DEF40 = "def40";// �Զ�����40
	public static final String DEF41 = "def41";// �Զ�����41
	public static final String DEF42 = "def42";// �Զ�����42
	public static final String DEF43 = "def43";// �Զ�����43
	public static final String DEF44 = "def44";// �Զ�����44
	public static final String DEF45 = "def45";// �Զ�����45
	public static final String DEF46 = "def46";// �Զ�����46
	public static final String DEF47 = "def47";// �Զ�����47
	public static final String DEF48 = "def48";// �Զ�����48
	public static final String DEF49 = "def49";// �Զ�����49
	public static final String DEF50 = "def50";// �Զ�����50
	public static final String DEF51 = "def51";// �Զ�����51
	public static final String DEF52 = "def52";// �Զ�����52
	public static final String DEF53 = "def53";// �Զ�����53
	public static final String DEF54 = "def54";// �Զ�����54
	public static final String DEF55 = "def55";// �Զ�����55
	public static final String DEF56 = "def56";// �Զ�����56
	public static final String DEF57 = "def57";// �Զ�����57
	public static final String DEF58 = "def58";// �Զ�����58
	public static final String DEF59 = "def59";// �Զ�����59
	public static final String DEF60 = "def60";// �Զ�����60
	public static final String DEF61 = "def61";// �Զ�����61
	public static final String DEF62 = "def62";// �Զ�����62
	public static final String DEF63 = "def63";// �Զ�����63
	public static final String DEF64 = "def64";// �Զ�����64
	public static final String DEF65 = "def65";// �Զ�����65
	public static final String DEF66 = "def66";// �Զ�����66
	public static final String DEF67 = "def67";// �Զ�����67
	public static final String DEF68 = "def68";// �Զ�����68
	public static final String DEF69 = "def69";// �Զ�����69
	public static final String DEF70 = "def70";// �Զ�����70
	public static final String DEF71 = "def71";// �Զ�����71
	public static final String DEF72 = "def72";// �Զ�����72
	public static final String DEF73 = "def73";// �Զ�����73
	public static final String DEF74 = "def74";// �Զ�����74
	public static final String DEF75 = "def75";// �Զ�����75
	public static final String DEF76 = "def76";// �Զ�����76
	public static final String DEF77 = "def77";// �Զ�����77
	public static final String DEF78 = "def78";// �Զ�����78
	public static final String DEF79 = "def79";// �Զ�����79
	public static final String DEF80 = "def80";// �Զ�����80
	public static final String DEF81 = "def81";// �Զ�����81
	public static final String DEF82 = "def82";// �Զ�����82
	public static final String DEF83 = "def83";// �Զ�����83
	public static final String DEF84 = "def84";// �Զ�����84
	public static final String DEF85 = "def85";// �Զ�����85
	public static final String DEF86 = "def86";// �Զ�����86
	public static final String DEF87 = "def87";// �Զ�����87
	public static final String DEF88 = "def88";// �Զ�����88
	public static final String DEF89 = "def89";// �Զ�����89
	public static final String DEF90 = "def90";// �Զ�����90
	public static final String DEF91 = "def91";// �Զ�����91
	public static final String DEF92 = "def92";// �Զ�����92
	public static final String DEF93 = "def93";// �Զ�����93
	public static final String DEF94 = "def94";// �Զ�����94
	public static final String DEF95 = "def95";// �Զ�����95
	public static final String DEF96 = "def96";// �Զ�����96
	public static final String DEF97 = "def97";// �Զ�����97
	public static final String DEF98 = "def98";// �Զ�����98
	public static final String DEF99 = "def99";// �Զ�����99

	public static final String BILLDATE = "billdate";// ҵ������
	public static final String CREATOR = "creator";// ������
	public static final String CREATIONTIME = "creationtime";// ����ʱ��
	public static final String MODIFIER = "modifier";// �޸���
	public static final String MODIFIEDTIME = "modifiedtime";// �޸�ʱ��
	/**
	*�Զ�����31
	*/
	public java.lang.String def31;
	/**
	*�Զ�����32
	*/
	public java.lang.String def32;
	/**
	*�Զ�����33
	*/
	public java.lang.String def33;
	/**
	*�Զ�����34
	*/
	public java.lang.String def34;
	/**
	*�Զ�����35
	*/
	public java.lang.String def35;
	/**
	*�Զ�����36
	*/
	public java.lang.String def36;
	/**
	*�Զ�����37
	*/
	public java.lang.String def37;
	/**
	*�Զ�����38
	*/
	public java.lang.String def38;
	/**
	*�Զ�����39
	*/
	public java.lang.String def39;
	/**
	*�Զ�����40
	*/
	public java.lang.String def40;
	/**
	*�Զ�����41
	*/
	public java.lang.String def41;
	/**
	*�Զ�����42
	*/
	public java.lang.String def42;
	/**
	*�Զ�����43
	*/
	public java.lang.String def43;
	/**
	*�Զ�����44
	*/
	public java.lang.String def44;
	/**
	*�Զ�����45
	*/
	public java.lang.String def45;
	/**
	*�Զ�����46
	*/
	public java.lang.String def46;
	/**
	*�Զ�����47
	*/
	public java.lang.String def47;
	/**
	*�Զ�����48
	*/
	public java.lang.String def48;
	/**
	*�Զ�����49
	*/
	public java.lang.String def49;
	/**
	*�Զ�����50
	*/
	public java.lang.String def50;
	/**
	*�Զ�����51
	*/
	public java.lang.String def51;
	/**
	*�Զ�����52
	*/
	public java.lang.String def52;
	/**
	*�Զ�����53
	*/
	public java.lang.String def53;
	/**
	*�Զ�����54
	*/
	public java.lang.String def54;
	/**
	*�Զ�����55
	*/
	public java.lang.String def55;
	/**
	*�Զ�����56
	*/
	public java.lang.String def56;
	/**
	*�Զ�����57
	*/
	public java.lang.String def57;
	/**
	*�Զ�����58
	*/
	public java.lang.String def58;
	/**
	*�Զ�����59
	*/
	public java.lang.String def59;
	/**
	*�Զ�����60
	*/
	public java.lang.String def60;
	/**
	*�Զ�����61
	*/
	public java.lang.String def61;
	/**
	*�Զ�����62
	*/
	public java.lang.String def62;
	/**
	*�Զ�����63
	*/
	public java.lang.String def63;
	/**
	*�Զ�����64
	*/
	public java.lang.String def64;
	/**
	*�Զ�����65
	*/
	public java.lang.String def65;
	/**
	*�Զ�����66
	*/
	public java.lang.String def66;
	/**
	*�Զ�����67
	*/
	public java.lang.String def67;
	/**
	*�Զ�����68
	*/
	public java.lang.String def68;
	/**
	*�Զ�����69
	*/
	public java.lang.String def69;
	/**
	*�Զ�����70
	*/
	public java.lang.String def70;
	/**
	*�Զ�����71
	*/
	public java.lang.String def71;
	/**
	*�Զ�����72
	*/
	public java.lang.String def72;
	/**
	*�Զ�����73
	*/
	public java.lang.String def73;
	/**
	*�Զ�����74
	*/
	public java.lang.String def74;
	/**
	*�Զ�����75
	*/
	public java.lang.String def75;
	/**
	*�Զ�����76
	*/
	public java.lang.String def76;
	/**
	*�Զ�����77
	*/
	public java.lang.String def77;
	/**
	*�Զ�����78
	*/
	public java.lang.String def78;
	/**
	*�Զ�����79
	*/
	public java.lang.String def79;
	/**
	*�Զ�����80
	*/
	public java.lang.String def80;
	/**
	*�Զ�����81
	*/
	public java.lang.String def81;
	/**
	*�Զ�����82
	*/
	public java.lang.String def82;
	/**
	*�Զ�����83
	*/
	public java.lang.String def83;
	/**
	*�Զ�����84
	*/
	public java.lang.String def84;
	/**
	*�Զ�����85
	*/
	public java.lang.String def85;
	/**
	*�Զ�����86
	*/
	public java.lang.String def86;
	/**
	*�Զ�����87
	*/
	public java.lang.String def87;
	/**
	*�Զ�����88
	*/
	public java.lang.String def88;
	/**
	*�Զ�����89
	*/
	public java.lang.String def89;
	/**
	*�Զ�����90
	*/
	public java.lang.String def90;
	/**
	*�Զ�����91
	*/
	public java.lang.String def91;
	/**
	*�Զ�����92
	*/
	public java.lang.String def92;
	/**
	*�Զ�����93
	*/
	public java.lang.String def93;
	/**
	*�Զ�����94
	*/
	public java.lang.String def94;
	/**
	*�Զ�����95
	*/
	public java.lang.String def95;
	/**
	*�Զ�����96
	*/
	public java.lang.String def96;
	/**
	*�Զ�����97
	*/
	public java.lang.String def97;
	/**
	*�Զ�����98
	*/
	public java.lang.String def98;
	/**
	*�Զ�����99
	*/
	public java.lang.String def99;

	/**
	 * ����
	 */
	public String pk_projectdata;
	/**
	 * ����
	 */
	public String pk_group;
	/**
	 * ��֯
	 */
	public String pk_org;
	/**
	 * ��֯�汾
	 */
	public String pk_org_v;
	/**
	 * ����
	 */
	public String code;
	/**
	 * ����
	 */
	public String name;
	/**
	 * ��Ŀ���ڹ�˾
	 */
	public String projectcorp;
	/**
	 * ��Դ��Ŀ����
	 */
	public String srcid;
	/**
	 * ��Դϵͳ
	 */
	public String srcsystem;
	/**
	 * ��Ŀ��������
	 */
	public String projectarea;
	/**
	 * ��������
	 */
	public String periodizationname;
	/**
	 * ��Ŀ����
	 */
	public String projecttype;
	/**
	 * ��Ŀ��ȡʱ��_p6
	 */
	public UFDate p6_datadate1;
	/**
	 * ��Ӫ������ﵽ_p6
	 */
	public UFDate p6_datadate2;
	/**
	 * ���������_p6
	 */
	public UFDate p6_datadate3;
	/**
	 * ��������ʹ��֤_p6
	 */
	public UFDate p6_datadate4;
	/**
	 * �õع滮���֤_p6
	 */
	public UFDate p6_datadate5;
	/**
	 * ���蹤�̹滮���֤_p6
	 */
	public UFDate p6_datadate6;
	/**
	 * ʩ�����֤_p6
	 */
	public UFDate p6_datadate7;
	/**
	 * ����ʱ��_p6
	 */
	public UFDate p6_datadate8;
	/**
	 * ������_p6
	 */
	public UFDate p6_datadate9;
	/**
	 * Ԥ��֤_p6
	 */
	public UFDate p6_datadate10;
	/**
	 * �ṹ�ⶥ_p6
	 */
	public UFDate p6_datadate11;
	/**
	 * ��������_p6
	 */
	public UFDate p6_datadate12;
	/**
	 * ����_p6
	 */
	public UFDate p6_datadate13;
	/**
	 * ȷȨ_p6
	 */
	public UFDate p6_datadate14;
	/**
	 * ��Ŀ��ȡʱ��_nc
	 */
	public UFDate nc_datadate1;
	/**
	 * ��Ӫ������ﵽ_nc
	 */
	public UFDate nc_datadate2;
	/**
	 * ���������_nc
	 */
	public UFDate nc_datadate3;
	/**
	 * ��������ʹ��֤_nc
	 */
	public UFDate nc_datadate4;
	/**
	 * �õع滮���֤_nc
	 */
	public UFDate nc_datadate5;
	/**
	 * ���蹤�̹滮���֤_nc
	 */
	public UFDate nc_datadate6;
	/**
	 * ʩ�����֤_nc
	 */
	public UFDate nc_datadate7;
	/**
	 * ����ʱ��_nc
	 */
	public UFDate nc_datadate8;
	/**
	 * ������_nc
	 */
	public UFDate nc_datadate9;
	/**
	 * Ԥ��֤_nc
	 */
	public UFDate nc_datadate10;
	/**
	 * �ṹ�ⶥ_nc
	 */
	public UFDate nc_datadate11;
	/**
	 * ��������_nc
	 */
	public UFDate nc_datadate12;
	/**
	 * ����_nc
	 */
	public UFDate nc_datadate13;
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
	 * ҵ������
	 */
	public UFDate billdate;
	/**
	 * ������
	 */
	public String creator;
	/**
	 * ����ʱ��
	 */
	public UFDateTime creationtime;
	/**
	 * �޸���
	 */
	public String modifier;
	/**
	 * �޸�ʱ��
	 */
	public UFDateTime modifiedtime;
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
	 * ���� pk_projectdata��Getter����.������������ ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPk_projectdata() {
		return this.pk_projectdata;
	}

	/**
	 * ����pk_projectdata��Setter����.������������ ��������:2019-6-29
	 * 
	 * @param newPk_projectdata
	 *            java.lang.String
	 */
	public void setPk_projectdata(String pk_projectdata) {
		this.pk_projectdata = pk_projectdata;
	}

	/**
	 * ���� pk_group��Getter����.������������ ��������:2019-6-29
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public String getPk_group() {
		return this.pk_group;
	}

	/**
	 * ����pk_group��Setter����.������������ ��������:2019-6-29
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * ���� pk_org��Getter����.����������֯ ��������:2019-6-29
	 * 
	 * @return nc.vo.org.OrgVO
	 */
	public String getPk_org() {
		return this.pk_org;
	}

	/**
	 * ����pk_org��Setter����.����������֯ ��������:2019-6-29
	 * 
	 * @param newPk_org
	 *            nc.vo.org.OrgVO
	 */
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * ���� pk_org_v��Getter����.����������֯�汾 ��������:2019-6-29
	 * 
	 * @return nc.vo.vorg.OrgVersionVO
	 */
	public String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * ����pk_org_v��Setter����.����������֯�汾 ��������:2019-6-29
	 * 
	 * @param newPk_org_v
	 *            nc.vo.vorg.OrgVersionVO
	 */
	public void setPk_org_v(String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * ���� code��Getter����.������������ ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * ����code��Setter����.������������ ��������:2019-6-29
	 * 
	 * @param newCode
	 *            java.lang.String
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * ���� name��Getter����.������������ ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ����name��Setter����.������������ ��������:2019-6-29
	 * 
	 * @param newName
	 *            java.lang.String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ���� projectcorp��Getter����.����������Ŀ���ڹ�˾ ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getProjectcorp() {
		return this.projectcorp;
	}

	/**
	 * ����projectcorp��Setter����.����������Ŀ���ڹ�˾ ��������:2019-6-29
	 * 
	 * @param newProjectcorp
	 *            java.lang.String
	 */
	public void setProjectcorp(String projectcorp) {
		this.projectcorp = projectcorp;
	}

	/**
	 * ���� srcid��Getter����.����������Դ��Ŀ���� ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getSrcid() {
		return this.srcid;
	}

	/**
	 * ����srcid��Setter����.����������Դ��Ŀ���� ��������:2019-6-29
	 * 
	 * @param newSrcid
	 *            java.lang.String
	 */
	public void setSrcid(String srcid) {
		this.srcid = srcid;
	}

	/**
	 * ���� srcsystem��Getter����.����������Դϵͳ ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getSrcsystem() {
		return this.srcsystem;
	}

	/**
	 * ����srcsystem��Setter����.����������Դϵͳ ��������:2019-6-29
	 * 
	 * @param newSrcsystem
	 *            java.lang.String
	 */
	public void setSrcsystem(String srcsystem) {
		this.srcsystem = srcsystem;
	}

	/**
	 * ���� projectarea��Getter����.����������Ŀ�������� ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getProjectarea() {
		return this.projectarea;
	}

	/**
	 * ����projectarea��Setter����.����������Ŀ�������� ��������:2019-6-29
	 * 
	 * @param newProjectarea
	 *            java.lang.String
	 */
	public void setProjectarea(String projectarea) {
		this.projectarea = projectarea;
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
	 * ���� projecttype��Getter����.����������Ŀ���� ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getProjecttype() {
		return this.projecttype;
	}

	/**
	 * ����projecttype��Setter����.����������Ŀ���� ��������:2019-6-29
	 * 
	 * @param newProjecttype
	 *            java.lang.String
	 */
	public void setProjecttype(String projecttype) {
		this.projecttype = projecttype;
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
	 * ���� def21��Getter����.���������Զ�����21 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef21() {
		return this.def21;
	}

	/**
	 * ����def21��Setter����.���������Զ�����21 ��������:2019-6-29
	 * 
	 * @param newDef21
	 *            java.lang.String
	 */
	public void setDef21(String def21) {
		this.def21 = def21;
	}

	/**
	 * ���� def22��Getter����.���������Զ�����22 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef22() {
		return this.def22;
	}

	/**
	 * ����def22��Setter����.���������Զ�����22 ��������:2019-6-29
	 * 
	 * @param newDef22
	 *            java.lang.String
	 */
	public void setDef22(String def22) {
		this.def22 = def22;
	}

	/**
	 * ���� def23��Getter����.���������Զ�����23 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef23() {
		return this.def23;
	}

	/**
	 * ����def23��Setter����.���������Զ�����23 ��������:2019-6-29
	 * 
	 * @param newDef23
	 *            java.lang.String
	 */
	public void setDef23(String def23) {
		this.def23 = def23;
	}

	/**
	 * ���� def24��Getter����.���������Զ�����24 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef24() {
		return this.def24;
	}

	/**
	 * ����def24��Setter����.���������Զ�����24 ��������:2019-6-29
	 * 
	 * @param newDef24
	 *            java.lang.String
	 */
	public void setDef24(String def24) {
		this.def24 = def24;
	}

	/**
	 * ���� def25��Getter����.���������Զ�����25 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef25() {
		return this.def25;
	}

	/**
	 * ����def25��Setter����.���������Զ�����25 ��������:2019-6-29
	 * 
	 * @param newDef25
	 *            java.lang.String
	 */
	public void setDef25(String def25) {
		this.def25 = def25;
	}

	/**
	 * ���� def26��Getter����.���������Զ�����26 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef26() {
		return this.def26;
	}

	/**
	 * ����def26��Setter����.���������Զ�����26 ��������:2019-6-29
	 * 
	 * @param newDef26
	 *            java.lang.String
	 */
	public void setDef26(String def26) {
		this.def26 = def26;
	}

	/**
	 * ���� def27��Getter����.���������Զ�����27 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef27() {
		return this.def27;
	}

	/**
	 * ����def27��Setter����.���������Զ�����27 ��������:2019-6-29
	 * 
	 * @param newDef27
	 *            java.lang.String
	 */
	public void setDef27(String def27) {
		this.def27 = def27;
	}

	/**
	 * ���� def28��Getter����.���������Զ�����28 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef28() {
		return this.def28;
	}

	/**
	 * ����def28��Setter����.���������Զ�����28 ��������:2019-6-29
	 * 
	 * @param newDef28
	 *            java.lang.String
	 */
	public void setDef28(String def28) {
		this.def28 = def28;
	}

	/**
	 * ���� def29��Getter����.���������Զ�����29 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef29() {
		return this.def29;
	}

	/**
	 * ����def29��Setter����.���������Զ�����29 ��������:2019-6-29
	 * 
	 * @param newDef29
	 *            java.lang.String
	 */
	public void setDef29(String def29) {
		this.def29 = def29;
	}

	/**
	 * ���� def30��Getter����.���������Զ�����30 ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef30() {
		return this.def30;
	}

	/**
	 * ����def30��Setter����.���������Զ�����30 ��������:2019-6-29
	 * 
	 * @param newDef30
	 *            java.lang.String
	 */
	public void setDef30(String def30) {
		this.def30 = def30;
	}

	/**
	 * ���� billdate��Getter����.��������ҵ������ ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getBilldate() {
		return this.billdate;
	}

	/**
	 * ����billdate��Setter����.��������ҵ������ ��������:2019-6-29
	 * 
	 * @param newBilldate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setBilldate(UFDate billdate) {
		this.billdate = billdate;
	}

	/**
	 * ���� creator��Getter����.�������������� ��������:2019-6-29
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public String getCreator() {
		return this.creator;
	}

	/**
	 * ����creator��Setter����.�������������� ��������:2019-6-29
	 * 
	 * @param newCreator
	 *            nc.vo.sm.UserVO
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * ���� creationtime��Getter����.������������ʱ�� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * ����creationtime��Setter����.������������ʱ�� ��������:2019-6-29
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * ���� modifier��Getter����.���������޸��� ��������:2019-6-29
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public String getModifier() {
		return this.modifier;
	}

	/**
	 * ����modifier��Setter����.���������޸��� ��������:2019-6-29
	 * 
	 * @param newModifier
	 *            nc.vo.sm.UserVO
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * ���� modifiedtime��Getter����.���������޸�ʱ�� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * ����modifiedtime��Setter����.���������޸�ʱ�� ��������:2019-6-29
	 * 
	 * @param newModifiedtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	public java.lang.String getDef31() {
		return def31;
	}

	public void setDef31(java.lang.String def31) {
		this.def31 = def31;
	}

	public java.lang.String getDef32() {
		return def32;
	}

	public void setDef32(java.lang.String def32) {
		this.def32 = def32;
	}

	public java.lang.String getDef33() {
		return def33;
	}

	public void setDef33(java.lang.String def33) {
		this.def33 = def33;
	}

	public java.lang.String getDef34() {
		return def34;
	}

	public void setDef34(java.lang.String def34) {
		this.def34 = def34;
	}

	public java.lang.String getDef35() {
		return def35;
	}

	public void setDef35(java.lang.String def35) {
		this.def35 = def35;
	}

	public java.lang.String getDef36() {
		return def36;
	}

	public void setDef36(java.lang.String def36) {
		this.def36 = def36;
	}

	public java.lang.String getDef37() {
		return def37;
	}

	public void setDef37(java.lang.String def37) {
		this.def37 = def37;
	}

	public java.lang.String getDef38() {
		return def38;
	}

	public void setDef38(java.lang.String def38) {
		this.def38 = def38;
	}

	public java.lang.String getDef39() {
		return def39;
	}

	public void setDef39(java.lang.String def39) {
		this.def39 = def39;
	}

	public java.lang.String getDef40() {
		return def40;
	}

	public void setDef40(java.lang.String def40) {
		this.def40 = def40;
	}

	public java.lang.String getDef41() {
		return def41;
	}

	public void setDef41(java.lang.String def41) {
		this.def41 = def41;
	}

	public java.lang.String getDef42() {
		return def42;
	}

	public void setDef42(java.lang.String def42) {
		this.def42 = def42;
	}

	public java.lang.String getDef43() {
		return def43;
	}

	public void setDef43(java.lang.String def43) {
		this.def43 = def43;
	}

	public java.lang.String getDef44() {
		return def44;
	}

	public void setDef44(java.lang.String def44) {
		this.def44 = def44;
	}

	public java.lang.String getDef45() {
		return def45;
	}

	public void setDef45(java.lang.String def45) {
		this.def45 = def45;
	}

	public java.lang.String getDef46() {
		return def46;
	}

	public void setDef46(java.lang.String def46) {
		this.def46 = def46;
	}

	public java.lang.String getDef47() {
		return def47;
	}

	public void setDef47(java.lang.String def47) {
		this.def47 = def47;
	}

	public java.lang.String getDef48() {
		return def48;
	}

	public void setDef48(java.lang.String def48) {
		this.def48 = def48;
	}

	public java.lang.String getDef49() {
		return def49;
	}

	public void setDef49(java.lang.String def49) {
		this.def49 = def49;
	}

	public java.lang.String getDef50() {
		return def50;
	}

	public void setDef50(java.lang.String def50) {
		this.def50 = def50;
	}

	public java.lang.String getDef51() {
		return def51;
	}

	public void setDef51(java.lang.String def51) {
		this.def51 = def51;
	}

	public java.lang.String getDef52() {
		return def52;
	}

	public void setDef52(java.lang.String def52) {
		this.def52 = def52;
	}

	public java.lang.String getDef53() {
		return def53;
	}

	public void setDef53(java.lang.String def53) {
		this.def53 = def53;
	}

	public java.lang.String getDef54() {
		return def54;
	}

	public void setDef54(java.lang.String def54) {
		this.def54 = def54;
	}

	public java.lang.String getDef55() {
		return def55;
	}

	public void setDef55(java.lang.String def55) {
		this.def55 = def55;
	}

	public java.lang.String getDef56() {
		return def56;
	}

	public void setDef56(java.lang.String def56) {
		this.def56 = def56;
	}

	public java.lang.String getDef57() {
		return def57;
	}

	public void setDef57(java.lang.String def57) {
		this.def57 = def57;
	}

	public java.lang.String getDef58() {
		return def58;
	}

	public void setDef58(java.lang.String def58) {
		this.def58 = def58;
	}

	public java.lang.String getDef59() {
		return def59;
	}

	public void setDef59(java.lang.String def59) {
		this.def59 = def59;
	}

	public java.lang.String getDef60() {
		return def60;
	}

	public void setDef60(java.lang.String def60) {
		this.def60 = def60;
	}

	public java.lang.String getDef61() {
		return def61;
	}

	public void setDef61(java.lang.String def61) {
		this.def61 = def61;
	}

	public java.lang.String getDef62() {
		return def62;
	}

	public void setDef62(java.lang.String def62) {
		this.def62 = def62;
	}

	public java.lang.String getDef63() {
		return def63;
	}

	public void setDef63(java.lang.String def63) {
		this.def63 = def63;
	}

	public java.lang.String getDef64() {
		return def64;
	}

	public void setDef64(java.lang.String def64) {
		this.def64 = def64;
	}

	public java.lang.String getDef65() {
		return def65;
	}

	public void setDef65(java.lang.String def65) {
		this.def65 = def65;
	}

	public java.lang.String getDef66() {
		return def66;
	}

	public void setDef66(java.lang.String def66) {
		this.def66 = def66;
	}

	public java.lang.String getDef67() {
		return def67;
	}

	public void setDef67(java.lang.String def67) {
		this.def67 = def67;
	}

	public java.lang.String getDef68() {
		return def68;
	}

	public void setDef68(java.lang.String def68) {
		this.def68 = def68;
	}

	public java.lang.String getDef69() {
		return def69;
	}

	public void setDef69(java.lang.String def69) {
		this.def69 = def69;
	}

	public java.lang.String getDef70() {
		return def70;
	}

	public void setDef70(java.lang.String def70) {
		this.def70 = def70;
	}

	public java.lang.String getDef71() {
		return def71;
	}

	public void setDef71(java.lang.String def71) {
		this.def71 = def71;
	}

	public java.lang.String getDef72() {
		return def72;
	}

	public void setDef72(java.lang.String def72) {
		this.def72 = def72;
	}

	public java.lang.String getDef73() {
		return def73;
	}

	public void setDef73(java.lang.String def73) {
		this.def73 = def73;
	}

	public java.lang.String getDef74() {
		return def74;
	}

	public void setDef74(java.lang.String def74) {
		this.def74 = def74;
	}

	public java.lang.String getDef75() {
		return def75;
	}

	public void setDef75(java.lang.String def75) {
		this.def75 = def75;
	}

	public java.lang.String getDef76() {
		return def76;
	}

	public void setDef76(java.lang.String def76) {
		this.def76 = def76;
	}

	public java.lang.String getDef77() {
		return def77;
	}

	public void setDef77(java.lang.String def77) {
		this.def77 = def77;
	}

	public java.lang.String getDef78() {
		return def78;
	}

	public void setDef78(java.lang.String def78) {
		this.def78 = def78;
	}

	public java.lang.String getDef79() {
		return def79;
	}

	public void setDef79(java.lang.String def79) {
		this.def79 = def79;
	}

	public java.lang.String getDef80() {
		return def80;
	}

	public void setDef80(java.lang.String def80) {
		this.def80 = def80;
	}

	public java.lang.String getDef81() {
		return def81;
	}

	public void setDef81(java.lang.String def81) {
		this.def81 = def81;
	}

	public java.lang.String getDef82() {
		return def82;
	}

	public void setDef82(java.lang.String def82) {
		this.def82 = def82;
	}

	public java.lang.String getDef83() {
		return def83;
	}

	public void setDef83(java.lang.String def83) {
		this.def83 = def83;
	}

	public java.lang.String getDef84() {
		return def84;
	}

	public void setDef84(java.lang.String def84) {
		this.def84 = def84;
	}

	public java.lang.String getDef85() {
		return def85;
	}

	public void setDef85(java.lang.String def85) {
		this.def85 = def85;
	}

	public java.lang.String getDef86() {
		return def86;
	}

	public void setDef86(java.lang.String def86) {
		this.def86 = def86;
	}

	public java.lang.String getDef87() {
		return def87;
	}

	public void setDef87(java.lang.String def87) {
		this.def87 = def87;
	}

	public java.lang.String getDef88() {
		return def88;
	}

	public void setDef88(java.lang.String def88) {
		this.def88 = def88;
	}

	public java.lang.String getDef89() {
		return def89;
	}

	public void setDef89(java.lang.String def89) {
		this.def89 = def89;
	}

	public java.lang.String getDef90() {
		return def90;
	}

	public void setDef90(java.lang.String def90) {
		this.def90 = def90;
	}

	public java.lang.String getDef91() {
		return def91;
	}

	public void setDef91(java.lang.String def91) {
		this.def91 = def91;
	}

	public java.lang.String getDef92() {
		return def92;
	}

	public void setDef92(java.lang.String def92) {
		this.def92 = def92;
	}

	public java.lang.String getDef93() {
		return def93;
	}

	public void setDef93(java.lang.String def93) {
		this.def93 = def93;
	}

	public java.lang.String getDef94() {
		return def94;
	}

	public void setDef94(java.lang.String def94) {
		this.def94 = def94;
	}

	public java.lang.String getDef95() {
		return def95;
	}

	public void setDef95(java.lang.String def95) {
		this.def95 = def95;
	}

	public java.lang.String getDef96() {
		return def96;
	}

	public void setDef96(java.lang.String def96) {
		this.def96 = def96;
	}

	public java.lang.String getDef97() {
		return def97;
	}

	public void setDef97(java.lang.String def97) {
		this.def97 = def97;
	}

	public java.lang.String getDef98() {
		return def98;
	}

	public void setDef98(java.lang.String def98) {
		this.def98 = def98;
	}

	public java.lang.String getDef99() {
		return def99;
	}

	public void setDef99(java.lang.String def99) {
		this.def99 = def99;
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
		return VOMetaFactory.getInstance().getVOMeta("tg.ProjectDataVO");
	}
}
