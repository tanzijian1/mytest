package nc.impl.pub.ace;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.itf.tools.HttpBusinessUtils;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.tg.alter.plugin.ebs.Httpconnectionutil;
import nc.bs.tg.outside.ebs.utils.outputinvoice.MoreToolsUtil;
import nc.itf.fip.generate.IGenerateService;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.image.IGuoXinImage;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaveLogService;
import nc.itf.tg.outside.OutsideUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceService;
import nc.pubitf.accperiod.AccountCalendar;
import nc.pubitf.erm.accruedexpense.IErmAccruedBillManage;
import nc.vo.arap.agiotage.ArapBusiDataVO;
import nc.vo.arap.verify.AggverifyVO;
import nc.vo.arap.verify.SameMnyVerify;
import nc.vo.arap.verify.VerifyDetailVO;
import nc.vo.arap.verifynew.ProxyVerify;
import nc.vo.arap.verifynew.Saver;
import nc.vo.arap.verifynew.VerifyFilter;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.ep.bx.JKHeaderVO;
import nc.vo.erm.accruedexpense.AggAccruedBillVO;
import nc.vo.fip.operatinglogs.OperatingLogVO;
import nc.vo.hzvat.outputtax.AggOutputTaxHVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;
import nc.vo.tg.outside.BusinessBillLogVO;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tg.outside.bpm.NcToBpmVO;
import nc.vo.verifynew.pub.DefaultVerifyRuleVO;
import nc.vo.verifynew.pub.VerifyCom;
import nc.vo.wfengine.core.activity.Activity;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.pub.WfTaskType;
import nc.vo.yer.AddBillHVO;
import nc.vo.yer.AggAddBillHVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class IsqlThreadimpl implements ISqlThread {

	@Override
	public AggAccruedBillVO ytinsert_RequiresNew(AggAccruedBillVO vo)
			throws BusinessException {
		// TODO 自动生成的方法存根
		IErmAccruedBillManage billManagerSer = NCLocator.getInstance().lookup(
				IErmAccruedBillManage.class);
		return billManagerSer.insertVO(vo);
	}

	@Override
	public AggAccruedBillVO ytupdate_RequiresNew(AggAccruedBillVO vo)
			throws BusinessException {
		// TODO 自动生成的方法存根
		IErmAccruedBillManage billManagerSer = NCLocator.getInstance().lookup(
				IErmAccruedBillManage.class);
		return billManagerSer.updateVO(vo);
	}

	@Override
	public AggAccruedBillVO ytunapprove_RequiresNew(AggAccruedBillVO vo)
			throws BusinessException {
		// TODO 自动生成的方法存根
		// PfUtilClient.runAction(null, IPFActionName.APPROVE, "262X", vo, null,
		// null, null, null);
		IPFBusiAction pfaction = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		Object returnobj = pfaction.processAction(IPFActionName.UNAPPROVE,
				"262X", null, vo, null, null);
		if (((Object[]) returnobj)[0] instanceof nc.vo.erm.common.MessageVO) {
			return (AggAccruedBillVO) ((nc.vo.erm.common.MessageVO) ((Object[]) returnobj)[0])
					.getSuccessVO();
		}
		return null;
	}

	@Override
	public void billInsert_RequiresNew(SuperVO vo) throws BusinessException {
		// TODO 自动生成的方法存根
		BaseDAO dao = new BaseDAO();
		dao.insertVO(vo);
	}

	@Override
	public void billupdate_RequiresNew(SuperVO vo) throws BusinessException {
		// TODO 自动生成的方法存根
		BaseDAO dao = new BaseDAO();
		dao.updateVO(vo);
	}

	@Override
	public void pushImage(AggregatedValueObject billvo)
			throws BusinessException {
		pushImage(billvo, false);
	}

	private BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void setDao(BaseDAO dao) {
		this.dao = dao;
	}

	public boolean checkimage(String pk_org) throws DAOException {
		// 业财模式 取值为Y/N
		String def11 = (String) getDao().executeQuery(
				"select def11  from org_orgs where pk_org='" + pk_org + "'",
				new ColumnProcessor());
		if ("简化业财模式".equals(def11))
			return false;
		return true;
	}

	/**
	 * 得到收款人
	 * 
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public String getreceiver1(String pk) throws DAOException {
		String Receivername = (String) getDao().executeQuery(
				"select name  from bd_psndoc where   pk_psndoc='" + pk + "'",
				new ColumnProcessor());
		return Receivername;
	}

	/**
	 * 得到银行
	 * 
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getbankaccount1(String pk) throws DAOException {
		Map<String, String> map = null;
		map = (Map<String, String>) getDao().executeQuery(
				"select accnum,pk_bankaccbas from bd_bankaccsub where pk_bankaccsub ='"
						+ pk + "'", new MapProcessor());
		return map;
	}

	/**
	 * 得到供应商名称
	 * 
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public String getsupplier1(String pk) throws DAOException {

		String name = null;
		name = (String) getDao().executeQuery(
				"select name from bd_supplier where  pk_supplier='" + pk + "'",
				new ColumnProcessor());
		return name;
	}

	/**
	 * 得到开户银行
	 * 
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public String getopenBank1(String pk) throws DAOException {
		String name = null;
		name = (String) getDao()
				.executeQuery(
						"select name from bd_banktype where pk_banktype=(select pk_banktype  from bd_bankaccbas where pk_bankaccbas ='"
								+ pk + "')", new ColumnProcessor());
		return name;
	}

	/**
	 * 得到收款人
	 * 
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public String getreceiver(String pk) throws DAOException {
		String Receivername = (String) getDao().executeQuery(
				"select name  from bd_psndoc where   pk_psndoc='" + pk + "'",
				new ColumnProcessor());
		return Receivername;
	}

	/**
	 * 得到银行
	 * 
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public String getbankaccount(String pk) throws DAOException {
		String banknum = null;
		banknum = (String) getDao().executeQuery(
				"select accnum from bd_bankaccsub where pk_bankaccsub ='" + pk
						+ "'", new ColumnProcessor());
		return banknum;
	}

	/**
	 * 得到供应商名称
	 * 
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public String getsupplier(String pk) throws DAOException {

		String name = null;
		name = (String) getDao().executeQuery(
				"select name from bd_supplier where  pk_supplier='" + pk + "'",
				new ColumnProcessor());
		return name;
	}

	@Override
	public void update__RequiresNew(SuperVO vo, String[] fileds)
			throws BusinessException {
		// TODO 自动生成的方法存根
		IMDPersistenceService pservice = NCLocator.getInstance().lookup(
				IMDPersistenceService.class);
		pservice.updateBillWithAttrs(new Object[] { vo }, fileds);
	}

	@Override
	public void insertsql_RequiresNew(String sql) throws BusinessException {
		// TODO 自动生成的方法存根
		BaseDAO dao = new BaseDAO();
		dao.executeUpdate(sql);
	}

	/**
 * 
 */
	@Override
	public void saleSendGL_RequiresNew(Map<String, Object> mapdata)
			throws BusinessException {
		// TODO 自动生成的方法存根
		ISaveLogService save = NCLocator.getInstance().lookup(
				ISaveLogService.class);
		OutsideLogVO logvo = new OutsideLogVO();
		try {
			logvo.setResult("1");
			logvo.setDesbill("退回回写凭证");
			String delurl = OutsideUtils.getOutsideInfo("SALE02");
			// PropertiesUtils.getInstance("sale_url.properties")
			// .readValue("DELSALEURL");
			String jdata = JSON.toJSONString(mapdata);
			logvo.setSrcparm(jdata);
			String returnjson = Httpconnectionutil.newinstance()
					.connectionjson(delurl, jdata);
			if (returnjson != null) {
				JSONObject returndata = JSONObject.parseObject(returnjson);
				if (!(returndata.getBoolean("success"))) {
					throw new BusinessException(returndata.getString("message"));
				}
			}
		} catch (Exception e) {
			logvo.setErrmsg(e.getMessage());
			logvo.setResult("0");
			throw new BusinessException(e.getMessage());
		} finally {
			try {
				save.saveLog_RequiresNew(logvo);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	@Override
	public void pushImageAction(AggregatedValueObject billvo)
			throws BusinessException {
		pushImage(billvo, true);
		if (billvo instanceof BXVO)
			getDao().updateVO((SuperVO) billvo.getParentVO(),
					new String[] { "zyx16", "zyx17" });
	}

	private void pushImage(AggregatedValueObject billvo, boolean isaction)
			throws BusinessException {
		try {
			String pk_creator = (String) billvo.getParentVO()
					.getAttributeValue(JKHeaderVO.CREATOR);
			String creator = (String) getDao().executeQuery(
					"SELECT user_code FROM sm_user WHERE cuserid = '"
							+ pk_creator + "'", new ColumnProcessor());
			if (StringUtils.isBlank(creator)) {
				throw new BusinessException("当前单据数据异常,创建人信息为空,请检查！");
			}
			if (billvo instanceof JKBXVO) {
				JKBXVO vo = (JKBXVO) billvo;
				JKBXHeaderVO headvo = vo.getParentVO();
				if ((headvo.getDjlxbm().equals("264X-Cxx-009")
						|| headvo.getDjlxbm().equals("264X-Cxx-007") || headvo
						.getDjlxbm().equals("264X-Cxx-008"))) {// 差旅费 //
																// 合同类费用报销单
																// 非合同类费用报销单
																// 新合同类费用请款单
																// 新差旅费报销单
																// 新非合同类费用请款
					String barcode = null;
					if (!StringUtils.isBlank(headvo.getZyx16()) && isaction) {
						barcode = headvo.getZyx16();
					} else if (!StringUtils.isBlank(headvo.getZyx30())
							&& !StringUtils.isBlank(headvo.getZyx16())) {
						barcode = headvo.getZyx16();
					} else {
						barcode = creator + System.currentTimeMillis();
					}
					IGuoXinImage sv = NCLocator.getInstance().lookup(
							IGuoXinImage.class);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("barcode", barcode);
					map.put("anattr2", headvo.getZyx49());// 发票总金额
					map.put("anattr3", headvo.getZyx40());// 发票总税额
					map.put("agreement", headvo.getZyx2());// 合同编码
					String zyx24 = headvo.getZyx24();// 标准指定付款函
					String zyx55 = headvo.getZyx55();// 非标准指定付款函
					if ("Y".equals(zyx24) && "Y".equals(zyx55))
						throw new BusinessException("错误：标准指定付款函与非标准付款函都已选中");
					if (!("Y".equals(zyx24)) && !("Y".equals(zyx55)))
						throw new BusinessException("错误：标准指定付款函与非标准付款函须选中一个");
					if ("Y".equals(zyx24)) {
						if ("264X-Cxx-002".equals(headvo.getDjlxbm())
								|| "264X-Cxx-008".equals(headvo.getDjlxbm())) {
							BXBusItemVO[] itemvos = vo.getChildrenVO();
							if (itemvos != null) {
								BXBusItemVO itemvo = itemvos[0];
								if (itemvo.getHbbm() != null
										&& itemvo.getHbbm().length() > 0) {
									map.put("anattr4",
											getsupplier(itemvo.getHbbm()));
									map.put("anattr5", getbankaccount(itemvo
											.getCustaccount()));// 付款函账号
									map.put("anattr6",
											(String) itemvo.getDefitem49());// 付款函开户银行
								}
							}
						} else {
							String name = getsupplier(headvo.getHbbm());
							String banknum = getbankaccount(headvo
									.getCustaccount());
							// TODO addBy ln 2020年5月15日17:49:20 ===start{===
							if (headvo.getDjlxbm().equals("264X-Cxx-009")) {
								if (headvo.getPaytarget() == 0) {// 员工
									name = (String) getDao().executeQuery(
											"select b.name from bd_psndoc b where b.pk_psndoc='"
													+ headvo.getReceiver()
													+ "'",
											new ColumnProcessor());
									banknum = (String) getDao().executeQuery(
											"select b.accnum from bd_bankaccsub b where b.pk_bankaccsub='"
													+ headvo.getSkyhzh() + "'",
											new ColumnProcessor());
								}
							}
							// ===end}===
							map.put("anattr4", name);// 付款函账户名称
							map.put("anattr5", banknum);// 付款函账号
							map.put("anattr6", headvo.getZyx50());// 付款函开户银行
							// if (headvo.getReceiver() == null) {
							// }
						}
					}
					map.put("datasource", headvo.getZyx10());// 流程类别
					sv.startWorkFlow(headvo.getDjlxbm(), headvo.getPk_jkbx(),
							headvo.getPk_org(), headvo.getCreator(),
							headvo.getBbje(), "2", headvo.getDjbh(), map);
					// 更新表字段影像状态与影像编码
					headvo.setZyx16(barcode);
					headvo.setZyx17("未扫描");// 影像状态
				}
			} else if (billvo instanceof AggAddBillHVO) {
				AddBillHVO headvo = ((AggAddBillHVO) billvo).getParentVO();
				String barcode = null;
				if (!StringUtils.isBlank((String) billvo.getParentVO()
						.getAttributeValue(AddBillHVO.IMAGCODE)) && isaction) {
					barcode = (String) billvo.getParentVO().getAttributeValue(
							AddBillHVO.IMAGCODE);
				} else if (!StringUtils.isBlank((String) billvo.getParentVO()
						.getAttributeValue(AddBillHVO.DEF18))
						&& !StringUtils.isBlank((String) billvo.getParentVO()
								.getAttributeValue(AddBillHVO.IMAGCODE))) {
					barcode = (String) billvo.getParentVO().getAttributeValue(
							AddBillHVO.IMAGCODE);
				} else {
					barcode = creator + System.currentTimeMillis();
				}
				IGuoXinImage sv = NCLocator.getInstance().lookup(
						IGuoXinImage.class);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("barcode", barcode);
				map.put("anattr2", headvo.getDef14());// 发票总金额
				map.put("anattr3", headvo.getDef10());// 发票总税额
				map.put("agreement", headvo.getConcode());// 合同编码
				map.put("datasource", "仅补票审批流程（费控）");// 流程类别
				sv.startWorkFlow(headvo.getPk_tradetype(),
						headvo.getPrimaryKey(), headvo.getPk_org(),
						headvo.getCreator(), headvo.getAmount(), "2",
						headvo.getBillno(), map);
				// 更新表字段影像状态与影像编码
				headvo.setImagcode(barcode);
				headvo.setImagstatus("未扫描");
			} else if (billvo instanceof AggFinancexpenseVO) {
				AggFinancexpenseVO vo = (AggFinancexpenseVO) billvo;
				FinancexpenseVO headvo = vo.getParentVO();
				IGuoXinImage sv = NCLocator.getInstance().lookup(
						IGuoXinImage.class);
				HashMap<String, String> map = new HashMap<String, String>();
				String trancode = (String) getDao()
						.executeQuery(
								"select pk_billtypecode from bd_billtype  where pk_billtypeid='"
										+ headvo.getAttributeValue("transtypepk")
										+ "'", new ColumnProcessor());
				String barcode = null;

				if (!StringUtils.isBlank((String) headvo
						.getAttributeValue("def21")) && isaction) {
					barcode = trancode;
				} else if (StringUtils.isEmpty((String) headvo
						.getAttributeValue("def21"))) {
					barcode = creator + System.currentTimeMillis();// 影像编码
				} else if (!StringUtils.isEmpty((String) headvo
						.getAttributeValue("def21"))
						&& !StringUtils.isEmpty((String) headvo
								.getAttributeValue("def19"))) {// def19:BPM主键，def21:影像编码
					barcode = headvo.getDef21();
				}
				map.put("barcode", barcode);
				String lcname = (String) dao
						.executeQuery(
								"select name from bd_defdoc where pk_defdoc='"
										+ headvo.getAttributeValue("def10")
										+ "'  and pk_defdoclist = '1001121000000000058Z'",
								new ColumnProcessor());
				String pk_org = null;
				if ("RZ06-Cxx-001".equals(trancode)) {// 财顾费
					pk_org = headvo.getPk_org();
					if (pk_org == null)
						pk_org = headvo.getPk_payer();
					map.put("anattr2",
							String.valueOf(headvo.getAttributeValue("def5")));// 发票总金额
					map.put("anattr3",
							String.valueOf(headvo.getAttributeValue("def6")));// 发票总税额
					String def44 = (String) headvo.getAttributeValue("def44");// 非标准付款函字段
					String isflag = (String) dao
							.executeQuery(
									"select pk_defdoc from bd_defdoc where pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code = 'zdy001') and code='01'",
									new ColumnProcessor());
					if (def44 != null) {
						if (!(def44.equals(isflag))) {
							map.put("anattr4", getsupplier(String
									.valueOf(headvo
											.getAttributeValue("pk_payee"))));// 付款函账户名称
							Map<String, String> bankmap = getbankaccount1(String
									.valueOf(headvo.getAttributeValue("def7")));
							if (bankmap != null) {
								map.put("anattr5", bankmap.get("accnum"));// 付款函账号
								map.put("anattr6", getopenBank1(bankmap
										.get("pk_bankaccbas")));// 付款函开户银行
							}
						}
					}
					// add by tjl
					// map.put("anattr6",
					// String.valueOf(headvo.getAttributeValue("vdef18")));//先付款后补票
					map.put("anattr7",
							String.valueOf(headvo.getAttributeValue("def3")));// 单据优先级(bpm填后接回nc)
					map.put("anattr8",
							String.valueOf(headvo.getAttributeValue("def22")));// 影像校验结果(接受)
					map.put("anattr9",
							String.valueOf(headvo.getAttributeValue("def9")));// 影像校验原因(接受)
					map.put("anattr10",
							String.valueOf(headvo.getAttributeValue("def11")));// 影像状态（接受）
					map.put("anattr11", String.valueOf(barcode));// 影像编码
					map.put("datasource", lcname);// 流程类别
					// end
					// map.put("anattr6", arg1);//付款函开户银行
				} else {// 融资费用
					pk_org = headvo.getPk_org();
					map.put("anattr2",
							String.valueOf(headvo.getAttributeValue("def6")));// 发票总金额
					map.put("anattr3",
							String.valueOf(headvo.getAttributeValue("def11")));// 发票总税额
					String def44 = (String) headvo.getAttributeValue("def44");// 非标准付款函字段
					if (!("Y".equals(def44))) {
						map.put("anattr4", getsupplier(String.valueOf(headvo
								.getAttributeValue("pk_payee"))));// 付款函账户名称
						Map<String, String> bankmap = getbankaccount1(String
								.valueOf(headvo.getAttributeValue("def7")));
						if (bankmap != null) {
							map.put("anattr5", bankmap.get("accnum"));// 付款函账号
							map.put("anattr6",
									getopenBank1(bankmap.get("pk_bankaccbas")));// 付款函开户银行
						}
					}
					// add by tjl
					// map.put("anattr6",
					// String.valueOf(headvo.getAttributeValue("def9")));//先付款后补票
					map.put("anattr7",
							String.valueOf(headvo.getAttributeValue("def4")));// 单据优先级(bpm填后接回nc)
					map.put("anattr8",
							String.valueOf(headvo.getAttributeValue("def12")));// 影像校验结果(接受)
					map.put("anattr9",
							String.valueOf(headvo.getAttributeValue("def13")));// 影像校验原因(接受)
					map.put("anattr10",
							String.valueOf(headvo.getAttributeValue("def14")));// 影像状态（接受）
					map.put("anattr11", String.valueOf(barcode));// 影像编码
					map.put("datasource", lcname);// 流程类别
					// end
					// map.put("anattr6", arg1);//付款函开户银行项目主键 billtype 项目主键
					// transtypepk
				}
				boolean ifsendimage = checkimage(pk_org);
				if (ifsendimage) {// 判断业财模式是否推待办
					sv.startWorkFlow(String.valueOf(trancode),
							String.valueOf(headvo
									.getAttributeValue("pk_finexpense")),
							String.valueOf(pk_org), headvo.getCreator(), null,
							"2", String.valueOf(headvo
									.getAttributeValue("billno")), map);
					headvo.setStatus(VOStatus.UPDATED);
					headvo.setAttributeValue("def21", barcode);
					getDao().executeUpdate(
							"update tgrz_financexpense set def21='"
									+ headvo.getDef21()
									+ "' where pk_finexpense='"
									+ headvo.getPk_finexpense() + "'");
				}

			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void saleBack(Map<String, Object> mapdata) throws BusinessException {
		ISaveLogService save = NCLocator.getInstance().lookup(
				ISaveLogService.class);
		OutsideLogVO logvo = new OutsideLogVO();
		try {
			logvo.setResult("1");
			logvo.setDesbill("退款单回退");
			String delurl = OutsideUtils.getOutsideInfo("SALE01");
			String jdata = JSON.toJSONString(mapdata);
			logvo.setSrcparm(jdata);
			String returnjson = Httpconnectionutil.newinstance()
					.connectionjson(delurl, jdata);
			if (returnjson != null) {
				JSONObject returndata = JSONObject.parseObject(returnjson);
				if (!(returndata.getBoolean("success"))) {
					throw new BusinessException("销售系统返回信息："
							+ returndata.getString("message"));
				}
			}
		} catch (Exception e) {
			logvo.setErrmsg(e.getMessage());
			logvo.setResult("0");
			throw new BusinessException(e.getMessage());
		} finally {
			try {
				save.saveLog_RequiresNew(logvo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void returnPfAction(String billType, String billId,
			String checkResult, String checkNote, String checkman,
			String actionName) throws Exception {
		// TODO 自动生成的方法存根
		PfUtilTools.approveSilently(billType, billId, checkResult, checkNote,
				checkman, null, actionName);
	}

	@Override
	public AggOutputTaxHVO[] moreOutputinvoice_RequiresNew(String jsonstr,
			int actiontype) throws Exception {
		// TODO 自动生成的方法存根
		AggOutputTaxHVO[] aggvos = MoreToolsUtil.getUtils().moreOutputinvoice(
				jsonstr, actiontype);
		return aggvos;
	}

	@Override
	public void salecz_RequiresNew(Map<String, Object> mapdata, int type,String pk)
			throws BusinessException {
		// TODO 自动生成的方法存根
		if (type == 1) {// 回写出账
			ISaveLogService save = NCLocator.getInstance().lookup(
					ISaveLogService.class);
			OutsideLogVO logvo = new OutsideLogVO();
			try {
				logvo.setResult("1");
				logvo.setDesbill("销售系统出账");
				String delurl = OutsideUtils.getOutsideInfo("SALE01");
				// ropertiesUtils.getInstance("sale_url.properties")
				// .readValue("BACKSALEURL");
				//String jdata = JSON.toJSONStringWithDateFormat(mapdata,"yyyy-MM-dd HH:mm:ss",SerializerFeature.WriteDateUseDateFormat);
				String jdata =JSON.toJSONString(mapdata);
				logvo.setSrcparm(jdata);
				String returnjson = Httpconnectionutil.newinstance()
						.connectionjson(delurl, jdata);
				if (returnjson != null) {
					JSONObject returndata = JSONObject.parseObject(returnjson);
					if (!(returndata.getBoolean("success"))) {
						throw new BusinessException("销售系统返回信息："
								+ returndata.getString("message"));
					}else {
					getDao().executeUpdate("update cmp_settlement set def1='Y' WHERE pk_settlement='"
							+ pk + "'");
				  }
				}
			} catch (Exception e) {
				logvo.setErrmsg(e.getMessage());
				logvo.setResult("0");
			} finally {
				try {
					save.saveLog_RequiresNew(logvo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {// 回写凭证

			// TODO 自动生成的方法存根
			ISaveLogService save = NCLocator.getInstance().lookup(
					ISaveLogService.class);
			OutsideLogVO logvo = new OutsideLogVO();
			try {
				logvo.setResult("1");
				logvo.setDesbill("定时任务回写凭证");
				String delurl = OutsideUtils.getOutsideInfo("SALE02");
				// PropertiesUtils.getInstance("sale_url.properties")
				// .readValue("DELSALEURL");
				String jdata = JSON.toJSONString(mapdata);
				logvo.setSrcparm(jdata);
				String returnjson = Httpconnectionutil.newinstance()
						.connectionjson(delurl, jdata);
				if (returnjson != null) {
					JSONObject returndata = JSONObject.parseObject(returnjson);
					if (!(returndata.getBoolean("success"))) {
						throw new BusinessException(
								returndata.getString("message"));
					}else{
						getDao().executeUpdate("update gl_voucher   set free1='Y' WHERE pk_voucher='"
								+ pk + "'");
					}
				}
			} catch (Exception e) {
				logvo.setErrmsg(e.getMessage());
				logvo.setResult("0");
			} finally {
				try {
					save.saveLog_RequiresNew(logvo);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public void addworkbill_RequiresNew(AggregatedValueObject billvo,
			String returnmsg) throws BusinessException {
		// TODO 自动生成的方法存根
		IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
				IWorkflowMachine.class);
		IPFBusiAction pf = NCLocator.getInstance().lookup(IPFBusiAction.class);
		Object retObj = null;
		NcToBpmVO vo = new NcToBpmVO();
		IPushBPMBillFileService service = NCLocator.getInstance().lookup(
				IPushBPMBillFileService.class);

		vo.setApprovaltype("ProcessBack");
		vo.setOperationtype("back");

		vo.setCommand(returnmsg);
		if (billvo instanceof AggAddBillHVO) {
			AggAddBillHVO aggvo = (AggAddBillHVO) billvo;
			nc.vo.pubapp.pflow.PfUserObject userobjec = new nc.vo.pubapp.pflow.PfUserObject();
			userobjec.setBusinessCheckMap(new HashMap());
			HashMap eParam = new HashMap();
			WorkflownoteVO workflowVO = null;
			String sql = "select cuserid from sm_user  where user_code = 'BPM'";
			String bpmUserid = (String) getDao().executeQuery(sql,
					new ColumnProcessor());
			vo.setPk_busibill(aggvo.getParentVO().getPrimaryKey());
			vo.setTaskid(aggvo.getParentVO().getDef18());
			vo.setDesbill("仅补票审批流程（费控）");
			try {
				// 审批驳回
				workflowVO = iWorkflowMachine.checkWorkFlow(
						IPFActionName.APPROVE, "267X", aggvo, eParam);
				workflowVO
						.getTaskInfo()
						.getTask()
						.setJumpToActivity(
								getBpmActiveid(aggvo.getParentVO()
										.getPk_addbill()));
				workflowVO.setChecknote(returnmsg);
				workflowVO.getTaskInfo().getTask()
						.setTaskType(WfTaskType.Backward.getIntValue());
				retObj = pf.processAction("APPROVE"
						+ InvocationInfoProxy.getInstance().getUserId(),
						"267X", workflowVO, aggvo, new Object[] { userobjec },
						eParam);
				Map<String, Object> map = service.pushBillToBpm(vo, null);
				if (!"true".equals(map.get("flag")))
					throw new BusinessException(map.get("errorMessage")
							.toString());
			} catch (Exception ex) {
				Logger.error(ex.getMessage(), ex);
				ExceptionUtils.wrappBusinessException("退回BPM异常 :"
						+ ex.getMessage());
			}

		} else if (billvo instanceof BXVO) {
			BXVO aggvo = (BXVO) billvo;
			vo.setPk_busibill(aggvo.getParentVO().getPk_jkbx());
			vo.setApprovaltype("ProcessBack");
			vo.setTaskid(aggvo.getParentVO().getZyx30());
			vo.setCommand(returnmsg);
			vo.setOperationtype("back");
			vo.setDesbill(aggvo.getParentVO().getZyx10());
			try {
				HashMap eParam = new HashMap();
				eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
						PfUtilBaseTools.PARAM_NOTE_CHECKED);
				nc.vo.pubapp.pflow.PfUserObject userobjec = new nc.vo.pubapp.pflow.PfUserObject();
				userobjec.setBusinessCheckMap(new HashMap());
				// 审批驳回
				IPFBusiAction pfBusiAction = NCLocator.getInstance().lookup(
						IPFBusiAction.class);
				WorkflownoteVO worknoteVO = iWorkflowMachine.checkWorkFlow(
						IPFActionName.APPROVE, "264X", billvo, eParam);
				worknoteVO
						.getTaskInfo()
						.getTask()
						.setJumpToActivity(
								getBpmActiveid(aggvo.getParentVO().getPk_jkbx()));
				worknoteVO.setChecknote(returnmsg);
				worknoteVO.getTaskInfo().getTask()
						.setTaskType(WfTaskType.Backward.getIntValue());
				pfBusiAction.processAction(IPFActionName.APPROVE
						+ InvocationInfoProxy.getInstance().getUserId(),
						"264X", worknoteVO, billvo, new Object[] { userobjec },
						eParam);
				Map<String, Object> map = service.pushBillToBpm(vo, null);
				if (map != null) {
					if (!"true".equals(map.get("flag"))) {
						throw new BusinessException(map.get("errorMessage")
								.toString());
					}
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				ExceptionUtils.wrappBusinessException("退回BPM异常 :"
						+ e.getMessage());
			}
		}
	}

	/**
	 * 根据单据id找到审批流程为bpm的审批流程id(控制驳回值bpm处)
	 * 
	 * @param billid
	 * @return
	 */
	public String getBpmActiveid(String billid) {

		int[] states = new int[] { 0, 1 };
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String stateIn = "";
		for (int s : states) {
			stateIn = stateIn + "," + s;
		}
		if (stateIn.length() > 0)
			stateIn = stateIn.substring(1);
		SQLParameter para = new SQLParameter();
		String sqlActivityQuery = "select a.activitydefid, a.actstatus, a.createtime, a.modifytime, b.processdefid from pub_wf_actinstance a left join pub_wf_instance b on a.pk_wf_instance = b.pk_wf_instance where a.pk_wf_instance =(select pk_wf_instance from pub_wf_instance where billid='"
				+ billid + "' and   workflow_type ='2')  ";

		para.addParam(billid);
		if (!StringUtil.isEmptyWithTrim(stateIn)) {
			sqlActivityQuery += " and a.actstatus in (" + stateIn + ")";
		}
		sqlActivityQuery += " order by a.ts desc";
		List<Object[]> alActInstance = null;
		try {
			alActInstance = (List<Object[]>) bs.executeQuery(sqlActivityQuery,
					new ArrayListProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < alActInstance.size(); i++) {
				WorkflowProcess wp = PfDataCache.getWorkflowProcess(
						alActInstance.get(i)[4].toString(),
						alActInstance.get(i)[0].toString());
				Activity act = wp.findActivityByID(alActInstance.get(i)[0]
						.toString());
				if (act.getMultiLangName().toString().contains("BPM")
						|| act.getMultiLangName().toString().contains("bpm")) {
					return act.getId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void genMergeGL_RequiresNew(List<OperatingLogVO> volist,
			String pk_sumrule,String pkuser) throws BusinessException {
		// TODO 自动生成的方法存根
		String usercode=(String)getDao().executeQuery("select   user_code  from sm_user where cuserid='"+pkuser+"'", new ColumnProcessor());
		InvocationInfoProxy.getInstance().setUserId(pkuser);
		InvocationInfoProxy.getInstance().setUserCode(usercode);
		IGenerateService generateService = NCLocator.getInstance().lookup(
				IGenerateService.class);
		generateService.generate(volist.toArray(new OperatingLogVO[0]),
				pk_sumrule);

	}
	/**
	 * 16进制md5
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public String getmd5_16(String key) throws NoSuchAlgorithmException{
		// 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象  
	    MessageDigest md = MessageDigest.getInstance("MD5");  

	    // 2 将消息变成byte数组  
	    byte[] input = key.getBytes();  

	    // 3 计算后获得字节数组,这就是那128位了  
	    byte[] buff = md.digest(input);  

	    // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串  
	    StringBuffer sb = new StringBuffer(buff.length);
	    String sTmp;
        int digtal;
	    for (int i = 0; i < buff.length; i++) {
	    	digtal=buff[i];
	    	if(digtal<0){
	    		digtal+=256;
	    	}
	    	if(digtal<16){sb.append("0");}
	    	
	      sb.append(Integer.toHexString(digtal));
    }
		 return sb.toString().toUpperCase();
	}
	
	
	@Override
	public void sendzpayrec__RequiresNew(Map<String, Object> map)
			throws BusinessException {
		// TODO 自动生成的方法存根
		OutsideLogVO logvo=new OutsideLogVO();
		ISaveLogService save = NCLocator.getInstance().lookup(
				ISaveLogService.class);
		logvo.setDesbill("销售系统转备案");
		logvo.setResult("1");
		logvo.setSrcsystem("SALE");
		try{
		String delurl = OutsideUtils.getOutsideInfo("SALE03");
		String secretkey=OutsideUtils.getOutsideInfo("SALEsecretkey");
		String system=OutsideUtils.getOutsideInfo("SALEsystem");
		 SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		 String str=secretkey+format.format(new Date().getTime())+system;
		String data=JSON.toJSONString(map);
		logvo.setSrcparm(data);
		String result=Httpconnectionutil.newinstance().connectionjson(delurl+"?system=nc&secretkey="+getmd5_16(str), data);
		logvo.setErrmsg(result);
		if (result != null&&result.length()>0) {
			JSONObject returndata = JSONObject.parseObject(result);
			if (!(returndata.getBoolean("success"))) {
				throw new BusinessException(
						returndata.getString("message"));
			}
		}
		}catch(Exception e){
			logvo.setResult("0");
			logvo.setErrmsg(e.getMessage());
		 }finally{
			 try {
				save.saveLog_RequiresNew(logvo);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		 }
		}

	@Override
	public void sendBackpayrec__RequiresNew(Map<String, Object> map) throws BusinessException {
		// TODO 自动生成的方法存根
		// TODO 自动生成的方法存根
				OutsideLogVO logvo=new OutsideLogVO();
				ISaveLogService save = NCLocator.getInstance().lookup(
						ISaveLogService.class);
				logvo.setDesbill("销售系统转备案退回");
				logvo.setResult("1");
				logvo.setSrcsystem("SALE");
				try{
				String delurl = OutsideUtils.getOutsideInfo("SALE03");
				String secretkey=OutsideUtils.getOutsideInfo("SALEsecretkey");
				String system=OutsideUtils.getOutsideInfo("SALEsystem");
				 SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
				 String str=secretkey+format.format(new Date().getTime())+system;
				String data=JSON.toJSONString(map);
				logvo.setSrcparm(data);
				String result=Httpconnectionutil.newinstance().connectionjson(delurl+"?system=nc&secretkey="+getmd5_16(str), data);
				logvo.setErrmsg(result);
				if (result != null&&result.length()>0) {
					JSONObject returndata = JSONObject.parseObject(result);
					if (!(returndata.getBoolean("success"))) {
						throw new BusinessException(
								returndata.getString("message"));
					}
				}
				}catch(Exception e){
					logvo.setResult("0");
					logvo.setErrmsg(e.getMessage());
					throw new BusinessException(e.getMessage());
				 }finally{
					 try {
						save.saveLog_RequiresNew(logvo);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				 }
	}

	@Override
	public String sendBusiness_RequiresNew(String url,Map<String, Object> map,String pk_settlement)
			throws BusinessException {
		// TODO 自动生成的方法存根
		BusinessBillLogVO logvo=new BusinessBillLogVO();
		try{
		String str=JSON.toJSONString(map);
		logvo.setDesbill("商业定时退款结算回写");
		String returnjson= HttpBusinessUtils.getUtils().connection(url, str,logvo);
		 new BaseDAO().executeUpdate(" update cmp_settlement set def1='Y' where pk_settlement='"+pk_settlement+"' ");
	    return returnjson;
		}catch(Exception e){
			throw new BusinessException("商业系统报错"+e.getMessage());
		}
		
	}

	@Override
	public void insertVerfy_RequiresNew(List<ArapBusiDataVO> dlist,
			List<ArapBusiDataVO> clist,String user) throws BusinessException {
		// TODO 自动生成的方法存根
		DefaultVerifyRuleVO rulevo=new DefaultVerifyRuleVO();
		ArrayList<AggverifyVO> aggvolist=null;
		VerifyCom com=	new VerifyCom(new VerifyFilter(), new Saver(), null);
		if(dlist!=null&&dlist.size()>0&&clist!=null&&clist.size()>0){
			AggverifyVO aggverifyVO  =new SameMnyVerify(com).onVerify(dlist.toArray(new ArapBusiDataVO[0]), clist.toArray(new ArapBusiDataVO[0]), rulevo);
			VerifyDetailVO[] detailVOArr = (VerifyDetailVO[]) aggverifyVO
					.getChildrenVO();
			nc.pubitf.accperiod.AccountCalendar accountCalendar=null;
			//补充核销明细部分数据
			for(VerifyDetailVO dvo:detailVOArr){
				dvo.setBusidate(new UFDate());
				dvo.setCreator(user);
				accountCalendar = AccountCalendar.getInstanceByPk_org(dvo.getPk_org());
				accountCalendar.setDate(dvo.getBusidate());
				String accYear = accountCalendar.getYearVO().getPeriodyear();
				String accMonth = accountCalendar.getMonthVO().getAccperiodmth();
				String accPeriod = accountCalendar.getMonthVO().getYearmth();
				dvo.setBusiperiod(accMonth);
				dvo.setBusiyear(accYear);
			}
			//end
			if(aggverifyVO!=null){
				long start = System.currentTimeMillis();
				if (aggverifyVO.getChildrenVO().length > 0) {
					nc.vo.verifynew.pub.VerifyCom.validate(aggverifyVO);
					nc.vo.verifynew.pub.VerifyCom.calGroupAndGloablMny(aggverifyVO);
					aggvolist =  (ArrayList<AggverifyVO>)nc.vo.verifynew.pub.VerifyCom.verifySumData(aggverifyVO);

					Logger.debug("计算全局本位币结束=" + (System.currentTimeMillis() - start) + "ms");
					// 生成核销聚合VO
				}
			}
			//保存核销
			if(aggvolist!=null&&aggvolist.size()>0){
				Hashtable<String,DefaultVerifyRuleVO> table=new Hashtable<String,DefaultVerifyRuleVO>();
				table.put("SAME_VERIFY", rulevo);
				ArrayList<String> pk_verify = ProxyVerify.getIArapVerifyLogPrivate().save(aggvolist,table,com);
				//核销成功后更新
				//贷方
				for(ArapBusiDataVO vo:clist){//pausetransact='N'
					UFDouble occ=vo.getOccupationmny()==null?UFDouble.ZERO_DBL:vo.getOccupationmny();
					UFDouble lcm=vo.getLocal_money()==null?UFDouble.ZERO_DBL:vo.getLocal_money();
					UFDouble nocc=occ.sub(lcm);
					dao.executeUpdate("update arap_busidata set Occupationmny='"+nocc+"'  where pk_busidata='"+vo.getPk_busidata()+"'");
				}
				//借方
				for(ArapBusiDataVO vo:dlist){
					UFDouble occ=vo.getOccupationmny()==null?UFDouble.ZERO_DBL:vo.getOccupationmny();
					UFDouble lcm=vo.getLocal_money()==null?UFDouble.ZERO_DBL:vo.getLocal_money();
					UFDouble nocc=occ.sub(lcm);
					dao.executeUpdate("update arap_busidata set Occupationmny='"+nocc+"' where pk_busidata='"+vo.getPk_busidata()+"'");
				}
				//end
			}
		}
	}
}
