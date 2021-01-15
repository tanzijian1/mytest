package nc.bs.tg.singleissue.ace.rule;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.GroupCreditVO;
/**
 * �������ķ�������ʱ�������������Ĭ���ļ�����
 * @author wenjie
 *
 */
public class SingleIssueFileRule implements IRule<AggSingleIssueVO>{

	@Override
	public void process(AggSingleIssueVO[] vos) {
		for (AggSingleIssueVO aggVO : vos) {
			GroupCreditVO[] gvos = (GroupCreditVO[])aggVO.getChildren(GroupCreditVO.class);
			if(gvos != null){
				for (GroupCreditVO gvo : gvos) {
					if("Y".equals(gvo.getDef3()) && gvo.getDef4()==null){
						ExceptionUtils.wrappBusinessException("���������š�ѡ��ռ������ʱ��������дռ�����Ž��");
					}
				}
			}
		}
		
		
		// TODO �Զ����ɵķ������
		String pk_appro = vos[0].getParentVO().getPrimaryKey();
		IFileSystemService file = NCLocator.getInstance().lookup(IFileSystemService.class);
		String userId = InvocationInfoProxy.getInstance().getUserId();
		try {
			if(file.queryNCFileNodeTree(pk_appro+"/1.�����걨�ļ�") == null){
				file.createNewFileNode(pk_appro, "1.�����걨�ļ�", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/2.���й����ļ�") == null){
				file.createNewFileNode(pk_appro, "2.���й����ļ�", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/3.�н����Э��") == null){
				file.createNewFileNode(pk_appro, "3.�н����Э��", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/4.�������Э��") == null){
				file.createNewFileNode(pk_appro, "4.�������Э��", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/5.ծȯ����Э��") == null){
				file.createNewFileNode(pk_appro, "5.ծȯ����Э��", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/6.ծȯ��Ϣ�ļ�") == null){
				file.createNewFileNode(pk_appro, "6.ծȯ��Ϣ�ļ�", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/7.ծȯ�����ļ�") == null){
				file.createNewFileNode(pk_appro, "7.ծȯ�����ļ�", userId);
			};
			if(file.queryNCFileNodeTree(pk_appro+"/8.ծȯ���ڶҸ��ļ�") == null){
				file.createNewFileNode(pk_appro, "8.ծȯ���ڶҸ��ļ�", userId);
			};
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

}
