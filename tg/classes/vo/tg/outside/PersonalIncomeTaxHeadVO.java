package vo.tg.outside;

import java.io.Serializable;

/**
 * ��ϵͳ���
 * 
 * @author ��ΰ
 * 
 */
public class PersonalIncomeTaxHeadVO implements Serializable {
	private String srcid;// ��ϵͳ��Դ����id
	private String srcbillno;// ��ϵͳ��Դ���ݱ��
	private String pk_org; // ���ʷ��Ź�˾
	private String pk_balatype; // ���㷽ʽ
	private String billdate; // �ۿ�����
	private String def67; // ����������

	public String getSrcid() {
		return srcid;
	}

	public void setSrcid(String srcid) {
		this.srcid = srcid;
	}

	public String getSrcbillno() {
		return srcbillno;
	}

	public void setSrcbillno(String srcbillno) {
		this.srcbillno = srcbillno;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPk_balatype() {
		return pk_balatype;
	}

	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}

	public String getDef67() {
		return def67;
	}

	public void setDef67(String def67) {
		this.def67 = def67;
	}

}
