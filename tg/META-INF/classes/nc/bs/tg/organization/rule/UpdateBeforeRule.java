package nc.bs.tg.organization.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.organization.OrganizationVO;

/**
 * ����ǰУ�����
 * 
 * @author ydx
 * 
 */
public class UpdateBeforeRule implements IRule<OrganizationVO> {

	@Override
	public void process(OrganizationVO[] vos) {
		// ��ȫ���ݹ���
		this.fillDate(vos);
	}

	private void fillDate(OrganizationVO[] vos) {
		// ��ʵ��
		for (OrganizationVO vo : vos) {
			// ����޸���
			vo.setAttributeValue(OrganizationVO.MODIFIER, AppContext.getInstance().getPkUser());
			// ����޸�ʱ��
			vo.setAttributeValue(OrganizationVO.MODIFIEDTIME, AppContext.getInstance().getServerTime());
		}
	}

}