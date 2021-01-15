package nc.ui.tg.pub.modeldata;

import java.util.Collection;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.pubapp.pub.smart.IBillQueryService;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.meta.IBDObjectAdapterFactory;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class TGModelDataRefresher {
	private AbstractUIAppModel model;

	public TGModelDataRefresher(AbstractUIAppModel model) {
		super();
		this.model = model;
	}

	public void refreshData() throws Exception {
		if (this.model instanceof BillManageModel) {
			this.refreshBillManageData((BillManageModel) this.model);
		} else if (this.model instanceof BatchBillTableModel) {
			this.refreshBillTableData((BatchBillTableModel) this.model);
		}
	}

	private IBDObjectAdapterFactory getBusinessObjectAdapterFactory() {
		if (this.model instanceof BillManageModel) {
			return ((BillManageModel) this.model)
					.getBusinessObjectAdapterFactory();
		} else if (this.model instanceof BatchBillTableModel) {
			return ((BatchBillTableModel) this.model)
					.getBusinessObjectAdapterFactory();
		}
		return null;
	}

	private IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}

	@SuppressWarnings("unchecked")
	private void refreshBillManageData(BillManageModel pModel) throws Exception {
		Object[] data = pModel.getSelectedOperaDatas();
		if (data == null || data.length == 0) {
			return;
		}
		String[] pks = new String[data.length];
		int i = 0;
		Class<AbstractBill> clazz = null;
		for (Object d : data) {
			IBDObject target = this.getBusinessObjectAdapterFactory()
					.createBDObject(d);
			pks[i++] = (String) target.getId();
			clazz = (Class<AbstractBill>) d.getClass();
		}
		if (clazz == null) {
			return;
		}

		// 注意：下面的写法只是暂时的写法，为了暂时完成CQ问题，这段代码以后肯定要修改的，在等待批查的接口
		// 否则会影响效率，产生很多连接数
		IBillQueryService billQuery = NCLocator.getInstance().lookup(
				IBillQueryService.class);
		AbstractBill[] bills = billQuery.queryAbstractBillsByPks(clazz, pks);

		if (bills == null || bills.length < 1) {
			return;
		}
		pModel.update(bills);
	}

	private void refreshBillTableData(BatchBillTableModel pModel)
			throws Exception {
		List<?> data = pModel.getRows();
		if (data == null || data.size() == 0) {
			return;
		}
		String[] pks = new String[data.size()];
		int i = 0;
		Class<?> clazz = null;
		for (Object d : data) {
			IBDObject target = this.getBusinessObjectAdapterFactory()
					.createBDObject(d);
			pks[i++] = (String) target.getId();
			clazz = d.getClass();
		}
		Collection<?> coll = this.getMDQueryService().queryBillOfVOByPKs(clazz,
				pks, false);
		pModel.initModel(coll.toArray());
	}
}
