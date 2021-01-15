package nc.ui.tg.pub.filter;

import java.util.ArrayList;
import java.util.List;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.refregion.AbstractLinkageColumnListener;
import nc.ui.querytemplate.filtereditor.FilterEditorWrapper;
import nc.ui.querytemplate.filtereditor.IFilterEditor;
import nc.ui.querytemplate.value.IFieldValueElement;
import nc.vo.pf.pub.util.SQLUtil;

public class QueryTempMainOrgFilterBaseDoc extends
		AbstractLinkageColumnListener {
	private String orgField = null;
	private String targetField = null;

	public QueryTempMainOrgFilterBaseDoc(QueryConditionDLGDelegator dlg,
			String orgField, String basedocField) {
		super(dlg);
		this.orgField = orgField;
		this.targetField = basedocField;
	}

	@Override
	protected void processLinkageLogic(List<IFieldValueElement> paramList,
			IFilterEditor paramIFilterEditor) {
		List<String> diffValues = new ArrayList();
		for (IFieldValueElement fve : paramList) {
			if ((!diffValues.contains(fve.getSqlString()))
					&& (fve.getSqlString() != null)) {

				diffValues.add(fve.getSqlString());
			}
		}
		FilterEditorWrapper wrapper = new FilterEditorWrapper(
				paramIFilterEditor);
		UIRefPane refPane = (UIRefPane) wrapper
				.getFieldValueElemEditorComponent();
		AbstractRefModel refModel = refPane.getRefModel();

		if (diffValues.size() > 0) {
			String[] pk_orgs = (String[]) diffValues.toArray(new String[0]);
			if (pk_orgs.length > 0) {
				refModel.addWherePart(" and exists (select 1"
						+ " from bd_project_b"
						+ "  where bd_project_b.pk_project = bd_project.pk_project and dr =0 and "
						+ SQLUtil.buildSqlForIn("pk_parti_org", pk_orgs) + ")");
			} else {
				refModel.addWherePart("and 1 = 2");
			}
		}
	}

	public void addEditorListener() {
		setFatherPath(orgField);
		setChildPath(targetField);
		qryCondDLGDelegator.registerCriteriaEditorListener(this);
	}

}
