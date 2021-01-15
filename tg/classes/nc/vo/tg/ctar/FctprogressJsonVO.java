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
	 * 属性 apartment的Getter方法.属性名：户型
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getApartment () {
		return apartment;
	}   
	/**
	 * 属性apartment的Setter方法.属性名：户型
	 * 创建日期:2019-9-11
	 * @param newApartment java.lang.String
	 */
	public void setApartment (java.lang.String newApartment ) {
	 	this.apartment = newApartment;
	} 	 
	
	/**
	 * 属性 decoratestan的Getter方法.属性名：装修标准
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getDecoratestan () {
		return decoratestan;
	}   
	/**
	 * 属性decoratestan的Setter方法.属性名：装修标准
	 * 创建日期:2019-9-11
	 * @param newDecoratestan java.lang.String
	 */
	public void setDecoratestan (java.lang.String newDecoratestan ) {
	 	this.decoratestan = newDecoratestan;
	} 	 
	
	/**
	 * 属性 note的Getter方法.属性名：折扣说明
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getNote () {
		return note;
	}   
	/**
	 * 属性note的Setter方法.属性名：折扣说明
	 * 创建日期:2019-9-11
	 * @param newNote java.lang.String
	 */
	public void setNote (java.lang.String newNote ) {
	 	this.note = newNote;
	} 	 
	
	/**
	 * 属性 roompart的Getter方法.属性名：关联房间车位
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getRoompart () {
		return roompart;
	}   
	/**
	 * 属性roompart的Setter方法.属性名：关联房间车位
	 * 创建日期:2019-9-11
	 * @param newRoompart java.lang.String
	 */
	public void setRoompart (java.lang.String newRoompart ) {
	 	this.roompart = newRoompart;
	} 	 
	
	/**
	 * 属性 proprocess的Getter方法.属性名：产权进程
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getProprocess () {
		return proprocess;
	}   
	/**
	 * 属性proprocess的Setter方法.属性名：产权进程
	 * 创建日期:2019-9-11
	 * @param newProprocess java.lang.String
	 */
	public void setProprocess (java.lang.String newProprocess ) {
	 	this.proprocess = newProprocess;
	} 	 
	
	/**
	 * 属性 subtotalprice的Getter方法.属性名：补充协议总价
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getSubtotalprice () {
		return subtotalprice;
	}   
	/**
	 * 属性subtotalprice的Setter方法.属性名：补充协议总价
	 * 创建日期:2019-9-11
	 * @param newSubtotalprice String
	 */
	public void setSubtotalprice (String newSubtotalprice ) {
	 	this.subtotalprice = newSubtotalprice;
	} 	 
	
	/**
	 * 属性 paymny的Getter方法.属性名：已付装修款
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getPaymny () {
		return paymny;
	}   
	/**
	 * 属性paymny的Setter方法.属性名：已付装修款
	 * 创建日期:2019-9-11
	 * @param newPaymny String
	 */
	public void setPaymny (String newPaymny ) {
	 	this.paymny = newPaymny;
	} 	 
	
	/**
	 * 属性 agredate的Getter方法.属性名：约定网签日期
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getAgredate () {
		return agredate;
	}   
	/**
	 * 属性agredate的Setter方法.属性名：约定网签日期
	 * 创建日期:2019-9-11
	 * @param newAgredate String
	 */
	public void setAgredate (String newAgredate ) {
	 	this.agredate = newAgredate;
	} 	 
	
	/**
	 * 属性 advancedate的Getter方法.属性名：取得预售证日期
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getAdvancedate () {
		return advancedate;
	}   
	/**
	 * 属性advancedate的Setter方法.属性名：取得预售证日期
	 * 创建日期:2019-9-11
	 * @param newAdvancedate String
	 */
	public void setAdvancedate (String newAdvancedate ) {
	 	this.advancedate = newAdvancedate;
	} 	 
	
	/**
	 * 属性 advabcecode的Getter方法.属性名：预售证编号
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getAdvabcecode () {
		return advabcecode;
	}   
	/**
	 * 属性advabcecode的Setter方法.属性名：预售证编号
	 * 创建日期:2019-9-11
	 * @param newAdvabcecode java.lang.String
	 */
	public void setAdvabcecode (java.lang.String newAdvabcecode ) {
	 	this.advabcecode = newAdvabcecode;
	} 	 
	
	/**
	 * 属性 partrightime的Getter方法.属性名：车位权确认日期
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getPartrightime () {
		return partrightime;
	}   
	/**
	 * 属性partrightime的Setter方法.属性名：车位权确认日期
	 * 创建日期:2019-9-11
	 * @param newPartrightime String
	 */
	public void setPartrightime (String newPartrightime ) {
	 	this.partrightime = newPartrightime;
	} 	 
	
	/**
	 * 属性 netagredate的Getter方法.属性名：网签日期
	 *  创建日期:2019-9-11
	 * @return String
	 */
	public String getNetagredate () {
		return netagredate;
	}   
	/**
	 * 属性netagredate的Setter方法.属性名：网签日期
	 * 创建日期:2019-9-11
	 * @param newNetagredate String
	 */
	public void setNetagredate (String newNetagredate ) {
	 	this.netagredate = newNetagredate;
	} 	 
	
	/**
	 * 属性 filedate的Getter方法.属性名：资料齐已送件日期
	 *  创建日期:2019-9-11
	 * @return java.lang.String
	 */
	public java.lang.String getFiledate () {
		return filedate;
	}   
	/**
	 * 属性filedate的Setter方法.属性名：资料齐已送件日期
	 * 创建日期:2019-9-11
	 * @param newFiledate java.lang.String
	 */
	public void setFiledate (java.lang.String newFiledate ) {
	 	this.filedate = newFiledate;
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
     public FctprogressJsonVO() {
		super();	
	}    
     
}