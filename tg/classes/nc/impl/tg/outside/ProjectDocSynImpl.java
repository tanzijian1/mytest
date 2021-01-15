package nc.impl.tg.outside;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.execute.Executor;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.bill.BillInsert;
import nc.impl.pubapp.pattern.data.vo.VOInsert;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.tg.outside.IProjectDocSyn;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.tg.projectbatchlog.AggProjectBatchLogVO;
import nc.vo.tg.projectbatchlog.InitialProjectDoc;
import nc.vo.tg.projectbatchlog.ProjectBatchLogDetailVO;
import nc.vo.tg.projectbatchlog.ProjectBatchLogVO;
import nc.vo.tg.projectbatchlog.ProjectDocParam;

public class ProjectDocSynImpl implements IProjectDocSyn {

	@Override
	public void initProjectDoc() throws BusinessException {
		//
		List<String> epsCodes = readInitialEpsList();
		if ((epsCodes == null) || (epsCodes.size() == 0)) {
			throw new BusinessException("读取初始化视图数据异常，读取的epscode集合为空");
		}
		Logger.info("~~~ProjectDocSyn~~~，从期初读取到" + epsCodes.size()
				+ "个epscode；");
		int i = 0;
		for (String epsCode : epsCodes) {
			i++;
			Logger.info("~~~ProjectDocSyn~~~，正在处理第" + i + "个epscode；");
			if (PubAppTool.isNull(epsCode)) {
				continue;
			}
			initProjectDocForOneEpscode(epsCode);
		}

	}

	public void initProjectDocByEpscode(String epscode)
			throws BusinessException {
		Logger.info("~~~ProjectDocSyn~~~，正在处理" + epscode);
		if (PubAppTool.isNull(epscode)) {
			return;
		}
		initProjectDocForOneEpscode(epscode);
	}

	private void initProjectDocForOneEpscode(String epsCode)
			throws BusinessException {
		List<InitialProjectDoc> initialProjectDocs = readInitialProjectDoc(epsCode);
		// 写入批量处理表
		if ((initialProjectDocs != null) && (initialProjectDocs.size() > 0)) {
			Logger.info("~~~ProjectDocSyn~~~，epsCode=" + epsCode
					+ "， 从期初视图读取记录" + initialProjectDocs.size());
			AggProjectBatchLogVO aggProjectBatchLogVO = new AggProjectBatchLogVO();
			ProjectBatchLogVO projectBatchLogVO = new ProjectBatchLogVO();
			aggProjectBatchLogVO.setParent(projectBatchLogVO);
			projectBatchLogVO.setAttributeValue("pk_group", GroupId);
			projectBatchLogVO.setAttributeValue("batchstatus", 0);
			projectBatchLogVO.setAttributeValue("epscode", epsCode);
			projectBatchLogVO.setAttributeValue("fetchtime", new UFDateTime());
			projectBatchLogVO.setAttributeValue("creator", SaleUserId);
			projectBatchLogVO.setAttributeValue("creationtime",
					new UFDateTime());
			List<ProjectBatchLogDetailVO> children = new ArrayList<ProjectBatchLogDetailVO>();
			for (InitialProjectDoc initialProjectDoc : initialProjectDocs) {
				ProjectBatchLogDetailVO projectBatchLogDetailVO = new ProjectBatchLogDetailVO();
				children.add(projectBatchLogDetailVO);
				projectBatchLogDetailVO.setAttributeValue("project_code",
						initialProjectDoc.getProject_code());
				projectBatchLogDetailVO.setAttributeValue("project_name",
						initialProjectDoc.getProject_name());
				projectBatchLogDetailVO.setAttributeValue("epscode",
						initialProjectDoc.getEpscode());
				projectBatchLogDetailVO.setAttributeValue("def2",
						initialProjectDoc.getDef2());
				projectBatchLogDetailVO.setAttributeValue("detailstatus", 0);
			}
			aggProjectBatchLogVO.setChildren(ProjectBatchLogDetailVO.class,
					children.toArray(new ProjectBatchLogDetailVO[0]));

			VOInsert<ProjectBatchLogVO> voLogInsert = new VOInsert<ProjectBatchLogVO>();
			voLogInsert.insert(new ProjectBatchLogVO[] { projectBatchLogVO });

			Logger.info("~~~ProjectDocSyn~~~，epsCode=" + epsCode
					+ "，把内码写入项目档案。");
			InitProjectDoc initService = new InitProjectDoc(epsCode,
					aggProjectBatchLogVO);
			initService.doInitByEpscode();
		}

	}

