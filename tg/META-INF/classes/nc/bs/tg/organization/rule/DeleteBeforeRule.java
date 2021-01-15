package nc.bs.tg.organization.rule;

import nc.bs.uif2.validation.ValidationFailure;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.organization.OrganizationVO;
import nc.vo.util.BDReferenceChecker;

/**
 * ɾ��ǰУ�����
 * 
 * @author 
 * 
 */
public class DeleteBeforeRule implements IRule<OrganizationVO> {

	@Override
	public void process(OrganizationVO[] vos) {
		// ��ȫ���ݹ���
		this.fillDate(vos);
	}

	private void fillDate(OrganizationVO[] vos) {
		// ��ʵ��
		for (OrganizationVO vo : vos) {
			ValidationFailure failure = BDReferenceChecker.getInstance()
					.validate(vo);
			if (failure != null) {
				ExceptionUtils.wrappBusinessException("���ʻ�����"
						+ vo.getAttributeValue(OrganizationVO.CODE)
						+ ","
						+ vo.getAttributeValue(OrganizationVO.NAME + "��:"
								+ failure.getMessage()));
			}

		}
	}
}