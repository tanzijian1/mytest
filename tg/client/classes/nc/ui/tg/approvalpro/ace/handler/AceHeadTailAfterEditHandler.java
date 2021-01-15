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
		// TODO �Զ����ɵķ������
		if(e.getKey().equals("def1")){
			try {
				String pk = (String)e.getValue();
				String name = getNameBypk(pk);
				if(SdfnUtil.getABSList().contains(name) || SdfnUtil.getDomDebtList().contains(name)){
					UFDate billdate = (UFDate) e.getBillCardPanel()
							.getHeadTailItem("billdate").getValueObject();
					if(billdate==null){
						nc.vo.pubapp.pattern.exception.ExceptionUtils
						.wrappBusinessException("�õ�������Ϊ��,������д�������ں��ٲ���");
					}
					if(e.getBillCardPanel().getBillModel("pk_progress").getRowCount()>0){
						e.getBillCardPanel().getBillModel("pk_progress").delLine(new int[e.getBillCardPanel().getBillModel("pk_progress").getRowCount()]);
					}

					e.getBillCardPanel().getBillModel("pk_progress").addLine();
					e.getBillCardPanel().getBillModel("pk_progress").addLine();
					e.getBillCardPanel().getBillModel("pk_progress").addLine();
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("�ƻ�ʱ��", 0,
							"def1");
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("10", 0,
							"rowno");//����кţ���������˳�����к�����
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("ʵ��ʱ��", 1,
							"def1");
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("20", 1,
							"rowno");//����кţ���������˳�����к�����
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("����", 2,
							"def1");
					e.getBillCardPanel().getBillModel("pk_progress").setValueAt("30", 2,
							"rowno");//����кţ���������˳�����к�����
					e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
				}else{
					e.getBillCardPanel().getBillModel("pk_progress").clearBodyData();
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
		}
		if("def19".equals(e.getKey())){//���ж�ȴ���ʣ��ɷ��ж��
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
