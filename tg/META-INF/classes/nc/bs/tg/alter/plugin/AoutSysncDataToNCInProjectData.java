package nc.bs.tg.alter.plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.bs.tg.alter.result.ConvertResultUtil;
import nc.bs.tg.alter.result.TGInterestMessage;
import nc.itf.tg.IProjectdataMaintain;
import nc.itf.tg.outside.OutsideUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.generator.IdGenerator;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.checkfinance.CheckFinanceHVO;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tg.projectdata.AggProjectDataVO;
import nc.vo.tg.projectdata.CommercialSalesInformationVO;
import nc.vo.tg.projectdata.ConstructionSupportingSalesInformationVO;
import nc.vo.tg.projectdata.OfficeSalesVO;
import nc.vo.tg.projectdata.ParkingSalesInformationVO;
import nc.vo.tg.projectdata.ProjectDataBVO;
import nc.vo.tg.projectdata.ProjectDataCVO;
import nc.vo.tg.projectdata.ProjectDataFVO;
import nc.vo.tg.projectdata.ProjectDataPVO;
import nc.vo.tg.projectdata.ProjectDataVO;
import nc.vo.tg.projectdata.ResidentialSalesInformationVO;
import nc.vo.tg.projectdata.VSalesCapitalProjVO;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * ���ݲ�ϵͳ��ʱͬ����Ŀ������Ϣ��NCϵͳ
 * 
 * @author ASUS
 * 
 */
public class AoutSysncDataToNCInProjectData implements IBackgroundWorkPlugin {
	BaseDAO baseDAO = null;
	IMDPersistenceQueryService queryServcie = null;
	String dblinkName = "@NC_TO_BI_LINK";// dblinke @NC_TO_BI_LINK
	IProjectdataMaintain maintain = null;
	List<String> copylist = null;
	List<String> copylist_c = null;
	List<String> list = new ArrayList<>();

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
//		int keyp = 1;
		AggProjectDataVO aggVO = new AggProjectDataVO();
		String title = bgwc.getAlertTypeName();
		List<VSalesCapitalProjVO> CapitalList = getVSalesCapitalProj();
		Map<String, VSalesCapitalProjVO> capitalMap = new HashMap<String,VSalesCapitalProjVO>();
		for (VSalesCapitalProjVO vSalesCapitalProjVO : CapitalList) {
			if(vSalesCapitalProjVO.getProjectPhases() != null){
				capitalMap.put(vSalesCapitalProjVO.getProjectPhases(), vSalesCapitalProjVO);
			}
		}
		List<Object[]> reflist = new ArrayList<Object[]>();
		ConvertResultUtil util = ConvertResultUtil.getUtil();
		util.setRemsg(new TGInterestMessage());
		ProjectDataVO[] dataVOs = getProjectDataVOs();
		Map<String, AggProjectDataVO> updateVOs = getUpAggProjectDataVOs();

		Object[] msgObjs = new Object[3];
		Map<String, List<ProjectDataPVO>> pMap = null;
		OutsideLogVO logVO = new OutsideLogVO();
		// ��ʱע��
		try {
			pMap = getMainProjectDataPVOs(logVO);
		} catch (Exception e1) {
			return util.executeTask(title, e1.getMessage());
		}
		// msgObjs[2] = msgpro;
		// if (!"".equals(msgObjs[2]) && msgObjs[2] != null) {
		// reflist.add(msgObjs);
		// }
		// logVO.setSrcsystem("EBS");
		// logVO.setDesbill("EBS���ݲ�");
		// logVO.setExedate(new UFDateTime().toString());
		// if (pMap == null) {
		// logVO.setSrcparm(Arrays.toString(list.toArray()));
		// logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
		// logVO.setErrmsg(msgpro);
		// getBaseDAO().insertVO(logVO);
		// return null;
		// }
		// logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
		// getBaseDAO().insertVO(logVO);
		// Map<String, List<ProjectDataPVO>> pMap = getProjectDataPVOs();

