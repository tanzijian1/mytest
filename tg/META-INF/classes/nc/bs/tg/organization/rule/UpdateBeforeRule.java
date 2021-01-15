package nc.bs.tg.organization.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.organization.OrganizationVO;

/**
 * 更改前校验规则
 * 
 * @author ydx
 * 
 */
public class UpdateBeforeRule implements IRule<OrganizationVO> {

	@Override
	public void process(OrganizationVO[] vos) {
		// 补全数据规则
		this.fillDate(vos);
	}

	private void fillDate(OrganizationVO[] vos) {
		// 主实体
		for (OrganizationVO vo : vos) {
			// 最后修改人
			vo.setAttributeValue(OrganizationVO.MODIFIER, AppContext.getInstance().getPkUser());
			// 最后修改时间
			vo.setAttributeValue(OrganizationVO.MODIFIEDTIME, AppContext.getInstance().getServerTime());
		}
	}

}