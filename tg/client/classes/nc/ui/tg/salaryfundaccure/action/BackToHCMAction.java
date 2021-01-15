package nc.ui.tg.salaryfundaccure.action;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.os.outside.TGOutsideUtils;
import nc.itf.tg.outside.IPushBPMLLBillService;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillListView;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;

import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BackToHCMAction extends NCAction {

	private static final long serialVersionUID = 1L;

	private BillManageModel model = null;
	private ShowUpableBillListView listview;
	private nc.ui.pubapp.uif2app.view.ShowUpableBillForm editor;

	private IUAPQueryBS queryBS;

	private BaseDAO baseDAO = null;
	private IPFBusiAction pfBusiAction;
	IMDPersistenceQueryService mdQryService = null;

	public BackToHCMAction() {
		super.setCode("BackToHCM");
		super.setBtnName("�˻�HCM");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		AggregatedValueObject[] selectvos = getSelectedAggVOs();

		IBill data = (IBill) getModel().getSelectedData();
		if (null == data) {
			throw new BusinessException("������ѡ��һ������");
		}
		AggregatedValueObject aggvo = (AggregatedValueObject) data;
		String approvestatus = aggvo.getParentVO()
				.getAttributeValue("approvestatus").toString();
		if ("3".equals(approvestatus) || "-1".equals(approvestatus)) {
			String def1 = (String) aggvo.getParentVO()
					.getAttributeValue("def1");
			String billid = (String) aggvo.getParentVO().getAttributeValue(
					"pk_salaryfundaccure");
			String tradetype = (String) aggvo.getParentVO().getAttributeValue(
					"transtype");
			// Date date =
			String hcmKey = TGOutsideUtils.getUtils().getOutsidInfo("HCM02");
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
			String date = format.format(new Date());
			String accessToken = DigestUtils.md5DigestAsHex((date + hcmKey)
					.getBytes());
			String key = getMethodCode(tradetype);
			String sql = "SELECT user_code FROM sm_user where cuserid = '"
					+ InvocationInfoProxy.getInstance().getUserId() + "'";
			String applyUser = (String) getIUAPQueryBS(sql);
			String ip = TGOutsideUtils.getUtils().getOutsidInfo("HCM01");
			String url = ip + "?sysCode=NC&accessToken=" + accessToken;
			JSONObject json = new JSONObject();
			json.put("srcid", def1);
			json.put("billid", billid);
			json.put("key", key);
			json.put("status", "9");
			json.put("applyUser", applyUser);
			
			String result = getPostRequest(url, "", json.toJSONString());
			Logger.debug("BackToHCMAction" + result);
			String msg = JSON.parseObject(result).getString("msg");
			String code = JSON.parseObject(result).getString("code");
			if ("S".equals(code)) {
				if("3".equals(approvestatus)){
					IPushBPMLLBillService service = NCLocator.getInstance().lookup(
							IPushBPMLLBillService.class);
					service.dealChargebackBill(AggSalaryfundaccure.class, aggvo
							.getParentVO().getPrimaryKey(), "", "HCM1-Cxx-001",false);
					aggvo = (AggSalaryfundaccure) getBillVO(AggSalaryfundaccure.class,"isnull(dr,0)=0 and pk_salaryfundaccure = '" + billid + "'");
				}
	   
				// ɾ������
				HashMap eParam = new HashMap();
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
						PfUtilBaseTools.PARAM_NOTE_CHECKED);
				// ɾ���������뵥
				getPfBusiAction().processAction(IPFActionName.DEL_DELETE, "HCM1", null,
								aggvo, null, eParam);
				String billno  = aggvo.getParentVO().getAttributeValue("billno").toString();
				getModel().directlyDelete(aggvo);
				MessageDialog.showHintDlg(getModel().getContext()
						.getEntranceUI(), "��ʾ", "�˻سɹ�"+billno+"������ɾ��");

			} else {
				throw new BusinessException("HCM:" + msg);
			}
		} else {
			throw new BusinessException("�˵��ݷ�Ĭ������̬���ύ̬�����˻�ɾ����");
		}

	}

	/*
	 * ��ȡѡ��VOs�ľۺ�aggvos
	 * 
	 * @return
	 */
	private AggregatedValueObject[] getSelectedAggVOs() {
		Object[] value = null;
		// �ж���ʾʲô����
		if (getListview().isShowing()) {
			value = (getListview()).getModel().getSelectedOperaDatas();
		} else if (getEditor().isShowing()) {
			value = new Object[1];
			value[0] = ((BillForm) editor).getModel().getSelectedData();
		}

		if (null == value || value.length == 0) {
			return null;
		}
		AggregatedValueObject[] aggs = new AggregatedValueObject[value.length];
		System.arraycopy(value, 0, aggs, 0, aggs.length);
		return aggs;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}

	public ShowUpableBillListView getListview() {
		return listview;
	}

	public void setListview(ShowUpableBillListView listview) {
		this.listview = listview;
	}

	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getEditor() {
		return editor;
	}

	public void setEditor(nc.ui.pubapp.uif2app.view.ShowUpableBillForm editor) {
		this.editor = editor;
	}

	public IUAPQueryBS getQueryBS() {
		if (queryBS == null) {
			queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}

		return queryBS;
	}

	/**
	 * ��ȡ����
	 * 
	 * @param token
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getPostRequest(String requestUrl, String token, String param)
			throws Exception {
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
		conn.setRequestProperty("Content-Type", "application/json");
		// //��������ͷ
		// conn.setRequestProperty("certificate", token);

		// ��ʼ��������
		// �����������
		OutputStream out = conn.getOutputStream();
		out.write(param.getBytes());
		out.flush();
		out.close();

		String resultMsg = null;

		int code = conn.getResponseCode();
		if (code == 200) {// ��Ӧ�ɹ��������Ӧ������
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));

			// ����String�������ڴ��浥������
			String line = null;
			// ����StringBuffer�������ڴ洢��������
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String str = sb.toString();
			// JSONObject result = JSONObject.parseObject(str);

			resultMsg = str;
		}

		return resultMsg;

	}

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	public String getUserName(String cuserid) throws DAOException {
		String sql = "SELECT user_name FROM sm_user where cuserid = '"
				+ cuserid + "'";
		String pk_vid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_vid;
	}

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	protected String getIUAPQueryBS(String sql) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String result = "";
		try {
			result = (String) bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getMethodCode(String tradetype) {
		switch (tradetype) {
		case "F3-Cxx-029":
			return "salaryReqPay";
		case "F3-Cxx-030":
			return "getSSPay";
		case "F3-Cxx-031":
			return "getHCMFundPay";
		case "F3-Cxx-032":
			return "personalIncomeTax";
		case "HCM1-Cxx-001":
			return "salaryFundAccure";
		default:
			return "";
		}
	}
	
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}
	
	 public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			   throws BusinessException {
			  Collection coll = getMDQryService().queryBillOfVOByCond(c,
			    whereCondStr, false);
			  if (coll.size() > 0) {
			   return (AggregatedValueObject) coll.toArray()[0];
			  } else {
			   return null;
			  }
		 }
}
