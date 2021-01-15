package nc.vo.tgfn.report.cost;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 中长期项目成本台账（成本报表）
 * 
 * ljf 2020_05_21 成本台账报表
 * 
 * @author ASUS
 * 
 */
public class MinAndLongProjectCostLedgerVO extends SuperVO implements Cloneable {

	private String pk_costleder;
	private String pk_fct_ap;// 合同主键
	private String pk_project;// 项目名称
	private String innercode;// 内编码
	private String projectname;
	private String vclass;// 大类
	private String contcode;// 合同编码
	private String contname;// 合同名称
	private String tatalcontcode;// 总包合同编码
	private String tatalcontname;// 总包合同名称
	private String conttype;// 合同类别
	private String contattribute;// 合同属性
	private String pk_first;// 甲方单位
	private String firstname;
	private String pk_second;// 乙方单位
	private String secondname;
	private String signdate;// 签约日期
	private String settstate;// 结算状态
	private UFDouble amount1;// 签约金额
	private UFDouble amount2;// 累计变更金额
	private UFDouble amount3;// 累计补充协议金额
	private UFDouble amount4;// 结算调整金额
	private UFDouble amount5;// 动态金额（含税）
	private UFDouble rate1;// 税率
	private UFDouble amount6;// 动态金额（不含税）
	private UFDouble amount7;// 动态金额（税额）
	private UFDouble amount8;// 结算金额
	private UFDouble amount9;// 累计应付款
	private UFDouble amount10;// 累计应付未付款
	private UFDouble amount11;// 累计实付款（含税）
	private UFDouble amount12;// 累计实付款（不含税）
	private UFDouble amount13;//
	private UFDouble amount14;// 累计实付款（税额）
	private UFDouble amount15;// ?合同未付款（含税）
	private UFDouble amount16;// ?合同未付款（不含税）
	private UFDouble amount17;// 合同未付款 （税额）
	private UFDouble amount18;//
	private UFDouble amount19;// 累计实付款-NC
	private UFDouble amount20;// 累计实付款-NC（不含税）
	private UFDouble amount21;// 累计实付款-NC（税额）
	private UFDouble amount22;// 保理付款金额
	private UFDouble amount23;// nc入账发票金额（含税）
	private UFDouble amount24;// 已入成本（不含税）
	private UFDouble amount25;// 可抵扣进项税（税额）
	private UFDouble amount26;// 未入成本（含税）
	private UFDouble amount27;// 未入成本（不含税）
	private UFDouble amount28;// 未入成本（税额）
	private UFDouble amount29;// 累计产值（含税）
	private UFDouble amount30;// 累计产值（不含税）
	private UFDouble amount31;// 累计产值（税额）
	private UFDouble amount32;// 最终产值
	private UFDouble amount33;// 累计 产值未付款 （含税）
	private UFDouble amount34;// 累计产值未付款 （不含税）
	private UFDouble amount35;// 累计产值未付款 （税额）
	private String pk_dept;// 部门
	private String pk_psndoc;// 经办人
	private String pk_mode;// 承包方式
	private String modename;// 承包方式
	private String pk_format;// 业态
	private String formatname;// 业态名称
	private UFDouble rate2;// 业态比例
	
	//add by 黄冠华  添加融资费用和开发间接费的余额数据 20200813 begin
	private UFDouble amt36;// 开发间接费
	private UFDouble amt37;// 融资费用
	//add by 黄冠华  添加融资费用和开发间接费的余额数据 20200813 end

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
	 * 返回表名称.
	 * <p>
	 * 创建日期:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "temp_tg_costleder";
	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_costleder";
	}
}
