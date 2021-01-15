package nc.impl.tg.outside;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
//import nc.itf.pmpub.project.pub.IProjectServiceForPu;
import nc.itf.pmpub.prv.IProjectQuery;
import nc.itf.tg.outside.IProjectDocSyn;
import nc.vo.pmpub.project.ProjectHeadVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.tg.projectbatchlog.AggProjectBatchLogVO;
import nc.vo.tg.projectbatchlog.ProjectBatchLogDetailVO;
import nc.vo.tg.projectbatchlog.ProjectBatchLogVO;

public class InitProjectDocRunnable implements Runnable {

	private AggProjectBatchLogVO aggProjectBatchLogVO;

	public InitProjectDocRunnable(AggProjectBatchLogVO aggProjectBatchLogVO) {
		this.aggProjectBatchLogVO = aggProjectBatchLogVO;
	}

	@Override
	public void run() {
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(IProjectDocSyn.NC_DataSourceName);
			InvocationInfoProxy.getInstance().setUserId(IProjectDocSyn.SaleUserId);
			InvocationInfoProxy.getInstance().setUserCode(IProjectDocSyn.SaleOperatorName);
			InvocationInfoProxy.getInstance().setGroupId(IProjectDocSyn.GroupId);
			ProjectBatchLogDetailVO[] projectBatchLogDetailVOs = (ProjectBatchLogDetailVO[]) aggProjectBatchLogVO
					.getChildren(ProjectBatchLogDetailVO.class);
			if ((projectBatchLogDetailVOs != null)
					&& (projectBatchLogDetailVOs.length > 0)) {
				IProjectQuery projectService = NCLocator.getInstance().lookup(
						IProjectQuery.class);
				// IProjectServiceForPu projectServicePu =
				// NCLocator.getInstance()
				// .lookup(IProjectServiceForPu.class);
				Logger.info("项目档案房间内码初始处理：开始读取项目档案");
				ProjectHeadVO[] projectHeadVOs = projectService
						.queryProjectHeadVOsByCondition("pk_org = '"
								+ IProjectDocSyn.GroupId
								+ "' and pk_projectclass = '"
								+ IProjectDocSyn.ProjectClass04 + "' ");
				Logger.info("项目档案房间内码初始处理：读取项目档案:" + projectHeadVOs.length);
				if ((projectHeadVOs == null) || (projectHeadVOs.length == 0)) {
					return;
				}
				VOUpdate<ProjectHeadVO> voProjectUpdate = new VOUpdate<ProjectHeadVO>();
				for (ProjectBatchLogDetailVO projectBatchLogDetailVO : projectBatchLogDetailVOs) {
					String errMessage = validProjectDetail(projectBatchLogDetailVO);
					if (PubAppTool.isNull(errMessage)) {
						String project_code = (String) projectBatchLogDetailVO
								.getAttributeValue("project_code");
						String def2 = (String) projectBatchLogDetailVO
								.getAttributeValue("def2");
						boolean found = false;
						for (ProjectHeadVO projectHeadVO : projectHeadVOs) {
							if (projectHeadVO.getProject_code().equals(
									project_code)) {
								projectHeadVO.setDef2(def2);
								voProjectUpdate
										.update(new ProjectHeadVO[] { projectHeadVO });
								found = true;
								break;
							}
						}
						if (found) {
							projectBatchLogDetailVO.setAttributeValue(
									"detailstatus", 1);
						} else {
							projectBatchLogDetailVO.setAttributeValue(
									"detailstatus", 2);
							projectBatchLogDetailVO.setAttributeValue(
									"message", "没有匹配到对应的project_code");
						}
					} else {
						projectBatchLogDetailVO.setAttributeValue(
								"detailstatus", 2);
						projectBatchLogDetailVO.setAttributeValue("message",
								errMessage);
					}
				}

				ProjectBatchLogVO projectBatchLogVO = aggProjectBatchLogVO
						.getParentVO();
				projectBatchLogVO.setAttributeValue("batchstatus", 1);
				projectBatchLogVO.setAttributeValue("dealtime",
						new UFDateTime());
				VOUpdate<ProjectBatchLogVO> voUpdate = new VOUpdate<ProjectBatchLogVO>();
				voUpdate.update(new ProjectBatchLogVO[] { projectBatchLogVO });
				VOUpdate<ProjectBatchLogDetailVO> voDetailUpdate = new VOUpdate<ProjectBatchLogDetailVO>();
				voDetailUpdate.update(projectBatchLogDetailVOs);

			}
			Logger.info("项目档案房间内码初始处理完成。");
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
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

}
