package nc.ui.tg.payapplication;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ITaxCalculationMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pubapp.uif2app.query2.model.IRefQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ecpubapp.pattern.exception.ExceptionUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.vo.tgfn.taxcalculation.TaxCalculationBody;

public class FN10QueryServiceImpl  implements IRefQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		AggTaxCalculationHead[] retbills = null;
		ITaxCalculationMaintain service = NCLocator.getInstance().lookup(
				ITaxCalculationMaintain.class);
		IMDPersistenceQueryService sv = NCLocator.getInstance().lookup(
				IMDPersistenceQueryService.class);
		List<AggTaxCalculationHead> filterlist=new ArrayList<AggTaxCalculationHead>();
		try {
			retbills = service.query(queryScheme);
			for (AggTaxCalculationHead aggPayrequest : retbills) {
				String pk = aggPayrequest.getParentVO().getPk_taxcalhead();
				IUAPQueryBS serivce = NCLocator.getInstance().lookup(
						IUAPQueryBS.class);
				List<TaxCalculationBody> bvoList = (List<TaxCalculationBody>) serivce.executeQuery("select * from  tgfn_taxcalbody where nvl(dr,0)=0  and "+ "pk_taxcalhead = '" + pk + "'",new BeanListProcessor(TaxCalculationBody.class));
				List<TaxCalculationBody> vos = new ArrayList<TaxCalculationBody>();
				if (bvoList != null && bvoList.size() > 0) {
					aggPayrequest.setChildrenVO(bvoList
							.toArray(new TaxCalculationBody[0]));
				}
				if(bvoList!=null&&bvoList.size()>0)filterlist.add(aggPayrequest);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

//		AggTaxCalculationHead[] changeretbills = querybefore(retbills);
		return filterlist.toArray(new AggTaxCalculationHead[0]);
	}

//	/**
//	 * 
//	 * 对查询到的信息进行校验过滤
//	 * 
//	 * @param retbills
//	 * @return
//	 */
//	private AggTaxCalculationHead[] querybefore(AggTaxCalculationHead[] retbills) {
//		AggTaxCalculationHead[] AggVO = new AggTaxCalculationHead[retbills.length];
//		int num = 0;
//		for (int i = 0; i < retbills.length; i++) {
//			AggTaxCalculationHead aggPayrequest = retbills[i];
//			// 对查询到的数据金额进行遍历校验只要表体一行不通过就返回ture
//			boolean check = true;
//			Integer approvestatus = (Integer) aggPayrequest.getParentVO()
//					.getAttributeValue("approvestatus");
//			Integer effectstatus = (Integer) aggPayrequest.getParentVO()
//					.getAttributeValue("effectstatus");
//			String def27 = (String) aggPayrequest.getParentVO()
//					.getAttributeValue("def27");
//			if (approvestatus != 1) {
//				check = false;
//			}
//			if (effectstatus != 10) {
//				check = false;
//			}
//			if ("Y".equals(def27)) {
//				check = false;
//			}
//			if (aggPayrequest.getChildrenVO() != null) {
//				for (int j = 0; j < aggPayrequest.getChildrenVO().length; j++) {
//					TaxCalculationBody buvo = (TaxCalculationBody) aggPayrequest
//							.getChildrenVO()[j];
//					UFDouble paymny = UFDouble.ZERO_DBL;
//
//					if (buvo.getLocal_money_de() != null) {
//						paymny = buvo.getLocal_money_de();
//					}
//
//					UFDouble summny = querymny(buvo.getPk_business_b());
//					if (paymny.sub(summny).compareTo(UFDouble.ZERO_DBL) < 0) {
//						// check = false;
//					}
//				}
//			}
//			// 对数据进行过滤
//			if (check) {
//				AggVO[i] = aggPayrequest;
//				num++;
//			}
//		}
//		AggTaxCalculationHead[] resVO = new AggTaxCalculationHead[num];
//		int j = 0;
//		for (int i = 0; i < retbills.length; i++) {
//			AggTaxCalculationHead aggPayrequest = AggVO[i];
//			if (aggPayrequest != null) {
//				resVO[j] = aggPayrequest;
//				j++;
//			}
//		}
//		return resVO;
//	}
//
	@Override
	public Object[] queryByWhereSql(String whereSql) throws Exception {
		return null;
	}
//
//	public UFDouble querymny(String src_itemid) {
//		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
//
//		String sql = "select sum(money_de) from ap_payitem where dr = 0 and src_itemid = '"
//				+ src_itemid + "'";
//
//		UFDouble mny = UFDouble.ZERO_DBL;
//		Object strmny = null;
//
//		try {
//			strmny = bs.executeQuery(sql, new ColumnProcessor());
//		} catch (BusinessException e) {
//			e.printStackTrace();
//		}
//		if (strmny != null) {
//			mny = new UFDouble(strmny.toString());
//		}
//
//		return mny;
//	}
}
