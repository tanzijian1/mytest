package nc.vo.tg.financingexpense;

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
 * 创建日期:2020-4-5
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class FinancexpenseVO extends SuperVO {

	/**
	 * 单据主键
	 */
	public java.lang.String pk_finexpense;
	/**
	 * 集团
	 */
	public java.lang.String pk_group;
	/**
	 * 组织
	 */
	public java.lang.String pk_org;
	/**
	 * 组织版本
	 */
	public java.lang.String pk_org_v;
	/**
	 * 单据ID
	 */
	public java.lang.String billid;
	/**
	 * 单据号
	 */
	public java.lang.String billno;
	/**
	 * 单据日期
	 */
	public UFDate dbilldate;
	/**
	 * 审批状态
	 */
	public java.lang.Integer approvestatus;
	/**
	 * 是否请款
	 */
	public UFBoolean isqk;
	/**
	 * 基本信息
	 */
	public java.lang.String message;
	/**
	 * 申请部门
	 */
	public java.lang.String pk_applicationdept;
	/**
	 * 标题
	 */
	public java.lang.String title;
	/**
	 * 申请人
	 */
	public java.lang.String pk_applicant;
	/**
	 * 申请日期
	 */
	public UFDate applicationdate;
	/**
	 * 申请公司
	 */
	public java.lang.String pk_applicationorg;
	/**
	 * 业务信息
	 */
	public java.lang.String businessmsg;
	/**
	 * 项目名称
	 */
	public java.lang.String pk_project;
	/**
	 * 收款单位
	 */
	public java.lang.String pk_payee;
	/**
	 * 付款单位
	 */
	public java.lang.String pk_payer;
	/**
	 * 合同金额
	 */
	public nc.vo.pub.lang.UFDouble contractmoney;
	/**
	 * 累计已付款金额
	 */
	public nc.vo.pub.lang.UFDouble paymentamount;
	/**
	 * 本次请款金额
	 */
	public nc.vo.pub.lang.UFDouble applyamount;
	/**
	 * 集团会计
	 */
	public java.lang.String pk_accountant;
	/**
	 * 出纳
	 */
	public java.lang.String pk_cashier;
	/**
	 * 用款内容
	 */
	public java.lang.String usecontent;
	/**
	 * 创建人
	 */
	public java.lang.String creator;
	/**
	 * 创建时间
	 */
	public UFDateTime creationtime;
	/**
	 * 修改人
	 */
	public java.lang.String modifier;
	/**
	 * 修改时间
	 */
	public UFDateTime modifiedtime;
	/**
	 * 业务类型
	 */
	public java.lang.String busitype;
	/**
	 * 审批人
	 */
	public java.lang.String approver;
	/**
	 * 审批批语
	 */
	public java.lang.String approvenote;
	/**
	 * 审批时间
	 */
	public UFDateTime approvedate;
	/**
	 * 交易类型
	 */
	public java.lang.String transtype;
	/**
	 * 单据类型
	 */
	public java.lang.String billtype;
	/**
	 * 交易类型pk
	 */
	public java.lang.String transtypepk;
	/**
	 * 是否付款后补票
	 */
	public nc.vo.pub.lang.UFDouble ispay;
	/**
	 * 摘要
	 */
	public java.lang.String summary;
	/**
	 * 财务部发票人员
	 */
	public java.lang.String noteman;
	/**
	 * 地区财务人员说明
	 */
	public java.lang.String explain;
	/**
	 * 承诺归还发票日期
	 */
	public UFDate notedate;
	/**
	 * 承诺归还发票日期说明
	 */
	public java.lang.String noteexplain;
	/**
	 * 未取得发票原因
	 */
	public java.lang.String reason;
	/**
	 * 来源单据类型
	 */
	public java.lang.String srcbilltype;
	/**
	 * 来源单据id
	 */
	public java.lang.String srcbillid;
	/**
	 * 修订枚举
	 */
	public java.lang.Integer emendenum;
	/**
	 * 单据版本pk
	 */
	public java.lang.String billversionpk;
	/**
	 * 自定义项1
	 */
	public java.lang.String def1;
	/**
	 * 自定义项2
	 */
	public java.lang.String def2;
	/**
	 * 自定义项3
	 */
	public java.lang.String def3;
	/**
	 * 自定义项4
	 */
	public java.lang.String def4;
	/**
	 * 自定义项5
	 */
	public java.lang.String def5;
	/**
	 * 自定义项6
	 */
	public java.lang.String def6;
	/**
	 * 自定义项7
	 */
	public java.lang.String def7;
	/**
	 * 自定义项8
	 */
	public java.lang.String def8;
	/**
	 * 自定义项9
	 */
	public java.lang.String def9;
	/**
	 * 自定义项10
	 */
	public java.lang.String def10;
	/**
	 * 自定义项11
	 */
	public java.lang.String def11;
	/**
	 * 自定义项12
	 */
	public java.lang.String def12;
	/**
	 * 自定义项13
	 */
	public java.lang.String def13;
	/**
	 * 自定义项14
	 */
	public java.lang.String def14;
	/**
	 * 自定义项15
	 */
	public java.lang.String def15;
	/**
	 * 自定义项16
	 */
	public java.lang.String def16;
	/**
	 * 自定义项17
	 */
	public java.lang.String def17;
	/**
	 * 自定义项18
	 */
	public java.lang.String def18;
	/**
	 * 自定义项19
	 */
	public java.lang.String def19;
	/**
	 * 自定义项20
	 */
	public java.lang.String def20;
	/**
	 * 自定义项21
	 */
	public java.lang.String def21;
	/**
	 * 自定义项22
	 */
	public java.lang.String def22;
	/**
	 * 自定义项23
	 */
	public java.lang.String def23;
	/**
	 * 自定义项24
	 */
	public java.lang.String def24;
	/**
	 * 自定义项25
	 */
	public java.lang.String def25;
	/**
	 * 自定义项26
	 */
	public java.lang.String def26;
	/**
	 * 自定义项27
	 */
	public java.lang.String def27;
	/**
	 * 自定义项28
	 */
	public java.lang.String def28;
	/**
	 * 自定义项29
	 */
	public java.lang.String def29;
	/**
	 * 自定义项30
	 */
	public java.lang.String def30;
	/**
	 * 自定义项31
	 */
	public java.lang.String def31;
	/**
	 * 自定义项32
	 */
	public java.lang.String def32;
	/**
	 * 自定义项33
	 */
	public java.lang.String def33;
	/**
	 * 自定义项34
	 */
	public java.lang.String def34;
	/**
	 * 自定义项35
	 */
	public java.lang.String def35;
	/**
	 * 自定义项36
	 */
	public java.lang.String def36;
	/**
	 * 自定义项37
	 */
	public java.lang.String def37;
	/**
	 * 自定义项38
	 */
	public java.lang.String def38;
	/**
	 * 自定义项39
	 */
	public java.lang.String def39;
	/**
	 * 自定义项40
	 */
	public java.lang.String def40;
	/**
	 * 自定义项41
	 */
	public java.lang.String def41;
	/**
	 * 自定义项42
	 */
	public java.lang.String def42;
	/**
	 * 自定义项43
	 */
	public java.lang.String def43;
	/**
	 * 自定义项44
	 */
	public java.lang.String def44;
	/**
	 * 自定义项45
	 */
	public java.lang.String def45;
	/**
	 * 自定义项46
	 */
	public java.lang.String def46;
	/**
	 * 自定义项47
	 */
	public java.lang.String def47;
	/**
	 * 自定义项48
	 */
	public java.lang.String def48;
	/**
	 * 自定义项49
	 */
	public java.lang.String def49;
	/**
	 * 自定义项50
	 */
	public java.lang.String def50;
	/**
	 * 自定义项51
	 */
	public java.lang.String def51;
	/**
	 * 自定义项52
	 */
	public java.lang.String def52;
	/**
	 * 自定义项53
	 */
	public java.lang.String def53;
	/**
	 * 自定义项54
	 */
	public java.lang.String def54;
	/**
	 * 自定义项55
	 */
	public java.lang.String def55;
	/**
	 * 自定义项56
	 */
	public java.lang.String def56;
	/**
	 * 自定义项57
	 */
	public java.lang.String def57;
	/**
	 * 自定义项58
	 */
	public java.lang.String def58;
	/**
	 * 自定义项59
	 */
	public java.lang.String def59;
	/**
	 * 自定义项60
	 */
	public java.lang.String def60;
	/**
	 * 自定义大文本a
	 */
	public java.lang.String big_text_a;
	/**
	 * 自定义大文本b
	 */
	public java.lang.String big_text_b;
	/**
	 * 自定义大文本c
	 */
	public java.lang.String big_text_c;
	/**
	 * 自定义项61
	 */
	public java.lang.String def61;
	/**
	 * 自定义项62
	 */
	public java.lang.String def62;
	/**
	 * 自定义项63
	 */
	public java.lang.String def63;
	/**
	 * 自定义项64
	 */
	public java.lang.String def64;
	/**
	 * 自定义项65
	 */
	public java.lang.String def65;
	/**
	 * 自定义项66
	 */
	public java.lang.String def66;
	/**
	 * 自定义项67
	 */
	public java.lang.String def67;
	/**
	 * 自定义项68
	 */
	public java.lang.String def68;
	/**
	 * 自定义项69
	 */
	public java.lang.String def69;
	/**
	 * 自定义项70
	 */
	public java.lang.String def70;
	/**
	 * 自定义项71
	 */
	public java.lang.String def71;
	/**
	 * 自定义项72
	 */
	public java.lang.String def72;
	/**
	 * 自定义项73
	 */
	public java.lang.String def73;
	/**
	 * 自定义项74
	 */
	public java.lang.String def74;
	/**
	 * 自定义项75
	 */
	public java.lang.String def75;
	/**
	 * 自定义项76
	 */
	public java.lang.String def76;
	/**
	 * 自定义项77
	 */
	public java.lang.String def77;
	/**
	 * 自定义项78
	 */
	public java.lang.String def78;
	/**
	 * 自定义项79
	 */
	public java.lang.String def79;
	/**
	 * 自定义项80
	 */
	public java.lang.String def80;
	/**
	 * 自定义项81
	 */
	public java.lang.String def81;
	/**
	 * 自定义项82
	 */
	public java.lang.String def82;
	/**
	 * 自定义项83
	 */
	public java.lang.String def83;
	/**
	 * 自定义项84
	 */
	public java.lang.String def84;
	/**
	 * 自定义项85
	 */
	public java.lang.String def85;
	/**
	 * 自定义项86
	 */
	public java.lang.String def86;
	/**
	 * 自定义项87
	 */
	public java.lang.String def87;
	/**
	 * 自定义项88
	 */
	public java.lang.String def88;
	/**
	 * 自定义项89
	 */
	public java.lang.String def89;
	/**
	 * 自定义项90
	 */
	public java.lang.String def90;
	/**
	 * 自定义项91
	 */
	public java.lang.String def91;
	/**
	 * 自定义项92
	 */
	public java.lang.String def92;
	/**
	 * 自定义项93
	 */
	public java.lang.String def93;
	/**
	 * 自定义项94
	 */
	public java.lang.String def94;
	/**
	 * 自定义项95
	 */
	public java.lang.String def95;
	/**
	 * 自定义项96
	 */
	public java.lang.String def96;
	/**
	 * 自定义项97
	 */
	public java.lang.String def97;
	/**
	 * 自定义项98
	 */
	public java.lang.String def98;
	/**
	 * 自定义项99
	 */
	public java.lang.String def99;
	/**
	 * 自定义项100
	 */
	public java.lang.String def100;
	/**
	 * 时间戳
	 */
	public UFDateTime ts;

	/**
	 * dr值
	 */
	public Integer dr;

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	/**
	 * 属性 pk_finexpense的Getter方法.属性名：单据主键 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_finexpense() {
		return this.pk_finexpense;
	}

	/**
	 * 属性pk_finexpense的Setter方法.属性名：单据主键 创建日期:2020-4-5
	 * 
	 * @param newPk_finexpense
	 *            java.lang.String
	 */
	public void setPk_finexpense(java.lang.String pk_finexpense) {
		this.pk_finexpense = pk_finexpense;
	}

	/**
	 * 属性 pk_group的Getter方法.属性名：集团 创建日期:2020-4-5
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public java.lang.String getPk_group() {
		return this.pk_group;
	}

	/**
	 * 属性pk_group的Setter方法.属性名：集团 创建日期:2020-4-5
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * 属性 pk_org的Getter方法.属性名：组织 创建日期:2020-4-5
	 * 
	 * @return nc.vo.org.OrgVO
	 */
	public java.lang.String getPk_org() {
		return this.pk_org;
	}

	/**
	 * 属性pk_org的Setter方法.属性名：组织 创建日期:2020-4-5
	 * 
	 * @param newPk_org
	 *            nc.vo.org.OrgVO
	 */
	public void setPk_org(java.lang.String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * 属性 pk_org_v的Getter方法.属性名：组织版本 创建日期:2020-4-5
	 * 
	 * @return nc.vo.vorg.OrgVersionVO
	 */
	public java.lang.String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * 属性pk_org_v的Setter方法.属性名：组织版本 创建日期:2020-4-5
	 * 
	 * @param newPk_org_v
	 *            nc.vo.vorg.OrgVersionVO
	 */
	public void setPk_org_v(java.lang.String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * 属性 billid的Getter方法.属性名：单据ID 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillid() {
		return this.billid;
	}

	/**
	 * 属性billid的Setter方法.属性名：单据ID 创建日期:2020-4-5
	 * 
	 * @param newBillid
	 *            java.lang.String
	 */
	public void setBillid(java.lang.String billid) {
		this.billid = billid;
	}

	/**
	 * 属性 billno的Getter方法.属性名：单据号 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillno() {
		return this.billno;
	}

	/**
	 * 属性billno的Setter方法.属性名：单据号 创建日期:2020-4-5
	 * 
	 * @param newBillno
	 *            java.lang.String
	 */
	public void setBillno(java.lang.String billno) {
		this.billno = billno;
	}

	/**
	 * 属性 dbilldate的Getter方法.属性名：单据日期 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDbilldate() {
		return this.dbilldate;
	}

	/**
	 * 属性dbilldate的Setter方法.属性名：单据日期 创建日期:2020-4-5
	 * 
	 * @param newDbilldate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	/**
	 * 属性 approvestatus的Getter方法.属性名：审批状态 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.pf.BillStatusEnum
	 */
	public java.lang.Integer getApprovestatus() {
		return this.approvestatus;
	}

	/**
	 * 属性approvestatus的Setter方法.属性名：审批状态 创建日期:2020-4-5
	 * 
	 * @param newApprovestatus
	 *            nc.vo.pub.pf.BillStatusEnum
	 */
	public void setApprovestatus(java.lang.Integer approvestatus) {
		this.approvestatus = approvestatus;
	}

	/**
	 * 属性 isqk的Getter方法.属性名：是否请款 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getIsqk() {
		return this.isqk;
	}

	/**
	 * 属性isqk的Setter方法.属性名：是否请款 创建日期:2020-4-5
	 * 
	 * @param newIsqk
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setIsqk(UFBoolean isqk) {
		this.isqk = isqk;
	}

	/**
	 * 属性 message的Getter方法.属性名：基本信息 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMessage() {
		return this.message;
	}

	/**
	 * 属性message的Setter方法.属性名：基本信息 创建日期:2020-4-5
	 * 
	 * @param newMessage
	 *            java.lang.String
	 */
	public void setMessage(java.lang.String message) {
		this.message = message;
	}

	/**
	 * 属性 pk_applicationdept的Getter方法.属性名：申请部门 创建日期:2020-4-5
	 * 
	 * @return nc.vo.org.DeptVO
	 */
	public java.lang.String getPk_applicationdept() {
		return this.pk_applicationdept;
	}

	/**
	 * 属性pk_applicationdept的Setter方法.属性名：申请部门 创建日期:2020-4-5
	 * 
	 * @param newPk_applicationdept
	 *            nc.vo.org.DeptVO
	 */
	public void setPk_applicationdept(java.lang.String pk_applicationdept) {
		this.pk_applicationdept = pk_applicationdept;
	}

	/**
	 * 属性 title的Getter方法.属性名：标题 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTitle() {
		return this.title;
	}

	/**
	 * 属性title的Setter方法.属性名：标题 创建日期:2020-4-5
	 * 
	 * @param newTitle
	 *            java.lang.String
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	/**
	 * 属性 pk_applicant的Getter方法.属性名：申请人 创建日期:2020-4-5
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPk_applicant() {
		return this.pk_applicant;
	}

	/**
	 * 属性pk_applicant的Setter方法.属性名：申请人 创建日期:2020-4-5
	 * 
	 * @param newPk_applicant
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPk_applicant(java.lang.String pk_applicant) {
		this.pk_applicant = pk_applicant;
	}

	/**
	 * 属性 applicationdate的Getter方法.属性名：申请日期 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getApplicationdate() {
		return this.applicationdate;
	}

	/**
	 * 属性applicationdate的Setter方法.属性名：申请日期 创建日期:2020-4-5
	 * 
	 * @param newApplicationdate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setApplicationdate(UFDate applicationdate) {
		this.applicationdate = applicationdate;
	}

	/**
	 * 属性 pk_applicationorg的Getter方法.属性名：申请公司 创建日期:2020-4-5
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getPk_applicationorg() {
		return this.pk_applicationorg;
	}

	/**
	 * 属性pk_applicationorg的Setter方法.属性名：申请公司 创建日期:2020-4-5
	 * 
	 * @param newPk_applicationorg
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setPk_applicationorg(java.lang.String pk_applicationorg) {
		this.pk_applicationorg = pk_applicationorg;
	}

	/**
	 * 属性 businessmsg的Getter方法.属性名：业务信息 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBusinessmsg() {
		return this.businessmsg;
	}

	/**
	 * 属性businessmsg的Setter方法.属性名：业务信息 创建日期:2020-4-5
	 * 
	 * @param newBusinessmsg
	 *            java.lang.String
	 */
	public void setBusinessmsg(java.lang.String businessmsg) {
		this.businessmsg = businessmsg;
	}

	/**
	 * 属性 pk_project的Getter方法.属性名：项目名称 创建日期:2020-4-5
	 * 
	 * @return nc.vo.tg.projectdata.ProjectDataVO
	 */
	public java.lang.String getPk_project() {
		return this.pk_project;
	}

	/**
	 * 属性pk_project的Setter方法.属性名：项目名称 创建日期:2020-4-5
	 * 
	 * @param newPk_project
	 *            nc.vo.tg.projectdata.ProjectDataVO
	 */
	public void setPk_project(java.lang.String pk_project) {
		this.pk_project = pk_project;
	}

	/**
	 * 属性 pk_payee的Getter方法.属性名：收款单位 创建日期:2020-4-5
	 * 
	 * @return nc.vo.bd.cust.CustSupplierVO
	 */
	public java.lang.String getPk_payee() {
		return this.pk_payee;
	}

	/**
	 * 属性pk_payee的Setter方法.属性名：收款单位 创建日期:2020-4-5
	 * 
	 * @param newPk_payee
	 *            nc.vo.bd.cust.CustSupplierVO
	 */
	public void setPk_payee(java.lang.String pk_payee) {
		this.pk_payee = pk_payee;
	}

	/**
	 * 属性 pk_payer的Getter方法.属性名：付款单位 创建日期:2020-4-5
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getPk_payer() {
		return this.pk_payer;
	}

	/**
	 * 属性pk_payer的Setter方法.属性名：付款单位 创建日期:2020-4-5
	 * 
	 * @param newPk_payer
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setPk_payer(java.lang.String pk_payer) {
		this.pk_payer = pk_payer;
	}

	/**
	 * 属性 contractmoney的Getter方法.属性名：合同金额 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getContractmoney() {
		return this.contractmoney;
	}

	/**
	 * 属性contractmoney的Setter方法.属性名：合同金额 创建日期:2020-4-5
	 * 
	 * @param newContractmoney
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setContractmoney(nc.vo.pub.lang.UFDouble contractmoney) {
		this.contractmoney = contractmoney;
	}

	/**
	 * 属性 paymentamount的Getter方法.属性名：累计已付款金额 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPaymentamount() {
		return this.paymentamount;
	}

	/**
	 * 属性paymentamount的Setter方法.属性名：累计已付款金额 创建日期:2020-4-5
	 * 
	 * @param newPaymentamount
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPaymentamount(nc.vo.pub.lang.UFDouble paymentamount) {
		this.paymentamount = paymentamount;
	}

	/**
	 * 属性 applyamount的Getter方法.属性名：本次请款金额 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getApplyamount() {
		return this.applyamount;
	}

	/**
	 * 属性applyamount的Setter方法.属性名：本次请款金额 创建日期:2020-4-5
	 * 
	 * @param newApplyamount
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setApplyamount(nc.vo.pub.lang.UFDouble applyamount) {
		this.applyamount = applyamount;
	}

	/**
	 * 属性 pk_accountant的Getter方法.属性名：集团会计 创建日期:2020-4-5
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPk_accountant() {
		return this.pk_accountant;
	}

	/**
	 * 属性pk_accountant的Setter方法.属性名：集团会计 创建日期:2020-4-5
	 * 
	 * @param newPk_accountant
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPk_accountant(java.lang.String pk_accountant) {
		this.pk_accountant = pk_accountant;
	}

	/**
	 * 属性 pk_cashier的Getter方法.属性名：出纳 创建日期:2020-4-5
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPk_cashier() {
		return this.pk_cashier;
	}

	/**
	 * 属性pk_cashier的Setter方法.属性名：出纳 创建日期:2020-4-5
	 * 
	 * @param newPk_cashier
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPk_cashier(java.lang.String pk_cashier) {
		this.pk_cashier = pk_cashier;
	}

	/**
	 * 属性 usecontent的Getter方法.属性名：用款内容 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUsecontent() {
		return this.usecontent;
	}

	/**
	 * 属性usecontent的Setter方法.属性名：用款内容 创建日期:2020-4-5
	 * 
	 * @param newUsecontent
	 *            java.lang.String
	 */
	public void setUsecontent(java.lang.String usecontent) {
		this.usecontent = usecontent;
	}

	/**
	 * 属性 creator的Getter方法.属性名：创建人 创建日期:2020-4-5
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getCreator() {
		return this.creator;
	}

	/**
	 * 属性creator的Setter方法.属性名：创建人 创建日期:2020-4-5
	 * 
	 * @param newCreator
	 *            nc.vo.sm.UserVO
	 */
	public void setCreator(java.lang.String creator) {
		this.creator = creator;
	}

	/**
	 * 属性 creationtime的Getter方法.属性名：创建时间 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * 属性creationtime的Setter方法.属性名：创建时间 创建日期:2020-4-5
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * 属性 modifier的Getter方法.属性名：修改人 创建日期:2020-4-5
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getModifier() {
		return this.modifier;
	}

	/**
	 * 属性modifier的Setter方法.属性名：修改人 创建日期:2020-4-5
	 * 
	 * @param newModifier
	 *            nc.vo.sm.UserVO
	 */
	public void setModifier(java.lang.String modifier) {
		this.modifier = modifier;
	}

	/**
	 * 属性 modifiedtime的Getter方法.属性名：修改时间 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * 属性modifiedtime的Setter方法.属性名：修改时间 创建日期:2020-4-5
	 * 
	 * @param newModifiedtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	/**
	 * 属性 busitype的Getter方法.属性名：业务类型 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBusitype() {
		return this.busitype;
	}

	/**
	 * 属性busitype的Setter方法.属性名：业务类型 创建日期:2020-4-5
	 * 
	 * @param newBusitype
	 *            java.lang.String
	 */
	public void setBusitype(java.lang.String busitype) {
		this.busitype = busitype;
	}

	/**
	 * 属性 approver的Getter方法.属性名：审批人 创建日期:2020-4-5
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getApprover() {
		return this.approver;
	}

	/**
	 * 属性approver的Setter方法.属性名：审批人 创建日期:2020-4-5
	 * 
	 * @param newApprover
	 *            nc.vo.sm.UserVO
	 */
	public void setApprover(java.lang.String approver) {
		this.approver = approver;
	}

	/**
	 * 属性 approvenote的Getter方法.属性名：审批批语 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getApprovenote() {
		return this.approvenote;
	}

	/**
	 * 属性approvenote的Setter方法.属性名：审批批语 创建日期:2020-4-5
	 * 
	 * @param newApprovenote
	 *            java.lang.String
	 */
	public void setApprovenote(java.lang.String approvenote) {
		this.approvenote = approvenote;
	}

	/**
	 * 属性 approvedate的Getter方法.属性名：审批时间 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getApprovedate() {
		return this.approvedate;
	}

	/**
	 * 属性approvedate的Setter方法.属性名：审批时间 创建日期:2020-4-5
	 * 
	 * @param newApprovedate
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setApprovedate(UFDateTime approvedate) {
		this.approvedate = approvedate;
	}

	/**
	 * 属性 transtype的Getter方法.属性名：交易类型 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTranstype() {
		return this.transtype;
	}

	/**
	 * 属性transtype的Setter方法.属性名：交易类型 创建日期:2020-4-5
	 * 
	 * @param newTranstype
	 *            java.lang.String
	 */
	public void setTranstype(java.lang.String transtype) {
		this.transtype = transtype;
	}

	/**
	 * 属性 billtype的Getter方法.属性名：单据类型 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBilltype() {
		return this.billtype;
	}

	/**
	 * 属性billtype的Setter方法.属性名：单据类型 创建日期:2020-4-5
	 * 
	 * @param newBilltype
	 *            java.lang.String
	 */
	public void setBilltype(java.lang.String billtype) {
		this.billtype = billtype;
	}

	/**
	 * 属性 transtypepk的Getter方法.属性名：交易类型pk 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.billtype.BilltypeVO
	 */
	public java.lang.String getTranstypepk() {
		return this.transtypepk;
	}

	/**
	 * 属性transtypepk的Setter方法.属性名：交易类型pk 创建日期:2020-4-5
	 * 
	 * @param newTranstypepk
	 *            nc.vo.pub.billtype.BilltypeVO
	 */
	public void setTranstypepk(java.lang.String transtypepk) {
		this.transtypepk = transtypepk;
	}

	/**
	 * 属性 ispay的Getter方法.属性名：是否付款后补票 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getIspay() {
		return this.ispay;
	}

	/**
	 * 属性ispay的Setter方法.属性名：是否付款后补票 创建日期:2020-4-5
	 * 
	 * @param newIspay
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setIspay(nc.vo.pub.lang.UFDouble ispay) {
		this.ispay = ispay;
	}

	/**
	 * 属性 summary的Getter方法.属性名：摘要 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSummary() {
		return this.summary;
	}

	/**
	 * 属性summary的Setter方法.属性名：摘要 创建日期:2020-4-5
	 * 
	 * @param newSummary
	 *            java.lang.String
	 */
	public void setSummary(java.lang.String summary) {
		this.summary = summary;
	}

	/**
	 * 属性 noteman的Getter方法.属性名：财务部发票人员 创建日期:2020-4-5
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getNoteman() {
		return this.noteman;
	}

	/**
	 * 属性noteman的Setter方法.属性名：财务部发票人员 创建日期:2020-4-5
	 * 
	 * @param newNoteman
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setNoteman(java.lang.String noteman) {
		this.noteman = noteman;
	}

	/**
	 * 属性 explain的Getter方法.属性名：地区财务人员说明 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getExplain() {
		return this.explain;
	}

	/**
	 * 属性explain的Setter方法.属性名：地区财务人员说明 创建日期:2020-4-5
	 * 
	 * @param newExplain
	 *            java.lang.String
	 */
	public void setExplain(java.lang.String explain) {
		this.explain = explain;
	}

	/**
	 * 属性 notedate的Getter方法.属性名：承诺归还发票日期 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNotedate() {
		return this.notedate;
	}

	/**
	 * 属性notedate的Setter方法.属性名：承诺归还发票日期 创建日期:2020-4-5
	 * 
	 * @param newNotedate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNotedate(UFDate notedate) {
		this.notedate = notedate;
	}

	/**
	 * 属性 noteexplain的Getter方法.属性名：承诺归还发票日期说明 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getNoteexplain() {
		return this.noteexplain;
	}

	/**
	 * 属性noteexplain的Setter方法.属性名：承诺归还发票日期说明 创建日期:2020-4-5
	 * 
	 * @param newNoteexplain
	 *            java.lang.String
	 */
	public void setNoteexplain(java.lang.String noteexplain) {
		this.noteexplain = noteexplain;
	}

	/**
	 * 属性 reason的Getter方法.属性名：未取得发票原因 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getReason() {
		return this.reason;
	}

	/**
	 * 属性reason的Setter方法.属性名：未取得发票原因 创建日期:2020-4-5
	 * 
	 * @param newReason
	 *            java.lang.String
	 */
	public void setReason(java.lang.String reason) {
		this.reason = reason;
	}

	/**
	 * 属性 srcbilltype的Getter方法.属性名：来源单据类型 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbilltype() {
		return this.srcbilltype;
	}

	/**
	 * 属性srcbilltype的Setter方法.属性名：来源单据类型 创建日期:2020-4-5
	 * 
	 * @param newSrcbilltype
	 *            java.lang.String
	 */
	public void setSrcbilltype(java.lang.String srcbilltype) {
		this.srcbilltype = srcbilltype;
	}

	/**
	 * 属性 srcbillid的Getter方法.属性名：来源单据id 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbillid() {
		return this.srcbillid;
	}

	/**
	 * 属性srcbillid的Setter方法.属性名：来源单据id 创建日期:2020-4-5
	 * 
	 * @param newSrcbillid
	 *            java.lang.String
	 */
	public void setSrcbillid(java.lang.String srcbillid) {
		this.srcbillid = srcbillid;
	}

	/**
	 * 属性 emendenum的Getter方法.属性名：修订枚举 创建日期:2020-4-5
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEmendenum() {
		return this.emendenum;
	}

	/**
	 * 属性emendenum的Setter方法.属性名：修订枚举 创建日期:2020-4-5
	 * 
	 * @param newEmendenum
	 *            java.lang.Integer
	 */
	public void setEmendenum(java.lang.Integer emendenum) {
		this.emendenum = emendenum;
	}

	/**
	 * 属性 billversionpk的Getter方法.属性名：单据版本pk 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillversionpk() {
		return this.billversionpk;
	}

	/**
	 * 属性billversionpk的Setter方法.属性名：单据版本pk 创建日期:2020-4-5
	 * 
	 * @param newBillversionpk
	 *            java.lang.String
	 */
	public void setBillversionpk(java.lang.String billversionpk) {
		this.billversionpk = billversionpk;
	}

	/**
	 * 属性 def1的Getter方法.属性名：自定义项1 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef1() {
		return this.def1;
	}

	/**
	 * 属性def1的Setter方法.属性名：自定义项1 创建日期:2020-4-5
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(java.lang.String def1) {
		this.def1 = def1;
	}

	/**
	 * 属性 def2的Getter方法.属性名：自定义项2 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef2() {
		return this.def2;
	}

	/**
	 * 属性def2的Setter方法.属性名：自定义项2 创建日期:2020-4-5
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(java.lang.String def2) {
		this.def2 = def2;
	}

	/**
	 * 属性 def3的Getter方法.属性名：自定义项3 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef3() {
		return this.def3;
	}

	/**
	 * 属性def3的Setter方法.属性名：自定义项3 创建日期:2020-4-5
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(java.lang.String def3) {
		this.def3 = def3;
	}

	/**
	 * 属性 def4的Getter方法.属性名：自定义项4 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef4() {
		return this.def4;
	}

	/**
	 * 属性def4的Setter方法.属性名：自定义项4 创建日期:2020-4-5
	 * 
	 * @param newDef4
	 *            java.lang.String
	 */
	public void setDef4(java.lang.String def4) {
		this.def4 = def4;
	}

	/**
	 * 属性 def5的Getter方法.属性名：自定义项5 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef5() {
		return this.def5;
	}

	/**
	 * 属性def5的Setter方法.属性名：自定义项5 创建日期:2020-4-5
	 * 
	 * @param newDef5
	 *            java.lang.String
	 */
	public void setDef5(java.lang.String def5) {
		this.def5 = def5;
	}

	/**
	 * 属性 def6的Getter方法.属性名：自定义项6 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef6() {
		return this.def6;
	}

	/**
	 * 属性def6的Setter方法.属性名：自定义项6 创建日期:2020-4-5
	 * 
	 * @param newDef6
	 *            java.lang.String
	 */
	public void setDef6(java.lang.String def6) {
		this.def6 = def6;
	}

	/**
	 * 属性 def7的Getter方法.属性名：自定义项7 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef7() {
		return this.def7;
	}

	/**
	 * 属性def7的Setter方法.属性名：自定义项7 创建日期:2020-4-5
	 * 
	 * @param newDef7
	 *            java.lang.String
	 */
	public void setDef7(java.lang.String def7) {
		this.def7 = def7;
	}

	/**
	 * 属性 def8的Getter方法.属性名：自定义项8 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef8() {
		return this.def8;
	}

	/**
	 * 属性def8的Setter方法.属性名：自定义项8 创建日期:2020-4-5
	 * 
	 * @param newDef8
	 *            java.lang.String
	 */
	public void setDef8(java.lang.String def8) {
		this.def8 = def8;
	}

	/**
	 * 属性 def9的Getter方法.属性名：自定义项9 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef9() {
		return this.def9;
	}

	/**
	 * 属性def9的Setter方法.属性名：自定义项9 创建日期:2020-4-5
	 * 
	 * @param newDef9
	 *            java.lang.String
	 */
	public void setDef9(java.lang.String def9) {
		this.def9 = def9;
	}

	/**
	 * 属性 def10的Getter方法.属性名：自定义项10 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef10() {
		return this.def10;
	}

	/**
	 * 属性def10的Setter方法.属性名：自定义项10 创建日期:2020-4-5
	 * 
	 * @param newDef10
	 *            java.lang.String
	 */
	public void setDef10(java.lang.String def10) {
		this.def10 = def10;
	}

	/**
	 * 属性 def11的Getter方法.属性名：自定义项11 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef11() {
		return this.def11;
	}

	/**
	 * 属性def11的Setter方法.属性名：自定义项11 创建日期:2020-4-5
	 * 
	 * @param newDef11
	 *            java.lang.String
	 */
	public void setDef11(java.lang.String def11) {
		this.def11 = def11;
	}

	/**
	 * 属性 def12的Getter方法.属性名：自定义项12 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef12() {
		return this.def12;
	}

	/**
	 * 属性def12的Setter方法.属性名：自定义项12 创建日期:2020-4-5
	 * 
	 * @param newDef12
	 *            java.lang.String
	 */
	public void setDef12(java.lang.String def12) {
		this.def12 = def12;
	}

	/**
	 * 属性 def13的Getter方法.属性名：自定义项13 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef13() {
		return this.def13;
	}

	/**
	 * 属性def13的Setter方法.属性名：自定义项13 创建日期:2020-4-5
	 * 
	 * @param newDef13
	 *            java.lang.String
	 */
	public void setDef13(java.lang.String def13) {
		this.def13 = def13;
	}

	/**
	 * 属性 def14的Getter方法.属性名：自定义项14 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef14() {
		return this.def14;
	}

	/**
	 * 属性def14的Setter方法.属性名：自定义项14 创建日期:2020-4-5
	 * 
	 * @param newDef14
	 *            java.lang.String
	 */
	public void setDef14(java.lang.String def14) {
		this.def14 = def14;
	}

	/**
	 * 属性 def15的Getter方法.属性名：自定义项15 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef15() {
		return this.def15;
	}

	/**
	 * 属性def15的Setter方法.属性名：自定义项15 创建日期:2020-4-5
	 * 
	 * @param newDef15
	 *            java.lang.String
	 */
	public void setDef15(java.lang.String def15) {
		this.def15 = def15;
	}

	/**
	 * 属性 def16的Getter方法.属性名：自定义项16 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef16() {
		return this.def16;
	}

	/**
	 * 属性def16的Setter方法.属性名：自定义项16 创建日期:2020-4-5
	 * 
	 * @param newDef16
	 *            java.lang.String
	 */
	public void setDef16(java.lang.String def16) {
		this.def16 = def16;
	}

	/**
	 * 属性 def17的Getter方法.属性名：自定义项17 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef17() {
		return this.def17;
	}

	/**
	 * 属性def17的Setter方法.属性名：自定义项17 创建日期:2020-4-5
	 * 
	 * @param newDef17
	 *            java.lang.String
	 */
	public void setDef17(java.lang.String def17) {
		this.def17 = def17;
	}

	/**
	 * 属性 def18的Getter方法.属性名：自定义项18 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef18() {
		return this.def18;
	}

	/**
	 * 属性def18的Setter方法.属性名：自定义项18 创建日期:2020-4-5
	 * 
	 * @param newDef18
	 *            java.lang.String
	 */
	public void setDef18(java.lang.String def18) {
		this.def18 = def18;
	}

	/**
	 * 属性 def19的Getter方法.属性名：自定义项19 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef19() {
		return this.def19;
	}

	/**
	 * 属性def19的Setter方法.属性名：自定义项19 创建日期:2020-4-5
	 * 
	 * @param newDef19
	 *            java.lang.String
	 */
	public void setDef19(java.lang.String def19) {
		this.def19 = def19;
	}

	/**
	 * 属性 def20的Getter方法.属性名：自定义项20 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef20() {
		return this.def20;
	}

	/**
	 * 属性def20的Setter方法.属性名：自定义项20 创建日期:2020-4-5
	 * 
	 * @param newDef20
	 *            java.lang.String
	 */
	public void setDef20(java.lang.String def20) {
		this.def20 = def20;
	}

	/**
	 * 属性 def21的Getter方法.属性名：自定义项21 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef21() {
		return this.def21;
	}

	/**
	 * 属性def21的Setter方法.属性名：自定义项21 创建日期:2020-4-5
	 * 
	 * @param newDef21
	 *            java.lang.String
	 */
	public void setDef21(java.lang.String def21) {
		this.def21 = def21;
	}

	/**
	 * 属性 def22的Getter方法.属性名：自定义项22 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef22() {
		return this.def22;
	}

	/**
	 * 属性def22的Setter方法.属性名：自定义项22 创建日期:2020-4-5
	 * 
	 * @param newDef22
	 *            java.lang.String
	 */
	public void setDef22(java.lang.String def22) {
		this.def22 = def22;
	}

	/**
	 * 属性 def23的Getter方法.属性名：自定义项23 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef23() {
		return this.def23;
	}

	/**
	 * 属性def23的Setter方法.属性名：自定义项23 创建日期:2020-4-5
	 * 
	 * @param newDef23
	 *            java.lang.String
	 */
	public void setDef23(java.lang.String def23) {
		this.def23 = def23;
	}

	/**
	 * 属性 def24的Getter方法.属性名：自定义项24 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef24() {
		return this.def24;
	}

	/**
	 * 属性def24的Setter方法.属性名：自定义项24 创建日期:2020-4-5
	 * 
	 * @param newDef24
	 *            java.lang.String
	 */
	public void setDef24(java.lang.String def24) {
		this.def24 = def24;
	}

	/**
	 * 属性 def25的Getter方法.属性名：自定义项25 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef25() {
		return this.def25;
	}

	/**
	 * 属性def25的Setter方法.属性名：自定义项25 创建日期:2020-4-5
	 * 
	 * @param newDef25
	 *            java.lang.String
	 */
	public void setDef25(java.lang.String def25) {
		this.def25 = def25;
	}

	/**
	 * 属性 def26的Getter方法.属性名：自定义项26 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef26() {
		return this.def26;
	}

	/**
	 * 属性def26的Setter方法.属性名：自定义项26 创建日期:2020-4-5
	 * 
	 * @param newDef26
	 *            java.lang.String
	 */
	public void setDef26(java.lang.String def26) {
		this.def26 = def26;
	}

	/**
	 * 属性 def27的Getter方法.属性名：自定义项27 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef27() {
		return this.def27;
	}

	/**
	 * 属性def27的Setter方法.属性名：自定义项27 创建日期:2020-4-5
	 * 
	 * @param newDef27
	 *            java.lang.String
	 */
	public void setDef27(java.lang.String def27) {
		this.def27 = def27;
	}

	/**
	 * 属性 def28的Getter方法.属性名：自定义项28 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef28() {
		return this.def28;
	}

	/**
	 * 属性def28的Setter方法.属性名：自定义项28 创建日期:2020-4-5
	 * 
	 * @param newDef28
	 *            java.lang.String
	 */
	public void setDef28(java.lang.String def28) {
		this.def28 = def28;
	}

	/**
	 * 属性 def29的Getter方法.属性名：自定义项29 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef29() {
		return this.def29;
	}

	/**
	 * 属性def29的Setter方法.属性名：自定义项29 创建日期:2020-4-5
	 * 
	 * @param newDef29
	 *            java.lang.String
	 */
	public void setDef29(java.lang.String def29) {
		this.def29 = def29;
	}

	/**
	 * 属性 def30的Getter方法.属性名：自定义项30 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef30() {
		return this.def30;
	}

	/**
	 * 属性def30的Setter方法.属性名：自定义项30 创建日期:2020-4-5
	 * 
	 * @param newDef30
	 *            java.lang.String
	 */
	public void setDef30(java.lang.String def30) {
		this.def30 = def30;
	}

	/**
	 * 属性 def31的Getter方法.属性名：自定义项31 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef31() {
		return this.def31;
	}

	/**
	 * 属性def31的Setter方法.属性名：自定义项31 创建日期:2020-4-5
	 * 
	 * @param newDef31
	 *            java.lang.String
	 */
	public void setDef31(java.lang.String def31) {
		this.def31 = def31;
	}

	/**
	 * 属性 def32的Getter方法.属性名：自定义项32 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef32() {
		return this.def32;
	}

	/**
	 * 属性def32的Setter方法.属性名：自定义项32 创建日期:2020-4-5
	 * 
	 * @param newDef32
	 *            java.lang.String
	 */
	public void setDef32(java.lang.String def32) {
		this.def32 = def32;
	}

	/**
	 * 属性 def33的Getter方法.属性名：自定义项33 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef33() {
		return this.def33;
	}

	/**
	 * 属性def33的Setter方法.属性名：自定义项33 创建日期:2020-4-5
	 * 
	 * @param newDef33
	 *            java.lang.String
	 */
	public void setDef33(java.lang.String def33) {
		this.def33 = def33;
	}

	/**
	 * 属性 def34的Getter方法.属性名：自定义项34 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef34() {
		return this.def34;
	}

	/**
	 * 属性def34的Setter方法.属性名：自定义项34 创建日期:2020-4-5
	 * 
	 * @param newDef34
	 *            java.lang.String
	 */
	public void setDef34(java.lang.String def34) {
		this.def34 = def34;
	}

	/**
	 * 属性 def35的Getter方法.属性名：自定义项35 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef35() {
		return this.def35;
	}

	/**
	 * 属性def35的Setter方法.属性名：自定义项35 创建日期:2020-4-5
	 * 
	 * @param newDef35
	 *            java.lang.String
	 */
	public void setDef35(java.lang.String def35) {
		this.def35 = def35;
	}

	/**
	 * 属性 def36的Getter方法.属性名：自定义项36 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef36() {
		return this.def36;
	}

	/**
	 * 属性def36的Setter方法.属性名：自定义项36 创建日期:2020-4-5
	 * 
	 * @param newDef36
	 *            java.lang.String
	 */
	public void setDef36(java.lang.String def36) {
		this.def36 = def36;
	}

	/**
	 * 属性 def37的Getter方法.属性名：自定义项37 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef37() {
		return this.def37;
	}

	/**
	 * 属性def37的Setter方法.属性名：自定义项37 创建日期:2020-4-5
	 * 
	 * @param newDef37
	 *            java.lang.String
	 */
	public void setDef37(java.lang.String def37) {
		this.def37 = def37;
	}

	/**
	 * 属性 def38的Getter方法.属性名：自定义项38 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef38() {
		return this.def38;
	}

	/**
	 * 属性def38的Setter方法.属性名：自定义项38 创建日期:2020-4-5
	 * 
	 * @param newDef38
	 *            java.lang.String
	 */
	public void setDef38(java.lang.String def38) {
		this.def38 = def38;
	}

	/**
	 * 属性 def39的Getter方法.属性名：自定义项39 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef39() {
		return this.def39;
	}

	/**
	 * 属性def39的Setter方法.属性名：自定义项39 创建日期:2020-4-5
	 * 
	 * @param newDef39
	 *            java.lang.String
	 */
	public void setDef39(java.lang.String def39) {
		this.def39 = def39;
	}

	/**
	 * 属性 def40的Getter方法.属性名：自定义项40 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef40() {
		return this.def40;
	}

	/**
	 * 属性def40的Setter方法.属性名：自定义项40 创建日期:2020-4-5
	 * 
	 * @param newDef40
	 *            java.lang.String
	 */
	public void setDef40(java.lang.String def40) {
		this.def40 = def40;
	}

	/**
	 * 属性 def41的Getter方法.属性名：自定义项41 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef41() {
		return this.def41;
	}

	/**
	 * 属性def41的Setter方法.属性名：自定义项41 创建日期:2020-4-5
	 * 
	 * @param newDef41
	 *            java.lang.String
	 */
	public void setDef41(java.lang.String def41) {
		this.def41 = def41;
	}

	/**
	 * 属性 def42的Getter方法.属性名：自定义项42 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef42() {
		return this.def42;
	}

	/**
	 * 属性def42的Setter方法.属性名：自定义项42 创建日期:2020-4-5
	 * 
	 * @param newDef42
	 *            java.lang.String
	 */
	public void setDef42(java.lang.String def42) {
		this.def42 = def42;
	}

	/**
	 * 属性 def43的Getter方法.属性名：自定义项43 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef43() {
		return this.def43;
	}

	/**
	 * 属性def43的Setter方法.属性名：自定义项43 创建日期:2020-4-5
	 * 
	 * @param newDef43
	 *            java.lang.String
	 */
	public void setDef43(java.lang.String def43) {
		this.def43 = def43;
	}

	/**
	 * 属性 def44的Getter方法.属性名：自定义项44 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef44() {
		return this.def44;
	}

	/**
	 * 属性def44的Setter方法.属性名：自定义项44 创建日期:2020-4-5
	 * 
	 * @param newDef44
	 *            java.lang.String
	 */
	public void setDef44(java.lang.String def44) {
		this.def44 = def44;
	}

	/**
	 * 属性 def45的Getter方法.属性名：自定义项45 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef45() {
		return this.def45;
	}

	/**
	 * 属性def45的Setter方法.属性名：自定义项45 创建日期:2020-4-5
	 * 
	 * @param newDef45
	 *            java.lang.String
	 */
	public void setDef45(java.lang.String def45) {
		this.def45 = def45;
	}

	/**
	 * 属性 def46的Getter方法.属性名：自定义项46 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef46() {
		return this.def46;
	}

	/**
	 * 属性def46的Setter方法.属性名：自定义项46 创建日期:2020-4-5
	 * 
	 * @param newDef46
	 *            java.lang.String
	 */
	public void setDef46(java.lang.String def46) {
		this.def46 = def46;
	}

	/**
	 * 属性 def47的Getter方法.属性名：自定义项47 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef47() {
		return this.def47;
	}

	/**
	 * 属性def47的Setter方法.属性名：自定义项47 创建日期:2020-4-5
	 * 
	 * @param newDef47
	 *            java.lang.String
	 */
	public void setDef47(java.lang.String def47) {
		this.def47 = def47;
	}

	/**
	 * 属性 def48的Getter方法.属性名：自定义项48 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef48() {
		return this.def48;
	}

	/**
	 * 属性def48的Setter方法.属性名：自定义项48 创建日期:2020-4-5
	 * 
	 * @param newDef48
	 *            java.lang.String
	 */
	public void setDef48(java.lang.String def48) {
		this.def48 = def48;
	}

	/**
	 * 属性 def49的Getter方法.属性名：自定义项49 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef49() {
		return this.def49;
	}

	/**
	 * 属性def49的Setter方法.属性名：自定义项49 创建日期:2020-4-5
	 * 
	 * @param newDef49
	 *            java.lang.String
	 */
	public void setDef49(java.lang.String def49) {
		this.def49 = def49;
	}

	/**
	 * 属性 def50的Getter方法.属性名：自定义项50 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef50() {
		return this.def50;
	}

	/**
	 * 属性def50的Setter方法.属性名：自定义项50 创建日期:2020-4-5
	 * 
	 * @param newDef50
	 *            java.lang.String
	 */
	public void setDef50(java.lang.String def50) {
		this.def50 = def50;
	}

	/**
	 * 属性 def51的Getter方法.属性名：自定义项51 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef51() {
		return this.def51;
	}

	/**
	 * 属性def51的Setter方法.属性名：自定义项51 创建日期:2020-4-5
	 * 
	 * @param newDef51
	 *            java.lang.String
	 */
	public void setDef51(java.lang.String def51) {
		this.def51 = def51;
	}

	/**
	 * 属性 def52的Getter方法.属性名：自定义项52 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef52() {
		return this.def52;
	}

	/**
	 * 属性def52的Setter方法.属性名：自定义项52 创建日期:2020-4-5
	 * 
	 * @param newDef52
	 *            java.lang.String
	 */
	public void setDef52(java.lang.String def52) {
		this.def52 = def52;
	}

	/**
	 * 属性 def53的Getter方法.属性名：自定义项53 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef53() {
		return this.def53;
	}

	/**
	 * 属性def53的Setter方法.属性名：自定义项53 创建日期:2020-4-5
	 * 
	 * @param newDef53
	 *            java.lang.String
	 */
	public void setDef53(java.lang.String def53) {
		this.def53 = def53;
	}

	/**
	 * 属性 def54的Getter方法.属性名：自定义项54 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef54() {
		return this.def54;
	}

	/**
	 * 属性def54的Setter方法.属性名：自定义项54 创建日期:2020-4-5
	 * 
	 * @param newDef54
	 *            java.lang.String
	 */
	public void setDef54(java.lang.String def54) {
		this.def54 = def54;
	}

	/**
	 * 属性 def55的Getter方法.属性名：自定义项55 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef55() {
		return this.def55;
	}

	/**
	 * 属性def55的Setter方法.属性名：自定义项55 创建日期:2020-4-5
	 * 
	 * @param newDef55
	 *            java.lang.String
	 */
	public void setDef55(java.lang.String def55) {
		this.def55 = def55;
	}

	/**
	 * 属性 def56的Getter方法.属性名：自定义项56 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef56() {
		return this.def56;
	}

	/**
	 * 属性def56的Setter方法.属性名：自定义项56 创建日期:2020-4-5
	 * 
	 * @param newDef56
	 *            java.lang.String
	 */
	public void setDef56(java.lang.String def56) {
		this.def56 = def56;
	}

	/**
	 * 属性 def57的Getter方法.属性名：自定义项57 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef57() {
		return this.def57;
	}

	/**
	 * 属性def57的Setter方法.属性名：自定义项57 创建日期:2020-4-5
	 * 
	 * @param newDef57
	 *            java.lang.String
	 */
	public void setDef57(java.lang.String def57) {
		this.def57 = def57;
	}

	/**
	 * 属性 def58的Getter方法.属性名：自定义项58 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef58() {
		return this.def58;
	}

	/**
	 * 属性def58的Setter方法.属性名：自定义项58 创建日期:2020-4-5
	 * 
	 * @param newDef58
	 *            java.lang.String
	 */
	public void setDef58(java.lang.String def58) {
		this.def58 = def58;
	}

	/**
	 * 属性 def59的Getter方法.属性名：自定义项59 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef59() {
		return this.def59;
	}

	/**
	 * 属性def59的Setter方法.属性名：自定义项59 创建日期:2020-4-5
	 * 
	 * @param newDef59
	 *            java.lang.String
	 */
	public void setDef59(java.lang.String def59) {
		this.def59 = def59;
	}

	/**
	 * 属性 def60的Getter方法.属性名：自定义项60 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef60() {
		return this.def60;
	}

	/**
	 * 属性def60的Setter方法.属性名：自定义项60 创建日期:2020-4-5
	 * 
	 * @param newDef60
	 *            java.lang.String
	 */
	public void setDef60(java.lang.String def60) {
		this.def60 = def60;
	}

	/**
	 * 属性 big_text_a的Getter方法.属性名：自定义大文本a 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBig_text_a() {
		return this.big_text_a;
	}

	/**
	 * 属性big_text_a的Setter方法.属性名：自定义大文本a 创建日期:2020-4-5
	 * 
	 * @param newBig_text_a
	 *            java.lang.String
	 */
	public void setBig_text_a(java.lang.String big_text_a) {
		this.big_text_a = big_text_a;
	}

	/**
	 * 属性 big_text_b的Getter方法.属性名：自定义大文本b 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBig_text_b() {
		return this.big_text_b;
	}

	/**
	 * 属性big_text_b的Setter方法.属性名：自定义大文本b 创建日期:2020-4-5
	 * 
	 * @param newBig_text_b
	 *            java.lang.String
	 */
	public void setBig_text_b(java.lang.String big_text_b) {
		this.big_text_b = big_text_b;
	}

	/**
	 * 属性 big_text_c的Getter方法.属性名：自定义大文本c 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBig_text_c() {
		return this.big_text_c;
	}

	/**
	 * 属性big_text_c的Setter方法.属性名：自定义大文本c 创建日期:2020-4-5
	 * 
	 * @param newBig_text_c
	 *            java.lang.String
	 */
	public void setBig_text_c(java.lang.String big_text_c) {
		this.big_text_c = big_text_c;
	}

	/**
	 * 属性 def61的Getter方法.属性名：自定义项61 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef61() {
		return this.def61;
	}

	/**
	 * 属性def61的Setter方法.属性名：自定义项61 创建日期:2020-4-5
	 * 
	 * @param newDef61
	 *            java.lang.String
	 */
	public void setDef61(java.lang.String def61) {
		this.def61 = def61;
	}

	/**
	 * 属性 def62的Getter方法.属性名：自定义项62 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef62() {
		return this.def62;
	}

	/**
	 * 属性def62的Setter方法.属性名：自定义项62 创建日期:2020-4-5
	 * 
	 * @param newDef62
	 *            java.lang.String
	 */
	public void setDef62(java.lang.String def62) {
		this.def62 = def62;
	}

	/**
	 * 属性 def63的Getter方法.属性名：自定义项63 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef63() {
		return this.def63;
	}

	/**
	 * 属性def63的Setter方法.属性名：自定义项63 创建日期:2020-4-5
	 * 
	 * @param newDef63
	 *            java.lang.String
	 */
	public void setDef63(java.lang.String def63) {
		this.def63 = def63;
	}

	/**
	 * 属性 def64的Getter方法.属性名：自定义项64 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef64() {
		return this.def64;
	}

	/**
	 * 属性def64的Setter方法.属性名：自定义项64 创建日期:2020-4-5
	 * 
	 * @param newDef64
	 *            java.lang.String
	 */
	public void setDef64(java.lang.String def64) {
		this.def64 = def64;
	}

	/**
	 * 属性 def65的Getter方法.属性名：自定义项65 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef65() {
		return this.def65;
	}

	/**
	 * 属性def65的Setter方法.属性名：自定义项65 创建日期:2020-4-5
	 * 
	 * @param newDef65
	 *            java.lang.String
	 */
	public void setDef65(java.lang.String def65) {
		this.def65 = def65;
	}

	/**
	 * 属性 def66的Getter方法.属性名：自定义项66 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef66() {
		return this.def66;
	}

	/**
	 * 属性def66的Setter方法.属性名：自定义项66 创建日期:2020-4-5
	 * 
	 * @param newDef66
	 *            java.lang.String
	 */
	public void setDef66(java.lang.String def66) {
		this.def66 = def66;
	}

	/**
	 * 属性 def67的Getter方法.属性名：自定义项67 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef67() {
		return this.def67;
	}

	/**
	 * 属性def67的Setter方法.属性名：自定义项67 创建日期:2020-4-5
	 * 
	 * @param newDef67
	 *            java.lang.String
	 */
	public void setDef67(java.lang.String def67) {
		this.def67 = def67;
	}

	/**
	 * 属性 def68的Getter方法.属性名：自定义项68 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef68() {
		return this.def68;
	}

	/**
	 * 属性def68的Setter方法.属性名：自定义项68 创建日期:2020-4-5
	 * 
	 * @param newDef68
	 *            java.lang.String
	 */
	public void setDef68(java.lang.String def68) {
		this.def68 = def68;
	}

	/**
	 * 属性 def69的Getter方法.属性名：自定义项69 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef69() {
		return this.def69;
	}

	/**
	 * 属性def69的Setter方法.属性名：自定义项69 创建日期:2020-4-5
	 * 
	 * @param newDef69
	 *            java.lang.String
	 */
	public void setDef69(java.lang.String def69) {
		this.def69 = def69;
	}

	/**
	 * 属性 def70的Getter方法.属性名：自定义项70 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef70() {
		return this.def70;
	}

	/**
	 * 属性def70的Setter方法.属性名：自定义项70 创建日期:2020-4-5
	 * 
	 * @param newDef70
	 *            java.lang.String
	 */
	public void setDef70(java.lang.String def70) {
		this.def70 = def70;
	}

	/**
	 * 属性 def71的Getter方法.属性名：自定义项71 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef71() {
		return this.def71;
	}

	/**
	 * 属性def71的Setter方法.属性名：自定义项71 创建日期:2020-4-5
	 * 
	 * @param newDef71
	 *            java.lang.String
	 */
	public void setDef71(java.lang.String def71) {
		this.def71 = def71;
	}

	/**
	 * 属性 def72的Getter方法.属性名：自定义项72 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef72() {
		return this.def72;
	}

	/**
	 * 属性def72的Setter方法.属性名：自定义项72 创建日期:2020-4-5
	 * 
	 * @param newDef72
	 *            java.lang.String
	 */
	public void setDef72(java.lang.String def72) {
		this.def72 = def72;
	}

	/**
	 * 属性 def73的Getter方法.属性名：自定义项73 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef73() {
		return this.def73;
	}

	/**
	 * 属性def73的Setter方法.属性名：自定义项73 创建日期:2020-4-5
	 * 
	 * @param newDef73
	 *            java.lang.String
	 */
	public void setDef73(java.lang.String def73) {
		this.def73 = def73;
	}

	/**
	 * 属性 def74的Getter方法.属性名：自定义项74 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef74() {
		return this.def74;
	}

	/**
	 * 属性def74的Setter方法.属性名：自定义项74 创建日期:2020-4-5
	 * 
	 * @param newDef74
	 *            java.lang.String
	 */
	public void setDef74(java.lang.String def74) {
		this.def74 = def74;
	}

	/**
	 * 属性 def75的Getter方法.属性名：自定义项75 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef75() {
		return this.def75;
	}

	/**
	 * 属性def75的Setter方法.属性名：自定义项75 创建日期:2020-4-5
	 * 
	 * @param newDef75
	 *            java.lang.String
	 */
	public void setDef75(java.lang.String def75) {
		this.def75 = def75;
	}

	/**
	 * 属性 def76的Getter方法.属性名：自定义项76 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef76() {
		return this.def76;
	}

	/**
	 * 属性def76的Setter方法.属性名：自定义项76 创建日期:2020-4-5
	 * 
	 * @param newDef76
	 *            java.lang.String
	 */
	public void setDef76(java.lang.String def76) {
		this.def76 = def76;
	}

	/**
	 * 属性 def77的Getter方法.属性名：自定义项77 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef77() {
		return this.def77;
	}

	/**
	 * 属性def77的Setter方法.属性名：自定义项77 创建日期:2020-4-5
	 * 
	 * @param newDef77
	 *            java.lang.String
	 */
	public void setDef77(java.lang.String def77) {
		this.def77 = def77;
	}

	/**
	 * 属性 def78的Getter方法.属性名：自定义项78 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef78() {
		return this.def78;
	}

	/**
	 * 属性def78的Setter方法.属性名：自定义项78 创建日期:2020-4-5
	 * 
	 * @param newDef78
	 *            java.lang.String
	 */
	public void setDef78(java.lang.String def78) {
		this.def78 = def78;
	}

	/**
	 * 属性 def79的Getter方法.属性名：自定义项79 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef79() {
		return this.def79;
	}

	/**
	 * 属性def79的Setter方法.属性名：自定义项79 创建日期:2020-4-5
	 * 
	 * @param newDef79
	 *            java.lang.String
	 */
	public void setDef79(java.lang.String def79) {
		this.def79 = def79;
	}

	/**
	 * 属性 def80的Getter方法.属性名：自定义项80 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef80() {
		return this.def80;
	}

	/**
	 * 属性def80的Setter方法.属性名：自定义项80 创建日期:2020-4-5
	 * 
	 * @param newDef80
	 *            java.lang.String
	 */
	public void setDef80(java.lang.String def80) {
		this.def80 = def80;
	}

	/**
	 * 属性 def81的Getter方法.属性名：自定义项81 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef81() {
		return this.def81;
	}

	/**
	 * 属性def81的Setter方法.属性名：自定义项81 创建日期:2020-4-5
	 * 
	 * @param newDef81
	 *            java.lang.String
	 */
	public void setDef81(java.lang.String def81) {
		this.def81 = def81;
	}

	/**
	 * 属性 def82的Getter方法.属性名：自定义项82 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef82() {
		return this.def82;
	}

	/**
	 * 属性def82的Setter方法.属性名：自定义项82 创建日期:2020-4-5
	 * 
	 * @param newDef82
	 *            java.lang.String
	 */
	public void setDef82(java.lang.String def82) {
		this.def82 = def82;
	}

	/**
	 * 属性 def83的Getter方法.属性名：自定义项83 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef83() {
		return this.def83;
	}

	/**
	 * 属性def83的Setter方法.属性名：自定义项83 创建日期:2020-4-5
	 * 
	 * @param newDef83
	 *            java.lang.String
	 */
	public void setDef83(java.lang.String def83) {
		this.def83 = def83;
	}

	/**
	 * 属性 def84的Getter方法.属性名：自定义项84 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef84() {
		return this.def84;
	}

	/**
	 * 属性def84的Setter方法.属性名：自定义项84 创建日期:2020-4-5
	 * 
	 * @param newDef84
	 *            java.lang.String
	 */
	public void setDef84(java.lang.String def84) {
		this.def84 = def84;
	}

	/**
	 * 属性 def85的Getter方法.属性名：自定义项85 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef85() {
		return this.def85;
	}

	/**
	 * 属性def85的Setter方法.属性名：自定义项85 创建日期:2020-4-5
	 * 
	 * @param newDef85
	 *            java.lang.String
	 */
	public void setDef85(java.lang.String def85) {
		this.def85 = def85;
	}

	/**
	 * 属性 def86的Getter方法.属性名：自定义项86 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef86() {
		return this.def86;
	}

	/**
	 * 属性def86的Setter方法.属性名：自定义项86 创建日期:2020-4-5
	 * 
	 * @param newDef86
	 *            java.lang.String
	 */
	public void setDef86(java.lang.String def86) {
		this.def86 = def86;
	}

	/**
	 * 属性 def87的Getter方法.属性名：自定义项87 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef87() {
		return this.def87;
	}

	/**
	 * 属性def87的Setter方法.属性名：自定义项87 创建日期:2020-4-5
	 * 
	 * @param newDef87
	 *            java.lang.String
	 */
	public void setDef87(java.lang.String def87) {
		this.def87 = def87;
	}

	/**
	 * 属性 def88的Getter方法.属性名：自定义项88 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef88() {
		return this.def88;
	}

	/**
	 * 属性def88的Setter方法.属性名：自定义项88 创建日期:2020-4-5
	 * 
	 * @param newDef88
	 *            java.lang.String
	 */
	public void setDef88(java.lang.String def88) {
		this.def88 = def88;
	}

	/**
	 * 属性 def89的Getter方法.属性名：自定义项89 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef89() {
		return this.def89;
	}

	/**
	 * 属性def89的Setter方法.属性名：自定义项89 创建日期:2020-4-5
	 * 
	 * @param newDef89
	 *            java.lang.String
	 */
	public void setDef89(java.lang.String def89) {
		this.def89 = def89;
	}

	/**
	 * 属性 def90的Getter方法.属性名：自定义项90 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef90() {
		return this.def90;
	}

	/**
	 * 属性def90的Setter方法.属性名：自定义项90 创建日期:2020-4-5
	 * 
	 * @param newDef90
	 *            java.lang.String
	 */
	public void setDef90(java.lang.String def90) {
		this.def90 = def90;
	}

	/**
	 * 属性 def91的Getter方法.属性名：自定义项91 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef91() {
		return this.def91;
	}

	/**
	 * 属性def91的Setter方法.属性名：自定义项91 创建日期:2020-4-5
	 * 
	 * @param newDef91
	 *            java.lang.String
	 */
	public void setDef91(java.lang.String def91) {
		this.def91 = def91;
	}

	/**
	 * 属性 def92的Getter方法.属性名：自定义项92 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef92() {
		return this.def92;
	}

	/**
	 * 属性def92的Setter方法.属性名：自定义项92 创建日期:2020-4-5
	 * 
	 * @param newDef92
	 *            java.lang.String
	 */
	public void setDef92(java.lang.String def92) {
		this.def92 = def92;
	}

	/**
	 * 属性 def93的Getter方法.属性名：自定义项93 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef93() {
		return this.def93;
	}

	/**
	 * 属性def93的Setter方法.属性名：自定义项93 创建日期:2020-4-5
	 * 
	 * @param newDef93
	 *            java.lang.String
	 */
	public void setDef93(java.lang.String def93) {
		this.def93 = def93;
	}

	/**
	 * 属性 def94的Getter方法.属性名：自定义项94 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef94() {
		return this.def94;
	}

	/**
	 * 属性def94的Setter方法.属性名：自定义项94 创建日期:2020-4-5
	 * 
	 * @param newDef94
	 *            java.lang.String
	 */
	public void setDef94(java.lang.String def94) {
		this.def94 = def94;
	}

	/**
	 * 属性 def95的Getter方法.属性名：自定义项95 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef95() {
		return this.def95;
	}

	/**
	 * 属性def95的Setter方法.属性名：自定义项95 创建日期:2020-4-5
	 * 
	 * @param newDef95
	 *            java.lang.String
	 */
	public void setDef95(java.lang.String def95) {
		this.def95 = def95;
	}

	/**
	 * 属性 def96的Getter方法.属性名：自定义项96 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef96() {
		return this.def96;
	}

	/**
	 * 属性def96的Setter方法.属性名：自定义项96 创建日期:2020-4-5
	 * 
	 * @param newDef96
	 *            java.lang.String
	 */
	public void setDef96(java.lang.String def96) {
		this.def96 = def96;
	}

	/**
	 * 属性 def97的Getter方法.属性名：自定义项97 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef97() {
		return this.def97;
	}

	/**
	 * 属性def97的Setter方法.属性名：自定义项97 创建日期:2020-4-5
	 * 
	 * @param newDef97
	 *            java.lang.String
	 */
	public void setDef97(java.lang.String def97) {
		this.def97 = def97;
	}

	/**
	 * 属性 def98的Getter方法.属性名：自定义项98 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef98() {
		return this.def98;
	}

	/**
	 * 属性def98的Setter方法.属性名：自定义项98 创建日期:2020-4-5
	 * 
	 * @param newDef98
	 *            java.lang.String
	 */
	public void setDef98(java.lang.String def98) {
		this.def98 = def98;
	}

	/**
	 * 属性 def99的Getter方法.属性名：自定义项99 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef99() {
		return this.def99;
	}

	/**
	 * 属性def99的Setter方法.属性名：自定义项99 创建日期:2020-4-5
	 * 
	 * @param newDef99
	 *            java.lang.String
	 */
	public void setDef99(java.lang.String def99) {
		this.def99 = def99;
	}

	/**
	 * 属性 def100的Getter方法.属性名：自定义项100 创建日期:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef100() {
		return this.def100;
	}

	/**
	 * 属性def100的Setter方法.属性名：自定义项100 创建日期:2020-4-5
	 * 
	 * @param newDef100
	 *            java.lang.String
	 */
	public void setDef100(java.lang.String def100) {
		this.def100 = def100;
	}

	/**
	 * 属性 生成时间戳的Getter方法.属性名：时间戳 创建日期:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * 属性生成时间戳的Setter方法.属性名：时间戳 创建日期:2020-4-5
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.FinancexpenseVO");
	}
}
