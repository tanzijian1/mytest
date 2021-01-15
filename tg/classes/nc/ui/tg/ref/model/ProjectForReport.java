package nc.ui.tg.ref.model;

import nc.ui.bd.ref.AbstractRefModel;

public class ProjectForReport extends AbstractRefModel {

	public ProjectForReport() {
		super();
		init();
	}

	private void init() {
		// *��������������Ӧ����
		setFieldCode(new String[] { "project_code", "project_name", "project_sh_name" });
		setFieldName(new String[] { "��Ŀ����", "��Ŀ����", "���" });
		setHiddenFieldCode(new String[] { "pk_project" });
		setPkFieldCode("pk_project");
		setRefCodeField("project_code");
		setRefNameField("project_code");
		setTableName("bd_project");
		setRefNodeName("��Ŀ���ɱ�̨�ˣ�");
		setRefTitle("��Ŀ���ɱ�̨�ˣ�");
		// setRefShowNameField("");
	}

	@Override
	protected String getEnvWherePart() {
		return "nvl(dr,0)=0";
	}

}
