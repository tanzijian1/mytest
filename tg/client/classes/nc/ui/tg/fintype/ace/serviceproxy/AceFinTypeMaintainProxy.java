package nc.ui.tg.fintype.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IFinTypeMaintain;
import nc.ui.pubapp.uif2app.model.IQueryService;
import nc.ui.uif2.model.IAppModelService;
import nc.vo.pub.BusinessException;
import nc.vo.tg.fintype.FinTypeVO;
import nc.vo.uif2.LoginContext;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceFinTypeMaintainProxy implements IAppModelService, IQueryService {
	@Override
	public Object insert(Object object) throws Exception {
		IFinTypeMaintain operator = NCLocator.getInstance().lookup(
				IFinTypeMaintain.class);
		return operator.insert((FinTypeVO) object);
	}

	@Override
	public Object update(Object object) throws Exception {
		IFinTypeMaintain operator = NCLocator.getInstance().lookup(
				IFinTypeMaintain.class);
		return operator.update((FinTypeVO) object);
	}

	@Override
	public void delete(Object object) throws Exception {
		IFinTypeMaintain operator = NCLocator.getInstance().lookup(
				IFinTypeMaintain.class);
		operator.delete((FinTypeVO) object);
	}

	/**
	 * 封存/取消封存地区分类信息
	 * 
	 * @param vo
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Object enableTreeVO(Object object) throws BusinessException {
		IFinTypeMaintain operator = NCLocator.getInstance().lookup(
				IFinTypeMaintain.class);
		return operator.enableTreeVO((FinTypeVO) object);
	}

	@Override
	public Object[] queryByDataVisibilitySetting(LoginContext context)
			throws Exception {
		return null;
	}

	@Override
	public Object[] queryByWhereSql(String whereSql) throws Exception {
		IFinTypeMaintain operator = NCLocator.getInstance().lookup(
				IFinTypeMaintain.class);
		return operator.queryByWhereSql(whereSql);
	}
}
