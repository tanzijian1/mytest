package nc.vo.tg.outside;

import nc.vo.pub.SuperVO;
/**
 * NC����SRM�ӿڻ�д��Ϣ��־VO
 * @author TZJ
 *
 */
public class NcToSrmLogVO extends SuperVO {

	public String pk_log;// ��־����
	public String srcsystem;// ��Դϵͳ
	public String taskname;// ��̨��������
	public String ncparm;// ����
	public String result;// ������
	public String msg; // ��Ϣ
	public String operator;// ����Ա
	public String exedate;// ִ��ʱ��
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
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_log";
	}

	/**
	 * <p>
	 * ���ر�����
	 * <p>
	 * ��������:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "nctosrmlog";
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2020-03-21
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "nctosrmlog";
	}
}
