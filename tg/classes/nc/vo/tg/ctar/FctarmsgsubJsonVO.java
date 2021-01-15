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
	 * 属性 creditdate的Getter方法.属性名：放款日期
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getCreditdate () {
		return creditdate;
	}   
	/**
	 * 属性creditdate的Setter方法.属性名：放款日期
	 * 创建日期:2019-9-11
	 * @param newCreditdate String
	 */
	public void setCreditdate (String newCreditdate ) {
	 	this.creditdate = newCreditdate;
	} 	 
	
	/**
	 * 属性 prcreditdate的Getter方法.属性名：达待放款日期
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getPrcreditdate () {
		return prcreditdate;
	}   
	/**
	 * 属性prcreditdate的Setter方法.属性名：达待放款日期
	 * 创建日期:2019-9-11
	 * @param newPrcreditdate String
	 */
	public void setPrcreditdate (String newPrcreditdate ) {
	 	this.prcreditdate = newPrcreditdate;
	} 	 
	
	/**
	 * 属性 pk_psndoc的Getter方法.属性名：跟进人
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getPk_psndoc () {
		return pk_psndoc;
	}   
	/**
	 * 属性pk_psndoc的Setter方法.属性名：跟进人
	 * 创建日期:2019-9-11
	 * @param newPk_psndoc java.lang.String
	 */
	public void setPk_psndoc (java.lang.String newPk_psndoc ) {
	 	this.pk_psndoc = newPk_psndoc;
	} 	 
	
	/**
	 * 属性 purchqualifi的Getter方法.属性名：购房资格
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getPurchqualifi () {
		return purchqualifi;
	}   
	/**
	 * 属性purchqualifi的Setter方法.属性名：购房资格
	 * 创建日期:2019-9-11
	 * @param newPurchqualifi java.lang.String
	 */
	public void setPurchqualifi (java.lang.String newPurchqualifi ) {
	 	this.purchqualifi = newPurchqualifi;
	} 	 
	
	/**
	 * 属性 buildamount的Getter方法.属性名：楼款总额
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getBuildamount () {
		return buildamount;
	}   
	/**
	 * 属性buildamount的Setter方法.属性名：楼款总额
	 * 创建日期:2019-9-11
	 * @param newBuildamount java.lang.String
	 */
	public void setBuildamount (java.lang.String newBuildamount ) {
	 	this.buildamount = newBuildamount;
	} 	 
	
	/**
	 * 属性 arnoloanmny的Getter方法.属性名：实收非贷款总额
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getArnoloanmny () {
		return arnoloanmny;
	}   
	/**
	 * 属性arnoloanmny的Setter方法.属性名：实收非贷款总额
	 * 创建日期:2019-9-11
	 * @param newArnoloanmny java.lang.String
	 */
	public void setArnoloanmny (java.lang.String newArnoloanmny ) {
	 	this.arnoloanmny = newArnoloanmny;
	} 	 
	
	/**
	 * 属性 arloanmny的Getter方法.属性名：实收贷款进额
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getArloanmny () {
		return arloanmny;
	}   
	/**
	 * 属性arloanmny的Setter方法.属性名：实收贷款进额
	 * 创建日期:2019-9-11
	 * @param newArloanmny java.lang.String
	 */
	public void setArloanmny (java.lang.String newArloanmny ) {
	 	this.arloanmny = newArloanmny;
	} 	 
	
	/**
	 * 属性 pripaymethod的Getter方法.属性名：一级付款方式
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getPripaymethod () {
		return pripaymethod;
	}   
	/**
	 * 属性pripaymethod的Setter方法.属性名：一级付款方式
	 * 创建日期:2019-9-11
	 * @param newPripaymethod java.lang.String
	 */
	public void setPripaymethod (java.lang.String newPripaymethod ) {
	 	this.pripaymethod = newPripaymethod;
	} 	 
	
	/**
	 * 属性 senpaymethod的Getter方法.属性名：二级付款方式
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getSenpaymethod () {
		return senpaymethod;
	}   
	/**
	 * 属性senpaymethod的Setter方法.属性名：二级付款方式
	 * 创建日期:2019-9-11
	 * @param newSenpaymethod java.lang.String
	 */
	public void setSenpaymethod (java.lang.String newSenpaymethod ) {
	 	this.senpaymethod = newSenpaymethod;
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
     public FctarmsgsubJsonVO() {
		super();	
	}    
	
     
}