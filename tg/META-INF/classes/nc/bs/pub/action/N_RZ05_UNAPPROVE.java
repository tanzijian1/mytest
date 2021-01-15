package nc.bs.pub.action;

import java.util.Collection;

import org.apache.poi.ss.formula.functions.T;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.fischeme.plugin.bpplugin.FischemePluginPoint;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.projectdata.ProjectDataFBVO;
import nc.vo.tg.projectdata.ProjectDataMVO;
import nc.vo.tg.projectdata.ProjectDataNVO;
import nc.vo.tg.projectdata.ProjectDataVVO;
import nc.itf.tg.IFischemeMaintain;

public class N_RZ05_UNAPPROVE extends AbstractPfAction<AggFIScemeHVO> {

	@Override
	protected CompareAroundProcesser<AggFIScemeHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFIScemeHVO> processor = new CompareAroundProcesser<AggFIScemeHVO>(
				FischemePluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggFIScemeHVO[] processBP(Object userObj,
			AggFIScemeHVO[] clientFullVOs, AggFIScemeHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggFIScemeHVO[] bills = null;
		try {
			IFischemeMaintain operator = NCLocator.getInstance()
					.lookup(IFischemeMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			for(AggFIScemeHVO aggvo:bills){
				FIScemeHVO hvo=(FIScemeHVO) aggvo.getParent();
				String pk_project=hvo.getPk_project();
				String billno = hvo.getBillno();
				//弃审时将项目资料的表体清掉
				if(pk_project!=null&&pk_project.trim().length()>0){
					Collection<ProjectDataVVO> coll = (Collection<ProjectDataVVO>) getBodyBillVO(ProjectDataVVO.class,pk_project,billno);
					Collection<ProjectDataFBVO> fboll = (Collection<ProjectDataFBVO>) getBodyBillVO(ProjectDataFBVO.class,pk_project,billno);
					Collection<ProjectDataNVO> ncoll = (Collection<ProjectDataNVO>) getBodyBillVO(ProjectDataNVO.class,pk_project,billno);
					Collection<ProjectDataMVO> mcoll = (Collection<ProjectDataMVO>) getBodyBillVO(ProjectDataMVO.class,pk_project,billno);
					if(coll!=null&&coll.size()>0){
						for(ProjectDataVVO vo:coll){
							getBaseDao().deleteByPK(ProjectDataVVO.class, vo.getPk_projectdata_v());
							getBaseDao().deleteByClause(ProjectDataVVO.class, "pk_projectdata_v = '"+vo.getPk_projectdata_v()+"' and vbdef10 = '"+hvo.getPk_scheme()+"' and nvl(dr,0) = 0 ");
						}
					}
					if(ncoll!=null&&ncoll.size()>0){
						for(ProjectDataNVO vo:ncoll){
							getBaseDao().deleteByPK(ProjectDataNVO.class, vo.getPk_projectdata_n());
							getBaseDao().deleteByClause(ProjectDataNVO.class, "pk_projectdata_n = '"+vo.getPk_projectdata_n()+"' and def10 = '"+hvo.getPk_scheme()+"' and nvl(dr,0) = 0 ");
						}
					}
					if(mcoll!=null&&mcoll.size()>0){
						for(ProjectDataMVO vo:mcoll){
							getBaseDao().deleteByPK(ProjectDataMVO.class, vo.getPk_projectdata_m());
							getBaseDao().deleteByClause(ProjectDataMVO.class, "pk_projectdata_m = '"+vo.getPk_projectdata_m()+"' and def10 = '"+hvo.getPk_scheme()+"' and nvl(dr,0) = 0 ");
						}
					}
					if(fboll!=null&&fboll.size()>0){
						for(ProjectDataFBVO vo:fboll){
							getBaseDao().deleteByPK(ProjectDataFBVO.class, vo.getPk_projectdata_fb());
							getBaseDao().deleteByClause(ProjectDataFBVO.class, "pk_projectdata_fb = '"+vo.getPk_projectdata_fb()+"' and def10 = '"+hvo.getPk_scheme()+"' and nvl(dr,0) = 0 ");
						}
					}
					hvo.setStatus(VOStatus.UPDATED);
					hvo.setVdef2(null);
					getBaseDao().updateVO(hvo);
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
	
	public Collection<?> getBodyBillVO(Class<? extends ISuperVO> clazz,String pk_project, String billno) throws DAOException{
		Collection<?> coll = null;
//		coll=(Collection<?>)getBaseDao().retrieveByClause(clazz, " pk_projectdata='"+pk_project+"' ");
		if(clazz.isAssignableFrom(ProjectDataVVO.class)){
			coll=(Collection<?>)getBaseDao().retrieveByClause(clazz, " pk_projectdata='"+pk_project+"' and vbdef10 = '"+billno+"'");
		}else{
			coll=(Collection<?>)getBaseDao().retrieveByClause(clazz, " pk_projectdata='"+pk_project+"' and def9 = '"+billno+"'");
		}
			
		return coll;
	}

	BaseDAO dao = null;
	public BaseDAO getBaseDao(){
		if(dao==null){
			dao =  new BaseDAO();
		}
		return dao;
	}
}
