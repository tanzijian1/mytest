package nc.vo.tg.image;

import nc.vo.pub.*;
import nc.vo.pub.lang.*;
/**
 * 调用影像接口日志记录 image_callimg_log 生成的VO对象
 *
 * 
 */
public class CallimgLogVO extends SuperVO  {
	
	private static final long serialVersionUID = 1L;
	
	/** 影像条码号 　单据类型+单据主键 */
	public String barcode ; 
	/** 单据号 　 */
	public String billcode ; 
	/** 单据类型 　 */
	public String billtypecode ; 
	/** 自定义项1 　 */
	public String def1 ; 
	/** 自定义项10 　 */
	public String def10 ; 
	/** 自定义项2 　 */
	public String def2 ; 
	/** 自定义项3 　 */
	public String def3 ; 
	/** 自定义项4 　 */
	public String def4 ; 
	/** 自定义项5 　 */
	public String def5 ; 
	/** 自定义项6 　 */
	public String def6 ; 
	/** 自定义项7 　 */
	public String def7 ; 
	/** 自定义项8 　 */
	public String def8 ; 
	/** 自定义项9 　 */
	public String def9 ; 
	/** 删除标记 　 */
	public Integer dr =0; 
	/** 影像状态 　0=待登记接收 
	public String imgstate ; 
	/** 操作类型 　 */
	public String optype ; 
	/** 单据主键 　 */
	public String pk_billid ; 
	/** 集团 　 */
	public String pk_group ; 
	/** 主键 　 */
	public String pk_img_log ; 
	/** 机构编码 　 */
	public String pk_org ; 
	/** 返回地址 　 */
	public String readdress ; 
	/** 返回信息 　 */
	public String remess ; 
	/** 发送信息 　 */
	public String sendmess ; 
	/** 发送时间 　 */
	public UFDateTime sendtime ; 
	/** 调用服务标记 　 */
	public String servicename ; 
	/** 时间戳 　 */
	public UFDateTime ts ; 
	/** 票据类型 　1=报账单
2=资金
3=合同 */
	public String typecode ; 
	/** 用户账号 　 */
	public String useraccount ; 
	/** 用户名称 　 */
	public String username ; 

	public static final String BARCODE = "barcode";
	public static final String BILLCODE = "billcode";
	public static final String BILLTYPECODE = "billtypecode";
	public static final String DEF1 = "def1";
	public static final String DEF10 = "def10";
	public static final String DEF2 = "def2";
	public static final String DEF3 = "def3";
	public static final String DEF4 = "def4";
	public static final String DEF5 = "def5";
	public static final String DEF6 = "def6";
	public static final String DEF7 = "def7";
	public static final String DEF8 = "def8";
	public static final String DEF9 = "def9";
	public static final String DR = "dr";
	public static final String OPTYPE = "optype";
	public static final String PK_BILLID = "pk_billid";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_IMG_LOG = "pk_img_log";
	public static final String PK_ORG = "pk_org";
	public static final String READDRESS = "readdress";
	public static final String REMESS = "remess";
	public static final String SENDMESS = "sendmess";
	public static final String SENDTIME = "sendtime";
	public static final String SERVICENAME = "servicename";
	public static final String TS = "ts";
	public static final String TYPECODE = "typecode";
	public static final String USERACCOUNT = "useraccount";
	public static final String USERNAME = "username";

	public CallimgLogVO(){
		super();
	}
	
	/**
	 * 属性 影像条码号 的setter方法.
	 * @param String barcode 　单据类型+单据主键
	 */
	public void setBarcode(String barcode){
		this.barcode = barcode;
	}
	
	/**
	 * 属性 影像条码号 的getter方法.
	 * 
	 * @return String barcode
	 */
	public  String getBarcode(){
		return barcode;
	}
	
	/**
	 * 属性 单据号 的setter方法.
	 * @param String billcode 　
	 */
	public void setBillcode(String billcode){
		this.billcode = billcode;
	}
	
	/**
	 * 属性 单据号 的getter方法.
	 * 
	 * @return String billcode
	 */
	public  String getBillcode(){
		return billcode;
	}
	
	/**
	 * 属性 单据类型 的setter方法.
	 * @param String billtypecode 　
	 */
	public void setBilltypecode(String billtypecode){
		this.billtypecode = billtypecode;
	}
	
	/**
	 * 属性 单据类型 的getter方法.
	 * 
	 * @return String billtypecode
	 */
	public  String getBilltypecode(){
		return billtypecode;
	}
	
	/**
	 * 属性 自定义项1 的setter方法.
	 * @param String def1 　
	 */
	public void setDef1(String def1){
		this.def1 = def1;
	}
	
