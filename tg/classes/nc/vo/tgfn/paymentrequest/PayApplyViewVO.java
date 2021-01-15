package nc.vo.tgfn.paymentrequest;

import nc.vo.pubapp.pattern.model.entity.view.AbstractDataView;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.view.IDataViewMeta;

public class PayApplyViewVO extends AbstractDataView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 309400697790027869L;

	@Override
	public IDataViewMeta getMetaData() {
		return DataViewMetaFactory.getInstance().getBillViewMeta(AggPayrequest.class);
	}

	public Payrequest getHead() {
		return  (Payrequest) this.getVO(Payrequest.class);
	}

	public void setHead(Payrequest head) {
		this.setVO(head);
	}

	public Business_b getBody() {
		return (Business_b) this.getVO(Business_b.class);
	}

	public void setBody(Business_b body) {
		this.setVO(body);
	}

	public AggPayrequest changeToAggPayrequest() {
		AggPayrequest vo = new AggPayrequest();
		vo.setParent(this.getHead());
		Business_b[] bodys = new Business_b[] {
			this.getBody()
		};
		vo.setChildrenVO(bodys);
		return vo;
	}
}
