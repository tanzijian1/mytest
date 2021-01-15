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
					throw new BusinessException("发票回写单调用异常 返回null");
				}
				// 返回信息处理
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
		HashMap<String, Object> payapplymap_temp = new HashMap<String, Object>();// 进项发票数据
		payapplymap_temp.put("ebs_cbcon_fconid", aggvo.getParentVO().getFph());// 发票系统过来的发票id
		payapplymap_temp.put("invoinces_type", aggvo.getParentVO().getFplx());// 发票类型
		payapplymap_temp.put("invoinces_code", aggvo.getParentVO().getFpdm());// 发票代码
		payapplymap_temp.put("invoinces_no", aggvo.getParentVO().getFph());//发票编号
		payapplymap_temp.put("invoinces_date", aggvo.getParentVO().getKprq());//发票日期
		payapplymap_temp.put("vendor_id", aggvo.getParentVO().getSellname());//供应商id 
		payapplymap_temp.put("invoinces_amount", aggvo.getParentVO().getCostmoneyin());//发票总金额
		List<Map<String, String>> bodylist = new ArrayList<Map<String, String>>();
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		InvoiceBVO[] bodyvo = (InvoiceBVO[]) aggvo.getChildrenVO();
		for (InvoiceBVO invoiceBVO : bodyvo) {
			bodyMap.put("ebs_cbcon_fconid",(String) invoiceBVO.getAttributeValue("pk_invoice_h"));// 发票系统头(对应nc的发票头id)
			bodyMap.put("ebs_cbcon_lines_id", (String) invoiceBVO.getAttributeValue("pk_invoice_b"));// 发票系统行(对应nc的发票行id)	
			bodyMap.put("untaxed_amount", 
					invoiceBVO.getAttributeValue("moneyouttax")==null?"":
					invoiceBVO.getAttributeValue("moneyouttax").toString());// 无税金额
			bodyMap.put("tax_rate", 
					invoiceBVO.getAttributeValue("taxrate")==null?"":
					invoiceBVO.getAttributeValue("taxrate").toString());// 税率
			bodyMap.put("tax_amount", 
					invoiceBVO.getAttributeValue("moneytax")==null?"":
					invoiceBVO.getAttributeValue("moneytax").toString());// 税额
			bodylist.add(bodyMap);
		}
		

		payapplymap_temp.put("line_listData", bodylist);

		// 数据推送

		// String returnjson = Httpconnectionutil.newinstance().connection(url,
		// "&req=" + getjson(payapplymap_temp));
		String syscode = "test";/*PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("EBSSYSTEM");*/
		String key = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("KEY");
		String token = "";
		if ("nc".equals(syscode)) {
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
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
		// 记录日志nctoebslog
		if (returnjson != null) {
			JSONObject jobj = JSON.parseObject(returnjson);
			String flag = jobj.getString("code");
			String msg = jobj.getString("msg");
			logVO.setTaskname("进项发票回写");
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
	 * 读取业务单据聚合VO
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
	 * map转json
	 */
	private String getjson(HashMap<String, Object> map) {
		String json = JSON.toJSONString(map);
		return json;
	}
}
