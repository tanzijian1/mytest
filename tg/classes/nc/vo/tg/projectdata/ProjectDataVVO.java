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

public class ProjectDataVVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -481230544205715199L;
	public static final String PK_PROJECTDATA_V = "pk_projectdata_v";// 主键
	public static final String ISMAIN = "ismain";// 是否主推
	public static final String FINTYPE = "fintype";// 融资类型
	public static final String ORGANIZATION = "organization";// 融资机构
	public static final String FINMNY = "finmny";// 融资金额
	public static final String FINDETAILED = "findetailed";// 融资明细
	public static final String FINRATE = "finrate";// 融资利率
	public static final String PROCESS = "process";// 融资进度
	public static final String BRA_NCHDATE = "bra_nchdate";// 分行/分部上会时间
	public static final String PROVI_NCEDATE = "provi_ncedate";// 省行上会时间
	public static final String HEADQUARTERSDATE = "headquartersdate";// 总行/总部上会时间
	public static final String REPLYDATE = "replydate";// 批复时间
	public static final String COSTSIGNDATE = "costsigndate";// 合同签定时间
	public static final String PK_PROJECTDATA = "pk_projectdata";// 项目资料_主键
	public static final String DISABLE="disable";//是否停用
	public static final String ISDRAWESCHEME = "isdrawescheme";// 是否已提款
	public static final String REMARKSCHEDULE = "remarkschedule";// 是否已提款
	public static final String DR="dr";
	public static final String ZHRATE = "zhrate";// 拟融资综合利率
	private int dr;
	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}
	
	/**
	 * 拟融资综合利率
	 */
	public UFDouble zhrate;
	
	/**
	 * 是否停用
	 */
	public UFBoolean disable;
	/*
	 * 进度备注
	 */
	public String remarkschedule;

	/**
	 * 主键
	 */
	public String pk_projectdata_v;
	/**
	 * 是否主推
	 */
	public UFBoolean ismain;
	/**
	 * 融资类型
	 */
	public String fintype;
	/**
	 * 融资机构
	 */
	public String organization;
	/**
	 * 融资金额
	 */
	public UFDouble finmny;
	/**
	 * 融资明细
	 */
	public String findetailed;
	/**
	 * 融资利率
	 */
	public UFDouble finrate;
	/**
	 * 融资进度
	 */
	public String process;
	/**
	 * 分行/分部上会时间
	 */
	public String bra_nchdate;
	/**
	 * 省行上会时间
	 */
	public String provi_ncedate;
	/**
	 * 总行/总部上会时间
	 */
	public String headquartersdate;
	/**
	 * 批复时间
	 */
	public String replydate;
	/**
	 * 合同签定时间
	 */
	public String costsigndate;
	/**
	 * 上层单据主键
	 */
	public String pk_projectdata;
	/**
	 * 时间戳
	 */
	public UFDateTime ts;
	/*
	 * 是否已提款
	 */
	public UFBoolean isdrawescheme;
	/**
	 * 自定义项1
	 */
	public String vbdef1;
	/**
	 * 自定义项2
	 */
	public String vbdef2;
	/**
	 * 自定义项3
	 */
	public String vbdef3;
	/**
	 * 自定义项4
	 */
	public String vbdef4;
	/**
	 * 自定义项5
	 */
	public String vbdef5;
	/**
	 * 自定义项6
	 */
	public String vbdef6;
	/**
	 * 自定义项7
	 */
	public String vbdef7;
	/**
	 * 自定义项8
	 */
	public String vbdef8;
	/**
	 * 自定义项9
	 */
	public String vbdef9;
	/**
	 * 自定义项10
	 */
	public String vbdef10;
	
	
	public String getVbdef1() {
		return vbdef1;
	}

	public void setVbdef1(String vbdef1) {
		this.vbdef1 = vbdef1;
	}

	public String getVbdef2() {
		return vbdef2;
	}

	public void setVbdef2(String vbdef2) {
		this.vbdef2 = vbdef2;
	}

	public String getVbdef3() {
		return vbdef3;
	}

	public void setVbdef3(String vbdef3) {
		this.vbdef3 = vbdef3;
	}

	public String getVbdef4() {
		return vbdef4;
	}

	public void setVbdef4(String vbdef4) {
		this.vbdef4 = vbdef4;
	}

	public String getVbdef5() {
		return vbdef5;
	}

	public void setVbdef5(String vbdef5) {
		this.vbdef5 = vbdef5;
	}

	public String getVbdef6() {
		return vbdef6;
	}

	public void setVbdef6(String vbdef6) {
		this.vbdef6 = vbdef6;
	}

	public String getVbdef7() {
		return vbdef7;
	}

	public void setVbdef7(String vbdef7) {
		this.vbdef7 = vbdef7;
	}

	public String getVbdef8() {
		return vbdef8;
	}

	public void setVbdef8(String vbdef8) {
		this.vbdef8 = vbdef8;
	}

	public String getVbdef9() {
		return vbdef9;
	}

	public void setVbdef9(String vbdef9) {
		this.vbdef9 = vbdef9;
	}

	public String getVbdef10() {
		return vbdef10;
	}

	public void setVbdef10(String vbdef10) {
		this.vbdef10 = vbdef10;
	}

	/**
	 * 属性 zhrate的Getter方法.属性名：拟融资综合利率 创建日期:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public UFDouble getZhrate() {
		return this.zhrate;
	}

	/**
	 * 属性zhrate的Setter方法.属性名：拟融资综合利率 创建日期:2019-6-23
	 * 
	 * @param newZhrate
	 *            java.lang.String
	 */
	public void setZhrate(UFDouble zhrate) {
		this.zhrate = zhrate;
	}
	
	
	public String getRemarkschedule() {
		return remarkschedule;
	}

	public void setRemarkschedule(String remarkschedule) {
		this.remarkschedule = remarkschedule;
	}

	/**
	 * 属性 disable的Getter方法.属性名：融资方案表头主键 创建日期:2019-6-23
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */

	public UFBoolean getDisable() {
		return disable;
	}

	public void setDisable(UFBoolean disable) {
		this.disable = disable;
	}
	
	/**
	 * 属性 isdrawescheme的Getter方法.属性名：融资方案表体是否已提款 创建日期:2019-08-29
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
	 * 属性 pk_projectdata_v的Getter方法.属性名：主键 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPk_projectdata_v() {
		return this.pk_projectdata_v;
	}

	/**
	 * 属性pk_projectdata_v的Setter方法.属性名：主键 创建日期:2019-6-29
	 * 
	 * @param newPk_projectdata_v
	 *            java.lang.String
	 */
	public void setPk_projectdata_v(String pk_projectdata_v) {
		this.pk_projectdata_v = pk_projectdata_v;
	}

	/**
	 * 属性 ismain的Getter方法.属性名：是否主推 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getIsmain() {
		return this.ismain;
	}

	/**
	 * 属性ismain的Setter方法.属性名：是否主推 创建日期:2019-6-29
	 * 
	 * @param newIsmain
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setIsmain(UFBoolean ismain) {
		this.ismain = ismain;
	}

	/**
	 * 属性 fintype的Getter方法.属性名：融资类型 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getFintype() {
		return this.fintype;
	}

	/**
	 * 属性fintype的Setter方法.属性名：融资类型 创建日期:2019-6-29
	 * 
	 * @param newFintype
	 *            java.lang.String
	 */
	public void setFintype(String fintype) {
		this.fintype = fintype;
	}

	/**
	 * 属性 organization的Getter方法.属性名：融资机构 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getOrganization() {
		return this.organization;
	}

	/**
	 * 属性organization的Setter方法.属性名：融资机构 创建日期:2019-6-29
	 * 
	 * @param newOrganization
	 *            java.lang.String
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * 属性 finmny的Getter方法.属性名：融资金额 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public UFDouble getFinmny() {
		return this.finmny;
	}

	/**
	 * 属性finmny的Setter方法.属性名：融资金额 创建日期:2019-6-29
	 * 
	 * @param newFinmny
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setFinmny(UFDouble finmny) {
		this.finmny = finmny;
	}

	/**
	 * 属性 findetailed的Getter方法.属性名：融资明细 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getFindetailed() {
		return this.findetailed;
	}

	/**
	 * 属性findetailed的Setter方法.属性名：融资明细 创建日期:2019-6-29
	 * 
	 * @param newFindetailed
	 *            java.lang.String
	 */
	public void setFindetailed(String findetailed) {
		this.findetailed = findetailed;
	}

	/**
	 * 属性 finrate的Getter方法.属性名：融资利率 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public UFDouble getFinrate() {
		return this.finrate;
	}

	/**
	 * 属性finrate的Setter方法.属性名：融资利率 创建日期:2019-6-29
	 * 
	 * @param newFinrate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setFinrate(UFDouble finrate) {
		this.finrate = finrate;
	}

	/**
	 * 属性 process的Getter方法.属性名：融资进度 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getProcess() {
		return this.process;
	}

	/**
	 * 属性process的Setter方法.属性名：融资进度 创建日期:2019-6-29
	 * 
	 * @param newProcess
	 *            java.lang.String
	 */
	public void setProcess(String process) {
		this.process = process;
	}

	/**
	 * 属性 bra_nchdate的Getter方法.属性名：分行/分部上会时间 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getBra_nchdate() {
		return this.bra_nchdate;
	}

	/**
	 * 属性bra_nchdate的Setter方法.属性名：分行/分部上会时间 创建日期:2019-6-29
	 * 
	 * @param newBra_nchdate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setBra_nchdate(String bra_nchdate) {
		this.bra_nchdate = bra_nchdate;
	}

	/**
	 * 属性 provi_ncedate的Getter方法.属性名：省行上会时间 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getProvi_ncedate() {
		return this.provi_ncedate;
	}

	/**
	 * 属性provi_ncedate的Setter方法.属性名：省行上会时间 创建日期:2019-6-29
	 * 
	 * @param newProvi_ncedate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setProvi_ncedate(String provi_ncedate) {
		this.provi_ncedate = provi_ncedate;
	}

	/**
	 * 属性 headquartersdate的Getter方法.属性名：总行/总部上会时间 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getHeadquartersdate() {
		return this.headquartersdate;
	}

	/**
	 * 属性headquartersdate的Setter方法.属性名：总行/总部上会时间 创建日期:2019-6-29
	 * 
	 * @param newHeadquartersdate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setHeadquartersdate(String headquartersdate) {
		this.headquartersdate = headquartersdate;
	}

	/**
	 * 属性 replydate的Getter方法.属性名：批复时间 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getReplydate() {
		return this.replydate;
	}

	/**
	 * 属性replydate的Setter方法.属性名：批复时间 创建日期:2019-6-29
	 * 
	 * @param newReplydate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setReplydate(String replydate) {
		this.replydate = replydate;
	}

	/**
	 * 属性 costsigndate的Getter方法.属性名：合同签定时间 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getCostsigndate() {
		return this.costsigndate;
	}

	/**
	 * 属性costsigndate的Setter方法.属性名：合同签定时间 创建日期:2019-6-29
	 * 
	 * @param newCostsigndate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setCostsigndate(String costsigndate) {
		this.costsigndate = costsigndate;
	}

	/**
	 * 属性 生成上层主键的Getter方法.属性名：上层主键 创建日期:2019-6-29
	 * 
	 * @return String
	 */
	public String getPk_projectdata() {
		return this.pk_projectdata;
	}

	/**
	 * 属性生成上层主键的Setter方法.属性名：上层主键 创建日期:2019-6-29
	 * 
	 * @param newPk_projectdata
	 *            String
	 */
	public void setPk_projectdata(String pk_projectdata) {
		this.pk_projectdata = pk_projectdata;
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
		return VOMetaFactory.getInstance().getVOMeta("tg.ProjectDataVVO");
	}
}
