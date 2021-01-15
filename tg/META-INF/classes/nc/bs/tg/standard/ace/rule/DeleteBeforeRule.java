package nc.bs.tg.standard.ace.rule;

import nc.bs.uif2.validation.ValidationFailure;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.standard.AggStandardVO;
import nc.vo.tg.standard.StandardVO;
import nc.vo.util.BDReferenceChecker;

/**
 * 删除前校验规则
 * 
 * @author
 * 
 */
public class DeleteBeforeRule implements IRule<AggStandardVO> {

	@Override
	public void process(AggStandardVO[] vos) {
		// 补全数据规则
		this.fillDate(vos);
	}

	private void fillDate(AggStandardVO[] vos) {
		// 主实体
		for (AggStandardVO vo : vos) {
			ValidationFailure failure = BDReferenceChecker.getInstance()
					.validate(vo.getParentVO());
			if (failure != null) {
				ExceptionUtils.wrappBusinessException("融资标准【"
						+ vo.getParentVO().getAttributeValue(StandardVO.CODE)
						+ ","
						+ vo.getParentVO().getAttributeValue(
								StandardVO.NAME + "】:" + failure.getMessage()));
			}

		}
	}
}