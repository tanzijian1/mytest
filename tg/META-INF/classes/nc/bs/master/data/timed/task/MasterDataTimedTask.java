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
	 * 数据库持久化
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
		//获取到定时：任务类型
		String title = bgwc.getAlertTypeName();
		String sql = "update sdfn_masterdata set dr = '1' where dr = '0' ";
		getDao().executeUpdate(sql);
		List<Object[]> reflist = new ArrayList<Object[]>();
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new TGInterestMessage());
		Map<String, List<ProjectBlockInfoVO>> pbMap = null;//项目分期信息接口
		Map<String, List<BlockBuildingInfoVO>> bbMap = null;//楼栋信息	
		Map<String, List<ProjectInfoVO>> pMap = null;//项目信息接口

		//调用保存日志的接口地址
		ISaveLogService service = NCLocator.getInstance().lookup(
			    ISaveLogService.class);
		OutsideLogVO logVO = new OutsideLogVO();
		try {
			logVO.setDesbill(bgwc.getAlertTypeName());
			logVO.setSrcsystem("主数据");
			logVO.setOperator("主数据");
			pMap = getMainProjectDataPVOs(logVO);//获取主数据 项目信息
			pbMap = getProjectBlockInfoVOs(logVO);//获取主数据 项目分期信息
			bbMap = getBlockBuildingInfoVOs(logVO);//获取主数据 楼栋信息
			System.out.println("获取主数据信息完成");
			if(pMap.size()== 0 && pMap == null){
				logVO.setResult("获取到项目信息接口信息为空");				
			}else{
				logVO.setSrcparm(pMap.toString());
			}
			if(pbMap.size() == 0 && pbMap == null ){
				logVO.setResult("获取到项目分期信息接口信息为空");
			}else{
				logVO.setSrcparm(pbMap.toString());
			}
			if(bbMap.size() == 0 && bbMap == null){
				logVO.setResult("获取到楼栋信息接口信息为空");
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
				// TODO 自动生成的 catch 块
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
		InvocationInfoProxy.getInstance().setUserId("100112100000000030TI");// 程亚运
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
					/**表体*/
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
									//项目分期
									String name = null;
									if(pbvo.getFname() != null){
										name =  pbvo.getFname();
									}
									MasterDataXFVO xfvo = new MasterDataXFVO();
									xfvo.setDef1(pbvo.getFnumber());//分期编号
									if(pbvo.getFname() == null || "null".equals(pbvo.getFname())){
										xfvo.setDef2(null);//分期名称
									}else{
										xfvo.setDef2(pbvo.getFname());//分期名称
									}
									if(pbvo.getFproductseries() == null || "null".equals(pbvo.getFproductseries())){
										xfvo.setDef3(null);//产品系列
									}else{
										xfvo.setDef3(pbvo.getFproductseries());//产品系列
									}
									if(pbvo.getFremark() == null || "null".equals(pbvo.getFremark())){
										xfvo.setDef4(null);//分期备案名
									}else{
										xfvo.setDef4(pbvo.getFremark());//分期备案名
									}
									if(pbvo.getFdescription() == null || "null".equals(pbvo.getFdescription())){
										xfvo.setDef5(null);//说明
									}else{
										xfvo.setDef5(pbvo.getFdescription());//说明
									}
									xfvo.setDr(0);
									xfvo.setStatus(VOStatus.NEW);
									xfvo.setTs(new UFDateTime());
									xfvolist[i] = xfvo;
									i=i+1;
									String id = pbvo.getId();//项目分期ID
									/*xfvo.setDef6(bbvo.getFprdid());//产品类型
									xfvo.setDef7(null);//地上/地下
									xfvo.setDef8(null);//建筑面积
									xfvo.setDef9(null);//计容面积
									xfvo.setDef10(null);//不计容面积
									xfvo.setDef11(null);//可售面积
									xfvo.setDef12(null);//不可售面积
									xfvo.setDef13(null);//架空面积
									xfvo.setDef14(null);//基地面积
									xfvo.setDef15(null);//入户大堂面积
									xfvo.setDef16(null);//普通住宅面积
									xfvo.setDef17(null);//非普通住宅面积
									xfvo.setDef18(null);//单元数
									xfvo.setDef19(null);//套数/个数
									xfvo.setDef20(null);//电梯楼							*/							

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
														/**住宅计容信息*/
														
														MasterDataZJVO zjvo = getMasterDataZJVO(bbvo,name);
														zjvolist[f] = zjvo;
														
														/**商业计容信息子表*/

														MasterDataSJVO sjvo = new MasterDataSJVO();
														sjvo = getMasterDataSJVO(bbvo,
																sjvo,name);
														sjvolist[f] = sjvo;
														
														/**办公计容信息子表*/
			
														MasterDataBJVO bjvo = new MasterDataBJVO();
														bjvo = getMasterDataBJVO(bbvo,
																bjvo,name);
														bjvolist[f] = bjvo;
														
														/**公建配套信息子表*/
														
														MasterDataGPVO gpvo = new MasterDataGPVO();
														gpvo = getMasterDataGPVO(bbvo,
																gpvo,name);
														gpvolist[f] = gpvo;

														/**地下室计容信息子表*/
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
				str= "成功";
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
		vom.setDef23(vo.getId());//主数据项目信息主键
		vom.setDef1(vo.getOrg_name());//项目公司def23
		vom.setDef2(vo.getFnumber());//项目编码
		if(vo.getFname() == null || "null".equals(vo.getFname())){
			vom.setDef3(null);//项目名称
		}else{
			vom.setDef3(vo.getFname());//项目名称
		}
		if(vo.getFproductseries_name() == null || "null".equals(vo.getFproductseries_name())){
			vom.setDef4(null);//产品系列
		}else{
			vom.setDef4(vo.getFproductseries_name());//产品系列
		}
		if(vo.getFname1() == null || "null".equals(vo.getFname1())){
			vom.setDef5(null);//报建名
		}else{
			vom.setDef5(vo.getFname1());//报建名
		}
		if(vo.getFname2() == null || "null".equals(vo.getFname2())){
			vom.setDef6(null);//营销用名
		}else{
			vom.setDef6(vo.getFname2());//营销用名
		}
		if(vo.getFregion_name() == null || "null".equals(vo.getFregion_name())){
			vom.setDef7(null);//所属区域
		}else{
			vom.setDef7(vo.getFregion_name());//所属区域
		}
		if(vo.getFprojectstatus_cn() == null || "null".equals(vo.getFprojectstatus_cn())){
			vom.setDef8(null);//项目转态
		}else{
			vom.setDef8(vo.getFprojectstatus_cn());//项目转态
		}
		if(vo.getFversion() == null || "null".equals(vo.getFversion())){
			vom.setDef9(null);//版本号
		}else{
			vom.setDef9(vo.getFversion());//版本号
		}
		if(vo.getCountry_name() == null || "null".equals(vo.getCountry_name())){
			vom.setDef10(null);//国家
		}else{
			vom.setDef10(vo.getCountry_name());//国家
		}
		if(vo.getProvience_name() == null || "null".equals(vo.getProvience_name())){
			vom.setDef11(null);//省份
		}else{
			vom.setDef11(vo.getProvience_name());//省份
		}
		if(vo.getCity_name() == null || "null".equals(vo.getCity_name())){
			vom.setDef12(null);//城市
		}else{
			vom.setDef12(vo.getCity_name());//城市
		}
		if(vo.getFaddress() == null || "null".equals(vo.getFaddress())){
			vom.setDef13(null);//项目地址
		}else{
			vom.setDef13(vo.getFaddress());//项目地址
		}
		if(vo.getFplantotalarea() == null || "null".equals(vo.getFplantotalarea())){
			vom.setDef14(null);//规划总用地面积(平方米)
		}else{
			vom.setDef14(vo.getFplantotalarea());//规划总用地面积(平方米)
		}
		if(vo.getFlandarea() == null || "null".equals(vo.getFlandarea())){
			vom.setDef15(null);//净用地面积(平方米
		}else{
			vom.setDef15(vo.getFlandarea());//净用地面积(平方米
		}
		if(vo.getFglossarea() == null || "null".equals(vo.getFglossarea())){
			vom.setDef16(null);//毛容积率
		}else{
			vom.setDef16(vo.getFglossarea());//毛容积率
		}
		if(vo.getFnetarea() == null || "null".equals(vo.getFnetarea())){
			vom.setDef17(null);//净容积率
		}else{
			vom.setDef17(vo.getFnetarea());//净容积率
		}
		if(vo.getFgreenrate() == null || "null".equals(vo.getFgreenrate())){
			vom.setDef18(null);//绿地率(%)
		}else{
			vom.setDef18(vo.getFgreenrate());//绿地率(%)
		}
		if(vo.getFcontrolhight() == null || "null".equals(vo.getFcontrolhight())){
			vom.setDef19(null);//建筑控高(米)
		}else{
			vom.setDef19(vo.getFcontrolhight());//建筑控高(米)
		}
		if(vo.getFglossdensity() == null || "null".equals(vo.getFglossdensity())){
			vom.setDef20(null);//毛建筑密度(%)
		}else{
			vom.setDef20(vo.getFglossdensity());//毛建筑密度(%)
		}
		if(vo.getFnetdensity() == null || "null".equals(vo.getFnetdensity())){
			vom.setDef21(null);//净建筑密度(%)
		}else{
			vom.setDef21(vo.getFnetdensity());//净建筑密度(%)
		}
		if(vo.getFdescription() == null || "null".equals(vo.getFdescription())){
			vom.setBig_text_a(null);//说明
		}else{
			vom.setBig_text_a(vo.getFdescription());//说明
		}				
//				panel.setHeadItem("pk_group", pk_group);
//				panel.setHeadItem("pk_org", pk_org);
//				// 设置单据状态、单据业务日期默认值
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
	/**住宅计容信息*/
	public MasterDataZJVO getMasterDataZJVO(BlockBuildingInfoVO bbvo,String name) {
		MasterDataZJVO zjvo = new MasterDataZJVO();
		if(bbvo.getFpbid() == null || "null".equals(bbvo.getFpbid())){
			zjvo.setDef1(null);//项目分期
		}else{
			zjvo.setDef1(name);//项目分期
		}
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			zjvo.setDef2(null);//楼栋
		}else{
			zjvo.setDef2(bbvo.getFnumber().toString());//楼栋
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			zjvo.setDef3(null);//营销楼号
		}else{
			zjvo.setDef3(bbvo.getFmarketnumber().toString());//营销楼号
		}
		if(bbvo.getFunitnumber() == null || "null".equals(bbvo.getFunitnumber())){
			zjvo.setDef4(null);//单元号
		}else{
			zjvo.setDef4(bbvo.getFunitnumber().toString());//单元号
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			zjvo.setDef5(null);//标段
		}else{
			zjvo.setDef5(bbvo.getFtender().toString());//标段
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			zjvo.setDef6(null);//组团
		}else{
			zjvo.setDef6(bbvo.getFbuildgroup().toString());//组团
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			zjvo.setDef7(null);//产品类型
		}else{
			zjvo.setDef7(bbvo.getFprdid().toString());//产品类型
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			zjvo.setDef8(null);//开发阶段
		}else{
			zjvo.setDef8(bbvo.getFblockstage().toString());//开发阶段
		}
//													zjvo.setDef9(null);//地上/地下
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			zjvo.setDef10(null);//建筑面积
		}else{
			zjvo.setDef10(bbvo.getFbuildingarea().toString());//建筑面积
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			zjvo.setDef11(null);//计容面积
		}else{
			zjvo.setDef11(bbvo.getFfactplotarea().toString());//计容面积
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			zjvo.setDef12(null);//不计容面积
		}else{
			zjvo.setDef12(bbvo.getFfactnonplotarea().toString());//不计容面积
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			zjvo.setDef13(null);//可售面积
		}else{
			zjvo.setDef13(bbvo.getFsaleablearea().toString());//可售面积
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			zjvo.setDef14(null);//不可售面积 
		}else{
			zjvo.setDef14(bbvo.getFnonsaleablearea().toString());//不可售面积 
		} 
		if(bbvo.getFoperfloorarea() == null || "null".equals(bbvo.getFoperfloorarea())){
			zjvo.setDef15(null);//架空面积 基
		}else{
			zjvo.setDef15(bbvo.getFoperfloorarea().toString());//架空面积 基
		}
		if(bbvo.getFhallarea() == null || "null".equals(bbvo.getFhallarea())){
			zjvo.setDef16(null);//入户大堂面积 
		}else{
			zjvo.setDef16(bbvo.getFhallarea().toString());//入户大堂面积 
		}
		if(bbvo.getFbuildingbasearea() == null || "null".equals(bbvo.getFbuildingbasearea())){
			zjvo.setDef17(null);//基地面积 地面积非普通住宅套数
		}else{
			zjvo.setDef17(bbvo.getFbuildingbasearea().toString());//基地面积 地面积非普通住宅套数
		}
		if(bbvo.getFcommonnumber() == null || "null".equals(bbvo.getFcommonnumber())){
			zjvo.setDef18(null);//普通住宅套数 
		}else{
			zjvo.setDef18(bbvo.getFcommonnumber().toString());//普通住宅套数 
		}
		if(bbvo.getFuncommonnumber() == null || "null".equals(bbvo.getFuncommonnumber())){
			zjvo.setDef19(null);//非普通住宅套数 
		}else{
			zjvo.setDef19(bbvo.getFuncommonnumber().toString());//非普通住宅套数 
		}
		if(bbvo.getFroomnumber() == null || "null".equals(bbvo.getFroomnumber())){
			zjvo.setDef20(null);//总套数 
		}else{
			zjvo.setDef20(bbvo.getFroomnumber().toString());//总套数 
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			zjvo.setDef21(null);//地上层数
		}else{
			zjvo.setDef21(bbvo.getFfloor().toString());//地上层数
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			zjvo.setDef22(null);//地下层数
		}else{
			zjvo.setDef22(bbvo.getFunderfloorcount().toString());//地下层数
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			zjvo.setDef23(null);//建筑总高度
		}else{
			zjvo.setDef23(bbvo.getFbuildingheight().toString());//建筑总高度
		}
		if(bbvo.getFinternalarea() == null || "null".equals(bbvo.getFinternalarea())){
			zjvo.setDef24(null);//套内面积
		}else{
			zjvo.setDef24(bbvo.getFinternalarea().toString());//套内面积
		}
		if(bbvo.getFapportionarea() == null || "null".equals(bbvo.getFapportionarea())){
			zjvo.setDef25(null);//分摊面积
		}else{
			zjvo.setDef25(bbvo.getFapportionarea().toString());//分摊面积
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			zjvo.setDef26(null);//均价
		}else{
			zjvo.setDef26(bbvo.getFprice().toString());//均价
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			zjvo.setDef27(null);//主体结构、
		}else{
			zjvo.setDef27(bbvo.getFunderfloorcount().toString());//主体结构、
		}													
		zjvo.setDr(0);
		zjvo.setTs(new UFDateTime());
		return zjvo;
	}
	/**商业计容信息子表*/
	public MasterDataSJVO getMasterDataSJVO(BlockBuildingInfoVO bbvo, MasterDataSJVO sjvo,String name) {
		if(bbvo.getFpbid() == null || "null".equals(bbvo.getFpbid())){
			sjvo.setDef1(null);//项目分期
		}else{
			sjvo.setDef1(name);//项目分期
		}
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			sjvo.setDef2(null);//楼栋
		}else{
			sjvo.setDef2(bbvo.getFnumber());//楼栋
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			sjvo.setDef3(null);//营销楼号
		}else{
			sjvo.setDef3(bbvo.getFmarketnumber());//营销楼号
		}
		if(bbvo.getFunitnumber() == null || "null".equals(bbvo.getFunitnumber())){
			sjvo.setDef4(null);//单元号
		}else{
			sjvo.setDef4(bbvo.getFunitnumber());//单元号
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			sjvo.setDef5(null);//标段
		}else{
			sjvo.setDef5(bbvo.getFtender());//标段
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			sjvo.setDef6(null);//组团
		}else{
			sjvo.setDef6(bbvo.getFbuildgroup());//组团
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			sjvo.setDef7(null);//产品类型
		}else{
			sjvo.setDef7(bbvo.getFprdid());//产品类型
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			sjvo.setDef8(null);//开发阶段
		}else{
			sjvo.setDef8(bbvo.getFblockstage());//开发阶段
		}
