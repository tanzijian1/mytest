package nc.impl.tg;

import nc.impl.pub.ace.AceProjectdataPubServiceImpl;
import nc.vo.tg.projectdata.AggProjectDataVO;
import nc.itf.tg.IProjectdataMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;

public class ProjectdataMaintainImpl extends AceProjectdataPubServiceImpl
		implements IProjectdataMaintain {

	@Override
	public void delete(AggProjectDataVO[] vos) throws BusinessException {
		super.pubdeleteBills(vos);
	}

	@Override
	public AggProjectDataVO[] insert(AggProjectDataVO[] vos)
			throws BusinessException {
		return super.pubinsertBills(vos);
	}

	@Override
	public AggProjectDataVO[] update(AggProjectDataVO[] vos)
			throws BusinessException {
		return super.pubupdateBills(vos);
	}

	@Override
	public AggProjectDataVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public void syncProjectData_RequiresNew(AggProjectDataVO vo)
			throws BusinessException {
		if (vo.getPrimaryKey() != null) {
			super.pubupdateBills(new AggProjectDataVO[] { vo });
		} else {
			super.pubinsertBills(new AggProjectDataVO[] { vo });
		}

	}

}
