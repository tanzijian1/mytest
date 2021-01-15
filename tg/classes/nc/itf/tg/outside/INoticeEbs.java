package nc.itf.tg.outside;

import nc.vo.ep.bx.BXVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.pub.BusinessException;

public interface INoticeEbs {

	/**
	 * ���ͷ������뵥��ͬ���ݵ�EBS
	 * 
	 * @param aggvo
	 *            �������뵥�ۺ�VO
	 * @param apflag
	 *            ���ͱ�ʶ��ɾ��(20)�����(10)
	 * @param isSetted
	 *            �Ƿ��ѽ���
	 * @throws BusinessException
	 */
	public void pushMattContractDataToEbs(AggMatterAppVO aggvo, String apflag,
			Boolean isSetted) throws BusinessException;

	/**
	 * ���ͱ�������ͬ���ݵ�EBS
	 * 
	 * @param aggvo
	 *            �������ۺ�VO
	 * @param apflag
	 *            ���ͱ�ʶ��ɾ��(20)�����(10)
	 * @param isSetted
	 *            �Ƿ��ѽ���
	 * @throws BusinessException
	 */
	public void pushBXContractDataToEbs(BXVO aggvo, String apflag,
			Boolean isSetted) throws BusinessException;

	/**
	 * ����������ִ�и������
	 * 
	 * @param updateSql
	 * @throws BusinessException
	 */
	public void updateFlag_RequiresNew(String updateSql)
			throws BusinessException;
}
