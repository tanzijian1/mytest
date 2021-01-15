package nc.bs.tg.addticket.ace.rule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.cdm.contractbankcredit.InsertContractExecRuleInterface;
import nc.itf.uap.busibean.ISysInitQry;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.cdm.contractbankcredit.CwgwfzxqkBVO;
import nc.vo.cdm.contractbankcredit.FinancexpenseMapping;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.cdm.repayreceiptbankcredit.RePayReceiptBankCreditVO;
import nc.vo.cmp.settlement.SettlementHeadVO;
import nc.vo.org.FinanceOrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.contract.ContractExecVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;
import nc.vo.tg.singleissue.BondTransSaleVO;
import nc.vo.tg.singleissue.ConstateExeVO;

/**
 * ����д�����ͬ��ϸҳǩ
 * @author yy
 *
 */
public class QinKuanWriteBackRule implements IRule<AggAddTicket>{
private IMDPersistenceQueryService service;
	public IMDPersistenceQueryService getService() {
		if(service==null){
			service=NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		}
	return service;
}
public void setService(IMDPersistenceQueryService service) {
	this.service = service;
}
	@Override
	public void process(AggAddTicket[] vos) {
		// TODO �Զ����ɵķ������
		for(AggAddTicket billvo:vos){
			if(billvo.getParentVO().getApprovestatus()==1){
			CircularlyAccessibleValueObject[] bvos=billvo.getChildrenVO();
			for(CircularlyAccessibleValueObject bvo:bvos){
				String def1=(String)bvo.getAttributeValue("def1");//����
				if(def1==null)return;
				String def5=(String)bvo.getAttributeValue("def5");//������ֵ˰���
				try {
					//�ƹ˷�����
					NCObject nobj=getService().queryBillOfNCObjectByPK(AggFinancexpenseVO.class, def1);
				    if(nobj!=null){
					if(nobj.getContainmentObject()!=null){
				    	AggFinancexpenseVO vo=(AggFinancexpenseVO)nobj.getContainmentObject();
				    	if(def5!=null&&def5.length()>0){
				    		String pk_singleissue = vo.getParentVO().getDef12();//���ڷ������
				    		if(pk_singleissue == null){
				    			processCwgwfz(vo,new UFDouble(def5));
				    		}else{
				    			processSingleIssue(vo,def5);
				    		}
				    	}
				    }
				    }
				    //���
				    NCObject nobj1=getService().queryBillOfNCObjectByPK(AggRePayReceiptBankCreditVO.class, def1);
				    if(nobj1!=null){
				    	if(nobj1.getContainmentObject()!=null){
				    	AggRePayReceiptBankCreditVO vo=(AggRePayReceiptBankCreditVO)nobj1.getContainmentObject();
				    	if(def5!=null&&def5.length()>0) processRepay(vo,new UFDouble(def5) );
				    	}
				    }
				    
				    //�ʱ��г�����
					NCObject nobj2=getService().queryBillOfNCObjectByPK(AggMarketRepalayVO.class, def1);
					if(nobj2!=null){
						AggMarketRepalayVO vo = (AggMarketRepalayVO)nobj2.getContainmentObject();
						if(def5!=null&&def5.length()>0)processConstractExe(vo,def5);
					}
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					ExceptionUtils
					.wrappBusinessException(e.getMessage());
				}
			}
		}
		}
	}
	/**
	 * ��д�����ڷ�������ĺ�ִͬ�����
	 * @throws BusinessException
	 */
	private void processConstractExe(AggMarketRepalayVO vo, String d) throws BusinessException {
		MarketRepalayVO parentVO = vo.getParentVO();
		String billno = parentVO.getBillno();
		String pk_singleissue = parentVO.getDef5();
		String decName = getDecName(parentVO.getDef15());
		String isPush = parentVO.getDef34();
		if("��".equals(decName) || !"Y".equals(isPush)){
			Collection col=getBaseDao().retrieveByClause(BondTransSaleVO.class, 
					"def1='"+billno+"' and pk_singleissue='"+pk_singleissue+"'");
			if(col!=null&&col.size()>0){
				Iterator<BondTransSaleVO> iter= col.iterator();
				BondTransSaleVO bvo= iter.next();
				if(bvo!=null){
					bvo.setDef9(d);
					getBaseDao().updateVO(bvo,new String[]{"def9"});//��Ʊ���
				}
			}
		}
	}
	/**
	 * ��д���ڷ�������Ĳƹ˷�ִ�����ҳǩ
	 */
	public void processSingleIssue(AggFinancexpenseVO vo,String d) throws BusinessException {
		FinancexpenseVO hvo = vo.getParentVO();
		String billno=hvo.getBillno();
		String pk_singleissue = hvo.getDef12();
		String decName = getDecName(hvo.getDef31());
		String isPush = hvo.getDef17();
		if("��".equals(decName) || !"Y".equals(isPush)){//�Ƿ������Ʊ
			Collection col=getBaseDao().retrieveByClause(ConstateExeVO.class, 
					"def5='"+billno+"' and pk_singleissue='"+pk_singleissue+"'");
			if(col!=null&&col.size()>0){
				 Iterator<ConstateExeVO> iter= col.iterator();
				 ConstateExeVO bvo= iter.next();
				 if(bvo!=null){
					 bvo.setDef4(d);
					 getBaseDao().updateVO(bvo,new String[]{"def4"});//��Ʊ���
				 }
			}
		}
//		}else{
//			String isPush = hvo.getDef17();
//			if (!"Y".equals(isPush)){//20200114 ��def17�����й�ѡʱ�����Ƹ�����㵥ֱ�ӻ�д�����ڷ�������Ĳƹ���ִ���������
//				UFDouble applyamount = hvo.getApplyamount();//���������
//				String pk_payer = hvo.getPk_payer();
//				if(pk_payer==null||pk_payer.length()<1)throw new BusinessException("�ƹ˷Ѹ��λΪ��");
//				ConstateExeVO cvo = new ConstateExeVO();
//				cvo.setDr(0);
//				cvo.setPk_singleissue(pk_singleissue);
//				cvo.setDef1(pk_payer);//֧����˾
//				cvo.setDef2(new UFDate().toString());//֧��ʱ��
//				cvo.setDef3(applyamount.toString());//֧�����
//				cvo.setDef5(billno);//���ݱ��
//				cvo.setDef4(d);//��Ʊ���
//				getBaseDao().insertVO(cvo);
//			}
//		}
	}
	/**
	 * ��д��ͬ��ϸ�ƹ˷����ҳǩ
	 * @param vo
	 * @param d
	 * @throws BusinessException 
	 */
	public void processCwgwfz(AggFinancexpenseVO vo,UFDouble d) throws BusinessException {
		// TODO �Զ����ɵķ������
		FinancexpenseVO hvo = vo.getParentVO();
		String pk=hvo.getPk_finexpense();
		if("Y".equals(hvo.getDef31())){//�Ƿ������Ʊ
			Collection col=getBaseDao().retrieveByClause(CwgwfzxqkBVO.class, "rowno='"+pk+"'");
			 if(col!=null&&col.size()>0){
				 Iterator<CwgwfzxqkBVO> iter= col.iterator();
				 CwgwfzxqkBVO bvo= iter.next();
				 if(bvo!=null){
				 UFDouble m_zfje=(UFDouble)bvo.getAttributeValue("m_zfje");
				 m_zfje= m_zfje==null? UFDouble.ZERO_DBL:m_zfje;
				 bvo.setAttributeValue("m_zfje", m_zfje.add(d));
				 getBaseDao().updateVO(bvo,new String[]{"m_zfje"});
				 }
			 }
		}else{
			insertCwgwfzxqkBVO(vo,d.toString());
		}
		}
	public void insertCwgwfzxqkBVO(AggFinancexpenseVO finVO,String def5) throws BusinessException{
		FinancexpenseVO vo =finVO.getParentVO();
		String isPush = (String) vo.getAttributeValue("def17");
		
			if (!"Y".equals(isPush)){// add swh  20200114 ��def17�����й�ѡʱ�����Ƹ�����㵥ֱ�ӻ�д�����д����ͬ��ϸ�Ĳƹ���ִ���������
				//��ѯ��ǰ���õĶ�Ӧ�����ͬ��ϸpk
				
				//���������
			UFDouble applyamount=	(UFDouble) finVO.getParentVO().getAttributeValue("applyamount");
				//���λ
			String contract=	(String) finVO.getParentVO().getAttributeValue("def4");
			if(contract==null||contract.length()<1)throw new BusinessException("�ƹ˷Ѷ�Ӧ��ͬΪ��");
			String pk_payer=	(String) finVO.getParentVO().getAttributeValue("pk_payer");
			if(pk_payer==null||pk_payer.length()<1)throw new BusinessException("�ƹ˷Ѹ��λΪ��");
			String pk_finexpense=    (String) finVO.getParentVO().getAttributeValue("pk_finexpense");
			String 	payer	=getContractPK(pk_payer);
			//�Ƿ��з�Ʊ
			String def31=(String)finVO.getParentVO().getAttributeValue("def31");
			//��Ʊ���
			String def27=(String)finVO.getParentVO().getAttributeValue("def27");
			StringBuffer  sql =new  StringBuffer();
			List<CwgwfzxqkBVO>  CwgwVOs =null;
			sql.append("  cdm_cwgwfzxqk.pk_contract ='"+contract+"' ")
			//.append("(select pk_contract  from cdm_contract where cdm_contract.contractcode='"+contract+"') ")
			.append("and  cdm_cwgwfzxqk.rowno='"+pk_finexpense+"' ");
				CwgwVOs=	(List<CwgwfzxqkBVO>) getBaseDao().retrieveByClause(CwgwfzxqkBVO.class, sql.toString() );
			if(CwgwVOs!=null&&CwgwVOs.size()>0){
				for(CwgwfzxqkBVO cvo:CwgwVOs){
					cvo.setAttributeValue("pk_contract", contract);
					cvo.setAttributeValue("rowno", pk_finexpense);
					cvo.setAttributeValue("pk_zfgs", payer);
					cvo.setAttributeValue("m_zfje", applyamount);
					cvo.setAttributeValue("d_zfsj", new UFDate());
					cvo.setAttributeValue("def1",def31);//�Ƿ�Ʊ
					cvo.setAttributeValue("def2", def5);//��Ʊ���
					cvo.setAttributeValue("dr",0);
					try {
						getBaseDao().updateObject(cvo, new FinancexpenseMapping());
					} catch (DAOException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
			}else{
			try {
				CwgwfzxqkBVO  inserVO = new CwgwfzxqkBVO();
				inserVO.setAttributeValue("rowno", pk_finexpense);
				inserVO.setAttributeValue("pk_zfgs", payer);
				inserVO.setAttributeValue("m_zfje", applyamount);
				inserVO.setAttributeValue("pk_contract", contract);
				inserVO.setAttributeValue("d_zfsj", new UFDate());
				inserVO.setAttributeValue("def1",def31);
				inserVO.setAttributeValue("dr",0);
				getBaseDao().insertObject(inserVO, new FinancexpenseMapping());
			} catch (DAOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}	
			}
		
	/*		try {
				dao.updateObject(cvo,new FinancexpenseMapping(), " and where pk_contract='10016A1000000024I50S'");
			} catch (DAOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}*/
			}
	}
public String  getContractPK(String  pk_financeorg ){
	List<FinanceOrgVO >  vos =null;
	try {
		vos =(List<FinanceOrgVO >) getBaseDao().retrieveByClause(FinanceOrgVO .class,"pk_financeorg  ='"+pk_financeorg +"'" );
		//dao.retrieveByClause(ContractVO.class,"contractcode ='"+contractCode+"'" );
	} catch (DAOException e) {
		// TODO �Զ����ɵ� catch ��
		e.printStackTrace();
	}
	
	return vos==null?null:vos.get(0).getName();
	
}

public void processRepay(AggRePayReceiptBankCreditVO billvo,UFDouble d) throws Exception{
	RePayReceiptBankCreditVO vo=billvo.getParentVO();
	if("01".equals(getCodeByPk_defdoc((String)vo.getAttributeValue("def31"),"zdy001"))){//�Ƿ�Ʊ
		Collection col=getBaseDao().retrieveByClause(ContractExecVO.class, "pk_srcbill='"+vo.getPk_repayrcpt()+"'");
	    if(col!=null&&col.size()>0){
	    	Iterator<ContractExecVO> iter= col.iterator();
	    	ContractExecVO obj=iter.next();
	    	ContractExecVO cvo=(ContractExecVO)obj;
	    	String vdef2=cvo.getVdef2();//��Ʊ���
	    	if(vdef2!=null&&vdef2.length()>0){
	    		cvo.setVdef2(new UFDouble(vdef2).add(d).toString());
	    		getBaseDao().updateVO(cvo,new String[]{"vdef2"});
	    	}
	    }
	}else{
		InsertContractExecRuleInterface services=NCLocator.getInstance().lookup(InsertContractExecRuleInterface.class);
//		Map<String,String> map=(Map<String,String>)getBaseDao().executeQuery("select b.paydate,b.pk_paybill from cmp_paybilldetail b   where   pk_upperbill='"+vo.getPk_repayrcpt()+"' and nvl(b.dr,0)=0", new MapProcessor());
		ISysInitQry SysInitQry = NCLocator.getInstance().lookup(ISysInitQry.class);
		String para=null;
		try {
			 para = SysInitQry.getParaString(nc.itf.org.IOrgConst.GLOBEORG, "TGRZ99");
		} catch (BusinessException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		Map<String,String> map=new HashMap<String,String>();
		map.put("imoneny", d.toString());
		services.process(new AggRePayReceiptBankCreditVO[]{billvo} ,map,null);
	}
}
/**
 * ͨ�����ƻ�����Զ�����õ�pk
 * @param name
 * @param deflistcode
 * @return
 * @throws BusinessException
 */
public String getCodeByPk_defdoc(String pk,String deflistcode) throws BusinessException {
	String sql = "SELECT code FROM bd_defdoc def\n" +
					"WHERE def.pk_defdoc = '"+pk+"'  AND NVL(def.dr,0) = 0 and def.pk_defdoclist=(select bd_defdoclist.pk_defdoclist from bd_defdoclist where bd_defdoclist.code='"+deflistcode+"')";
	String pk_project = (String) getBaseDao().executeQuery(sql,
			new ColumnProcessor());
	return pk_project;
}
/**
 * ��ȡ������Ϣ��ͷvo
 * 
 * @param pk_paybill
 * @return
 * @throws DAOException
 */
	private SettlementHeadVO getHeadVO(String pk_paybill) throws DAOException {
		Collection<SettlementHeadVO> docVO = new BaseDAO().retrieveByClause(
				SettlementHeadVO.class, "isnull(dr,0)=0 and pk_busibill = '"
						+ pk_paybill + "' ");
		if (docVO.size() > 0 && null != docVO) {
			for (SettlementHeadVO settlementHeadVO : docVO) {
				return settlementHeadVO;
			}
		}
		return null;
	}
	private BaseDAO  getBaseDao(){
		if(dao==null)
			 dao =new BaseDAO();
		return dao;
		
	}
	private BaseDAO dao =null;
	
	private String getDecName (String pk) throws BusinessException{
		String sql = "select name from bd_defdoc where pk_defdoc = '"+pk+"' and nvl(dr,0) = 0";
		return (String) getBaseDao().executeQuery(sql,new ColumnProcessor());
	}
}
