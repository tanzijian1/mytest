package nc.ui.tg.financingexpense.ace.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.pub.contract.IContractQueryService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.vo.cdm.contractbankcredit.CwgwfzxqkBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.contract.ContractVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.ConstateExeVO;
import nc.vo.tg.singleissue.ContractStateVO;

public class AceHeadAfterEditHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	private BillForm billfrom;
	
	public AceHeadAfterEditHandler(BillForm billfrom) {
		super();
		this.billfrom = billfrom;
	}
	
	public AceHeadAfterEditHandler() {
		super();
	}
	/**
	 * Ԫ���ݳ־û���ѯ�ӿ�
	 * 
	 * @return
	 */
	IMDPersistenceQueryService mdQryService = null;
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}
	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent event) {
		// TODO �Զ����ɵķ������
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		BillCardPanel panel = event.getBillCardPanel();
		if ("transtypepk".equals(event.getKey())) {
			String transtypepk = (String) event.getValue();
			String sql = "select pk_billtypecode from bd_billtype where pk_billtypeid='"
					+ transtypepk + "'";
			String transtypecode = null;
			try {
				transtypecode = (String) bs.executeQuery(sql,
						new ColumnProcessor());
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			panel.setHeadItem("transtype", transtypecode);
		}
		// ������ʷ�
		String pk_tradetype = (String) event.getBillCardPanel()
				.getHeadItem("transtype").getValueObject();
		if ("RZ06-Cxx-001".equals(pk_tradetype)) {
			if(event.getBillCardPanel().getHeadItem("def12").getValueObject() == null){
				//���ڷ������Ϊ�յ������
				if ("def4".equals(event.getKey())
						|| "pk_payer".equals(event.getKey())) {
					String pk_contract = (String) event.getBillCardPanel()
							.getHeadItem("def4").getValueObject();
					String pk_payer = (String) event.getBillCardPanel()
							.getHeadItem("pk_payer").getValueObject();
					if (pk_contract != null && pk_payer != null) {
						UIRefPane repanel = (UIRefPane) event.getBillCardPanel()
								.getHeadItem("pk_payer").getComponent();
						String payer = repanel.getRefName();
						String sql = "select sum(m_ftje�� from cdm_cwfyft  where pk_contract ='"
								+ pk_contract
								+ "' and pk_czgs='"
								+ pk_payer
								+ "' and dr =0";
						Object amount = null;
						try {
							amount = (Object) bs.executeQuery(sql,
									new ColumnProcessor());
							
							sql = "select contractamount from cdm_contract   where pk_contract ='"
									+ pk_contract + "' and dr =0";
							Object contractamount = bs.executeQuery(sql,
									new ColumnProcessor());
							
							sql = "select m_zfje,pk_zfgs  from cdm_cwgwfzxqk where pk_contract ='"
									+ pk_contract + "' and dr =0";
							List<CwgwfzxqkBVO> list = (List<CwgwfzxqkBVO>) bs
									.executeQuery(sql, new BeanListProcessor(
											CwgwfzxqkBVO.class));
							
							// ��Ӧ���빫˾�ۼ�������
							UFDouble orgAmout = UFDouble.ZERO_DBL;
							// ��Ӧ��ͬ�ۼ�������
							UFDouble contractAmout = UFDouble.ZERO_DBL;
							for (CwgwfzxqkBVO vo : list) {
								if (payer.equals(vo.getAttributeValue("pk_zfgs"))) {
									orgAmout = orgAmout.add((UFDouble) vo
											.getAttributeValue("m_zfje"));
								}
								contractAmout = contractAmout.add((UFDouble) vo
										.getAttributeValue("m_zfje"));
							}
							panel.setHeadItem("def6", amount);// ���÷�̯���
							panel.setHeadItem("paymentamount", contractAmout);// ���õ�ǰ��ͬ���ۼƽ��
							panel.setHeadItem("def8", orgAmout);// ���õ�ǰ��ͬ�����빫˾�µ����ۼƽ��
							panel.setHeadItem("contractmoney", contractamount);// ���ú�ͬ���
							
							ContractVO contVO = NCLocator
									.getInstance()
									.lookup(IContractQueryService.class)
									.queryContractMainByPk(
											(String) panel.getHeadItem(
													event.getKey())
													.getValueObject());
							if (contVO != null) {
								panel.setHeadItem("def16", contVO.getVdef16());// ��Ŀ����
							}
							
							//�ڲ�����ʷ������ô����ͬʱ���Զ��������Ƿ�ɵֿۣ�def25�����ֿ�˰����Ϣ(def26) 2020-04-08-̸�ӽ�-start
//							String pk_contract = (String) event.getBillCardPanel()
//									.getHeadItem("def4").getValueObject();
//							String pk_payer = (String) event.getBillCardPanel()
//									.getHeadItem("pk_payer").getValueObject();
							if (pk_payer != null && pk_contract != null) {
								StringBuffer query = new StringBuffer();
								query.append("select t.b_dkjxs, t.m_sx  ");
								query.append("  from cdm_contract c  ");
								query.append("  left join cdm_cwfyft t  ");
								query.append("    on c.pk_contract = t.pk_contract  ");
								query.append(" where c.pk_contract = '" + pk_contract
										+ "'  ");
								query.append("   and t.pk_czgs = '" + pk_payer + "'  ");
								query.append("   and c.dr = 0  ");
								query.append("   and t.dr = 0  ");
								HashMap<String, Object> map = (HashMap<String, Object>) bs
										.executeQuery(query.toString(), new MapProcessor());
								if (map != null) {
									Object b_dkjxs = map.get("b_dkjxs");
									Object m_sx = map.get("m_sx");
									panel.setHeadItem("def25", b_dkjxs);
									panel.setHeadItem("def26", m_sx);
								}
							}
							
						} catch (BusinessException e) {
							MessageDialog.showErrorDlg(this.billfrom, "����", e.getMessage());
						}
					}
				}
			}
			
		}
		
		//�Ƿ��з�Ʊ
		//add by tjl 2020-05-19 
		if("def31".equals(event.getKey())){
			String pk_defdoc = (String) panel.getHeadItem("def31").getValueObject();
			String code = null;
			try {
				code = getCodeByPk_defdoc(pk_defdoc);
			} catch (BusinessException e) {
				MessageDialog.showErrorDlg(this.billfrom, "����", e.getMessage());
			}
			if("01".equals(code)){//��
				panel.getHeadItem("def18").setNull(true);//���ñ���
			}else{
				panel.getHeadItem("def18").setNull(false);
			}
		}
		//end
		
		if ("def4".equals(event.getKey())) {
			if ("RZ06-Cxx-001".equals(pk_tradetype)){
				//���ڷ�����������д����ֻͬ��ѡ��һ��
				if(event.getValue()!=null){
					event.getBillCardPanel().getHeadItem("def12").setEnabled(false);
					event.getBillCardPanel().getHeadItem("def12").setNull(false);
				}else{
					event.getBillCardPanel().getHeadItem("def12").setEnabled(true);
					event.getBillCardPanel().getHeadItem("def12").setNull(true);
				}
			}
			ContractVO contVO = null;
			try {
				contVO = NCLocator
						.getInstance()
						.lookup(IContractQueryService.class)
						.queryContractMainByPk(
								(String) panel.getHeadItem(event.getKey())
								.getValueObject());
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				MessageDialog.showErrorDlg(this.billfrom, "����", e.getMessage());
			}
			if (contVO != null) {
				panel.setHeadItem("def16", contVO.getVdef16());// ��Ŀ����
			}
		}
		//���Ľ�  ���뵥�ڷ������
		if ("RZ06-Cxx-001".equals(pk_tradetype)){
			if("def12".equals(event.getKey())){
				event.getBillCardPanel().getHeadItem("pk_payer").setValue(null);//���λ
				event.getBillCardPanel().getHeadItem("contractmoney").setValue(null);//��ͬ���
				event.getBillCardPanel().getHeadItem("paymentamount").setValue(null);//�ۼ��Ѹ����
				event.getBillCardPanel().getHeadItem("def1").setValue(null);//���������˻�
				event.getBillCardPanel().getHeadItem("def6").setValue(null);//�����̯���
				try {
					if(event.getValue()!=null){
						//���ڷ�����������д����ֻͬ��ѡ��һ��
						event.getBillCardPanel().getHeadItem("def4").setEnabled(false);
						event.getBillCardPanel().getHeadItem("def4").setNull(false);
						event.getBillCardPanel().getHeadItem("contractmoney").setEnabled(false);
						AggSingleIssueVO aggvo=(AggSingleIssueVO)getBillVO(AggSingleIssueVO.class,"nvl(dr,0) = 0 and pk_singleissue='"+event.getValue()+"'");
						String def5 = aggvo.getParentVO().getDef5();//�����ܹ�ģ
						event.getBillCardPanel().getHeadItem("contractmoney").setValue(def5);//��ͬ���
					}else{
						event.getBillCardPanel().getHeadItem("def4").setEnabled(true);
						event.getBillCardPanel().getHeadItem("def4").setNull(true);
						event.getBillCardPanel().getHeadItem("contractmoney").setEnabled(true);
					}
				} catch (BusinessException e) {
					MessageDialog.showErrorDlg(this.billfrom, "����", e.getMessage());
				}
			}
			if("pk_payer".equals(event.getKey())){
				try {
					if(event.getBillCardPanel().getHeadItem("def12").getValueObject() != null){
						event.getBillCardPanel().getHeadItem("paymentamount").setValue(null);
						event.getBillCardPanel().getHeadItem("def1").setValue(null);
						event.getBillCardPanel().getHeadItem("def6").setValue(null);//�����̯���
						
						String pk_sigleissue = (String)event.getBillCardPanel().getHeadItem("def12").getValueObject();
						AggSingleIssueVO aggvo=(AggSingleIssueVO)getBillVO(AggSingleIssueVO.class,"nvl(dr,0) = 0 and pk_singleissue='"+pk_sigleissue+"'");
						String pk_payer = (String)event.getValue();
						if(pk_payer != null){
							ConstateExeVO[] cEvos = (ConstateExeVO[])aggvo.getChildren(ConstateExeVO.class);
							UFDouble sum = UFDouble.ZERO_DBL.setScale(2, UFDouble.ROUND_HALF_UP);
							if(cEvos != null){
								for (ConstateExeVO cvo : cEvos) {
									if(pk_payer.equals(cvo.getDef1())){
										sum = sum.add(new UFDouble(cvo.getDef3()),2);
									}
								}
							}
							event.getBillCardPanel().getHeadItem("paymentamount").setValue(sum);//�ۼ��Ѹ����
							
							ContractStateVO[] cTvos = (ContractStateVO[])aggvo.getChildren(ContractStateVO.class);
							sum = UFDouble.ZERO_DBL.setScale(2, UFDouble.ROUND_HALF_UP);
							if(cTvos != null){
								for (ContractStateVO vo : cTvos) {
									if(pk_payer.equals(vo.getDef1())){
										sum = sum.add(new UFDouble(vo.getDef2()),2);
									}
								}
							}
							event.getBillCardPanel().getHeadItem("def6").setValue(sum);//�����̯���
						}
					}
				} catch (BusinessException e) {
					MessageDialog.showErrorDlg(this.billfrom, "����", e.getMessage());
				}
			}
		}
		//add by tjl 2020-05-25
		//֧����ʽ
		if("def42".equals(event.getKey())){
			String pk_defdoc = (String) panel.getHeadItem("def42").getValueObject();
			String code = null;
			try {
				code = getCodeByPk_defdoc(pk_defdoc);
			} catch (BusinessException e) {
				MessageDialog.showErrorDlg(this.billfrom, "����", e.getMessage());
			}
			if("14".equals(code)){//��
				panel.getHeadItem("def7").setNull(false);//�տ������˻�
				panel.getHeadItem("pk_payee").setNull(false);//�տλ
			}else{
				panel.getHeadItem("def7").setNull(true);//�տ������˻�
				panel.getHeadItem("pk_payee").setNull(true);//�տλ
			}
		}
		//end
	}
	
	private String getCodeByPk_defdoc(String pk_defdoc) throws BusinessException {
		StringBuffer query = new StringBuffer();
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		query.append("select code from bd_defdoc where pk_defdoc = '"+pk_defdoc+"' and dr = 0 and  enablestate = 2");
		String code = (String) bs.executeQuery(query.toString(), new ColumnProcessor());
		return code==null?null:code;
	}

	/**
	 * ��ѯ���ݾۺ�VO
	 */
	@SuppressWarnings("rawtypes")
	private AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, true,false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
}
