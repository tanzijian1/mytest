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
		// TODO �Զ����ɵķ������
		IErmAccruedBillManage billManagerSer = NCLocator.getInstance().lookup(
				IErmAccruedBillManage.class);
		return billManagerSer.insertVO(vo);
	}

	@Override
	public AggAccruedBillVO ytupdate_RequiresNew(AggAccruedBillVO vo)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		IErmAccruedBillManage billManagerSer = NCLocator.getInstance().lookup(
				IErmAccruedBillManage.class);
		return billManagerSer.updateVO(vo);
	}

	@Override
	public AggAccruedBillVO ytunapprove_RequiresNew(AggAccruedBillVO vo)
			throws BusinessException {
		// TODO �Զ����ɵķ������
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
		// TODO �Զ����ɵķ������
		BaseDAO dao = new BaseDAO();
		dao.insertVO(vo);
	}

	@Override
	public void billupdate_RequiresNew(SuperVO vo) throws BusinessException {
		// TODO �Զ����ɵķ������
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
		// ҵ��ģʽ ȡֵΪY/N
		String def11 = (String) getDao().executeQuery(
				"select def11  from org_orgs where pk_org='" + pk_org + "'",
				new ColumnProcessor());
		if ("��ҵ��ģʽ".equals(def11))
			return false;
		return true;
	}

	/**
	 * �õ��տ���
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
	 * �õ�����
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
	 * �õ���Ӧ������
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
	 * �õ���������
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
	 * �õ��տ���
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
	 * �õ�����
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
	 * �õ���Ӧ������
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
		// TODO �Զ����ɵķ������
		IMDPersistenceService pservice = NCLocator.getInstance().lookup(
				IMDPersistenceService.class);
		pservice.updateBillWithAttrs(new Object[] { vo }, fileds);
	}

	@Override
	public void insertsql_RequiresNew(String sql) throws BusinessException {
		// TODO �Զ����ɵķ������
		BaseDAO dao = new BaseDAO();
		dao.executeUpdate(sql);
	}

	/**
 * 
 */
	@Override
	public void saleSendGL_RequiresNew(Map<String, Object> mapdata)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		ISaveLogService save = NCLocator.getInstance().lookup(
				ISaveLogService.class);
		OutsideLogVO logvo = new OutsideLogVO();
		try {
			logvo.setResult("1");
			logvo.setDesbill("�˻ػ�дƾ֤");
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
				// TODO �Զ����ɵ� catch ��
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
				throw new BusinessException("��ǰ���������쳣,��������ϢΪ��,���飡");
			}
			if (billvo instanceof JKBXVO) {
				JKBXVO vo = (JKBXVO) billvo;
				JKBXHeaderVO headvo = vo.getParentVO();
				if ((headvo.getDjlxbm().equals("264X-Cxx-009")
						|| headvo.getDjlxbm().equals("264X-Cxx-007") || headvo
						.getDjlxbm().equals("264X-Cxx-008"))) {// ���÷� //
																// ��ͬ����ñ�����
																// �Ǻ�ͬ����ñ�����
																// �º�ͬ�������
																// �²��÷ѱ�����
																// �·Ǻ�ͬ��������
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
					map.put("anattr2", headvo.getZyx49());// ��Ʊ�ܽ��
					map.put("anattr3", headvo.getZyx40());// ��Ʊ��˰��
					map.put("agreement", headvo.getZyx2());// ��ͬ����
					String zyx24 = headvo.getZyx24();// ��׼ָ�����
					String zyx55 = headvo.getZyx55();// �Ǳ�׼ָ�����
					if ("Y".equals(zyx24) && "Y".equals(zyx55))
						throw new BusinessException("���󣺱�׼ָ�������Ǳ�׼�������ѡ��");
					if (!("Y".equals(zyx24)) && !("Y".equals(zyx55)))
						throw new BusinessException("���󣺱�׼ָ�������Ǳ�׼�����ѡ��һ��");
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
											.getCustaccount()));// ����˺�
									map.put("anattr6",
											(String) itemvo.getDefitem49());// �����������
								}
							}
						} else {
							String name = getsupplier(headvo.getHbbm());
							String banknum = getbankaccount(headvo
									.getCustaccount());
							// TODO addBy ln 2020��5��15��17:49:20 ===start{===
							if (headvo.getDjlxbm().equals("264X-Cxx-009")) {
								if (headvo.getPaytarget() == 0) {// Ա��
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
							map.put("anattr4", name);// ����˻�����
							map.put("anattr5", banknum);// ����˺�
							map.put("anattr6", headvo.getZyx50());// �����������
							// if (headvo.getReceiver() == null) {
							// }
						}
					}
					map.put("datasource", headvo.getZyx10());// �������
					sv.startWorkFlow(headvo.getDjlxbm(), headvo.getPk_jkbx(),
							headvo.getPk_org(), headvo.getCreator(),
							headvo.getBbje(), "2", headvo.getDjbh(), map);
					// ���±��ֶ�Ӱ��״̬��Ӱ�����
					headvo.setZyx16(barcode);
					headvo.setZyx17("δɨ��");// Ӱ��״̬
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
				map.put("anattr2", headvo.getDef14());// ��Ʊ�ܽ��
				map.put("anattr3", headvo.getDef10());// ��Ʊ��˰��
				map.put("agreement", headvo.getConcode());// ��ͬ����
				map.put("datasource", "����Ʊ�������̣��ѿأ�");// �������
				sv.startWorkFlow(headvo.getPk_tradetype(),
						headvo.getPrimaryKey(), headvo.getPk_org(),
						headvo.getCreator(), headvo.getAmount(), "2",
						headvo.getBillno(), map);
				// ���±��ֶ�Ӱ��״̬��Ӱ�����
				headvo.setImagcode(barcode);
				headvo.setImagstatus("δɨ��");
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
					barcode = creator + System.currentTimeMillis();// Ӱ�����
				} else if (!StringUtils.isEmpty((String) headvo
						.getAttributeValue("def21"))
						&& !StringUtils.isEmpty((String) headvo
								.getAttributeValue("def19"))) {// def19:BPM������def21:Ӱ�����
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
				if ("RZ06-Cxx-001".equals(trancode)) {// �ƹ˷�
					pk_org = headvo.getPk_org();
					if (pk_org == null)
						pk_org = headvo.getPk_payer();
					map.put("anattr2",
							String.valueOf(headvo.getAttributeValue("def5")));// ��Ʊ�ܽ��
					map.put("anattr3",
							String.valueOf(headvo.getAttributeValue("def6")));// ��Ʊ��˰��
					String def44 = (String) headvo.getAttributeValue("def44");// �Ǳ�׼����ֶ�
					String isflag = (String) dao
							.executeQuery(
									"select pk_defdoc from bd_defdoc where pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code = 'zdy001') and code='01'",
									new ColumnProcessor());
					if (def44 != null) {
						if (!(def44.equals(isflag))) {
							map.put("anattr4", getsupplier(String
									.valueOf(headvo
											.getAttributeValue("pk_payee"))));// ����˻�����
							Map<String, String> bankmap = getbankaccount1(String
									.valueOf(headvo.getAttributeValue("def7")));
							if (bankmap != null) {
								map.put("anattr5", bankmap.get("accnum"));// ����˺�
								map.put("anattr6", getopenBank1(bankmap
										.get("pk_bankaccbas")));// �����������
							}
						}
					}
					// add by tjl
					// map.put("anattr6",
					// String.valueOf(headvo.getAttributeValue("vdef18")));//�ȸ����Ʊ
					map.put("anattr7",
							String.valueOf(headvo.getAttributeValue("def3")));// �������ȼ�(bpm���ӻ�nc)
					map.put("anattr8",
							String.valueOf(headvo.getAttributeValue("def22")));// Ӱ��У����(����)
					map.put("anattr9",
							String.valueOf(headvo.getAttributeValue("def9")));// Ӱ��У��ԭ��(����)
					map.put("anattr10",
							String.valueOf(headvo.getAttributeValue("def11")));// Ӱ��״̬�����ܣ�
					map.put("anattr11", String.valueOf(barcode));// Ӱ�����
					map.put("datasource", lcname);// �������
					// end
					// map.put("anattr6", arg1);//�����������
				} else {// ���ʷ���
					pk_org = headvo.getPk_org();
					map.put("anattr2",
							String.valueOf(headvo.getAttributeValue("def6")));// ��Ʊ�ܽ��
					map.put("anattr3",
							String.valueOf(headvo.getAttributeValue("def11")));// ��Ʊ��˰��
					String def44 = (String) headvo.getAttributeValue("def44");// �Ǳ�׼����ֶ�
					if (!("Y".equals(def44))) {
						map.put("anattr4", getsupplier(String.valueOf(headvo
								.getAttributeValue("pk_payee"))));// ����˻�����
						Map<String, String> bankmap = getbankaccount1(String
								.valueOf(headvo.getAttributeValue("def7")));
						if (bankmap != null) {
							map.put("anattr5", bankmap.get("accnum"));// ����˺�
							map.put("anattr6",
									getopenBank1(bankmap.get("pk_bankaccbas")));// �����������
						}
					}
					// add by tjl
					// map.put("anattr6",
					// String.valueOf(headvo.getAttributeValue("def9")));//�ȸ����Ʊ
					map.put("anattr7",
							String.valueOf(headvo.getAttributeValue("def4")));// �������ȼ�(bpm���ӻ�nc)
					map.put("anattr8",
							String.valueOf(headvo.getAttributeValue("def12")));// Ӱ��У����(����)
					map.put("anattr9",
							String.valueOf(headvo.getAttributeValue("def13")));// Ӱ��У��ԭ��(����)
					map.put("anattr10",
							String.valueOf(headvo.getAttributeValue("def14")));// Ӱ��״̬�����ܣ�
					map.put("anattr11", String.valueOf(barcode));// Ӱ�����
					map.put("datasource", lcname);// �������
					// end
					// map.put("anattr6", arg1);//�������������Ŀ���� billtype ��Ŀ����
					// transtypepk
				}
				boolean ifsendimage = checkimage(pk_org);
				if (ifsendimage) {// �ж�ҵ��ģʽ�Ƿ��ƴ���
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
			logvo.setDesbill("�˿����");
			String delurl = OutsideUtils.getOutsideInfo("SALE01");
			String jdata = JSON.toJSONString(mapdata);
			logvo.setSrcparm(jdata);
			String returnjson = Httpconnectionutil.newinstance()
					.connectionjson(delurl, jdata);
			if (returnjson != null) {
				JSONObject returndata = JSONObject.parseObject(returnjson);
				if (!(returndata.getBoolean("success"))) {
					throw new BusinessException("����ϵͳ������Ϣ��"
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
		// TODO �Զ����ɵķ������
		PfUtilTools.approveSilently(billType, billId, checkResult, checkNote,
				checkman, null, actionName);
	}

	@Override
	public AggOutputTaxHVO[] moreOutputinvoice_RequiresNew(String jsonstr,
			int actiontype) throws Exception {
		// TODO �Զ����ɵķ������
		AggOutputTaxHVO[] aggvos = MoreToolsUtil.getUtils().moreOutputinvoice(
				jsonstr, actiontype);
		return aggvos;
	}

	@Override
	public void salecz_RequiresNew(Map<String, Object> mapdata, int type,String pk)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		if (type == 1) {// ��д����
			ISaveLogService save = NCLocator.getInstance().lookup(
					ISaveLogService.class);
			OutsideLogVO logvo = new OutsideLogVO();
			try {
				logvo.setResult("1");
				logvo.setDesbill("����ϵͳ����");
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
						throw new BusinessException("����ϵͳ������Ϣ��"
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
		} else {// ��дƾ֤

			// TODO �Զ����ɵķ������
			ISaveLogService save = NCLocator.getInstance().lookup(
					ISaveLogService.class);
			OutsideLogVO logvo = new OutsideLogVO();
			try {
				logvo.setResult("1");
				logvo.setDesbill("��ʱ�����дƾ֤");
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
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public void addworkbill_RequiresNew(AggregatedValueObject billvo,
			String returnmsg) throws BusinessException {
		// TODO �Զ����ɵķ������
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
			vo.setDesbill("����Ʊ�������̣��ѿأ�");
			try {
				// ��������
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
				ExceptionUtils.wrappBusinessException("�˻�BPM�쳣 :"
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
				// ��������
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
				ExceptionUtils.wrappBusinessException("�˻�BPM�쳣 :"
						+ e.getMessage());
			}
		}
	}

	/**
	 * ���ݵ���id�ҵ���������Ϊbpm����������id(���Ʋ���ֵbpm��)
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
		// TODO �Զ����ɵķ������
		String usercode=(String)getDao().executeQuery("select   user_code  from sm_user where cuserid='"+pkuser+"'", new ColumnProcessor());
		InvocationInfoProxy.getInstance().setUserId(pkuser);
		InvocationInfoProxy.getInstance().setUserCode(usercode);
		IGenerateService generateService = NCLocator.getInstance().lookup(
				IGenerateService.class);
		generateService.generate(volist.toArray(new OperatingLogVO[0]),
				pk_sumrule);

	}
	/**
	 * 16����md5
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public String getmd5_16(String key) throws NoSuchAlgorithmException{
		// 1 ����һ���ṩ��ϢժҪ�㷨�Ķ��󣬳�ʼ��Ϊmd5�㷨����  
	    MessageDigest md = MessageDigest.getInstance("MD5");  

	    // 2 ����Ϣ���byte����  
	    byte[] input = key.getBytes();  

	    // 3 ��������ֽ�����,�������128λ��  
	    byte[] buff = md.digest(input);  

	    // 4 ������ÿһ�ֽڣ�һ���ֽ�ռ��λ������16��������md5�ַ���  
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
		// TODO �Զ����ɵķ������
		OutsideLogVO logvo=new OutsideLogVO();
		ISaveLogService save = NCLocator.getInstance().lookup(
				ISaveLogService.class);
		logvo.setDesbill("����ϵͳת����");
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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		 }
		}

	@Override
	public void sendBackpayrec__RequiresNew(Map<String, Object> map) throws BusinessException {
		// TODO �Զ����ɵķ������
		// TODO �Զ����ɵķ������
				OutsideLogVO logvo=new OutsideLogVO();
				ISaveLogService save = NCLocator.getInstance().lookup(
						ISaveLogService.class);
				logvo.setDesbill("����ϵͳת�����˻�");
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
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				 }
	}

	@Override
	public String sendBusiness_RequiresNew(String url,Map<String, Object> map,String pk_settlement)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		BusinessBillLogVO logvo=new BusinessBillLogVO();
		try{
		String str=JSON.toJSONString(map);
		logvo.setDesbill("��ҵ��ʱ�˿�����д");
		String returnjson= HttpBusinessUtils.getUtils().connection(url, str,logvo);
		 new BaseDAO().executeUpdate(" update cmp_settlement set def1='Y' where pk_settlement='"+pk_settlement+"' ");
	    return returnjson;
		}catch(Exception e){
			throw new BusinessException("��ҵϵͳ����"+e.getMessage());
		}
		
	}

	@Override
	public void insertVerfy_RequiresNew(List<ArapBusiDataVO> dlist,
			List<ArapBusiDataVO> clist,String user) throws BusinessException {
		// TODO �Զ����ɵķ������
		DefaultVerifyRuleVO rulevo=new DefaultVerifyRuleVO();
		ArrayList<AggverifyVO> aggvolist=null;
		VerifyCom com=	new VerifyCom(new VerifyFilter(), new Saver(), null);
		if(dlist!=null&&dlist.size()>0&&clist!=null&&clist.size()>0){
			AggverifyVO aggverifyVO  =new SameMnyVerify(com).onVerify(dlist.toArray(new ArapBusiDataVO[0]), clist.toArray(new ArapBusiDataVO[0]), rulevo);
			VerifyDetailVO[] detailVOArr = (VerifyDetailVO[]) aggverifyVO
					.getChildrenVO();
			nc.pubitf.accperiod.AccountCalendar accountCalendar=null;
			//���������ϸ��������
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

					Logger.debug("����ȫ�ֱ�λ�ҽ���=" + (System.currentTimeMillis() - start) + "ms");
					// ���ɺ����ۺ�VO
				}
			}
			//�������
			if(aggvolist!=null&&aggvolist.size()>0){
				Hashtable<String,DefaultVerifyRuleVO> table=new Hashtable<String,DefaultVerifyRuleVO>();
				table.put("SAME_VERIFY", rulevo);
				ArrayList<String> pk_verify = ProxyVerify.getIArapVerifyLogPrivate().save(aggvolist,table,com);
				//�����ɹ������
				//����
				for(ArapBusiDataVO vo:clist){//pausetransact='N'
					UFDouble occ=vo.getOccupationmny()==null?UFDouble.ZERO_DBL:vo.getOccupationmny();
					UFDouble lcm=vo.getLocal_money()==null?UFDouble.ZERO_DBL:vo.getLocal_money();
					UFDouble nocc=occ.sub(lcm);
					dao.executeUpdate("update arap_busidata set Occupationmny='"+nocc+"'  where pk_busidata='"+vo.getPk_busidata()+"'");
				}
				//�跽
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
