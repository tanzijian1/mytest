package nc.bs.master.data.timed.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.alter.result.ConvertResultUtil;
import nc.bs.tg.alter.result.TGInterestMessage;
import nc.bs.tg.outside.salebpm.utils.SyncSaleBPMBillStatesUtils;
import nc.itf.tg.outside.ISaveLogService;
import nc.itf.tg.outside.OutsideUtils;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.vo.tg.masterdata.BlockBuildingInfoVO;
import nc.vo.tg.masterdata.MasterDataBJVO;
import nc.vo.tg.masterdata.MasterDataDJVO;
import nc.vo.tg.masterdata.MasterDataGPVO;
import nc.vo.tg.masterdata.MasterDataSJVO;
import nc.vo.tg.masterdata.MasterDataVO;
import nc.vo.tg.masterdata.MasterDataXFVO;
import nc.vo.tg.masterdata.MasterDataZJVO;
import nc.vo.tg.masterdata.ProjectBlockInfoVO;
import nc.vo.tg.masterdata.ProjectInfoVO;
import nc.vo.tg.outside.OutsideLogVO;

public class MasterDataTimedTask implements IBackgroundWorkPlugin{
	IPFBusiAction pfBusiAction = null;
	/**
	 * ���ݿ�־û�
	 * 
	 * @return
	 */
	private static BaseDAO dao = new BaseDAO();

	public static BaseDAO getDao() {
		return dao;
	}
	@SuppressWarnings("unused")
	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		//��ȡ����ʱ����������
		String title = bgwc.getAlertTypeName();
		String sql = "update sdfn_masterdata set dr = '1' where dr = '0' ";
		getDao().executeUpdate(sql);
		List<Object[]> reflist = new ArrayList<Object[]>();
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new TGInterestMessage());
		Map<String, List<ProjectBlockInfoVO>> pbMap = null;//��Ŀ������Ϣ�ӿ�
		Map<String, List<BlockBuildingInfoVO>> bbMap = null;//¥����Ϣ	
		Map<String, List<ProjectInfoVO>> pMap = null;//��Ŀ��Ϣ�ӿ�

		//���ñ�����־�Ľӿڵ�ַ
		ISaveLogService service = NCLocator.getInstance().lookup(
			    ISaveLogService.class);
		OutsideLogVO logVO = new OutsideLogVO();
		try {
			logVO.setDesbill(bgwc.getAlertTypeName());
			logVO.setSrcsystem("������");
			logVO.setOperator("������");
			pMap = getMainProjectDataPVOs(logVO);//��ȡ������ ��Ŀ��Ϣ
			pbMap = getProjectBlockInfoVOs(logVO);//��ȡ������ ��Ŀ������Ϣ
			bbMap = getBlockBuildingInfoVOs(logVO);//��ȡ������ ¥����Ϣ
			System.out.println("��ȡ��������Ϣ���");
			if(pMap.size()== 0 && pMap == null){
				logVO.setResult("��ȡ����Ŀ��Ϣ�ӿ���ϢΪ��");				
			}else{
				logVO.setSrcparm(pMap.toString());
			}
			if(pbMap.size() == 0 && pbMap == null ){
				logVO.setResult("��ȡ����Ŀ������Ϣ�ӿ���ϢΪ��");
			}else{
				logVO.setSrcparm(pbMap.toString());
			}
			if(bbMap.size() == 0 && bbMap == null){
				logVO.setResult("��ȡ��¥����Ϣ�ӿ���ϢΪ��");
			}else{
				logVO.setSrcparm(bbMap.toString());
			}

			String listvo = null;
			listvo = generateSetVO(pMap,pbMap,bbMap);
			logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
			logVO.setErrmsg(listvo);
		} catch (Exception e1) {
			logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e1).toString());
			e1.getMessage().toString();
			logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);

