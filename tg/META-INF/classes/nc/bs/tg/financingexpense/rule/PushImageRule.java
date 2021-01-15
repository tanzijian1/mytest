package nc.bs.tg.financingexpense.rule;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.image.IGuoXinImage;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceService;
import nc.vo.cmp.util.StringUtils;
import nc.vo.ecpubapp.pattern.exception.ExceptionUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;

public class PushImageRule implements IRule<AggFinancexpenseVO> {
	private String para;
	public PushImageRule(String para){
		this.para=para;
	}
	@Override
	public void process(AggFinancexpenseVO[] vos) {
		// TODO �Զ����ɵķ������
		BaseDAO dao=new BaseDAO();
		if (vos != null && vos.length > 0&&"Y".equals(para)) {
			for(AggFinancexpenseVO vo :vos){
				try {
					FinancexpenseVO headvo=vo.getParentVO();
					String creator = (String) getDao().executeQuery(
							"SELECT user_code FROM sm_user WHERE cuserid = '"
									+headvo.getCreator()+ "'", new ColumnProcessor());
					IGuoXinImage sv=NCLocator.getInstance().lookup(IGuoXinImage.class);
					HashMap<String, String> map=new HashMap<String,String>();
					String trancode=(String)dao.executeQuery("select pk_billtypecode from bd_billtype  where pk_billtypeid='"+headvo.getAttributeValue("transtypepk")+"'", new ColumnProcessor());
					String barcode=null;
					if(StringUtils.isEmpty((String)headvo.getAttributeValue("def21"))){
						long ufd=System.currentTimeMillis();
						 barcode = creator+ufd;//Ӱ�����
						}else{
							barcode=(String)headvo.getAttributeValue("def21");
						}
					map.put("barcode", barcode);
					String lcname=(String)dao.executeQuery("select name from bd_defdoc where pk_defdoc='"+headvo.getAttributeValue("def10")+"'  and pk_defdoclist = '1001121000000000058Z'", new ColumnProcessor());
					String pk_org=null;
					if("RZ06-Cxx-001".equals(trancode)){//�ƹ˷�
						pk_org=headvo.getPk_payer();
						if(pk_org==null)pk_org=headvo.getDef61();
						map.put("anattr2",String.valueOf(headvo.getAttributeValue("def27")));//��Ʊ�ܽ��
						map.put("anattr3", String.valueOf(headvo.getAttributeValue("def5")));//��Ʊ��˰��
						String def44=(String)headvo.getAttributeValue("def44");//�Ǳ�׼����ֶ�
					     String isflag=(String)dao.executeQuery("select pk_defdoc from bd_defdoc where pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code = 'zdy001') and code='01'", new ColumnProcessor());
						  if(def44!=null){
							  if(!(def44.equals(isflag))){
							map.put("anattr4", getsupplier(String.valueOf(headvo.getAttributeValue("pk_payee"))));//����˻�����
							Map<String,String> bankmap=getbankaccount(String.valueOf(headvo.getAttributeValue("def7")));
							if(bankmap!=null){
							map.put("anattr5", bankmap.get("accnum"));//����˺�
							map.put("anattr6", getopenBank(bankmap.get("pk_bankaccbas")));//�����������
							    }
							  }
						  }
						//add by tjl
//						map.put("anattr6", String.valueOf(headvo.getAttributeValue("vdef18")));//�ȸ����Ʊ
						map.put("anattr7", String.valueOf(headvo.getAttributeValue("def3")));//�������ȼ�(bpm���ӻ�nc)
						map.put("anattr8", String.valueOf(headvo.getAttributeValue("def22")));//Ӱ��У����(����)
						map.put("anattr9", String.valueOf(headvo.getAttributeValue("def9")));//Ӱ��У��ԭ��(����)
						map.put("anattr10", String.valueOf(headvo.getAttributeValue("def11")));//Ӱ��״̬�����ܣ�
						map.put("anattr11", String.valueOf(barcode));//Ӱ�����
						map.put("datasource", lcname);//�������
						//end
//			map.put("anattr6", arg1);//�����������
					}else{//���ʷ���
						pk_org=headvo.getPk_org();
						map.put("anattr2",String.valueOf(headvo.getAttributeValue("def27")));//��Ʊ�ܽ��
						map.put("anattr3", String.valueOf(headvo.getAttributeValue("def11")));//��Ʊ��˰��
						String def44=(String)headvo.getAttributeValue("def44");//�Ǳ�׼����ֶ�
						String isflag=(String)dao.executeQuery("select pk_defdoc from bd_defdoc where pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code = 'zdy001') and code='01'", new ColumnProcessor());
						  if(def44!=null){
							  if(!(def44.equals(isflag))){
							map.put("anattr4", getsupplier(String.valueOf(headvo.getAttributeValue("pk_payee"))));//����˻�����
							Map<String,String> bankmap=getbankaccount(String.valueOf(headvo.getAttributeValue("def7")));
							if(bankmap!=null){
							map.put("anattr5", bankmap.get("accnum"));//����˺�
							map.put("anattr6", getopenBank(bankmap.get("pk_bankaccbas")));//�����������
							  }
						    }
						  }
						//add by tjl
//						map.put("anattr6", String.valueOf(headvo.getAttributeValue("def9")));//�ȸ����Ʊ
						map.put("anattr7", String.valueOf(headvo.getAttributeValue("def4")));//�������ȼ�(bpm���ӻ�nc)
						map.put("anattr8", String.valueOf(headvo.getAttributeValue("def12")));//Ӱ��У����(����)
						map.put("anattr9", String.valueOf(headvo.getAttributeValue("def13")));//Ӱ��У��ԭ��(����)
						map.put("anattr10", String.valueOf(headvo.getAttributeValue("def14")));//Ӱ��״̬�����ܣ�
						map.put("anattr11", String.valueOf(barcode));//Ӱ�����
						map.put("datasource", lcname);//�������
						//end
//				map.put("anattr6", arg1);//�������������Ŀ����	billtype ��Ŀ����	transtypepk
					}
					boolean ifsendimage=checkimage(pk_org);
					if(ifsendimage){//�ж�ҵ��ģʽ�Ƿ��ƴ���
						sv.startWorkFlow(String.valueOf(trancode),String.valueOf(headvo.getAttributeValue("pk_finexpense")), String.valueOf(pk_org), headvo.getCreator(), null, "2",String.valueOf(headvo.getAttributeValue("billno")),map);
//						headvo.setStatus(VOStatus.UPDATED);
						headvo.setAttributeValue("def21", barcode);
//						getDao().executeUpdate("update tgrz_financexpense set def21='"+headvo.getDef21()+"' where pk_finexpense='"+headvo.getPk_finexpense()+"'");
					}
					
				} catch (BusinessException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					ExceptionUtils
					.wrappBusinessException(e.getMessage());
				}
				
				
			}
		}
	}
	
