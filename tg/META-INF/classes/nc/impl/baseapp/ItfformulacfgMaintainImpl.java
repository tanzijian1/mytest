package nc.impl.baseapp;

import nc.bs.dao.BaseDAO;
import nc.impl.pub.ace.AceItfformulacfgPubServiceImpl;
import nc.vo.baseapp.itfformulacfg.AggFormulaCfgVO;
import nc.itf.baseapp.IItfformulacfgMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * @Description:
 * @version with NC V6.5
 */
public class ItfformulacfgMaintainImpl extends AceItfformulacfgPubServiceImpl implements IItfformulacfgMaintain {
	private BaseDAO dao=null;
	@Override
	public void delete(AggFormulaCfgVO[] vos) throws BusinessException {
		super.pubdeleteBills(vos);
	}

	@Override
	public AggFormulaCfgVO[] insert(AggFormulaCfgVO[] vos) throws BusinessException {
		return super.pubinsertBills(vos);
	}

	@Override
	public AggFormulaCfgVO[] update(AggFormulaCfgVO[] vos) throws BusinessException {
		return super.pubupdateBills(vos);
	}

	@Override
	public AggFormulaCfgVO[] query(IQueryScheme queryScheme) throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public String insertVO_RequiresNew(SuperVO vo) throws BusinessException {
		String result=getBaseDAO().insertVO(vo);
		return result;
	}

	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;
	}
}
