package nc.bs.pub.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.fischeme.plugin.bpplugin.FischemePluginPoint;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.CapmarketBVO;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.fischeme.FISchemeBVO;
import nc.vo.tg.fischeme.NFISchemeBVO;
import nc.vo.tg.projectdata.ProjectDataCVO;
import nc.vo.tg.projectdata.ProjectDataFBVO;
import nc.vo.tg.projectdata.ProjectDataMVO;
import nc.vo.tg.projectdata.ProjectDataNVO;
import nc.vo.tg.projectdata.ProjectDataVVO;
import nc.itf.tg.IFischemeMaintain;
import nc.itf.tg.bd.pub.IPlanAndRealityConst;
import nc.jdbc.framework.processor.MapProcessor;

public class N_RZ05_APPROVE extends AbstractPfAction<AggFIScemeHVO> {
	BaseDAO baseDAO = null;
	public N_RZ05_APPROVE() {
		super();
	}
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	
	@Override
	protected CompareAroundProcesser<AggFIScemeHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFIScemeHVO> processor = new CompareAroundProcesser<AggFIScemeHVO>(
				FischemePluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}
	
	@Override
	protected AggFIScemeHVO[] processBP(Object userObj,
			AggFIScemeHVO[] clientFullVOs, AggFIScemeHVO[] originBills) {
		AggFIScemeHVO[] bills = null;
		IFischemeMaintain operator = NCLocator.getInstance().lookup(
				IFischemeMaintain.class);
		String fischemeTimeTypePk = "";
		try {
			bills = operator.approve(clientFullVOs, originBills);
			//融资方案表体回写项目资料对应的表体
			for(AggFIScemeHVO aggvo:bills){
				FIScemeHVO hvo=(FIScemeHVO) aggvo.getParent();
				//校验项目资料表体是否存在相同的单据编号
				FISchemeBVO[] bvos=(FISchemeBVO[])aggvo.getAllChildrenVO();
				NFISchemeBVO[] nbvos= (NFISchemeBVO[]) aggvo.getChildren(NFISchemeBVO.class);
				CapmarketBVO[] cbvos= (CapmarketBVO[]) aggvo.getChildren(CapmarketBVO.class);
				//根据融资方案的审核时间和融资方案标准表头审核时间获取表体第一行的数据
				String tableName = "";
				//根据表名和pk获取融资方案推进标准表体
				hvo.setStatus(VOStatus.UPDATED);
				if(bvos!=null&&bvos.length>0){
					tableName = "tg_fischemepushstandard_b";
					getBaseDAO().deleteByClause(ProjectDataFBVO.class, " def9 = '"+hvo.getBillno()+"' ");
					if(hvo.getVdef2()==null||"~".equals(hvo.getVdef2())||"".equals(hvo.getVdef2())){
						hvo.setVdef2(hvo.getApprovedate().toString().substring(0,11));
						bvos = handleFischemeBodyFirstToFb(bvos,hvo,tableName,fischemeTimeTypePk);
						getBaseDAO().updateVO(hvo);
						getBaseDAO().updateVOArray(bvos);
					}
				}if(nbvos!=null&&nbvos.length>0){
					tableName = "tg_fischemepushstandard_n";
					getBaseDAO().deleteByClause(ProjectDataNVO.class, " def9 = '"+hvo.getBillno()+"' ");
					if(hvo.getVdef2()==null||"~".equals(hvo.getVdef2())||"".equals(hvo.getVdef2())){
						hvo.setVdef2(hvo.getApprovedate().toString().substring(0,11));
						nbvos = handleFischemeBodyFirstToN(nbvos,hvo,tableName,fischemeTimeTypePk);
						getBaseDAO().updateVO(hvo);
						getBaseDAO().updateVOArray(nbvos);
					}
				}if(cbvos!=null&&cbvos.length>0){
					tableName = "tg_fischemepushstandard_c";
					getBaseDAO().deleteByClause(ProjectDataMVO.class, " def9 = '"+hvo.getBillno()+"' ");
					if(hvo.getVdef2()==null||"~".equals(hvo.getVdef2())||"".equals(hvo.getVdef2())){
						hvo.setVdef2(hvo.getApprovedate().toString().substring(0,11));
						cbvos = handleFischemeBodyFirstToC(cbvos,hvo,tableName,fischemeTimeTypePk);
						getBaseDAO().updateVO(hvo);
						getBaseDAO().updateVOArray(cbvos);
					}
				}
				aggvo.setChildren(FISchemeBVO.class, bvos);
				aggvo.setChildren(NFISchemeBVO.class, nbvos);
				aggvo.setChildren(CapmarketBVO.class, cbvos);
				String pk_project=hvo.getPk_project();
				if(pk_project!=null&&pk_project.trim().length()>0){
//					Collection<ProjectDataVVO> coll=(Collection<ProjectDataVVO>)dao.retrieveByClause(ProjectDataVVO.class, " pk_projectdata='"+pk_project+"'");
//					if(coll!=null&&coll.size()>0){
//						for(ProjectDataVVO vo:coll){
//							dao.deleteByPK(ProjectDataVVO.class, vo.getPk_projectdata_v());
//						}
//					}
					getBaseDAO().deleteByClause(ProjectDataVVO.class, " vbdef10 = '"+hvo.getBillno()+"' ");
					//融资方案
					ProjectDataVVO pdvo=new ProjectDataVVO();
					if(bvos.length>0||cbvos.length>0||nbvos.length>0){
							pdvo.setPk_projectdata(pk_project);
							pdvo.setDr(0);
							pdvo.setStatus(VOStatus.NEW);
							pdvo.setIsmain(hvo.bmain);
							pdvo.setFintype(hvo.getPk_organizationtype());
							pdvo.setOrganization(hvo.getPk_organization());
							pdvo.setFinmny(hvo.getNmy());
//					pdvo.setFindetailed(findetailed);
							pdvo.setFinrate(hvo.getRate());
							pdvo.setZhrate(hvo.getZhrate());
							pdvo.setVbdef10(hvo.getBillno());
//					pdvo.setProcess(process);
//							pdvo.setBra_nchdate(bvos[i].getBracnchmeet());
//							pdvo.setProvi_ncedate(bvos[i].getProvincemeet());
//							pdvo.setHeadquartersdate(bvos[i].getTotalbankmeet());
//							pdvo.setReplydate(bvos[i].getReply());
//							pdvo.setCostsigndate(bvos[i].getContrsingntime());
//							pdvo.setIsdrawescheme(bvos[i].getIsdrawescheme());
//							pdvo.setRemarkschedule(bvos[i].getNote());
							getBaseDAO().insertVO(pdvo);
					}
					else{
						pdvo.setPk_projectdata(pk_project);
						pdvo.setStatus(VOStatus.NEW);
						pdvo.setIsmain(hvo.bmain);
						pdvo.setFintype(hvo.getPk_organizationtype());
						pdvo.setOrganization(hvo.getPk_organization());
						pdvo.setFinmny(hvo.getNmy());
//				pdvo.setFindetailed(findetailed);
						pdvo.setFinrate(hvo.getRate());
						pdvo.setZhrate(hvo.getZhrate());
//				pdvo.setProcess(process);
						pdvo.setBra_nchdate(null);
						pdvo.setProvi_ncedate(null);
						pdvo.setHeadquartersdate(null);
						pdvo.setReplydate(null);
						pdvo.setDr(0);
						pdvo.setCostsigndate(null);
						pdvo.setVbdef10(hvo.getBillno());
						getBaseDAO().insertVO(pdvo);
					}
//					Collection<ProjectDataNVO> ncoll=(Collection<ProjectDataNVO>)dao.retrieveByClause(ProjectDataNVO.class, " pk_projectdata='"+pk_project+"'");
//					if(ncoll!=null&&ncoll.size()>0){
//						for(ProjectDataNVO vo:ncoll){
//							dao.deleteByPK(ProjectDataNVO.class, vo.getPk_projectdata_n());
//						}
//					}
					//方案进度(银行)
					List<ProjectDataFBVO> listFbvo = new LinkedList<ProjectDataFBVO>();
					if(bvos.length>0){
						for(int i=0;i<bvos.length;i++){
							ProjectDataFBVO fbvo = new ProjectDataFBVO();
							fbvo.setStatus(VOStatus.NEW);
							fbvo.setPk_projectdata(pk_project);
							fbvo.setAttributeValue("dr", 0);
							fbvo.setDef1(bvos[i].getVbdef1());
							fbvo.setDef2(bvos[i].getVbdef2());
							fbvo.setDef3(bvos[i].getVbdef3());
							fbvo.setDef4(bvos[i].getVbdef4());
							fbvo.setDef5(bvos[i].getVbdef5());
							fbvo.setDef6(bvos[i].getVbdef6());
							fbvo.setDef7(bvos[i].getVbdef7());
							fbvo.setDef8(bvos[i].getVbdef8());
							fbvo.setDef9(hvo.getBillno());
							fbvo.setDef11(bvos[i].getNote());
							fbvo.setDef13(bvos[i].getDef13());
							if(i==0){
								fbvo.setDef10(hvo.getBillno());
							}
							listFbvo.add(fbvo);
							getBaseDAO().insertVO(fbvo);
						}
					}
					//方案进度(非银)
					List<ProjectDataNVO> listNvo = new LinkedList<ProjectDataNVO>();
					if(nbvos.length>0){
						for(int i=0;i<nbvos.length;i++){
							ProjectDataNVO nvo = new ProjectDataNVO();
							nvo.setStatus(VOStatus.NEW);
							nvo.setPk_projectdata(pk_project);
							nvo.setAttributeValue("dr", 0);
							nvo.setDef1(nbvos[i].getDef1());
							nvo.setDef2(nbvos[i].getDef2());
							nvo.setDef3(nbvos[i].getDef3());
							nvo.setDef4(nbvos[i].getDef4());
							nvo.setDef5(nbvos[i].getDef5());
							nvo.setDef6(nbvos[i].getDef6());
							nvo.setDef7(nbvos[i].getDef7());
							nvo.setDef8(nbvos[i].getDef8());
							nvo.setDef9(hvo.getBillno());
							nvo.setDef13(nbvos[i].getDef13());
							if(i==0){
								nvo.setDef10(hvo.getBillno());
							}
							listNvo.add(nvo);
							getBaseDAO().insertVO(nvo);
						}
					}
//					Collection<ProjectDataMVO> mcoll=(Collection<ProjectDataMVO>)dao.retrieveByClause(ProjectDataMVO.class, " pk_projectdata='"+pk_project+"'");
//					if(mcoll!=null&&mcoll.size()>0){
//						for(ProjectDataMVO vo:mcoll){
//							dao.deleteByPK(ProjectDataMVO.class, vo.getPk_projectdata_m());
//						}
//					}
					//方案进度(资本市场)
					List<ProjectDataMVO> listMvo = new LinkedList<ProjectDataMVO>();
					if(cbvos.length>0){
						for(int i=0;i<cbvos.length;i++){
							ProjectDataMVO mvo = new ProjectDataMVO();
							mvo.setPk_projectdata(pk_project);
							mvo.setStatus(VOStatus.NEW);
							mvo.setAttributeValue("dr", 0);
							mvo.setDef1(cbvos[i].getDef1());
							mvo.setDef2(cbvos[i].getDef2());
							mvo.setDef3(cbvos[i].getDef3());
							mvo.setDef4(cbvos[i].getDef4());
							mvo.setDef5(cbvos[i].getDef5());
							mvo.setDef6(cbvos[i].getDef6());
							mvo.setDef7(cbvos[i].getDef7());
							mvo.setDef8(cbvos[i].getDef8());
							mvo.setDef9(hvo.getBillno());
							mvo.setDef13(cbvos[i].getDef13());
							if(i==0){
								mvo.setDef10(hvo.getBillno());
							}
							listMvo.add(mvo);
							getBaseDAO().insertVO(mvo);
						}
					}
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
	
	/**
	 * 资本市场
	 * @param cbvos
	 * @param hvo
	 * @param tableName
	 * @param fischemeTimeTypePk
	 * @return
	 */
	private CapmarketBVO[] handleFischemeBodyFirstToC(CapmarketBVO[] cbvos,
			FIScemeHVO hvo, String tableName, String fischemeTimeTypePk) {
		if(getCode(hvo.getPk_organization())!=null){
			Map<String, Object> bMap = getRow(tableName,(String) getCode(hvo.getPk_organization()).get("pk_defdoc"));
			if(getHeadVoForPk((String)bMap.get("pk_fispushstandard"))!=null){
				fischemeTimeTypePk = (String) getHeadVoForPk((String)bMap.get("pk_fispushstandard")).get("def2");
			}
			if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"002".equals(getDefdocCode(fischemeTimeTypePk))){
				cbvos[0].setStatus(VOStatus.UPDATED);
				cbvos[1].setStatus(VOStatus.UPDATED);
				cbvos[2].setStatus(VOStatus.UPDATED);
				cbvos[0].setDef1(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")).intValue()).toString().substring(0,11));
				cbvos[0].setDef2(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")).intValue()).toString().substring(0,11));
				cbvos[0].setDef3(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")).intValue()).toString().substring(0,11));
				cbvos[0].setDef4(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")).intValue()).toString().substring(0,11));
				cbvos[0].setDef5(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")).intValue()).toString().substring(0,11));
				cbvos[0].setDef6(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")).intValue()).toString().substring(0,11));
				cbvos[0].setDef7(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")).intValue()).toString().substring(0,11));
				cbvos[0].setDef13(IPlanAndRealityConst.PlanTime);
				cbvos[1].setDef13(IPlanAndRealityConst.RealityTime);
				cbvos[2].setDef13(IPlanAndRealityConst.Days);
				if(StringUtils.isNotBlank(cbvos[0].getDef1())&&StringUtils.isNotBlank(cbvos[1].getDef1())){
					cbvos[2].setDef1(getValue(cbvos[0].getDef1(),cbvos[1].getDef1()));
				}
				if(StringUtils.isNotBlank(cbvos[0].getDef2())&&StringUtils.isNotBlank(cbvos[1].getDef2())){
					cbvos[2].setDef2(getValue(cbvos[0].getDef2(),cbvos[1].getDef2()));
				}
				if(StringUtils.isNotBlank(cbvos[0].getDef3())&&StringUtils.isNotBlank(cbvos[1].getDef3())){
					cbvos[2].setDef3(getValue(cbvos[0].getDef3(),cbvos[1].getDef3()));
				}
				if(StringUtils.isNotBlank(cbvos[0].getDef4())&&StringUtils.isNotBlank(cbvos[1].getDef4())){
					cbvos[2].setDef4(getValue(cbvos[0].getDef4(),cbvos[1].getDef4()));
				}
				if(StringUtils.isNotBlank(cbvos[0].getDef5())&&StringUtils.isNotBlank(cbvos[1].getDef5())){
					cbvos[2].setDef5(getValue(cbvos[0].getDef5(),cbvos[1].getDef5()));
				}
				if(StringUtils.isNotBlank(cbvos[0].getDef6())&&StringUtils.isNotBlank(cbvos[1].getDef6())){
					cbvos[2].setDef6(getValue(cbvos[0].getDef6(),cbvos[1].getDef6()));
				}
				if(StringUtils.isNotBlank(cbvos[0].getDef7())&&StringUtils.isNotBlank(cbvos[1].getDef7())){
					cbvos[2].setDef7(getValue(cbvos[0].getDef7(),cbvos[1].getDef7()));
				}
			}
		}
		return cbvos;
	}

	/**
	 * 非银
	 * @param nbvos
	 * @param hvo
	 * @param tableName
	 * @param fischemeTimeTypePk
	 * @return
	 */
	private NFISchemeBVO[] handleFischemeBodyFirstToN(NFISchemeBVO[] nbvos,
			FIScemeHVO hvo, String tableName, String fischemeTimeTypePk) {
		if(getCode(hvo.getPk_organization())!=null){
			Map<String, Object> bMap = getRow(tableName,(String) getCode(hvo.getPk_organization()).get("pk_defdoc"));
			if(getHeadVoForPk((String)bMap.get("pk_fispushstandard"))!=null){
				fischemeTimeTypePk = (String) getHeadVoForPk((String)bMap.get("pk_fispushstandard")).get("def2");
			}
			if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"002".equals(getDefdocCode(fischemeTimeTypePk))){
				nbvos[0].setStatus(VOStatus.UPDATED);
				nbvos[1].setStatus(VOStatus.UPDATED);
				nbvos[2].setStatus(VOStatus.UPDATED);
				nbvos[0].setDef1(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def1")==null?0:Integer.valueOf((String) bMap.get("def1")).intValue()).toString().substring(0,11));
				nbvos[0].setDef2(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def2")==null?0:Integer.valueOf((String) bMap.get("def2")).intValue()).toString().substring(0,11));
				nbvos[0].setDef3(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def3")==null?0:Integer.valueOf((String) bMap.get("def3")).intValue()).toString().substring(0,11));
				nbvos[0].setDef4(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def4")==null?0:Integer.valueOf((String) bMap.get("def4")).intValue()).toString().substring(0,11));
				nbvos[0].setDef5(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def5")==null?0:Integer.valueOf((String) bMap.get("def5")).intValue()).toString().substring(0,11));
				nbvos[0].setDef6(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def6")==null?0:Integer.valueOf((String) bMap.get("def6")).intValue()).toString().substring(0,11));
				nbvos[0].setDef7(hvo.getVdef2()==null?null:new UFDate(hvo.getVdef2()).getDateAfter(bMap.get("def7")==null?0:Integer.valueOf((String) bMap.get("def7")).intValue()).toString().substring(0,11));
				nbvos[0].setDef13(IPlanAndRealityConst.PlanTime);
				nbvos[1].setDef13(IPlanAndRealityConst.RealityTime);
				nbvos[2].setDef13(IPlanAndRealityConst.Days);
				if(StringUtils.isNotBlank(nbvos[0].getDef1())&&StringUtils.isNotBlank(nbvos[1].getDef1())){
					nbvos[2].setDef1(getValue(nbvos[0].getDef1(),nbvos[1].getDef1()));
				}
				if(StringUtils.isNotBlank(nbvos[0].getDef2())&&StringUtils.isNotBlank(nbvos[1].getDef2())){
					nbvos[2].setDef2(getValue(nbvos[0].getDef2(),nbvos[1].getDef2()));
				}
				if(StringUtils.isNotBlank(nbvos[0].getDef3())&&StringUtils.isNotBlank(nbvos[1].getDef3())){
					nbvos[2].setDef3(getValue(nbvos[0].getDef3(),nbvos[1].getDef3()));
				}
				if(StringUtils.isNotBlank(nbvos[0].getDef4())&&StringUtils.isNotBlank(nbvos[1].getDef4())){
					nbvos[2].setDef4(getValue(nbvos[0].getDef4(),nbvos[1].getDef4()));
				}
				if(StringUtils.isNotBlank(nbvos[0].getDef5())&&StringUtils.isNotBlank(nbvos[1].getDef5())){
					nbvos[2].setDef5(getValue(nbvos[0].getDef5(),nbvos[1].getDef5()));
				}
				if(StringUtils.isNotBlank(nbvos[0].getDef6())&&StringUtils.isNotBlank(nbvos[1].getDef6())){
					nbvos[2].setDef6(getValue(nbvos[0].getDef6(),nbvos[1].getDef6()));
				}
				if(StringUtils.isNotBlank(nbvos[0].getDef7())&&StringUtils.isNotBlank(nbvos[1].getDef7())){
					nbvos[2].setDef7(getValue(nbvos[0].getDef7(),nbvos[1].getDef7()));
				}
			}
		}
		return nbvos;
	}

	/**
	 * 银行
	 * @param bvos
	 * @param hvo
	 * @param tableName
	 * @param fischemeTimeTypePk
	 * @return
	 */
	private FISchemeBVO[] handleFischemeBodyFirstToFb(FISchemeBVO[] bvos, FIScemeHVO hvo,String tableName,String fischemeTimeTypePk) {
		
		if(getCode(hvo.getPk_organization())!=null){
			Map<String, Object> bMap = getRow(tableName,(String) getCode(hvo.getPk_organization()).get("pk_defdoc"));
			if(bMap!=null){
				if(getHeadVoForPk((String)bMap.get("pk_fispushstandard"))!=null){
					fischemeTimeTypePk = (String) getHeadVoForPk((String)bMap.get("pk_fispushstandard")).get("def2");
				}
				if(StringUtils.isNotBlank(getDefdocCode(fischemeTimeTypePk))&&"002".equals(getDefdocCode(fischemeTimeTypePk))){
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
		}
		return bvos;
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
