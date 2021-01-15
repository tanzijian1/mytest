package nc.bs.tg.capitalmarketrepay.ace.bp.rule;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.pf.IPfExchangeService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.pf.IplatFormEntry;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.cc.grpprotocol.AggGroupProtocolVO;
import nc.vo.cmp.bill.BillAggVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.GroupCreditBVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;
import nc.vo.tg.singleissue.BondTransSaleVO;
import nc.vo.trade.pub.IBillStatus;

public class WriteBackRule implements IRule<AggMarketRepalayVO>{
	/**
	 * 元数据持久化查询接口
	 * 
	 * @return
	 */
	private IPfExchangeService ips = null;
	IMDPersistenceQueryService mdQryService = null;
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}
	private BaseDAO baseDAO = null;
	private BaseDAO getBaseDAO(){
		if(baseDAO==null){
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	@Override
	public void process(AggMarketRepalayVO[] vos) {
		// TODO 自动生成的方法存根
		if(vos[0].getParentVO().getApprovestatus() == IBillStatus.CHECKPASS){
			MarketRepalayVO parentVO = vos[0].getParentVO();
			String isPush = parentVO.getDef43();
			if("Y".equals(isPush)){
				try {
					this.afterPush(vos[0]);
				} catch (BusinessException e) {
					ExceptionUtils.wrappBusinessException(e.getMessage());
				}
			}else{
				writeBackToSingleIssue(vos);//回写合同执行情况
			}
			writeBackToProtocol(vos);//回写授信
		}
		
	}
	/**
	 * 推动生成付款结算单
	 * @param aggvo
	 * @throws BusinessException 
	 */
	private void afterPush(AggMarketRepalayVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		MarketRepalayVO parentVO = aggvo.getParentVO();
		InvocationInfoProxy.getInstance().setGroupId(parentVO.getPk_group());
		BillAggVO[] vo = (BillAggVO[]) getPfExchangeService()
				.runChangeDataAryNeedClassify("SD08", "F5",
						new AggMarketRepalayVO[] { aggvo }, null, 2);
		Boolean istrue = vo == null || vo.length == 0;
		if (istrue) {
			throw new BusinessException("资本市场还款["
					+ parentVO.getBilltype()
					+ "]转换付款结算单异常 [未转换成功]!");
		}
		
		IplatFormEntry plat = (IplatFormEntry) NCLocator.getInstance()
				.lookup(IplatFormEntry.class);
		IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
				IWorkflowMachine.class);
		WorkflownoteVO worknoteVO = iWorkflowMachine.checkWorkFlow("SAVE",
				"F5", vo[0], null);
		plat.processAction("SAVE", "F5",worknoteVO, vo[0], null, null);
	}
	/**
	 * 回写到集团授信
	 * @param vos
	 */
	private void writeBackToProtocol(AggMarketRepalayVO[] vos){
		GroupCreditBVO[] bvos = (GroupCreditBVO[])vos[0].getChildren(GroupCreditBVO.class);
		for (GroupCreditBVO bvo : bvos) {
			if(bvo.getDef1() != null && "Y".equals(bvo.getDef3())){
				try {
					AggGroupProtocolVO aggvoPro = (AggGroupProtocolVO)getBillVO(AggGroupProtocolVO.class,"nvl(dr,0) = 0 and pk_bankprotocol='"+bvo.getDef1()+"'");
					if(aggvoPro != null){
						UFDouble curusdcdtlnamt = aggvoPro.getParentVO().getCurusdcdtlnamt();
						curusdcdtlnamt = curusdcdtlnamt==null?UFDouble.ZERO_DBL:curusdcdtlnamt;//已用授信额度
						UFDouble availcdtlnamt = aggvoPro.getParentVO().getAvailcdtlnamt();//未用授信额度
						availcdtlnamt = availcdtlnamt==null?UFDouble.ZERO_DBL:availcdtlnamt;
						
						UFDouble def4 = bvo.getDef4()==null?UFDouble.ZERO_DBL:new UFDouble(bvo.getDef4());
						//回写授信情况
						aggvoPro.getParentVO().setCurusdcdtlnamt(curusdcdtlnamt.sub(def4,2));
						aggvoPro.getParentVO().setAvailcdtlnamt(availcdtlnamt.add(def4, 2));
						getBaseDAO().updateVO(aggvoPro.getParentVO());
					}
				} catch (BusinessException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					ExceptionUtils.wrappBusinessException(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 回写到单期发行情况
	 */
	private void writeBackToSingleIssue(AggMarketRepalayVO[] vos){
		try {
			MarketRepalayVO parentVO = vos[0].getParentVO();
			BondTransSaleVO bvo = new BondTransSaleVO();
			bvo.setPk_singleissue(parentVO.getDef5());
			bvo.setDr(0);
			bvo.setDef1(parentVO.getBillno());//单据编号
			bvo.setDef3(parentVO.getDef22());//日期
			bvo.setDef6(parentVO.getDef23());//已还本金
			UFDouble total = parentVO.getDef11()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef11());//发行总规模
			String sql = "select def6 from sdfn_bondtranssale where pk_singleissue = '"+parentVO.getDef5()+"' and nvl(dr,0)=0";
			List<Map<String,String>> maplist = (List<Map<String,String>>)this.getBaseDAO().executeQuery(sql, new MapListProcessor());
			for (Map<String, String> map : maplist) {
				total = total.sub(map.get("def6")==null?UFDouble.ZERO_DBL:new UFDouble(map.get("def6")));
			}
			UFDouble repaid = parentVO.getDef23()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef23());
			bvo.setDef7(total.sub(repaid,2).toString());//未还本金
			bvo.setDef8(parentVO.getDef24());//已还利息
			bvo.setDef9(parentVO.getDef18());//发票金额
			bvo.setDef10(parentVO.getDef12());//支付金额
			UFDouble repaidtax = parentVO.getDef24()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef24());
			if(repaid.toDouble()>0 && repaidtax.toDouble()>0){
				bvo.setDef2("还本还息");
			}else if(repaid.toDouble()>0 && repaidtax.toDouble()<=0){
				bvo.setDef2("还本");
			}else if(repaid.toDouble()<=0 && repaidtax.toDouble()>0){
				bvo.setDef2("还息");
			}
			getBaseDAO().insertVO(bvo);
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}
	
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
	/**
	 * VO转换服务类
	 * 
	 * @return
	 */
	public IPfExchangeService getPfExchangeService() {
		if (ips == null) {
			ips = NCLocator.getInstance().lookup(IPfExchangeService.class);
		}
		return ips;
	}
}
