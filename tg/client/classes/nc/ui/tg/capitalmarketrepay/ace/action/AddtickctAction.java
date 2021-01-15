package nc.ui.tg.capitalmarketrepay.ace.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.pubapp.AppContext;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tg.addticket.AddTicket;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.addticket.Ticketbody;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;

public class AddtickctAction extends NCAction{
	private BillManageModel model;
	public BillManageModel getModel() {
		return model;
	}
	public void setModel(BillManageModel model) {
		this.model = model;
	}
	/**
	 * 
	 */
	private IUAPQueryBS bs=null;
	private IUAPQueryBS getIUAPQuery(){
		if(bs==null){
			bs=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
	}
	private static final long serialVersionUID = 8889354496529340026L;
    public AddtickctAction(){
    	setBtnName("仅补票");
    	setCode("addtickctAction");
    }
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		AggMarketRepalayVO aggvo=(AggMarketRepalayVO)getModel().getSelectedData();
		validData(aggvo);
		MarketRepalayVO pvo = aggvo.getParentVO();
		if(pvo.getApprovestatus()!=1)throw new BusinessException("单据必须为审批状态单据");
		AggAddTicket addvo=getaddticketVO(aggvo);
		DefaultLinkData userdata = new DefaultLinkData();
		
		userdata.setBillType("qingkuan");
		userdata.setUserObject(addvo);
		FuncletInitData initdata = new FuncletInitData();
		initdata.setInitType(ILinkType.LINK_TYPE_ADD);
		initdata.setInitData(userdata);
		BilltypeVO billType = PfDataCache.getBillType("RZ30");
		FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()
				.getFuncRegisterVO(billType.getNodecode());
		FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
				.getEntranceUI(), registerVO, initdata, null, true, true);
	}
	
	
	
	private AggAddTicket getaddticketVO(AggMarketRepalayVO aggvo) throws BusinessException{
		getIUAPQuery();
		MarketRepalayVO pVO = aggvo.getParentVO();
		AggAddTicket addaggvo=new AggAddTicket();
		AddTicket hvo=new AddTicket();
		Ticketbody tbvo=new Ticketbody();
		addaggvo.setParentVO(hvo);
		hvo.setPk_group(AppContext.getInstance().getPkGroup());
		hvo.setBilldate(new UFDate());
		hvo.setBilltype("RZ30");
		hvo.setCreator(AppContext.getInstance().getPkUser());
		hvo.setBillmaker(AppContext.getInstance().getPkUser());
		hvo.setCreationtime(new UFDateTime());
		hvo.setPk_org(pVO.getDef9());
		hvo.setPk_org_v(pVO.getPk_org_v());
		hvo.setApprovestatus(-1);
		hvo.setMaketime(new UFDateTime());
		hvo.setTs(new UFDateTime());
		hvo.setDef1(getPk_defdocByNameORCode("005","RZ015"));
		hvo.setDef36(pVO.getDef5());//单期发行情况
		hvo.setDef18(getSingleIssueName(pVO.getDef5()));//合同名称
		hvo.setDef30(pVO.getDef12());//实付金额
		String flow=(String)bs.executeQuery("select pk_defdoc from bd_defdoc where bd_defdoc.pk_defdoclist= (select pk_defdoclist from   bd_defdoclist where code='zdy005') and code='0407'", new ColumnProcessor());//补票单设置默认流程
		hvo.setDef35(flow);//流程名称
		String sql="select d.pk_psndoc ,d.pk_dept,d.pk_org from sm_user c,bd_psnjob d where c.pk_psndoc = d.pk_psndoc and c.cuserid='"+ AppContext.getInstance().getPkUser()+"' and  d.ismainjob='Y' and d.dr=0";
		Object[] as=(Object[]) bs.executeQuery(sql, new ArrayProcessor());
		if(as!=null&&as.length>0){
			hvo.setDef24((String)as[0]);//申请人
			hvo.setDef25((String)as[1]);//申请部门
			hvo.setDef26((String)as[2]);//申请公司
		}
		tbvo.setDef1(pVO.getPk_marketreplay());
		tbvo.setDef2(pVO.getDef7());
		addaggvo.setChildrenVO(new Ticketbody[]{tbvo});
		return addaggvo;
	}
	/**
	 * 通过名称或编码自定义项得到pk
	 * @param name
	 * @param deflistcode
	 * @return
	 * @throws BusinessException
	 */
	public String getPk_defdocByNameORCode(String name,String deflistcode) throws BusinessException {
		String sql = "SELECT def.pk_defdoc FROM bd_defdoc def\n" +
						"WHERE (def.name = '"+name+"' or def.code='"+name+"') AND NVL(def.dr,0) = 0 and def.pk_defdoclist=(select bd_defdoclist.pk_defdoclist from bd_defdoclist where bd_defdoclist.code='"+deflistcode+"')";
		String pk_project = (String) getIUAPQuery().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}
	/**
	 *单期发行情况名称 
	 */
	private String getSingleIssueName(String pk) throws BusinessException{
		return (String)bs.executeQuery("select name from sdfn_singleissue where pk_singleissue='"+pk+"'", new ColumnProcessor());
	}
	@Override
	public boolean isEnabled() {
		// TODO 自动生成的方法存根
		if(getModel()!=null){
			if(getModel().getSelectedData()!=null){
				AggMarketRepalayVO aggvo=(AggMarketRepalayVO)getModel().getSelectedData();
				if(aggvo.getParentVO().getApprovestatus()==1){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 贷款合同名称
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	private String getContractName(String pk) throws BusinessException{
		return (String)this.getIUAPQuery().executeQuery("select htmc from cdm_contract where cdm_contract.pk_contract='"+pk+"'", new ColumnProcessor());
	}
	/**
	 * 校验数据
	 * @param aggvo
	 * @throws BusinessException 
	 */
	private void validData(AggMarketRepalayVO aggvo) throws BusinessException{
		String pk=aggvo.getParentVO().getPk_marketreplay();
		String obj=(String)getIUAPQuery().executeQuery("select to_char((sum(case when nvl(tg_ticketbody.def5,'~')='~' then 0 else to_number(tg_ticketbody.def5) end ))) money from tg_ticketbody "
				+ "inner join tg_addticket on tg_addticket.pk_ticket=tg_ticketbody.pk_ticket where tg_ticketbody.def1='"+
				pk+"'  and tg_addticket.approvestatus=1 and nvl(tg_addticket.dr,0)=0 and nvl(tg_ticketbody.dr,0)=0"  , new ColumnProcessor());
		if(obj!=null){
			String def12 = aggvo.getParentVO().getDef12();
			UFDouble amount = def12==null?UFDouble.ZERO_DBL:new UFDouble(def12);
			if(amount.compareTo(new UFDouble(obj))<1){
				throw new BusinessException("资本市场还款单本次请款金额必须大于生成仅补票单总金额");
			}
		}
	}
}
