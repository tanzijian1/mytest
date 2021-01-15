package nc.ui.tg.refmodel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.IRefConst;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.tg.organization.OrganizationVO;
import nc.vo.util.SqlWhereUtil;

public class OrganizationRefModel extends AbstractRefModel {
	private boolean isShowDisableData;

	public OrganizationRefModel() {
		reset();
	}

	public void reset() {
		setFieldCode(new String[] { OrganizationVO.CODE, OrganizationVO.NAME,
				OrganizationVO.PK_ORGANIZATIONTYPE, OrganizationVO.BRANCH,
				OrganizationVO.SUBBRANCH, OrganizationVO.CUSTYPE });
		String[] fieldNames = new String[] { "编码", "名称", "融资类型", "支行", "分行",
				"我司所属客户类型" };
		setDefaultFieldCount(6);

		setFieldName(fieldNames);
		setHiddenFieldCode(new String[] { OrganizationVO.PK_ORGANIZATION });
		setTableName(OrganizationVO.getDefaultTableName());
		setPkFieldCode(OrganizationVO.PK_ORGANIZATION);
		setRefCodeField(OrganizationVO.CODE);
		setRefNameField(OrganizationVO.NAME);
		// 使用启用条件
		setAddEnableStateWherePart(true);
		resetFieldName();
		setOrderPart(OrganizationVO.CODE);
		String strFomula = "getColValue(tgrz_organizationtype, name,pk_organizationtype,pk_organizationtype)";
		setFormulas(new String[][] { { OrganizationVO.PK_ORGANIZATIONTYPE,
				strFomula } });
		// 过滤重复数据
		setStrPatch(IRefConst.DISTINCT);
		resetFieldName();

	}

	protected String getEnvWherePart() {
		SqlWhereUtil sw = new SqlWhereUtil(" 1=1 ");
		sw.and(" dr = 0");
		if (this.isDisabledDataShow()) {
			sw.and(OrganizationVO.ENABLESTATE + " in ("
					+ IPubEnumConst.ENABLESTATE_ENABLE + ","
					+ IPubEnumConst.ENABLESTATE_DISABLE + ") ");
		} else {
			sw.and(OrganizationVO.ENABLESTATE + " = "
					+ IPubEnumConst.ENABLESTATE_ENABLE);
		}
		return sw.getSQLWhere();
	}

	public void setShowDisableData(boolean isShowDisableData) {
		this.isShowDisableData = isShowDisableData;
	}

	public boolean isShowDisableData() {
		return isShowDisableData;
	}
}