	public boolean checkimage(String pk_org) throws DAOException{
		//ҵ��ģʽ ȡֵΪY/N
		 String def11=(String)getDao().executeQuery("select def11  from org_orgs where pk_org='"+pk_org+"'", new ColumnProcessor());
          if("��ҵ��ģʽ".equals(def11))return false;
          return true;
	}
	private BaseDAO dao;
	public BaseDAO getDao() {
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;
	}

	public void setDao(BaseDAO dao) {
		this.dao = dao;
	}

	/**
	 * �õ��տ���
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public String getreceiver(String pk) throws DAOException{
		String Receivername=(String)getDao().executeQuery("select name  from bd_psndoc where   pk_psndoc='"+pk+"'", new ColumnProcessor());
      return Receivername;
	}
	/**
	 * �õ�����
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
  public Map<String,String> getbankaccount(String pk) throws DAOException{
	  Map<String,String> map=null;
		 map=(Map<String,String>)getDao().executeQuery("select accnum,pk_bankaccbas from bd_bankaccsub where pk_bankaccsub ='"+pk+"'", new MapProcessor());
       return map;
  }
	/**
	 * �õ���Ӧ������
	 * @param pk
	 * @return
	 * @throws DAOException 
	 */
	public String getsupplier(String pk) throws DAOException{
		
		String name=null;
		 name=(String)getDao().executeQuery("select name from bd_supplier where  pk_supplier='"+pk+"'", new ColumnProcessor());
		return name; 
	}
	/**
	 * �õ���������
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public String getopenBank(String pk) throws DAOException{
		  String name=null;
		  name=(String)getDao().executeQuery("select name from bd_banktype where pk_banktype=(select pk_banktype  from bd_bankaccbas where pk_bankaccbas ='"+pk+"')", new ColumnProcessor());
		return name;
	}
	
}
