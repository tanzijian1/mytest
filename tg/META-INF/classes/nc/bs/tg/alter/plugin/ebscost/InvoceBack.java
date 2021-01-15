package nc.bs.tg.alter.plugin.ebscost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.tg.alter.plugin.ebs.AoutSysncEbsData;
import nc.bs.tg.alter.plugin.ebs.Httpconnectionutil;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.infotransform.IInfoTransformService;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISaveLog;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.hzvat.invoice.AggInvoiceVO;
import nc.vo.hzvat.invoice.InvoiceBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.NcToEbsLogVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

public class InvoceBack extends AoutSysncEbsData{
	
	IMDPersistenceQueryService mdQryService = null;

	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}

	@Override
	protected List<Object[]> getWorkResult(BgWorkingContext bgwc)
			throws BusinessException {
		ArrayList<String> pklist = null;
		String sql = "select def3 from ap_payablebill  where pk_tradetype = 'F1-Cxx-001' and dr = 0 and approvestatus = 1 and def54 <> 'Y'";
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		pklist = (ArrayList<String>) bs.executeQuery(sql,
				new ColumnListProcessor());
		if (pklist != null && pklist.size() > 0) {
			for (String pk : pklist) {
				AggInvoiceVO aggvo = (AggInvoiceVO) getBillVO(
						AggInvoiceVO.class,
						"isnull(dr,0)=0 and def8 = '" + pk + "'");
				String returnjson = null;
				
				if(aggvo!=null){
					returnjson = pushEbsInvoce(aggvo);
				}
				
				if (returnjson == null) {
					throw new BusinessException("��Ʊ��д�������쳣 ����null");
				}
				// ������Ϣ����
				JSONObject jsonobj = JSONObject.parseObject(returnjson);
				String code = (String) jsonobj.get("code");
				if (!("S".equalsIgnoreCase(code))) {
					throw new BusinessException(jsonobj.getString("msg"));
				}

			}
		}
		return null;
	}
	
	private String pushEbsInvoce(AggInvoiceVO aggvo) throws BusinessException{
		
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();// ���Ʊ����
		payapplymap_temp.put("ebs_cbcon_fconid", aggvo.getParentVO().getFph());// ��Ʊϵͳ�����ķ�Ʊid
		payapplymap_temp.put("invoinces_type", aggvo.getParentVO().getFplx());// ��Ʊ����
		payapplymap_temp.put("invoinces_code", aggvo.getParentVO().getFpdm());// ��Ʊ����
		payapplymap_temp.put("invoinces_no", aggvo.getParentVO().getFph());//��Ʊ���
		payapplymap_temp.put("invoinces_date", aggvo.getParentVO().getKprq());//��Ʊ����
		payapplymap_temp.put("vendor_id", aggvo.getParentVO().getSellname());//��Ӧ��id 
		payapplymap_temp.put("invoinces_amount", aggvo.getParentVO().getCostmoneyin());//��Ʊ�ܽ��
		List<Map<String, String>> bodylist = new ArrayList<Map<String, String>>();
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		InvoiceBVO[] bodyvo = (InvoiceBVO[]) aggvo.getChildrenVO();
		for (InvoiceBVO invoiceBVO : bodyvo) {
			bodyMap.put("ebs_cbcon_fconid",(String) invoiceBVO.getAttributeValue("pk_invoice_h"));// ��Ʊϵͳͷ(��Ӧnc�ķ�Ʊͷid)
			bodyMap.put("ebs_cbcon_lines_id", (String) invoiceBVO.getAttributeValue("pk_invoice_b"));// ��Ʊϵͳ��(��Ӧnc�ķ�Ʊ��id)	
			bodyMap.put("untaxed_amount", 
					invoiceBVO.getAttributeValue("moneyouttax")==null?"":
					invoiceBVO.getAttributeValue("moneyouttax").toString());// ��˰���
			bodyMap.put("tax_rate", 
					invoiceBVO.getAttributeValue("taxrate")==null?"":
					invoiceBVO.getAttributeValue("taxrate").toString());// ˰��
			bodyMap.put("tax_amount", 
					invoiceBVO.getAttributeValue("moneytax")==null?"":
					invoiceBVO.getAttributeValue("moneytax").toString());// ˰��
			bodylist.add(bodyMap);
		}
		

		payapplymap_temp.put("line_listData", bodylist);

		// ��������

		// String returnjson = Httpconnectionutil.newinstance().connection(url,
		// "&req=" + getjson(payapplymap_temp));
		String syscode = "test";/*PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("EBSSYSTEM");*/
		String key = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("KEY");
		String token = "";
		if ("nc".equals(syscode)) {
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// ������ʱ��
			String time = formater.format(date);
			String tokenkey = time + key;
			token = MD5Util.getMD5(tokenkey).toUpperCase();
		}
		String code = "response_ap_invoince_date";
		String params = "code=" + code + "&syscode=" + syscode + "&token="
				+ token;
		String address = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("EBSURL");
		String urls = address + "/postMethod?" + params;
		String data = getjson(payapplymap_temp);
		String returnjson = Httpconnectionutil.newinstance().connection(urls,
				"&req=" + data);
		// ��¼��־nctoebslog
		if (returnjson != null) {
			JSONObject jobj = JSON.parseObject(returnjson);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("���Ʊ��д");
			logVO.setNcparm(getjson(payapplymap_temp));
			logVO.setSrcsystem("EBS");
			logVO.setOperator("NC");
			logVO.setExedate(new UFDateTime().toString());
			try {
				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg(msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
					String upsql = "update ap_payablebill set  def54 = 'Y' where def3 = '"+aggvo.getParentVO().getDef8()+"' and nvl(dr,0) = 0";
					String usql = "update hzvat_invoice_h set  def17 = 'Y' where def8 = '"+aggvo.getParentVO().getDef8()+"' and nvl(dr,0) = 0";
					getBaseDAO().executeUpdate(upsql);
					getBaseDAO().executeUpdate(usql);
				} else {
					logVO.setMsg(msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				logVO.setDr(0);
				saveLog.SaveLog_RequiresNew(logVO);
			}
			
		}
		return returnjson;
	}

	/**
	 * ��ȡҵ�񵥾ݾۺ�VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
	
	/**
	 * mapתjson
	 */
	private String getjson(HashMap<String, Object> map) {
		String json = JSON.toJSONString(map);
		return json;
	}
}
