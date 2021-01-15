package nc.ui.tg.financingexpense.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import nc.itf.bd.pub.IBDResourceIDConstBasic;
import nc.itf.org.IOrgResourceCodeConstBasic;
import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.IRefDocEdit;
import nc.ui.bd.ref.IRefMaintenanceHandler;
import nc.ui.bd.ref.RefSearchFieldSetting;
import nc.ui.bd.ref.model.PsndocDefaultRefModel;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.bd.psn.util.GetBusiDateUtil;
import nc.vo.org.DeptVO;
import nc.vo.pub.lang.UFLiteralDate;

public class FinancingPersonRefModel extends PsndocDefaultRefModel {

	public FinancingPersonRefModel() {
		super();
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void reset() {
		setRefNodeName("人员");/* -=notranslate=- */
		if (isLeavePowerUI()) {
			setUiControlComponentClassName("nc.ui.bd.psn.psndoc.ref.busi.PsndocRefModelWithLeavedSelectedPanel");
		}
		setRootName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"10140psn", "010140psn0085"));// 部门
		setClassFieldCode(new String[] { DeptVO.CODE, DeptVO.NAME,
				DeptVO.PK_DEPT, DeptVO.PK_FATHERORG });
		setFatherField(DeptVO.PK_FATHERORG);
		setChildField(PsnjobVO.PK_DEPT);
		setClassJoinField(PsnjobVO.PK_DEPT);
		setClassTableName(new DeptVO().getDefaultTableName());
		setClassDefaultFieldCount(2);
		setClassDataPower(true);
		setFieldCode(new String[] { "bd_psndoc.code", "bd_psndoc.name",
				"bd_psnjob.pk_dept" });
		setFieldName(new String[] {
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0065"),
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0066"),
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0085") });// "编码", "姓名", "部门"
		setDefaultFieldCount(3);
		// 不增加隐藏的pk_dept字段，匹配时不能直接选中相应的树节点
		setHiddenFieldCode(new String[] { "bd_psndoc.pk_psndoc",
				"bd_psnjob.pk_psnjob", "bd_psndoc.idtype", "bd_psndoc.id" });
		setTableName("bd_psndoc left join  bd_psnjob on bd_psndoc.pk_psndoc = bd_psnjob.pk_psndoc ");
		setPkFieldCode("bd_psndoc.pk_psndoc");
		setDocJoinField("bd_psnjob.pk_dept");
		setRefCodeField("bd_psndoc.code");
		setRefNameField("bd_psndoc.name");
		setCommonDataBasDocTableName(new PsnjobVO().getTableName());
		setCommonDataBasDocPkField("bd_psnjob.pk_psnjob");
		setCommonDataTableName(new PsndocVO().getTableName());

		String strFomula = "getmlcvalue(\"org_dept\",\"name\",\"pk_dept\",bd_psnjob.pk_dept)";
		setFormulas(new String[][] { { "bd_psnjob.pk_dept", strFomula } });

		// if (!isMutiGroup())
		// setFilterRefNodeName(new String[] { "业务单元" });/* -=notranslate=- */
		// else
		// setFilterRefNodeName(new String[] { "集团", "业务单元" /* -=notranslate=-
		// */});
		setResourceID(IBDResourceIDConstBasic.PSNDOC);
		setClassResouceID(IOrgResourceCodeConstBasic.DEPT);
		setAddEnableStateWherePart(true);
		// 过滤重复数据
		setStrPatch(IRefConst.DISTINCT);
		resetFieldName();
		Hashtable content = new Hashtable();
		content.put(
				"0",
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0098"));// 身份证
		content.put(
				"1",
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0099"));// 军官证
		content.put(
				"2",
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140psn", "010140psn0100"));// 护照
		Hashtable convert = new Hashtable();
		convert.put("bd_psndoc.idtype", content);
		setDispConvertor(convert);
		setBlurQueryTableName("bd_psndoc");
		setRefMaintenanceHandler(new IRefMaintenanceHandler() {

			@Override
			public String[] getFucCodes() {
				return new String[] { "10140PSN" };
			}

			@Override
			public IRefDocEdit getRefDocEdit() {
				return null;
			}
		});
		// ------------------------------------------指定全文检索相关的设置信息-------------------------
		// 所有的搜索列及列描述
		Map<String, String> allFields = new HashMap<String, String>();
		allFields.put("name", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
				.getStrByID("10140psn", "010140psn0066")/* 姓名 */);
		allFields.put("code", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
				.getStrByID("10140psn", "010140psn0065")/* 编码 */);

		// 黙认的搜索列，按顺序进行搜索
		String[] defaultSearchFields = new String[] { "code", "name" };

		// 需要指定当前参照的nodeName
		String refNodeName = "人员"/* -=notranslate=- */;
		RefSearchFieldSetting setting = new RefSearchFieldSetting(refNodeName,
				allFields, defaultSearchFields);
		setSearchFieldSetting(setting);

		// -------------------------------------------------------------------------------------------
	}

	@Override
	public String getClassWherePart() {
		// SqlWhereUtil sw = new SqlWhereUtil(super.getClassWherePart());
		// sw.and(DeptVO.PK_ORG + " = '" + getPk_org() + "' and "
		// + DeptVO.ENABLESTATE + "=2");
		return null;
	}

	protected String getEnvWherePart() {
		// 按工作信息的任职业务单元过滤显示的数据
		// return "bd_psnjob.pk_org = '" + getPk_org() + "'" +
		// getIsLeaveCondition();
		return "bd_psnjob.ismainjob ='Y'";
	}

}
