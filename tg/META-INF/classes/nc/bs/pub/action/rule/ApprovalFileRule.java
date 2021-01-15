package nc.bs.pub.action.rule;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.BusinessException;
import nc.vo.tg.sealflow.AggSealFlowVO;

public class ApprovalFileRule implements IRule<AggSealFlowVO> {

	public void process(AggSealFlowVO[] vos) {
		// TODO 自动生成的方法存根
		String pk_appro = vos[0].getParentVO().getPrimaryKey();
		IFileSystemService file = NCLocator.getInstance().lookup(
				IFileSystemService.class);
		String userId = InvocationInfoProxy.getInstance().getUserId();
		try {
			if (file.queryNCFileNodeTree(pk_appro + "/流程中心盖章附件") == null) {
				file.createNewFileNode(pk_appro, "流程中心盖章附件", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/上传盖章版") == null) {
				file.createNewFileNode(pk_appro, "上传盖章版", userId);
			}
			;
			/*if (file.queryNCFileNodeTree(pk_appro + "/中介机构协议") == null) {
				file.createNewFileNode(pk_appro, "中介机构协议", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/财务顾问协议") == null) {
				file.createNewFileNode(pk_appro, "财务顾问协议", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/债券销售协议") == null) {
				file.createNewFileNode(pk_appro, "债券销售协议", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/债券付息文件") == null) {
				file.createNewFileNode(pk_appro, "债券付息文件", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/债券回售文件") == null) {
				file.createNewFileNode(pk_appro, "债券回售文件", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/债券到期兑付文件") == null) {
				file.createNewFileNode(pk_appro, "债券到期兑付文件", userId);
			}*/
			;
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
}