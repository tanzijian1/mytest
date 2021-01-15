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
public class FctArmsgJsonVO implements Serializable{
	
    private java.lang.String paymethod;
    private String stantotalprice;
    private String price;
    private String painland;
    private String debt;
    private String balancedue;
    private String mertrulitydebt;
    private String unmertrulitydebt;
    private String mgpayment;
    private String mgpaymentdebt;
    private String mutiplerept;
    private java.lang.String mgbank;
    private java.lang.String mgstatus;
    private String mgcompletime;
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
    public static final String PK_FCTARNSG = "pk_fctarnsg";
    public static final String PAYMETHOD = "paymethod";
    public static final String STANTOTALPRICE = "stantotalprice";
    public static final String PRICE = "price";
    public static final String PAINLAND = "painland";
    public static final String DEBT = "debt";
    public static final String BALANCEDUE = "balancedue";
    public static final String MERTRULITYDEBT = "mertrulitydebt";
    public static final String UNMERTRULITYDEBT = "unmertrulitydebt";
    public static final String MGPAYMENT = "mgpayment";
    public static final String MGPAYMENTDEBT = "mgpaymentdebt";
    public static final String MUTIPLEREPT = "mutiplerept";
    public static final String MGBANK = "mgbank";
    public static final String MGSTATUS = "mgstatus";
    public static final String MGCOMPLETIME = "mgcompletime";
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
	 * ���� paymethod��Getter����.�����������ʽ
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getPaymethod () {
		return paymethod;
	}   
	/**
	 * ����paymethod��Setter����.�����������ʽ
	 * ��������:2019-9-11
	 * @param newPaymethod java.lang.String
	 */
	public void setPaymethod (java.lang.String newPaymethod ) {
	 	this.paymethod = newPaymethod;
	} 	 
	
	/**
	 * ���� stantotalprice��Getter����.����������׼�ܼ�
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getStantotalprice () {
		return stantotalprice;
	}   
	/**
	 * ����stantotalprice��Setter����.����������׼�ܼ�
	 * ��������:2019-9-11
	 * @param newStantotalprice String
	 */
	public void setStantotalprice (String newStantotalprice ) {
	 	this.stantotalprice = newStantotalprice;
	} 	 
	
	/**
	 * ���� price��Getter����.������������
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getPrice () {
		return price;
	}   
	/**
	 * ����price��Setter����.������������
	 * ��������:2019-9-11
	 * @param newPrice String
	 */
	public void setPrice (String newPrice ) {
	 	this.price = newPrice;
	} 	 
	
	/**
	 * ���� painland��Getter����.���������Ѹ����
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getPainland () {
		return painland;
	}   
	/**
	 * ����painland��Setter����.���������Ѹ����
	 * ��������:2019-9-11
	 * @param newPainland String
	 */
	public void setPainland (String newPainland ) {
	 	this.painland = newPainland;
	} 	 
	
	/**
	 * ���� debt��Getter����.��������Ƿ��
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getDebt () {
		return debt;
	}   
	/**
	 * ����debt��Setter����.��������Ƿ��
	 * ��������:2019-9-11
	 * @param newDebt String
	 */
	public void setDebt (String newDebt ) {
	 	this.debt = newDebt;
	} 	 
	
	/**
	 * ���� balancedue��Getter����.��������װ��Ƿ��
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getBalancedue () {
		return balancedue;
	}   
	/**
	 * ����balancedue��Setter����.��������װ��Ƿ��
	 * ��������:2019-9-11
	 * @param newBalancedue String
	 */
	public void setBalancedue (String newBalancedue ) {
	 	this.balancedue = newBalancedue;
	} 	 
	
	/**
	 * ���� mertrulitydebt��Getter����.������������Ƿ��
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getMertrulitydebt () {
		return mertrulitydebt;
	}   
	/**
	 * ����mertrulitydebt��Setter����.������������Ƿ��
	 * ��������:2019-9-11
	 * @param newMertrulitydebt String
	 */
	public void setMertrulitydebt (String newMertrulitydebt ) {
	 	this.mertrulitydebt = newMertrulitydebt;
	} 	 
	
	/**
	 * ���� unmertrulitydebt��Getter����.��������δ����Ƿ��
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getUnmertrulitydebt () {
		return unmertrulitydebt;
	}   
	/**
	 * ����unmertrulitydebt��Setter����.��������δ����Ƿ��
	 * ��������:2019-9-11
	 * @param newUnmertrulitydebt String
	 */
	public void setUnmertrulitydebt (String newUnmertrulitydebt ) {
	 	this.unmertrulitydebt = newUnmertrulitydebt;
	} 	 
	
	/**
	 * ���� mgpayment��Getter����.�����������ҿ�
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getMgpayment () {
		return mgpayment;
	}   
	/**
	 * ����mgpayment��Setter����.�����������ҿ�
	 * ��������:2019-9-11
	 * @param newMgpayment String
	 */
	public void setMgpayment (String newMgpayment ) {
	 	this.mgpayment = newMgpayment;
	} 	 
	
	/**
	 * ���� mgpaymentdebt��Getter����.������������Ƿ��
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getMgpaymentdebt () {
		return mgpaymentdebt;
	}   
	/**
	 * ����mgpaymentdebt��Setter����.������������Ƿ��
	 * ��������:2019-9-11
	 * @param newMgpaymentdebt String
	 */
	public void setMgpaymentdebt (String newMgpaymentdebt ) {
	 	this.mgpaymentdebt = newMgpaymentdebt;
	} 	 
	
	/**
	 * ���� mutiplerept��Getter����.�����������տ�
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getMutiplerept () {
		return mutiplerept;
	}   
	/**
	 * ����mutiplerept��Setter����.�����������տ�
	 * ��������:2019-9-11
	 * @param newMutiplerept String
	 */
	public void setMutiplerept (String newMutiplerept ) {
	 	this.mutiplerept = newMutiplerept;
	} 	 
	
	/**
	 * ���� mgbank��Getter����.����������������
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getMgbank () {
		return mgbank;
	}   
	/**
	 * ����mgbank��Setter����.����������������
	 * ��������:2019-9-11
	 * @param newMgbank java.lang.String
	 */
	public void setMgbank (java.lang.String newMgbank ) {
	 	this.mgbank = newMgbank;
	} 	 
	
	/**
	 * ���� mgstatus��Getter����.������������״̬
	 *  ��������:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getMgstatus () {
		return mgstatus;
	}   
	/**
	 * ����mgstatus��Setter����.������������״̬
	 * ��������:2019-9-11
	 * @param newMgstatus java.lang.String
	 */
	public void setMgstatus (java.lang.String newMgstatus ) {
	 	this.mgstatus = newMgstatus;
	} 	 
	
	/**
	 * ���� mgcompletime��Getter����.���������������ʱ��
	 *  ��������:2019-9-11
	 * @return String
	 */
	public String getMgcompletime () {
		return mgcompletime;
	}   
	/**
	 * ����mgcompletime��Setter����.���������������ʱ��
	 * ��������:2019-9-11
	 * @param newMgcompletime String
	 */
	public void setMgcompletime (String newMgcompletime ) {
	 	this.mgcompletime = newMgcompletime;
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
     public FctArmsgJsonVO() {
		super();	
	}    
	
     
}