//			return util.executeTask(title, e1.getMessage());
			
		}finally{
			try {
				service.saveLog_RequiresNew(logVO);
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		
		return util.executeTask(title, reflist);
	}
	
	@SuppressWarnings("unchecked")
	private String generateSetVO(
			Map<String, List<ProjectInfoVO>> pMap,
			Map<String, List<ProjectBlockInfoVO>> pbMap,
			Map<String, List<BlockBuildingInfoVO>> bbMap) throws BusinessException {
		String str = null;
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserId("100112100000000030TI");// ������
		InvocationInfoProxy.getInstance().setUserCode("BPM");
//		InvocationInfoProxy.getInstance().setUserDataSource("nc65_20200618");
		int p = 0;
		for(Entry<String, List<ProjectInfoVO>> entry : pMap.entrySet()){
			try {

				AggMasterDataVO aggvo = new AggMasterDataVO();
				List<ProjectInfoVO> listvo = entry.getValue();
				for(ProjectInfoVO vo :listvo){
					
					MasterDataVO vom = new MasterDataVO();
					String proid = vo.getId();
					vom = getMasterDataVO(vo, vom);
					aggvo.setParentVO(vom);
					
					int f = 0;
					int i = 0;
					/**����*/
					MasterDataXFVO[]  xfvolist = new MasterDataXFVO[pbMap.size()];
					MasterDataZJVO[]  zjvolist = new MasterDataZJVO[bbMap.size()];
					MasterDataDJVO[]  djvolist = new MasterDataDJVO[bbMap.size()];
					MasterDataSJVO[]  sjvolist = new MasterDataSJVO[bbMap.size()];
					MasterDataBJVO[]  bjvolist = new MasterDataBJVO[bbMap.size()];
					MasterDataGPVO[]  gpvolist = new MasterDataGPVO[bbMap.size()];
					for(Entry<String, List<ProjectBlockInfoVO>> pbentry : pbMap.entrySet()){
						
							List<ProjectBlockInfoVO> bplistvo = pbentry.getValue();					
							List<BlockBuildingInfoVO> bblistvo = null;
							for(ProjectBlockInfoVO pbvo : bplistvo){
								if(pbvo.getFprjid().equals(proid)){
									//��Ŀ����
									String name = null;
									if(pbvo.getFname() != null){
										name =  pbvo.getFname();
									}
									MasterDataXFVO xfvo = new MasterDataXFVO();
									xfvo.setDef1(pbvo.getFnumber());//���ڱ��
									if(pbvo.getFname() == null || "null".equals(pbvo.getFname())){
										xfvo.setDef2(null);//��������
									}else{
										xfvo.setDef2(pbvo.getFname());//��������
									}
									if(pbvo.getFproductseries() == null || "null".equals(pbvo.getFproductseries())){
										xfvo.setDef3(null);//��Ʒϵ��
									}else{
										xfvo.setDef3(pbvo.getFproductseries());//��Ʒϵ��
									}
									if(pbvo.getFremark() == null || "null".equals(pbvo.getFremark())){
										xfvo.setDef4(null);//���ڱ�����
									}else{
										xfvo.setDef4(pbvo.getFremark());//���ڱ�����
									}
									if(pbvo.getFdescription() == null || "null".equals(pbvo.getFdescription())){
										xfvo.setDef5(null);//˵��
									}else{
										xfvo.setDef5(pbvo.getFdescription());//˵��
									}
									xfvo.setDr(0);
									xfvo.setStatus(VOStatus.NEW);
									xfvo.setTs(new UFDateTime());
									xfvolist[i] = xfvo;
									i=i+1;
									String id = pbvo.getId();//��Ŀ����ID
									/*xfvo.setDef6(bbvo.getFprdid());//��Ʒ����
									xfvo.setDef7(null);//����/����
									xfvo.setDef8(null);//�������
									xfvo.setDef9(null);//�������
									xfvo.setDef10(null);//���������
									xfvo.setDef11(null);//�������
									xfvo.setDef12(null);//���������
									xfvo.setDef13(null);//�ܿ����
									xfvo.setDef14(null);//�������
									xfvo.setDef15(null);//�뻧�������
									xfvo.setDef16(null);//��ͨסլ���
									xfvo.setDef17(null);//����ͨסլ���
									xfvo.setDef18(null);//��Ԫ��
									xfvo.setDef19(null);//����/����
									xfvo.setDef20(null);//����¥							*/							

								/*	if(f == 1){
										break;
									}*/
									for(Entry<String, List<BlockBuildingInfoVO>> bbentry : bbMap.entrySet()){
											bblistvo = bbentry.getValue();

											/*if(f == 1){
												break;
											}*/
											if(bblistvo != null && bblistvo.size()>0){

												for(BlockBuildingInfoVO bbvo : bblistvo){
													if(bbvo.getFpbid().equals(id)){
														/**סլ������Ϣ*/
														
														MasterDataZJVO zjvo = getMasterDataZJVO(bbvo,name);
														zjvolist[f] = zjvo;
														
														/**��ҵ������Ϣ�ӱ�*/

														MasterDataSJVO sjvo = new MasterDataSJVO();
														sjvo = getMasterDataSJVO(bbvo,
																sjvo,name);
														sjvolist[f] = sjvo;
														
														/**�칫������Ϣ�ӱ�*/
			
														MasterDataBJVO bjvo = new MasterDataBJVO();
														bjvo = getMasterDataBJVO(bbvo,
																bjvo,name);
														bjvolist[f] = bjvo;
														
														/**����������Ϣ�ӱ�*/
														
														MasterDataGPVO gpvo = new MasterDataGPVO();
														gpvo = getMasterDataGPVO(bbvo,
																gpvo,name);
														gpvolist[f] = gpvo;

														/**�����Ҽ�����Ϣ�ӱ�*/
														MasterDataDJVO djvo = new MasterDataDJVO();
														djvo = getMasterDataDJVO(bbvo,
																djvo,name);
														djvolist[f] = djvo;
														f = f+1;
														/*if(f == 1){
															break;
														}	*/												
													}
													
												}
												
											}
											
									}
								}
							}
							
					}
					MasterDataXFVO[] xfvolists = deleteArrayNull(xfvolist);
					aggvo.setChildren(MasterDataXFVO.class,xfvolists );
					MasterDataZJVO[] zjvolists = deleteArrayNull(zjvolist);
					aggvo.setChildren(MasterDataZJVO.class,zjvolists );
					MasterDataSJVO[] sjvolists = deleteArrayNull(sjvolist);
					aggvo.setChildren(MasterDataSJVO.class,sjvolists );
					MasterDataBJVO[] bjvolists = deleteArrayNull(bjvolist);
					aggvo.setChildren(MasterDataBJVO.class,bjvolists );
					MasterDataGPVO[] gpvolists = deleteArrayNull(gpvolist);
					aggvo.setChildren(MasterDataGPVO.class,gpvolists );
					MasterDataDJVO[] djvolists = deleteArrayNull(djvolist);
					aggvo.setChildren(MasterDataDJVO.class,djvolists );
				}			
				str= "�ɹ�";
				@SuppressWarnings("rawtypes")
				HashMap eParam = new HashMap();
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
						PfUtilBaseTools.PARAM_NOTE_CHECKED);
				try{
					@SuppressWarnings("unused")
					AggMasterDataVO[] aggvos = (AggMasterDataVO[]) getPfBusiAction().processAction(
							"SAVEBASE", "SD02", null, aggvo, null, eParam);
				}catch(Exception e){
					str =org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e).toString();
					
					e.printStackTrace();
				}
				p= 1+p;
				if(p == 80){
					System.out.println("11");
				}
				
			
			} catch (Exception e) {
				str = org.apache.commons.lang.exception.ExceptionUtils
				.getFullStackTrace(e).toString();
				e.printStackTrace();
			}
		}

		return str;
	}
	public MasterDataVO getMasterDataVO(ProjectInfoVO vo, MasterDataVO vom)
			throws DAOException {
		vom.setDef23(vo.getId());//��������Ŀ��Ϣ����
		vom.setDef1(vo.getOrg_name());//��Ŀ��˾def23
		vom.setDef2(vo.getFnumber());//��Ŀ����
		if(vo.getFname() == null || "null".equals(vo.getFname())){
			vom.setDef3(null);//��Ŀ����
		}else{
			vom.setDef3(vo.getFname());//��Ŀ����
		}
		if(vo.getFproductseries_name() == null || "null".equals(vo.getFproductseries_name())){
			vom.setDef4(null);//��Ʒϵ��
		}else{
			vom.setDef4(vo.getFproductseries_name());//��Ʒϵ��
		}
		if(vo.getFname1() == null || "null".equals(vo.getFname1())){
			vom.setDef5(null);//������
		}else{
			vom.setDef5(vo.getFname1());//������
		}
		if(vo.getFname2() == null || "null".equals(vo.getFname2())){
			vom.setDef6(null);//Ӫ������
		}else{
			vom.setDef6(vo.getFname2());//Ӫ������
		}
		if(vo.getFregion_name() == null || "null".equals(vo.getFregion_name())){
			vom.setDef7(null);//��������
		}else{
			vom.setDef7(vo.getFregion_name());//��������
		}
		if(vo.getFprojectstatus_cn() == null || "null".equals(vo.getFprojectstatus_cn())){
			vom.setDef8(null);//��Ŀת̬
		}else{
			vom.setDef8(vo.getFprojectstatus_cn());//��Ŀת̬
		}
		if(vo.getFversion() == null || "null".equals(vo.getFversion())){
			vom.setDef9(null);//�汾��
		}else{
			vom.setDef9(vo.getFversion());//�汾��
		}
		if(vo.getCountry_name() == null || "null".equals(vo.getCountry_name())){
			vom.setDef10(null);//����
		}else{
			vom.setDef10(vo.getCountry_name());//����
		}
		if(vo.getProvience_name() == null || "null".equals(vo.getProvience_name())){
			vom.setDef11(null);//ʡ��
		}else{
			vom.setDef11(vo.getProvience_name());//ʡ��
		}
		if(vo.getCity_name() == null || "null".equals(vo.getCity_name())){
			vom.setDef12(null);//����
		}else{
			vom.setDef12(vo.getCity_name());//����
		}
		if(vo.getFaddress() == null || "null".equals(vo.getFaddress())){
			vom.setDef13(null);//��Ŀ��ַ
		}else{
			vom.setDef13(vo.getFaddress());//��Ŀ��ַ
		}
		if(vo.getFplantotalarea() == null || "null".equals(vo.getFplantotalarea())){
			vom.setDef14(null);//�滮���õ����(ƽ����)
		}else{
			vom.setDef14(vo.getFplantotalarea());//�滮���õ����(ƽ����)
		}
		if(vo.getFlandarea() == null || "null".equals(vo.getFlandarea())){
			vom.setDef15(null);//���õ����(ƽ����
		}else{
			vom.setDef15(vo.getFlandarea());//���õ����(ƽ����
		}
		if(vo.getFglossarea() == null || "null".equals(vo.getFglossarea())){
			vom.setDef16(null);//ë�ݻ���
		}else{
			vom.setDef16(vo.getFglossarea());//ë�ݻ���
		}
		if(vo.getFnetarea() == null || "null".equals(vo.getFnetarea())){
			vom.setDef17(null);//���ݻ���
		}else{
			vom.setDef17(vo.getFnetarea());//���ݻ���
		}
		if(vo.getFgreenrate() == null || "null".equals(vo.getFgreenrate())){
			vom.setDef18(null);//�̵���(%)
		}else{
			vom.setDef18(vo.getFgreenrate());//�̵���(%)
		}
		if(vo.getFcontrolhight() == null || "null".equals(vo.getFcontrolhight())){
			vom.setDef19(null);//�����ظ�(��)
		}else{
			vom.setDef19(vo.getFcontrolhight());//�����ظ�(��)
		}
		if(vo.getFglossdensity() == null || "null".equals(vo.getFglossdensity())){
			vom.setDef20(null);//ë�����ܶ�(%)
		}else{
			vom.setDef20(vo.getFglossdensity());//ë�����ܶ�(%)
		}
		if(vo.getFnetdensity() == null || "null".equals(vo.getFnetdensity())){
			vom.setDef21(null);//�������ܶ�(%)
		}else{
			vom.setDef21(vo.getFnetdensity());//�������ܶ�(%)
		}
		if(vo.getFdescription() == null || "null".equals(vo.getFdescription())){
			vom.setBig_text_a(null);//˵��
		}else{
			vom.setBig_text_a(vo.getFdescription());//˵��
		}				
//				panel.setHeadItem("pk_group", pk_group);
//				panel.setHeadItem("pk_org", pk_org);
//				// ���õ���״̬������ҵ������Ĭ��ֵ
//				panel.setHeadItem("approvestatus", BillStatusEnum.FREE.value());
//				panel.setHeadItem("billdate", AppContext.getInstance().getBusiDate());
//				panel.setHeadItem("billtype", "SD02");
		vom.setBilltype("SD02");
		vom.setBilldate(AppContext.getInstance().getBusiDate());			
		String org_name = getSqlgroup(vo.getOrg_name());
		if(org_name != null){
			InvocationInfoProxy.getInstance().setGroupId(org_name);
			vom.setPk_group(org_name);

		}else{
			InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
			vom.setPk_group("000112100000000005FD");
		}
		String pk_org = getSqlorg(vo.getOrg_name());
		if(pk_org != null){
			vom.setPk_org(pk_org);
		}else{//GLOBLE00000000000000
			vom.setPk_org("GLOBLE00000000000000");
		}
		vom.setDr(0);
		vom.setStatus(VOStatus.NEW);
		vom.setTs(new UFDateTime());
		vom.setBilldate(new UFDate());
		vom.setCreationtime(new UFDateTime());
		vom.setApprovestatus(-1);
		return vom;
	}
	/**סլ������Ϣ*/
	public MasterDataZJVO getMasterDataZJVO(BlockBuildingInfoVO bbvo,String name) {
		MasterDataZJVO zjvo = new MasterDataZJVO();
		if(bbvo.getFpbid() == null || "null".equals(bbvo.getFpbid())){
			zjvo.setDef1(null);//��Ŀ����
		}else{
			zjvo.setDef1(name);//��Ŀ����
		}
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			zjvo.setDef2(null);//¥��
		}else{
			zjvo.setDef2(bbvo.getFnumber().toString());//¥��
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			zjvo.setDef3(null);//Ӫ��¥��
		}else{
			zjvo.setDef3(bbvo.getFmarketnumber().toString());//Ӫ��¥��
		}
		if(bbvo.getFunitnumber() == null || "null".equals(bbvo.getFunitnumber())){
			zjvo.setDef4(null);//��Ԫ��
		}else{
			zjvo.setDef4(bbvo.getFunitnumber().toString());//��Ԫ��
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			zjvo.setDef5(null);//���
		}else{
			zjvo.setDef5(bbvo.getFtender().toString());//���
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			zjvo.setDef6(null);//����
		}else{
			zjvo.setDef6(bbvo.getFbuildgroup().toString());//����
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			zjvo.setDef7(null);//��Ʒ����
		}else{
			zjvo.setDef7(bbvo.getFprdid().toString());//��Ʒ����
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			zjvo.setDef8(null);//�����׶�
		}else{
			zjvo.setDef8(bbvo.getFblockstage().toString());//�����׶�
		}
