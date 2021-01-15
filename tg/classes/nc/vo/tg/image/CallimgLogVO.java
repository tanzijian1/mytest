package nc.vo.tg.image;

import nc.vo.pub.*;
import nc.vo.pub.lang.*;
/**
 * ����Ӱ��ӿ���־��¼ image_callimg_log ���ɵ�VO����
 *
 * 
 */
public class CallimgLogVO extends SuperVO  {
	
	private static final long serialVersionUID = 1L;
	
	/** Ӱ������� ����������+�������� */
	public String barcode ; 
	/** ���ݺ� �� */
	public String billcode ; 
	/** �������� �� */
	public String billtypecode ; 
	/** �Զ�����1 �� */
	public String def1 ; 
	/** �Զ�����10 �� */
	public String def10 ; 
	/** �Զ�����2 �� */
	public String def2 ; 
	/** �Զ�����3 �� */
	public String def3 ; 
	/** �Զ�����4 �� */
	public String def4 ; 
	/** �Զ�����5 �� */
	public String def5 ; 
	/** �Զ�����6 �� */
	public String def6 ; 
	/** �Զ�����7 �� */
	public String def7 ; 
	/** �Զ�����8 �� */
	public String def8 ; 
	/** �Զ�����9 �� */
	public String def9 ; 
	/** ɾ����� �� */
	public Integer dr =0; 
	/** Ӱ��״̬ ��0=���Ǽǽ��� 
	public String imgstate ; 
	/** �������� �� */
	public String optype ; 
	/** �������� �� */
	public String pk_billid ; 
	/** ���� �� */
	public String pk_group ; 
	/** ���� �� */
	public String pk_img_log ; 
	/** �������� �� */
	public String pk_org ; 
	/** ���ص�ַ �� */
	public String readdress ; 
	/** ������Ϣ �� */
	public String remess ; 
	/** ������Ϣ �� */
	public String sendmess ; 
	/** ����ʱ�� �� */
	public UFDateTime sendtime ; 
	/** ���÷����� �� */
	public String servicename ; 
	/** ʱ��� �� */
	public UFDateTime ts ; 
	/** Ʊ������ ��1=���˵�
2=�ʽ�
3=��ͬ */
	public String typecode ; 
	/** �û��˺� �� */
	public String useraccount ; 
	/** �û����� �� */
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
	 * ���� Ӱ������� ��setter����.
	 * @param String barcode ����������+��������
	 */
	public void setBarcode(String barcode){
		this.barcode = barcode;
	}
	
	/**
	 * ���� Ӱ������� ��getter����.
	 * 
	 * @return String barcode
	 */
	public  String getBarcode(){
		return barcode;
	}
	
	/**
	 * ���� ���ݺ� ��setter����.
	 * @param String billcode ��
	 */
	public void setBillcode(String billcode){
		this.billcode = billcode;
	}
	
	/**
	 * ���� ���ݺ� ��getter����.
	 * 
	 * @return String billcode
	 */
	public  String getBillcode(){
		return billcode;
	}
	
	/**
	 * ���� �������� ��setter����.
	 * @param String billtypecode ��
	 */
	public void setBilltypecode(String billtypecode){
		this.billtypecode = billtypecode;
	}
	
	/**
	 * ���� �������� ��getter����.
	 * 
	 * @return String billtypecode
	 */
	public  String getBilltypecode(){
		return billtypecode;
	}
	
	/**
	 * ���� �Զ�����1 ��setter����.
	 * @param String def1 ��
	 */
	public void setDef1(String def1){
		this.def1 = def1;
	}
	
	/**
	 * ���� �Զ�����1 ��getter����.
	 * 
	 * @return String def1
	 */
	public  String getDef1(){
		return def1;
	}
	
	/**
	 * ���� �Զ�����10 ��setter����.
	 * @param String def10 ��
	 */
	public void setDef10(String def10){
		this.def10 = def10;
	}
	
	/**
	 * ���� �Զ�����10 ��getter����.
	 * 
	 * @return String def10
	 */
	public  String getDef10(){
		return def10;
	}
	
	/**
	 * ���� �Զ�����2 ��setter����.
	 * @param String def2 ��
	 */
	public void setDef2(String def2){
		this.def2 = def2;
	}
	
	/**
	 * ���� �Զ�����2 ��getter����.
	 * 
	 * @return String def2
	 */
	public  String getDef2(){
		return def2;
	}
	
