package nc.ui.tg.addticket.ace.handler;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;

//标识,这是需要打的类(融资)
public class AceHeadAfterEditHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent event) {
		// TODO 自动生成的方法存根
		if ("def2".equals(event.getKey())||"def23".equals(event.getKey())) {
			if(event.getValue() != null){
				event.getBillCardPanel().getHeadItem("def36").setEnabled(false);
			}else{
				event.getBillCardPanel().getHeadItem("def36").setEnabled(true);
			}
			String[] formulas = new String[] {
					"def3->getcolvalue(cdm_contract,vdef12,pk_contract,def2 )",// 合同贷款期
					"def4->getcolvalue(cdm_contract,vdef15,pk_contract,def2 )",// 银行机构
					"def5->getcolvalue(cdm_contract,vdef17,pk_contract,def2 )",// 有无抵押
					"def6->getcolvalue(cdm_contract,vdef16,pk_contract,def2 )",// 项目竣备
					"def7->getcolvalue(cdm_contract,vdef14,pk_contract,def2 )",// 利息类型
					"def18->getcolvalue(cdm_contract,htmc,pk_contract,def2 )"// 合同名称
			};

			event.getBillCardPanel().execHeadFormulas(formulas);


		}
		if("def36".equals(event.getKey())){
			if(event.getValue() != null){
				event.getBillCardPanel().getHeadItem("def2").setEnabled(false);
			}else{
				event.getBillCardPanel().getHeadItem("def2").setEnabled(true);
			}
			
			String[] formulas = new String[] {
				"def18->getcolvalue(sdfn_singleissue,name,pk_singleissue,def36 )"// 合同名称	
			};
			
			event.getBillCardPanel().execHeadFormulas(formulas);
		}
	}
}
