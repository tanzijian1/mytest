package nc.ui.baseapp.itfformulacfg.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.baseapp.IItfformulacfgMaintain;
import nc.ui.pubapp.pub.task.ISingleBillService;
import nc.ui.pubapp.uif2app.actions.IDataOperationService;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.baseapp.itfformulacfg.AggFormulaCfgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * @Description:
 * @version with NC V6.5
 */
public class AceItfformulacfgMaintainProxy implements IDataOperationService, IQueryService, ISingleBillService<AggFormulaCfgVO> {
	@Override
	public IBill[] insert(IBill[] value) throws BusinessException {
		IItfformulacfgMaintain operator = NCLocator.getInstance().lookup(IItfformulacfgMaintain.class);
		AggFormulaCfgVO[] vos = operator.insert((AggFormulaCfgVO[]) value);
		return vos;
	}

	@Override
	public IBill[] update(IBill[] value) throws BusinessException {
		IItfformulacfgMaintain operator = NCLocator.getInstance().lookup(IItfformulacfgMaintain.class);
		AggFormulaCfgVO[] vos = operator.update((AggFormulaCfgVO[]) value);
		return vos;
	}

	@Override
	public IBill[] delete(IBill[] value) throws BusinessException {
		// Ŀǰ��ɾ�����������������������pubapp��֧�ִ����������ִ��ɾ������
		// ���ݵ�ɾ��ʵ����ʹ�õ��ǣ�ISingleBillService<AggSingleBill>��operateBill
		IItfformulacfgMaintain operator = NCLocator.getInstance().lookup(IItfformulacfgMaintain.class);
		operator.delete((AggFormulaCfgVO[]) value);
		return value;
	}

	@Override
	public AggFormulaCfgVO operateBill(AggFormulaCfgVO bill) throws Exception {
		IItfformulacfgMaintain operator = NCLocator.getInstance().lookup(IItfformulacfgMaintain.class);
		operator.delete(new AggFormulaCfgVO[] { bill });
		return bill;
	}

	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme) throws Exception {
		IItfformulacfgMaintain query = NCLocator.getInstance().lookup(IItfformulacfgMaintain.class);
		return query.query(queryScheme);
	}

}
