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

public class ProjectDataCVO extends SuperVO {
	public static final String PK_PROJECTDATA_C = "pk_projectdata_c";// 主键
	public static final String PERIODIZATIONNAME = "periodizationname";// 分期名称
	public static final String P6_DATADATE1 = "p6_datadate1";// 项目获取时间_p6
	public static final String NC_DATADATE1 = "nc_datadate1";// 项目获取时间_nc
	public static final String P6_DATADATE2 = "p6_datadate2";// 运营启动点达到_p6
	public static final String NC_DATADATE2 = "nc_datadate2";// 运营启动点达到_nc
	public static final String P6_DATADATE3 = "p6_datadate3";// 修详规批复_p6
	public static final String NC_DATADATE3 = "nc_datadate3";// 修详规批复_nc
	public static final String P6_DATADATE4 = "p6_datadate4";// 国有土地使用证_p6
	public static final String NC_DATADATE4 = "nc_datadate4";// 国有土地使用证_nc
	public static final String P6_DATADATE5 = "p6_datadate5";// 用地规划许可证_p6
	public static final String NC_DATADATE5 = "nc_datadate5";// 用地规划许可证_nc
	public static final String P6_DATADATE6 = "p6_datadate6";// 建设工程规划许可证_p6
	public static final String NC_DATADATE6 = "nc_datadate6";// 建设工程规划许可证_nc
	public static final String P6_DATADATE7 = "p6_datadate7";// 施工许可证_p6
	public static final String NC_DATADATE7 = "nc_datadate7";// 施工许可证_nc
	public static final String P6_DATADATE8 = "p6_datadate8";// 开工时间_p6
	public static final String NC_DATADATE8 = "nc_datadate8";// 开工时间_nc
	public static final String P6_DATADATE9 = "p6_datadate9";// 正负零_p6
	public static final String NC_DATADATE9 = "nc_datadate9";// 正负零_nc
	public static final String P6_DATADATE10 = "p6_datadate10";// 预售证_p6
	public static final String NC_DATADATE10 = "nc_datadate10";// 预售证_nc
	public static final String P6_DATADATE11 = "p6_datadate11";// 结构封顶_p6
	public static final String NC_DATADATE11 = "nc_datadate11";// 结构封顶_nc
	public static final String P6_DATADATE12 = "p6_datadate12";// 竣工备案_p6
	public static final String NC_DATADATE12 = "nc_datadate12";// 竣工备案_nc
	public static final String P6_DATADATE13 = "p6_datadate13";// 交付_p6
	public static final String NC_DATADATE13 = "nc_datadate13";// 交付_nc
	public static final String P6_DATADATE14 = "p6_datadate14";// 确权_p6
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
	public static final String PK_PROJECTDATA = "pk_projectdata";// 项目资料_主键

	/**
	 * 主键
	 */
	public String pk_projectdata_c;
	/**
	 * 分期名称
	 */
	public String periodizationname;
	/**
	 * 项目获取时间_p6
	 */
	public UFDate p6_datadate1;
	/**
	 * 项目获取时间_nc
	 */
	public UFDate nc_datadate1;
	/**
	 * 运营启动点达到_p6
	 */
	public UFDate p6_datadate2;
	/**
	 * 运营启动点达到_nc
	 */
	public UFDate nc_datadate2;
	/**
	 * 修详规批复_p6
	 */
	public UFDate p6_datadate3;
	/**
	 * 修详规批复_nc
	 */
	public UFDate nc_datadate3;
	/**
	 * 国有土地使用证_p6
	 */
	public UFDate p6_datadate4;
	/**
	 * 国有土地使用证_nc
	 */
	public UFDate nc_datadate4;
	/**
	 * 用地规划许可证_p6
	 */
	public UFDate p6_datadate5;
	/**
	 * 用地规划许可证_nc
	 */
	public UFDate nc_datadate5;
	/**
	 * 建设工程规划许可证_p6
	 */
	public UFDate p6_datadate6;
	/**
	 * 建设工程规划许可证_nc
	 */
	public UFDate nc_datadate6;
	/**
	 * 施工许可证_p6
	 */
	public UFDate p6_datadate7;
	/**
	 * 施工许可证_nc
	 */
	public UFDate nc_datadate7;
	/**
	 * 开工时间_p6
	 */
	public UFDate p6_datadate8;
	/**
	 * 开工时间_nc
	 */
	public UFDate nc_datadate8;
	/**
	 * 正负零_p6
	 */
	public UFDate p6_datadate9;
	/**
	 * 正负零_nc
	 */
	public UFDate nc_datadate9;
	/**
	 * 预售证_p6
	 */
	public UFDate p6_datadate10;
	/**
	 * 预售证_nc
	 */
	public UFDate nc_datadate10;
	/**
	 * 结构封顶_p6
	 */
	public UFDate p6_datadate11;
	/**
	 * 结构封顶_nc
	 */
	public UFDate nc_datadate11;
	/**
	 * 竣工备案_p6
	 */
	public UFDate p6_datadate12;
	/**
	 * 竣工备案_nc
	 */
	public UFDate nc_datadate12;
	/**
	 * 交付_p6
	 */
	public UFDate p6_datadate13;
	/**
	 * 交付_nc
	 */
	public UFDate nc_datadate13;
	/**
	 * 确权_p6
	 */
	public UFDate p6_datadate14;
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
	 * 上层单据主键
	 */
	public String pk_projectdata;
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
	 * 属性 pk_projectdata_c的Getter方法.属性名：主键 创建日期:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPk_projectdata_c() {
		return this.pk_projectdata_c;
	}

	/**
	 * 属性pk_projectdata_c的Setter方法.属性名：主键 创建日期:2019-6-29
	 * 
	 * @param newPk_projectdata_c
	 *            java.lang.String
	 */
	public void setPk_projectdata_c(String pk_projectdata_c) {
		this.pk_projectdata_c = pk_projectdata_c;
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
		return VOMetaFactory.getInstance().getVOMeta("tg.ProjectDataCVO");
	}
}
