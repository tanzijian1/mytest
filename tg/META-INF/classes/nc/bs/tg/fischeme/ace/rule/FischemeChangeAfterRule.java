package nc.bs.tg.fischeme.ace.rule;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.tmpub.version.VersionInsertBP;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischemeversion.AggFISchemeversionHVO;
public class FischemeChangeAfterRule implements IRule{
	private	BaseDAO baseDao=null; 

	@Override
	public void process(Object[] vos) {
		// TODO 自动生成的方法存根
		for (int i = 0; i < vos.length; i++) {
//		      ((nc.vo.tg.fischeme.AggFIScemeHVO)vos[i]).getParentVO().setContstatus((String)ContStatusEnum.EXECUTING.value());
		      ((nc.vo.tg.fischeme.AggFIScemeHVO)vos[i]).getParentVO().setApprovedate(null);
		      ((nc.vo.tg.fischeme.AggFIScemeHVO)vos[i]).getParentVO().setApprover(null);
		      ((nc.vo.tg.fischeme.AggFIScemeHVO)vos[i]).getParentVO().setApprovenote(null);
		      ((nc.vo.tg.fischeme.AggFIScemeHVO)vos[i]).getParentVO().setApprovestatus(Integer.valueOf(BillStatusEnum.FREE.toIntValue()));
		      ((nc.vo.tg.fischeme.AggFIScemeHVO)vos[i]).getParentVO().setCreator(AppContext.getInstance().getPkUser());
		      ((nc.vo.tg.fischeme.AggFIScemeHVO)vos[i]).getParentVO().setCreationtime(AppContext.getInstance().getServerTime());
		      if(("Y").equals(((nc.vo.tg.fischeme.AggFIScemeHVO)vos[i]).getParentVO().getAttributeValue("vdef10"))){
		      String  Pk_scheme=   ((nc.vo.tg.fischeme.AggFIScemeHVO)vos[i]).getParentVO().getPk_scheme();
		      AggFIScemeHVO aggVerVO  = new AggFIScemeHVO ();
				
					try {
						AggFIScemeHVO[]	HistoryVo=getHistory(Pk_scheme);
						VersionInsertBP<AggFIScemeHVO, AggFISchemeversionHVO> verBp = new VersionInsertBP<AggFIScemeHVO, AggFISchemeversionHVO>();
						AggFIScemeHVO[]	gVerVO = verBp.insert(HistoryVo, null);
					
					} catch (DAOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
		      }}}
					
	
		
	private BaseDAO getBaseDao(){
		if(baseDao==null)
			baseDao = new BaseDAO();
		return baseDao;
	}
	private AggFIScemeHVO[] getHistory(String pk_scheme) throws DAOException{
		BillQuery<AggFIScemeHVO> billquery = new BillQuery(AggFIScemeHVO.class);
		AggFIScemeHVO[] aggvo=billquery.query(new String[] {pk_scheme});
		return aggvo;
		
		
	}
}
