package nc.itf.baseapp;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.baseapp.itfformulacfg.AggFormulaCfgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * @Description:外部接口协同公式增删改查接口
 * @version with NC V6.5
 */
public interface IItfformulacfgMaintain {

	public void delete(AggFormulaCfgVO[] vos) throws BusinessException;

	public AggFormulaCfgVO[] insert(AggFormulaCfgVO[] vos) throws BusinessException;

	public AggFormulaCfgVO[] update(AggFormulaCfgVO[] vos) throws BusinessException;

	public AggFormulaCfgVO[] query(IQueryScheme queryScheme) throws BusinessException;
	
	public String insertVO_RequiresNew(SuperVO vo) throws BusinessException;

}