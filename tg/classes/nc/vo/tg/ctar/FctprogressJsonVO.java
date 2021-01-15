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
public class FctprogressJsonVO implements Serializable{
	
    private java.lang.String apartment;
    private java.lang.String decoratestan;
    private java.lang.String note;
    private java.lang.String roompart;
    private java.lang.String proprocess;
    private String subtotalprice;
    private String paymny;
    private String agredate;
    private String advancedate;
    private java.lang.String advabcecode;
    private String partrightime;
    private String netagredate;
    private java.lang.String filedate;
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
    public static final String PK_FCTPROGRESS = "pk_fctprogress";
    public static final String APARTMENT = "apartment";
    public static final String DECORATESTAN = "decoratestan";
    public static final String NOTE = "note";
    public static final String ROOMPART = "roompart";
    public static final String PROPROCESS = "proprocess";
    public static final String SUBTOTALPRICE = "subtotalprice";
    public static final String PAYMNY = "paymny";
    public static final String AGREDATE = "agredate";
    public static final String ADVANCEDATE = "advancedate";
    public static final String ADVABCECODE = "advabcecode";
    public static final String PARTRIGHTIME = "partrightime";
    public static final String NETAGREDATE = "netagredate";
    public static final String FILEDATE = "filedate";
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
	 * ���� apartment��Getter����.������������
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getApartment () {
		return apartment;
	}   
	/**
	 * ����apartment��Setter����.������������
	 * ��������:2019-9-11
	 * @param newApartment java.lang.String
	 */
	public void setApartment (java.lang.String newApartment ) {
	 	this.apartment = newApartment;
	} 	 
	
	/**
	 * ���� decoratestan��Getter����.��������װ�ޱ�׼
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getDecoratestan () {
		return decoratestan;
	}   
	/**
	 * ����decoratestan��Setter����.��������װ�ޱ�׼
	 * ��������:2019-9-11
	 * @param newDecoratestan java.lang.String
	 */
	public void setDecoratestan (java.lang.String newDecoratestan ) {
	 	this.decoratestan = newDecoratestan;
	} 	 
	
	/**
	 * ���� note��Getter����.���������ۿ�˵��
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getNote () {
		return note;
	}   
	/**
	 * ����note��Setter����.���������ۿ�˵��
	 * ��������:2019-9-11
	 * @param newNote java.lang.String
	 */
	public void setNote (java.lang.String newNote ) {
	 	this.note = newNote;
	} 	 
	
	/**
	 * ���� roompart��Getter����.���������������䳵λ
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getRoompart () {
		return roompart;
	}   
	/**
	 * ����roompart��Setter����.���������������䳵λ
	 * ��������:2019-9-11
	 * @param newRoompart java.lang.String
	 */
	public void setRoompart (java.lang.String newRoompart ) {
	 	this.roompart = newRoompart;
	} 	 
	
	/**
	 * ���� proprocess��Getter����.����������Ȩ����
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getProprocess () {
		return proprocess;
	}   
	/**
	 * ����proprocess��Setter����.����������Ȩ����
	 * ��������:2019-9-11
	 * @param newProprocess java.lang.String
	 */
	public void setProprocess (java.lang.String newProprocess ) {
	 	this.proprocess = newProprocess;
	} 	 
	
	/**
	 * ���� subtotalprice��Getter����.������������Э���ܼ�
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getSubtotalprice () {
		return subtotalprice;
	}   
	/**
	 * ����subtotalprice��Setter����.������������Э���ܼ�
	 * ��������:2019-9-11
	 * @param newSubtotalprice String
	 */
	public void setSubtotalprice (String newSubtotalprice ) {
	 	this.subtotalprice = newSubtotalprice;
	} 	 
	
	/**
	 * ���� paymny��Getter����.���������Ѹ�װ�޿�
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getPaymny () {
		return paymny;
	}   
	/**
	 * ����paymny��Setter����.���������Ѹ�װ�޿�
	 * ��������:2019-9-11
	 * @param newPaymny String
	 */
	public void setPaymny (String newPaymny ) {
	 	this.paymny = newPaymny;
	} 	 
	
	/**
	 * ���� agredate��Getter����.��������Լ����ǩ����
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getAgredate () {
		return agredate;
	}   
	/**
	 * ����agredate��Setter����.��������Լ����ǩ����
	 * ��������:2019-9-11
	 * @param newAgredate String
	 */
	public void setAgredate (String newAgredate ) {
	 	this.agredate = newAgredate;
	} 	 
	
	/**
	 * ���� advancedate��Getter����.��������ȡ��Ԥ��֤����
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getAdvancedate () {
		return advancedate;
	}   
	/**
	 * ����advancedate��Setter����.��������ȡ��Ԥ��֤����
	 * ��������:2019-9-11
	 * @param newAdvancedate String
	 */
	public void setAdvancedate (String newAdvancedate ) {
	 	this.advancedate = newAdvancedate;
	} 	 
	
	/**
	 * ���� advabcecode��Getter����.��������Ԥ��֤���
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getAdvabcecode () {
		return advabcecode;
	}   
	/**
	 * ����advabcecode��Setter����.��������Ԥ��֤���
	 * ��������:2019-9-11
	 * @param newAdvabcecode java.lang.String
	 */
	public void setAdvabcecode (java.lang.String newAdvabcecode ) {
	 	this.advabcecode = newAdvabcecode;
	} 	 
	
	/**
	 * ���� partrightime��Getter����.����������λȨȷ������
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getPartrightime () {
		return partrightime;
	}   
	/**
	 * ����partrightime��Setter����.����������λȨȷ������
	 * ��������:2019-9-11
	 * @param newPartrightime String
	 */
	public void setPartrightime (String newPartrightime ) {
	 	this.partrightime = newPartrightime;
	} 	 
	
	/**
	 * ���� netagredate��Getter����.����������ǩ����
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getNetagredate () {
		return netagredate;
	}   
	/**
	 * ����netagredate��Setter����.����������ǩ����
	 * ��������:2019-9-11
	 * @param newNetagredate String
	 */
	public void setNetagredate (String newNetagredate ) {
	 	this.netagredate = newNetagredate;
	} 	 
	
	/**
	 * ���� filedate��Getter����.�����������������ͼ�����
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getFiledate () {
		return filedate;
	}   
	/**
	 * ����filedate��Setter����.�����������������ͼ�����
	 * ��������:2019-9-11
	 * @param newFiledate java.lang.String
	 */
	public void setFiledate (java.lang.String newFiledate ) {
	 	this.filedate = newFiledate;
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
     public FctprogressJsonVO() {
		super();	
	}    
     
}