package nc.bs.tg.call.guoxinimage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Iterator;

import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 上传附件到国信影像系统验证发票真伪
 * 
 * @author bobo
 * 
 */
public class FileUploadCheckFake extends TGImplCallHttpClient {
	public static final String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
	public static byte[] END_DATA_BYTES = ("\r\n--" + BOUNDARY + "--\r\n")
			.getBytes();// 定义最后数据分隔线
	public JSONObject fromData = new JSONObject();
	public JSONArray pathList = new JSONArray();

	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		String URL = TGOutsideUtils.getUtils().getOutsidInfo("IMG02");
		// 需要上传的文件路径列表
		CallImplInfoVO info = new CallImplInfoVO();
		info.setDessystem(dessystem);
		info.setUrls(URL);
		info.setClassName("nc.bs.tg.call.guoxinimage.FileUploadCheckFake");
		info.setPostdata(JSON.toJSONString(value));
		return info;
	}

	@Override
	protected String onBusinessProcessing(CallImplInfoVO info, String result) {
		// TODO 自动生成的方法存根
		return result;
	}

	@Override
	protected void initConnParameter(HttpURLConnection conn, CallImplInfoVO info)
			throws ProtocolException, IOException {
		// 设置允许输出
		conn.setDoOutput(true);
		// 设置允许输入
		conn.setDoInput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式
		conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", getEncoding());
		// 设置文件类型:
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);
	}

	@Override
	protected void onWriteInfo(HttpURLConnection conn, String postdata)
			throws IOException {
		JSONObject reqData = JSON.parseObject(postdata);
		setFromData(reqData.getJSONObject("datas"));
		setPathList(reqData.getJSONArray("files"));
		OutputStream out = new DataOutputStream(conn.getOutputStream());
		// 将业务参数写入form-data
		out.write(getFormData(getFromData(), BOUNDARY).getBytes("UTF-8"));
		JSONArray filePaths = getPathList();
		// 有需要上传的文件
		if (filePaths != null && filePaths.size() > 0) {
			// 遍历添加文件到HTTP输出流
			for (int i = 0; i < filePaths.size(); i++) {
				// 将读取文件写入输出流
				if (filePaths.getString(i) != null
						&& !filePaths.getString(i).isEmpty()) {
					File file = new File(filePaths.getString(i));
					StringBuilder fileData = new StringBuilder();
					fileData.append("--" + BOUNDARY + "\r\n");
					fileData.append("Content-Disposition: form-data;name=\"myFile\";filename=\""
							+ file.getName() + "\"\r\n");
					fileData.append("Content-Type:application/octet-stream\r\n\r\n");
					byte[] data = fileData.toString().getBytes("utf-8");
					out.write(data);
					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}
		}
		out.write(END_DATA_BYTES);
		out.flush();
		out.close();
	}

	@Override
	protected String getResultInfo(HttpURLConnection conn, CallImplInfoVO info)
			throws BusinessException {
		String resultMsg = null;
		try {
			// 请求返回的状态
			String msg = "";
			if (conn.getResponseCode() == 200) {
				// 请求返回的数据
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), getEncoding()));
				String line = "";
				while ((line = in.readLine()) != null) {
					msg += line;
				}
				JSONObject result = JSON.parseObject(msg);
				String flag = (String) result.getString("Result");
				if (!flag.equals("0")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("errormsg") + "】");
				} else {
					resultMsg = result.toJSONString();
				}
			} else {
				throw new BusinessException("连接失败");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}

	public String getFormData(JSONObject formData, String boundary) {
		StringBuilder formStr = new StringBuilder();
		Iterator<String> it = formData.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = formData.getString(key);
			formStr.append(getFormData(key, value, boundary));
		}
		return formStr.toString();
	}

	private String getFormData(String name, String value, String boundary) {
		StringBuilder sb = new StringBuilder();
		sb.append("--" + boundary + "\r\n");
		// 参数名
		sb.append("Content-Disposition: form-data; name=\"" + name
				+ "\" \r\n\r\n");
		// 参数值
		sb.append(value + "\r\n");
		return sb.toString();
	}

	public JSONObject getFromData() {
		return fromData;
	}

	public void setFromData(JSONObject fromData) {
		this.fromData = fromData;
	}

	public JSONArray getPathList() {
		return pathList;
	}

	public void setPathList(JSONArray pathList) {
		this.pathList = pathList;
	}
}
