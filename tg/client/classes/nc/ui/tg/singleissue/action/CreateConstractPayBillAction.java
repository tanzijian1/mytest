package nc.ui.tg.singleissue.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Action;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pubapp.uif2app.actions.AbstractBodyTableExtendAction;
import nc.ui.pubapp.uif2app.actions.intf.ICardPanelDefaultActionProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.GroupCreditBVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepaleyBVO;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.GroupCreditVO;
import nc.vo.tg.singleissue.RepaymentPlanVO;
/**
 * 表体生成贷款合同还款单按钮
 * @author wenjie
 *
 */
public class CreateConstractPayBillAction extends AbstractBodyTableExtendAction
implements ICardPanelDefaultActionProcessor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7718767623318093049L;
	IMDPersistenceQueryService mdQryService = null;
	
	/**
	 * 元数据持久化查询接口
	 * 
	 * @return
	 */
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}
	IUAPQueryBS query = null;

	public IUAPQueryBS getQuery() {
		if (query == null) {
			query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return query;
	}
	
	IPFBusiAction pfBusiAction = null;

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}
	
	public CreateConstractPayBillAction(){
		setCode("PushRepayBill");
		setBtnName("生成贷款合同还款单");
		String incon_url = "themeres/qrytemplate/next.png";
		nc.ui.uif2.actions.ActionInfo info = new nc.ui.uif2.actions.ActionInfo(
				"PushRepayBill", "生成贷款合同还款单", null, incon_url, null);
		this.setIcon(info.getIcon());
		this.putValue(Action.SHORT_DESCRIPTION, "生成贷款合同还款单");
	}
	@Override
	public int getType() {
		return 0;
	}

	@Override
	public void doAction() {
		String uiState = getModel().getUiState().toString();
		if("EDIT".equals(uiState) || "ADD".equals(uiState)){
			ExceptionUtils.wrappBusinessException("请保存单据后再推动生成贷款合同还款单");
		}
		if(getModel().getSelectedData()==null){
			ExceptionUtils.wrappBusinessException("请选择一条记录");
		}
		String tabCode = getCardPanel().getBodyPanel().getTableCode();
		AggSingleIssueVO aggvo = (AggSingleIssueVO) getModel().getSelectedData();
		int[] selectedRows = getCardPanel().getBodyPanel(tabCode).getTable().getSelectedRows();
		if(aggvo.getParentVO().getApprovestatus()!=1)ExceptionUtils.wrappBusinessException("单据必须为审批状态单据");
		if("pk_repayplan".equals(tabCode)){
			if(selectedRows.length==0){
				ExceptionUtils.wrappBusinessException("请选择至少一条【还款计划】数据");
			}
		}else if("pk_bondresale".equals(tabCode)){
			if(selectedRows.length==0){
				ExceptionUtils.wrappBusinessException("请选择至少一条【债券回售】数据");
			}
		}
		AggMarketRepalayVO addAggvo = addMarketRepalayVO(aggvo,tabCode);
		DefaultLinkData userdata = new DefaultLinkData();
		userdata.setBillType("huankuan");
		userdata.setUserObject(addAggvo);
		FuncletInitData initdata = new FuncletInitData();
		initdata.setInitType(ILinkType.LINK_TYPE_ADD);
		initdata.setInitData(userdata);
		BilltypeVO billType = PfDataCache.getBillType("SD08");
		FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()
				.getFuncRegisterVO(billType.getNodecode());
		FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
				.getEntranceUI(), registerVO, initdata, null, true, true);
	}
	private AggMarketRepalayVO addMarketRepalayVO(AggSingleIssueVO aggvo,String tabCode){
		//获取当前用户的用户id
		String userid = InvocationInfoProxy.getInstance().getUserId();
		AggMarketRepalayVO reAggvo = new AggMarketRepalayVO();
		MarketRepalayVO headervo = new MarketRepalayVO();
		try {
			//设置表头默认值
			headervo.setDr(0);
			headervo.setStatus(VOStatus.NEW);
			headervo.setPk_org_v(aggvo.getParentVO().getPk_org_v());
			headervo.setPk_group(aggvo.getParentVO().getPk_group());
			headervo.setPk_org(aggvo.getParentVO().getPk_org());//财务组织
			headervo.setBilltype("SD08");//单据类型
			headervo.setDef1(getUserInfoByID(userid)==null?null:getUserInfoByID(userid).get("pk_psndoc"));//申请人
			headervo.setDef3(getUserInfoByID(userid)==null?null:getUserInfoByID(userid).get("pk_org"));//申请公司
			headervo.setDef4(getUserInfoByID(userid)==null?null:getUserInfoByID(userid).get("pk_dept"));//申请部门
			headervo.setDef5(aggvo.getPrimaryKey());//合同编号(合同pk)
			headervo.setDef6(aggvo.getParentVO().getDef4());//项目名称
			//headervo.setDef9(aggvo.getParentVO().getPk_org());//付款单位
			headervo.setBilldate(new UFDate());//单据日期
			headervo.setCreationtime(new UFDateTime());//创建时间
			headervo.setCreator(userid);//创建人
			headervo.setBillmaker(userid);//制单人
			headervo.setApprovestatus(-1);//审批状态
			headervo.setDef15("N");//本次流程是否产生发票
			headervo.setDef34("N");//驳回是否通知BPM
			headervo.setDef36("N");//是否补附件
			headervo.setDef37("N");//是否补全附件
			headervo.setDef39("N");//是否结算完成归档
			headervo.setDef5(aggvo.getParentVO().getPrimaryKey());//单期发行编号
			headervo.setDef11(aggvo.getParentVO().getDef5());//单期发行总规模
			reAggvo.setParentVO(headervo);
			//设置集团授信
			GroupCreditVO[] groupVOs = (GroupCreditVO[]) aggvo.getChildren(GroupCreditVO.class);
			ArrayList<GroupCreditBVO> groupList = new ArrayList<GroupCreditBVO>();
			if(groupVOs!=null && groupVOs.length>0){
				for (GroupCreditVO groupVO : groupVOs) {
					GroupCreditBVO gvo = new GroupCreditBVO();
					gvo.setDef1(groupVO.getDef1());//集团授信协议
					gvo.setDef2(groupVO.getDef2());//授信银行
					gvo.setDef3(groupVO.getDef3());//是否释放授信
					gvo.setDef4(groupVO.getDef4());//释放授信金额
					groupList.add(gvo);
				}
				reAggvo.setChildrenVO(groupList.toArray(new GroupCreditBVO[0]));
			}
			
			if("pk_repayplan".equals(tabCode)){
				int[] selectedRows = getCardPanel().getBodyPanel(tabCode).getTable().getSelectedRows();
				UFDouble rep = UFDouble.ZERO_DBL;//还本金额
				UFDouble inte = UFDouble.ZERO_DBL;//还息金额
				ArrayList<MarketRepaleyBVO> list = new ArrayList<>();
				int rowNo = 0;
				for (int index : selectedRows) {
					rowNo+=10;
					MarketRepaleyBVO bodyvo = new MarketRepaleyBVO();
					UFDouble reamount = (UFDouble)getCardPanel().getBillModel(tabCode).getValueAt(index, "def3");
					reamount = reamount==null?UFDouble.ZERO_DBL:reamount;//还本
					if(reamount.doubleValue()<0){
						ExceptionUtils.wrappBusinessException("债券转售不可生成还款单");
					}
					UFDouble inamount = (UFDouble)getCardPanel().getBillModel(tabCode).getValueAt(index, "def4");
					inamount = inamount==null?UFDouble.ZERO_DBL:inamount;//还息
					
					UFDouble amount = UFDouble.ZERO_DBL;//还本
					UFDouble interest = UFDouble.ZERO_DBL;//还息
					String pk_repayplan = (String)getCardPanel().getBillModel(tabCode).getValueAt(index, "pk_repayplan");
					String sql = "select a1.def4,a1.def5 from sdfn_marketrepaley_b a1 left join sdfn_marketrepalay a2 "
									+ " on a1.pk_marketreplay = a2.pk_marketreplay  where a1.def1='"+pk_repayplan+"' "
									+ " and nvl(a1.dr,0)=0 and a2.approvestatus=1";
					List<Map<String, String>> mapList = (List<Map<String, String>>) getQuery().executeQuery(sql.toString(), new MapListProcessor());
					for (Map<String, String> map : mapList) {
						amount = amount.add(map.get("def4")==null?UFDouble.ZERO_DBL:new UFDouble(map.get("def4")));
						interest = interest.add(map.get("def5")==null?UFDouble.ZERO_DBL:new UFDouble(map.get("def5")));
					}
					//还款计划编号
					String no= (String)getCardPanel().getBillModel(tabCode).getValueAt(index, "def1");
					if(amount.compareTo(reamount)==0 && interest.compareTo(inamount)==0){
						ExceptionUtils.wrappBusinessException("编号【"+no+"】的本息已还完");
					}
					if(amount.compareTo(reamount)>0){
						ExceptionUtils.wrappBusinessException("编号【"+no+"】的本金已还完");
					}
					if(interest.compareTo(inamount)>0){
						ExceptionUtils.wrappBusinessException("编号【"+no+"】的利息已还完");
					}
					bodyvo.setDr(0);
					bodyvo.setRowno(rowNo+"");
					bodyvo.setDef1(pk_repayplan);//还款计划编号
					bodyvo.setDef2(getCardPanel().getBillModel(tabCode).getValueAt(index, "def2").toString());//还款日期
					bodyvo.setDef3(reamount.toString());//预还本金
					bodyvo.setDef4(reamount.sub(amount,2).toString());//还本金
					bodyvo.setDef5(inamount.sub(interest, 2).toString());//还息金额
					rep = rep.add(reamount.sub(amount),2);
					inte = inte.add(inamount.sub(interest),2);
					list.add(bodyvo);
				}
				headervo.setDef23(rep.toString());
				headervo.setDef24(inte.toString());
				reAggvo.setChildrenVO(list.toArray(new MarketRepaleyBVO[0]));
			}
			if("pk_bondresale".equals(tabCode)){
				int[] selectedRows = getCardPanel().getBodyPanel(tabCode).getTable().getSelectedRows();
				UFDouble rep = UFDouble.ZERO_DBL;//还本金额
				UFDouble inte = UFDouble.ZERO_DBL;//还息金额
				ArrayList<MarketRepaleyBVO> list = new ArrayList<>();
				int rowNo = 0;
				for (int index : selectedRows) {
					//回售日期
					UFDate sdate = (UFDate)getCardPanel().getBillModel(tabCode).getValueAt(index, "def1");
					if(sdate!=null){
						UFDate redate = sdate.asBegin();
						RepaymentPlanVO[] vos = (RepaymentPlanVO[])aggvo.getChildren(RepaymentPlanVO.class);
						for (RepaymentPlanVO vo : vos) {
							//还款日期与回售日期相同的数据，写入到还款计划
							if(new UFDate(vo.getDef2()).asBegin().compareTo(redate)==0){
								rowNo+=10;
								UFDouble amount = UFDouble.ZERO_DBL;//还本
								String sql = "select a1.def4,a1.def5 from sdfn_marketrepaley_b a1 left join sdfn_marketrepalay a2 "
										+ " on a1.pk_marketreplay = a2.pk_marketreplay  where a1.def1='"+vo.getPk_repayplan()+"' "
										+ " and nvl(a1.dr,0)=0 and a2.approvestatus=1";
								List<Map<String, String>> mapList = (List<Map<String, String>>) getQuery().executeQuery(sql.toString(), new MapListProcessor());
								for (Map<String, String> map : mapList) {
									amount = amount.add(map.get("def4")==null?UFDouble.ZERO_DBL:new UFDouble(map.get("def4")));
								}
								if(amount.compareTo(vo.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef3()))>=0){
									ExceptionUtils.wrappBusinessException("编号【"+vo.getDef1()+"】的本金已还完");
								}
								MarketRepaleyBVO bodyvo = new MarketRepaleyBVO();
								bodyvo.setRowno(rowNo+"");
								bodyvo.setDef1(vo.getPk_repayplan());
								bodyvo.setDef2(vo.getDef2());
								bodyvo.setDef3(vo.getDef3()==null?"0.00":vo.getDef3());
								bodyvo.setDef4(vo.getDef3()==null?"0.00":vo.getDef3());
								bodyvo.setDef5(vo.getDef4()==null?"0.00":vo.getDef4());
								bodyvo.setDr(0);
								rep = rep.add(vo.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef3()),2);
								inte = inte.add(vo.getDef4()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef4()),2);
								list.add(bodyvo);
							}
						}
					}
				}
				headervo.setDef23(rep.toString());
				headervo.setDef24(inte.toString());
				reAggvo.setChildrenVO(list.toArray(new MarketRepaleyBVO[0]));
			}
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return reAggvo;
	}
	@Override
	public boolean isEnabled() {
		// 设置按钮可点击
		if(getModel()==null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 读制单人信息
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getUserInfoByID(String cuserid)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select bd_psndoc.pk_psndoc pk_psndoc,bd_psndoc.name psnname,org_dept.pk_dept pk_dept,org_dept.name deptname,org_dept.code deptcode,bd_psnjob.pk_org pk_org from sm_user  ");
		sql.append(" left join bd_psndoc on sm_user.pk_psndoc = bd_psndoc.pk_psndoc ");
		sql.append(" left join bd_psnjob on sm_user.pk_psndoc = bd_psnjob.pk_psndoc and bd_psnjob.ismainjob = 'Y' ");
		sql.append(" left join org_dept on org_dept.pk_dept = bd_psnjob.pk_dept ");
		sql.append(" where sm_user.cuserid = '"+cuserid+"' ");
		Map<String, String> userInfo = (Map<String, String>) getQuery()
				.executeQuery(sql.toString(), new MapProcessor());
		return userInfo;
	}
	
}
