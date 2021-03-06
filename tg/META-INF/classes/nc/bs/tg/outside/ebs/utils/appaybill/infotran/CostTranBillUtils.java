package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import java.util.HashMap;
import java.util.Map;

import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.appaybill.PayBillHeaderVO;
import nc.vo.tg.outside.appaybill.PayBillItemVO;

import org.apache.commons.lang.StringUtils;

/**
 * 项目周周建/成本请款
 * 
 * @author ASUS
 * 
 */
public class CostTranBillUtils extends BillTranUtils {
	static CostTranBillUtils utils;

	public static CostTranBillUtils getUtils() {
		if (utils == null) {
			utils = new CostTranBillUtils();
		}
		return utils;
	}

	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			PayBillItemVO bodyvo) throws BusinessException {
		checkItemNotNull(bodyvo);

		itemVO.setAttributeValue(PayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// 所属集团
		itemVO.setAttributeValue(PayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// 单据类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// 交易类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// 交易类型ID
		itemVO.setAttributeValue(PayableBillItemVO.BILLDATE,
				headVO.getBilldate());// 单据日期
		itemVO.setAttributeValue(PayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// 起算日期

		itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
				headVO.getPk_deptid());// 部门
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// 部 门
		itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
				headVO.getPk_psndoc());// 业务员

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, headVO.getObjtype());// 往来对象

		itemVO.setAttributeValue(PayableBillItemVO.DIRECTION,
				BillEnumCollection.Direction.CREDIT.VALUE);// 方向

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayableBillItemVO.PAUSETRANSACT,
				UFBoolean.FALSE);// 挂起标志
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// 购销类型
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// 三角贸易
		itemVO.setAttributeValue(PayableBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// 币种
		itemVO.setAttributeValue(PayableBillItemVO.RATE, headVO.getRate());// 组织本币汇率
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// 收货国

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, bodyvo.getTaxrate());// 税率
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// 扣税类别
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, bodyvo
				.getLocal_money_cr() == null ? UFDouble.ZERO_DBL
				: new UFDouble(bodyvo.getLocal_money_cr()));// 借方原币金额 //
															// 价税合计->付款计划明细金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// 本币金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR, bodyvo
				.getLocal_notax_cr() == null ? UFDouble.ZERO_DBL
				: new UFDouble(bodyvo.getLocal_notax_cr()));// // 组织本币无税金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR, bodyvo
				.getLocal_tax_cr() == null ? UFDouble.ZERO_DBL : new UFDouble(
				bodyvo.getLocal_tax_cr()));// 税额->付款计划明细不含税金额*税率

		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER,
				headVO.getSupplier());// 供应商
		if (StringUtils.isNotBlank(bodyvo.getScomment())) {
			itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
					bodyvo.getScomment());// 摘要
		}
		if (StringUtils.isNotBlank(bodyvo.getSubjcode())) {
			String pk_subjcode = getAccsubjKeyByCode(bodyvo.getSubjcode(),
					headVO.getPk_org());
			if (pk_subjcode == null) {
				throw new BusinessException("会计科目[" + bodyvo.getSubjcode()
						+ "]未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.SUBJCODE, pk_subjcode);// 科目编码
																				// 会计科目
		}
		if (StringUtils.isNotBlank(bodyvo.getCostsubject())) {
			String pk_costsubject = getdefdocBycode(bodyvo.getCostsubject(),
					"zdy024");
			if (pk_costsubject == null) {
				throw new BusinessException("成本科目[" + bodyvo.getCostsubject()
						+ "]未能在NC档案关联到!");
			}
			itemVO.setAttributeValue("def7", pk_costsubject);// 成本科目

		}

		if (StringUtils.isNotBlank(bodyvo.getInvtype())) {
			String pk_costsubject = getdefdocBycode(bodyvo.getInvtype(), "pjlx");
			if (pk_costsubject == null) {
				throw new BusinessException("票种[" + bodyvo.getInvtype()
						+ "]未能在NC档案关联到!");
			}
			itemVO.setAttributeValue("def8", pk_costsubject);
		}
		// 设置业态 def3
		itemVO.setAttributeValue("def3", bodyvo.getFormat());
		// 设置比例 def4
		String formatratio = bodyvo.getFormatratio();
		UFDouble formatratioDouble = new UFDouble(formatratio);
		formatratio = formatratioDouble.multiply(100).toString();
		itemVO.setAttributeValue("def4", formatratio);
		// 财务票据类型
		itemVO.setAttributeValue("def9", "100112100000000069WH");
		// 成本应付单表体def15可抵扣税额，根据发票类型来重新计算-2020-04-24-谈子健
		String deductibleTax = getDeductibleTax(bodyvo);
		itemVO.setAttributeValue("def15", deductibleTax);
		/**
		 * 成本应付单添加结算方式-2020-06-04-谈子健
		 */
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// 结算方式

	}

	/**
	 * 设置主表信息
	 * 
	 * @param parentVO
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void setHeaderVO(PayableBillVO hvo, PayBillHeaderVO headvo)
			throws BusinessException {

		checkHeaderNotNull(headvo);
		// bpmid
		hvo.setAttributeValue("bpmid", headvo.getBpmid());
		/*
		 * String pk_balatype = getBalatypePkByCode(headvo.getBalatype()); if
		 * (pk_balatype == null) { throw new BusinessException("结算方式【" +
		 * headvo.getBalatype() + "】未能在NC档案查询到相关信息!"); }
		 * hvo.setAttributeValue(PayableBillVO.PK_BALATYPE, pk_balatype);// 结算方式
		 */
		hvo.setAttributeValue("def1", headvo.getEbsid());// 自定义项1 外系统主键
		hvo.setAttributeValue("def2", headvo.getEbsbillno());// 自定义项2
																// 外系统单据号->付款申请号
		hvo.setAttributeValue("def3", headvo.getImgno());// 自定义项3 影像编码->影像编码
		hvo.setAttributeValue("def4", headvo.getImgstate());// 自定义项4 影像状态->影像状态
		hvo.setAttributeValue("def5", headvo.getContcode());// 自定义项5 合同编码->合同编码
		hvo.setAttributeValue("def6", headvo.getContname());// 合同名称->合同名称 def6
		hvo.setAttributeValue("def7", headvo.getConttype());// 自定义项7
		/**
		 * 成本应付单接口，自动根据合同类型带出应付单表头“财务票据类型”（NC字段-def56）,参照档案zdy008-2020-05-08-谈子健
		 * -start
		 */
		String conttype = headvo.getConttype();
		hvo.setAttributeValue("def56", getNoteType(conttype));// 自定义项8
		/**
		 * 成本应付单接口，自动根据合同类型带出应付单表头“财务票据类型”（NC字段-def56）,参照档案zdy008-2020-05-08-谈子健
		 * -end
		 */
		// 合同细类->合同细类
		// 合同类型->合同类型
		hvo.setAttributeValue("def8", headvo.getContcell());// 自定义项8 合同细类->合同细类
		// 紧急程度->紧急程度2020-02-18-tzj
		String emergency = headvo.getEmergency();
		String def9 = getEmergency(emergency);
		hvo.setAttributeValue("def9", def9);// 自定义项9
		hvo.setAttributeValue("def10", "EBS");// 自定义项10 来源外部系统
		// hvo.setAttributeValue("def11", headvo.getMny_actual());// 自定义项11 请款金额
		// hvo.setAttributeValue("def14", headvo.getMny_abs());// 自定义项14 abs支付金额

		if (StringUtils.isNotBlank(headvo.getPlate())) {
			String pk_plate = getdefdocBycode(headvo.getPlate(), "bkxx");
			if (pk_plate == null) {
				throw new BusinessException("板块【" + headvo.getPlate()
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def15", pk_plate);// 自定义项15 板块
		}

		if (StringUtils.isNotBlank(headvo.getAccorg())) {
			String pk_accorg = getPk_orgByCode(headvo.getAccorg());
			if (pk_accorg == null) {
				throw new BusinessException("出账公司【" + headvo.getAccorg()
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def17", pk_accorg);// 自定义项17
			// 出账公司->NC业务单元编码
		}
		// 新增供应商2019-11-01-tzj
		String pk_supplier = getSupplierIDByCode(headvo.getSupplier(),
				hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("供应商【" + headvo.getSupplier()
					+ "】未能在NC档案查询到相关信息");
		}
		hvo.setAttributeValue("supplier", pk_supplier);// 供应商

		// hvo.setPk_busitype(null);//交易类型
		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// 往来对象

		if (StringUtils.isNotBlank(headvo.getProejctdata())) {
			String pk_projectdata = getpk_projectByCode(headvo.getProejctdata());
			if (pk_projectdata == null) {
				throw new BusinessException("项目【" + headvo.getProejctdata()
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def19", pk_projectdata);// 项目
			// 应付单由传入项目信息带出是否资本(def8),项目资质(def9)
			HashMap<String, Object> projectData = getProjectDataByCode(headvo
					.getProejctdata());
			if (projectData != null) {
				hvo.setAttributeValue("def61", projectData.get("def8"));// 是否资本
				hvo.setAttributeValue("def62", projectData.get("def9"));// 项目性质
			}
		}

		if (StringUtils.isNotBlank(headvo.getProejctstages())) {
			String pk_proejctstages = getpk_projectByCode(headvo
					.getProejctstages());
			if (pk_proejctstages == null) {
				throw new BusinessException("项目分期 【"
						+ headvo.getProejctstages() + "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def32", pk_proejctstages);// 自定义项32 项目分期
		}
		hvo.setAttributeValue("money", headvo.getBillamount());// 本次票据金额
		// hvo.setAttributeValue("def21", headvo.getTotalmny_request());//
		// 累计请款金额
		// hvo.setAttributeValue("def22", headvo.getTotalmny_pay());// 累计付款金额
		// hvo.setAttributeValue("def23", headvo.getIsshotgun());// 是否先付款后补票
		// hvo.setAttributeValue("def24", headvo.getTotalmny_paybythisreq());//
		// 本次请款累计付款金额
		hvo.setAttributeValue("def26", headvo.getTotalmny_inv());// 累计发票金额
		// hvo.setAttributeValue("def28", headvo.getIsdeduction());// 是否有质保金扣除
		hvo.setAttributeValue("def31", headvo.getNote());// 说明
		// hvo.setAttributeValue("def33", headvo.getAuditstate());
		// 地区财务审批状态
		if (StringUtils.isNotBlank(headvo.getAuditstate())) {
			String pk_auditstate = getAuditstateByCode(headvo.getAuditstate());
			if (pk_auditstate == null) {
				throw new BusinessException("地区财务审批状态 【"
						+ headvo.getAuditstate() + "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def33", pk_auditstate);// 自定义项32 项目分期
		}
		hvo.setAttributeValue("def43", headvo.getDef43());// 非标准指定付款涵def43
		hvo.setAttributeValue("def44", headvo.getDef44());// 补附件def44
		hvo.setAttributeValue("def45", headvo.getDef45());// 已补全def45
		hvo.setAttributeValue("def46", headvo.getDept());// 经办部门
		hvo.setAttributeValue("def47", headvo.getPsndoc());// 经办人
		hvo.setAttributeValue("def50", "N");// 票据权责发生制
		String def55 = headvo.getDef55();
		if (def55 != null && !"".equals(def55)) {
			hvo.setAttributeValue("def55", def55);// EBS请款方式-def55
		} else {
			throw new BusinessException("EBS请款方式不能为空!");
		}

		if (StringUtils.isNotBlank(headvo.getSignorg())) {
			String pk_signorg = getPk_orgByCode(headvo.getSignorg());
			if (pk_signorg == null) {
				throw new BusinessException("签约公司【" + headvo.getSignorg()
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def34", pk_signorg);// 签约公司
		}
		/**
		 * 成本应付单添加结算方式-2020-05-26-谈子健
		 */
		String pk_balatype = null;
		pk_balatype = getBalatypePkByCode(headvo.getBalatype());
//		if (pk_balatype == null) {
//			throw new BusinessException("结算方式【" + headvo.getBalatype()
//					+ "】未能在NC档案查询到相关信息!");
//		}
		hvo.setAttributeValue("pk_balatype", pk_balatype);// 结算方式

	}

	/**
	 * 检验表体必录信息
	 * 
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void checkItemNotNull(PayBillItemVO bodyvo)
			throws BusinessException {

		// if (StringUtils.isBlank(bodyvo.getSubjcode())) {
		// throw new BusinessException("会计科目不可为空");
		// }
		// if (bodyvo.getInoutbusiclass() == null) {
		// throw new BusinessException("收支项目不可为空");
		// }
		if (StringUtils.isBlank(bodyvo.getTaxrate())) {
			throw new BusinessException("税率不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_notax_cr())) {
			throw new BusinessException("无税金额不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_tax_cr())) {
			throw new BusinessException("税额不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_money_cr())) {
			throw new BusinessException("价税合计不可为空");
		}
		// if (bodyvo.getPaymenttype() == null) {
		// throw new BusinessException("款项类型不可为空");
		// }
		// if (StringUtils.isBlank(bodyvo.getFormat())) {
		// throw new BusinessException("业态不可为空");
		// }
		// if (StringUtils.isBlank(bodyvo.getFormatratio())) {
		// throw new BusinessException("比例不可为空");
		// }
		// if (StringUtils.isBlank(bodyvo.getRecaccount())) {
		// throw new BusinessException("收款方开户行行号不可为空");
		// }
		if (StringUtils.isBlank(bodyvo.getCostsubject())) {
			throw new BusinessException("成本科目不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getInvtype())) {
			throw new BusinessException("票据类型(票种)不可为空");
		}
		/*
		 * if (StringUtils.isBlank(bodyvo.getScomment())) { throw new
		 * BusinessException("摘要不可为空"); }
		 */
		// if (StringUtils.isBlank(bodyvo.getSupplier())) {
		// throw new BusinessException("供应商不可为空");
		// }
		// if (StringUtils.isBlank(bodyvo.getBudgetsub())) {
		// throw new BusinessException("预算科目不可为空");
		// }

	}

	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(PayBillHeaderVO headvo)
			throws BusinessException {
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("财务组织不可为空");
		}
		// if (StringUtils.isBlank(headvo.getDept())) {
		// throw new BusinessException("经办部门不可为空");
		// }
		// if (StringUtils.isBlank(headvo.getPsndoc())) {
		// throw new BusinessException("经办人不可为空");
		// }
		if (StringUtils.isBlank(headvo.getBillamount())) {
			throw new BusinessException("本次票据金额不可为空");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getBalatype())) { throw new
		 * BusinessException("结算方式不可为空"); }
		 */
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("EBS主键不可为空");
		}
		if (StringUtils.isBlank(headvo.getEbsbillno())) {
			throw new BusinessException("EBS单据号不可为空");
		}
		// if (StringUtils.isBlank(headvo.getImgno())) {
		// throw new BusinessException("影像编码不可为空");
		// }
		// if (StringUtils.isBlank(headvo.getImgstate())) {
		// throw new BusinessException("影像状态不可为空");
		// }
		if (StringUtils.isBlank(headvo.getContcode())) {
			throw new BusinessException("合同编码不可为空");
		}
		if (StringUtils.isBlank(headvo.getContname())) {
			throw new BusinessException("合同名称不可为空");
		}
		if (StringUtils.isBlank(headvo.getConttype())) {
			throw new BusinessException("合同类型不可为空");
		}
		// if (StringUtils.isBlank(headvo.getContcell())) {
		// throw new BusinessException("合同细类不可为空");
		// }
		// // if (StringUtils.isBlank(headvo.getEmergency())) {
		// throw new BusinessException("紧急程度不可为空");
		// }
		// if (StringUtils.isBlank(headvo.getMny_actual())) {
		// throw new BusinessException("请款金额/申请金额不可为空");
		// }
		/*
		 * if (StringUtils.isBlank(headvo.getMny_abs())) { throw new
		 * BusinessException("abs支付金额不可为空"); }
		 */
		if (StringUtils.isBlank(headvo.getPlate())) {
			throw new BusinessException("板块不可为空");
		}
		// if (StringUtils.isBlank(headvo.getAccorg())) {
		// throw new BusinessException("出账公司不可为空");
		// }
		if (StringUtils.isBlank(headvo.getSupplier())) {
			throw new BusinessException("供应商不可为空");
		}
		if (headvo.getProejctdata() == null) {
			throw new BusinessException("项目不可为空");
		}
		if (headvo.getProejctstages() == null) {
			throw new BusinessException("项目分期不可为空");
		}
		// if (StringUtils.isBlank(headvo.getTotalmny_request())) {
		// throw new BusinessException("累计请款金额不可为空");
		// }
		// if (StringUtils.isBlank(headvo.getTotalmny_pay())) {
		// throw new BusinessException("累计付款金额不可为空");
		// }
		/*
		 * if (StringUtils.isBlank(headvo.getIsshotgun())) { throw new
		 * BusinessException("是否先付款后补票不可为空"); }
		 */
		// if (StringUtils.isBlank(headvo.getTotalmny_paybythisreq())) {
		// throw new BusinessException("本次请款累计付款金额不可为空");
		// }
		if (StringUtils.isBlank(headvo.getTotalmny_inv())) {
			throw new BusinessException("累计发票金额不可为空");
		}
		// if (StringUtils.isBlank(headvo.getIsdeduction())) {
		// throw new BusinessException("是否有质保金扣除不可为空");
		// }
		if (StringUtils.isBlank(headvo.getAuditstate())) {
			throw new BusinessException("地区财务审批状态不可为空");
		}
		if (StringUtils.isBlank(headvo.getBilldate())) {
			throw new BusinessException("单据日期不可为空");
		}
		// if (StringUtils.isBlank(headvo.getSignorg())) {
		// throw new BusinessException("签约公司不可为空");
		// }

	}

	/**
	 * 根据成本科目PK获取收支科目和会计科目PK
	 * 
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getPk_subcode(String pk_costsubject)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT distinct f.factorvalue1 as pk_subjcode,f.factorvalue2 as subjcode  ");
		query.append("  FROM fip_docview_b f  ");
		query.append(" WHERE NVL(dr, 0) = 0  ");
		query.append("   AND factorvalue1 = '" + pk_costsubject + "'  ");
		query.append("   AND pk_classview = (SELECT pk_classview  ");
		query.append("                         FROM fip_docview  ");
		query.append("                        WHERE viewcode = 'CB03'  ");
		query.append("                          AND NVL(dr, 0) = 0)  ");

		Map<String, Object> pks = (Map<String, Object>) getBaseDAO()
				.executeQuery(query.toString(), new MapProcessor());
		return pks;
	}

	/**
	 * 根据emergency编码获取紧急程度主键2020-02-18-谈子健
	 * 
	 * @throws BusinessException
	 */
	private String getEmergency(String emergency) throws BusinessException {
		String pk_defdoc = "";
		StringBuffer query = new StringBuffer();
		query.append("select d.pk_defdoc  ");
		query.append("  from bd_defdoclist l  ");
		query.append("  left join bd_defdoc d  ");
		query.append("    on l.pk_defdoclist = d.pk_defdoclist  ");
		query.append(" where d.code = '" + emergency + "'  ");
		query.append("   and l.code = 'zdy031'  ");
		query.append("   and d.enablestate = 2  ");
		query.append("   and l.dr = 0  ");
		query.append("   and d.dr = 0  ");

		pk_defdoc = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_defdoc;

	}

	/**
	 * 成本应付单表体def15可抵扣税额，根据发票类型来重新计算-2020-04-24-谈子健
	 * 
	 * @param bodyvo
	 * 
	 * @param ocal_tax_cr
	 *            ()
	 * @throws BusinessException
	 */
	private String getDeductibleTax(PayBillItemVO bodyvo)
			throws BusinessException {
		String deductibleTax = "0";
		String invtype = bodyvo.getInvtype();
		String local_tax_cr = bodyvo.getLocal_tax_cr();
		if ("01".equals(invtype) || "08".equals(invtype)
				|| "10".equals(invtype) || "11".equals(invtype)
				|| "15".equals(invtype) || "18".equals(invtype)
				|| "19".equals(invtype)) {
			deductibleTax = local_tax_cr;
		}

		if ("04".equals(invtype) || "05".equals(invtype)
				|| "07".equals(invtype) || "16".equals(invtype)
				|| "17".equals(invtype)) {
			StringBuffer query = new StringBuffer();
			query.append("select d.memo  ");
			query.append("  from bd_defdoclist c, bd_defdoc d  ");
			query.append(" where c.pk_defdoclist = d.pk_defdoclist  ");
			query.append("   and c.code = 'pjlx'  ");
			query.append("   and d.code = '" + invtype + "'  ");
			query.append("   and nvl(c.dr, 0) = 0  ");
			query.append("   and nvl(d.dr, 0) = 0  ");
			query.append("   and d.enablestate = '2'  ");
			String calculation = (String) getBaseDAO().executeQuery(
					query.toString(), new ColumnProcessor());
			String amount = bodyvo.getLocal_money_cr();
			String sqlstr = calculation.toString().replace("amount", amount);
			String sql = "select " + sqlstr + " from dual ";
			Object Tax = getBaseDAO().executeQuery(sql, new ColumnProcessor());
			deductibleTax = Tax.toString();
		}

		return deductibleTax;
	}
}
