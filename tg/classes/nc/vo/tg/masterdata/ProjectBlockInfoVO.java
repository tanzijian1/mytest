package nc.vo.tg.masterdata;

import nc.vo.pub.SuperVO;

/**
 * @author ASUS
 *��Ŀ������Ϣ�ӿ�VO 2020-08-24 LJF
 */
@SuppressWarnings("serial")
public class ProjectBlockInfoVO extends SuperVO {
	/**������ĿID��������WBD_Project_T*/
	private String fprjid ;
	/**��Ŀ����ID*/
	private String id ;
	/**���ڱ���*/
	private String fnumber;
	/**��������*/
	private String fname;
	/**��Ʒϵ�У����룺WBD_ProductSeries*/
	private String fproductseries;
	/**˵��*/
	private String fdescription;
	/**���ڱ�����*/
	private String fremark;

	public String getFprjid() {
		return fprjid;
	}
	public void setFprjid(String fprjid) {
		this.fprjid = fprjid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getFproductseries() {
		return fproductseries;
	}
	public void setFproductseries(String fproductseries) {
		this.fproductseries = fproductseries;
	}
	public String getFdescription() {
		return fdescription;
	}
	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}
	public String getFremark() {
		return fremark;
	}
	public void setFremark(String fremark) {
		this.fremark = fremark;
	}
	
}
