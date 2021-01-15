package nc.bs.tg.outside.word.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.JdbcTransaction;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.ref.RefPubUtil;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.tg.outside.word.mintab.VoucherCVO;
import nc.vo.tg.outside.word.mintab.VoucherHVO;
import nc.vo.tg.outside.word.mintab.VoucherLVO;
import nc.vo.tg.outside.word.mintab.VoucherPVO;

import org.apache.commons.lang.StringUtils;

public class VoucherMinTabUtils extends WordBillUtils {
	static VoucherMinTabUtils utils = null;
	String datalink = "@LINK_WORD";
	HashMap<String, Map<String, String>> userMap = null;
	HashMap<String, String> assMap = null;
	HashMap<String, String> assItemMap = null;

	public VoucherMinTabUtils() {
		userMap = new HashMap<String, Map<String, String>>();
		assMap = new HashMap<String, String>();
		assItemMap = new HashMap<String, String>();

	}

	public static VoucherMinTabUtils getUtils() {
		if (utils == null) {
			utils = new VoucherMinTabUtils();
		}
		return utils;
	}

	public void initTempTable() throws BusinessException {
		SQLUtil.createTempTable(
				"TEMP_ACC_DOC_H",
				"ACC_BOOK_NAME VARCHAR2(255),ATTACHMENT_NUM NUMBER(38,4),CASHIER VARCHAR2(255),CASHIER_NAME VARCHAR2(255),CHECKED_BY VARCHAR2(255),CHECKED_BY_NAME VARCHAR2(255),CREATION_DATE VARCHAR2(20),CURRENCY_CODE VARCHAR2(255),DR NUMBER(38,2),FILE_DATES VARCHAR2(1020),INPUT_BY VARCHAR2(255),INPUT_BY_NAME VARCHAR2(255),MONTH VARCHAR2(20),ORDER_SEQ VARCHAR2(32),ORG_CODE VARCHAR2(255),ORG_NAME VARCHAR2(255),PREPARED_BY VARCHAR2(255),PREPARED_BY_NAME VARCHAR2(255),REMARKS VARCHAR2(255),SRC_DOC_NUM NUMBER(38,4),SRC_TYPE VARCHAR2(255),TAY NUMBER(38,2),TOTAL_CREDIT NUMBER(38,4),TOTAL_DEBIT NUMBER(38,4),TRANSACTOR VARCHAR2(255),TRANSACTOR_NAME VARCHAR2(255),TS VARCHAR2(25),YEAR VARCHAR2(20)",
				null);
		SQLUtil.createTempTable(
				"TEMP_ACC_DOC_L",
				"ACC_NAME VARCHAR2(2500),AMOUNT_OF_CREDIT NUMBER(38,4),AMOUNT_OF_DEBIT NUMBER(38,4),CREATION_DATE VARCHAR2(20),DR NUMBER(38,2),H_SEQ VARCHAR2(32),ORDER_SEQ VARCHAR2(32),SRC_DOC_NUM VARCHAR2(255),SUMMARY VARCHAR2(255),TS CHAR(25)",
				null);
		SQLUtil.createTempTable(
				"TEMP_ACC_DOC_C",
				"ATTACHMENT_NUM NUMBER(38,4),ATTACHMENT_NUM_MARK NUMBER(38,4),ATTACHMENT_NUM_PAY NUMBER(38,4),ATTACHMENT_NUM_TRA NUMBER(38,4),DR NUMBER(38,2),MONTH VARCHAR2(20),ORDER_SEQ VARCHAR2(255),ORG_CODE VARCHAR2(255),REASON VARCHAR2(1000),SRC_END NUMBER(38,4),SRC_END_END NUMBER(38,4),SRC_END_MARK NUMBER(38,4),SRC_END_TRA NUMBER(38,4),SRC_NUM NUMBER(38,4),SRC_NUM_MARK NUMBER(38,4),SRC_NUM_PAY NUMBER(38,4),SRC_NUM_TRA NUMBER(38,4),SRC_START NUMBER(38,4),SRC_START_MARK NUMBER(38,4),SRC_START_PAY NUMBER(38,4),SRC_START_TRA NUMBER(38,4),STATUS NUMBER(38,2),TOTAL_CREDIT NUMBER(38,4),TOTAL_CREDIT_MARK NUMBER(38,4),TOTAL_CREDIT_PAY NUMBER(38,4),TOTAL_CREDIT_TRA NUMBER(38,4),TOTAL_DEBIT NUMBER(38,4),TOTAL_DEBIT_MARK NUMBER(38,4),TOTAL_DEBIT_PAY NUMBER(38,4),TOTAL_DEBIT_TRA NUMBER(38,4),TS VARCHAR2(25),YEAR VARCHAR2(10)",
				null);
		SQLUtil.createTempTable(
				"TEMP_ACC_DOC_P",
				"CREATION_DATE VARCHAR2(100),DR NUMBER(22),H_SEQ VARCHAR2(32),ORDER_SEQ VARCHAR2(32),P_CODE VARCHAR2(1000),SRC_DOC_NUM VARCHAR2(255),TS CHAR(25)",
				null);
	}

