package nc.ui.tg.refmodel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.tg.projectdata.ProjectDataCVO;
import nc.vo.tg.projectdata.ProjectDataVO;

public class ProjectStagesDataRefModel extends AbstractRefModel {
	public ProjectStagesDataRefModel() {
		super();
		reset();
	}

	public void reset() {

		setFieldCode(new String[] { ProjectDataVO.CODE, ProjectDataVO.NAME,
				ProjectDataCVO.PERIODIZATIONNAME, ProjectDataVO.P6_DATADATE1,
				ProjectDataVO.PROJECTCORP, ProjectDataVO.PROJECTAREA });
		String[] fieldNames = new String[] { "编码", "项目名称", "分期名称", "项目日期",
				"所属公司", "所属区域" };
		setFieldName(fieldNames);
		setHiddenFieldCode(new String[] { ProjectDataCVO.PK_PROJECTDATA_C,
				ProjectDataCVO.PK_PROJECTDATA });
		setTableName("v_tgrz_ref_project");
		setPkFieldCode(ProjectDataCVO.PK_PROJECTDATA_C);
		setRefCodeField(ProjectDataVO.CODE);
		setRefNameField(ProjectDataVO.PERIODIZATIONNAME);
		setDefaultFieldCount(6);
	}
}