	/**
	 * 属性 自定义项1 的getter方法.
	 * 
	 * @return String def1
	 */
	public  String getDef1(){
		return def1;
	}
	
	/**
	 * 属性 自定义项10 的setter方法.
	 * @param String def10 　
	 */
	public void setDef10(String def10){
		this.def10 = def10;
	}
	
	/**
	 * 属性 自定义项10 的getter方法.
	 * 
	 * @return String def10
	 */
	public  String getDef10(){
		return def10;
	}
	
	/**
	 * 属性 自定义项2 的setter方法.
	 * @param String def2 　
	 */
	public void setDef2(String def2){
		this.def2 = def2;
	}
	
	/**
	 * 属性 自定义项2 的getter方法.
	 * 
	 * @return String def2
	 */
	public  String getDef2(){
		return def2;
	}
	
	/**
	 * 属性 自定义项3 的setter方法.
	 * @param String def3 　
	 */
	public void setDef3(String def3){
		this.def3 = def3;
	}
	
	/**
	 * 属性 自定义项3 的getter方法.
	 * 
	 * @return String def3
	 */
	public  String getDef3(){
		return def3;
	}
	
	/**
	 * 属性 自定义项4 的setter方法.
	 * @param String def4 　
	 */
	public void setDef4(String def4){
		this.def4 = def4;
	}
	
	/**
	 * 属性 自定义项4 的getter方法.
	 * 
	 * @return String def4
	 */
	public  String getDef4(){
		return def4;
	}
	
	/**
	 * 属性 自定义项5 的setter方法.
	 * @param String def5 　
	 */
	public void setDef5(String def5){
		this.def5 = def5;
	}
	
	/**
	 * 属性 自定义项5 的getter方法.
	 * 
	 * @return String def5
	 */
	public  String getDef5(){
		return def5;
	}
	
	/**
	 * 属性 自定义项6 的setter方法.
	 * @param String def6 　
	 */
	public void setDef6(String def6){
		this.def6 = def6;
	}
	
	/**
	 * 属性 自定义项6 的getter方法.
	 * 
	 * @return String def6
	 */
	public  String getDef6(){
		return def6;
	}
	
	/**
	 * 属性 自定义项7 的setter方法.
	 * @param String def7 　
	 */
	public void setDef7(String def7){
		this.def7 = def7;
	}
	
	/**
	 * 属性 自定义项7 的getter方法.
	 * 
	 * @return String def7
	 */
	public  String getDef7(){
		return def7;
	}
	
	/**
	 * 属性 自定义项8 的setter方法.
	 * @param String def8 　
	 */
	public void setDef8(String def8){
		this.def8 = def8;
	}
	
	/**
	 * 属性 自定义项8 的getter方法.
	 * 
	 * @return String def8
	 */
	public  String getDef8(){
		return def8;
	}
	
	/**
	 * 属性 自定义项9 的setter方法.
	 * @param String def9 　
	 */
	public void setDef9(String def9){
		this.def9 = def9;
	}
	
	/**
	 * 属性 自定义项9 的getter方法.
	 * 
	 * @return String def9
	 */
	public  String getDef9(){
		return def9;
	}
	
	/**
	 * 属性 删除标记 的setter方法.
	 * @param Integer dr 　
	 */
	public void setDr(Integer dr){
		this.dr = dr;
	}
	
	/**
	 * 属性 删除标记 的getter方法.
	 * 
	 * @return Integer dr
	 */
	public  Integer getDr(){
		return dr;
	}
	
	/**
	 * 属性 操作类型 的setter方法.
	 * @param String optype 　
	 */
	public void setOptype(String optype){
		this.optype = optype;
	}
	
	/**
	 * 属性 操作类型 的getter方法.
	 * 
	 * @return String optype
	 */
	public  String getOptype(){
		return optype;
	}
	
	/**
	 * 属性 单据主键 的setter方法.
	 * @param String pk_billid 　
	 */
	public void setPk_billid(String pk_billid){
		this.pk_billid = pk_billid;
	}
	
	/**
	 * 属性 单据主键 的getter方法.
	 * 
	 * @return String pk_billid
	 */
	public  String getPk_billid(){
		return pk_billid;
	}
	
	/**
	 * 属性 集团 的setter方法.
	 * @param String pk_group 　
	 */
	public void setPk_group(String pk_group){
		this.pk_group = pk_group;
	}
	
	/**
	 * 属性 集团 的getter方法.
	 * 
	 * @return String pk_group
	 */
	public  String getPk_group(){
		return pk_group;
	}
	
	/**
	 * 属性 主键 的setter方法.
	 * @param String pk_img_log 　
	 */
	public void setPk_img_log(String pk_img_log){
		this.pk_img_log = pk_img_log;
	}
	
