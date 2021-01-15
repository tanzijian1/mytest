package nc.bs.tg.organization.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.organization.OrganizationVO;

/**
 * 插入前校验规则
 * 
 * @author 
 * 
 */
public class InsertBeforeRule implements IRule<OrganizationVO> {

	@Override
	public void process(OrganizationVO[] vos) {
		// 补全数据规则
		this.fillDate(vos);
	}

	private void fillDate(OrganizationVO[] vos) {
		// 主实体
		for (OrganizationVO vo : vos) {
			// 创建人
			if (StringUtil.isEmptyWithTrim((String) vo
					.getAttributeValue(OrganizationVO.CREATOR))) {
				vo.setAttributeValue(OrganizationVO.CREATOR, AppContext
						.getInstance().getPkUser());
			}
			// 创建时间
			if (null == vo.getAttributeValue(OrganizationVO.CREATIONTIME)) {
				vo.setAttributeValue(OrganizationVO.CREATIONTIME, AppContext
						.getInstance().getServerTime());
			}

		}
	}

}
