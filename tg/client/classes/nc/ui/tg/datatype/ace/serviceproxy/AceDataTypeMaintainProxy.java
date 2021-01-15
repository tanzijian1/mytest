package nc.ui.tg.datatype.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IDataTypeMaintain;
import nc.ui.pubapp.uif2app.model.IQueryService;
import nc.ui.uif2.model.IAppModelService;
import nc.vo.pub.BusinessException;
import nc.vo.tg.datatype.DataTypeVO;
import nc.vo.uif2.LoginContext;

/**
 * ʾ�����ݵĲ�������
 * 
 * @author author
 * @version tempProject version
 */
public class AceDataTypeMaintainProxy implements IAppModelService,
		IQueryService {
	@Override
	public Object insert(Object object) throws Exception {
		IDataTypeMaintain operator = NCLocator.getInstance().lookup(
				IDataTypeMaintain.class);
		return operator.insert((DataTypeVO) object);
	}

	@Override
	public Object update(Object object) throws Exception {
		IDataTypeMaintain operator = NCLocator.getInstance().lookup(
				IDataTypeMaintain.class);
		return operator.update((DataTypeVO) object);
	}

	@Override
	public void delete(Object object) throws Exception {
		IDataTypeMaintain operator = NCLocator.getInstance().lookup(
				IDataTypeMaintain.class);
		operator.delete((DataTypeVO) object);
	}

	/**
	 * ���/ȡ��������������Ϣ
	 * 
	 * @param vo
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Object enableTreeVO(Object object) throws BusinessException {
		IDataTypeMaintain operator = NCLocator.getInstance().lookup(
				IDataTypeMaintain.class);
		return operator.enableTreeVO((DataTypeVO) object);
	}

	@Override
	public Object[] queryByDataVisibilitySetting(LoginContext context)
			throws Exception {
		return null;
	}

	@Override
	public Object[] queryByWhereSql(String whereSql) throws Exception {
		IDataTypeMaintain operator = NCLocator.getInstance().lookup(
				IDataTypeMaintain.class);
		return operator.queryByWhereSql(whereSql);
	}
}
