package nc.ui.tg.interestshare.action;

import java.awt.event.ActionEvent;
import java.util.List;

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
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.vo.tgfn.interestshare.IntshareHead;
import uap.web.exception.BusinessException;

public class LinkQueryInshareAction extends LinkQueryAction{
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		Object obj=getModel().getSelectedData();
		if(obj==null){
			throw new BusinessException("未选中数据");
		}
		IUAPQueryBS query=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		AggIntshareHead aggvo=(AggIntshareHead)obj;
		IntshareHead hvo=aggvo.getParentVO();
		if("FN24-Cxx-01".equals(aggvo.getParentVO().getTranstype())){//对内闲置利息-放贷
		  List<String> pklist=(List<String>)query.executeQuery("select pk_intsharehead from tgfn_intsharehead where def10='"+hvo.getBillno()+"'", new ColumnListProcessor());
		  if(pklist==null||pklist.size()<1) 
		  throw new BusinessException("该单据未找到对应单据");
		  DefaultLinkData userdata = new DefaultLinkData();
			userdata.setBillIDs(pklist.toArray(new String[0]));
			FuncletInitData initdata = new FuncletInitData();
			initdata.setInitType(ILinkType.LINK_TYPE_QUERY);
		
			initdata.setInitData(userdata);
			BilltypeVO billType = PfDataCache.getBillType("FN24-Cxx-02");
			FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()
					.getFuncRegisterVO(billType.getNodecode());
			FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
					.getEntranceUI(), registerVO, initdata, null, true, true);
		}else{
			  List<String> pklist=(List<String>)query.executeQuery("select def10 from tgfn_intsharehead where pk_intsharehead='"+hvo.getPk_intsharehead()+"'", new ColumnListProcessor());
			  if(pklist==null||pklist.size()<1) 
				  throw new BusinessException("该单据未找到对应单据");
			  DefaultLinkData userdata = new DefaultLinkData();
				userdata.setBillIDs(pklist.toArray(new String[0]));
				FuncletInitData initdata = new FuncletInitData();
				initdata.setInitType(ILinkType.LINK_TYPE_QUERY);
				initdata.setInitData(userdata);
				BilltypeVO billType = PfDataCache.getBillType("FN24-Cxx-01");
				FuncRegisterVO registerVO = WorkbenchEnvironment.getInstance()
						.getFuncRegisterVO(billType.getNodecode());
				FuncletWindowLauncher.openFuncNodeDialog(getModel().getContext()
						.getEntranceUI(), registerVO, initdata, null, true, true);
		}
	}

}
