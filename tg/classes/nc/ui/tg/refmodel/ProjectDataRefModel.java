package nc.ui.tg.refmodel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.tg.projectdata.ProjectDataCVO;
import nc.vo.tg.projectdata.ProjectDataVO;

public class ProjectDataRefModel extends AbstractRefModel {
	public ProjectDataRefModel() {
		super();
		reset();
	}

	public void reset() {
		setFieldCode(new String[] { ProjectDataVO.CODE, ProjectDataVO.NAME,
				ProjectDataVO.P6_DATADATE1, ProjectDataVO.PROJECTCORP,
				ProjectDataVO.PROJECTAREA });
		String[] fieldNames = new String[] { "编码", "项目名称", "项目日期", "所属公司",
				"所属区域" };
		setFieldName(fieldNames);
		setHiddenFieldCode(new String[] { ProjectDataCVO.PK_PROJECTDATA });
		setTableName("tgrz_projectdata");
		setPkFieldCode(ProjectDataVO.PK_PROJECTDATA);
		setRefCodeField(ProjectDataVO.CODE);
		setRefNameField(ProjectDataVO.NAME);

	}

	protected String getEnvWherePart() {
		String wherePart = "   dr = 0   ";
		return wherePart;
	}
}
