package nc.vo.tg.outside;

import nc.vo.pub.SuperVO;
/**
 * NC调用SRM接口回写信息日志VO
 * @author TZJ
 *
 */
public class NcToSrmLogVO extends SuperVO {

	public String pk_log;// 日志主键
	public String srcsystem;// 来源系统
	public String taskname;// 后台任务名称
	public String ncparm;// 参数
	public String result;// 处理结果
	public String msg; // 信息
	public String operator;// 操作员
	public String exedate;// 执行时间
	public Integer dr;
	public String ts;

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getNcparm() {
		return ncparm;
	}

	public void setNcparm(String ncparm) {
		this.ncparm = ncparm;
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
		return "nctosrmlog";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2020-03-21
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "nctosrmlog";
	}
}
