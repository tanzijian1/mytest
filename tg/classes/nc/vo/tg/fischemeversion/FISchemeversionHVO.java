package nc.vo.tg.fischemeversion;

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
 *   此处添加累的描述信息
 * </p>
 *  创建日期:2019-6-23
 * @author 
 * @version NCPrj ??
 */
 
public class FISchemeversionHVO extends SuperVO {
	public static final String FISCEMEVERSIONNUM="fiscemeversionnum";//版本号
	   public Integer fiscemeversionnum;
		public Integer getFiscemeversionnum() {
		return fiscemeversionnum;
	}

	public void setFiscemeversionnum(Integer fiscemeversionnum) {
		this.fiscemeversionnum = fiscemeversionnum;
	}
/**
*融资方案版本表头主键
*/
public String pk_fiscemeversion;
/**
*集团
*/
public String pk_group;
/**
*组织
*/
public String pk_org;
/**
*组织版本
*/
public String pk_org_v;
/**
*单据号
*/
public String billno;
/**
*方案名称
*/
public String name;
/**
*审批状态
*/
public Integer approvestatus;
/**
*单据日期
*/
public UFDate dbilldate;
/**
*版本日期
*/
public UFDate billversiondate;
/**
*项目资料
*/
public String pk_project;
/**
*是否主推
*/
public UFBoolean bmain;
/**
*拟融资类型
*/
public String pk_organizationtype;
/**
*拟融资机构
*/
public String pk_organization;
/**
*拟融资金额
*/
public UFDouble nmy;
/**
*拟融资期限
*/
public String fiterm;
/**
*拟融资利率
*/
public UFDouble rate;
/**
*拟融资综合利率
*/
public UFDouble zhrate;
/**
*增信条件
*/
public String ecreditcondition;
/**
*还款条件
*/
public String repaymentcond;
/**
*跟进人
*/
public String followper;
/**
*方案负责人
*/
public String percharge;
/**
*创建人
*/
public String creator;
/**
*创建时间
*/
public UFDateTime creationtime;
/**
*修改人
*/
public String modifier;
/**
*修改时间
*/
public UFDateTime modifiedtime;
/**
*业务类型
*/
public String busitype;
/**
*审批人
*/
public String approver;
/**
*审批批语
*/
public String approvenote;
/**
*审批时间
*/
public UFDateTime approvedate;
/**
*交易类型
*/
public String transtype;
/**
*单据类型
*/
public String billtype;
/**
*交易类型pk
*/
public String transtypepk;
/**
*来源单据类型
*/
public String srcbilltype;
/**
*来源单据id
*/
public String srcbillid;
/**
*审批修订
*/
public Integer emendenum;
/**
*来源单号pk
*/
public String pk_scheme;
/**
*自定义项1
*/
public String vdef1;
/**
*自定义项2
*/
public String vdef2;
/**
*自定义项3
*/
public String vdef3;
/**
*自定义项4
*/
public String vdef4;
/**
*自定义项5
*/
public String vdef5;
/**
*自定义项6
*/
public String vdef6;
/**
*自定义项7
*/
public String vdef7;
/**
*自定义项8
*/
public String vdef8;
/**
*自定义项9
*/
public String vdef9;
/**
*自定义项10
*/
public String vdef10;
/**
*自定义项11
*/
public String vdef11;
/**
*自定义项12
*/
public String vdef12;
/**
*自定义项13
*/
public String vdef13;
/**
*自定义项14
*/
public String vdef14;
/**
*自定义项15
*/
public String vdef15;
/**
*自定义项16
*/
public String vdef16;
/**
*自定义项17
*/
public String vdef17;
/**
*自定义项18
*/
public String vdef18;
/**
*自定义项19
*/
public String vdef19;
/**
*自定义项20
*/
public String vdef20;
/**
*时间戳
*/
public UFDateTime ts;
    
    
//add by tjl
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
 * 自定义项31
 */
public String def31;
/**
 * 自定义项32
 */
public String def32;
/**
 * 自定义项33
 */
public String def33;
/**
 * 自定义项34
 */
public String def34;
/**
 * 自定义项35
 */
public String def35;
/**
 * 自定义项36
 */
public String def36;
/**
 * 自定义项37
 */
public String def37;
/**
 * 自定义项38
 */
public String def38;
/**
 * 自定义项39
 */
public String def39;
/**
 * 自定义项40
 */
public String def40;
/**
 * 自定义项41
 */
public String def41;
/**
 * 自定义项42
 */
public String def42;
/**
 * 自定义项43
 */
public String def43;
/**
 * 自定义项44
 */
public String def44;
/**
 * 自定义项45
 */
public String def45;
/**
 * 自定义项46
 */
public String def46;
/**
 * 自定义项47
 */
public String def47;
/**
 * 自定义项48
 */
public String def48;
/**
 * 自定义项49
 */
public String def49;
/**
 * 自定义项50
 */
public String def50;
/**
 * 自定义项51
 */
public String def51;
/**
 * 自定义项52
 */
public String def52;
/**
 * 自定义项53
 */
public String def53;
/**
 * 自定义项54
 */
public String def54;
/**
 * 自定义项55
 */
public String def55;
/**
 * 自定义项56
 */
public String def56;
/**
 * 自定义项57
 */
public String def57;
/**
 * 自定义项58
 */
public String def58;
/**
 * 自定义项59
 */
public String def59;
/**
 * 自定义项60
 */
public String def60;
/**
 * 大文本a
 */
public String big_text_a;
/**
 * 大文本b
 */
public String big_text_b;
/**
 * 大文本c
 */
public String big_text_c;
/**
 * 大文本d
 */
public String big_text_d;
/**
 * 大文本e
 */
public String big_text_e;

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

public String getBig_text_a() {
	return big_text_a;
}

public void setBig_text_a(String big_text_a) {
	this.big_text_a = big_text_a;
}

public String getBig_text_b() {
	return big_text_b;
}

public void setBig_text_b(String big_text_b) {
	this.big_text_b = big_text_b;
}

public String getBig_text_c() {
	return big_text_c;
}

public void setBig_text_c(String big_text_c) {
	this.big_text_c = big_text_c;
}

public String getBig_text_d() {
	return big_text_d;
}

public void setBig_text_d(String big_text_d) {
	this.big_text_d = big_text_d;
}

public String getBig_text_e() {
	return big_text_e;
}

public void setBig_text_e(String big_text_e) {
	this.big_text_e = big_text_e;
}
/**
* 属性 pk_fiscemeversion的Getter方法.属性名：融资方案版本表头主键
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getPk_fiscemeversion() {
return this.pk_fiscemeversion;
} 

/**
* 属性pk_fiscemeversion的Setter方法.属性名：融资方案版本表头主键
* 创建日期:2019-6-23
* @param newPk_fiscemeversion java.lang.String
*/
public void setPk_fiscemeversion ( String pk_fiscemeversion) {
this.pk_fiscemeversion=pk_fiscemeversion;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-6-23
* @return nc.vo.org.GroupVO
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-6-23
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-6-23
* @return nc.vo.org.OrgVO
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-6-23
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-6-23
* @return nc.vo.vorg.OrgVersionVO
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-6-23
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-6-23
* @param newBillno java.lang.String
*/
public void setBillno ( String billno) {
this.billno=billno;
} 
 
/**
* 属性 name的Getter方法.属性名：方案名称
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getName() {
return this.name;
} 

/**
* 属性name的Setter方法.属性名：方案名称
* 创建日期:2019-6-23
* @param newName java.lang.String
*/
public void setName ( String name) {
this.name=name;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-6-23
* @return nc.vo.pub.pf.BillStatusEnum
*/
public Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-6-23
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 dbilldate的Getter方法.属性名：单据日期
*  创建日期:2019-6-23
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDbilldate() {
return this.dbilldate;
} 

/**
* 属性dbilldate的Setter方法.属性名：单据日期
* 创建日期:2019-6-23
* @param newDbilldate nc.vo.pub.lang.UFDate
*/
public void setDbilldate ( UFDate dbilldate) {
this.dbilldate=dbilldate;
} 
 
/**
* 属性 billversiondate的Getter方法.属性名：版本日期
*  创建日期:2019-6-23
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBillversiondate() {
return this.billversiondate;
} 

/**
* 属性billversiondate的Setter方法.属性名：版本日期
* 创建日期:2019-6-23
* @param newBillversiondate nc.vo.pub.lang.UFDate
*/
public void setBillversiondate ( UFDate billversiondate) {
this.billversiondate=billversiondate;
} 
 
/**
* 属性 pk_project的Getter方法.属性名：项目资料
*  创建日期:2019-6-23
* @return nc.vo.tg.projectdata.ProjectDataVO
*/
public String getPk_project() {
return this.pk_project;
} 

/**
* 属性pk_project的Setter方法.属性名：项目资料
* 创建日期:2019-6-23
* @param newPk_project nc.vo.tg.projectdata.ProjectDataVO
*/
public void setPk_project ( String pk_project) {
this.pk_project=pk_project;
} 
 
/**
* 属性 bmain的Getter方法.属性名：是否主推
*  创建日期:2019-6-23
* @return nc.vo.pub.lang.UFBoolean
*/
public UFBoolean getBmain() {
return this.bmain;
} 

/**
* 属性bmain的Setter方法.属性名：是否主推
* 创建日期:2019-6-23
* @param newBmain nc.vo.pub.lang.UFBoolean
*/
public void setBmain ( UFBoolean bmain) {
this.bmain=bmain;
} 
 
/**
* 属性 pk_organizationtype的Getter方法.属性名：拟融资类型
*  创建日期:2019-6-23
* @return nc.vo.tg.fintype.FinTypeVO
*/
public String getPk_organizationtype() {
return this.pk_organizationtype;
} 

/**
* 属性pk_organizationtype的Setter方法.属性名：拟融资类型
* 创建日期:2019-6-23
* @param newPk_organizationtype nc.vo.tg.fintype.FinTypeVO
*/
public void setPk_organizationtype ( String pk_organizationtype) {
this.pk_organizationtype=pk_organizationtype;
} 
 
/**
* 属性 pk_organization的Getter方法.属性名：拟融资机构
*  创建日期:2019-6-23
* @return nc.vo.tg.organization.OrganizationVO
*/
public String getPk_organization() {
return this.pk_organization;
} 

/**
* 属性pk_organization的Setter方法.属性名：拟融资机构
* 创建日期:2019-6-23
* @param newPk_organization nc.vo.tg.organization.OrganizationVO
*/
public void setPk_organization ( String pk_organization) {
this.pk_organization=pk_organization;
} 
 
/**
* 属性 nmy的Getter方法.属性名：拟融资金额
*  创建日期:2019-6-23
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getNmy() {
return this.nmy;
} 

/**
* 属性nmy的Setter方法.属性名：拟融资金额
* 创建日期:2019-6-23
* @param newNmy nc.vo.pub.lang.UFDouble
*/
public void setNmy ( UFDouble nmy) {
this.nmy=nmy;
} 
 
/**
* 属性 fiterm的Getter方法.属性名：拟融资期限
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getFiterm() {
return this.fiterm;
} 

/**
* 属性fiterm的Setter方法.属性名：拟融资期限
* 创建日期:2019-6-23
* @param newFiterm java.lang.String
*/
public void setFiterm ( String fiterm) {
this.fiterm=fiterm;
} 
 
/**
* 属性 rate的Getter方法.属性名：拟融资利率
*  创建日期:2019-6-23
* @return nc.vo.fi.rateconfig.RateCodeVO
*/
public UFDouble getRate() {
return this.rate;
} 

/**
* 属性rate的Setter方法.属性名：拟融资利率
* 创建日期:2019-6-23
* @param newRate nc.vo.fi.rateconfig.RateCodeVO
*/
public void setRate ( UFDouble rate) {
this.rate=rate;
} 
 
/**
* 属性 zhrate的Getter方法.属性名：拟融资综合利率
*  创建日期:2019-6-23
* @return java.lang.String
*/
public UFDouble getZhrate() {
return this.zhrate;
} 

/**
* 属性zhrate的Setter方法.属性名：拟融资综合利率
* 创建日期:2019-6-23
* @param newZhrate java.lang.String
*/
public void setZhrate ( UFDouble zhrate) {
this.zhrate=zhrate;
} 
 
/**
* 属性 ecreditcondition的Getter方法.属性名：增信条件
*  创建日期:2019-6-23
* @return nc.vo.tg.fischeme.EcreditconditionEnum
*/
public String getEcreditcondition() {
return this.ecreditcondition;
} 

/**
* 属性ecreditcondition的Setter方法.属性名：增信条件
* 创建日期:2019-6-23
* @param newEcreditcondition nc.vo.tg.fischeme.EcreditconditionEnum
*/
public void setEcreditcondition ( String ecreditcondition) {
this.ecreditcondition=ecreditcondition;
} 
 
/**
* 属性 repaymentcond的Getter方法.属性名：还款条件
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getRepaymentcond() {
return this.repaymentcond;
} 

/**
* 属性repaymentcond的Setter方法.属性名：还款条件
* 创建日期:2019-6-23
* @param newRepaymentcond java.lang.String
*/
public void setRepaymentcond ( String repaymentcond) {
this.repaymentcond=repaymentcond;
} 
 
/**
* 属性 followper的Getter方法.属性名：跟进人
*  创建日期:2019-6-23
* @return nc.vo.bd.psn.PsndocVO
*/
public String getFollowper() {
return this.followper;
} 

/**
* 属性followper的Setter方法.属性名：跟进人
* 创建日期:2019-6-23
* @param newFollowper nc.vo.bd.psn.PsndocVO
*/
public void setFollowper ( String followper) {
this.followper=followper;
} 
 
/**
* 属性 percharge的Getter方法.属性名：方案负责人
*  创建日期:2019-6-23
* @return nc.vo.bd.psn.PsndocVO
*/
public String getPercharge() {
return this.percharge;
} 

/**
* 属性percharge的Setter方法.属性名：方案负责人
* 创建日期:2019-6-23
* @param newPercharge nc.vo.bd.psn.PsndocVO
*/
public void setPercharge ( String percharge) {
this.percharge=percharge;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-6-23
* @return nc.vo.sm.UserVO
*/
public String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-6-23
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-6-23
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-6-23
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2019-6-23
* @return nc.vo.sm.UserVO
*/
public String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2019-6-23
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2019-6-23
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2019-6-23
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2019-6-23
* @param newBusitype java.lang.String
*/
public void setBusitype ( String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-6-23
* @param newApprover java.lang.String
*/
public void setApprover ( String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-6-23
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-6-23
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-6-23
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2019-6-23
* @param newTranstype java.lang.String
*/
public void setTranstype ( String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-6-23
* @param newBilltype java.lang.String
*/
public void setBilltype ( String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2019-6-23
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2019-6-23
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2019-6-23
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：版本号
*  创建日期:2019-6-23
* @return java.lang.Integer
*/
public Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：版本号
* 创建日期:2019-6-23
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 pk_scheme的Getter方法.属性名：来源单号pk
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getPk_scheme() {
return this.pk_scheme;
} 

/**
* 属性pk_scheme的Setter方法.属性名：来源单号pk
* 创建日期:2019-6-23
* @param newPk_scheme java.lang.String
*/
public void setPk_scheme ( String pk_scheme) {
this.pk_scheme=pk_scheme;
} 
 
/**
* 属性 vdef1的Getter方法.属性名：自定义项1
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef1() {
return this.vdef1;
} 

/**
* 属性vdef1的Setter方法.属性名：自定义项1
* 创建日期:2019-6-23
* @param newVdef1 java.lang.String
*/
public void setVdef1 ( String vdef1) {
this.vdef1=vdef1;
} 
 
/**
* 属性 vdef2的Getter方法.属性名：自定义项2
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef2() {
return this.vdef2;
} 

/**
* 属性vdef2的Setter方法.属性名：自定义项2
* 创建日期:2019-6-23
* @param newVdef2 java.lang.String
*/
public void setVdef2 ( String vdef2) {
this.vdef2=vdef2;
} 
 
/**
* 属性 vdef3的Getter方法.属性名：自定义项3
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef3() {
return this.vdef3;
} 

/**
* 属性vdef3的Setter方法.属性名：自定义项3
* 创建日期:2019-6-23
* @param newVdef3 java.lang.String
*/
public void setVdef3 ( String vdef3) {
this.vdef3=vdef3;
} 
 
/**
* 属性 vdef4的Getter方法.属性名：自定义项4
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef4() {
return this.vdef4;
} 

/**
* 属性vdef4的Setter方法.属性名：自定义项4
* 创建日期:2019-6-23
* @param newVdef4 java.lang.String
*/
public void setVdef4 ( String vdef4) {
this.vdef4=vdef4;
} 
 
/**
* 属性 vdef5的Getter方法.属性名：自定义项5
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef5() {
return this.vdef5;
} 

/**
* 属性vdef5的Setter方法.属性名：自定义项5
* 创建日期:2019-6-23
* @param newVdef5 java.lang.String
*/
public void setVdef5 ( String vdef5) {
this.vdef5=vdef5;
} 
 
/**
* 属性 vdef6的Getter方法.属性名：自定义项6
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef6() {
return this.vdef6;
} 

/**
* 属性vdef6的Setter方法.属性名：自定义项6
* 创建日期:2019-6-23
* @param newVdef6 java.lang.String
*/
public void setVdef6 ( String vdef6) {
this.vdef6=vdef6;
} 
 
/**
* 属性 vdef7的Getter方法.属性名：自定义项7
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef7() {
return this.vdef7;
} 

/**
* 属性vdef7的Setter方法.属性名：自定义项7
* 创建日期:2019-6-23
* @param newVdef7 java.lang.String
*/
public void setVdef7 ( String vdef7) {
this.vdef7=vdef7;
} 
 
/**
* 属性 vdef8的Getter方法.属性名：自定义项8
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef8() {
return this.vdef8;
} 

/**
* 属性vdef8的Setter方法.属性名：自定义项8
* 创建日期:2019-6-23
* @param newVdef8 java.lang.String
*/
public void setVdef8 ( String vdef8) {
this.vdef8=vdef8;
} 
 
/**
* 属性 vdef9的Getter方法.属性名：自定义项9
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef9() {
return this.vdef9;
} 

/**
* 属性vdef9的Setter方法.属性名：自定义项9
* 创建日期:2019-6-23
* @param newVdef9 java.lang.String
*/
public void setVdef9 ( String vdef9) {
this.vdef9=vdef9;
} 
 
/**
* 属性 vdef10的Getter方法.属性名：自定义项10
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef10() {
return this.vdef10;
} 

/**
* 属性vdef10的Setter方法.属性名：自定义项10
* 创建日期:2019-6-23
* @param newVdef10 java.lang.String
*/
public void setVdef10 ( String vdef10) {
this.vdef10=vdef10;
} 
 
/**
* 属性 vdef11的Getter方法.属性名：自定义项11
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef11() {
return this.vdef11;
} 

/**
* 属性vdef11的Setter方法.属性名：自定义项11
* 创建日期:2019-6-23
* @param newVdef11 java.lang.String
*/
public void setVdef11 ( String vdef11) {
this.vdef11=vdef11;
} 
 
/**
* 属性 vdef12的Getter方法.属性名：自定义项12
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef12() {
return this.vdef12;
} 

/**
* 属性vdef12的Setter方法.属性名：自定义项12
* 创建日期:2019-6-23
* @param newVdef12 java.lang.String
*/
public void setVdef12 ( String vdef12) {
this.vdef12=vdef12;
} 
 
/**
* 属性 vdef13的Getter方法.属性名：自定义项13
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef13() {
return this.vdef13;
} 

/**
* 属性vdef13的Setter方法.属性名：自定义项13
* 创建日期:2019-6-23
* @param newVdef13 java.lang.String
*/
public void setVdef13 ( String vdef13) {
this.vdef13=vdef13;
} 
 
/**
* 属性 vdef14的Getter方法.属性名：自定义项14
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef14() {
return this.vdef14;
} 

/**
* 属性vdef14的Setter方法.属性名：自定义项14
* 创建日期:2019-6-23
* @param newVdef14 java.lang.String
*/
public void setVdef14 ( String vdef14) {
this.vdef14=vdef14;
} 
 
/**
* 属性 vdef15的Getter方法.属性名：自定义项15
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef15() {
return this.vdef15;
} 

/**
* 属性vdef15的Setter方法.属性名：自定义项15
* 创建日期:2019-6-23
* @param newVdef15 java.lang.String
*/
public void setVdef15 ( String vdef15) {
this.vdef15=vdef15;
} 
 
/**
* 属性 vdef16的Getter方法.属性名：自定义项16
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef16() {
return this.vdef16;
} 

/**
* 属性vdef16的Setter方法.属性名：自定义项16
* 创建日期:2019-6-23
* @param newVdef16 java.lang.String
*/
public void setVdef16 ( String vdef16) {
this.vdef16=vdef16;
} 
 
/**
* 属性 vdef17的Getter方法.属性名：自定义项17
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef17() {
return this.vdef17;
} 

/**
* 属性vdef17的Setter方法.属性名：自定义项17
* 创建日期:2019-6-23
* @param newVdef17 java.lang.String
*/
public void setVdef17 ( String vdef17) {
this.vdef17=vdef17;
} 
 
/**
* 属性 vdef18的Getter方法.属性名：自定义项18
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef18() {
return this.vdef18;
} 

/**
* 属性vdef18的Setter方法.属性名：自定义项18
* 创建日期:2019-6-23
* @param newVdef18 java.lang.String
*/
public void setVdef18 ( String vdef18) {
this.vdef18=vdef18;
} 
 
/**
* 属性 vdef19的Getter方法.属性名：自定义项19
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef19() {
return this.vdef19;
} 

/**
* 属性vdef19的Setter方法.属性名：自定义项19
* 创建日期:2019-6-23
* @param newVdef19 java.lang.String
*/
public void setVdef19 ( String vdef19) {
this.vdef19=vdef19;
} 
 
/**
* 属性 vdef20的Getter方法.属性名：自定义项20
*  创建日期:2019-6-23
* @return java.lang.String
*/
public String getVdef20() {
return this.vdef20;
} 

/**
* 属性vdef20的Setter方法.属性名：自定义项20
* 创建日期:2019-6-23
* @param newVdef20 java.lang.String
*/
public void setVdef20 ( String vdef20) {
this.vdef20=vdef20;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-6-23
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-6-23
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.tgrz_fiscemeversion");
    }
   }
    