//													zjvo.setDef9(null);//����/����
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			zjvo.setDef10(null);//�������
		}else{
			zjvo.setDef10(bbvo.getFbuildingarea().toString());//�������
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			zjvo.setDef11(null);//�������
		}else{
			zjvo.setDef11(bbvo.getFfactplotarea().toString());//�������
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			zjvo.setDef12(null);//���������
		}else{
			zjvo.setDef12(bbvo.getFfactnonplotarea().toString());//���������
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			zjvo.setDef13(null);//�������
		}else{
			zjvo.setDef13(bbvo.getFsaleablearea().toString());//�������
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			zjvo.setDef14(null);//��������� 
		}else{
			zjvo.setDef14(bbvo.getFnonsaleablearea().toString());//��������� 
		} 
		if(bbvo.getFoperfloorarea() == null || "null".equals(bbvo.getFoperfloorarea())){
			zjvo.setDef15(null);//�ܿ���� ��
		}else{
			zjvo.setDef15(bbvo.getFoperfloorarea().toString());//�ܿ���� ��
		}
		if(bbvo.getFhallarea() == null || "null".equals(bbvo.getFhallarea())){
			zjvo.setDef16(null);//�뻧������� 
		}else{
			zjvo.setDef16(bbvo.getFhallarea().toString());//�뻧������� 
		}
		if(bbvo.getFbuildingbasearea() == null || "null".equals(bbvo.getFbuildingbasearea())){
			zjvo.setDef17(null);//������� ���������ͨסլ����
		}else{
			zjvo.setDef17(bbvo.getFbuildingbasearea().toString());//������� ���������ͨסլ����
		}
		if(bbvo.getFcommonnumber() == null || "null".equals(bbvo.getFcommonnumber())){
			zjvo.setDef18(null);//��ͨסլ���� 
		}else{
			zjvo.setDef18(bbvo.getFcommonnumber().toString());//��ͨסլ���� 
		}
		if(bbvo.getFuncommonnumber() == null || "null".equals(bbvo.getFuncommonnumber())){
			zjvo.setDef19(null);//����ͨסլ���� 
		}else{
			zjvo.setDef19(bbvo.getFuncommonnumber().toString());//����ͨסլ���� 
		}
		if(bbvo.getFroomnumber() == null || "null".equals(bbvo.getFroomnumber())){
			zjvo.setDef20(null);//������ 
		}else{
			zjvo.setDef20(bbvo.getFroomnumber().toString());//������ 
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			zjvo.setDef21(null);//���ϲ���
		}else{
			zjvo.setDef21(bbvo.getFfloor().toString());//���ϲ���
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			zjvo.setDef22(null);//���²���
		}else{
			zjvo.setDef22(bbvo.getFunderfloorcount().toString());//���²���
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			zjvo.setDef23(null);//�����ܸ߶�
		}else{
			zjvo.setDef23(bbvo.getFbuildingheight().toString());//�����ܸ߶�
		}
		if(bbvo.getFinternalarea() == null || "null".equals(bbvo.getFinternalarea())){
			zjvo.setDef24(null);//�������
		}else{
			zjvo.setDef24(bbvo.getFinternalarea().toString());//�������
		}
		if(bbvo.getFapportionarea() == null || "null".equals(bbvo.getFapportionarea())){
			zjvo.setDef25(null);//��̯���
		}else{
			zjvo.setDef25(bbvo.getFapportionarea().toString());//��̯���
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			zjvo.setDef26(null);//����
		}else{
			zjvo.setDef26(bbvo.getFprice().toString());//����
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			zjvo.setDef27(null);//����ṹ��
		}else{
			zjvo.setDef27(bbvo.getFunderfloorcount().toString());//����ṹ��
		}													
		zjvo.setDr(0);
		zjvo.setTs(new UFDateTime());
		return zjvo;
	}
	/**��ҵ������Ϣ�ӱ�*/
	public MasterDataSJVO getMasterDataSJVO(BlockBuildingInfoVO bbvo, MasterDataSJVO sjvo,String name) {
		if(bbvo.getFpbid() == null || "null".equals(bbvo.getFpbid())){
			sjvo.setDef1(null);//��Ŀ����
		}else{
			sjvo.setDef1(name);//��Ŀ����
		}
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			sjvo.setDef2(null);//¥��
		}else{
			sjvo.setDef2(bbvo.getFnumber());//¥��
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			sjvo.setDef3(null);//Ӫ��¥��
		}else{
			sjvo.setDef3(bbvo.getFmarketnumber());//Ӫ��¥��
		}
		if(bbvo.getFunitnumber() == null || "null".equals(bbvo.getFunitnumber())){
			sjvo.setDef4(null);//��Ԫ��
		}else{
			sjvo.setDef4(bbvo.getFunitnumber());//��Ԫ��
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			sjvo.setDef5(null);//���
		}else{
			sjvo.setDef5(bbvo.getFtender());//���
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			sjvo.setDef6(null);//����
		}else{
			sjvo.setDef6(bbvo.getFbuildgroup());//����
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			sjvo.setDef7(null);//��Ʒ����
		}else{
			sjvo.setDef7(bbvo.getFprdid());//��Ʒ����
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			sjvo.setDef8(null);//�����׶�
		}else{
			sjvo.setDef8(bbvo.getFblockstage());//�����׶�
		}
//													sjvo.setDef9(null);//����/����
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			sjvo.setDef10(null);//�������
		}else{
			sjvo.setDef10(bbvo.getFbuildingarea());//�������
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			sjvo.setDef11(null);//�������
		}else{
			sjvo.setDef11(bbvo.getFfactplotarea());//�������
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			sjvo.setDef12(null);//���������
		}else{
			sjvo.setDef12(bbvo.getFfactnonplotarea());//���������
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			sjvo.setDef13(null);//������� 
		}else{
			sjvo.setDef13(bbvo.getFsaleablearea());//������� 
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			sjvo.setDef14(null);//��������� 
		}else{
			sjvo.setDef14(bbvo.getFnonsaleablearea());//��������� 
		}
		if(bbvo.getFoperfloorarea() == null || "null".equals(bbvo.getFoperfloorarea())){
			sjvo.setDef15(null);//�ܿ���� 
		}else{
			sjvo.setDef15(bbvo.getFoperfloorarea());//�ܿ���� 
		}
		if(bbvo.getFbuildingbasearea() == null || "null".equals(bbvo.getFbuildingbasearea())){
			sjvo.setDef22(null);//�������
		}else{
			sjvo.setDef22(bbvo.getFbuildingbasearea());//�������
		}
		if(bbvo.getFhallarea() == null || "null".equals(bbvo.getFhallarea())){
			sjvo.setDef23(null);//�뻧������� 
		}else{
			sjvo.setDef23(bbvo.getFhallarea());//�뻧������� 
		}
		if(bbvo.getFroomnumber() == null || "null".equals(bbvo.getFroomnumber())){
			sjvo.setDef24(null);//������
		}else{
			sjvo.setDef24(bbvo.getFroomnumber());//������
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			sjvo.setDef25(null);//���ϲ���
		}else{
			sjvo.setDef25(bbvo.getFfloor());//���ϲ���
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			sjvo.setDef26(null);//���²��� ������ 
		}else{
			sjvo.setDef26(bbvo.getFunderfloorcount());//���²��� ������ 
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			sjvo.setDef27(null);//�����ܸ߶�
		}else{
			sjvo.setDef27(bbvo.getFbuildingheight());//�����ܸ߶�
		}
		if(bbvo.getFinternalarea() == null || "null".equals(bbvo.getFinternalarea())){
			sjvo.setDef28(null);//�������
		}else{
			sjvo.setDef28(bbvo.getFinternalarea());//�������
		}
		if(bbvo.getFapportionarea() == null || "null".equals(bbvo.getFapportionarea())){
			sjvo.setDef29(null);//��̯���
		}else{
			sjvo.setDef29(bbvo.getFapportionarea());//��̯���
		}
		if(bbvo.getFsharerate() == null || "null".equals(bbvo.getFsharerate())){
			sjvo.setDef30(null);//��̯ϵ��
		}else{
			sjvo.setDef30(bbvo.getFsharerate());//��̯ϵ��
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			sjvo.setDef31(null);//����
		}else{
			sjvo.setDef31(bbvo.getFprice());//����
		}
		if(bbvo.getFirstshop() == null || "null".equals(bbvo.getFirstshop())){
			sjvo.setDef32(null);//�ײ��������
		}else{
			sjvo.setDef32(bbvo.getFirstshop());//�ײ��������
		}
		if(bbvo.getFfirststepunit() == null || "null".equals(bbvo.getFfirststepunit())){
			sjvo.setDef33(null);//�ײ㵥Ԫ��
		}else{
			sjvo.setDef33(bbvo.getFfirststepunit());//�ײ㵥Ԫ��
		}
		if(bbvo.getSecondshop() == null || "null".equals(bbvo.getSecondshop())){
			sjvo.setDef34(null);//���㼰�����������
		}else{
			sjvo.setDef34(bbvo.getSecondshop());//���㼰�����������
		}
		if(bbvo.getFsecondstepunit() == null || "null".equals(bbvo.getFsecondstepunit())){
			sjvo.setDef35(null);//���ײ㵥Ԫ��
		}else{
			sjvo.setDef35(bbvo.getFsecondstepunit());//���ײ㵥Ԫ��
		}
		if(bbvo.getFhireyears() == null || "null".equals(bbvo.getFhireyears())){
			sjvo.setDef36(null);//��������
		}else{
			sjvo.setDef36(bbvo.getFhireyears());//��������
		}
		if(bbvo.getFaddarea() == null || "null".equals(bbvo.getFaddarea())){
			sjvo.setDef37(null);//�ӽ����
		}else{
			sjvo.setDef37(bbvo.getFaddarea());//�ӽ����
		}
		if(bbvo.getFhireratio() == null || "null".equals(bbvo.getFhireratio())){
			sjvo.setDef38(null);//�ӳ�ϵ��
		}else{
			sjvo.setDef38(bbvo.getFhireratio());//�ӳ�ϵ��
		}
		sjvo.setStatus(VOStatus.NEW);
		sjvo.setDr(0);
		sjvo.setTs(new UFDateTime());
		return sjvo;
	}
	/**�칫������Ϣ�ӱ�*/
	public MasterDataBJVO getMasterDataBJVO(BlockBuildingInfoVO bbvo, MasterDataBJVO bjvo,String name) {
		if(bbvo.getFpbid() == null || "null".equals(bbvo.getFpbid())){
			bjvo.setDef1(null);//��Ŀ����
		}else{
			bjvo.setDef1(name);//��Ŀ����
		}
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			bjvo.setDef2(null);//¥��
		}else{
			bjvo.setDef2(bbvo.getFnumber());//¥��
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			bjvo.setDef3(null);//Ӫ��¥��
		}else{
			bjvo.setDef3(bbvo.getFmarketnumber());//Ӫ��¥��
		}
		if(bbvo.getFunitnumber() == null || "null".equals(bbvo.getFunitnumber())){
			bjvo.setDef4(null);//��Ԫ��
		}else{
			bjvo.setDef4(bbvo.getFunitnumber());//��Ԫ��
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			bjvo.setDef5(null);//���
		}else{
			bjvo.setDef5(bbvo.getFtender());//���
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			bjvo.setDef6(null);//����
		}else{
			bjvo.setDef6(bbvo.getFbuildgroup());//����
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			bjvo.setDef7(null);//��Ʒ����
		}else{
			bjvo.setDef7(bbvo.getFprdid());//��Ʒ����
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			bjvo.setDef8(null);//�����׶�
		}else{
			bjvo.setDef8(bbvo.getFblockstage());//�����׶�
		}
