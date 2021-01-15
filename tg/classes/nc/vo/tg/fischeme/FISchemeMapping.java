package nc.vo.tg.fischeme;

import nc.jdbc.framework.mapping.IMappingMeta;

public class FISchemeMapping implements IMappingMeta {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6797304236680054595L;

	private static final String pkFieldName = "pk_scheme"; //����

	private static final String tableName = "tgrz_fisceme";//����

	private static final String[] attributeNames = new String[] { "pk_scheme", "disable"}; //��Ҫ�����ֶ�

	private final static String[] fieldsNames = new String[] { "pk_scheme", "disable" };  //��Ҫ�����ֶ�
   
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

