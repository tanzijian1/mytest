package nc.ui.tg.ref.model;

import nc.ui.bd.ref.AbstractRefModel;

public class ProjectForReport extends AbstractRefModel {

	public ProjectForReport() {
		super();
		init();
	}

	private void init() {
		// *根据需求设置相应参数
		setFieldCode(new String[] { "project_code", "project_name", "project_sh_name" });
		setFieldName(new String[] { "项目编码", "项目名称", "简称" });
		setHiddenFieldCode(new String[] { "pk_project" });
		setPkFieldCode("pk_project");
		setRefCodeField("project_code");
		setRefNameField("project_code");
		setTableName("bd_project");
		setRefNodeName("项目（成本台账）");
		setRefTitle("项目（成本台账）");
		// setRefShowNameField("");
	}

	@Override
	protected String getEnvWherePart() {
		return "nvl(dr,0)=0";
	}

}