//													bjvo.setDef9(null);//����/����
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			bjvo.setDef10(null);//�������
		}else{
			bjvo.setDef10(bbvo.getFbuildingarea());//�������
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			bjvo.setDef11(null);//�������
		}else{
			bjvo.setDef11(bbvo.getFfactplotarea());//�������
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			bjvo.setDef12(null);//���������
		}else{
			bjvo.setDef12(bbvo.getFfactnonplotarea());//���������
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			bjvo.setDef13(null);//������� 
		}else{
			bjvo.setDef13(bbvo.getFsaleablearea());//������� 
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			bjvo.setDef14(null);//��������� 
		}else{
			bjvo.setDef14(bbvo.getFnonsaleablearea());//��������� 
		}
		if(bbvo.getFoperfloorarea() == null || "null".equals(bbvo.getFoperfloorarea())){
			bjvo.setDef15(null);//�ܿ���� 
		}else{
			bjvo.setDef15(bbvo.getFoperfloorarea());//�ܿ���� 
		}
		if(bbvo.getFhallarea() == null || "null".equals(bbvo.getFhallarea())){
			bjvo.setDef16(null);//�뻧�������
		}else{
			bjvo.setDef16(bbvo.getFhallarea());//�뻧�������
		}
		if(bbvo.getFproductFeature() == null || "null".equals(bbvo.getFproductFeature())){
			bjvo.setDef17(null);//���ܷ���
		}else{
			bjvo.setDef17(bbvo.getFproductFeature());//���ܷ���
		}
		if(bbvo.getFroomnumber() == null || "null".equals(bbvo.getFroomnumber())){
			bjvo.setDef18(null);//������
		}else{
			bjvo.setDef18(bbvo.getFroomnumber());//������
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			bjvo.setDef19(null);//���ϲ���
		}else{
			bjvo.setDef19(bbvo.getFfloor());//���ϲ���
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			bjvo.setDef20(null);//���²���
		}else{
			bjvo.setDef20(bbvo.getFunderfloorcount());//���²���
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			bjvo.setDef21(null);//�����ܸ߶�
		}else{
			bjvo.setDef21(bbvo.getFbuildingheight());//�����ܸ߶�
		}
		if(bbvo.getFinternalarea() == null || "null".equals(bbvo.getFinternalarea())){
			bjvo.setDef22(null);//�������
		}else{
			bjvo.setDef22(bbvo.getFinternalarea());//�������
		}
		if(bbvo.getFapportionarea() == null || "null".equals(bbvo.getFapportionarea())){
			bjvo.setDef23(null);//��̯���
		}else{
			bjvo.setDef23(bbvo.getFapportionarea());//��̯���
		}
		if(bbvo.getFsharerate() == null || "null".equals(bbvo.getFsharerate())){
			bjvo.setDef24(null);//��̯ϵ��
		}else{
			bjvo.setDef24(bbvo.getFsharerate());//��̯ϵ��
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			bjvo.setDef25(null);//����
		}else{
			bjvo.setDef25(bbvo.getFprice());//����
		}
		if(bbvo.getFirstshop() == null || "null".equals(bbvo.getFirstshop())){
			bjvo.setDef26(null);//�ײ��������
		}else{
			bjvo.setDef26(bbvo.getFirstshop());//�ײ��������
		}
		if(bbvo.getFfirststepunit() == null || "null".equals(bbvo.getFfirststepunit())){
			bjvo.setDef27(null);//�ײ㵥Ԫ��
		}else{
			bjvo.setDef27(bbvo.getFfirststepunit());//�ײ㵥Ԫ��
		}
		if(bbvo.getSecondshop() == null || "null".equals(bbvo.getSecondshop())){
			bjvo.setDef28(null);//���㼰�����������
		}else{
			bjvo.setDef28(bbvo.getSecondshop());//���㼰�����������
		}
		if(bbvo.getFsecondstepunit() == null || "null".equals(bbvo.getFsecondstepunit())){
			bjvo.setDef29(null);//���ײ㵥Ԫ��
		}else{
			bjvo.setDef29(bbvo.getFsecondstepunit());//���ײ㵥Ԫ��
		}
		if(bbvo.getFhireyears() == null || "null".equals(bbvo.getFhireyears())){
			bjvo.setDef30(null);//��������
		}else{
			bjvo.setDef30(bbvo.getFhireyears());//��������
		}
		if(bbvo.getFaddarea() == null || "null".equals(bbvo.getFaddarea())){
			bjvo.setDef31(null);//�ӽ����
		}else{
			bjvo.setDef31(bbvo.getFaddarea());//�ӽ����
		}
		if(bbvo.getFhireratio() == null || "null".equals(bbvo.getFhireratio())){
			bjvo.setDef32(null);//�ӳ�ϵ�� 
		}else{
			bjvo.setDef32(bbvo.getFhireratio());//�ӳ�ϵ�� 
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			bjvo.setDef33(null);//����ṹ
		}else{
			bjvo.setDef33(bbvo.getFunderfloorcount());//����ṹ
		}
		bjvo.setStatus(VOStatus.NEW);
		bjvo.setDr(0);
		bjvo.setTs(new UFDateTime());
		return bjvo;
	}
	/**����������Ϣ�ӱ�*/
	public MasterDataGPVO getMasterDataGPVO(BlockBuildingInfoVO bbvo, MasterDataGPVO gpvo,String name) {
		gpvo.setDef1(name);//��Ŀ����
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			gpvo.setDef2(null);//¥��
		}else{
			gpvo.setDef2(bbvo.getFnumber().toString());//¥��
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			gpvo.setDef3(null);//Ӫ��¥��
		}else{
			gpvo.setDef3(bbvo.getFmarketnumber());//Ӫ��¥��]
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			gpvo.setDef4(null);//���
		}else{
			gpvo.setDef4(bbvo.getFtender());//���
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			gpvo.setDef5(null);//����
		}else{
			gpvo.setDef5(bbvo.getFbuildgroup());//����
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			gpvo.setDef6(null);//��Ʒ����
		}else{
			gpvo.setDef6(bbvo.getFprdid());//��Ʒ����
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			gpvo.setDef7(null);//�����׶�
		}else{
			gpvo.setDef7(bbvo.getFblockstage());//�����׶�
		}
//													gpvo.setDef8(null);//����/����
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			gpvo.setDef9(null);//�������
		}else{
			gpvo.setDef9(bbvo.getFbuildingarea());//�������
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			gpvo.setDef10(null);//�������
		}else{
			gpvo.setDef10(bbvo.getFfactplotarea());//�������
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			gpvo.setDef11(null);//���������
		}else{
			gpvo.setDef11(bbvo.getFfactnonplotarea());//���������
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			gpvo.setDef12(null);//�������
		}else{
			gpvo.setDef12(bbvo.getFsaleablearea());//�������
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			gpvo.setDef13(null);//���������
		}else{
			gpvo.setDef13(bbvo.getFnonsaleablearea());//���������
		}
		if(bbvo.getFoperfloorarea() == null || "null".equals(bbvo.getFoperfloorarea())){
			gpvo.setDef14(null);//�ܿ����
		}else{
			gpvo.setDef14(bbvo.getFoperfloorarea());//�ܿ����
		}
		if(bbvo.getFbuildingbasearea() == null || "null".equals(bbvo.getFbuildingbasearea())){
			gpvo.setDef15(null);//�������
		}else{
			gpvo.setDef15(bbvo.getFbuildingbasearea());//�������
		}
		if(bbvo.getFhallarea() == null || "null".equals(bbvo.getFhallarea())){
			gpvo.setDef16(null);//�뻧�������
		}else{
			gpvo.setDef16(bbvo.getFhallarea());//�뻧�������
		}
		if(bbvo.getFroomnumber() == null || "null".equals(bbvo.getFroomnumber())){
			gpvo.setDef17(null);//������
		}else{
			gpvo.setDef17(bbvo.getFroomnumber());//������
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			gpvo.setDef18(null);//���ϲ���
		}else{
			gpvo.setDef18(bbvo.getFfloor());//���ϲ���
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			gpvo.setDef19(null);//���²���
		}else{
			gpvo.setDef19(bbvo.getFunderfloorcount());//���²���
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			gpvo.setDef20(null);//�����ܸ߶�
		}else{
			gpvo.setDef20(bbvo.getFbuildingheight());//�����ܸ߶�
		}
		if(bbvo.getFinternalarea() == null || "null".equals(bbvo.getFinternalarea())){
			gpvo.setDef21(null);//������� 
		}else{
			gpvo.setDef21(bbvo.getFinternalarea());//������� 
		}
		if(bbvo.getFapportionarea() == null || "null".equals(bbvo.getFapportionarea())){
			gpvo.setDef22(null);//��̯���
		}else{
			gpvo.setDef22(bbvo.getFapportionarea());//��̯���
		}
		if(bbvo.getFsharerate() == null || "null".equals(bbvo.getFsharerate())){
			gpvo.setDef23(null);//��̯ϵ��
		}else{
			gpvo.setDef23(bbvo.getFsharerate());//��̯ϵ��
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			gpvo.setDef24(null);//����
		}else{
			gpvo.setDef24(bbvo.getFprice());//����
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			gpvo.setDef25(null);//����ṹ	
		}else{
			gpvo.setDef25(bbvo.getFunderfloorcount());//����ṹ	
		}
		gpvo.setStatus(VOStatus.NEW);
		gpvo.setDr(0);
		gpvo.setTs(new UFDateTime());
		return gpvo;
	}
	/**�����Ҽ�����Ϣ�ӱ�*/
	public MasterDataDJVO getMasterDataDJVO(BlockBuildingInfoVO bbvo, MasterDataDJVO djvo,String name) {
		djvo.setDef1(name);//��Ŀ����
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			djvo.setDef2(null);//¥��
		}else{
			djvo.setDef2(bbvo.getFnumber().toString());//¥��
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			djvo.setDef3(null);//Ӫ��¥��
		}else{
			djvo.setDef3(bbvo.getFmarketnumber());//Ӫ��¥��
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			djvo.setDef4(null);//���
		}else{
			djvo.setDef4(bbvo.getFtender());//���
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			djvo.setDef5(null);//����
		}else{
			djvo.setDef5(bbvo.getFbuildgroup());//����
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			djvo.setDef6(null);//��Ʒ����
		}else{
			djvo.setDef6(bbvo.getFprdid());//��Ʒ����
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			djvo.setDef7(null);//�����׶�
		}else{
			djvo.setDef7(bbvo.getFblockstage());//�����׶�
		}
