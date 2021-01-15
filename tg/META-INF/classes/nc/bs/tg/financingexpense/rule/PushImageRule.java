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
		// TODO 自动生成的方法存根
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
						 barcode = creator+ufd;//影像编码
						}else{
							barcode=(String)headvo.getAttributeValue("def21");
						}
					map.put("barcode", barcode);
					String lcname=(String)dao.executeQuery("select name from bd_defdoc where pk_defdoc='"+headvo.getAttributeValue("def10")+"'  and pk_defdoclist = '1001121000000000058Z'", new ColumnProcessor());
					String pk_org=null;
					if("RZ06-Cxx-001".equals(trancode)){//财顾费
						pk_org=headvo.getPk_payer();
						if(pk_org==null)pk_org=headvo.getDef61();
						map.put("anattr2",String.valueOf(headvo.getAttributeValue("def27")));//发票总金额
						map.put("anattr3", String.valueOf(headvo.getAttributeValue("def5")));//发票总税额
						String def44=(String)headvo.getAttributeValue("def44");//非标准付款函字段
					     String isflag=(String)dao.executeQuery("select pk_defdoc from bd_defdoc where pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code = 'zdy001') and code='01'", new ColumnProcessor());
						  if(def44!=null){
							  if(!(def44.equals(isflag))){
							map.put("anattr4", getsupplier(String.valueOf(headvo.getAttributeValue("pk_payee"))));//付款函账户名称
							Map<String,String> bankmap=getbankaccount(String.valueOf(headvo.getAttributeValue("def7")));
							if(bankmap!=null){
							map.put("anattr5", bankmap.get("accnum"));//付款函账号
							map.put("anattr6", getopenBank(bankmap.get("pk_bankaccbas")));//付款函开户银行
							    }
							  }
						  }
						//add by tjl
//						map.put("anattr6", String.valueOf(headvo.getAttributeValue("vdef18")));//先付款后补票
						map.put("anattr7", String.valueOf(headvo.getAttributeValue("def3")));//单据优先级(bpm填后接回nc)
						map.put("anattr8", String.valueOf(headvo.getAttributeValue("def22")));//影像校验结果(接受)
						map.put("anattr9", String.valueOf(headvo.getAttributeValue("def9")));//影像校验原因(接受)
						map.put("anattr10", String.valueOf(headvo.getAttributeValue("def11")));//影像状态（接受）
						map.put("anattr11", String.valueOf(barcode));//影像编码
						map.put("datasource", lcname);//流程类别
						//end
//			map.put("anattr6", arg1);//付款函开户银行
					}else{//融资费用
						pk_org=headvo.getPk_org();
						map.put("anattr2",String.valueOf(headvo.getAttributeValue("def27")));//发票总金额
						map.put("anattr3", String.valueOf(headvo.getAttributeValue("def11")));//发票总税额
						String def44=(String)headvo.getAttributeValue("def44");//非标准付款函字段
						String isflag=(String)dao.executeQuery("select pk_defdoc from bd_defdoc where pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code = 'zdy001') and code='01'", new ColumnProcessor());
						  if(def44!=null){
							  if(!(def44.equals(isflag))){
							map.put("anattr4", getsupplier(String.valueOf(headvo.getAttributeValue("pk_payee"))));//付款函账户名称
							Map<String,String> bankmap=getbankaccount(String.valueOf(headvo.getAttributeValue("def7")));
							if(bankmap!=null){
							map.put("anattr5", bankmap.get("accnum"));//付款函账号
							map.put("anattr6", getopenBank(bankmap.get("pk_bankaccbas")));//付款函开户银行
							  }
						    }
						  }
						//add by tjl
//						map.put("anattr6", String.valueOf(headvo.getAttributeValue("def9")));//先付款后补票
						map.put("anattr7", String.valueOf(headvo.getAttributeValue("def4")));//单据优先级(bpm填后接回nc)
						map.put("anattr8", String.valueOf(headvo.getAttributeValue("def12")));//影像校验结果(接受)
						map.put("anattr9", String.valueOf(headvo.getAttributeValue("def13")));//影像校验原因(接受)
						map.put("anattr10", String.valueOf(headvo.getAttributeValue("def14")));//影像状态（接受）
						map.put("anattr11", String.valueOf(barcode));//影像编码
						map.put("datasource", lcname);//流程类别
						//end
//				map.put("anattr6", arg1);//付款函开户银行项目主键	billtype 项目主键	transtypepk
					}
					boolean ifsendimage=checkimage(pk_org);
					if(ifsendimage){//判断业财模式是否推待办
						sv.startWorkFlow(String.valueOf(trancode),String.valueOf(headvo.getAttributeValue("pk_finexpense")), String.valueOf(pk_org), headvo.getCreator(), null, "2",String.valueOf(headvo.getAttributeValue("billno")),map);
//						headvo.setStatus(VOStatus.UPDATED);
						headvo.setAttributeValue("def21", barcode);
//						getDao().executeUpdate("update tgrz_financexpense set def21='"+headvo.getDef21()+"' where pk_finexpense='"+headvo.getPk_finexpense()+"'");
					}
					
				} catch (BusinessException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					ExceptionUtils
					.wrappBusinessException(e.getMessage());
				}
				
				
			}
		}
	}
	
	public boolean checkimage(String pk_org) throws DAOException{
		//业财模式 取值为Y/N
		 String def11=(String)getDao().executeQuery("select def11  from org_orgs where pk_org='"+pk_org+"'", new ColumnProcessor());
          if("简化业财模式".equals(def11))return false;
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
	 * 得到收款人
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public String getreceiver(String pk) throws DAOException{
		String Receivername=(String)getDao().executeQuery("select name  from bd_psndoc where   pk_psndoc='"+pk+"'", new ColumnProcessor());
      return Receivername;
	}
	/**
	 * 得到银行
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
	 * 得到供应商名称
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
	 * 得到开户银行
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
