package nc.ui.tg.refmodel;

import nc.bs.logging.Logger;
import nc.itf.tg.bd.pub.IBDMetaDataIDConst;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.vo.pub.BusinessException;
import nc.vo.tg.fintype.FinTypeVO;
import nc.vo.util.VisibleUtil;

public class FinTypeRefModel extends AbstractRefTreeModel {
	public FinTypeRefModel() {
		super();
		init();
	}

	private void init() {
		// *根据需求设置相应参数
		setFieldCode(new String[] { FinTypeVO.CODE, FinTypeVO.NAME });
		setFieldName(new String[] { "编码", "名称" });
		setHiddenFieldCode(new String[] { FinTypeVO.PK_FINTYPE,
				FinTypeVO.PK_FATHER, FinTypeVO.INNERCODE });
		setPkFieldCode(FinTypeVO.PK_FINTYPE);
		setRefCodeField(FinTypeVO.CODE);
		setRefNameField(FinTypeVO.NAME);
		setTableName(FinTypeVO.getDefaultTableName());
		setFatherField(FinTypeVO.PK_FATHER);
		setChildField(FinTypeVO.PK_FINTYPE);
		setAddEnableStateWherePart(true);
		resetFieldName();
	}

	protected String getEnvWherePart() {
		String wherePart = "   dr = 0 ";
		try {
			wherePart = "  dr = 0   and "
					+ VisibleUtil.getRefVisibleCondition(getPk_group(),
							getPk_org(), IBDMetaDataIDConst.FINTYPE);
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
		}
		return wherePart;
	}
}
