package nc.ui.tg.approvalpro.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.util.SdfnUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public class AceHeadTailAfterEditHandler  implements IAppEventHandler<CardHeadTailAfterEditEvent>{
	private  IUAPQueryBS query = null;

	public  IUAPQueryBS getQuery() {
		if (query == null) {
			query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return query;
	}
	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		// TODO 自动生成的方法存根
		if(e.getKey().equals("def1")){
			try {
				String pk = (String)e.getValue();
				String name = getNameBypk(pk);
				if(SdfnUtil.getABSList().contains(name) || SdfnUtil.getDomDebtList().contains(name)){
					UFDate billdate = (UFDate) e.getBillCardPanel()
							.getHeadTailItem("billdate").getValueObject();
					if(billdate==null){
						nc.vo.pubapp.pattern.exception.ExceptionUtils
						.wrappBusinessException("该单据日期为空,请先填写单据日期后再操作");
					}
					if(e.getBillCardPanel().getBillModel("pk_progress").getRowCount()>0){
						e.getBillCardPanel().getBillModel("pk_progress").delLine(new int[e.getBillCardPanel().getBillModel("pk_progress").getRowCount()]);
					}

					e.getBillCardPanel().getBillModel("pk_progress").addLine();
					e.getBillCardPanel().getBillModel("pk_progress").addLine();
					e.getBillCardPanel().getBillModel("pk_progress").addLine();
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("计划时间", 0,
							"def1");
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("10", 0,
							"rowno");//添加行号，表体数据顺序按照行号排序
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("实际时间", 1,
							"def1");
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("20", 1,
							"rowno");//添加行号，表体数据顺序按照行号排序
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("天数", 2,
							"def1");
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("30", 2,
							"rowno");//添加行号，表体数据顺序按照行号排序
					e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
				}else{
					e.getBillCardPanel().getBillModel("pk_progress").clearBodyData();
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
		}
		if("def19".equals(e.getKey())){//发行额度带出剩余可发行额度
			e.getBillCardPanel().getHeadItem("def7").setValue(e.getValue());
		}
	}
	
	private String getNameBypk(String pk_fintype) throws BusinessException{
		if(pk_fintype == null){
			return null;
		}else{
			String sql ="select name from tgrz_fintype where pk_fintype='"+pk_fintype+"' and nvl(dr,0)=0";
			String result = (String)getQuery().executeQuery(sql, new ColumnProcessor());
			return result;
		}
	}
}
