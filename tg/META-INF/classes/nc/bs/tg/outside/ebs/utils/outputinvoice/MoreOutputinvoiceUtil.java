package nc.bs.tg.outside.ebs.utils.outputinvoice;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import uap.serverdes.appesc.MD5Util;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.outputinvoice.thread.MoreThreadRunable;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.hzvat.IOutputtaxMaintain;
import nc.itf.tg.ISqlThread;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.hzvat.billmaintenance.BillmaintenanceVO;
import nc.vo.hzvat.outputtax.AggOutputTaxHVO;
import nc.vo.hzvat.outputtax.OutputTaxBVO;
import nc.vo.hzvat.outputtax.OutputTaxHVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outputtax.NCOutputtaxDetailsJsonVO;
import nc.vo.tg.outputtax.NcOutputtaxJsonVO;
import nc.vo.tg.outside.OutsideLogVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class MoreOutputinvoiceUtil extends SaleBillUtils {

	static MoreOutputinvoiceUtil utils;
	static ArrayBlockingQueue<Runnable> arrayWorkQueue = new ArrayBlockingQueue<>(500);
	static ExecutorService threadPool = new ThreadPoolExecutor(
	        10, //corePoolSize�̳߳��к����߳���
	        50, //maximumPoolSize �̳߳�������߳���
	        60, //�̳߳����̵߳�������ʱ�䣬�������ʱ������߳̽�������
	        TimeUnit.SECONDS,//ʱ�䵥λ
	        arrayWorkQueue,
	        new ThreadPoolExecutor.DiscardOldestPolicy());

	public static MoreOutputinvoiceUtil getUtils() {
		if (utils == null) {
			utils = new MoreOutputinvoiceUtil();
		}
		return utils;
	}

	/**
	 * ���Ʊ��ϵͳ�ӿڲ������
	 * 
	 * @param value
	 * @param dectype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> map, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);
		MoreThreadRunable run=new MoreThreadRunable(map);
 		threadPool.execute(run);
		return "���óɹ�";
	}

	
}
