package nc.ui.tg.projectdata.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.ui.pubapp.pub.task.ISingleBillService;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.pubapp.uif2app.actions.IDataOperationService;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.uif2.components.pagination.IPaginationQueryService;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.projectdata.AggProjectDataVO;
import nc.itf.tg.IProjectdataMaintain;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceProjectdataMaintainProxy implements IDataOperationService,
		IQueryService ,ISingleBillService<AggProjectDataVO>{
	@Override
	public IBill[] insert(IBill[] value) throws BusinessException {
		IProjectdataMaintain operator = NCLocator.getInstance().lookup(
				IProjectdataMaintain.class);
		AggProjectDataVO[] vos = operator.insert((AggProjectDataVO[]) value);
		return vos;
	}

	@Override
	public IBill[] update(IBill[] value) throws BusinessException {
		IProjectdataMaintain operator = NCLocator.getInstance().lookup(
				IProjectdataMaintain.class);
		AggProjectDataVO[] vos = operator.update((AggProjectDataVO[]) value);
		return vos;
	}

	@Override
	public IBill[] delete(IBill[] value) throws BusinessException {
		// 目前的删除并不是走这个方法，由于pubapp不支持从这个服务中执行删除操作
		// 单据的删除实际上使用的是：ISingleBillService<AggSingleBill>的operateBill
		IProjectdataMaintain operator = NCLocator.getInstance().lookup(
				IProjectdataMaintain.class);
		operator.delete((AggProjectDataVO[]) value);
		return value;
	}
	
	@Override
	public AggProjectDataVO operateBill(AggProjectDataVO bill) throws Exception {
		IProjectdataMaintain operator = NCLocator.getInstance().lookup(
				IProjectdataMaintain.class);
		operator.delete(new AggProjectDataVO[] { bill });
		return bill;
	}

	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IProjectdataMaintain query = NCLocator.getInstance().lookup(
				IProjectdataMaintain.class);
		return query.query(queryScheme);
	}

}