//													djvo.setDef8(null);//����/���� 
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			djvo.setDef9(null);//�������
		}else{
			djvo.setDef9(bbvo.getFbuildingarea());//�������
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			djvo.setDef10(null);//�������
		}else{
			djvo.setDef10(bbvo.getFfactplotarea());//�������
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			djvo.setDef11(null);//���������
		}else{
			djvo.setDef11(bbvo.getFfactnonplotarea());//���������
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			djvo.setDef12(null);//�������
		}else{
			djvo.setDef12(bbvo.getFsaleablearea());//�������
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			djvo.setDef13(null);//���������
		}else{
			djvo.setDef13(bbvo.getFnonsaleablearea());//���������
		}
		djvo.setDef14(null);//�ܳ�λ����
		djvo.setDef15(null);//�˷���λ����
		djvo.setDef16(null);//���˷���λ����
		if(bbvo.getFpropertyparkingplace() == null || "null".equals(bbvo.getFpropertyparkingplace())){
			djvo.setDef17(null);//��Ȩ��λ����
		}else{
			djvo.setDef17(bbvo.getFpropertyparkingplace());//��Ȩ��λ����
		}
		if(bbvo.getFnonpropertyparkingplace() == null || "null".equals(bbvo.getFnonpropertyparkingplace())){
			djvo.setDef18(null);//�޲�Ȩ��λ����
		}else{
			djvo.setDef18(bbvo.getFnonpropertyparkingplace());//�޲�Ȩ��λ����
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			djvo.setDef19(null);//����
		}else{
			djvo.setDef19(bbvo.getFfloor());//����
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			djvo.setDef20(null);//�����ܸ߶�
		}else{
			djvo.setDef20(bbvo.getFbuildingheight());//�����ܸ߶�
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			djvo.setDef21(null);//����
		}else{
			djvo.setDef21(bbvo.getFprice());//����
		}
		if(bbvo.getFlandarea() == null || "null".equals(bbvo.getFlandarea())){
			djvo.setDef22(null);//ռ�����
		}else{
			djvo.setDef22(bbvo.getFlandarea());//ռ�����
		}
		if(bbvo.getFtotalunderarea() == null || "null".equals(bbvo.getFtotalunderarea())){
			djvo.setDef23(null);//��������
		}else{
			djvo.setDef23(bbvo.getFtotalunderarea());//��������
		}
		if(bbvo.getFtotalunderarea() == null || "null".equals(bbvo.getFtotalunderarea())){
			djvo.setDef24(null);//�滮�����
		}else{
			djvo.setDef24(bbvo.getFtotalunderarea());//�滮�����
		}
		if(bbvo.getFsharerate() == null || "null".equals(bbvo.getFsharerate())){
			djvo.setDef25(null);//��̯ϵ��
		}else{
			djvo.setDef25(bbvo.getFsharerate());//��̯ϵ��
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			djvo.setDef26(null);//����ܹ�
		}else{
			djvo.setDef26(bbvo.getFunderfloorcount());//����ܹ�
		}
		djvo.setStatus(VOStatus.NEW);
		djvo.setDr(0);
		djvo.setTs(new UFDateTime());
		return djvo;
	}
	/**���˿�ֵ�����Ҽ���  */
	private MasterDataDJVO[] deleteArrayNull(MasterDataDJVO[] djvolist) {
		MasterDataDJVO[] kk = null;
		List<MasterDataDJVO> list = new ArrayList<MasterDataDJVO>();
		for(MasterDataDJVO vof : djvolist){
			if(vof != null && !"".equals(vof)){
				list.add(vof);
			}
		}
		kk = list.toArray(new MasterDataDJVO[list.size()]);
		return kk;	
	}
	/**���˿�ֵ�������� */
	private MasterDataGPVO[] deleteArrayNull(MasterDataGPVO[] gpvolist) {
		MasterDataGPVO[] kk = null;
		List<MasterDataGPVO> list = new ArrayList<MasterDataGPVO>();
		for(MasterDataGPVO vof : gpvolist){
			if(vof != null && !"".equals(vof)){
				list.add(vof);
			}
		}
		kk = list.toArray(new MasterDataGPVO[list.size()]);
		return kk;	
	}
	/**���˿�ֵ�칫���� */
	private MasterDataBJVO[] deleteArrayNull(MasterDataBJVO[] bjvolist) {
		MasterDataBJVO[] kk = null;
		List<MasterDataBJVO> list = new ArrayList<MasterDataBJVO>();
		for(MasterDataBJVO vof : bjvolist){
			if(vof != null && !"".equals(vof)){
				list.add(vof);
			}
		}
		kk = list.toArray(new MasterDataBJVO[list.size()]);
		return kk;	
	}
	/**���˿�ֵ��ҵ���� */
	private MasterDataSJVO[] deleteArrayNull(MasterDataSJVO[] sjvolist) {
		MasterDataSJVO[] kk = null;
		List<MasterDataSJVO> list = new ArrayList<MasterDataSJVO>();
		for(MasterDataSJVO vof : sjvolist){
			if(vof != null && !"".equals(vof)){
				list.add(vof);
			}
		}
		kk = list.toArray(new MasterDataSJVO[list.size()]);
		return kk;	
	}
	/**���˿�ֵסլ���� */
	private MasterDataZJVO[] deleteArrayNull(MasterDataZJVO[] zjvolist) {
		MasterDataZJVO[] kk = null;
		List<MasterDataZJVO> list = new ArrayList<MasterDataZJVO>();;
		for(MasterDataZJVO vof : zjvolist){
			if(vof != null && !"".equals(vof)){
				list.add(vof);
			}
		}
		kk=(MasterDataZJVO[]) list.toArray(new MasterDataZJVO[list.size()]);
		return kk;	
	}
	/**���˿�ֵ��Ŀ����*/
	public MasterDataXFVO[] deleteArrayNull(MasterDataXFVO [] xfvolist) {
		MasterDataXFVO[] kk = null;
		List<MasterDataXFVO> list = new ArrayList<MasterDataXFVO>();
		for(MasterDataXFVO vof : xfvolist){
			if(vof != null && !"".equals(vof)){
				list.add(vof);
			}
		}
		kk = list.toArray(new MasterDataXFVO[list.size()]);
		return kk;	
	}
	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}
	private String getSqlorg(String org_name) throws DAOException {
		String org = null;
		String sqlorg = "select org_orgs.pk_org from org_orgs where org_orgs.name = '"+org_name+"' and rownum < 1";
		org = (String) getDao().executeQuery(sqlorg, new ColumnProcessor());
		return org;
	}
	private String getSqlgroup(String org_name) throws DAOException {
		String group = null;
		String sqlgroup = "select org_orgs.pk_group from org_orgs where org_orgs.name = '"+org_name+"' and rownum < 1";
		group = (String) getDao().executeQuery(sqlgroup, new ColumnProcessor());
		return group;
	}
	private Map<String, List<BlockBuildingInfoVO>> getBlockBuildingInfoVOs(
			OutsideLogVO logVO) throws Exception{
		// http://times-uat.timesgroup.cn:8080/method?code=projectInfo&currpage=1&syscode=nc&token=9087698DEA1E3BD838762D84373733F0

		// PropertiesUtils util = PropertiesUtils
		// .getInstance("main_url.properties");
		String mianurl = OutsideUtils.getOutsideInfo("DATA01");//
		String syscode = OutsideUtils.getOutsideInfo("DATA02");//
		String token = OutsideUtils.getOutsideInfo("DATA03");//

//		String urlstr = mianurl + "?code=blockBuildingInfo&currpage=1&syscode=" + syscode + "&token="
//				+ initToken(token);

		Map<String, List<BlockBuildingInfoVO>> bodylists = new HashMap<String, List<BlockBuildingInfoVO>>();
		// �������������л��з�ҳ��ѯ,Ϊȷ����������,�ʴ˴�ʹ��ѭ����ȡ��ҳ,һ����ȡ�ɹ����ҷ��ص�dataΪ��������ѭ��
		for (int i = 1; i < 99999; i++) {
			// String urls = util.readValue("MIANURL")
			// +"&currpage="+i+""
			// +"&syscode="+util.readValue("SYSCODE")
			// +"&token="+initElementInv();
			String urls = mianurl + "?code=blockBuildingInfo&currpage="+i+"&syscode=" + syscode + "&token="
					+ initToken(token);
			// ����url��Դ
			URL url = new URL(urls);
			// ����http����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// �����������
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// ���ò��û���
			conn.setUseCaches(false);
			// ���ô��ݷ�ʽ
			conn.setRequestMethod("POST");
			// ����ά�ֳ�����
			conn.setRequestProperty("Connection", "Keep-Alive");
			// �����ļ��ַ���:
			conn.setRequestProperty("Charset", "utf-8");
			// // ת��Ϊ�ֽ�����
			// byte[] data = json.getBytes("utf-8");
			// �����ļ�����
			conn.setRequestProperty("Content-Length",
					String.valueOf(urls.length()));

			// �����ļ�����:
			conn.setRequestProperty("Content-Type", "application/json");

			// ��ʼ��������
			conn.connect();
			String a = "";

			if (conn.getResponseCode() == 200) {
				Logger.error("���ӳɹ�");
				// ���󷵻ص�����
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				String line = null;
				while ((line = in.readLine()) != null) {
					a += line;
				}
				JSONObject result = new JSONObject(a);
				String flag = (String) result.getString("code");
				String errmsg = null;
				if (flag.equals("S")) {
					if (result.getJSONArray("data") == null
							|| result.getJSONArray("data").length() <= 0) {
						logVO.setSrcparm("��" + i + "����" + result.toString());
						break;
					}
					JSONArray resMap = result.getJSONArray("data");
					net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
							.fromObject(resMap.toString());
					@SuppressWarnings({ "unchecked", "rawtypes" })
					List<Map<String, Object>> mapListJson = (List) jsonArray;
					for (Map<String, Object> map : mapListJson) {
						if (!bodylists.containsKey(map.get("ID"))) {
							bodylists.put(map.get("ID").toString(),
									new ArrayList<BlockBuildingInfoVO>());
						}
						BlockBuildingInfoVO pvo = new BlockBuildingInfoVO();
						if(map.get("ID") == null){
							pvo.setAttributeValue("id", "");// ��Ŀid
						}else{
							pvo.setAttributeValue("id", map.get("ID").toString());// ��Ŀid
						}
						pvo.setAttributeValue("fpbid", map.get("FPBID").toString());//��Ŀ���ڣ�������WBD_ProjectBlock_T
						if(map.get("FNUMBER") == null){
							pvo.setAttributeValue("fnumber", "");//¥����
						}else{
							pvo.setAttributeValue("fnumber", map.get("FNUMBER").toString());//¥����
						}
						if(map.get("FMARKETNUMBER") == null){
							pvo.setAttributeValue("fmarketnumber", "");//Ӫ��¥��
						}else{
							pvo.setAttributeValue("fmarketnumber", map.get("FMARKETNUMBER").toString());//Ӫ��¥��
						}
						if(map.get("FPRDID") == null){
							pvo.setAttributeValue("fprdid", "");//��Ʒ����ID��������WBD_Produce_T(����ҵ̬)
						}else{
							pvo.setAttributeValue("fprdid", map.get("FPRDID").toString());//��Ʒ����ID��������WBD_Produce_T(����ҵ̬)
						}
						if(map.get("FTENDER") == null){
							pvo.setAttributeValue("ftender", "");//���
						}else{
							pvo.setAttributeValue("ftender", map.get("FTENDER").toString());//���
						}
						if(map.get("FSALEABLE") == null){
							pvo.setAttributeValue("fsaleable", "");//�Ƿ���ۣ�1 = ���ۡ�0 = ������
						}else{
							pvo.setAttributeValue("fsaleable", map.get("FSALEABLE").toString());//�Ƿ���ۣ�1 = ���ۡ�0 = ������
						}
						if(map.get("FBUILDINGAREA") == null){
							pvo.setAttributeValue("fbuildingarea", "");//�������
						}else{
							pvo.setAttributeValue("fbuildingarea", map.get("FBUILDINGAREA").toString());//�������
						}
						if(map.get("FFACTPLOTAREA") == null){
							pvo.setAttributeValue("ffactplotarea", "");//�������
						}else{
							pvo.setAttributeValue("ffactplotarea", map.get("FFACTPLOTAREA").toString());//�������
						}
						if(map.get("FFACTNONPLOTAREA") == null){
							pvo.setAttributeValue("ffactnonplotarea", "");//���������
						}else{
							pvo.setAttributeValue("ffactnonplotarea", map.get("FFACTNONPLOTAREA").toString());//���������
						}
						if(map.get("FSALEABLEAREA") == null){
							pvo.setAttributeValue("fsaleablearea", "");//�������
						}else{
							pvo.setAttributeValue("fsaleablearea", map.get("FSALEABLEAREA").toString());//�������
						}
						if(map.get("FNONSALEABLEAREA") != null){
							pvo.setAttributeValue("fnonsaleablearea", "");//���������
						}else{
							pvo.setAttributeValue("fnonsaleablearea", map.get("FNONSALEABLEAREA").toString());//���������
						}
						if(map.get("FOPERFLOORAREA") == null){
							pvo.setAttributeValue("foperfloorarea", "");//�ܿ����
						}else{
							pvo.setAttributeValue("foperfloorarea", map.get("FOPERFLOORAREA").toString());//�ܿ����
						}
						if(map.get("FHALLAREA") == null){
							pvo.setAttributeValue("fhallarea", "");//�뻧�������
						}else{
							pvo.setAttributeValue("fhallarea", map.get("FHALLAREA").toString());//�뻧�������
						}
						if(map.get("FBUILDINGBASEAREA") == null){
							pvo.setAttributeValue("fbuildingbasearea", "");//�������
						}else{
							pvo.setAttributeValue("fbuildingbasearea", map.get("FBUILDINGBASEAREA").toString());//�������
						}
						if(map.get("FCOMMONNUMBER") == null){
							pvo.setAttributeValue("fcommonnumber", "");//��ͨסլ����
						}else {
							pvo.setAttributeValue("fcommonnumber", map.get("FCOMMONNUMBER").toString());//��ͨסլ����
						}
						if(map.get("FUNCOMMONNUMBER") == null){
							pvo.setAttributeValue("funcommonnumber", "");//����ͨסլ����
						}else{
							pvo.setAttributeValue("funcommonnumber", map.get("FUNCOMMONNUMBER").toString());//����ͨסլ����
						}
						if(map.get("FUNITNUMBER") == null){
							pvo.setAttributeValue("funitnumber","");//��Ԫ��
						}else{
							pvo.setAttributeValue("funitnumber", map.get("FUNITNUMBER").toString());//��Ԫ��
						}
						if(map.get("FROOMNUMBER") == null){
							pvo.setAttributeValue("froomnumber", "");//������
						}else{
							pvo.setAttributeValue("froomnumber", map.get("FROOMNUMBER").toString());//������
						}
						if(map.get("FFLOOR") == null){
							pvo.setAttributeValue("ffloor", "");//����-���ϲ���
						}else{
							pvo.setAttributeValue("ffloor", map.get("FFLOOR").toString());//����-���ϲ���
						}
						if(map.get("FBUILDINGHEIGHT") == null){
							pvo.setAttributeValue("fbuildingheight", "");//�����ܸ߶�
						}else{
							pvo.setAttributeValue("fbuildingheight", map.get("FBUILDINGHEIGHT").toString());//�����ܸ߶�
						}
						if(map.get("FINTERNALAREA") == null){
							pvo.setAttributeValue("finternalarea", "");//�������
						}else{
							pvo.setAttributeValue("finternalarea", map.get("FINTERNALAREA").toString());//�������
						}
						if(map.get("FAPPORTIONAREA") == null){
							pvo.setAttributeValue("fapportionarea", "");//��̯���
						}else{
							pvo.setAttributeValue("fapportionarea", map.get("FAPPORTIONAREA").toString());//��̯���
						}
						if(map.get("FBLOCKSTAGE") == null){
							pvo.setAttributeValue("fblockstage", "");//�����׶Σ����룺WBD_BlockStage
						}else{
							pvo.setAttributeValue("fblockstage", map.get("FBLOCKSTAGE").toString());//�����׶Σ����룺WBD_BlockStage
						}
						if(map.get("FBUILDGROUP") == null){
							pvo.setAttributeValue("fbuildgroup", "");//���� �����WBD_BuildGroup
						}else{
							pvo.setAttributeValue("fbuildgroup", map.get("FBUILDGROUP").toString());//���� �����WBD_BuildGroup
						}
						if(map.get("FTOTALUNDERAREA") == null){
							pvo.setAttributeValue("ftotalunderarea", "");//���������������ʱ��ʹ�ã����͹滮�����
						}else{
							pvo.setAttributeValue("ftotalunderarea", map.get("FTOTALUNDERAREA").toString());//����
						}
						if(map.get("FPROPERTYPARKINGPLACE") == null){
							pvo.setAttributeValue("fpropertyparkingplace", "");//��Ȩ��λ����������ʹ�ã���Ȩ��λ����
						}else{
							pvo.setAttributeValue("fpropertyparkingplace", map.get("FPROPERTYPARKINGPLACE").toString());//����
						}
						if(map.get("FNONPROPERTYPARKINGPLACE") == null){
							pvo.setAttributeValue("fnonpropertyparkingplace", "");//�ǲ�Ȩ��λ����������ʹ�ã��޲�Ȩ��λ����
						}else{
							pvo.setAttributeValue("fnonpropertyparkingplace", map.get("FNONPROPERTYPARKINGPLACE").toString());//����
						}
						if(map.get("FPRICE") == null){
							pvo.setAttributeValue("fprice", "");//����
						}else{
							pvo.setAttributeValue("fprice", map.get("FPRICE").toString());//����
						}
						if(map.get("FUNDERFLOORCOUNT") == null){
							pvo.setAttributeValue("funderfloorcount", "");//���²���
						}else{
							pvo.setAttributeValue("funderfloorcount", map.get("FUNDERFLOORCOUNT").toString());//���²���
						}
						if(map.get("FIRSTSHOP") == null){
							pvo.setAttributeValue("firstshop", "");//�ײ��������
						}else{
							pvo.setAttributeValue("firstshop", map.get("FIRSTSHOP").toString());//����ṹ
						}
						if(map.get("SECONDSHOP") == null){
							pvo.setAttributeValue("secondshop", "");//���㼰�����������
						}else{
							pvo.setAttributeValue("secondshop", map.get("SECONDSHOP").toString());//����ṹ
						}
						if(map.get("FADDAREA") == null){
							pvo.setAttributeValue("faddarea", "");//�ӽ����
						}else{
							pvo.setAttributeValue("faddarea", map.get("FADDAREA").toString());//����ṹ
						}
						if(map.get("FHIREYEARS") == null){
							pvo.setAttributeValue("fhireyears", "");//��������
						}else{
							pvo.setAttributeValue("fhireyears", map.get("FHIREYEARS").toString());//����ṹ
						}
						if(map.get("FFIRSTSTEPUNIT") == null){
							pvo.setAttributeValue("ffirststepunit", "");//�ײ㵥Ԫ��
						}else{
							pvo.setAttributeValue("ffirststepunit", map.get("FFIRSTSTEPUNIT").toString());//����ṹ
						}
						if(map.get("FSECONDSTEPUNIT") == null){
							pvo.setAttributeValue("fsecondstepunit", "");//�������ϵ�Ԫ��
						}else{
							pvo.setAttributeValue("fsecondstepunit", map.get("FSECONDSTEPUNIT").toString());//����ṹ
						}
						if(map.get("FHIRERATIO") == null){
							pvo.setAttributeValue("fhireratio", "");//���޼ӳ�ϵ�� �ӳ�ϵ��
						}else{
							pvo.setAttributeValue("fhireratio", map.get("FHIRERATIO").toString());//����ṹ
						}
						if(map.get("FLANDAREA") == null){
							pvo.setAttributeValue("flandarea", "");//ռ�����
						}else{
							pvo.setAttributeValue("flandarea", map.get("FLANDAREA").toString());//����ṹ
						}
						if(map.get("FMAINSTRUCTURE") == null){
							pvo.setAttributeValue("fmainstructure", "");//����ṹ
						}else{
							pvo.setAttributeValue("fmainstructure", map.get("FMAINSTRUCTURE").toString());//����ṹ
						}
						if(map.get("FSHARERATE") == null){
							pvo.setAttributeValue("fsharerate", "");//��̯ϵ��
						}else{
							pvo.setAttributeValue("fsharerate", map.get("FSHARERATE").toString());//��̯ϵ��
						}
						if(map.get("FPRODUCTFEATURE") == null){
							pvo.setAttributeValue("FproductFeature", "");//������������(ָ�꿨)��ҵ̬��
						}else{
							pvo.setAttributeValue("FproductFeature", map.get("FPRODUCTFEATURE").toString());//������������(ָ�꿨)��ҵ̬��
						}
//						pvo.setAttributeValue("dr", new Integer(0));
//						pvo.setStatus(VOStatus.NEW);
						bodylists.get(map.get("ID").toString()).add(pvo);
					}
				} else {
					errmsg = (String) result.getString("msg");
					throw new BusinessException("�����������ݵĴ�����Ϣ��" + errmsg + "��");
				}
			} else {
				Logger.error("����ʧ��");
				throw new BusinessException("����ʧ��");
			}
		}
		return bodylists;
	}
	private Map<String, List<ProjectBlockInfoVO>> getProjectBlockInfoVOs(
			OutsideLogVO logVO) throws Exception{
		// http://times-uat.timesgroup.cn:8080/method?code=projectInfo&currpage=1&syscode=nc&token=9087698DEA1E3BD838762D84373733F0

		// PropertiesUtils util = PropertiesUtils
		// .getInstance("main_url.properties");
		String mianurl = OutsideUtils.getOutsideInfo("DATA01");//
		String syscode = OutsideUtils.getOutsideInfo("DATA02");//
		String token = OutsideUtils.getOutsideInfo("DATA03");//

//		String urlstr = mianurl + "?code=projectBlockInfo&currpage=1&syscode=" + syscode + "&token="
//				+ initToken(token);

		Map<String, List<ProjectBlockInfoVO>> bodylists = new HashMap<String, List<ProjectBlockInfoVO>>();
		// �������������л��з�ҳ��ѯ,Ϊȷ����������,�ʴ˴�ʹ��ѭ����ȡ��ҳ,һ����ȡ�ɹ����ҷ��ص�dataΪ��������ѭ��
		for (int i = 1; i < 99999; i++) {
			// String urls = util.readValue("MIANURL")
			// +"&currpage="+i+""
			// +"&syscode="+util.readValue("SYSCODE")
			// +"&token="+initElementInv();
			String urls = mianurl + "?code=projectBlockInfo&currpage="+i+"&syscode=" + syscode + "&token="
					+ initToken(token);
			// ����url��Դ
			URL url = new URL(urls);
			// ����http����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// �����������
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// ���ò��û���
			conn.setUseCaches(false);
			// ���ô��ݷ�ʽ
			conn.setRequestMethod("POST");
			// ����ά�ֳ�����
			conn.setRequestProperty("Connection", "Keep-Alive");
			// �����ļ��ַ���:
			conn.setRequestProperty("Charset", "utf-8");
			// // ת��Ϊ�ֽ�����
			// byte[] data = json.getBytes("utf-8");
			// �����ļ�����
			conn.setRequestProperty("Content-Length",
					String.valueOf(urls.length()));

			// �����ļ�����:
			conn.setRequestProperty("Content-Type", "application/json");

			// ��ʼ��������
			conn.connect();
			String a = "";

			if (conn.getResponseCode() == 200) {
				Logger.error("���ӳɹ�");
				// ���󷵻ص�����
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				String line = null;
				while ((line = in.readLine()) != null) {
					a += line;
				}
				JSONObject result = new JSONObject(a);
				String flag = (String) result.getString("code");
				String errmsg = null;
				if (flag.equals("S")) {
					if (result.getJSONArray("data") == null
							|| result.getJSONArray("data").length() <= 0) {
						logVO.setSrcparm("��" + i + "����" + result.toString());
						break;
					}
					JSONArray resMap = result.getJSONArray("data");
					net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
							.fromObject(resMap.toString());
					@SuppressWarnings({ "unchecked", "rawtypes" })
					List<Map<String, Object>> mapListJson = (List) jsonArray;
					for (Map<String, Object> map : mapListJson) {
						if (!bodylists.containsKey(map.get("ID"))) {
							bodylists.put(map.get("ID").toString(),
									new ArrayList<ProjectBlockInfoVO>());
						}
						ProjectBlockInfoVO pvo = new ProjectBlockInfoVO();
						if( map.get("FPRJID") ==null){
							pvo.setAttributeValue("fprjid",map.get("FPRJID"));//������ĿID��������WBD_Project_T
						}else{
							pvo.setAttributeValue("fprjid",map.get("FPRJID").toString());//������ĿID��������WBD_Project_T
						}
						
						pvo.setAttributeValue("id", map.get("ID").toString());// ��Ŀ����id
						
						if(map.get("FNUMBER") != null){
							pvo.setAttributeValue("fnumber", map.get("FNUMBER"));//���ڱ���
						}
						if(map.get("FNAME") != null){
							pvo.setAttributeValue("fname", map.get("FNAME"));//��������
						}
						if(map.get("FPRODUCTSERIES") == null){
							pvo.setAttributeValue("fproductseries", "");//��Ʒϵ�У����룺WBD_ProductSeries
						}else{
							pvo.setAttributeValue("fproductseries", map.get("FPRODUCTSERIES").toString());//��Ʒϵ�У����룺WBD_ProductSeries
						}
						if(map.get("FDESCRIPTION") == null){
							pvo.setAttributeValue("fdescription", "");//˵��
						}else{
							pvo.setAttributeValue("fdescription", map.get("FDESCRIPTION").toString());//˵��
						}
						if(map.get("FREMARK") == null){
							pvo.setAttributeValue("fremark", "");//���ڱ�����
						}else{
							pvo.setAttributeValue("fremark", map.get("FREMARK").toString());//���ڱ�����
						}
//						pvo.setAttributeValue("dr", new Integer(0));
//						pvo.setStatus(VOStatus.NEW);
						bodylists.get(map.get("ID").toString()).add(pvo);
					}
				} else {
					errmsg = (String) result.getString("msg");
					throw new BusinessException("�����������ݵĴ�����Ϣ��" + errmsg + "��");
				}
			} else {
				Logger.error("����ʧ��");
				throw new BusinessException("����ʧ��");
			}
		}
		return bodylists;
	}
	private Map<String, List<ProjectInfoVO>> getMainProjectDataPVOs(
			OutsideLogVO logVO) throws Exception {
		// http://times-uat.timesgroup.cn:8080/method?code=projectInfo&currpage=1&syscode=nc&token=9087698DEA1E3BD838762D84373733F0

		// PropertiesUtils util = PropertiesUtils
		// .getInstance("main_url.properties");
		String mianurl = OutsideUtils.getOutsideInfo("DATA01");//
		String syscode = OutsideUtils.getOutsideInfo("DATA02");//
		String token = OutsideUtils.getOutsideInfo("DATA03");//
		//   ¥����blockBuildingInfo
		// ��Ŀ��projectInfo http://times-uat.timesgroup.cn:8080/method?code=projectInfo&currpage=1&syscode=invest&token=9087698DEA1E3BD838762D84373733F0
		//��Ŀ���ڣ�projectBlockInfo http://times-uat.timesgroup.cn:8080/method?code=projectBlockInfo&currpage=1&syscode=design&token=D8BB8A73678A4E37E8D7FC4B6F9C57F3
//		String urlstr = mianurl + "?code=projectBlockInfo&currpage=1&syscode=" + syscode + "&token="
//				+ initToken(token);

		Map<String, List<ProjectInfoVO>> bodylists = new HashMap<String, List<ProjectInfoVO>>();
		// �������������л��з�ҳ��ѯ,Ϊȷ����������,�ʴ˴�ʹ��ѭ����ȡ��ҳ,һ����ȡ�ɹ����ҷ��ص�dataΪ��������ѭ��
		int i = 1;
		for (; i < 99999; i++) {
			// String urls = util.readValue("MIANURL")
			// +"&currpage="+i+""
			// +"&syscode="+util.readValue("SYSCODE")
			// +"&token="+initElementInv();
			String urls = mianurl + "?code=projectInfo&currpage="+i+"&syscode=" + syscode + "&token="
					+ initToken(token);
			// ����url��Դ
			URL url = new URL(urls);
			// ����http����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// �����������
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// ���ò��û���
			conn.setUseCaches(false);
			// ���ô��ݷ�ʽ
			conn.setRequestMethod("POST");
			// ����ά�ֳ�����
			conn.setRequestProperty("Connection", "Keep-Alive");
			// �����ļ��ַ���:
			conn.setRequestProperty("Charset", "utf-8");
			// // ת��Ϊ�ֽ�����
			// byte[] data = json.getBytes("utf-8");
			// �����ļ�����
			conn.setRequestProperty("Content-Length",
					String.valueOf(urls.length()));

			// �����ļ�����:
			conn.setRequestProperty("Content-Type", "application/json");

			// ��ʼ��������
			conn.connect();
			String a = "";

			if (conn.getResponseCode() == 200) {
				Logger.error("���ӳɹ�");
				// ���󷵻ص�����
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				String line = null;
				while ((line = in.readLine()) != null) {
					a += line;
				}
				JSONObject result = new JSONObject(a);
				String flag = (String) result.getString("code");
				String errmsg = null;
				if (flag.equals("S")) {
					if (result.getJSONArray("data") == null
							|| result.getJSONArray("data").length() <= 0) {
						logVO.setSrcparm("��" + i + "����" + result.toString());
						break;
					}
					JSONArray resMap = result.getJSONArray("data");
					net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
							.fromObject(resMap.toString());
					@SuppressWarnings({ "unchecked", "rawtypes" })
					List<Map<String, Object>> mapListJson = (List) jsonArray;
					for (Map<String, Object> map : mapListJson) {

						if (!bodylists.containsKey(map.get("ID"))) {// ��Ŀid
							bodylists.put(map.get("ID").toString(),
									new ArrayList<ProjectInfoVO>());
						}
						ProjectInfoVO pvo = new ProjectInfoVO();
						pvo.setAttributeValue("id", map.get("ID").toString());// ��Ŀid
						if(map.get("FNUMBER") != null){
							pvo.setAttributeValue("fnumber", map.get("FNUMBER"));//��Ŀ����
						}
						if(map.get("FNAME") != null){
							pvo.setAttributeValue("fname", map.get("FNAME"));//��Ŀ����
						}
						if("null"
								.equals(map.get("FPRODUCTSERIES") != null ? map
										.get("FPRODUCTSERIES").toString()
										: "null")
								|| map.get("FPRODUCTSERIES") == null
								|| "".equals(map.get("FPRODUCTSERIES"))){
							pvo.setAttributeValue("fproductseries", "");//��Ʒϵ�У����룺WBD_ProductSeries
						}else{
							pvo.setAttributeValue("fproductseries",map.get("FPRODUCTSERIES"));//��Ʒϵ�У����룺WBD_ProductSeries
						}
						if(map.get("FVERSION") == null){
							pvo.setAttributeValue("fversion", "");//�汾
						}else{
							pvo.setAttributeValue("fversion", map.get("FVERSION").toString());//�汾
						}
						if(map.get("FADDRESS") == null){
							pvo.setAttributeValue("faddress", "");//��Ŀ��ַ
						}else{
							pvo.setAttributeValue("faddress", map.get("FADDRESS").toString());//��Ŀ��ַ
						}
						if(map.get("FDESCRIPTION") == null){
							pvo.setAttributeValue("fdescription", "");//˵��
						}else{
							pvo.setAttributeValue("fdescription", map.get("FDESCRIPTION").toString());//˵��
						}
						if(map.get("FLANDAREA") == null){
							pvo.setAttributeValue("flandarea","");//���õ����*
						}else{
							pvo.setAttributeValue("flandarea", map.get("FLANDAREA").toString());//���õ����*
						}
						if(map.get("FNAME1") == null){
							pvo.setAttributeValue("fname1", "");//������1--������
						}else{
							pvo.setAttributeValue("fname1", map.get("FNAME1").toString());//������1--������
						}
						if(map.get("FNAME2") == null){
							pvo.setAttributeValue("fname2", "");//������2--Ӫ������
						}else{
							pvo.setAttributeValue("fname2", map.get("FNAME2").toString());//������2--Ӫ������
						}
						if(map.get("FPLANTOTALAREA") != null){
							pvo.setAttributeValue("fplantotalarea", "");//�滮���õ����
						}else{
							pvo.setAttributeValue("fplantotalarea", map.get("FPLANTOTALAREA").toString());//�滮���õ����
						}
						if(map.get("FGLOSSAREA") == null){
							pvo.setAttributeValue("fglossarea", "");//ë�ݻ���
						}else{
							pvo.setAttributeValue("fglossarea", map.get("FGLOSSAREA").toString());//ë�ݻ���
						}
						if(map.get("FNETAREA") == null){
							pvo.setAttributeValue("fnetarea", "");//���ݻ���
						}else{
							pvo.setAttributeValue("fnetarea", map.get("FNETAREA").toString());//ë�ݻ���
						}
						if(map.get("FGREENRATE") == null){
							pvo.setAttributeValue("fgreenrate", "");//�̵���
						}else{
							pvo.setAttributeValue("fgreenrate", map.get("FGREENRATE").toString());//�̵���
						}
						if(map.get("FCONTROLHIGHT") == null && !"".equals(map.get("FCONTROLHIGHT"))){
							pvo.setAttributeValue("fcontrolhight", "");//�����ظ�
						}else{
							pvo.setAttributeValue("fcontrolhight", map.get("FCONTROLHIGHT").toString());//�����ظ�
						}
						if(map.get("FGLOSSDENSITY") == null){
							pvo.setAttributeValue("fglossdensity", "");//ë�����ܶ�
						}else{
							pvo.setAttributeValue("fglossdensity", map.get("FGLOSSDENSITY").toString());//ë�����ܶ�
						}
						if(map.get("FNETDENSITY") == null){
							pvo.setAttributeValue("fnetdensity", "");//�������ܶ�
						}else{
							pvo.setAttributeValue("fnetdensity", map.get("FNETDENSITY").toString());//�������ܶ�
						}
						if(map.get("FPROJECTSTATUS_CN") == null){
							pvo.setAttributeValue("Fprojectstatus_cn", "");//��Ŀ״̬�����ģ�
						}else{
							pvo.setAttributeValue("Fprojectstatus_cn", map.get("FPROJECTSTATUS_CN").toString());//��Ŀ״̬�����ģ�
						}
						if(map.get("FREGION_NAME") == null){
							pvo.setAttributeValue("Fregion_name", "");//��������
						}else{
							pvo.setAttributeValue("Fregion_name", map.get("FREGION_NAME").toString());//��������
						}
						if(map.get("FPRODUCTSERIES_NAME") == null){
							pvo.setAttributeValue("fproductseries_name", "");//��Ʒϵ������
						}else{
							pvo.setAttributeValue("fproductseries_name", map.get("FPRODUCTSERIES_NAME").toString());//��Ʒϵ������
						}
						if(map.get("ORG_NAME") == null){
							pvo.setAttributeValue("org_name", "");//��Ŀ��˾����
						}else{
							pvo.setAttributeValue("org_name", map.get("ORG_NAME").toString());//��Ŀ��˾����
						}
						if(map.get("COUNTRY_NAME") == null){
							pvo.setAttributeValue("country_name", "");//����
						}else{
							pvo.setAttributeValue("country_name", map.get("COUNTRY_NAME").toString());//����
						}
						if(map.get("PROVIENCE_NAME") == null){
							pvo.setAttributeValue("provience_name", "");//ʡ��
						}else{
							pvo.setAttributeValue("provience_name", map.get("PROVIENCE_NAME").toString());//ʡ��
						}
						if(map.get("CITY_NAME") == null){
							pvo.setAttributeValue("city_name", "");//����
						}else{
							pvo.setAttributeValue("city_name", map.get("CITY_NAME").toString());//����
						}						
//						pvo.setAttributeValue("dr", new Integer(0));
//						pvo.setStatus(VOStatus.NEW);
						bodylists.get(map.get("ID").toString()).add(pvo);
					
					}
					System.out.println(i);
				} else {
					errmsg = (String) result.getString("msg");
					throw new BusinessException("�����������ݵĴ�����Ϣ��" + errmsg + "��");
				}
			} else {
				Logger.error("����ʧ��");
				throw new BusinessException("����ʧ��");
			}
		}
		System.out.println(i);
		return bodylists;
	}
	/**
	 * ��ʼ��token��Ϣ
	 * 
	 * @param servername
	 * @return
	 */
	private String initToken(String key) {

		Date date = new UFDate().toDate();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// ������ʱ��
		String time = formater.format(date);
		String ticket = time + key;
		String ticketMD5 = DigestUtils.md5Hex(ticket).toUpperCase();// 32λ��д
		// String ticketMD5 = MD5Util.getMD5(ticket).toUpperCase();
		return ticketMD5;
	}
}