//													sjvo.setDef9(null);//地上/地下
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			sjvo.setDef10(null);//建筑面积
		}else{
			sjvo.setDef10(bbvo.getFbuildingarea());//建筑面积
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			sjvo.setDef11(null);//计容面积
		}else{
			sjvo.setDef11(bbvo.getFfactplotarea());//计容面积
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			sjvo.setDef12(null);//不计容面积
		}else{
			sjvo.setDef12(bbvo.getFfactnonplotarea());//不计容面积
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			sjvo.setDef13(null);//可售面积 
		}else{
			sjvo.setDef13(bbvo.getFsaleablearea());//可售面积 
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			sjvo.setDef14(null);//不可售面积 
		}else{
			sjvo.setDef14(bbvo.getFnonsaleablearea());//不可售面积 
		}
		if(bbvo.getFoperfloorarea() == null || "null".equals(bbvo.getFoperfloorarea())){
			sjvo.setDef15(null);//架空面积 
		}else{
			sjvo.setDef15(bbvo.getFoperfloorarea());//架空面积 
		}
		if(bbvo.getFbuildingbasearea() == null || "null".equals(bbvo.getFbuildingbasearea())){
			sjvo.setDef22(null);//基地面积
		}else{
			sjvo.setDef22(bbvo.getFbuildingbasearea());//基地面积
		}
		if(bbvo.getFhallarea() == null || "null".equals(bbvo.getFhallarea())){
			sjvo.setDef23(null);//入户大堂面积 
		}else{
			sjvo.setDef23(bbvo.getFhallarea());//入户大堂面积 
		}
		if(bbvo.getFroomnumber() == null || "null".equals(bbvo.getFroomnumber())){
			sjvo.setDef24(null);//总套数
		}else{
			sjvo.setDef24(bbvo.getFroomnumber());//总套数
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			sjvo.setDef25(null);//地上层数
		}else{
			sjvo.setDef25(bbvo.getFfloor());//地上层数
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			sjvo.setDef26(null);//地下层数 总套数 
		}else{
			sjvo.setDef26(bbvo.getFunderfloorcount());//地下层数 总套数 
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			sjvo.setDef27(null);//建筑总高度
		}else{
			sjvo.setDef27(bbvo.getFbuildingheight());//建筑总高度
		}
		if(bbvo.getFinternalarea() == null || "null".equals(bbvo.getFinternalarea())){
			sjvo.setDef28(null);//套内面积
		}else{
			sjvo.setDef28(bbvo.getFinternalarea());//套内面积
		}
		if(bbvo.getFapportionarea() == null || "null".equals(bbvo.getFapportionarea())){
			sjvo.setDef29(null);//分摊面积
		}else{
			sjvo.setDef29(bbvo.getFapportionarea());//分摊面积
		}
		if(bbvo.getFsharerate() == null || "null".equals(bbvo.getFsharerate())){
			sjvo.setDef30(null);//分摊系数
		}else{
			sjvo.setDef30(bbvo.getFsharerate());//分摊系数
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			sjvo.setDef31(null);//均价
		}else{
			sjvo.setDef31(bbvo.getFprice());//均价
		}
		if(bbvo.getFirstshop() == null || "null".equals(bbvo.getFirstshop())){
			sjvo.setDef32(null);//首层商铺面积
		}else{
			sjvo.setDef32(bbvo.getFirstshop());//首层商铺面积
		}
		if(bbvo.getFfirststepunit() == null || "null".equals(bbvo.getFfirststepunit())){
			sjvo.setDef33(null);//首层单元数
		}else{
			sjvo.setDef33(bbvo.getFfirststepunit());//首层单元数
		}
		if(bbvo.getSecondshop() == null || "null".equals(bbvo.getSecondshop())){
			sjvo.setDef34(null);//二层及以上商铺面积
		}else{
			sjvo.setDef34(bbvo.getSecondshop());//二层及以上商铺面积
		}
		if(bbvo.getFsecondstepunit() == null || "null".equals(bbvo.getFsecondstepunit())){
			sjvo.setDef35(null);//非首层单元数
		}else{
			sjvo.setDef35(bbvo.getFsecondstepunit());//非首层单元数
		}
		if(bbvo.getFhireyears() == null || "null".equals(bbvo.getFhireyears())){
			sjvo.setDef36(null);//承租年限
		}else{
			sjvo.setDef36(bbvo.getFhireyears());//承租年限
		}
		if(bbvo.getFaddarea() == null || "null".equals(bbvo.getFaddarea())){
			sjvo.setDef37(null);//加建面积
		}else{
			sjvo.setDef37(bbvo.getFaddarea());//加建面积
		}
		if(bbvo.getFhireratio() == null || "null".equals(bbvo.getFhireratio())){
			sjvo.setDef38(null);//加成系数
		}else{
			sjvo.setDef38(bbvo.getFhireratio());//加成系数
		}
		sjvo.setStatus(VOStatus.NEW);
		sjvo.setDr(0);
		sjvo.setTs(new UFDateTime());
		return sjvo;
	}
	/**办公计容信息子表*/
	public MasterDataBJVO getMasterDataBJVO(BlockBuildingInfoVO bbvo, MasterDataBJVO bjvo,String name) {
		if(bbvo.getFpbid() == null || "null".equals(bbvo.getFpbid())){
			bjvo.setDef1(null);//项目分期
		}else{
			bjvo.setDef1(name);//项目分期
		}
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			bjvo.setDef2(null);//楼栋
		}else{
			bjvo.setDef2(bbvo.getFnumber());//楼栋
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			bjvo.setDef3(null);//营销楼号
		}else{
			bjvo.setDef3(bbvo.getFmarketnumber());//营销楼号
		}
		if(bbvo.getFunitnumber() == null || "null".equals(bbvo.getFunitnumber())){
			bjvo.setDef4(null);//单元号
		}else{
			bjvo.setDef4(bbvo.getFunitnumber());//单元号
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			bjvo.setDef5(null);//标段
		}else{
			bjvo.setDef5(bbvo.getFtender());//标段
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			bjvo.setDef6(null);//组团
		}else{
			bjvo.setDef6(bbvo.getFbuildgroup());//组团
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			bjvo.setDef7(null);//产品类型
		}else{
			bjvo.setDef7(bbvo.getFprdid());//产品类型
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			bjvo.setDef8(null);//开发阶段
		}else{
			bjvo.setDef8(bbvo.getFblockstage());//开发阶段
		}