	/**
	 * ���� �Զ�����3 ��setter����.
	 * @param String def3 ��
	 */
	public void setDef3(String def3){
		this.def3 = def3;
	}
	
	/**
	 * ���� �Զ�����3 ��getter����.
	 * 
	 * @return String def3
	 */
	public  String getDef3(){
		return def3;
	}
	
	/**
	 * ���� �Զ�����4 ��setter����.
	 * @param String def4 ��
	 */
	public void setDef4(String def4){
		this.def4 = def4;
	}
	
	/**
	 * ���� �Զ�����4 ��getter����.
	 * 
	 * @return String def4
	 */
	public  String getDef4(){
		return def4;
	}
	
	/**
	 * ���� �Զ�����5 ��setter����.
	 * @param String def5 ��
	 */
	public void setDef5(String def5){
		this.def5 = def5;
	}
	
	/**
	 * ���� �Զ�����5 ��getter����.
	 * 
	 * @return String def5
	 */
	public  String getDef5(){
		return def5;
	}
	
	/**
	 * ���� �Զ�����6 ��setter����.
	 * @param String def6 ��
	 */
	public void setDef6(String def6){
		this.def6 = def6;
	}
	
	/**
	 * ���� �Զ�����6 ��getter����.
	 * 
	 * @return String def6
	 */
	public  String getDef6(){
		return def6;
	}
	
	/**
	 * ���� �Զ�����7 ��setter����.
	 * @param String def7 ��
	 */
	public void setDef7(String def7){
		this.def7 = def7;
	}
	
	/**
	 * ���� �Զ�����7 ��getter����.
	 * 
	 * @return String def7
	 */
	public  String getDef7(){
		return def7;
	}
	
	/**
	 * ���� �Զ�����8 ��setter����.
	 * @param String def8 ��
	 */
	public void setDef8(String def8){
		this.def8 = def8;
	}
	
	/**
	 * ���� �Զ�����8 ��getter����.
	 * 
	 * @return String def8
	 */
	public  String getDef8(){
		return def8;
	}
	
	/**
	 * ���� �Զ�����9 ��setter����.
	 * @param String def9 ��
	 */
	public void setDef9(String def9){
		this.def9 = def9;
	}
	
	/**
	 * ���� �Զ�����9 ��getter����.
	 * 
	 * @return String def9
	 */
	public  String getDef9(){
		return def9;
	}
	
	/**
	 * ���� ɾ����� ��setter����.
	 * @param Integer dr ��
	 */
	public void setDr(Integer dr){
		this.dr = dr;
	}
	
	/**
	 * ���� ɾ����� ��getter����.
	 * 
	 * @return Integer dr
	 */
	public  Integer getDr(){
		return dr;
	}
	
	/**
	 * ���� �������� ��setter����.
	 * @param String optype ��
	 */
	public void setOptype(String optype){
		this.optype = optype;
	}
	
	/**
	 * ���� �������� ��getter����.
	 * 
	 * @return String optype
	 */
	public  String getOptype(){
		return optype;
	}
	
	/**
	 * ���� �������� ��setter����.
	 * @param String pk_billid ��
	 */
	public void setPk_billid(String pk_billid){
		this.pk_billid = pk_billid;
	}
	
	/**
	 * ���� �������� ��getter����.
	 * 
	 * @return String pk_billid
	 */
	public  String getPk_billid(){
		return pk_billid;
	}
	
	/**
	 * ���� ���� ��setter����.
	 * @param String pk_group ��
	 */
	public void setPk_group(String pk_group){
		this.pk_group = pk_group;
	}
	
	/**
	 * ���� ���� ��getter����.
	 * 
	 * @return String pk_group
	 */
	public  String getPk_group(){
		return pk_group;
	}
	
	/**
	 * ���� ���� ��setter����.
	 * @param String pk_img_log ��
	 */
	public void setPk_img_log(String pk_img_log){
		this.pk_img_log = pk_img_log;
	}
	
	/**
	 * ���� ���� ��getter����.
	 * 
	 * @return String pk_img_log
	 */
	public  String getPk_img_log(){
		return pk_img_log;
	}
	
	/**
	 * ���� �������� ��setter����.
	 * @param String pk_org ��
	 */
	public void setPk_org(String pk_org){
		this.pk_org = pk_org;
	}
	
