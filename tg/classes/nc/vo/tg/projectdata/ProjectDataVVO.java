package nc.vo.tg.projectdata;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 * �˴�����۵�������Ϣ
 * </p>
 * ��������:2019-6-29
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class ProjectDataVVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -481230544205715199L;
	public static final String PK_PROJECTDATA_V = "pk_projectdata_v";// ����
	public static final String ISMAIN = "ismain";// �Ƿ�����
	public static final String FINTYPE = "fintype";// ��������
	public static final String ORGANIZATION = "organization";// ���ʻ���
	public static final String FINMNY = "finmny";// ���ʽ��
	public static final String FINDETAILED = "findetailed";// ������ϸ
	public static final String FINRATE = "finrate";// ��������
	public static final String PROCESS = "process";// ���ʽ���
	public static final String BRA_NCHDATE = "bra_nchdate";// ����/�ֲ��ϻ�ʱ��
	public static final String PROVI_NCEDATE = "provi_ncedate";// ʡ���ϻ�ʱ��
	public static final String HEADQUARTERSDATE = "headquartersdate";// ����/�ܲ��ϻ�ʱ��
	public static final String REPLYDATE = "replydate";// ����ʱ��
	public static final String COSTSIGNDATE = "costsigndate";// ��ͬǩ��ʱ��
	public static final String PK_PROJECTDATA = "pk_projectdata";// ��Ŀ����_����
	public static final String DISABLE="disable";//�Ƿ�ͣ��
	public static final String ISDRAWESCHEME = "isdrawescheme";// �Ƿ������
	public static final String REMARKSCHEDULE = "remarkschedule";// �Ƿ������
	public static final String DR="dr";
	public static final String ZHRATE = "zhrate";// �������ۺ�����
	private int dr;
	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}
	
	/**
	 * �������ۺ�����
	 */
	public UFDouble zhrate;
	
	/**
	 * �Ƿ�ͣ��
	 */
	public UFBoolean disable;
	/*
	 * ���ȱ�ע
	 */
	public String remarkschedule;

	/**
	 * ����
	 */
	public String pk_projectdata_v;
	/**
	 * �Ƿ�����
	 */
	public UFBoolean ismain;
	/**
	 * ��������
	 */
	public String fintype;
	/**
	 * ���ʻ���
	 */
	public String organization;
	/**
	 * ���ʽ��
	 */
	public UFDouble finmny;
	/**
	 * ������ϸ
	 */
	public String findetailed;
	/**
	 * ��������
	 */
	public UFDouble finrate;
	/**
	 * ���ʽ���
	 */
	public String process;
	/**
	 * ����/�ֲ��ϻ�ʱ��
	 */
	public String bra_nchdate;
	/**
	 * ʡ���ϻ�ʱ��
	 */
	public String provi_ncedate;
	/**
	 * ����/�ܲ��ϻ�ʱ��
	 */
	public String headquartersdate;
	/**
	 * ����ʱ��
	 */
	public String replydate;
	/**
	 * ��ͬǩ��ʱ��
	 */
	public String costsigndate;
	/**
	 * �ϲ㵥������
	 */
	public String pk_projectdata;
	/**
	 * ʱ���
	 */
	public UFDateTime ts;
	/*
	 * �Ƿ������
	 */
	public UFBoolean isdrawescheme;
	/**
	 * �Զ�����1
	 */
	public String vbdef1;
	/**
	 * �Զ�����2
	 */
	public String vbdef2;
	/**
	 * �Զ�����3
	 */
	public String vbdef3;
	/**
	 * �Զ�����4
	 */
	public String vbdef4;
	/**
	 * �Զ�����5
	 */
	public String vbdef5;
	/**
	 * �Զ�����6
	 */
	public String vbdef6;
	/**
	 * �Զ�����7
	 */
	public String vbdef7;
	/**
	 * �Զ�����8
	 */
	public String vbdef8;
	/**
	 * �Զ�����9
	 */
	public String vbdef9;
	/**
	 * �Զ�����10
	 */
	public String vbdef10;
	
	
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

	/**
	 * ���� zhrate��Getter����.���������������ۺ����� ��������:2019-6-23
	 * 
	 * @return java.lang.String
	 */
	public UFDouble getZhrate() {
		return this.zhrate;
	}

	/**
	 * ����zhrate��Setter����.���������������ۺ����� ��������:2019-6-23
	 * 
	 * @param newZhrate
	 *            java.lang.String
	 */
	public void setZhrate(UFDouble zhrate) {
		this.zhrate = zhrate;
	}
	
	
	public String getRemarkschedule() {
		return remarkschedule;
	}

	public void setRemarkschedule(String remarkschedule) {
		this.remarkschedule = remarkschedule;
	}

	/**
	 * ���� disable��Getter����.�����������ʷ�����ͷ���� ��������:2019-6-23
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */

	public UFBoolean getDisable() {
		return disable;
	}

	public void setDisable(UFBoolean disable) {
		this.disable = disable;
	}
	
	/**
	 * ���� isdrawescheme��Getter����.�����������ʷ��������Ƿ������ ��������:2019-08-29
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getIsdrawescheme() {
		return isdrawescheme;
	}

	public void setIsdrawescheme(UFBoolean isdrawescheme) {
		this.isdrawescheme = isdrawescheme;
	}

	/**
	 * ���� pk_projectdata_v��Getter����.������������ ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getPk_projectdata_v() {
		return this.pk_projectdata_v;
	}

	/**
	 * ����pk_projectdata_v��Setter����.������������ ��������:2019-6-29
	 * 
	 * @param newPk_projectdata_v
	 *            java.lang.String
	 */
	public void setPk_projectdata_v(String pk_projectdata_v) {
		this.pk_projectdata_v = pk_projectdata_v;
	}

	/**
	 * ���� ismain��Getter����.���������Ƿ����� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getIsmain() {
		return this.ismain;
	}

	/**
	 * ����ismain��Setter����.���������Ƿ����� ��������:2019-6-29
	 * 
	 * @param newIsmain
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setIsmain(UFBoolean ismain) {
		this.ismain = ismain;
	}

	/**
	 * ���� fintype��Getter����.���������������� ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getFintype() {
		return this.fintype;
	}

	/**
	 * ����fintype��Setter����.���������������� ��������:2019-6-29
	 * 
	 * @param newFintype
	 *            java.lang.String
	 */
	public void setFintype(String fintype) {
		this.fintype = fintype;
	}

	/**
	 * ���� organization��Getter����.�����������ʻ��� ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getOrganization() {
		return this.organization;
	}

	/**
	 * ����organization��Setter����.�����������ʻ��� ��������:2019-6-29
	 * 
	 * @param newOrganization
	 *            java.lang.String
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * ���� finmny��Getter����.�����������ʽ�� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public UFDouble getFinmny() {
		return this.finmny;
	}

	/**
	 * ����finmny��Setter����.�����������ʽ�� ��������:2019-6-29
	 * 
	 * @param newFinmny
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setFinmny(UFDouble finmny) {
		this.finmny = finmny;
	}

	/**
	 * ���� findetailed��Getter����.��������������ϸ ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getFindetailed() {
		return this.findetailed;
	}

	/**
	 * ����findetailed��Setter����.��������������ϸ ��������:2019-6-29
	 * 
	 * @param newFindetailed
	 *            java.lang.String
	 */
	public void setFindetailed(String findetailed) {
		this.findetailed = findetailed;
	}

	/**
	 * ���� finrate��Getter����.���������������� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public UFDouble getFinrate() {
		return this.finrate;
	}

	/**
	 * ����finrate��Setter����.���������������� ��������:2019-6-29
	 * 
	 * @param newFinrate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setFinrate(UFDouble finrate) {
		this.finrate = finrate;
	}

	/**
	 * ���� process��Getter����.�����������ʽ��� ��������:2019-6-29
	 * 
	 * @return java.lang.String
	 */
	public String getProcess() {
		return this.process;
	}

	/**
	 * ����process��Setter����.�����������ʽ��� ��������:2019-6-29
	 * 
	 * @param newProcess
	 *            java.lang.String
	 */
	public void setProcess(String process) {
		this.process = process;
	}

	/**
	 * ���� bra_nchdate��Getter����.������������/�ֲ��ϻ�ʱ�� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getBra_nchdate() {
		return this.bra_nchdate;
	}

	/**
	 * ����bra_nchdate��Setter����.������������/�ֲ��ϻ�ʱ�� ��������:2019-6-29
	 * 
	 * @param newBra_nchdate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setBra_nchdate(String bra_nchdate) {
		this.bra_nchdate = bra_nchdate;
	}

	/**
	 * ���� provi_ncedate��Getter����.��������ʡ���ϻ�ʱ�� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getProvi_ncedate() {
		return this.provi_ncedate;
	}

	/**
	 * ����provi_ncedate��Setter����.��������ʡ���ϻ�ʱ�� ��������:2019-6-29
	 * 
	 * @param newProvi_ncedate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setProvi_ncedate(String provi_ncedate) {
		this.provi_ncedate = provi_ncedate;
	}

	/**
	 * ���� headquartersdate��Getter����.������������/�ܲ��ϻ�ʱ�� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getHeadquartersdate() {
		return this.headquartersdate;
	}

	/**
	 * ����headquartersdate��Setter����.������������/�ܲ��ϻ�ʱ�� ��������:2019-6-29
	 * 
	 * @param newHeadquartersdate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setHeadquartersdate(String headquartersdate) {
		this.headquartersdate = headquartersdate;
	}

	/**
	 * ���� replydate��Getter����.������������ʱ�� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getReplydate() {
		return this.replydate;
	}

	/**
	 * ����replydate��Setter����.������������ʱ�� ��������:2019-6-29
	 * 
	 * @param newReplydate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setReplydate(String replydate) {
		this.replydate = replydate;
	}

	/**
	 * ���� costsigndate��Getter����.����������ͬǩ��ʱ�� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public String getCostsigndate() {
		return this.costsigndate;
	}

	/**
	 * ����costsigndate��Setter����.����������ͬǩ��ʱ�� ��������:2019-6-29
	 * 
	 * @param newCostsigndate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setCostsigndate(String costsigndate) {
		this.costsigndate = costsigndate;
	}

	/**
	 * ���� �����ϲ�������Getter����.���������ϲ����� ��������:2019-6-29
	 * 
	 * @return String
	 */
	public String getPk_projectdata() {
		return this.pk_projectdata;
	}

	/**
	 * ���������ϲ�������Setter����.���������ϲ����� ��������:2019-6-29
	 * 
	 * @param newPk_projectdata
	 *            String
	 */
	public void setPk_projectdata(String pk_projectdata) {
		this.pk_projectdata = pk_projectdata;
	}

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2019-6-29
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2019-6-29
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.ProjectDataVVO");
	}
}
