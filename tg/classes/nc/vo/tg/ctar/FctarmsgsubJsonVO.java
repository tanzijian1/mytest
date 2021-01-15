package nc.vo.tg.ctar;

import java.io.Serializable;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴�������������Ϣ
 * </p>
 *  ��������:2019-9-11
 * @author YONYOU NC
 * @version NCPrj ??
 */
public class FctarmsgsubJsonVO  implements Serializable{
	
    private String creditdate;
    private String prcreditdate;
    private java.lang.String pk_psndoc;
    private java.lang.String purchqualifi;
    private java.lang.String buildamount;
    private java.lang.String arnoloanmny;
    private java.lang.String arloanmny;
    private java.lang.String pripaymethod;
    private java.lang.String senpaymethod;
    private java.lang.String vbdef1;
    private java.lang.String vbdef2;
    private java.lang.String vbdef3;
    private java.lang.String vbdef4;
    private java.lang.String vbdef5;
    private java.lang.String vbdef6;
    private java.lang.String vbdef7;
    private java.lang.String vbdef8;
    private java.lang.String vbdef9;
    private java.lang.String vbdef10;
	
	
    public static final String PK_FCT_AR = "pk_fct_ar";
    public static final String PK_ARMSGSUB = "pk_armsgsub";
    public static final String CREDITDATE = "creditdate";
    public static final String PRCREDITDATE = "prcreditdate";
    public static final String PK_PSNDOC = "pk_psndoc";
    public static final String PURCHQUALIFI = "purchqualifi";
    public static final String BUILDAMOUNT = "buildamount";
    public static final String ARNOLOANMNY = "arnoloanmny";
    public static final String ARLOANMNY = "arloanmny";
    public static final String PRIPAYMETHOD = "pripaymethod";
    public static final String SENPAYMETHOD = "senpaymethod";
    public static final String VBDEF1 = "vbdef1";
    public static final String VBDEF2 = "vbdef2";
    public static final String VBDEF3 = "vbdef3";
    public static final String VBDEF4 = "vbdef4";
    public static final String VBDEF5 = "vbdef5";
    public static final String VBDEF6 = "vbdef6";
    public static final String VBDEF7 = "vbdef7";
    public static final String VBDEF8 = "vbdef8";
    public static final String VBDEF9 = "vbdef9";
    public static final String VBDEF10 = "vbdef10";

	
	/**
	 * ���� creditdate��Getter����.���������ſ�����
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getCreditdate () {
		return creditdate;
	}   
	/**
	 * ����creditdate��Setter����.���������ſ�����
	 * ��������:2019-9-11
	 * @param newCreditdate String
	 */
	public void setCreditdate (String newCreditdate ) {
	 	this.creditdate = newCreditdate;
	} 	 
	
	/**
	 * ���� prcreditdate��Getter����.������������ſ�����
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getPrcreditdate () {
		return prcreditdate;
	}   
	/**
	 * ����prcreditdate��Setter����.������������ſ�����
	 * ��������:2019-9-11
	 * @param newPrcreditdate String
	 */
	public void setPrcreditdate (String newPrcreditdate ) {
	 	this.prcreditdate = newPrcreditdate;
	} 	 
	
	/**
	 * ���� pk_psndoc��Getter����.��������������
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getPk_psndoc () {
		return pk_psndoc;
	}   
	/**
	 * ����pk_psndoc��Setter����.��������������
	 * ��������:2019-9-11
	 * @param newPk_psndoc java.lang.String
	 */
	public void setPk_psndoc (java.lang.String newPk_psndoc ) {
	 	this.pk_psndoc = newPk_psndoc;
	} 	 
	
	/**
	 * ���� purchqualifi��Getter����.�������������ʸ�
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getPurchqualifi () {
		return purchqualifi;
	}   
	/**
	 * ����purchqualifi��Setter����.�������������ʸ�
	 * ��������:2019-9-11
	 * @param newPurchqualifi java.lang.String
	 */
	public void setPurchqualifi (java.lang.String newPurchqualifi ) {
	 	this.purchqualifi = newPurchqualifi;
	} 	 
	
	/**
	 * ���� buildamount��Getter����.��������¥���ܶ�
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getBuildamount () {
		return buildamount;
	}   
	/**
	 * ����buildamount��Setter����.��������¥���ܶ�
	 * ��������:2019-9-11
	 * @param newBuildamount java.lang.String
	 */
	public void setBuildamount (java.lang.String newBuildamount ) {
	 	this.buildamount = newBuildamount;
	} 	 
	
	/**
	 * ���� arnoloanmny��Getter����.��������ʵ�շǴ����ܶ�
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getArnoloanmny () {
		return arnoloanmny;
	}   
	/**
	 * ����arnoloanmny��Setter����.��������ʵ�շǴ����ܶ�
	 * ��������:2019-9-11
	 * @param newArnoloanmny java.lang.String
	 */
	public void setArnoloanmny (java.lang.String newArnoloanmny ) {
	 	this.arnoloanmny = newArnoloanmny;
	} 	 
	
	/**
	 * ���� arloanmny��Getter����.��������ʵ�մ������
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getArloanmny () {
		return arloanmny;
	}   
	/**
	 * ����arloanmny��Setter����.��������ʵ�մ������
	 * ��������:2019-9-11
	 * @param newArloanmny java.lang.String
	 */
	public void setArloanmny (java.lang.String newArloanmny ) {
	 	this.arloanmny = newArloanmny;
	} 	 
	
