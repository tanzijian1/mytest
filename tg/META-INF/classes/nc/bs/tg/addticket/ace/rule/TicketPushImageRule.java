package nc.bs.tg.addticket.ace.rule;

import java.util.Date;
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
import nc.vo.ecpubapp.pattern.exception.ExceptionUtils;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.addticket.AddTicket;
import nc.vo.tg.addticket.AggAddTicket;

public class TicketPushImageRule implements IRule<AggAddTicket>{
private String par;
public TicketPushImageRule(String par_image){
	this.par=par_image;
}
	@Override
	public void process(AggAddTicket[] vos) {
		BaseDAO dao=new BaseDAO();
		// TODO 自动生成的方法存根
		if (vos != null && vos.length > 0&&"Y".equals(par)) {
		for(AggAddTicket vo:vos){
			try {
			AddTicket headvo=vo.getParentVO();
			String creator = (String) getDao().executeQuery(
					"SELECT user_code FROM sm_user WHERE cuserid = '"
							+headvo.getCreator()+ "'", new ColumnProcessor());
			if(checkimage(headvo.getPk_org())){//判断业财模式
		CircularlyAccessibleValueObject[] bvos=	vo.getChildrenVO();
		IGuoXinImage sv=NCLocator.getInstance().lookup(IGuoXinImage.class);
		HashMap<String, String> map=new HashMap<String,String>();
		if(headvo.getDef10()!=null)
		map.put("anattr2", headvo.getDef10());//发票总金额
		if(headvo.getDef10()!=null)
		map.put("anattr3", headvo.getDef11());//发票总税额
		StringBuffer namesb=new StringBuffer();
		StringBuffer codesb=new StringBuffer();
		StringBuffer kbank=new StringBuffer();
		if(bvos!=null){
		for(CircularlyAccessibleValueObject bvo:bvos){
				String name=(String)dao.executeQuery("select name from bd_supplier where  pk_supplier='"+bvo.getAttributeValue("def2")+"'", new ColumnProcessor());
//				String banknum=(String)dao.executeQuery("select accnum from bd_bankaccsub where pk_bankaccsub ='"+bvo.getAttributeValue("def3")+"'", new ColumnProcessor());
                if(name!=null&&name!="")namesb.append(name+";");//账户名称
                Map<String, String> bankmap=(Map<String, String>)getbankaccount((String)bvo.getAttributeValue("def3"));
                if(bankmap!=null){
                	codesb.append(bankmap.get("accnum")+";");//账号
                	String openbankname=getopenBank(bankmap.get("pk_bankaccbas"));
                	if(openbankname!=null){
                	kbank.append(openbankname+";");//开户银行
                	}
                  }
                }
		}
		//流程类别
		String lcname=(String)dao.executeQuery("select name from bd_defdoc where pk_defdoc='"+headvo.getAttributeValue("def35")+"'  and pk_defdoclist = '1001121000000000058Z'", new ColumnProcessor());
		map.put("datasource", lcname);//流程类别
		String def44=headvo.getDef44();//非标准付款函
	     String isflag=(String)dao.executeQuery("select pk_defdoc from bd_defdoc where pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code = 'zdy001') and code='01'", new ColumnProcessor());
	     if(def44!=null&&def44.length()>0){
	    	 if(!(def44.equals(isflag))){
		if(namesb.length()>0)
		map.put("anattr4", namesb.toString().substring(0,namesb.length()-1));//付款函账户名称
		if(codesb.length()>0)
		map.put("anattr5", codesb.toString().substring(0,codesb.length()-1));//付款函账号
		if(kbank.length()>0)
		map.put("anattr6", kbank.toString().substring(0,kbank.length()-1));//付款函开户银行
		}
	     }
		map.put("anattr7", String.valueOf(headvo.getAttributeValue("def8")));//先付款后补票
		map.put("anattr8", String.valueOf(headvo.getAttributeValue("def16")));//仅补票
		map.put("anattr9", String.valueOf(headvo.getAttributeValue("def9")));//单据优先级(bpm填后接回nc)
		map.put("anattr10", String.valueOf(headvo.getAttributeValue("def12")));//影像校验结果(接受)
		map.put("anattr11", String.valueOf(headvo.getAttributeValue("def13")));//影像校验原因(接受)
		map.put("anattr12", String.valueOf(headvo.getAttributeValue("def14")));//影像状态（接受）
		String billtype=headvo.getBilltype()==null?"RZ30":headvo.getBilltype();
		String barcode=null;
		if(StringUtil.isEmptyWithTrim(headvo.getDef21())){
			long ufd=System.currentTimeMillis();
			 barcode = creator+ufd;//影像编码
			}else{
				barcode=headvo.getDef21();
			}
		map.put("barcode", barcode);
//		map.put("anattr13", String.valueOf(barcode));//影像编码
	    sv.startWorkFlow(billtype, headvo.getPk_ticket(), headvo.getPk_org(), headvo.getCreator(), null, "2", headvo.getBillno(),map);
	    headvo.setDef21(barcode);
//	    dao.updateVO(headvo, new String[]{"def21"});
			}
			} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
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
