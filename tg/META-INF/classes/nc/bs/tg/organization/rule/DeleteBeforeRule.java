package nc.bs.tg.organization.rule;

import nc.bs.uif2.validation.ValidationFailure;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.organization.OrganizationVO;
import nc.vo.util.BDReferenceChecker;

/**
 * 删除前校验规则
 * 
 * @author 
 * 
 */
public class DeleteBeforeRule implements IRule<OrganizationVO> {

	@Override
	public void process(OrganizationVO[] vos) {
		// 补全数据规则
		this.fillDate(vos);
	}

	private void fillDate(OrganizationVO[] vos) {
		// 主实体
		for (OrganizationVO vo : vos) {
			ValidationFailure failure = BDReferenceChecker.getInstance()
					.validate(vo);
			if (failure != null) {
				ExceptionUtils.wrappBusinessException("融资机构【"
						+ vo.getAttributeValue(OrganizationVO.CODE)
						+ ","
						+ vo.getAttributeValue(OrganizationVO.NAME + "】:"
								+ failure.getMessage()));
			}

		}
	}
}