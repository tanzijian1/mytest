package nc.vo.tg.masterdata;

import nc.vo.pub.SuperVO;

/**
 * @author ASUS
 *¥����Ϣ�ӿ�VO 2020-08-24 LJF
 */
@SuppressWarnings("serial")
public class BlockBuildingInfoVO extends SuperVO {
	/**����¥��ID�����У�WBD_BlockBuilding_S*/
	private String id ;
	/**��Ŀ���ڣ�������WBD_ProjectBlock_T*/
	private String fpbid;
	/**¥����*/
	private String fnumber; 
	/**Ӫ��¥��*/
	private String fmarketnumber;
	/**��Ʒ����ID��������WBD_Produce_T(����ҵ̬)*/
	private String fprdid;
	/**���*/
	private String ftender;
	/**�Ƿ���ۣ�1 = ���ۡ�0 = ������*/
	private String fsaleable;
	/**�������*/
	private String fbuildingarea;
	/**�������*/
	private String ffactplotarea;
	/**���������*/
	private String ffactnonplotarea;
	/**�������*/
	private String fsaleablearea;
	/**���������*/
	private String fnonsaleablearea;
	/**�ܿ����*/
	private String foperfloorarea;
	/**�뻧�������*/
	private String fhallarea;
	/**�������*/
	private String fbuildingbasearea;
	/**��ͨסլ����*/
	private String fcommonnumber;
	/**����ͨסլ����*/
	private String funcommonnumber;
	/**��Ԫ��*/
	private String funitnumber;
	/**������*/
	private String froomnumber;
	/**����-���ϲ���*/
	private String ffloor;
	/**�����ܸ߶�*/
	private String fbuildingheight;
	/**�������*/
	private String finternalarea;
	/**��̯���*/
	private String fapportionarea;
	/**�����׶Σ����룺WBD_BlockStage*/
	private String fblockstage;
	/**���� �����WBD_BuildGroup*/
	private String fbuildgroup;
	/**���������������ʱ��ʹ�ã����͹滮�����*/
	private String ftotalunderarea;
	/**��Ȩ��λ����������ʹ�ã���Ȩ��λ����*/
	private String fpropertyparkingplace;
	/**�ǲ�Ȩ��λ����������ʹ�ã��޲�Ȩ��λ����*/
	private String fnonpropertyparkingplace;
	/**����*/
	private String fprice;
	/**���²���*/
	private String funderfloorcount;
	/**�ײ��������*/
	private String firstshop;
	/**���㼰�����������*/
	private String secondshop;
	/**�ӽ����*/
	private String faddarea;
	/**��������*/
	private String fhireyears;
	/**�ײ㵥Ԫ��*/
	private String ffirststepunit;
	/**�������ϵ�Ԫ��*/
	private String fsecondstepunit;
	/**���޼ӳ�ϵ�� �ӳ�ϵ�� */
	private String fhireratio;
	/**ռ�����*/
	private String flandarea;
	/**����ṹ*/
	private String fmainstructure;
	/**��̯ϵ��*/
	private String fsharerate;
	/**������������(ָ�꿨)��ҵ̬��*/
	private String FproductFeature;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFpbid() {
		return fpbid;
	}
	public void setFpbid(String fpbid) {
		this.fpbid = fpbid;
	}
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getFmarketnumber() {
		return fmarketnumber;
	}
	public void setFmarketnumber(String fmarketnumber) {
		this.fmarketnumber = fmarketnumber;
	}
	public String getFprdid() {
		return fprdid;
	}
	public void setFprdid(String fprdid) {
		this.fprdid = fprdid;
	}
	public String getFtender() {
		return ftender;
	}
	public void setFtender(String ftender) {
		this.ftender = ftender;
	}
	public String getFsaleable() {
		return fsaleable;
	}
	public void setFsaleable(String fsaleable) {
		this.fsaleable = fsaleable;
	}
	public String getFbuildingarea() {
		return fbuildingarea;
	}
	public void setFbuildingarea(String fbuildingarea) {
		this.fbuildingarea = fbuildingarea;
	}
	public String getFfactplotarea() {
		return ffactplotarea;
	}
	public void setFfactplotarea(String ffactplotarea) {
		this.ffactplotarea = ffactplotarea;
	}
	public String getFfactnonplotarea() {
		return ffactnonplotarea;
	}
	public void setFfactnonplotarea(String ffactnonplotarea) {
		this.ffactnonplotarea = ffactnonplotarea;
	}
	public String getFsaleablearea() {
		return fsaleablearea;
	}
	public void setFsaleablearea(String fsaleablearea) {
		this.fsaleablearea = fsaleablearea;
	}
	public String getFnonsaleablearea() {
		return fnonsaleablearea;
	}
	public void setFnonsaleablearea(String fnonsaleablearea) {
		this.fnonsaleablearea = fnonsaleablearea;
	}
	public String getFoperfloorarea() {
		return foperfloorarea;
	}
	public void setFoperfloorarea(String foperfloorarea) {
		this.foperfloorarea = foperfloorarea;
	}
	public String getFhallarea() {
		return fhallarea;
	}
	public void setFhallarea(String fhallarea) {
		this.fhallarea = fhallarea;
	}
	public String getFbuildingbasearea() {
		return fbuildingbasearea;
	}
	public void setFbuildingbasearea(String fbuildingbasearea) {
		this.fbuildingbasearea = fbuildingbasearea;
	}
	public String getFcommonnumber() {
		return fcommonnumber;
	}
	public void setFcommonnumber(String fcommonnumber) {
		this.fcommonnumber = fcommonnumber;
	}
	public String getFuncommonnumber() {
		return funcommonnumber;
	}
	public void setFuncommonnumber(String funcommonnumber) {
		this.funcommonnumber = funcommonnumber;
	}
	public String getFunitnumber() {
		return funitnumber;
	}
	public void setFunitnumber(String funitnumber) {
		this.funitnumber = funitnumber;
	}
	public String getFroomnumber() {
		return froomnumber;
	}
	public void setFroomnumber(String froomnumber) {
		this.froomnumber = froomnumber;
	}
	public String getFfloor() {
		return ffloor;
	}
	public void setFfloor(String ffloor) {
		this.ffloor = ffloor;
	}
	public String getFbuildingheight() {
		return fbuildingheight;
	}
	public void setFbuildingheight(String fbuildingheight) {
		this.fbuildingheight = fbuildingheight;
	}
	public String getFinternalarea() {
		return finternalarea;
	}
	public void setFinternalarea(String finternalarea) {
		this.finternalarea = finternalarea;
	}
	public String getFapportionarea() {
		return fapportionarea;
	}
	public void setFapportionarea(String fapportionarea) {
		this.fapportionarea = fapportionarea;
	}
	public String getFblockstage() {
		return fblockstage;
	}
	public void setFblockstage(String fblockstage) {
		this.fblockstage = fblockstage;
	}
	public String getFbuildgroup() {
		return fbuildgroup;
	}
	public void setFbuildgroup(String fbuildgroup) {
		this.fbuildgroup = fbuildgroup;
	}
	public String getFprice() {
		return fprice;
	}
	public void setFprice(String fprice) {
		this.fprice = fprice;
	}
	public String getFunderfloorcount() {
		return funderfloorcount;
	}
	public void setFunderfloorcount(String funderfloorcount) {
		this.funderfloorcount = funderfloorcount;
	}
	public String getFmainstructure() {
		return fmainstructure;
	}
	public void setFmainstructure(String fmainstructure) {
		this.fmainstructure = fmainstructure;
	}
	public String getFsharerate() {
		return fsharerate;
	}
	public void setFsharerate(String fsharerate) {
		this.fsharerate = fsharerate;
	}
	public String getFproductFeature() {
		return FproductFeature;
	}
	public void setFproductFeature(String fproductFeature) {
		FproductFeature = fproductFeature;
	}
	public String getFtotalunderarea() {
		return ftotalunderarea;
	}
	public void setFtotalunderarea(String ftotalunderarea) {
		this.ftotalunderarea = ftotalunderarea;
	}
	public String getFpropertyparkingplace() {
		return fpropertyparkingplace;
	}
	public void setFpropertyparkingplace(String fpropertyparkingplace) {
		this.fpropertyparkingplace = fpropertyparkingplace;
	}
	public String getFnonpropertyparkingplace() {
		return fnonpropertyparkingplace;
	}
	public void setFnonpropertyparkingplace(String fnonpropertyparkingplace) {
		this.fnonpropertyparkingplace = fnonpropertyparkingplace;
	}
	public String getFirstshop() {
		return firstshop;
	}
	public void setFirstshop(String firstshop) {
		this.firstshop = firstshop;
	}
	public String getSecondshop() {
		return secondshop;
	}
	public void setSecondshop(String secondshop) {
		this.secondshop = secondshop;
	}
	public String getFaddarea() {
		return faddarea;
	}
	public void setFaddarea(String faddarea) {
		this.faddarea = faddarea;
	}
	public String getFhireyears() {
		return fhireyears;
	}
	public void setFhireyears(String fhireyears) {
		this.fhireyears = fhireyears;
	}
	public String getFfirststepunit() {
		return ffirststepunit;
	}
	public void setFfirststepunit(String ffirststepunit) {
		this.ffirststepunit = ffirststepunit;
	}
	public String getFsecondstepunit() {
		return fsecondstepunit;
	}
	public void setFsecondstepunit(String fsecondstepunit) {
		this.fsecondstepunit = fsecondstepunit;
	}
	public String getFhireratio() {
		return fhireratio;
	}
	public void setFhireratio(String fhireratio) {
		this.fhireratio = fhireratio;
	}
	public String getFlandarea() {
		return flandarea;
	}
	public void setFlandarea(String flandarea) {
		this.flandarea = flandarea;
	}
	
}
