package nc.vo.tg.ctar;

import java.io.Serializable;


/**
 * <b> ESB收款合同表头字段 </b>
 * <p>
 * 合同主表VO
 * </p>
 * 创建日期:2019-09-05 19:14:49
 * 
 * @author lhh
 * @version NC6.5
 */

public class CtArJsonVO implements Serializable{


	// 单据类型
	private String cbilltypecode;

	// 本位币
	private String ccurrencyid;

	// 原币币种
	private String corigcurrencyid;

	// 项目
	private String cprojectid;


	// 合同名称
	private String ctname;


	/** 交易类型pk */
	private String ctrantypeid;


	// 单据日期
	private String dbilldate;

	private String deliaddr;

	// 部门最新版本
	private String depid;

	// 计划终止日期
	private String invallidate;

	// 组织最新版本
	private String pk_org;


	// 合同签订日期
	private String subscribedate;


	// 计划生效日期
	private String valdate;

	// 合同编码
	private String vbillcode;

	// 板块
	private String vdef1;
	
	// 收款公司
	private String vdef2;
	
	// 甲方
	private String vdef3;

	// 乙方
	private String vdef4;

	// 丙方
	private String vdef5;

	// 丁方
	private String vdef6;

	// 戊方
	private String vdef7;

	// 己方
	private String vdef8;

	// bpm合同状态
	private String vdef9;

	// 签约金额
	private String vdef10;

	// 动态金额
	private String vdef11;

	// 税率
	private String vdef12;

	// 是否多合同税率
	private String vdef13;
	
	// 经办人（人员）
	private String personnelid;
	
	// 部门
	private String depid_v;

	// 合同管理人
	private String vdef14;

	// 自定义项15
	private String vdef15;

	// 自定义项16
	private String vdef16;

	// 自定义项17
	private String vdef17;

	// 自定义项18
	private String vdef18;

	// 自定义项19
	private String vdef19;
	
	// 自定义项20
	private String vdef20;
	
	//自定义项21
	private String vdef21;
	
	//自定义项22
	private String vdef22;
	
	//自定义项23
	private String vdef23;
	
	//自定义项24
	private String vdef24;
	
	//自定义项25
	private String vdef25;
	
	//自定义项26
	private String vdef26;
	
	//自定义项27
	private String vdef27;
	
	//自定义项28
	private String vdef28;
	
	//自定义项29
	private String vdef29;
	
	//自定义项30
	private String vdef30;
	

	// 交易类型编码
	private String vtrantypecode;

	

	// 银行账号
	private String bankaccount;
	
	private String pk_customer;
	
	private String vdef31;
	private String vdef32;
	private String vdef33;
	private String vdef34;
	private String vdef35;
	private String vdef36;
	private String vdef37;
	private String vdef38;
	private String vdef39;
	private String vdef40;
	private String vdef41;
	private String vdef42;
	private String vdef43;
	private String vdef44;
	private String vdef45;
	private String vdef46;
	private String vdef47;
	private String vdef48;
	private String vdef49;
	private String vdef50;
	private String vdef51;
	private String vdef52;
	private String vdef53;


	public String getPk_customer() {
		return pk_customer;
	}

	public void setPk_customer(String pk_customer) {
		this.pk_customer = pk_customer;
	}

	public String getCbilltypecode() {
		return cbilltypecode;
	}

	public void setCbilltypecode(String cbilltypecode) {
		this.cbilltypecode = cbilltypecode;
	}

	public String getCcurrencyid() {
		return ccurrencyid;
	}

	public void setCcurrencyid(String ccurrencyid) {
		this.ccurrencyid = ccurrencyid;
	}

	public String getCorigcurrencyid() {
		return corigcurrencyid;
	}

	public void setCorigcurrencyid(String corigcurrencyid) {
		this.corigcurrencyid = corigcurrencyid;
	}

	public String getCprojectid() {
		return cprojectid;
	}

	public void setCprojectid(String cprojectid) {
		this.cprojectid = cprojectid;
	}


	public String getCtname() {
		return ctname;
	}

	public void setCtname(String ctname) {
		this.ctname = ctname;
	}


