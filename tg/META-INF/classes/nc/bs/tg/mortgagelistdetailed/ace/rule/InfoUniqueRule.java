package nc.bs.tg.mortgagelistdetailed.ace.rule;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

public class InfoUniqueRule implements IRule<AggMortgageListDetailedVO> {

	@Override
	public void process(AggMortgageListDetailedVO[] vos) {
		BaseDAO dao = new BaseDAO();
		try {
			for (AggMortgageListDetailedVO vo : vos) {
				String sql = "select (select periodizationname from tgrz_projectdata_c where  tgrz_projectdata_c.pk_projectdata_c =tgrz_rep_mortgagelist.pk_periodization) from tgrz_rep_mortgagelist where dr = 0 and pk_periodization ='"
						+ vo.getParentVO().getPk_periodization()
						+ "' and pk_rep_mortgagelist<>'"
						+ vo.getPrimaryKey()
						+ "' ";
				String value = (String) dao.executeQuery(sql,
						new ColumnProcessor());
				if (value != null) {
					throw new BusinessException("项目分期【" + value
							+ "】已存在,请检验完再录入信息!");
				}

			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}
}
