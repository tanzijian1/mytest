package nc.vo.tg.outside;

import nc.vo.pub.SuperVO;

/**
 * 外部系统交互日志表
 * 
 * @author ASUS
 * 
 */
public class OSImplLogVO extends SuperVO {

	private String pk_log;// 日志主键
	private String srcsystem;// 来源系统
	private String dessystem;// 目标系统
	private String srcparm;// 来源参数
	private String method;// 调用方法
	private String result;// 处理结果
	private String msg;// 信息反馈
	private String exedate;// 执行时间
	private String operator;// 操作员
	private Integer dr;
	private String ts;
	private String pk_relation;// 关联字

	public String getPk_relation() {
		return pk_relation;
	}

	public void setPk_relation(String pk_relation) {
		this.pk_relation = pk_relation;
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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	public String getDessystem() {
		return dessystem;
	}

	public void setDessystem(String dessystem) {
		this.dessystem = dessystem;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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
		return "tg_interfacelog";
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
		return "tg_interfacelog";
	}
}
