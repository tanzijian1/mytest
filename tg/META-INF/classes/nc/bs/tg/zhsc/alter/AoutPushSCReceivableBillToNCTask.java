
package nc.bs.tg.zhsc.alter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.os.outside.TGCallUtils;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.itf.tg.bd.pub.IPlanAndRealityConst;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.tg.fischeme.CapmarketBVO;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.fischeme.FISchemeBVO;
import nc.vo.tg.fischeme.NFISchemeBVO;
import nc.vo.tg.projectdata.ProjectDataFBVO;
import nc.vo.tg.projectdata.ProjectDataMVO;
import nc.vo.tg.projectdata.ProjectDataNVO;

import org.apache.commons.lang.StringUtils;

public class AoutPushSCReceivableBillToNCTask implements IBackgroundWorkPlugin{
	
	BaseDAO baseDAO = null;
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	
	IMDPersistenceQueryService queryServcie = null;
	
	
	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		
		HashMap<String, Object> km = bgwc.getKeyMap();
		Set<Entry<String, Object>> name = km.entrySet();
		for (Entry<String, Object> entry : name) {
		System.out.print(entry.getKey() + "=");
		System.out.print(entry.getValue() + "\n");
		}
		
		TGCallUtils.getUtils().onDesCallService("", "zhsc", "getReceiveBill", "");
		
