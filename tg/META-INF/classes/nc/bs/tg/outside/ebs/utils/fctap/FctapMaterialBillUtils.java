package nc.bs.tg.outside.ebs.utils.fctap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApBVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.gateway60.accountbook.GlOrgUtils;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FctapMaterialBillUtils extends EBSBillUtils {
	static FctapMaterialBillUtils utils;

	public static FctapMaterialBillUtils getUtils() {
		if (utils == null) {
			utils = new FctapMaterialBillUtils();
		}
		return utils;
	}

	/**
	 * 付款合同-材料合同保存
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		// 表头数据
		JSONObject headJSON = (JSONObject) value.get("headInfo");

		String vbillcode = headJSON.getString("vbillcode");// ebs协议编号
		String pk = headJSON.getString("def57");// 协议id

		// 目标单据名+请求协议编号作队列
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":"
				+ vbillcode;
		// 目标单据名+请求单据pk作日志输出
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + pk;

		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
		AggCtApVO aggVO = null;
		// 返回报文
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			// 检查NC是否有相应的单据存在
			aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
					"isnull(dr,0)=0  and def57 = '" + pk + "'");// and blatest
																// ='Y'

			String hpk = null;
			if (aggVO != null) {
				hpk = aggVO.getParentVO().getPrimaryKey();
			}
			// 数据转换工具对象
			FctapMaterialConvertor fctapConvertor = new FctapMaterialConvertor();
			// 配置表头key与名称映射
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("fct_ap", "付款合同");
			fctapConvertor.setHVOKeyName(hVOKeyName);

			// 配置表体key与名称映射
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("ExecutionBVO", "执行情况");
			fctapConvertor.setBVOKeyName(bVOKeyName);

			// 配置表头检验字段
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hKeyName = new HashMap<String, String>();
			hKeyName.put("pk_org", "财务组织");
			hKeyName.put("vbillcode", "协议编号"); // NC合同编码
			hKeyName.put("ctname", "协议名称");// NC合同名称
			// hKeyName.put("vdef21", "主协议编号");
			hKeyName.put("vdef22", "供应商后台id");
			hKeyName.put("cvendorid", "供应商编码");
			// hKeyName.put("vdef23", "供应商名称");
			hKeyName.put("def68", "协议税率");
			hKeyName.put("def71", "采购员");
			hKeyName.put("def72", "进度款比例");
			hKeyName.put("def73", "结算款比例");
			hKeyName.put("def74", "质保金比例");
			hValidatedKeyName.put("fct_ap", hKeyName);
			fctapConvertor.sethValidatedKeyName(hValidatedKeyName);

			// 配置表体检验字段
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			// **********执行情况页签字段检验
			Map<String, String> bExeContractrBVOKeyName = new HashMap<String, String>();
			bExeContractrBVOKeyName.put("def11", "请款单id");
			bExeContractrBVOKeyName.put("billno", "请款单号");
			bValidatedKeyName.put("ExecutionBVO", bExeContractrBVOKeyName);
			// **********执行情况页签字段检验
			fctapConvertor.setbValidatedKeyName(bValidatedKeyName);

			// 配置参照字段
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("fct_ap-pk_org"); // 财务组织
			refKeys.add("fct_ap-personnelid"); // 经办人
			refKeys.add("fct_ap-cvendorid"); // 供应商名称 参照供应商

			fctapConvertor.setRefKeys(refKeys);

			// 默认集团-时代集团
			fctapConvertor.setDefaultGroup("000112100000000005FD");

			AggCtApVO billvo = (AggCtApVO) fctapConvertor.castToBill(value,
					AggCtApVO.class, aggVO);

			// 设置合同单据默认信息
			// 表头默认信息配置
			billvo.getParentVO().setCtrantypeid(
					fctapConvertor.getRefAttributePk("ctrantypeid",
							"FCT1-Cxx-002")); // 交易类型
			billvo.getParentVO().setCbilltypecode("FCT1"); // 单据类型
			billvo.getParentVO().setValdate(new UFDate()); // 计划生效日期
			billvo.getParentVO().setInvallidate(new UFDate());// 计划终止日期
			billvo.getParentVO().setNexchangerate(new UFDouble(100));// 折本汇率
			billvo.getParentVO().setNtotalorigmny(new UFDouble(0));// 原币价税合计
			billvo.getParentVO().setSubscribedate(new UFDate());// 盖章签字日期
			// billvo.getParentVO().setCvendorid(billvo.getParentVO().getSecond());//
			// 供应商

			// ********************表头参照信息设置***************
			billvo.getParentVO().setPk_org(
					fctapConvertor.getRefAttributePk("fct_ap-pk_org", billvo
							.getParentVO().getPk_org())); // 财务组织
			billvo.getParentVO().setPk_org_v(
					GlOrgUtils.getPkorgVersionByOrgID(billvo.getParentVO()
							.getPk_org()));
			billvo.getParentVO().setCvendorid(
					fctapConvertor.getRefAttributePk("fct_ap-cvendorid", billvo
							.getParentVO().getCvendorid(), billvo.getParentVO()
							.getPk_org(), billvo.getParentVO().getPk_org())); // 供应商名称
																				// 参照供应商
			// ********************表头参照信息设置***************

			// 表体默认信息配置
			JSONObject jsonObject = (JSONObject) value.get("itemInfo");
			JSONArray executionBVOs = (JSONArray) jsonObject
					.get("ExeccontractrBVO");// 执行情况页签
			ExecutionBVO[] exeBVOs = new ExecutionBVO[executionBVOs.size()];
			for (int i = 0; i < executionBVOs.size(); i++) {
				JSONObject json = (JSONObject) executionBVOs.get(i);

				boolean check = selectno(json.getString("billno"));
				ExecutionBVO eVO = new ExecutionBVO();
				eVO.setPk_fct_ap(hpk);
				eVO.setBillno(json.getString("billno"));
				eVO.setDef11(json.getString("def11"));
				if (check) {
					eVO.setStatus(VOStatus.UPDATED);
				} else {
					eVO.setStatus(VOStatus.NEW);
				}

				exeBVOs[i] = eVO;
			}
			billvo.setChildrenVO(exeBVOs);

			CtApBVO ctApBVO = new CtApBVO();// 成本拆分页签（合同基本）
			int ctApBVORowNo = 10;
			ctApBVO.setCrowno(String.valueOf(ctApBVORowNo));
			ctApBVO.setPk_fct_ap(hpk);
			ctApBVO.setPk_org(billvo.getParentVO().getPk_org());
			ctApBVO.setPk_org_v(billvo.getParentVO().getPk_org());
			ctApBVO.setPk_group(billvo.getParentVO().getPk_group());
			ctApBVO.setFtaxtypeflag(1); // 扣税类别
			ctApBVO.setNtaxmny(UFDouble.ONE_DBL);
			ctApBVO.setNorigtaxmny(new UFDouble(0)); // 原币价税合计
			billvo.setChildrenVO(new CtApBVO[] { ctApBVO });

			CtApPlanVO ctApPlanVO = new CtApPlanVO(); // 付款计划
			// ctApPlanVO.setPlanmoney(new UFDouble(0)); // 计划金额
			if (aggVO == null) {
				ctApPlanVO.setPlanrate(new UFDouble(100));// 计划比例
			} else {
				ctApPlanVO.setPlanrate(UFDouble.ZERO_DBL);// 计划比例
			}
			ctApPlanVO.setPk_fct_ap(hpk);
			ctApPlanVO.setPk_org(billvo.getParentVO().getPk_org());
			ctApPlanVO.setPk_group(billvo.getParentVO().getPk_group());
			billvo.setChildrenVO(new CtApPlanVO[] { ctApPlanVO });
			// billvo.getParentVO().setAttributeValue("def40", "asdfasd");

			HashMap<String, Object> eParam = new HashMap<String, Object>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			AggCtApVO billVO = null;

			if (aggVO == null) {
				// 不存在，新增单据
				billvo.getParentVO().setCreator(getSaleUserID());
				billvo.getParentVO().setBillmaker(getSaleUserID());
				billvo.getParentVO().setCreationtime(new UFDateTime());
				billvo.getParentVO().setDmakedate(new UFDate());
				billvo.getParentVO().setFstatusflag(0);
				billvo.getParentVO().setVersion(UFDouble.ONE_DBL);

				billVO = ((AggCtApVO[]) getPfBusiAction().processAction(
						"SAVEBASE", "FCT1", null, billvo, null, eParam))[0];
				// 返回表头单据号和单据PK报文
				dataMap.put("billid", billVO.getParentVO().getPrimaryKey());
				dataMap.put("vbillcode", billVO.getParentVO().getVbillcode());
			} else {
				hpk = aggVO.getParentVO().getPrimaryKey();

				String vbcode = "";
				String ctname = "";
				String vdef21 = "";
				String vdef22 = "";
				String cvendorid = "";
				String vdef23 = "";
				String def65 = null;
				String def66 = null;
				String def67 = null;
				String def68 = null;
				String def69 = null;
				String def70 = null;
				String def71 = null;
				String def72 = null;
				String def73 = null;
				String def74 = null;
				String def75 = null;

				vbcode = billvo.getParentVO().getVbillcode();
				ctname = billvo.getParentVO().getCtname();
				if (billvo.getParentVO().getVdef21() != null) {
					vdef21 = billvo.getParentVO().getVdef21();
				}
				vdef22 = billvo.getParentVO().getVdef22();
				cvendorid = billvo.getParentVO().getCvendorid();
				if (billvo.getParentVO().getVdef23() != null) {
					vdef23 = billvo.getParentVO().getVdef23();
				}
				if (billvo.getParentVO().getDef65() != null) {
					def65 = billvo.getParentVO().getDef65();
				}
				if (billvo.getParentVO().getDef66() != null) {
					def66 = billvo.getParentVO().getDef66();
				}
				if (billvo.getParentVO().getDef67() != null) {
					def67 = billvo.getParentVO().getDef67();
				}
				if (billvo.getParentVO().getDef68() != null) {
					def68 = billvo.getParentVO().getDef68();
				}
				if (billvo.getParentVO().getDef69() != null) {
					def69 = billvo.getParentVO().getDef69();
				}
				if (billvo.getParentVO().getDef70() != null) {
					def70 = billvo.getParentVO().getDef70();
				}
				if (billvo.getParentVO().getDef71() != null) {
					def71 = billvo.getParentVO().getDef71();
				}
				if (billvo.getParentVO().getDef72() != null) {
					def72 = billvo.getParentVO().getDef72();
				}
				if (billvo.getParentVO().getDef73() != null) {
					def73 = billvo.getParentVO().getDef73();
				}
				if (billvo.getParentVO().getDef74() != null) {
					def74 = billvo.getParentVO().getDef74();
				}
				if (billvo.getParentVO().getDef75() != null) {
					def75 = billvo.getParentVO().getDef75();
				}

				String sql = "update fct_ap set vbillcode = '" + vbcode
						+ "',ctname = '" + ctname + "',vdef21 = '" + vdef21
						+ "',vdef22 = '" + vdef22 + "',cvendorid = '"
						+ cvendorid + "',vdef23 = '" + vdef23 + "',def65 = '"
						+ def65 + "',def66 = '" + def66 + "'" + ",def67 = '"
						+ def67 + "',def68 = '" + def68 + "',def69 = '" + def69
						+ "'" + ",def70 = '" + def70 + "' ,def71 = '" + def71
						+ "'" + ",def72 = '" + def72 + "',def73 = '" + def73
						+ "'" + ",def74 = '" + def74 + "' ,def75 = '" + def75
						+ "'" + " where pk_fct_ap = '" + hpk
						+ "' and nvl(dr,0)=0";
				BaseDAO dao = getBaseDAO();
				dao.executeUpdate(sql);
				String dsql = "delete from fct_execution_b where pk_fct_ap = '"
						+ hpk + "' and nvl(dr,0)=0";
				dao.executeUpdate(dsql);
				for (Object bvos : executionBVOs) {
					ExecutionBVO bvo = new ExecutionBVO();
					JSONObject json = (JSONObject) bvos;
					bvo.setDef11(json.getString("def11"));
					bvo.setBillno(json.getString("billno"));
					bvo.setPk_fct_ap(hpk);
					dao.insertVO(bvo);
				}
				// 返回表头单据号和单据PK报文
				dataMap.put("billid", hpk);
				dataMap.put("vbillcode", billvo.getParentVO().getVbillcode());
			}
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(dataMap);
	}

	/**
	 * 对表体的唯一性进行校验
	 * 
	 * @param billno
	 * @return
	 */
	private Boolean selectno(String billno) {
		boolean check = false;

		int result = 0;

		String sql = "select count(1) from fct_execution_b where nvl(dr,0)=0 billno = "
				+ billno;

		try {
			result = (int) getBaseDAO()
					.executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (result > 0) {
			check = true;
		}

		return check;
	}
}
