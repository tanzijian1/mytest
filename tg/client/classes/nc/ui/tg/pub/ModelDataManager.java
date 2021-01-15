package nc.ui.tg.pub;

import nc.bs.framework.common.NCLocator;
import nc.itf.tgfp.IInterestrateMaintain;
import nc.ui.querytemplate.querytree.QueryScheme;
import nc.ui.tgfp.pub.FPModelDataRefresher;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.bd.meta.IBDObject;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfp.pub.common.FPConst;
import nc.vo.trade.pub.HYBillVO;

public class ModelDataManager extends
		nc.ui.pubapp.uif2app.query2.model.ModelDataManager {
	public void refresh() {
		try {
			if (getModel() instanceof BillManageModel) {
				BillManageModel refModel = (BillManageModel) getModel();
				Object[] data = refModel.getSelectedOperaDatas();
				if (data == null || data.length == 0) {
					return;
				}
				if (data[0] instanceof HYBillVO) {
					String[] pks = new String[data.length];
					int i = 0;
					Class clazz = null;
					String pkname = null;
					for (Object d : data) {
						IBDObject target = refModel
								.getBusinessObjectAdapterFactory()
								.createBDObject(d);
						pks[i++] = (String) target.getId();
						clazz = d.getClass();
						pkname = ((SuperVO) ((HYBillVO) d).getParentVO())
								.getPKFieldName();
					}
					if (clazz == null) {
						return;
					}
					IInterestrateMaintain queryBill = NCLocator.getInstance()
							.lookup(IInterestrateMaintain.class);
					QueryScheme scheme = (QueryScheme) getQueryScheme();
					scheme.put(scheme.KEY_SQL_WHERE,
							SQLUtil.buildSqlForIn(pkname, pks));
					Object[] bills = null;
					if (nc.vo.tgfp.interestrate.agreement.AggInterestRateVO.class
							.equals(clazz)) {
						if (FPConst.CONST_FUNC_36H10402.equals(refModel
								.getContext().getNodeCode())) {// 新型协定存款单
							bills = queryBill
									.queryByNewAgreemetQueryScheme(getQueryScheme());
						} else {
							bills = queryBill
									.queryByAgreemetQueryScheme(getQueryScheme());
						}

					} else if (nc.vo.tgfp.interestrate.contract.AggInterestRateVO.class
							.equals(clazz)) {
						bills = queryBill
								.queryByContractQueryScheme(getQueryScheme());
					} else if (nc.vo.tgfp.interestrate.financing.AggInterestRateVO.class
							.equals(clazz)) {
						bills = queryBill
								.queryByFinancingQueryScheme(getQueryScheme());
					} else if (nc.vo.tgfp.interestrate.interest.AggInterestRateVO.class
							.equals(clazz)) {
						bills = queryBill
								.queryByInterestQueryScheme(getQueryScheme());
					}

					if (bills == null || bills.length < 1) {
						return;
					}
					refModel.update(bills);
					return;

				} else {
					new FPModelDataRefresher(getModel()).refreshData();

				}
			}
			new FPModelDataRefresher(getModel()).refreshData();

		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}

	}
}