	public String getCtrantypeid() {
		return ctrantypeid;
	}

	public void setCtrantypeid(String ctrantypeid) {
		this.ctrantypeid = ctrantypeid;
	}


	public String getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(String dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getDeliaddr() {
		return deliaddr;
	}

	public void setDeliaddr(String deliaddr) {
		this.deliaddr = deliaddr;
	}

	public String getDepid() {
		return depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

	public String getDepid_v() {
		return depid_v;
	}

	public void setDepid_v(String depidV) {
		depid_v = depidV;
	}


	public String getInvallidate() {
		return invallidate;
	}

	public void setInvallidate(String invallidate) {
		this.invallidate = invallidate;
	}


	/*public Integer getNinvctlstyle() {
		return ninvctlstyle;
	}

	public void setNinvctlstyle(Integer ninvctlstyle) {
		this.ninvctlstyle = ninvctlstyle;
	}*/



	public String getPersonnelid() {
		return personnelid;
	}

	public void setPersonnelid(String personnelid) {
		this.personnelid = personnelid;
	}


	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pkOrg) {
		pk_org = pkOrg;
	}


	public String getSubscribedate() {
		return subscribedate;
	}

	public void setSubscribedate(String subscribedate) {
		this.subscribedate = subscribedate;
	}



	public String getValdate() {
		return valdate;
	}

	public void setValdate(String valdate) {
		this.valdate = valdate;
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef10() {
		return vdef10;
	}

	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}

	public String getVdef11() {
		return vdef11;
	}

	public void setVdef11(String vdef11) {
		this.vdef11 = vdef11;
	}

	public String getVdef12() {
		return vdef12;
	}

	public void setVdef12(String vdef12) {
		this.vdef12 = vdef12;
	}

	public String getVdef13() {
		return vdef13;
	}

	public void setVdef13(String vdef13) {
		this.vdef13 = vdef13;
	}

	public String getVdef14() {
		return vdef14;
	}

	public void setVdef14(String vdef14) {
		this.vdef14 = vdef14;
	}

	public String getVdef15() {
		return vdef15;
	}

	public void setVdef15(String vdef15) {
		this.vdef15 = vdef15;
	}

	public String getVdef16() {
		return vdef16;
	}

	public void setVdef16(String vdef16) {
		this.vdef16 = vdef16;
	}

	public String getVdef17() {
		return vdef17;
	}

	public void setVdef17(String vdef17) {
		this.vdef17 = vdef17;
	}

	public String getVdef18() {
		return vdef18;
	}

	public void setVdef18(String vdef18) {
		this.vdef18 = vdef18;
	}

	public String getVdef19() {
		return vdef19;
	}

	public void setVdef19(String vdef19) {
		this.vdef19 = vdef19;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef20() {
		return vdef20;
	}

	public void setVdef20(String vdef20) {
		this.vdef20 = vdef20;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	public String getVdef6() {
		return vdef6;
	}

	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}

	public String getVdef7() {
		return vdef7;
	}

	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}

	public String getVdef8() {
		return vdef8;
	}

	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}

	public String getVdef9() {
		return vdef9;
	}

	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	
	public String getVdef21() {
		return vdef21;
	}

	public void setVdef21(String vdef21) {
		this.vdef21 = vdef21;
	}
	
	public String getVdef22() {
		return vdef22;
	}

	public void setVdef22(String vdef22) {
		this.vdef22 = vdef22;
	}
	
	public String getVdef23() {
		return vdef23;
	}

	public void setVdef23(String vdef23) {
		this.vdef23 = vdef23;
	}
	
	public String getVdef24() {
		return vdef24;
	}

	public void setVdef24(String vdef24) {
		this.vdef24 = vdef24;
	}
	
	public String getVdef25() {
		return vdef25;
	}

	public void setVdef25(String vdef25) {
		this.vdef25 = vdef25;
	}
	
	public String getVdef26() {
		return vdef26;
	}

	public void setVdef26(String vdef26) {
		this.vdef26 = vdef26;
	}
	
	public String getVdef27() {
		return vdef27;
	}