//													bjvo.setDef9(null);//地上/地下
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			bjvo.setDef10(null);//建筑面积
		}else{
			bjvo.setDef10(bbvo.getFbuildingarea());//建筑面积
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			bjvo.setDef11(null);//计容面积
		}else{
			bjvo.setDef11(bbvo.getFfactplotarea());//计容面积
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			bjvo.setDef12(null);//不计容面积
		}else{
			bjvo.setDef12(bbvo.getFfactnonplotarea());//不计容面积
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			bjvo.setDef13(null);//可售面积 
		}else{
			bjvo.setDef13(bbvo.getFsaleablearea());//可售面积 
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			bjvo.setDef14(null);//不可售面积 
		}else{
			bjvo.setDef14(bbvo.getFnonsaleablearea());//不可售面积 
		}
		if(bbvo.getFoperfloorarea() == null || "null".equals(bbvo.getFoperfloorarea())){
			bjvo.setDef15(null);//架空面积 
		}else{
			bjvo.setDef15(bbvo.getFoperfloorarea());//架空面积 
		}
		if(bbvo.getFhallarea() == null || "null".equals(bbvo.getFhallarea())){
			bjvo.setDef16(null);//入户大堂面积
		}else{
			bjvo.setDef16(bbvo.getFhallarea());//入户大堂面积
		}
		if(bbvo.getFproductFeature() == null || "null".equals(bbvo.getFproductFeature())){
			bjvo.setDef17(null);//功能分类
		}else{
			bjvo.setDef17(bbvo.getFproductFeature());//功能分类
		}
		if(bbvo.getFroomnumber() == null || "null".equals(bbvo.getFroomnumber())){
			bjvo.setDef18(null);//总套数
		}else{
			bjvo.setDef18(bbvo.getFroomnumber());//总套数
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			bjvo.setDef19(null);//地上层数
		}else{
			bjvo.setDef19(bbvo.getFfloor());//地上层数
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			bjvo.setDef20(null);//地下层数
		}else{
			bjvo.setDef20(bbvo.getFunderfloorcount());//地下层数
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			bjvo.setDef21(null);//建筑总高度
		}else{
			bjvo.setDef21(bbvo.getFbuildingheight());//建筑总高度
		}
		if(bbvo.getFinternalarea() == null || "null".equals(bbvo.getFinternalarea())){
			bjvo.setDef22(null);//套内面积
		}else{
			bjvo.setDef22(bbvo.getFinternalarea());//套内面积
		}
		if(bbvo.getFapportionarea() == null || "null".equals(bbvo.getFapportionarea())){
			bjvo.setDef23(null);//分摊面积
		}else{
			bjvo.setDef23(bbvo.getFapportionarea());//分摊面积
		}
		if(bbvo.getFsharerate() == null || "null".equals(bbvo.getFsharerate())){
			bjvo.setDef24(null);//分摊系数
		}else{
			bjvo.setDef24(bbvo.getFsharerate());//分摊系数
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			bjvo.setDef25(null);//均价
		}else{
			bjvo.setDef25(bbvo.getFprice());//均价
		}
		if(bbvo.getFirstshop() == null || "null".equals(bbvo.getFirstshop())){
			bjvo.setDef26(null);//首层商铺面积
		}else{
			bjvo.setDef26(bbvo.getFirstshop());//首层商铺面积
		}
		if(bbvo.getFfirststepunit() == null || "null".equals(bbvo.getFfirststepunit())){
			bjvo.setDef27(null);//首层单元数
		}else{
			bjvo.setDef27(bbvo.getFfirststepunit());//首层单元数
		}
		if(bbvo.getSecondshop() == null || "null".equals(bbvo.getSecondshop())){
			bjvo.setDef28(null);//二层及以上商铺面积
		}else{
			bjvo.setDef28(bbvo.getSecondshop());//二层及以上商铺面积
		}
		if(bbvo.getFsecondstepunit() == null || "null".equals(bbvo.getFsecondstepunit())){
			bjvo.setDef29(null);//非首层单元数
		}else{
			bjvo.setDef29(bbvo.getFsecondstepunit());//非首层单元数
		}
		if(bbvo.getFhireyears() == null || "null".equals(bbvo.getFhireyears())){
			bjvo.setDef30(null);//承租年限
		}else{
			bjvo.setDef30(bbvo.getFhireyears());//承租年限
		}
		if(bbvo.getFaddarea() == null || "null".equals(bbvo.getFaddarea())){
			bjvo.setDef31(null);//加建面积
		}else{
			bjvo.setDef31(bbvo.getFaddarea());//加建面积
		}
		if(bbvo.getFhireratio() == null || "null".equals(bbvo.getFhireratio())){
			bjvo.setDef32(null);//加成系数 
		}else{
			bjvo.setDef32(bbvo.getFhireratio());//加成系数 
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			bjvo.setDef33(null);//主体结构
		}else{
			bjvo.setDef33(bbvo.getFunderfloorcount());//主体结构
		}
		bjvo.setStatus(VOStatus.NEW);
		bjvo.setDr(0);
		bjvo.setTs(new UFDateTime());
		return bjvo;
	}
	/**公建配套信息子表*/
	public MasterDataGPVO getMasterDataGPVO(BlockBuildingInfoVO bbvo, MasterDataGPVO gpvo,String name) {
		gpvo.setDef1(name);//项目分期
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			gpvo.setDef2(null);//楼栋
		}else{
			gpvo.setDef2(bbvo.getFnumber().toString());//楼栋
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			gpvo.setDef3(null);//营销楼号
		}else{
			gpvo.setDef3(bbvo.getFmarketnumber());//营销楼号]
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			gpvo.setDef4(null);//标段
		}else{
			gpvo.setDef4(bbvo.getFtender());//标段
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			gpvo.setDef5(null);//组团
		}else{
			gpvo.setDef5(bbvo.getFbuildgroup());//组团
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			gpvo.setDef6(null);//产品类型
		}else{
			gpvo.setDef6(bbvo.getFprdid());//产品类型
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			gpvo.setDef7(null);//开发阶段
		}else{
			gpvo.setDef7(bbvo.getFblockstage());//开发阶段
		}
