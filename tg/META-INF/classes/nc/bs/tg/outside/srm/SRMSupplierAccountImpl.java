package nc.bs.tg.outside.srm;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.ISRMSupplierAccount;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.log.PortalLogger;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OutsideLogVO;

public class SRMSupplierAccountImpl implements ISRMSupplierAccount{
	private	BaseDAO baseDAO;
	@Override
	public String HyperLinkHandler(String userCode,String supplierCode) {

		OutsideLogVO logVO = new OutsideLogVO();
		String redirectUrl =null;
		try {
			logVO.setDesbill("SRM");
			logVO.setDr(0);
			logVO.setSrcsystem("NC融资请款单或贷款合同还款单");
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult("1");
			String vendorId = supplierCode;
			String token = null;
			String sysCode = "nc";
			String secretKey =null;//"5GDF3&&45dsfsSD*FGJGdGJ*HsHKf34";
			String urlstr=null;
			IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			Map<String,String> linkData=  (Map<String, String>) bs.executeQuery("select url,secretKey from nc_urlport where sysid='SupplierAccount'", new MapProcessor());
			urlstr=linkData.get("url");
			secretKey=linkData.get("secretkey");
			String ticket= vendorId+userCode+sysCode+secretKey;
			token = DigestUtils.md5Hex(ticket).toUpperCase();
			redirectUrl = urlstr+"/IspAPP/queryVendInfoServlet?vendorId="+vendorId+"&userCode="+userCode+"&sysCode="+sysCode+"&token="+token+"";
			logVO.setSrcparm(redirectUrl);
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			logVO.setResult("0");
			logVO.setErrmsg(e.getMessage());
			throw new LfwRuntimeException(e.getMessage(), e.getCause());
		} finally {
			try {
				getBaseDAO().insertVO(logVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return redirectUrl;
	}

	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

}
