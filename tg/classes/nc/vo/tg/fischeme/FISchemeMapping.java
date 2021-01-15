package nc.vo.tg.fischeme;

import nc.jdbc.framework.mapping.IMappingMeta;

public class FISchemeMapping implements IMappingMeta {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6797304236680054595L;

	private static final String pkFieldName = "pk_scheme"; //主键

	private static final String tableName = "tgrz_fisceme";//表名

	private static final String[] attributeNames = new String[] { "pk_scheme", "disable"}; //需要跟新字段

	private final static String[] fieldsNames = new String[] { "pk_scheme", "disable" };  //需要跟新字段
   
	public String[] getAttributes() {
		// TODO Auto-generated method stub
		return attributeNames;
	}

	public String[] getColumns() {
		// TODO Auto-generated method stub
		return fieldsNames;
	}

	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return pkFieldName;
	}

	public String getTableName() {
		// TODO Auto-generated method stub
		return tableName;
	}

}

