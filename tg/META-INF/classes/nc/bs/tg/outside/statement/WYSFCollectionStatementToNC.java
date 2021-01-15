package nc.bs.tg.outside.statement;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.cmp.bill.RecBillAggVO;
import nc.vo.cmp.bill.RecBillDetailVO;
import nc.vo.cmp.bill.RecBillVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.LLCollectionStatementJsonBVO;
import nc.vo.tg.outside.LLCollectionStatementJsonVO;

public class WYSFCollectionStatementToNC extends CollectionStatementUtils {

	protected RecBillAggVO onTranBill(LLCollectionStatementJsonVO headVO,
			List<LLCollectionStatementJsonBVO> bodyVOs)
			throws BusinessException {
		nc.vo.cmp.bill.RecBillAggVO aggVO = new RecBillAggVO();
		RecBillVO save_headVO = new RecBillVO();
		/**
		 * 物业收费系统传来的字段-2020-11-23-谈子健-start
		 */
		OrgVO orgvo = getOrgVO(headVO.getPk_org());
		if (orgvo == null || "".equals(orgvo)) {
			throw new BusinessException("财务组织pk_org:" + headVO.getPk_org()
					+ "在NC找不到关联的数据!");
		}
		save_headVO.setPk_org(orgvo.getPk_org());// 财务组织
		save_headVO.setPk_org_v(orgvo.getPk_vid());// 财务组织版本
		save_headVO.setBill_date(new UFDate(headVO.getBilldate()));// 单据日期
		save_headVO.setPk_tradetypeid(getPk_tradetype(headVO
				.getPk_tradetypeid()));// 交易类型id
		save_headVO.setTrade_type(headVO.getPk_tradetypeid());// 交易类型
		save_headVO.setObjecttype(0); // 交易对象类型
		// save_headVO.setLocal_money(new UFDouble(headVO.getLocal_money()));//
		// 合计金额
		// save_headVO.setPrimal_money(new UFDouble(headVO.getLocal_money()));//
		// 合计金额
		save_headVO.setContractno(headVO.getContractno());// 合同号
		save_headVO.setMail(headVO.getMailbox());// 经办人邮箱号
		save_headVO.setDef4(headVO.getImgcode());// 影像编码
		save_headVO.setDef1(headVO.getSrcid());// 外系统主键
		save_headVO.setDef2(headVO.getSrcbillno());// 外系统单号
		save_headVO.setPk_busiflow(getBusitypeByCode("cmp401",
				"000112100000000005FD"));// 业务流程

		/**
		 * 物业收费系统传来的字段-2020-11-23-谈子健-end
		 */
		save_headVO.setSource_flag("2");
		save_headVO.setBillmaker_date(new UFDate());
		save_headVO.setCreationtime(new UFDateTime());
		save_headVO.setBill_status(-1);// 单据状态
		save_headVO.setEffect_flag(0);// 生效状态
		save_headVO.setPk_group("000112100000000005FD");// 所属集团
		save_headVO.setBillclass("sj");
		save_headVO.setBillmaker(getUserIDByCode(WYSFDefaultOperator));
		save_headVO.setCreator(getUserIDByCode(WYSFDefaultOperator));
		save_headVO.setBill_type("F4");
		save_headVO.setPk_billtypeid(getPk_billtypeid("F4"));
		save_headVO.setStatus(VOStatus.NEW);

		List<RecBillDetailVO> bodylist = new ArrayList<>();
		for (LLCollectionStatementJsonBVO bodyVO : bodyVOs) {
			RecBillDetailVO save_bodyVO = new RecBillDetailVO();
			/**
			 * 物业收费系统传来的字段表体信息-2020-11-23-谈子健-start
			 * nc.vo.cmp.bill.RecBillDetailVO
			 */
			save_bodyVO.setMemo(bodyVO.getMemo());
			String customer = getCustomerByCode(bodyVO.getPk_customer());
			save_bodyVO.setPk_customer(customer);
			save_bodyVO.setPk_balatype(getBalatypePkByCode(bodyVO
					.getPk_balatype()));// 结算方式
			String accountId = getAccountIDByCode(orgvo.getPk_org(),
					bodyVO.getAccnum());
			if (accountId == null || "".equals(accountId)) {
				throw new BusinessException("在供应商pk_org【" + headVO.getPk_org()
						+ "】下不存在银行账号:" + bodyVO.getAccnum() + "请检查数据!");
			}
			save_bodyVO.setPk_account(accountId);

			save_bodyVO.setDef16(bodyVO.getTaxrate()); // 税率
			save_bodyVO.setDef17(bodyVO.getLocal_tax_cr()); // 税额
			save_bodyVO.setDef18(bodyVO.getNotax_cr()); // 无税金额
			save_bodyVO.setRec_local(new UFDouble(bodyVO.getRec_local())); // 收款组织本币金额
			save_bodyVO.setRec_primal(new UFDouble(bodyVO.getRec_local())); // 收款原币金额
			save_bodyVO.setPk_jobid(getProject(bodyVO.getProject()));// 项目
			save_bodyVO
					.setDef3(getdefdocBycode(bodyVO.getItemtype(), "SDLL008"));// 收费项目类型
			save_bodyVO
					.setDef4(getItemnameByPk(bodyVO.getItemname(), "SDLL008"));// 收费项目名称
			save_bodyVO.setDef5(getdefdocBycode(bodyVO.getProjectphase(),
					"SDLL006"));// 项目阶段
			save_bodyVO.setDef6(getdefdocBycode(bodyVO.getProperties(),
					"SDLL007"));// 项目属性
			save_bodyVO.setDef7(getdefdocBycode(bodyVO.getPaymenttype(),
					"SDLL010"));// 款项类型

			String propertycode = bodyVO.getPropertycode(); // 房产编码
			String propertyname = bodyVO.getPropertyname();// 房产名称

			if (propertycode != null && !"".equals(propertycode)
					&& propertyname != null && !"".equals(propertyname)) {
				String pk_defdoc = saveHousePropertyArchives(propertycode,
						propertyname, orgvo.getPk_org());
				if (pk_defdoc != null && !"".equals(pk_defdoc)) {
					save_bodyVO.setDef8(propertycode); // 房产编码
					save_bodyVO.setDef9(pk_defdoc);// 房产名称
				}

			}
			save_bodyVO.setDef10(bodyVO.getSubordinateyear());// 应收单所属年月
			save_bodyVO
					.setDef11(getdefdocBycode(bodyVO.getArrears(), "SDLL002"));// 【是否往年欠费】
			save_bodyVO.setDef12(bodyVO.getRowid());// 物业收费系统单据行ID

			String subordiperiod = bodyVO.getSubordiperiod();// 应收归属期
			if (null != subordiperiod && !"".equals(subordiperiod)
					&& subordiperiod.contains("-")) {
				// 如果和表体单据日期不一样的取月份的第一天
				int year = Integer.parseInt(subordiperiod.substring(0,
						subordiperiod.indexOf("-")));// 截取年份
				int month = Integer
						.parseInt(subordiperiod.substring(
								subordiperiod.indexOf("-") + 1,
								subordiperiod.length()));// 截取月份
				subordiperiod = getFirstDayOfMonth1(year, month);
				save_bodyVO.setDef13(subordiperiod);// 应收归属期
			} else {
				throw new BusinessException(
						"请检查单据应收归属期subordiperiod字段是否为空或格式是否正确!");
			}

			String actualnateyear = bodyVO.getActualnateyear();// 实收所属年月-入账
			// 如果和表体单据日期一样的取单据日期
			if (actualnateyear != null && !"".equals(actualnateyear)
					&& actualnateyear.contains("-")) {
				if (headVO.getBilldate() != null
						&& headVO.getBilldate().startsWith(actualnateyear)) {
					save_bodyVO.setDef14(headVO.getBilldate());// 实收所属年月
				} else {
					// 如果和表体单据日期不一样的取月份的第一天
					int year = Integer.parseInt(actualnateyear.substring(0,
							actualnateyear.indexOf("-")));// 截取年份
					int month = Integer.parseInt(actualnateyear.substring(
							actualnateyear.indexOf("-") + 1,
							actualnateyear.length()));// 截取月份
					actualnateyear = getFirstDayOfMonth1(year, month);
					save_bodyVO.setDef14(actualnateyear);// 实收所属年月
				}
			} else {
				throw new BusinessException(
						"请检查单据实收所属年月actualnateyear字段是否为空或格式是否正确!");
			}
			// 实收归属期
			String actualperiod = bodyVO.getActualperiod();// 应收归属期
			if (null != actualperiod && !"".equals(actualperiod)
					&& actualperiod.contains("-")) {
				// 如果和表体单据日期不一样的取月份的第一天
				int year = Integer.parseInt(actualperiod.substring(0,
						actualperiod.indexOf("-")));// 截取年份
				int month = Integer.parseInt(actualperiod.substring(
						actualperiod.indexOf("-") + 1, actualperiod.length()));// 截取月份
				actualperiod = getFirstDayOfMonth1(year, month);
				save_bodyVO.setDef15(actualperiod);// 实收归属期

			} else {
				throw new BusinessException(
						"请检查单据应收归属期actualperiod字段是否为空或格式是否正确!");
			}

			/**
			 * 物业收费系统传来的字段表体信息-2020-11-23-谈子健-end nc.vo.cmp.bill.RecBillDetailVO
			 */
			save_bodyVO.setCreationtime(new UFDateTime());
			save_bodyVO.setBill_date(new UFDate(headVO.getBilldate()));
			save_bodyVO.setObjecttype(0);
			save_bodyVO.setPk_group("000112100000000005FD");// 所属集团
			save_bodyVO.setDirection(1);
			save_bodyVO.setPk_currtype("1002Z0100000000001K1");
			save_bodyVO.setPk_org(orgvo.getPk_org());
			save_bodyVO.setPk_org_v(orgvo.getPk_vid());
			save_bodyVO.setBill_type("F4");
			save_bodyVO.setStatus(VOStatus.NEW);
			save_bodyVO.setBillclass("sj");
			bodylist.add(save_bodyVO);
		}
		if (bodylist.size() > 0) {
			save_headVO.setDef3(bodylist.get(0).getDef14());
		}
		aggVO.setParentVO(save_headVO);
		aggVO.setChildrenVO(bodylist.toArray(new RecBillDetailVO[0]));

		return aggVO;
	}

	/**
	 * 获取交易类型主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getPk_tradetype(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		String pk_tradetypeid = null;
		pk_tradetypeid = (String) dao.executeQuery(
				"select pk_billtypeid from bd_billtype where pk_billtypecode='"
						+ code + "' and nvl(dr,0)=0", new ColumnProcessor());
		if (pk_tradetypeid == null || "".equals(pk_tradetypeid)) {
			throw new BusinessException("单据交易类型pk_tradetypeid【" + code
					+ "】在NC不存在请检查!");
		}

		return pk_tradetypeid;
	}
}