//													gpvo.setDef8(null);//地上/地下
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			gpvo.setDef9(null);//建筑面积
		}else{
			gpvo.setDef9(bbvo.getFbuildingarea());//建筑面积
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			gpvo.setDef10(null);//计容面积
		}else{
			gpvo.setDef10(bbvo.getFfactplotarea());//计容面积
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			gpvo.setDef11(null);//不计容面积
		}else{
			gpvo.setDef11(bbvo.getFfactnonplotarea());//不计容面积
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			gpvo.setDef12(null);//可售面积
		}else{
			gpvo.setDef12(bbvo.getFsaleablearea());//可售面积
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			gpvo.setDef13(null);//不可售面积
		}else{
			gpvo.setDef13(bbvo.getFnonsaleablearea());//不可售面积
		}
		if(bbvo.getFoperfloorarea() == null || "null".equals(bbvo.getFoperfloorarea())){
			gpvo.setDef14(null);//架空面积
		}else{
			gpvo.setDef14(bbvo.getFoperfloorarea());//架空面积
		}
		if(bbvo.getFbuildingbasearea() == null || "null".equals(bbvo.getFbuildingbasearea())){
			gpvo.setDef15(null);//基地面积
		}else{
			gpvo.setDef15(bbvo.getFbuildingbasearea());//基地面积
		}
		if(bbvo.getFhallarea() == null || "null".equals(bbvo.getFhallarea())){
			gpvo.setDef16(null);//入户大堂面积
		}else{
			gpvo.setDef16(bbvo.getFhallarea());//入户大堂面积
		}
		if(bbvo.getFroomnumber() == null || "null".equals(bbvo.getFroomnumber())){
			gpvo.setDef17(null);//总套数
		}else{
			gpvo.setDef17(bbvo.getFroomnumber());//总套数
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			gpvo.setDef18(null);//地上层数
		}else{
			gpvo.setDef18(bbvo.getFfloor());//地上层数
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			gpvo.setDef19(null);//地下层数
		}else{
			gpvo.setDef19(bbvo.getFunderfloorcount());//地下层数
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			gpvo.setDef20(null);//建筑总高度
		}else{
			gpvo.setDef20(bbvo.getFbuildingheight());//建筑总高度
		}
		if(bbvo.getFinternalarea() == null || "null".equals(bbvo.getFinternalarea())){
			gpvo.setDef21(null);//套内面积 
		}else{
			gpvo.setDef21(bbvo.getFinternalarea());//套内面积 
		}
		if(bbvo.getFapportionarea() == null || "null".equals(bbvo.getFapportionarea())){
			gpvo.setDef22(null);//分摊面积
		}else{
			gpvo.setDef22(bbvo.getFapportionarea());//分摊面积
		}
		if(bbvo.getFsharerate() == null || "null".equals(bbvo.getFsharerate())){
			gpvo.setDef23(null);//分摊系数
		}else{
			gpvo.setDef23(bbvo.getFsharerate());//分摊系数
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			gpvo.setDef24(null);//均价
		}else{
			gpvo.setDef24(bbvo.getFprice());//均价
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			gpvo.setDef25(null);//主体结构	
		}else{
			gpvo.setDef25(bbvo.getFunderfloorcount());//主体结构	
		}
		gpvo.setStatus(VOStatus.NEW);
		gpvo.setDr(0);
		gpvo.setTs(new UFDateTime());
		return gpvo;
	}
	/**地下室计容信息子表*/
	public MasterDataDJVO getMasterDataDJVO(BlockBuildingInfoVO bbvo, MasterDataDJVO djvo,String name) {
		djvo.setDef1(name);//项目分期
		if(bbvo.getFnumber() == null || "null".equals(bbvo.getFnumber())){
			djvo.setDef2(null);//楼栋
		}else{
			djvo.setDef2(bbvo.getFnumber().toString());//楼栋
		}
		if(bbvo.getFmarketnumber() == null || "null".equals(bbvo.getFmarketnumber())){
			djvo.setDef3(null);//营销楼号
		}else{
			djvo.setDef3(bbvo.getFmarketnumber());//营销楼号
		}
		if(bbvo.getFtender() == null || "null".equals(bbvo.getFtender())){
			djvo.setDef4(null);//标段
		}else{
			djvo.setDef4(bbvo.getFtender());//标段
		}
		if(bbvo.getFbuildgroup() == null || "null".equals(bbvo.getFbuildgroup())){
			djvo.setDef5(null);//组团
		}else{
			djvo.setDef5(bbvo.getFbuildgroup());//组团
		}
		if(bbvo.getFprdid() == null || "null".equals(bbvo.getFprdid())){
			djvo.setDef6(null);//产品类型
		}else{
			djvo.setDef6(bbvo.getFprdid());//产品类型
		}
		if(bbvo.getFblockstage() == null || "null".equals(bbvo.getFblockstage())){
			djvo.setDef7(null);//开发阶段
		}else{
			djvo.setDef7(bbvo.getFblockstage());//开发阶段
		}
//													djvo.setDef8(null);//地上/地下 
		if(bbvo.getFbuildingarea() == null || "null".equals(bbvo.getFbuildingarea())){
			djvo.setDef9(null);//建筑面积
		}else{
			djvo.setDef9(bbvo.getFbuildingarea());//建筑面积
		}
		if(bbvo.getFfactplotarea() == null || "null".equals(bbvo.getFfactplotarea())){
			djvo.setDef10(null);//计容面积
		}else{
			djvo.setDef10(bbvo.getFfactplotarea());//计容面积
		}
		if(bbvo.getFfactnonplotarea() == null || "null".equals(bbvo.getFfactnonplotarea())){
			djvo.setDef11(null);//不计容面积
		}else{
			djvo.setDef11(bbvo.getFfactnonplotarea());//不计容面积
		}
		if(bbvo.getFsaleablearea() == null || "null".equals(bbvo.getFsaleablearea())){
			djvo.setDef12(null);//可售面积
		}else{
			djvo.setDef12(bbvo.getFsaleablearea());//可售面积
		}
		if(bbvo.getFnonsaleablearea() == null || "null".equals(bbvo.getFnonsaleablearea())){
			djvo.setDef13(null);//不可售面积
		}else{
			djvo.setDef13(bbvo.getFnonsaleablearea());//不可售面积
		}
		djvo.setDef14(null);//总车位个数
		djvo.setDef15(null);//人防车位个数
		djvo.setDef16(null);//非人防车位个数
		if(bbvo.getFpropertyparkingplace() == null || "null".equals(bbvo.getFpropertyparkingplace())){
			djvo.setDef17(null);//产权车位个数
		}else{
			djvo.setDef17(bbvo.getFpropertyparkingplace());//产权车位个数
		}
		if(bbvo.getFnonpropertyparkingplace() == null || "null".equals(bbvo.getFnonpropertyparkingplace())){
			djvo.setDef18(null);//无产权车位个数
		}else{
			djvo.setDef18(bbvo.getFnonpropertyparkingplace());//无产权车位个数
		}
		if(bbvo.getFfloor() == null || "null".equals(bbvo.getFfloor())){
			djvo.setDef19(null);//层数
		}else{
			djvo.setDef19(bbvo.getFfloor());//层数
		}
		if(bbvo.getFbuildingheight() == null || "null".equals(bbvo.getFbuildingheight())){
			djvo.setDef20(null);//建筑总高度
		}else{
			djvo.setDef20(bbvo.getFbuildingheight());//建筑总高度
		}
		if(bbvo.getFprice() == null || "null".equals(bbvo.getFprice())){
			djvo.setDef21(null);//均价
		}else{
			djvo.setDef21(bbvo.getFprice());//均价
		}
		if(bbvo.getFlandarea() == null || "null".equals(bbvo.getFlandarea())){
			djvo.setDef22(null);//占地面积
		}else{
			djvo.setDef22(bbvo.getFlandarea());//占地面积
		}
		if(bbvo.getFtotalunderarea() == null || "null".equals(bbvo.getFtotalunderarea())){
			djvo.setDef23(null);//测绘总面积
		}else{
			djvo.setDef23(bbvo.getFtotalunderarea());//测绘总面积
		}
		if(bbvo.getFtotalunderarea() == null || "null".equals(bbvo.getFtotalunderarea())){
			djvo.setDef24(null);//规划总面积
		}else{
			djvo.setDef24(bbvo.getFtotalunderarea());//规划总面积
		}
		if(bbvo.getFsharerate() == null || "null".equals(bbvo.getFsharerate())){
			djvo.setDef25(null);//分摊系数
		}else{
			djvo.setDef25(bbvo.getFsharerate());//分摊系数
		}
		if(bbvo.getFunderfloorcount() == null || "null".equals(bbvo.getFunderfloorcount())){
			djvo.setDef26(null);//主体架构
		}else{
			djvo.setDef26(bbvo.getFunderfloorcount());//主体架构
		}
		djvo.setStatus(VOStatus.NEW);
		djvo.setDr(0);
		djvo.setTs(new UFDateTime());
		return djvo;
	}
	/**过滤空值地下室计容  */
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
	/**过滤空值公建配套 */
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
	/**过滤空值办公计容 */
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
	/**过滤空值商业计容 */
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
	/**过滤空值住宅计容 */
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
	/**过滤空值项目分期*/
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
		// 由于主数据运行会有分页查询,为确保数据完整,故此处使用循环获取分页,一旦获取成功并且返回的data为空则跳出循环
		for (int i = 1; i < 99999; i++) {
			// String urls = util.readValue("MIANURL")
			// +"&currpage="+i+""
			// +"&syscode="+util.readValue("SYSCODE")
			// +"&token="+initElementInv();
			String urls = mianurl + "?code=blockBuildingInfo&currpage="+i+"&syscode=" + syscode + "&token="
					+ initToken(token);
			// 创建url资源
			URL url = new URL(urls);
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置允许输出
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// 设置不用缓存
			conn.setUseCaches(false);
			// 设置传递方式
			conn.setRequestMethod("POST");
			// 设置维持长连接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置文件字符集:
			conn.setRequestProperty("Charset", "utf-8");
			// // 转换为字节数组
			// byte[] data = json.getBytes("utf-8");
			// 设置文件长度
			conn.setRequestProperty("Content-Length",
					String.valueOf(urls.length()));

			// 设置文件类型:
			conn.setRequestProperty("Content-Type", "application/json");

			// 开始连接请求
			conn.connect();
			String a = "";

			if (conn.getResponseCode() == 200) {
				Logger.error("连接成功");
				// 请求返回的数据
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
						logVO.setSrcparm("第" + i + "条：" + result.toString());
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
							pvo.setAttributeValue("id", "");// 项目id
						}else{
							pvo.setAttributeValue("id", map.get("ID").toString());// 项目id
						}
						pvo.setAttributeValue("fpbid", map.get("FPBID").toString());//项目分期；关联表：WBD_ProjectBlock_T
						if(map.get("FNUMBER") == null){
							pvo.setAttributeValue("fnumber", "");//楼栋号
						}else{
							pvo.setAttributeValue("fnumber", map.get("FNUMBER").toString());//楼栋号
						}
						if(map.get("FMARKETNUMBER") == null){
							pvo.setAttributeValue("fmarketnumber", "");//营销楼号
						}else{
							pvo.setAttributeValue("fmarketnumber", map.get("FMARKETNUMBER").toString());//营销楼号
						}
						if(map.get("FPRDID") == null){
							pvo.setAttributeValue("fprdid", "");//产品类型ID；关联表：WBD_Produce_T(关联业态)
						}else{
							pvo.setAttributeValue("fprdid", map.get("FPRDID").toString());//产品类型ID；关联表：WBD_Produce_T(关联业态)
						}
						if(map.get("FTENDER") == null){
							pvo.setAttributeValue("ftender", "");//标段
						}else{
							pvo.setAttributeValue("ftender", map.get("FTENDER").toString());//标段
						}
						if(map.get("FSALEABLE") == null){
							pvo.setAttributeValue("fsaleable", "");//是否可售；1 = 可售、0 = 不可售
						}else{
							pvo.setAttributeValue("fsaleable", map.get("FSALEABLE").toString());//是否可售；1 = 可售、0 = 不可售
						}
						if(map.get("FBUILDINGAREA") == null){
							pvo.setAttributeValue("fbuildingarea", "");//建筑面积
						}else{
							pvo.setAttributeValue("fbuildingarea", map.get("FBUILDINGAREA").toString());//建筑面积
						}
						if(map.get("FFACTPLOTAREA") == null){
							pvo.setAttributeValue("ffactplotarea", "");//计容面积
						}else{
							pvo.setAttributeValue("ffactplotarea", map.get("FFACTPLOTAREA").toString());//计容面积
						}
						if(map.get("FFACTNONPLOTAREA") == null){
							pvo.setAttributeValue("ffactnonplotarea", "");//不计容面积
						}else{
							pvo.setAttributeValue("ffactnonplotarea", map.get("FFACTNONPLOTAREA").toString());//不计容面积
						}
						if(map.get("FSALEABLEAREA") == null){
							pvo.setAttributeValue("fsaleablearea", "");//可售面积
						}else{
							pvo.setAttributeValue("fsaleablearea", map.get("FSALEABLEAREA").toString());//可售面积
						}
						if(map.get("FNONSALEABLEAREA") != null){
							pvo.setAttributeValue("fnonsaleablearea", "");//不可售面积
						}else{
							pvo.setAttributeValue("fnonsaleablearea", map.get("FNONSALEABLEAREA").toString());//不可售面积
						}
						if(map.get("FOPERFLOORAREA") == null){
							pvo.setAttributeValue("foperfloorarea", "");//架空面积
						}else{
							pvo.setAttributeValue("foperfloorarea", map.get("FOPERFLOORAREA").toString());//架空面积
						}
						if(map.get("FHALLAREA") == null){
							pvo.setAttributeValue("fhallarea", "");//入户大堂面积
						}else{
							pvo.setAttributeValue("fhallarea", map.get("FHALLAREA").toString());//入户大堂面积
						}
						if(map.get("FBUILDINGBASEAREA") == null){
							pvo.setAttributeValue("fbuildingbasearea", "");//基底面积
						}else{
							pvo.setAttributeValue("fbuildingbasearea", map.get("FBUILDINGBASEAREA").toString());//基底面积
						}
						if(map.get("FCOMMONNUMBER") == null){
							pvo.setAttributeValue("fcommonnumber", "");//普通住宅套数
						}else {
							pvo.setAttributeValue("fcommonnumber", map.get("FCOMMONNUMBER").toString());//普通住宅套数
						}
						if(map.get("FUNCOMMONNUMBER") == null){
							pvo.setAttributeValue("funcommonnumber", "");//非普通住宅套数
						}else{
							pvo.setAttributeValue("funcommonnumber", map.get("FUNCOMMONNUMBER").toString());//非普通住宅套数
						}
						if(map.get("FUNITNUMBER") == null){
							pvo.setAttributeValue("funitnumber","");//单元数
						}else{
							pvo.setAttributeValue("funitnumber", map.get("FUNITNUMBER").toString());//单元数
						}
						if(map.get("FROOMNUMBER") == null){
							pvo.setAttributeValue("froomnumber", "");//总套数
						}else{
							pvo.setAttributeValue("froomnumber", map.get("FROOMNUMBER").toString());//总套数
						}
						if(map.get("FFLOOR") == null){
							pvo.setAttributeValue("ffloor", "");//层数-地上层数
						}else{
							pvo.setAttributeValue("ffloor", map.get("FFLOOR").toString());//层数-地上层数
						}
						if(map.get("FBUILDINGHEIGHT") == null){
							pvo.setAttributeValue("fbuildingheight", "");//建筑总高度
						}else{
							pvo.setAttributeValue("fbuildingheight", map.get("FBUILDINGHEIGHT").toString());//建筑总高度
						}
						if(map.get("FINTERNALAREA") == null){
							pvo.setAttributeValue("finternalarea", "");//套内面积
						}else{
							pvo.setAttributeValue("finternalarea", map.get("FINTERNALAREA").toString());//套内面积
						}
						if(map.get("FAPPORTIONAREA") == null){
							pvo.setAttributeValue("fapportionarea", "");//分摊面积
						}else{
							pvo.setAttributeValue("fapportionarea", map.get("FAPPORTIONAREA").toString());//分摊面积
						}
						if(map.get("FBLOCKSTAGE") == null){
							pvo.setAttributeValue("fblockstage", "");//开发阶段；代码：WBD_BlockStage
						}else{
							pvo.setAttributeValue("fblockstage", map.get("FBLOCKSTAGE").toString());//开发阶段；代码：WBD_BlockStage
						}
						if(map.get("FBUILDGROUP") == null){
							pvo.setAttributeValue("fbuildgroup", "");//组团 代码表：WBD_BuildGroup
						}else{
							pvo.setAttributeValue("fbuildgroup", map.get("FBUILDGROUP").toString());//组团 代码表：WBD_BuildGroup
						}
						if(map.get("FTOTALUNDERAREA") == null){
							pvo.setAttributeValue("ftotalunderarea", "");//地下总面积（车库时候使用）测绘和规划总面积
						}else{
							pvo.setAttributeValue("ftotalunderarea", map.get("FTOTALUNDERAREA").toString());//均价
						}
						if(map.get("FPROPERTYPARKINGPLACE") == null){
							pvo.setAttributeValue("fpropertyparkingplace", "");//产权车位个数（车库使用）产权车位个数
						}else{
							pvo.setAttributeValue("fpropertyparkingplace", map.get("FPROPERTYPARKINGPLACE").toString());//均价
						}
						if(map.get("FNONPROPERTYPARKINGPLACE") == null){
							pvo.setAttributeValue("fnonpropertyparkingplace", "");//非产权车位个数（车库使用）无产权车位个数
						}else{
							pvo.setAttributeValue("fnonpropertyparkingplace", map.get("FNONPROPERTYPARKINGPLACE").toString());//均价
						}
						if(map.get("FPRICE") == null){
							pvo.setAttributeValue("fprice", "");//均价
						}else{
							pvo.setAttributeValue("fprice", map.get("FPRICE").toString());//均价
						}
						if(map.get("FUNDERFLOORCOUNT") == null){
							pvo.setAttributeValue("funderfloorcount", "");//地下层数
						}else{
							pvo.setAttributeValue("funderfloorcount", map.get("FUNDERFLOORCOUNT").toString());//地下层数
						}
						if(map.get("FIRSTSHOP") == null){
							pvo.setAttributeValue("firstshop", "");//首层商铺面积
						}else{
							pvo.setAttributeValue("firstshop", map.get("FIRSTSHOP").toString());//主体结构
						}
						if(map.get("SECONDSHOP") == null){
							pvo.setAttributeValue("secondshop", "");//二层及二层以上面积
						}else{
							pvo.setAttributeValue("secondshop", map.get("SECONDSHOP").toString());//主体结构
						}
						if(map.get("FADDAREA") == null){
							pvo.setAttributeValue("faddarea", "");//加建面积
						}else{
							pvo.setAttributeValue("faddarea", map.get("FADDAREA").toString());//主体结构
						}
						if(map.get("FHIREYEARS") == null){
							pvo.setAttributeValue("fhireyears", "");//承租年限
						}else{
							pvo.setAttributeValue("fhireyears", map.get("FHIREYEARS").toString());//主体结构
						}
						if(map.get("FFIRSTSTEPUNIT") == null){
							pvo.setAttributeValue("ffirststepunit", "");//首层单元数
						}else{
							pvo.setAttributeValue("ffirststepunit", map.get("FFIRSTSTEPUNIT").toString());//主体结构
						}
						if(map.get("FSECONDSTEPUNIT") == null){
							pvo.setAttributeValue("fsecondstepunit", "");//二层以上单元数
						}else{
							pvo.setAttributeValue("fsecondstepunit", map.get("FSECONDSTEPUNIT").toString());//主体结构
						}
						if(map.get("FHIRERATIO") == null){
							pvo.setAttributeValue("fhireratio", "");//租赁加成系数 加成系数
						}else{
							pvo.setAttributeValue("fhireratio", map.get("FHIRERATIO").toString());//主体结构
						}
						if(map.get("FLANDAREA") == null){
							pvo.setAttributeValue("flandarea", "");//占地面积
						}else{
							pvo.setAttributeValue("flandarea", map.get("FLANDAREA").toString());//主体结构
						}
						if(map.get("FMAINSTRUCTURE") == null){
							pvo.setAttributeValue("fmainstructure", "");//主体结构
						}else{
							pvo.setAttributeValue("fmainstructure", map.get("FMAINSTRUCTURE").toString());//主体结构
						}
						if(map.get("FSHARERATE") == null){
							pvo.setAttributeValue("fsharerate", "");//分摊系数
						}else{
							pvo.setAttributeValue("fsharerate", map.get("FSHARERATE").toString());//分摊系数
						}
						if(map.get("FPRODUCTFEATURE") == null){
							pvo.setAttributeValue("FproductFeature", "");//功能中文名称(指标卡)（业态）
						}else{
							pvo.setAttributeValue("FproductFeature", map.get("FPRODUCTFEATURE").toString());//功能中文名称(指标卡)（业态）
						}
//						pvo.setAttributeValue("dr", new Integer(0));
//						pvo.setStatus(VOStatus.NEW);
						bodylists.get(map.get("ID").toString()).add(pvo);
					}
				} else {
					errmsg = (String) result.getString("msg");
					throw new BusinessException("【来自主数据的错误信息：" + errmsg + "】");
				}
			} else {
				Logger.error("连接失败");
				throw new BusinessException("连接失败");
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
		// 由于主数据运行会有分页查询,为确保数据完整,故此处使用循环获取分页,一旦获取成功并且返回的data为空则跳出循环
		for (int i = 1; i < 99999; i++) {
			// String urls = util.readValue("MIANURL")
			// +"&currpage="+i+""
			// +"&syscode="+util.readValue("SYSCODE")
			// +"&token="+initElementInv();
			String urls = mianurl + "?code=projectBlockInfo&currpage="+i+"&syscode=" + syscode + "&token="
					+ initToken(token);
			// 创建url资源
			URL url = new URL(urls);
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置允许输出
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// 设置不用缓存
			conn.setUseCaches(false);
			// 设置传递方式
			conn.setRequestMethod("POST");
			// 设置维持长连接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置文件字符集:
			conn.setRequestProperty("Charset", "utf-8");
			// // 转换为字节数组
			// byte[] data = json.getBytes("utf-8");
			// 设置文件长度
			conn.setRequestProperty("Content-Length",
					String.valueOf(urls.length()));

			// 设置文件类型:
			conn.setRequestProperty("Content-Type", "application/json");

			// 开始连接请求
			conn.connect();
			String a = "";

			if (conn.getResponseCode() == 200) {
				Logger.error("连接成功");
				// 请求返回的数据
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
						logVO.setSrcparm("第" + i + "条：" + result.toString());
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
							pvo.setAttributeValue("fprjid",map.get("FPRJID"));//所属项目ID；关联表：WBD_Project_T
						}else{
							pvo.setAttributeValue("fprjid",map.get("FPRJID").toString());//所属项目ID；关联表：WBD_Project_T
						}
						
						pvo.setAttributeValue("id", map.get("ID").toString());// 项目分期id
						
						if(map.get("FNUMBER") != null){
							pvo.setAttributeValue("fnumber", map.get("FNUMBER"));//分期编码
						}
						if(map.get("FNAME") != null){
							pvo.setAttributeValue("fname", map.get("FNAME"));//分期名称
						}
						if(map.get("FPRODUCTSERIES") == null){
							pvo.setAttributeValue("fproductseries", "");//产品系列；代码：WBD_ProductSeries
						}else{
							pvo.setAttributeValue("fproductseries", map.get("FPRODUCTSERIES").toString());//产品系列；代码：WBD_ProductSeries
						}
						if(map.get("FDESCRIPTION") == null){
							pvo.setAttributeValue("fdescription", "");//说明
						}else{
							pvo.setAttributeValue("fdescription", map.get("FDESCRIPTION").toString());//说明
						}
						if(map.get("FREMARK") == null){
							pvo.setAttributeValue("fremark", "");//分期备案名
						}else{
							pvo.setAttributeValue("fremark", map.get("FREMARK").toString());//分期备案名
						}
//						pvo.setAttributeValue("dr", new Integer(0));
//						pvo.setStatus(VOStatus.NEW);
						bodylists.get(map.get("ID").toString()).add(pvo);
					}
				} else {
					errmsg = (String) result.getString("msg");
					throw new BusinessException("【来自主数据的错误信息：" + errmsg + "】");
				}
			} else {
				Logger.error("连接失败");
				throw new BusinessException("连接失败");
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
		//   楼栋：blockBuildingInfo
		// 项目：projectInfo http://times-uat.timesgroup.cn:8080/method?code=projectInfo&currpage=1&syscode=invest&token=9087698DEA1E3BD838762D84373733F0
		//项目分期：projectBlockInfo http://times-uat.timesgroup.cn:8080/method?code=projectBlockInfo&currpage=1&syscode=design&token=D8BB8A73678A4E37E8D7FC4B6F9C57F3
//		String urlstr = mianurl + "?code=projectBlockInfo&currpage=1&syscode=" + syscode + "&token="
//				+ initToken(token);

		Map<String, List<ProjectInfoVO>> bodylists = new HashMap<String, List<ProjectInfoVO>>();
		// 由于主数据运行会有分页查询,为确保数据完整,故此处使用循环获取分页,一旦获取成功并且返回的data为空则跳出循环
		int i = 1;
		for (; i < 99999; i++) {
			// String urls = util.readValue("MIANURL")
			// +"&currpage="+i+""
			// +"&syscode="+util.readValue("SYSCODE")
			// +"&token="+initElementInv();
			String urls = mianurl + "?code=projectInfo&currpage="+i+"&syscode=" + syscode + "&token="
					+ initToken(token);
			// 创建url资源
			URL url = new URL(urls);
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置允许输出
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// 设置不用缓存
			conn.setUseCaches(false);
			// 设置传递方式
			conn.setRequestMethod("POST");
			// 设置维持长连接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置文件字符集:
			conn.setRequestProperty("Charset", "utf-8");
			// // 转换为字节数组
			// byte[] data = json.getBytes("utf-8");
			// 设置文件长度
			conn.setRequestProperty("Content-Length",
					String.valueOf(urls.length()));

			// 设置文件类型:
			conn.setRequestProperty("Content-Type", "application/json");

			// 开始连接请求
			conn.connect();
			String a = "";

			if (conn.getResponseCode() == 200) {
				Logger.error("连接成功");
				// 请求返回的数据
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
						logVO.setSrcparm("第" + i + "条：" + result.toString());
						break;
					}
					JSONArray resMap = result.getJSONArray("data");
					net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
							.fromObject(resMap.toString());
					@SuppressWarnings({ "unchecked", "rawtypes" })
					List<Map<String, Object>> mapListJson = (List) jsonArray;
					for (Map<String, Object> map : mapListJson) {

						if (!bodylists.containsKey(map.get("ID"))) {// 项目id
							bodylists.put(map.get("ID").toString(),
									new ArrayList<ProjectInfoVO>());
						}
						ProjectInfoVO pvo = new ProjectInfoVO();
						pvo.setAttributeValue("id", map.get("ID").toString());// 项目id
						if(map.get("FNUMBER") != null){
							pvo.setAttributeValue("fnumber", map.get("FNUMBER"));//项目编码
						}
						if(map.get("FNAME") != null){
							pvo.setAttributeValue("fname", map.get("FNAME"));//项目名称
						}
						if("null"
								.equals(map.get("FPRODUCTSERIES") != null ? map
										.get("FPRODUCTSERIES").toString()
										: "null")
								|| map.get("FPRODUCTSERIES") == null
								|| "".equals(map.get("FPRODUCTSERIES"))){
							pvo.setAttributeValue("fproductseries", "");//产品系列；代码：WBD_ProductSeries
						}else{
							pvo.setAttributeValue("fproductseries",map.get("FPRODUCTSERIES"));//产品系列；代码：WBD_ProductSeries
						}
						if(map.get("FVERSION") == null){
							pvo.setAttributeValue("fversion", "");//版本
						}else{
							pvo.setAttributeValue("fversion", map.get("FVERSION").toString());//版本
						}
						if(map.get("FADDRESS") == null){
							pvo.setAttributeValue("faddress", "");//项目地址
						}else{
							pvo.setAttributeValue("faddress", map.get("FADDRESS").toString());//项目地址
						}
						if(map.get("FDESCRIPTION") == null){
							pvo.setAttributeValue("fdescription", "");//说明
						}else{
							pvo.setAttributeValue("fdescription", map.get("FDESCRIPTION").toString());//说明
						}
						if(map.get("FLANDAREA") == null){
							pvo.setAttributeValue("flandarea","");//净用地面积*
						}else{
							pvo.setAttributeValue("flandarea", map.get("FLANDAREA").toString());//净用地面积*
						}
						if(map.get("FNAME1") == null){
							pvo.setAttributeValue("fname1", "");//曾用名1--报建名
						}else{
							pvo.setAttributeValue("fname1", map.get("FNAME1").toString());//曾用名1--报建名
						}
						if(map.get("FNAME2") == null){
							pvo.setAttributeValue("fname2", "");//曾用名2--营销用名
						}else{
							pvo.setAttributeValue("fname2", map.get("FNAME2").toString());//曾用名2--营销用名
						}
						if(map.get("FPLANTOTALAREA") != null){
							pvo.setAttributeValue("fplantotalarea", "");//规划总用地面积
						}else{
							pvo.setAttributeValue("fplantotalarea", map.get("FPLANTOTALAREA").toString());//规划总用地面积
						}
						if(map.get("FGLOSSAREA") == null){
							pvo.setAttributeValue("fglossarea", "");//毛容积率
						}else{
							pvo.setAttributeValue("fglossarea", map.get("FGLOSSAREA").toString());//毛容积率
						}
						if(map.get("FNETAREA") == null){
							pvo.setAttributeValue("fnetarea", "");//净容积率
						}else{
							pvo.setAttributeValue("fnetarea", map.get("FNETAREA").toString());//毛容积率
						}
						if(map.get("FGREENRATE") == null){
							pvo.setAttributeValue("fgreenrate", "");//绿地率
						}else{
							pvo.setAttributeValue("fgreenrate", map.get("FGREENRATE").toString());//绿地率
						}
						if(map.get("FCONTROLHIGHT") == null && !"".equals(map.get("FCONTROLHIGHT"))){
							pvo.setAttributeValue("fcontrolhight", "");//建筑控高
						}else{
							pvo.setAttributeValue("fcontrolhight", map.get("FCONTROLHIGHT").toString());//建筑控高
						}
						if(map.get("FGLOSSDENSITY") == null){
							pvo.setAttributeValue("fglossdensity", "");//毛建筑密度
						}else{
							pvo.setAttributeValue("fglossdensity", map.get("FGLOSSDENSITY").toString());//毛建筑密度
						}
						if(map.get("FNETDENSITY") == null){
							pvo.setAttributeValue("fnetdensity", "");//净建筑密度
						}else{
							pvo.setAttributeValue("fnetdensity", map.get("FNETDENSITY").toString());//净建筑密度
						}
						if(map.get("FPROJECTSTATUS_CN") == null){
							pvo.setAttributeValue("Fprojectstatus_cn", "");//项目状态（中文）
						}else{
							pvo.setAttributeValue("Fprojectstatus_cn", map.get("FPROJECTSTATUS_CN").toString());//项目状态（中文）
						}
						if(map.get("FREGION_NAME") == null){
							pvo.setAttributeValue("Fregion_name", "");//区域名称
						}else{
							pvo.setAttributeValue("Fregion_name", map.get("FREGION_NAME").toString());//区域名称
						}
						if(map.get("FPRODUCTSERIES_NAME") == null){
							pvo.setAttributeValue("fproductseries_name", "");//产品系列名称
						}else{
							pvo.setAttributeValue("fproductseries_name", map.get("FPRODUCTSERIES_NAME").toString());//产品系列名称
						}
						if(map.get("ORG_NAME") == null){
							pvo.setAttributeValue("org_name", "");//项目公司名称
						}else{
							pvo.setAttributeValue("org_name", map.get("ORG_NAME").toString());//项目公司名称
						}
						if(map.get("COUNTRY_NAME") == null){
							pvo.setAttributeValue("country_name", "");//国家
						}else{
							pvo.setAttributeValue("country_name", map.get("COUNTRY_NAME").toString());//国家
						}
						if(map.get("PROVIENCE_NAME") == null){
							pvo.setAttributeValue("provience_name", "");//省份
						}else{
							pvo.setAttributeValue("provience_name", map.get("PROVIENCE_NAME").toString());//省份
						}
						if(map.get("CITY_NAME") == null){
							pvo.setAttributeValue("city_name", "");//城市
						}else{
							pvo.setAttributeValue("city_name", map.get("CITY_NAME").toString());//城市
						}						
//						pvo.setAttributeValue("dr", new Integer(0));
//						pvo.setStatus(VOStatus.NEW);
						bodylists.get(map.get("ID").toString()).add(pvo);
					
					}
					System.out.println(i);
				} else {
					errmsg = (String) result.getString("msg");
					throw new BusinessException("【来自主数据的错误信息：" + errmsg + "】");
				}
			} else {
				Logger.error("连接失败");
				throw new BusinessException("连接失败");
			}
		}
		System.out.println(i);
		return bodylists;
	}
	/**
	 * 初始化token信息
	 * 
	 * @param servername
	 * @return
	 */
	private String initToken(String key) {

		Date date = new UFDate().toDate();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
		String time = formater.format(date);
		String ticket = time + key;
		String ticketMD5 = DigestUtils.md5Hex(ticket).toUpperCase();// 32位大写
		// String ticketMD5 = MD5Util.getMD5(ticket).toUpperCase();
		return ticketMD5;
	}
}
