package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.OutsideLogVO;

public interface IlogRecord {
	/**
	 * NC�ɱ����ҵ���Ӧ�ĸ���긶��дEBS ��־��¼�ӿ� 2019-12-04-̸�ӽ�
	 */
	public void logRecord(OutsideLogVO logVO) throws BusinessException;
}
