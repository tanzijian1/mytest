package nc.ui.tg.mortgagelistdetailed.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IMortgagelistdetailed;
import nc.ui.pubapp.pub.task.ISingleBillService;
import nc.ui.pubapp.uif2app.actions.IDataOperationService;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceMortgageListDetailedMaintainProxy implements
		IDataOperationService, IQueryService,
		ISingleBillService<AggMortgageListDetailedVO> {
	@Override
	public IBill[] insert(IBill[] value) throws BusinessException {
		IMortgagelistdetailed operator = NCLocator.getInstance().lookup(
				IMortgagelistdetailed.class);
		AggMortgageListDetailedVO[] vos = operator
				.insert((AggMortgageListDetailedVO[]) value);
		return vos;
	}

	@Override
	public IBill[] update(IBill[] value) throws BusinessException {
		IMortgagelistdetailed operator = NCLocator.getInstance().lookup(
				IMortgagelistdetailed.class);
		AggMortgageListDetailedVO[] vos = operator
				.update((AggMortgageListDetailedVO[]) value);
		return vos;
	}

	@Override
	public IBill[] delete(IBill[] value) throws BusinessException {
		// 目前的删除并不是走这个方法，由于pubapp不支持从这个服务中执行删除操作
		// 单据的删除实际上使用的是：ISingleBillService<AggSingleBill>的operateBill
		IMortgagelistdetailed operator = NCLocator.getInstance().lookup(
				IMortgagelistdetailed.class);
		operator.delete((AggMortgageListDetailedVO[]) value);
		return value;
	}

	@Override
	public AggMortgageListDetailedVO operateBill(AggMortgageListDetailedVO bill)
			throws Exception {
		IMortgagelistdetailed operator = NCLocator.getInstance().lookup(
				IMortgagelistdetailed.class);
		operator.delete(new AggMortgageListDetailedVO[] { bill });
		return bill;
	}

	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IMortgagelistdetailed query = NCLocator.getInstance().lookup(
				IMortgagelistdetailed.class);
		return query.query(queryScheme);
	}

}
