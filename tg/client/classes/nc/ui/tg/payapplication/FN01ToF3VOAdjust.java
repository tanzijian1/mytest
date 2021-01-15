package nc.ui.tg.payapplication;

import nc.pub.fa.card.DefaultChangeVOAdjust;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class FN01ToF3VOAdjust extends DefaultChangeVOAdjust {
	public AggregatedValueObject[] batchAdjustAfterChange(
			AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs,
			ChangeVOAdjustContext adjustContext) throws BusinessException {

		for (AggregatedValueObject aggVO : destVOs) {
			if("N".equals(aggVO.getParentVO().getAttributeValue("def27").toString())){
				throw new BusinessException("该付款单已作废");
			}
			aggVO.getParentVO().setAttributeValue("pk_currtype",
					"1002Z0100000000001K1");
			aggVO.getParentVO().setAttributeValue("objtype",0);
			for (int i = 0; i < aggVO.getChildrenVO().length; i++) {
				aggVO.getChildrenVO()[i].setAttributeValue("pk_currtype", "1002Z0100000000001K1");
			}
		}

		return destVOs;
	}

	public AggregatedValueObject[] batchAdjustBeforeChange(
			AggregatedValueObject[] srcVOs, ChangeVOAdjustContext adjustContext)
			throws BusinessException {
		AggregatedValueObject[] changeAfterVOs = null;

		changeAfterVOs = createAggVOByBody(srcVOs);
		return changeAfterVOs;
	}
}
