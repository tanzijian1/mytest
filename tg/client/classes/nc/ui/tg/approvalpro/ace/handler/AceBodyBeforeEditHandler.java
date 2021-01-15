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
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				if("def3".equals(e.getKey())){
					if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def2") == null){
						ExceptionUtils.wrappBusinessException("请先填写【完成尽调】实际时间值");
					}
				}
				if("def4".equals(e.getKey())){
					if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def3") == null){
						ExceptionUtils.wrappBusinessException("请先填写【提交交易所】实际时间值");
					}
				}
				if("def5".equals(e.getKey())){
					if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def4") == null){
						ExceptionUtils.wrappBusinessException("请先填写【交易所审批通过】实际时间值");
					}
				}
				if(SdfnUtil.getABSList().contains(nameBypk) ){
					if("def8".equals(e.getKey())){
						if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def5") == null){
							ExceptionUtils.wrappBusinessException("业务类型为ABS，请先填写【文件封卷】实际时间值");
						}
					}
				}else{
					if("def6".equals(e.getKey())){
						if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def5") == null){
							ExceptionUtils.wrappBusinessException("业务类型为债券，请先填写【文件封卷】实际时间值");
						}
					}
					if("def7".equals(e.getKey())){
						if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def6") == null){
							ExceptionUtils.wrappBusinessException("业务类型为债券，请先填写【提交证监会】实际时间值");
						}
					}
					if("def8".equals(e.getKey())){
						if(e.getBillCardPanel().getBillModel(e.getTableCode()).getValueAt(e.getRow(), "def7") == null){
							ExceptionUtils.wrappBusinessException("业务类型为债券，请先填写【证监会审批通过】实际时间值");
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