	public void onSyncVoucherMinTab(String org, String year, String proid)
			throws BusinessException {
		String sql = "select count(1) from ACC_DOC_C@LINK_WORD v"
				+ " inner join org_orgs on org_orgs.code = v.org_code "
				+ " where org_orgs.pk_org ='" + org + "' and v.year = '" + year
				+ "' and v.month = '" + proid + "'";
		Object value = getBaseDAO().executeQuery(sql, new ColumnProcessor());
		if (!"0".equals(String.valueOf(value))) {
			throw new BusinessException("凭证信息已同步电子文档,请勿重复推送!");
		}
		initTempTable();
		List<VoucherHVO>  headlist = getVoucher(org, year, proid);
		if (headlist != null && headlist.size() > 0) {
			headlist = onTransVoucher(headlist);
		}
		Logger.error("凭证信息：查询到符合条件数据" + headlist.size()+"条");
		List<VoucherLVO> bodylist = getVoucherLVOs(org, year, proid);
		if (bodylist != null && bodylist.size() > 0) {
			bodylist = onTransDetail(bodylist);
		}
		Logger.error("明细信息：查询到符合条件数据" + bodylist.size()+"条");
		VoucherCVO totalVO = getTotalVO(org, year, proid);
		List<VoucherPVO> bodylist2 = getVoucherPVOs(org, year, proid);
		Logger.error("voucherPVOs：查询到符合条件数据" + bodylist2.size()+"条");

		if (headlist != null && headlist.size() > 0) {
			String[] headPKs=getBaseDAO().insertVOArrayWithPK(
					headlist.toArray(new VoucherHVO[0]));
			Logger.error("插入凭证信息临时表" + headPKs.length+"条");
			String[] bodyPKs=getBaseDAO().insertVOArrayWithPK(
					bodylist.toArray(new VoucherLVO[0]));
			Logger.error("插入明细信息临时表" + bodyPKs.length+"条");
			String totalPK=getBaseDAO().insertVO(totalVO);
			Logger.error("插入total临时表" + totalPK);
			String sql_h = onInsertMinTable("ACC_DOC_H" + datalink,
					headlist.get(0));
			String sql_l = onInsertMinTable("ACC_DOC_L" + datalink,
					bodylist.get(0));
			String sql_c = onInsertMinTable("ACC_DOC_C" + datalink, totalVO);
			String sql_p = "";
			if (bodylist2 != null && bodylist2.size() > 0) {
				String[] bodyPKs2= getBaseDAO().insertVOArrayWithPK(
						bodylist2.toArray(new VoucherPVO[0]));
				Logger.error("插入明细信息2临时表" + bodyPKs2.length+"条");
				sql_p = onInsertMinTable("ACC_DOC_P" + datalink,
						bodylist2.get(0));
			}
			PersistenceManager sessionManager = null;
			JdbcTransaction transation = null;
			String errMsg="";//add by 黄冠华 添加报错信息 20200916
			try {
				sessionManager = PersistenceManager.getInstance();
				JdbcSession session = new JdbcSession();
				transation = new JdbcTransaction(session);
				transation.startTransaction();
				transation.addBatch(sql_h);
				transation.addBatch(sql_l);
				transation.addBatch(sql_c);
				if (!"".equals(sql_p)) {
					transation.addBatch(sql_p);
				}
				transation.executeBatch();
				transation.commitTransaction();
			} catch (DbException e) {
				transation.rollbackTransaction();
				errMsg=e.getMessage();//add by 黄冠华 添加报错信息 20200916
				throw new BusinessException(e.getMessage(), e);
			} finally {
				sessionManager.release();// 需要关闭会话
				//add by 黄冠华 添加报错信息 20200916 begin
				if(StringUtils.isNotEmpty(errMsg)){
					throw new BusinessException(errMsg);
				}
				//add by 黄冠华 添加报错信息 20200916 end
			}
		}

	}

