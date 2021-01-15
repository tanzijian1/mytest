package nc.ui.tg.tgrz_mortgageagreement.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.tg.util.SendBPMUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.vo.tg.tgrz_mortgageagreement.MortgageAgreementVO;

public class ApproveToBPMAction extends nc.ui.pubapp.uif2app.actions.pflow.CommitScriptAction{

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		AggMortgageAgreementVO originBill = (AggMortgageAgreementVO) getModel().getSelectedData();
		AggMortgageAgreementVO clientFullVO = (AggMortgageAgreementVO) originBill.clone();
		pushBillToBpm("",clientFullVO);
		super.doAction(e);
	}

	/**
	 * 调用bpm传单接口
	 * 
	 * @author tjl 20190604
	 * @param aggvo
	 * @param url
	 *            请求地址
	 * @throws BusinessException
	 */
	public void pushBillToBpm(String urls, AggMortgageAgreementVO aggvo)
			throws BusinessException {
		// TODO add by tjl 2019-06-04
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			MortgageAgreementVO parent = aggvo.getParentVO();
			Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
			SendBPMUtils util = new SendBPMUtils();
			// 财务组织
			Object[] orgmsg = util.getOrgmsg((String) parent
					.getAttributeValue("pk_org"));
			purchase.put("org_code", orgmsg[0]);// 组织编码
			purchase.put("org_name", orgmsg[1]);// 组织名字
			// 申请部门
			Object[] deptmsg = util.getDeptmsg((String) parent
					.getAttributeValue("applicationdept"));
			purchase.put("applicationdept_code", deptmsg[0]);// 申请部门编码
			purchase.put("applicationdept_name", deptmsg[1]);// 申请部门名称
			// 申请人
			String applicant = util.getPerson_name((String) parent
					.getAttributeValue("proposer"));
			purchase.put("applicant_name", applicant);// 申请人名称
			// 流程类别
			Object[] flowMsg = util.getFlowMsg((String) parent
					.getAttributeValue("process"));
			purchase.put("flow_code", flowMsg[0]);// 流程类别名编码
			purchase.put("flow_name", flowMsg[1]);// 流程类别名称
			// 申请公司
			Object[] applicationorg = util.getOrgmsg((String) parent
					.getAttributeValue("applicationorg"));
			purchase.put("applicationorg_name", applicationorg[1]);// 申请公司名称
			// 项目
			Object[] projectMsg = util.getProject_name((String) parent
					.getAttributeValue("proname"));
			purchase.put("project_name", projectMsg[1]);// 项目名称
			// 收款方
			Object[] payeeMsg = util.getCustomerMsg((String) parent
					.getAttributeValue("pk_payee"));
			purchase.put("payee_code", payeeMsg[0]);// 收款方编码
			purchase.put("payee_name", payeeMsg[1]);// 收款方名称
			// 付款方
			Object[] payerMsg = util.getCustomerMsg((String) parent
					.getAttributeValue("pk_payer"));
			purchase.put("payer_code", payerMsg[0]);// 付款方编码
			purchase.put("payer_name", payerMsg[1]);// 付款方名称
			// 集团会计
			String accountant_name = util.getPerson_name((String) parent
					.getGroupaccounting());
			purchase.put("pk_accountant", accountant_name==null?"":accountant_name);// 集团会计名称
			// 出纳
			String cashier_name = util.getPerson_name(parent
					.getCashier());
			purchase.put("pk_cashier", cashier_name==null?"":cashier_name);// 出纳名称
			// 用款内容
			purchase.put("usecontent",
					parent.getMoneycontent()==null?"":parent.getMoneycontent());
			// 业务信息
			purchase.put("businessmsg",
					parent.getBusiinformation()==null?"":parent.getBusiinformation());
			// 申请日期
			purchase.put("applicationdate",parent.getApplicationdate()==null?"":
					parent.getApplicationdate().toString());
			// 合同总金额
			purchase.put("contractmoney",parent.getN_amount()==null?UFDouble.ZERO_DBL.setScale(2, 2).toString():
					parent.getN_amount().setScale(2, 2).toString());
			// 累计付款金额
			purchase.put("paymentamount",parent.getI_totalpayamount()==null?UFDouble.ZERO_DBL.setScale(2, 2).toString():
					parent.getI_totalpayamount().setScale(2, 2).toString());
			// 请款金额
			purchase.put("applyamount",parent.getI_applyamount()==null?UFDouble.ZERO_DBL.setScale(2, 2).toString():
					parent.getI_applyamount().setScale(2, 2).toString());
			// 标题
			purchase.put("title", parent.getTitle()==null?"":parent.getTitle());
			// String purchaseDetail =
			// objectMapper.writeValueAsString(purchaseDetaillist);//
			// 转成数组json字符串格式

			Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
			formData.put("tgrz_mortgageagreement", purchase);

			Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
			postdata.put("ProcessName", "");
			postdata.put("Action", "提交");
			postdata.put("Comment", "签核意见");
			postdata.put("Draft", "false");
			postdata.put("FormData", formData);
			// postdata.put("DeptID", deptid);
			//上传附件
			 postdata.put("AttachmentInfo", util.getFiles(parent.getPk_moragreement(),parent));

			String json = objectMapper.writeValueAsString(postdata);

			Logger.error("----------请求开始---------------");
			Logger.error("----------请求参数：【" + json + "】---------------");

			// 创建url资源
			URL url = new URL(urls);
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置允许输出
			conn.setDoOutput(true);

			conn.setDoInput(true);
			// 设置不用缓存
			conn.setUseCaches(false);
			// 设置传递方式
			conn.setRequestMethod("POST");
			// 设置维持长连接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置文件字符集:
			conn.setRequestProperty("Charset", "utf-8");
			// 转换为字节数组
			byte[] data = json.getBytes("utf-8");
			// 设置文件长度
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));

			// 设置文件类型:
			conn.setRequestProperty("Content-Type", "application/json");

			// 开始连接请求
			conn.connect();
			OutputStream out = conn.getOutputStream();
			// 写入请求的字符串
			out.write(data);
			out.flush();
			out.close();
			String a = "";
			// 请求返回的状态
			if (conn.getResponseCode() == 200) {
				Logger.error("连接成功");
				// 请求返回的数据
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
					throw new BusinessException("【来自BPM的错误信息：" + errmsg + "】");
				}
			} else {
				Logger.error("连接失败");
				throw new BusinessException("连接失败");
			}

		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new BusinessException("【" + e.getMessage() + "】");
		}
	}

	
}
