package nc.bs.tg.settlement;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.am.db.processor.ListMapProcessor;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.tg.settlement.ISettleAfterService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.cdm.contractbankcredit.AggContractBankCreditVO;
import nc.vo.cdm.contractbankcredit.CwgwfzxqkBVO;
import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.cmp.settlement.SettlementHeadVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.contract.AggContractVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class SettleAfterServiceIImpl implements ISettleAfterService {

	@Override
	public String settleAfterWriteBack(SettlementAggVO[] aggvo)
			throws BusinessException {
		for (SettlementAggVO settlementAggVO : aggvo) {
			SettlementHeadVO headvo = (SettlementHeadVO) settlementAggVO
					.getParentVO();
			try {
				String sql = "select c.def4 from cmp_paybill d,tgrz_financexpense c where c.pk_finexpense =d.pk_upbill and d.pk_paybill='"
						+ headvo.getPk_busibill() + "' ";
				String pk_contract = (String) new BaseDAO().executeQuery(sql,
						new ColumnProcessor());
				nc.itf.pub.contract.IContractQueryService qs = NCLocator
						.getInstance()
						.lookup(nc.itf.pub.contract.IContractQueryService.class);
				if(pk_contract!=null){
					CwgwfzxqkBVO vo=new CwgwfzxqkBVO();
					vo.setAttributeValue("m_zfje", headvo.getPrimal());
					vo.setAttributeValue("d_zfsj", new UFDateTime());
					vo.setAttributeValue("pk_zfgs", headvo.getPk_org());
					vo.setAttributeValue("pk_contract", pk_contract);
					vo.setAttributeValue("dr",0);
					new BaseDAO().insertVO(vo);
					//add by tjl 2019-08-15
					//先合同pk查询财顾费的支付金额
					String sqlcg = "select sum(nvl(m_zfje,0)) m_zfje from cdm_cwgwfzxqk where pk_contract = '"+pk_contract+"' and nvl(dr,0) = 0 ";
					Object m_zfje = (Object) new BaseDAO().executeQuery(sqlcg,
							new ColumnProcessor());
					if(m_zfje!=null){
						new BaseDAO().executeUpdate("update cdm_contract set m_sjcwgwf = "+new UFDouble(m_zfje.toString())+"  where pk_contract = '"+pk_contract+"' and nvl(dr,0) = 0 ");
					}
				}
				
			} catch (BusinessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}

		}
		// TODO 自动生成的方法存根
		return null;
	}
	
	/**
	 * 获取还款的聚合vo
	 * @param payBillcode
	 * @return
	 * @throws BusinessException
	 */
	private AggContractBankCreditVO[] getFinancexpense(String pk_contract) throws BusinessException {
		AggContractBankCreditVO[] aggvo = null;
		if(pk_contract.length()>0){
			BillQuery<AggContractBankCreditVO> billquery = new BillQuery<AggContractBankCreditVO>(AggContractBankCreditVO.class);
			aggvo=billquery.query(new String[] {pk_contract});
		}
		return aggvo;
	}

}
