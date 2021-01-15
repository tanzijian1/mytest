package nc.impl.tg.outside;

import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.pub.ace.IsqlThreadimpl;
import nc.impl.pubapp.pattern.data.vo.VOInsert;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.IProjectDocSyn;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.org.cache.IOrgUnitPubService_C;
import nc.vo.org.OrgVO;
import nc.vo.pmpub.common.utils.PMProxy;
import nc.vo.pmpub.project.ProjectBodyVO;
import nc.vo.pmpub.project.ProjectHeadVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tg.projectbatchlog.AggProjectBatchLogVO;
import nc.vo.tg.projectbatchlog.ProjectBatchLogDetailVO;
import nc.vo.tg.projectbatchlog.ProjectBatchLogVO;

public class SynProjectDocRunnable implements Runnable {

	private BaseDAO dao = new BaseDAO();
	private AggProjectBatchLogVO aggProjectBatchLogVO;

	public SynProjectDocRunnable(AggProjectBatchLogVO aggProjectBatchLogVO) {
		this.aggProjectBatchLogVO = aggProjectBatchLogVO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		ISqlThread save=NCLocator.getInstance().lookup(ISqlThread.class);
		OutsideLogVO logvo=new OutsideLogVO();
		logvo.setDesbill("房间号");
		logvo.setSrcsystem("SALE");
		StringBuffer sb=new StringBuffer();
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(
					IProjectDocSyn.NC_DataSourceName);
			InvocationInfoProxy.getInstance().setUserId(
					IProjectDocSyn.SaleUserId);
			InvocationInfoProxy.getInstance().setUserCode(
					IProjectDocSyn.SaleOperatorName);
			InvocationInfoProxy.getInstance()
					.setGroupId(IProjectDocSyn.GroupId);
			ProjectBatchLogDetailVO[] projectBatchLogDetailVOs = (ProjectBatchLogDetailVO[]) aggProjectBatchLogVO
					.getChildren(ProjectBatchLogDetailVO.class);
			
			if ((projectBatchLogDetailVOs != null)
					&& (projectBatchLogDetailVOs.length > 0)) {
				ProjectBatchLogVO projectBatchLogVO = aggProjectBatchLogVO
						.getParentVO();
				String[] epscodestrs = ((String) projectBatchLogVO
						.getAttributeValue("epscode")).split("-");
				StringBuffer epscode = new StringBuffer();
				epscode.append("04");
				for (String str : epscodestrs) {
					epscode.append(str);
				}
				String pk_eps = (String) getDao().executeQuery(
						"select pk_eps from pm_eps where eps_code='" + epscode
								+ "'", new ColumnProcessor());
				if (pk_eps == null || pk_eps == "") {
					writeLog(2, "无法在nc找到对应的EPS");
					return;
				}
				Logger.info("~~~ProjectDocSyn~~~，pk_eps=" + pk_eps
						+ "，项目档案房间内码初始处理：开始读取项目档案");
				String sql = "select * from bd_project where  nvl(dr,0) = 0 and pk_projectclass='10011210000000006P3R' ";
//						+ IProjectDocSyn.GroupId
//						+ "' and pk_projectclass = '"
//						+ IProjectDocSyn.ProjectClass04
//						+ "'";
				String checksql = "select project_code from bd_project where (deletestate is null or deletestate<>1) and nvl(dr,0) = 0 and pk_org = '"
						+ IProjectDocSyn.GroupId
						+ "' and pk_projectclass = '"
						+ IProjectDocSyn.ProjectClass04
						+ "'";
//				List<ProjectHeadVO> projectHeadVOs = (List<ProjectHeadVO>) getDao()
//						.executeQuery(sql,
//								new BeanListProcessor(ProjectHeadVO.class));
//				List<String> project_codelist = (List<String>) getDao()
//						.executeQuery(checksql,
//								new ColumnListProcessor());
//				if ((projectHeadVOs == null) || (projectHeadVOs.size() == 0)) {
//					Logger.info("~~~ProjectDocSyn~~~，pk_eps=" + pk_eps
//							+ "，项目档案房间内码初始处理：读取项目档案:0");
//				} else {
//					Logger.info("~~~ProjectDocSyn~~~，pk_eps=" + pk_eps
//							+ "，项目档案房间内码初始处理：读取项目档案:" + projectHeadVOs.size());
//				}
				VOUpdate<ProjectHeadVO> voProjectUpdate = new VOUpdate<ProjectHeadVO>();
				VOInsert<ProjectHeadVO> voProjectInsert = new VOInsert<ProjectHeadVO>();
				for (ProjectBatchLogDetailVO projectBatchLogDetailVO : projectBatchLogDetailVOs) {
					
					String errMessage = validProjectDetail(projectBatchLogDetailVO);
					if (PubAppTool.isNull(errMessage)) {
						String def2 = (String) projectBatchLogDetailVO
								.getAttributeValue("def2")==null ?"*":(String) projectBatchLogDetailVO
										.getAttributeValue("def2");
						String project_code = (String) projectBatchLogDetailVO
								.getAttributeValue("project_code");
						boolean found = false;
						try{
						List<String> strlist=(List<String>)getDao().executeQuery("select pk_project from bd_project where PROJECT_CODE='"+project_code+"' or def2='"+def2+"' and nvl(dr,0)=0", new ColumnListProcessor());
                         if(strlist!=null&& strlist.size()>0){
                        	 for(String str:strlist){
                        		 sb.append(projectBatchLogDetailVO.getAttributeValue("def2")+"更新操作");
                        		 ProjectHeadVO projectHeadVO=(ProjectHeadVO)  getDao().executeQuery("select * from bd_project where pk_project='"+str+"' ", new BeanProcessor(ProjectHeadVO.class));
									
									onTranBill(pk_eps, projectBatchLogDetailVO,
											projectHeadVO, false);
									voProjectUpdate
											.update(new ProjectHeadVO[] { projectHeadVO });
									assignOrg(projectBatchLogDetailVO,
											projectHeadVO);
									projectBatchLogDetailVO.setAttributeValue(
											"detailstatus", 1);
									projectBatchLogDetailVO.setAttributeValue("message",
											null);
									found = true;
                        	 }
                        	 }
						}catch(Exception e){
							sb.append(e.getMessage());
						}
						//						if ((projectHeadVOs != null)
//								&& (projectHeadVOs.size() > 0)) {
//							for (ProjectHeadVO projectHeadVO : projectHeadVOs) {
//								boolean flag=false;
//								if(projectHeadVO.getProject_code()!=null){
//									flag=projectHeadVO.getProject_code().equals(project_code);
//								}
//								
//								
//								if (flag||def2.equals(projectHeadVO.getDef2())) {
//									
//									onTranBill(pk_eps, projectBatchLogDetailVO,
//											projectHeadVO, false);
//									voProjectUpdate
//											.update(new ProjectHeadVO[] { projectHeadVO });
//									assignOrg(projectBatchLogDetailVO,
//											projectHeadVO);
//									projectBatchLogDetailVO.setAttributeValue(
//											"detailstatus", 1);
//									projectBatchLogDetailVO.setAttributeValue("message",
//											null);
//									found = true;
//									break;
//								}
//							}
//						}
						if (!found) {
							ProjectHeadVO projectHeadVO = new ProjectHeadVO();
							onTranBill(pk_eps, projectBatchLogDetailVO,
									projectHeadVO, true);
							voProjectInsert
									.insert(new ProjectHeadVO[] { projectHeadVO });
							assignOrg(projectBatchLogDetailVO, projectHeadVO);
							projectBatchLogDetailVO.setAttributeValue(
									"detailstatus", 1);
							projectBatchLogDetailVO.setAttributeValue("message",
									null);
						}
					} else {
						projectBatchLogDetailVO.setAttributeValue(
								"detailstatus", 2);
						projectBatchLogDetailVO.setAttributeValue("message",
								errMessage);
					}
				}

				writeLog(1, null);

			}
			Logger.info("~~~ProjectDocSyn~~~，项目档案房间内码初始处理完成。");
		} catch (Exception e) {
			
			sb.append(e.getMessage());
			Logger.error("~~~ProjectDocSyn~~~，" + e.getMessage(), e);
		}finally{
			
			try {
				logvo.setErrmsg(sb.toString());
				save.billInsert_RequiresNew(logvo);
			} catch (BusinessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	private String validProjectDetail(
			ProjectBatchLogDetailVO projectBatchLogDetailVO) {
		String project_code = (String) projectBatchLogDetailVO
				.getAttributeValue("project_code");
		String project_name = (String) projectBatchLogDetailVO
				.getAttributeValue("project_name");
		String def2 = (String) projectBatchLogDetailVO
				.getAttributeValue("def2");
		if (PubAppTool.isNull(project_code)) {
			return "project_code不允许为空";
		}
		if (PubAppTool.isNull(def2)) {
			return "def2不允许为空";
		}
		if (PubAppTool.isNull(project_name)) {
			return "project_name不允许为空";
		}
		return null;
	}

	private void onTranBill(String pk_eps,
			ProjectBatchLogDetailVO projectBatchLogDetailVO,
			ProjectHeadVO projectHeadVO, boolean isNew)
			throws BusinessException {
		projectHeadVO.setAttributeValue("project_name",
				projectBatchLogDetailVO.getAttributeValue("project_name"));
		projectHeadVO.setAttributeValue("project_code",
				projectBatchLogDetailVO.getAttributeValue("project_code"));
		projectHeadVO.setAttributeValue("pk_eps", pk_eps);
		projectHeadVO.setAttributeValue("def2", projectBatchLogDetailVO.getAttributeValue("def2"));
		if (isNew) {
			projectHeadVO.setAttributeValue("pk_group", IProjectDocSyn.GroupId);
			projectHeadVO.setAttributeValue("pk_org", IProjectDocSyn.GroupId);
			projectHeadVO.setAttributeValue("creator",
					IProjectDocSyn.SaleUserId);
			projectHeadVO.setAttributeValue("creationtime", new UFDateTime());
			projectHeadVO.setAttributeValue("enablestate", 2);
			projectHeadVO.setAttributeValue("def2",
					projectBatchLogDetailVO.getAttributeValue("def2"));
			projectHeadVO.setAttributeValue("pk_projectclass",
					IProjectDocSyn.ProjectClass04);
			projectHeadVO.setAttributeValue("bill_type", "4D10");
			projectHeadVO.setAttributeValue("transi_type", "4D10-01");
		}
	}

	@SuppressWarnings("unchecked")
	private void assignOrg(ProjectBatchLogDetailVO projectBatchLogDetailVO,
			ProjectHeadVO projectHeadVO) {
		try {
			String orgcode = (String) projectBatchLogDetailVO
					.getAttributeValue("orgcode");
			if (orgcode == null || orgcode.length() == 0) {
				return;
			}
			// 按编码找业务单元
			String sql = "select * from org_orgs where pk_group = '"
					+ IProjectDocSyn.GroupId
					+ "' and isbusinessunit = 'Y' and code = '" + orgcode + "'";
			BaseDAO dao = getDao();
			List<OrgVO> orgvos = (List<OrgVO>) dao.executeQuery(sql,
					new BeanListProcessor(OrgVO.class));
			if ((orgvos == null) || (orgvos.size() == 0)) {
				return;
			}

			// 判断是否已分配
			OrgVO orgvo = orgvos.get(0);
			sql = "select * from bd_project_b where pk_group = '"
					+ IProjectDocSyn.GroupId + "' and pk_parti_org = '"
					+ orgvo.getPk_org() + "' and pk_project='"
					+ projectHeadVO.getPk_project() + "' and nvl(dr,0) = 0";
			List<ProjectBodyVO> projectBodyvos = (List<ProjectBodyVO>) dao
					.executeQuery(sql, new BeanListProcessor(
							ProjectBodyVO.class));
			VOInsert<ProjectBodyVO> voProjectBodyInsert = new VOInsert<ProjectBodyVO>();
			if ((projectBodyvos == null) || (projectBodyvos.size() == 0)) {
				// 得到组织对应组织版本的Map
				HashMap<String, String> org_orgvMap = PMProxy.lookup(
						IOrgUnitPubService_C.class).getNewVIDSByOrgIDS(
						new String[] { orgvo.getPk_org() });
				// 未分配的分配
				ProjectBodyVO newbodyvo = new ProjectBodyVO();
				newbodyvo.setPk_group(projectHeadVO.getPk_group());
				newbodyvo.setPk_org(projectHeadVO.getPk_org());
				newbodyvo.setPk_org_v(projectHeadVO.getPk_org_v());
				newbodyvo.setPk_parti_org(orgvo.getPk_org());
				newbodyvo.setPk_parti_org_v(org_orgvMap.get(orgvo.getPk_org()));
				newbodyvo.setAttributeValue("pk_project",
						projectHeadVO.getPk_project());
				newbodyvo.setStatus(VOStatus.NEW);
				voProjectBodyInsert.insert(new ProjectBodyVO[] { newbodyvo });
			}

		} catch (Exception e) {
			Logger.error("~~~ProjectDocSyn~~~，" + e.getMessage(), e);
		}
	}

	private void writeLog(int batchstatus, String message) {
		ProjectBatchLogVO projectBatchLogVO = aggProjectBatchLogVO
				.getParentVO();
		projectBatchLogVO.setAttributeValue("batchstatus", batchstatus);
		projectBatchLogVO.setAttributeValue("message", message);
		projectBatchLogVO.setAttributeValue("dealtime", new UFDateTime());
		VOUpdate<ProjectBatchLogVO> voUpdate = new VOUpdate<ProjectBatchLogVO>();
		voUpdate.update(new ProjectBatchLogVO[] { projectBatchLogVO });

		ProjectBatchLogDetailVO[] projectBatchLogDetailVOs = (ProjectBatchLogDetailVO[]) aggProjectBatchLogVO
				.getChildren(ProjectBatchLogDetailVO.class);
		VOUpdate<ProjectBatchLogDetailVO> voDetailUpdate = new VOUpdate<ProjectBatchLogDetailVO>();
		voDetailUpdate.update(projectBatchLogDetailVOs);
	}

	private BaseDAO getDao() {
		return dao;
	}

}
