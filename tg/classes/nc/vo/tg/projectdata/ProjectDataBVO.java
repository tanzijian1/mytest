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

public class ProjectDataBVO extends SuperVO {
	public static final String PK_PROJECTDATA_B = "pk_projectdata_b";// 主键
	public static final String PAYDATE = "paydate";// 地价/股权支付时间
	public static final String PAYMNY = "paymny";// 金额
	public static final String PK_PROJECTDATA = "pk_projectdata";// 项目资料_主键
	public static final String DEF1 = "DEF1";//
	public static final String DEF2 = "DEF2";
	public static final String DEF3 = "DEF3";
	public static final String DEF4 = "DEF4";
	public static final String DEF5 = "DEF5";
	/**
	 * 主键
	 */
	public String pk_projectdata_b;
	/**
	 * 地价/股权支付时间
	 */
	public UFDate paydate;
	/**
	 * 金额
	 */
	public UFDouble paymny;
	/**
	 * 上层单据主键
	 */
	public String pk_projectdata;
	/**
	 * 时间戳
	 */
	/**
	 * 自定义项1
	 */
	public String def1;
	/**
	 *自定义项2
	 */
	public String def2;
	/**
	 * 自定义项3
	 */
	public String def3;
	/**
	 *自定义项4
	 */
	public String def4;
	/**
	 * 自定义项5
	 */
	public String def5;
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
	 * 属性 pk_projectdata_b的Getter方法.属性名：主键 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPk_projectdata_b() {
		return this.pk_projectdata_b;
	}

	/**
	 * 属性pk_projectdata_b的Setter方法.属性名：主键 创建日期:2019-6-29
	 * 
	 * @param newPk_projectdata_b
	 *            java.lang.String
	 */
	public void setPk_projectdata_b(String pk_projectdata_b) {
		this.pk_projectdata_b = pk_projectdata_b;
	}

	/**
	 * 属性 paydate的Getter方法.属性名：地价/股权支付时间 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getPaydate() {
		return this.paydate;
	}

	/**
	 * 属性paydate的Setter方法.属性名：地价/股权支付时间 创建日期:2019-6-29
	 * 
	 * @param newPaydate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setPaydate(UFDate paydate) {
		this.paydate = paydate;
	}

	/**
	 * 属性 paymny的Getter方法.属性名：金额 创建日期:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public UFDouble getPaymny() {
		return this.paymny;
	}

	/**
	 * 属性paymny的Setter方法.属性名：金额 创建日期:2019-6-29
	 * 
	 * @param newPaymny
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPaymny(UFDouble paymny) {
		this.paymny = paymny;
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
	


	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
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
		return VOMetaFactory.getInstance().getVOMeta("tg.ProjectDataBVO");
	}
}
