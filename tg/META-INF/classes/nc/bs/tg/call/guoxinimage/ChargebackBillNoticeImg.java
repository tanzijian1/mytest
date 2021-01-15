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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import nc.bs.logging.Logger;
import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.itf.tg.outside.ITGCallService;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

/**
 * NC�����˵�ʱ֪ͨӰ��ɾ���ļ��ӿ� 2020-8-25 10:27:09
 * 
 * @author ln
 * 
 */
public class ChargebackBillNoticeImg implements ITGCallService {

	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		String URL = TGOutsideUtils.getUtils().getOutsidInfo("IMG01");
		// ��Ҫ�ϴ����ļ�·���б�
		CallImplInfoVO info = new CallImplInfoVO();
		info.setDessystem(dessystem);
		info.setUrls(URL);
		info.setClassName("nc.bs.tg.call.guoxinimage.ChargebackBillNoticeImg");
		info.setPostdata(value.toString());
		return info;
	}

	@Override
	public String onCallMethod(CallImplInfoVO info) throws BusinessException {
		String result = null;
		try {
			result = callImageService(info.getPostdata());
			if (result != null) {
				Element root = xmlConvertElement(result);
				String errmsg = root.elementText("errormsg");
				if (errmsg != null && errmsg != "") {
					throw new BusinessException("Ӱ��ϵͳ�쳣:"
							+ root.elementText("errormsg"));
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * ����Ӱ��ӿ�
	 * 
	 * @param xml
	 * @return
	 * @throws BusinessException
	 */
	private String callImageService(String xml) throws BusinessException {
		String url = TGOutsideUtils.getUtils().getOutsidInfo("IMG01");
		String result = "";
		try {
			Object[] results = null;
			results = getClient(url).invoke("ImageInterfaceService",
					new Object[] { xml.toString() });
			Logger.error("���صĲ�����" + results);
			result = (String) results[0];
		} catch (Exception e) {
			throw new BusinessException("Ӱ����ýӿ������쳣:" + e.getMessage());
		}
		return result;
	}

	private Client getClient(String url) throws BusinessException {
		String endpoint = url;
		Client client = null;
		try {
			client = new Client(new URL(endpoint));
			client.setProperty(CommonsHttpMessageSender.HTTP_TIMEOUT,
					String.valueOf(30000));// ���÷��͵ĳ�ʱ����,��λ�Ǻ���;
		} catch (MalformedURLException e) {
			throw new BusinessException(e);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
		return client;
	}

	private Element xmlConvertElement(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		Element root = doc.getRootElement();
		return root;
	}
}
