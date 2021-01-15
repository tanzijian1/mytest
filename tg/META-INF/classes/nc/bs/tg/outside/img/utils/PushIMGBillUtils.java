package nc.bs.tg.outside.img.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISqlThread;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OutsideLogVO;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PushIMGBillUtils extends IMGBillUtils {
	static PushIMGBillUtils utils;

	public static PushIMGBillUtils getUtils() {
		if (utils == null) {
			utils = new PushIMGBillUtils();
		}
		return utils;
	}

	public void pushBillToImg(Map<String, Object> formData,
			Map<String, Object> formBody, Boolean isLLBill) throws Exception {
		// TODO 自动生成的方法存根
		OutsideLogVO logVO = new OutsideLogVO();
		StringBuffer srcparm = new StringBuffer();
		logVO.setSrcsystem("IMG");
		logVO.setDesbill("影像取发票数据");
		try {
			PropertiesUtil util = PropertiesUtil
					.getInstance("image_url.properties");
			String urls = util.readValue("imgInvUrl");

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
			postdata.put("safety", formData);
			postdata.put("serverbody", formBody);
			String json = objectMapper.writeValueAsString(postdata);
			logVO.setSrcparm(json);
			srcparm.append("NC调用报文：" + json);
			Logger.error("----------请求开始---------------");
			Logger.error("----------请求参数：【" + json + "】---------------");

			// 创建url资源
			URL url = new URL(urls);
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置允许输出
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// 设置不用缓存
			conn.setUseCaches(false);
			// 设置传递方式
			conn.setRequestMethod("POST");
			// 设置维持长连接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置文件字符集:
			conn.setRequestProperty("Charset", "utf-8");
			// 转换为字节数组
			byte[] data = json.getBytes("utf-8");
			// 设置文件长度
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));

			// 设置文件类型:
			conn.setRequestProperty("Content-Type", "application/json");

			// 开始连接请求
			conn.connect();
			OutputStream out = conn.getOutputStream();
			// 写入请求的字符串
			out.write(data);
			out.flush();
			out.close();
			String a = "";
			// 请求返回的状态
			if (conn.getResponseCode() == 200) {
				Logger.error("连接成功");
				// 请求返回的数据
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				String line = null;
				while ((line = in.readLine()) != null) {
					a += line;
				}
				JSONObject result = new JSONObject(a);
				srcparm.append("\n\r影像回传报文：" + a);
				logVO.setSrcparm(srcparm == null ? "" : srcparm.toString());
				String flag = (String) result.getString("result");
				String errmsg = null;
				// String openUrl = null;
				if (flag.equals("0")) {
					org.json.JSONArray jarr = result.getJSONArray("items");
					Logger.error("huangxj进入：insertImgInvToInvoiceVO");
					insertImgInvToInvoiceVO(jarr,isLLBill);
					logVO.setResult(EBSBillUtils.STATUS_SUCCESS);
					// Desktop.getDesktop().browse(new URI(openUrl));
				} else {
					errmsg = (String) result.getString("errormsg");
					throw new BusinessException("【来自影像发票的错误信息：" + errmsg
							+ "】,【NC调用报文" + json + "】");
				}
			} else {
				Logger.error("连接失败");
				throw new BusinessException("连接失败" + json);
			}
		} catch (Exception e) {
			Logger.error("调用影像:" + e.getMessage());
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult(EBSBillUtils.STATUS_FAILED);
			logVO.setOperator(EBSBillUtils.OperatorName);
			logVO.setErrmsg("调用影像:" + e.getMessage());
			throw new BusinessException("连接失败" + e.getMessage(), e);
		} finally {
			try {
				ISqlThread thread = NCLocator.getInstance().lookup(
						ISqlThread.class);
				thread.billInsert_RequiresNew(logVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
