package nc.ui.tg.internalinterest.action;

import java.awt.event.ActionEvent;
import java.util.List;

import uap.web.exception.BusinessException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pubapp.uif2app.actions.LinkQueryAction;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Internalinterest;

public class LinkQuerySelfAction extends LinkQueryAction{
@Override
public void doAction(ActionEvent e) throws Exception {
	// TODO 自动生成的方法存根
	Object obj=getModel().getSelectedData();
	if(obj==null){
		throw new BusinessException("未选中数据");
	}
	IUAPQueryBS query=NCLocator.getInstance().lookup(IUAPQueryBS.class);
	AggInternalinterest aggvo=(AggInternalinterest)obj;
	Internalinterest hvo=aggvo.getParentVO();
	if("FN06-Cxx-01".equals(aggvo.getParentVO().getTranstype())){//对内计息工单-放贷
	  List<String> pklist=(List<String>)query.executeQuery("select pk_internal from tgfn_internalinterest where def10='"+hvo.getPk_internal()+"'", new ColumnListProcessor());
	  if(pklist==null||pklist.size()<1) 
	  throw new BusinessException("该单据未找到对应单据");
	  DefaultLinkData userdata = new DefaultLinkData();
		userdata.setBillIDs(pklist.toArray(new String[0]));
		FuncletInitData initdata = new FuncletInitData();
		initdata.setInitType(ILinkType.LINK_TYPE_QUERY);
	
		initdata.setInitData(userdata);
		BilltypeVO billType = PfDataCache.getBillType("FN06-Cxx-02");
		FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()
				.getFuncRegisterVO(billType.getNodecode());
		FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
				.getEntranceUI(), registerVO, initdata, null, true, true);
	}else{
		  List<String> pklist=(List<String>)query.executeQuery("select def10 from tgfn_internalinterest where pk_internal='"+hvo.getPk_internal()+"'", new ColumnListProcessor());
		  if(pklist==null||pklist.size()<1) 
			  throw new BusinessException("该单据未找到对应单据");
		  DefaultLinkData userdata = new DefaultLinkData();
			userdata.setBillIDs(pklist.toArray(new String[0]));
			FuncletInitData initdata = new FuncletInitData();
			initdata.setInitType(ILinkType.LINK_TYPE_QUERY);
			initdata.setInitData(userdata);
			BilltypeVO billType = PfDataCache.getBillType("FN06-Cxx-01");
			FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()
					.getFuncRegisterVO(billType.getNodecode());
			FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
					.getEntranceUI(), registerVO, initdata, null, true, true);
	}
}
}
