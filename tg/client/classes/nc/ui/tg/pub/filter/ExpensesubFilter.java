package nc.ui.tg.pub.filter;

import nc.ui.bd.ref.model.AccountDefaultRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.query2.refedit.IRefFilter;
import nc.vo.pub.lang.UFDate;

public class ExpensesubFilter implements IRefFilter {

	@Override
	public void doFilter(UIRefPane refPane) {
		// TODO �Զ����ɵķ������
		((AccountDefaultRefModel) refPane.getRefModel()).setPara(new Object[] {
				"100112100000000A7TBJ", new UFDate().toStdString() });
	}

}
