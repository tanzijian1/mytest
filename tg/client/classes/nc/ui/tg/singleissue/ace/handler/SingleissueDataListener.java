package nc.ui.tg.singleissue.ace.handler;

import java.util.List;

import nc.funcnode.ui.FuncletInitData;
import nc.ui.cdm.baselink.listener.CDMBaseInitDataListener;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.tg.singleissue.AggSingleIssueVO;

public class SingleissueDataListener extends CDMBaseInitDataListener{
	public void initData(FuncletInitData data) {
		// TODO 自动生成的方法存根
		AggSingleIssueVO[] pk = null;
		if (data != null) {
			if (data.getInitData() != null) {
//				getAutoShowUpListComponent().showMeUp();
				if ("2020SD06".equals(data.getTypeComment())) {
					//强转DefaultLinkData
					@SuppressWarnings("unchecked")
					List<AggSingleIssueVO> listvopk = (List<AggSingleIssueVO>) ((DefaultLinkData) data.getInitData()).getUserObject();
					AggSingleIssueVO[] pkvo = new AggSingleIssueVO[listvopk.size()];
					pk=listvopk.toArray(pkvo);
					getModel().initModel(pk);
					return ;
				}else{
//					getModel().initModel(data);
				}

			}
		}
		super.initData(data);
	}

}
