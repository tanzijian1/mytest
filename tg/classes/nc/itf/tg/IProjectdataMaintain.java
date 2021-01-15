package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.projectdata.AggProjectDataVO;
import nc.vo.pub.BusinessException;

public interface IProjectdataMaintain {

	public void delete(AggProjectDataVO[] vos) throws BusinessException;

	public AggProjectDataVO[] insert(AggProjectDataVO[] vos)
			throws BusinessException;

	public AggProjectDataVO[] update(AggProjectDataVO[] vos)
			throws BusinessException;

	public AggProjectDataVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public void syncProjectData_RequiresNew(AggProjectDataVO vo)
			throws BusinessException;

}