	private List<VoucherPVO> getVoucherPVOs(String org, String year,
			String proid) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select gl_voucher.pk_voucher h_seq ")
				// 凭证主键
				.append(", gl_voucher.num src_doc_num")
				// 凭证号
				.append(",gl_voucher.creationtime CREATION_DATE")
				// 创建时间
				.append(",fip_relation.pk_relation order_seq ")
				// 主键
				.append(",fip_relation.busimessage1 p_code")
				// 影像编码
				.append("  from gl_voucher ")
				.append(" inner join fip_relation on fip_relation.des_relationid = gl_voucher.pk_voucher ")
				.append(" where gl_voucher.year = '" + year + "'")
				.append(" and gl_voucher.adjustperiod = '" + proid + "'")
				.append(" and gl_voucher.pk_org = '" + org + "'")
				.append(" and gl_voucher.dr = 0")
				.append(" and gl_voucher.discardflag <> 'Y'")
				.append(" and gl_voucher.voucherkind <> 255 ")
				.append(" and gl_voucher.pk_manager <> 'N/A' ")
				.append(" and nvl(gl_voucher.errmessage, '~') = '~'")
				.append(" and gl_voucher.tempsaveflag <> 'Y' ")
				.append(" and gl_voucher.voucherkind <> 5")
				.append(" and fip_relation.busimessage1 is not null ")
				.append(" and fip_relation.dr =0")
				.append("  and not exists ( select 1 from bd_billtype t where t.pk_billtypecode =fip_relation.src_billtype and t.systemcode = 'FA')");
		List<VoucherPVO> list = (List<VoucherPVO>) getBaseDAO().executeQuery(
				sql.toString(), new BeanListProcessor(VoucherPVO.class));

