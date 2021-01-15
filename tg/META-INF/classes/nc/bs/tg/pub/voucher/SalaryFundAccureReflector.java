package nc.bs.tg.pub.voucher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nc.cmp.utils.CmpInterfaceProxy;
import nc.pubitf.fip.external.IBillReflectorService;
import nc.vo.fip.external.FipExtendAggVO;
import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tg.salaryfundaccure.SalaryFundAccureVO;
import nc.vo.tmpub.util.SqlUtil;

public class SalaryFundAccureReflector<T extends AggregatedValueObject> implements
IBillReflectorService {

	public Collection<FipExtendAggVO> queryBillByRelations(
			Collection<FipRelationInfoVO> relationvos) throws BusinessException {
		if (relationvos == null || relationvos.size() == 0)
			return null;

		List<String> list = new ArrayList<String>();
		for (Iterator<FipRelationInfoVO> iterator = relationvos.iterator(); iterator
				.hasNext();) {
			FipRelationInfoVO relationInfo = iterator.next();
			// 只查单据类型相等的
			if (needRelect(relationInfo)) {
				list.add(relationInfo.getRelationID());
			}
		}
		AggregatedValueObject[] aggvos = getBusiBill(list
				.toArray(new String[list.size()]));

		List<FipExtendAggVO> ret = new ArrayList<FipExtendAggVO>(aggvos.length);
		for (AggregatedValueObject aggvo : aggvos) {
			FipExtendAggVO vo = new FipExtendAggVO();
			vo.setBillVO(getVoucherBill((T) aggvo));
			vo.setRelationID(aggvo.getParentVO().getPrimaryKey());
			ret.add(vo);
		}
		return ret;
	}

	/**
	 * 得到送会计平台的VO（一般都是单据VO直接送）
	 * 
	 * @param billVO
	 * @return
	 */
	public AggregatedValueObject getVoucherBill(T billVO) {
		return billVO;
	}

	@SuppressWarnings("rawtypes")
	public AggregatedValueObject[] getBusiBill(String[] keys)
			throws BusinessException {
		if (null == keys || keys.length == 0)
			return null;
		String condition = SqlUtil.buildSqlForIn(getPKFieldName(), keys);

		// 修改下面的聚合VO类名即可
		Collection bills = CmpInterfaceProxy.INSTANCE
				.getPersistenceQueryService().queryBillOfVOByCond(
						getBillClass(), condition, true, false);

		return (AggregatedValueObject[]) bills
				.toArray(new AggregatedValueObject[bills.size()]);
	}

	public String getPKFieldName() {
		return "pk_salaryfundaccure";
	}

	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		return true;
	}

	public Class<SalaryFundAccureVO> getBillClass() {
		return SalaryFundAccureVO.class;
	}

}
