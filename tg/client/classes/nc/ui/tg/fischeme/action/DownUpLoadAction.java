package nc.ui.tg.fischeme.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;

public class DownUpLoadAction extends NCAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5321515464840540485L;
	private BillManageModel model;
    public BillManageModel getModel() {
		return model;
	}
	public void setModel(BillManageModel model) {
		this.model = model;
	}
	public DownUpLoadAction(){
		     super();
	         setBtnName("上传下载附件");
	         setCode("UdP01");
            }
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		
	}

}
