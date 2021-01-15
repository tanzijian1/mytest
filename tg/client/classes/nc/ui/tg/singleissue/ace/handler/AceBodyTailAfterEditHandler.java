package nc.ui.tg.singleissue.ace.handler;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.tg.singleissue.util.SingleIssueUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
/**
 * 表体编辑后事件
 * @author wenjie
 *
 */
public class AceBodyTailAfterEditHandler implements IAppEventHandler<CardBodyAfterEditEvent> {
	private IUAPQueryBS bs = null;
	private IUAPQueryBS getQueryBS(){
		if(bs==null){
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
	}
	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		String tableCode = e.getBillCardPanel().getBodyPanel().getTableCode();
		// TODO 自动生成的方法存根
		if(tableCode.equals("pk_bondresale")){//债券回售、转售
			UFDate tuDate=(UFDate)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def6");
			UFDate reDate=(UFDate)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def1");
			if("def1".equals(e.getKey()) || "def6".equals(e.getKey())){
				if(tuDate == null){
					e.getBillCardPanel().getBillModel("pk_bondresale").setValueAt(null, 0, "def6");
				}else{
					if(reDate!=null){
						tuDate = tuDate.asBegin();
						reDate = reDate.asBegin();
						if(tuDate.compareTo(reDate)<=0){
							e.getBillCardPanel().getBillModel("pk_bondresale").setValueAt(reDate.getDateAfter(1), 0, "def6");
						}
					}
				}
				SingleIssueUtil.writeRepayPlan(e);
			}else if("def2".equals(e.getKey()) || "def7".equals(e.getKey())){
				UFDouble remoney = (UFDouble)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def2");
				remoney = remoney==null?UFDouble.ZERO_DBL:remoney;
				UFDouble tumoney = (UFDouble)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def7");
				tumoney = tumoney==null?UFDouble.ZERO_DBL:tumoney;
				UFDouble money = (UFDouble)e.getBillCardPanel().getHeadItem("def5").getValueObject();
				if(remoney.compareTo(tumoney)<0){
					e.getBillCardPanel().getBillModel("pk_bondresale").setValueAt(null,0,"def7");
				}
				tumoney = (UFDouble)e.getBillCardPanel().getBillModel("pk_bondresale").getValueAt(0, "def7");
				tumoney = tumoney==null?UFDouble.ZERO_DBL:tumoney;
				e.getBillCardPanel().getBillModel("pk_bondresale").setValueAt(money.sub(remoney).add(tumoney), 0, "def3");
				SingleIssueUtil.writeRepayPlan(e);
			}else if("def4".equals(e.getKey())){
				BillModel billModel = e.getBillCardPanel().getBillModel("pk_bondresale");
				UFDouble rerate  = (UFDouble) billModel.getValueAt(0, "def4");//回售利率
				UFDouble value = (UFDouble) e.getBillCardPanel().getHeadItem("def41").getValueObject();//债券回售财务顾问费
				UFDouble total = (UFDouble) e.getBillCardPanel().getHeadItem("def5").getValueObject();//发行总规模
				UFDouble time = (UFDouble) e.getBillCardPanel().getHeadItem("def43").getValueObject();//可续期限
				if(rerate!=null && value!=null && total!=null && time!=null){
					e.getBillCardPanel().getHeadItem("def44").setValue(
							rerate.add(value.div(time.multiply(total)).multiply(new UFDouble(100),2)));
				}
				if(e.getValue() != null)SingleIssueUtil.writeRepayPlan(e);
			}
		}
		if(tableCode.equals("pk_issuedetail")){//发行明细
			if(e.getKey().equals("def3")){//监管户账户
					//开户行自动带出、户名自动带出
				DefaultConstEnum value = (DefaultConstEnum)e.getBillCardPanel().getBillModel(tableCode).getValueObjectAt(e.getRow(), "def3");
				String pk_bankaccsub = (String)value.getValue();
				String sql = "select bd_bankaccsub.name aname,bd_bankdoc.name bname from bd_bankaccbas"
						+ " inner join bd_bankaccsub on bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
						+ " inner join bd_bankdoc on bd_bankdoc.pk_bankdoc = bd_bankaccbas.pk_bankdoc "
						+ " where bd_bankaccbas.accclass=2 and bd_bankaccbas.enablestate = 2 and bd_bankaccbas.dr=0"
						+ " and bd_bankaccsub.dr=0 and bd_bankdoc.dr=0 and bd_bankaccsub.pk_bankaccsub ='"+pk_bankaccsub+"'";
				try {
					Map<String,Object> map = (Map<String,Object>)this.getQueryBS().executeQuery(sql, new MapProcessor());
					if(map != null){
						e.getBillCardPanel().getBillModel(tableCode).setValueAt((String)map.get("bname"), e.getRow(), "def4");
						e.getBillCardPanel().getBillModel(tableCode).setValueAt((String)map.get("aname"), e.getRow(), "def5");
					}
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
			}
			if("def2".equals(e.getKey())){
				e.getBillCardPanel().getBillModel(tableCode).setValueAt(null, e.getRow(), "def3");
				e.getBillCardPanel().getBillModel(tableCode).setValueAt(null, e.getRow(), "def4");
				e.getBillCardPanel().getBillModel(tableCode).setValueAt(null, e.getRow(), "def5");
			}
		}
		if("pk_groupcredit".equals(tableCode)){//集团授信页签
			if("def1".equals(e.getKey())){//集团授信协议带出授信银行
				DefaultConstEnum value = (DefaultConstEnum)e.getBillCardPanel().getBillModel(tableCode).getValueObjectAt(e.getRow(), "def1");
				String pk_bankprotocol = (String)value.getValue();
				String sql = "select bd_bankdoc.name from bd_bankdoc left join cc_bankprotocol "
							+ " on bd_bankdoc.pk_bankdoc=cc_bankprotocol.pk_creditbank and bd_bankdoc.dr=0"
							+ " where nvl(cc_bankprotocol.dr,0)=0 and cc_bankprotocol.vbillstatus=1 "
							+ " and cc_bankprotocol.pk_bankprotocol = '"+pk_bankprotocol+"'";
				try {
					String name = (String)this.getQueryBS().executeQuery(sql, new ColumnProcessor());
					e.getBillCardPanel().getBillModel(tableCode).setValueAt(name, e.getRow(), "def2");
				} catch (BusinessException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		}
	}
}
