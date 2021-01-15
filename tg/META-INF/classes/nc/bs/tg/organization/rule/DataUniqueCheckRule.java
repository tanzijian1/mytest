package nc.bs.tg.organization.rule;

import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.organization.OrganizationVO;

public class DataUniqueCheckRule implements IRule<OrganizationVO> {

	@Override
	public void process(OrganizationVO[] vos) {
		if (vos == null || vos.length == 0) {
			return;
		}
		if (vos != null && vos.length > 0) {
			checkDBUnique(vos);
		}
	}

	public void checkDBUnique(OrganizationVO[] bills) {
		if (bills == null || bills.length == 0) {
			return;
		}
		for (int j = 0; j < bills.length; j++) {
			OrganizationVO vo = bills[j];
			IRowSet rowSet = new DataAccessUtils().query(this.getCheckSql(vo));
			if (rowSet.size() > 0) {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("3601tmbd_0",
										"03601tmbd-0303")/*
														 * @res
														 * "保存失败，当前新增或修改的信息，该集团已经存在编码或名称相同的记录。"
														 */);
			}
		}
	}


	/**
	 * 拼接唯一性校验的sql
	 * 
	 * @param bill
	 * @return
	 */
	private String getCheckSql(OrganizationVO vo) {
		StringBuffer sql = new StringBuffer();
		sql.append("select code,name ");
		sql.append("  from ");
		sql.append(" tgrz_organization");
		sql.append(" where dr=0 ");
		sql.append(" and ");
		sql.append(" (code ='");
		sql.append(vo.getAttributeValue("code"));
		sql.append("' ");
		sql.append(" or ");
		sql.append(" name='");
		sql.append(vo.getAttributeValue("name"));
		sql.append("' ");
		sql.append(")");
		if (!StringUtil.isEmptyWithTrim(vo.getPrimaryKey())) {
			sql.append(" and pk_organization<>'");
			sql.append(vo.getPrimaryKey());
			sql.append("'");
		}
		sql.append(";");
		return sql.toString();
	}

}