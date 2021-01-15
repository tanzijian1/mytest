package nc.ui.tg.refmodel;

import nc.bs.logging.Logger;
import nc.itf.tg.bd.pub.IBDMetaDataIDConst;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.vo.pub.BusinessException;
import nc.vo.tg.datatype.DataTypeVO;
import nc.vo.util.VisibleUtil;

public class DataTypeRefModel extends AbstractRefTreeModel {
	public DataTypeRefModel() {
		super();
		init();
	}

	private void init() {
		// *根据需求设置相应参数
		setFieldCode(new String[] { DataTypeVO.CODE, DataTypeVO.NAME });
		setFieldName(new String[] { "编码", "名称" });
		setHiddenFieldCode(new String[] { DataTypeVO.PK_DATATYPE,
				DataTypeVO.PK_FATHER, DataTypeVO.INNERCODE });
		setPkFieldCode(DataTypeVO.PK_DATATYPE);
		setRefCodeField(DataTypeVO.CODE);
		setRefNameField(DataTypeVO.NAME);
		setTableName(DataTypeVO.getDefaultTableName());
		setFatherField(DataTypeVO.PK_FATHER);
		setChildField(DataTypeVO.PK_DATATYPE);
		setAddEnableStateWherePart(true);
		resetFieldName();
	}

	protected String getEnvWherePart() {
		String wherePart = "   dr = 0 ";
		try {
			wherePart = "  dr = 0   and "
					+ VisibleUtil.getRefVisibleCondition(getPk_group(),
							getPk_org(), IBDMetaDataIDConst.DATATYPE);
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
		}
		return wherePart;
	}
}
