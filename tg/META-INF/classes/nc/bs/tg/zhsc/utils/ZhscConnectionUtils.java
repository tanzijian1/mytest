package nc.bs.tg.zhsc.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;

public class ZhscConnectionUtils {
	
	private static ZhscConnectionUtils conUtils;
	
	public static ZhscConnectionUtils getInstance() {
		if (conUtils == null) {
			conUtils = new ZhscConnectionUtils();
		}
		return conUtils;
	}
	
	/**
	 * GET请求获取token
	 * @return
	 * @throws Exception
	 */
	public String getToken() throws Exception{
		String address = "/api/baseServer/baseLoginService/login?params={\"userName\":\""+63205+"\",\"pwd\":\""+123456+"\"}";
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// 初始化配置信息
		// 设置允许输出
		conn.setDoOutput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式(默认POST)
		//conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", "utf-8");
		// 设置文件类型:
		conn.setRequestProperty("Content-Type", "application/json");

		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//相应成功，获得相应的数据
            InputStream is = conn.getInputStream();//得到数据流（输入流）
            byte[] buffer = new byte[1024];
            int length = 0;
            String data = "";
            while((length = is.read(buffer)) != -1){
                String str = new String(buffer,0,length);
                data += str;
            }
            JSONObject result = JSONObject.parseObject(data);
            
            resultMsg = result.getString("data");
        }
		
		return resultMsg;
		
	}
	
	/**
	 * 获取数据
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostRequest(String requestUrl, String token,String param) throws Exception{
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// 初始化配置信息
		// 设置允许输出
		conn.setDoOutput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式(默认POST)
		conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", "utf-8");
		// 设置文件类型:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		//设置请求头
		conn.setRequestProperty("certificate", token);
		
		
		// 开始连接请求
		//输入请求参数
		OutputStream out = conn.getOutputStream();
		out.write(param.getBytes());
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//相应成功，获得相应的数据
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //定义String类型用于储存单行数据  
            String line=null;  
            //创建StringBuffer对象用于存储所有数据  
            StringBuffer sb=new StringBuffer();  
            while((line=br.readLine())!=null){  
                sb.append(line);  
            }   
            String str=sb.toString();
            JSONObject result = JSONObject.parseObject(str);
            
            resultMsg = result.getString("data");
        }
		
		return resultMsg;
		
	}
	
	/**
	 * POST请求获取token
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostToken(String urlstr,String param) throws Exception{
		URL url = new URL(urlstr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// 初始化配置信息
		// 设置允许输出
		conn.setDoOutput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式(默认POST)
		conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", "utf-8");
		// 设置文件类型:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		// 转换为字节数组
		
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(param);
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//相应成功，获得相应的数据

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //定义String类型用于储存单行数据  
            String line=null;  
            //创建StringBuffer对象用于存储所有数据  
            StringBuffer sb=new StringBuffer();  
            while((line=br.readLine())!=null){  
                sb.append(line);  
            }   
            String str=sb.toString();
            
            JSONObject result = JSONObject.parseObject(str);
            resultMsg = result.getString("data");
            JSONObject datajson = JSONObject.parseObject(resultMsg);
            resultMsg = datajson.getString("certificate");
        }
		
		return resultMsg;
		
	}
	
	/**
	 * 获取数据
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostReturnRequest(String requestUrl, String token,String param) throws Exception{
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// 初始化配置信息
		// 设置允许输出
		conn.setDoOutput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式(默认POST)
		conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", "utf-8");
		// 设置文件类型:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		//设置请求头
		conn.setRequestProperty("certificate", token);
		
		
		// 开始连接请求
		//输入请求参数
		OutputStream out = conn.getOutputStream();
		out.write(param.getBytes());
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//相应成功，获得相应的数据
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //定义String类型用于储存单行数据  
            String line=null;  
            //创建StringBuffer对象用于存储所有数据  
            StringBuffer sb=new StringBuffer();  
            while((line=br.readLine())!=null){  
                sb.append(line);  
            }   
            resultMsg=sb.toString();
            
        }
		
		return resultMsg;
		
	}

}