	@SuppressWarnings("unchecked")
	private List<String> readInitialEpsList() throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT distinct epscode ");
		sb.append("FROM ").append(XS_InitialProjectDocView);
		BaseDAO dao = new BaseDAO(NC_DataSourceName);
		ArrayList<String> result = (ArrayList<String>) dao.executeQuery(
				sb.toString(), new ColumnListProcessor());
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<InitialProjectDoc> readInitialProjectDoc(String epscode)
			throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT project_code, project_name, def2, epscode ");
		sb.append("FROM ").append(XS_InitialProjectDocView);
		sb.append(" where epscode = '" + epscode + "' ");
		BaseDAO dao = new BaseDAO(NC_DataSourceName);
		List<InitialProjectDoc> initialProjectDocs = (List<InitialProjectDoc>) dao
				.executeQuery(sb.toString(), new BeanListProcessor(
						InitialProjectDoc.class));
		return initialProjectDocs;
	}

	@Override
	public void synProjectDoc(String epscode, ProjectDocParam[] projectDocParams)
			throws BusinessException {
		// 写入批量处理表
		AggProjectBatchLogVO aggProjectBatchLogVO = new AggProjectBatchLogVO();
		ProjectBatchLogVO projectBatchLogVO = new ProjectBatchLogVO();
		aggProjectBatchLogVO.setParent(projectBatchLogVO);
		projectBatchLogVO.setAttributeValue("pk_group", GroupId);
		projectBatchLogVO.setAttributeValue("batchstatus", 0);
		projectBatchLogVO.setAttributeValue("epscode", epscode);
		projectBatchLogVO.setAttributeValue("fetchtime", new UFDateTime());
		projectBatchLogVO.setAttributeValue("creator", SaleUserId);
		projectBatchLogVO.setAttributeValue("creationtime", new UFDateTime());
		List<ProjectBatchLogDetailVO> children = new ArrayList<ProjectBatchLogDetailVO>();
		for (ProjectDocParam projectDoc : projectDocParams) {
			ProjectBatchLogDetailVO projectBatchLogDetailVO = new ProjectBatchLogDetailVO();
			children.add(projectBatchLogDetailVO);
			projectBatchLogDetailVO.setAttributeValue("epscode", epscode);
			projectBatchLogDetailVO.setAttributeValue("project_code",
					projectDoc.getProject_code());
			projectBatchLogDetailVO.setAttributeValue("project_name",
					projectDoc.getProject_name());
			projectBatchLogDetailVO.setAttributeValue("def2",
					projectDoc.getDef2());
			projectBatchLogDetailVO.setAttributeValue("orgcode",
					projectDoc.getOrgcode());
			projectBatchLogDetailVO.setAttributeValue("detailstatus", 0);
		}
		aggProjectBatchLogVO.setChildren(ProjectBatchLogDetailVO.class,
				children.toArray(new ProjectBatchLogDetailVO[0]));

		BillInsert<AggProjectBatchLogVO> billInsert = new BillInsert<AggProjectBatchLogVO>();
		billInsert.insert(new AggProjectBatchLogVO[] { aggProjectBatchLogVO });

		Logger.info("~~~ProjectDocSyn~~~，启动线程同步项目档案，epscode=" + epscode);
		try {
			SynProjectDocRunnable run = new SynProjectDocRunnable(
					aggProjectBatchLogVO);
			Executor thread = new Executor(run);
			thread.start();
		} catch (Exception e) {
			Logger.error(
					"~~~ProjectDocSyn~~~，epscode=" + epscode + "，"
							+ e.getMessage(), e);
		}
	}

	@Override
	public void synProjectDocByPk_projectbatchlog(String pk_projectbatchlog)
			throws BusinessException {
		AggProjectBatchLogVO aggProjectBatchLogVO = (AggProjectBatchLogVO) getMDQueryService()
				.queryBillOfVOByPK(AggProjectBatchLogVO.class,
						pk_projectbatchlog, false);
		ProjectBatchLogVO projectBatchLogVO = aggProjectBatchLogVO
				.getParentVO();
		projectBatchLogVO.setAttributeValue("batchstatus", 0);
		projectBatchLogVO.setAttributeValue("message", null);
		ProjectBatchLogDetailVO[] projectBatchLogDetailVOs = (ProjectBatchLogDetailVO[]) aggProjectBatchLogVO
				.getChildren(ProjectBatchLogDetailVO.class);
		for (ProjectBatchLogDetailVO projectBatchLogDetailVO : projectBatchLogDetailVOs) {
			projectBatchLogDetailVO.setAttributeValue("detailstatus", 0);
			projectBatchLogDetailVO.setAttributeValue("message", null);
		}
		VOUpdate<ProjectBatchLogVO> voUpdate = new VOUpdate<ProjectBatchLogVO>();
		voUpdate.update(new ProjectBatchLogVO[] { projectBatchLogVO });
		VOUpdate<ProjectBatchLogDetailVO> voDetailUpdate = new VOUpdate<ProjectBatchLogDetailVO>();
		voDetailUpdate.update(projectBatchLogDetailVOs);

		Logger.info("~~~ProjectDocSyn~~~，启动线程同步项目档案，pk_projectbatchlog="
				+ pk_projectbatchlog);
		try {
			SynProjectDocRunnable run = new SynProjectDocRunnable(
					aggProjectBatchLogVO);
			Executor thread = new Executor(run);
			thread.start();
		} catch (Exception e) {
			Logger.error("~~~ProjectDocSyn~~~，pk_projectbatchlog="
					+ pk_projectbatchlog + "，" + e.getMessage(), e);
		}
	}

	private static IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}
}
