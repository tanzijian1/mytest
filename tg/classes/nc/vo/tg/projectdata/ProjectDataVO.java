package nc.vo.tg.projectdata;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 * 此处添加累的描述信息
 * </p>
 * 创建日期:2019-6-29
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class ProjectDataVO extends SuperVO {
	public static final String PK_PROJECTDATA = "pk_projectdata";// 主键
	public static final String PK_GROUP = "pk_group";// 集团
	public static final String PK_ORG = "pk_org";// 组织
	public static final String PK_ORG_V = "pk_org_v";// 组织版本
	public static final String CODE = "code";// 编码
	public static final String NAME = "name";// 名称
	public static final String PROJECTCORP = "projectcorp";// 项目所在公司
	public static final String SRCID = "srcid";// 来源项目主键
	public static final String SRCSYSTEM = "srcsystem";// 来源系统
	public static final String PROJECTAREA = "projectarea";// 项目所属区域
	public static final String PERIODIZATIONNAME = "periodizationname";// 分期名称
	public static final String PROJECTTYPE = "projecttype";// 项目类型
	public static final String P6_DATADATE1 = "p6_datadate1";// 项目获取时间_p6
	public static final String P6_DATADATE2 = "p6_datadate2";// 运营启动点达到_p6
	public static final String P6_DATADATE3 = "p6_datadate3";// 修详规批复_p6
	public static final String P6_DATADATE4 = "p6_datadate4";// 国有土地使用证_p6
	public static final String P6_DATADATE5 = "p6_datadate5";// 用地规划许可证_p6
	public static final String P6_DATADATE6 = "p6_datadate6";// 建设工程规划许可证_p6
	public static final String P6_DATADATE7 = "p6_datadate7";// 施工许可证_p6
	public static final String P6_DATADATE8 = "p6_datadate8";// 开工时间_p6
	public static final String P6_DATADATE9 = "p6_datadate9";// 正负零_p6
	public static final String P6_DATADATE10 = "p6_datadate10";// 预售证_p6
	public static final String P6_DATADATE11 = "p6_datadate11";// 结构封顶_p6
	public static final String P6_DATADATE12 = "p6_datadate12";// 竣工备案_p6
	public static final String P6_DATADATE13 = "p6_datadate13";// 交付_p6
	public static final String P6_DATADATE14 = "p6_datadate14";// 确权_p6
	public static final String NC_DATADATE1 = "nc_datadate1";// 项目获取时间_nc
	public static final String NC_DATADATE2 = "nc_datadate2";// 运营启动点达到_nc
	public static final String NC_DATADATE3 = "nc_datadate3";// 修详规批复_nc
	public static final String NC_DATADATE4 = "nc_datadate4";// 国有土地使用证_nc
	public static final String NC_DATADATE5 = "nc_datadate5";// 用地规划许可证_nc
	public static final String NC_DATADATE6 = "nc_datadate6";// 建设工程规划许可证_nc
	public static final String NC_DATADATE7 = "nc_datadate7";// 施工许可证_nc
	public static final String NC_DATADATE8 = "nc_datadate8";// 开工时间_nc
	public static final String NC_DATADATE9 = "nc_datadate9";// 正负零_nc
	public static final String NC_DATADATE10 = "nc_datadate10";// 预售证_nc
	public static final String NC_DATADATE11 = "nc_datadate11";// 结构封顶_nc
	public static final String NC_DATADATE12 = "nc_datadate12";// 竣工备案_nc
	public static final String NC_DATADATE13 = "nc_datadate13";// 交付_nc
	public static final String NC_DATADATE14 = "nc_datadate14";// 确权_nc
	public static final String DEF1 = "def1";// 自定义项1
	public static final String DEF2 = "def2";// 自定义项2
	public static final String DEF3 = "def3";// 自定义项3
	public static final String DEF4 = "def4";// 自定义项4
	public static final String DEF5 = "def5";// 自定义项5
	public static final String DEF6 = "def6";// 自定义项6
	public static final String DEF7 = "def7";// 自定义项7
	public static final String DEF8 = "def8";// 自定义项8
	public static final String DEF9 = "def9";// 自定义项9
	public static final String DEF10 = "def10";// 自定义项10
	public static final String DEF11 = "def11";// 自定义项11
	public static final String DEF12 = "def12";// 自定义项12
	public static final String DEF13 = "def13";// 自定义项13
	public static final String DEF14 = "def14";// 自定义项14
	public static final String DEF15 = "def15";// 自定义项15
	public static final String DEF16 = "def16";// 自定义项16
	public static final String DEF17 = "def17";// 自定义项17
	public static final String DEF18 = "def18";// 自定义项18
	public static final String DEF19 = "def19";// 自定义项19
	public static final String DEF20 = "def20";// 自定义项20
	public static final String DEF21 = "def21";// 自定义项21
	public static final String DEF22 = "def22";// 自定义项22
	public static final String DEF23 = "def23";// 自定义项23
	public static final String DEF24 = "def24";// 自定义项24
	public static final String DEF25 = "def25";// 自定义项25
	public static final String DEF26 = "def26";// 自定义项26
	public static final String DEF27 = "def27";// 自定义项27
	public static final String DEF28 = "def28";// 自定义项28
	public static final String DEF29 = "def29";// 自定义项29
	public static final String DEF30 = "def30";// 自定义项30
	
	public static final String DEF31 = "def31";// 自定义项31
	public static final String DEF32 = "def32";// 自定义项32
	public static final String DEF33 = "def33";// 自定义项33
	public static final String DEF34 = "def34";// 自定义项34
	public static final String DEF35 = "def35";// 自定义项35
	public static final String DEF36 = "def36";// 自定义项36
	public static final String DEF37 = "def37";// 自定义项37
	public static final String DEF38 = "def38";// 自定义项38
	public static final String DEF39 = "def39";// 自定义项39
	public static final String DEF40 = "def40";// 自定义项40
	public static final String DEF41 = "def41";// 自定义项41
	public static final String DEF42 = "def42";// 自定义项42
	public static final String DEF43 = "def43";// 自定义项43
	public static final String DEF44 = "def44";// 自定义项44
	public static final String DEF45 = "def45";// 自定义项45
	public static final String DEF46 = "def46";// 自定义项46
	public static final String DEF47 = "def47";// 自定义项47
	public static final String DEF48 = "def48";// 自定义项48
	public static final String DEF49 = "def49";// 自定义项49
	public static final String DEF50 = "def50";// 自定义项50
	public static final String DEF51 = "def51";// 自定义项51
	public static final String DEF52 = "def52";// 自定义项52
	public static final String DEF53 = "def53";// 自定义项53
	public static final String DEF54 = "def54";// 自定义项54
	public static final String DEF55 = "def55";// 自定义项55
	public static final String DEF56 = "def56";// 自定义项56
	public static final String DEF57 = "def57";// 自定义项57
	public static final String DEF58 = "def58";// 自定义项58
	public static final String DEF59 = "def59";// 自定义项59
	public static final String DEF60 = "def60";// 自定义项60
	public static final String DEF61 = "def61";// 自定义项61
	public static final String DEF62 = "def62";// 自定义项62
	public static final String DEF63 = "def63";// 自定义项63
	public static final String DEF64 = "def64";// 自定义项64
	public static final String DEF65 = "def65";// 自定义项65
	public static final String DEF66 = "def66";// 自定义项66
	public static final String DEF67 = "def67";// 自定义项67
	public static final String DEF68 = "def68";// 自定义项68
	public static final String DEF69 = "def69";// 自定义项69
	public static final String DEF70 = "def70";// 自定义项70
	public static final String DEF71 = "def71";// 自定义项71
	public static final String DEF72 = "def72";// 自定义项72
	public static final String DEF73 = "def73";// 自定义项73
	public static final String DEF74 = "def74";// 自定义项74
	public static final String DEF75 = "def75";// 自定义项75
	public static final String DEF76 = "def76";// 自定义项76
	public static final String DEF77 = "def77";// 自定义项77
	public static final String DEF78 = "def78";// 自定义项78
	public static final String DEF79 = "def79";// 自定义项79
	public static final String DEF80 = "def80";// 自定义项80
	public static final String DEF81 = "def81";// 自定义项81
	public static final String DEF82 = "def82";// 自定义项82
	public static final String DEF83 = "def83";// 自定义项83
	public static final String DEF84 = "def84";// 自定义项84
	public static final String DEF85 = "def85";// 自定义项85
	public static final String DEF86 = "def86";// 自定义项86
	public static final String DEF87 = "def87";// 自定义项87
	public static final String DEF88 = "def88";// 自定义项88
	public static final String DEF89 = "def89";// 自定义项89
	public static final String DEF90 = "def90";// 自定义项90
	public static final String DEF91 = "def91";// 自定义项91
	public static final String DEF92 = "def92";// 自定义项92
	public static final String DEF93 = "def93";// 自定义项93
	public static final String DEF94 = "def94";// 自定义项94
	public static final String DEF95 = "def95";// 自定义项95
	public static final String DEF96 = "def96";// 自定义项96
	public static final String DEF97 = "def97";// 自定义项97
	public static final String DEF98 = "def98";// 自定义项98
	public static final String DEF99 = "def99";// 自定义项99

	public static final String BILLDATE = "billdate";// 业务日期
	public static final String CREATOR = "creator";// 创建人
	public static final String CREATIONTIME = "creationtime";// 创建时间
	public static final String MODIFIER = "modifier";// 修改人
	public static final String MODIFIEDTIME = "modifiedtime";// 修改时间
	/**
	*自定义项31
	*/
	public java.lang.String def31;
	/**
	*自定义项32
	*/
	public java.lang.String def32;
	/**
	*自定义项33
	*/
	public java.lang.String def33;
	/**
	*自定义项34
	*/
	public java.lang.String def34;
	/**
	*自定义项35
	*/
	public java.lang.String def35;
	/**
	*自定义项36
	*/
	public java.lang.String def36;
	/**
	*自定义项37
	*/
	public java.lang.String def37;
	/**
	*自定义项38
	*/
	public java.lang.String def38;
	/**
	*自定义项39
	*/
	public java.lang.String def39;
	/**
	*自定义项40
	*/
	public java.lang.String def40;
	/**
	*自定义项41
	*/
	public java.lang.String def41;
	/**
	*自定义项42
	*/
	public java.lang.String def42;
	/**
	*自定义项43
	*/
	public java.lang.String def43;
	/**
	*自定义项44
	*/
	public java.lang.String def44;
	/**
	*自定义项45
	*/
	public java.lang.String def45;
	/**
	*自定义项46
	*/
	public java.lang.String def46;
	/**
	*自定义项47
	*/
	public java.lang.String def47;
	/**
	*自定义项48
	*/
	public java.lang.String def48;
	/**
	*自定义项49
	*/
	public java.lang.String def49;
	/**
	*自定义项50
	*/
	public java.lang.String def50;
	/**
	*自定义项51
	*/
	public java.lang.String def51;
	/**
	*自定义项52
	*/
	public java.lang.String def52;
	/**
	*自定义项53
	*/
	public java.lang.String def53;
	/**
	*自定义项54
	*/
	public java.lang.String def54;
	/**
	*自定义项55
	*/
	public java.lang.String def55;
	/**
	*自定义项56
	*/
	public java.lang.String def56;
	/**
	*自定义项57
	*/
	public java.lang.String def57;
	/**
	*自定义项58
	*/
	public java.lang.String def58;
	/**
	*自定义项59
	*/
	public java.lang.String def59;
	/**
	*自定义项60
	*/
	public java.lang.String def60;
	/**
	*自定义项61
	*/
	public java.lang.String def61;
	/**
	*自定义项62
	*/
	public java.lang.String def62;
	/**
	*自定义项63
	*/
	public java.lang.String def63;
	/**
	*自定义项64
	*/
	public java.lang.String def64;
	/**
	*自定义项65
	*/
	public java.lang.String def65;
	/**
	*自定义项66
	*/
	public java.lang.String def66;
	/**
	*自定义项67
	*/
	public java.lang.String def67;
	/**
	*自定义项68
	*/
	public java.lang.String def68;
	/**
	*自定义项69
	*/
	public java.lang.String def69;
	/**
	*自定义项70
	*/
	public java.lang.String def70;
	/**
	*自定义项71
	*/
	public java.lang.String def71;
	/**
	*自定义项72
	*/
	public java.lang.String def72;
	/**
	*自定义项73
	*/
	public java.lang.String def73;
	/**
	*自定义项74
	*/
	public java.lang.String def74;
	/**
	*自定义项75
	*/
	public java.lang.String def75;
	/**
	*自定义项76
	*/
	public java.lang.String def76;
	/**
	*自定义项77
	*/
	public java.lang.String def77;
	/**
	*自定义项78
	*/
	public java.lang.String def78;
	/**
	*自定义项79
	*/
	public java.lang.String def79;
	/**
	*自定义项80
	*/
	public java.lang.String def80;
	/**
	*自定义项81
	*/
	public java.lang.String def81;
	/**
	*自定义项82
	*/
	public java.lang.String def82;
	/**
	*自定义项83
	*/
	public java.lang.String def83;
	/**
	*自定义项84
	*/
	public java.lang.String def84;
	/**
	*自定义项85
	*/
	public java.lang.String def85;
	/**
	*自定义项86
	*/
	public java.lang.String def86;
	/**
	*自定义项87
	*/
	public java.lang.String def87;
	/**
	*自定义项88
	*/
	public java.lang.String def88;
	/**
	*自定义项89
	*/
	public java.lang.String def89;
	/**
	*自定义项90
	*/
	public java.lang.String def90;
	/**
	*自定义项91
	*/
	public java.lang.String def91;
	/**
	*自定义项92
	*/
	public java.lang.String def92;
	/**
	*自定义项93
	*/
	public java.lang.String def93;
	/**
	*自定义项94
	*/
	public java.lang.String def94;
	/**
	*自定义项95
	*/
	public java.lang.String def95;
	/**
	*自定义项96
	*/
	public java.lang.String def96;
	/**
	*自定义项97
	*/
	public java.lang.String def97;
	/**
	*自定义项98
	*/
	public java.lang.String def98;
	/**
	*自定义项99
	*/
	public java.lang.String def99;

	/**
	 * 主键
	 */
	public String pk_projectdata;
	/**
	 * 集团
	 */
	public String pk_group;
	/**
	 * 组织
	 */
	public String pk_org;
	/**
	 * 组织版本
	 */
	public String pk_org_v;
	/**
	 * 编码
	 */
	public String code;
	/**
	 * 名称
	 */
	public String name;
	/**
	 * 项目所在公司
	 */
	public String projectcorp;
	/**
	 * 来源项目主键
	 */
	public String srcid;
	/**
	 * 来源系统
	 */
	public String srcsystem;
	/**
	 * 项目所属区域
	 */
	public String projectarea;
	/**
	 * 分期名称
	 */
	public String periodizationname;
	/**
	 * 项目类型
	 */
	public String projecttype;
	/**
	 * 项目获取时间_p6
	 */
	public UFDate p6_datadate1;
	/**
	 * 运营启动点达到_p6
	 */
	public UFDate p6_datadate2;
	/**
	 * 修详规批复_p6
	 */
	public UFDate p6_datadate3;
	/**
	 * 国有土地使用证_p6
	 */
	public UFDate p6_datadate4;
	/**
	 * 用地规划许可证_p6
	 */
	public UFDate p6_datadate5;
	/**
	 * 建设工程规划许可证_p6
	 */
	public UFDate p6_datadate6;
	/**
	 * 施工许可证_p6
	 */
	public UFDate p6_datadate7;
	/**
	 * 开工时间_p6
	 */
	public UFDate p6_datadate8;
	/**
	 * 正负零_p6
	 */
	public UFDate p6_datadate9;
	/**
	 * 预售证_p6
	 */
	public UFDate p6_datadate10;
	/**
	 * 结构封顶_p6
	 */
	public UFDate p6_datadate11;
	/**
	 * 竣工备案_p6
	 */
	public UFDate p6_datadate12;
	/**
	 * 交付_p6
	 */
	public UFDate p6_datadate13;
	/**
	 * 确权_p6
	 */
	public UFDate p6_datadate14;
	/**
	 * 项目获取时间_nc
	 */
	public UFDate nc_datadate1;
	/**
	 * 运营启动点达到_nc
	 */
	public UFDate nc_datadate2;
	/**
	 * 修详规批复_nc
	 */
	public UFDate nc_datadate3;
	/**
	 * 国有土地使用证_nc
	 */
	public UFDate nc_datadate4;
	/**
	 * 用地规划许可证_nc
	 */
	public UFDate nc_datadate5;
	/**
	 * 建设工程规划许可证_nc
	 */
	public UFDate nc_datadate6;
	/**
	 * 施工许可证_nc
	 */
	public UFDate nc_datadate7;
	/**
	 * 开工时间_nc
	 */
	public UFDate nc_datadate8;
	/**
	 * 正负零_nc
	 */
	public UFDate nc_datadate9;
	/**
	 * 预售证_nc
	 */
	public UFDate nc_datadate10;
	/**
	 * 结构封顶_nc
	 */
	public UFDate nc_datadate11;
	/**
	 * 竣工备案_nc
	 */
	public UFDate nc_datadate12;
	/**
	 * 交付_nc
	 */
	public UFDate nc_datadate13;
	/**
	 * 确权_nc
	 */
	public UFDate nc_datadate14;
	/**
	 * 自定义项1
	 */
	public String def1;
	/**
	 * 自定义项2
	 */
	public String def2;
	/**
	 * 自定义项3
	 */
	public String def3;
	/**
	 * 自定义项4
	 */
	public String def4;
	/**
	 * 自定义项5
	 */
	public String def5;
	/**
	 * 自定义项6
	 */
	public String def6;
	/**
	 * 自定义项7
	 */
	public String def7;
	/**
	 * 自定义项8
	 */
	public String def8;
	/**
	 * 自定义项9
	 */
	public String def9;
	/**
	 * 自定义项10
	 */
	public String def10;
	/**
	 * 自定义项11
	 */
	public String def11;
	/**
	 * 自定义项12
	 */
	public String def12;
	/**
	 * 自定义项13
	 */
	public String def13;
	/**
	 * 自定义项14
	 */
	public String def14;
	/**
	 * 自定义项15
	 */
	public String def15;
	/**
	 * 自定义项16
	 */
	public String def16;
	/**
	 * 自定义项17
	 */
	public String def17;
	/**
	 * 自定义项18
	 */
	public String def18;
	/**
	 * 自定义项19
	 */
	public String def19;
	/**
	 * 自定义项20
	 */
	public String def20;
	/**
	 * 自定义项21
	 */
	public String def21;
	/**
	 * 自定义项22
	 */
	public String def22;
	/**
	 * 自定义项23
	 */
	public String def23;
	/**
	 * 自定义项24
	 */
	public String def24;
	/**
	 * 自定义项25
	 */
	public String def25;
	/**
	 * 自定义项26
	 */
	public String def26;
	/**
	 * 自定义项27
	 */
	public String def27;
	/**
	 * 自定义项28
	 */
	public String def28;
	/**
	 * 自定义项29
	 */
	public String def29;
	/**
	 * 自定义项30
	 */
	public String def30;
	/**
	 * 业务日期
	 */
	public UFDate billdate;
	/**
	 * 创建人
	 */
	public String creator;
	/**
	 * 创建时间
	 */
	public UFDateTime creationtime;
	/**
	 * 修改人
	 */
	public String modifier;
	/**
	 * 修改时间
	 */
	public UFDateTime modifiedtime;
	/**
	 * 时间戳
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
	 * 属性 pk_projectdata的Getter方法.属性名：主键 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPk_projectdata() {
		return this.pk_projectdata;
	}

	/**
	 * 属性pk_projectdata的Setter方法.属性名：主键 创建日期:2019-6-29
	 * 
	 * @param newPk_projectdata
	 *            java.lang.String
	 */
	public void setPk_projectdata(String pk_projectdata) {
		this.pk_projectdata = pk_projectdata;
	}

	/**
	 * 属性 pk_group的Getter方法.属性名：集团 创建日期:2019-6-29
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public String getPk_group() {
		return this.pk_group;
	}

	/**
	 * 属性pk_group的Setter方法.属性名：集团 创建日期:2019-6-29
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * 属性 pk_org的Getter方法.属性名：组织 创建日期:2019-6-29
	 * 
	 * @return nc.vo.org.OrgVO
	 */
	public String getPk_org() {
		return this.pk_org;
	}

	/**
	 * 属性pk_org的Setter方法.属性名：组织 创建日期:2019-6-29
	 * 
	 * @param newPk_org
	 *            nc.vo.org.OrgVO
	 */
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * 属性 pk_org_v的Getter方法.属性名：组织版本 创建日期:2019-6-29
	 * 
	 * @return nc.vo.vorg.OrgVersionVO
	 */
	public String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * 属性pk_org_v的Setter方法.属性名：组织版本 创建日期:2019-6-29
	 * 
	 * @param newPk_org_v
	 *            nc.vo.vorg.OrgVersionVO
	 */
	public void setPk_org_v(String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * 属性 code的Getter方法.属性名：编码 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * 属性code的Setter方法.属性名：编码 创建日期:2019-6-29
	 * 
	 * @param newCode
	 *            java.lang.String
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 属性 name的Getter方法.属性名：名称 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 属性name的Setter方法.属性名：名称 创建日期:2019-6-29
	 * 
	 * @param newName
	 *            java.lang.String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 属性 projectcorp的Getter方法.属性名：项目所在公司 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getProjectcorp() {
		return this.projectcorp;
	}

	/**
	 * 属性projectcorp的Setter方法.属性名：项目所在公司 创建日期:2019-6-29
	 * 
	 * @param newProjectcorp
	 *            java.lang.String
	 */
	public void setProjectcorp(String projectcorp) {
		this.projectcorp = projectcorp;
	}

	/**
	 * 属性 srcid的Getter方法.属性名：来源项目主键 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getSrcid() {
		return this.srcid;
	}

	/**
	 * 属性srcid的Setter方法.属性名：来源项目主键 创建日期:2019-6-29
	 * 
	 * @param newSrcid
	 *            java.lang.String
	 */
	public void setSrcid(String srcid) {
		this.srcid = srcid;
	}

	/**
	 * 属性 srcsystem的Getter方法.属性名：来源系统 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getSrcsystem() {
		return this.srcsystem;
	}

	/**
	 * 属性srcsystem的Setter方法.属性名：来源系统 创建日期:2019-6-29
	 * 
	 * @param newSrcsystem
	 *            java.lang.String
	 */
	public void setSrcsystem(String srcsystem) {
		this.srcsystem = srcsystem;
	}

	/**
	 * 属性 projectarea的Getter方法.属性名：项目所属区域 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getProjectarea() {
		return this.projectarea;
	}

	/**
	 * 属性projectarea的Setter方法.属性名：项目所属区域 创建日期:2019-6-29
	 * 
	 * @param newProjectarea
	 *            java.lang.String
	 */
	public void setProjectarea(String projectarea) {
		this.projectarea = projectarea;
	}

	/**
	 * 属性 periodizationname的Getter方法.属性名：分期名称 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPeriodizationname() {
		return this.periodizationname;
	}

	/**
	 * 属性periodizationname的Setter方法.属性名：分期名称 创建日期:2019-6-29
	 * 
	 * @param newPeriodizationname
	 *            java.lang.String
	 */
	public void setPeriodizationname(String periodizationname) {
		this.periodizationname = periodizationname;
	}

	/**
	 * 属性 projecttype的Getter方法.属性名：项目类型 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getProjecttype() {
		return this.projecttype;
	}

	/**
	 * 属性projecttype的Setter方法.属性名：项目类型 创建日期:2019-6-29
	 * 
	 * @param newProjecttype
	 *            java.lang.String
	 */
	public void setProjecttype(String projecttype) {
		this.projecttype = projecttype;
	}

	/**
	 * 属性 p6_datadate1的Getter方法.属性名：项目获取时间_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate1() {
		return this.p6_datadate1;
	}

	/**
	 * 属性p6_datadate1的Setter方法.属性名：项目获取时间_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate1
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate1(UFDate p6_datadate1) {
		this.p6_datadate1 = p6_datadate1;
	}

	/**
	 * 属性 p6_datadate2的Getter方法.属性名：运营启动点达到_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate2() {
		return this.p6_datadate2;
	}

	/**
	 * 属性p6_datadate2的Setter方法.属性名：运营启动点达到_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate2
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate2(UFDate p6_datadate2) {
		this.p6_datadate2 = p6_datadate2;
	}

	/**
	 * 属性 p6_datadate3的Getter方法.属性名：修详规批复_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate3() {
		return this.p6_datadate3;
	}

	/**
	 * 属性p6_datadate3的Setter方法.属性名：修详规批复_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate3
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate3(UFDate p6_datadate3) {
		this.p6_datadate3 = p6_datadate3;
	}

	/**
	 * 属性 p6_datadate4的Getter方法.属性名：国有土地使用证_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate4() {
		return this.p6_datadate4;
	}

	/**
	 * 属性p6_datadate4的Setter方法.属性名：国有土地使用证_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate4
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate4(UFDate p6_datadate4) {
		this.p6_datadate4 = p6_datadate4;
	}

	/**
	 * 属性 p6_datadate5的Getter方法.属性名：用地规划许可证_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate5() {
		return this.p6_datadate5;
	}

	/**
	 * 属性p6_datadate5的Setter方法.属性名：用地规划许可证_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate5
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate5(UFDate p6_datadate5) {
		this.p6_datadate5 = p6_datadate5;
	}

	/**
	 * 属性 p6_datadate6的Getter方法.属性名：建设工程规划许可证_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate6() {
		return this.p6_datadate6;
	}

	/**
	 * 属性p6_datadate6的Setter方法.属性名：建设工程规划许可证_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate6
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate6(UFDate p6_datadate6) {
		this.p6_datadate6 = p6_datadate6;
	}

	/**
	 * 属性 p6_datadate7的Getter方法.属性名：施工许可证_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate7() {
		return this.p6_datadate7;
	}

	/**
	 * 属性p6_datadate7的Setter方法.属性名：施工许可证_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate7
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate7(UFDate p6_datadate7) {
		this.p6_datadate7 = p6_datadate7;
	}

	/**
	 * 属性 p6_datadate8的Getter方法.属性名：开工时间_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate8() {
		return this.p6_datadate8;
	}

	/**
	 * 属性p6_datadate8的Setter方法.属性名：开工时间_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate8
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate8(UFDate p6_datadate8) {
		this.p6_datadate8 = p6_datadate8;
	}

	/**
	 * 属性 p6_datadate9的Getter方法.属性名：正负零_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate9() {
		return this.p6_datadate9;
	}

	/**
	 * 属性p6_datadate9的Setter方法.属性名：正负零_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate9
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate9(UFDate p6_datadate9) {
		this.p6_datadate9 = p6_datadate9;
	}

	/**
	 * 属性 p6_datadate10的Getter方法.属性名：预售证_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate10() {
		return this.p6_datadate10;
	}

	/**
	 * 属性p6_datadate10的Setter方法.属性名：预售证_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate10
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate10(UFDate p6_datadate10) {
		this.p6_datadate10 = p6_datadate10;
	}

	/**
	 * 属性 p6_datadate11的Getter方法.属性名：结构封顶_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate11() {
		return this.p6_datadate11;
	}

	/**
	 * 属性p6_datadate11的Setter方法.属性名：结构封顶_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate11
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate11(UFDate p6_datadate11) {
		this.p6_datadate11 = p6_datadate11;
	}

	/**
	 * 属性 p6_datadate12的Getter方法.属性名：竣工备案_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate12() {
		return this.p6_datadate12;
	}

	/**
	 * 属性p6_datadate12的Setter方法.属性名：竣工备案_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate12
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate12(UFDate p6_datadate12) {
		this.p6_datadate12 = p6_datadate12;
	}

	/**
	 * 属性 p6_datadate13的Getter方法.属性名：交付_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate13() {
		return this.p6_datadate13;
	}

	/**
	 * 属性p6_datadate13的Setter方法.属性名：交付_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate13
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate13(UFDate p6_datadate13) {
		this.p6_datadate13 = p6_datadate13;
	}

	/**
	 * 属性 p6_datadate14的Getter方法.属性名：确权_p6 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getP6_datadate14() {
		return this.p6_datadate14;
	}

	/**
	 * 属性p6_datadate14的Setter方法.属性名：确权_p6 创建日期:2019-6-29
	 * 
	 * @param newP6_datadate14
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setP6_datadate14(UFDate p6_datadate14) {
		this.p6_datadate14 = p6_datadate14;
	}

	/**
	 * 属性 nc_datadate1的Getter方法.属性名：项目获取时间_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate1() {
		return this.nc_datadate1;
	}

	/**
	 * 属性nc_datadate1的Setter方法.属性名：项目获取时间_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate1
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate1(UFDate nc_datadate1) {
		this.nc_datadate1 = nc_datadate1;
	}

	/**
	 * 属性 nc_datadate2的Getter方法.属性名：运营启动点达到_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate2() {
		return this.nc_datadate2;
	}

	/**
	 * 属性nc_datadate2的Setter方法.属性名：运营启动点达到_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate2
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate2(UFDate nc_datadate2) {
		this.nc_datadate2 = nc_datadate2;
	}

	/**
	 * 属性 nc_datadate3的Getter方法.属性名：修详规批复_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate3() {
		return this.nc_datadate3;
	}

	/**
	 * 属性nc_datadate3的Setter方法.属性名：修详规批复_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate3
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate3(UFDate nc_datadate3) {
		this.nc_datadate3 = nc_datadate3;
	}

	/**
	 * 属性 nc_datadate4的Getter方法.属性名：国有土地使用证_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate4() {
		return this.nc_datadate4;
	}

	/**
	 * 属性nc_datadate4的Setter方法.属性名：国有土地使用证_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate4
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate4(UFDate nc_datadate4) {
		this.nc_datadate4 = nc_datadate4;
	}

	/**
	 * 属性 nc_datadate5的Getter方法.属性名：用地规划许可证_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate5() {
		return this.nc_datadate5;
	}

	/**
	 * 属性nc_datadate5的Setter方法.属性名：用地规划许可证_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate5
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate5(UFDate nc_datadate5) {
		this.nc_datadate5 = nc_datadate5;
	}

	/**
	 * 属性 nc_datadate6的Getter方法.属性名：建设工程规划许可证_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate6() {
		return this.nc_datadate6;
	}

	/**
	 * 属性nc_datadate6的Setter方法.属性名：建设工程规划许可证_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate6
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate6(UFDate nc_datadate6) {
		this.nc_datadate6 = nc_datadate6;
	}

	/**
	 * 属性 nc_datadate7的Getter方法.属性名：施工许可证_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate7() {
		return this.nc_datadate7;
	}

	/**
	 * 属性nc_datadate7的Setter方法.属性名：施工许可证_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate7
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate7(UFDate nc_datadate7) {
		this.nc_datadate7 = nc_datadate7;
	}

	/**
	 * 属性 nc_datadate8的Getter方法.属性名：开工时间_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate8() {
		return this.nc_datadate8;
	}

	/**
	 * 属性nc_datadate8的Setter方法.属性名：开工时间_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate8
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate8(UFDate nc_datadate8) {
		this.nc_datadate8 = nc_datadate8;
	}

	/**
	 * 属性 nc_datadate9的Getter方法.属性名：正负零_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate9() {
		return this.nc_datadate9;
	}

	/**
	 * 属性nc_datadate9的Setter方法.属性名：正负零_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate9
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate9(UFDate nc_datadate9) {
		this.nc_datadate9 = nc_datadate9;
	}

	/**
	 * 属性 nc_datadate10的Getter方法.属性名：预售证_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate10() {
		return this.nc_datadate10;
	}

	/**
	 * 属性nc_datadate10的Setter方法.属性名：预售证_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate10
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate10(UFDate nc_datadate10) {
		this.nc_datadate10 = nc_datadate10;
	}

	/**
	 * 属性 nc_datadate11的Getter方法.属性名：结构封顶_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate11() {
		return this.nc_datadate11;
	}

	/**
	 * 属性nc_datadate11的Setter方法.属性名：结构封顶_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate11
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate11(UFDate nc_datadate11) {
		this.nc_datadate11 = nc_datadate11;
	}

	/**
	 * 属性 nc_datadate12的Getter方法.属性名：竣工备案_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate12() {
		return this.nc_datadate12;
	}

	/**
	 * 属性nc_datadate12的Setter方法.属性名：竣工备案_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate12
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate12(UFDate nc_datadate12) {
		this.nc_datadate12 = nc_datadate12;
	}

	/**
	 * 属性 nc_datadate13的Getter方法.属性名：交付_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate13() {
		return this.nc_datadate13;
	}

	/**
	 * 属性nc_datadate13的Setter方法.属性名：交付_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate13
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate13(UFDate nc_datadate13) {
		this.nc_datadate13 = nc_datadate13;
	}

	/**
	 * 属性 nc_datadate14的Getter方法.属性名：确权_nc 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNc_datadate14() {
		return this.nc_datadate14;
	}

	/**
	 * 属性nc_datadate14的Setter方法.属性名：确权_nc 创建日期:2019-6-29
	 * 
	 * @param newNc_datadate14
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNc_datadate14(UFDate nc_datadate14) {
		this.nc_datadate14 = nc_datadate14;
	}

	/**
	 * 属性 def1的Getter方法.属性名：自定义项1 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef1() {
		return this.def1;
	}

	/**
	 * 属性def1的Setter方法.属性名：自定义项1 创建日期:2019-6-29
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(String def1) {
		this.def1 = def1;
	}

	/**
	 * 属性 def2的Getter方法.属性名：自定义项2 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef2() {
		return this.def2;
	}

	/**
	 * 属性def2的Setter方法.属性名：自定义项2 创建日期:2019-6-29
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(String def2) {
		this.def2 = def2;
	}

	/**
	 * 属性 def3的Getter方法.属性名：自定义项3 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef3() {
		return this.def3;
	}

	/**
	 * 属性def3的Setter方法.属性名：自定义项3 创建日期:2019-6-29
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(String def3) {
		this.def3 = def3;
	}

	/**
	 * 属性 def4的Getter方法.属性名：自定义项4 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef4() {
		return this.def4;
	}

	/**
	 * 属性def4的Setter方法.属性名：自定义项4 创建日期:2019-6-29
	 * 
	 * @param newDef4
	 *            java.lang.String
	 */
	public void setDef4(String def4) {
		this.def4 = def4;
	}

	/**
	 * 属性 def5的Getter方法.属性名：自定义项5 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef5() {
		return this.def5;
	}

	/**
	 * 属性def5的Setter方法.属性名：自定义项5 创建日期:2019-6-29
	 * 
	 * @param newDef5
	 *            java.lang.String
	 */
	public void setDef5(String def5) {
		this.def5 = def5;
	}

	/**
	 * 属性 def6的Getter方法.属性名：自定义项6 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef6() {
		return this.def6;
	}

	/**
	 * 属性def6的Setter方法.属性名：自定义项6 创建日期:2019-6-29
	 * 
	 * @param newDef6
	 *            java.lang.String
	 */
	public void setDef6(String def6) {
		this.def6 = def6;
	}

	/**
	 * 属性 def7的Getter方法.属性名：自定义项7 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef7() {
		return this.def7;
	}

	/**
	 * 属性def7的Setter方法.属性名：自定义项7 创建日期:2019-6-29
	 * 
	 * @param newDef7
	 *            java.lang.String
	 */
	public void setDef7(String def7) {
		this.def7 = def7;
	}

	/**
	 * 属性 def8的Getter方法.属性名：自定义项8 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef8() {
		return this.def8;
	}

	/**
	 * 属性def8的Setter方法.属性名：自定义项8 创建日期:2019-6-29
	 * 
	 * @param newDef8
	 *            java.lang.String
	 */
	public void setDef8(String def8) {
		this.def8 = def8;
	}

	/**
	 * 属性 def9的Getter方法.属性名：自定义项9 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef9() {
		return this.def9;
	}

	/**
	 * 属性def9的Setter方法.属性名：自定义项9 创建日期:2019-6-29
	 * 
	 * @param newDef9
	 *            java.lang.String
	 */
	public void setDef9(String def9) {
		this.def9 = def9;
	}

	/**
	 * 属性 def10的Getter方法.属性名：自定义项10 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef10() {
		return this.def10;
	}

	/**
	 * 属性def10的Setter方法.属性名：自定义项10 创建日期:2019-6-29
	 * 
	 * @param newDef10
	 *            java.lang.String
	 */
	public void setDef10(String def10) {
		this.def10 = def10;
	}

	/**
	 * 属性 def11的Getter方法.属性名：自定义项11 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef11() {
		return this.def11;
	}

	/**
	 * 属性def11的Setter方法.属性名：自定义项11 创建日期:2019-6-29
	 * 
	 * @param newDef11
	 *            java.lang.String
	 */
	public void setDef11(String def11) {
		this.def11 = def11;
	}

	/**
	 * 属性 def12的Getter方法.属性名：自定义项12 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef12() {
		return this.def12;
	}

	/**
	 * 属性def12的Setter方法.属性名：自定义项12 创建日期:2019-6-29
	 * 
	 * @param newDef12
	 *            java.lang.String
	 */
	public void setDef12(String def12) {
		this.def12 = def12;
	}

	/**
	 * 属性 def13的Getter方法.属性名：自定义项13 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef13() {
		return this.def13;
	}

	/**
	 * 属性def13的Setter方法.属性名：自定义项13 创建日期:2019-6-29
	 * 
	 * @param newDef13
	 *            java.lang.String
	 */
	public void setDef13(String def13) {
		this.def13 = def13;
	}

	/**
	 * 属性 def14的Getter方法.属性名：自定义项14 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef14() {
		return this.def14;
	}

	/**
	 * 属性def14的Setter方法.属性名：自定义项14 创建日期:2019-6-29
	 * 
	 * @param newDef14
	 *            java.lang.String
	 */
	public void setDef14(String def14) {
		this.def14 = def14;
	}

	/**
	 * 属性 def15的Getter方法.属性名：自定义项15 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef15() {
		return this.def15;
	}

	/**
	 * 属性def15的Setter方法.属性名：自定义项15 创建日期:2019-6-29
	 * 
	 * @param newDef15
	 *            java.lang.String
	 */
	public void setDef15(String def15) {
		this.def15 = def15;
	}

	/**
	 * 属性 def16的Getter方法.属性名：自定义项16 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef16() {
		return this.def16;
	}

	/**
	 * 属性def16的Setter方法.属性名：自定义项16 创建日期:2019-6-29
	 * 
	 * @param newDef16
	 *            java.lang.String
	 */
	public void setDef16(String def16) {
		this.def16 = def16;
	}

	/**
	 * 属性 def17的Getter方法.属性名：自定义项17 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef17() {
		return this.def17;
	}

	/**
	 * 属性def17的Setter方法.属性名：自定义项17 创建日期:2019-6-29
	 * 
	 * @param newDef17
	 *            java.lang.String
	 */
	public void setDef17(String def17) {
		this.def17 = def17;
	}

	/**
	 * 属性 def18的Getter方法.属性名：自定义项18 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef18() {
		return this.def18;
	}

	/**
	 * 属性def18的Setter方法.属性名：自定义项18 创建日期:2019-6-29
	 * 
	 * @param newDef18
	 *            java.lang.String
	 */
	public void setDef18(String def18) {
		this.def18 = def18;
	}

	/**
	 * 属性 def19的Getter方法.属性名：自定义项19 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef19() {
		return this.def19;
	}

	/**
	 * 属性def19的Setter方法.属性名：自定义项19 创建日期:2019-6-29
	 * 
	 * @param newDef19
	 *            java.lang.String
	 */
	public void setDef19(String def19) {
		this.def19 = def19;
	}

	/**
	 * 属性 def20的Getter方法.属性名：自定义项20 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef20() {
		return this.def20;
	}

	/**
	 * 属性def20的Setter方法.属性名：自定义项20 创建日期:2019-6-29
	 * 
	 * @param newDef20
	 *            java.lang.String
	 */
	public void setDef20(String def20) {
		this.def20 = def20;
	}

	/**
	 * 属性 def21的Getter方法.属性名：自定义项21 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef21() {
		return this.def21;
	}

	/**
	 * 属性def21的Setter方法.属性名：自定义项21 创建日期:2019-6-29
	 * 
	 * @param newDef21
	 *            java.lang.String
	 */
	public void setDef21(String def21) {
		this.def21 = def21;
	}

	/**
	 * 属性 def22的Getter方法.属性名：自定义项22 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef22() {
		return this.def22;
	}

	/**
	 * 属性def22的Setter方法.属性名：自定义项22 创建日期:2019-6-29
	 * 
	 * @param newDef22
	 *            java.lang.String
	 */
	public void setDef22(String def22) {
		this.def22 = def22;
	}

	/**
	 * 属性 def23的Getter方法.属性名：自定义项23 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef23() {
		return this.def23;
	}

	/**
	 * 属性def23的Setter方法.属性名：自定义项23 创建日期:2019-6-29
	 * 
	 * @param newDef23
	 *            java.lang.String
	 */
	public void setDef23(String def23) {
		this.def23 = def23;
	}

	/**
	 * 属性 def24的Getter方法.属性名：自定义项24 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef24() {
		return this.def24;
	}

	/**
	 * 属性def24的Setter方法.属性名：自定义项24 创建日期:2019-6-29
	 * 
	 * @param newDef24
	 *            java.lang.String
	 */
	public void setDef24(String def24) {
		this.def24 = def24;
	}

	/**
	 * 属性 def25的Getter方法.属性名：自定义项25 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef25() {
		return this.def25;
	}

	/**
	 * 属性def25的Setter方法.属性名：自定义项25 创建日期:2019-6-29
	 * 
	 * @param newDef25
	 *            java.lang.String
	 */
	public void setDef25(String def25) {
		this.def25 = def25;
	}

	/**
	 * 属性 def26的Getter方法.属性名：自定义项26 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef26() {
		return this.def26;
	}

	/**
	 * 属性def26的Setter方法.属性名：自定义项26 创建日期:2019-6-29
	 * 
	 * @param newDef26
	 *            java.lang.String
	 */
	public void setDef26(String def26) {
		this.def26 = def26;
	}

	/**
	 * 属性 def27的Getter方法.属性名：自定义项27 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef27() {
		return this.def27;
	}

	/**
	 * 属性def27的Setter方法.属性名：自定义项27 创建日期:2019-6-29
	 * 
	 * @param newDef27
	 *            java.lang.String
	 */
	public void setDef27(String def27) {
		this.def27 = def27;
	}

	/**
	 * 属性 def28的Getter方法.属性名：自定义项28 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef28() {
		return this.def28;
	}

	/**
	 * 属性def28的Setter方法.属性名：自定义项28 创建日期:2019-6-29
	 * 
	 * @param newDef28
	 *            java.lang.String
	 */
	public void setDef28(String def28) {
		this.def28 = def28;
	}

	/**
	 * 属性 def29的Getter方法.属性名：自定义项29 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef29() {
		return this.def29;
	}

	/**
	 * 属性def29的Setter方法.属性名：自定义项29 创建日期:2019-6-29
	 * 
	 * @param newDef29
	 *            java.lang.String
	 */
	public void setDef29(String def29) {
		this.def29 = def29;
	}

	/**
	 * 属性 def30的Getter方法.属性名：自定义项30 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getDef30() {
		return this.def30;
	}

	/**
	 * 属性def30的Setter方法.属性名：自定义项30 创建日期:2019-6-29
	 * 
	 * @param newDef30
	 *            java.lang.String
	 */
	public void setDef30(String def30) {
		this.def30 = def30;
	}

	/**
	 * 属性 billdate的Getter方法.属性名：业务日期 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getBilldate() {
		return this.billdate;
	}

	/**
	 * 属性billdate的Setter方法.属性名：业务日期 创建日期:2019-6-29
	 * 
	 * @param newBilldate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setBilldate(UFDate billdate) {
		this.billdate = billdate;
	}

	/**
	 * 属性 creator的Getter方法.属性名：创建人 创建日期:2019-6-29
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public String getCreator() {
		return this.creator;
	}

	/**
	 * 属性creator的Setter方法.属性名：创建人 创建日期:2019-6-29
	 * 
	 * @param newCreator
	 *            nc.vo.sm.UserVO
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 属性 creationtime的Getter方法.属性名：创建时间 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * 属性creationtime的Setter方法.属性名：创建时间 创建日期:2019-6-29
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * 属性 modifier的Getter方法.属性名：修改人 创建日期:2019-6-29
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public String getModifier() {
		return this.modifier;
	}

	/**
	 * 属性modifier的Setter方法.属性名：修改人 创建日期:2019-6-29
	 * 
	 * @param newModifier
	 *            nc.vo.sm.UserVO
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * 属性 modifiedtime的Getter方法.属性名：修改时间 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * 属性modifiedtime的Setter方法.属性名：修改时间 创建日期:2019-6-29
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
	 * 属性 生成时间戳的Getter方法.属性名：时间戳 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * 属性生成时间戳的Setter方法.属性名：时间戳 创建日期:2019-6-29
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
