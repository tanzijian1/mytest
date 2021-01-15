package nc.bs.tg.outside.sale.utils.salessystem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.pub.doc.DefdocUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.bs.tg.outside.tobankdoc.InsertBankdoc;
import nc.itf.bd.bankdoc.IBankdocService;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.tg.outside.EBSBankdocVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import net.sf.json.JSONArray;

import com.alibaba.fastjson.JSON;

public class BankArchivesUtils extends SaleBillUtils {

	static BankArchivesUtils utils;

	public static BankArchivesUtils getUtils() {
		if (utils == null) {
			utils = new BankArchivesUtils();
		}
		return utils;
	}

	/**
	 * 银行档案保存类
	 * 
	 * @param value
	 * @param dectype
	 * @param srcsystem
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srcsystem) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		Map<String, String> refMap = new HashMap<String, String>();

		IBankdocService bankdocService = NCLocator.getInstance().lookup(
				IBankdocService.class);

		BankdocVO headVO = new BankdocVO();
		// 来源系统
		headVO.setDef1(srcsystem);
		headVO.setPk_country("0001Z010000000079UJJ");// 国家（默认中国）
		headVO.setPk_group("000112100000000005FD");
		headVO.setPk_org("GLOBLE00000000000000");// 所属组织
		headVO.setEnablestate(2);// 启用状态（默认已启用）
		//开户行
		if (value.get("name")!=null) {
			headVO.setName((String) value.get("name"));
		}else{
			throw new BusinessException("开户行名称不能为空，请检查参数设置");
		}
		
		headVO.setPk_banktype(getBnakTypeID((String) value.get("name"))); // 银行类别
		headVO.setShortname((String) value.get("name"));//简称
		
		

		String billqueue = EBSCont.getDocNameMap().get(dectype) + ":"
				+ value.get("name");
		String billkey = EBSCont.getDocNameMap().get(dectype) + ":"
				+ value.get("name");

		try {
			EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
			Collection<BankdocVO> docVO = getBaseDAO().retrieveByClause(
					BankdocVO.class,
					"isnull(dr,0)=0 and name = '" + value.get("name") + "'");
			if (docVO != null && docVO.size() > 0) {
				throw new BusinessException("该银行档案已存在!");
			} else {
				IBillcodeManage codeManage = NCLocator.getInstance().lookup(
						IBillcodeManage.class);
				String creatcode = codeManage.getBillCode_RequiresNew(
						"bankdoc", "000112100000000005FD",
						"GLOBLE00000000000000", headVO);
				headVO.setCode(creatcode);
				bankdocService.insertBankdocVO(headVO, false);
				refMap.put("msg", "【银行档案】," + "操作完成!");
			}
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(refMap);

	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 */
	private AggPayrequest onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {
		AggPayrequest aggvo = new AggPayrequest();
		JSON headjson = (JSON) value.get("headInfo");
		JSON bodyjson = (JSON) value.get("itemInfo");

		return aggvo;
	}
	
	private String getBnakTypeID(String name) throws BusinessException {
		Map<String, String> infoMap = getBankTypeMap();
		String pk_banktype = null;
		if (infoMap.size() > 0) {
			for (String key : infoMap.keySet()) {
				if ("中国银行".equals(name.substring(0, 4))) {
					pk_banktype = infoMap.get("中国银行");
					break;
				}
				if (name.contains(key) && !"中国银行".equals(key)) {
					pk_banktype = infoMap.get(key);
					break;
				}
			}
		}

		if (pk_banktype == null) {
			throw new BusinessException(name + "无法找到相应的银行类别，请检查参数设置");
		}

		return pk_banktype;
	}

	private Map<String, String> getBankTypeMap() throws DAOException {
		String sql = "select bd_banktype.pk_banktype,bd_banktype.name from bd_banktype where dr =0 ";
		List<Map<String, String>> list = (List<Map<String, String>>) getBaseDAO()
				.executeQuery(sql, new MapListProcessor());
		Map<String, String> infoMap = new HashMap<String, String>();
		if (list != null && list.size() > 0) {
			for (Map<String, String> map : list) {
				infoMap.put(map.get("name"), map.get("pk_banktype"));

			}
		}
		return infoMap;

	}
}
