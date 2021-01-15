package nc.vo.tgfn.report.financing;

import nc.vo.pub.SuperVO;
/**
 * ���ÿ�Ŀ��ϸ����ʱ��,���ڴ���ҵ�񵥾�PK�͵�������
 * @author ln
 *
 */
public class ScheduleofExpensesTempVO extends SuperVO {

	private static final long serialVersionUID = -7168898004632115321L;
	private String pk_schedule;
	/** ҵ�񵥾�PK */
	private String billskey;
	/** �������� */
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
	 * ���ر�����.
	 * <p>
	 * ��������:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "temp_tg_schedule";
	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_schedule";
	}
}
