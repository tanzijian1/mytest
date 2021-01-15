package nc.bs.tg.outside.costadvance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.erm.accruedexpense.common.ErmAccruedBillUtils;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.fi.pub.Currency;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.erm.accruedexpense.IErmAccruedBillManage;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.er.exception.BugetAlarmBusinessException;
import nc.vo.erm.accruedexpense.AccruedDetailVO;
import nc.vo.erm.accruedexpense.AccruedVO;
import nc.vo.erm.accruedexpense.AggAccruedBillVO;
import nc.vo.fct.ap.entity.CtApVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.outside.CostBodyVO;
import nc.vo.tg.outside.CostHeadVO;

public class CostAdvanceBillToHTZYUtil extends BillUtils implements
		ITGSyncService {
	public static final String WYSFDefaultOperator = "LLWYSF";// ��ҵ�շ�ϵͳ�Ƶ���
	public static final String SRMDefaultOperator = "LLSRM";// ��ҵ�շ�ϵͳ�Ƶ���
	private IErmAccruedBillManage billManagerService;

	/**
	 * EBS��ͬ���ú�ͬԤ�ᵥ���ͨ����ͬʱ����һ�ź���Ԥ�ᵥ��һ�����ֺ�ͬԤռ������ͬ����
	 * ����Ԥ�ᵥ�͵��õ�Ԥ�ᵥ��Ϣһ�£�ֻ�н����ԭ����������Ԥ�ᵥ���š�����Ŀ����Ϣ��д���õ�Ԥ�ᵥ������ϸ�ӱ��С�
	 * ���ֺ�ͬԤռ�������Ǻ�ͬ��
	 */
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		// TODO �Զ����ɵķ������

		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(DefaultOperator));
		// �����û���
		InvocationInfoProxy.getInstance().setUserCode(DefaultOperator);

		// �������Ϣ
		JSONObject jsonData = (JSONObject) info.get("data");// ������

		// InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		ISqlThread iser = NCLocator.getInstance().lookup(ISqlThread.class);// Ԥ�����
		AggAccruedBillVO aggvo = null;
		// �������Ϣ
		String jsonhead = jsonData.getString("accrualHeadVO");// ��ϵͳ��Դ��ͷ����
		String jsonbody = jsonData.getString("accrualBodyVOs");// ��ϵͳ��Դ��������
		StringBuffer operation = new StringBuffer();// nc���в�������
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + jsonData);
		}
		// ת��json
		CostHeadVO headVO = JSONObject.parseObject(jsonhead, CostHeadVO.class);
		List<CostBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				CostBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("������ת��ʧ�ܣ����飡json:" + jsonData);
		}

		String srcid = headVO.getSrcid();// ��Դҵ�񵥾�ID
		String srcno = headVO.getSrcbillcode();// ��Դҵ�񵥾ݵ��ݺ�
		// String ncid = headVO.getNcid();
		String isadvance = headVO.getIsadvance();
		String billqueue = "��ͬԤռ�ӿ�" + ":" + srcid;
		String billkey = "��ͬԤռ�ӿ�" + ":" + srcid;
		String destype = "01";
		// // TODO ebsid ��ʵ�ʴ�����Ϣλ�ý��б��

		String contractcode = headVO.getContractcode();
		try {
			// ɾ������
			if ("1".equals(headVO.getType())) {
				AggAccruedBillVO prebillvo = new AggAccruedBillVO();
				IPFBusiAction pfaction = NCLocator.getInstance().lookup(
						IPFBusiAction.class);
				operation.append("ɾ������");
				AggAccruedBillVO deleaggvo = (AggAccruedBillVO) getBillVO(
						AggAccruedBillVO.class,
						"isnull(dr,0)=0 and   defitem11  = '"
								+ headVO.getContractcode()
								+ "' and defitem25='" + headVO.getYear() + "'");
				if (deleaggvo == null) {
					throw new BusinessException("�Ҳ�����Ӧ����" + headVO.getNcid());
				}
				if (deleaggvo.getParentVO().getBillstatus() == 3) {
					Object returnobj_uapp = pfaction.processAction(
							IPFActionName.UNAPPROVE, "262X", null, deleaggvo,
							null, null);
					if (((Object[]) returnobj_uapp)[0] instanceof nc.vo.erm.common.MessageVO) {
						prebillvo = (AggAccruedBillVO) ((nc.vo.erm.common.MessageVO) ((Object[]) returnobj_uapp)[0])
								.getSuccessVO();
						if (prebillvo != null) {
							deleaggvo = prebillvo;
						}
					}
				}
				Object returnobj = null;
				if (deleaggvo.getParentVO().getApprstatus() != -1) {
					returnobj = pfaction.processAction(IPFActionName.UNSAVE,
							"262X", null, deleaggvo, null, null);
				}
				if (returnobj != null) {
					if (((Object[]) returnobj)[0] instanceof nc.vo.erm.accruedexpense.AggAccruedBillVO) {
						deleaggvo = (AggAccruedBillVO) ((Object[]) returnobj)[0];
					}
				}
				getAppService().deleteVOs(new AggAccruedBillVO[] { deleaggvo });
				aggvo = deleaggvo;
			} else {
				if ("2".equals(headVO.getType())) {// ��������
					aggvo = doApprove(headVO, bodyVOs);
					operation.append("��������");
					if ("Y".equals(isadvance)) {
						String billno =aggvo.getParentVO().getBillno();
						AggAccruedBillVO preupdateAggvo = (AggAccruedBillVO) getBillVO(
								AggAccruedBillVO.class,
								"isnull(dr,0)=0 and   billno='"
										+ headVO.getNcid() + "'");
						if (preupdateAggvo == null) {
							throw new BusinessException("��Ԥ���������Ԥ�ᵥ����Ч���Ҳ�����Ӧ����"
									+ headVO.getNcid());
						}
						if(preupdateAggvo.getParentVO().getRedflag()==null||preupdateAggvo.getParentVO().getRedflag()!=1||preupdateAggvo.getParentVO().getRedflag()!=2){
							AggAccruedBillVO redbill = onTranRedBill(preupdateAggvo);// ���ɺ���Ԥ�ᵥ
							redbill.getParentVO().setDefitem30(billno);
							redbill.getParentVO().setPk_tradetype("262X-Cxx-LL04");
							redbill.getParentVO().setPk_tradetypeid(getBillTypePkByCode("262X-Cxx-LL04",
									redbill.getParentVO().getPk_group()));
							getAppService().insertVO(redbill);
						}else {
							String sql="select d.defitem30 from er_accrued_detail c, er_accrued d where c.pk_accrued_bill = d.pk_accrued_bill "
									+ " and c.src_accruedpk = '"+preupdateAggvo.getParentVO().getPk_accrued_bill()+"'";
							
							String redbillno=(String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
							if(redbillno!=null&&!redbillno.equals(billno)){
								throw new BusinessException("��Ԥ�����,Ԥ�ᵥ�ѱ���ͬռ�õ�"+billno+"��壬�����ظ�������");
							}
						}
					}
				} else if ("3".equals(headVO.getType())) {// ��������
						operation.append("����/���²���");
						aggvo=doInsertOrUpdate(headVO, bodyVOs);
						
				} else if ("4".equals(headVO.getType())) { // ��������+����
					operation.append("����+��������");
					aggvo=doInsertOrUpdate(headVO, bodyVOs);
					doApprove(headVO, bodyVOs);
				}
			}
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
			// }
		}
		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("billid", aggvo.getPrimaryKey());
		dataMap.put("operation", operation.toString());
		dataMap.put("billno",
				(String) aggvo.getParentVO()
						.getAttributeValue(AccruedVO.BILLNO));
		if (aggvo == null) {
			throw new BusinessException("��" + billkey + "��," + "����**�޷��ص���"
					+ "���в���**" + operation.toString());
		}
		return JSON.toJSONString(dataMap);
	}

	private AggAccruedBillVO onTranRedBill(AggAccruedBillVO preupdateAggvo)
			throws Exception {
		// TODO �Զ����ɵķ������
		AggAccruedBillVO redbill = (AggAccruedBillVO) preupdateAggvo.clone();
		AggAccruedBillVO newredbill = ErmAccruedBillUtils.getRedbackVO(redbill);
		return newredbill;
	}

	/*
	 * ����Ԥ�ᵥ��vo
	 */
	public AggAccruedBillVO updateVO(CostHeadVO headvo, AggAccruedBillVO aggvo,
			List<CostBodyVO> bodyVOs, CtApVO contracVO) throws Exception {
		AccruedVO update_hvo = aggvo.getParentVO();
		AccruedDetailVO[] bodyvos = aggvo.getChildrenVO();
		List<AccruedDetailVO> list = Arrays.asList(new AccruedDetailVO[0]);
		update_hvo.setAttributeValue("org_currinfo", null);// ��֯���һ���
		update_hvo.setAttributeValue("group_currinfo", null);// ���ű��һ���
		update_hvo.setAttributeValue("global_currinfo", null);// ȫ�ֱ��һ���
		update_hvo.setAttributeValue("amount", null);// ���
		update_hvo.setAttributeValue("org_amount", null);// ��֯���ҽ��
		update_hvo.setAttributeValue("group_amount", null);// ���ű��ҽ��
		update_hvo.setAttributeValue("global_amount", null);// ȫ�ֱ��ҽ��
		update_hvo.setAttributeValue("verify_amount", null);// �������
		update_hvo.setAttributeValue("org_verify_amount", null);// ��֯���Һ������
		update_hvo.setAttributeValue("group_verify_amount", null);// ���ű��Һ������
		update_hvo.setAttributeValue("global_verify_amount", null);// ȫ�ֱ��Һ������
		update_hvo.setAttributeValue("predict_rest_amount", null);// Ԥ�����
		update_hvo.setAttributeValue("rest_amount", null);// ���
		update_hvo.setAttributeValue("org_rest_amount", null);// ��֯�������
		update_hvo.setAttributeValue("group_rest_amount", null);// ���ű������
		update_hvo.setAttributeValue("global_rest_amount", null);// ȫ�ֱ������
		update_hvo.setAttributeValue("reason", headvo.getReason());// ����
		update_hvo.setAttributeValue("attach_amount", null);// ��������
		update_hvo.setAttributeValue("defitem1", headvo.getSrcid());// EBSID
		update_hvo.setAttributeValue("defitem6", headvo.getPlate());// ���
		update_hvo.setAttributeValue("defitem7", headvo.getIspostcontract());// �Ƿ�����ͬ
		update_hvo.setAttributeValue("defitem8", headvo.getContractmoney());// ��ͬ��̬���
		update_hvo.setAttributeValue("defitem9", null);// �Զ�����9
		update_hvo.setAttributeValue("defitem10", null);// �Զ�����10
		update_hvo.setAttributeValue("defitem11", headvo.getContractcode());// ��ͬ����
		update_hvo.setAttributeValue("defitem12", headvo.getContractname());// ��ͬ����
		update_hvo.setAttributeValue("defitem13", headvo.getContractversion());// ��ͬ�汾
		update_hvo.setAttributeValue("defitem14", headvo.getBilltype());// ��������
		update_hvo.setAttributeValue("defitem3",
				getPk_orgByCode(headvo.getBudget()));// Ԥ������
		update_hvo.setAttributeValue("defitem29", headvo.getOperatordept());// ���첿��
		update_hvo.setAttributeValue("defitem30", headvo.getOperator());// ������
		List<AccruedDetailVO> save_bodyVOs = new ArrayList<>();

		String assume_org = update_hvo.getDefitem3();
		String assume_dept = getOperator_dept("12", assume_org);
		int row = 1;
		for (CostBodyVO costBodyVO : bodyVOs) {
			AccruedDetailVO save_bodyVO = new AccruedDetailVO();
			save_bodyVO.setAttributeValue("rowno", row);// �к�
			row++;
			// save_bodyVO.setAttributeValue("assume_org_name",costBodyVO.getAssumeorg_name());//
			// ���óе���λ����
			save_bodyVO.setAttributeValue("assume_org", assume_org);// ���óе���λ����
			// save_bodyVO.setAttributeValue("assume_dept_name",costBodyVO.getAssumedept_name());//
			// ���óе���������
			save_bodyVO.setAttributeValue("assume_dept", assume_dept);// ���óе����ű���
			save_bodyVO.setAttributeValue("pk_iobsclass", null);// ��֧��Ŀ
			save_bodyVO.setAttributeValue("pk_pcorg", null);// ��������
			save_bodyVO.setAttributeValue("pk_resacostcenter", null);// �ɱ�����
			save_bodyVO.setAttributeValue("pk_checkele", null);// ����Ҫ��
			save_bodyVO.setAttributeValue("pk_project",
					getProject(costBodyVO.getProject()));// ��Ŀgetpk_projectByCode()
			save_bodyVO.setAttributeValue("pk_wbs", null);// ��Ŀ����
			save_bodyVO.setAttributeValue("pk_supplier", null);// ��Ӧ��
			save_bodyVO.setAttributeValue("pk_customer", null);// �ͻ�
			save_bodyVO.setAttributeValue("pk_proline", null);// ��Ʒ��
			save_bodyVO.setAttributeValue("pk_brand", null);// Ʒ��
			save_bodyVO.setAttributeValue("org_currinfo", null);// ��֯���һ���
			save_bodyVO.setAttributeValue("group_currinfo", null);// ���ű��һ���
			save_bodyVO.setAttributeValue("global_currinfo", null);// ȫ�ֱ��һ���
			save_bodyVO.setAttributeValue("amount",
					new UFDouble(costBodyVO.getAmount()));// ���
			save_bodyVO.setAttributeValue("org_amount", null);// ��֯���ҽ��
			save_bodyVO.setAttributeValue("group_amount", null);// ���ű��ҽ��
			save_bodyVO.setAttributeValue("global_amount", null);// ȫ�ֱ��ҽ��
			save_bodyVO.setAttributeValue("verify_amount", null);// �������
			save_bodyVO.setAttributeValue("org_verify_amount", null);// ��֯���Һ������
			save_bodyVO.setAttributeValue("group_verify_amount", null);// ���ű��Һ������
			save_bodyVO.setAttributeValue("global_verify_amount", null);// ȫ�ֱ��Һ������
			save_bodyVO.setAttributeValue("predict_rest_amount", new UFDouble(
					costBodyVO.getAmount()));// Ԥ�����
			save_bodyVO.setAttributeValue("rest_amount", new UFDouble(
					costBodyVO.getAmount()));// ���
			save_bodyVO.setAttributeValue("org_rest_amount", null);// ��֯�������
			save_bodyVO.setAttributeValue("group_rest_amount", null);// ���ű������
			save_bodyVO.setAttributeValue("global_rest_amount", null);// ȫ�ֱ������
			save_bodyVO
					.setAttributeValue("defitem2", costBodyVO.getDeptfloor());// ����¥��
			// save_bodyVO.setAttributeValue("defitem3", null);// �Զ�����3
			if ("262X-Cxx-FY01".equals(update_hvo.getPk_tradetype())) {
				save_bodyVO.setAttributeValue("defitem7",
						costBodyVO.getAdvanceamount());// ��Ԥ����
			}
			save_bodyVO.setAttributeValue("defitem4", costBodyVO.getScale());// ��ֱ���
			save_bodyVO.setAttributeValue("defitem5", costBodyVO.getExplain());// ˵��
			save_bodyVO.setAttributeValue("defitem8",
					costBodyVO.getBudgetyear());// Ԥ�����
			save_bodyVO.setAttributeValue("defitem9", null);// �Զ�����9
			save_bodyVO.setAttributeValue("defitem10", null);// �Զ�����10
			save_bodyVO.setAttributeValue("defitem11", null);// �Զ�����11
			save_bodyVO.setAttributeValue("defitem12",
					costBodyVO.getBudgetsubject());// Ԥ���Ŀ
			// save_bodyVO.setAttributeValue("defitem48",costBodyVO.getBudgetsubjectname());//
			// Ԥ���Ŀȫ��
			// save_bodyVO.setAttributeValue("defitem12","1011001");// Ԥ���Ŀ
			save_bodyVO.setAttributeValue("defitem13", null);// �Զ�����13
			save_bodyVO.setAttributeValue("defitem14", null);// �Զ�����14
			save_bodyVO.setAttributeValue("defitem15", null);// �Զ�����15
			save_bodyVO.setAttributeValue("defitem16", null);// �Զ�����16
			save_bodyVO.setAttributeValue("defitem17", null);// �Զ�����17
			save_bodyVO.setAttributeValue("defitem18", null);// �Զ�����18
			save_bodyVO.setAttributeValue("defitem19", null);// �Զ�����19
			save_bodyVO.setAttributeValue("defitem20", null);// �Զ�����20
			save_bodyVO.setAttributeValue("defitem21", null);// �Զ�����21
			// save_bodyVO.setAttributeValue("defitem22",getdefdocBycode(costBodyVO.getBusinessformat(),
			// "ys004"));// ҵ̬
			save_bodyVO.setAttributeValue("defitem22",
					costBodyVO.getBusinessformat());
			save_bodyVO.setAttributeValue("defitem23", null);// �Զ�����23
			save_bodyVO.setAttributeValue("defitem24", null);// �Զ�����24
			save_bodyVO.setAttributeValue("defitem25",
					getPsndocPkByCode(costBodyVO.getLender()));// �ÿ���
			save_bodyVO.setAttributeValue("defitem26",
					getcarnum(costBodyVO.getCardeptdoc()));// ���Ʋ��ŵ���
			save_bodyVO.setAttributeValue("defitem27", null);// �Զ�����27
			save_bodyVO.setAttributeValue("defitem28", null);// �Զ�����28
			save_bodyVO.setAttributeValue("defitem29",
					costBodyVO.getBudgetsubjectname());// �Զ�����29
			save_bodyVO.setAttributeValue("defitem30", null);// �Զ�����30
			save_bodyVO.setAttributeValue("src_accruedpk", null);// ��ԴԤ�ᵥpk
			save_bodyVO.setAttributeValue("srctype", null);// ��Դ��������
			save_bodyVO.setAttributeValue("src_detailpk", null);// ��ԴԤ��detailpk
			save_bodyVO.setAttributeValue("red_amount", null);// �����
			save_bodyVO.setAttributeValue("pk_accrued_bill", null);// ����Ԥ�ᵥ_����
			save_bodyVO.setStatus(VOStatus.NEW);
			save_bodyVOs.add(save_bodyVO);
		}
		update_hvo.setStatus(VOStatus.UPDATED);
		aggvo.setParentVO(update_hvo);
		aggvo.setChildrenVO(save_bodyVOs.toArray(new AccruedDetailVO[0]));
		resetCurrencyRate(aggvo.getParentVO());
		setHeadAmountByBodyAmounts(aggvo);
		List<AccruedDetailVO> update_bodyVOs = new ArrayList<>();
		update_bodyVOs.addAll(Arrays.asList(aggvo.getChildrenVO()));
		for (AccruedDetailVO bvo : bodyvos) {
			bvo.setStatus(VOStatus.DELETED);
			bvo.setDr(1);
			update_bodyVOs.add(bvo);
		}
		aggvo.setChildrenVO(update_bodyVOs.toArray(new AccruedDetailVO[0]));
		resetBody(aggvo);
		return aggvo;
	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public AggAccruedBillVO onTranBill(CostHeadVO headvo,
			List<CostBodyVO> bodyVOs, CtApVO contracVO) throws Exception {
		AggAccruedBillVO aggvo = new AggAccruedBillVO();
		AccruedVO save_headVO = new AccruedVO();
		save_headVO.setAttributeValue("pk_accrued_bill", null);// ����
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ��������
		save_headVO.setAttributeValue("pk_org",
				getPk_orgByCode(headvo.getOrg()));// ������֯
		save_headVO.setAttributeValue("pk_billtype", "262X");// ��������
		save_headVO.setAttributeValue("pk_tradetype", "262X-Cxx-LL03");// �������ͱ��
		if (StringUtils.isNotBlank(getBillTypePkByCode(
				save_headVO.getPk_tradetype(), save_headVO.getPk_group()))) {
			save_headVO.setAttributeValue(
					"pk_tradetypeid",
					getBillTypePkByCode(save_headVO.getPk_tradetype(),
							save_headVO.getPk_group()));// ��������
		} else {
			throw new BusinessException("�ý������ͱ��룺"
					+ save_headVO.getPk_tradetype() + "δ����NC����������");
		}

		save_headVO.setAttributeValue("billno", null);// ���ݱ��
		save_headVO.setBilldate(new UFDate());// ��������
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		if (headvo.getCurrency() != null && headvo.getCurrency() != "") {
			save_headVO.setAttributeValue("pk_currtype", headvo.getCurrency());
		}
		save_headVO.setAttributeValue("billstatus", 1);// ����״̬
		save_headVO.setAttributeValue("apprstatus", -1);// ����״̬
		save_headVO.setAttributeValue("effectstatus", 0);// ��Ч״̬
		save_headVO.setAttributeValue("org_currinfo", null);// ��֯���һ���
		save_headVO.setAttributeValue("group_currinfo", null);// ���ű��һ���
		save_headVO.setAttributeValue("global_currinfo", null);// ȫ�ֱ��һ���
		save_headVO.setAttributeValue("amount", null);// ���
		save_headVO.setAttributeValue("org_amount", null);// ��֯���ҽ��
		save_headVO.setAttributeValue("group_amount", null);// ���ű��ҽ��
		save_headVO.setAttributeValue("global_amount", null);// ȫ�ֱ��ҽ��
		save_headVO.setAttributeValue("verify_amount", null);// �������
		save_headVO.setAttributeValue("org_verify_amount", null);// ��֯���Һ������
		save_headVO.setAttributeValue("group_verify_amount", null);// ���ű��Һ������
		save_headVO.setAttributeValue("global_verify_amount", null);// ȫ�ֱ��Һ������
		save_headVO.setAttributeValue("predict_rest_amount", null);// Ԥ�����
		save_headVO.setAttributeValue("rest_amount", null);// ���
		save_headVO.setAttributeValue("org_rest_amount", null);// ��֯�������
		save_headVO.setAttributeValue("group_rest_amount", null);// ���ű������
		save_headVO.setAttributeValue("global_rest_amount", null);// ȫ�ֱ������
		save_headVO.setAttributeValue("reason", headvo.getReason());// ����
		save_headVO.setAttributeValue("attach_amount", null);// ��������
		save_headVO.setAttributeValue("operator_org", getopertor_org());// �����˵�λ
		save_headVO.setAttributeValue("operator_dept", getOperator_dept());// �����˲���
		save_headVO.setAttributeValue("operator", getopertor());// ������
		save_headVO.setAttributeValue("defitem1", headvo.getSrcid());// EBSID
		save_headVO.setAttributeValue("defitem2", headvo.getSrcbillcode());// EBS����
		save_headVO.setAttributeValue("defitem3",
				getPk_orgByCode(headvo.getBudget()));// Ԥ������
		save_headVO.setAttributeValue("defitem4", null);// �Զ�����4
		save_headVO.setAttributeValue("defitem5", null);// �Զ�����5
		save_headVO.setAttributeValue("defitem6", headvo.getPlate());// ���
		save_headVO.setAttributeValue("defitem7", headvo.getIspostcontract());// �Ƿ��ǿ����ͬ
		save_headVO.setAttributeValue("defitem8", headvo.getContractmoney());// ��ͬ��̬���
		save_headVO.setAttributeValue("defitem9", null);// �Զ�����9
		save_headVO.setAttributeValue("defitem10", null);// �Զ�����10
		save_headVO.setAttributeValue("defitem11", headvo.getContractcode());// ��ͬ����
		save_headVO.setAttributeValue("defitem12", headvo.getContractname());// ��ͬ����
		save_headVO.setAttributeValue("defitem13", headvo.getContractversion());// ��ͬ�汾
		save_headVO.setAttributeValue("defitem14", headvo.getBilltype());// ��������
		save_headVO.setAttributeValue("defitem15", null);// Ԥ������
		save_headVO.setAttributeValue("defitem16", null);// �Զ�����16
		save_headVO.setAttributeValue("defitem17", null);// �Զ�����17
		save_headVO.setAttributeValue("defitem18", null);// �Զ�����18
		save_headVO.setAttributeValue("defitem19", null);// �Զ�����19
		save_headVO.setAttributeValue("defitem20", null);// �Զ�����20
		save_headVO.setAttributeValue("defitem21", null);// �Զ�����21
		save_headVO.setAttributeValue("defitem22", null);// �Զ�����22
		save_headVO.setAttributeValue("defitem23", null);// �Զ�����23
		save_headVO.setAttributeValue("defitem24", null);// �Զ�����24
		save_headVO.setAttributeValue("defitem25", headvo.getYear());// �Զ�����25
		save_headVO.setAttributeValue("defitem26", null);// �Զ�����26
		save_headVO.setAttributeValue("defitem28", null);// �Զ�����28
		save_headVO.setAttributeValue("defitem29", headvo.getOperatordept());// ���첿��
		save_headVO.setAttributeValue("defitem30", headvo.getOperator());// ������
		save_headVO.setAttributeValue("approver", null);// ������
		save_headVO.setAttributeValue("approvetime", null);// ����ʱ��
		save_headVO.setAttributeValue("printer", null);// ��ʽ��ӡ��
		save_headVO.setAttributeValue("printdate", null);// ��ʽ��ӡ����
		save_headVO.setAttributeValue("creator",
				getUserIDByCode(headvo.getCreator()));
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
		save_headVO.setAttributeValue("modifier", null);// ����޸���
		save_headVO.setAttributeValue("modifiedtime", null);// ����޸�ʱ��
		save_headVO.setAttributeValue("auditman",
				getUserIDByCode(headvo.getCreator()));
		save_headVO.setAttributeValue("redflag", null);// ����־
		save_headVO.setAttributeValue("imag_status", null);// Ӱ��״̬
		save_headVO.setAttributeValue("isneedimag", UFBoolean.FALSE);// ��ҪӰ��ɨ��
		save_headVO.setAttributeValue("isexpedited", UFBoolean.FALSE);// ����
		save_headVO.setAttributeValue("red_amount", null);// �����
		save_headVO.setAttributeValue("org_amount", null);// ��֯���
		List<AccruedDetailVO> save_bodyVOs = new ArrayList<>();
		int row = 1;
		String assume_org = save_headVO.getDefitem3();
		String assume_dept = getOperator_dept("12", assume_org);
		for (CostBodyVO costBodyVO : bodyVOs) {
			AccruedDetailVO save_bodyVO = new AccruedDetailVO();
			checkBodyTransforExpense(costBodyVO);// ��ֵ���
			save_bodyVO.setAttributeValue("pk_accrued_detail", null);// ����
			save_bodyVO.setAttributeValue("rowno", row);// �к�
			row++;
			// ���óе���λ����
			save_bodyVO.setAttributeValue("assume_org", assume_org);// ���óе���λ����
			// ���óе���������
			save_bodyVO.setAttributeValue("assume_dept", assume_dept);// ���óе����ű���
			save_bodyVO.setAttributeValue("pk_iobsclass", null);// ��֧��Ŀ
			save_bodyVO.setAttributeValue("pk_pcorg", null);// ��������
			save_bodyVO.setAttributeValue("pk_resacostcenter", null);// �ɱ�����
			save_bodyVO.setAttributeValue("pk_checkele", null);// ����Ҫ��
			save_bodyVO.setAttributeValue("pk_wbs", null);// ��Ŀ����
			save_bodyVO.setAttributeValue("pk_supplier", null);// ��Ӧ��
			save_bodyVO.setAttributeValue("pk_customer", null);// �ͻ�
			save_bodyVO.setAttributeValue("pk_proline", null);// ��Ʒ��
			save_bodyVO.setAttributeValue("pk_brand", null);// Ʒ��
			save_bodyVO.setAttributeValue("org_currinfo", null);// ��֯���һ���
			save_bodyVO.setAttributeValue("group_currinfo", null);// ���ű��һ���
			save_bodyVO.setAttributeValue("global_currinfo", null);// ȫ�ֱ��һ���
			save_bodyVO.setAttributeValue("amount",
					new UFDouble(costBodyVO.getAmount()));// ���
			save_bodyVO.setAttributeValue("org_amount", null);// ��֯���ҽ��
			save_bodyVO.setAttributeValue("group_amount", null);// ���ű��ҽ��
			save_bodyVO.setAttributeValue("global_amount", null);// ȫ�ֱ��ҽ��
			save_bodyVO.setAttributeValue("verify_amount", null);// �������
			save_bodyVO.setAttributeValue("org_verify_amount", null);// ��֯���Һ������
			save_bodyVO.setAttributeValue("group_verify_amount", null);// ���ű��Һ������
			save_bodyVO.setAttributeValue("global_verify_amount", null);// ȫ�ֱ��Һ������
			save_bodyVO.setAttributeValue("predict_rest_amount", new UFDouble(
					costBodyVO.getAmount()));// Ԥ�����
			save_bodyVO.setAttributeValue("rest_amount", new UFDouble(
					costBodyVO.getAmount()));// ���
			save_bodyVO.setAttributeValue("org_rest_amount", null);// ��֯�������
			save_bodyVO.setAttributeValue("group_rest_amount", null);// ���ű������
			save_bodyVO.setAttributeValue("global_rest_amount", null);// ȫ�ֱ������
			save_bodyVO
					.setAttributeValue("defitem2", costBodyVO.getDeptfloor());// ����¥��
			if ("262X-Cxx-FY01".equals(save_headVO.getPk_tradetype())
					&& Float.valueOf(costBodyVO.getAdvanceamount()) != null) {
				save_bodyVO.setAttributeValue("defitem7",
						costBodyVO.getAdvanceamount());// �Ƿ��Ԥ��
			}
			save_bodyVO.setAttributeValue("defitem3", null);// �Զ�����3
			if ("262X-Cxx-FY01".equals(save_headVO.getPk_tradetype())) {
				save_bodyVO.setAttributeValue("defitem3",
						costBodyVO.getAdvanceamount());// ��Ԥ����
			}
			save_bodyVO.setAttributeValue("defitem4", costBodyVO.getScale());// ��ֱ���
			save_bodyVO.setAttributeValue("defitem5", costBodyVO.getExplain());// ˵��
			save_bodyVO.setAttributeValue("defitem6", null);// �Զ�����6
			save_bodyVO.setAttributeValue("defitem8",
					costBodyVO.getBudgetyear());// Ԥ�����
			save_bodyVO.setAttributeValue("defitem9", null);// �Զ�����9
			save_bodyVO.setAttributeValue("defitem10", null);// �Զ�����10
			save_bodyVO.setAttributeValue("defitem11", null);// �Զ�����11
			save_bodyVO.setAttributeValue("defitem12",
					costBodyVO.getBudgetsubject());// Ԥ���Ŀ
			save_bodyVO.setAttributeValue("defitem48",
					costBodyVO.getBudgetsubjectname());// Ԥ���Ŀȫ��
			// save_bodyVO.setAttributeValue("defitem12","10011210000000002ALE");//
			// Ԥ���Ŀ
			save_bodyVO.setAttributeValue("defitem13", null);// �Զ�����13
			save_bodyVO.setAttributeValue("defitem14", null);// �Զ�����14
			save_bodyVO.setAttributeValue("defitem15", null);// �Զ�����15
			save_bodyVO.setAttributeValue("defitem16", null);// �Զ�����16
			save_bodyVO.setAttributeValue("defitem17", null);// �Զ�����17
			save_bodyVO.setAttributeValue("defitem18", null);// �Զ�����18
			save_bodyVO.setAttributeValue("defitem19", null);// �Զ�����19
			save_bodyVO.setAttributeValue("defitem20", null);// �Զ�����20
			save_bodyVO.setAttributeValue("defitem21", null);// �Զ�����21
			save_bodyVO.setAttributeValue("defitem22",
					costBodyVO.getBusinessformat());// ҵ̬
			// save_bodyVO.setAttributeValue("defitem22","10011210000000002BD7");
			save_bodyVO.setAttributeValue("defitem23", null);// �Զ�����23
			save_bodyVO.setAttributeValue("defitem24", null);// �Զ�����24
			save_bodyVO.setAttributeValue("defitem25",
					getPsndocPkByCode(costBodyVO.getLender()));// �ÿ���
			save_bodyVO.setAttributeValue("defitem26",
					getcarnum(costBodyVO.getCardeptdoc()));// ���Ʋ��ŵ���
			save_bodyVO.setAttributeValue("defitem27", null);// �Զ�����27
			save_bodyVO.setAttributeValue("defitem28", null);// �Զ�����28
			save_bodyVO.setAttributeValue("defitem29",
					costBodyVO.getBudgetsubjectname());// �Զ�����29
			save_bodyVO.setAttributeValue("defitem30", null);// �Զ�����30
			save_bodyVO.setAttributeValue("src_accruedpk", null);// ��ԴԤ�ᵥpk
			save_bodyVO.setAttributeValue("srctype", null);// ��Դ��������
			save_bodyVO.setAttributeValue("src_detailpk", null);// ��ԴԤ��detailpk
			save_bodyVO.setAttributeValue("red_amount", null);// �����
			save_bodyVO.setAttributeValue("pk_accrued_bill", null);// ����Ԥ�ᵥ_����
			save_bodyVO.setAttributeValue("pk_project",
					getProject(costBodyVO.getProject()));
			save_bodyVO.setStatus(VOStatus.NEW);
			save_bodyVOs.add(save_bodyVO);
		}

		save_headVO.setStatus(VOStatus.NEW);
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(save_bodyVOs.toArray(new AccruedDetailVO[0]));
		resetCurrencyRate(aggvo.getParentVO());
		setHeadAmountByBodyAmounts(aggvo);
		resetBody(aggvo);
		return aggvo;
	}

	private IErmAccruedBillManage getAppService() {
		if (billManagerService == null) {
			billManagerService = NCLocator.getInstance().lookup(
					IErmAccruedBillManage.class);
		}
		return billManagerService;
	}

	/**
	 * ���ݱ���������ͷ�ܽ��������ܽ��
	 * 
	 * @param cardPanel
	 */
	public static UFDouble setHeadAmountByBodyAmounts(AggAccruedBillVO aggvo) {
		UFDouble newYbje = null;
		AccruedDetailVO[] items = (AccruedDetailVO[]) aggvo.getChildrenVO();

		int length = items.length;

		for (int i = 0; i < length; i++) {
			if (items[i].getAmount() != null) {// �������д��ڿ���ʱ��ԭ�ҽ��Ϊ�գ������������п�
				if (newYbje == null) {
					newYbje = items[i].getAmount();
				} else {
					newYbje = newYbje.add(items[i].getAmount());
				}
			}
		}

		aggvo.getParentVO().setAmount(newYbje);
		aggvo.getParentVO().setPredict_rest_amount(newYbje);
		aggvo.getParentVO().setOrg_rest_amount(newYbje);
		aggvo.getParentVO().setOrg_verify_amount(newYbje);
		aggvo.getParentVO().setRest_amount(newYbje);
		aggvo.getParentVO().setOrg_amount(newYbje);
		return newYbje;
	}

	public String getPk_group() {
		return AppContext.getInstance().getPkGroup();
	}

	/**
	 * ���ݱ������ݽ��м���
	 * 
	 * @param aggvo
	 * @throws Exception
	 */
	public void resetBody(AggAccruedBillVO aggvo) throws Exception {
		AccruedVO headvo = aggvo.getParentVO();
		String headPk_org = headvo.getPk_org();
		AccruedDetailVO[] bodyVOs = aggvo.getChildrenVO();

		for (int i = 0; i < bodyVOs.length; i++) {
			AccruedDetailVO accruedDetailVO = bodyVOs[i];
			String assume_org = accruedDetailVO.getAssume_org();
			String pk_currtype = headvo.getPk_currtype();// ����
			UFDate date = headvo.getBilldate();// ��������
			if (headPk_org == null || assume_org == null || pk_currtype == null
					|| date == null) {
				return;
			}

			try {
				// �������
				String headOrgCurrPk = Currency.getOrgLocalCurrPK(headPk_org);
				String assume_orgCurrPk = Currency
						.getOrgLocalCurrPK(assume_org);// ������ͬʱ��ȡ��ͷ����
				if (headPk_org.equals(assume_org)
						|| (headOrgCurrPk != null && assume_orgCurrPk != null && assume_orgCurrPk
								.equals(headOrgCurrPk))) {

					accruedDetailVO.setOrg_currinfo(getHeadUFDoubleValue(
							headvo, AccruedVO.ORG_CURRINFO));
					accruedDetailVO.setGroup_currinfo(getHeadUFDoubleValue(
							headvo, AccruedVO.GROUP_CURRINFO));
					accruedDetailVO.setGlobal_currinfo(getHeadUFDoubleValue(
							headvo, AccruedVO.GLOBAL_CURRINFO));
				} else {
					// ����(���ң����ű��ң�ȫ�ֱ��һ���)
					UFDouble orgRate = Currency.getRate(assume_org,
							pk_currtype, date);
					UFDouble groupRate = Currency.getGroupRate(assume_org,
							getPk_group(), pk_currtype, date);
					UFDouble globalRate = Currency.getGlobalRate(assume_org,
							pk_currtype, date);

					accruedDetailVO.setOrg_currinfo(orgRate);
					accruedDetailVO.setGroup_currinfo(groupRate);
					accruedDetailVO.setGlobal_currinfo(globalRate);
				}

				Object amount = accruedDetailVO.getAmount();
				accruedDetailVO.setRest_amount(accruedDetailVO.getAmount());
				accruedDetailVO.setPredict_rest_amount(accruedDetailVO
						.getAmount());

				// ������
				UFDouble ori_amount = accruedDetailVO.getAmount();// ���þ���

				accruedDetailVO.setVerify_amount(UFDouble.ZERO_DBL);
				accruedDetailVO.setOrg_verify_amount(UFDouble.ZERO_DBL);
				accruedDetailVO.setGroup_verify_amount(UFDouble.ZERO_DBL);
				accruedDetailVO.setGlobal_verify_amount(UFDouble.ZERO_DBL);

				String pk_org = accruedDetailVO.getAssume_org();

				// ����
				String pk_group = (String) headvo
						.getAttributeValue(AccruedVO.PK_GROUP);

				// ��ȡ������(�ܸ��ݱ�����õ�λ)������屾�ҽ��
				UFDouble hl = (UFDouble) accruedDetailVO
						.getAttributeValue(AccruedDetailVO.ORG_CURRINFO);
				UFDouble grouphl = (UFDouble) accruedDetailVO
						.getAttributeValue(AccruedDetailVO.GROUP_CURRINFO);
				UFDouble globalhl = (UFDouble) accruedDetailVO
						.getAttributeValue(AccruedDetailVO.GLOBAL_CURRINFO);

				UFDouble[] bbje = null;
				// ��֯���ҽ��
				bbje = Currency.computeYFB(pk_org, Currency.Change_YBCurr,
						pk_currtype, ori_amount, null, null, null, hl,
						AppContext.getInstance().getServerTime().getDate());

				accruedDetailVO.setAttributeValue(AccruedDetailVO.ORG_AMOUNT,
						bbje[2]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.ORG_REST_AMOUNT, bbje[2]);
				// ���š�ȫ�ֽ��
				UFDouble[] money = null;
				if (bbje == null || bbje[2] == null) {
					money = Currency.computeGroupGlobalAmount(ori_amount,
							UFDouble.ZERO_DBL, pk_currtype, AppContext
									.getInstance().getServerTime().getDate(),
							pk_org, pk_group, globalhl, grouphl);

				} else {
					money = Currency.computeGroupGlobalAmount(ori_amount,
							bbje[2], pk_currtype, AppContext.getInstance()
									.getServerTime().getDate(), pk_org,
							pk_group, globalhl, grouphl);
				}

				accruedDetailVO.setAttributeValue(AccruedDetailVO.GROUP_AMOUNT,
						money[0]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.GROUP_REST_AMOUNT, money[0]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.GLOBAL_AMOUNT, money[1]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.GLOBAL_REST_AMOUNT, money[1]);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
	}

	public UFDouble getHeadUFDoubleValue(AccruedVO headvo, String key) {
		if (headvo.getAttributeValue(key) == null) {
			return UFDouble.ZERO_DBL;
		}
		return (UFDouble) headvo.getAttributeValue(key);
	}

	/**
	 * ���û���
	 * 
	 * @param pk_org
	 *            ��֯pk
	 * @param pk_currtype
	 *            ԭ�ұ���
	 * @param date
	 *            �Ƶ�ʱ��
	 * @throws Exception
	 */
	public void resetCurrencyRate(AccruedVO headvo) throws Exception {
		String pk_org = (String) headvo.getAttributeValue(AccruedVO.PK_ORG);// ��֯
		String pk_currtype = (String) headvo
				.getAttributeValue(AccruedVO.PK_CURRTYPE);// ����
		UFDate date = (UFDate) headvo.getAttributeValue(AccruedVO.BILLDATE);// ��������

		try {
			// ����(���ң����ű��ң�ȫ�ֱ��һ���)
			UFDouble orgRate = Currency.getRate(pk_org, pk_currtype, date);
			UFDouble groupRate = Currency.getGroupRate(pk_org,
					headvo.getPk_group(), pk_currtype, date);
			UFDouble globalRate = Currency.getGlobalRate(pk_org, pk_currtype,
					date);

			headvo.setAttributeValue(AccruedVO.ORG_CURRINFO, orgRate);
			headvo.setAttributeValue(AccruedVO.GROUP_CURRINFO, orgRate);
			headvo.setAttributeValue(AccruedVO.GLOBAL_CURRINFO, orgRate);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}


	private void checkBodyTransforExpense(CostBodyVO bodyvo)
			throws BusinessException {
		if (StringUtils.isBlank(Float.valueOf(bodyvo.getAmount()).toString())) {
			throw new BusinessException("����Ϊ��");
		}
	}

	public AggAccruedBillVO  doInsertOrUpdate(CostHeadVO headVO, List<CostBodyVO> bodyVOs) throws Exception {
		ISqlThread iser = NCLocator.getInstance().lookup(ISqlThread.class);// Ԥ�����
		AggAccruedBillVO aggvo = null;
		AggAccruedBillVO exitvo = (AggAccruedBillVO) getBillVO(
				AggAccruedBillVO.class, "isnull(dr,0)=0 and   defitem11  = '"
						+ headVO.getContractcode() + "' and defitem25='"
						+ headVO.getYear() + "'");
		if (exitvo != null) {
			AggAccruedBillVO prebillvo = new AggAccruedBillVO();
			AggAccruedBillVO updedbillvo = new AggAccruedBillVO();
			AggAccruedBillVO preupdateAggvo = exitvo;
			if (preupdateAggvo.getParentVO().getApprstatus() == 1) {//
				// ����ǰ���������������
				// flag=true;
				prebillvo = iser.ytunapprove_RequiresNew(preupdateAggvo);
			} else {
				prebillvo = preupdateAggvo;
			}
			updedbillvo = updateVO(headVO, prebillvo, bodyVOs, null);
			aggvo=iser.ytupdate_RequiresNew(updedbillvo);
		} else {
			AggAccruedBillVO billvo = onTranBill(headVO, bodyVOs, null);
			AccruedDetailVO[] bvoss = billvo.getChildrenVO();
			AggAccruedBillVO nbillvo = (AggAccruedBillVO) billvo.clone();
			aggvo=iser.ytinsert_RequiresNew(billvo);
		}
		return aggvo;
	}
	public AggAccruedBillVO  doApprove(CostHeadVO headVO, List<CostBodyVO> bodyVOs) throws Exception {
		AggAccruedBillVO aggvo = null;

		AggAccruedBillVO billvo = (AggAccruedBillVO) getBillVO(
				AggAccruedBillVO.class,
				"isnull(dr,0)=0 and   defitem11  = '"
						+ headVO.getContractcode()
						+ "' and defitem25='" + headVO.getYear()
						+ "'");
		if (billvo == null) {
			throw new BusinessException("�Ҳ�����Ӧ����"
					+ headVO.getNcid());
		}
		billvo.getParentVO().setApprover(
				getUserIDByCode(DefaultOperator));
		billvo.getParentVO().setApprovetime(new UFDateTime());
		IPFBusiAction pfaction = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		Object returnobj = pfaction.processAction(
				IPFActionName.APPROVE, "262X", null, billvo, null,
				null);
		if (((Object[]) returnobj)[0] instanceof nc.vo.erm.common.MessageVO) {
			aggvo = (AggAccruedBillVO) ((nc.vo.erm.common.MessageVO) ((Object[]) returnobj)[0])
					.getSuccessVO();
		}
		
		return aggvo;
	}
	
	
}
