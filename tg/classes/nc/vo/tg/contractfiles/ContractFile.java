package nc.vo.tg.contractfiles;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 * 此处添加累的描述信息
 * </p>
 * 创建日期:2020-3-25
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class ContractFile extends SuperVO {

	/**
	 * 档案主键
	 */
	public java.lang.String pk_defdoc;
	/**
	 * 所属集团
	 */
	public java.lang.String pk_group;
	/**
	 * 所属组织
	 */
	public java.lang.String pk_org;
	/**
	 * 档案编码
	 */
	public java.lang.String code;
	/**
	 * 助记码
	 */
	public java.lang.String mnecode;
	/**
	 * 档案名称
	 */
	public java.lang.String name;
	/**
	 * 档案名称2
	 */
	public java.lang.String name2;
	/**
	 * 档案名称3
	 */
	public java.lang.String name3;
	/**
	 * 档案名称4
	 */
	public java.lang.String name4;
	/**
	 * 档案名称5
	 */
	public java.lang.String name5;
	/**
	 * 档案名称6
	 */
	public java.lang.String name6;
	/**
	 * 简称
	 */
	public java.lang.String shortname;
	/**
	 * 简称2
	 */
	public java.lang.String shortname2;
	/**
	 * 简称3
	 */
	public java.lang.String shortname3;
	/**
	 * 简称4
	 */
	public java.lang.String shortname4;
	/**
	 * 简称5
	 */
	public java.lang.String shortname5;
	/**
	 * 简称6
	 */
	public java.lang.String shortname6;
	/**
	 * 档案列表主键
	 */
	public java.lang.String pk_defdoclist;
	/**
	 * 上级档案
	 */
	public java.lang.String pid;
	/**
	 * 备注
	 */
	public java.lang.String memo;
	/**
	 * 创建人
	 */
	public java.lang.String creator;
	/**
	 * 创建时间
	 */
	public UFDateTime creationtime;
	/**
	 * 最后修改人
	 */
	public java.lang.String modifier;
	/**
	 * 最后修改时间
	 */
	public UFDateTime modifiedtime;
	/**
	 * 内部编码
	 */
	public java.lang.String innercode;
	/**
	 * 启用状态
	 */
	public java.lang.String enablestate;
	/**
	 * 分布式
	 */
	public java.lang.String dataoriginflag;
	/**
	 * 数据类型
	 */
	public java.lang.String datatype;
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
	 * 时间戳
	 */
	public UFDateTime ts;

	/**
	 * 属性 pk_defdoc的Getter方法.属性名：档案主键 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_defdoc() {
		return this.pk_defdoc;
	}

	/**
	 * 属性pk_defdoc的Setter方法.属性名：档案主键 创建日期:2020-3-25
	 * 
	 * @param newPk_defdoc
	 *            java.lang.String
	 */
	public void setPk_defdoc(java.lang.String pk_defdoc) {
		this.pk_defdoc = pk_defdoc;
	}

	/**
	 * 属性 pk_group的Getter方法.属性名：所属集团 创建日期:2020-3-25
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public java.lang.String getPk_group() {
		return this.pk_group;
	}

	/**
	 * 属性pk_group的Setter方法.属性名：所属集团 创建日期:2020-3-25
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * 属性 pk_org的Getter方法.属性名：所属组织 创建日期:2020-3-25
	 * 
	 * @return nc.vo.org.OrgVO
	 */
	public java.lang.String getPk_org() {
		return this.pk_org;
	}

	/**
	 * 属性pk_org的Setter方法.属性名：所属组织 创建日期:2020-3-25
	 * 
	 * @param newPk_org
	 *            nc.vo.org.OrgVO
	 */
	public void setPk_org(java.lang.String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * 属性 code的Getter方法.属性名：档案编码 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 * 属性code的Setter方法.属性名：档案编码 创建日期:2020-3-25
	 * 
	 * @param newCode
	 *            java.lang.String
	 */
	public void setCode(java.lang.String code) {
		this.code = code;
	}

	/**
	 * 属性 mnecode的Getter方法.属性名：助记码 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMnecode() {
		return this.mnecode;
	}

	/**
	 * 属性mnecode的Setter方法.属性名：助记码 创建日期:2020-3-25
	 * 
	 * @param newMnecode
	 *            java.lang.String
	 */
	public void setMnecode(java.lang.String mnecode) {
		this.mnecode = mnecode;
	}

	/**
	 * 属性 name的Getter方法.属性名：档案名称 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 * 属性name的Setter方法.属性名：档案名称 创建日期:2020-3-25
	 * 
	 * @param newName
	 *            java.lang.String
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * 属性 name2的Getter方法.属性名：档案名称2 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName2() {
		return this.name2;
	}

	/**
	 * 属性name2的Setter方法.属性名：档案名称2 创建日期:2020-3-25
	 * 
	 * @param newName2
	 *            java.lang.String
	 */
	public void setName2(java.lang.String name2) {
		this.name2 = name2;
	}

	/**
	 * 属性 name3的Getter方法.属性名：档案名称3 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName3() {
		return this.name3;
	}

	/**
	 * 属性name3的Setter方法.属性名：档案名称3 创建日期:2020-3-25
	 * 
	 * @param newName3
	 *            java.lang.String
	 */
	public void setName3(java.lang.String name3) {
		this.name3 = name3;
	}

	/**
	 * 属性 name4的Getter方法.属性名：档案名称4 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName4() {
		return this.name4;
	}

	/**
	 * 属性name4的Setter方法.属性名：档案名称4 创建日期:2020-3-25
	 * 
	 * @param newName4
	 *            java.lang.String
	 */
	public void setName4(java.lang.String name4) {
		this.name4 = name4;
	}

	/**
	 * 属性 name5的Getter方法.属性名：档案名称5 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName5() {
		return this.name5;
	}

	/**
	 * 属性name5的Setter方法.属性名：档案名称5 创建日期:2020-3-25
	 * 
	 * @param newName5
	 *            java.lang.String
	 */
	public void setName5(java.lang.String name5) {
		this.name5 = name5;
	}

	/**
	 * 属性 name6的Getter方法.属性名：档案名称6 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName6() {
		return this.name6;
	}

	/**
	 * 属性name6的Setter方法.属性名：档案名称6 创建日期:2020-3-25
	 * 
	 * @param newName6
	 *            java.lang.String
	 */
	public void setName6(java.lang.String name6) {
		this.name6 = name6;
	}

	/**
	 * 属性 shortname的Getter方法.属性名：简称 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getShortname() {
		return this.shortname;
	}

	/**
	 * 属性shortname的Setter方法.属性名：简称 创建日期:2020-3-25
	 * 
	 * @param newShortname
	 *            java.lang.String
	 */
	public void setShortname(java.lang.String shortname) {
		this.shortname = shortname;
	}

	/**
	 * 属性 shortname2的Getter方法.属性名：简称2 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getShortname2() {
		return this.shortname2;
	}

	/**
	 * 属性shortname2的Setter方法.属性名：简称2 创建日期:2020-3-25
	 * 
	 * @param newShortname2
	 *            java.lang.String
	 */
	public void setShortname2(java.lang.String shortname2) {
		this.shortname2 = shortname2;
	}

	/**
	 * 属性 shortname3的Getter方法.属性名：简称3 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getShortname3() {
		return this.shortname3;
	}

	/**
	 * 属性shortname3的Setter方法.属性名：简称3 创建日期:2020-3-25
	 * 
	 * @param newShortname3
	 *            java.lang.String
	 */
	public void setShortname3(java.lang.String shortname3) {
		this.shortname3 = shortname3;
	}

	/**
	 * 属性 shortname4的Getter方法.属性名：简称4 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getShortname4() {
		return this.shortname4;
	}

	/**
	 * 属性shortname4的Setter方法.属性名：简称4 创建日期:2020-3-25
	 * 
	 * @param newShortname4
	 *            java.lang.String
	 */
	public void setShortname4(java.lang.String shortname4) {
		this.shortname4 = shortname4;
	}

	/**
	 * 属性 shortname5的Getter方法.属性名：简称5 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getShortname5() {
		return this.shortname5;
	}

	/**
	 * 属性shortname5的Setter方法.属性名：简称5 创建日期:2020-3-25
	 * 
	 * @param newShortname5
	 *            java.lang.String
	 */
	public void setShortname5(java.lang.String shortname5) {
		this.shortname5 = shortname5;
	}

	/**
	 * 属性 shortname6的Getter方法.属性名：简称6 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getShortname6() {
		return this.shortname6;
	}

	/**
	 * 属性shortname6的Setter方法.属性名：简称6 创建日期:2020-3-25
	 * 
	 * @param newShortname6
	 *            java.lang.String
	 */
	public void setShortname6(java.lang.String shortname6) {
		this.shortname6 = shortname6;
	}

	/**
	 * 属性 pk_defdoclist的Getter方法.属性名：档案列表主键 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_defdoclist() {
		return this.pk_defdoclist;
	}

	/**
	 * 属性pk_defdoclist的Setter方法.属性名：档案列表主键 创建日期:2020-3-25
	 * 
	 * @param newPk_defdoclist
	 *            java.lang.String
	 */
	public void setPk_defdoclist(java.lang.String pk_defdoclist) {
		this.pk_defdoclist = pk_defdoclist;
	}

	/**
	 * 属性 pid的Getter方法.属性名：上级档案 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPid() {
		return this.pid;
	}

	/**
	 * 属性pid的Setter方法.属性名：上级档案 创建日期:2020-3-25
	 * 
	 * @param newPid
	 *            java.lang.String
	 */
	public void setPid(java.lang.String pid) {
		this.pid = pid;
	}

	/**
	 * 属性 memo的Getter方法.属性名：备注 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMemo() {
		return this.memo;
	}

	/**
	 * 属性memo的Setter方法.属性名：备注 创建日期:2020-3-25
	 * 
	 * @param newMemo
	 *            java.lang.String
	 */
	public void setMemo(java.lang.String memo) {
		this.memo = memo;
	}

	/**
	 * 属性 creator的Getter方法.属性名：创建人 创建日期:2020-3-25
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getCreator() {
		return this.creator;
	}

	/**
	 * 属性creator的Setter方法.属性名：创建人 创建日期:2020-3-25
	 * 
	 * @param newCreator
	 *            nc.vo.sm.UserVO
	 */
	public void setCreator(java.lang.String creator) {
		this.creator = creator;
	}

	/**
	 * 属性 creationtime的Getter方法.属性名：创建时间 创建日期:2020-3-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * 属性creationtime的Setter方法.属性名：创建时间 创建日期:2020-3-25
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * 属性 modifier的Getter方法.属性名：最后修改人 创建日期:2020-3-25
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getModifier() {
		return this.modifier;
	}

	/**
	 * 属性modifier的Setter方法.属性名：最后修改人 创建日期:2020-3-25
	 * 
	 * @param newModifier
	 *            nc.vo.sm.UserVO
	 */
	public void setModifier(java.lang.String modifier) {
		this.modifier = modifier;
	}

	/**
	 * 属性 modifiedtime的Getter方法.属性名：最后修改时间 创建日期:2020-3-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * 属性modifiedtime的Setter方法.属性名：最后修改时间 创建日期:2020-3-25
	 * 
	 * @param newModifiedtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	/**
	 * 属性 innercode的Getter方法.属性名：内部编码 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getInnercode() {
		return this.innercode;
	}

	/**
	 * 属性innercode的Setter方法.属性名：内部编码 创建日期:2020-3-25
	 * 
	 * @param newInnercode
	 *            java.lang.String
	 */
	public void setInnercode(java.lang.String innercode) {
		this.innercode = innercode;
	}

	/**
	 * 属性 enablestate的Getter方法.属性名：启用状态 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getEnablestate() {
		return this.enablestate;
	}

	/**
	 * 属性enablestate的Setter方法.属性名：启用状态 创建日期:2020-3-25
	 * 
	 * @param newEnablestate
	 *            java.lang.String
	 */
	public void setEnablestate(java.lang.String enablestate) {
		this.enablestate = enablestate;
	}

	/**
	 * 属性 dataoriginflag的Getter方法.属性名：分布式 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDataoriginflag() {
		return this.dataoriginflag;
	}

	/**
	 * 属性dataoriginflag的Setter方法.属性名：分布式 创建日期:2020-3-25
	 * 
	 * @param newDataoriginflag
	 *            java.lang.String
	 */
	public void setDataoriginflag(java.lang.String dataoriginflag) {
		this.dataoriginflag = dataoriginflag;
	}

	/**
	 * 属性 datatype的Getter方法.属性名：数据类型 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDatatype() {
		return this.datatype;
	}

	/**
	 * 属性datatype的Setter方法.属性名：数据类型 创建日期:2020-3-25
	 * 
	 * @param newDatatype
	 *            java.lang.String
	 */
	public void setDatatype(java.lang.String datatype) {
		this.datatype = datatype;
	}

	/**
	 * 属性 def1的Getter方法.属性名：自定义项1 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef1() {
		return this.def1;
	}

	/**
	 * 属性def1的Setter方法.属性名：自定义项1 创建日期:2020-3-25
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(java.lang.String def1) {
		this.def1 = def1;
	}

	/**
	 * 属性 def2的Getter方法.属性名：自定义项2 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef2() {
		return this.def2;
	}

	/**
	 * 属性def2的Setter方法.属性名：自定义项2 创建日期:2020-3-25
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(java.lang.String def2) {
		this.def2 = def2;
	}

	/**
	 * 属性 def3的Getter方法.属性名：自定义项3 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef3() {
		return this.def3;
	}

	/**
	 * 属性def3的Setter方法.属性名：自定义项3 创建日期:2020-3-25
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(java.lang.String def3) {
		this.def3 = def3;
	}

	/**
	 * 属性 def4的Getter方法.属性名：自定义项4 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef4() {
		return this.def4;
	}

	/**
	 * 属性def4的Setter方法.属性名：自定义项4 创建日期:2020-3-25
	 * 
	 * @param newDef4
	 *            java.lang.String
	 */
	public void setDef4(java.lang.String def4) {
		this.def4 = def4;
	}

	/**
	 * 属性 def5的Getter方法.属性名：自定义项5 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef5() {
		return this.def5;
	}

	/**
	 * 属性def5的Setter方法.属性名：自定义项5 创建日期:2020-3-25
	 * 
	 * @param newDef5
	 *            java.lang.String
	 */
	public void setDef5(java.lang.String def5) {
		this.def5 = def5;
	}

	/**
	 * 属性 def6的Getter方法.属性名：自定义项6 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef6() {
		return this.def6;
	}

	/**
	 * 属性def6的Setter方法.属性名：自定义项6 创建日期:2020-3-25
	 * 
	 * @param newDef6
	 *            java.lang.String
	 */
	public void setDef6(java.lang.String def6) {
		this.def6 = def6;
	}

	/**
	 * 属性 def7的Getter方法.属性名：自定义项7 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef7() {
		return this.def7;
	}

	/**
	 * 属性def7的Setter方法.属性名：自定义项7 创建日期:2020-3-25
	 * 
	 * @param newDef7
	 *            java.lang.String
	 */
	public void setDef7(java.lang.String def7) {
		this.def7 = def7;
	}

	/**
	 * 属性 def8的Getter方法.属性名：自定义项8 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef8() {
		return this.def8;
	}

	/**
	 * 属性def8的Setter方法.属性名：自定义项8 创建日期:2020-3-25
	 * 
	 * @param newDef8
	 *            java.lang.String
	 */
	public void setDef8(java.lang.String def8) {
		this.def8 = def8;
	}

	/**
	 * 属性 def9的Getter方法.属性名：自定义项9 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef9() {
		return this.def9;
	}

	/**
	 * 属性def9的Setter方法.属性名：自定义项9 创建日期:2020-3-25
	 * 
	 * @param newDef9
	 *            java.lang.String
	 */
	public void setDef9(java.lang.String def9) {
		this.def9 = def9;
	}

	/**
	 * 属性 def10的Getter方法.属性名：自定义项10 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef10() {
		return this.def10;
	}

	/**
	 * 属性def10的Setter方法.属性名：自定义项10 创建日期:2020-3-25
	 * 
	 * @param newDef10
	 *            java.lang.String
	 */
	public void setDef10(java.lang.String def10) {
		this.def10 = def10;
	}

	/**
	 * 属性 def11的Getter方法.属性名：自定义项11 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef11() {
		return this.def11;
	}

	/**
	 * 属性def11的Setter方法.属性名：自定义项11 创建日期:2020-3-25
	 * 
	 * @param newDef11
	 *            java.lang.String
	 */
	public void setDef11(java.lang.String def11) {
		this.def11 = def11;
	}

	/**
	 * 属性 def12的Getter方法.属性名：自定义项12 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef12() {
		return this.def12;
	}

	/**
	 * 属性def12的Setter方法.属性名：自定义项12 创建日期:2020-3-25
	 * 
	 * @param newDef12
	 *            java.lang.String
	 */
	public void setDef12(java.lang.String def12) {
		this.def12 = def12;
	}

	/**
	 * 属性 def13的Getter方法.属性名：自定义项13 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef13() {
		return this.def13;
	}

	/**
	 * 属性def13的Setter方法.属性名：自定义项13 创建日期:2020-3-25
	 * 
	 * @param newDef13
	 *            java.lang.String
	 */
	public void setDef13(java.lang.String def13) {
		this.def13 = def13;
	}

	/**
	 * 属性 def14的Getter方法.属性名：自定义项14 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef14() {
		return this.def14;
	}

	/**
	 * 属性def14的Setter方法.属性名：自定义项14 创建日期:2020-3-25
	 * 
	 * @param newDef14
	 *            java.lang.String
	 */
	public void setDef14(java.lang.String def14) {
		this.def14 = def14;
	}

	/**
	 * 属性 def15的Getter方法.属性名：自定义项15 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef15() {
		return this.def15;
	}

	/**
	 * 属性def15的Setter方法.属性名：自定义项15 创建日期:2020-3-25
	 * 
	 * @param newDef15
	 *            java.lang.String
	 */
	public void setDef15(java.lang.String def15) {
		this.def15 = def15;
	}

	/**
	 * 属性 def16的Getter方法.属性名：自定义项16 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef16() {
		return this.def16;
	}

	/**
	 * 属性def16的Setter方法.属性名：自定义项16 创建日期:2020-3-25
	 * 
	 * @param newDef16
	 *            java.lang.String
	 */
	public void setDef16(java.lang.String def16) {
		this.def16 = def16;
	}

	/**
	 * 属性 def17的Getter方法.属性名：自定义项17 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef17() {
		return this.def17;
	}

	/**
	 * 属性def17的Setter方法.属性名：自定义项17 创建日期:2020-3-25
	 * 
	 * @param newDef17
	 *            java.lang.String
	 */
	public void setDef17(java.lang.String def17) {
		this.def17 = def17;
	}

	/**
	 * 属性 def18的Getter方法.属性名：自定义项18 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef18() {
		return this.def18;
	}

	/**
	 * 属性def18的Setter方法.属性名：自定义项18 创建日期:2020-3-25
	 * 
	 * @param newDef18
	 *            java.lang.String
	 */
	public void setDef18(java.lang.String def18) {
		this.def18 = def18;
	}

	/**
	 * 属性 def19的Getter方法.属性名：自定义项19 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef19() {
		return this.def19;
	}

	/**
	 * 属性def19的Setter方法.属性名：自定义项19 创建日期:2020-3-25
	 * 
	 * @param newDef19
	 *            java.lang.String
	 */
	public void setDef19(java.lang.String def19) {
		this.def19 = def19;
	}

	/**
	 * 属性 def20的Getter方法.属性名：自定义项20 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef20() {
		return this.def20;
	}

	/**
	 * 属性def20的Setter方法.属性名：自定义项20 创建日期:2020-3-25
	 * 
	 * @param newDef20
	 *            java.lang.String
	 */
	public void setDef20(java.lang.String def20) {
		this.def20 = def20;
	}

	/**
	 * 属性 def21的Getter方法.属性名：自定义项21 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef21() {
		return this.def21;
	}

	/**
	 * 属性def21的Setter方法.属性名：自定义项21 创建日期:2020-3-25
	 * 
	 * @param newDef21
	 *            java.lang.String
	 */
	public void setDef21(java.lang.String def21) {
		this.def21 = def21;
	}

	/**
	 * 属性 def22的Getter方法.属性名：自定义项22 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef22() {
		return this.def22;
	}

	/**
	 * 属性def22的Setter方法.属性名：自定义项22 创建日期:2020-3-25
	 * 
	 * @param newDef22
	 *            java.lang.String
	 */
	public void setDef22(java.lang.String def22) {
		this.def22 = def22;
	}

	/**
	 * 属性 def23的Getter方法.属性名：自定义项23 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef23() {
		return this.def23;
	}

	/**
	 * 属性def23的Setter方法.属性名：自定义项23 创建日期:2020-3-25
	 * 
	 * @param newDef23
	 *            java.lang.String
	 */
	public void setDef23(java.lang.String def23) {
		this.def23 = def23;
	}

	/**
	 * 属性 def24的Getter方法.属性名：自定义项24 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef24() {
		return this.def24;
	}

	/**
	 * 属性def24的Setter方法.属性名：自定义项24 创建日期:2020-3-25
	 * 
	 * @param newDef24
	 *            java.lang.String
	 */
	public void setDef24(java.lang.String def24) {
		this.def24 = def24;
	}

	/**
	 * 属性 def25的Getter方法.属性名：自定义项25 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef25() {
		return this.def25;
	}

	/**
	 * 属性def25的Setter方法.属性名：自定义项25 创建日期:2020-3-25
	 * 
	 * @param newDef25
	 *            java.lang.String
	 */
	public void setDef25(java.lang.String def25) {
		this.def25 = def25;
	}

	/**
	 * 属性 def26的Getter方法.属性名：自定义项26 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef26() {
		return this.def26;
	}

	/**
	 * 属性def26的Setter方法.属性名：自定义项26 创建日期:2020-3-25
	 * 
	 * @param newDef26
	 *            java.lang.String
	 */
	public void setDef26(java.lang.String def26) {
		this.def26 = def26;
	}

	/**
	 * 属性 def27的Getter方法.属性名：自定义项27 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef27() {
		return this.def27;
	}

	/**
	 * 属性def27的Setter方法.属性名：自定义项27 创建日期:2020-3-25
	 * 
	 * @param newDef27
	 *            java.lang.String
	 */
	public void setDef27(java.lang.String def27) {
		this.def27 = def27;
	}

	/**
	 * 属性 def28的Getter方法.属性名：自定义项28 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef28() {
		return this.def28;
	}

	/**
	 * 属性def28的Setter方法.属性名：自定义项28 创建日期:2020-3-25
	 * 
	 * @param newDef28
	 *            java.lang.String
	 */
	public void setDef28(java.lang.String def28) {
		this.def28 = def28;
	}

	/**
	 * 属性 def29的Getter方法.属性名：自定义项29 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef29() {
		return this.def29;
	}

	/**
	 * 属性def29的Setter方法.属性名：自定义项29 创建日期:2020-3-25
	 * 
	 * @param newDef29
	 *            java.lang.String
	 */
	public void setDef29(java.lang.String def29) {
		this.def29 = def29;
	}

	/**
	 * 属性 def30的Getter方法.属性名：自定义项30 创建日期:2020-3-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef30() {
		return this.def30;
	}

	/**
	 * 属性def30的Setter方法.属性名：自定义项30 创建日期:2020-3-25
	 * 
	 * @param newDef30
	 *            java.lang.String
	 */
	public void setDef30(java.lang.String def30) {
		this.def30 = def30;
	}

	/**
	 * 属性 生成时间戳的Getter方法.属性名：时间戳 创建日期:2020-3-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * 属性生成时间戳的Setter方法.属性名：时间戳 创建日期:2020-3-25
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		return "pk_defdoc";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "yer_contractfile";
	}
}