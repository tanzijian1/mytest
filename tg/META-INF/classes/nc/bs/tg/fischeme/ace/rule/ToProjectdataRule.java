package nc.bs.tg.fischeme.ace.rule;

import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.vo.pub.VOStatus;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.fischeme.FISchemeBVO;
import nc.vo.tg.projectdata.ProjectDataVVO;

public class ToProjectdataRule implements IRule<AggFIScemeHVO>{

	@Override
	public void process(AggFIScemeHVO[] vos) {
		// TODO 自动生成的方法存根
		for(AggFIScemeHVO aggvo:vos){
			FIScemeHVO hvo=(FIScemeHVO) aggvo.getParent();
			FISchemeBVO[] bvos=(FISchemeBVO[])aggvo.getAllChildrenVO();
			String pk_project=hvo.getPk_project();
			BaseDAO dao=new BaseDAO();
			if(pk_project!=null&&pk_project.trim().length()>0){
			Collection<ProjectDataVVO> coll=null;
			try {
				coll = (Collection<ProjectDataVVO>)dao.retrieveByClause(ProjectDataVVO.class, " pk_projectdata='"+pk_project+"'");
			
			if(coll!=null&&coll.size()>0){
				for(ProjectDataVVO vo:coll){
					dao.deleteByPK(ProjectDataVVO.class, vo.getPk_projectdata_v());
				}
			}
			
	if(bvos.length>0){
		for(int i=0;i<bvos.length;i++){
			ProjectDataVVO pdvo=new ProjectDataVVO();
			pdvo.setPk_projectdata(pk_project);;
			pdvo.setIsmain(hvo.bmain);
			pdvo.setStatus(VOStatus.NEW);
			pdvo.setVbdef10(hvo.getBillno());
			pdvo.setFintype(hvo.getPk_organizationtype());
			pdvo.setOrganization(hvo.getPk_organization());
			pdvo.setFinmny(hvo.getNmy());
//			pdvo.setFindetailed(findetailed);
			pdvo.setFinrate(hvo.getRate());
//			pdvo.setProcess(process);
//			pdvo.setBra_nchdate(bvos[i].getBracnchmeet());
//			pdvo.setProvi_ncedate(bvos[i].getProvincemeet());
//			pdvo.setHeadquartersdate(bvos[i].getTotalbankmeet());
//			pdvo.setReplydate(bvos[i].getReply());
//			pdvo.setCostsigndate(bvos[i].getContrsingntime()==null?null:bvos[i].getContrsingntime());//.getDate()
			pdvo.setDr(0);
			dao.insertVO(pdvo);
		}
	}else{
		ProjectDataVVO pdvo=new ProjectDataVVO();
		pdvo.setPk_projectdata(pk_project);;
		pdvo.setIsmain(hvo.bmain);
		pdvo.setStatus(VOStatus.NEW);
		pdvo.setFintype(hvo.getPk_organizationtype());
		pdvo.setOrganization(hvo.getPk_organization());
		pdvo.setFinmny(hvo.getNmy());
		pdvo.setVbdef10(hvo.getBillno());
		pdvo.setDr(0);
//		pdvo.setFindetailed(findetailed);
		pdvo.setFinrate(hvo.getRate());
//		pdvo.setProcess(process);
		dao.insertVO(pdvo);
	}
  } catch (DAOException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}
			}
	}
	}
}
