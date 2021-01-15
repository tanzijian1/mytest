package nc.itf.tg.outside;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;

public class OutsideUtils {
	static IUAPQueryBS queryBS = null;

	public static String getOutsideInfo(String code) throws BusinessException {
		String sql = "select c.memo from bd_defdoc c  "
				+ "  where code = '"
				+ code
				+ "' and c.dr = 0 "
				+ "and c.pk_defdoclist in(select l.pk_defdoclist from bd_defdoclist l where l.code = 'TGOUT' and l.dr = 0)  ";
		String url = (String) getQueryBS().executeQuery(sql,
				new ColumnProcessor());
		return url;
	}

	public static IUAPQueryBS getQueryBS() {
		if (queryBS == null) {
			queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return queryBS;
	}

}
