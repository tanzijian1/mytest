package nc.bs.tg.standard.ace.rule;

import nc.bs.uif2.validation.ValidationFailure;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.standard.AggStandardVO;
import nc.vo.tg.standard.StandardVO;
import nc.vo.util.BDReferenceChecker;

/**
 * ɾ��ǰУ�����
 * 
 * @author
 * 
 */
public class DeleteBeforeRule implements IRule<AggStandardVO> {

	@Override
	public void process(AggStandardVO[] vos) {
		// ��ȫ���ݹ���
		this.fillDate(vos);
	}

	private void fillDate(AggStandardVO[] vos) {
		// ��ʵ��
		for (AggStandardVO vo : vos) {
			ValidationFailure failure = BDReferenceChecker.getInstance()
					.validate(vo.getParentVO());
			if (failure != null) {
				ExceptionUtils.wrappBusinessException("���ʱ�׼��"
						+ vo.getParentVO().getAttributeValue(StandardVO.CODE)
						+ ","
						+ vo.getParentVO().getAttributeValue(
								StandardVO.NAME + "��:" + failure.getMessage()));
			}

		}
	}
}