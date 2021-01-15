package nc.ui.tg.payapplication;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IPaymentRequestMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pubapp.uif2app.query2.model.IRefQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ecpubapp.pattern.exception.ExceptionUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.paymentrequest.Business_b;

public class FN01QueryServiceImpl implements IRefQueryService {

	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		AggPayrequest[] retbills = null;
		IPaymentRequestMaintain service = NCLocator.getInstance().lookup(
				IPaymentRequestMaintain.class);
		IMDPersistenceQueryService sv = NCLocator.getInstance().lookup(
				IMDPersistenceQueryService.class);

		try {
			retbills = service.query(queryScheme);
			for (AggPayrequest aggPayrequest : retbills) {
				String pk = aggPayrequest.getParentVO().getPk_payreq();
				IUAPQueryBS serivce = NCLocator.getInstance().lookup(
						IUAPQueryBS.class);
				List<Business_b> bvoList = (List<Business_b>) serivce
						.executeQuery(
								"select * from  tgfn_payreqbus where nvl(dr,0)=0  and "
										+ "pk_payreq = '" + pk + "'",
								new BeanListProcessor(Business_b.class));
				List<Business_b> vos = new ArrayList<Business_b>();
				if (bvoList != null && bvoList.size() > 0) {
					aggPayrequest.setChildrenVO(bvoList
							.toArray(new Business_b[0]));
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

		AggPayrequest[] changeretbills = querybefore(retbills);
		return changeretbills;
	}

	/**
	 * 
	 * 对查询到的信息进行校验过滤
	 * 
	 * @param retbills
	 * @return
	 * @throws BusinessException
	 */
	private AggPayrequest[] querybefore(AggPayrequest[] retbills)
			throws BusinessException {
		IUAPQueryBS serivce = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		AggPayrequest[] AggVO = new AggPayrequest[retbills.length];
		int num = 0;
		for (int i = 0; i < retbills.length; i++) {
			AggPayrequest aggPayrequest = retbills[i];
			// 对查询到的数据金额进行遍历校验只要表体一行不通过就返回ture
			boolean check = true;
			Integer approvestatus = (Integer) aggPayrequest.getParentVO()
					.getAttributeValue("approvestatus");
			Integer effectstatus = (Integer) aggPayrequest.getParentVO()
					.getAttributeValue("effectstatus");
			String def27 = (String) aggPayrequest.getParentVO()
					.getAttributeValue("def27");
			if (approvestatus != 1) {
				check = false;
			}
			if (effectstatus != 10) {
				check = false;
			}
			if ("Y".equals(def27)) {
				check = false;
			}

			
			
			List<Business_b> bvos = new ArrayList<Business_b>();
			if (aggPayrequest.getChildrenVO() != null) {

				UFDouble paymny = UFDouble.ZERO_DBL;
				UFDouble summny = UFDouble.ZERO_DBL;
				
				for (int j = 0; j < aggPayrequest.getChildrenVO().length; j++) {
					Business_b buvo = (Business_b) aggPayrequest
							.getChildrenVO()[j];

					if (buvo.getLocal_money_de() != null) {
						paymny = paymny.add(buvo.getLocal_money_de());
					}

					summny = summny.add(querymny(buvo.getPk_business_b()));

					if (buvo.getLocal_money_de()
							.sub(querymny(buvo.getPk_business_b()))
							.compareTo(UFDouble.ZERO_DBL) > 0) {
						bvos.add(buvo);
					}

				}
				
				if (paymny.sub(summny).compareTo(UFDouble.ZERO_DBL) <= 0) {
					check = false;
				}
			}
			
			Business_b[] vos = new Business_b[bvos.size()];
			for (int j = 0; j < bvos.size(); j++) {
				vos[j] = bvos.get(j);
			}
			
			AggPayrequest payvo = new AggPayrequest();
			payvo.setParentVO(aggPayrequest.getParentVO());
			payvo.setChildrenVO(vos);

			// 对数据进行过滤
			if (check) {
				AggVO[i] = payvo;
				num++;
			}
		}
		AggPayrequest[] resVO = new AggPayrequest[num];
		int j = 0;
		for (int i = 0; i < retbills.length; i++) {
			AggPayrequest aggPayrequest = AggVO[i];
			if (aggPayrequest != null) {
				String def1 = aggPayrequest.getParentVO().getDef1();
				Business_b[] bodyVos = (Business_b[]) aggPayrequest
						.getChildrenVO();

				for (Business_b business_b : bodyVos) {
					if (business_b != null) {
						String src_itemid = business_b.getPk_business_b();
						UFDouble local_money_de = business_b
								.getLocal_money_de();
						String sql = "select sum(local_money_de) from ap_payitem where src_itemid = '"
								+ src_itemid + "' and nvl(dr,0) = 0";
						Object sunlmny = (Object) serivce.executeQuery(sql,
								new ColumnProcessor());
						UFDouble summny = sunlmny != null ? new UFDouble(
								sunlmny.toString()) : UFDouble.ZERO_DBL;

						business_b
								.setLocal_money_de(local_money_de.sub(summny));
					}
				}

				if (!"".equals(def1) && def1 != null) {
					String sql1 = "select def63 from ap_paybill where def1 = '"
							+ def1 + "' and nvl(dr,0) = 0";
					String sql2 = "select def62 from ap_paybill where def1 = '"
							+ def1 + "' and nvl(dr,0) = 0";
					String sql3 = "select def64 from ap_paybill where def1 = '"
							+ def1 + "' and nvl(dr,0) = 0";
					String sql4 = "select sum(money) from ap_paybill where def1 = '"
							+ def1 + "' and nvl(dr,0) = 0";
					List<Object> list1 = (List<Object>) serivce.executeQuery(
							sql1, new ColumnListProcessor());
					List<Object> list2 = (List<Object>) serivce.executeQuery(
							sql2, new ColumnListProcessor());
					List<Object> list3 = (List<Object>) serivce.executeQuery(
							sql3, new ColumnListProcessor());
					Object summoney = (Object) serivce.executeQuery(sql4,
							new ColumnProcessor());
					UFDouble def38 = UFDouble.ZERO_DBL;
					UFDouble def39 = UFDouble.ZERO_DBL;
					UFDouble def46 = UFDouble.ZERO_DBL;
					UFDouble summny = summoney != null ? new UFDouble(
							summoney.toString()) : UFDouble.ZERO_DBL;
					for (int k = 0; k < list1.size(); k++) {
						Object object1 = list1.get(k);
						Object object2 = list2.get(k);
						Object object3 = list3.get(k);
						def38 = def38.add(object1 != null ? new UFDouble(
								object1.toString()) : UFDouble.ZERO_DBL);
						def39 = def39.add(object2 != null ? new UFDouble(
								object2.toString()) : UFDouble.ZERO_DBL);
						def46 = def46.add(object3 != null ? new UFDouble(
								object3.toString()) : UFDouble.ZERO_DBL);
					}

					UFDouble odef38 = aggPayrequest.getParentVO().getDef38() == null ? UFDouble.ZERO_DBL
							: new UFDouble(aggPayrequest.getParentVO()
									.getDef38());
					UFDouble odef39 = aggPayrequest.getParentVO().getDef39() == null ? UFDouble.ZERO_DBL
							: new UFDouble(aggPayrequest.getParentVO()
									.getDef39());
					UFDouble odef46 = aggPayrequest.getParentVO().getDef46() == null ? UFDouble.ZERO_DBL
							: new UFDouble(aggPayrequest.getParentVO()
									.getDef46());
					UFDouble money = aggPayrequest.getParentVO().getMoney() == null ? UFDouble.ZERO_DBL
							: new UFDouble(aggPayrequest.getParentVO()
									.getMoney());

					aggPayrequest.getParentVO().setDef38(
							odef38.sub(def38).toString());
					aggPayrequest.getParentVO().setDef39(
							odef39.sub(def39).toString());
					aggPayrequest.getParentVO().setDef46(
							odef46.sub(def46).toString());
					aggPayrequest.getParentVO().setMoney(money.sub(summny));
				}
				resVO[j] = aggPayrequest;
				j++;
			}
		}
		return resVO;
	}

	@Override
	public Object[] queryByWhereSql(String whereSql) throws Exception {
		return null;
	}

	public UFDouble querymny(String src_itemid) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);

		String sql = "select sum(money_de) from ap_payitem where dr = 0 and src_itemid = '"
				+ src_itemid + "'";

		UFDouble mny = UFDouble.ZERO_DBL;
		Object strmny = null;

		try {
			strmny = bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (strmny != null) {
			mny = new UFDouble(strmny.toString());
		}

		return mny;
	}
}
