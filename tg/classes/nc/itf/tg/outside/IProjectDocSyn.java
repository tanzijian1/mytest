package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.tg.projectbatchlog.ProjectDocParam;

public interface IProjectDocSyn {
	public static final String NC_DataSourceName = "design";
	public static final String GroupId = "000112100000000005FD";
	public static final String SaleUserId = "1001ZZ100000001MY4QD";
	public static final String SaleOperatorName = "SALE";

	public static final String ProjectClass04 = "10011210000000006P3R";

	public static final String XS_View_DataSourceName = "xsview";
	public static final String XS_InitialProjectDocView = "sdc.initialprojectdocview@link_sale";

	// 项目房间内码初始化
	public void initProjectDoc() throws BusinessException;

	// 项目房间内码初始化
	public void initProjectDocByEpscode(String epscode)
			throws BusinessException;

	// 项目档案同步
	public void synProjectDoc(String epscode, ProjectDocParam[] projectDocParams)
			throws BusinessException;

	// 项目档案同步
	public void synProjectDocByPk_projectbatchlog(String pk_projectbatchlog)
			throws BusinessException;
}
