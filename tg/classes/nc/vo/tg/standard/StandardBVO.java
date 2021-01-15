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

public class StandardBVO extends SuperVO {
	public static final String PK_STANDARD_B = "pk_standard_b";// 主键
	public static final String CROWNO = "crowno";// 行号
	public static final String DAYS = "days";// 自然日
	public static final String RATIO1 = "ratio1";// 占比
	public static final String RATIO2 = "ratio2";// 贷款总额占比
	public static final String NOTE = "note";// 备注
	public static final String PK_STANDARD = "pk_standard";// 融资标准_主键

	/**
	 * 上层单据主键
	 */
	public String pk_standard;
	/**
	 * 时间戳
	 */
	public UFDateTime ts;

	/**
	 * 属性 生成上层主键的Getter方法.属性名：上层主键 创建日期:2019-8-22
	 * 
	 * @return String
	 */
	public String getPk_standard() {
		return this.pk_standard;
	}

	/**
	 * 属性生成上层主键的Setter方法.属性名：上层主键 创建日期:2019-8-22
	 * 
	 * @param newPk_standard
	 *            String
	 */
	public void setPk_standard(String pk_standard) {
		this.pk_standard = pk_standard;
	}

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
		return VOMetaFactory.getInstance().getVOMeta("tg.StandardBVO");
	}
}
