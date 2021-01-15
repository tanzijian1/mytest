package nc.bs.tg.alter.plugin.ebs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.itf.tg.ISqlThread;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.IMDPersistenceService;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.erm.expenseaccount.PayPlanDetailVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.PMPlanBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

//EBSͨ��֧����ͬ�������д
public class ContractCountMoneyTask implements IBackgroundWorkPlugin{

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		//���к�ͬ��
		 ArrayList<String> concodes=(ArrayList<String>)getBaseDao().executeQuery("select DISTINCT vbillcode from fct_ap where nvl(dr,0)=0", new ColumnListProcessor());
		 for(String code:concodes){//264X-Cxx-009�º�ͬ�������  264X-Cxx-001(��)��ͬ������� 264X-Cxx-004��ͬ��������
        	 NCObject[] nobjs=getService().queryBillOfNCObjectByCond(BXVO.class, "nvl(dr,0)=0 and zyx2='"+code+"' and djlxbm in ('264X-Cxx-001','264X-Cxx-009','264X-Cxx-004')", false);
        	 if(nobjs!=null&&nobjs.length>0){
        		 for(NCObject nobj:nobjs){
        			 BXVO vo=(BXVO) nobj.getContainmentObject();
        			 JKBXHeaderVO headvo= vo.getParentVO();
        			 //�Ƿ�����
        			 boolean isapprove=headvo.getSpzt()==1?true :false;
        			 //�Ƿ��ѽ���
        			 boolean issettle=issettle(headvo.getPk_jkbx());
        			 if("264X-Cxx-001".equals(headvo.getDjlxbm())){
        				 UFDouble total=headvo.getTotal();//ԭ������
        				 UFDouble mtotal=total;//����������м�ֵ
        				 UFDouble ptotal=total;//�����������м�ֵ
        				 List<CtApPlanVO> listplanvo= getConttractplan(headvo.getZyx2());
        				 if(total!=null&&total.compareTo(UFDouble.ZERO_DBL)>0){
        				 for(CtApPlanVO pvo:listplanvo){
        					//������
        			    	UFDouble planmoney= pvo.getPlanmoney()==null?UFDouble.ZERO_DBL:pvo.getPlanmoney();
        			        if(planmoney.compareTo(UFDouble.ZERO_DBL)<=0)continue;
                           //TODO --�ɵ���ͬNC�ۼ�������ֶλ�д    			       
        			        if(isapprove&&!("Y".equals(headvo.getDef63()))){//������ͨ�����дNC�ۼ�������ֶ�
        			        	if(mtotal!=null&&mtotal.compareTo(UFDouble.ZERO_DBL)>0){
        			      //�ۼ������
        			        UFDouble msumapplay=pvo.getMsumapply()==null?UFDouble.ZERO_DBL:pvo.getMsumapply();
        			    	//NC�ۼ������
        			        UFDouble def11=pvo.getDef11()==null?UFDouble.ZERO_DBL:new UFDouble(pvo.getDef11());
        			    	UFDouble balance= planmoney.sub(msumapplay);//
        			         if(balance.compareTo(UFDouble.ZERO_DBL)>0){
        			        	  if(mtotal.sub(balance).compareTo(UFDouble.ZERO_DBL)<=0){
        			        		  pvo.setDef11(mtotal.add(def11).toString());//NC�ۼ������
        			        	      headvo.setDef63("Y");//��д����־
        			        	  }else{
        			        		  headvo.setDef63("Y");
        			        		  pvo.setDef11(balance.add(def11).toString());//NC�ۼ������
        			        	  }
        			        	  mtotal= mtotal.sub(balance);
        			         }
        			        	}
        			        }
        			        //end
        			        // TODO �ɵ���ͬNC�ۼƸ������ֶλ�д                                        
        			        if(issettle&&!("Y".equals(headvo.getDef64()))){//�ѽ����дNC�ۼƸ�����
        			        	if(ptotal.compareTo(UFDouble.ZERO_DBL)>0){
        			        	//NC�ۼƸ�����
        			        	UFDouble def12=pvo.getDef12()==null?UFDouble.ZERO_DBL:new UFDouble(pvo.getDef12());
        			        	//�ۼ��Ѹ�����
        			        	UFDouble msumpayed=pvo.getMsumpayed()==null?UFDouble.ZERO_DBL:pvo.getMsumpayed();
        			        	UFDouble balance=planmoney.sub(msumpayed);
        			        	 if(balance.compareTo(UFDouble.ZERO_DBL)>0){
            			        	  if(ptotal.sub(balance).compareTo(UFDouble.ZERO_DBL)<=0){
            			        		  pvo.setDef12(ptotal.add(def12).toString());//NC�ۼƸ�����
            			        		  headvo.setDef64("Y");//��д�����־
            			        	  }else{
            			        		  pvo.setDef12(balance.add(def12).toString());//NC�ۼƸ�����
            			        		  headvo.setDef64("Y");//��д�����־
            			        	  }
            			        	  ptotal=ptotal.sub(balance);
            			         }
        			        }
        			        }
        			        pvo.setDef7("Y");//��д��־
        			        update(pvo, new String[]{"def11","def12","def7"});
        			        //END
        			       }
        				 }
        			 }else{//������Ϊ�µ�
        				 handlenewBill(vo);
        			 }
        			 update(headvo, new String[]{"def63","def64"});
        		 }
        	 }
//        	 getBaseDao().executeQuery("select * from er_bxzb where zyx2='"+code+"'", processor)
         }
		return null;
	}
