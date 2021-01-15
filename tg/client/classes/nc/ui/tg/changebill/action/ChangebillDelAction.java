package nc.ui.tg.changebill.action;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.tg.util.PropertiesUtil;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.tgfn.changebill.ChangeBillHVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ChangebillDelAction extends
		nc.ui.pubapp.uif2app.actions.pflow.DeleteScriptAction {
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		Object obj = getModel().getSelectedData();
		if (obj == null) {
			throw new BusinessException("请选中一条数据");
		}
		
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		AggChangeBillHVO disvo = (AggChangeBillHVO) obj;

		String creatorsql = "select user_name from sm_user where nvl(dr,0)=0 and cuserid = '"+disvo.getParentVO().getCreator()+"'";
		
		String creator = (String) query.executeQuery(creatorsql, new ColumnProcessor());

		if (UIDialog.ID_YES == showConfirm(this.getModel().getContext()
				.getEntranceUI(), creator)) {
			String url = PropertiesUtil.getInstance("ebs_url.properties")
					.readValue("SALEURL");// 销售回写
			AggChangeBillHVO billvo = (AggChangeBillHVO) obj;
			String cuserid = (String) query.executeQuery(
					"select cuserid  from sm_user where user_name='SALE'",
					new ColumnProcessor());
			ChangeBillHVO hvo = (ChangeBillHVO) billvo.getParentVO();
			if (!(hvo.getBillmaker().equals(cuserid))) {
				super.doSuperAction(e);
			} else {
				String sql = "select to_char(g.num) as ncdocnum from tgfn_changeBill_h t inner join fip_relation f  on f.src_relationid=t.pk_changebill_h inner join  gl_voucher  g on   g.pk_voucher =f.des_relationid where  pk_changebill_h='"
						+ hvo.getPk_changebill_h() + "'";
				String num = (String) query.executeQuery(sql,
						new ColumnProcessor());// 凭证号
				String isnum = num == null ? "0" : "1";
				String isshr = hvo.getApprovestatus() == 1 ? "1" : "0";
				String tradetype = "0";
				HashMap<String, Object> mapdata = new HashMap<String, Object>();
				mapdata.put("vouchid", hvo.getDef1());// 销售系统单据ID
				mapdata.put("generateCredentials", isnum);// 是否生成凭证
				mapdata.put("ncDocumentNo", hvo.getBillno());// NC票据号
				mapdata.put("ncDocumentnumber", num);// 凭证号
				mapdata.put("shr", isshr);// 审核是否成功
				mapdata.put("isDj", tradetype);// 是否为转备案款单据（1为是）
				String data = JSON.toJSONString(mapdata);
				String returndata = connectionjson(url, data);
				JSONObject jobj = JSON.parseObject(returndata);
				if (!(jobj.getBoolean("success"))) {
					throw new BusinessException("EBS exception "
							+ jobj.get("message") + "");
				}
				super.doSuperAction(e);
			}
		}
	}

	/*
	 * 传json
	 */
	public String connectionjson(String address, String data)
			throws BusinessException {
		StringBuffer sb = new StringBuffer();
		String responsecode = null;
		try {
			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/json; charset=UTF-8");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.connect();
			if (data != null) {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(connection.getOutputStream(),
								"utf-8"));
				writer.write(data);
				writer.flush();
			}
			String temp = "";
			responsecode = String.valueOf(connection.getResponseCode());
			if (200 == connection.getResponseCode()) {
				BufferedReader br = new BufferedReader(new BufferedReader(
						new InputStreamReader(connection.getInputStream(),
								"utf-8")));
				while ((temp = br.readLine()) != null) {
					sb.append(temp);
				}
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			throw new BusinessException("连接异常 " + responsecode + "    "
					+ e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * add by huangxj 2020年3月25日 用于修改删除弹框
	 * 
	 * @param parent
	 * @param transtype
	 * @return
	 */
	public static int showConfirm(Container parent, String creator) {
		String TITLE = NCLangRes.getInstance().getStrByID("uif2",
				"CommonConfirmDialogUtils-000004");

		String QUESTION = null;

		if ("SALE".equals(creator)) {
			QUESTION = "本单要退回到销售系统，请确认";
		} else {
			QUESTION = NCLangRes.getInstance().getStrByID("uif2",
					"CommonConfirmDialogUtils-000007");
		}
		return MessageDialog.showYesNoDlg(parent, TITLE, QUESTION, 8);
	}

}
