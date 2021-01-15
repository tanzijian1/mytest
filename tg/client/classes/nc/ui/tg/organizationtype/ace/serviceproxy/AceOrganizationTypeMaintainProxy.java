package nc.ui.tg.organizationtype.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IOrganizationTypeMaintain;
import nc.ui.pubapp.uif2app.model.IQueryService;
import nc.ui.uif2.model.IAppModelService;
import nc.vo.pub.BusinessException;
import nc.vo.tg.organizationtype.OrganizationTypeVO;
import nc.vo.uif2.LoginContext;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceOrganizationTypeMaintainProxy implements IAppModelService,IQueryService {
	@Override
	public Object insert(Object object) throws Exception {
		IOrganizationTypeMaintain operator = NCLocator.getInstance().lookup(
				IOrganizationTypeMaintain.class);
		return operator.insert((OrganizationTypeVO) object);
	}

	@Override
	public Object update(Object object) throws Exception {
		IOrganizationTypeMaintain operator = NCLocator.getInstance().lookup(
				IOrganizationTypeMaintain.class);
		return operator.update((OrganizationTypeVO) object);
	}

	@Override
	public void delete(Object object) throws Exception {
		IOrganizationTypeMaintain operator = NCLocator.getInstance().lookup(
				IOrganizationTypeMaintain.class);
		operator.delete((OrganizationTypeVO) object);
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
		IOrganizationTypeMaintain operator = NCLocator.getInstance().lookup(
				IOrganizationTypeMaintain.class);
		return operator.enableTreeVO((OrganizationTypeVO) object);
	}

	@Override
	public Object[] queryByDataVisibilitySetting(LoginContext context)
			throws Exception {
		return null;
	}

	@Override
	public Object[] queryByWhereSql(String whereSql) throws Exception {
		IOrganizationTypeMaintain operator = NCLocator.getInstance().lookup(
				IOrganizationTypeMaintain.class);
		return operator.queryByWhereSql(whereSql);
	}
}
