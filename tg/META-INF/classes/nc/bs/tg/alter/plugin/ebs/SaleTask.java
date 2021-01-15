package nc.bs.tg.alter.plugin.ebs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISqlThread;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.OutsideLogVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/*
 * ����ϵͳ��д
 * */
public class SaleTask implements IBackgroundWorkPlugin{

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		StringBuffer errorsb=new StringBuffer();
		OutsideLogVO logvo=new OutsideLogVO();
		StringBuffer sbs=new StringBuffer();
		StringBuffer sbsa =new StringBuffer();
		OutsideLogVO logvo1=new OutsideLogVO();
		ISqlThread  thread =NCLocator.getInstance().lookup(ISqlThread.class);
		logvo.setDesbill("����ϵͳ��дƾ֤");
		logvo.setResult("1");
		logvo.setSrcsystem("SALE");
//		logvo1.setDesbill("����ϵͳ��д����");
		logvo1.setResult("1");
		logvo1.setDesbill("����ϵͳ��д����");
		logvo1.setSrcparm("SALE");
		try{
		BaseDAO dao=new BaseDAO();
		List<HashMap<String, Object>> listmap=new ArrayList<HashMap<String,Object>>();
		String url =PropertiesUtil.getInstance("ebs_url.properties").readValue("SALEURL");//���ۻ�д
		String czurl=PropertiesUtil.getInstance("ebs_url.properties").readValue("CZURL");//����url
////		/*����ϵͳ��д*/
		String paysql="select g.pk_voucher ,def1 as saleid,'1' as ispzid, billno,to_char(g.num) as  ncdocnum,'1' as shr,a.pk_tradetype as pk_tradetype  from ap_paybill a inner join fip_relation f on f.src_relationid=a.pk_paybill inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid  where a.billmaker=(select cuserid  from sm_user where user_name='SALE')   and f.des_relationid is not null  and nvl(a.dr,0)=0 and nvl(g.free1,'N')!='Y'";
		String gathersql="  select g.pk_voucher ,def1 as saleid,billno,'1' as shr,to_char(g.num) as  ncdocnum,'1' as ispzid,a.pk_tradetype as pk_tradetype from ar_gatherbill a inner join fip_relation f on f.src_relationid=a. pk_gatherbill inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid  where a.billmaker=(select cuserid  from sm_user where user_name='SALE')   and f.des_relationid is not null and nvl(a.dr,0)=0 and nvl(g.free1,'N')!='Y'";		
		//��Ʊ��
		String changesql="select g.pk_voucher ,t.def1 as saleid,'1' as ispzid,t.billno,to_char(g.num) as ncdocnum,'1' as shr,'' as pk_tradetype from tgfn_changeBill_h t inner join fip_relation f  on f.src_relationid=t.pk_changebill_h inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid where t.billmaker=(select cuserid  from sm_user where user_name='SALE') and f.des_relationid is not null and nvl(t.dr,0)=0 and nvl(g.free1,'N')!='Y'";
		//��̯��
		String ftsql="select g.pk_voucher ,t.def1 as saleid,'1' as ispzid,t.billno,to_char(g.num) as ncdocnum,'1' as shr,'' as pk_tradetype from tgfn_distribution t inner join fip_relation f  on f.src_relationid=t.pk_distribution inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid where t.billmaker=(select cuserid  from sm_user where user_name='SALE') and f.des_relationid is not null and nvl(t.dr,0)=0 and nvl(g.free1,'N')!='Y'";
		//��ת��ת��
		String czdsql="select g.pk_voucher ,t.def1 as saleid,'1' as ispzid,t.billno, to_char(g.num) as ncdocnum,'1' as shr,'' as pk_tradetype from tgfn_transferBill_h t inner join fip_relation f  on f.src_relationid=t.pk_transferbill_h inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid where t.billmaker=(select cuserid  from sm_user where user_name='SALE') and f.des_relationid is not null and nvl(t.dr,0)=0 and nvl(g.free1,'N')!='Y'";
		//̢������
		String tdsql="select g.pk_voucher ,t.def1 as saleid,'1' as ispzid,t.billno, to_char(g.num) as ncdocnum,'1' as shr,'' as pk_tradetype from tg_tartingbill_h t inner join fip_relation f  on f.src_relationid=t.pk_tartingbill_h inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid where t.billmaker=(select cuserid  from sm_user where user_name='SALE') and f.des_relationid is not null and nvl(t.dr,0)=0 and nvl(g.free1,'N')!='Y'";
		//����ת�˵�(ת�˵�)
		String hf_sql="select g.pk_voucher ,t.def1 as saleid,'1' as ispzid,t.billno, to_char(g.num) as ncdocnum,'1' as shr,'' as pk_tradetype from tgfn_exhousetransferbill_h t inner join fip_relation f  on f.src_relationid=t.pk_exhoutranbill_h inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid where t.billmaker=(select cuserid  from sm_user where user_name='SALE') and f.des_relationid is not null and nvl(t.dr,0)=0 and nvl(g.free1,'N')!='Y'";
		//���վݵ�
		String hrecsql="select g.pk_voucher ,t.def1 as saleid,'1' as ispzid,t.billno, to_char(g.num) as ncdocnum,'1' as shr,'' as pk_tradetype from tgfn_renameChangeBill_h t inner join fip_relation f  on f.src_relationid=t.pk_renamechbill_h inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid where t.billmaker=(select cuserid  from sm_user where user_name='SALE') and f.des_relationid is not null and nvl(t.dr,0)=0 and nvl(g.free1,'N')!='Y'";
		//̢�������
		String tdactsql="select g.pk_voucher ,t.def1 as saleid,'1' as ispzid,t.billno, to_char(g.num) as ncdocnum,'1' as shr,'' as pk_tradetype from tgfn_targetactivation t inner join fip_relation f  on f.src_relationid=t.pk_target inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid where t.billmaker=(select cuserid  from sm_user where user_name='SALE') and f.des_relationid is not null and nvl(t.dr,0)=0 and nvl(g.free1,'N')!='Y'";
		List<HashMap<String, Object>> paylistmap=(List<HashMap<String, Object>>)dao.executeQuery(paysql, new MapListProcessor());//�����д
		List<HashMap<String, Object>> reclistmap=(List<HashMap<String, Object>>)dao.executeQuery(gathersql, new MapListProcessor());//�տ��д
		List<HashMap<String, Object>> changelistmap=(List<HashMap<String, Object>>)dao.executeQuery(changesql, new MapListProcessor());//��Ʊ��
		List<HashMap<String, Object>> ftlistmap=(List<HashMap<String, Object>>)dao.executeQuery(ftsql, new MapListProcessor());//��̯��
		List<HashMap<String, Object>> czdlistmap=(List<HashMap<String, Object>>)dao.executeQuery(czdsql, new MapListProcessor());//��ת��ת��
		List<HashMap<String, Object>> tdlistmap=(List<HashMap<String, Object>>)dao.executeQuery(tdsql, new MapListProcessor());//̢������
		List<HashMap<String, Object>> hflistmap=(List<HashMap<String, Object>>)dao.executeQuery(hf_sql, new MapListProcessor());//����ת�˵�(ת�˵�)
		List<HashMap<String, Object>> hreclistmap=(List<HashMap<String, Object>>)dao.executeQuery(hrecsql, new MapListProcessor());//���վݵ�
		List<HashMap<String, Object>> tdalistmap=(List<HashMap<String, Object>>)dao.executeQuery(tdactsql, new MapListProcessor());//̢�������
		listmap.addAll(paylistmap);
		listmap.addAll(reclistmap);
		listmap.addAll(changelistmap);
		listmap.addAll(ftlistmap);
		listmap.addAll(czdlistmap);
		listmap.addAll(tdlistmap);
		listmap.addAll(hflistmap);
		listmap.addAll(hreclistmap);
		listmap.addAll(tdalistmap);
		List<HashMap<String, Object>>  listerr=new ArrayList<HashMap<String, Object>>();
		for(HashMap<String, Object> map:listmap){
			if(map==null)
				continue;
			HashMap<String, Object> errmap=new HashMap<String, Object>();
			HashMap<String, Object> mapdata=new HashMap<String, Object>();
			String tradetype="0";
			mapdata.put("vouchid", map.get("saleid"));//����ϵͳ����ID
			mapdata.put("ncDocumentNo", map.get("billno"));//���ݺ�
			mapdata.put("generateCredentials", map.get("ispzid"));//�Ƿ�����ƾ֤
			mapdata.put("ncDocumentnumber", map.get("ncdocnum"));//NCƾ֤��
			mapdata.put("shr", map.get("shr"));//����Ƿ�ɹ�
			if("F2-Cxx-001".equals(map.get("pk_tradetype"))|| "F3-Cxx-001".equals(map.get("pk_tradetype"))){
				tradetype="1";
			}
			mapdata.put("isDj", tradetype);//�Ƿ�ת������ݣ�1Ϊ�ǣ�
			String data=JSON.toJSONString(mapdata);
	        sbs.append(data);
			thread.salecz_RequiresNew(mapdata, 2,(String)map.get("pk_voucher"));
		}
		/*���˽ӿ�*/
		logvo.setSrcparm(sbs.toString());
		String czsql="select t.settledate, t.pk_settlement,(select bd_psndoc.code from sm_user left join bd_psndoc "
				+ " on bd_psndoc.pk_psndoc = sm_user.pk_psndoc "
				+ " where nvl(bd_psndoc.dr,0) = 0 and nvl(sm_user.dr,0) = 0 and sm_user.cuserid = t.pk_executor) adaccount,"
				+ " bb.accname bank,a.def1  from ap_payitem item "
				+ "left join ap_paybill a on a.pk_paybill=item.pk_paybill "
				+ "inner join bd_bankaccsub bb on bb.pk_bankaccsub =item.payaccount  "
				+ "inner  join cmp_settlement t on  a.pk_paybill = t.pk_busibill "
				+ "where a.creator=(select cuserid  from sm_user where user_name='SALE') "
				+ "and nvl(a.dr,0)=0 and t.settlestatus='5' and nvl(t.def1,'N')!='Y'";
		List<HashMap<String, Object>>  maplist=(List<HashMap<String, Object>>)dao.executeQuery(czsql, new MapListProcessor());
		List<HashMap<String, Object>>  errlist=new ArrayList<HashMap<String, Object>>();
		for(HashMap<String, Object> map:maplist){
			HashMap<String, Object> errmap=new HashMap<String, Object>();
			HashMap<String, Object> mapdata=new HashMap<String, Object>();
			mapdata.put("vouchID", map.get("def1"));
			mapdata.put("rzBank", map.get("bank"));
			mapdata.put("adaccount", map.get("adaccount"));
			mapdata.put("issuccess", 1);
			mapdata.put("command", "ͨ��");
			mapdata.put("rzdate", map.get("settledate"));//��������
			String data=JSON.toJSONString(mapdata);
			sbsa.append(data+"*");
			logvo1.setSrcparm(data);
			thread.salecz_RequiresNew(mapdata,1,(String)map.get("pk_settlement"));
		}
		logvo1.setSrcparm(sbsa.toString());
		logvo1.setErrmsg(JSONObject.toJSONString(errlist));
		bgwc.setLogStr(errorsb.toString());
		}catch(Exception e){
			throw new BusinessException(e.getMessage());
		}
		return null;
	}

}