	/**
	 * ���� pripaymethod��Getter����.��������һ�����ʽ
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getPripaymethod () {
		return pripaymethod;
	}   
	/**
	 * ����pripaymethod��Setter����.��������һ�����ʽ
	 * ��������:2019-9-11
	 * @param newPripaymethod java.lang.String
	 */
	public void setPripaymethod (java.lang.String newPripaymethod ) {
	 	this.pripaymethod = newPripaymethod;
	} 	 
	
	/**
	 * ���� senpaymethod��Getter����.���������������ʽ
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getSenpaymethod () {
		return senpaymethod;
	}   
	/**
	 * ����senpaymethod��Setter����.���������������ʽ
	 * ��������:2019-9-11
	 * @param newSenpaymethod java.lang.String
	 */
	public void setSenpaymethod (java.lang.String newSenpaymethod ) {
	 	this.senpaymethod = newSenpaymethod;
	} 	 
	
	/**
	 * ���� vbdef1��Getter����.���������Զ�����1
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef1 () {
		return vbdef1;
	}   
	/**
	 * ����vbdef1��Setter����.���������Զ�����1
	 * ��������:2019-9-11
	 * @param newVbdef1 java.lang.String
	 */
	public void setVbdef1 (java.lang.String newVbdef1 ) {
	 	this.vbdef1 = newVbdef1;
	} 	 
	
	/**
	 * ���� vbdef2��Getter����.���������Զ�����2
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef2 () {
		return vbdef2;
	}   
	/**
	 * ����vbdef2��Setter����.���������Զ�����2
	 * ��������:2019-9-11
	 * @param newVbdef2 java.lang.String
	 */
	public void setVbdef2 (java.lang.String newVbdef2 ) {
	 	this.vbdef2 = newVbdef2;
	} 	 
	
	/**
	 * ���� vbdef3��Getter����.���������Զ�����3
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef3 () {
		return vbdef3;
	}   
	/**
	 * ����vbdef3��Setter����.���������Զ�����3
	 * ��������:2019-9-11
	 * @param newVbdef3 java.lang.String
	 */
	public void setVbdef3 (java.lang.String newVbdef3 ) {
	 	this.vbdef3 = newVbdef3;
	} 	 
	
	/**
	 * ���� vbdef4��Getter����.���������Զ�����4
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef4 () {
		return vbdef4;
	}   
	/**
	 * ����vbdef4��Setter����.���������Զ�����4
	 * ��������:2019-9-11
	 * @param newVbdef4 java.lang.String
	 */
	public void setVbdef4 (java.lang.String newVbdef4 ) {
	 	this.vbdef4 = newVbdef4;
	} 	 
	
	/**
	 * ���� vbdef5��Getter����.���������Զ�����5
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef5 () {
		return vbdef5;
	}   
	/**
	 * ����vbdef5��Setter����.���������Զ�����5
	 * ��������:2019-9-11
	 * @param newVbdef5 java.lang.String
	 */
	public void setVbdef5 (java.lang.String newVbdef5 ) {
	 	this.vbdef5 = newVbdef5;
	} 	 
	
	/**
	 * ���� vbdef6��Getter����.���������Զ�����6
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef6 () {
		return vbdef6;
	}   
	/**
	 * ����vbdef6��Setter����.���������Զ�����6
	 * ��������:2019-9-11
	 * @param newVbdef6 java.lang.String
	 */
	public void setVbdef6 (java.lang.String newVbdef6 ) {
	 	this.vbdef6 = newVbdef6;
	} 	 
	
	/**
	 * ���� vbdef7��Getter����.���������Զ�����7
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef7 () {
		return vbdef7;
	}   
	/**
	 * ����vbdef7��Setter����.���������Զ�����7
	 * ��������:2019-9-11
	 * @param newVbdef7 java.lang.String
	 */
	public void setVbdef7 (java.lang.String newVbdef7 ) {
	 	this.vbdef7 = newVbdef7;
	} 	 
	
	/**
	 * ���� vbdef8��Getter����.���������Զ�����8
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef8 () {
		return vbdef8;
	}   
	/**
	 * ����vbdef8��Setter����.���������Զ�����8
	 * ��������:2019-9-11
	 * @param newVbdef8 java.lang.String
	 */
	public void setVbdef8 (java.lang.String newVbdef8 ) {
	 	this.vbdef8 = newVbdef8;
	} 	 
	
	/**
	 * ���� vbdef9��Getter����.���������Զ�����9
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef9 () {
		return vbdef9;
	}   
	/**
	 * ����vbdef9��Setter����.���������Զ�����9
	 * ��������:2019-9-11
	 * @param newVbdef9 java.lang.String
	 */
	public void setVbdef9 (java.lang.String newVbdef9 ) {
	 	this.vbdef9 = newVbdef9;
	} 	 
	
	/**
	 * ���� vbdef10��Getter����.���������Զ�����10
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef10 () {
		return vbdef10;
	}   
	/**
	 * ����vbdef10��Setter����.���������Զ�����10
	 * ��������:2019-9-11
	 * @param newVbdef10 java.lang.String
	 */
	public void setVbdef10 (java.lang.String newVbdef10 ) {
	 	this.vbdef10 = newVbdef10;
	} 	 
	
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2019-9-11
	  */
     public FctarmsgsubJsonVO() {
		super();	
	}    
	
     
}