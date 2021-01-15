package nc.vo.tg.standard;

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
 * 创建日期:2019-8-22
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class StandardVO extends SuperVO {
	public static final String PK_STANDARD = "pk_standard";// 主键
	public static final String PK_GROUP = "pk_group";// 集团
	public static final String PK_ORG = "pk_org";// 组织
	public static final String PK_ORG_V = "pk_org_v";// 组织版本
	public static final String CODE = "code";// 编码
	public static final String NAME = "name";// 名称
	public static final String PERIODYEAR = "periodyear";// 时间维度
	public static final String PK_FINTYPE = "pk_fintype";// 融资类型
	public static final String VSTANDARD = "vstandard";// 融资标准
	public static final String ENABLESTATE = "enablestate";// 启用状态
	public static final String CREATOR = "creator";// 创建人
	public static final String CREATIONTIME = "creationtime";// 创建时间
	public static final String MODIFIER = "modifier";// 修改人
	public static final String MODIFIEDTIME = "modifiedtime";// 修改时间
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
	public static final String BILLDATE = "billdate";// 业务日期

	/**
	 * 时间戳
	 */
	public UFDateTime ts;

	/**
	 * 属性 生成时间戳的Getter方法.属性名：时间戳 创建日期:2019-8-22
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * 属性生成时间戳的Setter方法.属性名：时间戳 创建日期:2019-8-22
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.StandardVO");
	}
}