		// leijun+
		// longtimeWork(50000);//hzg--
		PreAlertObject retObj = new PreAlertObject();
		retObj.setReturnObj("业务插件成功执行完毕.");
		retObj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
		return retObj;
		
		


		
//		Collection<AggFIScemeHVO> list = (Collection<AggFIScemeHVO>) retrieveAll(AggFIScemeHVO.class);
//		String fischemeTimeTypePk = "";
//		for (AggFIScemeHVO aggvo : list) {
//			FIScemeHVO hvo = aggvo.getParentVO();
//			FISchemeBVO[] bvos  = (FISchemeBVO[]) aggvo.getChildren(FISchemeBVO.class);
//			NFISchemeBVO[] nvos  = (NFISchemeBVO[]) aggvo.getChildren(NFISchemeBVO.class);
//			CapmarketBVO[] cvos  = (CapmarketBVO[]) aggvo.getChildren(CapmarketBVO.class);
//			String pk_project=hvo.getPk_project();
//			String tableName = "";
//			if(bvos!=null){
//				tableName = "tg_fischemepushstandard_b";
//				if(bvos.length>=3){
//					if(bvos[1].getVbdef5()==null){//若实际批复时间为空,则第一行计划数据进行重新铺排并更新项目资料
//						bvos = handleFischemeBodyToFbFirstAppDate(bvos,hvo,tableName,fischemeTimeTypePk);//根据第一次审核时间刷新融资方案表体
//						List<ProjectDataFBVO> aggFhvoList= (List<ProjectDataFBVO>) retrieveByPkToFb(pk_project,hvo,bvos);
//						getBaseDAO().updateVOArray(bvos);
//						getBaseDAO().updateVOList(aggFhvoList);
//					}
//					
//				}
//			}
//			if(nvos!=null){
//				tableName= "tg_fischemepushstandard_n";
//				if(nvos.length>=3){
//					if(nvos[1].getDef5()==null){//若实际批复时间为空,则第一行计划数据进行重新铺排并更新项目资料
//						nvos = handleFischemeBodyToNFirstAppDate(nvos,hvo,tableName,fischemeTimeTypePk);//根据第一次审核时间刷新融资方案表体
//						List<ProjectDataNVO> aggNvoList= (List<ProjectDataNVO>) retrieveByPkToN(pk_project,hvo,nvos);
//						getBaseDAO().updateVOArray(nvos);
//						getBaseDAO().updateVOList(aggNvoList);
//					}
//				}
//			}
//			if(cvos!=null){
//				tableName= "tg_fischemepushstandard_c";
//				if(cvos.length>=3){
//					if(cvos[1].getDef5()==null){//若实际批复时间为空,则第一行计划数据进行重新铺排并更新项目资料
//						cvos = handleFischemeBodyToCFirstAppDate(cvos,hvo,tableName,fischemeTimeTypePk);//根据第一次审核时间刷新融资方案表体
//						List<ProjectDataMVO> aggNvoList= (List<ProjectDataMVO>) retrieveByPkToC(pk_project,hvo,cvos);
//						getBaseDAO().updateVOArray(cvos);
//						getBaseDAO().updateVOList(aggNvoList);
//					}
//				}
//			}
//		}
//		return null;
	}
	
	private List<ProjectDataMVO> retrieveByPkToC(String pk_project,
			FIScemeHVO hvo, CapmarketBVO[] cvos) throws BusinessException {
		List<ProjectDataMVO> mvos= (List<ProjectDataMVO>) getQueryServcie().queryBillOfVOByCond(ProjectDataMVO.class, " nvl(dr,0) = 0 and pk_projectdata = '"+pk_project+"' and def9 = '"+hvo.getBillno()+"' ", true, false);
		for (int i=0;i<mvos.size();i++) {
			mvos.get(i).setStatus(VOStatus.UPDATED);
			mvos.get(i).setDef1(cvos[i].getDef1());
			mvos.get(i).setDef2(cvos[i].getDef2());
			mvos.get(i).setDef3(cvos[i].getDef3());
			mvos.get(i).setDef4(cvos[i].getDef4());
			mvos.get(i).setDef5(cvos[i].getDef5());
			mvos.get(i).setDef6(cvos[i].getDef6());
			mvos.get(i).setDef7(cvos[i].getDef7());
			mvos.get(i).setDef8(cvos[i].getDef8());
			mvos.get(i).setDef13(cvos[i].getDef13());
		}
		return mvos;
	}
	
	private CapmarketBVO[] handleFischemeBodyToCFirstAppDate(
			CapmarketBVO[] cvos, FIScemeHVO hvo, String tableName,
			String fischemeTimeTypePk) {
		if(getCode(hvo.getPk_organization())!=null){
			Map<String, Object> bMap = getRow(tableName,(String) getCode(hvo.getPk_organization()).get("pk_defdoc"));
			if(bMap!=null){
				if(getHeadVoForPk((String)bMap.get("pk_fispushstandard"))!=null){
					fischemeTimeTypePk = (String) getHeadVoForPk((String)bMap.get("pk_fispushstandard")).get("def2");
				}
				//根据第一次审核时间更新
				if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"002".equals(getDefdocCode(fischemeTimeTypePk))){
					if(bMap!=null&&StringUtils.isNotBlank(hvo.getVdef2())){
						cvos[0].setStatus(VOStatus.UPDATED);
						cvos[1].setStatus(VOStatus.UPDATED);
						cvos[2].setStatus(VOStatus.UPDATED);
						cvos[0].setDef1(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")).intValue()).toString().substring(0,11));
						cvos[0].setDef2(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")).intValue()).toString().substring(0,11));
						cvos[0].setDef3(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")).intValue()).toString().substring(0,11));
						cvos[0].setDef4(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")).intValue()).toString().substring(0,11));
						cvos[0].setDef5(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")).intValue()).toString().substring(0,11));
						cvos[0].setDef6(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")).intValue()).toString().substring(0,11));
						cvos[0].setDef7(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")).intValue()).toString().substring(0,11));
						cvos[0].setDef13(IPlanAndRealityConst.PlanTime);
						cvos[1].setDef13(IPlanAndRealityConst.RealityTime);
						cvos[2].setDef13(IPlanAndRealityConst.Days);
						if(StringUtils.isNotBlank(cvos[0].getDef1())&&StringUtils.isNotBlank(cvos[1].getDef1())){
							cvos[2].setDef1(getValue(cvos[0].getDef1(),cvos[1].getDef1()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef2())&&StringUtils.isNotBlank(cvos[1].getDef2())){
							cvos[2].setDef2(getValue(cvos[0].getDef2(),cvos[1].getDef2()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef3())&&StringUtils.isNotBlank(cvos[1].getDef3())){
							cvos[2].setDef3(getValue(cvos[0].getDef3(),cvos[1].getDef3()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef4())&&StringUtils.isNotBlank(cvos[1].getDef4())){
							cvos[2].setDef4(getValue(cvos[0].getDef4(),cvos[1].getDef4()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef5())&&StringUtils.isNotBlank(cvos[1].getDef5())){
							cvos[2].setDef5(getValue(cvos[0].getDef5(),cvos[1].getDef5()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef6())&&StringUtils.isNotBlank(cvos[1].getDef6())){
							cvos[2].setDef6(getValue(cvos[0].getDef6(),cvos[1].getDef6()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef7())&&StringUtils.isNotBlank(cvos[1].getDef7())){
							cvos[2].setDef7(getValue(cvos[0].getDef7(),cvos[1].getDef7()));
						}
						
					}
				}
				//根据单据时间更新
				if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"001".equals(getDefdocCode(fischemeTimeTypePk))){
					if(bMap!=null&&StringUtils.isNotBlank(hvo.getVdef2())){
						cvos[0].setStatus(VOStatus.UPDATED);
						cvos[1].setStatus(VOStatus.UPDATED);
						cvos[2].setStatus(VOStatus.UPDATED);
						cvos[0].setDef1(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")).intValue()).toString().substring(0,11));
						cvos[0].setDef2(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")).intValue()).toString().substring(0,11));
						cvos[0].setDef3(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")).intValue()).toString().substring(0,11));
						cvos[0].setDef4(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")).intValue()).toString().substring(0,11));
						cvos[0].setDef5(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")).intValue()).toString().substring(0,11));
						cvos[0].setDef6(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")).intValue()).toString().substring(0,11));
						cvos[0].setDef7(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")).intValue()).toString().substring(0,11));
						cvos[0].setDef13(IPlanAndRealityConst.PlanTime);
						cvos[1].setDef13(IPlanAndRealityConst.RealityTime);
						cvos[2].setDef13(IPlanAndRealityConst.Days);
						if(StringUtils.isNotBlank(cvos[0].getDef1())&&StringUtils.isNotBlank(cvos[1].getDef1())){
							cvos[2].setDef1(getValue(cvos[0].getDef1(),cvos[1].getDef1()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef2())&&StringUtils.isNotBlank(cvos[1].getDef2())){
							cvos[2].setDef2(getValue(cvos[0].getDef2(),cvos[1].getDef2()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef3())&&StringUtils.isNotBlank(cvos[1].getDef3())){
							cvos[2].setDef3(getValue(cvos[0].getDef3(),cvos[1].getDef3()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef4())&&StringUtils.isNotBlank(cvos[1].getDef4())){
							cvos[2].setDef4(getValue(cvos[0].getDef4(),cvos[1].getDef4()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef5())&&StringUtils.isNotBlank(cvos[1].getDef5())){
							cvos[2].setDef5(getValue(cvos[0].getDef5(),cvos[1].getDef5()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef6())&&StringUtils.isNotBlank(cvos[1].getDef6())){
							cvos[2].setDef6(getValue(cvos[0].getDef6(),cvos[1].getDef6()));
						}
						if(StringUtils.isNotBlank(cvos[0].getDef7())&&StringUtils.isNotBlank(cvos[1].getDef7())){
							cvos[2].setDef7(getValue(cvos[0].getDef7(),cvos[1].getDef7()));
						}
					}
				}
			}
		}
		return cvos;
	}
	
	private List<ProjectDataNVO> retrieveByPkToN(String pk_project,
			FIScemeHVO hvo, NFISchemeBVO[] nbvos) throws BusinessException {
		List<ProjectDataNVO> nvos= (List<ProjectDataNVO>) getQueryServcie().queryBillOfVOByCond(ProjectDataNVO.class, " nvl(dr,0) = 0 and pk_projectdata = '"+pk_project+"' and def9 = '"+hvo.getBillno()+"' ", true, false);
		for (int i=0;i<nvos.size();i++) {
			nvos.get(i).setStatus(VOStatus.UPDATED);
			nvos.get(i).setDef1(nbvos[i].getDef1());
			nvos.get(i).setDef2(nbvos[i].getDef2());
			nvos.get(i).setDef3(nbvos[i].getDef3());
			nvos.get(i).setDef4(nbvos[i].getDef4());
			nvos.get(i).setDef5(nbvos[i].getDef5());
			nvos.get(i).setDef6(nbvos[i].getDef6());
			nvos.get(i).setDef7(nbvos[i].getDef7());
			nvos.get(i).setDef8(nbvos[i].getDef8());
			nvos.get(i).setDef13(nbvos[i].getDef13());
		}
		return nvos;
	}
	
	/**
	 * 非银页签
	 * @param nvos
	 * @param hvo
	 * @param tableName
	 * @param fischemeTimeTypePk
	 * @return
	 */
	private NFISchemeBVO[] handleFischemeBodyToNFirstAppDate(
			NFISchemeBVO[] nvos, FIScemeHVO hvo, String tableName,
			String fischemeTimeTypePk) {
		if(getCode(hvo.getPk_organization())!=null){
			Map<String, Object> bMap = getRow(tableName,(String) getCode(hvo.getPk_organization()).get("pk_defdoc"));
			if(bMap!=null){
				
			if(getHeadVoForPk((String)bMap.get("pk_fispushstandard"))!=null){
				fischemeTimeTypePk = (String) getHeadVoForPk((String)bMap.get("pk_fispushstandard")).get("def2");
			}
			//根据第一次审核时间更新
			if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"002".equals(getDefdocCode(fischemeTimeTypePk))){
				if(bMap!=null&&StringUtils.isNotBlank(hvo.getVdef2())){
					nvos[0].setStatus(VOStatus.UPDATED);
					nvos[1].setStatus(VOStatus.UPDATED);
					nvos[2].setStatus(VOStatus.UPDATED);
					nvos[0].setDef1(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")).intValue()).toString().substring(0,11));
					nvos[0].setDef2(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")).intValue()).toString().substring(0,11));
					nvos[0].setDef3(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")).intValue()).toString().substring(0,11));
					nvos[0].setDef4(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")).intValue()).toString().substring(0,11));
					nvos[0].setDef5(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")).intValue()).toString().substring(0,11));
					nvos[0].setDef6(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")).intValue()).toString().substring(0,11));
					nvos[0].setDef7(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")).intValue()).toString().substring(0,11));
					nvos[0].setDef13(IPlanAndRealityConst.PlanTime);
					nvos[1].setDef13(IPlanAndRealityConst.RealityTime);
					nvos[2].setDef13(IPlanAndRealityConst.Days);
					if(StringUtils.isNotBlank(nvos[0].getDef1())&&StringUtils.isNotBlank(nvos[1].getDef1())){
						nvos[2].setDef1(getValue(nvos[0].getDef1(),nvos[1].getDef1()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef2())&&StringUtils.isNotBlank(nvos[1].getDef2())){
						nvos[2].setDef2(getValue(nvos[0].getDef2(),nvos[1].getDef2()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef3())&&StringUtils.isNotBlank(nvos[1].getDef3())){
						nvos[2].setDef3(getValue(nvos[0].getDef3(),nvos[1].getDef3()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef4())&&StringUtils.isNotBlank(nvos[1].getDef4())){
						nvos[2].setDef4(getValue(nvos[0].getDef4(),nvos[1].getDef4()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef5())&&StringUtils.isNotBlank(nvos[1].getDef5())){
						nvos[2].setDef5(getValue(nvos[0].getDef5(),nvos[1].getDef5()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef6())&&StringUtils.isNotBlank(nvos[1].getDef6())){
						nvos[2].setDef6(getValue(nvos[0].getDef6(),nvos[1].getDef6()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef7())&&StringUtils.isNotBlank(nvos[1].getDef7())){
						nvos[2].setDef7(getValue(nvos[0].getDef7(),nvos[1].getDef7()));
					}
					
				}
			}
			//根据单据时间更新
			if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"001".equals(getDefdocCode(fischemeTimeTypePk))){
				if(bMap!=null){
					nvos[0].setStatus(VOStatus.UPDATED);
					nvos[1].setStatus(VOStatus.UPDATED);
					nvos[2].setStatus(VOStatus.UPDATED);
					nvos[0].setDef1(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")).intValue()).toString().substring(0,11));
					nvos[0].setDef2(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")).intValue()).toString().substring(0,11));
					nvos[0].setDef3(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")).intValue()).toString().substring(0,11));
					nvos[0].setDef4(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")).intValue()).toString().substring(0,11));
					nvos[0].setDef5(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")).intValue()).toString().substring(0,11));
					nvos[0].setDef6(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")).intValue()).toString().substring(0,11));
					nvos[0].setDef7(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")).intValue()).toString().substring(0,11));
					nvos[0].setDef13(IPlanAndRealityConst.PlanTime);
					nvos[1].setDef13(IPlanAndRealityConst.RealityTime);
					nvos[2].setDef13(IPlanAndRealityConst.Days);
					if(StringUtils.isNotBlank(nvos[0].getDef1())&&StringUtils.isNotBlank(nvos[1].getDef1())){
						nvos[2].setDef1(getValue(nvos[0].getDef1(),nvos[1].getDef1()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef2())&&StringUtils.isNotBlank(nvos[1].getDef2())){
						nvos[2].setDef2(getValue(nvos[0].getDef2(),nvos[1].getDef2()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef3())&&StringUtils.isNotBlank(nvos[1].getDef3())){
						nvos[2].setDef3(getValue(nvos[0].getDef3(),nvos[1].getDef3()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef4())&&StringUtils.isNotBlank(nvos[1].getDef4())){
						nvos[2].setDef4(getValue(nvos[0].getDef4(),nvos[1].getDef4()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef5())&&StringUtils.isNotBlank(nvos[1].getDef5())){
						nvos[2].setDef5(getValue(nvos[0].getDef5(),nvos[1].getDef5()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef6())&&StringUtils.isNotBlank(nvos[1].getDef6())){
						nvos[2].setDef6(getValue(nvos[0].getDef6(),nvos[1].getDef6()));
					}
					if(StringUtils.isNotBlank(nvos[0].getDef7())&&StringUtils.isNotBlank(nvos[1].getDef7())){
						nvos[2].setDef7(getValue(nvos[0].getDef7(),nvos[1].getDef7()));
					}
				}
			}
			}
		}
		return nvos;
	}
	
	/**
	 * 银行页签
	 * @param bvos
	 * @param hvo
	 * @param tableName
	 * @param fischemeTimeTypePk
	 * @return
	 */
	private FISchemeBVO[] handleFischemeBodyToFbFirstAppDate(FISchemeBVO[] bvos,
			FIScemeHVO hvo, String tableName, String fischemeTimeTypePk) {
		if(getCode(hvo.getPk_organization())!=null){
			Map<String, Object> bMap = getRow(tableName,(String) getCode(hvo.getPk_organization()).get("pk_defdoc"));
			if(bMap!=null){
				
				
				if(getHeadVoForPk((String)bMap.get("pk_fispushstandard"))!=null){
					fischemeTimeTypePk = (String) getHeadVoForPk((String)bMap.get("pk_fispushstandard")).get("def2");
				}
				//根据第一次审核时间更新
				if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"002".equals(getDefdocCode(fischemeTimeTypePk))){
					if(bMap!=null&&StringUtils.isNotBlank(hvo.getVdef2())){
						bvos[0].setStatus(VOStatus.UPDATED);
						bvos[1].setStatus(VOStatus.UPDATED);
						bvos[2].setStatus(VOStatus.UPDATED);
						bvos[0].setVbdef1(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef2(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef3(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef4(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef5(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef6(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef7(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")).intValue()).toString().substring(0,11));
						bvos[0].setDef13(IPlanAndRealityConst.PlanTime);
						bvos[1].setDef13(IPlanAndRealityConst.RealityTime);
						bvos[2].setDef13(IPlanAndRealityConst.Days);
						if(StringUtils.isNotBlank(bvos[0].getVbdef1())&&StringUtils.isNotBlank(bvos[1].getVbdef1())){
							bvos[2].setVbdef1(getValue(bvos[0].getVbdef1(),bvos[1].getVbdef1()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef2())&&StringUtils.isNotBlank(bvos[1].getVbdef2())){
							bvos[2].setVbdef2(getValue(bvos[0].getVbdef2(),bvos[1].getVbdef2()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef3())&&StringUtils.isNotBlank(bvos[1].getVbdef3())){
							bvos[2].setVbdef3(getValue(bvos[0].getVbdef3(),bvos[1].getVbdef3()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef4())&&StringUtils.isNotBlank(bvos[1].getVbdef4())){
							bvos[2].setVbdef4(getValue(bvos[0].getVbdef4(),bvos[1].getVbdef4()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef5())&&StringUtils.isNotBlank(bvos[1].getVbdef5())){
							bvos[2].setVbdef5(getValue(bvos[0].getVbdef5(),bvos[1].getVbdef5()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef6())&&StringUtils.isNotBlank(bvos[1].getVbdef6())){
							bvos[2].setVbdef6(getValue(bvos[0].getVbdef6(),bvos[1].getVbdef6()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef7())&&StringUtils.isNotBlank(bvos[1].getVbdef7())){
							bvos[2].setVbdef7(getValue(bvos[0].getVbdef7(),bvos[1].getVbdef7()));
						}
						
					}
				}
				//根据单据时间更新
				if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"001".equals(getDefdocCode(fischemeTimeTypePk))){
					if(bMap!=null){
						bvos[0].setStatus(VOStatus.UPDATED);
						bvos[1].setStatus(VOStatus.UPDATED);
						bvos[2].setStatus(VOStatus.UPDATED);
						bvos[0].setVbdef1(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef2(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef3(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef4(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef5(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef6(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")).intValue()).toString().substring(0,11));
						bvos[0].setVbdef7(hvo.getDbilldate()==null?null:hvo.getDbilldate().getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")).intValue()).toString().substring(0,11));
						bvos[0].setDef13(IPlanAndRealityConst.PlanTime);
						bvos[1].setDef13(IPlanAndRealityConst.RealityTime);
						bvos[2].setDef13(IPlanAndRealityConst.Days);
						if(StringUtils.isNotBlank(bvos[0].getVbdef1())&&StringUtils.isNotBlank(bvos[1].getVbdef1())){
							bvos[2].setVbdef1(getValue(bvos[0].getVbdef1(),bvos[1].getVbdef1()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef2())&&StringUtils.isNotBlank(bvos[1].getVbdef2())){
							bvos[2].setVbdef2(getValue(bvos[0].getVbdef2(),bvos[1].getVbdef2()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef3())&&StringUtils.isNotBlank(bvos[1].getVbdef3())){
							bvos[2].setVbdef3(getValue(bvos[0].getVbdef3(),bvos[1].getVbdef3()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef4())&&StringUtils.isNotBlank(bvos[1].getVbdef4())){
							bvos[2].setVbdef4(getValue(bvos[0].getVbdef4(),bvos[1].getVbdef4()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef5())&&StringUtils.isNotBlank(bvos[1].getVbdef5())){
							bvos[2].setVbdef5(getValue(bvos[0].getVbdef5(),bvos[1].getVbdef5()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef6())&&StringUtils.isNotBlank(bvos[1].getVbdef6())){
							bvos[2].setVbdef6(getValue(bvos[0].getVbdef6(),bvos[1].getVbdef6()));
						}
						if(StringUtils.isNotBlank(bvos[0].getVbdef7())&&StringUtils.isNotBlank(bvos[1].getVbdef7())){
							bvos[2].setVbdef7(getValue(bvos[0].getVbdef7(),bvos[1].getVbdef7()));
						}
					}
				}
			}
		}
		return bvos;
	}
	
	public Collection<?> retrieveAll(Class className) throws BusinessException {
		Collection<Object> aggVOs= getQueryServcie().queryBillOfVOByCond(
				className, " 1=1 ", true, false);// true:ignoreDrEquals1,false:bLazyLoad
		return aggVOs;
	}
	
	public List<ProjectDataFBVO> retrieveByPkToFb(String pk,FIScemeHVO hvo, FISchemeBVO[] bvos) throws BusinessException {
		List<ProjectDataFBVO> fbvo= (List<ProjectDataFBVO>) getQueryServcie().queryBillOfVOByCond(ProjectDataFBVO.class, " nvl(dr,0) = 0 and pk_projectdata = '"+pk+"' and def9 = '"+hvo.getBillno()+"' ", true, false);
		for (int i=0;i<fbvo.size();i++) {
			fbvo.get(i).setStatus(VOStatus.UPDATED);
			fbvo.get(i).setDef1(bvos[i].getVbdef1());
			fbvo.get(i).setDef2(bvos[i].getVbdef2());
			fbvo.get(i).setDef3(bvos[i].getVbdef3());
			fbvo.get(i).setDef4(bvos[i].getVbdef4());
			fbvo.get(i).setDef5(bvos[i].getVbdef5());
			fbvo.get(i).setDef6(bvos[i].getVbdef6());
			fbvo.get(i).setDef7(bvos[i].getVbdef7());
			fbvo.get(i).setDef8(bvos[i].getVbdef8());
			fbvo.get(i).setDef11(bvos[i].getNote());
			fbvo.get(i).setDef13(bvos[i].getDef13());
		}
		return fbvo;
	}
	
	public IMDPersistenceQueryService getQueryServcie() {
		if (queryServcie == null) {
			queryServcie = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return queryServcie;
	}
	
	private Map<String, Object> getHeadVoForPk(String pk_fispushstandard) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from tg_fischemepushstandard_h where pk_fispushstandard = '"+pk_fispushstandard+"'  ");
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result = (Map<String, Object>) getBaseDAO().executeQuery(sql.toString(), new MapProcessor());
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return result;
	}
	
	private Map<String, Object> getRow(String tableName,String pk) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from "+tableName+" where pk_fispushstandard in( ");
		sql.append(" select pk_fispushstandard from tg_fischemepushstandard_h where def1 = '"+pk+"' and def2 <> '~' and def2 is not null and nvl(dr,0) = 0 )   and rownum = 1 order by ts desc ");
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result = (Map<String, Object>) getBaseDAO().executeQuery(sql.toString(), new MapProcessor());
//			pk_defdoc =  (String) getQuery().executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		return result;
	}
	
	private String getDefdocCode(String pk_defdoc) {
		DefdocVO defdocvo = null;
		String code = "";
		try {
			if(StringUtils.isNotBlank(pk_defdoc)){
				defdocvo = (DefdocVO) getBaseDAO().retrieveByPK(DefdocVO.class, pk_defdoc);
				if(defdocvo!=null){
					code  = defdocvo.getCode();
				}
			}
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return code;
	}
	
	private Map<String, Object> getCode(String pk_organization){
		StringBuffer sql = new StringBuffer();
		sql.append("select code,pk_defdoc from bd_defdoc where pk_defdoc in( ");
		sql.append(" select def1 from tgrz_OrganizationType otype where  otype.pk_organizationtype in  ");
		sql.append(" (select pk_organizationtype from tgrz_organization where pk_organization = '"+pk_organization+"' and nvl(dr,0) = 0 )  ");
		sql.append(" and nvl(dr,0) = 0) and nvl(dr,0)= 0 and enablestate = '2' ");
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			result = (Map<String, Object>) getBaseDAO().executeQuery(sql.toString(), new MapProcessor());
//			pk_defdoc =  (String) getQuery().executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		return result;
	}
	
	private String getValue(String value1,String value2){
		UFDate date1 = new UFDate(value1);
		UFDate date2 = new UFDate(value2);
		return Integer.toString(date1.getDaysAfter(date2));
	}
}