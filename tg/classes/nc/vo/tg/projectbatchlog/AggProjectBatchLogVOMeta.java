package nc.vo.tg.projectbatchlog;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggProjectBatchLogVOMeta extends AbstractBillMeta{
	
	public AggProjectBatchLogVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.projectbatchlog.ProjectBatchLogVO.class);
		this.addChildren(nc.vo.tg.projectbatchlog.ProjectBatchLogDetailVO.class);
	}
}