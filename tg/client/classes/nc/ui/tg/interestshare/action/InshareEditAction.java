package nc.ui.tg.interestshare.action;

import java.awt.event.ActionEvent;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.vo.tgfn.internalinterest.AggInternalinterest;

public class InshareEditAction extends nc.ui.pubapp.uif2app.actions.EditAction{
	   public void check(AggIntshareHead[] bills) throws BusinessException{
		   IUAPQueryBS query=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			for(AggIntshareHead vo:bills){
				String pk=vo.getPrimaryKey();
					List<Integer> lists=(List<Integer>)query.executeQuery("select approvestatus from tgfn_intsharehead where def10='"+pk+"'",  new ColumnListProcessor());
					if(lists!=null){
						if(lists.contains(1)){
							throw new BusinessException("协同单据已经确认，上游不能反操作");
					}
				} 
			}
	   }
	   @Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
			Object obj=getModel().getSelectedData();
			if(obj==null){
				throw new BusinessException("未选中数据");
			}
			check(new AggIntshareHead[]{(AggIntshareHead)obj});
		super.doAction(e);
	}
	 @Override
	    protected boolean isActionEnable() {
	    	// TODO 自动生成的方法存根
		 boolean flag=true;
		 if(getModel()!=null){
	    	Object obj=getModel().getSelectedData();
	    	
	    	if(obj!=null){
	    		AggIntshareHead aggvo=(AggIntshareHead)obj;
	    		String def10=aggvo.getParentVO().getDef10();
	    		if(def10!=null&&def10!=""&&!("~".equals(def10))){
	    			flag=false;
	    		}
	    	}
		 }
	    	return super.isActionEnable()&&flag;
	    }
}
