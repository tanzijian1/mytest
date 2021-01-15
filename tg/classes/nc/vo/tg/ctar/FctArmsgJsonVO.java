package nc.vo.tg.ctar;

import java.io.Serializable;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加类的描述信息
 * </p>
 *  创建日期:2019-9-11
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
	 * 属性 paymethod的Getter方法.属性名：付款方式
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getPaymethod () {
		return paymethod;
	}   
	/**
	 * 属性paymethod的Setter方法.属性名：付款方式
	 * 创建日期:2019-9-11
	 * @param newPaymethod java.lang.String
	 */
	public void setPaymethod (java.lang.String newPaymethod ) {
	 	this.paymethod = newPaymethod;
	} 	 
	
	/**
	 * 属性 stantotalprice的Getter方法.属性名：标准总价
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getStantotalprice () {
		return stantotalprice;
	}   
	/**
	 * 属性stantotalprice的Setter方法.属性名：标准总价
	 * 创建日期:2019-9-11
	 * @param newStantotalprice String
	 */
	public void setStantotalprice (String newStantotalprice ) {
	 	this.stantotalprice = newStantotalprice;
	} 	 
	
	/**
	 * 属性 price的Getter方法.属性名：单价
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getPrice () {
		return price;
	}   
	/**
	 * 属性price的Setter方法.属性名：单价
	 * 创建日期:2019-9-11
	 * @param newPrice String
	 */
	public void setPrice (String newPrice ) {
	 	this.price = newPrice;
	} 	 
	
	/**
	 * 属性 painland的Getter方法.属性名：已付借款
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getPainland () {
		return painland;
	}   
	/**
	 * 属性painland的Setter方法.属性名：已付借款
	 * 创建日期:2019-9-11
	 * @param newPainland String
	 */
	public void setPainland (String newPainland ) {
	 	this.painland = newPainland;
	} 	 
	
	/**
	 * 属性 debt的Getter方法.属性名：欠款
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getDebt () {
		return debt;
	}   
	/**
	 * 属性debt的Setter方法.属性名：欠款
	 * 创建日期:2019-9-11
	 * @param newDebt String
	 */
	public void setDebt (String newDebt ) {
	 	this.debt = newDebt;
	} 	 
	
	/**
	 * 属性 balancedue的Getter方法.属性名：装修欠款
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getBalancedue () {
		return balancedue;
	}   
	/**
	 * 属性balancedue的Setter方法.属性名：装修欠款
	 * 创建日期:2019-9-11
	 * @param newBalancedue String
	 */
	public void setBalancedue (String newBalancedue ) {
	 	this.balancedue = newBalancedue;
	} 	 
	
	/**
	 * 属性 mertrulitydebt的Getter方法.属性名：到期欠款
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getMertrulitydebt () {
		return mertrulitydebt;
	}   
	/**
	 * 属性mertrulitydebt的Setter方法.属性名：到期欠款
	 * 创建日期:2019-9-11
	 * @param newMertrulitydebt String
	 */
	public void setMertrulitydebt (String newMertrulitydebt ) {
	 	this.mertrulitydebt = newMertrulitydebt;
	} 	 
	
	/**
	 * 属性 unmertrulitydebt的Getter方法.属性名：未到期欠款
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getUnmertrulitydebt () {
		return unmertrulitydebt;
	}   
	/**
	 * 属性unmertrulitydebt的Setter方法.属性名：未到期欠款
	 * 创建日期:2019-9-11
	 * @param newUnmertrulitydebt String
	 */
	public void setUnmertrulitydebt (String newUnmertrulitydebt ) {
	 	this.unmertrulitydebt = newUnmertrulitydebt;
	} 	 
	
	/**
	 * 属性 mgpayment的Getter方法.属性名：按揭款
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getMgpayment () {
		return mgpayment;
	}   
	/**
	 * 属性mgpayment的Setter方法.属性名：按揭款
	 * 创建日期:2019-9-11
	 * @param newMgpayment String
	 */
	public void setMgpayment (String newMgpayment ) {
	 	this.mgpayment = newMgpayment;
	} 	 
	
	/**
	 * 属性 mgpaymentdebt的Getter方法.属性名：按揭欠款
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getMgpaymentdebt () {
		return mgpaymentdebt;
	}   
	/**
	 * 属性mgpaymentdebt的Setter方法.属性名：按揭欠款
	 * 创建日期:2019-9-11
	 * @param newMgpaymentdebt String
	 */
	public void setMgpaymentdebt (String newMgpaymentdebt ) {
	 	this.mgpaymentdebt = newMgpaymentdebt;
	} 	 
	
	/**
	 * 属性 mutiplerept的Getter方法.属性名：多收款
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getMutiplerept () {
		return mutiplerept;
	}   
	/**
	 * 属性mutiplerept的Setter方法.属性名：多收款
	 * 创建日期:2019-9-11
	 * @param newMutiplerept String
	 */
	public void setMutiplerept (String newMutiplerept ) {
	 	this.mutiplerept = newMutiplerept;
	} 	 
	
	/**
	 * 属性 mgbank的Getter方法.属性名：按揭银行
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getMgbank () {
		return mgbank;
	}   
	/**
	 * 属性mgbank的Setter方法.属性名：按揭银行
	 * 创建日期:2019-9-11
	 * @param newMgbank java.lang.String
	 */
	public void setMgbank (java.lang.String newMgbank ) {
	 	this.mgbank = newMgbank;
	} 	 
	
	/**
	 * 属性 mgstatus的Getter方法.属性名：按揭状态
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getMgstatus () {
		return mgstatus;
	}   
	/**
	 * 属性mgstatus的Setter方法.属性名：按揭状态
	 * 创建日期:2019-9-11
	 * @param newMgstatus java.lang.String
	 */
	public void setMgstatus (java.lang.String newMgstatus ) {
	 	this.mgstatus = newMgstatus;
	} 	 
	
	/**
	 * 属性 mgcompletime的Getter方法.属性名：按揭完成时间
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getMgcompletime () {
		return mgcompletime;
	}   
	/**
	 * 属性mgcompletime的Setter方法.属性名：按揭完成时间
	 * 创建日期:2019-9-11
	 * @param newMgcompletime String
	 */
	public void setMgcompletime (String newMgcompletime ) {
	 	this.mgcompletime = newMgcompletime;
	} 	 
	
	/**
	 * 属性 vbdef1的Getter方法.属性名：自定义项1
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef1 () {
		return vbdef1;
	}   
	/**
	 * 属性vbdef1的Setter方法.属性名：自定义项1
	 * 创建日期:2019-9-11
	 * @param newVbdef1 java.lang.String
	 */
	public void setVbdef1 (java.lang.String newVbdef1 ) {
	 	this.vbdef1 = newVbdef1;
	} 	 
	
	/**
	 * 属性 vbdef2的Getter方法.属性名：自定义项2
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef2 () {
		return vbdef2;
	}   
	/**
	 * 属性vbdef2的Setter方法.属性名：自定义项2
	 * 创建日期:2019-9-11
	 * @param newVbdef2 java.lang.String
	 */
	public void setVbdef2 (java.lang.String newVbdef2 ) {
	 	this.vbdef2 = newVbdef2;
	} 	 
	
	/**
	 * 属性 vbdef3的Getter方法.属性名：自定义项3
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef3 () {
		return vbdef3;
	}   
	/**
	 * 属性vbdef3的Setter方法.属性名：自定义项3
	 * 创建日期:2019-9-11
	 * @param newVbdef3 java.lang.String
	 */
	public void setVbdef3 (java.lang.String newVbdef3 ) {
	 	this.vbdef3 = newVbdef3;
	} 	 
	
	/**
	 * 属性 vbdef4的Getter方法.属性名：自定义项4
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef4 () {
		return vbdef4;
	}   
	/**
	 * 属性vbdef4的Setter方法.属性名：自定义项4
	 * 创建日期:2019-9-11
	 * @param newVbdef4 java.lang.String
	 */
	public void setVbdef4 (java.lang.String newVbdef4 ) {
	 	this.vbdef4 = newVbdef4;
	} 	 
	
	/**
	 * 属性 vbdef5的Getter方法.属性名：自定义项5
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef5 () {
		return vbdef5;
	}   
	/**
	 * 属性vbdef5的Setter方法.属性名：自定义项5
	 * 创建日期:2019-9-11
	 * @param newVbdef5 java.lang.String
	 */
	public void setVbdef5 (java.lang.String newVbdef5 ) {
	 	this.vbdef5 = newVbdef5;
	} 	 
	
	/**
	 * 属性 vbdef6的Getter方法.属性名：自定义项6
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef6 () {
		return vbdef6;
	}   
	/**
	 * 属性vbdef6的Setter方法.属性名：自定义项6
	 * 创建日期:2019-9-11
	 * @param newVbdef6 java.lang.String
	 */
	public void setVbdef6 (java.lang.String newVbdef6 ) {
	 	this.vbdef6 = newVbdef6;
	} 	 
	
	/**
	 * 属性 vbdef7的Getter方法.属性名：自定义项7
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef7 () {
		return vbdef7;
	}   
	/**
	 * 属性vbdef7的Setter方法.属性名：自定义项7
	 * 创建日期:2019-9-11
	 * @param newVbdef7 java.lang.String
	 */
	public void setVbdef7 (java.lang.String newVbdef7 ) {
	 	this.vbdef7 = newVbdef7;
	} 	 
	
	/**
	 * 属性 vbdef8的Getter方法.属性名：自定义项8
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef8 () {
		return vbdef8;
	}   
	/**
	 * 属性vbdef8的Setter方法.属性名：自定义项8
	 * 创建日期:2019-9-11
	 * @param newVbdef8 java.lang.String
	 */
	public void setVbdef8 (java.lang.String newVbdef8 ) {
	 	this.vbdef8 = newVbdef8;
	} 	 
	
	/**
	 * 属性 vbdef9的Getter方法.属性名：自定义项9
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef9 () {
		return vbdef9;
	}   
	/**
	 * 属性vbdef9的Setter方法.属性名：自定义项9
	 * 创建日期:2019-9-11
	 * @param newVbdef9 java.lang.String
	 */
	public void setVbdef9 (java.lang.String newVbdef9 ) {
	 	this.vbdef9 = newVbdef9;
	} 	 
	
	/**
	 * 属性 vbdef10的Getter方法.属性名：自定义项10
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef10 () {
		return vbdef10;
	}   
	/**
	 * 属性vbdef10的Setter方法.属性名：自定义项10
	 * 创建日期:2019-9-11
	 * @param newVbdef10 java.lang.String
	 */
	public void setVbdef10 (java.lang.String newVbdef10 ) {
	 	this.vbdef10 = newVbdef10;
	} 	 
	
	
    
    
	
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2019-9-11
	  */
     public FctArmsgJsonVO() {
		super();	
	}    
	
     
}