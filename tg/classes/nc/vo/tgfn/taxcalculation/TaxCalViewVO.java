package nc.vo.tgfn.taxcalculation;

import nc.vo.pubapp.pattern.model.entity.view.AbstractDataView;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.view.IDataViewMeta;

public class TaxCalViewVO extends AbstractDataView {
	



	@Override
	public IDataViewMeta getMetaData() {
		return DataViewMetaFactory.getInstance().getBillViewMeta(AggTaxCalculationHead.class);
	}

	public TaxCalculationHead getHead() {
		return  (TaxCalculationHead) this.getVO(TaxCalculationHead.class);
	}

	public void setHead(TaxCalculationHead head) {
		this.setVO(head);
	}

	public TaxCalculationBody getBody() {
		return (TaxCalculationBody) this.getVO(TaxCalculationBody.class);
	}

	public void setBody(TaxCalculationBody body) {
		this.setVO(body);
	}

	public AggTaxCalculationHead changeToAggPayrequest() {
		AggTaxCalculationHead vo = new AggTaxCalculationHead();
		vo.setParent(this.getHead());
		TaxCalculationBody[] bodys = new TaxCalculationBody[] {
			this.getBody()
		};
		vo.setChildrenVO(bodys);
		return vo;
	}
}
