package nc.ui.tg.pub.model;

import java.util.Arrays;
import java.util.Comparator;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.IQueryService;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.ui.uif2.model.IAppModelDataManagerEx;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.res.Variable;

public class BaseTreeModelDataManager implements IAppModelDataManagerEx {
	boolean isShowSealDataFlag = false;
	private String sqlWhere;
	private HierachicalDataAppModel model;
	private IQueryService service;

	@Override
	public void initModel() {
		try {
			String condition = "";
			if (!isShowSealDataFlag) {
				condition = " and enablestate = 2 ";
			}
			String pk_group = AppContext.getInstance().getPkGroup();
			Object[] objs = getVOsBySqlWhere("  isnull(dr,0)=0 and pk_group = '"
					+ pk_group + "' " + condition);
			if (objs != null) {
				SuperVO[] vos = new SuperVO[objs.length];
				System.arraycopy(objs, 0, vos, 0, vos.length);
				Arrays.sort(vos, new BaseTreeComparator());
				model.initModel(vos);
			} else {
				model.initModel(null);
			}
		} catch (Exception e) {
			throw new BusinessRuntimeException("", e);
		}
	}

	@Override
	public void setShowSealDataFlag(boolean showSealDataFlag) {
		isShowSealDataFlag = showSealDataFlag;
	}

	public void refresh() {
		Object obj = getModel().getSelectedData();
		initModel();
		try {
			getModel()
					.setSelectedNode(getModel().findNodeByBusinessObject(obj));
		} catch (Exception e) {
			getModel().setSelectedNode(null);
		}
	}

	public Object[] getVOsBySqlWhere(String sqlWhere) {
		setSqlWhere(sqlWhere);
		Object[] objs = null;
		try {
			objs = getService().queryByWhereSql(getSqlWhere());
			if (objs != null && objs.length == Variable.getMaxQueryCount()) {
				String hint = nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID(
								"pubapp_0",
								"0pubapp-0268",
								null,
								new String[] { "" + Variable.getMaxQueryCount() })/*
																				 * @
																				 * res
																				 * "查询结果太大，只返回了{0}条数据，请缩小范围再次查询"
																				 */;
				MessageDialog.showHintDlg(this.getModel().getContext()
						.getEntranceUI(), null, hint);
			}
		} catch (Exception e) {
			throw new BusinessRuntimeException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("pubapp_0", "0pubapp-0007")/*
																		 * @res
																		 * "查询单据发生异常"
																		 */, e);
		}
		return objs;
	}

	public String getSqlWhere() {
		return sqlWhere;
	}

	public void setSqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere;
	}

	public HierachicalDataAppModel getModel() {
		return model;
	}

	public IQueryService getService() {
		return service;
	}

	public void setModel(HierachicalDataAppModel model) {
		this.model = model;
	}

	public void setService(IQueryService service) {
		this.service = service;
	}

	class BaseTreeComparator implements Comparator<SuperVO> {
		public int compare(SuperVO vo1, SuperVO vo2) {
			return ((String) vo1.getAttributeValue("code"))
					.compareTo((String) vo2.getAttributeValue("code"));
		}

	}

	@Override
	public void initModelBySqlWhere(String sqlWhere) {
		// TODO 自动生成的方法存根

	}

}
