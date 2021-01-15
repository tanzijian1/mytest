package nc.ui.tg.refmodel;

import nc.bs.logging.Logger;
import nc.itf.tg.bd.pub.IBDMetaDataIDConst;
import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.pub.BusinessException;
import nc.vo.tg.fischeme.FIScemeHVO;
import nc.vo.tg.projectdata.ProjectDataVO;
import nc.vo.util.VisibleUtil;

/**
 * ���ʷ�������
 * 
 * @author HUANGDQ
 * @date 2019��6��23�� ����12:24:53
 */
public class FIScemeRefModel extends AbstractRefModel {
	public FIScemeRefModel() {
		super();
		reset();
	}

	public void reset() {
		setFieldCode(new String[] { FIScemeHVO.BILLNO, FIScemeHVO.NAME });
		String[] fieldNames = new String[] { "����", "����", };
		setFieldName(fieldNames);
		setHiddenFieldCode(new String[] { FIScemeHVO.PK_SCHEME });
		setTableName("tgrz_fisceme");
		setPkFieldCode(FIScemeHVO.PK_SCHEME);
		setRefCodeField(FIScemeHVO.BILLNO);
		setRefNameField(FIScemeHVO.NAME);

	}

	protected String getEnvWherePart() {
		String wherePart = "   dr = 0 ";
		return wherePart;
	}
}
