package nc.ui.tg.financingexpense.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.bs.tg.util.UtilHelp;
import nc.ui.pubapp.uif2app.actions.pflow.CommitScriptAction;
import nc.vo.pub.BusinessException;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FinancingExpenseCommitAction extends CommitScriptAction {

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO �Զ����ɵķ������
		super.doAction(e);
		// pushBillToBpm();
	}

	/**
	 * ����bpm�����ӿ�
	 * 
	 * @author lyq 2018511
	 * @param aggvo
	 * @param url
	 *            �����ַ
	 * @throws BusinessException
	 */
	public void pushBillToBpm(String urls, AggFinancexpenseVO aggvo)
			throws BusinessException {
		// TODO add by lyq
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			FinancexpenseVO parent = aggvo.getParentVO();
			Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
			UtilHelp util = new UtilHelp();
			// ������֯
			Object[] orgmsg = util.getOrgmsg((String) parent
					.getAttributeValue("pk_org"));
			purchase.put("org_code", orgmsg[0]);// ��֯����
			purchase.put("org_name", orgmsg[1]);// ��֯����
			// ���벿��
			Object[] deptmsg = util.getDeptmsg((String) parent
					.getAttributeValue("pk_applicationdept"));
			purchase.put("applicationdept_code", deptmsg[0]);// ���벿�ű���
			purchase.put("applicationdept_name", deptmsg[1]);// ���벿������
			// ������
			String applicant = util.getPerson_name((String) parent
					.getAttributeValue("pk_applicant"));
			purchase.put("applicant_name", applicant);// ����������
			// �������
			Object[] flowMsg = util.getFlowMsg((String) parent
					.getAttributeValue("pk_flow"));
			purchase.put("flow_code", flowMsg[0]);// �������������
			purchase.put("flow_name", flowMsg[1]);// �����������
			// ���빫˾
			Object[] applicationorg = util.getOrgmsg((String) parent
					.getAttributeValue("pk_applicationorg"));
			purchase.put("applicationorg_name", applicationorg[1]);// ���빫˾����
			// ��Ŀ
			Object[] projectMsg = util.getProject_name((String) parent
					.getAttributeValue("pk_project"));
			purchase.put("project_name", projectMsg[1]);// ��Ŀ����
			// �տ
			Object[] payeeMsg = util.getCustomerMsg((String) parent
					.getAttributeValue("pk_payee"));
			purchase.put("payee_code", payeeMsg[0]);// �տ����
			purchase.put("payee_name", payeeMsg[1]);// �տ����
			// ���
			Object[] payerMsg = util.getCustomerMsg((String) parent
					.getAttributeValue("pk_payer"));
			purchase.put("payer_code", payerMsg[0]);// �������
			purchase.put("payer_name", payerMsg[1]);// �������
			// ���Ż��
			String accountant_name = util.getPerson_name((String) parent
					.getAttributeValue("pk_accountant"));
			purchase.put("pk_accountant", accountant_name);// ���Ż������
			// ����
			String cashier_name = util.getPerson_name((String) parent
					.getAttributeValue("pk_cashier"));
			purchase.put("pk_cashier", cashier_name);// ��������
			// ������Ϣ
			purchase.put("message",
					(String) parent.getAttributeValue("message"));
			// �ÿ�����
			purchase.put("usecontent",
					(String) parent.getAttributeValue("usecontent"));
			// ҵ����Ϣ
			purchase.put("businessmsg",
					(String) parent.getAttributeValue("businessmsg"));
			// ��������
			purchase.put("applicationdate",
					(String) parent.getAttributeValue("applicationdate"));
			// ��ͬ���
			purchase.put("contractmoney",
					(String) parent.getAttributeValue("contractmoney"));
			// �ۼƸ�����
			purchase.put("paymentamount",
					(String) parent.getAttributeValue("paymentamount"));
			// �����
			purchase.put("applyamount",
					(String) parent.getAttributeValue("applyamount"));
			// ����
			purchase.put("title", (String) parent.getAttributeValue("title"));
			// String purchaseDetail =
			// objectMapper.writeValueAsString(purchaseDetaillist);//
			// ת������json�ַ�����ʽ

			Map<String, Object> formData = new HashMap<String, Object>();// ������
			formData.put("tgrz_financexpense", purchase);

			Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
			postdata.put("ProcessName", "");
			postdata.put("Action", "�ύ");
			postdata.put("Comment", "ǩ�����");
			postdata.put("Draft", "false");
			postdata.put("FormData", formData);
			// postdata.put("DeptID", deptid);
			// postdata.put("AttachmentInfo", getFiles(parent.getPk_jkbx()));

			String json = objectMapper.writeValueAsString(postdata);

			Logger.error("----------����ʼ---------------");
			Logger.error("----------�����������" + json + "��---------------");

			// ����url��Դ
			URL url = new URL(urls);
			// ����http����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// �����������
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// ���ò��û���
			conn.setUseCaches(false);
			// ���ô��ݷ�ʽ
			conn.setRequestMethod("POST");
			// ����ά�ֳ�����
			conn.setRequestProperty("Connection", "Keep-Alive");
			// �����ļ��ַ���:
			conn.setRequestProperty("Charset", "utf-8");
			// ת��Ϊ�ֽ�����
			byte[] data = json.getBytes("utf-8");
			// �����ļ�����
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));

			// �����ļ�����:
			conn.setRequestProperty("Content-Type", "application/json");

			// ��ʼ��������
			conn.connect();
			OutputStream out = conn.getOutputStream();
			// д��������ַ���
			out.write(data);
			out.flush();
			out.close();
			String a = "";
			// ���󷵻ص�״̬
			if (conn.getResponseCode() == 200) {
				Logger.error("���ӳɹ�");
				// ���󷵻ص�����
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "UTF-8"));
				String line = null;
				while ((line = in.readLine()) != null) {
					a += line;
				}
				JSONObject result = new JSONObject(a);
				String flag = (String) result.getString("success");
				String taskid = null;
				String errmsg = null;
				String openUrl = null;
				if (flag.equals("true")) {
					taskid = (String) result.getString("TaskID");
					openUrl = (String) result.getString("OpenUrl");
					Desktop.getDesktop().browse(new URI(openUrl));
				} else {
					errmsg = (String) result.getString("errorMessage");
					throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
				}
			} else {
				Logger.error("����ʧ��");
				throw new BusinessException("����ʧ��");
			}

		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new BusinessException("��" + e.getMessage() + "��");
		}
	}
}
