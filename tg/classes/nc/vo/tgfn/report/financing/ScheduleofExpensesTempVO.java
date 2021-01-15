package nc.vo.tgfn.report.financing;

import nc.vo.pub.SuperVO;
/**
 * 费用科目明细表临时表,用于存在业务单据PK和单据类型
 * @author ln
 *
 */
public class ScheduleofExpensesTempVO extends SuperVO {

	private static final long serialVersionUID = -7168898004632115321L;
	private String pk_schedule;
	/** 业务单据PK */
	private String billskey;
	/** 单据类型 */
	private String billtype;
	
	public String getPk_schedule() {
		return pk_schedule;
	}

	public void setPk_schedule(String pk_schedule) {
		this.pk_schedule = pk_schedule;
	}

	public String getBillskey() {
		return billskey;
	}

	public void setBillskey(String billskey) {
		this.billskey = billskey;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
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

		return "temp_tg_schedule";
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
		return "pk_schedule";
	}
}