		return list;
	}

	/**
	 * 
	 * @param minTable
	 * @param vo
	 * @throws DAOException
	 */
	private String onInsertMinTable(String minTable, SuperVO vo)
			throws DAOException {
		String[] attNames = vo.getAttributeNames();
		String column = "";
		for (int i$ = 0; i$ < attNames.length; i$++) {
			if (minTable.contains("ACC_DOC_L")) {
				if ("assid".equals(attNames[i$])) {
					continue;
				}
			}

			column += attNames[i$];
			if (i$ < (attNames.length - 1)) {
				column += ",";
			}
		}
		String sql = "insert into  " + minTable + "(" + column + ")  select "
				+ column + " from " + vo.getTableName();
		return sql;
	}

	/**
	 * 凭证归档信息
	 * 
	 * @param org
	 * @param year
	 * @param proid
	 * @return
	 * @throws BusinessException
	 */
	private VoucherCVO getTotalVO(String org, String year, String proid)
			throws BusinessException {
		VoucherCVO voucherVO = getVoucherCVO(org, year, proid);
		return voucherVO;
	}

	private VoucherCVO getVoucherCVO(String org, String year, String proid)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select org_orgs.code org_code,v.year,v.period month ")
				// 记账凭证
				.append(", count(v1.pk_voucher)  src_num_mark ")
				.append(",sum(nvl(v1.attachment,0)) attachment_num_mark")
				.append(",min(v1.num) src_start_mark")
				.append(",max(v1.num) src_end_mark")
				.append(",sum(nvl(v1.totalcredit,0)) total_credit_mark")
				.append(",sum(nvl(v1.totaldebit,0)) total_debit_mark")
				// 收
				.append(",count(v2.pk_voucher)  src_num")
				.append(",sum(nvl(v2.attachment,0)) attachment_num")
				.append(",min(v2.num) src_start")
				.append(",max(v2.num) src_end")
				.append(",sum(nvl(v2.totalcredit,0)) total_credit")
				.append(",sum(nvl(v2.totaldebit,0)) total_debit")
				// 付
				.append(",count(v3.pk_voucher)  src_num_pay")
				.append(",sum(nvl(v3.attachment,0)) attachment_num_pay")
				.append(",min(v3.num) src_start_pay")
				.append(",max(v3.num) src_end_pay")
				.append(",sum(nvl(v3.totalcredit,0)) total_credit_pay")
				.append(",sum(nvl(v3.totaldebit,0)) total_debit_pay")
				// 转
				.append(", count(v4.pk_voucher)  src_num_tra")
				.append(",sum(nvl(v4.attachment,0)) attachment_num_tra")
				.append(",min(v4.num) src_start_pay")
				.append(",max(v4.num) src_end_pay")
				.append(",sum(nvl(v4.totalcredit,0)) total_credit_tra")
				.append(",sum(nvl(v4.totaldebit,0)) total_debit_tra")
				.append(" from gl_voucher v ")
				.append(" inner join org_orgs on org_orgs.pk_org = v.pk_org ")
				.append(" left join gl_voucher v1 on v1.pk_voucher = v.pk_voucher and exists  (select 1  from bd_vouchertype where code = '01' and bd_vouchertype.pk_vouchertype = v1.pk_vouchertype) ")
				.append(" left join gl_voucher v2 on v2.pk_voucher = v.pk_voucher and exists  (select 1  from bd_vouchertype where code = '02' and bd_vouchertype.pk_vouchertype = v2.pk_vouchertype) ")
				.append(" left join gl_voucher v3 on v3.pk_voucher = v.pk_voucher and exists  (select 1  from bd_vouchertype where code = '03' and bd_vouchertype.pk_vouchertype = v3.pk_vouchertype) ")
				.append(" left join gl_voucher v4 on v4.pk_voucher = v.pk_voucher and exists  (select 1  from bd_vouchertype where code = '04' and bd_vouchertype.pk_vouchertype = v4.pk_vouchertype) ")
				.append(" where v.year = '" + year + "'")
				.append(" and v.adjustperiod = '" + proid + "'")
				.append(" and v.pk_org = '" + org + "'")
				.append(" and v.dr = 0").append(" and v.discardflag <> 'Y'")
				.append(" and v.voucherkind <> 255 ")
				.append(" and v.pk_manager <> 'N/A' ")
				.append(" and nvl(v.errmessage, '~') = '~'")
				.append(" and v.tempsaveflag <> 'Y' ")
				.append(" and v.voucherkind <> 5")
				.append(" group by org_orgs.code,v.year, v.period");
		VoucherCVO vo = (VoucherCVO) getBaseDAO().executeQuery(sql.toString(),
				new BeanProcessor(VoucherCVO.class));
		return vo;
	}

	/**
	 * 明细信息转换
	 * 
	 * @param bodylist
	 * @return
	 * @throws BusinessException
	 */
	private List<VoucherLVO> onTransDetail(List<VoucherLVO> bodylist)
			throws BusinessException {
		for (VoucherLVO vo : bodylist) {
			if (vo.getAssid() != null && !"~".equals(vo.getAssid())) {
				String assInfo = getAssInfo(vo.getAssid());
				vo.setAcc_name(vo.getAcc_name() + " " + assInfo);
			}
		}
		return bodylist;
	}

	/**
	 * 辅助项信息读取
	 * 
	 * @param assid
	 * @return
	 * @throws BusinessException
	 */
	private String getAssInfo(String assid) throws BusinessException {
		if (assMap.get(assid) != null) {
			return assMap.get(assid);
		}
		String sql = " select bd_accassitem.name, bd_accassitem.refnodename,v.assvalue  from ("
				+ "select  substr(typevalue, 0, 20) asstype, trim(substr(typevalue, 21, length(typevalue))) assvalue "
				+ " from (select f.freevalueid assid, f.typevalue1,f.typevalue2,f.typevalue3,f.typevalue4,f.typevalue5,f.typevalue6,f.typevalue7,f.typevalue8,f.typevalue9 "
				+ "   from gl_freevalue f "
				+ "  where (nvl(dr, 0) = 0) and freevalueid = '"
				+ assid
				+ "') T UNPIVOT(typevalue FOR gl_freevalue IN(typevalue1, typevalue2,typevalue3,typevalue4, typevalue5,typevalue6, typevalue7, typevalue8,typevalue9)) P "
				+ "  where typevalue <> 'NN/A' ) v "
				+ " inner join bd_accassitem on bd_accassitem.pk_accassitem =v. asstype";

		List<Map<String, String>> asslist = (List<Map<String, String>>) getBaseDAO()
				.executeQuery(sql, new MapListProcessor());
		String assMsg = "";
		for (Map<String, String> assInfo : asslist) {
			String assvalue=getAssItemValue(assInfo.get("refnodename"),
					assInfo.get("assvalue"));
			if(!"null".equals(assvalue)&&assvalue!=null&&!"".equals(assvalue)){
				assMsg += "【"
						+ assInfo.get("name")
						+ ":"
						+ assvalue + "】";
			}
		}
		assMap.put(assid, assMsg);
		return assMap.get(assid);
	}

	private String getAssItemValue(String refNodeName, String key) {
		if (key == null || "~".equals(key)) {
			return "~";
		}
		String link = refNodeName + ":" + key;
		if (assItemMap.get(link) != null) {
			return assItemMap.get(link);
		}

		nc.ui.bd.ref.AbstractRefModel refModel = RefPubUtil
				.getRefModel(refNodeName);
		refModel.matchPkData(key);

		String name = refModel.getRefNameValue();
		assItemMap.put(link, name);
		return assItemMap.get(link);
	}

	/**
	 * 凭证信息
	 * 
	 * @param org
	 * @param year
	 * @param proid
	 * @return
	 * @throws BusinessException
	 */
	private List<VoucherHVO> getVoucher(String org, String year, String proid)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct gl_voucher.pk_voucher ORDER_SEQ ")
				// 凭证主键
				.append(",gl_voucher.prepareddate  FILE_DATES")
				// 凭证日期
				.append(",org_orgs.code ORG_CODE")
				// 组织编码
				.append(",org_orgs.name ORG_NAME")
				// 组织名称
				.append(",l.name  acc_book_name")
				// 账簿名称
				.append(", gl_voucher.num SRC_DOC_NUM")
				// 凭证类别
				.append(",bd_vouchertype.name src_type")
				// 凭证号
				.append(", gl_voucher.attachment  ATTACHMENT_NUM")
				// 附单数量
				.append(", gl_voucher.pk_manager input_by")
				// 记账人
				.append(",gl_voucher.pk_checked checked_by")
				// 审核人
				.append(",gl_voucher.pk_casher cashier")
				// 出纳人
				.append(", gl_voucher.creator prepared_by")
				// 制单人
				.append(", gl_voucher.totalcredit TOTAL_CREDIT")
				// 贷方总金额
				.append(",gl_voucher.totaldebit TOTAL_DEBIT")
				// 借方合计
				.append(",gl_voucher.explanation REMARKS")
				// 备注
				.append(",gl_voucher.creationtime CREATION_DATE")
				// 创建时间
				.append(",'" + year + "' year")
				// 创建时间
				.append(",'" + proid + "' month")
				// 创建时间
				.append(",case when nvl(dapsys.systypename,'~')='~' then '总账' else dapsys.systypename end  from_system ")//add by 黄冠华 添加来源系统字段 20201023
				.append("  from gl_voucher ")
				.append(" inner join org_accountingbook l on l.pk_accountingbook = gl_voucher.pk_accountingbook")
				.append(" inner join org_orgs on gl_voucher.pk_org = org_orgs.pk_org")
				.append(" inner join bd_vouchertype on gl_voucher.pk_vouchertype = bd_vouchertype.pk_vouchertype")
				.append(" inner join org_orgs on gl_voucher.pk_org = org_orgs.pk_org")
				.append(" left join fip_relation relation on gl_voucher.pk_voucher=relation.des_relationid ")
				.append(" left join dap_dapsystem dapsys on relation.src_system=dapsys.systypecode  ")
				.append(" where gl_voucher.year = '" + year + "'")
				.append(" and gl_voucher.adjustperiod = '" + proid + "'")
				.append(" and gl_voucher.pk_org = '" + org + "'")
				.append(" and gl_voucher.dr = 0")
				.append(" and gl_voucher.discardflag <> 'Y'")
				.append(" and gl_voucher.voucherkind <> 255 ")
				.append(" and gl_voucher.pk_manager <> 'N/A' ")
				.append(" and nvl(gl_voucher.errmessage, '~') = '~'")
				.append(" and gl_voucher.tempsaveflag <> 'Y' ")
				.append(" and gl_voucher.voucherkind <> 5");
		List<VoucherHVO> list = (List<VoucherHVO>) getBaseDAO().executeQuery(
				sql.toString(), new BeanListProcessor(VoucherHVO.class));
		if (list == null || list.size() == 0) {
			throw new BusinessException("当前期间未能查询凭证信息");
		}

		return list;
	}

	/**
	 * 凭证信息
	 * 
	 * @param headlist
	 * @return
	 * @throws BusinessException
	 */
	List<VoucherHVO> onTransVoucher(List<VoucherHVO> headlist)
			throws BusinessException {
		for (VoucherHVO headVO : headlist) {
			// 记账人工号
			String input_by = headVO.getInput_by();
			headVO.setInput_by(getUserMap(input_by).get("code"));// 记账人工号
			headVO.setInput_by_name(getUserMap(input_by).get("name"));// 记账人姓名
			// 审核人工号
			String checked_by = headVO.getChecked_by();
			headVO.setChecked_by(getUserMap(checked_by).get("code"));// 审核人工号
			headVO.setChecked_by_name(getUserMap(checked_by).get("name"));// 审核人姓名
			// 出纳工号
			String cashier = headVO.getCashier();// 出纳工号
			headVO.setCashier(getUserMap(cashier).get("code"));// 出纳工号
			headVO.setCashier_name(getUserMap(cashier).get("name"));// 出纳姓名
			// 制单人工号
			String prepared_by = headVO.getPrepared_by();
			headVO.setPrepared_by(getUserMap(prepared_by).get("code"));// 制单人工号
			headVO.setPrepared_by_name(getUserMap(prepared_by).get("name"));// 制单人姓名

			headVO.setAttachment_num(null);
			// // 经办人工号
			// String transactor = headVO.getTransactor();
			// headVO.setTransactor(null);// 经办人工号
			// headVO.setTransactor(null);// 经办人姓名

		}

		return headlist;
	}

	/**
	 * 用户信息
	 * 
	 * @param userid
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getUserMap(String userid)
			throws BusinessException {
		if (userMap.get(userid) != null) {
			return userMap.get(userid);
		}
		String sql = "select bd_psndoc.code,bd_psndoc.name from sm_user  "
				+ " inner join bd_psndoc on sm_user.pk_psndoc = bd_psndoc.pk_psndoc "
				+ " where bd_psndoc.dr = 0 and sm_user.cuserid = '" + userid
				+ "' ";
		Map<String, String> value = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		userMap.put(userid, value == null ? new HashMap<String, String>()
				: value);
		return userMap.get(userid);
	}

	/**
	 * 凭证信息
	 * 
	 * @param org
	 * @param year
	 * @param proid
	 * @return
	 * @throws BusinessException
	 */
	private List<VoucherLVO> getVoucherLVOs(String org, String year,
			String proid) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select gl_detail.pk_voucher   H_SEQ")
				// 凭证主键
				.append(",gl_detail.pk_detail    ORDER_SEQ")
				// 凭证明细主键
				.append(",  gl_detail.nov SRC_DOC_NUM")
				// 凭证号
				.append(",  gl_detail.nov     SRC_DOC_NUM")
				// 凭证号
				.append(", gl_detail.explanation  SUMMARY")
				// 摘要
				.append(",bd_accasoa.dispname    ACC_NAME")
				// 会计科目
				.append(",gl_detail.assid")
				// 辅助项ID
				.append(",gl_detail.debitamount 	AMOUNT_OF_DEBIT")
				// 借方金额
				.append(",gl_detail.creditamount  	AMOUNT_OF_CREDIT")
				// 贷方金额
				.append(",gl_voucher.creationtime  	CREATION_DATE")
				//创建时间
				.append(",gl_detail.detailindex  	DETAIL_INDEX")
				//分录索引
				.append(" from gl_detail ")
				.append(" inner join bd_accasoa on bd_accasoa.pk_accasoa = gl_detail.pk_accasoa ")
				.append(" inner join gl_voucher on gl_voucher.pk_voucher = gl_detail.pk_voucher ")
				.append(" where  gl_detail.yearv = '" + year + "'")
				.append(" and gl_detail.adjustperiod = '" + proid + "'")
				.append(" and gl_detail.pk_org = '" + org + "'")
				.append(" and gl_voucher.dr = 0")
				.append(" and gl_voucher.discardflag <> 'Y'")
				.append(" and gl_voucher.voucherkind <> 255 ")
				.append(" and gl_voucher.pk_manager <> 'N/A' ")
				.append(" and nvl(gl_voucher.errmessage, '~') = '~'")
				.append(" and gl_voucher.tempsaveflag <> 'Y' ")
				.append(" and gl_voucher.voucherkind <> 5");
		List<VoucherLVO> list = (List<VoucherLVO>) getBaseDAO().executeQuery(
				sql.toString(), new BeanListProcessor(VoucherLVO.class));

		return list;
	}
}
