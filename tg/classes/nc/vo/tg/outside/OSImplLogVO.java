package nc.vo.tg.outside;

import nc.vo.pub.SuperVO;

/**
 * �ⲿϵͳ������־��
 * 
 * @author ASUS
 * 
 */
public class OSImplLogVO extends SuperVO {

	private String pk_log;// ��־����
	private String srcsystem;// ��Դϵͳ
	private String dessystem;// Ŀ��ϵͳ
	private String srcparm;// ��Դ����
	private String method;// ���÷���
	private String result;// ������
	private String msg;// ��Ϣ����
	private String exedate;// ִ��ʱ��
	private String operator;// ����Ա
	private Integer dr;
	private String ts;
	private String pk_relation;// ������

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
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2019-6-9
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
	 * ���ر�����
	 * <p>
	 * ��������:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "tg_interfacelog";
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "tg_interfacelog";
	}
}
