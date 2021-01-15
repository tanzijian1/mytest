package nc.ui.org.ref;

import nc.bs.IconResources;
import nc.itf.org.IOrgConst;
import nc.itf.org.IOrgResourceCodeConst;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.util.OrgTreeCellRendererIconPolicy;
import nc.vo.org.util.OrgTypeManager;

/**
 * 财务组织 树形参照（以公司树构造财务组织树，公司本身为财务组织，公司下的所有财务组织平级）
 * @version NC6.0
 * @author tjl 2019-07-17
 */
public class FinanceOrgDIYRefTreeModel extends OrgBaseTreeDefaultRefModel {

	String orgFilterSQL = null;
	public FinanceOrgDIYRefTreeModel() {
		super();
		reset();
	}

	public void reset() {
		setRefNodeName("财务组织");/*-=notranslate=-*/
		
		setFieldCode(new String[] { "code", "name"});
		setFieldName(new String[] { 
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003279") /* @res "编码" */,
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001155") /* @res "名称" */
					});
		setHiddenFieldCode(new String[] {"pk_financeorg", "pk_fatherorg", "pk_corp", "pk_group", "enablestate" });
		setPkFieldCode("pk_financeorg");
		setRefCodeField("code");
		setRefNameField("name");
		setFatherField("pk_fatherorg");
		setChildField("pk_financeorg");
		
		setFilterRefNodeName(new String[] {"集团"});/*-=notranslate=-*/
		
		setResourceID(IOrgResourceCodeConst.FINANCEORG);
		
		//打开启用过滤条件开关
		setAddEnableStateWherePart(true);
		
		setOrderPart("code");
		
		resetFieldName();
		
    this.setTreeIconPolicy(new OrgTreeCellRendererIconPolicy(
        IconResources.ICON_Bu));
	}
	
	@Override
	public String getTableName() {
//		String financeorg_fieldname = OrgTypeManager.getInstance().getOrgTypeByID(IOrgConst.FINANCEORGTYPE).getFieldname();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT org_corp.code, org_corp.name, org_corp.name2, org_corp.name3, org_corp.name4, org_corp.name5, org_corp.name6, org_corp.pk_corp AS pk_financeorg, org_corp.pk_fatherorg, org_corp.pk_corp, org_corp.pk_group, org_financeorg.enablestate " +
				"FROM org_corp LEFT JOIN org_financeorg ON org_corp.pk_corp = org_financeorg.pk_financeorg " +
				"WHERE  "+getOrgFilterSQL()+" ");
		
		return "(" + sb.toString() + ") org_financeorg_temp";
	}
	
	@Override
	public void filterValueChanged(ValueChangedEvent changedValue) {
		super.filterValueChanged(changedValue);
		String[] selectedPKs = (String[]) changedValue.getNewValue();
		if (selectedPKs != null && selectedPKs.length > 0) {
			setPk_group(selectedPKs[0]);
		}
	}
	
	/**
	 * 因为财务组织树是由公司树来构成的，没有innercode，所以不允许使用包含下级功能
	 */
	@Override
	public boolean isIncludeSub() {
		return false;
	}
	
	public String getOrgFilterSQL() {
		// if (StringUtils.isEmpty(orgFilterSQL)) {
		StringBuffer sql = new StringBuffer();
		sql.append(" pk_financeorg  in (")
				.append("select distinct v1.pk_financeorg from(  ")
				.append(getFinanceOrgSQL() + ")v1 ")
				.append(" left join ( ")
				.append(getFinanceOrgSQL()
						+ ")v2 on v1.pk_financeorg = v2.pk_fatherorg ")
				.append(" where v2.pk_financeorg is not null ").append(")");
		orgFilterSQL = sql.toString();
		// }

		return orgFilterSQL;
	}

	public String getFinanceOrgSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT org_financeorg.pk_financeorg ")
				.append(",org_financeorg.code ")
				.append(",org_financeorg.name")
				.append(",org_orgs.pk_fatherorg")
				.append(",org_financeorg.enablestate ")
				.append(" FROM org_financeorg ")
				.append(" inner JOIN org_orgs  on org_financeorg.pk_financeorg = org_orgs.pk_org ")
				.append("  WHERE org_financeorg.enablestate = 2 and  org_orgs.enablestate = 2  order by org_orgs.code  ");

		return sql.toString();
	}

}
