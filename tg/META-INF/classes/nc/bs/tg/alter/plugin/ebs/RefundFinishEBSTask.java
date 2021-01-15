package nc.bs.tg.alter.plugin.ebs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tools.ant.BuildException;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISqlThread;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OutsideLogVO;

/**
 * 
 * 退款完成接口EBS接口
 * */
public class RefundFinishEBSTask implements IBackgroundWorkPlugin {

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		StringBuffer sb = new StringBuffer();
		ISqlThread insert=NCLocator.getInstance().lookup(ISqlThread.class);
		List<OutsideLogVO> listlog=new ArrayList<OutsideLogVO>();
		try {
			BaseDAO dao = new BaseDAO();
			// TODO 自动生成的方法存根
			String url = PropertiesUtil.getInstance("ebs_url.properties")
					.readValue("TKURL");
			String token = "";
			String syscode = PropertiesUtil.getInstance("ebs_url.properties")
					.readValue("EBSSYSTEM");
			String key = PropertiesUtil.getInstance("ebs_url.properties")
					.readValue("KEY");
			if ("nc".equals(syscode)) {
				Date date = new Date();
				SimpleDateFormat formater = new SimpleDateFormat(
						"yyyyMMddHHmm");// 年月日时分
				String time = formater.format(date);
				String tokenkey = time + key;
				token = MD5Util.getMD5(tokenkey).toUpperCase();

			}
			// UFDateTime time = AppContext.getInstance().getServerTime();
			// String date = time.getYear() + time.getStrMonth()
			// + time.getStrDay() + time.getLocalHour()
			// + time.getLocalMinute();
			// token = date + MD5Util.getMD5(key);
			String sql = "select  a.billno as billno, item.local_money_cr as money,item.def30 as def30,a.billdate as billdate	 from ar_gatherbill a  inner join ar_gatheritem item on  item.pk_gatherbill=a.pk_gatherbill  where a.pk_tradetype='F2-Cxx-007' and a.approvestatus=1 and nvl(a.def10,'N') <> 'Y'";
			List<HashMap<String, Object>> listmap = (List<HashMap<String, Object>>) dao
					.executeQuery(sql, new MapListProcessor());
			for (HashMap<String, Object> map : listmap) {
				HashMap<String, Object> map_temp = new HashMap<String, Object>();
				OutsideLogVO log=new OutsideLogVO();
				log.setDesbill("退款完成");
				log.setResult("1");
				listlog.add(log);
				String def2 = (String) dao
						.executeQuery(
								"select def2 from tgfn_payrequest  where pk_payreq='"
										+ map.get("def30") + "'",
								new ColumnProcessor());
				if (def2 == null)
					continue;
				map_temp.put("p_Applynumber", def2);// 请款单单号
				map_temp.put("p_Refundmoney", map.get("money"));// 退款金额
				map_temp.put("p_Ncrecpnumber", map.get("billno"));// Nc收款单号
				if (map.get("billdate") != null) {
					UFDate uf = new UFDate((String) map.get("billdate"));
					String s = uf.getYear() + "-" + uf.getMonth() + "-"
							+ uf.getDay();
					map_temp.put("p_Refunddate", s);// 退款日期
				}
				String data = JSON.toJSONString(map_temp);
				log.setSrcparm(data);
				String returnjson = Httpconnectionutil.newinstance()
						.connection(url+token, "&req=" + data);
				JSONObject jobj = JSONObject.parseObject(returnjson);
				String code = jobj.getString("code");
				if (!("S".equals(code))) {
					log.setResult("0");
					log.setErrmsg(jobj.getString("msg"));
					String msg = jobj.getString("msg") + "***" + def2;
					sb.append(msg);
				} else if ("S".equals(code)) {
					JSONObject obj = JSON.parseObject(data);
					String billno = obj.getString("p_Ncrecpnumber");
					BaseDAO upddao = new BaseDAO();
					String filtersql = "update ar_gatherbill set def10='Y' where billno='"
							+ billno + "'";
					insert.insertsql_RequiresNew(filtersql);
				}
			}
		} catch (Exception e) {
			sb.append(e.getMessage());
			bgwc.setLogStr(sb.toString());
		}finally{
			for(OutsideLogVO lvo:listlog){//处理报错日志
            	insert.billInsert_RequiresNew(lvo);
            	}
		}
		if (sb.length() > 0) {
			throw new BuildException(sb.toString());
		}
		return null;
	}

}