		Map<String, List<ProjectDataBVO>> bodyMap = getProjectDataBVOs();
		Map<String, List<ProjectDataCVO>> cMap = getProjectDataCVOs();
		Map<String, String> checkProjectType = new HashMap<String, String>();
		checkProjectType.put("���Ĺ�", getPkFintypeByCode("0004"));// tgrz_fintype
		checkProjectType.put("���Ĺ���Ŀ", getPkFintypeByCode("0004"));// tgrz_fintype
		checkProjectType.put("�ղ�����Ŀ", getPkFintypeByCode("0005"));// tgrz_fintype
		String areaOrgs = "";
		List<CheckFinanceHVO> checkHvos = new ArrayList<CheckFinanceHVO>();
		UFDouble doble = UFDouble.ZERO_DBL;
		for (ProjectDataVO vo : dataVOs) {

			boolean isupdate = false;
			vo.setStatus(VOStatus.NEW);

			if (updateVOs.containsKey(vo.getSrcid())) {
				aggVO = updateVOs.get(vo.getSrcid());
				vo = onTransVO(vo, aggVO.getParentVO());
				vo.setStatus(VOStatus.UPDATED);
				isupdate = true;
			}
			/******** start 2020-09-15 LJF �ӱ�ҳǩ��סլ������Ϣ����ҵ������Ϣ���칫������Ϣ ����λ������Ϣ����������������Ϣ */
			String pk_projectdata = vo.getPk_projectdata();
			/**
			 * ���Ľ�.2020.11.05,ȡ����ϵͳ�����÷������ƹ���
			 */
			List<ProjectDataCVO> cList = cMap.get(vo.getSrcid());
			// סլ������Ϣ
			List<ResidentialSalesInformationVO> rsvoslist = getResidentialSalesInformation(
					capitalMap, cList, pk_projectdata);
			ResidentialSalesInformationVO[] rsvobvos = (ResidentialSalesInformationVO[]) aggVO
					.getChildren(ResidentialSalesInformationVO.class);
			if (rsvobvos != null && rsvobvos.length > 0) {
				getBaseDAO().deleteVOArray(rsvobvos);
			}
			aggVO.setChildren(
					ResidentialSalesInformationVO.class,
					rsvoslist != null && rsvoslist.size() > 0 ? rsvoslist
							.toArray(new ResidentialSalesInformationVO[0])
							: null);
			if (rsvoslist != null && rsvoslist.size() > 0) {

				UFDouble ksmj = UFDouble.ZERO_DBL;
				UFDouble ysmj = UFDouble.ZERO_DBL;
				UFDouble zjzj = UFDouble.ZERO_DBL;
				UFDouble xshkje = UFDouble.ZERO_DBL;
				UFDouble rgywq = UFDouble.ZERO_DBL;
				UFDouble yrgwwq = UFDouble.ZERO_DBL;
				for (ResidentialSalesInformationVO rsvo : rsvoslist) {
					String Def4 = rsvo.getDef4();// סլ�������
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = rsvo.getDef5();// סլ�������
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = rsvo.getDef6();// סլ�ܽ������
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = rsvo.getDef7();// סլ���ۻؿ���
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = rsvo.getDef8();// סլ���Ϲ�����ǩ
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = rsvo.getDef9();// סլ���Ϲ�δ��ǩ
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));
				}
				vo.setDef1(ksmj.toString());// סլ�������
				vo.setDef2(ysmj.toString());// סլ�������
				vo.setDef3(zjzj.toString());// סլ�ܽ������
				vo.setDef4(xshkje.toString());// סլ���ۻؿ���
				vo.setDef5(rgywq.toString());// סլ���Ϲ�����ǩ
				vo.setDef6(yrgwwq.toString());// סլ���Ϲ�δ��ǩ
			}
			// ��ҵ������Ϣ
			List<CommercialSalesInformationVO> csvoslist = getCommercialSalesInformation(
					capitalMap, cList, pk_projectdata);
			CommercialSalesInformationVO[] osvobvos = (CommercialSalesInformationVO[]) aggVO
					.getChildren(CommercialSalesInformationVO.class);
			if (osvobvos != null && osvobvos.length > 0) {
				getBaseDAO().deleteVOArray(osvobvos);
			}
			aggVO.setChildren(
					CommercialSalesInformationVO.class,
					csvoslist != null && csvoslist.size() > 0 ? csvoslist
							.toArray(new CommercialSalesInformationVO[0])
							: null);

			if (csvoslist != null && csvoslist.size() > 0) {
				UFDouble ksmj = UFDouble.ZERO_DBL;
				UFDouble ysmj = UFDouble.ZERO_DBL;
				UFDouble zjzj = UFDouble.ZERO_DBL;
				UFDouble xshkje = UFDouble.ZERO_DBL;
				UFDouble rgywq = UFDouble.ZERO_DBL;
				UFDouble yrgwwq = UFDouble.ZERO_DBL;
				for (CommercialSalesInformationVO csvo : csvoslist) {
					String Def4 = csvo.getDef4();// סլ�������
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = csvo.getDef5();// סլ�������
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = csvo.getDef6();// סլ�ܽ������
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = csvo.getDef7();// סլ���ۻؿ���
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = csvo.getDef8();// סլ���Ϲ�����ǩ
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = csvo.getDef9();// סլ���Ϲ�δ��ǩ
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));

				}
				vo.setDef7(ksmj.toString());// ��ҵ�������
				vo.setDef8(ysmj.toString());// ��ҵ�������
				vo.setDef9(zjzj.toString());// ��ҵ�ܽ������
				vo.setDef10(xshkje.toString());// ��ҵ���ۻؿ���
				vo.setDef11(rgywq.toString());// ��ҵ���Ϲ�����ǩ
				vo.setDef12(yrgwwq.toString());// ��ҵ���Ϲ�δ��ǩ
			}
			// �칫������Ϣ
			List<OfficeSalesVO> osvoslist = getOfficeSales(capitalMap,cList, pk_projectdata);
			OfficeSalesVO[] osvobvo = (OfficeSalesVO[]) aggVO
					.getChildren(OfficeSalesVO.class);
			if (osvobvo != null && osvobvo.length > 0) {
				getBaseDAO().deleteVOArray(osvobvo);
			}
			aggVO.setChildren(
					OfficeSalesVO.class,
					osvoslist != null && osvoslist.size() > 0 ? osvoslist
							.toArray(new OfficeSalesVO[0]) : null);
			if (osvoslist != null && osvoslist.size() > 0) {
				UFDouble ksmj = UFDouble.ZERO_DBL;
				UFDouble ysmj = UFDouble.ZERO_DBL;
				UFDouble zjzj = UFDouble.ZERO_DBL;
				UFDouble xshkje = UFDouble.ZERO_DBL;
				UFDouble rgywq = UFDouble.ZERO_DBL;
				UFDouble yrgwwq = UFDouble.ZERO_DBL;
				for (OfficeSalesVO osvo : osvoslist) {

					String Def4 = osvo.getDef4();// סլ�������
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = osvo.getDef5();// סլ�������
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = osvo.getDef6();// סլ�ܽ������
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = osvo.getDef7();// סլ���ۻؿ���
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = osvo.getDef8();// סլ���Ϲ�����ǩ
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = osvo.getDef9();// סլ���Ϲ�δ��ǩ
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));
				}
				vo.setDef19(ksmj.toString());// �칫�������
				vo.setDef20(ysmj.toString());// �칫�������
				vo.setDef21(zjzj.toString());// �칫�ܽ������
				vo.setDef22(xshkje.toString());// �칫���ۻؿ���
				vo.setDef23(rgywq.toString());// �칫���Ϲ�����ǩ
				vo.setDef24(yrgwwq.toString());// �칫���Ϲ�δ��ǩ
			}
			// ��λ������Ϣ
			List<ParkingSalesInformationVO> psvoslist = getParkingSalesInformations(
					capitalMap, cList, pk_projectdata);
			ParkingSalesInformationVO[] psvobvos = (ParkingSalesInformationVO[]) aggVO
					.getChildren(ParkingSalesInformationVO.class);
			if (psvobvos != null && psvobvos.length > 0) {
				getBaseDAO().deleteVOArray(psvobvos);
			}
			aggVO.setChildren(
					ParkingSalesInformationVO.class,
					psvoslist != null && psvoslist.size() > 0 ? psvoslist
							.toArray(new ParkingSalesInformationVO[0]) : null);
			if (psvoslist != null && psvoslist.size() > 0) {
				UFDouble ksmj = UFDouble.ZERO_DBL;
				UFDouble ysmj = UFDouble.ZERO_DBL;
				UFDouble zjzj = UFDouble.ZERO_DBL;
				UFDouble xshkje = UFDouble.ZERO_DBL;
				UFDouble rgywq = UFDouble.ZERO_DBL;
				UFDouble yrgwwq = UFDouble.ZERO_DBL;
				for (ParkingSalesInformationVO psvo : psvoslist) {
					String Def4 = psvo.getDef4();// סլ�������
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = psvo.getDef5();// סլ�������
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = psvo.getDef6();// סլ�ܽ������
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = psvo.getDef7();// סլ���ۻؿ���
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = psvo.getDef8();// סլ���Ϲ�����ǩ
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = psvo.getDef9();// סլ���Ϲ�δ��ǩ
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));

				}
				vo.setDef13(ksmj.toString());// ��λ�������
				vo.setDef14(ysmj.toString());// ��λ�������
				vo.setDef15(zjzj.toString());// ��λ�ܽ������
				vo.setDef16(xshkje.toString());// ��λ���ۻؿ���
				vo.setDef17(rgywq.toString());// ��λ���Ϲ�����ǩ
				vo.setDef18(yrgwwq.toString());// ��λ���Ϲ�δ��ǩ
			}
			// ��������������Ϣ
			List<ConstructionSupportingSalesInformationVO> cssvoslist = getConstructionSupportingSalesInformation(
					capitalMap, cList, pk_projectdata);
			ConstructionSupportingSalesInformationVO[] cssvobvos = (ConstructionSupportingSalesInformationVO[]) aggVO
					.getChildren(ConstructionSupportingSalesInformationVO.class);
			if (cssvobvos != null && cssvobvos.length > 0) {
				getBaseDAO().deleteVOArray(cssvobvos);
			}
			aggVO.setChildren(
					ConstructionSupportingSalesInformationVO.class,
					cssvoslist != null && cssvoslist.size() > 0 ? cssvoslist
							.toArray(new ConstructionSupportingSalesInformationVO[0])
							: null);
			if (cssvoslist != null && cssvoslist.size() > 0) {
				UFDouble ksmj = UFDouble.ZERO_DBL;
				UFDouble ysmj = UFDouble.ZERO_DBL;
				UFDouble zjzj = UFDouble.ZERO_DBL;
				UFDouble xshkje = UFDouble.ZERO_DBL;
				UFDouble rgywq = UFDouble.ZERO_DBL;
				UFDouble yrgwwq = UFDouble.ZERO_DBL;
				for (ConstructionSupportingSalesInformationVO cssvo : cssvoslist) {
					String Def4 = cssvo.getDef4();// סլ�������
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = cssvo.getDef5();// סլ�������
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = cssvo.getDef6();// סլ�ܽ������
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = cssvo.getDef7();// סլ���ۻؿ���
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = cssvo.getDef8();// סլ���Ϲ�����ǩ
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = cssvo.getDef9();// סլ���Ϲ�δ��ǩ
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));
				}
				vo.setDef25(ksmj.toString());// �칫�������
				vo.setDef26(ysmj.toString());// �칫�������
				vo.setDef27(zjzj.toString());// �칫�ܽ������
				vo.setDef30(xshkje.toString());// �칫���ۻؿ���
				vo.setDef28(rgywq.toString());// �칫���Ϲ�����ǩ
				vo.setDef29(yrgwwq.toString());// �칫���Ϲ�δ��ǩ
			}
			/*
			 * �Խ�P6��Ӫ����Ŀ����ĳһ��ĿP6�ڵ��������ʹ��֤���õع滮���֤�����蹤�̹滮���֤��ʩ�����֤�������������������ĸ���ȡ��ʱ��ʱ
			 * �� ����֪ͨ����ҵ����Ա����ʾ����Ŀ�ѻ���֤����֤������֤��
			 */
			Set<String> keypk = updateVOs.keySet(); // ��ȡ�����ɵ���Ŀ����
													// ��������ʹ��֤���õع滮���֤�����蹤�̹滮���֤��ʩ�����֤

			// if(keyp == 1){
			for (String key : keypk) {
				AggProjectDataVO vou = updateVOs.get(key);
				if (vou.getParentVO().srcid != null
						&& vo.getSrcid().equals(vou.getParentVO().srcid)) {
					UFDate def4 = vou.getParentVO().getP6_datadate4();// ��������ʹ��֤_p6
					UFDate def5 = vou.getParentVO().getP6_datadate5();// �õع滮���֤_p6
					UFDate def6 = vou.getParentVO().getP6_datadate6();// ���蹤�̹滮���֤_p6
					UFDate def7 = vou.getParentVO().getP6_datadate7();// ʩ�����֤_p6
					// UFDate def8 = vou.getParentVO().getP6_datadate8();//
					// ����ʱ��_p6
					if (def4 == null && def5 == null && def6 == null
							&& def7 == null) {
						System.out.println("û�б��");
					} else {
						String msg = null;
						for (ProjectDataVO vop : dataVOs) {// ��ȡ������ϢP6ϵͳ����
							if (vo.getSrcid().equals(vop.getSrcid())) {
								UFDate p6_datadate4 = vop.getP6_datadate4();// ��������ʹ��֤_p6
								UFDate p6_datadate5 = vop.getP6_datadate5();// �õع滮���֤_p6
								UFDate p6_datadate6 = vop.getP6_datadate6();// ���蹤�̹滮���֤_p6
								UFDate p6_datadate7 = vop.getP6_datadate7();// ʩ�����֤_p6
								// UFDate p6_datadate8 =
								// vop.getP6_datadate8();// ����ʱ��_p6
								if (def4 == p6_datadate4
										&& def5 == p6_datadate5
										&& def6 == p6_datadate6
										&& def7 == p6_datadate7) {
									System.out.println("û�б��");

								} else if (def4 != p6_datadate4
										&& def5 == p6_datadate5
										&& def6 == p6_datadate6
										&& def7 == p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "����������ʹ��֤  "
														+ p6_datadate4
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}

								} else if (def4 == p6_datadate4
										&& def5 != p6_datadate5
										&& def6 == p6_datadate6
										&& def7 == p6_datadate7) {

									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "���õع滮���֤  "
														+ p6_datadate5
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								} else if (def4 == p6_datadate4
										&& def5 == p6_datadate5
										&& def6 != p6_datadate6
										&& def7 == p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "�����蹤�̹滮���֤  "
														+ p6_datadate6
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}

								} else if (def4 == p6_datadate4
										&& def5 == p6_datadate5
										&& def6 == p6_datadate6
										&& def7 != p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "��ʩ�����֤  "
														+ p6_datadate7
														+ "��ȡ��Ԥ��֤";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}

								} else if (def4 != p6_datadate4
										&& def5 != p6_datadate5
										&& def6 == p6_datadate6
										&& def7 == p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "����������ʹ��֤ "
														+ p6_datadate4
														+ " �õع滮���֤ "
														+ p6_datadate5
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								} else if (def4 != p6_datadate4
										&& def5 == p6_datadate5
										&& def6 != p6_datadate6
										&& def7 == p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "����������ʹ��֤  "
														+ p6_datadate4
														+ " ���蹤�̹滮���֤ "
														+ p6_datadate6
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								} else if (def4 != p6_datadate4
										&& def5 == p6_datadate5
										&& def6 == p6_datadate6
										&& def7 != p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "����������ʹ��֤  "
														+ p6_datadate4
														+ " ʩ�����֤  "
														+ p6_datadate7
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								} else if (def4 == p6_datadate4
										&& def5 != p6_datadate5
										&& def6 != p6_datadate6
										&& def7 == p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "���õع滮���֤  "
														+ p6_datadate5
														+ " ���蹤�̹滮���֤  "
														+ p6_datadate6
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								} else if (def4 == p6_datadate4
										&& def5 != p6_datadate5
										&& def6 == p6_datadate6
										&& def7 != p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "���õع滮���֤  "
														+ p6_datadate5
														+ " ʩ�����֤  "
														+ p6_datadate7
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								} else if (def4 == p6_datadate4
										&& def5 == p6_datadate5
										&& def6 != p6_datadate6
										&& def7 != p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "�����蹤�̹滮���֤  "
														+ p6_datadate6
														+ " ʩ�����֤  "
														+ p6_datadate7
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								} else if (def4 != p6_datadate4
										&& def5 != p6_datadate5
										&& def6 != p6_datadate6
										&& def7 == p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "����������ʹ��֤  "
														+ p6_datadate4
														+ "�õع滮���֤  "
														+ p6_datadate5
														+ " ���蹤�̹滮���֤  "
														+ p6_datadate6
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								} else if (def4 != p6_datadate4
										&& def5 != p6_datadate5
										&& def6 == p6_datadate6
										&& def7 != p6_datadate7) {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "����������ʹ��֤  "
														+ p6_datadate4
														+ "�õع滮���֤  "
														+ p6_datadate5
														+ " ʩ�����֤  "
														+ p6_datadate7
														+ "��ȡ��Ԥ��֤ ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								} else {
									try {
										String sqlrole = "select sm_user_role.cuserid from sm_user_role  where sm_user_role.pk_role = (select sm_role.pk_role from sm_role where sm_role.role_code = 'RZ003')";
										IUAPQueryBS query = NCLocator
												.getInstance().lookup(
														IUAPQueryBS.class);
										@SuppressWarnings("unchecked")
										List<String> pks = (List<String>) query
												.executeQuery(
														sqlrole,// and rownum
																// = 1
														new ColumnListProcessor());
										if (pks != null && pks.size() > 0) {
											for (String pk_role : pks) {
												msg = vou.getParentVO().name
														+ "����������ʹ��֤  "
														+ p6_datadate4
														+ " �õع滮���֤ "
														+ p6_datadate5
														+ " ���蹤�̹滮���֤ "
														+ p6_datadate6
														+ " ʩ�����֤ "
														+ p6_datadate7
														+ " ��ȡ��Ԥ��֤";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
									}
								}

							}
						}
					}

				}
			}
			// keyp =keyp+1;
			// }
			/******** end 2020-09-15 LJF */
			aggVO.setParentVO(vo);
			// �ؼ���Ϣ
			List<ProjectDataBVO> bodylist = bodyMap.get(vo.getName());
			ProjectDataBVO[] bvos = (ProjectDataBVO[]) aggVO
					.getChildren(ProjectDataBVO.class);
			if (bvos != null && bvos.length > 0) {
				getBaseDAO().deleteVOArray(bvos);
			}
			aggVO.setChildren(
					ProjectDataBVO.class,
					bodylist != null && bodylist.size() > 0 ? bodylist
							.toArray(new ProjectDataBVO[0]) : null);
			// ��ʱע��
			// add by tjl 2019-11-21
			// ��Ŀ�滮����

			List<ProjectDataPVO> plist = pMap.get(vo.getSrcid());
			ProjectDataPVO[] pbvos = (ProjectDataPVO[]) aggVO
					.getChildren(ProjectDataPVO.class);
			if (pbvos != null && pbvos.length > 0) {
				getBaseDAO().deleteVOArray(pbvos);
			}
			aggVO.setChildren(
					ProjectDataPVO.class,
					plist != null && plist.size() > 0 ? plist
							.toArray(new ProjectDataPVO[0]) : null);
			// end

			// add by tjl 2019-11-22
			// ������Ŀ���Ͳ�ѯ�������ʽ������׼
			areaOrgs = getAreaOrgPk(aggVO.getParentVO().getProjectarea());// ����˾
			UFDouble sumMny = UFDouble.ZERO_DBL;
			List<ProjectDataFVO> fvoList = new ArrayList<ProjectDataFVO>();
			ProjectDataFVO[] fbvos = (ProjectDataFVO[]) aggVO
					.getChildren(ProjectDataFVO.class);
			if (fbvos != null && fbvos.length > 0) {
				getBaseDAO().deleteVOArray(fbvos);
			}
			// ��ʱע��

			ProjectDataFVO fvo = new ProjectDataFVO();

			fvo.setPk_projectdata(vo.getPk_projectdata());
			// ǰ������
			checkHvos = getCheckHVOsByAll(
					checkProjectType.get(aggVO.getParentVO().getProjecttype()),
					areaOrgs);
			if (aggVO.getParentVO().getProjecttype() != null
					&& aggVO.getParentVO().getProjecttype().contains("���Ĺ�")
					&& checkHvos.size() > 0 && checkHvos != null) {
				UFDouble ClientMny = UFDouble.ZERO_DBL;
				// ��ȡ��У�鿼�����ʽ������׼��Ӧ�������Ƿ����
				if (checkHvos.size() <= 0 || checkHvos == null) {
					throw new BusinessException("�������ʽ������׼�ڵ�δά��"
							+ aggVO.getParentVO().getDef1() + "���ͻ�δά��������˾"
							+ aggVO.getParentVO().getProjectarea() + "����");
				}
				ProjectDataBVO[] qbvos = (ProjectDataBVO[]) aggVO
						.getChildren(ProjectDataBVO.class);
				if (qbvos != null) {
					for (ProjectDataBVO projectDataBVO : qbvos) {
						sumMny = sumMny.add(projectDataBVO.getPaymny());
					}
					ClientMny = sumMny.multiply(new UFDouble(checkHvos.get(0)
							.getDef2()).multiply(0.01));
				}
				fvo.setDef1(ClientMny.setScale(2, UFDouble.ROUND_HALF_UP)
						.toString());
				fvo.setStatus(VOStatus.NEW);
				fvo.setDr(0);
			}

			// �ղ���
			sumMny = UFDouble.ZERO_DBL;
			checkHvos = getCheckHVOsByAll(
					checkProjectType.get(aggVO.getParentVO().getProjecttype()),
					areaOrgs);
			if (aggVO.getParentVO().getProjecttype() != null
					&& aggVO.getParentVO().getProjecttype().contains("�ղ�����Ŀ")
					&& checkHvos != null && checkHvos.size() > 0) {
				UFDouble ClientMny = UFDouble.ZERO_DBL;
				// ��ȡ��У�鿼�����ʽ������׼��Ӧ�������Ƿ����
				if (checkHvos.size() <= 0 || checkHvos == null) {
					throw new BusinessException("�������ʽ������׼�ڵ�δά��"
							+ aggVO.getParentVO().getDef1() + "���ͻ�δά��������˾"
							+ aggVO.getParentVO().getProjectarea() + "�����飡");
				}
				for (int i = 0; i < checkHvos.size(); i++) {
					ProjectDataBVO[] qbvos = (ProjectDataBVO[]) aggVO
							.getChildren(ProjectDataBVO.class);
					if (qbvos != null) {
						for (ProjectDataBVO projectDataBVO : qbvos) {
							sumMny = sumMny.add(projectDataBVO.getPaymny());
						}
					}
					ClientMny = sumMny.multiply(new UFDouble(checkHvos.get(0)
							.getDef3()).multiply(0.01));
				}
				fvo.setDef2(ClientMny.setScale(2, UFDouble.ROUND_HALF_UP)
						.toString());
				fvo.setStatus(VOStatus.NEW);
				fvo.setDr(0);
			}

			// BuildArea(��Ŀ�滮����)*def6(��������)
			// ������(����)
			UFDouble sumArea = UFDouble.ZERO_DBL;
			checkHvos = getCheckHVOsByAreaPk(areaOrgs);
			if (StringUtils.isNotBlank(aggVO.getParentVO().getProjecttype())
					&& checkHvos.size() > 0 && checkHvos != null) {
				UFDouble ClientMny = UFDouble.ZERO_DBL;
				// �ܽ������
				ProjectDataPVO[] pvos = (ProjectDataPVO[]) aggVO
						.getChildren(ProjectDataPVO.class);
				if (pvos != null) {
					for (ProjectDataPVO projectDataPVO : pvos) {
						sumArea = sumArea.add(new UFDouble(projectDataPVO
								.getDef2()));
					}
				}
				if (checkHvos.size() <= 0 || checkHvos == null) {
					throw new BusinessException("�������ʽ������׼�ڵ�δά��������˾"
							+ aggVO.getParentVO().getProjectarea() + "�����飡");
				}
				ClientMny = sumArea.multiply(
						new UFDouble(checkHvos.get(0).getDef6()))
						.multiply(
								new UFDouble(checkHvos.get(0).getDef4())
										.multiply(0.01));
				fvo.setDef3(ClientMny.setScale(2, UFDouble.ROUND_HALF_UP)
						.toString());
				fvo.setStatus(VOStatus.NEW);
				fvo.setDr(0);
			}
			if (fvo.getDef1() == null) {
				fvo.setDef1("0");
			}
			if (fvo.getDef2() == null) {
				fvo.setDef2("0");
			}
			if (fvo.getDef3() == null) {
				fvo.setDef3("0");
			}
			// ��ʱע��
			fvoList.add(fvo);

			aggVO.setChildren(
					ProjectDataFVO.class,
					fvoList != null && fvoList.size() > 0 ? fvoList
							.toArray(new ProjectDataFVO[0]) : null);
			// end
			// ��ʱע��

			// ������Ϣ
			ProjectDataCVO[] cVOs = null;
			List<ProjectDataCVO> clist = cMap.get(vo.getSrcid());
			cVOs = onTransCVO(
					(ProjectDataCVO[]) aggVO.getChildren(ProjectDataCVO.class),
					clist);

			if (isupdate) {
				bvos = (ProjectDataBVO[]) aggVO
						.getChildren(ProjectDataBVO.class);
				if (bvos != null && bvos.length > 0) {
					for (ProjectDataBVO bvo : bvos) {
						bvo.setAttributeValue(ProjectDataBVO.PK_PROJECTDATA,
								aggVO.getPrimaryKey());
					}
				}

				if (cVOs != null && cVOs.length > 0) {
					for (ProjectDataCVO cvo : cVOs) {
						cvo.setAttributeValue(ProjectDataCVO.PK_PROJECTDATA,
								aggVO.getPrimaryKey());
					}
				}
				// ��ʱע��
				if (plist != null && plist.size() > 0) {
					for (ProjectDataPVO pvo : plist) {
						pvo.setPk_projectdata(aggVO.getPrimaryKey());
					}
				}

				if (fvoList != null && fvoList.size() > 0) {
					for (ProjectDataFVO projectDataFVO : fvoList) {
						projectDataFVO.setPk_projectdata(aggVO.getPrimaryKey());
					}
				}
				// ��ʱע��
			}

			aggVO.setChildren(ProjectDataCVO.class, cVOs);

			Object[] msgObj = new Object[util.getRemsg().getNames().length];

			msgObj[0] = null;
			msgObj[1] = vo.getAttributeValue(ProjectDataVO.NAME);
			String msg = "";
			try {
				getMaintain().syncProjectData_RequiresNew(aggVO);
				msg = (!isupdate ? "����" : "���") + "�ɹ�";

			} catch (Exception e) {
				msg = (!isupdate ? "����" : "���") + "ʧ��:" + e.getMessage();
				Logger.error(e.getMessage());
			}
			msgObj[2] = msg;
			if ("".equals(msgObjs[2]) || msgObjs[2] == null) {
				reflist.add(msgObj);
			}
		}

		return util.executeTask(title, reflist);
		// return null;

	}

	/** �칫������Ϣ */
	private List<OfficeSalesVO> getOfficeSales(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {

		List<OfficeSalesVO> bodylist = new ArrayList<OfficeSalesVO>();
		if (cList != null && cList.size() > 0) {
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					OfficeSalesVO bvo = new OfficeSalesVO();
					bvo.setDef1(salesVO.getProjectPhases());// ��������
					bvo.setDef2(null);// ��������
					bvo.setDef3(salesVO.getMarketingName());// ��������
					bvo.setDef4(salesVO.getOfficeSaleableSpace());// �칫�������
					bvo.setDef5(salesVO.getOfficeSaleableSpaceSub());// �칫�������
					bvo.setDef6(salesVO.getOfficeTotal());// �칫�ܽ������
					bvo.setDef7(salesVO.getOfficeSReceivable());// �칫���ۻؿ���
					bvo.setDef8(salesVO.getOfficeSaleableSpaceSigncon());// �칫���Ϲ�����ǩ
					bvo.setDef9(salesVO.getOfficeSaleableSpaceSub());// �칫���Ϲ�δ��ǩ����
					IdGenerator idtor = NCLocator.getInstance().lookup(
							IdGenerator.class);
					String pk_filemanage = idtor.generate();
					bvo.setPk_office_sales_b(pk_filemanage);
					bvo.setPk_projectdata(pk_projectdata);
					bvo.setAttributeValue("dr", new Integer(0));
					bvo.setStatus(VOStatus.NEW);
					bodylist.add(bvo);
				}
			}
		}
		return bodylist;
	}

	/** ��λ������Ϣ */
	private List<ParkingSalesInformationVO> getParkingSalesInformations(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {

		List<ParkingSalesInformationVO> bodylist = new ArrayList<ParkingSalesInformationVO>();
		if (cList != null && cList.size() > 0) {
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					ParkingSalesInformationVO bvo = new ParkingSalesInformationVO();
					bvo.setDef1(salesVO.getProjectPhases());// ��������
					bvo.setDef2(null);// ��������
					// bvo.setDesalesVOnull);//��������
					bvo.setDef4(salesVO.getSpaceAvailableForSale());// ��λ�������
					bvo.setDef5(salesVO.getSpaceAvailableForSaleSub());// ��λ�������
					bvo.setDef6(salesVO.getTotalParking());// ��λ�ܽ������
					bvo.setDef7(salesVO.getParkingReceivable());// ��λ���ۻؿ���
					bvo.setDef8(salesVO.getSpaceAvailableForSaleSigncon());// ��λ���Ϲ�����ǩ
					bvo.setDef9(salesVO.getSpaceAvailableForSaleSub());// ��λ���Ϲ�δ��ǩ����
					IdGenerator idtor = NCLocator.getInstance().lookup(
							IdGenerator.class);
					String pk_filemanage = idtor.generate();
					bvo.setPk_parking_sales_b(pk_filemanage);
					bvo.setPk_projectdata(pk_projectdata);
					bvo.setAttributeValue("dr", new Integer(0));
					bvo.setStatus(VOStatus.NEW);
					bodylist.add(bvo);
				}
			}
		}
		return bodylist;
	}

	/** ��������������Ϣ */
	private List<ConstructionSupportingSalesInformationVO> getConstructionSupportingSalesInformation(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {

		List<ConstructionSupportingSalesInformationVO> bodylist = new ArrayList<ConstructionSupportingSalesInformationVO>();
		if (cList != null && cList.size() > 0) {
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					ConstructionSupportingSalesInformationVO bvo = new ConstructionSupportingSalesInformationVO();
					bvo.setDef1(salesVO.getProjectPhases());// ��������
					bvo.setDef2(null);// ��������
					// bvo.setDesalesVOnull);//��������
					bvo.setDef4(salesVO.getPublicSacilitiesSaleableArea());// �������׿������
					bvo.setDef5(salesVO.getPublicSacilitiesSaleableAreaSub());// ���������������
					bvo.setDef6(salesVO.getPublicTotaSacilities());// ���������ܽ������
					bvo.setDef7(salesVO.getPublicSacilitiesReceivable());// �����������ۻؿ���
					bvo.setDef8(salesVO.getPublicSacilitiesSaleableAreaSigncon());// ������������ǩ
					bvo.setDef9(salesVO.getPublicSacilitiesSaleableAreaSub());// ��������δ��ǩ
					IdGenerator idtor = NCLocator.getInstance().lookup(
							IdGenerator.class);
					String pk_filemanage = idtor.generate();
					bvo.setPk_construction_supporting_b(pk_filemanage);
					bvo.setPk_projectdata(pk_projectdata);
					bvo.setAttributeValue("dr", new Integer(0));
					bvo.setStatus(VOStatus.NEW);
					bodylist.add(bvo);
				}
			}
		}
		return bodylist;
	}

	/** ��ҵ������Ϣ */
	private List<CommercialSalesInformationVO> getCommercialSalesInformation(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {

		List<CommercialSalesInformationVO> bodylist = new ArrayList<CommercialSalesInformationVO>();
		if (cList != null && cList.size() > 0) {
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					CommercialSalesInformationVO bvo = new CommercialSalesInformationVO();
					bvo.setDef1(salesVO.getProjectPhases());// ��������
					bvo.setDef2(null);// ��������
					bvo.setDef3(salesVO.getMarketingName());// ��������
					bvo.setDef4(salesVO.getCommercialSaleableArea());// ��ҵ�������
					bvo.setDef5(salesVO.getCommercialSaleableAreaSub());// ��ҵ�������
					bvo.setDef6(salesVO.getTotalCommercia());// ��ҵ�ܽ������
					bvo.setDef7(salesVO.getCommercialReceivable());// ��ҵ���ۻؿ���
					bvo.setDef8(salesVO.getCommercialSaleableAreaSigncon());// ��ҵ���Ϲ�����ǩ
					bvo.setDef9(salesVO.getCommercialSaleableAreaSub());// ��ҵ���Ϲ�δ��ǩ����
					IdGenerator idtor = NCLocator.getInstance().lookup(
							IdGenerator.class);
					String pk_filemanage = idtor.generate();
					bvo.setPk_commercial_sales_b(pk_filemanage);
					bvo.setPk_projectdata(pk_projectdata);
					bvo.setAttributeValue("dr", new Integer(0));
					bvo.setStatus(VOStatus.NEW);
					bodylist.add(bvo);
				}
			}

		}
		return bodylist;
	}

	/** סլ������Ϣ */
	private List<ResidentialSalesInformationVO> getResidentialSalesInformation(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {
		List<ResidentialSalesInformationVO> bodylist = new ArrayList<ResidentialSalesInformationVO>();
		if(cList != null && cList.size()>0){
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					ResidentialSalesInformationVO bvo = new ResidentialSalesInformationVO();
					bvo.setDef1(salesVO.getProjectPhases());// ��������
					bvo.setDef2(null);// ��������
					bvo.setDef3(salesVO.getMarketingName());// ��������
					bvo.setDef4(salesVO.getResidentialSaleableArea());// סլ�������
					bvo.setDef5(salesVO.getResidentialSaleableAreaSub());// סլ�������
					bvo.setDef6(salesVO.getTotalHome());// סլ�ܽ������
					bvo.setDef7(salesVO.getHomeReceivable());// סլ���ۻؿ���
					bvo.setDef8(salesVO.getResidentialSaleableAreSigncon());// סլ���Ϲ�����ǩ
					bvo.setDef9(salesVO.getResidentialSaleableAreaSub());// סլ���Ϲ�δ��ǩ����
					IdGenerator idtor = NCLocator.getInstance().lookup(
							IdGenerator.class);
					String pk_filemanage = idtor.generate();
					bvo.setPk_residential_sales_b(pk_filemanage);
					bvo.setPk_projectdata(pk_projectdata);
					bvo.setAttributeValue("dr", new Integer(0));
					bvo.setStatus(VOStatus.NEW);
					bodylist.add(bvo);
				}
			}
		}
		return bodylist;
	}

	private List<VSalesCapitalProjVO> getVSalesCapitalProj()
			throws DAOException {
		String sql = "select * from sdc.v_sales_capital_proj@link_sale ";// where
																								// ����Ŀ
																								// =
																								// 'ʱ�����ţ���ɽ��'
		/*
		 * List<VSalesCapitalProjVO> prolist = (List<VSalesCapitalProjVO>)
		 * getBaseDAO() .executeQuery(sql, new
		 * BeanListProcessor(VSalesCapitalProjVO.class));
		 */
		@SuppressWarnings("unchecked")
		List<Map<String, String>> prolist = (List<Map<String, String>>) getBaseDAO()
				.executeQuery(sql, new MapListProcessor());
		List<VSalesCapitalProjVO> list = new ArrayList<VSalesCapitalProjVO>();
		for (int i = 0; i < prolist.size(); i++) {
			VSalesCapitalProjVO vo = new VSalesCapitalProjVO();
			vo.setProjid(prolist.get(i).get("projid"));
			vo.setProjfcode(prolist.get(i).get("projfcode"));
			vo.setBroadHeading(prolist.get(i).get("����Ŀ"));
			vo.setCityCompany(prolist.get(i).get("���й�˾"));
			vo.setProjectPhases(prolist.get(i).get("��Ŀ��������"));
			vo.setEstablishName(prolist.get(i).get("��������"));
			vo.setMarketingName(prolist.get(i).get("Ӫ������"));
			Object object = prolist.get(i).get("סլ�Ϲ�����");
			if (object != null) {
				vo.setAttributeValue("averageHome", object.toString());
			}
			Object parkingSpaceAverage = prolist.get(i).get("��λ�Ϲ�����");
			if (parkingSpaceAverage != null) {
				vo.setAttributeValue("parkingSpaceAverage",
						parkingSpaceAverage.toString());
			}
			Object commercialAverage = prolist.get(i).get("��ҵ�Ϲ�����");
			if (commercialAverage != null) {
				vo.setAttributeValue("commercialAverage",
						commercialAverage.toString());
			}
			Object officeAverage = prolist.get(i).get("�칫�Ϲ�����");
			if (officeAverage != null) {
				vo.setAttributeValue("officeAverage", officeAverage.toString());
			}
			Object totalHome = prolist.get(i).get("סլ�����");
			if (totalHome != null) {
				vo.setAttributeValue("totalHome", totalHome.toString());
			}
			Object totalParking = prolist.get(i).get("��λ�����");
			if (totalParking != null) {
				vo.setAttributeValue("totalParking", totalParking.toString());
			}
			Object totalCommercia = prolist.get(i).get("��ҵ�����");
			if (totalCommercia != null) {
				vo.setAttributeValue("totalCommercia", totalCommercia.toString());
			}
			Object OfficeTotal = prolist.get(i).get("�칫�����");
			if (OfficeTotal != null) {
				vo.setAttributeValue("OfficeTotal", OfficeTotal.toString());
			}
			Object publicTotaSacilities = prolist.get(i).get("�������������");	
			if (publicTotaSacilities != null) {
				vo.setAttributeValue("publicTotaSacilities", publicTotaSacilities.toString());
			}
			Object residentialSaleableArea = prolist.get(i).get("סլ�������");
			if (residentialSaleableArea != null) {
				vo.setAttributeValue("residentialSaleableArea",
						residentialSaleableArea.toString());
			}
			Object spaceAvailableForSale = prolist.get(i).get("��λ�������");
			if (spaceAvailableForSale != null) {
				vo.setAttributeValue("spaceAvailableForSale",
						spaceAvailableForSale.toString());
			}
			Object commercialSaleableArea = prolist.get(i).get("��ҵ�������");
			if (commercialSaleableArea != null) {
				vo.setAttributeValue("commercialSaleableArea",
						commercialSaleableArea.toString());
			}
			Object officeSaleableSpace = prolist.get(i).get("�칫�������");
			if (officeSaleableSpace != null) {
				vo.setAttributeValue("officeSaleableSpace",
						officeSaleableSpace.toString());
			}
			Object publicSacilitiesSaleableArea = prolist.get(i)
					.get("�������׿������");
			if (publicSacilitiesSaleableArea != null) {
				vo.setAttributeValue("publicSacilitiesSaleableArea",
						publicSacilitiesSaleableArea.toString());
			}
			Object residentialSaleableAreaSub = prolist.get(i).get("סլ�������_�Ϲ�");
			if (residentialSaleableAreaSub != null) {
				vo.setAttributeValue("residentialSaleableAreaSub",
						residentialSaleableAreaSub.toString());
			}
			Object spaceAvailableForSaleSub = prolist.get(i).get("��λ�������_�Ϲ�");
			if (spaceAvailableForSaleSub != null) {
				vo.setAttributeValue("spaceAvailableForSaleSub",
						spaceAvailableForSaleSub.toString());
			}
			Object commercialSaleableAreaSub = prolist.get(i).get("��ҵ�������_�Ϲ�");
			if (commercialSaleableAreaSub != null) {
				vo.setAttributeValue("commercialSaleableAreaSub",
						commercialSaleableAreaSub.toString());
			}
			Object officeSaleableSpaceSub = prolist.get(i).get("�칫�������_�Ϲ�");
			if (officeSaleableSpaceSub != null) {
				vo.setAttributeValue("officeSaleableSpaceSub",
						officeSaleableSpaceSub.toString());
			}
			Object publicSacilitiesSaleableAreaSub = prolist.get(i).get(
					"�����������_�Ϲ�");
			if (publicSacilitiesSaleableAreaSub != null) {
				vo.setAttributeValue("publicSacilitiesSaleableAreaSub",
						publicSacilitiesSaleableAreaSub.toString());
			}
			Object residentialSaleableAreSigncon = prolist.get(i).get(
					"סլ�������_ǩԼ");
			if (residentialSaleableAreSigncon != null) {
				vo.setAttributeValue("residentialSaleableAreSigncon",
						residentialSaleableAreSigncon.toString());
			}
			Object spaceAvailableForSaleSigncon = prolist.get(i).get(
					"��λ�������_ǩԼ");
			if (spaceAvailableForSaleSigncon != null) {
				vo.setAttributeValue("spaceAvailableForSaleSigncon",
						spaceAvailableForSaleSigncon.toString());
			}
			Object commercialSaleableAreaSigncon = prolist.get(i).get(
					"��ҵ�������_ǩԼ");
			if (commercialSaleableAreaSigncon != null) {
				vo.setAttributeValue("commercialSaleableAreaSigncon",
						commercialSaleableAreaSigncon.toString());
			}
			Object officeSaleableSpaceSigncon = prolist.get(i).get("�칫�������_ǩԼ");
			if (officeSaleableSpaceSigncon != null) {
				vo.setAttributeValue("officeSaleableSpaceSigncon",
						officeSaleableSpaceSigncon.toString());
			}
			Object publicSacilitiesSaleableAreaSigncon = prolist.get(i).get(
					"�����������_ǩԼ");
			if (publicSacilitiesSaleableAreaSigncon != null) {
				vo.setAttributeValue("publicSacilitiesSaleableAreaSigncon",
						publicSacilitiesSaleableAreaSigncon.toString());
			}
			Object projectTotalArea = prolist.get(i).get("��Ŀ�ܽ����");
			if (projectTotalArea != null) {
				vo.setAttributeValue("projectTotalArea",
						projectTotalArea.toString());
			}
			Object AmountSalesProceeds = prolist.get(i).get("���ۻؿ���");
			if (AmountSalesProceeds != null) {
				vo.setAttributeValue("AmountSalesProceeds",
						AmountSalesProceeds.toString());
			}
			Object homeReceivable = prolist.get(i).get("סլ�ؿ���");
			if (homeReceivable != null) {
				vo.setAttributeValue("homeReceivable",
						homeReceivable.toString());
			}
			Object parkingReceivable = prolist.get(i).get("��λ�ؿ���");
			if (parkingReceivable != null) {
				vo.setAttributeValue("parkingReceivable",
						parkingReceivable.toString());
			}
			Object commercialReceivable = prolist.get(i).get("��ҵ�ؿ���");
			if (commercialReceivable != null) {
				vo.setAttributeValue("commercialReceivable",
						commercialReceivable.toString());
			}
			Object officeSReceivable = prolist.get(i).get("�칫�ؿ���");
			if (officeSReceivable != null) {
				vo.setAttributeValue("officeSReceivable",
						officeSReceivable.toString());
			}
			Object publicSacilitiesReceivable = prolist.get(i).get("�������׻ؿ���");
			if (publicSacilitiesReceivable != null) {
				vo.setAttributeValue("publicSacilitiesReceivable",
						publicSacilitiesReceivable.toString());
			}

			list.add(vo);
		}

		return list;
	}

	private String getPkFintypeByCode(String code) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select  pk_fintype  from tgrz_fintype WHERE nvl(dr,0) = 0 and code = '"
				+ code + "'  order by code");
		String pk_fintype = (String) getBaseDAO().executeQuery(sql.toString(),
				new ColumnProcessor());
		return pk_fintype == null ? "" : pk_fintype;
	}

	private String getAreaOrgPk(String projectarea) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select  pk_org  from org_orgs WHERE nvl(dr,0) = 0 and name = '"
				+ projectarea + "'  order by code");
		String pk_org = (String) getBaseDAO().executeQuery(sql.toString(),
				new ColumnProcessor());
		return pk_org == null ? "" : pk_org;
	}

	/**
	 * ������Ŀ����pk������˾pk��ѯ���з��ϵĿ������ʼ����׼�ĵ���
	 * 
	 * @param pk
	 * @param areaPk
	 * @return
	 * @throws BusinessException
	 */
	private List<CheckFinanceHVO> getCheckHVOsByAll(String pk, String areaPk)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select   def2,def3,def4  from tgrz_checkfinance_h WHERE nvl(dr,0) = 0 and def1 = '"
				+ pk + "'    order by modifiedtime ");
		@SuppressWarnings("unchecked")
		List<CheckFinanceHVO> value = (List<CheckFinanceHVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(CheckFinanceHVO.class));
		return value;
	}

	/**
	 * ��������˾��ѯ���з��ϵĿ������ʼ����׼�ĵ���
	 * 
	 * @param areaPk
	 * @return
	 * @throws BusinessException
	 */
	private List<CheckFinanceHVO> getCheckHVOsByAreaPk(String areaPk)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select   def4,def6  from tgrz_checkfinance_h WHERE nvl(dr,0) = 0  and def5 = '"
				+ areaPk + "'   order by modifiedtime ");
		@SuppressWarnings("unchecked")
		List<CheckFinanceHVO> value = (List<CheckFinanceHVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(CheckFinanceHVO.class));
		return value;
	}

	private Map<String, List<ProjectDataPVO>> getMainProjectDataPVOs(
			OutsideLogVO logVO) throws Exception {
		// http://times-uat.timesgroup.cn:8080/method?code=projectInfo&currpage=1&syscode=nc&token=9087698DEA1E3BD838762D84373733F0

		// PropertiesUtils util = PropertiesUtils
		// .getInstance("main_url.properties");
		String mianurl = OutsideUtils.getOutsideInfo("DATA01");//
		String syscode = OutsideUtils.getOutsideInfo("DATA02");//
		String token = OutsideUtils.getOutsideInfo("DATA03");//

		String urlstr = mianurl + "?code=projectInfo&syscode=" + syscode
				+ "&token=" + initToken(token);

		Map<String, List<ProjectDataPVO>> bodylists = new HashMap<String, List<ProjectDataPVO>>();
		// �������������л��з�ҳ��ѯ,Ϊȷ����������,�ʴ˴�ʹ��ѭ����ȡ��ҳ,һ����ȡ�ɹ����ҷ��ص�dataΪ��������ѭ��
		for (int i = 1; i < 99999; i++) {
			// String urls = util.readValue("MIANURL")
			// +"&currpage="+i+""
			// +"&syscode="+util.readValue("SYSCODE")
			// +"&token="+initElementInv();
			String urls = urlstr + "&currpage=" + i + "";
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
			// // ת��Ϊ�ֽ�����
			// byte[] data = json.getBytes("utf-8");
			// �����ļ�����
			conn.setRequestProperty("Content-Length",
					String.valueOf(urls.length()));

			// �����ļ�����:
			conn.setRequestProperty("Content-Type", "application/json");

			// ��ʼ��������
			conn.connect();
			String a = "";

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
				String flag = (String) result.getString("code");
				String errmsg = null;
				if (flag.equals("S")) {
					if (result.getJSONArray("data") == null
							|| result.getJSONArray("data").length() <= 0) {
						logVO.setSrcparm("��" + i + "����" + result.toString());
						break;
					}
					JSONArray resMap = result.getJSONArray("data");
					net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
							.fromObject(resMap.toString());
					@SuppressWarnings({ "unchecked", "rawtypes" })
					List<Map<String, Object>> mapListJson = (List) jsonArray;
					for (Map<String, Object> map : mapListJson) {
						if (!bodylists.containsKey(map.get("FNUMBER"))) {
							bodylists.put((String) map.get("FNUMBER"),
									new ArrayList<ProjectDataPVO>());
						}
						ProjectDataPVO pvo = new ProjectDataPVO();
						pvo.setAttributeValue("srcid", map.get("FNUMBER"));// ��Ŀid
						if ("null"
								.equals(map.get("M_FFACTPLOTRATE") != null ? map
										.get("M_FFACTPLOTRATE").toString()
										: "null")
								|| map.get("M_FFACTPLOTRATE") == null
								|| "".equals(map.get("M_FFACTPLOTRATE"))) {
							pvo.setAttributeValue("def1", 0);// �滮�õ����
						} else {
							pvo.setAttributeValue("def1",
									map.get("M_FFACTPLOTRATE") == null ? 0
											: map.get("M_FFACTPLOTRATE"));// �滮�õ����
						}
						if ("null"
								.equals(map.get("FBUILDINGAREA") != null ? map
										.get("FBUILDINGAREA").toString()
										: "null")
								|| map.get("FBUILDINGAREA") == null
								|| "".equals(map.get("FBUILDINGAREA"))) {
							pvo.setAttributeValue("def2", 0);// �ܽ������
						} else {
							pvo.setAttributeValue(
									"def2",
									map.get("FBUILDINGAREA") == null ? 0 : map
											.get("FBUILDINGAREA"));// �ܽ������
						}
						if ("null"
								.equals(map.get("FFACTPLOTAREA") != null ? map
										.get("FFACTPLOTAREA").toString()
										: "null")
								|| map.get("FFACTPLOTAREA") == null
								|| "".equals(map.get("FFACTPLOTAREA"))) {
							pvo.setAttributeValue("def3", 0);// �������
						} else {
							pvo.setAttributeValue(
									"def3",
									map.get("FFACTPLOTAREA") == null ? 0 : map
											.get("FFACTPLOTAREA"));// �������
						}
						if ("null"
								.equals(map.get("FFACTNONPLOTAREA") != null ? map
										.get("FFACTNONPLOTAREA").toString()
										: "null")
								|| map.get("FFACTNONPLOTAREA") == null
								|| "".equals(map.get("FFACTNONPLOTAREA"))) {
							pvo.setAttributeValue("def4", 0);// ���������
						} else {
							pvo.setAttributeValue("def4",
									map.get("FFACTNONPLOTAREA") == null ? 0
											: map.get("FFACTNONPLOTAREA"));// ���������
						}
						if ("null"
								.equals(map.get("FSALEABLEAREA") != null ? map
										.get("FSALEABLEAREA").toString()
										: "null")
								|| map.get("FSALEABLEAREA") == null
								|| "".equals(map.get("FSALEABLEAREA"))) {
							pvo.setAttributeValue("def5", 0);// ���۽������
						} else {
							pvo.setAttributeValue(
									"def5",
									map.get("FSALEABLEAREA") == null ? 0 : map
											.get("FSALEABLEAREA"));// ���۽������
						}
						if ("null"
								.equals(map.get("FNONSALEABLEAREA") != null ? map
										.get("FNONSALEABLEAREA").toString()
										: "null")
								|| map.get("FNONSALEABLEAREA") == null
								|| "".equals(map.get("FNONSALEABLEAREA"))) {
							pvo.setAttributeValue("def6", 0);// �����۽������
						} else {
							pvo.setAttributeValue("def6",
									map.get("FNONSALEABLEAREA") == null ? 0
											: map.get("FNONSALEABLEAREA"));// �����۽������
						}

						pvo.setAttributeValue("dr", new Integer(0));
						pvo.setStatus(VOStatus.NEW);
						bodylists.get((String) map.get("FNUMBER")).add(pvo);
					}
				} else {
					errmsg = (String) result.getString("msg");
					throw new BusinessException("�����������ݵĴ�����Ϣ��" + errmsg + "��");
				}
			} else {
				Logger.error("����ʧ��");
				throw new BusinessException("����ʧ��");
			}
		}
		return bodylists;
	}

	// private String getUrls(int i, PropertiesUtils util) {
	// String urls = util.readValue("MIANURL") + "&currpage=" + i + ""
	// + "&syscode=" + util.readValue("SYSCODE") + "&token="
	// + initToken();
	// // List<String> list = new ArrayList<>();
	// list.add(urls);
	// return urls;
	// }

	/**
	 * ��ʼ��token��Ϣ
	 * 
	 * @param servername
	 * @return
	 */
	private String initToken(String key) {

		Date date = new UFDate().toDate();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// ������ʱ��
		String time = formater.format(date);
		String ticket = time + key;
		String ticketMD5 = DigestUtils.md5Hex(ticket).toUpperCase();// 32λ��д
		// String ticketMD5 = MD5Util.getMD5(ticket).toUpperCase();
		return ticketMD5;
	}

	/**
	 * ��ȡ�����ͼ
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unused")
	private Map<String, List<ProjectDataPVO>> getProjectDataPVOs()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select   *  from v_tgrz_projectarea WHERE projectid IS NOT NULL order by project");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> value = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, List<ProjectDataPVO>> bodylists = new HashMap<String, List<ProjectDataPVO>>();
		if (value != null && value.size() > 0) {
			for (Map<String, Object> map : value) {
				if (!bodylists.containsKey(map.get("projectid"))) {
					bodylists.put((String) map.get("projectid"),
							new ArrayList<ProjectDataPVO>());
				}
				ProjectDataPVO pvo = new ProjectDataPVO();
				pvo.setAttributeValue("srcid", map.get("projectid"));// ��Ŀid
				pvo.setAttributeValue("def1", map.get("ghydarea"));// �滮�õ����
				pvo.setAttributeValue("def2", map.get("buildarea"));// �ܽ������
				pvo.setAttributeValue("def3", map.get("jrbuildarea"));// �������
				pvo.setAttributeValue("def4", map.get("bjrbuildarea"));// ���������
				pvo.setAttributeValue("def5", map.get("cansalearea"));// ���۽������
				pvo.setAttributeValue("def6", map.get("uncansalearea"));// �����۽������

				pvo.setAttributeValue("dr", new Integer(0));
				pvo.setStatus(VOStatus.NEW);
				bodylists.get((String) map.get("projectid")).add(pvo);
			}

		}
		return bodylists;
	}

	/**
	 * ת��������Ϣ
	 * 
	 * @param children
	 * @param clist
	 * @return
	 */
	private ProjectDataCVO[] onTransCVO(ProjectDataCVO[] childrens,
			List<ProjectDataCVO> clist) {

		if (clist != null && clist.size() > 0) {
			List<ProjectDataCVO> pdclist = new ArrayList<ProjectDataCVO>();
			for (ProjectDataCVO cvo : clist) {
				ProjectDataCVO newvo = cvo;
				String name = (String) cvo
						.getAttributeValue(ProjectDataCVO.PERIODIZATIONNAME);
				if (childrens != null && childrens.length > 0) {
					S1: for (ProjectDataCVO children : childrens) {
						String childrenName = (String) children
								.getAttributeValue(ProjectDataCVO.PERIODIZATIONNAME);
						if (name.equals(childrenName)) {
							for (String key : getCopylist_C()) {
								children.setAttributeValue(key,
										cvo.getAttributeValue(key));
							}
							children.setAttributeValue("dr", new Integer(0));
							children.setStatus(VOStatus.UPDATED);
							newvo = children;
							break S1;
						}
					}
				}
				pdclist.add(newvo);

			}
			return pdclist.toArray(new ProjectDataCVO[0]);

		}
		return null;
	}

	/**
	 * 
	 * @param vo
	 * @param parentVO
	 * @return
	 */
	private ProjectDataVO onTransVO(ProjectDataVO newvo, ProjectDataVO oldVO) {
		for (String key : getCopylist()) {
			oldVO.setAttributeValue(key, newvo.getAttributeValue(key));
		}
		return oldVO;
	}

	/**
	 * ��ȡ�����ɵ���Ŀ����
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, AggProjectDataVO> getUpAggProjectDataVOs()
			throws BusinessException {
		String wherestr = "isnull(tgrz_projectdata.dr,0)= 0 and (tgrz_projectdata.srcid) in(select PROJECTID from V_TGRZ_PROJECT ) ";
		@SuppressWarnings("unchecked")
		Collection<AggProjectDataVO> coll = getQueryServcie()
				.queryBillOfVOByCond(AggProjectDataVO.class, wherestr, false);
		Map<String, AggProjectDataVO> dataMap = new HashMap<String, AggProjectDataVO>();
		if (coll != null && coll.size() > 0) {
			for (AggProjectDataVO aggVO : coll) {
				dataMap.put(aggVO.getParentVO().getSrcid(), aggVO);
			}
		}

		return dataMap;
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<ProjectDataCVO>> getProjectDataCVOs()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select   *  from V_TGRZ_PROJECTSTAGING WHERE Projectstaging IS NOT NULL order by PROJECTSTAGINGID");
		List<Map<String, Object>> value = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, List<ProjectDataCVO>> bodylists = new HashMap<String, List<ProjectDataCVO>>();
		if (value != null && value.size() > 0) {
			for (Map<String, Object> map : value) {
				if (!bodylists.containsKey(map.get("projectid"))) {
					bodylists.put((String) map.get("projectid"),
							new ArrayList<ProjectDataCVO>());
				}
				ProjectDataCVO cvo = new ProjectDataCVO();
				cvo.setAttributeValue(ProjectDataCVO.PERIODIZATIONNAME,
						map.get("projectstaging"));// ��������
				cvo.setAttributeValue(ProjectDataCVO.DEF1,
						map.get("projectstagingid"));// ��������
				// P6-INFO
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE1, map
						.get("p6_datadate1") == null ? null : new UFDate(map
						.get("p6_datadate1").toString()));// ��Ŀʱ��
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE2, map
						.get("p6_datadate2") == null ? null : new UFDate(map
						.get("p6_datadate2").toString()));// ��Ӫ������ﵽ_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE3, map
						.get("p6_datadate3") == null ? null : new UFDate(map
						.get("p6_datadate3").toString()));// ���������
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE4, map
						.get("p6_datadate4") == null ? null : new UFDate(map
						.get("p6_datadate4").toString()));// ��������ʹ��֤
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE5, map
						.get("p6_datadate5") == null ? null : new UFDate(map
						.get("p6_datadate5").toString()));// �õع滮���֤
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE6, map
						.get("p6_datadate6") == null ? null : new UFDate(map
						.get("p6_datadate6").toString()));// ���蹤�̹滮���֤
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE7, map
						.get("p6_datadate7") == null ? null : new UFDate(map
						.get("p6_datadate7").toString()));// ʩ�����֤
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE8, map
						.get("p6_datadate8") == null ? null : new UFDate(map
						.get("p6_datadate8").toString()));// ����ʱ��
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE9, map
						.get("p6_datadate9") == null ? null : new UFDate(map
						.get("p6_datadate9").toString()));// ������
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE10, map
						.get("p6_datadate10") == null ? null : new UFDate(map
						.get("p6_datadate10").toString()));// Ԥ��֤_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE11, map
						.get("p6_datadate11") == null ? null : new UFDate(map
						.get("p6_datadate11").toString()));// �ṹ�ⶥ_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE12, map
						.get("p6_datadate12") == null ? null : new UFDate(map
						.get("p6_datadate12").toString()));// ��������_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE13, map
						.get("p6_datadate13") == null ? null : new UFDate(map
						.get("p6_datadate13").toString()));// ����_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE14, map
						.get("p6_datadate14") == null ? null : new UFDate(map
						.get("p6_datadate14").toString()));// ȷȨ_nc

				// NC-INFO
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE1, map
						.get("p6_datadate1") == null ? null : new UFDate(map
						.get("p6_datadate1").toString()));// ��Ŀʱ��
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE2, map
						.get("p6_datadate2") == null ? null : new UFDate(map
						.get("p6_datadate2").toString()));// ��Ӫ������ﵽ_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE3, map
						.get("p6_datadate3") == null ? null : new UFDate(map
						.get("p6_datadate3").toString()));// ���������
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE4, map
						.get("p6_datadate4") == null ? null : new UFDate(map
						.get("p6_datadate4").toString()));// ��������ʹ��֤
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE5, map
						.get("p6_datadate5") == null ? null : new UFDate(map
						.get("p6_datadate5").toString()));// �õع滮���֤
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE6, map
						.get("p6_datadate6") == null ? null : new UFDate(map
						.get("p6_datadate6").toString()));// ���蹤�̹滮���֤
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE7, map
						.get("p6_datadate7") == null ? null : new UFDate(map
						.get("p6_datadate7").toString()));// ʩ�����֤
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE8, map
						.get("p6_datadate8") == null ? null : new UFDate(map
						.get("p6_datadate8").toString()));// ����ʱ��
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE9, map
						.get("p6_datadate9") == null ? null : new UFDate(map
						.get("p6_datadate9").toString()));// ������
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE10, map
						.get("p6_datadate10") == null ? null : new UFDate(map
						.get("p6_datadate10").toString()));// Ԥ��֤_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE11, map
						.get("p6_datadate11") == null ? null : new UFDate(map
						.get("p6_datadate11").toString()));// �ṹ�ⶥ_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE12, map
						.get("p6_datadate12") == null ? null : new UFDate(map
						.get("p6_datadate12").toString()));// ��������_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE13, map
						.get("p6_datadate13") == null ? null : new UFDate(map
						.get("p6_datadate13").toString()));// ����_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE14, map
						.get("p6_datadate14") == null ? null : new UFDate(map
						.get("p6_datadate14").toString()));// ȷȨ_nc

				cvo.setAttributeValue("dr", new Integer(0));
				cvo.setStatus(VOStatus.NEW);
				bodylists.get((String) map.get("projectid")).add(cvo);
			}

		}
		return bodylists;
	}

	/**
	 * ��ȡ�ؼ�
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, List<ProjectDataBVO>> getProjectDataBVOs()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select ��Ŀ����  projectname ")
				// ��Ŀ����
				.append(",������ nmny ")
				// ���
				.append(",to_char(cj_bi.��������,'yyyy-mm-dd') paydate ")
				// �ؼ�/��Ȩ֧��ʱ������
				.append(",case when cj_bi.�Ƿ�֧�������ܻ�='��' then 'Y' else 'N' end  def1 ")// �Ƿ�֧�������ܻ�
				.append(" from cj_bi@NC_TO_BI_LINK ");
		//ע����ʽ��������Ҫ����database links�� @NC_TO_BI_LINK 
		
		// sql.append("select   projectname ")// ��Ŀ����
		// .append(", nmny ")// ���
		// .append(",paydate ")// �ؼ�/��Ȩ֧��ʱ������
		// .append(" from cj_bi");

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> value = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());
		Map<String, List<ProjectDataBVO>> bodylists = new HashMap<String, List<ProjectDataBVO>>();
		if (value != null && value.size() > 0) {
			for (Map<String, Object> map : value) {
				if (!bodylists.containsKey(map.get("projectname"))) {
					bodylists.put((String) map.get("projectname"),
							new ArrayList<ProjectDataBVO>());
				}
				ProjectDataBVO bvo = new ProjectDataBVO();
				bvo.setAttributeValue(
						ProjectDataBVO.PAYMNY,
						map.get("nmny") == null ? null : new UFDouble(String
								.valueOf(map.get("nmny"))).toString());
				bvo.setAttributeValue(
						ProjectDataBVO.PAYDATE,
						map.get("paydate") == null ? null : new UFDate(String
								.valueOf(map.get("paydate"))));
				bvo.setAttributeValue(ProjectDataBVO.DEF1, map.get("def1"));// �Ƿ�֧�������ܻ�
				bvo.setAttributeValue("dr", new Integer(0));
				bvo.setStatus(VOStatus.NEW);
				bodylists.get((String) map.get("projectname")).add(bvo);

			}

		}
		return bodylists;
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private ProjectDataVO[] getProjectDataVOs() throws BusinessException {
		String sql = "select * from V_TGRZ_PROJECT  ";// where
																								// V_TGRZ_PROJECT.project
																								// =
																								// 'ʱ�����ţ���ɽ��'
		@SuppressWarnings("unchecked")
		List<Map<String, String>> prolist = (List<Map<String, String>>) getBaseDAO()
				.executeQuery(sql, new MapListProcessor());

		ProjectDataVO[] vos = null;
		if (prolist != null && prolist.size() > 0) {
			vos = new ProjectDataVO[prolist.size()];
			for (int i$ = 0; i$ < prolist.size(); i$++) {
				vos[i$] = new ProjectDataVO();
				vos[i$].setAttributeValue("pk_group", AppContext.getInstance()
						.getPkGroup());
				vos[i$].setAttributeValue("pk_org", AppContext.getInstance()
						.getPkGroup());
				vos[i$].setAttributeValue("creator", "NC_USER0000000000000");
				vos[i$].setAttributeValue("creationtime", AppContext
						.getInstance().getBusiDate());
				vos[i$].setAttributeValue(ProjectDataVO.NAME, prolist.get(i$)
						.get("project"));// ����
				vos[i$].setAttributeValue(ProjectDataVO.PROJECTAREA, prolist
						.get(i$).get("tfcompany"));// ��Ŀ��������
				vos[i$].setAttributeValue(ProjectDataVO.PROJECTCORP, prolist
						.get(i$).get("projectcompany"));// ��Ŀ���� ��˾
				vos[i$].setAttributeValue(ProjectDataVO.SRCID, prolist.get(i$)
						.get("projectid"));// ��Դ��Ŀ����
				vos[i$].setAttributeValue(ProjectDataVO.PROJECTTYPE, prolist
						.get(i$).get("tztype"));// ��Ŀ����
				// P6-INFO
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE1, prolist
						.get(i$).get("p6_datadate1") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate1")));// ��Ŀʱ��
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE2, prolist
						.get(i$).get("p6_datadate2") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate2")));// ��Ӫ������ﵽ_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE3, prolist
						.get(i$).get("p6_datadate3") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate3")));// ���������
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE4, prolist
						.get(i$).get("p6_datadate4") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate4")));// ��������ʹ��֤
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE5, prolist
						.get(i$).get("p6_datadate5") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate5")));// �õع滮���֤
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE6, prolist
						.get(i$).get("p6_datadate6") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate6")));// ���蹤�̹滮���֤
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE7, prolist
						.get(i$).get("p6_datadate7") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate7")));// ʩ�����֤
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE8, prolist
						.get(i$).get("p6_datadate8") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate8")));// ����ʱ��
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE9, prolist
						.get(i$).get("p6_datadate9") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate9")));// ������
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE10, prolist
						.get(i$).get("p6_datadate10") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate10")));// Ԥ��֤_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE11, prolist
						.get(i$).get("p6_datadate11") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate11")));// �ṹ�ⶥ_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE12, prolist
						.get(i$).get("p6_datadate12") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate12")));// ��������_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE13, prolist
						.get(i$).get("p6_datadate13") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate13")));// ����_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE14, prolist
						.get(i$).get("p6_datadate14") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate14")));// ȷȨ_p6

				// NC-INFO
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE1, prolist
						.get(i$).get("p6_datadate1") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate1")));// ��Ŀʱ��
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE2, prolist
						.get(i$).get("p6_datadate2") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate2")));// ��Ӫ������ﵽ_p6
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE3, prolist
						.get(i$).get("p6_datadate3") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate3")));// ���������
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE4, prolist
						.get(i$).get("p6_datadate4") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate4")));// ��������ʹ��֤
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE5, prolist
						.get(i$).get("p6_datadate5") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate5")));// �õع滮���֤
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE6, prolist
						.get(i$).get("p6_datadate6") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate6")));// ���蹤�̹滮���֤
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE7, prolist
						.get(i$).get("p6_datadate7") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate7")));// ʩ�����֤
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE8, prolist
						.get(i$).get("p6_datadate8") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate8")));// ����ʱ��
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE9, prolist
						.get(i$).get("p6_datadate9") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate9")));// ������
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE10, prolist
						.get(i$).get("p6_datadate10") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate10")));// Ԥ��֤
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE11, prolist
						.get(i$).get("p6_datadate11") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate11")));// �ṹ�ⶥ
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE12, prolist
						.get(i$).get("p6_datadate12") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate12")));// ��������
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE13, prolist
						.get(i$).get("p6_datadate13") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate13")));// ����
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE14, prolist
						.get(i$).get("p6_datadate14") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate14")));// ȷȨ

				vos[i$].setAttributeValue(ProjectDataVO.SRCSYSTEM, "DATADB");//
			}

		}

		return vos;
	}

	/**
	 * ������Ϣ�滻���ֶ���Ϣ
	 * 
	 * @return
	 */
	public List<String> getCopylist() {
		if (copylist == null) {
			copylist = new ArrayList<String>();
			copylist.add(ProjectDataVO.NAME);// ����
			copylist.add(ProjectDataVO.SRCID);// ��Դ��Ŀ
			copylist.add(ProjectDataVO.PROJECTCORP);// ��Ŀ������˾
			copylist.add(ProjectDataVO.PROJECTAREA);// ��Ŀ��������
			copylist.add(ProjectDataVO.PROJECTTYPE);// ��Ŀ����
			copylist.add(ProjectDataVO.P6_DATADATE1);// ��Ŀ��ȡʱ��_p6
			copylist.add(ProjectDataVO.P6_DATADATE2);// ��Ӫ������ﵽ_p6
			copylist.add(ProjectDataVO.P6_DATADATE3);// ���������_p6
			copylist.add(ProjectDataVO.P6_DATADATE4);// ��������ʹ��֤_p6
			copylist.add(ProjectDataVO.P6_DATADATE5);// �õع滮���֤_p6
			copylist.add(ProjectDataVO.P6_DATADATE6);// ���蹤�̹滮���֤_p6
			copylist.add(ProjectDataVO.P6_DATADATE7);// ʩ�����֤_p6
			copylist.add(ProjectDataVO.P6_DATADATE8);// ����ʱ��_p6
			copylist.add(ProjectDataVO.P6_DATADATE9);// ������_p6
			copylist.add(ProjectDataVO.P6_DATADATE10);// Ԥ��֤_p6
			copylist.add(ProjectDataVO.P6_DATADATE11);// �ṹ�ⶥ_p6
			copylist.add(ProjectDataVO.P6_DATADATE12);// ��������_p6
			copylist.add(ProjectDataVO.P6_DATADATE13);// ����_p6
			copylist.add(ProjectDataVO.P6_DATADATE14);// ȷȨ_p6

			// copylist.add(ProjectDataVO.DEF1);//�Զ�����1
			// copylist.add(ProjectDataVO.DEF2);//�Զ�����2
			// copylist.add(ProjectDataVO.DEF3);//�Զ�����3
			// copylist.add(ProjectDataVO.DEF4);//�Զ�����4
			// copylist.add(ProjectDataVO.DEF5);//�Զ�����5
			// copylist.add(ProjectDataVO.DEF6);//�Զ�����6
			// copylist.add(ProjectDataVO.DEF7);//�Զ�����7
			// copylist.add(ProjectDataVO.DEF8);//�Զ�����8
			// copylist.add(ProjectDataVO.DEF9);//�Զ�����9
			// copylist.add(ProjectDataVO.DEF10);//�Զ�����10
			// copylist.add(ProjectDataVO.DEF11);//�Զ�����11
			// copylist.add(ProjectDataVO.DEF12);//�Զ�����12
			// copylist.add(ProjectDataVO.DEF13);//�Զ�����13
			// copylist.add(ProjectDataVO.DEF14);//�Զ�����14
			// copylist.add(ProjectDataVO.DEF15);//�Զ�����15
			// copylist.add(ProjectDataVO.DEF16);//�Զ�����16
			// copylist.add(ProjectDataVO.DEF17);//�Զ�����17
			// copylist.add(ProjectDataVO.DEF18);//�Զ�����18
			// copylist.add(ProjectDataVO.DEF19);//�Զ�����19
			// copylist.add(ProjectDataVO.DEF20);//�Զ�����20
			// copylist.add(ProjectDataVO.DEF21);//�Զ�����21
			// copylist.add(ProjectDataVO.DEF22);//�Զ�����22
			// copylist.add(ProjectDataVO.DEF23);//�Զ�����23
			// copylist.add(ProjectDataVO.DEF24);//�Զ�����24
			// copylist.add(ProjectDataVO.DEF25);//�Զ�����25
			// copylist.add(ProjectDataVO.DEF26);//�Զ�����26
			// copylist.add(ProjectDataVO.DEF27);//�Զ�����27
			// copylist.add(ProjectDataVO.DEF28);//�Զ�����28
			// copylist.add(ProjectDataVO.DEF29);//�Զ�����29
			// copylist.add(ProjectDataVO.DEF30);//�Զ�����30
		}
		return copylist;
	}

	/**
	 * ������Ϣ�滻���ֶ���Ϣ
	 * 
	 * @return
	 */
	public List<String> getCopylist_C() {
		if (copylist_c == null) {
			copylist_c = new ArrayList<String>();
			copylist_c.add(ProjectDataCVO.PERIODIZATIONNAME);// ����
			copylist_c.add(ProjectDataCVO.P6_DATADATE1);// ��Ŀ��ȡʱ��_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE2);// ��Ӫ������ﵽ_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE3);// ���������_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE4);// ��������ʹ��֤_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE5);// �õع滮���֤_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE6);// ���蹤�̹滮���֤_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE7);// ʩ�����֤_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE8);// ����ʱ��_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE9);// ������_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE10);// Ԥ��֤_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE11);// �ṹ�ⶥ_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE12);// ��������_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE13);// ����_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE14);// ȷȨ_p6

		}
		return copylist_c;
	}

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	public IMDPersistenceQueryService getQueryServcie() {
		if (queryServcie == null) {
			queryServcie = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return queryServcie;
	}

	public IProjectdataMaintain getMaintain() {
		if (maintain == null) {
			maintain = NCLocator.getInstance().lookup(
					IProjectdataMaintain.class);
		}
		return maintain;
	}

	/**
	 * ����֪ͨ��Ϣ��NC�û�
	 * 
	 * @throws Exception
	 * */
	public void pushNCUser(String user, String msg) throws Exception {
		StringBuffer contexts = new StringBuffer();// ��������+���ݱ��:SD012020081700000070+����:���Ը���
													// +���ϴ������ļ�
		contexts.append(msg);
		List<NCMessage> ncMessageList = new ArrayList<NCMessage>();
		NCMessage ncMessage = new NCMessage();
		MessageVO messageVO = new MessageVO();
		messageVO.setBillid(null);// ����ID
		messageVO.setBilltype(null);// ��������
		messageVO.setContent(contexts.toString());// ����
		messageVO.setContenttype("text/plain");// ���ݸ�ʽ
		messageVO.setDestination("outbox");// ���λ��
		messageVO.setDomainflag("CDM");// ������
		messageVO.setDr(0);//
		messageVO.setExpiration(null);// ��Ч��
		// messageVO.setISACTIVED 1
		messageVO.setIsdelete(UFBoolean.FALSE);// �Ƿ�ɾ��
		messageVO.setIshandled(UFBoolean.FALSE);// �Ƿ��Ѵ���
		messageVO.setIsread(UFBoolean.FALSE);// �Ƿ��Ѷ�
		messageVO.setMsgsendpk(null);// ������Ϣ������
		messageVO.setMsgsourcetype("worklist");// ������յ���Ϣ�� Ԥ����prealert
												// ֪ͨ��Ϣ��commitnotice
												// ��������worklist
		messageVO.setMsgtype("nc");// ��Ϣ��������
		messageVO.setPk_colorkey(null);
		messageVO.setPk_group(AppContext.getInstance().getPkGroup());// ����
		messageVO.setPk_message(null);// ����
		messageVO.setPk_org(null);// ��֯
		messageVO.setPriority(5);// ���ȼ�
		messageVO.setReceipt(null);// ��ִ
		messageVO.setReceiver(user);// "vo.getUser());// ������ "100112100000003IR5K1"hz
		messageVO.setResendtimes(0);// �ط�����
		messageVO.setSender("100112100000003G9D0D");// ������ NC_USER0000000000000
		messageVO.setSendstate(UFBoolean.TRUE);// �Ƿ��ͳɹ�
		messageVO.setSendtime(new UFDateTime());// ����ʱ��
		messageVO.setSubcolor("~");// ������ɫ
		messageVO.setSubject(contexts.toString());// ����
		messageVO.setTs(null);// ʱ���
		ncMessage.setMessage(messageVO);
		ncMessageList.add(ncMessage);
		MessageCenter.sendMessage(ncMessageList.toArray(new NCMessage[0]));
	}

}
