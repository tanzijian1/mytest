package nc.bs.tg.outside.image;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.IImageFileUploadService;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class ImageFileUploadServiceImpl implements IImageFileUploadService{
	public static final String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
	public static byte[] END_DATA_BYTES = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线

	@Override
	public String uploadFile(String urlstr, Map<String, String> fromData,
			File[] files) throws BusinessException {
		try {
			//String URL = TGOutsideUtils.getUtils().getOutsidInfo("IMG03");
			URL url = new URL(urlstr);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
           
            onWriteInfo(conn, files,fromData);
            
            String msg = getResultInfo(conn);
            
            return msg;
		}catch (Exception e) {
        	throw new BusinessException("附件信息处理异常：" + e.getMessage(), e);
        }
	}
	
	
	protected void onWriteInfo(HttpURLConnection conn, File[] files, Map<String, String> fromData) throws IOException {

		OutputStream out = new DataOutputStream(conn.getOutputStream());
		// 将业务参数写入form-data
		out.write(getFormData(fromData, BOUNDARY).getBytes("UTF-8"));
		
		for (File file : files) {
			//String file = fileArr.getString(i);
			//String fileName = fileNameArr.getString(i);				 
			
			StringBuilder fileData = new StringBuilder();
			fileData.append("--" + BOUNDARY + "\r\n");
			fileData.append("Content-Disposition: form-data;name=\"myFile\";filename=\""
					+ file.getName() + "\"\r\n");
			fileData.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] data = fileData.toString().getBytes("utf-8");
			out.write(data);
			
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
		}
		
		out.write(END_DATA_BYTES);
		out.flush();
		out.close();
	}
	
	
    public String updateFile(String urlstr,Map<String, String> fromData,String filePath) throws BusinessException {
    	try {
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            //将业务参数写入form-data
            out.write(getFormData(fromData, BOUNDARY).getBytes("UTF-8"));
            //将文件流写入输出流
            if (filePath != null && !filePath.isEmpty()){
                File file = new File(filePath);
                StringBuilder fileData = new StringBuilder();
                fileData.append("--" + BOUNDARY + "\r\n");
                fileData.append("Content-Disposition: form-data;name=\"myFile\";filename=\"" + file.getName() + "\"\r\n");
                fileData.append("Content-Type:application/octet-stream\r\n\r\n");
                byte[] data = fileData.toString().getBytes("utf-8");
                out.write(data);
                DataInputStream in = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                in.close();
            }
            out.write(END_DATA_BYTES);
            out.flush();
            out.close();
            
            String msg = getResultInfo(conn);
            
            return msg;

        } catch (Exception e) {
        	throw new BusinessException("附件信息处理异常：" + e.getMessage(), e);
        }
    }
    
    private String getFormData(Map<String,String> formData, String boundary) throws UnsupportedEncodingException {
        StringBuilder formStr = new StringBuilder();
        Iterator it = formData.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = formData.get(key);
            formStr.append(getFormData(key, value, boundary));
        }
        return formStr.toString();
    }
    
    private String getFormData( String name, String value, String boundary) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();

        sb.append("--" + boundary + "\r\n");
        //参数名
        sb.append("Content-Disposition: form-data; name=\"" + name + "\" \r\n\r\n");
        //参数值
        sb.append(value + "\r\n");
        return sb.toString();
    }
    
    protected String getResultInfo(HttpURLConnection conn)
			throws BusinessException {
		String resultMsg = null;
		try {
			// 请求返回的状态
			String msg = "";
			if (conn.getResponseCode() == 200) {
				// 请求返回的数据
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				String line = "";
				while ((line = in.readLine()) != null) {
					msg += line;
				}
				JSONObject result = JSON.parseObject(msg);
				
				resultMsg = result.toJSONString();
			} else {
				throw new BusinessException("连接失败");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}


	@Override
	public String uploadFileByByte(String urlstr, Map<String, String> fromData,byte[] fileBuffer,String filname) throws BusinessException {
		try {
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            //将业务参数写入form-data
            out.write(getFormData(fromData, BOUNDARY).getBytes("UTF-8"));
            //将文件流写入输出流
            if (fileBuffer != null){
                //File file = new File(filePath);
                StringBuilder fileData = new StringBuilder();
                fileData.append("--" + BOUNDARY + "\r\n");
                fileData.append("Content-Disposition: form-data;name=\"myFile\";filename=\"" + filname + "\"\r\n");
                fileData.append("Content-Type:application/octet-stream\r\n\r\n");
                byte[] data = fileData.toString().getBytes("utf-8");
                out.write(data);
                //InputStream inputStream = new ByteArrayInputStream(fileBuffer);
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(fileBuffer));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                in.close();
            }
            out.write(END_DATA_BYTES);
            out.flush();
            out.close();
            
            String msg = getResultInfo(conn);
            
            return msg;

        } catch (Exception e) {
        	throw new BusinessException("附件信息处理异常：" + e.getMessage(), e);
        }
	}


	@Override
	public String uploadFileByStream(String urlstr,Map<String, String> fromData, InputStream inputStream,String filname) throws BusinessException {
		try {
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            //将业务参数写入form-data
            out.write(getFormData(fromData, BOUNDARY).getBytes("UTF-8"));
            //将文件流写入输出流
            if (inputStream != null){
                //File file = new File(filePath);
                StringBuilder fileData = new StringBuilder();
                fileData.append("--" + BOUNDARY + "\r\n");
                fileData.append("Content-Disposition: form-data;name=\"myFile\";filename=\"" + filname + "\"\r\n");
                fileData.append("Content-Type:application/octet-stream\r\n\r\n");
                byte[] data = fileData.toString().getBytes("utf-8");
                out.write(data);
                //InputStream inputStream = new ByteArrayInputStream(fileBuffer);
                DataInputStream in = new DataInputStream(inputStream);
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                in.close();
            }
            out.write(END_DATA_BYTES);
            out.flush();
            out.close();
            
            String msg = getResultInfo(conn);
            
            return msg;

        } catch (Exception e) {
        	throw new BusinessException("附件信息处理异常：" + e.getMessage(), e);
        }
	}


	@Override
	public AggregatedValueObject getBillVo(Class c, String whereCondStr) throws BusinessException {
		try {
			AggregatedValueObject billVO = BillUtils.getUtils().getBillVO(c, whereCondStr);
			return billVO;
		} catch (BusinessException e) {
			throw new BusinessException("获取处理后的单据失败" + e.getMessage(), e);
		}
	}

	@Override
	public String deleteFile(String urlstr, Map<String, String> fromData) throws BusinessException {
		try {
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            //将业务参数写入form-data
            out.write(getFormData(fromData, BOUNDARY).getBytes("UTF-8"));
            
            out.write(END_DATA_BYTES);
            out.flush();
            out.close();
            
            String msg = getResultInfo(conn);
            
            return msg;

        } catch (Exception e) {
        	throw new BusinessException("删除附件信息异常：" + e.getMessage(), e);
        }
	}
}