//	public void sortbodyvo(BXVO ppbvos){
////		PayPlanDetailVO[] ppbvos=(PayPlanDetailVO[])billvo.getTableVO("id_payplandetailvo");
////		ppbvos
//	}
	public PayPlanDetailVO[] getpayplDetailVOs(String headpk) throws DAOException{
		 Collection col= getBaseDao().retrieveByClause(PayPlanDetailVO.class, "nvl(dr,0)=0 and pk_jkbx='"+headpk+"'");
		  if(col!=null&&!(col.isEmpty())){
			  return (PayPlanDetailVO[])col.toArray(new PayPlanDetailVO[0]);
		  }
		 return null;
	}
	public void handlenewBill(BXVO vo) throws BusinessException{
		JKBXHeaderVO headvo=vo.getParentVO();
		boolean isapprove=headvo.getSpzt()==1?true :false;
		PayPlanDetailVO[] bxpvos=getpayplDetailVOs(headvo.getPk_jkbx());
         if(bxpvos==null||bxpvos.length<1)return;
		if(headvo.getZyx31()==null||headvo.getZyx31().length()<1)throw new BusinessException(headvo.getDjbh()+"�������Ϊ��");
    	String type=(String)getBaseDao().executeQuery("select pk_defdoc from bd_defdoc where enablestate = 2 and code='002' and pk_defdoclist =(select pk_defdoclist from bd_defdoclist where code='zdy025')", new ColumnProcessor());
    	//TODO ����ƻ���֤��ҳǩ��д
    	if(headvo.getZyx31().equals(type)){//����ƻ�(ǩԼ���)
    		List<PMPlanBVO> listpmp=(List<PMPlanBVO>)getConttractmoney(headvo.getZyx2());
		Map<String,PMPlanBVO> map=new HashMap<String, PMPlanBVO>();
		if(listpmp!=null&&listpmp.size()>0){
	    		for(PMPlanBVO pmvo:listpmp){
	    			map.put(pmvo.getPk_fct_pmplan_b(), pmvo);
	    		}
	    			for(PayPlanDetailVO bxpvo:bxpvos){
	    				PMPlanBVO pmvo=map.get(bxpvo.getZyx2());
	    				if(pmvo!=null){
	    					String zyx1=bxpvo.getZyx1();//������ʵ�������
	    					if(zyx1!=null&&new UFDouble(zyx1).compareTo(UFDouble.ZERO_DBL)>0){
	    					if(isapprove&&!("Y".equals(headvo.getDef63()))){//��������д��ͬNC�����
	    						headvo.setDef63("Y");//���º�ͬ����־
	    						UFDouble def7=pmvo.getDef7()==null?UFDouble.ZERO_DBL:new UFDouble(pmvo.getDef7());//NC�ۼ������
                                pmvo.setDef7(def7.add(new UFDouble(zyx1)).toString());
	    						}else if(issettle(headvo.getPk_jkbx())&&!("Y".equals(headvo.getDef64()))){//�ѽ����д��ͬNC�ۼƸ�����
		    						headvo.setDef64("Y");//���º�ͬ�����־
	    							UFDouble def6=pmvo.getDef6()==null?UFDouble.ZERO_DBL:new UFDouble(pmvo.getDef6());//NC�ۼƸ�����
	    						    pmvo.setDef6(def6.add(new UFDouble(zyx1)).toString());
	    						}
	    					pmvo.setDr(0);
	    					 update(pmvo, new String[]{"def6","def7","def5"});
	    					}
	    				}
	    			}
	    	}
		//END
    	}else{
    		//TODO ����ƻ���д
    		List<CtApPlanVO> listp= getConttractplan(headvo.getZyx2());
    		HashMap<String, CtApPlanVO> map=new HashMap<>();
    		if(listp!=null&&listp.size()>0){
    			for(CtApPlanVO pvo:listp){
    			map.put(pvo.getPk_fct_ap_plan(), pvo);
    			}
	    			for(PayPlanDetailVO bxpvo:bxpvos){
	    				String zyx1=bxpvo.getZyx1();//������ʵ�������
	    				if(zyx1!=null&&new UFDouble(zyx1).compareTo(UFDouble.ZERO_DBL)>0){
	    				CtApPlanVO pvo=map.get(bxpvo.getZyx2());
	    				if(pvo!=null){
	    					if(headvo.getSpzt()==1&&!("Y".equals(headvo.getDef63()))){//��������дNC�ۼ������
	    						//NC�ۼ������
	    						UFDouble def11=pvo.getDef11()==null?UFDouble.ZERO_DBL:new UFDouble(pvo.getDef11());
	    					    pvo.setDef11(def11.add(new UFDouble(zyx1)).toString());
	    					}else if(issettle(headvo.getPk_jkbx())&&!("Y".equals(headvo.getDef64()))){
	    					  //NC�ۼƸ�����
	    						UFDouble def12=pvo.getDef12()==null?UFDouble.ZERO_DBL:new UFDouble(pvo.getDef12());
	    						pvo.setDef12(def12.add(new UFDouble(zyx1)).toString());
	    					}
	    					pvo.setDr(0);
	    					update(pvo, new String[]{"def11","def12","def4"});
	    				}
	    			  }
	    			}
    		}
    	}
    	//end
	}
	/**
	 * ���ݺ�ͬ�����Ҹ���ƻ�ҳǩ
	 * @param contractcode
	 * @return
	 * @throws DAOException
	 */
 public List<CtApPlanVO> getConttractplan(String contractcode) throws DAOException{
	 return (List<CtApPlanVO>)getBaseDao().executeQuery("select * from fct_ap_plan where  nvl(dr,0)=0 and pk_fct_ap=(select pk_fct_ap from fct_ap where blatest='Y' and nvl(dr,0)=0 and  vbillcode='"+contractcode+"')", new BeanListProcessor(CtApPlanVO.class));  
 }
 
 /**
  * ���ݺ�ͬ�����Ҹ���ƻ���֤��
  * @param contractcode
  * @return
  * @throws DAOException
  */
 public List<PMPlanBVO> getConttractmoney(String contractcode) throws DAOException{
	 return (List<PMPlanBVO>)getBaseDao().executeQuery("select * from fct_pmplan_b where  nvl(dr,0)=0 and pk_fct_ap=(select pk_fct_ap from fct_ap where blatest='Y' and nvl(dr,0)=0 and vbillcode='"+contractcode+"')", new BeanListProcessor(PMPlanBVO.class));
 }
 
 public boolean issettle(String pk) throws DAOException{
	 Object obj=getBaseDao().executeQuery("select t.settlestatus from er_bxzb a inner join cmp_settlement t on  t.pk_busibill=a.pk_jkbx where a.pk_jkbx='"+pk+"'", new ColumnProcessor());
     if(obj!=null){
    	 int status=(int)obj;
    	 if(status==5)return true;
     }
     return true;
 }
 private BaseDAO baseDao;
	public BaseDAO getBaseDao() {
		if(baseDao==null){
			baseDao=new BaseDAO();
		}
	return baseDao;
}
public void setBaseDao(BaseDAO baseDao) {
	this.baseDao = baseDao;
}
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
/**
 * ����vo�ֶη�����
 * @throws BusinessException 
 */
public void update(SuperVO vo, String[] fileds) throws BusinessException{
	ISqlThread thread=NCLocator.getInstance().lookup(ISqlThread.class);
	thread.update__RequiresNew(vo, fileds);
}
}
