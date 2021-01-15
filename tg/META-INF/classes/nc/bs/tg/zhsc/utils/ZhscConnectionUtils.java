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
	 * GET�����ȡtoken
	 * @return
	 * @throws Exception
	 */
	public String getToken() throws Exception{
		String address = "/api/baseServer/baseLoginService/login?params={\"userName\":\""+63205+"\",\"pwd\":\""+123456+"\"}";
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// ��ʼ��������Ϣ
		// �����������
		conn.setDoOutput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ(Ĭ��POST)
		//conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");

		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//��Ӧ�ɹ��������Ӧ������
            InputStream is = conn.getInputStream();//�õ�����������������
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
	 * ��ȡ����
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostRequest(String requestUrl, String token,String param) throws Exception{
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// ��ʼ��������Ϣ
		// �����������
		conn.setDoOutput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ(Ĭ��POST)
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// �����ļ�����:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		//��������ͷ
		conn.setRequestProperty("certificate", token);
		
		
		// ��ʼ��������
		//�����������
		OutputStream out = conn.getOutputStream();
		out.write(param.getBytes());
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//��Ӧ�ɹ��������Ӧ������
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //����String�������ڴ��浥������  
            String line=null;  
            //����StringBuffer�������ڴ洢��������  
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
	 * POST�����ȡtoken
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostToken(String urlstr,String param) throws Exception{
		URL url = new URL(urlstr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// ��ʼ��������Ϣ
		// �����������
		conn.setDoOutput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ(Ĭ��POST)
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// �����ļ�����:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		// ת��Ϊ�ֽ�����
		
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(param);
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//��Ӧ�ɹ��������Ӧ������

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //����String�������ڴ��浥������  
            String line=null;  
            //����StringBuffer�������ڴ洢��������  
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
	 * ��ȡ����
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostReturnRequest(String requestUrl, String token,String param) throws Exception{
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// ��ʼ��������Ϣ
		// �����������
		conn.setDoOutput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ(Ĭ��POST)
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// �����ļ�����:
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		//��������ͷ
		conn.setRequestProperty("certificate", token);
		
		
		// ��ʼ��������
		//�����������
		OutputStream out = conn.getOutputStream();
		out.write(param.getBytes());
		out.flush();
		out.close();
		
		String resultMsg = null;
		
		int code = conn.getResponseCode();
		if(code == 200){//��Ӧ�ɹ��������Ӧ������
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            
            //����String�������ڴ��浥������  
            String line=null;  
            //����StringBuffer�������ڴ洢��������  
            StringBuffer sb=new StringBuffer();  
            while((line=br.readLine())!=null){  
                sb.append(line);  
            }   
            resultMsg=sb.toString();
            
        }
		
		return resultMsg;
		
	}

}
