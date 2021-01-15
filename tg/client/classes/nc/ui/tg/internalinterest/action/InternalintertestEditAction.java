package nc.ui.tg.internalinterest.action;

import java.awt.event.ActionEvent;
import java.util.List;

import com.borland.dx.dataset.ColumnList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.internalinterest.AggInternalinterest;

public class InternalintertestEditAction extends nc.ui.pubapp.uif2app.actions.EditAction{
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		Object obj=getModel().getSelectedData();
		if(obj==null){
			throw new BusinessException("未选中数据");
		}
		check(new AggInternalinterest[]{(AggInternalinterest)obj});
		super.doAction(e);
	}
	 public void check(AggInternalinterest[] vos) throws BusinessException{
			IUAPQueryBS query=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			for(AggInternalinterest vo:vos){
				String pk=vo.getPrimaryKey();
				String sql="select approvestatus from tgfn_internalinterest where def10='"+pk+"'";
				List<Integer> lists=(List<Integer>)query.executeQuery(sql,  new ColumnListProcessor());
					if(lists!=null){
						if(lists.contains(1)){
							throw new BusinessException("协同单据已经确认，上游不能反操作");
					}
				} 
			}
	  }
    @Override
    protected boolean isActionEnable() {
    	// TODO 自动生成的方法存根
    	boolean flag=true;
    	if(getModel()!=null){
    	Object obj=getModel().getSelectedData();
    	if(obj!=null){
    		AggInternalinterest aggvo=(AggInternalinterest)obj;
    		String def10=aggvo.getParentVO().getDef10();
    		if(def10!=null&&def10!=""&&!("~".equals(def10))){
    			flag=false;
    		}
    	}
    	}
    	return super.isActionEnable()&&flag;
    }
}