	public void setVdef27(String vdef27) {
		this.vdef27 = vdef27;
	}
	
	public String getVdef28() {
		return vdef28;
	}

	public void setVdef28(String vdef28) {
		this.vdef28 = vdef28;
	}
	
	public String getVdef29() {
		return vdef29;
	}

	public void setVdef29(String vdef29) {
		this.vdef29 = vdef29;
	}

	public String getVdef30() {
		return vdef30;
	}

	public void setVdef30(String vdef30) {
		this.vdef30 = vdef30;
	}
	

	public String getVdef31() {
		return vdef31;
	}

	public void setVdef31(String vdef31) {
		this.vdef31 = vdef31;
	}

	public String getVdef32() {
		return vdef32;
	}

	public void setVdef32(String vdef32) {
		this.vdef32 = vdef32;
	}

	public String getVdef33() {
		return vdef33;
	}

	public void setVdef33(String vdef33) {
		this.vdef33 = vdef33;
	}

	public String getVdef34() {
		return vdef34;
	}

	public void setVdef34(String vdef34) {
		this.vdef34 = vdef34;
	}

	public String getVdef35() {
		return vdef35;
	}

	public void setVdef35(String vdef35) {
		this.vdef35 = vdef35;
	}

	public String getVdef36() {
		return vdef36;
	}

	public void setVdef36(String vdef36) {
		this.vdef36 = vdef36;
	}

	public String getVdef37() {
		return vdef37;
	}

	public void setVdef37(String vdef37) {
		this.vdef37 = vdef37;
	}

	public String getVdef38() {
		return vdef38;
	}

	public void setVdef38(String vdef38) {
		this.vdef38 = vdef38;
	}

	public String getVdef39() {
		return vdef39;
	}

	public void setVdef39(String vdef39) {
		this.vdef39 = vdef39;
	}

	public String getVdef40() {
		return vdef40;
	}

	public void setVdef40(String vdef40) {
		this.vdef40 = vdef40;
	}

	public String getVdef41() {
		return vdef41;
	}

	public void setVdef41(String vdef41) {
		this.vdef41 = vdef41;
	}

	public String getVdef42() {
		return vdef42;
	}

	public void setVdef42(String vdef42) {
		this.vdef42 = vdef42;
	}

	public String getVdef43() {
		return vdef43;
	}

	public void setVdef43(String vdef43) {
		this.vdef43 = vdef43;
	}

	public String getVdef44() {
		return vdef44;
	}

	public void setVdef44(String vdef44) {
		this.vdef44 = vdef44;
	}

	public String getVdef45() {
		return vdef45;
	}

	public void setVdef45(String vdef45) {
		this.vdef45 = vdef45;
	}

	public String getVdef46() {
		return vdef46;
	}

	public void setVdef46(String vdef46) {
		this.vdef46 = vdef46;
	}

	public String getVdef47() {
		return vdef47;
	}

	public void setVdef47(String vdef47) {
		this.vdef47 = vdef47;
	}

	public String getVdef48() {
		return vdef48;
	}

	public void setVdef48(String vdef48) {
		this.vdef48 = vdef48;
	}

	public String getVdef49() {
		return vdef49;
	}

	public void setVdef49(String vdef49) {
		this.vdef49 = vdef49;
	}

	public String getVdef50() {
		return vdef50;
	}

	public void setVdef50(String vdef50) {
		this.vdef50 = vdef50;
	}

	public String getVdef51() {
		return vdef51;
	}

	public void setVdef51(String vdef51) {
		this.vdef51 = vdef51;
	}

	public String getVdef52() {
		return vdef52;
	}

	public void setVdef52(String vdef52) {
		this.vdef52 = vdef52;
	}

	public String getVdef53() {
		return vdef53;
	}

	public void setVdef53(String vdef53) {
		this.vdef53 = vdef53;
	}

	public String getVtrantypecode() {
		return vtrantypecode;
	}

	public void setVtrantypecode(String vtrantypecode) {
		this.vtrantypecode = vtrantypecode;
	}


	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	
	/**
   * 
   */
	private static final long serialVersionUID = -2234985987427154525L;

