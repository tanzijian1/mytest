package nc.vo.tg.projectbatchlog;

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
 * 创建日期:2020-3-30
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class ProjectBatchLogDetailVO extends SuperVO {

	/**
	 * 上层单据主键
	 */
	public static final String PK_PROJECTBATCHLOG = "pk_projectbatchlog";
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";;

	/**
	 * 属性 生成上层主键的Getter方法.属性名：上层主键 创建日期:2020-3-30
	 * 
	 * @return String
	 */
	public String getPk_projectbatchlog() {
		return (String) this
				.getAttributeValue(ProjectBatchLogDetailVO.PK_PROJECTBATCHLOG);
	}

	/**
	 * 属性生成上层主键的Setter方法.属性名：上层主键 创建日期:2020-3-30
	 * 
	 * @param newPk_projectbatchlog
	 *            String
	 */
	public void setPk_projectbatchlog(String pk_projectbatchlog) {
		this.setAttributeValue(ProjectBatchLogDetailVO.PK_PROJECTBATCHLOG,
				pk_projectbatchlog);
	}

	/**
	 * 属性 生成时间戳的Getter方法.属性名：时间戳 创建日期:2020-3-30
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(ProjectBatchLogDetailVO.TS);
	}

	/**
	 * 属性生成时间戳的Setter方法.属性名：时间戳 创建日期:2020-3-30
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(ProjectBatchLogDetailVO.TS, ts);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta(
				"tg.ProjectBatchLogDetailVO");
	}
}
