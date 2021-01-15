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
	private static final String HCMSalaryOperatorName = "RLZY";// HCMĬ�ϲ���Ա
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
				throw new BusinessRuntimeException("��" + billno + "��,"
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
		save_headVO.setPk_org(pk_org);// ��ͬǩ����˾=������֯
		save_headVO.setDef1((String) ticketHead.getAttributeValue("def1"));// ��ϵͳ����
		save_headVO.setDef2((String) ticketHead.getAttributeValue("def2"));// ��ϵͳ����
		save_headVO.setBilldate(new UFDate(ticketHead.getAttributeValue(
				"billdate").toString()));// �ۿ�����
		save_headVO.setDef67((String) ticketHead.getAttributeValue("def67"));// ����������

		// save_headVO.setDef56((String)ticketHead.getAttributeValue("def5"));//
		// �Ƿ��ʱ���
		// save_headVO.setDef57((String)ticketHead.getAttributeValue("def4"));//
		// ��˾����

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
		save_headVO.setPk_billtype("F3");// �������ͱ���
		// save_headVO.setBilldate(new UFDate());// ��������
		save_headVO.setBusidate(new UFDate());//
		save_headVO.setSyscode(1);// ��������ϵͳ��Ĭ��Ϊ1��1=Ӧ��ϵͳ
		save_headVO.setSrc_syscode(1);// ������Դϵͳ
		save_headVO.setPk_currtype("1002Z0100000000001K1");// ����
		save_headVO.setBillstatus(2);// ����״̬,Ĭ��Ϊ2������
		save_headVO.setPk_group("000112100000000005FD");// �������ţ�Ĭ��Ϊʱ������
		save_headVO.setBillmaker(getUserIDByCode(HCMSalaryOperatorName));// �Ƶ���
		save_headVO.setCreator(getUserIDByCode(HCMSalaryOperatorName));// ������
		save_headVO.setObjtype(1); // ��������
		// 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�
		save_headVO.setIsinit(UFBoolean.FALSE);// �ڳ���־
		save_headVO.setIsreded(UFBoolean.FALSE);// �Ƿ����
		save_headVO.setStatus(VOStatus.NEW);

		List<PayBillItemVO> bodylist = new ArrayList<>();
		List<PayBillItemVO> newbodylist = new ArrayList<>();// ��ͬǩ���͹��ʷ���һ��ʱ
		for (CircularlyAccessibleValueObject hcmTicketBody : ticketBody) {
			PayBillItemVO save_bodyVO = new PayBillItemVO();
			save_bodyVO.setPk_org(save_headVO.getPk_org());// Ӧ�ղ�����֯
			save_bodyVO.setSupplier(getSupplierIDByPkOrg((String) hcmTicketBody
					.getAttributeValue("def18")));// ��Ӧ��=���ʷ��Ź�˾
			save_bodyVO.setDef18(getSupplierIDByPkOrg((String) hcmTicketBody
					.getAttributeValue("def18")));// ��Ӧ��=���ʷ��Ź�˾
			save_bodyVO.setDef30((String) hcmTicketBody
					.getAttributeValue("def30"));// ��������
			save_bodyVO.setDef22((String) hcmTicketBody
					.getAttributeValue("def22"));// ����
			save_bodyVO.setDef29((String) hcmTicketBody
					.getAttributeValue("def29"));// ��������-Ӫ��
			save_bodyVO.setDef27((String) hcmTicketBody
					.getAttributeValue("def27"));// ��������-��չ
			save_bodyVO.setDef25((String) hcmTicketBody
					.getAttributeValue("def25"));// ��������-����
			save_bodyVO.setDef24((String) hcmTicketBody
					.getAttributeValue("def24"));// Ӷ��
			save_bodyVO.setDef23((String) hcmTicketBody
					.getAttributeValue("def23"));// ����˰ǰ�ۿ�-�籣��˾����
			save_bodyVO.setDef20((String) hcmTicketBody
					.getAttributeValue("def20"));// ����˰ǰ�ۿ�-�籣���˲���
			save_bodyVO.setDef19((String) hcmTicketBody
					.getAttributeValue("def19"));// ����˰ǰ�ۿ�-������˾����
			save_bodyVO.setDef17((String) hcmTicketBody
					.getAttributeValue("def17"));// ����˰ǰ�ۿ�-��������˲���
			save_bodyVO.setDef6((String) hcmTicketBody
					.getAttributeValue("def6"));// Ӧ���ϼ�

			save_bodyVO.setDef31((String) hcmTicketBody
					.getAttributeValue("def31"));// Ӧ�ۻ���
			save_bodyVO.setDef32((String) hcmTicketBody
					.getAttributeValue("def32"));// ���ƾ��
			save_bodyVO.setDef33((String) hcmTicketBody
					.getAttributeValue("def33"));// ����˰��ۿ�-����
			save_bodyVO.setDef45((String) hcmTicketBody
					.getAttributeValue("def45"));// ����˰��ۿ�-Ӫ��
			save_bodyVO.setDef46((String) hcmTicketBody
					.getAttributeValue("def46"));// ����˰��ۿ�-�籣��˾����
			save_bodyVO.setDef47((String) hcmTicketBody
					.getAttributeValue("def47"));// ����˰ǰ�ۿ�-�籣���˲���
			save_bodyVO.setDef21((String) hcmTicketBody
					.getAttributeValue("def21"));// ����˰��ۿ�-������˾����
			save_bodyVO.setDef15((String) hcmTicketBody
					.getAttributeValue("def15"));// ����˰��ۿ�-��������˲���
			save_bodyVO.setDef14((String) hcmTicketBody
					.getAttributeValue("def14"));// ����˰��ۿ�-��˰

			save_bodyVO.setDef13((String) hcmTicketBody
					.getAttributeValue("def13"));// Ӧ�۸�˰
			save_bodyVO.setDef12((String) hcmTicketBody
					.getAttributeValue("def12"));// ���Ϲ�˾����
			save_bodyVO.setDef11((String) hcmTicketBody
					.getAttributeValue("def11"));// ���ϸ��˲���
			save_bodyVO.setDef51((String) hcmTicketBody
					.getAttributeValue("def51"));// ����ҽ�ƹ�˾����
			save_bodyVO.setDef52((String) hcmTicketBody
					.getAttributeValue("def52"));// ����ҽ�Ƹ��˲���
			save_bodyVO.setDef10((String) hcmTicketBody
					.getAttributeValue("def10"));// ����ҽ�ƹ�˾����
			save_bodyVO.setDef9((String) hcmTicketBody
					.getAttributeValue("def9"));// ����ҽ�Ƹ��˲���
			save_bodyVO.setDef8((String) hcmTicketBody
					.getAttributeValue("def8"));// ʧҵ��˾����
			save_bodyVO.setDef7((String) hcmTicketBody
					.getAttributeValue("def7"));// ʧҵ���˲���
			save_bodyVO.setDef5((String) hcmTicketBody
					.getAttributeValue("def5"));// ���˹�˾����
			save_bodyVO.setDef4((String) hcmTicketBody
					.getAttributeValue("def4"));// ���˸��˲���

			save_bodyVO.setDef3((String) hcmTicketBody
					.getAttributeValue("def3"));// ������˾����
			save_bodyVO.setDef2((String) hcmTicketBody
					.getAttributeValue("def2"));// �������˲���
			save_bodyVO.setDef1((String) hcmTicketBody
					.getAttributeValue("def1"));// ������˾����
			save_bodyVO.setDef36((String) hcmTicketBody
					.getAttributeValue("def36"));// ��������˲���
			save_bodyVO.setDef48((String) hcmTicketBody
					.getAttributeValue("def48"));// ��������ۿ�-Ӫ��
			save_bodyVO.setDef49((String) hcmTicketBody
					.getAttributeValue("def49"));// ��������ۿ�-��չ
			save_bodyVO.setDef50((String) hcmTicketBody
					.getAttributeValue("def50"));// ��������ۿ�-����
			save_bodyVO.setDef53((String) hcmTicketBody
					.getAttributeValue("def53"));// �ش󼲲�ҽ�Ʋ���-��˾����
			save_bodyVO.setDef54((String) hcmTicketBody
					.getAttributeValue("def54"));// �ش󼲲�ҽ�Ʋ���-���˲���

			save_bodyVO.setDef55((String) hcmTicketBody
					.getAttributeValue("def55"));// ҽ�������˻�-��˾����
			save_bodyVO.setDef56((String) hcmTicketBody
					.getAttributeValue("def56"));// ҽ�������˻�-���˲���
			save_bodyVO.setDef57((String) hcmTicketBody
					.getAttributeValue("def57"));// ����-��˾����
			save_bodyVO.setDef58((String) hcmTicketBody
					.getAttributeValue("def58"));// ����-���˲���

			save_bodyVO.setMoney_de(new UFDouble(hcmTicketBody
					.getAttributeValue("money_de").toString()));// ʵ���ϼ� ԭ��
			save_bodyVO.setLocal_money_bal(new UFDouble((String) hcmTicketBody
					.getAttributeValue("money_de").toString()));// ����=ԭ�ң�����һ����
			save_bodyVO.setLocal_money_de(new UFDouble((String) hcmTicketBody
					.getAttributeValue("money_de").toString()));// ���

			save_bodyVO.setTop_billid((String) ticketHead
					.getAttributeValue("pk_salaryfundaccure"));// �ϲ㵥������
			save_bodyVO.setTop_tradetype((String) ticketHead
					.getAttributeValue("transtype"));// �ϲ㽻������
			save_bodyVO.setTop_billtype("HCM1");// �ϲ㵥������
			save_bodyVO.setTop_itemid((String) hcmTicketBody
					.getAttributeValue("pk_salaryfundaccure_b"));// �ϲ㵥��������

			save_bodyVO.setPausetransact(UFBoolean.FALSE);// �����־
			save_bodyVO.setBilldate(save_headVO.getBilldate());// ��������
			save_bodyVO.setPk_group(save_headVO.getPk_group());// ��������
			save_bodyVO.setPk_billtype(save_headVO.getPk_billtype());// �������ͱ���
			save_bodyVO.setBillclass(save_headVO.getBillclass());// ���ݴ���
			save_bodyVO.setPk_tradetype(save_headVO.getPk_tradetype());// Ӧ������code
			save_bodyVO.setPk_tradetypeid(save_headVO.getPk_tradetypeid());// Ӧ������
			save_bodyVO.setBusidate(save_headVO.getBilldate());// ��������
			save_bodyVO.setObjtype(save_headVO.getObjtype());// ��������
																// 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�
			save_bodyVO.setDirection(1);// ����
			save_bodyVO.setPk_currtype(save_headVO.getPk_currtype());// ����
			save_bodyVO.setRate(new UFDouble(1));// ��֯���һ���
			save_bodyVO.setPk_deptid(save_headVO.getPk_deptid());// ����

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
				save_bodyVO.setPk_org(save_headVO.getPk_org());// Ӧ�ղ�����֯
				save_bodyVO
						.setSupplier(getSupplierIDByPkOrg(getPk_orgByCode("GZ1-9001")));// ��Ӧ��=���ʷ��Ź�˾
				save_bodyVO.setDef31(sum31.toString());// Ӧ�ۻ���
				save_bodyVO.setAttributeValue("def32", sum32);// ���ƾ��
				save_bodyVO.setMoney_de(sum);// ʵ���ϼ� ԭ��
				save_bodyVO.setLocal_money_bal(sum);// ����=ԭ�ң�����һ����
				save_bodyVO.setLocal_money_de(sum);// ���
				save_bodyVO.setPausetransact(UFBoolean.FALSE);// �����־
				save_bodyVO.setBilldate(save_headVO.getBilldate());// ��������
				save_bodyVO.setPk_group(save_headVO.getPk_group());// ��������
				save_bodyVO.setPk_billtype(save_headVO.getPk_billtype());// �������ͱ���
				save_bodyVO.setBillclass(save_headVO.getBillclass());// ���ݴ���
				save_bodyVO.setPk_tradetype(save_headVO.getPk_tradetype());// Ӧ������code
				save_bodyVO.setPk_tradetypeid(save_headVO.getPk_tradetypeid());// Ӧ������
				save_bodyVO.setBusidate(save_headVO.getBilldate());// ��������
				save_bodyVO.setObjtype(save_headVO.getObjtype());// ��������
																	// 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�
				save_bodyVO.setDirection(1);// ����
				save_bodyVO.setPk_currtype(save_headVO.getPk_currtype());// ����
				save_bodyVO.setRate(new UFDouble(1));// ��֯���һ���
				save_bodyVO.setPk_deptid(save_headVO.getPk_deptid());// ����
				save_bodyVO.setStatus(VOStatus.NEW);

				save_bodyVO.setTop_billid((String) ticketHead
						.getAttributeValue("pk_salaryfundaccure"));// �ϲ㵥������
				save_bodyVO.setTop_tradetype((String) ticketHead
						.getAttributeValue("transtype"));// �ϲ㽻������
				save_bodyVO.setTop_billtype("HCM1");// �ϲ㵥������
				// save_bodyVO.setAttributeValue("top_itemid",
				// hcmTicketBody.getAttributeValue("pk_salaryfundaccure_b"));//
				// �ϲ㵥��������

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
	 * ���ݡ��û����롿��ȡ����
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
	 * ���ݡ�pk_org����ȡ����
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
	 * ���ݡ���˾���롿��ȡ����
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
