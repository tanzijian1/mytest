package nc.ui.tg.pub.action.pub.model;

import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.querytemplate.querytree.QueryScheme;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class GdModelDataManager  extends
nc.ui.pubapp.uif2app.query2.model.ModelDataManager {
	private ShowUpableBillForm billForm;

public ShowUpableBillForm getBillForm() {
		return billForm;
	}
	public void setBillForm(ShowUpableBillForm billForm) {
		this.billForm = billForm;
	}
@Override
public void refresh() {
	// TODO 自动生成的方法存根
	if (getModel() instanceof BillManageModel) {
    Object obj=((BillManageModel)getModel()).getSelectedData();
    if(obj!=null){
    if(getBillForm()!=null){
    	if(getBillForm().isShowing()){
    		try {
				refreshcard(obj);//刷新卡片界面
				return;
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				 ExceptionUtils.wrappException(e);
			}
    		}
    	}
      }
	}
	super.refresh();

}
protected void  refreshcard(Object obj) throws Exception {
	String pkname=null;
	AggregatedValueObject billvo=(AggregatedValueObject)obj;
	pkname=((SuperVO)billvo.getParentVO()).getParentPKFieldName();
	String pk=((SuperVO)billvo.getParentVO()).getPrimaryKey();
//	QueryScheme scheme = (QueryScheme)getQueryScheme();
//	scheme.put(scheme.KEY_SQL_WHERE, SQLUtil.buildSqlForIn(pkname, pks));
//	Object[] nobjs=getQryService().queryByQueryScheme(getQueryScheme());
	NCObject nobj=getMDQueryService().queryBillOfNCObjectByPK(obj.getClass(), pk);
	((BillManageModel)getModel()).update(nobj.getContainmentObject());
   }
private IMDPersistenceQueryService getMDQueryService() {
	return MDPersistenceService.lookupPersistenceQueryService();
}
}