	/**
	 * ���� �������� ��getter����.
	 * 
	 * @return String pk_org
	 */
	public  String getPk_org(){
		return pk_org;
	}
	
	/**
	 * ���� ���ص�ַ ��setter����.
	 * @param String readdress ��
	 */
	public void setReaddress(String readdress){
		this.readdress = readdress;
	}
	
	/**
	 * ���� ���ص�ַ ��getter����.
	 * 
	 * @return String readdress
	 */
	public  String getReaddress(){
		return readdress;
	}
	
	/**
	 * ���� ������Ϣ ��setter����.
	 * @param String remess ��
	 */
	public void setRemess(String remess){
		this.remess = remess;
	}
	
	/**
	 * ���� ������Ϣ ��getter����.
	 * 
	 * @return String remess
	 */
	public  String getRemess(){
		return remess;
	}
	
	/**
	 * ���� ������Ϣ ��setter����.
	 * @param String sendmess ��
	 */
	public void setSendmess(String sendmess){
		this.sendmess = sendmess;
	}
	
	/**
	 * ���� ������Ϣ ��getter����.
	 * 
	 * @return String sendmess
	 */
	public  String getSendmess(){
		return sendmess;
	}
	
	/**
	 * ���� ����ʱ�� ��setter����.
	 * @param UFDateTime sendtime ��
	 */
	public void setSendtime(UFDateTime sendtime){
		this.sendtime = sendtime;
	}
	
	/**
	 * ���� ����ʱ�� ��getter����.
	 * 
	 * @return UFDateTime sendtime
	 */
	public  UFDateTime getSendtime(){
		return sendtime;
	}
	
	/**
	 * ���� ���÷����� ��setter����.
	 * @param String servicename ��
	 */
	public void setServicename(String servicename){
		this.servicename = servicename;
	}
	
	/**
	 * ���� ���÷����� ��getter����.
	 * 
	 * @return String servicename
	 */
	public  String getServicename(){
		return servicename;
	}
	
	/**
	 * ���� ʱ��� ��setter����.
	 * @param UFDateTime ts ��
	 */
	public void setTs(UFDateTime ts){
		this.ts = ts;
	}
	
	/**
	 * ���� ʱ��� ��getter����.
	 * 
	 * @return UFDateTime ts
	 */
	public  UFDateTime getTs(){
		return ts;
	}
	
	/**
	 * ���� Ʊ������ ��setter����.
	 * @param String typecode ��1=���˵�
2=�ʽ�
3=��ͬ
	 */
	public void setTypecode(String typecode){
		this.typecode = typecode;
	}
	
	/**
	 * ���� Ʊ������ ��getter����.
	 * 
	 * @return String typecode
	 */
	public  String getTypecode(){
		return typecode;
	}
	
	/**
	 * ���� �û��˺� ��setter����.
	 * @param String useraccount ��
	 */
	public void setUseraccount(String useraccount){
		this.useraccount = useraccount;
	}
	
	/**
	 * ���� �û��˺� ��getter����.
	 * 
	 * @return String useraccount
	 */
	public  String getUseraccount(){
		return useraccount;
	}
	
	/**
	 * ���� �û����� ��setter����.
	 * @param String username ��
	 */
	public void setUsername(String username){
		this.username = username;
	}
	
	/**
	 * ���� �û����� ��getter����.
	 * 
	 * @return String username
	 */
	public  String getUsername(){
		return username;
	}
	
	public void validate() throws ValidationException {
	}
	
	/**
	 * ȡ�ø�VO�����ֶ�.
	 * @return java.lang.String
	 */
	public String getParentPKFieldName() {
	
		return null;
	
	}
	
	/**
	 * ȡ�ñ�����.
	 * @return String
	 */
	public String getPKFieldName() {
	
		return "pk_img_log";
	
	}
	
	/**
	 * ���ر�����.
	 * @return java.lang.String tableName
	 */
	public String getTableName() {
		return "image_callimg_log";
	}
	
	/**
	 * ������ֵ�������ʾ����.
	 */
	public String getEntityName() {
		return "image_callimg_log";
	}
	    
	public CallimgLogVO(String pk_img_log) {
		// Ϊ�����ֶθ�ֵ:
		this.pk_img_log = pk_img_log;
	}
	
	public String getPrimaryKey() {
		return pk_img_log;
	}
	
	public void setPrimaryKey(String pk_img_log) {
		this.pk_img_log = pk_img_log;
	}
	
}
