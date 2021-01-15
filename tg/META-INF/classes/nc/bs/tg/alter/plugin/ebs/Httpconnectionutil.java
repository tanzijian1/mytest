package nc.bs.tg.alter.plugin.ebs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import nc.bs.pmpub.uap.util.ExceptionUtils;
import nc.vo.pub.BusinessException;

public class Httpconnectionutil {
	public static Httpconnectionutil newinstance(){
		return new Httpconnectionutil();
	}
  public String connection(String address,String data) throws BusinessException{
	 
	  StringBuffer sb=new StringBuffer();
	  String responsecode=null;
	try {
		 URL url = new URL(address);
		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setConnectTimeout(60000);
		connection.setReadTimeout(60000);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.connect();
		if(data!=null){
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8"));
		writer.write(data);
		writer.flush();
		}
		String temp="";
		responsecode=String.valueOf(connection.getResponseCode());
		if(200==connection.getResponseCode()){
			BufferedReader br=new BufferedReader(new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8")));
	       while((temp=br.readLine())!=null){
		      sb.append(temp);
	          }
		}
	} catch (Exception e) {
		// TODO 自动生成的 catch 块
		throw new BusinessException("连接异常 "+responsecode+"    "+e.getMessage());
	}
	  return sb.toString();
  }
  /*
   * 传json
   * */
  public String connectionjson(String address,String data) throws BusinessException{
	  StringBuffer sb=new StringBuffer();
	  String responsecode=null;
	try {
		 URL url = new URL(address);
		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.connect();
		if(data!=null){
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8"));
		writer.write(data);
		writer.flush();
		}
		String temp="";
		responsecode=String.valueOf(connection.getResponseCode());
		if(200==connection.getResponseCode()){
			BufferedReader br=new BufferedReader(new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8")));
	       while((temp=br.readLine())!=null){
		      sb.append(temp);
	          }
		}else{
			throw new BusinessException("响应码"+responsecode);
		}
	} catch (Exception e) {
		// TODO 自动生成的 catch 块
		throw new BusinessException("连接异常 "+responsecode+"    "+e.getMessage());
	}
	  return sb.toString();
  }
}
