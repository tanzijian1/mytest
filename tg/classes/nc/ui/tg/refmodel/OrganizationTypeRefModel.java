package nc.ui.tg.refmodel;

import nc.bs.logging.Logger;
import nc.itf.tg.bd.pub.IBDMetaDataIDConst;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.vo.pub.BusinessException;
import nc.vo.tg.organizationtype.OrganizationTypeVO;
import nc.vo.util.VisibleUtil;

public class OrganizationTypeRefModel extends AbstractRefTreeModel {
	public OrganizationTypeRefModel() {
		super();
		init();
	}

	private void init() {
		// *根据需求设置相应参数
		setFieldCode(new String[] { OrganizationTypeVO.CODE,
				OrganizationTypeVO.NAME });
		setFieldName(new String[] { "编码", "名称" });
		setHiddenFieldCode(new String[] {
				OrganizationTypeVO.PK_ORGANIZATIONTYPE,
				OrganizationTypeVO.PK_FATHER, OrganizationTypeVO.INNERCODE });
		setPkFieldCode(OrganizationTypeVO.PK_ORGANIZATIONTYPE);
		setRefCodeField(OrganizationTypeVO.CODE);
		setRefNameField(OrganizationTypeVO.NAME);
		setTableName(OrganizationTypeVO.getDefaultTableName());
		setFatherField(OrganizationTypeVO.PK_FATHER);
		setChildField(OrganizationTypeVO.PK_ORGANIZATIONTYPE);
		setAddEnableStateWherePart(true);
		resetFieldName();
	}

	protected String getEnvWherePart() {
		String wherePart = "  " + OrganizationTypeVO.getDefaultTableName()
				+ "." + "dr =0 ";
		try {
			wherePart = "  "
					+ OrganizationTypeVO.getDefaultTableName()
					+ "."
					+ "dr =0 and "
					+ VisibleUtil.getRefVisibleCondition(getPk_group(),
							getPk_org(), IBDMetaDataIDConst.ORGANIZATIONTYPE);
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
		}
		return wherePart;
	}
}