	/**
	 * 属性 主键 的getter方法.
	 * 
	 * @return String pk_img_log
	 */
	public  String getPk_img_log(){
		return pk_img_log;
	}
	
	/**
	 * 属性 机构编码 的setter方法.
	 * @param String pk_org 　
	 */
	public void setPk_org(String pk_org){
		this.pk_org = pk_org;
	}
	
	/**
	 * 属性 机构编码 的getter方法.
	 * 
	 * @return String pk_org
	 */
	public  String getPk_org(){
		return pk_org;
	}
	
	/**
	 * 属性 返回地址 的setter方法.
	 * @param String readdress 　
	 */
	public void setReaddress(String readdress){
		this.readdress = readdress;
	}
	
	/**
	 * 属性 返回地址 的getter方法.
	 * 
	 * @return String readdress
	 */
	public  String getReaddress(){
		return readdress;
	}
	
	/**
	 * 属性 返回信息 的setter方法.
	 * @param String remess 　
	 */
	public void setRemess(String remess){
		this.remess = remess;
	}
	
	/**
	 * 属性 返回信息 的getter方法.
	 * 
	 * @return String remess
	 */
	public  String getRemess(){
		return remess;
	}
	
	/**
	 * 属性 发送信息 的setter方法.
	 * @param String sendmess 　
	 */
	public void setSendmess(String sendmess){
		this.sendmess = sendmess;
	}
	
	/**
	 * 属性 发送信息 的getter方法.
	 * 
	 * @return String sendmess
	 */
	public  String getSendmess(){
		return sendmess;
	}
	
	/**
	 * 属性 发送时间 的setter方法.
	 * @param UFDateTime sendtime 　
	 */
	public void setSendtime(UFDateTime sendtime){
		this.sendtime = sendtime;
	}
	
	/**
	 * 属性 发送时间 的getter方法.
	 * 
	 * @return UFDateTime sendtime
	 */
	public  UFDateTime getSendtime(){
		return sendtime;
	}
	
	/**
	 * 属性 调用服务标记 的setter方法.
	 * @param String servicename 　
	 */
	public void setServicename(String servicename){
		this.servicename = servicename;
	}
	
	/**
	 * 属性 调用服务标记 的getter方法.
	 * 
	 * @return String servicename
	 */
	public  String getServicename(){
		return servicename;
	}
	
	/**
	 * 属性 时间戳 的setter方法.
	 * @param UFDateTime ts 　
	 */
	public void setTs(UFDateTime ts){
		this.ts = ts;
	}
	
	/**
	 * 属性 时间戳 的getter方法.
	 * 
	 * @return UFDateTime ts
	 */
	public  UFDateTime getTs(){
		return ts;
	}
	
	/**
	 * 属性 票据类型 的setter方法.
	 * @param String typecode 　1=报账单
2=资金
3=合同
	 */
	public void setTypecode(String typecode){
		this.typecode = typecode;
	}
	
	/**
	 * 属性 票据类型 的getter方法.
	 * 
	 * @return String typecode
	 */
	public  String getTypecode(){
		return typecode;
	}
	
	/**
	 * 属性 用户账号 的setter方法.
	 * @param String useraccount 　
	 */
	public void setUseraccount(String useraccount){
		this.useraccount = useraccount;
	}
	
	/**
	 * 属性 用户账号 的getter方法.
	 * 
	 * @return String useraccount
	 */
	public  String getUseraccount(){
		return useraccount;
	}
	
	/**
	 * 属性 用户名称 的setter方法.
	 * @param String username 　
	 */
	public void setUsername(String username){
		this.username = username;
	}
	
	/**
	 * 属性 用户名称 的getter方法.
	 * 
	 * @return String username
	 */
	public  String getUsername(){
		return username;
	}
	
	public void validate() throws ValidationException {
	}
	
	/**
	 * 取得父VO主键字段.
	 * @return java.lang.String
	 */
	public String getParentPKFieldName() {
	
		return null;
	
	}
	
	/**
	 * 取得表主键.
	 * @return String
	 */
	public String getPKFieldName() {
	
		return "pk_img_log";
	
	}
	
	/**
	 * 返回表名称.
	 * @return java.lang.String tableName
	 */
	public String getTableName() {
		return "image_callimg_log";
	}
	
	/**
	 * 返回数值对象的显示名称.
	 */
	public String getEntityName() {
		return "image_callimg_log";
	}
	    
	public CallimgLogVO(String pk_img_log) {
		// 为主键字段赋值:
		this.pk_img_log = pk_img_log;
	}
	
	public String getPrimaryKey() {
		return pk_img_log;
	}
	
	public void setPrimaryKey(String pk_img_log) {
		this.pk_img_log = pk_img_log;
	}
	
}
