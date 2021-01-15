package nc.bs.tg.organization.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.organization.OrganizationVO;

/**
 * ����ǰУ�����
 * 
 * @author 
 * 
 */
public class InsertBeforeRule implements IRule<OrganizationVO> {

	@Override
	public void process(OrganizationVO[] vos) {
		// ��ȫ���ݹ���
		this.fillDate(vos);
	}

	private void fillDate(OrganizationVO[] vos) {
		// ��ʵ��
		for (OrganizationVO vo : vos) {
			// ������
			if (StringUtil.isEmptyWithTrim((String) vo
					.getAttributeValue(OrganizationVO.CREATOR))) {
				vo.setAttributeValue(OrganizationVO.CREATOR, AppContext
						.getInstance().getPkUser());
			}
			// ����ʱ��
			if (null == vo.getAttributeValue(OrganizationVO.CREATIONTIME)) {
				vo.setAttributeValue(OrganizationVO.CREATIONTIME, AppContext
						.getInstance().getServerTime());
			}

		}
	}

}
