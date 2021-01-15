package nc.bs.tg.capitalmarketrepay.ace.bp.rule;

import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.image.IGuoXinImage;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;

public class PushImageRule implements IRule<AggMarketRepalayVO>{
	private BaseDAO baseDao;
	@Override
	public void process(AggMarketRepalayVO[] vos) {
		MarketRepalayVO parentVO = vos[0].getParentVO();
		// TODO �Զ����ɵķ������
		try {
			IGuoXinImage sv=NCLocator.getInstance().lookup(IGuoXinImage.class);
			String creator = (String) getDao().executeQuery(
					"SELECT user_code FROM sm_user WHERE cuserid = '"
							+parentVO.getCreator()+ "'", new ColumnProcessor());
			String barcode = parentVO.getDef21()==null?creator+System.currentTimeMillis():parentVO.getDef21();
			String lcname=(String)getDao().executeQuery("select name from bd_defdoc where pk_defdoc='"+parentVO.getDef17()+"' and pk_defdoclist = '1001121000000000058Z'", new ColumnProcessor());
			
			HashMap<String, String> map=new HashMap<String,String>();
			map.put("barcode", barcode);//Ӱ�����
			map.put("anattr2",parentVO.getDef18());//��Ʊ�ܽ��
			map.put("anattr3",parentVO.getDef26());//��Ʊ��˰��
			map.put("datasource", lcname);//�������
			
			sv.startWorkFlow(parentVO.getBilltype(), parentVO.getPk_marketreplay(), parentVO.getDef9(), parentVO.getCreator(), null, "2", parentVO.getBillno(), map);
			parentVO.setDef21(barcode);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		
	}

	private BaseDAO getDao() {
		// TODO �Զ����ɵķ������
		if(baseDao == null){
			baseDao = new BaseDAO();
		}
		return baseDao;
	}

}
