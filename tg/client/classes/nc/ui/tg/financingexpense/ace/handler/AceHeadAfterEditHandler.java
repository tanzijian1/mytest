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
	 * 元数据持久化查询接口
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
		// TODO 自动生成的方法存根
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
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			panel.setHeadItem("transtype", transtypecode);
		}
		// 财务顾问费
		String pk_tradetype = (String) event.getBillCardPanel()
				.getHeadItem("transtype").getValueObject();
		if ("RZ06-Cxx-001".equals(pk_tradetype)) {
			if(event.getBillCardPanel().getHeadItem("def12").getValueObject() == null){
				//单期发行情况为空的情况下
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
						String sql = "select sum(m_ftje） from cdm_cwfyft  where pk_contract ='"
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
							
							// 对应申请公司累计申请金额
							UFDouble orgAmout = UFDouble.ZERO_DBL;
							// 对应合同累计申请金额
							UFDouble contractAmout = UFDouble.ZERO_DBL;
							for (CwgwfzxqkBVO vo : list) {
								if (payer.equals(vo.getAttributeValue("pk_zfgs"))) {
									orgAmout = orgAmout.add((UFDouble) vo
											.getAttributeValue("m_zfje"));
								}
								contractAmout = contractAmout.add((UFDouble) vo
										.getAttributeValue("m_zfje"));
							}
							panel.setHeadItem("def6", amount);// 设置分摊金额
							panel.setHeadItem("paymentamount", contractAmout);// 设置当前合同已累计金额
							panel.setHeadItem("def8", orgAmout);// 设置当前合同、申请公司下的已累计金额
							panel.setHeadItem("contractmoney", contractamount);// 设置合同金额
							
							ContractVO contVO = NCLocator
									.getInstance()
									.lookup(IContractQueryService.class)
									.queryContractMainByPk(
											(String) panel.getHeadItem(
													event.getKey())
													.getValueObject());
							if (contVO != null) {
								panel.setHeadItem("def16", contVO.getVdef16());// 项目竣备
							}
							
							//在财务顾问费请款，引用贷款合同时，自动带出：是否可抵扣（def25），抵扣税率信息(def26) 2020-04-08-谈子健-start
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
							MessageDialog.showErrorDlg(this.billfrom, "错误", e.getMessage());
						}
					}
				}
			}
			
		}
		
		//是否有发票
		//add by tjl 2020-05-19 
		if("def31".equals(event.getKey())){
			String pk_defdoc = (String) panel.getHeadItem("def31").getValueObject();
			String code = null;
			try {
				code = getCodeByPk_defdoc(pk_defdoc);
			} catch (BusinessException e) {
				MessageDialog.showErrorDlg(this.billfrom, "错误", e.getMessage());
			}
			if("01".equals(code)){//是
				panel.getHeadItem("def18").setNull(true);//设置必填
			}else{
				panel.getHeadItem("def18").setNull(false);
			}
		}
		//end
		
		if ("def4".equals(event.getKey())) {
			if ("RZ06-Cxx-001".equals(pk_tradetype)){
				//单期发行情况和银行贷款合同只能选择一个
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
				// TODO 自动生成的 catch 块
				MessageDialog.showErrorDlg(this.billfrom, "错误", e.getMessage());
			}
			if (contVO != null) {
				panel.setHeadItem("def16", contVO.getVdef16());// 项目竣备
			}
		}
		//吕文杰  引入单期发行情况
		if ("RZ06-Cxx-001".equals(pk_tradetype)){
			if("def12".equals(event.getKey())){
				event.getBillCardPanel().getHeadItem("pk_payer").setValue(null);//付款单位
				event.getBillCardPanel().getHeadItem("contractmoney").setValue(null);//合同金额
				event.getBillCardPanel().getHeadItem("paymentamount").setValue(null);//累计已付金额
				event.getBillCardPanel().getHeadItem("def1").setValue(null);//付款银行账户
				event.getBillCardPanel().getHeadItem("def6").setValue(null);//财务分摊金额
				try {
					if(event.getValue()!=null){
						//单期发行情况和银行贷款合同只能选择一个
						event.getBillCardPanel().getHeadItem("def4").setEnabled(false);
						event.getBillCardPanel().getHeadItem("def4").setNull(false);
						event.getBillCardPanel().getHeadItem("contractmoney").setEnabled(false);
						AggSingleIssueVO aggvo=(AggSingleIssueVO)getBillVO(AggSingleIssueVO.class,"nvl(dr,0) = 0 and pk_singleissue='"+event.getValue()+"'");
						String def5 = aggvo.getParentVO().getDef5();//发行总规模
						event.getBillCardPanel().getHeadItem("contractmoney").setValue(def5);//合同金额
					}else{
						event.getBillCardPanel().getHeadItem("def4").setEnabled(true);
						event.getBillCardPanel().getHeadItem("def4").setNull(true);
						event.getBillCardPanel().getHeadItem("contractmoney").setEnabled(true);
					}
				} catch (BusinessException e) {
					MessageDialog.showErrorDlg(this.billfrom, "错误", e.getMessage());
				}
			}
			if("pk_payer".equals(event.getKey())){
				try {
					if(event.getBillCardPanel().getHeadItem("def12").getValueObject() != null){
						event.getBillCardPanel().getHeadItem("paymentamount").setValue(null);
						event.getBillCardPanel().getHeadItem("def1").setValue(null);
						event.getBillCardPanel().getHeadItem("def6").setValue(null);//财务分摊金额
						
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
							event.getBillCardPanel().getHeadItem("paymentamount").setValue(sum);//累计已付金额
							
							ContractStateVO[] cTvos = (ContractStateVO[])aggvo.getChildren(ContractStateVO.class);
							sum = UFDouble.ZERO_DBL.setScale(2, UFDouble.ROUND_HALF_UP);
							if(cTvos != null){
								for (ContractStateVO vo : cTvos) {
									if(pk_payer.equals(vo.getDef1())){
										sum = sum.add(new UFDouble(vo.getDef2()),2);
									}
								}
							}
							event.getBillCardPanel().getHeadItem("def6").setValue(sum);//财务分摊金额
						}
					}
				} catch (BusinessException e) {
					MessageDialog.showErrorDlg(this.billfrom, "错误", e.getMessage());
				}
			}
		}
		//add by tjl 2020-05-25
		//支付方式
		if("def42".equals(event.getKey())){
			String pk_defdoc = (String) panel.getHeadItem("def42").getValueObject();
			String code = null;
			try {
				code = getCodeByPk_defdoc(pk_defdoc);
			} catch (BusinessException e) {
				MessageDialog.showErrorDlg(this.billfrom, "错误", e.getMessage());
			}
			if("14".equals(code)){//是
				panel.getHeadItem("def7").setNull(false);//收款银行账户
				panel.getHeadItem("pk_payee").setNull(false);//收款单位
			}else{
				panel.getHeadItem("def7").setNull(true);//收款银行账户
				panel.getHeadItem("pk_payee").setNull(true);//收款单位
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
	 * 查询单据聚合VO
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
