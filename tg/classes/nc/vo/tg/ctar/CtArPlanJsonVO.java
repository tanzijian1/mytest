package nc.vo.tg.ctar;

import java.io.Serializable;


/**
 * <b> �տ�ƻ�VO </b>
 * @author xucp3
 * 
 */
public class CtArPlanJsonVO implements Serializable{



	// �ƻ�����
	private float planrate;


	// ��������
	private Integer accountdate;

	// �ƻ�������
	private String enddate;

	// �ƻ����
	private float planmoney;

	//��Ʊ���
	private float norikpmny;
	
	// ��Ŀ����
		private String vbdef1;

		// ��ֱ���
		private String vbdef2;
		
		// �Զ�����3
		private String vbdef3;

		// �Զ�����4
		private String vbdef4;

		// �Զ�����5
		private String vbdef5;

		// �Զ�����6
		private String vbdef6;

		// �Զ�����7
		private String vbdef7;

		// �Զ�����8
		private String vbdef8;

		// �Զ�����9
		private String vbdef9;
		
		// �Զ�����10
		private String vbdef10;
	
	

	public static final String ACCOUNTNUM = "accountnum";
	public static final String PLANRATE = "planrate";
	public static final String BEGINDATE = "begindate";
	public static final String ACCOUNTDATE = "accountdate";
	public static final String ENDDATE = "enddate";
	public static final String PLANMONEY = "planmoney";
	public static final String BALANCEMONEY = "balancemoney";
	public static final String MONEYTYPE = "moneytype";
	public static final String PK_FINANCEORG = "pk_financeorg";
	public static final String PK_FINANCEORG_V = "pk_financeorg_v";
	public static final String NORIKPMNY = "norikpmny";

	/**
   * 
   */
	private static final long serialVersionUID = -8384924725828916172L;

	/**
	 * ����Ĭ�Ϸ�ʽ����������. ��������:2010-03-18 19:43:57
	 */
	public CtArPlanJsonVO() {
		super();
	}



	public float getPlanrate() {
		return planrate;
	}

	public void setPlanrate(float planrate) {
		this.planrate = planrate;
	}


	public Integer getAccountdate() {
		return accountdate;
	}

	public void setAccountdate(Integer accountdate) {
		this.accountdate = accountdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public float getPlanmoney() {
		return planmoney;
	}

	public void setPlanmoney(float planmoney) {
		this.planmoney = planmoney;
	}


	public void setNorikpmny(float norikpmny) {
		this.norikpmny = norikpmny;
	}

	public float getNorikpmny() {
		return norikpmny;
	}
	public String getVbdef1() {
		return vbdef1;
	}

	public void setVbdef1(String vbdef1) {
		this.vbdef1 = vbdef1;
	}

	public String getVbdef2() {
		return vbdef2;
	}

	public void setVbdef2(String vbdef2) {
		this.vbdef2 = vbdef2;
	}

	public String getVbdef3() {
		return vbdef3;
	}

	public void setVbdef3(String vbdef3) {
		this.vbdef3 = vbdef3;
	}

	public String getVbdef4() {
		return vbdef4;
	}

	public void setVbdef4(String vbdef4) {
		this.vbdef4 = vbdef4;
	}

	public String getVbdef5() {
		return vbdef5;
	}

	public void setVbdef5(String vbdef5) {
		this.vbdef5 = vbdef5;
	}

	public String getVbdef6() {
		return vbdef6;
	}

	public void setVbdef6(String vbdef6) {
		this.vbdef6 = vbdef6;
	}

	public String getVbdef7() {
		return vbdef7;
	}

	public void setVbdef7(String vbdef7) {
		this.vbdef7 = vbdef7;
	}

	public String getVbdef8() {
		return vbdef8;
	}

	public void setVbdef8(String vbdef8) {
		this.vbdef8 = vbdef8;
	}

	public String getVbdef9() {
		return vbdef9;
	}

	public void setVbdef9(String vbdef9) {
		this.vbdef9 = vbdef9;
	}

	public String getVbdef10() {
		return vbdef10;
	}

	public void setVbdef10(String vbdef10) {
		this.vbdef10 = vbdef10;
	}

}
