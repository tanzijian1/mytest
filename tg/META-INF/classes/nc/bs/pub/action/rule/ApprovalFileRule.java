package nc.bs.pub.action.rule;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.BusinessException;
import nc.vo.tg.sealflow.AggSealFlowVO;

public class ApprovalFileRule implements IRule<AggSealFlowVO> {

	public void process(AggSealFlowVO[] vos) {
		// TODO �Զ����ɵķ������
		String pk_appro = vos[0].getParentVO().getPrimaryKey();
		IFileSystemService file = NCLocator.getInstance().lookup(
				IFileSystemService.class);
		String userId = InvocationInfoProxy.getInstance().getUserId();
		try {
			if (file.queryNCFileNodeTree(pk_appro + "/�������ĸ��¸���") == null) {
				file.createNewFileNode(pk_appro, "�������ĸ��¸���", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/�ϴ����°�") == null) {
				file.createNewFileNode(pk_appro, "�ϴ����°�", userId);
			}
			;
			/*if (file.queryNCFileNodeTree(pk_appro + "/�н����Э��") == null) {
				file.createNewFileNode(pk_appro, "�н����Э��", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/�������Э��") == null) {
				file.createNewFileNode(pk_appro, "�������Э��", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/ծȯ����Э��") == null) {
				file.createNewFileNode(pk_appro, "ծȯ����Э��", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/ծȯ��Ϣ�ļ�") == null) {
				file.createNewFileNode(pk_appro, "ծȯ��Ϣ�ļ�", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/ծȯ�����ļ�") == null) {
				file.createNewFileNode(pk_appro, "ծȯ�����ļ�", userId);
			}
			;
			if (file.queryNCFileNodeTree(pk_appro + "/ծȯ���ڶҸ��ļ�") == null) {
				file.createNewFileNode(pk_appro, "ծȯ���ڶҸ��ļ�", userId);
			}*/
			;
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
}