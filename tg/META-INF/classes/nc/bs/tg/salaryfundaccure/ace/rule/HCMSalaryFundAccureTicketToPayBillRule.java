package nc.bs.tg.salaryfundaccure.ace.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.internal.win32.UDACCEL;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.action.SendVoucherUtil;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.tg.salaryfundaccure.SalaryFundAccureVO;

public class HCMSalaryFundAccureTicketToPayBillRule implements
		IRule<AggSalaryfundaccure> {
	private static final String HCMSalaryOperatorName = "RLZY";// HCM默认操作员
	private IArapBillPubQueryService arapBillPubQueryService = null;
	private IPFBusiAction pfBusiAction = null;
	private BaseDAO baseDAO = null;

	@Override
	public void process(AggSalaryfundaccure[] vos) {
		for (AggSalaryfundaccure vo : vos) {
			SalaryFundAccureVO ticketHead = vo.getParentVO();
			CircularlyAccessibleValueObject[] ticketBody = vo.getChildrenVO();
			String billno = (String) ticketHead.getAttributeValue("def2");
			try {
				if (!"1".equals(String.valueOf(ticketHead
						.getAttributeValue("approvestatus")))) {
					continue;
				}
				AggPayBillVO aggvo = onTranBill(ticketHead, ticketBody);
				HashMap eParam = new HashMap<>();
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
						PfUtilBaseTools.PARAM_NOTE_CHECKED);
				String jjpkOrg = getPk_orgByCode("GZ1-9001");
				String pkOrg = (String) ticketHead.getAttributeValue("pk_org");
				String bodyDef18 = (String) ticketBody[0]
						.getAttributeValue("def18");
				if (!(pkOrg.equals(bodyDef18) && pkOrg.equals(jjpkOrg) && ticketBody.length == 1)) {
					if(aggvo.getChildrenVO()!=null&&aggvo.getChildrenVO().length!=0){
						AggPayBillVO[] obj = (AggPayBillVO[]) getPfBusiAction()
								.processAction("SAVE", "F3", null, aggvo, null,
										eParam);
						AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
						WorkflownoteVO worknoteVO = NCLocator.getInstance()
								.lookup(IWorkflowMachine.class)
								.checkWorkFlow("SAVE", "F3", billvos[0], eParam);
						obj = (AggPayBillVO[]) getPfBusiAction().processAction(
								"SAVE", "F3", worknoteVO, billvos[0], null, eParam);
						String payBillID = (String) obj[0].getParentVO()
								.getAttributeValue("pk_paybill");
						ticketHead.setAttributeValue("def3", payBillID);
						IUifService iUifService = NCLocator.getInstance().lookup(
								IUifService.class);
						iUifService.update(vos[0].getParentVO());
					}
				}
				SendVoucherUtil util = new SendVoucherUtil();
				util.addVoucher(vo);

			} catch (Exception e) {
				throw new BusinessRuntimeException("【" + billno + "】,"
						+ e.getMessage(), e);
			}
		}

	}

	public AggPayBillVO onTranBill(SalaryFundAccureVO ticketHead,
			CircularlyAccessibleValueObject[] ticketBody)
			throws BusinessException {
		AggPayBillVO aggvo = new AggPayBillVO();
		PayBillVO save_headVO = new PayBillVO();
		String pk_org = (String) ticketHead.getAttributeValue("pk_org");
		save_headVO.setPk_org(pk_org);// 合同签订公司=财务组织
		save_headVO.setDef1((String) ticketHead.getAttributeValue("def1"));// 外系统主键
		save_headVO.setDef2((String) ticketHead.getAttributeValue("def2"));// 外系统单号
		save_headVO.setBilldate(new UFDate(ticketHead.getAttributeValue(
				"billdate").toString()));// 扣款日期
		save_headVO.setDef67((String) ticketHead.getAttributeValue("def67"));// 工资所属月

		// save_headVO.setDef56((String)ticketHead.getAttributeValue("def5"));//
		// 是否资本化
		// save_headVO.setDef57((String)ticketHead.getAttributeValue("def4"));//
		// 公司性质

		String pk_vid = getvidByorg(save_headVO.getPk_org());
		save_headVO.setPk_fiorg(save_headVO.getPk_org());
		save_headVO.setPk_fiorg_v(pk_vid);
		save_headVO.setSett_org(save_headVO.getPk_org());
		save_headVO.setSett_org_v(pk_vid);
		save_headVO.setCreationtime(new UFDateTime());
		save_headVO.setObjtype(1);
		save_headVO.setBillclass("fk");
		save_headVO.setApprovestatus(-1);
		save_headVO.setPk_tradetype("F3-Cxx-033");
		save_headVO.setPk_billtype("F3");// 单据类型编码
		// save_headVO.setBilldate(new UFDate());// 单据日期
		save_headVO.setBusidate(new UFDate());//
		save_headVO.setSyscode(1);// 单据所属系统，默认为1，1=应付系统
		save_headVO.setSrc_syscode(1);// 单据来源系统
		save_headVO.setPk_currtype("1002Z0100000000001K1");// 币种
		save_headVO.setBillstatus(2);// 单据状态,默认为2审批中
		save_headVO.setPk_group("000112100000000005FD");// 所属集团，默认为时代集团
		save_headVO.setBillmaker(getUserIDByCode(HCMSalaryOperatorName));// 制单人
		save_headVO.setCreator(getUserIDByCode(HCMSalaryOperatorName));// 创建人
		save_headVO.setObjtype(1); // 往来对象
		// 2=部门，3=业务员，1=供应商，0=客户
		save_headVO.setIsinit(UFBoolean.FALSE);// 期初标志
		save_headVO.setIsreded(UFBoolean.FALSE);// 是否红冲过
		save_headVO.setStatus(VOStatus.NEW);

		List<PayBillItemVO> bodylist = new ArrayList<>();
		List<PayBillItemVO> newbodylist = new ArrayList<>();// 合同签订和工资发放一致时
		for (CircularlyAccessibleValueObject hcmTicketBody : ticketBody) {
			PayBillItemVO save_bodyVO = new PayBillItemVO();
			save_bodyVO.setPk_org(save_headVO.getPk_org());// 应收财务组织
			save_bodyVO.setSupplier(getSupplierIDByPkOrg((String) hcmTicketBody
					.getAttributeValue("def18")));// 供应商=工资发放公司
			save_bodyVO.setDef18(getSupplierIDByPkOrg((String) hcmTicketBody
					.getAttributeValue("def18")));// 供应商=工资发放公司
			save_bodyVO.setDef30((String) hcmTicketBody
					.getAttributeValue("def30"));// 部门属性
			save_bodyVO.setDef22((String) hcmTicketBody
					.getAttributeValue("def22"));// 人数
			save_bodyVO.setDef29((String) hcmTicketBody
					.getAttributeValue("def29"));// 代发奖金-营销
			save_bodyVO.setDef27((String) hcmTicketBody
					.getAttributeValue("def27"));// 代发奖金-拓展
			save_bodyVO.setDef25((String) hcmTicketBody
					.getAttributeValue("def25"));// 代发奖金-其他
			save_bodyVO.setDef24((String) hcmTicketBody
					.getAttributeValue("def24"));// 佣金
			save_bodyVO.setDef23((String) hcmTicketBody
					.getAttributeValue("def23"));// 其他税前扣款-社保公司部分
			save_bodyVO.setDef20((String) hcmTicketBody
					.getAttributeValue("def20"));// 其他税前扣款-社保个人部分
			save_bodyVO.setDef19((String) hcmTicketBody
					.getAttributeValue("def19"));// 其他税前扣款-公积金公司部分
			save_bodyVO.setDef17((String) hcmTicketBody
					.getAttributeValue("def17"));// 其他税前扣款-公积金个人部分
			save_bodyVO.setDef6((String) hcmTicketBody
					.getAttributeValue("def6"));// 应发合计

			save_bodyVO.setDef31((String) hcmTicketBody
					.getAttributeValue("def31"));// 应扣基金
			save_bodyVO.setDef32((String) hcmTicketBody
					.getAttributeValue("def32"));// 慈善捐款
			save_bodyVO.setDef33((String) hcmTicketBody
					.getAttributeValue("def33"));// 其他税后扣款-其他
			save_bodyVO.setDef45((String) hcmTicketBody
					.getAttributeValue("def45"));// 其他税后扣款-营销
			save_bodyVO.setDef46((String) hcmTicketBody
					.getAttributeValue("def46"));// 其他税后扣款-社保公司部分
			save_bodyVO.setDef47((String) hcmTicketBody
					.getAttributeValue("def47"));// 其他税前扣款-社保个人部分
			save_bodyVO.setDef21((String) hcmTicketBody
					.getAttributeValue("def21"));// 其他税后扣款-公积金公司部分
			save_bodyVO.setDef15((String) hcmTicketBody
					.getAttributeValue("def15"));// 其他税后扣款-公积金个人部分
			save_bodyVO.setDef14((String) hcmTicketBody
					.getAttributeValue("def14"));// 其他税后扣款-个税

			save_bodyVO.setDef13((String) hcmTicketBody
					.getAttributeValue("def13"));// 应扣个税
			save_bodyVO.setDef12((String) hcmTicketBody
					.getAttributeValue("def12"));// 养老公司部分
			save_bodyVO.setDef11((String) hcmTicketBody
					.getAttributeValue("def11"));// 养老个人部分
			save_bodyVO.setDef51((String) hcmTicketBody
					.getAttributeValue("def51"));// 基础医疗公司部分
			save_bodyVO.setDef52((String) hcmTicketBody
					.getAttributeValue("def52"));// 基础医疗个人部分
			save_bodyVO.setDef10((String) hcmTicketBody
					.getAttributeValue("def10"));// 补充医疗公司部分
			save_bodyVO.setDef9((String) hcmTicketBody
					.getAttributeValue("def9"));// 补充医疗个人部分
			save_bodyVO.setDef8((String) hcmTicketBody
					.getAttributeValue("def8"));// 失业公司部分
			save_bodyVO.setDef7((String) hcmTicketBody
					.getAttributeValue("def7"));// 失业个人部分
			save_bodyVO.setDef5((String) hcmTicketBody
					.getAttributeValue("def5"));// 工伤公司部分
			save_bodyVO.setDef4((String) hcmTicketBody
					.getAttributeValue("def4"));// 工伤个人部分

			save_bodyVO.setDef3((String) hcmTicketBody
					.getAttributeValue("def3"));// 生育公司部分
			save_bodyVO.setDef2((String) hcmTicketBody
					.getAttributeValue("def2"));// 生育个人部分
			save_bodyVO.setDef1((String) hcmTicketBody
					.getAttributeValue("def1"));// 公积金公司部分
			save_bodyVO.setDef36((String) hcmTicketBody
					.getAttributeValue("def36"));// 公积金个人部分
			save_bodyVO.setDef48((String) hcmTicketBody
					.getAttributeValue("def48"));// 代发奖金扣款-营销
			save_bodyVO.setDef49((String) hcmTicketBody
					.getAttributeValue("def49"));// 代发奖金扣款-拓展
			save_bodyVO.setDef50((String) hcmTicketBody
					.getAttributeValue("def50"));// 代发奖金扣款-其他
			save_bodyVO.setDef53((String) hcmTicketBody
					.getAttributeValue("def53"));// 重大疾病医疗补助-公司部分
			save_bodyVO.setDef54((String) hcmTicketBody
					.getAttributeValue("def54"));// 重大疾病医疗补助-个人部分

			save_bodyVO.setDef55((String) hcmTicketBody
					.getAttributeValue("def55"));// 医保个人账户-公司部分
			save_bodyVO.setDef56((String) hcmTicketBody
					.getAttributeValue("def56"));// 医保个人账户-个人部分
			save_bodyVO.setDef57((String) hcmTicketBody
					.getAttributeValue("def57"));// 其他-公司部分
			save_bodyVO.setDef58((String) hcmTicketBody
					.getAttributeValue("def58"));// 其他-个人部分

			save_bodyVO.setMoney_de(new UFDouble(hcmTicketBody
					.getAttributeValue("money_de").toString()));// 实发合计 原币
			save_bodyVO.setLocal_money_bal(new UFDouble((String) hcmTicketBody
					.getAttributeValue("money_de").toString()));// 本币=原币（币种一样）
			save_bodyVO.setLocal_money_de(new UFDouble((String) hcmTicketBody
					.getAttributeValue("money_de").toString()));// 余额

			save_bodyVO.setTop_billid((String) ticketHead
					.getAttributeValue("pk_salaryfundaccure"));// 上层单据主键
			save_bodyVO.setTop_tradetype((String) ticketHead
					.getAttributeValue("transtype"));// 上层交易类型
			save_bodyVO.setTop_billtype("HCM1");// 上层单据类型
			save_bodyVO.setTop_itemid((String) hcmTicketBody
					.getAttributeValue("pk_salaryfundaccure_b"));// 上层单据行主键

			save_bodyVO.setPausetransact(UFBoolean.FALSE);// 挂起标志
			save_bodyVO.setBilldate(save_headVO.getBilldate());// 单据日期
			save_bodyVO.setPk_group(save_headVO.getPk_group());// 所属集团
			save_bodyVO.setPk_billtype(save_headVO.getPk_billtype());// 单据类型编码
			save_bodyVO.setBillclass(save_headVO.getBillclass());// 单据大类
			save_bodyVO.setPk_tradetype(save_headVO.getPk_tradetype());// 应收类型code
			save_bodyVO.setPk_tradetypeid(save_headVO.getPk_tradetypeid());// 应收类型
			save_bodyVO.setBusidate(save_headVO.getBilldate());// 起算日期
			save_bodyVO.setObjtype(save_headVO.getObjtype());// 往来对象
																// 2=部门，3=业务员，1=供应商，0=客户
			save_bodyVO.setDirection(1);// 方向
			save_bodyVO.setPk_currtype(save_headVO.getPk_currtype());// 币种
			save_bodyVO.setRate(new UFDouble(1));// 组织本币汇率
			save_bodyVO.setPk_deptid(save_headVO.getPk_deptid());// 部门

			save_bodyVO.setStatus(VOStatus.NEW);
			if (((String) hcmTicketBody.getAttributeValue("def18"))
					.equals((String) save_headVO.getPk_org())) {
				newbodylist.add(save_bodyVO);
				;
			} else {
				bodylist.add(save_bodyVO);
			}
		}

		UFDouble sum = new UFDouble();
		UFDouble sum31 = new UFDouble();
		UFDouble sum32 = new UFDouble();
		for (PayBillItemVO body : bodylist) {
			sum31 = sum31.add(new UFDouble(body.getDef31()));
			sum32 = sum32.add(new UFDouble(body.getDef32()));
		}
		for (PayBillItemVO body : newbodylist) {
			sum31 = sum31.add(new UFDouble(body.getDef31()));
			sum32 = sum32.add(new UFDouble(body.getDef32()));
		}
		sum = sum31.add(sum32);
		if (!UFDouble.ZERO_DBL.equals(sum)) {
			if (!pk_org.equals(getPk_orgByCode("GZ1-9001"))) {
				PayBillItemVO save_bodyVO = new PayBillItemVO();
				save_bodyVO.setPk_org(save_headVO.getPk_org());// 应收财务组织
				save_bodyVO
						.setSupplier(getSupplierIDByPkOrg(getPk_orgByCode("GZ1-9001")));// 供应商=工资发放公司
				save_bodyVO.setDef31(sum31.toString());// 应扣基金
				save_bodyVO.setAttributeValue("def32", sum32);// 慈善捐款
				save_bodyVO.setMoney_de(sum);// 实发合计 原币
				save_bodyVO.setLocal_money_bal(sum);// 本币=原币（币种一样）
				save_bodyVO.setLocal_money_de(sum);// 余额
				save_bodyVO.setPausetransact(UFBoolean.FALSE);// 挂起标志
				save_bodyVO.setBilldate(save_headVO.getBilldate());// 单据日期
				save_bodyVO.setPk_group(save_headVO.getPk_group());// 所属集团
				save_bodyVO.setPk_billtype(save_headVO.getPk_billtype());// 单据类型编码
				save_bodyVO.setBillclass(save_headVO.getBillclass());// 单据大类
				save_bodyVO.setPk_tradetype(save_headVO.getPk_tradetype());// 应收类型code
				save_bodyVO.setPk_tradetypeid(save_headVO.getPk_tradetypeid());// 应收类型
				save_bodyVO.setBusidate(save_headVO.getBilldate());// 起算日期
				save_bodyVO.setObjtype(save_headVO.getObjtype());// 往来对象
																	// 2=部门，3=业务员，1=供应商，0=客户
				save_bodyVO.setDirection(1);// 方向
				save_bodyVO.setPk_currtype(save_headVO.getPk_currtype());// 币种
				save_bodyVO.setRate(new UFDouble(1));// 组织本币汇率
				save_bodyVO.setPk_deptid(save_headVO.getPk_deptid());// 部门
				save_bodyVO.setStatus(VOStatus.NEW);

				save_bodyVO.setTop_billid((String) ticketHead
						.getAttributeValue("pk_salaryfundaccure"));// 上层单据主键
				save_bodyVO.setTop_tradetype((String) ticketHead
						.getAttributeValue("transtype"));// 上层交易类型
				save_bodyVO.setTop_billtype("HCM1");// 上层单据类型
				// save_bodyVO.setAttributeValue("top_itemid",
				// hcmTicketBody.getAttributeValue("pk_salaryfundaccure_b"));//
				// 上层单据行主键

				bodylist.add(save_bodyVO);
			}
		}

		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new PayBillItemVO[0]));
		
		// getArapBillPubQueryService().getDefaultVO(aggvo, true);

		return aggvo;
	}

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	public String getvidByorg(String pk_org) throws DAOException {
		String sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + pk_org
				+ "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_vid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_vid;
	}

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	/**
	 * 根据【用户编码】获取主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getUserIDByCode(String code) throws BusinessException {
		String sql = "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
				+ code + "'";
		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}

	/**
	 * 根据【pk_org】获取主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getSupplierIDByPkOrg(String pk_org) throws BusinessException {
		String sql = "select c.pk_supplier from bd_supplier c where c.pk_financeorg = (select org.pk_financeorg from org_financeorg org where org.pk_financeorg ='"
				+ pk_org + "')";
		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}

	public IArapBillPubQueryService getArapBillPubQueryService() {
		if (arapBillPubQueryService == null) {
			arapBillPubQueryService = NCLocator.getInstance().lookup(
					IArapBillPubQueryService.class);
		}
		return arapBillPubQueryService;
	}

	/**
	 * 根据【公司编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_orgByCode(String code) {
		String sql = "select pk_org from org_orgs where (code='" + code
				+ "' or name = '" + code + "') and dr=0 and enablestate=2 ";
		String pk_org = null;
		try {
			pk_org = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
