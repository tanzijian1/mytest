package nc.bs.tg.outside.matterapp.imag.util;

import java.util.HashMap;
import java.util.List;

import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.ITGSyncService;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ImagMtappBillImageStatusUtils extends BillUtils implements
		ITGSyncService {

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		JSONObject jsonhead = (JSONObject) info.get("headinfo");// ��ϵͳ��Դ��ͷ����
		String barcode = jsonhead.getString("barcode");// Ӱ��ϵͳҵ�񵥾ݺ�
		String imagestatus = jsonhead.getString("imagestatus");// Ӱ��ϵͳ״̬
		String billtype = jsonhead.getString("billtype");// Ӱ��ϵͳ״̬
		// Ӱ��ϵͳҵ�񵥾ݵ��ݺ������п��ƺ���־���
		try {
			String sql = null;
			if (billtype.startsWith("261X")) {
				sql = "select pk_mtapp_bill from er_mtapp_bill where nvl(dr,0)=0 and defitem3='"
						+ barcode + "'";
			} else if (billtype.startsWith("264X")) {
				sql = "select pk_jkbx from er_bxzb where nvl(dr,0)=0 and zyx16='"
						+ barcode + "'";
			}
			List<String> pks = (List<String>) getBaseDAO().executeQuery(sql,
					new ColumnListProcessor());
			if (pks == null || pks.isEmpty()) {
				throw new BusinessException("Ӱ�����δ����NC����!");
			}
			for (String str : pks) {
				if (billtype.startsWith("261X")) {
					sql = "update er_mtapp_bill set defitem4='" + imagestatus
							+ "' where pk_mtapp_bill='" + str + "'";
				} else if (billtype.startsWith("264X")) {
					sql = "update er_bxzb set zyx17='" + imagestatus
							+ "' where pk_jkbx='" + str + "'";
				}
				getBaseDAO().executeUpdate(sql);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("barcode", barcode);
		map.put("msg", "��"+billtype+"������Ӱ��״̬�ɹ�");
		return JSON.toJSONString(map);
	}

}