	/**
	 * 按照默认方式创建构造子. 创建日期:2010-03-18 19:14:49
	 */
	public CtArJsonVO() {
		super();
	}
	 // 实际终止日期
	  public static final String ACTUALINVALIDATE = "actualinvalidate";

	  // 实际生效日期
	  public static final String ACTUALVALIDATE = "actualvalidate";

	  // 审核人
	  public static final String APPROVER = "approver";

	  /** 制单人 */
	  public static final String BILLMAKER = "billmaker";

	  // 是否最新版本
	  public static final String BLATEST = "blatest";

	  // 已生成订单量作为合同执行
	  public static final String BORDERNUMEXEC = "bordernumexec";

	  // 委外
	  public static final String BSC = "bsc";

	  // 单据类型编码
	  public static final String CBILLTYPECODE = "cbilltypecode";

	  // 本位币
	  public static final String CCURRENCYID = "ccurrencyid";

	  // 原币币种
	  public static final String CORIGCURRENCYID = "corigcurrencyid";

	  // 项目
	  public static final String CPROJECTID = "cprojectid";

	  // 创建时间
	  public static final String CREATIONTIME = "creationtime";

	  // 创建人
	  public static final String CREATOR = "creator";

	  // 合同名称
	  public static final String CTNAME = "ctname";

	  // 合同名称2
	  public static final String CTNAME2 = "ctname2";

	  // 合同名称3
	  public static final String CTNAME3 = "ctname3";

	  // 合同名称4
	  public static final String CTNAME4 = "ctname4";

	  // 合同名称5
	  public static final String CTNAME5 = "ctname5";

	  // 合同名称6
	  public static final String CTNAME6 = "ctname6";

	  /** 交易类型 */
	  public static final String CTRANTYPEID = "ctrantypeid";

	  // 对方单位说明
	  public static final String CUSTUNIT = "custunit";

	  // 单据日期
	  public static final String DBILLDATE = "dbilldate";

	  // 交货地点
	  public static final String DELIADDR = "deliaddr";

	  // 部门最新版本
	  public static final String DEPID = "depid";

	  // 部门
	  public static final String DEPID_V = "depid_v";

	  // 制单日期
	  public static final String DMAKEDATE = "dmakedate";

	  // dr
	  public static final String DR = "dr";

	  // 合同状态
	  public static final String FSTATUSFLAG = "fstatusflag";

	  // 计划终止日期
	  public static final String INVALLIDATE = "invallidate";

	  // 打印次数
	  public static final String IPRINTCOUNT = "iprintcount";

	  // 修改时间
	  public static final String MODIFIEDTIME = "modifiedtime";

	  // 修改人
	  public static final String MODIFIER = "modifier";

	  // 折本汇率
	  public static final String NEXCHANGERATE = "nexchangerate";

	  // 全局本位币汇率
	  public static final String NGLOBALEXCHGRATE = "nglobalexchgrate";

	  // 集团本位币汇率
	  public static final String NGROUPEXCHGRATE = "ngroupexchgrate";

	  // 物料控制方式
	  public static final String NINVCTLSTYLE = "ninvctlstyle";

	  // 累计原币收付款金额
	  public static final String NORIGPSHAMOUNT = "norigpshamount";

	  // 原币预付款限额
	  public static final String NORIPREPAYLIMITMNY = "noriprepaylimitmny";

	  // 本币预付款限额
	  public static final String NPREPAYLIMITMNY = "nprepaylimitmny";

	  // 总数量
	  public static final String NTOTALASTNUM = "ntotalastnum";

	  // 累计本币收付款金额
	  public static final String NTOTALGPAMOUNT = "ntotalgpamount";

	  // 原币价税合计
	  public static final String NTOTALORIGMNY = "ntotalorigmny";

	  // 本币价税合计
	  public static final String NTOTALTAXMNY = "ntotaltaxmny";

	  // 人员
	  public static final String PERSONNELID = "personnelid";

	  // pk_billorg
	  public static final String PK_BILLORG = "pk_billorg";

	  // 集团
	  public static final String PK_GROUP = "pk_group";

