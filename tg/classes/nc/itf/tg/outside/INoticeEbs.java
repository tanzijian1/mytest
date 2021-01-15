package nc.itf.tg.outside;

import nc.vo.ep.bx.BXVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.pub.BusinessException;

public interface INoticeEbs {

	/**
	 * 推送费用申请单合同数据到EBS
	 * 
	 * @param aggvo
	 *            费用申请单聚合VO
	 * @param apflag
	 *            类型标识：删除(20)或更新(10)
	 * @param isSetted
	 *            是否已结算
	 * @throws BusinessException
	 */
	public void pushMattContractDataToEbs(AggMatterAppVO aggvo, String apflag,
			Boolean isSetted) throws BusinessException;

	/**
	 * 推送报销单合同数据到EBS
	 * 
	 * @param aggvo
	 *            报销单聚合VO
	 * @param apflag
	 *            类型标识：删除(20)或更新(10)
	 * @param isSetted
	 *            是否已结算
	 * @throws BusinessException
	 */
	public void pushBXContractDataToEbs(BXVO aggvo, String apflag,
			Boolean isSetted) throws BusinessException;

	/**
	 * 开启新事物执行更新语句
	 * 
	 * @param updateSql
	 * @throws BusinessException
	 */
	public void updateFlag_RequiresNew(String updateSql)
			throws BusinessException;
}
