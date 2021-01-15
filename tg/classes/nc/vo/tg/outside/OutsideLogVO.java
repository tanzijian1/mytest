package nc.vo.tg.outside;

import nc.vo.pub.SuperVO;

public class OutsideLogVO extends SuperVO {

	public String pk_log;// ��־����
	public String srcsystem;// ��Դϵͳ
	public String srcparm;// ��Դ����
	public String desbill;// Ŀ�굥��
	public String result;// ������
	public String errmsg;// ������
	public String operator;// ����Ա
	public String exedate;// ִ��ʱ��
	public String primaryKey;// ��������
	public String isOperInNC;// �Ƿ���NC�в����������ж��ǽӿ��쳣����ɾ������NCǰ̨����

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
		return "tg_outsidelog";
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
		return "tg_outsidelog";
	}
}
