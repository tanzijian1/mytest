package nc.vo.tg.ctar;

import java.io.Serializable;


/**
 * <b> 收款计划VO </b>
 * @author xucp3
 * 
 */
public class CtArPlanJsonVO implements Serializable{



	// 计划比例
	private float planrate;


	// 账期天数
	private Integer accountdate;

	// 计划到期日
	private String enddate;

	// 计划金额
	private float planmoney;

	//开票金额
	private float norikpmny;
	
	// 科目名称
		private String vbdef1;

		// 拆分比例
		private String vbdef2;
		
		// 自定义项3
		private String vbdef3;

		// 自定义项4
		private String vbdef4;

		// 自定义项5
		private String vbdef5;

		// 自定义项6
		private String vbdef6;

		// 自定义项7
		private String vbdef7;

		// 自定义项8
		private String vbdef8;

		// 自定义项9
		private String vbdef9;
		
		// 自定义项10
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
	 * 按照默认方式创建构造子. 创建日期:2010-03-18 19:43:57
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