	  // 采购组织最新版本
	  public static final String PK_ORG = "pk_org";

	  // 采购组织
	  public static final String PK_ORG_V = "pk_org_v";

	  // 原始id
	  public static final String PK_ORIGCT = "pk_origct";

	  // 收付款协议
	  public static final String PK_PAYTERM = "pk_payterm";

	  // 合同签订日期
	  public static final String SUBSCRIBEDATE = "subscribedate";

	  // 审核日期
	  public static final String TAUDITTIME = "taudittime";

	  // 时间戳
	  public static final String TS = "ts";

	  // 计划生效日期
	  public static final String VALDATE = "valdate";

	  // 合同编码
	  public static final String VBILLCODE = "vbillcode";

	  // 自定义项1
	  public static final String VDEF1 = "vdef1";
	  
	  // 自定义项2
	  public static final String VDEF2 = "vdef2";
	  
	  // 自定义项3
	  public static final String VDEF3 = "vdef3";

	  // 自定义项4
	  public static final String VDEF4 = "vdef4";

	  // 自定义项5
	  public static final String VDEF5 = "vdef5";

	  // 自定义项6
	  public static final String VDEF6 = "vdef6";

	  // 自定义项7
	  public static final String VDEF7 = "vdef7";

	  // 自定义项8
	  public static final String VDEF8 = "vdef8";

	  // 自定义项9
	  public static final String VDEF9 = "vdef9";

	  // 自定义项10
	  public static final String VDEF10 = "vdef10";

	  // 自定义项11
	  public static final String VDEF11 = "vdef11";

	  // 自定义项12
	  public static final String VDEF12 = "vdef12";

	  // 自定义项13
	  public static final String VDEF13 = "vdef13";

	  // 自定义项14
	  public static final String VDEF14 = "vdef14";

	  // 自定义项15
	  public static final String VDEF15 = "vdef15";

	  // 自定义项16
	  public static final String VDEF16 = "vdef16";

	  // 自定义项17
	  public static final String VDEF17 = "vdef17";

	  // 自定义项18
	  public static final String VDEF18 = "vdef18";

	  // 自定义项19
	  public static final String VDEF19 = "vdef19";

	  // 自定义项20
	  public static final String VDEF20 = "vdef20";
	  
	  // 自定义项21
	  public static final String VDEF21 = "vdef21";

	  // 自定义项22
	  public static final String VDEF22 = "vdef22";

	  // 自定义项23
	  public static final String VDEF23 = "vdef23";

	  // 自定义项24
	  public static final String VDEF24 = "vdef24";

	  // 自定义项25
	  public static final String VDEF25 = "vdef25";

	  // 自定义项26
	  public static final String VDEF26 = "vdef26";

	  // 自定义项27
	  public static final String VDEF27 = "vdef27";

	  // 自定义项28
	  public static final String VDEF28 = "vdef28";

	  // 自定义项29
	  public static final String VDEF29 = "vdef29";

	  // 自定义项30
	  public static final String VDEF30 = "vdef30";

	  // 版本号
	  public static final String VERSION = "version";

	  // 交易类型
	  public static final String VTRANTYPECODE = "vtrantypecode";
	  
	  //累计原币预收付款金额
	  public static final String NORIGPLANAMOUNT = "norigplanamount";
	  
	  //累计本币预收付款金额
	  public static final String NTOTALPLANAMOUNT = "ntotalplanamount";
	  
	  //累计原币开票金额
	  public static final String NORIGCOPAMOUNT = "norigcopamount";
	  
	  //累计本币开票金额
	  public static final String NTOTALCOPAMOUNT = "ntotalcopamount";
	  
	  //超合同比例
	  public static final String OVERRATE = "overrate";
	  
	  //承办组织
	  public static final String ORGANIZER = "organizer";
	  
	  //承办组织版本
	  public static final String ORGANIZER_V = "organizer_v";
	  
	  //签约组织
	  public static final String SIGNORG = "signorg";
	  
	  //签约组织版本
	  public static final String SIGNORG_V = "signorg_v";
	  
	  // 银行帐号
	  public static final String BANKACCOUNT = "bankaccount";
	  

}
