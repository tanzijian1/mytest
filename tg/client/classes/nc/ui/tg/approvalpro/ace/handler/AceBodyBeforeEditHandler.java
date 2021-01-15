package nc.ui.tg.approvalpro.ace.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.util.SdfnUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class AceBodyBeforeEditHandler implements
		IAppEventHandler<CardBodyBeforeEditEvent> {
	IUAPQueryBS bs = null;
	private IUAPQueryBS getQB(){
		if(bs == null){
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
	}
	@Override
	public void handleAppEvent(CardBodyBeforeEditEvent e) {
		if("pk_progress".equals(e.getTableCode())){
			if(e.getRow() == 1){
				String pk_fintype = (String) e.getBillCardPanel().getHeadItem("def1").getValueObject();
				String nameBypk = null;
				try {
					nameBypk = this.getNameBypk(pk_fintype);
				} catch (BusinessException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
				if("def3".equals(e.getKey())){
					if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def2") == null){
						ExceptionUtils.wrappBusinessException("������д����ɾ�����ʵ��ʱ��ֵ");
					}
				}
				if("def4".equals(e.getKey())){
					if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def3") == null){
						ExceptionUtils.wrappBusinessException("������д���ύ��������ʵ��ʱ��ֵ");
					}
				}
				if("def5".equals(e.getKey())){
					if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def4") == null){
						ExceptionUtils.wrappBusinessException("������д������������ͨ����ʵ��ʱ��ֵ");
					}
				}
				if(SdfnUtil.getABSList().contains(nameBypk) ){
					if("def8".equals(e.getKey())){
						if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def5") == null){
							ExceptionUtils.wrappBusinessException("ҵ������ΪABS��������д���ļ����ʵ��ʱ��ֵ");
						}
					}
				}else{
					if("def6".equals(e.getKey())){
						if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def5") == null){
							ExceptionUtils.wrappBusinessException("ҵ������Ϊծȯ��������д���ļ����ʵ��ʱ��ֵ");
						}
					}
					if("def7".equals(e.getKey())){
						if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def6") == null){
							ExceptionUtils.wrappBusinessException("ҵ������Ϊծȯ��������д���ύ֤��᡿ʵ��ʱ��ֵ");
						}
					}
					if("def8".equals(e.getKey())){
						if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def7") == null){
							ExceptionUtils.wrappBusinessException("ҵ������Ϊծȯ��������д��֤�������ͨ����ʵ��ʱ��ֵ");
						}
					}
				}
				e.setReturnValue(true);
			}else{
				e.setReturnValue(false);
			}
		}else{
			e.setReturnValue(false);
		}
	}
	private String getNameBypk(String pk_fintype) throws BusinessException{
		String sql ="select name from tgrz_fintype where pk_fintype='"+pk_fintype+"' and nvl(dr,0)=0";
		String result = (String)getQB().executeQuery(sql, new ColumnProcessor());
		return result;
	}

}