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
 * 数据仓系统定时同步项目资料信息至NC系统
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
		// 暂时注释
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
		// logVO.setDesbill("EBS数据仓");
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
		checkProjectType.put("招拍挂", getPkFintypeByCode("0004"));// tgrz_fintype
		checkProjectType.put("招拍挂项目", getPkFintypeByCode("0004"));// tgrz_fintype
		checkProjectType.put("收并购项目", getPkFintypeByCode("0005"));// tgrz_fintype
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
			/******** start 2020-09-15 LJF 子表页签的住宅销售信息、商业销售信息、办公销售信息 、车位销售信息、公建配套销售信息 */
			String pk_projectdata = vo.getPk_projectdata();
			/**
			 * 吕文杰.2020.11.05,取销售系统数据用分期名称关联
			 */
			List<ProjectDataCVO> cList = cMap.get(vo.getSrcid());
			// 住宅销售信息
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
					String Def4 = rsvo.getDef4();// 住宅可售面积
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = rsvo.getDef5();// 住宅已售面积
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = rsvo.getDef6();// 住宅总建筑面积
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = rsvo.getDef7();// 住宅销售回款金额
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = rsvo.getDef8();// 住宅已认购已网签
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = rsvo.getDef9();// 住宅已认购未网签
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));
				}
				vo.setDef1(ksmj.toString());// 住宅可售面积
				vo.setDef2(ysmj.toString());// 住宅已售面积
				vo.setDef3(zjzj.toString());// 住宅总建筑面积
				vo.setDef4(xshkje.toString());// 住宅销售回款金额
				vo.setDef5(rgywq.toString());// 住宅已认购已网签
				vo.setDef6(yrgwwq.toString());// 住宅已认购未网签
			}
			// 商业销售信息
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
					String Def4 = csvo.getDef4();// 住宅可售面积
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = csvo.getDef5();// 住宅已售面积
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = csvo.getDef6();// 住宅总建筑面积
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = csvo.getDef7();// 住宅销售回款金额
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = csvo.getDef8();// 住宅已认购已网签
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = csvo.getDef9();// 住宅已认购未网签
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));

				}
				vo.setDef7(ksmj.toString());// 商业可售面积
				vo.setDef8(ysmj.toString());// 商业已售面积
				vo.setDef9(zjzj.toString());// 商业总建筑面积
				vo.setDef10(xshkje.toString());// 商业销售回款金额
				vo.setDef11(rgywq.toString());// 商业已认购已网签
				vo.setDef12(yrgwwq.toString());// 商业已认购未网签
			}
			// 办公销售信息
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

					String Def4 = osvo.getDef4();// 住宅可售面积
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = osvo.getDef5();// 住宅已售面积
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = osvo.getDef6();// 住宅总建筑面积
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = osvo.getDef7();// 住宅销售回款金额
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = osvo.getDef8();// 住宅已认购已网签
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = osvo.getDef9();// 住宅已认购未网签
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));
				}
				vo.setDef19(ksmj.toString());// 办公可售面积
				vo.setDef20(ysmj.toString());// 办公已售面积
				vo.setDef21(zjzj.toString());// 办公总建筑面积
				vo.setDef22(xshkje.toString());// 办公销售回款金额
				vo.setDef23(rgywq.toString());// 办公已认购已网签
				vo.setDef24(yrgwwq.toString());// 办公已认购未网签
			}
			// 车位销售信息
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
					String Def4 = psvo.getDef4();// 住宅可售面积
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = psvo.getDef5();// 住宅已售面积
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = psvo.getDef6();// 住宅总建筑面积
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = psvo.getDef7();// 住宅销售回款金额
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = psvo.getDef8();// 住宅已认购已网签
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = psvo.getDef9();// 住宅已认购未网签
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));

				}
				vo.setDef13(ksmj.toString());// 车位可售面积
				vo.setDef14(ysmj.toString());// 车位已售面积
				vo.setDef15(zjzj.toString());// 车位总建筑面积
				vo.setDef16(xshkje.toString());// 车位销售回款金额
				vo.setDef17(rgywq.toString());// 车位已认购已网签
				vo.setDef18(yrgwwq.toString());// 车位已认购未网签
			}
			// 公建配套销售信息
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
					String Def4 = cssvo.getDef4();// 住宅可售面积
					ksmj = ksmj.add((UFDouble) (Def4 == null ? doble
							: new UFDouble(Def4)));
					String Def5 = cssvo.getDef5();// 住宅已售面积
					ysmj = ysmj.add((UFDouble) (Def5 == null ? doble
							: new UFDouble(Def5)));
					String Def6 = cssvo.getDef6();// 住宅总建筑面积
					zjzj = zjzj.add((UFDouble) (Def6 == null ? doble
							: new UFDouble(Def6)));
					String Def7 = cssvo.getDef7();// 住宅销售回款金额
					xshkje = xshkje.add((UFDouble) (Def7 == null ? doble
							: new UFDouble(Def7)));
					String Def8 = cssvo.getDef8();// 住宅已认购已网签
					rgywq = rgywq.add((UFDouble) (Def8 == null ? doble
							: new UFDouble(Def8)));
					String Def9 = cssvo.getDef9();// 住宅已认购未网签
					yrgwwq = yrgwwq.add((UFDouble) (Def9 == null ? doble
							: new UFDouble(Def9)));
				}
				vo.setDef25(ksmj.toString());// 办公可售面积
				vo.setDef26(ysmj.toString());// 办公已售面积
				vo.setDef27(zjzj.toString());// 办公总建筑面积
				vo.setDef30(xshkje.toString());// 办公销售回款金额
				vo.setDef28(rgywq.toString());// 办公已认购已网签
				vo.setDef29(yrgwwq.toString());// 办公已认购未网签
			}
			/*
			 * 对接P6运营类项目，当某一项目P6节点国有土地使用证、用地规划许可证、建设工程规划许可证、施工许可证中有两个、三个、或四个获取到时间时
			 * ， 发送通知给各业务部人员，提示该项目已获两证、三证、或四证；
			 */
			Set<String> keypk = updateVOs.keySet(); // 读取已生成的项目资料
													// 国有土地使用证、用地规划许可证、建设工程规划许可证、施工许可证

			// if(keyp == 1){
			for (String key : keypk) {
				AggProjectDataVO vou = updateVOs.get(key);
				if (vou.getParentVO().srcid != null
						&& vo.getSrcid().equals(vou.getParentVO().srcid)) {
					UFDate def4 = vou.getParentVO().getP6_datadate4();// 国有土地使用证_p6
					UFDate def5 = vou.getParentVO().getP6_datadate5();// 用地规划许可证_p6
					UFDate def6 = vou.getParentVO().getP6_datadate6();// 建设工程规划许可证_p6
					UFDate def7 = vou.getParentVO().getP6_datadate7();// 施工许可证_p6
					// UFDate def8 = vou.getParentVO().getP6_datadate8();//
					// 开工时间_p6
					if (def4 == null && def5 == null && def6 == null
							&& def7 == null) {
						System.out.println("没有变更");
					} else {
						String msg = null;
						for (ProjectDataVO vop : dataVOs) {// 读取主体信息P6系统数据
							if (vo.getSrcid().equals(vop.getSrcid())) {
								UFDate p6_datadate4 = vop.getP6_datadate4();// 国有土地使用证_p6
								UFDate p6_datadate5 = vop.getP6_datadate5();// 用地规划许可证_p6
								UFDate p6_datadate6 = vop.getP6_datadate6();// 建设工程规划许可证_p6
								UFDate p6_datadate7 = vop.getP6_datadate7();// 施工许可证_p6
								// UFDate p6_datadate8 =
								// vop.getP6_datadate8();// 开工时间_p6
								if (def4 == p6_datadate4
										&& def5 == p6_datadate5
										&& def6 == p6_datadate6
										&& def7 == p6_datadate7) {
									System.out.println("没有变更");

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
														+ "：国有土地使用证  "
														+ p6_datadate4
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：用地规划许可证  "
														+ p6_datadate5
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：建设工程规划许可证  "
														+ p6_datadate6
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：施工许可证  "
														+ p6_datadate7
														+ "已取得预售证";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：国有土地使用证 "
														+ p6_datadate4
														+ " 用地规划许可证 "
														+ p6_datadate5
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：国有土地使用证  "
														+ p6_datadate4
														+ " 建设工程规划许可证 "
														+ p6_datadate6
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：国有土地使用证  "
														+ p6_datadate4
														+ " 施工许可证  "
														+ p6_datadate7
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：用地规划许可证  "
														+ p6_datadate5
														+ " 建设工程规划许可证  "
														+ p6_datadate6
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：用地规划许可证  "
														+ p6_datadate5
														+ " 施工许可证  "
														+ p6_datadate7
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：建设工程规划许可证  "
														+ p6_datadate6
														+ " 施工许可证  "
														+ p6_datadate7
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：国有土地使用证  "
														+ p6_datadate4
														+ "用地规划许可证  "
														+ p6_datadate5
														+ " 建设工程规划许可证  "
														+ p6_datadate6
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：国有土地使用证  "
														+ p6_datadate4
														+ "用地规划许可证  "
														+ p6_datadate5
														+ " 施工许可证  "
														+ p6_datadate7
														+ "已取得预售证 ";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
														+ "：国有土地使用证  "
														+ p6_datadate4
														+ " 用地规划许可证 "
														+ p6_datadate5
														+ " 建设工程规划许可证 "
														+ p6_datadate6
														+ " 施工许可证 "
														+ p6_datadate7
														+ " 已取得预售证";
												pushNCUser(pk_role, msg);
											}
										}
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
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
			// 地价信息
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
			// 暂时注释
			// add by tjl 2019-11-21
			// 项目规划数据

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
			// 根据项目类型查询考核融资金额计算标准
			areaOrgs = getAreaOrgPk(aggVO.getParentVO().getProjectarea());// 区域公司
			UFDouble sumMny = UFDouble.ZERO_DBL;
			List<ProjectDataFVO> fvoList = new ArrayList<ProjectDataFVO>();
			ProjectDataFVO[] fbvos = (ProjectDataFVO[]) aggVO
					.getChildren(ProjectDataFVO.class);
			if (fbvos != null && fbvos.length > 0) {
				getBaseDAO().deleteVOArray(fbvos);
			}
			// 暂时注释

			ProjectDataFVO fvo = new ProjectDataFVO();

			fvo.setPk_projectdata(vo.getPk_projectdata());
			// 前端融资
			checkHvos = getCheckHVOsByAll(
					checkProjectType.get(aggVO.getParentVO().getProjecttype()),
					areaOrgs);
			if (aggVO.getParentVO().getProjecttype() != null
					&& aggVO.getParentVO().getProjecttype().contains("招拍挂")
					&& checkHvos.size() > 0 && checkHvos != null) {
				UFDouble ClientMny = UFDouble.ZERO_DBL;
				// 获取并校验考核融资金额计算标准对应的数据是否存在
				if (checkHvos.size() <= 0 || checkHvos == null) {
					throw new BusinessException("考核融资金额计算标准节点未维护"
							+ aggVO.getParentVO().getDef1() + "类型或未维护地区公司"
							+ aggVO.getParentVO().getProjectarea() + "请检查");
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

			// 收并购
			sumMny = UFDouble.ZERO_DBL;
			checkHvos = getCheckHVOsByAll(
					checkProjectType.get(aggVO.getParentVO().getProjecttype()),
					areaOrgs);
			if (aggVO.getParentVO().getProjecttype() != null
					&& aggVO.getParentVO().getProjecttype().contains("收并购项目")
					&& checkHvos != null && checkHvos.size() > 0) {
				UFDouble ClientMny = UFDouble.ZERO_DBL;
				// 获取并校验考核融资金额计算标准对应的数据是否存在
				if (checkHvos.size() <= 0 || checkHvos == null) {
					throw new BusinessException("考核融资金额计算标准节点未维护"
							+ aggVO.getParentVO().getDef1() + "类型或未维护地区公司"
							+ aggVO.getParentVO().getProjectarea() + "，请检查！");
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

			// BuildArea(项目规划数据)*def6(考核融资)
			// 开发贷(所有)
			UFDouble sumArea = UFDouble.ZERO_DBL;
			checkHvos = getCheckHVOsByAreaPk(areaOrgs);
			if (StringUtils.isNotBlank(aggVO.getParentVO().getProjecttype())
					&& checkHvos.size() > 0 && checkHvos != null) {
				UFDouble ClientMny = UFDouble.ZERO_DBL;
				// 总建筑面积
				ProjectDataPVO[] pvos = (ProjectDataPVO[]) aggVO
						.getChildren(ProjectDataPVO.class);
				if (pvos != null) {
					for (ProjectDataPVO projectDataPVO : pvos) {
						sumArea = sumArea.add(new UFDouble(projectDataPVO
								.getDef2()));
					}
				}
				if (checkHvos.size() <= 0 || checkHvos == null) {
					throw new BusinessException("考核融资金额计算标准节点未维护地区公司"
							+ aggVO.getParentVO().getProjectarea() + "，请检查！");
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
			// 暂时注释
			fvoList.add(fvo);

			aggVO.setChildren(
					ProjectDataFVO.class,
					fvoList != null && fvoList.size() > 0 ? fvoList
							.toArray(new ProjectDataFVO[0]) : null);
			// end
			// 暂时注释

			// 分期信息
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
				// 暂时注释
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
				// 暂时注释
			}

			aggVO.setChildren(ProjectDataCVO.class, cVOs);

			Object[] msgObj = new Object[util.getRemsg().getNames().length];

			msgObj[0] = null;
			msgObj[1] = vo.getAttributeValue(ProjectDataVO.NAME);
			String msg = "";
			try {
				getMaintain().syncProjectData_RequiresNew(aggVO);
				msg = (!isupdate ? "新增" : "变更") + "成功";

			} catch (Exception e) {
				msg = (!isupdate ? "新增" : "变更") + "失败:" + e.getMessage();
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

	/** 办公销售信息 */
	private List<OfficeSalesVO> getOfficeSales(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {

		List<OfficeSalesVO> bodylist = new ArrayList<OfficeSalesVO>();
		if (cList != null && cList.size() > 0) {
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					OfficeSalesVO bvo = new OfficeSalesVO();
					bvo.setDef1(salesVO.getProjectPhases());// 分期名称
					bvo.setDef2(null);// 备案名称
					bvo.setDef3(salesVO.getMarketingName());// 销售名称
					bvo.setDef4(salesVO.getOfficeSaleableSpace());// 办公可售面积
					bvo.setDef5(salesVO.getOfficeSaleableSpaceSub());// 办公已售面积
					bvo.setDef6(salesVO.getOfficeTotal());// 办公总建筑面积
					bvo.setDef7(salesVO.getOfficeSReceivable());// 办公销售回款金额
					bvo.setDef8(salesVO.getOfficeSaleableSpaceSigncon());// 办公已认购已网签
					bvo.setDef9(salesVO.getOfficeSaleableSpaceSub());// 办公已认购未网签数据
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

	/** 车位销售信息 */
	private List<ParkingSalesInformationVO> getParkingSalesInformations(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {

		List<ParkingSalesInformationVO> bodylist = new ArrayList<ParkingSalesInformationVO>();
		if (cList != null && cList.size() > 0) {
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					ParkingSalesInformationVO bvo = new ParkingSalesInformationVO();
					bvo.setDef1(salesVO.getProjectPhases());// 分期名称
					bvo.setDef2(null);// 备案名称
					// bvo.setDesalesVOnull);//销售名称
					bvo.setDef4(salesVO.getSpaceAvailableForSale());// 车位可售面积
					bvo.setDef5(salesVO.getSpaceAvailableForSaleSub());// 车位已售面积
					bvo.setDef6(salesVO.getTotalParking());// 车位总建筑面积
					bvo.setDef7(salesVO.getParkingReceivable());// 车位销售回款金额
					bvo.setDef8(salesVO.getSpaceAvailableForSaleSigncon());// 车位已认购已网签
					bvo.setDef9(salesVO.getSpaceAvailableForSaleSub());// 车位已认购未网签数据
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

	/** 公建配套销售信息 */
	private List<ConstructionSupportingSalesInformationVO> getConstructionSupportingSalesInformation(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {

		List<ConstructionSupportingSalesInformationVO> bodylist = new ArrayList<ConstructionSupportingSalesInformationVO>();
		if (cList != null && cList.size() > 0) {
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					ConstructionSupportingSalesInformationVO bvo = new ConstructionSupportingSalesInformationVO();
					bvo.setDef1(salesVO.getProjectPhases());// 分期名称
					bvo.setDef2(null);// 备案名称
					// bvo.setDesalesVOnull);//销售名称
					bvo.setDef4(salesVO.getPublicSacilitiesSaleableArea());// 公建配套可售面积
					bvo.setDef5(salesVO.getPublicSacilitiesSaleableAreaSub());// 公建配套已售面积
					bvo.setDef6(salesVO.getPublicTotaSacilities());// 公建配套总建筑面积
					bvo.setDef7(salesVO.getPublicSacilitiesReceivable());// 公建配套销售回款金额
					bvo.setDef8(salesVO.getPublicSacilitiesSaleableAreaSigncon());// 公建配套已网签
					bvo.setDef9(salesVO.getPublicSacilitiesSaleableAreaSub());// 公建配套未网签
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

	/** 商业销售信息 */
	private List<CommercialSalesInformationVO> getCommercialSalesInformation(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {

		List<CommercialSalesInformationVO> bodylist = new ArrayList<CommercialSalesInformationVO>();
		if (cList != null && cList.size() > 0) {
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					CommercialSalesInformationVO bvo = new CommercialSalesInformationVO();
					bvo.setDef1(salesVO.getProjectPhases());// 分期名称
					bvo.setDef2(null);// 备案名称
					bvo.setDef3(salesVO.getMarketingName());// 销售名称
					bvo.setDef4(salesVO.getCommercialSaleableArea());// 商业可售面积
					bvo.setDef5(salesVO.getCommercialSaleableAreaSub());// 商业已售面积
					bvo.setDef6(salesVO.getTotalCommercia());// 商业总建筑面积
					bvo.setDef7(salesVO.getCommercialReceivable());// 商业销售回款金额
					bvo.setDef8(salesVO.getCommercialSaleableAreaSigncon());// 商业已认购已网签
					bvo.setDef9(salesVO.getCommercialSaleableAreaSub());// 商业已认购未网签数据
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

	/** 住宅销售信息 */
	private List<ResidentialSalesInformationVO> getResidentialSalesInformation(
			Map<String, VSalesCapitalProjVO> capitalMap, List<ProjectDataCVO> cList,
			String pk_projectdata) {
		List<ResidentialSalesInformationVO> bodylist = new ArrayList<ResidentialSalesInformationVO>();
		if(cList != null && cList.size()>0){
			for (ProjectDataCVO cvo : cList) {
				VSalesCapitalProjVO salesVO = capitalMap.get(cvo.getPeriodizationname());
				if (salesVO != null){
					ResidentialSalesInformationVO bvo = new ResidentialSalesInformationVO();
					bvo.setDef1(salesVO.getProjectPhases());// 分期名称
					bvo.setDef2(null);// 备案名称
					bvo.setDef3(salesVO.getMarketingName());// 销售名称
					bvo.setDef4(salesVO.getResidentialSaleableArea());// 住宅可售面积
					bvo.setDef5(salesVO.getResidentialSaleableAreaSub());// 住宅已售面积
					bvo.setDef6(salesVO.getTotalHome());// 住宅总建筑面积
					bvo.setDef7(salesVO.getHomeReceivable());// 住宅销售回款金额
					bvo.setDef8(salesVO.getResidentialSaleableAreSigncon());// 住宅已认购已网签
					bvo.setDef9(salesVO.getResidentialSaleableAreaSub());// 住宅已认购未网签数据
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
																								// 大项目
																								// =
																								// '时代廊桥（佛山）'
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
			vo.setBroadHeading(prolist.get(i).get("大项目"));
			vo.setCityCompany(prolist.get(i).get("城市公司"));
			vo.setProjectPhases(prolist.get(i).get("项目分期名称"));
			vo.setEstablishName(prolist.get(i).get("报建用名"));
			vo.setMarketingName(prolist.get(i).get("营销用名"));
			Object object = prolist.get(i).get("住宅认购均价");
			if (object != null) {
				vo.setAttributeValue("averageHome", object.toString());
			}
			Object parkingSpaceAverage = prolist.get(i).get("车位认购均价");
			if (parkingSpaceAverage != null) {
				vo.setAttributeValue("parkingSpaceAverage",
						parkingSpaceAverage.toString());
			}
			Object commercialAverage = prolist.get(i).get("商业认购均价");
			if (commercialAverage != null) {
				vo.setAttributeValue("commercialAverage",
						commercialAverage.toString());
			}
			Object officeAverage = prolist.get(i).get("办公认购均价");
			if (officeAverage != null) {
				vo.setAttributeValue("officeAverage", officeAverage.toString());
			}
			Object totalHome = prolist.get(i).get("住宅总面积");
			if (totalHome != null) {
				vo.setAttributeValue("totalHome", totalHome.toString());
			}
			Object totalParking = prolist.get(i).get("车位总面积");
			if (totalParking != null) {
				vo.setAttributeValue("totalParking", totalParking.toString());
			}
			Object totalCommercia = prolist.get(i).get("商业总面积");
			if (totalCommercia != null) {
				vo.setAttributeValue("totalCommercia", totalCommercia.toString());
			}
			Object OfficeTotal = prolist.get(i).get("办公总面积");
			if (OfficeTotal != null) {
				vo.setAttributeValue("OfficeTotal", OfficeTotal.toString());
			}
			Object publicTotaSacilities = prolist.get(i).get("公建配套总面积");	
			if (publicTotaSacilities != null) {
				vo.setAttributeValue("publicTotaSacilities", publicTotaSacilities.toString());
			}
			Object residentialSaleableArea = prolist.get(i).get("住宅可售面积");
			if (residentialSaleableArea != null) {
				vo.setAttributeValue("residentialSaleableArea",
						residentialSaleableArea.toString());
			}
			Object spaceAvailableForSale = prolist.get(i).get("车位可售面积");
			if (spaceAvailableForSale != null) {
				vo.setAttributeValue("spaceAvailableForSale",
						spaceAvailableForSale.toString());
			}
			Object commercialSaleableArea = prolist.get(i).get("商业可售面积");
			if (commercialSaleableArea != null) {
				vo.setAttributeValue("commercialSaleableArea",
						commercialSaleableArea.toString());
			}
			Object officeSaleableSpace = prolist.get(i).get("办公可售面积");
			if (officeSaleableSpace != null) {
				vo.setAttributeValue("officeSaleableSpace",
						officeSaleableSpace.toString());
			}
			Object publicSacilitiesSaleableArea = prolist.get(i)
					.get("公建配套可售面积");
			if (publicSacilitiesSaleableArea != null) {
				vo.setAttributeValue("publicSacilitiesSaleableArea",
						publicSacilitiesSaleableArea.toString());
			}
			Object residentialSaleableAreaSub = prolist.get(i).get("住宅已售面积_认购");
			if (residentialSaleableAreaSub != null) {
				vo.setAttributeValue("residentialSaleableAreaSub",
						residentialSaleableAreaSub.toString());
			}
			Object spaceAvailableForSaleSub = prolist.get(i).get("车位已售面积_认购");
			if (spaceAvailableForSaleSub != null) {
				vo.setAttributeValue("spaceAvailableForSaleSub",
						spaceAvailableForSaleSub.toString());
			}
			Object commercialSaleableAreaSub = prolist.get(i).get("商业已售面积_认购");
			if (commercialSaleableAreaSub != null) {
				vo.setAttributeValue("commercialSaleableAreaSub",
						commercialSaleableAreaSub.toString());
			}
			Object officeSaleableSpaceSub = prolist.get(i).get("办公已售面积_认购");
			if (officeSaleableSpaceSub != null) {
				vo.setAttributeValue("officeSaleableSpaceSub",
						officeSaleableSpaceSub.toString());
			}
			Object publicSacilitiesSaleableAreaSub = prolist.get(i).get(
					"公建已售面积_认购");
			if (publicSacilitiesSaleableAreaSub != null) {
				vo.setAttributeValue("publicSacilitiesSaleableAreaSub",
						publicSacilitiesSaleableAreaSub.toString());
			}
			Object residentialSaleableAreSigncon = prolist.get(i).get(
					"住宅已售面积_签约");
			if (residentialSaleableAreSigncon != null) {
				vo.setAttributeValue("residentialSaleableAreSigncon",
						residentialSaleableAreSigncon.toString());
			}
			Object spaceAvailableForSaleSigncon = prolist.get(i).get(
					"车位已售面积_签约");
			if (spaceAvailableForSaleSigncon != null) {
				vo.setAttributeValue("spaceAvailableForSaleSigncon",
						spaceAvailableForSaleSigncon.toString());
			}
			Object commercialSaleableAreaSigncon = prolist.get(i).get(
					"商业已售面积_签约");
			if (commercialSaleableAreaSigncon != null) {
				vo.setAttributeValue("commercialSaleableAreaSigncon",
						commercialSaleableAreaSigncon.toString());
			}
			Object officeSaleableSpaceSigncon = prolist.get(i).get("办公已售面积_签约");
			if (officeSaleableSpaceSigncon != null) {
				vo.setAttributeValue("officeSaleableSpaceSigncon",
						officeSaleableSpaceSigncon.toString());
			}
			Object publicSacilitiesSaleableAreaSigncon = prolist.get(i).get(
					"公建已售面积_签约");
			if (publicSacilitiesSaleableAreaSigncon != null) {
				vo.setAttributeValue("publicSacilitiesSaleableAreaSigncon",
						publicSacilitiesSaleableAreaSigncon.toString());
			}
			Object projectTotalArea = prolist.get(i).get("项目总建面积");
			if (projectTotalArea != null) {
				vo.setAttributeValue("projectTotalArea",
						projectTotalArea.toString());
			}
			Object AmountSalesProceeds = prolist.get(i).get("销售回款金额");
			if (AmountSalesProceeds != null) {
				vo.setAttributeValue("AmountSalesProceeds",
						AmountSalesProceeds.toString());
			}
			Object homeReceivable = prolist.get(i).get("住宅回款金额");
			if (homeReceivable != null) {
				vo.setAttributeValue("homeReceivable",
						homeReceivable.toString());
			}
			Object parkingReceivable = prolist.get(i).get("车位回款金额");
			if (parkingReceivable != null) {
				vo.setAttributeValue("parkingReceivable",
						parkingReceivable.toString());
			}
			Object commercialReceivable = prolist.get(i).get("商业回款金额");
			if (commercialReceivable != null) {
				vo.setAttributeValue("commercialReceivable",
						commercialReceivable.toString());
			}
			Object officeSReceivable = prolist.get(i).get("办公回款金额");
			if (officeSReceivable != null) {
				vo.setAttributeValue("officeSReceivable",
						officeSReceivable.toString());
			}
			Object publicSacilitiesReceivable = prolist.get(i).get("公建配套回款金额");
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
	 * 根据项目类型pk和区域公司pk查询所有符合的考核融资计算标准的单据
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
	 * 根据区域公司查询所有符合的考核融资计算标准的单据
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
		// 由于主数据运行会有分页查询,为确保数据完整,故此处使用循环获取分页,一旦获取成功并且返回的data为空则跳出循环
		for (int i = 1; i < 99999; i++) {
			// String urls = util.readValue("MIANURL")
			// +"&currpage="+i+""
			// +"&syscode="+util.readValue("SYSCODE")
			// +"&token="+initElementInv();
			String urls = urlstr + "&currpage=" + i + "";
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
			// // 转换为字节数组
			// byte[] data = json.getBytes("utf-8");
			// 设置文件长度
			conn.setRequestProperty("Content-Length",
					String.valueOf(urls.length()));

			// 设置文件类型:
			conn.setRequestProperty("Content-Type", "application/json");

			// 开始连接请求
			conn.connect();
			String a = "";

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
				String flag = (String) result.getString("code");
				String errmsg = null;
				if (flag.equals("S")) {
					if (result.getJSONArray("data") == null
							|| result.getJSONArray("data").length() <= 0) {
						logVO.setSrcparm("第" + i + "条：" + result.toString());
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
						pvo.setAttributeValue("srcid", map.get("FNUMBER"));// 项目id
						if ("null"
								.equals(map.get("M_FFACTPLOTRATE") != null ? map
										.get("M_FFACTPLOTRATE").toString()
										: "null")
								|| map.get("M_FFACTPLOTRATE") == null
								|| "".equals(map.get("M_FFACTPLOTRATE"))) {
							pvo.setAttributeValue("def1", 0);// 规划用地面积
						} else {
							pvo.setAttributeValue("def1",
									map.get("M_FFACTPLOTRATE") == null ? 0
											: map.get("M_FFACTPLOTRATE"));// 规划用地面积
						}
						if ("null"
								.equals(map.get("FBUILDINGAREA") != null ? map
										.get("FBUILDINGAREA").toString()
										: "null")
								|| map.get("FBUILDINGAREA") == null
								|| "".equals(map.get("FBUILDINGAREA"))) {
							pvo.setAttributeValue("def2", 0);// 总建筑面积
						} else {
							pvo.setAttributeValue(
									"def2",
									map.get("FBUILDINGAREA") == null ? 0 : map
											.get("FBUILDINGAREA"));// 总建筑面积
						}
						if ("null"
								.equals(map.get("FFACTPLOTAREA") != null ? map
										.get("FFACTPLOTAREA").toString()
										: "null")
								|| map.get("FFACTPLOTAREA") == null
								|| "".equals(map.get("FFACTPLOTAREA"))) {
							pvo.setAttributeValue("def3", 0);// 计容面积
						} else {
							pvo.setAttributeValue(
									"def3",
									map.get("FFACTPLOTAREA") == null ? 0 : map
											.get("FFACTPLOTAREA"));// 计容面积
						}
						if ("null"
								.equals(map.get("FFACTNONPLOTAREA") != null ? map
										.get("FFACTNONPLOTAREA").toString()
										: "null")
								|| map.get("FFACTNONPLOTAREA") == null
								|| "".equals(map.get("FFACTNONPLOTAREA"))) {
							pvo.setAttributeValue("def4", 0);// 不计容面积
						} else {
							pvo.setAttributeValue("def4",
									map.get("FFACTNONPLOTAREA") == null ? 0
											: map.get("FFACTNONPLOTAREA"));// 不计容面积
						}
						if ("null"
								.equals(map.get("FSALEABLEAREA") != null ? map
										.get("FSALEABLEAREA").toString()
										: "null")
								|| map.get("FSALEABLEAREA") == null
								|| "".equals(map.get("FSALEABLEAREA"))) {
							pvo.setAttributeValue("def5", 0);// 可售建筑面积
						} else {
							pvo.setAttributeValue(
									"def5",
									map.get("FSALEABLEAREA") == null ? 0 : map
											.get("FSALEABLEAREA"));// 可售建筑面积
						}
						if ("null"
								.equals(map.get("FNONSALEABLEAREA") != null ? map
										.get("FNONSALEABLEAREA").toString()
										: "null")
								|| map.get("FNONSALEABLEAREA") == null
								|| "".equals(map.get("FNONSALEABLEAREA"))) {
							pvo.setAttributeValue("def6", 0);// 不可售建筑面积
						} else {
							pvo.setAttributeValue("def6",
									map.get("FNONSALEABLEAREA") == null ? 0
											: map.get("FNONSALEABLEAREA"));// 不可售建筑面积
						}

						pvo.setAttributeValue("dr", new Integer(0));
						pvo.setStatus(VOStatus.NEW);
						bodylists.get((String) map.get("FNUMBER")).add(pvo);
					}
				} else {
					errmsg = (String) result.getString("msg");
					throw new BusinessException("【来自主数据的错误信息：" + errmsg + "】");
				}
			} else {
				Logger.error("连接失败");
				throw new BusinessException("连接失败");
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
	 * 初始化token信息
	 * 
	 * @param servername
	 * @return
	 */
	private String initToken(String key) {

		Date date = new UFDate().toDate();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
		String time = formater.format(date);
		String ticket = time + key;
		String ticketMD5 = DigestUtils.md5Hex(ticket).toUpperCase();// 32位大写
		// String ticketMD5 = MD5Util.getMD5(ticket).toUpperCase();
		return ticketMD5;
	}

	/**
	 * 读取面积视图
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
				pvo.setAttributeValue("srcid", map.get("projectid"));// 项目id
				pvo.setAttributeValue("def1", map.get("ghydarea"));// 规划用地面积
				pvo.setAttributeValue("def2", map.get("buildarea"));// 总建筑面积
				pvo.setAttributeValue("def3", map.get("jrbuildarea"));// 计容面积
				pvo.setAttributeValue("def4", map.get("bjrbuildarea"));// 不计容面积
				pvo.setAttributeValue("def5", map.get("cansalearea"));// 可售建筑面积
				pvo.setAttributeValue("def6", map.get("uncansalearea"));// 不可售建筑面积

				pvo.setAttributeValue("dr", new Integer(0));
				pvo.setStatus(VOStatus.NEW);
				bodylists.get((String) map.get("projectid")).add(pvo);
			}

		}
		return bodylists;
	}

	/**
	 * 转换分期信息
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
	 * 读取已生成的项目资料
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
	 * 读取分期信息
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
						map.get("projectstaging"));// 分期名称
				cvo.setAttributeValue(ProjectDataCVO.DEF1,
						map.get("projectstagingid"));// 分期主键
				// P6-INFO
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE1, map
						.get("p6_datadate1") == null ? null : new UFDate(map
						.get("p6_datadate1").toString()));// 项目时间
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE2, map
						.get("p6_datadate2") == null ? null : new UFDate(map
						.get("p6_datadate2").toString()));// 运营启动点达到_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE3, map
						.get("p6_datadate3") == null ? null : new UFDate(map
						.get("p6_datadate3").toString()));// 修详规批复
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE4, map
						.get("p6_datadate4") == null ? null : new UFDate(map
						.get("p6_datadate4").toString()));// 国有土地使用证
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE5, map
						.get("p6_datadate5") == null ? null : new UFDate(map
						.get("p6_datadate5").toString()));// 用地规划许可证
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE6, map
						.get("p6_datadate6") == null ? null : new UFDate(map
						.get("p6_datadate6").toString()));// 建设工程规划许可证
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE7, map
						.get("p6_datadate7") == null ? null : new UFDate(map
						.get("p6_datadate7").toString()));// 施工许可证
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE8, map
						.get("p6_datadate8") == null ? null : new UFDate(map
						.get("p6_datadate8").toString()));// 开工时间
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE9, map
						.get("p6_datadate9") == null ? null : new UFDate(map
						.get("p6_datadate9").toString()));// 正负零
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE10, map
						.get("p6_datadate10") == null ? null : new UFDate(map
						.get("p6_datadate10").toString()));// 预售证_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE11, map
						.get("p6_datadate11") == null ? null : new UFDate(map
						.get("p6_datadate11").toString()));// 结构封顶_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE12, map
						.get("p6_datadate12") == null ? null : new UFDate(map
						.get("p6_datadate12").toString()));// 竣工备案_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE13, map
						.get("p6_datadate13") == null ? null : new UFDate(map
						.get("p6_datadate13").toString()));// 交付_p6
				cvo.setAttributeValue(ProjectDataCVO.P6_DATADATE14, map
						.get("p6_datadate14") == null ? null : new UFDate(map
						.get("p6_datadate14").toString()));// 确权_nc

				// NC-INFO
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE1, map
						.get("p6_datadate1") == null ? null : new UFDate(map
						.get("p6_datadate1").toString()));// 项目时间
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE2, map
						.get("p6_datadate2") == null ? null : new UFDate(map
						.get("p6_datadate2").toString()));// 运营启动点达到_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE3, map
						.get("p6_datadate3") == null ? null : new UFDate(map
						.get("p6_datadate3").toString()));// 修详规批复
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE4, map
						.get("p6_datadate4") == null ? null : new UFDate(map
						.get("p6_datadate4").toString()));// 国有土地使用证
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE5, map
						.get("p6_datadate5") == null ? null : new UFDate(map
						.get("p6_datadate5").toString()));// 用地规划许可证
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE6, map
						.get("p6_datadate6") == null ? null : new UFDate(map
						.get("p6_datadate6").toString()));// 建设工程规划许可证
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE7, map
						.get("p6_datadate7") == null ? null : new UFDate(map
						.get("p6_datadate7").toString()));// 施工许可证
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE8, map
						.get("p6_datadate8") == null ? null : new UFDate(map
						.get("p6_datadate8").toString()));// 开工时间
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE9, map
						.get("p6_datadate9") == null ? null : new UFDate(map
						.get("p6_datadate9").toString()));// 正负零
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE10, map
						.get("p6_datadate10") == null ? null : new UFDate(map
						.get("p6_datadate10").toString()));// 预售证_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE11, map
						.get("p6_datadate11") == null ? null : new UFDate(map
						.get("p6_datadate11").toString()));// 结构封顶_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE12, map
						.get("p6_datadate12") == null ? null : new UFDate(map
						.get("p6_datadate12").toString()));// 竣工备案_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE13, map
						.get("p6_datadate13") == null ? null : new UFDate(map
						.get("p6_datadate13").toString()));// 交付_p6
				cvo.setAttributeValue(ProjectDataCVO.NC_DATADATE14, map
						.get("p6_datadate14") == null ? null : new UFDate(map
						.get("p6_datadate14").toString()));// 确权_nc

				cvo.setAttributeValue("dr", new Integer(0));
				cvo.setStatus(VOStatus.NEW);
				bodylists.get((String) map.get("projectid")).add(cvo);
			}

		}
		return bodylists;
	}

	/**
	 * 读取地价
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, List<ProjectDataBVO>> getProjectDataBVOs()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select 项目名称  projectname ")
				// 项目名称
				.append(",付款金额 nmny ")
				// 金额
				.append(",to_char(cj_bi.付款日期,'yyyy-mm-dd') paydate ")
				// 地价/股权支付时间日期
				.append(",case when cj_bi.是否支付到共管户='是' then 'Y' else 'N' end  def1 ")// 是否支付到共管户
				.append(" from cj_bi@NC_TO_BI_LINK ");
		//注意正式环境不需要加上database links： @NC_TO_BI_LINK 
		
		// sql.append("select   projectname ")// 项目名称
		// .append(", nmny ")// 金额
		// .append(",paydate ")// 地价/股权支付时间日期
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
				bvo.setAttributeValue(ProjectDataBVO.DEF1, map.get("def1"));// 是否支付到共管户
				bvo.setAttributeValue("dr", new Integer(0));
				bvo.setStatus(VOStatus.NEW);
				bodylists.get((String) map.get("projectname")).add(bvo);

			}

		}
		return bodylists;
	}

	/**
	 * 读取主体信息
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private ProjectDataVO[] getProjectDataVOs() throws BusinessException {
		String sql = "select * from V_TGRZ_PROJECT  ";// where
																								// V_TGRZ_PROJECT.project
																								// =
																								// '时代廊桥（佛山）'
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
						.get("project"));// 名称
				vos[i$].setAttributeValue(ProjectDataVO.PROJECTAREA, prolist
						.get(i$).get("tfcompany"));// 项目所属区域
				vos[i$].setAttributeValue(ProjectDataVO.PROJECTCORP, prolist
						.get(i$).get("projectcompany"));// 项目所属 公司
				vos[i$].setAttributeValue(ProjectDataVO.SRCID, prolist.get(i$)
						.get("projectid"));// 来源项目主键
				vos[i$].setAttributeValue(ProjectDataVO.PROJECTTYPE, prolist
						.get(i$).get("tztype"));// 项目类型
				// P6-INFO
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE1, prolist
						.get(i$).get("p6_datadate1") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate1")));// 项目时间
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE2, prolist
						.get(i$).get("p6_datadate2") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate2")));// 运营启动点达到_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE3, prolist
						.get(i$).get("p6_datadate3") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate3")));// 修详规批复
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE4, prolist
						.get(i$).get("p6_datadate4") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate4")));// 国有土地使用证
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE5, prolist
						.get(i$).get("p6_datadate5") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate5")));// 用地规划许可证
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE6, prolist
						.get(i$).get("p6_datadate6") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate6")));// 建设工程规划许可证
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE7, prolist
						.get(i$).get("p6_datadate7") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate7")));// 施工许可证
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE8, prolist
						.get(i$).get("p6_datadate8") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate8")));// 开工时间
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE9, prolist
						.get(i$).get("p6_datadate9") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate9")));// 正负零
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE10, prolist
						.get(i$).get("p6_datadate10") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate10")));// 预售证_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE11, prolist
						.get(i$).get("p6_datadate11") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate11")));// 结构封顶_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE12, prolist
						.get(i$).get("p6_datadate12") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate12")));// 竣工备案_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE13, prolist
						.get(i$).get("p6_datadate13") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate13")));// 交付_p6
				vos[i$].setAttributeValue(ProjectDataVO.P6_DATADATE14, prolist
						.get(i$).get("p6_datadate14") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate14")));// 确权_p6

				// NC-INFO
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE1, prolist
						.get(i$).get("p6_datadate1") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate1")));// 项目时间
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE2, prolist
						.get(i$).get("p6_datadate2") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate2")));// 运营启动点达到_p6
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE3, prolist
						.get(i$).get("p6_datadate3") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate3")));// 修详规批复
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE4, prolist
						.get(i$).get("p6_datadate4") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate4")));// 国有土地使用证
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE5, prolist
						.get(i$).get("p6_datadate5") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate5")));// 用地规划许可证
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE6, prolist
						.get(i$).get("p6_datadate6") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate6")));// 建设工程规划许可证
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE7, prolist
						.get(i$).get("p6_datadate7") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate7")));// 施工许可证
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE8, prolist
						.get(i$).get("p6_datadate8") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate8")));// 开工时间
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE9, prolist
						.get(i$).get("p6_datadate9") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate9")));// 正负零
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE10, prolist
						.get(i$).get("p6_datadate10") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate10")));// 预售证
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE11, prolist
						.get(i$).get("p6_datadate11") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate11")));// 结构封顶
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE12, prolist
						.get(i$).get("p6_datadate12") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate12")));// 竣工备案
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE13, prolist
						.get(i$).get("p6_datadate13") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate13")));// 交付
				vos[i$].setAttributeValue(ProjectDataVO.NC_DATADATE14, prolist
						.get(i$).get("p6_datadate14") == null ? null
						: new UFDate(prolist.get(i$).get("p6_datadate14")));// 确权

				vos[i$].setAttributeValue(ProjectDataVO.SRCSYSTEM, "DATADB");//
			}

		}

		return vos;
	}

	/**
	 * 用于信息替换的字段信息
	 * 
	 * @return
	 */
	public List<String> getCopylist() {
		if (copylist == null) {
			copylist = new ArrayList<String>();
			copylist.add(ProjectDataVO.NAME);// 名称
			copylist.add(ProjectDataVO.SRCID);// 来源项目
			copylist.add(ProjectDataVO.PROJECTCORP);// 项目所属公司
			copylist.add(ProjectDataVO.PROJECTAREA);// 项目所属区域
			copylist.add(ProjectDataVO.PROJECTTYPE);// 项目类型
			copylist.add(ProjectDataVO.P6_DATADATE1);// 项目获取时间_p6
			copylist.add(ProjectDataVO.P6_DATADATE2);// 运营启动点达到_p6
			copylist.add(ProjectDataVO.P6_DATADATE3);// 修详规批复_p6
			copylist.add(ProjectDataVO.P6_DATADATE4);// 国有土地使用证_p6
			copylist.add(ProjectDataVO.P6_DATADATE5);// 用地规划许可证_p6
			copylist.add(ProjectDataVO.P6_DATADATE6);// 建设工程规划许可证_p6
			copylist.add(ProjectDataVO.P6_DATADATE7);// 施工许可证_p6
			copylist.add(ProjectDataVO.P6_DATADATE8);// 开工时间_p6
			copylist.add(ProjectDataVO.P6_DATADATE9);// 正负零_p6
			copylist.add(ProjectDataVO.P6_DATADATE10);// 预售证_p6
			copylist.add(ProjectDataVO.P6_DATADATE11);// 结构封顶_p6
			copylist.add(ProjectDataVO.P6_DATADATE12);// 竣工备案_p6
			copylist.add(ProjectDataVO.P6_DATADATE13);// 交付_p6
			copylist.add(ProjectDataVO.P6_DATADATE14);// 确权_p6

			// copylist.add(ProjectDataVO.DEF1);//自定义项1
			// copylist.add(ProjectDataVO.DEF2);//自定义项2
			// copylist.add(ProjectDataVO.DEF3);//自定义项3
			// copylist.add(ProjectDataVO.DEF4);//自定义项4
			// copylist.add(ProjectDataVO.DEF5);//自定义项5
			// copylist.add(ProjectDataVO.DEF6);//自定义项6
			// copylist.add(ProjectDataVO.DEF7);//自定义项7
			// copylist.add(ProjectDataVO.DEF8);//自定义项8
			// copylist.add(ProjectDataVO.DEF9);//自定义项9
			// copylist.add(ProjectDataVO.DEF10);//自定义项10
			// copylist.add(ProjectDataVO.DEF11);//自定义项11
			// copylist.add(ProjectDataVO.DEF12);//自定义项12
			// copylist.add(ProjectDataVO.DEF13);//自定义项13
			// copylist.add(ProjectDataVO.DEF14);//自定义项14
			// copylist.add(ProjectDataVO.DEF15);//自定义项15
			// copylist.add(ProjectDataVO.DEF16);//自定义项16
			// copylist.add(ProjectDataVO.DEF17);//自定义项17
			// copylist.add(ProjectDataVO.DEF18);//自定义项18
			// copylist.add(ProjectDataVO.DEF19);//自定义项19
			// copylist.add(ProjectDataVO.DEF20);//自定义项20
			// copylist.add(ProjectDataVO.DEF21);//自定义项21
			// copylist.add(ProjectDataVO.DEF22);//自定义项22
			// copylist.add(ProjectDataVO.DEF23);//自定义项23
			// copylist.add(ProjectDataVO.DEF24);//自定义项24
			// copylist.add(ProjectDataVO.DEF25);//自定义项25
			// copylist.add(ProjectDataVO.DEF26);//自定义项26
			// copylist.add(ProjectDataVO.DEF27);//自定义项27
			// copylist.add(ProjectDataVO.DEF28);//自定义项28
			// copylist.add(ProjectDataVO.DEF29);//自定义项29
			// copylist.add(ProjectDataVO.DEF30);//自定义项30
		}
		return copylist;
	}

	/**
	 * 用于信息替换的字段信息
	 * 
	 * @return
	 */
	public List<String> getCopylist_C() {
		if (copylist_c == null) {
			copylist_c = new ArrayList<String>();
			copylist_c.add(ProjectDataCVO.PERIODIZATIONNAME);// 名称
			copylist_c.add(ProjectDataCVO.P6_DATADATE1);// 项目获取时间_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE2);// 运营启动点达到_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE3);// 修详规批复_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE4);// 国有土地使用证_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE5);// 用地规划许可证_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE6);// 建设工程规划许可证_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE7);// 施工许可证_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE8);// 开工时间_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE9);// 正负零_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE10);// 预售证_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE11);// 结构封顶_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE12);// 竣工备案_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE13);// 交付_p6
			copylist_c.add(ProjectDataCVO.P6_DATADATE14);// 确权_p6

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
	 * 推送通知信息给NC用户
	 * 
	 * @throws Exception
	 * */
	public void pushNCUser(String user, String msg) throws Exception {
		StringBuffer contexts = new StringBuffer();// 盖章流程+单据编号:SD012020081700000070+标题:测试附件
													// +需上传盖章文件
		contexts.append(msg);
		List<NCMessage> ncMessageList = new ArrayList<NCMessage>();
		NCMessage ncMessage = new NCMessage();
		MessageVO messageVO = new MessageVO();
		messageVO.setBillid(null);// 单据ID
		messageVO.setBilltype(null);// 单据类型
		messageVO.setContent(contexts.toString());// 内容
		messageVO.setContenttype("text/plain");// 内容格式
		messageVO.setDestination("outbox");// 存放位置
		messageVO.setDomainflag("CDM");// 领域标记
		messageVO.setDr(0);//
		messageVO.setExpiration(null);// 有效期
		// messageVO.setISACTIVED 1
		messageVO.setIsdelete(UFBoolean.FALSE);// 是否被删除
		messageVO.setIshandled(UFBoolean.FALSE);// 是否已处理
		messageVO.setIsread(UFBoolean.FALSE);// 是否已读
		messageVO.setMsgsendpk(null);// 发送消息的主键
		messageVO.setMsgsourcetype("worklist");// 左边已收到消息： 预警：prealert
												// 通知消息：commitnotice
												// 工作任务：worklist
		messageVO.setMsgtype("nc");// 消息发送类型
		messageVO.setPk_colorkey(null);
		messageVO.setPk_group(AppContext.getInstance().getPkGroup());// 集团
		messageVO.setPk_message(null);// 主键
		messageVO.setPk_org(null);// 组织
		messageVO.setPriority(5);// 优先级
		messageVO.setReceipt(null);// 回执
		messageVO.setReceiver(user);// "vo.getUser());// 接收人 "100112100000003IR5K1"hz
		messageVO.setResendtimes(0);// 重发次数
		messageVO.setSender("100112100000003G9D0D");// 发送人 NC_USER0000000000000
		messageVO.setSendstate(UFBoolean.TRUE);// 是否发送成功
		messageVO.setSendtime(new UFDateTime());// 发送时间
		messageVO.setSubcolor("~");// 标题颜色
		messageVO.setSubject(contexts.toString());// 标题
		messageVO.setTs(null);// 时间戳
		ncMessage.setMessage(messageVO);
		ncMessageList.add(ncMessage);
		MessageCenter.sendMessage(ncMessageList.toArray(new NCMessage[0]));
	}

}
