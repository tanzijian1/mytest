package nc.bs.tg.outside.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import nc.bs.logging.Logger;
import nc.fi.arap.pubutil.RuntimeEnv;

/**
 * 读取配置信息
 * 
 * @author Administrator
 * 
 */
public class PropertiesUtil {
	private static PropertiesUtil pUtil;
	private static Properties config = null;

	private PropertiesUtil(String filename) {
		String url = PropertiesUtil.class
				.getResource("/tg/"+filename).getPath();
		try {
			File file = new File(url);
			InputStream in = new FileInputStream(file);
			if (config == null)
				config = new Properties();
			config.load(new InputStreamReader(in, "gb2312"));
			in.close();
		} catch (IOException e) {
			Logger.error(
					"===No login_on.properties defined error====TGReadProperties===>>",
					e);
		}

	}

	public static PropertiesUtil getInstance(String filename) {
		pUtil = new PropertiesUtil(filename);
		return pUtil;
	}

	public String readValue(String key) {
		try {
			String value = config.getProperty(key);
			if (value != null) {
				value = value.trim();// 去掉空格
			}
			return value;
		} catch (Exception e) {
			Logger.error(
					"===login_on.properties analysis error====agile_send_Email===>>",
					e);
		}
		return null;
	}

}
