package nc.impl.tg.outside;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
//import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
//import nc.itf.pmpub.project.pub.IProjectServiceForPu;
//import nc.itf.pmpub.prv.IProjectQuery;
import nc.itf.tg.outside.IProjectDocSyn;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pmpub.project.ProjectHeadVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.tg.projectbatchlog.AggProjectBatchLogVO;
import nc.vo.tg.projectbatchlog.ProjectBatchLogDetailVO;
import nc.vo.tg.projectbatchlog.ProjectBatchLogVO;

public class InitProjectDoc {

	private BaseDAO dao = new BaseDAO();
	private String epscode;
	private AggProjectBatchLogVO aggProjectBatchLogVO;

	public InitProjectDoc(String epscode,
			AggProjectBatchLogVO aggProjectBatchLogVO) {
		this.epscode = epscode;
		this.aggProjectBatchLogVO = aggProjectBatchLogVO;
	}

	@SuppressWarnings("unchecked")
	public void doInitByEpscode() throws BusinessException {
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(
					IProjectDocSyn.NC_DataSourceName);
			InvocationInfoProxy.getInstance().setUserId(
					IProjectDocSyn.SaleUserId);
			InvocationInfoProxy.getInstance().setUserCode(
					IProjectDocSyn.SaleOperatorName);
			InvocationInfoProxy.getInstance()
					.setGroupId(IProjectDocSyn.GroupId);
			ProjectBatchLogVO projectBatchLogVO = aggProjectBatchLogVO
					.getParentVO();
			String errorMessage = validProjectHead(projectBatchLogVO);
			if (!PubAppTool.isNull(errorMessage)) {
				writeLog(2, errorMessage);
				return;
			}
			String[] strs = ((String) projectBatchLogVO
					.getAttributeValue("epscode")).split("-");
			StringBuffer epscode = new StringBuffer();
			epscode.append("04");
			for (String str : strs) {
				epscode.append(str);
			}
			String eps = (String) getDao().executeQuery(
					"select pk_eps from pm_eps where eps_code='" + epscode
							+ "'", new ColumnProcessor());
			if (eps == null || eps == "") {
				writeLog(2, "无法在nc找到对应的EPS");
				return;
			}
			ProjectBatchLogDetailVO[] projectBatchLogDetailVOs = (ProjectBatchLogDetailVO[]) aggProjectBatchLogVO
					.getChildren(ProjectBatchLogDetailVO.class);
			if ((projectBatchLogDetailVOs != null)
					&& (projectBatchLogDetailVOs.length > 0)) {

				// IProjectQuery projectService =
				// NCLocator.getInstance().lookup(
				// IProjectQuery.class);
				// IProjectServiceForPu projectServicePu =
				// NCLocator.getInstance()
				// .lookup(IProjectServiceForPu.class);
				Logger.info("~~~ProjectDocSyn~~~，epscode=" + this.epscode
						+ "，把内码写入项目档案初始处理：开始读取项目档案");
				String sql = "select * from bd_project where (deletestate is null or deletestate<>1) and nvl(dr,0) = 0 and pk_org = '"
						+ IProjectDocSyn.GroupId
						+ "' and pk_projectclass = '"
						+ IProjectDocSyn.ProjectClass04
						+ "' and pk_eps = '"
						+ eps + "'";
				List<ProjectHeadVO> projectHeadVOs = (List<ProjectHeadVO>) getDao()
						.executeQuery(sql,
								new BeanListProcessor(ProjectHeadVO.class));
				// ProjectHeadVO[] projectHeadVOs = projectService
				// .queryProjectHeadVOsByCondition("pk_org = '"
				// + IProjectDocSyn.GroupId
				// + "' and pk_projectclass = '"
				// + IProjectDocSyn.ProjectClass04
				// + "' and pk_eps = '" + eps + "'");
				if ((projectHeadVOs == null) || (projectHeadVOs.size() == 0)) {
					writeLog(1, null);
					return;
				}
				Logger.info("~~~ProjectDocSyn~~~，epscode=" + this.epscode
						+ "，把内码写入项目档案初始处理：读取项目档案:" + projectHeadVOs.size());
				VOUpdate<ProjectHeadVO> voProjectUpdate = new VOUpdate<ProjectHeadVO>();
				Map<String, ProjectBatchLogDetailVO> projectDetailMap = new HashMap<String, ProjectBatchLogDetailVO>();
				for (ProjectBatchLogDetailVO projectBatchLogDetailVO : projectBatchLogDetailVOs) {
					errorMessage = validProjectDetail(projectBatchLogDetailVO);
					if (!PubAppTool.isNull(errorMessage)) {
						writeLog(2, errorMessage);
						return;
					}
					String project_code = (String) projectBatchLogDetailVO
							.getAttributeValue("project_code");
					projectDetailMap.put(project_code, projectBatchLogDetailVO);
				}

				for (ProjectHeadVO projectHeadVO : projectHeadVOs) {
					String project_code = projectHeadVO.getProject_code();
					if (projectDetailMap.containsKey(project_code)) {
						String def2 = (String) projectDetailMap.get(
								project_code).getAttributeValue("def2");
						projectHeadVO.setDef2(def2);
						voProjectUpdate
								.update(new ProjectHeadVO[] { projectHeadVO });
					}
				}

				writeLog(1, null);

			}
			Logger.info("~~~ProjectDocSyn~~~，把内码写入项目档案初始处理完成，epscode="
					+ this.epscode);
		} catch (Exception e) {
			Logger.error("~~~ProjectDocSyn~~~，，把内码写入项目档案初始处理异常终止，epscode="
					+ this.epscode + "，" + e.getMessage(), e);
			throw new BusinessException(e);
		}
	}

	private String validProjectDetail(
			ProjectBatchLogDetailVO projectBatchLogDetailVO) {
		String project_code = (String) projectBatchLogDetailVO
				.getAttributeValue("project_code");
		String def2 = (String) projectBatchLogDetailVO
				.getAttributeValue("def2");
		if (PubAppTool.isNull(project_code)) {
			return "project_code不允许为空";
		}
		if (PubAppTool.isNull(def2)) {
			return "def2不允许为空";
		}
		return null;
	}

	private String validProjectHead(ProjectBatchLogVO projectBatchLogVO) {
		String epscode = (String) projectBatchLogVO
				.getAttributeValue("epscode");
		if (PubAppTool.isNull(epscode)) {
			return "epscode不允许为空";
		}
		return null;
	}

	private void writeLog(int batchstatus, String message) {
		ProjectBatchLogVO projectBatchLogVO = aggProjectBatchLogVO
				.getParentVO();
		projectBatchLogVO.setAttributeValue("batchstatus", batchstatus);
		projectBatchLogVO.setAttributeValue("message", message);
		projectBatchLogVO.setAttributeValue("dealtime", new UFDateTime());
		VOUpdate<ProjectBatchLogVO> voUpdate = new VOUpdate<ProjectBatchLogVO>();
		voUpdate.update(new ProjectBatchLogVO[] { projectBatchLogVO });

	}

	private BaseDAO getDao() {
		return dao;
	}
}
