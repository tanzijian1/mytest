package nc.vo.tg.paymentrequest;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 * 此处添加累的描述信息
 * </p>
 * 创建日期:2019-12-9
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class Payablepage extends SuperVO {

	/**
	 * 应付财务组织
	 */
	public java.lang.String pk_org;
	/**
	 * 废弃财务组织
	 */
	public java.lang.String pk_fiorg;
	/**
	 * 利润中心
	 */
	public java.lang.String pk_pcorg;
	/**
	 * 利润中心版本
	 */
	public java.lang.String pk_pcorg_v;
	/**
	 * 应付财务组织版本
	 */
	public java.lang.String pk_org_v;
	/**
	 * 废弃财务组织版本
	 */
	public java.lang.String pk_fiorg_v;
	/**
	 * 结算财务组织版本
	 */
	public java.lang.String sett_org_v;
	/**
	 * 业务部门
	 */
	public java.lang.String pu_deptid;
	/**
	 * 业务部门版本
	 */
	public java.lang.String pu_deptid_v;
	/**
	 * 业务人员
	 */
	public java.lang.String pu_psndoc;
	/**
	 * 业务组织
	 */
	public java.lang.String pu_org;
	/**
	 * 业务组织版本
	 */
	public java.lang.String pu_org_v;
	/**
	 * 结算财务组织
	 */
	public java.lang.String sett_org;
	/**
	 * 物料
	 */
	public java.lang.String material;
	/**
	 * 供应商
	 */
	public java.lang.String supplier;
	/**
	 * 报价计量单位
	 */
	public java.lang.String postunit;
	/**
	 * 报价单位无税单价
	 */
	public nc.vo.pub.lang.UFDouble postpricenotax;
	/**
	 * 报价单位数量
	 */
	public nc.vo.pub.lang.UFDouble postquantity;
	/**
	 * 报价单位含税单价
	 */
	public nc.vo.pub.lang.UFDouble postprice;
	/**
	 * 单据协同状态
	 */
	public java.lang.Integer coordflag;
	/**
	 * 设备编码
	 */
	public java.lang.String equipmentcode;
	/**
	 * 产品线
	 */
	public java.lang.String productline;
	/**
	 * 现金流量项目
	 */
	public java.lang.String cashitem;
	/**
	 * 资金计划项目
	 */
	public java.lang.String bankrollprojet;
	/**
	 * 挂起标志
	 */
	public UFBoolean pausetransact;
	/**
	 * 单据日期
	 */
	public UFDate billdate;
	/**
	 * 所属集团
	 */
	public java.lang.String pk_group;
	/**
	 * 单据类型编码
	 */
	public java.lang.String pk_billtype;
	/**
	 * 单据大类
	 */
	public java.lang.String billclass;
	/**
	 * 应付类型code
	 */
	public java.lang.String pk_tradetype;
	/**
	 * 应付类型
	 */
	public java.lang.String pk_tradetypeid;
	/**
	 * 应付单行标识
	 */
	public java.lang.String pk_payableitem;
	/**
	 * 起算日期
	 */
	public UFDate busidate;
	/**
	 * 收支项目
	 */
	public java.lang.String pk_subjcode;
	/**
	 * 单据号
	 */
	public java.lang.String billno;
	/**
	 * 往来对象
	 */
	public java.lang.Integer objtype;
	/**
	 * 单据分录号
	 */
	public java.lang.Integer rowno;
	/**
	 * 行类型
	 */
	public java.lang.Integer rowtype;
	/**
	 * 方向
	 */
	public java.lang.Integer direction;
	/**
	 * 票据类型
	 */
	public java.lang.String checktype;
	/**
	 * 事项审批单
	 */
	public java.lang.String pk_ssitem;
	/**
	 * 摘要
	 */
	public java.lang.String scomment;
	/**
	 * 科目
	 */
	public java.lang.String subjcode;
	/**
	 * 币种
	 */
	public java.lang.String pk_currtype;
	/**
	 * 组织本币汇率
	 */
	public nc.vo.pub.lang.UFDouble rate;
	/**
	 * 部门
	 */
	public java.lang.String pk_deptid;
	/**
	 * 部 门
	 */
	public java.lang.String pk_deptid_v;
	/**
	 * 业务员
	 */
	public java.lang.String pk_psndoc;
	/**
	 * 贷方数量
	 */
	public nc.vo.pub.lang.UFDouble quantity_cr;
	/**
	 * 组织本币金额
	 */
	public nc.vo.pub.lang.UFDouble local_money_cr;
	/**
	 * 贷方原币金额
	 */
	public nc.vo.pub.lang.UFDouble money_cr;
	/**
	 * 原币余额
	 */
	public nc.vo.pub.lang.UFDouble money_bal;
	/**
	 * 组织本币余额
	 */
	public nc.vo.pub.lang.UFDouble local_money_bal;
	/**
	 * 数量余额
	 */
	public nc.vo.pub.lang.UFDouble quantity_bal;
	/**
	 * 税额
	 */
	public nc.vo.pub.lang.UFDouble local_tax_cr;
	/**
	 * 贷方原币无税金额
	 */
	public nc.vo.pub.lang.UFDouble notax_cr;
	/**
	 * 组织本币无税金额
	 */
	public nc.vo.pub.lang.UFDouble local_notax_cr;
	/**
	 * 单价
	 */
	public nc.vo.pub.lang.UFDouble price;
	/**
	 * 含税单价
	 */
	public nc.vo.pub.lang.UFDouble taxprice;
	/**
	 * 税率
	 */
	public nc.vo.pub.lang.UFDouble taxrate;
	/**
	 * 税号
	 */
	public java.lang.String taxnum;
	/**
	 * 上层单据主键
	 */
	public java.lang.String top_billid;
	/**
	 * 上层单据行主键
	 */
	public java.lang.String top_itemid;
	/**
	 * 上层单据类型
	 */
	public java.lang.String top_billtype;
	/**
	 * 上层交易类型
	 */
	public java.lang.String top_tradetype;
	/**
	 * 源头交易类型
	 */
	public java.lang.String src_tradetype;
	/**
	 * 源头单据类型
	 */
	public java.lang.String src_billtype;
	/**
	 * 源头单据主键
	 */
	public java.lang.String src_billid;
	/**
	 * 源头单据行主键
	 */
	public java.lang.String src_itemid;
	/**
	 * 扣税类别
	 */
	public java.lang.Integer taxtype;
	/**
	 * 付款协议
	 */
	public java.lang.String pk_payterm;
	/**
	 * 付款银行账户
	 */
	public java.lang.String payaccount;
	/**
	 * 收款银行账户
	 */
	public java.lang.String recaccount;
	/**
	 * 订单供应商
	 */
	public java.lang.String ordercubasdoc;
	/**
	 * 调拨订单号
	 */
	public java.lang.String innerorderno;
	/**
	 * 资产合同号
	 */
	public java.lang.String assetpactno;
	/**
	 * 合同号
	 */
	public java.lang.String contractno;
	/**
	 * 散户
	 */
	public java.lang.String freecust;
	/**
	 * 固定资产卡片号
	 */
	public java.lang.String facard;
	/**
	 * 订单号
	 */
	public java.lang.String purchaseorder;
	/**
	 * 发票号
	 */
	public java.lang.String invoiceno;
	/**
	 * 出库单号
	 */
	public java.lang.String outstoreno;
	/**
	 * 责任核算要素
	 */
	public java.lang.String checkelement;
	/**
	 * 集团本币汇率
	 */
	public nc.vo.pub.lang.UFDouble grouprate;
	/**
	 * 全局本币汇率
	 */
	public nc.vo.pub.lang.UFDouble globalrate;
	/**
	 * 集团本币金额(贷方)
	 */
	public nc.vo.pub.lang.UFDouble groupcrebit;
	/**
	 * 全局本币金额(贷方)
	 */
	public nc.vo.pub.lang.UFDouble globalcrebit;
	/**
	 * 集团本币余额
	 */
	public nc.vo.pub.lang.UFDouble groupbalance;
	/**
	 * 全局本币余额
	 */
	public nc.vo.pub.lang.UFDouble globalbalance;
	/**
	 * 集团本币无税金额(贷方)
	 */
	public nc.vo.pub.lang.UFDouble groupnotax_cre;
	/**
	 * 全局本币无税金额(贷方)
	 */
	public nc.vo.pub.lang.UFDouble globalnotax_cre;
	/**
	 * 预占用原币余额
	 */
	public nc.vo.pub.lang.UFDouble occupationmny;
	/**
	 * 项目
	 */
	public java.lang.String project;
	/**
	 * 项目任务
	 */
	public java.lang.String project_task;
	/**
	 * 结算清单号
	 */
	public java.lang.String settleno;
	/**
	 * 本币单价
	 */
	public nc.vo.pub.lang.UFDouble local_price;
	/**
	 * 本币含税单价
	 */
	public nc.vo.pub.lang.UFDouble local_taxprice;
	/**
	 * 成本中心
	 */
	public java.lang.String costcenter;
	/**
	 * 内部交易结算号
	 */
	public java.lang.String confernum;
	/**
	 * 发货国
	 */
	public java.lang.String sendcountryid;
	/**
	 * 购销类型
	 */
	public java.lang.Integer buysellflag;
	/**
	 * 税码
	 */
	public java.lang.String taxcodeid;
	/**
	 * 不可抵扣税率
	 */
	public nc.vo.pub.lang.UFDouble nosubtaxrate;
	/**
	 * 不可抵扣税额
	 */
	public nc.vo.pub.lang.UFDouble nosubtax;
	/**
	 * 计税金额
	 */
	public nc.vo.pub.lang.UFDouble caltaxmny;
	/**
	 * 是否逆向征税
	 */
	public UFBoolean opptaxflag;
	/**
	 * 供应商VAT注册码
	 */
	public java.lang.String vendorvatcode;
	/**
	 * VAT注册码
	 */
	public java.lang.String vatcode;
	/**
	 * 供应商应付单标识
	 */
	public java.lang.String pk_payablebill;
	/**
	 * 原始物料
	 */
	public java.lang.String material_src;
	/**
	 * 付款金额
	 */
	public nc.vo.pub.lang.UFDouble settlemoney;
	/**
	 * 付款币种
	 */
	public java.lang.String settlecurr;
	/**
	 * 结算方式
	 */
	public java.lang.String pk_balatype;
	/**
	 * CBS
	 */
	public java.lang.String cbs;
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
	 * 上层单据主键
	 */
	public String pk_payreq;
	/**
	 * 时间戳
	 */
	public UFDateTime ts;

	/**
	 * 属性 pk_org的Getter方法.属性名：应付财务组织 创建日期:2019-12-9
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getPk_org() {
		return this.pk_org;
	}

	/**
	 * 属性pk_org的Setter方法.属性名：应付财务组织 创建日期:2019-12-9
	 * 
	 * @param newPk_org
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setPk_org(java.lang.String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * 属性 pk_fiorg的Getter方法.属性名：废弃财务组织 创建日期:2019-12-9
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getPk_fiorg() {
		return this.pk_fiorg;
	}

	/**
	 * 属性pk_fiorg的Setter方法.属性名：废弃财务组织 创建日期:2019-12-9
	 * 
	 * @param newPk_fiorg
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setPk_fiorg(java.lang.String pk_fiorg) {
		this.pk_fiorg = pk_fiorg;
	}

	/**
	 * 属性 pk_pcorg的Getter方法.属性名：利润中心 创建日期:2019-12-9
	 * 
	 * @return nc.vo.org.LiabilityCenterVO
	 */
	public java.lang.String getPk_pcorg() {
		return this.pk_pcorg;
	}

	/**
	 * 属性pk_pcorg的Setter方法.属性名：利润中心 创建日期:2019-12-9
	 * 
	 * @param newPk_pcorg
	 *            nc.vo.org.LiabilityCenterVO
	 */
	public void setPk_pcorg(java.lang.String pk_pcorg) {
		this.pk_pcorg = pk_pcorg;
	}

	/**
	 * 属性 pk_pcorg_v的Getter方法.属性名：利润中心版本 创建日期:2019-12-9
	 * 
	 * @return nc.vo.vorg.LiabilityCenterVersionVO
	 */
	public java.lang.String getPk_pcorg_v() {
		return this.pk_pcorg_v;
	}

	/**
	 * 属性pk_pcorg_v的Setter方法.属性名：利润中心版本 创建日期:2019-12-9
	 * 
	 * @param newPk_pcorg_v
	 *            nc.vo.vorg.LiabilityCenterVersionVO
	 */
	public void setPk_pcorg_v(java.lang.String pk_pcorg_v) {
		this.pk_pcorg_v = pk_pcorg_v;
	}

	/**
	 * 属性 pk_org_v的Getter方法.属性名：应付财务组织版本 创建日期:2019-12-9
	 * 
	 * @return nc.vo.vorg.FinanceOrgVersionVO
	 */
	public java.lang.String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * 属性pk_org_v的Setter方法.属性名：应付财务组织版本 创建日期:2019-12-9
	 * 
	 * @param newPk_org_v
	 *            nc.vo.vorg.FinanceOrgVersionVO
	 */
	public void setPk_org_v(java.lang.String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * 属性 pk_fiorg_v的Getter方法.属性名：废弃财务组织版本 创建日期:2019-12-9
	 * 
	 * @return nc.vo.vorg.FinanceOrgVersionVO
	 */
	public java.lang.String getPk_fiorg_v() {
		return this.pk_fiorg_v;
	}

	/**
	 * 属性pk_fiorg_v的Setter方法.属性名：废弃财务组织版本 创建日期:2019-12-9
	 * 
	 * @param newPk_fiorg_v
	 *            nc.vo.vorg.FinanceOrgVersionVO
	 */
	public void setPk_fiorg_v(java.lang.String pk_fiorg_v) {
		this.pk_fiorg_v = pk_fiorg_v;
	}

	/**
	 * 属性 sett_org_v的Getter方法.属性名：结算财务组织版本 创建日期:2019-12-9
	 * 
	 * @return nc.vo.vorg.FinanceOrgVersionVO
	 */
	public java.lang.String getSett_org_v() {
		return this.sett_org_v;
	}

	/**
	 * 属性sett_org_v的Setter方法.属性名：结算财务组织版本 创建日期:2019-12-9
	 * 
	 * @param newSett_org_v
	 *            nc.vo.vorg.FinanceOrgVersionVO
	 */
	public void setSett_org_v(java.lang.String sett_org_v) {
		this.sett_org_v = sett_org_v;
	}

	/**
	 * 属性 pu_deptid的Getter方法.属性名：业务部门 创建日期:2019-12-9
	 * 
	 * @return nc.vo.org.DeptVO
	 */
	public java.lang.String getPu_deptid() {
		return this.pu_deptid;
	}

	/**
	 * 属性pu_deptid的Setter方法.属性名：业务部门 创建日期:2019-12-9
	 * 
	 * @param newPu_deptid
	 *            nc.vo.org.DeptVO
	 */
	public void setPu_deptid(java.lang.String pu_deptid) {
		this.pu_deptid = pu_deptid;
	}

	/**
	 * 属性 pu_deptid_v的Getter方法.属性名：业务部门版本 创建日期:2019-12-9
	 * 
	 * @return nc.vo.vorg.DeptVersionVO
	 */
	public java.lang.String getPu_deptid_v() {
		return this.pu_deptid_v;
	}

	/**
	 * 属性pu_deptid_v的Setter方法.属性名：业务部门版本 创建日期:2019-12-9
	 * 
	 * @param newPu_deptid_v
	 *            nc.vo.vorg.DeptVersionVO
	 */
	public void setPu_deptid_v(java.lang.String pu_deptid_v) {
		this.pu_deptid_v = pu_deptid_v;
	}

	/**
	 * 属性 pu_psndoc的Getter方法.属性名：业务人员 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPu_psndoc() {
		return this.pu_psndoc;
	}

	/**
	 * 属性pu_psndoc的Setter方法.属性名：业务人员 创建日期:2019-12-9
	 * 
	 * @param newPu_psndoc
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPu_psndoc(java.lang.String pu_psndoc) {
		this.pu_psndoc = pu_psndoc;
	}

	/**
	 * 属性 pu_org的Getter方法.属性名：业务组织 创建日期:2019-12-9
	 * 
	 * @return nc.vo.org.OrgVO
	 */
	public java.lang.String getPu_org() {
		return this.pu_org;
	}

	/**
	 * 属性pu_org的Setter方法.属性名：业务组织 创建日期:2019-12-9
	 * 
	 * @param newPu_org
	 *            nc.vo.org.OrgVO
	 */
	public void setPu_org(java.lang.String pu_org) {
		this.pu_org = pu_org;
	}

	/**
	 * 属性 pu_org_v的Getter方法.属性名：业务组织版本 创建日期:2019-12-9
	 * 
	 * @return nc.vo.vorg.OrgVersionVO
	 */
	public java.lang.String getPu_org_v() {
		return this.pu_org_v;
	}

	/**
	 * 属性pu_org_v的Setter方法.属性名：业务组织版本 创建日期:2019-12-9
	 * 
	 * @param newPu_org_v
	 *            nc.vo.vorg.OrgVersionVO
	 */
	public void setPu_org_v(java.lang.String pu_org_v) {
		this.pu_org_v = pu_org_v;
	}

	/**
	 * 属性 sett_org的Getter方法.属性名：结算财务组织 创建日期:2019-12-9
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getSett_org() {
		return this.sett_org;
	}

	/**
	 * 属性sett_org的Setter方法.属性名：结算财务组织 创建日期:2019-12-9
	 * 
	 * @param newSett_org
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setSett_org(java.lang.String sett_org) {
		this.sett_org = sett_org;
	}

	/**
	 * 属性 material的Getter方法.属性名：物料 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.material.MaterialVO
	 */
	public java.lang.String getMaterial() {
		return this.material;
	}

	/**
	 * 属性material的Setter方法.属性名：物料 创建日期:2019-12-9
	 * 
	 * @param newMaterial
	 *            nc.vo.bd.material.MaterialVO
	 */
	public void setMaterial(java.lang.String material) {
		this.material = material;
	}

	/**
	 * 属性 supplier的Getter方法.属性名：供应商 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.supplier.SupplierVO
	 */
	public java.lang.String getSupplier() {
		return this.supplier;
	}

	/**
	 * 属性supplier的Setter方法.属性名：供应商 创建日期:2019-12-9
	 * 
	 * @param newSupplier
	 *            nc.vo.bd.supplier.SupplierVO
	 */
	public void setSupplier(java.lang.String supplier) {
		this.supplier = supplier;
	}

	/**
	 * 属性 postunit的Getter方法.属性名：报价计量单位 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.material.measdoc.MeasdocVO
	 */
	public java.lang.String getPostunit() {
		return this.postunit;
	}

	/**
	 * 属性postunit的Setter方法.属性名：报价计量单位 创建日期:2019-12-9
	 * 
	 * @param newPostunit
	 *            nc.vo.bd.material.measdoc.MeasdocVO
	 */
	public void setPostunit(java.lang.String postunit) {
		this.postunit = postunit;
	}

	/**
	 * 属性 postpricenotax的Getter方法.属性名：报价单位无税单价 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPostpricenotax() {
		return this.postpricenotax;
	}

	/**
	 * 属性postpricenotax的Setter方法.属性名：报价单位无税单价 创建日期:2019-12-9
	 * 
	 * @param newPostpricenotax
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPostpricenotax(nc.vo.pub.lang.UFDouble postpricenotax) {
		this.postpricenotax = postpricenotax;
	}

	/**
	 * 属性 postquantity的Getter方法.属性名：报价单位数量 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPostquantity() {
		return this.postquantity;
	}

	/**
	 * 属性postquantity的Setter方法.属性名：报价单位数量 创建日期:2019-12-9
	 * 
	 * @param newPostquantity
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPostquantity(nc.vo.pub.lang.UFDouble postquantity) {
		this.postquantity = postquantity;
	}

	/**
	 * 属性 postprice的Getter方法.属性名：报价单位含税单价 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPostprice() {
		return this.postprice;
	}

	/**
	 * 属性postprice的Setter方法.属性名：报价单位含税单价 创建日期:2019-12-9
	 * 
	 * @param newPostprice
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPostprice(nc.vo.pub.lang.UFDouble postprice) {
		this.postprice = postprice;
	}

	/**
	 * 属性 coordflag的Getter方法.属性名：单据协同状态 创建日期:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getCoordflag() {
		return this.coordflag;
	}

	/**
	 * 属性coordflag的Setter方法.属性名：单据协同状态 创建日期:2019-12-9
	 * 
	 * @param newCoordflag
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setCoordflag(java.lang.Integer coordflag) {
		this.coordflag = coordflag;
	}

	/**
	 * 属性 equipmentcode的Getter方法.属性名：设备编码 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getEquipmentcode() {
		return this.equipmentcode;
	}

	/**
	 * 属性equipmentcode的Setter方法.属性名：设备编码 创建日期:2019-12-9
	 * 
	 * @param newEquipmentcode
	 *            java.lang.String
	 */
	public void setEquipmentcode(java.lang.String equipmentcode) {
		this.equipmentcode = equipmentcode;
	}

	/**
	 * 属性 productline的Getter方法.属性名：产品线 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.prodline.ProdLineVO
	 */
	public java.lang.String getProductline() {
		return this.productline;
	}

	/**
	 * 属性productline的Setter方法.属性名：产品线 创建日期:2019-12-9
	 * 
	 * @param newProductline
	 *            nc.vo.bd.prodline.ProdLineVO
	 */
	public void setProductline(java.lang.String productline) {
		this.productline = productline;
	}

	/**
	 * 属性 cashitem的Getter方法.属性名：现金流量项目 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.cashflow.CashflowVO
	 */
	public java.lang.String getCashitem() {
		return this.cashitem;
	}

	/**
	 * 属性cashitem的Setter方法.属性名：现金流量项目 创建日期:2019-12-9
	 * 
	 * @param newCashitem
	 *            nc.vo.bd.cashflow.CashflowVO
	 */
	public void setCashitem(java.lang.String cashitem) {
		this.cashitem = cashitem;
	}

	/**
	 * 属性 bankrollprojet的Getter方法.属性名：资金计划项目 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.fundplan.FundPlanVO
	 */
	public java.lang.String getBankrollprojet() {
		return this.bankrollprojet;
	}

	/**
	 * 属性bankrollprojet的Setter方法.属性名：资金计划项目 创建日期:2019-12-9
	 * 
	 * @param newBankrollprojet
	 *            nc.vo.bd.fundplan.FundPlanVO
	 */
	public void setBankrollprojet(java.lang.String bankrollprojet) {
		this.bankrollprojet = bankrollprojet;
	}

	/**
	 * 属性 pausetransact的Getter方法.属性名：挂起标志 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getPausetransact() {
		return this.pausetransact;
	}

	/**
	 * 属性pausetransact的Setter方法.属性名：挂起标志 创建日期:2019-12-9
	 * 
	 * @param newPausetransact
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setPausetransact(UFBoolean pausetransact) {
		this.pausetransact = pausetransact;
	}

	/**
	 * 属性 billdate的Getter方法.属性名：单据日期 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getBilldate() {
		return this.billdate;
	}

	/**
	 * 属性billdate的Setter方法.属性名：单据日期 创建日期:2019-12-9
	 * 
	 * @param newBilldate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setBilldate(UFDate billdate) {
		this.billdate = billdate;
	}

	/**
	 * 属性 pk_group的Getter方法.属性名：所属集团 创建日期:2019-12-9
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public java.lang.String getPk_group() {
		return this.pk_group;
	}

	/**
	 * 属性pk_group的Setter方法.属性名：所属集团 创建日期:2019-12-9
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * 属性 pk_billtype的Getter方法.属性名：单据类型编码 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_billtype() {
		return this.pk_billtype;
	}

	/**
	 * 属性pk_billtype的Setter方法.属性名：单据类型编码 创建日期:2019-12-9
	 * 
	 * @param newPk_billtype
	 *            java.lang.String
	 */
	public void setPk_billtype(java.lang.String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	/**
	 * 属性 billclass的Getter方法.属性名：单据大类 创建日期:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.String getBillclass() {
		return this.billclass;
	}

	/**
	 * 属性billclass的Setter方法.属性名：单据大类 创建日期:2019-12-9
	 * 
	 * @param newBillclass
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setBillclass(java.lang.String billclass) {
		this.billclass = billclass;
	}

	/**
	 * 属性 pk_tradetype的Getter方法.属性名：应付类型code 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_tradetype() {
		return this.pk_tradetype;
	}

	/**
	 * 属性pk_tradetype的Setter方法.属性名：应付类型code 创建日期:2019-12-9
	 * 
	 * @param newPk_tradetype
	 *            java.lang.String
	 */
	public void setPk_tradetype(java.lang.String pk_tradetype) {
		this.pk_tradetype = pk_tradetype;
	}

	/**
	 * 属性 pk_tradetypeid的Getter方法.属性名：应付类型 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.billtype.BilltypeVO
	 */
	public java.lang.String getPk_tradetypeid() {
		return this.pk_tradetypeid;
	}

	/**
	 * 属性pk_tradetypeid的Setter方法.属性名：应付类型 创建日期:2019-12-9
	 * 
	 * @param newPk_tradetypeid
	 *            nc.vo.pub.billtype.BilltypeVO
	 */
	public void setPk_tradetypeid(java.lang.String pk_tradetypeid) {
		this.pk_tradetypeid = pk_tradetypeid;
	}

	/**
	 * 属性 pk_payableitem的Getter方法.属性名：应付单行标识 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_payableitem() {
		return this.pk_payableitem;
	}

	/**
	 * 属性pk_payableitem的Setter方法.属性名：应付单行标识 创建日期:2019-12-9
	 * 
	 * @param newPk_payableitem
	 *            java.lang.String
	 */
	public void setPk_payableitem(java.lang.String pk_payableitem) {
		this.pk_payableitem = pk_payableitem;
	}

	/**
	 * 属性 busidate的Getter方法.属性名：起算日期 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getBusidate() {
		return this.busidate;
	}

	/**
	 * 属性busidate的Setter方法.属性名：起算日期 创建日期:2019-12-9
	 * 
	 * @param newBusidate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setBusidate(UFDate busidate) {
		this.busidate = busidate;
	}

	/**
	 * 属性 pk_subjcode的Getter方法.属性名：收支项目 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.inoutbusiclass.InoutBusiClassVO
	 */
	public java.lang.String getPk_subjcode() {
		return this.pk_subjcode;
	}

	/**
	 * 属性pk_subjcode的Setter方法.属性名：收支项目 创建日期:2019-12-9
	 * 
	 * @param newPk_subjcode
	 *            nc.vo.bd.inoutbusiclass.InoutBusiClassVO
	 */
	public void setPk_subjcode(java.lang.String pk_subjcode) {
		this.pk_subjcode = pk_subjcode;
	}

	/**
	 * 属性 billno的Getter方法.属性名：单据号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillno() {
		return this.billno;
	}

	/**
	 * 属性billno的Setter方法.属性名：单据号 创建日期:2019-12-9
	 * 
	 * @param newBillno
	 *            java.lang.String
	 */
	public void setBillno(java.lang.String billno) {
		this.billno = billno;
	}

	/**
	 * 属性 objtype的Getter方法.属性名：往来对象 创建日期:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getObjtype() {
		return this.objtype;
	}

	/**
	 * 属性objtype的Setter方法.属性名：往来对象 创建日期:2019-12-9
	 * 
	 * @param newObjtype
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setObjtype(java.lang.Integer objtype) {
		this.objtype = objtype;
	}

	/**
	 * 属性 rowno的Getter方法.属性名：单据分录号 创建日期:2019-12-9
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getRowno() {
		return this.rowno;
	}

	/**
	 * 属性rowno的Setter方法.属性名：单据分录号 创建日期:2019-12-9
	 * 
	 * @param newRowno
	 *            java.lang.Integer
	 */
	public void setRowno(java.lang.Integer rowno) {
		this.rowno = rowno;
	}

	/**
	 * 属性 rowtype的Getter方法.属性名：行类型 创建日期:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getRowtype() {
		return this.rowtype;
	}

	/**
	 * 属性rowtype的Setter方法.属性名：行类型 创建日期:2019-12-9
	 * 
	 * @param newRowtype
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setRowtype(java.lang.Integer rowtype) {
		this.rowtype = rowtype;
	}

	/**
	 * 属性 direction的Getter方法.属性名：方向 创建日期:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getDirection() {
		return this.direction;
	}

	/**
	 * 属性direction的Setter方法.属性名：方向 创建日期:2019-12-9
	 * 
	 * @param newDirection
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setDirection(java.lang.Integer direction) {
		this.direction = direction;
	}

	/**
	 * 属性 checktype的Getter方法.属性名：票据类型 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.notetype.NotetypeVO
	 */
	public java.lang.String getChecktype() {
		return this.checktype;
	}

	/**
	 * 属性checktype的Setter方法.属性名：票据类型 创建日期:2019-12-9
	 * 
	 * @param newChecktype
	 *            nc.vo.bd.notetype.NotetypeVO
	 */
	public void setChecktype(java.lang.String checktype) {
		this.checktype = checktype;
	}

	/**
	 * 属性 pk_ssitem的Getter方法.属性名：事项审批单 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_ssitem() {
		return this.pk_ssitem;
	}

	/**
	 * 属性pk_ssitem的Setter方法.属性名：事项审批单 创建日期:2019-12-9
	 * 
	 * @param newPk_ssitem
	 *            java.lang.String
	 */
	public void setPk_ssitem(java.lang.String pk_ssitem) {
		this.pk_ssitem = pk_ssitem;
	}

	/**
	 * 属性 scomment的Getter方法.属性名：摘要 创建日期:2019-12-9
	 * 
	 * @return nc.vo.fipub.summary.SummaryVO
	 */
	public java.lang.String getScomment() {
		return this.scomment;
	}

	/**
	 * 属性scomment的Setter方法.属性名：摘要 创建日期:2019-12-9
	 * 
	 * @param newScomment
	 *            nc.vo.fipub.summary.SummaryVO
	 */
	public void setScomment(java.lang.String scomment) {
		this.scomment = scomment;
	}

	/**
	 * 属性 subjcode的Getter方法.属性名：科目 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.account.AccAsoaVO
	 */
	public java.lang.String getSubjcode() {
		return this.subjcode;
	}

	/**
	 * 属性subjcode的Setter方法.属性名：科目 创建日期:2019-12-9
	 * 
	 * @param newSubjcode
	 *            nc.vo.bd.account.AccAsoaVO
	 */
	public void setSubjcode(java.lang.String subjcode) {
		this.subjcode = subjcode;
	}

	/**
	 * 属性 pk_currtype的Getter方法.属性名：币种 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.currtype.CurrtypeVO
	 */
	public java.lang.String getPk_currtype() {
		return this.pk_currtype;
	}

	/**
	 * 属性pk_currtype的Setter方法.属性名：币种 创建日期:2019-12-9
	 * 
	 * @param newPk_currtype
	 *            nc.vo.bd.currtype.CurrtypeVO
	 */
	public void setPk_currtype(java.lang.String pk_currtype) {
		this.pk_currtype = pk_currtype;
	}

	/**
	 * 属性 rate的Getter方法.属性名：组织本币汇率 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getRate() {
		return this.rate;
	}

	/**
	 * 属性rate的Setter方法.属性名：组织本币汇率 创建日期:2019-12-9
	 * 
	 * @param newRate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setRate(nc.vo.pub.lang.UFDouble rate) {
		this.rate = rate;
	}

	/**
	 * 属性 pk_deptid的Getter方法.属性名：部门 创建日期:2019-12-9
	 * 
	 * @return nc.vo.org.DeptVO
	 */
	public java.lang.String getPk_deptid() {
		return this.pk_deptid;
	}

	/**
	 * 属性pk_deptid的Setter方法.属性名：部门 创建日期:2019-12-9
	 * 
	 * @param newPk_deptid
	 *            nc.vo.org.DeptVO
	 */
	public void setPk_deptid(java.lang.String pk_deptid) {
		this.pk_deptid = pk_deptid;
	}

	/**
	 * 属性 pk_deptid_v的Getter方法.属性名：部 门 创建日期:2019-12-9
	 * 
	 * @return nc.vo.vorg.DeptVersionVO
	 */
	public java.lang.String getPk_deptid_v() {
		return this.pk_deptid_v;
	}

	/**
	 * 属性pk_deptid_v的Setter方法.属性名：部 门 创建日期:2019-12-9
	 * 
	 * @param newPk_deptid_v
	 *            nc.vo.vorg.DeptVersionVO
	 */
	public void setPk_deptid_v(java.lang.String pk_deptid_v) {
		this.pk_deptid_v = pk_deptid_v;
	}

	/**
	 * 属性 pk_psndoc的Getter方法.属性名：业务员 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPk_psndoc() {
		return this.pk_psndoc;
	}

	/**
	 * 属性pk_psndoc的Setter方法.属性名：业务员 创建日期:2019-12-9
	 * 
	 * @param newPk_psndoc
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPk_psndoc(java.lang.String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	/**
	 * 属性 quantity_cr的Getter方法.属性名：贷方数量 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getQuantity_cr() {
		return this.quantity_cr;
	}

	/**
	 * 属性quantity_cr的Setter方法.属性名：贷方数量 创建日期:2019-12-9
	 * 
	 * @param newQuantity_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setQuantity_cr(nc.vo.pub.lang.UFDouble quantity_cr) {
		this.quantity_cr = quantity_cr;
	}

	/**
	 * 属性 local_money_cr的Getter方法.属性名：组织本币金额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_money_cr() {
		return this.local_money_cr;
	}

	/**
	 * 属性local_money_cr的Setter方法.属性名：组织本币金额 创建日期:2019-12-9
	 * 
	 * @param newLocal_money_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_money_cr(nc.vo.pub.lang.UFDouble local_money_cr) {
		this.local_money_cr = local_money_cr;
	}

	/**
	 * 属性 money_cr的Getter方法.属性名：贷方原币金额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getMoney_cr() {
		return this.money_cr;
	}

	/**
	 * 属性money_cr的Setter方法.属性名：贷方原币金额 创建日期:2019-12-9
	 * 
	 * @param newMoney_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setMoney_cr(nc.vo.pub.lang.UFDouble money_cr) {
		this.money_cr = money_cr;
	}

	/**
	 * 属性 money_bal的Getter方法.属性名：原币余额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getMoney_bal() {
		return this.money_bal;
	}

	/**
	 * 属性money_bal的Setter方法.属性名：原币余额 创建日期:2019-12-9
	 * 
	 * @param newMoney_bal
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setMoney_bal(nc.vo.pub.lang.UFDouble money_bal) {
		this.money_bal = money_bal;
	}

	/**
	 * 属性 local_money_bal的Getter方法.属性名：组织本币余额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_money_bal() {
		return this.local_money_bal;
	}

	/**
	 * 属性local_money_bal的Setter方法.属性名：组织本币余额 创建日期:2019-12-9
	 * 
	 * @param newLocal_money_bal
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_money_bal(nc.vo.pub.lang.UFDouble local_money_bal) {
		this.local_money_bal = local_money_bal;
	}

	/**
	 * 属性 quantity_bal的Getter方法.属性名：数量余额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getQuantity_bal() {
		return this.quantity_bal;
	}

	/**
	 * 属性quantity_bal的Setter方法.属性名：数量余额 创建日期:2019-12-9
	 * 
	 * @param newQuantity_bal
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setQuantity_bal(nc.vo.pub.lang.UFDouble quantity_bal) {
		this.quantity_bal = quantity_bal;
	}

	/**
	 * 属性 local_tax_cr的Getter方法.属性名：税额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_tax_cr() {
		return this.local_tax_cr;
	}

	/**
	 * 属性local_tax_cr的Setter方法.属性名：税额 创建日期:2019-12-9
	 * 
	 * @param newLocal_tax_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_tax_cr(nc.vo.pub.lang.UFDouble local_tax_cr) {
		this.local_tax_cr = local_tax_cr;
	}

	/**
	 * 属性 notax_cr的Getter方法.属性名：贷方原币无税金额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getNotax_cr() {
		return this.notax_cr;
	}

	/**
	 * 属性notax_cr的Setter方法.属性名：贷方原币无税金额 创建日期:2019-12-9
	 * 
	 * @param newNotax_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setNotax_cr(nc.vo.pub.lang.UFDouble notax_cr) {
		this.notax_cr = notax_cr;
	}

	/**
	 * 属性 local_notax_cr的Getter方法.属性名：组织本币无税金额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_notax_cr() {
		return this.local_notax_cr;
	}

	/**
	 * 属性local_notax_cr的Setter方法.属性名：组织本币无税金额 创建日期:2019-12-9
	 * 
	 * @param newLocal_notax_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_notax_cr(nc.vo.pub.lang.UFDouble local_notax_cr) {
		this.local_notax_cr = local_notax_cr;
	}

	/**
	 * 属性 price的Getter方法.属性名：单价 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPrice() {
		return this.price;
	}

	/**
	 * 属性price的Setter方法.属性名：单价 创建日期:2019-12-9
	 * 
	 * @param newPrice
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPrice(nc.vo.pub.lang.UFDouble price) {
		this.price = price;
	}

	/**
	 * 属性 taxprice的Getter方法.属性名：含税单价 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getTaxprice() {
		return this.taxprice;
	}

	/**
	 * 属性taxprice的Setter方法.属性名：含税单价 创建日期:2019-12-9
	 * 
	 * @param newTaxprice
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setTaxprice(nc.vo.pub.lang.UFDouble taxprice) {
		this.taxprice = taxprice;
	}

	/**
	 * 属性 taxrate的Getter方法.属性名：税率 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getTaxrate() {
		return this.taxrate;
	}

	/**
	 * 属性taxrate的Setter方法.属性名：税率 创建日期:2019-12-9
	 * 
	 * @param newTaxrate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setTaxrate(nc.vo.pub.lang.UFDouble taxrate) {
		this.taxrate = taxrate;
	}

	/**
	 * 属性 taxnum的Getter方法.属性名：税号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTaxnum() {
		return this.taxnum;
	}

	/**
	 * 属性taxnum的Setter方法.属性名：税号 创建日期:2019-12-9
	 * 
	 * @param newTaxnum
	 *            java.lang.String
	 */
	public void setTaxnum(java.lang.String taxnum) {
		this.taxnum = taxnum;
	}

	/**
	 * 属性 top_billid的Getter方法.属性名：上层单据主键 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTop_billid() {
		return this.top_billid;
	}

	/**
	 * 属性top_billid的Setter方法.属性名：上层单据主键 创建日期:2019-12-9
	 * 
	 * @param newTop_billid
	 *            java.lang.String
	 */
	public void setTop_billid(java.lang.String top_billid) {
		this.top_billid = top_billid;
	}

	/**
	 * 属性 top_itemid的Getter方法.属性名：上层单据行主键 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTop_itemid() {
		return this.top_itemid;
	}

	/**
	 * 属性top_itemid的Setter方法.属性名：上层单据行主键 创建日期:2019-12-9
	 * 
	 * @param newTop_itemid
	 *            java.lang.String
	 */
	public void setTop_itemid(java.lang.String top_itemid) {
		this.top_itemid = top_itemid;
	}

	/**
	 * 属性 top_billtype的Getter方法.属性名：上层单据类型 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTop_billtype() {
		return this.top_billtype;
	}

	/**
	 * 属性top_billtype的Setter方法.属性名：上层单据类型 创建日期:2019-12-9
	 * 
	 * @param newTop_billtype
	 *            java.lang.String
	 */
	public void setTop_billtype(java.lang.String top_billtype) {
		this.top_billtype = top_billtype;
	}

	/**
	 * 属性 top_tradetype的Getter方法.属性名：上层交易类型 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTop_tradetype() {
		return this.top_tradetype;
	}

	/**
	 * 属性top_tradetype的Setter方法.属性名：上层交易类型 创建日期:2019-12-9
	 * 
	 * @param newTop_tradetype
	 *            java.lang.String
	 */
	public void setTop_tradetype(java.lang.String top_tradetype) {
		this.top_tradetype = top_tradetype;
	}

	/**
	 * 属性 src_tradetype的Getter方法.属性名：源头交易类型 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrc_tradetype() {
		return this.src_tradetype;
	}

	/**
	 * 属性src_tradetype的Setter方法.属性名：源头交易类型 创建日期:2019-12-9
	 * 
	 * @param newSrc_tradetype
	 *            java.lang.String
	 */
	public void setSrc_tradetype(java.lang.String src_tradetype) {
		this.src_tradetype = src_tradetype;
	}

	/**
	 * 属性 src_billtype的Getter方法.属性名：源头单据类型 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrc_billtype() {
		return this.src_billtype;
	}

	/**
	 * 属性src_billtype的Setter方法.属性名：源头单据类型 创建日期:2019-12-9
	 * 
	 * @param newSrc_billtype
	 *            java.lang.String
	 */
	public void setSrc_billtype(java.lang.String src_billtype) {
		this.src_billtype = src_billtype;
	}

	/**
	 * 属性 src_billid的Getter方法.属性名：源头单据主键 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrc_billid() {
		return this.src_billid;
	}

	/**
	 * 属性src_billid的Setter方法.属性名：源头单据主键 创建日期:2019-12-9
	 * 
	 * @param newSrc_billid
	 *            java.lang.String
	 */
	public void setSrc_billid(java.lang.String src_billid) {
		this.src_billid = src_billid;
	}

	/**
	 * 属性 src_itemid的Getter方法.属性名：源头单据行主键 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrc_itemid() {
		return this.src_itemid;
	}

	/**
	 * 属性src_itemid的Setter方法.属性名：源头单据行主键 创建日期:2019-12-9
	 * 
	 * @param newSrc_itemid
	 *            java.lang.String
	 */
	public void setSrc_itemid(java.lang.String src_itemid) {
		this.src_itemid = src_itemid;
	}

	/**
	 * 属性 taxtype的Getter方法.属性名：扣税类别 创建日期:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getTaxtype() {
		return this.taxtype;
	}

	/**
	 * 属性taxtype的Setter方法.属性名：扣税类别 创建日期:2019-12-9
	 * 
	 * @param newTaxtype
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setTaxtype(java.lang.Integer taxtype) {
		this.taxtype = taxtype;
	}

	/**
	 * 属性 pk_payterm的Getter方法.属性名：付款协议 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.payment.PaymentVO
	 */
	public java.lang.String getPk_payterm() {
		return this.pk_payterm;
	}

	/**
	 * 属性pk_payterm的Setter方法.属性名：付款协议 创建日期:2019-12-9
	 * 
	 * @param newPk_payterm
	 *            nc.vo.bd.payment.PaymentVO
	 */
	public void setPk_payterm(java.lang.String pk_payterm) {
		this.pk_payterm = pk_payterm;
	}

	/**
	 * 属性 payaccount的Getter方法.属性名：付款银行账户 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.bankaccount.BankAccSubVO
	 */
	public java.lang.String getPayaccount() {
		return this.payaccount;
	}

	/**
	 * 属性payaccount的Setter方法.属性名：付款银行账户 创建日期:2019-12-9
	 * 
	 * @param newPayaccount
	 *            nc.vo.bd.bankaccount.BankAccSubVO
	 */
	public void setPayaccount(java.lang.String payaccount) {
		this.payaccount = payaccount;
	}

	/**
	 * 属性 recaccount的Getter方法.属性名：收款银行账户 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.bankaccount.BankAccSubVO
	 */
	public java.lang.String getRecaccount() {
		return this.recaccount;
	}

	/**
	 * 属性recaccount的Setter方法.属性名：收款银行账户 创建日期:2019-12-9
	 * 
	 * @param newRecaccount
	 *            nc.vo.bd.bankaccount.BankAccSubVO
	 */
	public void setRecaccount(java.lang.String recaccount) {
		this.recaccount = recaccount;
	}

	/**
	 * 属性 ordercubasdoc的Getter方法.属性名：订单供应商 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.supplier.SupplierVO
	 */
	public java.lang.String getOrdercubasdoc() {
		return this.ordercubasdoc;
	}

	/**
	 * 属性ordercubasdoc的Setter方法.属性名：订单供应商 创建日期:2019-12-9
	 * 
	 * @param newOrdercubasdoc
	 *            nc.vo.bd.supplier.SupplierVO
	 */
	public void setOrdercubasdoc(java.lang.String ordercubasdoc) {
		this.ordercubasdoc = ordercubasdoc;
	}

	/**
	 * 属性 innerorderno的Getter方法.属性名：调拨订单号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getInnerorderno() {
		return this.innerorderno;
	}

	/**
	 * 属性innerorderno的Setter方法.属性名：调拨订单号 创建日期:2019-12-9
	 * 
	 * @param newInnerorderno
	 *            java.lang.String
	 */
	public void setInnerorderno(java.lang.String innerorderno) {
		this.innerorderno = innerorderno;
	}

	/**
	 * 属性 assetpactno的Getter方法.属性名：资产合同号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAssetpactno() {
		return this.assetpactno;
	}

	/**
	 * 属性assetpactno的Setter方法.属性名：资产合同号 创建日期:2019-12-9
	 * 
	 * @param newAssetpactno
	 *            java.lang.String
	 */
	public void setAssetpactno(java.lang.String assetpactno) {
		this.assetpactno = assetpactno;
	}

	/**
	 * 属性 contractno的Getter方法.属性名：合同号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getContractno() {
		return this.contractno;
	}

	/**
	 * 属性contractno的Setter方法.属性名：合同号 创建日期:2019-12-9
	 * 
	 * @param newContractno
	 *            java.lang.String
	 */
	public void setContractno(java.lang.String contractno) {
		this.contractno = contractno;
	}

	/**
	 * 属性 freecust的Getter方法.属性名：散户 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.freecustom.FreeCustomVO
	 */
	public java.lang.String getFreecust() {
		return this.freecust;
	}

	/**
	 * 属性freecust的Setter方法.属性名：散户 创建日期:2019-12-9
	 * 
	 * @param newFreecust
	 *            nc.vo.bd.freecustom.FreeCustomVO
	 */
	public void setFreecust(java.lang.String freecust) {
		this.freecust = freecust;
	}

	/**
	 * 属性 facard的Getter方法.属性名：固定资产卡片号 创建日期:2019-12-9
	 * 
	 * @return nc.vo.fa.asset.AssetVO
	 */
	public java.lang.String getFacard() {
		return this.facard;
	}

	/**
	 * 属性facard的Setter方法.属性名：固定资产卡片号 创建日期:2019-12-9
	 * 
	 * @param newFacard
	 *            nc.vo.fa.asset.AssetVO
	 */
	public void setFacard(java.lang.String facard) {
		this.facard = facard;
	}

	/**
	 * 属性 purchaseorder的Getter方法.属性名：订单号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPurchaseorder() {
		return this.purchaseorder;
	}

	/**
	 * 属性purchaseorder的Setter方法.属性名：订单号 创建日期:2019-12-9
	 * 
	 * @param newPurchaseorder
	 *            java.lang.String
	 */
	public void setPurchaseorder(java.lang.String purchaseorder) {
		this.purchaseorder = purchaseorder;
	}

	/**
	 * 属性 invoiceno的Getter方法.属性名：发票号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getInvoiceno() {
		return this.invoiceno;
	}

	/**
	 * 属性invoiceno的Setter方法.属性名：发票号 创建日期:2019-12-9
	 * 
	 * @param newInvoiceno
	 *            java.lang.String
	 */
	public void setInvoiceno(java.lang.String invoiceno) {
		this.invoiceno = invoiceno;
	}

	/**
	 * 属性 outstoreno的Getter方法.属性名：出库单号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getOutstoreno() {
		return this.outstoreno;
	}

	/**
	 * 属性outstoreno的Setter方法.属性名：出库单号 创建日期:2019-12-9
	 * 
	 * @param newOutstoreno
	 *            java.lang.String
	 */
	public void setOutstoreno(java.lang.String outstoreno) {
		this.outstoreno = outstoreno;
	}

	/**
	 * 属性 checkelement的Getter方法.属性名：责任核算要素 创建日期:2019-12-9
	 * 
	 * @return nc.vo.resa.factor.FactorAsoaVO
	 */
	public java.lang.String getCheckelement() {
		return this.checkelement;
	}

	/**
	 * 属性checkelement的Setter方法.属性名：责任核算要素 创建日期:2019-12-9
	 * 
	 * @param newCheckelement
	 *            nc.vo.resa.factor.FactorAsoaVO
	 */
	public void setCheckelement(java.lang.String checkelement) {
		this.checkelement = checkelement;
	}

	/**
	 * 属性 grouprate的Getter方法.属性名：集团本币汇率 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGrouprate() {
		return this.grouprate;
	}

	/**
	 * 属性grouprate的Setter方法.属性名：集团本币汇率 创建日期:2019-12-9
	 * 
	 * @param newGrouprate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGrouprate(nc.vo.pub.lang.UFDouble grouprate) {
		this.grouprate = grouprate;
	}

	/**
	 * 属性 globalrate的Getter方法.属性名：全局本币汇率 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGlobalrate() {
		return this.globalrate;
	}

	/**
	 * 属性globalrate的Setter方法.属性名：全局本币汇率 创建日期:2019-12-9
	 * 
	 * @param newGlobalrate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGlobalrate(nc.vo.pub.lang.UFDouble globalrate) {
		this.globalrate = globalrate;
	}

	/**
	 * 属性 groupcrebit的Getter方法.属性名：集团本币金额(贷方) 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGroupcrebit() {
		return this.groupcrebit;
	}

	/**
	 * 属性groupcrebit的Setter方法.属性名：集团本币金额(贷方) 创建日期:2019-12-9
	 * 
	 * @param newGroupcrebit
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGroupcrebit(nc.vo.pub.lang.UFDouble groupcrebit) {
		this.groupcrebit = groupcrebit;
	}

	/**
	 * 属性 globalcrebit的Getter方法.属性名：全局本币金额(贷方) 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGlobalcrebit() {
		return this.globalcrebit;
	}

	/**
	 * 属性globalcrebit的Setter方法.属性名：全局本币金额(贷方) 创建日期:2019-12-9
	 * 
	 * @param newGlobalcrebit
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGlobalcrebit(nc.vo.pub.lang.UFDouble globalcrebit) {
		this.globalcrebit = globalcrebit;
	}

	/**
	 * 属性 groupbalance的Getter方法.属性名：集团本币余额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGroupbalance() {
		return this.groupbalance;
	}

	/**
	 * 属性groupbalance的Setter方法.属性名：集团本币余额 创建日期:2019-12-9
	 * 
	 * @param newGroupbalance
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGroupbalance(nc.vo.pub.lang.UFDouble groupbalance) {
		this.groupbalance = groupbalance;
	}

	/**
	 * 属性 globalbalance的Getter方法.属性名：全局本币余额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGlobalbalance() {
		return this.globalbalance;
	}

	/**
	 * 属性globalbalance的Setter方法.属性名：全局本币余额 创建日期:2019-12-9
	 * 
	 * @param newGlobalbalance
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGlobalbalance(nc.vo.pub.lang.UFDouble globalbalance) {
		this.globalbalance = globalbalance;
	}

	/**
	 * 属性 groupnotax_cre的Getter方法.属性名：集团本币无税金额(贷方) 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGroupnotax_cre() {
		return this.groupnotax_cre;
	}

	/**
	 * 属性groupnotax_cre的Setter方法.属性名：集团本币无税金额(贷方) 创建日期:2019-12-9
	 * 
	 * @param newGroupnotax_cre
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGroupnotax_cre(nc.vo.pub.lang.UFDouble groupnotax_cre) {
		this.groupnotax_cre = groupnotax_cre;
	}

	/**
	 * 属性 globalnotax_cre的Getter方法.属性名：全局本币无税金额(贷方) 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGlobalnotax_cre() {
		return this.globalnotax_cre;
	}

	/**
	 * 属性globalnotax_cre的Setter方法.属性名：全局本币无税金额(贷方) 创建日期:2019-12-9
	 * 
	 * @param newGlobalnotax_cre
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGlobalnotax_cre(nc.vo.pub.lang.UFDouble globalnotax_cre) {
		this.globalnotax_cre = globalnotax_cre;
	}

	/**
	 * 属性 occupationmny的Getter方法.属性名：预占用原币余额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getOccupationmny() {
		return this.occupationmny;
	}

	/**
	 * 属性occupationmny的Setter方法.属性名：预占用原币余额 创建日期:2019-12-9
	 * 
	 * @param newOccupationmny
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setOccupationmny(nc.vo.pub.lang.UFDouble occupationmny) {
		this.occupationmny = occupationmny;
	}

	/**
	 * 属性 project的Getter方法.属性名：项目 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pmpub.project.ProjectHeadVO
	 */
	public java.lang.String getProject() {
		return this.project;
	}

	/**
	 * 属性project的Setter方法.属性名：项目 创建日期:2019-12-9
	 * 
	 * @param newProject
	 *            nc.vo.pmpub.project.ProjectHeadVO
	 */
	public void setProject(java.lang.String project) {
		this.project = project;
	}

	/**
	 * 属性 project_task的Getter方法.属性名：项目任务 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pmpub.wbs.WbsVO
	 */
	public java.lang.String getProject_task() {
		return this.project_task;
	}

	/**
	 * 属性project_task的Setter方法.属性名：项目任务 创建日期:2019-12-9
	 * 
	 * @param newProject_task
	 *            nc.vo.pmpub.wbs.WbsVO
	 */
	public void setProject_task(java.lang.String project_task) {
		this.project_task = project_task;
	}

	/**
	 * 属性 settleno的Getter方法.属性名：结算清单号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSettleno() {
		return this.settleno;
	}

	/**
	 * 属性settleno的Setter方法.属性名：结算清单号 创建日期:2019-12-9
	 * 
	 * @param newSettleno
	 *            java.lang.String
	 */
	public void setSettleno(java.lang.String settleno) {
		this.settleno = settleno;
	}

	/**
	 * 属性 local_price的Getter方法.属性名：本币单价 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_price() {
		return this.local_price;
	}

	/**
	 * 属性local_price的Setter方法.属性名：本币单价 创建日期:2019-12-9
	 * 
	 * @param newLocal_price
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_price(nc.vo.pub.lang.UFDouble local_price) {
		this.local_price = local_price;
	}

	/**
	 * 属性 local_taxprice的Getter方法.属性名：本币含税单价 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_taxprice() {
		return this.local_taxprice;
	}

	/**
	 * 属性local_taxprice的Setter方法.属性名：本币含税单价 创建日期:2019-12-9
	 * 
	 * @param newLocal_taxprice
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_taxprice(nc.vo.pub.lang.UFDouble local_taxprice) {
		this.local_taxprice = local_taxprice;
	}

	/**
	 * 属性 costcenter的Getter方法.属性名：成本中心 创建日期:2019-12-9
	 * 
	 * @return nc.vo.resa.costcenter.CostCenterVO
	 */
	public java.lang.String getCostcenter() {
		return this.costcenter;
	}

	/**
	 * 属性costcenter的Setter方法.属性名：成本中心 创建日期:2019-12-9
	 * 
	 * @param newCostcenter
	 *            nc.vo.resa.costcenter.CostCenterVO
	 */
	public void setCostcenter(java.lang.String costcenter) {
		this.costcenter = costcenter;
	}

	/**
	 * 属性 confernum的Getter方法.属性名：内部交易结算号 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getConfernum() {
		return this.confernum;
	}

	/**
	 * 属性confernum的Setter方法.属性名：内部交易结算号 创建日期:2019-12-9
	 * 
	 * @param newConfernum
	 *            java.lang.String
	 */
	public void setConfernum(java.lang.String confernum) {
		this.confernum = confernum;
	}

	/**
	 * 属性 sendcountryid的Getter方法.属性名：发货国 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.countryzone.CountryZoneVO
	 */
	public java.lang.String getSendcountryid() {
		return this.sendcountryid;
	}

	/**
	 * 属性sendcountryid的Setter方法.属性名：发货国 创建日期:2019-12-9
	 * 
	 * @param newSendcountryid
	 *            nc.vo.bd.countryzone.CountryZoneVO
	 */
	public void setSendcountryid(java.lang.String sendcountryid) {
		this.sendcountryid = sendcountryid;
	}

	/**
	 * 属性 buysellflag的Getter方法.属性名：购销类型 创建日期:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getBuysellflag() {
		return this.buysellflag;
	}

	/**
	 * 属性buysellflag的Setter方法.属性名：购销类型 创建日期:2019-12-9
	 * 
	 * @param newBuysellflag
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setBuysellflag(java.lang.Integer buysellflag) {
		this.buysellflag = buysellflag;
	}

	/**
	 * 属性 taxcodeid的Getter方法.属性名：税码 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.taxcode.TaxcodeVO
	 */
	public java.lang.String getTaxcodeid() {
		return this.taxcodeid;
	}

	/**
	 * 属性taxcodeid的Setter方法.属性名：税码 创建日期:2019-12-9
	 * 
	 * @param newTaxcodeid
	 *            nc.vo.bd.taxcode.TaxcodeVO
	 */
	public void setTaxcodeid(java.lang.String taxcodeid) {
		this.taxcodeid = taxcodeid;
	}

	/**
	 * 属性 nosubtaxrate的Getter方法.属性名：不可抵扣税率 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getNosubtaxrate() {
		return this.nosubtaxrate;
	}

	/**
	 * 属性nosubtaxrate的Setter方法.属性名：不可抵扣税率 创建日期:2019-12-9
	 * 
	 * @param newNosubtaxrate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setNosubtaxrate(nc.vo.pub.lang.UFDouble nosubtaxrate) {
		this.nosubtaxrate = nosubtaxrate;
	}

	/**
	 * 属性 nosubtax的Getter方法.属性名：不可抵扣税额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getNosubtax() {
		return this.nosubtax;
	}

	/**
	 * 属性nosubtax的Setter方法.属性名：不可抵扣税额 创建日期:2019-12-9
	 * 
	 * @param newNosubtax
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setNosubtax(nc.vo.pub.lang.UFDouble nosubtax) {
		this.nosubtax = nosubtax;
	}

	/**
	 * 属性 caltaxmny的Getter方法.属性名：计税金额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getCaltaxmny() {
		return this.caltaxmny;
	}

	/**
	 * 属性caltaxmny的Setter方法.属性名：计税金额 创建日期:2019-12-9
	 * 
	 * @param newCaltaxmny
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setCaltaxmny(nc.vo.pub.lang.UFDouble caltaxmny) {
		this.caltaxmny = caltaxmny;
	}

	/**
	 * 属性 opptaxflag的Getter方法.属性名：是否逆向征税 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getOpptaxflag() {
		return this.opptaxflag;
	}

	/**
	 * 属性opptaxflag的Setter方法.属性名：是否逆向征税 创建日期:2019-12-9
	 * 
	 * @param newOpptaxflag
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setOpptaxflag(UFBoolean opptaxflag) {
		this.opptaxflag = opptaxflag;
	}

	/**
	 * 属性 vendorvatcode的Getter方法.属性名：供应商VAT注册码 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVendorvatcode() {
		return this.vendorvatcode;
	}

	/**
	 * 属性vendorvatcode的Setter方法.属性名：供应商VAT注册码 创建日期:2019-12-9
	 * 
	 * @param newVendorvatcode
	 *            java.lang.String
	 */
	public void setVendorvatcode(java.lang.String vendorvatcode) {
		this.vendorvatcode = vendorvatcode;
	}

	/**
	 * 属性 vatcode的Getter方法.属性名：VAT注册码 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVatcode() {
		return this.vatcode;
	}

	/**
	 * 属性vatcode的Setter方法.属性名：VAT注册码 创建日期:2019-12-9
	 * 
	 * @param newVatcode
	 *            java.lang.String
	 */
	public void setVatcode(java.lang.String vatcode) {
		this.vatcode = vatcode;
	}

	/**
	 * 属性 pk_payablebill的Getter方法.属性名：供应商应付单标识 创建日期:2019-12-9
	 * 
	 * @return nc.vo.arap.payable.PayableBillVO
	 */
	public java.lang.String getPk_payablebill() {
		return this.pk_payablebill;
	}

	/**
	 * 属性pk_payablebill的Setter方法.属性名：供应商应付单标识 创建日期:2019-12-9
	 * 
	 * @param newPk_payablebill
	 *            nc.vo.arap.payable.PayableBillVO
	 */
	public void setPk_payablebill(java.lang.String pk_payablebill) {
		this.pk_payablebill = pk_payablebill;
	}

	/**
	 * 属性 material_src的Getter方法.属性名：原始物料 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.material.MaterialVO
	 */
	public java.lang.String getMaterial_src() {
		return this.material_src;
	}

	/**
	 * 属性material_src的Setter方法.属性名：原始物料 创建日期:2019-12-9
	 * 
	 * @param newMaterial_src
	 *            nc.vo.bd.material.MaterialVO
	 */
	public void setMaterial_src(java.lang.String material_src) {
		this.material_src = material_src;
	}

	/**
	 * 属性 settlemoney的Getter方法.属性名：付款金额 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getSettlemoney() {
		return this.settlemoney;
	}

	/**
	 * 属性settlemoney的Setter方法.属性名：付款金额 创建日期:2019-12-9
	 * 
	 * @param newSettlemoney
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setSettlemoney(nc.vo.pub.lang.UFDouble settlemoney) {
		this.settlemoney = settlemoney;
	}

	/**
	 * 属性 settlecurr的Getter方法.属性名：付款币种 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.currtype.CurrtypeVO
	 */
	public java.lang.String getSettlecurr() {
		return this.settlecurr;
	}

	/**
	 * 属性settlecurr的Setter方法.属性名：付款币种 创建日期:2019-12-9
	 * 
	 * @param newSettlecurr
	 *            nc.vo.bd.currtype.CurrtypeVO
	 */
	public void setSettlecurr(java.lang.String settlecurr) {
		this.settlecurr = settlecurr;
	}

	/**
	 * 属性 pk_balatype的Getter方法.属性名：结算方式 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.balatype.BalaTypeVO
	 */
	public java.lang.String getPk_balatype() {
		return this.pk_balatype;
	}

	/**
	 * 属性pk_balatype的Setter方法.属性名：结算方式 创建日期:2019-12-9
	 * 
	 * @param newPk_balatype
	 *            nc.vo.bd.balatype.BalaTypeVO
	 */
	public void setPk_balatype(java.lang.String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}

	/**
	 * 属性 cbs的Getter方法.属性名：CBS 创建日期:2019-12-9
	 * 
	 * @return nc.vo.bd.cbs.CBSNodeVO
	 */
	public java.lang.String getCbs() {
		return this.cbs;
	}

	/**
	 * 属性cbs的Setter方法.属性名：CBS 创建日期:2019-12-9
	 * 
	 * @param newCbs
	 *            nc.vo.bd.cbs.CBSNodeVO
	 */
	public void setCbs(java.lang.String cbs) {
		this.cbs = cbs;
	}

	/**
	 * 属性 def1的Getter方法.属性名：自定义项1 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef1() {
		return this.def1;
	}

	/**
	 * 属性def1的Setter方法.属性名：自定义项1 创建日期:2019-12-9
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(java.lang.String def1) {
		this.def1 = def1;
	}

	/**
	 * 属性 def2的Getter方法.属性名：自定义项2 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef2() {
		return this.def2;
	}

	/**
	 * 属性def2的Setter方法.属性名：自定义项2 创建日期:2019-12-9
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(java.lang.String def2) {
		this.def2 = def2;
	}

	/**
	 * 属性 def3的Getter方法.属性名：自定义项3 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef3() {
		return this.def3;
	}

	/**
	 * 属性def3的Setter方法.属性名：自定义项3 创建日期:2019-12-9
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(java.lang.String def3) {
		this.def3 = def3;
	}

	/**
	 * 属性 def4的Getter方法.属性名：自定义项4 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef4() {
		return this.def4;
	}

	/**
	 * 属性def4的Setter方法.属性名：自定义项4 创建日期:2019-12-9
	 * 
	 * @param newDef4
	 *            java.lang.String
	 */
	public void setDef4(java.lang.String def4) {
		this.def4 = def4;
	}

	/**
	 * 属性 def5的Getter方法.属性名：自定义项5 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef5() {
		return this.def5;
	}

	/**
	 * 属性def5的Setter方法.属性名：自定义项5 创建日期:2019-12-9
	 * 
	 * @param newDef5
	 *            java.lang.String
	 */
	public void setDef5(java.lang.String def5) {
		this.def5 = def5;
	}

	/**
	 * 属性 def6的Getter方法.属性名：自定义项6 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef6() {
		return this.def6;
	}

	/**
	 * 属性def6的Setter方法.属性名：自定义项6 创建日期:2019-12-9
	 * 
	 * @param newDef6
	 *            java.lang.String
	 */
	public void setDef6(java.lang.String def6) {
		this.def6 = def6;
	}

	/**
	 * 属性 def7的Getter方法.属性名：自定义项7 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef7() {
		return this.def7;
	}

	/**
	 * 属性def7的Setter方法.属性名：自定义项7 创建日期:2019-12-9
	 * 
	 * @param newDef7
	 *            java.lang.String
	 */
	public void setDef7(java.lang.String def7) {
		this.def7 = def7;
	}

	/**
	 * 属性 def8的Getter方法.属性名：自定义项8 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef8() {
		return this.def8;
	}

	/**
	 * 属性def8的Setter方法.属性名：自定义项8 创建日期:2019-12-9
	 * 
	 * @param newDef8
	 *            java.lang.String
	 */
	public void setDef8(java.lang.String def8) {
		this.def8 = def8;
	}

	/**
	 * 属性 def9的Getter方法.属性名：自定义项9 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef9() {
		return this.def9;
	}

	/**
	 * 属性def9的Setter方法.属性名：自定义项9 创建日期:2019-12-9
	 * 
	 * @param newDef9
	 *            java.lang.String
	 */
	public void setDef9(java.lang.String def9) {
		this.def9 = def9;
	}

	/**
	 * 属性 def10的Getter方法.属性名：自定义项10 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef10() {
		return this.def10;
	}

	/**
	 * 属性def10的Setter方法.属性名：自定义项10 创建日期:2019-12-9
	 * 
	 * @param newDef10
	 *            java.lang.String
	 */
	public void setDef10(java.lang.String def10) {
		this.def10 = def10;
	}

	/**
	 * 属性 def11的Getter方法.属性名：自定义项11 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef11() {
		return this.def11;
	}

	/**
	 * 属性def11的Setter方法.属性名：自定义项11 创建日期:2019-12-9
	 * 
	 * @param newDef11
	 *            java.lang.String
	 */
	public void setDef11(java.lang.String def11) {
		this.def11 = def11;
	}

	/**
	 * 属性 def12的Getter方法.属性名：自定义项12 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef12() {
		return this.def12;
	}

	/**
	 * 属性def12的Setter方法.属性名：自定义项12 创建日期:2019-12-9
	 * 
	 * @param newDef12
	 *            java.lang.String
	 */
	public void setDef12(java.lang.String def12) {
		this.def12 = def12;
	}

	/**
	 * 属性 def13的Getter方法.属性名：自定义项13 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef13() {
		return this.def13;
	}

	/**
	 * 属性def13的Setter方法.属性名：自定义项13 创建日期:2019-12-9
	 * 
	 * @param newDef13
	 *            java.lang.String
	 */
	public void setDef13(java.lang.String def13) {
		this.def13 = def13;
	}

	/**
	 * 属性 def14的Getter方法.属性名：自定义项14 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef14() {
		return this.def14;
	}

	/**
	 * 属性def14的Setter方法.属性名：自定义项14 创建日期:2019-12-9
	 * 
	 * @param newDef14
	 *            java.lang.String
	 */
	public void setDef14(java.lang.String def14) {
		this.def14 = def14;
	}

	/**
	 * 属性 def15的Getter方法.属性名：自定义项15 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef15() {
		return this.def15;
	}

	/**
	 * 属性def15的Setter方法.属性名：自定义项15 创建日期:2019-12-9
	 * 
	 * @param newDef15
	 *            java.lang.String
	 */
	public void setDef15(java.lang.String def15) {
		this.def15 = def15;
	}

	/**
	 * 属性 def16的Getter方法.属性名：自定义项16 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef16() {
		return this.def16;
	}

	/**
	 * 属性def16的Setter方法.属性名：自定义项16 创建日期:2019-12-9
	 * 
	 * @param newDef16
	 *            java.lang.String
	 */
	public void setDef16(java.lang.String def16) {
		this.def16 = def16;
	}

	/**
	 * 属性 def17的Getter方法.属性名：自定义项17 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef17() {
		return this.def17;
	}

	/**
	 * 属性def17的Setter方法.属性名：自定义项17 创建日期:2019-12-9
	 * 
	 * @param newDef17
	 *            java.lang.String
	 */
	public void setDef17(java.lang.String def17) {
		this.def17 = def17;
	}

	/**
	 * 属性 def18的Getter方法.属性名：自定义项18 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef18() {
		return this.def18;
	}

	/**
	 * 属性def18的Setter方法.属性名：自定义项18 创建日期:2019-12-9
	 * 
	 * @param newDef18
	 *            java.lang.String
	 */
	public void setDef18(java.lang.String def18) {
		this.def18 = def18;
	}

	/**
	 * 属性 def19的Getter方法.属性名：自定义项19 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef19() {
		return this.def19;
	}

	/**
	 * 属性def19的Setter方法.属性名：自定义项19 创建日期:2019-12-9
	 * 
	 * @param newDef19
	 *            java.lang.String
	 */
	public void setDef19(java.lang.String def19) {
		this.def19 = def19;
	}

	/**
	 * 属性 def20的Getter方法.属性名：自定义项20 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef20() {
		return this.def20;
	}

	/**
	 * 属性def20的Setter方法.属性名：自定义项20 创建日期:2019-12-9
	 * 
	 * @param newDef20
	 *            java.lang.String
	 */
	public void setDef20(java.lang.String def20) {
		this.def20 = def20;
	}

	/**
	 * 属性 def21的Getter方法.属性名：自定义项21 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef21() {
		return this.def21;
	}

	/**
	 * 属性def21的Setter方法.属性名：自定义项21 创建日期:2019-12-9
	 * 
	 * @param newDef21
	 *            java.lang.String
	 */
	public void setDef21(java.lang.String def21) {
		this.def21 = def21;
	}

	/**
	 * 属性 def22的Getter方法.属性名：自定义项22 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef22() {
		return this.def22;
	}

	/**
	 * 属性def22的Setter方法.属性名：自定义项22 创建日期:2019-12-9
	 * 
	 * @param newDef22
	 *            java.lang.String
	 */
	public void setDef22(java.lang.String def22) {
		this.def22 = def22;
	}

	/**
	 * 属性 def23的Getter方法.属性名：自定义项23 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef23() {
		return this.def23;
	}

	/**
	 * 属性def23的Setter方法.属性名：自定义项23 创建日期:2019-12-9
	 * 
	 * @param newDef23
	 *            java.lang.String
	 */
	public void setDef23(java.lang.String def23) {
		this.def23 = def23;
	}

	/**
	 * 属性 def24的Getter方法.属性名：自定义项24 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef24() {
		return this.def24;
	}

	/**
	 * 属性def24的Setter方法.属性名：自定义项24 创建日期:2019-12-9
	 * 
	 * @param newDef24
	 *            java.lang.String
	 */
	public void setDef24(java.lang.String def24) {
		this.def24 = def24;
	}

	/**
	 * 属性 def25的Getter方法.属性名：自定义项25 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef25() {
		return this.def25;
	}

	/**
	 * 属性def25的Setter方法.属性名：自定义项25 创建日期:2019-12-9
	 * 
	 * @param newDef25
	 *            java.lang.String
	 */
	public void setDef25(java.lang.String def25) {
		this.def25 = def25;
	}

	/**
	 * 属性 def26的Getter方法.属性名：自定义项26 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef26() {
		return this.def26;
	}

	/**
	 * 属性def26的Setter方法.属性名：自定义项26 创建日期:2019-12-9
	 * 
	 * @param newDef26
	 *            java.lang.String
	 */
	public void setDef26(java.lang.String def26) {
		this.def26 = def26;
	}

	/**
	 * 属性 def27的Getter方法.属性名：自定义项27 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef27() {
		return this.def27;
	}

	/**
	 * 属性def27的Setter方法.属性名：自定义项27 创建日期:2019-12-9
	 * 
	 * @param newDef27
	 *            java.lang.String
	 */
	public void setDef27(java.lang.String def27) {
		this.def27 = def27;
	}

	/**
	 * 属性 def28的Getter方法.属性名：自定义项28 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef28() {
		return this.def28;
	}

	/**
	 * 属性def28的Setter方法.属性名：自定义项28 创建日期:2019-12-9
	 * 
	 * @param newDef28
	 *            java.lang.String
	 */
	public void setDef28(java.lang.String def28) {
		this.def28 = def28;
	}

	/**
	 * 属性 def29的Getter方法.属性名：自定义项29 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef29() {
		return this.def29;
	}

	/**
	 * 属性def29的Setter方法.属性名：自定义项29 创建日期:2019-12-9
	 * 
	 * @param newDef29
	 *            java.lang.String
	 */
	public void setDef29(java.lang.String def29) {
		this.def29 = def29;
	}

	/**
	 * 属性 def30的Getter方法.属性名：自定义项30 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef30() {
		return this.def30;
	}

	/**
	 * 属性def30的Setter方法.属性名：自定义项30 创建日期:2019-12-9
	 * 
	 * @param newDef30
	 *            java.lang.String
	 */
	public void setDef30(java.lang.String def30) {
		this.def30 = def30;
	}

	/**
	 * 属性 def31的Getter方法.属性名：自定义项31 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef31() {
		return this.def31;
	}

	/**
	 * 属性def31的Setter方法.属性名：自定义项31 创建日期:2019-12-9
	 * 
	 * @param newDef31
	 *            java.lang.String
	 */
	public void setDef31(java.lang.String def31) {
		this.def31 = def31;
	}

	/**
	 * 属性 def32的Getter方法.属性名：自定义项32 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef32() {
		return this.def32;
	}

	/**
	 * 属性def32的Setter方法.属性名：自定义项32 创建日期:2019-12-9
	 * 
	 * @param newDef32
	 *            java.lang.String
	 */
	public void setDef32(java.lang.String def32) {
		this.def32 = def32;
	}

	/**
	 * 属性 def33的Getter方法.属性名：自定义项33 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef33() {
		return this.def33;
	}

	/**
	 * 属性def33的Setter方法.属性名：自定义项33 创建日期:2019-12-9
	 * 
	 * @param newDef33
	 *            java.lang.String
	 */
	public void setDef33(java.lang.String def33) {
		this.def33 = def33;
	}

	/**
	 * 属性 def34的Getter方法.属性名：自定义项34 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef34() {
		return this.def34;
	}

	/**
	 * 属性def34的Setter方法.属性名：自定义项34 创建日期:2019-12-9
	 * 
	 * @param newDef34
	 *            java.lang.String
	 */
	public void setDef34(java.lang.String def34) {
		this.def34 = def34;
	}

	/**
	 * 属性 def35的Getter方法.属性名：自定义项35 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef35() {
		return this.def35;
	}

	/**
	 * 属性def35的Setter方法.属性名：自定义项35 创建日期:2019-12-9
	 * 
	 * @param newDef35
	 *            java.lang.String
	 */
	public void setDef35(java.lang.String def35) {
		this.def35 = def35;
	}

	/**
	 * 属性 def36的Getter方法.属性名：自定义项36 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef36() {
		return this.def36;
	}

	/**
	 * 属性def36的Setter方法.属性名：自定义项36 创建日期:2019-12-9
	 * 
	 * @param newDef36
	 *            java.lang.String
	 */
	public void setDef36(java.lang.String def36) {
		this.def36 = def36;
	}

	/**
	 * 属性 def37的Getter方法.属性名：自定义项37 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef37() {
		return this.def37;
	}

	/**
	 * 属性def37的Setter方法.属性名：自定义项37 创建日期:2019-12-9
	 * 
	 * @param newDef37
	 *            java.lang.String
	 */
	public void setDef37(java.lang.String def37) {
		this.def37 = def37;
	}

	/**
	 * 属性 def38的Getter方法.属性名：自定义项38 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef38() {
		return this.def38;
	}

	/**
	 * 属性def38的Setter方法.属性名：自定义项38 创建日期:2019-12-9
	 * 
	 * @param newDef38
	 *            java.lang.String
	 */
	public void setDef38(java.lang.String def38) {
		this.def38 = def38;
	}

	/**
	 * 属性 def39的Getter方法.属性名：自定义项39 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef39() {
		return this.def39;
	}

	/**
	 * 属性def39的Setter方法.属性名：自定义项39 创建日期:2019-12-9
	 * 
	 * @param newDef39
	 *            java.lang.String
	 */
	public void setDef39(java.lang.String def39) {
		this.def39 = def39;
	}

	/**
	 * 属性 def40的Getter方法.属性名：自定义项40 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef40() {
		return this.def40;
	}

	/**
	 * 属性def40的Setter方法.属性名：自定义项40 创建日期:2019-12-9
	 * 
	 * @param newDef40
	 *            java.lang.String
	 */
	public void setDef40(java.lang.String def40) {
		this.def40 = def40;
	}

	/**
	 * 属性 def41的Getter方法.属性名：自定义项41 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef41() {
		return this.def41;
	}

	/**
	 * 属性def41的Setter方法.属性名：自定义项41 创建日期:2019-12-9
	 * 
	 * @param newDef41
	 *            java.lang.String
	 */
	public void setDef41(java.lang.String def41) {
		this.def41 = def41;
	}

	/**
	 * 属性 def42的Getter方法.属性名：自定义项42 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef42() {
		return this.def42;
	}

	/**
	 * 属性def42的Setter方法.属性名：自定义项42 创建日期:2019-12-9
	 * 
	 * @param newDef42
	 *            java.lang.String
	 */
	public void setDef42(java.lang.String def42) {
		this.def42 = def42;
	}

	/**
	 * 属性 def43的Getter方法.属性名：自定义项43 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef43() {
		return this.def43;
	}

	/**
	 * 属性def43的Setter方法.属性名：自定义项43 创建日期:2019-12-9
	 * 
	 * @param newDef43
	 *            java.lang.String
	 */
	public void setDef43(java.lang.String def43) {
		this.def43 = def43;
	}

	/**
	 * 属性 def44的Getter方法.属性名：自定义项44 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef44() {
		return this.def44;
	}

	/**
	 * 属性def44的Setter方法.属性名：自定义项44 创建日期:2019-12-9
	 * 
	 * @param newDef44
	 *            java.lang.String
	 */
	public void setDef44(java.lang.String def44) {
		this.def44 = def44;
	}

	/**
	 * 属性 def45的Getter方法.属性名：自定义项45 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef45() {
		return this.def45;
	}

	/**
	 * 属性def45的Setter方法.属性名：自定义项45 创建日期:2019-12-9
	 * 
	 * @param newDef45
	 *            java.lang.String
	 */
	public void setDef45(java.lang.String def45) {
		this.def45 = def45;
	}

	/**
	 * 属性 def46的Getter方法.属性名：自定义项46 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef46() {
		return this.def46;
	}

	/**
	 * 属性def46的Setter方法.属性名：自定义项46 创建日期:2019-12-9
	 * 
	 * @param newDef46
	 *            java.lang.String
	 */
	public void setDef46(java.lang.String def46) {
		this.def46 = def46;
	}

	/**
	 * 属性 def47的Getter方法.属性名：自定义项47 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef47() {
		return this.def47;
	}

	/**
	 * 属性def47的Setter方法.属性名：自定义项47 创建日期:2019-12-9
	 * 
	 * @param newDef47
	 *            java.lang.String
	 */
	public void setDef47(java.lang.String def47) {
		this.def47 = def47;
	}

	/**
	 * 属性 def48的Getter方法.属性名：自定义项48 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef48() {
		return this.def48;
	}

	/**
	 * 属性def48的Setter方法.属性名：自定义项48 创建日期:2019-12-9
	 * 
	 * @param newDef48
	 *            java.lang.String
	 */
	public void setDef48(java.lang.String def48) {
		this.def48 = def48;
	}

	/**
	 * 属性 def49的Getter方法.属性名：自定义项49 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef49() {
		return this.def49;
	}

	/**
	 * 属性def49的Setter方法.属性名：自定义项49 创建日期:2019-12-9
	 * 
	 * @param newDef49
	 *            java.lang.String
	 */
	public void setDef49(java.lang.String def49) {
		this.def49 = def49;
	}

	/**
	 * 属性 def50的Getter方法.属性名：自定义项50 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef50() {
		return this.def50;
	}

	/**
	 * 属性def50的Setter方法.属性名：自定义项50 创建日期:2019-12-9
	 * 
	 * @param newDef50
	 *            java.lang.String
	 */
	public void setDef50(java.lang.String def50) {
		this.def50 = def50;
	}

	/**
	 * 属性 def51的Getter方法.属性名：自定义项51 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef51() {
		return this.def51;
	}

	/**
	 * 属性def51的Setter方法.属性名：自定义项51 创建日期:2019-12-9
	 * 
	 * @param newDef51
	 *            java.lang.String
	 */
	public void setDef51(java.lang.String def51) {
		this.def51 = def51;
	}

	/**
	 * 属性 def52的Getter方法.属性名：自定义项52 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef52() {
		return this.def52;
	}

	/**
	 * 属性def52的Setter方法.属性名：自定义项52 创建日期:2019-12-9
	 * 
	 * @param newDef52
	 *            java.lang.String
	 */
	public void setDef52(java.lang.String def52) {
		this.def52 = def52;
	}

	/**
	 * 属性 def53的Getter方法.属性名：自定义项53 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef53() {
		return this.def53;
	}

	/**
	 * 属性def53的Setter方法.属性名：自定义项53 创建日期:2019-12-9
	 * 
	 * @param newDef53
	 *            java.lang.String
	 */
	public void setDef53(java.lang.String def53) {
		this.def53 = def53;
	}

	/**
	 * 属性 def54的Getter方法.属性名：自定义项54 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef54() {
		return this.def54;
	}

	/**
	 * 属性def54的Setter方法.属性名：自定义项54 创建日期:2019-12-9
	 * 
	 * @param newDef54
	 *            java.lang.String
	 */
	public void setDef54(java.lang.String def54) {
		this.def54 = def54;
	}

	/**
	 * 属性 def55的Getter方法.属性名：自定义项55 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef55() {
		return this.def55;
	}

	/**
	 * 属性def55的Setter方法.属性名：自定义项55 创建日期:2019-12-9
	 * 
	 * @param newDef55
	 *            java.lang.String
	 */
	public void setDef55(java.lang.String def55) {
		this.def55 = def55;
	}

	/**
	 * 属性 def56的Getter方法.属性名：自定义项56 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef56() {
		return this.def56;
	}

	/**
	 * 属性def56的Setter方法.属性名：自定义项56 创建日期:2019-12-9
	 * 
	 * @param newDef56
	 *            java.lang.String
	 */
	public void setDef56(java.lang.String def56) {
		this.def56 = def56;
	}

	/**
	 * 属性 def57的Getter方法.属性名：自定义项57 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef57() {
		return this.def57;
	}

	/**
	 * 属性def57的Setter方法.属性名：自定义项57 创建日期:2019-12-9
	 * 
	 * @param newDef57
	 *            java.lang.String
	 */
	public void setDef57(java.lang.String def57) {
		this.def57 = def57;
	}

	/**
	 * 属性 def58的Getter方法.属性名：自定义项58 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef58() {
		return this.def58;
	}

	/**
	 * 属性def58的Setter方法.属性名：自定义项58 创建日期:2019-12-9
	 * 
	 * @param newDef58
	 *            java.lang.String
	 */
	public void setDef58(java.lang.String def58) {
		this.def58 = def58;
	}

	/**
	 * 属性 def59的Getter方法.属性名：自定义项59 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef59() {
		return this.def59;
	}

	/**
	 * 属性def59的Setter方法.属性名：自定义项59 创建日期:2019-12-9
	 * 
	 * @param newDef59
	 *            java.lang.String
	 */
	public void setDef59(java.lang.String def59) {
		this.def59 = def59;
	}

	/**
	 * 属性 def60的Getter方法.属性名：自定义项60 创建日期:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef60() {
		return this.def60;
	}

	/**
	 * 属性def60的Setter方法.属性名：自定义项60 创建日期:2019-12-9
	 * 
	 * @param newDef60
	 *            java.lang.String
	 */
	public void setDef60(java.lang.String def60) {
		this.def60 = def60;
	}

	/**
	 * 属性 生成上层主键的Getter方法.属性名：上层主键 创建日期:2019-12-9
	 * 
	 * @return String
	 */
	public String getPk_payreq() {
		return this.pk_payreq;
	}

	/**
	 * 属性生成上层主键的Setter方法.属性名：上层主键 创建日期:2019-12-9
	 * 
	 * @param newPk_payreq
	 *            String
	 */
	public void setPk_payreq(String pk_payreq) {
		this.pk_payreq = pk_payreq;
	}

	/**
	 * 属性 生成时间戳的Getter方法.属性名：时间戳 创建日期:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * 属性生成时间戳的Setter方法.属性名：时间戳 创建日期:2019-12-9
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.payablepage");
	}
}
