package nc.ui.tg.refmodel;

import nc.bs.logging.Logger;
import nc.itf.tg.bd.pub.IBDMetaDataIDConst;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.IRefConst;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.pub.BusinessException;
import nc.vo.tg.fintype.FinTypeVO;
import nc.vo.tg.standard.StandardVO;
import nc.vo.util.BDVisibleUtil;
import nc.vo.util.SqlWhereUtil;
import nc.vo.util.VisibleUtil;

public class StandardRefModel extends AbstractRefGridTreeModel {
	private boolean isShowDisableData;

	public StandardRefModel() {
		super();
		reset();
	}

	public void reset() {
		setRootName("融资类型");

		setClassFieldCode(new String[] { FinTypeVO.CODE, FinTypeVO.NAME,
				FinTypeVO.PK_FINTYPE, FinTypeVO.PK_ORG, FinTypeVO.PK_FATHER });
		setFatherField(FinTypeVO.PK_FATHER);
		setChildField(FinTypeVO.PK_FINTYPE);
		setClassJoinField(FinTypeVO.PK_FINTYPE);
		setClassTableName(FinTypeVO.getDefaultTableName());
		setClassDefaultFieldCount(2);
		setClassDataPower(true);

		setFieldCode(new String[] { StandardVO.CODE, StandardVO.NAME,
				StandardVO.PERIODYEAR, StandardVO.VSTANDARD });
		String[] fieldNames = new String[] { "编码", "名称", "时间维度", "融资标准" };

		setFieldName(fieldNames);
		setHiddenFieldCode(new String[] { StandardVO.PK_STANDARD,
				StandardVO.PK_FINTYPE });
		setTableName("tgrz_standard");
		setPkFieldCode(StandardVO.PK_STANDARD);
		setRefCodeField(StandardVO.CODE);
		setRefNameField(StandardVO.NAME);
		setDocJoinField(StandardVO.PK_FINTYPE);
		// 使用启用条件
		setAddEnableStateWherePart(true);
		setDefaultFieldCount(4);

		// setCommonDataTableName(StandardVO.getDefaultTableName());
		// setCommonDataBasDocPkField(StandardVO.PK_STANDARD);

		resetFieldName();
		setOrderPart(StandardVO.CODE);

		// 过滤重复数据
		setStrPatch(IRefConst.DISTINCT);
		resetFieldName();

	}

	@Override
	public String getClassWherePart() {
		try {
			SqlWhereUtil sw = new SqlWhereUtil(super.getClassWherePart());
			sw.and(VisibleUtil.getRefVisibleCondition(getPk_group(),
					getPk_org(), IBDMetaDataIDConst.FINTYPE));
			if (this.isDisabledDataShow()) {
				sw.and(StandardVO.ENABLESTATE + " in ("
						+ IPubEnumConst.ENABLESTATE_ENABLE + ","
						+ IPubEnumConst.ENABLESTATE_DISABLE + ") ");
			} else {
				sw.and(StandardVO.ENABLESTATE + " = "
						+ IPubEnumConst.ENABLESTATE_ENABLE);
			}
			return sw.getSQLWhere();
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
		}
		return super.getClassWherePart();
	}

	protected String getEnvWherePart() {
		try {
			return "tgrz_standard.dr = 0 and "
					+ BDVisibleUtil.getRefVisibleCondition(getPk_group(),
							getPk_org(), IBDMetaDataIDConst.STANDARD,
							isShowDisableData());
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
		}
		return null;
	}

	public void setShowDisableData(boolean isShowDisableData) {
		this.isShowDisableData = isShowDisableData;
	}

	public boolean isShowDisableData() {
		return isShowDisableData;
	}
}
