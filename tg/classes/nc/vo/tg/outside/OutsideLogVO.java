package nc.vo.tg.outside;

import nc.vo.pub.SuperVO;

public class OutsideLogVO extends SuperVO {

	public String pk_log;// 日志主键
	public String srcsystem;// 来源系统
	public String srcparm;// 来源参数
	public String desbill;// 目标单据
	public String result;// 处理结果
	public String errmsg;// 错误反馈
	public String operator;// 操作员
	public String exedate;// 执行时间
	public String primaryKey;// 单据主键
	public String isOperInNC;// 是否在NC中操作，用于判断是接口异常调用删除还是NC前台调用

	public String getIsOperInNC() {
		return isOperInNC;
	}

	public void setIsOperInNC(String isOperInNC) {
		this.isOperInNC = isOperInNC;
	}

	public Integer dr;
	public String ts;

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getPk_log() {
		return pk_log;
	}

	public void setPk_log(String pk_log) {
		this.pk_log = pk_log;
	}

	public String getSrcsystem() {
		return srcsystem;
	}

	public void setSrcsystem(String srcsystem) {
		this.srcsystem = srcsystem;
	}

	public String getSrcparm() {
		return srcparm;
	}

	public void setSrcparm(String srcparm) {
		this.srcparm = srcparm;
	}

	public String getDesbill() {
		return desbill;
	}

	public void setDesbill(String desbill) {
		this.desbill = desbill;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getExedate() {
		return exedate;
	}

	public void setExedate(String exedate) {
		this.exedate = exedate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_log";
	}

	/**
	 * <p>
	 * 返回表名称
	 * <p>
	 * 创建日期:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "tg_outsidelog";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "tg_outsidelog";
	}
}
